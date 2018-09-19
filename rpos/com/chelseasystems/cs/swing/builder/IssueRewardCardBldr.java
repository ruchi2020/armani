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
 | 3    | 06-15-2005 | Vikram    | 144       | Modified to support MSR                      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-31-2005 | Megha     | 83        |Loyalty/Enroll: Number format Exception       |
 |      |            |           |             occurs when alpha-numeric value added.       |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.builder;

import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.IsDigit;


/**
 */
public class IssueRewardCardBldr implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String accountNum = null;
  private RewardCard theRewardCard = new RewardCard();
  private CMSMSR cmsMSR = null;

  /**
   */
  public IssueRewardCardBldr() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    this.accountNum = "";
  }

  /**
   */
  public void cleanup() {
    cmsMSR.release();
    this.accountNum = "";
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("ISSUE_REWARD")) {
      processSwipe((String)theEvent);
      //return;
    }
    if (theCommand.equals("ACCOUNT")) {
      processAccount((String)theEvent);
    }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "ISSUERWDCARD", theRewardCard, this);
      theRewardCard = null;
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    resetAttributes();
    this.applet = applet;
    theAppMgr.setSingleEditArea(applet.res.getString(
        "Swipe reward card or press 'Enter' for manual entry."), "ISSUE_REWARD");
    theAppMgr.setEditAreaFocus();
    System.out.println("reward card builder getting instance of CMSMSR...");
    CMSMSR cmsMSR = CMSMSR.getInstance();
    cmsMSR.registerCreditCardBuilder(this);
    cmsMSR.activate();
  }

  /**
   * @param input
   */
  public void processSwipe(String input) {
    if (input == null || input.length() == 0
        || (getRewardCardInfo(input) && processAccount(accountNum))) {
      completeAttributes();
      return;
    }
    //        if (getRewardCardInfo(accountNum)) {
    //
    //          // Check to see if the loyalty Card number already exists
    //          try {
    //
    //            if (CMSRedeemableHelper.findRedeemableById(theAppMgr,RewardCard.REWARD_CARD_TYPE,accountNum)!=null) {
    //              return;
    //            }
    //
    //            else {
    //              theRewardCard = LoyaltyBinRangeUtil.allocRewardCard(accountNum);
    //              if (theRewardCard != null) {
    //                populateRewardCard();
    //                theRewardCard.setId(accountNum);
    //                completeAttributes();
    //                return;
    //              }
    //              else {
    //                theAppMgr.showErrorDlg(applet.res.getString(
    //                    "The account number is invalid."));
    //              }
    //            }
    //          }
    //          catch (Exception e) {
    //            e.printStackTrace();
    //          }
    //
    //        }
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    // didn't swipe, get account number for ISSUERWDCARD type
    if (theRewardCard == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return (false);
    }
    // manual override only if in re-entry mode
    PaymentTransaction aTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    /*Commented for Armani
     if (thePayment != null & thePayment instanceof Amex) {
     if (cid == null) {
     theAppMgr.setSingleEditArea(applet.res.getString("Enter CID."), "CID");
     return (false);
     }
     }*/
    return (true);
  }

  /**
   */
  private void resetAttributes() {
    theRewardCard = null;
    accountNum = null;
  }

  /**
   */
  private void populateRewardCard() {
    Date issueDate = new Date();
    String expDate = "";
    ArmCurrency curr = new ArmCurrency("0.0");
    try {
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      String rewardAmount = config.getString("LOYALTY_REWARD_AMOUNT");
      expDate = config.getString("REWARD_EXPIRY_DAYS");
      curr = new ArmCurrency(rewardAmount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Calendar rightNow = Calendar.getInstance();
    rightNow.setTime(new Date());
    rightNow.add(Calendar.DAY_OF_YEAR, new Integer(expDate).intValue());
    //rwd.setId(accoun);
    // Add this in applet.
    //        rwdCard.setLoyalty(loyaltyCard);
    //        rwdCard.getLoyalty().setLoyaltyNumber(loyaltyCard.getLoyaltyNumber());
    theRewardCard.setCreateDate(issueDate);
    theRewardCard.setExpDate(rightNow.getTime());
    theRewardCard.setIssueAmount(curr);
    theRewardCard.setStatus(true);
    theRewardCard.setIssuingStoreId(((CMSStore)theAppMgr.getGlobalObject("STORE")).getId());
    //rwdCard.setAuditNote("Reward Card");
    return;
  }

  /**
   * put your documentation comment here
   * @param theEvent
   * @return
   */
  private boolean processAccount(String theEvent) {
    accountNum = ((String)theEvent);
    // Check to see if the loyaltyNum already exists.
    boolean reply = true;
    IsDigit digitCheck = new IsDigit();
    if (!digitCheck.isDigit(accountNum)) {
      theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return false;
    }
    try {
      if (CMSRedeemableHelper.findRedeemable(theAppMgr, accountNum) != null) {
        theAppMgr.showErrorDlg(applet.res.getString("The RewardCard already exists"));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    theRewardCard = LoyaltyBinRangeUtil.allocRewardCard(accountNum);
    // if payment is still null (manual account is bad then
    // return null and start over
    if (theRewardCard == null) {
      theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return false;
    }
    if (!isValidInStore((RewardCard)theRewardCard)) {
      theAppMgr.showErrorDlg(applet.res.getString(
          "This reward card type is not valid in this store."));
      return false;
    }
    theRewardCard.setId(accountNum);
    populateRewardCard();
    return true;
  }

  /**
   * put your documentation comment here
   * @param accountNum
   */
  public void setAccountNum(String accountNum) {
    this.accountNum = new IsDigit().filterToGetDigits(accountNum);
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getRewardCardInfo(String inputStr) {
    if (this.cmsMSR instanceof NonJavaPOSMSR)
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
        theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe loyalty card or press 'Enter' for manual entry."), "ISSUE_REWARD");
        theAppMgr.setEditAreaFocus();
        theAppMgr.showErrorDlg(applet.res.getString("Error reading data from MSR."));
        return (false);
      }
    return (true);
  }

  /**
   * @return
   */
  public String getAccountNum() {
    return (accountNum);
  }

  /**
   * put your documentation comment here
   * @param cmsMSR
   */
  public void setCMSMSR(CMSMSR cmsMSR) {
    this.cmsMSR = cmsMSR;
  }

  /**
   * put your documentation comment here
   * @param rewardCard
   * @return
   */
  private boolean isValidInStore(RewardCard rewardCard) {
    ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
    String brandID = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getBrandID();
    String loyaltyStrBrandID = loyaltyConfigMgr.getString(LoyaltyBinRangeUtil.CardType + ".TYPE");
    if (loyaltyStrBrandID != null && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim()))
      return true;
    //else
    return false;
  }
}

