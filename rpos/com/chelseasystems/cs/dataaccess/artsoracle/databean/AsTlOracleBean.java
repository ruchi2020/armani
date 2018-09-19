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
 * This class is an object representation of the Arts database table AS_TL<BR>
 * Followings are the column of the table: <BR>
 *     ID_RPSTY_TND(PK) -- VARCHAR2(128)<BR>
 *     ID_STR_RT(PK) -- VARCHAR2(128)<BR>
 *     ID_OPR -- VARCHAR2(128)<BR>
 *     SC_TL -- VARCHAR2(20)<BR>
 *     TS_STS_TL -- DATE(7)<BR>
 *     CP_BLNC_DFLT_OPN -- VARCHAR2(75)<BR>
 *     LU_TND_MXM_ALW -- VARCHAR2(75)<BR>
 *     DE_RPSTY_TND -- VARCHAR2(50)<BR>
 *     TY_RND -- VARCHAR2(10)<BR>
 *     TY_RPSTY_TND -- VARCHAR2(10)<BR>
 *
 */
public class AsTlOracleBean extends BaseOracleBean {

  public AsTlOracleBean() {}

  public static String selectSql = "select ID_RPSTY_TND, ID_STR_RT, ID_OPR, SC_TL, TS_STS_TL, CP_BLNC_DFLT_OPN, LU_TND_MXM_ALW, DE_RPSTY_TND, TY_RND, TY_RPSTY_TND from AS_TL ";
  public static String insertSql = "insert into AS_TL (ID_RPSTY_TND, ID_STR_RT, ID_OPR, SC_TL, TS_STS_TL, CP_BLNC_DFLT_OPN, LU_TND_MXM_ALW, DE_RPSTY_TND, TY_RND, TY_RPSTY_TND) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update AS_TL set ID_RPSTY_TND = ?, ID_STR_RT = ?, ID_OPR = ?, SC_TL = ?, TS_STS_TL = ?, CP_BLNC_DFLT_OPN = ?, LU_TND_MXM_ALW = ?, DE_RPSTY_TND = ?, TY_RND = ?, TY_RPSTY_TND = ? ";
  public static String deleteSql = "delete from AS_TL ";

  public static String TABLE_NAME = "AS_TL";
  public static String COL_ID_RPSTY_TND = "AS_TL.ID_RPSTY_TND";
  public static String COL_ID_STR_RT = "AS_TL.ID_STR_RT";
  public static String COL_ID_OPR = "AS_TL.ID_OPR";
  public static String COL_SC_TL = "AS_TL.SC_TL";
  public static String COL_TS_STS_TL = "AS_TL.TS_STS_TL";
  public static String COL_CP_BLNC_DFLT_OPN = "AS_TL.CP_BLNC_DFLT_OPN";
  public static String COL_LU_TND_MXM_ALW = "AS_TL.LU_TND_MXM_ALW";
  public static String COL_DE_RPSTY_TND = "AS_TL.DE_RPSTY_TND";
  public static String COL_TY_RND = "AS_TL.TY_RND";
  public static String COL_TY_RPSTY_TND = "AS_TL.TY_RPSTY_TND";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idRpstyTnd;
  private String idStrRt;
  private String idOpr;
  private String scTl;
  private Date tsStsTl;
  private ArmCurrency cpBlncDfltOpn;
  private ArmCurrency luTndMxmAlw;
  private String deRpstyTnd;
  private String tyRnd;
  private String tyRpstyTnd;

  public String getIdRpstyTnd() { return this.idRpstyTnd; }
  public void setIdRpstyTnd(String idRpstyTnd) { this.idRpstyTnd = idRpstyTnd; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdOpr() { return this.idOpr; }
  public void setIdOpr(String idOpr) { this.idOpr = idOpr; }

  public String getScTl() { return this.scTl; }
  public void setScTl(String scTl) { this.scTl = scTl; }

  public Date getTsStsTl() { return this.tsStsTl; }
  public void setTsStsTl(Date tsStsTl) { this.tsStsTl = tsStsTl; }

  public ArmCurrency getCpBlncDfltOpn() { return this.cpBlncDfltOpn; }
  public void setCpBlncDfltOpn(ArmCurrency cpBlncDfltOpn) { this.cpBlncDfltOpn = cpBlncDfltOpn; }

  public ArmCurrency getLuTndMxmAlw() { return this.luTndMxmAlw; }
  public void setLuTndMxmAlw(ArmCurrency luTndMxmAlw) { this.luTndMxmAlw = luTndMxmAlw; }

  public String getDeRpstyTnd() { return this.deRpstyTnd; }
  public void setDeRpstyTnd(String deRpstyTnd) { this.deRpstyTnd = deRpstyTnd; }

  public String getTyRnd() { return this.tyRnd; }
  public void setTyRnd(String tyRnd) { this.tyRnd = tyRnd; }

  public String getTyRpstyTnd() { return this.tyRpstyTnd; }
  public void setTyRpstyTnd(String tyRpstyTnd) { this.tyRpstyTnd = tyRpstyTnd; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      AsTlOracleBean bean = new AsTlOracleBean();
      bean.idRpstyTnd = getStringFromResultSet(rs, "ID_RPSTY_TND");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idOpr = getStringFromResultSet(rs, "ID_OPR");
      bean.scTl = getStringFromResultSet(rs, "SC_TL");
      bean.tsStsTl = getDateFromResultSet(rs, "TS_STS_TL");
      bean.cpBlncDfltOpn = getCurrencyFromResultSet(rs, "CP_BLNC_DFLT_OPN");
      bean.luTndMxmAlw = getCurrencyFromResultSet(rs, "LU_TND_MXM_ALW");
      bean.deRpstyTnd = getStringFromResultSet(rs, "DE_RPSTY_TND");
      bean.tyRnd = getStringFromResultSet(rs, "TY_RND");
      bean.tyRpstyTnd = getStringFromResultSet(rs, "TY_RPSTY_TND");
      list.add(bean);
    }
    return (AsTlOracleBean[]) list.toArray(new AsTlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdRpstyTnd(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdOpr(), Types.VARCHAR);
    addToList(list, this.getScTl(), Types.VARCHAR);
    addToList(list, this.getTsStsTl(), Types.TIMESTAMP);
    addToList(list, this.getCpBlncDfltOpn(), Types.VARCHAR);
    addToList(list, this.getLuTndMxmAlw(), Types.VARCHAR);
    addToList(list, this.getDeRpstyTnd(), Types.VARCHAR);
    addToList(list, this.getTyRnd(), Types.VARCHAR);
    addToList(list, this.getTyRpstyTnd(), Types.VARCHAR);
    return list;
  }

}
