/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.TxnHistPanel;
import com.chelseasystems.cs.swing.panel.TxnListHeaderPanel;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import java.awt.event.*;
import java.awt.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.dlg.*;
import javax.swing.table.DefaultTableCellRenderer;
import com.chelseasystems.cs.util.CustomerUtil;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 * <p>Title:CustomerAsisTxnHistoryApplet </p>
 * <p>Description: Lists Customer's transaction history</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Usha Papineni
 * @version 1.0
 */
public class CustomerAsisTxnHistoryApplet extends CMSApplet {
	/**
	 * TransactionHistory list panel
	 */
	private TxnHistPanel pnlTxnHistory;
	private TxnListHeaderPanel pnlHeader;
	private GenericChooserRow[] availPaymentTypes;
	private DefaultTableCellRenderer renderer;
	private CMSTransactionHeader[] transactionHeaders;
	private CMSCustomer cmsCustomer;

	/**
	 * put your documentation comment here
	 */
	public void init() {
		try {
			jbInit();
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
		pnlHeader.reset();
		if (theAppMgr.getStateObject("CUSTOMER_LOOKUP") != null) {
			cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_LOOKUP");
			pnlHeader.setCustomerName(CustomerUtil.getCustomerNameString(cmsCustomer, false));
		}
		if (cmsCustomer != null) {
			loadByCustomerId(cmsCustomer.getId());
		}
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
	}

	/**
	 * put your documentation comment here
	 * @param sCustomerId
	 * @return
	 */
	private boolean loadByCustomerId(String sCustomerId) {
		try {
			String sCustomerNameString = CustomerUtil.getCustomerNameString(cmsCustomer, false);
			String reasonCode = (String) theAppMgr.getStateObject("REASON_CODE");
			if (reasonCode != null && reasonCode.equals("MISC_PAID_IN")) {
				TransactionSearchString txnSrchStr = new TransactionSearchString();
				txnSrchStr.setPayType("CREDIT");
				txnSrchStr.setCustomerId(sCustomerId);
				pnlHeader.setSearchCriteria("Customer = " + sCustomerId + ", " + "Pay Type is CREDIT");
				transactionHeaders = CMSTransactionPOSHelper.findBySearchCriteria(theAppMgr, txnSrchStr);
			} else {
				transactionHeaders = CMSTransactionPOSHelper.findByCustomerIdHeader(theAppMgr, sCustomerId);
				pnlHeader.setSearchCriteria(res.getString("Customer = ") + sCustomerNameString);
			}
			if (transactionHeaders == null || transactionHeaders.length == 0) {
				theAppMgr.showErrorDlgLater(res.getString("No transactions found for : ") + sCustomerNameString);
				return false;
			}
			for (int iCtr = 0; iCtr < transactionHeaders.length; iCtr++) {
				pnlTxnHistory.addTxnHeader(transactionHeaders[iCtr]);
			}
			int selectedItemRowNew = pnlTxnHistory.getAddressModel().doInitialSort(null);
			if (selectedItemRowNew > -1)
				pnlTxnHistory.selectRow(selectedItemRowNew);
			pnlHeader.setSearchCriteria(res.getString("Customer = ") + sCustomerNameString);
			pnlHeader.setDateRange(res.getString("All Dates"));
		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * put your documentation comment here
	 */
	private void initMainBtns() {
		theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
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
		pnlTxnHistory = new TxnHistPanel();
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
				try {
					if (pnlTxnHistory.getSelectedTransactionHeader() == null)
						return;
					CurrencyType txnCurrencyType = pnlTxnHistory.getSelectedTransactionHeader().getTotalAmountDue().getCurrencyType();
					CurrencyType storeCurrencyType = ((CMSStore) theStore).getCurrencyType();
					if (!txnCurrencyType.equals(storeCurrencyType)) {
						theAppMgr.showErrorDlg(res.getString("Transaction can't be loaded, Store doesn't support Transaction's currency type."));
						return;
					}
					PaymentTransaction theTxn = (PaymentTransaction) CMSTransactionPOSHelper.findById(theAppMgr, pnlTxnHistory.getSelectedTransactionHeader().getId());
					theAppMgr.addStateObject("THE_TXN", theTxn);
					theAppMgr.fireButtonEvent("TXN_DETAILS");
				} catch (Exception e) {
					theAppMgr.showExceptionDlg(e);
				}
			}
		});
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageDown(MouseEvent e) {
		pnlTxnHistory.pageDown();
		theAppMgr.showPageNumber(e, (pnlTxnHistory.getCurrentPageNumber() + 1), pnlTxnHistory.getTotalPages());
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageUp(MouseEvent e) {
		pnlTxnHistory.pageUp();
		theAppMgr.showPageNumber(e, (pnlTxnHistory.getCurrentPageNumber() + 1), pnlTxnHistory.getTotalPages());
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
