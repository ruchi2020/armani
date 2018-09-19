/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.payment.Cash;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.swing.dlg.GenericChooseFromTableDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.util.TransactionUtil;


/**
 */
public class ForeignCashBldr implements IObjectBuilder {
  private Cash cash;
  private CurrencyType foreignType;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;

  /**
   */
  public ForeignCashBldr() {
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
        
      //Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency
        ConfigMgr cfgMgr = new ConfigMgr("currency.cfg");
    	String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
    	
    	if (Rounding_Mode == null) {
    		Rounding_Mode = "false";
    	}
    	if(Rounding_Mode.equalsIgnoreCase("true"))
    	{
    		double amount=roundMode(pmtAmt.convertTo(ArmCurrency.getBaseCurrencyType()).doubleValue(), 0);
    		pmtAmt = new ArmCurrency(amount);
    	} //end Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency
    	else
    	{
    		pmtAmt = pmtAmt.convertTo(ArmCurrency.getBaseCurrencyType());
    	}
        // if change, only allow amount left as max
        //if (validateChangeAmount(pmtAmt))
        //cash.setAmount(pmtAmt);
        String paymentTypeView = cash.getGUIPaymentName();
        String paymentType = cash.getTransactionPaymentName();
        if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView, pmtAmt))
          cash.setAmount(pmtAmt);
      } catch (UnsupportedCurrencyTypeException uce) {
        theAppMgr.showErrorDlg(applet.res.getString("Foreign currency is not supported.") + "  "
            + foreignType.getDescription());
        theBldrMgr.processObject(applet, "PAYMENT", null, this);
      } catch (MissingExchangeRateException mere) {
        theAppMgr.showErrorDlg(applet.res.getString(
            "Missing foreign conversion information.  Please call the Help Desk."));
        theBldrMgr.processObject(applet, "PAYMENT", null, this);
      }
    }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "PAYMENT", cash, this);
      cash = null;
    }
  }
  
  
//Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency
  public static double roundMode(double value, int places)
	{
		if (places < 0) throw new IllegalArgumentException();
		
		
		BigDecimal bd = new BigDecimal(value);
		ConfigMgr cfgMgr = new ConfigMgr("currency.cfg");
		String Rounding_Mode_Flag=cfgMgr.getString("ROUNDING_MODE");
		if(Rounding_Mode_Flag !=null){
		if(Rounding_Mode_Flag.equalsIgnoreCase("UP"))
		{
		bd =bd.setScale(places,RoundingMode.UP);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("DOWN"))
		{
			bd =bd.setScale(places,RoundingMode.DOWN);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("FLOOR"))
		{
			bd =bd.setScale(places,RoundingMode.FLOOR);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("CEILING"))
		{
			bd =bd.setScale(places,RoundingMode.CEILING);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_UP"))
		{
			bd =bd.setScale(places,RoundingMode.HALF_UP);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_DOWN"))
		{
			bd =bd.setScale(places,RoundingMode.HALF_DOWN);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("HALF_EVEN"))
		{
			bd =bd.setScale(places,RoundingMode.HALF_EVEN);
		}
		else if(Rounding_Mode_Flag.equalsIgnoreCase("UNNECESSARY"))
		{
			bd =bd.setScale(places,RoundingMode.UNNECESSARY);
		}
		}
		
		return bd.doubleValue();
		
	}//end Mahesh Nandure : 26 APR 2017 : Rounding selling price for Foreign currency
  

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    cash = new Cash((String)initValue);
    this.applet = applet;
    determineForeignType((String)initValue);
    completeAttributes();
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
      if (amtLeft.greaterThanOrEqualTo(new ArmCurrency(0.0)))
        return true;
      if (amt.greaterThan(amtLeft.absoluteValue())) {
        theAppMgr.showErrorDlg(applet.res.getString("You cannot give more change than what is due."));
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
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
        ArmCurrency foreignAmt = amt.convertTo(foreignType);
        /*
         * this hack brought to you by bug 4539 - CG
         * if the conversion rate does not convert to enough of our currency
         * probably because our currency is weaker than the foreign, then
         * add an extra "penny" of the foreign type.  They will receive a
         * single or multiple "pennies" of our weaker currency type as change.
         */
        double roundingError = amt.doubleValue()
            - foreignAmt.round().convertTo(ArmCurrency.getBaseCurrencyType()).doubleValue();
        if (roundingError > 0) {
          // the foreign amount is still short, add a little more
          foreignAmt = foreignAmt.add(ArmCurrency.getMinimumDenomination(foreignType));
        }
        //theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "CASH", foreignAmt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        //  we've got to know what the currency type is in order to display it properly...
        int length = (new Double(ArmCurrency.getConversionRate(amt, foreignAmt).doubleValue()).
            toString()).length();
        int index = (new Double(ArmCurrency.getConversionRate(amt, foreignAmt).doubleValue()).toString()).
            indexOf(".");
        int afterDecimal = length - index;
        String strConversion = null;
        if (afterDecimal <= 4) {
          strConversion = (new Double(ArmCurrency.getConversionRate(amt, foreignAmt).doubleValue()).
              toString());
        } else {
          strConversion = (new Double(ArmCurrency.getConversionRate(amt, foreignAmt).doubleValue()).
              toString()).substring(0, index + 4);
        }
        String sSuggested = new Double((foreignAmt.round()).doubleValue()).toString(); //5021
        int indexForRounding = sSuggested.indexOf(".");
        String strAmount = new Integer(sSuggested.substring(0, indexForRounding)).toString();
        double intAmount_initial = new Double("0.0").doubleValue();
        if (indexForRounding >= 4) {
          intAmount_initial = (new Double(sSuggested.substring(0, indexForRounding - 3))).
              doubleValue();
        }
        Double intAmount_final = new Double(intAmount_initial + 1);
        /*StringBuffer sBuf = new StringBuffer("1");
         for(int i =0; i < strAmount.length()-1; i++){
         sBuf.append("0");
         }

         String strMult = sBuf.toString();*/
        Double dMult = new Double("1000"); //.doubleValue();
        Double suggestedAmt = new Double(intAmount_final.doubleValue() * dMult.doubleValue());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter foreign amount. Exact = "
            + (new Double((foreignAmt.absoluteValue()).doubleValue())).longValue()
            + "; Suggested = " + suggestedAmt.longValue() + "; Min. Denom = "
            + (new Double((CMSPaymentMgr.getMinDenomAllowed("JPY_CASH")).getDoubleValue())).
            longValue() + "; Exchange Rate: " + "1 (" + ArmCurrency.getBaseCurrencyType().getCode()
            + ") = " + strConversion
            /*(foreignAmt.divide(amt.doubleValue())).doubleValue()*/
            + " (" + foreignType.getCode()) + ")", "CASH", new ArmCurrency(suggestedAmt.doubleValue())
            , theAppMgr.CURRENCY_MASK);
        return false;
      } catch (Exception ex) {
        System.out.println("ForeignCashBldr.completeAttributes()->" + ex);
        ex.printStackTrace();
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "CASH"
            , theAppMgr.CURRENCY_MASK);
        return false;
      }
    }
    return true;
  }
}

