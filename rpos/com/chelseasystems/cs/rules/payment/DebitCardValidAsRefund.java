
/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.pos.*;

import com.chelseasystems.cr.payment.DebitCard;
/**
 * put your documentation comment here
 */
public class DebitCardValidAsRefund extends Rule {

  /**
   * put your documentation comment here
   */
  public DebitCardValidAsRefund() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Credit
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((DebitCard)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Credit
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(DebitCard credit, PaymentTransaction paymentTransaction) {
        return new RulesInfo("Not a Valid Refund");
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "DebitCardValidAsRefund";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Debit Card  is not a valid type of refund");
    return buf.toString();
  }
}


