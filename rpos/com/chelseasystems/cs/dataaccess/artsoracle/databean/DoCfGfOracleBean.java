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
 * This class is an object representation of the Arts database table DO_CF_GF<BR>
 * Followings are the column of the table: <BR>
 *     ID_STR_ISSG -- VARCHAR2(128)<BR>
 *     ID_NMB_SRZ_GF_CF(PK) -- VARCHAR2(40)<BR>
 *     TY_GF_CF(PK) -- VARCHAR2(40)<BR>
 *     TS_ISS_GF_CF -- DATE(7)<BR>
 *     DC_EP_GF_CF -- DATE(7)<BR>
 *     MO_VL_FC_GF_CF -- VARCHAR2(75)<BR>
 *     MO_BLNC_UNSP_GF_CF -- VARCHAR2(75)<BR>
 *     TY_ISS_GF_CF -- VARCHAR2(20)<BR>
 *     NM_DNR_GF_CF -- VARCHAR2(40)<BR>
 *     AD_DNR_GF_CF -- VARCHAR2(40)<BR>
 *     CI_DNR_GF_CF -- VARCHAR2(30)<BR>
 *     ST_DNR_GF_CF -- CHAR(2)<BR>
 *     CC_DNR_GF_CF -- VARCHAR2(10)<BR>
 *     PC_DNR_GF_CF -- VARCHAR2(15)<BR>
 *     TA_DNR_GF_CF -- VARCHAR2(10)<BR>
 *     TL_DNR_GF_CF -- VARCHAR2(15)<BR>
 *     FN_DNR_GF_CF -- VARCHAR2(40)<BR>
 *     DELETED -- NUMBER(1)<BR>
 *     GIFT_CONTROL -- VARCHAR2(200)<BR>
 *     AUDIT_NOTE -- VARCHAR2(200)<BR>
 *     ID_CT -- VARCHAR2(128)<BR>
 *     DC_EXPIRATION -- DATE(7)<BR>
 *     LOYALTY_NUM -- VARCHAR2(20)<BR>
 *
 */
public class DoCfGfOracleBean extends BaseOracleBean {

  public DoCfGfOracleBean() {}

  public static String selectSql = "select ID_STR_ISSG, ID_NMB_SRZ_GF_CF, TY_GF_CF, TS_ISS_GF_CF, DC_EP_GF_CF, MO_VL_FC_GF_CF, MO_BLNC_UNSP_GF_CF, TY_ISS_GF_CF, NM_DNR_GF_CF, AD_DNR_GF_CF, CI_DNR_GF_CF, ST_DNR_GF_CF, CC_DNR_GF_CF, PC_DNR_GF_CF, TA_DNR_GF_CF, TL_DNR_GF_CF, FN_DNR_GF_CF, DELETED, GIFT_CONTROL, AUDIT_NOTE, ID_CT, DC_EXPIRATION, LOYALTY_NUM from DO_CF_GF ";
  public static String insertSql = "insert into DO_CF_GF (ID_STR_ISSG, ID_NMB_SRZ_GF_CF, TY_GF_CF, TS_ISS_GF_CF, DC_EP_GF_CF, MO_VL_FC_GF_CF, MO_BLNC_UNSP_GF_CF, TY_ISS_GF_CF, NM_DNR_GF_CF, AD_DNR_GF_CF, CI_DNR_GF_CF, ST_DNR_GF_CF, CC_DNR_GF_CF, PC_DNR_GF_CF, TA_DNR_GF_CF, TL_DNR_GF_CF, FN_DNR_GF_CF, DELETED, GIFT_CONTROL, AUDIT_NOTE, ID_CT, DC_EXPIRATION, LOYALTY_NUM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update DO_CF_GF set ID_STR_ISSG = ?, ID_NMB_SRZ_GF_CF = ?, TY_GF_CF = ?, TS_ISS_GF_CF = ?, DC_EP_GF_CF = ?, MO_VL_FC_GF_CF = ?, MO_BLNC_UNSP_GF_CF = ?, TY_ISS_GF_CF = ?, NM_DNR_GF_CF = ?, AD_DNR_GF_CF = ?, CI_DNR_GF_CF = ?, ST_DNR_GF_CF = ?, CC_DNR_GF_CF = ?, PC_DNR_GF_CF = ?, TA_DNR_GF_CF = ?, TL_DNR_GF_CF = ?, FN_DNR_GF_CF = ?, DELETED = ?, GIFT_CONTROL = ?, AUDIT_NOTE = ?, ID_CT = ?, DC_EXPIRATION = ?, LOYALTY_NUM = ? ";
  public static String deleteSql = "delete from DO_CF_GF ";

  public static String TABLE_NAME = "DO_CF_GF";
  public static String COL_ID_STR_ISSG = "DO_CF_GF.ID_STR_ISSG";
  public static String COL_ID_NMB_SRZ_GF_CF = "DO_CF_GF.ID_NMB_SRZ_GF_CF";
  public static String COL_TY_GF_CF = "DO_CF_GF.TY_GF_CF";
  public static String COL_TS_ISS_GF_CF = "DO_CF_GF.TS_ISS_GF_CF";
  public static String COL_DC_EP_GF_CF = "DO_CF_GF.DC_EP_GF_CF";
  public static String COL_MO_VL_FC_GF_CF = "DO_CF_GF.MO_VL_FC_GF_CF";
  public static String COL_MO_BLNC_UNSP_GF_CF = "DO_CF_GF.MO_BLNC_UNSP_GF_CF";
  public static String COL_TY_ISS_GF_CF = "DO_CF_GF.TY_ISS_GF_CF";
  public static String COL_NM_DNR_GF_CF = "DO_CF_GF.NM_DNR_GF_CF";
  public static String COL_AD_DNR_GF_CF = "DO_CF_GF.AD_DNR_GF_CF";
  public static String COL_CI_DNR_GF_CF = "DO_CF_GF.CI_DNR_GF_CF";
  public static String COL_ST_DNR_GF_CF = "DO_CF_GF.ST_DNR_GF_CF";
  public static String COL_CC_DNR_GF_CF = "DO_CF_GF.CC_DNR_GF_CF";
  public static String COL_PC_DNR_GF_CF = "DO_CF_GF.PC_DNR_GF_CF";
  public static String COL_TA_DNR_GF_CF = "DO_CF_GF.TA_DNR_GF_CF";
  public static String COL_TL_DNR_GF_CF = "DO_CF_GF.TL_DNR_GF_CF";
  public static String COL_FN_DNR_GF_CF = "DO_CF_GF.FN_DNR_GF_CF";
  public static String COL_DELETED = "DO_CF_GF.DELETED";
  public static String COL_GIFT_CONTROL = "DO_CF_GF.GIFT_CONTROL";
  public static String COL_AUDIT_NOTE = "DO_CF_GF.AUDIT_NOTE";
  public static String COL_ID_CT = "DO_CF_GF.ID_CT";
  public static String COL_DC_EXPIRATION = "DO_CF_GF.DC_EXPIRATION";
  public static String COL_LOYALTY_NUM = "DO_CF_GF.LOYALTY_NUM";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idStrIssg;
  private String idNmbSrzGfCf;
  private String tyGfCf;
  private Date tsIssGfCf;
  private Date dcEpGfCf;
  private ArmCurrency moVlFcGfCf;
  private ArmCurrency moBlncUnspGfCf;
  private String tyIssGfCf;
  private String nmDnrGfCf;
  private String adDnrGfCf;
  private String ciDnrGfCf;
  private String stDnrGfCf;
  private String ccDnrGfCf;
  private String pcDnrGfCf;
  private String taDnrGfCf;
  private String tlDnrGfCf;
  private String fnDnrGfCf;
  private Boolean deleted;
  private String giftControl;
  private String auditNote;
  private String idCt;
  private Date dcExpiration;
  private String loyaltyNum;

  public String getIdStrIssg() { return this.idStrIssg; }
  public void setIdStrIssg(String idStrIssg) { this.idStrIssg = idStrIssg; }

  public String getIdNmbSrzGfCf() { return this.idNmbSrzGfCf; }
  public void setIdNmbSrzGfCf(String idNmbSrzGfCf) { this.idNmbSrzGfCf = idNmbSrzGfCf; }

  public String getTyGfCf() { return this.tyGfCf; }
  public void setTyGfCf(String tyGfCf) { this.tyGfCf = tyGfCf; }

  public Date getTsIssGfCf() { return this.tsIssGfCf; }
  public void setTsIssGfCf(Date tsIssGfCf) { this.tsIssGfCf = tsIssGfCf; }

  public Date getDcEpGfCf() { return this.dcEpGfCf; }
  public void setDcEpGfCf(Date dcEpGfCf) { this.dcEpGfCf = dcEpGfCf; }

  public ArmCurrency getMoVlFcGfCf() { return this.moVlFcGfCf; }
  public void setMoVlFcGfCf(ArmCurrency moVlFcGfCf) { this.moVlFcGfCf = moVlFcGfCf; }

  public ArmCurrency getMoBlncUnspGfCf() { return this.moBlncUnspGfCf; }
  public void setMoBlncUnspGfCf(ArmCurrency moBlncUnspGfCf) { this.moBlncUnspGfCf = moBlncUnspGfCf; }

  public String getTyIssGfCf() { return this.tyIssGfCf; }
  public void setTyIssGfCf(String tyIssGfCf) { this.tyIssGfCf = tyIssGfCf; }

  public String getNmDnrGfCf() { return this.nmDnrGfCf; }
  public void setNmDnrGfCf(String nmDnrGfCf) { this.nmDnrGfCf = nmDnrGfCf; }

  public String getAdDnrGfCf() { return this.adDnrGfCf; }
  public void setAdDnrGfCf(String adDnrGfCf) { this.adDnrGfCf = adDnrGfCf; }

  public String getCiDnrGfCf() { return this.ciDnrGfCf; }
  public void setCiDnrGfCf(String ciDnrGfCf) { this.ciDnrGfCf = ciDnrGfCf; }

  public String getStDnrGfCf() { return this.stDnrGfCf; }
  public void setStDnrGfCf(String stDnrGfCf) { this.stDnrGfCf = stDnrGfCf; }

  public String getCcDnrGfCf() { return this.ccDnrGfCf; }
  public void setCcDnrGfCf(String ccDnrGfCf) { this.ccDnrGfCf = ccDnrGfCf; }

  public String getPcDnrGfCf() { return this.pcDnrGfCf; }
  public void setPcDnrGfCf(String pcDnrGfCf) { this.pcDnrGfCf = pcDnrGfCf; }

  public String getTaDnrGfCf() { return this.taDnrGfCf; }
  public void setTaDnrGfCf(String taDnrGfCf) { this.taDnrGfCf = taDnrGfCf; }

  public String getTlDnrGfCf() { return this.tlDnrGfCf; }
  public void setTlDnrGfCf(String tlDnrGfCf) { this.tlDnrGfCf = tlDnrGfCf; }

  public String getFnDnrGfCf() { return this.fnDnrGfCf; }
  public void setFnDnrGfCf(String fnDnrGfCf) { this.fnDnrGfCf = fnDnrGfCf; }

  public Boolean getDeleted() { return this.deleted; }
  public void setDeleted(Boolean deleted) { this.deleted = deleted; }
  public void setDeleted(boolean deleted) { this.deleted = new Boolean(deleted); }

  public String getGiftControl() { return this.giftControl; }
  public void setGiftControl(String giftControl) { this.giftControl = giftControl; }

  public String getAuditNote() { return this.auditNote; }
  public void setAuditNote(String auditNote) { this.auditNote = auditNote; }

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public Date getDcExpiration() { return this.dcExpiration; }
  public void setDcExpiration(Date dcExpiration) { this.dcExpiration = dcExpiration; }

  public String getLoyaltyNum() { return this.loyaltyNum; }
  public void setLoyaltyNum(String loyaltyNum) { this.loyaltyNum = loyaltyNum; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      DoCfGfOracleBean bean = new DoCfGfOracleBean();
      bean.idStrIssg = getStringFromResultSet(rs, "ID_STR_ISSG");
      bean.idNmbSrzGfCf = getStringFromResultSet(rs, "ID_NMB_SRZ_GF_CF");
      bean.tyGfCf = getStringFromResultSet(rs, "TY_GF_CF");
      bean.tsIssGfCf = getDateFromResultSet(rs, "TS_ISS_GF_CF");
      bean.dcEpGfCf = getDateFromResultSet(rs, "DC_EP_GF_CF");
      bean.moVlFcGfCf = getCurrencyFromResultSet(rs, "MO_VL_FC_GF_CF");
      bean.moBlncUnspGfCf = getCurrencyFromResultSet(rs, "MO_BLNC_UNSP_GF_CF");
      bean.tyIssGfCf = getStringFromResultSet(rs, "TY_ISS_GF_CF");
      bean.nmDnrGfCf = getStringFromResultSet(rs, "NM_DNR_GF_CF");
      bean.adDnrGfCf = getStringFromResultSet(rs, "AD_DNR_GF_CF");
      bean.ciDnrGfCf = getStringFromResultSet(rs, "CI_DNR_GF_CF");
      bean.stDnrGfCf = getStringFromResultSet(rs, "ST_DNR_GF_CF");
      bean.ccDnrGfCf = getStringFromResultSet(rs, "CC_DNR_GF_CF");
      bean.pcDnrGfCf = getStringFromResultSet(rs, "PC_DNR_GF_CF");
      bean.taDnrGfCf = getStringFromResultSet(rs, "TA_DNR_GF_CF");
      bean.tlDnrGfCf = getStringFromResultSet(rs, "TL_DNR_GF_CF");
      bean.fnDnrGfCf = getStringFromResultSet(rs, "FN_DNR_GF_CF");
      bean.deleted = getBooleanFromResultSet(rs, "DELETED");
      bean.giftControl = getStringFromResultSet(rs, "GIFT_CONTROL");
      bean.auditNote = getStringFromResultSet(rs, "AUDIT_NOTE");
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.dcExpiration = getDateFromResultSet(rs, "DC_EXPIRATION");
      bean.loyaltyNum = getStringFromResultSet(rs, "LOYALTY_NUM");
      list.add(bean);
    }
    return (DoCfGfOracleBean[]) list.toArray(new DoCfGfOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdStrIssg(), Types.VARCHAR);
    addToList(list, this.getIdNmbSrzGfCf(), Types.VARCHAR);
    addToList(list, this.getTyGfCf(), Types.VARCHAR);
    addToList(list, this.getTsIssGfCf(), Types.TIMESTAMP);
    addToList(list, this.getDcEpGfCf(), Types.TIMESTAMP);
    addToList(list, this.getMoVlFcGfCf(), Types.VARCHAR);
    addToList(list, this.getMoBlncUnspGfCf(), Types.VARCHAR);
    addToList(list, this.getTyIssGfCf(), Types.VARCHAR);
    addToList(list, this.getNmDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getAdDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getCiDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getStDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getCcDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getPcDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getTaDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getTlDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getFnDnrGfCf(), Types.VARCHAR);
    addToList(list, this.getDeleted(), Types.DECIMAL);
    addToList(list, this.getGiftControl(), Types.VARCHAR);
    addToList(list, this.getAuditNote(), Types.VARCHAR);
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getDcExpiration(), Types.TIMESTAMP);
    addToList(list, this.getLoyaltyNum(), Types.VARCHAR);
    return list;
  }

}
