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
 * This class is an object representation of the Arts database table ARM_CUSTOMER_MESSAGES<BR>
 * Followings are the column of the table: <BR>
 *     ID_CT -- VARCHAR2(128)<BR>
 *     TY_CT -- VARCHAR2(10)<BR>
 *     TY_MSG -- VARCHAR2(1)<BR>
 *     MESSAGE -- VARCHAR2(255)<BR>
 *     STATUS -- VARCHAR2(1)<BR>
 *     RESPONSE -- VARCHAR2(255)<BR>
 *
 */
public class ArmCustomerMessagesOracleBean extends BaseOracleBean {

  public ArmCustomerMessagesOracleBean() {}

  public static String selectSql = "select ID_CT, TY_CT, TY_MSG, MESSAGE, STATUS, RESPONSE from ARM_CUSTOMER_MESSAGES ";
  public static String insertSql = "insert into ARM_CUSTOMER_MESSAGES (ID_CT, TY_CT, TY_MSG, MESSAGE, STATUS, RESPONSE) values (?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CUSTOMER_MESSAGES set ID_CT = ?, TY_CT = ?, TY_MSG = ?, MESSAGE = ?, STATUS = ?, RESPONSE = ? ";
  public static String deleteSql = "delete from ARM_CUSTOMER_MESSAGES ";

  public static String TABLE_NAME = "ARM_CUSTOMER_MESSAGES";
  public static String COL_ID_CT = "ARM_CUSTOMER_MESSAGES.ID_CT";
  public static String COL_TY_CT = "ARM_CUSTOMER_MESSAGES.TY_CT";
  public static String COL_TY_MSG = "ARM_CUSTOMER_MESSAGES.TY_MSG";
  public static String COL_MESSAGE = "ARM_CUSTOMER_MESSAGES.MESSAGE";
  public static String COL_STATUS = "ARM_CUSTOMER_MESSAGES.STATUS";
  public static String COL_RESPONSE = "ARM_CUSTOMER_MESSAGES.RESPONSE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String tyCt;
  private String tyMsg;
  private String message;
  private String status;
  private String response;

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getTyCt() { return this.tyCt; }
  public void setTyCt(String tyCt) { this.tyCt = tyCt; }

  public String getTyMsg() { return this.tyMsg; }
  public void setTyMsg(String tyMsg) { this.tyMsg = tyMsg; }

  public String getMessage() { return this.message; }
  public void setMessage(String message) { this.message = message; }

  public String getStatus() { return this.status; }
  public void setStatus(String status) { this.status = status; }

  public String getResponse() { return this.response; }
  public void setResponse(String response) { this.response = response; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCustomerMessagesOracleBean bean = new ArmCustomerMessagesOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.tyCt = getStringFromResultSet(rs, "TY_CT");
      bean.tyMsg = getStringFromResultSet(rs, "TY_MSG");
      bean.message = getStringFromResultSet(rs, "MESSAGE");
      bean.status = getStringFromResultSet(rs, "STATUS");
      bean.response = getStringFromResultSet(rs, "RESPONSE");
      list.add(bean);
    }
    return (ArmCustomerMessagesOracleBean[]) list.toArray(new ArmCustomerMessagesOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getTyCt(), Types.VARCHAR);
    addToList(list, this.getTyMsg(), Types.VARCHAR);
    addToList(list, this.getMessage(), Types.VARCHAR);
    addToList(list, this.getStatus(), Types.VARCHAR);
    addToList(list, this.getResponse(), Types.VARCHAR);
    return list;
  }

}
