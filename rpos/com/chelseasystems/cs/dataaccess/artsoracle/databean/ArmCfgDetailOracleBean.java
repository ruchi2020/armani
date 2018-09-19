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
 * This class is an object representation of the Arts database table ARM_CFG_DETAIL<BR>
 * Followings are the column of the table: <BR>
 *     TY_CFG(PK) -- VARCHAR2(50)<BR>
 *     CD_CFG(PK) -- VARCHAR2(50)<BR>
 *     ED_CO(PK) -- VARCHAR2(20)<BR>
 *     ED_LA(PK) -- VARCHAR2(20)<BR>
 *     DE_CFG -- VARCHAR2(200)<BR>
 *     PRIORITY -- CHAR(3)<BR>
 *
 */
public class ArmCfgDetailOracleBean extends BaseOracleBean {

  public ArmCfgDetailOracleBean() {}

  public static String selectSql = "select TY_CFG, CD_CFG, ED_CO, ED_LA, DE_CFG, PRIORITY from ARM_CFG_DETAIL ";
  public static String insertSql = "insert into ARM_CFG_DETAIL (TY_CFG, CD_CFG, ED_CO, ED_LA, DE_CFG, PRIORITY) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CFG_DETAIL set TY_CFG = ?, CD_CFG = ?, ED_CO = ?, ED_LA = ?, DE_CFG = ?, PRIORITY = ? ";
  public static String deleteSql = "delete from ARM_CFG_DETAIL ";

  public static String TABLE_NAME = "ARM_CFG_DETAIL";
  public static String COL_TY_CFG = "ARM_CFG_DETAIL.TY_CFG";
  public static String COL_CD_CFG = "ARM_CFG_DETAIL.CD_CFG";
  public static String COL_ED_CO = "ARM_CFG_DETAIL.ED_CO";
  public static String COL_ED_LA = "ARM_CFG_DETAIL.ED_LA";
  public static String COL_DE_CFG = "ARM_CFG_DETAIL.DE_CFG";
  public static String COL_PRIORITY = "ARM_CFG_DETAIL.PRIORITY";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String tyCfg;
  private String cdCfg;
  private String edCo;
  private String edLa;
  private String deCfg;
  private String priority;

  public String getTyCfg() { return this.tyCfg; }
  public void setTyCfg(String tyCfg) { this.tyCfg = tyCfg; }

  public String getCdCfg() { return this.cdCfg; }
  public void setCdCfg(String cdCfg) { this.cdCfg = cdCfg; }

  public String getEdCo() { return this.edCo; }
  public void setEdCo(String edCo) { this.edCo = edCo; }

  public String getEdLa() { return this.edLa; }
  public void setEdLa(String edLa) { this.edLa = edLa; }

  public String getDeCfg() { return this.deCfg; }
  public void setDeCfg(String deCfg) { this.deCfg = deCfg; }

  public String getPriority() { return this.priority; }
  public void setPriority(String priority) { this.priority = priority; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCfgDetailOracleBean bean = new ArmCfgDetailOracleBean();
      bean.tyCfg = getStringFromResultSet(rs, "TY_CFG");
      bean.cdCfg = getStringFromResultSet(rs, "CD_CFG");
      bean.edCo = getStringFromResultSet(rs, "ED_CO");
      bean.edLa = getStringFromResultSet(rs, "ED_LA");
      bean.deCfg = getStringFromResultSet(rs, "DE_CFG");
      bean.priority = getStringFromResultSet(rs, "PRIORITY");
      list.add(bean);
    }
    return (ArmCfgDetailOracleBean[]) list.toArray(new ArmCfgDetailOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTyCfg(), Types.VARCHAR);
    addToList(list, this.getCdCfg(), Types.VARCHAR);
    addToList(list, this.getEdCo(), Types.VARCHAR);
    addToList(list, this.getEdLa(), Types.VARCHAR);
    addToList(list, this.getDeCfg(), Types.VARCHAR);
    addToList(list, this.getPriority(), Types.VARCHAR);
    return list;
  }

}
