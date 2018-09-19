/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.discount.RewardDiscount;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.msr.CMSMSR;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransaction;

import java.util.Date;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.register.LightPoleDisplay;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.discount.CMSDiscountMgr;
import com.chelseasystems.cs.msr.NonJavaPOSMSR;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.store.CMSStore;
import java.util.Enumeration;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.util.Version; 
/**
 * Builder to construct an 'Reward Discount' <code>Discount</code> object.
 */
public class RewardDiscountBldr implements IObjectBuilder {
  private RewardDiscount rewardDiscount = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private ConfigMgr config = null;
  private CMSCompositePOSTransaction theTxn;
  private CMSMSR cmsMSR = null;
  private boolean rewardCardInquery=false;

  //private boolean offlineMode = false;
  /**
   */
  public RewardDiscountBldr() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    config = new ConfigMgr(System.getProperty("USER_CONFIG"));
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    this.accountNum = "";
  }

  /**
   */
  public void cleanup() {
    rewardDiscount = null;
    cmsMSR.release();
    this.accountNum = "";
  }

  /**
   * put your documentation comment here
   */
  public void resetAttributes() {
    if (rewardDiscount != null) {
      rewardDiscount.doSetRewardCard(null);
      rewardDiscount.doSetAmount(null);
    }
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("REWARD")) {
      processSwipe((String)theEvent);
      return;
    }
    if (theCommand.equals("REWARD_CARD")) {
      processSwipe((String)theEvent);
      return;
    }

    if (theCommand.equals("ACCOUNT")) {
      boolean accountSet= processAccount((String)theEvent);
      if (rewardCardInquery && accountSet){
        return;
      }
    }
    if (theCommand.equals("AUTH_CODE")) {
      if (rewardDiscount.getRewardCard() != null)
        rewardDiscount.getRewardCard().setManualAuthCode((String)theEvent);
      //return;
    }
    if (theCommand.equals("AMOUNT")) {
        //rewardDiscount.doSetAmount((ArmCurrency)theEvent);
      	//Started code for issue #1947 Light pole display by Neeti
        try {
      	  if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
      	  ArmCurrency disAmount = (ArmCurrency)theEvent;    	
      	  POSLineItem lineItem = (POSLineItem)theAppMgr.getStateObject("SELECTED_LINE_ITEM");
      	    POSLineItem[] lineItemArray = theTxn.getLineItemsArray();     	     
      	    for(int i=0; i<lineItemArray.length ; i++){
      			  lineItem = lineItemArray[i];      	
      		 LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),lineItem.getExtendedRetailAmount().subtract(disAmount).formattedStringValue());
      	    }
      	  }
          //Ended code for issue #1947 Light pole display by Neeti
          processAmount((ArmCurrency)theEvent);
        } catch (Exception ex) {
          ex.printStackTrace();
          theAppMgr.showErrorDlg(applet.res.getString("Please enter valid amount"));
        }
      }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "DISCOUNT", rewardDiscount, this);
      rewardDiscount = null;
      resetAttributes();
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    //rewardDiscount = new RewardDiscount();
    rewardDiscount = (RewardDiscount)CMSDiscountMgr.createDiscount("REWARD_DISCOUNT");
    if (rewardDiscount == null)
      return;
    resetAttributes();
    this.applet = applet;
    //System.out.println("reward discount builder getting instance of CMSMSR...");
    //Vivek Mishra : Changed to resolve POS non working issue 13-OCT-2016
    //CMSMSR cmsMSR = CMSMSR.getInstance();
    cmsMSR = CMSMSR.getInstance();
    cmsMSR.registerCreditCardBuilder(this);
    cmsMSR.activate();
    if (Command.equalsIgnoreCase("REWARD_CARD")){
      rewardCardInquery=true;
      theAppMgr.setSingleEditArea(applet.res.getString(
          "Swipe reward card or press 'Enter' for manual entry."), "REWARD_CARD", theAppMgr.NO_MASK);
    } else{
      theAppMgr.setSingleEditArea(applet.res.getString(
          "Swipe reward card or press 'Enter' for manual entry."), "REWARD", theAppMgr.NO_MASK);
    }
    theAppMgr.setEditAreaFocus();
    // register for call backs
    //        if (completeAttributes()) {
    //          theBldrMgr.processObject(applet, "DISCOUNT", rewardDiscount, this);
    //        }
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
      //       rewardDiscount.setRewardCard(getRewardCardUsingId(accountNum));
      // if the card is expired or invalid
      //       if(rewardDiscount.getRewardCard()==null)
      if (rewardCardInquery){
        theBldrMgr.processObject(applet, "REWARD_CARD", null, this);
      }else{
        theBldrMgr.processObject(applet, "DISCOUNT", null, this);
      }
  }
  /**
   * put your documentation comment here
   * @param input
   * @return
   */
  public boolean processAccount(String input) {
    if (input == null || input.length() == 0) {
      if (rewardCardInquery){
        theAppMgr.showErrorDlg(applet.res.getString("Please Enter Reward Card Nunber."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter Customer Reward Card."), "ACCOUNT"
            , theAppMgr.NO_MASK);
      }else {
        theAppMgr.showErrorDlg(applet.res.getString("Please Enter Reward Discount ID."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter Reward Discount ID."), "ACCOUNT"
            , theAppMgr.NO_MASK);
      }
      return false;
    }
    accountNum = input;
    if (rewardCardInquery){
    // Just set the Account Number
    theBldrMgr.processObject(applet, "REWARD_CARD", new String(accountNum), this);
    rewardDiscount = null;
    resetAttributes();
    }else{
      Enumeration enm = theTxn.getSettlementsDiscounts();
      while(enm.hasMoreElements()){
        CMSDiscount dis = (CMSDiscount)enm.nextElement();
        if (dis instanceof RewardDiscount){
          RewardDiscount rd = (RewardDiscount)dis;
          if (accountNum.equals(rd.getRewardCard().getId())){
            theAppMgr.showErrorDlg(applet.res.getString("Reward card already used for this sale."));
            theAppMgr.setSingleEditArea(applet.res.getString(
                "Swipe reward card or press 'Enter' for manual entry."), "REWARD_CARD", theAppMgr.NO_MASK);
            return false;
          }
        }
      }//end while

      rewardDiscount.setRewardCard(getRewardCardUsingId(accountNum));
    }
    return true;
    // if the card is expired or invalid
    //       if(rewardDiscount.getRewardCard()==null)
    //           theBldrMgr.processObject(applet, "DISCOUNT", null, this);
  }

  /**
   * put your documentation comment here
   * @param amt
   * @exception CurrencyException, BusinessRuleException
   */
  public void processAmount(ArmCurrency amt)
      throws CurrencyException, BusinessRuleException {
    if (amt == null || amt.greaterThan(rewardDiscount.getRewardCard().getIssueAmount())) {
      completeAttributes();
      return;
    }
    rewardDiscount.setAmount(amt);
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
      if (rewardDiscount == null) {
        rewardDiscount = (RewardDiscount)CMSDiscountMgr.createDiscount("REWARD_DISCOUNT");
        resetAttributes();
      }
      if (rewardDiscount.getRewardCard() == null) {
        if (rewardCardInquery){
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Customer Reward Card."), "ACCOUNT"
              , theAppMgr.NO_MASK);
        }else {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Reward Discount ID."), "ACCOUNT"
              , theAppMgr.NO_MASK);
        }
        theAppMgr.setEditAreaFocus();
        return (false);
      }

      if (rewardCardInquery){
        theBldrMgr.processObject(applet, "REWARD_CARD", new String(accountNum), this);
        return true;
      }

      if (!theAppMgr.isOnLine()
          && (rewardDiscount.getRewardCard().getManualAuthCode() == null
          || rewardDiscount.getRewardCard().getManualAuthCode().trim().equals(""))) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter Authorization Code."), "AUTH_CODE"
            , theAppMgr.NO_MASK);
        theAppMgr.setEditAreaFocus();
        return (false);
      }
      if (rewardDiscount.getAmount() == null) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter Amount"), "AMOUNT"
            , rewardDiscount.getRewardCard().getIssueAmount().getAbsoluteValue()
            , theAppMgr.CURRENCY_MASK);
        theAppMgr.setEditAreaFocus();
        return false;
      }
      try {
        rewardDiscount.setReason(rewardDiscount.getGuiLabel());
      } catch (BusinessRuleException ex) {
        ex.printStackTrace();
      }
      return (true);
  }

  /**
   * put your documentation comment here
   * @param rewardCardId
   * @return
   */
  public RewardCard getRewardCardUsingId(String rewardCardId) {
    Redeemable redeemable = null;
    if (!theAppMgr.isOnLine()) { //***************************** check for off-line
      // for off-line flow
      if (LoyaltyBinRangeUtil.validateEnteredRewardCard(rewardCardId)) {
        //offlineMode = true;
        redeemable = new RewardCard();
        ((RewardCard)redeemable).setId(rewardCardId);
        ConfigMgr config = new ConfigMgr("loyalty.cfg");
        String rewardAmount = config.getString("LOYALTY_REWARD_AMOUNT");
        ((RewardCard)redeemable).setIssueAmount(new ArmCurrency(rewardAmount));
      }
      return (RewardCard)redeemable;
    }
    //else
    //offlineMode = false;
    try {
      redeemable = CMSRedeemableHelper.findRedeemable(theAppMgr, rewardCardId);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    if (redeemable != null && redeemable instanceof RewardCard) {
      if (((RewardCard)redeemable).getExpDate() != null
          && new Date().after(((RewardCard)redeemable).getExpDate())) {
        theAppMgr.showErrorDlg(applet.res.getString("Reward has expired."));
        return null;
      }
      if (!((RewardCard)redeemable).getStatus()) {
        theAppMgr.showErrorDlg(applet.res.getString("This card is no more valid."));
        return null;
      }
      if (!isValidInStore((RewardCard)redeemable)) {
        theAppMgr.showErrorDlg(applet.res.getString(
            "This reward card cannot be used in this store."));
        return null;
      }
      return (RewardCard)redeemable;
    } else
      theAppMgr.showErrorDlg(applet.res.getString("This card is not a valid reward card"));
    return null;
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getRewardCardInfo(String inputStr) {
    if (this.cmsMSR instanceof NonJavaPOSMSR)
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))){
        theAppMgr.showErrorDlg(applet.res.getString("Failure reading reward card data."));
        if (rewardCardInquery){
          theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe reward card or press 'Enter' for manual entry."), "REWARD_CARD",theAppMgr.NO_MASK);
        } else{
          theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe reward card or press 'Enter' for manual entry."), "REWARD",theAppMgr.NO_MASK);
        }
        return (false);
      }
    return (true);
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
   * @param accountNum
   */
  public void setAccountNum(String accountNum) {
    this.accountNum = new IsDigit().filterToGetDigits(accountNum);
    ;
  }

  private String accountNum = "";

  /**
   * put your documentation comment here
   * @param rewardCard
   * @return
   */
  private boolean isValidInStore(RewardCard rewardCard) {
    ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
    String brandID = ((CMSStore)theTxn.getStore()).getBrandID();
    String loyaltyStrBrandID = loyaltyConfigMgr.getString(rewardCard.getLoyalty().getStoreType()
        + ".TYPE");
    if (loyaltyStrBrandID != null && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim()))
      return true;
    //else
    return false;
  }
}

