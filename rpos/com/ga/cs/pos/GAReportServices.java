/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.util.*;
import com.ga.cs.pos.datatranssferobject.*;
import com.chelseasystems.cr.txnposter.SalesSummary;


/**
 * put your documentation comment here
 */
public abstract class GAReportServices {
  /***************************************************************************
   * Static variable for holding different client side and server side
   * implementations of a network service
   **************************************************************************/
  private static GAReportServices current = null;

  /**
   * Basic method that gets the current implementation of TransactionPOS find
   * network services.
   *
   * @return The current network service that a TransactionPOS object will use
   */
  public static GAReportServices getCurrent() {
    return current;
  }

  /**
   * Basic method that sets the current implementation of transactionPOS find
   * network services
   *
   * @param svcs a service implementation that extends the TransactionPOSServices class
   */
  public static void setCurrent(GAReportServices svcs) {
    current = svcs;
  }

  //for Media Report
  public abstract Hashtable getStorePaymentSummaryTableByPaymentType(String storeId, Date dateStart
      , Date dateEnd)
      throws Exception;

  //for ConsolidatedOverShortReport
  public abstract HashMap getOverShortSummariesAndTotals(String storeId, Date dateStart
      , Date dateEnd)
      throws Exception;

  /**
   * put your documentation comment here
   * @param storeId
   * @param dateStart
   * @param dateEnd
   * @return
   * @exception Exception
   */
  public abstract Hashtable getTransactionAnalysisDataByStore(String storeId, Date dateStart
      , Date dateEnd)
      throws Exception;

  //for Associate Sale Report
  public abstract Hashtable getAssociateSalesSummaryTableByDate(String storeId, Date dateStart
      , Date dateEnd)
      throws Exception;

  //ClassReportApplet
  public abstract SalesSummary[] getDepartmentReportByStoreIdAndDates(String storeId
      , Date startDate, Date endDate)
      throws Exception;

  //ClassReportApplet
  public abstract SalesSummary[] getDeptClassSalesReportByStoreIdDeptIdAndDates(String stroreId1
      , Date startDate1, Date endDate1, String deptId1)
      throws Exception;

  //DepartmentReportApplet
  public abstract SalesSummary[] getGroupReportByStoreIdAndDates(String storeId, Date startDate
      , Date endDate)
      throws Exception;

  //DepartmentReportApplet
  public abstract SalesSummary[] getDepartmentSalesReportByStoreIdGroupAndDates(String storeId1
      , Date startDate1, Date endDate1, String grpDiv1)
      throws Exception;

  //GaFiscalReportApplet
  public abstract GaFiscalReportDataTransferObject[] getGaFiscalReportByStoreIdAndDates(String
      storeId, Date startDate, Date endDate)
      throws Exception;

  //GaFiscalReportApplet
  public abstract GaTranFiscalReportDataTransferObject[] getTotalSalesByStoreIdHalfAndDates(String
      storeId, Date startDate, Date endDate)
      throws Exception;

  //GaItemReportApplet
  public abstract SalesSummary[] getItemSalesReportByStoreIdAndDates(String storeId1
      , Date startDate1, Date endDate1)
      throws Exception;

  //Consignment Summary
  public abstract ConsignmentSummaryReportDataTransferObject[] getConsignmentSummaryByCustomer(
      String storeId, Date begin, Date end)
      throws Exception;

  //Consignment Details
  public abstract ConsignmentDetailsReport[] getConsignmentDetails(String storeId, Date begin
      , Date end)
      throws Exception;
  
  //Reservation Details
  public abstract ReservationDetailsReport[] getReservationDetails(String storeId, Date begin
      , Date end)
      throws Exception;
  
  //Presale Details
  public abstract PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin
      , Date end)
      throws Exception;
}

