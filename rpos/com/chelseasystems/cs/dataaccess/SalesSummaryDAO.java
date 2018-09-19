/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.*;
import  java.sql.SQLException;
import  java.util.Date;
import  com.chelseasystems.cr.txnposter.SalesSummary;
import  com.chelseasystems.cr.pos.PaymentTransaction;


public interface SalesSummaryDAO extends BaseDAO
{

  /**
   * @param object
   * @exception SQLException
   */
  public void insert (SalesSummary object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void update (SalesSummary object) throws SQLException;



  /**
   * @param date
   * @param storeId
   * @return
   * @exception SQLException
   */
  public SalesSummary[] selectByDateStoreId (Date date, String storeId) throws SQLException;



  /**
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public SalesSummary[] selectByDateStoreId (Date from, Date to, String storeId) throws SQLException;



  /**
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public SalesSummary[] selectTotalByDateStoreId (Date from, Date to, String storeId) throws SQLException;



  /**
   * @param paymentTransaction
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] createForPaymentTransaction (PaymentTransaction paymentTransaction) throws SQLException;
}



