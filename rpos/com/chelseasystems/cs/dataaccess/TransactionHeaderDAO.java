/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.pos.TransactionHeader;
import  com.chelseasystems.cr.pos.AdHocQueryConstraints;
import  com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cs.pos.TransactionSearchString;
import  java.sql.SQLException;
import  java.util.Date;


public interface TransactionHeaderDAO extends BaseDAO
{

  /**
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDates (String storeId, Date begin, Date end) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @param consultantId
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndConsultantId (String storeId, Date begin, Date end, String consultantId) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @param discountType
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndDiscountType (String storeId, Date begin, Date end, String discountType) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @param amount
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndAmount (String storeId, Date begin, Date end, ArmCurrency amount) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @param operatorId
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndOperatorId (String storeId, Date begin, Date end, String operatorId) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @param paymentType
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndPaymentType (String storeId, Date begin, Date end, String paymentType) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @param creditPaymentTypes
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndCreditPaymentTypes (String storeId, Date begin, Date end, String[] creditPaymentTypes) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @param transactionType
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndTransactionType (String storeId, Date begin, Date end, String transactionType) throws SQLException;



  /**
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByStoreIdAndDatesAndShippingRequested (String storeId, Date begin, Date end) throws SQLException;



  /**
   * @param customerId
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByCustomerId (String customerId) throws SQLException;



  /**
   * @param sCustomerId
   * @param dtStart
   * @param dtEnd
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByCustomerIdAndDates (String sCustomerId, Date dtStart, Date dtEnd) throws SQLException;



  /**
   * @param txnSearchString
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectBySearchCriteria (TransactionSearchString txnSearchString) throws SQLException;



  /**
   * @param sCustomerId
   * @param sPaymentType
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByCustomerIdAndPaymentType (String sCustomerId, String sPaymentType) throws SQLException;



  /**
   * @param sCustomerId
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByCustomerIdAndShippingRequested (String sCustomerId) throws SQLException;



  /**
   * @param adHocQueryConstraints
   * @return
   * @exception SQLException
   */
  public TransactionHeader[] selectByAdHocQueryConstraints (AdHocQueryConstraints adHocQueryConstraints) throws SQLException;
}



