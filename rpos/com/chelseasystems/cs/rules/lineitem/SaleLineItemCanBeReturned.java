package com.chelseasystems.cs.rules.lineitem;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.pos.*;

public class SaleLineItemCanBeReturned extends Rule {

   public SaleLineItemCanBeReturned() {
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSSaleLineItem
    */
   public RulesInfo execute (Object theParent, Object args[]) {
      return execute((CMSSaleLineItem) theParent);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSSaleLineItem
    */
   private RulesInfo execute(CMSSaleLineItem cmssalelineitem) {
      try {
        if(((CMSSaleLineItemDetail)cmssalelineitem.getLineItemDetailsArray()[0]).getAlterationLineItemDetailArray()!=null
           &&
           ((CMSSaleLineItemDetail)cmssalelineitem.getLineItemDetailsArray()[0]).getAlterationLineItemDetailArray().length>0)
          return new RulesInfo("This line item has Alteration which is processed so can not be returned.");

         return  new com.chelseasystems.cs.rules.item.
            MiscItemsNotEligibleReturn().execute(null, new Object[] { cmssalelineitem });
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
      return "Sale line item can be returned";
   }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
   public String getDesc() {
      StringBuffer buf = new StringBuffer();
      buf.append("This rules should test and see if the sp");
      buf.append("eciifed sale line item is valid for return.");
      return buf.toString();
   }
}
