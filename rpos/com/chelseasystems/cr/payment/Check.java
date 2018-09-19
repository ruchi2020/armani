/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.payment;

import java.util.*;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.authorization.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.rules.*;


/**
 *  Abstract class for all checks as payment.
 *  @author John Gray
 *  @version 1.0a
 */
public class Check extends Payment implements IRuleEngine {
	public boolean authorizeOffline;

  /**
   * Default Constructor
   */
  protected Check() {
    this(null);
  }

  /**
   * @param transactionPaymentName a key representing this payment
   */
  public Check(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  /**
   * @return
   */
  public boolean isAuthRequired() {
    return (true);
  }

  /**
   * Tests if the payment requires the customer to sign a receipt.
   * @return true if the payment requires the customer to sign a receipt
   */
  public boolean isSignatureRequired() {
    return (false);
  }

  /**
   * Tests if the payment is valid for more than the current amount due.
   * @return true if the payment is valid for more than the current amount due
   */
  public boolean isOverPaymentAllowed() {
    return (false);
  }

  /**
   * Tests if the payment requires the cash register to print on the back of
   * the document submitted for payment.
   * @return true if the payment requires the cash register to print on the
   * back of the document submitted for payment
   */
  public boolean isFrankingRequired() {
    try {
      if (this.getAmount().greaterThanOrEqualTo(new ArmCurrency(0.00))) {
        return (true);
      } else {
        return (false);
      }
    } catch (CurrencyException ce) {
      return (false);
    }
  }
  
  public boolean getAuthorizeOffline(){
	  return authorizeOffline;
	    }
  
  public void setAuthorizeOffline(boolean authorizeOffline){
	  this.authorizeOffline = authorizeOffline;
  }
}

