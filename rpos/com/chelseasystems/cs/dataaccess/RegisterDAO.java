/**
 *  History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method selectByStoreAndRegID
 |      |            |           |           |   selectByStoreID                               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.register.*;
import  java.sql.SQLException;
import  com.chelseasystems.cs.register.CMSRegister;


public interface RegisterDAO extends BaseDAO
{

  /**
   * @param storeId
   * @param defaultId
   * @return
   * @exception SQLException
   */
  public Register getNextRegister (String storeId, String defaultId) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void update (Register object) throws SQLException;



  /**
   * @param aRegister
   * @param aStore
   * @return
   * @exception SQLException
   */
  public CMSRegister selectByStoreAndRegID (String aRegister, String aStore) throws SQLException;



  /**
   * @param storeId
   * @return
   * @exception SQLException
   */
  public CMSRegister[] selectByStoreId (String storeId) throws SQLException;
}



