/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,Chelsea Market Systems


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.igray.naming.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import com.chelseasystems.cr.pos.*;
import java.lang.*;
import java.util.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;


/**
 *
 * <p>Title: CMSTransactionPOSRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * fetching/submitting information.  This class delgates all method calls to
 * the object referenced by the return value from
 * CMSTransactionPOSServices.getCurrent(). </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTransactionPOSRMIServerImpl extends CMSComponent implements
    ICMSTransactionPOSRMIServer {

  /**
   * Constructor
   * @param props Properties
   * @throws RemoteException
   */
  public CMSTransactionPOSRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * This method sets the current implementation
   **/
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure pos.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSTransactionPOSServices.setCurrent((CMSTransactionPOSServices)obj);
  }

  /**
   * This method is used to bind remote object
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure pos.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * This method is used to receives callback when the config file changes
   * @param aKey String[]
   */
  protected void configEvent(String[] aKey) {}

  /**
   * This method is used by DowntimeManager to determine when this object is
   * available. Just because this process is up doesn't mean that the clients
   * can come up. Make sure that the database is available.
   * @return boolean
   * @throws RemoteException
   */
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * This method is used to submit a CompositePOSTransaction to data store
   * @param paymentTransaction PaymentTransaction
   * @return boolean
   * @throws RemoteException
   */
  public boolean submit(PaymentTransaction paymentTransaction)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (boolean)CMSTransactionPOSServices.getCurrent().submit(paymentTransaction);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("submit", start);
      decConnection();
    }
  }

  /**
   * This method is used to search a CompositePOSTransaction by its id
   * @param id String
   * @return PaymentTransaction
   * @throws RemoteException
   */
  public PaymentTransaction findById(String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (PaymentTransaction)CMSTransactionPOSServices.getCurrent().findById(id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }

  /**
   * This method is used to search a CompositePOSTransactions by customer's id
   * @param customerId String
   * @return PaymentTransaction[]
   * @throws RemoteException
   */
  public PaymentTransaction[] findByCustomerId(String customerId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (PaymentTransaction[])CMSTransactionPOSServices.getCurrent().findByCustomerId(
          customerId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCustomerId", start);
      decConnection();
    }
  }

  /**
   *
   * @param sCutomerId String
   * @param dtStart Date
   * @param dtEnd Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCustomerIdAndDates(String sCutomerId, Date dtStart
      , Date dtEnd)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndDates(sCutomerId, dtStart, dtEnd);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCustomerIdAndDate", start);
      decConnection();
    }
  }

  /**
   *
   * @param sCutomerId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCustomerIdAndShippingRequested(String sCutomerId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndShippingRequested(sCutomerId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findBySearchCriteria", start);
      decConnection();
    }
  }

  /**
   *
   * @param sCutomerId String
   * @param sPaymentType String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCustomerIdAndPaymentType(String sCutomerId
      , String sPaymentType)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findByCustomerIdAndPaymentType(sCutomerId, sPaymentType);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findBySearchCriteria", start);
      decConnection();
    }
  }

  /**
   *
   * @param txnSrchStr TransactionSearchString
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findBySearchCriteria(TransactionSearchString txnSrchStr)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findBySearchCriteria(txnSrchStr);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findBySearchCriteria", start);
      decConnection();
    }
  }

  /**
   * This method is used to search Transaction Headers by customer's id
   * @param customerId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCustomerIdHeader(String customerId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByCustomerIdHeader(
          customerId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCustomerIdHeader", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of store id and a
   * specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByDate(String storeId, Date beginDate, Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByDate(storeId
          , beginDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByDate", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of exact amount,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param amount Currency
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByExactAmount(String storeId, Date beginDate, Date endDate
      , ArmCurrency amount)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByExactAmount(
          storeId, beginDate, endDate, amount);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByExactAmount", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of payment type,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param paymentType String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByPaymentType(String storeId, Date beginDate, Date endDate
      , String paymentType)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByPaymentType(
          storeId, beginDate, endDate, paymentType);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByPaymentType", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of payment types,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param aPayments String[]
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCreditPaymentType(String storeId, Date beginDate
      , Date endDate, String[] aPayments)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByCreditPaymentType(
          storeId, beginDate, endDate, aPayments);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByCreditPaymentType", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of discount type,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param discountType String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByDiscountType(String storeId, Date beginDate, Date endDate
      , String discountType)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByDiscountType(
          storeId, beginDate, endDate, discountType);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByDiscountType", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of transaction type,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param transactionType String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByTransactionType(String storeId, Date beginDate, Date endDate
      , String transactionType)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByTransactionType(
          storeId, beginDate, endDate, transactionType);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByTransactionType", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of consultant id,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param consultantId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByConsultantID(String storeId, Date beginDate, Date endDate
      , String consultantId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByConsultantID(
          storeId, beginDate, endDate, consultantId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByConsultantID", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of operator id,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param operatorId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByOperatorID(String storeId, Date beginDate, Date endDate
      , String operatorId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByOperatorID(
          storeId, beginDate, endDate, operatorId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByOperatorID", start);
      decConnection();
    }
  }

  /**
   * This method is used to find transaction on the basis of store id and
   * a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByShippingRequested(String storeId, Date beginDate
      , Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().findByShippingRequested(
          storeId, beginDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByShippingRequested", start);
      decConnection();
    }
  }

  /**
   * This method is used to display all Transaction Ids for a specific AdHocQueryConstraints
   * @param adHocQueryConstraints AdHocQueryConstraints
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByAdHocQueryConstraints(AdHocQueryConstraints
      adHocQueryConstraints)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])CMSTransactionPOSServices.getCurrent().
          findByAdHocQueryConstraints(adHocQueryConstraints);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findByAdHocQueryConstraints", start);
      decConnection();
    }
  }

  /**
   * This method is used to get the payment summaries for the specified store
   * for on specified date.
   * @param storeId String
   * @param date Date
   * @return PaymentSummary[]
   * @throws RemoteException
   */
  public PaymentSummary[] getStorePaymentSummary(String storeId, Date date)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (PaymentSummary[])CMSTransactionPOSServices.getCurrent().getStorePaymentSummary(
          storeId, date);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getStorePaymentSummary", start);
      decConnection();
    }
  }

  /**
   * This method is used to get the transaction type summaries for the specified
   * store on the specified date.
   * @param storeId String
   * @param date Date
   * @return TxnTypeSummary[]
   * @throws RemoteException
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (TxnTypeSummary[])CMSTransactionPOSServices.getCurrent().getStoreTxnTypeSummary(
          storeId, date);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getStoreTxnTypeSummary[1]", start);
      decConnection();
    }
  }

  /**
   * This method is used to get the transaction type summaries for the specified
   * store on the specified date by the specified operator.
   * @param storeId String
   * @param date Date
   * @param operatorId String
   * @return TxnTypeSummary[]
   * @throws RemoteException
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date, String operatorId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (TxnTypeSummary[])CMSTransactionPOSServices.getCurrent().getStoreTxnTypeSummary(
          storeId, date, operatorId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getStoreTxnTypeSummary[2]", start);
      decConnection();
    }
  }

  /**
   * This method is used to get the sales summaries for the specified store
   * for on specified date.
   * @param storeId String
   * @param date Date
   * @return SalesSummary[]
   * @throws RemoteException
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date date)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (SalesSummary[])CMSTransactionPOSServices.getCurrent().getStoreSalesSummary(storeId
          , date);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getStoreSalesSummary[1]", start);
      decConnection();
    }
  }

  /**
   * This method is used to get the sales summaries for the specified
   * store for between the specified dates (inclusive).
   * @param storeId String
   * @param from Date
   * @param to Date
   * @return SalesSummary[]
   * @throws RemoteException
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date from, Date to)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (SalesSummary[])CMSTransactionPOSServices.getCurrent().getStoreSalesSummary(storeId
          , from, to);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getStoreSalesSummary[2]", start);
      decConnection();
    }
  }

  /**
   * This method is used to find open presale on the basis of transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws RemoteException
   */
  public CMSTransactionHeader findOpenPresaleById(String txnId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenPresaleById(txnId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenPresaleById", start);
      decConnection();
    }
  }

  /**
   * This method is used to find open presale on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByStore(String storeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByStore(storeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenPresaleByStore", start);
      decConnection();
    }
  }

  /**
   * This method is used to find open presale on the basis of customer
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByCustomer(String custId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByCustomer(custId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenPresaleByCustomer", start);
      decConnection();
    }
  }

  /**
   * This method is used to find open presale on the basis of date
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByDate(String storeId, Date startDate, Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenPresaleByDate(storeId, startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenPresaleByDate", start);
      decConnection();
    }
  }

  /** This method is used to find open consignment on the basis of transaction id
   * This method is used to
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws RemoteException
   */
  public CMSTransactionHeader findOpenConsignmentById(String txnId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenConsignmentById(txnId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenConsignmentById", start);
      decConnection();
    }
  }

  /**
   * This method is used to find open consignment on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenConsignmentByStore(String storeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByStore(storeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenConsignmentByStore", start);
      decConnection();
    }
  }

  /**
   * This method is used to find open consignment on the basis of customer
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenConsignmentByCustomer(String custId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByCustomer(custId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenConsignmentByCustomer", start);
      decConnection();
    }
  }

  /**
   * This method is used to find the open consignments on the basis of date
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenConsignmentByDate(String storeId, Date startDate
      , Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenConsignmentByDate(storeId, startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenConsignmentByDate", start);
      decConnection();
    }
  }

  /**
   * This method facilitate the update of the Expiration Date for consignment
   * Transaction
   * @param txn ConsignmentTransaction
   * @return ConsignmentTransaction
   * @throws RemoteException
   */
  public ConsignmentTransaction updateConsignmentExpirationDate(ConsignmentTransaction txn)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (ConsignmentTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).updateConsignmentExpirationDate(txn);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateConsignmentExpirationDate", start);
      decConnection();
    }
  }

  /**
   * This method facilitate the update of the Expiration Date for Presale Transaction
   * @param txn PresaleTransaction
   * @return PresaleTransaction
   * @throws RemoteException
   */
  public PresaleTransaction updatePresaleExpirationDate(PresaleTransaction txn)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (PresaleTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updatePresaleExpirationDate(txn);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updatePresaleExpirationDate", start);
      decConnection();
    }
  }

  /**
   * This method facilitate the update of the Expiration Date for Reservation Transaction
   * @param txn ReservationTransaction
   * @return ReservationTransaction
   * @throws RemoteException
   */
  public ReservationTransaction updateReservationExpirationDate(ReservationTransaction txn)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (ReservationTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          updateReservationExpirationDate(txn);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateReservationExpirationDate", start);
      decConnection();
    }
  }

  /**
   * New method added to find the transaction id on the basis of store id
   * and register id
   * @param storeId String
   * @param registerId String
   * @return String[]
   * @throws RemoteException
   */
  public String[] findTxnIdsByStoreIdAndRegisterId(String storeId, String registerId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String[])((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findTxnIdsByStoreIdAndRegisterId(storeId, registerId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findTxnIdsByStoreIdAndRegisterId", start);
      decConnection();
    }
  }

  //added by shushma for promotion
  public String[] findPromCodeByTxnId (String txnId)throws RemoteException {
	  long start = getStartTime();
	    try {
	    if (!isAvailable())
	      throw new ConnectException("Service is not available");
	      incConnection();
	      return (String[])((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).findPromCodeByTxnId(txnId);
	    } catch (Exception e) {
	        throw new RemoteException(e.getMessage(), e);
	      } finally {
	        addPerformance("fintPromCodeByTxnId", start);
	        decConnection();
	      }
  }
  /**
   * put your documentation comment here
   * @param sStoreId
   * @param sRegisterId
   * @return
   * @exception RemoteException
   */
  public FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(String sStoreId
      , String sRegisterId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (FiscalDocumentNumber)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findFiscalDocNumByStoreAndRegister(sStoreId, sRegisterId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findFiscalDocNumByStoreAndRegister", start);
      decConnection();
    }
  }

  /**
   * This method is used to persist FiscalDocument
   * @param fiscalDocument FiscalDocument
   * @throws Exception
   * @return boolean
   */
  public boolean submitFiscalDocument(FiscalDocument fiscalDocument)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          submitFiscalDocument(fiscalDocument);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("submitFiscalDocument", start);
      decConnection();
    }
  }

  /**
   * Lookup OpenReservation transaction by id
   * @param txnId String
   * @throws RemoteException
   * @return CMSTransactionHeader
   */
  public CMSTransactionHeader findOpenReservationById(String txnId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
          findOpenReservationById(txnId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenReservationById", start);
      decConnection();
    }
  }

  /**
   * Lookup OpenReservation txn by Store
   * @param storeId String
   * @throws RemoteException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] findOpenReservationByStore(String storeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByStore(storeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenReservationByStore", start);
      decConnection();
    }
  }

  /**
   * Lookup OpenReservation Txn by Customer
   * @param custId String
   * @throws RemoteException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] findOpenReservationByCustomer(String custId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByCustomer(custId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenReservationByCustomer", start);
      decConnection();
    }
  }

  /**
   * Lookup OpenReservation txn by Date
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @throws RemoteException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] findOpenReservationByDate(String storeId, Date startDate
      , Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSTransactionHeader[])((CMSTransactionPOSServices)CMSTransactionPOSServices.
          getCurrent()).findOpenReservationByDate(storeId, startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenReservationByDate", start);
      decConnection();
    }
  }

  /**
   * put your documentation comment here
   * @param composite
   * @return
   * @exception RemoteException
   */
  public CMSCompositePOSTransaction addShippingRequestToTransaction(CMSCompositePOSTransaction composite)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSCompositePOSTransaction)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).addShippingRequestToTransaction(
          composite);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findOpenReservationByDate", start);
      decConnection();
    }
}

  /**
   * This method facilitate the update of the Customer for Transaction
   * @param txn CMSCompositePOSTransaction
   * @param oldCustomer Customer
   * @return CMSCompositePOSTransaction
   * @throws RemoteException
   */
  public CMSCompositePOSTransaction updateCustomer(CMSCompositePOSTransaction txn)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).updateCustomer(txn);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateCustomer", start);
      decConnection();
    }
  }

  /**
   *
   * @param txn CMSCompositePOSTransaction
   * @param lineItems POSLineItem[]
   * @throws RemoteException
   * @return CMSCompositePOSTransaction
   */
  public CMSCompositePOSTransaction updateConsultant(CMSCompositePOSTransaction txn, POSLineItem[] lineItems)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return ((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).updateConsultant(txn, lineItems);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateConsultant", start);
      decConnection();
    }
  }


  
  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws RemoteException
	 * @return String
	 */
  public String selectDigitalSignature(String txnId) throws RemoteException {
	
		    long start = getStartTime();
		    try {
		      if (!isAvailable())
		        throw new ConnectException("Service is not available");
		      incConnection();
		      return (String)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
		          selectDigitalSignature(txnId);
		    } catch (Exception e) {
		      throw new RemoteException(e.getMessage(), e);
		    } finally {
		      addPerformance("findTxnIdsByStoreIdAndRegisterId", start);
		      decConnection();
		    }
		  }

//Added by Anjana to save promo code in DB
public String findPromCodeByTxnIdandBarcode(String txnId, String barcode)
		throws RemoteException {
	  long start = getStartTime();
	    try {
	      if (!isAvailable())
	        throw new ConnectException("Service is not available");
	      incConnection();
	      return (String)((CMSTransactionPOSServices)CMSTransactionPOSServices.getCurrent()).
	    		  findPromCodeByTxnIdandBarcode(txnId,barcode);
	    } catch (Exception e) {
	      throw new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("findPromCodeByTxnIdandBarcode", start);
	      decConnection();
	    }
}

}

