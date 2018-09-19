/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 * <p>Title:JCB Credit Card</p>
 *
 * <p>Description: Newly added credit-card </p>
 *
 * <p>Copyright: RETEK Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JCBCreditCard extends CreditCard {

  /**
   * Default Constructor
   */
  public JCBCreditCard() {
    this(null);
  }

  /**
   * put your documentation comment here
   * @param   String transactionPaymentName
   */
  public JCBCreditCard(String transactionPaymentName) {
    super(transactionPaymentName);
    super.messageIdentifier = new String("CC");
    super.messageType = new String("0");
    super.tenderType = new String("03");
    setGUIPaymentName("JCB");
  }
}
