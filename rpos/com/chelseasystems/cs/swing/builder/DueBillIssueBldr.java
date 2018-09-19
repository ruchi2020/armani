/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.payment.DueBill;
import com.chelseasystems.cr.payment.DueBillIssue;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.swing.pos.PaymentApplet;
import com.chelseasystems.cs.ajbauthorization.AJBRequestResponseMessage;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.msr.CMSMSR;
import com.chelseasystems.cs.msr.NonJavaPOSMSR;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cr.payment.StoreValueCard;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;


/**
 * The purpose of a "Bldr" class is to build a complex object, and pass it back
 * to the GUI.
 * The GUI provides an amount.  The due bill takes control and builds a due bill
 * suitable for posting on the back end.
 * @author Andrew Reed (based on code by John Gray)
 * @version 1.0a
 */
public class DueBillIssueBldr implements IObjectBuilder {
  private DueBillIssue dBill = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private PaymentTransaction theTxn;
  private CMSMSR cmsMSR = null;
  //Start: Added by Himani for Credit Note fipay integration on 21-SEP-2016
  private static ConfigMgr config;
  private String fipay_gc_flag;
  //End: Added by Himani for Credit Note fipay integration on 21-SEP-2016
  
  public static ArmCurrency gcAmtTnd = new ArmCurrency(0.0d);

  /** Default ctor */
  public DueBillIssueBldr() {
  }

  /**
   * Initialize the environment.
   * @param theBldrMgr the builder manager
   * @param theAppMgr the application manager
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    this.accountNum = "";
    //Start: Added by Himani for credit note fipay integration on 21-SEP-2016
    String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_gc_flag = config.getString("FIPAY_GIFTCARD_INTEGRATION");
	if(fipay_gc_flag == null){
		fipay_gc_flag = "N";
	}
	//End: Added by Himani for credit note fipay integration on 21-SEP-2016
  }

  /** Cleanup before exiting. */
  public void cleanup() {
	  if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")) //Added by Himani for credit note fipay integration on 21-SEP-2016
	  {
		  if(cmsMSR!=null)
			  cmsMSR.release();
		  this.accountNum = "";
	  }
  }

  /**
   * Try to find the due bill on the server, using the record id.
   * @param theCommand the description of what the user typed.
   * @param theEvent what the user typed.
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
	  if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")) //Added by Himani for credit note fipay integration on 21-SEP-2016
		{ 
		  if (theCommand.equals("DUE_BILL_ISSUE_ID")) {
			  processSwipe((String)theEvent);
			  return;
		  }
		  if (theCommand.equals("MANUAL")) {
			  ((CMSDueBillIssue)dBill).setIsManual(true);
			  theAppMgr.setSingleEditArea(applet.res.getString("Enter Credit Note Id."), "ID"
					  , theAppMgr.UPPER_CASE_MASK);
			  return;
		  }
		  if (theCommand.equals("ID")) {
			  processID((String)theEvent);
		  }
		}
		  if (theCommand.equals("AMOUNT")) {
			 ArmCurrency testAmt = (ArmCurrency)theEvent;
			 try {
    	  
				 CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
						 CMSAppModelFactory.getInstance(), theAppMgr);
				 ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
				 if(PaymentApplet.OrgSaleTxn !=null && amt.lessThan(new ArmCurrency(0.0d))){
					 Payment[] payments = PaymentApplet.OrgSaleTxn .getPaymentsArray();
					  ArmCurrency amtGiftCard = new ArmCurrency(0.0d);
					  for(int i = 0 ;i<payments.length;i++ ){
						  if(payments[i] instanceof StoreValueCard){
							  amtGiftCard = amtGiftCard.add(payments[i].getAmount().absoluteValue());
						  }
					  }
		
		//if override button is clicked while returning , and cashier decides to issue gift card for that payment 
		//gcAmtTnd checks if a gift card is already issued in the current transaction , then allow the new amount for credit note as whatevr is amount  for the row selected
	if(PaymentApplet.retAmt.absoluteValue().greaterThan(new ArmCurrency(0.0d))){
		PaymentApplet.rownum = 1;
		amtGiftCard = amtGiftCard.add(PaymentApplet.retAmt.absoluteValue());
		if(gcAmtTnd.absoluteValue().greaterThan(new ArmCurrency(0.0d))){
			amtGiftCard = PaymentApplet.retAmt.absoluteValue();
		}
	}
	if((amtGiftCard.absoluteValue()).lessThan(amt.absoluteValue())){
		if(testAmt.lessThan(amtGiftCard) || testAmt.greaterThan(amtGiftCard)){
		 theAppMgr.showErrorDlg(applet.res.getString("Amount must be equal to") + " "
	              + amtGiftCard.absoluteValue().stringValue() + ".");
		 theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", amtGiftCard.absoluteValue(), theAppMgr.CURRENCY_MASK);
			//  theBldrMgr.processObject(applet, "PAYMENT", null, this);
	       
	          return;
	}}
	//Vivek Mishra : Added to restrict credit note issue amount ot 2500$ 20-DEC-2016
	if(fipay_gc_flag!=null && fipay_gc_flag.toUpperCase().equals("Y"))
    {
     boolean isValidAmt=validateAmount((ArmCurrency)theEvent);
    	    	  if(isValidAmt==false)
    	    	  {
    	    		  theAppMgr.showErrorDlg(applet.res.getString("Amount cannot be greater than $"+ (2500)));  
    	    		  theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", amtGiftCard.absoluteValue(), theAppMgr.CURRENCY_MASK);
    	    		  return;
    	    	  }

    }
	//Ends here 20-DEC-2016
	
	if((amtGiftCard.absoluteValue()).greaterThanOrEqualTo(amt.absoluteValue())){
		if(testAmt.lessThan(amt.absoluteValue()) || testAmt.greaterThan(amt.absoluteValue()) ){
		 theAppMgr.showErrorDlg(applet.res.getString("Amount must be equal to") + " "
	              + amt.absoluteValue().stringValue() + ".");
		 theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
		 //   theBldrMgr.processObject(applet, "PAYMENT", null, this);
	       
	          return;
	}}
   } 
   
   else{   // Make sure amount is in the acceptable range.
        if (testAmt.lessThanOrEqualTo(new ArmCurrency(0.0))) {
          theAppMgr.showErrorDlg(applet.res.getString("Amount must be greater than") + " "
              + applet.res.getString("zero") + ".");
          theBldrMgr.processObject(applet, "PAYMENT", null, this);
          dBill = null;
          return;
        }
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        
        ArmCurrency txnBalanceDue = appModel.getCompositeTotalAmountDue();
        txnBalanceDue = txnBalanceDue.subtract(theTxn.getTotalPaymentAmount());
        txnBalanceDue = txnBalanceDue.absoluteValue();
        if (testAmt.greaterThan(txnBalanceDue)) {
          theAppMgr.showErrorDlg(applet.res.getString("Amount must not be greater than") + " "
              + txnBalanceDue.stringValue() + ".");
          theBldrMgr.processObject(applet, "PAYMENT", null, this);
          dBill = null;
          return;
        }
        
      //Vivek Mishra : Added to restrict credit note issue amount ot 2500$ 20-DEC-2016
    	if(fipay_gc_flag!=null && fipay_gc_flag.toUpperCase().equals("Y"))
        {
         boolean isValidAmt=validateAmount((ArmCurrency)theEvent);
        	    	  if(isValidAmt==false)
        	    	  {
        	    		  theAppMgr.showErrorDlg(applet.res.getString("Amount cannot be greater than $"+ (2500)));  
        	    		  theBldrMgr.processObject(applet, "PAYMENT", null, this);
        	              dBill = null;
        	              return;
        	    	  }

        }
    	//Ends here 20-DEC-2016
   }
		//Mayuri Edhara :: added the setAuthRequired for payment objects		 
		 if(fipay_gc_flag!=null && fipay_gc_flag.toUpperCase().equals("Y"))
	     {		 
			 if(dBill instanceof CMSDueBillIssue){
				 ((CMSDueBillIssue) dBill).setAuthRequired(true);
			 }
	     }
        dBill.setAmount((ArmCurrency)theEvent);
        gcAmtTnd = (ArmCurrency)theEvent;
      } catch (Exception e) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "EditAreaEvent"
            , "Currency conflict", "Make sure currencies match.", LoggingServices.MAJOR, e);
        // Send a null back (to get out of this class).
        theBldrMgr.processObject(applet, "PAYMENT", null, this);
        return;
      }
    }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "PAYMENT", dBill, this);
      dBill = null;
    }
  }

  /**
   * Creates a new due bill to populate.
   * @param applet the applet calling the builder
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
	      dBill = new CMSDueBillIssue((String)initValue);
    this.applet = applet;
    if(fipay_gc_flag!=null && fipay_gc_flag.toUpperCase().equals("Y")) //Added by Himani for credit note fipay integration on 21-SEP-2016
    {
    	//theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", theAppMgr.CURRENCY_MASK);
    	try {
            //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        	  CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
        	            CMSAppModelFactory.getInstance(), theAppMgr);
        	        ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
           	if(PaymentApplet.OrgSaleTxn !=null && amt.lessThan(new ArmCurrency(0.0d))){
            Payment[] payments = PaymentApplet.OrgSaleTxn .getPaymentsArray();
            ArmCurrency amtGiftCard = new ArmCurrency(0.0d);
    		for(int i = 0 ;i<payments.length;i++ ){
    			if(payments[i] instanceof StoreValueCard){
    				  amtGiftCard = amtGiftCard.add(payments[i].getAmount().absoluteValue());
    			}
    		}
    		
    		if(PaymentApplet.retAmt.absoluteValue().greaterThan(new ArmCurrency(0.0d))){
    			PaymentApplet.rownum = 1;
    			amtGiftCard = amtGiftCard.add(PaymentApplet.retAmt.absoluteValue());
    			if(gcAmtTnd.absoluteValue().greaterThan(new ArmCurrency(0.0d))){
    				amtGiftCard = PaymentApplet.retAmt.absoluteValue();
    			}
    		}
    	if((amtGiftCard.absoluteValue()).lessThan(amt.absoluteValue())){
    		theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", amtGiftCard.absoluteValue(), theAppMgr.CURRENCY_MASK);
    	}else{
    	theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
    	}
    	
    		}
        	else{
    	 theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
                , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
          }
         }catch (Exception ex) {
            theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
                , theAppMgr.CURRENCY_MASK);
          }
    }
    else
    {
    theAppMgr.setSingleEditArea(applet.res.getString(
        "Swipe Credit Note or press 'Enter' for manual entry."), "DUE_BILL_ISSUE_ID","");
    }
    // register for call backs
    //completeAttributes();
    theAppMgr.setEditAreaFocus();
    System.out.println("credit card builder getting instance of CMSMSR...");
    CMSMSR cmsMSR = CMSMSR.getInstance();
    cmsMSR.registerCreditCardBuilder(this);
    cmsMSR.activate();
	  //}
  }

  /**
   * Returns true if all questions have answers.
   * @return true if all questions have answers
   */
  private boolean completeAttributes() {
	if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")){//Added by Himani for credit note fipay integration on 21-SEP-2016
    String noteType = CMSPaymentMgr.getCreditNoteType("CREDIT_MEMO_ISSUE");
    ((CMSDueBillIssue)dBill).setType(noteType);
    if (((CMSDueBillIssue)dBill).getControlNum() == null
        || ((CMSDueBillIssue)dBill).getControlNum().equals("")) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter Credit Note Id."), "ID"
          , theAppMgr.UPPER_CASE_MASK);
      //         theAppMgr.setEditAreaFocus();
      //       System.out.println("credit card builder getting instance of CMSMSR...");
      //       CMSMSR cmsMSR = CMSMSR.getInstance();
      ////              cmsMSR.registerCreditCardBuilder(this);
      ////              cmsMSR.activate();
      return false;
    }
	}
    if (dBill.getAmount() == null) {
      // calculate remaining balance
      try {
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
    	  CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
    	            CMSAppModelFactory.getInstance(), theAppMgr);
    	        ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
       	if(PaymentApplet.OrgSaleTxn !=null && amt.lessThan(new ArmCurrency(0.0d))){
        Payment[] payments = PaymentApplet.OrgSaleTxn .getPaymentsArray();
        ArmCurrency amtGiftCard = new ArmCurrency(0.0d);
		for(int i = 0 ;i<payments.length;i++ ){
			if(payments[i] instanceof StoreValueCard){
				  amtGiftCard = amtGiftCard.add(payments[i].getAmount().absoluteValue());
			}
		}
		
		if(PaymentApplet.retAmt.absoluteValue().greaterThan(new ArmCurrency(0.0d))){
			PaymentApplet.rownum = 1;
			amtGiftCard = amtGiftCard.add(PaymentApplet.retAmt.absoluteValue());
			if(gcAmtTnd.absoluteValue().greaterThan(new ArmCurrency(0.0d))){
				amtGiftCard = PaymentApplet.retAmt.absoluteValue();
			}
		}
	if((amtGiftCard.absoluteValue()).lessThan(amt.absoluteValue())){
		theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", amtGiftCard.absoluteValue(), theAppMgr.CURRENCY_MASK);
	}else{
	theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT", amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
	}
	
		}
    	else{
	 theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
      }
     }catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , theAppMgr.CURRENCY_MASK);
      }
      return false;
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   */
  private boolean processID(String id) {
    if (dBill != null) {
      String paymentName = ((CMSDueBillIssue)dBill).getTransactionPaymentName();
      try {
        IsDigit digitCheck = new IsDigit();
        if (!digitCheck.isDigit(id)) {
          theAppMgr.showErrorDlg(applet.res.getString("Invalid Card ID."));
          return false;
        }
        boolean success = RedeemableBldrUtil.validateEnteredCard("CREDIT_MEMO_ISSUE", id);
        //prompt for amount
        if (!success) {
          theAppMgr.showErrorDlg(applet.res.getString("Invalid Card ID."));
          return false;
        }
        //VM: Check to see if the card is already being used in this Txn as a Gift card
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        if (theTxn != null && theTxn instanceof CompositePOSTransaction) {
          POSLineItem[] lines = ((CompositePOSTransaction)theTxn).getSaleLineItemsArray();
          for (int i = 0; i < lines.length; i++) {
            POSLineItemDetail[] details = lines[i].getLineItemDetailsArray();
            for (int j = 0; j < details.length; j++) {
              String giftId = details[j].getGiftCertificateId();
              if (giftId != null && giftId.length() > 0 && giftId.trim().equals(id.trim())) {
                theAppMgr.showErrorDlg(applet.res.getString(
                    "This card has already been added to the transaction as a gift card."));
                return false;
              }
            }
          }
        }
        if (theAppMgr.isOnLine()) {
          Redeemable received = CMSRedeemableHelper.findRedeemable(theAppMgr, id);
          if (received != null) {
            if (received instanceof DueBillIssue)
              theAppMgr.showErrorDlg(applet.res.getString("This is an already issued Credit Note."));
            else if (received instanceof StoreValueCard)
              theAppMgr.showErrorDlg(applet.res.getString("This is an already issued Gift Card."));
            else
              theAppMgr.showErrorDlg(applet.res.getString("This is an already issued Card."));
            return false;
          }
        }
        //else
        //{
        ((CMSDueBillIssue)dBill).doSetControlNum(id);
        ((CMSDueBillIssue)dBill).setId(id);
        //}
      } catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }
    theAppMgr.addStateObject("THE_EVENT", "SUCCESS");
    return true;
  }

  /**
   * @param input
   */
  public void processSwipe(String input) {
    if ((input == null || input.trim().length() == 0)
        //|| processID(accountNum))
        || (getDueBillIssueInfo(input) && processID(accountNum))) {
      completeAttributes();
      return;
    }
    // the card is expired or unreadable
    theBldrMgr.processObject(applet, "PAYMENT", null, this);
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getDueBillIssueInfo(String inputStr) {
    if (this.cmsMSR instanceof NonJavaPOSMSR)
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr)))
        return (false);
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
  }

  private String accountNum = "";
  //Vivek Mishra : Added to restrict credit note issue amount ot 2500$ 20-DEC-2016
  private boolean validateAmount(ArmCurrency amount)
  {
	  if(((amount.doubleValue())) <= 2500)
		  return true;
	  else
		  return false;
  }
  //Ends 20-DEC-2016
}

