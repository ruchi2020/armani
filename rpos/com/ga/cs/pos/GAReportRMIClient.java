/*
 * @copyright (c) 2005 Retek. All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.rmi.*;
import java.util.*;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.node.*;
import com.ga.cs.pos.datatranssferobject.*;
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.igray.naming.NamingService;


/**
 * The client-side of an RMI connection for fetching/submitting.
 */
public class GAReportRMIClient extends GAReportServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private IGAReportRMIServer gareportServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   */
  public GAReportRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("gareport.cfg");
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
    init();
  }

  /**
   * Get the remote interface from the registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("GAReportRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the gareport.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * Perform lookup of remote server.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    gareportServer = (IGAReportRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.gareportServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
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
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getStorePaymentSummaryTableByPaymentType((String)storeId
            , (Date)strartDate, (Date)endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }

  /**
   * For Over/Short reports.
   * @param storeId storeId
   * @param beginDate begin date
   * @param endDate end date
   */
  public HashMap getOverShortSummariesAndTotals(String storeId, Date beginDate, Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getOverShortSummariesAndTotals((String)storeId, (Date)beginDate
            , (Date)endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }

  /**
   * comment
   * @param storeId comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getTransactionAnalysisDataByStore(String storeId, Date startDate, Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getTransactionAnalysisDataByStore((String)storeId, (Date)startDate
            , (Date)endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public Hashtable getAssociateSalesSummaryTableByDate(String store, Date startDate, Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getAssociateSalesSummaryTableByDate((String)store, (Date)startDate
            , (Date)endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getDepartmentReportByStoreIdAndDates((String)store, (Date)startDate
            , (Date)endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getDeptClassSalesReportByStoreIdDeptIdAndDates((String)store1
            , (Date)start1, (Date)end1, (String)dept1);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }

  /**
   * comment
   * @param store comment
   * @param startDate comment
   * @param endDate comment
   */
  public SalesSummary[] getGroupReportByStoreIdAndDates(String store, Date startDate, Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getGroupReportByStoreIdAndDates((String)store, (Date)startDate
            , (Date)endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getDepartmentSalesReportByStoreIdGroupAndDates((String)store1
            , (Date)start1, (Date)end1, (String)group1);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getGaFiscalReportByStoreIdAndDates((String)store, (Date)startDate
            , (Date)endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }

  /**
   * comment
   * @param store comment
   * @param fiscalDay comment
   */
  public GaTranFiscalReportDataTransferObject[] getTotalSalesByStoreIdHalfAndDates(String store
      , Date startDate, Date endDate)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getTotalSalesByStoreIdHalfAndDates(store, startDate, endDate);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getItemSalesReportByStoreIdAndDates((String)store1, (Date)start1
            , (Date)end1);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getConsignmentSummaryByCustomer((String)storeId, (Date)begin
            , (Date)end);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }

  //Consignment Details
  public ConsignmentDetailsReport[] getConsignmentDetails(String storeId, Date begin, Date end)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getConsignmentDetails((String)storeId, (Date)begin, (Date)end);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }
  
  //Reservation Details
  public ReservationDetailsReport[] getReservationDetails(String storeId, Date begin, Date end)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getReservationDetails((String)storeId, (Date)begin, (Date)end);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }
  
  //Presale Details
  public PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin, Date end)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (gareportServer == null) {
        init();
      }
      try {
        return gareportServer.getPresaleDetails((String)storeId, (Date)begin, (Date)end);
      } catch (ConnectException ce) {
        gareportServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to GAReportServices");
  }
}

