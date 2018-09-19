/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 04-19-2005 | Anand     | Original         | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.register;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.register.CMSRegister;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Enumeration;
import com.chelseasystems.cs.employee.CMSEmployee;


/**
 * <p>Title: ShowIfBackOfficeRegister</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:SkillNet Inc. </p>
 * @author
 * @version 1.0
 */
public class ShowIfBackOfficeRegister extends Rule {
  private ConfigMgr config = null;
  private String strRoleList = null;
  private Vector vecRoles = null;
  private StringTokenizer st = null;

  /**
   * To find out if the employee who is logging in is a manager
   * @return boolean
   */
  /* private boolean isLoggedInEmployeeManager(){
   config = new ConfigMgr("employee.cfg");
   strRoleList = config.getString("ROLE_LIST");
   vecRoles = null;
   StringTokenizer st = new StringTokenizer(strRoleList, ",");
   while (st.hasMoreTokens()) {
   String role = st.nextToken();
   vecRoles.add(role);
   }
   Enumeration enumRoleList = vecRoles.elements();
   CMSEmployee employee = (CMSEmployee)CMSApplet.theAppMgr.getStateObject("OPERATOR");
   Vector vecCurrEmployeeRoles = employee.hasWhichAccessRoles(enumRoleList);
   if(vecCurrEmployeeRoles.contains("MANAGER"))
   return true;
   return false;
   }*/
  public ShowIfBackOfficeRegister() {
  }

  /**
   * Execute the rule.
   *
   * @param object theParent - instance of CMSMenuOption
   * @param args - instance of Employee
   * @param args - instance of Store
   * @return RulesInfo
   */
  public RulesInfo execute(Object theParent, Object[] args) {
    return execute((CMSMenuOption)theParent);
  }

  /**
   * Execute the Rule
   * @param cmsmenuoption theParent
   * @param employee Employee
   * @param store Store
   * @return RulesInfo
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption) {
    try {
      String registerType = (String)(((CMSRegister)CMSApplet.theAppMgr.getGlobalObject("REGISTER")).
          getRegisterType());
      if (registerType != null && registerType.length() > 0) {
        if (registerType.equalsIgnoreCase("B")
            /*&& isLoggedInEmployeeManager()*/
            ) {
          return new RulesInfo();
        } else {
          return new RulesInfo("should NOT be enabled");
        }
      } else {
        return new RulesInfo("should NOT be enabled");
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return new RulesInfo();
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return ("Suppress menu button if not appropriate.");
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return "ShowIfBackOfficeRegister";
  }
}

