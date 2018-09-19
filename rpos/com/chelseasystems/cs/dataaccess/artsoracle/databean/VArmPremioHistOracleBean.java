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
 * This class is an object representation of the Arts database table V_ARM_PREMIO_HIST<BR>
 * Followings are the column of the table: <BR>
 *     STORE_NAME -- VARCHAR2(50)<BR>
 *     TXN_NUM -- VARCHAR2(128)<BR>
 *     TXN_DATE -- DATE(7)<BR>
 *     REDEEMED_POINTS -- VARCHAR2(128)<BR>
 *     TXN_AMT -- VARCHAR2(75)<BR>
 *     LOYALTY_NUM -- VARCHAR2(15)<BR>
 *
 */
public class VArmPremioHistOracleBean extends BaseOracleBean {

  public VArmPremioHistOracleBean() {}

  public static String selectSql = "select STORE_NAME, TXN_NUM, TXN_DATE, REDEEMED_POINTS, TXN_AMT, LOYALTY_NUM from V_ARM_PREMIO_HIST ";
  public static String insertSql = "insert into V_ARM_PREMIO_HIST (STORE_NAME, TXN_NUM, TXN_DATE, REDEEMED_POINTS, TXN_AMT, LOYALTY_NUM) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update V_ARM_PREMIO_HIST set STORE_NAME = ?, TXN_NUM = ?, TXN_DATE = ?, REDEEMED_POINTS = ?, TXN_AMT = ?, LOYALTY_NUM = ? ";
  public static String deleteSql = "delete from V_ARM_PREMIO_HIST ";

  public static String TABLE_NAME = "V_ARM_PREMIO_HIST";
  public static String COL_STORE_NAME = "V_ARM_PREMIO_HIST.STORE_NAME";
  public static String COL_TXN_NUM = "V_ARM_PREMIO_HIST.TXN_NUM";
  public static String COL_TXN_DATE = "V_ARM_PREMIO_HIST.TXN_DATE";
  public static String COL_REDEEMED_POINTS = "V_ARM_PREMIO_HIST.REDEEMED_POINTS";
  public static String COL_TXN_AMT = "V_ARM_PREMIO_HIST.TXN_AMT";
  public static String COL_LOYALTY_NUM = "V_ARM_PREMIO_HIST.LOYALTY_NUM";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String storeName;
  private String txnNum;
  private Date txnDate;
  private String redeemedPoints;
  private ArmCurrency txnAmt;
  private String loyaltyNum;

  public String getStoreName() { return this.storeName; }
  public void setStoreName(String storeName) { this.storeName = storeName; }

  public String getTxnNum() { return this.txnNum; }
  public void setTxnNum(String txnNum) { this.txnNum = txnNum; }

  public Date getTxnDate() { return this.txnDate; }
  public void setTxnDate(Date txnDate) { this.txnDate = txnDate; }

  public String getRedeemedPoints() { return this.redeemedPoints; }
  public void setRedeemedPoints(String redeemedPoints) { this.redeemedPoints = redeemedPoints; }

  public ArmCurrency getTxnAmt() { return this.txnAmt; }
  public void setTxnAmt(ArmCurrency txnAmt) { this.txnAmt = txnAmt; }

  public String getLoyaltyNum() { return this.loyaltyNum; }
  public void setLoyaltyNum(String loyaltyNum) { this.loyaltyNum = loyaltyNum; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      VArmPremioHistOracleBean bean = new VArmPremioHistOracleBean();
      bean.storeName = getStringFromResultSet(rs, "STORE_NAME");
      bean.txnNum = getStringFromResultSet(rs, "TXN_NUM");
      bean.txnDate = getDateFromResultSet(rs, "TXN_DATE");
      bean.redeemedPoints = getStringFromResultSet(rs, "REDEEMED_POINTS");
      bean.txnAmt = getCurrencyFromResultSet(rs, "TXN_AMT");
      bean.loyaltyNum = getStringFromResultSet(rs, "LOYALTY_NUM");
      list.add(bean);
    }
    return (VArmPremioHistOracleBean[]) list.toArray(new VArmPremioHistOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getStoreName(), Types.VARCHAR);
    addToList(list, this.getTxnNum(), Types.VARCHAR);
    addToList(list, this.getTxnDate(), Types.TIMESTAMP);
    addToList(list, this.getRedeemedPoints(), Types.VARCHAR);
    addToList(list, this.getTxnAmt(), Types.VARCHAR);
    addToList(list, this.getLoyaltyNum(), Types.VARCHAR);
    return list;
  }

}
