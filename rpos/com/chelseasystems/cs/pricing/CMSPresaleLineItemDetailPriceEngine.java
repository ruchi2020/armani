/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.item.MiscItemManager;
import com.chelseasystems.cr.pricing.DiscountPriceEngine;
import com.chelseasystems.cr.pricing.LineItemDetailPriceEngine;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import java.util.*;

import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.POSLineItemGrouping;
import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cs.discount.ArmLineDiscount;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;
import com.chelseasystems.cs.discount.RewardDiscount;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;
import com.chelseasystems.cs.pos.CMSReduction;
import com.chelseasystems.cs.util.LineItemPOSUtil;

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
public class CMSPresaleLineItemDetailPriceEngine extends LineItemDetailPriceEngine {
  /**
   * put your documentation comment here
   */
  public CMSPresaleLineItemDetailPriceEngine() {
  }

  /**
   * createDiscountPriceEngine
   *
   * @param discount Discount
   * @return DiscountPriceEngine
   * @todo Implement this
   *   com.chelseasystems.cr.pricing.LineItemDetailPriceEngine method
   */
  public DiscountPriceEngine createDiscountPriceEngine(Discount aDiscount) {
    if (aDiscount instanceof ArmLineDiscount)
      return new ArmLineDiscountPriceEngine(aDiscount);
    return new CMSDiscountPriceEngine(aDiscount);
  }

  public ArmCurrency calcItemMarkdown()
  {
      try
      {
        return getItemSellingPrice();
      }
      catch(Exception anException)
      {
          logCurrencyException("calcItemMarkdown", anException);
      }
      return null;
  }

  public void assignPricing()
  {
      Vector appliedReductions = new Vector();
      ArmCurrency aUnitPrice;
      try
      {
          ArmCurrency aTotalReduction = calcTotalReduction(appliedReductions);
          if(aTotalReduction.greaterThan(new ArmCurrency(0.0D)))
          {
              for(Enumeration aReductionsList = appliedReductions.elements();
                  aReductionsList.hasMoreElements();
                  super.getLineItemDetail().doAddReduction((Reduction)aReductionsList.nextElement()));
          }
          aUnitPrice = getItemRetailPrice().subtract(aTotalReduction);
      }
      catch(CurrencyException anException)
      {
          logCurrencyException("assignPricing", anException);
          aUnitPrice = null;
      }	
      //Addded to show actual amount due for PRESALE line item on Initial sale applet.
      super.getLineItemDetail().doSetNetAmount(aUnitPrice);
        //super.getLineItemDetail().doSetNetAmount(new ArmCurrency(0.0d));
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
    Discount[] aDiscounts = this.getLineItem().getDiscountsArray();
    try {
      aDiscountAmount = aDiscountAmount.add(baseMarkdown);
      Discount priceDiscount = null;
      for (int i = 0; i < aDiscounts.length; i++) {
        if (aDiscounts[i].getType().equals("BY_PRICE_DISCOUNT")) {
          try {
            CMSPresaleLineItem presaleItem = (CMSPresaleLineItem)this.getLineItem();
            if (presaleItem.getPriceDiscount() != null) {
              priceDiscount = (Discount)presaleItem.getPriceDiscount().clone();
              priceDiscount.setAmount(presaleItem.getItemSellingPrice().subtract(this.
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
        CMSPresaleLineItem presaleItem = (CMSPresaleLineItem)this.getLineItem();
        CMSDiscount discount = (CMSDiscount)aDiscounts[index];
        if (discount == null)
          continue;
        if (presaleItem.getPriceDiscount() == discount)
          continue;
        // added by nupura
        if (discount instanceof CMSEmployeeDiscount
            && !((CMSEmployeeDiscount)discount).getIsOverridden()) {
          ArmCurrency retailPrice = presaleItem.getExtendedRetailAmount();
          ArmCurrency sellingPrice = presaleItem.getItem().getSellingPrice().multiply(presaleItem.
              getQuantity().intValue());
          sellingPrice = sellingPrice.subtract(((CMSPresaleLineItem)presaleItem).
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
        ArmCurrency discountAmount = (this.createDiscountPriceEngine(discount)).
            calculateDiscountAmount(this.getLineItem(), aDiscountAmount);
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
        POSLineItemDetail lineItemDetail = (POSLineItemDetail)iterator.next();
        POSLineItemDetail[] discLineItemDetails = lineDiscount.getLineItemDetailArray();
        for (int index = 0; index < discLineItemDetails.length; index++) {
          if (lineItemDetail == discLineItemDetails[index]) {
            list.add(discLineItemDetails[index]);
            break;
          }
        }
      }
    }
    return (POSLineItemDetail[])list.toArray(new POSLineItemDetail[0]);
  }

}

