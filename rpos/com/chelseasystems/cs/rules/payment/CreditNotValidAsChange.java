/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

/**
 * put your documentation comment here
 */
public class CreditNotValidAsChange extends Rule {

  /**
   * put your documentation comment here
   */
  public CreditNotValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Credit
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((Credit)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Credit
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(Credit credit, PaymentTransaction paymentTransaction) {
//    try {
      // place business logic here

        return new RulesInfo("Not a Valid Change");
//      if (paymentTransaction instanceof CMSPaidOutTransaction) {
//        if (((CMSPaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
//            "CASH_TRANSFER")
//            || ((CMSPaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
//            "CLOSE_DEPOSIT"))
//          return new RulesInfo("Not a Valid Refund");
//      }else{
//        return new RulesInfo();
//      }
//    } catch (Exception ex) {
//      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
//          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
//    }
//    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "CreditNotValidAsChange";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Credit  is not a valid type of payment");
    return buf.toString();
  }
}

