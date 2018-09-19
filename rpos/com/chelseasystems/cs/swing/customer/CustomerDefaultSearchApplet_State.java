package com.chelseasystems.cs.swing.customer;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.state.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Paresh Mondkar
 * @version 1.0
 */

public class CustomerDefaultSearchApplet_State {
	  /**
	   * put your documentation comment here
	   * @param theAppMgr
	   * @return
	   * @exception StateException
	   */
	  public int SEARCH(IApplicationManager theAppMgr)
	      throws StateException {
	    return 0;
	  }
	  /**
	   * put your documentation comment here
	   * @param theAppMgr
	   * @return
	   * @exception StateException
	   */
	  public int NEW_SEARCH(IApplicationManager theAppMgr)
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
		  return 2;
	  }	  
}
