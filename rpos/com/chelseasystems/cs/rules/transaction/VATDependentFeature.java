/*
 * @copyright (c) 2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;


/**
 * <p>Title: VATDependentFeature</p>
 * <p>Description: This class checks the business rule that the vat dependent
 * feature should NOT be enabled </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author
 * @version 1.0
 */
public class VATDependentFeature extends Rule {
  static boolean isVatEnabled = new ConfigMgr("vat.cfg").getString("VAT_ENABLED").equalsIgnoreCase(
      "TRUE");

  /**
   * Default Constructor
   */
  public VATDependentFeature() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSMenuOption
   * @param args[] - instance of Employee and Store
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return execute((CMSMenuOption)theParent, (Employee)args[0], (Store)args[1]);
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSMenuOption
   * @param args - instance of Employee
   * @param args - instance of Store
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {
    try {
      if (!isVatEnabled)
        return new RulesInfo("vat dependent feature should NOT be enabled");
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
    }
    return new RulesInfo();
  }

  /**
   * Returns the name of the rule.
   * @return the name of the rule
   */
  public String getName() {
    return ("VATDependentFeature");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    return ("Suppress menu button if not appropriate.");
  }
}

