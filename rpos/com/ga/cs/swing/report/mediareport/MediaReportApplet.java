/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.mediareport;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.SwingConstants;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.pos.datatranssferobject.MediaReportDataTransferObject;
import com.ga.cs.swing.model.MediaReportModel;
import com.ga.cs.swing.report.ArmaniReportPrinter;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper;
import com.ga.cs.swing.table.cellrenderer.action.MediaReportAllRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.ReportUtils;
import com.ga.cs.utils.Utils;
import com.ga.cs.swing.report.ReportConstants;


/**
 * GA Media report: Display sales by store and media (payment) type for a
 * specified date range Loosely based on the Retek RPOS Payment Sales Summary
 * Report
 *
 * @author fbulah
 *
 */
public class MediaReportApplet extends BaseReportApplet {
  public static final String MAIN_TITLE = "Sales By Media Type - Store :";
  public static final int COLNUM_MEDIA_TYPE = 0;
  public static final int COLNUM_MEDIA_COUNT = 1;
  public static final int COLNUM_GROSS_AMOUNT = 2;
  public static final int COLNUM_CREDIT_RETURN = 3;
  public static final int COLNUM_NET_AMOUNT = 4;
  protected String tableFontName;
  protected Font tableFont;
  protected Font tableTotalsRowFont;
  protected int tableFontSize;
  protected int totalsRowNum;
  protected TableSorter sorter;
  public static final String[] MEDIA_REPORT_COL_HEADINGS = new String[] {"Media Type"
      , "Media Count", "Gross Amount", "Credit/Return", "Net Amount"
  };
  public static final int MEDIA_REPORT_PAGE_SIZE = 25;
  public static final String GRAND_TOTAL_LABEL = "Grand Total:";
  public static boolean DEBUG = false;

  /**
   *
   */
  public MediaReportApplet() {
    ReportUtils.logInfoTsMsg("MEDIA REPORT STARTED");
    model = new MediaReportModel(MEDIA_REPORT_COL_HEADINGS);
    sorter = new TableSorter(model);
    tbl = new JCMSTable(sorter, JCMSTable.VIEW_ROW);
    //model.setRowsShown(MEDIA_REPORT_PAGE_SIZE);
    // Issue # 829
    //sorter.setTableHeader(tbl.getTableHeader());
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    tableTotalsRowFont = new java.awt.Font(tableFontName, Font.BOLD, tableFontSize);
    multiRowCellRenderer = MultipleRowCellRendererHelper.createMultipleRowCellRenderer();
    MultipleRowCellRendererHelper.setAllRowsCellRendererAction(multiRowCellRenderer
        , new MediaReportAllRowsRenderAction());
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(MAIN_TITLE);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("Media Rpt"));
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
    startDate = Utils.getBeginingOfDay(startDate);
    endDate = Utils.getEndOfDay(endDate);
    int rowNum = 0;
    try {
      ReportUtils.logInfoTsMsg("MEDIA REPORT POPULATE SCREEN");
      model.clear();
      multiRowCellRenderer = MultipleRowCellRendererHelper.createMultipleRowCellRenderer();
      MultipleRowCellRendererHelper.setAllRowsCellRendererAction(multiRowCellRenderer
          , new MediaReportAllRowsRenderAction());
      ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
      setTitle(MAIN_TITLE);
      SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      String fullTitle = res.getString(MAIN_TITLE) + " " + theStore.getId() + " "
          + res.getString("For") + " " + sdf.format(startDate) + " - " + sdf.format(endDate);
      model.setFullTitle(fullTitle);
      lblTitle.setText(fullTitle);
      theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, IApplicationManager.PREV_CANCEL_BUTTON);
      Hashtable summaries = GAReportHelper.getStorePaymentSummaryTableByPaymentType(theAppMgr
          , theStore.getId(), startDate, endDate);
      //
      //printData(summaries);
      //
      TreeMap sortedSummaries = new TreeMap(summaries);
      Set mediaTypes = sortedSummaries.keySet();
      Iterator iter = mediaTypes.iterator();
      totalsRowNum = 0;
      // add renderer to left align the payment type column
      MultipleRowCellRendererHelper.addHorizontalAlignmentCellRendererAction(multiRowCellRenderer
          , COLNUM_MEDIA_TYPE, SwingConstants.LEFT);
      //
      // loop through the data and populate table rows
      //
      while (iter.hasNext()) {
        String mediaType = (String)iter.next();
        //
        // create an array for the table row and add it to the tabel
        // model
        //
        MediaReportDataTransferObject mo = (MediaReportDataTransferObject)summaries.get(mediaType);
        Vector v = new Vector();
        //v.add(mo.getDisplayTypeId());
        v.add(ReportConstants.getPaymentType(mediaType));
        v.add(new Integer(mo.getMediaCount()));
        v.add(mo.getGrossAmount());
        v.add(mo.getCreditReturn());
        v.add(mo.getNetAmount());
        model.addRow(v);
        rowNum++;
        totalsRowNum++;
      }
      // add grand totals row
      MediaReportDataTransferObject totals = this.getStorePaymentSummaryGrandTotals(
          GRAND_TOTAL_LABEL, summaries);
      Vector v = new Vector();
      v.add(totals.getDisplayTypeId());
      v.add(new Integer(totals.getMediaCount()));
      v.add(totals.getGrossAmount());
      v.add(totals.getCreditReturn());
      v.add(totals.getNetAmount());
      if (DEBUG) {
        for (int i = 0; i < v.size(); i++) {
          System.out.println("totals: v[" + i + "]=" + v.get(i));
          ReportUtils.logInfoTsMsg("MEDIA REPORT totals: v[" + i + "]=" + v.get(i));
        }
      }
      model.addRow(v);
      // add renderer action that will boldface and underline the grand
      // totals row
      MultipleRowCellRendererHelper.addBoldUnderlineCellRendererAction(multiRowCellRenderer
          , totalsRowNum, tableTotalsRowFont);
      tbl.repaint();
    } catch (Exception ex) {
      System.out.println("Exception in method MediaReportApplet.populateScreen: e=" + ex + " msg="
          + ex.getMessage() + " rowNum=" + rowNum + " totalsRowNum=" + totalsRowNum);
      ReportUtils.logInfoTsMsg("MEDIA REPORT: MediaReportApplet.populateScreen: e=" + ex + " msg="
          + ex.getMessage() + " rowNum=" + rowNum + " totalsRowNum=" + totalsRowNum);
      ex.printStackTrace();
      theAppMgr.showErrorDlg("Report Error. Call support.");
    }
  }

  //    /**
   //     * @param summaries
   //     */
  //    protected void printData(Hashtable summaries) {
  //        int i = 0;
  //        for (Iterator iter = summaries.keySet().iterator(); iter.hasNext();) {
  //            String paymentType = (String) iter.next();
  //            MediaReportDataTransferObject mo = (MediaReportDataTransferObject) summaries.get(paymentType);
  //            if (mo != null) {
  //                if (DEBUG) {
  //                    System.out.println(" i=" + i);
  //                    System.out.println(" MediaReport.printData: paymentType   =" + paymentType);
  //                    System.out.println(" MediaReport.printData: mediaCount=   " + mo.getMediaCount());
  //                    System.out.println(" MediaReport.printData: groSSAmount   =" + mo.getGrossAmount().formattedStringValue());
  //                    System.out.println(" MediaReport.printData: creditReturn  =" + mo.getCreditReturn().formattedStringValue());
  //                    System.out.println(" MediaReport.printData: netAmount     =" + mo.getNetAmount().formattedStringValue());
  //                    System.out.println("=======================================\n\n");
  //                    ReportUtils.logInfoTsMsg("MEDIA REPORT:  i=" + i);
  //                    ReportUtils.logInfoTsMsg("MEDIA REPORT:  printData: paymentType   =" + paymentType);
  //                    ReportUtils.logInfoTsMsg("MEDIA REPORT:  printData: mediaCount=   " + mo.getMediaCount());
  //                    ReportUtils.logInfoTsMsg("MEDIA REPORT:  printData: groSSAmount   =" + mo.getGrossAmount().formattedStringValue());
  //                    ReportUtils.logInfoTsMsg("MEDIA REPORT:  printData: creditReturn  =" + mo.getCreditReturn().formattedStringValue());
  //                    ReportUtils.logInfoTsMsg("MEDIA REPORT:  printData: netAmount     =" + mo.getNetAmount().formattedStringValue());
  //                    ReportUtils.logInfoTsMsg("MEDIA REPORT:  =======================================\n\n");
  //                }
  //            }
  //
  //        }
  //    }
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
      reportColumnWidths[0] = 0.30;
      reportColumnWidths[1] = 0.10;
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

  /**
   * put your documentation comment here
   * @param totalsLabel
   * @param paymentSummaries
   * @return
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
      //System.out.println( "totals: mediaType=" + mediaType + " mo=" + mo );
      totals.setMediaCount(totals.getMediaCount() + mo.getMediaCount());
      //System.out.println( "totals: mediaType=" + mediaType + " mo.mediaCount=" + mo.getMediaCount() );
      try {
        //System.out.println( "totals: mediaType=" + mediaType + " mo.grossAmount=" + mo.getGrossAmount() );
        totals.setGrossAmount(totals.getGrossAmount().add(mo.getGrossAmount()));
      } catch (CurrencyException e) {
        System.out.println(errMsgPrefix + "grossAmount on mediaType" + mediaType + " msg="
            + e.getMessage());
        e.printStackTrace();
      }
      try {
        //System.out.println( "totals: mediaType=" + mediaType + " mo.creditReturn=" + mo.getCreditReturn() );
        totals.setCreditReturn(totals.getCreditReturn().add(mo.getCreditReturn()));
      } catch (CurrencyException e1) {
        System.out.println(errMsgPrefix + "creditReturns on mediaType" + mediaType + " msg="
            + e1.getMessage());
        e1.printStackTrace();
      }
    }
    try {
      totals.setNetAmount(totals.getGrossAmount().subtract(totals.getCreditReturn()));
    } catch (CurrencyException exp) {
      System.out.println(errMsgPrefix + " msg=" + exp.getMessage());
      exp.printStackTrace();
    }
    //System.out.println( "totals: totals.netAmount=" + totals.getNetAmount() );
    return totals;
  }
}

