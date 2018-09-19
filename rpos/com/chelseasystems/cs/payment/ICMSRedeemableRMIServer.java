/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import java.rmi.*;
import com.igray.naming.*;
import com.chelseasystems.cr.payment.*;
import java.lang.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;


/**
 *
 * <p>Title: ICMSRedeemableRMIServer</p>
 *
 * <p>Description: Defines the customer services that are available remotely via RMI.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ICMSRedeemableRMIServer extends Remote, IPing {

  /**
   * This method finds any redeemable with a matching control number
   * @param controlNumber String
   * @return Redeemable
   * @throws Exception
   */
  public Redeemable findRedeemableByControlNumber(String controlNumber)
      throws Exception;


  /**
   * This method adds a new redeemable. This method should be called each time
   * a redeemable is purchased
   * @param redeemable Redeemable
   * @throws Exception
   */
  public void addRedeemable(Redeemable redeemable)
      throws Exception;


  /**
   * Added by Satin.
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barCode String
   * @param storeId String
   * @return CMSCoupon
   * @throws RemoteException
   */
  public CMSCoupon findByBarcodeAndStoreId(String barcode, String storeId)
      throws RemoteException;
  
  
  
  /**
   * This method finds a store value card
   * @param id String
   * @return StoreValueCard
   * @throws RemoteException
   */
  public StoreValueCard findStoreValueCard(String id)
      throws RemoteException;


  /**
   * This method finds a gift certificate
   * @param id String
   * @return GiftCert
   * @throws RemoteException
   */
  public GiftCert findGiftCert(String id)
      throws RemoteException;


  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param gift GiftCert
   * @throws RemoteException
   */
  public void addGiftCert(GiftCert gift)
      throws RemoteException;


  /**
   * This methods updated an exiting gift certificate
   * @param aTxn CMSCompositePOSTransaction
   * @throws RemoteException
   */
  public void updateGiftCert(CMSCompositePOSTransaction aTxn)
      throws RemoteException;


  /**
   * This method finds a due bill based on an id
   * @param id String
   * @return DueBill
   * @throws RemoteException
   */
  public DueBill findDueBill(String id)
      throws RemoteException;


  /**
   * This method Adds a new due bill.  This method should be called when a
   * due bill is issued
   * @param aDueBill DueBill
   * @throws RemoteException
   */
  public void addDueBill(DueBill aDueBill)
      throws RemoteException;


  /**
   * This method Updated an existing due bill
   * @param aTxn CMSCompositePOSTransaction
   * @throws RemoteException
   */
  public void updateDueBill(CMSCompositePOSTransaction aTxn)
      throws RemoteException;


  /**
   * This method check if the new Gift Cert control number is valid
   * @param newControlNumber String
   * @return boolean
   * @throws RemoteException
   */
  public boolean isNewGiftCertControlNumberValid(String newControlNumber)
      throws RemoteException;


  /**
   *
   * @param id String
   * @throws RemoteException
   * @return HouseAccount
   */
  //  public HouseAccount findHouseAccount(String id) throws RemoteException;
  /**
   * This method finds redeemables by customer ID
   * @param customerId String
   * @throws RemoteException
   * @return Redeemable[]
   */
  public Redeemable[] findByCustomerId(String customerId)
      throws RemoteException;


  /**
   * This method finds house account by its customer ID
   * @param customerid String
   * @throws RemoteException
   * @return Redeemable[]
   */
  public Redeemable[] findHouseAccountByCustomerId(String customerid)
      throws RemoteException;


  /**
   * This method finds redeemable by its ID
   * @param type String
   * @param id String
   * @throws RemoteException
   * @return Redeemable
   */
  public Redeemable findRedeemableById(String type, String id)
      throws RemoteException;






}
