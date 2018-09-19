/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
//import  com.chelseasystems.cr.payment.MailCheck;
import com.chelseasystems.cs.payment.MailCheck;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.swing.pos.PaymentApplet;


/**
 * The purpose of a "Bldr" class is to build a complex object, and pass it back
 * to the GUI.
 * The GUI provides an amount.  The due bill takes control and builds a due bill
 * suitable for posting on the back end.
 * @author Andrew Reed (based on code by John Gray)
 * @version 1.0a
 */
public class MailCheckBldr implements IObjectBuilder {
  private MailCheck dBill = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private PaymentTransaction theTxn;
  private CMSPaymentTransactionAppModel txn = null;

  /** Default ctor */
  public MailCheckBldr() {
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
  }

  /** Cleanup before exiting. */
  public void cleanup() {}

  /**
   * Try to find the due bill on the server, using the record id.
   * @param theCommand the description of what the user typed.
   * @param theEvent what the user typed.
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    theAppMgr.addStateObject("THE_EVENT", "SUCCESS");
    if (theCommand.equals("AMOUNT")) {
      ArmCurrency testAmt = (ArmCurrency)theEvent;
      try {
        // Make sure amount is in the acceptable range.
        if (testAmt.lessThanOrEqualTo(new ArmCurrency(0.0))) {
          theAppMgr.showErrorDlg(applet.res.getString("Amount must be greater than") + " "
              + applet.res.getString("zero") + ".");
          theBldrMgr.processObject(applet, "PAYMENT", null, this);
          dBill = null;
          return;
        }
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
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
      //Vivek Mishra : Added to validate actual tender amount in case of Return Override scenario
		if(PaymentApplet.OrgSaleTxn!=null){
			try{
			ArmCurrency Value = (ArmCurrency)theEvent;
			//Mayuri Edhara :: 05-16-2017 : Added absoluteValue to remove the negative value.
			ArmCurrency retAmt = PaymentApplet.retAmt.absoluteValue();
			PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
	        ArmCurrency amt = new ArmCurrency(0.0d);
	        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
	      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			if (((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
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
        dBill.setAmount((ArmCurrency)theEvent);
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
    dBill = new MailCheck((String)initValue);
    this.applet = applet;
    // register for call backs
    completeAttributes();
  }

  /**
   * Returns true if all questions have answers.
   * @return true if all questions have answers
   */
  private boolean completeAttributes() {
    if (dBill.getAmount() == null) {
      try {
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = new ArmCurrency(0.0d);
        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
  		if (((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
  				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
  			amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
  		}
  		//Ends
  		else
  			amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
  	    //Vivek Mishra : Added to show actual tender amount in case of Return Override scenario
  		//Mayuri Edhara :: 05-16-2017 : Added absoluteValue to remove the negative value.
  		if(PaymentApplet.OrgSaleTxn != null && amt.lessThan(new ArmCurrency(0.0d)))
			theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
		              , PaymentApplet.retAmt.absoluteValue(), theAppMgr.CURRENCY_MASK);
  		//Ends
  		else
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , theAppMgr.CURRENCY_MASK);
      }
      return false;
    }
    return true;
  }
}

