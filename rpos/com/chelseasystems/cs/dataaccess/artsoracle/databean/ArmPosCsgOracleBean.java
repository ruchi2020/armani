//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
/*
History:
+------+------------+-----------+-----------+----------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                  |
+------+------------+-----------+-----------+----------------------------------------------+
| 3    | 04-29-2005 | Pankaja   | N/A       | New sql for updation of the expiration dt    |
--------------------------------------------------------------------------------------------
| 2    | 04-12-2005 | Rajesh    | N/A       | Specs Consignment impl                       |
+------+------------+-----------+-----------+----------------------------------------------+
*/
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
 * This class is an object representation of the Arts database table ARM_POS_CSG<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     ID_CONSIGNMENT -- VARCHAR2(128)<BR>
 *     EXP_DT -- DATE(7)<BR>
 *
 */
public class ArmPosCsgOracleBean extends BaseOracleBean {

  public ArmPosCsgOracleBean() {}

  public static String selectSql = "select AI_TRN, ID_CONSIGNMENT, EXP_DT from ARM_POS_CSG ";
  public static String insertSql = "insert into ARM_POS_CSG (AI_TRN, ID_CONSIGNMENT, EXP_DT) values (?, ?, ?)";
  public static String updateSql = "update ARM_POS_CSG set AI_TRN = ?, ID_CONSIGNMENT = ?, EXP_DT = ? ";
  public static String updateExpirationDtSql = "update ARM_POS_CSG set EXP_DT = ? ";
  public static String deleteSql = "delete from ARM_POS_CSG ";

  public static String TABLE_NAME = "ARM_POS_CSG";
  public static String COL_AI_TRN = "ARM_POS_CSG.AI_TRN";
  public static String COL_ID_CONSIGNMENT = "ARM_POS_CSG.ID_CONSIGNMENT";
  public static String COL_EXP_DT = "ARM_POS_CSG.EXP_DT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private String idConsignment;
  private Date expDt;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getIdConsignment() { return this.idConsignment; }
  public void setIdConsignment(String idConsignment) { this.idConsignment = idConsignment; }

  public Date getExpDt() { return this.expDt; }
  public void setExpDt(Date expDt) { this.expDt = expDt; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmPosCsgOracleBean bean = new ArmPosCsgOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.idConsignment = getStringFromResultSet(rs, "ID_CONSIGNMENT");
      bean.expDt = getDateFromResultSet(rs, "EXP_DT");
      list.add(bean);
    }
    return (ArmPosCsgOracleBean[]) list.toArray(new ArmPosCsgOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getIdConsignment(), Types.VARCHAR);
    addToList(list, this.getExpDt(), Types.TIMESTAMP);
    return list;
  }

}
