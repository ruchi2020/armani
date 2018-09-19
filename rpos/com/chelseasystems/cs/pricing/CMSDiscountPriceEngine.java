/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;


/**
 *
 * <p>Title: CMSDiscountPriceEngine</p>
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
public class CMSDiscountPriceEngine extends DiscountPriceEngine {

  /**
   * Constructor
   * @param aDiscount Discount
   */
  public CMSDiscountPriceEngine(Discount aDiscount) {
    super(aDiscount);
  }

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
    ArmCurrency base = aLineItem.getItemRetailPrice();
  //Mahesh Nandure : 21 APR 2017 : Rounding selling price for discount
    ArmCurrency total = null;
    ArmCurrency discountAmount = null;
    ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
	String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
	
	if (Rounding_Mode == null) {
		Rounding_Mode = "false";
	}
	//end Mahesh Nandure code 21 APR 2017
    try {
      if (this.discount.isInAdditionToMarkdown())
        base = base.subtract(aBaseMarkdownAmount);
      if (this.discount.isDiscountPercent()){
    	  if(base.isApplyTruncation()){
    		  return base.multiply(this.getDiscountPercent(aLineItem)).roundAndTruncate(1);
    	  }
    	//Mahesh Nandure : 21 APR 2017 : Rounding selling price for discount
    	  if(Rounding_Mode.equalsIgnoreCase("true"))
    	  {
    		  double discountPrice = roundMode(base.multiply(this.getDiscountPercent(aLineItem)).doubleValue(),0);
	          total=new ArmCurrency(discountPrice);
	          return total;
    	  }//end Mahesh Nandure code 21 APR 2017
        return base.multiply(this.getDiscountPercent(aLineItem)).round();
      }
      if (this.discount.getAmount().lessThan(base))
    	//Mahesh Nandure : 21 APR 2017 : Rounding selling price for discount
    	   if(Rounding_Mode.equalsIgnoreCase("true"))
    	  {
    		  double discountPrice = roundMode(this.discount.getAmount().doubleValue(),0);
    		  discountAmount=new ArmCurrency(discountPrice);
	          return discountAmount;
    	  }else{
    		  return this.discount.getAmount();
    	  }//end Mahesh Nandure code 21 APR 2017
      return base;
    } catch (CurrencyException anException) {
      this.logCurrencyException("calcDiscountAmount", anException);
      return null;
    }
  }
//Mahesh Nandure : 21 APR 2017 : Rounding selling price for discount
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
}

