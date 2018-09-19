/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.collection.*;
import com.chelseasystems.cr.collection.*;
import com.chelseasystems.cs.paidout.*;
import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.layaway.*;
import com.chelseasystems.cr.layaway.*;
import com.chelseasystems.cr.appmgr.*;


/**
 */
public class CMSAppModelFactory extends AppModelFactory {

  /**
   */
  public CMSAppModelFactory() {
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(CompositePOSTransaction txn
      , IApplicationManager theAppMgr) {
    return new CMSCompositePOSTransactionAppModel(txn);
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(PaidOutTransaction txn
      , IApplicationManager theAppMgr) {
    return new CMSPaidOutTransactionAppModel(txn);
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(CollectionTransaction txn
      , IApplicationManager theAppMgr) {
    return new CMSCollectionTransactionAppModel(txn);
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(LayawayPaymentTransaction txn
      , IApplicationManager theAppMgr) {
    return new CMSLayawayPaymentTransactionAppModel(txn, theAppMgr);
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(LayawayRTSTransaction txn
      , IApplicationManager theAppMgr) {
    return new CMSLayawayRTSTransactionAppModel(txn, theAppMgr);
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(VoidTransaction txn
      , IApplicationManager theAppMgr) {
    return new CMSVoidTransactionAppModel(txn);
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(NoSaleTransaction txn
      , IApplicationManager theAppMgr) {
    if (txn instanceof RewardTransaction) {
      return new RewardTransactionAppModel((RewardTransaction)txn);
    } else {
      return new CMSNoSaleTransactionAppModel(txn);
    }
  }

  /**
   * @param txn
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel createPaymentTransactionAppModel(RedeemableBuyBackTransaction
      txn, IApplicationManager theAppMgr) {
    return new CMSRedeemableBuyBackTransactionAppModel(txn);
  }
}

