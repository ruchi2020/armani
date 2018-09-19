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
 * This class is an object representation of the Arts database table CO_PRM<BR>
 * Followings are the column of the table: <BR>
 *     ID_PRM -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     ID_ACNT_LDG -- VARCHAR2(128)<BR>
 *     DC_PRM_EF -- VARCHAR2(96)<BR>
 *     DC_PRM_EP -- VARCHAR2(96)<BR>
 *     CD_MLP_EL -- CHAR(2)<BR>
 *     TY_CNCRN -- CHAR(2)<BR>
 *     TY_UP_SELL -- CHAR(2)<BR>
 *     NM_PRM_OPR -- VARCHAR2(40)<BR>
 *     NM_PRM_CT -- VARCHAR2(40)<BR>
 *     NM_PRM_PRT -- VARCHAR2(40)<BR>
 *     DP_ACNT -- CHAR(4)<BR>
 *
 */
public class CoPrmOracleBean extends BaseOracleBean {

  public CoPrmOracleBean() {}

  public static String selectSql = "select ID_PRM, ID_STR_RT, ID_ACNT_LDG, DC_PRM_EF, DC_PRM_EP, CD_MLP_EL, TY_CNCRN, TY_UP_SELL, NM_PRM_OPR, NM_PRM_CT, NM_PRM_PRT, DP_ACNT from CO_PRM ";
  public static String insertSql = "insert into CO_PRM (ID_PRM, ID_STR_RT, ID_ACNT_LDG, DC_PRM_EF, DC_PRM_EP, CD_MLP_EL, TY_CNCRN, TY_UP_SELL, NM_PRM_OPR, NM_PRM_CT, NM_PRM_PRT, DP_ACNT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update CO_PRM set ID_PRM = ?, ID_STR_RT = ?, ID_ACNT_LDG = ?, DC_PRM_EF = ?, DC_PRM_EP = ?, CD_MLP_EL = ?, TY_CNCRN = ?, TY_UP_SELL = ?, NM_PRM_OPR = ?, NM_PRM_CT = ?, NM_PRM_PRT = ?, DP_ACNT = ? ";
  public static String deleteSql = "delete from CO_PRM ";

  public static String TABLE_NAME = "CO_PRM";
  public static String COL_ID_PRM = "CO_PRM.ID_PRM";
  public static String COL_ID_STR_RT = "CO_PRM.ID_STR_RT";
  public static String COL_ID_ACNT_LDG = "CO_PRM.ID_ACNT_LDG";
  public static String COL_DC_PRM_EF = "CO_PRM.DC_PRM_EF";
  public static String COL_DC_PRM_EP = "CO_PRM.DC_PRM_EP";
  public static String COL_CD_MLP_EL = "CO_PRM.CD_MLP_EL";
  public static String COL_TY_CNCRN = "CO_PRM.TY_CNCRN";
  public static String COL_TY_UP_SELL = "CO_PRM.TY_UP_SELL";
  public static String COL_NM_PRM_OPR = "CO_PRM.NM_PRM_OPR";
  public static String COL_NM_PRM_CT = "CO_PRM.NM_PRM_CT";
  public static String COL_NM_PRM_PRT = "CO_PRM.NM_PRM_PRT";
  public static String COL_DP_ACNT = "CO_PRM.DP_ACNT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idPrm;
  private String idStrRt;
  private String idAcntLdg;
  private Calendar dcPrmEf;
  private Calendar dcPrmEp;
  private String cdMlpEl;
  private String tyCncrn;
  private String tyUpSell;
  private String nmPrmOpr;
  private String nmPrmCt;
  private String nmPrmPrt;
  private String dpAcnt;

  public String getIdPrm() { return this.idPrm; }
  public void setIdPrm(String idPrm) { this.idPrm = idPrm; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdAcntLdg() { return this.idAcntLdg; }
  public void setIdAcntLdg(String idAcntLdg) { this.idAcntLdg = idAcntLdg; }

  public Calendar getDcPrmEf() { return this.dcPrmEf; }
  public void setDcPrmEf(Calendar dcPrmEf) { this.dcPrmEf = dcPrmEf; }

  public Calendar getDcPrmEp() { return this.dcPrmEp; }
  public void setDcPrmEp(Calendar dcPrmEp) { this.dcPrmEp = dcPrmEp; }

  public String getCdMlpEl() { return this.cdMlpEl; }
  public void setCdMlpEl(String cdMlpEl) { this.cdMlpEl = cdMlpEl; }

  public String getTyCncrn() { return this.tyCncrn; }
  public void setTyCncrn(String tyCncrn) { this.tyCncrn = tyCncrn; }

  public String getTyUpSell() { return this.tyUpSell; }
  public void setTyUpSell(String tyUpSell) { this.tyUpSell = tyUpSell; }

  public String getNmPrmOpr() { return this.nmPrmOpr; }
  public void setNmPrmOpr(String nmPrmOpr) { this.nmPrmOpr = nmPrmOpr; }

  public String getNmPrmCt() { return this.nmPrmCt; }
  public void setNmPrmCt(String nmPrmCt) { this.nmPrmCt = nmPrmCt; }

  public String getNmPrmPrt() { return this.nmPrmPrt; }
  public void setNmPrmPrt(String nmPrmPrt) { this.nmPrmPrt = nmPrmPrt; }

  public String getDpAcnt() { return this.dpAcnt; }
  public void setDpAcnt(String dpAcnt) { this.dpAcnt = dpAcnt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      CoPrmOracleBean bean = new CoPrmOracleBean();
      bean.idPrm = getStringFromResultSet(rs, "ID_PRM");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idAcntLdg = getStringFromResultSet(rs, "ID_ACNT_LDG");
      bean.dcPrmEf = getCalendarFromResultSet(rs, "DC_PRM_EF");
      bean.dcPrmEp = getCalendarFromResultSet(rs, "DC_PRM_EP");
      bean.cdMlpEl = getStringFromResultSet(rs, "CD_MLP_EL");
      bean.tyCncrn = getStringFromResultSet(rs, "TY_CNCRN");
      bean.tyUpSell = getStringFromResultSet(rs, "TY_UP_SELL");
      bean.nmPrmOpr = getStringFromResultSet(rs, "NM_PRM_OPR");
      bean.nmPrmCt = getStringFromResultSet(rs, "NM_PRM_CT");
      bean.nmPrmPrt = getStringFromResultSet(rs, "NM_PRM_PRT");
      bean.dpAcnt = getStringFromResultSet(rs, "DP_ACNT");
      list.add(bean);
    }
    return (CoPrmOracleBean[]) list.toArray(new CoPrmOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdPrm(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdAcntLdg(), Types.VARCHAR);
    addToList(list, this.getDcPrmEf(), Types.VARCHAR);
    addToList(list, this.getDcPrmEp(), Types.VARCHAR);
    addToList(list, this.getCdMlpEl(), Types.VARCHAR);
    addToList(list, this.getTyCncrn(), Types.VARCHAR);
    addToList(list, this.getTyUpSell(), Types.VARCHAR);
    addToList(list, this.getNmPrmOpr(), Types.VARCHAR);
    addToList(list, this.getNmPrmCt(), Types.VARCHAR);
    addToList(list, this.getNmPrmPrt(), Types.VARCHAR);
    addToList(list, this.getDpAcnt(), Types.VARCHAR);
    return list;
  }

}
