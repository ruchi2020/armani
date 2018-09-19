package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;


/**
*
* This class is an object representation of the Arts database table ARM_STG_CUST_CRM<BR>
* Followings are the column of the table: <BR>
*     CUSTOMER_ID -- VARCHAR2(20)<BR>
*     DATA_POPULATION_DT -- DATE<BR>
*     STORE_ID -- VARCHAR2(6)<BR>
*     REGISTER_ID -- VARCHAR2(2)<BR>
*     STG_PROCESS_DATE -- DATE<BR>
*     STATUS -- NUMBER(1)<BR>
*
*/
public class ArmStgCustCrmOracleBean  extends BaseOracleBean{

	public ArmStgCustCrmOracleBean() {}

	  public static String selectSql = "SELECT CUSTOMER_ID,DATA_POPULATION_DT,STORE_ID,REGISTER_ID,STG_PROCESS_DATE,STATUS FROM ARM_STG_CUST_CRM";
	  public static String insertSql = "INSERT INTO ARM_STG_CUST_CRM (CUSTOMER_ID,DATA_POPULATION_DT,STORE_ID,REGISTER_ID,STG_PROCESS_DATE,STATUS) VALUES(?,?,?,?,?,?)";
	  public static String updateSql = "UPDATE ARM_STG_CUST_CRM SET CUSTOMER_ID = ?,DATA_POPULATION_DT = ?,STORE_ID = ?,REGISTER_ID = ?,STG_PROCESS_DATE = ?,STATUS  = ?";
	  public static String deleteSql = "DELETE FROM ARM_STG_CUST_CRM ";
	  
	  public static String TABLE_NAME = "ARM_STG_CUST_CRM";
	  public static String COL_CUSTOMER_ID = "ARM_STG_CUST_CRM.CUSTOMER_ID";
	  public static String COL_DATA_POPULATION_DT = "ARM_STG_CUST_CRM.DATA_POPULATION_DT";
	  public static String COL_STORE_ID = "ARM_STG_CUST_CRM.STORE_ID";
	  public static String COL_REGISTER_ID = "ARM_STG_CUST_CRM.REGISTER_ID";
	  public static String COL_STG_PROCESS_DATE = "ARM_STG_CUST_CRM.STG_PROCESS_DATE";
	  public static String COL_STATUS = "ARM_STG_CUST_CRM.STATUS";
	  
	  
	  public static final int STATUS_NOT_PROCESSED = 0;
	  public static final int STATUS_PROCESSED = 1;
	  
	  public String getSelectSql() { return selectSql; }
	  public String getInsertSql() { return insertSql; }
	  public String getUpdateSql() { return updateSql; }
	  public String getDeleteSql() { return deleteSql; }

	  
	  private String customerID;
	  private String dataPopulationDT;
	  private String storeID;
	  private String registerID;
	  private String stgProcessDate;
	  private String status;

	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getDataPopulationDT() {
		return dataPopulationDT;
	}
	public void setDataPopulationDT(String dataPopulationDT) {
		this.dataPopulationDT = dataPopulationDT;
	}
	public String getRegisterID() {
		return registerID;
	}
	public void setRegisterID(String registerID) {
		this.registerID = registerID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStgProcessDate() {
		return stgProcessDate;
	}
	public void setStgProcessDate(String stgProcessDate) {
		this.stgProcessDate = stgProcessDate;
	}
	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	
	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
	    ArrayList list = new ArrayList();
	    while(rs.next()) {
	      ArmStgCustCrmOracleBean bean = new ArmStgCustCrmOracleBean();
	      bean.customerID = getStringFromResultSet(rs, "CUSTOMER_ID");
	      bean.dataPopulationDT = getStringFromResultSet(rs, "DATA_POPULATION_DT");
	      bean.storeID = getStringFromResultSet(rs, "STORE_ID");
	      bean.registerID = getStringFromResultSet(rs, "REGISTER_ID");
	      bean.stgProcessDate = getStringFromResultSet(rs, "STG_PROCESS_DATE");
	      bean.status = getStringFromResultSet(rs, "STATUS");
	      list.add(bean);
	    }
	    return (ArmStgCustCrmOracleBean[]) list.toArray(new ArmStgCustCrmOracleBean[0]);
	  }

	  public List toList() {
	    List list = new ArrayList();
	    addToList(list, this.getCustomerID(), Types.VARCHAR);
	    addToList(list, this.getDataPopulationDT(), Types.VARCHAR);
	    addToList(list, this.getStoreID(), Types.VARCHAR);
	    addToList(list, this.getRegisterID(), Types.VARCHAR);
	    addToList(list, this.getStgProcessDate(), Types.VARCHAR);
	    addToList(list, this.getStatus(), Types.TIMESTAMP);
	    return list;
	  }

	
	  
}
