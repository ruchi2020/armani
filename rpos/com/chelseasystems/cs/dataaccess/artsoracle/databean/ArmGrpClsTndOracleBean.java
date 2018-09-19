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
 * This class is an object representation of the Arts database table ARM_GRP_CLS_TND<BR>
 * Followings are the column of the table: <BR>
 *     LU_CLS_TND -- VARCHAR2(40)<BR>
 *     TND_CODE -- VARCHAR2(10)<BR>
 *     ED_CO -- VARCHAR2(20)<BR>
 *     ED_LA -- VARCHAR2(20)<BR>
 *     DE_CLS_TND -- VARCHAR2(255)<BR>
 *
 */
public class ArmGrpClsTndOracleBean extends BaseOracleBean {

  public ArmGrpClsTndOracleBean() {}

  public static String selectSql = "select LU_CLS_TND, TND_CODE, ED_CO, ED_LA, DE_CLS_TND from ARM_GRP_CLS_TND ";
  public static String insertSql = "insert into ARM_GRP_CLS_TND (LU_CLS_TND, TND_CODE, ED_CO, ED_LA, DE_CLS_TND) values (?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_GRP_CLS_TND set LU_CLS_TND = ?, TND_CODE = ?, ED_CO = ?, ED_LA = ?, DE_CLS_TND = ? ";
  public static String deleteSql = "delete from ARM_GRP_CLS_TND ";

  public static String TABLE_NAME = "ARM_GRP_CLS_TND";
  public static String COL_LU_CLS_TND = "ARM_GRP_CLS_TND.LU_CLS_TND";
  public static String COL_TND_CODE = "ARM_GRP_CLS_TND.TND_CODE";
  public static String COL_ED_CO = "ARM_GRP_CLS_TND.ED_CO";
  public static String COL_ED_LA = "ARM_GRP_CLS_TND.ED_LA";
  public static String COL_DE_CLS_TND = "ARM_GRP_CLS_TND.DE_CLS_TND";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String luClsTnd;
  private String tndCode;
  private String edCo;
  private String edLa;
  private String deClsTnd;

  public String getLuClsTnd() { return this.luClsTnd; }
  public void setLuClsTnd(String luClsTnd) { this.luClsTnd = luClsTnd; }

  public String getTndCode() { return this.tndCode; }
  public void setTndCode(String tndCode) { this.tndCode = tndCode; }

  public String getEdCo() { return this.edCo; }
  public void setEdCo(String edCo) { this.edCo = edCo; }

  public String getEdLa() { return this.edLa; }
  public void setEdLa(String edLa) { this.edLa = edLa; }

  public String getDeClsTnd() { return this.deClsTnd; }
  public void setDeClsTnd(String deClsTnd) { this.deClsTnd = deClsTnd; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmGrpClsTndOracleBean bean = new ArmGrpClsTndOracleBean();
      bean.luClsTnd = getStringFromResultSet(rs, "LU_CLS_TND");
      bean.tndCode = getStringFromResultSet(rs, "TND_CODE");
      bean.edCo = getStringFromResultSet(rs, "ED_CO");
      bean.edLa = getStringFromResultSet(rs, "ED_LA");
      bean.deClsTnd = getStringFromResultSet(rs, "DE_CLS_TND");
      list.add(bean);
    }
    return (ArmGrpClsTndOracleBean[]) list.toArray(new ArmGrpClsTndOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getLuClsTnd(), Types.VARCHAR);
    addToList(list, this.getTndCode(), Types.VARCHAR);
    addToList(list, this.getEdCo(), Types.VARCHAR);
    addToList(list, this.getEdLa(), Types.VARCHAR);
    addToList(list, this.getDeClsTnd(), Types.VARCHAR);
    return list;
  }

}
