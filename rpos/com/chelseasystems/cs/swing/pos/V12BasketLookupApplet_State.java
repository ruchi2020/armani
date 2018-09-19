package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.state.StateException;

public class V12BasketLookupApplet_State {

	public int PREV(IApplicationManager theAppMgr) throws StateException {
		theAppMgr.removeStateObject("V12BASKET_LOOKUP");
		return 0;
	}

	public int RESUME(IApplicationManager theAppMgr) throws StateException {
		return 1;
	}
	
}
