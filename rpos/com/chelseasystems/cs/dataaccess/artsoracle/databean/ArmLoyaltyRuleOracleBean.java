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
 * This class is an object representation of the Arts database table ARM_LOYALTY_RULE<BR>
 * Followings are the column of the table: <BR>
 *     ID_RULE(PK) -- VARCHAR2(128)<BR>
 *     DC_START -- DATE(7)<BR>
 *     DC_END -- DATE(7)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     TY_CT -- VARCHAR2(10)<BR>
 *     POINTS -- NUMBER(16.4)<BR>
 *     ID_DPT_POS -- VARCHAR2(128)<BR>
 *     ID_CLSS -- VARCHAR2(128)<BR>
 *     ID_SBCL -- VARCHAR2(128)<BR>
 *     STYLE_NUM -- VARCHAR2(10)<BR>
 *
 */
public class ArmLoyaltyRuleOracleBean extends BaseOracleBean {

  public ArmLoyaltyRuleOracleBean() {}

  public static String selectSql = "select ID_RULE, DC_START, DC_END, ID_STR_RT, TY_CT, POINTS, ID_DPT_POS, ID_CLSS, ID_SBCL, STYLE_NUM from ARM_LOYALTY_RULE ";
  public static String insertSql = "insert into ARM_LOYALTY_RULE (ID_RULE, DC_START, DC_END, ID_STR_RT, TY_CT, POINTS, ID_DPT_POS, ID_CLSS, ID_SBCL, STYLE_NUM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_LOYALTY_RULE set ID_RULE = ?, DC_START = ?, DC_END = ?, ID_STR_RT = ?, TY_CT = ?, POINTS = ?, ID_DPT_POS = ?, ID_CLSS = ?, ID_SBCL = ?, STYLE_NUM = ? ";
  public static String deleteSql = "delete from ARM_LOYALTY_RULE ";

  public static String TABLE_NAME = "ARM_LOYALTY_RULE";
  public static String COL_ID_RULE = "ARM_LOYALTY_RULE.ID_RULE";
  public static String COL_DC_START = "ARM_LOYALTY_RULE.DC_START";
  public static String COL_DC_END = "ARM_LOYALTY_RULE.DC_END";
  public static String COL_ID_STR_RT = "ARM_LOYALTY_RULE.ID_STR_RT";
  public static String COL_TY_CT = "ARM_LOYALTY_RULE.TY_CT";
  public static String COL_POINTS = "ARM_LOYALTY_RULE.POINTS";
  public static String COL_ID_DPT_POS = "ARM_LOYALTY_RULE.ID_DPT_POS";
  public static String COL_ID_CLSS = "ARM_LOYALTY_RULE.ID_CLSS";
  public static String COL_ID_SBCL = "ARM_LOYALTY_RULE.ID_SBCL";
  public static String COL_STYLE_NUM = "ARM_LOYALTY_RULE.STYLE_NUM";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idRule;
  private Date dcStart;
  private Date dcEnd;
  private String idStrRt;
  private String tyCt;
  private Double points;
  private String idDptPos;
  private String idClss;
  private String idSbcl;
  private String styleNum;

  public String getIdRule() { return this.idRule; }
  public void setIdRule(String idRule) { this.idRule = idRule; }

  public Date getDcStart() { return this.dcStart; }
  public void setDcStart(Date dcStart) { this.dcStart = dcStart; }

  public Date getDcEnd() { return this.dcEnd; }
  public void setDcEnd(Date dcEnd) { this.dcEnd = dcEnd; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getTyCt() { return this.tyCt; }
  public void setTyCt(String tyCt) { this.tyCt = tyCt; }

  public Double getPoints() { return this.points; }
  public void setPoints(Double points) { this.points = points; }
  public void setPoints(double points) { this.points = new Double(points); }

  public String getIdDptPos() { return this.idDptPos; }
  public void setIdDptPos(String idDptPos) { this.idDptPos = idDptPos; }

  public String getIdClss() { return this.idClss; }
  public void setIdClss(String idClss) { this.idClss = idClss; }

  public String getIdSbcl() { return this.idSbcl; }
  public void setIdSbcl(String idSbcl) { this.idSbcl = idSbcl; }

  public String getStyleNum() { return this.styleNum; }
  public void setStyleNum(String styleNum) { this.styleNum = styleNum; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmLoyaltyRuleOracleBean bean = new ArmLoyaltyRuleOracleBean();
      bean.idRule = getStringFromResultSet(rs, "ID_RULE");
      bean.dcStart = getDateFromResultSet(rs, "DC_START");
      bean.dcEnd = getDateFromResultSet(rs, "DC_END");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.tyCt = getStringFromResultSet(rs, "TY_CT");
      bean.points = getDoubleFromResultSet(rs, "POINTS");
      bean.idDptPos = getStringFromResultSet(rs, "ID_DPT_POS");
      bean.idClss = getStringFromResultSet(rs, "ID_CLSS");
      bean.idSbcl = getStringFromResultSet(rs, "ID_SBCL");
      bean.styleNum = getStringFromResultSet(rs, "STYLE_NUM");
      list.add(bean);
    }
    return (ArmLoyaltyRuleOracleBean[]) list.toArray(new ArmLoyaltyRuleOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdRule(), Types.VARCHAR);
    addToList(list, this.getDcStart(), Types.TIMESTAMP);
    addToList(list, this.getDcEnd(), Types.TIMESTAMP);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getTyCt(), Types.VARCHAR);
    addToList(list, this.getPoints(), Types.DECIMAL);
    addToList(list, this.getIdDptPos(), Types.VARCHAR);
    addToList(list, this.getIdClss(), Types.VARCHAR);
    addToList(list, this.getIdSbcl(), Types.VARCHAR);
    addToList(list, this.getStyleNum(), Types.VARCHAR);
    return list;
  }

}
