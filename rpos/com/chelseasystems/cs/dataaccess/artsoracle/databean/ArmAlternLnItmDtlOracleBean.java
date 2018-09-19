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
 * This class is an object representation of the Arts database table ARM_ALTERN_LN_ITM_DTL<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     DET_SEQ_NUM(PK) -- NUMBER(3)<BR>
 *     SEQ_NUM(PK) -- NUMBER(3)<BR>
 *     ID_ALTERN -- VARCHAR2(128)<BR>
 *     TRY_DT -- DATE(7)<BR>
 *     PROMISE_DT -- DATE(7)<BR>
 *     FITTER_ID -- VARCHAR2(128)<BR>
 *     TAILOR -- VARCHAR2(128)<BR>
 *     TOTAL_PRICE -- VARCHAR2(128)<BR>
 *     COMMENTS -- VARCHAR2(150)<BR>
 *
 */
public class ArmAlternLnItmDtlOracleBean extends BaseOracleBean {

  public ArmAlternLnItmDtlOracleBean() {}

  public static String selectSql = "select AI_TRN, AI_LN_ITM, DET_SEQ_NUM, SEQ_NUM, ID_ALTERN, TRY_DT, PROMISE_DT, FITTER_ID, TAILOR, TOTAL_PRICE, COMMENTS from ARM_ALTERN_LN_ITM_DTL ";
  public static String insertSql = "insert into ARM_ALTERN_LN_ITM_DTL (AI_TRN, AI_LN_ITM, DET_SEQ_NUM, SEQ_NUM, ID_ALTERN, TRY_DT, PROMISE_DT, FITTER_ID, TAILOR, TOTAL_PRICE, COMMENTS) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_ALTERN_LN_ITM_DTL set AI_TRN = ?, AI_LN_ITM = ?, DET_SEQ_NUM = ?, SEQ_NUM = ?, ID_ALTERN = ?, TRY_DT = ?, PROMISE_DT = ?, FITTER_ID = ?, TAILOR = ?, TOTAL_PRICE = ?, COMMENTS = ? ";
  public static String deleteSql = "delete from ARM_ALTERN_LN_ITM_DTL ";

  public static String TABLE_NAME = "ARM_ALTERN_LN_ITM_DTL";
  public static String COL_AI_TRN = "ARM_ALTERN_LN_ITM_DTL.AI_TRN";
  public static String COL_AI_LN_ITM = "ARM_ALTERN_LN_ITM_DTL.AI_LN_ITM";
  public static String COL_DET_SEQ_NUM = "ARM_ALTERN_LN_ITM_DTL.DET_SEQ_NUM";
  public static String COL_SEQ_NUM = "ARM_ALTERN_LN_ITM_DTL.SEQ_NUM";
  public static String COL_ID_ALTERN = "ARM_ALTERN_LN_ITM_DTL.ID_ALTERN";
  public static String COL_TRY_DT = "ARM_ALTERN_LN_ITM_DTL.TRY_DT";
  public static String COL_PROMISE_DT = "ARM_ALTERN_LN_ITM_DTL.PROMISE_DT";
  public static String COL_FITTER_ID = "ARM_ALTERN_LN_ITM_DTL.FITTER_ID";
  public static String COL_TAILOR = "ARM_ALTERN_LN_ITM_DTL.TAILOR";
  public static String COL_TOTAL_PRICE = "ARM_ALTERN_LN_ITM_DTL.TOTAL_PRICE";
  public static String COL_COMMENTS = "ARM_ALTERN_LN_ITM_DTL.COMMENTS";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long aiLnItm;
  private Long detSeqNum;
  private Long seqNum;
  private String idAltern;
  private Date tryDt;
  private Date promiseDt;
  private String fitterId;
  private String tailor;
  private ArmCurrency totalPrice;
  private String comments;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public Long getDetSeqNum() { return this.detSeqNum; }
  public void setDetSeqNum(Long detSeqNum) { this.detSeqNum = detSeqNum; }
  public void setDetSeqNum(long detSeqNum) { this.detSeqNum = new Long(detSeqNum); }
  public void setDetSeqNum(int detSeqNum) { this.detSeqNum = new Long((long)detSeqNum); }

  public Long getSeqNum() { return this.seqNum; }
  public void setSeqNum(Long seqNum) { this.seqNum = seqNum; }
  public void setSeqNum(long seqNum) { this.seqNum = new Long(seqNum); }
  public void setSeqNum(int seqNum) { this.seqNum = new Long((long)seqNum); }

  public String getIdAltern() { return this.idAltern; }
  public void setIdAltern(String idAltern) { this.idAltern = idAltern; }

  public Date getTryDt() { return this.tryDt; }
  public void setTryDt(Date tryDt) { this.tryDt = tryDt; }

  public Date getPromiseDt() { return this.promiseDt; }
  public void setPromiseDt(Date promiseDt) { this.promiseDt = promiseDt; }

  public String getFitterId() { return this.fitterId; }
  public void setFitterId(String fitterId) { this.fitterId = fitterId; }

  public String getTailor() { return this.tailor; }
  public void setTailor(String tailor) { this.tailor = tailor; }

  public ArmCurrency getTotalPrice() { return this.totalPrice; }
  public void setTotalPrice(ArmCurrency totalPrice) { this.totalPrice = totalPrice; }

  public String getComments() { return this.comments; }
  public void setComments(String comments) { this.comments = comments; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmAlternLnItmDtlOracleBean bean = new ArmAlternLnItmDtlOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.detSeqNum = getLongFromResultSet(rs, "DET_SEQ_NUM");
      bean.seqNum = getLongFromResultSet(rs, "SEQ_NUM");
      bean.idAltern = getStringFromResultSet(rs, "ID_ALTERN");
      bean.tryDt = getDateFromResultSet(rs, "TRY_DT");
      bean.promiseDt = getDateFromResultSet(rs, "PROMISE_DT");
      bean.fitterId = getStringFromResultSet(rs, "FITTER_ID");
      bean.tailor = getStringFromResultSet(rs, "TAILOR");
      bean.totalPrice = getCurrencyFromResultSet(rs, "TOTAL_PRICE");
      bean.comments = getStringFromResultSet(rs, "COMMENTS");
      list.add(bean);
    }
    return (ArmAlternLnItmDtlOracleBean[]) list.toArray(new ArmAlternLnItmDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getDetSeqNum(), Types.DECIMAL);
    addToList(list, this.getSeqNum(), Types.DECIMAL);
    addToList(list, this.getIdAltern(), Types.VARCHAR);
    addToList(list, this.getTryDt(), Types.TIMESTAMP);
    addToList(list, this.getPromiseDt(), Types.TIMESTAMP);
    addToList(list, this.getFitterId(), Types.VARCHAR);
    addToList(list, this.getTailor(), Types.VARCHAR);
    addToList(list, this.getTotalPrice(), Types.VARCHAR);
    addToList(list, this.getComments(), Types.VARCHAR);
    return list;
  }

}
