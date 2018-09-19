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
import com.chelseasystems.cs.util.*;
/**
 *
 * This class is an object representation of the Arts database table ARM_OUT_STG_TIMECARD<BR>
 * Followings are the column of the table: <BR>
 *     TRN_CODE -- VARCHAR2(20)<BR>
 *     COMPANY_CODE -- VARCHAR2(30)<BR>
 *     SHOP_CODE -- VARCHAR2(30)<BR>
 *     EMP_HR_ID -- VARCHAR2(30)<BR>
 *     WEEK_ENDING -- VARCHAR2(8)<BR>
 *     STATUS -- CHAR(1)<BR>
 *     DATA_POPULATION_DT -- DATE(7)<BR>
 *     EXTRACTED_DT -- DATE(7)<BR>
 *     TIMESTAMP -- DATE(11.6)<BR>
 *
 */
public class ArmOutStgTimecardOracleBean extends BaseOracleBean {

  public ArmOutStgTimecardOracleBean() {}

  public static String selectSql = "select TRN_CODE, COMPANY_CODE, SHOP_CODE, EMP_HR_ID, WEEK_ENDING, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, TIMESTAMP from ARM_OUT_STG_TIMECARD ";
  public static String insertSql = "insert into ARM_OUT_STG_TIMECARD (TRN_CODE, COMPANY_CODE, SHOP_CODE, EMP_HR_ID, WEEK_ENDING, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, TIMESTAMP) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_OUT_STG_TIMECARD set TRN_CODE = ?, COMPANY_CODE = ?, SHOP_CODE = ?, EMP_HR_ID = ?, WEEK_ENDING = ?, STATUS = ?, DATA_POPULATION_DT = ?, EXTRACTED_DT = ?, TIMESTAMP = ? ";
  public static String deleteSql = "delete from ARM_OUT_STG_TIMECARD ";

  public static String TABLE_NAME = "ARM_OUT_STG_TIMECARD";
  public static String COL_TRN_CODE = "ARM_OUT_STG_TIMECARD.TRN_CODE";
  public static String COL_COMPANY_CODE = "ARM_OUT_STG_TIMECARD.COMPANY_CODE";
  public static String COL_SHOP_CODE = "ARM_OUT_STG_TIMECARD.SHOP_CODE";
  public static String COL_EMP_HR_ID = "ARM_OUT_STG_TIMECARD.EMP_HR_ID";
  public static String COL_WEEK_ENDING = "ARM_OUT_STG_TIMECARD.WEEK_ENDING";
  public static String COL_STATUS = "ARM_OUT_STG_TIMECARD.STATUS";
  public static String COL_DATA_POPULATION_DT = "ARM_OUT_STG_TIMECARD.DATA_POPULATION_DT";
  public static String COL_EXTRACTED_DT = "ARM_OUT_STG_TIMECARD.EXTRACTED_DT";
  public static String COL_TIMESTAMP = "ARM_OUT_STG_TIMECARD.TIMESTAMP";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String trnCode;
  private String companyCode;
  private String shopCode;
  private String empHrId;
  private String weekEnding;
  private String status;
  private Date dataPopulationDt;
  private Date extractedDt;
  private Date timestamp;

  public String getTrnCode() { return this.trnCode; }
  public void setTrnCode(String trnCode) { this.trnCode = trnCode; }

  public String getCompanyCode() { return this.companyCode; }
  public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

  public String getShopCode() { return this.shopCode; }
  public void setShopCode(String shopCode) { this.shopCode = shopCode; }

  public String getEmpHrId() { return this.empHrId; }
  public void setEmpHrId(String empHrId) { this.empHrId = empHrId; }

  public String getWeekEnding() { return this.weekEnding; }
  public void setWeekEnding(String weekEnding) { this.weekEnding = weekEnding; }

  public String getStatus() { return this.status; }
  public void setStatus(String status) { this.status = status; }

  public Date getDataPopulationDt() { return this.dataPopulationDt; }
  public void setDataPopulationDt(Date dataPopulationDt) { this.dataPopulationDt = dataPopulationDt; }

  public Date getExtractedDt() { return this.extractedDt; }
  public void setExtractedDt(Date extractedDt) { this.extractedDt = extractedDt; }

  public Date getTimestamp() { return this.timestamp; }
  public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmOutStgTimecardOracleBean bean = new ArmOutStgTimecardOracleBean();
      bean.trnCode = getStringFromResultSet(rs, "TRN_CODE");
      bean.companyCode = getStringFromResultSet(rs, "COMPANY_CODE");
      bean.shopCode = getStringFromResultSet(rs, "SHOP_CODE");
      bean.empHrId = getStringFromResultSet(rs, "EMP_HR_ID");
      bean.weekEnding = getStringFromResultSet(rs, "WEEK_ENDING");
      bean.status = getStringFromResultSet(rs, "STATUS");
      bean.dataPopulationDt = getDateFromResultSet(rs, "DATA_POPULATION_DT");
      bean.extractedDt = getDateFromResultSet(rs, "EXTRACTED_DT");
      bean.timestamp = getDateFromResultSet(rs, "TIMESTAMP");
      list.add(bean);
    }
    return (ArmOutStgTimecardOracleBean[]) list.toArray(new ArmOutStgTimecardOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTrnCode(), Types.VARCHAR);
    addToList(list, this.getCompanyCode(), Types.VARCHAR);
    addToList(list, this.getShopCode(), Types.VARCHAR);
    addToList(list, this.getEmpHrId(), Types.VARCHAR);
    addToList(list, this.getWeekEnding(), Types.VARCHAR);
    addToList(list, this.getStatus(), Types.VARCHAR);
    addToList(list, this.getDataPopulationDt(), Types.TIMESTAMP);
    addToList(list, this.getExtractedDt(), Types.TIMESTAMP);
    addToList(list, this.getTimestamp(), Types.TIMESTAMP);
    return list;
  }

}
