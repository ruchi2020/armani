/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 1    | 04-28-2005 |Megha    |             | Loyalty Management                              |
 -----------------------------------------------------------------------------------------------
 */
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.loyalty.Loyalty;
import java.util.*;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.loyalty.RewardCard;


/**
 * put your documentation comment here
 */
public class LoyaltyBinRangeUtil {
  private static String types[] = null;
  private static String typesReward[] = null;
  private static Hashtable htLoyaltyKeys = null;
  private static Hashtable htLoyaltyValues = null;
  private static Hashtable htRewardKeys = null;
  private static Hashtable htRewardValues = null;
  private static ResourceBundle res = null;
  public static String CardType = "";
  public static IApplicationManager theAppMgr = null;
  //ks: Read the loyalty.cfg to get the values to check the card
  static {
    htLoyaltyKeys = new Hashtable();
    htLoyaltyValues = new Hashtable();
    htRewardKeys = new Hashtable();
    htRewardValues = new Hashtable();
    res = ResourceManager.getResourceBundle();
    try {
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      String loyaltyTypes = config.getString("LOYALTY_CARDS");
      String rewardTypes = config.getString("REWARD_CARDS");
      StringTokenizer stkReason = null;
      StringTokenizer stkLoyalty = null;
      int i = -1;
      if (loyaltyTypes != null) {
        stkLoyalty = new StringTokenizer(loyaltyTypes, ",");
      }
      if (stkLoyalty != null) {
        types = new String[stkLoyalty.countTokens()];
        while (stkLoyalty.hasMoreTokens()) {
          types[++i] = stkLoyalty.nextToken();
          htLoyaltyValues = new Hashtable();
          String loyaltyType = config.getString(types[i] + ".TYPE");
          htLoyaltyValues.put("TYPES", loyaltyType);
          String loyaltyLength = config.getString(types[i] + ".LENGTH");
          htLoyaltyValues.put("LENGTH", loyaltyLength);
          String lowBinRange = config.getString(types[i] + ".LOW_BIN_RANGE");
          htLoyaltyValues.put("LOW_BIN_RANGE", lowBinRange);
          String highBinRange = config.getString(types[i] + ".HIGH_BIN_RANGE");
          htLoyaltyValues.put("HIGH_BIN_RANGE", highBinRange);
          String checkDigit = config.getString(types[i] + ".INCLUDE_CHECK_DIGIT");
          htLoyaltyValues.put("INCLUDE_CHECK_DIGIT", checkDigit);
          htLoyaltyKeys.put(types[i], htLoyaltyValues);
        }
      }
      if (rewardTypes != null) {
        stkReason = new StringTokenizer(rewardTypes, ",");
      }
      if (stkReason != null) {
        typesReward = new String[stkReason.countTokens()];
        i = -1;
        while (stkReason.hasMoreTokens()) {
          typesReward[++i] = stkReason.nextToken();
          htRewardValues = new Hashtable();
          String rewardType = config.getString(typesReward[i] + ".TYPE");
          htRewardValues.put("TYPES", rewardType);
          String rewardLength = config.getString(typesReward[i] + ".LENGTH");
          htRewardValues.put("LENGTH", rewardLength);
          String lowBinRange = config.getString(typesReward[i] + ".LOW_BIN_RANGE");
          htRewardValues.put("LOW_BIN_RANGE", lowBinRange);
          String highBinRange = config.getString(typesReward[i] + ".HIGH_BIN_RANGE");
          htRewardValues.put("HIGH_BIN_RANGE", highBinRange);
          String checkDigit = config.getString(typesReward[i] + ".INCLUDE_CHECK_DIGIT");
          htRewardValues.put("INCLUDE_CHECK_DIGIT", checkDigit);
          htRewardKeys.put(typesReward[i], htRewardValues);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("\t\t*** Exception in LoyaltyBinRangeUtil static initializer: " + e);
    }
  }

  /**
   * @return
   */
  public static String createExpirationDate() {
    /*
    Date currentDate = new Date();
    int iYY = currentDate.getYear() - 99;
    String sYY = new String((iYY < 10 ? new String("0" + Integer.toString(iYY))
        : new String(Integer.toString(iYY))));
    int iMM = currentDate.getMonth();
    String sMM = new String((iMM < 10 ? new String("0" + Integer.toString(iMM))
        : new String(Integer.toString(iMM))));
    */
    //TD
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, 1);
    String sYY = (""+cal.get(Calendar.YEAR)).substring(2);
    int iMonth = cal.get(Calendar.MONTH);
    String sMM = null;
    if(iMonth < 10) {
    	sMM="0"+iMonth;
    }else{
    	sMM=""+iMonth;
    }
    return (sMM + sYY);
  }

  /**
   * put your documentation comment here
   * @param accountNum
   * @return
   */
  public static Loyalty allocLoyaltyCard(String accountNum) {
    // Check to see if the entered loyalty Number is valid.
    if (!validateEnteredLoyaltyCard(accountNum)) {
      return null;
    } else {
      Loyalty loyalty = new Loyalty();
      return loyalty;
    }
  }

  /**
   * put your documentation comment here
   * @param accountNum
   * @return
   */
  public static RewardCard allocRewardCard(String accountNum) {
    // Check to see if the entered loyalty Number is valid.
    if (!validateEnteredRewardCard(accountNum)) {
      return null;
    } else {
      RewardCard reward = new RewardCard();
      return reward;
    }
  }

  /**
   * put your documentation comment here
   * @param accountNum
   * @return
   */
  public static boolean validateEnteredLoyaltyCard(String accountNum) {
    /**
     * The card type needs to be determined based on the BIN_RANGE.
     */
    String cardType = "";
    if (accountNum.length() < 4)
      return false;
    int code = new Integer(accountNum.substring(0, 4)).intValue();
    System.out.println("Account num : " + accountNum);
    System.out.println("Account num : " + code);
    for (int i = 0; i < types.length; i++) {
      //For Loyalty Card
      Hashtable val = (Hashtable)htLoyaltyKeys.get(types[i]);
      String Start_Range = (String)val.get("LOW_BIN_RANGE");
      String End_Range = (String)val.get("HIGH_BIN_RANGE");
      Integer startRange = new Integer(Start_Range);
      Integer endRange = new Integer(End_Range);
      System.out.println("Start Range : " + startRange);
      System.out.println("End Range : " + endRange);
      if (code >= startRange.intValue() && code <= endRange.intValue()) {
        cardType = types[i];
        CardType = cardType;
        System.out.println("Card Type : " + cardType);
      }
    }
    if (cardType.equals(""))
      return false;
    Hashtable thisCardValues = (Hashtable)htLoyaltyKeys.get(cardType);
    Integer iCardLength = new Integer((String)thisCardValues.get("LENGTH"));
    int cardLen = iCardLength.intValue();
    if (accountNum == null || accountNum.length() < cardLen) {
      System.out.println("check length");
      return false;
    }
    if (((String)thisCardValues.get("INCLUDE_CHECK_DIGIT")).equals("TRUE")) {
      if (!LoyaltyBinRangeUtil.validateCheckDigit(accountNum)) {
        System.out.println("validateCheckDigit");
        return false;
      }
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @param accountNum
   * @return
   */
  public static boolean validateEnteredRewardCard(String accountNum) {
    /**
     * The card type needs to be determined based on the BIN_RANGE.
     */
    String cardType = "";
    int code = new Integer(accountNum.substring(0, 4)).intValue();
    // For Reward Card
    try {
      for (int i = 0; i < typesReward.length; i++) {
        Hashtable valReward = (Hashtable)htRewardKeys.get(typesReward[i]);
        String Start_RangeReward = (String)valReward.get("LOW_BIN_RANGE");
        String End_RangeReward = (String)valReward.get("HIGH_BIN_RANGE");
        Integer startRangeReward = new Integer(Start_RangeReward);
        Integer endRangeReward = new Integer(End_RangeReward);
        if (code >= startRangeReward.intValue() && code <= endRangeReward.intValue()) {
          cardType = typesReward[i];
          CardType = cardType;
        }
      }
      if (cardType.equals(""))
        return false;
    } catch (Exception e) {
      System.out.println("This is the trace:");
      e.printStackTrace();
    }
    Hashtable thisCardValues = (Hashtable)htRewardKeys.get(cardType);
    Integer iCardLength = new Integer((String)thisCardValues.get("LENGTH"));
    int cardLen = iCardLength.intValue();
    if (accountNum == null || accountNum.length() < cardLen) {
      System.out.println("check length");
      return false;
    }
    if (((String)thisCardValues.get("INCLUDE_CHECK_DIGIT")).equals("TRUE")) {
      if (!LoyaltyBinRangeUtil.validateCheckDigit(accountNum)) {
        System.out.println("validateCheckDigit");
        return false;
      }
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @param expCal
   * @return
   */
  public static boolean validateDate(Calendar expCal) {
    Calendar nowCal = Calendar.getInstance();
    nowCal.setTime(new Date());
    // truncate first 2 digits from year
    //     String sNowYear = new Integer(nowCal.get(cal.YEAR)).toString();
    //     nowCal.set(cal.YEAR, Integer.parseInt(sNowYear.substring(2)));
    // if last 2 digits are less than 50, assume year 2000 else 1900
    String sExpYear = new Integer(expCal.get(Calendar.YEAR)).toString();
    String sNowYear = new Integer(nowCal.get(Calendar.YEAR)).toString();
    // if last 2 digits are less than 50, assume year 2000 else 1900
    //     if (nExpYear < 50)
    //       nExpYear+=100;
    //     cal.set(cal.YEAR, nExpYear);
    if (expCal.get(Calendar.YEAR) > nowCal.get(Calendar.YEAR)) {
      return true;
    } else if (expCal.get(Calendar.YEAR) < nowCal.get(Calendar.YEAR)) {
      return false;
      // years must be the same, so check month
    } else if (expCal.get(Calendar.MONTH) < nowCal.get(Calendar.MONTH)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * put your documentation comment here
   * @param sAccountNum
   * @return
   */
  public static boolean validateCheckDigit(String sAccountNum) {
    if (sAccountNum == null)
      return false;
    sAccountNum = removeSpaces(sAccountNum);
    try {
      int checkSum = 0;
      for (int x = sAccountNum.length() - 1; x >= 1; x = x - 2) {
        String digit = sAccountNum.substring(x - 1, x);
        int nDigit = Integer.parseInt(digit);
        nDigit = nDigit * 2;
        if (nDigit < 10) {
          checkSum += nDigit;
        } else {
          String sTemp = new Integer(nDigit).toString();
          int nLeft = Integer.parseInt(sTemp.substring(0, 1));
          int nRight = Integer.parseInt(sTemp.substring(1, 2));
          checkSum += (nLeft + nRight);
        }
      }
      for (int x = sAccountNum.length() - 2; x >= 1; x = x - 2) {
        String digit = sAccountNum.substring(x - 1, x);
        checkSum += Integer.parseInt(digit);
      }
      int mod = checkSum % 10;
      if (mod != 0) {
        mod = 10 - mod;
      }
      int lastDigit = Integer.parseInt(sAccountNum.substring(sAccountNum.length() - 1));
      if (lastDigit != mod) {
        return false;
      }
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
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

