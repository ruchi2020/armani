//@author Mahesh Nandure: 12-1-2017: markdown markup cr by sergio for EUROPE 

package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArmStgMrkprzOracleBean extends BaseOracleBean{
	
	public ArmStgMrkprzOracleBean()
	{
		
	}
	 public static String selectSql = "select COUNTRY, BARCODE, PRICEDMS, PRICEOUT, COEFF, DATA_INS, DATA_MOD from ARM_STG_MRKPRZ";
	  public static String insertSql = "insert into ARM_STG_MRKPRZ (COUNTRY, BARCODE, PRICEDMS, PRICEOUT, COEFF, DATA_INS, DATA_MOD) values (?, ?, ?, ?, ?, ?, ?)";
	  public static String updateSql = "update ARM_STG_MRKPRZ set COUNTRY = ?, BARCODE = ?, PRICEDMS = ?, PRICEOUT = ?, COEFF = ?, DATA_INS=?, DATA_MOD=? ";
	  public static String deleteSql = "delete from ARM_STG_MRKPRZ ";
	  
	  public static String TABLE_NAME = "ARM_STG_MRKPRZ";
	  public static String COL_COUNTRY = "ARM_STG_MRKPRZ.COUNTRY";
	  public static String COL_BARCODE = "ARM_STG_MRKPRZ.BARCODE";
	  public static String COL_PRICEDMS = "ARM_STG_MRKPRZ.PRICEDMS";
	  public static String COL_PRICEOUT = "ARM_STG_MRKPRZ.PRICEOUT";
	  public static String COL_COEFF = "ARM_STG_MRKPRZ.COEFF";
	  public static String COL_DATA_INS = "ARM_STG_MRKPRZ.DATA_INS";
	  public static String COL_DATA_MOD = "ARM_STG_MRKPRZ.DATA_MOD";
	  

	@Override
	public String getSelectSql() {
		return selectSql;
	}

	@Override
	public String getInsertSql() {
		return insertSql;
	}

	@Override
	public String getUpdateSql() {
		return updateSql;
	}

	@Override
	public String getDeleteSql() {
		return deleteSql;
	}
	
	private String country;
	private String barcode;
	private Double priceDMS;
	private Double priceOUT;
	private Double coeff;
	private Date insDate;
	private Date modDate;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getPriceDMS() {
		return priceDMS;
	}

	public void setPriceDMS(Double priceDMS) {
		this.priceDMS = priceDMS;
	}

	public void setPriceOUT(Double priceOUT) {
		this.priceOUT = priceOUT;
	}

	public void setCoeff(Double coeff) {
		this.coeff = coeff;
	}

	public void setPriceDMS(double priceDMS) {
		this.priceDMS = new Double(priceDMS);
	}

	public Double getPriceOUT() {
		return priceOUT;
	}

	public void setPriceOUT(double priceOUT) {
		this.priceOUT = new Double(priceOUT);
	}

	public Double getCoeff() {
		return this.coeff;
	}

	public void setCoeff(double coefficient) {
		this.coeff = new Double(coefficient);
	}
	
	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	@Override
	public BaseOracleBean[] getDatabeans(ResultSet rs)
			throws SQLException {
		
		ArrayList list=new ArrayList();
		while(rs.next())                     
		{
			ArmStgMrkprzOracleBean bean=new ArmStgMrkprzOracleBean();
			bean.country=getStringFromResultSet(rs, "COUNTRY");
			bean.barcode=getStringFromResultSet(rs, "BARCODE");
			bean.priceDMS=getDoubleFromResultSet(rs, "PRICEDMS");
			bean.priceOUT=getDoubleFromResultSet(rs, "PRICEOUT");
			bean.coeff=getDoubleFromResultSet(rs, "COEFF");
			bean.insDate=getDateFromResultSet(rs, "DATA_INS");
			bean.modDate=getDateFromResultSet(rs, "DATA_MOD");
			list.add(bean);
		}
		
		return (ArmStgMrkprzOracleBean[]) list.toArray(new ArmStgMrkprzOracleBean[0]);
	}

	@Override
	public List toList() {
		
		List list=new ArrayList();
		 addToList(list, this.getCountry(), Types.VARCHAR);
		    addToList(list, this.getBarcode(), Types.VARCHAR);
		    addToList(list, this.getPriceDMS(), Types.DECIMAL);
		    addToList(list, this.getPriceOUT(), Types.DECIMAL);
		    addToList(list, this.getCoeff(), Types.DECIMAL);
		    addToList(list, this.getInsDate(), Types.DATE);
		    addToList(list, this.getModDate(), Types.DATE);
		    
		return list;
	}
	
	

}
