/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 06-15-2005 | Vikram    | 144       | Modified to support MSR to other builders    |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */


package com.chelseasystems.cs.msr;

import com.chelseasystems.cs.swing.builder.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.util.CreditAuthUtil;
import com.chelseasystems.cs.util.Version;
//import com.sun.java_cup.internal.version;


/**
 * put your documentation comment here
 */
public class NonJavaPOSMSR extends CMSMSR {
  private String accountNum = null;
  private String cardHolderName = null;
  private String expDate = null;
  private String serviceCode = null;
  private String rawData = null;
  private String trackNumber = null;
  
  //added by shushma for duty free management
  private String flightNo = null;
	private String checkIn = null;
	private String comp = null;
	private String seatNo = null;

  /* Used to obtain configuration parameters from config files */
  private ConfigMgr config;
  private boolean MSRDebugMode = false;
private String destination;

  /**
   * put your documentation comment here
   */
  private NonJavaPOSMSR() {
    // Start Issue # 991
    config = new ConfigMgr("JPOS_peripherals.cfg");
    String strMSRDebugMode = config.getString("MSR_DEBUG_MODE");
    if (strMSRDebugMode.equalsIgnoreCase("TRUE")) {
      MSRDebugMode = true;
    }
    System.out.println("MSR DEBUG MODE=" + MSRDebugMode);
    // End Issue # 991
  }

  /**
   * put your documentation comment here
   * @param   String inputStr
   */
  public NonJavaPOSMSR(String inputStr) {
    if (!extractData(inputStr))
      accountNum = null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected static NonJavaPOSMSR getNonJavaPOSMSRInstance() {
    return new NonJavaPOSMSR();
  }

  /**
   * put your documentation comment here
   */
  public void release() {
    //this method is for JPOS devices only, do nothing here
  }

  /**
   * put your documentation comment here
   */
  public void activate() {
    // this method is for JPOS devices only.
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAccountNum() {
    return this.accountNum;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCardHolderName() {
    return this.cardHolderName;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getExpDate() {
    return this.expDate;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getServiceCode() {
    return this.serviceCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getRawData() {
    return this.rawData;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTrackNumber() {
    return this.trackNumber;
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean extractDataToBuilder(String inputStr) {
    if (!this.extractData(inputStr))
      return false;
    if (this.getCreditCardBldr() instanceof CreditCardBldr) {
      ((CreditCardBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
      ((CreditCardBldr)this.getCreditCardBldr()).setCardHolderName(this.cardHolderName);
      ((CreditCardBldr)this.getCreditCardBldr()).setExpDate(this.expDate);
      ((CreditCardBldr)this.getCreditCardBldr()).setServiceCode(this.serviceCode);
      ((CreditCardBldr)this.getCreditCardBldr()).setRawData(this.rawData);
      ((CreditCardBldr)this.getCreditCardBldr()).setTrackNumber(this.trackNumber);
    }
    // Issue # 989
    else if (this.getCreditCardBldr() instanceof CustomerCreditCardBldr) {
      ((CustomerCreditCardBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
      ((CustomerCreditCardBldr)this.getCreditCardBldr()).setCardHolderName(this.cardHolderName);
      ((CustomerCreditCardBldr)this.getCreditCardBldr()).setExpDate(this.expDate);
    }
    // End Issue # 989
    else if (this.getCreditCardBldr() instanceof DueBillBldr) {
      ((DueBillBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    } else if (this.getCreditCardBldr() instanceof DueBillIssueBldr) {
      ((DueBillIssueBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    } else if (this.getCreditCardBldr() instanceof StoreValueCardBldr) {
      ((StoreValueCardBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    } else if (this.getCreditCardBldr() instanceof IssueRewardCardBldr) {
      ((IssueRewardCardBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    } else if (this.getCreditCardBldr() instanceof LoyaltyBldr) {
      ((LoyaltyBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    } else if (this.getCreditCardBldr() instanceof LoyaltyItemBldr) {
      ((LoyaltyItemBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    } else if (this.getCreditCardBldr() instanceof RewardDiscountBldr) {
      ((RewardDiscountBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    } else if (this.getCreditCardBldr() instanceof RedeemableInquiryBldr) {
      ((RedeemableInquiryBldr)this.getCreditCardBldr()).setAccountNum(this.accountNum);
    }
     //****************************************************************************
     return true;
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean extractData(String inputStr) {
	int currentIndex = 0;
    int startIndex = 0;
    int stopIndex = 0;
    int trackTwoBegIndex = 0;
    int trackTwoAccountNumEndIndex = 0;
    int trackTwoEndIndex = 0;
    int trackOneEndIndex = 0;
    boolean trackOnePresent = false;
    boolean trackTwoPresent = false;
    System.out.println("The valus of MSRDebugMode=" + MSRDebugMode);
    if (MSRDebugMode)
      System.out.println(inputStr);
    // Extracting track two data
    if ((trackTwoBegIndex = inputStr.indexOf(";", currentIndex)) != -1) {
      trackTwoPresent = true;
      trackTwoBegIndex += 1; //pass special char :.
      if ((trackTwoEndIndex = inputStr.indexOf("?", trackTwoBegIndex)) != -1) {
        rawData = inputStr.substring(trackTwoBegIndex, trackTwoEndIndex);
        trackNumber = "2";
      } else {
        rawData = inputStr.substring(startIndex);
        trackNumber = "2";
      }
      if ((trackTwoAccountNumEndIndex = inputStr.indexOf("=", trackTwoBegIndex)) != -1) {} else
        trackTwoAccountNumEndIndex = trackTwoEndIndex;
      if (MSRDebugMode)
        System.out.println("Track two end=" + trackTwoEndIndex);
    } else
      trackTwoPresent = false; //failure case
    if (MSRDebugMode) {
      System.out.println("Track two beg=" + trackTwoBegIndex);
      System.out.println("Track two end=" + trackTwoEndIndex);
    }
    //extracting the account number
    if ((startIndex = inputStr.indexOf("%B", currentIndex)) != -1) {
      startIndex += 2; //pass these special chars.
      currentIndex = startIndex;
      trackOnePresent = true;
      if (!trackTwoPresent) {
        if((trackOneEndIndex = inputStr.indexOf("?", currentIndex)) != -1)
        {
          rawData = inputStr.substring(startIndex,trackOneEndIndex+1);
           trackNumber = "1";
        }  else {
           rawData = inputStr.substring(startIndex);
           trackNumber = "1";
        }
      }
    } else
      trackOnePresent = false; //failure case
    if ((stopIndex = inputStr.indexOf('^', currentIndex)) != -1) {
      currentIndex = stopIndex;
    } else
      trackOnePresent = false; //failure case
    if (trackTwoPresent) {
          if (MSRDebugMode) {
            System.out.println("TRACK TWO PRESENT");
            System.out.println("RAW DATA ========"
                + CreditAuthUtil.maskCreditCardNo(rawData));
          }
          accountNum = CreditCardBldrUtil.removeSpaces(inputStr.substring(trackTwoBegIndex
              , trackTwoAccountNumEndIndex));
          if ((stopIndex = inputStr.indexOf('=', currentIndex)) != -1) {
            //extracting the expiration date
            currentIndex = stopIndex + 1;
            expDate = inputStr.substring(currentIndex, currentIndex + 4);
            // extracting the service code
            serviceCode = inputStr.substring(currentIndex + 4, currentIndex + 7);
          }
    } else if (trackOnePresent) {
    	//Anjana commenting the account number 
   //   accountNum = CreditCardBldrUtil.removeSpaces(inputStr.substring(startIndex, stopIndex));
      //extracting the cardholder name
      startIndex = stopIndex + 1;
      currentIndex = startIndex;
      if ((stopIndex = inputStr.indexOf('^', currentIndex)) != -1) {
        currentIndex = stopIndex;
      } else
        return false; //failure case
      // Card read format is  LASTNAME/FIRST M
      cardHolderName = inputStr.substring(startIndex, stopIndex).trim();
      // Must be extracted out and written to database as FIRST MIDDLE LAST.
      int slashPosition = 0;
      if ((slashPosition = cardHolderName.indexOf('/', 0)) != -1) {
        String lastName = cardHolderName.substring(0, slashPosition);
        String firstName = cardHolderName.substring(slashPosition + 1);
        cardHolderName = firstName + " " + lastName;
      }
      //extracting the expiration date
      currentIndex = stopIndex + 1;
      expDate = inputStr.substring(currentIndex, currentIndex + 4);
      // extracting the service code
      //Anjana commenting the service code
    //  serviceCode = inputStr.substring(currentIndex + 4, currentIndex + 7);
    } else
      return false;
    this.accountNum = accountNum;
    this.cardHolderName = cardHolderName;
    this.expDate = expDate;
    this.serviceCode = serviceCode;
    this.rawData = rawData;
    this.trackNumber = trackNumber;
    
   return true;
  }
	  
}

