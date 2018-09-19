package com.chelseasystems.cs.rules.customer;

import com.chelseasystems.cr.appmgr.menu.CMSMenuOption;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.util.Version;

/**
 * <p>Title: ShowIfCustomerSelected</p>
 *
 * <p>Description: This business rule checks if POS is Online</p>
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
 | 1      | 06-02-2011 | Manpreet  | SAP-CRM PCR      | Show button if ONLINE mode   |
 +--------+------------+-----------+------------------+------------------------------+
 */

public class ShowIfOnline extends Rule {

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
	    	if(!CMSApplet.theAppMgr.isOnLine())	    	
	    		return new RulesInfo("Sholw in Online Mode");
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
	    return "HideIfCustomerIsNew";
	  }

}
