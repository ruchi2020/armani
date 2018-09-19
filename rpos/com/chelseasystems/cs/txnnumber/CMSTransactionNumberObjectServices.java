/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.txnnumber;

import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.txnnumber.TransactionNumberStore;
import com.chelseasystems.cs.pos.CMSTransactionPOSJDBCServices;


/**
 *
 */
public class CMSTransactionNumberObjectServices extends CMSTransactionNumberFileServices {
  /**
   * This is the key to the txn number in the global repository.  It also
   * winds up as the name of the file when the TransactionNumber object is
   * serialized.
   */
  public static final String TXN_NUMBER = "TXN_NUMBER";

  /**
   *
   */
  public void init() {
    delim = config.getString(DELIM);
    //    if (null == delim) {
    //      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()",
    //          "Cannot find the delimiter to use in building a transaction number.",
    //          "Make sure txnnumber.cfg contains a line like " + DELIM +
    //          "=x where x is the character to use" +
    //          " between parts of the transaction number.",
    //          LoggingServices.CRITICAL);
    //    }
  }

  /**
   * Create a client-side transaction number consisting of a store number,
   * register number and sequence number, separated by a delimiter character.
   * The combination of store, register and sequence number must be unique.
   * Sequence numbers should always be one more that the last one used (no
   * missing sequence numbers).
   * @param theStore must be the CURRENT store (the store for which this register was opened)
   * @param theRegister must be the CURRENT register
   * @return String a unique client-side transaction number consisting of a
   *                store number, register number and sequence number separated
   *                by a delimiter character.
   **/
  public synchronized String getNextTransactionNumber(Store theStore, Register theRegister)
      throws Exception {
    Integer sequenceNumber = new Integer(0);
    try {
      TransactionNumberStore transactionNumber = getTransactionNumberStore();
      sequenceNumber = transactionNumber.getSequenceNumber();
      transactionNumber.setSequenceNumber(new Integer(sequenceNumber.intValue() + 1));
      saveTransactionNumberStore(transactionNumber);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getNextTransactionNumber()"
          , "Unable to read or write transaction number from repository."
          , "Make sure all files in repository are read/writable", LoggingServices.CRITICAL, e);
      throw e;
    }
    StringBuffer buffer = new StringBuffer(theStore.getId());
    buffer.append(delim);
    buffer.append(theRegister.getId());
    buffer.append(delim);
    buffer.append(sequenceNumber);
    return buffer.toString();
  }

  /**
   * Return the next available sequence number without changing it!
   * This is most probably used by the end-of-day function to ensure that there
   * were no missing transactions during the day.
   * @return String the next available sequence number, or zero for default.
   **/
  public synchronized String peekSequenceNumber()
      throws Exception {
    try {
      TransactionNumberStore transactionNumber = getTransactionNumberStore();
      return transactionNumber.getSequenceNumber().toString();
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "peekSequenceNumber()"
          , "Cannot read transaction number from repository."
          , "Make sure resetSequenceNumber() is called during bootstrap.", LoggingServices.CRITICAL
          , e);
      throw e;
    }
  }

  /**
   * Reset the sequence number to a configurable default value.  This must be
   * done the first time a register is booted up (changing the current store
   * from <code>null</code> to whatever the user typed in during start-of-day) and
   * each time the register is brought up as a different store (when a register
   * is sent from store 1101 to 1113 because one of 1113's registers caught fire).
   * @note this method will default the starting sequence number to 1000.
   */
  public synchronized void resetSequenceNumber()
      throws Exception {
    Integer startingSequenceNumber = config.getInteger(STARTING_TXN_SEQ_NUM);
    if (startingSequenceNumber == null) {
      startingSequenceNumber = new Integer(1000);
    }
    try {
      TransactionNumberStore transactionNumber = getTransactionNumberStore();
      transactionNumber.setSequenceNumber(startingSequenceNumber);
      saveTransactionNumberStore(transactionNumber);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "resetSequenceNumber()"
          , "Cannot write to the file where the next transaction sequence number is stored."
          , "Make sure txnnumber.cfg contains a line like  " + STARTING_TXN_SEQ_NUM
          + "=<integer> where <integer> is the starting sequence number for transactions."
          , LoggingServices.CRITICAL, e);
      throw e;
    }
  }

  /**
   * Reset the sequence number to the specified (presumably from GUI) value.
   */
  public synchronized void resetSequenceNumber(int nextSequenceNumber)
      throws Exception {
    Integer startingSequenceNumber = new Integer(nextSequenceNumber);
    try {
      TransactionNumberStore transactionNumber = getTransactionNumberStore();
      transactionNumber.setSequenceNumber(startingSequenceNumber);
      saveTransactionNumberStore(transactionNumber);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "resetSequenceNumber()"
          , "Cannot write to the file where the next transaction sequence number is stored."
          , "Make sure txnnumber.cfg contains a line like  " + STARTING_TXN_SEQ_NUM
          + "=<integer> where <integer> is the starting sequence number for transactions."
          , LoggingServices.CRITICAL, e);
      throw e;
    }
  }

  /**
   *
   * @return
   */
  private TransactionNumberStore getTransactionNumberStore()
      throws Exception {
    TransactionNumberStore transactionNumber = (TransactionNumberStore)BrowserManager.getInstance().
        getGlobalObject(TXN_NUMBER);
    if (transactionNumber == null) {
      transactionNumber = new TransactionNumberStore();
      saveTransactionNumberStore(transactionNumber);
    }
    return transactionNumber;
  }

  /**
   * put your documentation comment here
   * @param transactionNumber
   * @exception Exception
   */
  private void saveTransactionNumberStore(TransactionNumberStore transactionNumber)
      throws Exception {
    BrowserManager.getInstance().addGlobalObject(TXN_NUMBER, transactionNumber, true);
  }
}

