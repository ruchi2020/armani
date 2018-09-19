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
import com.chelseasystems.cr.config.ConfigMgr;

import javax.swing.table.DefaultTableCellRenderer;

/**
 */
public class CreditCardBldr_JP
    implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private CMSCreditCard thePayment;
  private CreditCardDlg_JP overRideDlg;
  /*bug-28360-start-showing the credit card dialog box in proper manner */
 // private GenericChooseFromTableDlg overRideDlg;
  /*bug-28360-start-showing the credit card dialog box in proper manner */
  private String cardCode = null;
  private String paymentType = null;
  private String paymentDesc = null;
  private String sCardPlan = null;
  private String sPlanDesc = null;
  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
  private static Hashtable htCreditCardPlans;

  public CreditCardBldr_JP() {
  }


  public void init(IObjectBuilderManager theBldrMgr,
                   IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
  }


  public void cleanup() {
  }

  /**
   *Display Credit Card options
   */
  private void displayCardTypes() {
    List paymentCCodes;
    paymentCCodes = CMSPaymentMgr.getPaymentCode("CREDIT_CARD");
    if (paymentCCodes == null || paymentCCodes.size() < 1)return;
    int numPaymentCCodes = paymentCCodes.size();
    ArrayList aPaymentCodes = new ArrayList(numPaymentCCodes);
    aPaymentCodes.addAll(paymentCCodes);
    //Fix for Defect # 1348
    Collections.sort(aPaymentCodes, new CreditCardCodeComparator());

    CMSPaymentCode[] paymentCodes = (CMSPaymentCode[]) aPaymentCodes.toArray(new
        CMSPaymentCode[0]);
    GenericChooserRow[] availPaymentCodes = new GenericChooserRow[
        numPaymentCCodes];
    if (numPaymentCCodes > 0) {
      for (int i = 0; i < numPaymentCCodes; i++) {
        availPaymentCodes[i] = new GenericChooserRow(new String[] {
            paymentCodes[i].getPaymentDesc()}
            , paymentCodes[i]);
      }
    }
    overRideDlg = new CreditCardDlg_JP(theAppMgr.getParentFrame(), theAppMgr, availPaymentCodes,
                new String[] {applet.res.getString("Payment Desc")}, 10);
    overRideDlg.setVisible(true);
  }

  private boolean displayCreditCardPlansFor(String sCreditCardTypeCode) {
    ConfigMgr config = new ConfigMgr("ArmCreditCardPlans.cfg");
    String sCreditCardPlans = "CREDIT_CARD_" + sCreditCardTypeCode +
        ".CARD_PLANS";
    Vector vecAvailCardPlans = new Vector();

    // If Plans for Card has already been loaded Before.
    if (htCreditCardPlans.get(sCreditCardTypeCode) != null) {
      vecAvailCardPlans = (Vector) htCreditCardPlans.get(sCreditCardTypeCode);
      if (vecAvailCardPlans.size() > 1) {
        GenericChooserRow[] availCardPlans = (GenericChooserRow[])
            vecAvailCardPlans.toArray(new GenericChooserRow[vecAvailCardPlans.
                                      size()]);
        overRideDlg = new CreditCardDlg_JP(theAppMgr.getParentFrame(), theAppMgr, availCardPlans,
        		new String[] {applet.res.getString("Credit Card Plans")
        });
        overRideDlg.setVisible(true);
        return true;
      }
    }
    // Load the Plans for the card from configuration
    else if (config != null) {
      sCreditCardPlans = config.getString(sCreditCardPlans);
      if (sCreditCardPlans != null) {
        StringTokenizer sTokens = new StringTokenizer(sCreditCardPlans, ",");
        if (sTokens != null) {
          String sTmp = "";
          while (sTokens.hasMoreTokens()) {
            sTmp = sTokens.nextToken();
            String sCode = config.getString(sTmp + ".CODE");
            String sLabel = config.getString(sTmp + ".LABEL");
            if (sCode != null && sLabel != null) {

              GenericChooserRow row = new GenericChooserRow(new String[] {
                  sLabel}
                  , sCode);
              vecAvailCardPlans.add(row);
            }
          }
          if (vecAvailCardPlans.size() > 1) {
            htCreditCardPlans.put(sCreditCardTypeCode, vecAvailCardPlans);
            GenericChooserRow[] availCardPlans = (GenericChooserRow[])
                vecAvailCardPlans.toArray(new GenericChooserRow[
                                          vecAvailCardPlans.size()]);
            overRideDlg = new CreditCardDlg_JP(theAppMgr.
                getParentFrame(), theAppMgr
                , availCardPlans, new String[] {applet.res.getString("Credit Card Plans")
            });
            overRideDlg.setVisible(true);
            return true;
          }

        }

      }
    }
    return false;
  }

  private String getSelectedPlan() {
    if (overRideDlg != null && overRideDlg.isOK()) {
      sCardPlan = (String) overRideDlg.getSelectedRow().getRowKeyData();
      sPlanDesc = (String) overRideDlg.getSelectedRow().getDisplayRow()[0];
      overRideDlg = null;
      return sCardPlan;
    }
    return null;
  }

  private void getSelectedType() {
    if (overRideDlg != null) {
      if (overRideDlg.isOK()) {
        CMSPaymentCode payCode = (CMSPaymentCode) overRideDlg.getSelectedRow().
            getRowKeyData();
        cardCode = payCode.getPaymentCode();
        paymentType = payCode.getPaymentType();
        paymentDesc = payCode.getPaymentDesc();
        overRideDlg = null;
      }
      else {
        cardCode = null;
        paymentType = null;
        paymentDesc = null;
        overRideDlg = null;
      }
    }
  }

  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("AMOUNT")) {
    	ConfigMgr payConfig = new ConfigMgr("ArmPaymentCommon.cfg");	    
      String key = "CREDIT_CARD_" + thePayment.getPaymentCode();
      String paymentTypeView = payConfig.getString(key + ".DESC");	
      if(paymentTypeView != null && paymentTypeView.length() > 0
      	&& paymentTypeView.indexOf("null")==-1){
      	thePayment.setGUIPaymentName(paymentTypeView);
      }else{
      	paymentTypeView = thePayment.getGUIPaymentName();
      }
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
    enterCreditCardType();
    displayCardTypes();
    getSelectedType();
    // If CardType selected
    if (paymentType != null && paymentType.equalsIgnoreCase("CREDIT_CARD")) {
      enterCreditCardPlan();
      if(!displayCreditCardPlansFor(cardCode) || getSelectedPlan() != null) {
        createPayment();
        applyAmount();
        return;
      }
    }
    // Do nothing, return to Payment applet.
    theBldrMgr.processObject(applet, "PAYMENT", null, this);
  }

  private void enterCreditCardType() {
    theAppMgr.setSingleEditArea(applet.res.getString("Select Credit Card type"));
  }

  private void enterCreditCardPlan() {
    theAppMgr.setSingleEditArea(applet.res.getString("Select Credit Plan"));
  }

  private void createPayment() {
    thePayment = new CMSCreditCard(ICMSPaymentConstants.CREDIT_CARD);
    if (cardCode != null) {
      thePayment.setGUIPaymentName(paymentDesc);
      thePayment.setPaymentCode(cardCode);
    }
    thePayment.setMessageIdentifier("CC");
    thePayment.setMessageType("0");
    thePayment.setTenderType("03");
    if (sCardPlan != null) {
      thePayment.setCardPlanCode(sCardPlan);
      thePayment.setGUICardPlanDesc(sPlanDesc);
    }
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
    sCardPlan = null;
    sPlanDesc = null;
    if (htCreditCardPlans == null)
      htCreditCardPlans = new Hashtable();
  }

  private class CreditCardCodeComparator implements Comparator {
	  public int compare(Object obj1, Object obj2) {
		  String code1 = ((CMSPaymentCode) obj1).getPaymentCode();
	      String code2 = ((CMSPaymentCode) obj2).getPaymentCode();
	      return code1.compareTo(code2);
	  }
  }

}
