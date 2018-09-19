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
 * This class is an object representation of the Arts database table ARM_ALTERN_CODE<BR>
 * Followings are the column of the table: <BR>
 *     GROUP_ID(PK) -- VARCHAR2(30)<BR>
 *     ALTERN_CODE(PK) -- VARCHAR2(50)<BR>
 *     ALTERN_DESC -- VARCHAR2(50)<BR>
 *     ALTERN_TIME -- DATE(7)<BR>
 *     ALTERN_PRICE -- VARCHAR2(20)<BR>
 *
 */
public class ArmAlternCodeOracleBean extends BaseOracleBean {

  public ArmAlternCodeOracleBean() {}

  public static String selectSql = "select GROUP_ID, ALTERN_CODE, ALTERN_DESC, ALTERN_TIME, ALTERN_PRICE from ARM_ALTERN_CODE ";
  public static String insertSql = "insert into ARM_ALTERN_CODE (GROUP_ID, ALTERN_CODE, ALTERN_DESC, ALTERN_TIME, ALTERN_PRICE) values (?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_ALTERN_CODE set GROUP_ID = ?, ALTERN_CODE = ?, ALTERN_DESC = ?, ALTERN_TIME = ?, ALTERN_PRICE = ? ";
  public static String deleteSql = "delete from ARM_ALTERN_CODE ";

  public static String TABLE_NAME = "ARM_ALTERN_CODE";
  public static String COL_GROUP_ID = "ARM_ALTERN_CODE.GROUP_ID";
  public static String COL_ALTERN_CODE = "ARM_ALTERN_CODE.ALTERN_CODE";
  public static String COL_ALTERN_DESC = "ARM_ALTERN_CODE.ALTERN_DESC";
  public static String COL_ALTERN_TIME = "ARM_ALTERN_CODE.ALTERN_TIME";
  public static String COL_ALTERN_PRICE = "ARM_ALTERN_CODE.ALTERN_PRICE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String groupId;
  private String alternCode;
  private String alternDesc;
  private Date alternTime;
  private ArmCurrency alternPrice;

  public String getGroupId() { return this.groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public String getAlternCode() { return this.alternCode; }
  public void setAlternCode(String alternCode) { this.alternCode = alternCode; }

  public String getAlternDesc() { return this.alternDesc; }
  public void setAlternDesc(String alternDesc) { this.alternDesc = alternDesc; }

  public Date getAlternTime() { return this.alternTime; }
  public void setAlternTime(Date alternTime) { this.alternTime = alternTime; }

  public ArmCurrency getAlternPrice() { return this.alternPrice; }
  public void setAlternPrice(ArmCurrency alternPrice) { this.alternPrice = alternPrice; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmAlternCodeOracleBean bean = new ArmAlternCodeOracleBean();
      bean.groupId = getStringFromResultSet(rs, "GROUP_ID");
      bean.alternCode = getStringFromResultSet(rs, "ALTERN_CODE");
      bean.alternDesc = getStringFromResultSet(rs, "ALTERN_DESC");
      bean.alternTime = getDateFromResultSet(rs, "ALTERN_TIME");
      bean.alternPrice = getCurrencyFromResultSet(rs, "ALTERN_PRICE");
      list.add(bean);
    }
    return (ArmAlternCodeOracleBean[]) list.toArray(new ArmAlternCodeOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getGroupId(), Types.VARCHAR);
    addToList(list, this.getAlternCode(), Types.VARCHAR);
    addToList(list, this.getAlternDesc(), Types.VARCHAR);
    addToList(list, this.getAlternTime(), Types.TIMESTAMP);
    addToList(list, this.getAlternPrice(), Types.VARCHAR);
    return list;
  }

}
