//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;

/**
 *
 * This class is an object representation of the Arts database table ARM_DSC_RULE<BR>
 * Followings are the column of the table: <BR>
 *     CD_DSC -- VARCHAR2(10)<BR>
 *     START_RANGE -- NUMBER(10.2)<BR>
 *     END_RANGE -- NUMBER(10.2)<BR>
 *     IS_DSC_PERCENT -- NUMBER(1)<BR>
 *     PERCENT -- NUMBER(7.4)<BR>
 *     MO_DSC -- NUMBER(10.2)<BR>
 *     CD_NOTE -- VARCHAR2(25)<BR>
 *     ED_CO -- VARCHAR2(20)<BR>
 *     ED_LA -- VARCHAR2(20)<BR>
 *
 */
public class ArmDscRuleOracleBean extends BaseOracleBean {

  public ArmDscRuleOracleBean() {}

  public static String selectSql = "select CD_DSC, START_RANGE, END_RANGE, IS_DSC_PERCENT, PERCENT, MO_DSC, CD_NOTE, ED_CO, ED_LA from ARM_DSC_RULE ";
  public static String insertSql = "insert into ARM_DSC_RULE (CD_DSC, START_RANGE, END_RANGE, IS_DSC_PERCENT, PERCENT, MO_DSC, CD_NOTE, ED_CO, ED_LA) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_DSC_RULE set CD_DSC = ?, START_RANGE = ?, END_RANGE = ?, IS_DSC_PERCENT = ?, PERCENT = ?, MO_DSC = ?, CD_NOTE = ?, ED_CO = ?, ED_LA = ? ";
  public static String deleteSql = "delete from ARM_DSC_RULE ";

  public static String TABLE_NAME = "ARM_DSC_RULE";
  public static String COL_CD_DSC = "ARM_DSC_RULE.CD_DSC";
  public static String COL_START_RANGE = "ARM_DSC_RULE.START_RANGE";
  public static String COL_END_RANGE = "ARM_DSC_RULE.END_RANGE";
  public static String COL_IS_DSC_PERCENT = "ARM_DSC_RULE.IS_DSC_PERCENT";
  public static String COL_PERCENT = "ARM_DSC_RULE.PERCENT";
  public static String COL_MO_DSC = "ARM_DSC_RULE.MO_DSC";
  public static String COL_CD_NOTE = "ARM_DSC_RULE.CD_NOTE";
  public static String COL_ED_CO = "ARM_DSC_RULE.ED_CO";
  public static String COL_ED_LA = "ARM_DSC_RULE.ED_LA";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String cdDsc;
  private Double startRange;
  private Double endRange;
  private Boolean isDscPercent;
  private Double percent;
  private Double moDsc;
  private String cdNote;
  private String edCo;
  private String edLa;

  public String getCdDsc() { return this.cdDsc; }
  public void setCdDsc(String cdDsc) { this.cdDsc = cdDsc; }

  public Double getStartRange() { return this.startRange; }
  public void setStartRange(Double startRange) { this.startRange = startRange; }
  public void setStartRange(double startRange) { this.startRange = new Double(startRange); }

  public Double getEndRange() { return this.endRange; }
  public void setEndRange(Double endRange) { this.endRange = endRange; }
  public void setEndRange(double endRange) { this.endRange = new Double(endRange); }

  public Boolean getIsDscPercent() { return this.isDscPercent; }
  public void setIsDscPercent(Boolean isDscPercent) { this.isDscPercent = isDscPercent; }
  public void setIsDscPercent(boolean isDscPercent) { this.isDscPercent = new Boolean(isDscPercent); }

  public Double getPercent() { return this.percent; }
  public void setPercent(Double percent) { this.percent = percent; }
  public void setPercent(double percent) { this.percent = new Double(percent); }

  public Double getMoDsc() { return this.moDsc; }
  public void setMoDsc(Double moDsc) { this.moDsc = moDsc; }
  public void setMoDsc(double moDsc) { this.moDsc = new Double(moDsc); }

  public String getCdNote() { return this.cdNote; }
  public void setCdNote(String cdNote) { this.cdNote = cdNote; }

  public String getEdCo() { return this.edCo; }
  public void setEdCo(String edCo) { this.edCo = edCo; }

  public String getEdLa() { return this.edLa; }
  public void setEdLa(String edLa) { this.edLa = edLa; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmDscRuleOracleBean bean = new ArmDscRuleOracleBean();
      bean.cdDsc = getStringFromResultSet(rs, "CD_DSC");
      bean.startRange = getDoubleFromResultSet(rs, "START_RANGE");
      bean.endRange = getDoubleFromResultSet(rs, "END_RANGE");
      bean.isDscPercent = getBooleanFromResultSet(rs, "IS_DSC_PERCENT");
      bean.percent = getDoubleFromResultSet(rs, "PERCENT");
      bean.moDsc = getDoubleFromResultSet(rs, "MO_DSC");
      bean.cdNote = getStringFromResultSet(rs, "CD_NOTE");
      bean.edCo = getStringFromResultSet(rs, "ED_CO");
      bean.edLa = getStringFromResultSet(rs, "ED_LA");
      list.add(bean);
    }
    return (ArmDscRuleOracleBean[]) list.toArray(new ArmDscRuleOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getCdDsc(), Types.VARCHAR);
    addToList(list, this.getStartRange(), Types.DECIMAL);
    addToList(list, this.getEndRange(), Types.DECIMAL);
    addToList(list, this.getIsDscPercent(), Types.DECIMAL);
    addToList(list, this.getPercent(), Types.DECIMAL);
    addToList(list, this.getMoDsc(), Types.DECIMAL);
    addToList(list, this.getCdNote(), Types.VARCHAR);
    addToList(list, this.getEdCo(), Types.VARCHAR);
    addToList(list, this.getEdLa(), Types.VARCHAR);
    return list;
  }

}
