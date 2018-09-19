/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 09-08-2006 | Sandhya   | N/A       | Redeemable Buyback Transaction               |
 --------------------------------------------------------------------------------------------
 */

public class RedeemableBuyBackApplet_State {	
	
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int OK(IApplicationManager theAppMgr)
      throws StateException {
	  theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
      theAppMgr.addStateObject("ARM_DIRECTED_FROM", "REDEEMABLE_BUYBACK_APPLET");
      theAppMgr.addStateObject("ARM_DIRECT_TO", "PAYMENT_APPLET");
	  return 0;
  }
  
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int BUY_BACK(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CANCEL(IApplicationManager theAppMgr)
      throws StateException {
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
    return 2;
  }
}

