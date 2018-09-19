/*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.pricing.DiscountPriceEngine;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cs.discount.ArmLineDiscount;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Enumeration;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSNoSaleLineItem;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ArmLineDiscountPriceEngine extends DiscountPriceEngine {

  /**
   * Constructor
   * @param aDiscount Discount
   */
  public ArmLineDiscountPriceEngine(Discount aDiscount) {
    super(aDiscount);
  }
//Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
  public static double roundMode(double value, int places)
	{
		if (places < 0) throw new IllegalArgumentException();
		
		
		BigDecimal bd = new BigDecimal(value);
		ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
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
		
	}
//end Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount

  // OVERRIDE protected double getDiscountPercent(POSLineItem aLineItem)
  // if you want to calculate the discount percent differently.
  // Use the parameter aLineItem to get to the CompositePOSTransaction, Item, etc.
  /**
   * This method is used to calculate discount amount
   * @param aLineItem POSLineItem
   * @param aBaseMarkdownAmount Currency
   * @return Currency
   */
  public ArmCurrency calculateDiscountAmount(POSLineItem aLineItem, ArmCurrency aBaseMarkdownAmount) {
	//Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
	  ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
		String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
		
		if (Rounding_Mode == null) {
			Rounding_Mode = "false";
		}
		ArmCurrency discountAmount=null;
		double netAmount;
		
	  //end Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
    ArmCurrency base = aLineItem.getItemRetailPrice();
    ArmLineDiscount lineDiscount = ((ArmLineDiscount)this.discount);
    try {
      if (this.discount.isInAdditionToMarkdown())
        base = base.subtract(aBaseMarkdownAmount);
      if (this.discount.isDiscountPercent()) {
    	  if(base.isApplyTruncation()){
    		  return base.multiply(this.getDiscountPercent(aLineItem)).roundAndTruncate(1);
    	  }
        return base.multiply(this.getDiscountPercent(aLineItem)).round();
      }
      //      if (this.discount.getAmount().lessThan(base)) {
      if (lineDiscount.getMethodOfReduction() == CMSDiscount.TOTAL_PRICE_OFF) {
        ArmCurrency remainingBalance = lineDiscount.getRemainingBalance();
    	//Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount  
    	if(Rounding_Mode.equalsIgnoreCase("true"))
        {
        	discountAmount = base.multiply(this.getDiscountPercent(aLineItem
                    , aBaseMarkdownAmount)).round();
        	netAmount=roundMode(discountAmount.doubleValue(), 0);
        	discountAmount=new ArmCurrency(netAmount);
        }
        else
        {
        	discountAmount = base.multiply(this.getDiscountPercent(aLineItem
                    , aBaseMarkdownAmount)).round();
        }
    	//end Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
        if (remainingBalance.lessThan(discountAmount)) {
          lineDiscount.setTotalDiscountTakenOff(lineDiscount.getTotalDiscountTakenOff().add(
              remainingBalance));
          return remainingBalance;
        } else {
          lineDiscount.setTotalDiscountTakenOff(lineDiscount.getTotalDiscountTakenOff().add(
              discountAmount));
          return discountAmount;
        }
      }
      return this.discount.getAmount();
      //      }
      //      return base;
    } catch (CurrencyException anException) {
      this.logCurrencyException("calcDiscountAmount", anException);
      return null;
    }
  }

  /**
   * @param lineItem
   * @param aBaseMarkdownAmount
   * @return
   */
  public double getDiscountPercent(POSLineItem lineItem, ArmCurrency aBaseMarkdownAmount) {
    ArmCurrency currTotalAmount = new ArmCurrency(0.0d);
    Discount priceDiscount = null;
    if ((((ArmLineDiscount)discount).isLineItemDiscount
         || ((ArmLineDiscount)discount).isSubTotalDiscount
        || ((ArmLineDiscount)discount).isMultiDiscount)
        && ((ArmLineDiscount)discount).getMethodOfReduction() == CMSDiscount.TOTAL_PRICE_OFF) {
//System.out.println("Percent=="+discount.getAmount()+"/"+getDiscountNetAmount(lineItem).doubleValue());
      return discount.getAmount().doubleValue()/getDiscountNetAmount(lineItem).doubleValue();
    }
    return super.getDiscountPercent(lineItem);
  }

  /**
   * @param lineItem
   * @return
   */
  public ArmCurrency getDiscountNetAmount(POSLineItem lineItem) {
	//Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
	  ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
			String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
			
			if (Rounding_Mode == null) {
				Rounding_Mode = "false";
			}
			double netAmount;
		//end Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
    CMSCompositePOSTransaction transaction = (CMSCompositePOSTransaction)lineItem.getTransaction().
        getCompositeTransaction();
//    transaction.doRemoveAllSettlementDiscounts();
    POSLineItem posLineItems[] = ((ArmLineDiscount)discount).getLineItemsArray();
    ArmCurrency totalPriceDiscount = new ArmCurrency(0.0d);
    ArmLineDiscount armLineDiscount = null;
    ArmCurrency totalNetAmount = new ArmCurrency(0.0d);
    try {
      for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
        Discount[] discounts = posLineItems[i].getDiscountsArray();
        Discount priceDiscount = null;
        ArmCurrency currDealMarkdownAmt = new ArmCurrency(0.0d);
        if (posLineItems[i] instanceof CMSSaleLineItem) {
          priceDiscount = ((CMSSaleLineItem)posLineItems[i]).getPriceDiscount();
          currDealMarkdownAmt = ((CMSSaleLineItem)posLineItems[i]).getExtendedDealMarkdownAmount();
        }
        if (posLineItems[i] instanceof CMSReturnLineItem) {
          priceDiscount = ((CMSReturnLineItem)posLineItems[i]).getPriceDiscount();
          currDealMarkdownAmt = ((CMSReturnLineItem)posLineItems[i]).getExtendedDealMarkdownAmount();
        }
        if (priceDiscount != null) {
//          System.out.println("Price Discount AMOUNT ======="+priceDiscount.getAmount());
//          totalPriceDiscount = totalPriceDiscount.add(priceDiscount.getAmount());
          totalPriceDiscount = totalPriceDiscount.add(posLineItems[i].getItemSellingPrice().subtract(currDealMarkdownAmt).subtract(
              priceDiscount.getAmount()));

        }
      }
//      boolean removeDiscount = false;
//      for (int index = 0; index < discounts.length; index++) {
//        removeDiscount = false;
//        if (discounts[index] instanceof ArmLineDiscount) {
//          ((ArmLineDiscount)discounts[index]).setTotalDiscountTakenOff(new ArmCurrency(0.0d));
//          if (((CMSDiscount)discounts[index]).getSequenceNumber()
//              == ((CMSDiscount)discount).getSequenceNumber())
//            armLineDiscount = (ArmLineDiscount)discounts[index];
//        }
//        if ((((CMSDiscount)discounts[index]).getSequenceNumber()
//            >= ((CMSDiscount)discount).getSequenceNumber()) && discounts[index] != priceDiscount)
//          removeDiscount = true;
//        if (removeDiscount)
//          posLineItems[i].doRemoveDiscount(discounts[index]);
//      }
//     if (this.discount instanceof ArmLineDiscount) {
//System.out.println("TOTAL NEt AMOUNT Before======="+((ArmLineDiscount)this.discount).getDiscountNetAmount());
    //Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
      if(Rounding_Mode.equalsIgnoreCase("true"))
 	 {
    	  totalNetAmount = ((ArmLineDiscount)this.discount).getDiscountNetAmount().subtract(totalPriceDiscount);
 		 netAmount=roundMode(totalNetAmount.doubleValue(), 0);
 		 totalNetAmount=new ArmCurrency(netAmount);
 	 }
      else
      {
    	  totalNetAmount = ((ArmLineDiscount)this.discount).getDiscountNetAmount().subtract(totalPriceDiscount);
      }
    //end Mahesh Nandure : 6 JUNE 2017 : Rounding selling price for discount
//System.out.println("TOTAL NEt AMOUNT After======="+totalNetAmount);
    } catch (CurrencyException anException) {
      this.logCurrencyException("getDiscountNetAmount", anException);
    }

//    transaction.getPriceEngine().run();
    return totalNetAmount;
  }
}


