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
 * This class is an object representation of the Arts database table ARM_SEASON<BR>
 * Followings are the column of the table: <BR>
 *     ID_SEASON -- VARCHAR2(6)<BR>
 *     DE_SEASON -- VARCHAR2(30)<BR>
 *     ED_CO -- VARCHAR2(20)<BR>
 *     ED_LA -- VARCHAR2(2)<BR>
 *
 */
public class ArmSeasonOracleBean extends BaseOracleBean {

  public ArmSeasonOracleBean() {}

  public static String selectSql = "select ID_SEASON, DE_SEASON, ED_CO, ED_LA from ARM_SEASON ";
  public static String insertSql = "insert into ARM_SEASON (ID_SEASON, DE_SEASON, ED_CO, ED_LA) values (?, ?, ?, ?)";
  public static String updateSql = "update ARM_SEASON set ID_SEASON = ?, DE_SEASON = ?, ED_CO = ?, ED_LA = ? ";
  public static String deleteSql = "delete from ARM_SEASON ";

  public static String TABLE_NAME = "ARM_SEASON";
  public static String COL_ID_SEASON = "ARM_SEASON.ID_SEASON";
  public static String COL_DE_SEASON = "ARM_SEASON.DE_SEASON";
  public static String COL_ED_CO = "ARM_SEASON.ED_CO";
  public static String COL_ED_LA = "ARM_SEASON.ED_LA";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idSeason;
  private String deSeason;
  private String edCo;
  private String edLa;

  public String getIdSeason() { return this.idSeason; }
  public void setIdSeason(String idSeason) { this.idSeason = idSeason; }

  public String getDeSeason() { return this.deSeason; }
  public void setDeSeason(String deSeason) { this.deSeason = deSeason; }

  public String getEdCo() { return this.edCo; }
  public void setEdCo(String edCo) { this.edCo = edCo; }

  public String getEdLa() { return this.edLa; }
  public void setEdLa(String edLa) { this.edLa = edLa; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmSeasonOracleBean bean = new ArmSeasonOracleBean();
      bean.idSeason = getStringFromResultSet(rs, "ID_SEASON");
      bean.deSeason = getStringFromResultSet(rs, "DE_SEASON");
      bean.edCo = getStringFromResultSet(rs, "ED_CO");
      bean.edLa = getStringFromResultSet(rs, "ED_LA");
      list.add(bean);
    }
    return (ArmSeasonOracleBean[]) list.toArray(new ArmSeasonOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdSeason(), Types.VARCHAR);
    addToList(list, this.getDeSeason(), Types.VARCHAR);
    addToList(list, this.getEdCo(), Types.VARCHAR);
    addToList(list, this.getEdLa(), Types.VARCHAR);
    return list;
  }

}
