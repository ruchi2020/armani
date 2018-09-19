//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
*@author vivek.sawant
*
* This class is an object representation of the Arts database table ARM_CUST_DISCOUNTS<BR>
* Followings are the column of the table: <BR>
*     RECORD_TYPE -- CHAR(10 BYTE)<BR>
*     MEMBER_NUM -- VARCHAR2(50 BYTE)<BR>
*     BRAND --VARCHAR2(30 BYTE)<BR>
*     DISCOUNT -- NUMBER(30,0)<BR>

*/
public class ArmCustDiscountsOracleBean extends BaseOracleBean  {

	public ArmCustDiscountsOracleBean() {}
	public static String selectSql = "select RECORD_TYPE, MEMBER_NUM, BRAND, DISCOUNT FROM ARM_CUST_DISCOUNTS";
	public static String insertSql = "insert into ARM_CUST_DISCOUNTS(RECORD_TYPE, MEMBER_NUM, BRAND, DISCOUNT) values(?, ?, ?, ?)";
	public static String updateSql = "update ARM_CUST_DISCOUNTS set RECORD_TYPE = ?, MEMBER_NUM = ?, BRAND = ?, DISCOUNT = ?";
	public static String deleteSql = "delete from ARM_CUST_DISCOUNTS ";

	public static String TABLE_NAME = "ARM_CUST_DISCOUNTS";
	public static String COL_RECORD_TYPE = "ARM_CUST_DISCOUNTS.RECORD_TYPE";
	public static String COL_MEMBER_NUM = "ARM_CUST_DISCOUNTS.MEMBER_NUM";
	public static String COL_BRAND = "ARM_CUST_DISCOUNTS.BRAND";
	public static String COL_DISCOUNT = "ARM_CUST_DISCOUNTS.DISCOUNT";
	
	public String getSelectSql() { return selectSql; }
    public String getInsertSql() { return insertSql; }
	public String getUpdateSql() { return updateSql; }
	public String getDeleteSql() { return deleteSql; }
	
	private String record_type;
	private String membership_num;
	private String brand;
	private double discount;

	public String getBrand() {return this.brand;}
	public void setBrand(String brand) {this.brand = brand;}
	
	public double getDiscount() {return this.discount;}
	public void setDiscount(double discount) {this.discount = discount;}
	
	public String getMembership_num() {	return this.membership_num;}
	public void setMembership_num(String membership_num) {this.membership_num = membership_num;}
	
	public String getRecord_type() {return this.record_type;}
	public void setRecord_type(String record_type) {this.record_type = record_type;}
	
	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
	    ArrayList list = new ArrayList();
	    while(rs.next()) {
	    ArmCustDiscountsOracleBean bean = new ArmCustDiscountsOracleBean();
	      bean.record_type = getStringFromResultSet(rs, "RECORD_TYPE");
	      bean.membership_num = getStringFromResultSet(rs, "MEMBER_NUM");
	      bean.brand = getStringFromResultSet(rs, "BRAND");
	      bean.discount = getDoubleFromResultSet(rs, "DISCOUNT");
	      list.add(bean);
	    }
	    return (ArmCustDiscountsOracleBean[]) list.toArray(new ArmCustDiscountsOracleBean[0]);
	  }

	public List toList() {
	    List list = new ArrayList();
	    addToList(list, this.getRecord_type(), Types.VARCHAR);
	    addToList(list, this.getMembership_num(), Types.VARCHAR);
	    addToList(list, this.getBrand(), Types.VARCHAR);
	    addToList(list, this.getDiscount(), Types.FLOAT);
	    return list;
	  }
	
	
}
