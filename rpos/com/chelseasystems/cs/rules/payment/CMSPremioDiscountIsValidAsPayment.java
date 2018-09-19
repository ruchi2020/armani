package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.builder.CMSPremioDiscountBldr;

public class CMSPremioDiscountIsValidAsPayment  extends Rule {
	public CMSPremioDiscountIsValidAsPayment() {
	  }
	
	public RulesInfo execute(Object theParent, Object args[]) {
	    return execute((CMSPremioDiscount)theParent, (PaymentTransaction)args[0]);
	  }
	private RulesInfo execute(CMSPremioDiscount premio, PaymentTransaction paymentTransaction) {
	    try {
	        if(paymentTransaction instanceof CMSCompositePOSTransaction){
	        	if(((CMSCompositePOSTransaction)paymentTransaction).getRedeemableAmount()>0){
	        		return new RulesInfo(CMSApplet.res.getString("should NOT be enabled"));
	        	}
	        	
	        	CMSCustomer cust = (CMSCustomer)((CMSCompositePOSTransaction)paymentTransaction).getCustomer();
	        	if(cust == null ){
	        		return new RulesInfo(CMSApplet.res.getString("should NOT be enabled"));
	        	}
	        	
	        	if(((CMSCompositePOSTransaction)paymentTransaction).getLoyaltyCard() == null){
	        		String brandID = ((CMSStore)paymentTransaction.getStore()).getBrandID();
	        		ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
		        	
	        		Loyalty[] loyalties = LoyaltyHelper.getCustomerLoyalties(CMSApplet.theAppMgr,cust.getId());
			        Loyalty loyalty = null;
			        String loyaltyStrBrandID = null;
		        	for(int i=0;loyalties!=null && i<loyalties.length;i++){
		        		if(loyalties[i]!=null){
		        			loyaltyStrBrandID = loyaltyConfigMgr.getString(loyalties[i].getStoreType() + ".TYPE");
			        	    if (loyaltyStrBrandID != null && brandID != null 
			        	    	  && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim())
			        	          && loyalties[i].getStatus() == true){
			        	    	loyalty = loyalties[i];
			        	    	break;
			        	    }
		        		}
		        	}
		        	if(loyalty==null || loyalty.getCurrBalance()==0){
		        		return new RulesInfo(CMSApplet.res.getString("should NOT be enabled")); 
		        	}
		        	System.out.println("loyaltyCurrBalance " + loyalty.getCurrBalance());
		        	((CMSCompositePOSTransaction)paymentTransaction).setLoyaltyCard(loyalty);
	        	}else{
	        		Loyalty loyalty = ((CMSCompositePOSTransaction)paymentTransaction).getLoyaltyCard();
	        		System.out.println("loyalty set "  + loyalty==null?"null":loyalty.getLoyaltyNumber());
	        		if(loyalty==null || loyalty.getCurrBalance()==0){
		        		return new RulesInfo(CMSApplet.res.getString("should NOT be enabled")); 
		        	}
	        		if(!CMSPremioDiscountBldr.isEnoughPoints(loyalty.getCurrBalance())){
	        			return new RulesInfo(CMSApplet.res.getString("should NOT be enabled"));
	        		}
	        		
	        		System.out.println("loyalty currbalance  "   + loyalty.getCurrBalance());
	        	}
	        	
	        }
	    	
	    } catch (Exception ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
	          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	      ex.printStackTrace();
	      return new RulesInfo(CMSApplet.res.getString("should NOT be enabled"));
	    }
	    return new RulesInfo();
	  }
	
	 public String getDesc() {
		return (CMSApplet.res.getString("Suppress menu button if not appropriate."));
	}

	public String getName() {
		return CMSApplet.res.getString("SuppressOrShowCashMenuButton");
	}
	
}
