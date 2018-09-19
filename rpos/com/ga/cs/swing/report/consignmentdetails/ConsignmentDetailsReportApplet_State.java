/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.consignmentdetails;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.state.StateException;


/**
 * put your documentation comment here
 */
public class ConsignmentDetailsReportApplet_State {

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
  public int CANCEL(IApplicationManager theAppMgr)
      throws StateException {
    theAppMgr.goHome();
    return -1;
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
  public int SELL_THROUGH(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SELL_THROUGH_BY_ASSOCIATE(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int ALL_CONSIGNMENT_DETAIL(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CLOSED_CONSIGNMENT_DETAIL_ONLY(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int OPEN_CONSIGNMENT_DETAIL_ONLY(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int ALL_CONSIGNMENT_DETAIL_BY_ASSOCIATE(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CLOSED_CONSIGNMENT_DETAIL_BY_ASSOCIATE(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int OPEN_CONSIGNMENT_DETAIL_BY_ASSOCIATE(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
  
}

