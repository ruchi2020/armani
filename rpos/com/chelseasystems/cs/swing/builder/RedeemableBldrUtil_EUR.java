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
import com.chelseasystems.cr.currency.ArmCurrency;

import java.util.*;




/**
 * put your documentation comment here
 */
public class RedeemableBldrUtil_EUR {
	
	private static String giftcardBarcodes[] = null;
	private static Hashtable htGiftCardKeys = null;
	private static int maxBarcodeLength = 13;
	private static String GIFT_IDENTIFIER = "99";
    private static String types[] = null;
    private static Hashtable htRedeemKeys = null;

    static {
        htGiftCardKeys = new Hashtable();
        Hashtable htRedeemKeys = new Hashtable();
        try {
          ConfigMgr config = new ConfigMgr("redeemable.cfg");
          String giftcardBarcode = config.getString("REDEEMABLE_LIST_EUR");
                //System.out.println("giftcardBarcode"+ giftcardBarcode);
          try{
        	  //BARCODE_LENGTH = Integer.parseInt(config.getString("BARCODE_LENGTH"));
        	  GIFT_IDENTIFIER = config.getString("GIFT_IDENTIFIER");
          }
          catch(Exception e){  }      
          
          StringTokenizer stkGCBarcodes = null;
          int i = -1;
          if (giftcardBarcode != null) {
        	  stkGCBarcodes = new StringTokenizer(giftcardBarcode, ",");
          }
          if (stkGCBarcodes != null) {
        	  giftcardBarcodes = new String[stkGCBarcodes.countTokens()];
            while (stkGCBarcodes.hasMoreTokens()) {
            	giftcardBarcodes[++i] = stkGCBarcodes.nextToken();
            	Hashtable htGiftCardValues	 = new Hashtable();
            	String lowBinRange = config.getString(giftcardBarcodes[i] + ".LOW_BIN_RANGE");
              htGiftCardValues.put("LOW_BIN_RANGE", lowBinRange);
              String highBinRange = config.getString(giftcardBarcodes[i] + ".HIGH_BIN_RANGE");
              htGiftCardValues.put("HIGH_BIN_RANGE", highBinRange);
              String amount = config.getString(giftcardBarcodes[i] + ".GIFT_CARD_AMOUNT");
              htGiftCardValues.put("GIFT_CARD_AMOUNT", amount);
              String checkDigit = config.getString(giftcardBarcodes[i] + ".INCLUDE_CHECK_DIGIT");
              htGiftCardValues.put("INCLUDE_CHECK_DIGIT", checkDigit);              
              String redeemType = config.getString(giftcardBarcodes[i] + ".TYPE");
              htGiftCardValues.put("TYPES", redeemType);            
              String redeemLength = config.getString(giftcardBarcodes[i] + ".LENGTH");
              htGiftCardValues.put("LENGTH", redeemLength);                 
              
              htGiftCardKeys.put(giftcardBarcodes[i], htGiftCardValues);
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("\t\t*** Exception in GiftCardBinRangeUtil static initializer: " + e);
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
   * @param cardType
   * @param accountNum
   * @return
   */
  public static boolean validateEnteredCard(String accountNum) {
    //System.out.println("****** validateEnteredCard");
     //System.out.println("AccountNum   >>>>"+accountNum); 
     //Hashtable thisCardValues = (Hashtable)htRedeemKeys.get(cardType);
     if(accountNum == null || accountNum.trim().length()<1){
    	 return false;
     }
	if (! accountNum.substring(0, 2).equals(GIFT_IDENTIFIER)){
		return false;
	}
    
	if(accountNum.length() > maxBarcodeLength){
		return false;
	}	
	if (accountNum == null || accountNum.length() < maxBarcodeLength) {
	      //System.out.println("check length");
	      return false;
	    }
	Hashtable giftCard = getGiftCardFromBarcode(accountNum);	

	if(giftCard == null){
		return false;
	}
	
    //System.out.println("****** validateEnteredCard333");
    if (((String)giftCard.get("INCLUDE_CHECK_DIGIT")).equals("TRUE")) {
      if (!RedeemableBldrUtil_EUR.validateCheckDigit(accountNum)) {
        //System.out.println("validateCheckDigit");
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
  
  public static Hashtable getGiftCardFromBarcode(String accountNum) {

	  	int code = new Integer(accountNum.substring(0, 6)).intValue();
		//System.out.println("Account num : " + accountNum);
		//System.out.println("Account num : " + code);
		for (int i = 0; i < giftcardBarcodes.length; i++) {
			//For Loyalty Card
			Hashtable val = (Hashtable) htGiftCardKeys.get(giftcardBarcodes[i]);
			String Start_Range = (String) val.get("LOW_BIN_RANGE");
			String End_Range = (String) val.get("HIGH_BIN_RANGE");
			Integer startRange = new Integer(Start_Range);
			Integer endRange = new Integer(End_Range);
			//System.out.println("Start Range : " + startRange);
			//System.out.println("End Range : " + endRange);
			if (code >= startRange.intValue() && code <= endRange.intValue()) {
				return (val);
			}
		}
		return null;
	}

	public static Hashtable getGiftCardFromAmount(String amount) {

		if (amount.length() < 1)
			return null;

		for (int i = 0; i < giftcardBarcodes.length; i++) {
			//For Loyalty Card
			Hashtable val = (Hashtable) htGiftCardKeys.get(giftcardBarcodes[i]);
			String gcAmount = (String) val.get("GIFT_CARD_AMOUNT");
			if (amount.equals(gcAmount)) {
				return (val);
			}
		}

		return null;
	}

	public static ArmCurrency getGiftCardAmount(String barcode) {
		Hashtable htGiftCard = getGiftCardFromBarcode(barcode);
		if (htGiftCard != null) {
			String sValue = (String) htGiftCard.get("GIFT_CARD_AMOUNT");
			return new ArmCurrency(sValue);
		}
		return null;
	}

	public static ArmCurrency getGiftCardBarcode(String amount) {
		Hashtable htGiftCard = getGiftCardFromAmount(amount);
		if (amount != null) {
			String sValue = (String) htGiftCard.get("GIFT_CARD_AMOUNT");
			return new ArmCurrency(sValue);
		}
		return null;
	}

	public static boolean isValidBarcode(String barcode) {
		boolean isValid = (barcode.length() <= maxBarcodeLength && barcode
				.substring(0, 2).equals(GIFT_IDENTIFIER));
		return isValid;
	}  
  
}

