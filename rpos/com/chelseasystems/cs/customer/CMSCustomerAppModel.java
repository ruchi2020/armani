/**
 * Title:        Your Product Name<p>
 * Description:  Decorator for receipt builder, wraps employee business object<p>
 * Copyright:    Copyright (c) 1999<p>
 * Company:      Your Company<p>
 * @author       Dan Reading
 * @version
 */
package com.chelseasystems.cs.customer;

import com.chelseasystems.cr.customer.*;
import java.io.*;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.ResourceBundle;

public class CMSCustomerAppModel extends CustomerAppModel implements Serializable {
	static final long serialVersionUID = -961089241233184366L;
	static ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
	private CMSCustomer customer;

	public CMSCustomerAppModel(CMSCustomer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getLastAndFirstName() {
		ConfigMgr config = new ConfigMgr("JPOS_peripherals.cfg");
		String firstName = res.getString(config.getString("DEFAULT_CUSTOMER.FIRST_NAME"));
		String lastName = res.getString(config.getString("DEFAULT_CUSTOMER.LAST_NAME"));
		if (firstName == null) {
			firstName = "";
		}
		if (lastName == null) {
			lastName = "";
		}
		Customer c = getCustomer();
		return c == null ? lastName + " " + firstName : c.getLastName() + " " + c.getFirstName();
	}

	public String getLastAndFirstName_JP() {
		ConfigMgr config = new ConfigMgr("JPOS_peripherals.cfg");
		String firstName = res.getString(config.getString("DEFAULT_CUSTOMER.FIRST_NAME"));
		String lastName = res.getString(config.getString("DEFAULT_CUSTOMER.LAST_NAME"));
		if (firstName == null) {
			firstName = "";
		}
		if (lastName == null) {
			lastName = "";
		}
		CMSCustomer c = (CMSCustomer) getCustomer();
		String nameString;
		if (c == null) {
			nameString = lastName + " " + firstName;
		} else {
			if (c.getDBLastName() != null) {
				if (c.getDBFirstName() != null) {
					nameString = c.getDBLastName() + " " + c.getDBFirstName();
				} else {
					nameString = c.getDBLastName();
				}
			} else {
				nameString = c.getLastName() + " " + c.getFirstName();
			}
		}
		// return c == null ? lastName+" "+firstName : c.getDBLastName() + " " + c.getDBFirstName();
		return nameString;
	}
}
