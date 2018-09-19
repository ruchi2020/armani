/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,Chelsea Market Systems


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;
import java.rmi.*;
import com.chelseasystems.cr.pos.*;
import java.lang.*;
import java.util.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;


/**
 *
 * <p>Title:CMSTransactionPOSRMIClient </p>
 *
 * <p>Description: This class deal with client-side of an RMI connection
 * for fetching/submitting Transaction object</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public class CMSTransactionPOSRMIClient extends CMSTransactionPOSServices implements
    IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSTransactionPOSRMIServer cmstransactionposServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Constructor
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   **/
  public CMSTransactionPOSRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("pos.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * This method is used to get the remote object from the registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSTransactionPOSRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the update.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * This method perform the lookup of remote server.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmstransactionposServer = (ICMSTransactionPOSRMIServer)NamingService.lookup(connect);
  }

  /**
   * This method returns whether remote server is available or not
   * @return  boolean <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmstransactionposServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * This method submit a CompositePOSTransaction to data store
   * @param paymentTransaction PaymentTransaction
   * @return boolean
   * @throws Exception
   */
  public boolean submit(PaymentTransaction paymentTransaction)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.submit((PaymentTransaction)paymentTransaction);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method search a CompositePOSTransaction by its id
   * @param id String
   * @return PaymentTransaction
   * @throws Exception
   */
  public PaymentTransaction findById(String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findById((String)id);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method search a CompositePOSTransactions by customer's id
   * @param customerId String
   * @return PaymentTransaction[]
   * @throws Exception
   */
  public PaymentTransaction[] findByCustomerId(String customerId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByCustomerId((String)customerId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByCustomerIdAndDates(sCutomerId, dtStart, dtEnd);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByCustomerIdAndPaymentType(sCutomerId, sPaymentType);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   *
   * @param sCutomerId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndShippingRequested(String sCutomerId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByCustomerIdAndShippingRequested(sCutomerId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   *
   * @param txnSrchStr TransactionSearchString
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findBySearchCriteria(TransactionSearchString txnSrchStr)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findBySearchCriteria(txnSrchStr);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method search Transaction Headers by customer's id
   * @param customerId String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByCustomerIdHeader(String customerId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByCustomerIdHeader((String)customerId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method search a CompositePOSTransactions by store id and a range of dates
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByDate(String storeId, Date beginDate, Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByDate((String)storeId, (Date)beginDate, (Date)endDate);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * exact amount, store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param amount Currency
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByExactAmount(String storeId, Date beginDate, Date endDate
      , ArmCurrency amount)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByExactAmount((String)storeId, (Date)beginDate
            , (Date)endDate, (ArmCurrency)amount);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * payment type, store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param paymentType String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByPaymentType(String storeId, Date beginDate, Date endDate
      , String paymentType)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByPaymentType((String)storeId, (Date)beginDate
            , (Date)endDate, (String)paymentType);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * credit payment types, store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param aPayments String[]
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByCreditPaymentType(String storeId, Date beginDate, Date endDate
      , String[] aPayments)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByCreditPaymentType((String)storeId, (Date)beginDate
            , (Date)endDate, (String[])aPayments);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * discount type, store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param discountType String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByDiscountType(String storeId, Date beginDate, Date endDate
      , String discountType)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByDiscountType((String)storeId, (Date)beginDate
            , (Date)endDate, (String)discountType);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * transaction type, store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param transactionType String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByTransactionType(String storeId, Date beginDate, Date endDate
      , String transactionType)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByTransactionType((String)storeId, (Date)beginDate
            , (Date)endDate, (String)transactionType);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * consultant id, store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param consultantId String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByConsultantID(String storeId, Date beginDate, Date endDate
      , String consultantId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByConsultantID((String)storeId, (Date)beginDate
            , (Date)endDate, (String)consultantId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * operator id, store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param operatorId String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByOperatorID(String storeId, Date beginDate, Date endDate
      , String operatorId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByOperatorID((String)storeId, (Date)beginDate
            , (Date)endDate, (String)operatorId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find CompositePOSTransactions on the basis of
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByShippingRequested(String storeId, Date beginDate, Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByShippingRequested((String)storeId, (Date)beginDate
            , (Date)endDate);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to display all CompositePOSTransactions Ids for a specific AdHocQueryConstraints
   * @param adHocQueryConstraints AdHocQueryConstraints
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public TransactionHeader[] findByAdHocQueryConstraints(AdHocQueryConstraints
      adHocQueryConstraints)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findByAdHocQueryConstraints((AdHocQueryConstraints)
            adHocQueryConstraints);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null) {
        init();
      }
      try {
        return cmstransactionposServer.getStorePaymentSummary(storeId, date);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null) {
        init();
      }
      try {
        return cmstransactionposServer.getStoreTxnTypeSummary(storeId, date);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null) {
        init();
      }
      try {
        return cmstransactionposServer.getStoreTxnTypeSummary(storeId, date, operatorId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null) {
        init();
      }
      try {
        return cmstransactionposServer.getStoreSalesSummary(storeId, date);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null) {
        init();
      }
      try {
        return cmstransactionposServer.getStoreSalesSummary(storeId, from, to);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find open presale on the basis of transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws RemoteException
   */
  public CMSTransactionHeader findOpenPresaleById(String txnId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenPresaleById(txnId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find open presale on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByStore(String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenPresaleByStore(storeId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find open presale on the basis of customer
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByCustomer(String custId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenPresaleByCustomer(custId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenPresaleByDate(storeId, startDate, endDate);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /** This method is used to find open consignment on the basis of transaction id
   * This method is used to
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws RemoteException
   */
  public CMSTransactionHeader findOpenConsignmentById(String txnId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenConsignmentById(txnId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find open consignment on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenConsignmentByStore(String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenConsignmentByStore(storeId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find open consignment on the basis of customer
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenConsignmentByCustomer(String custId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenConsignmentByCustomer(custId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenConsignmentByDate(storeId, startDate, endDate);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method facilitate the update of the Expiration Date for consignment
   * Transaction
   * @param txn ConsignmentTransaction
   * @return ConsignmentTransaction
   * @throws RemoteException
   */
  public ConsignmentTransaction updateConsignmentExpirationDate(ConsignmentTransaction txn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.updateConsignmentExpirationDate(txn);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method facilitate the update of the Expiration Date for Presale Transaction
   * @param txn PresaleTransaction
   * @return PresaleTransaction
   * @throws RemoteException
   */
  public PresaleTransaction updatePresaleExpirationDate(PresaleTransaction txn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.updatePresaleExpirationDate(txn);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method facilitate the update of the Expiration Date for Reservation Transaction
   * @param txn ReservationTransaction
   * @return ReservationTransaction
   * @throws RemoteException
   */
  public ReservationTransaction updateReservationExpirationDate(ReservationTransaction txn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.updateReservationExpirationDate(txn);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
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
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findTxnIdsByStoreIdAndRegisterId(storeId, registerId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method search a FiscalDocumentNumber by StoreId and RegisterId
   * @param sStoreId String
   * @param sRegisterId String
   * @throws Exception
   * @return FiscalDocumentNumber
   */
  public FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(String sStoreId
      , String sRegisterId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findFiscalDocNumByStoreAndRegister(sStoreId, sRegisterId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to persist FiscalDocument
   * @param fiscalDocument FiscalDocument
   * @throws Exception
   * @return boolean
   */
  public boolean submitFiscalDocument(FiscalDocument fiscalDocument)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.submitFiscalDocument(fiscalDocument);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * Lookup open reservation by id
   * @param txnId String
   * @throws Exception
   * @return CMSTransactionHeader
   */
  public CMSTransactionHeader findOpenReservationById(String txnId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenReservationById(txnId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find open Reservation on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenReservationByStore(String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenReservationByStore(storeId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find open Reservation on the basis of customer
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenReservationByCustomer(String custId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenReservationByCustomer(custId);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method is used to find the open Reservation on the basis of date
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenReservationByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.findOpenReservationByDate(storeId, startDate, endDate);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   *
   * @param composite CMSCompositePOSTransaction
   * @throws Exception
   * @return boolean
   */
  public CMSCompositePOSTransaction addShippingRequestToTransaction(CMSCompositePOSTransaction composite)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.addShippingRequestToTransaction(composite);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }

  /**
   * This method facilitate the update of the Customer for the transaction
   * @param txn CMSCompositePOSTransaction
   * @param oldCustomer Customer
   * @return CMSCompositePOSTransaction
   * @throws RemoteException
   */
  public CMSCompositePOSTransaction updateCustomer(CMSCompositePOSTransaction txn)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.updateCustomer(txn);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
}

  /**
   * put your documentation comment here
   * @param txn
   * @param lineItems
   * @return
   * @exception Exception
   */
  public CMSCompositePOSTransaction updateConsultant(CMSCompositePOSTransaction txn, POSLineItem[] lineItems)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmstransactionposServer == null)
        init();
      try {
        return cmstransactionposServer.updateConsultant(txn, lineItems);
      } catch (ConnectException ce) {
        cmstransactionposServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
  }
  
  
  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws Exception
	 * @return String
	 */
  public String selectDigitalSignature(String txnId)
	      throws Exception {
	    for (int x = 0; x < maxTries; x++) {
	      if (cmstransactionposServer == null)
	        init();
	      try {
	    	return cmstransactionposServer.selectDigitalSignature(txnId);
	      } catch (ConnectException ce) {
	        cmstransactionposServer = null;
	      } catch (Exception ex) {
	        throw new DowntimeException(ex.getMessage());
	      }
	    }
	    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
	  }
//Added by shushma for promotion
@Override
public String[] findPromCodeByTxnId(String txnId) throws Exception {
	// TODO Auto-generated method stub
	 		    for (int x = 0; x < maxTries; x++) {
		      if (cmstransactionposServer == null)
		        init();
		      try {
		        return cmstransactionposServer.findPromCodeByTxnId(txnId);
		      } catch (ConnectException ce) {
		        cmstransactionposServer = null;
		      } catch (Exception ex) {
		        throw new DowntimeException(ex.getMessage());
		      }
		    }
		    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
}


//Added by Anjana to persist promocode in DB
@Override
public String findPromCodeByTxnIdandBarcode(String txnId, String barcode) throws Exception {
	// TODO Auto-generated method stub
	 		    for (int x = 0; x < maxTries; x++) {
		      if (cmstransactionposServer == null)
		        init();
		      try {
		        return cmstransactionposServer.findPromCodeByTxnIdandBarcode(txnId,barcode);
		      } catch (ConnectException ce) {
		        cmstransactionposServer = null;
		      } catch (Exception ex) {
		        throw new DowntimeException(ex.getMessage());
		      }
		    }
		    throw new DowntimeException("Unable to establish connection to CMSTransactionPOSServices");
}

  
}






