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

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.igray.naming.*;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import com.chelseasystems.cr.register.*;

import java.lang.*;

import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;


/**
 *
 **/
/**
 *
 * <p>Title: CMSRegisterRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * fetching/submitting information. This class delgates all method calls to the
 * object referenced by the return value from CMSRegisterServices.getCurrent()</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRegisterRMIServerImpl extends CMSComponent implements ICMSRegisterRMIServer {

  /**
   *
   * @param props Properties
   * @throws RemoteException
   */
  public CMSRegisterRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * sets the current implementation
   **/
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure register contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSRegisterServices.setCurrent((CMSRegisterServices)obj);
  }

  /**
   *
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure register contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * Used by the DowntimeManager to determine when this object is available.
   * Just because this process is up doesn't mean that the clients can come up.
   * Make sure that the database is available.
   * @return boolean <code>true</code> indicates that this class is available.
   **/
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * Method is used to get the next register
   *@parm store store
   **/
  public CMSRegister getNextRegister(CMSStore store)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSRegister)CMSRegisterServices.getCurrent().getNextRegister(store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getNextRegister", start);
      decConnection();
    }
  }

  /**
   * Method is used to update a register
   * @param register register
   */
  public void updateRegister(CMSRegister register)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      CMSRegisterServices.getCurrent().updateRegister(register);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateRegister", start);
      decConnection();
    }
  }

  /**
   * Method is used to Set a register
   * @param register register
   */
  public CMSRegister selectByStoreAndRegID(String register, String store)
      throws RemoteException {
    long start = getStartTime();
    CMSRegister reg = null;
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      reg = ((CMSRegisterServices)CMSRegisterServices.getCurrent()).selectByStoreAndRegID(register
          , store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("selectByStoreAndRegID", start);
      decConnection();
    }
    return reg;
  }

  /**
   * Method is used to Get all register
   * @param register register
   */
  public CMSRegister[] selectByStoreID(String store)
      throws RemoteException {
    long start = getStartTime();
    CMSRegister reg[] = null;
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      reg = ((CMSRegisterServices)CMSRegisterServices.getCurrent()).selectByStoreID(store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("selectByStoreID", start);
      decConnection();
    }
    return reg;
  }
  
  
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
  public HashMap submitAndReturnNetAmount(Object txnObject) throws RemoteException {
	    long start = getStartTime();
	    HashMap hashmap = null;
	    try {
	      if (!isAvailable())
	        throw new ConnectException("Service is not available");
	      incConnection();
	      hashmap = ((CMSRegisterServices)CMSRegisterServices.getCurrent()).submitAndReturnNetAmount(txnObject);
	    } catch (Exception e) {
	      throw new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("submitAndReturnNetAmount", start);
	      decConnection();
	    }
	    return hashmap;
		
	}
}

