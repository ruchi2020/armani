//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.chelseasystems.cr.currency.ArmCurrency;

/**
 *
 * This class is an object representation of the Arts database table RK_PAID_OUT<BR>
 * Followings are the column of the table: <BR>
 *     TYPE -- VARCHAR2(100)<BR>
 *     AMOUNT -- VARCHAR2(75)<BR>
 *     COMMENTS -- VARCHAR2(200)<BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *
 */
public class RkPaidOutOracleBean extends BaseOracleBean {

  public RkPaidOutOracleBean() {}

  public static String selectSql = "select TYPE, AMOUNT, COMMENTS, AI_TRN from RK_PAID_OUT ";
  public static String insertSql = "insert into RK_PAID_OUT (TYPE, AMOUNT, COMMENTS, AI_TRN) values (?, ?, ?, ?)";
  public static String updateSql = "update RK_PAID_OUT set TYPE = ?, AMOUNT = ?, COMMENTS = ?, AI_TRN = ? ";
  public static String deleteSql = "delete from RK_PAID_OUT ";

  public static String TABLE_NAME = "RK_PAID_OUT";
  public static String COL_TYPE = "RK_PAID_OUT.TYPE";
  public static String COL_AMOUNT = "RK_PAID_OUT.AMOUNT";
  public static String COL_COMMENTS = "RK_PAID_OUT.COMMENTS";
  public static String COL_AI_TRN = "RK_PAID_OUT.AI_TRN";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String type;
  private ArmCurrency amount;
  private String comments;
  private String aiTrn;

  public String getType() { return this.type; }
  public void setType(String type) { this.type = type; }

  public ArmCurrency getAmount() { return this.amount; }
  public void setAmount(ArmCurrency amount) { this.amount = amount; }

  public String getComments() { return this.comments; }
  public void setComments(String comments) { this.comments = comments; }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkPaidOutOracleBean bean = new RkPaidOutOracleBean();
      bean.type = getStringFromResultSet(rs, "TYPE");
      bean.amount = getCurrencyFromResultSet(rs, "AMOUNT");
      bean.comments = getStringFromResultSet(rs, "COMMENTS");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      list.add(bean);
    }
    return (RkPaidOutOracleBean[]) list.toArray(new RkPaidOutOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getType(), Types.VARCHAR);
    addToList(list, this.getAmount(), Types.VARCHAR);
    addToList(list, this.getComments(), Types.VARCHAR);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    return list;
  }

}
