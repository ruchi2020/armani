package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.CMSPremioDiscount;

public class ManufacturerCouponIsValidAsRefund extends Rule {

   public ManufacturerCouponIsValidAsRefund() {
   }

   /**
    * Execute the Rule
    * @param theParent - instance of ManufacturerCoupon
    * @param args - instance of PaymentTransaction
    */
   public RulesInfo execute (Object theParent, Object args[]) {
      return execute((ManufacturerCoupon) theParent, (PaymentTransaction) args[0]);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of ManufacturerCoupon
    * @param args - instance of PaymentTransaction
    */
   private RulesInfo execute(ManufacturerCoupon manufacturercoupon, PaymentTransaction paymenttransaction) {
      try {

        /*// place business logic here
    	  System.out.println("calling MAnufacturerCouponIsValidAsRefund ");
    	  if(manufacturercoupon instanceof CMSPremioDiscount){
    		  return new RulesInfo();
    	  }*/
         return new RulesInfo("Manufacturer coupon is not valid as refund.");
      } catch (Exception ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "execute",
                                "Rule Failed, see exception.",
                                "N/A", LoggingServices.MAJOR, ex);
         return new RulesInfo();
      }
   }

   /**
    * Returns the name of the Rule.
    * @return the name of the rule
    */
    public String getName() {
       return "Manufacturer Coupin is valid as refund";
    }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
    public String getDesc() {
       StringBuffer buf = new StringBuffer();
      buf.append("");
      return buf.toString();
   }
}
