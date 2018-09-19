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
 * This class is an object representation of the Arts database table RK_TH_PRM<BR>
 * Followings are the column of the table: <BR>
 *     ID(PK) -- VARCHAR2(128)<BR>
 *     CURRENCY_CODE(PK) -- VARCHAR2(50)<BR>
 *     START_DATE -- DATE(7)<BR>
 *     END_DATE -- DATE(7)<BR>
 *     AMOUNT_OFF -- VARCHAR2(75)<BR>
 *     PERCENT_OFF -- NUMBER(7.4)<BR>
 *     PERCENT_OFF_FL -- NUMBER(1)<BR>
 *     THRESHOLD_AMT -- VARCHAR2(75)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     PROMOTION_NAME -- VARCHAR2(50)<BR>
 *
 */
public class RkThPrmOracleBean extends BaseOracleBean {

  public RkThPrmOracleBean() {}

  public static String selectSql = "select ID, CURRENCY_CODE, START_DATE, END_DATE, AMOUNT_OFF, PERCENT_OFF, PERCENT_OFF_FL, THRESHOLD_AMT, ID_STR_RT, PROMOTION_NAME from RK_TH_PRM ";
  public static String insertSql = "insert into RK_TH_PRM (ID, CURRENCY_CODE, START_DATE, END_DATE, AMOUNT_OFF, PERCENT_OFF, PERCENT_OFF_FL, THRESHOLD_AMT, ID_STR_RT, PROMOTION_NAME) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_TH_PRM set ID = ?, CURRENCY_CODE = ?, START_DATE = ?, END_DATE = ?, AMOUNT_OFF = ?, PERCENT_OFF = ?, PERCENT_OFF_FL = ?, THRESHOLD_AMT = ?, ID_STR_RT = ?, PROMOTION_NAME = ? ";
  public static String deleteSql = "delete from RK_TH_PRM ";

  public static String TABLE_NAME = "RK_TH_PRM";
  public static String COL_ID = "RK_TH_PRM.ID";
  public static String COL_CURRENCY_CODE = "RK_TH_PRM.CURRENCY_CODE";
  public static String COL_START_DATE = "RK_TH_PRM.START_DATE";
  public static String COL_END_DATE = "RK_TH_PRM.END_DATE";
  public static String COL_AMOUNT_OFF = "RK_TH_PRM.AMOUNT_OFF";
  public static String COL_PERCENT_OFF = "RK_TH_PRM.PERCENT_OFF";
  public static String COL_PERCENT_OFF_FL = "RK_TH_PRM.PERCENT_OFF_FL";
  public static String COL_THRESHOLD_AMT = "RK_TH_PRM.THRESHOLD_AMT";
  public static String COL_ID_STR_RT = "RK_TH_PRM.ID_STR_RT";
  public static String COL_PROMOTION_NAME = "RK_TH_PRM.PROMOTION_NAME";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String id;
  private String currencyCode;
  private Date startDate;
  private Date endDate;
  private ArmCurrency amountOff;
  private Double percentOff;
  private Boolean percentOffFl;
  private ArmCurrency thresholdAmt;
  private String idStrRt;
  private String promotionName;

  public String getId() { return this.id; }
  public void setId(String id) { this.id = id; }

  public String getCurrencyCode() { return this.currencyCode; }
  public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

  public Date getStartDate() { return this.startDate; }
  public void setStartDate(Date startDate) { this.startDate = startDate; }

  public Date getEndDate() { return this.endDate; }
  public void setEndDate(Date endDate) { this.endDate = endDate; }

  public ArmCurrency getAmountOff() { return this.amountOff; }
  public void setAmountOff(ArmCurrency amountOff) { this.amountOff = amountOff; }

  public Double getPercentOff() { return this.percentOff; }
  public void setPercentOff(Double percentOff) { this.percentOff = percentOff; }
  public void setPercentOff(double percentOff) { this.percentOff = new Double(percentOff); }

  public Boolean getPercentOffFl() { return this.percentOffFl; }
  public void setPercentOffFl(Boolean percentOffFl) { this.percentOffFl = percentOffFl; }
  public void setPercentOffFl(boolean percentOffFl) { this.percentOffFl = new Boolean(percentOffFl); }

  public ArmCurrency getThresholdAmt() { return this.thresholdAmt; }
  public void setThresholdAmt(ArmCurrency thresholdAmt) { this.thresholdAmt = thresholdAmt; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getPromotionName() { return this.promotionName; }
  public void setPromotionName(String promotionName) { this.promotionName = promotionName; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkThPrmOracleBean bean = new RkThPrmOracleBean();
      bean.id = getStringFromResultSet(rs, "ID");
      bean.currencyCode = getStringFromResultSet(rs, "CURRENCY_CODE");
      bean.startDate = getDateFromResultSet(rs, "START_DATE");
      bean.endDate = getDateFromResultSet(rs, "END_DATE");
      bean.amountOff = getCurrencyFromResultSet(rs, "AMOUNT_OFF");
      bean.percentOff = getDoubleFromResultSet(rs, "PERCENT_OFF");
      bean.percentOffFl = getBooleanFromResultSet(rs, "PERCENT_OFF_FL");
      bean.thresholdAmt = getCurrencyFromResultSet(rs, "THRESHOLD_AMT");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.promotionName = getStringFromResultSet(rs, "PROMOTION_NAME");
      list.add(bean);
    }
    return (RkThPrmOracleBean[]) list.toArray(new RkThPrmOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getId(), Types.VARCHAR);
    addToList(list, this.getCurrencyCode(), Types.VARCHAR);
    addToList(list, this.getStartDate(), Types.TIMESTAMP);
    addToList(list, this.getEndDate(), Types.TIMESTAMP);
    addToList(list, this.getAmountOff(), Types.VARCHAR);
    addToList(list, this.getPercentOff(), Types.DECIMAL);
    addToList(list, this.getPercentOffFl(), Types.DECIMAL);
    addToList(list, this.getThresholdAmt(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getPromotionName(), Types.VARCHAR);
    return list;
  }

}
