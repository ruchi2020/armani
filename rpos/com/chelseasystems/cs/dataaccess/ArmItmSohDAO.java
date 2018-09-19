/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.pos.PaymentTransaction;
import  com.chelseasystems.cs.item.ItemStock;
import  java.sql.SQLException;


public interface ArmItmSohDAO extends BaseDAO
{

  /**
   * @param object
   * @exception SQLException
   */
  public void insert (ItemStock object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (ItemStock object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void update (ItemStock object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (ItemStock object) throws SQLException;



  /**
   * @param storeId
   * @param itemId
   * @return
   * @exception SQLException
   */
  public ItemStock selectById (String storeId, String itemId) throws SQLException;



  /**
   * @param pTxn
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] createForPaymentTransaction (PaymentTransaction pTxn) throws SQLException;
}



