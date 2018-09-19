/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.sos;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.sos.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.register.*;
import java.util.*;


/**
 *
 * <p>Title: CMSTransactionSOSJDBCServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTransactionSOSJDBCServices extends CMSTransactionSOSServices {
  private TransactionDAO commonTransactionDAO;
  private ArmStgTxnHdrDAO armStgTxnHdrDAO;
  private StoreDAO storeDAO;
  private RegisterDAO registerDAO;

  /**
   * default Constructor
   */
  public CMSTransactionSOSJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    commonTransactionDAO = (TransactionDAO)configMgr.getObject("COMMONTRANSACTION_DAO");
    storeDAO = (StoreDAO)configMgr.getObject("STORE_DAO");
    registerDAO = (RegisterDAO)configMgr.getObject("REGISTER_DAO");
    armStgTxnHdrDAO = (ArmStgTxnHdrDAO)configMgr.getObject("ARMSTGTXNHDR_DAO");
  }

  /**
   * Method post the sos transaction
   * @param transactionSOS
   * @return
   * @exception Exception
   */
  public boolean submit(TransactionSOS transactionSOS)
      throws Exception {
    List statements = new ArrayList();
    transactionSOS.doSetPostDate(new Date());
    try {
      persistRegister(transactionSOS);
      statements.addAll(Arrays.asList(commonTransactionDAO.getInsertSQL(transactionSOS)));
      statements.addAll(Arrays.asList(armStgTxnHdrDAO.getStgTxnHeaderInsertSQL(transactionSOS)));
      commonTransactionDAO.execute((ParametricStatement[])statements.toArray(new
          ParametricStatement[0]));
      return true;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      return false;
    }
  }

  /**
   * persist the register object in data base
   * @param transactionSOS
   * @exception Exception
   */
  private void persistRegister(TransactionSOS transactionSOS)
      throws Exception {
    Register register = transactionSOS.getRegister();
    if (register != null) {
      registerDAO.update(register);
    }
  }
}

