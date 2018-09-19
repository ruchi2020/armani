/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.eod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.eod.TransactionEOD;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cs.dataaccess.ArmStgTxnHdrDAO;
import com.chelseasystems.cs.dataaccess.EodDAO;
import com.chelseasystems.cs.dataaccess.RegisterDAO;
import com.chelseasystems.cs.dataaccess.StoreDAO;
import com.chelseasystems.cs.dataaccess.TransactionDAO;


/**
 * put your documentation comment here
 */
public class CMSTransactionEODJDBCServices extends CMSTransactionEODServices {
  private EodDAO eodDAO;
  private TransactionDAO transactionDAO;
  private StoreDAO storeDAO;
  private RegisterDAO registerDAO;
  private ArmStgTxnHdrDAO armStgTxnHdrDAO;

  /**
   * Default Constructor
   */
  public CMSTransactionEODJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    eodDAO = (EodDAO)configMgr.getObject("EOD_DAO");
    transactionDAO = (TransactionDAO)configMgr.getObject("TRANSACTION_DAO");
    storeDAO = (StoreDAO)configMgr.getObject("STORE_DAO");
    registerDAO = (RegisterDAO)configMgr.getObject("REGISTER_DAO");
    armStgTxnHdrDAO = (ArmStgTxnHdrDAO)configMgr.getObject("ARMSTGTXNHDR_DAO");
  }

  /**
   * This method id used to find EOD transaction on the basis of transaction id
   * @param id String
   * @return TransactionEOD
   * @throws Exception
   */
  public TransactionEOD findById(String id)
      throws java.lang.Exception {
    try {
      return eodDAO.selectById(id);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "XXXXXX", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to persist EOD transaction and register
   * @param transactionEOD TransactionEOD
   * @return boolean
   * @throws Exception
   */
  public boolean submit(TransactionEOD transactionEOD)
      throws java.lang.Exception {
    List statements = new ArrayList();
    transactionEOD.doSetPostDate(new Date());
    try {
      persistRegister(transactionEOD);
      statements.addAll(Arrays.asList(transactionDAO.getInsertSQL(transactionEOD)));
      statements.addAll(Arrays.asList(armStgTxnHdrDAO.getStgTxnHeaderInsertSQL(transactionEOD)));
      transactionDAO.execute((ParametricStatement[])statements.toArray(new ParametricStatement[0]));
      return true;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "XXXXXX", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      return false;
    } catch (Throwable throwable) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Throwable"
          , "See Exception", LoggingServices.MAJOR, new Exception(throwable.getMessage()));
      return false;
    }
  }

  /**
   * This method is used to persist register
   * @param transactionEOD TransactionEOD
   * @throws Exception
   */
  private void persistRegister(TransactionEOD transactionEOD)
      throws Exception {
    Register register = transactionEOD.getRegister();
    if (register != null) {
      registerDAO.update(register);
    }
  }
}

