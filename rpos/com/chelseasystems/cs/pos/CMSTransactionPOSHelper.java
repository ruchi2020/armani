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
 | 3    | 04-29-2005 | Pankaja   | N/A       | New method for updation of the expiration dt |
 --------------------------------------------------------------------------------------------
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Cosignment/Presale impl                |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.employee.CMSEmployee;
import java.lang.*;
import java.util.*;
import com.chelseasystems.cr.util.DateUtil;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
//import com.chelseasystems.cs.item.CMSItemClientServices;


/**
 *
 * <p>Title: CMSTransactionPOSHelper</p>
 *
 * <p>Description: Static convenience methods to manipulate Client Services.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSTransactionPOSHelper {

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @return CMSCompositePOSTransaction
   * @throws BusinessRuleException
   */
  public static CMSCompositePOSTransaction allocate(IRepositoryManager theAppMgr)
      throws BusinessRuleException {
    CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.
        getGlobalObject("STORE"));
    txn.setProcessDate((Date)theAppMgr.getGlobalObject("PROCESS_DATE"));
    txn.setTheOperator((CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
    return txn;
  }

  /**
   * This method is used to submit the cms composite pos transaction
   * @param theAppMgr IRepositoryManager
   * @param composite CMSCompositePOSTransaction
   * @return boolean
   * @throws Exception
   */
  public static boolean submit(IRepositoryManager theAppMgr, CMSCompositePOSTransaction composite)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.submit(composite);
  }

  /**
   * This method is used to find transaction on the basis of transaction id
   * @param theAppMgr IRepositoryManager
   * @param id String
   * @return PaymentTransaction
   * @throws Exception
   */
  public static PaymentTransaction findById(IRepositoryManager theAppMgr, String id)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findById(id);
  }

  /**
   * This method is used to find transaction on the basis of customer id
   *
   * @param theAppMgr IRepositoryManager
   * @param customerId String
   * @return PaymentTransaction[]
   * @throws Exception
   */
  public static PaymentTransaction[] findByCustomerId(IRepositoryManager theAppMgr
      , String customerId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findByCustomerId(customerId);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @param txnSrchStr TransactionSearchString
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findBySearchCriteria(IRepositoryManager theAppMgr
      , TransactionSearchString txnSrchStr)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findBySearchCriteria(txnSrchStr);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @param sCustomerId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByCustomerIdAndShippingRequested(IRepositoryManager
      theAppMgr, String sCustomerId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findByCustomerIdAndShippingRequested(sCustomerId);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @param sCustomerId String
   * @param dtStart Date
   * @param dtEnd Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByCustomerIdAndDates(IRepositoryManager theAppMgr
      , String sCustomerId, Date dtStart, Date dtEnd)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findByCustomerIdAndDates(sCustomerId, dtStart, dtEnd);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @param sCustomerId String
   * @param sPaymentType String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByCustomerIdAndPaymentType(IRepositoryManager theAppMgr
      , String sCustomerId, String sPaymentType)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findByCustomerIdAndPaymentType(sCustomerId, sPaymentType);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @param customerId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByCustomerIdHeader(IRepositoryManager theAppMgr
      , String customerId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findByCustomerIdHeader(customerId);
  }

  /**
   * This method is used to find transaction on the basis of store id and for
   * the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByDate(IRepositoryManager theAppMgr, String aStoreId
      , Date aBeginDate, Date anEndDate)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByDate(aStoreId, beginDayDate, endDayDate);
  }

  /**
   * This method is used to find transaction on the basis of store id, exact
   * amount and for the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @param anAmount Currency
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByExactAmount(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate, ArmCurrency anAmount)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByExactAmount(aStoreId, beginDayDate, endDayDate, anAmount);
  }

  /**
   * This method is used to find transaction on the basis of store id, payment
   * type and for the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @param aPayment String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByPaymentType(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate, String aPayment)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByPaymentType(aStoreId, beginDayDate, endDayDate, aPayment);
  }

  /**
   * This method is used to find transaction on the basis of store id, credit
   * payment type and for the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @param aPayments String[]
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByCreditPaymentType(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate, String[] aPayments)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByCreditPaymentType(aStoreId, beginDayDate, endDayDate, aPayments);
  }

  /**
   * This method is used to find transaction on the basis of store id, discount
   * type and for the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @param aDiscount String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByDiscountType(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate, String aDiscount)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByDiscountType(aStoreId, beginDayDate, endDayDate, aDiscount);
  }

  /**
   * This method is used to find transaction on the basis of store id, transaction
   * type and for the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @param aTransType String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByTransactionType(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate, String aTransType)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByTransactionType(aStoreId, beginDayDate, endDayDate, aTransType);
  }

  /**
   * This method is used to find transaction on the basis of store id, consultant
   * id and for the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @param aConsultantID String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByConsultantID(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate, String aConsultantID)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByConsultantID(aStoreId, beginDayDate, endDayDate, aConsultantID);
  }

  /**
   * This method is used to find transaction on the basis of store id, operator
   * id and for the specific date range
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @param anOperatorID String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByOperatorID(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate, String anOperatorID)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByOperatorID(aStoreId, beginDayDate, endDayDate, anOperatorID);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @param aStoreId String
   * @param aBeginDate Date
   * @param anEndDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByShippingRequested(IRepositoryManager theAppMgr
      , String aStoreId, Date aBeginDate, Date anEndDate)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(aBeginDate);
    Date endDayDate = DateUtil.getEndOfDay(anEndDate);
    return cs.findByShippingRequested(aStoreId, beginDayDate, endDayDate);
  }

  /**
   *
   * @param theAppMgr IRepositoryManager
   * @param adHocQueryConstraints AdHocQueryConstraints
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findByAdHocQueryConstraints(IRepositoryManager theAppMgr
      , AdHocQueryConstraints adHocQueryConstraints)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    //Make sure all manipulation of dates happens on the client, to accomadate Time Zones
    Date beginDayDate = DateUtil.getBeginingOfDay(adHocQueryConstraints.getTxnBeginDate());
    Date endDayDate = DateUtil.getEndOfDay(adHocQueryConstraints.getTxnEndDate());
    adHocQueryConstraints.setTxnDateRange(beginDayDate, endDayDate);
    return (CMSTransactionHeader[])cs.findByAdHocQueryConstraints(adHocQueryConstraints);
  }

  /**
   */
  /*public static CMSTransactionHeader[] findByDateAndSearchValue (IRepositoryManager theAppMgr, String aStoreId,
   Date aBeginDate, Date anEndDate, int searchType, String searchValue) throws Exception {
   CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices) theAppMgr.getGlobalObject("TXN_POS_SRVC");
   return  cs.findByDateAndSearchValue (aStoreId, aBeginDate, anEndDate, searchType, searchValue);
   }*/
  /**
   * This method return the payment summaries for the specified store for on
   * specified date.
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @return PaymentSummary[]
   * @throws Exception
   */
  public static PaymentSummary[] getStorePaymentSummary(IRepositoryManager theAppMgr
      , String storeId, Date date)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return (PaymentSummary[])cs.getStorePaymentSummary(storeId, date);
  }

  /**
   * This method return the payment summaries for the specified store for on specified date grouped
   * by payment type. The keys in the returned hashtable will be payment type String
   * objects. The values will be PaymentSummary objects. The operator id and register id
   * in the returned PaymentSummary objects will have no meaning.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @return Hashtable
   * @throws Exception
   */
  public static Hashtable getStorePaymentSummaryTableByPaymentType(IRepositoryManager theAppMgr
      , String storeId, Date date)
      throws Exception {
    PaymentSummary[] summaries = getStorePaymentSummary(theAppMgr, storeId, date);
    Hashtable returnTable = new Hashtable();
    for (int index = 0; index < summaries.length; index++) {
      String typeId = summaries[index].getPaymentType();
      PaymentSummary tempPS = (PaymentSummary)returnTable.get(typeId);
      if (tempPS == null) {
        returnTable.put(typeId, summaries[index]);
      } else {
        tempPS.setPaymentTotal(tempPS.getPaymentTotal().add(summaries[index].getPaymentTotal()));
      }
    }
    return returnTable;
  }

  /**
   * This method returns the transaction type summaries for the specified store
   * on the specified date.
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @return TxnTypeSummary[]
   * @throws Exception
   */
  public static TxnTypeSummary[] getStoreTxnTypeSummary(IRepositoryManager theAppMgr
      , String storeId, Date date)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return (TxnTypeSummary[])cs.getStoreTxnTypeSummary(storeId, date);
  }

  /**
   * This method returns the transaction type summaries for the specified store on the specified date
   * grouped by transaction type. The keys in the returned hashtable will be the type id
   * String objects. The values will be TxnTypeSummary instances. The operator id and
   * register id in the TxnTypeSummary objects will not be meaningful.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @return Hashtable
   * @throws Exception
   */
  public static Hashtable getStoreTxnTypeSummaryTableByTxnType(IRepositoryManager theAppMgr
      , String storeId, Date date)
      throws Exception {
    TxnTypeSummary[] summaries = getStoreTxnTypeSummary(theAppMgr, storeId, date);
    return buildTxnTypeSummaryTableByType(summaries);
  }

  /**
   * This method returns the transaction type cumulative summaries for the
   * specified store on the specified date grouped by transaction type. The keys
   * in the returned hashtable will be the type id String objects. The values
   * will be TxnTypeSummary instances. The operator id and register id in the
   * TxnTypeSummary objects will not be meaningful.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @return Hashtable
   * @throws Exception
   */
  public static Hashtable getStoreTxnTypeCumulativeSummaryTableByTxnType(IRepositoryManager
      theAppMgr, String storeId, Date date)
      throws Exception {
    TxnTypeSummary[] summaries = getStoreTxnTypeSummary(theAppMgr, storeId, date);
    return buildTxnTypeCumulativeSummaryTableByType(summaries);
  }

  /**
   * This method returns the transaction type summaries for the specified store
   * on the specified date by the specified operator.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @param operatorId String
   * @return TxnTypeSummary[]
   * @throws Exception
   */
  public static TxnTypeSummary[] getStoreTxnTypeSummary(IRepositoryManager theAppMgr
      , String storeId, Date date, String operatorId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return (TxnTypeSummary[])cs.getStoreTxnTypeSummary(storeId, date, operatorId);
  }

  /**
   * This method return the transaction type summaries for the specified store
   * on the specified date by the specified operator grouped by transaction
   * type. The keys in the returned hashtable will be the type id String
   * objects. The values will be TxnTypeSummary instances. The register id
   * in the TxnTypeSummary objects will not be meaningful.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @param operatorId String
   * @return Hashtable
   * @throws Exception
   */
  public static Hashtable getStoreTxnTypeSummaryTableByTxnType(IRepositoryManager theAppMgr
      , String storeId, Date date, String operatorId)
      throws Exception {
    TxnTypeSummary[] summaries = getStoreTxnTypeSummary(theAppMgr, storeId, date, operatorId);
    return CMSTransactionPOSHelper.buildTxnTypeSummaryTableByType(summaries);
  }

  /**
   * This method returns the transaction type cumulative summaries for the
   * specified store on the specified date by the specified operator grouped by
   * transaction type. The keys in the returned hashtable will be the type id
   * String objects. The values will be TxnTypeSummary instances. The register
   * id in the TxnTypeSummary objects will not be meaningful.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @param operatorId String
   * @return Hashtable
   * @throws Exception
   */
  public static Hashtable getStoreTxnTypeCumulativeSummaryTableByTxnType(IRepositoryManager
      theAppMgr, String storeId, Date date, String operatorId)
      throws Exception {
    TxnTypeSummary[] summaries = getStoreTxnTypeSummary(theAppMgr, storeId, date, operatorId);
    return CMSTransactionPOSHelper.buildTxnTypeCumulativeSummaryTableByType(summaries);
  }

  /**
   * This method returns the sales summaries for the specified store on the
   * specified date.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @return SalesSummary[]
   * @throws Exception
   */
  public static SalesSummary[] getStoreSalesSummary(IRepositoryManager theAppMgr, String storeId
      , Date date)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return (SalesSummary[])cs.getStoreSalesSummary(storeId, date);
  }

  /**
   * This method returns the sales summaries for the specified store for on
   * specified date grouped by item. The keys of the returned table will be item
   * id String objects. The values will be SalesSummary objects. The operator id
   * and register id will be meaningless in the SalesSummary objects in the
   * returned table.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param date Date
   * @return Hashtable
   * @throws Exception
   */
  public static Hashtable getStoreSalesSummaryTableByItem(IRepositoryManager theAppMgr
      , String storeId, Date date)
      throws Exception {
    SalesSummary[] summaries = getStoreSalesSummary(theAppMgr, storeId, date);
    Hashtable returnTable = new Hashtable();
    for (int index = 0; index < summaries.length; index++) {
      String itemId = summaries[index].getItemId();
      SalesSummary tempSS = (SalesSummary)returnTable.get(itemId);
      if (tempSS == null) {
        returnTable.put(itemId, summaries[index]);
      } else {
        tempSS.setTotalAmount(tempSS.getTotalAmount().add(summaries[index].getTotalAmount()));
        tempSS.setTotalQuantity(tempSS.getTotalQuantity().intValue()
            + summaries[index].getTotalQuantity().intValue());
      }
    }
    return returnTable;
  }

  /**
   * This method return the sales summaries for the specified store between the
   * specified dates (inclusive).
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param from Date
   * @param to Date
   * @return SalesSummary[]
   * @throws Exception
   */
  public static SalesSummary[] getStoreSalesSummary(IRepositoryManager theAppMgr, String storeId
      , Date from, Date to)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return (SalesSummary[])cs.getStoreSalesSummary(storeId, from, to);
  }

  /**
   * This method returns the sales summaries for the specified store between the specified
   * dates (inclusive) grouped by item. The keys of the returned table will be
   * item id String objects. The values will be SalesSummary objects. The
   * operator id, register id, and date will be meaningless in the SalesSummary
   * objects in the returned table.
   *
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param from Date
   * @param to Date
   * @return Hashtable
   * @throws Exception
   */
  public static Hashtable getStoreSalesSummaryTableByItem(IRepositoryManager theAppMgr
      , String storeId, Date from, Date to)
      throws Exception {
    SalesSummary[] summaries = getStoreSalesSummary(theAppMgr, storeId, from, to);
    Hashtable returnTable = new Hashtable();
    for (int index = 0; index < summaries.length; index++) {
      String itemId = summaries[index].getItemId();
      SalesSummary tempSS = (SalesSummary)returnTable.get(itemId);
      if (tempSS == null) {
        returnTable.put(itemId, summaries[index]);
      } else {
        tempSS.setTotalAmount(tempSS.getTotalAmount().add(summaries[index].getTotalAmount()));
        tempSS.setTotalQuantity(tempSS.getTotalQuantity().intValue()
            + summaries[index].getTotalQuantity().intValue());
      }
    }
    return returnTable;
  }

  /**
   *
   * @param summaries TxnTypeSummary[]
   * @return Hashtable
   * @throws CurrencyException
   */
  private static Hashtable buildTxnTypeSummaryTableByType(TxnTypeSummary[] summaries)
      throws CurrencyException {
    Hashtable returnTable = new Hashtable();
    for (int index = 0; index < summaries.length; index++) {
      String typeId = summaries[index].getTxnType();
      String voidedTypeId = summaries[index].getVoidedTxnType();
      if (voidedTypeId == null || voidedTypeId.equals("")) {
        CMSTransactionPOSHelper.addTxnTypeSummaryToTypeTable(summaries[index], typeId, returnTable, false);
      } else {
        CMSTransactionPOSHelper.addTxnTypeSummaryToTypeTable(summaries[index], voidedTypeId
            , returnTable, true);
      }
    }
    return returnTable;
  }

  /**
   *
   * @param summaries TxnTypeSummary[]
   * @return Hashtable
   * @throws CurrencyException
   */
  private static Hashtable buildTxnTypeCumulativeSummaryTableByType(TxnTypeSummary[] summaries)
      throws CurrencyException {
    Hashtable returnTable = new Hashtable();
    for (int index = 0; index < summaries.length; index++) {
      CMSTransactionPOSHelper.addTxnTypeSummaryToTypeTable(summaries[index]
          , summaries[index].getTxnType(), returnTable, false);
    }
    return returnTable;
  }

  /**
   *
   * @param summary TxnTypeSummary
   * @param typeId String
   * @param table Hashtable
   * @param subtract boolean
   * @throws CurrencyException
   */
  private static void addTxnTypeSummaryToTypeTable(TxnTypeSummary summary, String typeId
      , Hashtable table, boolean subtract)
      throws CurrencyException {
    TxnTypeSummary tempTTS = (TxnTypeSummary)table.get(typeId);
    if (tempTTS == null) {
      tempTTS = subtract ? summary.negate() : summary;
      if (subtract) {
        tempTTS.setTxnType(typeId);
        tempTTS.setVoidedTxnType("");
      }
      table.put(typeId, tempTTS);
    } else {
      if (subtract) {
        tempTTS.decrease(summary);
      } else {
        tempTTS.increase(summary);
      }
    }
  }

  /**
   * This method is used to find open pre sale transaction on the basis of
   * transaction id.
   * @param theAppMgr IRepositoryManager
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public static CMSTransactionHeader findOpenPresaleById(IRepositoryManager theAppMgr, String txnId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenPresaleById(txnId);
  }

  /**
   * This method is used to find open pre sale transaction on the basis of
   * store id.
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenPresaleByStore(IRepositoryManager theAppMgr
      , String storeId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenPresaleByStore(storeId);
  }

  /**
   * This method is used to find open pre sale transaction on the basis of
   * customer id.
   * @param theAppMgr IRepositoryManager
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenPresaleByCustomer(IRepositoryManager theAppMgr
      , String custId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenPresaleByCustomer(custId);
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * store id and for specific date range.
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenPresaleByDate(IRepositoryManager theAppMgr
      , String storeId, Date startDate, Date endDate)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenPresaleByDate(storeId, startDate, endDate);
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * transaction id.
   * @param theAppMgr IRepositoryManager
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public static CMSTransactionHeader findOpenConsignmentById(IRepositoryManager theAppMgr
      , String txnId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenConsignmentById(txnId);
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * store id.
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenConsignmentByStore(IRepositoryManager theAppMgr
      , String storeId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenConsignmentByStore(storeId);
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * customer id.
   * @param theAppMgr IRepositoryManager
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenConsignmentByCustomer(IRepositoryManager theAppMgr
      , String custId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenConsignmentByCustomer(custId);
  }

  /**
   * This method is used to find open consignment transaction on the basis of
   * store id and a specific date range
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenConsignmentByDate(IRepositoryManager theAppMgr
      , String storeId, Date startDate, Date endDate)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenConsignmentByDate(storeId, startDate, endDate);
  }

  /**
   * This method is used to update of the Expiration Date for Consignment
   * Transactions
   * @param theAppMgr
   * @param txn
   * @return
   * @throws Exception
   */
  public static ConsignmentTransaction updateConsignmentExpirationDate(IRepositoryManager theAppMgr
      , ConsignmentTransaction txn)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.updateConsignmentExpirationDate(txn);
  }

  /**
   * This method is used to update of the Expiration Date for Reservation
   * Transactions
   * @param theAppMgr
   * @param txn
   * @return
   * @throws Exception
   */
  public static ReservationTransaction updateReservationExpirationDate(IRepositoryManager theAppMgr
      , ReservationTransaction txn)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.updateReservationExpirationDate(txn);
  }


  /**
   * This method is used to update the Expiration Date for Presale Transaction
   * @param theAppMgr
   * @param txn
   * @return
   * @throws Exception
   */
  public static PresaleTransaction updatePresaleExpirationDate(IRepositoryManager theAppMgr
      , PresaleTransaction txn)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.updatePresaleExpirationDate(txn);
  }

  /**
   * New method for findTxnIdsByStore&RegIDs
   */
  /**
   * This is new method, which is used to find transaction id on the basis of
   * store id and registration id
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param registerId String
   * @return String[]
   * @throws Exception
   */
  public static String[] findTxnIdsByStoreIdAndRegisterId(IRepositoryManager theAppMgr
      , String storeId, String registerId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findTxnIdsByStoreIdAndRegisterId(storeId, registerId);
  }
  
  //added by shushma for promotion
  public static String[] findPromCodeByTxnId(IRepositoryManager theAppMgr, String txnId)
	      throws Exception {
	    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
	        "TXN_POS_SRVC");
	    return cs.findPromCodeByTxnId(txnId);
	  }
  
  //added by Anjana to persist promo code inARM_STG_TXN_dtl table while return happens
  public static String findPromCodeByTxnIdandBarcode(IRepositoryManager theAppMgr, String txnId , String barcode)
	      throws Exception {
	   CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
	        "TXN_POS_SRVC");
	    return cs.findPromCodeByTxnIdandBarcode(txnId, barcode);
	  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @param storeId
   * @param registerId
   * @return
   * @exception Exception
   */
  public static FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(IRepositoryManager
      theAppMgr, String storeId, String registerId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findFiscalDocNumByStoreAndRegister(storeId, registerId);
  }

  /**
   * This method is used to submit the FiscalDocument
   * @param theAppMgr IRepositoryManager
   * @param fiscalDocument FiscalDocument
   * @throws Exception
   * @return boolean
   */
  public static boolean submitFiscalDocument(IRepositoryManager theAppMgr
      , FiscalDocument fiscalDocument)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.submitFiscalDocument(fiscalDocument);
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * transaction id.
   * @param theAppMgr IRepositoryManager
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public static CMSTransactionHeader findOpenReservationById(IRepositoryManager theAppMgr
      , String txnId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenReservationById(txnId);
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * store id.
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenReservationByStore(IRepositoryManager theAppMgr
      , String storeId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenReservationByStore(storeId);
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * customer id.
   * @param theAppMgr IRepositoryManager
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenReservationByCustomer(IRepositoryManager theAppMgr
      , String custId)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenReservationByCustomer(custId);
  }

  /**
   * This method is used to find open Reservation transaction on the basis of
   * store id and a specific date range
   * @param theAppMgr IRepositoryManager
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public static CMSTransactionHeader[] findOpenReservationByDate(IRepositoryManager theAppMgr
      , String storeId, Date startDate, Date endDate)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.findOpenReservationByDate(storeId, startDate, endDate);
  }

  public static CMSCompositePOSTransaction addShippingRequestToTransaction(IRepositoryManager theAppMgr, CMSCompositePOSTransaction composite)
      throws Exception {
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.addShippingRequestToTransaction(composite);
  }

  /**
   * This method is used to update of the customer for posTransaction
   * Transactions
   * @param theAppMgr
   * @param theTxn
   * @param oldCustomer
   * @return
   * @throws Exception
   */
  public static CMSCompositePOSTransaction updateCustomer(IRepositoryManager theAppMgr,
      CMSCompositePOSTransaction theTxn)
      throws Exception {

    System.out.println("CMSCompositePOSTransaction:updateCustomer:");
    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.updateCustomer(theTxn);
  }

  public static CMSCompositePOSTransaction updateConsultant(IRepositoryManager theAppMgr,
      CMSCompositePOSTransaction theTxn, POSLineItem[] lineItems)
      throws Exception {
    System.out.println("CMSCompositePOSTransaction:updateConsultant:" + lineItems.length);

    CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices)theAppMgr.getGlobalObject(
        "TXN_POS_SRVC");
    return cs.updateConsultant(theTxn, lineItems);
  }
  
  /**
	 * Added by Satin.
	 * This method is used to search digital signature based on transactionId.
	 * 
	 * @param theAppMgr
	 *            IRepositoryManager
	 * @param txnId
	 *            String
	 * @return String
	 * @throws Exception
	 */
	public static String selectDigitalSignature(IRepositoryManager theAppMgr, String txnId) throws Exception {
		CMSTransactionPOSClientServices cs = (CMSTransactionPOSClientServices) theAppMgr.getGlobalObject("TXN_POS_SRVC");
		return cs.selectDigitalSignature(txnId);
	  } 

}

