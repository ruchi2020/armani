/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import java.util.Enumeration;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.payment.Coupon;
// Added by Satin.
import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.util.Version;


/**
 */
public class PaymentTxnAddCouponIsValid extends Rule {

  /**
   */
  public PaymentTxnAddCouponIsValid() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCompositePOSTransaction
   * @param args - instance of Payment
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((PaymentTransaction)theParent, (Payment)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCompositePOSTransaction
   * @param args - instance of Payment
   */
  private RulesInfo execute(PaymentTransaction txn, Payment payment) {
    try {
	//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
    	//Changed by Satin for Coupon Management PCR.
    	/*   commentato da Riccardino poichè  i COFIX possono essere multipli.
    	if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
    		if (payment instanceof CMSCoupon) {
    	        Payment[] array = txn.getPaymentsArray();
    	        for (int i = 0; i < array.length; i++) {
    	          if (array[i] instanceof CMSCoupon) {
    	            if (((CMSCoupon)array[i]).getScanCode().equals(((CMSCoupon)payment).getScanCode()))
    	              return new RulesInfo(
    	                  "This coupon has already been added to this transaction and may not be added again.");
    	          }
    	        }
    	      }
    	}	
    	/*   commentato da Riccardino poichè  i COFIX possono essere multipli.
    	else if (payment instanceof Coupon) {
        Payment[] array = txn.getPaymentsArray();
        for (int i = 0; i < array.length; i++) {
          if (array[i] instanceof Coupon) {
            if (((Coupon)array[i]).getScanCode().equals(((Coupon)payment).getScanCode()))
              return new RulesInfo(
                  "This coupon has already been added to this transaction and may not be added again.");
          }
        }
      } */
	  //Ends here
      return new RulesInfo();
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
    return "Payment Txn Add Coupon is Valid";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Rule should determine whether it is allo");
    buf.append("wed to add a specific payment to a <code>PaymentTransaction</code>.");
    return buf.toString();
  }
}

