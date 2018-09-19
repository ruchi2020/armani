/*
 * @copyright (c) 2002 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.rmi.*;
import java.util.Date;
import java.util.Map;

import com.igray.naming.*;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cs.customer.CustomerSearchString;
import com.chelseasystems.cr.telephone.*;


/**
 *
 * <p>Title: ICMSCustomerRMIServer</p>
 *
 * <p>Description: Defines the customer services that are available remotely via RMI.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ICMSCustomerRMIServer extends Remote, IPing {

  /**
   * This method is used to merge two Customers
   * @param customer1 customer1
   * @param customer2 customer2
   */
  public boolean merge(CMSCustomer customer1, CMSCustomer customer2)
      throws RemoteException;


  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId customerId
   */
  public CMSCustomer findById(String customerId)
      throws RemoteException;


  /**
   * This method is used to presist customer to data store
   * @param customer customer
   */
  public CMSCustomer submit(CMSCustomer customer)
      throws RemoteException;


  /**
   * This method is used to search customers by a telephone number
   * @param phoneNumber phoneNumber
   */
  public CMSCustomer[] findByPhone(String phoneNumber)
      throws RemoteException;


  /**
   * This method is used to search customers by a barcode
   * @param sBarcode Barcode
   */
  public CMSCustomer[] findByBarcode(String sBarcode)
      throws RemoteException;

  /**
   * This method is used to search customers by a telephone
   * @param aTelephone a telephone
   */
  public Customer[] findByTelephone(Telephone aTelephone)
      throws RemoteException;


  /**
   * This method is used to search Customer by their last name and postal code
   * @param lastName lastName
   * @param zip zip
   */
  public CMSCustomer[] findByLastNameZipCode(String lastName, String zip)
      throws RemoteException;


  /**
   * This method is used to search for customers by last and first names
   * @param lastName last name
   * @param firstName first name
   */
  public CMSCustomer[] findByLastNameFirstName(String lastName, String firstName)
      throws RemoteException;


  /**
   * This method is used to search for depositHistory
   * @param String custId
   */
  public DepositHistory[] getDepositHistory(String custId)
      throws RemoteException;


  /**
   * This method is used to search for depositHistoryForStore
   * @param String custId, storeId
   */
  public DepositHistory[] getDepositHistory(String custId,String storeId)
      throws RemoteException;

  /**
   * This method is used to search for creditHistoryForStore
   * @param String custId, storeId
   */
  public CreditHistory[] getCreditHistory(String custId,String storeId)
      throws RemoteException;
  
  /**
   * This method is used to get a default customer
   */
  public Customer getDefaultCustomer()
      throws RemoteException;

  /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   */
  public CMSCustomer[] findBySearchQuery(String searchStr,boolean isFranchisingStore)
      throws RemoteException;


  /**
   * This method is used to search for customers matching search string
   * @param searchStr search string
   */
  public CMSCustomer[] findBySearchQuery(String searchStr)
      throws RemoteException;


  /**
   * This method is used for Advance Customer Search.
   * @param searchStr CustomerSearchString
   * @throws RemoteException
   * @return CMSCustomer[]
   */
  public CMSCustomer[] findBySearchQuery(CustomerSearchString searchStr)
      throws RemoteException;


  /**
   * This method is used to search for customers matching reward card id
   * @param rcId reward card id
   */
  public CMSCustomer[] findByRewardCard(String rcId)
      throws RemoteException;


  /**
   * This method is used to search get customer sale summary
   * @param custId customer id
   * @param storeId store id
   */
  public CustomerSaleSummary[] getCustSaleSummary(String custId, String storeId)
      throws RemoteException;


  /**
   * This method is used to search get all customer messages
   *  return CMSCustomerMessage
   *
   */
  public CMSCustomerMessage[] getAllCustomerMessages()
      throws RemoteException;
  
  /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @return Map
   */
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode)
      throws RemoteException;
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
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode,CMSCustomerAlertRule rules[],String id_brand)
      throws RemoteException;
  
 
  /**
   * This method is used to get customer alert rules
   * @param countryCode
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
      throws RemoteException;
    /**
	 * This method is used to get transaction data by employee rule
	 * @param countryCode
	 * @param discountLevel
	 * @param id_brand
	 * @author vivek.sawant
	 * @return CMSCustomerAlertRule
	 */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,String brandID)
  throws RemoteException;
  
  /**
	 * This method is used to get transaction data by employee rule
	 * @param countryCode
	 * @param discountLevel
	 * @param id_brand
	 * @param businessDate
	 * @author vishal
	 * @return CMSCustomerAlertRule
	 */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,
			String brandID, java.sql.Date businessDate)
  throws RemoteException;
  
  /**
   * This method is used to get Expiry Date of current Membership ID 
   * @param membershipNo
   * @param brand_ID
   * @author vivek.sawant
   * @return CMSVIPMembershipDetail
   */
  public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo , String brand_ID) throws Exception;
  
  /**
   * This method return Membership Id depends on Customer ID 
   * @param Customer ID
   * @author vivek.sawant
   * @return String
   */
  public String getVIPMembershipID(String customerID) throws Exception;
	
  /**
   * This method return date of creation/modify of customer 
   * @param Customer ID
   * @author vivek.sawant
   * @return Date
   */
	
	public Date getCustomerCreatationDate(String customerID)throws Exception;
  

	
	/**
	 * 
	 * SAP CRM 
	 * 
	 */
	
	public Customer[] selectNewCustomersForStore(String storeId)throws Exception;
	
	
	public String[] getCardToken(String customerId)throws Exception;



}

