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


/**
 * put your documentation comment here
 */
public class LocalCheckIsValidAsChange extends Rule {

  /**
   * put your documentation comment here
   */
  public LocalCheckIsValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of LocalCheck
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((LocalCheck)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of LocalCheck
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(LocalCheck localcheck, PaymentTransaction paymentTransaction1) {
    try {
      // place business logic here
      if (paymentTransaction1 instanceof CMSPaidOutTransaction) {
        if (((CMSPaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
            "MISC_PAID_OUT")) {
          return new RulesInfo();
        }
      } else
        return new RulesInfo("Not a Valid type of Change");
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return new RulesInfo();
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "LocalCheckIsValidAsChange";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("LocalCheck is not a valid as change");
    return buf.toString();
  }
}

