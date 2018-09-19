/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.CreditCard;


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
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 |      |            |           |           | Initial Version                                    |
 | 1    | 06-28-2005 | Khayati   |  N/A      |                                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class Moneta extends CreditCard {

  /**
   * Default Constructor
   */
  public Moneta() {
  }

  /**
   * put your documentation comment here
   * @param   String transactionPaymentName
   */
  public Moneta(String transactionPaymentName) {
    // For setting transaction payment name: for renderer.
    super(transactionPaymentName);
    super.messageIdentifier = new String("CC");
    super.messageType = new String("0");
    super.tenderType = new String("03");
    setGUIPaymentName("Moneta");
  }
}
