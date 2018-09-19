/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 05-10-2005 | Manpreet  | N/A       | New method findBySearchCriteria()            |
 --------------------------------------------------------------------------------------------
 | 3    | 04-29-2005 | Pankaja   | N/A       | New method for updation of the expiration dt |
 --------------------------------------------------------------------------------------------
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Presale/Cosignment impl                |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.pos.*;
import java.lang.*;
import java.util.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
//import com.chelseasystems.cs.item.CMSItemServices;


/**
 *
 * <p>Title: CMSTransactionPOSClientServices</p>
 *
 * <p>Description: Client-side object for retrieving and submitting transaction</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTransactionPOSClientServices extends ClientServices {

  /** Configuration manager **/
//  private ConfigMgr config = null;

  /**
   * Default constructor
   **/
  public CMSTransactionPOSClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("pos.cfg");
  }

  /**
   * This method initialize primary implementation
   * @param online boolean
   * @throws Exception
   */
  public void init(boolean online)
      throws Exception {
    // Set up the proper implementation of the service.
    if (online)
      onLineMode();
    else
      offLineMode();
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
   * This method is used set transaction pos services when client is running
   * in on line mode
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSTransactionPOSClientServices");
    CMSTransactionPOSServices serviceImpl = (CMSTransactionPOSServices)config.getObject(
        "CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSTransactionPOSClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSTransactionPOSServices in pos.cfg."
          , "Make sure that pos.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSTransactionPOSServices."
          , LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSTransactionPOSServices.setCurrent(serviceImpl);
  }

  /**
   * This method is used set transaction pos services when client is running
   * in off line mode
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSTransactionPOSClientServices");
    CMSTransactionPOSServices serviceImpl = (CMSTransactionPOSServices)config.getObject(
        "CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSTransactionPOSClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSTransactionPOSServices in pos.cfg."
          , "Make sure that pos.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSTransactionPOSServices.", LoggingServices.CRITICAL);
    }
    CMSTransactionPOSServices.setCurrent(serviceImpl);
  }

  /**
   * This method is used to get current services
   * @return Object
   */
  public Object getCurrentService() {
    return CMSTransactionPOSServices.getCurrent();
  }

  /**
   * This method is used to submit a CompositePOSTransaction to data store
   * @param paymentTransaction PaymentTransaction
   * @return boolean
   * @throws Exception
   */
  public boolean submit(PaymentTransaction paymentTransaction)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (boolean)CMSTransactionPOSServices.getCurrent().submit(paymentTransaction);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "submit"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (boolean)CMSTransactionPOSServices.getCurrent().submit(paymentTransaction);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *  This method is used to search a CompositePOSTransaction by its id
   * @param id String
   * @return PaymentTransaction
   * @throws Exception
   */
  public PaymentTransaction findById(String id)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (PaymentTransaction)CMSTransactionPOSServices.getCurrent().findById(id);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findById"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (PaymentTransaction)CMSTransactionPOSServices.getCurrent().findById(id);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search a CompositePOSTransactions by customer's id
   * @param customerId String
   * @return PaymentTransaction[]
   * @throws Exception
   */
  public PaymentTransaction[] findByCustomerId(String customerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (PaymentTransaction[])CMSTransactionPOSServices.getCurrent().findByCustomerId(
          customerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCustomerId"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (PaymentTransaction[])CMSTransactionPOSServices.getCurrent().findByCustomerId(
          customerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search Transaction Headers by customer's id
   * @param customerId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdHeader(String customerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByCustomerIdHeader(
          customerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCustomerIdHeader"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByCustomerIdHeader(
          customerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *
   * @param sCutomerId String
   * @param dtStart Date
   * @param dtEnd Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndDates(String sCutomerId, Date dtStart
      , Date dtEnd)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndDates(sCutomerId, dtStart, dtEnd);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findBySearchCriteria"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndDates(sCutomerId, dtStart, dtEnd);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *
   * @param sCutomerId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndShippingRequested(String sCutomerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndShippingRequested(sCutomerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "findByCustomerIdAndShippingRequested"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndShippingRequested(sCutomerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *
   * @param sCutomerId String
   * @param sPaymentType String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndPaymentType(String sCutomerId
      , String sPaymentType)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndPaymentType(sCutomerId, sPaymentType);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCustomerIdAndPaymentType"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndPaymentType(sCutomerId, sPaymentType);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Find by search Criteria
   * @param sQuery String
   * @throws Exception
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] findBySearchCriteria(TransactionSearchString txnSrchString)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findBySearchCriteria(txnSrchString);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findBySearchCriteria"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findBySearchCriteria(txnSrchString);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search a CompositePOSTransactions by store id and
   * a range of dates
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByDate(String storeId, Date beginDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByDate(storeId
          , beginDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByDate(storeId
          , beginDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by exact amount
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param amount Currency
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByExactAmount(String storeId, Date beginDate, Date endDate
      , ArmCurrency amount)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByExactAmount(
          storeId, beginDate, endDate, amount);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByExactAmount"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByExactAmount(
          storeId, beginDate, endDate, amount);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by payment type
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param paymentType String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByPaymentType(String storeId, Date beginDate, Date endDate
      , String paymentType)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByPaymentType(
          storeId, beginDate, endDate, paymentType);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByPaymentType"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByPaymentType(
          storeId, beginDate, endDate, paymentType);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by credit payment type
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param aPayments String[]
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCreditPaymentType(String storeId, Date beginDate
      , Date endDate, String[] aPayments)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByCreditPaymentType(
          storeId, beginDate, endDate, aPayments);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByCreditPaymentType"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByCreditPaymentType(
          storeId, beginDate, endDate, aPayments);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by discount type
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param discountType String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByDiscountType(String storeId, Date beginDate, Date endDate
      , String discountType)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByDiscountType(
          storeId, beginDate, endDate, discountType);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByDiscountType"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByDiscountType(
          storeId, beginDate, endDate, discountType);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by transaction type
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param transactionType String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByTransactionType(String storeId, Date beginDate, Date endDate
      , String transactionType)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByTransactionType(
          storeId, beginDate, endDate, transactionType);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByTransactionType"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByTransactionType(
          storeId, beginDate, endDate, transactionType);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by consultant id
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param consultantId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByConsultantID(String storeId, Date beginDate, Date endDate
      , String consultantId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByConsultantID(
          storeId, beginDate, endDate, consultantId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByConsultantID"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByConsultantID(
          storeId, beginDate, endDate, consultantId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by operator id
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param operatorId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByOperatorID(String storeId, Date beginDate, Date endDate
      , String operatorId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByOperatorID(
          storeId, beginDate, endDate, operatorId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByOperatorID"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByOperatorID(
          storeId, beginDate, endDate, operatorId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find CompositePOSTransactions by shipping request
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByShippingRequested(String storeId, Date beginDate
      , Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByShippingRequested(
          storeId, beginDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByShippingRequested"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByShippingRequested(
          storeId, beginDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to display all Transaction Ids for a specific
   * AdHocQueryConstraints
   * @param adHocQueryConstraints AdHocQueryConstraints
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByAdHocQueryConstraints(AdHocQueryConstraints
      adHocQueryConstraints)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().
          findByAdHocQueryConstraints(adHocQueryConstraints);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByAdHocQueryConstraints"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().
          findByAdHocQueryConstraints(adHocQueryConstraints);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to return the payment summaries for the specified
   * store for on specified date
   * @param storeId String
   * @param date Date
   * @return PaymentSummary[]
   * @throws Exception
   */
  public PaymentSummary[] getStorePaymentSummary(String storeId, Date date)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (PaymentSummary[])CMSTransactionPOSServices.getCurrent().getStorePaymentSummary(
          storeId, date);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getStorePaymentSummary"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (PaymentSummary[])CMSTransactionPOSServices.getCurrent().getStorePaymentSummary(
          storeId, date);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to return the transaction type summaries for the
   * specified store on the specified date
   * @param storeId String
   * @param date Date
   * @return TxnTypeSummary[]
   * @throws Exception
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (TxnTypeSummary[])CMSTransactionPOSServices.getCurrent().getStoreTxnTypeSummary(
          storeId, date);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getStoreTxnTypeSummary[1]"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (TxnTypeSummary[])CMSTransactionPOSServices.getCurrent().getStoreTxnTypeSummary(
          storeId, date);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to return the transaction type summaries for the
   * specified store on the specified date by the specified operator.
   * @param storeId String
   * @param date Date
   * @param operatorId String
   * @return TxnTypeSummary[]
   * @throws Exception
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date, String operatorId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (TxnTypeSummary[])CMSTransactionPOSServices.getCurrent().getStoreTxnTypeSummary(
          storeId, date, operatorId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getStoreTxnTypeSummary[2]"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (TxnTypeSummary[])CMSTransactionPOSServices.getCurrent().getStoreTxnTypeSummary(
          storeId, date, operatorId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to return the sales summaries for the specified
   * store for on specified date.
   * @param storeId String
   * @param date Date
   * @return SalesSummary[]
   * @throws Exception
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date date)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SalesSummary[])CMSTransactionPOSServices.getCurrent().getStoreSalesSummary(storeId
          , date);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getStoreSalesSummary[1]"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SalesSummary[])CMSTransactionPOSServices.getCurrent().getStoreSalesSummary(storeId
          , date);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to return the sales summaries for the specified store
   * for between the specified dates
   * @param storeId String
   * @param from Date
   * @param to Date
   * @return SalesSummary[]
   * @throws Exception
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date from, Date to)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SalesSummary[])CMSTransactionPOSServices.getCurrent().getStoreSalesSummary(storeId
          , from, to);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getStoreSalesSummary[2]"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SalesSummary[])CMSTransactionPOSServices.getCurrent().getStoreSalesSummary(storeId
          , from, to);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open pre sale on the basis of transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenPresaleById(String txnId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenPresaleById(txnId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenPresaleById"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenPresaleById(txnId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open pre sale on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByStore(String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByStore(storeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenPresaleByStore"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByStore(storeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open pre sale onthe basis of customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByCustomer(String custId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByCustomer(custId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenPresaleByCustomer"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByCustomer(custId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open pre sale on the basis of store id and
   * for specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByDate(String storeId, Date startDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByDate(storeId, startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenPresaleByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByDate(storeId, startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open consignment on the basis of transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenConsignmentById(String txnId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenConsignmentById(txnId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenConsignmentById"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenConsignmentById(txnId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open consignment on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByStore(String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByStore(storeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenConsignmentByStore"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByStore(storeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open consignment on the basis of customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByCustomer(String custId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByCustomer(custId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenConsignmentByCustomer"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByCustomer(custId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open consignment on the basis of store id and
   * for specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByDate(storeId, startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenConsignmentByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByDate(storeId, startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to update the expiration date of consignment transaction
   * @param txn ConsignmentTransaction
   * @return ConsignmentTransaction
   * @throws Exception
   */
  public ConsignmentTransaction updateConsignmentExpirationDate(ConsignmentTransaction txn)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ConsignmentTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).updateConsignmentExpirationDate(txn);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenConsignmentByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ConsignmentTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).updateConsignmentExpirationDate(txn);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to update the expiration date of pre sale transaction
   * @param txn PresaleTransaction
   * @return PresaleTransaction
   * @throws Exception
   */
  public PresaleTransaction updatePresaleExpirationDate(PresaleTransaction txn)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (PresaleTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updatePresaleExpirationDate(txn);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenConsignmentByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (PresaleTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updatePresaleExpirationDate(txn);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to update the expiration date of reservation transaction
   * @param txn ReservationTransaction
   * @return ReservationTransaction
   * @throws Exception
   */
  public ReservationTransaction updateReservationExpirationDate(ReservationTransaction txn)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ReservationTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updateReservationExpirationDate(txn);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenConsignmentByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ReservationTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updateReservationExpirationDate(txn);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This is new method added to find CompositePOSTransactions on the store id
   * and register id
   * @param storeId String
   * @param registerId String
   * @return String[]
   * @throws Exception
   */
  public String[] findTxnIdsByStoreIdAndRegisterId(String storeId, String registerId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (String[])((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findTxnIdsByStoreIdAndRegisterId(storeId, registerId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findTxnIdsByStoreIdAndRegisterId"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (String[])((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findTxnIdsByStoreIdAndRegisterId(storeId, registerId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  //added by shushma for promotion
  public String[] findPromCodeByTxnId(String txnId)
  throws Exception {
try {
  this.fireWorkInProgressEvent(true);
  return (String[])((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).findPromCodeByTxnId(txnId);
} catch (DowntimeException ex) {
  LoggingServices.getCurrent().logMsg(getClass().getName(), "findPromCodeByTxnId"
      , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
      , "See Exception", LoggingServices.MAJOR, ex);
  offLineMode();
  setOffLineMode();
  return (String[])((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
  findPromCodeByTxnId(txnId);
} finally {
  this.fireWorkInProgressEvent(false);
}
}
  
  
//added by Anjana to persist promo code inARM_STG_TXN_dtl table while return happens
  public String findPromCodeByTxnIdandBarcode(String txnId , String barcode)
  throws Exception {
try {
  this.fireWorkInProgressEvent(true);
 return (String)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).findPromCodeByTxnIdandBarcode(txnId,barcode);
} catch (DowntimeException ex) {
  LoggingServices.getCurrent().logMsg(getClass().getName(), "findPromCodeByTxnIdandBarcode"
      , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
      , "See Exception", LoggingServices.MAJOR, ex);
  offLineMode();
  setOffLineMode();
  return (String)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
		  findPromCodeByTxnIdandBarcode(txnId,barcode);
} finally {
  this.fireWorkInProgressEvent(false);
}
}
  
  
  /**
   * put your documentation comment here
   * @param sStoreId
   * @param sRegisterId
   * @return
   * @exception Exception
   */
  public FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(String sStoreId
      , String sRegisterId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (FiscalDocumentNumber)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findFiscalDocNumByStoreAndRegister(sStoreId, sRegisterId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "findFiscalDocNumByStoreAndRegister"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (FiscalDocumentNumber)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findFiscalDocNumByStoreAndRegister(sStoreId, sRegisterId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to persist FiscalDocument
   * @param fiscalDocument FiscalDocument
   * @throws Exception
   * @return boolean
   */
  public boolean submitFiscalDocument(FiscalDocument fiscalDocument)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          submitFiscalDocument(fiscalDocument);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "submitFiscalDocument"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          submitFiscalDocument(fiscalDocument);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open Reservation on the basis of transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenReservationById(String txnId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenReservationById(txnId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenReservationById"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenReservationById(txnId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open Reservation on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByStore(String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByStore(storeId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenReservationByStore"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByStore(storeId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open Reservation on the basis of customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByCustomer(String custId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByCustomer(custId);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenReservationByCustomer"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByCustomer(custId);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to find open Reservation on the basis of store id and
   * for specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByDate(storeId, startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenReservationByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByDate(storeId, startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  public CMSCompositePOSTransaction addShippingRequestToTransaction(CMSCompositePOSTransaction composite)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSCompositePOSTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).addShippingRequestToTransaction(composite);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findOpenReservationByDate"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSCompositePOSTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).addShippingRequestToTransaction(composite);
    } finally {
      this.fireWorkInProgressEvent(false);
    }

  }


  /**
   * This method is used to update the customer of transaction
   * @param txn CMSPosTransaction
   * @return CMSPosTransaction
   * @throws Exception
   */
  public CMSCompositePOSTransaction updateCustomer(CMSCompositePOSTransaction txn)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updateCustomer(txn);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "updateCustomer"
          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updateCustomer(txn);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  public CMSCompositePOSTransaction updateConsultant(
      CMSCompositePOSTransaction txn, POSLineItem[] lineItems)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices
          .getCurrent()).updateConsultant(txn, lineItems);
    } catch (DowntimeException ex) {
      LoggingServices
          .getCurrent()
          .logMsg(
          getClass().getName(),
          "updateCustomer",
          "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line...",
          "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices
          .getCurrent()).updateConsultant(txn, lineItems);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  
  
  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws Exception
	 * @return String
	 */
  public String selectDigitalSignature(String txnId)
	      throws Exception {
	    try {
	      this.fireWorkInProgressEvent(true);
	      return (String)((CMSTransactionPOSServices)CMSTransactionPOSServices.
	          getCurrent()).selectDigitalSignature(txnId);
	    } catch (DowntimeException ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "selectDigitalSignature"
	          , "Primary Implementation for CMSTransactionPOSServices failed, going Off-Line..."
	          , "See Exception", LoggingServices.MAJOR, ex);
	      offLineMode();
	      setOffLineMode();
	      return (String)((CMSTransactionPOSServices)CMSTransactionPOSServices.
	          getCurrent()).selectDigitalSignature(txnId);
	    } finally {
	      this.fireWorkInProgressEvent(false);
	    }

	  }

}

