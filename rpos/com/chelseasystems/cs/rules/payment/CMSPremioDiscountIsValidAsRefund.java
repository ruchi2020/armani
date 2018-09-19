package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItemDetail;

public class CMSPremioDiscountIsValidAsRefund extends Rule{
	/**
	   * put your documentation comment here
	   */
	  public CMSPremioDiscountIsValidAsRefund() {
	  }

	  
	  public RulesInfo execute(Object theParent, Object args[]) {
	    return execute((CMSPremioDiscount)theParent, (PaymentTransaction)args[0]);
	  }

	  
	  private RulesInfo execute(CMSPremioDiscount coupon, PaymentTransaction paymentTransaction) {
	    try {
	      // place business logic here
	    	System.out.println("CMSPremioDiscountIsValidAsRefund here " );
	    	
	    	if(paymentTransaction instanceof CMSCompositePOSTransaction){
	    		/*CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS");
	    		POSLineItem[] retItems = theTxn.getReturnLineItemsArray();
	    		CMSCompositePOSTransaction origTxn = null;
	    	      if (retItems != null) {
	    	    	  for (int i = 0; i < retItems.length; i++) {
	    	              CMSReturnLineItem retItem = (CMSReturnLineItem)retItems[i];
	    	              if (((CMSReturnLineItemDetail)retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
		    	              origTxn = (CMSCompositePOSTransaction)((CMSReturnLineItemDetail)retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail().getLineItem().getTransaction().getCompositeTransaction();
		    	              if(origTxn !=null){
		    	            	  break;
		    	              }
		    	    	  }
	    	    	  }
	    	    	  if(origTxn!=null) { 
	    	    		  System.out.println("orig Txn " + origTxn.getId());
	    	    		  Payment[] payments = origTxn.getPaymentsArray();
	    	    		  System.out.println("payments " + payments.length );
	    	    		  for(int j=0;j<payments.length;j++){
	    	    			  Payment payment = payments[j];
	    	    			  System.out.println(payments[j].getGUIPaymentName());
	    	    			  if(payment instanceof CMSPremioDiscount){
	    	    				  String Builder = CMSPaymentMgr.getPaymentBuilder("PREMIO_DISCOUNT");
	    	    		          if (Builder != null) {
	    	    		        	  CMSApplet.theAppMgr.addStateObject("REFUND_PREMIO",payment.getAmount());
	    	    		            CMSApplet.theAppMgr.buildObject("PAYMENT", Builder, "PREMIO_DISCOUNT");
	    	    		            
	    	    		          }
	    	    			  }
	    	    		  }
	    	    		  
	    	    	  }else{
	    	    		  System.out.println("orig Txn is null");
	    	    	  }
	    	      }else{
	    	    	  System.out.println("No return items ");
	    	      } */
	    	}	    	
	      return new RulesInfo();
	    } catch (Exception ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
	          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	      return new RulesInfo();
	    }
	  }

	  
	  public String getName() {
	    return "CMSPremioDiscount is valid as refund";
	  }

	  
	  public String getDesc() {
	    StringBuffer buf = new StringBuffer();
	    buf.append("");
	    return buf.toString();
	  }
}
