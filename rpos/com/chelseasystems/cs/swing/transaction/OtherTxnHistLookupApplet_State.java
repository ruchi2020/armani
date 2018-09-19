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
public class OtherTxnHistLookupApplet_State {

  /**
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SEARCH(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }

  /**
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.goBack();
    return -1;
  }
}

