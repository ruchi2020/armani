/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 * <p>Title: Diners</p>
 *
 * <p>Description: Type of Credit Card</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    |            |           |           | Initial Version                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-03-2005 | Megha     | 92        |  Missing    super(transactionPaymentName);         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class Diners extends CreditCard {

  /**
   * Default Constructor
   */
  public Diners() {
    this(null);
  }

  /**
   * put your documentation comment here
   * @param   String transactionPaymentName
   */
  public Diners(String transactionPaymentName) {
    // For setting transaction payment name: for renderer.
    super(transactionPaymentName);
    super.messageIdentifier = new String("CC");
    super.messageType = new String("0");
    super.tenderType = new String("03");
    setGUIPaymentName("Diners");
  }
}
