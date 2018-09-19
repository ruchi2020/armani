/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.Cash;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.swing.pos.PaymentApplet;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 */
public class CashBldr implements IObjectBuilder {
  private Payment cash = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  static ArmCurrency curAmount;
  private CMSPaymentTransactionAppModel txn = null;

  /**
   */
  public CashBldr() {
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
  public void cleanup() {}

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("CASH")) {
      // if change, only allow amount left as max
      //if (validateChangeAmount((ArmCurrency)theEvent)){
      //cash.setAmount((ArmCurrency) theEvent);
      String paymentTypeView = cash.getGUIPaymentName();
      String paymentType = cash.getTransactionPaymentName();
    //Vivek Mishra : Added to show actual tender amount in case of Return Override scenario
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
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
          , (ArmCurrency)theEvent))
        cash.setAmount((ArmCurrency)theEvent);
      if (completeAttributes()) {
        theBldrMgr.processObject(applet, "PAYMENT", cash, this);
        cash = null;
      }
    } else {
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amtLeft = new ArmCurrency(0.0d);
        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
  		if (((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
  				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
  			amtLeft = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
  		}
  		//Ends
  		else
            amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
            getTotalPaymentAmount());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "CASH"
            , amtLeft.absoluteValue(), theAppMgr.CURRENCY_MASK);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    cash = new Cash((String)initValue);
    this.applet = applet;
    curAmount = new ArmCurrency(0.0);
    theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "CASH", initValue
        , theAppMgr.CURRENCY_MASK);
    // register for call backs
    completeAttributes();
  }

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
      ArmCurrency amtLeft = new ArmCurrency(0.0d);
      txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
    //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
		if (((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
			amtLeft = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
		}
		//Ends
		else
            amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
          getTotalPaymentAmount());
      if (amtLeft.greaterThanOrEqualTo(new ArmCurrency(0.0)))
        return true;
      if (amt.greaterThan(amtLeft.absoluteValue())) {
        theAppMgr.showErrorDlg(applet.res.getString("Change cannot be more than") + " "
            + amtLeft.formattedStringValue() + ".");
        return false;
      } else
        return true;
    } catch (Exception ex) {
      return true;
    }
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    if (cash.getAmount() == null) {
      // calculate remaining balance
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        ArmCurrency amt = new ArmCurrency(0.0d);
        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
  		if ((txn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
  				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
  			amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
  		}
  		//Ends
  		else
            amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
        // only default amount if change is due
  	//Vivek Mishra : Added to validate actual tender amount in case of Return Override scenario
  	// Mayuri Edhara : Altered the text in Edit Area as below From Enter Amount.
		if(PaymentApplet.OrgSaleTxn != null && amt.lessThan(new ArmCurrency(0.0d)))
			theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "CASH"
		              , PaymentApplet.retAmt, theAppMgr.CURRENCY_MASK);
		//Ends
		else if (amt.lessThan(new ArmCurrency(0.0)))
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Cash amount."), "CASH"
              , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        else
          theAppMgr.setSingleEditArea(applet.res.getString("Enter Cash amount."), "CASH", amt
              , theAppMgr.CURRENCY_MASK);
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter Cash amount."), "CASH"
            , theAppMgr.CURRENCY_MASK);
      }
      return false;
    }
    if (theAppMgr.getStateObject("MAX_CHANGE") != null) {
      ArmCurrency maxChange = (ArmCurrency)theAppMgr.getStateObject("MAX_CHANGE");
      try {
        curAmount = curAmount.add(cash.getAmount());
      } catch (CurrencyException e) {
        e.printStackTrace();
      }
      //Currency maxChange = (ArmCurrency)cashChange.getAmount();
      /*try{
       if(cash.getAmount().greaterThan(maxChange)){
       theAppMgr.showErrorDlg("Change returned cannot be more than "+ maxChange.formattedStringValue() + " allowed for this tender");
       theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "CASH",theAppMgr.CURRENCY_MASK);
       theAppMgr.removeStateObject("MAX_CHANGE");
       return false;
       } else return true;
       }catch(Exception e){
       e.printStackTrace();
       return false;
       }*/
    }
    return true;
  }
}

