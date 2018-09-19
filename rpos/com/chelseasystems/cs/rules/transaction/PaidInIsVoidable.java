/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05/02/2005 | Khyati    | N/A       | Added a payment type check for Redeemables         |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-26-2005 | Mukesh    | N/A       |1.This class is new class created to achive   |
 |      |            |           |           | new business rule that Paid in transaction   |
 |            |           |           | is not voidable                              |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.collection.CMSMiscCollection;


/**
 * <p>Title: PaidInIsVoidable</p>
 * <p>Description: This class checks the business rule that a paid in of
 * type redeemable cann't be voided</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Mukesh
 * @version 1.0
 */
public class PaidInIsVoidable extends Rule {

  /**
   */
  public PaidInIsVoidable() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of PaymentTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((PaymentTransaction)theParent);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of PaymentTransaction
   */
  private RulesInfo execute(PaymentTransaction txn) {
    try {
      if (txn instanceof CMSMiscCollection) {
        Redeemable redeemable = ((CMSMiscCollection)txn).getRedeemable();
        if (redeemable != null) {
          return new RulesInfo("Redeemable Paid In cannot be voided");
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
    return "Redeemable Paid In transaction is not Voidable";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "Rule should determine that a Redeemable paid in transaction  " + "  cann't Voidable.";
  }
}

