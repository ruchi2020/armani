/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax;

import java.util.*;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.tax.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.util.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.tax.flatrate.CMSTaxStoreRateServices;
import com.chelseasystems.cs.config.ArmTaxRateConfig;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.tax.flatrate.CMSTaxFixedRateServices;
import com.chelseasystems.cs.util.Version;


/*
 History:
 Ver#   Date        By         Defect #   Description
 1     11-30-2004  Pankaja    N/A        Resolved the problem with the tax not calculating
 correctly when delegated to local implementation.
 */
/**
 * <p>Title: TaxUtilities</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class TaxUtilities {

  public TaxUtilities() {
  }

  public static final String THRESHOLD_APPLICABLE_LINEITEM = "L";
  public static final String THRESHOLD_APPLICABLE_TRANSACTION = "T";
  public static final String ABOVE_THRESHOLD_RULE_FULL = "F";
  public static final String ABOVE_THRESHOLD_RULE_PARTIAL = "P";
  static ConfigMgr config =null;
  static boolean offlineconfig = false;
  String sapLookup=null;
  static ConfigMgr sapConfig = null;
  public static HashMap<String, Object[]> taxDetailMap = new HashMap<String,Object[]>();
  
  

  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
   
  static{
	   sapConfig = new ConfigMgr("item.cfg");
	  String sapLookup= sapConfig.getString("SAP_LOOKUP");  
	  if(sapLookup!=null && sapLookup.equalsIgnoreCase("Y")){
	  config = new ConfigMgr("taxExceptionRate.cfg");
	  }else{
		  config = new ConfigMgr("tax.cfg");
	  }
  }
  
  //end

  /**
   * @param theAppMgr
   * @param aCompositeTransaction
   * @param fromStore
   * @param toStore
   * @param aProcessDate
   * @return A SaleTax object. This corresponds the sale tax of the taxable line
   * item details in the composite transaction.
   * @exception Exception
   */
  public static void applyTax (IRepositoryManager theAppMgr, CMSCompositePOSTransaction aCompositeTransaction, CMSStore fromStore, CMSStore toStore, Date aProcessDate) throws Exception {
    try {
      String taxType = config.getString("TAX_TYPE");
      SaleTaxDetail[] taxDetails = null;
      POSLineItemDetail[] taxableLineItemDetails = null;
      ArrayList aTaxableLineItemDetails = new ArrayList();
      SaleTax saleTax = null;
      Hashtable hTaxExceptionRules = new Hashtable();
      CMSTaxServices localServices = null;
      if (aCompositeTransaction.isTaxExempt()) {}
      else {
        boolean calculateTaxLocally = false;
        //taxtype is  not presennt in cfg file so mostly this loop is executed
        //CMSTaxStoreRateServices is the localServices
        if (taxType == null) {
          localServices = new CMSTaxStoreRateServices();
          hTaxExceptionRules = getTaxExceptionRules();
          ShippingRequest aShippingRequest[] = aCompositeTransaction.getShippingRequestsArray();
          if ((aShippingRequest == null || aShippingRequest.length == 0)) {
            calculateTaxLocally = true;
          } else if (aShippingRequest != null && aShippingRequest.length > 0) {
            for (int i = 0; i < aShippingRequest.length; i++) {
              CMSShippingRequest cmsShippingRequest = (CMSShippingRequest)aShippingRequest[i];
              if (!cmsShippingRequest.getZipCode().equals(fromStore.getZipCode())) {
                calculateTaxLocally = false;
                break;
              }
            }
          }
        }
        else {
          calculateTaxLocally = true;
          localServices = new CMSTaxFixedRateServices();
        }
        //this loop is to calculate tax on fixed rate when calculateTaxLocally is true
        if (calculateTaxLocally) {
        	//localServices is CMSTaxFixedRateServices if tax_type is null
        	//localServices is CMSTaxStoreRateServices is shipping is null
        	//added state tax below
          saleTax = localServices.getTaxAmounts(aCompositeTransaction, fromStore, toStore, aProcessDate);
          taxDetails = saleTax.getSaleTaxDetailArray();
          taxableLineItemDetails = aCompositeTransaction.getTaxableLineItemDetailsArray();
          for (int i = 0; i < taxDetails.length; i++) {
            POSLineItem item = taxableLineItemDetails[i].getLineItem();
            taxableLineItemDetails[i].doSetTaxAmount(taxDetails[i].getAmount());
            if (item instanceof CMSSaleLineItem) {
//              if (!((CMSSaleLineItem)item).isMiscItem()) {
                ((CMSSaleLineItem)item).doSetTaxJurisdiction(((CMSSaleTaxDetail)taxDetails[i]).getTaxJurisdiction());
//              }
              ((CMSSaleLineItemDetail)taxableLineItemDetails[i]).doSetTaxRate(((CMSSaleTaxDetail)taxDetails[i]).getRate());
            }
            if (item instanceof CMSReturnLineItem) {
              if (!((CMSReturnLineItem)item).isMiscItem()) {
                ((CMSReturnLineItem)item).doSetTaxJurisdiction(((CMSSaleTaxDetail)taxDetails[i]).getTaxJurisdiction());
              }
              //((CMSReturnLineItemDetail)taxableLineItemDetails[i]).doSetTaxRate(((CMSSaleTaxDetail)
              //    taxDetails[i]).getRate());
            }
            if (item instanceof CMSPresaleLineItem) {
              if (!((CMSPresaleLineItem)item).isMiscItem()) {
                ((CMSPresaleLineItem)item).doSetTaxJurisdiction(((CMSSaleTaxDetail)taxDetails[i]).getTaxJurisdiction());
              }
              ((CMSPresaleLineItemDetail)taxableLineItemDetails[i]).doSetTaxRate(((CMSSaleTaxDetail)taxDetails[i]).getRate());
            }
          }
        } else {
        	//this is mostly for send sale as calculate tax is false
        	//this execute in server side
          taxableLineItemDetails = aCompositeTransaction.getTaxableLineItemDetailsArray();

          saleTax = CMSTaxHelper.getTaxAmounts(theAppMgr, aCompositeTransaction, fromStore, toStore, aProcessDate, taxDetailMap);
          taxDetails = saleTax.getSaleTaxDetailArray();
          Double threhAmt = 0.0d;
          String threhRule = "";
          //Anjana added the below to show split taxes for send sale receipts
          
          taxDetailMap = ((CMSSaleTaxDetail)taxDetails[0]).getTaxDetailMap();
      
          for (int i = 0; i < taxDetails.length; i++) {
            POSLineItem item = taxableLineItemDetails[i].getLineItem();
            taxableLineItemDetails[i].doSetTaxAmount(taxDetails[i].getAmount());

            if (taxDetails[i] instanceof CMSSaleTaxDetail) {
              if (item instanceof CMSSaleLineItem) {
//                if (!((CMSSaleLineItem)item).isMiscItem()) {
                  ((CMSSaleLineItem)item).doSetTaxJurisdiction(((CMSSaleTaxDetail)taxDetails[i]).getTaxJurisdiction());
//                }
                ((CMSSaleLineItemDetail)taxableLineItemDetails[i]).doSetTaxRate(((CMSSaleTaxDetail)taxDetails[i]).getRate());
                if(((CMSSaleTaxDetail)taxDetails[i]).getStateTax()!=null)
                	aCompositeTransaction.setStateTax(((CMSSaleTaxDetail)taxDetails[i]).getStateTax());
              
                
                
               	if(((CMSSaleTaxDetail)taxDetails[i]).getTaxMap()!=null){
               		aCompositeTransaction.setTaxMap(((CMSSaleTaxDetail)taxDetails[i]).getTaxMap());
               		
              	
                }
              
              if(((CMSSaleTaxDetail)taxDetails[i]).getTaxExcMap()!=null){
            		aCompositeTransaction.setTaxExcMap(((CMSSaleTaxDetail)taxDetails[i]).getTaxExcMap());
              }
              
              if(((CMSSaleTaxDetail)taxDetails[i]).getArmCATaxConfig()!=null){
          		ArmTaxRateConfig[] armTaxRateConfig = ((CMSSaleTaxDetail)taxDetails[i]).getArmCATaxConfig();
          		for(int j = 0; j<armTaxRateConfig.length ;j++){
          			 threhAmt = armTaxRateConfig[0].getAmountThr();
          			 threhRule = armTaxRateConfig[0].getThrRule();
          		}
          		aCompositeTransaction.setThreshAmt(new ArmCurrency(threhAmt));
          		aCompositeTransaction.setThrehRule(threhRule);
            }
           
              } 	  
            	  
           
            	  
              if (item instanceof CMSPresaleLineItem) {
                ((CMSPresaleLineItem)item).doSetTaxJurisdiction(((CMSSaleTaxDetail)taxDetails[i]).getTaxJurisdiction());
                ((CMSPresaleLineItemDetail)taxableLineItemDetails[i]).doSetTaxRate(((CMSSaleTaxDetail)taxDetails[i]).getRate());
              }
              if (item instanceof CMSReturnLineItem) {
                if (!((CMSReturnLineItem)item).isMiscItem()) {
                  ((CMSReturnLineItem)item).doSetTaxJurisdiction(((CMSSaleTaxDetail)taxDetails[i]).getTaxJurisdiction());
                }
              }
            }
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Returns Tax exception rule
   * @return Hashtable
   */
  
  public static Hashtable getTaxExceptionRules() {
	    Hashtable hTaxExceptionRules = new Hashtable();
	    String strExceptionState = null;   
	     strExceptionState = config.getString("EXCEPTION_STATE");
	   
	     String totalCount=null;
	     totalCount=config.getString("TOTAL_RULE");
	     //Vishal Yevale : 28 Nov 2016  added this code for TAX exception CR from Jason
	     if(totalCount!=null && strExceptionState!=null){
	    	 int size=Integer.valueOf(totalCount);
   		  String exceptionState=strExceptionState;
	    	  for(int i=1;i<=size;i++){
	    		  StringTokenizer tokens = null;
	    	      TaxExceptionRule exceptionRule = new TaxExceptionRule();
	    	      exceptionRule.setExceptionState(strExceptionState);
	    	      exceptionRule.setExceptionTaxRate(getExceptionTaxRate(exceptionState).doubleValue());
	    	      exceptionRule.setThresholdAmount(getThresholdAmount(exceptionState));
	    	      exceptionRule.setAboveThresholdRule(getAboveThresholdRule(exceptionState));
	    	      if(!exceptionState.equals(null)){
	    	      tokens = new StringTokenizer(getApplicableItemClass(exceptionState), ",");
	    	      }
	    	      while (tokens.hasMoreTokens()) {
	    	        String itemClass = tokens.nextToken();
	    	        exceptionRule.addItemClass(itemClass);
	    	      }
	    	      tokens = null;
	    	      tokens = new StringTokenizer(getExceptionZipCodes(exceptionState), ",");
	    	      while (tokens.hasMoreTokens()) {
	    	        String strZipCode = tokens.nextToken();
	    	        exceptionRule.addExceptionZipCode(strZipCode);
	    	      }
	    	      tokens = null;
	    	      tokens = new StringTokenizer(getExceptionTaxJur(exceptionState), ",");
	    	      while (tokens.hasMoreTokens()) {
	    	        String strTaxJur = tokens.nextToken();
	    	        exceptionRule.addExceptionTaxJur(strTaxJur);
	    	      }
	    	      tokens = null;
	    	      if(getApplicableItemBrand(exceptionState)!=null){
	    	      tokens = new StringTokenizer(getApplicableItemBrand(exceptionState), ",");
	    	      while (tokens.hasMoreTokens()) {
	    	        String itemBrand = tokens.nextToken();
	    	        exceptionRule.addItemBrand(itemBrand);
	    	      }
	    	      }
	    	      tokens = null;
	    	      if(getApplicableItemProduct(exceptionState)!=null){
	    	      tokens = new StringTokenizer(getApplicableItemProduct(exceptionState), ",");
	    	      while (tokens.hasMoreTokens()) {
	    	        String itemProduct = tokens.nextToken();
	    	        exceptionRule.addItemProduct(itemProduct);
	    	      }
	    	      }
	    	      hTaxExceptionRules.put(exceptionState.toUpperCase(), exceptionRule);
	    	      exceptionState=String.valueOf(i)+strExceptionState;
	  	    }
	     }else{ // end vishal Yevale 28 Nov 2016
		     //Added null check by Anjana to calculate tax properly when there are no exceptions
	     if(strExceptionState!=null){
	    StringTokenizer statetokens = new StringTokenizer(strExceptionState, ",");
	    StringTokenizer tokens = null;
	  
	    while (statetokens.hasMoreTokens()) {
	      TaxExceptionRule exceptionRule = new TaxExceptionRule();
	      String exceptionState = statetokens.nextToken();
	      exceptionRule.setExceptionState(exceptionState);
	      exceptionRule.setExceptionTaxRate(getExceptionTaxRate(exceptionState).doubleValue());
	      exceptionRule.setThresholdAmount(getThresholdAmount(exceptionState));
	      exceptionRule.setAboveThresholdRule(getAboveThresholdRule(exceptionState));
	      if(!exceptionState.equals(null)){
	      tokens = new StringTokenizer(getApplicableItemClass(exceptionState), ",");
	      }
	      while (tokens.hasMoreTokens()) {
	        String itemClass = tokens.nextToken();
	        exceptionRule.addItemClass(itemClass);
	      }
	      tokens = null;
	      tokens = new StringTokenizer(getExceptionZipCodes(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String strZipCode = tokens.nextToken();
	        exceptionRule.addExceptionZipCode(strZipCode);
	      }
	      tokens = null;
	      tokens = new StringTokenizer(getExceptionTaxJur(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String strTaxJur = tokens.nextToken();
	        exceptionRule.addExceptionTaxJur(strTaxJur);
	      }
	      tokens = null;
	      if(getApplicableItemBrand(exceptionState)!=null){
	      tokens = new StringTokenizer(getApplicableItemBrand(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String itemBrand = tokens.nextToken();
	        exceptionRule.addItemBrand(itemBrand);
	      }
	      }
	      tokens = null;
	      if(getApplicableItemProduct(exceptionState)!=null){
	      tokens = new StringTokenizer(getApplicableItemProduct(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String itemProduct = tokens.nextToken();
	        exceptionRule.addItemProduct(itemProduct);
	      }
	      }
	      hTaxExceptionRules.put(exceptionState.toUpperCase(), exceptionRule);
	    }
	     }//ends
  }
	    return hTaxExceptionRules;
	  }
 
  
  public static Hashtable getSendSaleOfflineTaxExceptionRules() {
	    Hashtable hTaxExceptionRules = new Hashtable();
	    String strExceptionState = null;  
	    offlineconfig = true;
	    config = new ConfigMgr("tax.cfg");
	     strExceptionState = config.getString("EXCEPTION_STATE");
	    //Added null check by Anjana to calculate tax properly when there are no exceptions
	     if(strExceptionState!=null){
	    StringTokenizer statetokens = new StringTokenizer(strExceptionState, ",");
	    StringTokenizer tokens = null;
	    
	    while (statetokens.hasMoreTokens()) {
	      TaxExceptionRule exceptionRule = new TaxExceptionRule();
	      String exceptionState = statetokens.nextToken();
	      exceptionRule.setExceptionState(exceptionState);
	      exceptionRule.setExceptionTaxRate(getExceptionTaxRate(exceptionState).doubleValue());
	      exceptionRule.setThresholdAmount(getThresholdAmount(exceptionState));
	      exceptionRule.setAboveThresholdRule(getAboveThresholdRule(exceptionState));
	      if(!exceptionState.equals(null)){
	      tokens = new StringTokenizer(getApplicableItemClass(exceptionState), ",");
	      }
	      while (tokens.hasMoreTokens()) {
	        String itemClass = tokens.nextToken();
	        exceptionRule.addItemClass(itemClass);
	      }
	      tokens = null;
	      tokens = new StringTokenizer(getExceptionZipCodes(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String strZipCode = tokens.nextToken();
	        exceptionRule.addExceptionZipCode(strZipCode);
	      }
	      tokens = null;
	      tokens = new StringTokenizer(getExceptionTaxJur(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String strTaxJur = tokens.nextToken();
	        exceptionRule.addExceptionTaxJur(strTaxJur);
	      }
	      tokens = null;
	      if(getApplicableItemBrand(exceptionState)!=null){
	      tokens = new StringTokenizer(getApplicableItemBrand(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String itemBrand = tokens.nextToken();
	        exceptionRule.addItemBrand(itemBrand);
	      }
	      }
	      tokens = null;
	      if(getApplicableItemProduct(exceptionState)!=null){
	      tokens = new StringTokenizer(getApplicableItemProduct(exceptionState), ",");
	      while (tokens.hasMoreTokens()) {
	        String itemProduct = tokens.nextToken();
	        exceptionRule.addItemProduct(itemProduct);
	      }
	      }
	      hTaxExceptionRules.put(exceptionState.toUpperCase(), exceptionRule);
	    }
	     }//ends
	     
	     String sapLookup= sapConfig.getString("SAP_LOOKUP");  
	     if(sapLookup!=null && sapLookup.equalsIgnoreCase("Y")){
	   	  config = new ConfigMgr("taxExceptionRate.cfg");
	   	  }else{
	   		  config = new ConfigMgr("tax.cfg");
	   	
	   	  }
	     offlineconfig = false;
	    return hTaxExceptionRules;
	  }
  /**
   * Check the applicability of tax
   * @param strState String
   * @param posLineItemDetail POSLineItemDetail
   * @param shippingRequest ShippingRequest
   * @return boolean
   */
  public boolean isApplicableForTax(String strState, POSLineItemDetail posLineItemDetail, ShippingRequest shippingRequest) {
    if (isExceptionState(strState)) {
      String strThresholdApplicable = getThresholdApplicable(strState);
      if (strThresholdApplicable.equals(THRESHOLD_APPLICABLE_LINEITEM)) {}
    }
    return posLineItemDetail.getLineItem().isItemTaxable();
  }

  /**
   *
   * @param strState String
   * @return boolean
   */
  public boolean isExceptionState(String strState) {
    String strExceptionState = config.getString("EXCEPTION_STATE");
    StringTokenizer tokens = new StringTokenizer(strExceptionState, ",");
    while (tokens.hasMoreTokens()) {
      String exceptionState = tokens.nextToken();
      if (strState.equals(strExceptionState))
        return true;
    }
    return false;
  }

  /**
   *
   * @param strState String
   * @return String
   */
  public static String getThresholdApplicable(String strState) {
    String strThresholdApplicable = config.getString(strState + ".THRESHOLD_APPLICABLE");
    return strThresholdApplicable;
  }

  /**
   * Return Threshold amount
   * @param strState String
   * @return Currency
   */
  public static ArmCurrency getThresholdAmount(String strState) {
    String strThresholdAmt = config.getString(strState + ".THRESHOLD_AMT");
    return new ArmCurrency(strThresholdAmt);
  }

  /**
   * Return exception tax rate
   * @param strState String
   * @return Double
   */
  public static Double getExceptionTaxRate(String strState) {
    String strTaxRate = config.getString(strState + ".EXCEPTION_TAX_RATE");
    if (strTaxRate != null)
      return new Double(strTaxRate);
    else
      return new Double(0.0d);
  }

  /**
   * Return applicable item class
   * @param strState String
   * @return String
   */
  public static String getApplicableItemClass(String strState) {
	//Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
    //start
	String strApplicableItemClass="";
	if(offlineconfig){
		strApplicableItemClass = config.getString(strState + ".EXCEPTION_APPLIED_ITEM_CATEGORY");
		return (strApplicableItemClass!=null ? strApplicableItemClass : "");
	}
	String sapLookup= sapConfig.getString("SAP_LOOKUP");;
	
		if((sapLookup != null) && sapLookup.equalsIgnoreCase("Y"))
		{
			strApplicableItemClass = config.getString(strState + ".EXCEPTION_APPLIED_ITEM_CATEGORY");
		}
		//end
		else
		{
		strApplicableItemClass = config.getString(strState + ".EXCEPTION_APPLIED_ITEM_CLASS");
		}
		return strApplicableItemClass;
  }
  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
  /**
   * Return applicable item product
   * @param strState String
   * @return String
   */
  public static String getApplicableItemProduct(String strState) {
	  
		String strApplicableItemProduct = config.getString(strState + ".EXCEPTION_APPLIED_ITEM_PRODUCT");
	    return strApplicableItemProduct;
  }
  
  /**
   * Return applicable item brand
   * @param strState String
   * @return String
   */
  public static String getApplicableItemBrand(String strState) {
	  
		String strApplicableItemBrand = config.getString(strState + ".EXCEPTION_APPLIED_ITEM_BRAND");
	    return strApplicableItemBrand;
  }
  //end

  /**
   * Returns exception zip code
   * @param strState String
   * @return String
   */
  public static String getExceptionZipCodes(String strState) {
    String strExceptionZipcodes = config.getString(strState + ".EXCEPTION_ZIPCODES");
    return strExceptionZipcodes;
  }

  /**
   * Returns exception tax jurisdiction list
   * @param strState String
   * @return String
   */
  public static String getExceptionTaxJur(String strState) {
    String strExceptionTaxJur = config.getString(strState + ".EXCEPTION_TAX_JUR");
    return strExceptionTaxJur;
  }

  /**
   * put your documentation comment here
   * @param strState
   * @return
   */
  public static String getAboveThresholdRule(String strState) {
    String rule = config.getString(strState + ".ABOVE_THRESHOLD_RULE");
    return rule;
  }

  /**
   * Returns applicable tax rate
   * @param posLineItemDetail POSLineItemDetail
   * @param fromStore Store
   * @param taxExceptionRule TaxExceptionRule
   * @param defaultTaxRate double
   * @return double
   */
  public static double getApplicableTaxRate(POSLineItemDetail posLineItemDetail, Store fromStore, TaxExceptionRule taxExceptionRule, double defaultTaxRate, String taxJur) {
	    double rate = 0.0d;
	    ConfigMgr mgr = new ConfigMgr("item.cfg");
        String hasSapLookup = mgr.getString("SAP_LOOKUP");
        String countryId = "";
     //   System.out.println("hasSapLookup>>>>>>>>>>>>>>>>>>>>>>>."+hasSapLookup);
        if(hasSapLookup!=null && hasSapLookup.equalsIgnoreCase("Y")){
        	if((com.chelseasystems.cr.appmgr.AppManager.getInstance())!=null){
        countryId = (((CMSStore)com.chelseasystems.cr.appmgr.AppManager.getInstance().
		          getGlobalObject("STORE")).getCountry());
        	}
        }
       String strZipcode = fromStore.getZipCode();
	    if (taxExceptionRule != null) {
	      POSLineItem lineItem = posLineItemDetail.getLineItem();
	      CMSItem currentItem = (CMSItem)lineItem.getItem();
	      if (lineItem instanceof SaleLineItem && ((SaleLineItem)lineItem).hasShippingRequest())
	        strZipcode = ((SaleLineItem)lineItem).getShippingRequest().getZipCode();
	      if (lineItem instanceof CMSPresaleLineItem && ((CMSPresaleLineItem)lineItem).hasShippingRequest())
	        strZipcode = ((CMSPresaleLineItem)lineItem).getShippingRequest().getZipCode();
			if(hasSapLookup!=null && hasSapLookup.equalsIgnoreCase("Y")){
				if(!countryId.equals(null) && countryId.equals("CAN")){
				      if (taxExceptionRule.getItemBrandList().contains(currentItem.getBrand()) && ((((taxExceptionRule.getExceptionZipCodeList().contains(strZipcode) && taxExceptionRule.getExceptionZipCodeList().size() > 0)) || ((taxExceptionRule.getExceptionTaxJurList().contains(taxJur) && taxExceptionRule.getExceptionTaxJurList().size() > 0)))
					          || (taxExceptionRule.getExceptionTaxJurList().size() == 0 && taxExceptionRule.getExceptionZipCodeList().size() == 0))) {
					        try {
					           if ((posLineItemDetail instanceof SaleLineItemDetail && posLineItemDetail.getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) ||
					              (posLineItemDetail instanceof ReturnLineItemDetail && posLineItemDetail.getNetAmount().absoluteValue().greaterThan(taxExceptionRule.getThresholdAmount())) ||
					              (posLineItemDetail instanceof CMSPresaleLineItemDetail && (posLineItemDetail.getLineItem().getExtendedRetailAmount().subtract(posLineItemDetail.getLineItem().getExtendedReductionAmount())).greaterThan(taxExceptionRule.getThresholdAmount()))) {
					            if (taxExceptionRule.getAboveThresholdRule().equals(TaxUtilities.ABOVE_THRESHOLD_RULE_PARTIAL)) {
					              rate = taxExceptionRule.getExceptionTaxRate();
					              //Test
					              setExcTaxBreakdown(posLineItemDetail);
					              //Ends Test
					              return rate;
					            }
					          } else {
					            rate = taxExceptionRule.getExceptionTaxRate();
					            //Test
					              setExcTaxBreakdown(posLineItemDetail);
					            //Ends Test
					            return rate;
					          }
					          } catch (Exception ex) {
					          ex.printStackTrace();
					        }
					        
					      }
				}//changed size check from null to zero
				else if(taxExceptionRule.getItemProductList().size()==0){
					if(taxExceptionRule.getItemClassList().contains(currentItem.getCategory()) && ((((((taxExceptionRule.getExceptionZipCodeList().contains(strZipcode) && taxExceptionRule.getExceptionZipCodeList().size() > 0)) || ((taxExceptionRule.getExceptionTaxJurList().contains(taxJur) && taxExceptionRule.getExceptionTaxJurList().size() > 0)))
					          || (taxExceptionRule.getExceptionTaxJurList().size() == 0 && taxExceptionRule.getExceptionZipCodeList().size() == 0))))){

						
				        try {
				       
				          if ((posLineItemDetail instanceof SaleLineItemDetail && posLineItemDetail.getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) ||
				              (posLineItemDetail instanceof ReturnLineItemDetail && posLineItemDetail.getNetAmount().absoluteValue().greaterThan(taxExceptionRule.getThresholdAmount())) ||
				              (posLineItemDetail instanceof CMSPresaleLineItemDetail && (posLineItemDetail.getLineItem().getExtendedRetailAmount().subtract(posLineItemDetail.getLineItem().getExtendedReductionAmount())).greaterThan(taxExceptionRule.getThresholdAmount()))) {
				            if (taxExceptionRule.getAboveThresholdRule().equals(TaxUtilities.ABOVE_THRESHOLD_RULE_PARTIAL)) {
				              rate = taxExceptionRule.getExceptionTaxRate();
				              return rate;
				            }
				          } else {
				            rate = taxExceptionRule.getExceptionTaxRate();
				            return rate;
				          }
				      
				        } catch (Exception ex) {
				          ex.printStackTrace();
				        }
				        
				      
					}
				}
				else  if (taxExceptionRule.getItemClassList().contains(currentItem.getCategory()) && (taxExceptionRule.getItemProductList().contains(currentItem.getProductNum())) && ((((taxExceptionRule.getExceptionZipCodeList().contains(strZipcode) && taxExceptionRule.getExceptionZipCodeList().size() > 0)) || ((taxExceptionRule.getExceptionTaxJurList().contains(taxJur) && taxExceptionRule.getExceptionTaxJurList().size() > 0)))
				          || (taxExceptionRule.getExceptionTaxJurList().size() == 0 && taxExceptionRule.getExceptionZipCodeList().size() == 0))) {
				        try {
				       
				          if ((posLineItemDetail instanceof SaleLineItemDetail && posLineItemDetail.getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) ||
				              (posLineItemDetail instanceof ReturnLineItemDetail && posLineItemDetail.getNetAmount().absoluteValue().greaterThan(taxExceptionRule.getThresholdAmount())) ||
				              (posLineItemDetail instanceof CMSPresaleLineItemDetail && (posLineItemDetail.getLineItem().getExtendedRetailAmount().subtract(posLineItemDetail.getLineItem().getExtendedReductionAmount())).greaterThan(taxExceptionRule.getThresholdAmount()))) {
				            if (taxExceptionRule.getAboveThresholdRule().equals(TaxUtilities.ABOVE_THRESHOLD_RULE_PARTIAL)) {
				              rate = taxExceptionRule.getExceptionTaxRate();
				              return rate;
				            }
				          } else {
				            rate = taxExceptionRule.getExceptionTaxRate();
				            return rate;
				          }
				      
				        } catch (Exception ex) {
				          ex.printStackTrace();
				        }
				        
				      }
				
			}//old implementation : this code will execute when there is no entry for SAP_LOOKUP variable in item.cfg file
			else if (taxExceptionRule.getItemClassList().contains(currentItem.getClassId()) && ((((taxExceptionRule.getExceptionZipCodeList().contains(strZipcode) && taxExceptionRule.getExceptionZipCodeList().size() > 0)) || ((taxExceptionRule.getExceptionTaxJurList().contains(taxJur) && taxExceptionRule.getExceptionTaxJurList().size() > 0)))
				          || (taxExceptionRule.getExceptionTaxJurList().size() == 0 && taxExceptionRule.getExceptionZipCodeList().size() == 0))) {
	        try {
	          // rajesh
	          if ((posLineItemDetail instanceof SaleLineItemDetail && posLineItemDetail.getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) ||
	              (posLineItemDetail instanceof ReturnLineItemDetail && posLineItemDetail.getNetAmount().absoluteValue().greaterThan(taxExceptionRule.getThresholdAmount())) ||
	              (posLineItemDetail instanceof CMSPresaleLineItemDetail && (posLineItemDetail.getLineItem().getExtendedRetailAmount().subtract(posLineItemDetail.getLineItem().getExtendedReductionAmount())).greaterThan(taxExceptionRule.getThresholdAmount()))) {
	            if (taxExceptionRule.getAboveThresholdRule().equals(TaxUtilities.ABOVE_THRESHOLD_RULE_PARTIAL)) {
	              rate = taxExceptionRule.getExceptionTaxRate();
	              return rate;
	            }
	          } else {
	            rate = taxExceptionRule.getExceptionTaxRate();
	            return rate;
	          }
	          // end rajesh
	        } catch (Exception ex) {
	          ex.printStackTrace();
	        }
	      }
	    }
	    
	    
	    //FOR PR AND CAN add the state tas as well
	    if(fromStore.getState().equalsIgnoreCase("PR")){
	    	if(((CMSStore)fromStore).getStateTax()!=null)
	    	defaultTaxRate = defaultTaxRate + ((CMSStore)fromStore).getStateTax();
	    }
	    
	    if(fromStore.getCountry().equalsIgnoreCase("CAN")){
	    	if(((CMSStore)fromStore).getStateTax()!=null)
	    	defaultTaxRate = ((CMSStore)fromStore).getGstTAX() + ((CMSStore)fromStore).getGst_hstTAX() + ((CMSStore)fromStore).getPstTAX() + ((CMSStore)fromStore).getQstTAX();
	    }  
	    
	    return defaultTaxRate;
	  }
  
  
  /**
   * Returns applicable tax rate
   * @param posLineItemDetail POSLineItemDetail
   * @param fromStore Store
   * @param taxExceptionRule TaxExceptionRule
   * @param defaultTaxRate double
   * @return double
   */
  public static double getApplicableTaxRateforCategoryProductandBrand(POSLineItemDetail posLineItemDetail, Store fromStore, TaxExceptionRule taxExceptionRule, double defaultTaxRate, String taxJur) {
	    double rate = 0.0d;
	    ArmCurrency thresholdAmount = taxExceptionRule.getThresholdAmount();
	    String strZipcode = fromStore.getZipCode();
	    if (taxExceptionRule != null) {
	      POSLineItem lineItem = posLineItemDetail.getLineItem();
	      CMSItem currentItem = (CMSItem)lineItem.getItem();
	      if (lineItem instanceof SaleLineItem && ((SaleLineItem)lineItem).hasShippingRequest())
	        strZipcode = ((SaleLineItem)lineItem).getShippingRequest().getZipCode();
	      if (lineItem instanceof CMSPresaleLineItem && ((CMSPresaleLineItem)lineItem).hasShippingRequest())
	        strZipcode = ((CMSPresaleLineItem)lineItem).getShippingRequest().getZipCode();

	      if (taxExceptionRule.getItemClassList().contains(currentItem.getClassId()) && (taxExceptionRule.getItemProductList().contains(currentItem.getProductNum())) && ((((taxExceptionRule.getExceptionZipCodeList().contains(strZipcode) && taxExceptionRule.getExceptionZipCodeList().size() > 0)) || ((taxExceptionRule.getExceptionTaxJurList().contains(taxJur) && taxExceptionRule.getExceptionTaxJurList().size() > 0)))
	          || (taxExceptionRule.getExceptionTaxJurList().size() == 0 && taxExceptionRule.getExceptionZipCodeList().size() == 0))) {
	        try {
	          // rajesh
	          if ((posLineItemDetail instanceof SaleLineItemDetail && posLineItemDetail.getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) ||
	              (posLineItemDetail instanceof ReturnLineItemDetail && posLineItemDetail.getNetAmount().absoluteValue().greaterThan(taxExceptionRule.getThresholdAmount())) ||
	              (posLineItemDetail instanceof CMSPresaleLineItemDetail && (posLineItemDetail.getLineItem().getExtendedRetailAmount().subtract(posLineItemDetail.getLineItem().getExtendedReductionAmount())).greaterThan(taxExceptionRule.getThresholdAmount()))) {
	            if (taxExceptionRule.getAboveThresholdRule().equals(TaxUtilities.ABOVE_THRESHOLD_RULE_PARTIAL)) {
	              rate = taxExceptionRule.getExceptionTaxRate();
	              return rate;
	            }
	          } else {
	            rate = taxExceptionRule.getExceptionTaxRate();
	            return rate;
	          }
	          // end rajesh
	        } catch (Exception ex) {
	          ex.printStackTrace();
	        }
	        
	      }
	      String countryId = (((CMSStore)com.chelseasystems.cr.appmgr.AppManager.getInstance().
		          getGlobalObject("STORE")).getCountry());
	      if(!countryId.equals(null) && countryId.equals("CAN")){
	      if (taxExceptionRule.getItemBrandList().contains(currentItem.getBrand()) && ((((taxExceptionRule.getExceptionZipCodeList().contains(strZipcode) && taxExceptionRule.getExceptionZipCodeList().size() > 0)) || ((taxExceptionRule.getExceptionTaxJurList().contains(taxJur) && taxExceptionRule.getExceptionTaxJurList().size() > 0)))
		          || (taxExceptionRule.getExceptionTaxJurList().size() == 0 && taxExceptionRule.getExceptionZipCodeList().size() == 0))) {
		        try {
		           if ((posLineItemDetail instanceof SaleLineItemDetail && posLineItemDetail.getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) ||
		              (posLineItemDetail instanceof ReturnLineItemDetail && posLineItemDetail.getNetAmount().absoluteValue().greaterThan(taxExceptionRule.getThresholdAmount())) ||
		              (posLineItemDetail instanceof CMSPresaleLineItemDetail && (posLineItemDetail.getLineItem().getExtendedRetailAmount().subtract(posLineItemDetail.getLineItem().getExtendedReductionAmount())).greaterThan(taxExceptionRule.getThresholdAmount()))) {
		            if (taxExceptionRule.getAboveThresholdRule().equals(TaxUtilities.ABOVE_THRESHOLD_RULE_PARTIAL)) {
		              rate = taxExceptionRule.getExceptionTaxRate();
		              return rate;
		            }
		          } else {
		            rate = taxExceptionRule.getExceptionTaxRate();
		            return rate;
		          }
		          } catch (Exception ex) {
		          ex.printStackTrace();
		        }
		        
		      }
	      }
	    }
	    return defaultTaxRate;
	  }

  /**
   * put your documentation comment here
   * @param rule
   * @return
   */
  public static boolean isExceptionAppliedToFullAboveThresholdLineItem(String rule) {
    return rule.equals(TaxUtilities.ABOVE_THRESHOLD_RULE_FULL);
  }

 // Vishal changes this method to apply exact tax rate on item. 12 jan 2017
  public static void setExcTaxBreakdown(POSLineItemDetail posLineItemDetail)
  {
	  POSLineItem lineItem = posLineItemDetail.getLineItem();
	  CMSItem currentItem = (CMSItem)lineItem.getItem();  
	      String itemProduct=currentItem.getProductNum();
	      String itemCategory=currentItem.getCategory();
	 String strExceptionState = config.getString("EXCEPTION_STATE");
	       strExceptionState = getStateString(itemProduct, itemCategory, strExceptionState);
	       if(strExceptionState!=null){
      String strPstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_PST");
      String strQstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_QST");
      String strGstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_GST");
      String strHstTaxRate = config.getString(strExceptionState + ".EXCEPTION_TAX_RATE_HST");
      if(strPstTaxRate!=null)
      {
    	  lineItem.setPstTaxAmt(lineItem.getItemSellingPrice().multiply(new Double(strPstTaxRate))) ; 
      }
      if(strGstTaxRate!=null)
      {
    	  lineItem.setGstTaxAmt(lineItem.getItemSellingPrice().multiply(new Double(strGstTaxRate))) ; 
      }
      if(strQstTaxRate!=null)
      {
    	  lineItem.setQstTaxAmt(lineItem.getItemSellingPrice().multiply(new Double(strQstTaxRate))) ; 
      }
      if(strHstTaxRate!=null)
      {
    	  lineItem.setGsthstTaxAmt(lineItem.getItemSellingPrice().multiply(new Double(strHstTaxRate))) ; 
      }
	       }
  }
  //Ends Test
  //vishal yevale 29 Nov 2016 
  public static TaxExceptionRule getRule(Hashtable taxExceptionRules, String itemProduct, String itemCategory, Store fromStore ){
	  int size=taxExceptionRules.size();
	  String state =fromStore.getState().toUpperCase();
	  TaxExceptionRule taxExceptionRule = null;
	  for(int i=1;i<=size;i++){
		  taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(state);
		  if(taxExceptionRule!=null && taxExceptionRule.getItemProduct()!=null && taxExceptionRule.getItemClass()!=null){
		  if(Arrays.asList(taxExceptionRule.getItemProduct()).contains(itemProduct) && Arrays.asList(taxExceptionRule.getItemClass()).contains(itemCategory)){
			  return taxExceptionRule;
		  }
		  }
		  state=String.valueOf(i)+fromStore.getState().toUpperCase();;
	  }
	   return null;
  }

 //vishal Yevale : created this method  
  public static String getStateString(String itemProduct, String itemCategory, String strExceptionState ){
	  Hashtable taxExceptionRules = getTaxExceptionRules();     
	  int size=taxExceptionRules.size();
	  String state =strExceptionState;
	  TaxExceptionRule taxExceptionRule = null;
	  for(int i=1;i<=size;i++){
		  taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(state);
		  if(taxExceptionRule!=null && taxExceptionRule.getItemProduct()!=null && taxExceptionRule.getItemClass()!=null){
			  if(Arrays.asList(taxExceptionRule.getItemProduct()).contains(itemProduct) && Arrays.asList(taxExceptionRule.getItemClass()).contains(itemCategory)){
			  return state;
		  }		  
		  }
		  state=String.valueOf(i)+strExceptionState;
	  }
	   return null;
  }
  //end vishal yevale
}

