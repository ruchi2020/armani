package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
*@author vivek.sawant
*
* This class is an object representation of the Arts database table ARM_VIP_DISCOUNT_DETAIL<BR>
* Followings are the column of the table: <BR>
*   CUSTOMER_ID -- VARCHAR2(30 BYTE) <BR>
*   MEMBERSHIP_NUMBER -- VARCHAR2(50 BYTE)<BR>
*   EXPIRY_DATE --DATE <BR>
*/
public class ArmVipDiscountDetailOracleBean extends BaseOracleBean{
 
	public ArmVipDiscountDetailOracleBean(){}

	public static String selectSql = "select CUSTOMER_ID, MEMBERSHIP_NUMBER, EXPIRY_DATE FROM ARM_VIP_DISCOUNT_DETAIL";
	public static String insertSql = "insert into ARM_VIP_DISCOUNT_DETAIL(CUSTOMER_ID, MEMBERSHIP_NUMBER, EXPIRY_DATE) values(?, ?, ?)";
	public static String updateSql = "update ARM_VIP_DISCOUNT_DETAIL set CUSTOMER_ID = ?, MEMBERSHIP_NUMBER = ?, EXPIRY_DATE = ?";
	public static String deleteSql = "delete from ARM_VIP_DISCOUNT_DETAIL ";
	
	public static String TABLE_NAME = "ARM_VIP_DISCOUNT_DETAIL";
	public static String COL_CUSTOMER_ID = "ARM_VIP_DISCOUNT_DETAIL.CUSTOMER_ID";
	public static String COL_MEMBERSHIP_NUMBER = "ARM_VIP_DISCOUNT_DETAIL.MEMBERSHIP_NUMBER";
	public static String COL_EXPIRY_DATE = "ARM_VIP_DISCOUNT_DETAIL.EXPIRY_DATE";
	
	public String getSelectSql() { return selectSql; }
    public String getInsertSql() { return insertSql; }
	public String getUpdateSql() { return updateSql; }
	public String getDeleteSql() { return deleteSql; }
	
	private String cust_id;
	private String membership_no;
	private Date expiryDate;

	public String getCust_id() {return this.cust_id;}
	public void setCust_id(String cust_id) {this.cust_id = cust_id;}
	
	public Date getExpiryDate() {return this.expiryDate;}
	public void setExpiryDate(Date expiryDate) {this.expiryDate = expiryDate;}
	
	public String getMembership_no() {	return this.membership_no;}
	public void setMembership_no(String membership_id) {this.membership_no = membership_id;}
	
	
	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
	    ArrayList list = new ArrayList();
	    while(rs.next()) {
	    	ArmVipDiscountDetailOracleBean bean = new ArmVipDiscountDetailOracleBean();
	      bean.cust_id = getStringFromResultSet(rs, "CUSTOMER_ID");
	      bean.membership_no = getStringFromResultSet(rs, "MEMBERSHIP_NUMBER");
	      bean.expiryDate = getDateFromResultSet(rs, "EXPIRY_DATE");
	   
	      list.add(bean);
	    }
	    return (ArmVipDiscountDetailOracleBean[]) list.toArray(new ArmVipDiscountDetailOracleBean[0]);
	  }

	public List toList() {
	    List list = new ArrayList();
	    addToList(list, this.getCust_id(), Types.VARCHAR);
	    addToList(list, this.getMembership_no(), Types.VARCHAR);
	    addToList(list, this.getExpiryDate(), Types.DATE);
	   
	    return list;
	  }

}
