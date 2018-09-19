/*package com.chelseasystems.cs.dataaccess.artsoracle.databean;

public class ArmEmpSaleSummaryOracleBean {

}
*/
//
// Copyright 2002, Retek Inc. All Rights Reserved.
// Created by Vivek on 21 Nov 08
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
 * This class is an object representation of the Arts database table ARM_EMP_SALE_SUMMARY<BR>
 * Followings are the column of the table: <BR>
 *      ID_CT  VARCHAR2(128),	<BR>
 *		ID_STR_RT VARCHAR2(128),<BR>
 *		TXN_TYPE VARCHAR2(20),	<BR>
 *		TXN_DATE DATE,			<BR>
 *		NET_AMOUNT VARCHAR2(75),<BR>
 *		DSC_PERCENT NUMBER(7,4) <BR>
 *
 */
public class ArmEmpSaleSummaryOracleBean extends BaseOracleBean {

  public ArmEmpSaleSummaryOracleBean() {}

  public static String selectSql = "select ID_CT, ID_STR_RT, TXN_TYPE, TXN_DATE, NET_AMOUNT ,DSC_PERCENT from ARM_EMP_SALE_SUMMARY ";
  public static String insertSql = "insert into ARM_EMP_SALE_SUMMARY (ID_CT, ID_STR_RT, TXN_TYPE, TXN_DATE, NET_AMOUNT ,DSC_PERCENT) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_EMP_SALE_SUMMARY set ID_CT = ?, ID_STR_RT = ?, TXN_TYPE = ?, TXN_DATE = ?,  NET_AMOUNT = ?,DSC_PERCENT = ?";
  public static String deleteSql = "delete from ARM_EMP_SALE_SUMMARY ";

  public static String TABLE_NAME = "ARM_EMP_SALE_SUMMARY";
  public static String COL_ID_CT = "ARM_EMP_SALE_SUMMARY.ID_CT";
  public static String COL_ID_STR_RT = "ARM_EMP_SALE_SUMMARY.ID_STR_RT";
  public static String COL_TXN_TYPE = "ARM_EMP_SALE_SUMMARY.TXN_TYPE";
  public static String COL_TXN_DATE = "ARM_EMP_SALE_SUMMARY.TXN_DATE";
  public static String COL_NET_AMOUNT = "ARM_EMP_SALE_SUMMARY.NET_AMOUNT";
  public static String COL_DSC_PERCENT = "ARM_EMP_SALE_SUMMARY.DSC_PERCENT";
  

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String idStrRt;
  private String txnType;
  private Date txnDate;
  private ArmCurrency netAmount;
  private double disPercent;

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getTxnType() { return this.txnType; }
  public void setTxnType(String txnType) { this.txnType = txnType; }

  public Date getTxnDate() { return this.txnDate; }
  public void setTxnDate(Date txnDate) { this.txnDate = txnDate; }

  public ArmCurrency getNetAmount() {	return this.netAmount; }
  public void setNetAmount(ArmCurrency netAmount) {	this.netAmount = netAmount;	}
  
  public double getDisPercent() {	return this.disPercent; }
  public void setDisPercent(double disPercent) {	this.disPercent = disPercent;}

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmEmpSaleSummaryOracleBean bean = new ArmEmpSaleSummaryOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.txnType = getStringFromResultSet(rs, "TXN_TYPE");
      bean.txnDate = getDateFromResultSet(rs, "TXN_DATE");
      bean.netAmount = getCurrencyFromResultSet(rs,"NET_AMOUNT");
      bean.disPercent = getDoubleFromResultSet(rs,"DSC_PERCENT").doubleValue();
      list.add(bean);
    }
    return (ArmEmpSaleSummaryOracleBean[]) list.toArray(new ArmEmpSaleSummaryOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getTxnType(), Types.VARCHAR);
    addToList(list, this.getTxnDate(), Types.TIMESTAMP);
    addToList(list, this.getNetAmount(),Types.VARCHAR);
    addToList(list, new Double(this.getDisPercent()), Types.DOUBLE);
    return list;
  }

}
