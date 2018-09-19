/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
/** Copyright 1999-2001, Chelsea Market Systems
 *  History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method selectByStoreAndRegID
 |      |            |           |           |   selectByStoreID                               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.transaction.*;
import  com.chelseasystems.cr.database.*;
import  java.sql.*;


public interface TransactionDAO extends BaseDAO
{

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (CommonTransaction object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (CommonTransaction object) throws SQLException;



  /**
   * @param sqls
   * @exception SQLException
   */
  public void execute (ParametricStatement[] sqls) throws SQLException;



  /**
   * @param storeId
   * @param registerId
   * @return
   * @exception SQLException
   */
  public String[] selectMaxTxnId (String storeId, String registerId) throws SQLException;
}



