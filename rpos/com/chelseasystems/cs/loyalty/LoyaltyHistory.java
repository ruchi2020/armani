/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

import java.util.Date;
import com.chelseasystems.cr.business.BusinessObject;


/**
 * <p>Title: </p>
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
public class LoyaltyHistory extends BusinessObject {
  /**
   * Attributes
   */
  protected Date transactionDate = new Date();
  protected String storeID = "";
  protected String transactionId = "";
  protected double pointEarned;
  protected double pointUsed;
  protected String transactionType = "";
  protected String reasonCode = "";
  protected String loyaltyNumber = "";

  /**
   * put your documentation comment here
   */
  public LoyaltyHistory() {
  }

  /**
   * Methods
   */
  public Date getTransactionDate() {
    return this.transactionDate;
  }

  /**
   * put your documentation comment here
   * @param TransactionDate
   */
  public void doSetTransactionDate(Date TransactionDate) {
    this.transactionDate = TransactionDate;
  }

  /**
   * put your documentation comment here
   * @param TransactionDate
   */
  public void setTransactionDate(Date TransactionDate) {
    doSetTransactionDate(TransactionDate);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getStoreID() {
    return this.storeID;
  }

  /**
   * put your documentation comment here
   * @param StoreID
   */
  public void doSetStoreID(String StoreID) {
    this.storeID = StoreID;
  }

  /**
   * put your documentation comment here
   * @param StoreID
   */
  public void setStoreID(String StoreID) {
    doSetStoreID(StoreID);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTransactionId() {
    return this.transactionId;
  }

  /**
   * put your documentation comment here
   * @param TransactionId
   */
  public void doSetTransactionId(String TransactionId) {
    this.transactionId = TransactionId;
  }

  /**
   * put your documentation comment here
   * @param TransactionId
   */
  public void setTransactionId(String TransactionId) {
    doSetTransactionId(TransactionId);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double getPointEarned() {
    return this.pointEarned;
  }

  /**
   * put your documentation comment here
   * @param PointEarned
   */
  public void doSetPointEarned(double PointEarned) {
    this.pointEarned = PointEarned;
  }

  /**
   * put your documentation comment here
   * @param PointEarned
   */
  public void setPointEarned(double PointEarned) {
    doSetPointEarned(PointEarned);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTransactionType() {
    return this.transactionType;
  }

  /**
   * put your documentation comment here
   * @param TransactionType
   */
  public void doSetTransactionType(String TransactionType) {
    this.transactionType = TransactionType;
  }

  /**
   * put your documentation comment here
   * @param TransactionType
   */
  public void setTransactionType(String TransactionType) {
    doSetTransactionType(TransactionType);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getReasonCode() {
    return this.reasonCode;
  }

  /**
   * put your documentation comment here
   * @param ReasonCode
   */
  public void doSetReasonCode(String ReasonCode) {
    this.reasonCode = ReasonCode;
  }

  /**
   * put your documentation comment here
   * @param ReasonCode
   */
  public void setReasonCode(String ReasonCode) {
    doSetReasonCode(ReasonCode);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getLoyaltyNumber() {
    return this.loyaltyNumber;
  }

  /**
   * put your documentation comment here
   * @param LoyaltyNumber
   */
  public void doSetLoyaltyNumber(String LoyaltyNumber) {
    this.loyaltyNumber = LoyaltyNumber;
  }

  /**
   * put your documentation comment here
   * @param LoyaltyNumber
   */
  public void setLoyaltyNumber(String LoyaltyNumber) {
    doSetLoyaltyNumber(LoyaltyNumber);
  }

public double getPointUsed() {
	return pointUsed;
}

public void setPointUsed(double pointUsed) {
	doSetPointUsed(pointUsed);
}
public void doSetPointUsed(double pointUsed) {
    this.pointUsed = pointUsed;
  }
}

