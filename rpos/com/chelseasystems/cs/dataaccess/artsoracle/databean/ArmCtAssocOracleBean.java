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
 * This class is an object representation of the Arts database table ARM_CT_ASSOC<BR>
 * Followings are the column of the table: <BR>
 *     ID_CT -- VARCHAR2(128)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     ID_ASSOCIATE -- VARCHAR2(128)<BR>
 *     ID_CT_ASSOCIATE -- VARCHAR2(128)<BR>
 *
 */
public class ArmCtAssocOracleBean extends BaseOracleBean {

  public ArmCtAssocOracleBean() {}

  public static String selectSql = "select ID_CT, ID_STR_RT, ID_ASSOCIATE, ID_CT_ASSOCIATE from ARM_CT_ASSOC ";
  public static String insertSql = "insert into ARM_CT_ASSOC (ID_CT, ID_STR_RT, ID_ASSOCIATE, ID_CT_ASSOCIATE) values (?, ?, ?, ?)";
  public static String updateSql = "update ARM_CT_ASSOC set ID_CT = ?, ID_STR_RT = ?, ID_ASSOCIATE = ?, ID_CT_ASSOCIATE = ? ";
  public static String deleteSql = "delete from ARM_CT_ASSOC ";

  public static String TABLE_NAME = "ARM_CT_ASSOC";
  public static String COL_ID_CT = "ARM_CT_ASSOC.ID_CT";
  public static String COL_ID_STR_RT = "ARM_CT_ASSOC.ID_STR_RT";
  public static String COL_ID_ASSOCIATE = "ARM_CT_ASSOC.ID_ASSOCIATE";
  public static String COL_ID_CT_ASSOCIATE = "ARM_CT_ASSOC.ID_CT_ASSOCIATE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String idStrRt;
  private String idAssociate;
  private String idCtAssociate;

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdAssociate() { return this.idAssociate; }
  public void setIdAssociate(String idAssociate) { this.idAssociate = idAssociate; }

  public String getIdCtAssociate() { return this.idCtAssociate; }
  public void setIdCtAssociate(String idCtAssociate) { this.idCtAssociate = idCtAssociate; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCtAssocOracleBean bean = new ArmCtAssocOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idAssociate = getStringFromResultSet(rs, "ID_ASSOCIATE");
      bean.idCtAssociate = getStringFromResultSet(rs, "ID_CT_ASSOCIATE");
      list.add(bean);
    }
    return (ArmCtAssocOracleBean[]) list.toArray(new ArmCtAssocOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdAssociate(), Types.VARCHAR);
    addToList(list, this.getIdCtAssociate(), Types.VARCHAR);
    return list;
  }

}
