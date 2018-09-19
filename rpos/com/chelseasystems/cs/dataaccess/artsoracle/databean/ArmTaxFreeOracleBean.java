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
 * This class is an object representation of the Arts database table ARM_TAX_FREE<BR>
 * Followings are the column of the table: <BR>
 *     STORE_CODE(PK) -- VARCHAR2(20)<BR>
 *     DETAX_CODE(PK) -- VARCHAR2(50)<BR>
 *     DESC_CODE -- VARCHAR2(50)<BR>
 *     RATIO -- NUMBER(9.2)<BR>
 *     START_DATE(PK) -- DATE(7)<BR>
 *     NET_MIN_VALUE -- VARCHAR2(75)<BR>
 *     GROSS_MIN_VALUE -- VARCHAR2(75)<BR>
 *     PAYMENT_CODE -- VARCHAR2(20)<BR>
 *     MIN_PRICE -- VARCHAR2(75)<BR>
 *     MAX_PRICE -- VARCHAR2(75)<BR>
 *     MIN_AMOUNT -- VARCHAR2(75)<BR>
 *     MAX_AMOUNT -- VARCHAR2(75)<BR>
 *     REFUND_AMOUNT -- VARCHAR2(75)<BR>
 *     REFUND_PERCENT -- VARCHAR2(10)<BR>
 *     COMMISSION -- VARCHAR2(20)<BR>
 *
 */
public class ArmTaxFreeOracleBean extends BaseOracleBean {

  public ArmTaxFreeOracleBean() {}

  public static String selectSql = "select STORE_CODE, DETAX_CODE, DESC_CODE, RATIO, START_DATE, NET_MIN_VALUE, GROSS_MIN_VALUE, PAYMENT_CODE, MIN_PRICE, MAX_PRICE, MIN_AMOUNT, MAX_AMOUNT, REFUND_AMOUNT, REFUND_PERCENT, COMMISSION from ARM_TAX_FREE ";
  public static String insertSql = "insert into ARM_TAX_FREE (STORE_CODE, DETAX_CODE, DESC_CODE, RATIO, START_DATE, NET_MIN_VALUE, GROSS_MIN_VALUE, PAYMENT_CODE, MIN_PRICE, MAX_PRICE, MIN_AMOUNT, MAX_AMOUNT, REFUND_AMOUNT, REFUND_PERCENT, COMMISSION) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_TAX_FREE set STORE_CODE = ?, DETAX_CODE = ?, DESC_CODE = ?, RATIO = ?, START_DATE = ?, NET_MIN_VALUE = ?, GROSS_MIN_VALUE = ?, PAYMENT_CODE = ?, MIN_PRICE = ?, MAX_PRICE = ?, MIN_AMOUNT = ?, MAX_AMOUNT = ?, REFUND_AMOUNT = ?, REFUND_PERCENT = ?, COMMISSION = ? ";
  public static String deleteSql = "delete from ARM_TAX_FREE ";

  public static String TABLE_NAME = "ARM_TAX_FREE";
  public static String COL_STORE_CODE = "ARM_TAX_FREE.STORE_CODE";
  public static String COL_DETAX_CODE = "ARM_TAX_FREE.DETAX_CODE";
  public static String COL_DESC_CODE = "ARM_TAX_FREE.DESC_CODE";
  public static String COL_RATIO = "ARM_TAX_FREE.RATIO";
  public static String COL_START_DATE = "ARM_TAX_FREE.START_DATE";
  public static String COL_NET_MIN_VALUE = "ARM_TAX_FREE.NET_MIN_VALUE";
  public static String COL_GROSS_MIN_VALUE = "ARM_TAX_FREE.GROSS_MIN_VALUE";
  public static String COL_PAYMENT_CODE = "ARM_TAX_FREE.PAYMENT_CODE";
  public static String COL_MIN_PRICE = "ARM_TAX_FREE.MIN_PRICE";
  public static String COL_MAX_PRICE = "ARM_TAX_FREE.MAX_PRICE";
  public static String COL_MIN_AMOUNT = "ARM_TAX_FREE.MIN_AMOUNT";
  public static String COL_MAX_AMOUNT = "ARM_TAX_FREE.MAX_AMOUNT";
  public static String COL_REFUND_AMOUNT = "ARM_TAX_FREE.REFUND_AMOUNT";
  public static String COL_REFUND_PERCENT = "ARM_TAX_FREE.REFUND_PERCENT";
  public static String COL_COMMISSION = "ARM_TAX_FREE.COMMISSION";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String storeCode;
  private String detaxCode;
  private String descCode;
  private Double ratio;
  private Date startDate;
  private ArmCurrency netMinValue;
  private ArmCurrency grossMinValue;
  private String paymentCode;
  private ArmCurrency minPrice;
  private ArmCurrency maxPrice;
  private ArmCurrency minAmount;
  private ArmCurrency maxAmount;
  private ArmCurrency refundAmount;
  private String refundPercent;
  private String commission;

  public String getStoreCode() { return this.storeCode; }
  public void setStoreCode(String storeCode) { this.storeCode = storeCode; }

  public String getDetaxCode() { return this.detaxCode; }
  public void setDetaxCode(String detaxCode) { this.detaxCode = detaxCode; }

  public String getDescCode() { return this.descCode; }
  public void setDescCode(String descCode) { this.descCode = descCode; }

  public Double getRatio() { return this.ratio; }
  public void setRatio(Double ratio) { this.ratio = ratio; }
  public void setRatio(double ratio) { this.ratio = new Double(ratio); }

  public Date getStartDate() { return this.startDate; }
  public void setStartDate(Date startDate) { this.startDate = startDate; }

  public ArmCurrency getNetMinValue() { return this.netMinValue; }
  public void setNetMinValue(ArmCurrency netMinValue) { this.netMinValue = netMinValue; }

  public ArmCurrency getGrossMinValue() { return this.grossMinValue; }
  public void setGrossMinValue(ArmCurrency grossMinValue) { this.grossMinValue = grossMinValue; }

  public String getPaymentCode() { return this.paymentCode; }
  public void setPaymentCode(String paymentCode) { this.paymentCode = paymentCode; }

  public ArmCurrency getMinPrice() { return this.minPrice; }
  public void setMinPrice(ArmCurrency minPrice) { this.minPrice = minPrice; }

  public ArmCurrency getMaxPrice() { return this.maxPrice; }
  public void setMaxPrice(ArmCurrency maxPrice) { this.maxPrice = maxPrice; }

  public ArmCurrency getMinAmount() { return this.minAmount; }
  public void setMinAmount(ArmCurrency minAmount) { this.minAmount = minAmount; }

  public ArmCurrency getMaxAmount() { return this.maxAmount; }
  public void setMaxAmount(ArmCurrency maxAmount) { this.maxAmount = maxAmount; }

  public ArmCurrency getRefundAmount() { return this.refundAmount; }
  public void setRefundAmount(ArmCurrency refundAmount) { this.refundAmount = refundAmount; }

  public String getRefundPercent() { return this.refundPercent; }
  public void setRefundPercent(String refundPercent) { this.refundPercent = refundPercent; }

  public String getCommission() { return this.commission; }
  public void setCommission(String commission) { this.commission = commission; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmTaxFreeOracleBean bean = new ArmTaxFreeOracleBean();
      bean.storeCode = getStringFromResultSet(rs, "STORE_CODE");
      bean.detaxCode = getStringFromResultSet(rs, "DETAX_CODE");
      bean.descCode = getStringFromResultSet(rs, "DESC_CODE");
      bean.ratio = getDoubleFromResultSet(rs, "RATIO");
      bean.startDate = getDateFromResultSet(rs, "START_DATE");
      bean.netMinValue = getCurrencyFromResultSet(rs, "NET_MIN_VALUE");
      bean.grossMinValue = getCurrencyFromResultSet(rs, "GROSS_MIN_VALUE");
      bean.paymentCode = getStringFromResultSet(rs, "PAYMENT_CODE");
      bean.minPrice = getCurrencyFromResultSet(rs, "MIN_PRICE");
      bean.maxPrice = getCurrencyFromResultSet(rs, "MAX_PRICE");
      bean.minAmount = getCurrencyFromResultSet(rs, "MIN_AMOUNT");
      bean.maxAmount = getCurrencyFromResultSet(rs, "MAX_AMOUNT");
      bean.refundAmount = getCurrencyFromResultSet(rs, "REFUND_AMOUNT");
      bean.refundPercent = getStringFromResultSet(rs, "REFUND_PERCENT");
      bean.commission = getStringFromResultSet(rs, "COMMISSION");
      list.add(bean);
    }
    return (ArmTaxFreeOracleBean[]) list.toArray(new ArmTaxFreeOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getStoreCode(), Types.VARCHAR);
    addToList(list, this.getDetaxCode(), Types.VARCHAR);
    addToList(list, this.getDescCode(), Types.VARCHAR);
    addToList(list, this.getRatio(), Types.DECIMAL);
    addToList(list, this.getStartDate(), Types.TIMESTAMP);
    addToList(list, this.getNetMinValue(), Types.VARCHAR);
    addToList(list, this.getGrossMinValue(), Types.VARCHAR);
    addToList(list, this.getPaymentCode(), Types.VARCHAR);
    addToList(list, this.getMinPrice(), Types.VARCHAR);
    addToList(list, this.getMaxPrice(), Types.VARCHAR);
    addToList(list, this.getMinAmount(), Types.VARCHAR);
    addToList(list, this.getMaxAmount(), Types.VARCHAR);
    addToList(list, this.getRefundAmount(), Types.VARCHAR);
    addToList(list, this.getRefundPercent(), Types.VARCHAR);
    addToList(list, this.getCommission(), Types.VARCHAR);
    return list;
  }

}
