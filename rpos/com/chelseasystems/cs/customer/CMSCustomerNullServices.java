/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.customer;

import  com.chelseasystems.cr.customer.*;
import  com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

import  java.util.*;

import  com.chelseasystems.cr.telephone.*;
import  com.chelseasystems.cs.customer.CustomerSearchString;


/**
 *
 * <p>Title: CMSCustomerNullServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSCustomerNullServices extends CMSCustomerServices {

  /**
   * Default Constructor
   */
  public CMSCustomerNullServices () {
  }

  /**
   * This method is used to search Customer by their last name and postal code
   * @param lastName
   * @param zipCode
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByLastNameZipCode (String lastName, String zipCode) throws java.lang.Exception {
    return  new CMSCustomer[0];
  }

  /**
   * This method is used to search Customer by Barcode
   * @param Barcode
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByBarcode (String sBarcode) throws java.lang.Exception {
    return  new CMSCustomer[0];
  }

  /**
   * This method is used to search customers by last and first names
   * @param lastName
   * @param firstName
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByLastNameFirstName (String lastName, String firstName) throws java.lang.Exception {
    return  new CMSCustomer[0];
  }

  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId
   * @return
   * @exception java.lang.Exception
   */
  public Customer findById (String customerId) throws java.lang.Exception {
    return  null;
  }

  /**
   * This method is used to search customers by a telephone number
   * @param phone
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByPhone (String phone) throws java.lang.Exception {
    return  new CMSCustomer[0];
  }

  /**
   * This method is used to search customers by a telephone
   * @param telephone
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByTelephone (Telephone telephone) throws java.lang.Exception {
    return  new CMSCustomer[0];
  }

  /**
   * This method is used to search customers matching search string
   * @param searchStr search string
   */
  public Customer[] findBySearchQuery (String searchStr) throws Exception {
    return  new CMSCustomer[0];
  }

  /**
   * Lookup customer using advance search criteria
   * @param searchStr CustomerSearchString
   * @throws Exception
   * @return Customer[]
   */
  public Customer[] findBySearchQuery (CustomerSearchString searchStr) throws Exception {
    return  new CMSCustomer[0];
  }

  /**
   * This method is used to search customers on the basis of reward card id
   * @param rcId reward card id
   */
  public Customer[] findByRewardCard (String rcId) throws Exception {
    return  new CMSCustomer[0];
  }

  /**
   * This method is used to get customer's sale summary on the basis of
   * customer id for a given store
   * @param custId customer id
   * @param storeId store id
   */
  public CustomerSaleSummary[] getCustSaleSummary (String custId, String storeId) throws Exception {
    return  new CustomerSaleSummary[0];
  }

  /**
   * This method is used to get a default customer
   * @return
   * @exception java.lang.Exception
   */
  public Customer getDefaultCustomer () throws java.lang.Exception {
    return  null;
  }

  /**
   * This method is used to presist customer to data store
   * @param customer
   * @exception java.lang.Exception
   */
  public void submit (Customer customer) throws java.lang.Exception {
    return;
  }

  /**
   * This method is used to Submit Customer and return it to client.
   * @param customer Customer
   * @return Customer
   */
  public Customer submitAndReturn (Customer customer) throws Exception {
    return  null;
  }

  /**
   * This method is used to merge two Customers
   * @param parm1
   * @param parm2
   * @return
   * @exception java.lang.Exception
   */
  public boolean merge (Customer parm1, Customer parm2) throws java.lang.Exception {
    return  false;
  }

  /**
   * This method is used to get all customer messages
   * @return
   * @exception java.lang.Exception
   */
  public CMSCustomerMessage[] getAllCustomerMessages () throws java.lang.Exception {
    return  null;
  }

  /**
   * put your documentation comment here
   * @param custId
   * @return
   * @exception java.lang.Exception
   */
  public DepositHistory[] getDepositHistory (String custId) throws java.lang.Exception {
    return  null;
  }

  /**
   * put your documentation comment here
   * @param customerId
   * @param storeId
   * @return
   * @exception Exception
   */
  public DepositHistory[] getDepositHistory (String customerId, String storeId) throws Exception {
    // TODO Auto-generated method stub
    return  null;
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
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode)
    	throws java.lang.Exception {
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
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode, CMSCustomerAlertRule rules[], String id_brand)  throws java.lang.Exception {
		
		return null;
	}
  /**
   * This method is used to get customer alert rules
   * @param countryCode
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
  		throws java.lang.Exception {
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
			String brandID) throws java.lang.Exception {
		return null;
	}
	
	/**
		   * This method is used to get Expiry Date of current Membership ID 
		   * @param membershipNo
		   * @param brand_ID
		   * @author vivek.sawant
		   * @return CMSVIPMembershipDetail
		   */
		 public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID) throws java.lang.Exception {
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

