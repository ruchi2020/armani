/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

/* History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	?	        AK	Add Tender
 * 2    4/19/05         KS      Redeemable Managment             AddCustomer Id
 * 3    7/8/2005        MP        ""                               Added status field.
 */
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.payment.*;


/**
 *
 * <p>Title: HouseAccount</p>
 *
 * <p>Description: Stores the details of house account</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class HouseAccount extends Redeemable implements IRuleEngine {
  private String controlNum;
  private String customerId;
  private boolean status = true;

  /**
   * Default Constructor
   */
  public HouseAccount() {
    this(null);
    customerId = "";
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public HouseAccount(String transactionPaymentName) {
    super(transactionPaymentName);
    controlNum = new String();
    customerId = "";
  }

  /**
   *
   * @return String
   */
  public String getControlNum() {
    return controlNum;
  }

  /**
   *
   * @param aString String
   */
  public void doSetControlNum(String aString) {
    if (aString == null) {
      return;
    } else {
      controlNum = aString;
      return;
    }
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
    return true;
  }

  /**
   * This method id used to check whether franking is required
   * @return boolean
   */
  public boolean isFrankingRequired() {
    return false;
  }

  /**
   * This method is used to get customer id
   * @return String
   */
  public String getCustomerId() {
    return this.customerId;
  }

  /**
   * This method is used to set customer id
   * @param customerId String
   */
  public void setCustomerId(String customerId) {
    doSetCustomerId(customerId);
  }

  /**
   * This method is used to set customer id
   * @param customerId String
   */
  public void doSetCustomerId(String customerId) {
    this.customerId = customerId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean getStatus() {
    return this.status;
  }

  /**
   * put your documentation comment here
   * @param Status
   */
  public void doSetStatus(boolean Status) {
    this.status = Status;
  }

  /**
   * put your documentation comment here
   * @param Status
   */
  public void setStatus(boolean Status) {
    doSetStatus(Status);
  }
}
