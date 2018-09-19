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
 * This class is an object representation of the Arts database table TR_LTM_DSC_DTL<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AL_LN_ITM(PK) -- NUMBER(2)<BR>
 *     SEQ_NUM(PK) -- NUMBER(22)<BR>
 *     DSC_SEQ_NUM -- NUMBER(22)<BR>
 *
 */
public class TrLtmDscDtlOracleBean extends BaseOracleBean {

  public TrLtmDscDtlOracleBean() {}

  public static String selectSql = "select AI_TRN, AL_LN_ITM, SEQ_NUM, DSC_SEQ_NUM from TR_LTM_DSC_DTL ";
  public static String insertSql = "insert into TR_LTM_DSC_DTL (AI_TRN, AL_LN_ITM, SEQ_NUM, DSC_SEQ_NUM) values (?, ?, ?, ?)";
  public static String updateSql = "update TR_LTM_DSC_DTL set AI_TRN = ?, AL_LN_ITM= ?, SEQ_NUM = ?, DSC_SEQ_NUM = ? ";
  public static String deleteSql = "delete from TR_LTM_DSC_DTL ";

  public static String TABLE_NAME = "TR_LTM_DSC_DTL";
  public static String COL_AI_TRN = "TR_LTM_DSC_DTL.AI_TRN";
  public static String COL_AL_LN_ITM= "TR_LTM_DSC_DTL.AL_LN_ITM";
  public static String COL_SEQ_NUM = "TR_LTM_DSC_DTL.SEQ_NUM";
  public static String COL_DSC_SEQ_NUM = "TR_LTM_DSC_DTL.DSC_SEQ_NUM";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long alLnItm;
  private Long seqNum;
  private Long dscSeqNum;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAlLnItm() { return this.alLnItm; }
  public void setAlLnItm(Long alLnItm) { this.alLnItm = alLnItm; }

  public Long getSeqNum() { return this.seqNum; }
  public void setSeqNum(Long seqNum) { this.seqNum = seqNum; }
  public void setSeqNum(long seqNum) { this.seqNum = new Long(seqNum); }
  public void setSeqNum(int seqNum) { this.seqNum = new Long((long)seqNum); }

  public Long getDscSeqNum() { return this.dscSeqNum; }
  public void setDscSeqNum(Long dscSeqNum) { this.dscSeqNum = dscSeqNum; }
  public void setDscSeqNum(long dscSeqNum) { this.dscSeqNum = new Long(dscSeqNum); }
  public void setDscSeqNum(int dscSeqNum) { this.dscSeqNum = new Long((long)dscSeqNum); }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      TrLtmDscDtlOracleBean bean = new TrLtmDscDtlOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.alLnItm = getLongFromResultSet(rs, "AL_LN_ITM");
      bean.seqNum = getLongFromResultSet(rs, "SEQ_NUM");
      bean.dscSeqNum = getLongFromResultSet(rs, "DSC_SEQ_NUM");
      list.add(bean);
    }
    return (TrLtmDscDtlOracleBean[]) list.toArray(new TrLtmDscDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAlLnItm(), Types.DECIMAL);
    addToList(list, this.getSeqNum(), Types.DECIMAL);
    addToList(list, this.getDscSeqNum(), Types.DECIMAL);
    return list;
  }

}
