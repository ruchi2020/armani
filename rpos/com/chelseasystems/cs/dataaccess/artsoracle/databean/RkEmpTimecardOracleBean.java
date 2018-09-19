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
 * This class is an object representation of the Arts database table RK_EMP_TIMECARD<BR>
 * Followings are the column of the table: <BR>
 *     CUR_STATUS -- NUMBER(3)<BR>
 *     WORK_TIME -- NUMBER(22)<BR>
 *     WEEK_ENDING(PK) -- VARCHAR2(50)<BR>
 *     IS_FINALIZED -- NUMBER(1)<BR>
 *     IS_ADJSTABLE -- NUMBER(1)<BR>
 *     FINALIZ_TIME -- VARCHAR2(96)<BR>
 *     FINALIZ_MGR -- VARCHAR2(50)<BR>
 *     COMMENT -- VARCHAR2(200)<BR>
 *     ID_EM(PK) -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *
 */
public class RkEmpTimecardOracleBean extends BaseOracleBean {

  public RkEmpTimecardOracleBean() {}

  public static String selectSql = "select CUR_STATUS, WORK_TIME, WEEK_ENDING, IS_FINALIZED, IS_ADJSTABLE, FINALIZ_TIME, FINALIZ_MGR, COMMENTS, ID_EM, ID_STR_RT from RK_EMP_TIMECARD ";
  public static String insertSql = "insert into RK_EMP_TIMECARD (CUR_STATUS, WORK_TIME, WEEK_ENDING, IS_FINALIZED, IS_ADJSTABLE, FINALIZ_TIME, FINALIZ_MGR, COMMENTS, ID_EM, ID_STR_RT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_EMP_TIMECARD set CUR_STATUS = ?, WORK_TIME = ?, WEEK_ENDING = ?, IS_FINALIZED = ?, IS_ADJSTABLE = ?, FINALIZ_TIME = ?, FINALIZ_MGR = ?, COMMENTS = ?, ID_EM = ?, ID_STR_RT = ? ";
  public static String deleteSql = "delete from RK_EMP_TIMECARD ";

  public static String TABLE_NAME = "RK_EMP_TIMECARD";
  public static String COL_CUR_STATUS = "RK_EMP_TIMECARD.CUR_STATUS";
  public static String COL_WORK_TIME = "RK_EMP_TIMECARD.WORK_TIME";
  public static String COL_WEEK_ENDING = "RK_EMP_TIMECARD.WEEK_ENDING";
  public static String COL_IS_FINALIZED = "RK_EMP_TIMECARD.IS_FINALIZED";
  public static String COL_IS_ADJSTABLE = "RK_EMP_TIMECARD.IS_ADJSTABLE";
  public static String COL_FINALIZ_TIME = "RK_EMP_TIMECARD.FINALIZ_TIME";
  public static String COL_FINALIZ_MGR = "RK_EMP_TIMECARD.FINALIZ_MGR";
  public static String COL_COMMENT = "RK_EMP_TIMECARD.COMMENTS";
  public static String COL_ID_EM = "RK_EMP_TIMECARD.ID_EM";
  public static String COL_ID_STR_RT = "RK_EMP_TIMECARD.ID_STR_RT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Long curStatus;
  private Long workTime;
  private String weekEnding;
  private Boolean isFinalized;
  private Boolean isAdjstable;
  private Calendar finalizTime;
  private String finalizMgr;
  private String comment;
  private String idEm;
  private String idStrRt;

  public Long getCurStatus() { return this.curStatus; }
  public void setCurStatus(Long curStatus) { this.curStatus = curStatus; }
  public void setCurStatus(long curStatus) { this.curStatus = new Long(curStatus); }
  public void setCurStatus(int curStatus) { this.curStatus = new Long((long)curStatus); }

  public Long getWorkTime() { return this.workTime; }
  public void setWorkTime(Long workTime) { this.workTime = workTime; }
  public void setWorkTime(long workTime) { this.workTime = new Long(workTime); }
  public void setWorkTime(int workTime) { this.workTime = new Long((long)workTime); }

  public String getWeekEnding() { return this.weekEnding; }
  public void setWeekEnding(String weekEnding) { this.weekEnding = weekEnding; }

  public Boolean getIsFinalized() { return this.isFinalized; }
  public void setIsFinalized(Boolean isFinalized) { this.isFinalized = isFinalized; }
  public void setIsFinalized(boolean isFinalized) { this.isFinalized = new Boolean(isFinalized); }

  public Boolean getIsAdjstable() { return this.isAdjstable; }
  public void setIsAdjstable(Boolean isAdjstable) { this.isAdjstable = isAdjstable; }
  public void setIsAdjstable(boolean isAdjstable) { this.isAdjstable = new Boolean(isAdjstable); }

  public Calendar getFinalizTime() { return this.finalizTime; }
  public void setFinalizTime(Calendar finalizTime) { this.finalizTime = finalizTime; }

  public String getFinalizMgr() { return this.finalizMgr; }
  public void setFinalizMgr(String finalizMgr) { this.finalizMgr = finalizMgr; }

  public String getComments() { return this.comment; }
  public void setComments(String comment) { this.comment = comment; }

  public String getIdEm() { return this.idEm; }
  public void setIdEm(String idEm) { this.idEm = idEm; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkEmpTimecardOracleBean bean = new RkEmpTimecardOracleBean();
      bean.curStatus = getLongFromResultSet(rs, "CUR_STATUS");
      bean.workTime = getLongFromResultSet(rs, "WORK_TIME");
      bean.weekEnding = getStringFromResultSet(rs, "WEEK_ENDING");
      bean.isFinalized = getBooleanFromResultSet(rs, "IS_FINALIZED");
      bean.isAdjstable = getBooleanFromResultSet(rs, "IS_ADJSTABLE");
      bean.finalizTime = getCalendarFromResultSet(rs, "FINALIZ_TIME");
      bean.finalizMgr = getStringFromResultSet(rs, "FINALIZ_MGR");
      bean.comment = getStringFromResultSet(rs, "COMMENTS");
      bean.idEm = getStringFromResultSet(rs, "ID_EM");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      list.add(bean);
    }
    return (RkEmpTimecardOracleBean[]) list.toArray(new RkEmpTimecardOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getCurStatus(), Types.DECIMAL);
    addToList(list, this.getWorkTime(), Types.DECIMAL);
    addToList(list, this.getWeekEnding(), Types.VARCHAR);
    addToList(list, this.getIsFinalized(), Types.DECIMAL);
    addToList(list, this.getIsAdjstable(), Types.DECIMAL);
    addToList(list, this.getFinalizTime(), Types.VARCHAR);
    addToList(list, this.getFinalizMgr(), Types.VARCHAR);
    addToList(list, this.getComments(), Types.VARCHAR);
    addToList(list, this.getIdEm(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    return list;
  }

}
