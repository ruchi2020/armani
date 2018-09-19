/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import java.util.*;
import java.text.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.JCBCreditCard;
import com.chelseasystems.cs.payment.CMSIPaymentConstants;
import com.chelseasystems.cs.payment.Diners;
import com.chelseasystems.cs.payment.Coupon;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;


/**
 */
public class CouponBldrUtil {

  /**
   * put your documentation comment here
   * @param barCode
   * @return
   */
  public static Payment allocCouponPayment(String barCode) {
    if (barCode == null || barCode.length() < 13) {
      return null;
    }
    int code = new Integer(barCode.substring(0, 2)).intValue();
    // Coupon
    if ((code != 99) || (barCode.length() != 13)) {
      return null;
    }
    if (!CouponBldrUtil.validateCheckDigit(barCode))
      return null;
    Coupon coupon = new Coupon(ICMSPaymentConstants.COUPON);
    return coupon;
  }

  /**
   * put your documentation comment here
   * @param barCode
   * @return
   */
  public static boolean validateCheckDigit(String barCode) {
    if (barCode == null)
      return false;
    barCode = removeSpaces(barCode);
    int len = barCode.length();
    Integer code;
    String checkStr = barCode.substring(len - 1, len);
    int checkDigit = (new Integer(checkStr)).intValue();
    String actualBarCode = barCode.substring(0, len - 1);
    int sumEven = 0;
    int sumOdd = 0;
    int calculatedCheckdigit = -1;
    try {
      // Calculation.
      for (int i = 0; i < len - 1; i++) {
        code = new Integer(actualBarCode.substring(i, i + 1));
        int val = code.intValue();
        if ((i + 1) % 2 == 0)
          sumEven = sumEven + val;
        else
          sumOdd = sumOdd + val;
      }
      sumEven = sumEven * 3;
      int sumTotal = sumEven + sumOdd;
      int firstDigit = 0;
      if (sumTotal % 10 == 0) {
        calculatedCheckdigit = 0;
      } else {
        firstDigit = (int)(sumTotal / 10);
        firstDigit = firstDigit + 1;
        firstDigit = firstDigit * 10;
        firstDigit = firstDigit - sumTotal;
        calculatedCheckdigit = firstDigit;
      }
    } catch (Exception ex) {
      return false;
    }
    if (calculatedCheckdigit == checkDigit) {
      return true;
    } else
      return false;
  }

  /**
   * @param inStr
   * @return
   */
  public static String removeSpaces(String inStr) {
    // remove spaces in account num
    int pos = inStr.indexOf(" ");
    if (pos < 0) {
      return inStr;
    } else {
      StringBuffer buf = new StringBuffer(inStr.length());
      for (int i = 0; i < inStr.length(); i++) {
        if (' ' != inStr.charAt(i))
          buf.append(inStr.charAt(i));
      }
      return buf.toString();
    }
  }
}

