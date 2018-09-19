/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.swing.pos.PaymentApplet;
//import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.util.CreditAuthUtil;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;;


/**
 */
public class CreditCardBldr implements IObjectBuilder {
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
  private Payment thePayment;
  private boolean manual = false;
  private boolean digitVerify = false;
 // private CMSMSR cmsMSR = null;
  private String zipCode = null;
  boolean zipTraversed = false;
  private CMSPaymentTransactionAppModel txn = null;
  private static ConfigMgr config;
  private String fipay_flag;

  /**
   */
  public CreditCardBldr() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_flag = config.getString("FIPAY_Integration");
  }

  /**
   */
  public void cleanup() {
	  //Commenting for PCR Compliance
   // cmsMSR.release();
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("CREDIT_CARD")) {
    	//Anjana commenting to make PCI compliant and added a new mthod
    	processCard((String)theEvent);
      // processSwipe((String)theEvent);
      return;
    }
    if (theCommand.equals("ACCOUNT")) {
      accountNum = (String)theEvent;
     //sending card number to Fipay  
     thePayment = CreditCardBldrUtil.allocCreditCardObject(accountNum);
      // if payment is still null (manual account is bad then
      // return null and start over
      if (thePayment == null) {
        theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
        theBldrMgr.processObject(applet, "PAYMENT", null, this);
        return;
      }
      CreditCard cc = (CreditCard)thePayment;
      cc.setAccountNumber(accountNum);
      manual = true;
      digitVerify = true;
    }
    if (theCommand.equals("ZIPCODE")) {
      if (((String)theEvent != null) && (!((String)theEvent).equals(""))) {
        if (((String)theEvent).length() != 5) {
          theAppMgr.showErrorDlg(applet.res.getString("The zipcode should be 5 digit string"));
          return;
        }
        try {
          new Long(((String)theEvent));
        } catch (Exception e) {
          // Parsing error .. characters present.
          theAppMgr.showErrorDlg(applet.res.getString("The zipcode should be 5 digit string"));
          return;
        }
      }
      CreditCard cc = (CreditCard)thePayment;
      cc.setZipCode((String)theEvent);
      zipCode = (String)theEvent;
      zipTraversed = true;
    }
    // verify last four digits
    if (theCommand.equals("LAST_FOUR")) {
      if (((String)theEvent).trim().length() != 4) {
        theAppMgr.showErrorDlg(applet.res.getString("Enter last 4 digits of account number."));
        return;
      }
      if (accountNum.endsWith(((String)theEvent).trim())) {
        digitVerify = true;
      } else {
        // kick out if last 4 digits not correct
        theAppMgr.showErrorDlg(applet.res.getString(
            "The last four digits did not match those on the card."));
        //theBldrMgr.processObject(applet, "PAYMENT", null, this);
        return;
      }
    }
    if (theCommand.equals("CARDHOLDER_NAME")) {
      cardHolderName = ((String)theEvent).trim();
      CreditCard cc = (CreditCard)thePayment;
      cc.setCreditCardHolderName(cardHolderName);
    }
    if (theCommand.equals("AMOUNT")) {
    	
    	 //Default value of the flag is Y if its not present in credit_auth.cfg
		if (fipay_flag == null) {
			fipay_flag = "Y";
		}
		
	 if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("N")){
			theAppMgr.showErrorDlg(applet.res.getString("Please use22 Mobile Terminal for credit card payments"));
			return;
		}
    	
      //if (validateChangeAmount((ArmCurrency) theEvent)) // remove, this is checked in a business rule
      String paymentTypeView = thePayment.getGUIPaymentName();
      String paymentType = thePayment.getTransactionPaymentName();
    //Vivek Mishra : Added to validate actual tender amount in case of Return Override scenario
		if(PaymentApplet.OrgSaleTxn!=null){
			try{
			ArmCurrency Value = (ArmCurrency)theEvent;
			ArmCurrency retAmt = PaymentApplet.retAmt;
			PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
	        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
	        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
	            CMSAppModelFactory.getInstance(), theAppMgr);
	        ArmCurrency amt = new ArmCurrency(0.0d);
	        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
	      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
	        if ((txn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
	  				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
	  			amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
			}
			//Ends
			else
				amt = appModel.getCompositeTotalAmountDue().subtract(appModel.
	            getTotalPaymentAmount());
			if(amt.lessThan(new ArmCurrency(0.0))){

		if(Value.greaterThan(retAmt)){
			theAppMgr.showErrorDlg(applet.res.getString("Amount must be equal to "+retAmt.absoluteValue().stringValue()+"."));
			theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
					, retAmt, theAppMgr.CURRENCY_MASK);	
		return;
		}
		if(Value.lessThan(retAmt)){
			theAppMgr.showErrorDlg(applet.res.getString("Amount must be equal to "+retAmt.absoluteValue().stringValue()+"."));
			theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
					, retAmt, theAppMgr.CURRENCY_MASK);	
			return;
		}
			} 
		}catch(Exception e)
		{
			e.printStackTrace();
		}
			}
		//Ends
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
          , (ArmCurrency)theEvent))
        thePayment.setAmount((ArmCurrency)theEvent);
    }
    if (theCommand.equals("DATE")) {
      // Expiration date on the magstripe is coded YYMM.  Jay wanted the manual entry
      // of an expiration date to come in MMYY.  So we have to reverse the month
      // and year here for the getCalendar() method.
      String testExpDate = (String)theEvent;
      if (4 == testExpDate.length()) {
        StringBuffer buf = new StringBuffer(testExpDate.substring(2));
        buf.append(testExpDate.substring(0, 2));
        testExpDate = buf.toString();
        Calendar cal = CreditCardBldrUtil.getCalendar(testExpDate);
        if (cal == null) {} else {
          if (CreditCardBldrUtil.validateDate(cal)) {
            expDate = (String)theEvent;
            CreditCard cc = (CreditCard)thePayment;
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
    if (theCommand.equals("OVERRIDE")) {
      CreditCard cc = (CreditCard)thePayment;
      cc.setManualOverride((String)theEvent);
    }
    if (theCommand.equals("CID")) {
      Amex amx = (Amex)thePayment;
      amx.setCidNumber((String)theEvent);
      cid = (String)theEvent;
    }
    if (completeAttributes()) {
    
      //Anjana commenting the journal key as we need to set the journal key feild with the IxInvoice number field used for AJB request
    //  thePayment.setJournalKey(CreditAuthUtil.getJournalKey(storeId, registerId));
      theBldrMgr.processObject(applet, "PAYMENT", thePayment, this);
      thePayment = null;
    }
  }

  /**
   * @param amt
   * @return
   */
  private boolean validateChangeAmount(ArmCurrency amt) {
    try {
      System.out.println("validateChangeAmount");
      PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
      CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
          CMSAppModelFactory.getInstance(), theAppMgr);
      ArmCurrency amtLeft = new ArmCurrency(0.0d);
      txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
    //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
      if ((txn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
			amtLeft = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
		}
		//Ends
		else
			amtLeft = appModel.getCompositeTotalAmountDue().subtract(appModel.
          getTotalPaymentAmount());
      // if (amtLeft.greaterThanOrEqualTo(new ArmCurrency(0.0)))
      //    return true;
      if (amt.greaterThan(amtLeft.absoluteValue())) {
        theAppMgr.showErrorDlg(applet.res.getString("You can not give more change than is due."));
        return (false);
      } else
        return (true);
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
    zipTraversed = false;
    //Comment this section for future Verifone integration
   /* theAppMgr.setSingleEditArea(applet.res.getString(
        "Enter amount"), "CREDIT_CARD"
        , IApplicationManager.CREDIT_CARD_MASK);*/
   //comment the below line when integrating with Verifone as we can't do manual entry 
 //  theAppMgr.setEditAreaFocus();
 //Added compelte attribute here to asak for enter amount as soon as tender starts
   completeAttributes();
    System.out.println("credit card builder getting instance of CMSMSR...");
   // CMSMSR cmsMSR = CMSMSR.getInstance();
    //cmsMSR.registerCreditCardBuilder(this);
    //cmsMSR.activate();
  }

  //Commenting process swipe for PCI compliance
  /**
   * @param input
   */
 /* public void processSwipe(String input) {
   if (input == null || input.length() == 0) {
      completeAttributes();
      return;
    }
    if (getCreditCardInfo(input)) { //... and exp date is valid
      thePayment = CreditCardBldrUtil.allocCreditCardPayment(accountNum);
      if (thePayment != null) {
        populateCreditCard();
        completeAttributes();
        return;
      } else {
        theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
      }
    }
    // the card is expired or unreadable
    theBldrMgr.processObject(applet, "PAYMENT", null, this);
  }*/
  
  /**
   * @param input
   */
  public void processCard(String input) {
   if (input == null || input.length() == 0) {
      completeAttributes();
      return;
    }
/*    if (getCreditCardInfo(input)) { //... and exp date is valid
      thePayment = CreditCardBldrUtil.allocCreditCardPayment(accountNum);
      if (thePayment != null) {
        populateCreditCard();
        completeAttributes();
        return;
      } else {
        theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
      }
    }*/
    // the card is expired or unreadable
    theBldrMgr.processObject(applet, "PAYMENT", null, this);
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    // didn't swipe, get account number for payment type
	  //Comment the below code and keep only what is needed like Enter amount for Verifone Integration
	  //We dont need this after we get Verifone
   /* if (thePayment == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return (false);
    }*/
	  //Vivek Mishra : Added condition to restrict new Payment Object in case of already existing object
	  if(thePayment==null)
	     thePayment =  new CreditCard(IPaymentConstants.CREDIT_CARD);
    //Commenting the below code as it is not needed while using Verifone device 
    
 /*   if ((manual) && !zipTraversed && (thePayment instanceof CreditCard)
        && (zipCode == null || zipCode.equals(""))) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter customer billing zip code")
          , "ZIPCODE");
      return false;
    }*/
    
    
/*    if ((!manual) && (!digitVerify)) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter last four digits."), "LAST_FOUR", "");
      return (false);
    }*/
    
/*    if ((manual) && (null == cardHolderName)) {
      theAppMgr.setSingleEditArea(applet.res.getString(
          "Enter card holder's name if different than customer, or press 'Enter'.")
          , "CARDHOLDER_NAME", theAppMgr.UPPER_CASE_MASK);
      return (false);
    }*/
    // manual override only if in re-entry mode
    PaymentTransaction aTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    //No need to get expiration date with Verifone device
 /*   if (expDate == null) {
      //TMW does not have expiration date, so fake one a year from now - djr
      //        if ((thePayment instanceof TMW)) {
      //          expDate = createExpirationDate();
      //          EditAreaEvent("DATE",expDate);
      //        }
      //        else {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter expiration date.  MMYY"), "DATE");
      return (false);
      //        }
    }*/
    if (aTxn.getHandWrittenTicketNumber().length() > 0 && thePayment.isAuthRequired()) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter authorization number."), "OVERRIDE"
          , theAppMgr.REQUIRED_MASK);
      return (false);
    }
    /*Commented for Armani
     if (thePayment != null & thePayment instanceof Amex) {
     if (cid == null) {
     theAppMgr.setSingleEditArea(applet.res.getString("Enter CID."), "CID");
     return (false);
     }
     }*/
    if (thePayment!=null && thePayment.getAmount() == null) {
      // calculate remaining balance
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = new ArmCurrency(0.0d);
        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
		if ((txn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
			amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
		}
		//Ends
		else
			amt = appModel.getCompositeTotalAmountDue().subtract(appModel.
            getTotalPaymentAmount());
		
        /*if(PaymentApplet.overrideReturn){
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
        	            ,  PaymentApplet.amtOverridden.absoluteValue(), theAppMgr.CURRENCY_MASK);
        }
        else{*/
		//Vivek Mishra : Added to show actual tender amount in case of Return Override scenario
		// Mayuri Edhara : Altered the text in Edit Area as below From Enter Amount.
		 if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("N")){
				return false;
		 }
		if(PaymentApplet.OrgSaleTxn != null && amt.lessThan(new ArmCurrency(0.0d)))
			theAppMgr.setSingleEditArea(applet.res.getString("Press Enter for CreditCard Payment"), "AMOUNT"
		            , PaymentApplet.retAmt, theAppMgr.CURRENCY_MASK);
		else //Ends
        theAppMgr.setSingleEditArea(applet.res.getString("Press Enter for CreditCard Payment"), "AMOUNT"
            , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        //} 
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Press Enter for CreditCard Payment"), "AMOUNT"
            , theAppMgr.CURRENCY_MASK);
      }
      return (false);
    }
    if (manual) {
      ((CreditCard)thePayment).setManuallyKeyed(true);
      // the following dlg boxed caused the edit area not to take any
      // additional key stroks.. jrg
      //armani US wants to change this message
      //Ruchi
      //theAppMgr.showErrorDlg(applet.res.getString(
          //"Don't forget to get an imprint of the customer's card."));
      theAppMgr.showErrorDlg(applet.res.getString(
              "Don't forget to get an imprint of the customer's credit card. Imprint must include item, details, subtotal, tax, total, date, authorization code and client signature."));
        //changes end      
    }
    return (true);
  }

  /**
   */
  private void resetAttributes() {
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
      cc.setAccountNumber(accountNum);
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
   */// Check later as to whether we need to change the MSR to verifone related 
  public boolean getCreditCardInfo(String inputStr) {
	  //MSR needs to stop to make it PCI compliance
    /*if (this.cmsMSR instanceof NonJavaPOSMSR)
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
    	  //Anjana  added the correct Swipe error message for Verifone device
          String errMessage = applet.res.getString("Swipe Error: Select 'Retry' to try again or 'Manual' to enter card information manually.");
    	   boolean swipeRetry = theAppMgr.showOptionDlg(applet.res.getString("Swipe Error")
    	            , errMessage, applet.res.getString("Retry"), applet.res.getString("Manual"));
    //Anjana Commenting the below error message 
    //    theAppMgr.showErrorDlg(applet.res.getString("Failure reading credit card data."));
    	   if (!swipeRetry) {
    		  //Waiting for the verifone device to be set up to see how it works when manual is selected 
    		  //Vivek Mishra : Added the manualKeyed flag set to true which is getting set when cashier enters the account number manually on POS currently. 
    		   ((CreditCard)thePayment).setManuallyKeyed(true);
      	        }
        return (false);
      }*/
	  //end changes
    Calendar cal = CreditCardBldrUtil.getCalendar(expDate);
    if (cal == null)
      theAppMgr.showErrorDlg(applet.res.getString("The expiration date is not valid."));
    if (cal != null && !CreditCardBldrUtil.validateDate(cal)) {
      theAppMgr.showErrorDlg(applet.res.getString("This card has passed its expiration date."));
      return (false);
    }
  //Vivek Mishra : Added the manualKeyed flag set to true which is getting set when cashier swipes the card successfully on POS currently. 
	   ((CreditCard)thePayment).setManuallyKeyed(false);
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
  // MSR code needs to be commented
 // public void setCMSMSR(CMSMSR cmsMSR) {
   // this.cmsMSR = cmsMSR;
  //}
}

