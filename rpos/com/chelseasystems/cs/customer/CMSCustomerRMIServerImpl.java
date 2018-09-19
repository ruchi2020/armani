/*
 * @copyright (c) 2002 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.customer;

import  com.chelseasystems.cr.appmgr.DowntimeException;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cr.node.*;
import  com.igray.naming.*;

import  java.rmi.*;
import  java.rmi.server.UnicastRemoteObject;
import  java.util.*;

import  com.chelseasystems.cr.customer.*;
import  com.chelseasystems.cr.telephone.*;
import  com.chelseasystems.cs.customer.CustomerSearchString;


/**
 *
 * <p>Title: CMSCustomerRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for fetching/submitting
 * information.  This class delgates all method calls to the object referenced
 * by the return value from CMSCustomerServices.getCurrent(). </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSCustomerRMIServerImpl extends CMSComponent
implements ICMSCustomerRMIServer {

  /**
   * Constructor
   * @param props Properties
   * @throws RemoteException
   */
  public CMSCustomerRMIServerImpl (Properties props) throws RemoteException
  {
    super(props);
    setImpl();
    init();
  }

  /**
   * This method is used to set the server side implementation
   */
  private void setImpl () {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()", "Could not instantiate SERVER_IMPL.",
          "Make sure customer.cfg contains SERVER_IMPL", LoggingServices.MAJOR);
    }
    CMSCustomerServices.setCurrent((CMSCustomerServices)obj);
  }

  /**
   * This method is used to bind employee object to RMI registery
   */
  private void init () {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    }
    else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()", "Could not find name to bind to in registry.",
          "Make sure customer.cfg contains a REMOTE_NAME entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * This method receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent (String[] aKey) {}

  /**
   * This method is used by the DowntimeManager to determine when this object
   * is available. Just because this process is up doesn't mean that the
   * clients can come up. Make sure that the database is available.
   * @return boolean <code>true</code> indicates that this class is available.
   */
  public boolean ping () throws RemoteException {
    return  true;
  }

  /**
   * This method is used to merge two Customers
   * @param customer1 CMSCustomer
   * @param customer2 CMSCustomer
   * @return boolean
   * @throws RemoteException
   */
  public boolean merge (CMSCustomer customer1, CMSCustomer customer2) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (boolean)CMSCustomerServices.getCurrent().merge(customer1, customer2);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("merge", start);
      decConnection();
    }
  }

  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId String
   * @return CMSCustomer
   * @throws RemoteException
   */
  public CMSCustomer findById (String customerId) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer)CMSCustomerServices.getCurrent().findById(customerId);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }

  /**
   * This method is used to presist customer to data store
   * @param customer CMSCustomer
   * @throws RemoteException
   */
  public CMSCustomer submit (CMSCustomer customer) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer)((CMSCustomerServices)CMSCustomerServices.getCurrent()).submitAndReturn(customer);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("submit", start);
      decConnection();
    }
  }

  /**
   * This method is used to search customers by a telephone number
   * @param phoneNumber String
   * @return CMSCustomer[]
   * @throws RemoteException
   */
  public CMSCustomer[] findByPhone (String phoneNumber) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])CMSCustomerServices.getCurrent().findByPhone(phoneNumber);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByPhone", start);
      decConnection();
    }
  }

  /**
   * This method is used to search customers by a Barcode
   * @param Barcode String
   * @return CMSCustomer[]
   * @throws RemoteException
   */
  public CMSCustomer[] findByBarcode (String sBarcode) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).findByBarcode(sBarcode);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByBarcode", start);
      decConnection();
    }
  }

  /**
   * This method is used to search customers by a telephone
   * @param aTelephone Telephone
   * @return Customer[]
   * @throws RemoteException
   */
  public Customer[] findByTelephone (Telephone aTelephone) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (Customer[])CMSCustomerServices.getCurrent().findByTelephone(aTelephone);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByTelephone", start);
      decConnection();
    }
  }

  /**
   * This method is used to search customers for deposit
   * @param String custId
   * @return DepositHistory
   * @throws RemoteException
   */
  public DepositHistory[] getDepositHistory (String custId) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (DepositHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).getDepositHistory(custId);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDepositHistory", start);
      decConnection();
    }
  }

  /**
   * This method is used to search customers for deposit
   * @param String custId, String storeId
   * @return DepositHistory
   * @throws RemoteException
   */
  public DepositHistory[] getDepositHistory (String custId, String storeId) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (DepositHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).getDepositHistory(custId,
          storeId);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDepositHistory", start);
      decConnection();
    }
  }

  /**
   * This method is used to search customers for deposit
   * @param String custId, String storeId
   * @return CreditHistory
   * @throws RemoteException
   */
  public CreditHistory[] getCreditHistory (String custId, String storeId) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CreditHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).getCreditHistory(custId,
          storeId);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDepositHistory", start);
      decConnection();
    }
  }

  /**
   * This method is used to search Customer by their last name and postal code
   * @param lastName String
   * @param zip String
   * @return CMSCustomer[]
   * @throws RemoteException
   */
  public CMSCustomer[] findByLastNameZipCode (String lastName, String zip) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])CMSCustomerServices.getCurrent().findByLastNameZipCode(lastName, zip);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByLastNameZipCode", start);
      decConnection();
    }
  }

  /**
   * This method is used to search for customers by last and first names
   * @param lastName String
   * @param firstName String
   * @return CMSCustomer[]
   * @throws RemoteException
   */
  public CMSCustomer[] findByLastNameFirstName (String lastName, String firstName) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])CMSCustomerServices.getCurrent().findByLastNameFirstName(lastName, firstName);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByLastNameFirstName", start);
      decConnection();
    }
  }
  
  
  /**
   * 
   * 
   * SAP CRM
   * 
   * 
   */
  public CMSCustomer[] selectNewCustomersForStore (String storeId) throws RemoteException {
	    long start = getStartTime();
	    try {
	      if (!isAvailable()) {
	        throw  new ConnectException("Service is not available");
	      }
	      System.out.println("SAP CRM CMSCustomerRMIServerImpl   "+storeId);
	      incConnection();
	      return  (CMSCustomer[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).selectNewCustomersForStore(storeId);
	    } catch (Exception e) {
	    	e.getStackTrace();
	      throw  new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("selectByStoreIdandRegisterId", start);
	      decConnection();
	    }
	  }
  
  
  
  
  
  
  
  /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   */
  public CMSCustomer[] findBySearchQuery (String searchStr,boolean isFranchisingStore) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).findBySearchQuery(searchStr,isFranchisingStore);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findBySearchQuery", start);
      decConnection();
    }
  }
  
  /**
   * This method is used to search for customers matching search string
   * @param searchStr String
   * @return CMSCustomer[]
   * @throws RemoteException
   */
  public CMSCustomer[] findBySearchQuery (String searchStr) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).findBySearchQuery(searchStr);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findBySearchQuery", start);
      decConnection();
    }
  }

  /**
   * This method is used to search customer using advance search criteria
   * @param searchStr CustomerSearchString
   * @throws RemoteException
   * @return CMSCustomer[]
   */
  public CMSCustomer[] findBySearchQuery (CustomerSearchString searchStr) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).findBySearchQuery(searchStr);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findBySearchQuery", start);
      decConnection();
    }
  }

  /**
   * This method is used to search for customers by matching reward card id
   * @param rcId String
   * @return CMSCustomer[]
   * @throws RemoteException
   */
  public CMSCustomer[] findByRewardCard (String rcId) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).findByRewardCard(rcId);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByRewardCard", start);
      decConnection();
    }
  }

  /**
   * This method is used to get customer sale summary
   * @param custId String
   * @param storeId String
   * @return CustomerSaleSummary[]
   * @throws RemoteException
   */
  public CustomerSaleSummary[] getCustSaleSummary (String custId, String storeId) throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CustomerSaleSummary[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).getCustSaleSummary(custId,
          storeId);
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getCustSaleSummary", start);
      decConnection();
    }
  }

  /**
   * This method is used to get a default customer
   * @return Customer
   * @throws RemoteException
   */
  public Customer getDefaultCustomer () throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (Customer)CMSCustomerServices.getCurrent().getDefaultCustomer();
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDefaultCustomer", start);
      decConnection();
    }
  }

  /**
   * This method is used to get a default customer
   * @return CMSCustomerMessage
   * @throws RemoteException
   */
  public CMSCustomerMessage[] getAllCustomerMessages () throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw  new ConnectException("Service is not available");
      }
      incConnection();
      return  (CMSCustomerMessage[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).getAllCustomerMessages();
    } catch (Exception e) {
      throw  new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDefaultCustomer", start);
      decConnection();
    }
  }


  /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @return Map
   */
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (Map)((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      	getTransactionDataByEmpRule(customerId, countryCode, societyCode);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getTransactionDataByEmpRule", start);
      decConnection();
    }
  }


	/**
	 * This method is used to get transaction data by employee rule
	 * 
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
			String id_brand) throws RemoteException {
		long start = getStartTime();
		try {
			if (!isAvailable()) {
				throw new ConnectException("Service is not available");
			}
			incConnection();
			return (Map) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getTransactionDataByEmpRule(customerId,
					countryCode, societyCode, rules, id_brand);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		} finally {
			addPerformance("getTransactionDataByEmpRule", start);
			decConnection();
		}
	}

  /**
   * This method is used to get customer alert rules
   * @param countryCode
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (CMSCustomerAlertRule[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      getAllCustomerAlertRules(countryCode);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDefaultCustomer", start);
      decConnection();
    }
  }
  	/**
	 * This method is used to get transaction data by employee rule
	 * @param countryCode
	 * @param discountLevel
	 * @param id_brand
	 * @author vivek.sawant
	 * @return CMSCustomerAlertRule
	 */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID)
	throws RemoteException {
		long start = getStartTime();
		try {
			if (!isAvailable()) {
				throw new ConnectException("Service is not available");
			}
			incConnection();
			return (CMSCustomerAlertRule[]) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getAllCustomerAlertRules(countryCode,
					brandID);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		} finally {
			addPerformance("getDefaultCustomer", start);
			decConnection();
		}
}
	
	/**
	 * This method is used to get transaction data by employee rule
	 * @param countryCode
	 * @param discountLevel
	 * @param id_brand
	 * @param businessDate
	 * @author Vishal
	 * @return CMSCustomerAlertRule
	 */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID, java.sql.Date businessDate)
	throws RemoteException {
		long start = getStartTime();
		try {
			if (!isAvailable()) {
				throw new ConnectException("Service is not available");
			}
			incConnection();
			return (CMSCustomerAlertRule[]) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getAllCustomerAlertRules(countryCode,
					brandID, businessDate);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		} finally {
			addPerformance("getDefaultCustomer", start);
			decConnection();
		}
}
	
	
	  /**
	   * This method is used to get Expiry Date of current Membership ID 
	   * @param membershipNo
	   * @param brand_ID
	   * @author vivek.sawant
	   * @return CMSVIPMembershipDetail
	   * @throws RemoteException 
	   */
	  public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID ) throws RemoteException {
		  long start = getStartTime();
			try {
				if (!isAvailable()) {
					throw new ConnectException("Service is not available");
				}
				incConnection();
				return (CMSVIPMembershipDetail) ((CMSCustomerServices) CMSCustomerServices
						.getCurrent()).selectByMembershipNumber(membershipNo, brand_ID);
			} catch (Exception e) {
				throw new RemoteException(e.getMessage(), e);
			} finally {
				addPerformance("getTransactionDataByEmpRule", start);
				decConnection();
			}
	  }

		/**
		  * This method return Membership Id depends on Customer ID 
		  * @param Customer ID
		  * @author vivek.sawant
		  * @return String
		  */
	public String getVIPMembershipID(String customerID) throws Exception {
		  long start = getStartTime();
			try {
				if (!isAvailable()) {
					throw new ConnectException("Service is not available");
				}
				incConnection();
				return (String) ((CMSCustomerServices) CMSCustomerServices
						.getCurrent()).getVIPMembershipID(customerID);
			} catch (Exception e) {
				throw new RemoteException(e.getMessage(), e);
			} finally {
				addPerformance("getTransactionDataByEmpRule", start);
				decConnection();
			}
	}
	
	  /**
	   * This method return date of creation/modify of customer 
	   * @param Customer ID
	   * @author vivek.sawant
	   * @return Date
	   */
		
		public Date getCustomerCreatationDate(String customerID)throws Exception{
			  long start = getStartTime();
				try {
					if (!isAvailable()) {
						throw new ConnectException("Service is not available");
					}
					incConnection();
					return (Date) ((CMSCustomerServices) CMSCustomerServices
							.getCurrent()).getCustomerCreatationDate(customerID);
				} catch (Exception e) {
					throw new RemoteException(e.getMessage(), e);
				} finally {
					decConnection();
				}
			
		}

	public String[] getCardToken(String customerId) throws Exception {
		// TODO Auto-generated method stub
		long start = getStartTime();
		try {
			if (!isAvailable()) {
				throw new ConnectException("Service is not available");
			}
			incConnection();
			return  ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getCardToken(customerId);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		} finally {
			decConnection();
		}
	}
	  

	
}

