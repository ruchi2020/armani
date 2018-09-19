/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 *
 * <p>Title: CMSVisa</p>
 *
 * <p>Description: Type of credit card</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSVisa extends Visa {
  private String zipCode;

  /**
   * Default Constructor
   */
  protected CMSVisa() {
    super();
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public CMSVisa(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  /**
   * This method is used to get zip code
   * @return String
   */
  public String getZipCode() {
    return this.zipCode;
  }

  /**
   * This method is used to set zip code
   * @param zipCode String
   */
  public void setZipCode(String zipCode) {
    doSetZipCode(zipCode);
  }

  /**
   * This method is used to set zip code
   * @param zipCode String
   */
  public void doSetZipCode(String zipCode) {
    this.zipCode = zipCode;
  }
}
