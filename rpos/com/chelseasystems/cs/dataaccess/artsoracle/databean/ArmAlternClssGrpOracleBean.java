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
 * This class is an object representation of the Arts database table ARM_ALTERN_CLSS_GRP<BR>
 * Followings are the column of the table: <BR>
 *     GROUP_ID -- VARCHAR2(30)<BR>
 *     SUB_GROUP_ID -- VARCHAR2(30)<BR>
 *
 */
public class ArmAlternClssGrpOracleBean extends BaseOracleBean {

  public ArmAlternClssGrpOracleBean() {}

  public static String selectSql = "select GROUP_ID, SUB_GROUP_ID from ARM_ALTERN_CLSS_GRP ";
  public static String insertSql = "insert into ARM_ALTERN_CLSS_GRP (GROUP_ID, SUB_GROUP_ID) values (?, ?)";
  public static String updateSql = "update ARM_ALTERN_CLSS_GRP set GROUP_ID = ?, SUB_GROUP_ID = ? ";
  public static String deleteSql = "delete from ARM_ALTERN_CLSS_GRP ";

  public static String TABLE_NAME = "ARM_ALTERN_CLSS_GRP";
  public static String COL_GROUP_ID = "ARM_ALTERN_CLSS_GRP.GROUP_ID";
  public static String COL_SUB_GROUP_ID = "ARM_ALTERN_CLSS_GRP.SUB_GROUP_ID";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String groupId;
  private String subGroupId;

  public String getGroupId() { return this.groupId; }
  public void setGroupId(String groupId) { this.groupId = groupId; }

  public String getSubGroupId() { return this.subGroupId; }
  public void setSubGroupId(String subGroupId) { this.subGroupId = subGroupId; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmAlternClssGrpOracleBean bean = new ArmAlternClssGrpOracleBean();
      bean.groupId = getStringFromResultSet(rs, "GROUP_ID");
      bean.subGroupId = getStringFromResultSet(rs, "SUB_GROUP_ID");
      list.add(bean);
    }
    return (ArmAlternClssGrpOracleBean[]) list.toArray(new ArmAlternClssGrpOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getGroupId(), Types.VARCHAR);
    addToList(list, this.getSubGroupId(), Types.VARCHAR);
    return list;
  }

}
