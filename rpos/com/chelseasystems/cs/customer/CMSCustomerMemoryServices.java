/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.customer.CustomerSearchString;

import java.io.*;
import java.util.*;


/**
 *
 * <p>Title: CMSCustomerMemoryServices</p>
 *
 * <p>Description: A simple implementation of CustomerServices that manages a
 * non-persistent memory-based database of Customer information.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSCustomerMemoryServices extends CMSCustomerServices {
    static private CMSCustomer[] allCustomers;
    private String delim = ",";

    /**
     * Default Constructor
     */
    public CMSCustomerMemoryServices() {
        if (allCustomers != null) {
            return;
        }
        String fileName = FileMgr.getLocalFile("tmp", "CMS_CUSTOMERS");
        if (fileName == null || fileName.equals("")) {
            LoggingServices.getCurrent().logMsg(getClass().getName(),
                                                "CMSCustomerMemoryServices()"
                                                , "Missing data file name."
                                                ,
                                                "Make an entry in cms_customer.cfg like" +
                                                " DATA_FILE=datafilename where datafilename"
                                                +
                                                " is the pathname to the customer data file.",
                                                LoggingServices.CRITICAL);
            System.exit(1);
        }
        Vector customerVector = new Vector();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(
                    fileName)));
            String data = br.readLine();
            while (data != null) {
                StringTokenizer st = new StringTokenizer(data, delim);
                String id = st.nextToken();
                CMSCustomer customer = new CMSCustomer(id);
                //            customer.doSetPhone(st.nextToken());
                //            customer.doSetFirstName(st.nextToken());
                //            customer.doSetLastName(st.nextToken());
                //            customer.doSetAddress(st.nextToken());
                //            customer.doSetApartment(st.nextToken());
                //            customer.doSetCity(st.nextToken());
                //            customer.doSetState(st.nextToken());
                //            customer.doSetZipCode(st.nextToken());
                //            customer.doSetCountry(st.nextToken());
                //            customer.doSetAlternatePhone(st.nextToken());
                //            customer.doSetEMail(st.nextToken());
                //            customer.doSetComment(st.nextToken());
                //            customer.doSetBirthDate(st.nextToken());
                //            customer.doSetPhoneType((new Integer(st.nextToken())).intValue());
                //            customer.doSetReceiveMail( st.nextToken().equals("true")?true:false );
                customerVector.add(customer);
                data = br.readLine();
            }
        } catch (FileNotFoundException e) {
            LoggingServices.getCurrent().logMsg(getClass().getName(),
                                                "CMSCustomerMemoryServices()"
                                                , "Missing data file."
                                                ,
                                                "Make sure that the data file pointed to by the" +
                                                " DATA_FILE=datafilename entry in Customer.cfg"
                                                +
                                                " exists and has read permissions.",
                                                LoggingServices.CRITICAL);
            System.exit(1);
        } catch (IOException e) {
            LoggingServices.getCurrent().logMsg(getClass().getName(),
                                                "CMSCustomerMemoryServices()"
                                                ,
                                                "Cannot process the customer data file."
                                                ,
                                                "Verify the integrity of the customer data file"
                                                +
                                                " pointed to by the DATA_FILE=datafilename" +
                                                " entry in the Customer.cfg file."
                                                , LoggingServices.CRITICAL);
            System.exit(1);
        }
        customerVector.trimToSize();
        int size = customerVector.size();
        if (size > 0) {
            allCustomers = new CMSCustomer[size];
            for (int i = 0; i < size; i++) {
                allCustomers[i] = (CMSCustomer) customerVector.elementAt(i);
            }
        }
    }

    /**
     * This method is used to seach a Customer by coustomer id
     * @param id String
     * @return Customer
     * @throws Exception
     */
    public Customer findById(String id) throws java.lang.Exception {
        if (allCustomers == null || allCustomers.length == 0) {
            return null;
        }
        for (int i = 0; i < allCustomers.length; i++) {
            if (allCustomers[i].getId().equalsIgnoreCase(id)) {
                return allCustomers[i];
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
    public Customer[] findByLastNameZipCode(String lastName, String zipCode) throws
            java.lang.Exception {
        if (allCustomers == null || allCustomers.length == 0) {
            return null;
        }
        Vector customerVector = new Vector();
        for (int i = 0; i < allCustomers.length; i++) {
            if (allCustomers[i].getLastName().equalsIgnoreCase(lastName)
                && allCustomers[i].getZipCode().equalsIgnoreCase(zipCode)) {
                customerVector.add(allCustomers[i]);
            }
        }
        customerVector.trimToSize();
        int size = customerVector.size();
        if (size > 0) {
            CMSCustomer[] customers = new CMSCustomer[size];
            for (int i = 0; i < size; i++) {
                customers[i] = (CMSCustomer) customerVector.elementAt(i);
            }
            return customers;
        } else {
            return null;
        }
    }

    /**
     * This method is used to search customers by last and first names
     * @param lastName String
     * @param firstName String
     * @return Customer[]
     * @throws Exception
     */
    public Customer[] findByLastNameFirstName(String lastName, String firstName) throws
            java.lang.Exception {
        return null;
    }

    /**
     * This method is used to search customers by Barcode
     * @param Barcode String
     * @return Customer[]
     * @throws Exception
     */
    public Customer[] findByBarcode(String sBarcode) throws java.lang.Exception {
        return null;
    }

    /**
     * This method is used to presist customer to data store
     * @param customer Customer
     * @throws Exception
     */
    public void submit(Customer customer) throws java.lang.Exception {
        boolean isNew = true;
        for (int i = 0; i < allCustomers.length; i++) {
            if (allCustomers[i].getId().equalsIgnoreCase(customer.getId())) {
                allCustomers[i] = (CMSCustomer) customer;
                isNew = false;
                break;
            }
        }
        if (isNew) {
            CMSCustomer[] allNewCustomers = new CMSCustomer[allCustomers.length +
                                            1];
            for (int i = 0; i < allCustomers.length; i++) {
                allNewCustomers[i] = allCustomers[i];
            }
            allNewCustomers[allNewCustomers.length -
                    1] = (CMSCustomer) customer;
            allCustomers = allNewCustomers;
        }
    }

    /**
     * This method is used to Submit Customer and return it to client.
     * @param customer Customer
     * @return Customer
     */
    public Customer submitAndReturn(Customer customer) throws Exception {
        return null;
    }

    /**
     * This method is used to load customer object
     * @param id String
     * @return Customer
     * @throws Exception
     */
    public Customer loadCustomer(String id) throws java.lang.Exception {
        CMSCustomer customer = null;
        for (int i = 0; i < allCustomers.length; i++) {
            if (allCustomers[i].getId().equalsIgnoreCase(id)) {
                customer = allCustomers[i];
                break;
            }
        }
        return customer;
    }

    /**
     * This method is used to search customers by a telephone number
     * @param phone String
     * @return Customer[]
     * @throws Exception
     */
    public Customer[] findByPhone(String phone) throws java.lang.Exception {
        return null;
        //    if (allCustomers == null || allCustomers.length == 0)
        //      return null;
        //    Vector customerVector = new Vector();
        //    for (int i = 0; i < allCustomers.length; i++)
        //    {
        //      if (allCustomers[i].getPhone().equalsIgnoreCase(phone) || allCustomers[i].getAlternatePhone().equalsIgnoreCase(phone))
        //        customerVector.add(allCustomers[i]);
        //    }
        //    customerVector.trimToSize();
        //    int size = customerVector.size();
        //
        //    if (size > 0)
        //    {
        //      CMSCustomer[] customers = new CMSCustomer[size];
        //      for (int i = 0; i < size; i++)
        //        customers[i] = (CMSCustomer)customerVector.elementAt(i);
        //      return customers;
        //    }
        //    else
        //    {
        //      return null;
        //    }
    }

    /**
     * This method is used to search customers by a telephone
     * @param telephone Telephone
     * @return Customer[]
     * @throws Exception
     */
    public Customer[] findByTelephone(Telephone telephone) throws java.lang.
            Exception {
        return null;
    }

    /**
     * This method is used to search customers matching search string
     * @param searchStr search string
     */
    public Customer[] findBySearchQuery(String searchStr) throws Exception {
        return null;
    }

    /**
     * Search customer using advance search criteria
     * @param searchStr CustomerSearchString
     * @throws Exception
     * @return Customer[]
     */
    public Customer[] findBySearchQuery(CustomerSearchString searchStr) throws
            Exception {
        return null;
    }

    /**
     * This method is used to search customers on the basis of reward card id
     * @param rcId reward card id
     */
    public Customer[] findByRewardCard(String rcId) throws Exception {
        return null;
    }

    /**
     * This method is used to get customer's sale summary on the basis of
     * customer id for a given store
     * @param custId customer id
     * @param storeId store id
     */
    public CustomerSaleSummary[] getCustSaleSummary(String custId,
            String storeId) throws Exception {
        return null;
    }

    /**
     * put your documentation comment here
     * @param custId
     * @return
     * @exception Exception
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
        //    if (allCustomers != null)
        //      for (int i = 0; i < allCustomers.length; i++)
        //        if (allCustomers[i].getPhone().equalsIgnoreCase(CMSCustomer.UNKNOWN_CUSTOMER_PHONE))
        //           return  allCustomers[i];
        return null;
    }

    /**
     * This method is used to merge two Customers
     * @param sourceCustomer Customer
     * @param targetCustomer Customer
     * @return boolean
     * @throws Exception
     */
    public boolean merge(Customer sourceCustomer, Customer targetCustomer) throws
            java.lang.Exception {
        return false;
    }

    /**
     * This method is used to get all customers messages
     * @return CMSCustomerMessage
     */
    public CMSCustomerMessage[] getAllCustomerMessages() throws Exception {
        return null;
    }

    public DepositHistory[] getDepositHistory(String customerId, String storeId) throws
            Exception {
        // TODO Auto-generated method stub
        return null;
    }
public CreditHistory[] getCreditHistory(String customerId, String storeId) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

  /**
   * This method is used to get transaction data by employee rule 
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
   * @param countryCode
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
      throws Exception {
    return null;
  }
  	/**
	 * This method is used to get transaction data by employee rule
	 * 
	 * @param countryCode
	 * @param discountLevel
	 * @param id_brand
	 * @author vivek.sawant
	 * @return CMSCustomerAlertRule
	 */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,
			String BrandID) throws Exception {
		return null;
	}
	 /**
	   * This method is used to get Expiry Date of current Membership ID 
	   * @param membershipNo
	   * @param brand_ID
	   * @author vivek.sawant
	   * @return CMSVIPMembershipDetail
	   */
	 public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID) throws Exception {
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
		 * SAP CRM
		 * 
		 * 
		 */
		public Customer[] selectNewCustomersForStore(String storeId)throws Exception{
			return null;
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
		 * This method is used to get CustomerAlertRule
		 * 
		 * @param countryCode
		 * @param businessDate
		 * @param id_brand
		 * @author vishal
		 * @return CMSCustomerAlertRule
		 */
		
		@Override
		public CMSCustomerAlertRule[] getAllCustomerAlertRules(
				String countryCode, String brandID, java.sql.Date businessDate)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
}

