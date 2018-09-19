/*
 * @copyright (c) 2002 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.util.Date;
import java.util.Map;

import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cs.customer.CustomerSearchString;


/**
 *
 * <p>Title: CMSCustomerHelper</p>
 *
 * <p>Description: This class have static convenience methods to manipulate
 * Client Services</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSCustomerHelper {

  /**
   * This method is used to merge two Customers
   * @param customer1 customer1
   * @param customer2 customer2
   */
  public static boolean merge(IRepositoryManager theMgr, CMSCustomer customer1
      , CMSCustomer customer2)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.merge(customer1, customer2);
  }

  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId customerId
   */
  public static CMSCustomer findById(IRepositoryManager theMgr, String customerId)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findById(customerId);
  }

  /**
   * This method is used to seach a Customer by coustomer id
   * @param customerId customerId  getDepositHistory(cmsCust.getId())
   */
  public static DepositHistory[] getDepositHistory(IRepositoryManager theMgr, String customerId)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.getDepositHistory(customerId);
  }

  /**
   * This method is used to seach a Customer by coustomer id and storeid
   * @param customerId customerId  getDepositHistory(cmsCust.getId())
   */
  public static DepositHistory[] getDepositHistory(IRepositoryManager theMgr, String customerId,String storeId)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.getDepositHistory(customerId,storeId);
  }

  /**
   * This method is used to seach a Customer by coustomer id and storeid
   * @param customerId customerId  getCreditTenderHistory(cmsCust.getId())
   */
  public static CreditHistory[] getCreditTenderHistory(IRepositoryManager theMgr, String customerId,String storeId)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return (CreditHistory[])cs.getCreditTenderHistory(customerId,storeId);
  }


  /**
   * This method is used to presist customer to data store
   * @param customer customer
   */
  public static CMSCustomer submit(IRepositoryManager theMgr, CMSCustomer customer)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.submit(customer);
  }

  /**
   * This method is used to search customers by a telephone number
   * @param phoneNumber phoneNumber
   * @deprecated use findByTelephone(IRepositoryManager, Telephone)
   */
  public static CMSCustomer[] findByPhone(IRepositoryManager theMgr, String phoneNumber)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findByPhone(phoneNumber);
  }

  /**
   * This method is used to search customers by a telephone
   * @param aTelephone a telephone
   */
  public static Customer[] findByTelephone(IRepositoryManager theMgr, Telephone aTelephone)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findByTelephone(aTelephone);
  }

  /**
   * This method is used to search Customer by their last name and postal code
   * @param lastName lastName
   * @param zip zip
   */
  public static CMSCustomer[] findByLastNameZipCode(IRepositoryManager theMgr, String lastName
      , String zip)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findByLastNameZipCode(lastName, zip);
  }

  /**
   * This method is used to search customers by last and first names
   * @param lastName last name
   * @param firstName first name
   */
  public static CMSCustomer[] findByLastNameFirstName(IRepositoryManager theMgr, String lastName
      , String firstName)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findByLastNameFirstName(lastName, firstName);
  }

  /**o
   * 
   * SAP CRM
   * 
   */
  
  public static CMSCustomer[] findNewCustomersForStore(IRepositoryManager theMgr, String storeId
	      )
	      throws Exception {
	    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
	        "CUSTOMER_SRVC");
	     return cs.selectNewCustomersForStore(storeId);
	  }
  
  /**
   * This method is used to search customers by Barcode
   * @param lastName last name
   * @param firstName first name
   */
  public static CMSCustomer[] findByBarcode(IRepositoryManager theMgr, String sBarcode)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findByBarcode(sBarcode);
  }

   /**
   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
   */
  public static Customer[] findBySearchQuery(IRepositoryManager theMgr, String searchStr,boolean isFranchisingStore)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findBySearchQuery(searchStr,isFranchisingStore);
  }

  /**
   * This method is used to search customers matching search string
   * @param searchStr search string
   */
  public static Customer[] findBySearchQuery(IRepositoryManager theMgr, String searchStr)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findBySearchQuery(searchStr);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param searchStr
   * @return
   * @exception Exception
   */
  public static Customer[] findBySearchQuery(IRepositoryManager theMgr
      , CustomerSearchString searchStr)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findBySearchQuery(searchStr);
  }

  /**
   * This method is used to search customers on the basis of reward card id
   * @param rcId reward card id
   */
  public static Customer[] findByRewardCard(IRepositoryManager theMgr, String rcId)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.findByRewardCard(rcId);
  }

  /**
   * This method is used to get customer's sale summary on the basis of
   * customer id for a given store
   * @param custId customer id
   * @param storeId store id
   */
  public static CustomerSaleSummary[] getCustSaleSummary(IRepositoryManager theMgr, String custId
      , String storeId)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.getCustSaleSummary(custId, storeId);
  }

  /**
   * This method is used to get a default customer
   */
  public static Customer getDefaultCustomer(IRepositoryManager theMgr)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.getDefaultCustomer();
  }

  /**
   * This method is used to get a default customer
   */
  public static CMSCustomerMessage getAllCustomerMessages(IRepositoryManager theMgr)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return null;
  }

  /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @return Map
   */
  public static Map getTransactionDataByEmpRule(IRepositoryManager theMgr, 
		  String customerId, String countryCode, String societyCode)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.getTransactionDataByEmpRule(customerId, countryCode, societyCode);
  }
  /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @param discountLevel
   * @param id_brand
   * @author vivek.sawant
   * @return Map
   *  method Updated : Vishal Yevale : PCR for Employee budget threshold for multi level discount 29th May 2017 (EUROPE)
   */
  public static Map getTransactionDataByEmpRule(IRepositoryManager theMgr, 
		  String customerId, String countryCode, String societyCode,CMSCustomerAlertRule rules[],String id_brand)
      throws Exception {
	  CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return cs.getTransactionDataByEmpRule(customerId, countryCode, societyCode, rules,id_brand );
  }
  
  /**
   * This method is used to get customer alert rules
   * @param countryCode
   */
  public static CMSCustomerAlertRule getAllCustomerAlertRules(IRepositoryManager theMgr, String countryCode)
      throws Exception {
    CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
        "CUSTOMER_SRVC");
    return null;
  }
  
  
  public static CMSCustomerAlertRule[] getAllCustomerAlertRules(IRepositoryManager theMgr, String countryCode, String brandID)
  throws Exception {
CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
    "CUSTOMER_SRVC");
return cs.getAllCustomerAlertRules(countryCode,brandID);
}
  /**
   * This method is used to get customer alert rules
   * @param countryCode
   * @param brandID
   * @param businessDate
   * @author vishal
   */
  
  public static CMSCustomerAlertRule[] getAllCustomerAlertRules(IRepositoryManager theMgr, String countryCode, String brandID, java.sql.Date businessDate)
		  throws Exception {
		CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
		    "CUSTOMER_SRVC");
		return cs.getAllCustomerAlertRules(countryCode,brandID, businessDate);
	}
  
  /**
   * This method is used to get Expiry Date of current Membership ID 
   * @param membershipNo
   * @author vivek.sawant
   * @return CMSVIPMembershipDetail
   */
  public static CMSVIPMembershipDetail selectByMembershipNumber(IRepositoryManager theMgr,String membershipNo,String brand_ID){
	  CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
      "CUSTOMER_SRVC");
  return cs.selectByMembershipNumber(membershipNo, brand_ID);
	  
  }

  /**
   * This method return Membership Id depends on Customer ID 
   * @param Customer ID
   * @author vivek.sawant
   * @return String
   */
  public static String getVIPMembershipID(IRepositoryManager theMgr,String customerID){
	  CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
      "CUSTOMER_SRVC");
  return cs.getVIPMembershipID(customerID);
	  
  }
  /**
   * This method return date of creation/modify of customer 
   * @param Customer ID
   * @author vivek.sawant
   * @return Date
   */
	
	public static Date getCustomerCreatationDate(IRepositoryManager theMgr,String customerID)throws Exception{
		CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
	      "CUSTOMER_SRVC");
	  return cs.getCustomerCreatationDate(customerID);
		  
	}
	//Added by Sonali for getting list of token numbers
	public static String[] getCardTokens(String customerId, IRepositoryManager theMgr) throws Exception
	{
		CMSCustomerClientServices cs = (CMSCustomerClientServices)theMgr.getGlobalObject(
	      "CUSTOMER_SRVC");
	  return cs.getCardToken(customerId);
		}
}

