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
 * This class is an object representation of the Arts database table ARM_YEAR<BR>
 * Followings are the column of the table: <BR>
 *     ID_YEAR -- VARCHAR2(30)<BR>
 *     NM_YEAR -- VARCHAR2(30)<BR>
 *
 */
public class ArmYearOracleBean extends BaseOracleBean {

  public ArmYearOracleBean() {}

  public static String selectSql = "select ID_YEAR, NM_YEAR from ARM_YEAR ";
  public static String insertSql = "insert into ARM_YEAR (ID_YEAR, NM_YEAR) values (?, ?)";
  public static String updateSql = "update ARM_YEAR set ID_YEAR = ?, NM_YEAR = ? ";
  public static String deleteSql = "delete from ARM_YEAR ";

  public static String TABLE_NAME = "ARM_YEAR";
  public static String COL_ID_YEAR = "ARM_YEAR.ID_YEAR";
  public static String COL_NM_YEAR = "ARM_YEAR.NM_YEAR";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idYear;
  private String nmYear;

  public String getIdYear() { return this.idYear; }
  public void setIdYear(String idYear) { this.idYear = idYear; }

  public String getNmYear() { return this.nmYear; }
  public void setNmYear(String nmYear) { this.nmYear = nmYear; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmYearOracleBean bean = new ArmYearOracleBean();
      bean.idYear = getStringFromResultSet(rs, "ID_YEAR");
      bean.nmYear = getStringFromResultSet(rs, "NM_YEAR");
      list.add(bean);
    }
    return (ArmYearOracleBean[]) list.toArray(new ArmYearOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdYear(), Types.VARCHAR);
    addToList(list, this.getNmYear(), Types.VARCHAR);
    return list;
  }

}
