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
 * This class is an object representation of the Arts database table PA_EM<BR>
 * Followings are the column of the table: <BR>
 *     ID_EM(PK) -- VARCHAR2(128)<BR>
 *     ID_PLN_EM_CMN -- VARCHAR2(20)<BR>
 *     NM_EM -- VARCHAR2(40)<BR>
 *     UN_NMB_SCL_SCTY -- CHAR(9)<BR>
 *     TY_EM_CMPSN -- VARCHAR2(20)<BR>
 *     ID_PRTY -- VARCHAR2(128)<BR>
 *     TY_RO_PRTY -- VARCHAR2(60)<BR>
 *     HI_LV_VCTN -- NUMBER(9.2)<BR>
 *     IT_ACCESS_SCRTY -- NUMBER(22)<BR>
 *     QY_LV_SCK -- NUMBER(9.2)<BR>
 *     SC_EM -- VARCHAR2(20)<BR>
 *     ARM_EXTERNAL_ID -- VARCHAR2(30)<BR>
 *     ID_CT -- VARCHAR2(128)<BR>
 *
 */
public class PaEmOracleBean extends BaseOracleBean {

  public PaEmOracleBean() {}

  public static String selectSql = "select ID_EM, ID_PLN_EM_CMN, NM_EM, UN_NMB_SCL_SCTY, TY_EM_CMPSN, ID_PRTY, TY_RO_PRTY, HI_LV_VCTN, IT_ACCESS_SCRTY, QY_LV_SCK, SC_EM, ARM_EXTERNAL_ID, ID_CT from PA_EM ";
  public static String insertSql = "insert into PA_EM (ID_EM, ID_PLN_EM_CMN, NM_EM, UN_NMB_SCL_SCTY, TY_EM_CMPSN, ID_PRTY, TY_RO_PRTY, HI_LV_VCTN, IT_ACCESS_SCRTY, QY_LV_SCK, SC_EM, ARM_EXTERNAL_ID, ID_CT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update PA_EM set ID_EM = ?, ID_PLN_EM_CMN = ?, NM_EM = ?, UN_NMB_SCL_SCTY = ?, TY_EM_CMPSN = ?, ID_PRTY = ?, TY_RO_PRTY = ?, HI_LV_VCTN = ?, IT_ACCESS_SCRTY = ?, QY_LV_SCK = ?, SC_EM = ?, ARM_EXTERNAL_ID = ?, ID_CT = ? ";
  public static String deleteSql = "delete from PA_EM ";

  public static String TABLE_NAME = "PA_EM";
  public static String COL_ID_EM = "PA_EM.ID_EM";
  public static String COL_ID_PLN_EM_CMN = "PA_EM.ID_PLN_EM_CMN";
  public static String COL_NM_EM = "PA_EM.NM_EM";
  public static String COL_UN_NMB_SCL_SCTY = "PA_EM.UN_NMB_SCL_SCTY";
  public static String COL_TY_EM_CMPSN = "PA_EM.TY_EM_CMPSN";
  public static String COL_ID_PRTY = "PA_EM.ID_PRTY";
  public static String COL_TY_RO_PRTY = "PA_EM.TY_RO_PRTY";
  public static String COL_HI_LV_VCTN = "PA_EM.HI_LV_VCTN";
  public static String COL_IT_ACCESS_SCRTY = "PA_EM.IT_ACCESS_SCRTY";
  public static String COL_QY_LV_SCK = "PA_EM.QY_LV_SCK";
  public static String COL_SC_EM = "PA_EM.SC_EM";
  public static String COL_ARM_EXTERNAL_ID = "PA_EM.ARM_EXTERNAL_ID";
  public static String COL_ID_CT = "PA_EM.ID_CT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idEm;
  private String idPlnEmCmn;
  private String nmEm;
  private String unNmbSclScty;
  private String tyEmCmpsn;
  private String idPrty;
  private String tyRoPrty;
  private Double hiLvVctn;
  private Long itAccessScrty;
  private Double qyLvSck;
  private String scEm;
  private String armExternalId;
  private String idCt;

  public String getIdEm() { return this.idEm; }
  public void setIdEm(String idEm) { this.idEm = idEm; }

  public String getIdPlnEmCmn() { return this.idPlnEmCmn; }
  public void setIdPlnEmCmn(String idPlnEmCmn) { this.idPlnEmCmn = idPlnEmCmn; }

  public String getNmEm() { return this.nmEm; }
  public void setNmEm(String nmEm) { this.nmEm = nmEm; }

  public String getUnNmbSclScty() { return this.unNmbSclScty; }
  public void setUnNmbSclScty(String unNmbSclScty) { this.unNmbSclScty = unNmbSclScty; }

  public String getTyEmCmpsn() { return this.tyEmCmpsn; }
  public void setTyEmCmpsn(String tyEmCmpsn) { this.tyEmCmpsn = tyEmCmpsn; }

  public String getIdPrty() { return this.idPrty; }
  public void setIdPrty(String idPrty) { this.idPrty = idPrty; }

  public String getTyRoPrty() { return this.tyRoPrty; }
  public void setTyRoPrty(String tyRoPrty) { this.tyRoPrty = tyRoPrty; }

  public Double getHiLvVctn() { return this.hiLvVctn; }
  public void setHiLvVctn(Double hiLvVctn) { this.hiLvVctn = hiLvVctn; }
  public void setHiLvVctn(double hiLvVctn) { this.hiLvVctn = new Double(hiLvVctn); }

  public Long getItAccessScrty() { return this.itAccessScrty; }
  public void setItAccessScrty(Long itAccessScrty) { this.itAccessScrty = itAccessScrty; }
  public void setItAccessScrty(long itAccessScrty) { this.itAccessScrty = new Long(itAccessScrty); }
  public void setItAccessScrty(int itAccessScrty) { this.itAccessScrty = new Long((long)itAccessScrty); }

  public Double getQyLvSck() { return this.qyLvSck; }
  public void setQyLvSck(Double qyLvSck) { this.qyLvSck = qyLvSck; }
  public void setQyLvSck(double qyLvSck) { this.qyLvSck = new Double(qyLvSck); }

  public String getScEm() { return this.scEm; }
  public void setScEm(String scEm) { this.scEm = scEm; }

  public String getArmExternalId() { return this.armExternalId; }
  public void setArmExternalId(String armExternalId) { this.armExternalId = armExternalId; }

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      PaEmOracleBean bean = new PaEmOracleBean();
      bean.idEm = getStringFromResultSet(rs, "ID_EM");
      bean.idPlnEmCmn = getStringFromResultSet(rs, "ID_PLN_EM_CMN");
      bean.nmEm = getStringFromResultSet(rs, "NM_EM");
      bean.unNmbSclScty = getStringFromResultSet(rs, "UN_NMB_SCL_SCTY");
      bean.tyEmCmpsn = getStringFromResultSet(rs, "TY_EM_CMPSN");
      bean.idPrty = getStringFromResultSet(rs, "ID_PRTY");
      bean.tyRoPrty = getStringFromResultSet(rs, "TY_RO_PRTY");
      bean.hiLvVctn = getDoubleFromResultSet(rs, "HI_LV_VCTN");
      bean.itAccessScrty = getLongFromResultSet(rs, "IT_ACCESS_SCRTY");
      bean.qyLvSck = getDoubleFromResultSet(rs, "QY_LV_SCK");
      bean.scEm = getStringFromResultSet(rs, "SC_EM");
      bean.armExternalId = getStringFromResultSet(rs, "ARM_EXTERNAL_ID");
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      list.add(bean);
    }
    return (PaEmOracleBean[]) list.toArray(new PaEmOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdEm(), Types.VARCHAR);
    addToList(list, this.getIdPlnEmCmn(), Types.VARCHAR);
    addToList(list, this.getNmEm(), Types.VARCHAR);
    addToList(list, this.getUnNmbSclScty(), Types.VARCHAR);
    addToList(list, this.getTyEmCmpsn(), Types.VARCHAR);
    addToList(list, this.getIdPrty(), Types.VARCHAR);
    addToList(list, this.getTyRoPrty(), Types.VARCHAR);
    addToList(list, this.getHiLvVctn(), Types.DECIMAL);
    addToList(list, this.getItAccessScrty(), Types.DECIMAL);
    addToList(list, this.getQyLvSck(), Types.DECIMAL);
    addToList(list, this.getScEm(), Types.VARCHAR);
    addToList(list, this.getArmExternalId(), Types.VARCHAR);
    addToList(list, this.getIdCt(), Types.VARCHAR);
    return list;
  }

}
