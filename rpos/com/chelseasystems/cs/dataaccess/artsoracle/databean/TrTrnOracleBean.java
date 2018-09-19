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
 * This class is an object representation of the Arts database table TR_TRN<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     ID_OPR -- VARCHAR2(128)<BR>
 *     TY_TRN -- VARCHAR2(60)<BR>
 *     TS_TM_SRT -- DATE(7)<BR>
 *     TS_TRN_BGN -- DATE(7)<BR>
 *     TS_TRN_END -- DATE(7)<BR>
 *     FL_TRG_TRN -- NUMBER(1)<BR>
 *     FL_KY_OFL -- NUMBER(1)<BR>
 *     TS_TRN_PST -- DATE(7)<BR>
 *     TS_TRN_SBM -- DATE(7)<BR>
 *     TS_TRN_CRT -- DATE(7)<BR>
 *     TS_TRN_PRC -- DATE(7)<BR>
 *     TY_GUI_TRN -- VARCHAR2(128)<BR>
 *     ID_VOID -- VARCHAR2(128)<BR>
 *     DE_HND_TCK -- VARCHAR2(255)<BR>
 *     NOT_VOID_REASON -- VARCHAR2(200)<BR>
 *     ID_RPSTY_TND -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     TRN_SEQ_NUM -- NUMBER(12)<BR>
 *     FISCAL_RECEIPT_NUMBER -- VARCHAR2(128)<BR>
 *     FISCAL_RECEIPT_DATE -- DATE(7)<BR>
 *
 */
public class TrTrnOracleBean extends BaseOracleBean {

  public TrTrnOracleBean() {}

  public static String selectSql = "select AI_TRN, ID_OPR, TY_TRN, TS_TM_SRT, TS_TRN_BGN, TS_TRN_END, FL_TRG_TRN, FL_KY_OFL, TS_TRN_PST, TS_TRN_SBM, TS_TRN_CRT, TS_TRN_PRC, TY_GUI_TRN, ID_VOID, DE_HND_TCK, NOT_VOID_REASON, ID_RPSTY_TND, ID_STR_RT, TRN_SEQ_NUM, FISCAL_RECEIPT_NUMBER, FISCAL_RECEIPT_DATE from TR_TRN ";
  public static String insertSql = "insert into TR_TRN (AI_TRN, ID_OPR, TY_TRN, TS_TM_SRT, TS_TRN_BGN, TS_TRN_END, FL_TRG_TRN, FL_KY_OFL, TS_TRN_PST, TS_TRN_SBM, TS_TRN_CRT, TS_TRN_PRC, TY_GUI_TRN, ID_VOID, DE_HND_TCK, NOT_VOID_REASON, ID_RPSTY_TND, ID_STR_RT, TRN_SEQ_NUM, FISCAL_RECEIPT_NUMBER, FISCAL_RECEIPT_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update TR_TRN set AI_TRN = ?, ID_OPR = ?, TY_TRN = ?, TS_TM_SRT = ?, TS_TRN_BGN = ?, TS_TRN_END = ?, FL_TRG_TRN = ?, FL_KY_OFL = ?, TS_TRN_PST = ?, TS_TRN_SBM = ?, TS_TRN_CRT = ?, TS_TRN_PRC = ?, TY_GUI_TRN = ?, ID_VOID = ?, DE_HND_TCK = ?, NOT_VOID_REASON = ?, ID_RPSTY_TND = ?, ID_STR_RT = ?, TRN_SEQ_NUM = ?, FISCAL_RECEIPT_NUMBER = ?, FISCAL_RECEIPT_DATE = ? ";
  public static String deleteSql = "delete from TR_TRN ";

  public static String TABLE_NAME = "TR_TRN";
  public static String COL_AI_TRN = "TR_TRN.AI_TRN";
  public static String COL_ID_OPR = "TR_TRN.ID_OPR";
  public static String COL_TY_TRN = "TR_TRN.TY_TRN";
  public static String COL_TS_TM_SRT = "TR_TRN.TS_TM_SRT";
  public static String COL_TS_TRN_BGN = "TR_TRN.TS_TRN_BGN";
  public static String COL_TS_TRN_END = "TR_TRN.TS_TRN_END";
  public static String COL_FL_TRG_TRN = "TR_TRN.FL_TRG_TRN";
  public static String COL_FL_KY_OFL = "TR_TRN.FL_KY_OFL";
  public static String COL_TS_TRN_PST = "TR_TRN.TS_TRN_PST";
  public static String COL_TS_TRN_SBM = "TR_TRN.TS_TRN_SBM";
  public static String COL_TS_TRN_CRT = "TR_TRN.TS_TRN_CRT";
  public static String COL_TS_TRN_PRC = "TR_TRN.TS_TRN_PRC";
  public static String COL_TY_GUI_TRN = "TR_TRN.TY_GUI_TRN";
  public static String COL_ID_VOID = "TR_TRN.ID_VOID";
  public static String COL_DE_HND_TCK = "TR_TRN.DE_HND_TCK";
  public static String COL_NOT_VOID_REASON = "TR_TRN.NOT_VOID_REASON";
  public static String COL_ID_RPSTY_TND = "TR_TRN.ID_RPSTY_TND";
  public static String COL_ID_STR_RT = "TR_TRN.ID_STR_RT";
  public static String COL_TRN_SEQ_NUM = "TR_TRN.TRN_SEQ_NUM";
  public static String COL_FISCAL_RECEIPT_NUMBER = "TR_TRN.FISCAL_RECEIPT_NUMBER";
  public static String COL_FISCAL_RECEIPT_DATE = "TR_TRN.FISCAL_RECEIPT_DATE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private String idOpr;
  private String tyTrn;
  private Date tsTmSrt;
  private Date tsTrnBgn;
  private Date tsTrnEnd;
  private Boolean flTrgTrn;
  private Boolean flKyOfl;
  private Date tsTrnPst;
  private Date tsTrnSbm;
  private Date tsTrnCrt;
  private Date tsTrnPrc;
  private String tyGuiTrn;
  private String idVoid;
  private String deHndTck;
  private String notVoidReason;
  private String idRpstyTnd;
  private String idStrRt;
  private Long trnSeqNum;
  private String fiscalReceiptNumber;
  private Date fiscalReceiptDate;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getIdOpr() { return this.idOpr; }
  public void setIdOpr(String idOpr) { this.idOpr = idOpr; }

  public String getTyTrn() { return this.tyTrn; }
  public void setTyTrn(String tyTrn) { this.tyTrn = tyTrn; }

  public Date getTsTmSrt() { return this.tsTmSrt; }
  public void setTsTmSrt(Date tsTmSrt) { this.tsTmSrt = tsTmSrt; }

  public Date getTsTrnBgn() { return this.tsTrnBgn; }
  public void setTsTrnBgn(Date tsTrnBgn) { this.tsTrnBgn = tsTrnBgn; }

  public Date getTsTrnEnd() { return this.tsTrnEnd; }
  public void setTsTrnEnd(Date tsTrnEnd) { this.tsTrnEnd = tsTrnEnd; }

  public Boolean getFlTrgTrn() { return this.flTrgTrn; }
  public void setFlTrgTrn(Boolean flTrgTrn) { this.flTrgTrn = flTrgTrn; }
  public void setFlTrgTrn(boolean flTrgTrn) { this.flTrgTrn = new Boolean(flTrgTrn); }

  public Boolean getFlKyOfl() { return this.flKyOfl; }
  public void setFlKyOfl(Boolean flKyOfl) { this.flKyOfl = flKyOfl; }
  public void setFlKyOfl(boolean flKyOfl) { this.flKyOfl = new Boolean(flKyOfl); }

  public Date getTsTrnPst() { return this.tsTrnPst; }
  public void setTsTrnPst(Date tsTrnPst) { this.tsTrnPst = tsTrnPst; }

  public Date getTsTrnSbm() { return this.tsTrnSbm; }
  public void setTsTrnSbm(Date tsTrnSbm) { this.tsTrnSbm = tsTrnSbm; }

  public Date getTsTrnCrt() { return this.tsTrnCrt; }
  public void setTsTrnCrt(Date tsTrnCrt) { this.tsTrnCrt = tsTrnCrt; }

  public Date getTsTrnPrc() { return this.tsTrnPrc; }
  public void setTsTrnPrc(Date tsTrnPrc) { this.tsTrnPrc = tsTrnPrc; }

  public String getTyGuiTrn() { return this.tyGuiTrn; }
  public void setTyGuiTrn(String tyGuiTrn) { this.tyGuiTrn = tyGuiTrn; }

  public String getIdVoid() { return this.idVoid; }
  public void setIdVoid(String idVoid) { this.idVoid = idVoid; }

  public String getDeHndTck() { return this.deHndTck; }
  public void setDeHndTck(String deHndTck) { this.deHndTck = deHndTck; }

  public String getNotVoidReason() { return this.notVoidReason; }
  public void setNotVoidReason(String notVoidReason) { this.notVoidReason = notVoidReason; }

  public String getIdRpstyTnd() { return this.idRpstyTnd; }
  public void setIdRpstyTnd(String idRpstyTnd) { this.idRpstyTnd = idRpstyTnd; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public Long getTrnSeqNum() { return this.trnSeqNum; }
  public void setTrnSeqNum(Long trnSeqNum) { this.trnSeqNum = trnSeqNum; }
  public void setTrnSeqNum(long trnSeqNum) { this.trnSeqNum = new Long(trnSeqNum); }
  public void setTrnSeqNum(int trnSeqNum) { this.trnSeqNum = new Long((long)trnSeqNum); }

  public String getFiscalReceiptNumber() { return this.fiscalReceiptNumber; }
  public void setFiscalReceiptNumber(String fiscalReceiptNumber) { this.fiscalReceiptNumber = fiscalReceiptNumber; }

  public Date getFiscalReceiptDate() { return this.fiscalReceiptDate; }
  public void setFiscalReceiptDate(Date fiscalReceiptDate) { this.fiscalReceiptDate = fiscalReceiptDate; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      TrTrnOracleBean bean = new TrTrnOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.idOpr = getStringFromResultSet(rs, "ID_OPR");
      bean.tyTrn = getStringFromResultSet(rs, "TY_TRN");
      bean.tsTmSrt = getDateFromResultSet(rs, "TS_TM_SRT");
      bean.tsTrnBgn = getDateFromResultSet(rs, "TS_TRN_BGN");
      bean.tsTrnEnd = getDateFromResultSet(rs, "TS_TRN_END");
      bean.flTrgTrn = getBooleanFromResultSet(rs, "FL_TRG_TRN");
      bean.flKyOfl = getBooleanFromResultSet(rs, "FL_KY_OFL");
      bean.tsTrnPst = getDateFromResultSet(rs, "TS_TRN_PST");
      bean.tsTrnSbm = getDateFromResultSet(rs, "TS_TRN_SBM");
      bean.tsTrnCrt = getDateFromResultSet(rs, "TS_TRN_CRT");
      bean.tsTrnPrc = getDateFromResultSet(rs, "TS_TRN_PRC");
      bean.tyGuiTrn = getStringFromResultSet(rs, "TY_GUI_TRN");
      bean.idVoid = getStringFromResultSet(rs, "ID_VOID");
      bean.deHndTck = getStringFromResultSet(rs, "DE_HND_TCK");
      bean.notVoidReason = getStringFromResultSet(rs, "NOT_VOID_REASON");
      bean.idRpstyTnd = getStringFromResultSet(rs, "ID_RPSTY_TND");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.trnSeqNum = getLongFromResultSet(rs, "TRN_SEQ_NUM");
      bean.fiscalReceiptNumber = getStringFromResultSet(rs, "FISCAL_RECEIPT_NUMBER");
      bean.fiscalReceiptDate = getDateFromResultSet(rs, "FISCAL_RECEIPT_DATE");
      list.add(bean);
    }
    return (TrTrnOracleBean[]) list.toArray(new TrTrnOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getIdOpr(), Types.VARCHAR);
    addToList(list, this.getTyTrn(), Types.VARCHAR);
    addToList(list, this.getTsTmSrt(), Types.TIMESTAMP);
    addToList(list, this.getTsTrnBgn(), Types.TIMESTAMP);
    addToList(list, this.getTsTrnEnd(), Types.TIMESTAMP);
    addToList(list, this.getFlTrgTrn(), Types.DECIMAL);
    addToList(list, this.getFlKyOfl(), Types.DECIMAL);
    addToList(list, this.getTsTrnPst(), Types.TIMESTAMP);
    addToList(list, this.getTsTrnSbm(), Types.TIMESTAMP);
    addToList(list, this.getTsTrnCrt(), Types.TIMESTAMP);
    addToList(list, this.getTsTrnPrc(), Types.TIMESTAMP);
    addToList(list, this.getTyGuiTrn(), Types.VARCHAR);
    addToList(list, this.getIdVoid(), Types.VARCHAR);
    addToList(list, this.getDeHndTck(), Types.VARCHAR);
    addToList(list, this.getNotVoidReason(), Types.VARCHAR);
    addToList(list, this.getIdRpstyTnd(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getTrnSeqNum(), Types.DECIMAL);
    addToList(list, this.getFiscalReceiptNumber(), Types.VARCHAR);
    addToList(list, this.getFiscalReceiptDate(), Types.TIMESTAMP);
    return list;
  }

}
