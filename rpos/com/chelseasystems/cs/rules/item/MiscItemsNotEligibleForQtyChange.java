package com.chelseasystems.cs.rules.item;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import java.lang.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;

public class MiscItemsNotEligibleForQtyChange extends Rule {

    private HashMap ineligibleItems;
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

   public MiscItemsNotEligibleForQtyChange() {
      ineligibleItems = new HashMap();
      ineligibleItems.put("POSTAGE", "POSTAGE");
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSLayawayLineItem
    * @param args - instance of Integer
    */
   public RulesInfo execute (Object theParent, Object args[]) {
      return execute((POSLineItem) theParent, (Integer) args[0]);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSLayawayLineItem
    * @param args - instance of Integer
    */
   private RulesInfo execute(POSLineItem lineitem, Integer integer) {
      try {
         if(lineitem.isMiscItem())
            if(ineligibleItems.containsKey(lineitem.getMiscItemId())
                  && !(lineitem instanceof ReturnLineItem))
               return new RulesInfo(res.getString("Cannot modify the quantity of this miscellaneous item."));
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
      return "Misc Items Not Eligible For Qty Change";
   }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
   public String getDesc() {
      StringBuffer buf = new StringBuffer();
      buf.append("Misc items Not Eligible for Qty Change");
      return buf.toString();
   }
}
