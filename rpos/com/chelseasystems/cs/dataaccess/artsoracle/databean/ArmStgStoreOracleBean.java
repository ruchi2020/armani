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
 * This class is an object representation of the Arts database table ARM_STG_STORE<BR>
 * Followings are the column of the table: <BR>
 *     TRAN_TYPE -- NUMBER(2)<BR>
 *     COMPANY_CODE -- VARCHAR2(30)<BR>
 *     SHOP_CODE -- VARCHAR2(30)<BR>
 *     OFFICE_PHONE_AREA -- VARCHAR2(4)<BR>
 *     OFFICE_PHONE_NUMBER -- VARCHAR2(10)<BR>
 *     ADDRESS_LINE1 -- VARCHAR2(50)<BR>
 *     ADDRESS_LINE2 -- VARCHAR2(50)<BR>
 *     CITY -- VARCHAR2(30)<BR>
 *     STATE -- VARCHAR2(30)<BR>
 *     POSTAL_CODE -- VARCHAR2(30)<BR>
 *     COUNTRY -- VARCHAR2(20)<BR>
 *     CURRENCY -- VARCHAR2(10)<BR>
 *     TAX_RATE -- NUMBER(4.2)<BR>
 *     COUNTRY_CODE -- VARCHAR2(5)<BR>
 *     LANGUAGE_CODE -- VARCHAR2(5)<BR>
 *     BRAND_ID -- VARCHAR2(30)<BR>
 *     FRANKING_COMPANY_NAME -- VARCHAR2(30)<BR>
 *     FRANKING_COMPANY_AC_NUM -- VARCHAR2(30)<BR>
 *     STG_EVENT_ID -- NUMBER(22)<BR>
 *     STG_STATUS -- NUMBER(22)<BR>
 *     STG_ERROR_MESSAGE -- VARCHAR2(255)<BR>
 *     STG_LOAD_DATE -- DATE(7)<BR>
 *     STG_PROCESS_DATE -- DATE(7)<BR>
 *     FISCAL_CODE -- VARCHAR2(30)<BR>
 *     SHOP_DESC -- VARCHAR2(50)<BR>
 *     COMPANY_DESC -- VARCHAR2(50)<BR>
 *     COMP_OFFICE_PHONE_AREA -- VARCHAR2(4)<BR>
 *     COMP_OFFICE_PHONE_NUMBER -- VARCHAR2(10)<BR>
 *     COMP_ADDRESS_LINE1 -- VARCHAR2(50)<BR>
 *     COMP_ADDRESS_LINE2 -- VARCHAR2(50)<BR>
 *     COMP_CITY -- VARCHAR2(30)<BR>
 *     COMP_STATE -- VARCHAR2(30)<BR>
 *     COMP_POSTAL_CODE -- VARCHAR2(30)<BR>
 *     COMP_COUNTRY -- VARCHAR2(20)<BR>
 *
 */
public class ArmStgStoreOracleBean extends BaseOracleBean {

  public ArmStgStoreOracleBean() {}

  public static String selectSql = "select TRAN_TYPE, COMPANY_CODE, SHOP_CODE, OFFICE_PHONE_AREA, OFFICE_PHONE_NUMBER, ADDRESS_LINE1, ADDRESS_LINE2, CITY, STATE, POSTAL_CODE, COUNTRY, CURRENCY, TAX_RATE, COUNTRY_CODE, LANGUAGE_CODE, BRAND_ID, FRANKING_COMPANY_NAME, FRANKING_COMPANY_AC_NUM, STG_EVENT_ID, STG_STATUS, STG_ERROR_MESSAGE, STG_LOAD_DATE, STG_PROCESS_DATE, FISCAL_CODE, SHOP_DESC, COMPANY_DESC, COMP_OFFICE_PHONE_AREA, COMP_OFFICE_PHONE_NUMBER, COMP_ADDRESS_LINE1, COMP_ADDRESS_LINE2, COMP_CITY, COMP_STATE, COMP_POSTAL_CODE, COMP_COUNTRY from ARM_STG_STORE ";
  public static String insertSql = "insert into ARM_STG_STORE (TRAN_TYPE, COMPANY_CODE, SHOP_CODE, OFFICE_PHONE_AREA, OFFICE_PHONE_NUMBER, ADDRESS_LINE1, ADDRESS_LINE2, CITY, STATE, POSTAL_CODE, COUNTRY, CURRENCY, TAX_RATE, COUNTRY_CODE, LANGUAGE_CODE, BRAND_ID, FRANKING_COMPANY_NAME, FRANKING_COMPANY_AC_NUM, STG_EVENT_ID, STG_STATUS, STG_ERROR_MESSAGE, STG_LOAD_DATE, STG_PROCESS_DATE, FISCAL_CODE, SHOP_DESC, COMPANY_DESC, COMP_OFFICE_PHONE_AREA, COMP_OFFICE_PHONE_NUMBER, COMP_ADDRESS_LINE1, COMP_ADDRESS_LINE2, COMP_CITY, COMP_STATE, COMP_POSTAL_CODE, COMP_COUNTRY) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  //public static String updateSql = "update ARM_STG_STORE set TRAN_TYPE = ?, COMPANY_CODE = ?, SHOP_CODE = ?, OFFICE_PHONE_AREA = ?, OFFICE_PHONE_NUMBER = ?, ADDRESS_LINE1 = ?, ADDRESS_LINE2 = ?, CITY = ?, STATE = ?, POSTAL_CODE = ?, COUNTRY = ?, CURRENCY = ?, TAX_RATE = ?, COUNTRY_CODE = ?, LANGUAGE_CODE = ?, BRAND_ID = ?, FRANKING_COMPANY_NAME = ?, FRANKING_COMPANY_AC_NUM = ?, STG_EVENT_ID = ?, STG_STATUS = ?, STG_ERROR_MESSAGE = ?, STG_LOAD_DATE = ?, STG_PROCESS_DATE = ?, FISCAL_CODE = ?, SHOP_DESC = ?, COMPANY_DESC = ?, COMP_OFFICE_PHONE_AREA = ?, COMP_OFFICE_PHONE_NUMBER = ?, COMP_ADDRESS_LINE1 = ?, COMP_ADDRESS_LINE2 = ?, COMP_CITY = ?, COMP_STATE = ?, COMP_POSTAL_CODE = ?, COMP_COUNTRY = ? ";
  public static String updateSql ="update ARM_STG_STORE set STG_PROCESS_DATE = SYSDATE, STG_STATUS = ?, STG_ERROR_MESSAGE = ? where STG_EVENT_ID = ?";

  public static String deleteSql = "delete from ARM_STG_STORE ";

  public static String TABLE_NAME = "ARM_STG_STORE";
  public static String COL_TRAN_TYPE = "ARM_STG_STORE.TRAN_TYPE";
  public static String COL_COMPANY_CODE = "ARM_STG_STORE.COMPANY_CODE";
  public static String COL_SHOP_CODE = "ARM_STG_STORE.SHOP_CODE";
  public static String COL_OFFICE_PHONE_AREA = "ARM_STG_STORE.OFFICE_PHONE_AREA";
  public static String COL_OFFICE_PHONE_NUMBER = "ARM_STG_STORE.OFFICE_PHONE_NUMBER";
  public static String COL_ADDRESS_LINE1 = "ARM_STG_STORE.ADDRESS_LINE1";
  public static String COL_ADDRESS_LINE2 = "ARM_STG_STORE.ADDRESS_LINE2";
  public static String COL_CITY = "ARM_STG_STORE.CITY";
  public static String COL_STATE = "ARM_STG_STORE.STATE";
  public static String COL_POSTAL_CODE = "ARM_STG_STORE.POSTAL_CODE";
  public static String COL_COUNTRY = "ARM_STG_STORE.COUNTRY";
  public static String COL_CURRENCY = "ARM_STG_STORE.CURRENCY";
  public static String COL_TAX_RATE = "ARM_STG_STORE.TAX_RATE";
  public static String COL_COUNTRY_CODE = "ARM_STG_STORE.COUNTRY_CODE";
  public static String COL_LANGUAGE_CODE = "ARM_STG_STORE.LANGUAGE_CODE";
  public static String COL_BRAND_ID = "ARM_STG_STORE.BRAND_ID";
  public static String COL_FRANKING_COMPANY_NAME = "ARM_STG_STORE.FRANKING_COMPANY_NAME";
  public static String COL_FRANKING_COMPANY_AC_NUM = "ARM_STG_STORE.FRANKING_COMPANY_AC_NUM";
  public static String COL_STG_EVENT_ID = "ARM_STG_STORE.STG_EVENT_ID";
  public static String COL_STG_STATUS = "ARM_STG_STORE.STG_STATUS";
  public static String COL_STG_ERROR_MESSAGE = "ARM_STG_STORE.STG_ERROR_MESSAGE";
  public static String COL_STG_LOAD_DATE = "ARM_STG_STORE.STG_LOAD_DATE";
  public static String COL_STG_PROCESS_DATE = "ARM_STG_STORE.STG_PROCESS_DATE";
  public static String COL_FISCAL_CODE = "ARM_STG_STORE.FISCAL_CODE";
  public static String COL_SHOP_DESC = "ARM_STG_STORE.SHOP_DESC";
  public static String COL_COMPANY_DESC = "ARM_STG_STORE.COMPANY_DESC";
  public static String COL_COMP_OFFICE_PHONE_AREA = "ARM_STG_STORE.COMP_OFFICE_PHONE_AREA";
  public static String COL_COMP_OFFICE_PHONE_NUMBER = "ARM_STG_STORE.COMP_OFFICE_PHONE_NUMBER";
  public static String COL_COMP_ADDRESS_LINE1 = "ARM_STG_STORE.COMP_ADDRESS_LINE1";
  public static String COL_COMP_ADDRESS_LINE2 = "ARM_STG_STORE.COMP_ADDRESS_LINE2";
  public static String COL_COMP_CITY = "ARM_STG_STORE.COMP_CITY";
  public static String COL_COMP_STATE = "ARM_STG_STORE.COMP_STATE";
  public static String COL_COMP_POSTAL_CODE = "ARM_STG_STORE.COMP_POSTAL_CODE";
  public static String COL_COMP_COUNTRY = "ARM_STG_STORE.COMP_COUNTRY";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Long tranType;
  private String companyCode;
  private String shopCode;
  private String officePhoneArea;
  private String officePhoneNumber;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String postalCode;
  private String country;
  private String currency;
  private Double taxRate;
  private String countryCode;
  private String languageCode;
  private String brandId;
  private String frankingCompanyName;
  private String frankingCompanyAcNum;
  private Long stgEventId;
  private Long stgStatus;
  private String stgErrorMessage;
  private Date stgLoadDate;
  private Date stgProcessDate;
  private String fiscalCode;
  private String shopDesc;
  private String companyDesc;
  private String compOfficePhoneArea;
  private String compOfficePhoneNumber;
  private String compAddressLine1;
  private String compAddressLine2;
  private String compCity;
  private String compState;
  private String compPostalCode;
  private String compCountry;

  public Long getTranType() { return this.tranType; }
  public void setTranType(Long tranType) { this.tranType = tranType; }
  public void setTranType(long tranType) { this.tranType = new Long(tranType); }
  public void setTranType(int tranType) { this.tranType = new Long((long)tranType); }

  public String getCompanyCode() { return this.companyCode; }
  public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

  public String getShopCode() { return this.shopCode; }
  public void setShopCode(String shopCode) { this.shopCode = shopCode; }

  public String getOfficePhoneArea() { return this.officePhoneArea; }
  public void setOfficePhoneArea(String officePhoneArea) { this.officePhoneArea = officePhoneArea; }

  public String getOfficePhoneNumber() { return this.officePhoneNumber; }
  public void setOfficePhoneNumber(String officePhoneNumber) { this.officePhoneNumber = officePhoneNumber; }

  public String getAddressLine1() { return this.addressLine1; }
  public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

  public String getAddressLine2() { return this.addressLine2; }
  public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }

  public String getState() { return this.state; }
  public void setState(String state) { this.state = state; }

  public String getPostalCode() { return this.postalCode; }
  public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

  public String getCountry() { return this.country; }
  public void setCountry(String country) { this.country = country; }

  public String getCurrency() { return this.currency; }
  public void setCurrency(String currency) { this.currency = currency; }

  public Double getTaxRate() { return this.taxRate; }
  public void setTaxRate(Double taxRate) { this.taxRate = taxRate; }
  public void setTaxRate(double taxRate) { this.taxRate = new Double(taxRate); }

  public String getCountryCode() { return this.countryCode; }
  public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

  public String getLanguageCode() { return this.languageCode; }
  public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }

  public String getBrandId() { return this.brandId; }
  public void setBrandId(String brandId) { this.brandId = brandId; }

  public String getFrankingCompanyName() { return this.frankingCompanyName; }
  public void setFrankingCompanyName(String frankingCompanyName) { this.frankingCompanyName = frankingCompanyName; }

  public String getFrankingCompanyAcNum() { return this.frankingCompanyAcNum; }
  public void setFrankingCompanyAcNum(String frankingCompanyAcNum) { this.frankingCompanyAcNum = frankingCompanyAcNum; }

  public Long getStgEventId() { return this.stgEventId; }
  public void setStgEventId(Long stgEventId) { this.stgEventId = stgEventId; }
  public void setStgEventId(long stgEventId) { this.stgEventId = new Long(stgEventId); }
  public void setStgEventId(int stgEventId) { this.stgEventId = new Long((long)stgEventId); }

  public Long getStgStatus() { return this.stgStatus; }
  public void setStgStatus(Long stgStatus) { this.stgStatus = stgStatus; }
  public void setStgStatus(long stgStatus) { this.stgStatus = new Long(stgStatus); }
  public void setStgStatus(int stgStatus) { this.stgStatus = new Long((long)stgStatus); }

  public String getStgErrorMessage() { return this.stgErrorMessage; }
  public void setStgErrorMessage(String stgErrorMessage) { this.stgErrorMessage = stgErrorMessage; }

  public Date getStgLoadDate() { return this.stgLoadDate; }
  public void setStgLoadDate(Date stgLoadDate) { this.stgLoadDate = stgLoadDate; }

  public Date getStgProcessDate() { return this.stgProcessDate; }
  public void setStgProcessDate(Date stgProcessDate) { this.stgProcessDate = stgProcessDate; }

  public String getFiscalCode() { return this.fiscalCode; }
  public void setFiscalCode(String fiscalCode) { this.fiscalCode = fiscalCode; }

  public String getShopDesc() { return this.shopDesc; }
  public void setShopDesc(String shopDesc) { this.shopDesc = shopDesc; }

  public String getCompanyDesc() { return this.companyDesc; }
  public void setCompanyDesc(String companyDesc) { this.companyDesc = companyDesc; }

  public String getCompOfficePhoneArea() { return this.compOfficePhoneArea; }
  public void setCompOfficePhoneArea(String compOfficePhoneArea) { this.compOfficePhoneArea = compOfficePhoneArea; }

  public String getCompOfficePhoneNumber() { return this.compOfficePhoneNumber; }
  public void setCompOfficePhoneNumber(String compOfficePhoneNumber) { this.compOfficePhoneNumber = compOfficePhoneNumber; }

  public String getCompAddressLine1() { return this.compAddressLine1; }
  public void setCompAddressLine1(String compAddressLine1) { this.compAddressLine1 = compAddressLine1; }

  public String getCompAddressLine2() { return this.compAddressLine2; }
  public void setCompAddressLine2(String compAddressLine2) { this.compAddressLine2 = compAddressLine2; }

  public String getCompCity() { return this.compCity; }
  public void setCompCity(String compCity) { this.compCity = compCity; }

  public String getCompState() { return this.compState; }
  public void setCompState(String compState) { this.compState = compState; }

  public String getCompPostalCode() { return this.compPostalCode; }
  public void setCompPostalCode(String compPostalCode) { this.compPostalCode = compPostalCode; }

  public String getCompCountry() { return this.compCountry; }
  public void setCompCountry(String compCountry) { this.compCountry = compCountry; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmStgStoreOracleBean bean = new ArmStgStoreOracleBean();
      bean.tranType = getLongFromResultSet(rs, "TRAN_TYPE");
      bean.companyCode = getStringFromResultSet(rs, "COMPANY_CODE");
      bean.shopCode = getStringFromResultSet(rs, "SHOP_CODE");
      bean.officePhoneArea = getStringFromResultSet(rs, "OFFICE_PHONE_AREA");
      bean.officePhoneNumber = getStringFromResultSet(rs, "OFFICE_PHONE_NUMBER");
      bean.addressLine1 = getStringFromResultSet(rs, "ADDRESS_LINE1");
      bean.addressLine2 = getStringFromResultSet(rs, "ADDRESS_LINE2");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.state = getStringFromResultSet(rs, "STATE");
      bean.postalCode = getStringFromResultSet(rs, "POSTAL_CODE");
      bean.country = getStringFromResultSet(rs, "COUNTRY");
      bean.currency = getStringFromResultSet(rs, "CURRENCY");
      bean.taxRate = getDoubleFromResultSet(rs, "TAX_RATE");
      bean.countryCode = getStringFromResultSet(rs, "COUNTRY_CODE");
      bean.languageCode = getStringFromResultSet(rs, "LANGUAGE_CODE");
      bean.brandId = getStringFromResultSet(rs, "BRAND_ID");
      bean.frankingCompanyName = getStringFromResultSet(rs, "FRANKING_COMPANY_NAME");
      bean.frankingCompanyAcNum = getStringFromResultSet(rs, "FRANKING_COMPANY_AC_NUM");
      bean.stgEventId = getLongFromResultSet(rs, "STG_EVENT_ID");
      bean.stgStatus = getLongFromResultSet(rs, "STG_STATUS");
      bean.stgErrorMessage = getStringFromResultSet(rs, "STG_ERROR_MESSAGE");
      bean.stgLoadDate = getDateFromResultSet(rs, "STG_LOAD_DATE");
      bean.stgProcessDate = getDateFromResultSet(rs, "STG_PROCESS_DATE");
      bean.fiscalCode = getStringFromResultSet(rs, "FISCAL_CODE");
      bean.shopDesc = getStringFromResultSet(rs, "SHOP_DESC");
      bean.companyDesc = getStringFromResultSet(rs, "COMPANY_DESC");
      bean.compOfficePhoneArea = getStringFromResultSet(rs, "COMP_OFFICE_PHONE_AREA");
      bean.compOfficePhoneNumber = getStringFromResultSet(rs, "COMP_OFFICE_PHONE_NUMBER");
      bean.compAddressLine1 = getStringFromResultSet(rs, "COMP_ADDRESS_LINE1");
      bean.compAddressLine2 = getStringFromResultSet(rs, "COMP_ADDRESS_LINE2");
      bean.compCity = getStringFromResultSet(rs, "COMP_CITY");
      bean.compState = getStringFromResultSet(rs, "COMP_STATE");
      bean.compPostalCode = getStringFromResultSet(rs, "COMP_POSTAL_CODE");
      bean.compCountry = getStringFromResultSet(rs, "COMP_COUNTRY");
      list.add(bean);
    }
    return (ArmStgStoreOracleBean[]) list.toArray(new ArmStgStoreOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTranType(), Types.DECIMAL);
    addToList(list, this.getCompanyCode(), Types.VARCHAR);
    addToList(list, this.getShopCode(), Types.VARCHAR);
    addToList(list, this.getOfficePhoneArea(), Types.VARCHAR);
    addToList(list, this.getOfficePhoneNumber(), Types.VARCHAR);
    addToList(list, this.getAddressLine1(), Types.VARCHAR);
    addToList(list, this.getAddressLine2(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getState(), Types.VARCHAR);
    addToList(list, this.getPostalCode(), Types.VARCHAR);
    addToList(list, this.getCountry(), Types.VARCHAR);
    addToList(list, this.getCurrency(), Types.VARCHAR);
    addToList(list, this.getTaxRate(), Types.DECIMAL);
    addToList(list, this.getCountryCode(), Types.VARCHAR);
    addToList(list, this.getLanguageCode(), Types.VARCHAR);
    addToList(list, this.getBrandId(), Types.VARCHAR);
    addToList(list, this.getFrankingCompanyName(), Types.VARCHAR);
    addToList(list, this.getFrankingCompanyAcNum(), Types.VARCHAR);
    addToList(list, this.getStgEventId(), Types.DECIMAL);
    addToList(list, this.getStgStatus(), Types.DECIMAL);
    addToList(list, this.getStgErrorMessage(), Types.VARCHAR);
    addToList(list, this.getStgLoadDate(), Types.TIMESTAMP);
    addToList(list, this.getStgProcessDate(), Types.TIMESTAMP);
    addToList(list, this.getFiscalCode(), Types.VARCHAR);
    addToList(list, this.getShopDesc(), Types.VARCHAR);
    addToList(list, this.getCompanyDesc(), Types.VARCHAR);
    addToList(list, this.getCompOfficePhoneArea(), Types.VARCHAR);
    addToList(list, this.getCompOfficePhoneNumber(), Types.VARCHAR);
    addToList(list, this.getCompAddressLine1(), Types.VARCHAR);
    addToList(list, this.getCompAddressLine2(), Types.VARCHAR);
    addToList(list, this.getCompCity(), Types.VARCHAR);
    addToList(list, this.getCompState(), Types.VARCHAR);
    addToList(list, this.getCompPostalCode(), Types.VARCHAR);
    addToList(list, this.getCompCountry(), Types.VARCHAR);
    return list;
  }

}
