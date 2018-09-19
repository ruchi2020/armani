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
 * This class is an object representation of the Arts database table RK_SHIP_REQ<BR>
 * Followings are the column of the table: <BR>
 *     SEQ_NUM(PK) -- NUMBER(3)<BR>
 *     FIRST_NAME -- VARCHAR2(100)<BR>
 *     LAST_NAME -- VARCHAR2(100)<BR>
 *     ADDRESS -- VARCHAR2(100)<BR>
 *     APARTMENT -- VARCHAR2(50)<BR>
 *     CITY -- VARCHAR2(50)<BR>
 *     STATE -- VARCHAR2(50)<BR>
 *     ZIP_CODE -- VARCHAR2(50)<BR>
 *     COUNTRY -- VARCHAR2(50)<BR>
 *     PHONE -- VARCHAR2(50)<BR>
 *     SPECIAL_INSTR -- VARCHAR2(200)<BR>
 *     GIFT_MESSAGE -- VARCHAR2(200)<BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     ADDRESS2 -- VARCHAR2(100)<BR>
 *     ADS_FORMAT -- VARCHAR2(40)<BR>
 *
 */
public class RkShipReqOracleBean extends BaseOracleBean {

  public RkShipReqOracleBean() {}

  public static String selectSql = "select SEQ_NUM, FIRST_NAME, LAST_NAME, ADDRESS, APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, PHONE, SPECIAL_INSTR, GIFT_MESSAGE, AI_TRN, ADDRESS2, ADS_FORMAT from RK_SHIP_REQ ";
  public static String insertSql = "insert into RK_SHIP_REQ (SEQ_NUM, FIRST_NAME, LAST_NAME, ADDRESS, APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, PHONE, SPECIAL_INSTR, GIFT_MESSAGE, AI_TRN, ADDRESS2, ADS_FORMAT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_SHIP_REQ set SEQ_NUM = ?, FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ?, APARTMENT = ?, CITY = ?, STATE = ?, ZIP_CODE = ?, COUNTRY = ?, PHONE = ?, SPECIAL_INSTR = ?, GIFT_MESSAGE = ?, AI_TRN = ?, ADDRESS2 = ?, ADS_FORMAT = ? ";
  public static String deleteSql = "delete from RK_SHIP_REQ ";

  public static String TABLE_NAME = "RK_SHIP_REQ";
  public static String COL_SEQ_NUM = "RK_SHIP_REQ.SEQ_NUM";
  public static String COL_FIRST_NAME = "RK_SHIP_REQ.FIRST_NAME";
  public static String COL_LAST_NAME = "RK_SHIP_REQ.LAST_NAME";
  public static String COL_ADDRESS = "RK_SHIP_REQ.ADDRESS";
  public static String COL_APARTMENT = "RK_SHIP_REQ.APARTMENT";
  public static String COL_CITY = "RK_SHIP_REQ.CITY";
  public static String COL_STATE = "RK_SHIP_REQ.STATE";
  public static String COL_ZIP_CODE = "RK_SHIP_REQ.ZIP_CODE";
  public static String COL_COUNTRY = "RK_SHIP_REQ.COUNTRY";
  public static String COL_PHONE = "RK_SHIP_REQ.PHONE";
  public static String COL_SPECIAL_INSTR = "RK_SHIP_REQ.SPECIAL_INSTR";
  public static String COL_GIFT_MESSAGE = "RK_SHIP_REQ.GIFT_MESSAGE";
  public static String COL_AI_TRN = "RK_SHIP_REQ.AI_TRN";
  public static String COL_ADDRESS2 = "RK_SHIP_REQ.ADDRESS2";
  public static String COL_ADS_FORMAT = "RK_SHIP_REQ.ADS_FORMAT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Long seqNum;
  private String firstName;
  private String lastName;
  private String address;
  private String apartment;
  private String city;
  private String state;
  private String zipCode;
  private String country;
  private String phone;
  private String specialInstr;
  private String giftMessage;
  private String aiTrn;
  private String address2;
  private String adsFormat;

  public Long getSeqNum() { return this.seqNum; }
  public void setSeqNum(Long seqNum) { this.seqNum = seqNum; }
  public void setSeqNum(long seqNum) { this.seqNum = new Long(seqNum); }
  public void setSeqNum(int seqNum) { this.seqNum = new Long((long)seqNum); }

  public String getFirstName() { return this.firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return this.lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getAddress() { return this.address; }
  public void setAddress(String address) { this.address = address; }

  public String getApartment() { return this.apartment; }
  public void setApartment(String apartment) { this.apartment = apartment; }

  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }

  public String getState() { return this.state; }
  public void setState(String state) { this.state = state; }

  public String getZipCode() { return this.zipCode; }
  public void setZipCode(String zipCode) { this.zipCode = zipCode; }

  public String getCountry() { return this.country; }
  public void setCountry(String country) { this.country = country; }

  public String getPhone() { return this.phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getSpecialInstr() { return this.specialInstr; }
  public void setSpecialInstr(String specialInstr) { this.specialInstr = specialInstr; }

  public String getGiftMessage() { return this.giftMessage; }
  public void setGiftMessage(String giftMessage) { this.giftMessage = giftMessage; }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getAddress2() { return this.address2; }
  public void setAddress2(String address2) { this.address2 = address2; }

  public String getAdsFormat() { return this.adsFormat; }
  public void setAdsFormat(String adsFormat) { this.adsFormat = adsFormat; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkShipReqOracleBean bean = new RkShipReqOracleBean();
      bean.seqNum = getLongFromResultSet(rs, "SEQ_NUM");
      bean.firstName = getStringFromResultSet(rs, "FIRST_NAME");
      bean.lastName = getStringFromResultSet(rs, "LAST_NAME");
      bean.address = getStringFromResultSet(rs, "ADDRESS");
      bean.apartment = getStringFromResultSet(rs, "APARTMENT");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.state = getStringFromResultSet(rs, "STATE");
      bean.zipCode = getStringFromResultSet(rs, "ZIP_CODE");
      bean.country = getStringFromResultSet(rs, "COUNTRY");
      bean.phone = getStringFromResultSet(rs, "PHONE");
      bean.specialInstr = getStringFromResultSet(rs, "SPECIAL_INSTR");
      bean.giftMessage = getStringFromResultSet(rs, "GIFT_MESSAGE");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.address2 = getStringFromResultSet(rs, "ADDRESS2");
      bean.adsFormat = getStringFromResultSet(rs, "ADS_FORMAT");
      list.add(bean);
    }
    return (RkShipReqOracleBean[]) list.toArray(new RkShipReqOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getSeqNum(), Types.DECIMAL);
    addToList(list, this.getFirstName(), Types.VARCHAR);
    addToList(list, this.getLastName(), Types.VARCHAR);
    addToList(list, this.getAddress(), Types.VARCHAR);
    addToList(list, this.getApartment(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getState(), Types.VARCHAR);
    addToList(list, this.getZipCode(), Types.VARCHAR);
    addToList(list, this.getCountry(), Types.VARCHAR);
    addToList(list, this.getPhone(), Types.VARCHAR);
    addToList(list, this.getSpecialInstr(), Types.VARCHAR);
    addToList(list, this.getGiftMessage(), Types.VARCHAR);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAddress2(), Types.VARCHAR);
    addToList(list, this.getAdsFormat(), Types.VARCHAR);
    return list;
  }

}
