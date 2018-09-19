/*
 * Created on Oct 1, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.chelseasystems.cs.payment;

/**
 * @author Milind
 */

import java.io.Serializable;
import java.util.Date;

import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cr.appmgr.AppManager;

public class CMSLoyaltyAppModel implements Serializable {
	static final long serialVersionUID = 4226671535815953973L;

	private Loyalty loyalty = null;
	private CMSEmployee issuedBy = null;
	private CMSStore store = null;
	private CMSEmployee operator = null;

	protected CMSLoyaltyAppModel() {
	}

	public CMSLoyaltyAppModel(Loyalty loyalty) {
		store = (CMSStore) AppManager.getCurrent().getGlobalObject("STORE");
		operator = (CMSEmployee) AppManager.getCurrent().getStateObject("OPERATOR");
		this.loyalty = loyalty;
		try {
			issuedBy = CMSEmployeeHelper.findByShortName(AppManager.getCurrent(), loyalty.getIssuedBy());
		} catch (Exception e) {
			issuedBy = new CMSEmployee(loyalty.getIssuedBy());
			issuedBy.doSetShortName("");
		}
	}

	/**
	 * Get current balance
	 * @return double
	 */
	public double getCurrentBalance() {
		return loyalty.getCurrBalance();
	}

	/**
	 * Get current year balance
	 * @return double
	 */
	public double getCurrentYearBalance() {
		return loyalty.getCurrYearBalance();
	}

	/**
	 * Get last year balance
	 * @return double
	 */
	public double getLastYearBalance() {
		return loyalty.getLastYearBalance();
	}

	/**
	 * Get lifetime balance
	 * @return double
	 */
	public double getLifeTimeBalance() {
		return loyalty.getLifeTimeBalance();
	}

	/**
	 * Get issue date
	 * @return Date
	 */
	public Date getIssueDate() {
		return loyalty.getIssueDate();
	}

	/**
	 * Get issued by employee
	 * @return CMSEmployee
	 */
	public CMSEmployee getIssuedBy() {
		return issuedBy;
	}

	/**
	 * Get customer
	 * @return CMSCustomer
	 */
	public CMSCustomer getCustomer() {
		return loyalty.getCustomer();
	}

	/**
	 * Get store
	 * @return CMSStore
	 */
	public CMSStore getStore() {
		return store;
	}

	/**
	 * Get operator
	 * @return CMSEmployee
	 */
	public CMSEmployee getOperator() {
		return operator;
	}

	/**
	 * Get current date
	 * @return Date
	 */
	public Date getCurrentDate() {
		return new Date();
	}

	/**
	 * Get card number
	 * @return String
	 */
	public String getCardNumber() {
		return loyalty.getLoyaltyNumber();
	}
}
