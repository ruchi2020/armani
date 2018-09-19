/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cs.pos.CMSTransactionHeader;
import  java.util.Date;
import  java.sql.SQLException;


public interface VArmTxnHdrDAO extends BaseDAO
{

  /**
   * @param txnId
   * @param txnType
   * @return
   * @exception SQLException
   */
  public CMSTransactionHeader selectByTxnId (String txnId, String txnType) throws SQLException;



  /**
   * @param storeId
   * @param txnType
   * @return
   * @exception SQLException
   */
  public CMSTransactionHeader[] selectByStoreId (String storeId, String txnType) throws SQLException;



  /**
   * @param custId
   * @param txnType
   * @return
   * @exception SQLException
   */
  public CMSTransactionHeader[] selectByCustId (String custId, String txnType) throws SQLException;



  /**
   * @param storeId
   * @param txnType
   * @param startDate
   * @param endDate
   * @return
   * @exception SQLException
   */
  public CMSTransactionHeader[] selectByDate (String storeId, String txnType, Date startDate, Date endDate) throws SQLException;



  /**
   * @param storeId
   * @param txnType
   * @return
   * @exception SQLException
   */
  public CMSTransactionHeader[] selectByTxnType (String storeId, String txnType) throws SQLException;
}



