/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 * <p>Title: CashRenderer</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CashRenderer extends PaymentRenderer {
  StringBuffer buf = null;

  /**
   * Default Constructor
   */
  public CashRenderer() {
  }
  /* public String getGUIPaymentDetail () {
   if(getPayment() instanceof Coupon)
   if(((Coupon)getPayment()).getType() != null)
   buf = new StringBuffer(((Coupon)getPayment()).getType());
   else buf = new StringBuffer("");
   return buf.toString();

   }*/
}
