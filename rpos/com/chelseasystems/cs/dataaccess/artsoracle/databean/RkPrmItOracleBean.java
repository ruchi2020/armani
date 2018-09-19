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
 * This class is an object representation of the Arts database table RK_PRM_IT<BR>
 * Followings are the column of the table: <BR>
 *     ID_RU_PRDV(PK) -- VARCHAR2(128)<BR>
 *     CURRENCY_CODE(PK) -- VARCHAR2(50)<BR>
 *     TRIGGER_QTY -- NUMBER(3)<BR>
 *     TRIGGER_AMT -- VARCHAR2(75)<BR>
 *
 */
public class RkPrmItOracleBean extends BaseOracleBean {

  public RkPrmItOracleBean() {}

  public static String selectSql = "select ID_RU_PRDV, CURRENCY_CODE, TRIGGER_QTY, TRIGGER_AMT from RK_PRM_IT ";
  public static String insertSql = "insert into RK_PRM_IT (ID_RU_PRDV, CURRENCY_CODE, TRIGGER_QTY, TRIGGER_AMT) values (?, ?, ?, ?)";
  public static String updateSql = "update RK_PRM_IT set ID_RU_PRDV = ?, CURRENCY_CODE = ?, TRIGGER_QTY = ?, TRIGGER_AMT = ? ";
  public static String deleteSql = "delete from RK_PRM_IT ";

  public static String TABLE_NAME = "RK_PRM_IT";
  public static String COL_ID_RU_PRDV = "RK_PRM_IT.ID_RU_PRDV";
  public static String COL_CURRENCY_CODE = "RK_PRM_IT.CURRENCY_CODE";
  public static String COL_TRIGGER_QTY = "RK_PRM_IT.TRIGGER_QTY";
  public static String COL_TRIGGER_AMT = "RK_PRM_IT.TRIGGER_AMT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idRuPrdv;
  private String currencyCode;
  private Long triggerQty;
  private ArmCurrency triggerAmt;

  public String getIdRuPrdv() { return this.idRuPrdv; }
  public void setIdRuPrdv(String idRuPrdv) { this.idRuPrdv = idRuPrdv; }

  public String getCurrencyCode() { return this.currencyCode; }
  public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

  public Long getTriggerQty() { return this.triggerQty; }
  public void setTriggerQty(Long triggerQty) { this.triggerQty = triggerQty; }
  public void setTriggerQty(long triggerQty) { this.triggerQty = new Long(triggerQty); }
  public void setTriggerQty(int triggerQty) { this.triggerQty = new Long((long)triggerQty); }

  public ArmCurrency getTriggerAmt() { return this.triggerAmt; }
  public void setTriggerAmt(ArmCurrency triggerAmt) { this.triggerAmt = triggerAmt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkPrmItOracleBean bean = new RkPrmItOracleBean();
      bean.idRuPrdv = getStringFromResultSet(rs, "ID_RU_PRDV");
      bean.currencyCode = getStringFromResultSet(rs, "CURRENCY_CODE");
      bean.triggerQty = getLongFromResultSet(rs, "TRIGGER_QTY");
      bean.triggerAmt = getCurrencyFromResultSet(rs, "TRIGGER_AMT");
      list.add(bean);
    }
    return (RkPrmItOracleBean[]) list.toArray(new RkPrmItOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdRuPrdv(), Types.VARCHAR);
    addToList(list, this.getCurrencyCode(), Types.VARCHAR);
    addToList(list, this.getTriggerQty(), Types.DECIMAL);
    addToList(list, this.getTriggerAmt(), Types.VARCHAR);
    return list;
  }

}
