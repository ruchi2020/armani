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
 * This class is an object representation of the Arts database table PA_PRS<BR>
 * Followings are the column of the table: <BR>
 *     NM_PRS_SLN -- VARCHAR2(40)<BR>
 *     FN_PRS -- VARCHAR2(40)<BR>
 *     MD_PRS -- CHAR(1)<BR>
 *     LN_PRS -- VARCHAR2(40)<BR>
 *     TY_GND_PRS -- VARCHAR2(20)<BR>
 *     DC_PRS_BRT -- DATE(7)<BR>
 *     ID_PRTY_PRS(PK) -- VARCHAR2(128)<BR>
 *     MN_PRS -- VARCHAR2(40)<BR>
 *     NN_PRS -- VARCHAR2(40)<BR>
 *     FL_HND_PRS -- NUMBER(1)<BR>
 *     DB_FN_PRS -- VARCHAR2(30)<BR>
 *     DB_LN_PRS -- VARCHAR2(30)<BR>
 *     SUFFIX -- VARCHAR2(12)<BR>
 *
 */
public class PaPrsOracleBean extends BaseOracleBean {

  public PaPrsOracleBean() {}

  public static String selectSql = "select NM_PRS_SLN, FN_PRS, MD_PRS, LN_PRS, TY_GND_PRS, DC_PRS_BRT, ID_PRTY_PRS, MN_PRS, NN_PRS, FL_HND_PRS, DB_FN_PRS, DB_LN_PRS, SUFFIX from PA_PRS ";
  public static String insertSql = "insert into PA_PRS (NM_PRS_SLN, FN_PRS, MD_PRS, LN_PRS, TY_GND_PRS, DC_PRS_BRT, ID_PRTY_PRS, MN_PRS, NN_PRS, FL_HND_PRS, DB_FN_PRS, DB_LN_PRS, SUFFIX) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update PA_PRS set NM_PRS_SLN = ?, FN_PRS = ?, MD_PRS = ?, LN_PRS = ?, TY_GND_PRS = ?, DC_PRS_BRT = ?, ID_PRTY_PRS = ?, MN_PRS = ?, NN_PRS = ?, FL_HND_PRS = ?, DB_FN_PRS = ?, DB_LN_PRS = ?, SUFFIX = ? ";
  public static String deleteSql = "delete from PA_PRS ";

  public static String TABLE_NAME = "PA_PRS";
  public static String COL_NM_PRS_SLN = "PA_PRS.NM_PRS_SLN";
  public static String COL_FN_PRS = "PA_PRS.FN_PRS";
  public static String COL_MD_PRS = "PA_PRS.MD_PRS";
  public static String COL_LN_PRS = "PA_PRS.LN_PRS";
  public static String COL_TY_GND_PRS = "PA_PRS.TY_GND_PRS";
  public static String COL_DC_PRS_BRT = "PA_PRS.DC_PRS_BRT";
  public static String COL_ID_PRTY_PRS = "PA_PRS.ID_PRTY_PRS";
  public static String COL_MN_PRS = "PA_PRS.MN_PRS";
  public static String COL_NN_PRS = "PA_PRS.NN_PRS";
  public static String COL_FL_HND_PRS = "PA_PRS.FL_HND_PRS";
  public static String COL_DB_FN_PRS = "PA_PRS.DB_FN_PRS";
  public static String COL_DB_LN_PRS = "PA_PRS.DB_LN_PRS";
  public static String COL_SUFFIX = "PA_PRS.SUFFIX";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String nmPrsSln;
  private String fnPrs;
  private String mdPrs;
  private String lnPrs;
  private String tyGndPrs;
  private Date dcPrsBrt;
  private String idPrtyPrs;
  private String mnPrs;
  private String nnPrs;
  private Boolean flHndPrs;
  private String dbFnPrs;
  private String dbLnPrs;
  private String suffix;

  public String getNmPrsSln() { return this.nmPrsSln; }
  public void setNmPrsSln(String nmPrsSln) { this.nmPrsSln = nmPrsSln; }

  public String getFnPrs() { return this.fnPrs; }
  public void setFnPrs(String fnPrs) { this.fnPrs = fnPrs; }

  public String getMdPrs() { return this.mdPrs; }
  public void setMdPrs(String mdPrs) { this.mdPrs = mdPrs; }

  public String getLnPrs() { return this.lnPrs; }
  public void setLnPrs(String lnPrs) { this.lnPrs = lnPrs; }

  public String getTyGndPrs() { return this.tyGndPrs; }
  public void setTyGndPrs(String tyGndPrs) { this.tyGndPrs = tyGndPrs; }

  public Date getDcPrsBrt() { return this.dcPrsBrt; }
  public void setDcPrsBrt(Date dcPrsBrt) { this.dcPrsBrt = dcPrsBrt; }

  public String getIdPrtyPrs() { return this.idPrtyPrs; }
  public void setIdPrtyPrs(String idPrtyPrs) { this.idPrtyPrs = idPrtyPrs; }

  public String getMnPrs() { return this.mnPrs; }
  public void setMnPrs(String mnPrs) { this.mnPrs = mnPrs; }

  public String getNnPrs() { return this.nnPrs; }
  public void setNnPrs(String nnPrs) { this.nnPrs = nnPrs; }

  public Boolean getFlHndPrs() { return this.flHndPrs; }
  public void setFlHndPrs(Boolean flHndPrs) { this.flHndPrs = flHndPrs; }
  public void setFlHndPrs(boolean flHndPrs) { this.flHndPrs = new Boolean(flHndPrs); }

  public String getDbFnPrs() { return this.dbFnPrs; }
  public void setDbFnPrs(String dbFnPrs) { this.dbFnPrs = dbFnPrs; }

  public String getDbLnPrs() { return this.dbLnPrs; }
  public void setDbLnPrs(String dbLnPrs) { this.dbLnPrs = dbLnPrs; }

  public String getSuffix() { return this.suffix; }
  public void setSuffix(String suffix) { this.suffix = suffix; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      PaPrsOracleBean bean = new PaPrsOracleBean();
      bean.nmPrsSln = getStringFromResultSet(rs, "NM_PRS_SLN");
      bean.fnPrs = getStringFromResultSet(rs, "FN_PRS");
      bean.mdPrs = getStringFromResultSet(rs, "MD_PRS");
      bean.lnPrs = getStringFromResultSet(rs, "LN_PRS");
      bean.tyGndPrs = getStringFromResultSet(rs, "TY_GND_PRS");
      bean.dcPrsBrt = getDateFromResultSet(rs, "DC_PRS_BRT");
      bean.idPrtyPrs = getStringFromResultSet(rs, "ID_PRTY_PRS");
      bean.mnPrs = getStringFromResultSet(rs, "MN_PRS");
      bean.nnPrs = getStringFromResultSet(rs, "NN_PRS");
      bean.flHndPrs = getBooleanFromResultSet(rs, "FL_HND_PRS");
      bean.dbFnPrs = getStringFromResultSet(rs, "DB_FN_PRS");
      bean.dbLnPrs = getStringFromResultSet(rs, "DB_LN_PRS");
      bean.suffix = getStringFromResultSet(rs, "SUFFIX");
      list.add(bean);
    }
    return (PaPrsOracleBean[]) list.toArray(new PaPrsOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getNmPrsSln(), Types.VARCHAR);
    addToList(list, this.getFnPrs(), Types.VARCHAR);
    addToList(list, this.getMdPrs(), Types.VARCHAR);
    addToList(list, this.getLnPrs(), Types.VARCHAR);
    addToList(list, this.getTyGndPrs(), Types.VARCHAR);
    addToList(list, this.getDcPrsBrt(), Types.TIMESTAMP);
    addToList(list, this.getIdPrtyPrs(), Types.VARCHAR);
    addToList(list, this.getMnPrs(), Types.VARCHAR);
    addToList(list, this.getNnPrs(), Types.VARCHAR);
    addToList(list, this.getFlHndPrs(), Types.DECIMAL);
    addToList(list, this.getDbFnPrs(), Types.VARCHAR);
    addToList(list, this.getDbLnPrs(), Types.VARCHAR);
    addToList(list, this.getSuffix(), Types.VARCHAR);
    return list;
  }

}
