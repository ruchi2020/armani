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
 * This class is an object representation of the Arts database table ARM_RSV_POS_LN_ITM_DTL<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     SEQUENCE_NUM(PK) -- NUMBER(3)<BR>
 *     ORG_AI_TRN -- VARCHAR2(128)<BR>
 *     ORIG_AI_LN_ITM -- NUMBER(22)<BR>
 *     ORIG_SEQ_NUM -- NUMBER(3)<BR>
 *
 */
public class ArmRsvPosLnItmDtlOracleBean extends BaseOracleBean {

  public ArmRsvPosLnItmDtlOracleBean() {}

  public static String selectSql = "select AI_TRN, AI_LN_ITM, SEQUENCE_NUM, ORG_AI_TRN, ORIG_AI_LN_ITM, ORIG_SEQ_NUM from ARM_RSV_POS_LN_ITM_DTL ";
  public static String insertSql = "insert into ARM_RSV_POS_LN_ITM_DTL (AI_TRN, AI_LN_ITM, SEQUENCE_NUM, ORG_AI_TRN, ORIG_AI_LN_ITM, ORIG_SEQ_NUM) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_RSV_POS_LN_ITM_DTL set AI_TRN = ?, AI_LN_ITM = ?, SEQUENCE_NUM = ?, ORG_AI_TRN = ?, ORIG_AI_LN_ITM = ?, ORIG_SEQ_NUM = ? ";
  public static String updateVoidSql = "update ARM_PRS_POS_LN_ITM_DTL set FL_VD_LN_ITM = ?";
  public static String deleteSql = "delete from ARM_RSV_POS_LN_ITM_DTL ";

  public static String TABLE_NAME = "ARM_RSV_POS_LN_ITM_DTL";
  public static String COL_AI_TRN = "ARM_RSV_POS_LN_ITM_DTL.AI_TRN";
  public static String COL_AI_LN_ITM = "ARM_RSV_POS_LN_ITM_DTL.AI_LN_ITM";
  public static String COL_SEQUENCE_NUM = "ARM_RSV_POS_LN_ITM_DTL.SEQUENCE_NUM";
  public static String COL_ORG_AI_TRN = "ARM_RSV_POS_LN_ITM_DTL.ORG_AI_TRN";
  public static String COL_ORIG_AI_LN_ITM = "ARM_RSV_POS_LN_ITM_DTL.ORIG_AI_LN_ITM";
  public static String COL_ORIG_SEQ_NUM = "ARM_RSV_POS_LN_ITM_DTL.ORIG_SEQ_NUM";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long aiLnItm;
  private Long sequenceNum;
  private String orgAiTrn;
  private Long origAiLnItm;
  private Long origSeqNum;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public Long getSequenceNum() { return this.sequenceNum; }
  public void setSequenceNum(Long sequenceNum) { this.sequenceNum = sequenceNum; }
  public void setSequenceNum(long sequenceNum) { this.sequenceNum = new Long(sequenceNum); }
  public void setSequenceNum(int sequenceNum) { this.sequenceNum = new Long((long)sequenceNum); }

  public String getOrgAiTrn() { return this.orgAiTrn; }
  public void setOrgAiTrn(String orgAiTrn) { this.orgAiTrn = orgAiTrn; }

  public Long getOrigAiLnItm() { return this.origAiLnItm; }
  public void setOrigAiLnItm(Long origAiLnItm) { this.origAiLnItm = origAiLnItm; }
  public void setOrigAiLnItm(long origAiLnItm) { this.origAiLnItm = new Long(origAiLnItm); }
  public void setOrigAiLnItm(int origAiLnItm) { this.origAiLnItm = new Long((long)origAiLnItm); }

  public Long getOrigSeqNum() { return this.origSeqNum; }
  public void setOrigSeqNum(Long origSeqNum) { this.origSeqNum = origSeqNum; }
  public void setOrigSeqNum(long origSeqNum) { this.origSeqNum = new Long(origSeqNum); }
  public void setOrigSeqNum(int origSeqNum) { this.origSeqNum = new Long((long)origSeqNum); }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmRsvPosLnItmDtlOracleBean bean = new ArmRsvPosLnItmDtlOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.sequenceNum = getLongFromResultSet(rs, "SEQUENCE_NUM");
      bean.orgAiTrn = getStringFromResultSet(rs, "ORG_AI_TRN");
      bean.origAiLnItm = getLongFromResultSet(rs, "ORIG_AI_LN_ITM");
      bean.origSeqNum = getLongFromResultSet(rs, "ORIG_SEQ_NUM");
      list.add(bean);
    }
    return (ArmRsvPosLnItmDtlOracleBean[]) list.toArray(new ArmRsvPosLnItmDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getSequenceNum(), Types.DECIMAL);
    addToList(list, this.getOrgAiTrn(), Types.VARCHAR);
    addToList(list, this.getOrigAiLnItm(), Types.DECIMAL);
    addToList(list, this.getOrigSeqNum(), Types.DECIMAL);
    return list;
  }

}
