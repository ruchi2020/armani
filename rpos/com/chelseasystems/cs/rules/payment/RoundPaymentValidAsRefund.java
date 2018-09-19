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
public class RoundPaymentValidAsRefund extends Rule {

  /**
   * put your documentation comment here
   */
  public RoundPaymentValidAsRefund() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of RoundPayment
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((RoundPayment)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of RoundPayment
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(RoundPayment roundpayment, PaymentTransaction paymentTransaction1) {
    try {
      // place business logic here
      return new RulesInfo("Round Payment not valid as refund");
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
    return "RoundPaymentIsValidAsRefund";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Round payment not valid refund type");
    return buf.toString();
  }
}

