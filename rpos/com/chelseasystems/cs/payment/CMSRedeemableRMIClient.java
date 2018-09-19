/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,Chelsea Market Systems


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;
import java.rmi.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import java.lang.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;


/**
 *
 * <p>Title: CMSRedeemableRMIClient</p>
 *
 * <p>Description: This class deal with client-side of an RMI connection for
 * fetching/submitting employee object</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRedeemableRMIClient extends CMSRedeemableServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSRedeemableRMIServer cmsredeemableServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * This method set the configuration manager and make sure that the system
   * has a security manager set.
   * @throws DowntimeException
   */
  public CMSRedeemableRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("redeemable.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   * @throws DowntimeException
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSRedeemableRMIClient Lookup: Complete");
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
   * This method is used to lookup the remote object from the RMI registry.
   * @throws Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmsredeemableServer = (ICMSRedeemableRMIServer)NamingService.lookup(connect);
  }

  /**
   * This method is used to check whether remote server is available or not
   * @return boolean
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsredeemableServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * This method finds a redeemable by control number
   * @param controlNumber String
   * @return Redeemable
   * @throws Exception
   */
  public Redeemable findRedeemable(String controlNumber)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findRedeemableByControlNumber((String)controlNumber);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * This method adds a new redeemable. This method should be called each time a
   * redeemable is purchased
   * @param redeemable Redeemable
   * @throws Exception
   */
  public void addRedeemable(Redeemable redeemable)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        cmsredeemableServer.addRedeemable((Redeemable)redeemable);
        return;
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * This method finds a gift certificate
   * @param id String
   * @return StoreValueCard
   * @throws Exception
   */
  public StoreValueCard findStoreValueCard(String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findStoreValueCard((String)id);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw
    new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /** Changes made by Satin for CMSCoupon
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barCode String
   * @param storeId String
   * @throws Exception
   * @return CMSCoupon
   */
  public CMSCoupon findByBarcodeAndStoreId(String barcode, String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findByBarcodeAndStoreId(barcode, storeId);
      } catch (ConnectException ce) {
    	  cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }
  
  
  
  /**
   * This method finds a gift certificate
   * @param id String
   * @return GiftCert
   * @throws Exception
   */
  public GiftCert findGiftCert(String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findGiftCert((String)id);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  //ks: to be deleted
  //  public HouseAccount findHouseAccount(String id) throws Exception {
  //      for (int x = 0; x< maxTries; x++) {
  //         if (cmsredeemableServer == null)
  //            init();
  //         try {
  //            return cmsredeemableServer.findHouseAccount((String) id);
  //         } catch (ConnectException ce) {
  //            cmsredeemableServer = null;
  //         } catch (Exception ex) {
  //            throw new DowntimeException(ex.getMessage());
  //         }
  //      }
  //      throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  //     }
  /**
   * Method finds redeemables on the basis of customer id
   * @param CustomerId String
   * @return Redeemable[]
   * @throws Exception
   */
  public Redeemable[] findByCustomerId(String CustomerId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findByCustomerId((String)CustomerId);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * Method finds house account on the basis of customer id
   * @param CustomerId String
   * @throws Exception
   * @return Redeemable[]
   */
  public Redeemable[] findHouseAccountByCustomerId(String CustomerId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findHouseAccountByCustomerId((String)CustomerId);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * Method finds redeemables on the basis of type and id
   * @param type String
   * @param id String
   * @throws Exception
   * @return Redeemable
   */
  public Redeemable findRedeemableById(String type, String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findRedeemableById(type, id);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param gift GiftCert
   * @throws Exception
   */
  public void addGiftCert(GiftCert gift)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        cmsredeemableServer.addGiftCert((GiftCert)gift);
        return;
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * This methods updated an exiting gift certificate
   * @param aTxn CompositePOSTransaction
   * @throws Exception
   */
  public void updateGiftCert(CompositePOSTransaction aTxn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        cmsredeemableServer.updateGiftCert((CMSCompositePOSTransaction)aTxn);
        return;
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * Finds a due bill based on an id
   * @param id String
   * @return DueBill
   * @throws Exception
   */
  public DueBill findDueBill(String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.findDueBill((String)id);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * Adds a new due bill.  This method should be called when a due bill is issued
   * @param aDueBill DueBill
   * @throws Exception
   */
  public void addDueBill(DueBill aDueBill)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        cmsredeemableServer.addDueBill((DueBill)aDueBill);
        return;
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * Method Updates an existing due bill
   * @param aTxn CompositePOSTransaction
   * @throws Exception
   */
  public void updateDueBill(CompositePOSTransaction aTxn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        cmsredeemableServer.updateDueBill((CMSCompositePOSTransaction)aTxn);
        return;
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }

  /**
   * Method check if the new Gift Cert control number is valid
   * @param newControlNumber String
   * @return boolean
   * @throws Exception
   */
  public boolean isNewGiftCertControlNumberValid(String newControlNumber)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsredeemableServer == null)
        init();
      try {
        return cmsredeemableServer.isNewGiftCertControlNumberValid((String)newControlNumber);
      } catch (ConnectException ce) {
        cmsredeemableServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSRedeemableServices");
  }


}
