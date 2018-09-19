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
 | 1    | 04-26-2006 | Vikram    | N/A       |1.This class is new class created to achive   |
 |      |            |           |           | new business rule that Reservation open once |
 |      |            |           |           | closed can't be void                         |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.ReservationTransaction;
import com.chelseasystems.cs.pos.CMSReservationLineItemDetail;


/**
 * <p>Title: ReservationOpenIsVoidable</p>
 * <p>Description: This class checks the business rule that if a Reservation open
 * transaction is already processed then it cann't be voided</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class ReservationOpenIsVoidable extends Rule {

  /**
   * Default Constructor
   */
  public ReservationOpenIsVoidable() {
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
          ReservationTransaction reservationTxn = ((CMSCompositePOSTransaction)txn).getReservationTransaction();
          POSLineItem[] lines = reservationTxn.getLineItemsArray();
          for (int i = 0; i < lines.length; i++) {
            POSLineItemDetail[] details = lines[i].getLineItemDetailsArray();
            for (int j = 0; j < details.length; j++) {
              if (((CMSReservationLineItemDetail)details[j]).getProcessed()) {
                return new RulesInfo(
                    "This transaction is a reservation open which is already processed, hence it can not be voided.");
              }
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
    return "Processed Reservation Open Transaction is not Voidable";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "Rule should determine that a processed Reservation Open transaction " + "is not voidable.";
  }
}

