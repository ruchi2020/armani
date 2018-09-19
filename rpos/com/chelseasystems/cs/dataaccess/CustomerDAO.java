/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cr.customer.*;
import  com.chelseasystems.cr.telephone.*;
import  com.chelseasystems.cs.customer.*;
import  com.chelseasystems.cs.customer.DepositHistory;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cr.pos.PaymentTransaction;
import  com.chelseasystems.cs.customer.CustomerSearchString;


public interface CustomerDAO extends BaseDAO
{

  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Customer object) throws SQLException;



  /**
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Customer object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void insert (Customer object) throws SQLException;



  /**
   * @param object
   * @exception SQLException
   */
  public void update (Customer object) throws SQLException;



  /**
   * @param id
   * @return
   * @exception SQLException
   */
  public Customer selectById (String id) throws SQLException;



  /**
   * @deprecated use selectByTelephoneNumber(Telephone)
   */
  public Customer[] selectByPhone (String phone) throws SQLException;



  /**
   * @param telephone
   * @return
   * @exception Exception
   */
  public Customer[] selectByTelephone (Telephone telephone) throws Exception;



  /**
   * @param lastName
   * @param zipCode
   * @return
   * @exception SQLException
   */
  public Customer[] selectByLastNameZipCode (String lastName, String zipCode) throws SQLException;



  /**
   * @param lastName
   * @param firstName
   * @return
   * @exception Exception
   */
  public Customer[] selectByLastNameFirstName (String lastName, String firstName) throws Exception;



  /**
   * ADDED VISHAL FOR FRANCHISING STORE REQUIREMENT 16 SEPT 2016
   */
  public Customer[] selectBySearchQuery (String searchStr,boolean isFranchisingStore) throws Exception;


  /**
   * @param searchStr
   * @return
   * @exception Exception
   */
  public Customer[] selectBySearchQuery (String searchStr) throws Exception;



  /**
   * @param customerSearchString
   * @return
   * @exception Exception
   */
  public Customer[] selectBySearchQuery (CustomerSearchString customerSearchString) throws Exception;



  /**
   * @param rcId
   * @return
   * @exception Exception
   */
  public Customer[] selectByRewardCard (String rcId) throws Exception;

  /**
   * Select Customer by Barcode
   * @param sBarcode String
   * @throws SQLException
   * @return Customer[]
   */
  public Customer[] selectByBarcode(String sBarcode) throws Exception;

  /**
   * @param custId
   * @param storeId
   * @return
   * @exception SQLException
   */
  public CustomerSaleSummary[] getCustSaleSummary (String custId, String storeId) throws SQLException;



  /**
   * @param customerId
   * @param loyaltyMemberFlag
   * @return
   */
  public ParametricStatement getUpdateSQLForCustomerLoyaltyFlag (String customerId, boolean loyaltyMemberFlag);



  /**
   * @return
   * @exception SQLException
   */
  public CMSCustomerMessage[] getAllCustomerMessages () throws SQLException;



  /**
   * @param custMsg
   * @param cust
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQLForMessages (CMSCustomerMessage custMsg, Customer cust) throws SQLException;



  /**
   * @param custMsg
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQLForCustomerMessage (CMSCustomerMessage custMsg) throws SQLException;



  /**
   * @param depositHistory
   * @return
   * @exception SQLException
   */
  public ParametricStatement getInsertDepositHistorySQL (DepositHistory depositHistory) throws SQLException;



  /**
   * @param depositHistory
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getInsertDepositHistorySQL (DepositHistory depositHistory, PaymentTransaction object) throws SQLException;



  /**
   * @param cmsCust
   * @param currAmount
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateDepositSQL (CMSCustomer cmsCust, ArmCurrency currAmount) throws SQLException;



  /**
   * @param custId
   * @return
   * @exception SQLException
   */
  public DepositHistory[] getDepositHistory (String custId) throws SQLException;

  /**
  *
  * @param custId String, storeId String
  * @param transactionId String
  * @return DepositHistory[]
  */
  public DepositHistory[] getDepositHistoryForStore (String custId, String storeId);
  /**
  *
  * @param custId String, storeId String
  * @param transactionId String
  * @return CreditHistory[]
  */
  public CreditHistory[] getCreditHistoryForStore (String custId, String storeId);
  /**
   *
   * @param custId String
   * @param transactionId String
   * @return DepositHistory[]
   */
  public DepositHistory[] getDepositHistory (String custId, String transactionId);

  /**
   *
   * @param depositHistory DepositHistory
   * @param object PaymentTransaction
   * @return ParametricStatement
   */
  public ParametricStatement getUpdateDepositHistorySQLForDelete(DepositHistory depositHistory);
  /**
   * @param creditHistory
   * @return
   * @exception SQLException
   */
  public ParametricStatement getInsertCreditHistorySQL (CreditHistory creditHistory) throws SQLException;



  /**
   * @param creditHistory
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getInsertCreditHistorySQL (CreditHistory creditHistory, PaymentTransaction object) throws SQLException;
  
  
  /**
   * @param customerId
   * @param countryCode
   * @return Map
   * @exception SQLException
   */
  public Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode) throws SQLException;
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
  public  Map getTransactionDataByEmpRule(String customerId, String countryCode, String societyCode,CMSCustomerAlertRule rules[],String id_brand) throws SQLException;
  /**
   * @param countryCode
   * @return CMSCustomerAlertRule[]
   * @exception SQLException
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode) throws SQLException;
  /**
   * This method is used to get Expiry Date of current Membership ID 
   * @param countryCode
   * @param brandID
   * @author vivek.sawant
   * @return CMSCustomerAlertRule
   */
  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID) throws SQLException;
  
  /**
   * This method is used to get Expiry Date of current Membership ID 
   * @param countryCode
   * @param brandID
   * @author vishal
   * @return CMSCustomerAlertRule
   */

  public CMSCustomerAlertRule[] getAllCustomerAlertRules(String countryCode, String brandID, java.sql.Date businessDate) throws SQLException;
  /**
  * This method is used to get Expiry Date of current Membership ID 
  * @param membershipNo
  * @param brand_ID
  * @author vivek.sawant
  * @return CMSVIPMembershipDetail
  * 
  * 
  */
 public CMSVIPMembershipDetail selectByMembershipNumber(String membershipNo, String brand_ID) throws SQLException; 
 
 /**
  * This method return Membership Id depends on Customer ID 
  * @param Customer ID
  * @author vivek.sawant
  * @return String
  */
 public String getVIPMembershipID(String customerID) throws SQLException;
 /**
  * This method return date of creation/modify of customer 
  * @param Customer ID
  * @author vivek.sawant
  * @return Date
  */
 public Date getCustomerCreatationDate(String customerID)throws Exception;
 
 
 /**
  * 
  * This method takes store_id and register_id from store and returns the customer array 
  * present in SAP CRM table
  * 
  * 
  */
 
 public Customer[] selectNewCustomersForStore (String storeId) throws Exception;

 public String[] getCardToken(String customerId)  throws Exception;




}







