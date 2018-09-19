/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.item;

import com.chelseasystems.cr.appmgr.state.StateException;
import com.chelseasystems.cr.appmgr.IApplicationManager;


/**
 * put your documentation comment here
 */
public class ItemDetailApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.goBack();
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int ITEM_DETAILS(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
}

