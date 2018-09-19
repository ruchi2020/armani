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
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Consignment impl                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPosCsgOracleBean;
import  com.chelseasystems.cs.pos.ConsignmentTransaction;
import  java.sql.SQLException;


public interface ArmPosCsgDAO extends BaseDAO
{

  /**
   * @param object
   * @exception SQLException
   */
  public void insert (ConsignmentTransaction object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (ConsignmentTransaction object) throws SQLException;



  /**
   * @param txnId
   * @return
   * @exception SQLException
   */
  public ArmPosCsgOracleBean getConsignmentTransaction (String txnId) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ConsignmentTransaction updateExpirationDt (ConsignmentTransaction object) throws SQLException;
}



