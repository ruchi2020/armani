/*
 * @copyright (c) 2002 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.customer;

import java.util.Date;
import java.util.Map;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cr.telephone.*;

/**
 *
 * <p>Title: CMSCustomerClientServices</p>
 *
 * <p>Description: This is client side object for retrieving and submitting
 * employee information.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSCustomerClientServices extends ClientServices {

  /** Configuration manager **/
  //private ConfigMgr config = null;
  /**
   * Default Constructor which set the current implementation
   */
  public CMSCustomerClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("customer.cfg");
  }

  /**
   * This method initialize the primary implementation
   */
  public void init(boolean online)
      throws Exception {
    // Set up the proper implementation of the service.
    if (online) {
      onLineMode();
    } else {
      offLineMode();
    }
  }

  /**
   * Reads "CLIENT_IMPL" from config file. Returns the class that defines
   * what object is providing the service to objects using this client service
   * in "on-line" mode, i.e. connected to an app server.  If null, this 
   * clientservice is not considered when determining app online status.
   * @return a class of the online service.
   */
  protected Class getOnlineService () throws ClassNotFoundException {
    String className = config.getString("CLIENT_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }

  /**
   * This method is invoked when system is online, to get the client remote
   * implementation from the employee.cfg and set the same in CMSEmployeeServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSCustomerClientServices");
    CMSCustomerServices serviceImpl = (CMSCustomerServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSCustomerClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSCustomerServices in customer.cfg."
          , "Make sure that customer.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSCustomerServices."
          , LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSCustomerServices.setCurrent(serviceImpl);
  }

  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the employee.cfg and set the same in CMSEmployeeServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSCustomerClientServices");
    CMSCustomerServices serviceImpl = (CMSCustomerServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSCustomerClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSCustomerServices in customer.cfg."
          , "Make sure that customer.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSCustomerServices.", LoggingServices.CRITICAL);
    }
    CMSCustomerServices.setCurrent(serviceImpl);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return CMSCustomerServices.getCurrent();
  }

  /**
   * This method is used to merge two Customers
   * @param customer1 customer1
   * @param customer2 customer2
   */
  public boolean merge(CMSCustomer customer1, CMSCustomer customer2)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (boolean)CMSCustomerServices.getCurrent().merge(customer1, customer2);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "merge"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (boolean)CMSCustomerServices.getCurrent().merge(customer1, customer2);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId customerId
   */
  public CMSCustomer findById(String customerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer)CMSCustomerServices.getCurrent().findById(customerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findById"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer)CMSCustomerServices.getCurrent().findById(customerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to seach   Deposit by coustomer id
   * @param customerId customerId
   */
  public DepositHistory[] getDepositHistory(String customerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (DepositHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).
          getDepositHistory(customerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getDepositHistory"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (DepositHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).
          getDepositHistory(customerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to seach   Deposit by coustomer id
   * @param customerId customerId
   */
  public DepositHistory[] getDepositHistory(String customerId,String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (DepositHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).
          getDepositHistory(customerId,storeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getDepositHistory with store"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (DepositHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).
          getDepositHistory(customerId,storeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  /**
   * This method is used to seach   Deposit by coustomer id
   * @param customerId customerId
   */
  public CreditHistory[] getCreditTenderHistory(String customerId,String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CreditHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).
          getCreditHistory(customerId,storeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getCreditHistory with store"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CreditHistory[])((CMSCustomerServices)(CMSCustomerServices.getCurrent())).
          getCreditHistory(customerId,storeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  /**
   * This method is used to presist customer to data store
   * @param customer customer
   */
  public CMSCustomer submit(CMSCustomer customer)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer)((CMSCustomerServices)CMSCustomerServices.getCurrent()).submitAndReturn(
          customer);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "submit"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer)((CMSCustomerServices)CMSCustomerServices.getCurrent()).submitAndReturn(
          customer);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search customers by a telephone number
   * @param phoneNumber phoneNumber
   */
  public CMSCustomer[] findByPhone(String phoneNumber)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])CMSCustomerServices.getCurrent().findByPhone(phoneNumber);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByPhone"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])CMSCustomerServices.getCurrent().findByPhone(phoneNumber);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search customers by a telephone
   * @param aTelephone a telephone
   */
  public Customer[] findByTelephone(Telephone aTelephone)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Customer[])CMSCustomerServices.getCurrent().findByTelephone(aTelephone);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByTelephone"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Customer[])CMSCustomerServices.getCurrent().findByTelephone(aTelephone);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search Customer by their last name and postal code
   * @param lastName lastName
   * @param zip zip
   */
  public CMSCustomer[] findByLastNameZipCode(String lastName, String zip)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])CMSCustomerServices.getCurrent().findByLastNameZipCode(lastName, zip);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByLastNameZipCode"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])CMSCustomerServices.getCurrent().findByLastNameZipCode(lastName, zip);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search customers by Barcode
   * @param lastName Barcode
   *
   */
  public CMSCustomer[] findByBarcode(String sBarcode)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).findByBarcode(sBarcode);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByBarcode"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).findByBarcode(sBarcode);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search customers by last and first names
   * @param lastName last name
   * @param firstName first name
   */
  public CMSCustomer[] findByLastNameFirstName(String lastName, String firstName)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])CMSCustomerServices.getCurrent().findByLastNameFirstName(lastName
          , firstName);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByLastNameFirstName"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])CMSCustomerServices.getCurrent().findByLastNameFirstName(lastName
          , firstName);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  
  /**
   * 
   * SAP CRM
   * 
   * 
   */
  public CMSCustomer[] selectNewCustomersForStore(String storeId)
  throws Exception {
try {
  this.fireWorkInProgressEvent(true);
  LoggingServices.getCurrent().logMsg("Begin CMSCustomerClientServices.selectNewCustomersForStore->");

  return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).selectNewCustomersForStore(storeId);
} catch (DowntimeException ex) {
	ex.getStackTrace();
  LoggingServices.getCurrent().logMsg(getClass().getName(), "selectNewCustomersForStore"
      , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
      , "See Exception", LoggingServices.MAJOR, ex);
  offLineMode();
  setOffLineMode();
  return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).selectNewCustomersForStore(storeId );
} finally {
  this.fireWorkInProgressEvent(false);
  LoggingServices.getCurrent().logMsg("End CMSCustomerClientServices.selectNewCustomersForStore->");
}
}
  
  
  
  
  
  
  /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   */
  public CMSCustomer[] findBySearchQuery(String searchStr,boolean isFranchisingStore)
      throws Exception {
    try {
      System.out.println("Inside CMSCustomerClientServices   :"+searchStr);
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findBySearchQuery(searchStr,isFranchisingStore);
    } catch (TooManySearchResultsException ex) {
      throw ex;
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findBySearchQuery"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findBySearchQuery(searchStr,isFranchisingStore);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  /**
   * This method is used to search customers matching search string
   * @param searchStr search string
   */
  public CMSCustomer[] findBySearchQuery(String searchStr)
      throws Exception {
    try {
      System.out.println("Inside CMSCustomerClientServices   :"+searchStr);
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findBySearchQuery(searchStr);
    } catch (TooManySearchResultsException ex) {
      throw ex;
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findBySearchQuery"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findBySearchQuery(searchStr);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Lookup customer based on advance search criteria.
   * @param searchStr CustomerSearchString
   * @throws Exception
   * @return CMSCustomer[]
   */
  public CMSCustomer[] findBySearchQuery(CustomerSearchString searchStr)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findBySearchQuery(searchStr);
    } catch (TooManySearchResultsException ex) {
      throw ex;
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findBySearchQuery"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findBySearchQuery(searchStr);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search customers on the basis of reward card id
   * @param rcId reward card id
   */
  public CMSCustomer[] findByRewardCard(String rcId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findByRewardCard(rcId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByRewardCard"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomer[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          findByRewardCard(rcId);
    } finally {
      this.fireWorkInProgressEvent(false);
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
      this.fireWorkInProgressEvent(true);
      return (CustomerSaleSummary[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          getCustSaleSummary(custId, storeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getCustSaleSummary"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CustomerSaleSummary[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          getCustSaleSummary(custId, storeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to get a default customer
   */
  public Customer getDefaultCustomer()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Customer)CMSCustomerServices.getCurrent().getDefaultCustomer();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getDefaultCustomer"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Customer)CMSCustomerServices.getCurrent().getDefaultCustomer();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to get all customers messages.
   */
  public CMSCustomerMessage[] getAllCustomerMessages()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomerMessage[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          getAllCustomerMessages();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getgetAllCustomerMessages"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomerMessage[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
          getAllCustomerMessages();
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

 /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @return Map
   */
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Map)((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      	getTransactionDataByEmpRule(customerId, countryCode, societyCode);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getTransactionDataByEmpRule"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Map)((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      	getTransactionDataByEmpRule(customerId, countryCode, societyCode);
    } finally {
      this.fireWorkInProgressEvent(false);
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
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode,CMSCustomerAlertRule rules[],String id_brand)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Map)((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      	getTransactionDataByEmpRule(customerId, countryCode, societyCode,rules,id_brand);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getTransactionDataByEmpRule"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Map)((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      	getTransactionDataByEmpRule(customerId, countryCode, societyCode,rules,id_brand);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  /**
   * This method is used to get customer alert rules
   * @param countryCode
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCustomerAlertRule[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      getAllCustomerAlertRules(countryCode);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getAllCustomerAlertRules"
          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCustomerAlertRule[])((CMSCustomerServices)CMSCustomerServices.getCurrent()).
      getAllCustomerAlertRules(countryCode);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  /**
	 * This method is used to get transaction data by employee rule
	 * 
	 * @param discountLevel
	 * @param id_brand
	 * @author vivek.sawant
	 * @return CMSCustomerAlertRule
	 */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,
			String brandID) throws Exception {
		try {
			this.fireWorkInProgressEvent(true);
			return (CMSCustomerAlertRule[]) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getAllCustomerAlertRules(countryCode,
					brandID);
		} catch (DowntimeException ex) {
			LoggingServices
					.getCurrent()
					.logMsg(
							getClass().getName(),
							"getAllCustomerAlertRules",
							"Primary Implementation for CMSCustomerServices failed, going Off-Line...",
							"See Exception", LoggingServices.MAJOR, ex);
			offLineMode();
			setOffLineMode();
			return (CMSCustomerAlertRule[]) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getAllCustomerAlertRules(countryCode,brandID);
		} finally {
			this.fireWorkInProgressEvent(false);
		}
	}
	/**
	 * This method is used to get CustomerAlertRule
	 * 
	 * @param countryCode
	 * @param id_brand
	 * @param businessDate
	 * @author vishal
	 * @return CMSCustomerAlertRule
	 */
	public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,
			String brandID, java.sql.Date businessDate) throws Exception {
		try {
			this.fireWorkInProgressEvent(true);
			return (CMSCustomerAlertRule[]) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getAllCustomerAlertRules(countryCode,
					brandID, businessDate);
		} catch (DowntimeException ex) {
			LoggingServices
					.getCurrent()
					.logMsg(
							getClass().getName(),
							"getAllCustomerAlertRules",
							"Primary Implementation for CMSCustomerServices failed, going Off-Line...",
							"See Exception", LoggingServices.MAJOR, ex);
			offLineMode();
			setOffLineMode();
			return (CMSCustomerAlertRule[]) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getAllCustomerAlertRules(countryCode,
							brandID, businessDate);
		} finally {
			this.fireWorkInProgressEvent(false);
		}
	}
	 /**
	   * This method is used to get Expiry Date of current Membership ID 
	   * @param membershipNo
	   * @param brand_ID
	   * @author vivek.sawant
	   * @return CMSVIPMembershipDetail
	   */
	public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID){
		CMSVIPMembershipDetail membershipDetail = new CMSVIPMembershipDetail();
		try {
			this.fireWorkInProgressEvent(true);
			membershipDetail = (CMSVIPMembershipDetail) ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).selectByMembershipNumber(membershipNo ,brand_ID);
		} catch (DowntimeException ex) {
			LoggingServices
					.getCurrent()
					.logMsg(
							getClass().getName(),
							"getAllCustomerAlertRules",
							"Primary Implementation for CMSCustomerServices failed, going Off-Line...",
							"See Exception", LoggingServices.MAJOR, ex);
			offLineMode();
			setOffLineMode();
			try {
				membershipDetail = (CMSVIPMembershipDetail) ((CMSCustomerServices) CMSCustomerServices
						.getCurrent()).selectByMembershipNumber(membershipNo ,brand_ID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.fireWorkInProgressEvent(false);
		}
		return membershipDetail;
	}
	/**
	   * This method return Membership Id depends on Customer ID 
	   * @param Customer ID
	   * @author vivek.sawant
	   * @return String
	   */
	public String getVIPMembershipID(String customerID){
		String membershipID = new String();
		try {
			this.fireWorkInProgressEvent(true);
			membershipID = (String)((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getVIPMembershipID(customerID);
		} catch (DowntimeException ex) {
			LoggingServices
					.getCurrent()
					.logMsg(
							getClass().getName(),
							"getAllCustomerAlertRules",
							"Primary Implementation for CMSCustomerServices failed, going Off-Line...",
							"See Exception", LoggingServices.MAJOR, ex);
			offLineMode();
			setOffLineMode();
			try {
				membershipID = (String)((CMSCustomerServices) CMSCustomerServices
						.getCurrent()).getVIPMembershipID(customerID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.fireWorkInProgressEvent(false);
		}
		return membershipID;
	}
	  /**
	   * This method return date of creation/modify of customer 
	   * @param Customer ID
	   * @author vivek.sawant
	   * @return Date
	   */
		
		public Date getCustomerCreatationDate(String customerID)throws Exception{
			Date date = new Date();
			try {
				this.fireWorkInProgressEvent(true);
				date = (Date)((CMSCustomerServices) CMSCustomerServices
						.getCurrent()).getCustomerCreatationDate(customerID);
			} catch (DowntimeException ex) {
				LoggingServices
						.getCurrent()
						.logMsg(
								getClass().getName(),
								"getCustomerCreatationDate",
								"Primary Implementation for CMSCustomerServices failed, going Off-Line...",
								"See Exception", LoggingServices.MAJOR, ex);
				offLineMode();
				setOffLineMode();
				try {
					date = (Date)((CMSCustomerServices) CMSCustomerServices
							.getCurrent()).getCustomerCreatationDate(customerID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
			    	e.printStackTrace();
			} finally {
				this.fireWorkInProgressEvent(false);
			}
			return date;
		
			
		}
		public String[] getCardToken(String customerId)throws Exception
		{
			try {
				this.fireWorkInProgressEvent(true);
			((CMSCustomerServices) CMSCustomerServices
						.getCurrent()).getCardToken(customerId);
			return ((CMSCustomerServices) CMSCustomerServices
					.getCurrent()).getCardToken(customerId);
			} catch (DowntimeException ex) {
				LoggingServices
						.getCurrent()
						.logMsg(
								getClass().getName(),
								"getCardToken",
								"Primary Implementation for CMSCustomerServices failed, going Off-Line...",
								"See Exception", LoggingServices.MAJOR, ex);
				offLineMode();
				setOffLineMode();
				return null;
			} finally {
				this.fireWorkInProgressEvent(false);
			}

			}
}

