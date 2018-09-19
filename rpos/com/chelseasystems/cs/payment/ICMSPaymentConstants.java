/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;


/**
 *
 * <p>Title: ICMSPaymentConstants</p>
 *
 * <p>Description: Defines payment constants</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ICMSPaymentConstants extends IPaymentConstants {
  public static final String HOUSE_ACCOUNT = "HOUSE_ACCOUNT";
  public static final String REWARD_CARD = "REWD";
  // From Europe code base
  public static final String LOCAL_CHECK = "LOCAL_CHECK";
  public static final String OUT_OF_AREA_CHECK = "OUT_OF_AREA_CHECK";
  public static final String ROUND_PAYMENT = "ROUND_PAYMENT";
  public static final String CREDIT = "CREDIT";
  public static final String DINERS = "DINERS";
  public static final String JCB = "JCB";
  public static final String VISA = "VISA";
  public static final String MONETA = "MONETA";
  public static final String COUPON = "COUPON";
}
