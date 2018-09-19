/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 10-06-2005 | Pankaja   | N/A       |Restructing during the Merge Activity               |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 1    | 07-11-2005 | Manpreet  | N/A       |  POS_104665_TS_Reservations_Rev4                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.item.*;
import java.io.Serializable;
import java.util.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;

/**
 *
 * <p>Title: CMSPresaleLineItem</p>
 *
 * <p>Description: This class store the Reservation line item</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet
 * @version 1.0
 */
public class CMSReservationLineItem extends POSLineItem implements Serializable, IRuleEngine {
  private Hashtable hashLineDiscount = new Hashtable(60);
  public boolean isPriceDiscountAdded = false;
  private Discount priceDiscount = null;
  private CMSSaleLineItem saleLnItm;
  private CMSReturnLineItem rtnLnItm;
  private FiscalDocument fiscalDocument;
  private String sVATComment;
  private Vector vecFiscalDocuments;
  private CMSMiscItem taxableMiscLineItem = null;
  private CMSSaleLineItem alterationLineItem = null;
  private Hashtable hFiscalDocComments = null;

  /**
   * Constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   * @param sequenceNumber int
   */
  public CMSReservationLineItem(POSTransaction aTransaction, Item anItem, int sequenceNumber) {
    super(aTransaction, anItem, sequenceNumber);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   */
  public CMSReservationLineItem(POSTransaction aTransaction, Item anItem) {
    this(aTransaction, anItem, aTransaction.getCompositeTransaction().getNextSequenceNumber());
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * This method is used to create pre sale line item detail
   * @return POSLineItemDetail
   */
  public POSLineItemDetail createLineItemDetail() {
    return new CMSReservationLineItemDetail(this);
  }

  /**
   * This method is used to set sale line item
   * @param val CMSSaleLineItem
   */
  public void doSetSaleLineItem(CMSSaleLineItem val) {
    this.saleLnItm = val;
  }

  /**
   * This method is used to get the sale line item
   * @return CMSSaleLineItem
   */
  public CMSSaleLineItem getSaleLineItem() {
    return this.saleLnItm;
  }

  /**
   * This method is used to set return line item
   * @param val CMSReturnLineItem
   */
  public void doSetReturnLineItem(CMSReturnLineItem val) {
    this.rtnLnItm = val;
  }

  /**
   * This method is used to get return line item
   * @return CMSReturnLineItem
   */
  public CMSReturnLineItem getReturnLineItem() {
    return this.rtnLnItm;
  }

  /**
   * This method is used to get item selling price
   * @return Currency
   */
  public ArmCurrency getItemSellingPrice() {
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to get pre sale quantity available for return
   * @return int
   */
  public int getQuantityAvailableForReturn() {
    int total = getQuantity().intValue();
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); )
      if (((CMSReservationLineItemDetail)aLineItemDetailList.nextElement()).getProcessed())
        total--;
    return total;
  }

  /**
   * This method is used to get pre sale line item discount detail
   * @return Hashtable
   */
  public Hashtable getLineItemDiscountDetails() {
    return this.hashLineDiscount;
  }

  /**
   * This method is used to set add price discount
   * @param priceDiscount CMSDiscount
   */
  public void setAddPriceDiscount(CMSDiscount priceDiscount) {
    this.priceDiscount = priceDiscount;
    isPriceDiscountAdded = true;
  }

  /**
   * This method is used to remove price discount
   */
  public void removeAddPriceDiscount() {
    try {
      if (priceDiscount != null)
        this.removeDiscount(priceDiscount);
      isPriceDiscountAdded = false;
    } catch (Exception ex) {}
  }

  /**
   * This method is used to get price discount
   * @return Discount
   */
  public Discount getPriceDiscount() {
    return this.priceDiscount;
  }

  /**
   *
   * @param aLineItem POSLineItem
   */
  public void transferInformationTo(POSLineItem aLineItem) {
    aLineItem.doSetManualMarkdownAmount(getManualMarkdownAmount());
    aLineItem.doSetManualMarkdownReason(getManualMarkdownReason());
    aLineItem.doSetManualUnitPrice(getManualUnitPrice());
    aLineItem.doSetItemRetailPrice(getItemRetailPrice());
    aLineItem.doSetAdditionalConsultant(getAdditionalConsultant());
    aLineItem.doSetTaxExemptId(getTaxExemptId());
    aLineItem.doSetRegionalTaxExemptId(getRegionalTaxExemptId());
    aLineItem.doSetMiscItemId(getMiscItemId());
    aLineItem.doSetMiscItemDescription(doGetMiscItemDescription());
    aLineItem.doSetMiscItemTaxable(doGetMiscItemTaxable());
    aLineItem.doSetMiscItemRegionalTaxable(doGetMiscItemRegionalTaxable());
    aLineItem.doSetMiscItemGLAccount(getMiscItemGLAccount());
    aLineItem.doSetMiscItemComment(getMiscItemComment());
    aLineItem.getLineItemGrouping().doSetItemPriceOverrideWithoutReset(getLineItemGrouping().
        getItemPriceOverride());
  }

  /**
   * This method is used to get reservation line item detail for return
   * @return CMSReservationLineItemDetail
   */
  public CMSReservationLineItemDetail getReservationLineItemDetailForReturn() {
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      CMSReservationLineItemDetail aResSaleLineItemDetail = (CMSReservationLineItemDetail)
          aLineItemDetailList.nextElement();
      if (!aResSaleLineItemDetail.getProcessed())
        return aResSaleLineItemDetail;
    }
    return null;
  }

  /**
   * This method is used to get reservation line item detail for sale
   * @return CMSReservationLineItemDetail
   */
  public CMSReservationLineItemDetail getReservationLineItemDetailForSale() {
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      CMSReservationLineItemDetail aReservationItmDtl = (CMSReservationLineItemDetail)
          aLineItemDetailList.nextElement();
      if (!aReservationItmDtl.getProcessed())
        return aReservationItmDtl;
    }
    return null;
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

  public boolean isDocumentPrintedForDocType(String docType) {
    FiscalDocument[] documents = this.getFiscalDocumentArray();
    for (int index = 0; index < documents.length; index++) {
      if (documents[index].getDocumentType().equals(docType))
        return true;
    }
    return false;
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
      }
      else
          hFiscalDocComments.put(type, sValue);
  }

  /**
   * Get Fiscal Document Comment
   * @return Fiscal Document Comment
   */
  public String getFiscalDocComment(String type) {
      return (String)hFiscalDocComments.get(type);
  }

}

