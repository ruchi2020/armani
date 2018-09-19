/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.TxnHistPanel;
import com.chelseasystems.cs.swing.panel.TxnListHeaderPanel;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet_EUR;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.CurrencyType;
import java.awt.event.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import com.chelseasystems.cr.swing.event.CMSActionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingUtilities;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 * <p>Title:ViewTxnListApplet </p>
 * <p>Description: Lists transaction history</p>
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
 | 1   | 05-12-2005  |Manpreet   | N/A       | POS_104665_TS_TransactionHistory_Rev0               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ViewTxnListApplet extends CMSApplet {
	private TxnHistPanel pnlTxnHistory;
	private TxnListHeaderPanel pnlHeader;
	private DefaultTableCellRenderer renderer;
	private CMSTransactionHeader[] transactionHeaders;
	private PaymentTransaction payTxn;
	private CMSRegister register = null;
	private static final String CONFIGURATION_FILE = "pos.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	private String txnHistPanelClassName = null;
	private String homeStoreId = null;
	private String homeRegisterId = null;

	/**
	 * put your documentation comment here
	 */
	public void init() {
		try {
			jbInit();
			pnlHeader.setNameVisible(false);
			pnlHeader.setTotalVisible(false);
			register = (CMSRegister) theAppMgr.getGlobalObject("REGISTER");
			homeStoreId = register.getStoreId();
			homeRegisterId = register.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void start() {
		initMainBtns();
		payTxn = null;
		pnlTxnHistory.clear();
		pnlHeader.reset();
		if (theAppMgr.getStateObject("ARM_TXN_HEADERS") != null) {
			transactionHeaders = (CMSTransactionHeader[]) theAppMgr.getStateObject("ARM_TXN_HEADERS");
			pnlHeader.setSearchCriteria((String) theAppMgr.getStateObject("TITLE_STRING"));
			pnlHeader.setDateRange((String) theAppMgr.getStateObject("DATE_STRING"));
			String selectedTranHeaderID = ((String) theAppMgr.getStateObject("TXN_HIST_SELECTEDTXNID"));
			Integer sortColumn = ((Integer) theAppMgr.getStateObject("TXN_HIST_SORTCOLUMN"));
			theAppMgr.removeStateObject("ARM_TXN_HEADERS");
			theAppMgr.removeStateObject("TXN_HIST_SELECTEDTXNID");
			theAppMgr.removeStateObject("TXN_HIST_SORTCOLUMN");
			loadBySearchedHeaders(sortColumn, selectedTranHeaderID);
			setHeaders();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void setHeaders() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				try {
					if (theAppMgr.getStateObject("TITLE_STRING") != null) {
						pnlHeader.setSearchCriteria((String) theAppMgr.getStateObject("TITLE_STRING"));
						theAppMgr.removeStateObject("TITLE_STRING");
					}
					if (theAppMgr.getStateObject("DATE_STRING") != null) {
						pnlHeader.setDateRange((String) theAppMgr.getStateObject("DATE_STRING"));
						theAppMgr.removeStateObject("DATE_STRING");
					}
				} catch (Exception ex) {
					theAppMgr.showExceptionDlg(ex);
				}
			}
		});
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sCommand = anEvent.getActionCommand();
		if (sCommand.equals("PREV")) {
			if (theAppMgr.getStateObject("ARM_BACK_TO_SELECT_ITEMS") != null) {
				if (!applySelectedTxn() && pnlTxnHistory.getSelectedTransactionHeader() != null) {
					anEvent.consume();
					return;
				}
			}
			theAppMgr.removeStateObject("TXN_CUSTOMER");
		}
		if (sCommand.equals("CREATE_SALE") || sCommand.equals("ADD_TO_FISCAL")) {
			TransactionHeader[] txnHeader = pnlTxnHistory.getSelectedRows();
			int rowIndices = txnHeader.length;
			if (rowIndices == 0) {
				theAppMgr.showErrorDlg(res.getString("Please select transaction"));
				anEvent.consume();
				return;
			}
			if ((rowIndices > 1) && (sCommand.equals("CREATE_SALE"))) {
				theAppMgr.showErrorDlg(res.getString("Please select only one transaction for creating a new sale"));
				anEvent.consume();
				return;
			}
			if (sCommand.equals("ADD_TO_FISCAL")) {
				if (!applySelectedTxns(sCommand)) {
					anEvent.consume();
					return;
				}
				POSLineItem[] saleItems = null;
				String message = null;
				ArrayList alltxns = (ArrayList) theAppMgr.getStateObject("TXNS_LOADING");
				int itemCount = 0;
				for (int i = 0; alltxns != null && i < alltxns.size(); i++) {
					PaymentTransaction payTxn = (PaymentTransaction) alltxns.get(i);
					if (payTxn instanceof CMSCompositePOSTransaction) {
						saleItems = ((CMSCompositePOSTransaction) payTxn).getSaleLineItemsArray();
						if (saleItems != null) {
							itemCount += saleItems.length;
						}
						message = "No sale items present";
					} else if (payTxn instanceof CMSMiscCollection) {
						message = "Its not a merchandise transaction";
					}
				}
				if (itemCount == 0) {
					theAppMgr.showErrorDlg(message);
					anEvent.consume();
					return;
				}
			} else {
				if (!applySelectedTxns(sCommand)) {
					anEvent.consume();
					return;
				}
				PaymentTransaction payTxn = (PaymentTransaction) theAppMgr.getStateObject("THE_TXN");
				if (payTxn instanceof CMSCompositePOSTransaction) {
					POSLineItem[] saleItems = ((CMSCompositePOSTransaction) payTxn).getSaleLineItemsArray();
					if (saleItems == null || saleItems.length == 0) {
						theAppMgr.showErrorDlg(res.getString("No sale items present"));
						anEvent.consume();
						return;
					}
				}
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param sortColumn
	 * @param selectedTranHeaderID
	 */
	private void loadBySearchedHeaders(Integer sortColumn, String selectedTranHeaderID) {
		if (transactionHeaders == null || transactionHeaders.length == 0)
			return;
		//PCR 1817 - Improvements for Europe
		if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
			ConfigMgr regConfig = new ConfigMgr("register.cfg");
			boolean filterTransactions = false;
			if (regConfig.getString("FILTER_TRANSACTIONS") != null) {
				filterTransactions = "true".equalsIgnoreCase(regConfig.getString("FILTER_TRANSACTIONS"));
				if (filterTransactions) {
					List filteredList = new ArrayList();
					String currentStoreId = null;
					String currentRegisterId = null;
					for (int i = 0; i < transactionHeaders.length; i++) {
						currentStoreId = transactionHeaders[i].getStoreId();
						currentRegisterId = transactionHeaders[i].getRegisterId();
						if (currentStoreId != null && currentRegisterId != null) {
							if (currentStoreId.equalsIgnoreCase(homeStoreId) && currentRegisterId.equalsIgnoreCase(homeRegisterId)) {
								filteredList.add(transactionHeaders[i]);
							}
						}
					}
					if (filteredList != null && filteredList.size() > 0) {
						transactionHeaders = null;
						transactionHeaders = new CMSTransactionHeader[filteredList.size()];
						for (int i = 0; i < filteredList.size(); i++) {
							transactionHeaders[i] = (CMSTransactionHeader)filteredList.get(i);
						}
					}
				}
			}
		}
		pnlTxnHistory.setTxnHeaders(transactionHeaders);
		int selectedItemRowNew = -1;

		// If Sort Column is defined
		// do the column sort
		if (sortColumn != null) {
			selectedItemRowNew = pnlTxnHistory.getAddressModel().sortByColumnType(sortColumn.intValue(), selectedTranHeaderID);
		}
		// Do the default sort.
		else {
			selectedItemRowNew = pnlTxnHistory.getAddressModel().doInitialSort(selectedTranHeaderID);
		}
		if (selectedItemRowNew > -1) {
			pnlTxnHistory.selectRow(selectedItemRowNew);
		} else {
			pnlTxnHistory.selectFirstRow();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void initMainBtns() {
		Integer txnMode = (Integer) theAppMgr.getStateObject("TXN_MODE");
		if ((theAppMgr.getStateObject("TXN_POS") != null) && (txnMode == null || txnMode.intValue() == InitialSaleApplet_EUR.SALE_MODE)) {
			theAppMgr.showMenu(MenuConst.VIEW_TXN_LIST, theOpr);
		} else {
			theAppMgr.showMenu(MenuConst.PREV_CANCEL, theOpr);
		}
		selectTransaction();
	}

	/**
	 * put your documentation comment here
	 */
	private void selectTransaction() {
		theAppMgr.setSingleEditArea(res.getString("Select a transaction to view details"));
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getVersion() {
		return ("$Revision: 1.2 $");
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
		// PCR1326 ViewTransactionHistory fix for Armani Japan
		// EUR Issue: Enable multiple selection on the transaction list for Armani Europe
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
				applySelectedTxn();
				if (payTxn == null)
					return;
				if (theAppMgr.getStateObject("ARM_BACK_TO_SELECT_ITEMS") != null) {
					theAppMgr.fireButtonEvent("PREV");
					return;
				}
				theAppMgr.addStateObject("TXN_HEADER_LIST", pnlTxnHistory.getCMSTxnHeaders());
				theAppMgr.addStateObject("TXN_HEADER_ROW", new Integer(pnlTxnHistory.getSelectedRow()));
				theAppMgr.addStateObject("TITLE_STRING", pnlHeader.getSearchCriteria());
				theAppMgr.addStateObject("DATE_STRING", pnlHeader.getDateRange());

				theAppMgr.addStateObject("TXN_HIST_SORTCOLUMN", new Integer(pnlTxnHistory.getAddressModel().getCurrentSortColumnAndType()));
				theAppMgr.addStateObject("TXN_HIST_SELECTEDTXNID", pnlTxnHistory.getSelectedTransactionHeader().getId());
				theAppMgr.fireButtonEvent("TXN_DETAILS");
			}
		});
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
	 */
	private boolean applySelectedTxn() {
		try {
			if (pnlTxnHistory.getSelectedTransactionHeader() == null)
				return false;
			if (theAppMgr.getStateObject("TXN_POS") != null) {
				String voidId = ((CMSTransactionHeader) pnlTxnHistory.getSelectedTransactionHeader()).getVoidID();
				if (voidId != null && voidId.length() > 0) {
					theAppMgr.showErrorDlg(res.getString("Voided transaction cannot be loaded"));
					return false;
				}
			}
			CurrencyType txnCurrencyType = pnlTxnHistory.getSelectedTransactionHeader().getTotalAmountDue().getCurrencyType();
			CurrencyType storeCurrencyType = ((CMSStore) theStore).getCurrencyType();
			if (!txnCurrencyType.equals(storeCurrencyType)) {
				payTxn = null;
				theAppMgr.showErrorDlg(res.getString("Transaction can't be loaded, Store doesn't support Transaction's currency type."));
				return false;
			}
			payTxn = CMSTransactionPOSHelper.findById(theAppMgr, pnlTxnHistory.getSelectedTransactionHeader().getId());
			if (payTxn == null)
				return false;
			if (theAppMgr.getStateObject("ARM_BACK_TO_SELECT_ITEMS") != null) {
				theAppMgr.addStateObject("ARM_TXN_SELECTED", payTxn);
			} else {
				theAppMgr.addStateObject("THE_TXN", payTxn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean applySelectedTxns(String sCommand) {
		TransactionHeader[] txnHeader = pnlTxnHistory.getSelectedRows();
		for (int i = 0; i < txnHeader.length; i++) {
			if (applySelectedTxn(txnHeader[i], sCommand)) {
				continue;
			}
		}
		return true;
	}

	private boolean applySelectedTxn(TransactionHeader txn, String sCommand) {
		try {
			if (txn == null)
				return false;
			CMSTransactionHeader txnHeader = (CMSTransactionHeader) txn;
			if (txnHeader == null) {
				theAppMgr.showErrorDlg(res.getString("No transaction header" + txnHeader.getId()));
				return false;
			}
			if (theAppMgr.getStateObject("TXN_POS") != null) {
				String voidId = txnHeader.getVoidID();
				if (voidId != null && voidId.length() > 0) {
					theAppMgr.showErrorDlg(res.getString("Voided transaction cannot be loaded" + txnHeader.getId()));
					return false;
				}
			}
			CurrencyType txnCurrencyType = txnHeader.getTotalAmountDue().getCurrencyType();
			CurrencyType storeCurrencyType = ((CMSStore) theStore).getCurrencyType();
			if (!txnCurrencyType.equals(storeCurrencyType)) {
				payTxn = null;
				theAppMgr.showErrorDlg(res.getString("Transaction can't be loaded, Store doesn't support Transaction's currency type." + txnHeader.getId()));
				return false;
			}
			payTxn = CMSTransactionPOSHelper.findById(theAppMgr, txnHeader.getId());
			if (payTxn == null)
				return false;
			if (sCommand.equals("ADD_TO_FISCAL")) {
				if (theAppMgr.getStateObject("TXNS_LOADING") == null) {
					theAppMgr.addStateObject("TXNS_LOADING", new ArrayList());
				}
				((ArrayList) theAppMgr.getStateObject("TXNS_LOADING")).add(payTxn);
			}
			if (sCommand.equals("CREATE_SALE")) {
				if (theAppMgr.getStateObject("ARM_BACK_TO_SELECT_ITEMS") != null) {
					theAppMgr.addStateObject("ARM_TXN_SELECTED", payTxn);
				} else {
					theAppMgr.addStateObject("THE_TXN", payTxn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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

	/**
	 * MP: Home pressed at customer display exits transaction with no message
	 * @return
	 */
	public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}
		
		/*Added by Yves Agbessi (05-Dec-2017)
		 * Handles the posting of the Sign Off event for Fiscal Solutions Service
		 * START--
		 * */
		boolean goToHomeView = (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?")));
		if(goToHomeView){
			
			ARMFSBridge.postARMSignOffTransaction((CMSEmployee)theOpr);
		}
		
		return goToHomeView;
		/*
		 * --END
		 * */
		
		
	}
}
