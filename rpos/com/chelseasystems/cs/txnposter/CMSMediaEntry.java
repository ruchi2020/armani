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
 | 1    | 04-29-2005 | Pankaja   | N/A       |To compute the media count                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
package com.chelseasystems.cs.txnposter;

import com.chelseasystems.cr.txnposter.MediaEntry;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.payment.CMSForeignCash;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSMediaEntry extends MediaEntry {
	int mediaCount = 0;
	private Payment payment;
	private ArmCurrency totalAmt;
	public final String VISA = "VISA";
	public final String MASTER_CARD = "MASTER_CARD";

	/**
	 * put your documentation comment here
	 * @param Payment
	 *            payment
	 */
	public CMSMediaEntry(Payment payment) {
		super(payment); // Just to make it compatible
		this.payment = payment;
		if (payment.isForeign()) {
		ArmCurrency tempTotal = new ArmCurrency(payment.getCurrencyTypeConvertedFrom(), 0.0D);
			try {
				totalAmt = tempTotal.convertTo(ArmCurrency.getBaseCurrencyType());
			} catch (MissingExchangeRateException missingexchangerateexception) {
				System.err.println("MediaEntry()-> Exchange rate missing for conversion between " 
					+ payment.getCurrencyTypeConvertedFrom().getDescription() 
					+ " and " + ArmCurrency.getBaseCurrencyType().getDescription() + ".");
				totalAmt = new ArmCurrency(0.0D);
			} catch (UnsupportedCurrencyTypeException unsupportedcurrencytypeexception) {
				System.err.println("MediaEntry()-> Unsupported currency type.");
				totalAmt = new ArmCurrency(0.0D);
			}
		}	
		else
			totalAmt = new ArmCurrency(0.0d);
	}

	public ArmCurrency getTotalAmount() {
		if (payment.isForeign() && !(payment instanceof CMSForeignCash)) {
			return totalAmt.getConvertedFrom();
		} else {
			return totalAmt;
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public int getMediaCount() {
		return mediaCount;
	}

	/**
	 * put your documentation comment here
	 * @param mediaCount
	 */
	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}

	/**
	 * put your documentation comment here
	 * @param amt
	 */
	public void addAmount(ArmCurrency amt) {
		try {
		ArmCurrency total = this.getTotalAmount().add(amt);
			if (!total.getCurrencyType().equals(ArmCurrency.getBaseCurrencyType()) && payment.isForeign()) {
				total = total.convertTo(ArmCurrency.getBaseCurrencyType(), new ArmCurrency(ArmCurrency.getConversionRate(amt, payment.getAmount().getConvertedFrom()).doubleValue()));
			}
			totalAmt = total;
		} catch (Exception ex) {
			System.out.println("Exception addAmount()->" + ex);
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getPaymentID() {
		System.out.println("Ruchi inside getPaymentID()");
		if (payment instanceof Visa) {
			return VISA;
		}
		if (payment instanceof MasterCard) {
			return MASTER_CARD;
		}
		if (payment instanceof CMSForeignCash) {
			return ((CMSForeignCash) payment).getEODTenderType();
		}
		return payment.getTransactionPaymentName();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getPaymentCode() {
		System.out.println("Ruchi printing PaymentCode  :"+payment.getPaymentCode());
		return payment.getPaymentCode();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getPaymentDescription() {
		return payment.getGUIPaymentName();
	}
}
