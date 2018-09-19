/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.transactionanalysis;

import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.currency.ComparableCurrency;
import com.ga.cs.pos.POSReportHelperInterface;
import com.ga.cs.pos.datatranssferobject.TransactionReportDataTransferObject;
import com.ga.cs.swing.model.TransactionReportModel;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.table.cellrenderer.DoubleValueCellRenderer;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper;
import com.ga.cs.swing.table.cellrenderer.action.ConstantStringValueRenderAction;
import com.ga.cs.swing.table.cellrenderer.action.TransactionReportDataRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.ReportUtils;


/**
 * GA Transaction report: Display trnsaction data by store and register id for a specified date range
 * Loosely based on the Retek RPOS Transaction Summary Report
 * @author fbulah
 *
 */
public class TransactionReportApplet extends BaseReportApplet {
  protected String tableFontName;
  protected Font tableFont;
  protected Font tableTotalsRowFont;
  protected int tableFontSize;
  protected int totalsRowNum;
  protected TableSorter sorter;
  protected static final String TITLE = "Transaction Analysis Store :";
  protected static final String TOTAL = "TOTAL";
  protected static final String TOTALS = "TOTALS";
  public static final String[] COL_HEADINGS = {"Register", "Transaction Type", "Transactions"
      , "Units", "Sale Amount", "Units Per Trans", "Dollars Per Trans"
  };
  public static final int COLNUM_REGISTERID = 0;
  public static final int COLNUM_TRANSACTION_TYPE = 1;
  public static final int COLNUM_TRANSACTION_COUNT = 2;
  public static final int COLNUM_UNITS_COUNT = 3;
  public static final int COLNUM_SALE_AMOUNT = 4;
  public static final int COLNUM_UNITS_PER_TRANSACTION = 5;
  public static final int COLNUM_DOLLARS_PER_TRANSACTION = 6;
  public static final int NUM_REPORT_COLUMNS = COL_HEADINGS.length;
  public static final int THIN_UNDERLINE_THICKNESS = 2;
  public static final int TRANSACTION_REPORT_PAGE_SIZE = 50;
  public static final String[] TRANSACTION_TYPE_KEYS = {POSReportHelperInterface.SALE_KEY
      , POSReportHelperInterface.RETURN_KEY, POSReportHelperInterface.VOID_KEY
      , POSReportHelperInterface.PRE_SALE_OUT_KEY, POSReportHelperInterface.PRE_SALE_RETURN_KEY
      , POSReportHelperInterface.CONSIGNMENT_OUT_KEY
      , POSReportHelperInterface.CONSIGNMENT_RETURN_KEY, POSReportHelperInterface.NO_SALE_KEY
  };
  protected Vector BLANK_ROW = new Vector();
  protected static final Integer INTEGER_ZERO = new Integer(0);
  protected static final Double DOUBLE_ZERO = new Double(0.0);
  protected static final ArmCurrency CURRENCY_ZERO = new ArmCurrency(0);
  protected static final String EMPTY_STRING = "";
  protected final int TRANS_TYPE_WIDTH_MULT = 2;
  protected final DecimalFormat df = new DecimalFormat("######.##");
  protected final DefaultTableCellRenderer doubleValueCellRenderer = new DoubleValueCellRenderer(df);
  public static final boolean DEBUG = false;

  /**
   *
   */
  public TransactionReportApplet() {
    ReportUtils.logInfoTsMsg("TRANSACTION REPORT: STARTED");
    model = new TransactionReportModel(COL_HEADINGS);
    //		model.setRowsShown(TRANSACTION_REPORT_PAGE_SIZE);
    sorter = new TableSorter(model);
    tbl = new JCMSTable(sorter, JCMSTable.VIEW_ROW);
    sorter.setTableHeader(tbl.getTableHeader());
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    tableTotalsRowFont = new java.awt.Font(tableFontName, Font.BOLD, tableFontSize);
    //
    // The multiRowCellRenderer sets the cell renderer on a per row basis. It can be used to render any
    // set of rows or columns, and it particularly useful for rows that require multiple properties set,
    // e.g. bold, underline, italic; total rows fall into this category
    // It is set as the default renderer for all rows and uses the allRowsRenderAction as the default
    // action for all rows.  RenderActions are added dynamically for the specific rows
    // rerquing different renderings.
    //
    // NOTE:
    // Setting the default cell renderer does not guarantee that the renderer will be used for every column.
    // Base classes like Integer and Double have their own renderers, and there is an implicit
    // search performed during the render process for an appropriate renderer. It appears that
    // the system default renderer is often found in the search path before any user-defined
    // renderer.  What this means is that setting the renderer for the Object.class as a
    // catch-all - as in for every row and column - is not assured to work.
    // For that reason, the setCellRenderer() method is used which extracts the columns from
    // the table's TableColumnModel and then forcibly sets the renderer for that column.
    //
    multiRowCellRenderer = MultipleRowCellRendererHelper.createMultipleRowCellRenderer();
    multiRowCellRenderer.setAllRowsRenderAction(new TransactionReportDataRowsRenderAction());
    MultipleRowCellRendererHelper.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_REGISTERID, SwingConstants.CENTER);
    MultipleRowCellRendererHelper.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_TRANSACTION_TYPE, SwingConstants.LEFT);
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(TITLE);
    initializeBlankRow(); // initalize a blank row used for spacing
    //
    // widen the transation type column
    //
    int width = tbl.getColumnModel().getColumn(COLNUM_TRANSACTION_TYPE).getWidth();
    tbl.getColumnModel().getColumn(COLNUM_TRANSACTION_TYPE).setPreferredWidth(width
        * TRANS_TYPE_WIDTH_MULT);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("Tran Rpt"));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * put your documentation comment here
   * @param startDate
   * @param endDate
   */
  public void populateScreenTEST(Date startDate, Date endDate) {
    ReportUtils.logInfoTsMsg("TRANSACTION REPORT: BEGIN POPULATE SCREEN TEST");
    Vector row = new Vector();
    row.add(COLNUM_REGISTERID, "REGISTER ID");
    row.add(COLNUM_TRANSACTION_TYPE, "TRANS TYPE");
    row.add(COLNUM_TRANSACTION_COUNT, new Integer(1));
    row.add(COLNUM_UNITS_COUNT, new Integer(1));
    row.add(COLNUM_SALE_AMOUNT, new ComparableCurrency(1));
    row.add(COLNUM_UNITS_PER_TRANSACTION, new Double(1));
    row.add(COLNUM_DOLLARS_PER_TRANSACTION, new ComparableCurrency(1));
    model.addRow(row);
    tbl.repaint();
    ReportUtils.logInfoTsMsg("TRANSACTION REPORT: END POPULATE SCREEN TEST");
  }

  /**
   * method to populate the scrren
   */
  public void populateScreen(Date startDate, Date endDate) {
    ReportUtils.logInfoTsMsg("TRANSACTION REPORT: POPULATE SCREEN");
    int rowNum = 0;
    try {
      Vector summariesList = new Vector();
      model.clear();
      SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      String fullTitle = res.getString(TITLE) + " " + theStore.getId() + " " + res.getString("For")
          + " " + sdf.format(startDate) + " - " + sdf.format(endDate);
      model.setFullTitle(fullTitle);
      lblTitle.setText(fullTitle);
      theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
      Hashtable summaries = posReportHelper.getTransactionAnalysisDataByStore(theAppMgr
          , theStore.getId(), startDate, endDate);
      TreeMap sortedSummaries = new TreeMap(summaries);
      Set registerIds = sortedSummaries.keySet();
      Iterator iter = registerIds.iterator();
      Vector colHeaders = new Vector(Arrays.asList(COL_HEADINGS));
      while (iter.hasNext()) {
        String registerId = (String)iter.next();
        Hashtable dataTable = (Hashtable)summaries.get(registerId);
        if (dataTable == null) {
          continue;
        }
        //
        // insert data row for each transaction type for this register
        //
        for (Iterator transTypeIter = dataTable.keySet().iterator(); transTypeIter.hasNext(); ) {
          String transType = (String)transTypeIter.next();
          TransactionReportDataTransferObject to = (TransactionReportDataTransferObject)dataTable.
              get(transType);
          model.addRow(populateRow(registerId, transType, to));
          registerId = EMPTY_STRING; // display registerID on first line of the group only
          rowNum++;
        }
        // add totals row for this register with bold highlighting
        TransactionReportDataTransferObject to = posReportHelper.getTransactionAnalysisTotals(
            summaries);
        summariesList.add(to);
        model.addRow(populateRow(EMPTY_STRING, TOTAL, to));
        MultipleRowCellRendererHelper.addBoldCellRendererAction(multiRowCellRenderer, rowNum
            , tableTotalsRowFont);
        rowNum++;
        // add blank row for spacing
        model.addRow(BLANK_ROW);
        multiRowCellRenderer.addRowRenderAction(rowNum, new ConstantStringValueRenderAction());
        rowNum++;
        ReportUtils.logInfoTsMsg("TRANSACTION REPORT: POPULATE SCREEN; rowNum=" + rowNum);
      }
      ReportUtils.logInfoTsMsg("TRANSACTION REPORT: ADDING TOTALS: rowNum=" + rowNum);
      // add grand totals row with bold highlighting
      model.addRow(populateRow(TOTALS, EMPTY_STRING
          , posReportHelper.getTransactionAnalysisTotals(summariesList)));
      MultipleRowCellRendererHelper.addBoldCellRendererAction(multiRowCellRenderer, rowNum
          , tableTotalsRowFont);
      tbl.repaint();
      ReportUtils.logInfoTsMsg("TRANSACTION REPORT: REPAINT COMPLETE: RETURN AFTER rowNum="
          + rowNum);
    } catch (Exception ex) {
      System.out.println("Exception in method TransactionReportApplet.populateScreen: e=" + ex
          + " msg=" + ex.getMessage() + " rowNum=" + rowNum);
      ReportUtils.logInfoTsMsg(
          "TRANSACTION REPORT: PException in methodTransactionReportApplet.populateScreen: e=" + ex
          + " msg=" + ex.getMessage() + " rowNum=" + rowNum);
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param registerId
   * @param transType
   * @param to
   * @return
   */
  protected Vector populateRow(String registerId, String transType
      , TransactionReportDataTransferObject to) {
    Vector row = new Vector();
    if (DEBUG) {
      System.out.println("TransactionReportApplet.populateRow: transType=" + transType
          + " registerId=" + registerId);
    }
    ArmCurrency totalSales = to.getTotalSales();
    row.add(COLNUM_REGISTERID, registerId);
    row.add(COLNUM_TRANSACTION_TYPE, transType);
    row.add(COLNUM_TRANSACTION_COUNT, new Integer(to.getTransactionCount()));
    row.add(COLNUM_UNITS_COUNT, new Integer(to.getUnitsCount()));
    row.add(COLNUM_SALE_AMOUNT, totalSales);
    row.add(COLNUM_UNITS_PER_TRANSACTION, to.getUnitsPerTransaction());
    row.add(COLNUM_DOLLARS_PER_TRANSACTION, to.getDollarsPerTransaction());
    return row;
  }

  /**
   * initalize a vector containing a blank row used for spacing
   */
  protected void initializeBlankRow() {
    BLANK_ROW.add(COLNUM_REGISTERID, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_TRANSACTION_TYPE, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_TRANSACTION_COUNT, INTEGER_ZERO);
    BLANK_ROW.add(COLNUM_UNITS_COUNT, INTEGER_ZERO);
    BLANK_ROW.add(COLNUM_SALE_AMOUNT, CURRENCY_ZERO);
    BLANK_ROW.add(COLNUM_UNITS_PER_TRANSACTION, DOUBLE_ZERO);
    BLANK_ROW.add(COLNUM_DOLLARS_PER_TRANSACTION, CURRENCY_ZERO);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double[] getReportColumnWidths() {
    double[] reportColumnWidths = new double[this.model.getColumnCount()];
    if (reportColumnWidths.length == 7) {
      reportColumnWidths[0] = 0.10;
      reportColumnWidths[1] = 0.20;
      reportColumnWidths[2] = 0.10;
      reportColumnWidths[3] = 0.10;
      reportColumnWidths[4] = 0.20;
      reportColumnWidths[5] = 0.10;
      reportColumnWidths[6] = 0.20;
    } else {
      for (int i = 0; i < reportColumnWidths.length; i++) {
        reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
      }
    }
    return reportColumnWidths;
  }
}

