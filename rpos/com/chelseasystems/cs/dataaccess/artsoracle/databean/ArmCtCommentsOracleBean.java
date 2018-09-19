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
 * This class is an object representation of the Arts database table ARM_CT_COMMENTS<BR>
 * Followings are the column of the table: <BR>
 *     ID_CT -- VARCHAR2(30)<BR>
 *     ID_STR_RT -- VARCHAR2(30)<BR>
 *     ID_CMY -- VARCHAR2(30)<BR>
 *     ID_BRN -- VARCHAR2(30)<BR>
 *     ID_ASSOCIATE -- VARCHAR2(30)<BR>
 *     CREATE_DT -- DATE(7)<BR>
 *     COMMENTS -- VARCHAR2(50)<BR>
 *     ID_CT_COMMENT -- VARCHAR2(128)<BR>
 *
 */
public class ArmCtCommentsOracleBean extends BaseOracleBean {

  public ArmCtCommentsOracleBean() {}

  public static String selectSql = "select ID_CT, ID_STR_RT, ID_CMY, ID_BRN, ID_ASSOCIATE, CREATE_DT, COMMENTS, ID_CT_COMMENT from ARM_CT_COMMENTS ";
  public static String insertSql = "insert into ARM_CT_COMMENTS (ID_CT, ID_STR_RT, ID_CMY, ID_BRN, ID_ASSOCIATE, CREATE_DT, COMMENTS, ID_CT_COMMENT) values (?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CT_COMMENTS set ID_CT = ?, ID_STR_RT = ?, ID_CMY = ?, ID_BRN = ?, ID_ASSOCIATE = ?, CREATE_DT = ?, COMMENTS = ?, ID_CT_COMMENT = ? ";
  public static String deleteSql = "delete from ARM_CT_COMMENTS ";

  public static String TABLE_NAME = "ARM_CT_COMMENTS";
  public static String COL_ID_CT = "ARM_CT_COMMENTS.ID_CT";
  public static String COL_ID_STR_RT = "ARM_CT_COMMENTS.ID_STR_RT";
  public static String COL_ID_CMY = "ARM_CT_COMMENTS.ID_CMY";
  public static String COL_ID_BRN = "ARM_CT_COMMENTS.ID_BRN";
  public static String COL_ID_ASSOCIATE = "ARM_CT_COMMENTS.ID_ASSOCIATE";
  public static String COL_CREATE_DT = "ARM_CT_COMMENTS.CREATE_DT";
  public static String COL_COMMENTS = "ARM_CT_COMMENTS.COMMENTS";
  public static String COL_ID_CT_COMMENT = "ARM_CT_COMMENTS.ID_CT_COMMENT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String idStrRt;
  private String idCmy;
  private String idBrn;
  private String idAssociate;
  private Date createDt;
  private String comments;
  private String idCtComment;

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdCmy() { return this.idCmy; }
  public void setIdCmy(String idCmy) { this.idCmy = idCmy; }

  public String getIdBrn() { return this.idBrn; }
  public void setIdBrn(String idBrn) { this.idBrn = idBrn; }

  public String getIdAssociate() { return this.idAssociate; }
  public void setIdAssociate(String idAssociate) { this.idAssociate = idAssociate; }

  public Date getCreateDt() { return this.createDt; }
  public void setCreateDt(Date createDt) { this.createDt = createDt; }

  public String getComments() { return this.comments; }
  public void setComments(String comments) { this.comments = comments; }

  public String getIdCtComment() { return this.idCtComment; }
  public void setIdCtComment(String idCtComment) { this.idCtComment = idCtComment; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCtCommentsOracleBean bean = new ArmCtCommentsOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idCmy = getStringFromResultSet(rs, "ID_CMY");
      bean.idBrn = getStringFromResultSet(rs, "ID_BRN");
      bean.idAssociate = getStringFromResultSet(rs, "ID_ASSOCIATE");
      bean.createDt = getDateFromResultSet(rs, "CREATE_DT");
      bean.comments = getStringFromResultSet(rs, "COMMENTS");
      bean.idCtComment = getStringFromResultSet(rs, "ID_CT_COMMENT");
      list.add(bean);
    }
    return (ArmCtCommentsOracleBean[]) list.toArray(new ArmCtCommentsOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdCmy(), Types.VARCHAR);
    addToList(list, this.getIdBrn(), Types.VARCHAR);
    addToList(list, this.getIdAssociate(), Types.VARCHAR);
    addToList(list, this.getCreateDt(), Types.TIMESTAMP);
    addToList(list, this.getComments(), Types.VARCHAR);
    addToList(list, this.getIdCtComment(), Types.VARCHAR);
    return list;
  }

}
