/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
/**
 *
 *  Tax Free Data Access Object.<br>
 *  This object encapsulates all database access for ArmTaxFree.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Store_Code</td><td>ARM_TAX_FREE</td><td>STORE_CODE</td></tr>
 *    <tr><td>DetaxCode</td><td>ARM_TAX_FREE</td><td>DETAX_CODE</td></tr>
 *    <tr><td>DescCode</td><td>ARM_TAX_FREE</td><td>DESC_CODE</td></tr>
 *    <tr><td>Ratio</td><td>ARM_TAX_FREE</td><td>RATIO</td></tr>
 *    <tr><td>Start_date</td><td>ARM_TAX_FREE</td><td>START_DATE</td></tr>
 *    <tr><td>Net_Min_Val</td><td>ARM_TAX_FREE</td><td>NET_MIN_VALUE</td></tr>
 *    <tr><td>Gross_Min_Val</td><td>ARM_TAX_FREE</td><td>GROSS_MIN_VALUE</td></tr>
 *    <tr><td>Payment_Code</td><td>ARM_TAX_FREE</td><td>PAYMENT_CODE</td></tr>
 *    <tr><td>Min_Price</td><td>ARM_TAX_FREE</td><td>MIN_PRICE</td></tr>
 *    <tr><td>Max_Price</td><td>ARM_TAX_FREE</td><td>MAX_PRICE</td></tr>
 *    <tr><td>MinAmount</td><td>ARM_TAX_FREE</td><td>MIN_AMOUNT</td></tr>
 *    <tr><td>MaxAmount</td><td>ARM_TAX_FREE</td><td>MAX_AMOUNT</td></tr>
 *    <tr><td>Refund</td><td>ARM_TAX_FREE</td><td>REFUND_AMOUNT</td></tr>
 *    <tr><td>Percentage</td><td>ARM_TAX_FREE</td><td>REFUND_PERCENT</td></tr>
 *    <tr><td>Commission</td><td>ARM_TAX_FREE</td><td>COMMISSION</td></tr>
 *  @see ArmTaxFree
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmTaxFreeOracleBean
 *
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cs.fiscaldocument.TaxFree;


/**
 * put your documentation comment here
 */
public class ArmTaxFreeOracleDAO extends BaseOracleDAO
    implements ArmTaxFreeDAO {
  private static String selectSql = ArmTaxFreeOracleBean.selectSql;
  private static String insertSql = ArmTaxFreeOracleBean.insertSql;
  private static String updateSql = ArmTaxFreeOracleBean.updateSql + "where STORE_CODE = ?";
  private static String deleteSql = ArmTaxFreeOracleBean.deleteSql + "where STORE_CODE = ?";

  /**
   * To get Tax Free
   * @return Tax Free
   * @throws SQLException
   */
  public TaxFree[] selectAllTaxFree () throws SQLException {
    BaseOracleBean[] beans = null;
    List result = new ArrayList();
    List params = new ArrayList();
    beans = this.query(new ArmTaxFreeOracleBean(), selectSql, params);
    if (beans == null || beans.length == 0) {
      return  new TaxFree[0];
    }
    TaxFree[] taxFree = new TaxFree[beans.length];
    for (int i = 0; i < beans.length; i++) {
      taxFree[i] = fromBeanToObject((ArmTaxFreeOracleBean)beans[i]);
    }
    return  taxFree;
  }

  /**
   * To get Tax Free
   * @return Tax Free
   * @throws SQLException
   */
  public TaxFree[] selectTaxFreeForStore (String storeID) throws SQLException {
    BaseOracleBean[] beans = null;
    String whereClause = where(ArmTaxFreeOracleBean.COL_STORE_CODE);
    List result = new ArrayList();
    List params = new ArrayList();
    params.add(storeID);
    beans = this.query(new ArmTaxFreeOracleBean(), selectSql + " " + whereClause, params);
    if (beans == null || beans.length == 0) {
      return  new TaxFree[0];
    }
    TaxFree[] taxFree = new TaxFree[beans.length];
    for (int i = 0; i < beans.length; i++) {
      taxFree[i] = fromBeanToObject((ArmTaxFreeOracleBean)beans[i]);
    }
    return  taxFree;
  }

  /**
   * From Bean to Object
   * @param baseBean BaseOracleBean
   * @return TaxFree
   */
  private TaxFree fromBeanToObject (BaseOracleBean baseBean) {
    ArmTaxFreeOracleBean bean = (ArmTaxFreeOracleBean)baseBean;
    TaxFree object = new TaxFree();
    object.doSetStoreCode(bean.getStoreCode());
    object.doSetDetaxCode(bean.getDetaxCode());
    object.doSetDescCode(bean.getDescCode());
    object.doSetRatio(bean.getRatio());
    object.doSetStartDate(bean.getStartDate());
    object.doSetNetMinVal(bean.getNetMinValue());
    object.doSetGrossMinValue(bean.getGrossMinValue());
    object.doSetPaymentCode(bean.getPaymentCode());
    object.doSetMinPrice(bean.getMinPrice());
    object.doSetMaxPrice(bean.getMaxPrice());
    object.doSetMinAmount(bean.getMinAmount());
    object.doSetMaxAmount(bean.getMaxAmount());
    object.doSetRefundAmount(bean.getRefundAmount());
    object.doSetRefundPercentage(bean.getRefundPercent());
    object.doSetCommission(bean.getCommission());
    return  object;
  }

  /**
   * From Object to Bean
   * @param object TaxFree
   * @return ArmTaxFreeOracleBean
   */
  private ArmTaxFreeOracleBean fromObjectToBean (TaxFree object) {
    ArmTaxFreeOracleBean bean = new ArmTaxFreeOracleBean();
    bean.setStoreCode(object.getStoreCode());
    bean.setDetaxCode(object.getDetaxCode());
    bean.setDescCode(object.getDescCode());
    bean.setRatio(object.getRatio());
    bean.setStartDate(object.getStartDate());
    bean.setNetMinValue(object.getNetMinVal());
    bean.setGrossMinValue(object.getGrossMinValue());
    bean.setPaymentCode(object.getPaymentCode());
    bean.setMinPrice(object.getMinPrice());
    bean.setMaxPrice(object.getMaxPrice());
    bean.setMinAmount(object.getMinAmount());
    bean.setMaxAmount(object.getMaxAmount());
    bean.setRefundAmount(object.getRefundAmount());
    bean.setRefundPercent(object.getRefundPercentage());
    bean.setCommission(object.getCommission());
    return  bean;
  }
}



