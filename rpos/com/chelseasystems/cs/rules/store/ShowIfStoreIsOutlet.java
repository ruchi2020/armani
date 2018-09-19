/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.store;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;


/**
 * <p>Title: ShowIfStoreIsOutlet</p>
 *
 * <p>Description: Show PRICE_OVERRIDE only of store is Outlet.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 03-15-2005 | Manpreet  | Original         | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 */
public class ShowIfStoreIsOutlet extends Rule {
  static boolean isOutLetStore = new ConfigMgr("store.cfg").getString("IS_OUTLET_STORE").
      equalsIgnoreCase("FALSE");

  /**
   * put your documentation comment here
   */
  public ShowIfStoreIsOutlet() {
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
      if (isOutLetStore)
        return new RulesInfo("NOT an Outlet Store should NOT be enabled");
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
    return ("Suppress menu button if not appropriate.");
  }

  /**
   * Return the name of the rule.
   * @return name of the rule.
   */
  public String getName() {
    return "HideIfStoreIsOutlet";
  }
}

