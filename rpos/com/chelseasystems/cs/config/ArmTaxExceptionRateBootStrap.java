package com.chelseasystems.cs.config;

import java.awt.*;
import java.util.*;
import java.io.*;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cs.download.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.config.ArmTaxRateConfig;


public class ArmTaxExceptionRateBootStrap implements IBootStrap {

	  /**
	   * BootstrapManager
	   */
	  private BootStrapManager bootMgr;
	  /**
	   * BrowserManager
	   */
	  private IBrowserManager theMgr;

	  /**
	   * put your documentation comment here
	   */
	  public ArmTaxExceptionRateBootStrap() {
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getName() {
	    return "Armani Tax Exception Rate BootStrap";
	  }

	  /**
	   * returns the long description of the bootstrap
	   * @return String the long description of the bootstrap
	   */
	  public String getDesc() {
	    return "This bootstrap determines if the tax configuration file needs to be downloaded.";
	  }

	  /**
	   * put your documentation comment here
	   * @param theMgr
	   * @param parentFrame
	   * @param bootMgr
	   * @return
	   */
	  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
		  ConfigMgr config = new ConfigMgr("item.cfg");
		  String sapLookup = config.getString("SAP_LOOKUP");
		  if(sapLookup!=null && sapLookup.equalsIgnoreCase("Y"))
		  {
	    try {
	      this.bootMgr = bootMgr;
	      this.theMgr = theMgr;
	      // check to make sure that ArmaniCommon.cfg exists and its not in the backup
	      File taxConfig = new File(FileMgr.getLocalFile("config", "taxExceptionRate.cfg"));
	      if (!taxConfig.exists()) {
	        File fileBackup = new File(FileMgr.getLocalFile("config", "taxExceptionRate.bkup"));
	        if (fileBackup.exists()) {
	          fileBackup.renameTo(taxConfig);
	        } else {
	          File fileDate = new File(FileMgr.getLocalFile("repository", "TAX_CONFIG_DOWNLOAD_DATE"));
	          fileDate.delete();
	        }
	      }
	      if (!theMgr.isOnLine()) {
	        return new BootStrapInfo(this.getClass().getName());
	      }
	      //Anjana Removed the repeated line to fix the Services text
	      Date date = (Date)theMgr.getGlobalObject("TAX_CONFIG_DOWNLOAD_DATE");
	      /*if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)
	          // || (!armCustomConfig.exists())  MSB - 09/08/05
	          // Needed for offline scenario.
	          ) {
	        downloadFile();
	      }*/
	      downloadFile();
	    } catch (Exception ex) {
	      System.out.println("Exception ArmTaxExceptionRateBootStrap.start()->" + ex);
	      ex.printStackTrace();
	      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
	          , "See Exception", LoggingServices.MAJOR, ex);
	    }
		  }
	    return new BootStrapInfo(this.getClass().getName());
	  }

	  /**
	   * Download ArmaniCommon.cfg on Client
	   * @throws Exception
	   */
	  private void downloadFile()
	      throws Exception {
	    try {
	      Vector rulesVec = new Vector();
	      File backup = new File(FileMgr.getLocalFile("config", "taxExceptionRate.bkup"));
	      backup.delete();
	      File discountRulesConfig = new File(FileMgr.getLocalFile("config", "taxExceptionRate.cfg"));
	      discountRulesConfig.renameTo(backup);
	      discountRulesConfig.delete();
	      FileWriter fileWriter = new FileWriter("..//files//prod//config//taxExceptionRate.cfg");
	      BufferedWriter buffWriter = new BufferedWriter(fileWriter);
	      ArmTaxRateConfig obj = new ArmTaxRateConfig();
	      File armCustomCFGFile = new File(FileMgr.getLocalFile("config", "taxExceptionRate.cfg"));
	      HashMap <String,String> confDtl = new HashMap();
	      
	      String state="";
		  Double amountThr =0.0d;
		  String zipCode="";
		  String taxJur="";
		  Double taxRate = 0.0d;
		  String thrRule="";
		  Date effectiveDt;
		  Double idRate;
		  String nation="";
		  String brand="";
		  String category="";
		  String product="";
		  Date datains;
		  Date dataMod;
		  String taxType;
		  Double taxPST = null;
		  Double taxGST = null;
		  Double taxQST = null;
		  Double taxHST = null;
		  Double CAtaxRate= 0.0d;
		  
	      String label ="";
	      String key = "";
	      String rule="";
	      ConfigMgr config = new ConfigMgr("armaniDownload.cfg");
	      ArmaniDownloadServices armDownloadServices = (ArmaniDownloadServices)config.getObject(
	          "CLIENT_IMPL");
	      bootMgr.setBootStrapStatus("Downloading Armani Configurations file");
	      CMSStore store = (CMSStore)theMgr.getGlobalObject("STORE");
	      ArmTaxRateConfig[] armTaxRateConfig = armDownloadServices.getExceptionTaxDetailByStateAndZipcode(store.getState(), store.getZipCode());
	      if (armTaxRateConfig != null && armTaxRateConfig.length>0) {
	    		
	    	  //Poonam: Added for expiration date issue on Nov 15,2016 
	    	       Date process_date = (Date)theMgr.getGlobalObject("PROCESS_DATE");
	    	       Date expirationDt = null; 
	    	       for (int i = 0; i < armTaxRateConfig.length; i++) {
	    	    	   expirationDt =armTaxRateConfig[i].getExpirationDt();
	    	        if((expirationDt != null)){
	    	            if(expirationDt.after(process_date) || (expirationDt.compareTo(process_date) == 0)) {
	    	            	rulesVec.add(armTaxRateConfig[i]);
	    	            }
	    	        }else{
	    	        	//Poonam : 2 jan 2017 : considered null date as valid record.
    	            	rulesVec.add(armTaxRateConfig[i]);
	    	        }
	    	        }
	    	      
	    	       armTaxRateConfig= new ArmTaxRateConfig[rulesVec.size()];
	    	       for (int i = 0; i < rulesVec.size(); i++) {
	    	        armTaxRateConfig[i] = (ArmTaxRateConfig)rulesVec.get(i);
	    	       }	    	       
	    	    //end poonam : added code to add valid tax rate details in vector
	    	  }
	      
	    	 //vishal yevale : 28 Nov 2016 Tax CR from JSON : logic to write file ( considering threshold amnt, rule , taxRate)
	    	 	    	 ArrayList<Set<ArmTaxRateConfig>> setList=null;
	    	    Set<ArmTaxRateConfig> set =null;
	    	    Set<ArmTaxRateConfig> setAll=null;
	    	 if(store.getCountry().equalsIgnoreCase("USA")){
	    		 setList=new ArrayList();
	    		 set =new HashSet();
	    		 setAll=new HashSet();
	    		 amountThr=null;
	    		 taxRate=null;
	    		 thrRule=null;
	    		 for(int i=0;i<armTaxRateConfig.length;i++){
		    	        
	    			 ArmTaxRateConfig armConfig=armTaxRateConfig[i];
	    				if(amountThr==null && taxRate==null && thrRule==null && !(setAll.contains(armTaxRateConfig[i]))){
	    					taxRate=armTaxRateConfig[i].getTaxRate();
	    		    		amountThr=armTaxRateConfig[i].getAmountThr();	
	    		    		thrRule = armTaxRateConfig[i].getThrRule();
	    		    		if(amountThr==null || taxRate==null || thrRule==null){
		    		    		setAll.add(armTaxRateConfig[i]);
	    		    		}
	    		    	}
	    				if( amountThr!=null && taxRate!=null && thrRule!=null && armTaxRateConfig[i].getThrRule()!=null && armTaxRateConfig[i].getTaxRate()!=null && armTaxRateConfig[i].getAmountThr()!=null && 0==armTaxRateConfig[i].getTaxRate().compareTo(taxRate) && 0==armTaxRateConfig[i].getAmountThr().compareTo(amountThr) &&  0==armTaxRateConfig[i].getThrRule().compareTo(thrRule)){
	    					set.add(armTaxRateConfig[i]);
	    		    		setAll.add(armTaxRateConfig[i]);
	    		    	}
	    				if(i==armTaxRateConfig.length-1){
	    					if(set.size()>=1){
	    					setList.add(set);
	    					}
	    					set=null;
	    					set=new HashSet();
	    		    		taxRate=null;
	    		    		amountThr=null;
	    		    		thrRule=null;
	    		    		if(setAll.size()!=armTaxRateConfig.length){
	    		    			i=0;
	    		    		}
	    		    		else{
	    		    			setAll=null;
	    		    		}
	    		    	}
	    		 }
	    	 }else if(store.getCountry().equalsIgnoreCase("CAN")){
	    		 //Vishal Yevale : logic to write file for Canada ( considered threshold amount, rule) : 11 jan 2017
	    		 setList=new ArrayList();
	    		 set =new HashSet();
	    		 setAll=new HashSet();
	    		 amountThr=null;
	    		 thrRule=null;
	   	  for (int i = 0; i < armTaxRateConfig.length; i++) {
 	        
 			 ArmTaxRateConfig armConfig=armTaxRateConfig[i];
 				if(amountThr==null && thrRule==null && !(setAll.contains(armTaxRateConfig[i]))){
 		    		amountThr=armTaxRateConfig[i].getAmountThr();	
 		    		thrRule = armTaxRateConfig[i].getThrRule();
 		    		if(amountThr==null || thrRule==null){
	    		    		setAll.add(armTaxRateConfig[i]);
 		    		}
 		    	}
 				if( amountThr!=null && thrRule!=null && armTaxRateConfig[i].getThrRule()!=null && armTaxRateConfig[i].getAmountThr()!=null && 0==armTaxRateConfig[i].getAmountThr().compareTo(amountThr) &&  0==armTaxRateConfig[i].getThrRule().compareTo(thrRule)){
 					set.add(armTaxRateConfig[i]);
 		    		setAll.add(armTaxRateConfig[i]);
 		    	}
 				if(i==armTaxRateConfig.length-1){
 					if(set.size()>=1){
 					setList.add(set);
 					}
 					set=null;
 					set=new HashSet();
 		    		amountThr=null;
 		    		thrRule=null;
 		    		if(setAll.size()!=armTaxRateConfig.length){
 		    			i=0;
 		    		}
 		    		else{
 		    			setAll=null;
 		    		}
 		    	}}		  
	 
	   	  }
	    	 if(store.getCountry().equalsIgnoreCase("USA")){
	    		 Iterator<Set<ArmTaxRateConfig>> itr=setList.iterator();  
	    		 int count=1;
	    		 String nu="";
	  		   while(itr.hasNext()){
	  		       state="";
	  			   amountThr =0.0d;
	  			   zipCode="";
	  			   taxJur="";
	  			   taxRate = 0.0d;
	  			   thrRule="";
	  			   nation="";
	  			   brand="";
	  			   category="";
	  			   product="";
	  			  label ="";
	  		       key = "";
	  		       rule="";
	    		 set=null;
	    		 set=itr.next();
	    		 
	    		 for(ArmTaxRateConfig taxConfig:set){
	   	   		//zipCode=obj.getZipCode();
	   	   		if(!(taxConfig.getZipCode()==null) && !(zipCode.equals("")))
	   	   			zipCode+=","+taxConfig.getZipCode();
	   			else if(!(taxConfig.getZipCode()==null) && zipCode.equals(""))
	   				zipCode+=taxConfig.getZipCode();
	   			else{}
	   	   		if(state.equals(""))
	   	   		state = taxConfig.getState();
	   	   		else if(state.contains(taxConfig.getState()))
	   	   		{}
	   	   		else if(!(state.contains(taxConfig.getState())))
	   	   		state = ","+taxConfig.getState();	
	   			amountThr = taxConfig.getAmountThr();

	   			if(!(taxConfig.getTaxJur()==null) && !(taxJur.equals("")))
	   				taxJur+=","+taxConfig.getTaxJur();
	   			else if(!(taxConfig.getTaxJur()==null) && taxJur.equals(""))
	   				taxJur+=taxConfig.getTaxJur();
	   			taxRate = taxConfig.getTaxRate();	   	
	   			
	   			thrRule = taxConfig.getThrRule();
	   			effectiveDt = taxConfig.getEffectiveDt();
	   			idRate = taxConfig.getIdRate();
	   			nation = taxConfig.getNation();
	   			//brand = obj.getBrand();
	   			if(!(taxConfig.getBrand()==null) && !(brand.equals("")))
	   				brand+=","+taxConfig.getBrand();
	   			else if(!(taxConfig.getBrand()==null) && brand.equals(""))
	   				brand+=taxConfig.getBrand();
	   			else{}
	   			//category = obj.getCategory();
	   			if(!(taxConfig.getCategory()==null) && !(category.equals("")))
	   			    category+=","+taxConfig.getCategory();
	   			else if(!(taxConfig.getCategory()==null) && category.equals(""))
	   			    category+=taxConfig.getCategory();
	   			else{}
	   			//product = obj.getProduct();
	   			if(!(taxConfig.getProduct()==null) && !(product.equals("")))
	   				product+=","+taxConfig.getProduct();
	   			else if(!(taxConfig.getProduct()==null) && product.equals(""))
	   				product+=taxConfig.getProduct();
	   			else{}
	   			datains = taxConfig.getDatains();
	   			dataMod = taxConfig.getDataMod();
	   	   		state=taxConfig.getState();
	   	   		taxRate=taxConfig.getTaxRate();
	   	   		effectiveDt=taxConfig.getEffectiveDt();
	   	   		//For PR  add the state tax along with current calculation
	   	   	 if(store.getState().equalsIgnoreCase("PR")){
	   	   		 if(store.getStateTax()!=null)
	   			 taxRate = taxRate +  store.getStateTax();
	   		 }   	   	  
	    		 }
	    		 
	    		 label = "EXCEPTION_STATE=" + state;
	    		  
	    		  writeRuleToConfigFile(buffWriter, label);
	    		  
	    		  
	    	 rule = nu+state + ".THRESHOLD_AMT=" + amountThr;
	    	 writeRuleToConfigFile(buffWriter, rule);
	    	 rule = nu+state + ".EXCEPTION_ZIPCODES=" + zipCode;
	    	  writeRuleToConfigFile(buffWriter, rule);
	    	  rule = nu+state + ".EXCEPTION_TAX_JUR=" + taxJur;
	    	  writeRuleToConfigFile(buffWriter, rule);	    	  
	    	  rule = nu+state + ".EXCEPTION_TAX_RATE=" + taxRate;
	    	 writeRuleToConfigFile(buffWriter, rule);	    	  	     	
	   		  rule = nu+state + ".EXCEPTION_APPLIED_ITEM_CATEGORY=" + category;
	   		  writeRuleToConfigFile(buffWriter, rule);
	   		  rule = nu+state + ".EXCEPTION_APPLIED_ITEM_PRODUCT=" + product;
	   		  writeRuleToConfigFile(buffWriter, rule);
	   		  rule = nu+state + ".EXCEPTION_APPLIED_ITEM_BRAND=" + brand;
	   		  writeRuleToConfigFile(buffWriter, rule);
	   		  rule = nu+state + ".ABOVE_THRESHOLD_RULE=" + thrRule;
	   		  writeRuleToConfigFile(buffWriter, rule);
			  buffWriter.newLine();
			  buffWriter.newLine();
			  nu=String.valueOf(count);
			  count++;
	  		   }
	  		   rule = "TOTAL_RULE="+String.valueOf(--count);
		   		  writeRuleToConfigFile(buffWriter, rule);

	  		 buffWriter.flush();
	     	  buffWriter.close();
	     	  fileWriter.close();
	   	      theMgr.addGlobalObject("TAX_CONFIG_DOWNLOAD_DATE", new java.util.Date(), true);
	    	 }
	    	//end vishal 28 Nov 2016 tax CR from Jason 
	    	 else if(store.getCountry().equalsIgnoreCase("CAN")){
	    		 
	    		 Iterator<Set<ArmTaxRateConfig>> itr=setList.iterator();  
	    		 int count=1;
	    		 String nu="";
	    		  while(itr.hasNext()){
	    		       state="";
	    			   amountThr =0.0d;
	    			   zipCode="";
	    			   taxJur="";
	    			   taxRate = 0.0d;
	    			   thrRule="";
	    			   nation="";
	    			   brand="";
	    			   category="";
	    			   product="";
	    			   taxPST = null;
	    			   taxGST = null;
	    			   taxQST = null;
	    			   taxHST = null;
	    			   CAtaxRate= 0.0d;
	    			  
	    		       label ="";
	    		       key = "";
	    		       rule="";
		    		 set=null;
		    		 set=itr.next();
		    	 for(ArmTaxRateConfig taxConfig:set){	   		
		    		 if(!(taxConfig.getZipCode()==null) && !(zipCode.equals("")))
			   			zipCode+=","+taxConfig.getZipCode();
		 			else if(!(taxConfig.getZipCode()==null) && zipCode.equals(""))
		 				zipCode+=taxConfig.getZipCode();
		 			else{}
		 	   		if(state.equals(""))
		 	   		state = taxConfig.getState();
		 	   		else if(state.contains(taxConfig.getState()))
		 	   		{}
		 	   		else if(!(state.contains(taxConfig.getState())))
		 	   		state = ","+taxConfig.getState();	
		 			amountThr = taxConfig.getAmountThr();
		 		//	taxJur = obj.getTaxJur();
		 			if(!(taxConfig.getTaxJur()==null) && !(taxJur.equals("")))
		 				taxJur+=","+taxConfig.getTaxJur();
		 			else if(!(taxConfig.getTaxJur()==null) && taxJur.equals(""))
		 				taxJur+=taxConfig.getTaxJur();
		 			taxRate = taxConfig.getTaxRate();
		 			
		 				taxType = taxConfig.getTaxType();
		 				if(taxPST == null && taxType.equalsIgnoreCase("PST")){
		 					taxPST = taxRate;
		 				}
		 				if(taxGST == null && taxType.equalsIgnoreCase("GST")){
		 					taxGST = taxRate;
		 				}
		 				if(taxQST == null && taxType.equalsIgnoreCase("QST")){
		 					taxQST = taxRate;
		 				}
		 				if(taxHST == null && taxType.contains("HST")){
		 					taxHST = taxRate;
		 				}
		 			
		 			thrRule = taxConfig.getThrRule();
		 			effectiveDt = taxConfig.getEffectiveDt();
		 			idRate = taxConfig.getIdRate();
		 			nation = taxConfig.getNation();
		 			//brand = obj.getBrand();
		 			if(!(taxConfig.getBrand()==null) && !(brand.equals("")))
		 				brand+=","+taxConfig.getBrand();
		 			else if(!(taxConfig.getBrand()==null) && brand.equals(""))
		 				brand+=taxConfig.getBrand();
		 			else{}
		 			//category = obj.getCategory();
		 			if(!(taxConfig.getCategory()==null) && !(category.equals("")))
		 			    category+=","+taxConfig.getCategory();
		 			else if(!(taxConfig.getCategory()==null) && category.equals(""))
		 			    category+=taxConfig.getCategory();
		 			else{}
		 			//product = obj.getProduct();
		 			if(!(taxConfig.getProduct()==null) && !(product.equals("")))
		 				product+=","+taxConfig.getProduct();
		 			else if(!(taxConfig.getProduct()==null) && product.equals(""))
		 				product+=taxConfig.getProduct();
		 			else{}
		 			datains = taxConfig.getDatains();
		 			dataMod = taxConfig.getDataMod();
		 	   		state=taxConfig.getState();
		 	   		taxRate=taxConfig.getTaxRate();
		 	   		effectiveDt=taxConfig.getEffectiveDt();
		    	 }
		    	 
		    		label = "EXCEPTION_STATE=" + state;
		   		  writeRuleToConfigFile(buffWriter, label);
		   		  rule = nu + state + ".THRESHOLD_AMT=" + amountThr;
		  		 writeRuleToConfigFile(buffWriter, rule);
		  		 rule = nu + state + ".EXCEPTION_ZIPCODES=" + zipCode;
		  	  writeRuleToConfigFile(buffWriter, rule);
		  	  rule = nu + state + ".EXCEPTION_TAX_JUR=" + taxJur;
		  	  writeRuleToConfigFile(buffWriter, rule);
	 		  if(taxPST!=null){
	  			 CAtaxRate =   CAtaxRate + taxPST;
	  			 rule = nu + state + ".EXCEPTION_TAX_RATE_PST=" + taxPST;
	  			writeRuleToConfigFile(buffWriter, rule);
	  		}
	  		 if(taxGST!=null){
	  			 
	  			CAtaxRate =   CAtaxRate + taxGST;
	  			rule = nu + state + ".EXCEPTION_TAX_RATE_GST=" + taxGST;
	  			writeRuleToConfigFile(buffWriter, rule);
	  	 	 }
	  		 if(taxQST!=null){
	  			CAtaxRate =   CAtaxRate + taxQST;
	  			rule = nu + state + ".EXCEPTION_TAX_RATE_QST=" + taxQST;
	  			writeRuleToConfigFile(buffWriter, rule);
	  	 	 }
	  		 if(taxHST!=null){
	  			CAtaxRate =   CAtaxRate + taxHST;
	  			rule = nu + state + ".EXCEPTION_TAX_RATE_HST=" + taxHST;
	  			writeRuleToConfigFile(buffWriter, rule);
	  	 	 }
	  		 
	  		  rule = nu + state + ".EXCEPTION_TAX_RATE=" + CAtaxRate;
	  		 writeRuleToConfigFile(buffWriter, rule);
	  	   	
	 		  rule = nu + state + ".EXCEPTION_APPLIED_ITEM_CATEGORY=" + category;
	 		  writeRuleToConfigFile(buffWriter, rule);
	 		  rule = nu + state + ".EXCEPTION_APPLIED_ITEM_PRODUCT=" + product;
	 		  writeRuleToConfigFile(buffWriter, rule);
	 		  rule = nu + state + ".EXCEPTION_APPLIED_ITEM_BRAND=" + brand;
	 		  writeRuleToConfigFile(buffWriter, rule);
	 		  rule = nu + state + ".ABOVE_THRESHOLD_RULE=" + thrRule;
	 		  writeRuleToConfigFile(buffWriter, rule);
	 		  buffWriter.newLine();
			  buffWriter.newLine();
			  nu=String.valueOf(count);
			  count++;
		 	   	  }
		  		   rule = "TOTAL_RULE="+String.valueOf(--count);
			   		  writeRuleToConfigFile(buffWriter, rule);

		  		 buffWriter.flush();
		     	  buffWriter.close();
		     	  fileWriter.close();
		   	      theMgr.addGlobalObject("TAX_CONFIG_DOWNLOAD_DATE", new java.util.Date(), true);
		   	      }
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      try {
	        File armaniCommon = new File(FileMgr.getLocalFile("config", "taxExceptionRate.cfg"));
	        armaniCommon.delete();
	        File backup = new File(FileMgr.getLocalFile("config", "taxExceptionRate.bkup"));
	        backup.renameTo(armaniCommon);
	      } catch (Exception ex1) {}
	      System.out.println("Exception downloadFile()->" + ex);
	      ex.printStackTrace();
	    } finally {
	      if (theMgr instanceof IApplicationManager) {
	        ((IApplicationManager)theMgr).closeStatusDlg();
	      }
	    }
	    
	    
	  }
	  
	  private void writeRuleToConfigFile(BufferedWriter buffWriter, String sTaxExceptionRule) 
		  		throws Exception {
			  buffWriter.write(sTaxExceptionRule);
			  buffWriter.newLine();
		  }
		  
}
