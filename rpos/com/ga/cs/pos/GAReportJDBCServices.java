/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ArrayList;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.*;
import com.ga.cs.currency.ComparableCurrency;
import com.ga.cs.dataaccess.artsoracle.dao.*;
import com.ga.cs.pos.datatranssferobject.*;
import com.chelseasystems.cs.txnposter.CMSSalesSummary;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.chelseasystems.cs.txnposter.ArmSalesSummary;
import com.chelseasystems.cs.pos.CMSEmpSales;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.ga.cs.swing.report.ReportConstants;
import com.ga.cs.swing.report.consolidatedovershort.ConsolidatedOverShortReportApplet;


/**
 * put your documentation comment here
 */
public class GAReportJDBCServices extends GAReportServices {
	 public CurrencyType currencyType ;
  /**
   * For Media Report. Return the payment summaries for the specified store
   * for on specified date grouped by payment type. The keys in the returned
   * hashtable will be payment type String objects. The values will be
   * MediaReportDataTransferObject objects.
   */
  public Hashtable getStorePaymentSummaryTableByPaymentType(String storeId, Date dateStart
      , Date dateEnd)
      throws Exception {
    MediaReportOracleDAO mediaReportDAO = new MediaReportOracleDAO();
    MediaReportDataTransferObject[] mo = mediaReportDAO.getTotalSalesByStoreIdAndDates(storeId
        , dateStart, dateEnd);
    Hashtable table = MediaReportDataTransferObject.groupByTypes(mo);
    if (table.get("MAIL_CHECK") != null) {
      table.remove("MAIL_CHECK");
    }
    return MediaReportDataTransferObject.groupByTypes(mo);
  }

  /**
   * For Consolidate Over/Short Report.
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An HashMap of the payment summaries.
   */
  public HashMap getOverShortSummariesAndTotals(String storeId, Date dateStart, Date dateEnd)
      throws Exception {
    ConsolidatedOverShortReportOracleDAO dao = new ConsolidatedOverShortReportOracleDAO();
    RegisterOracleDAO registerDAO = new RegisterOracleDAO();
    StoreOracleDAO storeOracleDAO = new StoreOracleDAO();
    try {
      String[] registerIds = registerDAO.selectRegisterIdsByStoreId(storeId);
      Store store =  storeOracleDAO.selectById(storeId);
       currencyType = store.getCurrencyType();
      HashMap sosDataByRegisterId = dao.getStartFund(storeId, registerIds, dateStart, dateEnd);
      HashMap eodDataByRegisterId = dao.getEODCounts(storeId, registerIds, dateStart, dateEnd);
      HashMap netSalesByRegisterByTender = dao.getSales(storeId, registerIds, dateStart, dateEnd);
      return createSummaryAndTotals(sosDataByRegisterId, eodDataByRegisterId
          , netSalesByRegisterByTender);
    } catch (SQLException e) {
      System.out.println("GAReportJDBCServices.getOverShortSummariesAndTotals: SQLException: e="
          + e + " msg=" + e.getMessage());
      e.printStackTrace();
      throw e;
    } catch (CurrencyException ce) {
      System.out.println(
          "GAReportJDBCServices.getOverShortSummariesAndTotals: CurrencyException: e=" + ce
          + " msg=" + ce.getMessage());
      ce.printStackTrace();
      throw ce;
    }
  }

  //for Fred's reports -- POSReportHelper
  public Hashtable getTransactionAnalysisDataByStore(String storeId, Date dateStart, Date dateEnd)
      throws Exception {
    return new Hashtable();
  }

  //Associate Sale Reports
  public Hashtable getAssociateSalesSummaryTableByDate(String storeId, Date dateStart, Date dateEnd)
      throws Exception {
    EmpSaleOracleDAO empSaleDAO = new EmpSaleOracleDAO();
    ArrayList alAst = new ArrayList();
    Hashtable returnTable = new Hashtable();
    int AddNetUnits = 0;
    int AddTrnCount = 0;
    int AddAvgCount = 0;
    ArmCurrency AddNetSales = new ArmCurrency(0);
    ArmCurrency AddAvgUnits = new ArmCurrency(0);
    CMSEmpSales[] empSales = empSaleDAO.selectTotalSaleByStoreAndDates(storeId, dateStart, dateEnd);
    //int GrandNetUnits=0;
    for (int index = 0; index < empSales.length; index++) {
      CMSEmployee emp = empSales[index].getConsultantId();
      CMSEmpSales empSale = null;
      if (returnTable.get(emp.getId()) != null) {
        empSale = (CMSEmpSales)returnTable.get(emp.getId());
        int totalNetUnit = empSale.getQuantity().intValue()
            + empSales[index].getQuantity().intValue();
        ArmCurrency currTotalNetAmount = empSale.getNetAmount().add(empSales[index].getNetAmount());
        int txnCnt = empSale.getTxnCount().intValue() + empSales[index].getTxnCount().intValue();
        empSale.setQuantity(new Long(totalNetUnit));
        empSale.setNetAmount(currTotalNetAmount);
        empSale.setTxnCount(new Long(txnCnt));
        } else {
        empSale = empSales[index];
        returnTable.put(emp.getId(), empSale);
      }
    }   
    return returnTable;
  }

  //ClassReportApplet
  public SalesSummary[] getDepartmentReportByStoreIdAndDates(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    VArmItemSaleOracleDAO dao = new VArmItemSaleOracleDAO();
    SalesSummary[] salesSummaries = dao.selectTotalByDept(startDate, endDate, storeId);
    return salesSummaries;
  }

  //  ClassReportApplet
  public SalesSummary[] getDeptClassSalesReportByStoreIdDeptIdAndDates(String storeId
      , Date startDate, Date endDate, String deptId)
      throws Exception {
    VArmItemSaleOracleDAO dao = new VArmItemSaleOracleDAO();
    SalesSummary[] salesSummaries = dao.selectTotalByDeptClass(startDate, endDate, storeId);
    return salesSummaries;
  }

  //DepartmentReportApplet
  public SalesSummary[] getGroupReportByStoreIdAndDates(String storeId, Date startDate
      , Date endDate)
      throws Exception {
    return new SalesSummary[0];
  }

  //DepartmentReportApplet
  public SalesSummary[] getDepartmentSalesReportByStoreIdGroupAndDates(String storeId
      , Date startDate, Date endDate, String grpDiv)
      throws Exception {
    VArmItemSaleOracleDAO dao = new VArmItemSaleOracleDAO();
    SalesSummary[] salesSummaries = dao.selectTotalByDept(startDate, endDate, storeId);
    return salesSummaries;
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
    GaTranFiscalOracleDAO dao = new GaTranFiscalOracleDAO();
    return dao.getFiscalByStoreIdAndDates(storeId, startDate, endDate);
  }

  //GaItemReportApplet
  public SalesSummary[] getItemSalesReportByStoreIdAndDates(String storeId1, Date startDate1
      , Date endDate1)
      throws Exception {
    VArmItemSaleOracleDAO dao = new VArmItemSaleOracleDAO();
    SalesSummary[] salesSummaries = dao.selectTotalByItem(startDate1, endDate1, storeId1);
    /*        ItemSalesReportDataTransferObject[] array = new
     ItemSalesReportDataTransferObject[salesSummaries.length];
     for (int index = 0; index < salesSummaries.length; index++) {
     ArmSalesSummary salesSummary = (ArmSalesSummary) salesSummaries[index];
     ItemSalesReportDataTransferObject ast = new
     ItemSalesReportDataTransferObject();
     ast.setDeptId(salesSummary.getDeptID());
     ast.setDeptDesc(salesSummary.getDeptDesc());
     ast.setItemId(salesSummary.getItemId());
     ast.setItemDesc(salesSummary.getItemDesc());
     ast.setClassDesc(salesSummary.getClassDesc());
     ast.setNetSales(salesSummary.getTotalAmount());
     ast.setNetUnits(salesSummary.getTotalQuantity().intValue());
     array[index] = ast;
     }
     */
    return salesSummaries;
  }

  //Consignment Summary
  public ConsignmentSummaryReportDataTransferObject[] getConsignmentSummaryByCustomer(String
      storeId, Date begin, Date end)
      throws Exception {
    ConsignmentSummaryReportOracleDAO dao = new ConsignmentSummaryReportOracleDAO();
    ConsignmentSummaryReportDataTransferObject[] dto = dao.getConsignmentSummaryByCustomer(storeId
        , begin, end);
    return dto;
  }

  //Consignment Details
  public ConsignmentDetailsReport[] getConsignmentDetails(String storeId, Date begin, Date end)
      throws Exception {
    ConsignmentDetailsReportOracleDAO dao = new ConsignmentDetailsReportOracleDAO();
    ConsignmentDetailsReport[] reports = dao.getConsignmentDetails(storeId, begin, end);
    return reports;
  }

  //Reservation Details
  public ReservationDetailsReport[] getReservationDetails(String storeId, Date begin, Date end)
      throws Exception {
	ReservationDetailsReportOracleDAO dao = new ReservationDetailsReportOracleDAO();
    ReservationDetailsReport[] reports = dao.getReservationDetails(storeId, begin, end);
    return reports;
  }
  
  //Presale Details
  public PresaleDetailsReport[] getPresaleDetails(String storeId, Date begin, Date end)
      throws Exception {	  
	PresaleDetailsReportOracleDAO dao = new PresaleDetailsReportOracleDAO();
	PresaleDetailsReport[] reports = dao.getPresaleDetails(storeId, begin, end);
    return reports;
  }

  /***************************************************************************
   *
   * Private methods for Consolidated O/S reports
   *
   **************************************************************************/
  private static final String[] TENDER_TYPES = new String[] {"CASH", "CHECK", "TRAVELLERS_CHECK"
      , "JPY_CASH" ,"MALL_CERTIFICATE"
  };

  /**
   * put your documentation comment here
   * @param sosDataByRegisterId
   * @param eodDataByRegisterId
   * @param netSalesByRegisterByTender
   * @return
   * @exception CurrencyException
   */
  private HashMap createSummaryAndTotals(HashMap sosDataByRegisterId, HashMap eodDataByRegisterId
      , HashMap netSalesByRegisterByTender)
      throws CurrencyException {
    HashMap summariesAndTotalsByRegisterId = new HashMap();
    Iterator iter = getSortedRegisterIds(sosDataByRegisterId, eodDataByRegisterId
        , netSalesByRegisterByTender);
    while (iter.hasNext()) {
      //for each reg id
      String registerId = (String)iter.next();
      HashMap totals = new HashMap();
      totals.put(ConsolidatedOverShortReportApplet.COUNTED, new ComparableCurrency(0));
      totals.put(ConsolidatedOverShortReportApplet.EXPECTED, new ComparableCurrency(0));
      totals.put(ConsolidatedOverShortReportApplet.OVERSHORT, new ComparableCurrency(0));
      HashMap summaryByTenderType = new HashMap();
      String[] tenderTypes = TENDER_TYPES;
      for (int i = 0; i < tenderTypes.length; i++) {
        String tenderType = tenderTypes[i];
        HashMap summary = new HashMap();
        ArmCurrency counted = getCountedAmount(registerId, tenderType, sosDataByRegisterId
            , eodDataByRegisterId);
        ArmCurrency expected = getExpectedAmount(registerId, tenderType, netSalesByRegisterByTender);
        ArmCurrency overShort = getOverShort(registerId, tenderType, summary, counted, expected);
        addTotals(totals, counted, expected, overShort);
        //add summary followed by totals as a pseudo-type
        summaryByTenderType.put(tenderType, summary);
        summaryByTenderType.put(ConsolidatedOverShortReportApplet.TOTALS, totals);
        summariesAndTotalsByRegisterId.put(registerId, summaryByTenderType);
      }
    }
    return summariesAndTotalsByRegisterId;
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param tenderType
   * @param summary
   * @param counted
   * @param expected
   * @return
   * @exception CurrencyException
   */
  private ArmCurrency getOverShort(String registerId, String tenderType, HashMap summary
      , ArmCurrency counted, ArmCurrency expected)
      throws CurrencyException {
    if (counted == null) {
      counted = new ArmCurrency(currencyType,0);
    }
    if (expected == null) {
      expected = new ArmCurrency(currencyType,0);
    }
    ArmCurrency overShort = counted.subtract(expected);
    summary.put(ConsolidatedOverShortReportApplet.COUNTED, new ComparableCurrency(counted));
    summary.put(ConsolidatedOverShortReportApplet.EXPECTED, new ComparableCurrency(expected));
    summary.put(ConsolidatedOverShortReportApplet.OVERSHORT, new ComparableCurrency(overShort));
    return overShort;
  }

  /**
   * put your documentation comment here
   * @param totals
   * @param counted
   * @param expected
   * @param overShort
   * @exception CurrencyException
   */
  private void addTotals(HashMap totals, ArmCurrency counted, ArmCurrency expected, ArmCurrency overShort)
      throws CurrencyException {
    addTotal(totals, ConsolidatedOverShortReportApplet.COUNTED, counted);
    addTotal(totals, ConsolidatedOverShortReportApplet.EXPECTED, expected);
    addTotal(totals, ConsolidatedOverShortReportApplet.OVERSHORT, overShort);
  }

  /**
   * put your documentation comment here
   * @param totals
   * @param key
   * @param amount
   * @exception CurrencyException
   */
  private void addTotal(HashMap totals, String key, ArmCurrency amount)
      throws CurrencyException {
    if (amount != null) {
	//Anjana:06/23/2017 to support CAD currency for canada store
    	  ArmCurrency current = new ArmCurrency (amount.getCurrencyType(),((ArmCurrency)totals.get(key)).doubleValue());
      current = current.add(amount);
      totals.put(key, ComparableCurrency.toComparableCurrency(current));
    }
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param tenderType
   * @param sosDataByRegisterId
   * @param eodDataByRegisterId
   * @return
   * @exception CurrencyException
   */
  private ArmCurrency getCountedAmount(String registerId, String tenderType
      , HashMap sosDataByRegisterId, HashMap eodDataByRegisterId)
      throws CurrencyException {
    ConsolidatedOverShortReportDataTransferObject co = (
        ConsolidatedOverShortReportDataTransferObject)eodDataByRegisterId.get(registerId);
    ArmCurrency countedAmount = new ArmCurrency(currencyType,0);
    if (co != null) {
      countedAmount = getAmountFromCOByType(co, tenderType);
      if (tenderType.equals(ArtsConstants.PAYMENT_TYPE_CASH)) {
        ArmCurrency startAmount = (ArmCurrency)sosDataByRegisterId.get(registerId);
        ArmCurrency endAmount = (ArmCurrency)co.getTenderTypeValue("ENDING_DRAWER_FUND");
        if (startAmount != null) {
          countedAmount = countedAmount.subtract(startAmount).add(endAmount);
        }
      }
    }
    return countedAmount;
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param tenderType
   * @param netSalesByRegisterByTender
   * @return
   */
  private ArmCurrency getExpectedAmount(String registerId, String tenderType
      , HashMap netSalesByRegisterByTender) {
    ArmCurrency expectedAmount = new ArmCurrency(currencyType,0);
    ConsolidatedOverShortReportDataTransferObject co = (
        ConsolidatedOverShortReportDataTransferObject)netSalesByRegisterByTender.get(registerId);
    if (co != null) {
      expectedAmount = getAmountFromCOByType(co, tenderType);
    }
    return expectedAmount;
  }

  /**
   * put your documentation comment here
   * @param co
   * @param tenderType
   * @return
   */
  private ArmCurrency getAmountFromCOByType(ConsolidatedOverShortReportDataTransferObject co
      , String tenderType) {
    ArmCurrency amount = co.getTenderTypeValue(tenderType);
    if (tenderType.equals(ArtsConstants.PAYMENT_TYPE_CASH)) {
      if (amount == null) {
        // CASH is synonymoous with DOLLARS, so try
        // DOLLARS if not
        // set as CASH. if still not found, set to 0
        amount = co.getTenderTypeValue(ReportConstants.PAYMENT_TYPE_DOLLARS);
        if (amount == null) {
          amount = new ArmCurrency(currencyType,0);
        }
      }
    }
    return amount;
  }

  /**
   * put your documentation comment here
   * @param sosDataByRegisterId
   * @param eodDataByRegisterId
   * @param netSalesByRegisterByTender
   * @return
   */
  private Iterator getSortedRegisterIds(HashMap sosDataByRegisterId, HashMap eodDataByRegisterId
      , HashMap netSalesByRegisterByTender) {
    java.util.TreeSet all = new java.util.TreeSet();
    all.addAll(sosDataByRegisterId.keySet());
    all.addAll(eodDataByRegisterId.keySet());
    all.addAll(netSalesByRegisterByTender.keySet());
    return all.iterator();
  }
}

