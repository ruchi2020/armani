/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

/////put your import statemants here.
import  com.chelseasystems.cs.eod.*;
/////
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.eod.TransactionEOD;


/**
 * put your documentation comment here
 */
public class ArmEodDtlOracleDAO extends BaseOracleDAO
    implements ArmEodDtlDAO {
  private static String selectSql = ArmEodDtlOracleBean.selectSql;
  private static String insertSql = ArmEodDtlOracleBean.insertSql;
  private static String updateSql = ArmEodDtlOracleBean.updateSql + "where AI_TRN = ?";
  private static String deleteSql = ArmEodDtlOracleBean.deleteSql + "where AI_TRN = ?";

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL (CMSTransactionEOD object) throws SQLException {
    ArrayList statements = new ArrayList();
    CMSEODNonDepositTotal[] tenderTotal = (CMSEODNonDepositTotal[])object.getEODTenderTotals().values().toArray(new CMSEODNonDepositTotal[0]);
    for (int i = 0; i < tenderTotal.length; i++)
      statements.add(new ParametricStatement(insertSql, fromObjectToBean(object, tenderTotal[i]).toList()));
    CMSEODNonDepositTotal[] txnTotal = (CMSEODNonDepositTotal[])object.getEODTransactionTotals().values().toArray(new CMSEODNonDepositTotal[0]);
    for (int i = 0; i < txnTotal.length; i++)
      statements.add(new ParametricStatement(insertSql, fromObjectToBean(object, txnTotal[i]).toList()));
    CMSEODNonDepositTotal[] taxTotal = (CMSEODNonDepositTotal[])object.getEODTaxTotals().values().toArray(new CMSEODNonDepositTotal[0]);
    for (int i = 0; i < taxTotal.length; i++)
      statements.add(new ParametricStatement(insertSql, fromObjectToBean(object, taxTotal[i]).toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public CMSTransactionEOD selectByID (CMSTransactionEOD object) throws SQLException {
    String whereSql = where(ArmEodDtlOracleBean.COL_AI_TRN);
    List params = new ArrayList();
    params.add(object.getId());
    CMSEODNonDepositTotal[] eodTotals = fromBeansToObjects(query(new ArmEodDtlOracleBean(), whereSql, params));
    for (int index = 0; index < eodTotals.length; index++) {
      CMSEODNonDepositTotal eodTotal = eodTotals[index];
      if (eodTotal != null) {
        if (eodTotal.getGroupCode().equals(ArtsConstants.TENDER_GROUP_CODE))
          object.addEODTenderTotal(eodTotal);
        else if (eodTotal.getGroupCode().equals(ArtsConstants.TXN_GROUP_CODE))
          object.addEODTransactionTotal(eodTotal);
        else if (eodTotal.getGroupCode().equals(ArtsConstants.TAX_GROUP_CODE))
          object.addEODTaxTotal(eodTotal);
        else {}
      }
    }
    return  object;
  }

  /**
   * put your documentation comment here
   * @param cdGroup
   * @return
   * @exception SQLException
   */
  public CMSEODNonDepositTotal[] selectByCdGroup (String cdGroup) throws SQLException {
    String whereSql = "where CD_GROUP = ?";
    return  fromBeansToObjects(query(new ArmEodDtlOracleBean(), whereSql, cdGroup));
  }

  /**
   * put your documentation comment here
   * @param cdType
   * @return
   * @exception SQLException
   */
  public CMSEODNonDepositTotal[] selectByCdType (String cdType) throws SQLException {
    String whereSql = "where CD_TYPE = ?";
    return  fromBeansToObjects(query(new ArmEodDtlOracleBean(), whereSql, cdType));
  }

  //
  //Non public methods begin here!
  //
  protected BaseOracleBean getDatabeanInstance () {
    return  new ArmEodDtlOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   */
  private CMSEODNonDepositTotal[] fromBeansToObjects (BaseOracleBean[] beans) {
    CMSEODNonDepositTotal[] array = new CMSEODNonDepositTotal[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /////this method needs to customized
  private CMSEODNonDepositTotal fromBeanToObject (BaseOracleBean baseBean) {
    ArmEodDtlOracleBean bean = (ArmEodDtlOracleBean)baseBean;
    CMSEODNonDepositTotal object = new CMSEODNonDepositTotal();
    object.doSetGroupCode(bean.getCdGroup());
    object.doSetTypeCode(bean.getCdType());
    object.doSetEODTotal(bean.getTotal());
    object.doSetReportedEODTotal(bean.getReportedTotal());
    object.doSetMediaCount(bean.getCount().intValue());
    return  object;
  }

  /////these method needs to customized
  private ArmEodDtlOracleBean fromObjectToBean (CMSTransactionEOD eodTxn, CMSEODNonDepositTotal object) {
    ArmEodDtlOracleBean bean = new ArmEodDtlOracleBean();
    bean.setAiTrn(eodTxn.getId());
    bean.setCdGroup(object.getGroupCode());
    if(object.getTypeCode().equalsIgnoreCase("DEBIT")&& (eodTxn.getStore().getCountry().equals("CAN"))){
    	bean.setCdType("DINERS");
    }else
    bean.setCdType(object.getTypeCode());
    bean.setTotal(object.getEODTotal());
    bean.setReportedTotal(object.getReportedEODTotal());
    bean.setCount(object.getMediaCount());
    return  bean;
  }
}



