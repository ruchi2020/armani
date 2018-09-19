/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,Chelsea Market Systems


package com.chelseasystems.cs.pos;

import java.rmi.*;
import com.igray.naming.*;
import com.chelseasystems.cr.pos.*;
import java.lang.*;
import java.util.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;


/**
 *
 * <p>Title: ICMSTransactionPOSRMIServer</p>
 *
 * <p>Description: Defines the customer services that are available remotely via RMI.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public interface ICMSTransactionPOSRMIServer extends Remote, IPing {

  /**
   * This method is used to submit a CompositePOSTransaction to data store
   * @param paymentTransaction PaymentTransaction
   * @return boolean
   * @throws RemoteException
   */
  public boolean submit(PaymentTransaction paymentTransaction)
      throws RemoteException;


  /**
   * This method is used to search a CompositePOSTransaction by its id
   * @param id String
   * @return PaymentTransaction
   * @throws RemoteException
   */
  public PaymentTransaction findById(String id)
      throws RemoteException;


  /**
   * This method is used to search a CompositePOSTransactions by customer's id
   * @param customerId String
   * @return PaymentTransaction[]
   * @throws RemoteException
   */
  public PaymentTransaction[] findByCustomerId(String customerId)
      throws RemoteException;


  /**
   * This method is used to search Transaction Headers by customer's id
   * @param customerId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCustomerIdHeader(String customerId)
      throws RemoteException;


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
      throws RemoteException;


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
      throws RemoteException;


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
      throws RemoteException;


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
      throws RemoteException;


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
      throws RemoteException;


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
      throws RemoteException;


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
      throws RemoteException;


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
      throws RemoteException;


  /**
   * This method is used to find transaction on the basis of shipping request,
   * store id and a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByShippingRequested(String aStoreId, Date aBeginDate
      , Date anEndDate)
      throws RemoteException;


  /**
   * This method is used to display all Transaction Ids for a specific AdHocQueryConstraints
   * @param adHocQueryConstraints AdHocQueryConstraints
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByAdHocQueryConstraints(AdHocQueryConstraints
      adHocQueryConstraints)
      throws RemoteException;


  /**
   * This method is used to get the payment summaries for the specified store
   * for on specified date.
   * @param storeId String
   * @param date Date
   * @return PaymentSummary[]
   * @throws RemoteException
   */
  public PaymentSummary[] getStorePaymentSummary(String storeId, Date date)
      throws RemoteException;


  /**
   * This method is used to get the transaction type summaries for the specified
   * store on the specified date.
   * @param storeId String
   * @param date Date
   * @return TxnTypeSummary[]
   * @throws RemoteException
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date)
      throws RemoteException;


  /**
   * This method is used to get the transaction type summaries for the specified
   * store on the specified date.
   * @param storeId String
   * @param date Date
   * @return TxnTypeSummary[]
   * @throws RemoteException
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date, String operatorId)
      throws RemoteException;


  /**
   * This method is used to get the sales summaries for the specified store
   * for on specified date.
   * @param storeId String
   * @param date Date
   * @return SalesSummary[]
   * @throws RemoteException
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date date)
      throws RemoteException;


  /**
   * This method is used to get the sales summaries for the specified store
   * for on specified date.
   * @param storeId String
   * @param date Date
   * @return SalesSummary[]
   * @throws RemoteException
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date from, Date to)
      throws RemoteException;


  /**
   * This method is used to find open presale on the basis of transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws RemoteException
   */
  public CMSTransactionHeader findOpenPresaleById(String txnId)
      throws RemoteException;


  /**
   *
   * @param txnSrchStr TransactionSearchString
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findBySearchCriteria(TransactionSearchString txnSrchStr)
      throws RemoteException;


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
      throws RemoteException;


  /**
   *
   * @param sCutomerId String
   * @param sPaymentType String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCustomerIdAndPaymentType(String sCutomerId
      , String sPaymentType)
      throws RemoteException;


  /**
   *
   * @param sCutomerId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findByCustomerIdAndShippingRequested(String sCutomerId)
      throws RemoteException;


  /**
   * This method is used to find open presale on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByStore(String storeId)
      throws RemoteException;


  /**
   * This method is used to find open presale on the basis of customer
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByCustomer(String custId)
      throws RemoteException;


  /**
   * This method is used to find open presale on the basis of date
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenPresaleByDate(String storeId, Date startDate, Date endDate)
      throws RemoteException;


  /** This method is used to find open consignment on the basis of transaction id
   * This method is used to
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws RemoteException
   */
  public CMSTransactionHeader findOpenConsignmentById(String txnId)
      throws RemoteException;


  /**
   * This method is used to find open consignment on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenConsignmentByStore(String storeId)
      throws RemoteException;


  /**
   * This method is used to find open consignment on the basis of customer
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws RemoteException
   */
  public CMSTransactionHeader[] findOpenConsignmentByCustomer(String custId)
      throws RemoteException;


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
      throws RemoteException;


  /**
   * This method facilitate the update of the Expiration Date for consignment
   * Transaction
   * @param txn ConsignmentTransaction
   * @return ConsignmentTransaction
   * @throws RemoteException
   */
  public ConsignmentTransaction updateConsignmentExpirationDate(ConsignmentTransaction txn)
      throws RemoteException;


  /**
   * This method facilitate the update of the Expiration Date for Presale Transaction
   * @param txn PresaleTransaction
   * @return PresaleTransaction
   * @throws RemoteException
   */
  public PresaleTransaction updatePresaleExpirationDate(PresaleTransaction txn)
      throws RemoteException;

  /**
   * This method facilitate the update of the Expiration Date for Reservation Transaction
   * @param txn ReservationTransaction
   * @return ReservationTransaction
   * @throws RemoteException
   */
  public ReservationTransaction updateReservationExpirationDate(ReservationTransaction txn)
      throws RemoteException;


  /**
   * New method added to find the transaction id on the basis of store id
   * and register id
   * @param storeId String
   * @param registerId String
   * @return String[]
   * @throws RemoteException
   */
  public String[] findTxnIdsByStoreIdAndRegisterId(String storeId, String registerId)
      throws RemoteException;


  /**
   *
   * @param sStoreId String
   * @param sRegisterId String
   * @return FiscalDocumentNumber
   * @throws Exception
   */
  public FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(String sStoreId
      , String sRegisterId)
      throws RemoteException;


  /**
   * This method is used to persist FiscalDocument
   * @param fiscalDocument FiscalDocument
   * @throws Exception
   * @return boolean
   */
  public boolean submitFiscalDocument(FiscalDocument fiscalDocument)
      throws RemoteException;


  /**
   * Lookup open reservation by id
   * @param txnId String
   * @throws RemoteException
   * @return CMSTransactionHeader
   */
  public CMSTransactionHeader findOpenReservationById(String txnId)
      throws RemoteException;


  /**
   * Lookup open reservation store
   * @param storeId String
   * @throws RemoteException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] findOpenReservationByStore(String storeId)
      throws RemoteException;


  /**
   * Lookup open reservation by customer
   * @param custId String
   * @throws RemoteException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] findOpenReservationByCustomer(String custId)
      throws RemoteException;


  /**
   * Lookup open reservation by Date
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @throws RemoteException
   * @return CMSTransactionHeader[]
   */
  public CMSTransactionHeader[] findOpenReservationByDate(String storeId, Date startDate
      , Date endDate)
      throws RemoteException;


  /**
   *
   * @param composite CMSCompositePOSTransaction
   * @throws Exception
   * @return boolean
   */
  public CMSCompositePOSTransaction addShippingRequestToTransaction(CMSCompositePOSTransaction composite)
      throws Exception;


  public CMSCompositePOSTransaction updateCustomer(CMSCompositePOSTransaction txn)
      throws RemoteException;

  public CMSCompositePOSTransaction updateConsultant(CMSCompositePOSTransaction txn, POSLineItem[] lineItems)
      throws RemoteException;


  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws RemoteException
	 * @return String
	 */
  public String selectDigitalSignature(String txnId)
	  throws RemoteException;


public String[] findPromCodeByTxnId(String txnId)throws RemoteException;

public String findPromCodeByTxnIdandBarcode(String txnId , String barcode)throws RemoteException;


}

