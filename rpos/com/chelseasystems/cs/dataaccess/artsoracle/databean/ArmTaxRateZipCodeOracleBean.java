package com.chelseasystems.cs.dataaccess.artsoracle.databean;

//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


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
 *     STATE -- VARCHAR2(10)<BR>
 *
 */
public class ArmTaxRateZipCodeOracleBean extends BaseOracleBean {

  public ArmTaxRateZipCodeOracleBean() {}

  public static String selectSql = "select ZIP_CODE, CITY, STATE from ARM_TAX_RATE ";
  public static String insertSql = "insert into ARM_TAX_RATE (ZIP_CODE, CITY, STATE) values (?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_TAX_RATE set ZIP_CODE = ?, CITY = ?, STATE = ? ";
  public static String deleteSql = "delete from ARM_TAX_RATE ";

  public static String TABLE_NAME = "ARM_TAX_RATE";
  public static String COL_ZIP_CODE = "ARM_TAX_RATE.ZIP_CODE";
  public static String COL_CITY = "ARM_TAX_RATE.CITY";
  public static String COL_COUNTY = "ARM_TAX_RATE.COUNTY";
  public static String COL_STATE = "ARM_TAX_RATE.STATE";
  public static String COL_TAX_RATE = "ARM_TAX_RATE.TAX_RATE";
  public static String COL_EFFECTIVE_DT = "ARM_TAX_RATE.EFFECTIVE_DT";
  public static String COL_TAX_JUR = "ARM_TAX_RATE.TAX_JUR";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String zipCode;
  private String city;
  private String state;


  public String getZipCode() { return this.zipCode; }
  public void setZipCode(String zipCode) { this.zipCode = zipCode; }

  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }

  public String getState() { return this.state; }
  public void setState(String state) { this.state = state; }


  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while (rs.next()) {
      ArmTaxRateZipCodeOracleBean bean = new ArmTaxRateZipCodeOracleBean();
      bean.zipCode = getStringFromResultSet(rs, "ZIP_CODE");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.state = getStringFromResultSet(rs, "STATE");
      list.add(bean);
    }
    return (ArmTaxRateZipCodeOracleBean[]) list.toArray(new
        ArmTaxRateZipCodeOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getZipCode(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getState(), Types.VARCHAR);

    return list;
  }

}
