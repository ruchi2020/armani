/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;


/**
 * Static convenience methods to manipulate Client Services.
 */
/**
 *
 * <p>Title: CMSRedeemableHelper</p>
 *
 * <p>Description: This class have static convenience methods to manipulate
 * Client Services</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRedeemableHelper {

  /**
   * Method finds redeemables on the basis of control number
   * @param theAppMgr IRepositoryManager
   * @param controlNumber String
   * @return Redeemable
   * @throws Exception
   */
  public static Redeemable findRedeemable(IRepositoryManager theAppMgr, String controlNumber)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return cs.findRedeemable(controlNumber);
  }

  /**
   * This method finds a store value card
   * @param id
   * @exception Exception
   */
  public static StoreValueCard findStoreValueCard(IRepositoryManager theAppMgr, String id)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return cs.findStoreValueCard(id);
  }

  
  /**
   * Added by Satin
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barcode, storeId
   * @exception Exception
   */
  public static CMSCoupon findByBarcodeAndStoreId(IRepositoryManager theAppMgr, String barCode, String storeId) throws Exception {
	  CMSRedeemableClientServices cs = (CMSRedeemableClientServices) theAppMgr.getGlobalObject("REDEEMABLE_SRVC");
	  return cs.findByBarcodeAndStoreId(barCode, storeId);
  } 
  
    
  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param id
   * @exception Exception
   */
  public static GiftCert findGiftCert(IRepositoryManager theAppMgr, String id)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return cs.findGiftCert(id);
  }

  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param theAppMgr IRepositoryManager
   * @param gift GiftCert
   * @throws Exception
   */
  public static void addGiftCert(IRepositoryManager theAppMgr, GiftCert gift)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    cs.addGiftCert(gift);
  }

  /**
   * This methods updated an exiting gift certificate
   * @param theAppMgr IRepositoryManager
   * @param aTxn CMSCompositePOSTransaction
   * @throws Exception
   */
  public static void updateGiftCert(IRepositoryManager theAppMgr, CMSCompositePOSTransaction aTxn)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    cs.updateGiftCert(aTxn);
  }

  /**
   * Method finds a due bill based on an id
   * @param theAppMgr IRepositoryManager
   * @param id String
   * @return DueBill
   * @throws Exception
   */
  public static DueBill findDueBill(IRepositoryManager theAppMgr, String id)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return (DueBill)cs.findDueBill(id);
  }

  /**
   * Method Adds a new due bill.  This method should be called when a due bill
   * is issued
   * @param theAppMgr IRepositoryManager
   * @param aDueBill DueBill
   * @throws Exception
   */
  public static void addDueBill(IRepositoryManager theAppMgr, DueBill aDueBill)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    cs.addDueBill(aDueBill);
  }

  /**
   * Method Updated an existing due bill
   * @param theAppMgr IRepositoryManager
   * @param aTxn CMSCompositePOSTransaction
   * @throws Exception
   */
  public static void updateDueBill(IRepositoryManager theAppMgr, CMSCompositePOSTransaction aTxn)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    cs.updateDueBill(aTxn);
  }

  /**
   * Method checks whether new credit card control number is valid
   * @param theAppMgr IRepositoryManager
   * @param newControlNumber String
   * @return boolean
   * @throws Exception
   */
  public static boolean isNewGiftCertControlNumberValid(IRepositoryManager theAppMgr
      , String newControlNumber)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return cs.isNewGiftCertControlNumberValid(newControlNumber);
  }

  /**
   * Method finds house account by its id
   * @param theAppMgr IRepositoryManager
   * @param id String
   * @return HouseAccount
   * @throws Exception
   */
  public static HouseAccount findHouseAccount(IRepositoryManager theAppMgr, String id)
      throws Exception {
    //     CMSRedeemableClientServices cs = (CMSRedeemableClientServices) theAppMgr.getGlobalObject("REDEEMABLE_SRVC");
    //     return  cs.findHouseAccount(id);
    return (HouseAccount)findRedeemableById(theAppMgr, "HOUSE_ACCOUNT", id);
  }

  /**
   * Method finds redeemables on the basis of id and type
   * @param theAppMgr IRepositoryManager
   * @param type String
   * @param id String
   * @return Redeemable
   * @throws Exception
   */
  public static Redeemable findRedeemableById(IRepositoryManager theAppMgr, String type, String id)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return cs.findRedeemableById(type, id);
  }

  /**
   * Method used to find house account on the basis of customer id
   * @param theAppMgr IRepositoryManager
   * @param customerid String
   * @return Redeemable[]
   * @throws Exception
   */
  public static Redeemable[] findHouseAccountByCustomerId(IRepositoryManager theAppMgr
      , String customerid)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return cs.findHouseAccountByCustomerId(customerid);
  }

  /**
   * Method finds redeemables on the basis of customer id
   * @param theAppMgr IRepositoryManager
   * @param customerid String
   * @return Redeemable[]
   * @throws Exception
   */
  public static Redeemable[] findByCustomerId(IRepositoryManager theAppMgr, String customerid)
      throws Exception {
    CMSRedeemableClientServices cs = (CMSRedeemableClientServices)theAppMgr.getGlobalObject(
        "REDEEMABLE_SRVC");
    return cs.findByCustomerId(customerid);
  }
}
