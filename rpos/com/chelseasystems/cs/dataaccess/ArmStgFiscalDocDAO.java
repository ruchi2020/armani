//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess;

import java.sql.SQLException;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;

public interface ArmStgFiscalDocDAO extends BaseDAO {

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getStgFiscalDocInsertSQL(FiscalDocument object)
      throws SQLException;
}
