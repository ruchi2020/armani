package com.chelseasystems.cs.rules.payment;



import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;

import com.chelseasystems.cr.payment.TravellersCheck;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.util.Version;

public class TravellersCheckIsValidAsPayment extends Rule {

	@Override
	public RulesInfo execute(Object theParent, Object args[]) {
		return execute((TravellersCheck)theParent, (PaymentTransaction)args[0]);
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	private RulesInfo execute(TravellersCheck card, PaymentTransaction paymentTransaction) {
		try {
			//Added by SonaliRaina for disabling payment option buttons
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
			{
				ConfigMgr mgr =new ConfigMgr("payment.cfg");
				String enable="true";
				enable=mgr.getString(card.getTransactionPaymentName()+".Enable");
				if(enable!=null){
					if(enable.equalsIgnoreCase("false"))
					{
						return new RulesInfo("should NOT be enabled");
					}
				}
			}

		}
		catch(NullPointerException e){}
		//Added by SonaliRaina to handle null values if no parameter is found in payment.cfg
		catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
					, "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
		}
		return new RulesInfo();
	}


}
