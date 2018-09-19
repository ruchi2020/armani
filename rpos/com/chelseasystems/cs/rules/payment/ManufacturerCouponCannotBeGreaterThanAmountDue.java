package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;

import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.util.Version;

public class ManufacturerCouponCannotBeGreaterThanAmountDue extends Rule {


   public ManufacturerCouponCannotBeGreaterThanAmountDue() {
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSCompositePOSTransaction
    * @param args - instance of Payment
    */
   public RulesInfo execute (Object theParent, Object args[]) {
      return execute((PaymentTransaction) theParent, (Payment) args[0]);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSCompositePOSTransaction
    * @param args - instance of Payment
    */
   private RulesInfo execute(PaymentTransaction cmscompositepostransaction, Payment payment) {
      try {
    	  System.out.println("calling ManufacturerCouponCannotBeGreaterThanAmountDue ");
    	  if(payment instanceof CMSPremioDiscount){
    		  return new RulesInfo();
    	  }
    	  //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
    	  //Added by Vivek Mishra for Coupon Management PCR for Europe region : start
          //To show coupon specific message in the Europe region.
    	  if(payment instanceof ManufacturerCoupon){
        	 if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))&&(payment.getAmount().greaterThan(cmscompositepostransaction.getAppModel(CMSAppModelFactory.getInstance(), com.chelseasystems.cr.swing.CMSApplet.theAppMgr).getCompositeTotalAmountDue().subtract(cmscompositepostransaction.getTotalPaymentAmount()))))
                 return new RulesInfo("Coupons cannot be greater than the total amount due.");
        	 //end
            
        	 if (payment.getAmount().greaterThan(cmscompositepostransaction.getAppModel(CMSAppModelFactory.getInstance(), com.chelseasystems.cr.swing.CMSApplet.theAppMgr).getCompositeTotalAmountDue().subtract(cmscompositepostransaction.getTotalPaymentAmount())))
               return new RulesInfo("Manufacturer coupons cannot be greater than the total amount due.");
          }//Ends here
      } catch (Exception ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "execute",
                                "Rule Failed, see exception.",
                                "N/A", LoggingServices.MAJOR, ex);
      }
      return new RulesInfo();
   }

   /**
    * Returns the name of the Rule.
    * @return the name of the rule
    */
   public String getName() {
      return "Manufacturer Coupon Cannot Be Greater Than Amount Due";
   }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
   public String getDesc() {
      StringBuffer buf = new StringBuffer();
      buf.append("Manufacturer cannot be greater than amou");
      buf.append("nt due.  You do not want to give cahnge back from a coupon");
      return buf.toString();
   }
}
