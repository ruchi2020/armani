/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 09-13-2005 | Manpreet  | 508       |Implementing Alterations on PresaleLineItem         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import java.io.Serializable;
import java.util.*;
import com.chelseasystems.cs.item.CMSItem;


/**
 *
 * <p>Title:CMSPresaleLineItemDetail </p>
 *
 * <p>Description: This class store the pre sale line item detail information</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh Pradhan
 * @version 1.0
 */
public class CMSPresaleLineItemDetail extends POSLineItemDetail implements Serializable {
  private boolean processed;
  private CMSSaleLineItemDetail saleLnItmDtl;
  private CMSReturnLineItemDetail rtnLnItmDtl;
  private boolean isForSale, isForReturn;
  private List listAlterationLineItemDetails;
  private double taxRate = 0.0d;
  private boolean isTaxRateDefined = false;
  /**
   * Constructor
   * @param posLnItm POSLineItem
   */
  public CMSPresaleLineItemDetail(POSLineItem posLnItm) {
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
   * This method is used to set the sale line item detail
   * @param val CMSSaleLineItemDetail
   */
  public void doSetSaleLineItemDetail(CMSSaleLineItemDetail val) {
    this.saleLnItmDtl = val;
    this.isForSale = true;
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
    this.isForSale = false;
  }

  /**
   * This method is used to set the return line item detail
   * @param val CMSReturnLineItemDetail
   */
  public void doSetReturnLineItemDetail(CMSReturnLineItemDetail val) {
    this.rtnLnItmDtl = val;
    this.isForReturn = true;
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
    this.isForReturn = false;
  }

  /**
   * This method is used to check whether the line item is for sale or not
   * @return boolean
   */
  public boolean isForSale() {
    return this.isForSale;
  }

  /**
   * This method is used to check whether the line item is for return or not
   * @return boolean
   */
  public boolean isForReturn() {
    return this.isForReturn;
  }

  //
  //   public ArmCurrency getNetAmount() {
  //     return new ArmCurrency(0.0d);
  //   }
  /**
   * This method is used to get the pre sale total amount due
   * @return Currency
   */
  public ArmCurrency getTotalAmountDue() {
    return new ArmCurrency(0.0d);
  }

  /**
   * This method is used to get the tax amount
   * @return Currency
   */
//  public ArmCurrency getTaxAmount() {
//    return new ArmCurrency(0.0d);
//  }

  /**
   * This method is used to set the sale item detail
   * @param aSaleLineItemDetail CMSSaleLineItemDetail
   */
  public void connectSaleLineItemDetail(CMSSaleLineItemDetail aSaleLineItemDetail) {
    if (aSaleLineItemDetail == null) {
      return;
    } else {
      this.doSetSaleLineItemDetail(aSaleLineItemDetail);
      aSaleLineItemDetail.doSetPresaleLineItemDetail(this);
      return;
    }
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
      aReturnLineItemDetail.doSetPresaleLineItemDetail(this);
      return;
    }
  }

  /**
   * This method is used to add an AlterationLineItemDetail to list
   * @param alterLineItemDetail AlterationLineItemDetail
   * @author Manpreet S Bawa
   */
  public void addAlterationLineItemDetail(AlterationLineItemDetail alterLineItemDetail) {
    if (alterLineItemDetail == null) {
      return;
    }
    doAddAlterationLineItemDetail(alterLineItemDetail);
  }

  /**
   * This method is used to add an AlterationLineItemDetail to list
   * @param alterLineItemDetail AlterationLineItemDetail
   *
   */
  public void doAddAlterationLineItemDetail(AlterationLineItemDetail alterLineItemDetail) {
    if (listAlterationLineItemDetails == null) {
      listAlterationLineItemDetails = new ArrayList();
    }
    listAlterationLineItemDetails.add(alterLineItemDetail);
  }

  /**
   * This method is used to remove an AlterationLineItemDetail from the
   * specific index of list
   * @param iCtr Index
   */
  public void removeAlterationLineItemDetailAt(int iCtr) {
    if (iCtr < 0 || listAlterationLineItemDetails == null
        || iCtr > listAlterationLineItemDetails.size()) {
      return;
    }
    listAlterationLineItemDetails.remove(iCtr);
  }

  /**
   * This method is used to remove an AlterationLineItemDetail to list
   * @param alterationLineItemDetail AlterationLineItemDetail
   */
  public void removeAlterationLineItemDetail(AlterationLineItemDetail alterationLineItemDetail) {
    if (listAlterationLineItemDetails == null || alterationLineItemDetail == null) {
      return;
    }
    listAlterationLineItemDetails.remove(alterationLineItemDetail);
  }

  /**
   * This method is used to set an AlterationLineItemDetail object at
   * specific index to the list
   * @param iCtr Index
   * @param alterLineItemDetail AlterationLineItemDetail
   */
  public void setAlterationLineItemDetailAt(int iCtr, AlterationLineItemDetail alterLineItemDetail) {
    if (iCtr < 0 || listAlterationLineItemDetails == null
        || iCtr > listAlterationLineItemDetails.size() || alterLineItemDetail == null) {
      return;
    }
    doSetAlterationLineItemDetailAt(iCtr, alterLineItemDetail);
  }

  /**
   * This method is used to set an AlterationLineItemDetail object at
   * specific index to the list
   * @param iCtr int
   * @param alterLineItemDetail AlterationLineItemDetail
   */
  public void doSetAlterationLineItemDetailAt(int iCtr
      , AlterationLineItemDetail alterLineItemDetail) {
    if (listAlterationLineItemDetails == null) {
      return;
    }
    listAlterationLineItemDetails.set(iCtr, alterLineItemDetail);
  }

  /**
   * This method is used to get an AlterationLineItemDetail object at
   * specific index from the list
   * @param iCtr Index
   * @return AlterationLineItemDetail
   */
  public AlterationLineItemDetail getAlterationLineItemDetailAt(int iCtr) {
    if (iCtr < 0 || listAlterationLineItemDetails == null
        || iCtr > listAlterationLineItemDetails.size()) {
      return null;
    }
    return (AlterationLineItemDetail)listAlterationLineItemDetails.get(iCtr);
  }

  /**
   * This method used to get AlterationLineItemDetail array
   * @return AlterationLineItemDetail[]
   */
  public AlterationLineItemDetail[] getAlterationLineItemDetailArray() {
    if (listAlterationLineItemDetails == null) {
      return null;
    }
    return (AlterationLineItemDetail[])listAlterationLineItemDetails.toArray(new
        AlterationLineItemDetail[listAlterationLineItemDetails.size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double getTaxRate() {
    return taxRate;
  }

  // New method added for Inquiry Screen
  public boolean isTaxRateDefined() {
    return this.isTaxRateDefined;
  }

  /**
   * put your documentation comment here
   * @param taxRate
   */
  public void setTaxRate(double taxRate) {
    doSetTaxRate(taxRate);
    this.isTaxRateDefined = true;
  }

  /**
   * put your documentation comment here
   * @param taxRate
   */
  public void doSetTaxRate(double taxRate) {
    this.taxRate = taxRate;
  }

}

