/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Specifications.Constructor   |
 |      |            |           |           | added                                              |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.paidout;

import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.customer.CMSCustomer;


/**
 * <p>Title: CMSMiscPaidOut</p>
 * <p>Description: This class is used for Miscellaneous paid out</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Anand
 * @version 1.0
 */
public class CMSMiscPaidOut extends CMSPaidOutTransaction implements com.chelseasystems.cr.rules.
    IRuleEngine {
  static final long serialVersionUID = -7512978247893185674L;
  private CMSCustomer customer = null;

  /**
   * Constructor
   * @param store
   * @param type
   */
  public CMSMiscPaidOut(String type, Store store) {
    super(type, store);
  }

  /**
   * Constructor
   * @param store
   */
  public CMSMiscPaidOut(Store store) {
    super("misc", store);
  }

  /** This method is used to post transaction of Miscellaneous paid out
   * @throw Exception
   */
  public boolean post()
      throws java.lang.Exception {
    return TransactionPOSServices.getCurrent().submit(this);
  }

  /**
   * put your documentation comment here
   * @param customer
   */
  public void setCustomer(CMSCustomer customer) {
    this.customer = customer;
  }

  /**
   * put your documentation comment here
   * @param customer
   */
  public void doSetCustomer(CMSCustomer customer) {
    setCustomer(customer);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSCustomer getCustomer() {
    return customer;
  }
}

