/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.pos.CMSTransactionHeader;
import  java.sql.*;
import  java.util.Date;
import  java.util.*;


/**
 * put your documentation comment here
 */
public class VArmTxnHdrOracleDAO extends BaseOracleDAO
    implements VArmTxnHdrDAO {
  private static String selectSql = VArmTxnHdrOracleBean.selectSql;

  /**
   *
   * @param txnId String
   * @param txnType String
   * @throws SQLException
   * @return CMSTransactionHeader
   */
  public CMSTransactionHeader selectByTxnId (String txnId, String txnType) throws SQLException {
    List params = new ArrayList();
    ;
    BaseOracleBean[] beans = null;
    String whereSql = " where " + VArmTxnHdrOracleBean.COL_AI_TRN + " = ? and " + VArmTxnHdrOracleBean.COL_TY_GUI_TRN + " like ?";
    params.add(txnId);
    params.add("%" + txnType + "%");
    beans = this.query(new VArmTxnHdrOracleBean(), whereSql, params);
    if (beans == null || beans.length < 1)
      return  null;
    return  fromBeanToObject(beans[0]);
  }

  /**
   *
   * @param storeId String
   * @param txnType String
   * @throws SQLException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] selectByStoreId (String storeId, String txnType) throws SQLException {
    List params = new ArrayList();
    String whereSql = " where " + VArmTxnHdrOracleBean.COL_ID_STR_RT + " = ? and " + VArmTxnHdrOracleBean.COL_TY_GUI_TRN + " like ? order by "+VArmTxnHdrOracleBean.COL_EXP_DT+" desc";
    params.add(storeId);
    params.add("%" + txnType + "%");
    return  fromBeansToObjects(query(new VArmTxnHdrOracleBean(), whereSql, params));
  }

  /**
   *
   * @param custId String
   * @param txnType String
   * @throws SQLException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] selectByCustId (String custId, String txnType) throws SQLException {
    List params = new ArrayList();
    String whereSql = " where " + VArmTxnHdrOracleBean.COL_CUST_ID + " = ? and " + VArmTxnHdrOracleBean.COL_TY_GUI_TRN + " like ?";
    params.add(custId);
    params.add("%" + txnType + "%");
    return  fromBeansToObjects(query(new VArmTxnHdrOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId String
   * @param txnType String
   * @param startDate Date
   * @param endDate Date
   * @throws SQLException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] selectByDate (String storeId, String txnType, Date startDate, Date endDate) throws SQLException {
    List params = new ArrayList();
    String whereSql = " where " + VArmTxnHdrOracleBean.COL_ID_STR_RT + " = ? and " + VArmTxnHdrOracleBean.COL_TY_GUI_TRN + " like ? and " + VArmTxnHdrOracleBean.COL_TS_TRN_PRC + " >=? and " + VArmTxnHdrOracleBean.COL_TS_TRN_PRC + " <?";
    params.add(storeId);
    params.add("%" + txnType + "%");
    params.add(startDate);
    params.add(endDate);
    return  fromBeansToObjects(query(new VArmTxnHdrOracleBean(), whereSql, params));
  }

  /**
   *
   * @param storeId String
   * @param txnType String
   * @throws SQLException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] selectByTxnType (String storeId, String txnType) throws SQLException {
    List params = new ArrayList();
    String whereSql = " where " + VArmTxnHdrOracleBean.COL_ID_STR_RT + " = ? and " + VArmTxnHdrOracleBean.COL_TY_GUI_TRN + " like ?";
    params.add(storeId);
    params.add("%" + txnType + "%");
    return  fromBeansToObjects(query(new VArmTxnHdrOracleBean(), whereSql, params));
  }

  /**
   *
   * @param baseBean BaseOracleBean
   * @throws SQLException
   * @return CMSTransactionHeader
   */
  private CMSTransactionHeader fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    VArmTxnHdrOracleBean bean = (VArmTxnHdrOracleBean)baseBean;
    CMSTransactionHeader object = new CMSTransactionHeader(bean.getAiTrn());
    object.doSetStoreId(bean.getIdStrRt());
    object.doSetTheOperatorId(bean.getIdOpr());
    object.doSetProcessDate(bean.getTsTrnPrc());
    object.doSetSubmitDate(bean.getTsTrnSbm());
    object.doSetTransactionType(bean.getTyGuiTrn());
    if (bean.getCustId() != null)
      object.doSetCustomerId(bean.getCustId());
    if (bean.getCustFirstName() !=null)
      object.doSetCustomerFirstName(bean.getCustFirstName());
    if (bean.getCustLastName() !=null)
       object.doSetCustomerLastName(bean.getCustLastName());
    if (bean.getConsultantId() != null)
      object.doSetConsultantId(bean.getConsultantId());
    if (bean.getFnPrs() != null)
      object.doSetConsultantFirstName(bean.getFnPrs());
    if (bean.getLnPrs() != null)
      object.doSetConsultantLastName(bean.getLnPrs());
    if (bean.getReductionAmount() != null)
      object.doSetMarkdownAmount(bean.getReductionAmount());
    if (bean.getNetAmount() != null)
      object.doSetRetailAmount(bean.getNetAmount());
    if (bean.getTotalAmt() != null)
      object.doSetTotalAmountDue(bean.getTotalAmt());
    object.doSetExpirationDate(bean.getExpDt());
    object.doSetRefId(bean.getRefId());
    object.doSetPayTxnType(bean.getPayTrnType());
    object.doSetVoidID(bean.getVoidId());
    object.doSetStoreName(bean.getStoreDesc());
    return  object;
  }

  /**
   *
   * @param beans BaseOracleBean[]
   * @throws SQLException
   * @return CMSTransactionHeader[]
   */
  private CMSTransactionHeader[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    CMSTransactionHeader[] array = new CMSTransactionHeader[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = (CMSTransactionHeader)fromBeanToObject(beans[i]);
    return  array;
  }
}



