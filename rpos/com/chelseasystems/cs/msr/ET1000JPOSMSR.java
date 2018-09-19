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
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.swing.builder.ET1000CreditCardBldr;
import com.chelseasystems.cs.swing.builder.CreditCardBldr;
import com.chelseasystems.cs.swing.builder.DueBillBldr;
import com.chelseasystems.cs.swing.builder.DueBillIssueBldr;
import com.chelseasystems.cs.swing.builder.StoreValueCardBldr;
import com.chelseasystems.cs.swing.builder.IssueRewardCardBldr;
import com.chelseasystems.cs.swing.builder.LoyaltyBldr;
import com.chelseasystems.cs.swing.builder.LoyaltyItemBldr;
import com.chelseasystems.cs.swing.builder.RewardDiscountBldr;
import com.chelseasystems.cs.swing.builder.RedeemableInquiryBldr;
import java.util.*;
import java.text.*;
import jpos.*;
import jpos.events.*;


/**
 * put your documentation comment here
 */
public class ET1000JPOSMSR extends CMSMSR implements DataListener {
  private static ET1000JPOSMSR et1000JPOSMSR = new ET1000JPOSMSR();
  private MSR msr;

  /**
   * put your documentation comment here
   */
  private ET1000JPOSMSR() {
    msr = new MSR();
    try {
      msr.open(this.getMSRLogicalName());
      msr.addDataListener(this);
    } catch (JposException je) {
      System.out.println("ET1000JPOSMSR() -> error on open msr -- " + je);
      je.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected static ET1000JPOSMSR getET1000JPOSMSRInstance() {
    MSR instance = et1000JPOSMSR.msr;
    try {
      if (instance.getClaimed()) {
        instance.release();
      }
      ET1000JPOSForm.getInstance().displayScanCardMessage();
      if (!instance.getClaimed()) {
        instance.claim( -1);
      }
      if (!instance.getDeviceEnabled()) {
        instance.setDeviceEnabled(true);
      }
      instance.setParseDecodeData(true);
      instance.setDecodeData(true);
      instance.clearInput();
    } catch (JposException je) {
      System.out.println("ET1000JPOSMSR.getET1000JPOSMSRInstance() -> " + je);
      je.printStackTrace();
      LoggingServices.getCurrent().logMsg("ET1000JPOSMSR", "ET1000JPOSMSR()", "Exception"
          , "See Exception", LoggingServices.MAJOR, je);
    }
    return et1000JPOSMSR;
  }

  /**
   * put your documentation comment here
   * @param bldr
   */
  public void registerCreditCardBuilder(ET1000CreditCardBldr bldr) {
    super.registerCreditCardBuilder(bldr);
    ET1000JPOSForm.getInstance().setCreditCardBuilder(bldr);
    ET1000JPOSPINPad.getInstance().setCreditCardBuilder(bldr);
  }

  /**
   * put your documentation comment here
   * @param dataEvent
   */
  public void dataOccurred(DataEvent dataEvent) {
    try {
      System.out.println("ET1000JPOSMSR Data event has occurred [" + msr.getAccountNumber() + "]");
      //****************************************************************************
       if (this.getCreditCardBldr() instanceof CreditCardBldr) {
         ((CreditCardBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         ((CreditCardBldr)this.getCreditCardBldr()).setCardHolderName(msr.getFirstName() + " "
             + msr.getMiddleInitial() + " " + msr.getSurname());
         ((CreditCardBldr)this.getCreditCardBldr()).setExpDate(msr.getExpirationDate());
         ((CreditCardBldr)this.getCreditCardBldr()).setServiceCode(msr.getServiceCode());
         ((CreditCardBldr)this.getCreditCardBldr()).setTrackNumber("");
         ((CreditCardBldr)this.getCreditCardBldr()).setRawData("");
         //commenting the code for PCI Compliance
       /*  if (msr.getAccountNumber().length() > 2)
           ((CreditCardBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((CreditCardBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
            */
       } else if (this.getCreditCardBldr() instanceof DueBillBldr) {
         ((DueBillBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((DueBillBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((DueBillBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       } else if (this.getCreditCardBldr() instanceof DueBillIssueBldr) {
         ((DueBillIssueBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((DueBillIssueBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((DueBillIssueBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       } else if (this.getCreditCardBldr() instanceof StoreValueCardBldr) {
         ((StoreValueCardBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((StoreValueCardBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((StoreValueCardBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       } else if (this.getCreditCardBldr() instanceof IssueRewardCardBldr) {
         ((IssueRewardCardBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((IssueRewardCardBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((IssueRewardCardBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       } else if (this.getCreditCardBldr() instanceof LoyaltyBldr) {
         ((LoyaltyBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((LoyaltyBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((LoyaltyBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       } else if (this.getCreditCardBldr() instanceof LoyaltyItemBldr) {
         ((LoyaltyItemBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((LoyaltyItemBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((LoyaltyItemBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       } else if (this.getCreditCardBldr() instanceof RewardDiscountBldr) {
         ((RewardDiscountBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((RewardDiscountBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((RewardDiscountBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       } else if (this.getCreditCardBldr() instanceof RedeemableInquiryBldr) {
         ((RedeemableInquiryBldr)this.getCreditCardBldr()).setAccountNum(msr.getAccountNumber());
         if (msr.getAccountNumber().length() > 2)
           ((RedeemableInquiryBldr)this.getCreditCardBldr()).processSwipe("ET1000_MSR_DEVICE"); //this will check error and populateCreditCard
         else
            ((RedeemableInquiryBldr)this.getCreditCardBldr()).processSwipe(""); //no input, do manual input
       }
       //****************************************************************************
        else {
          this.getET1000CreditCardBldr().setAccountNum(msr.getAccountNumber());
          this.getET1000CreditCardBldr().setCardHolderName(msr.getFirstName() + " "
              + msr.getMiddleInitial() + " " + msr.getSurname());
          this.getET1000CreditCardBldr().setExpDate(msr.getExpirationDate());
          this.getET1000CreditCardBldr().setServiceCode(msr.getServiceCode());
          this.getET1000CreditCardBldr().setTrackNumber("");
          this.getET1000CreditCardBldr().setRawData("");
          this.getET1000CreditCardBldr().completeAttributes();
        }
    } catch (JposException je) {
      System.out.println("ET1000JPOSMSR.dataOccurred(DataEvent) -> error on reading MSR data" + je);
      je.printStackTrace();
    } catch (Exception e) {
      System.out.println("ET1000JPOSMSR.dataOccurred(DataEvent) -> error on reading MSR data" + e);
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void release() {
    MSR instance = this.msr;
    try {
      if (instance.getClaimed()) {
        instance.release();
      }
    } catch (JposException je) {
      System.out.println("ET1000JPOSMSR.release() -> " + je);
      je.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public static void releaseForClearnup() {
    et1000JPOSMSR.release();
  }

  /**
   * put your documentation comment here
   * @return
   */
  private ET1000CreditCardBldr getET1000CreditCardBldr() {
    return (ET1000CreditCardBldr)this.getCreditCardBldr();
  }

  /**
   * This method should enable event handling.
   */
  public void activate() {}
}
