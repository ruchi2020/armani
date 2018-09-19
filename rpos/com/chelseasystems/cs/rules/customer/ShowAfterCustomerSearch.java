/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.rules.customer;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.appmgr.menu.*;
import com.chelseasystems.cr.swing.CMSApplet;

/* History:-
+--------+------------+-----------+------------------+------------------------------+
| Ver#   |    Date    |   By      |        Defect #  |              Description     |
+--------+------------+-----------+------------------+------------------------------+
| 1      | 03-15-2005 | Manpreet  | Original         | Original Version per spec    |
+--------+------------+-----------+------------------+------------------------------+
*/
/**
* <p>Title: ShowAfterCustomerSearch</p>
*
* <p>Description: This business rule check whether customer is referred or not,
* if customer is referred then will not allow to add new customer</p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company:SkillNet Inc. </p>
*
* @author Manpreet S Bawa
* @version 1.0
*/
public class ShowAfterCustomerSearch extends Rule {

	/**
	 * Default Constructor
	 */
	public ShowAfterCustomerSearch() {
	}

	/**
	 * Execute the rule.
	 * 
	 * @param object
	 *            theParent - instance of CMSMenuOption
	 * @param args -
	 *            instance of Employee
	 * @param args -
	 *            instance of Store
	 * @return RulesInfo
	 */
	public RulesInfo execute(Object theParent, Object[] args) {
		return execute((CMSMenuOption) theParent, (Employee) args[0], (Store) args[1]);
	}

	/**
	 * Execute the Rule
	 * 
	 * @param cmsmenuoption
	 *            theParent
	 * @param employee
	 *            Employee
	 * @param store
	 *            Store
	 * @return RulesInfo
	 */
	private RulesInfo execute(CMSMenuOption cmsmenuoption, Employee employee, Store store) {
		try {
			if ((CMSApplet.theAppMgr.getStateObject("CUST_SEARCH") != null) || (CMSApplet.theAppMgr.isOnLine() != true))
				return new RulesInfo();
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "execute", "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
		}
		return new RulesInfo("First Search a customer and if not found then add New Customer");
	}

	/**
	 * Returns the description of the business rule.
	 * 
	 * @returns description of the business rule.
	 */
	public String getDesc() {
		return ("Suppress menu button if not appropriate.");
	}

	/**
	 * Return the name of the rule.
	 * 
	 * @return name of the rule.
	 */
	public String getName() {
		return "HideIfCustomerIsReferred";
	}
}
