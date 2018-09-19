/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.returns;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.TxnHistPanel;
import com.chelseasystems.cs.swing.panel.TxnListHeaderPanel;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.customer.CMSCustomer;
import java.awt.event.*;
import java.awt.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.dlg.*;
import javax.swing.table.DefaultTableCellRenderer;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.rules.BusinessRuleException;
import java.util.Calendar;
import java.util.Date;
import com.chelseasystems.cr.payment.PaymentMgr;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.util.TransactionUtil;
import java.util.Arrays;

/**
 * <p>Title:ReturnTxnHistApplet </p>
 * <p>Description: Lists Customer's transaction history</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 07-27-2005 |Vikram     | 771       | Transaction detail screen should not be shown if   |
 |      |            |           |           | transaction has no line items and not returnable.  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1   | 05-05-2005 |Manpreet   | N/A       | POS_104665_TS_TransactionHistory_Rev0               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ReturnTxnHistApplet extends CMSApplet {
	/**
	 * TransactionHistory list panel
	 */
	private TxnHistPanel pnlTxnHistory;
	private TxnListHeaderPanel pnlHeader;
	private GenericChooserRow[] availPaymentTypes;
	private DefaultTableCellRenderer renderer;
	private CMSTransactionHeader[] transactionHeaders;
	private CMSCustomer cmsCustomer;
	private Date RETURN_PERIOD_START_DATE;
	private static final String CONFIGURATION_FILE = "pos.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	private String txnListHeaderPanelString = null;
	private String txnHistPanelClassName = null;

	/**
	 * put your documentation comment here
	 */
	public void init() {
		try {
			jbInit();
			try {
				ConfigMgr config = new ConfigMgr("returns.cfg");
				Calendar calendar = Calendar.getInstance();
				if (config != null) {
					int iNumReturnDays = config.getInt("RETURN_TXN_PERIOD");
					calendar.add(Calendar.DATE, -iNumReturnDays);
					RETURN_PERIOD_START_DATE = calendar.getTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void start() {
		initMainBtns();
		pnlTxnHistory.clear();
		pnlTxnHistory.updateUI();
		pnlHeader.reset();
		cmsCustomer = null;
		if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
			cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
			// PCR1507 TransactionHistory fix for Armani Japan
			txnListHeaderPanelString = configMgr.getString("TRANSACTION.TXN_LIST_HEADER_PANEL");
			if (txnListHeaderPanelString == null) {
				pnlHeader.setCustomerName(CustomerUtil.getCustomerNameString(cmsCustomer, false));
			} else {
				pnlHeader.setCustomerName(cmsCustomer.getLastName() + " " + cmsCustomer.getFirstName());
			}
		}
		if (theAppMgr.getStateObject("ARM_TXN_HEADERS") != null) {
			transactionHeaders = (CMSTransactionHeader[]) theAppMgr.getStateObject("ARM_TXN_HEADERS");
			pnlHeader.setSearchCriteria((String) theAppMgr.getStateObject("TITLE_STRING"));
			pnlHeader.setDateRange((String) theAppMgr.getStateObject("DATE_STRING"));
			String selectedTranHeaderID = ((String) theAppMgr.getStateObject("TXN_HIST_SELECTEDTXNID"));
			Integer sortColumn = ((Integer) theAppMgr.getStateObject("TXN_HIST_SORTCOLUMN"));
			theAppMgr.removeStateObject("ARM_TXN_HEADERS");
			theAppMgr.removeStateObject("TITLE_STRING");
			theAppMgr.removeStateObject("DATE_STRING");
			theAppMgr.removeStateObject("TXN_HIST_SELECTEDTXNID");
			theAppMgr.removeStateObject("TXN_HIST_SORTCOLUMN");
			loadBySearchedHeaders(sortColumn, selectedTranHeaderID);
		} else if (cmsCustomer != null) {
			loadByCustomerId(cmsCustomer.getId());
			pnlHeader.setSearchCriteria("Customer is: " + cmsCustomer.getFirstName() + " " + cmsCustomer.getLastName());
			pnlHeader.setDateRange("All");
		}
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sCommand = anEvent.getActionCommand();
		if (sCommand.equals("TENDER_TYPE")) {
			if (availPaymentTypes == null || availPaymentTypes.length < 1)
				populatePaymentTypes();
			GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availPaymentTypes, new String[] { res.getString("Tender Types") });
			if (dlg != null) {
				dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
				dlg.setVisible(true);
			}
			if (dlg.isOK()) {
				GenericChooserRow row = dlg.getSelectedRow();
				loadByPaymentType((String) row.getRowKeyData());
				pnlHeader.setSearchCriteria(res.getString("Tender Type") + ": " + (String) dlg.getTable().getModel().getValueAt(dlg.getTable().getSelectedRow(), 0));
				pnlHeader.setDateRange("All");
			}
		} else if (sCommand.equals("ITEM_DETAILS")) {
			TransactionSearchString txnSrchStr = new TransactionSearchString();
			ItemDetailsTxnLookupDlg itmDlg = new ItemDetailsTxnLookupDlg(theAppMgr.getParentFrame(), theAppMgr, txnSrchStr);
			itmDlg.setVisible(true);
			if (txnSrchStr.isSearchRequired()) {
				try {
					txnSrchStr.setStoreId(theStore.getId());
					txnSrchStr.setStartDate(RETURN_PERIOD_START_DATE);
					txnSrchStr.setEndDate(new Date());
					transactionHeaders = CMSTransactionPOSHelper.findBySearchCriteria(theAppMgr, txnSrchStr);
					pnlHeader.setSearchCriteria(TransactionUtil.getTransactionSearchString(txnSrchStr));
					pnlHeader.setDateRange("All");
					loadBySearchedHeaders(null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (sCommand.equals("PREV")) {
			theAppMgr.removeStateObject("TXN_CUSTOMER");
			// theAppMgr.goBack();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void populatePaymentTypes() {
		// String[] payments = CMSPaymentMgr.getAllPaymentKeys();
		Payment[] payments = PaymentMgr.getAllPayments();
		availPaymentTypes = new GenericChooserRow[payments.length + 1];
		int j = 0;
		for (int i = 0; i < payments.length; i++) {
			if (!payments[i].getTransactionPaymentName().equals("STORE_VALUE_CREDIT_MEMO")) {
				String searchPredicate = payments[i].getTransactionPaymentName();
				String searchLabel = payments[i].getGUIPaymentName();
				availPaymentTypes[i + j] = new GenericChooserRow(new String[] { searchLabel }, searchPredicate);
			} else {
				try {
					Payment storeValue = PaymentMgr.getPayment("STORE_VALUE_CARD");
					String searchLabel = res.getString(storeValue.getGUIPaymentName());
					String searchPredicate = storeValue.getTransactionPaymentName();
					availPaymentTypes[i + j] = new GenericChooserRow(new String[] { searchLabel }, searchPredicate);
					j = 1;
					Payment creditMemo = PaymentMgr.getPayment("CREDIT_MEMO");
					String searchLabel1 = res.getString(creditMemo.getGUIPaymentName());
					String searchPredicate1 = creditMemo.getTransactionPaymentName();
					availPaymentTypes[i + j] = new GenericChooserRow(new String[] { searchLabel1 }, searchPredicate1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param sPaymentType
	 */
	private void loadByPaymentType(String sPaymentType) {
		try {
			if (sPaymentType.equals("CREDIT_CARD")) {
				// then check for payments that are credit
				String[] creditPayments = PaymentMgr.getReportCreditPayments(); // .getCreditPaymentDesc();
				Arrays.sort(creditPayments);
				transactionHeaders = CMSTransactionPOSHelper.findByCreditPaymentType(theAppMgr, theStore.getId(), RETURN_PERIOD_START_DATE, new Date(), creditPayments);
			} else {
				transactionHeaders = CMSTransactionPOSHelper.findByPaymentType(theAppMgr, theStore.getId(), RETURN_PERIOD_START_DATE, new Date(), sPaymentType);
			}
			loadBySearchedHeaders(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @param sortColumn
	 * @param selectedTranHeaderID
	 */
	private void loadBySearchedHeaders(Integer sortColumn, String selectedTranHeaderID) {
		pnlTxnHistory.clear();
		pnlTxnHistory.setTxnHeaders(transactionHeaders);
		int sortColumnIntValue = Integer.MIN_VALUE;
		if (sortColumn != null)
			sortColumnIntValue = sortColumn.intValue();
		int selectedItemRowNew = pnlTxnHistory.getAddressModel().sortByColumnType(sortColumnIntValue, selectedTranHeaderID);
		if (selectedItemRowNew > -1)
			pnlTxnHistory.selectRow(selectedItemRowNew);
	}

	/**
	 * put your documentation comment here
	 * @param sCustomerId
	 * @return
	 */
	private boolean loadByCustomerId(String sCustomerId) {
		try {
			transactionHeaders = CMSTransactionPOSHelper.findByCustomerIdAndDates(theAppMgr, sCustomerId, RETURN_PERIOD_START_DATE, new Date());
			if (transactionHeaders == null)
				return false;
			loadBySearchedHeaders(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * put your documentation comment here
	 */
	private void initMainBtns() {
		theAppMgr.showMenu(MenuConst.RETN_TXN_HIST, theOpr);
		selectTransaction();
	}

	/**
	 * put your documentation comment here
	 */
	private void selectTransaction() {
		theAppMgr.setSingleEditArea(res.getString("Select Option"));
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
	 * @return
	 */
	public String getScreenName() {
		return (res.getString("Transaction History"));
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		// PCR1507 TransactionHistory fix for Armani Japan
		txnHistPanelClassName = configMgr.getString("TRANSACTION.TXN_HIST_PANEL");
		if (txnHistPanelClassName == null) {
			pnlTxnHistory = new TxnHistPanel();
		} else {
			pnlTxnHistory = (TxnHistPanel) Class.forName(txnHistPanelClassName).newInstance();
		}
		pnlHeader = new TxnListHeaderPanel();
		renderer = new DefaultTableCellRenderer();
		pnlHeader.setPreferredSize(new Dimension(833, 95));
		this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
		this.getContentPane().add(pnlTxnHistory, BorderLayout.CENTER);
		setAppMgr();
		pnlTxnHistory.addMouseListener(new MouseAdapter() {
			/**
			 * put your documentation comment here
			 * @param me
			 */
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() != 2)
					return;
				txnHdrSelected((CMSTransactionHeader) pnlTxnHistory.getSelectedTransactionHeader());
				// theAppMgr.fireButtonEvent("TXN_DETAILS");
			}
		});
	}

	/**
	 * put your documentation comment here
	 * @param hdr
	 */
	public void txnHdrSelected(CMSTransactionHeader hdr) {
		try {
			CMSCompositePOSTransaction txn = (CMSCompositePOSTransaction) CMSTransactionPOSHelper.findById(theAppMgr, hdr.getId());
			try {
				txn.testIsReturnable();
				theAppMgr.addStateObject("RETURN_TXN", txn);
				theAppMgr.addStateObject("ARM_TXN_HEADERS", transactionHeaders);
				theAppMgr.addStateObject("TITLE_STRING", pnlHeader.getSearchCriteria());
				theAppMgr.addStateObject("DATE_STRING", pnlHeader.getDateRange());
				theAppMgr.addStateObject("TXN_HIST_SORTCOLUMN", new Integer(pnlTxnHistory.getAddressModel().getCurrentSortColumnAndType()));
				theAppMgr.addStateObject("TXN_HIST_SELECTEDTXNID", hdr.getId());
				theAppMgr.fireButtonEvent("TXN_DETAILS");
			} catch (BusinessRuleException bx) {
				theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
			}
		} catch (ClassCastException cce) {
			theAppMgr.showErrorDlg(res.getString("This tranasction has no line items and is not returnable."));
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageDown(MouseEvent e) {
		int selectedRow = pnlTxnHistory.getAddressModel().getAllRows().indexOf(pnlTxnHistory.getSelectedTransactionHeader());
		if (selectedRow < 0)
			selectedRow = pnlTxnHistory.getAddressModel().getLastSelectedTxnRow();
		pnlTxnHistory.pageDown();
		theAppMgr.showPageNumber(e, (pnlTxnHistory.getCurrentPageNumber() + 1), pnlTxnHistory.getTotalPages());
		pnlTxnHistory.selectRowIfInCurrentPage(selectedRow);
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageUp(MouseEvent e) {
		int selectedRow = pnlTxnHistory.getAddressModel().getAllRows().indexOf(pnlTxnHistory.getSelectedTransactionHeader());
		if (selectedRow < 0)
			selectedRow = pnlTxnHistory.getAddressModel().getLastSelectedTxnRow();
		pnlTxnHistory.pageUp();
		theAppMgr.showPageNumber(e, (pnlTxnHistory.getCurrentPageNumber() + 1), pnlTxnHistory.getTotalPages());
		pnlTxnHistory.selectRowIfInCurrentPage(selectedRow);
	}

	/**
	 * put your documentation comment here
	 */
	private void setAppMgr() {
		pnlTxnHistory.setAppMgr(theAppMgr);
		pnlHeader.setAppMgr(theAppMgr);
	}

	/**
	 * put your documentation comment here
	 */
	public void stop() {
	}
}
