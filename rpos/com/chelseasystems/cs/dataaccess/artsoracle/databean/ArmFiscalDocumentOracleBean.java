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
 * This class is an object representation of the Arts database table ARM_FISCAL_DOCUMENT<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN -- VARCHAR2(128)<BR>
 *     DOC_NUM -- VARCHAR2(20)<BR>
 *     TY_DOC -- VARCHAR2(10)<BR>
 *     REFUND_AMOUNT -- VARCHAR2(50)<BR>
 *     VAT_NUM -- VARCHAR2(50)<BR>
 *     DESTINATION -- VARCHAR2(50)<BR>
 *     TY_PKG -- VARCHAR2(50)<BR>
 *     NOTE -- VARCHAR2(100)<BR>
 *     COMPANY_NAME -- VARCHAR2(50)<BR>
 *     COMPANY_NAME2 -- VARCHAR2(50)<BR>
 *     ADDRESS_LINE_1 -- VARCHAR2(50)<BR>
 *     ADDRESS_LINE_2 -- VARCHAR2(50)<BR>
 *     CITY -- VARCHAR2(50)<BR>
 *     COUNTY -- VARCHAR2(50)<BR>
 *     ZIP_CODE -- VARCHAR2(10)<BR>
 *     STATE -- VARCHAR2(50)<BR>
 *     COUNTRY -- VARCHAR2(50)<BR>
 *     CD_DEST -- VARCHAR2(10)<BR>
 *     SENDER -- VARCHAR2(10)<BR>
 *     CD_SENDER -- VARCHAR2(10)<BR>
 *     TY_CARRIER -- VARCHAR2(10)<BR>
 *     CD_EXPEDITION -- VARCHAR2(10)<BR>
 *     GOODS_NUM -- VARCHAR2(50)<BR>
 *     CD_CARRIER -- VARCHAR2(10)<BR>
 *     DE_CARRIER -- VARCHAR2(50)<BR>
 *     TY_PACKAGE -- VARCHAR2(10)<BR>
 *     TY_PAYMENT -- VARCHAR2(10)<BR>
 *     WEIGHT -- NUMBER(5.2)<BR>
 *     FISCAL_DATE -- DATE<BR>
 *
 */
public class ArmFiscalDocumentOracleBean extends BaseOracleBean {

  public ArmFiscalDocumentOracleBean() {}

  public static String selectSql = "select AI_TRN, DOC_NUM, TY_DOC, REFUND_AMOUNT, VAT_NUM, DESTINATION, TY_PKG, NOTE, COMPANY_NAME, COMPANY_NAME2, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, COUNTY, ZIP_CODE, STATE, COUNTRY, CD_DEST, SENDER, CD_SENDER, TY_CARRIER, CD_EXPEDITION, GOODS_NUM, CD_CARRIER, DE_CARRIER, TY_PACKAGE, TY_PAYMENT, WEIGHT, FISCAL_DATE from ARM_FISCAL_DOCUMENT ";
  public static String insertSql = "insert into ARM_FISCAL_DOCUMENT (AI_TRN, DOC_NUM, TY_DOC, REFUND_AMOUNT, VAT_NUM, DESTINATION, TY_PKG, NOTE, COMPANY_NAME, COMPANY_NAME2, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, COUNTY, ZIP_CODE, STATE, COUNTRY, CD_DEST, SENDER, CD_SENDER, TY_CARRIER, CD_EXPEDITION, GOODS_NUM, CD_CARRIER, DE_CARRIER, TY_PACKAGE, TY_PAYMENT, WEIGHT, FISCAL_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_FISCAL_DOCUMENT set AI_TRN = ?, DOC_NUM = ?, TY_DOC = ?, REFUND_AMOUNT = ?, VAT_NUM = ?, DESTINATION = ?, TY_PKG = ?, NOTE = ?, COMPANY_NAME = ?, COMPANY_NAME2 = ?, ADDRESS_LINE_1 = ?, ADDRESS_LINE_2 = ?, CITY = ?, COUNTY = ?, ZIP_CODE = ?, STATE = ?, COUNTRY = ?, CD_DEST = ?, SENDER = ?, CD_SENDER = ?, TY_CARRIER = ?, CD_EXPEDITION = ?, GOODS_NUM = ?, CD_CARRIER = ?, DE_CARRIER = ?, TY_PACKAGE = ?, TY_PAYMENT = ?, WEIGHT = ?, FISCAL_DATE = ?";
  public static String deleteSql = "delete from ARM_FISCAL_DOCUMENT ";

  public static String TABLE_NAME = "ARM_FISCAL_DOCUMENT";
  public static String COL_AI_TRN = "ARM_FISCAL_DOCUMENT.AI_TRN";
  public static String COL_DOC_NUM = "ARM_FISCAL_DOCUMENT.DOC_NUM";
  public static String COL_TY_DOC = "ARM_FISCAL_DOCUMENT.TY_DOC";
  public static String COL_REFUND_AMOUNT = "ARM_FISCAL_DOCUMENT.REFUND_AMOUNT";
  public static String COL_VAT_NUM = "ARM_FISCAL_DOCUMENT.VAT_NUM";
  public static String COL_DESTINATION = "ARM_FISCAL_DOCUMENT.DESTINATION";
  public static String COL_TY_PKG = "ARM_FISCAL_DOCUMENT.TY_PKG";
  public static String COL_NOTE = "ARM_FISCAL_DOCUMENT.NOTE";
  public static String COL_COMPANY_NAME = "ARM_FISCAL_DOCUMENT.COMPANY_NAME";
  public static String COL_COMPANY_NAME2 = "ARM_FISCAL_DOCUMENT.COMPANY_NAME2";
  public static String COL_ADDRESS_LINE_1 = "ARM_FISCAL_DOCUMENT.ADDRESS_LINE_1";
  public static String COL_ADDRESS_LINE_2 = "ARM_FISCAL_DOCUMENT.ADDRESS_LINE_2";
  public static String COL_CITY = "ARM_FISCAL_DOCUMENT.CITY";
  public static String COL_COUNTY = "ARM_FISCAL_DOCUMENT.COUNTY";
  public static String COL_ZIP_CODE = "ARM_FISCAL_DOCUMENT.ZIP_CODE";
  public static String COL_STATE = "ARM_FISCAL_DOCUMENT.STATE";
  public static String COL_COUNTRY = "ARM_FISCAL_DOCUMENT.COUNTRY";
  public static String COL_CD_DEST = "ARM_FISCAL_DOCUMENT.CD_DEST";
  public static String COL_SENDER = "ARM_FISCAL_DOCUMENT.SENDER";
  public static String COL_CD_SENDER = "ARM_FISCAL_DOCUMENT.CD_SENDER";
  public static String COL_TY_CARRIER = "ARM_FISCAL_DOCUMENT.TY_CARRIER";
  public static String COL_CD_EXPEDITION = "ARM_FISCAL_DOCUMENT.CD_EXPEDITION";
  public static String COL_GOODS_NUM = "ARM_FISCAL_DOCUMENT.GOODS_NUM";
  public static String COL_CD_CARRIER = "ARM_FISCAL_DOCUMENT.CD_CARRIER";
  public static String COL_DE_CARRIER = "ARM_FISCAL_DOCUMENT.DE_CARRIER";
  public static String COL_TY_PACKAGE = "ARM_FISCAL_DOCUMENT.TY_PACKAGE";
  public static String COL_TY_PAYMENT = "ARM_FISCAL_DOCUMENT.TY_PAYMENT";
  public static String COL_WEIGHT = "ARM_FISCAL_DOCUMENT.WEIGHT";
  public static String COL_FISCAL_DATE = "ARM_FISCAL_DOCUMENT.FISCAL_DATE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private String docNum;
  private String tyDoc;
  private ArmCurrency refundAmount;
  private String vatNum;
  private String destination;
  private String tyPkg;
  private String note;
  private String companyName;
  private String companyName2;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String county;
  private String zipCode;
  private String state;
  private String country;
  private String cdDest;
  private String sender;
  private String cdSender;
  private String tyCarrier;
  private String cdExpedition;
  private String goodsNum;
  private String cdCarrier;
  private String deCarrier;
  private String tyPackage;
  private String tyPayment;
  private Double weight;
  private Date fiscalDate;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getDocNum() { return this.docNum; }
  public void setDocNum(String docNum) { this.docNum = docNum; }

  public String getTyDoc() { return this.tyDoc; }
  public void setTyDoc(String tyDoc) { this.tyDoc = tyDoc; }

  public ArmCurrency getRefundAmount() { return this.refundAmount; }
  public void setRefundAmount(ArmCurrency refundAmount) { this.refundAmount = refundAmount; }

  public String getVatNum() { return this.vatNum; }
  public void setVatNum(String vatNum) { this.vatNum = vatNum; }

  public String getDestination() { return this.destination; }
  public void setDestination(String destination) { this.destination = destination; }

  public String getTyPkg() { return this.tyPkg; }
  public void setTyPkg(String tyPkg) { this.tyPkg = tyPkg; }

  public String getNote() { return this.note; }
  public void setNote(String note) { this.note = note; }

  public String getCompanyName() { return this.companyName; }
  public void setCompanyName(String companyName) { this.companyName = companyName; }

  public String getCompanyName2() { return this.companyName2; }
  public void setCompanyName2(String companyName2) { this.companyName2 = companyName2; }

  public String getAddressLine1() { return this.addressLine1; }
  public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

  public String getAddressLine2() { return this.addressLine2; }
  public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }

  public String getCounty() { return this.county; }
  public void setCounty(String county) { this.county = county; }

  public String getZipCode() { return this.zipCode; }
  public void setZipCode(String zipCode) { this.zipCode = zipCode; }

  public String getState() { return this.state; }
  public void setState(String state) { this.state = state; }

  public String getCountry() { return this.country; }
  public void setCountry(String country) { this.country = country; }

  public String getCdDest() { return this.cdDest; }
  public void setCdDest(String cdDest) { this.cdDest = cdDest; }

  public String getSender() { return this.sender; }
  public void setSender(String sender) { this.sender = sender; }

  public String getCdSender() { return this.cdSender; }
  public void setCdSender(String cdSender) { this.cdSender = cdSender; }

  public String getTyCarrier() { return this.tyCarrier; }
  public void setTyCarrier(String tyCarrier) { this.tyCarrier = tyCarrier; }

  public String getCdExpedition() { return this.cdExpedition; }
  public void setCdExpedition(String cdExpedition) { this.cdExpedition = cdExpedition; }

  public String getGoodsNum() { return this.goodsNum; }
  public void setGoodsNum(String goodsNum) { this.goodsNum = goodsNum; }

  public String getCdCarrier() { return this.cdCarrier; }
  public void setCdCarrier(String cdCarrier) { this.cdCarrier = cdCarrier; }

  public String getDeCarrier() { return this.deCarrier; }
  public void setDeCarrier(String deCarrier) { this.deCarrier = deCarrier; }

  public String getTyPackage() { return this.tyPackage; }
  public void setTyPackage(String tyPackage) { this.tyPackage = tyPackage; }

  public String getTyPayment() { return this.tyPayment; }
  public void setTyPayment(String tyPayment) { this.tyPayment = tyPayment; }

  public Double getWeight() { return this.weight; }
  public void setWeight(Double weight) { this.weight = weight; }
  public void setWeight(double weight) { this.weight = new Double(weight); }

  public Date getFiscalDate() { return this.fiscalDate;}
  public void setFiscalDate(Date fiscalDate) { this.fiscalDate = fiscalDate; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmFiscalDocumentOracleBean bean = new ArmFiscalDocumentOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.docNum = getStringFromResultSet(rs, "DOC_NUM");
      bean.tyDoc = getStringFromResultSet(rs, "TY_DOC");
      bean.refundAmount = getCurrencyFromResultSet(rs, "REFUND_AMOUNT");
      bean.vatNum = getStringFromResultSet(rs, "VAT_NUM");
      bean.destination = getStringFromResultSet(rs, "DESTINATION");
      bean.tyPkg = getStringFromResultSet(rs, "TY_PKG");
      bean.note = getStringFromResultSet(rs, "NOTE");
      bean.companyName = getStringFromResultSet(rs, "COMPANY_NAME");
      bean.companyName2 = getStringFromResultSet(rs, "COMPANY_NAME2");
      bean.addressLine1 = getStringFromResultSet(rs, "ADDRESS_LINE_1");
      bean.addressLine2 = getStringFromResultSet(rs, "ADDRESS_LINE_2");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.county = getStringFromResultSet(rs, "COUNTY");
      bean.zipCode = getStringFromResultSet(rs, "ZIP_CODE");
      bean.state = getStringFromResultSet(rs, "STATE");
      bean.country = getStringFromResultSet(rs, "COUNTRY");
      bean.cdDest = getStringFromResultSet(rs, "CD_DEST");
      bean.sender = getStringFromResultSet(rs, "SENDER");
      bean.cdSender = getStringFromResultSet(rs, "CD_SENDER");
      bean.tyCarrier = getStringFromResultSet(rs, "TY_CARRIER");
      bean.cdExpedition = getStringFromResultSet(rs, "CD_EXPEDITION");
      bean.goodsNum = getStringFromResultSet(rs, "GOODS_NUM");
      bean.cdCarrier = getStringFromResultSet(rs, "CD_CARRIER");
      bean.deCarrier = getStringFromResultSet(rs, "DE_CARRIER");
      bean.tyPackage = getStringFromResultSet(rs, "TY_PACKAGE");
      bean.tyPayment = getStringFromResultSet(rs, "TY_PAYMENT");
      bean.weight = getDoubleFromResultSet(rs, "WEIGHT");
      bean.fiscalDate = getDateFromResultSet(rs,"FISCAL_DATE");
      list.add(bean);
    }
    return (ArmFiscalDocumentOracleBean[]) list.toArray(new ArmFiscalDocumentOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getDocNum(), Types.VARCHAR);
    addToList(list, this.getTyDoc(), Types.VARCHAR);
    addToList(list, this.getRefundAmount(), Types.VARCHAR);
    addToList(list, this.getVatNum(), Types.VARCHAR);
    addToList(list, this.getDestination(), Types.VARCHAR);
    addToList(list, this.getTyPkg(), Types.VARCHAR);
    addToList(list, this.getNote(), Types.VARCHAR);
    addToList(list, this.getCompanyName(), Types.VARCHAR);
    addToList(list, this.getCompanyName2(), Types.VARCHAR);
    addToList(list, this.getAddressLine1(), Types.VARCHAR);
    addToList(list, this.getAddressLine2(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getCounty(), Types.VARCHAR);
    addToList(list, this.getZipCode(), Types.VARCHAR);
    addToList(list, this.getState(), Types.VARCHAR);
    addToList(list, this.getCountry(), Types.VARCHAR);
    addToList(list, this.getCdDest(), Types.VARCHAR);
    addToList(list, this.getSender(), Types.VARCHAR);
    addToList(list, this.getCdSender(), Types.VARCHAR);
    addToList(list, this.getTyCarrier(), Types.VARCHAR);
    addToList(list, this.getCdExpedition(), Types.VARCHAR);
    addToList(list, this.getGoodsNum(), Types.VARCHAR);
    addToList(list, this.getCdCarrier(), Types.VARCHAR);
    addToList(list, this.getDeCarrier(), Types.VARCHAR);
    addToList(list, this.getTyPackage(), Types.VARCHAR);
    addToList(list, this.getTyPayment(), Types.VARCHAR);
    addToList(list, this.getWeight(), Types.DECIMAL);
    addToList(list, this.getFiscalDate(), Types.DATE);
    return list;
  }

}
