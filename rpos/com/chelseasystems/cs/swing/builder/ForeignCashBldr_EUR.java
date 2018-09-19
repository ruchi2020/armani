/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------|
 | 2    | 06-15-2005 | Khyati    |           |Europe: Display currency pick list            |
 +------+------------+-----------+-----------+----------------------------------------------|
 | 1    | 06-13-2005 | Khyati    |           |Base From Armani global                       |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.payment.Cash;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.currency.*;
import com.chelseasystems.cs.swing.dlg.ForeignCurrencyExchangeRateListDlg;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.currency.ExchangeRateMemoryServices;
import java.util.Hashtable;
import java.util.Enumeration;
import com.chelseasystems.cs.store.CMSStore;


/**
 */
public class ForeignCashBldr_EUR implements IObjectBuilder {
  private Cash cash;
  private CurrencyType foreignType;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private ForeignCurrencyExchangeRateListDlg fDlg;
//  private ArmCurrency rate = new ArmCurrency(0.0);
  private ArmCurrency cashAmount = new ArmCurrency(0.0);
  private Double rate = new Double(0.0d);
  /**
   */
  public ForeignCashBldr_EUR() {
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
    // The amount coming in is the amount of Foreign cash.  All payments are converted
    // to the base currency type to prevent a different conversion from being
    // performed on the server.
    if (theCommand.equals("CASH")) {
      try {
        double rawAmt = ((ArmCurrency)theEvent).doubleValue();
        // truncate any non-significant sigits
        ArmCurrency pmtAmt = new ArmCurrency(foreignType, rawAmt);
        pmtAmt = pmtAmt.convertTo(ArmCurrency.getBaseCurrencyType(), rate);
        // if change, only allow amount left as max
        if (validateChangeAmount(pmtAmt)) {
          cash.setAmount(pmtAmt);
        }
      } catch (Exception uce) {
        System.out.println("ForeignCashBldr.completeAttributes()->" + uce);
        uce.printStackTrace();
        theBldrMgr.processObject(applet, "PAYMENT", null, this);
        return;
      }
      if (completeAttributes()) {
        theBldrMgr.processObject(applet, "PAYMENT", cash, this);
        cash = null;
        return;
      }
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    cash = new CMSForeignCash((String)initValue);
    System.out.println("cash =" + cash.getAmount());
    this.applet = applet;
    //ks: Europe - Comment determineForeignType
    //      determineForeignType((String)initValue);
    //ks: Europe - Display the foreign cash pick list
    displayPickList();
    if (fDlg.getIsOk()) {
      if (completeAttributes()) {}
    }
  }

  /**
   * display the foreign currency with exchange rate information from currencyrates.dat
   */
  private void displayPickList() {
    try {
      CMSStore store = (CMSStore)theAppMgr.getGlobalObject("STORE");
      CurrencyRate currencyRate[] = CMSExchangeRateHelper.findAllCurrencyRates(theAppMgr
          , store.getPreferredISOCountry(), store.getPreferredISOLanguage());
      CMSPaymentTransactionAppModel theTxn = (CMSPaymentTransactionAppModel)((PaymentTransaction)
          theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance()
          , theAppMgr);
      fDlg = new ForeignCurrencyExchangeRateListDlg(theAppMgr, currencyRate, theTxn);
      fDlg.setVisible(true);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   */
  private void determineForeignType(String aPaymentKey) {
    String key = "USD";
    if (aPaymentKey.length() > 2) {
      key = aPaymentKey.substring(0, 3);
    }
    try {
      foreignType = CurrencyType.getCurrencyType(key);
    } catch (UnsupportedCurrencyTypeException ucte) {
      System.err.println("ForeignCashBuilder.determineForeignType()->" + ucte);
      foreignType = ArmCurrency.getBaseCurrencyType();
    }
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
      ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
          getTotalPaymentAmount());
      if (amtLeft.greaterThanOrEqualTo(new ArmCurrency(0.0))) {
        return true;
      }
      if (amt.greaterThan(amtLeft.absoluteValue())) {
        theAppMgr.showErrorDlg(applet.res.getString("You cannot give more change than what is due."));
        return false;
      } else {
        return true;
      }
    } catch (Exception ex) {
      return true;
    }
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    Hashtable ht = fDlg.getSelectedRow();
    if (ht == null) {
    	return false;
    }
    Enumeration enm = ht.keys();
    CurrencyRate curRate = null;
    if (enm.hasMoreElements()) {
      curRate = (CurrencyRate)enm.nextElement();
    }
    System.out.println("@@@@@@@@@@@@@@@@Tender Code : " + curRate.getTenderCode());
    cash.setPaymentCode(curRate.getTenderCode());
    if (cash.getAmount() == null) {
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
        foreignType = CurrencyType.getCurrencyType(curRate.getFromCurrency());
        rate = curRate.getConversionRate();
        ArmCurrency foreignAmt = amt.convertTo(foreignType, curRate.getConversionRate());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount as foreign currency.")
            , "CASH", foreignAmt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        theAppMgr.setEditAreaFocus();
        /*
         * this hack brought to you by bug 4539 - CG
         * if the conversion rate does not convert to enough of our currency
         * probably because our currency is weaker than the foreign, then
         * add an extra "penny" of the foreign type.  They will receive a
         * single or multiple "pennies" of our weaker currency type as change.
         */
        //            double roundingError = amt.doubleValue() - foreignAmt.round().convertTo
        //                                    (ArmCurrency.getBaseCurrencyType()).doubleValue();
        //            if (roundingError > 0) {
        //               // the foreign amount is still short, add a little more
        //               foreignAmt = foreignAmt.add(ArmCurrency.getMinimumDenomination(foreignType));
        //            }
        //theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "CASH", foreignAmt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        //  we've got to know what the currency type is in order to display it properly...
        /* code from US
         int length = (new Double(ArmCurrency.getConversionRate(amt,foreignAmt).doubleValue()).toString()).length();
         int index = (new Double(ArmCurrency.getConversionRate(amt,foreignAmt).doubleValue()).toString()).indexOf(".");
         int afterDecimal = length - index;
         String strConversion = null;
         if(afterDecimal <=4){
         strConversion = (new Double(ArmCurrency.getConversionRate(amt,foreignAmt).doubleValue()).toString());
         } else{
         strConversion = (new Double(ArmCurrency.getConversionRate(amt,foreignAmt).doubleValue()).toString()).substring(0,index + 4);
         }
         String sSuggested = new Double((foreignAmt.round()).doubleValue()).toString();//5021
         int indexForRounding = sSuggested.indexOf(".");
         String strAmount = new Integer(sSuggested.substring(0,indexForRounding)).toString();
         double intAmount_initial = new Double("0.0").doubleValue();
         if(indexForRounding >= 4){
         intAmount_initial = (new Double(sSuggested.substring(0,indexForRounding-3))).doubleValue();
         }
         Double intAmount_final = new Double(intAmount_initial + 1);
         Double dMult = new Double("1000");//.doubleValue();
         Double suggestedAmt = new Double(intAmount_final.doubleValue() * dMult.doubleValue());
         theAppMgr.setSingleEditArea(applet.res.getString("Enter foreign amount. Exact = " + (new Double((foreignAmt.absoluteValue()).doubleValue())).longValue() + "; Suggested = " + suggestedAmt.longValue() + "; Min. Denom = " + (new Double((CMSPaymentMgr.getMinDenomAllowed("JPY_CASH")).getDoubleValue())).longValue() + "; Exchange Rate: " +  "1 (" + ArmCurrency.getBaseCurrencyType().getCode() + ") = " + strConversion/*(foreignAmt.divide(amt.doubleValue())).doubleValue() + " (" + foreignType.getCode()) + ")" , "CASH", new ArmCurrency(suggestedAmt.doubleValue()), theAppMgr.CURRENCY_MASK);
          */
         return false;
      } catch (Exception ex) {
        System.out.println("ForeignCashBldr.completeAttributes()->" + ex);
        ex.printStackTrace();
        return false;
      }
    }
    return true;
  }
}

