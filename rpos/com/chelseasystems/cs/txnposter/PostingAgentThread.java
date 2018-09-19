/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.txnposter;

import java.io.*;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.transaction.ITransaction;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;


/**
 * Thread that monitors for pending transactions and attempts
 * to post any pending transactions.
 */
public class PostingAgentThread extends Thread {

  /** directory where the pending transaction are located */
  private String theDir;

  /** prefix for transactions */
  static final String TXNPFX = "Txn";
  private CMSTxnPosterSyncServices txnPoster;

  /** flag whether thread is activitly posting pending txns */
  private boolean bActive = false;

  //private StrandedTxnThread strandedThread = null;
  /**
   * constructor
   */
  public PostingAgentThread(String sDir, CMSTxnPosterSyncServices txnPoster) {
    theDir = sDir;
    this.txnPoster = txnPoster;
    //strandedThread = new  StrandedTxnThread(this, theDir);
  }

  /**
   * run method for thread. Processes pending txns and suspends itself
   */
  public void run() {
    while (true) {
      try {
        processPendingTxns();
        System.out.println("*** Suspend ProcessingAgentThread");
        this.suspend();
      } catch (DowntimeException downExp) {
        System.err.println("PostingAgentThread: DowntimeException->" + downExp);
        downExp.printStackTrace();
        this.suspend();
      } catch (Exception ex) {
        System.err.println("PostingAgentThread:  ERROR run()->" + ex);
        ex.printStackTrace();
      }
    }
  }

  /**
   * Post any pending persistent requests on to the
   * the network services layer. This request will attempt
   * to post all pending requests from oldest to newest,
   * and will return only when either no more remaing *or*
   * a request fails. It is here that we carefully attempt
   * to remove each pending txn when it succeeds (else we
   * end redundantly and indefinitely re-posting the same
   * transaction).
   */
  public synchronized void processPendingTxns()
      throws Exception {
    System.out.println("PostingAgentThread:  Starting posting process...");
    bActive = true;
    ITransaction next;
    while ((next = readNextPendingTxn()) != null) {
    	System.out.println("Calling sleep for 1 second");
      this.sleep(1000);
      System.out.println("Thread released");
      System.out.print("PostingAgentThread:  executing:");
      if (!txnPoster.sendTransaction(next)) {
        System.out.println("PostingAgentThread:  Execution failed on " + next.getId());
        this.sleep(2500); // 2.5 seconds
      }
    } // end while
    bActive = false;
  }

  /**
   * filters the files based on prefix
   */
  private final FilenameFilter filterNameAsTXN = new FilenameFilter() {

    /**
     * put your documentation comment here
     * @param file
     * @param fn
     * @return
     */
    public boolean accept(File file, String fn) {
      return fn.startsWith(TXNPFX);
    }
  };

  /**
   * Return the oldest pending transaction from the
   * persistent txn queue.
   */
  private synchronized ITransaction readNextPendingTxn() {
    File file = new File(theDir);
    String[] fns = file.list(); //filterNameAsTXN);
    long oldest = Long.MAX_VALUE;
    int oldest_index = -1;
    int numTxnFiles = fns == null ? 0 : fns.length;
    for (int i = 0; i < numTxnFiles; i++) {
      long next = 0;
      // check for invalid name of file, if caught, move to broken
      try {
        next = Long.parseLong(fns[i].substring(3));
      } catch (Exception ex) {
        moveBrokenFile(fns[i]);
        next = Long.MAX_VALUE;
      }
      if (next < oldest) {
        oldest = next;
        oldest_index = i;
      }
    }
    return oldest_index < 0 ? null : read(fns[oldest_index]);
  }

  /**
   * Move file to broken directory and remove from pending
   */
  private void moveBrokenFile(String fileName) {
    try {
      File file = new File(theDir + File.separator + fileName);
      ConfigMgr mgr = new ConfigMgr("txnposter.cfg");
      String brokenDir = mgr.getString("BROKEN_DIRECTORY");
      java.io.File dir = new java.io.File(FileMgr.getLocalDirectory(brokenDir));
      if (!dir.exists()) {
        dir.mkdir();
      }
      ObjectStore readObj = new ObjectStore(file);
      Object obj = readObj.read();
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile(brokenDir, fileName));
      store.write(obj);
      file.delete();
    } catch (Exception ex) {
      System.err.println("Exception moveBrokenFile()->" + ex);
    }
  }

  /**
   * get number of pending txns
   */
  public int getPendingTxnsNum() {
    File file = new File(theDir);
    String[] fns = file.list();
    return fns.length;
  }

  /**
   * check to see if process if active
   */
  public boolean isActive() {
    return bActive;
  }

  /**
   * Return from the persistent queue the pending txn
   * with the given id. This does *not* delete the txn.
   */
  private synchronized ITransaction read(String fn) {
    ITransaction pending = null;
    try {
      FileInputStream fs = new FileInputStream(txnPoster.txnFileName(fn));
      ObjectInputStream os = new ObjectInputStream(fs);
      pending = (ITransaction)os.readObject();
      os.close();
    } catch (Exception e) {
      System.out.println("ERROR read()-> " + e);
    }
    return pending;
  }
}

