/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.fiscaldocument.TaxFree;


public interface ArmTaxFreeDAO extends BaseDAO
{

  /**
   * @return
   * @exception SQLException
   */
  public TaxFree[] selectAllTaxFree () throws SQLException;

  /**
   * To get Tax Free
   * @return Tax Free
   * @throws SQLException
   */
  public TaxFree[] selectTaxFreeForStore (String storeID) throws SQLException;
}



