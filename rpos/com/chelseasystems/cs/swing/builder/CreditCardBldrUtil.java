/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 06-21-2005 | Vikram    |  257      |The correct Bin Range for JCB is 3528 to 3589.|
 |      |            |           |           |It is a 16 digit number.                      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 06-03-2005 | Megha     |  92       |  Payment/Credit Card: Diners Card account    |
 |      |            |           |           |  numbers are not accepted :                  |
 |      |            |           |           |  Modified: allocCreditCardPayment            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-01-2005 | Megha     | Internal  |   Payment/Credit Card: Diner Credit Card     |
 |      |            |           | Bug       |   account numbers are not accepted           |
 |      |            |           |           |   Modified: allocCreditCardPayment           |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 06-01-2005 | Megha     |    91     |   Payment/Credit Card: JCB Credit Card       |
 |      |            |           |           |   account numbers are not accepted           |
 |      |            |           |           |                                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 06-01-2005 | Megha     |    93     |   Payment/Credit Card: Discover account      |
 |      |            |           |           |   numbers should NOT be accepted             |
 |      |            |           |           |                                              |
 +------+------------+-----------+-----------+----------------------------------------------+
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


/**
 */
public class CreditCardBldrUtil {

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
    //Removed the Date - Depricated API
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
  public static Payment allocCreditCardPayment(String accountNum) {
    if (accountNum == null || accountNum.length() < 5) {
      return null;
    }
    /*
     if (this.creditOrDebit != null && this.creditOrDebit.equals("D")) {
     return new DebitCard();
     }
     */
    if (!CreditCardBldrUtil.validateCheckDigit(accountNum))
      return null;
    int code = new Integer(accountNum.substring(0, 2)).intValue();
    int code4 = new Integer(accountNum.substring(0, 4)).intValue();
    // american express
    if ((code == 34) || (code == 37)) {
      if (accountNum.length() != 15) {
        return null;
      }
      Amex amx = new Amex(IPaymentConstants.AMERICAN_EXPRESS);
      return amx;
    }
    /* Bug # 92 Diners Card:  Payment/Credit Card: Diners Card account numbers are not accepted
     */
    if (((code >= 51) && (code <= 55)) || (code == 36)) {
      // code 36 with account number length 16 represents Master Cards and
      // code 36 with account number length 14 represents Diners Master Card.
      // If the code is 36 check to see if the account number length is either
      // 16 or 14. If not, return null.
      if (code == 36) {
        if (accountNum.length() != 16 && accountNum.length() != 14) {
          return null;
        }
      } else if (accountNum.length() != 16) {
        return null;
      }

      //  code 36 with account number length 14 represents JCB
      if((code == 36) && (accountNum.length()==14)){
    	  JCBCreditCard jcb = new JCBCreditCard(CMSIPaymentConstants.JCB);
    	  return jcb;
      }else{
      MasterCard mc = new MasterCard(IPaymentConstants.BANK_CARD);
      return mc;
      }
    }
    if ((code >= 40) && (code <= 49)) {
      if (accountNum.length() != 16 && accountNum.length() != 13) {
        return null;
      }
      Visa visa = new Visa(IPaymentConstants.BANK_CARD);
      return visa;
    }
    // For Bug #93
    // Discover card not supported.( Payment/Credit Card: Discover account not supported)
    //Issue # 1849 US wants to get support for Discover card.
       /*if (code4 == 6011)  {
             if (accountNum.length() != 16) {
                return  null;
             }
             Discover dis = new Discover(IPaymentConstants.DISCOVER);
             return  dis;
          }
          */
    // For JCB Credit Card.
    //Issue # 1849 add the Discover bin ranges into JCB.  So if a Discover card is used, it will be the same as if a JCB card was used.
    int codeJCB = new Integer(accountNum.substring(0, 4)).intValue();
    int codeCUP = new Integer(accountNum.substring(0, 8)).intValue();
    boolean IsJCBObj = false;
    // for bug#91 : JCB credit-card account numbers are not accepted changed code to codeJCB
    // 1849: updated bin ranges.
    //added new BIN Ranges to support discover card
    if (((codeJCB >= 3000) && (codeJCB <= 3059))
    		||(codeJCB == 3095)
    		||((codeJCB >= 3528) && (codeJCB <= 3589))
    		||((codeJCB >= 3600) && (codeJCB <= 3699))
    		||((codeJCB >= 3800) && (codeJCB <= 3999))
    		||(codeJCB == 6011)
//    		||((codeJCB == 6500)&& (codeJCB ==6509))
    		||((codeCUP >= 62212600)&& (codeCUP <= 62292599))
    		||((codeJCB >= 6240) && (codeJCB <= 6269))
    		||((codeJCB >= 6282) && (codeJCB <= 6288))
    		||((codeJCB >= 6440) && (codeJCB <= 6599))
    		||((codeCUP >= 65000000)&& (codeCUP <= 65099999))) {
      IsJCBObj = true;
      if ((IsJCBObj == true) && (accountNum.length() == 16)) {
        JCBCreditCard jcb = new JCBCreditCard(CMSIPaymentConstants.JCB);
        return jcb;
      }
    }
    // For North American Diners Club CreditCard
    // i.	To extend MasterCard bin range to include International Diners Club
    //1.	Identifier - 36.
    //ii.	To add bin range calculations for North American Diners Club credit card.
    //1.	Identifier  300  305
    //2.	 380  388.
    // for internal bug : Diners credit-card account numbers are not accepted changed code to codeJCBDiners
    int codeJCBDiners = new Integer(accountNum.substring(0, 3)).intValue();
    boolean IsDinersObj = false;
    
    
    
    //Comment following code as the new BIN RANGES added for JCB and DINERS CREDIT CARD.
	    /*if ((codeJCBDiners >= 300) && (codeJCBDiners <= 305)
	        || ((codeJCBDiners >= 380) && (codeJCBDiners <= 389))) {
	            IsDinersObj = true;
	            if ((IsDinersObj == true) && (accountNum.length() == 14)) {
	            	Diners diner = new Diners(CMSIPaymentConstants.diner);
	            	return diner;
                }
  
	        
	    */
    	int jcbNewCode = new Integer(accountNum.substring(0,3)).intValue();
       	if((jcbNewCode>=360)&&(jcbNewCode<=369))
       	 //((jcbNewCode>=300) && (jcbNewCode<=305)
    		//||((jcbNewCode>=309)&&(jcbNewCode<=309))	
    		//||((jcbNewCode>=360)&&(jcbNewCode<=369))
    		//||((jcbNewCode>=380)&&(jcbNewCode<=399))    		
    	{
    		IsDinersObj = true; 
            if ((IsDinersObj == true) && (accountNum.length() == 14)) {
    		 JCBCreditCard jcb = new JCBCreditCard(CMSIPaymentConstants.JCB);
    	    return jcb;
    	}
    
       	} else
      return null;
    //     if (code == 50) {
    //        if (accountNum.length() != 11) {
    //           theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
    //           return null;
    //        }
    //        TMW tmw = new TMW();
    //        return tmw;
    //     }
    return null;
  }
  //for Canada
  public static Payment allocCreditCardPaymentforCanada(String accountNum) {
	    if (accountNum == null || accountNum.length() < 5) {
	      return null;
	    }
	    CreditCard cc = new CreditCard(IPaymentConstants.CREDIT_CARD);
	    return cc;
	   }
  
  //Checking for card number null and mininum number of digits and returning credit card object. BIN lookup will be done using Fipay.
  public static Payment allocCreditCardObject(String accountNum) {
	    if (accountNum == null || accountNum.length() < 5) {
	      return null;
	    }
	    CreditCard cc = new CreditCard(IPaymentConstants.CREDIT_CARD);
	    return cc;
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
   * @param sDate
   * @return
   */
  public static Calendar getCalendar(String sDate) {
    if (sDate == null || sDate.length() != 4)
      return null;
    Calendar cal = Calendar.getInstance();
    String month = sDate.substring(2);
    int nMonth = new Integer(month).intValue();
    if (nMonth < 1 || nMonth > 12)
      return null;
    String year = sDate.substring(0, 2);
    int nYear = new Integer(year).intValue();
    if (nYear < 50)
      nYear += 2000; // cal can't deal with year 0
    else
      nYear += 1900;
    cal.set(cal.YEAR, nYear);
    cal.set(cal.MONTH, nMonth - 1);
    cal.set(Calendar.DATE, 1);
    return cal;
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

