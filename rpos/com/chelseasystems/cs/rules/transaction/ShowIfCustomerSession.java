package com.chelseasystems.cs.rules.transaction;
/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;



/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 12-27-2005 | Manpreet  | PCR # 59         | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 */
/**
 * <p>Title: ShowIfReservationsOpen</p>
 *
 * <p>Description: This business rule check whether Customer needs to login after posting
 *  transaction or not. </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ShowIfCustomerSession extends Rule {

  /**
   * Default Constructor
   */
  public ShowIfCustomerSession() {
  }

  /**
   * Execute the rule.
   *
   * @param object theParent - instance of CMSMenuOption
   * @param args - instance of Employee
   * @param args - instance of Store
   * @return RulesInfo
   */
  public RulesInfo execute(Object theParent, Object[] args) {
    return execute((CMSMenuOption)theParent, (Employee)args[0], (Store)args[1]);
  }

  /**
   * Execute the Rule
   * @param cmsmenuoption theParent
   * @param employee Employee
   * @param store Store
   * @return RulesInfo
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {
    try {
      ConfigMgr config = new ConfigMgr("client_master.cfg");

      if (config != null && config.getString("CASHIER_SESSION")!=null) {
        if(!config.getString("CASHIER_SESSION").equalsIgnoreCase("TRUE"))
          return new RulesInfo("Hide Button");
      }
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      return new RulesInfo("Cant print");
    }
    return new RulesInfo();
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return ("Suppress menu button if not appropriate.");
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return "ShowIfCustomerSession";
  }
}


