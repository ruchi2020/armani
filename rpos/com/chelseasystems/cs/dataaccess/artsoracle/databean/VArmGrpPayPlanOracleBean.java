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
 * This class is an object representation of the Arts database table V_ARM_GRP_PAY_PLAN<BR>
 * Followings are the column of the table: <BR>
 *     TND_CODE -- VARCHAR2(10)<BR>
 *     CRD_PLAN_CODE -- VARCHAR2(20)<BR>
 *     ED_CO -- VARCHAR2(20)<BR>
 *     ED_LA -- VARCHAR2(20)<BR>
 *     DE_CRD_PLAN -- VARCHAR2(255)<BR>
 *
 */
public class VArmGrpPayPlanOracleBean extends BaseOracleBean {

  public VArmGrpPayPlanOracleBean() {}

  public static String selectSql = "select TND_CODE, CRD_PLAN_CODE, ED_CO, ED_LA, DE_CRD_PLAN from V_ARM_GRP_PAY_PLAN ";
  public static String insertSql = "insert into V_ARM_GRP_PAY_PLAN (TND_CODE, CRD_PLAN_CODE, ED_CO, ED_LA, DE_CRD_PLAN) values (?, ?, ?, ?, ?)";
  public static String updateSql = "update V_ARM_GRP_PAY_PLAN set TND_CODE = ?, CRD_PLAN_CODE = ?, ED_CO = ?, ED_LA = ?, DE_CRD_PLAN = ? ";
  public static String deleteSql = "delete from V_ARM_GRP_PAY_PLAN ";

  public static String TABLE_NAME = "V_ARM_GRP_PAY_PLAN";
  public static String COL_TND_CODE = "V_ARM_GRP_PAY_PLAN.TND_CODE";
  public static String COL_CRD_PLAN_CODE = "V_ARM_GRP_PAY_PLAN.CRD_PLAN_CODE";
  public static String COL_ED_CO = "V_ARM_GRP_PAY_PLAN.ED_CO";
  public static String COL_ED_LA = "V_ARM_GRP_PAY_PLAN.ED_LA";
  public static String COL_DE_CRD_PLAN = "V_ARM_GRP_PAY_PLAN.DE_CRD_PLAN";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String tndCode;
  private String crdPlanCode;
  private String edCo;
  private String edLa;
  private String deCrdPlan;

  public String getTndCode() { return this.tndCode; }
  public void setTndCode(String tndCode) { this.tndCode = tndCode; }

  public String getCrdPlanCode() { return this.crdPlanCode; }
  public void setCrdPlanCode(String crdPlanCode) { this.crdPlanCode = crdPlanCode; }

  public String getEdCo() { return this.edCo; }
  public void setEdCo(String edCo) { this.edCo = edCo; }

  public String getEdLa() { return this.edLa; }
  public void setEdLa(String edLa) { this.edLa = edLa; }

  public String getDeCrdPlan() { return this.deCrdPlan; }
  public void setDeCrdPlan(String deCrdPlan) { this.deCrdPlan = deCrdPlan; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      VArmGrpPayPlanOracleBean bean = new VArmGrpPayPlanOracleBean();
      bean.tndCode = getStringFromResultSet(rs, "TND_CODE");
      bean.crdPlanCode = getStringFromResultSet(rs, "CRD_PLAN_CODE");
      bean.edCo = getStringFromResultSet(rs, "ED_CO");
      bean.edLa = getStringFromResultSet(rs, "ED_LA");
      bean.deCrdPlan = getStringFromResultSet(rs, "DE_CRD_PLAN");
      list.add(bean);
    }
    return (VArmGrpPayPlanOracleBean[]) list.toArray(new VArmGrpPayPlanOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTndCode(), Types.VARCHAR);
    addToList(list, this.getCrdPlanCode(), Types.VARCHAR);
    addToList(list, this.getEdCo(), Types.VARCHAR);
    addToList(list, this.getEdLa(), Types.VARCHAR);
    addToList(list, this.getDeCrdPlan(), Types.VARCHAR);
    return list;
  }

}
