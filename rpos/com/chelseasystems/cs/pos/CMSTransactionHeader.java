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
 | 5    | 05-10-2005 | Manpreet  | N/A       |added  (isShipping, sStoreType, SCurrencyCode)      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-29-2005 | Pankaja   | N/A       |added new attrib (Void Id)                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-14-2005 | Rajesh    | N/A       |added new attrib                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.TransactionHeader;
import java.util.Date;

/**
 *
 * <p>Title: CMSTransactionHeader</p>
 *
 * <p>Description: This is transaction header class, store the details of transaction </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTransactionHeader extends TransactionHeader implements com.chelseasystems.cr.rules.IRuleEngine {
	static final long serialVersionUID = -6767724080842030307L;
	private String customerId, refId, payTxnType;
	private Date expDt;
	private String voidID;
	private Long isShipping;
	private String sStoreType;
	private String sStoreName;
	private String sCurrencyCode;
	private String sCustomerFName;
	private String sCustomerLName;
	private String sFiscalReceiptNum;
	private String sRegisterId;

	/**
	 * constructor
	 * 
	 * @param anId
	 *            String
	 */
	public CMSTransactionHeader(String anId) {
		super(anId);
	}

	/**
	 * @param sType
	 *            String
	 */
	public void doSetStoreType(String sType) {
		this.sStoreType = sType;
	}

	/**
	 * @return String
	 */
	public String getStoreType() {
		return this.sStoreType;
	}

	/**
	 * @param sType
	 *            String
	 */
	public void doSetStoreName(String sStoreName) {
		this.sStoreName = sStoreName;
	}

	/**
	 * @return String
	 */
	public String getStoreName() {
		return this.sStoreName;
	}

	/**
	 * @param sCode
	 *            String
	 */
	public void doSetCurrencyCode(String sCode) {
		this.sCurrencyCode = sCode;
	}

	/**
	 * @return String
	 */
	public String getCurrencyCode() {
		return this.sCurrencyCode;
	}

	/**
	 * @param isShipping
	 *            Long
	 */
	public void doSetIsShipping(Long isShipping) {
		this.isShipping = isShipping;
	}

	/**
	 * @return Long
	 */
	public Long getIsShipping() {
		return this.isShipping;
	}

	/**
	 * This method is used to set the expiration date
	 * 
	 * @param val
	 *            Date
	 */
	public void doSetExpirationDate(Date val) {
		this.expDt = val;
	}

	/**
	 * This method is used to get the expiration date
	 * 
	 * @return Date
	 */
	public Date getExpirationDate() {
		return this.expDt;
	}

	/**
	 * This method is used to set the reference id
	 * 
	 * @param val
	 *            String
	 */
	public void doSetRefId(String val) {
		this.refId = val;
	}

	/**
	 * This method is used to get the reference id
	 * 
	 * @return String
	 */
	public String getRefId() {
		return this.refId;
	}

	/**
	 * This method is used to set the pay transaction type
	 * 
	 * @param val
	 *            String
	 */
	public void doSetPayTxnType(String val) {
		this.payTxnType = val;
	}

	/**
	 * This method is used to get the pay transaction type
	 * 
	 * @return String
	 */
	public String getPayTxnType() {
		return this.payTxnType;
	}

	/**
	 * This method is used to set the customer id
	 * 
	 * @param val
	 *            String
	 */
	public void doSetCustomerId(String val) {
		this.customerId = val;
	}

	/**
	 * This method is used to set the customer id
	 * 
	 * @param val
	 *            String
	 */
	public void setCustomerId(String val) {
		doSetCustomerId(val);
	}

	/**
	 * This method is used to get the customer id
	 * 
	 * @return String
	 */
	public String getCustomerId() {
		return this.customerId;
	}

	/**
	 * This method is used to get the void id
	 * 
	 * @return String
	 */
	public String getVoidID() {
		return voidID;
	}

	/**
	 * This method is used to set the void id
	 * 
	 * @param voidID
	 *            String
	 */
	public void doSetVoidID(String voidID) {
		this.voidID = voidID;
	}

	/**
	 * This method is used to set the void id
	 * 
	 * @param voidID
	 *            String
	 */
	public void setVoidID(String voidID) {
		doSetVoidID(voidID);
	}

	/**
	 * This method is used to get the CustomerFirstName
	 * 
	 * @return String
	 */
	public String getCustomerFirstName() {
		return sCustomerFName;
	}

	/**
	 * This method is used to set the CustomerFirstName
	 * 
	 * @param voidID
	 *            String
	 */
	public void doSetCustomerFirstName(String CustomerFirstName) {
		this.sCustomerFName = CustomerFirstName;
	}

	/**
	 * This method is used to set the CustomerFirstName
	 * 
	 * @param voidID
	 *            String
	 */
	public void setCustomerFirstName(String CustomerFirstName) {
		doSetCustomerFirstName(CustomerFirstName);
	}

	/**
	 * This method is used to get the CustomerLastName
	 * 
	 * @return String
	 */
	public String getCustomerLastName() {
		return this.sCustomerLName;
	}

	/**
	 * This method is used to set the CustomerLastName
	 * 
	 * @param voidID
	 *            String
	 */
	public void doSetCustomerLastName(String CustomerLastName) {
		this.sCustomerLName = CustomerLastName;
	}

	/**
	 * This method is used to set the CustomerLastName
	 * 
	 * @param voidID
	 *            String
	 */
	public void setCustomerLastName(String CustomerLastName) {
		doSetCustomerLastName(CustomerLastName);
	}

	/**
	 * This method is used to get the Fiscal Receipt Number
	 * 
	 * @return String
	 */
	public String getFiscalReceiptNum() {
		return (this.sFiscalReceiptNum == null) ? "" : this.sFiscalReceiptNum;
	}

	/**
	 * This method is used to set the Fiscal Receipt Number
	 * 
	 * @param sFiscalReceiptNum
	 *            String
	 */
	public void doSetFiscalReceiptNum(String sFiscalReceiptNum) {
		this.sFiscalReceiptNum = sFiscalReceiptNum;
	}

	/**
	 * This method is used to set the Fiscal Receipt Number
	 * 
	 * @param sFiscalReceiptNum
	 *            String
	 */
	public void setFiscalReceiptNum(String sFiscalReceiptNum) {
		doSetFiscalReceiptNum(sFiscalReceiptNum);
	}
	
	/**
	 * This method is used to get the register Id 
	 * @return String
	 */
	public String getRegisterId() {
		return (this.sRegisterId == null) ? "" : this.sRegisterId;
	}
	
	/**
	 * @param sRegisterId
	 *            String
	 */
	public void doSetRegisterId(String sRegisterId) {
		this.sRegisterId = sRegisterId;
	}

	/**
	 * This method is used to set the register Id
	 * 
	 * @param sRegisterId
	 *            String
	 */
	public void setRegisterId(String sRegisterId) {
		doSetRegisterId(sRegisterId);
	}
}
