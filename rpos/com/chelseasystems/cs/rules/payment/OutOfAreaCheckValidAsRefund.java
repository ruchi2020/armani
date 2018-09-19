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


/**
 * put your documentation comment here
 */
public class OutOfAreaCheckValidAsRefund extends Rule {

  /**
   * put your documentation comment here
   */
  public OutOfAreaCheckValidAsRefund() {
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
      return new RulesInfo("Out of Area is not a valid type of Refund");
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "OutOfAreaCheckValidAsRefund";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("OutOfArea check is not a valid refund type");
    return buf.toString();
  }
}

