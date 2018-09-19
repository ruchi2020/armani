/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2006 | Sandhya   | N/A       | Reservation Details Report                         |
 -------------------------------------------------------------------------------------------------- 
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.swing.report.reservationdetails;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IReceiptAppManager;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.bean.table.JCMSTableHeader;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.report.Reportable;
import com.chelseasystems.cs.swing.mask.CMSMaskConstants;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.pos.datatranssferobject.ReservationDetailsReport;
import com.ga.cs.swing.model.ReservationDetailsReportModel;
import com.ga.cs.swing.report.ArmaniReportPrinter;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.swing.report.consignmentdetails.MultiLineHeaderRenderer;
import com.ga.cs.utils.ReportUtils;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 * put your documentation comment here
 */
public class ReservationDetailsReportApplet extends BaseReportApplet implements Reportable {

	static final long serialVersionUID = 0;

	public static final String MAIN_TITLE = "Reservation Details Report";
	public static final boolean DEBUG = false;
	private static int FILTER_BY_ALL = 0;
	private static int FILTER_BY_OPEN = 1;
	private static int FILTER_BY_CLOSE = 2;
	private ReservationDetailsReport[] data = null;
	private List rows;
	private int currentReportFilter = FILTER_BY_ALL;
	private String currentAssociateId = null;
	private Date endDate = null;

	/**
	 * put your documentation comment here
	 */
	public ReservationDetailsReportApplet() {
		super();
		setTitle(MAIN_TITLE);
		this.model = new ReservationDetailsReportModel();
		this.tbl = new JCMSTable(this.model, JCMSTable.VIEW_ROW);
		setMultiLineHeader(2);
		initializeColInc();
	}

	protected void initializeColInc() {
		initColInc(0, 100);
		initColInc(1, 140);
		initColInc(2, 100);
		initColInc(3, 50);
		initColInc(4, 100);
		initColInc(5, 70);
		initColInc(6, 80);
		initColInc(7, 60);
		initColInc(8, 100);
		initColInc(9, 200);
	}

	/**
	 * put your documentation comment here
	 * @param Colname
	 * @param Wid
	 */
	public void initColInc(int Colname, int Wid) {
		int width = tbl.getColumnModel().getColumn(Colname).getWidth();
		tbl.getColumnModel().getColumn(Colname).setPreferredWidth(Wid);
	}

	/**
	 * put your documentation comment here
	 * @param numOfLines
	 */
	private void setMultiLineHeader(int numOfLines) {
		JCMSTableHeader tblHeader = (JCMSTableHeader) this.tbl.getTableHeader();
		tblHeader.setPreferredSize(new Dimension(tblHeader.getWidth(), (int) (tblHeader.getPreferredSize().height * numOfLines * 0.8)));
		MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer(theAppMgr.getBackgroundColor());
		Enumeration e = this.tbl.getColumnModel().getColumns();
		while(e.hasMoreElements()) {
			((TableColumn) e.nextElement()).setHeaderRenderer(renderer);
		}
	}

	/**
	 * @see com.ga.cs.swing.report.BaseReportApplet#populateScreen(java.util.Date, java.util.Date)
	 */
	public void populateScreen(Date startDate, Date endDate) {
		try {
			ReportUtils.logInfoTsMsg("RESERVATION DETAILS REPORT POPULATE SCREEN");
			this.model.clear();
			this.model.setFullTitle(this.getPrintTitle());
			this.lblTitle.setText(this.getScreenTitle());
			theAppMgr.showMenu(MenuConst.RESERVATION_DETAIL_REPORT, theOpr, IReceiptAppManager.PREV_CANCEL_BUTTON);
			this.prepareRows();
			for (Iterator iter = rows.iterator(); iter.hasNext();) {
				String[] row = (String[]) iter.next();
				this.model.addList(row);
			}
		} catch (Exception ex) {
			ReportUtils.logInfoTsMsg("RESERVATION DETAILS REPORT: Exception in method-> POPULATESCREEN " + ex + " msg=" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getScreenName() {
		return (res.getString("Reservation Detail Rpt"));
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getVersion() {
		return ("$Revision: 1.1.6.4 $");
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public double[] getReportColumnWidths() {
		int columnCount = this.model.getColumnCount();
		if (columnCount == 10) {
			double[] reportColumnWidths = new double[columnCount];
			reportColumnWidths[0] = 0.10;
			reportColumnWidths[1] = 0.15;
			reportColumnWidths[2] = 0.10;
			reportColumnWidths[3] = 0.07;
			reportColumnWidths[4] = 0.10;
			reportColumnWidths[5] = 0.07;
			reportColumnWidths[6] = 0.08;
			reportColumnWidths[7] = 0.08;
			reportColumnWidths[8] = 0.10;
			reportColumnWidths[9] = 0.15;
			return reportColumnWidths;
		} else {
			return super.getReportColumnWidths();
		}
	}

	/**
	 * put your documentation comment here
	 * @param storeId
	 * @param begin
	 * @param end
	 */
	private void fetchData(String storeId, Date begin, Date end) {
		try {
			this.data = GAReportHelper.getReservationDetails(theAppMgr, storeId, begin, end);
			if (DEBUG) {
				System.out.println("\n\ndata: " + data + " of size " + data.length);
				for (int i = 0; i < data.length; i++) {
					ReservationDetailsReport row = data[i];
					System.out.println("  row " + i + ": " + row);
				}
			}
		} catch (Exception ex) {
			ReportUtils.logInfoTsMsg("RESERVATION DETAILS REPORT: Exception in method-> FETCHDATA " + ex + " msg=" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private ReservationDetailsReport[] applyCurrentReportFilter() {
		if (currentReportFilter == FILTER_BY_OPEN) {
			return ReservationDetailsReport.filterOpenByAssociate(this.data, currentAssociateId);
		} else if (currentReportFilter == FILTER_BY_CLOSE) {
			return ReservationDetailsReport.filterCloseByAssociate(this.data, currentAssociateId);
		} else {
			return ReservationDetailsReport.filterByAssociate(this.data, currentAssociateId);
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void prepareRows() {
		ReservationDetailsReport[] currentData = this.applyCurrentReportFilter();
		Map reportRows = new TreeMap();
		for (int i = 0; i < currentData.length; i++) {
			ReservationDetailsReport report = currentData[i];
			String key = report.bean.customerLastName + ',' + report.bean.customerFirstName + ',' + report.bean.transactionId;
			List reports = (List) reportRows.get(key);
			if (reports == null) {
				reports = new ArrayList();
			}
			reports.add(report);
			reportRows.put(key, reports);
		}
		// Prepare row data (header row, detail rows and total row for each reservation)
		List rows = new ArrayList();
		Set totalSet = new HashSet(); // For grand total

		Vector nonMerchandiseItemIdVec = new Vector();
		ConfigMgr configMgr = new ConfigMgr("item.cfg");
		String miscItemKeys = configMgr.getString("MISC_ITEM_KEYS");
		StringTokenizer strTok = new StringTokenizer(miscItemKeys, ",");
		while(strTok.hasMoreTokens()) {
			String misckey = strTok.nextToken();
			if (misckey.trim().equalsIgnoreCase("NOTONFILE"))
				continue;
			String miscItemId = configMgr.getString(misckey + ".BASE_ITEM");
			if (miscItemId != null)
				nonMerchandiseItemIdVec.add(miscItemId);
		}
		for (Iterator iter = reportRows.keySet().iterator(); iter.hasNext();) {
			String curKey = (String) iter.next();
			boolean headerAdded = false;
			List reports = (List) reportRows.get(curKey);
			ReservationTotals totals = getTotals(reports);
			for (Iterator iterator = reports.iterator(); iterator.hasNext();) {
				ReservationDetailsReport report = (ReservationDetailsReport) iterator.next();
				if (!headerAdded) {
					String[] headerRow = new String[] { res.getString("Customer:"), report.bean.customerLastName + ", " + report.bean.customerFirstName, "- " + report.bean.customerId, "", "", "", "",
							"", "", "" };
					rows.add(headerRow);
					headerAdded = true;
				}
				if (nonMerchandiseItemIdVec.contains(report.bean.itemId)) {
					try {
						totals.totalOrigAmt = totals.totalOrigAmt.subtract(report.bean.origAmt);
						totals.totalOrigQty = totals.totalOrigQty - report.bean.origQty;
						totals.totalRemainAmt = totals.totalRemainAmt.subtract(report.bean.remainingAmt);
						totals.totalRemainQty = totals.totalRemainQty - report.bean.remainingQty;
					} catch (CurrencyException ex) {
						ex.printStackTrace();
					}
				} else {
					rows.add(report.toStringArray());
				}
			}
			String[] totalRow = ReservationDetailsReport.getTotalArray(res.getString("Total Qty/Amt:"), totals.totalOrigQty, totals.totalOrigAmt, totals.totalRemainQty, totals.totalRemainAmt);
			rows.add(totalRow);
			rows.add(new String[] { "", "", "", "", "", "", "", "", "", "" });
			totalSet.add(totals);
		}
		//Calculate and prepare grand total row
		int grandTotalOrigQty = 0;
		int grandTotalRemainQty = 0;
	ArmCurrency grandTotalOrigAmt = null;
	ArmCurrency grandTotalRemainAmt = null;
		try {
			for (Iterator iter = totalSet.iterator(); iter.hasNext();) {
				ReservationTotals totals = (ReservationTotals) iter.next();
				grandTotalOrigQty += totals.totalOrigQty;
				grandTotalRemainQty += totals.totalRemainQty;
				if (grandTotalOrigAmt == null) {
					grandTotalOrigAmt = totals.totalOrigAmt;
					grandTotalRemainAmt = totals.totalRemainAmt;
				} else {
					grandTotalOrigAmt = grandTotalOrigAmt.add(totals.totalOrigAmt);
					grandTotalRemainAmt = grandTotalRemainAmt.add(totals.totalRemainAmt);
				}
			}
		} catch (CurrencyException e) {
			throw new IllegalStateException("CurrencyException occurs in grand totalling");
		}
		String[] grandTotalRow = (grandTotalOrigQty == 0) ? new String[] { res.getString("Grand Total:"), "", "", "0", "0", "", "", "0", "0", "" } : ReservationDetailsReport.getTotalArray(
				res.getString("Grand Total:"), grandTotalOrigQty, grandTotalOrigAmt, grandTotalRemainQty, grandTotalRemainAmt);
		rows.add(grandTotalRow);
		this.rows = rows;
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("PRINT")) {
			if (model.getRowCount() == 0) {
				theAppMgr.showErrorDlg(res.getString("There is no data to print."));
				return;
			}
			ArmaniReportPrinter reportPrinter = new ArmaniReportPrinter(this);
			reportPrinter.setLandscape();
			reportPrinter.print();
		} else if (sAction.equals("ALL_RESERVATION_DETAIL")) {
			currentReportFilter = ReservationDetailsReportApplet.FILTER_BY_ALL;
			currentAssociateId = null;
			populateScreen(startDate, endDate);
		} else if (sAction.equals("CLOSED_RESERVATION_DETAIL_ONLY")) {
			currentReportFilter = ReservationDetailsReportApplet.FILTER_BY_CLOSE;
			currentAssociateId = null;
			populateScreen(startDate, endDate);
		} else if (sAction.equals("OPEN_RESERVATION_DETAIL_ONLY")) {
			currentReportFilter = ReservationDetailsReportApplet.FILTER_BY_OPEN;
			currentAssociateId = null;
			populateScreen(startDate, endDate);
		} else if (sAction.equals("ALL_RESERVATION_DETAIL_BY_ASSOCIATE")) {
			currentReportFilter = ReservationDetailsReportApplet.FILTER_BY_ALL;
			this.enterAssocaiteId();
		} else if (sAction.equals("CLOSED_RESERVATION_DETAIL_BY_ASSOCIATE")) {
			currentReportFilter = ReservationDetailsReportApplet.FILTER_BY_CLOSE;
			this.enterAssocaiteId();
		} else if (sAction.equals("OPEN_RESERVATION_DETAIL_BY_ASSOCIATE")) {
			currentReportFilter = ReservationDetailsReportApplet.FILTER_BY_OPEN;
			this.enterAssocaiteId();
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void enterAssocaiteId() {
		theAppMgr.setSingleEditArea(res.getString("Enter Associate Number."), "ASSOCIATE", IApplicationManager.NO_MASK);
	}

	/**
	 * put your documentation comment here
	 * @param command
	 * @param string
	 */
	public void editAreaEvent(String command, String string) {
		if (command.equals("ASSOCIATE")) {
			currentAssociateId = string;
			populateScreen(startDate, endDate);
		}
	}

	/**
	 * put your documentation comment here
	 * @param command
	 * @param date
	 */
	public void editAreaEvent(String command, Date date) {
		if (command.equals("STARTDATE")) {
			startDate = date;
			theAppMgr.setSingleEditArea(res.getString("Enter end date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "ENDDATE", new Date(), CMSMaskConstants.CALENDAR_MASK);
		}
		if (command.equals("ENDDATE")) {
			if (date.compareTo(startDate) >= 0) {
				endDate = date;
				currentReportFilter = ReservationDetailsReportApplet.FILTER_BY_ALL;
				currentAssociateId = null;
				fetchData(theStore.getId(), startDate, endDate);
				populateScreen(startDate, date);
				theAppMgr.setSingleEditArea(res.getString("Enter start date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "STARTDATE", new Date(), CMSMaskConstants.CALENDAR_MASK);
			} else { // end date is less than start date; request re-entry
				theAppMgr.setSingleEditArea(res.getString("End date is earlier than start date. Please re-enter end date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "ENDDATE", new Date(),
						CMSMaskConstants.CALENDAR_MASK);
			}
		}
	}

	// Overriding implementation from superclass. Report elements are basically the table model, with
	// additional header row and total row for each reservation (ie. transaction).
	public String[][] getReportElements() {
		int length = this.rows.size();
		int width = this.model.getColumnCount();
		String[][] reportElements = new String[length][width];
		if (DEBUG) {
			System.out.println("\n\n\n--------elements dump--------");
		}
		for (int i = 0; i < rows.size(); i++) {
			String[] row = (String[]) rows.get(i);
			for (int j = 0; j < row.length; j++) {
				reportElements[i][j] = row[j];
				if (DEBUG) {
					System.out.print(reportElements[i][j] + "\t");
					;
				}
			}
			if (DEBUG) {
				System.out.println(" ");
			}
		}
		if (DEBUG) {
			System.out.println("--------elements end---------\n\n\n");
		}
		return reportElements;
	}

	/**
	 * put your documentation comment here
	 * @param reports
	 * @return
	 */
	private ReservationTotals getTotals(List reports) {
		try {
			ReservationTotals totals = new ReservationTotals();
			for (Iterator iter = reports.iterator(); iter.hasNext();) {
				ReservationDetailsReport report = (ReservationDetailsReport) iter.next();
				totals.totalOrigQty += report.bean.origQty;
				totals.totalRemainQty += report.bean.remainingQty;
				if (totals.totalOrigAmt == null) {
					totals.totalOrigAmt = report.bean.origAmt;
					totals.totalRemainAmt = report.bean.remainingAmt;
				} else {
					totals.totalOrigAmt = totals.totalOrigAmt.add(report.bean.origAmt);
					totals.totalRemainAmt = totals.totalRemainAmt.add(report.bean.remainingAmt);
				}
			}
			return totals;
		} catch (CurrencyException e) {
			throw new IllegalStateException("CurrencyException occurs in totalling");
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private String getScreenTitle() {
		SimpleDateFormat sdf = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
		StringBuffer sb = new StringBuffer();
		if (this.currentReportFilter == ReservationDetailsReportApplet.FILTER_BY_OPEN) {
			sb.append(res.getString("OPEN"));
			sb.append(" ");
		} else if (this.currentReportFilter == ReservationDetailsReportApplet.FILTER_BY_CLOSE) {
			sb.append(res.getString("CLOSED"));
			sb.append(" ");
		}
		sb.append(res.getString("Reservation Details"));
		sb.append(" ");
		sb.append(sdf.format(startDate));
		sb.append("-");
		sb.append(sdf.format(endDate));
		if (this.currentAssociateId != null && this.currentAssociateId.length() > 0) {
			sb.append(" ");
			sb.append("ID:");
			sb.append(this.currentAssociateId);
		}
		return sb.toString();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private String getPrintTitle() {
		SimpleDateFormat sdf = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
		StringBuffer sb = new StringBuffer();
		sb.append(res.getString("HDR_Store"));
		sb.append(" ");
		sb.append(theStore.getId());
		sb.append(" ");
		if (this.currentReportFilter == ReservationDetailsReportApplet.FILTER_BY_OPEN) {
			sb.append(res.getString("OPEN"));
			sb.append(" ");
		} else if (this.currentReportFilter == ReservationDetailsReportApplet.FILTER_BY_CLOSE) {
			sb.append(res.getString("CLOSED"));
			sb.append(" ");
		}
		sb.append(res.getString(this.getTitle()));
		sb.append(" ");
		sb.append("For");
		sb.append(" ");
		sb.append(sdf.format(startDate));
		sb.append(" - ");
		sb.append(sdf.format(endDate));
		if (this.currentAssociateId != null && this.currentAssociateId.length() > 0) {
			sb.append(" ");
			sb.append("Associate:");
			sb.append(this.currentAssociateId);
		}
		return sb.toString();
	}

	private class ReservationTotals {
		public int totalOrigQty = 0;
		public int totalRemainQty = 0;
		public ArmCurrency totalOrigAmt = null;
		public ArmCurrency totalRemainAmt = null;
	}
}
