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
 * This class is an object representation of the Arts database table ARM_ALTERN_GRP<BR>
 * Followings are the column of the table: <BR>
 *     GROUP_ID(PK) -- VARCHAR2(30)<BR>
 *     GROUP_NAME -- VARCHAR2(50)<BR>
 *     ED_CO -- VARCHAR2(20)<BR>
 *     ED_LA -- VARCHAR2(20)<BR>
 *
 */
public class ArmAlternGrpOracleBean extends BaseOracleBean {

  public ArmAlternGrpOracleBean() {}

  public static String selectSql = "select GROUP_ID, GROUP_NAME, ED_CO, ED_LA from ARM_ALTERN_GRP ";
  public static String insertSql = "insert into ARM_ALTERN_GRP (GROUP_ID, GROUP_NAME, ED_CO, ED_LA) values (?, ?, ?, ?)";
  public static String updateSql = "update ARM_ALTERN_GRP set GROUP_ID = ?, GROUP_NAME = ?, ED_CO = ?, ED_LA = ? ";
  public static String deleteSql = "delete from ARM_ALTERN_GRP ";

  public static String TABLE_NAME = "ARM_ALTERN_GRP";
  public static String COL_GROUP_ID = "ARM_ALTERN_GRP.GROUP_ID";
  public static String COL_GROUP_NAME = "ARM_ALTERN_GRP.GROUP_NAME";
  public static String COL_ED_CO = "ARM_ALTERN_GRP.ED_CO";
  public static String COL_ED_LA = "ARM_ALTERN_GRP.ED_LA";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String groupId;
  private String groupName;
  private String edCo;
  private String edLa;

  public String getGroupId() { return this.groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public String getGroupName() { return this.groupName; }
  public void setGroupName(String groupName) { this.groupName = groupName; }

  public String getEdCo() { return this.edCo; }
  public void setEdCo(String edCo) { this.edCo = edCo; }

  public String getEdLa() { return this.edLa; }
  public void setEdLa(String edLa) { this.edLa = edLa; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmAlternGrpOracleBean bean = new ArmAlternGrpOracleBean();
      bean.groupId = getStringFromResultSet(rs, "GROUP_ID");
      bean.groupName = getStringFromResultSet(rs, "GROUP_NAME");
      bean.edCo = getStringFromResultSet(rs, "ED_CO");
      bean.edLa = getStringFromResultSet(rs, "ED_LA");
      list.add(bean);
    }
    return (ArmAlternGrpOracleBean[]) list.toArray(new ArmAlternGrpOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getGroupId(), Types.VARCHAR);
    addToList(list, this.getGroupName(), Types.VARCHAR);
    addToList(list, this.getEdCo(), Types.VARCHAR);
    addToList(list, this.getEdLa(), Types.VARCHAR);
    return list;
  }

}
