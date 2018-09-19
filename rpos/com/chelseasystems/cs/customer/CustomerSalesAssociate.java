/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.io.Serializable;


/**
 * <p>Title: CustomerSalesAssociate</p>
 * @author Rajesh Pradhan
 */
/**
 *
 * <p>Title: CustomerSalesAssociate</p>
 *
 * <p>Description: This class store the information of customer sales associate</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CustomerSalesAssociate implements Serializable {

  /**
   * put your documentation comment here
   */
  public CustomerSalesAssociate() {
  }

  String custSalesAssocId, storeId, assocId;
  boolean isModified;

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
   * This method is used to set associate id
   * @param val String
   */
  public void setAssocId(String val) {
    this.assocId = val;
  }

  /**
   * This method is used to get associate id
   * @return String
   */
  public String getAssocId() {
    return this.assocId;
  }

  /**
   * This method is used to set customer is modified
   * @param val boolean
   */
  public void setIsModified(boolean val) {
    this.isModified = val;
  }

  /**
   * This method is used to check whether customer is modified or not
   * @return boolean
   */
  public boolean isModified() {
    return this.isModified;
  }

  /**
   * This method is used to set customer sales associate id
   * @param val String
   */
  public void setCustSalesAssocId(String val) {
    this.custSalesAssocId = val;
  }

  /**
   * This method is used to get customer sales associate id
   * @return String
   */
  public String getCustSalesAssocId() {
    return this.custSalesAssocId;
  }
}

