/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CustomerManagementApplet_State {

	/**
	 * put your documentation comment here
	 */
	public CustomerManagementApplet_State() {
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PURCHASE_HISTORY(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int TXN_HIST(IApplicationManager theAppMgr) throws StateException {
		return 3;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int LOYALTY(IApplicationManager theAppMgr) throws StateException {
		return 2;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int OK(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null && theAppMgr.getStateObject("ARM_DIRECTED_FROM") == null) {
			theAppMgr.removeStateObject("CUST_MGMT_MODE");
			theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
			return 4;
		}
		return 0;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int VIEW_DEPOSITS(IApplicationManager theAppMgr) throws StateException {
		return 5;
	}

	public int VIEW_CREDITS(IApplicationManager theAppMgr) throws StateException {
		return 6;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CANCEL(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null 
			&& theAppMgr.getStateObject("ARM_DIRECTED_FROM") == null) {
			theAppMgr.removeStateObject("CUST_MGMT_MODE");
			theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
			return 4;
		}
		return 0;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null 
			&& theAppMgr.getStateObject("ARM_DIRECTED_FROM") == null) {
			theAppMgr.removeStateObject("CUST_MGMT_MODE");
			theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
			return 4;
		}
		return 0;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int NEW_CUST(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int MOD_CUST(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int VIEW_CARD_DETAILS(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int VIEW_COMMENTS(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int VIEW_ADDRESSES(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int ADD_COMMENTS(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int ADD_ADDRESS(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int ADD_CREDITCARD(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int MODIFY_CREDITCARD(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int REMOVE_CREDITCARD(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CUST_LOOKUP(IApplicationManager theAppMgr) throws StateException {
		if (!theAppMgr.isOnLine()) {
			theAppMgr.showErrorDlg("Operation not available this time.");
			return -1;
		}
		theAppMgr.addStateObject("REFERRED_BY", "REFERRED_BY");
		return 1;
	}
}
