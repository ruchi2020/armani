/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.consignmentsummary;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.bean.table.JCMSTableHeader;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.pos.datatranssferobject.*;
import com.ga.cs.swing.model.ConsignmentSummaryReportModel;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.report.consignmentdetails.MultiLineHeaderRenderer;
import com.ga.cs.swing.table.cellrenderer.MultipleRowCellRendererHelper;
import com.ga.cs.swing.table.cellrenderer.action.ConsignmentSummaryReportAllRowsRenderAction;
import com.ga.cs.swing.table.tablesorter.TableSorter;
import com.ga.cs.utils.*;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.employee.CMSEmployee;


/**
 * GA Cosnignment Summary report: Display consignment summaries by customer for
 * a store and specified date range
 */
public class ConsignmentSummaryReportApplet extends BaseReportApplet {
  public static final String MAIN_TITLE = "Consignment By Customer";
  public static final int COLNUM_CONSOLIDATED_OVER_SHORT_TYPE = 0;
  public static final int COLNUM_CONSOLIDATED_OVER_SHORT_COUNT = 1;
  public static final int COLNUM_GROSS_AMOUNT = 2;
  public static final int COLNUM_CREDIT_RETURN = 3;
  public static final int COLNUM_NET_AMOUNT = 4;
  public static final String CUSTOMER_NUMBER_AND_NAME = "Customer\nNumber\n& Name";
  public static final String SALES_ASSOCIATE = "Sales\nAssociate";
  public static final String CONSIGNMENT_NUMBER = "Transaction\nNumber";
  public static final String CREATION_DATE = "Creation\nDate";
  public static final String AGED_0_15 = "0 - 15 Days\nUnits\nDollars";
  public static final String AGED_16_30 = "16 - 30 Days\nUnits\nDollars";
  public static final String AGED_31_60 = "31 - 60 Days\nUnits\nDollars";
  public static final String AGED_61 = "> 61 Days\nUnits\nDollars";
  public static final String TOTAL = "Total\nUnits\nDollars";
  protected String tableFontName;
  protected Font tableFont;
  protected Font tableTotalsRowFont;
  protected int tableFontSize;
  protected int totalsRowNum;
  protected TableSorter sorter;
  public static final String[] COL_HEADINGS = new String[] {CUSTOMER_NUMBER_AND_NAME
      , SALES_ASSOCIATE, CONSIGNMENT_NUMBER, CREATION_DATE, AGED_0_15, AGED_16_30, AGED_31_60
      , AGED_61, TOTAL
  };
  public static final int CONSIGNMENT_SUMMARY_REPORT_PAGE_SIZE = 25;

  /**
   * put your documentation comment here
   */
  public ConsignmentSummaryReportApplet() {
    ReportUtils.logInfoTsMsg("CONSIGNMENT SUMMARY REPORT STARTED");
    model = new ConsignmentSummaryReportModel(COL_HEADINGS);
    sorter = new TableSorter(model);
    tbl = new JCMSTable(sorter, JCMSTable.VIEW_ROW);
    sorter.setTableHeader(tbl.getTableHeader());
    setMultiLineHeader(3);
    tableFont = tbl.getFont();
    tableFontName = tableFont.getName();
    tableFontSize = tableFont.getSize();
    tableTotalsRowFont = new java.awt.Font(tableFontName, Font.BOLD, tableFontSize);
    multiRowCellRenderer = MultipleRowCellRendererHelper.createMultipleRowCellRenderer();
    MultipleRowCellRendererHelper.setAllRowsCellRendererAction(multiRowCellRenderer
        , new ConsignmentSummaryReportAllRowsRenderAction());
    ReportUtils.setCellRenderer(tbl, multiRowCellRenderer);
    setTitle(MAIN_TITLE);
  }

  public void start() {
    super.start();
    theAppMgr.setSingleEditArea("", "", "");
    theAppMgr.setSingleEditArea(null);
    this.populateScreen(null, null);
  }


  /**
   * put your documentation comment here
   * @param numOfLines
   */
  private void setMultiLineHeader(int numOfLines) {
    JCMSTableHeader tblHeader = (JCMSTableHeader)this.tbl.getTableHeader();
    tblHeader.setPreferredSize(new Dimension(tblHeader.getWidth()
        , (int)(tblHeader.getPreferredSize().height * numOfLines * 0.8)));
    MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer(theAppMgr.getBackgroundColor());
    Enumeration e = this.tbl.getColumnModel().getColumns();
    while (e.hasMoreElements()) {
      ((TableColumn)e.nextElement()).setHeaderRenderer(renderer);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("Consign A Rpt"));
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
    if(startDate != null)
      startDate = Utils.getBeginingOfDay(startDate);
    if(endDate != null)
      endDate = Utils.getEndOfDay(endDate);
    try {
      ReportUtils.logInfoTsMsg("CONSIGNMENT SUMMARY REPORT POPULATE SCREEN");
      model.clear();
      SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      String fullTitle = res.getString(MAIN_TITLE);
      model.setFullTitle(fullTitle);
      lblTitle.setText(fullTitle);
      theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, IApplicationManager.PREV_CANCEL_BUTTON);
      ConsignmentSummaryReportDataTransferObject[] dto = GAReportHelper.
          getConsignmentSummaryByCustomer(theAppMgr, theStore.getId(), startDate, endDate);
      for (int i = 0; i < dto.length; i++) {
        Vector v = new Vector();
        v.add(dto[i].getCustomerNumberAndName());
        CMSEmployee consultant = CMSEmployeeHelper.findById(this.theAppMgr, dto[i].getConsultantId());
        v.add(consultant.getExternalID());
        v.add(dto[i].getTransactionId());
        v.add(dto[i].getCreationDate());
        addUnitsAndDollarsDataToCell(v, dto[i].getAged_0_15());
        addUnitsAndDollarsDataToCell(v, dto[i].getAged_16_30());
        addUnitsAndDollarsDataToCell(v, dto[i].getAged_31_60());
        addUnitsAndDollarsDataToCell(v, dto[i].getAged_61());
        addUnitsAndDollarsDataToCell(v, dto[i].getTotals());
        model.addRow(v);
      }
      tbl.repaint();
    } catch (Exception ex) {
      System.out.println("Exception in method-> POPULATESCREEN" + ex + " msg=" + ex.getMessage());
      ex.printStackTrace();
      ReportUtils.logInfoTsMsg("CONSIGNMENT SUMMARY REPORT: Exception in method-> POPULATESCREEN"
          + ex + " msg=" + ex.getMessage());
      ex.printStackTrace();
      theAppMgr.showErrorDlg("Report Error. Call support.");
    }
  }

  /**
   * put your documentation comment here
   * @param v
   * @param data
   */
  protected void addUnitsAndDollarsDataToCell(Vector v, ConsignmentSummaryItemData data) {
    String str = data.isEmply() ? "" : (data.getUnits() + " / " + data.getDollarAmount());
    v.add(str);
  }

  /**
   * @param summaries
   */
  public static void printData() {}

  /**
   * put your documentation comment here
   * @return
   */
  public double[] getReportColumnWidths() {
    double[] reportColumnWidths = new double[this.model.getColumnCount()];
    if (reportColumnWidths.length == 9) {
      reportColumnWidths[0] = 0.225;
      reportColumnWidths[1] = 0.075;
      reportColumnWidths[2] = 0.10;
      reportColumnWidths[3] = 0.075;
      reportColumnWidths[4] = 0.10;
      reportColumnWidths[5] = 0.10;
      reportColumnWidths[6] = 0.10;
      reportColumnWidths[7] = 0.10;
      reportColumnWidths[8] = 0.125;
    } else {
      for (int i = 0; i < reportColumnWidths.length; i++) {
        reportColumnWidths[i] = 1.0d / reportColumnWidths.length;
      }
    }
    return reportColumnWidths;
  }
}

