/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import java.io.Serializable;
import com.chelseasystems.cs.item.CMSItem;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 10-06-2005 | Pankaja   | N/A       |  Restructing during the Merge Activity             |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 1    | 07-11-2005 | Manpreet  | N/A       |  POS_104665_TS_Reservations_Rev4                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 * <p>Title: CMSReservationLineItemDetail </p>
 * <p>Description: Used to store reservation line item detail </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CMSReservationLineItemDetail extends POSLineItemDetail implements Serializable {
  private boolean bProcessed;
  private CMSSaleLineItemDetail saleLnItmDtl;
  private CMSReturnLineItemDetail rtnLnItmDtl;
  private boolean bIsForSale, bIsForReturn;

  /**
   * Constructor
   * @param posLnItm POSLineItem
   */
  public CMSReservationLineItemDetail(POSLineItem posLnItm) {
    super(posLnItm);
  }

  /**
   *
   * @param val boolean
   */
  public void doSetProcessed(boolean val) {
    this.bProcessed = val;
  }

  /**
   *
   * @return boolean
   */
  public boolean getProcessed() {
    return this.bProcessed;
  }

  /**
   * This method is used to set the sale line item detail
   * @param val CMSSaleLineItemDetail
   */
  public void doSetSaleLineItemDetail(CMSSaleLineItemDetail val) {
    this.saleLnItmDtl = val;
    this.bIsForSale = true;
  }

  /**
   * This method is used to get the sale line item detail
   * @return CMSSaleLineItemDetail
   */
  public CMSSaleLineItemDetail getSaleLineItemDetail() {
    return this.saleLnItmDtl;
  }

  /** This method id used to remove sale line item details
   * This method is used to remove sale line item detail
   */
  public void doRemoveSaleLineItemDetail() {
    this.saleLnItmDtl = null;
    this.bIsForSale = false;
  }

  /**
   * This method is used to set the return line item detail
   * @param val CMSReturnLineItemDetail
   */
  public void doSetReturnLineItemDetail(CMSReturnLineItemDetail val) {
    this.rtnLnItmDtl = val;
    this.bIsForReturn = true;
  }

  /**
   * This method is used to get the return line item detail
   * @return CMSReturnLineItemDetail
   */
  public CMSReturnLineItemDetail getReturneLineItemDetail() {
    return this.rtnLnItmDtl;
  }

  /**
   * This method is used to remove return line item detail
   */
  public void doRemoveReturnLineItemDetail() {
    this.rtnLnItmDtl = null;
    this.bIsForReturn = false;
  }

  /**
   * This method is used to check whether the line item is for sale or not
   * @return boolean
   */
  public boolean isForSale() {
    return this.bIsForSale;
  }

  /**
   * This method is used to check whether the line item is for return or not
   * @return boolean
   */
  public boolean isForReturn() {
    return this.bIsForReturn;
  }

  /**
   * This method is used to get the pre sale total amount due
   * @return Currency
   */
  public ArmCurrency getTotalAmountDue() {
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to get the tax amount
   * @return Currency
   */
  public ArmCurrency getTaxAmount() {
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to set the sale item detail
   * @param aSaleLineItemDetail CMSSaleLineItemDetail
   */
  public void connectSaleLineItemDetail(CMSSaleLineItemDetail aSaleLineItemDetail) {
    if (aSaleLineItemDetail == null)
      return;
    doSetSaleLineItemDetail(aSaleLineItemDetail);
    aSaleLineItemDetail.doSetReservationLineItemDetail(this);
  }

  /**
   * This method is used to set the pre sale return item detail
   * @param aReturnLineItemDetail CMSReturnLineItemDetail
   */
  public void connectReturnLineItemDetail(CMSReturnLineItemDetail aReturnLineItemDetail) {
    if (aReturnLineItemDetail == null) {
      return;
    } else {
      this.doSetReturnLineItemDetail(aReturnLineItemDetail);
      aReturnLineItemDetail.doSetReservationLineItemDetail(this);
      return;
    }
  }
}

