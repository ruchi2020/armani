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
 * This class is an object representation of the Arts database table RU_PRDV<BR>
 * Followings are the column of the table: <BR>
 *     ID_RU_PRDV(PK) -- VARCHAR2(128)<BR>
 *     NM_RU_PRDV -- VARCHAR2(40)<BR>
 *     DE_RU_PRDV -- VARCHAR2(255)<BR>
 *     LU_CBRK_PRDV_TRN -- CHAR(2)<BR>
 *     SC_RU_PRDV -- CHAR(2)<BR>
 *     TY_RU_PRDV -- CHAR(2)<BR>
 *     APPLICABLE_ITM -- VARCHAR2(1)<BR>
 *     IS_DISCOUNT -- CHAR(1)<BR>
 *
 */
public class RuPrdvOracleBean extends BaseOracleBean {

  public RuPrdvOracleBean() {}

  public static String selectSql = "select ID_RU_PRDV, NM_RU_PRDV, DE_RU_PRDV, LU_CBRK_PRDV_TRN, SC_RU_PRDV, TY_RU_PRDV, APPLICABLE_ITM, IS_DISCOUNT from RU_PRDV ";
  public static String insertSql = "insert into RU_PRDV (ID_RU_PRDV, NM_RU_PRDV, DE_RU_PRDV, LU_CBRK_PRDV_TRN, SC_RU_PRDV, TY_RU_PRDV, APPLICABLE_ITM, IS_DISCOUNT) values (?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RU_PRDV set ID_RU_PRDV = ?, NM_RU_PRDV = ?, DE_RU_PRDV = ?, LU_CBRK_PRDV_TRN = ?, SC_RU_PRDV = ?, TY_RU_PRDV = ?, APPLICABLE_ITM = ?, IS_DISCOUNT = ? ";
  public static String deleteSql = "delete from RU_PRDV ";

  public static String TABLE_NAME = "RU_PRDV";
  public static String COL_ID_RU_PRDV = "RU_PRDV.ID_RU_PRDV";
  public static String COL_NM_RU_PRDV = "RU_PRDV.NM_RU_PRDV";
  public static String COL_DE_RU_PRDV = "RU_PRDV.DE_RU_PRDV";
  public static String COL_LU_CBRK_PRDV_TRN = "RU_PRDV.LU_CBRK_PRDV_TRN";
  public static String COL_SC_RU_PRDV = "RU_PRDV.SC_RU_PRDV";
  public static String COL_TY_RU_PRDV = "RU_PRDV.TY_RU_PRDV";
  public static String COL_APPLICABLE_ITM = "RU_PRDV.APPLICABLE_ITM";
  public static String COL_IS_DISCOUNT = "RU_PRDV.IS_DISCOUNT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idRuPrdv;
  private String nmRuPrdv;
  private String deRuPrdv;
  private String luCbrkPrdvTrn;
  private String scRuPrdv;
  private String tyRuPrdv;
  private String applicableItm;
  private Boolean isDiscount;

  public String getIdRuPrdv() { return this.idRuPrdv; }
  public void setIdRuPrdv(String idRuPrdv) { this.idRuPrdv = idRuPrdv; }

  public String getNmRuPrdv() { return this.nmRuPrdv; }
  public void setNmRuPrdv(String nmRuPrdv) { this.nmRuPrdv = nmRuPrdv; }

  public String getDeRuPrdv() { return this.deRuPrdv; }
  public void setDeRuPrdv(String deRuPrdv) { this.deRuPrdv = deRuPrdv; }

  public String getLuCbrkPrdvTrn() { return this.luCbrkPrdvTrn; }
  public void setLuCbrkPrdvTrn(String luCbrkPrdvTrn) { this.luCbrkPrdvTrn = luCbrkPrdvTrn; }

  public String getScRuPrdv() { return this.scRuPrdv; }
  public void setScRuPrdv(String scRuPrdv) { this.scRuPrdv = scRuPrdv; }

  public String getTyRuPrdv() { return this.tyRuPrdv; }
  public void setTyRuPrdv(String tyRuPrdv) { this.tyRuPrdv = tyRuPrdv; }

  public String getApplicableItm() { return this.applicableItm; }
  public void setApplicableItm(String applicableItm) { this.applicableItm = applicableItm; }

  public Boolean getIsDiscount() { return this.isDiscount; }
  public void setIsDiscount(Boolean isDiscount) { this.isDiscount = isDiscount; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RuPrdvOracleBean bean = new RuPrdvOracleBean();
      bean.idRuPrdv = getStringFromResultSet(rs, "ID_RU_PRDV");
      bean.nmRuPrdv = getStringFromResultSet(rs, "NM_RU_PRDV");
      bean.deRuPrdv = getStringFromResultSet(rs, "DE_RU_PRDV");
      bean.luCbrkPrdvTrn = getStringFromResultSet(rs, "LU_CBRK_PRDV_TRN");
      bean.scRuPrdv = getStringFromResultSet(rs, "SC_RU_PRDV");
      bean.tyRuPrdv = getStringFromResultSet(rs, "TY_RU_PRDV");
      bean.applicableItm = getStringFromResultSet(rs, "APPLICABLE_ITM");
      bean.isDiscount = getBooleanFromResultSet(rs, "IS_DISCOUNT");
      list.add(bean);
    }
    return (RuPrdvOracleBean[]) list.toArray(new RuPrdvOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdRuPrdv(), Types.VARCHAR);
    addToList(list, this.getNmRuPrdv(), Types.VARCHAR);
    addToList(list, this.getDeRuPrdv(), Types.VARCHAR);
    addToList(list, this.getLuCbrkPrdvTrn(), Types.VARCHAR);
    addToList(list, this.getScRuPrdv(), Types.VARCHAR);
    addToList(list, this.getTyRuPrdv(), Types.VARCHAR);
    addToList(list, this.getApplicableItm(), Types.VARCHAR);
    addToList(list, this.getIsDiscount(), Types.VARCHAR);
    return list;
  }

}
