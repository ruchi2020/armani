package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IObjectBuilder;
import com.chelseasystems.cr.appmgr.IObjectBuilderManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import com.chelseasystems.cs.payment.Coupon;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.swing.pos.PaymentApplet_JP;
import com.chelseasystems.cs.util.TransactionUtil;

public class CMSPremioDiscountBldr implements IObjectBuilder {

  private CMSPremioDiscount premioDiscount = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  public static double pointsMultiplier = 1.0;
  private static double defaultPointRatio = 0.0;
  public static double currentPointRatio = 1.0;

  static ConfigMgr loyaltyconfig = null;
  public static String loyaltyRewardRatio = null;
  public static String loyaltyAmount = null;
  static {
    loyaltyconfig = new ConfigMgr("loyalty.cfg");
    loyaltyRewardRatio = loyaltyconfig.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
    loyaltyAmount = loyaltyconfig.getString("LOYALTY_REWARD_AMOUNT");
    String defaultPointRatioStr = loyaltyconfig.getString("DEFAULT_POINT_RATIO");
    if (defaultPointRatioStr != null) {
      defaultPointRatio = Double.parseDouble(defaultPointRatioStr);
    }
    if (defaultPointRatio != 0.0) {
      pointsMultiplier = Double.parseDouble(loyaltyRewardRatio) / defaultPointRatio;
    }
    if (loyaltyconfig.getString("CURRENT_POINT_RATIO") != null) {
        currentPointRatio = Double.parseDouble(loyaltyconfig.getString("CURRENT_POINT_RATIO"));
      }
  }

  /**
   */
  public CMSPremioDiscountBldr() {
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

  public void build(String Command, CMSApplet applet, Object initValue) {

    premioDiscount = new CMSPremioDiscount("PREMIO_DISCOUNT");
    this.applet = applet;
    // register for call backs
    CMSStore store = (CMSStore)theAppMgr.getGlobalObject("STORE");
    CMSRegister reg = (CMSRegister)theAppMgr.getGlobalObject("REGISTER");
    ((CMSPremioDiscount)premioDiscount).setStoreId(store.getId());
    ((CMSPremioDiscount)premioDiscount).setRegisterId(reg.getId());
    ArmCurrency curr = null;
    if(initValue!=null && initValue instanceof ArmCurrency){
    	curr = (ArmCurrency)initValue;
    }
    if (curr != null) {
      premioDiscount.setAmount(curr);
      PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      ArmCurrency currAmountDue = ((CMSCompositePOSTransaction)theTxn).getCompositeTotalAmountDue();
      ((CMSCompositePOSTransaction)theTxn).setRedeemableAmount(( -1) * curr.doubleValue());
      premioDiscount.setRedeemPoints(String.valueOf(((CMSCompositePOSTransaction)theTxn).
          getRedeemablePoints()));
      try {
        if (currAmountDue.absoluteValue().lessThanOrEqualTo(curr.absoluteValue())) {
          showPremioPointsMessage(getReturnRedeemablePointsAsString(curr.doubleValue()),
        		  Math.abs(currAmountDue.doubleValue()), true);
        }
      } catch (CurrencyException ce) {}

      theBldrMgr.processObject(applet, "PAYMENT", premioDiscount, this);
      premioDiscount = null;
      return;
    }

    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "PAYMENT", premioDiscount, this);
      premioDiscount = null;
    }
  }

  private String getReturnRedeemablePointsAsString(double redeemPoints){
//	 return String.valueOf(Math.round(Math.ceil(Math.abs(redeemPoints)/Double.parseDouble(loyaltyAmount)) * pointsMultiplier ));
 	 return String.valueOf(Math.round(Math.ceil((redeemPoints)/Double.parseDouble(loyaltyAmount)) * pointsMultiplier ));

  }
  
  private void showPremioPointsMessage(String premio, double amount, boolean isReturn) {
	  if(isReturn){
			if ((Integer.parseInt(premio)) < 0) {
				theAppMgr.showErrorDlg(String.valueOf(Math.abs(Integer.parseInt(premio))) + " "
						+ CMSApplet.res.getString("Premio points refunded"));
			} else {
      theAppMgr.showErrorDlg(premio + " "
						+ CMSApplet.res.getString("Premio points charged as Tender"));
			}
			//+ " for Refund Amount " + new ArmCurrency(Math.round(amount)).formattedStringValue());
	  } /*else{
      theAppMgr.showErrorDlg(premio + " "
          + applet.res.getString("Points Removed ") +
          " for Tendered Amount " + new ArmCurrency(Math.round(amount)).formattedStringValue());
	  }*/
  }

  private void adjustLoyalty(CMSCompositePOSTransaction cmsCompositePosTxn) {
    Loyalty loyalty = cmsCompositePosTxn.getLoyaltyCard();
    double points = cmsCompositePosTxn.getLoyaltyPoints();
    double loyaltyUsed = cmsCompositePosTxn.getUsedLoyaltyPoints();
    double currBal = loyalty.getCurrBalance();
    double currYearBal = loyalty.getCurrYearBalance();
    double lastYearBal = loyalty.getLastYearBalance();
    if (loyalty.getLastYearBalance() < loyaltyUsed) {
      currYearBal = currYearBal - (loyaltyUsed - lastYearBal);
      lastYearBal = 0.0;
    } else {
      lastYearBal -= loyaltyUsed;
    }
    currBal -= loyaltyUsed;
    loyalty.setCurrBalance(currBal + points);
    loyalty.setLifeTimeBalance(loyalty.getLifeTimeBalance() + points);
    if (loyalty.isYearlyComputed()) {
      loyalty.setCurrYearBalance(currYearBal + points);
      loyalty.setLastYearBalance(lastYearBal);
    } else {
      loyalty.setCurrYearBalance(0);
      loyalty.setLastYearBalance(0);
    }
  }

  private String getValueWithNoDecimal(double val){
	  return (new ArmCurrency(val)).formattedStringValue();
  }
  
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("PREMIODISCOUNT")) {
      //      premioDiscount.setAmount((ArmCurrency)theEvent);
      double permioDiscountEntered = ((ArmCurrency)theEvent).doubleValue();
      PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      if (theTxn instanceof CMSCompositePOSTransaction) {
        double nonMiscSaleAmtDue = ((CMSCompositePOSTransaction)theTxn).getCompositeTotalAmountDue().
            doubleValue();
        if((new Double(loyaltyRewardRatio)).doubleValue()!=0){
        	double loyaltyMultiple = Double.parseDouble(loyaltyAmount);

        	if((( (ArmCurrency)theEvent).doubleValue() % loyaltyMultiple) > 0){
        		theAppMgr.showErrorDlg("Please enter multiple of " + getValueWithNoDecimal(loyaltyMultiple));
        		return;
        	}
        }
        Loyalty loyalty = ((CMSCompositePOSTransaction)theTxn).getLoyaltyCard();
        if (loyalty == null) {
          theAppMgr.setSingleEditArea(applet.res.getString("No Loyalty Available."));
          return;
        }
        double loyaltyMaxValue = (loyalty.getCurrBalance())/(new Double(loyaltyRewardRatio)).doubleValue();
        ArmCurrency tmpCurr = getPremioDiscountAvailable(loyalty.getCurrBalance());
        try {
            if(((ArmCurrency)theEvent).greaterThan(getPremioDiscountAvailable(loyalty.getCurrBalance()))){
            	theAppMgr.showErrorDlg("Premio discount can not exceed " + getValueWithNoDecimal(tmpCurr.doubleValue()));
            	return;
            } else if (((ArmCurrency)theEvent).doubleValue() < Double.parseDouble(loyaltyAmount)) {
                theAppMgr.showErrorDlg("Premio discount can not be lower than " + getValueWithNoDecimal(Double.parseDouble(loyaltyAmount)));
                return;
            }
        } catch (CurrencyException ce) {
        }
/*MD*/ System.out.println("==>> 1.  nonMiscSaleAmtDue and premioDiscountEntered " + nonMiscSaleAmtDue + " " + permioDiscountEntered);        
        ((CMSPremioDiscount)premioDiscount).setRedeemPoints(String.valueOf(((ArmCurrency)theEvent).
            getDoubleValue()));
        nonMiscSaleAmtDue -= ((CMSCompositePOSTransaction)theTxn).getTotalPaymentAmount().doubleValue();
/*MD*/ System.out.println("==>> 2.  nonMiscSaleAmtDue and premioDiscountEntered " + nonMiscSaleAmtDue + " " + permioDiscountEntered);        
        if (nonMiscSaleAmtDue < permioDiscountEntered) {
          try {
            premioDiscount.setAmount(ArmCurrency.valueOf(String.valueOf(nonMiscSaleAmtDue)));
   //         theAppMgr.showErrorDlg(applet.res.getString("Points removed = ") + getPointsAsString(permioDiscountEntered));
            showPremioPointsMessage(getPointsAsString(permioDiscountEntered),nonMiscSaleAmtDue,false);
          } catch (Exception e) {
            e.printStackTrace();
            // nothing set
          }
        } else {
          premioDiscount.setAmount((ArmCurrency)theEvent);
        }
      }
    }
    if (completeAttributes()) {
      PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      //   ((CMSCompositePOSTransaction)theTxn).setRedeemablePoints(premioDiscount.getAmount().getDoubleValue());
      ((CMSCompositePOSTransaction)theTxn).setRedeemableAmount(Double.parseDouble(((
          CMSPremioDiscount)premioDiscount).getRedeemPoints()));
      premioDiscount.setRedeemPoints(String.valueOf(((CMSCompositePOSTransaction)theTxn).
          getRedeemablePoints()));
      theBldrMgr.processObject(applet, "PAYMENT", premioDiscount, this);
      premioDiscount = null;
    }
  }


  private boolean completeAttributes() {
    if (premioDiscount.getAmount() == null) {
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
            getTotalPaymentAmount());

        CMSCustomer cust = (CMSCustomer)appModel.getCustomer();
        ((CMSPremioDiscount)premioDiscount).setCustId(cust.getId());

        Loyalty loyalty = ((CMSCompositePOSTransaction)theTxn).getLoyaltyCard();
        if (loyalty == null) {
          theAppMgr.setSingleEditArea(applet.res.getString("No Loyalty Available."));
          return false;
        }
        ((CMSPremioDiscount)premioDiscount).setLoyaltyNumber(loyalty.getLoyaltyNumber());
        double availableValue = loyalty.getCurrBalance();

        ArmCurrency premioDiscountAvl = getPremioDiscountAvailable(availableValue);
        double loyaltyAmt = Double.parseDouble(loyaltyAmount);
        if (premioDiscountAvl.getDoubleValue() < loyaltyAmt) {
          theAppMgr.setSingleEditArea(applet.res.getString("Available Premio Discount = ")
              + premioDiscountAvl.stringValue());
        } else {
          theAppMgr.setSingleEditArea(applet.res.getString("Available Premio Discount = ")
              + premioDiscountAvl.stringValue(), "PREMIODISCOUNT"
              , premioDiscountAvl, theAppMgr.CURRENCY_MASK);
        }
        System.out.println("completeAttributes returning false ");
        return false;
      } catch (Exception e) {
        theAppMgr.setSingleEditArea(applet.res.getString("Available Premio Discount = ")
            , "PREMIODISCOUNT"
            , theAppMgr.CURRENCY_MASK);
        e.printStackTrace();
        System.out.println("completeAttributes returning false ");
        return false;
      }
    } else {
      System.out.println("completeAttributes returning true ");
      return true;
    }
  }


  public ArmCurrency getPremioDiscountAvailable(double value) {
    System.out.println(" getPremioDiscountAvailable " + value);
    if ((new Double(loyaltyRewardRatio)).doubleValue() == 0 ||
        (new Double(loyaltyAmount)).doubleValue() == 0) {
      return new ArmCurrency(0);
    }
    double loyaltyAmt = new Double(loyaltyAmount).doubleValue();

    PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
        CMSAppModelFactory.getInstance(), theAppMgr);
    double totalPoints = value / (new Double(loyaltyRewardRatio)).doubleValue();

    if (theTxn instanceof CMSCompositePOSTransaction) {
      double usedPremio = ((CMSCompositePOSTransaction)theTxn).getRedeemableAmount();
      totalPoints = totalPoints - usedPremio;
    }
    if (totalPoints > loyaltyAmt) {
      totalPoints = (Math.floor(totalPoints / loyaltyAmt)) * loyaltyAmt;
    }
    try {
      /*Currency amtLeft = appModel.getCompositeTotalAmountDue().subtract(theTxn.
          getTotalPaymentAmount());*/

      ArmCurrency amtLeft = leftAmountForPremio();
      /*System.out.println("totalPoitsns " + totalPoints);
                 System.out.println("amtLeft " + amtLeft);
                 System.out.println("loyaltyAmt  " + loyaltyAmt);*/
      if (amtLeft.getDoubleValue() < totalPoints) {
        if (amtLeft.getDoubleValue() == 0) {
          totalPoints = 0;
        } else if (amtLeft.getDoubleValue() < loyaltyAmt) {
          totalPoints = loyaltyAmt;
        } else {
          totalPoints = (Math.ceil(amtLeft.getDoubleValue() / loyaltyAmt)) * loyaltyAmt;
        }
      }
      return ArmCurrency.valueOf(String.valueOf(totalPoints));
    } catch (Exception e) {
      return new ArmCurrency(0);
    }

  }

  private ArmCurrency leftAmountForPremio() {
    PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
        CMSAppModelFactory.getInstance(), theAppMgr);
    double saleTotalAmountDue = ((CMSCompositePOSTransaction)theTxn).getNonMiscSaleAmountDue().
        doubleValue();
    double compositeTotalAmountDue = appModel.getCompositeTotalAmountDue().doubleValue()/* -
        appModel.getCompositeTaxAmount().doubleValue()*/;

    double alreadyPaidAmount = theTxn.getTotalPaymentAmount().doubleValue();
    double premioAlreadyPaid = ((CMSCompositePOSTransaction)theTxn).getRedeemableAmount();

    double alreadyPaid = alreadyPaidAmount + premioAlreadyPaid;
    double totalToBePaid = compositeTotalAmountDue - alreadyPaid;
    double maxPremioAllowed = 0;

    System.out.println("saleTotalAmtDue " + saleTotalAmountDue);
    System.out.println("compositeTotalAmountDue " + compositeTotalAmountDue);
    System.out.println("alreadyPaidAmount " + alreadyPaidAmount);
    System.out.println("premioAlreadyPaid " + premioAlreadyPaid);

    /*if (totalToBePaid > saleTotalAmountDue) {
      maxPremioAllowed = saleTotalAmountDue - premioAlreadyPaid;
    } else {
      maxPremioAllowed = totalToBePaid;
    }*/

    if (totalToBePaid > compositeTotalAmountDue) {
        maxPremioAllowed = compositeTotalAmountDue - premioAlreadyPaid;
      } else {
        maxPremioAllowed = totalToBePaid;
      }
    
    
    System.out.println("returning maxPremioAllowed " + maxPremioAllowed);
    try {
      return new ArmCurrency(maxPremioAllowed);
    } catch (Exception e) {
      e.printStackTrace();
      return new ArmCurrency(0);
    }
  }

  public static boolean isEnoughPoints(double points) {
    if (loyaltyRewardRatio != null) {
      double ratio = Double.parseDouble(loyaltyRewardRatio);
      if (ratio != 0.0) {
        if (points / ratio >= Double.parseDouble(loyaltyAmount)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static String getPointsAsString(double amount) {
	  double ratio = Double.parseDouble(loyaltyRewardRatio);
	  return String.valueOf(Math.round(Math.floor(amount*ratio)));
  }
}
