/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.PaymentRenderer;
import com.chelseasystems.cr.payment.*;


/**
 * put your documentation comment here
 */
public class RedeemableRenderer extends PaymentRenderer {

  /**
   * Default Constructor
   */
  public RedeemableRenderer() {
  }

  /**
   * This method is used to get payment detail given by user
   *
   * @return String
   */
  public String getGUIPaymentDetail() {
    StringBuffer buf = null;
    Payment payment = this.getPayment();
    if (payment instanceof StoreValueCard) {
      buf = new StringBuffer(((StoreValueCard)payment).getControlNum());
    } else if (payment instanceof HouseAccount) {
      buf = new StringBuffer(((HouseAccount)payment).getControlNum());
    } else if (payment instanceof CMSDueBillIssue) {
      buf = new StringBuffer(((CMSDueBillIssue)payment).getControlNum());
    } else if (payment instanceof DueBill) {
      buf = new StringBuffer(((DueBill)payment).getId());
    } else if (payment instanceof MallCert) {
      buf = new StringBuffer(((MallCert)payment).getDesc());
    } else if (payment instanceof CMSRedeemable) {
      buf = new StringBuffer(((CMSRedeemable)payment).getControlNum());
    }
    buf.append(" " + super.getGUIPaymentDetail());
    return buf.toString();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getMaskedPaymentDetail() {
    return getGUIPaymentDetail();
  }
}
