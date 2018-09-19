/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: CouponIsValidAsRefund</p>
 * <p>Description: This business rule checks that Coupon is not valid form as
 * refund </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author
 * @version 1.0
 */
public class CouponIsValidAsRefund extends Rule {

  /**
   * put your documentation comment here
   */
  public CouponIsValidAsRefund() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of Coupon or CMSCoupon
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
	  if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
		  return execute((CMSCoupon)theParent, (PaymentTransaction)args[0]);
	  }
	  else{
		  return execute((Coupon)theParent, (PaymentTransaction)args[0]);
	  }
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCoupon
   * @param args - instance of PaymentTransaction
   */
  // Changed by Satin for Coupon management PCR.
  private RulesInfo execute(CMSCoupon coupon, PaymentTransaction paymenttransaction) {
    try {
      // place business logic here
      return new RulesInfo("Coupon is not valid as refund.");
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return new RulesInfo();
    }
  }
  
  
  /**
   * Execute the Rule
   * @param theParent - instance of Coupon
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(Coupon coupon, PaymentTransaction paymenttransaction) {
	    try {
	      // place business logic here
	      return new RulesInfo("Coupon is not valid as refund.");
	    } catch (Exception ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
	          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	      return new RulesInfo();
	    }
	  }
  
  

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "Coupin is valid as refund";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("");
    return buf.toString();
  }
}

