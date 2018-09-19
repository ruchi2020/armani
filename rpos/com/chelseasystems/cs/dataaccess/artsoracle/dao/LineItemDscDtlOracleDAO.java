/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-28-2005 | Pankaja   | N/A       | Modify to support all line item (PRS/CSG)          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-15-2005 | Khyati    | N/A       | Discount Detail for Line Item                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cr.pos.POSLineItem;
import  com.chelseasystems.cs.pos.CMSSaleLineItem;
import  com.chelseasystems.cr.discount.Discount;
import  com.chelseasystems.cs.discount.CMSDiscount;
import  com.chelseasystems.cs.discount.LineItemDiscountDetail;
import  com.chelseasystems.cr.pos.CompositePOSTransaction;


/**
 * put your documentation comment here
 */
public class LineItemDscDtlOracleDAO extends BaseOracleDAO {
  private static String selectSql = TrLtmDscDtlOracleBean.selectSql;
  private static String insertSql = TrLtmDscDtlOracleBean.insertSql;
  private static String updateSql = TrLtmDscDtlOracleBean.updateSql + "where AI_TRN = ?";
  private static String deleteSql = TrLtmDscDtlOracleBean.deleteSql + "where AI_TRN = ?";

  //
  //Non public methods begin here!
  //
  protected BaseOracleBean getDatabeanInstance () {
    return  new TrLtmDscDtlOracleBean();
  }

  //  private TrLtmDscDtl[] fromBeansToObjects(BaseOracleBean[] beans) {
  //    TrLtmDscDtl[] array = new TrLtmDscDtl[beans.length];
  //    for (int i = 0; i < array.length; i++) array[i] = fromBeanToObject(beans[i]);
  //    return array;
  //  }
  /////this method needs to customized
  //  private TrLtmDscDtl fromBeanToObject(BaseOracleBean baseBean) {
  //    TrLtmDscDtlOracleBean bean = (TrLtmDscDtlOracleBean) baseBean;
  //    TrLtmDscDtl object = new TrLtmDscDtl();
  //    object.doSetAiTrn(bean.getAiTrn());
  //    object.doSetIdItm(bean.getIdItm());
  //    object.doSetSeqNum(bean.getSeqNum());
  //    object.doSetDscSeqNum(bean.getDscSeqNum());
  //    return object;
  //  }
  ParametricStatement[] getInsertSQL (POSLineItem object) throws SQLException {
    ArrayList statements = new ArrayList();
    Discount discounts[] = object.getDiscountsArray();
    if (discounts != null) {
      for (int i = 0; i < discounts.length; i++) {
        LineItemDiscountDetail lineItemDscDtl = new LineItemDiscountDetail();
        lineItemDscDtl.doSetTxnId(object.getTransaction().getCompositeTransaction().getId());
        lineItemDscDtl.doSetItemId(object.getSequenceNumber());
        lineItemDscDtl.doSetSeqNum(i);
        lineItemDscDtl.doSetDscSeqNum(((CMSDiscount)discounts[i]).getSequenceNumber());
        statements.add(new ParametricStatement(insertSql, fromObjectToBean(lineItemDscDtl).toList()));
      }
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param txnId
   * @param itemSeqNum
   * @param dscSeqNum
   * @return 
   * @exception SQLException
   */
  public LineItemDiscountDetail[] getLineItemDiscounts (String txnId, int itemSeqNum, int dscSeqNum) throws SQLException {
    String whereSql = where(TrLtmDscDtlOracleBean.COL_AI_TRN, TrLtmDscDtlOracleBean.COL_AL_LN_ITM, TrLtmDscDtlOracleBean.COL_DSC_SEQ_NUM);
    List params = new ArrayList();
    params.add(txnId);
    params.add(new Integer(itemSeqNum));
    params.add(new Integer(dscSeqNum));
    return  fromBeansToObjects(query(new TrLtmDscDtlOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  private LineItemDiscountDetail[] fromBeansToObjects (BaseOracleBean[] beans) {
    LineItemDiscountDetail[] details = new LineItemDiscountDetail[beans.length];
    if (beans != null && beans.length > 0) {
      for (int i = 0; i < beans.length; i++) {
        details[i] = fromBeanToObject(beans[i]);
      }
    }
    return  details;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return 
   */
  private LineItemDiscountDetail fromBeanToObject (BaseOracleBean bean) {
    TrLtmDscDtlOracleBean dtlBean = (TrLtmDscDtlOracleBean)bean;
    LineItemDiscountDetail deail = new LineItemDiscountDetail(dtlBean.getAiTrn(), dtlBean.getAlLnItm().intValue(), dtlBean.getSeqNum().intValue(), dtlBean.getDscSeqNum().intValue());
    return  deail;
  }

  /////these method needs to customized
  private TrLtmDscDtlOracleBean fromObjectToBean (LineItemDiscountDetail object) {
    TrLtmDscDtlOracleBean bean = new TrLtmDscDtlOracleBean();
    bean.setAiTrn(object.getTxnId());
    bean.setAlLnItm(new Long(object.getItemId()));
    bean.setSeqNum(new Long(object.getSeqNum()));
    bean.setDscSeqNum(new Long(object.getDScSeqNum()));
    return  bean;
  }
}



