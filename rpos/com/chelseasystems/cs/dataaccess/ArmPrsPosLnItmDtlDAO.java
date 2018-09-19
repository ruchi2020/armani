/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.pos.POSLineItemDetail;
import  com.chelseasystems.cs.pos.CMSPresaleLineItemDetail;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPrsPosLnItmDtlOracleBean;
import  java.sql.SQLException;


public interface ArmPrsPosLnItmDtlDAO extends BaseDAO
{

  /**
   * @param posLnItmDtl
   * @param prsLnItmDtl
   * @exception SQLException
   */
  public void insert (POSLineItemDetail posLnItmDtl, CMSPresaleLineItemDetail prsLnItmDtl) throws SQLException;



  /**
   * @param posLnItmDtl
   * @param prsLnItmDtl
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (POSLineItemDetail posLnItmDtl, CMSPresaleLineItemDetail prsLnItmDtl) throws SQLException;



  /**
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateVoidSQL (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException;



  /**
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ArmPrsPosLnItmDtlOracleBean getPresaleLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException;



  /**
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @param isVoid
   * @return
   * @exception SQLException
   */
  public ArmPrsPosLnItmDtlOracleBean getProcessedToLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum, boolean isVoid) throws SQLException;
}



