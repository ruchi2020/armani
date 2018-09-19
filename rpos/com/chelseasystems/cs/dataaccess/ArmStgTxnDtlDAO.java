/*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package com.chelseasystems.cs.dataaccess;

import com.chelseasystems.cs.eod.CMSTransactionEOD;
import com.chelseasystems.cs.sos.CMSTransactionSOS;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.database.ParametricStatement;
import java.sql.SQLException;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;


public interface ArmStgTxnDtlDAO extends BaseDAO {

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getEODTxnDtlInsertSQL(CMSTransactionEOD object)
      throws SQLException;

  public ParametricStatement[] getFiscalDocumentInsertSQL(FiscalDocument document)
      throws SQLException;


  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getSOSTxnDtlInsertSQL(CMSTransactionSOS object)
      throws SQLException;


  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] processPaymentTxnDtl(PaymentTransaction object)
      throws SQLException;
}


