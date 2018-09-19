/*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.payment.RoundPayment;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 * <p>Title: RoundPaymentNotValidAsRefundOrChange_EUR</p>
 * <p>Description: This business rule check that RoundPayment is not a valid
 * form as Refund or Change payment.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sumit Krishnan
 * @version 1.0
 */
public class RoundPaymentNotValidAsPayment extends Rule {

	/**
	 * put your documentation comment here
	 */
	public RoundPaymentNotValidAsPayment() {
	}

	/**
	 * Execute the Rule
	 * @param theParent - instance of RoundPayment
	 * @param args - instance of PaymentTransaction
	 */
	public RulesInfo execute(Object theParent, Object args[]) {
		return (execute((RoundPayment)theParent, (PaymentTransaction)args[0]));
	}

	/**
	 * Execute the Rule
	 * @param theParent - instance of RoundPayment
	 * @param args - instance of PaymentTransaction
	 */
	private RulesInfo execute(RoundPayment roundPayment, PaymentTransaction paymentTransaction) {
		try {
			if (paymentTransaction instanceof CollectionTransaction) {
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
				enable=mgr.getString(roundPayment.getTransactionPaymentName()+".Enable");
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
		return (CMSApplet.res.getString("RoundPaymentNotValidAsPayment_EUR"));
	}

	/**
	 * Returns the description of the business rule.
	 * @returns description of the business rule.
	 */
	public String getDesc() {
		StringBuffer buf = new StringBuffer();
		buf.append("");
		return (buf.toString());
	}
}

