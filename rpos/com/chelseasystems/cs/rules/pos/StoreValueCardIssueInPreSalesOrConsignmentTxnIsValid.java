/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 08-08-2005 | Vikram    | N/A       | New business rule so check if StoreValueCard |
 |      |            |           |           | issue valid for that Presale/Consignment.    |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.pos;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;


/**
 * <p>Title: StoreValueCardIssueInPreSalesOrConsignmentTxnIsValid</p>
 * <p>Description: This class checks the business rule that if a StoreValueCard
 * can be issued in a Pre-sale/Consignment transaction</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class StoreValueCardIssueInPreSalesOrConsignmentTxnIsValid extends Rule {

  /**
   * Default Constructor
   */
  public StoreValueCardIssueInPreSalesOrConsignmentTxnIsValid() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CompositePOSTransaction
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    if (args != null && args.length > 0 && args[0] instanceof Integer)
      return execute(((Integer)args[0]).intValue());
    //else
    return execute( -1);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CompositePOSTransaction
   */
  private RulesInfo execute(int txnAppletMode) {
    try {
      if (txnAppletMode > -1
          && (txnAppletMode == InitialSaleApplet.CONSIGNMENT_OPEN
          || txnAppletMode == InitialSaleApplet.PRE_SALE_OPEN)) {
        return new RulesInfo(CMSApplet.res.getString(
            "Issue of StoreValueCard is not valid for Pre-Sale / Consignment transactions."));
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
    return CMSApplet.res.getString(
        "StoreValueCard Issue in Pre-sale/Consignment Transaction is not Valid.");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return CMSApplet.res.getString(
        "Rule to determine that issue of StoreValueCard in a Pre-sale/Consignment transaction is not valid.");
  }
}

