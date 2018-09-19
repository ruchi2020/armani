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
 * This class is an object representation of the Arts database table RK_COLLECTION<BR>
 * Followings are the column of the table: <BR>
 *     TYPE -- VARCHAR2(100)<BR>
 *     AMOUNT -- VARCHAR2(75)<BR>
 *     COMMENTS -- VARCHAR2(200)<BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     REDEEMABLE_CONTROL_ID -- VARCHAR2(50)<BR>
 *
 */
public class RkCollectionOracleBean extends BaseOracleBean {

  public RkCollectionOracleBean() {}

  public static String selectSql = "select TYPE, AMOUNT, COMMENTS, AI_TRN, REDEEMABLE_CONTROL_ID from RK_COLLECTION ";
  public static String insertSql = "insert into RK_COLLECTION (TYPE, AMOUNT, COMMENTS, AI_TRN, REDEEMABLE_CONTROL_ID) values (?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_COLLECTION set TYPE = ?, AMOUNT = ?, COMMENTS = ?, AI_TRN = ?, REDEEMABLE_CONTROL_ID = ? ";
  public static String deleteSql = "delete from RK_COLLECTION ";

  public static String TABLE_NAME = "RK_COLLECTION";
  public static String COL_TYPE = "RK_COLLECTION.TYPE";
  public static String COL_AMOUNT = "RK_COLLECTION.AMOUNT";
  public static String COL_COMMENTS = "RK_COLLECTION.COMMENTS";
  public static String COL_AI_TRN = "RK_COLLECTION.AI_TRN";
  public static String COL_REDEEMABLE_CONTROL_ID = "RK_COLLECTION.REDEEMABLE_CONTROL_ID";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String type;
  private ArmCurrency amount;
  private String comments;
  private String aiTrn;
  private String redeemableControlId;

  public String getType() { return this.type; }
  public void setType(String type) { this.type = type; }

  public ArmCurrency getAmount() { return this.amount; }
  public void setAmount(ArmCurrency amount) { this.amount = amount; }

  public String getComments() { return this.comments; }
  public void setComments(String comments) { this.comments = comments; }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getRedeemableControlId() { return this.redeemableControlId; }
  public void setRedeemableControlId(String redeemableControlId) { this.redeemableControlId = redeemableControlId; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkCollectionOracleBean bean = new RkCollectionOracleBean();
      bean.type = getStringFromResultSet(rs, "TYPE");
      bean.amount = getCurrencyFromResultSet(rs, "AMOUNT");
      bean.comments = getStringFromResultSet(rs, "COMMENTS");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.redeemableControlId = getStringFromResultSet(rs, "REDEEMABLE_CONTROL_ID");
      list.add(bean);
    }
    return (RkCollectionOracleBean[]) list.toArray(new RkCollectionOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getType(), Types.VARCHAR);
    addToList(list, this.getAmount(), Types.VARCHAR);
    addToList(list, this.getComments(), Types.VARCHAR);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getRedeemableControlId(), Types.VARCHAR);
    return list;
  }

}
