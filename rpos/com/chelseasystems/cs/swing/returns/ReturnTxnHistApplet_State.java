/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 05-11-2005 | Manpreet  | N/A       | Return Txn History                           |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.returns;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * put your documentation comment here
 */
public class ReturnTxnHistApplet_State {

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

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CUST_LOOKUP(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.addStateObject("ARM_DIRECT_TO", "RETURN_TXN_HIST_APPLET");
    theAppMgr.addStateObject("ARM_DIRECTED_FROM", "RETURN_TXN_HIST_APPLET");
    return 0;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int OTHER(IApplicationManager theAppMgr)
      throws StateException {
    return 1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.removeStateObject("ARM_DIRECT_TO");
    theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
    return 3;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int TENDER_TYPE(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int TXN_DETAILS(IApplicationManager theAppMgr)
      throws StateException {
    return 2;
  }
}

