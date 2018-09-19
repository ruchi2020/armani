/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-22-2005 | Mukesh    | N/A       |1.This class is new class created to achive   |
 |      |            |           |           | new business rule that Trxn with rewards     |
 |            |           |           | discount can't be void                       |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.discount.RewardDiscount;
import com.chelseasystems.cr.discount.Discount;


/**
 * <p>Title: TransactionCannotVoidIfRewardDiscount</p>
 * <p>Description: This class checks the business rule that is if a Transaction
 * is having Reward discoount then it cann't be voided</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Mukesh
 * @version 1.0
 */
public class TransactionCannotVoidIfRewardDiscount extends Rule {

  /**
   * Default Constructor
   */
  public TransactionCannotVoidIfRewardDiscount() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CompositePOSTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((Transaction)theParent);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CompositePOSTransaction
   */
  private RulesInfo execute(Transaction txn) {
    try {
      if (txn instanceof PaymentTransaction) {
        if (txn instanceof CompositePOSTransaction) {
          Discount[] discontTxn = ((CMSCompositePOSTransaction)txn).getDiscountsArray();
          for (int i = 0; i < discontTxn.length; i++) {
            if (discontTxn[i] instanceof RewardDiscount) {
              return new RulesInfo("RewardDiscount Transaction can not be voided.");
            }
          }
        }
      }
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
    return "RewardDiscount Transaction is not Voidable";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "Rule should determine that a transaction with a "
        + "RewardDiscount payment is not voidable.";
  }
}

