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
 * This class is an object representation of the Arts database table ARM_ASIS_TXN_DATA<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN -- VARCHAR2(128)<BR>
 *     COMPANY_CODE -- VARCHAR2(20)<BR>
 *     STORE_ID -- VARCHAR2(20)<BR>
 *     REGISTER_ID -- VARCHAR2(20)<BR>
 *     TXN_NO -- VARCHAR2(50)<BR>
 *     TXN_DATE -- DATE(7)<BR>
 *     FISCAL_RECEIPT_NO -- VARCHAR2(50)<BR>
 *     FISCAL_RECEIPT_DATE -- DATE(7)<BR>
 *     FISCAL_DOC_NO -- VARCHAR2(20)<BR>
 *     FISCAL_DOC_DATE -- DATE(7)<BR>
 *     FISCAL_DOC_TYPE -- VARCHAR2(20)<BR>
 *     CUSTOMER_NO -- VARCHAR2(50)<BR>
 *     CUSTOMER_NAME -- VARCHAR2(50)<BR>
 *     COMMENTS -- VARCHAR2(200)<BR>
 *     TXN_AMOUNT -- VARCHAR2(75)<BR>
 *     ORDER_NO -- VARCHAR2(50)<BR>
 *     ORDER_DATE -- DATE(7)<BR>
 *     SUPPLIER_NO -- VARCHAR2(50)<BR>
 *     SUPPLIER_DATE -- DATE(7)<BR>
 *     NOTES -- VARCHAR2(200)<BR>
 *
 */
public class ArmAsisTxnDataOracleBean extends BaseOracleBean {

  public ArmAsisTxnDataOracleBean() {}

  public static String selectSql = "select AI_TRN, COMPANY_CODE, STORE_ID, REGISTER_ID, TXN_NO, TXN_DATE, FISCAL_RECEIPT_NO, FISCAL_RECEIPT_DATE, FISCAL_DOC_NO, FISCAL_DOC_DATE, FISCAL_DOC_TYPE, CUSTOMER_NO, CUSTOMER_NAME, COMMENTS, TXN_AMOUNT, ORDER_NO, ORDER_DATE, SUPPLIER_NO, SUPPLIER_DATE, NOTES from ARM_ASIS_TXN_DATA ";
  public static String insertSql = "insert into ARM_ASIS_TXN_DATA (AI_TRN, COMPANY_CODE, STORE_ID, REGISTER_ID, TXN_NO, TXN_DATE, FISCAL_RECEIPT_NO, FISCAL_RECEIPT_DATE, FISCAL_DOC_NO, FISCAL_DOC_DATE, FISCAL_DOC_TYPE, CUSTOMER_NO, CUSTOMER_NAME, COMMENTS, TXN_AMOUNT, ORDER_NO, ORDER_DATE, SUPPLIER_NO, SUPPLIER_DATE, NOTES) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_ASIS_TXN_DATA set AI_TRN = ?, COMPANY_CODE = ?, STORE_ID = ?, REGISTER_ID = ?, TXN_NO = ?, TXN_DATE = ?, FISCAL_RECEIPT_NO = ?, FISCAL_RECEIPT_DATE = ?, FISCAL_DOC_NO = ?, FISCAL_DOC_DATE = ?, FISCAL_DOC_TYPE = ?, CUSTOMER_NO = ?, CUSTOMER_NAME = ?, COMMENTS = ?, TXN_AMOUNT = ?, ORDER_NO = ?, ORDER_DATE = ?, SUPPLIER_NO = ?, SUPPLIER_DATE = ?, NOTES = ? ";
  public static String deleteSql = "delete from ARM_ASIS_TXN_DATA ";

  public static String TABLE_NAME = "ARM_ASIS_TXN_DATA";
  public static String COL_AI_TRN = "ARM_ASIS_TXN_DATA.AI_TRN";
  public static String COL_COMPANY_CODE = "ARM_ASIS_TXN_DATA.COMPANY_CODE";
  public static String COL_STORE_ID = "ARM_ASIS_TXN_DATA.STORE_ID";
  public static String COL_REGISTER_ID = "ARM_ASIS_TXN_DATA.REGISTER_ID";
  public static String COL_TXN_NO = "ARM_ASIS_TXN_DATA.TXN_NO";
  public static String COL_TXN_DATE = "ARM_ASIS_TXN_DATA.TXN_DATE";
  public static String COL_FISCAL_RECEIPT_NO = "ARM_ASIS_TXN_DATA.FISCAL_RECEIPT_NO";
  public static String COL_FISCAL_RECEIPT_DATE = "ARM_ASIS_TXN_DATA.FISCAL_RECEIPT_DATE";
  public static String COL_FISCAL_DOC_NO = "ARM_ASIS_TXN_DATA.FISCAL_DOC_NO";
  public static String COL_FISCAL_DOC_DATE = "ARM_ASIS_TXN_DATA.FISCAL_DOC_DATE";
  public static String COL_FISCAL_DOC_TYPE = "ARM_ASIS_TXN_DATA.FISCAL_DOC_TYPE";
  public static String COL_CUSTOMER_NO = "ARM_ASIS_TXN_DATA.CUSTOMER_NO";
  public static String COL_CUSTOMER_NAME = "ARM_ASIS_TXN_DATA.CUSTOMER_NAME";
  public static String COL_COMMENTS = "ARM_ASIS_TXN_DATA.COMMENTS";
  public static String COL_TXN_AMOUNT = "ARM_ASIS_TXN_DATA.TXN_AMOUNT";
  public static String COL_ORDER_NO = "ARM_ASIS_TXN_DATA.ORDER_NO";
  public static String COL_ORDER_DATE = "ARM_ASIS_TXN_DATA.ORDER_DATE";
  public static String COL_SUPPLIER_NO = "ARM_ASIS_TXN_DATA.SUPPLIER_NO";
  public static String COL_SUPPLIER_DATE = "ARM_ASIS_TXN_DATA.SUPPLIER_DATE";
  public static String COL_NOTES = "ARM_ASIS_TXN_DATA.NOTES";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private String companyCode;
  private String storeId;
  private String registerId;
  private String txnNo;
  private Date txnDate;
  private String fiscalReceiptNo;
  private Date fiscalReceiptDate;
  private String fiscalDocNo;
  private Date fiscalDocDate;
  private String fiscalDocType;
  private String customerNo;
  private String customerName;
  private String comments;
  private ArmCurrency txnAmount;
  private String orderNo;
  private Date orderDate;
  private String supplierNo;
  private Date supplierDate;
  private String notes;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getCompanyCode() { return this.companyCode; }
  public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

  public String getStoreId() { return this.storeId; }
  public void setStoreId(String storeId) { this.storeId = storeId; }

  public String getRegisterId() { return this.registerId; }
  public void setRegisterId(String registerId) { this.registerId = registerId; }

  public String getTxnNo() { return this.txnNo; }
  public void setTxnNo(String txnNo) { this.txnNo = txnNo; }

  public Date getTxnDate() { return this.txnDate; }
  public void setTxnDate(Date txnDate) { this.txnDate = txnDate; }

  public String getFiscalReceiptNo() { return this.fiscalReceiptNo; }
  public void setFiscalReceiptNo(String fiscalReceiptNo) { this.fiscalReceiptNo = fiscalReceiptNo; }

  public Date getFiscalReceiptDate() { return this.fiscalReceiptDate; }
  public void setFiscalReceiptDate(Date fiscalReceiptDate) { this.fiscalReceiptDate = fiscalReceiptDate; }

  public String getFiscalDocNo() { return this.fiscalDocNo; }
  public void setFiscalDocNo(String fiscalDocNo) { this.fiscalDocNo = fiscalDocNo; }

  public Date getFiscalDocDate() { return this.fiscalDocDate; }
  public void setFiscalDocDate(Date fiscalDocDate) { this.fiscalDocDate = fiscalDocDate; }

  public String getFiscalDocType() { return this.fiscalDocType; }
  public void setFiscalDocType(String fiscalDocType) { this.fiscalDocType = fiscalDocType; }

  public String getCustomerNo() { return this.customerNo; }
  public void setCustomerNo(String customerNo) { this.customerNo = customerNo; }

  public String getCustomerName() { return this.customerName; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }

  public String getComments() { return this.comments; }
  public void setComments(String comments) { this.comments = comments; }

  public ArmCurrency getTxnAmount() { return this.txnAmount; }
  public void setTxnAmount(ArmCurrency txnAmount) { this.txnAmount = txnAmount; }

  public String getOrderNo() { return this.orderNo; }
  public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

  public Date getOrderDate() { return this.orderDate; }
  public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

  public String getSupplierNo() { return this.supplierNo; }
  public void setSupplierNo(String supplierNo) { this.supplierNo = supplierNo; }

  public Date getSupplierDate() { return this.supplierDate; }
  public void setSupplierDate(Date supplierDate) { this.supplierDate = supplierDate; }

  public String getNotes() { return this.notes; }
  public void setNotes(String notes) { this.notes = notes; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmAsisTxnDataOracleBean bean = new ArmAsisTxnDataOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.companyCode = getStringFromResultSet(rs, "COMPANY_CODE");
      bean.storeId = getStringFromResultSet(rs, "STORE_ID");
      bean.registerId = getStringFromResultSet(rs, "REGISTER_ID");
      bean.txnNo = getStringFromResultSet(rs, "TXN_NO");
      bean.txnDate = getDateFromResultSet(rs, "TXN_DATE");
      bean.fiscalReceiptNo = getStringFromResultSet(rs, "FISCAL_RECEIPT_NO");
      bean.fiscalReceiptDate = getDateFromResultSet(rs, "FISCAL_RECEIPT_DATE");
      bean.fiscalDocNo = getStringFromResultSet(rs, "FISCAL_DOC_NO");
      bean.fiscalDocDate = getDateFromResultSet(rs, "FISCAL_DOC_DATE");
      bean.fiscalDocType = getStringFromResultSet(rs, "FISCAL_DOC_TYPE");
      bean.customerNo = getStringFromResultSet(rs, "CUSTOMER_NO");
      bean.customerName = getStringFromResultSet(rs, "CUSTOMER_NAME");
      bean.comments = getStringFromResultSet(rs, "COMMENTS");
      bean.txnAmount = getCurrencyFromResultSet(rs, "TXN_AMOUNT");
      bean.orderNo = getStringFromResultSet(rs, "ORDER_NO");
      bean.orderDate = getDateFromResultSet(rs, "ORDER_DATE");
      bean.supplierNo = getStringFromResultSet(rs, "SUPPLIER_NO");
      bean.supplierDate = getDateFromResultSet(rs, "SUPPLIER_DATE");
      bean.notes = getStringFromResultSet(rs, "NOTES");
      list.add(bean);
    }
    return (ArmAsisTxnDataOracleBean[]) list.toArray(new ArmAsisTxnDataOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getCompanyCode(), Types.VARCHAR);
    addToList(list, this.getStoreId(), Types.VARCHAR);
    addToList(list, this.getRegisterId(), Types.VARCHAR);
    addToList(list, this.getTxnNo(), Types.VARCHAR);
    addToList(list, this.getTxnDate(), Types.TIMESTAMP);
    addToList(list, this.getFiscalReceiptNo(), Types.VARCHAR);
    addToList(list, this.getFiscalReceiptDate(), Types.TIMESTAMP);
    addToList(list, this.getFiscalDocNo(), Types.VARCHAR);
    addToList(list, this.getFiscalDocDate(), Types.TIMESTAMP);
    addToList(list, this.getFiscalDocType(), Types.VARCHAR);
    addToList(list, this.getCustomerNo(), Types.VARCHAR);
    addToList(list, this.getCustomerName(), Types.VARCHAR);
    addToList(list, this.getComments(), Types.VARCHAR);
    addToList(list, this.getTxnAmount(), Types.VARCHAR);
    addToList(list, this.getOrderNo(), Types.VARCHAR);
    addToList(list, this.getOrderDate(), Types.TIMESTAMP);
    addToList(list, this.getSupplierNo(), Types.VARCHAR);
    addToList(list, this.getSupplierDate(), Types.TIMESTAMP);
    addToList(list, this.getNotes(), Types.VARCHAR);
    return list;
  }

}
