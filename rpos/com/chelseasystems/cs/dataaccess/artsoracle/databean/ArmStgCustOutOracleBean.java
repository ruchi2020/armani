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
 * This class is an object representation of the Arts database table ARM_STG_CUST_OUT<BR>
 * Followings are the column of the table: <BR>
 *     RECORD_TYPE -- CHAR(2)<BR>
 *     CUSTOMER_ID -- VARCHAR2(20)<BR>
 *     STATUS -- NUMBER(1)<BR>
 *     DATA_POPULATION_DT -- DATE(7)<BR>
 *     EXTRACTED_DT -- DATE(7)<BR>
 *     CUST_BARCODE -- VARCHAR2(20)<BR>
 *     TITLE -- VARCHAR2(10)<BR>
 *     FIRST_NM -- VARCHAR2(30)<BR>
 *     LAST_NM -- VARCHAR2(30)<BR>
 *     MIDDLE_NM -- VARCHAR2(15)<BR>
 *     SUFFIX -- VARCHAR2(12)<BR>
 *     DB_FIRST_NM -- VARCHAR2(60)<BR>
 *     DB_LAST_NM -- VARCHAR2(60)<BR>
 *     GENDER -- CHAR(1)<BR>
 *     CUST_TYPE -- VARCHAR2(10)<BR>
 *     LOYALTY_PROGRAM -- CHAR(1)<BR>
 *     VIP_PERCENT -- NUMBER(4.2)<BR>
 *     PRIVACY -- CHAR(1)<BR>
 *     NO_MAIL -- CHAR(1)<BR>
 *     NO_EMAIL -- CHAR(1)<BR>
 *     NO_CALL -- CHAR(1)<BR>
 *     NO_SMS -- CHAR(1)<BR>
 *     EMAIL1 -- VARCHAR2(40)<BR>
 *     EMAIL2 -- VARCHAR2(40)<BR>
 *     VAT_NUM -- VARCHAR2(25)<BR>
 *     FISCAL_CD -- VARCHAR2(25)<BR>
 *     ID_TYPE -- VARCHAR2(25)<BR>
 *     DOC_NUM -- VARCHAR2(25)<BR>
 *     PLACE_OF_ISSUE -- VARCHAR2(25)<BR>
 *     ISSUE_DT -- DATE(7)<BR>
 *     PAY_TYPE -- VARCHAR2(25)<BR>
 *     COMPANY_ID -- VARCHAR2(11)<BR>
 *     INTER_CMY_CD -- VARCHAR2(11)<BR>
 *     CUST_STATUS -- VARCHAR2(6)<BR>
 *     ACC_NUM -- VARCHAR2(11)<BR>
 *     BIRTH_DAY -- VARCHAR2(2)<BR>
 *     BIRTH_MONTH -- VARCHAR2(2)<BR>
 *     REAL_BIRTHDAY -- DATE(7)<BR>
 *     AGE -- VARCHAR2(2)<BR>
 *     REFERRED_BY -- VARCHAR2(11)<BR>
 *     PROFESSION -- VARCHAR2(25)<BR>
 *     EDUCATION -- VARCHAR2(25)<BR>
 *     NOTES1 -- VARCHAR2(100)<BR>
 *     NOTES2 -- VARCHAR2(100)<BR>
 *     PNTR_FMLY_NM -- VARCHAR2(30)<BR>
 *     PNTR_NM -- VARCHAR2(30)<BR>
 *     BIRTH_PLACE -- VARCHAR2(30)<BR>
 *     SPL_EVT_TYPE -- VARCHAR2(20)<BR>
 *     SPL_EVT_DT -- DATE(7)<BR>
 *     CHILDREN_NAMES -- VARCHAR2(50)<BR>
 *     NUM_OF_CHILDREN -- NUMBER(2)<BR>
 *     CREATEDOFFLINE -- CHAR(1)<BR>
 *     COUNTRY -- VARCHAR2(30)<BR>
 *     POST_CODE -- VARCHAR2(10)<BR>
 *     STATE -- VARCHAR2(30)<BR>
 *     CITY -- VARCHAR2(30)<BR>
 *     ADDRESS_LINE_1 -- VARCHAR2(60)<BR>
 *     ADDRESS_LINE_2 -- VARCHAR2(60)<BR>
 *     UNIT_NAME -- VARCHAR2(50)<BR>
 *     ADDRESS_TYPE -- VARCHAR2(20)<BR>
 *     PHONE_TYPE_1 -- VARCHAR2(20)<BR>
 *     PHONE_NUMBER_1 -- VARCHAR2(20)<BR>
 *     PHONE_TYPE_2 -- VARCHAR2(20)<BR>
 *     PHONE_NUMBER_2 -- VARCHAR2(20)<BR>
 *     PHONE_TYPE_3 -- VARCHAR2(20)<BR>
 *     PHONE_NUMBER_3 -- VARCHAR2(20)<BR>
 *     USE_AS_PRIMARY_ADDR -- CHAR(1)<BR>
 *     STORE_ID -- VARCHAR2(30)<BR>
 *     COMPANY_CD -- VARCHAR2(30)<BR>
 *     BRAND -- VARCHAR2(30)<BR>
 *     ASSOCIATE_ID -- VARCHAR2(30)<BR>
 *     CREATE_DATE -- DATE(7)<BR>
 *     COMMENTS -- VARCHAR2(50)<BR>
 *     SUPPLIER_PAYMENT -- VARCHAR2(20)<BR>
 *     BANK -- VARCHAR2(30)<BR>
 *     CRDT_CRD_NUM_1 -- VARCHAR2(30)<BR>
 *     CRDT_CRD_TYP_1 -- VARCHAR2(30)<BR>
 *     CRDT_CRD_NUM_2 -- VARCHAR2(30)<BR>
 *     CRDT_CRD_TYP_2 -- VARCHAR2(30)<BR>
 *    ADDED BY VIVEK FOR CUSTOMER PRIVACY MANAGEMENT
 *     FL_MASTER -- VARCHAR2(1)<BR>
 *     FL_MARKETING -- VARCHAR2(1)<BR>
 *    ADDED BY VIVEK FOR CUSTOMER PRIVACY MANAGEMENT
 *     CUST_ISSUE_DATE -- DATE<BR>
 * 
 */
public class ArmStgCustOutOracleBean extends BaseOracleBean {

  public ArmStgCustOutOracleBean() {}

  public static String selectSql = "select RECORD_TYPE, CUSTOMER_ID, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, CUST_BARCODE, TITLE, FIRST_NM, LAST_NM, MIDDLE_NM, SUFFIX, DB_FIRST_NM, DB_LAST_NM, GENDER, CUST_TYPE, LOYALTY_PROGRAM, VIP_PERCENT, PRIVACY, NO_MAIL, NO_EMAIL, NO_CALL, NO_SMS, EMAIL1, EMAIL2, VAT_NUM, FISCAL_CD, ID_TYPE, DOC_NUM, PLACE_OF_ISSUE, ISSUE_DT, PAY_TYPE, COMPANY_ID, INTER_CMY_CD, CUST_STATUS, ACC_NUM, BIRTH_DAY, BIRTH_MONTH, REAL_BIRTHDAY, AGE, REFERRED_BY, PROFESSION, EDUCATION, NOTES1, NOTES2, PNTR_FMLY_NM, PNTR_NM, BIRTH_PLACE, SPL_EVT_TYPE, SPL_EVT_DT, CHILDREN_NAMES, NUM_OF_CHILDREN, CREATEDOFFLINE, COUNTRY, POST_CODE, STATE, CITY, ADDRESS_LINE_1, ADDRESS_LINE_2, UNIT_NAME, ADDRESS_TYPE, PHONE_TYPE_1, PHONE_NUMBER_1, PHONE_TYPE_2, PHONE_NUMBER_2, PHONE_TYPE_3, PHONE_NUMBER_3, USE_AS_PRIMARY_ADDR, STORE_ID, COMPANY_CD, BRAND, ASSOCIATE_ID, CREATE_DATE, COMMENTS, SUPPLIER_PAYMENT, BANK, CRDT_CRD_NUM_1, CRDT_CRD_TYP_1, CRDT_CRD_NUM_2, CRDT_CRD_TYP_2, FL_MASTER, FL_MARKETING, CUST_ISSUE_DATE  from ARM_STG_CUST_OUT ";
  public static String insertSql = "insert into ARM_STG_CUST_OUT (RECORD_TYPE, CUSTOMER_ID, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, CUST_BARCODE, TITLE, FIRST_NM, LAST_NM, MIDDLE_NM, SUFFIX, DB_FIRST_NM, DB_LAST_NM, GENDER, CUST_TYPE, LOYALTY_PROGRAM, VIP_PERCENT, PRIVACY, NO_MAIL, NO_EMAIL, NO_CALL, NO_SMS, EMAIL1, EMAIL2, VAT_NUM, FISCAL_CD, ID_TYPE, DOC_NUM, PLACE_OF_ISSUE, ISSUE_DT, PAY_TYPE, COMPANY_ID, INTER_CMY_CD, CUST_STATUS, ACC_NUM, BIRTH_DAY, BIRTH_MONTH, REAL_BIRTHDAY, AGE, REFERRED_BY, PROFESSION, EDUCATION, NOTES1, NOTES2, PNTR_FMLY_NM, PNTR_NM, BIRTH_PLACE, SPL_EVT_TYPE, SPL_EVT_DT, CHILDREN_NAMES, NUM_OF_CHILDREN, CREATEDOFFLINE, COUNTRY, POST_CODE, STATE, CITY, ADDRESS_LINE_1, ADDRESS_LINE_2, UNIT_NAME, ADDRESS_TYPE, PHONE_TYPE_1, PHONE_NUMBER_1, PHONE_TYPE_2, PHONE_NUMBER_2, PHONE_TYPE_3, PHONE_NUMBER_3, USE_AS_PRIMARY_ADDR, STORE_ID, COMPANY_CD, BRAND, ASSOCIATE_ID, CREATE_DATE, COMMENTS, SUPPLIER_PAYMENT, BANK, CRDT_CRD_NUM_1, CRDT_CRD_TYP_1, CRDT_CRD_NUM_2, CRDT_CRD_TYP_2, FL_MASTER, FL_MARKETING, CUST_ISSUE_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";  
  public static String updateSql = "update ARM_STG_CUST_OUT set RECORD_TYPE = ?, CUSTOMER_ID = ?, STATUS = ?, DATA_POPULATION_DT = ?, EXTRACTED_DT = ?, CUST_BARCODE = ?, TITLE = ?, FIRST_NM = ?, LAST_NM = ?, MIDDLE_NM = ?, SUFFIX = ?, DB_FIRST_NM = ?, DB_LAST_NM = ?, GENDER = ?, CUST_TYPE = ?, LOYALTY_PROGRAM = ?, VIP_PERCENT = ?, PRIVACY = ?, NO_MAIL = ?, NO_EMAIL = ?, NO_CALL = ?, NO_SMS = ?, EMAIL1 = ?, EMAIL2 = ?, VAT_NUM = ?, FISCAL_CD = ?, ID_TYPE = ?, DOC_NUM = ?, PLACE_OF_ISSUE = ?, ISSUE_DT = ?, PAY_TYPE = ?, COMPANY_ID = ?, INTER_CMY_CD = ?, CUST_STATUS = ?, ACC_NUM = ?, BIRTH_DAY = ?, BIRTH_MONTH = ?, REAL_BIRTHDAY = ?, AGE = ?, REFERRED_BY = ?, PROFESSION = ?, EDUCATION = ?, NOTES1 = ?, NOTES2 = ?, PNTR_FMLY_NM = ?, PNTR_NM = ?, BIRTH_PLACE = ?, SPL_EVT_TYPE = ?, SPL_EVT_DT = ?, CHILDREN_NAMES = ?, NUM_OF_CHILDREN = ?, CREATEDOFFLINE = ?, COUNTRY = ?, POST_CODE = ?, STATE = ?, CITY = ?, ADDRESS_LINE_1 = ?, ADDRESS_LINE_2 = ?, UNIT_NAME = ?, ADDRESS_TYPE = ?, PHONE_TYPE_1 = ?, PHONE_NUMBER_1 = ?, PHONE_TYPE_2 = ?, PHONE_NUMBER_2 = ?, PHONE_TYPE_3 = ?, PHONE_NUMBER_3 = ?, USE_AS_PRIMARY_ADDR = ?, STORE_ID = ?, COMPANY_CD = ?, BRAND = ?, ASSOCIATE_ID = ?, CREATE_DATE = ?, COMMENTS = ?, SUPPLIER_PAYMENT = ?, BANK = ?, CRDT_CRD_NUM_1 = ?, CRDT_CRD_TYP_1 = ?, CRDT_CRD_NUM_2 = ?, CRDT_CRD_TYP_2 = ?, FL_MASTER = ?, FL_MARKETING = ?, CUST_ISSUE_DATE = ? ";  
  public static String deleteSql = "delete from ARM_STG_CUST_OUT ";

  public static String TABLE_NAME = "ARM_STG_CUST_OUT";
  public static String COL_RECORD_TYPE = "ARM_STG_CUST_OUT.RECORD_TYPE";
  public static String COL_CUSTOMER_ID = "ARM_STG_CUST_OUT.CUSTOMER_ID";
  public static String COL_STATUS = "ARM_STG_CUST_OUT.STATUS";
  public static String COL_DATA_POPULATION_DT = "ARM_STG_CUST_OUT.DATA_POPULATION_DT";
  public static String COL_EXTRACTED_DT = "ARM_STG_CUST_OUT.EXTRACTED_DT";
  public static String COL_CUST_BARCODE = "ARM_STG_CUST_OUT.CUST_BARCODE";
  public static String COL_TITLE = "ARM_STG_CUST_OUT.TITLE";
  public static String COL_FIRST_NM = "ARM_STG_CUST_OUT.FIRST_NM";
  public static String COL_LAST_NM = "ARM_STG_CUST_OUT.LAST_NM";
  public static String COL_MIDDLE_NM = "ARM_STG_CUST_OUT.MIDDLE_NM";
  public static String COL_SUFFIX = "ARM_STG_CUST_OUT.SUFFIX";
  public static String COL_DB_FIRST_NM = "ARM_STG_CUST_OUT.DB_FIRST_NM";
  public static String COL_DB_LAST_NM = "ARM_STG_CUST_OUT.DB_LAST_NM";
  public static String COL_GENDER = "ARM_STG_CUST_OUT.GENDER";
  public static String COL_CUST_TYPE = "ARM_STG_CUST_OUT.CUST_TYPE";
  public static String COL_LOYALTY_PROGRAM = "ARM_STG_CUST_OUT.LOYALTY_PROGRAM";
  public static String COL_VIP_PERCENT = "ARM_STG_CUST_OUT.VIP_PERCENT";
  public static String COL_PRIVACY = "ARM_STG_CUST_OUT.PRIVACY";
  public static String COL_NO_MAIL = "ARM_STG_CUST_OUT.NO_MAIL";
  public static String COL_NO_EMAIL = "ARM_STG_CUST_OUT.NO_EMAIL";
  public static String COL_NO_CALL = "ARM_STG_CUST_OUT.NO_CALL";
  public static String COL_NO_SMS = "ARM_STG_CUST_OUT.NO_SMS";
  public static String COL_EMAIL1 = "ARM_STG_CUST_OUT.EMAIL1";
  public static String COL_EMAIL2 = "ARM_STG_CUST_OUT.EMAIL2";
  public static String COL_VAT_NUM = "ARM_STG_CUST_OUT.VAT_NUM";
  public static String COL_FISCAL_CD = "ARM_STG_CUST_OUT.FISCAL_CD";
  public static String COL_ID_TYPE = "ARM_STG_CUST_OUT.ID_TYPE";
  public static String COL_DOC_NUM = "ARM_STG_CUST_OUT.DOC_NUM";
  public static String COL_PLACE_OF_ISSUE = "ARM_STG_CUST_OUT.PLACE_OF_ISSUE";
  public static String COL_ISSUE_DT = "ARM_STG_CUST_OUT.ISSUE_DT";
  public static String COL_PAY_TYPE = "ARM_STG_CUST_OUT.PAY_TYPE";
  public static String COL_COMPANY_ID = "ARM_STG_CUST_OUT.COMPANY_ID";
  public static String COL_INTER_CMY_CD = "ARM_STG_CUST_OUT.INTER_CMY_CD";
  public static String COL_CUST_STATUS = "ARM_STG_CUST_OUT.CUST_STATUS";
  public static String COL_ACC_NUM = "ARM_STG_CUST_OUT.ACC_NUM";
  public static String COL_BIRTH_DAY = "ARM_STG_CUST_OUT.BIRTH_DAY";
  public static String COL_BIRTH_MONTH = "ARM_STG_CUST_OUT.BIRTH_MONTH";
  public static String COL_REAL_BIRTHDAY = "ARM_STG_CUST_OUT.REAL_BIRTHDAY";
  public static String COL_AGE = "ARM_STG_CUST_OUT.AGE";
  public static String COL_REFERRED_BY = "ARM_STG_CUST_OUT.REFERRED_BY";
  public static String COL_PROFESSION = "ARM_STG_CUST_OUT.PROFESSION";
  public static String COL_EDUCATION = "ARM_STG_CUST_OUT.EDUCATION";
  public static String COL_NOTES1 = "ARM_STG_CUST_OUT.NOTES1";
  public static String COL_NOTES2 = "ARM_STG_CUST_OUT.NOTES2";
  public static String COL_PNTR_FMLY_NM = "ARM_STG_CUST_OUT.PNTR_FMLY_NM";
  public static String COL_PNTR_NM = "ARM_STG_CUST_OUT.PNTR_NM";
  public static String COL_BIRTH_PLACE = "ARM_STG_CUST_OUT.BIRTH_PLACE";
  public static String COL_SPL_EVT_TYPE = "ARM_STG_CUST_OUT.SPL_EVT_TYPE";
  public static String COL_SPL_EVT_DT = "ARM_STG_CUST_OUT.SPL_EVT_DT";
  public static String COL_CHILDREN_NAMES = "ARM_STG_CUST_OUT.CHILDREN_NAMES";
  public static String COL_NUM_OF_CHILDREN = "ARM_STG_CUST_OUT.NUM_OF_CHILDREN";
  public static String COL_CREATEDOFFLINE = "ARM_STG_CUST_OUT.CREATEDOFFLINE";
  public static String COL_COUNTRY = "ARM_STG_CUST_OUT.COUNTRY";
  public static String COL_POST_CODE = "ARM_STG_CUST_OUT.POST_CODE";
  public static String COL_STATE = "ARM_STG_CUST_OUT.STATE";
  public static String COL_CITY = "ARM_STG_CUST_OUT.CITY";
  public static String COL_ADDRESS_LINE_1 = "ARM_STG_CUST_OUT.ADDRESS_LINE_1";
  public static String COL_ADDRESS_LINE_2 = "ARM_STG_CUST_OUT.ADDRESS_LINE_2";
  public static String COL_UNIT_NAME = "ARM_STG_CUST_OUT.UNIT_NAME";
  public static String COL_ADDRESS_TYPE = "ARM_STG_CUST_OUT.ADDRESS_TYPE";
  public static String COL_PHONE_TYPE_1 = "ARM_STG_CUST_OUT.PHONE_TYPE_1";
  public static String COL_PHONE_NUMBER_1 = "ARM_STG_CUST_OUT.PHONE_NUMBER_1";
  public static String COL_PHONE_TYPE_2 = "ARM_STG_CUST_OUT.PHONE_TYPE_2";
  public static String COL_PHONE_NUMBER_2 = "ARM_STG_CUST_OUT.PHONE_NUMBER_2";
  public static String COL_PHONE_TYPE_3 = "ARM_STG_CUST_OUT.PHONE_TYPE_3";
  public static String COL_PHONE_NUMBER_3 = "ARM_STG_CUST_OUT.PHONE_NUMBER_3";
  public static String COL_USE_AS_PRIMARY_ADDR = "ARM_STG_CUST_OUT.USE_AS_PRIMARY_ADDR";
  public static String COL_STORE_ID = "ARM_STG_CUST_OUT.STORE_ID";
  public static String COL_COMPANY_CD = "ARM_STG_CUST_OUT.COMPANY_CD";
  public static String COL_BRAND = "ARM_STG_CUST_OUT.BRAND";
  public static String COL_ASSOCIATE_ID = "ARM_STG_CUST_OUT.ASSOCIATE_ID";
  public static String COL_CREATE_DATE = "ARM_STG_CUST_OUT.CREATE_DATE";
  public static String COL_COMMENTS = "ARM_STG_CUST_OUT.COMMENTS";
  public static String COL_SUPPLIER_PAYMENT = "ARM_STG_CUST_OUT.SUPPLIER_PAYMENT";
  public static String COL_BANK = "ARM_STG_CUST_OUT.BANK";
  public static String COL_CRDT_CRD_NUM_1 = "ARM_STG_CUST_OUT.CRDT_CRD_NUM_1";
  public static String COL_CRDT_CRD_TYP_1 = "ARM_STG_CUST_OUT.CRDT_CRD_TYP_1";
  public static String COL_CRDT_CRD_NUM_2 = "ARM_STG_CUST_OUT.CRDT_CRD_NUM_2";
  public static String COL_CRDT_CRD_TYP_2 = "ARM_STG_CUST_OUT.CRDT_CRD_TYP_2";
  //Added for Privacy Mgmt Marketing and Master
  public static String COL_FL_MASTER = "ARM_STG_CUST_OUT.FL_MASTER";
  public static String COL_FL_MARKETING = "ARM_STG_CUST_OUT.FL_MARKETING";
  public static String COL_CUST_ISSUE_DATE = "ARM_STG_CUST_OUT.CUST_ISSUE_DATE";
  
  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String recordType;
  private String customerId;
  private Boolean status;
  private Date dataPopulationDt;
  private Date extractedDt;
  private String custBarcode;
  private String title;
  private String firstNm;
  private String lastNm;
  private String middleNm;
  private String suffix;
  private String dbFirstNm;
  private String dbLastNm;
  private String gender;
  private String custType;
  private String loyaltyProgram;
  private Double vipPercent;
  private String privacy;
  private String noMail;
  private String noEmail;
  private String noCall;
  private String noSms;
  private String email1;
  private String email2;
  private String vatNum;
  private String fiscalCd;
  private String idType;
  private String docNum;
  private String placeOfIssue;
  private Date issueDt;
  private String payType;
  private String companyId;
  private String interCmyCd;
  private String custStatus;
  private String accNum;
  private String birthDay;
  private String birthMonth;
  private Date realBirthday;
  private String age;
  private String referredBy;
  private String profession;
  private String education;
  private String notes1;
  private String notes2;
  private String pntrFmlyNm;
  private String pntrNm;
  private String birthPlace;
  private String splEvtType;
  private Date splEvtDt;
  private String childrenNames;
  private Long numOfChildren;
  private String createdoffline;
  private String country;
  private String postCode;
  private String state;
  private String city;
  private String addressLine1;
  private String addressLine2;
  private String unitName;
  private String addressType;
  private String phoneType1;
  private String phoneNumber1;
  private String phoneType2;
  private String phoneNumber2;
  private String phoneType3;
  private String phoneNumber3;
  private String useAsPrimaryAddr;
  private String storeId;
  private String companyCd;
  private String brand;
  private String associateId;
  private Date createDate;
  private String comments;
  private String supplierPayment;
  private String bank;
  private String crdtCrdNum1;
  private String crdtCrdTyp1;
  private String crdtCrdNum2;
  private String crdtCrdTyp2;
  //Added for Privacy Mgmt Marketing and Master
  private String flMaster;
  private String flMarketing;
  private Date custIssueDate;

  public String getRecordType() { return this.recordType; }
  public void setRecordType(String recordType) { this.recordType = recordType; }

  public String getCustomerId() { return this.customerId; }
  public void setCustomerId(String customerId) { this.customerId = customerId; }

  public Boolean getStatus() { return this.status; }
  public void setStatus(Boolean status) { this.status = status; }
  public void setStatus(boolean status) { this.status = new Boolean(status); }

  public Date getDataPopulationDt() { return this.dataPopulationDt; }
  public void setDataPopulationDt(Date dataPopulationDt) { this.dataPopulationDt = dataPopulationDt; }

  public Date getExtractedDt() { return this.extractedDt; }
  public void setExtractedDt(Date extractedDt) { this.extractedDt = extractedDt; }

  public String getCustBarcode() { return this.custBarcode; }
  public void setCustBarcode(String custBarcode) { this.custBarcode = custBarcode; }

  public String getTitle() { return this.title; }
  public void setTitle(String title) { this.title = title; }

  public String getFirstNm() { return this.firstNm; }
  public void setFirstNm(String firstNm) { this.firstNm = firstNm; }

  public String getLastNm() { return this.lastNm; }
  public void setLastNm(String lastNm) { this.lastNm = lastNm; }

  public String getMiddleNm() { return this.middleNm; }
  public void setMiddleNm(String middleNm) { this.middleNm = middleNm; }

  public String getSuffix() { return this.suffix; }
  public void setSuffix(String suffix) { this.suffix = suffix; }

  public String getDbFirstNm() { return this.dbFirstNm; }
  public void setDbFirstNm(String dbFirstNm) { this.dbFirstNm = dbFirstNm; }

  public String getDbLastNm() { return this.dbLastNm; }
  public void setDbLastNm(String dbLastNm) { this.dbLastNm = dbLastNm; }

  public String getGender() { return this.gender; }
  public void setGender(String gender) { this.gender = gender; }

  public String getCustType() { return this.custType; }
  public void setCustType(String custType) { this.custType = custType; }

  public String getLoyaltyProgram() { return this.loyaltyProgram; }
  public void setLoyaltyProgram(String loyaltyProgram) { this.loyaltyProgram = loyaltyProgram; }

  public Double getVipPercent() { return this.vipPercent; }
  public void setVipPercent(Double vipPercent) { this.vipPercent = vipPercent; }
  public void setVipPercent(double vipPercent) { this.vipPercent = new Double(vipPercent); }

  public String getPrivacy() { return this.privacy; }
  public void setPrivacy(String privacy) { this.privacy = privacy; }

  public String getNoMail() { return this.noMail; }
  public void setNoMail(String noMail) { this.noMail = noMail; }

  public String getNoEmail() { return this.noEmail; }
  public void setNoEmail(String noEmail) { this.noEmail = noEmail; }

  public String getNoCall() { return this.noCall; }
  public void setNoCall(String noCall) { this.noCall = noCall; }

  public String getNoSms() { return this.noSms; }
  public void setNoSms(String noSms) { this.noSms = noSms; }

  public String getEmail1() { return this.email1; }
  public void setEmail1(String email1) { this.email1 = email1; }

  public String getEmail2() { return this.email2; }
  public void setEmail2(String email2) { this.email2 = email2; }

  public String getVatNum() { return this.vatNum; }
  public void setVatNum(String vatNum) { this.vatNum = vatNum; }

  public String getFiscalCd() { return this.fiscalCd; }
  public void setFiscalCd(String fiscalCd) { this.fiscalCd = fiscalCd; }

  public String getIdType() { return this.idType; }
  public void setIdType(String idType) { this.idType = idType; }

  public String getDocNum() { return this.docNum; }
  public void setDocNum(String docNum) { this.docNum = docNum; }

  public String getPlaceOfIssue() { return this.placeOfIssue; }
  public void setPlaceOfIssue(String placeOfIssue) { this.placeOfIssue = placeOfIssue; }

  public Date getIssueDt() { return this.issueDt; }
  public void setIssueDt(Date issueDt) { this.issueDt = issueDt; }

  public String getPayType() { return this.payType; }
  public void setPayType(String payType) { this.payType = payType; }

  public String getCompanyId() { return this.companyId; }
  public void setCompanyId(String companyId) { this.companyId = companyId; }

  public String getInterCmyCd() { return this.interCmyCd; }
  public void setInterCmyCd(String interCmyCd) { this.interCmyCd = interCmyCd; }

  public String getCustStatus() { return this.custStatus; }
  public void setCustStatus(String custStatus) { this.custStatus = custStatus; }

  public String getAccNum() { return this.accNum; }
  public void setAccNum(String accNum) { this.accNum = accNum; }

  public String getBirthDay() { return this.birthDay; }
  public void setBirthDay(String birthDay) { this.birthDay = birthDay; }

  public String getBirthMonth() { return this.birthMonth; }
  public void setBirthMonth(String birthMonth) { this.birthMonth = birthMonth; }

  public Date getRealBirthday() { return this.realBirthday; }
  public void setRealBirthday(Date realBirthday) { this.realBirthday = realBirthday; }

  public String getAge() { return this.age; }
  public void setAge(String age) { this.age = age; }

  public String getReferredBy() { return this.referredBy; }
  public void setReferredBy(String referredBy) { this.referredBy = referredBy; }

  public String getProfession() { return this.profession; }
  public void setProfession(String profession) { this.profession = profession; }

  public String getEducation() { return this.education; }
  public void setEducation(String education) { this.education = education; }

  public String getNotes1() { return this.notes1; }
  public void setNotes1(String notes1) { this.notes1 = notes1; }

  public String getNotes2() { return this.notes2; }
  public void setNotes2(String notes2) { this.notes2 = notes2; }

  public String getPntrFmlyNm() { return this.pntrFmlyNm; }
  public void setPntrFmlyNm(String pntrFmlyNm) { this.pntrFmlyNm = pntrFmlyNm; }

  public String getPntrNm() { return this.pntrNm; }
  public void setPntrNm(String pntrNm) { this.pntrNm = pntrNm; }

  public String getBirthPlace() { return this.birthPlace; }
  public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }

  public String getSplEvtType() { return this.splEvtType; }
  public void setSplEvtType(String splEvtType) { this.splEvtType = splEvtType; }

  public Date getSplEvtDt() { return this.splEvtDt; }
  public void setSplEvtDt(Date splEvtDt) { this.splEvtDt = splEvtDt; }

  public String getChildrenNames() { return this.childrenNames; }
  public void setChildrenNames(String childrenNames) { this.childrenNames = childrenNames; }

  public Long getNumOfChildren() { return this.numOfChildren; }
  public void setNumOfChildren(Long numOfChildren) { this.numOfChildren = numOfChildren; }
  public void setNumOfChildren(long numOfChildren) { this.numOfChildren = new Long(numOfChildren); }
  public void setNumOfChildren(int numOfChildren) { this.numOfChildren = new Long((long)numOfChildren); }

  public String getCreatedoffline() { return this.createdoffline; }
  public void setCreatedoffline(String createdoffline) { this.createdoffline = createdoffline; }

  public String getCountry() { return this.country; }
  public void setCountry(String country) { this.country = country; }

  public String getPostCode() { return this.postCode; }
  public void setPostCode(String postCode) { this.postCode = postCode; }

  public String getState() { return this.state; }
  public void setState(String state) { this.state = state; }

  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }

  public String getAddressLine1() { return this.addressLine1; }
  public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

  public String getAddressLine2() { return this.addressLine2; }
  public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

  public String getUnitName() { return this.unitName; }
  public void setUnitName(String unitName) { this.unitName = unitName; }

  public String getAddressType() { return this.addressType; }
  public void setAddressType(String addressType) { this.addressType = addressType; }

  public String getPhoneType1() { return this.phoneType1; }
  public void setPhoneType1(String phoneType1) { this.phoneType1 = phoneType1; }

  public String getPhoneNumber1() { return this.phoneNumber1; }
  public void setPhoneNumber1(String phoneNumber1) { this.phoneNumber1 = phoneNumber1; }

  public String getPhoneType2() { return this.phoneType2; }
  public void setPhoneType2(String phoneType2) { this.phoneType2 = phoneType2; }

  public String getPhoneNumber2() { return this.phoneNumber2; }
  public void setPhoneNumber2(String phoneNumber2) { this.phoneNumber2 = phoneNumber2; }

  public String getPhoneType3() { return this.phoneType3; }
  public void setPhoneType3(String phoneType3) { this.phoneType3 = phoneType3; }

  public String getPhoneNumber3() { return this.phoneNumber3; }
  public void setPhoneNumber3(String phoneNumber3) { this.phoneNumber3 = phoneNumber3; }

  public String getUseAsPrimaryAddr() { return this.useAsPrimaryAddr; }
  public void setUseAsPrimaryAddr(String useAsPrimaryAddr) { this.useAsPrimaryAddr = useAsPrimaryAddr; }

  public String getStoreId() { return this.storeId; }
  public void setStoreId(String storeId) { this.storeId = storeId; }

  public String getCompanyCd() { return this.companyCd; }
  public void setCompanyCd(String companyCd) { this.companyCd = companyCd; }

  public String getBrand() { return this.brand; }
  public void setBrand(String brand) { this.brand = brand; }

  public String getAssociateId() { return this.associateId; }
  public void setAssociateId(String associateId) { this.associateId = associateId; }

  public Date getCreateDate() { return this.createDate; }
  public void setCreateDate(Date createDate) { this.createDate = createDate; }

  public String getComments() { return this.comments; }
  public void setComments(String comments) { this.comments = comments; }

  public String getSupplierPayment() { return this.supplierPayment; }
  public void setSupplierPayment(String supplierPayment) { this.supplierPayment = supplierPayment; }

  public String getBank() { return this.bank; }
  public void setBank(String bank) { this.bank = bank; }

  public String getCrdtCrdNum1() { return this.crdtCrdNum1; }
  public void setCrdtCrdNum1(String crdtCrdNum1) { this.crdtCrdNum1 = crdtCrdNum1; }

  public String getCrdtCrdTyp1() { return this.crdtCrdTyp1; }
  public void setCrdtCrdTyp1(String crdtCrdTyp1) { this.crdtCrdTyp1 = crdtCrdTyp1; }

  public String getCrdtCrdNum2() { return this.crdtCrdNum2; }
  public void setCrdtCrdNum2(String crdtCrdNum2) { this.crdtCrdNum2 = crdtCrdNum2; }

  public String getCrdtCrdTyp2() { return this.crdtCrdTyp2; }
  public void setCrdtCrdTyp2(String crdtCrdTyp2) { this.crdtCrdTyp2 = crdtCrdTyp2; }
  
  //Added for Privacy Mgmt Marketing and Master
  public String getFlMarketing() {	return this.flMarketing;	}
  public void setFlMarketing(String flMarketing) {	this.flMarketing = flMarketing; }
  
  public String getFlMaster() {	return this.flMaster;}
  public void setFlMaster(String flMaster) {this.flMaster = flMaster;	}

  public Date getCustIssueDate() {return this.custIssueDate;}
  public void setCustIssueDate(Date custIssueDate) {this.custIssueDate = custIssueDate;}
  
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmStgCustOutOracleBean bean = new ArmStgCustOutOracleBean();
      bean.recordType = getStringFromResultSet(rs, "RECORD_TYPE");
      bean.customerId = getStringFromResultSet(rs, "CUSTOMER_ID");
      bean.status = getBooleanFromResultSet(rs, "STATUS");
      bean.dataPopulationDt = getDateFromResultSet(rs, "DATA_POPULATION_DT");
      bean.extractedDt = getDateFromResultSet(rs, "EXTRACTED_DT");
      bean.custBarcode = getStringFromResultSet(rs, "CUST_BARCODE");
      bean.title = getStringFromResultSet(rs, "TITLE");
      bean.firstNm = getStringFromResultSet(rs, "FIRST_NM");
      bean.lastNm = getStringFromResultSet(rs, "LAST_NM");
      bean.middleNm = getStringFromResultSet(rs, "MIDDLE_NM");
      bean.suffix = getStringFromResultSet(rs, "SUFFIX");
      bean.dbFirstNm = getStringFromResultSet(rs, "DB_FIRST_NM");
      bean.dbLastNm = getStringFromResultSet(rs, "DB_LAST_NM");
      bean.gender = getStringFromResultSet(rs, "GENDER");
      bean.custType = getStringFromResultSet(rs, "CUST_TYPE");
      bean.loyaltyProgram = getStringFromResultSet(rs, "LOYALTY_PROGRAM");
      bean.vipPercent = getDoubleFromResultSet(rs, "VIP_PERCENT");
      bean.privacy = getStringFromResultSet(rs, "PRIVACY");
      bean.noMail = getStringFromResultSet(rs, "NO_MAIL");
      bean.noEmail = getStringFromResultSet(rs, "NO_EMAIL");
      bean.noCall = getStringFromResultSet(rs, "NO_CALL");
      bean.noSms = getStringFromResultSet(rs, "NO_SMS");
      bean.email1 = getStringFromResultSet(rs, "EMAIL1");
      bean.email2 = getStringFromResultSet(rs, "EMAIL2");
      bean.vatNum = getStringFromResultSet(rs, "VAT_NUM");
      bean.fiscalCd = getStringFromResultSet(rs, "FISCAL_CD");
      bean.idType = getStringFromResultSet(rs, "ID_TYPE");
      bean.docNum = getStringFromResultSet(rs, "DOC_NUM");
      bean.placeOfIssue = getStringFromResultSet(rs, "PLACE_OF_ISSUE");
      bean.issueDt = getDateFromResultSet(rs, "ISSUE_DT");
      bean.payType = getStringFromResultSet(rs, "PAY_TYPE");
      bean.companyId = getStringFromResultSet(rs, "COMPANY_ID");
      bean.interCmyCd = getStringFromResultSet(rs, "INTER_CMY_CD");
      bean.custStatus = getStringFromResultSet(rs, "CUST_STATUS");
      bean.accNum = getStringFromResultSet(rs, "ACC_NUM");
      bean.birthDay = getStringFromResultSet(rs, "BIRTH_DAY");
      bean.birthMonth = getStringFromResultSet(rs, "BIRTH_MONTH");
      bean.realBirthday = getDateFromResultSet(rs, "REAL_BIRTHDAY");
      bean.age = getStringFromResultSet(rs, "AGE");
      bean.referredBy = getStringFromResultSet(rs, "REFERRED_BY");
      bean.profession = getStringFromResultSet(rs, "PROFESSION");
      bean.education = getStringFromResultSet(rs, "EDUCATION");
      bean.notes1 = getStringFromResultSet(rs, "NOTES1");
      bean.notes2 = getStringFromResultSet(rs, "NOTES2");
      bean.pntrFmlyNm = getStringFromResultSet(rs, "PNTR_FMLY_NM");
      bean.pntrNm = getStringFromResultSet(rs, "PNTR_NM");
      bean.birthPlace = getStringFromResultSet(rs, "BIRTH_PLACE");
      bean.splEvtType = getStringFromResultSet(rs, "SPL_EVT_TYPE");
      bean.splEvtDt = getDateFromResultSet(rs, "SPL_EVT_DT");
      bean.childrenNames = getStringFromResultSet(rs, "CHILDREN_NAMES");
      bean.numOfChildren = getLongFromResultSet(rs, "NUM_OF_CHILDREN");
      bean.createdoffline = getStringFromResultSet(rs, "CREATEDOFFLINE");
      bean.country = getStringFromResultSet(rs, "COUNTRY");
      bean.postCode = getStringFromResultSet(rs, "POST_CODE");
      bean.state = getStringFromResultSet(rs, "STATE");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.addressLine1 = getStringFromResultSet(rs, "ADDRESS_LINE_1");
      bean.addressLine2 = getStringFromResultSet(rs, "ADDRESS_LINE_2");
      bean.unitName = getStringFromResultSet(rs, "UNIT_NAME");
      bean.addressType = getStringFromResultSet(rs, "ADDRESS_TYPE");
      bean.phoneType1 = getStringFromResultSet(rs, "PHONE_TYPE_1");
      bean.phoneNumber1 = getStringFromResultSet(rs, "PHONE_NUMBER_1");
      bean.phoneType2 = getStringFromResultSet(rs, "PHONE_TYPE_2");
      bean.phoneNumber2 = getStringFromResultSet(rs, "PHONE_NUMBER_2");
      bean.phoneType3 = getStringFromResultSet(rs, "PHONE_TYPE_3");
      bean.phoneNumber3 = getStringFromResultSet(rs, "PHONE_NUMBER_3");
      bean.useAsPrimaryAddr = getStringFromResultSet(rs, "USE_AS_PRIMARY_ADDR");
      bean.storeId = getStringFromResultSet(rs, "STORE_ID");
      bean.companyCd = getStringFromResultSet(rs, "COMPANY_CD");
      bean.brand = getStringFromResultSet(rs, "BRAND");
      bean.associateId = getStringFromResultSet(rs, "ASSOCIATE_ID");
      bean.createDate = getDateFromResultSet(rs, "CREATE_DATE");
      bean.comments = getStringFromResultSet(rs, "COMMENTS");
      bean.supplierPayment = getStringFromResultSet(rs, "SUPPLIER_PAYMENT");
      bean.bank = getStringFromResultSet(rs, "BANK");
      bean.crdtCrdNum1 = getStringFromResultSet(rs, "CRDT_CRD_NUM_1");
      bean.crdtCrdTyp1 = getStringFromResultSet(rs, "CRDT_CRD_TYP_1");
      bean.crdtCrdNum2 = getStringFromResultSet(rs, "CRDT_CRD_NUM_2");
      bean.crdtCrdTyp2 = getStringFromResultSet(rs, "CRDT_CRD_TYP_2");
      //Added for Privacy Mgmt Marketing and Master
      bean.flMaster = getStringFromResultSet(rs, "FL_MASTER");
      bean.flMarketing = getStringFromResultSet(rs, "FL_MARKETING");      
      bean.custIssueDate = getDateFromResultSet(rs, "CUST_ISSUE_DATE");
      list.add(bean);
    }
    return (ArmStgCustOutOracleBean[]) list.toArray(new ArmStgCustOutOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getRecordType(), Types.VARCHAR);
    addToList(list, this.getCustomerId(), Types.VARCHAR);
    addToList(list, this.getStatus(), Types.DECIMAL);
    addToList(list, this.getDataPopulationDt(), Types.TIMESTAMP);
    addToList(list, this.getExtractedDt(), Types.TIMESTAMP);
    addToList(list, this.getCustBarcode(), Types.VARCHAR);
    addToList(list, this.getTitle(), Types.VARCHAR);
    addToList(list, this.getFirstNm(), Types.VARCHAR);
    addToList(list, this.getLastNm(), Types.VARCHAR);
    addToList(list, this.getMiddleNm(), Types.VARCHAR);
    addToList(list, this.getSuffix(), Types.VARCHAR);
    addToList(list, this.getDbFirstNm(), Types.VARCHAR);
    addToList(list, this.getDbLastNm(), Types.VARCHAR);
    addToList(list, this.getGender(), Types.VARCHAR);
    addToList(list, this.getCustType(), Types.VARCHAR);
    addToList(list, this.getLoyaltyProgram(), Types.VARCHAR);
    addToList(list, this.getVipPercent(), Types.DECIMAL);
    addToList(list, this.getPrivacy(), Types.VARCHAR);
    addToList(list, this.getNoMail(), Types.VARCHAR);
    addToList(list, this.getNoEmail(), Types.VARCHAR);
    addToList(list, this.getNoCall(), Types.VARCHAR);
    addToList(list, this.getNoSms(), Types.VARCHAR);
    addToList(list, this.getEmail1(), Types.VARCHAR);
    addToList(list, this.getEmail2(), Types.VARCHAR);
    addToList(list, this.getVatNum(), Types.VARCHAR);
    addToList(list, this.getFiscalCd(), Types.VARCHAR);
    addToList(list, this.getIdType(), Types.VARCHAR);
    addToList(list, this.getDocNum(), Types.VARCHAR);
    addToList(list, this.getPlaceOfIssue(), Types.VARCHAR);
    addToList(list, this.getIssueDt(), Types.TIMESTAMP);
    addToList(list, this.getPayType(), Types.VARCHAR);
    addToList(list, this.getCompanyId(), Types.VARCHAR);
    addToList(list, this.getInterCmyCd(), Types.VARCHAR);
    addToList(list, this.getCustStatus(), Types.VARCHAR);
    addToList(list, this.getAccNum(), Types.VARCHAR);
    addToList(list, this.getBirthDay(), Types.VARCHAR);
    addToList(list, this.getBirthMonth(), Types.VARCHAR);
    addToList(list, this.getRealBirthday(), Types.TIMESTAMP);
    addToList(list, this.getAge(), Types.VARCHAR);
    addToList(list, this.getReferredBy(), Types.VARCHAR);
    addToList(list, this.getProfession(), Types.VARCHAR);
    addToList(list, this.getEducation(), Types.VARCHAR);
    addToList(list, this.getNotes1(), Types.VARCHAR);
    addToList(list, this.getNotes2(), Types.VARCHAR);
    addToList(list, this.getPntrFmlyNm(), Types.VARCHAR);
    addToList(list, this.getPntrNm(), Types.VARCHAR);
    addToList(list, this.getBirthPlace(), Types.VARCHAR);
    addToList(list, this.getSplEvtType(), Types.VARCHAR);
    addToList(list, this.getSplEvtDt(), Types.TIMESTAMP);
    addToList(list, this.getChildrenNames(), Types.VARCHAR);
    addToList(list, this.getNumOfChildren(), Types.DECIMAL);
    addToList(list, this.getCreatedoffline(), Types.VARCHAR);
    addToList(list, this.getCountry(), Types.VARCHAR);
    addToList(list, this.getPostCode(), Types.VARCHAR);
    addToList(list, this.getState(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getAddressLine1(), Types.VARCHAR);
    addToList(list, this.getAddressLine2(), Types.VARCHAR);
    addToList(list, this.getUnitName(), Types.VARCHAR);
    addToList(list, this.getAddressType(), Types.VARCHAR);
    addToList(list, this.getPhoneType1(), Types.VARCHAR);
    addToList(list, this.getPhoneNumber1(), Types.VARCHAR);
    addToList(list, this.getPhoneType2(), Types.VARCHAR);
    addToList(list, this.getPhoneNumber2(), Types.VARCHAR);
    addToList(list, this.getPhoneType3(), Types.VARCHAR);
    addToList(list, this.getPhoneNumber3(), Types.VARCHAR);
    addToList(list, this.getUseAsPrimaryAddr(), Types.VARCHAR);
    addToList(list, this.getStoreId(), Types.VARCHAR);
    addToList(list, this.getCompanyCd(), Types.VARCHAR);
    addToList(list, this.getBrand(), Types.VARCHAR);
    addToList(list, this.getAssociateId(), Types.VARCHAR);
    addToList(list, this.getCreateDate(), Types.TIMESTAMP);
    addToList(list, this.getComments(), Types.VARCHAR);
    addToList(list, this.getSupplierPayment(), Types.VARCHAR);
    addToList(list, this.getBank(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdNum1(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdTyp1(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdNum2(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdTyp2(), Types.VARCHAR);  
    //Added for Privacy Mgmt Marketing and Master
    addToList(list, this.getFlMaster(), Types.VARCHAR);
    addToList(list, this.getFlMarketing(), Types.VARCHAR);    
    addToList(list, this.getCustIssueDate(), Types.TIMESTAMP);    
    return list;
  }

}
