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
 * This class is an object representation of the Arts database table ARM_TAX_RATE<BR>
 * Followings are the column of the table: <BR>
 *     ZIP_CODE -- VARCHAR2(10)<BR>
 *     CITY -- VARCHAR2(50)<BR>
 *     COUNTY -- VARCHAR2(50)<BR>
 *     STATE -- VARCHAR2(10)<BR>
 *     TAX_RATE -- NUMBER(9.4)<BR>
 *     EFFECTIVE_DT -- DATE(7)<BR>
 *     TAX_JUR -- VARCHAR2(10)<BR>
 *
 */
public class ArmTaxRateOracleBean extends BaseOracleBean {

  public ArmTaxRateOracleBean() {}

  public static String selectSql = "select ZIP_CODE, CITY, COUNTY, STATE, TAX_RATE, EFFECTIVE_DT, TAX_JUR, NATION, TAX_TYPE, EXPIRATION_DT from ARM_TAX_RATE ";
  public static String insertSql = "insert into ARM_TAX_RATE (ZIP_CODE, CITY, COUNTY, STATE, TAX_RATE, EFFECTIVE_DT, TAX_JUR) values (?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_TAX_RATE set ZIP_CODE = ?, CITY = ?, COUNTY = ?, STATE = ?, TAX_RATE = ?, EFFECTIVE_DT = ?, TAX_JUR = ? ";
  public static String deleteSql = "delete from ARM_TAX_RATE ";

  public static String TABLE_NAME = "ARM_TAX_RATE";
  public static String COL_ZIP_CODE = "ARM_TAX_RATE.ZIP_CODE";
  public static String COL_CITY = "ARM_TAX_RATE.CITY";
  public static String COL_COUNTY = "ARM_TAX_RATE.COUNTY";
  public static String COL_STATE = "ARM_TAX_RATE.STATE";
  public static String COL_TAX_RATE = "ARM_TAX_RATE.TAX_RATE";
  public static String COL_EFFECTIVE_DT = "ARM_TAX_RATE.EFFECTIVE_DT";
  public static String COL_TAX_JUR = "ARM_TAX_RATE.TAX_JUR";
  public static String COL_NATION = "ARM_TAX_RATE.NATION";
  public static String COL_TAX_TYPE = "ARM_TAX_RATE.TAX_TYPE";
  
  //Field added for expiration date issue on Nov 15,2016
  public static String COL_EXPIRATION_DT = "ARM_TAX_RATE.EXPIRATION_DT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String zipCode;
  private String city;
  private String county;
  private String state;
  private Double taxRate;
  private Date effectiveDt;
  private String taxJur;
  private String nation;
  private String tax_type;
 //Field added for expiration date issue on Nov 15,2016
  private Date expiration_dt;

  public String getZipCode() { return this.zipCode; }
  public void setZipCode(String zipCode) { this.zipCode = zipCode; }

  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }

  public String getCounty() { return this.county; }
  public void setCounty(String county) { this.county = county; }

  public String getState() { return this.state; }
  public void setState(String state) { this.state = state; }

  public Double getTaxRate() { return this.taxRate; }
  public void setTaxRate(Double taxRate) { this.taxRate = taxRate; }
  public void setTaxRate(double taxRate) { this.taxRate = new Double(taxRate); }

  public Date getEffectiveDt() { return this.effectiveDt; }
  public void setEffectiveDt(Date effectiveDt) { this.effectiveDt = effectiveDt; }

  public String getTaxJur() { return this.taxJur; }
  public void setTaxJur(String taxJur) { this.taxJur = taxJur; }
  
  public String getNation() {return nation;}
  public void setNation(String nation) { this.nation = nation; }

  public String getTax_type() { return tax_type; }
  public void setTax_type(String tax_type) { this.tax_type = tax_type; }
  
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmTaxRateOracleBean bean = new ArmTaxRateOracleBean();
      bean.zipCode = getStringFromResultSet(rs, "ZIP_CODE");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.county = getStringFromResultSet(rs, "COUNTY");
      bean.state = getStringFromResultSet(rs, "STATE");
      bean.taxRate = getDoubleFromResultSet(rs, "TAX_RATE");
      bean.effectiveDt = getDateFromResultSet(rs, "EFFECTIVE_DT");
      bean.taxJur = getStringFromResultSet(rs, "TAX_JUR");
      bean.nation = getStringFromResultSet(rs, "NATION");
      bean.tax_type = getStringFromResultSet(rs, "TAX_TYPE");
      
      //Added for expiration date issue on Nov 15,2016
      bean.expiration_dt=getDateFromResultSet(rs, "EXPIRATION_DT");
      //ends here
      list.add(bean);
    }
    return (ArmTaxRateOracleBean[]) list.toArray(new ArmTaxRateOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getZipCode(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getCounty(), Types.VARCHAR);
    addToList(list, this.getState(), Types.VARCHAR);
    addToList(list, this.getTaxRate(), Types.DECIMAL);
    addToList(list, this.getEffectiveDt(), Types.TIMESTAMP);
    addToList(list, this.getTaxJur(), Types.VARCHAR);
    addToList(list, this.getNation(), Types.VARCHAR);
    addToList(list, this.getTax_type(), Types.VARCHAR);
    
    //Added for expiration date issue on Nov 15,2016
    addToList(list, this.getExpiration_dt(), Types.TIMESTAMP);
    //ends here
    return list;
  }
  
	/**Added for expiration date issue on Nov 15,2016
	 * @return the expiration_dt
	 */
	public Date getExpiration_dt() {
		return this.expiration_dt;
	}
	
	/**Added for expiration date issue on Nov 15,2016
	 * @param expiration_dt the expiration_dt to set
	 */
	public void setExpiration_dt(Date expiration_dt) {
		this.expiration_dt = expiration_dt;
	}
	//ends here

}
