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
 * This class is an object representation of the Arts database table ARM_ITM_HIST<BR>
 * Followings are the column of the table: <BR>
 *     ID_ITM -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     ID_ITM_HIST -- VARCHAR2(128)<BR>
 *     KEY -- VARCHAR2(25)<BR>
 *     VALUE -- VARCHAR2(255)<BR>
 *     EFFECTIVE_DT -- DATE(7)<BR>
 *     EXPIRATION_DT -- DATE(7)<BR>
 *
 */
public class ArmItmHistOracleBean extends BaseOracleBean {

  public ArmItmHistOracleBean() {}

  public static String selectSql = "select ID_ITM, ID_STR_RT, ID_ITM_HIST, KEY, VALUE, EFFECTIVE_DT, EXPIRATION_DT from ARM_ITM_HIST ";
  public static String insertSql = "insert into ARM_ITM_HIST (ID_ITM, ID_STR_RT, ID_ITM_HIST, KEY, VALUE, EFFECTIVE_DT, EXPIRATION_DT) values (?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_ITM_HIST set ID_ITM = ?, ID_STR_RT = ?, ID_ITM_HIST = ?, KEY = ?, VALUE = ?, EFFECTIVE_DT = ?, EXPIRATION_DT = ? ";
  public static String deleteSql = "delete from ARM_ITM_HIST ";

  public static String TABLE_NAME = "ARM_ITM_HIST";
  public static String COL_ID_ITM = "ARM_ITM_HIST.ID_ITM";
  public static String COL_ID_STR_RT = "ARM_ITM_HIST.ID_STR_RT";
  public static String COL_ID_ITM_HIST = "ARM_ITM_HIST.ID_ITM_HIST";
  public static String COL_KEY = "ARM_ITM_HIST.KEY";
  public static String COL_VALUE = "ARM_ITM_HIST.VALUE";
  public static String COL_EFFECTIVE_DT = "ARM_ITM_HIST.EFFECTIVE_DT";
  public static String COL_EXPIRATION_DT = "ARM_ITM_HIST.EXPIRATION_DT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idItm;
  private String idStrRt;
  private String idItmHist;
  private String key;
  private String value;
  private Date effectiveDt;
  private Date expirationDt;

  public String getIdItm() { return this.idItm; }
  public void setIdItm(String idItm) { this.idItm = idItm; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdItmHist() { return this.idItmHist; }
  public void setIdItmHist(String idItmHist) { this.idItmHist = idItmHist; }

  public String getKey() { return this.key; }
  public void setKey(String key) { this.key = key; }

  public String getValue() { return this.value; }
  public void setValue(String value) { this.value = value; }

  public Date getEffectiveDt() { return this.effectiveDt; }
  public void setEffectiveDt(Date effectiveDt) { this.effectiveDt = effectiveDt; }

  public Date getExpirationDt() { return this.expirationDt; }
  public void setExpirationDt(Date expirationDt) { this.expirationDt = expirationDt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmItmHistOracleBean bean = new ArmItmHistOracleBean();
      bean.idItm = getStringFromResultSet(rs, "ID_ITM");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idItmHist = getStringFromResultSet(rs, "ID_ITM_HIST");
      bean.key = getStringFromResultSet(rs, "KEY");
      bean.value = getStringFromResultSet(rs, "VALUE");
      bean.effectiveDt = getDateFromResultSet(rs, "EFFECTIVE_DT");
      bean.expirationDt = getDateFromResultSet(rs, "EXPIRATION_DT");
      list.add(bean);
    }
    return (ArmItmHistOracleBean[]) list.toArray(new ArmItmHistOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdItm(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdItmHist(), Types.VARCHAR);
    addToList(list, this.getKey(), Types.VARCHAR);
    addToList(list, this.getValue(), Types.VARCHAR);
    addToList(list, this.getEffectiveDt(), Types.TIMESTAMP);
    addToList(list, this.getExpirationDt(), Types.TIMESTAMP);
    return list;
  }

}
