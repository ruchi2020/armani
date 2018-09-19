/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.appmgr.menu.*;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;


/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 08-21-2006 | Sandhya   | 1641             | Request for reservation close|
 +--------+------------+-----------+------------------+------------------------------+
 */
/**
 * <p>Title: HideReserveOptionIfReservationsClose</p>
 * <p>Description: This class checks the business rule to hide RESERVE button when SaleMode is RESERVATIONS_CLOSE
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:SkillNet Inc. </p>
 * @author Sandhya Ajit
 * @version 1.0
 */
public class HideReserveButtonIfReservationClose extends Rule {

  /**
   * Default Constructor
   */
  public HideReserveButtonIfReservationClose() {
  }


  /**
   * Execute the Rule
   * @param theParent Object
   * @param args Object[]
   */
  public RulesInfo execute(Object theParent, Object args[]) {
	  return execute((CMSMenuOption)theParent, (Employee)args[0], (Store)args[1]);
  }
  
  /**
   * Execute the rule
   * @param cmsmenuoption
   * @param employee
   * @param store
   * @return
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {	  
	  if (CMSApplet.theAppMgr.getStateObject("TXN_MODE") != null) {
		  int iMode = ((Integer)CMSApplet.theAppMgr.getStateObject("TXN_MODE")).intValue();
			  
		  if (iMode == InitialSaleApplet.RESERVATIONS_CLOSE) {			  
			  return new RulesInfo("Hide RESERVE button for reservation close for Japan.");			  
		  }
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
    return "HideReserveButtonIfReservationClose";
  }
}
