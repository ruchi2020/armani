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
 * This class is an object representation of the Arts database table ARM_EOD_DTL<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     CD_GROUP(PK) -- VARCHAR2(50)<BR>
 *     CD_TYPE(PK) -- VARCHAR2(50)<BR>
 *     TOTAL -- VARCHAR2(75)<BR>
 *     COUNT -- NUMBER(3)<BR>
 *     REPORTED_TOTAL -- VARCHAR2(75)<BR>
 *
 */
public class ArmEodDtlOracleBean extends BaseOracleBean {

  public ArmEodDtlOracleBean() {}

  public static String selectSql = "select AI_TRN, CD_GROUP, CD_TYPE, TOTAL, COUNT, REPORTED_TOTAL from ARM_EOD_DTL ";
  public static String insertSql = "insert into ARM_EOD_DTL (AI_TRN, CD_GROUP, CD_TYPE, TOTAL, COUNT, REPORTED_TOTAL) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_EOD_DTL set AI_TRN = ?, CD_GROUP = ?, CD_TYPE = ?, TOTAL = ?, COUNT = ?, REPORTED_TOTAL = ? ";
  public static String deleteSql = "delete from ARM_EOD_DTL ";

  public static String TABLE_NAME = "ARM_EOD_DTL";
  public static String COL_AI_TRN = "ARM_EOD_DTL.AI_TRN";
  public static String COL_CD_GROUP = "ARM_EOD_DTL.CD_GROUP";
  public static String COL_CD_TYPE = "ARM_EOD_DTL.CD_TYPE";
  public static String COL_TOTAL = "ARM_EOD_DTL.TOTAL";
  public static String COL_COUNT = "ARM_EOD_DTL.COUNT";
  public static String COL_REPORTED_TOTAL = "ARM_EOD_DTL.REPORTED_TOTAL";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private String cdGroup;
  private String cdType;
  private ArmCurrency total;
  private Long count;
  private ArmCurrency reportedTotal;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getCdGroup() { return this.cdGroup; }
  public void setCdGroup(String cdGroup) { this.cdGroup = cdGroup; }

  public String getCdType() { return this.cdType; }
  public void setCdType(String cdType) { this.cdType = cdType; }

  public ArmCurrency getTotal() { return this.total; }
  public void setTotal(ArmCurrency total) { this.total = total; }

  public Long getCount() { return this.count; }
  public void setCount(Long count) { this.count = count; }
  public void setCount(long count) { this.count = new Long(count); }
  public void setCount(int count) { this.count = new Long((long)count); }

  public ArmCurrency getReportedTotal() { return this.reportedTotal; }
  public void setReportedTotal(ArmCurrency reportedTotal) { this.reportedTotal = reportedTotal; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmEodDtlOracleBean bean = new ArmEodDtlOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.cdGroup = getStringFromResultSet(rs, "CD_GROUP");
      bean.cdType = getStringFromResultSet(rs, "CD_TYPE");
      bean.total = getCurrencyFromResultSet(rs, "TOTAL");
      bean.count = getLongFromResultSet(rs, "COUNT");
      bean.reportedTotal = getCurrencyFromResultSet(rs, "REPORTED_TOTAL");
      list.add(bean);
    }
    return (ArmEodDtlOracleBean[]) list.toArray(new ArmEodDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getCdGroup(), Types.VARCHAR);
    addToList(list, this.getCdType(), Types.VARCHAR);
    addToList(list, this.getTotal(), Types.VARCHAR);
    addToList(list, this.getCount(), Types.DECIMAL);
    addToList(list, this.getReportedTotal(), Types.VARCHAR);
    return list;
  }

}
