/*
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.register;

import com.chelseasystems.cr.register.RegisterSessionAppModel;
import com.chelseasystems.rb.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.IReceiptAppManager;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.sos.*;


/**
 *
 * <p>Title: CMSRegisterSessionAppModel</p>
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
public class CMSRegisterSessionAppModel extends RegisterSessionAppModel {
  CMSTransactionSOS txnSOS = null;

  /**
   * Default Constructor
   */
  public CMSRegisterSessionAppModel() {
  }

  /**
   *
   * @param receiptBlueprint
   * @param theAppMgr
   */
  public void setSOSTxn(CMSTransactionSOS txn) {
    txnSOS = txn;
  }

  /**
   *
   * @param receiptBlueprint
   * @param theAppMgr
   */
  public String getSOSTransId() {
    if (txnSOS != null) {
      return txnSOS.getId();
    }
    return "";
  }

  /**
   *
   * @param receiptBlueprint
   * @param theAppMgr
   */
  public void print(String receiptBlueprint, IReceiptAppManager theAppMgr) {
    if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
      String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + receiptBlueprint + ".rdo";
      try {
        ObjectStore objectStore = new ObjectStore(fileName);
        objectStore.write(this);
      } catch (Exception e) {
        System.out.println("exception on writing object to blueprint folder: " + e);
      }
    }
    Object[] arguments = {this
    };
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(getStore(), getOpr());
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments, receiptBlueprint);
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
  }
}

