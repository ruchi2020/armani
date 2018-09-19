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
 * This class is an object representation of the Arts database table LO_ADS_NSTD<BR>
 * Followings are the column of the table: <BR>
 *     ID_ADS(PK) -- VARCHAR2(128)<BR>
 *     A1_ADS -- VARCHAR2(40)<BR>
 *     A2_ADS -- VARCHAR2(40)<BR>
 *     NM_UN -- VARCHAR2(40)<BR>
 *     TE_NM -- VARCHAR2(40)<BR>
 *     CO_NM -- VARCHAR2(40)<BR>
 *     PC_NM -- VARCHAR2(15)<BR>
 *     MU_NM -- VARCHAR2(20)<BR>
 *     FL_PRMY_ADS -- VARCHAR2(1)<BR>
 *     ADS_FORMAT -- VARCHAR2(40)<BR>
 *
 */
public class LoAdsNstdOracleBean extends BaseOracleBean {

  public LoAdsNstdOracleBean() {}

  public static String selectSql = "select ID_ADS, A1_ADS, A2_ADS, NM_UN, TE_NM, CO_NM, PC_NM, MU_NM, FL_PRMY_ADS, ADS_FORMAT from LO_ADS_NSTD ";
  public static String insertSql = "insert into LO_ADS_NSTD (ID_ADS, A1_ADS, A2_ADS, NM_UN, TE_NM, CO_NM, PC_NM, MU_NM, FL_PRMY_ADS, ADS_FORMAT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update LO_ADS_NSTD set ID_ADS = ?, A1_ADS = ?, A2_ADS = ?, NM_UN = ?, TE_NM = ?, CO_NM = ?, PC_NM = ?, MU_NM = ?, FL_PRMY_ADS = ?, ADS_FORMAT = ? ";
  public static String deleteSql = "delete from LO_ADS_NSTD ";

  public static String TABLE_NAME = "LO_ADS_NSTD";
  public static String COL_ID_ADS = "LO_ADS_NSTD.ID_ADS";
  public static String COL_A1_ADS = "LO_ADS_NSTD.A1_ADS";
  public static String COL_A2_ADS = "LO_ADS_NSTD.A2_ADS";
  public static String COL_NM_UN = "LO_ADS_NSTD.NM_UN";
  public static String COL_TE_NM = "LO_ADS_NSTD.TE_NM";
  public static String COL_CO_NM = "LO_ADS_NSTD.CO_NM";
  public static String COL_PC_NM = "LO_ADS_NSTD.PC_NM";
  public static String COL_MU_NM = "LO_ADS_NSTD.MU_NM";
  public static String COL_FL_PRMY_ADS = "LO_ADS_NSTD.FL_PRMY_ADS";
  public static String COL_ADS_FORMAT = "LO_ADS_NSTD.ADS_FORMAT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idAds;
  private String a1Ads;
  private String a2Ads;
  private String nmUn;
  private String teNm;
  private String coNm;
  private String pcNm;
  private String muNm;
  private Boolean flPrmyAds;
  private String adsFormat;

  public String getIdAds() { return this.idAds; }
  public void setIdAds(String idAds) { this.idAds = idAds; }

  public String getA1Ads() { return this.a1Ads; }
  public void setA1Ads(String a1Ads) { this.a1Ads = a1Ads; }

  public String getA2Ads() { return this.a2Ads; }
  public void setA2Ads(String a2Ads) { this.a2Ads = a2Ads; }

  public String getNmUn() { return this.nmUn; }
  public void setNmUn(String nmUn) { this.nmUn = nmUn; }

  public String getTeNm() { return this.teNm; }
  public void setTeNm(String teNm) { this.teNm = teNm; }

  public String getCoNm() { return this.coNm; }
  public void setCoNm(String coNm) { this.coNm = coNm; }

  public String getPcNm() { return this.pcNm; }
  public void setPcNm(String pcNm) { this.pcNm = pcNm; }

  public String getMuNm() { return this.muNm; }
  public void setMuNm(String muNm) { this.muNm = muNm; }

  public Boolean getFlPrmyAds() { return this.flPrmyAds; }
  public void setFlPrmyAds(Boolean flPrmyAds) { this.flPrmyAds = flPrmyAds; }
  public void setFlPrmyAds(boolean flPrmyAds) { this.flPrmyAds = new Boolean(flPrmyAds); }

  public String getAdsFormat() { return this.adsFormat; }
  public void setAdsFormat(String adsFormat) { this.adsFormat = adsFormat; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      LoAdsNstdOracleBean bean = new LoAdsNstdOracleBean();
      bean.idAds = getStringFromResultSet(rs, "ID_ADS");
      bean.a1Ads = getStringFromResultSet(rs, "A1_ADS");
      bean.a2Ads = getStringFromResultSet(rs, "A2_ADS");
      bean.nmUn = getStringFromResultSet(rs, "NM_UN");
      bean.teNm = getStringFromResultSet(rs, "TE_NM");
      bean.coNm = getStringFromResultSet(rs, "CO_NM");
      bean.pcNm = getStringFromResultSet(rs, "PC_NM");
      bean.muNm = getStringFromResultSet(rs, "MU_NM");
      bean.flPrmyAds = getBooleanFromResultSet(rs, "FL_PRMY_ADS");
      bean.adsFormat = getStringFromResultSet(rs, "ADS_FORMAT");
      list.add(bean);
    }
    return (LoAdsNstdOracleBean[]) list.toArray(new LoAdsNstdOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdAds(), Types.VARCHAR);
    addToList(list, this.getA1Ads(), Types.VARCHAR);
    addToList(list, this.getA2Ads(), Types.VARCHAR);
    addToList(list, this.getNmUn(), Types.VARCHAR);
    addToList(list, this.getTeNm(), Types.VARCHAR);
    addToList(list, this.getCoNm(), Types.VARCHAR);
    addToList(list, this.getPcNm(), Types.VARCHAR);
    addToList(list, this.getMuNm(), Types.VARCHAR);
    addToList(list, this.getFlPrmyAds(), Types.DECIMAL);
    addToList(list, this.getAdsFormat(), Types.VARCHAR);
    return list;
  }

}
