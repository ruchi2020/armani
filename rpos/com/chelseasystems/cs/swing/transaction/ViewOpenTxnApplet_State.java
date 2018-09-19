/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;

/**
 * put your documentation comment here
 */
public class ViewOpenTxnApplet_State {

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CANCEL(IApplicationManager theAppMgr) throws StateException {
		return 0;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int OK(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int PREV(IApplicationManager theAppMgr) throws StateException {
		return 0;
	}

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int CANCEL_PRE_SALE(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	public int CANCEL_RESERVATION(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}

	public int CANCEL_CONSIGNMENT(IApplicationManager theAppMgr) throws StateException {
		return -1;
	}
}
