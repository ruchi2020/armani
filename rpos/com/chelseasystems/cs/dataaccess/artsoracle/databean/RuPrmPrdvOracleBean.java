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
 * This class is an object representation of the Arts database table RU_PRM_PRDV<BR>
 * Followings are the column of the table: <BR>
 *     ID_PRM(PK) -- VARCHAR2(128)<BR>
 *     ID_RU_PRDV(PK) -- VARCHAR2(128)<BR>
 *     ID_GP_TM -- VARCHAR2(256)<BR>
 *     ID_STR_RT(PK) -- VARCHAR2(128)<BR>
 *
 */
public class RuPrmPrdvOracleBean extends BaseOracleBean {

  public RuPrmPrdvOracleBean() {}

  public static String selectSql = "select ID_PRM, ID_RU_PRDV, ID_GP_TM, ID_STR_RT from RU_PRM_PRDV ";
  public static String insertSql = "insert into RU_PRM_PRDV (ID_PRM, ID_RU_PRDV, ID_GP_TM, ID_STR_RT) values (?, ?, ?, ?)";
  public static String updateSql = "update RU_PRM_PRDV set ID_PRM = ?, ID_RU_PRDV = ?, ID_GP_TM = ?, ID_STR_RT = ? ";
  public static String deleteSql = "delete from RU_PRM_PRDV ";

  public static String TABLE_NAME = "RU_PRM_PRDV";
  public static String COL_ID_PRM = "RU_PRM_PRDV.ID_PRM";
  public static String COL_ID_RU_PRDV = "RU_PRM_PRDV.ID_RU_PRDV";
  public static String COL_ID_GP_TM = "RU_PRM_PRDV.ID_GP_TM";
  public static String COL_ID_STR_RT = "RU_PRM_PRDV.ID_STR_RT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idPrm;
  private String idRuPrdv;
  private String idGpTm;
  private String idStrRt;

  public String getIdPrm() { return this.idPrm; }
  public void setIdPrm(String idPrm) { this.idPrm = idPrm; }

  public String getIdRuPrdv() { return this.idRuPrdv; }
  public void setIdRuPrdv(String idRuPrdv) { this.idRuPrdv = idRuPrdv; }

  public String getIdGpTm() { return this.idGpTm; }
  public void setIdGpTm(String idGpTm) { this.idGpTm = idGpTm; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RuPrmPrdvOracleBean bean = new RuPrmPrdvOracleBean();
      bean.idPrm = getStringFromResultSet(rs, "ID_PRM");
      bean.idRuPrdv = getStringFromResultSet(rs, "ID_RU_PRDV");
      bean.idGpTm = getStringFromResultSet(rs, "ID_GP_TM");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      list.add(bean);
    }
    return (RuPrmPrdvOracleBean[]) list.toArray(new RuPrmPrdvOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdPrm(), Types.VARCHAR);
    addToList(list, this.getIdRuPrdv(), Types.VARCHAR);
    addToList(list, this.getIdGpTm(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    return list;
  }

}
