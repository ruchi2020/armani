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
 * This class is an object representation of the Arts database table ARM_ADS_PH<BR>
 * Followings are the column of the table: <BR>
 *     ID_ADS -- VARCHAR2(128)<BR>
 *     ID_PH_TYP -- VARCHAR2(20)<BR>
 *     TL_PH -- VARCHAR2(15)<BR>
 *     CC_PH -- VARCHAR2(10)<BR>
 *     TA_PH -- VARCHAR2(10)<BR>
 *     PH_EXTN -- VARCHAR2(10)<BR>
 *
 */
public class ArmAdsPhOracleBean extends BaseOracleBean {

  public ArmAdsPhOracleBean() {}

  public static String selectSql = "select ID_ADS, ID_PH_TYP, TL_PH, CC_PH, TA_PH, PH_EXTN from ARM_ADS_PH ";
  public static String insertSql = "insert into ARM_ADS_PH (ID_ADS, ID_PH_TYP, TL_PH, CC_PH, TA_PH, PH_EXTN) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_ADS_PH set ID_ADS = ?, ID_PH_TYP = ?, TL_PH = ?, CC_PH = ?, TA_PH = ?, PH_EXTN = ? ";
  public static String deleteSql = "delete from ARM_ADS_PH ";

  public static String TABLE_NAME = "ARM_ADS_PH";
  public static String COL_ID_ADS = "ARM_ADS_PH.ID_ADS";
  public static String COL_ID_PH_TYP = "ARM_ADS_PH.ID_PH_TYP";
  public static String COL_TL_PH = "ARM_ADS_PH.TL_PH";
  public static String COL_CC_PH = "ARM_ADS_PH.CC_PH";
  public static String COL_TA_PH = "ARM_ADS_PH.TA_PH";
  public static String COL_PH_EXTN = "ARM_ADS_PH.PH_EXTN";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idAds;
  private String idPhTyp;
  private String tlPh;
  private String ccPh;
  private String taPh;
  private String phExtn;

  public String getIdAds() { return this.idAds; }
  public void setIdAds(String idAds) { this.idAds = idAds; }

  public String getIdPhTyp() { return this.idPhTyp; }
  public void setIdPhTyp(String idPhTyp) { this.idPhTyp = idPhTyp; }

  public String getTlPh() { return this.tlPh; }
  public void setTlPh(String tlPh) { this.tlPh = tlPh; }

  public String getCcPh() { return this.ccPh; }
  public void setCcPh(String ccPh) { this.ccPh = ccPh; }

  public String getTaPh() { return this.taPh; }
  public void setTaPh(String taPh) { this.taPh = taPh; }

  public String getPhExtn() { return this.phExtn; }
  public void setPhExtn(String phExtn) { this.phExtn = phExtn; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmAdsPhOracleBean bean = new ArmAdsPhOracleBean();
      bean.idAds = getStringFromResultSet(rs, "ID_ADS");
      bean.idPhTyp = getStringFromResultSet(rs, "ID_PH_TYP");
      bean.tlPh = getStringFromResultSet(rs, "TL_PH");
      bean.ccPh = getStringFromResultSet(rs, "CC_PH");
      bean.taPh = getStringFromResultSet(rs, "TA_PH");
      bean.phExtn = getStringFromResultSet(rs, "PH_EXTN");
      list.add(bean);
    }
    return (ArmAdsPhOracleBean[]) list.toArray(new ArmAdsPhOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdAds(), Types.VARCHAR);
    addToList(list, this.getIdPhTyp(), Types.VARCHAR);
    addToList(list, this.getTlPh(), Types.VARCHAR);
    addToList(list, this.getCcPh(), Types.VARCHAR);
    addToList(list, this.getTaPh(), Types.VARCHAR);
    addToList(list, this.getPhExtn(), Types.VARCHAR);
    return list;
  }

}
