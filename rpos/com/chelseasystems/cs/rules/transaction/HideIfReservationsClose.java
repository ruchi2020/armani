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
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;


/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 10-05-2005 | Manpreet  | N/A              | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 */
/**
 * <p>Title: HideIfReservationsClose</p>
 * <p>Description: This business rule check whether SaleMode is RESERVATIONS_CLOSE
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:SkillNet Inc. </p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class HideIfReservationsClose extends Rule {

  /**
   * Default Constructor
   */
  public HideIfReservationsClose() {
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
    return execute((CMSMenuOption)theParent, (Employee)args[0], (Store)args[1]);
  }


  /**
   * Execute the Rule
   * @param cmsmenuoption theParent
   * @param employee Employee
   * @param store Store
   * @return RulesInfo
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {
    try {
      if (CMSApplet.theAppMgr.getStateObject("TXN_MODE") != null) {
        int iMode = ((Integer)CMSApplet.theAppMgr.getStateObject("TXN_MODE")).intValue();
        if (iMode == InitialSaleApplet.RESERVATIONS_CLOSE)
          return new RulesInfo("HIDE BUTTON");
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
    return "HideIfReservationsClose";
  }
}
