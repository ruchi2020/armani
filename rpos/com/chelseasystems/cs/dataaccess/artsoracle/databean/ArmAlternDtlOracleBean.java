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
 * This class is an object representation of the Arts database table ARM_ALTERN_DTL<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     DET_SEQ_NUM(PK) -- NUMBER(3)<BR>
 *     ALTERN_SEQ_NUM(PK) -- NUMBER(3)<BR>
 *     CD_ALTERN(PK) -- VARCHAR2(128)<BR>
 *     DESCRIPTION -- VARCHAR2(128)<BR>
 *     ESTIMATED_PRICE -- VARCHAR2(128)<BR>
 *     ESTIMATED_TIME -- DATE(7)<BR>
 *     ACTUAL_PRICE -- VARCHAR2(128)<BR>
 *     ACTUAL_TIME -- DATE(7)<BR>
 *
 */
public class ArmAlternDtlOracleBean extends BaseOracleBean {

  public ArmAlternDtlOracleBean() {}

  public static String selectSql = "select AI_TRN, AI_LN_ITM, DET_SEQ_NUM, ALTERN_SEQ_NUM, CD_ALTERN, DESCRIPTION, ESTIMATED_PRICE, ESTIMATED_TIME, ACTUAL_PRICE, ACTUAL_TIME from ARM_ALTERN_DTL ";
  public static String insertSql = "insert into ARM_ALTERN_DTL (AI_TRN, AI_LN_ITM, DET_SEQ_NUM, ALTERN_SEQ_NUM, CD_ALTERN, DESCRIPTION, ESTIMATED_PRICE, ESTIMATED_TIME, ACTUAL_PRICE, ACTUAL_TIME) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_ALTERN_DTL set AI_TRN = ?, AI_LN_ITM = ?, DET_SEQ_NUM = ?, ALTERN_SEQ_NUM = ?, CD_ALTERN = ?, DESCRIPTION = ?, ESTIMATED_PRICE = ?, ESTIMATED_TIME = ?, ACTUAL_PRICE = ?, ACTUAL_TIME = ? ";
  public static String deleteSql = "delete from ARM_ALTERN_DTL ";

  public static String TABLE_NAME = "ARM_ALTERN_DTL";
  public static String COL_AI_TRN = "ARM_ALTERN_DTL.AI_TRN";
  public static String COL_AI_LN_ITM = "ARM_ALTERN_DTL.AI_LN_ITM";
  public static String COL_DET_SEQ_NUM = "ARM_ALTERN_DTL.DET_SEQ_NUM";
  public static String COL_ALTERN_SEQ_NUM = "ARM_ALTERN_DTL.ALTERN_SEQ_NUM";
  public static String COL_CD_ALTERN = "ARM_ALTERN_DTL.CD_ALTERN";
  public static String COL_DESCRIPTION = "ARM_ALTERN_DTL.DESCRIPTION";
  public static String COL_ESTIMATED_PRICE = "ARM_ALTERN_DTL.ESTIMATED_PRICE";
  public static String COL_ESTIMATED_TIME = "ARM_ALTERN_DTL.ESTIMATED_TIME";
  public static String COL_ACTUAL_PRICE = "ARM_ALTERN_DTL.ACTUAL_PRICE";
  public static String COL_ACTUAL_TIME = "ARM_ALTERN_DTL.ACTUAL_TIME";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long aiLnItm;
  private Long detSeqNum;
  private Long alternSeqNum;
  private String cdAltern;
  private String description;
  private ArmCurrency estimatedPrice;
  private Date estimatedTime;
  private ArmCurrency actualPrice;
  private Date actualTime;

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

  public Long getAlternSeqNum() { return this.alternSeqNum; }
  public void setAlternSeqNum(Long alternSeqNum) { this.alternSeqNum = alternSeqNum; }
  public void setAlternSeqNum(long alternSeqNum) { this.alternSeqNum = new Long(alternSeqNum); }
  public void setAlternSeqNum(int alternSeqNum) { this.alternSeqNum = new Long((long)alternSeqNum); }

  public String getCdAltern() { return this.cdAltern; }
  public void setCdAltern(String cdAltern) { this.cdAltern = cdAltern; }

  public String getDescription() { return this.description; }
  public void setDescription(String description) { this.description = description; }

  public ArmCurrency getEstimatedPrice() { return this.estimatedPrice; }
  public void setEstimatedPrice(ArmCurrency estimatedPrice) { this.estimatedPrice = estimatedPrice; }

  public Date getEstimatedTime() { return this.estimatedTime; }
  public void setEstimatedTime(Date estimatedTime) { this.estimatedTime = estimatedTime; }

  public ArmCurrency getActualPrice() { return this.actualPrice; }
  public void setActualPrice(ArmCurrency actualPrice) { this.actualPrice = actualPrice; }

  public Date getActualTime() { return this.actualTime; }
  public void setActualTime(Date actualTime) { this.actualTime = actualTime; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmAlternDtlOracleBean bean = new ArmAlternDtlOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.detSeqNum = getLongFromResultSet(rs, "DET_SEQ_NUM");
      bean.alternSeqNum = getLongFromResultSet(rs, "ALTERN_SEQ_NUM");
      bean.cdAltern = getStringFromResultSet(rs, "CD_ALTERN");
      bean.description = getStringFromResultSet(rs, "DESCRIPTION");
      bean.estimatedPrice = getCurrencyFromResultSet(rs, "ESTIMATED_PRICE");
      bean.estimatedTime = getDateFromResultSet(rs, "ESTIMATED_TIME");
      bean.actualPrice = getCurrencyFromResultSet(rs, "ACTUAL_PRICE");
      bean.actualTime = getDateFromResultSet(rs, "ACTUAL_TIME");
      list.add(bean);
    }
    return (ArmAlternDtlOracleBean[]) list.toArray(new ArmAlternDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getDetSeqNum(), Types.DECIMAL);
    addToList(list, this.getAlternSeqNum(), Types.DECIMAL);
    addToList(list, this.getCdAltern(), Types.VARCHAR);
    addToList(list, this.getDescription(), Types.VARCHAR);
    addToList(list, this.getEstimatedPrice(), Types.VARCHAR);
    addToList(list, this.getEstimatedTime(), Types.TIMESTAMP);
    addToList(list, this.getActualPrice(), Types.VARCHAR);
    addToList(list, this.getActualTime(), Types.TIMESTAMP);
    return list;
  }

}
