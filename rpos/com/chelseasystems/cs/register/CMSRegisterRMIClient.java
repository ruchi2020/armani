/** Copyright 2001,chelseamarketsystem
 *  History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method selectByStoreAndRegID
 |      |            |           |           |   selectByStoreID                               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.register;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;

import java.rmi.*;
import java.util.HashMap;

import com.chelseasystems.cr.register.*;

import java.lang.*;

import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;


/**
 *
 * <p>Title: CMSRegisterRMIClient</p>
 *
 * <p>Description: The client-side of an RMI connection for fetching/submitting.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRegisterRMIClient extends CMSRegisterServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSRegisterRMIServer cmsregisterServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   **/
  public CMSRegisterRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("register.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * Get the remote interface from the registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSRegisterRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the update.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * Perform lookup of remote server.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmsregisterServer = (ICMSRegisterRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsregisterServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * Method is used to get the next register
   *@parm store store
   **/
  public Register getNextRegister(Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsregisterServer == null)
        init();
      try {
        return cmsregisterServer.getNextRegister((CMSStore)store);
      } catch (ConnectException ce) {
        cmsregisterServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRegisterServices");
  }

  /**
   * Method is used to update a register
   * @param register register
   */
  public void updateRegister(Register register)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsregisterServer == null)
        init();
      try {
        cmsregisterServer.updateRegister((CMSRegister)register);
        return;
      } catch (ConnectException ce) {
        cmsregisterServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRegisterServices");
  }

  /**
   * Method is used to Select a register
   * @param register register
   */
  public CMSRegister selectByStoreAndRegID(String register, String store)
      throws Exception {
    CMSRegister reg = null;
    for (int x = 0; x < maxTries; x++) {
      if (cmsregisterServer == null)
        init();
      try {
        reg = cmsregisterServer.selectByStoreAndRegID(register, store);
        return reg;
      } catch (ConnectException ce) {
        cmsregisterServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRegisterServices");
  }

  /**
   * Method is used to Select a register
   * @param register register
   */
  public CMSRegister[] selectByStoreID(String store)
      throws Exception {
    CMSRegister reg[] = null;
    for (int x = 0; x < maxTries; x++) {
      if (cmsregisterServer == null)
        init();
      try {
        reg = cmsregisterServer.selectByStoreID(store);
        return reg;
      } catch (ConnectException ce) {
        cmsregisterServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRegisterServices");
  }
  
  
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
	public HashMap submitAndReturnNetAmount(Object txnObject) throws Exception {
		HashMap map = null;
	    for (int x = 0; x < maxTries; x++) {
	      if (cmsregisterServer == null)
	        init();
	      try {
	        map = cmsregisterServer.submitAndReturnNetAmount(txnObject);
	        return map;
	      } catch (ConnectException ce) {
	    	  cmsregisterServer = null;
	      } catch (Exception ex) {
	        throw new DowntimeException(ex.getMessage());
	      }
	    }
	    throw new DowntimeException("Unable to establish connection to CMSEopServices");
	}

}

