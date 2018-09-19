/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cs.payment.Coupon;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;



/**
 */
public class CouponBldr implements IObjectBuilder {
  private Payment coupon = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;

  /**
   */
  public CouponBldr() {
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
      String paymentTypeView = coupon.getGUIPaymentName();
      String paymentType = coupon.getTransactionPaymentName();
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
          , (ArmCurrency)theEvent))
        coupon.setAmount((ArmCurrency)theEvent);
    }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "PAYMENT", coupon, this);
      coupon = null;
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    coupon = new Coupon((String)initValue);
    CMSStore store = (CMSStore)theAppMgr.getGlobalObject("STORE");
    CMSRegister reg = (CMSRegister)theAppMgr.getGlobalObject("REGISTER");
	((Coupon)coupon).setStoreId(store.getId());
    ((Coupon)coupon).setRegisterId(reg.getId());
	((Coupon)coupon).setDesc(applet.res.getString("Type: ") + ((Coupon)coupon).getType());
    this.applet = applet;
    // register for call backs
    completeAttributes();
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    if (coupon.getAmount() == null) {
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

