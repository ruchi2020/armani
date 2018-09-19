//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;

/**
 *
 * This class is an object representation of the Arts database table ARM_EMP_ALERT_RULE<BR>
 * Followings are the column of the table: <BR>
 *     CD_CO -- VARCHAR2(20)<BR>
 *     RECORD_TYPE -- VARCHAR2(20)<BR>
 *     START_DATE -- DATE(7)<BR>
 *     END_DATE -- DATE(7)<BR>
 *     PRODUCT_CD -- VARCHAR2(20)<BR>
 *     VALUE -- VARCHAR2(20)<BR>
 *     PRIORITY -- NUMBER(22)<BR>
 *
 */
public class ArmEmpAlertRuleOracleBean extends BaseOracleBean {

  public ArmEmpAlertRuleOracleBean() {}

  public static String selectSql = "select CD_CO, RECORD_TYPE, START_DATE, END_DATE, PRODUCT_CD, VALUE, PRIORITY,ID_BRAND, DSC_LEVEL from ARM_EMP_ALERT_RULE ";
  public static String insertSql = "insert into ARM_EMP_ALERT_RULE (CD_CO, RECORD_TYPE, START_DATE, END_DATE, PRODUCT_CD, VALUE, PRIORITY,ID_BRAND, DSC_LEVEL) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_EMP_ALERT_RULE set CD_CO = ?, RECORD_TYPE = ?, START_DATE = ?, END_DATE = ?, PRODUCT_CD = ?, VALUE = ?, PRIORITY = ?, ID_BRAND = ?, DSC_LEVEL = ? ";
  public static String deleteSql = "delete from ARM_EMP_ALERT_RULE ";

  public static String TABLE_NAME = "ARM_EMP_ALERT_RULE";
  public static String COL_CD_CO = "ARM_EMP_ALERT_RULE.CD_CO";
  public static String COL_RECORD_TYPE = "ARM_EMP_ALERT_RULE.RECORD_TYPE";
  public static String COL_START_DATE = "ARM_EMP_ALERT_RULE.START_DATE";
  public static String COL_END_DATE = "ARM_EMP_ALERT_RULE.END_DATE";
  public static String COL_PRODUCT_CD = "ARM_EMP_ALERT_RULE.PRODUCT_CD";
  public static String COL_VALUE = "ARM_EMP_ALERT_RULE.VALUE";
  public static String COL_PRIORITY = "ARM_EMP_ALERT_RULE.PRIORITY";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String cdCo;
  private String recordType;
  private Date startDate;
  private Date endDate;
  private String productCd;
  private String value;
  private Long priority;
  private String iDBrand;
  private String dsc_level;

  public String getCdCo() { return this.cdCo; }
  public void setCdCo(String cdCo) { this.cdCo = cdCo; }

  public String getRecordType() { return this.recordType; }
  public void setRecordType(String recordType) { this.recordType = recordType; }

  public Date getStartDate() { return this.startDate; }
  public void setStartDate(Date startDate) { this.startDate = startDate; }

  public Date getEndDate() { return this.endDate; }
  public void setEndDate(Date endDate) { this.endDate = endDate; }

  public String getProductCd() { return this.productCd; }
  public void setProductCd(String productCd) { this.productCd = productCd; }

  public String getValue() { return this.value; }
  public void setValue(String value) { this.value = value; }

  public Long getPriority() { return this.priority; }
  public void setPriority(Long priority) { this.priority = priority; }
  public void setPriority(long priority) { this.priority = new Long(priority); }
  public void setPriority(int priority) { this.priority = new Long((long)priority); }

  public String getIDBrand() {		return this.iDBrand;}
  public void setIDBrand(String brand) {	this.iDBrand = brand;}
  
  public String getDsc_level() {return dsc_level;}
  public void setDsc_level(String dsc_level) {this.dsc_level = dsc_level;}
 
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmEmpAlertRuleOracleBean bean = new ArmEmpAlertRuleOracleBean();
      bean.cdCo = getStringFromResultSet(rs, "CD_CO");
      bean.recordType = getStringFromResultSet(rs, "RECORD_TYPE");
      bean.startDate = getDateFromResultSet(rs, "START_DATE");
      bean.endDate = getDateFromResultSet(rs, "END_DATE");
      bean.productCd = getStringFromResultSet(rs, "PRODUCT_CD");
      bean.value = getStringFromResultSet(rs, "VALUE");
      bean.priority = getLongFromResultSet(rs, "PRIORITY");
     if( !bean.cdCo.equals("US")){
	 bean.iDBrand = getStringFromResultSet(rs,"ID_BRAND");
         bean.dsc_level = getStringFromResultSet(rs,"DSC_LEVEL");
     }
     
      list.add(bean);
    }
    return (ArmEmpAlertRuleOracleBean[]) list.toArray(new ArmEmpAlertRuleOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getCdCo(), Types.VARCHAR);
    addToList(list, this.getRecordType(), Types.VARCHAR);
    addToList(list, this.getStartDate(), Types.TIMESTAMP);
    addToList(list, this.getEndDate(), Types.TIMESTAMP);
    addToList(list, this.getProductCd(), Types.VARCHAR);
    addToList(list, this.getValue(), Types.VARCHAR);
    addToList(list, this.getPriority(), Types.DECIMAL);
    addToList(list, this.getIDBrand(), Types.VARCHAR);
    addToList(list, this.getDsc_level(), Types.VARCHAR);
    
    return list;
  }

}
