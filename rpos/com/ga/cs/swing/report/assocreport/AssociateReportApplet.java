/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.assocreport;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.pos.datatranssferobject.AssociateSalesReportDataTransferObject;
import com.ga.cs.swing.model.AssocSalesReportModel;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper2;
import com.ga.cs.swing.table.cellrenderer.action.AssocSalesAllRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.ReportUtils;
import com.ga.cs.utils.Utils;
import com.ga.cs.swing.report.ArmaniReportPrinter;
import com.chelseasystems.cs.pos.CMSEmpSales;


/**
 * GA Sales By Associate report: Display sales by associate for a specified date
 * range
 *
 *
 */
public class AssociateReportApplet extends BaseReportApplet {
  public static final String MAIN_TITLE = "Sales By Associate";
  public static final int COLNUM_ID = 0;
  public static final int COLNUM_NAME = 1;
  public static final int COLNUM_TRAN = 2;
  public static final int COLNUM_NETUN = 3;
  public static final int COLNUM_NETAM = 4;
  public static final int COLNUM_AV_NETUN = 5;
  public static final int COLNUM_AV_NETAM = 6;
  final boolean off = false;
  final boolean on = true;
  final Color UNDERLINE_COLOR_SUB = Color.lightGray;
  final int UNDERLINE_THICKNESS_SUB = 1;
  Color underlineColor_sub = UNDERLINE_COLOR_SUB;
  int underlineThickness_sub = UNDERLINE_THICKNESS_SUB;
  final Color UNDERLINE_COLOR_GRAND = Color.darkGray;
  final int UNDERLINE_THICKNESS_GRAND = 4;
  protected int OFF = JTable.AUTO_RESIZE_OFF;
  Color underlineColor_grand = UNDERLINE_COLOR_GRAND;
  int underlineThickness_grand = UNDERLINE_THICKNESS_GRAND;
  protected String tableFontName;
  protected Font tableFont;
  protected Font tableTotalsRowFont;
  protected int tableFontSize;
  protected int totalsRowNum;
  protected TableSorter sorter;
  public static final int THIN_UNDERLINE_THICKNESS = 1;
  protected Vector BLANK_ROW = new Vector();
  protected static final Integer INTEGER_ZERO = new Integer(0);
  protected static final Double DOUBLE_ZERO = new Double(0.0);
  protected static final ArmCurrency CURRENCY_ZERO = new ArmCurrency(0);
  protected static final String EMPTY_STRING = "";
  protected final int WIDTH_MULT = 2;
  protected final int WIDTH_MULT_2 = 1;
  public static final String[] ASSOC_REPORT_COL_HEADINGS = new String[] {"AssocName", "AssocID"
      , "TrnCnt", "SalesQty", "SalesAmt", "AvQ", "AvgSale"
  };
  public static final int ASSOC_REPORT_PAGE_SIZE = 25;
  public static final String GRAND_TOTAL_LABEL = "Grand Total:";
  public static boolean DEBUG = false;

  /**
   * put your documentation comment here
   */
  public AssociateReportApplet() {
    ReportUtils.logInfoTsMsg("ASSOCIATE SALES REPORT STARTED");
    model = new AssocSalesReportModel(ASSOC_REPORT_COL_HEADINGS);
    sorter = new TableSorter(model);
    tbl = new JCMSTable(sorter, JCMSTable.VIEW_ROW);
    // Issue # 829
    //sorter.setTableHeader(tbl.getTableHeader());
    //sorter.setSortingStatus(COLNUM_NETAM,-1);
    Font smfont = new Font("Century", 1, 10);
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    tableTotalsRowFont = new java.awt.Font(tableFontName, Font.BOLD, tableFontSize);
    multiRowCellRenderer = MultipleRowCellRendererHelper2.createMultipleRowCellRenderer();
    MultipleRowCellRendererHelper2.setAllRowsCellRendererAction(multiRowCellRenderer
        , new AssocSalesAllRowsRenderAction());
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NAME, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_ID, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_TRAN, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_AV_NETUN, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_AV_NETAM, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NETAM, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NETUN, SwingConstants.CENTER);
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(MAIN_TITLE);
    initializeBlankRow();
    initializeFontChange(smfont);
    //setFont(jfont);
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    //setTitle(MAIN_TITLE);
    //setAutoResizeMode(OFF);
    RemoveHorizontalGrid(off);
    AddVerticalGrid(off);
    initializeColInc();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("Associate Rpt"));
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
   * @param rownum
   * @param col
   * @param thickness
   */
  protected void intializeBoldRowChange(int rownum, Color col, int thickness) {
    final Color UNDERLINE_COLOR = Color.lightGray;
    final int UNDERLINE_THICKNESS = 1;
    Color underlineColor = UNDERLINE_COLOR;
    int underlineThickness = UNDERLINE_THICKNESS;
    Font jfont = new java.awt.Font("Arial", Font.BOLD, 10);
    MultipleRowCellRendererHelper2.addUnderlineCellRendererActionChoose(multiRowCellRenderer
        , rownum, col, thickness);
  }

  /**
   * put your documentation comment here
   */
  protected void initializeBlankRow() {
    BLANK_ROW.add(COLNUM_ID, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NAME, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_TRAN, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NETUN, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NETAM, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_AV_NETUN, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_AV_NETAM, EMPTY_STRING);
  }

  /**
   * put your documentation comment here
   */
  protected void initializeColInc() {
    initColInc(COLNUM_ID, 160);
    initColInc(COLNUM_NAME, 65);
    initColInc(COLNUM_TRAN, 45);
    initColInc(COLNUM_NETAM, 100);
    initColInc(COLNUM_NETUN, 45);
    initColInc(COLNUM_AV_NETAM, 90);
    initColInc(COLNUM_AV_NETUN, 25);
  }

  /**
   * put your documentation comment here
   * @param mytable
   */
  public void setMultilineHeader(JTable mytable) {
    MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer();
    Enumeration e = mytable.getColumnModel().getColumns();
    while (e.hasMoreElements()) {
      ((TableColumn)e.nextElement()).setHeaderRenderer(renderer);
    }
  }

  /**
   * put your documentation comment here
   * @param Colname
   * @param Wid
   */
  public void initColInc(int Colname, int Wid) {
    //setColumnWidth(Colname, Wid);
    int width = tbl.getColumnModel().getColumn(Colname).getWidth();
    tbl.getColumnModel().getColumn(Colname).setPreferredWidth(Wid);
  }

  /**
   * put your documentation comment here
   * @param OFF
   */
  public void setAutoResizeMode(int OFF) {
    this.tbl.setAutoResizeMode(OFF);
  }

  /**
   * put your documentation comment here
   * @param Off
   */
  public void RemoveHorizontalGrid(boolean Off) {
    this.tbl.setShowHorizontalLines(Off);
  }

  /**
   * put your documentation comment here
   * @param Off
   */
  public void AddVerticalGrid(boolean Off) {
    this.tbl.setShowVerticalLines(Off);
  }

  /**
   * put your documentation comment here
   * @param newfont
   */
  public void SetFont(Font newfont) {
    this.tbl.setFont(newfont);
  }

  /**
   * put your documentation comment here
   * @param smfont
   */
  protected void initializeFontChange(Font smfont) {
    Font smfont1 = new Font("Century", 1, 10);
    tbl.setFont(smfont);
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_ID, smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_TRAN
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NAME
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NETUN
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NETAM
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_AV_NETUN
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_AV_NETAM
        , smfont);
    MultipleRowCellRendererHelper2.addBoldCellRendererAction(multiRowCellRenderer, 5, smfont1);
  }

  /**
   * put your documentation comment here
   * @param smfont
   * @param row
   */
  protected void initializeFontChangeOneRow(Font smfont, int row) {
    MultipleRowCellRendererHelper2.addBoldCellRendererAction(multiRowCellRenderer, row, smfont);
  }

  //Methods for printing to printer
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PRINT")) {
      theAppMgr.unRegisterSingleEditArea();
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
   * @param startDate
   * @param endDate
   */
  public void populateScreen(Date startDate, Date endDate) {
    startDate = Utils.getBeginingOfDay(startDate);
    endDate = Utils.getEndOfDay(endDate);
    try {
      model.clear();
      SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      String fullTitle = res.getString("Sales By Assoc# - Store ") + " " + theStore.getId() + " "
          + res.getString("For :") + " " + sdf.format(startDate) + " - " + sdf.format(endDate);
      lblTitle.setText(fullTitle);
      model.setFullTitle(fullTitle);
      theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
      Hashtable summaries = GAReportHelper.getAssociateSalesSummaryTableByDate(theAppMgr
          , theStore.getId(), startDate, endDate);
      TreeMap sortedSummaries = new TreeMap(summaries);
      Set keySales = sortedSummaries.keySet();
      Iterator iter = keySales.iterator();
      int totalCountTran = 0;
      int CountTran = 0;
      double totalAvgUnit = 0;
      double totalCountforAvg = 0;
      double AvgUnit = 0;
      double AvgUnittest = 0;
      double GetUnit = 0;
      double GetCount = 0;
      int totalNetUnit = 0;
      int NetUnit = 0;
      ArmCurrency totalNet = new ArmCurrency(0);
      double avgCnt = 0.00d;
      Double TavgCnt = new Double(0.00);
      ArmCurrency totalAvgNet = new ArmCurrency(0);
      ArmCurrency subtotalAvgNet = new ArmCurrency(0);
      ArmCurrency Net = new ArmCurrency(0);
      ArmCurrency avgNet = new ArmCurrency(0);
      int totalsRowNum = 0;
      MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
          , COLNUM_ID, SwingConstants.LEFT);
      String[] array = new String[7];
      NumberFormat form = NumberFormat.getInstance();
      form.setMaximumFractionDigits(2);
      while (iter.hasNext()) {
        String keySale = (String)iter.next();
        // create an array for the table row and add it to the tabel
        // model
        CMSEmpSales mo = (CMSEmpSales)summaries.get(keySale);
        GetUnit = mo.getQuantity().intValue();
        GetCount = mo.getTxnCount().intValue();
        subtotalAvgNet = mo.getNetAmount().divide(mo.getTxnCount().intValue());
        avgCnt = (double)mo.getQuantity().intValue() / mo.getTxnCount().intValue();
        array[0] = " " + mo.getConsultantId().getFirstName() + " "
            + mo.getConsultantId().getLastName();
        array[1] = mo.getConsultantId().getId();
        array[2] = "" + mo.getTxnCount().intValue();
        array[3] = "" + mo.getQuantity();
        array[4] = mo.getNetAmount().formattedStringValue();
        array[5] = "" + form.format(avgCnt);
        array[6] = subtotalAvgNet.formattedStringValue();
        model.addList(array);
        avgNet = avgNet.add(subtotalAvgNet);
        CountTran = CountTran + mo.getTxnCount().intValue();
        AvgUnit = AvgUnit + avgCnt;
        Net = Net.add(mo.getNetAmount());
        NetUnit = NetUnit + mo.getQuantity().intValue();
        totalsRowNum++;
      }
      String[] arrayTotals = new String[7];
      totalNet = totalNet.add(Net);
      totalNetUnit = totalNetUnit + NetUnit;
      totalCountTran = totalCountTran + CountTran;
      totalCountforAvg = totalNetUnit;
      double totalAvgUnitt = (double)totalCountforAvg / totalCountTran;
      totalAvgNet = totalNet.divide(totalCountTran);
      arrayTotals[0] = "GRAND TOTALS";
      arrayTotals[1] = "";
      arrayTotals[2] = "" + totalCountTran;
      arrayTotals[3] = "" + NetUnit;
      arrayTotals[4] = totalNet.formattedStringValue();
      arrayTotals[5] = "" + form.format(totalAvgUnitt);
      arrayTotals[6] = totalAvgNet.formattedStringValue();
      model.addList(arrayTotals);
      tbl.repaint();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out.println("Exception in method-> POPULATESCREEN" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double[] getReportColumnWidths() {
    double[] reportColumnWidths = new double[this.model.getColumnCount()];
    if (reportColumnWidths.length == 7) {
      reportColumnWidths[0] = 0.30;
      reportColumnWidths[1] = 0.10;
      reportColumnWidths[2] = 0.10;
      reportColumnWidths[3] = 0.10;
      reportColumnWidths[4] = 0.15;
      reportColumnWidths[5] = 0.10;
      reportColumnWidths[6] = 0.15;
    } else {
      for (int i = 0; i < reportColumnWidths.length; i++) {
        reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
      }
    }
    return reportColumnWidths;
  }
}

