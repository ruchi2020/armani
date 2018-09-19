/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax.flatrate;

import java.util.*;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.tax.*;
import com.chelseasystems.cs.tax.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.dlg.TaxExemptDlg;


/**
 *
 * <p>Title: CMSTaxStoreRateServices</p>
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
public class CMSTaxStoreRateServices extends CMSTaxServices {
  public static final String TAX_ENGINE_NAME = TaxTypeKey.TAX_ENGINE_NAME_STORE_RATE;
  public Hashtable taxExceptionRules;
  public Hashtable OffSendSaleExctaxRules;
 static ConfigMgr config =null;
 ArmCurrency pstTax = new ArmCurrency(0.0);
  /**
   * Default constructor
   */
  public CMSTaxStoreRateServices() {
    taxExceptionRules = TaxUtilities.getTaxExceptionRules();
  }

  /**
   * put your documentation comment here
   * @param zipcode
   * @return
   */
  public ArmTaxRate[] findByZipCode(String zipcode) {
    return null;
  }

  // Javadoc already defined in ancestor.
  public SaleTax getTaxAmounts(CompositePOSTransaction aCompositeTransaction, Store fromStore
      , Store toStore, Date aProcessDate)
      throws Exception {
	   ConfigMgr sapConfig = new ConfigMgr("item.cfg");
	  
	  String sapLookup= sapConfig.getString("SAP_LOOKUP");  
	  if(sapLookup!=null && sapLookup.equalsIgnoreCase("Y")){
		  config = new ConfigMgr("taxExceptionRate.cfg");
		  }else{
			  config = new ConfigMgr("tax.cfg");
			
		  }
    String strState = null;
    CMSSaleLineItem saleLineItem = null;
    ShippingRequest shippingRequest = null;
    
    //vishal yevale 29 nov 2016
    String itemProduct=null;
    String itemCategory=null;
    //end vishal
    double exceptionTaxRate = 0.0d;
    POSLineItemDetail[] taxableLineItemdetails = aCompositeTransaction.
        getTaxableLineItemDetailsArray();
    Double gstTax = 0.0;
    Double pstTax = 0.0;
    Double qstTax = 0.0;
    Double gst_hstTax = 0.0;
    CMSStore  cmsStore =  ((CMSStore)fromStore);
    CMSSaleTaxDetail[] taxDetails = new CMSSaleTaxDetail[taxableLineItemdetails.length];
    double rate = fromStore.getDefaultTaxRate().doubleValue();
    String taxJurisdiction = ((CMSStore)fromStore).getTaxJurisdiction();
    for (int i = 0; i < taxableLineItemdetails.length; i++) {
    	//this is the default tax rate add condition here to add default city for PR
      rate = fromStore.getDefaultTaxRate().doubleValue();
      //Anjana : Added For OFFline Send Sale
      if(taxableLineItemdetails[i].getLineItem() instanceof SaleLineItem){
      saleLineItem = (CMSSaleLineItem)taxableLineItemdetails[i].getLineItem();
      shippingRequest = saleLineItem.getShippingRequest();
      }
      //vishal yevale 29 Nov 2016
      POSLineItem lineItem = taxableLineItemdetails[i].getLineItem();
      CMSItem currentItem = (CMSItem)lineItem.getItem();  
      itemProduct=currentItem.getProductNum();
      itemCategory=currentItem.getCategory();
      //end vishal yevale
      if(fromStore.getState().equalsIgnoreCase("PR")){
    	  //Anjana: adding state tax for PR 
    	  if(((CMSStore)fromStore).getStateTax()!=null)
    	  rate = rate +  ((CMSStore)fromStore).getStateTax().doubleValue();
      }
      
      
      //For canada adding the tax rate from Store object (GST,PSt,hST,gst/hst)
      if(fromStore.getCountry().equalsIgnoreCase("CAN")){
    	  if(cmsStore.getGstTAX()!=null)
    	  gstTax = cmsStore.getGstTAX().doubleValue();
    	  if(cmsStore.getPstTAX()!=null)
    	  pstTax = cmsStore.getPstTAX().doubleValue();
    	  if(cmsStore.getQstTAX()!=null)
    	  qstTax = cmsStore.getQstTAX().doubleValue();
    	  if(cmsStore.getGst_hstTAX()!=null)
    	  gst_hstTax = cmsStore.getGst_hstTAX().doubleValue();
    	  
    	  rate = gstTax + pstTax + qstTax + gst_hstTax;
    	  
      }
      exceptionTaxRate = 0.0d;
      strState = null;
      ArmCurrency belowThresholdTaxAmount = new ArmCurrency(0.0d);
      ArmCurrency taxAmount = new ArmCurrency(0.0d);
      ArmCurrency netAmount = taxableLineItemdetails[i].getNetAmount();
      TaxExceptionRule taxExceptionRule=null;
      // vishal yevale 29 Nov 2016 tax CR from Jason
      if(fromStore.getCountry().equalsIgnoreCase("USA") || fromStore.getCountry().equalsIgnoreCase("CAN")){
       taxExceptionRule = TaxUtilities.getRule(taxExceptionRules , itemProduct , itemCategory, fromStore);
      }else{   
    	   taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(fromStore.
          getState().toUpperCase());
      }
      //end vishal yevale 29 nov 2016
      //Anjana: offline send sale 
      if(shippingRequest!=null){
          if(((CMSShippingRequest)shippingRequest).getOfflineShipTax()!=null){
        	  if(shippingRequest.getState().equals("PR"))
        	  {
        		  rate = ((CMSShippingRequest)shippingRequest).getOfflineCityTax().doubleValue() + ((CMSShippingRequest)shippingRequest).getOfflineStateTax().doubleValue();
        	  }
        	  
        	  else{
        	  rate = ((CMSShippingRequest)shippingRequest).getOfflineShipTax().doubleValue();
        	  }
          }
          else if(((CMSShippingRequest)shippingRequest).getOfflineGstTax()!=null || ((CMSShippingRequest)shippingRequest).getOfflineHstTax()!=null  ||
          ((CMSShippingRequest)shippingRequest).getOfflinePstTax()!=null|| ((CMSShippingRequest)shippingRequest).getOfflineQstTax()!=null) {
        	  
        	  rate = ((CMSShippingRequest)shippingRequest).getOfflineGstTax() + ((CMSShippingRequest)shippingRequest).getOfflineHstTax() + 
        	  ((CMSShippingRequest)shippingRequest).getOfflinePstTax()  + ((CMSShippingRequest)shippingRequest).getOfflineQstTax() ;
          }
          
      }
      
      if(shippingRequest!=null && ((CMSShippingRequest)shippingRequest).getOfflineShipTax()!=null){
    	  //exception rule tax calculation goes here
    	  OffSendSaleExctaxRules = TaxUtilities.getSendSaleOfflineTaxExceptionRules();
    	  TaxExceptionRule taxExceptionRuleOffSendSale = (TaxExceptionRule)OffSendSaleExctaxRules.get(shippingRequest.
                  getState().toUpperCase());
          if(taxExceptionRuleOffSendSale!=null){
    	     exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemdetails[i], fromStore
    	             , taxExceptionRuleOffSendSale, fromStore.getDefaultTaxRate().doubleValue(), taxJurisdiction);
    	         if (taxableLineItemdetails[i].getNetAmount().greaterThan(taxExceptionRuleOffSendSale.
    	             getThresholdAmount())) {
    	           belowThresholdTaxAmount = taxExceptionRuleOffSendSale.getThresholdAmount().multiply(exceptionTaxRate).
    	               round();
    	           netAmount = taxableLineItemdetails[i].getNetAmount().subtract(taxExceptionRuleOffSendSale.
    	               getThresholdAmount());
    	         } else {
    	          rate = exceptionTaxRate;
    	         }
    	         // Offline SendSale:- this is when exception state is there in tax.cfg but no matching tax rate then from store tax is applied so we need to set the to store details
    	         if(((CMSStore)fromStore).getStateTax()!=null){
    	         if(rate == (fromStore.getDefaultTaxRate() + ((CMSStore)fromStore).getStateTax())){
    	        	  if(shippingRequest.getState().equals("PR"))
    	        	  {
    	        		  rate = ((CMSShippingRequest)shippingRequest).getOfflineCityTax().doubleValue() + ((CMSShippingRequest)shippingRequest).getOfflineStateTax().doubleValue();
    	        	  }
    	        	  else{
    	        	  rate = ((CMSShippingRequest)shippingRequest).getOfflineShipTax().doubleValue();
    	        	  } }
    	         }
      }}
      else
      {
    	  if (taxExceptionRule != null) {
     exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemdetails[i], fromStore
            , taxExceptionRule, fromStore.getDefaultTaxRate().doubleValue(), taxJurisdiction);
        
        if(fromStore.getCountry().equalsIgnoreCase("CAN")){
        	 exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemdetails[i], fromStore
        	            , taxExceptionRule,rate, taxJurisdiction);
        }
        
        if (taxableLineItemdetails[i].getNetAmount().greaterThan(taxExceptionRule.
            getThresholdAmount())) {
          belowThresholdTaxAmount = taxExceptionRule.getThresholdAmount().multiply(exceptionTaxRate).
              round();
          netAmount = taxableLineItemdetails[i].getNetAmount().subtract(taxExceptionRule.
              getThresholdAmount());
          if(exceptionTaxRate!=rate){
          setExcTaxBreakdown(taxableLineItemdetails[i], taxExceptionRule.getThresholdAmount(),fromStore,netAmount);
          }
        } else {
          rate = exceptionTaxRate;
        }
      }
    }
      
      
      taxAmount = netAmount.multiply(rate).round();
      strState = fromStore.getState();
      taxDetails[i] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_STORE_RATE
          , taxAmount.add(belowThresholdTaxAmount), rate, fromStore.getState(), taxJurisdiction);
    }
    POSLineItemDetail[] regionalTaxableLineItemdetails = aCompositeTransaction.
        getRegionalTaxableLineItemDetailsArray();
    CMSSaleTaxDetail[] regionalTaxDetails = new CMSSaleTaxDetail[regionalTaxableLineItemdetails.
        length];
    double regionalRate = fromStore.getDefaultRegionalTaxRate().doubleValue();
    for (int i = 0; i < regionalTaxDetails.length; i++) {
      ArmCurrency amount = regionalTaxableLineItemdetails[i].getNetAmount().multiply(regionalRate).
          round();
      regionalTaxDetails[i] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_STORE_RATE_REGINOAL, amount
          , regionalRate, fromStore.getState(), taxJurisdiction);
    }
    return new SaleTax(TAX_ENGINE_NAME, taxDetails, regionalTaxDetails);
  }
  
  // Javadoc already defined in ancestor.
  public SaleTax getTaxAmounts(CompositePOSTransaction aCompositeTransaction,
      Store fromStore, Store toStore, Date aProcessDate, HashMap<String, Object[]> taxDetailMap)
      throws Exception {
	  return getTaxAmounts(aCompositeTransaction,fromStore,toStore,aProcessDate);
  }
  public static void setExcTaxBreakdown(POSLineItemDetail posLineItemDetail, ArmCurrency threshAmt, Store fromStore, ArmCurrency netAmount)
  {
	  Double gstTax = 0.0;
	  Double pstTax = 0.0;
	  Double qstTax = 0.0;
	  Double hstTax = 0.0;
	  Double rate = 0.0;
	  CMSStore  cmsStore =  ((CMSStore)fromStore);
	  if(fromStore.getCountry().equalsIgnoreCase("CAN")){
    	  if(cmsStore.getGstTAX()!=null)
    	  gstTax = cmsStore.getGstTAX().doubleValue();
    	  if(cmsStore.getPstTAX()!=null)
    	  pstTax = cmsStore.getPstTAX().doubleValue();
    	  if(cmsStore.getQstTAX()!=null)
    	  qstTax = cmsStore.getQstTAX().doubleValue();
    	  if(cmsStore.getGst_hstTAX()!=null)
    		  hstTax = cmsStore.getGst_hstTAX().doubleValue();
    	  
    	  rate = gstTax + pstTax + qstTax + hstTax;
    	  
      }
	  POSLineItem lineItem = posLineItemDetail.getLineItem();
	  // Vishal Yevale : 12 jan 2017 :added code to get exact state String to apply exact tax : like QC or QC1 or QC2
	  CMSItem currentItem = (CMSItem) lineItem.getItem();
	  String itemProduct=currentItem.getProductNum();
      String itemCategory=currentItem.getCategory();
 String strExceptionState = config.getString("EXCEPTION_STATE");
       strExceptionState = TaxUtilities.getStateString(itemProduct, itemCategory, strExceptionState);
      String strPstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_PST");
      String strQstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_QST");
      String strGstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_GST");
      String strHstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_HST");     
     try{ 
      if(strPstTaxRate!=null)
      {
    	
			lineItem.setPstTaxAmt(threshAmt.multiply(new Double(strPstTaxRate)).add(netAmount.multiply(pstTax)));
		
      }
      if(strGstTaxRate!=null)
      {
    	  lineItem.setGstTaxAmt(threshAmt.multiply(new Double(strGstTaxRate)).add(netAmount.multiply(gstTax))) ; 
      }
      if(strQstTaxRate!=null)
      {
    	  lineItem.setQstTaxAmt(threshAmt.multiply(new Double(strQstTaxRate)).add(netAmount.multiply(qstTax))) ; 
      }
      if(strHstTaxRate!=null)
      {
    	  lineItem.setGsthstTaxAmt(threshAmt.multiply(new Double(strHstTaxRate)).add(netAmount.multiply(hstTax))) ; 
      }

     } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CurrencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  }
}

