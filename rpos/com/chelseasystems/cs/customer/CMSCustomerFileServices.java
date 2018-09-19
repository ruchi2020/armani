/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.customer;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.xml.*;

import java.io.*;
import java.util.*;

/**
 *
 * <p>Title: CMSCustomerFileServices</p>
 *
 * <p>Description: A simple implementation of CustomerServices that manages a
 *  persistent file-based database of Customer information.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSCustomerFileServices extends CMSCustomerServices {
	static private Vector allCustomers = null;
	static private Vector allCustomerMessages = null;
	static private Vector allCustomerAlertRules = null;
	static private String fileName = FileMgr.getLocalFile("xml", "customers.xml");
	static private String fileNameForMsg = FileMgr.getLocalFile("xml", "customer_messages.xml");
	static private String fileNameForAlert = FileMgr.getLocalFile("xml", "customer_alert_rule.xml");

  /**
   * Reads a file with the given filename and places the lines, each of which
   * contains delimited data for a single customer, into a hashtable for
   * later retrieval.
   **/
	/**
	 * Default constructor
	 */
	public CMSCustomerFileServices() {
		if (allCustomers == null) {
			loadAllCustomersFromFile();
		}
	}

	/**
	 * This method is used to seach a Customer by coustomer id 
	 * @param id String
	 * @return Customer
	 * @throws Exception
	 */
	public Customer findById(String id) throws java.lang.Exception {
		if (id != null) {
			id = id.toUpperCase();
		}
		if (allCustomers == null || allCustomers.size() == 0) {
			return null;
		}
		for (int i = 0; i < allCustomers.size(); i++) {
			if (((CMSCustomer) allCustomers.get(i)).getId().equalsIgnoreCase(id)) {
				return (Customer) ((CMSCustomer) allCustomers.get(i)).clone();
			}
		}
		return null;
	}

	/**
	 * This method is used to search Customer by their last name and postal code
	 * @param lastName String
	 * @param zipCode String
	 * @return Customer[]
	 * @throws Exception
	 */
	public Customer[] findByLastNameZipCode(String lastName, String zipCode) throws java.lang.Exception {
		if (allCustomers == null || allCustomers.size() == 0) {
			return null;
		}
		Vector customerVector = new Vector();
		for (int i = 0; i < allCustomers.size(); i++) {
			if (((CMSCustomer) allCustomers.get(i)).getLastName().equalsIgnoreCase(lastName) && ((CMSCustomer) allCustomers.get(i)).getZipCode().equalsIgnoreCase(zipCode)) {
				customerVector.add(((CMSCustomer) allCustomers.get(i)).clone());
			}
		}
		if (customerVector != null && customerVector.size() > 0) {
			return (Customer[]) (customerVector.toArray(new CMSCustomer[0]));
		} else {
			return null;
		}
	}

	/**
	 * This method is used to search customers by last and first names
	 * param lastName String
	 * @param firstName String
	 * @return Customer[]
	 * @throws Exception
	 */
	public Customer[] findByLastNameFirstName(String lastName, String firstName) throws java.lang.Exception {
		if (allCustomers == null || allCustomers.size() == 0) {
			return null;
		}
		Vector customerVector = new Vector();
		for (int i = 0; i < allCustomers.size(); i++) {
			if (((CMSCustomer) allCustomers.get(i)).getLastName().equalsIgnoreCase(lastName) && ((CMSCustomer) allCustomers.get(i)).getFirstName().equalsIgnoreCase(firstName)) {
				customerVector.add(((CMSCustomer) allCustomers.get(i)).clone());
			}
		}
		if (customerVector != null && customerVector.size() > 0) {
			return (Customer[]) (customerVector.toArray(new CMSCustomer[0]));
		} else {
			return null;
		}
	}

	/**
	 * This method is used to search customers by Barcode
	 * @param Barcode String
	 * @return Customer[]
	 * @throws Exception
	 */
	public Customer[] findByBarcode(String sBarcode) throws java.lang.Exception {
		if (allCustomers == null || allCustomers.size() == 0) {
			return null;
		}
		Vector customerVector = new Vector();
		for (int i = 0; i < allCustomers.size(); i++) {
			if (((CMSCustomer) allCustomers.get(i)).getCustomerBC().equalsIgnoreCase(sBarcode)) {
				customerVector.add(((CMSCustomer) allCustomers.get(i)).clone());
			}
		}
		if (customerVector != null && customerVector.size() > 0) {
			return (Customer[]) (customerVector.toArray(new CMSCustomer[0]));
		} else {
			return null;
		}
	}

	/**
	 * This method is used to load customer object
	 * @param id String
	 * @return Customer
	 * @throws Exception
	 */
	public Customer loadCustomer(String id) throws java.lang.Exception {
		return findById(id);
	}

	/**
	 * This method is used to search customers by a telephone number
	 * @param phone String
	 * @return Customer[]
	 * @throws Exception
	 */
	public Customer[] findByPhone(String phone) throws java.lang.Exception {
		return null;
		// if (allCustomers == null || allCustomers.size() == 0) return null;
		//
		// Vector customerVector = new Vector();
		// for (int i = 0; i < allCustomers.size(); i++)
		// if (((CMSCustomer)allCustomers.get(i)).getPhone().equalsIgnoreCase(phone) || ((CMSCustomer)allCustomers.get(i)).getAlternatePhone().equalsIgnoreCase(phone))
		// customerVector.add(((CMSCustomer)allCustomers.get(i)).clone());
		//
		// if (customerVector != null && customerVector.size() > 0)
		// return (Customer[])customerVector.toArray(new CMSCustomer[0]);
		// else
		// return null;
	}

	/**
	 * This method is used to search customers by a telephone
	 * @param telephone Telephone
	 * @return Customer[]
	 * @throws Exception
	 */
	public Customer[] findByTelephone(Telephone telephone) throws java.lang.Exception {
		return null;
	}

	/**
	 * This method is used to search customers matching search string
	 * @param searchStr String
	 * @return Customer[]
	 * @throws Exception
	 */
	public Customer[] findBySearchQuery(String searchStr) throws Exception {
		return null;
	}

	/**
	 * Lookup customer based on advance search criteria
	 * @param searchStr CustomerSearchString
	 * @throws Exception
	 * @return Customer[]
	 */
	public Customer[] findBySearchQuery(CustomerSearchString searchStr) throws Exception {
		return null;
	}

	/**
	 * This method is used to search customers on the basis of reward card id
	 * @param rcId String
	 * @return Customer[]
	 * @throws Exception
	 */
	public Customer[] findByRewardCard(String rcId) throws Exception {
		return null;
	}

	/**
	 * This method is used to get customer's sale summary on the basis of customer id for a given store
	 * 
	 * @param custId String
	 * @param storeId String
	 * @return CustomerSaleSummary[]
	 * @throws Exception
	 */
	public CustomerSaleSummary[] getCustSaleSummary(String custId, String storeId) throws Exception {
		return null;
	}

	/**
	 * This method is used to get customer's Deposit 
     * customer id for a given store
	 * @throws Exception
	 */
	public DepositHistory[] getDepositHistory(String custId) throws Exception {
		return null;
	}

	/**
	 * This method is used to get a default customer
	 * @return Customer
	 * @throws Exception
	 */
	public Customer getDefaultCustomer() throws java.lang.Exception {
		if (allCustomers != null) {
			for (int i = 0; i < allCustomers.size(); i++) {
				if (((CMSCustomer) allCustomers.get(i)).getTelephone().equals(Customer.UNKNOWN_CUSTOMER_PHONE)) {
					return (CMSCustomer) allCustomers.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * This method is used to presist customer to data store
	 * @param customer Customer
	 * @throws Exception
	 */
	public void submit(Customer customer) throws java.lang.Exception {
		submitAndReturn(customer);
	}

	/**
	 * This method is used to Submit Customer and return it to client.
	 * @param customer Customer
	 * @return Customer
	 */
	public Customer submitAndReturn(Customer customer) throws Exception {
		customer.doSetLastName(customer.getLastName().toUpperCase());
		customer.doSetFirstName(customer.getFirstName().toUpperCase());
		if (customer.isNew()) {
			customer.doSetId(Long.toString(System.currentTimeMillis()));
		}
		if ((customer).isNew() || customer.isModified()) {
			for (int i = 0; i < allCustomers.size(); i++) {
				if (((CMSCustomer) allCustomers.get(i)).getId().equals(customer.getId())) {
					allCustomers.remove(i);
					break;
				}
			}
			customer.setExisting();
			customer.setUnmodified();
			allCustomers.add(customer);
		}
		String xml = new CustomerXML().toXML(allCustomers);
		FileWriter fileWriter = new FileWriter(fileName);
		fileWriter.write(xml);
		fileWriter.close();
		return customer;
	}

	/**
	 * This method is used to merge two Customers
	 * @param sourceCustomer Customer
	 * @param targetCustomer Customer
	 * @return boolean
	 * @throws Exception
	 */
	public boolean merge(Customer sourceCustomer, Customer targetCustomer) throws java.lang.Exception {
		return false;
	}

	/**
	 * This method is used to load all customer from local file
	 */
	private void loadAllCustomersFromFile() {
		if (fileName == null || fileName.equals("")) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Missing data file name.", "Make sure the file is there.", LoggingServices.CRITICAL);
			System.exit(1);
		}
		try {
			allCustomers = new CustomerXML().toObjects(fileName);
		} catch (org.xml.sax.SAXException saxException) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot parse the customer data file.", "Verify the integrity of the customer data file", LoggingServices.CRITICAL);
			System.exit(1);
		} catch (IOException e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer data file.", "Verify the integrity of the customer data file", LoggingServices.CRITICAL);
			System.exit(1);
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer data file.", "Unknow exception: " + e, LoggingServices.CRITICAL);
			System.exit(1);
		}
	}

	/**
	 * This method is used to load all customer messages from local file
	 */
	public CMSCustomerMessage[] getAllCustomerMessages() {
		if (fileNameForMsg == null || fileNameForMsg.equals("")) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Missing data file name.", "Make sure the file is there.", LoggingServices.CRITICAL);
			System.exit(1);
		}
		try {
			allCustomerMessages = new CustomerMessageXML().toObjects(fileNameForMsg);
			Object obj[] = allCustomerMessages.toArray();
			CMSCustomerMessage[] cmsCustMsg = new CMSCustomerMessage[obj.length];
			for (int i = 0; i < obj.length; i++) {
				cmsCustMsg[i] = (CMSCustomerMessage) obj[i];
			}
			return (cmsCustMsg);
		} catch (org.xml.sax.SAXException saxException) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot parse the customer data file.", "Verify the integrity of the customer data file", LoggingServices.CRITICAL);
			System.exit(1);
		} catch (IOException e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer data file.", "Verify the integrity of the customer data file", LoggingServices.CRITICAL);
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer data file.", "Unknow exception: " + e, LoggingServices.CRITICAL);
			System.exit(1);
		}
		return null;
	}

	/**
	 * put your documentation comment here
	 * @param customerId
	 * @param storeId
	 * @return
	 * @exception Exception
	 */
	public DepositHistory[] getDepositHistory(String customerId, String storeId) throws Exception {
		return null;
	}

	public CreditHistory[] getCreditHistory(String customerId, String storeId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is used to get transaction data by employee rule
	 * 
	 * @param customerId
	 * @param countryCode
	 * @return Map
	 */
	public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode) throws Exception {
		return null;
	}

	/**
	   * This method is used to get transaction data by employee rule 
	   * @param customerId
	   * @param countryCode
	   * @param discountLevel
	   * @param id_brand
	   * @author vivek.sawant
	   * @return Map
	   * method Updated : Vishal Yevale : PCR for Employee budget threshold for multi level discount 29th May 2017 (EUROPE)
	   */
	public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode, CMSCustomerAlertRule rules[], String id_brand) throws Exception {
		return null;
	}

	/**
	 * This method is used to get customer alert rules
	 * 
	 * @param countryCode
	 */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode) {
		if (fileNameForAlert == null || fileNameForAlert.equals("")) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Missing customer alert rule file name.", "Make sure the file is there.", LoggingServices.CRITICAL);
			System.exit(1);
		}
		try {
			Date processDate = null;
			if(CMSApplet.theAppMgr != null){
				processDate = (Date) CMSApplet.theAppMgr.getGlobalObject("PROCESS_DATE");
			}else{
				processDate = new Date();
			}
			allCustomerAlertRules = new CustomerAlertRuleXML().toObjects(fileNameForAlert);
			Object obj[] = allCustomerAlertRules.toArray();
			CMSCustomerAlertRule[] cmsCustAlertRule = new CMSCustomerAlertRule[obj.length];
				
			for (int i = 0, j = 0; i < obj.length; i++) {
				 if(processDate.compareTo(((CMSCustomerAlertRule)obj[i]).getEndDate()) == 1){
					  continue;
				 }
				 cmsCustAlertRule[j++] = (CMSCustomerAlertRule) obj[i];
			}
			return (cmsCustAlertRule);
		}  catch (org.xml.sax.SAXException saxException) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot parse the customer alert rule file.", "Verify the integrity of the customer alert rule file", LoggingServices.CRITICAL);
		} catch (IOException e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer data file.", "Verify the integrity of the customer alert rule file", LoggingServices.CRITICAL);
		} catch (Exception e) {
			e.printStackTrace();
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer alert rule file.", "Unknow exception: " + e, LoggingServices.CRITICAL);
		}
		return null;
	}
	

	  /**
	   * This method is used to get transaction data by employee rule 
	   * @param countryCode
	   * @param discountLevel
	   * @param id_brand
	   * @author vivek.sawant
	   * @return CMSCustomerAlertRule[]
	   */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID) {
		if (fileNameForAlert == null || fileNameForAlert.equals("")) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Missing customer alert rule file name.", "Make sure the file is there.", LoggingServices.CRITICAL);
			System.exit(1);
		}
		try {
			Date processDate = null;
			if(CMSApplet.theAppMgr != null){
				processDate = (Date) CMSApplet.theAppMgr.getGlobalObject("PROCESS_DATE");
			}else{
				processDate = new Date();
			}
			allCustomerAlertRules = new CustomerAlertRuleXML().toObjects(fileNameForAlert);
			Object obj[] = allCustomerAlertRules.toArray();
			CMSCustomerAlertRule[] cmsCustAlertRule = new CMSCustomerAlertRule[obj.length];
				
			for (int i = 0, j = 0; i < obj.length; i++) {
				 if(processDate.compareTo(((CMSCustomerAlertRule)obj[i]).getEndDate()) == 1){
					  continue;
				 }
				 cmsCustAlertRule[j++] = (CMSCustomerAlertRule) obj[i];
			}
			return (cmsCustAlertRule);
		}  catch (org.xml.sax.SAXException saxException) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot parse the customer alert rule file.", "Verify the integrity of the customer alert rule file", LoggingServices.CRITICAL);
		} catch (IOException e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer data file.", "Verify the integrity of the customer alert rule file", LoggingServices.CRITICAL);
		} catch (Exception e) {
			e.printStackTrace();
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSCustomerFileServices()", "Cannot process the customer alert rule file.", "Unknow exception: " + e, LoggingServices.CRITICAL);
		}
		return null;
	}
	  /**
	   * This method is used to get Expiry Date of current Membership ID 
	   * @param membershipNo
	   * @param brand_ID
	   * @author vivek.sawant
	   * @return CMSVIPMembershipDetail
	   */
	  public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID)  throws Exception {
		  return null;
	  }

	  /**
	   * This method return Membership Id depends on Customer ID 
	   * @param Customer ID
	   * @author vivek.sawant
	   * @return String
	   */
	public String getVIPMembershipID(String customerID) throws Exception {
		return null;
	}
	  /**
	   * This method return date of creation/modify of customer 
	   * @param Customer ID
	   * @author vivek.sawant
	   * @return Date
	   */
		
		public Date getCustomerCreatationDate(String customerID)throws Exception{
		return null;
		}

		/**
		 * 
		 * SAP CRM
		 * 
		 */
		
		public Customer[] selectNewCustomersForStore(String storeId)throws Exception{
			if (allCustomers == null || allCustomers.size() == 0) {
				return null;
			}
			Vector customerVector = new Vector();
			for (int i = 0; i < allCustomers.size(); i++) {
				if (((CMSCustomer) allCustomers.get(i)).getStoreId().equalsIgnoreCase(storeId)) {
					customerVector.add(((CMSCustomer) allCustomers.get(i)).clone());
				}
			}
			if (customerVector != null && customerVector.size() > 0) {
				return (Customer[]) (customerVector.toArray(new CMSCustomer[0]));
			} else {
				return null;
			}
		}

		@Override
		public String[] getCardToken(String customerId) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		 /**
		   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
		   */
		@Override
		public Customer[] findBySearchQuery(String searchStr,
				boolean isFranchisingStore) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}


        /**
		   * vishal : This method is used to get CustomerAlertRule
		   */
		@Override
		public CMSCustomerAlertRule[] getAllCustomerAlertRules(
				String countryCode, String brandID, java.sql.Date businessDate)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
}
