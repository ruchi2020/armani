/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.itemreport;

import java.awt.Color;
import java.awt.Font;
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
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.chelseasystems.cs.txnposter.ArmSalesSummary;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.swing.model.GaItemReportModel;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper2;
import com.ga.cs.swing.table.cellrenderer.action.ItemSalesAllRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.ReportUtils;
import com.ga.cs.utils.Utils;
import com.chelseasystems.cr.item.MiscItemManager;


/**
 * GA ITEM SALES report
 *  .
 */
public class GaItemReportApplet extends BaseReportApplet {
  public static final String MAIN_TITLE = "Sales By SKU";
  public static final int COLNUM_ID = 0;
  public static final int COLNUM_NAME = 1;
  public static final int COLNUM_IDD = 2;
  public static final int COLNUM_NAMED = 3;
  public static final int COLNUM_IDE = 4;
  public static final int COLNUM_NETSAL = 5;
  public static final int COLNUM_NETUN = 6;
  final boolean off = false;
  final boolean on = true;
  final Color UNDERLINE_COLOR_SUB = Color.lightGray;
  final int UNDERLINE_THICKNESS_SUB = 3;
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
  public static final String[] ITEM_REPORT_COL_HEADINGS = new String[] {"Sku", "DescShort", "VPN"
      , "Dept", "DeptDesc", "NetSales ", "NetUnits"
  };
  public static final int ASSOC_REPORT_PAGE_SIZE = 25;
  public static final String GRAND_TOTAL_LABEL = "Grand Total:";
  public static boolean DEBUG = false;

  /**
   *
   */
  public GaItemReportApplet() {
    ReportUtils.logInfoTsMsg("ITEM SALES REPORT STARTED");
    model = new GaItemReportModel(ITEM_REPORT_COL_HEADINGS);
    sorter = new TableSorter(model);
    tbl = new JCMSTable(sorter, JCMSTable.VIEW_ROW);
    // Issue # 829
    //sorter.setTableHeader(tbl.getTableHeader());
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    tableTotalsRowFont = new java.awt.Font(tableFontName, Font.BOLD, tableFontSize);
    multiRowCellRenderer = MultipleRowCellRendererHelper2.createMultipleRowCellRenderer();
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NAME, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_ID, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NAMED, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_IDD, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_IDE, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NETSAL, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NETUN, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.setAllRowsCellRendererAction(multiRowCellRenderer
        , new ItemSalesAllRowsRenderAction());
    Font smfont = new Font("Century", 1, 10);
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    //setTitle(MAIN_TITLE);
    initializeBlankRow();
    initializeFontChange(smfont);
    //setFont(smfont);
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(MAIN_TITLE);
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
    return (res.getString("Item Sale Rpt"));
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
   */
  protected void initializeColInc() {
    initColInc(COLNUM_ID, 90);
    initColInc(COLNUM_NAME, 85);
    initColInc(COLNUM_IDD, 85);
    initColInc(COLNUM_IDE, 85);
    initColInc(COLNUM_NAMED, 40);
    initColInc(COLNUM_NETSAL, 115);
    initColInc(COLNUM_NETUN, 50);
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
    BLANK_ROW.add(COLNUM_IDD, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NAMED, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_IDE, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NETSAL, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NETUN, EMPTY_STRING);
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
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NAME
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_IDD, smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NAMED
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_IDE, smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NAME
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NETSAL
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NETUN
        , smfont);
  }

  /**
   * put your documentation comment here
   * @param smfont
   * @param row
   */
  protected void initializeFontChangeOneRow(Font smfont, int row) {
    MultipleRowCellRendererHelper2.addBoldCellRendererAction(multiRowCellRenderer, row, smfont);
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
      String fullTitle = res.getString("Sales By SKU - Store ") + " " + theStore.getId() + " "
          + res.getString("For: ") + " " + sdf.format(startDate) + " - " + sdf.format(endDate);
      model.setFullTitle(fullTitle);
      lblTitle.setText(fullTitle);
      theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
      SalesSummary[] mo = GAReportHelper.getItemSalesReportByStoreIdAndDates(theAppMgr
          , theStore.getId(), startDate, endDate);
      int totalCountTran = 0;
      int CountTran = 0;
      int totalAvgUnit = 0;
      int AvgUnit = 0;
      int totalNetUnit = 0;
      int NetUnit = 0;
      ArmCurrency totalNet = new ArmCurrency(0);
      ArmCurrency totalAvgNet = new ArmCurrency(0);
      ArmCurrency Net = new ArmCurrency(0);
      int totalsRowNum = 0;
      //mo = dao.getTotalSalesByStoreIdAndDates( storeId, begin, end );
      for (int i = 0; i < mo.length; i++) {
        //if (MiscItemManager.getInstance().isMiscItem(mo[i].getItemId())) {
        //    continue;
        //}
        ArmSalesSummary summ = (ArmSalesSummary)mo[i];
        String[] array = new String[7];
        array[0] = summ.getBarCode();
        array[1] = summ.getClassDesc();
        array[2] = summ.getItemDesc();
        array[3] = summ.getDeptID();
        array[4] = summ.getDeptDesc();
        array[5] = summ.getTotalAmount().formattedStringValue();
        array[6] = "" + summ.getTotalQuantity();
        model.addList(array);
        Net = Net.add(summ.getTotalAmount());
        NetUnit = NetUnit + summ.getTotalQuantity().intValue();
        totalsRowNum++;
      }
      totalNet = totalNet.add(Net);
      totalNetUnit = totalNetUnit + NetUnit;
      String[] arrayTotals = new String[7];
      arrayTotals[0] = "TOTALS";
      arrayTotals[1] = "";
      arrayTotals[2] = "";
      arrayTotals[3] = "";
      arrayTotals[4] = "";
      arrayTotals[5] = totalNet.formattedStringValue();
      arrayTotals[6] = "" + totalNetUnit;
      model.addList(arrayTotals);
      //MultipleRowCellRendererHelper2.addBoldUnderlineCellRendererAction(multiRowCellRenderer, totalsRowNum, tableTotalsRowFont);
      tbl.repaint();
    } catch (Exception ex) {
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
      reportColumnWidths[0] = 0.20;
      reportColumnWidths[1] = 0.20;
      reportColumnWidths[2] = 0.20;
      reportColumnWidths[3] = 0.075;
      reportColumnWidths[4] = 0.10;
      reportColumnWidths[5] = 0.15;
      reportColumnWidths[6] = 0.075;
    } else {
      for (int i = 0; i < reportColumnWidths.length; i++) {
        reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
      }
    }
    return reportColumnWidths;
  }
}

