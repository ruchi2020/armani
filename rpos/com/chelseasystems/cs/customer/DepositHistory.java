/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.util.Date;
import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;


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
public class DepositHistory extends BusinessObject {
  /**
   * Attributes
   */
  protected Date transactionDate = new Date();
  protected String storeID = "";
  protected String storeName = "";
  protected String transactionId = "";
  protected String transactionType = "";
  protected String assoc = "";
  protected String assocFirstName = "";
  protected String assocLastName = "";
  protected ArmCurrency amount;
  public static final String OPEN_DEPOSIT_TYPE = "OPEN";
  public static final String CLOSE_DEPOSIT_TYPE = "CLOSE";
  protected CMSCustomer customer;

  /**
   * put your documentation comment here
   */
  public DepositHistory() {
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
  public String getassoc() {
    return this.assoc;
  }

  /**
   * put your documentation comment here
   * @param assoc
   */
  public void doSetassoc(String assoc) {
    this.assoc = assoc;
  }

  /**
   * put your documentation comment here
   * @param assoc
   */
  public void setassoc(String assoc) {
    doSetassoc(assoc);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getamount() {
    return this.amount;
  }

  /**
   * put your documentation comment here
   * @param amount
   */
  public void doSetamount(ArmCurrency amount) {
    this.amount = amount;
  }

  /**
   * put your documentation comment here
   * @param amount
   */
  public void setamount(ArmCurrency amount) {
    doSetamount(amount);
  }

  /**
   * put your documentation comment here
   * @param cmsCust
   */
  public void setCustomer(CMSCustomer cmsCust) {
    this.customer = cmsCust;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSCustomer getCustomer() {
    return this.customer;
  }
  
  //PCR1326 DepositHistory fix for Armani Japan
  /**
   * put your documentation comment here
   * @return
   */
  public String getAssocFirstName() {
    return this.assocFirstName;
  }

  /**
   * put your documentation comment here
   * @param assoc
   */
  public void doSetAssocFirstName(String assocFirstName) {
    this.assocFirstName = assocFirstName;
  }

   /**
   * put your documentation comment here
   * @param assoc
   */
  public void setAssocFirstName(String assocFirstName) {
    doSetAssocFirstName(assocFirstName);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAssocLastName() {
    return this.assocLastName;
  }

   /**
   * put your documentation comment here
   * @param assoc
   */
  public void doSetAssocLastName(String assocLastName) {
    this.assocLastName = assocLastName;
  }  

  /**
   * put your documentation comment here
   * @param assoc
   */
  public void setAssocLastName(String assocLastName) {
    doSetAssocLastName(assocLastName);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getStoreName() {
    return this.storeName;
  }

  /**
   * put your documentation comment here
   * @param StoreName
   */
  public void doSetStoreName(String storeName) {
    this.storeName = storeName;
  }

  /**
   * put your documentation comment here
   * @param StoreName
   */
  public void setStoreName(String storeName) {
    doSetStoreName(storeName);
  }
}

