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
* This class is an object representation of the Arts database table ARM_LOYALTY<BR>
* Followings are the column of the table: <BR>
*     ID_CT -- VARCHAR2(128)<BR>
*     LOYALTY_NUM(PK) -- VARCHAR2(20)<BR>
*     TY_STR_RT -- VARCHAR2(30)<BR>
*     DC_ISSUED -- DATE(7)<BR>
*     ISSUED_BY -- VARCHAR2(128)<BR>
*     FL_STATUS -- CHAR(1)<BR>
*     CURRENT_BAL -- NUMBER(15.2)<BR>
*     LIFETIME_BAL -- NUMBER(15.2)<BR>
*     CURRENT_YEAR_BAL -- NUMBER(15.2)<BR>
*     LAST_YEAR_BAL -- NUMBER(15.2)<BR>
*     SECOND_LAST_YEAR_BAL -- NUMBER(15.2)<BR>
*
*/
public class ArmLoyaltyOracleBean extends BaseOracleBean {

public ArmLoyaltyOracleBean() {}

public static String selectSql = "select ID_CT, LOYALTY_NUM, TY_STR_RT, DC_ISSUED, ISSUED_BY, FL_STATUS, CURRENT_BAL, LIFETIME_BAL, CURRENT_YEAR_BAL, LAST_YEAR_BAL, SECOND_LAST_YEAR_BAL from ARM_LOYALTY ";
public static String insertSql = "insert into ARM_LOYALTY (ID_CT, LOYALTY_NUM, TY_STR_RT, DC_ISSUED, ISSUED_BY, FL_STATUS, CURRENT_BAL, LIFETIME_BAL, CURRENT_YEAR_BAL, LAST_YEAR_BAL, SECOND_LAST_YEAR_BAL) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
public static String updateSql = "update ARM_LOYALTY set ID_CT = ?, LOYALTY_NUM = ?, TY_STR_RT = ?, DC_ISSUED = ?, ISSUED_BY = ?, FL_STATUS = ?, CURRENT_BAL = ?, LIFETIME_BAL = ?, CURRENT_YEAR_BAL = ?, LAST_YEAR_BAL = ?, SECOND_LAST_YEAR_BAL = ? ";
public static String deleteSql = "delete from ARM_LOYALTY ";

public static String TABLE_NAME = "ARM_LOYALTY";
public static String COL_ID_CT = "ARM_LOYALTY.ID_CT";
public static String COL_LOYALTY_NUM = "ARM_LOYALTY.LOYALTY_NUM";
public static String COL_TY_STR_RT = "ARM_LOYALTY.TY_STR_RT";
public static String COL_DC_ISSUED = "ARM_LOYALTY.DC_ISSUED";
public static String COL_ISSUED_BY = "ARM_LOYALTY.ISSUED_BY";
public static String COL_FL_STATUS = "ARM_LOYALTY.FL_STATUS";
public static String COL_CURRENT_BAL = "ARM_LOYALTY.CURRENT_BAL";
public static String COL_LIFETIME_BAL = "ARM_LOYALTY.LIFETIME_BAL";
public static String COL_CURRENT_YEAR_BAL = "ARM_LOYALTY.CURRENT_YEAR_BAL";
public static String COL_LAST_YEAR_BAL = "ARM_LOYALTY.LAST_YEAR_BAL";
public static String COL_SECOND_LAST_YEAR_BAL = "ARM_LOYALTY.SECOND_LAST_YEAR_BAL";

public String getSelectSql() { return selectSql; }
public String getInsertSql() { return insertSql; }
public String getUpdateSql() { return updateSql; }
public String getDeleteSql() { return deleteSql; }

private String idCt;
private String loyaltyNum;
private String tyStrRt;
private Date dcIssued;
private String issuedBy;
private Boolean flStatus;
private Double currentBal;
private Double lifetimeBal;
private Double currentYearBal;
private Double lastYearBal;
private Double secondLastYearBal;

public String getIdCt() { return this.idCt; }
public void setIdCt(String idCt) { this.idCt = idCt; }

public String getLoyaltyNum() { return this.loyaltyNum; }
public void setLoyaltyNum(String loyaltyNum) { this.loyaltyNum = loyaltyNum; }

public String getTyStrRt() { return this.tyStrRt; }
public void setTyStrRt(String tyStrRt) { this.tyStrRt = tyStrRt; }

public Date getDcIssued() { return this.dcIssued; }
public void setDcIssued(Date dcIssued) { this.dcIssued = dcIssued; }

public String getIssuedBy() { return this.issuedBy; }
public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }

public Boolean getFlStatus() { return this.flStatus; }
public void setFlStatus(Boolean flStatus) { this.flStatus = flStatus; }
public void setFlStatus(boolean flStatus) { this.flStatus = new Boolean(flStatus); }

public Double getCurrentBal() { return this.currentBal; }
public void setCurrentBal(Double currentBal) { this.currentBal = currentBal; }
public void setCurrentBal(double currentBal) { this.currentBal = new Double(currentBal); }

public Double getLifetimeBal() { return this.lifetimeBal; }
public void setLifetimeBal(Double lifetimeBal) { this.lifetimeBal = lifetimeBal; }
public void setLifetimeBal(double lifetimeBal) { this.lifetimeBal = new Double(lifetimeBal); }

public Double getCurrentYearBal() { return this.currentYearBal; }
public void setCurrentYearBal(Double currentYearBal) { this.currentYearBal = currentYearBal; }
public void setCurrentYearBal(double currentYearBal) { this.currentYearBal = new Double(currentYearBal); }

public Double getLastYearBal() { return this.lastYearBal; }
public void setLastYearBal(Double lastYearBal) { this.lastYearBal = lastYearBal; }
public void setLastYearBal(double lastYearBal) { this.lastYearBal = new Double(lastYearBal); }

public Double getSecondLastYearBal() { return this.secondLastYearBal; }
public void setSecondLastYearBal(Double secondLastYearBal) { this.secondLastYearBal = secondLastYearBal; }
public void setSecondLastYearBal(double secondLastYearBal) { this.secondLastYearBal = new Double(secondLastYearBal); }

public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
 ArrayList list = new ArrayList();
 while(rs.next()) {
   ArmLoyaltyOracleBean bean = new ArmLoyaltyOracleBean();
   bean.idCt = getStringFromResultSet(rs, "ID_CT");
   bean.loyaltyNum = getStringFromResultSet(rs, "LOYALTY_NUM");
   bean.tyStrRt = getStringFromResultSet(rs, "TY_STR_RT");
   bean.dcIssued = getDateFromResultSet(rs, "DC_ISSUED");
   bean.issuedBy = getStringFromResultSet(rs, "ISSUED_BY");
   bean.flStatus = getBooleanFromResultSet(rs, "FL_STATUS");
   bean.currentBal = getDoubleFromResultSet(rs, "CURRENT_BAL");
   bean.lifetimeBal = getDoubleFromResultSet(rs, "LIFETIME_BAL");
   bean.currentYearBal = getDoubleFromResultSet(rs, "CURRENT_YEAR_BAL");
   bean.lastYearBal = getDoubleFromResultSet(rs, "LAST_YEAR_BAL");
   bean.secondLastYearBal = getDoubleFromResultSet(rs, "SECOND_LAST_YEAR_BAL");
   list.add(bean);
 }
 return (ArmLoyaltyOracleBean[]) list.toArray(new ArmLoyaltyOracleBean[0]);
}

public List toList() {
 List list = new ArrayList();
 addToList(list, this.getIdCt(), Types.VARCHAR);
 addToList(list, this.getLoyaltyNum(), Types.VARCHAR);
 addToList(list, this.getTyStrRt(), Types.VARCHAR);
 addToList(list, this.getDcIssued(), Types.TIMESTAMP);
 addToList(list, this.getIssuedBy(), Types.VARCHAR);
 addToList(list, this.getFlStatus(), Types.DECIMAL);
 addToList(list, this.getCurrentBal(), Types.DECIMAL);
 addToList(list, this.getLifetimeBal(), Types.DECIMAL);
 addToList(list, this.getCurrentYearBal(), Types.DECIMAL);
 addToList(list, this.getLastYearBal(), Types.DECIMAL);
 addToList(list, this.getSecondLastYearBal(), Types.DECIMAL);
 return list;
}

}
