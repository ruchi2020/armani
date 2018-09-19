/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;
import com.chelseasystems.cr.pos.CompositePOSTransaction;

/**
 * put your documentation comment here
 */
public class SelectItemsApplet_State {

	/**
	 * put your documentation comment here
	 */
	public SelectItemsApplet_State() {
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CANCEL(IApplicationManager theAppMgr) throws StateException {
		int iAppletMode;
		if (theAppMgr.getStateObject("TXN_MODE") != null) {
			iAppletMode = ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue();
			theAppMgr.removeStateObject("ARM_TXN_SELECTED");
			theAppMgr.removeStateObject("TXN_CUSTOMER");
			theAppMgr.removeStateObject("CUSTOMER");
			if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE) {
				return 4;
			} else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
				return 5;
			} else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
				return 2;
			}
		}
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		int iAppletMode;
		if (theAppMgr.getStateObject("TXN_MODE") != null) {
			iAppletMode = ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue();
			theAppMgr.removeStateObject("ARM_TXN_SELECTED");
			theAppMgr.removeStateObject("CUSTOMER");
			if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE) {
				return 4;
			} else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
				return 5;
			} else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
				theAppMgr.addStateObject("PREV_FROM_RESSERVATION_CLOSE", "PREV_FROM_RESSERVATION_CLOSE");
				return 2;
			}
		}
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SELL(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int RETURN(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SELL_RETURN_ITEMS(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int DE_SELECT_ALL(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int SELECT_ALL(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int ADD_ITEM_TENDER(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.removeStateObject("ARM_TXN_SELECTED");
		return 2;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int EMPLOYEE_SALE(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.removeStateObject("ARM_TXN_SELECTED");
		return 2;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int VIEW_PRESALES(IApplicationManager theAppMgr) throws StateException {
		return 3;
	}

	public int VIEW_RESERVATIONS(IApplicationManager theAppMgr) throws StateException {
		return 3;
	}

	public int VIEW_CONSIGNMENTS(IApplicationManager theAppMgr) throws StateException {
		return 3;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int OK(IApplicationManager theAppMgr) throws StateException {
		CompositePOSTransaction theTxn = (CompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		if (theTxn.getCustomer() != null)
			theAppMgr.addStateObject("CUSTOMER", theTxn.getCustomer());
		if (theTxn.getConsultant() != null)
			theAppMgr.addStateObject("ASSOCIATE", theTxn.getConsultant());
		theAppMgr.removeStateObject("ARM_TXN_SELECTED");
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CUST_LOOKUP(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.addStateObject("ARM_DIRECT_TO", "SELECT_ITEMS_APPLET");
		theAppMgr.addStateObject("ARM_DIRECTED_FROM", "SELECT_ITEMS_APPLET");
		theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
		return 1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int LIST_CUSTOMER_TXNS(IApplicationManager theAppMgr) throws StateException {
		return 6;
	}

	public int NO_OPEN_TXN(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.addStateObject("NO_OPEN_RESERVATIONS_CLOSE", "NO_OPEN_RESERVATIONS_CLOSE");
		theAppMgr.addStateObject("FROM_INITIAL_SALE_APPLET", "FROM_INITIAL_SALE_APPLET");
		return 7;
	}
}
