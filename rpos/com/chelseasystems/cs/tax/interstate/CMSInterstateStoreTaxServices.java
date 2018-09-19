/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.tax.interstate;

import  java.sql.*;
import  java.util.*;
import  java.util.Date;

import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.currency.*;
import  com.chelseasystems.cr.pos.*;
import  com.chelseasystems.cs.pos.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cr.tax.*;
import com.chelseasystems.cs.config.ArmTaxRateConfig;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.item.*;
import  com.chelseasystems.cs.tax.*;
import  com.chelseasystems.cs.store.*;


/**
 * <p>Title: CMSInterstateStoreTaxServices</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class CMSInterstateStoreTaxServices extends CMSTaxServices {
  public static final String TAX_ENGINE_NAME = TaxTypeKey.TAX_ENGINE_NAME_STORE_RATE;
  public Hashtable taxExceptionRules;
  ArmTaxRateDAO armTaxRateDAO;
  ArmTaxRateExcDAO armTaxRateExcDAO;
  StoreDAO storeDAO;


  /**
   * Default constructor
   */
  public CMSInterstateStoreTaxServices () {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    armTaxRateDAO = (ArmTaxRateDAO)configMgr.getObject("TAXRATE_DAO");
    armTaxRateExcDAO = (ArmTaxRateExcDAO)configMgr.getObject("ARM_TAX_RATE_EXC_DAO");
    storeDAO = (StoreDAO)configMgr.getObject("STORE_DAO");
    taxExceptionRules = TaxUtilities.getTaxExceptionRules();
  }

  /**
   * put your documentation comment here
   * @param zipcode
   * @return
   */
  public ArmTaxRate[] findByZipCode (String zipcode) {
    try {
      return  armTaxRateDAO.multipleSelectByZipCode(zipcode);
    } catch (Exception e) {
      return  null;
    }
  }

  /**
   *
   * @param aCompositeTransaction CompositePOSTransaction
   * @param fromStore Store
   * @param toStore Store
   * @param aProcessDate Date
   * @return SaleTax
   * @throws Exception
   */
  public SaleTax getTaxAmounts (CompositePOSTransaction aCompositeTransaction, Store fromStore, Store toStore, Date process_dt, HashMap<String, Object[]> taxDetailMap) throws Exception {
	  //Mayuri Edhara :: added to get the key based on the query to be executed.
	  String taxKey = null;
	  
	  try {
    
    java.sql.Date aProcessDate = new java.sql.Date(process_dt.getTime());
      ArrayList aSaleLineItems = new ArrayList();
      double rate = 0.0d;
      String strState = null;
      String taxType =null;
      String taxJurisdiction = null;
      double gstTax = 0.0d;
      double pstTax = 0.0d;
      double hstTax = 0.0d;
      double qstTax = 0.0d;
      double exceptionTaxRate = 0.0d;
      double stateTaxAmt = 0.0d;
      double cityTaxAmt = 0.0d;
	  Map taxMap = new HashMap();
	  Map taxExcMap = new HashMap();
	  ArmTaxRateConfig[] armTaxCARateConfig = null;
	  ArmTaxRateConfig armTaxRateConfig = null;
      CMSCompositePOSTransaction transaction = (CMSCompositePOSTransaction)aCompositeTransaction;
      POSLineItemDetail[] taxableLineItemDetails = aCompositeTransaction.getTaxableLineItemDetailsArray();
      CMSSaleTaxDetail[] saleTaxDetails = new CMSSaleTaxDetail[taxableLineItemDetails.length];
      for (int index = 0; index < taxableLineItemDetails.length; index++) {
        rate = 0.0d;
        exceptionTaxRate = 0.0d;
        strState = null;
        ArmCurrency belowThresholdTaxAmount = new ArmCurrency(0.0d);
        ArmCurrency taxAmount = new ArmCurrency(0.0d);
        ArmCurrency netAmount = taxableLineItemDetails[index].getNetAmount();
        if (taxableLineItemDetails[index] instanceof CMSPresaleLineItemDetail)
          netAmount = taxableLineItemDetails[index].getLineItem().getExtendedRetailAmount().subtract(taxableLineItemDetails[index].getLineItem().getExtendedReductionAmount());
        if (taxableLineItemDetails[index] instanceof SaleLineItemDetail) {
          CMSSaleLineItem saleLineItem = (CMSSaleLineItem)taxableLineItemDetails[index].getLineItem();
          ShippingRequest shippingRequest = null;
          try {
            if (saleLineItem.getTaxableMiscLineItem() != null) {
              shippingRequest = saleLineItem.getShippingRequest();
            }
            else if (saleLineItem.getAlterationLineItem() != null) {
              if (saleLineItem.getAlterationLineItem().hasShippingRequest()) {
                saleLineItem.setShippingRequest(saleLineItem.getAlterationLineItem().getShippingRequest());
                shippingRequest = saleLineItem.getShippingRequest();
              }
            }
            else
              shippingRequest = saleLineItem.getShippingRequest();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
       
      // Anjana: this class gets executed for all the items in the sale screen so if condition takes care of shipping and else condition checks for other items which does not have shipping
          if (saleLineItem.hasShippingRequest()) {
        	  ArmTaxRate[] armcaTaxRate = null;
        	  ArmTaxRate armTaxRate = null;
        	  ArmTaxRate armStateTaxRate = null;
        	  //added country check for USA AND CAN : VISHAL YEVALE 9 JAN 2017
        	  if(shippingRequest.getCountry().equalsIgnoreCase("USA") || shippingRequest.getCountry().equalsIgnoreCase("CAN")){
        		  
        		  //Mayuri Edhara :: check for the Key value pair from the MAP selectByZipCityState:: 
        		  taxKey = shippingRequest.getZipCode()+shippingRequest.getCity()+shippingRequest.getState();
        		  
        		  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){

        			  Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
        			  
        			  	if(armTaxRateObj == null){
        			  		armTaxRate = null;
        			  		
  		            	}else if(armTaxRateObj instanceof ArmTaxRate){
  		            		armTaxRate = (ArmTaxRate)armTaxRateObj;  		            		

  		            	}        			  
        		  } else {

        			  armTaxRate = armTaxRateDAO.selectByZipCityState(shippingRequest.getZipCode(), shippingRequest.getCity(), shippingRequest.getState(),(java.sql.Date)aProcessDate);

        			  if(taxKey != null){
        				  
        				  taxDetailMap.put(taxKey, new Object[]{armTaxRate});
        		  	  }
        		  }

        		// check for the Key value pair from the MAP for PR :: selectByStateAndNullZip::
        		  if(shippingRequest.getState().equalsIgnoreCase("PR")){
        			  
        			  taxKey = null;
        			  taxKey = shippingRequest.getState();   
        			  
		    		  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){ 
		    			  
		    			  Object armStateTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
		    			  
		    			  	if(armStateTaxRateObj == null){
		    			  		armStateTaxRate = null;
		    			  		
      		            	}else if(armStateTaxRateObj instanceof ArmTaxRate){
      		            		armStateTaxRate = (ArmTaxRate)armStateTaxRateObj;
      		            		
      		            	}
		    			  
		    		  } else {
		    			  
		    			  armStateTaxRate = armTaxRateDAO.selectByStateAndNullZip(shippingRequest.getState(),(java.sql.Date)aProcessDate);
		    			  if(taxKey != null){
	        				  taxDetailMap.put(taxKey, new Object[]{armStateTaxRate});
		        		  }
		                 
		    			  
		    		  }           
        		  } 
                  
                  
                  
        	  }else{
        		  
        		 
        		  
        		  //Mayuri Edhara ::  check for the Key value pair from the MAP selectByZipCityState:: 
        		  taxKey = null;
    			  taxKey = shippingRequest.getZipCode()+shippingRequest.getCity()+shippingRequest.getState(); 
    			  
    			  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
    				  
       			   		Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
       			   
	    			  	if(armTaxRateObj == null){
	    			  		armTaxRate = null;
	    			  		
  		            	}else if(armTaxRateObj instanceof ArmTaxRate){
  		            		armTaxRate = (ArmTaxRate)armTaxRateObj;
  		            		
  		            	}
	    			  
	    		  } else {
	    			  
	    			  armTaxRate = armTaxRateDAO.selectByZipCityState(shippingRequest.getZipCode(), shippingRequest.getCity(), shippingRequest.getState());
	    			  if(taxKey != null){	    				  
        				  taxDetailMap.put(taxKey, new Object[]{armTaxRate});
	        		  }
	                 
	    			  
	    		  }  
    			  
    			// check for the Key value pair from the MAP for PR :: selectByStateAndNullZip::
        		  if(shippingRequest.getState().equalsIgnoreCase("PR")){
        			  
        			  taxKey = null;
        			  taxKey = shippingRequest.getState();        		  
		    		  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){

		    			  Object armStateTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
		    			  
		    			  	if(armStateTaxRateObj == null){
		    			  		armStateTaxRate = null;
		    			  		
      		            	}else if(armStateTaxRateObj instanceof ArmTaxRate){
      		            		armStateTaxRate = (ArmTaxRate)armStateTaxRateObj;
      		            		
      		            	}
		    			  
		    		  } else {
		    			  
		    			  armStateTaxRate = armTaxRateDAO.selectByStateAndNullZip(shippingRequest.getState());
		    			  if(taxKey != null){
		    				  taxDetailMap.put(taxKey, new Object[]{armStateTaxRate});
		        		  }
		                 
		    			  
		    		  }           
        		  }
        		  
                    

        	  }
        	  
        	  if(shippingRequest.getCountry().equalsIgnoreCase("CAN")){
        		 
        		//Mayuri Edhara ::  check for the Key value pair from the MAP selectByZipCityState:: 
        		  taxKey = null;
    			  taxKey = shippingRequest.getZipCode()+shippingRequest.getCountry()+shippingRequest.getState(); 
    			  
    			  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
    				  
    				  Object[] armCaTaxRateObj = taxDetailMap.get(taxKey);	
    				  
	    			  	if(armCaTaxRateObj == null){
	    			  		armcaTaxRate = null;
	    			  		
  		            	}else if(armCaTaxRateObj instanceof ArmTaxRate[]){
  		            		armcaTaxRate = (ArmTaxRate[])armCaTaxRateObj;
  		            		
  		            	}
	    			  
	    		  } else {
	    			  
	    			  armcaTaxRate = armTaxRateDAO.selectTaxTypeAndRate(shippingRequest.getZipCode(), shippingRequest.getCountry(), shippingRequest.getState(),(java.sql.Date)aProcessDate);
	    			  
	    			  if(taxKey != null){
	    				  taxDetailMap.put(taxKey, armcaTaxRate);
	        		  }
	                 
	    			  
	    		  }
        		  
            	  
        	  }
        	  
            if(armStateTaxRate!=null)
           ((CMSShippingRequest)shippingRequest).setOfflineStateTax(armStateTaxRate.getTaxRate());
                        
           if (armTaxRate != null) {
        	 //9 jan, 2017 :vishal_k : added for Handling null tax_rate and expired record from ARM_TAX_RATE   
        	  if(armTaxRate.getTaxRate() != null){
              rate = armTaxRate.getTaxRate().doubleValue();
              taxJurisdiction = armTaxRate.getTaxJurisdiction();
              if(armStateTaxRate!=null){
	              if(shippingRequest.getState().equalsIgnoreCase("PR")){
	            	  rate = rate + armStateTaxRate.getTaxRate().doubleValue();
	            	  stateTaxAmt = armStateTaxRate.getTaxRate().doubleValue();
	            	  cityTaxAmt = armTaxRate.getTaxRate().doubleValue();
	            	   saleTaxDetails[index] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_STORE_RATE, taxAmount.add(belowThresholdTaxAmount), rate, strState, taxJurisdiction);
	            	 //[index].setCityTax(cityTaxAmt);
	            	  //saleTaxDetails[index].setStateTax(stateTaxAmt);
	              }
              }
              
              if(armcaTaxRate!=null){
                  if(shippingRequest.getCountry().equalsIgnoreCase("CAN")){
                	 rate = getCAtax(armcaTaxRate);
                   }
              }
              
        	  }else{
        		   rate = fromStore.getDefaultTaxRate();
        		   taxJurisdiction = "";
        		   
        	  }
           } else {
               rate = fromStore.getDefaultTaxRate();
               taxJurisdiction = "";
           }
             //vishal_k: ends here 
           // Vishal Yevale : 29 nov 2016 added for TAX CR from Jason
           TaxExceptionRule taxExceptionRule=null;
           if(shippingRequest.getCountry().equalsIgnoreCase("USA") || shippingRequest.getCountry().equalsIgnoreCase("CAN")){
        	   POSLineItem lineItem = taxableLineItemDetails[index].getLineItem();
        	      CMSItem currentItem = (CMSItem)lineItem.getItem();  
        	      String itemProduct=currentItem.getProductNum();
        	      String itemCategory=currentItem.getCategory();
        	   taxExceptionRule =TaxUtilities.getRule(taxExceptionRules , itemProduct , itemCategory, fromStore);
           }else{   // end Vishal yevale 29 nov 2016
            	 taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(shippingRequest.getState().toUpperCase());
           }
          if (taxExceptionRule != null) {
              exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemDetails[index], fromStore, taxExceptionRule, rate, taxJurisdiction);
              
              if (taxableLineItemDetails[index].getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) {
                belowThresholdTaxAmount = taxExceptionRule.getThresholdAmount().multiply(exceptionTaxRate).round();
                netAmount = taxableLineItemDetails[index].getNetAmount().subtract(taxExceptionRule.getThresholdAmount());
              }
              //rajesh
              else {
                rate = exceptionTaxRate;
              }
              // end rajesh
            }
            //Anjana: this loop does not get executed as we have removed the condition for same store shipping . the if condition above gets executed for same store and different str
            CMSItem cmsItem;
       	 
            if (saleLineItem.getTaxableMiscLineItem() != null) {
    	    	cmsItem = (CMSItem)saleLineItem.getTaxableMiscLineItem().getItem();
    	    }else{
            	cmsItem = (CMSItem)saleLineItem.getItem();
    	    } 
            
            if(cmsItem.getCategory()!=null){
            //vishal yevale : 9 jan 2017 : passed product num and process date.
            	  //added country check for USA AND CAN : VISHAL YEVALE 9 JAN 2017
           	  if(shippingRequest.getCountry().equalsIgnoreCase("USA") || shippingRequest.getCountry().equalsIgnoreCase("CAN")){
           		  
           		
           		  //Mayuri Edhara ::  Getting the key value pair for ARM_TAX_EXEC :: selectByZipCityStateForSAP ::
           		  taxKey = null;
           		  taxKey = shippingRequest.getState()+shippingRequest.getZipCode()+cmsItem.getCategory()+cmsItem.getProductNum();
           		  
	           		if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           			
	           			Object armTaxRateConfigObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           			
	           			if(armTaxRateConfigObj == null){
    		            	armTaxRateConfig = null;
    		            	
    		            }else if(armTaxRateConfigObj instanceof ArmTaxRateConfig){
    		            	armTaxRateConfig = (ArmTaxRateConfig)armTaxRateConfigObj;
    		            	
    		            }
	      			  
	      		  } else {
	      			  
	      			  armTaxRateConfig = armTaxRateExcDAO.selectByZipCityStateForSAP(shippingRequest.getState(),shippingRequest.getZipCode() ,cmsItem.getCategory(), cmsItem.getProductNum(),(java.sql.Date)aProcessDate);
	              	  
	      			  if(taxKey != null){
	              		taxDetailMap.put(taxKey, new Object[] {armTaxRateConfig});
	      		  	  }
	      		  }           		  
           		           	 	          	  
           	  
           	  }else{
           		  
           		 //Mayuri Edhara ::  Getting the key value pair for ARM_TAX_EXEC :: selectByZipCityStateForSAP ::           		 
           		  taxKey = null;
         		  taxKey = shippingRequest.getState()+shippingRequest.getZipCode()+cmsItem.getCategory();
         		  
	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           		  
	           			Object armTaxRateConfigObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           			
	           			if(armTaxRateConfigObj == null){
    		            	armTaxRateConfig = null;
    		            	
    		            }else if(armTaxRateConfigObj instanceof ArmTaxRateConfig){
    		            	armTaxRateConfig = (ArmTaxRateConfig)armTaxRateConfigObj;
    		            	
    		            }
	      			  
	      		  } else {
	      			  
	      			  armTaxRateConfig = armTaxRateExcDAO.selectByZipCityStateForSAP(shippingRequest.getState(),shippingRequest.getZipCode() ,cmsItem.getCategory());
	      			  
	               	  if(taxKey != null){
	              		taxDetailMap.put(taxKey, new Object[]{armTaxRateConfig});
	      		  	  }
	      		  }          		 
           		 
               	 
           	  }
           	  
           	 if(shippingRequest.getCountry().equalsIgnoreCase("CAN")){
           		 
           	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_EXEC :: getExceptionTaxDetailByStateAndZipcodeCategory ::
           		taxKey = null;
           		taxKey = "CAN"+shippingRequest.getState()+shippingRequest.getZipCode()+cmsItem.getCategory();
           		
           		if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
           			Object[] armTaxCARateConfigObj = taxDetailMap.get(taxKey);
           			
           			if(armTaxCARateConfigObj == null){
           				armTaxCARateConfig = null;
           				
		            }else if(armTaxCARateConfigObj instanceof ArmTaxRateConfig[]){
		            	armTaxCARateConfig = (ArmTaxRateConfig[])armTaxCARateConfigObj;
		            	
		            }
      			  
           		} else {
      			  
           		  armTaxCARateConfig = armTaxRateExcDAO.getExceptionTaxDetailByStateAndZipcodeCategory(shippingRequest.getState(),shippingRequest.getZipCode() ,cmsItem.getCategory());
                  if(taxKey != null){
	              		taxDetailMap.put(taxKey, armTaxCARateConfig);
      		  	  }
           		}
           		           		
           	 
           	 }
           	 
           	 if(armTaxRateConfig!=null){
           	//Poonam: 10 jan, 2017 : added for Handling null threshold amt and null tax_rate from ARM_TAX_RATE_EXC
           		if(armTaxRateConfig.getAmountThr()!=null && armTaxRateConfig.getTaxRate()!=null){
           		  if ((taxableLineItemDetails[index].getNetAmount().doubleValue()) > ((armTaxRateConfig.getAmountThr()))) {
           			   belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*(armTaxRateConfig.getTaxRate()));
                    	 if(shippingRequest.getState().equalsIgnoreCase("PR")){
                    		 if(armStateTaxRate!=null)
                          	  belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*((armTaxRateConfig.getTaxRate()) +  armStateTaxRate.getTaxRate()));
                          	 }
                    	 
                    	 if(shippingRequest.getCountry().equalsIgnoreCase("CAN")){
                       	 if(armTaxCARateConfig!=null)
                    	  belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*(getCAExctax(armTaxCARateConfig)));
                    	 }
                    	 
                    	 
                         netAmount = new ArmCurrency((taxableLineItemDetails[index].getNetAmount().doubleValue()) - (armTaxRateConfig.getAmountThr()));
                       }
                       else {
                     	
                       	  rate = armTaxRateConfig.getTaxRate().doubleValue();
                       	  if(armStateTaxRate !=null){
                       	 if(shippingRequest.getState().equalsIgnoreCase("PR")){
                       	  rate = rate + armStateTaxRate.getTaxRate().doubleValue();
                       	  stateTaxAmt = armStateTaxRate.getTaxRate().doubleValue();
                       	  cityTaxAmt = armTaxRateConfig.getTaxRate().doubleValue();
                        }
                       	 }
                      
                       	 if(shippingRequest.getCountry().equalsIgnoreCase("CAN")){
                        	  if(armTaxCARateConfig!=null){
                       		rate = getCAExctax(armTaxCARateConfig);
                       	 }
                       	  }
                             taxJurisdiction = armTaxRateConfig.getTaxJur(); 
                       }
           		 
           	 }else{ //default store tax
            		rate = fromStore.getDefaultTaxRate();
            		taxJurisdiction = "";
           	 }
            	 //Poonam:ends here
           	}
           	}
           
         taxMap = gettaxMap(armcaTaxRate);
         //Test
         if(armTaxCARateConfig!=null)//Ends
         taxExcMap = getExctaxMap(armTaxCARateConfig);
            strState = shippingRequest.getState();
          }
          //Anjana: this is the loop which calculates tax for items which does not have shipping request
          else {
            rate = fromStore.getDefaultTaxRate().doubleValue();
            if(((CMSStore)fromStore).getStateTax()!=null){
       	 if(fromStore.getState().equalsIgnoreCase("PR")){
       		rate = fromStore.getDefaultTaxRate().doubleValue() + ((CMSStore)fromStore).getStateTax().doubleValue();
       	 }
      CMSStore cmsStore = 	((CMSStore)fromStore) ;
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
            }
            
            ArmTaxRate armTaxRate = null;
            ArmTaxRate armStateTaxRate = null;
            //Vishal Yevale : Null pointer Exception issue while send sale -> scan another item 25 Jan 2017
          	  if(fromStore.getCountry().equalsIgnoreCase("USA") || fromStore.getCountry().equalsIgnoreCase("CAN")){
          		  
          		//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByZipCityStateForSAP ::           		 
          		  taxKey = null;
        		  taxKey = fromStore.getZipCode()+fromStore.getCity()+fromStore.getState();
        		  
	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           		  
	           			Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           					
	           			if(armTaxRateObj == null){
	           				armTaxRate = null;
	           				
   		            }else if(armTaxRateObj instanceof ArmTaxRate){
   		            	armTaxRate = (ArmTaxRate)armTaxRateObj;
   		            	
   		            }
	      			  
	      		  } else {
	      			  
	      			armTaxRate = armTaxRateDAO.selectByZipCityState(fromStore.getZipCode(), fromStore.getCity(), fromStore.getState(),(java.sql.Date)aProcessDate);
	               	  if(taxKey != null){
	              		taxDetailMap.put(taxKey, new Object[]{armTaxRate});
	      		  	  }
	      		  }
                 
                                 
                 if(fromStore.getState().equalsIgnoreCase("PR")){                	
                	 
                	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByStateAndNullZip ::           		 
             		  taxKey = null;
             		  taxKey = fromStore.getState();
           		  
	   	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	   	           			Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	   	           			
	   	           			if(armTaxRateObj == null){
	   	           				armStateTaxRate = null;
	   	           		
	      		            }else if(armTaxRateObj instanceof ArmTaxRate){
	      		            	armStateTaxRate = (ArmTaxRate)armTaxRateObj;
	      		            	
	      		            }
	   	      			  
	   	      		  } else {
	   	      			  
	   	      			armStateTaxRate = armTaxRateDAO.selectByStateAndNullZip(fromStore.getState(), (java.sql.Date)aProcessDate);
	   	               	  if(taxKey != null){
	   	               		  taxDetailMap.put(taxKey, new Object[]{armStateTaxRate});
	   	      		  	  }
	   	      		  }                	 
                  
	   	           	 
                 }
                  

          	  }else{
          		  
          		  
          		  
          		//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByZipCityStateForSAP ::           		 
          		  taxKey = null;
        		  taxKey = fromStore.getZipCode()+fromStore.getCity()+fromStore.getState();
        		  
	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           		  
	           			Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           			
	           			if(armTaxRateObj == null){
	           				armTaxRate = null;
	           				
   		            }else if(armTaxRateObj instanceof ArmTaxRate){
   		            	armTaxRate = (ArmTaxRate)armTaxRateObj;
   		            	
   		            }
	      			  
	      		  } else {
	      			  
	      			 armTaxRate = armTaxRateDAO.selectByZipCityState(fromStore.getZipCode(), fromStore.getCity(), fromStore.getState());
	               	  if(taxKey != null){
	      			  taxDetailMap.put(taxKey, new Object[]{armTaxRate});
	      		  	  }
	      		  }
          		                   
                  
                  
                  if(fromStore.getState().equalsIgnoreCase("PR")){  	  
                  
                   
                   //Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByStateAndNullZip ::           		 
          		  	taxKey = null;
          		  	taxKey = fromStore.getState();
        		  
	   	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	   	           		  
	   	           			Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	   	           			
	   	           			if(armTaxRateObj == null){
	   	           				armStateTaxRate = null;
	   	           		
	      		            }else if(armTaxRateObj instanceof ArmTaxRate){
	      		            	armStateTaxRate = (ArmTaxRate)armTaxRateObj;
	      		            	
	      		            }
	   	      			  
	   	      		  } else {
	   	      			  
	   	      			  armStateTaxRate = armTaxRateDAO.selectByStateAndNullZip(fromStore.getState());
	   	               	  if(taxKey != null){
	   	      			  taxDetailMap.put(taxKey, new Object[]{armStateTaxRate});
	   	      		  	  }
	   	      		  }	
                   
                  }


          	 }
          	  
           ArmTaxRate armCATaxRate[] = null;
           if(fromStore.getCountry().equalsIgnoreCase("CAN")){ 
        	   
        	 //Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByStateAndNullZip ::           		 
     		  	taxKey = null;
     		  	taxKey = fromStore.getState()+fromStore.getCountry()+fromStore.getState();
     		  	
  	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
  	           			Object[] armCATaxRateObj = taxDetailMap.get(taxKey);
  	           			if(armCATaxRateObj == null){
  	           				armCATaxRate = null;
  	           		
     		            }else if(armCATaxRateObj instanceof ArmTaxRate[]){
     		            	armCATaxRate = (ArmTaxRate[])armCATaxRateObj;
     		            	
     		            }
  	      			  
  	      		  } else {
  	      			  
  	      			armCATaxRate = armTaxRateDAO.selectTaxTypeAndRate(fromStore.getZipCode(), fromStore.getCountry(), fromStore.getState(), (java.sql.Date)aProcessDate);
  	      			  
  	      			if(taxKey != null){
  	      				taxDetailMap.put(taxKey, armCATaxRate);
  	      		  	  }
  	      		  }
        	   
           }
                   
                
       if (armTaxRate != null) {
    	 //9 jan, 2017 :vishal_k : added for Handling null tax_rate and expired record  from ARM_TAX_RATE 
    	   if(armTaxRate.getTaxRate() !=null){
         rate = armTaxRate.getTaxRate().doubleValue();
         taxJurisdiction = armTaxRate.getTaxJurisdiction();
         if(armStateTaxRate!=null){
         if(fromStore.getState().equalsIgnoreCase("PR")){
       	  rate = rate + armStateTaxRate.getTaxRate().doubleValue();
       	 }
       }
         if(armCATaxRate!=null){
         if(fromStore.getCountry().equalsIgnoreCase("CAN")){
           	  rate = getCAtax(armCATaxRate);
            }
         }
    	   }else{
	    	   rate = fromStore.getDefaultTaxRate();
	           taxJurisdiction = "";
       }
       }
       
       else {
         rate =  fromStore.getDefaultTaxRate();
         taxJurisdiction = "";
       }//vishal_k:ends here
    // Vishal Yevale : 29 nov 2016 added for TAX CR from Jason
       TaxExceptionRule taxExceptionRule=null;
       if(fromStore.getCountry().equalsIgnoreCase("USA") || fromStore.getCountry().equalsIgnoreCase("CAN")){
    	   POSLineItem lineItem = taxableLineItemDetails[index].getLineItem();
    	      CMSItem currentItem = (CMSItem)lineItem.getItem();  
    	      String itemProduct=currentItem.getProductNum();
    	      String itemCategory=currentItem.getCategory();
    	   taxExceptionRule =TaxUtilities.getRule(taxExceptionRules , itemProduct , itemCategory, fromStore);
       }else{   // end Vishal yevale 29 nov 2016
        	 taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(fromStore.getState().toUpperCase());
       }
       
       if (taxExceptionRule != null) {
                     exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemDetails[index], fromStore, taxExceptionRule, rate, taxJurisdiction);
                    
                     if (taxableLineItemDetails[index].getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) {
                       belowThresholdTaxAmount = taxExceptionRule.getThresholdAmount().multiply(exceptionTaxRate).round();
                       netAmount = taxableLineItemDetails[index].getNetAmount().subtract(taxExceptionRule.getThresholdAmount());
                     }
                     //rajesh
                     else {
                       rate = exceptionTaxRate;
                     }
                     // end rajesh
                   }
				   
	 CMSItem cmsItem;
      	 if (saleLineItem.getTaxableMiscLineItem() != null) {
    	    	cmsItem = (CMSItem)saleLineItem.getTaxableMiscLineItem().getItem();
    	    }else{
            	cmsItem = (CMSItem)saleLineItem.getItem();
    	    }
      	 if(cmsItem.getCategory()!=null){
      		 //vishal Yevale : 9 jan 2017 passed product Num and process date
             if(fromStore.getCountry().equalsIgnoreCase("USA") || fromStore.getCountry().equalsIgnoreCase("CAN")){
            	 
            	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByZipCityStateForSAP ::
          		  taxKey = null;
          		  taxKey = fromStore.getState()+fromStore.getZipCode()+cmsItem.getCategory()+cmsItem.getProductNum();
          		  
	           	 if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           			Object armTaxRateConfigObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           			
	           			if(armTaxRateConfigObj == null){
	           				armTaxRateConfig = null;
	           				
	           			}else if(armTaxRateConfigObj instanceof ArmTaxRateConfig){
	           				armTaxRateConfig = (ArmTaxRateConfig)armTaxRateConfigObj;
	           				
	           			}
	      			  
	      		  } else {
	      			  
	      			armTaxRateConfig = armTaxRateExcDAO.selectByZipCityStateForSAP(fromStore.getState(),fromStore.getZipCode() ,cmsItem.getCategory(), cmsItem.getProductNum(),(java.sql.Date)aProcessDate);
	      	      	
	      			if(taxKey != null){
	      			  taxDetailMap.put(taxKey, new Object[]{armTaxRateConfig});
	      		  	  }
	      		  }
      			
             }
             else{
            	 
            	 
            	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByZipCityStateForSAP ::
         		  taxKey = null;
         		  taxKey = fromStore.getState()+fromStore.getZipCode()+cmsItem.getCategory();
         		  
	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           		  
	           			Object armTaxRateConfigObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           			
	           			if(armTaxRateConfigObj == null){
	           				armTaxRateConfig = null;
	           				
	           			}else if(armTaxRateConfigObj instanceof ArmTaxRateConfig){
	           				armTaxRateConfig = (ArmTaxRateConfig)armTaxRateConfigObj;
	           				
	           			}
	      			  
	      		  } else {
	      			  
	      			armTaxRateConfig = armTaxRateExcDAO.selectByZipCityStateForSAP(fromStore.getState(),fromStore.getZipCode() ,cmsItem.getCategory()); 
	      			if(taxKey != null){
	      			  taxDetailMap.put(taxKey, new Object[]{armTaxRateConfig});
	      		  	 }
	      		  }
               
             }
      	 if(fromStore.getCountry().equalsIgnoreCase("CAN")){      		
      		 
      		//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByZipCityStateForSAP ::
    		  taxKey = null;
    		  taxKey = "CAN"+fromStore.getState()+fromStore.getZipCode()+cmsItem.getCategory();

          	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
          			Object[] armTaxCARateConfigObj = taxDetailMap.get(taxKey);
          			if(armTaxCARateConfigObj == null){
          				armTaxCARateConfig = null;
          				
          			}else if(armTaxCARateConfigObj instanceof ArmTaxRateConfig[]){
          				armTaxCARateConfig = (ArmTaxRateConfig[])armTaxCARateConfigObj;
          				
          			}
     			  
     		  } else {
     			  
     			armTaxCARateConfig = armTaxRateExcDAO.getExceptionTaxDetailByStateAndZipcodeCategory(fromStore.getState(),fromStore.getZipCode() ,cmsItem.getCategory());
           		if(taxKey != null){
           			taxDetailMap.put(taxKey, armTaxCARateConfig);
     		  	 }
     		  }
      		 
      		
      	 }
      	 if(armTaxRateConfig!=null){
           	//Poonam: 10 jan, 2017 : added for Handling null threshold amt and null tax_rate from ARM_TAX_RATE_EXC
           		if(armTaxRateConfig.getAmountThr()!=null && armTaxRateConfig.getTaxRate()!=null){
      		  if ((taxableLineItemDetails[index].getNetAmount().doubleValue()) > ((armTaxRateConfig.getAmountThr()))) {
      			 belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*(armTaxRateConfig.getTaxRate()));
               	 if(fromStore.getState().equalsIgnoreCase("PR")){
               		 if(armStateTaxRate!=null)
                     	  belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*((armTaxRateConfig.getTaxRate()) +  armStateTaxRate.getTaxRate()));
                     	 }
               	 if(fromStore.getCountry().equalsIgnoreCase("CAN")){
               		 if(armTaxCARateConfig!=null)
                   		   belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*(getCAExctax(armTaxCARateConfig)));
                     	 }
                    netAmount = new ArmCurrency((taxableLineItemDetails[index].getNetAmount().doubleValue()) - (armTaxRateConfig.getAmountThr()));
                  }
                  else {
                	
                  	  rate = armTaxRateConfig.getTaxRate().doubleValue();
                  	  if(armStateTaxRate !=null){
                  	 if(fromStore.getState().equalsIgnoreCase("PR")){
                  	  rate = rate + armStateTaxRate.getTaxRate().doubleValue();
                   }
                  	  }
                  	  if(armTaxCARateConfig!=null){
                  	if(fromStore.getCountry().equalsIgnoreCase("CAN")){
                  	  rate = getCAExctax(armTaxCARateConfig);
                      }
                  	  } 
                  	  
                        taxJurisdiction = armTaxRateConfig.getTaxJur(); 
                  }
      		 
      	 }
      	else{//default store tax
           		rate = fromStore.getDefaultTaxRate();
        		taxJurisdiction = "";
      	}//Poonam:ends here
      	}
      	 }
       strState = fromStore.getState();
     
          }
          taxAmount = netAmount.multiply(rate).round();
          saleTaxDetails[index] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_STORE_RATE, taxAmount.add(belowThresholdTaxAmount), rate, strState, taxJurisdiction, new ArmCurrency(stateTaxAmt),taxMap, taxExcMap , armTaxCARateConfig,taxDetailMap);
        }
        else if (taxableLineItemDetails[index] instanceof ReturnLineItemDetail) {
          ReturnLineItem aReturnLineItem = (ReturnLineItem)taxableLineItemDetails[index].getLineItem();
          if (!aReturnLineItem.isTaxExempt() && aReturnLineItem.isMiscReturn() && aReturnLineItem.isItemTaxable()) {
        	// Vishal Yevale : 29 nov 2016 added for TAX CR from Jason
              TaxExceptionRule taxExceptionRule=null;
              if(fromStore.getCountry().equalsIgnoreCase("USA") || fromStore.getCountry().equalsIgnoreCase("CAN") ){
           	   POSLineItem lineItem = taxableLineItemDetails[index].getLineItem();
           	      CMSItem currentItem = (CMSItem)lineItem.getItem();  
           	      String itemProduct=currentItem.getProductNum();
           	      String itemCategory=currentItem.getCategory();
           	   taxExceptionRule =TaxUtilities.getRule(taxExceptionRules , itemProduct , itemCategory, fromStore);
              }else{   // end Vishal yevale 29 nov 2016
               	 taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(fromStore.getState().toUpperCase());
              }            rate = fromStore.getDefaultTaxRate().doubleValue();
            taxJurisdiction = ((CMSStore)fromStore).getTaxJurisdiction();
            if (taxExceptionRule != null) {
              exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemDetails[index], fromStore, taxExceptionRule, rate, taxJurisdiction);
              if (taxableLineItemDetails[index].getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) {
                belowThresholdTaxAmount = taxExceptionRule.getThresholdAmount().multiply(exceptionTaxRate).round();
                netAmount = taxableLineItemDetails[index].getNetAmount().subtract(taxExceptionRule.getThresholdAmount());
              }
              else {
                rate = exceptionTaxRate;
              }
            }
            taxAmount = netAmount.multiply(rate).round();
            saleTaxDetails[index] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_STORE_RATE, taxAmount.add(belowThresholdTaxAmount), rate, fromStore.getState(), taxJurisdiction);
            //  saleTaxList.add(index,new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_STORE_RATE
            //              , taxAmount.add(belowThresholdTaxAmount), rate, strState, taxJurisdiction));
          }
        }
        else if (taxableLineItemDetails[index] instanceof CMSPresaleLineItemDetail) {
          CMSPresaleLineItem presaleLineItem = (CMSPresaleLineItem)taxableLineItemDetails[index].getLineItem();
          ShippingRequest shippingRequest = null;
          try {
            if (presaleLineItem.getTaxableMiscLineItem() != null) {
              shippingRequest = presaleLineItem.getShippingRequest();
            }
            else if (presaleLineItem.getAlterationLineItem() != null) {
              if (presaleLineItem.getAlterationLineItem().hasShippingRequest()) {
                presaleLineItem.setShippingRequest(presaleLineItem.getAlterationLineItem().getShippingRequest());
                shippingRequest = presaleLineItem.getShippingRequest();
              }
            }
            else
              shippingRequest = presaleLineItem.getShippingRequest();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
          if (presaleLineItem.hasShippingRequest() && !shippingRequest.getZipCode().equals(fromStore.getZipCode())) {
        	  //vishal yevale 9 jan 2017 
        	  ArmTaxRate armTaxRate= null;
        	  ArmTaxRate armStateTaxRate = null;
              if(fromStore.getCountry().equalsIgnoreCase("USA") || fromStore.getCountry().equalsIgnoreCase("CAN") ){            	 
            	  
            	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByZipCityStateForSAP ::
         		  taxKey = null;
         		  taxKey = shippingRequest.getZipCode()+shippingRequest.getCity()+shippingRequest.getState();
         		  
	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           		  
	           			Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           			
	           			if(armTaxRateObj == null){
	           				armTaxRate = null;
	           				
	           			}else if(armTaxRateObj instanceof ArmTaxRateConfig){
	           				armTaxRate = (ArmTaxRate)armTaxRateObj;
	           				
	           			}
	      			  
	      		  } else {
	      			  
	      			armTaxRate = armTaxRateDAO.selectByZipCityState(shippingRequest.getZipCode(), shippingRequest.getCity(), shippingRequest.getState() , (java.sql.Date)aProcessDate);
	            	 if(taxKey != null){
	      			  taxDetailMap.put(taxKey, new Object[]{armTaxRate});
	      		  	 }
	      		  }           	  
            	   
            	   if(shippingRequest.getState().equalsIgnoreCase("PR")){            		  
            	   
	            	 //Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByStateAndNullZip ::
	          		  taxKey = null;
	          		  taxKey = shippingRequest.getState();
	          		  
	 	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	 	           			Object armStateTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	 	           			
	 	           			if(armStateTaxRateObj == null){
	 	           				armStateTaxRate = null;
	 	           			
	 	           			}else if(armStateTaxRateObj instanceof ArmTaxRateConfig){
	 	           				armStateTaxRate = (ArmTaxRate)armStateTaxRateObj;
	 	           			
	 	           			}
	 	      			  
	 	      		  } else {
	 	      			  
	 	      			armStateTaxRate = armTaxRateDAO.selectByStateAndNullZip(shippingRequest.getState(), (java.sql.Date)aProcessDate);
	 	      			if(taxKey != null){
	 	      			  taxDetailMap.put(taxKey, new Object[]{armStateTaxRate});
	 	      		  	 }
	 	      		  }             	                      
	 	           	  
            	   }
              }else{            	 
            	  
            	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByZipCityStateForSAP ::
         		  taxKey = null;
         		  taxKey = shippingRequest.getZipCode()+shippingRequest.getCity()+shippingRequest.getState();
         		  
	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	           			Object armTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	           			if(armTaxRateObj == null){
	           				armTaxRate = null;
	           				
	           			}else if(armTaxRateObj instanceof ArmTaxRateConfig){
	           				armTaxRate = (ArmTaxRate)armTaxRateObj;
	           				
	           			}
	      			  
	      		  } else {
	      			  
	      			armTaxRate = armTaxRateDAO.selectByZipCityState(shippingRequest.getZipCode(), shippingRequest.getCity(), shippingRequest.getState());
	                 if(taxKey != null){
	      			  taxDetailMap.put(taxKey, new Object[]{armTaxRate});
	      		  	 }
	      		  } 
             	 
             		
             		if(shippingRequest.getState().equalsIgnoreCase("PR")){             			
             	   
 	            	 //Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE :: selectByStateAndNullZip ::
 	          		  taxKey = null;
 	          		  taxKey = shippingRequest.getState();
 	          		  
 	 	           	  if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
 	 	           			Object armStateTaxRateObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
 	 	           			
 	 	           			if(armStateTaxRateObj == null){
 	 	           				armStateTaxRate = null;
 	 	           			
 	 	           			}else if(armStateTaxRateObj instanceof ArmTaxRateConfig){
 	 	           				armStateTaxRate = (ArmTaxRate)armStateTaxRateObj;
 	 	           			
 	 	           			}
 	 	      			  
 	 	      		  } else {
 	 	      			  
 	 	      			 armStateTaxRate = armTaxRateDAO.selectByStateAndNullZip(shippingRequest.getState());
 	 	      			if(taxKey != null){
 	 	      			  taxDetailMap.put(taxKey, new Object[]{armStateTaxRate});
 	 	      		  	 }
 	 	      		  }             	                      
 	 	           	
             	   }
             	
              }
     
            if (armTaxRate != null) {
          	  //9 jan, 2017 :vishal_k : added for Handling null tax_rate and expired record from ARM_TAX_RATE 
          	if(armTaxRate.getTaxRate() != null ){
              rate = armTaxRate.getTaxRate().doubleValue();
              taxJurisdiction = armTaxRate.getTaxJurisdiction();
              if(armStateTaxRate!=null){
                  if(shippingRequest.getState().equalsIgnoreCase("PR") || armTaxRate.getCounty().equalsIgnoreCase("CAN")){
                	  rate = rate + armStateTaxRate.getTaxRate().doubleValue();
                 }
                }
          	}else{ 
        		rate = fromStore.getDefaultTaxRate();
        		taxJurisdiction = "";
        		
        	}
            }
            else {
              rate = fromStore.getDefaultTaxRate();
              taxJurisdiction = "";
            }  //vishal_k: ends here
        //Added by Anjana to calcualte tax for presale whn sap look up is Y and there is an exception rule defined for ship to zip
 
         // Vishal Yevale : 29 nov 2016 added for TAX CR from Jason
            TaxExceptionRule taxExceptionRule=null;
            if(shippingRequest.getCountry().equalsIgnoreCase("USA") || shippingRequest.getCountry().equalsIgnoreCase("CAN")){
         	   POSLineItem lineItem = taxableLineItemDetails[index].getLineItem();
         	      CMSItem currentItem = (CMSItem)lineItem.getItem();  
         	      String itemProduct=currentItem.getProductNum();
         	      String itemCategory=currentItem.getCategory();
         	   taxExceptionRule =TaxUtilities.getRule(taxExceptionRules , itemProduct , itemCategory, fromStore);
            }else{   // end Vishal yevale 29 nov 2016
             	 taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(shippingRequest.getState().toUpperCase());
            }
            
            if (taxExceptionRule != null) {
              exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemDetails[index], fromStore, taxExceptionRule, rate, taxJurisdiction);
               if (taxableLineItemDetails[index].getLineItem().getExtendedRetailAmount().subtract(taxableLineItemDetails[index].getLineItem().getExtendedReductionAmount()).greaterThan(taxExceptionRule.getThresholdAmount())) {
                belowThresholdTaxAmount = taxExceptionRule.getThresholdAmount().multiply(exceptionTaxRate).round();
                //                netAmount = taxableLineItemDetails[index].getNetAmount().subtract(taxExceptionRule.getThresholdAmount());
                netAmount = (taxableLineItemDetails[index].getLineItem().getExtendedRetailAmount().subtract(taxableLineItemDetails[index].getLineItem().getExtendedReductionAmount())).subtract(taxExceptionRule.getThresholdAmount());
              }
              else {
                rate = exceptionTaxRate;
              }
            }else{
            	 CMSItem cmsItem;
            	 cmsItem = (CMSItem)presaleLineItem.getItem();
            	 //Vishal yevale : passsed Product Num and process date: 9 jan  2017
                 if(shippingRequest.getCountry().equalsIgnoreCase("USA") || shippingRequest.getCountry().equalsIgnoreCase("CAN")){
                	                 	 
                	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE_EXEC :: selectByZipCityStateForSAP ::
             		  taxKey = null;
             		  taxKey = shippingRequest.getState()+shippingRequest.getZipCode()+cmsItem.getCategory()+cmsItem.getProductNum();
             		  
	   	           	 if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	   	           			Object armTaxRateConfigObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	   	           			if(armTaxRateConfigObj == null){
	   	           				armTaxRateConfig = null;
	   	           			
	   	           			}else if(armTaxRateConfigObj instanceof ArmTaxRateConfig){
	   	           				armTaxRateConfig = (ArmTaxRateConfig)armTaxRateConfigObj;
	   	           			
	   	           			}
	   	      			  
	   	      		  } else {
	   	      			  
	   	      			armTaxRateConfig = armTaxRateExcDAO.selectByZipCityStateForSAP(shippingRequest.getState(),shippingRequest.getZipCode(),cmsItem.getCategory(),cmsItem.getProductNum(),(java.sql.Date)aProcessDate);
	                 	 if(taxKey != null){
	   	      			  taxDetailMap.put(taxKey, new Object[] {armTaxRateConfig});
	   	      		  	  }
	   	      		  }
              	 	
                 }else{
                	 
                	 
                	//Mayuri Edhara ::  Getting the key value pair for ARM_TAX_RATE_EXEC :: selectByZipCityStateForSAP ::
            		  taxKey = null;
            		  taxKey = shippingRequest.getState()+shippingRequest.getZipCode()+cmsItem.getCategory();
            		  
	   	           	 if(taxDetailMap.size() > 0 && taxDetailMap.containsKey(taxKey)){
	   	           			Object armTaxRateConfigObj = Arrays.asList(taxDetailMap.get(taxKey)).get(0);
	   	           			if(armTaxRateConfigObj == null){
	   	           				armTaxRateConfig = null;
	   	           			
	   	           			}else if(armTaxRateConfigObj instanceof ArmTaxRateConfig){
	   	           				armTaxRateConfig = (ArmTaxRateConfig)armTaxRateConfigObj;
	   	           			
	   	           			}
	   	      			  
	   	      		  } else {
	   	      			  
	   	      			armTaxRateConfig = armTaxRateExcDAO.selectByZipCityStateForSAP(shippingRequest.getState(),shippingRequest.getZipCode(),cmsItem.getCategory());
	                  	  if(taxKey != null){
	   	      			  taxDetailMap.put(taxKey, new Object[]{armTaxRateConfig});
	   	      		  	  }
	   	      		  } 
                	 
                  	
                 }
              	 if(armTaxRateConfig!=null){
                   	//Poonam: 10 jan, 2017 : added for Handling null threshold amt and null tax_rate from ARM_TAX_RATE_EXC
                   		if(armTaxRateConfig.getAmountThr()!=null && armTaxRateConfig.getTaxRate()!=null){
               		  if ((taxableLineItemDetails[index].getNetAmount().doubleValue()) > ((armTaxRateConfig.getAmountThr()))) {
               			   belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*(armTaxRateConfig.getTaxRate()));
                             if(shippingRequest.getState().equalsIgnoreCase("PR")){
                        		 if(armStateTaxRate!=null)
                              	  belowThresholdTaxAmount = new ArmCurrency((armTaxRateConfig.getAmountThr())*((armTaxRateConfig.getTaxRate()) +  armStateTaxRate.getTaxRate()));
                              	 }
                             netAmount = new ArmCurrency((taxableLineItemDetails[index].getNetAmount().doubleValue()) - (armTaxRateConfig.getAmountThr()));
                           }
                           else {
                         	  rate = armTaxRateConfig.getTaxRate().doubleValue();
                         	  if(armStateTaxRate!=null){
                                	 if(shippingRequest.getState().equalsIgnoreCase("PR")){
                                	  rate = rate + armStateTaxRate.getTaxRate().doubleValue();
                                  }}
                                 taxJurisdiction = armTaxRateConfig.getTaxJur(); 
                           }
               		 
               	 }else{ //default store tax
               		rate = fromStore.getDefaultTaxRate();
            		taxJurisdiction = "";
               	 }//Poonam:ends here
               }}
                
           
            strState = shippingRequest.getState();
          }
          else {
            rate = fromStore.getDefaultTaxRate().doubleValue();
         // Vishal Yevale : 29 nov 2016 added for TAX CR from Jason
            TaxExceptionRule taxExceptionRule=null;
            if(fromStore.getCountry().equalsIgnoreCase("USA") || fromStore.getCountry().equalsIgnoreCase("CAN")){
         	   POSLineItem lineItem = taxableLineItemDetails[index].getLineItem();
         	      CMSItem currentItem = (CMSItem)lineItem.getItem();  
         	      String itemProduct=currentItem.getProductNum();
         	      String itemCategory=currentItem.getCategory();
         	   taxExceptionRule =TaxUtilities.getRule(taxExceptionRules , itemProduct , itemCategory, fromStore);
            }else{   // end Vishal yevale 29 nov 2016
             	 taxExceptionRule = (TaxExceptionRule)taxExceptionRules.get(fromStore.getState().toUpperCase());
            }            if (taxExceptionRule != null) {
              exceptionTaxRate = TaxUtilities.getApplicableTaxRate(taxableLineItemDetails[index], fromStore, taxExceptionRule, rate, taxJurisdiction);
              if (taxableLineItemDetails[index] instanceof CMSPresaleLineItemDetail) {
                if (taxableLineItemDetails[index].getLineItem().getExtendedRetailAmount().subtract(taxableLineItemDetails[index].getLineItem().getExtendedReductionAmount()).greaterThan(taxExceptionRule.getThresholdAmount())) {
                  belowThresholdTaxAmount = taxExceptionRule.getThresholdAmount().multiply(exceptionTaxRate).round();
                  netAmount = taxableLineItemDetails[index].getLineItem().getExtendedRetailAmount().subtract(taxableLineItemDetails[index].getLineItem().getExtendedReductionAmount()).subtract(taxExceptionRule.getThresholdAmount());
                }
                //rajesh
                else {
                  rate = exceptionTaxRate;
                }
              }
              else {
                if (taxableLineItemDetails[index].getNetAmount().greaterThan(taxExceptionRule.getThresholdAmount())) {
                  belowThresholdTaxAmount = taxExceptionRule.getThresholdAmount().multiply(exceptionTaxRate).round();
                  netAmount = taxableLineItemDetails[index].getNetAmount().subtract(taxExceptionRule.getThresholdAmount());
                }
                else {
                  rate = exceptionTaxRate;
                }
              }
             }
            strState = fromStore.getState();
            taxJurisdiction = null;
          }
          taxAmount = netAmount.multiply(rate).round();
          saleTaxDetails[index] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_STORE_RATE, taxAmount.add(belowThresholdTaxAmount), rate, strState, taxJurisdiction , new ArmCurrency(stateTaxAmt), taxMap, taxExcMap, armTaxCARateConfig,taxDetailMap);
        }
      }
      //        return new SaleTax(TAX_ENGINE_NAME, (CMSSaleTaxDetail[])saleTaxList.toArray(new CMSSaleTaxDetail[0]), new SaleTaxDetail[0]);
      return  new SaleTax(TAX_ENGINE_NAME, saleTaxDetails, new SaleTaxDetail[0]);
    } catch (Exception ex) {
      System.out.println("Exception in interstateStoreTaxServices.getTaxAmounts " + ex.getMessage());
      ex.printStackTrace();
      return  null;
    } finally{
    	 taxKey = null;
    }
  }
  
  public Double getCAExctax(ArmTaxRateConfig[] armTaxRateConfig){
	  Double pstTax = 0.0;
	  Double gstTax = 0.0;
	  Double qstTax = 0.0;
	  Double hstTax = 0.0;
	  Double rate = 0.0;
	 String taxType = "";
	 
	  for(int i = 0 ; i<armTaxRateConfig.length;i++){
		  taxType = armTaxRateConfig[i].getTaxType();  
	  

 	  if(taxType.equalsIgnoreCase("PST")){
 		  pstTax = armTaxRateConfig[i].getTaxRate();
 	  }
 	  if(taxType.equalsIgnoreCase("QST")){
 		  qstTax = armTaxRateConfig[i].getTaxRate();
 	  }
 	  if(taxType.contains("HST")){
 		  hstTax = armTaxRateConfig[i].getTaxRate(); 
 	  }
 	  if(taxType.equalsIgnoreCase("GST")){
 		  gstTax = armTaxRateConfig[i].getTaxRate();  
 	  }
 	 rate = pstTax + qstTax + hstTax + gstTax;
  
	  }
 	  return rate;
  }
  public Double getCAtax(ArmTaxRate[] armTaxRate){ 
	  Double pstTax = 0.0;
	  Double gstTax = 0.0;
	  Double qstTax = 0.0;
	  Double hstTax = 0.0;
	  Double rate = 0.0;
	  String taxType = "" ;
	  for(int i = 0 ; i<armTaxRate.length;i++){
		  taxType =  armTaxRate[i].getTaxType();  
	  
	  if(taxType.equalsIgnoreCase("PST")){
		  pstTax = armTaxRate[i].getTaxRate();
	  }
	  if(taxType.equalsIgnoreCase("QST")){
		  qstTax = armTaxRate[i].getTaxRate();
	  }
	  if(taxType.contains("HST")){
		  hstTax = armTaxRate[i].getTaxRate(); 
	  }
	  if(taxType.equalsIgnoreCase("GST")){
		  gstTax = armTaxRate[i].getTaxRate();  
	  }
	  rate = pstTax + qstTax + hstTax + gstTax;
	  
	  }
	  return rate;
  }
  
  public Map getExctaxMap(ArmTaxRateConfig[] armTaxRateConfig){
	  Double pstTax = 0.0;
	  Double gstTax = 0.0;
	  Double qstTax = 0.0;
	  Double hstTax = 0.0;
	  Double rate = 0.0;
	 String taxType = "";
	 Map taxMap = new HashMap() ;
	 if(armTaxRateConfig!=null){
	  for(int i = 0 ; i<armTaxRateConfig.length;i++){
		  taxType = armTaxRateConfig[i].getTaxType();  
	  

 	  if(taxType.equalsIgnoreCase("PST")){
 		  pstTax = armTaxRateConfig[i].getTaxRate();
 		 taxMap.put("PST", pstTax);
 	  }
 	  if(taxType.equalsIgnoreCase("QST")){
 		  qstTax = armTaxRateConfig[i].getTaxRate();
 		 taxMap.put("QST", qstTax);
 	 	 
 	  }
 	  if(taxType.contains("HST")){
 		  hstTax = armTaxRateConfig[i].getTaxRate(); 
 		  taxMap.put("HST", hstTax);
 	  }
 	  if(taxType.equalsIgnoreCase("GST")){
 		  gstTax = armTaxRateConfig[i].getTaxRate();  
 		 taxMap.put("GST", gstTax);
 	  }
 	 rate = pstTax + qstTax + hstTax + gstTax;
  
	  }
	 }
 	  return taxMap;
  }
  
  
  public Map gettaxMap(ArmTaxRate[] armTaxRate){
	  Double pstTax = 0.0;
	  Double gstTax = 0.0;
	  Double qstTax = 0.0;
	  Double hstTax = 0.0;
	  Double rate = 0.0;
	 String taxType = "";
	 Map taxMap = new HashMap() ;
	if(armTaxRate!=null){
	  
	  for(int i = 0 ; i<armTaxRate.length;i++){
		  taxType =  armTaxRate[i].getTaxType();  
	  
	  if(taxType.equalsIgnoreCase("PST")){
		  pstTax = armTaxRate[i].getTaxRate();
			 taxMap.put("PST", pstTax);
	  }
	  if(taxType.equalsIgnoreCase("QST")){
		  qstTax = armTaxRate[i].getTaxRate();
		  taxMap.put("QST", qstTax);
	  }
	  if(taxType.contains("HST")){
		  hstTax = armTaxRate[i].getTaxRate(); 
		  taxMap.put("HST", hstTax);
	  }
	  if(taxType.equalsIgnoreCase("GST")){
		  gstTax = armTaxRate[i].getTaxRate(); 
		  taxMap.put("GST", gstTax);
	  }
	  rate = pstTax + qstTax + hstTax + gstTax;
	  
	  }}
 	  return taxMap;
  }
  
  public Double getExcThresAmt(ArmTaxRateConfig armTaxRateConfig){
	  //Test
	  if(armTaxRateConfig!=null && armTaxRateConfig.getAmountThr()!=null)//Ends
	  {
		  return armTaxRateConfig.getAmountThr();
	  }
	  return 0.0d;
  }

@Override
public SaleTax getTaxAmounts (CompositePOSTransaction aCompositeTransaction, Store fromStore, Store toStore, Date process_dt) throws Exception {
    try {
    return getTaxAmounts (aCompositeTransaction, fromStore, toStore, process_dt, null);

    } catch (Exception ex) {
      System.out.println("Exception in interstateStoreTaxServices.getTaxAmounts " + ex.getMessage());
      ex.printStackTrace();
      return  null;
    }
  }
}



