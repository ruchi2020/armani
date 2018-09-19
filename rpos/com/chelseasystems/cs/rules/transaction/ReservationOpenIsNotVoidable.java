/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 11-01-2005 | Vikram    | N/A       | New rule class to prevent void of Reservation|
 |      |            |           |           | -Open transaction.                           |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: ReservationOpenIsNotVoidable</p>
 * <p>Description: This class implements the business rule to prevent void of a Reservartion-open
 * transaction.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class ReservationOpenIsNotVoidable extends Rule {

  /**
   * Default Constructor
   */
  public ReservationOpenIsNotVoidable() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCompositePOSTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSCompositePOSTransaction)theParent);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCompositePOSTransaction
   */
  private RulesInfo execute(CMSCompositePOSTransaction txn) {
    try {
      if (txn.getReservationTransaction() != null)
        return new RulesInfo(CMSApplet.res.getString(
            "Reservation-open transaction cannot be voided."));
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
    return "Reservation-open transaction is not Voidable";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "Rule should determine that a Reservation-open transaction is not voidable.";
  }
}
