/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.armaniinterfaces;

import java.util.Date;
import java.io.Serializable;


/**
 * <p>Title: ArmStgEmployeeData.java </p>
 *
 * <p>Description: Stores Armani Staging Employee data</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNetInc </p>
 *
 * @author Bawa Manpreet Singh
 * @version 1.0
 * Date Created : 01/26/2005
 */
public class ArmStgEmployeeData implements Serializable {
  private Long lTranType;
  private String sEmployeeID;
  private String sFirstName;
  private String sLastName;
  private Date dtDateOfBirth;
  private String sEmail;
  private String sHomePhoneArea;
  private String sHomePhoneNumber;
  private String sAddressLine1;
  private String sAddressLine2;
  private String sCity;
  private String sState;
  private String sPostalCode;
  private String sCountry;
  private Long lEmployeeStatus;
  private String sHomeStoreId;
  private Date dtHireDate;
  private Date dtTermDate;
  private String sRole;
  private Long lStgEventId;
  private Long lStgStatus;
  private String sStgErrorMessage;
  private Date dtStgLoadDate;
  private Date dtStgProcessDate;

  /**
   * Default Constructor
   */
  public ArmStgEmployeeData() {
  }

  /**
   *
   * @return Long
   */
  public Long getTranType() {
    return this.lTranType;
  }

  /**
   *
   * @param lTranType Long
   */
  public void setTranType(Long lTranType) {
    this.lTranType = lTranType;
  }

  /**
   *
   * @param lTranType long
   */
  public void setTranType(long lTranType) {
    this.lTranType = new Long(lTranType);
  }

  /**
   *
   * @param iTranType int
   */
  public void setTranType(int iTranType) {
    this.lTranType = new Long((long)iTranType);
  }

  /**
   * Method used to get employee id
   * @return String
   */
  public String getEmployeeID() {
    return this.sEmployeeID;
  }

  /**
   * Method used to set employee id
   * @param sEmployeeID String
   */
  public void setEmployeeID(String sEmployeeID) {
    this.sEmployeeID = sEmployeeID;
  }

  /**
   * Method used to get first name of employee
   * @return String
   */
  public String getFirstName() {
    return this.sFirstName;
  }

  /**
   * Method used to set first name of employee
   * @param sFirstName String
   */
  public void setFirstName(String sFirstName) {
    this.sFirstName = sFirstName;
  }

  /**
   * Method used to get last name of employee
   * @return String
   */
  public String getLastName() {
    return this.sLastName;
  }

  /**
   * Method used to set last name of employee
   * @param sLastName String
   */
  public void setLastName(String sLastName) {
    this.sLastName = sLastName;
  }

  /**
   * Method used to get date of birth of employee
   * @return Date
   */
  public Date getDateOfBirth() {
    return this.dtDateOfBirth;
  }

  /**
   * Method used to set date of birth of employee
   * @param dtDateOfBirth Date
   */
  public void setDateOfBirth(Date dtDateOfBirth) {
    this.dtDateOfBirth = dtDateOfBirth;
  }

  /**
   * Method used to get email of employee
   * @return String
   */
  public String getEmail() {
    return this.sEmail;
  }

  /**
   * Method used to set email of employee
   * @param sEmail String
   */
  public void setEmail(String sEmail) {
    this.sEmail = sEmail;
  }

  /**
   *
   * @return String
   */
  public String getHomePhoneArea() {
    return this.sHomePhoneArea;
  }

  /**
   *
   * @param sHomePhoneArea String
   */
  public void setHomePhoneArea(String sHomePhoneArea) {
    this.sHomePhoneArea = sHomePhoneArea;
  }

  /**
   * Method used to get home phone of employee
   * @return String
   */
  public String getHomePhoneNumber() {
    return this.sHomePhoneNumber;
  }

  /**
   * Method used to set home phone of employee
   * @param sHomePhoneNumber String
   */
  public void setHomePhoneNumber(String sHomePhoneNumber) {
    this.sHomePhoneNumber = sHomePhoneNumber;
  }

  /**
   * Method used to get address of employee
   * @return String
   */
  public String getAddressLine1() {
    return this.sAddressLine1;
  }

  /**
   * Method used to set address of employee
   * @param sAddressLine1 String
   */
  public void setAddressLine1(String sAddressLine1) {
    this.sAddressLine1 = sAddressLine1;
  }

  /**
   * Method used to get address of employee
   * @return String
   */
  public String getAddressLine2() {
    return this.sAddressLine2;
  }

  /**
   * Method used to set address of employee
   * @param sAddressLine2 String
   */
  public void setAddressLine2(String sAddressLine2) {
    this.sAddressLine2 = sAddressLine2;
  }

  /**
   * Method used to get city of employee
   * @return String
   */
  public String getCity() {
    return this.sCity;
  }

  /**
   * Method used to set city of employee
   * @param sCity String
   */
  public void setCity(String sCity) {
    this.sCity = sCity;
  }

  /**
   * Method used to get state of employee
   * @return String
   */
  public String getState() {
    return this.sState;
  }

  /**
   * Method used to set state of employee
   * @param sState String
   */
  public void setState(String sState) {
    this.sState = sState;
  }

  /**
   * Method used to get employee postal code
   * @return String
   */
  public String getPostalCode() {
    return this.sPostalCode;
  }

  /**
   * Method used to set employee postal code
   * @param sPostalCode String
   */
  public void setPostalCode(String sPostalCode) {
    this.sPostalCode = sPostalCode;
  }

  /**
   * Method returns employee country
   * @return String
   */
  public String getCountry() {
    return this.sCountry;
  }

  /**
   * Method used to set employee country
   * @param sCountry String
   */
  public void setCountry(String sCountry) {
    this.sCountry = sCountry;
  }

  /**
   * Method return employee status
   * @return Long
   */
  public Long getEmployeeStatus() {
    return this.lEmployeeStatus;
  }

  /**
   * Method used to set employee status
   * @param lEmployeeStatus Long
   */
  public void setEmployeeStatus(Long lEmployeeStatus) {
    this.lEmployeeStatus = lEmployeeStatus;
  }

  /**
   * Method used to set employee status
   * @param lEmployeeStatus long
   */
  public void setEmployeeStatus(long lEmployeeStatus) {
    this.lEmployeeStatus = new Long(lEmployeeStatus);
  }

  /**
   * Method returns home store id
   * @return String
   */
  public String getHomeStoreId() {
    return this.sHomeStoreId;
  }

  /**
   * Method used to set home store id
   * @param lHomeStoreId String
   */
  public void setHomeStoreId(String sHomeStoreId) {
    this.sHomeStoreId = sHomeStoreId;
  }

  /**
   * Method return hire date
   * @return Date
   */
  public Date getHireDate() {
    return this.dtHireDate;
  }

  /**
   * Method used to set hire date
   * @param dtHireDate Date
   */
  public void setHireDate(Date dtHireDate) {
    this.dtHireDate = dtHireDate;
  }

  /**
   *
   * @return Date
   */
  public Date getTermDate() {
    return this.dtTermDate;
  }

  /**
   *
   * @param dtTermDate Date
   */
  public void setTermDate(Date dtTermDate) {
    this.dtTermDate = dtTermDate;
  }

  /**
   * Method return role
   * @return String
   */
  public String getRole() {
    return this.sRole;
  }

  /**
   * Method used to set role
   * @param sRole String
   */
  public void setRole(String sRole) {
    this.sRole = sRole;
  }

  /**
   * Method return stagging event id
   * @return Long
   */
  public Long getStgEventId() {
    return this.lStgEventId;
  }

  /**
   * Method used to set stagging event id
   * @param lStgEventId Long
   */
  public void setStgEventId(Long lStgEventId) {
    this.lStgEventId = lStgEventId;
  }

  /**
   * Method used to set stagging event id
   * @param lStgEventId long
   */
  public void setStgEventId(long lStgEventId) {
    this.lStgEventId = new Long(lStgEventId);
  }

  /**
   * Method used to set stagging event id
   * @param iStgEventId int
   */
  public void setStgEventId(int iStgEventId) {
    this.lStgEventId = new Long((long)iStgEventId);
  }

  /**
   * Method return stagging status
   * @return Long
   */
  public Long getStgStatus() {
    return this.lStgStatus;
  }

  /**
   * Method used to set stagging status
   * @param lStgStatus Long
   */
  public void setStgStatus(Long lStgStatus) {
    this.lStgStatus = lStgStatus;
  }

  /**
   * Method used to set stagging status
   * @param lStgStatus long
   */
  public void setStgStatus(long lStgStatus) {
    this.lStgStatus = new Long(lStgStatus);
  }

  /**
   * Method used to set stagging status
   * @param iStgStatus int
   */
  public void setStgStatus(int iStgStatus) {
    this.lStgStatus = new Long((long)iStgStatus);
  }

  /**
   * Method used to set error message
   * @return String
   */
  public String getStgErrorMessage() {
    return this.sStgErrorMessage;
  }

  /**
   * Method used to set error message
   * @param sStgErrorMessage String
   */
  public void setStgErrorMessage(String sStgErrorMessage) {
    this.sStgErrorMessage = sStgErrorMessage;
  }

  /**
   * Method used to set stagging process date
   * @return Date
   */
  public Date getStgLoadDate() {
    return this.dtStgLoadDate;
  }

  /**
   * Method used to set stagging load date
   * @param dtStgLoadDate Date
   */
  public void setStgLoadDate(Date dtStgLoadDate) {
    this.dtStgLoadDate = dtStgLoadDate;
  }

  /**
   * Method used to set stagging process date
   * @return Date
   */
  public Date getStgProcessDate() {
    return this.dtStgProcessDate;
  }

  /**
   * Method used to set stagging process date
   * @param dtStgProcessDate Date
   */
  public void setStgProcessDate(Date dtStgProcessDate) {
    this.dtStgProcessDate = dtStgProcessDate;
  }

  /**
   *
   * @return String
   */
  public String toString() {
    String sArmStgEmpData;
    sArmStgEmpData = "------ ArmaniStagingEmployeeData-------\n" + "TranType =" + lTranType + "\n"
        + "ID =" + sEmployeeID + "\n" + "First Name =" + sFirstName + "\n" + "LastName ="
        + sLastName + "\n" + "DateOfBirth =" + dtDateOfBirth + "\n" + "Email =" + sEmail + "\n"
        + "HomePhoneArea =" + sHomePhoneArea + "\n" + "HomePhoneNumber =" + sHomePhoneNumber + "\n"
        + "AddressLine1=" + sAddressLine1 + "\n" + "sAddressLine2=" + sAddressLine2 + "\n"
        + "City=" + sCity + "\n" + "State = " + sState + "\n" + "PostalCode= " + sPostalCode + "\n"
        + "Country =" + sCountry + "\n" + "EmployeeStatus =" + lEmployeeStatus + "\n"
        + "HomeStoreId =" + sHomeStoreId + "\n" + "HireDate=" + dtHireDate + "\n" + "TermDate="
        + dtTermDate + "\n" + "Role = " + sRole + "\n" + "EventID =" + lStgEventId + "\n"
        + "StagingStatus =" + lStgStatus + "\n" + "ErrorMessage =" + sStgErrorMessage + "\n"
        + "LoadDate =" + dtStgLoadDate + "\n" + "ProcessDate =" + dtStgProcessDate + "\n";
    return sArmStgEmpData;
  }
}
