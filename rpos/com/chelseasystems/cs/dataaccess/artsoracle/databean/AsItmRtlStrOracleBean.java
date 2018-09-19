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
 * This class is an object representation of the Arts database table AS_ITM_RTL_STR<BR>
 * Followings are the column of the table: <BR>
 *     ID_STR_RT(PK) -- VARCHAR2(128)<BR>
 *     ID_ITM(PK) -- VARCHAR2(128)<BR>
 *     SC_ITM_SLS -- VARCHAR2(20)<BR>
 *     ID_GP_TX -- VARCHAR2(128)<BR>
 *     DC_ITM_SLS -- DATE(7)<BR>
 *     SC_ITM -- VARCHAR2(20)<BR>
 *     RP_PR_SLS -- VARCHAR2(75)<BR>
 *     FL_MKD_ORGL_PRC_PR -- NUMBER(1)<BR>
 *     QU_MKD_PR_PRC_PR -- NUMBER(7)<BR>
 *     FL_STK_UPDT_ON_HD -- NUMBER(1)<BR>
 *     DC_PRC_EF_PRN_RT -- DATE(7)<BR>
 *     RP_SLS_CRT -- VARCHAR2(75)<BR>
 *     TY_PRC_RT -- VARCHAR2(20)<BR>
 *     FL_PRC_RT_PNT_ALW -- NUMBER(1)<BR>
 *     DC_PRC_SLS_EF_CRT -- DATE(7)<BR>
 *     DC_PRC_SLS_EP_CRT -- DATE(7)<BR>
 *     RP_PRC_MF_RCM_RT -- VARCHAR2(75)<BR>
 *     DC_PRC_MF_RCM_RT -- DATE(7)<BR>
 *     RP_PRC_CMPR_AT_SLS -- VARCHAR2(75)<BR>
 *     CURRENCY_CODE -- VARCHAR2(50)<BR>
 *     CD_VAT -- VARCHAR2(128)<BR>
 *     VAT_RATE -- NUMBER(7.4)<BR>
 *     FL_VAT_CLS -- NUMBER(1)<BR>
 *     LU_EXM_TX -- VARCHAR2(2)<BR>
 *     UPDATE_DT -- DATE(7)<BR>
 *     NM_ITM -- VARCHAR2(40)<BR>
 *
 */
public class AsItmRtlStrOracleBean extends BaseOracleBean {

  public AsItmRtlStrOracleBean() {}

  public static String selectSql = "select ID_STR_RT, ID_ITM, SC_ITM_SLS, ID_GP_TX, DC_ITM_SLS, SC_ITM, RP_PR_SLS, FL_MKD_ORGL_PRC_PR, QU_MKD_PR_PRC_PR, FL_STK_UPDT_ON_HD, DC_PRC_EF_PRN_RT, RP_SLS_CRT, TY_PRC_RT, FL_PRC_RT_PNT_ALW, DC_PRC_SLS_EF_CRT, DC_PRC_SLS_EP_CRT, RP_PRC_MF_RCM_RT, DC_PRC_MF_RCM_RT, RP_PRC_CMPR_AT_SLS, CURRENCY_CODE, CD_VAT, VAT_RATE, FL_VAT_CLS, LU_EXM_TX, UPDATE_DT, NM_ITM from AS_ITM_RTL_STR ";
  public static String insertSql = "insert into AS_ITM_RTL_STR (ID_STR_RT, ID_ITM, SC_ITM_SLS, ID_GP_TX, DC_ITM_SLS, SC_ITM, RP_PR_SLS, FL_MKD_ORGL_PRC_PR, QU_MKD_PR_PRC_PR, FL_STK_UPDT_ON_HD, DC_PRC_EF_PRN_RT, RP_SLS_CRT, TY_PRC_RT, FL_PRC_RT_PNT_ALW, DC_PRC_SLS_EF_CRT, DC_PRC_SLS_EP_CRT, RP_PRC_MF_RCM_RT, DC_PRC_MF_RCM_RT, RP_PRC_CMPR_AT_SLS, CURRENCY_CODE, CD_VAT, VAT_RATE, FL_VAT_CLS, LU_EXM_TX, UPDATE_DT, NM_ITM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update AS_ITM_RTL_STR set ID_STR_RT = ?, ID_ITM = ?, SC_ITM_SLS = ?, ID_GP_TX = ?, DC_ITM_SLS = ?, SC_ITM = ?, RP_PR_SLS = ?, FL_MKD_ORGL_PRC_PR = ?, QU_MKD_PR_PRC_PR = ?, FL_STK_UPDT_ON_HD = ?, DC_PRC_EF_PRN_RT = ?, RP_SLS_CRT = ?, TY_PRC_RT = ?, FL_PRC_RT_PNT_ALW = ?, DC_PRC_SLS_EF_CRT = ?, DC_PRC_SLS_EP_CRT = ?, RP_PRC_MF_RCM_RT = ?, DC_PRC_MF_RCM_RT = ?, RP_PRC_CMPR_AT_SLS = ?, CURRENCY_CODE = ?, CD_VAT = ?, VAT_RATE = ?, FL_VAT_CLS = ?, LU_EXM_TX = ?, UPDATE_DT = ?, NM_ITM = ? ";
  public static String deleteSql = "delete from AS_ITM_RTL_STR ";

  public static String TABLE_NAME = "AS_ITM_RTL_STR";
  public static String COL_ID_STR_RT = "AS_ITM_RTL_STR.ID_STR_RT";
  public static String COL_ID_ITM = "AS_ITM_RTL_STR.ID_ITM";
  public static String COL_SC_ITM_SLS = "AS_ITM_RTL_STR.SC_ITM_SLS";
  public static String COL_ID_GP_TX = "AS_ITM_RTL_STR.ID_GP_TX";
  public static String COL_DC_ITM_SLS = "AS_ITM_RTL_STR.DC_ITM_SLS";
  public static String COL_SC_ITM = "AS_ITM_RTL_STR.SC_ITM";
  public static String COL_RP_PR_SLS = "AS_ITM_RTL_STR.RP_PR_SLS";
  public static String COL_FL_MKD_ORGL_PRC_PR = "AS_ITM_RTL_STR.FL_MKD_ORGL_PRC_PR";
  public static String COL_QU_MKD_PR_PRC_PR = "AS_ITM_RTL_STR.QU_MKD_PR_PRC_PR";
  public static String COL_FL_STK_UPDT_ON_HD = "AS_ITM_RTL_STR.FL_STK_UPDT_ON_HD";
  public static String COL_DC_PRC_EF_PRN_RT = "AS_ITM_RTL_STR.DC_PRC_EF_PRN_RT";
  public static String COL_RP_SLS_CRT = "AS_ITM_RTL_STR.RP_SLS_CRT";
  public static String COL_TY_PRC_RT = "AS_ITM_RTL_STR.TY_PRC_RT";
  public static String COL_FL_PRC_RT_PNT_ALW = "AS_ITM_RTL_STR.FL_PRC_RT_PNT_ALW";
  public static String COL_DC_PRC_SLS_EF_CRT = "AS_ITM_RTL_STR.DC_PRC_SLS_EF_CRT";
  public static String COL_DC_PRC_SLS_EP_CRT = "AS_ITM_RTL_STR.DC_PRC_SLS_EP_CRT";
  public static String COL_RP_PRC_MF_RCM_RT = "AS_ITM_RTL_STR.RP_PRC_MF_RCM_RT";
  public static String COL_DC_PRC_MF_RCM_RT = "AS_ITM_RTL_STR.DC_PRC_MF_RCM_RT";
  public static String COL_RP_PRC_CMPR_AT_SLS = "AS_ITM_RTL_STR.RP_PRC_CMPR_AT_SLS";
  public static String COL_CURRENCY_CODE = "AS_ITM_RTL_STR.CURRENCY_CODE";
  public static String COL_CD_VAT = "AS_ITM_RTL_STR.CD_VAT";
  public static String COL_VAT_RATE = "AS_ITM_RTL_STR.VAT_RATE";
  public static String COL_FL_VAT_CLS = "AS_ITM_RTL_STR.FL_VAT_CLS";
  public static String COL_LU_EXM_TX = "AS_ITM_RTL_STR.LU_EXM_TX";
  public static String COL_UPDATE_DT = "AS_ITM_RTL_STR.UPDATE_DT";
  public static String COL_NM_ITM = "AS_ITM_RTL_STR.NM_ITM";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idStrRt;
  private String idItm;
  private String scItmSls;
  private String idGpTx;
  private Date dcItmSls;
  private String scItm;
  private ArmCurrency rpPrSls;
  private Boolean flMkdOrglPrcPr;
  private Long quMkdPrPrcPr;
  private Boolean flStkUpdtOnHd;
  private Date dcPrcEfPrnRt;
  private ArmCurrency rpSlsCrt;
  private String tyPrcRt;
  private Boolean flPrcRtPntAlw;
  private Date dcPrcSlsEfCrt;
  private Date dcPrcSlsEpCrt;
  private ArmCurrency rpPrcMfRcmRt;
  private Date dcPrcMfRcmRt;
  private ArmCurrency rpPrcCmprAtSls;
  private String currencyCode;
  private String cdVat;
  private Double vatRate;
  private Boolean flVatCls;
  private String luExmTx;
  private Date updateDt;
  private String nmItm;

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdItm() { return this.idItm; }
  public void setIdItm(String idItm) { this.idItm = idItm; }

  public String getScItmSls() { return this.scItmSls; }
  public void setScItmSls(String scItmSls) { this.scItmSls = scItmSls; }

  public String getIdGpTx() { return this.idGpTx; }
  public void setIdGpTx(String idGpTx) { this.idGpTx = idGpTx; }

  public Date getDcItmSls() { return this.dcItmSls; }
  public void setDcItmSls(Date dcItmSls) { this.dcItmSls = dcItmSls; }

  public String getScItm() { return this.scItm; }
  public void setScItm(String scItm) { this.scItm = scItm; }

  public ArmCurrency getRpPrSls() { return this.rpPrSls; }
  public void setRpPrSls(ArmCurrency rpPrSls) { this.rpPrSls = rpPrSls; }

  public Boolean getFlMkdOrglPrcPr() { return this.flMkdOrglPrcPr; }
  public void setFlMkdOrglPrcPr(Boolean flMkdOrglPrcPr) { this.flMkdOrglPrcPr = flMkdOrglPrcPr; }
  public void setFlMkdOrglPrcPr(boolean flMkdOrglPrcPr) { this.flMkdOrglPrcPr = new Boolean(flMkdOrglPrcPr); }

  public Long getQuMkdPrPrcPr() { return this.quMkdPrPrcPr; }
  public void setQuMkdPrPrcPr(Long quMkdPrPrcPr) { this.quMkdPrPrcPr = quMkdPrPrcPr; }
  public void setQuMkdPrPrcPr(long quMkdPrPrcPr) { this.quMkdPrPrcPr = new Long(quMkdPrPrcPr); }
  public void setQuMkdPrPrcPr(int quMkdPrPrcPr) { this.quMkdPrPrcPr = new Long((long)quMkdPrPrcPr); }

  public Boolean getFlStkUpdtOnHd() { return this.flStkUpdtOnHd; }
  public void setFlStkUpdtOnHd(Boolean flStkUpdtOnHd) { this.flStkUpdtOnHd = flStkUpdtOnHd; }
  public void setFlStkUpdtOnHd(boolean flStkUpdtOnHd) { this.flStkUpdtOnHd = new Boolean(flStkUpdtOnHd); }

  public Date getDcPrcEfPrnRt() { return this.dcPrcEfPrnRt; }
  public void setDcPrcEfPrnRt(Date dcPrcEfPrnRt) { this.dcPrcEfPrnRt = dcPrcEfPrnRt; }

  public ArmCurrency getRpSlsCrt() { return this.rpSlsCrt; }
  public void setRpSlsCrt(ArmCurrency rpSlsCrt) { this.rpSlsCrt = rpSlsCrt; }

  public String getTyPrcRt() { return this.tyPrcRt; }
  public void setTyPrcRt(String tyPrcRt) { this.tyPrcRt = tyPrcRt; }

  public Boolean getFlPrcRtPntAlw() { return this.flPrcRtPntAlw; }
  public void setFlPrcRtPntAlw(Boolean flPrcRtPntAlw) { this.flPrcRtPntAlw = flPrcRtPntAlw; }
  public void setFlPrcRtPntAlw(boolean flPrcRtPntAlw) { this.flPrcRtPntAlw = new Boolean(flPrcRtPntAlw); }

  public Date getDcPrcSlsEfCrt() { return this.dcPrcSlsEfCrt; }
  public void setDcPrcSlsEfCrt(Date dcPrcSlsEfCrt) { this.dcPrcSlsEfCrt = dcPrcSlsEfCrt; }

  public Date getDcPrcSlsEpCrt() { return this.dcPrcSlsEpCrt; }
  public void setDcPrcSlsEpCrt(Date dcPrcSlsEpCrt) { this.dcPrcSlsEpCrt = dcPrcSlsEpCrt; }

  public ArmCurrency getRpPrcMfRcmRt() { return this.rpPrcMfRcmRt; }
  public void setRpPrcMfRcmRt(ArmCurrency rpPrcMfRcmRt) { this.rpPrcMfRcmRt = rpPrcMfRcmRt; }

  public Date getDcPrcMfRcmRt() { return this.dcPrcMfRcmRt; }
  public void setDcPrcMfRcmRt(Date dcPrcMfRcmRt) { this.dcPrcMfRcmRt = dcPrcMfRcmRt; }

  public ArmCurrency getRpPrcCmprAtSls() { return this.rpPrcCmprAtSls; }
  public void setRpPrcCmprAtSls(ArmCurrency rpPrcCmprAtSls) { this.rpPrcCmprAtSls = rpPrcCmprAtSls; }

  public String getCurrencyCode() { return this.currencyCode; }
  public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

  public String getCdVat() { return this.cdVat; }
  public void setCdVat(String cdVat) { this.cdVat = cdVat; }

  public Double getVatRate() { return this.vatRate; }
  public void setVatRate(Double vatRate) { this.vatRate = vatRate; }
  public void setVatRate(double vatRate) { this.vatRate = new Double(vatRate); }

  public Boolean getFlVatCls() { return this.flVatCls; }
  public void setFlVatCls(Boolean flVatCls) { this.flVatCls = flVatCls; }
  public void setFlVatCls(boolean flVatCls) { this.flVatCls = new Boolean(flVatCls); }

  public String getLuExmTx() { return this.luExmTx; }
  public void setLuExmTx(String luExmTx) { this.luExmTx = luExmTx; }

  public Date getUpdateDt() { return this.updateDt; }
  public void setUpdateDt(Date updateDt) { this.updateDt = updateDt; }

  public String getNmItm() { return this.nmItm; }
  public void setNmItm(String nmItm) { this.nmItm = nmItm; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      AsItmRtlStrOracleBean bean = new AsItmRtlStrOracleBean();
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idItm = getStringFromResultSet(rs, "ID_ITM");
      bean.scItmSls = getStringFromResultSet(rs, "SC_ITM_SLS");
      bean.idGpTx = getStringFromResultSet(rs, "ID_GP_TX");
      bean.dcItmSls = getDateFromResultSet(rs, "DC_ITM_SLS");
      bean.scItm = getStringFromResultSet(rs, "SC_ITM");
      bean.rpPrSls = getCurrencyFromResultSet(rs, "RP_PR_SLS");
      bean.flMkdOrglPrcPr = getBooleanFromResultSet(rs, "FL_MKD_ORGL_PRC_PR");
      bean.quMkdPrPrcPr = getLongFromResultSet(rs, "QU_MKD_PR_PRC_PR");
      bean.flStkUpdtOnHd = getBooleanFromResultSet(rs, "FL_STK_UPDT_ON_HD");
      bean.dcPrcEfPrnRt = getDateFromResultSet(rs, "DC_PRC_EF_PRN_RT");
      bean.rpSlsCrt = getCurrencyFromResultSet(rs, "RP_SLS_CRT");
      bean.tyPrcRt = getStringFromResultSet(rs, "TY_PRC_RT");
      bean.flPrcRtPntAlw = getBooleanFromResultSet(rs, "FL_PRC_RT_PNT_ALW");
      bean.dcPrcSlsEfCrt = getDateFromResultSet(rs, "DC_PRC_SLS_EF_CRT");
      bean.dcPrcSlsEpCrt = getDateFromResultSet(rs, "DC_PRC_SLS_EP_CRT");
      bean.rpPrcMfRcmRt = getCurrencyFromResultSet(rs, "RP_PRC_MF_RCM_RT");
      bean.dcPrcMfRcmRt = getDateFromResultSet(rs, "DC_PRC_MF_RCM_RT");
      bean.rpPrcCmprAtSls = getCurrencyFromResultSet(rs, "RP_PRC_CMPR_AT_SLS");
      bean.currencyCode = getStringFromResultSet(rs, "CURRENCY_CODE");
      bean.cdVat = getStringFromResultSet(rs, "CD_VAT");
      bean.vatRate = getDoubleFromResultSet(rs, "VAT_RATE");
      bean.flVatCls = getBooleanFromResultSet(rs, "FL_VAT_CLS");
      bean.luExmTx = getStringFromResultSet(rs, "LU_EXM_TX");
      bean.updateDt = getDateFromResultSet(rs, "UPDATE_DT");
      bean.nmItm = getStringFromResultSet(rs, "NM_ITM");
      list.add(bean);
    }
    return (AsItmRtlStrOracleBean[]) list.toArray(new AsItmRtlStrOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdItm(), Types.VARCHAR);
    addToList(list, this.getScItmSls(), Types.VARCHAR);
    addToList(list, this.getIdGpTx(), Types.VARCHAR);
    addToList(list, this.getDcItmSls(), Types.TIMESTAMP);
    addToList(list, this.getScItm(), Types.VARCHAR);
    addToList(list, this.getRpPrSls(), Types.VARCHAR);
    addToList(list, this.getFlMkdOrglPrcPr(), Types.DECIMAL);
    addToList(list, this.getQuMkdPrPrcPr(), Types.DECIMAL);
    addToList(list, this.getFlStkUpdtOnHd(), Types.DECIMAL);
    addToList(list, this.getDcPrcEfPrnRt(), Types.TIMESTAMP);
    addToList(list, this.getRpSlsCrt(), Types.VARCHAR);
    addToList(list, this.getTyPrcRt(), Types.VARCHAR);
    addToList(list, this.getFlPrcRtPntAlw(), Types.DECIMAL);
    addToList(list, this.getDcPrcSlsEfCrt(), Types.TIMESTAMP);
    addToList(list, this.getDcPrcSlsEpCrt(), Types.TIMESTAMP);
    addToList(list, this.getRpPrcMfRcmRt(), Types.VARCHAR);
    addToList(list, this.getDcPrcMfRcmRt(), Types.TIMESTAMP);
    addToList(list, this.getRpPrcCmprAtSls(), Types.VARCHAR);
    addToList(list, this.getCurrencyCode(), Types.VARCHAR);
    addToList(list, this.getCdVat(), Types.VARCHAR);
    addToList(list, this.getVatRate(), Types.DECIMAL);
    addToList(list, this.getFlVatCls(), Types.DECIMAL);
    addToList(list, this.getLuExmTx(), Types.VARCHAR);
    addToList(list, this.getUpdateDt(), Types.TIMESTAMP);
    addToList(list, this.getNmItm(), Types.VARCHAR);
    return list;
  }

}
