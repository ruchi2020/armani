/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2006 | Sandhya   | N/A       | Reservation Details Report                         |
 -------------------------------------------------------------------------------------------------- 
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.swing.report.reservationdetails;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.state.StateException;

/**
 * put your documentation comment here
 */
public class ReservationDetailsReportApplet_State {

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
}

