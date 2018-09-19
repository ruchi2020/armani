/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.discount.CMSDiscountMgr;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.util.ArmConfigLoader;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;

/**
 * put your documentation comment here
 */
public class GroupedReceiptLine implements Comparable {

	public static ConfigMgr config = new ConfigMgr("discount.cfg");

	public static ConfigMgr itemCfg = new ConfigMgr("item.cfg");

	public static String normalPercent = config
			.getString("EMPLOYEE.DISCOUNT_PCT");

	public static String promoPercent = config
			.getString("EMPLOYEE.PROMO_DISCOUNT_PCT");


	
	//
	// Static methods
	//
	public static GroupedReceiptLine[] createGroupLineItemArrayToPrintForReservationClose(
			CMSCompositePOSTransaction transaction) {
		List list = new ArrayList();
		POSLineItem[] saleLineItems = transaction.getSaleLineItemsArray();
		for (int i = 0; i < saleLineItems.length; i++) {
			list.addAll(Arrays.asList(GroupedReceiptLine
					.createGroupedReceiptLine(saleLineItems[i])));
		}


		// filter all linked alterations for Japan as they would be available
		// via different method
		// also filter all open-deposit for Japan which would be made available
		// via another method
		// if (transaction.getStore().getPreferredISOCountry().equals("JP")) {
		if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
			List filteredList = new ArrayList();
			String unlinkedAlterationDesc = ResourceManager.getResourceBundle()
					.getString(
							(new ConfigMgr("item.cfg"))
									.getString("ALTERATION.MISC_ITEM_DESC"));
			String unlinkedAlterationDescChoices = ResourceManager
					.getResourceBundle().getString(
							(new ConfigMgr("item.cfg"))
									.getString("ALTERATION.DESC_CHOICES"));
			for (int i = 0; i < list.size(); i++) {
				if (((GroupedReceiptLine) list.get(i)).getItemId().equals(
						(new ConfigMgr("item.cfg"))
								.getString("ALTERATION.BASE_ITEM"))
						&& !(((GroupedReceiptLine) list.get(i)).getItemDesc()
								.equalsIgnoreCase(unlinkedAlterationDesc) || unlinkedAlterationDescChoices
								.toLowerCase().indexOf(
										((GroupedReceiptLine) list.get(i))
												.getItemDesc().toLowerCase()) > -1)) {
					continue;
				} else if (LineItemPOSUtil.isDeposit(((GroupedReceiptLine) list
						.get(i)).getLineItem().getMiscItemId())) {
					continue;
				} else {
					filteredList.add(list.get(i));
				}
			}
			list = filteredList;
		}

		GroupedReceiptLine[] receiptLines = (GroupedReceiptLine[]) list
				.toArray(new GroupedReceiptLine[0]);
		if (receiptLines.length > 1) {
			// Arrays.sort(receiptLines); //sorted by names of consultants.
			for (int i = receiptLines.length - 1; i > 0; i--) {
				if (receiptLines[i].getConsultant() != null
						&& receiptLines[i - 1].getConsultant() != null) {
					if (receiptLines[i].getConsultant().equals(
							receiptLines[i - 1].getConsultant())) {
						receiptLines[i].setConsultantNull();
					}
				}
				if (receiptLines[i].getLineItem().equals(
						receiptLines[i - 1].getLineItem())) {
					receiptLines[i].setItemNull();
				}
			}
		}
		// Do not print qty if it is 1 or -1.
		// Do not print Unit Retail if qty = 1 or -1 and equal extended amt
		// for US only
		// if (transaction.getStore().getPreferredISOCountry().equals("US")) {
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
			for (int i = 0; i < receiptLines.length; i++) {
				if (receiptLines[i].getItemQty() != null) {
					int qty = receiptLines[i].getItemQty().intValue();
					if (qty == 1 || qty == -1) {
						receiptLines[i].setItemQtyNull();
						if (receiptLines[i].getUnitRetailPrice() != null) {
							double extendedSellingPrice = receiptLines[i]
									.getExtendedSellingPrice().doubleValue()
									* qty;
							double unitRetailPrice = receiptLines[i]
									.getUnitRetailPrice().doubleValue();
							if (extendedSellingPrice == unitRetailPrice) {
								receiptLines[i].setRetailPriceNull();
							}
						}
					}
				}
			}
		}
		return receiptLines;
	}
	
	
	//
	// Static methods
	//
	public static GroupedReceiptLine[] createGroupLineItemArrayToPrint(
			CMSCompositePOSTransaction transaction) {
		
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION) && transaction.isReservationClose()){
			return createGroupLineItemArrayToPrintForReservationClose(transaction);
		}
	
		List list = new ArrayList();
		POSLineItem[] saleLineItems = transaction.getSaleLineItemsArray();
		for (int i = 0; i < saleLineItems.length; i++) {
			list.addAll(Arrays.asList(GroupedReceiptLine
					.createGroupedReceiptLine(saleLineItems[i])));
		}
		POSLineItem[] returnLineItems = transaction.getReturnLineItemsArray();
		for (int i = 0; i < returnLineItems.length; i++) {
			list.addAll(Arrays.asList(GroupedReceiptLine
					.createGroupedReceiptLine(returnLineItems[i])));
		}
		POSLineItem[] consignmentLineItems = transaction
				.getConsignmentLineItemsArray();
		for (int i = 0; i < consignmentLineItems.length; i++) {
			list.addAll(Arrays.asList(GroupedReceiptLine
					.createGroupedReceiptLine(consignmentLineItems[i])));
		}
		POSLineItem[] presaleLineItems = transaction.getPresaleLineItemsArray();
		for (int i = 0; i < presaleLineItems.length; i++) {
			list.addAll(Arrays.asList(GroupedReceiptLine
					.createGroupedReceiptLine(presaleLineItems[i])));
		}
		// Added for Reservation
		POSLineItem[] reservationLineItems = transaction
				.getReservationLineItemsArray();
		for (int i = 0; i < reservationLineItems.length; i++) {
			list.addAll(Arrays.asList(GroupedReceiptLine
					.createGroupedReceiptLine(reservationLineItems[i])));
		}

		POSLineItem[] voidLineItems = transaction.getVoidLineItemsArray();
		if(("JP".equalsIgnoreCase(Version.CURRENT_REGION) && voidLineItems.length >0))
		for (int i = 0; i < voidLineItems.length; i++) {
			list.addAll(Arrays.asList(GroupedReceiptLine
					.createGroupedReceiptLine(voidLineItems[i])));
		}
	
		
		// filter all linked alterations for Japan as they would be available
		// via different method
		// also filter all open-deposit for Japan which would be made available
		// via another method
		// if (transaction.getStore().getPreferredISOCountry().equals("JP")) {
		if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
			List filteredList = new ArrayList();
			String unlinkedAlterationDesc = ResourceManager.getResourceBundle()
					.getString(
							(new ConfigMgr("item.cfg"))
									.getString("ALTERATION.MISC_ITEM_DESC"));
			String unlinkedAlterationDescChoices = ResourceManager
					.getResourceBundle().getString(
							(new ConfigMgr("item.cfg"))
									.getString("ALTERATION.DESC_CHOICES"));
			for (int i = 0; i < list.size(); i++) {
				if (((GroupedReceiptLine) list.get(i)).getItemId().equals(
						(new ConfigMgr("item.cfg"))
								.getString("ALTERATION.BASE_ITEM"))
						&& !(((GroupedReceiptLine) list.get(i)).getItemDesc()
								.equalsIgnoreCase(unlinkedAlterationDesc) || unlinkedAlterationDescChoices
								.toLowerCase().indexOf(
										((GroupedReceiptLine) list.get(i))
												.getItemDesc().toLowerCase()) > -1)) {
					continue;
				} else if (LineItemPOSUtil.isDeposit(((GroupedReceiptLine) list
						.get(i)).getLineItem().getMiscItemId())) {
					continue;
				} else {
					filteredList.add(list.get(i));
				}
			}
			list = filteredList;
		}

		GroupedReceiptLine[] receiptLines = (GroupedReceiptLine[]) list
				.toArray(new GroupedReceiptLine[0]);
		if (receiptLines.length > 1) {
			// Arrays.sort(receiptLines); //sorted by names of consultants.
			for (int i = receiptLines.length - 1; i > 0; i--) {
				if (receiptLines[i].getConsultant() != null
						&& receiptLines[i - 1].getConsultant() != null) {
					if (receiptLines[i].getConsultant().equals(
							receiptLines[i - 1].getConsultant())) {
						receiptLines[i].setConsultantNull();
					}
				}
				if (receiptLines[i].getLineItem().equals(
						receiptLines[i - 1].getLineItem())) {
					receiptLines[i].setItemNull();
				}
			}
		}	
		// Do not print qty if it is 1 or -1.
		// Do not print Unit Retail if qty = 1 or -1 and equal extended amt
		// for US only
		// if (transaction.getStore().getPreferredISOCountry().equals("US")) {
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
			for (int i = 0; i < receiptLines.length; i++) {
				if (receiptLines[i].getItemQty() != null) {
					int qty = receiptLines[i].getItemQty().intValue();
					if (qty == 1 || qty == -1) {
						receiptLines[i].setItemQtyNull();
						if (receiptLines[i].getUnitRetailPrice() != null) {
							double extendedSellingPrice = receiptLines[i]
									.getExtendedSellingPrice().doubleValue()
									* qty;
							double unitRetailPrice = receiptLines[i]
									.getUnitRetailPrice().doubleValue();
							if (extendedSellingPrice == unitRetailPrice) {
								receiptLines[i].setRetailPriceNull();
							}
						}
					}
				}
			}
		}
		return receiptLines;
	}

	public static GroupedReceiptLine[] createGroupLineItemArrayToPrintForAlteration(
			CMSCompositePOSTransaction transaction) {
		GroupedReceiptLine[] groupedReceiptLines = createGroupLineItemArrayToPrint(transaction);
		List newGroupedReceiptLineList = new ArrayList();
		int reductionOrder = 0;
		for (int i = 0; i < groupedReceiptLines.length; i++) {
			// filter out existing reductions (discounts)
			if (groupedReceiptLines[i].getReductionAmount() != null
					&& groupedReceiptLines[i].getReductionAmount()
							.doubleValue() != 0) {
				// reductionOrder ++;
				continue;
			}
			// else
			reductionOrder = 0;
			newGroupedReceiptLineList.add(groupedReceiptLines[i]);
			AlterationLineItemDetail[] alterationLineItemDetails = null;
			if (groupedReceiptLines[i].getLineItem() instanceof CMSSaleLineItem)
				alterationLineItemDetails = ((CMSSaleLineItemDetail) groupedReceiptLines[i]
						.getLineItem().getLineItemDetailsArray()[0])
						.getAlterationLineItemDetailArray();
			else if (groupedReceiptLines[i].getLineItem() instanceof CMSPresaleLineItem)
				alterationLineItemDetails = ((CMSPresaleLineItemDetail) groupedReceiptLines[i]
						.getLineItem().getLineItemDetailsArray()[0])
						.getAlterationLineItemDetailArray();
			if (alterationLineItemDetails != null
					&& alterationLineItemDetails.length > 0) {
				for (int j = 0; j < alterationLineItemDetails.length; j++) {
					AlterationDetail[] alterationDetails = alterationLineItemDetails[j]
							.getAlterationDetailsArray();
					for (int k = 0; k < alterationDetails.length; k++) {
						alterationDetails[k].getDescription();
						alterationDetails[k].getEstimatedPrice();
						GroupedReceiptLine groupedReceiptLine = new GroupedReceiptLine(
								groupedReceiptLines[i].getLineItem(),
								alterationDetails[k].getDescription(),
								alterationDetails[k].getEstimatedPrice(),
								reductionOrder);
						if (reductionOrder == 0)
							newGroupedReceiptLineList
									.remove(groupedReceiptLines[i]);
						else {
							groupedReceiptLine.setItemNull();
							groupedReceiptLine.setConsultantNull();
							groupedReceiptLine.setItemQtyNull();
							groupedReceiptLine.setRetailPriceNull();
						}
						newGroupedReceiptLineList.add(groupedReceiptLine);
						reductionOrder++;
					}
				}
			}
		}
		return (GroupedReceiptLine[]) newGroupedReceiptLineList
				.toArray(new GroupedReceiptLine[newGroupedReceiptLineList
						.size()]);
	}

	public static GroupedReceiptLine[] createGroupLineItemArrayToPrintForOpenDeposit(
			CMSCompositePOSTransaction txn) {
		POSLineItem[] posLineItems = txn.getDepositLineItems();
		List list = new ArrayList();
		for (int i = 0; i < posLineItems.length; i++) {
			if (posLineItems[i].getItem().getId().equals(
					(new ConfigMgr("item.cfg"))
							.getString("OPEN_DEPOSIT.BASE_ITEM"))) {
				list.addAll(Arrays.asList(GroupedReceiptLine
						.createGroupedReceiptLine(posLineItems[i])));
			}
		}
		return (GroupedReceiptLine[]) list.toArray(new GroupedReceiptLine[list
				.size()]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param lineItem
	 * @return
	 */
	public static GroupedReceiptLine[] createGroupedReceiptLine(
			POSLineItem lineItem) {
		try {
			if (lineItem instanceof CMSSaleLineItem) {
				return GroupedReceiptLine
						.createGroupedReceiptLineForSale((CMSSaleLineItem) lineItem);
			} else if (lineItem instanceof CMSReturnLineItem) {
				return GroupedReceiptLine
						.createGroupedReceiptLineForReturn((CMSReturnLineItem) lineItem);
			} else if (lineItem instanceof CMSConsignmentLineItem) {
				return GroupedReceiptLine
						.createGroupedReceiptLineForConsignment((CMSConsignmentLineItem) lineItem);
			} else if (lineItem instanceof CMSPresaleLineItem) {
				return GroupedReceiptLine
						.createGroupedReceiptLineForPresale((CMSPresaleLineItem) lineItem);
			} else if (lineItem instanceof CMSReservationLineItem) {
				return GroupedReceiptLine
						.createGroupedReceiptLineForReservation((CMSReservationLineItem) lineItem);
			} else {
				return new GroupedReceiptLine[0];
			}
		} catch (Exception exp) {
			System.out.println("Excpetion in GroupedReceiptLine: " + exp);
			System.out.println("Excpetion with "
					+ lineItem.getClass().getName());
			exp.printStackTrace();
			return new GroupedReceiptLine[0];
		}
	}

	// Sale
	private static GroupedReceiptLine[] createGroupedReceiptLineForSale(
			CMSSaleLineItem lineItem) {
		ResourceBundle resource = ResourceManager.getResourceBundle();
		List list = new ArrayList();
		double dRetailPrice = 0.00;
		double dPromotionalMarkdown = 0.00;
		double dPercentage;
		try {
			Map map = new HashMap();
			POSLineItemDetail[] details = lineItem.getLineItemDetailsArray();
			if (details != null && details.length > 0) {
				for (int i = 0; i < details.length; i++) {
					for (Enumeration enm = details[i].getReductions(); enm
							.hasMoreElements();) {
						Reduction reduction = (Reduction) enm.nextElement();
						String reason = reduction.getReason();
						if (!reason.toUpperCase().trim().endsWith("DISCOUNT")) {
							if (map.containsKey(reason)) {
								try {
									ArmCurrency existAmount = (ArmCurrency) map
											.get(reason);
									map.put(reason, existAmount.add(reduction
											.getAmount()));
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								map.put(reason, reduction.getAmount());
							}
						}
					}
				}
			}
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				ArmCurrency rAmt = (ArmCurrency) map.get(key);

				// Fix for 1303 starts here: Items that are manually discounted
				// by the cashier correctly
				// reads "Promotion 40%." Items that were marked down by the
				// buyers and loaded
				// to the stores read "Deal Markdown." We need to change this
				// wording to read "Promotion ??%".
				String reductionReason = key;
				reductionReason = reductionReason.toUpperCase().trim();
				if (reductionReason.endsWith("MARKDOWN")) {
					LineItemPOSUtil util = new LineItemPOSUtil(lineItem);
					if (util.hasMarkDown()) {
						// System.out.println("ITEM : " +
						// lineItem.getItem().getId() + " HAS MARKDOWN");
						dRetailPrice = lineItem.getItemRetailPrice()
								.doubleValue();
						dPromotionalMarkdown = util.getPromotionalMarkdown()
								.doubleValue();
						dPercentage = (dPromotionalMarkdown / dRetailPrice);
						key = Math.round(dPercentage * 100) + "% "
								+ resource.getString(reductionReason);
						rAmt = util.getPromotionalMarkdown();
					}
				}
				// Fix for 1303 ends here
				list.add(new GroupedReceiptLine(lineItem, key, rAmt, -2));
			}
			// to take care the discount kind of reduction
			Discount[] txnDiscountArray = GroupedReceiptLine
					.getTransactionDiscount(lineItem.getTransaction()
							.getCompositeTransaction());
			Discount[] lineDiscountArray = lineItem.getDiscountsArray();
			List discountList = new ArrayList(Arrays.asList(lineDiscountArray));
			for (int i = 0; i < txnDiscountArray.length; i++) {
				if (!discountList.contains(txnDiscountArray[i])) {
					discountList.add(txnDiscountArray[i]);
				}
			}
			Reduction[] reductionsOfFirstDetail = details[0]
					.getReductionsArray();
			for (int i = 0; i < reductionsOfFirstDetail.length; i++) {
				String reductionReason = reductionsOfFirstDetail[i].getReason()
						.toUpperCase().trim();
				if (reductionReason.endsWith("DISCOUNT")) {
					for (Iterator it = discountList.iterator(); it.hasNext();) {
						Discount discount = (Discount) it.next();
						String trueDiscountReason = GroupedReceiptLine
								.getTrueDiscountReason(discount);
						String discountReason = discount.getReason()
								.toUpperCase();
						if (discountReason.length() == 0) {
							discountReason = discount.getType().toUpperCase();
						}
						if (discountReason.endsWith("DISCOUNT")) {
							discountReason = discountReason.substring(0,
									discountReason.lastIndexOf("DISCOUNT"))
									.trim();
						}
						if (trueDiscountReason.endsWith("DISCOUNT")) {
							trueDiscountReason = trueDiscountReason.substring(
									0,
									trueDiscountReason.lastIndexOf("DISCOUNT"))
									.trim();
						}
						if (reductionReason.startsWith(discountReason)
								|| reductionReason
										.startsWith(trueDiscountReason)) {
							discountList.remove(discount);
							String reasonLabel = fromatReductionReason(
									discount, reductionsOfFirstDetail[i]
											.getReason());

							if (reductionReason
									.equalsIgnoreCase("EMPLOYEE DISCOUNT")) {
								LineItemPOSUtil util = new LineItemPOSUtil(
										lineItem);
								if (!util.hasMarkDown()) {
									// System.out.println("ITEM : " +
									// lineItem.getItem().getId() + " HAS NO
									// MARKDOWN");
									dPercentage = Double.valueOf(
											normalPercent.trim()).doubleValue();
									reasonLabel = Math.round(dPercentage * 100)
											+ "% " + discountReason;
								} else {
									// System.out.println("ITEM : " +
									// lineItem.getItem().getId() + " HAS
									// MARKDOWN");
									dPercentage = Double.valueOf(
											promoPercent.trim()).doubleValue();
									reasonLabel = Math.round(dPercentage * 100)
											+ "% " + discountReason;
								}
							}
							if (!discount.getType().equals("BY_PRICE_DISCOUNT")) {
								ArmCurrency reductionAmount = reductionsOfFirstDetail[i]
										.getAmount().multiply(
												lineItem.getQuantity()
														.intValue());
								list.add(new GroupedReceiptLine(lineItem,
										reasonLabel, reductionAmount, i));
							}
							break;
						}
					}
				}
			}
		} catch (Exception exp) {
			System.out.println("Exception --> " + exp);
			System.err.println("Exception --> " + exp);
			exp.printStackTrace();
			list = new ArrayList();
			list.add(new GroupedReceiptLine(lineItem));
		}
		// For if there is NO reduction
		if (list.size() == 0) {
			list.add(new GroupedReceiptLine(lineItem));
		}
		return (GroupedReceiptLine[]) list.toArray(new GroupedReceiptLine[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param returnLineItem
	 * @return
	 */
	private static GroupedReceiptLine[] createGroupedReceiptLineForReturn(
			CMSReturnLineItem returnLineItem) {
		ResourceBundle resource = ResourceManager.getResourceBundle();
		List list = new ArrayList();
		double dRetailPrice = 0.00;
		double dPromotionalMarkdown = 0.00;
		double dPercentage;
		try {
			Map map = new HashMap();
			POSLineItemDetail[] returnDetails = returnLineItem
					.getLineItemDetailsArray();
			if (returnDetails != null && returnDetails.length > 0) {
				for (int i = 0; i < returnDetails.length; i++) {
					for (Enumeration enm = returnDetails[i].getReductions(); enm
							.hasMoreElements();) {
						Reduction reduction = (Reduction) enm.nextElement();
						String reason = reduction.getReason();
						if (!reason.toUpperCase().trim().endsWith("DISCOUNT")) {
							if (map.containsKey(reason)) {
								try {
									ArmCurrency existAmount = (ArmCurrency) map
											.get(reason);
									map.put(reason, existAmount.add(reduction
											.getAmount()));
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								map.put(reason, reduction.getAmount());
							}
						}
					}
				}
			}
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				ArmCurrency rAmt = (ArmCurrency) map.get(key);

				// Fix for 1303 starts here: Items that are manually discounted
				// by the cashier correctly
				// reads "Promotion 40%." Items that were marked down by the
				// buyers and loaded
				// to the stores read "Deal Markdown." We need to change this
				// wording to read "Promotion ??%".
				String reductionReason = key;
				reductionReason = reductionReason.toUpperCase().trim();
				if (reductionReason.endsWith("MARKDOWN")) {
					LineItemPOSUtil util = new LineItemPOSUtil(returnLineItem);
					if (util.hasMarkDown()) {
						// System.out.println("ITEM : " +
						// returnLineItem.getItem().getId() + " HAS MARKDOWN");
						dRetailPrice = returnLineItem.getItemRetailPrice()
								.doubleValue();
						dPromotionalMarkdown = util.getPromotionalMarkdown()
								.doubleValue();
						dPercentage = (dPromotionalMarkdown / dRetailPrice);
						key = Math.round(dPercentage * 100) + "% "
								+ resource.getString(reductionReason);
						// System.out.println("MARKDOWN LABEL: " + key);
						rAmt = util.getPromotionalMarkdown();
					}
				}
				// Fix for 1303 ends here
				list.add(new GroupedReceiptLine(returnLineItem, key, rAmt, -2));
			}
			// to take care the discount kind of reduction for returns WITH
			// receipts
			if (((CMSReturnLineItemDetail) returnLineItem
					.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
				CMSSaleLineItem saleLineItem = (CMSSaleLineItem) ((CMSReturnLineItemDetail) returnLineItem
						.getLineItemDetailsArray()[0]).getSaleLineItemDetail()
						.getLineItem();
				POSLineItemDetail[] saleDetails = saleLineItem
						.getLineItemDetailsArray();
				ArrayList discountList = new ArrayList();
				Discount[] txnDiscountArray = GroupedReceiptLine
						.getTransactionDiscount(saleLineItem.getTransaction()
								.getCompositeTransaction());
				Discount[] lineDiscountArray = saleLineItem.getDiscountsArray();
				for (int i = 0; i < lineDiscountArray.length; i++) {
					discountList.add(lineDiscountArray[i]);
				}
				for (int i = 0; i < txnDiscountArray.length; i++) {
					if (!discountList.contains(txnDiscountArray[i]))
						discountList.add(txnDiscountArray[i]);
				}
				Reduction[] reductionsOfFirstDetail = saleDetails[0]
						.getReductionsArray();
				for (int i = 0; i < reductionsOfFirstDetail.length; i++) {
					String reductionReason = reductionsOfFirstDetail[i]
							.getReason().toUpperCase();
					if (reductionsOfFirstDetail[i].getReason().toUpperCase()
							.trim().endsWith("DISCOUNT")) {
						for (Iterator it = discountList.iterator(); it
								.hasNext();) {
							Discount discount = (Discount) it.next();
							String trueDiscountReason = GroupedReceiptLine
									.getTrueDiscountReason(discount);
							String discountReason = discount.getReason()
									.toUpperCase();
							if (discountReason.length() == 0) {
								discountReason = discount.getType()
										.toUpperCase();
							}
							if (discountReason.endsWith("DISCOUNT"))
								discountReason = discountReason.substring(0,
										discountReason.lastIndexOf("DISCOUNT"))
										.trim();
							if (trueDiscountReason.endsWith("DISCOUNT"))
								trueDiscountReason = trueDiscountReason
										.substring(
												0,
												trueDiscountReason
														.lastIndexOf("DISCOUNT"))
										.trim();
							if (reductionReason.startsWith(discountReason)
									|| reductionReason
											.startsWith(trueDiscountReason)) {
								discountList.remove(discount);
								String reasonLabel = fromatReductionReason(
										discount, reductionsOfFirstDetail[i]
												.getReason());

								if (reductionReason
										.equalsIgnoreCase("EMPLOYEE DISCOUNT")) {
									LineItemPOSUtil util = new LineItemPOSUtil(
											returnLineItem);
									if (!util.hasMarkDown()) {
										// System.out.println("ITEM : " +
										// returnLineItem.getItem().getId() + "
										// HAS NO MARKDOWN");
										dPercentage = Double.valueOf(
												normalPercent.trim())
												.doubleValue();
										reasonLabel = Math
												.round(dPercentage * 100)
												+ "% " + discountReason;
									} else {
										// System.out.println("ITEM : " +
										// returnLineItem.getItem().getId() + "
										// HAS MARKDOWN");
										dPercentage = Double.valueOf(
												promoPercent.trim())
												.doubleValue();
										reasonLabel = Math
												.round(dPercentage * 100)
												+ "% " + discountReason;
									}
								}
								// System.out.println("REASON LABEL FOR RETURN
								// WITH RECEIPT: " + reasonLabel);
								if (!discount.getType().equals(
										"BY_PRICE_DISCOUNT")) {
									ArmCurrency reductionAmount = reductionsOfFirstDetail[i]
											.getAmount().multiply(
													returnLineItem
															.getQuantity()
															.intValue());
									list.add(new GroupedReceiptLine(
											returnLineItem, reasonLabel,
											reductionAmount, i));
								}
								break;
							}
						}
					}
				}
			}
			// to take care the discount kind of reduction for returns WITHOUT
			// receipts
			if (returnLineItem.getSaleLineItem() == null) {
				ArrayList discountList = new ArrayList();
				Discount[] txnDiscountArray = GroupedReceiptLine
						.getTransactionDiscount(returnLineItem.getTransaction()
								.getCompositeTransaction());
				Discount[] lineDiscountArray = returnLineItem
						.getDiscountsArray();
				for (int i = 0; i < lineDiscountArray.length; i++) {
					discountList.add(lineDiscountArray[i]);
				}
				for (int i = 0; i < txnDiscountArray.length; i++) {
					if (!discountList.contains(txnDiscountArray[i]))
						discountList.add(txnDiscountArray[i]);
				}
				Reduction[] reductionsOfFirstDetail = returnDetails[0]
						.getReductionsArray();
				for (int i = 0; i < reductionsOfFirstDetail.length; i++) {
					String reductionReason = reductionsOfFirstDetail[i]
							.getReason().toUpperCase();
					if (reductionsOfFirstDetail[i].getReason().toUpperCase()
							.trim().endsWith("DISCOUNT")) {
						for (Iterator it = discountList.iterator(); it
								.hasNext();) {
							Discount discount = (Discount) it.next();
							String trueDiscountReason = GroupedReceiptLine
									.getTrueDiscountReason(discount);
							String discountReason = discount.getReason()
									.toUpperCase();
							if (discountReason.length() == 0) {
								discountReason = discount.getType()
										.toUpperCase();
							}
							if (discountReason.endsWith("DISCOUNT"))
								discountReason = discountReason.substring(0,
										discountReason.lastIndexOf("DISCOUNT"))
										.trim();
							if (trueDiscountReason.endsWith("DISCOUNT"))
								trueDiscountReason = trueDiscountReason
										.substring(
												0,
												trueDiscountReason
														.lastIndexOf("DISCOUNT"))
										.trim();
							if (reductionReason.startsWith(discountReason)
									|| reductionReason
											.startsWith(trueDiscountReason)) {
								discountList.remove(discount);
								String reasonLabel = fromatReductionReason(
										discount, reductionsOfFirstDetail[i]
												.getReason());

								if (reductionReason
										.equalsIgnoreCase("EMPLOYEE DISCOUNT")) {
									LineItemPOSUtil util = new LineItemPOSUtil(
											returnLineItem);
									if (!util.hasMarkDown()) {
										// System.out.println("ITEM : " +
										// returnLineItem.getItem().getId() + "
										// HAS NO MARKDOWN");
										dPercentage = Double.valueOf(
												normalPercent.trim())
												.doubleValue();
										reasonLabel = Math
												.round(dPercentage * 100)
												+ "% " + discountReason;
									} else {
										// System.out.println("ITEM : " +
										// returnLineItem.getItem().getId() + "
										// HAS MARKDOWN");
										dPercentage = Double.valueOf(
												promoPercent.trim())
												.doubleValue();
										reasonLabel = Math
												.round(dPercentage * 100)
												+ "% " + discountReason;
									}
								}
								// System.out.println("REASON LABEL FOR RETURN
								// WITHOUT RECEIPT: " + reasonLabel);
								ArmCurrency reductionAmount = reductionsOfFirstDetail[i]
										.getAmount().multiply(
												returnLineItem.getQuantity()
														.intValue());
								list.add(new GroupedReceiptLine(returnLineItem,
										reasonLabel, reductionAmount, i));
								break;
							}
						}
					}
				}
			}
		} catch (Exception exp) {
			System.out.println("Exception --> " + exp);
			System.err.println("Exception --> " + exp);
			exp.printStackTrace();
			list = new ArrayList();
			list.add(new GroupedReceiptLine(returnLineItem));
		}
		// For if there is NO reduction
		if (list.size() == 0) {
			list.add(new GroupedReceiptLine(returnLineItem));
		}
		return (GroupedReceiptLine[]) list.toArray(new GroupedReceiptLine[0]);
	}

	// Consignments
	private static GroupedReceiptLine[] createGroupedReceiptLineForConsignment(
			CMSConsignmentLineItem lineItem) {
		List list = new ArrayList();
		list.add(new GroupedReceiptLine(lineItem));
		return (GroupedReceiptLine[]) list.toArray(new GroupedReceiptLine[0]);
	}

	// CMSPresaleLineItem
	private static GroupedReceiptLine[] createGroupedReceiptLineForPresale(
			CMSPresaleLineItem lineItem) {
		ResourceBundle resource = ResourceManager.getResourceBundle();
		List list = new ArrayList();
		double dRetailPrice = 0.00;
		double dPromotionalMarkdown = 0.00;
		double dPercentage;
		Map map = new HashMap();
		POSLineItemDetail[] details = lineItem.getLineItemDetailsArray();
		if (details != null && details.length > 0) {
			for (int i = 0; i < details.length; i++) {
				for (Enumeration enm = details[i].getReductions(); enm
						.hasMoreElements();) {
					Reduction reduction = (Reduction) enm.nextElement();
					String reason = reduction.getReason();
					if (!reason.toUpperCase().trim().endsWith("DISCOUNT")) {
						if (map.containsKey(reason)) {
							try {
								ArmCurrency existAmount = (ArmCurrency) map
										.get(reason);
								map.put(reason, existAmount.add(reduction
										.getAmount()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							map.put(reason, reduction.getAmount());
						}
					}
				}
			}
		}
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			ArmCurrency rAmt = (ArmCurrency) map.get(key);

			// Fix for 1303 starts here: Items that are manually discounted by
			// the cashier correctly
			// reads "Promotion 40%." Items that were marked down by the buyers
			// and loaded
			// to the stores read "Deal Markdown." We need to change this
			// wording to read "Promotion ??%".
			String reductionReason = key;
			reductionReason = reductionReason.toUpperCase().trim();
			if (reductionReason.endsWith("MARKDOWN")) {
				LineItemPOSUtil util = new LineItemPOSUtil(lineItem);
				if (util.hasMarkDown()) {
					// System.out.println("ITEM : " + lineItem.getItem().getId()
					// + " HAS MARKDOWN");
					dRetailPrice = lineItem.getItemRetailPrice().doubleValue();
					dPromotionalMarkdown = util.getPromotionalMarkdown()
							.doubleValue();
					dPercentage = (dPromotionalMarkdown / dRetailPrice);
					key = Math.round(dPercentage * 100) + "% "
							+ resource.getString(reductionReason);
					// System.out.println("MARKDOWN LABEL: " + key);
					rAmt = util.getPromotionalMarkdown();
				}
			}
			// Fix for 1303 ends here
			list.add(new GroupedReceiptLine(lineItem, key, rAmt, -2));
		}
		if (list.size() == 0)
			list.add(new GroupedReceiptLine(lineItem));
		return (GroupedReceiptLine[]) list.toArray(new GroupedReceiptLine[0]);
	}

	// CMSPresaleLineItem
	private static GroupedReceiptLine[] createGroupedReceiptLineForReservation(
			CMSReservationLineItem lineItem) {
		List list = new ArrayList();
		list.add(new GroupedReceiptLine(lineItem));
		return (GroupedReceiptLine[]) list.toArray(new GroupedReceiptLine[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param discount
	 * @param reductionReason
	 * @return
	 */
	private static String fromatReductionReason(Discount discount,
			String reductionReason) {
		ResourceBundle res = ResourceManager.getResourceBundle();
		reductionReason = reductionReason.trim();
		if (reductionReason.toUpperCase().endsWith("DISCOUNT")) {
			if (CMSDiscountMgr.getSolicitReasons()) {
				reductionReason = reductionReason.substring(0,
						reductionReason.length() - 9).trim();
				if (reductionReason.indexOf("DISCOUNT") > -1)
					reductionReason = discount.getGuiLabel();
			} else {
				// Outlet store condition
				if (discount instanceof CMSEmployeeDiscount) {
					reductionReason = discount.getReason();
				}
				else
					reductionReason = "DISCOUNT";
			}
		}
		if (discount.isDiscountPercent()) {
			// Def 1427 for employee discount
			if (reductionReason.equalsIgnoreCase("Employee")) {
				reductionReason = "EMPLOYEE_DISCOUNT";
				// reductionReason = "EMPLOYEE DISCOUNT";
			}
			return "" + ((int) (discount.getPercent() * 100)) + "% "
					+ res.getString(reductionReason);
		} else {
			return res.getString(reductionReason);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param transaction
	 * @return
	 */
	private static Discount[] getTransactionDiscount(
			CompositePOSTransaction transaction) {
		Discount[] allDiscounts = transaction.getDiscountsArray();
		ArrayList list = new ArrayList();
		for (int i = 0; i < allDiscounts.length; i++) {
			list.add(allDiscounts[i]);
		}
		Discount[] allSettlDiscounts = transaction
				.getSettlementDiscountsArray();
		for (int i = 0; i < allSettlDiscounts.length; i++) {
			list.add(allSettlDiscounts[i]);
		}
		POSLineItem[] lineItems = transaction.getLineItemsArray();
		for (int i = 0; i < lineItems.length; i++) {
			Discount[] discounts = lineItems[i].getDiscountsArray();
			for (int j = 0; j < discounts.length; j++) {
				list.remove(discounts[j]);
			}
		}
		return (Discount[]) list.toArray(new Discount[0]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param discount
	 * @return
	 * @exception Exception
	 */
	private static Discount fixDiscountReason(Discount discount)
			throws Exception {
		if (discount instanceof CMSEmployeeDiscount) {
			ArmConfigLoader config = new ArmConfigLoader();
			String empDiscountReason = config.getString("DISCOUNT_CD_06.LABEL"); // Employee
																				// Discount
																					// Reason
			if (empDiscountReason != null && empDiscountReason.length() > 0) {
				discount.doSetReason(empDiscountReason);
			}
		}
		if (discount.getReason() == null) {
			discount.doSetReason("");
		}
		return discount;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param lineItem
	 * @return
	 */
	private static ArmCurrency findLineItemNowPrice(POSLineItem lineItem) {
		Discount[] discounts = lineItem.getDiscountsArray();
		if (discounts == null || discounts.length == 0) {
			if (lineItem instanceof CMSReturnLineItem) {
				// POSLineItem saleLineItem =
				// ((CMSReturnLineItem)lineItem).getSaleLineItem();
				// Put this check as in PreSale and Consignment returns, it is
				// null and used to throw an exception
				if (((CMSReturnLineItemDetail) lineItem
						.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
					POSLineItem saleLineItem = ((CMSReturnLineItemDetail) lineItem
							.getLineItemDetailsArray()[0])
							.getSaleLineItemDetail().getLineItem();
					if (saleLineItem != null) {
						discounts = saleLineItem.getDiscountsArray();
					}
				}
			} else {
				return null;
			}
		}
		if (discounts != null && discounts.length > 0) {
			for (int i = 0; i < discounts.length; i++) {
				if (discounts[i].getType().equals("BY_PRICE_DISCOUNT")) {
					return discounts[i].getAmount();
				}
			}
		}
		return null;
	}

	//
	// End of Static methods
	//
	// The line item
	private POSLineItem lineItem;

	// for consultant
	private String consultantId;

	private Employee consultant;

	private String consultantLastName;

	private String consultantFirstName;

	// for items
	private String itemId;

	private String itemDesc;

	private Integer itemQty;

	private ArmCurrency extendedRetailPrice;

	private ArmCurrency unitRetailPrice;

	private ArmCurrency extendedSellingPrice;

	private ArmCurrency extendedTaxAmount;

	// for Reductions
	private String reductionLabel;

	private ArmCurrency reductionAmount;

	private ArmCurrency extendedReductionAmount;

	private int reductionOrder = -1;

	/**
	 * put your documentation comment here
	 * 
	 * @param POSLineItem
	 *            lineItem
	 * @param String
	 *            reductionReason
	 * @param ArmCurrency
	 *            reductionAmount
	 * @param int
	 *            reductionOrder
	 */
	public GroupedReceiptLine(POSLineItem lineItem, String reductionReason,
			ArmCurrency reductionAmount, int reductionOrder) {
		if (lineItem instanceof CMSSaleLineItem) {
			init((CMSSaleLineItem) lineItem, reductionReason, reductionAmount,
					reductionOrder);
		} else if (lineItem instanceof CMSReturnLineItem) {
			init((CMSReturnLineItem) lineItem, reductionReason,
					reductionAmount, reductionOrder);
		} else if (lineItem instanceof CMSConsignmentLineItem) {
			init((CMSConsignmentLineItem) lineItem);
		} else if (lineItem instanceof CMSPresaleLineItem) {
			init((CMSPresaleLineItem) lineItem, reductionReason,
					reductionAmount, reductionOrder);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param POSLineItem
	 *            lineItem
	 */
	public GroupedReceiptLine(POSLineItem lineItem) {
		if (lineItem instanceof CMSSaleLineItem) {
			init((CMSSaleLineItem) lineItem, null, null, -1);
		} else if (lineItem instanceof CMSReturnLineItem) {
			init((CMSReturnLineItem) lineItem, null, null, -1);
		} else if (lineItem instanceof CMSConsignmentLineItem) {
			init((CMSConsignmentLineItem) lineItem);
		} else if (lineItem instanceof CMSReservationLineItem) {
			init((CMSReservationLineItem) lineItem);
		} else if (lineItem instanceof CMSPresaleLineItem) {
			init((CMSPresaleLineItem) lineItem, null, null, -1);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param lineItem
	 * @param reductionReason
	 * @param reductionAmount
	 * @param reductionOrder
	 */
	public void init(CMSSaleLineItem lineItem, String reductionReason,
			ArmCurrency reductionAmount, int reductionOrder) {
		this.lineItem = lineItem;
		if (lineItem.getAdditionalConsultant() != null) {
			this.consultant = lineItem.getAdditionalConsultant();
			this.consultantLastName = this.consultant.getLastName();
			this.consultantFirstName = this.consultant.getFirstName();
			this.consultantId = this.consultant.getShortName();
		} else {
			CompositePOSTransaction compo = lineItem.getTransaction()
					.getCompositeTransaction();
			this.consultantLastName = compo.getConsultant().getLastName();
			this.consultantFirstName = compo.getConsultant().getFirstName();
			this.consultantId = compo.getConsultant().getShortName();
		}
		//
		this.itemId = lineItem.getItem().getId();
		this.itemDesc = lineItem.getItemDescription();
		this.itemQty = lineItem.getQuantity();
		this.unitRetailPrice = lineItem.getItemRetailPrice();
		this.extendedRetailPrice = lineItem.getExtendedRetailAmount();
		this.extendedSellingPrice = lineItem.getExtendedNetAmount();
		this.extendedTaxAmount = lineItem.getExtendedTaxAmount();
		//
		if (reductionReason != null) {
			this.reductionLabel = reductionReason;
			this.reductionAmount = reductionAmount;
			this.reductionOrder = reductionOrder;
		} else {
			this.reductionLabel = null;
			this.reductionAmount = null;
		}
		this.extendedReductionAmount = lineItem.getExtendedReductionAmount();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param returnLineItem
	 * @param reductionReason
	 * @param reductionAmount
	 * @param reductionOrder
	 */
	public void init(CMSReturnLineItem returnLineItem, String reductionReason,
			ArmCurrency reductionAmount, int reductionOrder) {
		this.lineItem = returnLineItem;
		CMSSaleLineItem saleLineItem = (CMSSaleLineItem) returnLineItem
				.getSaleLineItem();
		if (saleLineItem == null) {
			// No receipt return .. set the selected associate ..
			if (returnLineItem.getAdditionalConsultant() != null) {
				this.consultant = returnLineItem.getAdditionalConsultant();
				this.consultantLastName = this.consultant.getLastName();
				this.consultantFirstName = this.consultant.getFirstName();
				this.consultantId = this.consultant.getShortName();
			} else {
				this.consultant = null;
				this.consultantLastName = null;
				this.consultantFirstName = null;
				this.consultantId = null;
			}
		} else {
			if (saleLineItem.getAdditionalConsultant() != null) {
				this.consultant = saleLineItem.getAdditionalConsultant();
				this.consultantLastName = this.consultant.getLastName();
				this.consultantFirstName = this.consultant.getFirstName();
				this.consultantId = this.consultant.getShortName();
			} else {
				CompositePOSTransaction compo = saleLineItem.getTransaction()
						.getCompositeTransaction();
				this.consultantLastName = compo.getConsultant().getLastName();
				this.consultantFirstName = compo.getConsultant().getFirstName();
				this.consultantId = compo.getConsultant().getShortName();
			}
		}
		//
		this.itemId = lineItem.getItem().getId();
		this.itemDesc = lineItem.getItemDescription();
		this.itemQty = new Integer(lineItem.getQuantity().intValue() * -1);
		this.unitRetailPrice = lineItem.getItemRetailPrice();
		this.extendedRetailPrice = lineItem.getExtendedRetailAmount().multiply(
				-1);
		this.extendedSellingPrice = lineItem.getExtendedNetAmount()
				.multiply(-1);
		this.extendedTaxAmount = lineItem.getExtendedTaxAmount().multiply(-1);
		//
		if (reductionReason != null) {
			this.reductionLabel = reductionReason;
			this.reductionAmount = reductionAmount;
			this.reductionOrder = reductionOrder;
		} else {
			this.reductionLabel = null;
			this.reductionAmount = null;
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param consignmentLineItem
	 */
	public void init(CMSConsignmentLineItem consignmentLineItem) {
		this.lineItem = consignmentLineItem;
		if (lineItem.getAdditionalConsultant() != null) {
			this.consultant = lineItem.getAdditionalConsultant();
			this.consultantLastName = this.consultant.getLastName();
			this.consultantFirstName = this.consultant.getFirstName();
			this.consultantId = this.consultant.getShortName();
		} else {
			CompositePOSTransaction compo = lineItem.getTransaction()
					.getCompositeTransaction();
			this.consultantLastName = compo.getConsultant().getLastName();
			this.consultantFirstName = compo.getConsultant().getFirstName();
			this.consultantId = compo.getConsultant().getShortName();
		}
		//
		this.itemId = lineItem.getItem().getId();
		this.itemDesc = lineItem.getItemDescription();
		this.itemQty = lineItem.getQuantity();
		this.unitRetailPrice = lineItem.getItemRetailPrice();
		this.extendedRetailPrice = lineItem.getExtendedRetailAmount();
		try {
			this.extendedSellingPrice = lineItem.getExtendedRetailAmount()
					.subtract(
							((CMSConsignmentLineItem) lineItem)
									.getExtendedReductionAmount());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		this.reductionLabel = null;
		this.reductionAmount = null;
		this.extendedReductionAmount = null;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param presaleLineItem
	 */
	public void init(CMSPresaleLineItem presaleLineItem,
			String reductionReason, ArmCurrency reductionAmount,
			int reductionOrder) {
		this.lineItem = presaleLineItem;
		if (lineItem.getAdditionalConsultant() != null) {
			this.consultant = lineItem.getAdditionalConsultant();
			this.consultantLastName = this.consultant.getLastName();
			this.consultantFirstName = this.consultant.getFirstName();
			this.consultantId = this.consultant.getShortName();
		} else {
			CompositePOSTransaction compo = lineItem.getTransaction()
					.getCompositeTransaction();
			this.consultantLastName = compo.getConsultant().getLastName();
			this.consultantFirstName = compo.getConsultant().getFirstName();
			this.consultantId = compo.getConsultant().getShortName();
		}
		//
		this.itemId = lineItem.getItem().getId();
		this.itemDesc = lineItem.getItemDescription();
		this.itemQty = lineItem.getQuantity();
		this.unitRetailPrice = lineItem.getItemRetailPrice();
		this.extendedRetailPrice = lineItem.getExtendedRetailAmount();
		;
		if (reductionReason != null) {
			this.reductionLabel = reductionReason;
			this.reductionAmount = reductionAmount;
			this.reductionOrder = reductionOrder;
		} else {
			this.reductionLabel = null;
			this.reductionAmount = null;
		}

		try {
			this.extendedSellingPrice = lineItem.getExtendedRetailAmount()
					.subtract(
							((CMSPresaleLineItem) lineItem)
									.getExtendedReductionAmount());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param presaleLineItem
	 */
	public void init(CMSReservationLineItem reservationLineItem) {
		this.lineItem = reservationLineItem;
		if (lineItem.getAdditionalConsultant() != null) {
			this.consultant = lineItem.getAdditionalConsultant();
			this.consultantLastName = this.consultant.getLastName();
			this.consultantFirstName = this.consultant.getFirstName();
			this.consultantId = this.consultant.getShortName();
		} else {
			CompositePOSTransaction compo = lineItem.getTransaction()
					.getCompositeTransaction();
			this.consultantLastName = compo.getConsultant().getLastName();
			this.consultantFirstName = compo.getConsultant().getFirstName();
			this.consultantId = compo.getConsultant().getShortName();
		}
		//
		this.itemId = lineItem.getItem().getId();
		this.itemDesc = lineItem.getItemDescription();
		this.itemQty = lineItem.getQuantity();
		this.unitRetailPrice = lineItem.getItemRetailPrice();
		this.extendedRetailPrice = lineItem.getExtendedRetailAmount();
		try {
			this.extendedSellingPrice = lineItem.getExtendedRetailAmount()
					.subtract(
							((CMSReservationLineItem) lineItem)
									.getExtendedReductionAmount());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		this.reductionLabel = null;
		this.reductionAmount = null;
	}

	/**
	 * put your documentation comment here
	 */
	public void setConsultantNull() {
		this.consultant = null;
		this.consultantId = null;
		this.consultantLastName = null;
		this.consultantFirstName = null;
	}

	/**
	 * put your documentation comment here
	 */
	public void setItemNull() {
		this.itemId = null;
		this.itemDesc = null;
		this.itemQty = null;
		this.unitRetailPrice = null;
		this.extendedRetailPrice = null;
		this.extendedSellingPrice = null;
		this.extendedTaxAmount = null;
	}

	/**
	 * put your documentation comment here
	 */
	public void setItemQtyNull() {
		this.itemQty = null;
	}

	/**
	 * put your documentation comment here
	 */
	public void setRetailPriceNull() {
		this.unitRetailPrice = null;
		this.extendedRetailPrice = null;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Employee getConsultant() {
		return this.consultant;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getConsultantId() {
		return this.consultantId;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getConsultantLastName() {
		return this.consultantLastName;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getConsultantFirstName() {
		return this.consultantFirstName;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public POSLineItem getLineItem() {
		return lineItem;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getItemDesc() {
		return itemDesc;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Integer getItemQty() {
		return itemQty;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getExtendedRetailPrice() {
		return extendedRetailPrice;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getUnitRetailPrice() {
		return unitRetailPrice;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getUnitNowPrice() {
		if (this.unitRetailPrice == null) {
			return null;
		}
		return GroupedReceiptLine.findLineItemNowPrice(this.lineItem);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getExtendedSellingPrice() {
		return extendedSellingPrice;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getTaxableIndicator() {
		String nonTaxable = "N";
		String taxable = "T";
		if (this.extendedTaxAmount == null) {
			return null;
		} else if (this.extendedSellingPrice.doubleValue() == 0.0) {
			return " "; // not applicable
		} else if (this.extendedTaxAmount.doubleValue() == 0.0) {
			return getTagForShippedItems(this.getLineItem(), nonTaxable); // nontaxable
		} else {
			return getTagForShippedItems(this.getLineItem(), taxable); // taxable
		}
	}

	/**
	 * Method returns an "S" tag for shipped items
	 * 
	 * @return
	 */
	public String getTagForShippedItems(POSLineItem posLineItem, String tag) {
		CMSSaleLineItem saleLineItem = null;
		String itemId = null;
		if (posLineItem instanceof CMSSaleLineItem) {
			saleLineItem = (CMSSaleLineItem) posLineItem;
			itemId = saleLineItem.getItem().getId();
			if (!itemId.equals(itemCfg.getString("SHIP.BASE_ITEM"))) {
				if (saleLineItem.getShippingRequest() != null) {
					return "S";
				} else {
					return tag;
				}
			} else {
				return tag;
			}
		}
		return tag;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getPOSIndicator() {
		if (this.extendedSellingPrice == null) {
			return null;
		}
		if (this.getLineItem() instanceof CMSSaleLineItem) {
			CMSSaleLineItem line = (CMSSaleLineItem) this.getLineItem();
			if ((line.getConsignmentLineItem() != null || line
					.getPresaleLineItem() != null)
					|| line.getReservationLineItem() != null) {
				return "S";
			} else {
				return " ";
			}
		} else if (this.getLineItem() instanceof CMSReturnLineItem) {
			CMSReturnLineItem line = (CMSReturnLineItem) this.getLineItem();
			if ((line.getConsignmentLineItem() != null || line
					.getPresaleLineItem() != null)
					|| line.getReservationLineItem() != null) {
				return "R";
			} else {
				return " ";
			}
		} else {
			return " ";
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public boolean isNonTaxable() {
		if (this.extendedTaxAmount == null) {
			return false;
		}
		if (this.extendedTaxAmount.doubleValue() == 0.0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getReductionAmount() {
		return reductionAmount;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getReductionLabel() {
		return reductionLabel;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getItemIdAndDesc() {
		//Modified by deepika for printing barcode,cashier entered description for exception item.
		if (this.lineItem.isMiscItem()) {
			if(lineItem.getMiscItemId().equals("Exception_Item"))
			{
				String barCode = ((CMSItem) this.lineItem.getItem()).getBarCode();
				if (barCode == null || barCode.length() == 0) {
					return this.itemId + " " + this.itemDesc;
				} else {
					return barCode + " " + this.itemDesc;
				}
			}
			return this.itemDesc;
		}
		if (this.lineItem.getLineItemDetailsArray()[0].getGiftCertificateId() != null) {
			return this.itemDesc;
		}
		if (this.itemId != null && this.itemDesc != null) {
			String barCode = ((CMSItem) this.lineItem.getItem()).getBarCode();
			if (barCode == null || barCode.length() == 0) {
				return this.itemId + " " + this.itemDesc;
			} else {
				return barCode + " " + this.itemDesc;
			}
		} else if (this.itemId != null) {
			return this.itemId;
		} else {
			return this.itemDesc;
		}
	}
	//Mayuri Edhara - added for Receipt Changes
	/**
	 * getItemBarcode
	 * 
	 * @return
	 */
	public String getItemBarcode() {
		try {
			String barCode = ((CMSItem) this.lineItem.getItem()).getBarCode();
			if (barCode == null || barCode.length() == 0) {
				barCode = this.itemId;
			}
			if (this.lineItem.isMiscItem()) {
				return this.itemId;
			}
			if (this.lineItem.getLineItemDetailsArray()[0]
					.getGiftCertificateId() != null) {
				return this.itemId;
			}
			//Added by Anjana for reciept to genrate item description when SAP=Y
			if(itemCfg.getString("SAP_LOOKUP")!=null && itemCfg.getString("SAP_LOOKUP").equalsIgnoreCase("Y") && this.itemId != null && this.itemDesc != null){				
				return barCode;
			}
			else if (this.itemId != null) {
			//	String barCode = ((CMSItem) this.lineItem.getItem()).getBarCode();
				return barCode;
			}
			return this.itemId;
		} catch (Exception exp) {
			exp.printStackTrace();
			return this.itemId;
		}
	}
	//Mayuri Edhara - added for Receipt Changes
	/**
	 * getClassDescModelFabricAndColor
	 * 
	 * @return
	 */
	public String getClassDescModelFabricAndColor() {
		try {
			String classDesc = ((CMSItem) this.lineItem.getItem()).getDescription();
			String Model = ((CMSItem) this.lineItem.getItem()).getModel();
			String Fabric = ((CMSItem) this.lineItem.getItem()).getFabric();
			String ColorId = ((CMSItem) this.lineItem.getItem()).getColorId();	
			
			if (classDesc == null || classDesc.length() == 0){
				classDesc = this.itemDesc;
			}
			if(Model == null || Model.length() == 0){
				Model = "";
			}
			if(Fabric == null || Fabric.length() == 0){
				Fabric = "";
			}else {
				Fabric = "/"+Fabric;
			}
			if(ColorId == null || ColorId.length() == 0){
				ColorId = "";
			}else{
				ColorId = "/"+ColorId;
			}
			
			if (this.lineItem.isMiscItem()) {
				return this.itemDesc;
			}
			if (this.lineItem.getLineItemDetailsArray()[0]
					.getGiftCertificateId() != null) {
				return this.itemDesc;
			}
			//Added by Anjana for reciept to genrate item description when SAP=Y
			if(itemCfg.getString("SAP_LOOKUP")!=null && itemCfg.getString("SAP_LOOKUP").equalsIgnoreCase("Y") && this.itemId != null && this.itemDesc != null){				
				return classDesc+" "+Model+Fabric+ColorId;
			}
			else if (this.itemId != null && this.itemDesc != null) {
				classDesc = ((CMSItem) this.lineItem.getItem()).getItemDetail().getClassDesc();
				if (classDesc == null || classDesc.length() == 0){
					classDesc = this.itemDesc;
				}
				return classDesc+" "+ Model+Fabric+ColorId;				
			} else {
				return this.itemDesc;
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			return this.itemDesc;
		}
	}
	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getItemIdAndClassDesc() {
		try {
			if (this.lineItem.isMiscItem()) {
				return this.itemDesc;
			}
			if (this.lineItem.getLineItemDetailsArray()[0]
					.getGiftCertificateId() != null) {
				return this.itemDesc;
			}
			//Added by Anjana for reciept to genrate item description when SAP=Y
			if(itemCfg.getString("SAP_LOOKUP")!=null && itemCfg.getString("SAP_LOOKUP").equalsIgnoreCase("Y") && this.itemId != null && this.itemDesc != null){
				String barCode = ((CMSItem) this.lineItem.getItem())
						.getBarCode();
				String classDesc = ((CMSItem) this.lineItem.getItem())
						.getDescription();
				return barCode + " " + classDesc;
			}
			else if (this.itemId != null && this.itemDesc != null) {
				String barCode = ((CMSItem) this.lineItem.getItem())
						.getBarCode();
				String classDesc = ((CMSItem) this.lineItem.getItem())
						.getItemDetail().getClassDesc();
				if (classDesc == null || classDesc.length() == 0) {
					classDesc = this.itemDesc;
				}
				if (barCode == null || barCode.length() == 0) {
					barCode = this.itemId;
				}
				return barCode + " " + classDesc;
			} else if (this.itemId != null) {
				return this.itemId;
			} else {
				return this.itemDesc;
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			return this.itemId;
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param line
	 * @return
	 */
	public int compareTo(Object line) {
		if (line instanceof GroupedReceiptLine) {
			int result = 0;
			GroupedReceiptLine rLine = (GroupedReceiptLine) line;
			if (this.consultantLastName != null
					&& rLine.consultantLastName != null
					&& this.consultantLastName
							.compareTo(rLine.consultantLastName) != 0) {
				result = this.consultantLastName
						.compareTo(rLine.consultantLastName);
			} else if (this.consultantFirstName != null
					&& rLine.consultantFirstName != null) {
				result = this.consultantFirstName
						.compareTo(rLine.consultantFirstName);
			}
			if (result == 0) {
				if (this.lineItem != null && rLine.lineItem != null) {
					if (this.lineItem.getSequenceNumber() < rLine.lineItem
							.getSequenceNumber()) {
						result = -1;
					} else if (this.lineItem.getSequenceNumber() > rLine.lineItem
							.getSequenceNumber()) {
						result = 1;
					}
				}
			}
			if (result == 0) {
				if (this.reductionOrder < rLine.reductionOrder) {
					result = -1;
				} else if (this.reductionOrder > rLine.reductionOrder) {
					result = 1;
				}
			}
			return result;
		} else {
			return 0;
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.consultantLastName != null)
			sb.append("[consultantLastName=" + this.consultantLastName + "]");
		if (this.consultantFirstName != null)
			sb.append("[consultantFirstName=" + this.consultantFirstName + "]");
		if (this.itemId != null)
			sb.append("[itemId=" + this.itemId + "]");
		if (this.reductionLabel != null)
			sb.append("[reductionLabel=" + this.reductionLabel + "]");
		if (this.reductionAmount != null)
			sb.append("[reductionAmount=" + this.reductionAmount + "]");
		sb.append("[reductionOrder=" + this.reductionOrder + "]");
		if (this.lineItem != null) {
			sb.append("[lineItem.getSequenceNumber="
					+ this.lineItem.getSequenceNumber() + "]");
		}
		return sb.toString();
	}

	public double getExtendedReductionPercent() {
		double percent = 0;
		if (this.extendedReductionAmount != null
				&& this.getExtendedRetailPrice() != null
				&& this.getExtendedRetailPrice().doubleValue() != 0) {
			percent = this.extendedReductionAmount.doubleValue() * 100
					/ this.getExtendedRetailPrice().doubleValue();
		}
		return Math.floor(percent * 100) / 100;
	}

	public ArmCurrency getLineItemTotalAlterationAmount() {
		ArmCurrency total = new ArmCurrency(0);
		AlterationLineItemDetail[] alterationLineItemDetails = null;
		if (getLineItem() instanceof CMSSaleLineItem)
			alterationLineItemDetails = ((CMSSaleLineItemDetail) getLineItem()
					.getLineItemDetailsArray()[0])
					.getAlterationLineItemDetailArray();
		else if (getLineItem() instanceof CMSPresaleLineItem)
			alterationLineItemDetails = ((CMSPresaleLineItemDetail) getLineItem()
					.getLineItemDetailsArray()[0])
					.getAlterationLineItemDetailArray();
		if (alterationLineItemDetails != null) {
			try {
				for (int j = 0; j < alterationLineItemDetails.length; j++) {
					total = total.add(alterationLineItemDetails[j]
							.getTotalPrice());
				}
			} catch (CurrencyException e) {
				e.printStackTrace();
			}
		}
		return total;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param discount
	 * @return
	 * @exception Exception
	 */
	private static String getTrueDiscountReason(Discount discount)
			throws Exception {
		String discountReason = "";
		if (discount instanceof CMSEmployeeDiscount) {
			ArmConfigLoader config = new ArmConfigLoader();
			String empDiscountReason = config.getString("DISCOUNT_CD_06.LABEL"); // Employee
																					// Discount
			//Fix for 1824:only in case of japan as in reason is in japanese.
			if("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
				discountReason = "EMPLOYEE DISCOUNT";
			} else {
			// Reason
				if (empDiscountReason != null && empDiscountReason.length() > 0) {
					discountReason = empDiscountReason;
				}
			}
		}
		if (discount.getReason() == null) {
			discount.doSetReason(discountReason);
		}
		return discountReason;
	}
}
