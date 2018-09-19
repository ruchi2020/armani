/*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.payment.RoundPayment;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;


/**
 * <p>Title: RoundPaymentNotValidAsRefundOrChange_EUR</p>
 * <p>Description: This business rule check that RoundPayment is not a valid
 * form as Refund or Change payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sumit Krishnan
 * @version 1.0
 */
public class RoundPaymentNotValidAsRefundOrChange extends Rule {

  /**
   * put your documentation comment here
   */
  public RoundPaymentNotValidAsRefundOrChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of RoundPayment
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return (execute((RoundPayment)theParent, (PaymentTransaction)args[0]));
  }

  /**
   * Execute the Rule
   * @param theParent - instance of RoundPayment
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(RoundPayment roundPayment, PaymentTransaction paymentTransaction) {
    try {
        return (new RulesInfo(CMSApplet.res.getString("Round Payment Cannot be used as Change or Refund")));
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return (new RulesInfo());
    }
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return (CMSApplet.res.getString("RoundPaymentNotValidAsRefundOrChange_EUR"));
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("");
    return (buf.toString());
  }
}

