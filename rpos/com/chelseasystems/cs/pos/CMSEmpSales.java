/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.employee.CMSEmployee;
import java.util.Date;
import java.lang.String;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSEmpSales extends BusinessObject {
  private CMSEmployee consultantId;
  private String storeId;
  private ArmCurrency netAmount;
  private ArmCurrency amount;
  private String type;
  private String transactionId;
  private Date saleDate;
  private Long quantity;
  private Long txnCount;
  private final static String SALE = "S";
  private final static String RETURN = "R";

  /**
   * put your documentation comment here
   */
  public CMSEmpSales() {
    consultantId = null;
    storeId = null;
    netAmount = new ArmCurrency(0.0d);
    amount = new ArmCurrency(0.0d);
    type = SALE;
    saleDate = null;
    quantity = new Long(0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSEmployee getConsultantId() {
    return consultantId;
  }

  /**
   * put your documentation comment here
   * @param consultantId
   */
  public void setConsultantId(CMSEmployee consultantId) {
    this.consultantId = consultantId;
  }

  /**
   * put your documentation comment here
   * @param netAmount
   */
  public void setNetAmount(ArmCurrency netAmount) {
    this.netAmount = netAmount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getNetAmount() {
    return netAmount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Long getQuantity() {
    return quantity;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Date getSaleDate() {
    return saleDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getStoreId() {
    return storeId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTransactionId() {
    return transactionId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getType() {
    return type;
  }

  /**
   * put your documentation comment here
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   */
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  /**
   * put your documentation comment here
   * @param storeId
   */
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  /**
   * put your documentation comment here
   * @param saleDate
   */
  public void setSaleDate(Date saleDate) {
    this.saleDate = saleDate;
  }

  /**
   * put your documentation comment here
   * @param quantity
   */
  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getAmount() {
    return amount;
  }

  /**
   * put your documentation comment here
   * @param amount
   */
  public void setAmount(ArmCurrency amount) {
    this.amount = amount;
  }

  /**
   * put your documentation comment here
   * @param count
   */
  public void setTxnCount(Long count) {
    this.txnCount = count;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Long getTxnCount() {
    return this.txnCount;
  }
}

