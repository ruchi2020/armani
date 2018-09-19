/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 04-19-2005 | Sumit     | Original         | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.appmgr.menu.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.register.CMSRegister;


/**
 * <p>Title: ShowIfBackOfficeRegister</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:SkillNet Inc. </p>
 * @author
 * @version 1.0
 */
public class ShowIfMasterRegister extends Rule {

  public ShowIfMasterRegister() {
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
        if (registerType.equalsIgnoreCase("M")) {
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
    return "ShowIfMasterRegister";
  }
}
