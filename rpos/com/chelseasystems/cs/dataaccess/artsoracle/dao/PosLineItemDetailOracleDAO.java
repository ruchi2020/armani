/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 10   | 09-14-2005 | Manpreet  | 508       |getInsertSQL(),fromBeanToObject()-Alteration Details|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 9    | 05-20-2005 | Vikram    | 196       |Set RedeemableType for Redeemables                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 8    | 05-10-2005 | Vikram    | N/A       |POS_104665_TS_LoyaltyManagement_Rev0                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 04-27-2005 | Rajesh    | N/A       |Modified fromBeanToObject method check for null     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 04-26-2005 | Rajesh    | N/A       |Modified fromBeanToObject method                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 04-19-2005 | Manpreet  | N/A       |POS_104665_TS_Alterations_Rev2                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-17-2005 | Pankaja   | N/A       |Specs Presale/Consignment impl                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-13-2005 | Rajesh    | N/A       |Specs Consignment impl                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.pos.LayawayLineItem;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cr.pos.ReturnLineItemDetail;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.SaleLineItemDetail;
import com.chelseasystems.cs.dataaccess.PosLineItemDetailDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPosLnItmDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkRtnPosLnItmOracleBean;
import com.chelseasystems.cs.pos.CMSLayawayLineItemDetail;
import com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import com.chelseasystems.cs.pos.CMSSaleLineItemDetail;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;
import com.chelseasystems.cs.pos.CMSPresaleLineItemDetail;
import com.chelseasystems.cs.pos.CMSConsignmentLineItem;
import com.chelseasystems.cs.pos.CMSConsignmentLineItemDetail;
import com.chelseasystems.cs.pos.CMSReservationLineItem;
import com.chelseasystems.cs.pos.CMSReservationLineItemDetail;
import com.chelseasystems.cs.pos.CMSNoSaleLineItem;
import com.chelseasystems.cs.pos.CMSNoSaleLineItemDetail;
import com.chelseasystems.cs.pos.CMSNoReturnLineItem;
import com.chelseasystems.cs.pos.CMSNoReturnLineItemDetail;
import com.chelseasystems.cs.pos.CMSVoidLineItem;
import com.chelseasystems.cs.pos.AlterationLineItemDetail;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cs.pos.CMSReservationLineItemDetail;
import com.chelseasystems.cs.pos.CMSVoidLineItemDetail;


/**
 *
 *  POSLineItemDetail Data Access Object.<br>
 *  This object encapsulates all database access for POSLineItemDetail.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>DealId</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_DEAL_ID</td></tr>
 *    <tr><td>DealMarkdownAmount</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_DEAL_MARKDOWN_AMOUNT</td></tr>
 *    <tr><td>GiftCertificateId</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_GIFT_CERTIFICATE_ID</td></tr>
 *    <tr><td>ManualRegionalTaxAmount</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_MANUAL_REGIONAL_TAX_AMT</td></tr>
 *    <tr><td>ManualTaxAmount</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_MANUAL_TAX_AMOUNT</td></tr>
 *    <tr><td>NetAmount</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_NET_AMOUNT</td></tr>
 *    <tr><td>RegionalTaxAmount</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_REGIONAL_TAX_AMOUNT</td></tr>
 *    <tr><td>Returned</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_SALE_RETURNED</td></tr>
 *    <tr><td>SequenceNumber</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_SEQUENCE_NUMBER</td></tr>
 *    <tr><td>TaxAmount</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_TAX_AMOUNT</td></tr>
 *    <tr><td>UserEnteredDataFlag</td><td>RK_POS_LINE_ITEM_DETAIL</td><td>DETAIL_USER_ENTERED_DATA_FLAG</td></tr>
 *  </table>
 *
 *  @see POSLineItemDetail
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPosLnItmDtlOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkRtnPosLnItmOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.returnedPosLineItemOracleBean
 *
 */
public class PosLineItemDetailOracleDAO extends BaseOracleDAO implements PosLineItemDetailDAO {
  private static ReductionOracleDAO reductionDAO = new ReductionOracleDAO();
  private static ReturnedPosLineItemOracleDAO returnedPosLineItemDAO = new
      ReturnedPosLineItemOracleDAO();
  private static ArmPrsPosLnItmDtlOracleDAO armPrsPosLnItmDtlDAO = new ArmPrsPosLnItmDtlOracleDAO();
  private static ArmCsgPosLnItmDtlOracleDAO armCsgPosLnItmDtlDAO = new ArmCsgPosLnItmDtlOracleDAO();
  private static ArmRsvPosLnItmDtlOracleDAO armRsvPosLnItmDtlDAO = new ArmRsvPosLnItmDtlOracleDAO();
  private static RedeemableIssueOracleDAO redeemableIssueDAO = new RedeemableIssueOracleDAO();
  /**
   * AlterationLineItemDetail OracleDAO
   */
  private static ArmAlternLnItmDtlOracleDAO armAlternLnItmDtlDAO = new ArmAlternLnItmDtlOracleDAO();
  private static String wherePK = where(RkPosLnItmDtlOracleBean.COL_AI_TRN
      , RkPosLnItmDtlOracleBean.COL_AI_LN_ITM, RkPosLnItmDtlOracleBean.COL_SEQUENCE_NUMBER);
  private static String selectSql = RkPosLnItmDtlOracleBean.selectSql;
  private static String insertSql = RkPosLnItmDtlOracleBean.insertSql;
  private static String updateSql = RkPosLnItmDtlOracleBean.updateSql + wherePK;
  private static String deleteSql = RkPosLnItmDtlOracleBean.deleteSql
      + where(RkPosLnItmDtlOracleBean.COL_AI_TRN);
  private static String updateReturningSale = "update " + RkPosLnItmDtlOracleBean.TABLE_NAME
      + " set " + RkPosLnItmDtlOracleBean.COL_SALE_RETURNED + " = 1 " + wherePK;
  private static String updateUndoSaleReturn = "update " + RkPosLnItmDtlOracleBean.TABLE_NAME
      + " set " + RkPosLnItmDtlOracleBean.COL_SALE_RETURNED + " = 0 " + wherePK;
  public static String updatePresaleProcessed = "update " + RkPosLnItmDtlOracleBean.TABLE_NAME
      + " set " + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED + " = '1' " + wherePK;
  public static String updateUndoPresaleProcessed = "update " + RkPosLnItmDtlOracleBean.TABLE_NAME
      + " set " + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED + " = '0' " + wherePK;
  public static String updateConsignmentProcessed = "update " + RkPosLnItmDtlOracleBean.TABLE_NAME
      + " set " + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED + " = '1' " + wherePK;
  public static String updateUndoConsignmentProcessed = "update "
      + RkPosLnItmDtlOracleBean.TABLE_NAME + " set " + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED
      + " = '0' " + wherePK;
  public static String updateReservationProcessed = "update " + RkPosLnItmDtlOracleBean.TABLE_NAME
      + " set " + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED + " = '1' " + wherePK;
  public static String updateUndoReservationProcessed = "update "
      + RkPosLnItmDtlOracleBean.TABLE_NAME + " set " + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED
      + " = '0' " + wherePK;

  /**
   * put your documentation comment here
   * @param returnTransactionId
   * @param returnLineItemSeqNum
   * @param returnLineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUndoReturn(String returnTransactionId, int returnLineItemSeqNum
      , int returnLineItemDetailSeqNum)
      throws SQLException {
    RkRtnPosLnItmOracleBean returnedPosLineItemOracleBean = returnedPosLineItemDAO.
        getByReturnLineItemDetail(returnTransactionId, returnLineItemSeqNum
        , returnLineItemDetailSeqNum);
    List statements = new ArrayList();
    if (returnedPosLineItemOracleBean != null) {
      List params = new ArrayList();
      params.add(returnedPosLineItemOracleBean.getSaleAiTrn());
      params.add(returnedPosLineItemOracleBean.getSaleAiLnItm());
      params.add(returnedPosLineItemOracleBean.getSaleDtlSeqNum());
      statements.add(new ParametricStatement(updateUndoSaleReturn, params));
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param saleDetail
   * @param returnDetail
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateReturningSaleLineItemDetailSQL(SaleLineItemDetail
      saleDetail, ReturnLineItemDetail returnDetail)
      throws SQLException {
    RkPosLnItmDtlOracleBean saleDetailBean = fromObjectToBean(saleDetail);
    List params = new ArrayList();
    params.add(saleDetailBean.getAiTrn());
    params.add(saleDetailBean.getAiLnItm());
    params.add(saleDetailBean.getSequenceNumber());
    ParametricStatement[] statements = new ParametricStatement[2];
    statements[0] = new ParametricStatement(updateReturningSale, params);
    statements[1] = returnedPosLineItemDAO.getInsertSQL(saleDetail, returnDetail);
    return statements;
  }

  /**
   * put your documentation comment here
   * @param saleDetail
   * @param returnDetail
   * @exception SQLException
   */
  public void updateReturningSaleLineItemDetail(SaleLineItemDetail saleDetail
      , ReturnLineItemDetail returnDetail)
      throws SQLException {
    execute(getUpdateReturningSaleLineItemDetailSQL(saleDetail, returnDetail));
  }

  /**
   * put your documentation comment here
   * @param preSaleDetail
   * @param lineItemDetail
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateProcessedPreSaleLineItemDetailSQL(CMSPresaleLineItemDetail
      preSaleDetail, POSLineItemDetail lineItemDetail)
      throws SQLException {
    RkPosLnItmDtlOracleBean saleDetailBean = fromObjectToBean(preSaleDetail);
    List params = new ArrayList();
    params.add(saleDetailBean.getAiTrn());
    params.add(saleDetailBean.getAiLnItm());
    params.add(saleDetailBean.getSequenceNumber());
    ParametricStatement[] statements = new ParametricStatement[2];
    statements[0] = new ParametricStatement(updatePresaleProcessed, params);
    statements[1] = armPrsPosLnItmDtlDAO.getInsertSQL(lineItemDetail, preSaleDetail)[0];
    return statements;
  }

  /**
   * put your documentation comment here
   * @param preSaleDetail
   * @param lineItemDetail
   * @exception SQLException
   */
  public void updateProcessedPreSaleLineItemDetail(CMSPresaleLineItemDetail preSaleDetail
      , POSLineItemDetail lineItemDetail)
      throws SQLException {
    execute(getUpdateProcessedPreSaleLineItemDetailSQL(preSaleDetail, lineItemDetail));
  }

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param preSaleDetail
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUndoPresale(POSLineItemDetail posLnItmDtl
      , CMSPresaleLineItemDetail preSaleDetail)
      throws SQLException {
    RkPosLnItmDtlOracleBean saleDetailBean = fromObjectToBean(preSaleDetail);
    List params = new ArrayList();
    params.add(saleDetailBean.getAiTrn());
    params.add(saleDetailBean.getAiLnItm());
    params.add(saleDetailBean.getSequenceNumber());
    ParametricStatement[] statements = new ParametricStatement[2];
    statements[0] = new ParametricStatement(updateUndoPresaleProcessed, params);
    statements[1] = armPrsPosLnItmDtlDAO.getUpdateVoidSQL(posLnItmDtl.getLineItem().getTransaction().
        getCompositeTransaction().getId(), posLnItmDtl.getLineItem().getSequenceNumber()
        , posLnItmDtl.getSequenceNumber());
    return statements;
  }

  /**
   * put your documentation comment here
   * @param csgnDetail
   * @param lineItemDetail
   * @exception SQLException
   */
  public void updateProcessedConsignmentLineItemDetail(CMSConsignmentLineItemDetail csgnDetail
      , POSLineItemDetail lineItemDetail)
      throws SQLException {
    execute(getUpdateProcessedConsignmentLineItemDetailSQL(csgnDetail, lineItemDetail));
  }

  /**
   * put your documentation comment here
   * @param csgnDetail
   * @param lineItemDetail
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateProcessedConsignmentLineItemDetailSQL(
      CMSConsignmentLineItemDetail csgnDetail, POSLineItemDetail lineItemDetail)
      throws SQLException {
    RkPosLnItmDtlOracleBean saleDetailBean = fromObjectToBean(csgnDetail);
    List params = new ArrayList();
    params.add(saleDetailBean.getAiTrn());
    params.add(saleDetailBean.getAiLnItm());
    params.add(saleDetailBean.getSequenceNumber());
    ParametricStatement[] statements = new ParametricStatement[2];
    statements[0] = new ParametricStatement(updateConsignmentProcessed, params);
    statements[1] = armCsgPosLnItmDtlDAO.getInsertSQL(lineItemDetail, csgnDetail)[0];
    return statements;
  }

  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param csgnDetail
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUndoConsignment(POSLineItemDetail posLnItmDtl
      , CMSConsignmentLineItemDetail csgnDetail)
      throws SQLException {
    RkPosLnItmDtlOracleBean saleDetailBean = fromObjectToBean(csgnDetail);
    List params = new ArrayList();
    params.add(saleDetailBean.getAiTrn());
    params.add(saleDetailBean.getAiLnItm());
    params.add(saleDetailBean.getSequenceNumber());
    ParametricStatement[] statements = new ParametricStatement[2];
    statements[0] = new ParametricStatement(updateUndoConsignmentProcessed, params);
    statements[1] = armCsgPosLnItmDtlDAO.getUpdateVoidSQL(posLnItmDtl.getLineItem().getTransaction().
        getCompositeTransaction().getId(), posLnItmDtl.getLineItem().getSequenceNumber()
        , posLnItmDtl.getSequenceNumber());
    return statements;
  }


  /**
   * put your documentation comment here
   * @param posLnItmDtl
   * @param rsvSaleDetail
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUndoReservation(POSLineItemDetail posLnItmDtl
      , CMSReservationLineItemDetail rsvDetail)
      throws SQLException {
    RkPosLnItmDtlOracleBean saleDetailBean = fromObjectToBean(rsvDetail);
    List params = new ArrayList();
    params.add(saleDetailBean.getAiTrn());
    params.add(saleDetailBean.getAiLnItm());
    params.add(saleDetailBean.getSequenceNumber());
    ParametricStatement[] statements = new ParametricStatement[2];
    statements[0] = new ParametricStatement(updateUndoReservationProcessed, params);
    statements[1] = armRsvPosLnItmDtlDAO.getUpdateVoidSQL(posLnItmDtl.getLineItem().getTransaction().
        getCompositeTransaction().getId(), posLnItmDtl.getLineItem().getSequenceNumber()
        , posLnItmDtl.getSequenceNumber());
    return statements;
  }

  /**
   * put your documentation comment here
   * @param resSaleDetail
   * @param lineItemDetail
   * @exception SQLException
   */
  public void updateProcessedReservationLineItemDetail(CMSReservationLineItemDetail resSaleDetail
      , POSLineItemDetail lineItemDetail)
      throws SQLException {
    execute(getUpdateProcessedReservationLineItemDetailSQL(resSaleDetail, lineItemDetail));
  }

  /**
   * put your documentation comment here
   * @param resSaleDetail
   * @param lineItemDetail
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateProcessedReservationLineItemDetailSQL(
      CMSReservationLineItemDetail resSaleDetail, POSLineItemDetail lineItemDetail)
      throws SQLException {
    RkPosLnItmDtlOracleBean saleDetailBean = fromObjectToBean(resSaleDetail);
    List params = new ArrayList();
    params.add(saleDetailBean.getAiTrn());
    params.add(saleDetailBean.getAiLnItm());
    params.add(saleDetailBean.getSequenceNumber());
    ParametricStatement[] statements = new ParametricStatement[2];
    statements[0] = new ParametricStatement(updateReservationProcessed, params);
    statements[1] = armRsvPosLnItmDtlDAO.getInsertSQL(lineItemDetail, resSaleDetail)[0];
    return statements;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL(POSLineItemDetail object)
      throws SQLException {
    ArrayList statements = new ArrayList();
    statements.add(new ParametricStatement(insertSql, fromObjectToBean(object).toList()));
    String transactionId = object.getLineItem().getTransaction().getCompositeTransaction().getId();
    int lineSequenceNumber = object.getLineItem().getSequenceNumber();
    int detailSequenceNumber = object.getSequenceNumber();
    Reduction[] reductions = object.getReductionsArray();
    if (reductions != null && reductions.length > 0)
      for (int i = 0; i < reductions.length; i++)
        statements.add(reductionDAO.getInsertSQL(transactionId, lineSequenceNumber
            , detailSequenceNumber, reductions[i]));
    if (object instanceof SaleLineItemDetail || object instanceof CMSPresaleLineItemDetail) {
      // Alteration Items -- MSB -- POS_104665_TS_Alterations_Rev2
      // MSB - 09/14/2005  -- Implement Alteration details for Presale
      //      AlterationLineItemDetail[] alterationLineItemDetails= saleLnItmDtl.getAlterationLineItemDetailArray();
      //      CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail)object;
      AlterationLineItemDetail[] alterationLineItemDetails = null;
      if (object instanceof CMSPresaleLineItemDetail) {
        alterationLineItemDetails = ((CMSPresaleLineItemDetail)object).
            getAlterationLineItemDetailArray();
      } else if (object instanceof CMSSaleLineItemDetail) {
        alterationLineItemDetails = ((CMSSaleLineItemDetail)object).
            getAlterationLineItemDetailArray();
      }
      if (alterationLineItemDetails != null && alterationLineItemDetails.length > 0) {
        for (int iCtr = 0; iCtr < alterationLineItemDetails.length; iCtr++) {
          if (alterationLineItemDetails[iCtr] == null)
            continue;
          alterationLineItemDetails[iCtr].doSetTransactionID(transactionId);
          alterationLineItemDetails[iCtr].doSetLineItemID(new Long(lineSequenceNumber));
          alterationLineItemDetails[iCtr].doSetDetailSeqNum(new Long(detailSequenceNumber));
          alterationLineItemDetails[iCtr].doSetAlterationSeqNum(new Long(iCtr));
          statements.addAll(Arrays.asList(armAlternLnItmDtlDAO.getInsertSQL(
              alterationLineItemDetails[iCtr])));
        }
      }
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param posLineItem
   * @exception SQLException
   */
  void addDetailsToLineItem(POSLineItem posLineItem)
      throws SQLException {
    String whereSql = where(RkPosLnItmDtlOracleBean.COL_AI_TRN
        , RkPosLnItmDtlOracleBean.COL_AI_LN_ITM);
    List params = new ArrayList();
    params.add(posLineItem.getTransaction().getCompositeTransaction().getId());
    params.add(new Integer(posLineItem.getSequenceNumber()));
    fromBeansToObjects(posLineItem, query(new RkPosLnItmDtlOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance() {
    return new RkPosLnItmDtlOracleBean();
  }

  /**
   * put your documentation comment here
   * @param posLineItem
   * @param beans
   * @exception SQLException
   */
  private void fromBeansToObjects(POSLineItem posLineItem, BaseOracleBean[] beans)
      throws SQLException {
    for (int i = 0; i < beans.length; i++)
      fromBeanToObject(posLineItem, beans[i]);
  }

  /**
   * put your documentation comment here
   * @param posLineItem
   * @param baseBean
   * @exception SQLException
   */
  private void fromBeanToObject(POSLineItem posLineItem, BaseOracleBean baseBean)
      throws SQLException {
    RkPosLnItmDtlOracleBean bean = (RkPosLnItmDtlOracleBean)baseBean;
    POSLineItemDetail object = null;
    if (posLineItem instanceof CMSNoSaleLineItem)
      object = new CMSNoSaleLineItemDetail(posLineItem);
      else if (posLineItem instanceof CMSNoReturnLineItem)
        object = new CMSNoReturnLineItemDetail(posLineItem);
    else if (posLineItem instanceof SaleLineItem)
      object = new CMSSaleLineItemDetail(posLineItem);
    else if (posLineItem instanceof CMSVoidLineItem)
      object = new CMSVoidLineItemDetail(posLineItem);
    else if (posLineItem instanceof ReturnLineItem)
      object = new CMSReturnLineItemDetail(posLineItem);
    else if (posLineItem instanceof LayawayLineItem)
      object = new CMSLayawayLineItemDetail(posLineItem);
    else if (posLineItem instanceof CMSPresaleLineItem)
      object = new CMSPresaleLineItemDetail(posLineItem);
    else if (posLineItem instanceof CMSConsignmentLineItem)
      object = new CMSConsignmentLineItemDetail(posLineItem);
    else if (posLineItem instanceof CMSReservationLineItem)
      object = new CMSReservationLineItemDetail(posLineItem);
    else if (posLineItem instanceof CMSNoSaleLineItem)
      object = new CMSNoSaleLineItemDetail(posLineItem);
    object.doSetSequenceNumber(bean.getSequenceNumber().intValue());
    object.doSetNetAmount(bean.getNetAmt());
    object.doSetTaxAmount(bean.getTaxAmt());
    object.doSetVatAmount(bean.getVatAmt());
    object.doSetManualTaxAmount(bean.getManualTaxAmt());
    object.doSetDealMarkdownAmount(bean.getDealMkdnAmt());
    if (bean.getUserEnteredFl() != null)
      object.doSetUserEnteredDataFlag(bean.getUserEnteredFl().booleanValue());
    object.doSetGiftCertificateId(bean.getGiftCertId());
    object.doSetRegionalTaxAmount(bean.getRegionalTaxAmt());
    object.doSetManualRegionalTaxAmount(bean.getManRegTaxAmt());
    object.doSetDealId(bean.getDealId());
    if (bean.getGiftCertId() != null && !bean.getGiftCertId().trim().equals("")) {
      Redeemable redeemable = redeemableIssueDAO.selectRedeemableById(bean.getGiftCertId());
      try {
        if (redeemable != null)
          object.getLineItem().getItem().setRedeemableType(redeemable.getType());
      } catch (BusinessRuleException ex) {
        ex.printStackTrace();
      }
    }
    if (object instanceof CMSSaleLineItemDetail) {
      if (bean.getSaleReturned() != null)
    	  ((SaleLineItemDetail)object).doSetReturned(bean.getSaleReturned().booleanValue());
      addAlterationLineItemDetails(bean, (CMSSaleLineItemDetail)object); // MSB - 04/19/2005
      if (bean.getLoyaltyPt() != null)
        ((CMSSaleLineItemDetail)object).setLoyaltyPoints(bean.getLoyaltyPt().doubleValue());
      ((CMSSaleLineItemDetail)object).doSetTypeCode(bean.getPosLnItmTyId());
    }
    if (object instanceof CMSReturnLineItemDetail) {
    	if (bean.getLoyaltyPt() != null) {
    		((CMSReturnLineItemDetail)object).setLoyaltyPoints(bean.getLoyaltyPt().doubleValue());
    	}
      ((CMSReturnLineItemDetail)object).doSetTypeCode(bean.getPosLnItmTyId());
    }
    if (object instanceof CMSPresaleLineItemDetail) {
      if (bean.getFlProcessed() != null)
        ((CMSPresaleLineItemDetail)object).doSetProcessed(bean.getFlProcessed().booleanValue());
      else
        ((CMSPresaleLineItemDetail)object).doSetProcessed(false);
      addAlterationLineItemDetails(bean, (CMSPresaleLineItemDetail)object); // MSB - 09/14/2005
    }
    if (object instanceof CMSConsignmentLineItemDetail) {
      if (bean.getFlProcessed() != null)
        ((CMSConsignmentLineItemDetail)object).doSetProcessed(bean.getFlProcessed().booleanValue());
      else
        ((CMSConsignmentLineItemDetail)object).doSetProcessed(false);
    }
    if (object instanceof CMSReservationLineItemDetail) {
      if (bean.getFlProcessed() != null)
        ((CMSReservationLineItemDetail)object).doSetProcessed(bean.getFlProcessed().booleanValue());
      else
        ((CMSReservationLineItemDetail)object).doSetProcessed(false);
    }
    Reduction[] reductions = reductionDAO.getByTransactionIdAndSequenceNumbers(bean.getAiTrn()
        , bean.getAiLnItm().intValue(), bean.getSequenceNumber().intValue());
    for (int i = 0; i < reductions.length; i++)
      object.doAddReduction(reductions[i]);
    //Test
    if(bean.getStateTaxAmt()!=null)
    object.getLineItem().setStateTax(bean.getStateTaxAmt());
    if(bean.getCityTaxAmt()!=null)
        object.getLineItem().setCityTax(bean.getCityTaxAmt());
    if(bean.getGsthstTaxAmt()!=null)
        object.getLineItem().setGsthstTaxAmt(bean.getGsthstTaxAmt());
    if(bean.getGstTaxAmt()!=null)
        object.getLineItem().setGstTaxAmt(bean.getGstTaxAmt());
    if(bean.getPstTaxAmt()!=null)
        object.getLineItem().setPstTaxAmt(bean.getPstTaxAmt());
    if(bean.getQstTaxAmt()!=null)
        object.getLineItem().setQstTaxAmt(bean.getQstTaxAmt());

    //Ends
  }

  /**
   * Add AlterationDetails to LineItem Details.
   * @param rkPosLnItmDtlOracleBean RkPosLnItmDtlOracleBean
   * @param object POSLineItemDetail
   * @throws SQLException
   */
  private void addAlterationLineItemDetails(RkPosLnItmDtlOracleBean rkPosLnItmDtlOracleBean
      , POSLineItemDetail object)
      throws SQLException {
    try {
      // Get AletrationLineItemDetail array.
      AlterationLineItemDetail alterationLineItemDetails[] = armAlternLnItmDtlDAO.
          getByTransactionIdAndSequenceNumbers(rkPosLnItmDtlOracleBean.getAiTrn()
          , rkPosLnItmDtlOracleBean.getAiLnItm(), rkPosLnItmDtlOracleBean.getSequenceNumber());
      if (alterationLineItemDetails == null)
        return;
      //Populate alteration details into LineItemDetail
      for (int iCtr = 0; iCtr < alterationLineItemDetails.length; iCtr++) {
        if (alterationLineItemDetails[iCtr] == null)
          continue;
        if (object instanceof CMSPresaleLineItemDetail)
          ((CMSPresaleLineItemDetail)object).doAddAlterationLineItemDetail(
              alterationLineItemDetails[iCtr]);
        else if (object instanceof CMSSaleLineItemDetail)
          ((CMSSaleLineItemDetail)object).doAddAlterationLineItemDetail(alterationLineItemDetails[
              iCtr]);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private RkPosLnItmDtlOracleBean fromObjectToBean(POSLineItemDetail object) {
    RkPosLnItmDtlOracleBean bean = new RkPosLnItmDtlOracleBean();
    bean.setAiLnItm(object.getLineItem().getSequenceNumber());
    bean.setAiTrn(object.getLineItem().getTransaction().getCompositeTransaction().getId());
    bean.setSequenceNumber(object.getSequenceNumber());
    bean.setNetAmt(object.getNetAmount());
    if (object.getTaxAmount() != null)
      bean.setTaxAmt(object.getTaxAmount());
    //Test
    if (object.getLineItem().getStateTax() != null)
        bean.setStateTaxAmt(object.getLineItem().getStateTax());
    if (object.getLineItem().getCityTax() != null)
        bean.setCityTaxAmt(object.getLineItem().getCityTax());
    if (object.getLineItem().getGsthstTaxAmt() != null)
        bean.setGsthstTaxAmt(object.getLineItem().getGsthstTaxAmt());
    if (object.getLineItem().getGstTaxAmt() != null)
        bean.setGstTaxAmt(object.getLineItem().getGstTaxAmt());
    if (object.getLineItem().getPstTaxAmt() != null)
        bean.setPstTaxAmt(object.getLineItem().getPstTaxAmt());
    if (object.getLineItem().getQstTaxAmt() != null)
        bean.setQstTaxAmt(object.getLineItem().getQstTaxAmt());
    //Ends
    if (object.getManualTaxAmount() != null)
      bean.setManualTaxAmt(object.getManualTaxAmount());
    if (object.getVatAmount() != null)
      bean.setVatAmt(object.getVatAmount());
    bean.setDealMkdnAmt(object.getDealMarkdownAmount());
    bean.setUserEnteredFl(object.doGetUserEnteredDataFlag());
    bean.setGiftCertId(object.getGiftCertificateId());
    bean.setRegionalTaxAmt(object.doGetRegionalTaxAmount());
    bean.setDealId(object.getDealId());
    if (object.getManualRegionalTaxAmount() != null)
      bean.setManRegTaxAmt(object.getManualRegionalTaxAmount());
    if (object instanceof CMSSaleLineItemDetail) {
      bean.setSaleReturned(((SaleLineItemDetail)object).isReturned());
      if (((CMSSaleLineItemDetail)object).getTypeCode() != null) {
        // Presale
        if (((CMSSaleLineItemDetail)object).getTypeCode().longValue()
            == CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_PRESALE.longValue())
          bean.setPosLnItmTyId(CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_PRESALE);
        // Consignment
        if (((CMSSaleLineItemDetail)object).getTypeCode().longValue()
            == CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_CONSIGNMENT.longValue())
          bean.setPosLnItmTyId(CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_CONSIGNMENT);
        // Reservation
        if (((CMSSaleLineItemDetail)object).getTypeCode().longValue()
            == CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_RESERVATION.longValue())
          bean.setPosLnItmTyId(CMSSaleLineItemDetail.POS_LINE_ITEM_TYPE_RESERVATION);
      }
      if(((CMSSaleLineItemDetail)object).getLoyaltyPoints() >= 0.0)
      bean.setLoyaltyPt(((CMSSaleLineItemDetail)object).getLoyaltyPoints());
    	else 
    		bean.setLoyaltyPt(0.0);
    }
    if (object instanceof CMSReturnLineItemDetail) {
      if (((CMSReturnLineItemDetail)object).getTypeCode() != null) {
        // Presale
        if (((CMSReturnLineItemDetail)object).getTypeCode().longValue()
            == CMSReturnLineItemDetail.POS_LINE_ITEM_TYPE_PRESALE.longValue())
          bean.setPosLnItmTyId(CMSReturnLineItemDetail.POS_LINE_ITEM_TYPE_PRESALE);
        // Consignment
        if (((CMSReturnLineItemDetail)object).getTypeCode().longValue()
            == CMSReturnLineItemDetail.POS_LINE_ITEM_TYPE_CONSIGNMENT.longValue())
          bean.setPosLnItmTyId(CMSReturnLineItemDetail.POS_LINE_ITEM_TYPE_CONSIGNMENT);
        // Reservation
        if (((CMSReturnLineItemDetail)object).getTypeCode().longValue()
            == CMSReturnLineItemDetail.POS_LINE_ITEM_TYPE_RESERVATION.longValue()) {
          bean.setPosLnItmTyId(CMSReturnLineItemDetail.POS_LINE_ITEM_TYPE_RESERVATION);
        }
      }
      bean.setLoyaltyPt(((CMSReturnLineItemDetail)object).getLoyaltyPoints());
    }
    return bean;
  }
}


