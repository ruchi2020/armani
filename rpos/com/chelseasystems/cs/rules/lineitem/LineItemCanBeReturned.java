package com.chelseasystems.cs.rules.lineitem;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.POSLineItem;

public class LineItemCanBeReturned extends Rule {

   public LineItemCanBeReturned() {
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSSaleLineItem
    */
   public RulesInfo execute (Object theParent, Object args[]) {
      return execute((POSLineItem) theParent);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSSaleLineItem
    */
   private RulesInfo execute(POSLineItem posLineItem) {
      try {

        if(posLineItem instanceof CMSPresaleLineItem)
        {
          CMSPresaleLineItemDetail presaleDetail = (CMSPresaleLineItemDetail)((CMSPresaleLineItem)posLineItem).getLineItemDetailsArray()[0];
          if(presaleDetail.getAlterationLineItemDetailArray()!=null
            &&
           presaleDetail.getAlterationLineItemDetailArray().length>0)
          return new RulesInfo("This line item has Alteration which is processed so can not be returned.");
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
      return "Line item can be returned";
   }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
   public String getDesc() {
      StringBuffer buf = new StringBuffer();
      buf.append("This rules should test and see if the sp");
      buf.append("eciifed line item is valid for return.");
      return buf.toString();
   }
}
