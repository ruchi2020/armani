/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-17-2005 | Pankaja   | N/A       | New method getConsignmentLineItemDetailForSale     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-13-2005 | Rajesh    | N/A       | Override required method                           |
 +------+------------+-----------+-----------+----------------------------------------------------+ *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.item.*;
import java.io.Serializable;
import java.util.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;

import java.util.Enumeration;
import java.util.Vector;


/**
 * <p>Title: CMSConsignmentLineItem</p>
 * <p>Description: This class is used to store consignment line item attribures </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Rajesh Pradhan
 * @version 1.0
 */
public class CMSConsignmentLineItem extends POSLineItem implements Serializable, IRuleEngine {
  private Hashtable hashLineDiscount = new Hashtable(60);
  public boolean isPriceDiscountAdded = false;
  private Discount priceDiscount = null;
  private CMSSaleLineItem saleLnItm;
  private CMSReturnLineItem rtnLnItm;
  private ShippingRequest shippingRequest = null;
  private FiscalDocument fiscalDocument = null;
  private String sVATComment;
  private Vector vecFiscalDocuments;
  private CMSMiscItem taxableMiscLineItem = null;
  private CMSSaleLineItem alterationLineItem = null;
  private Hashtable hFiscalDocComments = null;

  /**
   * Contructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   * @param sequenceNumber int
   */
  public CMSConsignmentLineItem(POSTransaction aTransaction, Item anItem, int sequenceNumber) {
    super(aTransaction, anItem, sequenceNumber);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * Constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   */
  public CMSConsignmentLineItem(POSTransaction aTransaction, Item anItem) {
    this(aTransaction, anItem, aTransaction.getCompositeTransaction().getNextSequenceNumber());
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * This method is used to create the line item details for the consignment
   * @return POSLineItemDetail
   */
  public POSLineItemDetail createLineItemDetail() {
    return new CMSConsignmentLineItemDetail(this);
  }

  /**
   * This method is used to set the sale line item of the consignment
   * @param val CMSSaleLineItem
   */
  public void doSetSaleLineItem(CMSSaleLineItem val) {
    this.saleLnItm = val;
  }

  /**
   * This method is used to set the sale line item of the consignment
   * @return CMSSaleLineItem
   */
  public CMSSaleLineItem getSaleLineItem() {
    return this.saleLnItm;
  }

  /**
   * This method is used to set the return line item of the consignment
   * @param val CMSReturnLineItem
   */
  public void doSetReturnLineItem(CMSReturnLineItem val) {
    this.rtnLnItm = val;
  }

  /**
   * This method is used to get return line item of the consignment
   * @return CMSReturnLineItem
   */
  public CMSReturnLineItem getReturnLineItem() {
    return this.rtnLnItm;
  }

  /**
   * This method is used to get the consignment line item selling price
   * @return Currency
   */
  public ArmCurrency getItemSellingPrice() {
    return new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to get the quantity available of consignment line item
   * for return
   * @return int
   */
  public int getQuantityAvailableForReturn() {
    int total = getQuantity().intValue();
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); )
      if (((CMSConsignmentLineItemDetail)aLineItemDetailList.nextElement()).getProcessed())
        total--;
    return total;
  }

  /**
   * This method is used to get discount details of consignment line item
   * @return Hashtable
   */
  public Hashtable getLineItemDiscountDetails() {
    return this.hashLineDiscount;
  }

  /**
   * This method is used to set the price discount on consignment line item
   * @param priceDiscount CMSDiscount
   */
  public void setAddPriceDiscount(CMSDiscount priceDiscount) {
    this.priceDiscount = priceDiscount;
    isPriceDiscountAdded = true;
  }

  /**
   * This method is used to remove the price discount on consignment line item
   */
  public void removeAddPriceDiscount() {
    try {
      if (priceDiscount != null)
        this.removeDiscount(priceDiscount);
      isPriceDiscountAdded = false;
    } catch (Exception ex) {}
  }

  /**
   * This method is used to get the price discount on consignment line item
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
   * This method is used to get the shipping request data
   * @return ShippingRequest
   */
  public ShippingRequest getShippingRequest() {
    return shippingRequest;
  }

  /**
   * This method is used to set the shipping request data
   * @param aShippingRequest ShippingRequest
   * @throws BusinessRuleException
   */
  public void setShippingRequest(ShippingRequest aShippingRequest)
      throws BusinessRuleException {
    checkForNullParameter("setShippingRequest", aShippingRequest);
    executeRule("setShippingRequest", aShippingRequest);
    if (hasShippingRequest())
      ((CMSShippingRequest)shippingRequest).doRemoveConsignmentLineItem(this);
    doSetShippingRequest(aShippingRequest);
    ((CMSShippingRequest)shippingRequest).doAddConsignmentLineItem(this);
  }

  /**
   * This method is used to clear previous shipping request data
   * @throws BusinessRuleException
   */
  public void clearShippingRequest()
      throws BusinessRuleException {
    executeRule("clearShippingRequest");
    /*  if(hasShippingRequest())
     shippingRequest.doRemoveConsignmentLineItem(this);*/
    doClearShippingRequest();
  }

  /**
   * This method is used to set the shipping request data
   * @param aShippingRequest ShippingRequest
   */
  public void doSetShippingRequest(ShippingRequest aShippingRequest) {
    shippingRequest = aShippingRequest;
  }

  /**
   * This method is used to clear previous shipping request data
   */
  public void doClearShippingRequest() {
    shippingRequest = null;
  }

  /**
   * This method is used to check whether customer has requested for shipping or not
   * @return boolean
   */
  public boolean hasShippingRequest() {
    return shippingRequest != null;
  }

  /**
   * This method returns the selling consignment line item details
   * @return CMSConsignmentLineItemDetail
   */
  public CMSConsignmentLineItemDetail getConsignmentLineItemDetailForSale() {
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      CMSConsignmentLineItemDetail acsgnLineItemDetail = (CMSConsignmentLineItemDetail)
          aLineItemDetailList.nextElement();
      if (!acsgnLineItemDetail.getProcessed())
        return acsgnLineItemDetail;
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

