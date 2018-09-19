/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,Chelsea Market Systems


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import java.lang.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;


/**
 *
 * <p>Title: CMSRedeemableClientServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRedeemableClientServices extends ClientServices {

  /** Configuration manager **/
 // private ConfigMgr config = null;

  /**
   * Set the current implementation
   **/
  public CMSRedeemableClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("redeemable.cfg");
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
   * implementation from the payment.cfg and set the same in CMSRedeemableServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSRedeemableClientServices");
    CMSRedeemableServices serviceImpl = (CMSRedeemableServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSRedeemableClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSRedeemableServices in redeemable.cfg."
          , "Make sure that redeemable.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSRedeemableServices."
          , LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSRedeemableServices.setCurrent(serviceImpl);
  }

  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the payment.cfg and set the same in CMSRedeemableServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSRedeemableClientServices");
    CMSRedeemableServices serviceImpl = (CMSRedeemableServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSRedeemableClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSRedeemableServices in redeemable.cfg."
          , "Make sure that redeemable.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSRedeemableServices.", LoggingServices.CRITICAL);
    }
    CMSRedeemableServices.setCurrent(serviceImpl);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return CMSRedeemableServices.getCurrent();
  }

  /**
   * Method finds redemables on the basis of control number
   * @param controlNumber String
   * @return Redeemable
   * @throws Exception
   */
  public Redeemable findRedeemable(String controlNumber)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Redeemable)CMSRedeemableServices.getCurrent().findRedeemable(controlNumber);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findRedeemable"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Redeemable)CMSRedeemableServices.getCurrent().findRedeemable(controlNumber);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Method finds store value card on the basis of id
   * @param id String
   * @return StoreValueCard
   * @throws Exception
   */
  public StoreValueCard findStoreValueCard(String id)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (StoreValueCard)CMSRedeemableServices.getCurrent().findStoreValueCard(id);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findStoreValueCard"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (StoreValueCard)CMSRedeemableServices.getCurrent().findStoreValueCard(id);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  
  /**
   * Added by Satin
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barcode String, storeId String
   * @return CMSCoupon
   * @throws Exception
   */
  public CMSCoupon findByBarcodeAndStoreId(String barCode, String storeId)
	      throws Exception {
	    try {
	      this.fireWorkInProgressEvent(true);
	      return (CMSCoupon)CMSRedeemableServices.getCurrent().findByBarcodeAndStoreId(
	              barCode, storeId);
	    } catch (DowntimeException ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "findCoupon"
	          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
	          , "See Exception", LoggingServices.MAJOR, ex);
	      offLineMode();
	      setOffLineMode();
	      return (CMSCoupon)CMSRedeemableServices.getCurrent().findByBarcodeAndStoreId(barCode, storeId);
	    } finally {
	      this.fireWorkInProgressEvent(false);
	    }
	  }

   
  
  /**
   * Method finds gift certificates on the basis of id
   * @param id String
   * @return GiftCert
   * @throws Exception
   */
  public GiftCert findGiftCert(String id)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (GiftCert)CMSRedeemableServices.getCurrent().findGiftCert(id);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findGiftCert"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (GiftCert)CMSRedeemableServices.getCurrent().findGiftCert(id);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param gift GiftCert
   * @throws Exception
   */
  public void addGiftCert(GiftCert gift)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSRedeemableServices.getCurrent().addGiftCert(gift);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "addGiftCert"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      CMSRedeemableServices.getCurrent().addGiftCert(gift);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This methods updated an exiting gift certificate
   * @param aTxn CMSCompositePOSTransaction
   * @throws Exception
   */
  public void updateGiftCert(CMSCompositePOSTransaction aTxn)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSRedeemableServices.getCurrent().updateGiftCert(aTxn);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "updateGiftCert"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      CMSRedeemableServices.getCurrent().updateGiftCert(aTxn);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Method Finds a due bill based on an id
   * @param id String
   * @return DueBill
   * @throws Exception
   */
  public DueBill findDueBill(String id)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (DueBill)CMSRedeemableServices.getCurrent().findDueBill(id);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findDueBill"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (DueBill)CMSRedeemableServices.getCurrent().findDueBill(id);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Method Adds a new due bill.  This method should be called when a due bill is issued
   * @param aDueBill DueBill
   * @throws Exception
   */
  public void addDueBill(DueBill aDueBill)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSRedeemableServices.getCurrent().addDueBill(aDueBill);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "addDueBill"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      CMSRedeemableServices.getCurrent().addDueBill(aDueBill);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Method Updates an existing due bill
   * @param aTxn CMSCompositePOSTransaction
   * @throws Exception
   */
  public void updateDueBill(CMSCompositePOSTransaction aTxn)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSRedeemableServices.getCurrent().updateDueBill(aTxn);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "updateDueBill"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      CMSRedeemableServices.getCurrent().updateDueBill(aTxn);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Method checks if the new Gift Cert control number is valid
   * @param newControlNumber String
   * @return boolean
   * @throws Exception
   */
  public boolean isNewGiftCertControlNumberValid(String newControlNumber)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (boolean)CMSRedeemableServices.getCurrent().isNewGiftCertControlNumberValid(
          newControlNumber);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "isNewGiftCertControlNumberValid"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (boolean)CMSRedeemableServices.getCurrent().isNewGiftCertControlNumberValid(
          newControlNumber);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Method finds house account on the basis of customer id
   * @param id String
   * @throws Exception
   * @return Redeemable[]
   */
  public Redeemable[] findHouseAccountByCustomerId(String id)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Redeemable[])((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findHouseAccountByCustomerId(id);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findHouseAccount"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Redeemable[])((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findHouseAccountByCustomerId(id);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Method finds redeemables on the basis of id and type
   * @param type String
   * @param id String
   * @throws Exception
   * @return Redeemable
   */
  public Redeemable findRedeemableById(String type, String id)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Redeemable)((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findRedeemableById(type, id);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findHouseAccount"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Redeemable)((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findRedeemableById(type, id);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method finds redeemables on the basis of customer id
   * @param customerId String
   * @throws Exception
   * @return Redeemable
   */
  public Redeemable[] findByCustomerId(String customerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Redeemable[])((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findByCustomerId(customerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCustomerId"
          , "Primary Implementation for CMSRedeemableServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Redeemable[])((CMSRedeemableServices)CMSRedeemableServices.getCurrent()).
          findByCustomerId(customerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
}
