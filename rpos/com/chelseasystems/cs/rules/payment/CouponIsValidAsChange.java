/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;

import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.util.Version;


/**
 * <p>Title: CouponIsValidAsChange</p>
 * <p>Description: This business rule checks that Coupon is not a valid form as
 * change Payment </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class CouponIsValidAsChange extends Rule {

  /**
   * Default construtor
   */
  public CouponIsValidAsChange() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of GiftCert
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
	  // Change made by Satin for coupon management PCR
	  if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
		    return execute((CMSCoupon)theParent, (PaymentTransaction)args[0]);
	  }
	  else{
		  return execute((Coupon)theParent, (PaymentTransaction)args[0]);
	  }
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMScoupon
   * @param args - instance of PaymentTransaction
   * 
   */
  // Changed by Satin for coupon management PCR.
  private RulesInfo execute(CMSCoupon coupon, PaymentTransaction paymenttransaction) {
    try {
        return new RulesInfo("Coupon Not valid as Change");
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
   * 
   */
  private RulesInfo execute(Coupon coupon, PaymentTransaction paymenttransaction) {
	    try {
	        return new RulesInfo("Coupon Not valid as Change");
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
    return "Coupon is valid as change payment";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("This rule should determine whether a ");
    buf.append(" coupon is valid as a change payment.");
    return buf.toString();
  }
}

