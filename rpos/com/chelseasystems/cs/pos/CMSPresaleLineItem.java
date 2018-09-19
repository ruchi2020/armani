/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-14-2005 | Pankaja   | N/A       | New method getPresaleLineItemDetailForReturn/Sale  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-13-2005 | Rajesh    | N/A       | Override required method                           |
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
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cr.currency.CurrencyException;
/**
 *
 * <p>Title: CMSPresaleLineItem</p>
 *
 * <p>Description: This class store the details of pre sale line item</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh
 * @version 1.0
 */
public class CMSPresaleLineItem extends POSLineItem implements Serializable, IRuleEngine {
  private Hashtable hashLineDiscount = new Hashtable(60);
  public boolean isPriceDiscountAdded = false;
  private Discount priceDiscount = null;
  private CMSSaleLineItem saleLnItm;
  private CMSReturnLineItem rtnLnItm;
  private ShippingRequest shippingRequest = null;
  private FiscalDocument fiscalDocument;
  private String sVATComment;
  private Vector vecFiscalDocuments;
  private CMSMiscItem taxableMiscLineItem = null;
  private CMSPresaleLineItem alterationLineItem = null;
  private String taxJusrisdiction;
  private Hashtable hFiscalDocComments = null;


  /**
   * Constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   * @param sequenceNumber int
   */
  public CMSPresaleLineItem(POSTransaction aTransaction, Item anItem, int sequenceNumber) {
    super(aTransaction, anItem, sequenceNumber);
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * constructor
   * @param aTransaction POSTransaction
   * @param anItem Item
   */
  public CMSPresaleLineItem(POSTransaction aTransaction, Item anItem) {
    this(aTransaction, anItem, aTransaction.getCompositeTransaction().getNextSequenceNumber());
    vecFiscalDocuments = new Vector();
    hFiscalDocComments = new Hashtable();
  }

  /**
   * This method is used to create pre sale line item detail
   * @return POSLineItemDetail
   */
  public POSLineItemDetail createLineItemDetail() {
    return new CMSPresaleLineItemDetail(this);
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
      if (((CMSPresaleLineItemDetail)aLineItemDetailList.nextElement()).getProcessed())
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
   * This method is used to get pre sale return line item detail
   * @return CMSPresaleLineItemDetail
   */
  public CMSPresaleLineItemDetail getPresaleLineItemDetailForReturn() {
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      CMSPresaleLineItemDetail apreSaleLineItemDetail = (CMSPresaleLineItemDetail)
          aLineItemDetailList.nextElement();
      if (!apreSaleLineItemDetail.getProcessed())
        return apreSaleLineItemDetail;
    }
    return null;
  }

  /**
   * This method is used to get pre sale line item detail for sale
   * @return CMSPresaleLineItemDetail
   */
  public CMSPresaleLineItemDetail getPresaleLineItemDetailForSale() {
    for (Enumeration aLineItemDetailList = getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      CMSPresaleLineItemDetail apreSaleLineItemDetail = (CMSPresaleLineItemDetail)
          aLineItemDetailList.nextElement();
      if (!apreSaleLineItemDetail.getProcessed())
        return apreSaleLineItemDetail;
    }
    return null;
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
      ((CMSShippingRequest)shippingRequest).doRemovePresaleLineItem(this);
    doSetShippingRequest(aShippingRequest);
    ((CMSShippingRequest)shippingRequest).doAddPresaleLineItem(this);
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

  public void testIsReturnable() throws BusinessRuleException {
    executeRule("testIsReturnable");
  }

  public boolean isDocumentPrintedForDocType(String docType) {
    FiscalDocument[] documents = this.getFiscalDocumentArray();
    for (int index = 0; index < documents.length; index++) {
      if (documents[index].getDocumentType().equals(docType))
        return true;
    }
    return false;
  }
  public ArmCurrency getExtendedReductionAmount()
  {
      try
      {
          ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
          for(Enumeration aLineItemDetailList = getLineItemDetails(); aLineItemDetailList.hasMoreElements();)
          {
            POSLineItemDetail posline = (POSLineItemDetail)aLineItemDetailList.nextElement();
            ArmCurrency aReductionAmount = posline.getReductionAmount();
//              System.out.println("posline " + posline.getReductionsArray().length);
//              for (Enumeration enm = posline.getReductions(); enm.hasMoreElements();){
//                Reduction redu = (Reduction)enm.nextElement();
//                System.out.println("redu " + redu.getAmount() +" " + redu.getReason());
//              }
              total = total.add(aReductionAmount);
          }

          return total;
      }
      catch(CurrencyException anException)
      {
          logCurrencyException("getExtendedReductionAmount", anException);
      }
      return null;
  }

  public ArmCurrency getExtendedDealMarkdownAmount()
  {
    try {
      POSLineItemDetail[] lnItmDtlArr = this.getLineItemDetailsArray();
	  //Anjana: 06-09-2017 , added this line for Canada store currency conversion to pick the store currecy type
	//instead of the value from base_currency_type from currency.cfg
      ArmCurrency currTotalDealMarkdownAmt = new ArmCurrency(getBaseCurrencyType(), 0.0D);
      if (lnItmDtlArr != null && lnItmDtlArr.length > 0) {
        for (int i = 0; i < lnItmDtlArr.length; i++) {
        	//Vivek Mishra : Changed to show correct subtotal in case of presale open.
        	/*currTotalDealMarkdownAmt = currTotalDealMarkdownAmt.add(lnItmDtlArr[i].
              getDealMarkdownAmount());*/
        	
        	if(lnItmDtlArr[i].getNetAmount().doubleValue() == 0.0d)
        	{
        		
          currTotalDealMarkdownAmt = currTotalDealMarkdownAmt.add(lnItmDtlArr[i].
              getDealMarkdownAmount());
        	}
        	else
        	currTotalDealMarkdownAmt = currTotalDealMarkdownAmt.add(lnItmDtlArr[i].getLineItem().getItemRetailPrice().subtract(lnItmDtlArr[i].getNetAmount()));
        	//Ends
        }
      }
      return currTotalDealMarkdownAmt;
    }catch (CurrencyException ex){

    }
	//Anjana: 06-09-2017 , added theis line for Canada store currecy conversion to pick the store currecy type
	//instead of the vakue from base_currency_type from cuurrency.cfg
    return new ArmCurrency(getBaseCurrencyType(), 0.0D);
  }

//# 813
  public CMSMiscItem getTaxableMiscLineItem(){
    return this.taxableMiscLineItem;
  }

  public void setTaxableMiscLineItem(CMSMiscItem taxableMiscLineItem){
    this.taxableMiscLineItem = taxableMiscLineItem;
  }

  public void setAlterationLineItem(CMSPresaleLineItem presaleLineItem){
    this.alterationLineItem = presaleLineItem;
  }

  public CMSPresaleLineItem getAlterationLineItem(){
    return this.alterationLineItem;
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

