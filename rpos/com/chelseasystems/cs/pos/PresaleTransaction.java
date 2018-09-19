/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;

/**
 *
 * <p>Title: PresaleTransaction</p>
 *
 * <p>Description: This class store the pre sale transaction attribute</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public class PresaleTransaction extends POSTransaction implements IRuleEngine, Serializable {
	private String prsId;
	private Date expDate;
	private String ccNumber;
	private Date ccExpDt;
	private String ccZipCode;
	private String ccType;
	private boolean isCreditCardSaved = false;

	/**
	 * Constructor
	 * @param compTxn CompositePOSTransaction
	 */
	public PresaleTransaction(CompositePOSTransaction compTxn) {
		super(compTxn);
	}

	/**
	 * This method is used to create the line item for pre sale
	 * @param anItem Item
	 * @return POSLineItem
	 */
	public POSLineItem createLineItem(Item anItem) {
		return new CMSPresaleLineItem(this, anItem);
	}

	/**
	 * This method is used to create the line item grouping for pre sale
	 * @param lineItem POSLineItem
	 * @return POSLineItemGrouping
	 */
	public POSLineItemGrouping createLineItemGrouping(POSLineItem lineItem) {
		return new PresaleLineItemGrouping(lineItem);
	}

	/**
	 * This method is used to set expiration date for pre sale
	 * @param val Date
	 */
	public void doSetExpirationDate(Date val) {
		this.expDate = val;
	}

	/**
	 * This method is used to set expiration date for pre sale
	 * @param val Date
	 */
	public void setExpirationDate(Date val) {
		doSetExpirationDate(val);
	}

	/**
	 * This method is used to get expiration date for pre sale
	 * @return Date
	 */
	public Date getExpirationDate() {
		return this.expDate;
	}

	/**
	 * This method is used to set pre sale transaction id
	 * @param val String
	 */
	public void doSetPresaleId(String val) {
		this.prsId = val;
	}

	/**
	 * This method is used to get pre sale transaction id
	 * @return String
	 */
	public String getPresaleId() {
		return this.prsId;
	}

	/**
	 * This method is used to get net amount of the pre sale
	 * @return Currency
	 */
	public ArmCurrency getNetAmount() {
		try {
		ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
			for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements();) {
				//        ArmCurrency aLineItemExtendedNetAmount = ((CMSPresaleLineItem)aLineItemList.nextElement()).
				//            getExtendedRetailAmount();
				CMSPresaleLineItem lineItem = (CMSPresaleLineItem) aLineItemList.nextElement();
			ArmCurrency aLineItemExtendedNetAmount = lineItem.getExtendedRetailAmount().subtract(
						lineItem.getExtendedDealMarkdownAmount());
				total = total.add(aLineItemExtendedNetAmount);
			}
			return total;
		} catch (CurrencyException anException) {
			logCurrencyException("getNetAmount", anException);
		}
		return null;
	}

	public void setCreditCardNumber(String ccNumber) {
		this.doSetCreidtCardNumber(ccNumber);
	}

	public void doSetCreidtCardNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getCreditCardNumber() {
		return this.ccNumber;
	}

	public String doGetCreditCardNumber() {
		return this.ccNumber;
	}

	public void setCardExpirationDate(Date ccExpDt) {
		this.doSetCardExpirationDate(ccExpDt);
	}

	public void doSetCardExpirationDate(Date ccExpDt) {
		this.ccExpDt = ccExpDt;
	}

	public Date getCardExpirationDate() {
		return this.ccExpDt;
	}

	public Date doGetCardExpirationDate() {
		return this.ccExpDt;
	}

	public void setCardZipcode(String ccZipCode) {
		this.doSetCardZipcode(ccZipCode);
	}

	public void doSetCardZipcode(String ccZipCode) {
		this.ccZipCode = ccZipCode;
	}

	public String getCardZipCode() {
		return this.ccZipCode;
	}

	public String doGetCardZipCode() {
		return this.ccZipCode;
	}

	public void setCardType(String ccType) {
		this.doSetCardType(ccType);
	}

	public void doSetCardType(String ccType) {
		this.ccType = ccType;
	}

	public String getCardType() {
		return this.ccType;
	}

	public String doGetCardType() {
		return this.ccType;
	}

	public boolean getIsCreditCardSaved() {
		return this.isCreditCardSaved;
	}

	public void setIsCreditCardSaved(boolean isCreditCardSaved) {
		this.isCreditCardSaved = isCreditCardSaved;
	}
}
