/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.rmi.*;
import java.util.*;
import com.ga.cs.pos.datatranssferobject.*;
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.igray.naming.IPing;


/**
 * Defines the customer services that are available remotely via RMI.
 */
public interface IGAReportRMIServer extends Remote, IPing {

  /**
   * comment
   * @param storeId comment
   * @param strartDate comment
   * @param endDate comment
   */
  public Hashtable getStorePaymentSummaryTableByPaymentType(String storeId, Date strartDate
      , Date endDate)
      throws RemoteException;


  /**
   * For Over/Short reports.
   * @param storeId storeId
   * @param beginDate begin date
   * @param endDate end date
   */
  public HashMap getOverShortSummariesAndTotals(String storeId, Date beginDate, Date endDate)
      throws RemoteException;


  /**
   * comment
   * @param storeId comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getTransactionAnalysisDataByStore(String storeId, Date startDate, Date endDate)
      throws RemoteException;


  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getAssociateSalesSummaryTableByDate(String store, Date startDate, Date endDate)
      throws RemoteException;


  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public SalesSummary[] getDepartmentReportByStoreIdAndDates(String store, Date startDate
      , Date endDate)
      throws RemoteException;


  /**
   * comment
   * @param store1 comment
   * @param start1 comment
   * @param end1 comment
   * @param dept1 comment
   * @param store2 comment
   * @param start2 comment
   * @param end2 comment
   * @param dept2 comment
   */
  public SalesSummary[] getDeptClassSalesReportByStoreIdDeptIdAndDates(String store1, Date start1
      , Date end1, String dept1)
      throws RemoteException;


  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public SalesSummary[] getGroupReportByStoreIdAndDates(String store, Date startDate, Date endDate)
      throws RemoteException;


  /**
   * default-list
   * @param store1 comment
   * @param start1 comment
   * @param end1 comment
   * @param group1 comment
   * @param store2 comment
   * @param start2 comment
   * @param end2 comment
   * @param group2 comment
   */
  public SalesSummary[] getDepartmentSalesReportByStoreIdGroupAndDates(String store1, Date start1
      , Date end1, String group1)
      throws RemoteException;


  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public GaFiscalReportDataTransferObject[] getGaFiscalReportByStoreIdAndDates(String store
      , Date startDate, Date endDate)
      throws RemoteException;


  /**
   * comment
   * @param store comment
   * @param fiscalDay comment
   */
  public GaTranFiscalReportDataTransferObject[] getTotalSalesByStoreIdHalfAndDates(String store
      , Date startDate, Date endDate)
      throws RemoteException;


  /**
   * comment
   * @param store1 comment
   * @param start1 comment
   * @param end1 comment
   * @param store2 comment
   * @param start2 comment
   * @param end2 comment
   */
  public SalesSummary[] getItemSalesReportByStoreIdAndDates(String store1, Date start1, Date end1)
      throws RemoteException;


  /**
   * Comments
   * @param storeId comment
   * @param begin comment
   * @param end comment
   */
  public ConsignmentSummaryReportDataTransferObject[] getConsignmentSummaryByCustomer(String
      storeId, Date begin, Date end)
      throws RemoteException;


  /**
   * Consignment Details
   * @param storeId
   * @param begin
   * @param end
   */
  public ConsignmentDetailsReport[] getConsignmentDetails(String storeId, Date begin, Date end)
      throws RemoteException;
  
  /**
   * Reservation Details
   * @param storeId
   * @param begin
   * @param end
   */
  public ReservationDetailsReport[] getReservationDetails(String storeId, Date begin, Date end)
      throws RemoteException;
  
  /**
   * Presale Details
   * @param storeId
   * @param begin
   * @param end
   */
  public PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin, Date end)
      throws RemoteException;
}

