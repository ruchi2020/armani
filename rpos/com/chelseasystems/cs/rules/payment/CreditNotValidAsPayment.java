/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;

import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.config.ConfigMgr;
/**
 * put your documentation comment here
 */
public class CreditNotValidAsPayment extends Rule {

	/**
	 * put your documentation comment here
	 */
	public CreditNotValidAsPayment() {
	}

	/**
	 * Execute the Rule
	 * @param theParent - instance of Credit
	 * @param args - instance of PaymentTransaction
	 */
	public RulesInfo execute(Object theParent, Object args[]) {
		return execute((Credit)theParent, (PaymentTransaction)args[0]);
	}

	/**
	 * Execute the Rule
	 * @param theParent - instance of Credit
	 * @param args - instance of PaymentTransaction
	 */
	private RulesInfo execute(Credit credit, PaymentTransaction paymentTransaction1) {
		try {
			if (paymentTransaction1 instanceof CollectionTransaction) {
				if (((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
						"OPEN_DEPOSIT") ||
						((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
								"NFS_CHECK_PAYMENT"))
					return new RulesInfo("should NOT be enabled");
			}
			//Added by SonaliRaina for disabling payment option buttons
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
			{
				ConfigMgr mgr =new ConfigMgr("payment.cfg");
				String enable="true";
				enable=mgr.getString(credit.getTransactionPaymentName()+".Enable");
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

	/**
	 * Returns the name of the Rule.
	 * @return the name of the rule
	 */
	public String getName() {
		return "CreditNotValidAsPayment";
	}

	/**
	 * Returns a user-friendly version of the rule.
	 * @returns a user-friendly version of the rule.
	 */
	public String getDesc() {
		StringBuffer buf = new StringBuffer();
		buf.append("Credit  is not a valid type of payment");
		return buf.toString();
	}
}

