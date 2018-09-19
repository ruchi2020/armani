/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.dataaccess.*;
import java.util.*;


/**
 *
 * <p>Title: CMSRedeemableNullServices</p>
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
public class CMSRedeemableNullServices extends CMSRedeemableServices {

  /**
   * default Constructor
   */
  public CMSRedeemableNullServices() {
  }

  /**
   * Method find redeemables on the basis of control number
   * @param controlNumber String
   * @return Redeemable
   * @throws Exception
   */
  public Redeemable findRedeemable(String controlNumber)
      throws Exception {
    return null;
  }

  /**
   * Method finds store value card by its id
   * @param id String
   * @return StoreValueCard
   * @throws Exception
   */
  public StoreValueCard findStoreValueCard(String id)
      throws Exception {
    return null;
  }
  
  
  /**
   * Added by Satin
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barcode String, StoreId String 
   * @return CMSCoupon
   * @throws Exception
   */
  public CMSCoupon findByBarcodeAndStoreId(String barcode, String StoreId)
      throws Exception {
    return null;
  }
  

  /**
   * ks: TO be deleted
   * @param id String
   * @throws Exception
   * @return HouseAccount
   public HouseAccount findHouseAccount(String id) throws Exception {
   return null;
   }
   */
  /**
   * Method finds house account on the basis of customer id
   * @param customerid String
   * @throws Exception
   * @return Redeemable[]
   */
  public Redeemable[] findHouseAccountByCustomerId(String customerid)
      throws Exception {
    return null;
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
    return null;
  }

  /**
   * Method finds redeemables on the basis of customer id
   * @param customerId String
   * @throws Exception
   * @return Redeemable
   */
  public Redeemable[] findByCustomerId(String customerId)
      throws Exception {
    return null;
  }

  /**
   * Method finds gift certificate by its id
   * @param id String
   * @return GiftCert
   * @throws Exception
   */
  public GiftCert findGiftCert(String id)
      throws Exception {
    return null;
  }

  /**
   * Method Finds a due bill based on an id
   * @param id String
   * @return DueBill
   * @throws Exception
   */
  public DueBill findDueBill(String id)
      throws Exception {
    return null;
  }

  /**
   * Method adds redeemables
   * @param redeemable Redeemable
   * @throws Exception
   */
  public void addRedeemable(Redeemable redeemable)
      throws Exception {
    return;
  }

  /**
   * Method Adds a new due bill.  This method should be called when a due bill
   * is issued
   * @param dueBill DueBill
   * @throws Exception
   */
  public void addDueBill(DueBill dueBill)
      throws Exception {
    return;
  }

  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param giftCert GiftCert
   * @throws Exception
   */
  public void addGiftCert(GiftCert giftCert)
      throws Exception {
    return;
  }

  /**
   * This methods updated an exiting gift certificate
   * @param compositePOSTransaction CompositePOSTransaction
   * @throws Exception
   */
  public void updateGiftCert(CompositePOSTransaction compositePOSTransaction)
      throws Exception {
    return;
  }

  /**
   * Method Updates an existing due bill
   * @param compositePOSTransaction CompositePOSTransaction
   * @throws Exception
   */
  public void updateDueBill(CompositePOSTransaction compositePOSTransaction)
      throws Exception {
    return;
  }

  /**
   * Method checks if the new Gift Cert control number is valid
   * @param newControlNumber String
   * @return boolean
   * @throws Exception
   */
  public boolean isNewGiftCertControlNumberValid(String newControlNumber)
      throws Exception {
    return false;
  }
}
