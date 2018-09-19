/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.POSTransaction;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cr.currency.ArmCurrency;

import java.util.Enumeration;
import java.util.Vector;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.discount.Discount;
import java.util.Hashtable;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.discount.ArmLineDiscount;
import com.chelseasystems.cr.currency.CurrencyException;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSNoReturnLineItem extends ReturnLineItem {
  //Europe
  FiscalDocument fiscalDocument = null;
  private Vector vecFiscalDocuments;
  private Hashtable hFiscalDocComments = null;

  private Hashtable hashLineDiscount = new Hashtable(60);
  public boolean isPriceDiscountAdded = false;
  private Discount priceDiscount = null;

  /**
   *
   * @param aTransaction POSTransaction
   * @param anItem Item
   */
  public CMSNoReturnLineItem(POSTransaction aTransaction, Item anItem) {
    super(aTransaction, anItem, aTransaction.getCompositeTransaction().getNextSequenceNumber());
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   *
   * @param aTransaction POSTransaction
   * @param anItem Item
   * @param aSequenceNumber int
   */
  public CMSNoReturnLineItem(POSTransaction aTransaction, Item anItem, int aSequenceNumber) {
    super(aTransaction, anItem, aSequenceNumber);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * createLineItemDetail
   *
   * @return POSLineItemDetail
   * @todo Implement this com.chelseasystems.cr.pos.POSLineItem method
   */
  public POSLineItemDetail createLineItemDetail() {
    return new CMSNoReturnLineItemDetail(this);
  }

  /**
   *
   * @param fiscalDocument FiscalDocument
   */
  public void setFiscalDocument(FiscalDocument fiscalDocument)
      throws BusinessRuleException {
    executeRule("setFiscalDocument", fiscalDocument);
    doSetFiscalDocument(fiscalDocument);
  }

  /**
   *
   * @throws BusinessRuleException
   */
  public void clearFiscalDocument()
      throws BusinessRuleException {
    executeRule("clearFiscalDocument");
    if (hasFiscalDocument()) {
      fiscalDocument.doRemoveLineItem(this);
    }
    doClearFiscalDocument();
  }

  /**
   *
   * @param fiscalDocument FiscalDocument
   */
  public void doSetFiscalDocument(FiscalDocument fiscalDocument) {
    this.fiscalDocument = fiscalDocument;
  }

  /**
   *
   */
  public void doClearFiscalDocument() {
    this.fiscalDocument = null;
  }

  /**
   *
   * @return FiscalDocument
   */
  public FiscalDocument getFiscalDocument() {
    return this.fiscalDocument;
  }

  /**
   *
   * @return boolean
   */
  public boolean hasFiscalDocument() {
    return fiscalDocument != null;
  }

  /**
   * This method is used to get the no sale line item selling price
   * @return Currency
   */
//  public ArmCurrency getItemSellingPrice() {
//    return new ArmCurrency(0.0d);
//  }

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
        ((ArmLineDiscount)aDiscount).addDiscountNetAmount(this.getExtendedRetailAmount());
      } catch (CurrencyException ce) {}
      ((ArmLineDiscount)aDiscount).doAddLineItem(this);
    }
    doAddDiscount(aDiscount);
    //        broadcastUpdate();
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
  /**
   *
   * @param aLineItem POSLineItem
   */
//  public void transferInformationTo(POSLineItem aLineItem) {
//    aLineItem.doSetManualMarkdownAmount(getManualMarkdownAmount());
//    aLineItem.doSetManualMarkdownReason(getManualMarkdownReason());
//    aLineItem.doSetManualUnitPrice(getManualUnitPrice());
//    aLineItem.doSetItemRetailPrice(getItemRetailPrice());
//    aLineItem.doSetItemSellingPrice(getItemSellingPrice());
//    aLineItem.doSetAdditionalConsultant(getAdditionalConsultant());
//    aLineItem.doSetTaxExemptId(getTaxExemptId());
//    aLineItem.doSetRegionalTaxExemptId(getRegionalTaxExemptId());
//    aLineItem.doSetMiscItemId(getMiscItemId());
//    aLineItem.doSetMiscItemDescription(doGetMiscItemDescription());
//    aLineItem.doSetMiscItemTaxable(doGetMiscItemTaxable());
//    aLineItem.doSetMiscItemRegionalTaxable(doGetMiscItemRegionalTaxable());
//    aLineItem.doSetMiscItemGLAccount(getMiscItemGLAccount());
//    aLineItem.doSetMiscItemComment(getMiscItemComment());
//    aLineItem.getLineItemGrouping().doSetItemPriceOverrideWithoutReset(getLineItemGrouping().
//        getItemPriceOverride());
//  }

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

