package com.chelseasystems.cs.rules.transaction;

import com.armani.business.rules.ARMCustomerBR;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.payment.Credit;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.util.Version;

public class IsUpdateCustomerForTransactionAllowed  extends Rule {

	public IsUpdateCustomerForTransactionAllowed() {
	  }
	public RulesInfo execute(Object theParent, Object args[]) {
	    return execute((CMSCompositePOSTransaction)theParent);
	  }
	private RulesInfo execute(CMSCompositePOSTransaction txn) {
	    try {
	    	 //--Begin SAP-CRM changes. 
	    	//MB: 06/06/2011
	    	//Don't allow Customer update if 
	    	//customer is not DUMMY or one created on POS.
		    	if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
		    			{	  
		    	//	System.out.println("here   :"+txn.getCustomer().getId());
		    		if(txn!=null && txn.getCustomer()!=null){
		    			String customerID = txn.getCustomer().getId();
		    			ConfigMgr customerCFG = new ConfigMgr("customer.cfg");
		    	        String posCustomerPrefix = "P";
		    	        if(customerCFG.getString("POS_CUSTOMER_ID_PREFIX")!=null){
		    	        	posCustomerPrefix  = customerCFG.getString("POS_CUSTOMER_ID_PREFIX");
		    	        }	    			
		    	        if (ARMCustomerBR.isDummy(customerID)
								||  customerID.startsWith(posCustomerPrefix)||!customerID.equals(null)&& !customerID.startsWith("S69")) {
		    	        	    //||  customerID.startsWith(posCustomerPrefix)||!customerID.equals(null)&& customerID.startsWith("S")) {
							}else{
								return new RulesInfo("Customer update not allowed for Non-dummy customers");
						}
						if ( txn.hasFiscalDocuments() && (!customerID.equals(null)&& customerID.startsWith("S")) )
						{
							return new RulesInfo("Customer update not allowed for Non-dummy customers");
						}
		    		}
				}
		    	if(("US".equalsIgnoreCase(Version.CURRENT_REGION))){
		    		System.out.println("Inside the rule :");
		    		if(txn.getCustomer()!= null){
		    			return new RulesInfo("Customer update not allowed for already existing customers");	
		    		}
		    		if(txn!=null && txn.getCustomer()!=null){
		    			String customerID = txn.getCustomer().getId();
		    			ConfigMgr customerCFG = new ConfigMgr("customer.cfg");
		    	        String posCustomerPrefix = "P";
		    	        if(customerCFG.getString("POS_CUSTOMER_ID_PREFIX")!=null){
		    	        	posCustomerPrefix  = customerCFG.getString("POS_CUSTOMER_ID_PREFIX");
		    	        }
		    	        if(customerID.startsWith(posCustomerPrefix)||!customerID.equals(null) && customerID.startsWith("S")){
		    	        	}
						else{
							return new RulesInfo("Customer update not allowed for this customers");
						}
							
		    		}
		    		
		    	}
		    	
		    	//Added by Vivek Mishra for Japan's PCR : Delayed Customer Association
		    	if(("JP".equalsIgnoreCase(Version.CURRENT_REGION))){
		    		if(txn!=null && txn.getCustomer()!=null){
		    			String customerID = txn.getCustomer().getId();
		    	        if(ARMCustomerBR.isDummy(customerID)){
		    	        	}
						else{
							return new RulesInfo("Customer update not allowed for Non-dummy customers");
						}
							
		    		}
		    		
		    	}//End
	    	  //-End SAP-CRM changes.
	          Payment[] payments = txn.getPaymentsArray();
	          for(int i=0;payments!=null && i<payments.length;i++){
	        	  if(payments[i] instanceof Credit){
	        		  return new RulesInfo("Transaction with Credit tender does not allow customer update");
	        	  }
	          }
	          POSLineItem[] lineItems = txn.getConsignmentLineItemsArray();
	          if(lineItems!=null && lineItems.length>0){
	        	  return new RulesInfo("Transaction with consignment open does not allow customer update"); 
	          }
	          
	          lineItems = txn.getLineItemsArray();
	          for(int i=0;lineItems!=null && i<lineItems.length;i++){
	        	  CMSItem item = (CMSItem)lineItems[i].getItem();
	        	  if(item.isDeposit()){
	        		  return new RulesInfo("Transaction with deposit item does not allow customer update");
	        	  }
	          }
	    } catch (Exception ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
	          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	      ex.printStackTrace();
	    }
	    return new RulesInfo();
	  }
	public String getName() {
	    return "Update Customer for Transaction Allowed";
	  }
	public String getDesc() {
	    return "Rule should determine that the transaction allows update of customer";
	  }
}
