/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.menu;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * put your documentation comment here
 */
public class ReportsMgmtMenuApplet_State {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CANCEL(IApplicationManager theAppMgr)
      throws StateException {
    //
    // put state logic here
    //
    throw new StateException("State change not implemented->CANCEL");
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
  public int SALES_BY_CONS_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 0;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SALES_BY_DEPT_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SALES_BY_CLASS_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 2;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SALES_BY_ITEM_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 3;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SALES_BY_MEDIA_REPROT(IApplicationManager theAppMgr)
      throws StateException {
    return 4;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int OVER_SHORT_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 5;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int SALES_BY_PERIOD_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 6;
  }

  //    public int SALES_BY_TXN_TYPE_REPROT(IApplicationManager theAppMgr) throws StateException {
  //        return 7;
  //    }
  public int COSIGNMENT_DETAIL_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 7;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int COSIGNMENT_AGEING_REPORT(IApplicationManager theAppMgr)
      throws StateException {
    return 8;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int ADHOC_QUERY(IApplicationManager theAppMgr)
      throws StateException {
    return 9;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
//    theAppMgr.goBack();
    return 10;
  }
  
  /**
   * @param theAppMgr
   * @return
   * @throws StateException
   */
  public int RESERVATION_DETAIL_REPORT(IApplicationManager theAppMgr)
  	  throws StateException {
	return 11;
  }
  
  /**
   * @param theAppMgr
   * @return
   * @throws StateException
   */
  public int PRESALE_DETAIL_REPORT(IApplicationManager theAppMgr)
  	  throws StateException {
	return 12;
  }
}

