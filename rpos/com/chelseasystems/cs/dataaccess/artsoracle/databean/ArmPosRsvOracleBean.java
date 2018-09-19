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
 * This class is an object representation of the Arts database table ARM_POS_RSV<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN -- VARCHAR2(128)<BR>
 *     ID_RESERVATION(PK) -- VARCHAR2(128)<BR>
 *     EXP_DT -- DATE(7)<BR>
 *     REASON_CODE -- VARCHAR2(20)<BR>
 *     DEPOSIT_AMT -- VARCHAR2(75)<BR>
 *     ORIG_RSV_ID -- VARCHAR2(128)<BR>
 *
 */
public class ArmPosRsvOracleBean extends BaseOracleBean {

  public ArmPosRsvOracleBean() {}

  public static String selectSql = "select AI_TRN, ID_RESERVATION, EXP_DT, REASON_CODE, DEPOSIT_AMT, ORIG_RSV_ID from ARM_POS_RSV ";
  public static String insertSql = "insert into ARM_POS_RSV (AI_TRN, ID_RESERVATION, EXP_DT, REASON_CODE, DEPOSIT_AMT, ORIG_RSV_ID) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_POS_RSV set AI_TRN = ?, ID_RESERVATION = ?, EXP_DT = ?, REASON_CODE = ?, DEPOSIT_AMT = ?, ORIG_RSV_ID = ? ";
  public static String updateExpirationDtSql = "update ARM_POS_RSV set EXP_DT = ? ";
  public static String deleteSql = "delete from ARM_POS_RSV ";

  public static String TABLE_NAME = "ARM_POS_RSV";
  public static String COL_AI_TRN = "ARM_POS_RSV.AI_TRN";
  public static String COL_ID_RESERVATION = "ARM_POS_RSV.ID_RESERVATION";
  public static String COL_EXP_DT = "ARM_POS_RSV.EXP_DT";
  public static String COL_REASON_CODE = "ARM_POS_RSV.REASON_CODE";
  public static String COL_DEPOSIT_AMT = "ARM_POS_RSV.DEPOSIT_AMT";
  public static String COL_ORIG_RSV_ID = "ARM_POS_RSV.ORIG_RSV_ID";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private String idReservation;
  private Date expDt;
  private String reasonCode;
  private ArmCurrency depositAmt;
  private String origRsvId;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getIdReservation() { return this.idReservation; }
  public void setIdReservation(String idReservation) { this.idReservation = idReservation; }

  public Date getExpDt() { return this.expDt; }
  public void setExpDt(Date expDt) { this.expDt = expDt; }

  public String getReasonCode() { return this.reasonCode; }
  public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }

  public ArmCurrency getDepositAmt() { return this.depositAmt; }
  public void setDepositAmt(ArmCurrency depositAmt) { this.depositAmt = depositAmt; }

  public String getOrigRsvId() { return this.origRsvId; }
  public void setOrigRsvId(String origRsvId) { this.origRsvId = origRsvId; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmPosRsvOracleBean bean = new ArmPosRsvOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.idReservation = getStringFromResultSet(rs, "ID_RESERVATION");
      bean.expDt = getDateFromResultSet(rs, "EXP_DT");
      bean.reasonCode = getStringFromResultSet(rs, "REASON_CODE");
      bean.depositAmt = getCurrencyFromResultSet(rs, "DEPOSIT_AMT");
      bean.origRsvId = getStringFromResultSet(rs, "ORIG_RSV_ID");
      list.add(bean);
    }
    return (ArmPosRsvOracleBean[]) list.toArray(new ArmPosRsvOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getIdReservation(), Types.VARCHAR);
    addToList(list, this.getExpDt(), Types.TIMESTAMP);
    addToList(list, this.getReasonCode(), Types.VARCHAR);
    addToList(list, this.getDepositAmt(), Types.VARCHAR);
    addToList(list, this.getOrigRsvId(), Types.VARCHAR);
    return list;
  }

}
