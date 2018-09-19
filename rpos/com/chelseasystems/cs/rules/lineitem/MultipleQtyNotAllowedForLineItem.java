package com.chelseasystems.cs.rules.lineitem;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.POSLineItem;

public class MultipleQtyNotAllowedForLineItem extends Rule {
	
  static final long serialVersionUID = 0;

   public MultipleQtyNotAllowedForLineItem() {
   }

   /**
    * Execute the Rule
    * @param theParent - instance of POSLineItem
    * @param args - instance of Integer
    */
   public RulesInfo execute(Object theParent, Object args[]) {
     return execute((POSLineItem)theParent, (Integer)args[0]);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of POSLineItem
    * @param args - instance of Integer
    */
   private RulesInfo execute(POSLineItem lineItem, Integer newQuantity) {
      try {
       	if (newQuantity.intValue() > 1) {
        	return new RulesInfo("The requested quantity for line item cannot be greater than one");
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
      return "Multiple quantity not allowed for line items";
   }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
   public String getDesc() {
      StringBuffer buf = new StringBuffer();
      buf.append("This rules should test and see if the sp");
      buf.append("eciifed line item can have quantity greater than one.");
      return buf.toString();
   }
}
