/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  java.sql.*;
import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cs.customer.CustomerSaleSummary;
import  com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.VoidTransaction;


public interface ArmCustSaleSummaryDAO extends BaseDAO
{

  /**
   * @param object
   * @exception SQLException
   */
  public void insert (CompositePOSTransaction object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (CompositePOSTransaction object) throws SQLException;



  /**
   * @param custId
   * @param storeId
   * @return
   * @exception SQLException
   */
  public CustomerSaleSummary[] selectByCustNStoreId (String custId, String storeId) throws SQLException;
  
  /**
   * @param object
   * @return
   * @throws SQLException
   */
  public ParametricStatement[] getInsertSQL (VoidTransaction object) throws SQLException;
}



