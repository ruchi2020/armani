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
public class GAReportNullServices extends GAReportServices {

  //for Fred's reports -- POSReportHelper
  public Hashtable getStorePaymentSummaryTableByPaymentType(String storeId, Date dateStart
      , Date dateEnd)
      throws Exception {
    return new Hashtable();
  }

  /**
   * put your documentation comment here
   * @param storeId
   * @param dateStart
   * @param dateEnd
   * @return
   * @exception Exception
   */
  public HashMap getOverShortSummariesAndTotals(String storeId, Date dateStart, Date dateEnd)
      throws Exception {
    return new HashMap();
  }

  /**
   * put your documentation comment here
   * @param storeId
   * @param dateStart
   * @param dateEnd
   * @return
   * @exception Exception
   */
  public Hashtable getTransactionAnalysisDataByStore(String storeId, Date dateStart, Date dateEnd)
      throws Exception {
    return new Hashtable();
  }

  //for Magee's -- GAPOSReportHelper
  public Hashtable getAssociateSalesSummaryTableByDate(String storeId, Date dateStart, Date dateEnd)
      throws Exception {
    return new Hashtable();
  }

  //ClassReportApplet
  public SalesSummary[] getDepartmentReportByStoreIdAndDates(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    return new SalesSummary[0];
  }

  //  ClassReportApplet
  public SalesSummary[] getDeptClassSalesReportByStoreIdDeptIdAndDates(String stroreId1
      , Date startDate1, Date endDate1, String deptId1)
      throws Exception {
    return new SalesSummary[0];
  }

  //DepartmentReportApplet
  public SalesSummary[] getGroupReportByStoreIdAndDates(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    return new SalesSummary[0];
  }

  //DepartmentReportApplet
  public SalesSummary[] getDepartmentSalesReportByStoreIdGroupAndDates(String storeId1
      , Date startDate1, Date endDate1, String grpDiv1)
      throws Exception {
    return new SalesSummary[0];
  }

  //GaFiscalReportApplet
  public GaFiscalReportDataTransferObject[] getGaFiscalReportByStoreIdAndDates(String storeId
      , Date startDate, Date endDate)
      throws Exception {
    return new GaFiscalReportDataTransferObject[0];
  }

  //GaFiscalReportApplet
  public GaTranFiscalReportDataTransferObject[] getTotalSalesByStoreIdHalfAndDates(String storeId
      , Date startDate, Date endDate)
      throws Exception {
    return new GaTranFiscalReportDataTransferObject[0];
  }

  //GaItemReportApplet
  public SalesSummary[] getItemSalesReportByStoreIdAndDates(String storeId1, Date startDate1
      , Date endDate1)
      throws Exception {
    return new SalesSummary[0];
  }

  //Consignment Summary
  public ConsignmentSummaryReportDataTransferObject[] getConsignmentSummaryByCustomer(String
      storeId, Date begin, Date end)
      throws Exception {
    return new ConsignmentSummaryReportDataTransferObject[0];
  }

  //Consignment Details
  public ConsignmentDetailsReport[] getConsignmentDetails(String storeId, Date begin, Date end)
      throws Exception {
    return new ConsignmentDetailsReport[0];
  }
  
  //Reservation Details
  public ReservationDetailsReport[] getReservationDetails(String storeId, Date begin, Date end)
      throws Exception {
    return new ReservationDetailsReport[0];
  }
  
  //Presale Details
  public PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin, Date end)
      throws Exception {
    return new PresaleDetailsReport[0];
  }
}

