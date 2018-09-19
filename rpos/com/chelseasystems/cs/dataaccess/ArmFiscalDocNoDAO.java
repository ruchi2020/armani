/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;


public interface ArmFiscalDocNoDAO extends BaseDAO
{

  /**
   * @param sStoreId
   * @param sRegisterId
   * @return
   * @exception SQLException
   */
  public FiscalDocumentNumber getByStoreAndRegister (String sStoreId, String sRegisterId) throws SQLException;
}



