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
 * This class is an object representation of the Arts database table LO_ADS_PRTY<BR>
 * Followings are the column of the table: <BR>
 *     ID_ADS(PK) -- VARCHAR2(128)<BR>
 *     ID_PRTY(PK) -- VARCHAR2(128)<BR>
 *     SC_PRTY_ADS -- VARCHAR2(20)<BR>
 *     TY_RO_PRTY(PK) -- VARCHAR2(60)<BR>
 *     DC_EF -- DATE(7)<BR>
 *     DC_EP -- DATE(7)<BR>
 *     TY_ADS -- VARCHAR2(128)<BR>
 *
 */
public class LoAdsPrtyOracleBean extends BaseOracleBean {

  public LoAdsPrtyOracleBean() {}

  public static String selectSql = "select ID_ADS, ID_PRTY, SC_PRTY_ADS, TY_RO_PRTY, DC_EF, DC_EP, TY_ADS from LO_ADS_PRTY ";
  public static String insertSql = "insert into LO_ADS_PRTY (ID_ADS, ID_PRTY, SC_PRTY_ADS, TY_RO_PRTY, DC_EF, DC_EP, TY_ADS) values (?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update LO_ADS_PRTY set ID_ADS = ?, ID_PRTY = ?, SC_PRTY_ADS = ?, TY_RO_PRTY = ?, DC_EF = ?, DC_EP = ?, TY_ADS = ? ";
  public static String deleteSql = "delete from LO_ADS_PRTY ";

  public static String TABLE_NAME = "LO_ADS_PRTY";
  public static String COL_ID_ADS = "LO_ADS_PRTY.ID_ADS";
  public static String COL_ID_PRTY = "LO_ADS_PRTY.ID_PRTY";
  public static String COL_SC_PRTY_ADS = "LO_ADS_PRTY.SC_PRTY_ADS";
  public static String COL_TY_RO_PRTY = "LO_ADS_PRTY.TY_RO_PRTY";
  public static String COL_DC_EF = "LO_ADS_PRTY.DC_EF";
  public static String COL_DC_EP = "LO_ADS_PRTY.DC_EP";
  public static String COL_TY_ADS = "LO_ADS_PRTY.TY_ADS";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idAds;
  private String idPrty;
  private String scPrtyAds;
  private String tyRoPrty;
  private Date dcEf;
  private Date dcEp;
  private String tyAds;

  public String getIdAds() { return this.idAds; }
  public void setIdAds(String idAds) { this.idAds = idAds; }

  public String getIdPrty() { return this.idPrty; }
  public void setIdPrty(String idPrty) { this.idPrty = idPrty; }

  public String getScPrtyAds() { return this.scPrtyAds; }
  public void setScPrtyAds(String scPrtyAds) { this.scPrtyAds = scPrtyAds; }

  public String getTyRoPrty() { return this.tyRoPrty; }
  public void setTyRoPrty(String tyRoPrty) { this.tyRoPrty = tyRoPrty; }

  public Date getDcEf() { return this.dcEf; }
  public void setDcEf(Date dcEf) { this.dcEf = dcEf; }

  public Date getDcEp() { return this.dcEp; }
  public void setDcEp(Date dcEp) { this.dcEp = dcEp; }

  public String getTyAds() { return this.tyAds; }
  public void setTyAds(String tyAds) { this.tyAds = tyAds; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      LoAdsPrtyOracleBean bean = new LoAdsPrtyOracleBean();
      bean.idAds = getStringFromResultSet(rs, "ID_ADS");
      bean.idPrty = getStringFromResultSet(rs, "ID_PRTY");
      bean.scPrtyAds = getStringFromResultSet(rs, "SC_PRTY_ADS");
      bean.tyRoPrty = getStringFromResultSet(rs, "TY_RO_PRTY");
      bean.dcEf = getDateFromResultSet(rs, "DC_EF");
      bean.dcEp = getDateFromResultSet(rs, "DC_EP");
      bean.tyAds = getStringFromResultSet(rs, "TY_ADS");
      list.add(bean);
    }
    return (LoAdsPrtyOracleBean[]) list.toArray(new LoAdsPrtyOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdAds(), Types.VARCHAR);
    addToList(list, this.getIdPrty(), Types.VARCHAR);
    addToList(list, this.getScPrtyAds(), Types.VARCHAR);
    addToList(list, this.getTyRoPrty(), Types.VARCHAR);
    addToList(list, this.getDcEf(), Types.TIMESTAMP);
    addToList(list, this.getDcEp(), Types.TIMESTAMP);
    addToList(list, this.getTyAds(), Types.VARCHAR);
    return list;
  }

}
