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
import com.chelseasystems.cs.dataaccess.artsoracle.dao.*;
import java.util.*;


/**
 *
 * <p>Title: CMSRedeemableJDBCServices</p>
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
public class CMSRedeemableJDBCServices extends CMSRedeemableServices {
  private RedeemableIssueDAO redeemableIssueDAO;
  //Added by Satin for new coupon management.
  private CouponDAO couponDAO;


  /**
   * put your documentation comment here
   */
  public CMSRedeemableJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    redeemableIssueDAO = (RedeemableIssueDAO)configMgr.getObject("REDEEMABLEISSUE_DAO");
    //Added by Satin for new coupon management.
	couponDAO = (CouponDAO) configMgr.getObject("COUPON_DAO");
    
    
  }

  /**
   * Method find redeemables on the basis of control number
   * @param issueId String
   * @return Redeemable
   * @throws Exception
   */
  public Redeemable findRedeemable(String issueId)
      throws Exception {
    try {
      return (Redeemable)redeemableIssueDAO.selectRedeemableById(issueId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findRedeemable", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * Method finds store value card by its id
   * @param controlNumber String
   * @return StoreValueCard
   * @throws Exception
   */
  public StoreValueCard findStoreValueCard(String controlNumber)
      throws Exception {
    try {
      Redeemable redeemable = redeemableIssueDAO.selectRedeemableById(controlNumber);
      if (redeemable instanceof StoreValueCard)
        return (StoreValueCard)redeemable;
      return null;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findStoreValueCard"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  
  /**
   * Added by Satin
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barcode String, storeId String
   * @return CMSCoupon
   * @throws Exception
   */
  public CMSCoupon findByBarcodeAndStoreId(String barcode, String storeId)
			throws Exception {
	  try {
		  
		 CMSCoupon cmsCoupon = couponDAO.findByBarcodeAndStoreId(barcode, storeId );
		    return (CMSCoupon)cmsCoupon;
	      } catch (Exception exception) {
	      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByBarcodeAndStoreId"
	          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
	      exception.printStackTrace();
	      throw exception;
	    }
	}
  
  
  
  /**
   * Method finds redeemables on the basis of customer id
   * @param customerId String
   * @throws Exception
   * @return Redeemable[]
   */
  public Redeemable[] findByCustomerId(String customerId)
      throws Exception {
    try {
      return (Redeemable[])redeemableIssueDAO.selectByCustomerId(customerId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCustomerId"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * Method finds redeemables on the basis of type and id
   * @param type String
   * @param customerId String
   * @throws Exception
   * @return Redeemable
   */
  public Redeemable findRedeemableById(String type, String id)
      throws Exception {
    try {
      return (Redeemable)redeemableIssueDAO.selectRedeemableById(type, id);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findRedeemableById"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
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
      return (Redeemable[])redeemableIssueDAO.selectByCustomerId(ArtsConstants.
          PAYMENT_TYPE_HOUSE_ACCOUNT, id);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findHouseAccountByCustomerId"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * Method finds gift certificate by its id
   * @param id String
   * @return GiftCert
   * @throws Exception
   */
  public GiftCert findGiftCert(String id)
      throws Exception {
    try {
      Redeemable redeemable = redeemableIssueDAO.selectRedeemableById(id);
      if (redeemable instanceof GiftCert)
        return (GiftCert)redeemable;
      return null;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findGiftCert", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
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
      Redeemable redeemable = redeemableIssueDAO.selectRedeemableById(id);
      if (redeemable instanceof DueBillIssue)
        return toDueBill((DueBillIssue)redeemable);
      return null;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findDueBill", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * Method adds redeemables
   * @param redeemable Redeemable
   * @throws Exception
   */
  public void addRedeemable(Redeemable redeemable)
      throws Exception {
    try {
      redeemableIssueDAO.insert(redeemable);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "addRedeemable", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * Method Adds a new due bill.  This method should be called when a due bill
   * is issued
   * @param dueBill DueBill
   * @throws Exception
   */
  public void addDueBill(DueBill dueBill)
      throws Exception {
    try {
      redeemableIssueDAO.insert(dueBill);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "addDueBill", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param giftCert GiftCert
   * @throws Exception
   */
  public void addGiftCert(GiftCert giftCert)
      throws Exception {
    try {
      redeemableIssueDAO.insert(giftCert);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "addGiftCert", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This methods updated an exiting gift certificate
   * @param compositePOSTransaction CompositePOSTransaction
   * @throws Exception
   */
  public void updateGiftCert(CompositePOSTransaction compositePOSTransaction)
      throws Exception {
    try {
      updateRedeemable(compositePOSTransaction);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateGiftCert", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * Method Updates an existing due bill
   * @param compositePOSTransaction CompositePOSTransaction
   * @throws Exception
   */
  public void updateDueBill(CompositePOSTransaction compositePOSTransaction)
      throws Exception {
    try {
      updateRedeemable(compositePOSTransaction);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateDueBill", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
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
      return redeemableIssueDAO.isGiftCertControlNumberUsed(newControlNumber);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "isNewGiftCertControlNumberValid", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This methods updated an exiting gift certificate
   * @param aTxn CompositePOSTransaction
   * @throws Exception
   */
  private void updateRedeemable(CompositePOSTransaction aTxn)
      throws Exception {
    try {
      Payment[] pays = aTxn.getPaymentsArray();
      for (int i = 0; i < pays.length; i++)
        if ((pays[i] instanceof Redeemable) && !(pays[i] instanceof DueBillIssue)) {
          Redeemable redeemablePayment = (Redeemable)pays[i];
          //CMSCoupon couponPayment = (CMSCoupon) pays[i];
          if (redeemablePayment instanceof DueBill)
            redeemablePayment.setAmount(redeemablePayment.getAmount().absoluteValue());
          RedeemableHist hist = new RedeemableHist();
          hist.setDateUsed(new Date());
          hist.setTransactionsIdUsed(aTxn.getId());
          hist.setAmountUsed(redeemablePayment.getAmount());
          Redeemable originalRedeemableIssue = null;
          if (redeemablePayment instanceof GiftCert)
            originalRedeemableIssue = redeemableIssueDAO.selectGiftCertById(redeemablePayment.getId());
          else if (redeemablePayment instanceof DueBill)
            originalRedeemableIssue = redeemableIssueDAO.selectDueBillById(redeemablePayment.getId());
          //else if (couponPayment instanceof CMSCoupon)
        	  
          originalRedeemableIssue.addRedemption(hist);
          redeemableIssueDAO.update(originalRedeemableIssue);
        }
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateRedeemable"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }
  
  
  

  /**
   *
   * @param dueBillIssue DueBillIssue
   * @return DueBill
   * @throws Exception
   */
  private DueBill toDueBill(DueBillIssue dueBillIssue)
      throws Exception {
    try {
      if (dueBillIssue == null)
        return null;
      DueBill dueBill = new CMSDueBill(IPaymentConstants.CREDIT_MEMO);
      dueBill.setAmount(dueBillIssue.getIssueAmount().absoluteValue());
      dueBill.setId(dueBillIssue.getId());
      dueBill.setType(dueBillIssue.getType());
      dueBill.setIssueAmount(dueBillIssue.getIssueAmount().absoluteValue());
      dueBill.setCreateDate(dueBillIssue.getCreateDate());
      dueBill.setAuditNote(dueBillIssue.getAuditNote());
      dueBill.setFirstName(dueBillIssue.getFirstName());
      dueBill.setLastName(dueBillIssue.getLastName());
      dueBill.setPhoneNumber(dueBillIssue.getPhoneNumber());
      dueBill.setRedemptionHistory(dueBillIssue.getRedemptionHistory());
      return dueBill;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "toDueBill", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }






}
