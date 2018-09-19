/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.TravellersCheck;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;


/**
 */
public class TravellersCheckBldr implements IObjectBuilder {
  private Payment check = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;

  /**
   */
  public TravellersCheckBldr() {
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
    if (theCommand.equals("AMOUNT")) {
      String paymentTypeView = check.getGUIPaymentName();
      String paymentType = check.getTransactionPaymentName();
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
          , (ArmCurrency)theEvent))
        check.setAmount((ArmCurrency)theEvent);
    }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "PAYMENT", check, this);
      check = null;
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    check = new TravellersCheck((String)initValue);
    this.applet = applet;
    // register for call backs
    completeAttributes();
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    if (check.getAmount() == null) {
      // Issue # 711
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
            getTotalPaymentAmount());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , amtLeft.absoluteValue(), theAppMgr.CURRENCY_MASK);
        return false;
      } catch (Exception e) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "MALLCERT"
            , theAppMgr.CURRENCY_MASK);
        e.printStackTrace();
        return false;
      }
    } else
      return true;
  }
}

