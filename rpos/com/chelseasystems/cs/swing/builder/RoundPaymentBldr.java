/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.payment.RoundPayment;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;
import com.chelseasystems.cs.util.TransactionUtil;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class RoundPaymentBldr implements IObjectBuilder {
  private Payment roundPayment = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  static ArmCurrency curAmount;

  /**
   */
  public RoundPaymentBldr() {
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
    if (theCommand.equals("ROUNDPAYMENT")) {
      // if change, only allow amount left as max
      if (TransactionUtil.validateChangeAmount(theAppMgr, roundPayment.getTransactionPaymentName(), roundPayment.getGUIPaymentName()
          , (ArmCurrency)theEvent)) {
        roundPayment.setAmount((ArmCurrency)theEvent);
        if (completeAttributes()) {
          theBldrMgr.processObject(applet, "PAYMENT", roundPayment, this);
          roundPayment = null;
        }
      } else {
        try {
          PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
          CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.
              getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
          ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
              getTotalPaymentAmount());
          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "ROUNDPAYMENT"
              , amtLeft.absoluteValue(), theAppMgr.CURRENCY_MASK);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    roundPayment = new RoundPayment(ICMSPaymentConstants.ROUND_PAYMENT);
    this.applet = applet;
    curAmount = new ArmCurrency(0.0);
    theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "ROUNDPAYMENT", initValue
        , theAppMgr.CURRENCY_MASK);
    // register for call backs
    completeAttributes();
  }


  /**
   * @return
   */
  private boolean completeAttributes() {
    if (roundPayment.getAmount() == null) {
      // calculate remaining balance
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
        // only default amount if change is due
        if (amt.lessThan(new ArmCurrency(0.0)))
          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "ROUNDPAYMENT"
              , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        else
          theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "ROUNDPAYMENT", amt
              , theAppMgr.CURRENCY_MASK);
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "ROUNDPAYMENT"
            , theAppMgr.CURRENCY_MASK);
      }
      return false;
    }
    //      if(theAppMgr.getStateObject("MAX_CHANGE") != null){
    //      ArmCurrency maxChange = (ArmCurrency)theAppMgr.getStateObject("MAX_CHANGE");
    //      try{
    //        curAmount = curAmount.add(cash.getAmount());
    //      } catch (CurrencyException e){
    //        e.printStackTrace();
    //      }
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
    //      }
    return true;
  }
}

