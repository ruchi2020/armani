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


/**
 * <p>Title: CMSConsignmentLineItemDetail</p>
 * <p>Description: This class is used to store consignment line item detail
 * attribures  </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Rajesh Pradhan
 * @version 1.0
 */
public class CMSConsignmentLineItemDetail extends POSLineItemDetail implements Serializable {
  private boolean processed;
  private CMSSaleLineItemDetail saleLnItmDtl;
  private CMSReturnLineItemDetail rtnLnItmDtl;
  private boolean isForSale, isForReturn;

  /**
   * Constructor
   * @param posLnItm POSLineItem
   */
  public CMSConsignmentLineItemDetail(POSLineItem posLnItm) {
    super(posLnItm);
  }

  /**
   *
   * @param val boolean
   */
  public void doSetProcessed(boolean val) {
    this.processed = val;
  }

  /**
   *
   * @return boolean
   */
  public boolean getProcessed() {
    return this.processed;
  }

  /**
   * This method is used to set the consignment sale line item detail
   * @param val CMSSaleLineItemDetail
   */
  public void doSetSaleLineItemDetail(CMSSaleLineItemDetail val) {
    this.saleLnItmDtl = val;
    this.isForSale = true;
  }

  /**
   * This method is used to get the consignment sale line item detail
   * @return CMSSaleLineItemDetail
   */
  public CMSSaleLineItemDetail getSaleLineItemDetail() {
    return this.saleLnItmDtl;
  }

  /**
   * This method is used to remove the consignment sale line item detail
   */
  public void doRemoveSaleLineItemDetail() {
    this.saleLnItmDtl = null;
    this.isForSale = false;
  }

  /**
   * This method is used to set the consignment return line item detail
   * @param val CMSReturnLineItemDetail
   */
  public void doSetReturnLineItemDetail(CMSReturnLineItemDetail val) {
    this.rtnLnItmDtl = val;
    this.isForReturn = true;
  }

  /**
   * This method is used to get the consignment return line item detail
   * @return CMSReturnLineItemDetail
   */
  public CMSReturnLineItemDetail getReturneLineItemDetail() {
    return this.rtnLnItmDtl;
  }

  /**
   * This method is used to remove the consignment return line item detail
   */
  public void doRemoveReturnLineItemDetail() {
    this.rtnLnItmDtl = null;
    this.isForReturn = false;
  }

  /**
   * This method is used to check whether consignment line item is for sale
   * @return boolean
   */
  public boolean isForSale() {
    return this.isForSale;
  }

  /**
   * This method is used to check whether consignment line item is for return
   * @return boolean
   */
  public boolean isForReturn() {
    return this.isForReturn;
  }

  //   public ArmCurrency getNetAmount() {
  //     return new ArmCurrency(0.0d);
  //   }
  /**
   * This method is used to get the total amount due for the consignment
   * @return Currency
   */
  public ArmCurrency getTotalAmountDue() {
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to get the tax amount on the consignment line item
   * @return Currency
   */
  public ArmCurrency getTaxAmount() {
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to set the consignment sale item detail
   * @param aSaleLineItemDetail CMSSaleLineItemDetail
   */
  public void connectSaleLineItemDetail(CMSSaleLineItemDetail aSaleLineItemDetail) {
    if (aSaleLineItemDetail == null) {
      return;
    } else {
      this.doSetSaleLineItemDetail(aSaleLineItemDetail);
      aSaleLineItemDetail.doSetConsignmentLineItemDetail(this);
      return;
    }
  }

  /**
   * This method is used to set the consignment return item detail
   * @param aReturnLineItemDetail CMSReturnLineItemDetail
   */
  public void connectReturnLineItemDetail(CMSReturnLineItemDetail aReturnLineItemDetail) {
    if (aReturnLineItemDetail == null) {
      return;
    } else {
      this.doSetReturnLineItemDetail(aReturnLineItemDetail);
      aReturnLineItemDetail.doSetConsignmentLineItemDetail(this);
      return;
    }
  }
}

