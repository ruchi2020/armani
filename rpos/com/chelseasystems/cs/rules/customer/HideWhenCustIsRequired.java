/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.customer;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.pos.*;


/**
 *
 * <p>Title: HideWhenCustIsRequired</p>
 *
 * <p>Description: This business rule check whether customer is required or not,
 * If customer is required then this rule will not allow the skipping of customer</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class HideWhenCustIsRequired extends Rule {

	/**
	 * Default Constructor
	 */
	public HideWhenCustIsRequired() {
	}

	/**
	 * @param object
	 *            theParent - instance of CMSMenuOption
	 * @param args -
	 *            instance of Employee
	 * @param args -
	 *            instance of Store
	 * @return RulesInfo
	 */
	public RulesInfo execute(Object theParent, Object[] args) {
		return execute((CMSMenuOption) theParent, (Employee) args[0], (Store) args[1]);
	}

	/**
	 * Execute the Rule
	 * @param cmsmenuoption
	 *            theParent
	 * @param employee
	 *            Employee
	 * @param store
	 *            Store
	 * @return RulesInfo
	 */
	private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {
		try {
			String sCustRequired = null;
			PaymentTransaction theTxn = (PaymentTransaction) CMSApplet.theAppMgr.getStateObject("TXN_POS");
			//Added by Vivek Mishra for Japan's PCR : Delayed Customer Association
			//uncommented this line for US and Europe
			 sCustRequired = (String) CMSApplet.theAppMgr.getStateObject("ARM_CUST_REQUIRED");
			if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
			   sCustRequired = (CMSApplet.theAppMgr.getStateObject("ARM_CUST_REQUIRED")).toString();
			}
			//End
			CMSCustomer customer = null;
			if (theTxn != null && theTxn instanceof RedeemableBuyBackTransaction)
				customer = ((CMSRedeemableBuyBackTransaction) theTxn).getCustomer();
			else if (theTxn != null && theTxn instanceof CMSCompositePOSTransaction)
				customer = (CMSCustomer) ((CMSCompositePOSTransaction) theTxn).getCustomer();
			if (sCustRequired != null && sCustRequired.equalsIgnoreCase("TRUE") && customer == null) {
				// VM: Do not remove ARM_CUST_REQUIRED here, as it is required even after this stage
				// CMSApplet.theAppMgr.removeStateObject("ARM_CUST_REQUIRED");
				return new RulesInfo("Can't skip when customer is required");
			} else if (sCustRequired != null && sCustRequired.equalsIgnoreCase("TRUE") && CMSApplet.theAppMgr.getGlobalObject("EOD_COMPLETE") != null) {
				return new RulesInfo("Can't skip when customer is required");
			}

		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "execute", "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
			//ex.printStackTrace();
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
		return "HideIfCustomerIsRequired";
	}
}
