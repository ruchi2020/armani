/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2001-2002, Retek Inc.
//


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.pos.CMSReduction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.util.LineItemPOSUtil;


/**
 *
 * <p>Title: CMSSaleLineItemDetailPriceEngine</p>
 *
 * <p>Description: CMS Sale Line Item Detail Price Engine class</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 *  @version 1.0a
 */
public class CMSSaleLineItemDetailPriceEngine extends SaleLineItemDetailPriceEngine {
  private static final long serialVersionUID = 1286522392941093345L;
  private static ConfigMgr itemCfg = new ConfigMgr("item.cfg");

  /**
   * Default Constructor
   */
  public CMSSaleLineItemDetailPriceEngine() {
  }

  /**
   * Method used to create discount price engine
   * @param aDiscount Discount
   * @return DiscountPriceEngine
   */
  public DiscountPriceEngine createDiscountPriceEngine(Discount aDiscount) {
    if (aDiscount instanceof ArmLineDiscount)
      return new ArmLineDiscountPriceEngine(aDiscount);
    return new CMSDiscountPriceEngine(aDiscount);
  }

  /**
   * Method is used to calculate reduction amount for composite transaction
   * discount
   * @param appliedReductions Vector
   * @param baseMarkdown Currency
   * @param baseMarkdownReductions Vector
   * @return Currency
   */
  public ArmCurrency calcReductionAmountForCompositeTransactionDiscount(Vector appliedReductions
      , ArmCurrency baseMarkdown, Vector baseMarkdownReductions) {
    ArmCurrency aDiscountAmount = new ArmCurrency(0.0);
    Discount[] aDiscounts = this.getCompositeTransaction().getDiscountsArray();
    try {
      aDiscountAmount = aDiscountAmount.add(baseMarkdown);
      for (int index = 0; index < aDiscounts.length; index++) {
        ArmCurrency totalAmountOff = new ArmCurrency(0.0d);
        double percentToReduce;
        CMSDiscount discounttemp = (CMSDiscount)aDiscounts[index];
        CMSDiscount discount = (CMSDiscount)discounttemp.clone();
        if (discount == null)
          continue;
        if (discount.isLineItemDiscount) {
          continue;
        }
        if (discount.isSubTotalDiscount)
          continue;
        // Handled at line item level
        if (discount instanceof CMSEmployeeDiscount)
          continue;
        if (!discount.isDiscountPercent() || discount instanceof RewardDiscount) {
          CMSCompositePOSTransaction trans = (CMSCompositePOSTransaction)this.
              getCompositeTransaction();
          double compositeTotalThresholdAmount = trans.getSaleRetailAmount().subtract(trans.
              getCompositeTotalDealMkdnAmount()).doubleValue();
          totalAmountOff = new ArmCurrency(Math.min(discount.getAmount().getDoubleValue()
              , compositeTotalThresholdAmount));
          percentToReduce = totalAmountOff.doubleValue() / compositeTotalThresholdAmount;
          discount.doSetIsDiscountPercent(true);
          discount.doSetPercent(percentToReduce);
        }
        ArmCurrency discountAmount = (this.createDiscountPriceEngine(discount)).
            calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
        aDiscountAmount = aDiscountAmount.add(discountAmount);
        if (discountAmount.equals(new ArmCurrency(0.0)))
          continue;
        CMSReduction reduction = new CMSReduction(discountAmount, discount.getType() + " Discount");
        reduction.doSetDiscount(discount);
        appliedReductions.add(reduction);
      }
      aDiscountAmount = aDiscountAmount.subtract(baseMarkdown);
    } catch (CurrencyException anException) {
      this.logCurrencyException("calcReductionAmountForCompositeTransactionDiscount", anException);
      return null;
    }
    return aDiscountAmount;
  }

  /**
   * Method used to check whether composite transaction discount is in addition
   * to marked discount
   * @param discount CMSDiscount
   * @return boolean
   */
  public boolean isCompositeTransactionDiscountInAdditionToMarkdown(CMSDiscount discount) {
    if (discount != null)
      return discount.isInAdditionToMarkdown();
    return false;
  }

  /**
   * Method used to calculate reduction amount for line item discount
   * @param appliedReductions Vector
   * @param baseMarkdown Currency
   * @param baseMarkdownReductions Vector
   * @return Currency
   */
  public ArmCurrency calcReductionAmountForLineItemDiscount(Vector appliedReductions
      , ArmCurrency baseMarkdown, Vector baseMarkdownReductions) {
    ArmCurrency aDiscountAmount = new ArmCurrency(0.0);
    //Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
    double netAmount;
    
    ArmCurrency discountAmount=null;
    
    ConfigMgr cfgMgr = new ConfigMgr("discount.cfg");
	String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
	
	if (Rounding_Mode == null) {
		Rounding_Mode = "false";
	}
	 //end Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
    Discount[] aDiscounts = this.getLineItem().getDiscountsArray();
    try {
      aDiscountAmount = aDiscountAmount.add(baseMarkdown);
      Discount priceDiscount = null;
      for (int i = 0; i < aDiscounts.length; i++) {  
        if (aDiscounts[i].getType().equals("BY_PRICE_DISCOUNT")) {
          try {
            CMSSaleLineItem saleItem = (CMSSaleLineItem)this.getLineItem();
            if (saleItem.getPriceDiscount() != null) {
              priceDiscount = (Discount)saleItem.getPriceDiscount().clone();
              priceDiscount.setAmount(saleItem.getItemSellingPrice().subtract(this.
                  getLineItemDetail().getDealMarkdownAmount()).subtract(priceDiscount.getAmount()));
              //Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
              if(Rounding_Mode.equalsIgnoreCase("true"))
              {
            	  discountAmount = (this.createDiscountPriceEngine(priceDiscount)).
                          calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
            	  netAmount=roundMode(discountAmount.doubleValue(), 0);
            	  discountAmount=new ArmCurrency(netAmount);
              }
              
              else
              {
            	  discountAmount = (this.createDiscountPriceEngine(priceDiscount)).
                          calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
              }
             
              //end Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
              aDiscountAmount = aDiscountAmount.add(discountAmount);
              if (discountAmount.equals(new ArmCurrency(0.0)))
                continue;
              CMSReduction reduction = new CMSReduction(discountAmount
                  , priceDiscount.getType() + " Discount");
              reduction.doSetDiscount(priceDiscount);
              appliedReductions.add(reduction);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      for (int index = 0; index < aDiscounts.length; index++) {
        CMSSaleLineItem saleItem = (CMSSaleLineItem)this.getLineItem();
        CMSDiscount discount = (CMSDiscount)aDiscounts[index];
        if (discount == null)
          continue;
        if (saleItem.getPriceDiscount() == discount)
          continue;
        // added by nupura
        if (discount instanceof CMSEmployeeDiscount
            && !((CMSEmployeeDiscount)discount).getIsOverridden()) {
          ArmCurrency retailPrice = saleItem.getExtendedRetailAmount();
          ArmCurrency sellingPrice = saleItem.getItem().getSellingPrice().multiply(saleItem.
              getQuantity().intValue());
          sellingPrice = sellingPrice.subtract(((CMSSaleLineItem)saleItem).
              getExtendedDealMarkdownAmount());
          ArmCurrency markdown = retailPrice.subtract(sellingPrice);
          // if markdown greater than zero, item is on markdown
          if (markdown.greaterThan(new ArmCurrency(0.0d))) {
            double promoPercent = ((CMSEmployeeDiscount)discount).getPromoDiscountPercent();
            ((CMSEmployeeDiscount)discount).doSetPercent(promoPercent);
          } else {
            ((CMSEmployeeDiscount)discount).doSetPercent(((CMSEmployeeDiscount)discount).
                getNormalDiscountPrecent());
          }
        }
        //ends here
        //Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
        if(Rounding_Mode.equalsIgnoreCase("true"))
        {
        	 discountAmount = (this.createDiscountPriceEngine(discount)).
     	            calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
        	 netAmount=roundMode(discountAmount.doubleValue(), 0);
        	 discountAmount=new ArmCurrency(netAmount);

        }
        else
        {
        	   discountAmount = (this.createDiscountPriceEngine(discount)).
        	            calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
        }
        // end Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
        if (discount instanceof ArmLineDiscount && discount.getMethodOfReduction() == CMSDiscount.TOTAL_PRICE_OFF) {
          ArmLineDiscount lineDiscount = (ArmLineDiscount)discount;
          POSLineItemDetail[] lineItemDetails = this.getSortedLineItemDetailArray(lineDiscount);
          if (lineItemDetails[lineItemDetails.length-1] == this.getLineItemDetail()) {
           discountAmount = discountAmount.add(lineDiscount.getRemainingBalance());
          }
         }

        aDiscountAmount = aDiscountAmount.add(discountAmount);
        if (discountAmount.equals(new ArmCurrency(0.0)))
          continue;
        CMSReduction reduction = new CMSReduction(discountAmount, discount.getType() + " Discount");
        reduction.doSetDiscount(discount);
        appliedReductions.add(reduction);
      }
      //Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
      if(Rounding_Mode.equalsIgnoreCase("true"))
      {
    	 aDiscountAmount = aDiscountAmount.subtract(baseMarkdown);
      	 netAmount=roundMode(aDiscountAmount.doubleValue(), 0);
      	 aDiscountAmount=new ArmCurrency(netAmount);
      }
      else
      {
    	  aDiscountAmount = aDiscountAmount.subtract(baseMarkdown);
      }
      //end Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
    } catch (CurrencyException anException) {
      this.logCurrencyException("calcReductionAmountForLineItemDiscount", anException);
      return null;
    }
    return aDiscountAmount;
  }

  //Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
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
  //end Mahesh Nandure : 5 JUN 2017 : Rounding selling price for discount
  /**
   * Method is used to calculate total applied reduction
   * @param appliedReductions Vector
   * @return Currency
   */
  public ArmCurrency calcTotalReduction(Vector appliedReductions) {
    Vector appliedReductions1 = new Vector();
    Vector appliedReductions2 = new Vector();
    Vector appliedReductions3 = new Vector();
    ArmCurrency totalReduction = new ArmCurrency(0.0d);
    Item item = getLineItem().getItem();
    //Do not give discounts if the item is a gift card or a misc sale item
    if ((item.isRedeemable())
        || ((MiscItemManager.getInstance().isMiscItem(item.getId()))
        && !LineItemPOSUtil.isNotOnFileItem(item.getId()))) {
      totalReduction = new ArmCurrency(0.0);
      return totalReduction;
    }
    try {
      ArmCurrency reduction1 = this.getBaseMarkdownAmount(appliedReductions1);
      ArmCurrency reduction2 = this.calcReductionAmountForCompositeTransactionDiscount(
          appliedReductions2, reduction1, appliedReductions1);
      reduction1 = reduction1.add(reduction2);
      ArmCurrency reduction3 = this.calcReductionAmountForLineItemDiscount(appliedReductions3
          , reduction1, appliedReductions1);
      //      if (reduction1.greaterThan(reduction2))
      //        if (reduction1.greaterThan(reduction3))
      //        {
      //          appliedReductions.addAll(appliedReductions1);
      //          return reduction1;
      //        }
      //        else
      //        {
      //          appliedReductions.addAll(appliedReductions3);
      //          return reduction3;
      //        }
      //        else if (reduction2.greaterThan(reduction3))
      //        {
      //          appliedReductions.addAll(appliedReductions2);
      //          return reduction2;
      //        }
      //        else
      //        {
      //          appliedReductions.addAll(appliedReductions3);
      //          return reduction3;
      //        }
      appliedReductions.addAll(appliedReductions1);
      appliedReductions.addAll(appliedReductions2);
      appliedReductions.addAll(appliedReductions3);
      totalReduction = totalReduction.add(reduction1);
      totalReduction = totalReduction.add(reduction3);
    } catch (CurrencyException anException) {
      this.logCurrencyException("calcTotalReduction", anException);
      return null;
    }
    return totalReduction;
  }

  /**
   * Method used to check whether line item discount is in addition to marked
   * discount
   * @param discount CMSDiscount
   * @return boolean
   */
  public boolean isLineItemDiscountInAdditionToMarkdown(CMSDiscount discount) {
    if (discount != null)
      return discount.isInAdditionToMarkdown();
    return false;
  }

  public POSLineItemDetail[] getSortedLineItemDetailArray(ArmLineDiscount lineDiscount) {
    ArrayList list = new ArrayList();
    for (Iterator aLineItemGroupingList = this.getLineItem().getTransaction().getLineItemGroupings();
        aLineItemGroupingList.hasNext(); ) {
      POSLineItemGrouping aLineItemGrouping = (POSLineItemGrouping)aLineItemGroupingList.next();
      ArrayList lineItemDetails = aLineItemGrouping.getLineItemDetailsArrayList();
      for (Iterator iterator = lineItemDetails.iterator(); iterator.hasNext(); ) {
        POSLineItemDetail saleLineItemDetail = (POSLineItemDetail)iterator.next();
        POSLineItemDetail[] discLineItemDetails = lineDiscount.getLineItemDetailArray();
        for (int index = 0; index < discLineItemDetails.length; index++) {
          if (saleLineItemDetail == discLineItemDetails[index]) {
            list.add(discLineItemDetails[index]);
            break;
          }
        }
      }
    }
    return (POSLineItemDetail[])list.toArray(new POSLineItemDetail[0]);
  }

}

