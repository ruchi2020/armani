/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.txnposter;

import java.io.*;
import java.util.Date;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.transaction.ITransaction;


/**
 *  A client implementations for TxnPersistentServices that
 *  uses a local harddrive to store the transactions and post
 *  the transactions in a synchronized fashion
 *
 * @author John Gray
 * @version 1.0a
 */
public class CMSTxnPosterSyncObjectServices extends CMSTxnPosterSyncServices {

  /** directory where pending transactions are located */
  private String theDir = null;

  /** prefix for all transaction */
  static final String TXNPFX = "Txn";
  private ConfigMgr config = null;

  /** Number of times to try to connect to RMIServerImpl **/
  private int maxTries = 1;

  /** reference to PostingAgentThread */
  private PostingAgentThread agent = null;

  /**
   * constructor
   */
  public CMSTxnPosterSyncObjectServices()
      throws DowntimeException {
    config = new ConfigMgr("txnposter.cfg");
    this.theDir = FileMgr.getLocalDirectory(config.getString("PENDING_DIRECTORY"));
    System.out.println("CMSTxnPosterSyncObjectServices()->" + theDir);
    agent = new PostingAgentThread(theDir, this);
    System.out.println("Just before starting the posting agent thread  :"+System.currentTimeMillis());
    agent.start();
    init();
  }

  /**
   * Set up the RMI connection to the server.
   **/
  private void init()
      throws DowntimeException {
    try {
      NetworkMgr mgr = new NetworkMgr("network.cfg");
      maxTries = mgr.getRetryAttempts();
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the txnposter.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * synchronous process for submiting and posting transations
   * to server
   *
   * Step 1: Submit on transactions is called
   * Step 2: Serialized verions of pending object is stored
   *         on harddrive
   * Step 3: Check to see of offline thread is active,
   *         if active, exit routine
   * Step 4: Execute transaction
   * Step 5: If transaction successful, Delete pertisted txn file
   * Step 6: else, start offline thread
   *
   * @param txn  a transaction
   */
  public boolean post(ITransaction txn)
      throws Exception {
    // The following initialization of TxnNetworkServices should be placed in
    // a central class and driven from a configuration file (TBD).
    LoggingServices.getCurrent().logMsg("Submitting Transaction..." + txn.getId());
    LoggingServices.getCurrent().logMsg("Ruchi printing time    :"+System.currentTimeMillis());
    store(txn);
    if (notifyAgentThread()) {
      return true;
    }
    return sendTransaction(txn);
  }

  /**
   */
  public boolean repostBrokenTransactions()
      throws Exception {
    // not multithreading this method because the agent thread might wake up
    // before the requeue process has a chance to finish.
    new RequeueBrokenTransactionProcess().run();
    return notifyAgentThread();
  }

  /**
   * check for active offline thread
   */
  private boolean notifyAgentThread() {
    if (agent.getPendingTxnsNum() > 0) {
      if (!agent.isActive()) {
        agent.resume();
      }
      return true;
    }
    return false;
  }

  /**
   * sends transaction across network
   */
  protected boolean sendTransaction(ITransaction txn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      try {
        if (txn.post()) {
          removeFromQueue(txn);
          if (!AppManager.getInstance().isOnLine())
            AppManager.getInstance().setOnLine(true);
          return true;
        }
        CMSTxnPosterHelper.persistBrokenTxn(txn);
        System.out.println("CMSTxnPosterSyncObjectServices.persistBrokenTxn()!!!!!!!!!!!!!!!!!!");
        removeFromQueue(txn);
        return false;
      } catch (DowntimeException downExp) {
        //take the system offline
        AppManager.getInstance().setOnLine(false);
        LoggingServices.getCurrent().logMsg(getClass().getName(), "sendTransaction"
            , "Unable to post Tranaction.  Going offline", "See Exception", LoggingServices.MAJOR
            , downExp);
        throw downExp;
      } catch (Exception ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "sendTransaction"
            , "Unable to post Tranaction", "Determine what transaction did not post"
            , LoggingServices.MAJOR, ex);
        return false;
      }
    }
    throw new DowntimeException("Unable to establish connection to txnposter services");
  }

  /**
   * Store the given pending transaction in the persistent
   * queue. No checks for duplicates are made.
   * @param txn  a transaction
   */
  private synchronized void store(ITransaction txn)
      throws Exception {
    File dir = new File(theDir);
    if (dir.mkdirs()) // TBD overhead to check each time?
      System.out.println("Txn directory " + theDir + " created");
    // String fn = txn.getId();
    Date submitDate = new Date();
    txn.setSubmitDate(submitDate);
    long fnl = submitDate.getTime();
    String fn = TXNPFX + fnl;
    try {
      FileOutputStream fs = new FileOutputStream(txnFileName(fn));
      ObjectOutputStream os = new ObjectOutputStream(fs);
      os.writeObject(txn);
      os.close();
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "store"
          , "Unable to store Tranaction", "Determine what transaction did not post"
          , LoggingServices.MAJOR, e);
      throw new Exception("TxnMgr store failure: " + e);
    }
  }

  /**
   * Remove the given pending txn from the persistent queue.
   * @param txn  a transaction
   */
  private void removeFromQueue(ITransaction txn) {
    String fn = txnFileName(TXNPFX + txn.getSubmitDate().getTime());
    System.out.println("Removeing from queue: " + fn);
    File file = new File(fn);
    if (!file.delete()) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "removeFromQueue"
          , "Unable to remove Tranaction", "Determine why program in unable to delete file."
          , LoggingServices.MAJOR);
      throw new InternalError("TxnMgr failure: Could not delete txn file " + fn);
    }
  }

  /**
   * get file name, uses correct file seperator
   * @param fn a File Name
   */
  protected String txnFileName(String fn) {
    return theDir + File.separatorChar + fn;
  }
}

