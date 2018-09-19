/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,Chelsea Market Systems


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.igray.naming.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import java.lang.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;


/**
 *
 * <p>Title: CMSRedeemableRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * fetching/submitting information .  This class delgates all method calls to the
 * object referenced by the return value from CMSRedeemableServices.getLocal().</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRedeemableRMIServerImpl extends CMSComponent implements ICMSRedeemableRMIServer {

  /**
   * Constructor
   * @param props Properties
   * @throws RemoteException
   */
  public CMSRedeemableRMIServerImpl(Properties props)
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
          , "Could not instantiate SERVER_IMPL.", "Make sure redeemable.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSRedeemableServices.setCurrent((CMSRedeemableServices)obj);
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
          , "Make sure redeemable.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * This method receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * This method is used by the DowntimeManager to determine when this object is
   * available.
   * @return boolean <code>true</code> indicates that this class is available.
   * @throws RemoteException
   */
 
   
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * This method finds a redeemable by control number
   * @param controlNumber String
   * @return Redeemable
   * @throws RemoteException
   */
  public Redeemable findRedeemableByControlNumber(String controlNumber)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (Redeemable)CMSRedeemableServices.getCurrent().findRedeemable(controlNumber);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findRedeemable", start);
      decConnection();
    }
  }

  /**
   * This method add a new redeemable. This method should be called each time a
   * redeemable is purchased
   * @param redeemable Redeemable
   * @throws RemoteException
   */
  public void addRedeemable(Redeemable redeemable)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      CMSRedeemableServices.getCurrent().addRedeemable(redeemable);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("addRedeemable", start);
      decConnection();
    }
  }

  /**
   * This method finds a store value card
   * @param id String
   * @return StoreValueCard
   * @throws RemoteException
   */
  public StoreValueCard findStoreValueCard(String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (StoreValueCard)CMSRedeemableServices.getCurrent().findStoreValueCard(id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findStoreValueCard", start);
      decConnection();
    }
  }

  //ks: to be deleted
  /*   public HouseAccount findHouseAccount(String id) throws RemoteException {
   long start = getStartTime();
   try {
   if (!isAvailable())
   throw  new ConnectException("Service is not available");
   incConnection();
   return (HouseAccount) ((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).findHouseAccount(id);
   } catch (Exception e) {
   throw new RemoteException(e.getMessage(), e);
   } finally {
   addPerformance("findHouseAccount", start);
   decConnection();
   }
   }*/
  
  
  /** Change made by Satin for CMSCoupon
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barCode String
   * @param storeId String
   * @return CMSCoupon
   * @throws RemoteException
   */
  public CMSCoupon findByBarcodeAndStoreId(String barcode, String storeId)
    throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSCoupon)CMSRedeemableServices.getCurrent().findByBarcodeAndStoreId(barcode, storeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByBarcodeAndStoreId", start);
      decConnection();
    }
  }
  
  
  
  /**
   * Method used to find redeemables on the basis of customer id
   * @param customerId String
   * @throws RemoteException
   * @return Redeemable[]
   */
  public Redeemable[] findByCustomerId(String customerId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (Redeemable[])((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findByCustomerId(customerId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCustomerId", start);
      decConnection();
    }
  }

  /**
   * Method used to find house accounts on the basis of customer id
   * @param customerid String
   * @throws RemoteException
   * @return Redeemable[]
   */
  public Redeemable[] findHouseAccountByCustomerId(String customerid)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (Redeemable[])((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findHouseAccountByCustomerId(customerid);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findHouseAccountByCustomerId", start);
      decConnection();
    }
  }

  /**
   * Method used to find redeemables on the basis of id
   * @param type String
   * @param id String
   * @throws RemoteException
   * @return Redeemable
   */
  public Redeemable findRedeemableById(String type, String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (Redeemable)((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findRedeemableById(type, id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findRedeemableById", start);
      decConnection();
    }
  }

  /**
   * This method finds a gift certificate
   * @param id String
   * @return GiftCert
   * @throws RemoteException
   */
  public GiftCert findGiftCert(String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (GiftCert)CMSRedeemableServices.getCurrent().findGiftCert(id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findGiftCert", start);
      decConnection();
    }
  }

  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param gift GiftCert
   * @throws RemoteException
   */
  public void addGiftCert(GiftCert gift)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      CMSRedeemableServices.getCurrent().addGiftCert(gift);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("addGiftCert", start);
      decConnection();
    }
  }

  /**
   * This methods updated an exiting gift certificate
   * @param aTxn CMSCompositePOSTransaction
   * @throws RemoteException
   */
  public void updateGiftCert(CMSCompositePOSTransaction aTxn)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      CMSRedeemableServices.getCurrent().updateGiftCert(aTxn);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateGiftCert", start);
      decConnection();
    }
  }

  /**
   * Method finds a due bill based on an id
   * @param id String
   * @return DueBill
   * @throws RemoteException
   */
  public DueBill findDueBill(String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (DueBill)CMSRedeemableServices.getCurrent().findDueBill(id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findDueBill", start);
      decConnection();
    }
  }

  /**
   * Method Adds a new due bill.  This method should be called when a due bill
   * is issued
   * @param aDueBill DueBill
   * @throws RemoteException
   */
  public void addDueBill(DueBill aDueBill)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      CMSRedeemableServices.getCurrent().addDueBill(aDueBill);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("addDueBill", start);
      decConnection();
    }
  }

  /**
   * Method Updated an existing due bill
   * @param aTxn CMSCompositePOSTransaction
   * @throws RemoteException
   */
  public void updateDueBill(CMSCompositePOSTransaction aTxn)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      CMSRedeemableServices.getCurrent().updateDueBill(aTxn);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateDueBill", start);
      decConnection();
    }
  }

  /**
   * Method checks if the new Gift Cert control number is valid
   * @param newControlNumber String
   * @return boolean
   * @throws RemoteException
   */
  public boolean isNewGiftCertControlNumberValid(String newControlNumber)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (boolean)CMSRedeemableServices.getCurrent().isNewGiftCertControlNumberValid(
          newControlNumber);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("isNewGiftCertControlNumberValid", start);
      decConnection();
    }
  }
}
