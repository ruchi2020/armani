/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ViewCustomerAddressApplet_State {

  /**
   * put your documentation comment here
   */
  public ViewCustomerAddressApplet_State() {
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int CANCEL(IApplicationManager theAppMgr)
      throws StateException {
    if(theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE")!=null)
    {
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
  public int PREV(IApplicationManager theAppMgr)
      throws StateException {
    if(theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE")!=null)
    {
      return 1;
     }
    //theAppMgr.goBack();
    return 0;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int ADD_ADDRESS(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int USE_ADDRESS(IApplicationManager theAppMgr)
      throws StateException {
    if(theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE")!=null)
    {
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
  public int MODIFY_ADDRESS(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @return
   * @exception StateException
   */
  public int REMOVE_ADDRESS(IApplicationManager theAppMgr)
      throws StateException {
    return -1;
  }
}

