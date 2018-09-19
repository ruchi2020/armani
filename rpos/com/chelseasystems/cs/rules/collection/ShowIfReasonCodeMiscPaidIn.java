/* History:-
 +--------+------------+-----------+------------------+------------------------------+
 | Ver#   |    Date    |   By      |        Defect #  |              Description     |
 +--------+------------+-----------+------------------+------------------------------+
 | 1      | 04-19-2005 | Anand     | Original         | Original Version per spec    |
 +--------+------------+-----------+------------------+------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.collection;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;
import com.chelseasystems.cr.swing.CMSApplet;


/**
 * <p>Title: ShowIfReasonCodeMiscPaidIn</p>
 * <p>Description: This business rule checks whether the reason code is
 * Miscellaneous paid in </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anand
 * @version 1.0
 */
public class ShowIfReasonCodeMiscPaidIn extends Rule {

  /**
   * Default Constructor
   */
  public ShowIfReasonCodeMiscPaidIn() {
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
    return execute((CMSMenuOption)theParent);
  }

  /**
   * Execute the Rule
   * @param cmsmenuoption theParent
   * @return RulesInfo
   */
  private RulesInfo execute(CMSMenuOption cmsmenuoption) {
    try {
      String reasonCode = (String)CMSApplet.theAppMgr.getStateObject("REASON_CODE");
      if (reasonCode != null && reasonCode.equals("MISC_PAID_IN"))
        return new RulesInfo("should NOT be enabled");
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
    return "ShowIfReasonCodeMiscPaidIn";
  }
}

