/*
 * Created on May 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.ga.cs.pos.datatranssferobject.MediaReportDataTransferObject;
import com.ga.cs.pos.datatranssferobject.TransactionReportDataTransferObject;


/**
 * interface defining the POS report helper
 * @author fbulah
 *
 */
public interface POSReportHelperInterface {
  public static final String SALE_KEY = "SALE";
  public static final String RETURN_KEY = "RETURN";
  public static final String VOID_KEY = "VOID";
  public static final String PRE_SALE_OUT_KEY = "PRE-SALE OUT";
  public static final String PRE_SALE_RETURN_KEY = "PRE-SALE RETURN";
  public static final String CONSIGNMENT_OUT_KEY = "CONSIGNMENT OUT";
  public static final String CONSIGNMENT_RETURN_KEY = "CONSIGNMENT RETURN";
  public static final String NO_SALE_KEY = "NO SALES";
  public static final String TOTALS_KEY = "TOTALS";


  //	/**
   //	 * Return the payment summaries for the specified store for on specified date grouped
   //	 * by payment type. The keys in the returned hashtable will be payment type String
   //	 * objects. The values will be MediaReportDataTransferObject objects.
   //	 *
   //	 * @param storeId The store to search for.
   //	 * @param date The date on which to search.
   //	 * @return An array of the payment summaries.
   //	 */
  //	public abstract Hashtable getStorePaymentSummaryTableByPaymentType(
  //			IRepositoryManager theAppMgr, String storeId, Date dateStart,
  //			Date dateEnd) throws Exception;
  /**
   * Returns the grandtotals for the payment summaries across all payment types
   * @param paymentSummaries
   * @return
   */
  public abstract MediaReportDataTransferObject getStorePaymentSummaryGrandTotals(String
      totalsLabel, Hashtable paymentSummaries);


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
  public abstract Hashtable getTransactionAnalysisDataByStore(IRepositoryManager theAppMgr
      , String storeId, Date dateStart, Date dateEnd)
      throws Exception;


  /**
   * return transaction analysis data totals for all registers
   * @param taData
   * @return
   */
  public abstract TransactionReportDataTransferObject getTransactionAnalysisTotals(Hashtable taData);


  /**
   * return transaction analysis data totals for all registers
   * @param taData
   * @return
   */
  public abstract TransactionReportDataTransferObject getTransactionAnalysisTotals(Vector taData);
}

