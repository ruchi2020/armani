/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.payment.*;


/**
 *
 * <p>Title: MallCert</p>
 *
 * <p>Description: This class stores the details of Mall Certificate</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MallCert extends Payment implements IRuleEngine {
  private String type;
  private String storeId;
  private String registerId;
  private String code;

  /**
   * default Constructor
   */
  public MallCert() {
    this(null);
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public MallCert(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  /**
   * This method is used to set the type of mail certificate
   * @param code String
   */
  public void setType(String code) {
    this.code = code;
  }

  /**
   * This method is used to get the type of mall certificate
   * @return String
   */
  public String getType() {
    return code;
  }

  /**
   * This method is used to set the description
   * @param type String
   */
  public void setDesc(String type) {
    this.type = type;
  }

  /**
   * This method is used to get the description
   * @return String
   */
  public String getDesc() {
    if (type != null)
      return type;
    else
      return "";
  }

  /**
   * This method is used to check whether authorisation is required
   * @return boolean
   */
  public boolean isAuthRequired() {
    return false;
  }

  /**
   * This method is used to check whether signature is required
   * @return boolean
   */
  public boolean isSignatureRequired() {
    return false;
  }

  /**
   * This method is used to check whether over payment is allowed
   * @return boolean
   */
  public boolean isOverPaymentAllowed() {
    return false;
  }

  /**
   * This method is used to check whether franking is required
   * @return boolean
   */
  public boolean isFrankingRequired() {
    return true;
  }

  /**
   * This method is used to set the store id
   * @param storeId String
   */
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  /**
   * This method is used to get the store id
   * @return String
   */
  public String getStoreId() {
    return storeId;
  }

  /**
   * This method is used to set the register id
   * @param registerId String
   */
  public void setRegisterId(String registerId) {
    this.registerId = registerId;
  }

  /**
   * This method is used to get the register id
   * @return String
   */
  public String getRegisterId() {
    return registerId;
  }
}
