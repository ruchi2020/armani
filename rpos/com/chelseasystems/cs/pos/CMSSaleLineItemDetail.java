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
 | 9    | 10-06-2005 | Pankaja   | N/A       |Restructing during the Merge Activity               |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 8    | 07-12-2005 | Khyati    | N/A       |Added methods for setting and getting VATAmount     |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 7    | 07-12-2005 | Manpreet  | N/A       |Added methods for ReservationLineItem               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    |  05-03-2005|  Megha    | N/A       | Added Loyalty Points                                               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 04-19-2005 | Manpreet  | N/A       |POS_104665_TS_Alterations_Rev2                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-17-2005 | Pankaja   | N/A       |Specs Presale/Consignment impl                      |    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-12-2005 | Rajesh    | N/A       |Specs Consignment impl                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import java.util.List;
import java.util.ArrayList;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: CMSSaleLineItemDetail</p>
 *
 * <p>Description: This class store the information of sale line item</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet
 * @version 1.0
 */
public class CMSSaleLineItemDetail extends SaleLineItemDetail implements com.chelseasystems.cr.
    rules.IRuleEngine {
  static final long serialVersionUID = 968298697270990018L;
  private CMSPresaleLineItemDetail presaleLnItmDtl;
  private CMSConsignmentLineItemDetail consignmentLnItmDtl;
  private CMSReservationLineItemDetail reservationLnItmDtl;
  private double manualTaxRate = 0.0d;
  private Long typeCode = new Long(0);
  public static final Long POS_LINE_ITEM_TYPE_RETURN = new Long(2);
  public static final Long POS_LINE_ITEM_TYPE_PRESALE = new Long(4);
  public static final Long POS_LINE_ITEM_TYPE_CONSIGNMENT = new Long(5);
  public static final Long POS_LINE_ITEM_TYPE_RESERVATION = new Long(6);
  private double taxRate = 0.0d;
  private boolean isManualTaxPercent = false;
  private boolean isTaxRateDefined = false;
  /**
   * For Loyalty Points
   */
  private double LoyaltyPoints = 0.0;
  /**
   * List to hold alteration line item detail objects.
   */
  private List listAlterationLineItemDetails;
  private String manualAuthCode;

  /**
   * Constructor
   * @param lineItem POSLineItem
   */
  public CMSSaleLineItemDetail(POSLineItem lineItem) {
    super(lineItem);
  }

  /**
   * Constructor
   * @param val CMSPresaleLineItemDetail
   */
  public void doSetPresaleLineItemDetail(CMSPresaleLineItemDetail val) {
    this.presaleLnItmDtl = val;
    typeCode = POS_LINE_ITEM_TYPE_PRESALE;
  }

  /**
   * This method is used to get pre sale line item detail
   * @return CMSPresaleLineItemDetail
   */
  public CMSPresaleLineItemDetail getPresaleLineItemDetail() {
    return this.presaleLnItmDtl;
  }

  /**
   * This method is used to set consignment line item detail
   * @param val CMSConsignmentLineItemDetail
   */
  public void doSetConsignmentLineItemDetail(CMSConsignmentLineItemDetail val) {
    this.consignmentLnItmDtl = val;
    typeCode = POS_LINE_ITEM_TYPE_CONSIGNMENT;
  }

  /**
   * This method is used to get consignment line item detail
   * @return CMSConsignmentLineItemDetail
   */
  public CMSConsignmentLineItemDetail getConsignmentLineItemDetail() {
    return this.consignmentLnItmDtl;
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
   *
   * @return Long
   */
  public Long getTypeCode() {
    if (this.isReturned()) {
      this.typeCode = POS_LINE_ITEM_TYPE_RETURN;
    }
    return this.typeCode;
  }

  /**
   * This method is used to connect pre sale line item detail
   * @param apreSaleLineItemDetail CMSPresaleLineItemDetail
   */
  public void connectPresaleLineItemDetail(CMSPresaleLineItemDetail apreSaleLineItemDetail) {
    if (apreSaleLineItemDetail == null) {
      return;
    } else {
      this.doSetPresaleLineItemDetail(apreSaleLineItemDetail);
      apreSaleLineItemDetail.doSetSaleLineItemDetail(this);
      return;
    }
  }

  /**
   * This method is used to disconnect pre sale line item detail
   * @param apreSaleLineItemDetail CMSPresaleLineItemDetail
   */
  public void disconnectPresaleLineItemDetail(CMSPresaleLineItemDetail apreSaleLineItemDetail) {
    if (apreSaleLineItemDetail == null) {
      return;
    } else {
      doRemovePresaleLineItemDetail(apreSaleLineItemDetail);
      apreSaleLineItemDetail.doRemoveSaleLineItemDetail();
      return;
    }
  }

  /**
   * This method is used to remove pre sale line item detail
   * @param apreSaleLineItemDetail CMSPresaleLineItemDetail
   */
  public void doRemovePresaleLineItemDetail(CMSPresaleLineItemDetail apreSaleLineItemDetail) {
    this.presaleLnItmDtl = null;
  }

  /**
   * This method is used to connect consignment line item detail
   * @param aCsgnLineItemDetail CMSConsignmentLineItemDetail
   */
  public void connectConsignmentLineItemDetail(CMSConsignmentLineItemDetail aCsgnLineItemDetail) {
    if (aCsgnLineItemDetail == null) {
      return;
    } else {
      this.doSetConsignmentLineItemDetail(aCsgnLineItemDetail);
      aCsgnLineItemDetail.doSetSaleLineItemDetail(this);
      return;
    }
  }

  /**
   * This method is used to disconnect consignment line item detail
   * @param aCsgnLineItemDetail CMSConsignmentLineItemDetail
   */
  public void disconnectCosignmentLineItemDetail(CMSConsignmentLineItemDetail aCsgnLineItemDetail) {
    if (aCsgnLineItemDetail == null) {
      return;
    } else {
      doRemoveConsignmentLineItemDetail(aCsgnLineItemDetail);
      aCsgnLineItemDetail.doRemoveSaleLineItemDetail();
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

  /**
   * Connect a reservationLineItemDetail.
   * @param aReservationLnItmDtl CMSReservationLineItemDetail
   */
  public void connectReservationLineItemDetail(CMSReservationLineItemDetail aReservationLnItmDtl) {
    if (aReservationLnItmDtl == null)
      return;
    doSetReservationLineItemDetail(aReservationLnItmDtl);
    aReservationLnItmDtl.doSetSaleLineItemDetail(this);
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
   * This method is used to clean consignment and pre sale line item details
   */
  public void cleanup() {
    this.consignmentLnItmDtl = null;
    this.presaleLnItmDtl = null;
    this.reservationLnItmDtl = null;
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
   * @author Manpreet S bawa
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
   * Methods for Loyalty Points.
   */
  public double getLoyaltyPoints() {
    return this.LoyaltyPoints;
  }

  /**
   *
   * @param LoyaltyPoints doulbe
   */
  public void doSetLoyaltyPoints(double LoyaltyPoints) {
    this.LoyaltyPoints = LoyaltyPoints;
  }

  /**
   *
   * @param LoyaltyPoints double
   */
  public void setLoyaltyPoints(double LoyaltyPoints) {
    doSetLoyaltyPoints(LoyaltyPoints);
  }

  /**
   *
   * @return String
   */
  public String getManualAuthCode() {
    return manualAuthCode;
  }

  /**
   *
   * @param manualAuthCode String
   */
  public void setManualAuthCode(String manualAuthCode) {
    this.manualAuthCode = manualAuthCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double getTaxRate() {
    return taxRate;
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

  /**
   * put your documentation comment here
   * @return
   */
  public double getManualTaxRate() {
    return manualTaxRate;
  }

  /**
   * put your documentation comment here
   * @param manualTaxRate
   */
  public void setManualTaxRate(double manualTaxRate) {
    doSetManualTaxRate(manualTaxRate);
  }

  /**
   * put your documentation comment here
   * @param manualTaxRate
   */
  public void doSetManualTaxRate(double manualTaxRate) {
    this.manualTaxRate = manualTaxRate;
    this.isManualTaxPercent = true;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isManualTaxPercent() {
    return this.isManualTaxPercent;
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void clearManualTaxAmount()
      throws BusinessRuleException {
    super.clearManualTaxAmount();
    this.isManualTaxPercent = false;
    this.manualTaxRate = 0.0d;
  }

  // New method added for Inquiry Screen
  public boolean isTaxRateDefined() {
    return this.isTaxRateDefined;
  }

  /**
   * put your documentation comment here
   * @param typecode
   */
  public void doSetTypeCode(Long typecode) {
    this.typeCode = typecode;
  }
  public boolean isAvailableForDeal() {
    return (!isUsedInDeal() && ((CMSSaleLineItem)this.getLineItem()).isApplicableForPromotion());
  }
}

