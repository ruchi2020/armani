/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 * <p>Title: CMSBankCheck</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class LocalCheck extends Check {

  /**
   * Default Constructor
   */
  public LocalCheck() {
    this(null);
  }

  /**
   * Constructor
   * @param transactionPaymentName a key representing this payment
   */
  public LocalCheck(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  /**
   *
   * @return boolean
   */
  public boolean isAuthRequired() {
    return false;
  }
  /**
   *
   * @param responce Object
   * @return String
   */
  /*  public String setCreditAuthorization(Object response)
   {
   if(response instanceof String)
   return super.setCreditAuthorization((String)response);  //call to super.setCreditAuthorization(String)
   else
   validationRequest.setCheckAuthorization(response, this);
   return respStatusCode;
   }*/
}
