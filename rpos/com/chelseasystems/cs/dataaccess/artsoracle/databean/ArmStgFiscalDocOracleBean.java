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
 * This class is an object representation of the Arts database table ARM_STG_FISCAL_DOC<BR>
 * Followings are the column of the table: <BR>
 *     TRANSACTION_ID(PK) -- VARCHAR2(20)<BR>
 *     DOC_NUM(PK) -- VARCHAR2(10)<BR>
 *     DOC_TYPE(PK) -- VARCHAR2(2)<BR>
 *     COMPANY_NAME -- VARCHAR2(30)<BR>
 *     COMPANY_NAME2 -- VARCHAR2(30)<BR>
 *     ADDRESS -- VARCHAR2(60)<BR>
 *     ADDRESS_2 -- VARCHAR2(60)<BR>
 *     CITY -- VARCHAR2(30)<BR>
 *     COUNTY -- VARCHAR2(30)<BR>
 *     ZIP_CODE -- VARCHAR2(10)<BR>
 *     COUNTRY -- VARCHAR2(30)<BR>
 *     PRE_VAT_AMOUNT -- NUMBER(9.2)<BR>
 *     POST_VAT_AMOUNT -- NUMBER(9.2)<BR>
 *     VAT_EXEMPT_CD -- VARCHAR2(20)<BR>
 *     FISCAL_CD -- VARCHAR2(20)<BR>
 *     SUPP_PAYMENT_TYPE -- VARCHAR2(20)<BR>
 *     VAT_NUM -- VARCHAR2(20)<BR>
 *     TAX_FREE_CD -- VARCHAR2(20)<BR>
 *     PAYMENT_TYPE -- VARCHAR2(20)<BR>
 *     ID_TYPE -- VARCHAR2(20)<BR>
 *     PLACE_OF_ISSUE -- VARCHAR2(20)<BR>
 *     DATE_OF_ISSUE -- DATE(7)<BR>
 *     REFUND_AMOUNT -- NUMBER(9.2)<BR>
 *     DESTINATION_CD -- VARCHAR2(10)<BR>
 *     SENDER -- VARCHAR2(30)<BR>
 *     SENDER_CD -- VARCHAR2(20)<BR>
 *     CARRIER_TYPE -- VARCHAR2(20)<BR>
 *     EXPEDITION_CD -- VARCHAR2(10)<BR>
 *     GOODS_NUM -- VARCHAR2(20)<BR>
 *     CARRIER_CD -- VARCHAR2(10)<BR>
 *     CARRIER_DESC -- VARCHAR2(50)<BR>
 *     PACKAGE_TYPE -- VARCHAR2(10)<BR>
 *     WEIGHT -- NUMBER(5.2)<BR>
 *     NOTE -- VARCHAR2(100)<BR>
 *     FISCAL_DATE -- DATE<BR>
 */
public class ArmStgFiscalDocOracleBean extends BaseOracleBean {

  public ArmStgFiscalDocOracleBean() {}

  public static String selectSql = "select TRANSACTION_ID, DOC_NUM, DOC_TYPE, COMPANY_NAME, COMPANY_NAME2, ADDRESS, ADDRESS_2, CITY, COUNTY, ZIP_CODE, COUNTRY, PRE_VAT_AMOUNT, POST_VAT_AMOUNT, VAT_EXEMPT_CD, FISCAL_CD, SUPP_PAYMENT_TYPE, VAT_NUM, TAX_FREE_CD, PAYMENT_TYPE, ID_TYPE, PLACE_OF_ISSUE, DATE_OF_ISSUE, REFUND_AMOUNT, DESTINATION_CD, SENDER, SENDER_CD, CARRIER_TYPE, EXPEDITION_CD, GOODS_NUM, CARRIER_CD, CARRIER_DESC, PACKAGE_TYPE, WEIGHT, NOTE, FISCAL_DATE  from ARM_STG_FISCAL_DOC ";
  public static String insertSql = "insert into ARM_STG_FISCAL_DOC (TRANSACTION_ID, DOC_NUM, DOC_TYPE, COMPANY_NAME, COMPANY_NAME2, ADDRESS, ADDRESS_2, CITY, COUNTY, ZIP_CODE, COUNTRY, PRE_VAT_AMOUNT, POST_VAT_AMOUNT, VAT_EXEMPT_CD, FISCAL_CD, SUPP_PAYMENT_TYPE, VAT_NUM, TAX_FREE_CD, PAYMENT_TYPE, ID_TYPE, PLACE_OF_ISSUE, DATE_OF_ISSUE, REFUND_AMOUNT, DESTINATION_CD, SENDER, SENDER_CD, CARRIER_TYPE, EXPEDITION_CD, GOODS_NUM, CARRIER_CD, CARRIER_DESC, PACKAGE_TYPE, WEIGHT, NOTE, FISCAL_DATE ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_STG_FISCAL_DOC set TRANSACTION_ID = ?, DOC_NUM = ?, DOC_TYPE = ?, COMPANY_NAME = ?, COMPANY_NAME2 = ?, ADDRESS = ?, ADDRESS_2 = ?, CITY = ?, COUNTY = ?, ZIP_CODE = ?, COUNTRY = ?, PRE_VAT_AMOUNT = ?, POST_VAT_AMOUNT = ?, VAT_EXEMPT_CD = ?, FISCAL_CD = ?, SUPP_PAYMENT_TYPE = ?, VAT_NUM = ?, TAX_FREE_CD = ?, PAYMENT_TYPE = ?, ID_TYPE = ?, PLACE_OF_ISSUE = ?, DATE_OF_ISSUE = ?, REFUND_AMOUNT = ?, DESTINATION_CD = ?, SENDER = ?, SENDER_CD = ?, CARRIER_TYPE = ?, EXPEDITION_CD = ?, GOODS_NUM = ?, CARRIER_CD = ?, CARRIER_DESC = ?, PACKAGE_TYPE = ?, WEIGHT = ?, NOTE = ?, FISCAL_DATE =? ";
  public static String deleteSql = "delete from ARM_STG_FISCAL_DOC ";

  public static String TABLE_NAME = "ARM_STG_FISCAL_DOC";
  public static String COL_TRANSACTION_ID = "ARM_STG_FISCAL_DOC.TRANSACTION_ID";
  public static String COL_DOC_NUM = "ARM_STG_FISCAL_DOC.DOC_NUM";
  public static String COL_DOC_TYPE = "ARM_STG_FISCAL_DOC.DOC_TYPE";
  public static String COL_COMPANY_NAME = "ARM_STG_FISCAL_DOC.COMPANY_NAME";
  public static String COL_COMPANY_NAME2 = "ARM_STG_FISCAL_DOC.COMPANY_NAME2";
  public static String COL_ADDRESS = "ARM_STG_FISCAL_DOC.ADDRESS";
  public static String COL_ADDRESS_2 = "ARM_STG_FISCAL_DOC.ADDRESS_2";
  public static String COL_CITY = "ARM_STG_FISCAL_DOC.CITY";
  public static String COL_COUNTY = "ARM_STG_FISCAL_DOC.COUNTY";
  public static String COL_ZIP_CODE = "ARM_STG_FISCAL_DOC.ZIP_CODE";
  public static String COL_COUNTRY = "ARM_STG_FISCAL_DOC.COUNTRY";
  public static String COL_PRE_VAT_AMOUNT = "ARM_STG_FISCAL_DOC.PRE_VAT_AMOUNT";
  public static String COL_POST_VAT_AMOUNT = "ARM_STG_FISCAL_DOC.POST_VAT_AMOUNT";
  public static String COL_VAT_EXEMPT_CD = "ARM_STG_FISCAL_DOC.VAT_EXEMPT_CD";
  public static String COL_FISCAL_CD = "ARM_STG_FISCAL_DOC.FISCAL_CD";
  public static String COL_SUPP_PAYMENT_TYPE = "ARM_STG_FISCAL_DOC.SUPP_PAYMENT_TYPE";
  public static String COL_VAT_NUM = "ARM_STG_FISCAL_DOC.VAT_NUM";
  public static String COL_TAX_FREE_CD = "ARM_STG_FISCAL_DOC.TAX_FREE_CD";
  public static String COL_PAYMENT_TYPE = "ARM_STG_FISCAL_DOC.PAYMENT_TYPE";
  public static String COL_ID_TYPE = "ARM_STG_FISCAL_DOC.ID_TYPE";
  public static String COL_PLACE_OF_ISSUE = "ARM_STG_FISCAL_DOC.PLACE_OF_ISSUE";
  public static String COL_DATE_OF_ISSUE = "ARM_STG_FISCAL_DOC.DATE_OF_ISSUE";
  public static String COL_REFUND_AMOUNT = "ARM_STG_FISCAL_DOC.REFUND_AMOUNT";
  public static String COL_DESTINATION_CD = "ARM_STG_FISCAL_DOC.DESTINATION_CD";
  public static String COL_SENDER = "ARM_STG_FISCAL_DOC.SENDER";
  public static String COL_SENDER_CD = "ARM_STG_FISCAL_DOC.SENDER_CD";
  public static String COL_CARRIER_TYPE = "ARM_STG_FISCAL_DOC.CARRIER_TYPE";
  public static String COL_EXPEDITION_CD = "ARM_STG_FISCAL_DOC.EXPEDITION_CD";
  public static String COL_GOODS_NUM = "ARM_STG_FISCAL_DOC.GOODS_NUM";
  public static String COL_CARRIER_CD = "ARM_STG_FISCAL_DOC.CARRIER_CD";
  public static String COL_CARRIER_DESC = "ARM_STG_FISCAL_DOC.CARRIER_DESC";
  public static String COL_PACKAGE_TYPE = "ARM_STG_FISCAL_DOC.PACKAGE_TYPE";
  public static String COL_WEIGHT = "ARM_STG_FISCAL_DOC.WEIGHT";
  public static String COL_NOTE = "ARM_STG_FISCAL_DOC.NOTE";
  public static String COL_FISCAL_DATE = "ARM_FISCAL_DOCUMENT.FISCAL_DATE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String transactionId;
  private String docNum;
  private String docType;
  private String companyName;
  private String companyName2;
  private String address;
  private String address2;
  private String city;
  private String county;
  private String zipCode;
  private String country;
  private Double preVatAmount;
  private Double postVatAmount;
  private String vatExemptCd;
  private String fiscalCd;
  private String suppPaymentType;
  private String vatNum;
  private String taxFreeCd;
  private String paymentType;
  private String idType;
  private String placeOfIssue;
  private Date dateOfIssue;
  private Double refundAmount;
  private String destinationCd;
  private String sender;
  private String senderCd;
  private String carrierType;
  private String expeditionCd;
  private String goodsNum;
  private String carrierCd;
  private String carrierDesc;
  private String packageType;
  private Double weight;
  private String note;
  private Date fiscalDate;

  public String getTransactionId() { return this.transactionId; }
  public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

  public String getDocNum() { return this.docNum; }
  public void setDocNum(String docNum) { this.docNum = docNum; }

  public String getDocType() { return this.docType; }
  public void setDocType(String docType) { this.docType = docType; }

  public String getCompanyName() { return this.companyName; }
  public void setCompanyName(String companyName) { this.companyName = companyName; }

  public String getCompanyName2() { return this.companyName2; }
  public void setCompanyName2(String companyName2) { this.companyName2 = companyName2; }

  public String getAddress() { return this.address; }
  public void setAddress(String address) { this.address = address; }

  public String getAddress2() { return this.address2; }
  public void setAddress2(String address2) { this.address2 = address2; }

  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }

  public String getCounty() { return this.county; }
  public void setCounty(String county) { this.county = county; }

  public String getZipCode() { return this.zipCode; }
  public void setZipCode(String zipCode) { this.zipCode = zipCode; }

  public String getCountry() { return this.country; }
  public void setCountry(String country) { this.country = country; }

  public Double getPreVatAmount() { return this.preVatAmount; }
  public void setPreVatAmount(Double preVatAmount) { this.preVatAmount = preVatAmount; }
  public void setPreVatAmount(double preVatAmount) { this.preVatAmount = new Double(preVatAmount); }

  public Double getPostVatAmount() { return this.postVatAmount; }
  public void setPostVatAmount(Double postVatAmount) { this.postVatAmount = postVatAmount; }
  public void setPostVatAmount(double postVatAmount) { this.postVatAmount = new Double(postVatAmount); }

  public String getVatExemptCd() { return this.vatExemptCd; }
  public void setVatExemptCd(String vatExemptCd) { this.vatExemptCd = vatExemptCd; }

  public String getFiscalCd() { return this.fiscalCd; }
  public void setFiscalCd(String fiscalCd) { this.fiscalCd = fiscalCd; }

  public String getSuppPaymentType() { return this.suppPaymentType; }
  public void setSuppPaymentType(String suppPaymentType) { this.suppPaymentType = suppPaymentType; }

  public String getVatNum() { return this.vatNum; }
  public void setVatNum(String vatNum) { this.vatNum = vatNum; }

  public String getTaxFreeCd() { return this.taxFreeCd; }
  public void setTaxFreeCd(String taxFreeCd) { this.taxFreeCd = taxFreeCd; }

  public String getPaymentType() { return this.paymentType; }
  public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

  public String getIdType() { return this.idType; }
  public void setIdType(String idType) { this.idType = idType; }

  public String getPlaceOfIssue() { return this.placeOfIssue; }
  public void setPlaceOfIssue(String placeOfIssue) { this.placeOfIssue = placeOfIssue; }

  public Date getDateOfIssue() { return this.dateOfIssue; }
  public void setDateOfIssue(Date dateOfIssue) { this.dateOfIssue = dateOfIssue; }

  public Double getRefundAmount() { return this.refundAmount; }
  public void setRefundAmount(Double refundAmount) { this.refundAmount = refundAmount; }
  public void setRefundAmount(double refundAmount) { this.refundAmount = new Double(refundAmount); }

  public String getDestinationCd() { return this.destinationCd; }
  public void setDestinationCd(String destinationCd) { this.destinationCd = destinationCd; }

  public String getSender() { return this.sender; }
  public void setSender(String sender) { this.sender = sender; }

  public String getSenderCd() { return this.senderCd; }
  public void setSenderCd(String senderCd) { this.senderCd = senderCd; }

  public String getCarrierType() { return this.carrierType; }
  public void setCarrierType(String carrierType) { this.carrierType = carrierType; }

  public String getExpeditionCd() { return this.expeditionCd; }
  public void setExpeditionCd(String expeditionCd) { this.expeditionCd = expeditionCd; }

  public String getGoodsNum() { return this.goodsNum; }
  public void setGoodsNum(String goodsNum) { this.goodsNum = goodsNum; }

  public String getCarrierCd() { return this.carrierCd; }
  public void setCarrierCd(String carrierCd) { this.carrierCd = carrierCd; }

  public String getCarrierDesc() { return this.carrierDesc; }
  public void setCarrierDesc(String carrierDesc) { this.carrierDesc = carrierDesc; }

  public String getPackageType() { return this.packageType; }
  public void setPackageType(String packageType) { this.packageType = packageType; }

  public Double getWeight() { return this.weight; }
  public void setWeight(Double weight) { this.weight = weight; }
  public void setWeight(double weight) { this.weight = new Double(weight); }

  public String getNote() { return this.note; }
  public void setNote(String note) { this.note = note; }

  public Date getFiscalDate() { return this.fiscalDate;}
  public void setFiscalDate(Date responseDate) { this.fiscalDate = fiscalDate; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmStgFiscalDocOracleBean bean = new ArmStgFiscalDocOracleBean();
      bean.transactionId = getStringFromResultSet(rs, "TRANSACTION_ID");
      bean.docNum = getStringFromResultSet(rs, "DOC_NUM");
      bean.docType = getStringFromResultSet(rs, "DOC_TYPE");
      bean.companyName = getStringFromResultSet(rs, "COMPANY_NAME");
      bean.companyName2 = getStringFromResultSet(rs, "COMPANY_NAME2");
      bean.address = getStringFromResultSet(rs, "ADDRESS");
      bean.address2 = getStringFromResultSet(rs, "ADDRESS_2");
      bean.city = getStringFromResultSet(rs, "CITY");
      bean.county = getStringFromResultSet(rs, "COUNTY");
      bean.zipCode = getStringFromResultSet(rs, "ZIP_CODE");
      bean.country = getStringFromResultSet(rs, "COUNTRY");
      bean.preVatAmount = getDoubleFromResultSet(rs, "PRE_VAT_AMOUNT");
      bean.postVatAmount = getDoubleFromResultSet(rs, "POST_VAT_AMOUNT");
      bean.vatExemptCd = getStringFromResultSet(rs, "VAT_EXEMPT_CD");
      bean.fiscalCd = getStringFromResultSet(rs, "FISCAL_CD");
      bean.suppPaymentType = getStringFromResultSet(rs, "SUPP_PAYMENT_TYPE");
      bean.vatNum = getStringFromResultSet(rs, "VAT_NUM");
      bean.taxFreeCd = getStringFromResultSet(rs, "TAX_FREE_CD");
      bean.paymentType = getStringFromResultSet(rs, "PAYMENT_TYPE");
      bean.idType = getStringFromResultSet(rs, "ID_TYPE");
      bean.placeOfIssue = getStringFromResultSet(rs, "PLACE_OF_ISSUE");
      bean.dateOfIssue = getDateFromResultSet(rs, "DATE_OF_ISSUE");
      bean.refundAmount = getDoubleFromResultSet(rs, "REFUND_AMOUNT");
      bean.destinationCd = getStringFromResultSet(rs, "DESTINATION_CD");
      bean.sender = getStringFromResultSet(rs, "SENDER");
      bean.senderCd = getStringFromResultSet(rs, "SENDER_CD");
      bean.carrierType = getStringFromResultSet(rs, "CARRIER_TYPE");
      bean.expeditionCd = getStringFromResultSet(rs, "EXPEDITION_CD");
      bean.goodsNum = getStringFromResultSet(rs, "GOODS_NUM");
      bean.carrierCd = getStringFromResultSet(rs, "CARRIER_CD");
      bean.carrierDesc = getStringFromResultSet(rs, "CARRIER_DESC");
      bean.packageType = getStringFromResultSet(rs, "PACKAGE_TYPE");
      bean.weight = getDoubleFromResultSet(rs, "WEIGHT");
      bean.note = getStringFromResultSet(rs, "NOTE");
      bean.fiscalDate = getDateFromResultSet(rs,"RESPONSE_DATE");
      list.add(bean);
    }
    return (ArmStgFiscalDocOracleBean[]) list.toArray(new ArmStgFiscalDocOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getTransactionId(), Types.VARCHAR);
    addToList(list, this.getDocNum(), Types.VARCHAR);
    addToList(list, this.getDocType(), Types.VARCHAR);
    addToList(list, this.getCompanyName(), Types.VARCHAR);
    addToList(list, this.getCompanyName2(), Types.VARCHAR);
    addToList(list, this.getAddress(), Types.VARCHAR);
    addToList(list, this.getAddress2(), Types.VARCHAR);
    addToList(list, this.getCity(), Types.VARCHAR);
    addToList(list, this.getCounty(), Types.VARCHAR);
    addToList(list, this.getZipCode(), Types.VARCHAR);
    addToList(list, this.getCountry(), Types.VARCHAR);
    addToList(list, this.getPreVatAmount(), Types.DECIMAL);
    addToList(list, this.getPostVatAmount(), Types.DECIMAL);
    addToList(list, this.getVatExemptCd(), Types.VARCHAR);
    addToList(list, this.getFiscalCd(), Types.VARCHAR);
    addToList(list, this.getSuppPaymentType(), Types.VARCHAR);
    addToList(list, this.getVatNum(), Types.VARCHAR);
    addToList(list, this.getTaxFreeCd(), Types.VARCHAR);
    addToList(list, this.getPaymentType(), Types.VARCHAR);
    addToList(list, this.getIdType(), Types.VARCHAR);
    addToList(list, this.getPlaceOfIssue(), Types.VARCHAR);
    addToList(list, this.getDateOfIssue(), Types.TIMESTAMP);
    addToList(list, this.getRefundAmount(), Types.DECIMAL);
    addToList(list, this.getDestinationCd(), Types.VARCHAR);
    addToList(list, this.getSender(), Types.VARCHAR);
    addToList(list, this.getSenderCd(), Types.VARCHAR);
    addToList(list, this.getCarrierType(), Types.VARCHAR);
    addToList(list, this.getExpeditionCd(), Types.VARCHAR);
    addToList(list, this.getGoodsNum(), Types.VARCHAR);
    addToList(list, this.getCarrierCd(), Types.VARCHAR);
    addToList(list, this.getCarrierDesc(), Types.VARCHAR);
    addToList(list, this.getPackageType(), Types.VARCHAR);
    addToList(list, this.getWeight(), Types.DECIMAL);
    addToList(list, this.getNote(), Types.VARCHAR);
    addToList(list, this.getFiscalDate(), Types.DATE);
    return list;
  }

}
