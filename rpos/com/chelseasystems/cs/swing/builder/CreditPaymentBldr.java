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
import com.chelseasystems.cs.payment.Credit;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class CreditPaymentBldr implements IObjectBuilder {
  private Payment credit = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  static ArmCurrency curAmount;

  /**
   */
  public CreditPaymentBldr() {
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
  public void EditAreaEvent(String theCommand, Object theEvent)
  { }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    credit = new Credit(ICMSPaymentConstants.CREDIT);
    this.applet = applet;
    curAmount = new ArmCurrency(0.0);
    try {
      PaymentTransaction theTxn = (PaymentTransaction) theAppMgr.getStateObject(
          "TXN_POS");
      CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)
          theTxn.getAppModel(
          CMSAppModelFactory.getInstance(), theAppMgr);
      curAmount = appModel.getCompositeTotalAmountDue().subtract(theTxn.
          getTotalPaymentAmount());
    }
    catch (Exception e) {}
    credit.setAmount(curAmount);
    if (theAppMgr.showOptionDlg(applet.res.getString("Post Transaction")
                                , applet.res.getString(
        "Selecting 'Credit' will post the transaction.  Do you want to post this transaction?"))) {
      theBldrMgr.processObject(applet, "PAYMENT", credit, this);
      credit = null;
    }
    else {
      theBldrMgr.processObject(applet, "PAYMENT", null, this);
    }
  }

}



