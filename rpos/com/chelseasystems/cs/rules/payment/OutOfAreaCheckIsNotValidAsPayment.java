/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;

import java.util.*;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.collection.CMSCollectionTransaction;


/**
 * put your documentation comment here
 */
public class OutOfAreaCheckIsNotValidAsPayment extends Rule {

	/**
	 * put your documentation comment here
	 */
	public OutOfAreaCheckIsNotValidAsPayment() {
	}

	/**
	 * Execute the Rule
	 * @param theParent - instance of OutOfAreaCheck
	 * @param args - instance of PaymentTransaction
	 */
	public RulesInfo execute(Object theParent, Object args[]) {
		return execute((OutOfAreaCheck)theParent, (PaymentTransaction)args[0]);
	}

	/**
	 * Execute the Rule
	 * @param theParent - instance of OutOfAreaCheck
	 * @param args - instance of PaymentTransaction
	 */
	private RulesInfo execute(OutOfAreaCheck outofareacheck, PaymentTransaction paymentTransaction1) {
		try {
			// place business logic here
			if (paymentTransaction1 instanceof CMSCollectionTransaction) {
				if (((CMSCollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
						"NFS_CHECK_PAYMENT")) {
					return new RulesInfo("Out of Area is not a valid type of Payment");
				}
			}
			//Added by SonaliRaina for disabling payment option buttons
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
			{
				ConfigMgr mgr =new ConfigMgr("payment.cfg");
				String enable="true";
				enable=mgr.getString(outofareacheck.getTransactionPaymentName()+".Enable");
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
			return new RulesInfo();
		}
		return new RulesInfo();
	}

	/**
	 * Returns the name of the Rule.
	 * @return the name of the rule
	 */
	public String getName() {
		return "OutOfCheckISValidAsChange";
	}

	/**
	 * Returns a user-friendly version of the rule.
	 * @returns a user-friendly version of the rule.
	 */
	public String getDesc() {
		StringBuffer buf = new StringBuffer();
		buf.append("Out of Area is not a valid change type");
		return buf.toString();
	}
}

