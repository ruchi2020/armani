/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 07-29-2005 | Megha     | N/A       |1.This class is new class created to achive   |
 |      |            |           |           | new business rule that Nosale                |
 |            |           |           | can't be void                                |
 --------------------------------------------------------------------------------------------
 */
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;


/**
 * <p>Title: NoSaleIsNotVoidable</p>
 * <p>Description: This class checks the business rule that if a NoSale
 * transaction is can't be void</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Mukesh
 * @version 1.0
 */
public class NoSaleIsNotVoidable extends Rule {

  /**
   * Default Constructor
   */
  public NoSaleIsNotVoidable() {
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
        if (txn instanceof NoSaleTransaction && !txn.getTransactionType().trim().equals("REWD")) {
          return new RulesInfo("NoSale Transaction can not be voided.");
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
    return "Nosale Transaction is not Voidable";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "Rule should determine that a Nosale transaction " + "is not voidable.";
  }
}

