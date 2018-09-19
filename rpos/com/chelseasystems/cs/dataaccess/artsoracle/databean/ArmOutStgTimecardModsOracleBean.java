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
 * This class is an object representation of the Arts database table ARM_OUT_STG_TIMECARD_MODS<BR>
 * Followings are the column of the table: <BR>
 *     TRN_CODE -- VARCHAR2(20)<BR>
 *     COMPANY_CODE -- VARCHAR2(30)<BR>
 *     SHOP_CODE -- VARCHAR2(30)<BR>
 *     EMP_HR_ID -- VARCHAR2(30)<BR>
 *     WEEK_ENDING -- VARCHAR2(8)<BR>
 *     BF_TIMESTAMP -- DATE(11.6)<BR>
 *     AF_TIMESTAMP -- DATE(11.6)<BR>
 *     MOD_TIMESTAMP -- DATE(11.6)<BR>
 *     MOD_EMP_HR_ID -- VARCHAR2(30)<BR>
 *     MOD_REASON -- VARCHAR2(200)<BR>
 *     STATUS -- CHAR(1)<BR>
 *     DATA_POPULATION_DT -- DATE(7)<BR>
 *     EXTRACTED_DT -- DATE(7)<BR>
 *
 */
public class ArmOutStgTimecardModsOracleBean extends BaseOracleBean {

  public ArmOutStgTimecardModsOracleBean() {}

  public static String selectSql = "select TRN_CODE, COMPANY_CODE, SHOP_CODE, EMP_HR_ID, WEEK_ENDING, BF_TIMESTAMP, AF_TIMESTAMP, MOD_TIMESTAMP, MOD_EMP_HR_ID, MOD_REASON, STATUS, DATA_POPULATION_DT, EXTRACTED_DT from ARM_OUT_STG_TIMECARD_MODS ";
  public static String insertSql = "insert into ARM_OUT_STG_TIMECARD_MODS (TRN_CODE, COMPANY_CODE, SHOP_CODE, EMP_HR_ID, WEEK_ENDING, BF_TIMESTAMP, AF_TIMESTAMP, MOD_TIMESTAMP, MOD_EMP_HR_ID, MOD_REASON, STATUS, DATA_POPULATION_DT, EXTRACTED_DT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_OUT_STG_TIMECARD_MODS set TRN_CODE = ?, COMPANY_CODE = ?, SHOP_CODE = ?, EMP_HR_ID = ?, WEEK_ENDING = ?, BF_TIMESTAMP = ?, AF_TIMESTAMP = ?, MOD_TIMESTAMP = ?, MOD_EMP_HR_ID = ?, MOD_REASON = ?, STATUS = ?, DATA_POPULATION_DT = ?, EXTRACTED_DT = ? ";
  public static String deleteSql = "delete from ARM_OUT_STG_TIMECARD_MODS ";

  public static String TABLE_NAME = "ARM_OUT_STG_TIMECARD_MODS";
  public static String COL_TRN_CODE = "ARM_OUT_STG_TIMECARD_MODS.TRN_CODE";
  public static String COL_COMPANY_CODE = "ARM_OUT_STG_TIMECARD_MODS.COMPANY_CODE";
  public static String COL_SHOP_CODE = "ARM_OUT_STG_TIMECARD_MODS.SHOP_CODE";
  public static String COL_EMP_HR_ID = "ARM_OUT_STG_TIMECARD_MODS.EMP_HR_ID";
  public static String COL_WEEK_ENDING = "ARM_OUT_STG_TIMECARD_MODS.WEEK_ENDING";
  public static String COL_BF_TIMESTAMP = "ARM_OUT_STG_TIMECARD_MODS.BF_TIMESTAMP";
  public static String COL_AF_TIMESTAMP = "ARM_OUT_STG_TIMECARD_MODS.AF_TIMESTAMP";
  public static String COL_MOD_TIMESTAMP = "ARM_OUT_STG_TIMECARD_MODS.MOD_TIMESTAMP";
  public static String COL_MOD_EMP_HR_ID = "ARM_OUT_STG_TIMECARD_MODS.MOD_EMP_HR_ID";
  public static String COL_MOD_REASON = "ARM_OUT_STG_TIMECARD_MODS.MOD_REASON";
  public static String COL_STATUS = "ARM_OUT_STG_TIMECARD_MODS.STATUS";
  public static String COL_DATA_POPULATION_DT = "ARM_OUT_STG_TIMECARD_MODS.DATA_POPULATION_DT";
  public static String COL_EXTRACTED_DT = "ARM_OUT_STG_TIMECARD_MODS.EXTRACTED_DT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String trnCode;
  private String companyCode;
  private String shopCode;
  private String empHrId;
  private String weekEnding;
  private Date bfTimestamp;
  private Date afTimestamp;
  private Date modTimestamp;
  private String modEmpHrId;
  private String modReason;
  private String status;
  private Date dataPopulationDt;
  private Date extractedDt;

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

  public Date getBfTimestamp() { return this.bfTimestamp; }
  public void setBfTimestamp(Date bfTimestamp) { this.bfTimestamp = bfTimestamp; }

  public Date getAfTimestamp() { return this.afTimestamp; }
  public void setAfTimestamp(Date afTimestamp) { this.afTimestamp = afTimestamp; }

  public Date getModTimestamp() { return this.modTimestamp; }
  public void setModTimestamp(Date modTimestamp) { this.modTimestamp = modTimestamp; }

  public String getModEmpHrId() { return this.modEmpHrId; }
  public void setModEmpHrId(String modEmpHrId) { this.modEmpHrId = modEmpHrId; }

  public String getModReason() { return this.modReason; }
  public void setModReason(String modReason) { this.modReason = modReason; }

  public String getStatus() { return this.status; }
  public void setStatus(String status) { this.status = status; }

  public Date getDataPopulationDt() { return this.dataPopulationDt; }
  public void setDataPopulationDt(Date dataPopulationDt) { this.dataPopulationDt = dataPopulationDt; }

  public Date getExtractedDt() { return this.extractedDt; }
  public void setExtractedDt(Date extractedDt) { this.extractedDt = extractedDt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmOutStgTimecardModsOracleBean bean = new ArmOutStgTimecardModsOracleBean();
      bean.trnCode = getStringFromResultSet(rs, "TRN_CODE");
      bean.companyCode = getStringFromResultSet(rs, "COMPANY_CODE");
      bean.shopCode = getStringFromResultSet(rs, "SHOP_CODE");
      bean.empHrId = getStringFromResultSet(rs, "EMP_HR_ID");
      bean.weekEnding = getStringFromResultSet(rs, "WEEK_ENDING");
      bean.bfTimestamp = getDateFromResultSet(rs, "BF_TIMESTAMP");
      bean.afTimestamp = getDateFromResultSet(rs, "AF_TIMESTAMP");
      bean.modTimestamp = getDateFromResultSet(rs, "MOD_TIMESTAMP");
      bean.modEmpHrId = getStringFromResultSet(rs, "MOD_EMP_HR_ID");
      bean.modReason = getStringFromResultSet(rs, "MOD_REASON");
      bean.status = getStringFromResultSet(rs, "STATUS");
      bean.dataPopulationDt = getDateFromResultSet(rs, "DATA_POPULATION_DT");
      bean.extractedDt = getDateFromResultSet(rs, "EXTRACTED_DT");
      list.add(bean);
    }
    return (ArmOutStgTimecardModsOracleBean[]) list.toArray(new ArmOutStgTimecardModsOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTrnCode(), Types.VARCHAR);
    addToList(list, this.getCompanyCode(), Types.VARCHAR);
    addToList(list, this.getShopCode(), Types.VARCHAR);
    addToList(list, this.getEmpHrId(), Types.VARCHAR);
    addToList(list, this.getWeekEnding(), Types.VARCHAR);
    addToList(list, this.getBfTimestamp(), Types.TIMESTAMP);
    addToList(list, this.getAfTimestamp(), Types.TIMESTAMP);
    addToList(list, this.getModTimestamp(), Types.TIMESTAMP);
    addToList(list, this.getModEmpHrId(), Types.VARCHAR);
    addToList(list, this.getModReason(), Types.VARCHAR);
    addToList(list, this.getStatus(), Types.VARCHAR);
    addToList(list, this.getDataPopulationDt(), Types.TIMESTAMP);
    addToList(list, this.getExtractedDt(), Types.TIMESTAMP);
    return list;
  }

}
