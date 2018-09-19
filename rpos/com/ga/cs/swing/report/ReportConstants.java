/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report;

import java.util.HashMap;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;


/**
 * @author fbulah
 *
 */
public class ReportConstants {
  public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMd";
  public static final String DATE_FORMAT_SLASH_MMDDYYYY = "MM/dd/yyyy";
  public static final String DATE_FORMAT_SLASH_MDY = "M/d/y";
  protected static HashMap paymentTypeMap = new HashMap();
  public final static String PAYMENT_TYPE_YEN = "YEN";
  public final static String PAYMENT_TYPE_JPY_CASH = "JPY_CASH";
  public final static String PAYMENT_TYPE_DOLLARS = "DOLLARS";
  public final static String PAYMENT_TYPE_JCB = "JCB";
  public final static String PAYMENT_TYPE_BCRD = "BCRD";
  public final static String YEN_CURRENCY_SYMBOL = "JPY";
  //
  //TODO: MOVE THIS TO THE DB
  //
  static {
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_AMEX, "AMERICAN EXPRESS");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_CASH, "CASH");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_CHECK, "CHECK");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_GIFT_CERTIFICATE, "GIFT CERTIFICATE");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_MANUFACTURE_COUPON, "MFR COUPON");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_MONEY_ORDER, "MONEY ORDER");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_MASTER_CARD, "MASTER CARD");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_TRAVELLERS_CHECK, "TRAVELERS CHECK");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_VISA, "VISA");
    paymentTypeMap.put(ArtsConstants.PAYMENT_TYPE_STORE_VALUE_CARD, "GIFT CARD");
    //
    // Note:
    // The following values are in the DB, but do not seem to match the values in ArtsConstants
    //
    paymentTypeMap.put("MFR_COUPON", "MFR COUPON");
    paymentTypeMap.put("TRAVERLS_CHECK", "TRAVELERS CHECK");
    paymentTypeMap.put("DINERS", "DINERS");
    //=====================================================================================================
    //
    // The following values are not in the DB or in ArtsConstants
    //
    paymentTypeMap.put(PAYMENT_TYPE_YEN, "JAPANESE YEN");
    paymentTypeMap.put(PAYMENT_TYPE_JPY_CASH, "JAPANESE YEN");
    paymentTypeMap.put(YEN_CURRENCY_SYMBOL, "JAPANESE YEN");
    paymentTypeMap.put(PAYMENT_TYPE_JCB, "JCB");
    paymentTypeMap.put(PAYMENT_TYPE_BCRD, "BANK CARD");
    paymentTypeMap.put("CREDIT_MEMO", "CREDIT NOTE");
    paymentTypeMap.put("CREDIT_MEMO_ISSUE", "CREDIT NOTE ISSUE");
    paymentTypeMap.put("GIFT_CERT", "MALL CERTIFICATE");
    paymentTypeMap.put("HOUSE_ACCOUNT", "HOUSE ACCOUNT");
  }

  /**
   * put your documentation comment here
   */
  public ReportConstants() {
  }

  /**
   * put your documentation comment here
   * @param paymentTypeKey
   * @return
   */
  public static String getPaymentType(Object paymentTypeKey) {
    String paymentType = (String)paymentTypeMap.get(paymentTypeKey);
    if (paymentType == null) {
      paymentType = paymentTypeKey.toString();
    }
    return paymentType;
  }
}

