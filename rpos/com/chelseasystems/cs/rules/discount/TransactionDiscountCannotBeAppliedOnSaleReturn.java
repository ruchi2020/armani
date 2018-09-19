/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.discount;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.discount.Discount;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class TransactionDiscountCannotBeAppliedOnSaleReturn extends Rule {

  /**
   * put your documentation comment here
   */
  public TransactionDiscountCannotBeAppliedOnSaleReturn() {
  }

  /**
   * Execute the rule.
   * @param theParent the class calling the rule (i.e. TransactionPOS)
   * @param args
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSCompositePOSTransaction)theParent, (Discount)args[0]);
  }

  /**
   * Execute the rule.
   * @param theParent the class calling the rule (i.e. TransactionPOS)
   * @param args
   */
  public RulesInfo execute(CMSCompositePOSTransaction transaction, Discount discount) {
    try {
      String discountType = discount.getType();
      if (!discountType.equals("EMPLOYEE"))
        return new RulesInfo();
      if (transaction.getConsultant().getId().endsWith("99999"))
        return new RulesInfo();
      else
        return new RulesInfo(
            "Transaction Discount Cannot be Applied to a Transaction containing Sale/Return Line Item.");
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return new RulesInfo();
    }
  }

  /**
   * Returns the name of the rule.
   * @return the name of the rule
   */
  public String getName() {
    return
        "Transaction Discount Cannot be Applied to a Transaction containing Sale/Return Line Item.";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @return a user-friendly version of the rule.
   */
  public String getDesc() {
    String result =
        "Transaction Discount Cannot be Applied to a Transaction containing Sale/Return Line Item.";
    return result;
  }
}

