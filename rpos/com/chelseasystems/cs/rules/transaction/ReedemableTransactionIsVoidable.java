/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 1    | 06-21-2005 |  Vikram   | 195       |Used StoreValueCard instead of GiftCard          |
 -----------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 */
public class ReedemableTransactionIsVoidable extends Rule {

  /**
   */
  public ReedemableTransactionIsVoidable() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSLayawayRTSTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((Transaction)theParent);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSLayawayRTSTransaction
   */
  private RulesInfo execute(Transaction txn) {
    try {
      if (txn instanceof PaymentTransaction) {
        if (txn instanceof CompositePOSTransaction) {
          POSLineItem[] lines = ((CompositePOSTransaction)txn).getSaleLineItemsArray();
          for (int i = 0; i < lines.length; i++) {
            POSLineItemDetail[] details = lines[i].getLineItemDetailsArray();
            for (int j = 0; j < details.length; j++) {
              String id = details[j].getGiftCertificateId();
              if (id != null && id.length() > 0) {
                Redeemable gc = CMSRedeemableHelper.findStoreValueCard(repositoryManager, id);
                if (gc != null && gc.isRedeemed())
                  return new RulesInfo(CMSApplet.res.getString(
                      "This transaction has a gift card that has been redeemed and can not be voided."));
              }
            }
          }
        }
      }
      Redeemable db = CMSRedeemableHelper.findDueBill(repositoryManager, txn.getId());
      if (db != null && db.isRedeemed())
        return new RulesInfo(
            "This transaction contains a store credit that has been redeemed and can not be voided.");
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      ex.printStackTrace();
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return "Reedemable Transaction is Voidable";
  }

  /**
   * Returns a user-friendly version of the rule.
   * @returns a user-friendly version of the rule.
   */
  public String getDesc() {
    return "Rule should determine whether a transaction with a "
        + "reedemable payment is voidable.";
  }
}

