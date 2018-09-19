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
 |      |            |           |           | new business rule that Redeemable Trxn can't |
 |            |           |           | be void                                      |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.payment.*;


/**
 * <p>Title: TransactionCannotVoidIfRedeemablePayment</p>
 * <p>Description: This class checks the business rule that is if a Transaction
 * is having Redeemable Payment then it cann't be voided</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Mukesh
 * @version 1.0
 */
public class TransactionCannotVoidIfRedeemablePayment extends Rule {

  /**
   * Default constructor
   */
  public TransactionCannotVoidIfRedeemablePayment() {
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
          Payment[] paymentTxn = ((CMSCompositePOSTransaction)txn).getPaymentsArray();
          for (int i = 0; i < paymentTxn.length; i++) {
            if (paymentTxn[i] instanceof Redeemable) {
              return new RulesInfo("Redeemable Transaction can not be voided.");
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
    return "Reedemable payment Transaction is not Voidable";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "Rule should determine that a transaction with a "
        + "reedemable payment is not voidable.";
  }
}

