package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ArmAirportListOracleBean extends BaseOracleBean{
	
	public ArmAirportListOracleBean() {}
	
    public static String selectSql = "select AIRPORT_DESCR, AIRPORT_CODE, AREACODE, FL_TAXFREE from ARM_AIRPORTS";
    public static String insertSql = "insert into ARM_AIRPORTS (AIRPORT_DESCR, AIRPORT_CODE, AREACODE, FL_TAXFREE)values (?, ?, ?, ?)";
    public static String updateSql = "update ARM_AIRPORTS set AIRPORT_DESCR = ?, AIRPORT_CODE = ?, AREACODE = ?, FL_TAXFREE = ?";
    public static String deleteSql = "delete from ARM_AIRPORTS ";
   
    public static String TABLE_NAME = "ARM_AIRPORTS";
    public static String COL_AIRPORT_DESCR = "ARM_AIRPORTS.AIRPORT_DESCR";
    public static String COL_AIRPORT_CODE = "ARM_AIRPORTS.AIRPORT_CODE";
    public static String COL_AREACODE = "ARM_AIRPORTS.AREACODE";
    public static String COL_FL_TAXFREE = "ARM_AIRPORTS.FL_TAXFREE";
    
    private String airportDesc;
    private String airportCode;
    private String areaCode;
    private String flag;
    
    public String getAirportDesc() { return this.airportDesc; }
    public void setAirportDesc(String airportDesc) { this.airportDesc = airportDesc; }
    
    public String getAirportCode() { return this.airportCode; }
    public void setAirportCode(String airportCode) { this.airportCode = airportCode; }
    
    public String getAreaCode() { return this.areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    
    public String getFlag() { return this.flag; }
    public void setFlag(String flag) { this.flag = flag; }
    
    @Override
	public String getSelectSql() {
		// TODO Auto-generated method stub
		return selectSql;
	}

	@Override
	public String getInsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeleteSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseOracleBean[] getDatabeans(ResultSet rs)
			throws SQLException {
		ArrayList list = new ArrayList();
	    while(rs.next()) {
	    ArmAirportListOracleBean bean = new ArmAirportListOracleBean();
	      bean.airportDesc = getStringFromResultSet(rs, "AIRPORT_DESCR");
	      bean.airportCode = getStringFromResultSet(rs, "AIRPORT_CODE");
	      bean.areaCode = getStringFromResultSet(rs, "AREACODE");
	      bean.flag = getStringFromResultSet(rs, "FL_TAXFREE");
	      list.add(bean);
	      
	    }
	    System.out.println("list="+list);
	    return (ArmAirportListOracleBean[]) list.toArray(new ArmAirportListOracleBean[list.size()]);
	  
	}
	public String getSelectForAirportSql() {
		// TODO Auto-generated method stub
		return selectSql;
	}
	@Override
	public List toList() {
		List list = new ArrayList();
	    addToList(list, this.getAirportDesc(), Types.VARCHAR);
	    addToList(list, this.getAirportCode(), Types.VARCHAR);
	    addToList(list, this.getAreaCode(), Types.VARCHAR);
	    addToList(list, this.getFlag(), Types.VARCHAR);
	    return list;
	}

}
