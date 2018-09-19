/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.rules.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerAlertRule;
import com.chelseasystems.cs.customer.CMSCustomerFileServices;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cs.customer.CMSCustomerJDBCServices;
import com.chelseasystems.cs.customer.CustomerSaleSummary;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.Version;

/**
 * <p>Title: ValidateEmployeeRuleForCurrentTransaction</p>
 * 
 * <p>Description: This business rule validates employee rule</p>
 * 
 * <p>Copyright: Copyright (c) 2005</p>
 * 
 * <p>Company:SkillNet Inc. </p>
 * 
 * @author Sandhya Ajit
 * @version 1.0
 */
/* History:-
 +--------+------------+-----------+------------------+-------------------------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description                        |
 +--------+------------+-----------+------------------+-------------------------------------------------+
 | 1      | 11-03-2006 | Sandhya   |        NA        | Rule to alert the employee about the sales limit|                             |
 +--------+------------+-----------+------------------+-------------------------------------------------+
 */
public class ValidateEmployeeRuleForCurrentTransaction extends Rule {

	public static final long serialVersionUID = 0;
	public static String TOT_RECORD_TYPE = "TOT";
	public static String PRO_RECORD_TYPE = "PRO";
	String dsc_percent = null;
	 ArmCurrency netVal= new ArmCurrency(0);
	// PCR from US House_account added to rules
	// public static String HOUSE_ACC_RECORD_TYPE = "HOUSE_ACCOUNT";

	private static boolean isSocietyCodeSearchEnabled = false;
	private static boolean isHKenv = false;
	private String SOCIETY_CODE = "";

	static {
		ConfigMgr custCfg = new ConfigMgr("customer.cfg");
		ConfigMgr armCfg = new ConfigMgr("arm.cfg");
		if((custCfg.getString("USE_SOCIETY_CODE_FOR_EMP_ALERT_RULE") != null)	&& ("TRUE".equalsIgnoreCase(custCfg.getString("USE_SOCIETY_CODE_FOR_EMP_ALERT_RULE").trim()))){
			isSocietyCodeSearchEnabled = true;
		}
		if((armCfg.getString("HK_ENV") != null)	&& ("TRUE".equalsIgnoreCase(armCfg.getString("HK_ENV").trim()))){
			isHKenv = true;
		}
	}

	/**
	 * Default Constructor
	 */
	public ValidateEmployeeRuleForCurrentTransaction() {
	}

	/**
	 * Execute the rule.
	 * 
	 * @param object     theParent - instance of CMSCompositePOSTransaction
	 * @return RulesInfo
	 */
	public RulesInfo execute(Object theParent, Object[] args) {
		return execute((CMSCompositePOSTransaction) theParent,	(CMSCustomer) args[0]);
	}

	/**
	 * Execute the Rule
	 * @param CMSCompositePOSTransaction  theParent
	 * @return RulesInfo
	 */
	private RulesInfo execute(CMSCompositePOSTransaction compTxn,CMSCustomer customer) {
		// CMSCustomer customer = null;
		ArmCurrency currentNetAmount = new ArmCurrency(0.0d);
		ArmCurrency ytdNetAmount = new ArmCurrency(0.0d);
		ArmCurrency netAmount = new ArmCurrency(0.0d);
		ArmCurrency thresholdNetAmount = new ArmCurrency(0.0d);
		Map hCurrentQty = new HashMap();
		Map ytdQtyByProductCD = new HashMap();
		Map hQuantity = new HashMap();
		List dataFromCurrentTxn = new ArrayList();
		String productCode = "";
		String message = "";
		String countryCode = "";
		CMSCustomerAlertRule[] rules=null;
		Map dataByEmpRule;
		
		Integer qtyFromTxn;
		Integer qtyFromCust;
		Integer qty = new Integer(0);
		Integer thresholdQty = new Integer(0);

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("######## CUSTOMER ALERT RULE ######");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		try {
			// Get data from V_ARM_CUST_RULE_DATA and set in the customer object
			String customerId = customer.getId();
			customer.getAccountNum();
			countryCode = (compTxn.getStore()).getPreferredISOCountry();
			CMSStore store = (CMSStore) compTxn.getStore();
			String brand_ID = store.getBrandID();			
			int quantity = compTxn.getCompositeTotalQuantityOfItems();
			int qun = 0;
			POSLineItem[] posline = compTxn.getLineItemsArray();
			// saptarshi-to get the transaction type
			String transactionType=compTxn.getTransactionType();
			for(int i =0;i<posline.length;i++){
			qun = posline[i].getQuantity().intValue();
			}
			if (isSocietyCodeSearchEnabled) {
				SOCIETY_CODE = ((CMSStore) (compTxn.getStore())).getCompanyCode();
			}
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			//This is created by Vivek Sawant on 24 Nov 08
			rules = getCustomerAlertRules(countryCode, brand_ID);
			if(rules == null || rules.length < 0){}
			else{
				for (int i = 0; i < rules.length; i++) {
					if (rules[i] != null) {
						if (rules[i].getIdBrand() != null && (rules[i].getIdBrand().equals(brand_ID) || rules[i].getIdBrand().equalsIgnoreCase("ALL"))) {
							dsc_percent = rules[i].getDsc_level();
						}
					}
				 }
			}
			
		
			}
			//This is method is customised by Vivek
			// here purchase history will  be the same for all discount
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				// method Updated : Vishal Yevale : PCR for Employee budget threshold for multi level discount 29th May 2017 (EUROPE)
				dataByEmpRule = CMSCustomerHelper.getTransactionDataByEmpRule(CMSApplet.theAppMgr, customerId, countryCode, SOCIETY_CODE, rules, brand_ID);				
			}else{
				 dataByEmpRule = CMSCustomerHelper.getTransactionDataByEmpRule(CMSApplet.theAppMgr, customerId, countryCode, SOCIETY_CODE);
			}
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				if(dsc_percent == null){}
				else{
					if (dsc_percent!=null || !dsc_percent.equals("0")){
						customer.setYtdNetAmount((Map) dataByEmpRule.get(TOT_RECORD_TYPE));
					customer.setYtdQtyByProductCD((Map) dataByEmpRule.get(PRO_RECORD_TYPE));
					if(dataByEmpRule.get(TOT_RECORD_TYPE)!=null){	
						 Map netamt=  (Map) dataByEmpRule.get(TOT_RECORD_TYPE);						
				   		 Set innerMapKeys = netamt.keySet();
						 Iterator ittr = innerMapKeys.iterator();
						 ArmCurrency strValue =  new ArmCurrency(0);
						 netVal= new ArmCurrency(0);
						 Float f ;
					     Map test = new HashMap();
					     while(ittr.hasNext()){
							 Object intkey = ((Integer)ittr.next());
							 strValue = (ArmCurrency)netamt.get(intkey);
						          if(strValue!=null){
								 netVal = netVal.add(strValue);								
				         		}
					      }
					   	test.put("NET_AMT", netVal);
						customer.setYtdNetAmount(netamt);
					}
					else
					{
						Map nullMap = new HashMap();
						nullMap.put("NET_AMT" , new ArmCurrency(0));
					       customer.setYtdNetAmount(nullMap);
					}
					
				}
				}
		
			}else{
				if (dataByEmpRule != null && dataByEmpRule.size() > 0)  {
					customer.setYtdNetAmount((Map) dataByEmpRule.get(TOT_RECORD_TYPE));
					customer.setYtdQtyByProductCD((Map) dataByEmpRule.get(PRO_RECORD_TYPE));
				}
		}
			// Get data from current transaction
			dataFromCurrentTxn = getTransactionDataFromCurrentTxn(compTxn);
			if (dataFromCurrentTxn != null && dataFromCurrentTxn.size() > 0) {
				currentNetAmount = (ArmCurrency) dataFromCurrentTxn.get(0);
				hCurrentQty = (HashMap) dataFromCurrentTxn.get(1);
				}

			ytdQtyByProductCD = customer.getYtdQtyByProductCD();

			// Get the rules null check added for issue #1915 US
		
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				rules = getCustomerAlertRules(countryCode, brand_ID); 
		
			}else{
				rules = getCustomerAlertRules(countryCode);
			}
		if (rules == null || rules.length == 0) {
				return new RulesInfo();
			}
			if (rules != null && rules.length > 0) {
				
				//saptarshi-method returning total net amount in case of tot record type
				ArmCurrency totalNetAmount=getNetAmount(rules,posline,netVal);
				ArmCurrency zeroValue = new ArmCurrency("0.00") ;
				
				for (int i = 0; i < rules.length; i++) {
					if (rules[i] != null) {
						if (rules[i].getRecordType().equals(TOT_RECORD_TYPE)) {
							// Add netAmount from current txn and customer
							if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){									
								if ((!transactionType.equals("RETN"))){
									if ((currentNetAmount.greaterThan(zeroValue))) 
									{
												thresholdNetAmount = new ArmCurrency(rules[i].getValue());
												netAmount = currentNetAmount.add(customer.getYtdNetAmount(rules[i].getPriority()));
												//thresholdNetAmount = new ArmCurrency(rules[i].getValue());
												
												System.out.println("Before comparision thresholdNetAmount   "+ thresholdNetAmount);
												System.out.println(" Net amount = " + netAmount + " | " 
								  						+ "currentNetAmount = " + currentNetAmount + " | " 
								  						+ "custYtdNetAmount = " + customer.getYtdNetAmount(rules[i].getPriority()));
								  		  	  	
								  				
												if (totalNetAmount.greaterThan(thresholdNetAmount)) 
												{
														message = "Accumulated Staff Purchase amount including " +
																"this transaction is  "+ netAmount.formattedStringValue() + 
																"Which exceeded "+brand_ID+" Brand Quota "	
																+ thresholdNetAmount.getFormattedStringValue()+ 
																" this year.Please click “No” to make amendment";
															if (!isHKenv )
																message += "Do you still want to continue?";
						
																return new RulesInfo(message);
												}
									}	
							} // If Return 
							} // If Eur region 
							else{
			  				//Add netAmount from current txn and customer
			  				System.out.println(" Net amount = " + netAmount + " | " + "currentNetAmount = " + currentNetAmount + " | " 
			  						+ "custYtdNetAmount = " + customer.getYtdNetAmount(rules[i].getPriority()));
			  		  	  	netAmount = currentNetAmount.add(customer.getYtdNetAmount(rules[i].getPriority()));
			  				thresholdNetAmount = new ArmCurrency(rules[i].getValue());
//			  				if (netAmount.greaterThan(thresholdNetAmount)) {
			  					message = "The employee has purchased for " + netAmount.formattedStringValue() + 
			  						" currently and cannot exceed the permitted sales amount " 
			  						+ thresholdNetAmount.getFormattedStringValue() + ". ";
			  					message += "Do you still want to continue?";
			  					return new RulesInfo(message);
			  				}
			  				}
							
						
						if (rules[i].getRecordType().equals(PRO_RECORD_TYPE)) {
							productCode = rules[i].getProductCode();
							thresholdQty = new Integer(Integer.parseInt(rules[i].getValue()));
							qtyFromTxn = new Integer(0);
							qtyFromCust = new Integer(0);
							if (hCurrentQty.containsKey(productCode)) {
								qtyFromTxn = (Integer) hCurrentQty.get(productCode);
							}
							if (ytdQtyByProductCD != null
									&& ytdQtyByProductCD.size() > 0) {
								if (ytdQtyByProductCD.containsKey(productCode)) {
									qtyFromCust = (Integer) ytdQtyByProductCD.get(productCode);
								}
							}
							qty = new Integer(qtyFromTxn.intValue()
									+ qtyFromCust.intValue());
							hQuantity.put(productCode, qty);
							if (qty.intValue() > thresholdQty.intValue()) {
								message = "The employee has purchased "	+ (qtyFromCust.intValue() + qtyFromTxn.intValue())+ " items "
								+ "currently and cannot exceed the permitted sales quantity "+ thresholdQty.toString() + ". ";
								message += "Do you still want to continue?";
								return new RulesInfo(message);
							}
						}
					 }
					}
				   }
				//}
		} catch (Exception ex) {
			ex.printStackTrace();
		LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
				, "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
		}
		return new RulesInfo();
	}

	/**
	 * This method returns a list with current txn data
	 * @param compTxn
	 * @return
	 */
	private List getTransactionDataFromCurrentTxn(	CMSCompositePOSTransaction compTxn) {
		ArmCurrency currentNetAmount = new ArmCurrency(0.0d);
		CMSSaleLineItem saleLineItem = null;
		CMSReturnLineItem returnLineItem = null;
		Map currentQtyByProductCD = new HashMap();
		List dataFromCurrentTxn = new ArrayList();
		String productCode = null;
		Integer newValue, quantity;

		POSLineItem[] posLineItems = (POSLineItem[]) compTxn.getLineItemsArray();
		if (posLineItems != null) {			
			try {
				for (int i = 0; i < posLineItems.length; i++) {
					if (posLineItems[i] instanceof CMSSaleLineItem) {
						saleLineItem = (CMSSaleLineItem) posLineItems[i];
						if (!saleLineItem.isMiscItem()) {
							currentNetAmount = currentNetAmount.add(saleLineItem.getExtendedNetAmount());
							productCode = ((CMSItem) saleLineItem.getItem()).getProductNum();
							newValue = saleLineItem.getQuantity();							
							quantity = (Integer) currentQtyByProductCD.get(productCode);
							if (quantity == null) {
								quantity = newValue;
							} else {
								quantity = new Integer(quantity.intValue()+ newValue.intValue());
							}
							currentQtyByProductCD.put(productCode, quantity);
						}
					} else if (posLineItems[i] instanceof CMSReturnLineItem) {
						returnLineItem = (CMSReturnLineItem) posLineItems[i];
						if (!returnLineItem.isMiscItem()) {
							currentNetAmount = currentNetAmount.subtract(returnLineItem.getExtendedNetAmount());
							productCode = ((CMSItem) returnLineItem.getItem()).getProductNum();
							newValue = returnLineItem.getQuantity();							
							quantity = (Integer) currentQtyByProductCD.get(productCode);
							if (quantity == null) {
								quantity = newValue;
							} else {
								quantity = new Integer(quantity.intValue() - newValue.intValue());
							}
							currentQtyByProductCD.put(productCode, quantity);
						}
					} else {						
						continue;
					}
				}
			} catch (CurrencyException ce) {}
			dataFromCurrentTxn.add(currentNetAmount);
			dataFromCurrentTxn.add(currentQtyByProductCD);
		}
		return dataFromCurrentTxn;
	}

	/**
	 * This method gets customer alert rules
	 * @param countryCode
	 * @return
	 */
	private CMSCustomerAlertRule[] getCustomerAlertRules(String countryCode,String brand_id) {
		ConfigMgr config = new ConfigMgr("customer.cfg");
		CMSCustomerFileServices customerFileServices = (CMSCustomerFileServices) config.getObject("CLIENT_LOCAL_IMPL");
		CMSCustomerJDBCServices dao = (CMSCustomerJDBCServices) config.getObject("SERVER_IMPL");
		CMSCustomerAlertRule[] customerAlertRules = null;
	//vishal: multiple employee Discount
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
		try {
				//customerAlertRules = CMSCustomerHelper.getAllCustomerAlertRules(CMSApplet.theAppMgr, countryCode,brand_id);
			Date date = (Date)CMSApplet.theAppMgr.getGlobalObject("PROCESS_DATE");
			java.sql.Date businessDate = new java.sql.Date(date.getTime());
			customerAlertRules = CMSCustomerHelper.getAllCustomerAlertRules(CMSApplet.theAppMgr, countryCode,brand_id, businessDate);
		} catch (Exception e) {
				e.printStackTrace();
		}
		}		
		return customerAlertRules;
	}
	//vishal: ends here
	
	private CMSCustomerAlertRule[] getCustomerAlertRules(String countryCode	) {
		CMSCustomerAlertRule[] customerAlertRules = null;
		if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
		ConfigMgr config = new ConfigMgr("customer.cfg");
		CMSCustomerFileServices customerFileServices = (CMSCustomerFileServices) config.getObject("CLIENT_LOCAL_IMPL");
		CMSCustomerJDBCServices dao = (CMSCustomerJDBCServices) config.getObject("SERVER_IMPL");
		try {
			customerAlertRules = dao.getAllCustomerAlertRules(countryCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		}
		return customerAlertRules;
	}

	/**
	 * Returns the description of the business rule.
	 * @returns description of the business rule.
	 */
	public String getDesc() {
		return ("Validate Employee Rule For Current Transaction.");
	}

	/**
	 * Return the name of the rule.
	 * @return name of the rule.
	 */
	public String getName() {
		return "ValidateEmployeeRuleForCurrentTransaction";
	}
	/* 
	 * This method for getting total net amount including purchase history for the CR employee multi discount
	 * issue 1018 and 1019.
	 * saptarshi
	*/
	public static ArmCurrency getNetAmount(CMSCustomerAlertRule[] rules,POSLineItem[] posline,ArmCurrency netVal){
		ArmCurrency netAmount = new ArmCurrency(0.0d);
		for (int i = 0; i < rules.length; i++) {
			if (rules[i] != null) {
				try {
					if (rules[i].getRecordType().equals(TOT_RECORD_TYPE)) {
						double ruleDiscountPercent = Double.parseDouble(rules[i].getDsc_level());
 						for(int x=0;x<posline.length;x++){							
					    	Discount dis = posline[x].getDiscount();
					    	if(dis!=null){
					    		double retailAmountPercent = (posline[x].getDiscount().getPercent()*100);
					    		if(retailAmountPercent==ruleDiscountPercent){
					    			netAmount = netAmount.add(posline[x].getTotalAmountDue());	
					    			if (posline[x] instanceof CMSReturnLineItem) {
					    				String valore = posline[x].getTotalAmountDue().toString();
					    				valore ="-"+valore;
					    				ArmCurrency newValue = new ArmCurrency(valore);
					    				netAmount = netAmount.add(newValue);	
					    			}
					    		} else {
					    			if(ruleDiscountPercent== 0 ){
						    			netAmount = netAmount.add(posline[x].getTotalAmountDue());
						    		} 
					    		}
					    	}else{
					    		//Mahesh Nandure:Bug 28759- Wrong Calculation During SALE/ RETN Transaction
 					    		ArmCurrency returndesc=null;
 					    		returndesc = posline[x].getExtendedRetailAmount().subtract(posline[x].getNetAmount());
  		    					Double extendedRetailAmount=posline[x].getExtendedRetailAmount().doubleValue();
  		    					returndesc=returndesc.divide(extendedRetailAmount);
		    					Double discountPercent1=returndesc.doubleValue()*100;
					    		if(posline[x] instanceof CMSReturnLineItem && discountPercent1==ruleDiscountPercent){
					    		netAmount = netAmount.subtract(posline[x].getTotalAmountDue());     
							    }
					    		//end Mahesh Nandure:7th NOV 2017
					    		if(ruleDiscountPercent== 0 ){
					    			netAmount = netAmount.add(posline[x].getTotalAmountDue());
					    		} 
					    	}
						}
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
		try {
			//we have to write our code of getting correct net amt
			// as current purchase + prev purchase
			// doing it for now only Europe region
			// start--saptarshi
			netAmount=netAmount.add(netVal);
		} catch (CurrencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return netAmount;
	}

}

