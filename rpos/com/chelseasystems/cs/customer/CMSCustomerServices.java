/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;


/**
 *
 * <p>Title: CMSCustomerServices</p>
 *
 * <p>Description: This is an abstract extension of customer services</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class CMSCustomerServices extends CustomerServices {
	
	 /**
	   * vishal : added for customer search in europe (franchising store requirement) 16 sept 2016
	   */
	  public abstract Customer[] findBySearchQuery(String searchStr,boolean isFranchisingStore)
	      throws Exception;

	
  /**
   * This method is used to search for customers on the basis of matching
   * search string
   * @param searchStr search string
   */
  public abstract Customer[] findBySearchQuery(String searchStr)
      throws Exception;

  /**
   * This Methoed is used to search for customer using search criteria
   * @param searchStr CustomerSearchString
   * @throws Exception
   * @return Customer[]
   */
  public abstract Customer[] findBySearchQuery(CustomerSearchString searchStr)
      throws Exception;

  /**
   * This method is used to search for customers on the basis of matching
   * reward card id
   * @param rcId reward card id
   */
  public abstract Customer[] findByRewardCard(String rcId)
      throws Exception;

  /**
   * This method is used to search customers by a barcode
   * @param sBarcode Barcode
   */
  public abstract Customer[] findByBarcode(String sBarcode)
      throws Exception;

  /**
   * This method is used to get customer sale summary
   * @param custId customer id
   * @param storeId store id
   */
  public abstract CustomerSaleSummary[] getCustSaleSummary(String custId, String storeId)
      throws Exception;

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  public abstract CMSCustomerMessage[] getAllCustomerMessages()
      throws Exception;

  /**
   * put your documentation comment here
   * @param customerId
   * @return
   * @exception Exception
   */
  public abstract DepositHistory[] getDepositHistory(String customerId)
      throws Exception;
  /**
   * put your documentation comment here
   * @param customerId , storeId
   * @return
   * @exception Exception
   */
  public abstract DepositHistory[] getDepositHistory(String customerId,String storeId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param customerId , storeId
   * @return
   * @exception Exception
   */
  public abstract CreditHistory[] getCreditHistory(String customerId,String storeId)
      throws Exception;
  
  /**
   * This method is used to Submit Customer and return it to client.
   * @param customer Customer
   * @return Customer
   * @author Jin Zhu
   */
  public abstract Customer submitAndReturn(Customer ustomer)
      throws Exception;
  
  /**
   * This method is used to get transaction data by employee rule 
   * @param customerId
   * @param countryCode
   * @return Map
   */
  public abstract Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode)
  	throws Exception;
  
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
  public abstract Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode,CMSCustomerAlertRule rules[],String id_brand)
	throws Exception;
  
  /**
   * This method is used to get customer alert rules
   * @param countryCode
   */
  public abstract CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode)
      throws Exception;
  
  /**
   * This method is used to get transaction data by employee rule 
   * @param countryCode
   * @param discountLevel
   * @param id_brand
   * @author vivek.sawant
   * @return CMSCustomerAlertRule[]
   */
  public abstract CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,String brandID)
  throws Exception;
  
  /**
   * This method is used to get transaction data by employee rule 
   * @param countryCode
   * @param discountLevel
   *  * @param current date
   * @param id_brand
   * @author Vishal
   * @return CMSCustomerAlertRule[]
   */
  public abstract CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode,String brandID, java.sql.Date businessDate)
  throws Exception;
  
  
  /**
   * This method is used to get Expiry Date of current Membership ID 
   * @param membershipNo
   * @param brand_ID
   * @author vivek.sawant
   * @return CMSVIPMembershipDetail
   */
  public abstract CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID)throws Exception;

  /**
   * This method return Membership Id depends on Customer ID 
   * @param Customer ID
   * @author vivek.sawant
   * @return String
   */
  public abstract String getVIPMembershipID(String customerID)throws Exception;
	/**
   * This method return date of creation/modify of customer 
   * @param Customer ID
   * @author vivek.sawant
   * @return Date1
   */
	
	public abstract Date getCustomerCreatationDate(String customerID)throws Exception;
	
	/**
	 * 
	 * SAP CRM
	 * 
	 */
	public abstract Customer[] selectNewCustomersForStore (String storeId)throws Exception;
	
	public abstract String[] getCardToken(String customerId) throws Exception ;
		
	
	
  
}

