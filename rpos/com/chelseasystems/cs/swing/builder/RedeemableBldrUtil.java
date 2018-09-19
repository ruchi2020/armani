/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 1    | 04-28-2005 |Khayti     |           | Redeemable Management                                                |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.*;


/**
 * put your documentation comment here
 */
public class RedeemableBldrUtil {
  private static String types[] = null;
  private static Hashtable htRedeemKeys = null;
  private static Hashtable htRedeemValues = null;
  private static ResourceBundle res = null;
  //ks: Read the redeemable.cfg to get the values to check the card
  static {
    htRedeemKeys = new Hashtable();
    htRedeemValues = new Hashtable();
    res = ResourceManager.getResourceBundle();
    try {
      ConfigMgr config = new ConfigMgr("redeemable.cfg");
      String redeemTypes = config.getString("REDEEMABLE_LIST");
      StringTokenizer stk = new StringTokenizer(redeemTypes, ",");
      types = new String[stk.countTokens()];
      int i = -1;
      while (stk.hasMoreTokens()) {
        types[++i] = stk.nextToken();
        htRedeemValues = new Hashtable();
        String redeemType = config.getString(types[i] + ".TYPE");
        htRedeemValues.put("TYPES", redeemType);
        String redeemLength = config.getString(types[i] + ".LENGTH");
        htRedeemValues.put("LENGTH", redeemLength);
        String lowBinRange = config.getString(types[i] + ".LOW_BIN_RANGE");
        htRedeemValues.put("LOW_BIN_RANGE", lowBinRange);
        String highBinRange = config.getString(types[i] + ".HIGH_BIN_RANGE");
        htRedeemValues.put("HIGH_BIN_RANGE", highBinRange);
        String checkDigit = config.getString(types[i] + ".INCLUDE_CHECK_DIGIT");
        htRedeemValues.put("INCLUDE_CHECK_DIGIT", checkDigit);
        //Vivek Mishra : Added to allow gift card length range from 10 to 16 
        String redeemMinLength = config.getString(types[i] + ".MINLENGTH");
        if(redeemMinLength!=null)
        	htRedeemValues.put("MINLENGTH", redeemMinLength);	
        String redeemMaxLength = config.getString(types[i] + ".MAXLENGTH");
        if(redeemMaxLength!=null)
        	htRedeemValues.put("MAXLENGTH", redeemMaxLength);
        //Ends
        //Mayuri Edhara :: 10-MAR-17 adding the fixed bin range for the new gift cards
        String fixedBinRange = config.getString(types[i] + ".FIXED_BIN_RANGE");
        if(fixedBinRange != null){
        	htRedeemValues.put("FIXED_BIN_RANGE", fixedBinRange);
        }
        //ends
        htRedeemKeys.put(types[i], htRedeemValues);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("\t\t*** Exception in RedeemableBldrUtil static initializer: " + e);
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
   * Mayuri Edhara :: 10-MAR-17 - validate the AJB Implemented gift card entry.
   * put your documentation comment here
   * @param cardType
   * @param accountNum
   * @return
   */
  public static boolean validateGiftCard(String cardType, String accountNum) {  
    
	Hashtable thisCardValues = (Hashtable)htRedeemKeys.get(cardType);
    Integer iCardMaxLength = null;  
    Integer iCardMinLength = null;
    if(thisCardValues.get("MAXLENGTH")!=null) {
    	iCardMaxLength = new Integer((String)thisCardValues.get("MAXLENGTH"));
    	System.out.println(" Inside MAXLENGTH    :"+iCardMaxLength);
    }
    //Ruchi adding check for gift card length
    if(thisCardValues.get("MINLENGTH")!=null) {
    	iCardMinLength = new Integer((String)thisCardValues.get("MINLENGTH"));
        System.out.println(" Inside MINLENGTH    :"+iCardMinLength);
    }
   // System.out.println("cardMaxLen    :"+cardMaxLen);
    int cardMaxLen = iCardMaxLength.intValue();
    int cardMinLen = iCardMinLength.intValue();
    
    System.out.println("****** New validateGiftCard >> LENGTH " + cardMaxLen); 
    if (accountNum == null && (accountNum.length() < cardMinLen||accountNum.length() > cardMaxLen)) {
    	return false;
    }
    System.out.println(" printing variable INCLUDE_CHECK_DIGIT :"+((String)thisCardValues.get("INCLUDE_CHECK_DIGIT")));
    if (((String)thisCardValues.get("INCLUDE_CHECK_DIGIT")).equals("TRUE")) {
    	  if (!RedeemableBldrUtil.validateCheckDigit(accountNum)) {
        
        return false;
      }
    }
    
    //Get the fixed length card first. If it does not satisfy then check the low bin and high bin ranges.
    int fixedCode = new Integer(accountNum.substring(0, 6)).intValue();    
    int fixedBinRange = new Integer((String)thisCardValues.get("FIXED_BIN_RANGE")).intValue();
    int code = new Integer(accountNum.substring(0, 4)).intValue();
    int lowBin = new Integer((String)thisCardValues.get("LOW_BIN_RANGE")).intValue();
    int highBin = new Integer((String)thisCardValues.get("HIGH_BIN_RANGE")).intValue();
    
    if(fixedCode == fixedBinRange){
    	return true;    	
    } else {
    	// if the 
    	if ((code >= lowBin) && (code <= highBin)) {    	 	
    		return true;
          }
			return false;
    }
    
  }

  /**
   * put your documentation comment here
   * @param cardType
   * @param accountNum
   * @return
   */
  public static boolean validateEnteredCard(String cardType, String accountNum) {
    System.out.println("****** validateEnteredCard");
    Hashtable thisCardValues = (Hashtable)htRedeemKeys.get(cardType);
    Integer iCardLength = new Integer((String)thisCardValues.get("LENGTH"));
    //Vivek Mishra : Added to allow gift card length range from 10 to 16
    Integer iCardMinLength = null;
    Integer iCardMaxLength = null;
    if(thisCardValues.get("MINLENGTH")!=null && thisCardValues.get("MAXLENGTH")!=null)
    {
    iCardMinLength = new Integer((String)thisCardValues.get("MINLENGTH"));
    iCardMaxLength = new Integer((String)thisCardValues.get("MAXLENGTH"));
    }
    //Ends
    int cardLen = iCardLength.intValue();
    System.out.println("****** validateEnteredCard222");
    //Vivek Mishra : Added to allow gift card length range from 10 to 16
    if(iCardMinLength!=null && iCardMaxLength!=null)
    {
    	if (accountNum == null || !(accountNum.length()>=iCardMinLength && accountNum.length()<=iCardMaxLength)) {
    	      System.out.println("check length");
    	      return false;
    	    }
    }
    else//Ends
    {
    if (accountNum == null || accountNum.length() < cardLen) {
      System.out.println("check length");
      return false;
    }
    }
    System.out.println("****** validateEnteredCard333");
    if (((String)thisCardValues.get("INCLUDE_CHECK_DIGIT")).equals("TRUE")) {
      if (!RedeemableBldrUtil.validateCheckDigit(accountNum)) {
        System.out.println("validateCheckDigit");
        return false;
      }
    }
    System.out.println("****** validateEnteredCard444");
    int code = new Integer(accountNum.substring(0, 4)).intValue();
    int lowBin = new Integer((String)thisCardValues.get("LOW_BIN_RANGE")).intValue();
    int highBin = new Integer((String)thisCardValues.get("HIGH_BIN_RANGE")).intValue();
    if ((code >= lowBin) && (code <= highBin)) {
    	//Vivek Mishra : Added to allow gift card length range from 10 to 16
    	 if(iCardMinLength!=null && iCardMaxLength!=null){	
    	      if(!(accountNum.length()>=iCardMinLength && accountNum.length()<=iCardMaxLength)){
    	    	  System.out.println("innder");
    	          return false;
    	      }
    	    }	//Ends
    else if (accountNum.length() != cardLen) {
        System.out.println("innder");
        return false;
      }
      return true;
    } else {
      System.out.println("outher");
      return false;
    }
  }

  /**
   * put your documentation comment here
   * @param expCal
   * @return
   */
  public static boolean validateDate(Calendar expCal) {
    Calendar nowCal = Calendar.getInstance();
    System.out.println("nowCal   :"+nowCal);
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
	  System.out.println("Inside validateCheckDigit   :"+sAccountNum);
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

