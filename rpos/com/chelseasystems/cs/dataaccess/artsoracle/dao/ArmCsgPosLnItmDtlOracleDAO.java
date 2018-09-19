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
import  com.chelseasystems.cs.pos.CMSConsignmentLineItemDetail;
import  com.chelseasystems.cs.pos.CMSSaleLineItemDetail;
import  com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import  java.sql.*;
import  java.util.*;


/**
 * put your documentation comment here
 */
public class ArmCsgPosLnItmDtlOracleDAO extends BaseOracleDAO
    implements ArmCsgPosLnItmDtlDAO {
  private static String selectSql = ArmCsgPosLnItmDtlOracleBean.selectSql;
  private static String insertSql = ArmCsgPosLnItmDtlOracleBean.insertSql;
  private static String updateSql = ArmCsgPosLnItmDtlOracleBean.updateSql + "where AI_TRN = ?";
  private static String updateVoidSql = ArmCsgPosLnItmDtlOracleBean.updateVoidSql + "where AI_TRN = ? and AI_LN_ITM = ? and SEQUENCE_NUM = ?";
  private static String deleteSql = ArmCsgPosLnItmDtlOracleBean.deleteSql + "where AI_TRN = ?";

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param csgLnItmDtl
   * @exception SQLException
   */
  public void insert (POSLineItemDetail posLnItmDtl, CMSConsignmentLineItemDetail csgLnItmDtl) throws SQLException {
    this.execute(this.getInsertSQL(posLnItmDtl, csgLnItmDtl));
  }

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param csgLnItmDtl
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (POSLineItemDetail posLnItmDtl, CMSConsignmentLineItemDetail csgLnItmDtl) throws SQLException {
    List statements = new ArrayList();
    List params = new ArrayList();
    ArmCsgPosLnItmDtlOracleBean armCsgPosLnItmDtlBean = null;
    armCsgPosLnItmDtlBean = this.fromObjectToBean(posLnItmDtl, csgLnItmDtl);
    statements.add(new ParametricStatement(armCsgPosLnItmDtlBean.getInsertSql(), armCsgPosLnItmDtlBean.toList()));
    // update FL_PROCESSED in RK_POS_LNITM_DTL table.
    params.add(armCsgPosLnItmDtlBean.getOrgAiTrn());
    params.add(armCsgPosLnItmDtlBean.getOrigAiLnItm());
    params.add(armCsgPosLnItmDtlBean.getOrigSeqNum());
    statements.add(new ParametricStatement(PosLineItemDetailOracleDAO.updateConsignmentProcessed, params));
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
  public ArmCsgPosLnItmDtlOracleBean getConsignmentLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    String[] whereClause =  {
      ArmCsgPosLnItmDtlOracleBean.COL_AI_TRN, ArmCsgPosLnItmDtlOracleBean.COL_AI_LN_ITM, ArmCsgPosLnItmDtlOracleBean.COL_SEQUENCE_NUM
    };
    try {
      beans = this.query(new ArmCsgPosLnItmDtlOracleBean(), BaseOracleDAO.where(whereClause), params);
      if (beans != null && beans.length > 0)
        return  (ArmCsgPosLnItmDtlOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmCsgPosLnItmDtlOracleDAO.getConsignmentLineItemDetail***");
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
  public ArmCsgPosLnItmDtlOracleBean getProcessedToLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum, boolean isVoid) throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    params.add(new String("0"));
    String whereClause = " where ORG_AI_TRN = ? AND ORIG_AI_LN_ITM = ? AND ORIG_SEQ_NUM = ? AND NVL(FL_VD_LN_ITM, '0') = ?";
    try {
      beans = this.query(new ArmCsgPosLnItmDtlOracleBean(), whereClause, params);
      if (beans != null && beans.length > 0)
        return  (ArmCsgPosLnItmDtlOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmCsgPosLnItmDtlOracleDAO.getConsignmentLineItemDetail***");
      e.printStackTrace();
      throw  (SQLException)e;
    }
    return  null;
  }

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param csgLnItmDtl
   * @return 
   */
  private ArmCsgPosLnItmDtlOracleBean fromObjectToBean (POSLineItemDetail posLnItmDtl, CMSConsignmentLineItemDetail csgLnItmDtl) {
    ArmCsgPosLnItmDtlOracleBean bean = new ArmCsgPosLnItmDtlOracleBean();
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
    bean.setOrgAiTrn(csgLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
    bean.setOrigAiLnItm(csgLnItmDtl.getLineItem().getSequenceNumber());
    bean.setOrigSeqNum(csgLnItmDtl.getSequenceNumber());
    return  bean;
  }
}



