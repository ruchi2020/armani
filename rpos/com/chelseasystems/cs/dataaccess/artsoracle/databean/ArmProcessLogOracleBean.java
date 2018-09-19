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
 * This class is an object representation of the Arts database table ARM_PROCESS_LOG<BR>
 * Followings are the column of the table: <BR>
 *     INTERFACE_KEY -- VARCHAR2(20)<BR>
 *     START_TIME -- DATE(7)<BR>
 *     END_TIME -- DATE(7)<BR>
 *     STATUS -- CHAR(1)<BR>
 *     RECORD_NUM -- NUMBER(22)<BR>
 *     FILE_NAME -- VARCHAR2(100)<BR>
 *
 */
public class ArmProcessLogOracleBean extends BaseOracleBean {

  public ArmProcessLogOracleBean() {}

  public static String selectSql = "select INTERFACE_KEY, START_TIME, END_TIME, STATUS, RECORD_NUM, FILE_NAME from ARM_PROCESS_LOG ";
  public static String insertSql = "insert into ARM_PROCESS_LOG (INTERFACE_KEY, START_TIME, END_TIME, STATUS, RECORD_NUM, FILE_NAME) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_PROCESS_LOG set INTERFACE_KEY = ?, START_TIME = ?, END_TIME = ?, STATUS = ?, RECORD_NUM = ?, FILE_NAME = ? ";
  public static String deleteSql = "delete from ARM_PROCESS_LOG ";

  public static String TABLE_NAME = "ARM_PROCESS_LOG";
  public static String COL_INTERFACE_KEY = "ARM_PROCESS_LOG.INTERFACE_KEY";
  public static String COL_START_TIME = "ARM_PROCESS_LOG.START_TIME";
  public static String COL_END_TIME = "ARM_PROCESS_LOG.END_TIME";
  public static String COL_STATUS = "ARM_PROCESS_LOG.STATUS";
  public static String COL_RECORD_NUM = "ARM_PROCESS_LOG.RECORD_NUM";
  public static String COL_FILE_NAME = "ARM_PROCESS_LOG.FILE_NAME";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String interfaceKey;
  private Date startTime;
  private Date endTime;
  private String status;
  private Long recordNum;
  private String fileName;

  public String getInterfaceKey() { return this.interfaceKey; }
  public void setInterfaceKey(String interfaceKey) { this.interfaceKey = interfaceKey; }

  public Date getStartTime() { return this.startTime; }
  public void setStartTime(Date startTime) { this.startTime = startTime; }

  public Date getEndTime() { return this.endTime; }
  public void setEndTime(Date endTime) { this.endTime = endTime; }

  public String getStatus() { return this.status; }
  public void setStatus(String status) { this.status = status; }

  public Long getRecordNum() { return this.recordNum; }
  public void setRecordNum(Long recordNum) { this.recordNum = recordNum; }
  public void setRecordNum(long recordNum) { this.recordNum = new Long(recordNum); }
  public void setRecordNum(int recordNum) { this.recordNum = new Long((long)recordNum); }

  public String getFileName() { return this.fileName; }
  public void setFileName(String fileName) { this.fileName = fileName; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmProcessLogOracleBean bean = new ArmProcessLogOracleBean();
      bean.interfaceKey = getStringFromResultSet(rs, "INTERFACE_KEY");
      bean.startTime = getDateFromResultSet(rs, "START_TIME");
      bean.endTime = getDateFromResultSet(rs, "END_TIME");
      bean.status = getStringFromResultSet(rs, "STATUS");
      bean.recordNum = getLongFromResultSet(rs, "RECORD_NUM");
      bean.fileName = getStringFromResultSet(rs, "FILE_NAME");
      list.add(bean);
    }
    return (ArmProcessLogOracleBean[]) list.toArray(new ArmProcessLogOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getInterfaceKey(), Types.VARCHAR);
    addToList(list, this.getStartTime(), Types.TIMESTAMP);
    addToList(list, this.getEndTime(), Types.TIMESTAMP);
    addToList(list, this.getStatus(), Types.VARCHAR);
    addToList(list, this.getRecordNum(), Types.DECIMAL);
    addToList(list, this.getFileName(), Types.VARCHAR);
    return list;
  }

}
