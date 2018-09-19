/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 10-06-2005 | Pankaja   | N/A       |Restructing during the Merge Activity               |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-17-2005 | Pankaja   | N/A       |Specs Presale/Consignment impl                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-12-2005 | Rajesh    | N/A       |Specs Consignment impl                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.currency.*;


/**
 *
 * <p>Title: CMSReturnLineItemDetail</p>
 *
 * <p>Description: This class store the information of return line item</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh
 * @version 1.0
 */
public class CMSReturnLineItemDetail extends ReturnLineItemDetail implements com.chelseasystems.cr.
    rules.IRuleEngine {
  static final long serialVersionUID = -4461032097748732223L;
  private CMSPresaleLineItemDetail presaleLnItmDtl;
  private CMSConsignmentLineItemDetail consignmentLnItmDtl;
  private CMSReservationLineItemDetail reservationLnItmDtl;
  private Long typeCode;
  public static final Long POS_LINE_ITEM_TYPE_PRESALE = new Long(4);
  public static final Long POS_LINE_ITEM_TYPE_CONSIGNMENT = new Long(5);
  public static final Long POS_LINE_ITEM_TYPE_RESERVATION = new Long(6);
  public static final Long POS_LINE_ITEM_TYPE_VOID = new Long(7);

  /**
   * For Loyalty Points
   */
  private double loyaltyPoints = 0.0;

  /**
   * Constructor
   * @param lineItem POSLineItem
   */
  public CMSReturnLineItemDetail(POSLineItem lineItem) {
    super(lineItem);
  }

  /**
   * This method is used to set the pre sale return line item detail
   * @param val CMSPresaleLineItemDetail
   */
  public void doSetPresaleLineItemDetail(CMSPresaleLineItemDetail val) {
    this.presaleLnItmDtl = val;
    this.typeCode = POS_LINE_ITEM_TYPE_PRESALE;
  }

  /**
   * This method is used to get the pre sale return line item detail
   * @return CMSPresaleLineItemDetail
   */
  public CMSPresaleLineItemDetail getPresaleLineItemDetail() {
    return this.presaleLnItmDtl;
  }

  /**
   * This method is used to set the consignment return line item detail
   * @param val CMSConsignmentLineItemDetail
   */
  public void doSetConsignmentLineItemDetail(CMSConsignmentLineItemDetail val) {
    this.consignmentLnItmDtl = val;
    typeCode = POS_LINE_ITEM_TYPE_CONSIGNMENT;
  }

  /**
   * This method is used to get the pre sale return line item detail
   * @return CMSConsignmentLineItemDetail
   */
  public CMSConsignmentLineItemDetail getConsignmentLineItemDetail() {
    return this.consignmentLnItmDtl;
  }

  /**
   *
   * @return Long
   */
  public Long getTypeCode() {
    return this.typeCode;
  }

  /**
   * This method is used to connect pre sale return line item detail
   * @param apreSaleLineItemDetail CMSPresaleLineItemDetail
   */
  public void connectPresaleLineItemDetail(CMSPresaleLineItemDetail apreSaleLineItemDetail) {
    if (apreSaleLineItemDetail == null) {
      return;
    } else {
      this.doSetPresaleLineItemDetail(apreSaleLineItemDetail);
      apreSaleLineItemDetail.doSetReturnLineItemDetail(this);
      return;
    }
  }

  /**
   * This method is used to disconnect the pre sale return line item detail
   * @param apreSaleLineItemDetail CMSPresaleLineItemDetail
   */
  public void disconnectPresaleLineItemDetail(CMSPresaleLineItemDetail apreSaleLineItemDetail) {
    if (apreSaleLineItemDetail == null) {
      return;
    } else {
      doRemovePresaleLineItemDetail(apreSaleLineItemDetail);
      apreSaleLineItemDetail.doRemoveReturnLineItemDetail();
      return;
    }
  }

  /**
   * This method is used to set the pre sale return line item detail
   * @param apreSaleLineItemDetail CMSPresaleLineItemDetail
   */
  public void doRemovePresaleLineItemDetail(CMSPresaleLineItemDetail apreSaleLineItemDetail) {
    this.presaleLnItmDtl = null;
  }

  /**
   * This method is used to connect consignment line item details
   * @param aCsgnLineItemDetail CMSConsignmentLineItemDetail
   */
  public void connectConsignmentLineItemDetail(CMSConsignmentLineItemDetail aCsgnLineItemDetail) {
    if (aCsgnLineItemDetail == null) {
      return;
    } else {
      this.doSetConsignmentLineItemDetail(aCsgnLineItemDetail);
      aCsgnLineItemDetail.doSetReturnLineItemDetail(this);
      return;
    }
  }

  /**
   * This method is used to disconnect consignment line item details
   * @param aCsgnLineItemDetail CMSConsignmentLineItemDetail
   */
  public void disconnectCosignmentLineItemDetail(CMSConsignmentLineItemDetail aCsgnLineItemDetail) {
    if (aCsgnLineItemDetail == null) {
      return;
    } else {
      doRemoveConsignmentLineItemDetail(aCsgnLineItemDetail);
      aCsgnLineItemDetail.doRemoveReturnLineItemDetail();
      return;
    }
  }

  /**
   * This method is used to remove consignment line item detail
   * @param aCsgnLineItemDetail CMSConsignmentLineItemDetail
   */
  public void doRemoveConsignmentLineItemDetail(CMSConsignmentLineItemDetail aCsgnLineItemDetail) {
    this.consignmentLnItmDtl = null;
  }
  public boolean isAvailableForDeal() {
    return (!isUsedInDeal() && ((CMSReturnLineItem)this.getLineItem()).isApplicableForPromotion());
  }

  /**
   * Set ReservationLineItemDetail
   * @param lineItmDtl CMSReservationLineItemDetail
   */
  public void doSetReservationLineItemDetail(CMSReservationLineItemDetail lineItmDtl) {
    this.reservationLnItmDtl = lineItmDtl;
    typeCode = POS_LINE_ITEM_TYPE_RESERVATION;
  }

  /**
   * Get ReservationLineItemDetail
   * @return CMSReservationLineItemDetail
   */
  public CMSReservationLineItemDetail getReservationLineItemDetail() {
    return this.reservationLnItmDtl;
  }

  /**
   * Connect a reservationLineItemDetail.
   * @param aReservationLnItmDtl CMSReservationLineItemDetail
   */
  public void connectReservationLineItemDetail(CMSReservationLineItemDetail aReservationLnItmDtl) {
    if (aReservationLnItmDtl == null)
      return;
    doSetReservationLineItemDetail(aReservationLnItmDtl);
    aReservationLnItmDtl.doSetReturnLineItemDetail(this);
  }

  /**
   * Disconnect reservation line item detail.
   * @param aReservationLnItmDtl CMSReservationLineItemDetail
   */
  public void disconnectReservationLineItemDetail(CMSReservationLineItemDetail aReservationLnItmDtl) {
    if (aReservationLnItmDtl == null)
      return;
    doRemoveReservationLineItemDetail();
    aReservationLnItmDtl.doRemoveSaleLineItemDetail();
  }

  /**
   * Remove reservationLineItem Detail
   */
  public void doRemoveReservationLineItemDetail() {
    this.reservationLnItmDtl = null;
  }

  /**
   * This method is used to get net amount of return line items
   * @return Currency
   */
  public ArmCurrency getNetAmount() {
    //Vivek Mishra : Modified condition for PRESALE CLOSE return.
	  /*if (getPresaleLineItemDetail() == null && getConsignmentLineItemDetail() == null
        && getReservationLineItemDetail() == null)*/
	  if (getConsignmentLineItemDetail() == null && getReservationLineItemDetail() == null)
		//Ends
      return super.getNetAmount();
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to get the reduction amount of return line items
   * @return Currency
   */
  public ArmCurrency getReductionAmount() {
    if (getPresaleLineItemDetail() == null && getConsignmentLineItemDetail() == null
        && getReservationLineItemDetail() == null)
      return super.getReductionAmount();
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * put your documentation comment here
   * @param typecode
   */
  public void doSetTypeCode(Long typecode) {
    this.typeCode = typecode;
  }

  /**
   * This method is used to get the total amount due for the consignment
   * @return Currency
   */
  public ArmCurrency getTotalAmountDue() {
    if (this.getReservationLineItemDetail() != null
        && this.getReservationLineItemDetail().getLineItem().getTransaction()
        == ((CMSCompositePOSTransaction)(this.getReservationLineItemDetail().getLineItem().
        getTransaction().getCompositeTransaction())).getNoReservationOpenTransaction())
      return new ArmCurrency(getBaseCurrencyType(), 0.0d);
    return super.getTotalAmountDue();
  }

  /**
   * This method is used to get the tax amount on the consignment line item
   * @return Currency
   */
  public ArmCurrency getTaxAmount() {
    if (this.getReservationLineItemDetail() != null
        && this.getReservationLineItemDetail().getLineItem().getTransaction()
        == ((CMSCompositePOSTransaction)(this.getReservationLineItemDetail().getLineItem().
        getTransaction().getCompositeTransaction())).getNoReservationOpenTransaction())
      return new ArmCurrency(getBaseCurrencyType(), 0.0d);
    return super.getTaxAmount();
  }
  
  /**
   * Methods for Loyalty Points.
   */
  public double getLoyaltyPoints() {
    return this.loyaltyPoints;
  }

  /**
   *
   * @param LoyaltyPoints doulbe
   */
  public void doSetLoyaltyPoints(double loyaltyPoints) {
    this.loyaltyPoints = loyaltyPoints;
  }

  /**
   *
   * @param LoyaltyPoints double
   */
  public void setLoyaltyPoints(double loyaltyPoints) {
    doSetLoyaltyPoints(loyaltyPoints);
  }

}

