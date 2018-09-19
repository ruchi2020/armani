/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Specifications               |
 --------------------------------------------------------------------------------------------------
 | 2    | 08/15/2005 | KS        | N/A       | From Base Global Implemetation                     |
 --------------------------------------------------------------------------------------------------
 | 3    | 08/15/2005 | KS        | N/A       | Extend CMSCollectionTransaction                    |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.collection;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.payment.Redeemable;


/**
 * <p>Title: CMSMiscCollection</p>
 * <p>Description: This class used to handle paid in transaction activities </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class CMSMiscCollection extends CMSCollectionTransaction implements com.chelseasystems.cr.
    rules.IRuleEngine {
  /**
   *
   */
  static final long serialVersionUID = -3370666637920687368L;
  /**
   * This Stores the value of redeemable object
   */
  private Redeemable redeemable;
  /**
   * This Stores the value of customer object
   */
  private CMSCustomer customer;

  /**
   * Constructor
   * @param store Store
   */
  public CMSMiscCollection(Store store) {
    super("misc", store);
  }

  /**
   * Constructor
   * @param type String
   * @param store Store
   */
  public CMSMiscCollection(String type, Store store) {
    super(type, store);
  }

  /**
   * This method used to post the paid in transaction
   * @return boolean
   * @throws Exception
   */
  public boolean post()
      throws java.lang.Exception {
    return TransactionPOSServices.getCurrent().submit(this);
  }

  /**
   * This method used to set the customer value
   * @param customer CMSCustomer
   */
  public void setCustomer(CMSCustomer customer) {
    this.customer = customer;
  }

  /**
   * This method used to set the customer value
   * @param customer CMSCustomer
   */
  public void doSetCustomer(CMSCustomer customer) {
    setCustomer(customer);
  }

  /**
   * This method used to get the customer value
   * @return CMSCustomer
   */
  public CMSCustomer getCustomer() {
    return customer;
  }

  /**
   * This method used to get the value of Redeemable used as paid in
   * @return Redeemable
   */
  public Redeemable getRedeemable() {
    return redeemable;
  }

  /**
   * This method set the value of Redeemable used as paid in
   * @param redeemable Redeemable
   */
  public void setRedeemable(Redeemable redeemable) {
    this.redeemable = redeemable;
  }

  /**
   * This method set the value of Redeemable used as paid in
   * @param redeemable Redeemable
   */
  public void doSetRedeemable(Redeemable redeemable) {
    setRedeemable(redeemable);
  }
}
