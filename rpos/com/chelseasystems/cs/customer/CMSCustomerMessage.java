/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

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
public class CMSCustomerMessage extends BusinessObject {
  private String customerId;
  private String customerType;
  private String messageType;
  private String message;
  private String status;
  private String response;
  private boolean isSearchedById;

  /**
   * put your documentation comment here
   */
  public CMSCustomerMessage() {
  }

  /**
   * This method is used to set customerId of the customer
   * @param val String
   */
  public void setCustomerId(String val) {
    doSetCustomerId(val);
  }

  /**
   * This method is used to set customerType of the customer
   * @param val String
   */
  public void doSetCustomerId(String val) {
    this.customerId = val;
  }

  /**
   * This method is used to get customerId of the customer
   * @return String
   */
  public String getCustomerId() {
    return this.customerId;
  }

  /**
   * This method is used to set customerType of the customer
   * @param val String
   */
  public void setCustomerType(String val) {
    doSetCustomerType(val);
  }

  /**
   * This method is used to set customerType of the customer
   * @param val String
   */
  public void doSetCustomerType(String val) {
    this.customerType = val;
  }

  /**
   * This method is used to get customerType of the customer
   * @return String
   */
  public String getCustomerType() {
    return this.customerType;
  }

  /**
   * This method is used to set messageType of the customer message
   * @param val String
   */
  public void setMessageType(String val) {
    doSetMessageType(val);
  }

  /**
   * This method is used to set messageType of the customer message
   * @param val String
   */
  public void doSetMessageType(String val) {
    this.messageType = val;
  }

  /**
   * This method is used to get messageType of the customer message
   * @return String
   */
  public String getMessageType() {
    return this.messageType;
  }

  /**
   * This method is used to set the customer message
   * @param val String
   */
  public void setMessage(String val) {
    doSetMessage(val);
  }

  /**
   * This method is used to set  the customer message
   * @param val String
   */
  public void doSetMessage(String val) {
    this.message = val;
  }

  /**
   * This method is used to get  the customer message
   * @return String
   */
  public String getMessage() {
    return this.message;
  }

  /**
   * This method is used to set status of the customer message
   * @param val String
   */
  public void setStatus(String val) {
    doSetStatus(val);
  }

  /**
   * This method is used to set status of the customer message
   * @param val String
   */
  public void doSetStatus(String val) {
    this.status = val;
  }

  /**
   * This method is used to get status of the customer message
   * @return String
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * This method is used to set response of the customer message
   * @param val String
   */
  public void setresponse(String val) {
    doSetResponse(val);
  }

  /**
   * This method is used to set response of the customer message
   * @param val String
   */
  public void doSetResponse(String val) {
    this.response = val;
  }

  /**
   * This method is used to get response of the customer message
   * @return String
   */
  public String getResponse() {
    return this.response;
  }

  /**
   * This method is used to set flag of the customer message
   * @param val String
   */
  public void setisSearchedById(boolean val) {
    doSetisSearchedById(val);
  }

  /**
   * This method is used to set flag of the customer message
   * @param val String
   */
  public void doSetisSearchedById(boolean val) {
    this.isSearchedById = val;
  }

  /**
   * This method is used to get flag of the customer message
   * @return String
   */
  public boolean getisSearchedById() {
    return this.isSearchedById;
  }
}

