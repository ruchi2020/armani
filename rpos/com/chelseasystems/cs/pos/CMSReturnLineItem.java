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
 | 8    | 10-06-2005 | Pankaja   | N/A       |Restructing during the Merge Activity               |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 7    | 07-12-2005 | Manpreet  | N/A       |Added methods for ReservationLineItem               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 06-19-2005 | Manpreet  | N/A       |Added methods for Document number                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 05-05-2005 | Khyati    | N/A       |Added methods for Tax Jurisdiction                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-17-2005 | Pankaja   | N/A       |Specs Presale/Consignment impl                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-12-2005 | Rajesh    | N/A       |Specs Consignment impl                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import java.util.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.discount.ArmLineDiscount;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;


/**
 *
 * <p>Title: CMSReturnLineItem</p>
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
public class CMSReturnLineItem extends ReturnLineItem implements com.chelseasystems.cr.rules.
    IRuleEngine {
  static final long serialVersionUID = 8007768595310689426L;
  private CMSPresaleLineItem prsLnItm;
  private CMSConsignmentLineItem csgLnItm;
  private CMSReservationLineItem resLnItm;
  private String sVATComment;
  private String taxJusrisdiction;
  public boolean isPriceDiscountAdded = false;
  private Discount priceDiscount = null;
  private Boolean isForExchange;
  private boolean isApplicableForPromotion = true;
  private FiscalDocument fiscalDocument;
  private Vector vecFiscalDocuments;
  private Hashtable hFiscalDocComments = null;
  private Employee oldConsultant = null;
  
  /**
   * Constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   * @param sequenceNumber int
   */
  public CMSReturnLineItem(POSTransaction aTransaction, Item anItem, int sequenceNumber) {
    super(aTransaction, anItem, sequenceNumber);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * Constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   */
  public CMSReturnLineItem(POSTransaction aTransaction, Item anItem) {
    super(aTransaction, anItem);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * This method is used to create return return line item detail
   * @return POSLineItemDetail
   */
  public POSLineItemDetail createLineItemDetail() {
    return new CMSReturnLineItemDetail(this);
  }

  /**
   * This method is used to set pre sale line item
   * @param val CMSPresaleLineItem
   */
  public void doSetPresaleLineItem(CMSPresaleLineItem val) {
    this.prsLnItm = val;
  }

  /**
   * This method is used to get pre sale line item
   * @return CMSPresaleLineItem
   */
  public CMSPresaleLineItem getPresaleLineItem() {
    return this.prsLnItm;
  }

  /**
   * This method is used to set consignment line item
   * @param val CMSConsignmentLineItem
   */
  public void doSetConsignmentLineItem(CMSConsignmentLineItem val) {
    this.csgLnItm = val;
  }

  /**
   * This method is used to get consignment line item
   * @return CMSConsignmentLineItem
   */
  public CMSConsignmentLineItem getConsignmentLineItem() {
    return this.csgLnItm;
  }

  /**
   * This method is used to set reservation line item
   * @param val CMSReservationLineItem
   */
  public void doSetReservationLineItem(CMSReservationLineItem val) {
    this.resLnItm = val;
  }

  /**
   * This method is used to get consignment line item
   * @return CMSReservationLineItem
   */
  public CMSReservationLineItem getReservationLineItem() {
    return this.resLnItm;
  }

  /**
   * This method is used to check for miscellaneous return
   * @return boolean
   */
  public boolean isMiscReturn() {
    return (getSaleLineItem() == null);
  }

  /**
   * This method is used to initialize the line item detail
   * @param quantity int
   */
  public void initializeLineItemDetails(int quantity) {
    cleanupLineItemDetails();
    super.initializeLineItemDetails(quantity);
    CMSReturnLineItemDetail aReturnLineItemDetail;
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      aReturnLineItemDetail = (CMSReturnLineItemDetail)aLineItemDetailList.nextElement();
      //        if (!isMiscReturn() && this.getSaleLineItem() != null)
      //          aReturnLineItemDetail.connectSaleLineItemDetail(this.getSaleLineItem().getSaleLineItemDetailForReturn());
      if (this.getPresaleLineItem() != null) {
        aReturnLineItemDetail.connectPresaleLineItemDetail(this.getPresaleLineItem().
            getPresaleLineItemDetailForReturn());
      }
      if (this.getConsignmentLineItem() != null)
        aReturnLineItemDetail.connectConsignmentLineItemDetail(this.getConsignmentLineItem().
            getConsignmentLineItemDetailForSale());
      if (this.getReservationLineItem() != null)
        aReturnLineItemDetail.connectReservationLineItemDetail(this.getReservationLineItem().
            getReservationLineItemDetailForReturn());
    }
  }

  //ks: Set and get Method for Tax Jurisdiction
  /**
   *
   * @return String
   */
  public String getTaxJurisdiction() {
    return this.taxJusrisdiction;
  }

  /**
   *
   * @param taxJurisdiction String
   */
  public void setTaxJurisdiction(String taxJurisdiction) {
    doSetTaxJurisdiction(taxJurisdiction);
    return;
  }

  /**
   *
   * @param taxJurisdiction String
   */
  public void doSetTaxJurisdiction(String taxJurisdiction) {
    this.taxJusrisdiction = taxJurisdiction;
    return;
  }

  /**
   *
   * @return double
   */
  public double getLoyaltyPointsToReturn() {
    double points = 0.0;
    POSLineItemDetail[] dets = this.getLineItemDetailsArray();
    for (int i = 0; i < dets.length; i++) {
      CMSReturnLineItemDetail retDet = (CMSReturnLineItemDetail)dets[i];
      if (retDet.getSaleLineItemDetail() != null) {
        points = points + ((CMSSaleLineItemDetail)retDet.getSaleLineItemDetail()).getLoyaltyPoints();
      }
    }
    return points;
  }

  /**
   * This method is used to set the Price discount
   * @param priceDiscount CMSDiscount
   */
  public void setAddPriceDiscount(CMSDiscount priceDiscount) {
    this.priceDiscount = priceDiscount;
    isPriceDiscountAdded = true;
  }

  /**
   * This method is used to remove the added Price discount
   */
  public void removeAddPriceDiscount() {
    try {
      if (priceDiscount != null) {
        this.removeDiscount(priceDiscount);
        priceDiscount = null;
      }
      isPriceDiscountAdded = false;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * This method is used to get the Price discount for a given line item
   * @return Discount
   */
  public Discount getPriceDiscount() {
    return this.priceDiscount;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception CurrencyException
   */
  public ArmCurrency getExtendedDealMarkdownAmount()
      throws CurrencyException {
    POSLineItemDetail[] lnItmDtlArr = this.getLineItemDetailsArray();
    ArmCurrency currTotalDealMarkdownAmt = new ArmCurrency(0.0d);
    if (lnItmDtlArr != null && lnItmDtlArr.length > 0) {
      for (int i = 0; i < lnItmDtlArr.length; i++) {
        currTotalDealMarkdownAmt = currTotalDealMarkdownAmt.add(lnItmDtlArr[i].
            getDealMarkdownAmount());
      }
    }
    return currTotalDealMarkdownAmt;
  }

  /**
   * Set VATComment
   * @param VATComment String
   */
  public void setVatComment(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetVATComment(sValue);
  }

  /**
   * DoSet VATComment
   * @param VATComment String
   */
  public void doSetVATComment(String sValue) {
    this.sVATComment = sValue;
  }

  /**
   * Get VATComment
   * @return VATComment
   */
  public String getVATComment() {
    return this.sVATComment;
  }

  /** Should the Line Item be considered for Promotion
   * @return
   */
  public boolean isApplicableForPromotion() {
    return this.isApplicableForPromotion;
  }

  /** Remove Promotion from the Line Item
   * @exception BusinessRuleException
   */
  public void removePromotion()
      throws BusinessRuleException {
    executeRule("removePromotion", this);
    doRemovePromotion();
    zeroAllLineItemDetailAmounts();
    this.getTransaction().getCompositeTransaction().update();
  }

  /**
   */
  public void doRemovePromotion() {
    this.isApplicableForPromotion = false;
  }
  /**
   * For Europe: Storing the status String R/E
   * @return Boolean
   */
  public Boolean getIsForExchange() {
    return this.isForExchange;
  }

  /**
   * For Europe: Storing the status String R/E
   * @return Boolean
   */
  public void doSetIsForExchange(Boolean isForExchange) {
    this.isForExchange = isForExchange;
  }

  /**
   *
   * @param aDiscount Discount
   * @throws BusinessRuleException
   */
  public void addDiscount(Discount aDiscount)
      throws BusinessRuleException {
    checkForNullParameter("addDiscount", aDiscount);
    executeRule("addDiscount", aDiscount);
    if (aDiscount instanceof ArmLineDiscount) {
      try {
        ((ArmLineDiscount)aDiscount).addDiscountNetAmount(this.getExtendedNetAmount());
      } catch (CurrencyException ce) {}
      ((ArmLineDiscount)aDiscount).doAddLineItem(this);
      doAddDiscount(aDiscount);
      return;
    }
    doAddDiscount(aDiscount);
    broadcastUpdate();
  }
  /**
   * Add FiscalDocument
   * @param fiscalDocument FiscalDocument
   */
  public void addFiscalDocument(FiscalDocument fiscalDocument) {
    if (fiscalDocument == null)
      return;
    doAddFiscalDocument(fiscalDocument);
  }

  /**
   * Remove FiscalDocument
   * @param fiscalDocument FiscalDocument
   */
  public void removeFiscalDocument(FiscalDocument fiscalDocument) {
    if (fiscalDocument == null)
      return;
    doRemoveFiscalDocument(fiscalDocument);
  }

  /**
   * FiscalDocument
   * @param iIndex int
   * @return FiscalDocument
   */
  public FiscalDocument getFiscalDocumentAt(int iIndex) {
    if (iIndex < 0 || vecFiscalDocuments == null || iIndex >= vecFiscalDocuments.size())
      return null;
    return (FiscalDocument)vecFiscalDocuments.elementAt(iIndex);
  }

  /**
   * Fiscal Document array.
   * @return FiscalDocument[]
   */
  public FiscalDocument[] getFiscalDocumentArray() {
    return (FiscalDocument[])vecFiscalDocuments.toArray(new FiscalDocument[vecFiscalDocuments.size()]);
  }

  /**
   * Enumerated FiscalDocuments
   * @return Enumeration
   */
  public Enumeration getFiscalDocuments() {
    return vecFiscalDocuments.elements();
  }

  /**
   * Add Fiscal Document
   * @param fiscalDocument FiscalDocument
   */
  public void doAddFiscalDocument(FiscalDocument fiscalDocument) {
    vecFiscalDocuments.addElement(fiscalDocument);
  }

  /**
   * Remove Fiscal Document
   * @param fiscalDocument FiscalDocument
   */
  public void doRemoveFiscalDocument(FiscalDocument fiscalDocument) {
    vecFiscalDocuments.remove(fiscalDocument);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean hasFiscalDocument() {
    return fiscalDocument != null;
  }

  /**
   * @param docType
   * @return
   */
  public boolean isDocumentPrintedForDocType(String docType) {
    FiscalDocument[] documents = this.getFiscalDocumentArray();
    for (int index = 0; index < documents.length; index++) {
      if (documents[index].getDocumentType().equals(docType))
        return true;
    }
    return false;
  }

  /**
  *
  * @return double
  */
 public double getLoyaltyPoints() {
   double points = 0.0;
   POSLineItemDetail[] dets = this.getLineItemDetailsArray();
   for (int i = 0; i < dets.length; i++) {
     points = points + ((CMSReturnLineItemDetail)dets[i]).getLoyaltyPoints();
   }
   return points;
 }

  /**
   * @exception BusinessRuleException
   */
  public void clearManualUnitPrice()
      throws BusinessRuleException {
    executeRule("clearManualUnitPrice");
    doSetManualUnitPrice(null);
    doSetItemRetailPrice(null);
    doSetItemSellingPrice(null);
  }

  /**
   * @param aDiscount
   * @exception BusinessRuleException
   */
  public void removeDiscount(Discount aDiscount)
      throws BusinessRuleException {
    checkForNullParameter("removeDiscount", aDiscount);
    executeRule("removeDiscount", aDiscount);
    doRemoveDiscount(aDiscount);
    //        broadcastUpdate();
  }

  /**
   * @exception BusinessRuleException
   */
  public void removeAllDiscounts()
      throws BusinessRuleException {
    executeRule("removeAllDiscounts");
    doRemoveAllDiscounts();
    //        broadcastUpdate();
  }

  /**
   * Set Fiscal Document Comment
   * @param Fiscal Document Comment String
   */
  public void addFiscalDocComment(String type, String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doAddFiscalDocComment(type, sValue);
  }

  /**
   * Do Set Fiscal Document Comment
   * @param Fiscal Document Comment String
   */
  public void doAddFiscalDocComment(String type, String sValue) {
    if (hFiscalDocComments.containsKey(type)) {
      String comments = (String)hFiscalDocComments.get(type);
      comments = sValue;
      hFiscalDocComments.put(type, sValue);
    } else
      hFiscalDocComments.put(type, sValue);
  }

  /**
   * Get Fiscal Document Comment
   * @return Fiscal Document Comment
   */
  public String getFiscalDocComment(String type) {
    return (String)hFiscalDocComments.get(type);
  }
  
  public Employee getOldConsultant(){
    return this.oldConsultant;
  }

  public void setOldConsultant(Employee oldConsultant){
    this.oldConsultant = oldConsultant;
  }
  
}
