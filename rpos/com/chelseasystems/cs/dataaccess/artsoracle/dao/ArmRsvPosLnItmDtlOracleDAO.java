/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cs.pos.CMSReservationLineItemDetail;
import java.sql.*;
import java.util.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;


/*History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 07-21-2005 | Manpreet  | N/A       | Specs Reservation impl                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
/**
 *
 * <p>Title: ArmRsvLnItmDtlOracleDAO</p>
 * <p>Description:To persist/query Reservationline item details </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ArmRsvPosLnItmDtlOracleDAO extends BaseOracleDAO implements ArmRsvPosLnItmDtlDAO {
  private static String selectSql = ArmRsvPosLnItmDtlOracleBean.selectSql;
  private static String insertSql = ArmRsvPosLnItmDtlOracleBean.insertSql;
  private static String updateSql = ArmRsvPosLnItmDtlOracleBean.updateSql + "where AI_TRN = ?";
  private static String updateVoidSql = ArmRsvPosLnItmDtlOracleBean.updateVoidSql + " where AI_TRN = ? and AI_LN_ITM = ? and SEQUENCE_NUM = ?";
  private static String deleteSql = ArmRsvPosLnItmDtlOracleBean.deleteSql + "where AI_TRN = ?";

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param rsvLnItmDtl
   * @exception SQLException
   */
  public void insert(POSLineItemDetail posLnItmDtl, CMSReservationLineItemDetail rsvLnItmDtl)
      throws SQLException {
    this.execute(this.getInsertSQL(posLnItmDtl, rsvLnItmDtl));
  }

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param rsvLnItmDtl
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL(POSLineItemDetail posLnItmDtl
      , CMSReservationLineItemDetail rsvLnItmDtl)
      throws SQLException {
    List statements = new ArrayList();
    List params = new ArrayList();
    ArmRsvPosLnItmDtlOracleBean armRsvPosLnItmDtlBean = null;
    armRsvPosLnItmDtlBean = this.fromObjectToBean(posLnItmDtl, rsvLnItmDtl);
    statements.add(new ParametricStatement(armRsvPosLnItmDtlBean.getInsertSql()
        , armRsvPosLnItmDtlBean.toList()));
    // update FL_PROCESSED in RK_POS_LNITM_DTL table.
    params.add(armRsvPosLnItmDtlBean.getOrgAiTrn());
    params.add(armRsvPosLnItmDtlBean.getOrigAiLnItm());
    params.add(armRsvPosLnItmDtlBean.getOrigSeqNum());
    if (rsvLnItmDtl.getLineItem().getTransaction()
        == ((CMSCompositePOSTransaction)(rsvLnItmDtl.getLineItem().
        getTransaction().getCompositeTransaction())).getNoReservationOpenTransaction())
      return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    statements.add(new ParametricStatement(PosLineItemDetailOracleDAO.updateReservationProcessed
        , params));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ArmRsvPosLnItmDtlOracleBean getReservationLineItemDetail(String transactionId
      , int lineItemSeqNum, int lineItemDetailSeqNum)
      throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    String[] whereClause = {
        ArmRsvPosLnItmDtlOracleBean.COL_AI_TRN, ArmRsvPosLnItmDtlOracleBean.COL_AI_LN_ITM
        , ArmRsvPosLnItmDtlOracleBean.COL_SEQUENCE_NUM
    };
    try {
      beans = this.query(new ArmRsvPosLnItmDtlOracleBean(), BaseOracleDAO.where(whereClause)
          , params);
      if (beans != null)
        return (ArmRsvPosLnItmDtlOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmRsvLnItmDtlOracleDAO.getReservationLineItemDetail***");
      e.printStackTrace();
      throw (SQLException)e;
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ArmRsvPosLnItmDtlOracleBean getProcessedToLineItemDetail(String transactionId
      , int lineItemSeqNum, int lineItemDetailSeqNum)
      throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    String[] whereClause = {
        ArmRsvPosLnItmDtlOracleBean.COL_ORG_AI_TRN, ArmRsvPosLnItmDtlOracleBean.COL_ORIG_AI_LN_ITM
        , ArmRsvPosLnItmDtlOracleBean.COL_ORIG_SEQ_NUM
    };
    try {
      beans = this.query(new ArmRsvPosLnItmDtlOracleBean(), BaseOracleDAO.where(whereClause)
          , params);
      if (beans != null)
        return (ArmRsvPosLnItmDtlOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmRsvLnItmDtlOracleDAO.getReservationLineItemDetail***");
      e.printStackTrace();
      throw (SQLException)e;
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateVoidSQL(String transactionId, int lineItemSeqNum
      , int lineItemDetailSeqNum)
      throws SQLException {
    List params = new ArrayList();
    params.add(new String("1"));
    params.add(transactionId);
    params.add(new Long(lineItemSeqNum));
    params.add(new Long(lineItemDetailSeqNum));
    return new ParametricStatement(this.updateVoidSql, params);
  }


  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param rsvLnItmDtl
   * @return
   */
  private ArmRsvPosLnItmDtlOracleBean fromObjectToBean(POSLineItemDetail posLnItmDtl
      , CMSReservationLineItemDetail rsvLnItmDtl) {
    ArmRsvPosLnItmDtlOracleBean bean = new ArmRsvPosLnItmDtlOracleBean();
    bean.setAiTrn(posLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
    bean.setAiLnItm(posLnItmDtl.getLineItem().getSequenceNumber());
    bean.setSequenceNum(posLnItmDtl.getSequenceNumber());

    bean.setOrgAiTrn(rsvLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
    if (rsvLnItmDtl.getLineItem().getTransaction()
        == ((CMSCompositePOSTransaction)(rsvLnItmDtl.getLineItem().getTransaction().
        getCompositeTransaction())).getNoReservationOpenTransaction()) {
      bean.setOrigAiLnItm(posLnItmDtl.getLineItem().getSequenceNumber());
      bean.setOrigSeqNum(posLnItmDtl.getSequenceNumber());
    } else {
      bean.setOrigAiLnItm(rsvLnItmDtl.getLineItem().getSequenceNumber());
      bean.setOrigSeqNum(rsvLnItmDtl.getSequenceNumber());
    }
    return bean;
  }
}

