/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-29-2005 | Pankaja   | N/A       | New method for updation of the expiration dt |
 --------------------------------------------------------------------------------------------
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Presale impl                           |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPosPrsOracleBean;
import  com.chelseasystems.cs.pos.PresaleTransaction;
import  java.sql.SQLException;


public interface ArmPosPrsDAO extends BaseDAO
{

  /**
   * @param object
   * @exception SQLException
   */
  public void insert (PresaleTransaction object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (PresaleTransaction object) throws SQLException;



  /**
   * @param txnId
   * @return
   * @exception SQLException
   */
  public ArmPosPrsOracleBean getPresaleTransaction (String txnId) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public PresaleTransaction updateExpirationDt (PresaleTransaction object) throws SQLException;
}



