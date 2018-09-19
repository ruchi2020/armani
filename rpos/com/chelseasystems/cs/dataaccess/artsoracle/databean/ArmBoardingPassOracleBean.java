package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ArmBoardingPassOracleBean extends BaseOracleBean{
	
	public ArmBoardingPassOracleBean() {}
	
    public static String selectSql = "select TRANSACTION_ID, DESTINATION_DETAILS, FLIGHT_NUMBER, CHECKIN_NUMBER, COMP, SEAT_NUMBER, CUSTOMER_NAME, ENTRY_TYPE from ARM_BOARDING_PASS";
    public static String insertSql = "insert into ARM_BOARDING_PASS (TRANSACTION_ID, DESTINATION_DETAILS, FLIGHT_NUMBER, CHECKIN_NUMBER, COMP, SEAT_NUMBER, CUSTOMER_NAME, ENTRY_TYPE)values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static String updateSql = "update ARM_BOARDING_PASS set TRANSACTION_ID = ?, DESTINATION_DETAILS = ?, FLIGHT_NUMBER = ?, CHECKIN_NUMBER = ?, COMP=?, SEAT_NUMBER=?, CUSTOMER_NAME=?, ENTRY_TYPE=?";
    public static String deleteSql = "delete from ARM_BOARDING_PASS ";
   
    public static String TABLE_NAME = "ARM_BOARDING_PASS";
    public static String COL_TRANSACTION_ID = "ARM_BOARDING_PASS.TRANSACTION_ID";
    public static String COL_DESTINATION_DETAILS = "ARM_BOARDING_PASS.DESTINATION_DETAILS";
    public static String COL_FLIGHT_NUMBER = "ARM_BOARDING_PASS.FLIGHT_NUMBER";
    public static String COL_CHECKIN_NUMBER = "ARM_BOARDING_PASS.CHECKIN_NUMBER";
    public static String COL_COMP = "ARM_BOARDING_PASS.COMP";
    public static String COL_SEAT_NUMBER = "ARM_BOARDING_PASS.SEAT_NUMBER";
    public static String COL_CUSTOMER_NAME = "ARM_BOARDING_PASS.CUSTOMER_NAME";
    public static String COL_ENTRY_TYPE = "ARM_BOARDING_PASS.ENTRY_TYPE";

    private String transID;
    private String destination;
    private String flightNo;
    private String checkInNo;
    private String comp;
    private String seatNo;
    private String custName;
    private String entry;
    
    public String getTransID() { return this.transID; }
    public void setTransID(String transID) { this.transID = transID; }
    
    public String getDestination() { return this.destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public String getFlightNo() { return this.flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }
    
    public String getCheckInNo() { return this.checkInNo; }
    public void setCheckInNo(String checkInNo) { this.checkInNo = checkInNo; }
    
    public String getComp() { return this.comp; }
    public void setComp(String comp) { this.comp = comp; }
    
    public String getSeatNo() { return this.seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }
    
    public String getCustName() { return this.custName; }
    public void setCustName(String custName) { this.custName = custName; }
    
    public String getEntry() { return this.entry; }
    public void setEntry(String entry) { this.entry = entry; }
    
    public String getSelectSql() { return selectSql; }
    public String getInsertSql() { return insertSql; }
    public String getUpdateSql() { return updateSql; }
    public String getDeleteSql() { return deleteSql; }

	@Override
	public BaseOracleBean[] getDatabeans(ResultSet rs)
			throws SQLException {
		ArrayList list = new ArrayList();
	    while(rs.next()) {
	    ArmBoardingPassOracleBean bean = new ArmBoardingPassOracleBean();
	      bean.transID = getStringFromResultSet(rs, "TRANSACTION_ID");
	      bean.destination = getStringFromResultSet(rs, "DESTINATION_DETAILS");
	      bean.flightNo = getStringFromResultSet(rs, "FLIGHT_NUMBER");
	      bean.checkInNo = getStringFromResultSet(rs, "CHECKIN_NUMBER");
	      bean.comp = getStringFromResultSet(rs, "COMP");
	      bean.seatNo = getStringFromResultSet(rs, "SEAT_NUMBER");
	      bean.custName = getStringFromResultSet(rs, "CUSTOMER_NAME");
	      bean.entry = getStringFromResultSet(rs, "ENTRY_TYPE");
	      list.add(bean);
	      
	    }
	    return (ArmBoardingPassOracleBean[]) list.toArray(new ArmBoardingPassOracleBean[list.size()]);
	  
	}
	public String getSelectForAirportSql() {
		// TODO Auto-generated method stub
		return selectSql;
	}
	@Override
	public List toList() {
		List list = new ArrayList();
	    addToList(list, this.getTransID(), Types.VARCHAR);
	    addToList(list, this.getDestination(), Types.VARCHAR);
	    addToList(list, this.getFlightNo(), Types.VARCHAR);
	    addToList(list, this.getCheckInNo(), Types.VARCHAR);
	    addToList(list, this.getComp(), Types.VARCHAR);
	    addToList(list, this.getSeatNo(), Types.VARCHAR);
	    addToList(list, this.getCustName(), Types.VARCHAR);
	    addToList(list, this.getEntry(), Types.VARCHAR);
	    return list;
	}

}

