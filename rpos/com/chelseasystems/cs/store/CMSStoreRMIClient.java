/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,chelseamarketsystem


package com.chelseasystems.cs.store;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;

import java.rmi.*;
import java.util.Date;

import com.chelseasystems.cr.store.*;

import java.lang.*;


/**
 *
 * <p>Title: CMSStoreRMIClient</p>
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
public class CMSStoreRMIClient extends CMSStoreServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSStoreRMIServer cmsstoreServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   **/
  public CMSStoreRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("store.cfg");
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
      System.out.println("CMSStoreRMIClient Lookup: Complete");
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
    cmsstoreServer = (ICMSStoreRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsstoreServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**search a store by its id
   *@parm storeId storeId
   **/
  public Store findById(String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findById((String)storeId);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }
  
  /**Added for Expiry_DATE issue on Nov 22,2016 
   *@parm storeId storeId
   **/
  public Store findByStoreId(String storeId,java.sql.Date process_dt)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findByStoreId((String)storeId,(java.sql.Date)process_dt);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }
  //ends here

  /**search store for a city
   *@parm city city
   **/
  public Store[] findByCity(String city)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findByCity((String)city);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**search store form a given state
   *@parm state state
   **/
  public Store[] findByState(String state)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findByState((String)state);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**search stores for the given state and city
   *@parm city city
   *@parm state state
   **/
  public Store[] findByCityAndState(String city, String state)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findByCityAndState((String)city, (String)state);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**
   *
   * @return CMSStore[]
   * @throws Exception
   */
  public CMSStore[] findAllStores()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findAllStores();
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**
   *
   * @return Store[]
   * @throws Exception
   */
  public Store[] findAll()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findAll();
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**find stroes ids for the city
   *@parm city city
   **/
  public String[] findIdsByCity(String city)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findIdsByCity((String)city);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**find stores ids for the state
   *@parm state state
   **/
  public String[] findIdsByState(String state)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findIdsByState((String)state);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**find store ids in the city and state.
   *@parm city city
   *@parm state state
   **/
  public String[] findIdsByCityAndState(String city, String state)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findIdsByCityAndState((String)city, (String)state);
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }

  /**
   *
   * @return String[]
   * @throws Exception
   */
  public String[] findAllIds()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsstoreServer == null)
        init();
      try {
        return cmsstoreServer.findAllIds();
      } catch (ConnectException ce) {
        cmsstoreServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSStoreServices");
  }


}

