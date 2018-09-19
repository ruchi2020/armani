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
public class OutOfAreaCheckIsNotValidAsChange extends Rule {

  /**
   * put your documentation comment here
   */
  public OutOfAreaCheckIsNotValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of OutOfAreaCheck
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((OutOfAreaCheck)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of OutOfAreaCheck
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(OutOfAreaCheck outofareacheck, PaymentTransaction paymentTransaction1) {
    try {
      // place business logic here
      if (paymentTransaction1 instanceof CMSPaidOutTransaction) {
        if (((CMSPaidOutTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
            "MISC_PAID_OUT")) {
          return new RulesInfo();
        }
      } else
        return new RulesInfo("Out of Area is not a valid type of Change");
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
    return "OutOfCheckISValidAsChange";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Out of Area is not a valid change type");
    return buf.toString();
  }
}

