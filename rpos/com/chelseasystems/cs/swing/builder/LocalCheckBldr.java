/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.appmgr.*;

import com.chelseasystems.cr.currency.*;

import com.chelseasystems.cs.payment.LocalCheck;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cr.zipcode.ValidStates;


/**
 *  Creates a payment check object from a MICR read or a manual keyboard entry.
 *  @author Angela Tritter
 *  @version 1.0a
 */
public class LocalCheckBldr implements IObjectBuilder {
  private LocalCheck theCheck = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;

  /**
   */
  public LocalCheckBldr() {
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
      theCheck = null;
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    theCheck = null;
    theCheck = new LocalCheck(ICMSPaymentConstants.LOCAL_CHECK);
    this.applet = applet;
    completeAttributes();
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    // In re-entry mode, do not prompt for MICR.
    PaymentTransaction aTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    if (theCheck.getAmount() == null) {
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        return false;
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , theAppMgr.CURRENCY_MASK);
        return false;
      }
    }
    return true;
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("AMOUNT"))
    {
      String paymentTypeView = theCheck.getGUIPaymentName();
      String paymentType = theCheck.getTransactionPaymentName();
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
                                               , (ArmCurrency)theEvent))

      theCheck.setAmount( (ArmCurrency) theEvent);
    }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "PAYMENT", theCheck, this);
    }
  }


} //end class

