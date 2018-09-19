/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: HideIfBackOfficeRegister</p>
 *
 * <p>Description: Hide submenus of Item details and Print Gift Receipt buttons in TxnDetailApplet</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Anand Kannan
 * @version 1.0
 */
/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 05-01-2005 | Anand     | Original         | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 */
public class HideIfTxnDetailApplet extends Rule {

  /**
   * put your documentation comment here
   */
  public HideIfTxnDetailApplet() {
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
      String currPanel = (String)CMSApplet.theAppMgr.getStateObject("TXN_DETAIL_CURR_PANEL");
      if (currPanel != null && currPanel.length() > 0) {
        if (currPanel.equalsIgnoreCase("DETAILS")) {
          System.out.println("@@@@@in if @@@@@@");
          return new RulesInfo("should NOT be enabled");
        } else {
          System.out.println("@@@@@in else @@@@@@");
          return new RulesInfo();
        }
      } else {
        return new RulesInfo();
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return new RulesInfo();
  }

  /**
   * Returns a user-friendly version of the rule.
   * @return user-friendly version of the rule
   */
  public String getDesc() {
    return ("Suppress menu button if not appropriate.");
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return "HideIfBackOfficeRegister";
  }
}

