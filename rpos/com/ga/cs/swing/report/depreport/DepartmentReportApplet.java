/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.depreport;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.util.Enumeration;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.ScrollablePane;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.swing.model.DepartmentReportModel;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper2;
import com.ga.cs.swing.table.cellrenderer.action.DeptSalesAllRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.ReportUtils;
import com.ga.cs.utils.Utils;
import com.ga.cs.pos.GAReportHelper;
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.chelseasystems.cs.txnposter.ArmSalesSummary;


/**
 * GA DEPARTMENT SALES report
 *
 *
 */
public class DepartmentReportApplet extends BaseReportApplet {
  public static final String MAIN_TITLE = "Sales By Department for Store";
  ScrollablePane scrollPane;
  public static final int COLNUM_ID = 0;
  public static final int COLNUM_NAME = 1;
  public static final int COLNUM_NETUN = 2;
  public static final int COLNUM_GROSS = 3;
  public static final int COLNUM_DISC = 4;
  public static final int COLNUM_SAL = 5;
  public static final int COLNUM_RETUN = 6;
  public static final int COLNUM_RET = 7;
  public static final int COLNUM_RTNDISC = 8;
  public static final int COLNUM_RTNNET = 9;
  public static final int COLNUM_SALNET = 10;
  final boolean off = false;
  final boolean on = true;
  final Color UNDERLINE_COLOR_SUB = Color.lightGray;
  final int UNDERLINE_THICKNESS_SUB = 1;
  Color underlineColor_sub = UNDERLINE_COLOR_SUB;
  int underlineThickness_sub = UNDERLINE_THICKNESS_SUB;
  final Color UNDERLINE_COLOR_GRAND = Color.darkGray;
  final int UNDERLINE_THICKNESS_GRAND = 4;
  Color underlineColor_grand = UNDERLINE_COLOR_GRAND;
  int underlineThickness_grand = UNDERLINE_THICKNESS_GRAND;
  protected int OFF = JTable.AUTO_RESIZE_OFF;
  protected int Off = JTable.AUTO_RESIZE_OFF;
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
  Font jfont = new Font("Arial", 1, 6);
  protected final int WIDTH_MULT = 2;
  protected final int WIDTH_MULT_2 = 1;
  public static final String[] DEPT_REPORT_COL_HEADINGS = new String[] {"Dept#", "Department"
      , "GrSal$", "GrMkd$", "Sales$", "Rtn#", "GrRtn", "RtnMkd$", "Rtn$", "NetQty", "NetSal$"
  };
  public static final int DEPT_REPORT_PAGE_SIZE = 25;
  public static final String GRAND_TOTAL_LABEL = "Grand Total:";
  public static boolean DEBUG = false;

  /**
   *
   */
  public DepartmentReportApplet() {
    ReportUtils.logInfoTsMsg("DEPARTMENT SALES REPORT STARTED");
    model = new DepartmentReportModel(DEPT_REPORT_COL_HEADINGS);
    sorter = new TableSorter(model);
    Font jfont = new Font("Arial", 1, 6);
    tbl = new JCMSTable(sorter, JCMSTable.VIEW_ROW);
    //sorter.setTableHeader(tbl.getTableHeader());
    Font smfont = new Font("Century", 1, 10);
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    tableTotalsRowFont = new java.awt.Font(tableFontName, Font.BOLD, tableFontSize);
    multiRowCellRenderer = MultipleRowCellRendererHelper2.createMultipleRowCellRenderer();
    MultipleRowCellRendererHelper2.setAllRowsCellRendererAction(multiRowCellRenderer
        , new DeptSalesAllRowsRenderAction());
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_ID, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NAME, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NETUN, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_GROSS, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_DISC, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_SAL, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_RETUN, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_RET, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_RTNDISC, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_RTNNET, SwingConstants.LEFT);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_SALNET, SwingConstants.LEFT);
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(MAIN_TITLE);
    initializeBlankRow();
    initializeFontChange(smfont);
    setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    RemoveHorizontalGrid(off);
    AddVerticalGrid(off);
    initializeColInc();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("Dept Rpt"));
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
  protected void initializeBlankRow() {
    BLANK_ROW.add(COLNUM_ID, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NAME, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NETUN, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_GROSS, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_DISC, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_SAL, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_RETUN, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_RET, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_RTNDISC, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_RTNNET, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_SALNET, EMPTY_STRING);
  }

  /**
   * put your documentation comment here
   */
  protected void initializeColInc() {
    initColInc(COLNUM_ID, 55);
    initColInc(COLNUM_NAME, 90);
    initColInc(COLNUM_NETUN, 70);
    initColInc(COLNUM_GROSS, 70);
    initColInc(COLNUM_DISC, 70);
    initColInc(COLNUM_SAL, 35);
    initColInc(COLNUM_RETUN, 80);
    initColInc(COLNUM_RET, 75);
    initColInc(COLNUM_RTNDISC, 70);
    initColInc(COLNUM_RTNNET, 45);
    initColInc(COLNUM_SALNET, 70);
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
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NETUN
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_GROSS
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_DISC
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_SAL, smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_RETUN
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_RET, smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_RTNDISC
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_RTNNET
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_SALNET
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

  /**
   * put your documentation comment here
   * @param startDate
   * @param endDate
   */
  public void populateScreen(Date startDate, Date endDate) {
    startDate = Utils.getBeginingOfDay(startDate);
    endDate = Utils.getEndOfDay(endDate);
    ArmCurrency GGTNetSales = new ArmCurrency(0);
    ArmCurrency GGTSales = new ArmCurrency(0);
    ArmCurrency GGTSalesNet = new ArmCurrency(0);
    ArmCurrency GGTNetReturns = new ArmCurrency(0);
    int GGTNetUnits = 0;
    ArmCurrency GGTGrossSales = new ArmCurrency(0);
    ArmCurrency GGTGrossMkdown = new ArmCurrency(0);
    ArmCurrency GGTReturnMkdown = new ArmCurrency(0);
    int GGTReturnsUnits = 0;
    ArmCurrency GGTReturns = new ArmCurrency(0);
    String[] arrayGGTotals = new String[11];
    try {
      model.clear();
      {
        SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
        String fullTitle = res.getString("Sales By Dept# - Store :") + " " + theStore.getId() + " "
            + res.getString("For : ") + " " + sdf.format(startDate) + " - " + sdf.format(endDate);
        model.setFullTitle(fullTitle);
        lblTitle.setText(fullTitle);
        theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
        System.out.println("show menu");
        Hashtable hDeptSale = GAReportHelper.getDepartmentSalesReportByStoreIdGroupAndDates(
            theAppMgr, theStore.getId(), startDate, endDate, null);
        Enumeration enm = hDeptSale.keys();
        for (; enm.hasMoreElements(); ) {
          int TNetUnits = 0;
          int TReturnsUnits = 0;
          ArmCurrency TNetSales = new ArmCurrency(0);
          ArmCurrency TSales = new ArmCurrency(0);
          ArmCurrency TSalesNet = new ArmCurrency(0);
          ArmCurrency TNetReturns = new ArmCurrency(0);
          ArmCurrency TGrossSales = new ArmCurrency(0);
          ArmCurrency TGrossMkdown = new ArmCurrency(0);
          ArmCurrency TReturnMkdown = new ArmCurrency(0);
          ArmCurrency TReturns = new ArmCurrency(0);
          int TAddNetUnits = 0;
          String groupDiv = (String)enm.nextElement();
          ArrayList list = (ArrayList)hDeptSale.get(groupDiv);
          String[] arrayGroup = new String[11];
          arrayGroup[0] = "GROUP";
          arrayGroup[1] = groupDiv;
          arrayGroup[2] = "";
          arrayGroup[3] = "";
          arrayGroup[4] = "";
          arrayGroup[5] = "";
          arrayGroup[6] = "";
          arrayGroup[7] = "";
          arrayGroup[8] = "";
          arrayGroup[9] = "";
          arrayGroup[10] = "";
          model.addList(arrayGroup);
          SalesSummary[] salesSummaries = (SalesSummary[])list.toArray(new SalesSummary[0]);
          for (int index = 0; index < salesSummaries.length; index++) {
            ArmSalesSummary salesSummary = (ArmSalesSummary)salesSummaries[index];
            String[] arrayMain = new String[11];
            arrayMain[0] = salesSummary.getDeptID();
            arrayMain[1] = "";
            arrayMain[2] = salesSummary.getGrossSaleAmt().formattedStringValue();
            arrayMain[3] = salesSummary.getSaleMarkdownAmt().getFormattedStringValue();
            arrayMain[4] = salesSummary.getNetSaleAmt().getFormattedStringValue();
            arrayMain[5] = String.valueOf(salesSummary.getReturnQty());
            arrayMain[6] = salesSummary.getGrossReturnAmt().getFormattedStringValue();
            arrayMain[7] = salesSummary.getReturnMarkdownAmt().getFormattedStringValue();
            arrayMain[8] = salesSummary.getNetReturnAmt().getFormattedStringValue();
            arrayMain[9] = salesSummary.getTotalQuantity().toString();
            arrayMain[10] = salesSummary.getTotalAmount().formattedStringValue();
            model.addList(arrayMain);
            TNetUnits = TNetUnits + salesSummary.getTotalQuantity().intValue();
            TGrossSales = TGrossSales.add(salesSummary.getGrossSaleAmt());
            TSales = TSales.add(salesSummary.getNetSaleAmt());
            TReturnsUnits = TReturnsUnits + salesSummary.getReturnQty();
            TReturns = TReturns.add(salesSummary.getGrossReturnAmt());
            TReturnMkdown = TReturnMkdown.add(salesSummary.getReturnMarkdownAmt());
            TGrossMkdown = TGrossMkdown.add(salesSummary.getSaleMarkdownAmt());
            TNetReturns = TNetReturns.add(salesSummary.getNetReturnAmt());
            TNetSales = TNetSales.add(salesSummary.getTotalAmount());
            TAddNetUnits = TAddNetUnits + salesSummary.getTotalQuantity().intValue();
            TSalesNet = TSalesNet.add(salesSummary.getTotalAmount());
          }
          String[] arraySTotals = new String[11];
          arraySTotals[0] = "TOTAL ";
          arraySTotals[1] = groupDiv;
          arraySTotals[2] = TGrossSales.formattedStringValue();
          arraySTotals[3] = TGrossMkdown.formattedStringValue();
          arraySTotals[4] = TSales.formattedStringValue();
          arraySTotals[5] = "" + TReturnsUnits;
          arraySTotals[6] = TReturns.formattedStringValue();
          arraySTotals[7] = TReturnMkdown.formattedStringValue();
          arraySTotals[8] = TNetReturns.formattedStringValue();
          arraySTotals[9] = "" + TNetUnits;
          arraySTotals[10] = TSalesNet.formattedStringValue();
          model.addList(arraySTotals);
          GGTGrossSales = GGTGrossSales.add(TGrossSales);
          GGTGrossMkdown = GGTGrossMkdown.add(TGrossMkdown);
          GGTReturnMkdown = GGTReturnMkdown.add(TReturnMkdown);
          GGTSales = GGTSales.add(TSales);
          GGTReturnsUnits = GGTReturnsUnits + TReturnsUnits;
          GGTNetUnits = GGTNetUnits + TNetUnits;
          GGTReturns = GGTReturns.add(TReturns);
          GGTNetReturns = GGTNetReturns.add(TNetReturns);
          GGTNetSales = GGTNetSales.add(TNetSales);
        }
        model.addRow(BLANK_ROW);
        String arrayGTotals[] = new String[11];
        arrayGTotals[0] = "GRAND";
        arrayGTotals[1] = "TOTAL";
        arrayGTotals[2] = GGTGrossSales.formattedStringValue();
        arrayGTotals[3] = GGTGrossMkdown.formattedStringValue();
        arrayGTotals[4] = GGTSales.formattedStringValue();
        arrayGTotals[5] = "" + GGTReturnsUnits;
        arrayGTotals[6] = GGTReturns.formattedStringValue();
        arrayGTotals[7] = GGTReturnMkdown.formattedStringValue();
        arrayGTotals[8] = GGTNetReturns.formattedStringValue();
        arrayGTotals[9] = "" + GGTNetUnits;
        arrayGTotals[10] = GGTNetSales.formattedStringValue();
        model.addList(arrayGTotals);
        //intializeBoldRowChange(rowNum,underlineColor_grand,underlineThickness_grand) ;
        //MultipleRowCellRendererHelper2.addUnderlineCellRendererAction(multiRowCellRenderer, rowNum);
      }
      tbl.repaint();
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double[] getReportColumnWidths() {
    double[] reportColumnWidths = new double[this.model.getColumnCount()];
    if (reportColumnWidths.length == 10) {
      reportColumnWidths[0] = 0.10;
      reportColumnWidths[1] = 0.10;
      reportColumnWidths[2] = 0.05;
      reportColumnWidths[3] = 0.10;
      reportColumnWidths[4] = 0.10;
      reportColumnWidths[5] = 0.10;
      reportColumnWidths[6] = 0.05;
      reportColumnWidths[7] = 0.10;
      reportColumnWidths[8] = 0.10;
      reportColumnWidths[9] = 0.10;
      reportColumnWidths[10] = 0.10;
    } else {
      for (int i = 0; i < reportColumnWidths.length; i++) {
        reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
      }
    }
    return reportColumnWidths;
  }
}

