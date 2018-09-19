/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.armani.CMSCreditCard;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.util.CreditAuthUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.util.TransactionUtil;


/**
 */
public class CreditCardBldr_CA implements IObjectBuilder {
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
  boolean zipTraversed = false;
  //ruchi
   private IBrowserManager theMgr;
  private CMSCreditCard theCreditCardPayment;
  private CMSPaymentTransactionAppModel txn = null;
  
  /**
   */
  public CreditCardBldr_CA() {
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
  

 
  
  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String command, CMSApplet applet, Object initValue) {
    resetAttributes();
    this.applet = applet;
    CreditCard cc = (CreditCard)thePayment;
    
     theAppMgr.setSingleEditArea("Select option", null, -1); 
    this.setAccountNum("4444333322221111");
    this.setCardHolderName("Mobile");
    this.setExpDate("1220");
    this.setRawData("9999");
    accountNum="4444333322221111";
    thePayment = CreditCardBldrUtil.allocCreditCardPaymentforCanada(accountNum);
    if(thePayment!=null){
    	populateCreditCard(initValue.toString());
        completeAttributes();
        return;
      }     
    // the card is expired or unreadable
    theBldrMgr.processObject(applet, "PAYMENT", null, this);
    }
  
  

 

  /**
   * @return
   */
  private boolean completeAttributes() {
    // didn't swipe, get account number for payment type
	PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
	
   
    // manual override only if in re-entry mode
    PaymentTransaction aTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    if (thePayment.getAmount() == null) {
      // calculate remaining balance
      try {
         theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = new ArmCurrency(0.0d);
        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
			amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
		}
		//Ends
		else
			amt = appModel.getCompositeTotalAmountDue().subtract(appModel.
		            getTotalPaymentAmount());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , theAppMgr.CURRENCY_MASK);
      }
      return (false);
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
  private void populateCreditCard(String tenderType) {
	  String stringDate="1220";
	  SimpleDateFormat sdf = new SimpleDateFormat(stringDate);
	  CreditCard cc = (CreditCard)thePayment;
      cc.setAccountNumber("4444333322221111");
      cc.setCreditCardHolderName("Mobile");
      cc.setTenderType(tenderType);
      cc.setGUIPaymentName(tenderType);
      if(tenderType.equalsIgnoreCase("DEBIT")){
    	  cc.setTransactionPaymentName("DINERS");  
      }else
      cc.setTransactionPaymentName(tenderType);
      cc.setRespAuthorizationResponseCode("Mobile");
      cc.setLuNmbCrdSwpKy("Mobile");
      //Vivek Mishra : Added to avoid fatal exception in case of mobile terminal tenders
      cc.setRespStatusCode("0");
      cc.setAuthRequired(false);
      //Ends
      Payment p = (Payment)cc;
      p.setRespMessage("Mobile");
      try {
		cc.setExpirationDate(sdf.parse(stringDate));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	if (completeAttributes()) {
	      String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
	      String registerId = ((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId();
	      thePayment.setJournalKey(CreditAuthUtil.getJournalKey(storeId, registerId));
	      theBldrMgr.processObject(applet, "PAYMENT", thePayment, this);
	      thePayment = null;
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

  
public void cleanup() {
	// TODO Auto-generated method stub
	
}

public void EditAreaEvent(String theCommand, Object theEvent) {
	// TODO Auto-generated method stub
	if (theCommand.equals("AMOUNT")) {
	      //if (validateChangeAmount((ArmCurrency) theEvent)) // remove, this is checked in a business rule
	      String paymentTypeView = thePayment.getGUIPaymentName();
	      String paymentType = thePayment.getTransactionPaymentName();
	      if(paymentType.equalsIgnoreCase("DEBIT")){
	    	  thePayment.setTransactionPaymentName("DINERS");
	      }
	      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
	          , (ArmCurrency)theEvent))
	        thePayment.setAmount((ArmCurrency)theEvent);
	    }
	if (theCommand.equals("REFNUM")){
		thePayment.setRespMessage((String)theEvent);
	}
	
	if (completeAttributes()) {
	      String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
	      String registerId = ((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId();
	      thePayment.setJournalKey(CreditAuthUtil.getJournalKey(storeId, registerId));
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
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
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

}

