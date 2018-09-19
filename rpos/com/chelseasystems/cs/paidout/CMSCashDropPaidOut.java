/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Specifications(No modification|
 |      |            |           |           | from base)
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.paidout;

import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.store.Store;
import java.util.*;
import com.chelseasystems.cs.customer.CMSCustomer;


/**
 * <p>Title: CMSCashDropPaidOut</p>
 * <p>Description: This class is used for cash paid out</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Anand
 * @version 1.0
 */
public class CMSCashDropPaidOut extends CMSPaidOutTransaction implements com.chelseasystems.cr.
    rules.IRuleEngine {
  public static final long serialVersionUID = -6157811604458577712L;
  //Each Element in the Vector will be:  isocode||description||amount
  protected Vector drops = new Vector();
  protected double travChecks;
  protected double moneyOrders;
  private CMSCustomer customer = null;

  /**
   * Constructor
   * @param store
   * @param type
   */
  public CMSCashDropPaidOut(String type, Store store) {
    super(type, store);
  }

  /**
   *Constructor
   * @param store
   */
  public CMSCashDropPaidOut(Store store) {
    super("drop", store);
  }

  /** This method is used to post transaction of cash paid out
   * @throw Exception
   */
  public boolean post()
      throws java.lang.Exception {
    return (TransactionPOSServices.getCurrent().submit(this));
  }

  /** This method is used to add currency drop
   * @param code String
   * @param description String
   * @param amount
   */
  public void addCurrencyDrop(String code, String description, double amount) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(code);
    buffer.append("||");
    buffer.append(description);
    buffer.append("||");
    buffer.append(Double.toString(amount));
    drops.add(buffer.toString());
  }

  /** This method is used to get the currency drop on the basis of  iso code and discription
   * @param code String
   * @param description String
   */
  public String[] getCurrencyDropByISOAndDescription(String code, String description) {
    return (searchDropFor(code + "||" + description));
  }

  /** This method is used to get the currency drop on the basis of iso code
   * @param code String
   * @param description String
   */
  public String[] getCurrencyDropByISO(String code) {
    return (searchDropFor(code));
  }

  /** This method is used to search currency drop on the basis of search criteria
   * @param search search criteria
   */
  private String[] searchDropFor(String search) {
    Vector rtnVal = new Vector();
    for (Iterator it = drops.iterator(); it.hasNext(); ) {
      String current = (String)it.next();
      if (current.startsWith(search)) {
        rtnVal.add(current);
      }
    }
    return ((String[])rtnVal.toArray(new String[rtnVal.size()]));
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

