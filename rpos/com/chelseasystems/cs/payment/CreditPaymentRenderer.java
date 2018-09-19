/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 * <p>Title: RoundPaymentRenderer</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CreditPaymentRenderer extends PaymentRenderer {
  StringBuffer buf = null;

  /**
   * Default Constructor
   */
  public CreditPaymentRenderer() {
  }

  /**
   *
   * @return String
   */
  public String getGUIPaymentDetail() {
    buf = new StringBuffer("");
    return buf.toString();
  }
}
