/*
 * Chelsea Market Systems
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.lineitem;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import java.lang.*;
import com.chelseasystems.cs.pos.*;


/**
 * put your documentation comment here
 */
public class ReturnLineItemSetQtyIsValid extends Rule {

  /**
   * put your documentation comment here
   */
  public ReturnLineItemSetQtyIsValid() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSReturnLineItem
   * @param args - instance of Integer
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSReturnLineItem)theParent, (Integer)args[0]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSReturnLineItem
   * @param args - instance of Integer
   */
  private RulesInfo execute(CMSReturnLineItem cmsReturnLineItem, Integer newQuantity) {
    try {
      if (cmsReturnLineItem.getSaleLineItem().getQuantityAvailableForReturn()
          < newQuantity.intValue() - cmsReturnLineItem.getQuantity().intValue())
        return new RulesInfo(
            "The requested return quantity is greater than the quantity available for return."
            + "  (" + newQuantity + ") > ("
            + (cmsReturnLineItem.getSaleLineItem().getQuantityAvailableForReturn()
            + cmsReturnLineItem.getQuantity().intValue()) + ")");
      // place business logic here
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
    return "Return Line Item Set Qty Is Valid";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Is this qty change valid?");
    return buf.toString();
  }
}

