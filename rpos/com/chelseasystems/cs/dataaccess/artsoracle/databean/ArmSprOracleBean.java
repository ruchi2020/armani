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
 * This class is an object representation of the Arts database table ARM_SPR<BR>
 * Followings are the column of the table: <BR>
 *     ID_SPR -- VARCHAR2(10)<BR>
 *     NM_SPR -- VARCHAR2(30)<BR>
 *
 */
public class ArmSprOracleBean extends BaseOracleBean {

  public ArmSprOracleBean() {}

  public static String selectSql = "select ID_SPR, NM_SPR from ARM_SPR ";
  public static String insertSql = "insert into ARM_SPR (ID_SPR, NM_SPR) values (?, ?)";
  public static String updateSql = "update ARM_SPR set ID_SPR = ?, NM_SPR = ? ";
  public static String deleteSql = "delete from ARM_SPR ";

  public static String TABLE_NAME = "ARM_SPR";
  public static String COL_ID_SPR = "ARM_SPR.ID_SPR";
  public static String COL_NM_SPR = "ARM_SPR.NM_SPR";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idSpr;
  private String nmSpr;

  public String getIdSpr() { return this.idSpr; }
  public void setIdSpr(String idSpr) { this.idSpr = idSpr; }

  public String getNmSpr() { return this.nmSpr; }
  public void setNmSpr(String nmSpr) { this.nmSpr = nmSpr; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmSprOracleBean bean = new ArmSprOracleBean();
      bean.idSpr = getStringFromResultSet(rs, "ID_SPR");
      bean.nmSpr = getStringFromResultSet(rs, "NM_SPR");
      list.add(bean);
    }
    return (ArmSprOracleBean[]) list.toArray(new ArmSprOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdSpr(), Types.VARCHAR);
    addToList(list, this.getNmSpr(), Types.VARCHAR);
    return list;
  }

}
