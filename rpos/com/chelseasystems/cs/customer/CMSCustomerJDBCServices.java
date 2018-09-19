/*
 * @copyright (c) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.telephone.Telephone;
import com.chelseasystems.cs.dataaccess.CustomerDAO;


/**
 *
 * <p>Title: CMSCustomerJDBCServices</p>
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
public class CMSCustomerJDBCServices extends CMSCustomerServices {
  private CustomerDAO customerDAO;

  /**
   * Default Constructor
   */
  public CMSCustomerJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    customerDAO = (CustomerDAO)configMgr.getObject("CUSTOMER_DAO");
  }

  /**
   * This method is used to search Customer by their last name and postal code
   * @param lastName
   * @param zipCode
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByLastNameZipCode(String lastName, String zipCode)
      throws java.lang.Exception {
    try {
      lastName = lastName.toUpperCase().trim();
      zipCode = zipCode.trim();
      return customerDAO.selectByLastNameZipCode(lastName, zipCode);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByLastNameZipCode"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search Customer Deposit History
   * @param String custId
   * @return
   * @exception java.lang.Exception
   */
  public DepositHistory[] getDepositHistory(String custId)
      throws java.lang.Exception {
    try {
      return customerDAO.getDepositHistory(custId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getDepositHistory"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search Customer Deposit History
   * @param String custId, String storeId
   * @return
   * @exception java.lang.Exception
   */
  public DepositHistory[] getDepositHistory(String custId,String storeId)
      throws java.lang.Exception {
    try {
      return customerDAO.getDepositHistoryForStore(custId,storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getDepositHistory with store"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  /**
   * This method is used to search Customer Credit History
   * @param String custId, String storeId
   * @return
   * @exception java.lang.Exception
   */
  public CreditHistory[] getCreditHistory(String custId,String storeId)
      throws java.lang.Exception {
    try {
      return customerDAO.getCreditHistoryForStore(custId,storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getDepositHistory with store"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  
  
  
  
  /**
   * This method is used to search customers by last and first names
   * @param lastName
   * @param firstName
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByLastNameFirstName(String lastName, String firstName)
      throws java.lang.Exception {
    try {
      lastName = lastName.toUpperCase().trim();
      firstName = firstName.trim();
      return customerDAO.selectByLastNameFirstName(lastName, firstName);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByLastNameFirstName"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  
  
  /**
   * This method is used to search customers by last and first names
   * @param lastName
   * @param firstName
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] selectNewCustomersForStore(String storeId)
      throws java.lang.Exception {
    try {
    	storeId = storeId.trim();
    	System.out.println("SAP/CRM Inside CMSCustomerJDBCServices   :"+storeId);
      return customerDAO.selectNewCustomersForStore(storeId);
    } catch (Exception exception) {
    	LoggingServices.getCurrent().logMsg(this.getClass().getName(), "selectNewCustomersForStore"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  
  
  
  /**
   * This method is used to search customers by Barcode
   * @param Barcode
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByBarcode(String sBarcode)
      throws java.lang.Exception {
    try {
      return customerDAO.selectByBarcode(sBarcode.toUpperCase().trim());
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByBarcode"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId
   * @return
   * @exception java.lang.Exception
   */
  public Customer findById(String customerId)
      throws java.lang.Exception {
    try {
      return customerDAO.selectById(customerId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search customers by a telephone number
   * @param phone
   * @return
   * @exception java.lang.Exception
   * @deprecated use findByTelephone(Telephone)
   */
  public Customer[] findByPhone(String phone)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to search customers by a telephone
   * @param telephone
   * @return
   * @exception java.lang.Exception
   */
  public Customer[] findByTelephone(Telephone telephone)
      throws java.lang.Exception {
    try {
      return customerDAO.selectByTelephone(telephone);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByTelephone", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   */
  public Customer[] findBySearchQuery(String searchStr,boolean isFranchisingStore)
      throws Exception {
    try {
      return customerDAO.selectBySearchQuery(searchStr,isFranchisingStore);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findBySearchQuery"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search customers matching search string
   * @param searchStr search string
   */
  public Customer[] findBySearchQuery(String searchStr)
      throws Exception {
    try {
      return customerDAO.selectBySearchQuery(searchStr);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findBySearchQuery"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * Lookup customer using advance search Criteria
   * @param searchStr CustomerSearchString
   * @throws Exception
   * @return Customer[]
   */
  public Customer[] findBySearchQuery(CustomerSearchString searchStr)
      throws Exception {
    try {
      return customerDAO.selectBySearchQuery(searchStr);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findBySearchQuery"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search customers on the basis of reward card id
   * @param rcId reward card id
   */
  public Customer[] findByRewardCard(String rcId)
      throws Exception {
    try {
      return customerDAO.selectByRewardCard(rcId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByRewardCard"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to get customer's sale summary on the basis of
   * customer id for a given store
   * @param custId customer id
   * @param storeId store id
   */
  public CustomerSaleSummary[] getCustSaleSummary(String custId, String storeId)
      throws Exception {
    try {
      return customerDAO.getCustSaleSummary(custId, storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getCustSaleSummary"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to get a default customer
   * @return
   * @exception java.lang.Exception
   */
  public Customer getDefaultCustomer()
      throws java.lang.Exception {
    try {
      Customer[] array = customerDAO.selectByTelephone(Customer.UNKNOWN_CUSTOMER_PHONE);
      return (array.length > 0) ? array[0] : null;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getDefaultCustomer"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to presist customer to data store
   * @param customer
   * @exception java.lang.Exception
   */
  public void submit(Customer customer)
      throws Exception {
    submitAndReturn(customer);
  }

  /**
   * This method is used to Submit Customer and return it to client.
   * @param customer Customer
   * @return Customer
   */
  public Customer submitAndReturn(Customer customer)
      throws Exception {
    try {
      customer.doSetLastName(customer.getLastName().toUpperCase());
      customer.doSetFirstName(customer.getFirstName().toUpperCase());
      if (customer.isNew()) {
        customerDAO.insert(customer);
      } else if (customer.isModified()) {
        customerDAO.update(customer);
      }
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
    // Fetch the saved customer back from database to return
    return customerDAO.selectById(customer.getId());
  }

  /**
   * This method is used to merge two Customers
   * @param parm1
   * @param parm2
   * @return
   * @exception java.lang.Exception
   */
  public boolean merge(Customer parm1, Customer parm2)
      throws java.lang.Exception {
    //TODO: implement this com.chelseasystems.cr.customer.CustomerServices abstract method
    return false;
  }

  /**
   * This method is used to get all customer messages
   * @param customer
   * @exception java.lang.Exception
   */
  public CMSCustomerMessage[] getAllCustomerMessages()
      throws java.lang.Exception {
    try {
      return customerDAO.getAllCustomerMessages();
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }
  
  /**
	 * This method is used to get transaction data by employee rule
	 * @param customerId
	 * @param countryCode
	 * @return Map
	 */
	public Map getTransactionDataByEmpRule(String customerId,String countryCode, String societyCode)
                throws java.lang.Exception {
		try {
			return customerDAO.getTransactionDataByEmpRule(customerId,countryCode, societyCode);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(),	"getTransactionDataByEmpRule", "Exception"
                     ,"See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
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
	public Map getTransactionDataByEmpRule(String customerId,
			String countryCode, String societyCode, CMSCustomerAlertRule rules[],
			String id_brand) throws java.lang.Exception {
		try {
			return customerDAO.getTransactionDataByEmpRule(customerId,
					countryCode, societyCode, rules, id_brand);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(),
					"getTransactionDataByEmpRule", "Exception",
					"See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to get customer alert rules
	 * @param countryCode
	 */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
			throws java.lang.Exception {
		try {
			return customerDAO.getAllCustomerAlertRules(countryCode);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(),	"submit", "Exception"
                       , "See Exception",LoggingServices.MAJOR, exception);
			throw exception;
		}
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
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID)
	throws java.lang.Exception {
		try {
			return customerDAO.getAllCustomerAlertRules(countryCode, brandID);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(),
					"submit", "Exception", "See Exception",
					LoggingServices.MAJOR, exception);
			throw exception;
		}
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
public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID, java.sql.Date businessDate)
throws java.lang.Exception {
	try {
		return customerDAO.getAllCustomerAlertRules(countryCode, brandID, businessDate);
	} catch (Exception exception) {
		LoggingServices.getCurrent().logMsg(this.getClass().getName(),
				"submit", "Exception", "See Exception",
				LoggingServices.MAJOR, exception);
		throw exception;
	}
}

	
			/**
		   * This method is used to get Expiry Date of current Membership ID 
		   * @param membershipNo
		   * @param brand_ID
		   * @author vivek.sawant
		   * @return CMSVIPMembershipDetail
		   */
		 public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID)  throws java.lang.Exception  {
				try {
					return customerDAO.selectByMembershipNumber(membershipNo, brand_ID);
				} catch (Exception exception) {
					LoggingServices.getCurrent().logMsg(this.getClass().getName(),
							"submit", "Exception", "See Exception",
							LoggingServices.MAJOR, exception);
					throw exception;
				}
		 }

		 /**
		   * This method return Membership Id depends on Customer ID 
		   * @param Customer ID
		   * @author vivek.sawant
		   * @return String
		   */
		public String getVIPMembershipID(String customerID) throws Exception {
			try {
				return customerDAO.getVIPMembershipID(customerID);
			} catch (Exception exception) {
				LoggingServices.getCurrent().logMsg(this.getClass().getName(),
						"submit", "Exception", "See Exception",
						LoggingServices.MAJOR, exception);
				throw exception;
			}
		}
		  /**
		   * This method return date of creation/modify of customer 
		   * @param Customer ID
		   * @author vivek.sawant
		   * @return Date
		   */
			
			public Date getCustomerCreatationDate(String customerID)throws Exception{
				try {
					return customerDAO.getCustomerCreatationDate(customerID);
				} catch (Exception exception) {
					LoggingServices.getCurrent().logMsg(this.getClass().getName(),
							"submit", "Exception", "See Exception",
							LoggingServices.MAJOR, exception);
					throw exception;
				}
			
				
			}
			//Added by sonali
			public  String[] getCardToken(String customerId) throws Exception {
				try {
					
					customerDAO.getCardToken(customerId);
					
					return customerDAO.getCardToken(customerId);
					
				} catch (Exception exception) {
					LoggingServices.getCurrent().logMsg(this.getClass().getName(),
							"submit", "Exception", "See Exception",
							LoggingServices.MAJOR, exception);
					throw exception;
				}
			}

}
