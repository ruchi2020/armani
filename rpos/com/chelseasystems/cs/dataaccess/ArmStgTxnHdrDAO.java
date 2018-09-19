/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.transaction.CommonTransaction;
import  com.chelseasystems.cr.database.ParametricStatement;
import  java.sql.SQLException;


public interface ArmStgTxnHdrDAO extends BaseDAO
{

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getStgTxnHeaderInsertSQL (CommonTransaction object) throws SQLException;
}



