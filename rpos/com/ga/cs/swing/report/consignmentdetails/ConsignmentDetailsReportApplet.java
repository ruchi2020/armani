/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.ga.cs.swing.report.consignmentdetails;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.table.TableColumn;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IReceiptAppManager;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.bean.table.JCMSTableHeader;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.report.Reportable;
import com.chelseasystems.cs.swing.mask.CMSMaskConstants;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.ga.cs.pos.GAReportHelper;
import com.ga.cs.pos.datatranssferobject.ConsignmentDetailsReport;
import com.ga.cs.swing.model.ConsignmentDetailsReportModel;
import com.ga.cs.swing.report.ArmaniReportPrinter;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.cs.utils.ReportUtils;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 * put your documentation comment here
 */
public class ConsignmentDetailsReportApplet extends BaseReportApplet implements
		Reportable {
	public static final String MAIN_TITLE = "Consignment Details Report";
	public static final boolean DEBUG = false;
	private static int FILTER_BY_ALL = 0;
	private static int FILTER_BY_OPEN = 1;
	private static int FILTER_BY_CLOSE = 2;
	// PCR from US 1923
	private static int FILTER_BY_SELL_THROUGH = 3;
	private static int FILTER_BY_SELL_THROUGH_ASSOCIATE = 4;
	private ConsignmentDetailsReport[] data = null;
	private List rows;
	private int currentReportFilter = FILTER_BY_ALL;
	private String currentAssociateId = null;
	private Date endDate = null;
	/**
	 * put your documentation comment here
	 */
	public ConsignmentDetailsReportApplet() {
		super();
		setTitle(MAIN_TITLE);
		this.model = new ConsignmentDetailsReportModel();
		this.tbl = new JCMSTable(this.model, JCMSTable.VIEW_ROW);
		setMultiLineHeader(2);
		// int totalWidth = this.getViewPortWidth();
		// double[] ratio = this.getReportColumnWidths();
		// for (int i = 0; i < ratio.length; i++) {
		// tbl.getColumnModel().getColumn(i).setPreferredWidth((int)(totalWidth
		// * ratio[i]));
		// }
		// Issue # 1013
		initializeColInc();
	}

	// Issue # 1013
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
	 * 
	 * @param Colname
	 * @param Wid
	 */
	public void initColInc(int Colname, int Wid) {
		// setColumnWidth(Colname, Wid);
		int width = tbl.getColumnModel().getColumn(Colname).getWidth();
		tbl.getColumnModel().getColumn(Colname).setPreferredWidth(Wid);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param numOfLines
	 */
	private void setMultiLineHeader(int numOfLines) {
		JCMSTableHeader tblHeader = (JCMSTableHeader) this.tbl.getTableHeader();
		tblHeader
				.setPreferredSize(new Dimension(
						tblHeader.getWidth(),
						(int) (tblHeader.getPreferredSize().height * numOfLines * 0.8)));
		MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer(
				theAppMgr.getBackgroundColor());
		Enumeration e = this.tbl.getColumnModel().getColumns();
		while (e.hasMoreElements()) {
			((TableColumn) e.nextElement()).setHeaderRenderer(renderer);
		}
	}

	/**
	 * @see com.ga.cs.swing.report.BaseReportApplet#populateScreen(java.util.Date,
	 *      java.util.Date)
	 */
	public void populateScreen(Date startDate, Date endDate) {
		try {
			ReportUtils
					.logInfoTsMsg("CONSIGNMENT DETAILS REPORT POPULATE SCREEN");
			this.model.clear();
			this.model.setFullTitle(this.getPrintTitle());
			this.lblTitle.setText(this.getScreenTitle());
			// commented this code for PCR 1923
			// theAppMgr.showMenu(MenuConst.CONSIGNMENT_DETAIL_REPORT, theOpr
			// , IReceiptAppManager.PREV_CANCEL_BUTTON);
			// end
			// added this code for PCR 1923
			theAppMgr.showMenu(MenuConst.CONSIGNMENT_DETAIL_REPORT, theOpr,
					IReceiptAppManager.PREV_CANCEL_BUTTON);
			this.prepareRows();
			for (Iterator iter = rows.iterator(); iter.hasNext();) {
				String[] row = (String[]) iter.next();
				// Show only detail rows and grand total row on screen
				// if (row[9].length() != 0 || !iter.hasNext()) {
				this.model.addList(row);
				// }
			}
		} catch (Exception ex) {
			ReportUtils
					.logInfoTsMsg("CONSIGNMENT DETAILS REPORT: Exception in method-> POPULATESCREEN"
							+ ex + " msg=" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getScreenName() {
		return (res.getString("Consign Detail Rpt"));
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
	 * 
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
	 * 
	 * @param storeId
	 * @param begin
	 * @param end
	 */
	private void fetchData(String storeId, Date begin, Date end) {
		try {
			this.data = GAReportHelper.getConsignmentDetails(theAppMgr,
					storeId, begin, end);
			if (DEBUG) {
				System.out.println("\n\ndata: " + data + " of size "
						+ data.length);
				for (int i = 0; i < data.length; i++) {
					ConsignmentDetailsReport row = data[i];
					System.out.println("  row " + i + ": " + row);
				}
			}
		} catch (Exception ex) {
			ReportUtils
					.logInfoTsMsg("CONSIGNMENT DETAILS REPORT: Exception in method-> POPULATESCREEN"
							+ ex + " msg=" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	private ConsignmentDetailsReport[] applyCurrentReportFilter() {
		if (currentReportFilter == FILTER_BY_OPEN) {
			return ConsignmentDetailsReport.filterOpenByAssociate(this.data,
					currentAssociateId);
		} else if (currentReportFilter == FILTER_BY_CLOSE) {
			return ConsignmentDetailsReport.filterCloseByAssociate(this.data,
					currentAssociateId);
		} else if (currentReportFilter == FILTER_BY_SELL_THROUGH) { // added this for PCR 1923
			return ConsignmentDetailsReport.filterBySellThrough(this.data,
					currentAssociateId);
		} else if (currentReportFilter == FILTER_BY_SELL_THROUGH_ASSOCIATE) { // added this for PCR 1923
			return ConsignmentDetailsReport.filterBySellThroughByAssociate(
					this.data, currentAssociateId);
		} else {
			return ConsignmentDetailsReport.filterByAssociate(this.data,
					currentAssociateId);
		}
	}

	/**
	 * put your documentation comment here
	 * @throws CurrencyException 
	 */
	private void prepareRows() throws CurrencyException {
		// Collecting details by "Cust Last Name, Cust First Name, Consignment
		// (transaction) ID"
		int returnedItems = 0; // PCR 1923
		double sellThroughPercentage = 0.0d; // PCR 1923
		int itemsSold = 0;
		ArmCurrency origSoldAmount = null;
		ConsignmentDetailsReport[] currentData = this
				.applyCurrentReportFilter();
		Map reportRows = new TreeMap();
		for (int i = 0; i < currentData.length; i++) {
			ConsignmentDetailsReport report = currentData[i];
			String key = report.bean.customerLastName + ','
					+ report.bean.customerFirstName + ','
					+ report.bean.transactionId;
			List reports = (List) reportRows.get(key);
			if (reports == null) {
				reports = new ArrayList();
			}
			reports.add(report);
			reportRows.put(key, reports);
		}
		// Prepare row data (header row, detail rows and total row for each
		// consignment)
		List rows = new ArrayList();
		Set totalSet = new HashSet(); // For grand total

		Vector nonMerchandiseItemIdVec = new Vector();
		ConfigMgr configMgr = new ConfigMgr("item.cfg");
		String miscItemKeys = configMgr.getString("MISC_ITEM_KEYS");
		StringTokenizer strTok = new StringTokenizer(miscItemKeys, ",");
		while (strTok.hasMoreTokens()) {
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
			ConsignmentTotals totals = getTotals(reports);
			for (Iterator iterator = reports.iterator(); iterator.hasNext();) {
				ConsignmentDetailsReport report = (ConsignmentDetailsReport) iterator
						.next();
				if (!headerAdded) {
				   	String[] headerRow = new String[] {
							res.getString("Customer:"),
							report.bean.customerLastName + ", "
									+ report.bean.customerFirstName,
							"- " + report.bean.customerId, "", "", "", "", "",
							"", "" };
					rows.add(headerRow);
					headerAdded = true;
					}
				if (nonMerchandiseItemIdVec.contains(report.bean.itemId)){ 
					try {
						totals.totalOrigAmt = totals.totalOrigAmt
								.subtract(report.bean.origAmt);
						totals.totalOrigQty = totals.totalOrigQty
								- report.bean.origQty;
						totals.totalRemainAmt = totals.totalRemainAmt
								.subtract(report.bean.remainingAmt);
						totals.totalRemainQty = totals.totalRemainQty
								- report.bean.remainingQty;
										
					} catch (CurrencyException ex) {
						ex.printStackTrace();
					}
				} else {
					//This report considers NOTONFILE items(60009999)
					 if ((report.bean.itemStatus.equalsIgnoreCase("OPEN")||report.bean.itemStatus.equalsIgnoreCase("RETURNED")||report.bean.itemStatus.equalsIgnoreCase("SOLD"))) {
							if (report.getTransactionId()!=null) {
								totals.transactionCount.add(report.getTransactionId());
							}
						}
					 if(report.bean.itemStatus.equalsIgnoreCase("SOLD")){
						 itemsSold++;
					 }	 
					 if(report.bean.itemStatus.equalsIgnoreCase("RETURNED")){
						 returnedItems++;
					 }
					if (report.bean.itemStatus.equalsIgnoreCase("OPEN")&&((report.bean.dateCong).after(startDate)&& (report.bean.dateCong).before(endDate))
							||(report.bean.itemStatus.equalsIgnoreCase("SOLD")&&((report.bean.dateCong).after(startDate)&& (report.bean.dateCong).before(endDate)))
							||(report.bean.itemStatus.equalsIgnoreCase("RETURNED")&&((report.bean.dateCong).after(startDate)&& (report.bean.dateCong).before(endDate)))){
							if(report.bean.customerId !=null){
								totals.noOfClients.add(report.bean.customerId);
							}
					}
					
					if(report.bean.itemStatus.equalsIgnoreCase("SOLD")&&((report.bean.dateCong).after(startDate)&& (report.bean.dateCong).before(endDate))){
						if(report.bean.customerId!=null){
							totals.noOfClientsPurchased.add(report.bean.customerId);
						}
					  if(origSoldAmount == null){
						  origSoldAmount = report.bean.origAmt;
					  }else
							 origSoldAmount = origSoldAmount.add(report.bean.origAmt);
					  	}
					
				rows.add(report.toStringArray()); 
				}
				
			}
			if(totals.totalOrigQty!=0){
			String[] totalRow = ConsignmentDetailsReport.getTotalArray(res
					.getString("Total Qty/Amt:"), totals.totalOrigQty,
					totals.totalOrigAmt, totals.totalRemainQty,
					totals.totalRemainAmt);
			rows.add(totalRow);
			rows.add(new String[] { "", "", "", "", "", "", "", "", "", "" });
			totalSet.add(totals);
			}
		}
		// Calculate and prepare grand total row
		int grandTotalOrigQty = 0;
		int grandTotalRemainQty = 0;
		int transCount = 0;
		int clientNo = 0;
		int clientNoPurchased =0;
		double sellThroughBy = 0.0d;
		String sellThPercentage = null;
		String soldItemsAmountFormatted = null;
		
		ArmCurrency grandTotalOrigAmt = null;
		ArmCurrency grandTotalRemainAmt = null;
		try {
			for (Iterator iter = totalSet.iterator(); iter.hasNext();) {
				ConsignmentTotals totals = (ConsignmentTotals) iter.next();
				grandTotalOrigQty += totals.totalOrigQty;
				grandTotalRemainQty += totals.totalRemainQty;
			
				
				if(totals.transactionCount!=null)
					transCount += totals.transactionCount.size();
				
				if(totals.noOfClients!=null)
					clientNo +=totals.noOfClients.size();
				
				if(totals.noOfClientsPurchased!=null)
					clientNoPurchased += totals.noOfClientsPurchased.size();
				
				if (grandTotalOrigAmt == null) {
					grandTotalOrigAmt = totals.totalOrigAmt;
					grandTotalRemainAmt = totals.totalRemainAmt;
				} else {
					grandTotalOrigAmt = grandTotalOrigAmt
							.add(totals.totalOrigAmt);
					grandTotalRemainAmt = grandTotalRemainAmt
							.add(totals.totalRemainAmt);
				}
			}
		} catch (CurrencyException e) {
			throw new IllegalStateException(
					"CurrencyException occurs in grand totalling");
		}
		String[] grandTotalRow = (grandTotalOrigQty == 0) ? new String[] {
				res.getString("Grand Total:"), "", "", "0", "0", "", "", "0",
				"0", "" } : ConsignmentDetailsReport.getTotalArray(res
				.getString("Grand Total:"), grandTotalOrigQty,
				grandTotalOrigAmt, grandTotalRemainQty, grandTotalRemainAmt);
		rows.add(grandTotalRow);
		this.rows = rows;
		// PCR from US Calculate and prepare Sell through report 1923

		if (currentReportFilter == FILTER_BY_SELL_THROUGH
				||currentReportFilter == FILTER_BY_SELL_THROUGH_ASSOCIATE) {
			double totSoldA=0.0d;
			double actualSoldAmount = 0.0d;
			String sellThroughByPercent = null;
			if (itemsSold != 0) {
					double noOfsoldItems = (double) itemsSold;
					double newGrandTotalOrigQty = (double) grandTotalOrigQty;
					sellThroughPercentage = (noOfsoldItems/newGrandTotalOrigQty)*100;
								
					DecimalFormat df = new DecimalFormat("00.##%");
					if(sellThroughPercentage!=0.0d){
						sellThroughPercentage=sellThroughPercentage/100;
						sellThPercentage = df.format(sellThroughPercentage);
					}
					
					float totOrigAmt = (float)grandTotalOrigAmt.doubleValue();
					if(origSoldAmount!=null){
						DecimalFormat ds = new DecimalFormat("$00.##");
						totSoldA =  origSoldAmount.doubleValue()/100;
						actualSoldAmount = origSoldAmount.doubleValue();
						soldItemsAmountFormatted = ds.format(actualSoldAmount);
				    }
				    sellThroughBy = (totSoldA/totOrigAmt)*100;
				    if(sellThroughBy!=0.0d){
				    sellThroughByPercent = df.format(sellThroughBy);
				    }
			}
			if(sellThPercentage == null){
				sellThPercentage = "0.0%";
			}
			if(sellThroughByPercent == null){
		    	sellThroughByPercent="0.0%";
			}
			if(soldItemsAmountFormatted ==null)
			{
				soldItemsAmountFormatted ="$0.00";
			}
			 String[] totalSellThroughRow = (transCount ==0) ? new String [] {
					res.getString("SellThrough:"), "", "", "0", "0", "", "", "0",
					"0", ""} :ConsignmentDetailsReport
								.getTotalSellThroughArray("SellThrough:", "TotalTrns:", transCount,grandTotalOrigQty, "ReturnedItems:",
								returnedItems, "SoldItms:", itemsSold, "ST U:",
								sellThPercentage);
				rows.add(totalSellThroughRow);
			String[] totalSellThroughRowAddition =(transCount==0)? new String []{res.getString("Actual$Sold:"), "", "", "0", "0", "", "", "0",
					"0", ""} :ConsignmentDetailsReport.getTotalSellThroughAdditionArray("Actual$Sold",soldItemsAmountFormatted,
							"ClientsConsigned:",clientNo,"ClientsPurchased:",clientNoPurchased,"ST $:",sellThroughByPercent);
				rows.add(totalSellThroughRowAddition);
				}
			this.rows = rows;
			
		}
		// end of code for PCR 1923
	

	/**
	 * two new buttons added for PCR 1923 put your documentation comment here
	 * new buttons SELL_THROUGH and SELL_THROUGH_BY_ASSOCIATE added
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("PRINT")) {
			if (model.getRowCount() == 0) {
				theAppMgr.showErrorDlg(res
						.getString("There is no data to print."));
				return;
			}
			ArmaniReportPrinter reportPrinter = new ArmaniReportPrinter(this);
			reportPrinter.setLandscape();
			reportPrinter.print();
		} else if (sAction.equals("ALL_CONSIGNMENT_DETAIL")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_ALL;
			currentAssociateId = null;
			populateScreen(startDate, endDate);
		} else if (sAction.equals("CLOSED_CONSIGNMENT_DETAIL_ONLY")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_CLOSE;
			currentAssociateId = null;
			populateScreen(startDate, endDate);
		} else if (sAction.equals("OPEN_CONSIGNMENT_DETAIL_ONLY")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_OPEN;
			currentAssociateId = null;
			populateScreen(startDate, endDate);
		} else if (sAction.equals("ALL_CONSIGNMENT_DETAIL_BY_ASSOCIATE")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_ALL;
			this.enterAssocaiteId();
		} else if (sAction.equals("CLOSED_CONSIGNMENT_DETAIL_BY_ASSOCIATE")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_CLOSE;
			this.enterAssocaiteId();
		} else if (sAction.equals("OPEN_CONSIGNMENT_DETAIL_BY_ASSOCIATE")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_OPEN;
			this.enterAssocaiteId();
		} else if (sAction.equals("SELL_THROUGH")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_SELL_THROUGH;
			currentAssociateId = null;
			populateScreen(startDate, endDate);
		} else if (sAction.equals("SELL_THROUGH_BY_ASSOCIATE")) {
			currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_SELL_THROUGH_ASSOCIATE;
			this.enterAssocaiteId();
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void enterAssocaiteId() {
		theAppMgr.setSingleEditArea(res.getString("Enter Associate Number."),
				"ASSOCIATE", IApplicationManager.NO_MASK);
	}

	/**
	 * put your documentation comment here
	 * 
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
	 * 
	 * @param command
	 * @param date
	 */
	public void editAreaEvent(String command, Date date) {
		if (command.equals("STARTDATE")) {
			startDate = date;
			theAppMgr
					.setSingleEditArea(
							res
									.getString("Enter end date. (MM/DD/YYYY)  or  \"C\" for Calendar."),
							"ENDDATE", new Date(),
							CMSMaskConstants.CALENDAR_MASK);
		}
		if (command.equals("ENDDATE")) {
			if (date.compareTo(startDate) >= 0) {
				endDate = date;
				currentReportFilter = ConsignmentDetailsReportApplet.FILTER_BY_ALL;
				currentAssociateId = null;
				fetchData(theStore.getId(), startDate, endDate);
				populateScreen(startDate, date);
				theAppMgr
						.setSingleEditArea(
								res
										.getString("Enter start date. (MM/DD/YYYY)  or  \"C\" for Calendar."),
								"STARTDATE", new Date(),
								CMSMaskConstants.CALENDAR_MASK);
			} else { // end date is less than start date; request re-entry
				theAppMgr
						.setSingleEditArea(
								res
										.getString("End date is earlier than start date. Please re-enter end date. (MM/DD/YYYY)  or  \"C\" for Calendar."),
								"ENDDATE", new Date(),
								CMSMaskConstants.CALENDAR_MASK);
			}
		}
	}

	// Overriding implementation from superclass. Report elements are basically
	// the table model, with
	// additional header row and total row for each consignment (ie.
	// transaction).
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
	 * 
	 * @param reports
	 * @return
	 */
	private ConsignmentTotals getTotals(List reports) {
		try {
			ConsignmentTotals totals = new ConsignmentTotals();
			for (Iterator iter = reports.iterator(); iter.hasNext();) {
				ConsignmentDetailsReport report = (ConsignmentDetailsReport) iter
						.next();
				totals.totalOrigQty += report.bean.origQty;
				totals.totalRemainQty += report.bean.remainingQty;
				
				if (totals.totalOrigAmt == null) {
					totals.totalOrigAmt = report.bean.origAmt;
					totals.totalRemainAmt = report.bean.remainingAmt;
				} else {
					totals.totalOrigAmt = totals.totalOrigAmt
							.add(report.bean.origAmt);
					totals.totalRemainAmt = totals.totalRemainAmt
							.add(report.bean.remainingAmt);
				}
			}
			return totals;
		} catch (CurrencyException e) {
			throw new IllegalStateException(
					"CurrencyException occurs in totalling");
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	private String getScreenTitle() {
		SimpleDateFormat sdf = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
		StringBuffer sb = new StringBuffer();
		if (this.currentReportFilter == ConsignmentDetailsReportApplet.FILTER_BY_OPEN) {
			sb.append(res.getString("OPEN"));
			sb.append(" ");
		} else if (this.currentReportFilter == ConsignmentDetailsReportApplet.FILTER_BY_CLOSE) {
			sb.append(res.getString("CLOSED"));
			sb.append(" ");
		}
		sb.append(res.getString("Consignment Details"));
		sb.append(" ");
		sb.append(sdf.format(startDate));
		sb.append("-");
		sb.append(sdf.format(endDate));
		if (this.currentAssociateId != null
				&& this.currentAssociateId.length() > 0) {
			sb.append(" ");
			sb.append("ID:");
			sb.append(this.currentAssociateId);
		}
		return sb.toString();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	private String getPrintTitle() {
		SimpleDateFormat sdf = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
		StringBuffer sb = new StringBuffer();
		sb.append(res.getString("HDR_Store"));
		sb.append(" ");
		sb.append(theStore.getId());
		sb.append(" ");
		if (this.currentReportFilter == ConsignmentDetailsReportApplet.FILTER_BY_OPEN) {
			sb.append(res.getString("OPEN"));
			sb.append(" ");
		} else if (this.currentReportFilter == ConsignmentDetailsReportApplet.FILTER_BY_CLOSE) {
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
		if (this.currentAssociateId != null
				&& this.currentAssociateId.length() > 0) {
			sb.append(" ");
			sb.append("Associate:");
			sb.append(this.currentAssociateId);
		}
		return sb.toString();
	}
	
	private class ConsignmentTotals {
		public int totalOrigQty = 0;
		public int totalRemainQty = 0;
		public ArmCurrency totalOrigAmt = null;
		public ArmCurrency totalRemainAmt = null;
		public int soldItms =0;
		public int retItms = 0;
		TreeSet transactionCount= new TreeSet();
		TreeSet noOfClients = new TreeSet();
		TreeSet noOfClientsPurchased = new TreeSet();
	}

}
