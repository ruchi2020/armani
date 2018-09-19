/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.util.Date;
import java.io.Serializable;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: CustomerSaleSummarys</p>
 *
 * <p>Description: This class shows customer sales summary</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CustomerSaleSummary implements Serializable, Comparable {
  String custId, storeId, txnType;
  
  ArmCurrency txnAmount;
  Date txnDate;
//added by vivek for ARM_EMP_CUST_RULE
  
  ArmCurrency netAmount;
  double disPercent;
  
  public static final String TXN_TYPE_SALE = "SALE";
  public static final String TXN_TYPE_RETURN = "RETN";

  /**
   * Default Constructor
   */
  public CustomerSaleSummary() {
  }

  /**
   * This method is used to set customer id
   * @param val String
   */
  public void setCustomerId(String val) {
    this.custId = val;
  }

  /**
   * This method is used to get customer id
   * @return String
   */
  public String getCustomerId() {
    return this.custId;
  }

  /**
   * This method is used to set store id
   * @param val String
   */
  public void setStoreId(String val) {
    this.storeId = val;
  }

  /**
   * This method is used to get store id
   * @return String
   */
  public String getStoreId() {
    return this.storeId;
  }

  /**
   * This method is used to set transaction type
   * @param val String
   */
  public void setTxnType(String val) {
    this.txnType = val;
  }

  /**
   * This method is used to get transaction type
   * @return String
   */
  public String getTxnType() {
    return this.txnType;
  }

  /**
   * This method is used to set transaction date
   * @param val Date
   */
  public void setTxnDate(Date val) {
    this.txnDate = val;
  }

  /**
   * This method is used to get transaction date
   * @return Date
   */
  public Date getTxnDate() {
    return this.txnDate;
  }

  /**
   * This method is used to set transaction amount
   * @param val Currency
   */
  public void setTxnAmount(ArmCurrency val) {
    this.txnAmount = val;
  }

  /**
   * This method is used to get transaction amount
   * @return Currency
   */
  public ArmCurrency getTxnAmount() {
    return this.txnAmount;
  }
  /**
   * This method is used to get Discount percent
   * @return double
   * Auther:vivek Sawant
   */
  public double getDisPercent() {
		return disPercent;
	}
  /**
   * This method is used to set Discount percent
   * @return double
   * Auther:vivek Sawant
   */
	public void setDisPercent(double disPercent) {
		this.disPercent = disPercent;
	}
	 /**
	   * This method is used to get NetAmount for lineItem
	   * @return ArmCurrency
	   * Auther:vivek Sawant
	   */
	public ArmCurrency getNetAmount() {
		return netAmount;
	}
	 /**
	   * This method is used to set NetAmount for lineItem
	   * @return ArmCurrency
	   * Auther:vivek Sawant
	   */
	public void setNetAmount(ArmCurrency netAmount) {
		this.netAmount = netAmount;
	}

  /**
   * This method is used to Compater SaleSummaries.
   * @param obj CustomerSaleSummary
   * Returns 1 if Transactin Date is Later
   * Returns -1 if Transaction Date is Earlier
   * Returns 0 if Transaction Date is Same
   * @return int
   */
  public int compareTo(Object obj) {
    CustomerSaleSummary saleSum = (CustomerSaleSummary)obj;
    if (getTxnDate().before(saleSum.getTxnDate())) {
      return -1;
    }
    if (getTxnDate().after(saleSum.getTxnDate())) {
      return 1;
    }
    return 0;
  }
}

