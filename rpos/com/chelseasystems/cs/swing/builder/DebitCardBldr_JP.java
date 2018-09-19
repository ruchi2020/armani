/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 11/22/2005 | Manpreet  | N/A       | Credit/Debit Card builder for Japan                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.swing.builder;

import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.payment.*;
import com.armani.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.util.TransactionUtil;
import javax.swing.table.DefaultTableCellRenderer;


/**
 */
public class DebitCardBldr_JP implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;

  private CMSDebitCard thePayment;
  private double theAmount = 0.0d;
  private GenericChooseFromTableDlg overRideDlg;
  private String cardCode = null;
  private String paymentType = null;
  private String paymentDesc = null;
  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
  private static Hashtable htDebitCardPlans;

  public DebitCardBldr_JP() {
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
  }

  /**
   *Display Credit Card options
   */
  private void displayDebitCardTypes() {
    List paymentCCodes;
    paymentCCodes = CMSPaymentMgr.getPaymentCode("DEBIT_CARD");
    if(paymentCCodes == null || paymentCCodes.size()<1) return;
    int numPaymentCCodes = paymentCCodes.size();
    ArrayList aPaymentCodes = new ArrayList(numPaymentCCodes);
    aPaymentCodes.addAll(paymentCCodes);
    CMSPaymentCode[] paymentCodes =(CMSPaymentCode[])aPaymentCodes.toArray(new CMSPaymentCode[0]);
    GenericChooserRow[] availPaymentCodes = new GenericChooserRow[numPaymentCCodes];
    if (numPaymentCCodes>0){
        for (int i = 0; i < numPaymentCCodes; i++) {
            availPaymentCodes[i] = new GenericChooserRow(new String[] {
                    paymentCodes[i].getPaymentDesc()}, paymentCodes[i]);
        }
    }
    overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr
        , availPaymentCodes, new String[] {applet.res.getString("Payment Desc")
    });
    overRideDlg.setVisible(true);
  }


  private void getSelectedType() {
    if (overRideDlg!=null && overRideDlg.isOK()) {
      CMSPaymentCode payCode = (CMSPaymentCode)overRideDlg.getSelectedRow().getRowKeyData();
      cardCode = payCode.getPaymentCode();
      paymentType = payCode.getPaymentType();
      paymentDesc = payCode.getPaymentDesc();
      overRideDlg = null;
    }
  }
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("AMOUNT")) {
      String paymentTypeView = thePayment.getGUIPaymentName();
      String paymentType = thePayment.getTransactionPaymentName();
      if (theEvent == null ||
          ( (ArmCurrency) theEvent).doubleValue() <= 0.0d) {
        theAppMgr.showErrorDlg(applet.res.getString(
            "Amount can't be zero"));
        applyAmount();
        return;
      }
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType,
                                               paymentTypeView
                                               , (ArmCurrency) theEvent)) {
        thePayment.setAmount( (ArmCurrency) theEvent);
        theBldrMgr.processObject(applet, "PAYMENT", thePayment, this);
        thePayment = null;
      }
    }
  }


  public void build(String Command, CMSApplet applet, Object initValue) {
    resetAttributes();
    this.applet = applet;
    enterDebitCardType();
    displayDebitCardTypes();
    getSelectedType();
    //If Debitcard type was selected.
    if (paymentType != null) {
      createPayment();
      applyAmount();
    }
    // Do nothing and return to payment applet.
    else {
      theBldrMgr.processObject(applet, "PAYMENT", null, this);
    }
  }

  private void enterDebitCardType()
  {
    theAppMgr.setSingleEditArea(applet.res.getString("Select Debit Card type"));
  }


  private void createPayment() {
    thePayment = new CMSDebitCard(ICMSPaymentConstants.DEBIT_CARD);
    if (cardCode != null) {
      thePayment.setGUIPaymentName(paymentDesc);
      thePayment.setPaymentCode(cardCode);
    }
    thePayment.setMessageIdentifier("CC");
    thePayment.setMessageType("0");
    thePayment.setTenderType("03");
  }

  private void applyAmount() {
    try {
      PaymentTransaction theTxn = (PaymentTransaction) theAppMgr.
          getStateObject("TXN_POS");
      CMSPaymentTransactionAppModel appModel = (
          CMSPaymentTransactionAppModel)
          theTxn.getAppModel(
          CMSAppModelFactory.getInstance(), theAppMgr);
      ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(
          appModel.
          getTotalPaymentAmount());
      theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."),
                                  "AMOUNT"
                                  , amt.absoluteValue(),
                                  theAppMgr.CURRENCY_MASK);
    }
    catch (Exception ex) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."),
                                  "AMOUNT"
                                  , theAppMgr.CURRENCY_MASK);

    }

  }

  private void resetAttributes() {
    thePayment = null;
    if(htDebitCardPlans==null)
    htDebitCardPlans = new Hashtable();
  }




}

