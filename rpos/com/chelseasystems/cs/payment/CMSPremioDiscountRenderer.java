package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.PaymentRenderer;

public class CMSPremioDiscountRenderer extends PaymentRenderer {

	StringBuffer buf = null;

	  public CMSPremioDiscountRenderer() {
	  }

	  /**
	   *
	   * @return String
	   */
	  public String getGUIPaymentDetail() {
	    if (getPayment() instanceof CMSPremioDiscount)
	      if (((CMSPremioDiscount)getPayment()).getDesc() != null)
	        buf = new StringBuffer(((CMSPremioDiscount)getPayment()).getDesc());
	      else
	        buf = new StringBuffer("");
	    return buf.toString();
	  }
	
}
