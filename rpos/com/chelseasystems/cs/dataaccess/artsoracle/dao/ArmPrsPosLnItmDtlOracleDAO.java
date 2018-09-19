/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.pos.POSLineItemDetail;
import  com.chelseasystems.cr.pos.POSLineItem;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.pos.CMSPresaleLineItemDetail;
import  com.chelseasystems.cs.pos.CMSSaleLineItemDetail;
import  com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import  java.sql.*;
import  java.util.*;


/**
 * put your documentation comment here
 */
public class ArmPrsPosLnItmDtlOracleDAO extends BaseOracleDAO
    implements ArmPrsPosLnItmDtlDAO {
  private static String selectSql = ArmPrsPosLnItmDtlOracleBean.selectSql;
  private static String insertSql = ArmPrsPosLnItmDtlOracleBean.insertSql;
  private static String updateSql = ArmPrsPosLnItmDtlOracleBean.updateSql + "where AI_TRN = ?";
  private static String updateVoidSql = ArmPrsPosLnItmDtlOracleBean.updateVoidSql + " where AI_TRN = ? and AI_LN_ITM = ? and SEQUENCE_NUM = ?";
  private static String deleteSql = ArmPrsPosLnItmDtlOracleBean.deleteSql + "where AI_TRN = ?";

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param prsLnItmDtl
   * @exception SQLException
   */
  public void insert (POSLineItemDetail posLnItmDtl, CMSPresaleLineItemDetail prsLnItmDtl) throws SQLException {
    this.execute(this.getInsertSQL(posLnItmDtl, prsLnItmDtl));
  }

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param prsLnItmDtl
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (POSLineItemDetail posLnItmDtl, CMSPresaleLineItemDetail prsLnItmDtl) throws SQLException {
    List statements = new ArrayList();
    List params = new ArrayList();
    ArmPrsPosLnItmDtlOracleBean armPrsPosLnItmDtlBean = null;
    armPrsPosLnItmDtlBean = this.fromObjectToBean(posLnItmDtl, prsLnItmDtl);
    statements.add(new ParametricStatement(armPrsPosLnItmDtlBean.getInsertSql(), armPrsPosLnItmDtlBean.toList()));
    // update FL_PROCESSED in RK_POS_LNITM_DTL table.
    params.add(armPrsPosLnItmDtlBean.getOrgAiTrn());
    params.add(armPrsPosLnItmDtlBean.getOrigAiLnItm());
    params.add(armPrsPosLnItmDtlBean.getOrigSeqNum());
    statements.add(new ParametricStatement(PosLineItemDetailOracleDAO.updatePresaleProcessed, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return 
   * @exception SQLException
   */
  public ParametricStatement getUpdateVoidSQL (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException {
    List params = new ArrayList();
    params.add(new String("1"));
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    return  new ParametricStatement(this.updateVoidSql, params);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return 
   * @exception SQLException
   */
  public ArmPrsPosLnItmDtlOracleBean getPresaleLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    String[] whereClause =  {
      ArmPrsPosLnItmDtlOracleBean.COL_AI_TRN, ArmPrsPosLnItmDtlOracleBean.COL_AI_LN_ITM, ArmPrsPosLnItmDtlOracleBean.COL_SEQUENCE_NUM
    };
    try {
      beans = this.query(new ArmPrsPosLnItmDtlOracleBean(), BaseOracleDAO.where(whereClause), params);
      if (beans != null && beans.length > 0)
        return  (ArmPrsPosLnItmDtlOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmPrsPosLnItmDtlOracleDAO.getPresaleLineItemDetail***");
      e.printStackTrace();
      throw  (SQLException)e;
    }
    return  null;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @param isVoid
   * @return 
   * @exception SQLException
   */
  public ArmPrsPosLnItmDtlOracleBean getProcessedToLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum, boolean isVoid) throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    params.add(new String("0"));
    String whereClause = " where ORG_AI_TRN = ? AND ORIG_AI_LN_ITM = ? AND ORIG_SEQ_NUM = ? AND NVL(FL_VD_LN_ITM, '0') = ?";
    try {
      beans = this.query(new ArmPrsPosLnItmDtlOracleBean(), whereClause, params);
      if (beans != null && beans.length > 0)
        return  (ArmPrsPosLnItmDtlOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmPrsPosLnItmDtlOracleDAO.getPresaleLineItemDetail***");
      e.printStackTrace();
      throw  (SQLException)e;
    }
    return  null;
  }

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param prsLnItmDtl
   * @return 
   */
  private ArmPrsPosLnItmDtlOracleBean fromObjectToBean (POSLineItemDetail posLnItmDtl, CMSPresaleLineItemDetail prsLnItmDtl) {
    ArmPrsPosLnItmDtlOracleBean bean = new ArmPrsPosLnItmDtlOracleBean();
    if (posLnItmDtl instanceof CMSSaleLineItemDetail) {
      bean.setAiTrn(posLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
      bean.setAiLnItm(posLnItmDtl.getLineItem().getSequenceNumber());
      bean.setSequenceNum(posLnItmDtl.getSequenceNumber());
    }
    if (posLnItmDtl instanceof CMSReturnLineItemDetail) {
      bean.setAiTrn(posLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
      bean.setAiLnItm(posLnItmDtl.getLineItem().getSequenceNumber());
      bean.setSequenceNum(posLnItmDtl.getSequenceNumber());
    }
    bean.setOrgAiTrn(prsLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
    bean.setOrigAiLnItm(prsLnItmDtl.getLineItem().getSequenceNumber());
    bean.setOrigSeqNum(prsLnItmDtl.getSequenceNumber());
    return  bean;
  }
}



