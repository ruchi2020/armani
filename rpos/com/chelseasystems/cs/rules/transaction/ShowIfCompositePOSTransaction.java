/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cs.swing.pos.InitialSaleApplet;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.appmgr.menu.CMSMenuOption;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.rules.Rule;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ShowIfCompositePOSTransaction extends Rule {

  /**
   * put your documentation comment here
   */
  public ShowIfCompositePOSTransaction() {
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
      if (CMSApplet.theAppMgr.getStateObject("TXN_POS") != null) {
        PaymentTransaction theTxn = (PaymentTransaction)CMSApplet.theAppMgr.getStateObject(
            "TXN_POS");
        if (theTxn instanceof CMSCompositePOSTransaction)
          return new RulesInfo();
      }
      else if(CMSApplet.theAppMgr.getStateObject("THE_TXN") !=null)
      {
        PaymentTransaction theTxn = (PaymentTransaction)CMSApplet.theAppMgr.getStateObject(
            "THE_TXN");
        if (theTxn instanceof CMSCompositePOSTransaction)
          return new RulesInfo();
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return new RulesInfo("Cant print");
    }
    return new RulesInfo("Applicable only for Composite Transaction");
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
    return "ShowIfCompositePOSTransaction";
  }
}

