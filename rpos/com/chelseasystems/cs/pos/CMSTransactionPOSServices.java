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
 | 4    | 05-10-2005 | Manpreet  | N/A       | New method for find by search criteria       |
 --------------------------------------------------------------------------------------------
 | 3    | 04-29-2005 | Pankaja   | N/A       | New method for updation of the expiration dt |
 --------------------------------------------------------------------------------------------
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Presale impl                           |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import java.util.Date;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.eod.CheckRecon;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;


/**
 *
 * <p>Title: CMSTransactionPOSServices</p>
 *
 * <p>Description: This is abstract class having all the abstract methods
 * dealing with pre sale and consignment </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh
 * @version 1.0
 */
public abstract class CMSTransactionPOSServices extends TransactionPOSServices {

  /**
   * put your documentation comment here
   * @param txnId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader findOpenPresaleById(String txnId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param txnSrchStr
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findBySearchCriteria(TransactionSearchString txnSrchStr)
      throws Exception;

  /**
   * put your documentation comment here
   * @param sCutomerId
   * @param sPaymentType
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findByCustomerIdAndPaymentType(String sCutomerId
      , String sPaymentType)
      throws Exception;

  /**
   * put your documentation comment here
   * @param sCutomerId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findByCustomerIdAndShippingRequested(String sCutomerId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param sCutomerId
   * @param dtStart
   * @param dtEnd
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findByCustomerIdAndDates(String sCutomerId, Date dtStart
      , Date dtEnd)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenPresaleByStore(String storeId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param custId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenPresaleByCustomer(String custId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenPresaleByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception;

  /**
   * put your documentation comment here
   * @param txnId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader findOpenReservationById(String txnId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenReservationByStore(String storeId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param custId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenReservationByCustomer(String custId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenReservationByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception;

  /**
   * put your documentation comment here
   * @param txnId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader findOpenConsignmentById(String txnId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenConsignmentByStore(String storeId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param custId
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenConsignmentByCustomer(String custId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public abstract CMSTransactionHeader[] findOpenConsignmentByDate(String storeId, Date startDate
      , Date endDate)
      throws Exception;

  /**
   * put your documentation comment here
   * @param txn
   * @return
   * @exception Exception
   */
  public abstract ConsignmentTransaction updateConsignmentExpirationDate(ConsignmentTransaction txn)
      throws Exception;

  public abstract ReservationTransaction updateReservationExpirationDate(ReservationTransaction txn)
      throws Exception;

  /**
   * put your documentation comment here
   * @param txn
   * @return
   * @exception Exception
   */
  public abstract PresaleTransaction updatePresaleExpirationDate(PresaleTransaction txn)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @param registerId
   * @return
   * @exception Exception
   */
  public abstract String[] findTxnIdsByStoreIdAndRegisterId(String storeId, String registerId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param sStoreId
   * @param sRegisterId
   * @return
   * @exception Exception
   */
  public abstract FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(String sStoreId
      , String sRegisterId)
      throws Exception;

  /**
   * put your documentation comment here
   * @param fiscalDocument
   * @return
   * @exception Exception
   */
  public abstract boolean submitFiscalDocument(FiscalDocument fiscalDocument)
      throws Exception;


  /**
   * @param composite CMSCompositePOSTransaction
   * @throws Exception
   * @return boolean
   */
  public abstract CMSCompositePOSTransaction addShippingRequestToTransaction(CMSCompositePOSTransaction composite)
      throws Exception;


  /**
   * put your documentation comment here
   * @param txn
   * @return
   * @exception Exception
   */
  public abstract CMSCompositePOSTransaction updateCustomer(CMSCompositePOSTransaction txn)
      throws Exception;


  public abstract CMSCompositePOSTransaction updateConsultant(CMSCompositePOSTransaction txn, POSLineItem[] lineItems)
      throws Exception;
  
  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws Exception
	 * @return String
	 */
  public abstract String selectDigitalSignature(String txnId)
	      throws Exception;

//added by shushma for promotion
public abstract String[] findPromCodeByTxnId(String txnId)throws Exception;
	// TODO Auto-generated method stub

//Added by Anjana to save promo code in DB for Returns
public abstract String findPromCodeByTxnIdandBarcode(String txnId , String barcode)throws Exception;

}

