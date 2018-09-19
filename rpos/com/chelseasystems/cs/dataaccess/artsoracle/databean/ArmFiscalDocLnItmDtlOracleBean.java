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
 * This class is an object representation of the Arts database table ARM_FISCAL_DOC_LN_ITM_DTL<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     DOC_NUM(PK) -- VARCHAR2(20)<BR>
 *     TY_DOC(PK) -- VARCHAR2(10)<BR>
 *     VAT_COMMENTS -- VARCHAR2(200)<BR>
 *
 */
public class ArmFiscalDocLnItmDtlOracleBean extends BaseOracleBean {

  public ArmFiscalDocLnItmDtlOracleBean() {}

  public static String selectSql = "select AI_TRN, AI_LN_ITM, DOC_NUM, TY_DOC, VAT_COMMENTS from ARM_FISCAL_DOC_LN_ITM_DTL ";
  public static String insertSql = "insert into ARM_FISCAL_DOC_LN_ITM_DTL (AI_TRN, AI_LN_ITM, DOC_NUM, TY_DOC, VAT_COMMENTS) values (?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_FISCAL_DOC_LN_ITM_DTL set AI_TRN = ?, AI_LN_ITM = ?, DOC_NUM = ?, TY_DOC = ?, VAT_COMMENTS = ? ";
  public static String deleteSql = "delete from ARM_FISCAL_DOC_LN_ITM_DTL ";

  public static String TABLE_NAME = "ARM_FISCAL_DOC_LN_ITM_DTL";
  public static String COL_AI_TRN = "ARM_FISCAL_DOC_LN_ITM_DTL.AI_TRN";
  public static String COL_AI_LN_ITM = "ARM_FISCAL_DOC_LN_ITM_DTL.AI_LN_ITM";
  public static String COL_DOC_NUM = "ARM_FISCAL_DOC_LN_ITM_DTL.DOC_NUM";
  public static String COL_TY_DOC = "ARM_FISCAL_DOC_LN_ITM_DTL.TY_DOC";
  public static String COL_VAT_COMMENTS = "ARM_FISCAL_DOC_LN_ITM_DTL.VAT_COMMENTS";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long aiLnItm;
  private String docNum;
  private String tyDoc;
  private String vatComments;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public String getDocNum() { return this.docNum; }
  public void setDocNum(String docNum) { this.docNum = docNum; }

  public String getTyDoc() { return this.tyDoc; }
  public void setTyDoc(String tyDoc) { this.tyDoc = tyDoc; }

  public String getVatComments() { return this.vatComments; }
  public void setVatComments(String vatComments) { this.vatComments = vatComments; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmFiscalDocLnItmDtlOracleBean bean = new ArmFiscalDocLnItmDtlOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.docNum = getStringFromResultSet(rs, "DOC_NUM");
      bean.tyDoc = getStringFromResultSet(rs, "TY_DOC");
      bean.vatComments = getStringFromResultSet(rs, "VAT_COMMENTS");
      list.add(bean);
    }
    return (ArmFiscalDocLnItmDtlOracleBean[]) list.toArray(new ArmFiscalDocLnItmDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getDocNum(), Types.VARCHAR);
    addToList(list, this.getTyDoc(), Types.VARCHAR);
    addToList(list, this.getVatComments(), Types.VARCHAR);
    return list;
  }

}
