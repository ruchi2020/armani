package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.state.StateException;

public class V12BasketApplet_State {

	public int PREV(IApplicationManager theAppMgr) throws StateException {
		return 0;
	}

	public int BASKET_LOOKUP(IApplicationManager theAppMgr) throws StateException {
		return 1;
	}
	
}
