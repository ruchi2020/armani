/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.BaseOracleDAO;
import com.ga.cs.dataaccess.artsoracle.databean.TransactionReportBean;
import com.ga.cs.pos.datatranssferobject.TransactionReportDataTransferObject;


/**
 * DAO for GA Transaction Report
 * @author fbulah
 *
 */
public class TransactionReportOracleDAO extends BaseOracleDAO {
  public static String REGISTER_ID = "REGISTER_ID";
  public static String TRANSACTION_COUNT = "CT_TRANS";
  public static String UNITS_COUNT = "CT_UNITS";
  public static String TOTAL_SALES = "TOTAL_SALES";
  public static String SALE_GUI_TRANS_TYPE = "SALE";
  public static String RETURN_GUI_TRANS_TYPE = "RETN";
  public static String NUMBER_FORMAT = "'99999999D99'";

  /**
   * Retrieves sales transaction count, units count, and total sales by store grouped by register for a date range
   */
  public TransactionReportDataTransferObject[] getSalesDataByStoreIdAndDates(String storeId
      , Date begin, Date end)
      throws SQLException {
    return getDataByStoreIdAndGUITransTypeAndDates(storeId, SALE_GUI_TRANS_TYPE
        , ArtsConstants.POS_LINE_ITEM_TYPE_SALE, begin, end);
  }

  /**
   * Retrieves returns transaction count, units count, and total sales by store grouped by register for a date range
   */
  public TransactionReportDataTransferObject[] getReturnsDataByStoreIdAndDates(String storeId
      , Date begin, Date end)
      throws SQLException {
    return getDataByStoreIdAndGUITransTypeAndDates(storeId, RETURN_GUI_TRANS_TYPE
        , ArtsConstants.POS_LINE_ITEM_TYPE_SALE, begin, end);
  }

  /**
   * Retrieves transaction count, units count, and total sales by store and gui trans type grouped by register for a date range
   */
  public TransactionReportDataTransferObject[] getDataByStoreIdAndGUITransTypeAndDates(String
      storeId, String typeGUITran, Long posLineItemType, Date begin, Date end)
      throws SQLException {
    ////	  DEBUG: PROD TEST
    ConfigMgr jdbcConfig = new ConfigMgr("jdbc.cfg");
    String db = jdbcConfig.getString("DATABASE");
    String user = jdbcConfig.getString("USER_NAME");
    String url = jdbcConfig.getString("URL");
    System.out.println(
        "TransactionReportOracleDAO.getDataByStoreIdAndGUITransTypeAndDates: jdbc.db=" + db
        + " jdbc.user=" + user + " jdbc.url=" + url);
    String sql = "SELECT R.REGISTER_ID,  " + " COUNT( X.AI_TRN )     " + TRANSACTION_COUNT + ",  "
        + " SUM( L.QUANTITY )     " + UNITS_COUNT + ",  "
        + " SUM( TO_NUMBER( SUBSTR( L.ITM_SEL_PRICE, 4 ), " + NUMBER_FORMAT + " ) ) " + TOTAL_SALES
        + " " + " FROM   TR_LTM_RTL_TRN L, TR_RTL R, TR_TRN X "
        + " WHERE  L.AI_TRN           = R.AI_TRN " + " AND    L.AI_TRN           = X.AI_TRN "
        + " AND    R.AI_TRN           = X.AI_TRN "
        + " AND    (L.FL_VD_LN_ITM IS NULL OR (L.FL_VD_LN_ITM = 0)) "
        + " AND    X.ID_STR_RT        = ? " + " AND    L.POS_LN_ITM_TY_ID = ? "
        + " AND    X.ID_VOID IS NULL " + " AND    X.TS_TRN_PRC      >= ? "
        + " AND    X.TS_TRN_PRC      <= ? " + " AND    X.TY_GUI_TRN       = ? "
        + " GROUP BY R.REGISTER_ID " + " ORDER BY R.REGISTER_ID";
    List params = new ArrayList();
    params.add(storeId);
    params.add(posLineItemType);
    params.add(begin);
    params.add(end);
    params.add(typeGUITran);
    TransactionReportBean beans[] = (TransactionReportBean[])query(new TransactionReportBean(
        REGISTER_ID, TRANSACTION_COUNT, UNITS_COUNT, TOTAL_SALES), sql, params);
    return fromBeansToObjects(beans);
  }

  /**
   * Retrieves void transaction count, units count, and total sales by store grouped by register for a date range
   */
  public TransactionReportDataTransferObject[] getVoidsDataByStoreIdAndDates(String storeId
      , Date begin, Date end)
      throws SQLException {
    String sql = "SELECT R.REGISTER_ID,  " + " COUNT( X.AI_TRN )     " + TRANSACTION_COUNT + ", "
        + " SUM( L.QUANTITY )     " + UNITS_COUNT + ", "
        + " SUM( TO_NUMBER( SUBSTR( L.ITM_SEL_PRICE, 4 ), " + NUMBER_FORMAT + " ) ) " + TOTAL_SALES
        + " " + " FROM   TR_LTM_RTL_TRN L, TR_RTL R, TR_TRN X "
        + " WHERE  L.AI_TRN           = R.AI_TRN " + " AND    L.AI_TRN           = X.AI_TRN "
        + " AND    R.AI_TRN           = X.AI_TRN "
        + " AND    (L.FL_VD_LN_ITM IS NOT NULL AND (L.FL_VD_LN_ITM != 0)) "
        + " AND    X.ID_VOID IS NOT NULL " + " AND    X.ID_STR_RT        = ? "
        + " AND    X.TS_TRN_PRC      >= ? " + " AND    X.TS_TRN_PRC      <= ? "
        + " GROUP BY R.REGISTER_ID " + " ORDER BY R.REGISTER_ID";
    List params = new ArrayList();
    params.add(storeId);
    params.add(begin);
    params.add(end);
    TransactionReportBean beans[] = (TransactionReportBean[])query(new TransactionReportBean(
        REGISTER_ID, TRANSACTION_COUNT, UNITS_COUNT, TOTAL_SALES), sql, params);
    return fromBeansToObjects(beans);
  }

  /**
   * Retrieves no-sale transaction count, units count, and total sales by store grouped by register for a date range
   */
  public TransactionReportDataTransferObject[] getNoSalesDataByStoreIdAndDates(String storeId
      , Date begin, Date end)
      throws SQLException {
    String sql = "SELECT R.REGISTER_ID,   " + " COUNT( X.AI_TRN )      " + TRANSACTION_COUNT + ", "
        + " SUM( L.QUANTITY )      " + UNITS_COUNT + ", "
        + " SUM( TO_NUMBER( SUBSTR( L.ITM_SEL_PRICE, 4 ), " + NUMBER_FORMAT + " ) ) " + TOTAL_SALES
        + " " + " FROM   TR_LTM_RTL_TRN L, TR_RTL R, TR_TRN X "
        + " WHERE  L.AI_TRN           = R.AI_TRN " + " AND    L.AI_TRN           = X.AI_TRN "
        + " AND    R.AI_TRN           = X.AI_TRN "
        + " AND    (L.FL_VD_LN_ITM IS NULL OR (L.FL_VD_LN_ITM = 0)) "
        + " AND    X.ID_VOID IS NULL " + " AND    X.ID_STR_RT        = ? "
        + " AND    X.TY_TRN           = ? " + " AND    X.TS_TRN_PRC      >= ? "
        + " AND    X.TS_TRN_PRC      <= ? " + " GROUP BY R.REGISTER_ID "
        + " ORDER BY R.REGISTER_ID";
    List params = new ArrayList();
    params.add(storeId);
    params.add(ArtsConstants.TRANSACTION_TYPE_NO_SALE);
    params.add(begin);
    params.add(end);
    TransactionReportBean beans[] = (TransactionReportBean[])query(new TransactionReportBean(
        REGISTER_ID, TRANSACTION_COUNT, UNITS_COUNT, TOTAL_SALES), sql, params);
    return fromBeansToObjects(beans);
  }

  /**
   * return pre-sales out data by store and date range
   * still TDB; returns zero-valued transfer object for now
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @throws SQLException
   */
  public TransactionReportDataTransferObject[] getPreSalesOutDataByStoreIdAndDates(String storeId
      , Date begin, Date end)
      throws SQLException {
    return createZeroedTransactionReportDataTransferObjectList();
  }

  /**
   * return pre-sales return data by store and date range
   * still TDB; returns zero-valued transfer object for now
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @throws SQLException
   */
  public TransactionReportDataTransferObject[] getPreSalesReturnDataByStoreIdAndDates(String
      storeId, Date begin, Date end)
      throws SQLException {
    return createZeroedTransactionReportDataTransferObjectList();
  }

  /**
   * return consignment out data by store and date range
   * still TDB; returns zero-valued transfer object for now
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @throws SQLException
   */
  public TransactionReportDataTransferObject[] getConsignmentOutDataByStoreIdAndDates(String
      storeId, Date begin, Date end)
      throws SQLException {
    return createZeroedTransactionReportDataTransferObjectList();
  }

  /**
   * return consignment return data by store and date range
   * still TDB; returns zero-valued transfer object for now
   * @param storeId
   * @param begin
   * @param end
   * @return
   * @throws SQLException
   */
  public TransactionReportDataTransferObject[] getConsignmentReturnDataByStoreIdAndDates(String
      storeId, Date begin, Date end)
      throws SQLException {
    return createZeroedTransactionReportDataTransferObjectList();
  }

  /**
   * temporary method to return zeroed TransactionReportDataTransferObject
   * this will be removed once real implementations of pre-sales and consignments are done
   * @return
   */
  public TransactionReportDataTransferObject[] createZeroedTransactionReportDataTransferObjectList() {
    TransactionReportDataTransferObject[] to = new TransactionReportDataTransferObject[1];
    to[0] = new TransactionReportDataTransferObject();
    return to;
  }

  /**
   * load transaction report data transfer objects from bean
   * skips null registerd IDs, and initializes totalSales to zero if not set
   * note: total sales is null when no data for a register and transaction type exists
   * @param beans
   * @return
   * @throws SQLException
   */
  private TransactionReportDataTransferObject[] fromBeansToObjects(TransactionReportBean[] beans)
      throws SQLException {
    TransactionReportDataTransferObject[] array = new TransactionReportDataTransferObject[beans.
        length];
    for (int i = 0; i < array.length; i++) {
      TransactionReportDataTransferObject to = new TransactionReportDataTransferObject();
      if (beans[i].getRegisterId() != null) {
        to.setRegisterId(beans[i].getRegisterId());
        to.setTransactionCount(beans[i].getTransactionCount());
        to.setUnitsCount(beans[i].getUnitsCount());
        ArmCurrency totalSales = beans[i].getTotalSales();
        if (totalSales == null) {
          totalSales = new ArmCurrency(0);
        }
        to.setTotalSales(totalSales);
        to.setUnitsPerTransaction(); // calculates unitsCount/transactionCount
        to.setDollarsPerTransaction(); // calculates totalSales/transactionCount
        array[i] = to;
      }
    }
    return array;
  }
}

