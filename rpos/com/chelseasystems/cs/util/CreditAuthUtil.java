/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 07/06/2005 | Vikram    | 405       | Created - to generate Journal Key                  |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import java.util.Date;


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
public class CreditAuthUtil {
  private static long lastJournalKey = 0;

  /**
   * put your documentation comment here
   * @param storeID
   * @param registerID
   * @return
   */
  public static synchronized String getJournalKey(String storeID, String registerID) {
    if (storeID == null || registerID == null)
      return null;
    String journalKey = null;
    String timeStampStr = "" + new Date().getTime();
    //Store ID of 4 digits
    journalKey = getRightJustifiedNumber(storeID.trim(), 4);
    //Register ID of 3 digits
    journalKey += getRightJustifiedNumber(registerID.trim(), 3);
    //TimeStamp of 9 least significant digits with max precision=10 seconds
    journalKey += getRightJustifiedNumber(timeStampStr.substring(0, timeStampStr.length() - 4), 9);
    if (Long.parseLong(journalKey) <= lastJournalKey)
      journalKey = "" + (lastJournalKey + 1);
    lastJournalKey = Long.parseLong(journalKey);
    return journalKey;
  }

  // Helper Function: getRightJustifiedNumber
  /**
   * This method returns the orgString as a Right justified number
   * with a length of leng.
   * @param  orgString   String to left justify
   * @param  leng        Length to make string
   * @result             String containing the result as DIGITS
   */
  private static String getRightJustifiedNumber(String orgString, int leng) {
    String inStr = orgString.trim();
    int diff = leng - inStr.length();
    String result = null;
    if (diff > 0) {
      String temp = createZeroString(diff);
      result = temp.concat(inStr);
    } else if (diff < 0) {
      //System.err.println("ERROR -- input too large.  Input: >" + inStr + "<   Max. length: " + leng);
      result = inStr.substring(0 - diff, inStr.length());
    } else {
      result = new String(inStr);
    }
    return result;
  }

  // Function: createZeroString
  /**
   * This method creates a string of "size" containing zeroes
   * @result String containing the result
   */
  private static String createZeroString(int size) {
    char array[] = new char[size];
    for (int i = 0; i < size; i++)
      array[i] = '0';
    String str = new String(array);
    array = null;
    return str;
  }

  /**
   *
   * @param txnID fmtAccountNum
   * @return String
   */
  public static String maskCreditCardNo(String fmtAccountNum) {
    if (fmtAccountNum == null) {
      System.out.println("Error:: Masking: Credit Card Num is Null ");
      return "";
    }
    fmtAccountNum = fmtAccountNum.trim();
    if (!fmtAccountNum.equals("")) {
      try {
        //System.out.println ("Masking the Account Num");
        int strLength = fmtAccountNum.length();
        String appo_temp="XXXXXXXXXXXXXXXXXXXXXXXXX";
        if(strLength < 10){
        	int l=(10-strLength) + 1;
			fmtAccountNum=appo_temp.substring(0,l) + fmtAccountNum; 
        }
		strLength = fmtAccountNum.length();
        String firstStr = fmtAccountNum.substring(0, (strLength - 10));
        String lastStr = fmtAccountNum.substring(strLength - 4);
        return firstStr + "XXXXXX" + lastStr;
      } catch (Exception e) {
        System.out.println("Error in Masking the Credit Card Num :: " + e.getMessage());
        //e.printStackTrace();
        return fmtAccountNum;
      }
    } else
      return fmtAccountNum;
  }

  /**
   *
   * @param txnID trackData
   * @return String
   */
  public static String maskTrackData(String trackData) {
    trackData = trackData.trim();
    if (!trackData.equals("")) {
      try {
        System.out.println("Masking the Track Data");
        int strIndex = trackData.indexOf("=");
        if (strIndex > -1) {
          String firstStr = trackData.substring(0, (strIndex - 10));
          String middleStr = trackData.substring((strIndex - 4), strIndex);
          String lastStr = trackData.substring(strIndex, (strIndex + 5));
          return firstStr + "XXXXXX" + middleStr + lastStr + "XXXXXXXXXXXXXXXX";
        }
      } catch (Exception e) {
        System.out.println("Error in Masking the Track Number:: " + e.getMessage());
        e.printStackTrace();
        return trackData;
      }
    }
    return trackData;
  }
}

