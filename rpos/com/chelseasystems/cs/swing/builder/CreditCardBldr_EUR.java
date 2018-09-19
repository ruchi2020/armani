/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06/23/2005 | Khyati    | N/A       | Base. Copied from Global                           |
 --------------------------------------------------------------------------------------------------
 | 2    | 06/23/2005 | Khyati    | N/A       | Europe: credit card                                |
 --------------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.builder;

import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.payment.*;
import com.armani.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.swing.dlg.CreditCardsDlg;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cr.config.ConfigMgr;
import javax.swing.table.DefaultTableCellRenderer;


/**
 */
public class CreditCardBldr_EUR implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String accountNum = null;
  private String cardHolderName = null;
  private String expDate = null;
  private String serviceCode = null;
  private String rawData = null;
  private String trackNumber = null;
  private String cid = null;
  private CMSCreditCard thePayment;
  private boolean manual = false;
  private boolean digitVerify = false;
  private CMSMSR cmsMSR = null;
  private String zipCode = null;
  boolean zipTraversed = false;
  private PaymentTransaction aTxn = null;
  private double theAmount = 0.0d;
  private GenericChooseFromTableDlg overRideDlg;
  private String cardType = null;
  private String cardCode = null;
  private ArmCurrency amtDue;
  private String startDate = null;
  private String issueNumber = null;
  private Payment debitCard;
  private String paymentType = null;
  private String paymentDesc = null;

  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

  /**
   */
  public CreditCardBldr_EUR() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
  }

  /**
   */
  public void cleanup() {
    //        cmsMSR.release();
  }

  /**
   *Display Credit Card options
   */
  private void displayCreditCardTypes() {
    List paymentCCodes = CMSPaymentMgr.getPaymentCode("CREDIT_CARD");
    List paymentDCodes = CMSPaymentMgr.getPaymentCode("DEBIT_CARD");
    int numPaymentCCodes = paymentCCodes.size();
    int numPaymentDCodes = paymentDCodes.size();
    int numPaymentCodes = numPaymentCCodes + numPaymentDCodes;

    ArrayList aPaymentCodes = new ArrayList(numPaymentCodes);
    aPaymentCodes.addAll(paymentCCodes);
    //Fix for Defect # 1348
    Collections.sort(aPaymentCodes, new CreditCardCodeComparator());
    
    aPaymentCodes.addAll(paymentDCodes);
    CMSPaymentCode[] paymentCodes =(CMSPaymentCode[])aPaymentCodes.toArray(new CMSPaymentCode[0]);
    GenericChooserRow[] availPaymentCodes = new GenericChooserRow[numPaymentCodes];
    if (numPaymentCodes>0){
        for (int i = 0; i < numPaymentCodes; i++) {
            availPaymentCodes[i] = new GenericChooserRow(new String[] {
                    paymentCodes[i].getPaymentDesc()}, paymentCodes[i]);
        }
    }
    overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr
        , availPaymentCodes, new String[] {"Payment Desc"
    });
  }

  /**
   * put your documentation comment here
   */
  private void getSelectedType() {
    overRideDlg.setVisible(true);
    if (overRideDlg.isOK()) {
      CMSPaymentCode payCode = (CMSPaymentCode)overRideDlg.getSelectedRow().getRowKeyData();
      cardCode = payCode.getPaymentCode();
      paymentType = payCode.getPaymentType();
      paymentDesc = payCode.getPaymentDesc();
    }
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("CREDIT_CARD")) {
      if (theEvent == null || theEvent.toString().length() == 0) {
        manual = true;
        //display credit card picklist
        displayCreditCardTypes();
        getSelectedType();
      }
    }
    if (theCommand.equals("ACCOUNT")) {
      accountNum = (String)theEvent;
      if (paymentType.equalsIgnoreCase("CREDIT_CARD")) {
        thePayment = new CMSCreditCard(ICMSPaymentConstants.CREDIT_CARD);
        thePayment.setGUIPaymentName(paymentDesc);
		//vishal Yevale : added mask in creditcard number : 17 may 2017
        thePayment.setAccountNumber(addMask(accountNum));
        thePayment.setAmount(new ArmCurrency(theAmount));
        thePayment.setPaymentCode(cardCode);
        thePayment.setMessageIdentifier("CC");
        thePayment.setMessageType("0");
        thePayment.setTenderType("03");
        thePayment.setManuallyKeyed(true);
      } else if (paymentType.equalsIgnoreCase("DEBIT_CARD")) {
        debitCard = new CMSDebitCard(ICMSPaymentConstants.DEBIT_CARD);
        ((CreditCard)debitCard).setGUIPaymentName(paymentDesc);
		// ((CreditCard)debitCard).setAccountNumber(accountNum);  //added mask for debit card as well you can uncomment if not required
		//vishal Yevale :added mask in debitcard number :17 may 2017: if not required mask for debit - delete below line
        ((CreditCard)debitCard).setAccountNumber(addMask(accountNum));
        ((CreditCard)debitCard).setAmount(new ArmCurrency(theAmount));
        ((CreditCard)debitCard).setPaymentCode(cardCode);
        ((CreditCard)debitCard).setMessageIdentifier("CC");
        ((CreditCard)debitCard).setMessageType("0");
        ((CreditCard)debitCard).setTenderType("03");
        ((CreditCard)debitCard).setManuallyKeyed(true);
      }
      manual = true;
      digitVerify = true;
    }
    if (theCommand.equals("AMOUNT")) {
      try {

        if (TransactionUtil.validateChangeAmount(theAppMgr, thePayment.getTransactionPaymentName(), thePayment.getGUIPaymentName()
            , (ArmCurrency)theEvent)) {
//        if (validateChangeAmount((ArmCurrency)theEvent)) { // remove, this is checked in a business rule
          theAmount = ((ArmCurrency)theEvent).doubleValue();
        } else {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
              , amtDue.absoluteValue(), theAppMgr.CURRENCY_MASK);
        }
        //invoke the call the armani's api
      } catch (Exception ex) {
        ex.printStackTrace();
        theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe credit card, press K for keyed entry or press Enter to skip entry")
            , "CREDIT_CARD");
        theAppMgr.setEditAreaFocus();
      }
    }
    if (theCommand.equals("DATE")) {
      // Expiration date on the magstripe is coded YYMM.  Jay wanted the manual entry
      // of an expiration date to come in MMYY.  So we have to reverse the month
      // and year here for the getCalendar() method.
      String testExpDate = (String)theEvent;
      int dateLength=testExpDate.length();
      if (dateLength==0){
        expDate="";
      }
      else if (4 == testExpDate.length()) {
        StringBuffer buf = new StringBuffer(testExpDate.substring(2));
        buf.append(testExpDate.substring(0, 2));
        testExpDate = buf.toString();
        Calendar cal = CreditCardBldrUtil.getCalendar(testExpDate);
        if (cal == null) {} else {
          if (CreditCardBldrUtil.validateDate(cal)) {
            expDate = (String)theEvent;
            CreditCard cc = null;
            if (paymentType.equalsIgnoreCase("CREDIT_CARD")) {
              cc = (CreditCard)thePayment;
            } else if (paymentType.equalsIgnoreCase("DEBIT_CARD")) {
              cc = (CreditCard)debitCard;
            }
            Date dt = cal.getTime();
            cc.setExpirationDate(dt);
          } else {
            theAppMgr.showErrorDlg(applet.res.getString("This card has passed its expiration date."));
          }
        }
      } else {
        theAppMgr.showErrorDlg(applet.res.getString(
            "The expiration date must be formatted as MMYY."));
      }
    }
    /*
     if (theCommand.equals("STARTDATE")) {
     if (theEvent == null || theEvent.toString().length() == 0) {
     startDate = "";
     } else {
     String testExpDate = (String) theEvent;
     if (4 == testExpDate.length()) {
     StringBuffer buf = new StringBuffer(testExpDate.substring(2));
     buf.append(testExpDate.substring(0, 2));
     testExpDate = buf.toString();
     Calendar cal = CreditCardBldrUtil.getCalendar(testExpDate);
     if (cal == null) {
     } else {
     Calendar today = Calendar.getInstance();
     if (today.before(cal) == false) {
     startDate = (String) theEvent;
     Date dt = cal.getTime();
     ((CreditCard) thePayment).setStartDate(dt);
     } else {
     theAppMgr.showErrorDlg(applet.res.getString(
     "This card has start date in the future."));
     }
     }
     } else {
     theAppMgr.showErrorDlg(applet.res.getString(
     "The start date must be formatted as MMYY."));
     }
     }
     }
     */
    if (theCommand.equals("ISSUENUMBER")) {
      if (theEvent == null || theEvent.toString().length() == 0) {
        issueNumber = "";
      } else {
        try {
          String temp = (String)theEvent;
          Integer.parseInt(temp);
          ((CMSDebitCard)debitCard).setIssueNumber(temp);
          issueNumber = temp;
        } catch (NumberFormatException ex) {
          theAppMgr.showErrorDlg(applet.res.getString("Issue Number is numeric only."));
          issueNumber = null;
        }
      }
    }
    if (theCommand.equals("OVERRIDE")) {
      CreditCard cc = null;
      if (paymentType.equalsIgnoreCase("CREDIT_CARD")) {
        cc = (CreditCard)thePayment;
      } else if (paymentType.equalsIgnoreCase("DEBIT_CARD")) {
        cc = (CreditCard)debitCard;
      }
      cc.setManualOverride((String)theEvent);
    }
    if (paymentType.equalsIgnoreCase("CREDIT_CARD")) {
      completeAttributes();
    } else if (paymentType.equalsIgnoreCase("DEBIT_CARD")) {
      completeDebitCardAttributes();
    } else {
      completeAttributes();
    }
  }

//vishal Yevale : added mask in creditcard number : 17 may 2017
  public String addMask(String accountNumber){
	  if(accountNumber==null || accountNumber.contains("*") || accountNumber.length()==1){
		  return accountNumber;
	  }
	  if(accountNumber.length()>=10){
		  String firstFour=accountNumber.substring(0, 4);
		  String lastEightorSix="";
		  if(10!=accountNumber.length()-1){
		   lastEightorSix=accountNumber.substring(10, accountNumber.length());
		  }
		  accountNumber=firstFour+"******"+lastEightorSix;
		  return accountNumber;
	  }else{
		  int legnth=accountNumber.length();
		  int qut = legnth/2;
		  String star="";
		  for(int i=0;i<qut;i++){
			  star=star+"*";
		  }
		  int starLen = accountNumber.length()-star.length();
		  if(0!=starLen-1){
		  accountNumber = accountNumber.substring(0,starLen)+star;
		  }else{
			  accountNumber = ""+accountNumber.charAt(0)+star;
		  }
		  return accountNumber;
	  }
  } // end vishal yevale
  /**
   * @param amt
   * @return
   */
  private boolean validateChangeAmount(ArmCurrency amt) {
    try {
      PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
      CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
          CMSAppModelFactory.getInstance(), theAppMgr);
      ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue().subtract(appModel.
          getTotalPaymentAmount());
      // if (amtLeft.greaterThanOrEqualTo(new ArmCurrency(0.0)))
      //    return true;
      if (amt.greaterThan(amtLeft.absoluteValue())) {
        theAppMgr.showErrorDlg(applet.res.getString("You can not give more change than is due."));
        return (false);
      } else {
        return (true);
      }
    } catch (Exception ex) {
      return (true);
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
    if (initValue != null)
      theAmount = ((ArmCurrency)initValue).doubleValue();
    else
      theAmount = 0.0d;
    aTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    theAppMgr.setSingleEditArea("Select credit/debit card type ....");
    displayCreditCardTypes();
    getSelectedType();
    if (paymentType.equalsIgnoreCase("CREDIT_CARD")) {
      completeAttributes();
    } else if (paymentType.equalsIgnoreCase("DEBIT_CARD")) {
      completeDebitCardAttributes();
    } else {
      completeAttributes();
    }
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    // didn't swipe, get account number for payment type
    if (thePayment == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return (false);
    }
    if (expDate == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter expiration date.  MMYY"), "DATE");
      return (false);
    }
    if (aTxn.getHandWrittenTicketNumber().length() > 0 && thePayment.isAuthRequired()) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter authorization number."), "OVERRIDE"
          , theAppMgr.REQUIRED_MASK);
      return (false);
    }
    if (manual) {
      ((CreditCard)thePayment).setManuallyKeyed(true);
      // the following dlg boxed caused the edit area not to take any
      // additional key stroks.. jrg
      theAppMgr.showErrorDlg(applet.res.getString("Don't forget to get an imprint of the customer's card!"));
    } 
    theBldrMgr.processObject(applet, "PAYMENT", thePayment, this);
    return (true);
  }

  /**
   * @return
   */
  private boolean completeDebitCardAttributes() {
    // didn't swipe, get account number for payment type
    if (cardCode == null) {
      //display credit card picklist
      displayCreditCardTypes();
      getSelectedType();
    }
    if (debitCard == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT"
          , theAppMgr.REQUIRED_MASK);
      return (false);
    }
    if (expDate == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter expiration date.  MMYY"), "DATE");
      return (false);
    }
    if (issueNumber == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter issue number."), "ISSUENUMBER");
      return (false);
    }
    if (aTxn.getHandWrittenTicketNumber().length() > 0 && debitCard.isAuthRequired()) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter authorization number."), "OVERRIDE"
          , theAppMgr.REQUIRED_MASK);
      return (false);
    }
    if (debitCard.getAmount() == null) {
      // calculate remaining balance
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(appModel.
            getTotalPaymentAmount());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , theAppMgr.CURRENCY_MASK);
      }
      return (false);
    }
    if (manual) {
      ((CreditCard)debitCard).setManuallyKeyed(true);
      // the following dlg boxed caused the edit area not to take any
      // additional key stroks.. jrg
      theAppMgr.showErrorDlg(applet.res.getString(
          "Don't forget to get an imprint of the customer's card."));
    }
    theBldrMgr.processObject(applet, "PAYMENT", debitCard, this);
    return (true);
  }

  /**
   */
  private void resetAttributes() {
    debitCard = null;
    thePayment = null;
    manual = false;
    digitVerify = false;
    accountNum = null;
    cardHolderName = null;
    expDate = null;
    serviceCode = null;
    cid = null;
  }

  /**
   */
  private void populateCreditCard() {
    CreditCard cc = (CreditCard)thePayment;
    if (accountNum != null) {
      cc.setAccountNumber(addMask(accountNum));
    }
    if (cardHolderName != null) {
      cc.setCreditCardHolderName(cardHolderName);
    }
    if (expDate != null) {
      Calendar cal = CreditCardBldrUtil.getCalendar(expDate);
      if (cal == null) {
        theAppMgr.showErrorDlg(applet.res.getString("The expiration date is not valid."));
      } else {
        Date dt = cal.getTime();
        cc.setExpirationDate(dt);
      }
    }
    if (rawData != null) {
      cc.setTrackData(rawData);
    }
    if (trackNumber != null) {
      cc.setTrackNumber(trackNumber);
    }
  }

  /**
   * put your documentation comment here
   * @param accountNum
   */
  public void setAccountNum(String accountNum) {
    this.accountNum = accountNum;
  }

  /**
   * put your documentation comment here
   * @param cardHolderName
   */
  public void setCardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
  }

  /**
   * put your documentation comment here
   * @param mmyyExpDate
   */
  public void setExpDate(String mmyyExpDate) {
    this.expDate = mmyyExpDate;
  }

  /**
   * put your documentation comment here
   * @param serviceCode
   */
  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  /**
   * put your documentation comment here
   * @param rawData
   */
  public void setRawData(String rawData) {
    this.rawData = rawData;
  }

  /**
   * put your documentation comment here
   * @param trackNumber
   */
  public void setTrackNumber(String trackNumber) {
    this.trackNumber = trackNumber;
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
   * @param inputStr
   * @return
   */
  public boolean getCreditCardInfo(String inputStr) {
    if (this.cmsMSR instanceof NonJavaPOSMSR) {
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
        return (false);
      }
    }
    Calendar cal = CreditCardBldrUtil.getCalendar(expDate);
    if (cal == null) {
      theAppMgr.showErrorDlg(applet.res.getString("The expiration date is not valid."));
    }
    if (cal != null && !CreditCardBldrUtil.validateDate(cal)) {
      theAppMgr.showErrorDlg(applet.res.getString("This card has passed its expiration date."));
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
  
  private class CreditCardCodeComparator implements Comparator {
	  public int compare(Object obj1, Object obj2) {
		  String code1 = ((CMSPaymentCode) obj1).getPaymentCode();
	      String code2 = ((CMSPaymentCode) obj2).getPaymentCode();        
	      return code1.compareTo(code2);
	  }
  }
}

