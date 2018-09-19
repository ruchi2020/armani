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
 * @author not attributable
 * @version 1.0
 */
public class DepositHistoryApplet_State {

	/**
	 * put your documentation comment here
	 */
	public DepositHistoryApplet_State() {
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		if (theAppMgr.getStateObject("FOR_VIEW_DEPOSIT") != null) {
			theAppMgr.addStateObject("CALL_ITEM_BLDR", "CALL_ITEM_BLDR");
			return 2;
		}
		return 0;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int OK(IApplicationManager theAppMgr) throws StateException {
		return 1;
	}

	/**
	 * @param theAppMgr IApplicationManager
	 * @throws StateException
	 * @return int
	 */
	public int PRINT_DEPOSIT_HISTORY(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * @param theAppMgr
	 * @return
	 * @throws StateException
	 */
	public int DEPOSIT_DETAILS(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}
}
