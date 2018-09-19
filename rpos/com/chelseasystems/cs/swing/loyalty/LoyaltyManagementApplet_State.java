/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

import com.chelseasystems.cr.appmgr.state.StateException;
import com.chelseasystems.cr.appmgr.IApplicationManager;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LoyaltyManagementApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.removeStateObject("LOYALTYFORREISSUE");
    if (theAppMgr.getStateObject("PREV_LOYALTY") != null) {
      theAppMgr.removeStateObject("PREV_LOYALTY");
      return 2;
    }
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int LOYALTY_DETAILS(IApplicationManager theAppMgr)
      throws StateException {
    if (theAppMgr.getStateObject("LOYALTY") != null) {
      return 0;
    } else if (theAppMgr.getStateObject("REWARDVAL") != null) {
      return 1;
    }
    return 0;
  }
  
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int LOYALTY_CARD_ACTIVE(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int LOYALTY_CARD_INACTIVE(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  //  public int  (IApplicationManager theAppMgr) throws StateException
  //   {
  //     return 0;
  //  }
}

