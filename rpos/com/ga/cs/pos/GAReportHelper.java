/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.ga.cs.pos.datatranssferobject.*;
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.chelseasystems.cs.txnposter.ArmSalesSummary;
import java.util.*;


/**
 * Static convenience methods to manipulate Client Services.
 */
public class GAReportHelper {

  /**
   * This method is for Media Reports.
   * Return the payment summaries for the specified store for on specified date grouped
   * by payment type. The keys in the returned hashtable will be payment type String
   * objects. The values will be MediaReportDataTransferObject objects.
   */
  public static Hashtable getStorePaymentSummaryTableByPaymentType(IRepositoryManager theMgr
      , String storeId, Date strartDate, Date endDate)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getStorePaymentSummaryTableByPaymentType(storeId, strartDate, endDate);
  }

  /**
   * This mehtod is callled by Over/Short Report
   */
  public static HashMap getOverShortSummariesAndTotals(IRepositoryManager theMgr, String storeId
      , Date dateStart, Date dateEnd)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getOverShortSummariesAndTotals(storeId, dateStart, dateEnd);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param storeId
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public static Hashtable getTransactionAnalysisDataByStore(IRepositoryManager theMgr
      , String storeId, Date startDate, Date endDate)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getTransactionAnalysisDataByStore(storeId, startDate, endDate);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public static Hashtable getAssociateSalesSummaryTableByDate(IRepositoryManager theMgr
      , String store, Date startDate, Date endDate)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getAssociateSalesSummaryTableByDate(store, startDate, endDate);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public static SalesSummary[] getDepartmentReportByStoreIdAndDates(IRepositoryManager theMgr
      , String store, Date startDate, Date endDate)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getDepartmentReportByStoreIdAndDates(store, startDate, endDate);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store1
   * @param start1
   * @param end1
   * @param dept1
   * @return
   * @exception Exception
   */
  public static Hashtable getDeptClassSalesReportByStoreIdAndDates(IRepositoryManager theMgr
      , String store1, Date start1, Date end1, String dept1)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    SalesSummary[] salesSummaries = cs.getDeptClassSalesReportByStoreIdDeptIdAndDates(store1
        , start1, end1, dept1);
    Hashtable hDeptSales = new Hashtable();
    for (int index = 0; index < salesSummaries.length; index++) {
      ArmSalesSummary summ = (ArmSalesSummary)salesSummaries[index];
      if (hDeptSales.containsKey(summ.getDeptID())) {
        ArrayList list = (ArrayList)hDeptSales.get(summ.getDeptID());
        list.add(summ);
      } else {
        ArrayList list = new ArrayList();
        list.add(summ);
        hDeptSales.put(summ.getDeptID(), list);
      }
    }
    return hDeptSales;
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public static SalesSummary[] getGroupReportByStoreIdAndDates(IRepositoryManager theMgr
      , String store, Date startDate, Date endDate)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getGroupReportByStoreIdAndDates(store, startDate, endDate);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store1
   * @param start1
   * @param end1
   * @param group1
   * @return
   * @exception Exception
   */
  public static Hashtable getDepartmentSalesReportByStoreIdGroupAndDates(IRepositoryManager theMgr
      , String store1, Date start1, Date end1, String group1)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    SalesSummary[] salesSummaries = cs.getDepartmentSalesReportByStoreIdGroupAndDates(store1
        , start1, end1, group1);
    Hashtable hDeptSales = new Hashtable();
    for (int index = 0; index < salesSummaries.length; index++) {
      ArmSalesSummary summ = (ArmSalesSummary)salesSummaries[index];
      if (hDeptSales.containsKey(summ.getGrpDiv())) {
        ArrayList list = (ArrayList)hDeptSales.get(summ.getGrpDiv());
        list.add(summ);
      } else {
        ArrayList list = new ArrayList();
        list.add(summ);
        hDeptSales.put(summ.getGrpDiv(), list);
      }
    }
    return hDeptSales;
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store1
   * @param start1
   * @param end1
   * @param group1
   * @return
   * @exception Exception
   */
  public static Hashtable getClassSalesReportByStoreIdGroupAndDates(IRepositoryManager theMgr
      , String store1, Date start1, Date end1, String group1)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    SalesSummary[] salesSummaries = cs.getDeptClassSalesReportByStoreIdDeptIdAndDates(store1
        , start1, end1, group1);
    Hashtable hDeptSales = new Hashtable();
    for (int index = 0; index < salesSummaries.length; index++) {
      ArmSalesSummary summ = (ArmSalesSummary)salesSummaries[index];
      if (hDeptSales.containsKey(summ.getGrpDiv() + "*" + summ.getClassID())) {
        ArrayList list = (ArrayList)hDeptSales.get(summ.getGrpDiv() + "*" + summ.getClassID());
        list.add(summ);
      } else {
        ArrayList list = new ArrayList();
        list.add(summ);
        hDeptSales.put(summ.getGrpDiv() + "*" + summ.getClassID(), list);
      }
    }
    return hDeptSales;
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public static GaFiscalReportDataTransferObject[] getGaFiscalReportByStoreIdAndDates(
      IRepositoryManager theMgr, String store, Date startDate, Date endDate)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    Hashtable hObject = new Hashtable();
    GaTranFiscalReportDataTransferObject[] objects = cs.getTotalSalesByStoreIdHalfAndDates(store
        , startDate, endDate);
    for (int index = 0; index < objects.length; index++) {
      GaTranFiscalReportDataTransferObject object = objects[index];
      if (hObject.containsKey(object.getFiscalDay())) {
        GaFiscalReportDataTransferObject frObject = (GaFiscalReportDataTransferObject)hObject.get(
            object.getFiscalDay());
        frObject.addTranDetails(object);
      } else {
        GaFiscalReportDataTransferObject frObject = new GaFiscalReportDataTransferObject();
        frObject.setFiscalDay(object.getFiscalDay());
        frObject.addTranDetails(object);
        hObject.put(object.getFiscalDay(), frObject);
      }
    }
    // Sorting the records
    GaFiscalReportDataTransferObject[] arrayGAFRDTO = (GaFiscalReportDataTransferObject[])hObject.
        values().toArray(new GaFiscalReportDataTransferObject[0]);
    Arrays.sort(arrayGAFRDTO, new Comparator() {

      /**
       * put your documentation comment here
       * @param o1
       * @param o2
       * @return
       */
      public int compare(Object o1, Object o2) {
        return ((GaFiscalReportDataTransferObject)o1).getFiscalDay().compareTo(((
            GaFiscalReportDataTransferObject)o2).getFiscalDay());
      }
    });
    return arrayGAFRDTO;
    //return (GaFiscalReportDataTransferObject[])hObject.values().toArray(new GaFiscalReportDataTransferObject[0]);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param storeId
   * @param startDate
   * @param endDate
   * @return
   * @exception Exception
   */
  public static GaTranFiscalReportDataTransferObject[] getTotalSalesByStoreIdHalfAndDates(
      IRepositoryManager theMgr, String storeId, Date startDate, Date endDate)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getTotalSalesByStoreIdHalfAndDates(storeId, startDate, endDate);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param store1
   * @param start1
   * @param end1
   * @return
   * @exception Exception
   */
  public static SalesSummary[] getItemSalesReportByStoreIdAndDates(IRepositoryManager theMgr
      , String store1, Date start1, Date end1)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getItemSalesReportByStoreIdAndDates(store1, start1, end1);
  }

  /**
   * This method is for Consignment Summary Reports
   * @param theMgr
   * @param storeId
   * @param begin
   * @param end
   * @return ConsignmentSummaryReportDataTransferObject[]
   * @throws Exception
   */
  public static ConsignmentSummaryReportDataTransferObject[] getConsignmentSummaryByCustomer(
      IRepositoryManager theMgr, String storeId, Date begin, Date end)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getConsignmentSummaryByCustomer(storeId, begin, end);
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @exception Exception
   */
  public static ConsignmentDetailsReport[] getConsignmentDetails(IRepositoryManager theMgr
      , String storeId, Date begin, Date end)
      throws Exception {
    GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
    return cs.getConsignmentDetails(storeId, begin, end);
  }
  
  /**
   * Reservation Details
   * @param theMgr
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @throws Exception
   */
  public static ReservationDetailsReport[] getReservationDetails(IRepositoryManager theMgr
	  , String storeId, Date begin, Date end)
	  throws Exception {
	GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");
	return cs.getReservationDetails(storeId, begin, end);
  }
  
  /**
   * Presale Details
   * @param theMgr
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @throws Exception
   */
  public static PresaleDetailsReport[] getPresaleDetails(IRepositoryManager theMgr
	  , String storeId, Date begin, Date end)
	  throws Exception {
	GAReportClientServices cs = (GAReportClientServices)theMgr.getGlobalObject("GAREPORT_SRVC");	
	return cs.getPresaleDetails(storeId, begin, end);
  }
}

