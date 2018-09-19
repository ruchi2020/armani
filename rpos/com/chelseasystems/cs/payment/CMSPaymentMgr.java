/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.IConfig;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.util.Trace;
import java.io.PrintStream;
import com.chelseasystems.cr.payment.*;
import java.util.*;

import com.chelseasystems.cr.currency.ArmCurrency;


// Referenced classes of package com.chelseasystems.cr.payment:
//            Payment, PaymentRenderer
/**
 *
 * <p>Title: CMSPaymentMgr</p>
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
public class CMSPaymentMgr extends PaymentMgr implements IConfig {
  //  public static final int PAYMENT = 0;
  //  public static final int CHANGE = 1;
  //  public static final int REFUND = 2;
  //  public static final int CREDIT = 0;
  //  public static final int DEBIT = 1;
  private static ConfigMgr mgr = new ConfigMgr("payment.cfg");
  //ruchi
  private static String paymentKeys[] = null;
  
  /**Ruchi for Canada
   * This static block read the payment.cfg file and populate the array of
   * available Dolcy candies
  */
  static {
	try {
      String paymentTypes = mgr.getString("MOBILE_TERMINAL_TENDER_OPTIONS");
      StringTokenizer stk = new StringTokenizer(paymentTypes, ",");
      paymentKeys = new String[stk.countTokens()];
       while(stk.hasMoreElements()){
	       for(int i=0; i <paymentKeys.length;i++){
	    	   paymentKeys[i] =(String)stk.nextElement();
		    }
        }
      } catch (Exception e) {
      System.out.println("\t\t*** Exception in CMSPaymentMgr static initializer: " + e);
    }
  }
  

  //  private static Map htPayments = null;
  //  private static Map htPaymentRenderers = new HashMap();
  //  private static Map htSeq = null;
  //  private static Map htSession = null;
  //  private static Map htCallCenterDesc = null;
  //  private static Map htRptPaymentsDesc = null;
  //  private static Vector rptCreditPayments = null;
  /**
   * This method is to get the String that refers to the given Call Center
   * Description Key
   * @param payment
   * @return String
   */
  public static String getCallCenterDesKey(String payment) {
    System.out.println("getCallCenterDesKey: "
        + mgr.getString(payment + ".CALL_CENTER_DISPLAY").toString());
    return new String(mgr.getString(payment + ".CALL_CENTER_DISPLAY").toString());
  }

  /**
   * Method used to get Maximum change allowed
   * @param payment String
   * @return Currency
   */
  public static ArmCurrency getMaxChangeAllowed(String payment) {
    return new ArmCurrency(mgr.getDouble(payment + ".MAX_CHANGE_ALLOWED").doubleValue());
  }

  /**
   * put your documentation comment here
   * @param payment
   * @return
   */
  public static ArmCurrency getMinDenomAllowed(String payment) {
    return new ArmCurrency(mgr.getDouble(payment + ".MIN_DENOM_ALLOWED").doubleValue());
  }

  /**
   * Method used to get credit note type
   * @param key String
   * @return String
   */
  public static String getCreditNoteType(String key) {
    return mgr.getString(key + ".TYPE").toString();
  }

  //ks: For EOD Default total attribute added to the payment.cfg
  /**
   * Method used to get EOD payment total
   * @param key String
   * @return boolean
   */
  public static boolean getIsEODDefaultTotal(String key) {
    String temp = mgr.getString(key + ".EOD_DEFAULT_TOTAL");
//Merge from Europe
    if(temp == null || temp.length() == 0) {
      String typeId = getGroupingByPaymentCode(key);
      if(typeId != null && !typeId.equalsIgnoreCase(key))
        return getIsEODDefaultTotal(typeId);
      temp = "false";
    }
    Boolean hidden = new Boolean(temp);
    return hidden != null ? hidden.booleanValue() : false;
  }
//Merge from Europe
  public static boolean getShouldHideTotal(String key)
  {
    String typeId = getGroupingByPaymentCode(key);
    if (typeId == null)
      typeId = key;
    return PaymentMgr.getShouldHideTotal(typeId);
  }

  /**
   * put your documentation comment here
   * @param payment
   * @return
   */
  public static ArmCurrency getIRSAlertAmount(String payment) {
    if (mgr.getDouble(payment + ".IRS_ALERT_AMOUNT") != null)
      return new ArmCurrency(mgr.getDouble(payment + ".IRS_ALERT_AMOUNT").doubleValue());
    return null;
  }

  /**
   * put your documentation comment here
   * @param paymentType
   * @return
   */
  public static List getPaymentCode(String paymentType) {
    int i = 0;
    List listPaymentCodes = new ArrayList();
    try {
      ConfigMgr payConfig = new ConfigMgr("ArmPaymentCommon.cfg");
      String paymentCodes = payConfig.getString(paymentType);
      if (paymentCodes != null) {
        StringTokenizer stk = new StringTokenizer(paymentCodes, ",");
        String[] payCodes = new String[stk.countTokens()];
        for (; stk.hasMoreTokens(); ) {
          //Hashtable htPaymentCodes = new Hashtable();
          payCodes[i] = stk.nextToken();
          String paymentCode = payConfig.getString(payCodes[i] + ".CODE");
          String paymentDesc = payConfig.getString(payCodes[i] + ".DESC");
          CMSPaymentCode payCode = new CMSPaymentCode(paymentType, paymentCode, paymentDesc);
          //htPaymentCodes.put(paymentCode, paymentDesc);
          listPaymentCodes.add(payCode);
          i++;
        }
      }
      return listPaymentCodes;
    } catch (Exception e) {
      e.printStackTrace();
      return listPaymentCodes;
    }
  }

  //ks: Read PAYMENT.CODE from the payment.cfg
  public static String getOldPaymentCode(String key) {
    String code = mgr.getString(key + ".CODE");
    return code;
  }

//Merge from Europe
  public static CMSPaymentCode getPaymentCodeObjectByCode(String paymentCode) {
    String[] keys = getAllPaymentKeys();
    //Vector groupVec = new Vector();
    for(int i=0; i<keys.length; i++) {
      //groupVec.addAll(getPaymentCode(keys[i]));
      List payCodesList = getPaymentCode(keys[i]);
      for(int j=0; j<payCodesList.size(); j++) {
        if(((CMSPaymentCode)payCodesList.get(j)).getPaymentCode().equalsIgnoreCase(paymentCode))
          return ((CMSPaymentCode)payCodesList.get(j));
      }
    }
    return null;
  }


  public static String getPaymentDescByCode(String paymentCode) {
    String[] keys = getAllPaymentKeys();
    //Vector groupVec = new Vector();
    for(int i=0; i<keys.length; i++) {
      //groupVec.addAll(getPaymentCode(keys[i]));
      List payCodesList = getPaymentCode(keys[i]);
      for(int j=0; j<payCodesList.size(); j++) {
        if(((CMSPaymentCode)payCodesList.get(j)).getPaymentCode().equalsIgnoreCase(paymentCode)){
          return ((CMSPaymentCode)payCodesList.get(j)).getPaymentDesc();
        }
      }
    }
    return null;
  }


  public static String getGroupingByPaymentCode(String paymentCode) {
    String[] keys = getAllPaymentKeys();
    //Vector groupVec = new Vector();
    for(int i=0; i<keys.length; i++) {
      //groupVec.addAll(getPaymentCode(keys[i]));
      List payCodesList = getPaymentCode(keys[i]);
      for(int j=0; j<payCodesList.size(); j++) {
        if(((CMSPaymentCode)payCodesList.get(j)).getPaymentCode().equalsIgnoreCase(paymentCode))
          return ((CMSPaymentCode)payCodesList.get(j)).getPaymentType();
      }
    }
    return null;
  }


  public static String getGroupingByPaymentCodeDesc(String paymentCodeDesc) {
    String[] keys = getAllPaymentKeys();
    //Vector groupVec = new Vector();
    for(int i=0; i<keys.length; i++) {
      //groupVec.addAll(getPaymentCode(keys[i]));
      List payCodesList = getPaymentCode(keys[i]);
      for(int j=0; j<payCodesList.size(); j++) {
        if(((CMSPaymentCode)payCodesList.get(j)).getPaymentDesc().equalsIgnoreCase(paymentCodeDesc))
          return ((CMSPaymentCode)payCodesList.get(j)).getPaymentType();
      }
    }
    return null;
  }
//End Merge from Europe
  //Ruchi
  public static String[] getMobileTenderNames() {
		  return paymentKeys;
	  }
  
   //ruchi
}
