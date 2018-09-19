/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.pos;

import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.customer.CMSCustomer;


/**
 * <p>Title: IsCustomerRequired</p>
 *
 * <p>Description: This business rule checks the customer requirement </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class IsCustomerRequired extends Rule {

  /**
   * put your documentation comment here
   */
  public IsCustomerRequired() {
  }

  /**
   * Execute the rule.
   *
   * @param object CountryName
   * @param objectArray CMSCustomer
   * @return RulesInfo
   *
   */
  public RulesInfo execute(Object sCountry, Object[] objectArray) {
    return execute((String)sCountry, (CMSCustomer)objectArray[0]);
  }

  /**
   * Execute the rule.
   * @param sCountry Country
   * @param cust CMSCustomer
   * @return RulesInfo
   */
  public RulesInfo execute(String sCountry, CMSCustomer cust) {
    try {
      if (cust.getCountry().equalsIgnoreCase(sCountry))
        return new RulesInfo("Customer is Required");
      return new RulesInfo();
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return new RulesInfo();
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return "";
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return "";
  }
}

