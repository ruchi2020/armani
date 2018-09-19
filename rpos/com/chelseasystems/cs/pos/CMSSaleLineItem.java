/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 10   | 10-06-2005 | Pankaja   | N/A       |Restructing during the Merge Activity         |
 +------+-----------+-----------+-----------+-----------------------------------------------+
 | 9    | 07-12-2005 | Manpreet  | N/A       |Added methods for ReservationLineItem         |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 8   | 05-13-2005 | Rajesh    | N/A       | added helper methods               |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 7    | 05-05-2005 | Khyati    | N/A       |Added methods for Tax Jurisdiction            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 6    | 04-17-2005 | Pankaja   | N/A       |Specs Presale/Consignment impl                |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 04-12-2005 | Rajesh    | N/A       |Specs Consignment impl                        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-11-2005 | Khyati    | N/A       |1.Return Specification                        |
 ---------------------------------------------------------------------------------------------
 | 2    | 03-20-2005 | Khyati    | N/A       |1.Discount Specification                      |
 ---------------------------------------------------------------------------------------------
 | 1    | 03-20-2005 | Khyati    | N/A       | Base                                         |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import java.io.*;
import java.util.*;
import com.chelseasystems.cr.business.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cr.employee.Employee;


/**
 *
 * <p>Title: CMSSaleLineItem</p>
 *
 * <p>Description: This class store the information of sale line item</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Pankaja
 * @version 1.0
 */
public class CMSSaleLineItem extends SaleLineItem implements com.chelseasystems.cr.rules.
    IRuleEngine {
  static final long serialVersionUID = -1305551277290338756L;
  private Hashtable hashLineDiscount = new Hashtable(60);
  public boolean isPriceDiscountAdded = false;
  private Discount priceDiscount = null;
  public boolean isSelectedForReturn = false;
  private CMSPresaleLineItem prsLnItm;
  private CMSConsignmentLineItem csgLnItm;
  private CMSReservationLineItem resLnItm;
  private String taxJusrisdiction;
  // For Europe
  private String sVATComment;
  private FiscalDocument fiscalDocument;
  private boolean isApplicableForPromotion = true;
  private Vector vecFiscalDocuments;
  private Hashtable hFiscalDocComments = null;
  private Employee oldConsultant = null;
private CMSMiscItem taxableMiscLineItem = null;
  private CMSSaleLineItem alterationLineItem = null;
  
  /**
   * Constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   * @param sequenceNumber int
   */
  public CMSSaleLineItem(POSTransaction aTransaction, Item anItem, int sequenceNumber) {
    super(aTransaction, anItem, sequenceNumber);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * Constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   */
  public CMSSaleLineItem(POSTransaction aTransaction, Item anItem) {
    super(aTransaction, anItem);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * Constructor
   * @return POSLineItemDetail
   */
  public POSLineItemDetail createLineItemDetail() {
    return new CMSSaleLineItemDetail(this);
  }

  /**
   * This method is used to get line item discount detail
   * @return Hashtable
   */
  public Hashtable getLineItemDiscountDetails() {
    return this.hashLineDiscount;
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
   * This method is used to set the item as selected for return in the same txn
   * @param isSelectedForReturn boolean
   */
  public void setIsSelectedForReturn(boolean isSelectedForReturn) {
    this.isSelectedForReturn = isSelectedForReturn;
  }

  /**
   * This method is used to set the pre sale line item
   * @param val CMSPresaleLineItem
   */
  public void doSetPresaleLineItem(CMSPresaleLineItem val) {
    this.prsLnItm = val;
  }

  /**
   * This method is used to get the pre sale line item
   * @return CMSPresaleLineItem
   */
  public CMSPresaleLineItem getPresaleLineItem() {
    return this.prsLnItm;
  }

  /**
   * This method is used to set the consignment line item
   * @param val CMSConsignmentLineItem
   */
  public void doSetConsignmentLineItem(CMSConsignmentLineItem val) {
    this.csgLnItm = val;
  }

  /**
   * This method is used to get the consignment line item
   * @return CMSConsignmentLineItem
   */
  public CMSConsignmentLineItem getConsignmentLineItem() {
    return this.csgLnItm;
  }

  /**
   * This method is used to set the reservation line item
   * @param val CMSReservationLineItem
   */
  public void doSetReservationLineItem(CMSReservationLineItem val) {
    this.resLnItm = val;
  }

  /**
   * This method is used to get the consignment line item
   * @return CMSConsignmentLineItem
   */
  public CMSReservationLineItem getReservationLineItem() {
    return this.resLnItm;
  }

  /**
   * This method is used to clean line item
   */
  public void cleanupLineItem() {
    this.prsLnItm = null;
    this.csgLnItm = null;
    this.resLnItm = null;    
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements();
        ((POSLineItemDetail)aLineItemDetailList.nextElement()).cleanup());
  }

  /**
   * This method is used to initialize line item details
   * @param quantity int
   */
  public void initializeLineItemDetails(int quantity) {
    cleanupLineItemDetails();
    super.initializeLineItemDetails(quantity);
    CMSSaleLineItemDetail aSaleLineItemDetail;
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      aSaleLineItemDetail = (CMSSaleLineItemDetail)aLineItemDetailList.nextElement();
      if (this.getPresaleLineItem() != null)
        aSaleLineItemDetail.connectPresaleLineItemDetail(this.getPresaleLineItem().
            getPresaleLineItemDetailForSale());
      if (this.getConsignmentLineItem() != null)
        aSaleLineItemDetail.connectConsignmentLineItemDetail(this.getConsignmentLineItem().
            getConsignmentLineItemDetailForSale());
      if (this.getReservationLineItem() != null)
        aSaleLineItemDetail.connectReservationLineItemDetail(this.getReservationLineItem().
            getReservationLineItemDetailForSale());
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
  public double getLoyaltyPoints() {
    double points = 0.0;
    POSLineItemDetail[] dets = this.getLineItemDetailsArray();
    for (int i = 0; i < dets.length; i++) {
      points = points + ((CMSSaleLineItemDetail)dets[i]).getLoyaltyPoints();
    }
    return points;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception CurrencyException
   */
  public ArmCurrency getExtendedDealMarkdownAmount()
      throws CurrencyException {
    POSLineItemDetail[] lnItmDtlArr = this.getLineItemDetailsArray();
    ArmCurrency currTotalDealMarkdownAmt = new ArmCurrency(getBaseCurrencyType(), 0.0d);
    if (lnItmDtlArr != null && lnItmDtlArr.length > 0) {
      for (int i = 0; i < lnItmDtlArr.length; i++) {
        currTotalDealMarkdownAmt = currTotalDealMarkdownAmt.add(lnItmDtlArr[i].
            getDealMarkdownAmount());
      }
    }
    return currTotalDealMarkdownAmt;
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
		//1809:Uncommented because deleted lineItemDiscounts were not refreshed properly.
		broadcastUpdate();
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
public CMSMiscItem getTaxableMiscLineItem(){
    return this.taxableMiscLineItem;
  }

  public void setTaxableMiscLineItem(CMSMiscItem taxableMiscLineItem){
    this.taxableMiscLineItem = taxableMiscLineItem;
  }

  public void setAlterationLineItem(CMSSaleLineItem saleLineItem){
    this.alterationLineItem = saleLineItem;
  }

  public CMSSaleLineItem getAlterationLineItem(){
    return this.alterationLineItem;
  }
}
