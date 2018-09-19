/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.rules.BusinessRuleException;
import java.util.Date;


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
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | String       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06-09-2005 | Megha     | N/A       | Initial                                            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ASISTxnData extends BusinessObject {
	private static final long serialVersionUID = -4721439238660297987L;
	private String CompanyCode = null;
	private String StoreId = null;
	private String RegId = null;
	private String TxnNo = null;
	private Date TxnDate = null;
	private String fiscalReceiptNo = null;
	private Date fiscalReceiptDate = null;
	private String fiscalDocNo = null;
	private Date fiscalDocDate = null;
	private String fiscalDocType = null;
	private String custNo = null;
	private String custName = null;
	private String comment = null;
	private ArmCurrency txnAmount = null;
	private String orderNo = null;
	private Date orderDate = null;
	private String supplierNo = null;
	private Date supplierDate = null;
	private String notes = null;

	/**
	 * put your documentation comment here
	 */
	public ASISTxnData() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Company Code
	 * @param sValue
	 *            String
	 */
	public void doSetCompanyCode(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		CompanyCode = sValue;
	}

	/**
	 * Company Code
	 * @param sValue
	 *            String
	 */
	public void setCompanyCode(String sValue) throws BusinessRuleException {
		executeRule("setCompanyCode", sValue);
		doSetCompanyCode(sValue);
	}

	/**
	 * Store Id
	 * @return String
	 */
	public String getCompanyCode() {
		return this.CompanyCode;
	}

	/**
	 * Store Id
	 * @param sValue
	 *            String
	 */
	public void doSetStoreId(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		this.StoreId = sValue;
	}

	/**
	 * Store Id
	 * @param sValue
	 *            String
	 */
	public void setStoreId(String sValue) throws BusinessRuleException {
		executeRule("setStoreId", sValue);
		doSetStoreId(sValue);
	}

	/**
	 * Store Id
	 * @return String
	 */
	public String getStoreId() {
		return this.StoreId;
	}

	/**
	 * RegId
	 * @param sValue
	 *            String
	 */
	public void doSetRegId(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		RegId = sValue;
	}

	/**
	 * RegId
	 * @param sValue
	 *            String
	 */
	public void setRegId(String sValue) throws BusinessRuleException {
		executeRule("setRegId", sValue);
		doSetRegId(sValue);
	}

	/**
	 * RegId
	 * @return String
	 */
	public String getRegId() {
		return this.RegId;
	}

	/**
	 * TxnNo
	 * @param sValue
	 *            String
	 */
	public void doSetTxnNo(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		this.TxnNo = sValue;
	}

	/**
	 * TxnNo
	 * @param sValue
	 *            String
	 */
	public void setTxnNo(String sValue) throws BusinessRuleException {
		executeRule("setTxnNo", sValue);
		doSetTxnNo(sValue);
	}

	/**
	 * TxnNo
	 * @return String
	 */
	public String getTxnNo() {
		return this.TxnNo;
	}

	/**
	 * TxnString
	 * @param sValue
	 *            String
	 */
	public void doSetTxnDate(Date sValue) {
		this.TxnDate = sValue;
	}

	/**
	 * TxnString
	 * @param sValue
	 *            String
	 */
	public void setTxnDate(Date sValue) throws BusinessRuleException {
		executeRule("setTxnDate", sValue);
		doSetTxnDate(sValue);
	}

	/**
	 * TxnString
	 * @return String
	 */
	public Date getTxnDate() {
		return this.TxnDate;
	}

	/**
	 * FiscalReceiptNo
	 * @param sValue
	 *            String
	 */
	public void doSetFiscalReceiptNo(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		this.fiscalReceiptNo = sValue;
	}

	/**
	 * FiscalReceiptNo
	 * @param sValue
	 *            String
	 */
	public void setFiscalReceiptNo(String sValue) throws BusinessRuleException {
		executeRule("setFiscalReceiptNo", sValue);
		doSetFiscalReceiptNo(sValue);
	}

	/**
	 * FiscalReceiptNo
	 * @return String
	 */
	public String getFiscalReceiptNo() {
		return this.fiscalReceiptNo;
	}

	/**
	 * FiscalReceiptString
	 * @param sValue
	 *            String
	 */
	public void doSetFiscalReceiptDate(Date sValue) {
		this.fiscalReceiptDate = sValue;
	}

	/**
	 * FiscalReceiptString
	 * @param sValue
	 *            String
	 */
	public void setFiscalReceiptDate(Date sValue) throws BusinessRuleException {
		executeRule("setFiscalReceiptDate", sValue);
		doSetFiscalReceiptDate(sValue);
	}

	/**
	 * FiscalReceiptString
	 * @return String
	 */
	public Date getFiscalReceiptDate() {
		return this.fiscalReceiptDate;
	}

	/**
	 * FiscalDocNo
	 * @param sValue
	 *            String
	 */
	public void doSetFiscalDocNo(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		this.fiscalDocNo = sValue;
	}

	/**
	 * FiscalDocNo
	 * @param sValue
	 *            String
	 */
	public void setFiscalDocNo(String sValue) throws BusinessRuleException {
		executeRule("setFiscalDocNo", sValue);
		doSetFiscalDocNo(sValue);
	}

	/**
	 * FiscalDocNo
	 * @return String
	 */
	public String getFiscalDocNo() {
		return this.fiscalDocNo;
	}

	/**
	 * FiscalDocString
	 * @param sValue
	 *            String
	 */
	public void doSetFiscalDocDate(Date sValue) {
		this.fiscalDocDate = sValue;
	}

	/**
	 * FiscalDocString
	 * @param sValue
	 *            String
	 */
	public void setFiscalDocDate(Date sValue) throws BusinessRuleException {
		executeRule("setFiscalDocDate", sValue);
		doSetFiscalDocDate(sValue);
	}

	/**
	 * FiscalDocString
	 * @return String
	 */
	public Date getFiscalDocDate() {
		return this.fiscalDocDate;
	}

	/**
	 * DocType
	 * @param sValue
	 *            String
	 */
	public void doSetDocType(String sValue) {
		this.fiscalDocType = sValue;
	}

	/**
	 * DocType
	 * @param sValue
	 *            String
	 */
	public void setDocType(String sValue) throws BusinessRuleException {
		executeRule("setDocType", sValue);
		doSetDocType(sValue);
	}

	/**
	 * DocType
	 * @return String
	 */
	public String getDocType() {
		return this.fiscalDocType;
	}

	/**
	 * CustNo
	 * @param sValue
	 *            String
	 */
	public void doSetCustNo(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		this.custNo = sValue;
	}

	/**
	 * CustNo
	 * @param sValue
	 *            String
	 */
	public void setCustNo(String sValue) throws BusinessRuleException {
		executeRule("setCustNo", sValue);
		doSetCustNo(sValue);
	}

	/**
	 * CustNo
	 * @return String
	 */
	public String getCustNo() {
		return this.custNo;
	}

	/**
	 * CustName
	 * @param sValue
	 *            String
	 */
	public void doSetCustName(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		this.custName = sValue;
	}

	/**
	 * CustName
	 * @param sValue
	 *            String
	 */
	public void setCustName(String sValue) throws BusinessRuleException {
		executeRule("setCustName", sValue);
		doSetCustName(sValue);
	}

	/**
	 * CustName
	 * @return String
	 */
	public String getCustName() {
		return this.custName;
	}

	/**
	 * Comments
	 * @param sValue
	 *            String
	 */
	public void doSetComments(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		this.comment = sValue;
	}

	/**
	 * Comments
	 * @param sValue
	 *            String
	 */
	public void setComments(String sValue) throws BusinessRuleException {
		executeRule("setComments", sValue);
		doSetComments(sValue);
	}

	/**
	 * Comments
	 * @return String
	 */
	public String getComments() {
		return this.comment;
	}

	/**
	 * Transaction Amount
	 * @param sValue
	 *            String
	 */
	public void doSetTxnAmount(ArmCurrency sValue) {
		txnAmount = sValue;
	}

	/**
	 * Transaction Amount
	 * @param sValue
	 *            String
	 */
	public void setTxnAmount(ArmCurrency sValue) throws BusinessRuleException {
		executeRule("setTxnAmount", sValue);
		doSetTxnAmount(sValue);
	}

	/**
	 * Transaction Amount
     * @return String
	 */
	public ArmCurrency getTxnAmount() {
		return this.txnAmount;
	}

	/**
	 * Order No
	 * @param sValue
	 *            String
	 */
	public void doSetOrderNo(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		orderNo = sValue;
	}

	/**
	 * Order No
	 * @param sValue
	 *            String
	 */
	public void setOrderNo(String sValue) throws BusinessRuleException {
		executeRule("setOrderNo", sValue);
		doSetOrderNo(sValue);
	}

	/**
	 * Order No
	 * @return String
	 */
	public String getOrderNo() {
		return this.orderNo;
	}

	/**
	 * Order Date
	 * @param sValue
	 *            String
	 */
	public void doSetOrderDate(Date sValue) {
		this.orderDate = sValue;
	}

	/**
	 * Order Date
	 * @param sValue
	 *            String
	 */
	public void setOrderDate(Date sValue) throws BusinessRuleException {
		executeRule("setTxnDate", sValue);
		doSetOrderDate(sValue);
	}

	/**
	 * Order Date
	 * @return String
	 */
	public Date getOrderDate() {
		return this.orderDate;
	}

	/**
	 * Supplier No
	 * @param sValue
	 *            String
	 */
	public void doSetSupplierNo(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		supplierNo = sValue;
	}

	/**
	 * Supplier No
 	 * @param sValue
	 *            String
	 */
	public void setSupplierNo(String sValue) throws BusinessRuleException {
		executeRule("setSupplierNo", sValue);
		doSetSupplierNo(sValue);
	}

	/**
	 * Supplier No
	 * @return String
	 */
	public String getSupplierNo() {
		return this.supplierNo;
	}

	/**
	 * Supplier Date
	 * @param sValue
	 *            String
	 */
	public void doSetSupplierDate(Date sValue) {
		this.supplierDate = sValue;
	}

	/**
	 * Supplier Date
	 * @param sValue
	 *            String
	 */
	public void setSupplierDate(Date sValue) throws BusinessRuleException {
		executeRule("setTxnDate", sValue);
		doSetSupplierDate(sValue);
	}

	/**
	 * Supplier Date
	 * @return String
	 */
	public Date getSupplierDate() {
		return this.supplierDate;
	}

	/**
	 * Notes
	 * @param sValue
	 *            String
	 */
	public void doSetNotes(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		notes = sValue;
	}

	/**
	 * Notes
	 * @param sValue
	 *            String
	 */
	public void setNotes(String sValue) throws BusinessRuleException {
		executeRule("setNotes", sValue);
		doSetNotes(sValue);
	}

	/**
	 * Notes
	 * @return String
	 */
	public String getNotes() {
		return this.notes;
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
	}
}
