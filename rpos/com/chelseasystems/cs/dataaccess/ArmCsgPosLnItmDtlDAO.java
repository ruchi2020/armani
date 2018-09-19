/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.pos.POSLineItemDetail;
import  com.chelseasystems.cs.pos.CMSConsignmentLineItemDetail;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCsgPosLnItmDtlOracleBean;
import  java.sql.SQLException;


public interface ArmCsgPosLnItmDtlDAO extends BaseDAO
{

  /**
   * @param posLnItmDtl
   * @param csgLnItmDtl
   * @exception SQLException
   */
  public void insert (POSLineItemDetail posLnItmDtl, CMSConsignmentLineItemDetail csgLnItmDtl) throws SQLException;



  /**
   * @param posLnItmDtl
   * @param csgLnItmDtl
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (POSLineItemDetail posLnItmDtl, CMSConsignmentLineItemDetail csgLnItmDtl) throws SQLException;



  /**
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ArmCsgPosLnItmDtlOracleBean getConsignmentLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException;



  /**
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @param isVoid
   * @return
   * @exception SQLException
   */
  public ArmCsgPosLnItmDtlOracleBean getProcessedToLineItemDetail (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum, boolean isVoid) throws SQLException;



  /**
   * @param transactionId
   * @param lineItemSeqNum
   * @param lineItemDetailSeqNum
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateVoidSQL (String transactionId, int lineItemSeqNum, int lineItemDetailSeqNum) throws SQLException;
}



