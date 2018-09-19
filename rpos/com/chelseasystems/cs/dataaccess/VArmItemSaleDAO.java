/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  java.util.Date;
import  com.chelseasystems.cs.txnposter.ArmSalesSummary;


public interface VArmItemSaleDAO extends BaseDAO
{

  /**
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public ArmSalesSummary[] selectTotalByDept (Date from, Date to, String storeId) throws SQLException;



  /**
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public ArmSalesSummary[] selectTotalByDeptClass (Date from, Date to, String storeId) throws SQLException;



  /**
   * @param from
   * @param to
   * @param storeId
   * @return
   * @exception SQLException
   */
  public ArmSalesSummary[] selectTotalByItem (Date from, Date to, String storeId) throws SQLException;
}



