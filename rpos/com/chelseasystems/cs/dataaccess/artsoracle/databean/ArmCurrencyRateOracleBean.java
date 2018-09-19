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
 * This class is an object representation of the Arts database table ARM_CURRENCY_RATE<BR>
 * Followings are the column of the table: <BR>
 *     ID_CNY_FROM(PK) -- VARCHAR2(10)<BR>
 *     ID_CNY_TO(PK) -- VARCHAR2(10)<BR>
 *     MO_RT_TO_BUY -- NUMBER(22)<BR>
 *     UPDATE_DT -- DATE(7)<BR>
 *     ED_CO -- VARCHAR2(10)<BR>
 *     ED_LA -- VARCHAR2(10)<BR>
 *     CD_FIN -- VARCHAR2(10)<BR>
 *
 */
public class ArmCurrencyRateOracleBean extends BaseOracleBean {
  /**
   *
   */
  public ArmCurrencyRateOracleBean() {}

  public static String selectSql = "select ID_CNY_FROM, ID_CNY_TO, MO_RT_TO_BUY, UPDATE_DT, ED_CO, ED_LA, CD_FIN from ARM_CURRENCY_RATE ";
  public static String insertSql = "insert into ARM_CURRENCY_RATE (ID_CNY_FROM, ID_CNY_TO, MO_RT_TO_BUY, UPDATE_DT, ED_CO, ED_LA, CD_FIN) values (?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CURRENCY_RATE set ID_CNY_FROM = ?, ID_CNY_TO = ?, MO_RT_TO_BUY = ?, UPDATE_DT = ?, ED_CO = ?, ED_LA = ?, CD_FIN = ? ";
  public static String deleteSql = "delete from ARM_CURRENCY_RATE ";

  public static String TABLE_NAME = "ARM_CURRENCY_RATE";
  public static String COL_ID_CNY_FROM = "ARM_CURRENCY_RATE.ID_CNY_FROM";
  public static String COL_ID_CNY_TO = "ARM_CURRENCY_RATE.ID_CNY_TO";
  public static String COL_MO_RT_TO_BUY = "ARM_CURRENCY_RATE.MO_RT_TO_BUY";
  public static String COL_UPDATE_DT = "ARM_CURRENCY_RATE.UPDATE_DT";
  public static String COL_ED_CO = "ARM_CURRENCY_RATE.ED_CO";
  public static String COL_ED_LA = "ARM_CURRENCY_RATE.ED_LA";
  public static String COL_CD_FIN = "ARM_CURRENCY_RATE.CD_FIN";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCnyFrom;
  private String idCnyTo;
  private Double moRtToBuy;
  private Date updateDt;
  private String edCo;
  private String edLa;
  private String cdFin;

  public String getIdCnyFrom() { return this.idCnyFrom; }

  /**
   *
   * @param idCnyFrom String
   */
  public void setIdCnyFrom(String idCnyFrom) { this.idCnyFrom = idCnyFrom; }

  /**
   *
   * @return String
   */
  public String getIdCnyTo() { return this.idCnyTo; }
  /**
   *
   * @param idCnyTo String
   */
  public void setIdCnyTo(String idCnyTo) { this.idCnyTo = idCnyTo; }

  /**
   *
   * @return Double
   */
  public Double getMoRtToBuy() { return this.moRtToBuy; }

  /**
   *
   * @param moRtToBuy Double
   */
  public void setMoRtToBuy(Double moRtToBuy) { this.moRtToBuy = moRtToBuy; }
  /**
   *
   * @param moRtToBuy double
   */
  public void setMoRtToBuy(double moRtToBuy) { this.moRtToBuy = new Double(moRtToBuy); }

  /**
   *
   * @param moRtToBuy int
   */
  public void setMoRtToBuy(int moRtToBuy) { this.moRtToBuy = new Double((long)moRtToBuy); }

  /**
   *
   * @return Date
   */
  public Date getUpdateDt() { return this.updateDt; }

  /**
   *
   * @param updateDt Date
   */
  public void setUpdateDt(Date updateDt) { this.updateDt = updateDt; }

  public String getEdCo() { return this.edCo; }
  public void setEdCo(String edCo) { this.edCo = edCo; }

  public String getEdLa() { return this.edLa; }
  public void setEdLa(String edLa) { this.edLa = edLa; }

  public String getCdFin() { return this.cdFin; }
  public void setCdFin(String cdFin) { this.cdFin = cdFin; }
  /**
   *
   * @param rs ResultSet
   * @throws SQLException
   * @return BaseOracleBean[]
   */
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    try {
      ArrayList list = new ArrayList();
      while (rs.next()) {
        ArmCurrencyRateOracleBean bean = new ArmCurrencyRateOracleBean();
        bean.idCnyFrom = getStringFromResultSet(rs, "ID_CNY_FROM");
        bean.idCnyTo = getStringFromResultSet(rs, "ID_CNY_TO");
      	bean.edCo = getStringFromResultSet(rs, "ED_CO");
      	bean.edLa = getStringFromResultSet(rs, "ED_LA");
      	bean.cdFin = getStringFromResultSet(rs, "CD_FIN");
        if (bean.getIdCnyFrom() != null && bean.getIdCnyFrom().trim().length() == 3 && bean.getIdCnyTo() != null && bean.getIdCnyTo().trim().length() == 3){
          bean.moRtToBuy = getDoubleFromResultSet(rs, "MO_RT_TO_BUY");
          bean.updateDt = getDateFromResultSet(rs, "UPDATE_DT");
         list.add(bean);
        }
      }

      return (ArmCurrencyRateOracleBean[]) list.toArray(new
          ArmCurrencyRateOracleBean[0]);
    }
    catch (Exception ex){
      ex.printStackTrace();
      throw new SQLException();
    }
  }

  /**
   *
   * @return List
   */
    public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCnyFrom(), Types.VARCHAR);
    addToList(list, this.getIdCnyTo(), Types.VARCHAR);
    addToList(list, this.getMoRtToBuy(), Types.DECIMAL);
    addToList(list, this.getUpdateDt(), Types.TIMESTAMP);
    addToList(list, this.getEdCo(), Types.VARCHAR);
    addToList(list, this.getEdLa(), Types.VARCHAR);
    addToList(list, this.getCdFin(), Types.VARCHAR);
    return list;
  }
}
