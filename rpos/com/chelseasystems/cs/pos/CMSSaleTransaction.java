/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 09-13-2005 | Manpreet  | 508       |Implementing Alterations on Presale line items      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-12-2005 | Rajesh    | N/A       |Specs Consignment impl                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import java.io.*;
import java.util.*;

import com.chelseasystems.cr.business.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cs.pos.CMSMiscItem;
import com.chelseasystems.cs.util.Version;


/**
 *
 * <p>Title: CMSSaleTransaction</p>
 *
 * <p>Description: CMSSaleTransaction</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh
 * @version 1.0
 */
public class CMSSaleTransaction extends SaleTransaction implements com.chelseasystems.cr.rules.IRuleEngine {
	static final long serialVersionUID = -8717792333015608496L;
	int discSeqNum = 1;

	/**
	 * Constructor
	 * @param aCompositeTransaction CompositePOSTransaction
	 */
	public CMSSaleTransaction(CompositePOSTransaction aCompositeTransaction) {
		super(aCompositeTransaction);
	}

	/**
	 * This method is used to create sale transaction line item
	 * @param anItem Item
	 * @return POSLineItem
	 */
	public POSLineItem createLineItem(Item anItem) {
		return new CMSSaleLineItem(this, anItem);
	}

	/**
	 * This method is used to create sale transaction line item group
	 * @param lineItem POSLineItem
	 * @return POSLineItemGrouping
	 */
	public POSLineItemGrouping createLineItemGrouping(POSLineItem lineItem) {
		return new CMSSaleLineItemGrouping(lineItem);
	}

	/**
	 * This method is used to add pre sale line item
	 * @param prsLnItm CMSPresaleLineItem
	 * @param aSaleQuantity int
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem addPresaleLineItem(CMSPresaleLineItem prsLnItm, int aSaleQuantity) throws BusinessRuleException {
		checkForNullParameter("addPresaleLineItem", prsLnItm);
		executeRule("addPresaleLineItem", prsLnItm);
		SaleLineItem aSaleLineItem = doAddPresaleLineItem(prsLnItm);
		aSaleLineItem.setQuantity(new Integer(aSaleQuantity));
		return aSaleLineItem;
	}

	/**
	 * This method is used to add pre sale line item
	 * @param prsLnItm CMSPresaleLineItem
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem doAddPresaleLineItem(CMSPresaleLineItem prsLnItm) throws BusinessRuleException {
		int sequenceNumber = 1;
		CMSSaleLineItem aSaleLineItem = (CMSSaleLineItem) newLineItem(prsLnItm.getItem());
		CMSShippingRequest lnshippingRequest = (CMSShippingRequest) prsLnItm.getShippingRequest();
		if (prsLnItm.getTransaction().getCompositeTransaction().getTaxExemptId() != null)
			this.getCompositeTransaction().setTaxExemptId(prsLnItm.getTransaction().getCompositeTransaction().getTaxExemptId());
		if (lnshippingRequest != null) {
			//# 1260: Same Shipping address was added multiple times in the POS txn
			CMSShippingRequest shippingRequest = null;
			boolean addThisToTxn = true;
			if (this.getCompositeTransaction().hasShippingRequests()) {
				ShippingRequest[] sr = (ShippingRequest[]) this.getCompositeTransaction().getShippingRequestsArray();
				if (sr != null && sr.length > 0) {
					for (int i = 0; i < sr.length; i++) {
						System.out.println("sr[i] " + sr[i]);
						if (((CMSShippingRequest) sr[i]).compare(lnshippingRequest)) {
							addThisToTxn = false;
							shippingRequest = (CMSShippingRequest) sr[i];
						}
					}
				}
			}
			if (addThisToTxn) {
				shippingRequest = new CMSShippingRequest(this.getCompositeTransaction());
			}
			shippingRequest.copy(lnshippingRequest);
			aSaleLineItem.setShippingRequest(shippingRequest);
		}
		if (this.getCompositeTransaction().hasShippingRequests()) {
			ShippingRequest shippingRequest[] = this.getCompositeTransaction().getShippingRequestsArray();
			for (int i = 0; i < shippingRequest.length; i++) {
				POSLineItem[] posLineItems = shippingRequest[i].getLineItemsArray();
				if (posLineItems != null && posLineItems.length > 0) {
					for (int y = 0; y < posLineItems.length; y++) {
						CMSSaleLineItem saleLineItem = null;
						if (posLineItems[y] instanceof CMSSaleLineItem)
							saleLineItem = (CMSSaleLineItem) posLineItems[y];
						else
							continue;
						ConfigMgr configMgr = new ConfigMgr("item.cfg");
						String miscItemKeys = configMgr.getString("MISC_ITEM_KEYS");
						StringTokenizer strTok = new StringTokenizer(miscItemKeys, ",");
						while(strTok.hasMoreTokens()) {
							String misckey = strTok.nextToken();
							if (misckey.trim().equalsIgnoreCase("SHIP") || misckey.trim().equalsIgnoreCase("ALTERATION")) {
								String miscItemId = configMgr.getString(misckey + ".BASE_ITEM");
								String taxable = configMgr.getString(misckey + ".TAXABLE");
								if (miscItemId != null && saleLineItem.getItem().getId().equals(miscItemId)) {
									CMSMiscItem miscItem = new CMSMiscItem(miscItemId, (CMSItem) saleLineItem.getItem());
									saleLineItem.setTaxableMiscLineItem(miscItem);
									if (taxable != null && taxable.equalsIgnoreCase("y"))
										saleLineItem.getItem().setTaxable();
								}
							}
						}
					}
				}
			}
		}

		ConfigMgr configMgr = new ConfigMgr("item.cfg");
		String miscItemKeys = configMgr.getString("MISC_ITEM_KEYS");
		StringTokenizer strTok = new StringTokenizer(miscItemKeys, ",");
		while(strTok.hasMoreTokens()) {
			String misckey = strTok.nextToken();
			if (misckey.trim().equalsIgnoreCase("SHIP") || misckey.trim().equalsIgnoreCase("ALTERATION")) {
				String miscItemId = configMgr.getString(misckey + ".BASE_ITEM");
				String taxable = configMgr.getString(misckey + ".TAXABLE");
				if (miscItemId != null && aSaleLineItem.getItem().getId().equals(miscItemId)) {
					CMSMiscItem miscItem = new CMSMiscItem(miscItemId, (CMSItem) aSaleLineItem.getItem());
					aSaleLineItem.setTaxableMiscLineItem(miscItem);
					if (taxable != null && taxable.equalsIgnoreCase("y"))
						aSaleLineItem.getItem().setTaxable();
				}
			}
		}

		for (Enumeration aDiscountList = prsLnItm.getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			if (discount.getSequenceNumber() > discSeqNum) {
				discSeqNum = discount.getSequenceNumber();
				discSeqNum++;
			}
			//discount.setSequenceNumber(discSeqNum);
			aSaleLineItem.addDiscount(discount);
			if (this.getCompositeTransaction().getDiscountsArray().length == 0)
				this.getCompositeTransaction().addDiscount(discount);
				
			boolean addDiscount = true;
			for (Enumeration aTxnDiscountList = this.getCompositeTransaction().getDiscounts(); aTxnDiscountList.hasMoreElements();) {
				CMSDiscount txnDiscount = (CMSDiscount) aTxnDiscountList.nextElement();
				if (txnDiscount.getSequenceNumber() == discount.getSequenceNumber()) {
					addDiscount = false;
					break;
				}
			}
			if (addDiscount)
				this.getCompositeTransaction().addDiscount(discount);
		}
		for (Enumeration aDiscountList = prsLnItm.getTransaction().getCompositeTransaction().getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			CMSDiscount lineDiscount = (CMSDiscount) discount.clone();
			if (!discount.getIsLineItemDiscount()) {
				lineDiscount.setIsLineItemDiscount(true);
				lineDiscount.setSequenceNumber(discSeqNum);
				this.getCompositeTransaction().addDiscount(lineDiscount);
				aSaleLineItem.addDiscount(lineDiscount);
				discSeqNum++;
			}
		}
		// MSB : 09/14/2005
		// Transfer AlterationLineItemDetails
		POSLineItemDetail posLnItmDetails[] = prsLnItm.getLineItemDetailsArray();
		CMSSaleLineItemDetail saleDetail = (CMSSaleLineItemDetail) aSaleLineItem.createLineItemDetail();
		for (int iCtr = 0; iCtr < posLnItmDetails.length; iCtr++) {
			if (posLnItmDetails[iCtr] instanceof CMSPresaleLineItemDetail) {
				AlterationLineItemDetail altDets[] = ((CMSPresaleLineItemDetail) posLnItmDetails[iCtr]).getAlterationLineItemDetailArray();
				if (altDets == null)
					continue;
				for (int iAltCtr = 0; iAltCtr < altDets.length; iAltCtr++) {
					saleDetail.addAlterationLineItemDetail(altDets[iAltCtr]);
				}
			}
		}
		if (prsLnItm.getAdditionalConsultant() != null)
			aSaleLineItem.doSetAdditionalConsultant(prsLnItm.getAdditionalConsultant());
		//Vivek Mishra : Commented in order to show zero amount while presale close even in the case of price override.
		/*if (prsLnItm.getManualUnitPrice() != null)
			aSaleLineItem.setManualUnitPrice(prsLnItm.getManualUnitPrice());
		if (prsLnItm.getManualMarkdownReason() != null)
			aSaleLineItem.setManualMarkdownReason(prsLnItm.getManualMarkdownReason());*/
		//Ends
		aSaleLineItem.doSetPresaleLineItem(prsLnItm);
		if (prsLnItm.isMiscItem())
		{
			prsLnItm.transferInformationTo(aSaleLineItem);
			//Vivek Mishra : Changed to show 0 amount in case of PRESALE close txn
			if(prsLnItm.getTransaction() instanceof PresaleTransaction){
			//aSaleLineItem.setItemPriceOverride(new ArmCurrency(0.0d));
			aSaleLineItem.doSetItemRetailPrice(new ArmCurrency(0.0d));
			aSaleLineItem.doSetItemSellingPrice(new ArmCurrency(0.0d));
			}
		}
        //Vivek Mishra : Added to fix amount due issue for normal items during pre sale close.
        aSaleLineItem.doSetItemRetailPrice(new ArmCurrency(0.0d));
		aSaleLineItem.doSetItemSellingPrice(new ArmCurrency(0.0d));
		return aSaleLineItem;
	}

	/**
	 * This method is used to add entire pre sale line item
	 * @param prsLnItm CMSPresaleLineItem
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem addEntirePresaleLineItem(CMSPresaleLineItem prsLnItm) throws BusinessRuleException {
		checkForNullParameter("addEntirePresaleLineItem", prsLnItm);
		executeRule("addEntirePresaleLineItem", prsLnItm);
		return addPresaleLineItem(prsLnItm, prsLnItm.getQuantityAvailableForReturn());
	}

	/**
	 * This method is used to add entire pre sale transaction
	 * @param prsTxn PresaleTransaction
	 * @throws BusinessRuleException
	 */
	public void addEntirePresaleTransaction(PresaleTransaction prsTxn) throws BusinessRuleException {
		checkForNullParameter("addEntirePresaleTransaction", prsTxn);
		executeRule("addEntirePresaleTransaction", prsTxn);
		for (Enumeration aPrsLineItemList = prsTxn.getLineItems(); aPrsLineItemList.hasMoreElements(); addEntirePresaleLineItem((CMSPresaleLineItem) aPrsLineItemList.nextElement()))
			;
	}

	/**
	 * This method is used to add consignment line item
	 * @param csgLnItm CMSConsignmentLineItem
	 * @param aSaleQuantity int
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	// Consignment methods
	public SaleLineItem addConsignmentLineItem(CMSConsignmentLineItem csgLnItm, int aSaleQuantity)
			throws BusinessRuleException {
		checkForNullParameter("addConsignmentLineItem", csgLnItm);
		executeRule("addConsignmentLineItem", csgLnItm);
		SaleLineItem aSaleLineItem = doAddConsignmentLineItem(csgLnItm);
		aSaleLineItem.setQuantity(new Integer(aSaleQuantity));
		return aSaleLineItem;
	}

	/**
	 * This method is used to add consignment line item
	 * @param csgLnItm CMSConsignmentLineItem
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem doAddConsignmentLineItem(CMSConsignmentLineItem csgLnItm) throws BusinessRuleException {
		CMSSaleLineItem aSaleLineItem = (CMSSaleLineItem) newLineItem(csgLnItm.getItem());
		CMSShippingRequest lnshippingRequest = (CMSShippingRequest) csgLnItm.getShippingRequest();
		if (lnshippingRequest != null) {
			//#1260 Same shipping address was added multiple times in the pos txn
			//      CMSShippingRequest shippingRequest = new CMSShippingRequest(this.getCompositeTransaction());
			CMSShippingRequest shippingRequest = null;
			boolean addThisToTxn = true;
			if (this.getCompositeTransaction().hasShippingRequests()) {
				ShippingRequest[] sr = (ShippingRequest[]) this.getCompositeTransaction().getShippingRequestsArray();
				if (sr != null && sr.length > 0) {
					for (int i = 0; i < sr.length; i++) {
						System.out.println("sr[i] " + sr[i]);
						if (((CMSShippingRequest) sr[i]).compare(lnshippingRequest)) {
							addThisToTxn = false;
							shippingRequest = (CMSShippingRequest) sr[i];
						}
					}
				}
			}
			if (addThisToTxn) {
				shippingRequest = new CMSShippingRequest(this.getCompositeTransaction());
			}
			shippingRequest.copy(lnshippingRequest);
			aSaleLineItem.setShippingRequest(shippingRequest);
		}
		for (Enumeration aDiscountList = csgLnItm.getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			if (discount.getSequenceNumber() > discSeqNum) {
				discSeqNum = discount.getSequenceNumber();
				discSeqNum++;
			}
			aSaleLineItem.addDiscount(discount);
			if (this.getCompositeTransaction().getDiscountsArray().length == 0)
				this.getCompositeTransaction().addDiscount(discount);
			
			boolean addDiscount = true;
			for (Enumeration aTxnDiscountList = this.getCompositeTransaction().getDiscounts(); aTxnDiscountList.hasMoreElements();) {
				CMSDiscount txnDiscount = (CMSDiscount) aTxnDiscountList.nextElement();
				if (txnDiscount.getSequenceNumber() == discount.getSequenceNumber()) {
					addDiscount = false;
					break;
				}
				//				discSeqNum++;
			}
			if (addDiscount)
					this.getCompositeTransaction().addDiscount(discount);
		}
		for (Enumeration aDiscountList = csgLnItm.getTransaction().getCompositeTransaction().getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			CMSDiscount lineDiscount = (CMSDiscount) discount.clone();
			if (!discount.getIsLineItemDiscount()) {
				lineDiscount.setIsLineItemDiscount(true);
				lineDiscount.setSequenceNumber(discSeqNum);
				this.getCompositeTransaction().addDiscount(lineDiscount);
				aSaleLineItem.addDiscount(lineDiscount);
				discSeqNum++;
			}
		}
		if (csgLnItm.getAdditionalConsultant() != null)
			aSaleLineItem.doSetAdditionalConsultant(csgLnItm.getAdditionalConsultant());
		if (csgLnItm.getManualUnitPrice() != null)
			aSaleLineItem.setManualUnitPrice(csgLnItm.getManualUnitPrice());
		if (csgLnItm.getManualMarkdownReason() != null)
			aSaleLineItem.setManualMarkdownReason(csgLnItm.getManualMarkdownReason());
		aSaleLineItem.doSetConsignmentLineItem(csgLnItm);
		if (csgLnItm.isMiscItem())
			csgLnItm.transferInformationTo(aSaleLineItem);
		return aSaleLineItem;
	}

	/**
	 * This method is used to add entire consignment line item
	 * @param csgLnItm CMSConsignmentLineItem
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem addEntireConsignmentLineItem(CMSConsignmentLineItem csgLnItm) throws BusinessRuleException {
		checkForNullParameter("addEntireConsignmentLineItem", csgLnItm);
		executeRule("addEntireConsignmentLineItem", csgLnItm);
		return addConsignmentLineItem(csgLnItm, csgLnItm.getQuantityAvailableForReturn());
	}

	/**
	 * This method is used to add entire consignment transaction
	 * @param csgTxn ConsignmentTransaction
	 * @throws BusinessRuleException
	 */
	public void addEntireConsignmentTransaction(ConsignmentTransaction csgTxn) throws BusinessRuleException {
		checkForNullParameter("addEntireConsignmentTransaction", csgTxn);
		executeRule("addEntireConsignmentTransaction", csgTxn);
		for (Enumeration aPrsLineItemList = csgTxn.getLineItems(); aPrsLineItemList.hasMoreElements(); addEntireConsignmentLineItem((CMSConsignmentLineItem) aPrsLineItemList.nextElement()))
			;
	}

	/**
	 * This method is used to add reservation line item
	 * @param resLnItm CMSReservationLineItem
	 * @param aSaleQuantity int
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem addReservationLineItem(CMSReservationLineItem resLnItm, int aSaleQuantity)
			throws BusinessRuleException {
		checkForNullParameter("addReservationLineItem", resLnItm);
		executeRule("addReservationLineItem", resLnItm);
		SaleLineItem aSaleLineItem = doAddReservationLineItem(resLnItm);
		aSaleLineItem.setQuantity(new Integer(aSaleQuantity));
		return aSaleLineItem;
	}

	/**
	 * This method is used to add no reservation line item
	 * @param resLnItm CMSReservationLineItem
	 * @param aSaleQuantity int
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem addNoReservationLineItem(CMSItem item) throws BusinessRuleException {
		checkForNullParameter("addNoReservationLineItem", item);
		executeRule("addNoReservationLineItem", item);
		CMSReservationLineItem rsvLineItem = new CMSReservationLineItem(
				((CMSCompositePOSTransaction) getCompositeTransaction()).getNoReservationOpenTransaction(), item);
		rsvLineItem.setQuantity(new Integer(1));
		return addReservationLineItem(rsvLineItem, 1);
	}

	/**
	 * This method is used to add reservation line item
	 * @param resLnItm CMSReservationLineItem
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem doAddReservationLineItem(CMSReservationLineItem resLnItm) throws BusinessRuleException {
		int sequenceNumber = 1;
		CMSSaleLineItem aSaleLineItem = (CMSSaleLineItem) newLineItem(resLnItm.getItem());
		for (Enumeration aDiscountList = resLnItm.getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			if (discount.getSequenceNumber() > discSeqNum) {
				discSeqNum = discount.getSequenceNumber();
				discSeqNum++;
			}
			aSaleLineItem.addDiscount(discount);
			if (this.getCompositeTransaction().getDiscountsArray().length == 0)
				this.getCompositeTransaction().addDiscount(discount);
			
			boolean addDiscount = true;
			for (Enumeration aTxnDiscountList = this.getCompositeTransaction().getDiscounts(); aTxnDiscountList.hasMoreElements();) {
				CMSDiscount txnDiscount = (CMSDiscount) aTxnDiscountList.nextElement();
				if (txnDiscount.getSequenceNumber() == discount.getSequenceNumber()) {
					addDiscount = false;
					break;
				}
			}
			if (addDiscount)
				this.getCompositeTransaction().addDiscount(discount);

		}
		for (Enumeration aDiscountList = resLnItm.getTransaction().getCompositeTransaction().getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			CMSDiscount lineDiscount = (CMSDiscount) discount.clone();
			if (!discount.getIsLineItemDiscount()) {
				lineDiscount.setIsLineItemDiscount(true);
				lineDiscount.setSequenceNumber(discSeqNum);
				this.getCompositeTransaction().addDiscount(lineDiscount);
				aSaleLineItem.addDiscount(lineDiscount);
				discSeqNum++;
			}
		}
		if (resLnItm.getAdditionalConsultant() != null)
			aSaleLineItem.doSetAdditionalConsultant(resLnItm.getAdditionalConsultant());
		aSaleLineItem.doSetReservationLineItem(resLnItm);
		if (resLnItm.isMiscItem())
			resLnItm.transferInformationTo(aSaleLineItem);
		return aSaleLineItem;
	}

	/**
	 * This method is used to add entire reservation line item
	 * @param resLnItm CMSReservationLineItem
	 * @return SaleLineItem
	 * @throws BusinessRuleException
	 */
	public SaleLineItem addEntireReservationLineItem(CMSReservationLineItem resLnItm) throws BusinessRuleException {
		checkForNullParameter("addEntireReservationLineItem", resLnItm);
		executeRule("addEntireReservationLineItem", resLnItm);
		return addReservationLineItem(resLnItm, resLnItm.getQuantityAvailableForReturn());
	}

	/**
	 * This method is used to add entire reservation transaction
	 * @param resTxn Reservation
	 * @throws BusinessRuleException
	 */
	public void addEntireReservationTransaction(ReservationTransaction resTxn) throws BusinessRuleException {
		checkForNullParameter("addEntireReservationTransaction", resTxn);
		executeRule("addEntireReservationTransaction", resTxn);
		for (Enumeration aResLineItemList = resTxn.getLineItems(); aResLineItemList.hasMoreElements(); addEntireReservationLineItem((CMSReservationLineItem) aResLineItemList.nextElement()))
			;
	}

	public ArmCurrency getNetAmountExcludingDiscountedItems() {
		try {
			ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
			for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements();) {
				POSLineItem posLineItem = ((POSLineItem) aLineItemList.nextElement());
				//		    POSLineItemDetail[] dets = posLineItem.getLineItemDetailsArray();
				ArmCurrency aLineItemExtendedNetAmount = posLineItem.getExtendedNetAmount();
				if (posLineItem.getExtendedReductionAmount().doubleValue() <= 0.0D) {
					total = total.add(aLineItemExtendedNetAmount);
				}
			}
			return total;
		} catch (CurrencyException anException) {
			logCurrencyException("getNetAmountExcludingDiscountedItems", anException);
		}
		return null;
	}

	public void addEntireSaleTransaction(SaleTransaction saleTxn) throws BusinessRuleException {
		checkForNullParameter("addEntireSaleTransaction", saleTxn);
		executeRule("addEntireSaleTransaction", saleTxn);
		for (Enumeration aSaleLineItemList = saleTxn.getLineItems(); aSaleLineItemList.hasMoreElements(); addEntireSaleLineItem((CMSSaleLineItem) aSaleLineItemList.nextElement()))
			;
	}

	public SaleLineItem addEntireSaleLineItem(CMSSaleLineItem saleLnItm) throws BusinessRuleException {
		checkForNullParameter("addEntireSaleLineItem", saleLnItm);
		executeRule("addEntireSaleLineItem", saleLnItm);
		return addSaleLineItem(saleLnItm, saleLnItm.getQuantity().intValue());
	}

	public SaleLineItem addSaleLineItem(SaleLineItem saleLnItm, int aSaleQuantity) throws BusinessRuleException {
		checkForNullParameter("addSaleLineItem", saleLnItm);
		executeRule("addSaleLineItem", saleLnItm);
		SaleLineItem aSaleLineItem = doAddSaleLineItem(saleLnItm);
		aSaleLineItem.setQuantity(new Integer(aSaleQuantity));
		return aSaleLineItem;
	}

	public SaleLineItem doAddSaleLineItem(SaleLineItem saleLnItm) throws BusinessRuleException {
		CMSSaleLineItem aSaleLineItem = (CMSSaleLineItem) newLineItem(saleLnItm.getItem());
		for (Enumeration aDiscountList = this.getCompositeTransaction().getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			for (Enumeration lineItemDiscounts = saleLnItm.getDiscounts(); lineItemDiscounts.hasMoreElements();) {
				CMSDiscount lineItemDiscount = (CMSDiscount) lineItemDiscounts.nextElement();
				if (discount.getSequenceNumber() == lineItemDiscount.getSequenceNumber()) {
					discount.setIsLineItemDiscount(true);
					if (discount.getType().startsWith("BY_PRICE_DISCOUNT")) {
						aSaleLineItem.setAddPriceDiscount(discount);
					}
					aSaleLineItem.addDiscount(discount);
				}
			}
		}
		aSaleLineItem.doSetAdditionalConsultant(saleLnItm.getAdditionalConsultant());
		if (saleLnItm.getManualUnitPrice() != null)
			aSaleLineItem.setManualUnitPrice(saleLnItm.getManualUnitPrice());
		if (saleLnItm.getManualMarkdownReason() != null)
			aSaleLineItem.setManualMarkdownReason(saleLnItm.getManualMarkdownReason());
		if (saleLnItm.isMiscItem())
			saleLnItm.transferInformationTo(aSaleLineItem);
		//Vivek Mishra : Added to show correct extended barcode in Create Fiscal scenario for Europe region : 05-JUL-2016
		if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION) && saleLnItm.getExtendedBarCode() != null)
		{
			aSaleLineItem.setExtendedBarCode(saleLnItm.getExtendedBarCode());
		}
		//Ends here
		return aSaleLineItem;
	}

}
