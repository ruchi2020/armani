/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.ga.cs.pos.datatranssferobject.*;


/**
 * Client-side object for retrieving and submitting.
 */
public class GAReportClientServices extends ClientServices {

  /** Configuration manager **/
 // private ConfigMgr config = null;

  /**
   * Set the current implementation
   */
  public GAReportClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("gareport.cfg");
  }

  /**
   * initialize primary implementation
   */
  public void init(boolean online)
      throws Exception {
    // Set up the proper implementation of the service.
    if (online) {
      onLineMode();
    } else {
      offLineMode();
    }
  }

  /**
   * Reads "CLIENT_IMPL" from config file. Returns the class that defines
   * what object is providing the service to objects using this client service
   * in "on-line" mode, i.e. connected to an app server.  If null, this 
   * clientservice is not considered when determining app online status.
   * @return a class of the online service.
   */
  protected Class getOnlineService () throws ClassNotFoundException {
    String className = config.getString("CLIENT_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }
  
  /**
   * put your documentation comment here
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for GAReportClientServices");
    GAReportServices serviceImpl = (GAReportServices)config.getObject("CLIENT_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("GAReportClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of GAReportServices in gareport.cfg."
          , "Make sure that gareport.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of GAReportServices.", LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    GAReportServices.setCurrent(serviceImpl);
  }

  /**
   * put your documentation comment here
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for GAReportClientServices");
    GAReportServices serviceImpl = (GAReportServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("GAReportClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of GAReportServices in gareport.cfg."
          , "Make sure that gareport.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of GAReportServices.", LoggingServices.CRITICAL);
    }
    GAReportServices.setCurrent(serviceImpl);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Object getCurrentService() {
    return GAReportServices.getCurrent();
  }

  /**
   * comment
   * @param storeId comment
   * @param strartDate comment
   * @param endDate comment
   */
  public Hashtable getStorePaymentSummaryTableByPaymentType(String storeId, Date strartDate
      , Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Hashtable)GAReportServices.getCurrent().getStorePaymentSummaryTableByPaymentType(
          storeId, strartDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getStorePaymentSummaryTableByPaymentType(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Hashtable)GAReportServices.getCurrent().getStorePaymentSummaryTableByPaymentType(
          storeId, strartDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * For Over/Short reports.
   * @param storeId storeId
   * @param beginDate begin date
   * @param endDate end date
   */
  public HashMap getOverShortSummariesAndTotals(String storeId, Date beginDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (HashMap)GAReportServices.getCurrent().getOverShortSummariesAndTotals(storeId
          , beginDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getOverShortSummariesAndTotals(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (HashMap)GAReportServices.getCurrent().getOverShortSummariesAndTotals(storeId
          , beginDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * comment
   * @param storeId comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getTransactionAnalysisDataByStore(String storeId, Date startDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Hashtable)GAReportServices.getCurrent().getTransactionAnalysisDataByStore(storeId
          , startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getTransactionAnalysisDataByStore(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Hashtable)GAReportServices.getCurrent().getTransactionAnalysisDataByStore(storeId
          , startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getAssociateSalesSummaryTableByDate(String store, Date startDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (Hashtable)GAReportServices.getCurrent().getAssociateSalesSummaryTableByDate(store
          , startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getAssociateSalesSummaryTableByDate(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Hashtable)GAReportServices.getCurrent().getAssociateSalesSummaryTableByDate(store
          , startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public SalesSummary[] getDepartmentReportByStoreIdAndDates(String store, Date startDate
      , Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SalesSummary[])GAReportServices.getCurrent().getDepartmentReportByStoreIdAndDates(
          store, startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getDepartmentReportByStoreIdAndDates(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SalesSummary[])GAReportServices.getCurrent().getDepartmentReportByStoreIdAndDates(
          store, startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

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
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SalesSummary[])GAReportServices.getCurrent().
          getDeptClassSalesReportByStoreIdDeptIdAndDates(store1, start1, end1, dept1);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getDeptClassSalesReportByStoreIdDeptIdAndDates(String,Date,Date,String,String,Date,Date,String)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SalesSummary[])GAReportServices.getCurrent().
          getDeptClassSalesReportByStoreIdDeptIdAndDates(store1, start1, end1, dept1);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public SalesSummary[] getGroupReportByStoreIdAndDates(String store, Date startDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SalesSummary[])GAReportServices.getCurrent().getGroupReportByStoreIdAndDates(store
          , startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getGroupReportByStoreIdAndDates(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SalesSummary[])GAReportServices.getCurrent().getGroupReportByStoreIdAndDates(store
          , startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

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
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SalesSummary[])GAReportServices.getCurrent().
          getDepartmentSalesReportByStoreIdGroupAndDates(store1, start1, end1, group1);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getDepartmentSalesReportByStoreIdGroupAndDates(String,Date,Date,String,String,Date,Date,String)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SalesSummary[])GAReportServices.getCurrent().
          getDepartmentSalesReportByStoreIdGroupAndDates(store1, start1, end1, group1);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public GaFiscalReportDataTransferObject[] getGaFiscalReportByStoreIdAndDates(String store
      , Date startDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (GaFiscalReportDataTransferObject[])GAReportServices.getCurrent().
          getGaFiscalReportByStoreIdAndDates(store, startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getGaFiscalReportByStoreIdAndDates(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (GaFiscalReportDataTransferObject[])GAReportServices.getCurrent().
          getGaFiscalReportByStoreIdAndDates(store, startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * comment
   * @param store comment
   * @param fiscalDay comment
   */
  public GaTranFiscalReportDataTransferObject[] getTotalSalesByStoreIdHalfAndDates(String storeId
      , Date startDate, Date endDate)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (GaTranFiscalReportDataTransferObject[])GAReportServices.getCurrent().
          getTotalSalesByStoreIdHalfAndDates(storeId, startDate, endDate);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getTotalSalesByStoreIdHalfAndDates(String,String)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (GaTranFiscalReportDataTransferObject[])GAReportServices.getCurrent().
          getTotalSalesByStoreIdHalfAndDates(storeId, startDate, endDate);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

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
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (SalesSummary[])GAReportServices.getCurrent().getItemSalesReportByStoreIdAndDates(
          store1, start1, end1);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getItemSalesReportByStoreIdAndDates(String,Date,Date,String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (SalesSummary[])GAReportServices.getCurrent().getItemSalesReportByStoreIdAndDates(
          store1, start1, end1);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Comments
   * @param storeId comment
   * @param begin comment
   * @param end comment
   */
  public ConsignmentSummaryReportDataTransferObject[] getConsignmentSummaryByCustomer(String
      storeId, Date begin, Date end)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ConsignmentSummaryReportDataTransferObject[])GAReportServices.getCurrent().
          getConsignmentSummaryByCustomer(storeId, begin, end);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getConsignmentSummaryByCustomer(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ConsignmentSummaryReportDataTransferObject[])GAReportServices.getCurrent().
          getConsignmentSummaryByCustomer(storeId, begin, end);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * Consignment Details
   * @param storeId
   * @param begin
   * @param end
   */
  public ConsignmentDetailsReport[] getConsignmentDetails(String storeId, Date begin, Date end)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ConsignmentDetailsReport[])GAReportServices.getCurrent().getConsignmentDetails(
          storeId, begin, end);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getConsignmentDetails(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ConsignmentDetailsReport[])GAReportServices.getCurrent().getConsignmentDetails(
          storeId, begin, end);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  /**
   * Reservation Details
   * @param storeId
   * @param begin
   * @param end
   */
  public ReservationDetailsReport[] getReservationDetails(String storeId, Date begin, Date end)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (ReservationDetailsReport[])GAReportServices.getCurrent().getReservationDetails(
          storeId, begin, end);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getReservationDetails(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (ReservationDetailsReport[])GAReportServices.getCurrent().getReservationDetails(
          storeId, begin, end);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
  
  /**
   * Presale Details
   * @param storeId
   * @param begin
   * @param end
   */
  public PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin, Date end)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (PresaleDetailsReport[])GAReportServices.getCurrent().getPresaleDetails(
          storeId, begin, end);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName()
          , "getPresaleDetails(String,Date,Date)"
          , "Primary Implementation for GAReportServices failed, going Off-Line..."
          , "See Exception", LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (PresaleDetailsReport[])GAReportServices.getCurrent().getPresaleDetails(
          storeId, begin, end);
    } finally {
      this.fireWorkInProgressEvent(false);
    }
  }
}

