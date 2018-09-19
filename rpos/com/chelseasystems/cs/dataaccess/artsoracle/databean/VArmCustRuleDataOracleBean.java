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
 * V_ARM_CUST_RULE_DATA<BR>
 * Followings are the column of the table: <BR>
 * NET_AMOUNT -- VARCHAR2(4000)<BR>
 * QUANTITY -- NUMBER(22)<BR>
 * PRODUCT_CD -- VARCHAR2(20)<BR>
 * RECORD_TYPE -- VARCHAR2(20)<BR>
 * ID_CT -- VARCHAR2(128)<BR>
 * PRIORITY -- NUMBER(22)<BR>
 * ED_CO -- VARCHAR2(20)<BR>
 * CURRENCY_TYPE -- VARCHAR2(40)<BR>
 * SOCIETY_CODE -- VARCHAR2(50)<BR>
 * ID_BRAND -- VARCHAR(50)<BR>
 * DSC_PERCENT -- VARCHAR<BR>
 * This class updated added new column in View i.e. VALUE, START_DATE, END_DATE  : PCR for Employee budget threshold for multi level discount 29th May 2017 (EUROPE)
 */
public class VArmCustRuleDataOracleBean extends BaseOracleBean {

	public VArmCustRuleDataOracleBean() {
	}


	// public static String selectSql = "select NET_AMOUNT, QUANTITY,
	// PRODUCT_CD, RECORD_TYPE, ID_CT, PRIORITY, ED_CO, CURRENCY_TYPE,
	// SOCIETY_CODE from V_ARM_CUST_RULE_DATA ";
	
	public static String selectSql = "SELECT NET_AMOUNT, QUANTITY, PRODUCT_CD, RECORD_TYPE, ID_CT, PRIORITY, ED_CO, CURRENCY_TYPE, SOCIETY_CODE, ID_BRAND, DSC_PERCENT, VALUE, START_DATE, END_DATE FROM V_ARM_CUST_RULE_DATA";
			
	public static String insertSql = "insert into V_ARM_CUST_RULE_DATA (NET_AMOUNT, QUANTITY, PRODUCT_CD, RECORD_TYPE, ID_CT, PRIORITY, ED_CO, CURRENCY_TYPE, SOCIETY_CODE, ID_BRAND, DSC_PERCENT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static String updateSql = "update V_ARM_CUST_RULE_DATA set NET_AMOUNT = ?, QUANTITY = ?, PRODUCT_CD = ?, RECORD_TYPE = ?, ID_CT = ?, PRIORITY = ?, ED_CO = ?, CURRENCY_TYPE = ?, SOCIETY_CODE = ?,ID_BRAND = ?, DSC_PERCENT = ?";

	public static String deleteSql = "delete from V_ARM_CUST_RULE_DATA ";

	public static String TABLE_NAME = "V_ARM_CUST_RULE_DATA";

	public static String COL_NET_AMOUNT = "V_ARM_CUST_RULE_DATA.NET_AMOUNT";

	public static String COL_QUANTITY = "V_ARM_CUST_RULE_DATA.QUANTITY";

	public static String COL_PRODUCT_CD = "V_ARM_CUST_RULE_DATA.PRODUCT_CD";

	public static String COL_RECORD_TYPE = "V_ARM_CUST_RULE_DATA.RECORD_TYPE";

	public static String COL_ID_CT = "V_ARM_CUST_RULE_DATA.ID_CT";

	public static String COL_PRIORITY = "V_ARM_CUST_RULE_DATA.PRIORITY";

	public static String COL_ED_CO = "V_ARM_CUST_RULE_DATA.ED_CO";

	public static String COL_CURRENCY_TYPE = "V_ARM_CUST_RULE_DATA.CURRENCY_TYPE";

	public static String COL_SOCIETY_CODE = "V_ARM_CUST_RULE_DATA.SOCIETY_CODE";

	public static String COL_ID_BRAND = "V_ARM_CUST_RULE_DATA.ID_BRAND";

	public static String COL_DSC_LEVEL = "V_ARM_CUST_RULE_DATA.DSC_PERCENT";
	
	public static String COL_VALUE = "V_ARM_CUST_RULE_DATA.VALUE";
	
	public static String COL_START_DATE = "V_ARM_CUST_RULE_DATA.START_DATE";

	public static String COL_END_DATE = "V_ARM_CUST_RULE_DATA.END_DATE";


	public String getSelectSql() {
		return selectSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public String getDeleteSql() {
		return deleteSql;
	}

	private String netAmount;

	private Long quantity;

	private String productCd;

	private String recordType;

	private String idCt;

	private Long priority;

	private String edCo;

	private String currencyType;

	private String societyCode;

	
	//the two parameter added by vivek sawant on 21 Nov 2008
	private String iDBrand;

	private String dsc_level;
	
	private String value;
	
	private Date startDate;
	
	private Date endDate;
	
	

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getNetAmount() {
		return this.netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public Long getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = new Long(quantity);
	}

	public void setQuantity(int quantity) {
		this.quantity = new Long((long) quantity);
	}

	public String getProductCd() {
		return this.productCd;
	}

	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}

	public String getRecordType() {
		return this.recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getIdCt() {
		return this.idCt;
	}

	public void setIdCt(String idCt) {
		this.idCt = idCt;
	}

	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public void setPriority(long priority) {
		this.priority = new Long(priority);
	}

	public void setPriority(int priority) {
		this.priority = new Long((long) priority);
	}

	public String getEdCo() {
		return this.edCo;
	}

	public void setEdCo(String edCo) {
		this.edCo = edCo;
	}

	public String getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getSocietyCode() {
		return this.societyCode;
	}

	public void setSocietyCode(String societyCode) {
		this.societyCode = societyCode;
	}

	public String getIDBrand() {
		return this.iDBrand;
	}

	public void setIDBrand(String brand) {
		this.iDBrand = brand;
	}
 // new code added by vivek on 21 nov 08
	public String getDsc_level() {
		return dsc_level;
	}

	public void setDsc_level(String dsc_level) {
		this.dsc_level = dsc_level;
	}
	// end of code

	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
		ArrayList list = new ArrayList();
		while (rs.next()) {
			VArmCustRuleDataOracleBean bean = new VArmCustRuleDataOracleBean();
			
			bean.netAmount = getStringFromResultSet(rs, "NET_AMOUNT");
			bean.quantity = getLongFromResultSet(rs, "QUANTITY");
			bean.productCd = getStringFromResultSet(rs, "PRODUCT_CD");
			bean.recordType = getStringFromResultSet(rs, "RECORD_TYPE");
			bean.idCt = getStringFromResultSet(rs, "ID_CT");
			bean.priority = getLongFromResultSet(rs, "PRIORITY");
			bean.edCo = getStringFromResultSet(rs, "ED_CO");
			bean.currencyType = getStringFromResultSet(rs, "CURRENCY_TYPE");
			bean.societyCode = getStringFromResultSet(rs, "SOCIETY_CODE");
			//new code added by vivek on 21 Nov 08
			bean.iDBrand = getStringFromResultSet(rs, "ID_BRAND");
			bean.dsc_level = getStringFromResultSet(rs, "DSC_PERCENT");
			// end of code
			bean.value = getStringFromResultSet(rs, "VALUE");
			bean.startDate = getDateFromResultSet(rs, "START_DATE");
			bean.endDate = getDateFromResultSet(rs, "END_DATE");
			list.add(bean);
		}
		return (VArmCustRuleDataOracleBean[]) list
				.toArray(new VArmCustRuleDataOracleBean[0]);
	}

	public List toList() {
		List list = new ArrayList();
		addToList(list, this.getNetAmount(), Types.VARCHAR);
		addToList(list, this.getQuantity(), Types.DECIMAL);
		addToList(list, this.getProductCd(), Types.VARCHAR);
		addToList(list, this.getRecordType(), Types.VARCHAR);
		addToList(list, this.getIdCt(), Types.VARCHAR);
		addToList(list, this.getPriority(), Types.DECIMAL);
		addToList(list, this.getEdCo(), Types.VARCHAR);
		addToList(list, this.getCurrencyType(), Types.VARCHAR);
		addToList(list, this.getSocietyCode(), Types.VARCHAR);
       //new code added by vivek on 21 Nov 08
		addToList(list, this.getIDBrand(), Types.VARCHAR);
		addToList(list, this.getDsc_level(), Types.VARCHAR);
		//end of code.
		addToList(list, this.value, Types.VARCHAR);
		addToList(list,this.startDate, Types.DATE);
		addToList(list,this.endDate, Types.DATE);
		return list;
	}

}
