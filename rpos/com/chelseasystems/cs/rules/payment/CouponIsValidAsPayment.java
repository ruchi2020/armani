/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 04-19-2005 | Anand     | Original         | Original Version per spec    |
 |        |            |           |                  | to accomodate paid-in type   |
 +--------+------------+-----------+------------------+------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.payment;

import com.chelseasystems.cr.rules.*;

import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.collection.CMSMiscCollection;


/**
 * <p>Title: CouponIsValidAsPayment</p>
 * <p>Description: This business rule checks that Coupon is valid form as
 * Payment </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class CouponIsValidAsPayment extends Rule {

	//boolean isReasonCodeMiscPayment = CMSApplet.theAppMgr.getStateObject("REASON_CODE").equals("MISC_PAID_IN");
	public CouponIsValidAsPayment() {
	}

	/**
	 * Execute the rule.
	 *
	 * @param object theParent - instance of CMSMenuOption
	 * @param args - instance of Employee
	 * @param args - instance of Store
	 * @return RulesInfo
	 */
	public RulesInfo execute(Object theParent, Object args[]) {
		// Change made by Satin for coupon management PCR

		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			return execute(((CMSCoupon)theParent), ((PaymentTransaction)args[0]));
		}
		else {
			return execute(((Coupon)theParent), ((PaymentTransaction)args[0]));
		}

	}

	/**
	 * Execute the Rule
	 * @param cash - instance of CMSCoupon.
	 * @param args - instance of PaymentTransaction
	 */
	private RulesInfo execute(CMSCoupon cash, PaymentTransaction paymentTransaction) {
		try {

			// Added by Satin.
			// If Offline mode, 'Coupon' button gets invisible.
			if(!CMSApplet.theAppMgr.isOnLine()){
				return new RulesInfo("should NOT be enabled");
			}
			if (paymentTransaction instanceof CollectionTransaction) {
				if (((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
						"NFS_CHECK_PAYMENT")
						|| ((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().
						equals("HOUSE_ACCOUNT_PAYMENT")
						||((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
								"OPEN_DEPOSIT") )
					return new RulesInfo("should NOT be enabled");
			}
			//Added by SonaliRaina for disabling payment option buttons
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
			{
				ConfigMgr mgr =new ConfigMgr("payment.cfg");
				String enable="true";
				enable=mgr.getString(cash.getTransactionPaymentName()+".Enable");
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
	 * Execute the Rule
	 * @param cash - instance of Coupon.
	 * @param args - instance of PaymentTransaction
	 */
	private RulesInfo execute(Coupon cash, PaymentTransaction paymentTransaction) {
		try {
			if (paymentTransaction instanceof CollectionTransaction) {
				if (((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
						"NFS_CHECK_PAYMENT")
						|| ((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().
						equals("HOUSE_ACCOUNT_PAYMENT")
						||((CollectionTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS")).getType().equals(
								"OPEN_DEPOSIT") )
					return new RulesInfo("should NOT be enabled");
			}

		}

		catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
					, "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
		}
		return new RulesInfo();
	}


	/**
	 * Returns the description of the business rule.
	 * @returns description of the business rule.
	 */
	public String getDesc() {
		return ("Suppress menu button if not appropriate.");
	}

	/**
	 * Return the name of the rule.
	 * @return name of the rule.
	 */
	public String getName() {
		return "ShowIfReasonCodeNFSCheckPayment";
	}
}

