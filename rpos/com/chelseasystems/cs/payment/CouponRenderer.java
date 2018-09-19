/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.util.Version;


/**
 * <p>Title: CouponRenderer</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CouponRenderer extends PaymentRenderer {
  StringBuffer buf = null;

  /**
   * put your documentation comment here
   */
  public CouponRenderer() {
  }

  /**
   * Changes made by Satin
   * This method returns description of CMSCoupon and Coupon
   * @return String
   */
  public String getGUIPaymentDetail() {
	  if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
		  if (getPayment() instanceof CMSCoupon)
		      if (((CMSCoupon)getPayment()).getDesc() != null)
		        {buf = new StringBuffer(((CMSCoupon)getPayment()).getDesc());}
		      else
		        buf = new StringBuffer("");
		   
	  }
	  
	  else if (getPayment() instanceof Coupon){
       if (((Coupon)getPayment()).getDesc() != null)
        {buf = new StringBuffer(((Coupon)getPayment()).getDesc());}
      else
        buf = new StringBuffer("");
   
  }
	  return buf.toString();
  }
}
