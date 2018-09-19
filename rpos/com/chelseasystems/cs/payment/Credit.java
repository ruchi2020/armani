/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.payment.*;


/**
 *
 * <p>Title: Credit</p>
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
public class Credit extends Payment implements IRuleEngine {
  private String type;

  /**
   * Default Constructor
   */
  public Credit() {
    this(null);
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public Credit(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  /**
   * This method id used to check whether authorization is required
   * @return boolean
   */
  public boolean isAuthRequired() {
    return false;
  }

  /**
   * This method id used to check whether signature is required
   * @return boolean
   */
  public boolean isSignatureRequired() {
    return false;
  }

  /**
   * This method id used to check whether over payment is allowed
   * @return boolean
   */
  public boolean isOverPaymentAllowed() {
    return false;
  }

  /**
   * This method id used to check whether franking is required
   * @return boolean
   */
  public boolean isFrankingRequired() {
    return false;
  }
}
