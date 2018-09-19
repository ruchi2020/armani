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
 * This class is an object representation of the Arts database table
 * ARM_LOYALTY_HIST<BR>
 * Followings are the column of the table: <BR>
 * LOYALTY_NUM -- VARCHAR2(20)<BR>
 * DC_TRANSACTION -- DATE(7)<BR>
 * ID_STR_RT -- VARCHAR2(128)<BR>
 * AI_TRN -- VARCHAR2(128)<BR>
 * TY_TRN -- VARCHAR2(60)<BR>
 * CD_REASON -- VARCHAR2(200)<BR>
 * POINTS -- NUMBER(15.2)<BR>
 * 
 */
public class ArmLoyaltyHistOracleBean extends BaseOracleBean {

public ArmLoyaltyHistOracleBean() {}

public static String selectSql = "select LOYALTY_NUM, DC_TRANSACTION, ID_STR_RT, AI_TRN, TY_TRN, CD_REASON, POINTS from ARM_LOYALTY_HIST ";
public static String insertSql = "insert into ARM_LOYALTY_HIST (LOYALTY_NUM, DC_TRANSACTION, ID_STR_RT, AI_TRN, TY_TRN, CD_REASON, POINTS) values (?, ?, ?, ?, ?, ?, ?)";
public static String updateSql = "update ARM_LOYALTY_HIST set LOYALTY_NUM = ?, DC_TRANSACTION = ?, ID_STR_RT = ?, AI_TRN = ?, TY_TRN = ?, CD_REASON = ?, POINTS = ? ";
public static String deleteSql = "delete from ARM_LOYALTY_HIST ";

public static String TABLE_NAME = "ARM_LOYALTY_HIST";
public static String COL_LOYALTY_NUM = "ARM_LOYALTY_HIST.LOYALTY_NUM";
public static String COL_DC_TRANSACTION = "ARM_LOYALTY_HIST.DC_TRANSACTION";
public static String COL_ID_STR_RT = "ARM_LOYALTY_HIST.ID_STR_RT";
public static String COL_AI_TRN = "ARM_LOYALTY_HIST.AI_TRN";
public static String COL_TY_TRN = "ARM_LOYALTY_HIST.TY_TRN";
public static String COL_CD_REASON = "ARM_LOYALTY_HIST.CD_REASON";
public static String COL_POINTS = "ARM_LOYALTY_HIST.POINTS";

public String getSelectSql() { return selectSql; }
public String getInsertSql() { return insertSql; }
public String getUpdateSql() { return updateSql; }
public String getDeleteSql() { return deleteSql; }

private String loyaltyNum;
private Date dcTransaction;
private String idStrRt;
private String aiTrn;
private String tyTrn;
private String cdReason;
private Double points;

public String getLoyaltyNum() { return this.loyaltyNum; }
public void setLoyaltyNum(String loyaltyNum) { this.loyaltyNum = loyaltyNum; }

public Date getDcTransaction() { return this.dcTransaction; }
public void setDcTransaction(Date dcTransaction) { this.dcTransaction = dcTransaction; }

public String getIdStrRt() { return this.idStrRt; }
public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

public String getAiTrn() { return this.aiTrn; }
public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

public String getTyTrn() { return this.tyTrn; }
public void setTyTrn(String tyTrn) { this.tyTrn = tyTrn; }

public String getCdReason() { return this.cdReason; }
public void setCdReason(String cdReason) { this.cdReason = cdReason; }

public Double getPoints() { return this.points; }
public void setPoints(Double points) { this.points = points; }
public void setPoints(double points) { this.points = new Double(points); }

public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
 ArrayList list = new ArrayList();
 while(rs.next()) {
   ArmLoyaltyHistOracleBean bean = new ArmLoyaltyHistOracleBean();
   bean.loyaltyNum = getStringFromResultSet(rs, "LOYALTY_NUM");
   bean.dcTransaction = getDateFromResultSet(rs, "DC_TRANSACTION");
   bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
   bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
   bean.tyTrn = getStringFromResultSet(rs, "TY_TRN");
   bean.cdReason = getStringFromResultSet(rs, "CD_REASON");
   bean.points = getDoubleFromResultSet(rs, "POINTS");
   list.add(bean);
 }
 return (ArmLoyaltyHistOracleBean[]) list.toArray(new ArmLoyaltyHistOracleBean[0]);
}

public List toList() {
 List list = new ArrayList();
 addToList(list, this.getLoyaltyNum(), Types.VARCHAR);
 addToList(list, this.getDcTransaction(), Types.TIMESTAMP);
 addToList(list, this.getIdStrRt(), Types.VARCHAR);
 addToList(list, this.getAiTrn(), Types.VARCHAR);
 addToList(list, this.getTyTrn(), Types.VARCHAR);
 addToList(list, this.getCdReason(), Types.VARCHAR);
 addToList(list, this.getPoints(), Types.DECIMAL);
 return list;
}

}
