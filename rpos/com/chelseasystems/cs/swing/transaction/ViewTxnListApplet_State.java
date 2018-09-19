/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet_EUR;

/**
 * put your documentation comment here
 */
public class ViewTxnListApplet_State {

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CANCEL(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.removeStateObject("TXN_CUSTOMER");
		if (theAppMgr.getStateObject("ARM_BACK_TO_SELECT_ITEMS") != null) {
			return 3;
		}
		if (theAppMgr.getStateObject("TXN_POS") == null)
			theAppMgr.goHome();
		else
			return 2;
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.removeStateObject("TXN_CUSTOMER");
		if (theAppMgr.getStateObject("ARM_BACK_TO_SELECT_ITEMS") != null) {
			return 3;
		}
		if (theAppMgr.getStateObject("THE_TXN") != null) {
			theAppMgr.removeStateObject("THE_TXN");
			theAppMgr.removeStateObject("TXN_HEADER_LIST");
			theAppMgr.removeStateObject("TXN_HEADER_ROW");
			theAppMgr.removeStateObject("TXN_DETAIL_CURR_PANEL");
			return 0;
		}

		// return 0;
		theAppMgr.goBack();
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int TXN_DETAILS(IApplicationManager theAppMgr) throws StateException {
		return 1;
	}

	public int CREATE_SALE(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet_EUR.SALE_MODE));
		theAppMgr.addStateObject("CREATE_SALE", new Boolean(true));
		return 2;
	}

	public int ADD_TO_FISCAL(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet_EUR.NO_SALE_MODE));
		theAppMgr.addStateObject("ADD_TO_FISCAL", new Boolean(true));
		return 2;
	}
}
