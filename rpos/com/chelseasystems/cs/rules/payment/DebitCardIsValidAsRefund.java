/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.DebitCard;


/**
 * put your documentation comment here
 */
public class DebitCardIsValidAsRefund extends Rule {

  /**
   * put your documentation comment here
   */
  public DebitCardIsValidAsRefund() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSDebitCard
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((DebitCard)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DebitCard
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(DebitCard debitcard, PaymentTransaction paymentTransaction1) {
    try {
      // DebitCard is valid as refund
      return new RulesInfo("Debit card not a valid refund type");
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
    return "DebitCardIsValidAsRefund";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Debit Card is valid as refund");
    return buf.toString();
  }
}

