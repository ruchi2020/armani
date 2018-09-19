/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.swing.CMSApplet;
import java.util.ResourceBundle;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cr.swing.AppModelFactory;


/**
 * <p>Title: </p>
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
public class RewardTransaction extends CMSNoSaleTransaction {
  /**
   * Member Variables
   * @param aStore Store
   */
  protected RewardCard rewardCard;
  protected long points;
  private CMSCustomer customer = null;

  /**
   * put your documentation comment here
   * @param   Store aStore
   */
  public RewardTransaction(Store aStore) {
    super(aStore);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTransactionType() {
    return "REWD";
  }

  /**
   * put your documentation comment here
   * @return
   */
  public RewardCard getRewardCard() {
    return this.rewardCard;
  }

  /**
   * put your documentation comment here
   * @param RwdCard
   */
  public void doSetRewardCard(RewardCard RwdCard) {
    this.rewardCard = RwdCard;
    //set the comment
    try {
      ResourceBundle res = ResourceManager.getResourceBundle();
      if (rewardCard != null)
        setComment(res.getString("Reward Card Issued") + " : [" + rewardCard.getControlNum() + "]");
      else
        setComment(res.getString("Loyalty Reward Issue transaction"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param RwdCard
   */
  public void setRewardCard(RewardCard RwdCard) {
    doSetRewardCard(RwdCard);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public long getPoints() {
    return this.points;
  }

  /**
   * put your documentation comment here
   * @param points
   */
  public void doSetPoints(long points) {
    this.points = points;
  }

  /**
   * put your documentation comment here
   * @param points
   */
  public void setPoints(long points) {
    doSetPoints(points);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Customer getCustomer() {
    return customer;
  }

  /**
   * put your documentation comment here
   * @param aCustomer
   * @exception BusinessRuleException
   */
  public void setCustomer(Customer aCustomer)
      throws BusinessRuleException {
    checkForNullParameter("setCustomer", aCustomer);
    if (customer == null || !customer.equals(aCustomer)) {
      executeRule("setCustomer", aCustomer);
      doSetCustomer(aCustomer);
    }
  }

  /**
   * put your documentation comment here
   * @param aCustomer
   */
  public void doSetCustomer(Customer aCustomer) {
    customer = (CMSCustomer)aCustomer;
  }

  /**
   * put your documentation comment here
   * @param anAppModelFactory
   * @param theAppMgr
   * @return
   */
  public PaymentTransactionAppModel getAppModel(AppModelFactory anAppModelFactory
      , IApplicationManager theAppMgr) {
    return ((CMSAppModelFactory)anAppModelFactory).createPaymentTransactionAppModel(this, theAppMgr);
  }
}

