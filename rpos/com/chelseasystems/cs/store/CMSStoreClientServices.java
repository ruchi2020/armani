/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.store;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.store.*;

import java.lang.*;
import java.util.Date;


/**
 *
 * <p>Title: CMSStoreClientServices</p>
 *
 * <p>Description: Client-side object for retrieving and submitting.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSStoreClientServices extends ClientServices {

  /** Configuration manager **/
//  private ConfigMgr config = null;

  /**
   * Set the current implementation
   */
  public CMSStoreClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("store.cfg");
  }

  /**
   * This method initialize the primary implementation
   */
  public void init(boolean online)
      throws Exception {
    // Set up the proper implementation of the service.
    if (online)
      onLineMode();
    else
      offLineMode();
  }

  /**
   * Reads "CLIENT_IMPL" from config file. Returns the class that defines
   * what object is providing the service to objects using this client service
   * in "on-line" mode, i.e. connected to an app server.  If null, this 
   * clientservice is not considered when determining app online status.
   * @return a class of the online service.
   */
  protected Class getOnlineService () throws ClassNotFoundException {
    String className = config.getString("CLIENT_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }

  /**
   * This method is invoked when system is online, to get the client remote
   * implementation from the store.cfg and set the same in CMSStoreServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSStoreClientServices");
    CMSStoreServices serviceImpl = (CMSStoreServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSStoreClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSStoreServices in store.cfg."
          , "Make sure that store.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSStoreServices.", LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSStoreServices.setCurrent(serviceImpl);
  }

  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the store.cfg and set the same in CMSStoreServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSStoreClientServices");
    CMSStoreServices serviceImpl = (CMSStoreServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSStoreClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSStoreServices in store.cfg."
          , "Make sure that store.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSStoreServices.", LoggingServices.CRITICAL);
    }
    CMSStoreServices.setCurrent(serviceImpl);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return CMSStoreServices.getCurrent();
  }

  /**
   * search a store by its id
   * @param storeId storeId
   */
  public CMSStore findById(String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSStore)CMSStoreServices.getCurrent().findById(storeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findById"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSStore)CMSStoreServices.getCurrent().findById(storeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  /**Poonam S.
   * Added for Expiry_DATE issue on Nov 22,2016 
   * @param storeId storeId
   */
  public CMSStore findByStoreId(String storeId,java.sql.Date process_dt)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSStore)((CMSStoreServices) CMSStoreServices.getCurrent()).findByStoreId(storeId,process_dt);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByStoreId"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSStore)((CMSStoreServices) CMSStoreServices.getCurrent()).findByStoreId(storeId,process_dt);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  //ends here

  /**
   * search store for a city
   * @param city city
   */
  public CMSStore[] findByCity(String city)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSStore[])CMSStoreServices.getCurrent().findByCity(city);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCity"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSStore[])CMSStoreServices.getCurrent().findByCity(city);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * search store form a given state
   * @param state state
   */
  public CMSStore[] findByState(String state)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSStore[])CMSStoreServices.getCurrent().findByState(state);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByState"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSStore[])CMSStoreServices.getCurrent().findByState(state);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * search stores for the given state and city
   * @param city city
   * @param state state
   */
  public CMSStore[] findByCityAndState(String city, String state)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSStore[])CMSStoreServices.getCurrent().findByCityAndState(city, state);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCityAndState"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSStore[])CMSStoreServices.getCurrent().findByCityAndState(city, state);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *
   * @return CMSStore[]
   * @throws Exception
   */
  public CMSStore[] findAllStores()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSStore[])((CMSStoreServices)CMSStoreServices.getCurrent()).findAllStores();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAll"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSStore[])((CMSStoreServices)CMSStoreServices.getCurrent()).findAllStores();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * @return
   * @exception Exception
   */
  public CMSStore[] findAll()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSStore[])CMSStoreServices.getCurrent().findAll();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAll"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSStore[])CMSStoreServices.getCurrent().findAll();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * find stroes ids for the city
   * @param city city
   */
  public String[] findIdsByCity(String city)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (String[])CMSStoreServices.getCurrent().findIdsByCity(city);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findIdsByCity"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (String[])CMSStoreServices.getCurrent().findIdsByCity(city);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * find stores ids for the state
   * @param state state
   */
  public String[] findIdsByState(String state)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (String[])CMSStoreServices.getCurrent().findIdsByState(state);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findIdsByState"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (String[])CMSStoreServices.getCurrent().findIdsByState(state);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * find store ids in the city and state.
   * @param city city
   * @param state state
   */
  public String[] findIdsByCityAndState(String city, String state)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (String[])CMSStoreServices.getCurrent().findIdsByCityAndState(city, state);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findIdsByCityAndState"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (String[])CMSStoreServices.getCurrent().findIdsByCityAndState(city, state);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *
   * @return String[]
   * @throws Exception
   */
  public String[] findAllIds()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (String[])CMSStoreServices.getCurrent().findAllIds();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAllIds"
          , "Primary Implementation for CMSStoreServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (String[])CMSStoreServices.getCurrent().findAllIds();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
}

