/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2003, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.pricing.*;
import  com.chelseasystems.cr.store.*;
import  java.sql.*;


public interface PromotionDAO extends BaseDAO
{

  /**
   * @param store
   * @return
   * @exception SQLException
   */
  public IPromotion[] selectByStore (Store store) throws SQLException;



  /**
   * @param id
   * @param currencyCode
   * @param storeId
   * @return
   * @exception SQLException
   */
  public IPromotion selectById (String id, String currencyCode, String storeId) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (IPromotion object) throws SQLException;



  /**
   * @param object
   * @param currencyCode
   * @exception SQLException
   */
  public void insert (IPromotion object, String currencyCode) throws SQLException;
}



