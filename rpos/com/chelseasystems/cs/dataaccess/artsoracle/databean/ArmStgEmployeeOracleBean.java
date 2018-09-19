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
 * This class is an object representation of the Arts database table ARM_STG_EMPLOYEE<BR>
 * Followings are the column of the table: <BR>
 *     TRAN_TYPE -- NUMBER(2)<BR>
 *     ID -- VARCHAR2(30)<BR>
 *     FIRST_NAME -- VARCHAR2(30)<BR>
 *     LAST_NAME -- VARCHAR2(30)<BR>
 *     DATE_OF_BIRTH -- DATE(7)<BR>
 *     EMAIL -- VARCHAR2(30)<BR>
 *     HOME_PHONE_AREA -- VARCHAR2(4)<BR>
 *     HOME_PHONE_NUMBER -- VARCHAR2(10)<BR>
 *     ADDRESS_LINE1 -- VARCHAR2(50)<BR>
 *     ADDRESS_LINE2 -- VARCHAR2(50)<BR>
 *     CITY -- VARCHAR2(30)<BR>
 *     STATE -- VARCHAR2(30)<BR>
 *     POSTAL_CODE -- VARCHAR2(30)<BR>
 *     COUNTRY -- VARCHAR2(20)<BR>
 *     STATUS -- NUMBER(22)<BR>
 *     HOME_STORE_ID -- VARCHAR2(30)<BR>
 *     HIRE_DATE -- DATE(7)<BR>
 *     TERM_DATE -- DATE(7)<BR>
 *     ROLE -- CHAR(1)<BR>
 *     STG_EVENT_ID -- NUMBER(22)<BR>
 *     STG_STATUS -- NUMBER(22)<BR>
 *     STG_ERROR_MESSAGE -- VARCHAR2(255)<BR>
 *     STG_LOAD_DATE -- DATE(7)<BR>
 *     STG_PROCESS_DATE -- DATE(7)<BR>
 *
 */
public class ArmStgEmployeeOracleBean extends BaseOracleBean {

  public ArmStgEmployeeOracleBean() {}

  public static String selectSql = "select TRAN_TYPE, ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, EMAIL, HOME_PHONE_AREA, HOME_PHONE_NUMBER, ADDRESS_LINE1, ADDRESS_LINE2, CITY, STATE, POSTAL_CODE, COUNTRY, STATUS, HOME_STORE_ID, HIRE_DATE, TERM_DATE, ROLE, STG_EVENT_ID, STG_STATUS, STG_ERROR_MESSAGE, STG_LOAD_DATE, STG_PROCESS_DATE from ARM_STG_EMPLOYEE ";
  public static String insertSql = "insert into ARM_STG_EMPLOYEE (TRAN_TYPE, ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, EMAIL, HOME_PHONE_AREA, HOME_PHONE_NUMBER, ADDRESS_LINE1, ADDRESS_LINE2, CITY, STATE, POSTAL_CODE, COUNTRY, STATUS, HOME_STORE_ID, HIRE_DATE, TERM_DATE, ROLE, STG_EVENT_ID, STG_STATUS, STG_ERROR_MESSAGE, STG_LOAD_DATE, STG_PROCESS_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql ="update ARM_STG_EMPLOYEE set STG_PROCESS_DATE = SYSDATE, STG_STATUS = ?, STG_ERROR_MESSAGE = ? where STG_EVENT_ID = ?";
  public static String deleteSql = "delete from ARM_STG_EMPLOYEE ";

  public static String TABLE_NAME = "ARM_STG_EMPLOYEE";
  public static String COL_TRAN_TYPE = "ARM_STG_EMPLOYEE.TRAN_TYPE";
  public static String COL_ID = "ARM_STG_EMPLOYEE.ID";
  public static String COL_FIRST_NAME = "ARM_STG_EMPLOYEE.FIRST_NAME";
  public static String COL_LAST_NAME = "ARM_STG_EMPLOYEE.LAST_NAME";
  public static String COL_DATE_OF_BIRTH = "ARM_STG_EMPLOYEE.DATE_OF_BIRTH";
  public static String COL_EMAIL = "ARM_STG_EMPLOYEE.EMAIL";
  public static String COL_HOME_PHONE_AREA = "ARM_STG_EMPLOYEE.HOME_PHONE_AREA";
  public static String COL_HOME_PHONE_NUMBER = "ARM_STG_EMPLOYEE.HOME_PHONE_NUMBER";
  public static String COL_ADDRESS_LINE1 = "ARM_STG_EMPLOYEE.ADDRESS_LINE1";
  public static String COL_ADDRESS_LINE2 = "ARM_STG_EMPLOYEE.ADDRESS_LINE2";
  public static String COL_CITY = "ARM_STG_EMPLOYEE.CITY";
  public static String COL_STATE = "ARM_STG_EMPLOYEE.STATE";
  public static String COL_POSTAL_CODE = "ARM_STG_EMPLOYEE.POSTAL_CODE";
  public static String COL_COUNTRY = "ARM_STG_EMPLOYEE.COUNTRY";
  public static String COL_STATUS = "ARM_STG_EMPLOYEE.STATUS";
  public static String COL_HOME_STORE_ID = "ARM_STG_EMPLOYEE.HOME_STORE_ID";
  public static String COL_HIRE_DATE = "ARM_STG_EMPLOYEE.HIRE_DATE";
  public static String COL_TERM_DATE = "ARM_STG_EMPLOYEE.TERM_DATE";
  public static String COL_ROLE = "ARM_STG_EMPLOYEE.ROLE";
  public static String COL_STG_EVENT_ID = "ARM_STG_EMPLOYEE.STG_EVENT_ID";
  public static String COL_STG_STATUS = "ARM_STG_EMPLOYEE.STG_STATUS";
  public static String COL_STG_ERROR_MESSAGE = "ARM_STG_EMPLOYEE.STG_ERROR_MESSAGE";
  public static String COL_STG_LOAD_DATE = "ARM_STG_EMPLOYEE.STG_LOAD_DATE";
  public static String COL_STG_PROCESS_DATE = "ARM_STG_EMPLOYEE.STG_PROCESS_DATE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Long tranType;
  private String id;
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private String email;
  private String homePhoneArea;
  private String homePhoneNumber;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String postalCode;
  private String country;
  private Long status;
  private String homeStoreId;
  private Date hireDate;
  private Date termDate;
  private String role;
  private Long stgEventId;
  private Long stgStatus;
  private String stgErrorMessage;
  private Date stgLoadDate;
  private Date stgProcessDate;

  public Long getTranType() { return this.tranType; }
  public void setTranType(Long tranType) { this.tranType = tranType; }
  public void setTranType(long tranType) { this.tranType = new Long(tranType); }
  public void setTranType(int tranType) { this.tranType = new Long((long)tranType); }

  public String getId() { return this.id; }
  public void setId(String id) { this.id = id; }

  public String getFirstName() { return this.firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return this.lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public Date getDateOfBirth() { return this.dateOfBirth; }
  public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

  public String getEmail() { return this.email; }
  public void setEmail(String email) { this.email = email; }

  public String getHomePhoneArea() { return this.homePhoneArea; }
  public void setHomePhoneArea(String homePhoneArea) { this.homePhoneArea = homePhoneArea; }

  public String getHomePhoneNumber() { return this.homePhoneNumber; }
  public void setHomePhoneNumber(String homePhoneNumber) { this.homePhoneNumber = homePhoneNumber; }

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

  public Long getStatus() { return this.status; }
  public void setStatus(Long status) { this.status = status; }
  public void setStatus(long status) { this.status = new Long(status); }
  public void setStatus(int status) { this.status = new Long((long)status); }

  public String getHomeStoreId() { return this.homeStoreId; }
  public void setHomeStoreId(String homeStoreId) { this.homeStoreId = homeStoreId; }

  public Date getHireDate() { return this.hireDate; }
  public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

  public Date getTermDate() { return this.termDate; }
  public void setTermDate(Date termDate) { this.termDate = termDate; }

  public String getRole() { return this.role; }
  public void setRole(String role) { this.role = role; }

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

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmStgEmployeeOracleBean bean = new ArmStgEmployeeOracleBean();
      bean.tranType = getLongFromResultSet(rs, "TRAN_TYPE");
      bean.id = getStringFromResultSet(rs, "ID");
      bean.firstName = getStringFromResultSet(rs, "FIRST_NAME");
      bean.lastName = getStringFromResultSet(rs, "LAST_NAME");
      bean.dateOfBirth = getDateFromResultSet(rs, "DATE_OF_BIRTH");
      bean.email = getStringFromResultSet(rs, "EMAIL");
      bean.homePhoneArea = getStringFromResultSet(rs, "HOME_PHONE_AREA");
      bean.homePhoneNumber = getStringFromResultSet(rs, "HOME_PHONE_NUMBER");
      bean.addressLine1 = getStringFromResultSet(rs, "ADDRESS_LINE1");
      bean.addressLine2 = getStringFromResultSet(rs, "ADDRESS_LINE2");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.state = getStringFromResultSet(rs, "STATE");
      bean.postalCode = getStringFromResultSet(rs, "POSTAL_CODE");
      bean.country = getStringFromResultSet(rs, "COUNTRY");
      bean.status = getLongFromResultSet(rs, "STATUS");
      bean.homeStoreId = getStringFromResultSet(rs, "HOME_STORE_ID");
      bean.hireDate = getDateFromResultSet(rs, "HIRE_DATE");
      bean.termDate = getDateFromResultSet(rs, "TERM_DATE");
      bean.role = getStringFromResultSet(rs, "ROLE");
      bean.stgEventId = getLongFromResultSet(rs, "STG_EVENT_ID");
      bean.stgStatus = getLongFromResultSet(rs, "STG_STATUS");
      bean.stgErrorMessage = getStringFromResultSet(rs, "STG_ERROR_MESSAGE");
      bean.stgLoadDate = getDateFromResultSet(rs, "STG_LOAD_DATE");
      bean.stgProcessDate = getDateFromResultSet(rs, "STG_PROCESS_DATE");
      list.add(bean);
    }
    return (ArmStgEmployeeOracleBean[]) list.toArray(new ArmStgEmployeeOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTranType(), Types.DECIMAL);
    addToList(list, this.getId(), Types.VARCHAR);
    addToList(list, this.getFirstName(), Types.VARCHAR);
    addToList(list, this.getLastName(), Types.VARCHAR);
    addToList(list, this.getDateOfBirth(), Types.TIMESTAMP);
    addToList(list, this.getEmail(), Types.VARCHAR);
    addToList(list, this.getHomePhoneArea(), Types.VARCHAR);
    addToList(list, this.getHomePhoneNumber(), Types.VARCHAR);
    addToList(list, this.getAddressLine1(), Types.VARCHAR);
    addToList(list, this.getAddressLine2(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getState(), Types.VARCHAR);
    addToList(list, this.getPostalCode(), Types.VARCHAR);
    addToList(list, this.getCountry(), Types.VARCHAR);
    addToList(list, this.getStatus(), Types.DECIMAL);
    addToList(list, this.getHomeStoreId(), Types.VARCHAR);
    addToList(list, this.getHireDate(), Types.TIMESTAMP);
    addToList(list, this.getTermDate(), Types.TIMESTAMP);
    addToList(list, this.getRole(), Types.VARCHAR);
    addToList(list, this.getStgEventId(), Types.DECIMAL);
    addToList(list, this.getStgStatus(), Types.DECIMAL);
    addToList(list, this.getStgErrorMessage(), Types.VARCHAR);
    addToList(list, this.getStgLoadDate(), Types.TIMESTAMP);
    addToList(list, this.getStgProcessDate(), Types.TIMESTAMP);
    return list;
  }

}
