package com.ga.cs.interfaces;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.sql.SQLException;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.dataaccess.CustomerDAO;

public class GACustomerHelper {
	private ConfigMgr configMgr;
	private CustomerDAO customerDAO;

	public GACustomerHelper() {
		configMgr = new ConfigMgr("jdbc.cfg");
		customerDAO = (CustomerDAO) configMgr.getObject("CUSTOMER_DAO");
	}

	public Customer getCustomer(String id) {

		try {
			Customer existing = customerDAO.selectById(id);
			if (existing != null) {
				return existing;
			}
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg("GACustomerHelper", "submitCustomer", "Exception", "See Exception", LoggingServices.MAJOR, e);
			return null;
		}
		return null;
	}

	public int submitCustomer(CMSCustomer customer) throws SQLException {
		int inserted = 0;

		try {
			Customer existing = customerDAO.selectById(customer.getId());
			if (existing == null) {
				customerDAO.insert(customer);
				inserted++;
			} else {
				customerDAO.update(customer);
			}
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg("GACustomerHelper", "submitCustomer", "Exception", "See Exception", LoggingServices.MAJOR, e);
			//return -1;
			throw new SQLException(e.getMessage());
		}
		return inserted;
	}

}
