/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cs.armaniinterfaces.ArmStgStoreData;


/**
 * put your documentation comment here
 */
public class ArmStgStoreOracleDAO extends BaseOracleDAO
    implements ArmStgStoreDAO {
  private static String sSelectSQL = ArmStgStoreOracleBean.selectSql;
  private static String sInsertSQL = ArmStgStoreOracleBean.insertSql;
  private static String sUpdateSQL = ArmStgStoreOracleBean.updateSql;

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (ArmStgStoreData object) throws SQLException {
    throw  new SQLException("Insert Operation Not Allowed On this Object");
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update (ArmStgStoreData object) throws SQLException {
    ParametricStatement[] ps = getUpdateSql(object);
    execute(ps);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  public ParametricStatement[] getUpdateSql (ArmStgStoreData object) {
    List statements = new ArrayList();
    ArmStgStoreOracleBean armRMSStoreDataOracleBean = fromObjectToBean(object);
    List params = new ArrayList();
    params.add(object.getStgStatus());
    params.add(object.getStgErrorMessage());
    params.add(object.getStgEventID());
    statements.add(new ParametricStatement(armRMSStoreDataOracleBean.getUpdateSql(), params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  //
  //Non public methods begin here!
  //
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmStgStoreOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  private ArmStgStoreData[] fromBeansToObjects (BaseOracleBean[] beans) {
    // System.out.println("ArmStgStoreOracleDAO: fromBeansToObjects");
    ArmStgStoreData[] array = new ArmStgStoreData[beans.length];
    for (int i = 0; i < array.length; i++) {
      array[i] = fromBeanToObject(beans[i]);
    }
    return  array;
  }

  /////this method needs to customized
  private ArmStgStoreData fromBeanToObject (BaseOracleBean baseBean) {
    ArmStgStoreOracleBean bean = (ArmStgStoreOracleBean)baseBean;
    ArmStgStoreData object = new ArmStgStoreData();
    //object.setRowID(bean.getRowID());
    object.setTransactionType(bean.getTranType());
    object.setCompanyCode(bean.getCompanyCode());
    object.setFiscalCode(bean.getFiscalCode());
    object.setShopCode(bean.getShopCode());
    object.setShopDescription(bean.getShopDesc());
    object.setCompanyDescription(bean.getCompanyDesc());
    object.setCompOfficePhoneArea(bean.getCompOfficePhoneArea());
    object.setCompOfficePhoneNumber(bean.getCompOfficePhoneNumber());
    object.setCompAddressLine1(bean.getCompAddressLine1());
    object.setCompAddressLine2(bean.getCompAddressLine2());
    object.setCompCity(bean.getCompCity());
    object.setCompState(bean.getCompState());
    object.setCompPostalCode(bean.getCompPostalCode());
    object.setCompCountry(bean.getCompCountry());
    object.setOfficePhoneArea(bean.getOfficePhoneArea());
    object.setOfficePhoneNumber(bean.getOfficePhoneNumber());
    object.setAddressLine1(bean.getAddressLine1());
    object.setAddressLine2(bean.getAddressLine2());
    object.setCity(bean.getCity());
    object.setState(bean.getState());
    object.setPostalCode(bean.getPostalCode());
    object.setCountry(bean.getCountry());
    object.setCurrency(bean.getCurrency());
    object.setTaxRate(bean.getTaxRate());
    object.setCountryCode(bean.getCountryCode());
    object.setLanguageCode(bean.getLanguageCode());
    object.setBrandID(bean.getBrandId());
    object.setFrankingCompanyName(bean.getFrankingCompanyName());
    object.setFrankingCompanyAcNum(bean.getFrankingCompanyAcNum());
    object.setStgEventID(bean.getStgEventId());
    object.setStgStatus(bean.getStgStatus());
    object.setStgErrorMessage(bean.getStgErrorMessage());
    object.setLoadDate(bean.getStgLoadDate());
    object.setStgProcessDate(bean.getStgProcessDate());
    return  object;
  }

  /////these method needs to customized
  private ArmStgStoreOracleBean fromObjectToBean (ArmStgStoreData object) {
    ArmStgStoreOracleBean bean = new ArmStgStoreOracleBean();
    // bean.setRowID(object.getRowID());
    bean.setTranType(object.getTransactionType());
    bean.setCompanyCode(object.getCompanyCode());
    bean.setFiscalCode(object.getFiscalCode());
    bean.setShopDesc(object.getShopDescription());
    bean.setCompanyDesc(object.getCompanyDescription());
    bean.setCompOfficePhoneArea(object.getCompOfficePhoneArea());
    bean.setCompOfficePhoneNumber(object.getCompOfficePhoneNumber());
    bean.setCompAddressLine1(object.getCompAddressLine1());
    bean.setCompAddressLine2(object.getCompAddressLine2());
    bean.setCompCity(object.getCompCity());
    bean.setCompState(object.getCompState());
    bean.setCompPostalCode(object.getCompPostalCode());
    bean.setCompCountry(object.getCompCountry());
    bean.setShopCode(object.getShopCode());
    bean.setOfficePhoneArea(object.getOfficePhoneArea());
    bean.setOfficePhoneNumber(object.getOfficePhoneNumber());
    bean.setAddressLine1(object.getAddressLine1());
    bean.setAddressLine2(object.getAddressLine2());
    bean.setCity(object.getCity());
    bean.setState(object.getState());
    bean.setPostalCode(object.getPostalCode());
    bean.setCountry(object.getCountry());
    bean.setCurrency(object.getCurrency());
    bean.setTaxRate(object.getTaxRate());
    bean.setCountryCode(object.getCountryCode());
    bean.setLanguageCode(object.getLanguageCode());
    bean.setBrandId(object.getBrandID());
    bean.setFrankingCompanyName(object.getFrankingCompanyName());
    bean.setFrankingCompanyAcNum(object.getFrankingCompanyAcNum());
    bean.setStgEventId(object.getStgEventID());
    bean.setStgStatus(object.getStgStatus());
    bean.setStgErrorMessage(object.getStgErrorMessage());
    bean.setStgLoadDate(object.getLoadDate());
    bean.setStgProcessDate(object.getStgProcessDate());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  public ParametricStatement[] getErrorUpdateSql (ArmStgStoreData object) {
    List statements = new ArrayList();
    ArmStgStoreOracleBean armRMSStoreDataOracleBean = fromObjectToBean(object);
    List params = new ArrayList();
    params.add(armRMSStoreDataOracleBean.getStgErrorMessage());
    //  params.add(armRMSStoreDataOracleBean.getRowID());
    //   statements.add(new ParametricStatement(armRMSStoreDataOracleBean.getErrorUpdateSql(), params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @return 
   */
  public ArmStgStoreData[] getNewArmStgStoreData () {
    ArmStgStoreData armStgStoreData[];
    String sWhereSQL = "where NVL(ARM_STG_STORE.STG_STATUS, 0) IN (0, 2)";
    try {
      armStgStoreData = fromBeansToObjects(this.query(new ArmStgStoreOracleBean(), sWhereSQL, null));
      return  armStgStoreData;
    } catch (SQLException sQex) {
      sQex.printStackTrace();
      return  new ArmStgStoreData[0];
    }
  }
}



