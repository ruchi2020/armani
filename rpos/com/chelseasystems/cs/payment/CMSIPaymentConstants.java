/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;


/**
 *
 * <p>Title: CMSIPaymentConstants</p>
 *
 * <p>Description: Defines constants for payment</p>
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
 | 2    | 06-03-2005 | Megha     | 92        |   Changed lowercase Diners to DINERS ( according to|
 |      |            |           |           |   payment.cfg file                                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public interface CMSIPaymentConstants {
  /**
   * Credit Card type
   */
  public static final String JCB = "JCB";
  /**
   * Credit Card type
   */
  public static final String diner = "DINERS";
}
