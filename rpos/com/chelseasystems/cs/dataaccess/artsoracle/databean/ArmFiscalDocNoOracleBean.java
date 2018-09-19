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
 * This class is an object representation of the Arts database table ARM_FISCAL_DOC_NO<BR>
 * Followings are the column of the table: <BR>
 *     REGISTER_ID -- VARCHAR2(10)<BR>
 *     ID_STR_RT -- VARCHAR2(20)<BR>
 *     LAST_DDT_NO -- VARCHAR2(20)<BR>
 *     LAST_VAT_NO -- VARCHAR2(20)<BR>
 *     LAST_CREDIT_NOTE -- VARCHAR2(20)<BR>
 *
 */
public class ArmFiscalDocNoOracleBean extends BaseOracleBean {

  public ArmFiscalDocNoOracleBean() {}

  public static String selectSql = "select REGISTER_ID, ID_STR_RT, LAST_DDT_NO, LAST_VAT_NO, LAST_CREDIT_NOTE from ARM_FISCAL_DOC_NO ";
  public static String insertSql = "insert into ARM_FISCAL_DOC_NO (REGISTER_ID, ID_STR_RT, LAST_DDT_NO, LAST_VAT_NO, LAST_CREDIT_NOTE) values (?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_FISCAL_DOC_NO set REGISTER_ID = ?, ID_STR_RT = ?, LAST_DDT_NO = ?, LAST_VAT_NO = ?, LAST_CREDIT_NOTE = ? ";
  public static String deleteSql = "delete from ARM_FISCAL_DOC_NO ";

  public static String TABLE_NAME = "ARM_FISCAL_DOC_NO";
  public static String COL_REGISTER_ID = "ARM_FISCAL_DOC_NO.REGISTER_ID";
  public static String COL_ID_STR_RT = "ARM_FISCAL_DOC_NO.ID_STR_RT";
  public static String COL_LAST_DDT_NO = "ARM_FISCAL_DOC_NO.LAST_DDT_NO";
  public static String COL_LAST_VAT_NO = "ARM_FISCAL_DOC_NO.LAST_VAT_NO";
  public static String COL_LAST_CREDIT_NOTE = "ARM_FISCAL_DOC_NO.LAST_CREDIT_NOTE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String registerId;
  private String idStrRt;
  private String lastDdtNo;
  private String lastVatNo;
  private String lastCreditNote;

  public String getRegisterId() { return this.registerId; }
  public void setRegisterId(String registerId) { this.registerId = registerId; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getLastDdtNo() { return this.lastDdtNo; }
  public void setLastDdtNo(String lastDdtNo) { this.lastDdtNo = lastDdtNo; }

  public String getLastVatNo() { return this.lastVatNo; }
  public void setLastVatNo(String lastVatNo) { this.lastVatNo = lastVatNo; }

  public String getLastCreditNote() { return this.lastCreditNote; }
  public void setLastCreditNote(String lastCreditNote) { this.lastCreditNote = lastCreditNote; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmFiscalDocNoOracleBean bean = new ArmFiscalDocNoOracleBean();
      bean.registerId = getStringFromResultSet(rs, "REGISTER_ID");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.lastDdtNo = getStringFromResultSet(rs, "LAST_DDT_NO");
      bean.lastVatNo = getStringFromResultSet(rs, "LAST_VAT_NO");
      bean.lastCreditNote = getStringFromResultSet(rs, "LAST_CREDIT_NOTE");
      list.add(bean);
    }
    return (ArmFiscalDocNoOracleBean[]) list.toArray(new ArmFiscalDocNoOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getRegisterId(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getLastDdtNo(), Types.VARCHAR);
    addToList(list, this.getLastVatNo(), Types.VARCHAR);
    addToList(list, this.getLastCreditNote(), Types.VARCHAR);
    return list;
  }

}
