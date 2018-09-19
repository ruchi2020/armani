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
 | 1    | 09-16-2005 | Vikram    | 801       | This class implements business rule to       |
 |      |            |           |           | disallow converting a Txn to Employee sale if|
 |      |            |           |           | disconts already exist for the Txn.          |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.rules.pos;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;


/**
 * <p>Title: EmployeeSaleNotAllowedForTransactionWithDiscount</p>
 * <p>Description: This class checks the business rule to disallow converting a
 * Txn to Employee sale if disconts already exist for the Txn.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class EmployeeSaleNotAllowedForTransactionWithDiscount extends Rule {

  /**
   */
  public EmployeeSaleNotAllowedForTransactionWithDiscount() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCompositePOSTransaction
   * @param args Object[]
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSCompositePOSTransaction)theParent);
  }

  /**
   * Execute the Rule
   * @param txn CMSCompositePOSTransaction
   */
  private RulesInfo execute(CMSCompositePOSTransaction txn) {
    if (txn.getDiscountsArray().length > 0 && !txn.getEmployeeSale())
      return new RulesInfo(CMSApplet.res.getString(
          "Cannot convert to Employee Sale when discounts have already been applied."));
    return new RulesInfo();
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return CMSApplet.res.getString(
        "Employee sale is not allowed for transaction with any discount already applied.");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return CMSApplet.res.getString("Rule should ensure that a transaction is not allowed to convert to Employee Sale when discounts have already been applied.");
  }
}

