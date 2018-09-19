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
public class CustomerTxnHistoryApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CANCEL(IApplicationManager theAppMgr)
      throws StateException {
    // theAppMgr.goHome();
    clearStateObject(theAppMgr);
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    clearStateObject(theAppMgr);
    theAppMgr.goBack();
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int OTHER(IApplicationManager theAppMgr)
      throws StateException {
    return 0;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int TXN_DETAILS(IApplicationManager theAppMgr)
      throws StateException {
    return 1;
  }

  private void clearStateObject(IApplicationManager theAppMgr)
  {
    theAppMgr.removeStateObject("TXN_HEADER_ROW");
    theAppMgr.removeStateObject("TXN_HEADER_LIST");
    theAppMgr.removeStateObject("ARM_TXN_HEADERS");
    theAppMgr.removeStateObject("TITLE_STRING");
    theAppMgr.removeStateObject("DATE_STRING");
    theAppMgr.removeStateObject("TXN_HIST_SORTCOLUMN");
    theAppMgr.removeStateObject("TXN_HIST_SELECTEDTXNID");
  }
}

