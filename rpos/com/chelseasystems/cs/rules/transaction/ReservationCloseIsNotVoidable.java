/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 11-01-2005 | Vikram    | N/A       | New rule class to prevent void of Reservation|
 |      |            |           |           | -Close transaction.                          |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cs.pos.CMSSaleLineItemDetail;
import com.chelseasystems.cs.pos.CMSReturnLineItemDetail;


/**
 * <p>Title: ReservationCloseIsNotVoidable</p>
 * <p>Description: This class implements the business rule to prevent void of a Reservartion-close
 * transaction.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Vikram
 * @version 1.0
 */
public class ReservationCloseIsNotVoidable extends Rule {

  /**
   * Default Constructor
   */
  public ReservationCloseIsNotVoidable() {
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
      POSLineItem[] saleLineItems = txn.getSaleLineItemsArray();
      if (saleLineItems != null && saleLineItems.length > 0) {
        for (int i = 0; i < saleLineItems.length; i++) {
          POSLineItemDetail[] lineItemDetails = saleLineItems[i].getLineItemDetailsArray();
          for (int j = 0; j < lineItemDetails.length; j++) {
            CMSSaleLineItemDetail cmsSaleLnItmDtl = (CMSSaleLineItemDetail)lineItemDetails[j];
            if (cmsSaleLnItmDtl.getTypeCode() != null
                && cmsSaleLnItmDtl.getTypeCode().equals(CMSSaleLineItemDetail.
                POS_LINE_ITEM_TYPE_RESERVATION)) {
              return new RulesInfo(CMSApplet.res.getString(
                  "This transaction contains Reservation-close, hence cannot be voided."));
            }
          }
        }
      }
      POSLineItem[] returnLineItems = txn.getReturnLineItemsArray();
      if (returnLineItems != null && returnLineItems.length > 0) {
        for (int i = 0; i < returnLineItems.length; i++) {
          POSLineItemDetail[] lineItemDetails = returnLineItems[i].getLineItemDetailsArray();
          for (int j = 0; j < lineItemDetails.length; j++) {
            CMSReturnLineItemDetail cmsReturnLnItmDtl = (CMSReturnLineItemDetail)lineItemDetails[j];
            if (cmsReturnLnItmDtl.getTypeCode() != null
                && cmsReturnLnItmDtl.getTypeCode().equals(CMSReturnLineItemDetail.
                POS_LINE_ITEM_TYPE_RESERVATION)) {
              return new RulesInfo(CMSApplet.res.getString(
                  "This transaction contains Reservation-close, hence cannot be voided."));
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
    return "Reservation-close transaction is not Voidable";
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "Rule should determine that a Reservation-close transaction is not voidable.";
  }
}
