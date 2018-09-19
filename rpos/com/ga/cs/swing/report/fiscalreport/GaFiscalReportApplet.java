/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.fiscalreport;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.ScrollablePane;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.dataaccess.artsoracle.dao.GaTranFiscalOracleDAO;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.pos.datatranssferobject.GaFiscalReportDataTransferObject;
import com.ga.cs.pos.datatranssferobject.GaTranFiscalReportDataTransferObject;
import com.ga.cs.swing.model.FiscalReportModel;
import com.ga.cs.swing.report.ArmaniReportPrinter;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper2;
import com.ga.cs.swing.table.cellrenderer.action.FiscalAllRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.ReportUtils;
import com.ga.cs.utils.Utils;
import com.chelseasystems.cs.util.DateFormatUtil;


/**
 * GA DEPARTMENT SALES report
 */
public class GaFiscalReportApplet extends BaseReportApplet {
  public static final String MAIN_TITLE = "Transaction By Time";
  ScrollablePane scrollPane;
  //    public static final int COLNUM_ID = 0;
  public static final int COLNUM_NAME = 0;
  public static final int COLNUM_QTY = 1;
  public static final int COLNUM_GROSS_SAL = 2;
  public static final int COLNUM_NET_SAL = 3;
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
  public static final String[] FISCAL_REPORT_COL_HEADINGS = new String[] {"TIME", "NO TRANSCTION"
      , "GROSS AMOUNT", "SALE AMOUNT"
  };
  public static final int DEPT_REPORT_PAGE_SIZE = 25;
  public static final String GRAND_TOTAL_LABEL = "Grand Total:";
  public static boolean DEBUG = false;

  /**
   *
   */
  public GaFiscalReportApplet() {
    ReportUtils.logInfoTsMsg("FISCAL TRAN REPORT STARTED");
    model = new FiscalReportModel(FISCAL_REPORT_COL_HEADINGS);
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
        , new FiscalAllRowsRenderAction());
    //        MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer, COLNUM_ID, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NAME, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_QTY, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_GROSS_SAL, SwingConstants.CENTER);
    MultipleRowCellRendererHelper2.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
        , COLNUM_NET_SAL, SwingConstants.CENTER);
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(MAIN_TITLE);
    //initializeBlankRow();
    initializeFontChange(smfont);
    //int width = tbl.getColumnModel().getColumn(COLNUM_NAME).getWidth();
    //int width_id = tbl.getColumnModel().getColumn(COLNUM_ID).getWidth();
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
    return (res.getString("Fiscal Rpt"));
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
    //        BLANK_ROW.add(COLNUM_ID, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NAME, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_QTY, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_GROSS_SAL, EMPTY_STRING);
    BLANK_ROW.add(COLNUM_NET_SAL, EMPTY_STRING);
  }

  /**
   * put your documentation comment here
   */
  protected void initializeColInc() {
    //        initColInc(COLNUM_ID, 125);
    initColInc(COLNUM_NAME, 125);
    initColInc(COLNUM_QTY, 75);
    initColInc(COLNUM_GROSS_SAL, 175);
    initColInc(COLNUM_NET_SAL, 175);
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
    //        MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_ID, smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NAME
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_QTY, smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_GROSS_SAL
        , smfont);
    MultipleRowCellRendererHelper2.addHorizontalFontAction(multiRowCellRenderer, COLNUM_NET_SAL
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
      {
        SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
        String fullTitle = res.getString("Transaction By Time ") + " " + theStore.getId() + " "
            + res.getString("For : ") + " " + sdf.format(startDate) + " - " + sdf.format(endDate);
        model.setFullTitle(fullTitle);
        lblTitle.setText(fullTitle);
        theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
        System.out.println("show menu");
        ArmCurrency Sales = new ArmCurrency(0);
        int NetUnits = 0;
        Font jfont = new Font("Arial", 1, 11);
        int rowNum = 0;
        int GTNetUnits = 0;
        ArmCurrency GGTSales = new ArmCurrency(0);
        ArmCurrency GGTNetSales = new ArmCurrency(0);
        ArmCurrency GGTGrossSales = new ArmCurrency(0);
        int GGTNetUnits = 0;
        ArmCurrency AddSales = new ArmCurrency(0);
        ArmCurrency AddNetSales = new ArmCurrency(0);
        ArmCurrency AddGrossSales = new ArmCurrency(0);
        int AddNetUnits = 0;
        int totalsRowNum = 0;
        GaFiscalReportDataTransferObject[] dp = GAReportHelper.getGaFiscalReportByStoreIdAndDates(this.
            theAppMgr, theStore.getId(), startDate, endDate);
        for (int index = 0; index < dp.length; index++) {
          //VM: The date returned from GaTranFiscalOracleDAO is in 'DD/mm/yyyy' format
          Date fiscalDate = new SimpleDateFormat("DD/mm/yyyy").parse(dp[index].getFiscalDay());
          //String fiscalDay = dp[index].getFiscalDay();
          String fiscalDay = DateFormatUtil.getFormattedDateString(fiscalDate);
          ArmCurrency SSTSales = new ArmCurrency(0);
          ArmCurrency SSTNetSales = new ArmCurrency(0);
          ArmCurrency SSTGrossSales = new ArmCurrency(0);
          int SSTNetUnits = 0;
          int TNetUnits = 0;
          ArmCurrency TSNetSales = new ArmCurrency(0);
          ArmCurrency TSGrossSales = new ArmCurrency(0);
          int totalsRow = 0;
          String[] arrayFiscalDay = new String[4];
          //                    arrayFiscalDay[0] = "DATA FOR : ";
          arrayFiscalDay[0] = "DATA FOR : " + fiscalDay;
          arrayFiscalDay[1] = "";
          arrayFiscalDay[2] = "";
          arrayFiscalDay[3] = "";
          model.addList(arrayFiscalDay);
          //rowNum++;
          totalsRow = ++rowNum - 1;
          for (Enumeration enm = dp[index].getTranFiscalReports(); enm.hasMoreElements(); ) {
            GaTranFiscalReportDataTransferObject object = (GaTranFiscalReportDataTransferObject)enm.
                nextElement();
            String Half = object.getFiscalHalf();
            int Seq = object.getFiscalSeq();
            ArmCurrency STNetSales = new ArmCurrency(0);
            ArmCurrency STGrossSales = new ArmCurrency(0);
            int STNetUnits = 0;
            STNetSales = STNetSales.add(object.getNetSales());
            STGrossSales = STGrossSales.add(object.getGrossSales());
            STNetUnits = STNetUnits + object.getQty();
            String[] arrayMain = new String[4];
            //                        arrayMain[0] = dp[index01].getFiscalSeqString();
            arrayMain[0] = object.getFiscalHalf();
            arrayMain[1] = object.getQtyString();
            arrayMain[2] = object.getGrossSalesString();
            arrayMain[3] = object.getNetSalesString();
            model.addList(arrayMain);
            TNetUnits = TNetUnits + object.getQty();
            TSNetSales = TSNetSales.add(object.getNetSales());
            TSGrossSales = TSGrossSales.add(object.getGrossSales());
            SSTNetSales = STNetSales;
            SSTGrossSales = STGrossSales;
            SSTNetUnits = STNetUnits;
            rowNum++;
          }
          AddNetSales = AddNetSales.add(TSNetSales);
          AddGrossSales = AddGrossSales.add(TSGrossSales);
          AddNetUnits = AddNetUnits + TNetUnits;
          String[] arraySTotals = new String[4];
          arraySTotals[0] = "TOTALS FOR FISCAL: " + fiscalDay;
          arraySTotals[1] = "" + TNetUnits;
          arraySTotals[2] = TSGrossSales.formattedStringValue();
          arraySTotals[3] = TSNetSales.formattedStringValue();
          model.addList(arraySTotals);
          rowNum++;
          //intializeBoldRowChange(rowNum-1,underlineColor_sub,underlineThickness_sub)
          // ;
          GGTNetSales = GGTNetSales.add(TSNetSales);
          GGTGrossSales = GGTGrossSales.add(TSGrossSales);
        }
        rowNum++;
        GGTSales = GGTSales.add(AddSales);
        GGTNetUnits = GGTNetUnits + AddNetUnits;
        String arrayGTotals[] = new String[4];
        arrayGTotals[0] = "GRAND TOTALS";
        arrayGTotals[1] = "" + GGTNetUnits;
        arrayGTotals[2] = GGTGrossSales.formattedStringValue();
        arrayGTotals[3] = GGTNetSales.formattedStringValue();
        model.addList(arrayGTotals);
        rowNum++;
        //intializeBoldRowChange(rowNum,underlineColor_grand,underlineThickness_grand)
        // ;
        //MultipleRowCellRendererHelper2.addUnderlineCellRendererAction(multiRowCellRenderer,
        // rowNum);
      }
      tbl.repaint();
    } catch (Exception ex) {
      ex.printStackTrace();
      theAppMgr.showExceptionDlg(ex);
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
    if (reportColumnWidths.length == 4) {
      reportColumnWidths[0] = 0.25;
      reportColumnWidths[1] = 0.25;
      reportColumnWidths[2] = 0.25;
      reportColumnWidths[3] = 0.25;
    } else {
      for (int i = 0; i < reportColumnWidths.length; i++) {
        reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
      }
    }
    return reportColumnWidths;
  }
}

