/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright:   Copyright (c) 1999
// Company:     Chelsea Market Systems


package com.chelseasystems.cs.authorization;

import java.util.*;
import java.lang.*;
import java.text.*;
import java.io.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.collection.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.authorization.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.util.CreditAuthUtil;


/**
 *
 * <p>Title: ISDValidation.Java</p>
 *
 * <p>Description: This class is used to create ISD validation requests strings.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Karen Easley/Angela Tritter
 * @version 1.0
 */
public class ISDValidation extends PaymentValidationRequests implements java.io.Serializable
    , IConfig {
  private static final long serialVersionUID = 3202643702934024444L;
  private static ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  /* Track Numbers */
  private static final String KEYED_TRACK_NO = "0";
  private static final String TRACK_ONE = "1";
  private static final String TRACK_TWO = "2";

  /* Indicates scanned or keyed in */
  private static final String KEYED_IN = "0";
  private static final String SCANNED_IN = "1";

  /* Indicates if data digits or characters */
  private static final int DIGITS = 1;
  private static final int CHARACTERS = 2;

  /* Constants for some field lengths */
  private static final int MAX_ACCT_NUM_LENGTH = 19;
  private static final int MAX_AMOUNT_LENGTH = 11;

  /* TBD used to create unique id for each ISD credit query */
  private static int id = 0;
  private static long timeStamp = 0;

  /* Used to obtain configuration parameters from config files */
  private ConfigMgr config;

  /* Test and debug features from configuration file */
  boolean localTestMode = false;
  boolean verboseMode = false;
  boolean rawDataRequest = false;

  /** Constructor **/
  public ISDValidation() {
	config = new ConfigMgr("credit_auth.cfg");
    String strLocalTestMode = config.getString("LOCAL_TEST_MODE");
    String strVerboseMode = config.getString("VERBOSE_MODE");
    // Start Issue # 991
    String strRawDataRequest = config.getString("RAW_DATA_REQUEST");
    if (strRawDataRequest.equalsIgnoreCase("TRUE")) {
      rawDataRequest = true;
    }
    // End Issue # 991
    if (strLocalTestMode.equalsIgnoreCase("TRUE")) {
      localTestMode = true;
    }
    if (strVerboseMode.equalsIgnoreCase("TRUE")) {
      verboseMode = true;
    }
    System.out.println("VERBOSE MODE CC=" + verboseMode);
    System.out.println("TEST MODE CC=" + localTestMode);
    // Issue # 991
    System.out.println("RAW DATA REQUEST=" + rawDataRequest);
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // Helper Function: getRightJustifiedNumber
  /**
   * This method returns the orgString as a Right justified number
   * with a length of leng.
   * @param  orgString   String to left justify
   * @param  leng        Length to make string
   * @result             String containing the result as DIGITS
   */
  private String getRightJustifiedNumber(String orgString, int leng) {
    String inStr = orgString.trim();
    int diff = leng - inStr.length();
    String result = null;
    if (diff > 0) {
      String temp = createZeroString(diff);
      result = temp.concat(inStr);
    } else if (diff < 0) {
      if (verboseMode) {
        System.err.println(
            "INFO -- ISDValidation.getRightJustifiedNumber() input too large.  Input: >" + inStr
            + "<   Max. length: " + leng);
      }
      result = inStr.substring(0, leng);
    } else {
      result = new String(inStr);
    }
    return result;
  }

  // Helper Function: getLeftJustifiedNumber
  /**
   * This method returns the orgString as a Left justified number
   * with a length of leng.
   * @param  orgString   String to left justify
   * @param  leng        Length to make string
   * @result             String containing the result as DIGITS
   */
  private String getLeftJustifiedNumber(String orgString, int leng) {
    String inStr = orgString.trim();
    int diff = leng - inStr.length();
    String result = null;
    if (diff > 0) {
      result = inStr.concat(createZeroString(diff));
    } else if (diff < 0) {
      if (verboseMode) {
        System.err.println(
            "INFO -- ISDValidation.getLeftJustifiedNumber() input too large.  Input: >" + inStr
            + "<   Max. length: " + leng);
      }
      result = inStr.substring(0, leng);
    } else {
      result = new String(inStr);
    }
    return result;
  }

  // Helper Function: getLeftJustifiedString
  /**
   * This method returns the orgString as a left justified String
   * with a length of leng.
   * @param  orgString   String to left justify
   * @param  leng        desired length
   * @result String containing the result
   */
  private String getLeftJustifiedString(String orgString, int leng) {
    String inStr = orgString.trim();
    int diff = leng - inStr.length();
    String result = null;
    if (diff > 0) {
      result = inStr.concat(createBlankString(diff));
    } else if (diff < 0) {
      if (verboseMode) {
        System.err.println(
            "INFO -- ISDValidation.getLeftJustifiedString() input too large.  Input: >" + inStr
            + "<   Max. length: " + leng);
      }
      result = inStr.substring(0, leng);
    } else {
      result = new String(inStr);
    }
    return result;
  }

  // Helper Function: getRightJustifiedAccountNumber
  /**
   * Return account number right-justified
   * @return String containing the result
   */
  private String getRightJustifiedAccountNumber(String accountNum) {
    String inAcctNum = accountNum.trim();
    int diff = MAX_ACCT_NUM_LENGTH - inAcctNum.length();
    if ((diff < 0) && (verboseMode)) {
      System.out.println("ISD.getRightJustifiedAccountNumber: ERROR account number too large");
    }
    return getRightJustifiedNumber(inAcctNum, MAX_ACCT_NUM_LENGTH);
  }

  // Helper Function: getRightJustifiedAmount
  /**
   * Return amount number right-justified, minus decimal point, with two implied
   * decimal places.
   * @param   rawAmount   Number containing the amount
   * @return              String containing the result as digits
   */
  private String getRightJustifiedAmount(double rawAmount) {
    Double dRawAmount = new Double(rawAmount);
    String amount = new String(dRawAmount.toString()).trim();
    int length = amount.length();
    int decimalPoint = amount.indexOf('.');
    StringBuffer amtBuf = new StringBuffer();
    // The whole-number part
    amtBuf.append(amount.substring(0, decimalPoint));
    // The fractional part
    int placesOnRight = length - (decimalPoint + 1);
    if (placesOnRight >= 2) {
      amtBuf.append(amount.substring(decimalPoint + 1, decimalPoint + 3));
    } else if (1 == placesOnRight) {
      amtBuf.append(amount.substring(decimalPoint + 1, length));
      amtBuf.append('0');
    } else {
      amtBuf.append("00");
    }
    return getRightJustifiedNumber(amtBuf.toString(), MAX_AMOUNT_LENGTH);
  }

  // Function:  createAccountID
  /**
   * This method creates the Account Identification 1 field,
   * it contains a two character state code,
   * prefacing a 27 character drivers license number.
   * @param stateCode             String representing state
   * @param driversLicenseNumber  String containing drivers license number.
   * @return                      String containing the result
   */
  private String createAccountID(String stateCode, String driversLicenseNumber) {
    StringBuffer result = new StringBuffer();
    result.append(getLeftJustifiedString(stateCode, 2));
    result.append(getLeftJustifiedString(driversLicenseNumber, 27));
    return result.toString();
  }

  // Function: createBlankString
  /**
   * This method returns a String initialized to blanks
   * @result String containing the result
   */
  private String createBlankString(int size) {
    char array[] = new char[size];
    for (int i = 0; i < size; i++) {
      array[i] = ' ';
    }
    String str = new String(array);
    array = null;
    return str;
  }

  // Function: createZeroString
  /**
   * This method creates a string of "size" containing zeroes
   * @result String containing the result
   */
  private String createZeroString(int size) {
    char array[] = new char[size];
    for (int i = 0; i < size; i++) {
      array[i] = '0';
    }
    String str = new String(array);
    array = null;
    return str;
  }

  //Function: createMICRData
  /**
   * Creates a left justified string for MICR data:
   * the MICR data followed by "M" then followed by
   * the check number
   */
  private String createMICRData(BusinessCheck check) {
    String micr = check.getCheckMICRdata();
    String checkNumber = check.getCheckNumber();
    StringBuffer result = new StringBuffer(micr);
    result.append("M");
    result.append(checkNumber);
    return getLeftJustifiedString(result.toString(), 76);
  }

  /**
   * Creates a left justified Track Data from a credit card scan.
   * Track One must be terminated by a ? character (and since I'm not sure, I
   * will pad any unused bytes to spaces for Track One also   alt).
   * Track Two must pad any unused bytes to spaces.
   * the check number
   * @param trackNum A String representing the track number that was read.
   * @param data A String representing the track data that was read.
   * @return A String representing the formatted track data string.
   */
  private String createTrackData(String trackNum, String data) {
    StringBuffer result = null;
    if (trackNum.equals("0")) {
      result = new StringBuffer(79);
    } else {
      result = new StringBuffer(data);
    }
    if (trackNum.equals("1")) {
      result.append("?");
    }
    String returnResult = getLeftJustifiedString(result.toString(), 79);
    return returnResult;
  }

  /**
   * format the Calender data into a string format of YYMM
   * @result String containing the result
   */
  private String createYYMMDate(Date calenderDate) {
    SimpleDateFormat fmt = new SimpleDateFormat(res.getString("yyMM"));
    String strDate = fmt.format(calenderDate);
    return strDate;
  }

  /**
   * format the Calender data into a string format of YYYYMMDD
   * @result String containing the result
   */
  private String createYYYYMMDDDate(Date calenderDate) {
    SimpleDateFormat fmt = new SimpleDateFormat(res.getString("yyyyMMdd"));
    String newDate = fmt.format(calenderDate);
    return newDate;
  }

  /**
   * This method returns a string containing a credit card
   * validation request header.
   * @parm accountNum       Account number to obtain credit for
   * @parm amount           Amount to authorize
   * @parm messageSequence  Contains sequence of message, store
   *                        and terminal information
   * @result                String containing the result
   */
  private String getHeader(String messageID, String messageType, String tenderType
      , String accountNum, double rawAmount, String messageSequence, String sNumber, String tNumber) {
    String journalKey = createBlankString(16);
    String accountNumber = getRightJustifiedAccountNumber(accountNum);
    String tenderAmount = getRightJustifiedAmount(rawAmount);
    String storeNumber = getRightJustifiedNumber(sNumber, 4);
    String terminalNumber = getRightJustifiedNumber(tNumber, 4);
    // TBD use transaction ID here??
    // This is used to identify credit requests
    Integer userId = new Integer(++id);
    int diff = 20 - userId.toString().length();
    String temp = createZeroString(diff);
    String userData = new String(temp.concat(userId.toString()));
    String filler = createZeroString(23);
    StringBuffer result = new StringBuffer(2000);
    result.append(messageID);
    result.append(journalKey);
    result.append(messageType);
    result.append(tenderType);
    result.append(accountNumber);
    result.append(tenderAmount);
    result.append(storeNumber);
    result.append(terminalNumber);
    result.append(messageSequence);
    result.append(userData);
    result.append(filler);
    return result.toString();
  }

  // Function: getISDHeaderForRequest
  /**
   * This method returns the standard ISD Header for Request.
   * This Header is used to create ISD Header for CC and Check Data
   * @param cc CreditCard
   * @param storeNumber String
   * @param terminalNumber String
   * @return Object
   */
  public void getISDHeaderForRequest(Payment p, String storeNumber, String terminalNumber) {
    return;
  }

  /**
   * This method returns a string containing a credit card
   * validation request. This method should be used for:
   * all credit card validation requests,
   * except for AMEX and TMW Credit cards.
   * Amex cards should use: getAmexCreditCardValidationRequest().
   * @param creditCard - credit card payment
   */
  public Object getCreditCardValidationRequest(CreditCard cc, String storeNumber
      , String terminalNumber) {
    StringBuffer result = new StringBuffer();
    PaymentTransaction txn = (PaymentTransaction)AppManager.getCurrent().getStateObject("TXN_POS");
    // Dummy TransactionID [051010*107*1008]
    // Message Length
    // Create the message length last and then insert into stringbuffer before
    // returning string results
    //String messageLength = createZeroString(4);
    //result.append(messageLength);
    // Message Identifier
    result.append(cc.getMessageIdentifier()); // Message Identifier  CC=Credit Card, CA=Credit Application, CK=Check, GR=Gift Cert Redeem
    // Journal Key
    String journalKey;
    String strTxnID = txn.getId();
    // Calculation for Payment Sequence.
    Payment p[] = txn.getPaymentsArray();
    int value1 = 0;
    for (int i = 0; i < p.length; i++) {
      if ((p[i] instanceof CreditCard) && cc.equals(p[i])) {
        value1 = i;
      }
    }
    Integer valInt = new Integer(value1);
    Date currentDate = new Date();
    //    journalKey = String.valueOf(currentDate.getTime()) + valInt.toString();
    //    journalKey = this.getFormattedTransactionID(strTxnID) + valInt.toString();
    //    journalKey = getLeftJustifiedString(journalKey,16);
    journalKey = getLeftJustifiedString(cc.getJournalKey(), 16);
    result.append(journalKey); // Journal Key
    // Message Type
    result.append(cc.getMessageType()); // Message Type  0=sale auth, 4=credit reversal, p=payment
    // Tender Type
    result.append(cc.getTenderType()); // Tender Type   02=check, 03=credit card, 04=GECC payment, 05=GECC Application
    // Get Account Number
    String accountNum = cc.getAccountNumber();
    String fmtAccountNum = getRightJustifiedNumber(accountNum, 19);
    result.append(fmtAccountNum); // Account Number
    // Tender ( Get Total Amount)
    String payAmount = cc.getAmount().stringValue();
    String lessPayAmount = StringFormat.removeUnwantedCharacters(payAmount, '.');
    String lessPayAmount2 = StringFormat.removeUnwantedCharacters(lessPayAmount, ',');
    String lessPayAmount3 = StringFormat.removeUnwantedCharacters(lessPayAmount2, '$');
    String lessPayAmount4 = StringFormat.removeUnwantedCharacters(lessPayAmount3, '-');
    String fmtPayAmount = getRightJustifiedNumber(lessPayAmount4, 11);
    result.append(fmtPayAmount); // Tender Amount
    // Store Number
    String fmtStore = getRightJustifiedNumber(storeNumber, 4);
    result.append(fmtStore); // Store Number
    //String partialAuthCapableIndicator = "P";
    //result.append(partialAuthCapableIndicator);
    System.out.println("result string----->"+result);
    //result.append(partialAuthCapableIndicator);
    // Terminal Number
    String fmtTerminal = getRightJustifiedNumber(terminalNumber, 4);
    result.append(fmtTerminal); // Terminal Number
    // Message Sequence
    // valInt has the "Message Sequence Value" as calculated for Journal Key
    String msgSeq = getRightJustifiedNumber(valInt.toString(), 4);
    result.append(msgSeq); // Message Sequence
    // User Data
    // Timestamp as a unique ID put in User Data
    Date newDate = new Date();
    timeStamp = newDate.getTime();
    String strTimeStamp = new Long(timeStamp).toString();
    String strKey = fmtStore + fmtTerminal + strTimeStamp;
    result.append(getLeftJustifiedString(strKey, 20)); // User Data (contains unique Id of store, reg Nunumber, and timeStamp)
    // These fields aren't required and hence n/a
    // [FIELDS] Outstanding Count & Retry Indicator
    String notapplicable = createZeroString(4);
    result.append(notapplicable);
    //  These fields aren't required and space filled
    // [FIELDS] Stand-in Indicator & Transport Indicator
    notapplicable = createBlankString(2);
    result.append(notapplicable);
    // Host Capture
    result.append("N");
    // Filler
    String filler = createZeroString(16);
    result.append(filler); // Filler
    if (rawDataRequest) {
      System.out.println("\n HEADER \n");
      System.out.println("************** Journal Key = " + journalKey);
      System.out.println("************** Message Type = " + cc.getMessageType());
      System.out.println("************** Tender Type = " + cc.getTenderType());
      // Mask the Account No
      System.out.println("************** AccountNumber = "
          + CreditAuthUtil.maskCreditCardNo(fmtAccountNum));
      System.out.println("************** Tender = " + fmtPayAmount);
      System.out.println("************** Store Number = " + fmtStore);
      System.out.println("************** Terminal Number = " + fmtTerminal);
      System.out.println("************** Message Sequence = " + msgSeq);
      System.out.println("************** User Data = " + strKey);
      System.out.println("************** outstanding & Retry Indicator = " + notapplicable);
      System.out.println("************** Host Capture= N");
      System.out.println("************** Filler is a 16 char long String");
    }
    // ********End of Header*******************
    // ***************Begin CC Request Data***********************
    // Reversal Replacement Account
    String sReversalReplacementAmount = createZeroString(12);
    result.append(sReversalReplacementAmount); // Reversal Replacement Amount
    sReversalReplacementAmount = null;
    if (verboseMode) {
      System.out.println("*************** Reversal Replacement Account : N/A");
    }
    // System Trace Audit Number
    String sSystemTrace = createZeroString(6);
    result.append(sSystemTrace); // System Trace Audit Number
    sSystemTrace = null;
    if (verboseMode) {
      System.out.println("**************** SystemTrace Audit Number: N/A");
    }
    // Local Time in (HHMMSS) format.
    // For credit cards requests, use zero for time, date
    java.text.SimpleDateFormat sd = new SimpleDateFormat("HHmmss");
    String timeString = sd.format(new Date());
    result.append(timeString);
    if (verboseMode) {
      System.out.println("**************** Local Time  " + timeString);
    }
    // Local Transaction Date(YYMMDD)
    sd = new SimpleDateFormat("yyMMdd");
    String dateString = sd.format(new Date());
    result.append(dateString);
    if (verboseMode) {
      System.out.println("*************** Local Date  " + dateString);
    }
    // Expiration Date
    Date expiration = cc.getExpirationDate() == null ? new Date() : cc.getExpirationDate();
    String expirationDate = createYYMMDate(expiration);
    result.append(expirationDate); // Expiration Date
    if (verboseMode) {
      System.out.println("*************** Expiration Date : " + expirationDate);
    }
    expirationDate = null;
    // Track Number
    String trackNumber = (cc.getTrackNumber() == null || cc.getTrackNumber().equals(""))
        ? KEYED_TRACK_NO : cc.getTrackNumber();
    result.append(trackNumber); // Track Number
    if (verboseMode) {
      System.out.println("************** Track Number : " + trackNumber);
    }
    // POS Condition Code
    String conditionCode = new String("00");
    result.append(conditionCode); // POS Condition Code
    if (verboseMode) {
      System.out.println("************** POS Condition : " + conditionCode);
    }
    //String trackData
    String trackData = (cc.getTrackData() != null && cc.getTrackData().length() > 0) ?
                       createTrackData(trackNumber, cc.getTrackData() ) : createBlankString(79) ;
    result.append(trackData); // Track Data
    if (verboseMode) {
      System.out.println("***************** Track Data " + CreditAuthUtil.maskTrackData(trackData));
    }
    // Original Retrieval Reference
    String orgRetreivalReference = createBlankString(12);
    result.append(orgRetreivalReference); // Original Retrieval Reference
    if (verboseMode) {
      System.out.println("***************** Original retrieval Ref " + orgRetreivalReference);
    }
    // Original Authorization Code
    String orgAuthorizationCode = createBlankString(6);
    result.append(orgAuthorizationCode); // Original Authorization Code
    if (verboseMode) {
      System.out.println("***************** Original Auth Code" + orgAuthorizationCode);
    }
    // Account Data Source
    String accountDataSource = createBlankString(2);
    result.append(accountDataSource); // Account Data Source
    if (verboseMode) {
      System.out.println("***************** Account Data Source is N/A");
    }
    // ArmCurrency Code Default to : 840
    // TBD - obtain currency type for locale
    String currencyCode = "840";
    result.append(currencyCode); // ArmCurrency Code
    if (verboseMode) {
      System.out.println("***************** CurrencyCode" + currencyCode);
    }
    // Pin Data
    String pinData = createBlankString(16);
    result.append(pinData); // Pin
    if (verboseMode) {
      System.out.println("***************** Pin Data" + pinData);
    }
    // Security Related Control Info : N/A
    /**
     if ( cc instanceof Amex ) {
     String cid = ((Amex)cc).getCidNumber();
     String securityRelatedControlInformation = getLeftJustifiedString( cid, 16);
     result.append(securityRelatedControlInformation); // Security Related Control Information
     } else {
     String securityRelatedControlInformation = createBlankString(16);
     result.append(securityRelatedControlInformation); // Security Related Control Information
     }
     }*/
    String security_str = createBlankString(16);
    result.append(security_str);
    if (verboseMode) {
      System.out.println("***************** Security Related Control is N/A");
    }
    // Cash Back
    String additionalAmounts = createZeroString(12);
    result.append(additionalAmounts); // Additional Amounts (Cash Back)
    if (rawDataRequest) {
      System.out.println("\n Cash Back N/A");
    }
    // POS Terminal Type
    //String terminalType = "0";
    String terminalType = "4";
    result.append(terminalType); // POS Terminal Type
    if (verboseMode) {
      System.out.println("****************** POS Terminal Type " + terminalType);
    }
    // POS Terminal Entry
    // MSR Read Capable = 2
    String posTerminalEntry = "2";
    result.append(posTerminalEntry); // POS Terminal Entry
    // Original Payment Service Indicator
    String orgPaymentServiceIndicator = createBlankString(1);
    result.append(orgPaymentServiceIndicator); // Original Payment Service Indicator
    // Original Transaction ID
    String orgTransactionIdentifier = createBlankString(15);
    result.append(orgTransactionIdentifier); // Original Transaction Identifier
    // Customer Zip Code
    // We need to set the Zip Code if the values are Keyed in && cc is an instance of visa|| CMSVisa
    String customerZipcode = null;
    if (cc.getTrackNumber() == null || cc.getTrackNumber().equals("")
        || cc.getTrackNumber().equals("0")) {
      customerZipcode = getLeftJustifiedString(cc.getZipCode(), 9);
    } else {
      customerZipcode = createBlankString(9);
    }
    result.append(customerZipcode); // Customer Zip Code
    // Customer Address
    String customerAddress = createBlankString(20);
    result.append(customerAddress); // Customer Address
    // POS Pin Entry
    //String posPinEntry = "0";
    String posPinEntry = "2";
    result.append(posPinEntry); // POS Pin Entry
    // Credit Plan
    String creditPlan = createZeroString(5);
    result.append(creditPlan); // Credit Plan
    // Ship To Name
    String shipToName = createBlankString(30);
    result.append(shipToName); // Ship To Name
    // Primary Ship To Address
    String primaryShipToAddress = createBlankString(24);
    result.append(primaryShipToAddress); // Primary Ship To Address
    // Secondary Ship To Address
    String secondaryShipToAddress = createBlankString(24);
    result.append(secondaryShipToAddress); // Secondary Ship To Address
    // Ship To City
    String shipToCity = createBlankString(20);
    result.append(shipToCity); // Ship To City
    // Ship To State
    String shipToState = createBlankString(2);
    result.append(shipToState); // Ship To State
    // Ship To Zip
    String shipToZip = createBlankString(9);
    result.append(shipToZip); // Ship To Zip
    // EBT Voucher
    String ebtVoucher = createBlankString(15);
    result.append(ebtVoucher); // EBT Voucher
    // KSN Format
    String kSN = createBlankString(16);
    result.append(kSN);
    // CVV2 Presence Indicator
    String cVV2 = "0";
    result.append(cVV2);
    // CVV2 Value
    String CVV2Val = createBlankString(5);
    result.append(CVV2Val);
    // KSN20
    String KSN20 = createBlankString(20);
    result.append(KSN20);
    // Prepend the length to the result
    int length = result.length() + 4;
    Integer ilength = new Integer(length);
    String slength = getRightJustifiedNumber(ilength.toString(), 4);
    result.insert(0, slength);
    if (rawDataRequest) {
      System.out.println(" ********************The result **************");
      System.out.println(result);
      System.out.println(" *********************************************");
      System.out.println("The Length of the Request Transaction (Header + CC Request)"
          + result.toString().length());
    }
    if (verboseMode) {
      verifyCreditCard(result.toString());
    }
    return result.toString();
  }

  /**
   *
   * @param amex Amex
   * @param store String
   * @param terminal String
   * @return Object
   */
  public Object getAmexCreditCardValidationRequest(Amex amex, String store, String terminal) {
    Object result = getCreditCardValidationRequest(amex, store, terminal);
    /*  String org = new String(getCreditCardValidationRequest(amex, store, terminal).toString());
     String cid = getLeftJustifiedString( amex.getCidNumber(), 16);
     StringBuffer result = new StringBuffer( org.substring(0,265));
     result.append(cid).append(org.substring(281,org.length()));
     */
    return result.toString();
  }

  /**
   * This method returns a string containing a tmw credit card
   * payment request that will be applied to the tmw account thru GECC.
   * @param store A string that represents the store Id.
   * @param terminal A string that represents the cash register Id.
   */
  /*   public String getTMWCreditCardPaymentRequest( LineItemCollectionTMWCreditCard tmw,
   String store,
   String terminal,
   String emp )
   {
   // The following is the HEADER layout for GECC TMW Credit cards payment
   // on account request for authorization
   StringBuffer result = new StringBuffer();
   // Create the message length last and then insert into stringbuffer before
   // returning string results
   //String messageLength = createZeroString(4);
   //result.append(messageLength);                    // Message Length
   result.append(tmw.getMessageIdentifier());         // Message Identifier  CC=Credit Card, CA=Credit Application, CK=Check, GR=Gift Cert Redeem
   String journalKey = createBlankString(16);
   result.append(journalKey);                         // Journal Key
   result.append(tmw.getMessageType());               // Message Type  0=sale auth, 4=credit reversal, p=payment
   result.append(tmw.getTenderType());                // Tender Type   02=check, 03=credit card, 04=GECC payment, 05=GECC Application
   String accountNum = tmw.getAccountNumber();
   String fmtAccountNum = getRightJustifiedNumber(accountNum, 19);
   result.append(fmtAccountNum);                      // Account Number
   String payAmount = "0";
   try {
   payAmount = tmw.getRetailAmount().equals(new ArmCurrency(0.0)) ?
   new String("0") : tmw.getRetailAmount().stringValue();
   } catch (CurrencyException e) {
   LoggingServices.getCurrent().logMsg(getClass().getName(),
   "getTMWCreditCardPaymentRequest",
   "Could not convert application income to currency.  Setting to 0.",
   "Setting to 0.", LoggingServices.MAJOR, e);
   }
   String lessPayAmount = StringFormat.removeUnwantedCharacters(payAmount, '.' );
   String lessPayAmount2 = StringFormat.removeUnwantedCharacters(lessPayAmount, ',' );
   String lessPayAmount3 = StringFormat.removeUnwantedCharacters(lessPayAmount2, '$' );
   String lessPayAmount4 = StringFormat.removeUnwantedCharacters(lessPayAmount3, '-' );
   String fmtPayAmount = getRightJustifiedNumber(lessPayAmount4,11);
   result.append(fmtPayAmount);                       // Tender Amount
   String fmtStore        = getRightJustifiedNumber(store, 4);
   result.append(fmtStore);                           // Store Number
   String fmtTerminal     = getRightJustifiedNumber(terminal, 4);
   result.append(fmtTerminal);                        // Terminal Number
   result.append("0001");                             // Message Sequence
   //Integer userId = new Integer( ++id );
   Date newDate = new Date();
   timeStamp = newDate.getTime();
   String strTimeStamp = new Long( timeStamp ).toString();
   String strKey = fmtStore + fmtTerminal + strTimeStamp;
   result.append( getLeftJustifiedString(strKey,20)); // User Data (contains unique Id)
   String filler = createBlankString(23);
   result.append(filler);                             // Filler
   // End of Header
   // Begin CC Request
   String sReversalReplacementAmount = createZeroString(12);
   result.append(sReversalReplacementAmount);        // Reversal Replacement Amount
   sReversalReplacementAmount = null;
   String sSystemTrace = createZeroString(6);
   result.append(sSystemTrace);                      // System Trace Audit Number
   sSystemTrace  = null;
   // For credit cards requests, use zero for time, date
   String localTime = createZeroString(6);
   result.append(localTime);                         // Local Time
   localTime = null;
   String localDate = createZeroString(6);
   result.append(localDate);                         // Local Transaction Date
   localDate = null;
   Date expiration     = new Date();
   String   expirationDate = createYYMMDate(expiration);
   result.append(expirationDate);                    // Expiration Date
   expirationDate = null;
   String trackNumber = tmw.getTrackNumber().length() > 0 ?
   tmw.getTrackNumber() : KEYED_TRACK_NO ;
   result.append(trackNumber);                       // Track Number
   String conditionCode          = new String("00");
   result.append(conditionCode);                     // POS Condition Code
   String trackData = tmw.getTrackData().length() > 0 ?
   createTrackData( trackNumber, tmw.getTrackData() ) : createBlankString(79) ;
   result.append(trackData);                         // Track Data
   String orgRetreivalReference  = createBlankString(12);
   result.append(orgRetreivalReference);             // Original Retrieval Reference
   String orgAuthorizationCode   = createBlankString(6);
   result.append(orgAuthorizationCode);              // Original Authorization Code
   String accountDataSource      = createBlankString(2);
   result.append(accountDataSource);                 // Account Data Source
   // TBD - obtain currency type for locale
   String currencyCode = "840";
   result.append(currencyCode);                      // ArmCurrency Code
   String pinData  = createBlankString(16);
   result.append(pinData);                           // Pin Data
   String securityRelatedControlInformation = createBlankString(16);
   result.append(securityRelatedControlInformation); // Security Related Control Information
   String additionalAmounts = createZeroString(12);
   result.append(additionalAmounts);                 // Additional Amounts (Cash Back)
   String terminalType = "0";
   result.append(terminalType);                      // POS Terminal Type
   String posTerminalEntry = "2";
   result.append(posTerminalEntry);                  // POS Terminal Entry
   String orgPaymentServiceIndicator = " ";
   result.append(orgPaymentServiceIndicator);        // Original Payment Service Indicator
   String orgTransactionIdentifier = createBlankString(15);
   result.append(orgTransactionIdentifier);          // Original Transaction Identifier
   String customerZipcode = createBlankString(9);
   result.append(customerZipcode);                   // Customer Zip Code
   String customerAddress = createBlankString(20);
   result.append(customerAddress);                   // Customer Address
   String posPinEntry = "0";
   result.append(posPinEntry);                       // POS Pin Entry
   String creditPlan = createZeroString(5);
   result.append(creditPlan);                        // Credit Plan
   String shipToName = createBlankString(30);
   result.append(shipToName);                       // Ship To Name
   String primaryShipToAddress = createBlankString(24);
   result.append(primaryShipToAddress);             // Primary Ship To Address
   String secondaryShipToAddress = createBlankString(24);
   result.append(secondaryShipToAddress);           // Secondary Ship To Address
   String shipToCity = createBlankString(20);
   result.append(shipToCity);                       // Ship To City
   String shipToState = createBlankString(2);
   result.append(shipToState);                      // Ship To State
   String shipToZip = createBlankString(9);
   result.append(shipToZip);                        // Ship To Zip
   String ebtVoucher = createBlankString(15);
   result.append(ebtVoucher);                       // EBT Voucher
   // Now go back and fill in length in field 1 and insert into stringbuffer.
   int resultLength = result.length() + 4;
   String strResultLength = new Integer( resultLength ).toString();
   String formattedResultLength = getRightJustifiedNumber(strResultLength ,4);
   result.insert(0, formattedResultLength);
   if ( verboseMode ) { verifyCreditCard( result.toString() ); }
   return result.toString();
   }
   */
  /**
   * This method returns a string containing a TMW credit card
   * application request. This method should be used for tmw
   * all credit card application requests.
   * @param tmw  The credit card application object.
   * @param store A string that represents the store Id.
   * @param terminal A string that represents the cash register Id.
   * @param emp A string that represents the employee Id submitting request
   */
  /*   public String getTMWCreditCardApplicationRequest(  TMWCreditCardApplication tmw,
   String store,
   String terminal,
   String emp )
   {
   // The following is the HEADER layout for GECC TMW Credit cards request
   // for authorization
   StringBuffer result = new StringBuffer();
   // Create the message length last and then insert into stringbuffer before
   // returning string results
   //String messageLength = createZeroString(4);
   //result.append(messageLength);                    // Message Length
   result.append(tmw.getMessageIdentifier());         // Message Identifier  CC=Credit Card, CA=Credit Application, CK=Check, GR=Gift Cert Redeem
   String journalKey = createBlankString(16);
   result.append(journalKey);                         // Journal Key
   result.append(tmw.getMessageType());               // Message Type  0=sale auth, 4=credit reversal, p=payment
   result.append(tmw.getTenderType());                // Tender Type   02=check, 03=Amex/MC/Visa/Disc credit card, 04=GECC credit card & payment, 05=GECC Application
   String fmtAccountNum = createZeroString(19);
   result.append(fmtAccountNum);                      // Account Number
   String fmtAmount = createZeroString(11);
   result.append(fmtAmount);                          // Tender Amount
   String fmtStore        = getRightJustifiedNumber(store, 4);
   result.append(fmtStore);                           // Store Number
   String fmtTerminal     = getRightJustifiedNumber(terminal, 4);
   result.append(fmtTerminal);                        // Terminal Number
   result.append("0001");                             // Message Sequence
   // Test for previously assigned Id in case this is a re-submitted request
   // If not, create a new unique Id
   if ( tmw.getId().length() > 0 )
   {
   String existingId = tmw.getId().trim();
   result.append( getLeftJustifiedString(existingId,20)); // User Data (uses unique Id from previous request attempt)
   } else {
   Date newDate = new Date();
   timeStamp = newDate.getTime();
   String strTimeStamp = new Long( timeStamp ).toString();
   String strKey = fmtStore + fmtTerminal + strTimeStamp;
   result.append( getLeftJustifiedString(strKey,20)); // User Data (contains new unique Id)
   tmw.setId( strTimeStamp );
   }
   String filler = createBlankString(23);
   result.append(filler);                             // Filler
   // End of Header
   // Beginning of Application Data
   result.append(tmw.getMessageId() );                // Message Id   A=Credit application, P=Pre-approved application, I=Credit Application Inquiry
   String accountNum = tmw.getPreApprovedAccountNumber().length() > 0 ?
   tmw.getPreApprovedAccountNumber() : new String();
   String fmtPreApprovedAccount = getLeftJustifiedString(accountNum,11);
   result.append(fmtPreApprovedAccount);              // Pre-Approved Account Number
   String appRefNumber = tmw.getRespApplicationReferenceNumber().length() > 0 ?
   tmw.getRespApplicationReferenceNumber() : createBlankString(6);
   result.append(appRefNumber);                       // Application Reference Number
   String functionCode = createBlankString(1);
   result.append(functionCode);                       // Function Code   D=more demographic data, M=more data, E=error correction (Used to browse or modify data)
   String firstName = tmw.getFirstName().length() > 0 ?
   tmw.getFirstName() : new String() ;
   String fmtFirstName = getLeftJustifiedString(firstName,14);
   result.append(fmtFirstName);                       // First Name
   String middleInitial = tmw.getMiddleInitial().length() > 0 ?
   tmw.getMiddleInitial() : new String() ;
   String fmtMiddleInitial = getLeftJustifiedString(middleInitial,1);
   result.append(fmtMiddleInitial);                   // Middle Initial
   String lastName = tmw.getLastName().length() > 0 ?
   tmw.getLastName() : new String() ;
   String fmtLastName = getLeftJustifiedString(lastName,25);
   result.append(fmtLastName);                       // Last Name
   String address1 = tmw.getAddress().length() > 0 ?
   tmw.getAddress() : new String() ;
   String fmtAddress1 = getLeftJustifiedString(address1,35);
   result.append(fmtAddress1);                       // Address 1
   String address2 = tmw.getAdditionalAddress().length() > 0 ?
   tmw.getAdditionalAddress() : new String() ;
   String fmtAddress2 = getLeftJustifiedString(address2,35);
   result.append(fmtAddress2);                       // Address 2
   String city = tmw.getCity().length() > 0 ?
   tmw.getCity() : new String() ;
   String fmtCity = getLeftJustifiedString(city,22);
   result.append(fmtCity);                           // City
   String state = tmw.getState().length() > 0 ?
   tmw.getState() : new String() ;
   String fmtState = getLeftJustifiedString(state,5);
   result.append(fmtState);                          // State
   String zipCode = tmw.getZipCode().length() > 0 ?
   tmw.getZipCode() : new String() ;
   String fmtZipCode = getLeftJustifiedString(zipCode,9);
   result.append(fmtZipCode);                        // Zip Code
   String businessPhone = tmw.getBusinessPhone().length() > 0 ?
   tmw.getBusinessPhone() : new String() ;
   String lessBusinessPhone =  StringFormat.removeUnwantedCharacters(businessPhone, '-' );
   String fmtBusinessPhone = getLeftJustifiedNumber(lessBusinessPhone,10);
   result.append(fmtBusinessPhone);                  // Business Phone
   String homePhone = tmw.getHomePhone().length() > 0 ?
   tmw.getHomePhone() : new String() ;
   String lessHomePhone =  StringFormat.removeUnwantedCharacters(homePhone, '-' );
   String fmtHomePhone = getLeftJustifiedNumber(lessHomePhone,10);
   result.append(fmtHomePhone);                     // Home Phone
   String income = "0";
   try {
   String objIncome = tmw.getAnnualIncome().equals(new ArmCurrency(0.0) ) ?
   new String("0") : tmw.getAnnualIncome().stringValue();
   int dec = objIncome.indexOf(".");
   if ( dec > 0 ) {
   income = objIncome.substring(0,dec);
   } else {
   income = objIncome;
   }
   }
   catch (CurrencyException e) {
   LoggingServices.getCurrent().logMsg(getClass().getName(),
   "getTMWCreditCardApplicationRequest",
   "Could not convert application income to currency.  Setting to 0.",
   "Setting to 0.", LoggingServices.MAJOR, e);
   }
   String lessIncome = StringFormat.removeUnwantedCharacters(income, '.' );
   String lessIncome2 = StringFormat.removeUnwantedCharacters(lessIncome, ',' );
   String lessIncome3 = StringFormat.removeUnwantedCharacters(lessIncome2, '$' );
   String lessIncome4 = StringFormat.removeUnwantedCharacters(lessIncome3, '-' );
   String fmtIncome = getRightJustifiedNumber(lessIncome4,8);
   result.append(fmtIncome);                        // Annual Income
   String fmtAuthFirstName = tmw.getAuthorizedUserFirstName().length() > 0 ?
   getLeftJustifiedString(tmw.getAuthorizedUserFirstName(),15) : createBlankString(15) ;
   result.append(fmtAuthFirstName);                 // Authorized User First Name
   String fmtAuthMiddleInitial = tmw.getAuthorizedUserMiddleInitial().length() > 0 ?
   getLeftJustifiedString(tmw.getAuthorizedUserMiddleInitial(),1) : createBlankString(1);
   result.append(fmtAuthMiddleInitial);             // Authorized User Middle Initial
   String fmtAuthLastName = tmw.getAuthorizedUserLastName().length() > 0 ?
   getLeftJustifiedString(tmw.getAuthorizedUserLastName(),15) : createBlankString(15);
   result.append(fmtAuthLastName);                  // Authorized User Last Name
   String insuranceCode = tmw.getPaymentProtection().length() > 0 ?
   tmw.getPaymentProtection() : new String("N") ;
   String fmtInsuranceCode = getLeftJustifiedString(insuranceCode,2);
   result.append(fmtInsuranceCode);                 // Insurance Code  Y=yes, N=no, 99=numeric insurance code
   Date birthDate = tmw.getBirthDate() == null ?
   new Date() : tmw.getBirthDate();
   String fmtBirthDate = createYYYYMMDDDate(birthDate);
   result.append(fmtBirthDate);                    // Birth Date
   String ssn = tmw.getSSN().length() > 0 ?
   tmw.getSSN() : new String() ;
   String fmtSSN = getLeftJustifiedString(ssn,9);
   result.append(fmtSSN);                          // Social Security Number
   String anotherUser = tmw.getSpouseLastName().length() > 0 ?
   tmw.getSpouseLastName() : new String() ;
   String fmtJointAuth = ( anotherUser.trim().length() > 0 ) ? "J" : " " ;
   result.append(fmtJointAuth);                   // Joint/Auth   J=Joint Account, A=Authorized User
   String employeeId = emp.length() > 0 ?
   emp : new String() ;
   String fmtEmployeeId = getLeftJustifiedString(employeeId,10);
   result.append(fmtEmployeeId);                  // Sales Person Code
   String spouseFirstName = tmw.getSpouseFirstName().length() > 0 ?
   tmw.getSpouseFirstName() : new String() ;
   String fmtSpouseFirstName = getLeftJustifiedString(spouseFirstName,14);
   result.append(fmtSpouseFirstName);             // Spouse First Name
   String spouseMiddleInitial = tmw.getSpouseMiddleInitial().length() > 0 ?
   tmw.getSpouseMiddleInitial() : new String() ;
   String fmtSpouseMiddleInitial = getLeftJustifiedString(spouseMiddleInitial,1);
   result.append(fmtSpouseMiddleInitial);         // Spouse Middle Initial
   String spouseLastName = tmw.getSpouseLastName().length() > 0 ?
   tmw.getSpouseLastName() : new String() ;
   String fmtSpouseLastName = getLeftJustifiedString(spouseLastName,25);
   result.append(fmtSpouseLastName);             // Spouse Last Name
   String spouseAddress1 = tmw.getSpouseAddress().length() > 0 ?
   tmw.getSpouseAddress() : new String() ;
   String fmtSpouseAddress1 = getLeftJustifiedString(spouseAddress1,35);
   //String fmtSpouseAddress1 = createBlankString(35);
   result.append(fmtSpouseAddress1);             // Spouse Address 1
   String spouseAddress2 = tmw.getSpouseAdditionalAddress().length() > 0 ?
   tmw.getSpouseAdditionalAddress() : new String() ;
   String fmtSpouseAddress2 = getLeftJustifiedString(spouseAddress2,35);
   //String fmtSpouseAddress2 = createZeroString(35);
   result.append(fmtSpouseAddress2);             // Spouse Address 2
   String spouseCity = tmw.getSpouseCity().length() > 0 ?
   tmw.getSpouseCity() : new String() ;
   String fmtSpouseCity = getLeftJustifiedString(spouseCity,22);
   //String fmtSpouseCity = createBlankString(22);
   result.append(fmtSpouseCity);                 // Spouse City
   String spouseState = tmw.getSpouseState().length() > 0 ?
   tmw.getSpouseState() : new String() ;
   String fmtSpouseState = getLeftJustifiedString(spouseState,5);
   //String fmtSpouseState = createBlankString(5);
   result.append(fmtSpouseState);                // Spouse State
   String spouseZipCode = tmw.getSpouseZipCode().length() > 0 ?
   tmw.getSpouseZipCode() : new String() ;
   String fmtSpouseZipCode = getLeftJustifiedString(spouseZipCode,9);
   //String fmtSpouseZipCode = createBlankString(9);
   result.append(fmtSpouseZipCode);              // Spouse Zip Code
   String spouseIncome = "0";
   try {
   String objIncome = tmw.getSpouseAnnualIncome().equals(new ArmCurrency(0.0) ) ?
   new String("0") : tmw.getSpouseAnnualIncome().stringValue();
   int dec = objIncome.indexOf(".");
   if ( dec > 0 )
   spouseIncome = objIncome.substring(0,dec);
   else
   spouseIncome = objIncome;
   }
   catch (CurrencyException e) {
   LoggingServices.getCurrent().logMsg(getClass().getName(),
   "getTMWCreditCardApplicationRequest",
   "Could not convert spouse application income to currency.  Setting to 0.",
   "Setting to 0.", LoggingServices.MAJOR, e);
   }
   String spouseLessIncome = StringFormat.removeUnwantedCharacters(spouseIncome, '.' );
   String spouseLessIncome2 = StringFormat.removeUnwantedCharacters(spouseLessIncome, ',' );
   String spouseLessIncome3 = StringFormat.removeUnwantedCharacters(spouseLessIncome2, '$' );
   String spouseLessIncome4 = StringFormat.removeUnwantedCharacters(spouseLessIncome3, '-' );
   String fmtSpouseIncome = getRightJustifiedNumber(spouseLessIncome4,8);
   //String fmtSpouseIncome = createZeroString(8);
   result.append(fmtSpouseIncome);               // Spouse Income
   String fmtSpouseSSN = createZeroString(9);
   result.append(fmtSpouseSSN);                  // Spouse SSN
   // Now go back and fill in length in field 1 and insert into stringbuffer.
   int resultLength = result.length() + 4;
   String strResultLength = new Integer( resultLength ).toString();
   String formattedResultLength = getRightJustifiedNumber(strResultLength ,4);
   result.insert(0, formattedResultLength);
   if ( verboseModetmw ) { verifyTMWApplication( result.toString() ); }
   return result.toString();
   }
   */
  /**
   * This method parses the authorization information
   * and sets the fields appropriately within the check
   * Note: BankChecks and BusinessChecks use this
   *       method to set authorization.
   * @param check      - BankCheck payment
   * @param response   - contains authorization information
   * @result String containing the result
   */
  public void setCheckAuthorization(Object oResponse, BankCheck check) {
    //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&& In setCheckAuthorization method &&&&&&&&&&&&&&&&&&&&&");
    String response = oResponse.toString();
    /**
     * For Offline Mode
     */
    if (response.toString().length() <= 2) {
      check.setRespStatusCode("04");
      return;
    }
    if (response.length() < 1) {
      return;
    }
    //***********Journal Key SetMethod Missing.
     //    String JournalKey = response.substring(6,22);
     //    check.setJournalKey(JournalKey);
     //Set: Auth Date (12)
     String authDate = response.substring(87, 95);
     String authTime = response.substring(95, 101);
     if (verboseMode) {
      System.out.println("setresponse: approval date(YYYYMMDD) = " + authDate);
      System.out.println("setresponse: approval time(HHmmss) = " + authTime);
    }
    SimpleDateFormat fmt = new SimpleDateFormat(res.getString("yyyyMMddHHmmss"));
    Date approvalDate = null;
    authDate = authDate + authTime;
    try {
      approvalDate = fmt.parse(authDate);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setCreditCardAuthorization"
          , "Could not parse Approval date from authorizor.  Setting to current date."
          , "See Exception", LoggingServices.INFO, e);
      approvalDate = new Date();
    }
    check.setApprovalDate(approvalDate);
    String messageNumber = response.substring(142, 144);
    check.setMessageNumber(messageNumber);
    String merchantID = response.substring(163, 178);
    check.setMerchantID(merchantID);
    // Approval time set above
    //Host Action Code
    check.setRespHostActionCode(response.substring(101, 102));
    //*********Message Status Code****************
     if (oResponse == null) {
       check.setRespStatusCode(Payment.FAILURE);
       return;
     }
     if (response.length() < 159) {
      check.setRespStatusCode(Payment.FAILURE);
      System.out.println("check.setRespStatusCode() set to " + Payment.FAILURE);
      return;
    }
    String StatusCode = response.substring(102, 104);
    if (verboseMode) {
      System.out.println("setresponse: authstatus set to:" + StatusCode);
    }
    check.setRespStatusCode(StatusCode);
    //Set Response Message(16)
    String respMsg = response.substring(104, 142);
    check.setRespMessage(respMsg);
    // **************If Auth Required **********************
    // If not approved, return.
    if (!(StatusCode.equals(Payment.APPROVED))) {
      check.setAuthRequired(true);
    } else {
      check.setAuthRequired(false);
    }
    //******************** User data ************************
     if (verboseMode) {
       System.out.println("setresponse: id =" + response.substring(67, 87));
     }
     String respId = response.substring(67, 87);
    check.setRespId(respId);
    //**************************Auth Code**********************************
     if (verboseMode) {
       System.out.println("setresponse: auth code =" + response.substring(149, 155));
     }
     String authorizationCode = response.substring(149, 155);
    check.setRespAuthorizationCode(authorizationCode);
    //*****************************Auth Response Code*******************************
     if (verboseMode) {
       System.out.println("setresponse: auth response code = " + response.substring(155, 159));
     }
     String authorizationResponseCode = response.substring(155, 159);
    check.setRespAuthorizationResponseCode(authorizationResponseCode); /*
                     try
                     {
        System.out.println("&&&&&&&&&&&&&&&&& Message length = " + response.substring(0, 4));
    System.out.println("&&&&&&&&&&&&&&&&& Message Identifier = " + response.substring(4, 6));
        System.out.println("&&&&&&&&&&&&&&&&& Journal Key = " + response.substring(6, 22));
        System.out.println("&&&&&&&&&&&&&&&&& Message Type = " + response.substring(22, 23));
        System.out.println("&&&&&&&&&&&&&&&&& Tender Type = " + response.substring(23, 25));
            System.out.println("&&&&&&&&&&&&&&&&& Account Number = " + response.substring(25, 44));
        System.out.println("&&&&&&&&&&&&&&&&& Tender Amount = " + response.substring(44, 55));
        System.out.println("&&&&&&&&&&&&&&&&& Store Number = " + response.substring(55, 59));
    System.out.println("&&&&&&&&&&&&&&&&& Terminal Number = " + response.substring(59, 63));
    System.out.println("&&&&&&&&&&&&&&&&& Message Sequence = " + response.substring(63, 67));
    System.out.println("&&&&&&&&&&&&&&&&& User data = " + response.substring(67, 87));
    System.out.println("&&&&&&&&&&&&&&&&& Auth date = " + response.substring(87, 95));
        System.out.println("&&&&&&&&&&&&&&&&& Auth Time = " + response.substring(95, 101));
        System.out.println("&&&&&&&&&&&&&&&&& Host Action Code = " + response.substring(101, 102));
        System.out.println("&&&&&&&&&&&&&&&&& Status Code = " + response.substring(102, 104));
        System.out.println("&&&&&&&&&&&&&&&&& Response Message = " + response.substring(104, 142));
    System.out.println("&&&&&&&&&&&&&&&&& Message Number = " + response.substring(142, 144));
    System.out.println("&&&&&&&&&&&&&&&&& Message Length = " + response.substring(144, 146));
        System.out.println("&&&&&&&&&&&&&&&&& Print Flag = " + response.substring(146, 148));
        System.out.println("&&&&&&&&&&&&&&&&& More Flag = " + response.substring(148, 149));
        System.out.println("&&&&&&&&&&&&&&&&& Auth Code = " + response.substring(149, 155));
    System.out.println("&&&&&&&&&&&&&&&&& Auth Response Code = " + response.substring(155, 159));
    System.out.println("&&&&&&&&&&&&&&&&& Response Reason = " + response.substring(159, 163));
        System.out.println("&&&&&&&&&&&&&&&&& Merchant ID = " + response.substring(163, 178));
    System.out.println("&&&&&&&&&&&&&&&&& Filler = " + response.substring(178, 189));
    System.out.println("&&&&&&&&&&&&&&&&& Reserved portal Response = " + response.substring(189, 190));
                     System.out.println("&&&&&&&&&&&&&&&&& ================================================= Response data");
                     System.out.println("&&&&&&&&&&&&&&&&& Check Truncation Indicator = " + response.substring(190, 191));
    System.out.println("&&&&&&&&&&&&&&&&& Transaction ID = " + response.substring(191, 203));
    System.out.println("&&&&&&&&&&&&&&&&& Service Charge = " + response.substring(203, 208));
    System.out.println("&&&&&&&&&&&&&&&&& Customer name = " + response.substring(208, 238));
    System.out.println("&&&&&&&&&&&&&&&&& Denial Number = " + response.substring(238, 245));
    System.out.println("&&&&&&&&&&&&&&&&& Telecheck Trace ID = " + response.substring(245, 267));
    System.out.println("&&&&&&&&&&&&&&&&& Extended Output = " + response.substring(267, 312));
    System.out.println("&&&&&&&&&&&&&&&&& Merchant Type Code = " + response.substring(312, 316));
    System.out.println("&&&&&&&&&&&&&&&&& Auth date and Time = " + response.substring(316, 328));
    System.out.println("&&&&&&&&&&&&&&&&& Sale date and Time = " + response.substring(328, 340));
        System.out.println("&&&&&&&&&&&&&&&&& Auth source Code = " + response.substring(340, 342));
                     System.out.println("&&&&&&&&&&&&&&&&& Transmission date and time = " + response.substring(342, 354));
                     System.out.println("&&&&&&&&&&&&&&&&& Original Transmission Date and Time = " + response.substring(354, 366));
    System.out.println("&&&&&&&&&&&&&&&&& <<<<367-428>>>> = " + response.substring(366, 428));
    System.out.println("&&&&&&&&&&&&&&&&& Returned Transit Number = " + response.substring(428, 437));
    System.out.println("&&&&&&&&&&&&&&&&& Returned account Number = " + response.substring(437, 487));
    System.out.println("&&&&&&&&&&&&&&&&& Returned Check Number = " + response.substring(487, 495));
    System.out.println("&&&&&&&&&&&&&&&&& <<<<496-496>>>> = " + response.substring(495, 496));
    System.out.println("&&&&&&&&&&&&&&&&& Processing Code = " + response.substring(496, 497));
    System.out.println("&&&&&&&&&&&&&&&&& System Trace Audit Number = " + response.substring(497, 503));
    System.out.println("&&&&&&&&&&&&&&&&& Settlement date = " + response.substring(503, 507));
    System.out.println("&&&&&&&&&&&&&&&&& Settlement Code = " + response.substring(507, 508));
    System.out.println("&&&&&&&&&&&&&&&&& ArmCurrency Code = " + response.substring(508, 511));
    System.out.println("&&&&&&&&&&&&&&&&& Transaction ID = " + response.substring(511, 526));
            System.out.println("&&&&&&&&&&&&&&&&& Agency name1 = " + response.substring(526, 543));
    System.out.println("&&&&&&&&&&&&&&&&& Agency Phone1 = " + response.substring(543, 559));
            System.out.println("&&&&&&&&&&&&&&&&& Agency name2 = " + response.substring(559, 575));
    System.out.println("&&&&&&&&&&&&&&&&& Agency Phone2 = " + response.substring(575, 591));
                     System.out.println("&&&&&&&&&&&&&&&&& ==================== Extra Info = " + response.substring(591, response.length()));
                     }
                     catch(Exception e)
                     {
                     e.printStackTrace();
                     }
    System.out.println("################ check.getAccountNumber() = "+check.getAccountNumber());
        System.out.println("################ check.getActionDate() = "+check.getActionDate());
                     System.out.println("################ check.getAmount() = "+check.getAmount());
    System.out.println("################ check.getApprovalDate() = "+check.getApprovalDate());
        System.out.println("################ check.getBankNumber() = "+check.getBankNumber());
    System.out.println("################ check.getCheckMICRdata() = "+check.getCheckMICRdata());
    System.out.println("################ check.getCheckNumber() = "+check.getCheckNumber());
                     System.out.println("################ check.getCurrencyTypeConvertedFrom() = "+check.getCurrencyTypeConvertedFrom());
                     System.out.println("################ check.getDriversLicenseNumber() = "+check.getDriversLicenseNumber());
    System.out.println("################ check.getGUIPaymentDetail() = "+check.getGUIPaymentDetail());
    System.out.println("################ check.getGUIPaymentName() = "+check.getGUIPaymentName());
    System.out.println("################ check.getIsCheckScannedIn() = "+check.getIsCheckScannedIn());
    System.out.println("################ check.getIsIDScannedIn() = "+check.getIsIDScannedIn());
        System.out.println("################ check.getJournalKey() = "+check.getJournalKey());
    System.out.println("################ check.getManualOverride() = "+check.getManualOverride());
                     System.out.println("################ check.getMaskedPaymentDetail() = "+check.getMaskedPaymentDetail());
    System.out.println("################ check.getMaxChangeAllowed() = "+check.getMaxChangeAllowed());
        System.out.println("################ check.getMerchantID() = "+check.getMerchantID());
    System.out.println("################ check.getMessageIdentifier() = "+check.getMessageIdentifier());
    System.out.println("################ check.getMessageNumber() = "+check.getMessageNumber());
    System.out.println("################ check.getMessageType() = "+check.getMessageType());
                     System.out.println("################ check.getRespAuthorizationCode() = "+check.getRespAuthorizationCode());
                     System.out.println("################ check.getRespAuthorizationResponseCode() = "+check.getRespAuthorizationResponseCode());
                     System.out.println("################ check.getRespHostActionCode() = "+check.getRespHostActionCode());
                     System.out.println("################ check.getRespId() = "+check.getRespId());
    System.out.println("################ check.getRespMessage() = "+check.getRespMessage());
    System.out.println("################ check.getRespStatusCode() = "+check.getRespStatusCode());
                     System.out.println("################ check.getRespStatusCodeDesc() = "+check.getRespStatusCodeDesc());
    System.out.println("################ check.getStateIdCode() = "+check.getStateIdCode());
        System.out.println("################ check.getTenderType() = "+check.getTenderType());
                     System.out.println("################ check.getTransactionPaymentName() = "+check.getTransactionPaymentName());
    System.out.println("################ check.getTransitNumber() = "+check.getTransitNumber());
    System.out.println("################ check.isAuthRequired() = "+check.isAuthRequired());
                     System.out.println("################ check.isForeign() = "+check.isForeign());
    System.out.println("################ check.isFrankingRequired() = "+check.isFrankingRequired());
    System.out.println("################ check.isOverPaymentAllowed() = "+check.isOverPaymentAllowed());
    System.out.println("################ check.isSignatureRequired() = "+check.isSignatureRequired());
                     System.out.println("################ ==================================");
    */

  }

  /**
   * This method parses the authorization information
   * and sets the fields appropriately within the CreditCard
   * @param creditCard - credit card payment
   * @param response   - contains authorization information
   * @result String containing the result
   **/
  public void setCreditCardAuthorization(Object oResponse, CreditCard cc) {
	 String response = oResponse.toString();
    // To make it come out of the verbose mode(M).
    // verboseMode = false;
    /**
     * For Offline Mode
     */
    if (response.toString().length() <= 2) {
      cc.setRespStatusCode("04");
      return;
    }
    //Set: Message Length (1)
    String msgLength = response.substring(0, 4);
    //Set: Journal Key (3)
    //    String  journalStr = response.substring(6,22);
    //    cc.setJournalKey(journalStr);
    //Set: Auth Date (12)
    String authDate = response.substring(87, 95);
    String authTime = response.substring(95, 101);

    
    if (verboseMode) {
      System.out.println("setresponse: approval date(YYYYMMDD) = " + authDate);
      System.out.println("setresponse: approval Time(HHmmss)= " + authTime);
    }
    SimpleDateFormat fmt = new SimpleDateFormat(res.getString("yyyyMMddHHmmss"));
    Date approvalDate = null;
    authDate = authDate + authTime;
    try {
      approvalDate = fmt.parse(authDate);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setCreditCardAuthorization"
          , "Could not parse Approval date from authorizor.  Setting to current date."
          , "See Exception", LoggingServices.INFO, e);
      approvalDate = new Date();
    }
    cc.setApprovalDate(approvalDate);
    // Approval time taken care above
    //Set: setRespStatusCode(15)
    if (oResponse == null) {
      cc.setRespStatusCode(Payment.FAILURE);
      return;
    }
    if (response.length() < 234) {
      cc.setRespStatusCode(Payment.FAILURE);
      System.out.println("cc.setRespStatusCode() set to " + Payment.FAILURE);
      return;
    }
    if (verboseMode) {
      System.out.println("setresponse: authstatus code = " + response.substring(102, 104)
          + " message = " + response.substring(104, 142));
    }
    String authorizationStatusCode = new String(response.substring(102, 104));
    System.out.println("authorizationStatusCode at the time of  auth------>"+authorizationStatusCode);
    if (verboseMode) {
      System.out.println("setresponse: authstatus set to:" + authorizationStatusCode);
    }
    // Field being set here.
    cc.setRespStatusCode(authorizationStatusCode);
    //  1940 Authorized Amount
    String authorizedAmountFromBank = response.substring(44, 55);
    ArmCurrency authAmount = new ArmCurrency(authorizedAmountFromBank);
    
    if (verboseMode) {
      System.out.println("authSTATUScode>" + authorizationStatusCode + "<");
      System.out.println("PAYMENT.APPROVAL>" + Payment.APPROVED + "<");
    }
  //1940
    if(authAmount!=null && authorizationStatusCode.equals(Payment.PARTIAL_AUTH)){
    	  {
    		  if(verboseMode){
    			  System.out.println("Amount before setting into the credit card object ----->"+authAmount.divide(100));
    		  }
    		cc.setAmount(authAmount.divide(100));
    		   		
    	}
    }
    //1940 done
    //Set Response Message(16)
    String respMsg = response.substring(104, 142);
    Payment p = (Payment)cc;
    p.setRespMessage(respMsg);
    //Set Msg Number(17)
    String msgNumber = response.substring(142, 144);
    cc.setMessageNumber(msgNumber);
    String merchantID = response.substring(163, 178);
    cc.setMerchantID(merchantID);
    //Set AuthCode(21)
    if (verboseMode) {
      System.out.println("setresponse: auth code =" + response.substring(149, 155));
    }
    String authorizationCode = response.substring(149, 155);
    cc.setRespAuthorizationCode(authorizationCode);
    //Set  Authorization Response Code(22)
    if (verboseMode) {
      System.out.println("setresponse: auth response code = " + response.substring(155, 159));
    }
    String authorizationResponseCode = response.substring(155, 159);
    cc.setRespAuthorizationResponseCode(authorizationResponseCode);
    //*************** setAuthRequired ? ********************
    //1940
     if (authorizationStatusCode.equals(Payment.APPROVED)||(authorizationStatusCode.equals(Payment.PARTIAL_AUTH))){
    	 cc.setAuthRequired(false);
      	if(verboseMode){ 
    		System.out.println("PAYMENT NOT APPROVED. ");
    	}
     } else {
       cc.setAuthRequired(true);
     }
    //****************** setRespId *****************************
     if (verboseMode) {
       System.out.println("setresponse: id =" + response.substring(67, 87));
     }
     String respId = response.substring(67, 87);
    cc.setRespId(respId);
    //****************** POS Entry***************************
     if (verboseMode) {
       System.out.println("setresponse: pos entry mode = " + response.substring(194, 196));
     }
     String posEntryMode = response.substring(194, 196);
    cc.setRespPOSEntryMode(posEntryMode);
    //****************** Authorization Source******************
     if (verboseMode) {
       System.out.println("setresponse: auth source = " + response.substring(208, 209));
     }
     String authorizationSource = response.substring(208, 209);
    cc.setRespAuthorizationSource(authorizationSource);
    //******************* Address Verification*******************
     if (verboseMode) {
       System.out.println("setresponse: address verif code = " + response.substring(209, 210));
     }
     String addressVerificationCode = response.substring(209, 210);
    cc.setRespAddressVerification(addressVerificationCode);
    //****************** Payment Service Indicator*****************
     if (verboseMode) {
       System.out.println("setresponse: service indicator = " + response.substring(214, 215));
     }
     String ccServiceIndicator = response.substring(214, 215);
    cc.setRespPaymentServiceIndicator(ccServiceIndicator);
    //******************* Transaction Id***************************
     if (verboseMode) {
       System.out.println("setresponse: transaction id = " + response.substring(215, 230));
     }
     String transactionID = response.substring(215, 230);
    cc.setRespTransactionIdentifier(transactionID);
    //******************* Validation Code****************************
     if (verboseMode) {
       System.out.println("setresponse: validationCode = " + response.substring(230, 234));
     }
     String validationCode = response.substring(230, 234);
    cc.setRespValidationCode(validationCode);
    // **************(37)Local Trans Date Missing*********************
    // *****************(38)Local Trans Time Missing******************
    // *****************(58)Card Identifier Missing*******************
    //******************* Card identifier CHANGED*****************************
     // Need to be commented for the test string
     String cardIdStr = response.substring(412, 414);
     cc.setCardIdentifier(cardIdStr);
    String addressVerifier = response.substring(209, 210);
    cc.setRespAddressVerification(addressVerifier);
    return;
  }

  /**
   * This method parses the authorization information
   * and sets the fields appropriately within the CreditCard
   * @param creditCard - tmw `credit card payment
   * @param response   - contains authorization information
   * @result String containing the result
   */
  /*   public void setTMWCreditCardPaymentAuthorization( String response,
   LineItemCollectionTMWCreditCard coll )
   {
   if ( response == null )
   {
   coll.setRespStatusCode( coll.FAILURE );
   return;
   }
   if ( response.length() < 234)
   {
   coll.setRespStatusCode( coll.FAILURE );
   return;
   }
   if ( response.length() > 234 )
   {
   // Remember, java string starts at 0 and the credit authorizor starts at 1.
   // Substring starting position will always be one less than documentation.
   coll.setRespId( response.substring(67,87) );
   coll.setRespHostActionCode(response.substring(101,102));
   coll.setRespStatusCode(response.substring(102,104));
   coll.setRespAuthorizationCode(response.substring(149,155) );
   coll.setRespAuthorizationResponseCode(response.substring(155,159) );
   coll.setRespPOSEntryMode( response.substring(194,196) );
   coll.setRespAuthorizationSource( response.substring(208,209) );
   coll.setRespAddressVerification(response.substring(209,210));
   coll.setRespPaymentServiceIndicator( response.substring(214,215) );
   coll.setRespTransactionIdentifier( response.substring(215,230) );
   coll.setRespValidationCode( response.substring(230,234) );
   if ( verboseModetmw ) {
   System.out.println("setRespId= " + coll.getRespId()  );
   System.out.println("setRespHostActionCode= " + coll.getRespHostActionCode()  );
   System.out.println("setRespStatusCode= " + coll.getRespStatusCode()  );
   System.out.println("setRespAuthorizationCode= " + coll.getRespAuthorizationCode()  );
   System.out.println("setRespAuthorizationResponseCode= " + coll.getRespAuthorizationResponseCode() + "<" );
   System.out.println("setRespPOSEntryMode= " + coll.getRespPOSEntryMode()  );
   System.out.println("setRespAuthorizationSource= " + coll.getRespAuthorizationSource()  );
   System.out.println("setRespAddressVerification= " + coll.getRespAddressVerification()  );
   System.out.println("setRespPaymentServiceIndicator= " + coll.getRespPaymentServiceIndicator()  );
   System.out.println("setRespTransactionIdentifier= " + coll.getRespTransactionIdentifier()  );
   System.out.println("setRespValidationCode= " + coll.getRespValidationCode()  );
   }
   }
   }
   */
  /**
   * This method parses the authorization information
   * and sets the fields appropriately within the CreditCard
   * @param creditCard - credit card payment
   * @param response   - contains authorization information
   * @return String containing the result
   */
  /*   public void setTMWCreditCardApplicationAuthorization( String response,
   TMWCreditCardApplication app )
   {
   if ( response == null )
   {
   app.setRespStatusCode( app.FAILURE );
   return;
   }
   if ( response.length() < 654)
   {
   app.setRespStatusCode( app.FAILURE );
   return;
   }
   System.out.println("This is right before the CALL.");
   if ( response.length() > 654 )
   {
   // these fields are needed for determining if second & third loop
   app.setRespStatusCode(response.substring(102,104));
   app.setRespReceiptPromo(response.substring(104,142));
   app.setRespResponseMessage(response.substring(104,142));
   app.setRespAuthorizationResponseCode(response.substring(155,159) );
   app.setRespApplicationReferenceNumber(response.substring(219,225) );
   app.setRespAccountNumber(response.substring(190,209) );
   app.setId(response.substring(67,87).trim());
   // Only set these fields if an approval, otherwise not guaranteed to be there.
   // Remember, java string starts at 0 and the credit authorizor starts at 1.
   // Substring starting position will always be one less than documentation.
   if ( app.getRespStatusCode().equals("01") ) {
   // I took setting these out because the response string was not echoing
   // these fields back as the reply string documentation said they would.
   // These fields remain populated from the request string.    alt
   if ( app.getMessageId().equals("L") ) {
   app.setAdditionalAddress(response.substring(308,343).trim());
   app.setAddress(response.substring(273,308).trim());
   double dAnnualIncome = 0.0;
   String strAnnualIncome = response.substring(399,407).trim();
   NumberFormat nfmt = NumberFormat.getNumberInstance();
   Number annualIncome = null;
   try {
   annualIncome = nfmt.parse( strAnnualIncome );
   } catch (Exception e) {
   LoggingServices.getCurrent().logMsg(getClass().getName(),
   "setCreditCardApplicationAuthorization",
   "Could not parse Annual Income from authorizor.  Setting to 0.0.",
   "See Exception", LoggingServices.MAJOR, e);
   }
   if ( annualIncome.doubleValue() > 0 ) { dAnnualIncome = annualIncome.doubleValue() / 100; }
   app.setAnnualIncome(new ArmCurrency(dAnnualIncome));
   app.setAuthorizedUserFirstName(response.substring(407,422).trim());
   app.setAuthorizedUserLastName(response.substring(423,438).trim());
   app.setAuthorizedUserMiddleInitial(response.substring(422,423).trim());
   String strBirthDate = response.substring(440,448).trim();
   SimpleDateFormat fmt = new SimpleDateFormat(res.getString("yyyyMMdd"));
   Date birthDate = null;
   try {
   birthDate = fmt.parse( strBirthDate );
   } catch (Exception e) {
   LoggingServices.getCurrent().logMsg(getClass().getName(),
   "setCreditCardApplicationAuthorization",
   "Could not parse Birth date from authorizor.  Setting to current date.",
   "See Exception", LoggingServices.MAJOR, e);
   birthDate = new Date();
   }
   app.setBirthDate(birthDate);
   app.setBusinessPhone(response.substring(379,389).trim());
   app.setCity(response.substring(343,365).trim());
   app.setFirstName(response.substring(233,247).trim());
   app.setHomePhone(response.substring(389,399).trim());
   app.setLastName(response.substring(248,273).trim());
   app.setMiddleInitial(response.substring(247,248).trim());
   app.setPaymentProtection(response.substring(438,440).trim());
   app.setSpouseFirstName(response.substring(468,482).trim());
   app.setSpouseLastName(response.substring(483,508).trim());
   app.setSpouseMiddleInitial(response.substring(482,483).trim());
   app.setSSN(response.substring(448,457).trim());
   app.setState(response.substring(365,370).trim());
   app.setZipCode(response.substring(370,379).trim());
   }
   app.setRespHostActionCode(response.substring(101,102));
   app.setRespStatusCode(response.substring(102,104));
   app.setRespAuthorizationCode(response.substring(149,155) );
   app.setRespAuthorizationResponseCode(response.substring(155,159) );
   app.setRespAccountNumber(response.substring(190,209) );
   app.setRespCreditLine(response.substring(209,219) );
   app.setRespApplicationReferenceNumber(response.substring(219,225) );
   app.setRespPassExpirationDate(response.substring(225,233) );
   app.setRespAnnualRate(response.substring(642,648));
   app.setRespMonthlyRate(response.substring(648,654));
   if ( verboseModetmw & app.getMessageId().equals("L") ) {
   System.out.println("setAdditionalAddress= " + app.getAdditionalAddress()  );
   System.out.println("setAddress= " + app.getAddress()  );
   System.out.println("setAnnualIncome= " + app.getAnnualIncome().stringValue()  );
   System.out.println("setBirthDate= " + app.getBirthDate()  );
   System.out.println("setBusinessPhone= " + app.getBusinessPhone()  );
   System.out.println("setCity= " + app.getCity()  );
   System.out.println("setFirstName= " + app.getFirstName()  );
   System.out.println("setHomePhone= " + app.getHomePhone()  );
   System.out.println("setLastName= " + app.getLastName()  );
   System.out.println("setMiddleInitial= " + app.getMiddleInitial()  );
   System.out.println("setPaymentProtection= " + app.getPaymentProtection()  );
   System.out.println("setSpouseFirstName= " + app.getSpouseFirstName()  );
   System.out.println("setSpouseLastName= " + app.getSpouseLastName()  );
   System.out.println("setSpouseMiddleInitial= " + app.getSpouseMiddleInitial()  );
   System.out.println("setSSN= " + app.getSSN()  );
   System.out.println("setState= " + app.getState()  );
   System.out.println("setZipCode= " + app.getZipCode()  );
   System.out.println("setRespHostActionCode= " + app.getRespHostActionCode()  );
   }
   System.out.println("ISDValidation Response setId= " + app.getId()  );
   System.out.println("setRespStatusCode= " + app.getRespStatusCode()  );
   System.out.println("setRespAuthorizationCode= " + app.getRespAuthorizationCode()  );
   System.out.println("setRespAuthorizationResponseCode= " + app.getRespAuthorizationResponseCode()  );
   System.out.println("setRespMessage= " + app.getRespReceiptPromo()  );
   System.out.println("setRespAccountNumber= " + app.getRespAccountNumber()  );
   System.out.println("setRespCreditLine= " + app.getRespCreditLine()  );
   System.out.println("setRespApplicationReferenceNumber= " + app.getRespApplicationReferenceNumber()  );
   System.out.println("setRespPassExpirationDate= " + app.getRespPassExpirationDate()  );
   System.out.println("setRespAnnualRate= " + app.getRespAnnualRate()  );
   System.out.println("setRespMonthlyRate= " + app.getRespMonthlyRate()  );
   }
   }
   }
   */
  //Function: getBankCheckValidationRequest
  /**
   * This method returns a string containing a check
   * validation request.
   * @parm Check - check type of payment
   * @result String containing the result
   */
  public Object getBankCheckValidationRequest(BankCheck check, String storeNumber
      , String terminalNumber) {
    // For checks send zeros for account number,
    // sometimes the account number contains non-digits
    // which ISD won't accept
    //String accountNum      = createZeroString(ACCOUNT_NUMBER_SIZE);
    //double rawAmount       = check.getAmount().doubleValue();
    //String messageSequence = new String("0001");
    //String header          = getHeader( MESSAGE_CHECK,
    //                                    SALE_AUTHORIZATION,
    //                                    TENDER_CHECK,
    //                                    accountNum,
    //                                    rawAmount,
    //                                    messageSequence,
    //                                    storeNumber,
    //                                    terminalNumber );
    // The following is the HEADER layout for Bank Check authorizations
    StringBuffer result = new StringBuffer();
    PaymentTransaction txn = (PaymentTransaction)AppManager.getCurrent().getStateObject("TXN_POS");
    // Message Length.
    // Create the message length last and then insert into stringbuffer before
    // returning string results
    //String messageLength = createZeroString(4);
    //result.append(messageLength);                      // Message Length  4 digits
    // Message Identifier
    result.append(check.getMessageIdentifier()); // Message Identifier  2 Char CC=Credit Card, CA=Credit Application, CK=Check, GR=Gift Cert Redeem
    // Journal Key
    String journalKey;
    String strTxnID = txn.getId();
    // Payment Sequence Calculation
    Payment p[] = txn.getPaymentsArray();
    int value1 = 0;
    for (int i = 0; i < p.length; i++) {
      if ((p[i] instanceof Check) && check.equals(p[i])) {
        value1 = i;
      }
    }
    Integer valInt = new Integer(value1);
    Date currentDate = new Date();
    //    journalKey = String.valueOf(currentDate.getTime())+ valInt.toString();
    //    journalKey = this.getFormattedTransactionID(strTxnID)+ valInt.toString();
    //    journalKey = getLeftJustifiedString(journalKey, 16);
    journalKey = check.getJournalKey();
    if (verboseMode) {
      System.out.println("************** Journal Key = " + journalKey);
    }
    result.append(journalKey); // Journal Key
    // Message Type.
    result.append(check.getMessageType()); // Message Type 1 Char  0=sale auth, 4=credit reversal, p=payment
    if (verboseMode) {
      System.out.println("************** Message Type = " + check.getMessageType());
    }
    // Tender Type.
    result.append(check.getTenderType()); // Tender Type  2 digits 02=check, 03=credit card, 04=GECC payment, 05=GECC Application
    if (verboseMode) {
      System.out.println("************** Tender Type = " + check.getTenderType());
    }
    // Account Number
    // 9-22-00 Padded with zeros until TMW starts sending MICR data.   alt
    //String accountNum = "";
    //if ( null != check.getAccountNumber() ) {accountNum = check.getAccountNumber(); }
    //String fmtAccountNum = getRightJustifiedNumber(accountNum, 19);
    String fmtAccountNum = createZeroString(19);
    result.append(fmtAccountNum); // Account Number  19 digits
    // Tender
    String payAmount = check.getAmount().stringValue();
    String lessPayAmount = StringFormat.removeUnwantedCharacters(payAmount, '.');
    String lessPayAmount2 = StringFormat.removeUnwantedCharacters(lessPayAmount, ',');
    String lessPayAmount3 = StringFormat.removeUnwantedCharacters(lessPayAmount2, '$');
    String lessPayAmount4 = StringFormat.removeUnwantedCharacters(lessPayAmount3, '-');
    String fmtPayAmount = getRightJustifiedNumber(lessPayAmount4, 11);
    result.append(fmtPayAmount); // Tender Amount  11 digits
    // Store Number
    String fmtStore = getRightJustifiedNumber(storeNumber, 4);
    result.append(fmtStore); // Store Number  4 digits
    // Terminal Number
    String fmtTerminal = getRightJustifiedNumber(terminalNumber, 4);
    result.append(fmtTerminal); // Terminal Number  4 digits
    // Message Sequence
    // Message Sequence is calculated in the Journal Key Calculation.
    String msgSeq = getRightJustifiedNumber(valInt.toString(), 4);
    result.append(msgSeq); // Message Sequence
    if (verboseMode) {
      System.out.println("************** Message Sequence = " + msgSeq);
    }
    // User Data
    // Timestamp as a unique ID put in User Data
    Date newDate = new Date();
    timeStamp = newDate.getTime();
    String strTimeStamp = new Long(timeStamp).toString();
    String strKey = fmtStore + fmtTerminal + strTimeStamp;
    result.append(getLeftJustifiedString(strKey, 20)); // User Data (contains unique Id) 20 Char
    // These fields aren't required and hence n/a
    // [FIELDS] Outstanding Count & Retry Indicator
    String notapplicable = createZeroString(4);
    result.append(notapplicable);
    if (verboseMode) {
      System.out.println("************** outstanding & Retry Indicator = N/A");
    }
    //  These fields aren't required and space filled
    // [FIELDS] Stand-in Indicator
    notapplicable = createBlankString(2);
    result.append(notapplicable);
    // Host Capture
    result.append("N");
    // Filler
    String filler = createZeroString(16);
    result.append(filler); // Filler
    // ********End of Header*******************
    //*********Begin BankCheck Data *****************
     // Begin Check specific information
     // Calculation on Check Object
     String checkNumber = "";
     if (null != check.getCheckNumber()) {
      checkNumber = check.getCheckNumber();
    }
    String transitNumber = "";
    if (null != check.getTransitNumber()) {
      transitNumber = check.getTransitNumber();
    }
    String accountNumber = "";
    if (null != check.getAccountNumber()) {
      accountNumber = check.getAccountNumber();
    }
    // Check MICR Data
    //String checkMICRData = "";
    //String manualData = accountNum.trim() + "M" + check.getCheckNumber() ;
    //if ( null != check.getCheckMICRdata() ) { checkMICRData = check.getCheckMICRdata(); }
    //String micrData = checkMICRData + "M" + check.getCheckNumber() ;
    //if (  null == check.getCheckMICRdata() ) {
    //   micrData = getLeftJustifiedString( manualData, 76);
    //} else {
    //   micrData = getLeftJustifiedString( micrData, 76);
    //}
    //    if (check instanceof BusinessCheck) {
    //      String busData = transitNumber + accountNumber + "M" + checkNumber;
    //      result.append(getLeftJustifiedString(busData, 76)); // BusinessCheck MICR data 76 Char
    //    } else {
    //      result.append(getLeftJustifiedString(checkNumber, 76)); // PersonalCheck MICR data 76 Char
    //    }
    //VM
    //    if(check.getIsCheckScannedIn())
    //        result.append(getLeftJustifiedString(transitNumber + accountNumber + "M" + checkNumber, 76)); // Check MICR data 76 Char
    //    else
    //        result.append(getLeftJustifiedString(transitNumber + "T" + accountNumber + "A" + checkNumber, 76)); // Check MICR data 76 Char
    //VM: As per Bug #411, changed MICR string is:
    result.append(getLeftJustifiedString("T" + transitNumber + "A" + accountNumber + "C"
        + checkNumber, 76)); // Check MICR data 76 Char
    // MICR keyed or Scanned
    String micrScanned = "0";
    // 09-22-2000 Per layout from Ruth Martin at TMW, this will be populate like
    // current legacy system as a "0" until MICR data is used.    alt
    // 07-01-2000 Per TeleChek, Taking this out until TMW starts sending MICR data.
    // Was told to send one space instead of a "0" or "1" until we have MICR data.   alt
    //VM: As per Bug #412
    //if (!check.getIsIDScannedIn())
    if (!check.getIsCheckScannedIn()) {
      micrScanned = KEYED_IN;
    } else {
      micrScanned = SCANNED_IN;
    }
    result.append(micrScanned); // MICR 0-Keyed or 1-Scanned 1 Char
    // Action Date
    // Always set Action Date (Numeric Date of Birth) to zeros
    String actionDate = createBlankString(6);
    result.append(actionDate); // Action Date 6 Char MMDDYY
    // Acct. Id 1
    // For Bank checks, get StateIdCode and DriversLicenseNumber.
    // For Business check, this info is blank.
    /*
     if ( check instanceof BusinessCheck ) {
     accountID = getLeftJustifiedString( "19", 29);
     } else {
     accountID = createAccountID(check.getStateIdCode(),
     check.getDriversLicenseNumber());}
     */
    String accountId = createBlankString(29);
    result.append(accountId); // Account Identification 29 Char
    // (47) Acct. Id2 (Keyed or Scanned)
    String idScanned = createBlankString(1);
    // 09-22-2000 Per layout from Ruth Martin at TMW, this will be populate like
    // current legacy system as a "0" until MICR data is used.    alt
    // 08-01-2000 Per TeleChek, Taking this out until TMW starts scanning the drivers license.
    // Was told to send one space instead of a "0" or "1" until we have data.   alt
    //if ( check.getIsIDScannedIn() )
    //{
    //  idScanned = SCANNED_IN;
    //}
    //else
    //{
    //   idScanned = KEYED_IN;
    //}
    result.append(idScanned); // Account Identification Keyed or Scanned 1 Char
    //(48) Location Type
    // 09-22-2000 These fields will not be used until TMW begins using MICR data.   alt
    //result.append("2");                                     // Location Type 1-COD, 2-Retail, 3-Other
    String LocType = createBlankString(1);
    result.append(LocType);
    //(49) MICR Line Format Code
    String LineFormat = null;
    if (micrScanned == KEYED_IN) {
      LineFormat = "01";
    } else if (micrScanned == SCANNED_IN) {
      LineFormat = "04";
    }
    result.append(LineFormat);
    // MICR Line Format Code
    //(50) Check Type
    //String checkType = "P";
    //if ( check instanceof BusinessCheck ) {
    //   checkType = "B";
    //}
    String checkType = createBlankString(1);
    result.append(checkType); // Check Type
    //(51) Name
    String name = createBlankString(30);
    result.append(name); // Name
    //(52) Phone Number
    String phone = createBlankString(10);
    result.append(phone); // phone
    //(53) Address
    String address = createBlankString(24);
    result.append(address); // address
    //(54) City
    String city = createBlankString(20);
    result.append(city); // city
    //(55) State
    String state = createBlankString(2);
    result.append(state); // state
    //(56) Zip
    String zip = createBlankString(9);
    result.append(zip); // zip
    //(57) ArmCurrency Code
    //String currencyCode = createBlankString(3);
    String currencyCode = "840";
    result.append(currencyCode); // currency code 840=US
    // (58) Keyed Check Number
    // Per TeleChek, Taking this out until TMW starts sending MICR data.
    // Was told to send eight spaces instead of check number until we have MICR data.   alt
    String keyedCheckNumber = createZeroString(8);
    result.append(keyedCheckNumber); // keyed check number
    //(59) Transaction Type
    String transactionType = "S";
    result.append(transactionType); // transaction type C-change, S-sale, V-void, A-accept, D-decline
    //(60) Product Code
    String productCode = createZeroString(3);
    result.append(productCode); // Product code
    //(61) Delivery method
    String deliveryMethod = createBlankString(1);
    result.append(deliveryMethod); // Delivery Method
    //(62) Clerk Id
    String clerkID = createBlankString(8);
    result.append(clerkID); // Clerk ID
    //(63) Billing Control
    String billingControl = createBlankString(17);
    result.append(billingControl); // Billing Control
    //(64) New Account
    String newAmount = createZeroString(11);
    result.append(newAmount); // New Amount
    //(65) TeleCheck Trace Id
    String teleCheck = createBlankString(22);
    result.append(teleCheck); // TeleCheck Trace ID
    //(65a) cash back Amt
    String cashBack = createZeroString(11);
    result.append(cashBack);
    //(65 b) Track Number
    String trackNumber = "0";
    result.append(trackNumber);
    //(65 c) Track data.
    String trackData = createBlankString(104);
    result.append(trackData);
    //(65 d) ID Expiration Date
    String IDExpdate = createZeroString(4);
    result.append(IDExpdate);
    //(65 e) Local Trans Date
    // Local Transaction Date(YYMMDD)
    SimpleDateFormat sd = new SimpleDateFormat("yyMMdd");
    String dateString = sd.format(new Date());
    result.append(dateString);
    System.out.println(" Date is " + dateString);
    //(65 f) Local Trans Time
    // Local Time in (HHMMSS) format.
    // For credit cards requests, use zero for time, date
    sd = new SimpleDateFormat("HHmmss");
    String timeString = sd.format(new Date());
    result.append(timeString);
    System.out.println(" Time is " + timeString);
    //(65 g) Type of identification
    result.append("00");
    //(65 h) Reversal Reason Code
    String revCode = createBlankString(2);
    result.append(revCode);
    //(65 i) Original Local Date
    String localDate = createZeroString(6);
    result.append(localDate);
    //(65 j) Original Local Time
    String localTime = createZeroString(6);
    result.append(localTime);
    //(65 k) Original Message
    String OrgMsg = createZeroString(4);
    result.append(OrgMsg);
    //(65 l) Original STAN
    String OrgStan = createZeroString(22);
    result.append(OrgStan);
    //(65 m) Operator Id
    Integer OperId = new Integer(txn.getTheOperator().getId());
    String OP_str = getRightJustifiedNumber(OperId.toString(), 10);
    result.append(OP_str);
    //(65 n) Original Auth
    String OrgAuth = createZeroString(6);
    result.append(OrgAuth);
    //(65 o) Processing Code
    String ProcessCode = createZeroString(2);
    result.append(ProcessCode);
    //(65 p) System Trace
    String SysTrace = createZeroString(6);
    result.append(SysTrace);
    //(65 q) Original Retrieval Ref.
    String OrgRef = createBlankString(12);
    result.append(OrgRef);
    //(65 r) Original Trans Id
    String OrgTrans = createBlankString(15);
    result.append(OrgTrans);
    // End Specific Check Information
    // Prepend the length to the result
    int length = result.length() + 4;
    Integer ilength = new Integer(length);
    String slength = getRightJustifiedNumber(ilength.toString(), 4);
    result.insert(0, slength);
    if (verboseMode) {
      try {
        verifyCheck(result.toString());
      } catch (Exception e) {
        System.out.println("File output credit.out err>>" + e);
      }
    }
    return result.toString();
  }

  /**  // Method not needed.  Use getBankCheckValidationRequest
   * This method returns a string containing a business check
   * validation request.
   * @parm Check - Business check type of payment
   * @result String containing the result
   */
  //public String getBusinessCheckValidationRequest( BusinessCheck check,
  //                                                 String    storeNumber,
  //                                                 String    terminalNumber )
  //{
  //   String str          = getBankCheckValidationRequest((BankCheck) check,
  //                                                        storeNumber,
  //                                                        terminalNumber);
  //   String micrData     = createMICRData( check );
  //   StringBuffer result = new StringBuffer(str.substring(0,111));
  //   result.append(micrData).append(str.substring(186,str.length()));
  //
  //   return result.toString();
  //}
  /**
   * This method verifies if the length and data type are correct.
   * @param    length    Length of field
   * @param    start     Starting location of field
   * @param    end       Ending location of field
   * @param    data_type Type of data: DIGITS or CHARACTERS
   * @param    str       String containing field
   * @param    name      Name of field
   * @return             A boolean indicating whether field is correct
   */
  private boolean fieldOK(int length, int start, int end, int data_type, String str, String name) {
    if ((str.length() < end) || ((end - start) != length)) {
      if (rawDataRequest) {
        System.out.println("Field " + name + " length is wrong.  Length is: " + str.length()
            + ".  Length should be:" + length + " (start=" + start + ", end=" + end + ")");
      }
      return false;
    }
    String field = str.substring(start, end);
    if (data_type == DIGITS) {
      for (int i = 0; i < length; i++) {
        char ch = field.charAt(i);
        if (!Character.isDigit(ch)) {
          if (rawDataRequest) {
            System.out.println("Field: " + name + " contains non-digits " + "at index=" + i
                + ".  Char = " + ch + ". Field value = " + field);
          }
          return false;
        }
      }
    }
    if (rawDataRequest) {
      if (name.equalsIgnoreCase("Account Number 19 DIGITS RIGHT JUST/ZERO FILLED")) {
        // Masking the Account Number
        field = CreditAuthUtil.maskCreditCardNo(field);
      }
      if (name.equalsIgnoreCase("Track Data 79 CHAR LEFT JUSTIFIED") && name != null) {
        // Masking the Track Data
        field = CreditAuthUtil.maskTrackData(field);
      }
      System.out.println("Field: " + name + " is: >" + field + "< OK ");
    }
    return true;
  }

  /**
   * Method to check that the fields within the header are correct.
   * @param   result    String containing header
   */
  private void verifyHeader(String result) {
    fieldOK(4, 0, 4, DIGITS, result, "Message Length 4 DIGITS");
    fieldOK(2, 4, 6, CHARACTERS, result, "Message Identifier 2 CHAR");
    fieldOK(16, 6, 22, CHARACTERS, result, "Journal Key 16 CHAR SPACE FILLED");
    fieldOK(1, 22, 23, CHARACTERS, result, "Message Type 1 CHAR");
    fieldOK(2, 23, 25, DIGITS, result, "Tender Type 2 DIGITS");
    fieldOK(19, 25, 44, DIGITS, result, "Account Number 19 DIGITS RIGHT JUST/ZERO FILLED");
    fieldOK(11, 44, 55, DIGITS, result, "Auth Amount 11 DIGITS RIGHT JUST/ZERO FILLED");
    fieldOK(4, 55, 59, DIGITS, result, "Store Number 4 DIGITS");
    fieldOK(4, 59, 63, DIGITS, result, "Terminal Number 4 DIGITS RIGHT JUST/ZERO FILLED");
    fieldOK(4, 63, 67, DIGITS, result, "Message Sequence 4 DIGITS");
    fieldOK(20, 67, 87, CHARACTERS, result, "User Data 20 CHAR ");
    fieldOK(23, 87, 110, CHARACTERS, result, "Filler 23 CHAR ZERO FILLED");
  }

  /**
   * Method which checks the fields within the credit card request
   * to determine if they are correct.
   * @param result    String containing credit request
   */
  private void verifyCreditCard(String result) {
    if (rawDataRequest) {
      System.out.println("\nVERIFYING CREDIT CARD REQUEST");
    }
    verifyHeader(result);
    fieldOK(12, 110, 122, DIGITS, result, "Reversal Replacement Amount 12 DIGITS ZERO FILLED");
    fieldOK(6, 122, 128, DIGITS, result, "System Trace Audit No 6 DIGITS");
    fieldOK(6, 128, 134, DIGITS, result, "Local Time 6 DIGITS hhmmss");
    fieldOK(6, 134, 140, DIGITS, result, "Local Transaction Date 6 DIGITS YYMMDD");
    fieldOK(4, 140, 144, DIGITS, result, "Expiration Date 4 DIGITS YYMM");
    fieldOK(1, 144, 145, CHARACTERS, result, "Track Number 1 CHAR");
    fieldOK(2, 145, 147, DIGITS, result, "POS condition Code 2 DIGITS");
    fieldOK(79, 147, 226, CHARACTERS, result, "Track Data 79 CHAR LEFT JUSTIFIED");
    fieldOK(12, 226, 238, CHARACTERS, result, "Org Retrieval Ref 12 CHAR");
    fieldOK(6, 238, 244, CHARACTERS, result, "Org Auth Code 6 CHAR");
    fieldOK(2, 244, 246, CHARACTERS, result, "Account Data Source 2 CHAR");
    fieldOK(3, 246, 249, DIGITS, result, "Currency Code 3 DIGITS");
    fieldOK(16, 249, 265, CHARACTERS, result, "Pin Data 16 CHAR hexadecimal digit");
    fieldOK(16, 265, 281, CHARACTERS, result
        , "Security Related Control 16 CHAR LEFT JUST/SPACED FILLED");
    fieldOK(12, 281, 293, DIGITS, result, "Additional Amounts 12 DIGITS RIGHT JUST/ZERO FILLED");
    fieldOK(1, 293, 294, DIGITS, result, "POS Terminal Type 1 DIGITS");
    fieldOK(1, 294, 295, DIGITS, result, "POS Terminal Entry 1 DIGITS");
    fieldOK(1, 295, 296, CHARACTERS, result, "Org Payment Serice Ind 1 CHAR");
    fieldOK(15, 296, 311, CHARACTERS, result, "Org transaction ID 15 CHAR");
    fieldOK(9, 311, 320, CHARACTERS, result, "Customer Zipcode 9 CHAR SPACED FILLED RIGHT");
    fieldOK(20, 320, 340, CHARACTERS, result, "Customer Address 20 CHAR");
    fieldOK(1, 340, 341, DIGITS, result, "POS Pin Entry 1 DIGITS");
    fieldOK(5, 341, 346, DIGITS, result, "Credit Plan 5 DIGITS");
    fieldOK(30, 346, 376, CHARACTERS, result, "Ship To Name 30 CHAR");
    fieldOK(24, 376, 400, CHARACTERS, result, "Primary Ship To Address 24 CHAR");
    fieldOK(24, 400, 424, CHARACTERS, result, "Secondary Ship To Address 24 CHAR");
    fieldOK(20, 424, 444, CHARACTERS, result, "Ship To City 20 CHAR");
    fieldOK(2, 444, 446, CHARACTERS, result, "Ship To State 2 CHAR");
    fieldOK(9, 446, 455, CHARACTERS, result, "Ship To Zip 9 CHAR");
    fieldOK(15, 455, 470, CHARACTERS, result, "EBT Voucher 15 CHAR");
  }

  /**
   * Method which checks the fields within the tmw credit card application requests
   * to determine if they are correct.
   * @param result    String containing credit request
   */
  private void verifyTMWApplication(String result) {
    System.out.println("\nVERIFYING TMW APPLICATION REQUEST");
    verifyHeader(result);
    fieldOK(1, 110, 111, CHARACTERS, result, "Message Id");
    fieldOK(11, 111, 122, CHARACTERS, result, "Pre-Approved Number");
    fieldOK(6, 122, 128, CHARACTERS, result, "Application Reference Number");
    fieldOK(1, 128, 129, CHARACTERS, result, "Function Code");
    fieldOK(14, 129, 143, CHARACTERS, result, "First Name");
    fieldOK(1, 143, 144, CHARACTERS, result, "Middle Name");
    fieldOK(25, 144, 169, CHARACTERS, result, "Last Name");
    fieldOK(35, 169, 204, CHARACTERS, result, "Address 1");
    fieldOK(35, 204, 239, CHARACTERS, result, "Address 2");
    fieldOK(22, 239, 261, CHARACTERS, result, "City");
    fieldOK(5, 261, 266, CHARACTERS, result, "State");
    fieldOK(9, 266, 275, CHARACTERS, result, "Zip Code");
    fieldOK(10, 275, 285, DIGITS, result, "Business Phone");
    fieldOK(10, 285, 295, DIGITS, result, "Home Phone");
    fieldOK(8, 295, 303, DIGITS, result, "Application Income");
    fieldOK(15, 303, 318, CHARACTERS, result, "Authorized User First Name");
    fieldOK(1, 318, 319, CHARACTERS, result, "Authorized User Middle Initial");
    fieldOK(15, 319, 334, CHARACTERS, result, "Authorized User Last Name");
    fieldOK(2, 334, 336, CHARACTERS, result, "Insurance Code");
    fieldOK(8, 336, 344, DIGITS, result, "Date of Birth");
    fieldOK(9, 344, 353, CHARACTERS, result, "SS Number");
    fieldOK(1, 353, 354, CHARACTERS, result, "Joint Auth");
    fieldOK(10, 354, 364, CHARACTERS, result, "Salesperson Code");
    fieldOK(14, 364, 378, CHARACTERS, result, "Spouse First Name");
    fieldOK(1, 378, 379, CHARACTERS, result, "Spouse Middle Initial");
    fieldOK(25, 379, 404, CHARACTERS, result, "Spouse Last Name");
    fieldOK(35, 404, 439, CHARACTERS, result, "Spouse Address 1");
    fieldOK(35, 439, 474, CHARACTERS, result, "Spouse Address 2");
    fieldOK(22, 474, 496, CHARACTERS, result, "Spouse City");
    fieldOK(5, 496, 501, CHARACTERS, result, "Spouse State");
    fieldOK(9, 501, 510, CHARACTERS, result, "Spouse Zip");
    fieldOK(8, 510, 518, DIGITS, result, "Spouse Income");
    fieldOK(9, 518, 527, DIGITS, result, "Spouse SSN");
  }

  /**
   * This method verifies that each field within the check request
   * is correct.
   * @param   result    String containing credit request for a check
   */
  private void verifyCheck(String result) {
    System.out.println("\nVERIFYING CHECK REQUEST");
    verifyHeader(result);
    fieldOK(76, 110, 186, CHARACTERS, result, "MICR Data 76 CHARS");
    fieldOK(1, 186, 187, CHARACTERS, result, "MICR 0=KEYED OR 1=SCANNED 1 CHAR");
    fieldOK(6, 187, 193, CHARACTERS, result, "Action Date MMDDYY 6 CHARS");
    fieldOK(29, 193, 222, CHARACTERS, result, "Account ID 29 CHARS");
    fieldOK(1, 222, 223, CHARACTERS, result, "Account ID 0=KEYED OR 1=SCANNED 1 CHAR");
    if (result.length() > 223) {
      fieldOK(1, 223, 224, CHARACTERS, result, "Location Type 1=COD, 2=RETAIL, 3=OTHER 1 CHAR");
      fieldOK(2, 224, 226, CHARACTERS, result, "MICR Line Format Code 2 CHARS");
      fieldOK(1, 226, 227, CHARACTERS, result, "Check Type P=Personal, B=Business 1 CHAR");
      fieldOK(30, 227, 257, CHARACTERS, result, "Name 30 CHARS");
      fieldOK(10, 257, 267, CHARACTERS, result, "Phone Number 10 CHARS");
      fieldOK(24, 267, 291, CHARACTERS, result, "Street Address 24 CHARS");
      fieldOK(20, 291, 311, CHARACTERS, result, "City 20 CHARS");
      fieldOK(2, 311, 313, CHARACTERS, result, "State 2 CHARS");
      fieldOK(9, 313, 322, CHARACTERS, result, "ZipCode 9 CHARS");
      fieldOK(3, 322, 325, DIGITS, result, "Currency Code 3 DIGITS");
      fieldOK(8, 325, 333, DIGITS, result, "Keyed Check Number 8 DIGITS");
      fieldOK(1, 333, 334, CHARACTERS, result, "Transaction Type 1 CHAR");
      fieldOK(3, 334, 337, DIGITS, result, "Product Code 3 DIGITS");
      fieldOK(1, 337, 338, CHARACTERS, result, "Delivery Method 1 CHAR");
      fieldOK(8, 338, 346, CHARACTERS, result, "Clerk ID 8 CHAR");
      fieldOK(17, 346, 363, CHARACTERS, result, "Billing Control 17 CHARS");
      fieldOK(11, 363, 374, DIGITS, result, "New Amount 11 DIGITS");
      fieldOK(22, 374, 396, CHARACTERS, result, "TeleCheck Trace ID 22 CHARS");
      fieldOK(1704, 396, 2100, CHARACTERS, result, "End Filler 1704 CHARS");
    }
  }

  /**
   * This method allows the configuration parameters
   * to be changed.
   * @param aKey  An array of strings containing keys
   */
  public void processConfigEvent(String[] aKey) {
    for (int x = 0; x < aKey.length; x++) {
      if (aKey[x].equalsIgnoreCase("LOCAL_TEST_MODE")) {
        String strLocalTestMode = config.getString("LOCAL_TEST_MODE");
        if (strLocalTestMode.equalsIgnoreCase("TRUE")) {
          localTestMode = true;
        } else {
          localTestMode = false;
        }
      }
      if (aKey[x].equalsIgnoreCase("VERBOSE_MODE")) {
        String strVerboseMode = config.getString("VERBOSE_MODE");
        if (strVerboseMode.equalsIgnoreCase("TRUE")) {
          verboseMode = true;
        } else {
          verboseMode = false;
        }
      }
    } // End of for (int x = 0; x < aKey.length; x++)
  }

  /**
   * put your documentation comment here
   * @param parm1
   * @param parm2
   */
  public void setSignatureValidation(Object parm1, CreditCard parm2) {
    /**@todo: implement this com.chelseasystems.cr.authorization.PaymentValidationRequests abstract method*/
  }

  /**
   * put your documentation comment here
   * @param parm1
   * @param parm2
   * @param parm3
   * @return
   */
  public Object getDCCRequest(CreditCard parm1, String parm2, String parm3) {

    /**@todo: implement this com.chelseasystems.cr.authorization.PaymentValidationRequests abstract method*/
    return null;
  }

  /**
   * put your documentation comment here
   * @param parm1
   * @param parm2
   * @param parm3
   * @return
   */
  public Object getSignatureValidationRequest(CreditCard parm1, String parm2, String parm3) {

    /**@todo: implement this com.chelseasystems.cr.authorization.PaymentValidationRequests abstract method*/
    return null;
  }

  /**
   * put your documentation comment here
   * @param parm1
   * @param parm2
   */
  public void setDCC(Object parm1, CreditCard parm2) {
    /**@todo: implement this com.chelseasystems.cr.authorization.PaymentValidationRequests abstract method*/
  }

  /**
   * put your documentation comment here
   * @param parm1
   * @param parm2
   * @param parm3
   * @return
   */
  public Object getCancelRequest(CreditCard parm1, String parm2, String parm3) {

    /**@todo: implement this com.chelseasystems.cr.authorization.PaymentValidationRequests abstract method*/
    return null;
  }

  /**
   * put your documentation comment here
   * @param parm1
   * @param parm2
   */
  public void setAuthCancelled(Object parm1, CreditCard parm2) {
    /**@todo: implement this com.chelseasystems.cr.authorization.PaymentValidationRequests abstract method*/
  } // End of public void processConfigEvent(String[] aKey)

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {}

  /**
   *
   * @param txnID String
   * @return String
   */
  private String getFormattedTransactionID(String txnID) {
    int beginIndex = 0;
    int lastIndex = -1;
    String formattedTxnID = "";
    for (; lastIndex < txnID.length(); ) {
      lastIndex = txnID.indexOf("*", beginIndex);
      if (lastIndex != -1) {
        formattedTxnID += txnID.substring(beginIndex, lastIndex);
        beginIndex = lastIndex + 1;
      } else {
        if (beginIndex == 0) {
          return txnID;
        } else {
          formattedTxnID += txnID.substring(beginIndex);
          return formattedTxnID;
        }
      }
    }
    return formattedTxnID;
  }
  
  
}
