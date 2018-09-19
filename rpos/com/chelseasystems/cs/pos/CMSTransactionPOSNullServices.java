/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.layaway.*;
import com.chelseasystems.cr.layaway.*;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.collection.*;
import com.chelseasystems.cr.util.DateUtil;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cr.txnposter.*;
import java.util.Date;
import java.util.*;
import java.sql.*;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import java.rmi.RemoteException;


/**
 *
 * <p>Title: CMSTransactionPOSNullServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTransactionPOSNullServices extends CMSTransactionPOSServices {

  /**
   * Default Constructor
   */
  public CMSTransactionPOSNullServices() {
  }

  /**
   * This method is used to find payment transaction on the basis of transaction id
   * @param id String
   * @return PaymentTransaction
   * @throws Exception
   */
  public PaymentTransaction findById(String id)
      throws java.lang.Exception {
    return null;
  }

  /**
   * This method is used to find payment transaction on the basis of customer id
   *
   * @param customerId String
   * @return PaymentTransaction[]
   * @throws Exception
   */
  public PaymentTransaction[] findByCustomerId(String customerId)
      throws java.lang.Exception {
    return new PaymentTransaction[0];
  }

  /**
   *
   * @param txnSrchStr TransactionSearchString
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findBySearchCriteria(TransactionSearchString txnSrchStr)
      throws Exception {
    return new CMSTransactionHeader[0];
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
    return new CMSTransactionHeader[0];
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
    return new CMSTransactionHeader[0];
  }

  /**
   *
   * @param sCutomerId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndShippingRequested(String sCutomerId)
      throws Exception {
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id, consultant
   * id and for a specified date range.
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
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id, discount
   * type and for a specified date range.
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
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id, exact
   * amount and for a specified date range.
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
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id, operator
   * id and for a specified date range.
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
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id,
   * payment type and for a specified date range.
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
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id, credit
   * payment type and for a specified date range.
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param creditPaymentTypes String[]
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByCreditPaymentType(String storeId, Date beginDate, Date endDate
      , String[] creditPaymentTypes)
      throws Exception {
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id, transaction type
   * and for a specified date range.
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
    return new CMSTransactionHeader[0];
  }

  /**
   *
   * @param customerId String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByCustomerIdHeader(String customerId)
      throws java.lang.Exception {
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id and for
   * a specified date range.
   * for on specified date.
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByDate(String storeId, Date beginDate, Date endDate)
      throws java.lang.Exception {
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to find transaction on the basis of store id and for
   * for a specified date range.
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByShippingRequested(String aStoreId, Date aBeginDate
      , Date anEndDate)
      throws Exception {
    return null;
  }

  /**
   *
   * @param adHocQueryConstraints AdHocQueryConstraints
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByAdHocQueryConstraints(AdHocQueryConstraints
      adHocQueryConstraints)
      throws Exception {
    return new CMSTransactionHeader[0];
  }

  /**
   * This method is used to return the payment summaries for the specified
   * store for on specified date.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the payment summaries.
   */
  public PaymentSummary[] getStorePaymentSummary(String storeId, Date date)
      throws Exception {
    return new PaymentSummary[0];
  }

  /**
   * This method is used to return the transaction type summaries for the
   * specified store on the specified date.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the transaction type summaries.
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date)
      throws Exception {
    return new TxnTypeSummary[0];
  }

  /**
   * This method is used to return the transaction type summaries for the
   * specified store on the specified date by the specified operator.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the transaction type summaries.
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date, String operatorId)
      throws Exception {
    return new TxnTypeSummary[0];
  }

  /**
   * This method is used to return the sales summaries for the specified store
   * for on specified date.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the sales summaries.
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date date)
      throws Exception {
    return new SalesSummary[0];
  }

  /**
   *
   * This method is used to return the sales summaries for the specified
   * store for between the specified dates (inclusive).
   * @param storeId The store to search for.
   * @param from The date from which to search.
   * @param to The date up to which to search.
   * @return An array of the sales summaries.
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date from, Date to)
      throws Exception {
    return new SalesSummary[0];
  }

  /**
   * This method is used to submit payment transaction
   * @param paymentTransaction PaymentTransaction
   * @return boolean
   * @throws Exception
   */
  public boolean submit(PaymentTransaction paymentTransaction)
      throws java.lang.Exception {
    return false;
  }

  /**
   * This method is used to find open pre sale transaction on the basis of
   * transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenPresaleById(String txnId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open pre sale transaction on the basis of
   * store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByStore(String storeId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open pre sale transaction on the basis of
   * customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByCustomer(String custId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open pre sale transaction on the basis of
   * store id and for a specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByDate(String storeId, Date startDate, Date endDate)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * transaction id.
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenConsignmentById(String txnId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByStore(String storeId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByCustomer(String custId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * store id and for a specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    return null;
  }

  /**
   * This method is used to update expiration date of a consignment transaction
   * @param txn ConsignmentTransaction
   * @return ConsignmentTransaction
   * @throws Exception
   */
  public ConsignmentTransaction updateConsignmentExpirationDate(ConsignmentTransaction txn)
      throws Exception {
    return txn;
  }

  /**
   * This method is used to update expiration date of a pre sale transaction
   * @param txn PresaleTransaction
   * @return PresaleTransaction
   * @throws Exception
   */
  public PresaleTransaction updatePresaleExpirationDate(PresaleTransaction txn)
      throws Exception {
    return txn;
  }

  /**
   * This method is used to update expiration date of a Reservation transaction
   * @param txn ReservationTransaction
   * @return ReservationTransaction
   * @throws Exception
   */
  public ReservationTransaction updateReservationExpirationDate(ReservationTransaction txn)
      throws Exception {
    return txn;
  }

  /**
   * This method is used to find transaction on the basis of store id and register id
   * @param storeId String
   * @param registerId String
   * @return String[]
   * @throws Exception
   */
  public String[] findTxnIdsByStoreIdAndRegisterId(String storeId, String registerId)
      throws Exception {
    return null;
  }

  /**
   *
   * @param storeId String
   * @param registerId String
   * @return FiscalDocumentNumber
   * @throws Exception
   */
  public FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(String storeId, String registerId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to persist FiscalDocument
   * @param fiscalDocument FiscalDocument
   * @throws Exception
   * @return boolean
   */
  public boolean submitFiscalDocument(FiscalDocument fiscalDocument)
      throws Exception {
    return false;
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * transaction id.
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenReservationById(String txnId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByStore(String storeId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByCustomer(String custId)
      throws Exception {
    return null;
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * store id and for a specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    return null;
  }

  /**
   * put your documentation comment here
   * @param composite
   * @return
   * @exception Exception
   */
  public CMSCompositePOSTransaction addShippingRequestToTransaction (CMSCompositePOSTransaction composite) throws Exception {
    return  null;
  }

  /**
   * put your documentation comment here
   * @param txn
   * @return
   * @exception Exception
   */
  public CMSCompositePOSTransaction updateCustomer (CMSCompositePOSTransaction txn) throws Exception {
    return  null;
  }

  /**
   * put your documentation comment here
   * @param txn
   * @param lineItems
   * @return
   * @exception Exception
   */
  public CMSCompositePOSTransaction updateConsultant (CMSCompositePOSTransaction txn, POSLineItem[] lineItems) throws Exception {
    return  null;
  }
  
  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws Exception
	 * @return String
	 */
  public String selectDigitalSignature (String txnId) throws Exception {
    return  null;
  }
//added by shushma for promotion
@Override
public String[] findPromCodeByTxnId(String txnId) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

//added by Anjana to persist promotion in DB while returning
@Override
public String findPromCodeByTxnIdandBarcode(String txnId, String barcode)
		throws Exception {
	// TODO Auto-generated method stub
	return null;
}
}



