package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.state.StateException;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;


public class VoidTransactionApplet_State {
	
	public int CANCEL(IApplicationManager theAppMgr)
    throws StateException
  {
    theAppMgr.goHome();
    return -1;
  }

  public int OK(IApplicationManager theAppMgr) throws StateException {
    theAppMgr.goHome();
    return -1;
  }

  public int PREV(IApplicationManager theAppMgr)
    throws StateException
  {
    throw new StateException("State change not implemented->PREV");
  }

  public int MODIFY_REASON(IApplicationManager theAppMgr) throws StateException {
    return -1;
  }

  /**
	 * put your documentation comment here
	 * @param theAppMgr
	 * @return
	 * @exception StateException
	 */
	public int RETURN_ITEM(IApplicationManager theAppMgr) throws StateException {
		CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		return 0;
	}
}
