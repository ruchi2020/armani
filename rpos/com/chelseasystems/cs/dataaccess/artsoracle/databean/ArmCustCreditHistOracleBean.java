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
 * This class is an object representation of the Arts database table ARM_CUST_CREDIT_HIST<BR>
 * Followings are the column of the table: <BR>
 *     DC_TRANSACTION -- DATE(7)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     AI_TRN -- VARCHAR2(128)<BR>
 *     TY_TRN -- VARCHAR2(60)<BR>
 *     CUST_ID -- VARCHAR2(128)<BR>
 *     ASSOC -- VARCHAR2(128)<BR>
 *     AMOUNT -- VARCHAR2(75)<BR>
 *
 */
public class ArmCustCreditHistOracleBean extends BaseOracleBean {

  public ArmCustCreditHistOracleBean() {}

  public static String selectSql = "select DC_TRANSACTION, ID_STR_RT, AI_TRN, TY_TRN, CUST_ID, ASSOC, AMOUNT , FL_DELETED from ARM_CUST_CREDIT_HIST ";
  public static String insertSql = "insert into ARM_CUST_CREDIT_HIST (DC_TRANSACTION, ID_STR_RT, AI_TRN, TY_TRN, CUST_ID, ASSOC, AMOUNT, FL_DELETED) values (?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CUST_CREDIT_HIST set DC_TRANSACTION = ?, ID_STR_RT = ?, AI_TRN = ?, TY_TRN = ?, CUST_ID = ?, ASSOC = ?, AMOUNT = ? , FL_DELETED = ? ";
  public static String deleteSql = "delete from ARM_CUST_CREDIT_HIST ";

  public static String creditHistorySqlForStore = "select A.DC_TRANSACTION DC_TRANSACTION, A.ID_STR_RT ID_STR_RT, " +
  "A.AI_TRN AI_TRN, A.TY_TRN TY_TRN, A.ASSOC ASSOC, A.AMOUNT AMOUNT, A.CUST_ID CUST_ID, A.FL_DELETED FL_DELETED, " + 
  "B.FN_PRS FN_PRS, B.LN_PRS LN_PRS, C.DE_STR_RT DE_STR_RT from ARM_CUST_CREDIT_HIST A, PA_PRS B, PA_STR_RTL C " + 
  "where A.ASSOC = B.ID_PRTY_PRS and A.ID_STR_RT = C.ID_STR_RT and A.CUST_ID = ? and A.ID_STR_RT = ? "/* +
  "AND A.FL_DELETED NOT IN ?"*/;
  
  
  public static String TABLE_NAME = "ARM_CUST_CREDIT_HIST";
  public static String COL_DC_TRANSACTION = "ARM_CUST_CREDIT_HIST.DC_TRANSACTION";
  public static String COL_ID_STR_RT = "ARM_CUST_CREDIT_HIST.ID_STR_RT";
  public static String COL_AI_TRN = "ARM_CUST_CREDIT_HIST.AI_TRN";
  public static String COL_TY_TRN = "ARM_CUST_CREDIT_HIST.TY_TRN";
  public static String COL_CUST_ID = "ARM_CUST_CREDIT_HIST.CUST_ID";
  public static String COL_ASSOC = "ARM_CUST_CREDIT_HIST.ASSOC";
  public static String COL_AMOUNT = "ARM_CUST_CREDIT_HIST.AMOUNT";
  public static String COL_FL_DELETED = "ARM_CUST_DEPOSIT_HIST.FL_DELETED";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Date dcTransaction;
  private String idStrRt;
  private String aiTrn;
  private String tyTrn;
  private String custId;
  private String assoc;
  private ArmCurrency amount;
  private Boolean flDeleted;
  private String assocFirstName;
  private String assocLastName;
  private String deStrRt;
  

  public Date getDcTransaction() { return this.dcTransaction; }
  public void setDcTransaction(Date dcTransaction) { this.dcTransaction = dcTransaction; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getTyTrn() { return this.tyTrn; }
  public void setTyTrn(String tyTrn) { this.tyTrn = tyTrn; }

  public String getCustId() { return this.custId; }
  public void setCustId(String custId) { this.custId = custId; }

  public String getAssoc() { return this.assoc; }
  public void setAssoc(String assoc) { this.assoc = assoc; }

  public ArmCurrency getAmount() { return this.amount; }
  public void setAmount(ArmCurrency amount) { this.amount = amount; }

  public Boolean getFlDeleted() { return this.flDeleted; }
  public void setFlDeleted(Boolean flDeleted) { this.flDeleted = flDeleted; }
  public void setFlDeleted(boolean flDeleted) { this.flDeleted = new Boolean(flDeleted); }
  
  public String getAssocFirstName() { return this.assocFirstName; }
  public void setAssocFirstName(String assocFirstName) { this.assocFirstName = assocFirstName; }

  public String getAssocLastName() { return this.assocLastName; }
  public void setAssocLastName(String assocLastName) { this.assocLastName = assocLastName; }
  
  public String getDeStrRt() { return this.deStrRt; }
  public void setDeStrRt(String deStrRt) { this.deStrRt = deStrRt; }
  
  
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCustCreditHistOracleBean bean = new ArmCustCreditHistOracleBean();
      bean.dcTransaction = getDateFromResultSet(rs, "DC_TRANSACTION");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.tyTrn = getStringFromResultSet(rs, "TY_TRN");
      bean.custId = getStringFromResultSet(rs, "CUST_ID");
      bean.assoc = getStringFromResultSet(rs, "ASSOC");
      bean.amount = getCurrencyFromResultSet(rs, "AMOUNT");
      bean.flDeleted = getBooleanFromResultSet(rs, "FL_DELETED");
      bean.assocFirstName = getStringFromResultSet(rs, "FN_PRS");
	  bean.assocLastName = getStringFromResultSet(rs, "LN_PRS");
	  bean.deStrRt = getStringFromResultSet(rs, "DE_STR_RT");
      list.add(bean);
    }
    return (ArmCustCreditHistOracleBean[]) list.toArray(new ArmCustCreditHistOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getDcTransaction(), Types.TIMESTAMP);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getTyTrn(), Types.VARCHAR);
    addToList(list, this.getCustId(), Types.VARCHAR);
    addToList(list, this.getAssoc(), Types.VARCHAR);
    addToList(list, this.getAmount(), Types.VARCHAR);
    addToList(list, this.getFlDeleted(), Types.DECIMAL);
    return list;
  }

}
