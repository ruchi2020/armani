/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,chelseamarketsystem


package com.chelseasystems.cs.store;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.igray.naming.*;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import com.chelseasystems.cr.store.*;

import java.lang.*;


/**
 *
 * <p>Title: CMSStoreRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * fetching/submitting information.  This class delgates all method calls to
 * the object referenced by the return value from CMSStoreServices.getCurrent().</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSStoreRMIServerImpl extends CMSComponent implements ICMSStoreRMIServer {

  /**
   * put your documentation comment here
   * @param   Properties props
   */
  public CMSStoreRMIServerImpl(Properties props)
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
          , "Could not instantiate SERVER_IMPL.", "Make sure store.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSStoreServices.setCurrent((CMSStoreServices)obj);
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
          , "Make sure store.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
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

  /**search a store by its id
   *@parm storeId storeId
   **/
  public CMSStore findById(String storeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSStore)CMSStoreServices.getCurrent().findById(storeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }
  
  /**Added for Expiry_DATE issue on Nov 22,2016 
   *@parm storeId storeId
   **/
  public CMSStore findByStoreId(String storeId,java.sql.Date process_dt)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSStore)((CMSStoreServices) CMSStoreServices.getCurrent()).findByStoreId(storeId,process_dt);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByStoreId", start);
      decConnection();
    }
  }
  //ends here

  /**search store for a city
   *@parm city city
   **/
  public CMSStore[] findByCity(String city)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSStore[])CMSStoreServices.getCurrent().findByCity(city);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCity", start);
      decConnection();
    }
  }

  /**search store form a given state
   *@parm state state
   **/
  public CMSStore[] findByState(String state)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSStore[])CMSStoreServices.getCurrent().findByState(state);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByState", start);
      decConnection();
    }
  }

  /**search stores for the given state and city
   *@parm city city
   *@parm state state
   **/
  public CMSStore[] findByCityAndState(String city, String state)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSStore[])CMSStoreServices.getCurrent().findByCityAndState(city, state);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCityAndState", start);
      decConnection();
    }
  }

  /**
   *
   * @return CMSStore[]
   * @throws RemoteException
   */
  public CMSStore[] findAll()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSStore[])CMSStoreServices.getCurrent().findAll();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAll", start);
      decConnection();
    }
  }

  /**
   *
   * @return CMSStore[]
   * @throws RemoteException
   */
  public CMSStore[] findAllStores()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSStore[])((CMSStoreServices)CMSStoreServices.getCurrent()).findAllStores();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAll", start);
      decConnection();
    }
  }

  /**find stroes ids for the city
   *@parm city city
   **/
  public String[] findIdsByCity(String city)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String[])CMSStoreServices.getCurrent().findIdsByCity(city);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findIdsByCity", start);
      decConnection();
    }
  }

  /**find stores ids for the state
   *@parm state state
   **/
  public String[] findIdsByState(String state)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String[])CMSStoreServices.getCurrent().findIdsByState(state);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findIdsByState", start);
      decConnection();
    }
  }

  /**find store ids in the city and state.
   *@parm city city
   *@parm state state
   **/
  public String[] findIdsByCityAndState(String city, String state)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String[])CMSStoreServices.getCurrent().findIdsByCityAndState(city, state);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findIdsByCityAndState", start);
      decConnection();
    }
  }

  /**
   *
   * @return String[]
   * @throws RemoteException
   */
  public String[] findAllIds()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String[])CMSStoreServices.getCurrent().findAllIds();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllIds", start);
      decConnection();
    }
  }
}

