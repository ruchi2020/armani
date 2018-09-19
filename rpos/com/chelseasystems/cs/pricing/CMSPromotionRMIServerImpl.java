/*
 * @copyright (c) 2003 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.igray.naming.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;


/**
 *
 * <p>Title: CMSPromotionRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for fetching/submitting
 * information.  This class delgates all method calls to the object referenced
 * by the return value from CMSPromotionServices.getCurrent(). </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSPromotionRMIServerImpl extends CMSComponent implements ICMSPromotionRMIServer {

  /**
   * Constructor
   * @param props Properties
   * @throws RemoteException
   */
  public CMSPromotionRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * This method is used to set the server side implementation
   */
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure promotion.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSPromotionServices.setCurrent((CMSPromotionServices)obj);
  }

  /**
   * This method is used to bind employee object to RMI registery
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure promotion.cfg contains a REMOTE_NAME entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * This method receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * This method is used by the DowntimeManager to determine when this object
   * is available.
   * @return boolean <code>true</code> indicates that this class is available.
   */
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * Method to get an IPromotion by Id.
   * @param id String
   * @return IPromotion
   * @throws RemoteException
   */
  public IPromotion findById(String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (IPromotion)CMSPromotionServices.getCurrent().findById(id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById(String)", start);
      decConnection();
    }
  }

  /**
   * Method to return all IPromotion entries for a specified Store.
   * @param aStore Store
   * @return IPromotion[]
   * @throws RemoteException
   */
  public IPromotion[] findAllForStore(Store aStore)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (IPromotion[])CMSPromotionServices.getCurrent().findAllForStore(aStore);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllForStore(Store)", start);
      decConnection();
    }
  }

  /**
   * Method to get a ThresholdPromotion by Id.
   * @param id String
   * @return ThresholdPromotion
   * @throws RemoteException
   */
  public ThresholdPromotion findThresholdPromotionById(String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (ThresholdPromotion)CMSPromotionServices.getCurrent().findThresholdPromotionById(id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findThresholdPromotionById(String)", start);
      decConnection();
    }
  }

  /**
   * Method to return all ThresholdPromotions for a Store.
   * @param aStore Store
   * @return ThresholdPromotion[]
   * @throws RemoteException
   */
  public ThresholdPromotion[] findThresholdPromotionsForStore(Store aStore)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (ThresholdPromotion[])CMSPromotionServices.getCurrent().
          findThresholdPromotionsForStore(aStore);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findThresholdPromotionsForStore(Store)", start);
      decConnection();
    }
  }

  /**
   * Insert specified IPromotion into storage.
   * @param promotion IPromotion
   * @throws RemoteException
   */
  public void insert(IPromotion promotion)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      CMSPromotionServices.getCurrent().insert(promotion);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("insert(IPromotion)", start);
      decConnection();
    }
  }
}

