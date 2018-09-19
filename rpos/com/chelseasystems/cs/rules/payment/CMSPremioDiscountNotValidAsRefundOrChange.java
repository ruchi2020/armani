package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

public class CMSPremioDiscountNotValidAsRefundOrChange  extends Rule {
	public RulesInfo execute(Object theParent, Object args[]) {
	    return execute((CMSPremioDiscount)theParent, (PaymentTransaction)args[0]);
	  }
	private RulesInfo execute(CMSPremioDiscount discount, PaymentTransaction paymentTransaction) {
	    try {
	      // place business logic here
	    	if(true){
	    		return new RulesInfo();
	    	}
	    	System.out.println("CMSPremioDiscountNotValidAsRefundOrChange starts ");
	    	if(paymentTransaction instanceof CMSCompositePOSTransaction){
	    		/*CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS");
	    		System.out.println("theTxn " + (theTxn==null?"null":"not null"));
	    		
	    		Payment[] payments  = theTxn.getPaymentsArray();
	    		
	    		System.out.println("payments " + (payments.length));
	    		for(int i=0;i<payments.length;i++){
	    			System.out.println("Payment " + payments[i].getGUIPaymentName());
	    		}*/
	    		
	    		return new RulesInfo();
	    	}
	    	return new RulesInfo();  	
	    	
	//      return new RulesInfo("Premio Discount is not valid as refund or change.");
	    } catch (Exception ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
	          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	      ex.printStackTrace();
	      return new RulesInfo();
	    }
	  }
	public String getName() {
	    return "Premio Discount is valid as refund or change";
	  }
	public String getDesc() {
	    StringBuffer buf = new StringBuffer();
	    buf.append("");
	    return buf.toString();
	  }
}
