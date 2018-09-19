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


/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 10-03-2005 | Manpreet  | N/A              | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 */
/**
 * <p>Title: ShowIfReservationsClose</p>
 *
 * <p>Description: This business rule check whether SaleMode is RESERVATIONS_CLOSE
 * if it is then only ADJUST_DEPOSIT button will show up</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ShowIfReservationsClose extends Rule {

  public static final int RESERVATIONS_OPEN = 9;
  public static final int RESERVATIONS_CLOSE = 10;
  public static final int NO_OPEN_RESERVATIONS_CLOSE_SALE = 11;
  public static final int NO_OPEN_RESERVATIONS_CLOSE_RETURN = 12;
  /**
   * Default Constructor
   */
  public ShowIfReservationsClose() {
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
        if (iMode != RESERVATIONS_CLOSE && iMode != this.NO_OPEN_RESERVATIONS_CLOSE_RETURN && iMode!= this.NO_OPEN_RESERVATIONS_CLOSE_SALE)
          return new RulesInfo("HIDE BUTTON");
      } else {
        return new RulesInfo("HIDE BUTTON");
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return new RulesInfo("Cant print");
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
    return "ShowIfReservationsClose";
  }
}
