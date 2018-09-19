/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.currency.CurrencyException;
import com.ga.cs.dataaccess.artsoracle.dao.TransactionReportOracleDAO;
import com.ga.cs.pos.datatranssferobject.MediaReportDataTransferObject;
import com.ga.cs.pos.datatranssferobject.TransactionReportDataTransferObject;


/**
 * GA specific static convenience methods to manipulate Client Services.
 * @author fbulah
 */
public class POSReportHelper implements POSReportHelperInterface {
  private static POSReportHelper posReportHelper = null;
  protected static boolean DEBUG = true;

  /**
   * POSReportHelper consctuctor is a singleton and therefore its constructor is private
   */
  private POSReportHelper() {
  }

  /**
   * creates singleton instance
   * @return
   */
  public static POSReportHelper getInstance() {
    if (posReportHelper == null) {
      posReportHelper = new POSReportHelper();
    }
    return posReportHelper;
  }

  //	
  //	/**
   //	 * Return the payment summaries for the specified store for on specified date grouped
   //	 * by payment type. The keys in the returned hashtable will be payment type String
   //	 * objects. The values will be MediaReportDataTransferObject objects.
   //	 *
   //	 * @param storeId The store to search for.
   //	 * @param date The date on which to search.
   //	 * @return An array of the payment summaries.
   //	 */
  //	public Hashtable getStorePaymentSummaryTableByPaymentType( IRepositoryManager theAppMgr, String storeId, Date dateStart, Date dateEnd ) throws Exception {
  //
  //		MediaReportOracleDAO mediaReportDAO = new MediaReportOracleDAO();
  //
  //		MediaReportDataTransferObject[] mo = mediaReportDAO.getTotalSalesByStoreIdAndDates( storeId, dateStart, dateEnd );
  //
  //		Hashtable returnTable = new Hashtable();
  //
  //		for (int index = 0 ; index < mo.length ; index++) {
  //			String typeId = mo[index].getTypeId();
  //			mo[index].setCreditReturn( mediaReportDAO.getTotalReturnsByStoreIdAndDatesAndPaymentType( storeId, dateStart, dateEnd, typeId) );
  //			mo[index].setNetAmount();  // the transfer object does the calculation: gross - credit/returns
  //			returnTable.put(typeId, mo[index]);
  //		}
  //		
  //		return returnTable;
  //		
  //	}
  /* (non-Javadoc)
   * @see com.ga.cs.pos.POSReportHelperInterface#getStorePaymentSummaryGrandTotals(java.util.Hashtable)
   */
  public MediaReportDataTransferObject getStorePaymentSummaryGrandTotals(String totalsLabel
      , Hashtable paymentSummaries) {
    MediaReportDataTransferObject totals = new MediaReportDataTransferObject();
    totals.setTypeId(totalsLabel);
    Iterator iter = paymentSummaries.keySet().iterator();
    String errMsgPrefix = "getStorePaymentSummaryGrandTotals: CurrencyException calculating ";
    while (iter.hasNext()) {
      String mediaType = (String)iter.next();
      MediaReportDataTransferObject mo = (MediaReportDataTransferObject)paymentSummaries.get(
          mediaType);
      System.out.println("totals: mediaType=" + mediaType + " mo=" + mo);
      totals.setMediaCount(totals.getMediaCount() + mo.getMediaCount());
      System.out.println("totals: mediaType=" + mediaType + " mo.mediaCount=" + mo.getMediaCount());
      try {
        System.out.println("totals: mediaType=" + mediaType + " mo.grossAmount="
            + mo.getGrossAmount());
        totals.setGrossAmount(totals.getGrossAmount().add(mo.getGrossAmount()));
      } catch (CurrencyException e) {
        System.out.println(errMsgPrefix + "grossAmount on mediaType" + mediaType + " msg="
            + e.getMessage());
        e.printStackTrace();
      }
      try {
        System.out.println("totals: mediaType=" + mediaType + " mo.creditReturn="
            + mo.getCreditReturn());
        totals.setCreditReturn(totals.getCreditReturn().add(mo.getCreditReturn()));
      } catch (CurrencyException e1) {
        System.out.println(errMsgPrefix + "creditReturns on mediaType" + mediaType + " msg="
            + e1.getMessage());
        e1.printStackTrace();
      }
    }
    totals.setNetAmount();
    System.out.println("totals: totals.netAmount=" + totals.getNetAmount());
    return totals;
  }

  /**
   * get the transaction data by store and register
   * the returned hash table will be a 2-level nested hash table: the outer hash table is keyed by register ID
   * whose value is an inner hash table with transaction types as keys:
   *
   *    sales
   *    returns
   *    voids
   *    no-sales
   *    pre-sales out        [DB implementation still in progress as of 04/27/2005]
   *    pre-sales returns    [DB implementation still in progress as of 04/27/2005]
   *    consignment out      [DB implementation still in progress as of 04/27/2005]
   *    consignment returns  [DB implementation still in progress as of 04/27/2005]
   *
   * the value of the inner hash table is a TransactionReportDataTransferObject containing the data
   */
  public Hashtable getTransactionAnalysisDataByStore(IRepositoryManager theAppMgr, String storeId
      , Date dateStart, Date dateEnd)
      throws Exception {
    TransactionReportOracleDAO transDAO = new TransactionReportOracleDAO();
    HashMap data = new HashMap();
    System.out.println("sales"); //TODO: DEBUG
    data.put(SALE_KEY, transDAO.getSalesDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    System.out.println("returns"); //TODO: DEBUG
    data.put(RETURN_KEY, transDAO.getReturnsDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    System.out.println("voids"); //TODO: DEBUG
    data.put(VOID_KEY, transDAO.getVoidsDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    System.out.println("no-sales"); //TODO: DEBUG
    data.put(NO_SALE_KEY, transDAO.getNoSalesDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    System.out.println("pre-sales out"); //TODO: DEBUG
    data.put(PRE_SALE_OUT_KEY
        , transDAO.getPreSalesOutDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    System.out.println("pre-sales return"); //TODO: DEBUG
    data.put(PRE_SALE_RETURN_KEY
        , transDAO.getPreSalesReturnDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    System.out.println("consignment out"); //TODO: DEBUG
    data.put(CONSIGNMENT_OUT_KEY
        , transDAO.getConsignmentOutDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    System.out.println("consignment rerturn"); //TODO: DEBUG
    data.put(CONSIGNMENT_RETURN_KEY
        , transDAO.getConsignmentReturnDataByStoreIdAndDates(storeId, dateStart, dateEnd));
    Hashtable returnData = organizeDataByRegisterIdAndTransactionType(data);
    System.out.println("done"); //TODO: DEBUG
    return returnData;
  }

  /**
   * put your documentation comment here
   * @param data
   * @return
   */
  private Hashtable organizeDataByRegisterIdAndTransactionType(HashMap data) {
    //
    // 1st pass: reorganize data by registeId
    //
    Hashtable byRegisterId = new Hashtable();
    Iterator iter = data.keySet().iterator();
    while (iter.hasNext()) {
      String transType = (String)iter.next();
      System.out.println("organizeDataByRegisterIdAndTransactionType: 1st pass: transType="
          + transType); //TODO: DEBUG
      TransactionReportDataTransferObject[] to = (TransactionReportDataTransferObject[])data.get(
          transType);
      getDataForRegisterId(byRegisterId, transType, to);
    }
    //
    // 2nd pass: make sure that every registerId has data for every transaction type,
    // adding zero values for any missing trans types
    //
    Iterator regIter = byRegisterId.keySet().iterator();
    while (regIter.hasNext()) {
      String registerId = (String)regIter.next();
      Hashtable dataTable;
      if ((dataTable = (Hashtable)byRegisterId.get(registerId)) != null) {
        Iterator transTypeIter = data.keySet().iterator();
        while (transTypeIter.hasNext()) {
          String transType = (String)transTypeIter.next();
          System.out.println("organizeDataByRegisterIdAndTransactionType: 2nd pass: registerId="
              + registerId + "transType=" + transType); //TODO: DEBUG					
          if (dataTable.get(transType) == null) {
            dataTable.put(transType, new TransactionReportDataTransferObject(registerId));
          }
        }
      }
    }
    return byRegisterId;
  }

  /**
   * put your documentation comment here
   * @param byRegisterId
   * @param transType
   * @param to
   */
  private void getDataForRegisterId(Hashtable byRegisterId, String transType
      , TransactionReportDataTransferObject[] to) {
    for (int i = 0; i < to.length; i++) {
      String registerId = to[i].getRegisterId();
      //
      // a null registerId means there is no data for this transaction type; simply return
      // note: zero values will ultimately be filled in for this type for all regsiterIds by caller
      //
      if (registerId == null) {
        return;
      }
      System.out.println("getDataForRegisterId: transType=" + transType + " registerId="
          + registerId); //TODO: DEBUG
      Hashtable dataTable;
      if ((dataTable = (Hashtable)byRegisterId.get(registerId)) == null) {
        dataTable = new Hashtable();
        byRegisterId.put(registerId, dataTable);
      }
      dataTable.put(transType, to[i]);
    }
  }

  /**
   * return transaction analysis data totals for all registers
   * @param taData
   * @return
   */
  public TransactionReportDataTransferObject getTransactionAnalysisTotals(Hashtable taData) {
    TransactionReportDataTransferObject totals = new TransactionReportDataTransferObject();
    Iterator iter = taData.keySet().iterator();
    while (iter.hasNext()) {
      String registerId = (String)iter.next();
      if (DEBUG) {
        System.out.println("getTransactionAnalysisTotals: registerId=" + registerId);
      }
      Hashtable dataByRegister = (Hashtable)taData.get(registerId);
      if (dataByRegister == null) {
        if (DEBUG) {
          System.out.println("getTransactionAnalysisTotals: no data for registerId=" + registerId);
        }
        continue;
      }
      Iterator transTypeIter = dataByRegister.keySet().iterator();
      while (transTypeIter.hasNext()) {
        String transType = (String)transTypeIter.next();
        if (DEBUG) {
          System.out.println("getTransactionAnalysisTotals: transType=" + transType);
        }
        TransactionReportDataTransferObject to = (TransactionReportDataTransferObject)
            dataByRegister.get(transType);
        if (to == null) {
          if (DEBUG) {
            System.out.println("getTransactionAnalysisTotals: no " + transType + " data found");
          }
          continue;
        }
        if (DEBUG) {
          System.out.println("before: totals.transactionCount  =" + totals.getTransactionCount());
          System.out.println("before: totals.untisCount        =" + totals.getUnitsCount());
          System.out.println("before: totals.totalsSales       =" + totals.getTotalSales());
          System.out.println("before: totals.unitsPerTrans     =" + totals.getUnitsPerTransaction());
          System.out.println("before: totals.dollarsPerTrans   =" + totals.getDollarsPerTransaction()
              + "\n");
          System.out.println("before: to.transactionCount      =" + to.getTransactionCount());
          System.out.println("before: to.untisCount            =" + to.getUnitsCount());
          System.out.println("before: to.totalsSales           =" + to.getTotalSales());
          System.out.println("before: to.unitsPerTrans         =" + to.getUnitsPerTransaction());
          System.out.println("before: to.dollarsPerTrans       =" + to.getDollarsPerTransaction()
              + "\n");
        }
        totals.setTransactionCount(totals.getTransactionCount() + to.getTransactionCount());
        totals.setUnitsCount(totals.getUnitsCount() + to.getUnitsCount());
        totals.addToTotalSales(to.getTotalSales());
        totals.setUnitsPerTransaction();
        totals.setDollarsPerTransaction();
        if (DEBUG) {
          System.out.println("after: totals.transactionCount   =" + totals.getTransactionCount());
          System.out.println("after: totals.untisCount         =" + totals.getUnitsCount());
          System.out.println("after: totals.totalsSales        =" + totals.getTotalSales());
          System.out.println("after: totals.unitsPerTrans      =" + totals.getUnitsPerTransaction());
          System.out.println("after: totals.dollarsPerTrans    =" + totals.getDollarsPerTransaction()
              + "\n");
        }
      }
    }
    return totals;
  }

  /**
   * put your documentation comment here
   * @param toList
   * @return
   */
  public TransactionReportDataTransferObject getTransactionAnalysisTotals(Vector toList) {
    TransactionReportDataTransferObject totals = new TransactionReportDataTransferObject();
    for (Iterator iter = toList.iterator(); iter.hasNext(); ) {
      TransactionReportDataTransferObject to = (TransactionReportDataTransferObject)iter.next();
      if (DEBUG) {
        System.out.println("before: totals.transactionCount  =" + totals.getTransactionCount());
        System.out.println("before: totals.untisCount        =" + totals.getUnitsCount());
        System.out.println("before: totals.totalsSales       =" + totals.getTotalSales());
        System.out.println("before: totals.unitsPerTrans     =" + totals.getUnitsPerTransaction());
        System.out.println("before: totals.dollarsPerTrans   =" + totals.getDollarsPerTransaction()
            + "\n");
        System.out.println("before: to.transactionCount      =" + to.getTransactionCount());
        System.out.println("before: to.untisCount            =" + to.getUnitsCount());
        System.out.println("before: to.totalsSales           =" + to.getTotalSales());
        System.out.println("before: to.unitsPerTrans         =" + to.getUnitsPerTransaction());
        System.out.println("before: to.dollarsPerTrans       =" + to.getDollarsPerTransaction()
            + "\n");
      }
      totals.setTransactionCount(totals.getTransactionCount() + to.getTransactionCount());
      totals.setUnitsCount(totals.getUnitsCount() + to.getUnitsCount());
      totals.addToTotalSales(to.getTotalSales());
      totals.setUnitsPerTransaction();
      totals.setDollarsPerTransaction();
      if (DEBUG) {
        System.out.println("after: totals.transactionCount   =" + totals.getTransactionCount());
        System.out.println("after: totals.untisCount         =" + totals.getUnitsCount());
        System.out.println("after: totals.totalsSales        =" + totals.getTotalSales());
        System.out.println("after: totals.unitsPerTrans      =" + totals.getUnitsPerTransaction());
        System.out.println("after: totals.dollarsPerTrans    =" + totals.getDollarsPerTransaction()
            + "\n");
      }
    }
    return totals;
  }
}

