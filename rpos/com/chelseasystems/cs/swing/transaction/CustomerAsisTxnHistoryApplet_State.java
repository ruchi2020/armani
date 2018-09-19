/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

/*0. com.chelseasystems.cs.swing.transaction.ASISTxnDataApplet */
package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * put your documentation comment here
 */
public class CustomerAsisTxnHistoryApplet_State {


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

  public int TXN_DETAILS(IApplicationManager theAppMgr)
      throws StateException {
    return 0;
  }

}

