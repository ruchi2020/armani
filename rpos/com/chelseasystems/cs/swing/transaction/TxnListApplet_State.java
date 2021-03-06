/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * Comments generated by AppBuilder. Do not modify.
 * 0. com.chelseasystems.cs.swing.transaction.ViewTxnApplet
 * 1. com.chelseasystems.cs.swing.transaction.TxnDetailApplet
 */
public class TxnListApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CANCEL(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.goHome();
    return -1;
  }

  /**
   * Buttons does not really exist, but method will send applet to TxnDetailApplet
   */
  public int OK(IApplicationManager theAppMgr)
      throws StateException {
    return 1;
  }

  /**
   * Method should goBack to ViewTransactionApplet
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    if (theAppMgr.getStateObject("ADHOC_QUERY") == null)
      return 0;
    else
      return 2;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PRINT(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV_DETAIL(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
}

