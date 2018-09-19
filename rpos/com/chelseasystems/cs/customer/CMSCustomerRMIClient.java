/*
 * @copyright (c) 2002 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.customer;

import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.node.IRemoteServerClient;
import  com.chelseasystems.cr.node.ICMSComponent;
import  com.igray.naming.*;

import  java.rmi.*;
import java.util.Date;
import java.util.Map;

import  com.chelseasystems.cr.customer.*;
import  com.chelseasystems.cr.telephone.*;
import  com.chelseasystems.cs.customer.CustomerSearchString;


/**
 *
 * <p>Title: CMSCustomerRMIClient</p>
 *
 * <p>Description: This class deal with client-side of an RMI connection for
 * fetching/submitting customer object</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSCustomerRMIClient extends CMSCustomerServices
    implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSCustomerRMIServer cmscustomerServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * This method set the configuration manager and make sure that the system
   * has a security manager set.
   */
  public CMSCustomerRMIClient () throws DowntimeException
  {
    config = new ConfigMgr("customer.cfg");
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
    init();
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   */
  private void init () throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSCustomerRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()", "Cannot establish connection to RMI server.",
          "Make sure that the server is registered on the remote server" + " and that the name of the remote server and remote service are"
          + " correct in the customer.cfg file.", LoggingServices.MAJOR, e);
      throw  new DowntimeException(e.getMessage());
    }
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   * @exception Exception
   */
  public void lookup () throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmscustomerServer = (ICMSCustomerRMIServer)NamingService.lookup(connect);
  }

  /**
   * This method is used to check whether remote server is available or not
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable () {
    try {
      return  ((ICMSComponent)this.cmscustomerServer).isAvailable();
    } catch (Exception ex) {
      return  false;
    }
  }

  /**
   * This method is used to merge two Customers
   * @param customer1 Customer
   * @param customer2 Customer
   * @return boolean
   * @throws Exception
   */
  public boolean merge (Customer customer1, Customer customer2) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.merge((CMSCustomer)customer1, (CMSCustomer)customer2);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId String
   * @return Customer
   * @throws Exception
   */
  public Customer findById (String customerId) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findById((String)customerId);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to presist customer to data store
   * @param customer Customer
   * @throws Exception
   */
  public void submit (Customer customer) throws Exception {
    submitAndReturn(customer);
  }

  /**
   * This method is used to Submit Customer and return it to client.
   * @param customer Customer
   * @return Customer
   */
  public Customer submitAndReturn (Customer customer) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.submit((CMSCustomer)customer);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to search customers by a telephone number
   * @param phoneNumber String
   * @return Customer[]
   * @throws Exception
   */
  public Customer[] findByPhone (String phoneNumber) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findByPhone((String)phoneNumber);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to search customers by a Barcode
   * @param phoneNumber String
   * @return Customer[]
   * @throws Exception
   */
  public Customer[] findByBarcode (String sBarcode) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findByBarcode((String)sBarcode);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        if (ex instanceof RemoteException && ((RemoteException)ex).detail instanceof RemoteException) {
          Throwable rootCause = ((RemoteException)((RemoteException)ex).detail).detail;
          if (rootCause instanceof TooManySearchResultsException) {
            throw  (TooManySearchResultsException)((RemoteException)((RemoteException)ex).detail).detail;
          }
        }
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to search customers by a telephone
   * @param aTelephone Telephone
   * @return Customer[]
   * @throws Exception
   */
  public Customer[] findByTelephone (Telephone aTelephone) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findByTelephone((Telephone)aTelephone);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        if (ex instanceof RemoteException && ((RemoteException)ex).detail instanceof RemoteException) {
          Throwable rootCause = ((RemoteException)((RemoteException)ex).detail).detail;
          if (rootCause instanceof TooManySearchResultsException) {
            throw  (TooManySearchResultsException)((RemoteException)((RemoteException)ex).detail).detail;
          }
        }
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to search Customer by their last name and postal code
   * @param lastName String
   * @param zip String
   * @return Customer[]
   * @throws Exception
   */
  public Customer[] findByLastNameZipCode (String lastName, String zip) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findByLastNameZipCode((String)lastName, (String)zip);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to search for customers by last and first names
   * @param lastName String
   * @param firstName String
   * @return Customer[]
   * @throws Exception
   */
  public Customer[] findByLastNameFirstName (String lastName, String firstName) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findByLastNameFirstName((String)lastName, (String)firstName);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        if (ex instanceof RemoteException && ((RemoteException)ex).detail instanceof RemoteException) {
          Throwable rootCause = ((RemoteException)((RemoteException)ex).detail).detail;
          if (rootCause instanceof TooManySearchResultsException) {
            throw  (TooManySearchResultsException)((RemoteException)((RemoteException)ex).detail).detail;
          }
        }
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }
/**
 * 
 * SAP CRM
 * 
 * 
 */
  
  public Customer[] selectNewCustomersForStore (String storeId) throws Exception {
	    for (int x = 0; x < maxTries; x++) {
	      if (cmscustomerServer == null) {
	        init();
	      }
	      try {
	    	  System.out.println("SAP/CRM Store id   :"+storeId);
	        return  cmscustomerServer.selectNewCustomersForStore((String)storeId);
	      } catch (ConnectException ce) {
	        cmscustomerServer = null;
	      } catch (Exception ex) {
	        if (ex instanceof RemoteException && ((RemoteException)ex).detail instanceof RemoteException) {
	          Throwable rootCause = ((RemoteException)((RemoteException)ex).detail).detail;
	          if (rootCause instanceof TooManySearchResultsException) {
	            throw  (TooManySearchResultsException)((RemoteException)((RemoteException)ex).detail).detail;
	          }
	        }
	        throw  new DowntimeException(ex.getMessage());
	      }
	    }
	    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
	  }

  
  
  
  /**
   * This method is used to search for customers by last and first names
   * @param CustId String
   * @return DepositHistory[]
   * @throws Exception
   */
  public DepositHistory[] getDepositHistory (String custId) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.getDepositHistory((String)custId);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to search for customers by last and first names
   * @param CustId String, StoreId String
   * @return DepositHistory[]
   * @throws Exception
   */
  public DepositHistory[] getDepositHistory (String custId, String storeId) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.getDepositHistory((String)custId, (String)storeId);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }
  /**
   * This method is used to search for customers by last and first names
   * @param CustId String, StoreId String
   * @return CreditHistory[]
   * @throws Exception
   */
  public CreditHistory[] getCreditHistory(String custId,String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return cmscustomerServer.getCreditHistory((String)custId,(String)storeId);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }
  /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   */
  public Customer[] findBySearchQuery (String searchStr,boolean isFranchisingStore) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findBySearchQuery((String)searchStr,isFranchisingStore);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        if (ex instanceof RemoteException && ((RemoteException)ex).detail instanceof RemoteException) {
          Throwable rootCause = ((RemoteException)((RemoteException)ex).detail).detail;
          if (rootCause instanceof TooManySearchResultsException) {
            throw  (TooManySearchResultsException)((RemoteException)((RemoteException)ex).detail).detail;
          }
          throw  new DowntimeException(ex.getMessage());
        }
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }
  /*
   * This method is used to search for customers by matching search string
   * @param searchStr String
   * @return Customer[]
   * @throws Exception
   */
  public Customer[] findBySearchQuery (String searchStr) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findBySearchQuery((String)searchStr);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        if (ex instanceof RemoteException && ((RemoteException)ex).detail instanceof RemoteException) {
          Throwable rootCause = ((RemoteException)((RemoteException)ex).detail).detail;
          if (rootCause instanceof TooManySearchResultsException) {
            throw  (TooManySearchResultsException)((RemoteException)((RemoteException)ex).detail).detail;
          }
          throw  new DowntimeException(ex.getMessage());
        }
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * Lookup customer using advance search criteria
   * @param searchStr CustomerSearchString
   * @throws Exception
   * @return Customer[]
   */
  public Customer[] findBySearchQuery (CustomerSearchString searchStr) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findBySearchQuery((CustomerSearchString)searchStr);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        if (ex instanceof RemoteException && ((RemoteException)ex).detail instanceof RemoteException) {
          Throwable rootCause = ((RemoteException)((RemoteException)ex).detail).detail;
          if (rootCause instanceof TooManySearchResultsException) {
            throw  (TooManySearchResultsException)((RemoteException)((RemoteException)ex).detail).detail;
          }
          throw  new DowntimeException(ex.getMessage());
        }
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to search for customers matching reward card id
   * @param rcId String
   * @return Customer[]
   * @throws Exception
   */
  public Customer[] findByRewardCard (String rcId) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.findByRewardCard((String)rcId);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to get customer sale summary
   * @param custId String
   * @param storeId String
   * @return CustomerSaleSummary[]
   * @throws Exception
   */
  public CustomerSaleSummary[] getCustSaleSummary (String custId, String storeId) throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.getCustSaleSummary((String)custId, (String)storeId);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to return a default customer
   * @return Customer
   * @throws Exception
   */
  public Customer getDefaultCustomer () throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return  cmscustomerServer.getDefaultCustomer();
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw  new DowntimeException(ex.getMessage());
      }
    }
    throw  new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }

  /**
   * This method is used to return all customer messages
   * @return Customer Messages
   * @throws Exception
   */
  public CMSCustomerMessage[] getAllCustomerMessages()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return cmscustomerServer.getAllCustomerMessages();
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }
  
  /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @return Map
   */
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return cmscustomerServer.getTransactionDataByEmpRule(customerId, countryCode, societyCode);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSCustomerServices");
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
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode,CMSCustomerAlertRule rules[],String id_brand)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return cmscustomerServer.getTransactionDataByEmpRule(customerId, countryCode, societyCode,rules,id_brand);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSCustomerServices");
  }
  
  
  /**
   * This method is used to get customer alert rules
   * @param countryCode
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmscustomerServer == null) {
        init();
      }
      try {
        return cmscustomerServer.getAllCustomerAlertRules(countryCode);
      } catch (ConnectException ce) {
        cmscustomerServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSCustomerServices");
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
			String brandID) throws Exception {
		for (int x = 0; x < maxTries; x++) {
			if (cmscustomerServer == null) {
				init();
			}
			try {
				return cmscustomerServer.getAllCustomerAlertRules(countryCode,
						brandID);
			} catch (ConnectException ce) {
				cmscustomerServer = null;
			} catch (Exception ex) {
				throw new DowntimeException(ex.getMessage());
			}
		}
		throw new DowntimeException(
				"Unable to establish connection to CMSCustomerServices");
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
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,
			String brandID, java.sql.Date businessDate) throws Exception {
		for (int x = 0; x < maxTries; x++) {
			if (cmscustomerServer == null) {
				init();
			}
			try {
				return cmscustomerServer.getAllCustomerAlertRules(countryCode,
						brandID, businessDate);
			} catch (ConnectException ce) {
				cmscustomerServer = null;
			} catch (Exception ex) {
				throw new DowntimeException(ex.getMessage());
			}
		}
		throw new DowntimeException(
				"Unable to establish connection to CMSCustomerServices");
	}
	
	
	
	
	/**
	 * This method is used to get Expiry Date of current Membership ID
	 * @param membershipNo
	 * @param brand_ID
	 * @author vivek.sawant
	 * @return CMSVIPMembershipDetail
	 */
	public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID) throws Exception {

		for (int x = 0; x < maxTries; x++) {
			if (cmscustomerServer == null) {
				init();
			}
			try {
				return cmscustomerServer.selectByMembershipNumber(membershipNo, brand_ID);
			} catch (ConnectException ce) {
				cmscustomerServer = null;
			} catch (Exception ex) {
				throw new DowntimeException(ex.getMessage());
			}
		}
		throw new DowntimeException(
				"Unable to establish connection to CMSCustomerServices");
	
	}

	/**
	  * This method return Membership Id depends on Customer ID 
	  * @param Customer ID
	  * @author vivek.sawant
	  * @return String
	  */
	public String getVIPMembershipID(String customerID) throws Exception {
		for (int x = 0; x < maxTries; x++) {
			if (cmscustomerServer == null) {
				init();
			}
			try {
				return cmscustomerServer.getVIPMembershipID(customerID);
			} catch (ConnectException ce) {
				cmscustomerServer = null;
			} catch (Exception ex) {
				throw new DowntimeException(ex.getMessage());
			}
		}
		throw new DowntimeException(
				"Unable to establish connection to CMSCustomerServices");
	}
	
	  /**
	   * This method return date of creation/modify of customer 
	   * @param Customer ID
	   * @author vivek.sawant
	   * @return Date
	   */
		
		public Date getCustomerCreatationDate(String customerID)throws Exception{
			for (int x = 0; x < maxTries; x++) {
				if (cmscustomerServer == null) {
					init();
				}
				try {
					return cmscustomerServer.getCustomerCreatationDate(customerID);
				} catch (ConnectException ce) {
					cmscustomerServer = null;
				} catch (Exception ex) {
					throw new DowntimeException(ex.getMessage());
				}
			}
			throw new DowntimeException(
					"Unable to establish connection to CMSCustomerServices");
		
			
		}
		public String[] getCardToken(String customerId)throws Exception
		{
			for (int x = 0; x < maxTries; x++) {
				if (cmscustomerServer == null) {
					init();
				}
				try {
					return cmscustomerServer.getCardToken(customerId);
				} catch (ConnectException ce) {
					cmscustomerServer = null;
				} catch (Exception ex) {
					throw new DowntimeException(ex.getMessage());
				}
			}
			throw new DowntimeException(
					"Unable to establish connection to CMSCustomerServices");
		}

}

