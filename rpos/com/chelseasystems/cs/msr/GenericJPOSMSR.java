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

import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.swing.builder.CreditCardBldr;
import jpos.*;
import jpos.events.*;
import com.chelseasystems.cs.swing.builder.DueBillBldr;
import com.chelseasystems.cs.swing.builder.DueBillIssueBldr;
import com.chelseasystems.cs.swing.builder.StoreValueCardBldr;
import com.chelseasystems.cs.swing.builder.IssueRewardCardBldr;
import com.chelseasystems.cs.swing.builder.LoyaltyBldr;
import com.chelseasystems.cs.swing.builder.LoyaltyItemBldr;
import com.chelseasystems.cs.swing.builder.RewardDiscountBldr;
import com.chelseasystems.cs.swing.builder.RedeemableInquiryBldr;


/**
 * This class represents a generic JPOS MSR.  On construction, setAutoDisable(false) is called.
 */
public class GenericJPOSMSR extends CMSMSR implements DataListener {
  private static GenericJPOSMSR genericJPOSMSR = new GenericJPOSMSR();
  private MSR msr;

  /**
   * put your documentation comment here
   */
  private GenericJPOSMSR() {
    msr = new MSR();
    try {
      System.out.println("CMSMSR() -> opening msr: " + this.getMSRLogicalName());
      msr.addDataListener(this);
      msr.open(this.getMSRLogicalName());
      msr.claim(1000);
      msr.setDeviceEnabled(true);
      msr.setParseDecodeData(true);
      msr.setDecodeData(true);
      msr.setAutoDisable(true);
      msr.clearInput();
    } catch (JposException je) {
      System.out.println("CMSMSR() -> error on open msr" + je);
      je.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "GenericJPOSMSR", "Exception"
          , "See Exception", LoggingServices.MAJOR, je);
    }
  }

  /**
   * This returns the one instance of a MSR that is registered to this VM.
   * @return genericJPOSMSR
   */
  protected static GenericJPOSMSR getGenericJPOSMSRInstance() {
    return genericJPOSMSR;
  }

  /**
   * Enable event handling for active MSR.
   */
  public void activate() {
    System.out.println("GenericJPOSMSR.activate() called...");
    try {
      msr.setDeviceEnabled(true);
      msr.setDataEventEnabled(true);
      msr.clearInput();
    } catch (JposException je) {
      System.out.println("GerericJPOSMSR.release() -> error on msr activate " + je);
      je.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "GenericJPOSMSR", "Exception"
          , "See Exception", LoggingServices.MAJOR, je);
    }
  }

  /**
   * This method disables event handling for an active msr.
   */
  public void release() {
    System.out.println("GenericJPOSMSR.release() called...");
    try {
      if (msr.getDeviceEnabled()) {
        msr.setDeviceEnabled(false);
      }
    } catch (JposException je) {
      System.out.println("GerericJPOSMSR.release() -> error on msr release " + je);
      je.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "GenericJPOSMSR", "Exception"
          , "See Exception", LoggingServices.MAJOR, je);
    }
  }

  /**
   * Event Handler. Interacts with CreditCardBldr.
   * @param dataEvent
   */
  public void dataOccurred(DataEvent dataEvent) {
    //System.out.println("MSR dataOccurred: " + dataEvent.getStatus());
    try {
      System.out.println("MSR Data event has occurred [" + msr.getAccountNumber() + "]");
      if (this.getCreditCardBldr() instanceof CreditCardBldr) {
        ((CreditCardBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        ((CreditCardBldr)this.getCreditCardBldr()).setCardHolderName(msr.getFirstName() + " "
            + msr.getMiddleInitial() + " " + msr.getSurname());
        ((CreditCardBldr)this.getCreditCardBldr()).setExpDate(msr.getExpirationDate());
        ((CreditCardBldr)this.getCreditCardBldr()).setServiceCode(msr.getServiceCode());
        ((CreditCardBldr)this.getCreditCardBldr()).setTrackNumber("");
        ((CreditCardBldr)this.getCreditCardBldr()).setRawData("");
        //commenting the code for PCI Compliance
       /* if (msr.getAccountNumber().length() > 2)
          ((CreditCardBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((CreditCardBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
*/      } else if (this.getCreditCardBldr() instanceof DueBillBldr) {
        ((DueBillBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((DueBillBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((DueBillBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      } else if (this.getCreditCardBldr() instanceof DueBillIssueBldr) {
        ((DueBillIssueBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((DueBillIssueBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((DueBillIssueBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      } else if (this.getCreditCardBldr() instanceof StoreValueCardBldr) {
        ((StoreValueCardBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((StoreValueCardBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((StoreValueCardBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      } else if (this.getCreditCardBldr() instanceof IssueRewardCardBldr) {
        ((IssueRewardCardBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((IssueRewardCardBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((IssueRewardCardBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      } else if (this.getCreditCardBldr() instanceof LoyaltyBldr) {
        ((LoyaltyBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((LoyaltyBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((LoyaltyBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      } else if (this.getCreditCardBldr() instanceof LoyaltyItemBldr) {
        ((LoyaltyItemBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((LoyaltyItemBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((LoyaltyItemBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      } else if (this.getCreditCardBldr() instanceof RewardDiscountBldr) {
        ((RewardDiscountBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((RewardDiscountBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((RewardDiscountBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      } else if (this.getCreditCardBldr() instanceof RedeemableInquiryBldr) {
        ((RedeemableInquiryBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
        if (msr.getAccountNumber().length() > 2)
          ((RedeemableInquiryBldr)this.getCreditCardBldr()).processSwipe("JPOS_MSR_DEVICE"); //this will check error and populateCreditCard
        else
           ((RedeemableInquiryBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
      }
      msr.setDataEventEnabled(false); // Deactive the MSR.
    } catch (JposException je) {
      System.out.println("dataOccurred(DataEvent) -> error on reading MSR data" + je);
    }
  }
}
