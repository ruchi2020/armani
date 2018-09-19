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
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSReduction;
import java.util.Vector;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Iterator;
import java.util.ArrayList;
import com.chelseasystems.cs.util.LineItemPOSUtil;


/**
 *  CMS Return Line Item Detail Price Engine class.
 *  @author (rev. a) Mark Mayfield
 *  @version 1.0a
 */
public class CMSReturnLineItemDetailPriceEngine extends ReturnLineItemDetailPriceEngine {
  private static final long serialVersionUID = 8963033714451901684L;
  private static ConfigMgr itemCfg = new ConfigMgr("item.cfg");

  /**
   * put your documentation comment here
   */
  public CMSReturnLineItemDetailPriceEngine() {
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
   * put your documentation comment here
   * @param appliedReductions
   * @param baseMarkdown
   * @param baseMarkdownReductions
   * @return
   */
  public ArmCurrency calcReductionAmountForCompositeTransactionDiscount(Vector appliedReductions
      , ArmCurrency baseMarkdown, Vector baseMarkdownReductions) {
    Item item = getLineItem().getItem();
    //Do not give discounts if the item is a gift card or a misc sale item
    if ((item.isRedeemable()) || (MiscItemManager.getInstance().isMiscItem(item.getId()))) {
      return new ArmCurrency(0.0);
    }
    ArmCurrency aDiscountAmount = new ArmCurrency(0.0);
    Discount[] aDiscounts = this.getCompositeTransaction().getDiscountsArray();
    try {
      aDiscountAmount = aDiscountAmount.add(baseMarkdown);
      for (int index = 0; index < aDiscounts.length; index++) {
        ArmCurrency totalAmountOff = new ArmCurrency(0.0d);
        double percentToReduce;
        CMSDiscount discount = (CMSDiscount)aDiscounts[index];
        if (discount == null)
          continue;
        if (discount.isLineItemDiscount) {
          continue;
        }
        if (discount.isSubTotalDiscount)
          continue;
        if (!discount.isDiscountPercent() || discount instanceof RewardDiscount) {
          continue;
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
   * Method used to calculate reduction amount for line item discount
   * @param appliedReductions Vector
   * @param baseMarkdown Currency
   * @param baseMarkdownReductions Vector
   * @return Currency
   */
  public ArmCurrency calcReductionAmountForLineItemDiscount(Vector appliedReductions
      , ArmCurrency baseMarkdown, Vector baseMarkdownReductions) {
    ArmCurrency aDiscountAmount = new ArmCurrency(0.0);
    Discount[] aDiscounts = this.getLineItem().getDiscountsArray();
    try {
      aDiscountAmount = aDiscountAmount.add(baseMarkdown);
      Discount priceDiscount = null;
      for (int i = 0; i < aDiscounts.length; i++) {
        if (aDiscounts[i].getType().equals("BY_PRICE_DISCOUNT")) {
          try {
            CMSReturnLineItem returnItem = (CMSReturnLineItem)this.getLineItem();
            if (returnItem.getPriceDiscount() != null) {
              priceDiscount = (Discount)returnItem.getPriceDiscount().clone();
              priceDiscount.setAmount(returnItem.getItemSellingPrice().subtract(this.
                  getLineItemDetail().getDealMarkdownAmount()).subtract(priceDiscount.getAmount()));
              ArmCurrency discountAmount = (this.createDiscountPriceEngine(priceDiscount)).
                  calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
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
        CMSDiscount discount = (CMSDiscount)aDiscounts[index];
        CMSReturnLineItem returnItem = (CMSReturnLineItem)this.getLineItem();
        if (discount == null)
          continue;
        if (returnItem.getPriceDiscount() == discount)
          continue;
        if (discount instanceof CMSEmployeeDiscount
            && !((CMSEmployeeDiscount)discount).getIsOverridden()) {
          ArmCurrency retailPrice = returnItem.getExtendedRetailAmount();
          ArmCurrency sellingPrice = returnItem.getItem().getSellingPrice().multiply(returnItem.
              getQuantity().intValue());
          sellingPrice = sellingPrice.subtract(((CMSReturnLineItem)returnItem).
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
        ArmCurrency discountAmount = (this.createDiscountPriceEngine(discount)).
            calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
        aDiscountAmount = aDiscountAmount.add(discountAmount);
        if (discount instanceof ArmLineDiscount && discount.getMethodOfReduction() == CMSDiscount.TOTAL_PRICE_OFF) {
          ArmLineDiscount lineDiscount = (ArmLineDiscount)discount;
          POSLineItemDetail[] lineItemDetails = this.getSortedLineItemDetailArray(lineDiscount);
          if (lineItemDetails[lineItemDetails.length-1] == this.getLineItemDetail())
            discountAmount = discountAmount.add(lineDiscount.getRemainingBalance());
        }
        if (discountAmount.equals(new ArmCurrency(0.0)))
          continue;
        CMSReduction reduction = new CMSReduction(discountAmount, discount.getType() + " Discount");
        reduction.doSetDiscount(discount);
        appliedReductions.add(reduction);
      }
      aDiscountAmount = aDiscountAmount.subtract(baseMarkdown);
    } catch (CurrencyException anException) {
      this.logCurrencyException("calcReductionAmountForLineItemDiscount", anException);
      return null;
    }
    return aDiscountAmount;
  }

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

