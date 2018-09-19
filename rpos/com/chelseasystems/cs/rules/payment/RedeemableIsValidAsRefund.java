/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                              |
 +------+------------+-----------+-----------+----------------------------------------------------------+
 | 1    | 06/06/2005 | Vikram    | 70        | Gift Card and Credit Note are supposed to be same tender.|
 |      |            |           |           | The is the common rule repesenting both tenders          |
 --------------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: RedeemableIsValidAsRefund</p>
 * <p>Description: This business rule check if Redeemable is not a valid form
 * of refund payment. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Vikram
 * @version 1.0
 */
public class RedeemableIsValidAsRefund extends Rule {

  /**
   * Default Constructor
   */
  public RedeemableIsValidAsRefund() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of DueBill
   * @param args - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((Redeemable)theParent, (PaymentTransaction)args[0]);
  }

  /**
   * Execute the Rule
   * @param redeemable - instance of Redeemable
   * @param args - instance of PaymentTransaction
   */
  private RulesInfo execute(Redeemable redeemable, PaymentTransaction paymenttransaction) {
    try {
      if (redeemable instanceof StoreValueCard)
        return new RulesInfo(CMSApplet.res.getString(
            "Gift Card is not a valid form of refund payment."));
      if (redeemable instanceof DueBill)
        return new RulesInfo(CMSApplet.res.getString(
            "Credit Note is not a valid form of refund payment."));
      return new RulesInfo("");
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
    return CMSApplet.res.getString("Redeemable is not a valid form of refund payment.");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append(CMSApplet.res.getString(
        "Determines whether a Redeemable is allowed as for of refund on this transaction."));
    return buf.toString();
  }
}

