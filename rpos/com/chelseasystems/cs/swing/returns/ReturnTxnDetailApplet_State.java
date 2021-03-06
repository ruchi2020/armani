/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.returns;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * Comments generated by AppBuilder. Do not modify.
 * 0. com.chelseasystems.cs.swing.returns.ReturnTxnHistoryApplet
 * 1. com.chelseasystems.cs.swing.returns.InitialReturnApplet
 */
public class ReturnTxnDetailApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CANCEL(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.removeStateObject("ARM_TXN_HEADERS");
    theAppMgr.removeStateObject("TITLE_STRING");
    theAppMgr.removeStateObject("DATE_STRING");
    theAppMgr.removeStateObject("TXN_HIST_SELECTEDTXNID");
    theAppMgr.removeStateObject("TXN_HIST_SORTCOLUMN");
    return 1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int OK(IApplicationManager theAppMgr)
      throws StateException {
    //
    // put state logic here
    //
    throw new StateException("State change not implemented->OK");
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    return 0;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int RETURN_TXN(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.removeStateObject("ARM_TXN_HEADERS");
    theAppMgr.removeStateObject("TITLE_STRING");
    theAppMgr.removeStateObject("DATE_STRING");
    theAppMgr.removeStateObject("TXN_HIST_SELECTEDTXNID");
    theAppMgr.removeStateObject("TXN_HIST_SORTCOLUMN");
    return 1;
  }
}

