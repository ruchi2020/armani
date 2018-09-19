
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
 * This class is an object representation of the Arts database table TR_LTM_GF_CF_TND<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     ID_STR_ISSG -- VARCHAR2(128)<BR>
 *     ID_NMB_SRZ_GF_CF -- VARCHAR2(40)<BR>
 *     CONTROL_NUMBER -- VARCHAR2(200)<BR>
 *     AUDIT_NOTE -- VARCHAR2(200)<BR>
 *     TYPE -- VARCHAR2(50)<BR>
 *     ISSUE_AMOUNT -- VARCHAR2(75)<BR>
 *     CREATE_DATE -- DATE(7)<BR>
 *     FIRST_NAME -- VARCHAR2(200)<BR>
 *     LAST_NAME -- VARCHAR2(200)<BR>
 *     PHONE_NUMBER -- VARCHAR2(50)<BR>
 *     LU_CLS_TND -- VARCHAR2(40)<BR>
 *     MANUAL_AUTH_CODE -- VARCHAR2(50)<BR>
 *     FL_MANUAL -- CHAR(1)<BR>
 *
 */
public class TrLtmGfCfTndOracleBean extends BaseOracleBean {

  public TrLtmGfCfTndOracleBean() {}

  public static String selectSql = "select AI_TRN, AI_LN_ITM, ID_STR_ISSG, ID_NMB_SRZ_GF_CF, CONTROL_NUMBER, AUDIT_NOTE, TYPE, ISSUE_AMOUNT, CREATE_DATE, FIRST_NAME, LAST_NAME, PHONE_NUMBER, LU_CLS_TND, MANUAL_AUTH_CODE, FL_MANUAL, AJB_SEQ from TR_LTM_GF_CF_TND ";
  public static String insertSql = "insert into TR_LTM_GF_CF_TND (AI_TRN, AI_LN_ITM, ID_STR_ISSG, ID_NMB_SRZ_GF_CF, CONTROL_NUMBER, AUDIT_NOTE, TYPE, ISSUE_AMOUNT, CREATE_DATE, FIRST_NAME, LAST_NAME, PHONE_NUMBER, LU_CLS_TND, MANUAL_AUTH_CODE, FL_MANUAL, AJB_SEQ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update TR_LTM_GF_CF_TND set AI_TRN = ?, AI_LN_ITM = ?, ID_STR_ISSG = ?, ID_NMB_SRZ_GF_CF = ?, CONTROL_NUMBER = ?, AUDIT_NOTE = ?, TYPE = ?, ISSUE_AMOUNT = ?, CREATE_DATE = ?, FIRST_NAME = ?, LAST_NAME = ?, PHONE_NUMBER = ?, LU_CLS_TND = ?, MANUAL_AUTH_CODE = ?, FL_MANUAL = ?, AJB_SEQ = ?";
  public static String deleteSql = "delete from TR_LTM_GF_CF_TND ";

  public static String TABLE_NAME = "TR_LTM_GF_CF_TND";
  public static String COL_AI_TRN = "TR_LTM_GF_CF_TND.AI_TRN";
  public static String COL_AI_LN_ITM = "TR_LTM_GF_CF_TND.AI_LN_ITM";
  public static String COL_ID_STR_ISSG = "TR_LTM_GF_CF_TND.ID_STR_ISSG";
  public static String COL_ID_NMB_SRZ_GF_CF = "TR_LTM_GF_CF_TND.ID_NMB_SRZ_GF_CF";
  public static String COL_CONTROL_NUMBER = "TR_LTM_GF_CF_TND.CONTROL_NUMBER";
  public static String COL_AUDIT_NOTE = "TR_LTM_GF_CF_TND.AUDIT_NOTE";
  public static String COL_TYPE = "TR_LTM_GF_CF_TND.TYPE";
  public static String COL_ISSUE_AMOUNT = "TR_LTM_GF_CF_TND.ISSUE_AMOUNT";
  public static String COL_CREATE_DATE = "TR_LTM_GF_CF_TND.CREATE_DATE";
  public static String COL_FIRST_NAME = "TR_LTM_GF_CF_TND.FIRST_NAME";
  public static String COL_LAST_NAME = "TR_LTM_GF_CF_TND.LAST_NAME";
  public static String COL_PHONE_NUMBER = "TR_LTM_GF_CF_TND.PHONE_NUMBER";
  public static String COL_LU_CLS_TND = "TR_LTM_GF_CF_TND.LU_CLS_TND";
  public static String COL_MANUAL_AUTH_CODE = "TR_LTM_GF_CF_TND.MANUAL_AUTH_CODE";
  public static String COL_FL_MANUAL = "TR_LTM_GF_CF_TND.FL_MANUAL";
//Vivek Mishra : Added to capture AJB sequence 02-NOV-2016
  public static String COL_AJB_SEQ = "TR_LTM_GF_CF_TND.AJB_SEQ";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long aiLnItm;
  private String idStrIssg;
  private String idNmbSrzGfCf;
  private String controlNumber;
  private String auditNote;
  private String type;
  private ArmCurrency issueAmount;
  private Date createDate;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String luClsTnd;
  private String manualAuthCode;
  private String flManual;
  //Vivek Mishra : Added to capture AJB sequence 02-NOV-2016
  private String ajbSeq;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public String getIdStrIssg() { return this.idStrIssg; }
  public void setIdStrIssg(String idStrIssg) { this.idStrIssg = idStrIssg; }

  public String getIdNmbSrzGfCf() { return this.idNmbSrzGfCf; }
  public void setIdNmbSrzGfCf(String idNmbSrzGfCf) { this.idNmbSrzGfCf = idNmbSrzGfCf; }

  public String getControlNumber() { return this.controlNumber; }
  public void setControlNumber(String controlNumber) { this.controlNumber = controlNumber; }

  public String getAuditNote() { return this.auditNote; }
  public void setAuditNote(String auditNote) { this.auditNote = auditNote; }

  public String getType() { return this.type; }
  public void setType(String type) { this.type = type; }

  public ArmCurrency getIssueAmount() { return this.issueAmount; }
  public void setIssueAmount(ArmCurrency issueAmount) { this.issueAmount = issueAmount; }

  public Date getCreateDate() { return this.createDate; }
  public void setCreateDate(Date createDate) { this.createDate = createDate; }

  public String getFirstName() { return this.firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return this.lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getPhoneNumber() { return this.phoneNumber; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

  public String getLuClsTnd() { return this.luClsTnd; }
  public void setLuClsTnd(String luClsTnd) { this.luClsTnd = luClsTnd; }

  public String getManualAuthCode() { return this.manualAuthCode; }
  public void setManualAuthCode(String manualAuthCode) { this.manualAuthCode = manualAuthCode; }

  public String getFlManual() { return this.flManual; }
  public void setFlManual(String flManual) { this.flManual = flManual; }
//  public void setFlManual(boolean flManual) { this.flManual = new String(flManual); }
  
//Vivek Mishra : Added to capture AJB sequence 02-NOV-2016
  public String getAjbSeq() { return ajbSeq; }
  public void setAjbSeq(String ajbSeq) { this.ajbSeq = ajbSeq; }  
//Ends here 02-NOV-2016
  
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      TrLtmGfCfTndOracleBean bean = new TrLtmGfCfTndOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.idStrIssg = getStringFromResultSet(rs, "ID_STR_ISSG");
      bean.idNmbSrzGfCf = getStringFromResultSet(rs, "ID_NMB_SRZ_GF_CF");
      bean.controlNumber = getStringFromResultSet(rs, "CONTROL_NUMBER");
      bean.auditNote = getStringFromResultSet(rs, "AUDIT_NOTE");
      bean.type = getStringFromResultSet(rs, "TYPE");
      bean.issueAmount = getCurrencyFromResultSet(rs, "ISSUE_AMOUNT");
      bean.createDate = getDateFromResultSet(rs, "CREATE_DATE");
      bean.firstName = getStringFromResultSet(rs, "FIRST_NAME");
      bean.lastName = getStringFromResultSet(rs, "LAST_NAME");
      bean.phoneNumber = getStringFromResultSet(rs, "PHONE_NUMBER");
      bean.luClsTnd = getStringFromResultSet(rs, "LU_CLS_TND");
      bean.manualAuthCode = getStringFromResultSet(rs, "MANUAL_AUTH_CODE");
      bean.flManual = getStringFromResultSet(rs, "FL_MANUAL");
      bean.ajbSeq = getStringFromResultSet(rs, "AJB_SEQ");
      list.add(bean);
    }
    return (TrLtmGfCfTndOracleBean[]) list.toArray(new TrLtmGfCfTndOracleBean[0]);
  }


public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getIdStrIssg(), Types.VARCHAR);
    addToList(list, this.getIdNmbSrzGfCf(), Types.VARCHAR);
    addToList(list, this.getControlNumber(), Types.VARCHAR);
    addToList(list, this.getAuditNote(), Types.VARCHAR);
    addToList(list, this.getType(), Types.VARCHAR);
    addToList(list, this.getIssueAmount(), Types.VARCHAR);
    addToList(list, this.getCreateDate(), Types.TIMESTAMP);
    addToList(list, this.getFirstName(), Types.VARCHAR);
    addToList(list, this.getLastName(), Types.VARCHAR);
    addToList(list, this.getPhoneNumber(), Types.VARCHAR);
    addToList(list, this.getLuClsTnd(), Types.VARCHAR);
    addToList(list, this.getManualAuthCode(), Types.VARCHAR);
    addToList(list, this.getFlManual(), Types.DECIMAL);
    addToList(list, this.getAjbSeq(), Types.VARCHAR);
    return list;
  }

}
