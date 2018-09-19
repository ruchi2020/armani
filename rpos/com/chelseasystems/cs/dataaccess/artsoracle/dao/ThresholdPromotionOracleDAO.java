/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2003, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cr.pricing.*;
import  com.chelseasystems.cs.pricing.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.store.CMSStore;
import  com.chelseasystems.cr.currency.*;
import  java.sql.*;
import  java.util.*;


/**
 *
 *  ThresholdPromotion Data Access Object.<br>
 *  This object encapsulates all database access for ThresholdPromotion.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *  </table>
 *
 *  @see ThresholdPromotion
 *  @see CMSThresholdPromotion
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkThPrmOracleBean
 *
 */
public class ThresholdPromotionOracleDAO extends BaseOracleDAO
    implements ThresholdPromotionDAO {

  /**
   * put your documentation comment here
   * @param object
   * @param currencyCode
   * @return 
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL (CMSThresholdPromotion object, String currencyCode) throws SQLException {
    if (!(object instanceof CMSThresholdPromotion))
      return  new ParametricStatement[0];
    List statements = new ArrayList();
    statements.add(new ParametricStatement(RkThPrmOracleBean.insertSql, this.toRkThPrmBean(object, currencyCode).toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (ThresholdPromotion object) throws SQLException {
    CMSThresholdPromotion cmsobject = (CMSThresholdPromotion)object;
    this.execute(getInsertSQL(cmsobject, new CMSStore(cmsobject.getIdStrRt()).getCurrencyType().getCode()));
  }

  /**
   * put your documentation comment here
   * @param object
   * @param currencyCode
   * @exception SQLException
   */
  public void insert (ThresholdPromotion object, String currencyCode) throws SQLException {
    this.execute(getInsertSQL((CMSThresholdPromotion)object, currencyCode));
  }

  /**
   * put your documentation comment here
   * @param store
   * @return 
   * @exception SQLException
   */
  public ThresholdPromotion[] selectByStore (Store store) throws SQLException {
    String currencyCode = store.getCurrencyType().getCode();
    BaseOracleBean[] beans = this.query(new RkThPrmOracleBean(), where(RkThPrmOracleBean.COL_CURRENCY_CODE), currencyCode);
    return  this.toObjects(beans);
  }

  /**
   * put your documentation comment here
   * @param id
   * @param currencyCode
   * @return 
   * @exception SQLException
   */
  public ThresholdPromotion selectById (String id, String currencyCode) throws SQLException {
    List params = new ArrayList();
    params.add(id);
    params.add(currencyCode);
    BaseOracleBean[] beans = this.query(new RkThPrmOracleBean(), where(RkThPrmOracleBean.COL_ID, RkThPrmOracleBean.COL_CURRENCY_CODE), params);
    if (beans == null || beans.length == 0)
      return  null;
    return  this.toObject((RkThPrmOracleBean)beans[0]);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  public CMSThresholdPromotion[] toObjects (BaseOracleBean[] beans) {
    if (beans == null || beans.length == 0)
      return  new CMSThresholdPromotion[0];
    CMSThresholdPromotion[] array = new CMSThresholdPromotion[beans.length];
    for (int i = 0; i < beans.length; i++)
      array[i] = this.toObject((RkThPrmOracleBean)beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return 
   */
  public CMSThresholdPromotion toObject (RkThPrmOracleBean bean) {
    CMSThresholdPromotion object = new CMSThresholdPromotion();
    object.doSetId(bean.getId());
    object.doSetThresholdAmount(bean.getThresholdAmt());
    object.doSetStartDate(bean.getStartDate());
    object.doSetEndDate(bean.getEndDate());
    if (bean.getPercentOffFl() != null) {
      object.doSetPercentOffFlag(bean.getPercentOffFl().booleanValue());
    } 
    else {
      System.out.println("bean.getPercentOffFl() is null ");
    }
    if (object.isPercentOff())
      object.doSetPercentOff(bean.getPercentOff()); 
    else 
      object.doSetAmountOff(bean.getAmountOff());
    ((CMSThresholdPromotion)object).doSetIdStrRt(bean.getIdStrRt());
    ((CMSThresholdPromotion)object).doSetDescription(bean.getPromotionName());
    return  object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @param currencyCode
   * @return 
   */
  private RkThPrmOracleBean toRkThPrmBean (ThresholdPromotion object, String currencyCode) {
    RkThPrmOracleBean bean = new RkThPrmOracleBean();
    bean.setId(object.getId());
    bean.setCurrencyCode(currencyCode);
    bean.setThresholdAmt(object.getThresholdAmount());
    bean.setStartDate(object.getStartDate());
    bean.setEndDate(object.getEndDate());
    bean.setPercentOffFl(object.isPercentOff());
    bean.setAmountOff(object.getAmountOff());
    bean.setPercentOff(object.getPercentOff());
    bean.setIdStrRt(((CMSThresholdPromotion)object).getIdStrRt());
    bean.setPromotionName(((CMSThresholdPromotion)object).getDescription());
    return  bean;
  }
}



