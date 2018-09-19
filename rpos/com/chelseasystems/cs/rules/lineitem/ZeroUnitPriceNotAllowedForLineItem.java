package com.chelseasystems.cs.rules.lineitem;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

public class ZeroUnitPriceNotAllowedForLineItem extends Rule {
	
  static final long serialVersionUID = 0;

   public ZeroUnitPriceNotAllowedForLineItem() {}

   /**
    * Execute the Rule
    * @param theParent - instance of CMSCompositePOSTransaction
    * @param args - instance of CMSItem
    */
   public RulesInfo execute(Object theParent, Object args[]) {
	 return execute((CMSCompositePOSTransaction) theParent, (CMSItem) args[0]);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSCompositePOSTransaction
    * @param args - instance of CMSItem
    */
   private RulesInfo execute(CMSCompositePOSTransaction txn, CMSItem item) {
	   try {
		   if (item != null && item.getRetailPrice().doubleValue() == 0.00) {
			   return new RulesInfo("The requested barcode has zero unit price. Please contact technical support.");
		   }
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
      return "Zero unit price not allowed for line item";
   }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
   public String getDesc() {
      StringBuffer buf = new StringBuffer();
      buf.append("This rule should test and see if an ");
      buf.append("item with zero unit price can be entered.");
      return buf.toString();
   }
}
