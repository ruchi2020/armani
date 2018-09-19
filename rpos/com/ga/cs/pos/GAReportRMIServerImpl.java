/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.rmi.*;
import java.util.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.node.CMSComponent;
import com.ga.cs.pos.datatranssferobject.*;
import com.chelseasystems.cr.txnposter.SalesSummary;


/**
 * This is the server side of the RMI connection used for fetching/submitting
 * information.  This class delgates all method calls to the object referenced
 * by the return value from GAReportServices.getCurrent().
 */
public class GAReportRMIServerImpl extends CMSComponent implements IGAReportRMIServer {

  /**
   * put your documentation comment here
   * @param   Properties props
   */
  public GAReportRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * Sets the current implementation
   */
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure gareport.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    GAReportServices.setCurrent((GAReportServices)obj);
  }

  /**
   * put your documentation comment here
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure gareport.cfg contains a REMOTE_NAME entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * Receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * Used by the DowntimeManager to determine when this object is available.
   * Just because this process is up doesn't mean that the clients can come up.
   * Make sure that the database is available.
   * @return boolean <code>true</code> indicates that this class is available.
   */
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * comment
   * @param storeId comment
   * @param strartDate comment
   * @param endDate comment
   */
  public Hashtable getStorePaymentSummaryTableByPaymentType(String storeId, Date strartDate
      , Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (Hashtable)GAReportServices.getCurrent().getStorePaymentSummaryTableByPaymentType(
          storeId, strartDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getStorePaymentSummaryTableByPaymentType(String,Date,Date)", start);
      decConnection();
    }
  }

  /**
   * For Over/Short reports.
   * @param storeId storeId
   * @param beginDate begin date
   * @param endDate end date
   */
  public HashMap getOverShortSummariesAndTotals(String storeId, Date beginDate, Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (HashMap)GAReportServices.getCurrent().getOverShortSummariesAndTotals(storeId
          , beginDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getOverShortSummariesAndTotals(String,Date,Date)", start);
      decConnection();
    }
  }

  /**
   * comment
   * @param storeId comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getTransactionAnalysisDataByStore(String storeId, Date startDate, Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (Hashtable)GAReportServices.getCurrent().getTransactionAnalysisDataByStore(storeId
          , startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getTransactionAnalysisDataByStore(String,Date,Date)", start);
      decConnection();
    }
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getAssociateSalesSummaryTableByDate(String store, Date startDate, Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (Hashtable)GAReportServices.getCurrent().getAssociateSalesSummaryTableByDate(store
          , startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getAssociateSalesSummaryTableByDate(String,Date,Date)", start);
      decConnection();
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
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (SalesSummary[])GAReportServices.getCurrent().getDepartmentReportByStoreIdAndDates(
          store, startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getDepartmentReportByStoreIdAndDates(String,Date,Date)", start);
      decConnection();
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
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (SalesSummary[])GAReportServices.getCurrent().
          getDeptClassSalesReportByStoreIdDeptIdAndDates(store1, start1, end1, dept1);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance(
          "getDeptClassSalesReportByStoreIdDeptIdAndDates(String,Date,Date,String,String,Date,Date,String)"
          , start);
      decConnection();
    }
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public SalesSummary[] getGroupReportByStoreIdAndDates(String store, Date startDate, Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (SalesSummary[])GAReportServices.getCurrent().getGroupReportByStoreIdAndDates(store
          , startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getGroupReportByStoreIdAndDates(String,Date,Date)", start);
      decConnection();
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
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (SalesSummary[])GAReportServices.getCurrent().
          getDepartmentSalesReportByStoreIdGroupAndDates(store1, start1, end1, group1);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance(
          "getDepartmentSalesReportByStoreIdGroupAndDates(String,Date,Date,String,String,Date,Date,String)"
          , start);
      decConnection();
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
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (GaFiscalReportDataTransferObject[])GAReportServices.getCurrent().
          getGaFiscalReportByStoreIdAndDates(store, startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getGaFiscalReportByStoreIdAndDates(String,Date,Date)", start);
      decConnection();
    }
  }

  /**
   * comment
   * @param store comment
   * @param fiscalDay comment
   */
  public GaTranFiscalReportDataTransferObject[] getTotalSalesByStoreIdHalfAndDates(String store
      , Date startDate, Date endDate)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (GaTranFiscalReportDataTransferObject[])GAReportServices.getCurrent().
          getTotalSalesByStoreIdHalfAndDates(store, startDate, endDate);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getTotalSalesByStoreIdHalfAndDates(String,String)", start);
      decConnection();
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
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (SalesSummary[])GAReportServices.getCurrent().getItemSalesReportByStoreIdAndDates(
          store1, start1, end1);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getItemSalesReportByStoreIdAndDates(String,Date,Date,String,Date,Date)"
          , start);
      decConnection();
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
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (ConsignmentSummaryReportDataTransferObject[])GAReportServices.getCurrent().
          getConsignmentSummaryByCustomer(storeId, begin, end);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getConsignmentSummaryByCustomer(String,Date,Date)", start);
      decConnection();
    }
  }

  /**
   * Consignment Details
   * @param storeId
   * @param begin
   * @param end
   */
  public ConsignmentDetailsReport[] getConsignmentDetails(String storeId, Date begin, Date end)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (ConsignmentDetailsReport[])GAReportServices.getCurrent().getConsignmentDetails(
          storeId, begin, end);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getConsignmentDetails(String,Date,Date)", start);
      decConnection();
    }
  }
  
  /**
   * Reservation Details
   * @param storeId
   * @param begin
   * @param end
   */
  public ReservationDetailsReport[] getReservationDetails(String storeId, Date begin, Date end)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (ReservationDetailsReport[])GAReportServices.getCurrent().getReservationDetails(
          storeId, begin, end);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getReservationDetails(String,Date,Date)", start);
      decConnection();
    }
  }
  
  /**
   * Presale Details
   * @param storeId
   * @param begin
   * @param end
   */
  public PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin, Date end)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable()) {
        throw new ConnectException("Service is not available");
      }
      incConnection();
      return (PresaleDetailsReport[])GAReportServices.getCurrent().getPresaleDetails(
          storeId, begin, end);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getPresaleDetails(String,Date,Date)", start);
      decConnection();
    }
  }
}

