package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* AUTHOR Ruchi Gupta : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
* This class is an object representation of the Arts database table ARM_EOP_DTL<BR>
* Followings are the column of the table: <BR>
*     ID_CMY -- VARCHAR2(128)<BR>
*     ID_STR_RT -- VARCHAR2(128)<BR>
*     REGISTER_ID -- VARCHAR2(128)<BR>
*     YEAR -- VARCHAR2(96)<BR>
*     MONTH -- VARCHAR2(96)<BR>
*     DAY -- CHAR(2)<BR>
*     CD_TYPE -- CHAR(2)<BR>
*     TOTAL -- CHAR(2)<BR>
*     CURRENCY -- VARCHAR2(40)<BR>
*     CREATE_DATE -- VARCHAR2(40)<BR>
*
*/
public class ArmEopDtlOracleBean extends BaseOracleBean{
	
	public ArmEopDtlOracleBean()
	{
		
	}
	 public static String selectSql = "select ID_CMY, ID_STR_RT, REGISTER_ID, YEAR, MONTH, DAY, CD_TYPE, TOTAL, CURRENCY, CREATE_DATE from ARM_EOP_DTL";
	 public static String insertSql = "insert into ARM_EOP_DTL (ID_CMY, ID_STR_RT, REGISTER_ID, YEAR, MONTH, DAY, CD_TYPE, TOTAL, CURRENCY, CREATE_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	 public static String updateSql = "update ARM_EOP_DTL set ID_CMY = ?, ID_STR_RT = ?, REGISTER_ID = ?, YEAR = ?, MONTH = ?, DAY=?, CD_TYPE=?, TOTAL=?, CURRENCY=?, CREATE_DATE=? ";
	 public static String deleteSql = "delete from ARM_EOP_DTL ";

	 
	 public static String TABLE_NAME      = "ARM_EOP_DTL";
     public static String COL_ID_CMY      = "ARM_EOP_DTL.ID_CMY";
	 public static String COL_ID_STR_RT   = "ARM_EOP_DTL.ID_STR_RT";
	 public static String COL_REGISTER_ID = "ARM_EOP_DTL.REGISTER_ID";
	 public static String COL_YEAR        = "ARM_EOP_DTL.YEAR";
	 public static String COL_MONTH       = "ARM_EOP_DTL.MONTH";
	 public static String COL_DAY         = "ARM_EOP_DTL.DAY";
	 public static String COL_CD_TYPE     = "ARM_EOP_DTL.CD_TYPE";
	 public static String COL_TOTAL       = "ARM_EOP_DTL.TOTAL";
	 public static String COL_CURRENCY    = "ARM_EOP_DTL.CURRENCY";
	 public static String COL_CREATE_DATE = "ARM_EOP_DTL.CREATE_DATE";
	  
	 public String getSelectSql() { return selectSql; }
	 public String getInsertSql() { return insertSql; }
	 public String getUpdateSql() { return updateSql; }
	 public String getDeleteSql() { return deleteSql; }
	  
   	 private String idCmy;
	 private String idStrRt;
	 private String registerId;
	 private String year;
	 private String month;
	 private String day;
	 private String cdType;
	 private String total;
	 private String currency;
	 private Date   createDate;
	 
	 
	public String getIdCmy() {
		return idCmy;
	}
	public void setIdCmy(String idCmy) {
		this.idCmy = idCmy;
	}
	public String getIdStrRt() {
		return idStrRt;
	}
	public void setIdStrRt(String idStrRt) {
		this.idStrRt = idStrRt;
	}
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getCdType() {
		return cdType;
	}
	public void setCdType(String cdType) {
		this.cdType = cdType;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	 
	
	@Override
	public BaseOracleBean[] getDatabeans(ResultSet rs)
			throws SQLException {
		ArrayList list=new ArrayList();
		while(rs.next())                     
		{
			ArmEopDtlOracleBean bean=new ArmEopDtlOracleBean();
			bean.idCmy=getStringFromResultSet(rs, "ID_CMY");
			bean.idStrRt=getStringFromResultSet(rs, "ID_STR_RT");
			bean.registerId=getStringFromResultSet(rs, "REGISTER_ID");
			bean.year=getStringFromResultSet(rs, "YEAR");
			bean.month=getStringFromResultSet(rs, "MONTH");
			bean.day=getStringFromResultSet(rs, "DAY");
			bean.cdType=getStringFromResultSet(rs, "CD_TYPE");
			bean.total=getStringFromResultSet(rs, "TOTAL");
			bean.currency=getStringFromResultSet(rs, "CURRENCY");
			bean.createDate=getDateFromResultSet(rs, "CREATE_DATE");
			list.add(bean);
		}
		
		return (ArmEopDtlOracleBean[]) list.toArray(new ArmEopDtlOracleBean[0]);
	}

	@Override
	public List toList() {
		    List list=new ArrayList();
		    addToList(list, this.getIdCmy(), Types.VARCHAR);
		    addToList(list, this.getIdStrRt(), Types.VARCHAR);
		    addToList(list, this.getRegisterId(), Types.VARCHAR);
		    addToList(list, this.getYear(), Types.VARCHAR);
		    addToList(list, this.getMonth(), Types.VARCHAR);
		    addToList(list, this.getDay(), Types.VARCHAR);
		    addToList(list, this.getCdType(), Types.VARCHAR);
		    addToList(list, this.getTotal(), Types.VARCHAR);
		    addToList(list, this.getCurrency(), Types.VARCHAR);
		    addToList(list, this.getCreateDate(), Types.DATE);
		    
		return list;
	}

}
