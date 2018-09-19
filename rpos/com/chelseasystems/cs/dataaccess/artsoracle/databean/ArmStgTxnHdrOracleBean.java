//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import com.chelseasystems.cr.currency.ArmCurrency;

/**
 *
 * This class is an object representation of the Arts database table ARM_STG_TXN_HDR<BR>
 * Followings are the column of the table: <BR>
 *     TRANSACTION_ID -- VARCHAR2(50)<BR>
 *     TRANSACTION_TYPE -- VARCHAR2(6)<BR>
 *     PAY_TXN_TYPE -- VARCHAR2(20)<BR>
 *     TRANSACTION_NUM -- NUMBER(9)<BR>
 *     ADD_NO -- VARCHAR2(20)<BR>
 *     FISCAL_NO -- VARCHAR2(128)<BR>
 *     FISCAL_RECEIPT_DATE -- DATE(7)<BR>
 *     ASIS_COMPANY_CD -- VARCHAR2(20)<BR>
 *     ASIS_STORE_ID -- VARCHAR2(20)<BR>
 *     ASIS_REGISTER_ID -- VARCHAR2(20)<BR>
 *     ASIS_TXN_NUM -- VARCHAR2(50)<BR>
 *     ASIS_TXN_DATE -- DATE(7)<BR>
 *     ASIS_FISCAL_RECEIPT_NO -- VARCHAR2(50)<BR>
 *     ASIS_FISCAL_DATE -- DATE(7)<BR>
 *     ASIS_FISCAL_DOC_NUM -- VARCHAR2(20)<BR>
 *     ASIS_FISCAL_DOC_DATE -- DATE(7)<BR>
 *     ASIS_FISCAL_DOC_TYPE -- CHAR(2)<BR>
 *     ASIS_CUSTOMER_ID -- VARCHAR2(50)<BR>
 *     ASIS_CUSTOMER_NAME -- VARCHAR2(50)<BR>
 *     ASIS_COMMENTS -- VARCHAR2(200)<BR>
 *     TXN_CATEGORY -- CHAR(1)<BR>
 *     ENTRY_DATE -- DATE(7)<BR>
 *     TR_BEGIN_DATE -- DATE(7)<BR>
 *     TR_END_DATE -- DATE(7)<BR>
 *     TRAINING_FLAG -- CHAR(1)<BR>
 *     POST_DATE -- DATE(7)<BR>
 *     SUBMIT_DATE -- DATE(7)<BR>
 *     CREATE_DATE -- DATE(7)<BR>
 *     PROCESS_DATE -- DATE(7)<BR>
 *     VOID_FLAG -- CHAR(1)<BR>
 *     CASHIER -- VARCHAR2(50)<BR>
 *     REGISTER_ID -- VARCHAR2(50)<BR>
 *     STORE_ID -- VARCHAR2(50)<BR>
 *     COMPANY_ID -- VARCHAR2(50)<BR>
 *     EMPLOYEE_ID -- VARCHAR2(50)<BR>
 *     CUSTOMER_ID -- VARCHAR2(50)<BR>
 *     CONSULTANT_ID -- VARCHAR2(50)<BR>
 *     TAX_EXEMPT_ID -- VARCHAR2(20)<BR>
 *     TAX_JURISDICTION -- VARCHAR2(20)<BR>
 *     CLOSEOUT_FLAG -- CHAR(1)<BR>
 *     CURRENCY_CD -- CHAR(3)<BR>
 *     REDUCTION_AMOUNT -- NUMBER(10.2)<BR>
 *     NET_AMOUNT -- NUMBER(10.2)<BR>
 *     STATUS -- CHAR(1)<BR>
 *     DATA_POPULATION_DT -- DATE(7)<BR>
 *     EXTRACTED_DT -- DATE(7)<BR>
 *     ASIS_ORDER_NO -- VARCHAR2(50)<BR>
 *     ASIS_ORDER_DATE -- DATE(7)<BR>
 *     ASIS_SUPPLIER_NO -- VARCHAR2(50)<BR>
 *     ASIS_SUPPLIER_DATE -- DATE(7)<BR>
 *     ASIS_NOTES -- VARCHAR2(200)<BR>
 *     APPROVER_ID -- VARCHAR2(128)<BR>
 */
public class ArmStgTxnHdrOracleBean extends BaseOracleBean {

  public ArmStgTxnHdrOracleBean() {}

  public static String selectSql = "select TRANSACTION_ID, TRANSACTION_TYPE, PAY_TXN_TYPE, TRANSACTION_NUM, ADD_NO, FISCAL_NO, FISCAL_RECEIPT_DATE, ASIS_COMPANY_CD, ASIS_STORE_ID, ASIS_REGISTER_ID, ASIS_TXN_NUM, ASIS_TXN_DATE, ASIS_FISCAL_RECEIPT_NO, ASIS_FISCAL_DATE, ASIS_FISCAL_DOC_NUM, ASIS_FISCAL_DOC_DATE, ASIS_FISCAL_DOC_TYPE, ASIS_CUSTOMER_ID, ASIS_CUSTOMER_NAME, ASIS_COMMENTS, TXN_CATEGORY, ENTRY_DATE, TR_BEGIN_DATE, TR_END_DATE, TRAINING_FLAG, POST_DATE, SUBMIT_DATE, CREATE_DATE, PROCESS_DATE, VOID_FLAG, CASHIER, REGISTER_ID, STORE_ID, COMPANY_ID, EMPLOYEE_ID, CUSTOMER_ID, CONSULTANT_ID, TAX_EXEMPT_ID, TAX_JURISDICTION, CLOSEOUT_FLAG, CURRENCY_CD, REDUCTION_AMOUNT, NET_AMOUNT, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, ASIS_ORDER_NO, ASIS_ORDER_DATE, ASIS_SUPPLIER_NO, ASIS_SUPPLIER_DATE, ASIS_NOTES,APPROVER_ID from ARM_STG_TXN_HDR ";
  public static String insertSql = "insert into ARM_STG_TXN_HDR (TRANSACTION_ID, TRANSACTION_TYPE, PAY_TXN_TYPE, TRANSACTION_NUM, ADD_NO, FISCAL_NO, FISCAL_RECEIPT_DATE, ASIS_COMPANY_CD, ASIS_STORE_ID, ASIS_REGISTER_ID, ASIS_TXN_NUM, ASIS_TXN_DATE, ASIS_FISCAL_RECEIPT_NO, ASIS_FISCAL_DATE, ASIS_FISCAL_DOC_NUM, ASIS_FISCAL_DOC_DATE, ASIS_FISCAL_DOC_TYPE, ASIS_CUSTOMER_ID, ASIS_CUSTOMER_NAME, ASIS_COMMENTS, TXN_CATEGORY, ENTRY_DATE, TR_BEGIN_DATE, TR_END_DATE, TRAINING_FLAG, POST_DATE, SUBMIT_DATE, CREATE_DATE, PROCESS_DATE, VOID_FLAG, CASHIER, REGISTER_ID, STORE_ID, COMPANY_ID, EMPLOYEE_ID, CUSTOMER_ID, CONSULTANT_ID, TAX_EXEMPT_ID, TAX_JURISDICTION, CLOSEOUT_FLAG, CURRENCY_CD, REDUCTION_AMOUNT, NET_AMOUNT, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, ASIS_ORDER_NO, ASIS_ORDER_DATE, ASIS_SUPPLIER_NO, ASIS_SUPPLIER_DATE, ASIS_NOTES,APPROVER_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_STG_TXN_HDR set TRANSACTION_ID = ?, TRANSACTION_TYPE = ?, PAY_TXN_TYPE = ?, TRANSACTION_NUM = ?, ADD_NO = ?, FISCAL_NO = ?, FISCAL_RECEIPT_DATE = ?, ASIS_COMPANY_CD = ?, ASIS_STORE_ID = ?, ASIS_REGISTER_ID = ?, ASIS_TXN_NUM = ?, ASIS_TXN_DATE = ?, ASIS_FISCAL_RECEIPT_NO = ?, ASIS_FISCAL_DATE = ?, ASIS_FISCAL_DOC_NUM = ?, ASIS_FISCAL_DOC_DATE = ?, ASIS_FISCAL_DOC_TYPE = ?, ASIS_CUSTOMER_ID = ?, ASIS_CUSTOMER_NAME = ?, ASIS_COMMENTS = ?, TXN_CATEGORY = ?, ENTRY_DATE = ?, TR_BEGIN_DATE = ?, TR_END_DATE = ?, TRAINING_FLAG = ?, POST_DATE = ?, SUBMIT_DATE = ?, CREATE_DATE = ?, PROCESS_DATE = ?, VOID_FLAG = ?, CASHIER = ?, REGISTER_ID = ?, STORE_ID = ?, COMPANY_ID = ?, EMPLOYEE_ID = ?, CUSTOMER_ID = ?, CONSULTANT_ID = ?, TAX_EXEMPT_ID = ?, TAX_JURISDICTION = ?, CLOSEOUT_FLAG = ?, CURRENCY_CD = ?, REDUCTION_AMOUNT = ?, NET_AMOUNT = ?, STATUS = ?, DATA_POPULATION_DT = ?, EXTRACTED_DT = ?, ASIS_ORDER_NO = ?, ASIS_ORDER_DATE = ?, ASIS_SUPPLIER_NO = ?, ASIS_SUPPLIER_DATE = ?, ASIS_NOTES = ?,APPROVER_ID = ? ";
  public static String deleteSql = "delete from ARM_STG_TXN_HDR ";

  public static String TABLE_NAME = "ARM_STG_TXN_HDR";
  public static String COL_TRANSACTION_ID = "ARM_STG_TXN_HDR.TRANSACTION_ID";
  public static String COL_TRANSACTION_TYPE = "ARM_STG_TXN_HDR.TRANSACTION_TYPE";
  public static String COL_PAY_TXN_TYPE = "ARM_STG_TXN_HDR.PAY_TXN_TYPE";
  public static String COL_TRANSACTION_NUM = "ARM_STG_TXN_HDR.TRANSACTION_NUM";
  public static String COL_ADD_NO = "ARM_STG_TXN_HDR.ADD_NO";
  public static String COL_FISCAL_NO = "ARM_STG_TXN_HDR.FISCAL_NO";
  public static String COL_FISCAL_RECEIPT_DATE = "ARM_STG_TXN_HDR.FISCAL_RECEIPT_DATE";
  public static String COL_ASIS_COMPANY_CD = "ARM_STG_TXN_HDR.ASIS_COMPANY_CD";
  public static String COL_ASIS_STORE_ID = "ARM_STG_TXN_HDR.ASIS_STORE_ID";
  public static String COL_ASIS_REGISTER_ID = "ARM_STG_TXN_HDR.ASIS_REGISTER_ID";
  public static String COL_ASIS_TXN_NUM = "ARM_STG_TXN_HDR.ASIS_TXN_NUM";
  public static String COL_ASIS_TXN_DATE = "ARM_STG_TXN_HDR.ASIS_TXN_DATE";
  public static String COL_ASIS_FISCAL_RECEIPT_NO = "ARM_STG_TXN_HDR.ASIS_FISCAL_RECEIPT_NO";
  public static String COL_ASIS_FISCAL_DATE = "ARM_STG_TXN_HDR.ASIS_FISCAL_DATE";
  public static String COL_ASIS_FISCAL_DOC_NUM = "ARM_STG_TXN_HDR.ASIS_FISCAL_DOC_NUM";
  public static String COL_ASIS_FISCAL_DOC_DATE = "ARM_STG_TXN_HDR.ASIS_FISCAL_DOC_DATE";
  public static String COL_ASIS_FISCAL_DOC_TYPE = "ARM_STG_TXN_HDR.ASIS_FISCAL_DOC_TYPE";
  public static String COL_ASIS_CUSTOMER_ID = "ARM_STG_TXN_HDR.ASIS_CUSTOMER_ID";
  public static String COL_ASIS_CUSTOMER_NAME = "ARM_STG_TXN_HDR.ASIS_CUSTOMER_NAME";
  public static String COL_ASIS_COMMENTS = "ARM_STG_TXN_HDR.ASIS_COMMENTS";
  public static String COL_TXN_CATEGORY = "ARM_STG_TXN_HDR.TXN_CATEGORY";
  public static String COL_ENTRY_DATE = "ARM_STG_TXN_HDR.ENTRY_DATE";
  public static String COL_TR_BEGIN_DATE = "ARM_STG_TXN_HDR.TR_BEGIN_DATE";
  public static String COL_TR_END_DATE = "ARM_STG_TXN_HDR.TR_END_DATE";
  public static String COL_TRAINING_FLAG = "ARM_STG_TXN_HDR.TRAINING_FLAG";
  public static String COL_POST_DATE = "ARM_STG_TXN_HDR.POST_DATE";
  public static String COL_SUBMIT_DATE = "ARM_STG_TXN_HDR.SUBMIT_DATE";
  public static String COL_CREATE_DATE = "ARM_STG_TXN_HDR.CREATE_DATE";
  public static String COL_PROCESS_DATE = "ARM_STG_TXN_HDR.PROCESS_DATE";
  public static String COL_VOID_FLAG = "ARM_STG_TXN_HDR.VOID_FLAG";
  public static String COL_CASHIER = "ARM_STG_TXN_HDR.CASHIER";
  public static String COL_REGISTER_ID = "ARM_STG_TXN_HDR.REGISTER_ID";
  public static String COL_STORE_ID = "ARM_STG_TXN_HDR.STORE_ID";
  public static String COL_COMPANY_ID = "ARM_STG_TXN_HDR.COMPANY_ID";
  public static String COL_EMPLOYEE_ID = "ARM_STG_TXN_HDR.EMPLOYEE_ID";
  public static String COL_CUSTOMER_ID = "ARM_STG_TXN_HDR.CUSTOMER_ID";
  public static String COL_CONSULTANT_ID = "ARM_STG_TXN_HDR.CONSULTANT_ID";
  public static String COL_TAX_EXEMPT_ID = "ARM_STG_TXN_HDR.TAX_EXEMPT_ID";
  public static String COL_TAX_JURISDICTION = "ARM_STG_TXN_HDR.TAX_JURISDICTION";
  public static String COL_CLOSEOUT_FLAG = "ARM_STG_TXN_HDR.CLOSEOUT_FLAG";
  public static String COL_CURRENCY_CD = "ARM_STG_TXN_HDR.CURRENCY_CD";
  public static String COL_REDUCTION_AMOUNT = "ARM_STG_TXN_HDR.REDUCTION_AMOUNT";
  public static String COL_NET_AMOUNT = "ARM_STG_TXN_HDR.NET_AMOUNT";
  public static String COL_STATUS = "ARM_STG_TXN_HDR.STATUS";
  public static String COL_DATA_POPULATION_DT = "ARM_STG_TXN_HDR.DATA_POPULATION_DT";
  public static String COL_EXTRACTED_DT = "ARM_STG_TXN_HDR.EXTRACTED_DT";
  public static String COL_ASIS_ORDER_NO = "ARM_STG_TXN_HDR.ASIS_ORDER_NO";
  public static String COL_ASIS_ORDER_DATE = "ARM_STG_TXN_HDR.ASIS_ORDER_DATE";
  public static String COL_ASIS_SUPPLIER_NO = "ARM_STG_TXN_HDR.ASIS_SUPPLIER_NO";
  public static String COL_ASIS_SUPPLIER_DATE = "ARM_STG_TXN_HDR.ASIS_SUPPLIER_DATE";
  public static String COL_ASIS_NOTES = "ARM_STG_TXN_HDR.ASIS_NOTES";
  //Added by Rachana for approval of return transaction
  public static String COL_APPROVER_ID = "ARM_STG_TXN_HDR.APPROVER_ID";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String transactionId;
  private String transactionType;
  private String payTxnType;
  private Long transactionNum;
  private String addNo;
  private String fiscalNo;
  private Date fiscalReceiptDate;
  private String asisCompanyCd;
  private String asisStoreId;
  private String asisRegisterId;
  private String asisTxnNum;
  private Date asisTxnDate;
  private String asisFiscalReceiptNo;
  private Date asisFiscalDate;
  private String asisFiscalDocNum;
  private Date asisFiscalDocDate;
  private String asisFiscalDocType;
  private String asisCustomerId;
  private String asisCustomerName;
  private String asisComments;
  private String txnCategory;
  private Date entryDate;
  private Date trBeginDate;
  private Date trEndDate;
  private String trainingFlag;
  private Date postDate;
  private Date submitDate;
  private Date createDate;
  private Date processDate;
  private String voidFlag;
  private String cashier;
  private String registerId;
  private String storeId;
  private String companyId;
  private String employeeId;
  private String customerId;
  private String consultantId;
  private String taxExemptId;
  private String taxJurisdiction;
  private String closeoutFlag;
  private String currencyCd;
  private Double reductionAmount;
  private Double netAmount;
  private String status;
  private Date dataPopulationDt;
  private Date extractedDt;
  private String asisOrderNo;
  private Date asisOrderDate;
  private String asisSupplierNo;
  private Date asisSupplierDate;
  private String asisNotes;
  //Added by Rachana for approval of return transaction
  private String approverId;
 
  public String getTransactionId() { return this.transactionId; }
  public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

  public String getTransactionType() { return this.transactionType; }
  public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

  public String getPayTxnType() { return this.payTxnType; }
  public void setPayTxnType(String payTxnType) { this.payTxnType = payTxnType; }

  public Long getTransactionNum() { return this.transactionNum; }
  public void setTransactionNum(Long transactionNum) { this.transactionNum = transactionNum; }
  public void setTransactionNum(long transactionNum) { this.transactionNum = new Long(transactionNum); }
  public void setTransactionNum(int transactionNum) { this.transactionNum = new Long((long)transactionNum); }

  public String getAddNo() { return this.addNo; }
  public void setAddNo(String addNo) { this.addNo = addNo; }

  public String getFiscalNo() { return this.fiscalNo; }
  public void setFiscalNo(String fiscalNo) { this.fiscalNo = fiscalNo; }

  public Date getFiscalReceiptDate() { return this.fiscalReceiptDate; }
  public void setFiscalReceiptDate(Date fiscalReceiptDate) { this.fiscalReceiptDate = fiscalReceiptDate; }

  public String getAsisCompanyCd() { return this.asisCompanyCd; }
  public void setAsisCompanyCd(String asisCompanyCd) { this.asisCompanyCd = asisCompanyCd; }

  public String getAsisStoreId() { return this.asisStoreId; }
  public void setAsisStoreId(String asisStoreId) { this.asisStoreId = asisStoreId; }

  public String getAsisRegisterId() { return this.asisRegisterId; }
  public void setAsisRegisterId(String asisRegisterId) { this.asisRegisterId = asisRegisterId; }

  public String getAsisTxnNum() { return this.asisTxnNum; }
  public void setAsisTxnNum(String asisTxnNum) { this.asisTxnNum = asisTxnNum; }

  public Date getAsisTxnDate() { return this.asisTxnDate; }
  public void setAsisTxnDate(Date asisTxnDate) { this.asisTxnDate = asisTxnDate; }

  public String getAsisFiscalReceiptNo() { return this.asisFiscalReceiptNo; }
  public void setAsisFiscalReceiptNo(String asisFiscalReceiptNo) { this.asisFiscalReceiptNo = asisFiscalReceiptNo; }

  public Date getAsisFiscalDate() { return this.asisFiscalDate; }
  public void setAsisFiscalDate(Date asisFiscalDate) { this.asisFiscalDate = asisFiscalDate; }

  public String getAsisFiscalDocNum() { return this.asisFiscalDocNum; }
  public void setAsisFiscalDocNum(String asisFiscalDocNum) { this.asisFiscalDocNum = asisFiscalDocNum; }

  public Date getAsisFiscalDocDate() { return this.asisFiscalDocDate; }
  public void setAsisFiscalDocDate(Date asisFiscalDocDate) { this.asisFiscalDocDate = asisFiscalDocDate; }

  public String getAsisFiscalDocType() { return this.asisFiscalDocType; }
  public void setAsisFiscalDocType(String asisFiscalDocType) { this.asisFiscalDocType = asisFiscalDocType; }

  public String getAsisCustomerId() { return this.asisCustomerId; }
  public void setAsisCustomerId(String asisCustomerId) { this.asisCustomerId = asisCustomerId; }

  public String getAsisCustomerName() { return this.asisCustomerName; }
  public void setAsisCustomerName(String asisCustomerName) { this.asisCustomerName = asisCustomerName; }

  public String getAsisComments() { return this.asisComments; }
  public void setAsisComments(String asisComments) { this.asisComments = asisComments; }

  public String getTxnCategory() { return this.txnCategory; }
  public void setTxnCategory(String txnCategory) { this.txnCategory = txnCategory; }

  public Date getEntryDate() { return this.entryDate; }
  public void setEntryDate(Date entryDate) { this.entryDate = entryDate; }

  public Date getTrBeginDate() { return this.trBeginDate; }
  public void setTrBeginDate(Date trBeginDate) { this.trBeginDate = trBeginDate; }

  public Date getTrEndDate() { return this.trEndDate; }
  public void setTrEndDate(Date trEndDate) { this.trEndDate = trEndDate; }

  public String getTrainingFlag() { return this.trainingFlag; }
  public void setTrainingFlag(String trainingFlag) { this.trainingFlag = trainingFlag; }

  public Date getPostDate() { return this.postDate; }
  public void setPostDate(Date postDate) { this.postDate = postDate; }

  public Date getSubmitDate() { return this.submitDate; }
  public void setSubmitDate(Date submitDate) { this.submitDate = submitDate; }

  public Date getCreateDate() { return this.createDate; }
  public void setCreateDate(Date createDate) { this.createDate = createDate; }

  public Date getProcessDate() { return this.processDate; }
  public void setProcessDate(Date processDate) { this.processDate = processDate; }

  public String getVoidFlag() { return this.voidFlag; }
  public void setVoidFlag(String voidFlag) { this.voidFlag = voidFlag; }

  public String getCashier() { return this.cashier; }
  public void setCashier(String cashier) { this.cashier = cashier; }

  public String getRegisterId() { return this.registerId; }
  public void setRegisterId(String registerId) { this.registerId = registerId; }

  public String getStoreId() { return this.storeId; }
  public void setStoreId(String storeId) { this.storeId = storeId; }

  public String getCompanyId() { return this.companyId; }
  public void setCompanyId(String companyId) { this.companyId = companyId; }

  public String getEmployeeId() { return this.employeeId; }
  public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

  public String getCustomerId() { return this.customerId; }
  public void setCustomerId(String customerId) { this.customerId = customerId; }

  public String getConsultantId() { return this.consultantId; }
  public void setConsultantId(String consultantId) { this.consultantId = consultantId; }

  public String getTaxExemptId() { return this.taxExemptId; }
  public void setTaxExemptId(String taxExemptId) { this.taxExemptId = taxExemptId; }

  public String getTaxJurisdiction() { return this.taxJurisdiction; }
  public void setTaxJurisdiction(String taxJurisdiction) { this.taxJurisdiction = taxJurisdiction; }

  public String getCloseoutFlag() { return this.closeoutFlag; }
  public void setCloseoutFlag(String closeoutFlag) { this.closeoutFlag = closeoutFlag; }

  public String getCurrencyCd() { return this.currencyCd; }
  public void setCurrencyCd(String currencyCd) { this.currencyCd = currencyCd; }

  public Double getReductionAmount() { return this.reductionAmount; }
  public void setReductionAmount(Double reductionAmount) { this.reductionAmount = reductionAmount; }
  public void setReductionAmount(double reductionAmount) { this.reductionAmount = new Double(reductionAmount); }

  public Double getNetAmount() { return this.netAmount; }
  public void setNetAmount(Double netAmount) { this.netAmount = netAmount; }
  public void setNetAmount(double netAmount) { this.netAmount = new Double(netAmount); }

  public String getStatus() { return this.status; }
  public void setStatus(String status) { this.status = status; }

  public Date getDataPopulationDt() { return this.dataPopulationDt; }
  public void setDataPopulationDt(Date dataPopulationDt) { this.dataPopulationDt = dataPopulationDt; }

  public Date getExtractedDt() { return this.extractedDt; }
  public void setExtractedDt(Date extractedDt) { this.extractedDt = extractedDt; }

  public String getAsisOrderNo() { return this.asisOrderNo; }
  public void setAsisOrderNo(String asisOrderNo) { this.asisOrderNo = asisOrderNo; }

  public Date getAsisOrderDate() { return this.asisOrderDate; }
  public void setAsisOrderDate(Date asisOrderDate) { this.asisOrderDate = asisOrderDate; }

  public String getAsisSupplierNo() { return this.asisSupplierNo; }
  public void setAsisSupplierNo(String asisSupplierNo) { this.asisSupplierNo = asisSupplierNo; }

  public Date getAsisSupplierDate() { return this.asisSupplierDate; }
  public void setAsisSupplierDate(Date asisSupplierDate) { this.asisSupplierDate = asisSupplierDate; }

  public String getAsisNotes() { return this.asisNotes; }
  public void setAsisNotes(String asisNotes) { this.asisNotes = asisNotes; }
  
  //Added by Rachana for approval of return transaction
  public String getApproverId() { return this.approverId; }
  public void setApproverId(String approverId) { this.approverId = approverId; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
	ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmStgTxnHdrOracleBean bean = new ArmStgTxnHdrOracleBean();
      bean.transactionId = getStringFromResultSet(rs, "TRANSACTION_ID");
      bean.transactionType = getStringFromResultSet(rs, "TRANSACTION_TYPE");
      bean.payTxnType = getStringFromResultSet(rs, "PAY_TXN_TYPE");
      bean.transactionNum = getLongFromResultSet(rs, "TRANSACTION_NUM");
      bean.addNo = getStringFromResultSet(rs, "ADD_NO");
      bean.fiscalNo = getStringFromResultSet(rs, "FISCAL_NO");
      bean.fiscalReceiptDate = getDateFromResultSet(rs, "FISCAL_RECEIPT_DATE");
      bean.asisCompanyCd = getStringFromResultSet(rs, "ASIS_COMPANY_CD");
      bean.asisStoreId = getStringFromResultSet(rs, "ASIS_STORE_ID");
      bean.asisRegisterId = getStringFromResultSet(rs, "ASIS_REGISTER_ID");
      bean.asisTxnNum = getStringFromResultSet(rs, "ASIS_TXN_NUM");
      bean.asisTxnDate = getDateFromResultSet(rs, "ASIS_TXN_DATE");
      bean.asisFiscalReceiptNo = getStringFromResultSet(rs, "ASIS_FISCAL_RECEIPT_NO");
      bean.asisFiscalDate = getDateFromResultSet(rs, "ASIS_FISCAL_DATE");
      bean.asisFiscalDocNum = getStringFromResultSet(rs, "ASIS_FISCAL_DOC_NUM");
      bean.asisFiscalDocDate = getDateFromResultSet(rs, "ASIS_FISCAL_DOC_DATE");
      bean.asisFiscalDocType = getStringFromResultSet(rs, "ASIS_FISCAL_DOC_TYPE");
      bean.asisCustomerId = getStringFromResultSet(rs, "ASIS_CUSTOMER_ID");
      bean.asisCustomerName = getStringFromResultSet(rs, "ASIS_CUSTOMER_NAME");
      bean.asisComments = getStringFromResultSet(rs, "ASIS_COMMENTS");
      bean.txnCategory = getStringFromResultSet(rs, "TXN_CATEGORY");
      bean.entryDate = getDateFromResultSet(rs, "ENTRY_DATE");
      bean.trBeginDate = getDateFromResultSet(rs, "TR_BEGIN_DATE");
      bean.trEndDate = getDateFromResultSet(rs, "TR_END_DATE");
      bean.trainingFlag = getStringFromResultSet(rs, "TRAINING_FLAG");
      bean.postDate = getDateFromResultSet(rs, "POST_DATE");
      bean.submitDate = getDateFromResultSet(rs, "SUBMIT_DATE");
      bean.createDate = getDateFromResultSet(rs, "CREATE_DATE");
      bean.processDate = getDateFromResultSet(rs, "PROCESS_DATE");
      bean.voidFlag = getStringFromResultSet(rs, "VOID_FLAG");
      bean.cashier = getStringFromResultSet(rs, "CASHIER");
      bean.registerId = getStringFromResultSet(rs, "REGISTER_ID");
      bean.storeId = getStringFromResultSet(rs, "STORE_ID");
      bean.companyId = getStringFromResultSet(rs, "COMPANY_ID");
      bean.employeeId = getStringFromResultSet(rs, "EMPLOYEE_ID");
      bean.customerId = getStringFromResultSet(rs, "CUSTOMER_ID");
      bean.consultantId = getStringFromResultSet(rs, "CONSULTANT_ID");
      bean.taxExemptId = getStringFromResultSet(rs, "TAX_EXEMPT_ID");
      bean.taxJurisdiction = getStringFromResultSet(rs, "TAX_JURISDICTION");
      bean.closeoutFlag = getStringFromResultSet(rs, "CLOSEOUT_FLAG");
      bean.currencyCd = getStringFromResultSet(rs, "CURRENCY_CD");
      bean.reductionAmount = getDoubleFromResultSet(rs, "REDUCTION_AMOUNT");
      bean.netAmount = getDoubleFromResultSet(rs, "NET_AMOUNT");
      bean.status = getStringFromResultSet(rs, "STATUS");
      bean.dataPopulationDt = getDateFromResultSet(rs, "DATA_POPULATION_DT");
      bean.extractedDt = getDateFromResultSet(rs, "EXTRACTED_DT");
      bean.asisOrderNo = getStringFromResultSet(rs, "ASIS_ORDER_NO");
      bean.asisOrderDate = getDateFromResultSet(rs, "ASIS_ORDER_DATE");
      bean.asisSupplierNo = getStringFromResultSet(rs, "ASIS_SUPPLIER_NO");
      bean.asisSupplierDate = getDateFromResultSet(rs, "ASIS_SUPPLIER_DATE");
      bean.asisNotes = getStringFromResultSet(rs, "ASIS_NOTES");
      //Added by Rachana for approval of return transaction
      bean.approverId = getStringFromResultSet(rs, "APPROVER_ID");
      list.add(bean);
    }
    return (ArmStgTxnHdrOracleBean[]) list.toArray(new ArmStgTxnHdrOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTransactionId(), Types.VARCHAR);
    addToList(list, this.getTransactionType(), Types.VARCHAR);
    addToList(list, this.getPayTxnType(), Types.VARCHAR);
    addToList(list, this.getTransactionNum(), Types.DECIMAL);
    addToList(list, this.getAddNo(), Types.VARCHAR);
    addToList(list, this.getFiscalNo(), Types.VARCHAR);
    addToList(list, this.getFiscalReceiptDate(), Types.TIMESTAMP);
    addToList(list, this.getAsisCompanyCd(), Types.VARCHAR);
    addToList(list, this.getAsisStoreId(), Types.VARCHAR);
    addToList(list, this.getAsisRegisterId(), Types.VARCHAR);
    addToList(list, this.getAsisTxnNum(), Types.VARCHAR);
    addToList(list, this.getAsisTxnDate(), Types.TIMESTAMP);
    addToList(list, this.getAsisFiscalReceiptNo(), Types.VARCHAR);
    addToList(list, this.getAsisFiscalDate(), Types.TIMESTAMP);
    addToList(list, this.getAsisFiscalDocNum(), Types.VARCHAR);
    addToList(list, this.getAsisFiscalDocDate(), Types.TIMESTAMP);
    addToList(list, this.getAsisFiscalDocType(), Types.VARCHAR);
    addToList(list, this.getAsisCustomerId(), Types.VARCHAR);
    addToList(list, this.getAsisCustomerName(), Types.VARCHAR);
    addToList(list, this.getAsisComments(), Types.VARCHAR);
    addToList(list, this.getTxnCategory(), Types.VARCHAR);
    addToList(list, this.getEntryDate(), Types.TIMESTAMP);
    addToList(list, this.getTrBeginDate(), Types.TIMESTAMP);
    addToList(list, this.getTrEndDate(), Types.TIMESTAMP);
    addToList(list, this.getTrainingFlag(), Types.VARCHAR);
    addToList(list, this.getPostDate(), Types.TIMESTAMP);
    addToList(list, this.getSubmitDate(), Types.TIMESTAMP);
    addToList(list, this.getCreateDate(), Types.TIMESTAMP);
    addToList(list, this.getProcessDate(), Types.TIMESTAMP);
    addToList(list, this.getVoidFlag(), Types.VARCHAR);
    addToList(list, this.getCashier(), Types.VARCHAR);
    addToList(list, this.getRegisterId(), Types.VARCHAR);
    addToList(list, this.getStoreId(), Types.VARCHAR);
    addToList(list, this.getCompanyId(), Types.VARCHAR);
    addToList(list, this.getEmployeeId(), Types.VARCHAR);
    addToList(list, this.getCustomerId(), Types.VARCHAR);
    addToList(list, this.getConsultantId(), Types.VARCHAR);
    addToList(list, this.getTaxExemptId(), Types.VARCHAR);
    addToList(list, this.getTaxJurisdiction(), Types.VARCHAR);
    addToList(list, this.getCloseoutFlag(), Types.VARCHAR);
    addToList(list, this.getCurrencyCd(), Types.VARCHAR);
    addToList(list, this.getReductionAmount(), Types.DECIMAL);
    addToList(list, this.getNetAmount(), Types.DECIMAL);
    addToList(list, this.getStatus(), Types.VARCHAR);
    addToList(list, this.getDataPopulationDt(), Types.TIMESTAMP);
    addToList(list, this.getExtractedDt(), Types.TIMESTAMP);
    addToList(list, this.getAsisOrderNo(), Types.VARCHAR);
    addToList(list, this.getAsisOrderDate(), Types.TIMESTAMP);
    addToList(list, this.getAsisSupplierNo(), Types.VARCHAR);
    addToList(list, this.getAsisSupplierDate(), Types.TIMESTAMP);
    addToList(list, this.getAsisNotes(), Types.VARCHAR);
    //Added by Rachana for approval of return transaction
    addToList(list, this.getApproverId(), Types.VARCHAR);
    return list;
  }

}
