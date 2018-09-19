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
 * This class is an object representation of the Arts database table ARM_CUST_SALE_SUMMARY<BR>
 * Followings are the column of the table: <BR>
 *     ID_CT -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     TXN_TYPE -- VARCHAR2(20)<BR>
 *     TXN_DATE -- DATE(7)<BR>
 *     TXN_AMOUNT -- VARCHAR2(75)<BR>
 *
 */
public class ArmCustSaleSummaryOracleBean extends BaseOracleBean {

  public ArmCustSaleSummaryOracleBean() {}

  public static String selectSql = "select ID_CT, ID_STR_RT, TXN_TYPE, TXN_DATE, TXN_AMOUNT from ARM_CUST_SALE_SUMMARY ";
  public static String insertSql = "insert into ARM_CUST_SALE_SUMMARY (ID_CT, ID_STR_RT, TXN_TYPE, TXN_DATE, TXN_AMOUNT) values (?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CUST_SALE_SUMMARY set ID_CT = ?, ID_STR_RT = ?, TXN_TYPE = ?, TXN_DATE = ?, TXN_AMOUNT = ? ";
  public static String deleteSql = "delete from ARM_CUST_SALE_SUMMARY ";

  public static String TABLE_NAME = "ARM_CUST_SALE_SUMMARY";
  public static String COL_ID_CT = "ARM_CUST_SALE_SUMMARY.ID_CT";
  public static String COL_ID_STR_RT = "ARM_CUST_SALE_SUMMARY.ID_STR_RT";
  public static String COL_TXN_TYPE = "ARM_CUST_SALE_SUMMARY.TXN_TYPE";
  public static String COL_TXN_DATE = "ARM_CUST_SALE_SUMMARY.TXN_DATE";
  public static String COL_TXN_AMOUNT = "ARM_CUST_SALE_SUMMARY.TXN_AMOUNT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String idStrRt;
  private String txnType;
  private Date txnDate;
  private ArmCurrency txnAmount;

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getTxnType() { return this.txnType; }
  public void setTxnType(String txnType) { this.txnType = txnType; }

  public Date getTxnDate() { return this.txnDate; }
  public void setTxnDate(Date txnDate) { this.txnDate = txnDate; }

  public ArmCurrency getTxnAmount() { return this.txnAmount; }
  public void setTxnAmount(ArmCurrency txnAmount) { this.txnAmount = txnAmount; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCustSaleSummaryOracleBean bean = new ArmCustSaleSummaryOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.txnType = getStringFromResultSet(rs, "TXN_TYPE");
      bean.txnDate = getDateFromResultSet(rs, "TXN_DATE");
      bean.txnAmount = getCurrencyFromResultSet(rs, "TXN_AMOUNT");
      list.add(bean);
    }
    return (ArmCustSaleSummaryOracleBean[]) list.toArray(new ArmCustSaleSummaryOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getTxnType(), Types.VARCHAR);
    addToList(list, this.getTxnDate(), Types.TIMESTAMP);
    addToList(list, this.getTxnAmount(), Types.VARCHAR);
    return list;
  }

}
