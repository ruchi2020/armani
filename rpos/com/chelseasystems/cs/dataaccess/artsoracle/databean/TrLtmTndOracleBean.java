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
 * This class is an object representation of the Arts database table TR_LTM_TND<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     TY_TND -- VARCHAR2(40)<BR>
 *     ID_ACNT_NMB -- VARCHAR2(128)<BR>
 *     ID_ACNT_TND -- VARCHAR2(128)<BR>
 *     MO_ITM_LN_TND -- VARCHAR2(75)<BR>
 *     LU_CLS_TND -- VARCHAR2(40)<BR>
 *     JOURNAL_KEY -- VARCHAR2(16)<BR>
 *     RES_MSG -- VARCHAR2(38)<BR>
 *     MSG_NUM -- VARCHAR2(2)<BR>
 *     MERCHANT_ID -- VARCHAR2(15)<BR>
 *     CODE -- VARCHAR2(20)<BR>
 *
 */
public class TrLtmTndOracleBean extends BaseOracleBean {

  public TrLtmTndOracleBean() {}

  public static String selectSql = "select AI_TRN, AI_LN_ITM, TY_TND, ID_ACNT_NMB, ID_ACNT_TND, MO_ITM_LN_TND, LU_CLS_TND, JOURNAL_KEY, RES_MSG, MSG_NUM, MERCHANT_ID, CODE from TR_LTM_TND ";
  public static String insertSql = "insert into TR_LTM_TND (AI_TRN, AI_LN_ITM, TY_TND, ID_ACNT_NMB, ID_ACNT_TND, MO_ITM_LN_TND, LU_CLS_TND, JOURNAL_KEY, RES_MSG, MSG_NUM, MERCHANT_ID, CODE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update TR_LTM_TND set AI_TRN = ?, AI_LN_ITM = ?, TY_TND = ?, ID_ACNT_NMB = ?, ID_ACNT_TND = ?, MO_ITM_LN_TND = ?, LU_CLS_TND = ?, JOURNAL_KEY = ?, RES_MSG = ?, MSG_NUM = ?, MERCHANT_ID = ?, CODE = ? ";
  public static String deleteSql = "delete from TR_LTM_TND ";

  public static String TABLE_NAME = "TR_LTM_TND";
  public static String COL_AI_TRN = "TR_LTM_TND.AI_TRN";
  public static String COL_AI_LN_ITM = "TR_LTM_TND.AI_LN_ITM";
  public static String COL_TY_TND = "TR_LTM_TND.TY_TND";
  public static String COL_ID_ACNT_NMB = "TR_LTM_TND.ID_ACNT_NMB";
  public static String COL_ID_ACNT_TND = "TR_LTM_TND.ID_ACNT_TND";
  public static String COL_MO_ITM_LN_TND = "TR_LTM_TND.MO_ITM_LN_TND";
  public static String COL_LU_CLS_TND = "TR_LTM_TND.LU_CLS_TND";
  public static String COL_JOURNAL_KEY = "TR_LTM_TND.JOURNAL_KEY";
  public static String COL_RES_MSG = "TR_LTM_TND.RES_MSG";
  public static String COL_MSG_NUM = "TR_LTM_TND.MSG_NUM";
  public static String COL_MERCHANT_ID = "TR_LTM_TND.MERCHANT_ID";
  public static String COL_CODE = "TR_LTM_TND.CODE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long aiLnItm;
  private String tyTnd;
  private String idAcntNmb;
  private String idAcntTnd;
  private ArmCurrency moItmLnTnd;
  private String luClsTnd;
  private String journalKey;
  private String resMsg;
  private String msgNum;
  private String merchantId;
  private String code;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public String getTyTnd() { return this.tyTnd; }
  public void setTyTnd(String tyTnd) { this.tyTnd = tyTnd; }

  public String getIdAcntNmb() { return this.idAcntNmb; }
  public void setIdAcntNmb(String idAcntNmb) { this.idAcntNmb = idAcntNmb; }

  public String getIdAcntTnd() { return this.idAcntTnd; }
  public void setIdAcntTnd(String idAcntTnd) { this.idAcntTnd = idAcntTnd; }

  public ArmCurrency getMoItmLnTnd() { return this.moItmLnTnd; }
  public void setMoItmLnTnd(ArmCurrency moItmLnTnd) { this.moItmLnTnd = moItmLnTnd; }

  public String getLuClsTnd() { return this.luClsTnd; }
  public void setLuClsTnd(String luClsTnd) { this.luClsTnd = luClsTnd; }

  public String getJournalKey() { return this.journalKey; }
  public void setJournalKey(String journalKey) { this.journalKey = journalKey; }

  public String getResMsg() { return this.resMsg; }
  public void setResMsg(String resMsg) { this.resMsg = resMsg; }

  public String getMsgNum() { return this.msgNum; }
  public void setMsgNum(String msgNum) { this.msgNum = msgNum; }

  public String getMerchantId() { return this.merchantId; }
  public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

  public String getCode() { return this.code; }
  public void setCode(String code) { this.code = code; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      TrLtmTndOracleBean bean = new TrLtmTndOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.tyTnd = getStringFromResultSet(rs, "TY_TND");
      bean.idAcntNmb = getStringFromResultSet(rs, "ID_ACNT_NMB");
      bean.idAcntTnd = getStringFromResultSet(rs, "ID_ACNT_TND");
      bean.moItmLnTnd = getCurrencyFromResultSet(rs, "MO_ITM_LN_TND");
      bean.luClsTnd = getStringFromResultSet(rs, "LU_CLS_TND");
      bean.journalKey = getStringFromResultSet(rs, "JOURNAL_KEY");
      bean.resMsg = getStringFromResultSet(rs, "RES_MSG");
      bean.msgNum = getStringFromResultSet(rs, "MSG_NUM");
      bean.merchantId = getStringFromResultSet(rs, "MERCHANT_ID");
      bean.code = getStringFromResultSet(rs, "CODE");
      list.add(bean);
    }
    return (TrLtmTndOracleBean[]) list.toArray(new TrLtmTndOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getTyTnd(), Types.VARCHAR);
    addToList(list, this.getIdAcntNmb(), Types.VARCHAR);
    addToList(list, this.getIdAcntTnd(), Types.VARCHAR);
    addToList(list, this.getMoItmLnTnd(), Types.VARCHAR);
    addToList(list, this.getLuClsTnd(), Types.VARCHAR);
    addToList(list, this.getJournalKey(), Types.VARCHAR);
    addToList(list, this.getResMsg(), Types.VARCHAR);
    addToList(list, this.getMsgNum(), Types.VARCHAR);
    addToList(list, this.getMerchantId(), Types.VARCHAR);
    addToList(list, this.getCode(), Types.VARCHAR);
    return list;
  }

}
