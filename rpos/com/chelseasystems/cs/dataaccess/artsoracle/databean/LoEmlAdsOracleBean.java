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
 * This class is an object representation of the Arts database table LO_EML_ADS<BR>
 * Followings are the column of the table: <BR>
 *     EM_ADS(PK) -- VARCHAR2(64)<BR>
 *     ID_PRTY(PK) -- VARCHAR2(128)<BR>
 *     TY_EML_ADS -- VARCHAR2(10)<BR>
 *
 */
public class LoEmlAdsOracleBean extends BaseOracleBean {

  public LoEmlAdsOracleBean() {}

  public static String selectSql = "select EM_ADS, ID_PRTY, TY_EML_ADS from LO_EML_ADS ";
  public static String insertSql = "insert into LO_EML_ADS (EM_ADS, ID_PRTY, TY_EML_ADS) values (?, ?, ?)";
  public static String updateSql = "update LO_EML_ADS set EM_ADS = ?, ID_PRTY = ?, TY_EML_ADS = ? ";
  public static String deleteSql = "delete from LO_EML_ADS ";

  public static String TABLE_NAME = "LO_EML_ADS";
  public static String COL_EM_ADS = "LO_EML_ADS.EM_ADS";
  public static String COL_ID_PRTY = "LO_EML_ADS.ID_PRTY";
  public static String COL_TY_EML_ADS = "LO_EML_ADS.TY_EML_ADS";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String emAds;
  private String idPrty;
  private String tyEmlAds;

  public String getEmAds() { return this.emAds; }
  public void setEmAds(String emAds) { this.emAds = emAds; }

  public String getIdPrty() { return this.idPrty; }
  public void setIdPrty(String idPrty) { this.idPrty = idPrty; }

  public String getTyEmlAds() { return this.tyEmlAds; }
  public void setTyEmlAds(String tyEmlAds) { this.tyEmlAds = tyEmlAds; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      LoEmlAdsOracleBean bean = new LoEmlAdsOracleBean();
      bean.emAds = getStringFromResultSet(rs, "EM_ADS");
      bean.idPrty = getStringFromResultSet(rs, "ID_PRTY");
      bean.tyEmlAds = getStringFromResultSet(rs, "TY_EML_ADS");
      list.add(bean);
    }
    return (LoEmlAdsOracleBean[]) list.toArray(new LoEmlAdsOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getEmAds(), Types.VARCHAR);
    addToList(list, this.getIdPrty(), Types.VARCHAR);
    addToList(list, this.getTyEmlAds(), Types.VARCHAR);
    return list;
  }

}
