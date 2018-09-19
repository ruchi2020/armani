/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;


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
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-13-2005 | Vikram    | 144       |Redeemables: Mag Stripe is not being read     |
 |      |            |           |           |properly on Electronic Gift Cards             |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-31-2005 | Megha     | 83       |Loyalty/Enroll: Number format Exception        |
 |      |            |           |             occurs when alpha-numeric value added.       |
 --------------------------------------------------------------------------------------------
 */
public class IsDigit {

  /**
   * put your documentation comment here
   */
  public IsDigit() {
  }

  /**
   * put your documentation comment here
   * @param accountNum
   * @return
   */
  public boolean isDigit(String accountNum) {
    char c;
    for (int i = 0; i < accountNum.length(); i++) {
      c = accountNum.charAt(i);
      if (!Character.isDigit(c)) {
        return false;
      }
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @param stringToFilter
   * @return
   */
  public String filterToGetDigits(String stringToFilter) {
    String newStr = "";
    char c;
    for (int i = 0; i < stringToFilter.length(); i++) {
      c = stringToFilter.charAt(i);
      if (Character.isDigit(c)) {
        newStr += c;
      }
    }
    return newStr;
  }
}

