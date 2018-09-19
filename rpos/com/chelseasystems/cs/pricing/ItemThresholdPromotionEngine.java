/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.Version;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * put your documentation comment here
 */
public class ItemThresholdPromotionEngine extends CMSPromotionEngine {
	private static ItemThresholdPromotionEngine engine = new ItemThresholdPromotionEngine();
	CMSStore currStore = (CMSStore) AppManager.getCurrent().getGlobalObject(
			"STORE");

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public static ItemThresholdPromotionEngine getInstance() {
		return ItemThresholdPromotionEngine.engine;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param transaction
	 * @param activatedPromotion
	 */
	public void assignPromotionMarkdown(CompositePOSTransaction transaction, IActivatedPromotion activatedPromotion) {
		CMSPromotion promotion = (CMSPromotion) activatedPromotion.getPromotion();
		if (((CMSCompositePOSTransaction) transaction).getPresaleTransaction().getLineItemsArray().length > 0) {
			if (!this.isWithinPresaleStartDays(promotion))
				return;
		} else {
			if (!this.isOnPromotionTimeRange(promotion))
				return;
		}
		apply(transaction.getSaleTransaction(), activatedPromotion);
		apply(transaction.getReturnTransaction(), activatedPromotion);
		apply(transaction.getLayawayTransaction(), activatedPromotion);
		apply(((CMSCompositePOSTransaction) transaction).getPresaleTransaction(), activatedPromotion);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param posTransaction
	 * @param activatedPromotion
	 */
	private void apply(POSTransaction posTransaction, IActivatedPromotion activatedPromotion) {
		String[] itemIds = activatedPromotion.getItemIdsArray();
		List detailList = new ArrayList();
	ArmCurrency curTotalReductionAmt = new ArmCurrency(0.0d);
	ArmCurrency totalOnTransaction = new ArmCurrency(0.0d);
		double percentAmountOff = 0.0d;
		try {
			for (int i = 0; i < itemIds.length; i++) {
				POSLineItemGrouping grouping = posTransaction.getLineItemGroupingForItemId(itemIds[i]);
				if (grouping != null)
					detailList.addAll(grouping.getLineItemDetailsArrayList());
			}
			ItemThresholdPromotion promotion = (ItemThresholdPromotion) activatedPromotion.getPromotion();
			// if (promotion.isReductionByFixedUnitPrice() || promotion.isReductionByUnitPriceOff() ||
			// promotion.isReductionByPercentageOff())
			{
				POSLineItemDetail[] lineItemDetailArray = (POSLineItemDetail[]) detailList.toArray(new POSLineItemDetail[0]);
				lineItemDetailArray = this.removeNotAvailableForPromtion(lineItemDetailArray);
				lineItemDetailArray = this.sortByAmount(lineItemDetailArray);
				try {
					for (int i = 0; i < lineItemDetailArray.length; i++) {
					ArmCurrency retail = lineItemDetailArray[i].getLineItem().getItemRetailPrice();
						totalOnTransaction = totalOnTransaction.add(retail);
					}
					if (totalOnTransaction.greaterThanOrEqualTo(promotion.getTriggerAmount())) {
						if (promotion.isReductionByTotalPriceOff()) {
							int promotionCounts = calculateNumberOfTriggers(posTransaction, lineItemDetailArray, promotion, totalOnTransaction);
						ArmCurrency totalReductionAmt = promotion.getReductionAmount().multiply(promotionCounts);
						ArmCurrency currRemainingReductionAmt = totalReductionAmt;
							percentAmountOff = totalReductionAmt.doubleValue() / totalOnTransaction.doubleValue();
							for (int i = 0; i <= lineItemDetailArray.length - 2; i++) {
								ArmCurrency currAppliedReductionAmt = applyToLineItemDetail(lineItemDetailArray[i], promotion, percentAmountOff);
								currRemainingReductionAmt = currRemainingReductionAmt.subtract(currAppliedReductionAmt);
							}
							if (lineItemDetailArray.length > 0) {
								lineItemDetailArray[lineItemDetailArray.length - 1].doSetDealId(promotion.getId());
								lineItemDetailArray[lineItemDetailArray.length - 1].doSetDealMarkdownAmount(currRemainingReductionAmt);
							}
						} else {
							for (int i = 0; i < lineItemDetailArray.length; i++){
								//issue resolved for Europe- Promotion discount rounding for items 
								if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
									applyToLineItemDetailForEur(lineItemDetailArray[i], promotion, percentAmountOff);
								}else{
									applyToLineItemDetail(lineItemDetailArray[i], promotion, percentAmountOff);
								}
							}
						}
					}
				} catch (CurrencyException ce) {
					System.out.println("CurrencyException: " + ce);
					ce.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param lineItemDetail
	 * @param promotion
	 * @param percentToReduce
	 * @return
	 * @exception CurrencyException
	 */
	private ArmCurrency applyToLineItemDetail(POSLineItemDetail lineItemDetail, ItemThresholdPromotion promotion, double percentToReduce) throws CurrencyException {
		ArmCurrency markDown = null;
		if (promotion.isReductionByFixedUnitPrice()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount()))
				markDown = new ArmCurrency(0.0);
			else
				markDown = retail.subtract(promotion.getReductionAmount());
		} else if (promotion.isReductionByTotalPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			markDown = retail.multiply(percentToReduce);
		} else if (promotion.isReductionByUnitPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount())) {
				markDown = retail;
			} else {
				markDown = promotion.getReductionAmount();
			}
		} else if (promotion.isReductionByPercentageOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
		ArmCurrency tempMarkdown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
			if (retail.lessThan(tempMarkdown)) {
				markDown = retail;
			} else {
					markDown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
					//Vivek Mishra : Added condition for Armani Exchange US stores in order to stop rounding of the selling price 
					double sellingPrice = 0.0d;
					String storeBrand = currStore.getBrandID();
					//Vivek Mishra : Changed the condition to include all the brands having AX in the brand id as asked by Jason 09-MAT-2016
					//if("US".equalsIgnoreCase(Version.CURRENT_REGION) && (storeBrand.equalsIgnoreCase("AX") ||storeBrand.equalsIgnoreCase("GAC"))){
					if("US".equalsIgnoreCase(Version.CURRENT_REGION) && ((storeBrand.toUpperCase()).contains("AX") ||storeBrand.equalsIgnoreCase("GAC"))){
						sellingPrice = lineItemDetail.getLineItem().getItemRetailPrice().subtract(markDown).doubleValue();
					}else{//Ends
					    sellingPrice = Math.floor(lineItemDetail.getLineItem().getItemRetailPrice().subtract(markDown).doubleValue());
					}					
					markDown = lineItemDetail.getLineItem().getItemRetailPrice().subtract(new ArmCurrency(sellingPrice));
				}
			} else {
			System.out.println("Invalid promotion reduction method on " + promotion);
			return new ArmCurrency(0.0d);
		}
		lineItemDetail.doSetDealId(promotion.getId());
		lineItemDetail.doSetDealMarkdownAmount(markDown);
		return markDown;
	}

	public static double roundMode(double value, int places)
	{
		if (places < 0) throw new IllegalArgumentException();
		
		
		BigDecimal bd = new BigDecimal(value);
		//Mahesh Nandure : 02 FEB 2017 : Rounding selling price By promotion for Europe
		ConfigMgr cfgMgr = new ConfigMgr("promotion.cfg");
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
		}//end Mahesh Nandure code 02 FEB 2017
		}
		
		return bd.doubleValue();
		
	}
	//This method was created to resolve issue for Europe- Promotion discount rounding for items 
	private ArmCurrency applyToLineItemDetailForEur(POSLineItemDetail lineItemDetail, ItemThresholdPromotion promotion, double percentToReduce) throws CurrencyException {
		ArmCurrency markDown = null;
		//Mahesh Nandure : 02 FEB 2017 : Rounding selling price By promotion for Europe
		
				ConfigMgr cfgMgr = new ConfigMgr("promotion.cfg");
				String Rounding_Mode=cfgMgr.getString("ROUNDING_SELLING_PRICE");
				
				if (Rounding_Mode == null) {
					Rounding_Mode = "false";
				} //end Mahesh Nandure code 02 FEB 2017
				
		if (promotion.isReductionByFixedUnitPrice()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount()))
				markDown = new ArmCurrency(0.0);
			else
				markDown = retail.subtract(promotion.getReductionAmount());
		} else if (promotion.isReductionByTotalPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			markDown = retail.multiply(percentToReduce);
		} else if (promotion.isReductionByUnitPriceOff()) {
		ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			if (retail.lessThan(promotion.getReductionAmount())) {
				markDown = retail;
			} else {
				markDown = promotion.getReductionAmount();
			}
		} else if (promotion.isReductionByPercentageOff()) {
			ArmCurrency retail = lineItemDetail.getLineItem().getItemRetailPrice();
			ArmCurrency tempMarkdown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
			if (retail.lessThan(tempMarkdown)) {
				markDown = retail;
			} else {
				markDown = lineItemDetail.getLineItem().getItemRetailPrice().multiply(promotion.getReductionPercent());
				//Mahesh Nandure : 02 FEB 2017 : Rounding selling price By promotion for Europe
				if(Rounding_Mode.equalsIgnoreCase("true"))
				{
                double sellingPrice = roundMode(lineItemDetail.getLineItem().getItemRetailPrice().subtract(markDown).doubleValue(), 0);
				//double sellingPrice = Math.floor(lineItemDetail.getLineItem().getItemRetailPrice().subtract(markDown).doubleValue());
				markDown = lineItemDetail.getLineItem().getItemRetailPrice().subtract(new ArmCurrency(sellingPrice));
				}
			}
		} else {
			System.out.println("Invalid promotion reduction method on " + promotion);
			return new ArmCurrency(0.0d);
		}
		lineItemDetail.doSetDealId(promotion.getId());
		lineItemDetail.doSetDealMarkdownAmount(markDown.round());
		return markDown.round();
	}

	
	
	/**
	 * put your documentation comment here
	 * 
	 * @param posTransaction
	 * @param lineItemDetailArray
	 * @param promotion
	 * @param totalOnTransaction
	 * @return
	 * @exception CurrencyException
	 */
	private int calculateNumberOfTriggers(POSTransaction posTransaction, POSLineItemDetail[] lineItemDetailArray, ItemThresholdPromotion promotion, ArmCurrency totalOnTransaction)
			throws CurrencyException {
		int i = calculateNumberOfTriggersByQunatity(posTransaction, lineItemDetailArray, promotion);
		int j = calculateNumberOfTriggersByAmount(posTransaction, lineItemDetailArray, promotion, totalOnTransaction);
		return Math.max(i, j);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param posTransaction
	 * @param lineItemDetailArray
	 * @param promotion
	 * @return
	 */
	private int calculateNumberOfTriggersByQunatity(POSTransaction posTransaction, POSLineItemDetail[] lineItemDetailArray, ItemThresholdPromotion promotion) {
		if (promotion.getTriggerQuantity() <= 0 || lineItemDetailArray.length == 0)
			return 0;
		int count = 0;
		int quantityOnTransaction = posTransaction.getTotalQuantityOfItems();
		for (int i = 0; i < lineItemDetailArray.length; i++) {
			quantityOnTransaction++;
			// quantityOnTransaction -= promotion.getTriggerQuantity();
			if (quantityOnTransaction < 0)
				break;
			count++;
		}
		count = (int) (quantityOnTransaction / promotion.getTriggerQuantity());
		return count;
	}

	private int calculateNumberOfTriggersByAmount(POSTransaction posTransaction, POSLineItemDetail[] lineItemDetailArray, ItemThresholdPromotion promotion, ArmCurrency totalOnTransaction)
			throws CurrencyException {
		if (promotion.getTriggerAmount().doubleValue() <= 0.0 || lineItemDetailArray.length == 0)
			return 0;
		int count = 0;
		count = (int) (totalOnTransaction.doubleValue() / promotion.getTriggerAmount().doubleValue());
		return count;
	}
}
