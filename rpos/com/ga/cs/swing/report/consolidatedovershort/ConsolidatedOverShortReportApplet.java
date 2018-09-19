/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.consolidatedovershort;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import com.chelseasystems.cr.appmgr.IReceiptAppManager;
import com.chelseasystems.cr.payment.PaymentMgr;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.currency.ComparableCurrency;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.swing.model.ConsolidatedOverShortReportModel;
import com.ga.cs.swing.report.ArmaniReportPrinter;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.report.ReportConstants;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper;
import com.ga.cs.swing.table.cellrenderer.action.ConsolidatedOverShortReportAllRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.ReportUtils;
import com.ga.cs.utils.Utils;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;


/**
 * GA Consolidated Over/Short report: Display over/short for each register of a
 * store for a specified date range
 *
 * @author fbulah
 *
 */
public class ConsolidatedOverShortReportApplet extends BaseReportApplet {
  public static final String MAIN_TITLE = "Consolidated Over/Short ";
  public static final int COLNUM_CONSOLIDATED_OVER_SHORT_TYPE = 0;
  public static final int COLNUM_CONSOLIDATED_OVER_SHORT_COUNT = 1;
  public static final int COLNUM_GROSS_AMOUNT = 2;
  public static final int COLNUM_CREDIT_RETURN = 3;
  public static final int COLNUM_NET_AMOUNT = 4;
  public static final String REGISTEER_ID = "Register";
  public static final String MEDIA_TYPE = "Media Type";
  public static final String COUNTED = "Counted";
  public static final String EXPECTED = "Expected";
  public static final String OVERSHORT = "Over/Short";
  public static final String SUMMARIES = "summaries";
  public static final String TOTALS = "totals";
  // Issue # 1064
  public static final String GRAND_TOTALS = "grand totals";
  protected String tableFontName;
  protected Font tableFont;
  protected Font tableTotalsRowFont;
  protected int tableFontSize;
  protected int totalsRowNum;
  protected TableSorter sorter;
  public static final String[] COL_HEADINGS = new String[] {REGISTEER_ID, MEDIA_TYPE, COUNTED
      , EXPECTED, OVERSHORT,
  };
  public static final int CONSOLIDATED_OVER_SHORT_REPORT_PAGE_SIZE = 25;
  public static boolean DEBUG = false;

  /**
   *
   */
  public ConsolidatedOverShortReportApplet() {
    ReportUtils.logInfoTsMsg("CONSOLIDATED OVER-SHORT REPORT STARTED");
    model = new ConsolidatedOverShortReportModel(COL_HEADINGS);
    sorter = new TableSorter(model);
    tbl = new JCMSTable(sorter, JCMSTable.VIEW_ROW);
    // Issue # 829
    //sorter.setTableHeader(tbl.getTableHeader());
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    tableTotalsRowFont = new java.awt.Font(tableFontName, Font.BOLD, tableFontSize);
    multiRowCellRenderer = MultipleRowCellRendererHelper.createMultipleRowCellRenderer();
    MultipleRowCellRendererHelper.setAllRowsCellRendererAction(multiRowCellRenderer
        , new ConsolidatedOverShortReportAllRowsRenderAction());
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(MAIN_TITLE);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("OverShort Rpt"));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * method to populate the scrren
   */
  public void populateScreen(Date startDate, Date endDate) {
    String[] tenderTypes = PaymentMgr.getEndSessionPaymentTypes();
    startDate = Utils.getBeginingOfDay(startDate);
    endDate = Utils.getEndOfDay(endDate);
    try {
      ReportUtils.logInfoTsMsg("CONSOLIDATED OVER-SHORT REPORT POPULATE SCREEN");
      model.clear();
      SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      String fullTitle = res.getString(MAIN_TITLE) + " " + theStore.getId() + " "
          + res.getString("For") + " " + sdf.format(startDate) + " - " + sdf.format(endDate);
      model.setFullTitle(fullTitle);
      lblTitle.setText(fullTitle);
      theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, IReceiptAppManager.PREV_CANCEL_BUTTON);
      HashMap summaryAndTotalsByRegisterId = GAReportHelper.getOverShortSummariesAndTotals(
          theAppMgr, theStore.getId(), startDate, endDate);
      //
      printData(summaryAndTotalsByRegisterId);
      //
      // Issue # 1064
      HashMap grandTotals = new HashMap();
      grandTotals.put(ConsolidatedOverShortReportApplet.COUNTED, new ComparableCurrency(0));
      grandTotals.put(ConsolidatedOverShortReportApplet.EXPECTED, new ComparableCurrency(0));
      grandTotals.put(ConsolidatedOverShortReportApplet.OVERSHORT, new ComparableCurrency(0));
      totalsRowNum = 0;
      Iterator registerIdIter = summaryAndTotalsByRegisterId.keySet().iterator();
      while (registerIdIter.hasNext()) {
        String registerId = (String)registerIdIter.next();
        HashMap summary = new HashMap();
        HashMap summaryByTenderType = (HashMap)summaryAndTotalsByRegisterId.get(registerId);
        Iterator iter = summaryByTenderType.keySet().iterator();
        //
        // loop through all tender types skipping totals
        //
        for (int ixTender = 0; ixTender < tenderTypes.length; ixTender++) {
          String tenderTypeKey = tenderTypes[ixTender];
          if (tenderTypeKey.equals("TRAVERLS_CHECK")) {
            tenderTypeKey = "TRAVELLERS_CHECK";
          }
          //Fix for 1421: Mall Certificate Tender doesn't appear on the Consolidated Over/Short Report.
          if (tenderTypeKey.equals("GIFT_CERT") || tenderTypeKey.equals("MALL_CERTIFICATE")){
            tenderTypeKey = "MALL_CERTIFICATE";
          }
          String tenderTypeView = ReportConstants.getPaymentType(tenderTypeKey);
          if (!tenderTypeKey.equals(TOTALS)) {
            int n = populateRowsForRegister(registerId, tenderTypeKey, tenderTypeView
                , summaryByTenderType);
            totalsRowNum += n;
          }
        }
        // add totals row for this register
        MultipleRowCellRendererHelper.addBoldUnderlineCellRendererAction(multiRowCellRenderer
            , totalsRowNum, tableTotalsRowFont);
        populateRowsForRegister("", TOTALS, TOTALS.toUpperCase(), summaryByTenderType);
        totalsRowNum++;
        // Issue # 1064
        summary = (HashMap)summaryByTenderType.get(TOTALS);
        ArmCurrency counted = (ArmCurrency)summary.get(COUNTED);
        ArmCurrency expected = (ArmCurrency)summary.get(EXPECTED);
        ArmCurrency overShort = (ArmCurrency)summary.get(OVERSHORT);
        addTotals(grandTotals, counted, expected, overShort);
      }
      // Issue # 1064
      // Add Grand Total
      System.out.println("I m here");
      HashMap grandTotalByStore = new HashMap();
      grandTotalByStore.put(GRAND_TOTALS, grandTotals);
      MultipleRowCellRendererHelper.addBoldUnderlineCellRendererAction(multiRowCellRenderer
          , totalsRowNum, tableTotalsRowFont);
      populateRowsForRegister("", GRAND_TOTALS, GRAND_TOTALS.toUpperCase(), grandTotalByStore);
      totalsRowNum++;
      System.out.println("I m here 11");
      tbl.repaint();
    } catch (Exception ex) {
      System.out.println("Exception in method-> POPULATESCREEN" + ex + " msg=" + ex.getMessage());
      ex.printStackTrace();
      ReportUtils.logInfoTsMsg(
          "CONSOLIDATED_OVER_SHORT REPORT: Exception in method-> POPULATESCREEN" + ex + " msg="
          + ex.getMessage());
      theAppMgr.showErrorDlg("Report Error. Call support.");
    }
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param tenderTypeKey
   * @param tenderTypeView
   * @param summaryByTenderType
   * @return
   */
  protected int populateRowsForRegister(String registerId, String tenderTypeKey
      , String tenderTypeView, HashMap summaryByTenderType) {
    int rowsAdded = 0;
    Vector v = new Vector();
    v.add(registerId);
    if (summaryByTenderType != null) {
      HashMap summary = (HashMap)summaryByTenderType.get(tenderTypeKey);
      v.add(tenderTypeView);
      if (summary != null) {
        if (DEBUG) {
          Utils.printHashMap(summary, "populateRowsForRegister.summary");
          System.out.println("summary.get(COUNTED).class="
              + summary.get(COUNTED).getClass().getName() + " value=" + summary.get(COUNTED));
        }
        v.add((ComparableCurrency)summary.get(COUNTED));
        v.add((ComparableCurrency)summary.get(EXPECTED));
        v.add((ComparableCurrency)summary.get(OVERSHORT));
        model.addRow(v);
        rowsAdded++;
      } else { // no summary data for this tender type
        v.add("<NO DATA>");
        //	  			v.add("<NO DATA>");
        //				v.add("<NO DATA>");
        model.addRow(v);
        rowsAdded++;
      }
    } else { // no summary data for any tender type
      v.add("<NO DATA>");
      //  			v.add("<NO DATA>");
      //	  		v.add("<NO DATA>");
      model.addRow(v);
      rowsAdded++;
    }
    return rowsAdded;
  }

  /**
   * @param summaries
   */
  public static void printData(HashMap summariesAndTotalsByRegisterId) {
    Iterator registerIdIter = summariesAndTotalsByRegisterId.keySet().iterator();
    while (registerIdIter.hasNext()) {
      String registerId = (String)registerIdIter.next();
      HashMap summaryByTenderType = (HashMap)summariesAndTotalsByRegisterId.get(registerId);
      Iterator tenderTypeIter = summaryByTenderType.keySet().iterator();
      while (tenderTypeIter.hasNext()) {
        String tenderType = (String)tenderTypeIter.next();
        HashMap summary = (HashMap)summaryByTenderType.get(tenderType);
        System.out.println(" ConsolidatedOverShortReport.printData: registerId    =" + registerId);
        System.out.println(" ConsolidatedOverShortReport.printData: tenderType    =" + tenderType);
        System.out.println(" ConsolidatedOverShortReport.printData: counted       ="
            + summary.get(COUNTED));
        System.out.println(" ConsolidatedOverShortReport.printData: expected      ="
            + summary.get(EXPECTED));
        System.out.println(" ConsolidatedOverShortReport.printData: overShort     ="
            + summary.get(OVERSHORT));
        System.out.println(
            "===================================================================================\n\n");
        ReportUtils.logInfoTsMsg(" CONSOLIDATED OVER SHORT REPORT: registerId    =" + registerId);
        ReportUtils.logInfoTsMsg(" CONSOLIDATED OVER SHORT REPORT: tenderType    =" + tenderType);
        ReportUtils.logInfoTsMsg(" CONSOLIDATED OVER SHORT REPORT: counted       ="
            + summary.get(COUNTED));
        ReportUtils.logInfoTsMsg(" CONSOLIDATED OVER SHORT REPORT: expected      ="
            + summary.get(EXPECTED));
        ReportUtils.logInfoTsMsg(" CONSOLIDATED OVER SHORT REPORT: overShort     ="
            + summary.get(OVERSHORT));
        ReportUtils.logInfoTsMsg(
            "===================================================================================\n\n");
      }
    }
  }

  //Methods for printing to printer
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PRINT")) {
      if (model.getRowCount() == 0) {
        theAppMgr.showErrorDlg(res.getString("There is no data to print."));
        return;
      }
      ArmaniReportPrinter reportPrinter = new ArmaniReportPrinter(this);
      reportPrinter.setPortrait();
      reportPrinter.print();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double[] getReportColumnWidths() {
    double[] reportColumnWidths = new double[this.model.getColumnCount()];
    if (reportColumnWidths.length == 5) {
      reportColumnWidths[0] = 0.10;
      reportColumnWidths[1] = 0.30;
      reportColumnWidths[2] = 0.20;
      reportColumnWidths[3] = 0.20;
      reportColumnWidths[4] = 0.20;
    } else {
      for (int i = 0; i < reportColumnWidths.length; i++) {
        reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
      }
    }
    return reportColumnWidths;
  }

  // Issue # 1064
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
	  //Anjana : 06/23/2017 for cad currency connversion of canda stores
	  amount = new ArmCurrency(amount.doubleValue());
    if (amount != null) {
      ArmCurrency current = new ArmCurrency(((ArmCurrency)totals.get(key)).doubleValue());
      current = current.add(amount);
      totals.put(key, ComparableCurrency.toComparableCurrency(current));
    }
  }
}

