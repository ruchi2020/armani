/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 08-05-2005 | Vikram    | N/A       | New business rule so that Presale/Consignment|
 |      |            |           |           | close cannot be void.                        |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.pos.CMSSaleLineItemDetail;


/**
 * <p>Title: PreSalesOrConsignmentCloseIsVoidable</p>
 * <p>Description: This class checks the business rule that if a Pre-sale/Consignment close
 * transaction cannot be voided</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class PreSalesOrConsignmentCloseIsVoidable extends Rule {

  /**
   * Default Constructor
   */
  public PreSalesOrConsignmentCloseIsVoidable() {
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
      if (txn instanceof CompositePOSTransaction) {
        POSLineItem[] lineItem = ((CMSCompositePOSTransaction)txn).getSaleLineItemsArray();
        for (int i = 0; i < lineItem.length; i++) {
          POSLineItemDetail[] lineItemDetails = ((CMSSaleLineItem)lineItem[i]).
              getLineItemDetailsArray();
          //for(int j = 0 ; j < lineItemDetails.length ; j++)
          {
            if (((CMSSaleLineItemDetail)lineItemDetails[0]).getPresaleLineItemDetail() != null) {
              return new RulesInfo(CMSApplet.res.getString(
                  "This transaction has a Pre-sale which is closed, hence it cannot be voided."));
            } else if (((CMSSaleLineItemDetail)lineItemDetails[0]).getConsignmentLineItemDetail() != null) {
              return new RulesInfo(CMSApplet.res.getString(
                  "This transaction has a Consignment which is closed, hence it cannot be voided."));
            }
          }
        }
        lineItem = ((CMSCompositePOSTransaction)txn).getReturnLineItemsArray();
        for (int i = 0; i < lineItem.length; i++) {
          POSLineItemDetail[] lineItemDetails = ((CMSSaleLineItem)lineItem[i]).
              getLineItemDetailsArray();
          //for(int j = 0 ; j < lineItemDetails.length ; j++)
          {
            if (((CMSSaleLineItemDetail)lineItemDetails[0]).getPresaleLineItemDetail() != null) {
              return new RulesInfo(CMSApplet.res.getString(
                  "This transaction has a Pre-sale which is closed, hence it cannot be voided."));
            } else if (((CMSSaleLineItemDetail)lineItemDetails[0]).getConsignmentLineItemDetail() != null) {
              return new RulesInfo(CMSApplet.res.getString(
                  "This transaction has a Consignment which is closed, hence it cannot be voided."));
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
    return CMSApplet.res.getString("Pre-sale/Consignment Close Transaction is not Voidable");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return CMSApplet.res.getString(
        "Rule to determine that a closed Pre-sale/Consignment transaction is not voidable.");
  }
}

