/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.transaction;

import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.model.ViewTxnModel;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cs.swing.mask.CMSMaskConstants;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cs.swing.dlg.ItemDetailsTxnLookupDlg;
import com.chelseasystems.cs.swing.dlg.FiscalTxnLookupDlg;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cr.config.ConfigMgr;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 */
public class ViewTxnApplet extends CMSApplet {
	// public class ViewTxnApplet extends JApplet {
	private SimpleDateFormat dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
	private String dateString = null;
	private Date date = null;
	private int searchType = 0;
	private ViewTxnModel.SearchSelection searchSelection = null;
	protected static String searchTitle = "";
	private static final int SEARCH_PAYMENT = 1;
	private static final int SEARCH_DISCOUNT = 2;
	private static final int SEARCH_TRANSACTION = 3;
	private static final int SEARCH_AMOUNT = 4;
	private static final int SEARCH_CUSTOMER = 5;
	private static final int SEARCH_CONSULTANT = 6;
	private static final int SEARCH_ALL = 7;
	private static final int SEARCH_ID = 8;
	private static final int SEARCH_OPERATOR = 9;
	private static final int SEARCH_SHIPPING = 10;
	private static final String NOTHING_FOUND = res.getString("Cannot find any matching transations.");
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlEast = new JPanel();
	private JPanel pnlWest = new JPanel();
	private JPanel pnlCenter = new JPanel();
	ViewTxnModel model = new ViewTxnModel();
	ViewTxnTable table = new ViewTxnTable(model, JCMSTable.SELECT_CELL);
	JCMSLabel lblSearchType = new JCMSLabel();
	private CMSEmployee employee;
	private INIFile file;

	/**
	 * put your documentation comment here
	 */
	public ViewTxnApplet() {
	}

	/**
	 * put your documentation comment here
	 */
	public void init() {
		try {
			jbInit();
			file = new INIFile(FileMgr.getLocalFile("config", "pos.cfg"), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void stop() {
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		JCMSLabel jCMSLabel1 = new JCMSLabel();
		jCMSLabel1.setPreferredSize(new Dimension(200, 20));
		jCMSLabel1.setText(res.getString("Search Type") + ": ");
		jCMSLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
		jCMSLabel1.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblSearchType.setPreferredSize(new Dimension(200, 20));
		lblSearchType.setFont(theAppMgr.getTheme().getTextFieldFont());
		this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
		pnlNorth.add(jCMSLabel1, null);
		pnlNorth.add(lblSearchType, null);
		this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		this.getContentPane().add(pnlEast, BorderLayout.EAST);
		this.getContentPane().add(pnlWest, BorderLayout.WEST);
		this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout(new BorderLayout());
		pnlCenter.add(table, BorderLayout.CENTER);
		pnlCenter.add(table.getTableHeader(), BorderLayout.NORTH);
		pnlNorth.setPreferredSize(new Dimension(10, 35));
		pnlNorth.setBackground(theAppMgr.getBackgroundColor());
		pnlSouth.setBackground(theAppMgr.getBackgroundColor());
		pnlEast.setBackground(theAppMgr.getBackgroundColor());
		pnlWest.setBackground(theAppMgr.getBackgroundColor());
		pnlCenter.setBackground(theAppMgr.getBackgroundColor());
		table.setAppMgr(theAppMgr);
		table.addMouseListener(new MouseAdapter() {
			/**
			 * put your documentation comment here
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				clickEvent(e);
			}
		});
		// table.setBackground(theAppMgr.getBackgroundColor());
		ViewTxnCellRenderer cellRenderer = new ViewTxnCellRenderer();
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
		table.addComponentListener(new java.awt.event.ComponentAdapter() {
			/**
			 * put your documentation comment here
			 * @param e
			 */
			public void componentResized(ComponentEvent e) {
				model.setRowsShown(table.getHeight() / table.getRowHeight());
			}
		});
		table.registerKeyboardAction(table, JCMSTable.SELECT_CMD, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JTable.WHEN_FOCUSED);
	}

	/**
	 * put your documentation comment here
	 */
	public void start() {
		model.clear();
		searchTitle = null;
		dateString = null;
		lblSearchType.setText("");
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		theAppMgr.showMenu(MenuConst.VIEW_TXN, theOpr, theAppMgr.CANCEL_BUTTON);
		if (theAppMgr.getStateObject("ARM_TXN_HEADERS") != null) {
			final CMSTransactionHeader[] transactionHeaders = (CMSTransactionHeader[]) theAppMgr.getStateObject("ARM_TXN_HEADERS");
			theAppMgr.removeStateObject("ARM_TXN_HEADERS");
			SwingUtilities.invokeLater(new Runnable() {
				/**
				 * put your documentation comment here
				 */
				public void run() {
					try {
						displaySearchResults(transactionHeaders);
					} catch (Exception ex) {
						theAppMgr.showExceptionDlg(ex);
					}
				}
			});
		} else if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
			lblSearchType.setText(res.getString("CUSTOMER"));
			CMSCustomer cust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
			// theAppMgr.removeStateObject("TXN_CUSTOMER");
			populateCustomer(cust);
		} else
			enterTxnNumber();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getScreenName() {
		return (res.getString("View Transaction"));
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		lblSearchType.setText(sAction);
		if (sAction.equals("CANCEL")) {
		} else if (sAction.equals("ALL")) {
			model.clear();
			searchType = SEARCH_ALL;
			searchSelection = null;
			enterDate();
		} else if (sAction.equals("ASSOCIATE")) {
			model.clear();
			searchType = SEARCH_CONSULTANT;
			searchSelection = null;
			// enterDate();
			enterConsultantName();
		} else if (sAction.equals("CASHIER")) {
			model.clear();
			searchType = SEARCH_OPERATOR;
			searchSelection = null;
			// enterDate();
			enterOperatorName();
		} else if (sAction.equals("AMOUNT")) {
			model.clear();
			searchType = SEARCH_AMOUNT;
			searchSelection = null;
			enterDate();
		} else if (sAction.equals("ID")) {
			model.clear();
			searchType = SEARCH_ID;
			searchSelection = null;
			enterTxnNumber();
		} else if (sAction.equals("CUSTOMER")) {
			model.clear();
			searchType = SEARCH_CUSTOMER;
			searchSelection = null;
			dateString = res.getString("All Dates");
			// enterCustomer();
		} else if (sAction.equals("DISCOUNT")) {
			model.clear();
			searchType = SEARCH_DISCOUNT;
			searchSelection = null;
			enterSelectTable();
			loadDiscountChoices();
		} else if (sAction.equals("TRANSACTION")) {
			model.clear();
			searchType = SEARCH_TRANSACTION;
			searchSelection = null;
			enterSelectTable();
			try {
				loadTransactionChoices();
			} catch (Exception e) {
				theAppMgr.showExceptionDlg(e);
				e.printStackTrace();
			}
		} else if (sAction.equals("TENDER")) {
			model.clear();
			searchType = SEARCH_PAYMENT;
			searchSelection = null;
			enterSelectTable();
			loadPaymentChoices();
		} else if (sAction.equals("SEND_SALE")) {
			model.clear();
			searchType = SEARCH_SHIPPING;
			searchSelection = null;
			enterDate();
		} else if (sAction.equals("ITEM_DETAILS")) {
			model.clear();
			searchSelection = null;
			TransactionSearchString txnSrchStr = new TransactionSearchString();
			ItemDetailsTxnLookupDlg itmDlg = new ItemDetailsTxnLookupDlg(theAppMgr.getParentFrame(), theAppMgr, txnSrchStr);
			itmDlg.setVisible(true);
			if (txnSrchStr.isSearchRequired()) {
				try {
					txnSrchStr.setStoreId(theStore.getId());
					// txnSrchStr.buildQuery()
					CMSTransactionHeader[] transactionHeaders = CMSTransactionPOSHelper.findBySearchCriteria(theAppMgr, txnSrchStr);
					if (transactionHeaders == null || transactionHeaders.length < 1) {
						theAppMgr.showErrorDlg(res.getString("No matching transactions found"));
						return;
					}
					searchTitle = TransactionUtil.getTransactionSearchString(txnSrchStr);
					dateString = res.getString("Between ") + dateFormat.format(transactionHeaders[transactionHeaders.length - 1].getSubmitDate());
					dateString += res.getString(" and ") + dateFormat.format(transactionHeaders[0].getSubmitDate());
					displaySearchResults(transactionHeaders);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (sAction.equals("FISCAL_SEARCH")) {
			model.clear();
			searchSelection = null;
			FiscalTxnLookupDlg fiscalDlg = new FiscalTxnLookupDlg(theAppMgr.getParentFrame(), theAppMgr);
			fiscalDlg.setVisible(true);
			TransactionSearchString txnSrchStr = fiscalDlg.getTransactionSearchString();
			if (txnSrchStr.isSearchRequired()) {
				try {
					// txnSrchStr.setStoreId(theStore.getId());
					CMSTransactionHeader[] transactionHeaders = CMSTransactionPOSHelper.findBySearchCriteria(theAppMgr, txnSrchStr);
					if (transactionHeaders == null || transactionHeaders.length < 1) {
						theAppMgr.showErrorDlg(res.getString("No matching transactions found"));
						return;
					}
					searchTitle = TransactionUtil.getTransactionSearchString(txnSrchStr);
					dateString = res.getString("Between ") + dateFormat.format(transactionHeaders[transactionHeaders.length - 1].getSubmitDate());
					dateString += res.getString(" and ") + dateFormat.format(transactionHeaders[0].getSubmitDate());
					displaySearchResults(transactionHeaders);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/*
		 * else if (sAction.equals("TENDER")) { theAppMgr.showMenu(MenuConst.TENDER_MENU_US,theOpr,null); }
		 */
	}

	/**
	 * put your documentation comment here
	 */
	private void loadDiscountChoices() {
		String[] discountTypes = DiscountMgr.getDiscountTypes();
		model.clear();
		for (int i = 0; i < discountTypes.length; i++) {
			ViewTxnModel.SearchSelection searchSelection = model.new SearchSelection();
			// searchSelection.SearchPredicate = discountTypes[i];
			searchSelection.SearchPredicate = DiscountMgr.getLabel(discountTypes[i]);
			searchSelection.SearchLabel = DiscountMgr.getLabel(discountTypes[i]);
			model.addSearchButton(searchSelection);
		}
		table.setRowSelectionInterval(0, 0);
		table.setColumnSelectionInterval(0, 0);
		table.repaint();
		table.requestFocus(); // this is necessary for registered kbrd enter action listener to activate in JCMSTable
		// if enter is pressed before moving within cells (can't leave focus on cell). djr
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void loadTransactionChoices() throws Exception {
		model.clear();
		StringTokenizer st;
		String transTypes = file.getValue("TRANS_TYPES");
		st = new StringTokenizer(transTypes, ",");
		while(st.hasMoreElements()) {
			String key = (String) st.nextElement();
			String transTypeDesc = res.getString(file.getValue(key, "DESC_KEY", ""));
			String transTypeCode = file.getValue(key, "CODE", "");
			ViewTxnModel.SearchSelection searchSelection = model.new SearchSelection();
			searchSelection.SearchPredicate = transTypeCode; // Trans type code
			searchSelection.SearchLabel = transTypeDesc; // Trans type description
			model.addSearchButton(searchSelection);
		}
		// theAppMgr.showErrorDlg("Waiting for Mark to add a way to get these...");
		// enterTxnNumber();
		table.setRowSelectionInterval(0, 0);
		table.setColumnSelectionInterval(0, 0);
		table.repaint();
		table.requestFocus(); // this is necessary for registered kbrd enter action listener to activate in JCMSTable
		// if enter is pressed before moving within cells (can't leave focus on cell). djr
	}

	/**
	 * put your documentation comment here
	 */
	private void loadPaymentChoices() {
		model.clear();
		Payment[] payments = null;
		ConfigMgr config = new ConfigMgr("payment.cfg");
		ArrayList txnPayments = new ArrayList();
		String strTxnPayments = config.getString("TXN_PAYMENTS");
		if (strTxnPayments != null && strTxnPayments.length() > 0) {
			StringTokenizer st = new StringTokenizer(strTxnPayments, ",");
			while(st.hasMoreTokens()) {
				try {
					txnPayments.add(PaymentMgr.getPayment(st.nextToken()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			payments = new Payment[txnPayments.size()];
			payments = (Payment[]) txnPayments.toArray(payments);
		} else {
			payments = PaymentMgr.getAllPayments();
		}
		for (int i = 0; i < payments.length; i++) {
			if (!payments[i].getTransactionPaymentName().equals("STORE_VALUE_CREDIT_MEMO")) {
				ViewTxnModel.SearchSelection searchSelection = model.new SearchSelection();
				searchSelection.SearchLabel = res.getString(payments[i].getGUIPaymentName());
				searchSelection.SearchPredicate = payments[i].getTransactionPaymentName();
				model.addSearchButton(searchSelection);
			} else {
				try {
					ViewTxnModel.SearchSelection searchSelection = model.new SearchSelection();
					Payment storeValue = PaymentMgr.getPayment("STORE_VALUE_CARD");
					searchSelection.SearchLabel = res.getString(storeValue.getGUIPaymentName());
					searchSelection.SearchPredicate = storeValue.getTransactionPaymentName();
					model.addSearchButton(searchSelection);
					ViewTxnModel.SearchSelection searchSelection1 = model.new SearchSelection();
					Payment creditMemo = PaymentMgr.getPayment("CREDIT_MEMO");
					searchSelection1.SearchLabel = res.getString(creditMemo.getGUIPaymentName());
					searchSelection1.SearchPredicate = creditMemo.getTransactionPaymentName();
					model.addSearchButton(searchSelection1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		table.setRowSelectionInterval(0, 0);
		table.setColumnSelectionInterval(0, 0);
		table.repaint();
		table.requestFocus(); // this is necessary for registered kbrd enter action listener to activate in JCMSTable
		// if enter is pressed before moving within cells (can't leave focus on cell). djr
	}

	/**
	 * put your documentation comment here
	 * @param me
	 */
	public void clickEvent(MouseEvent me) {
		if (table.getSelectedRow() >= 0) {
			String text = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
			if (text.trim().length() == 0) {
				table.setRowSelectionInterval(0, 0);
				table.setColumnSelectionInterval(0, 0);
				return;
			}
			searchSelection = model.getSearchButton(table.getSelectedRow(), table.getSelectedColumn());
			enterDate();
		}
	}

	/**
	 * put your documentation comment here
	 * @param command
	 * @param currency
	 */
	public void editAreaEvent(String command, ArmCurrency currency) {
		try {
			if (command.equals("AMOUNT")) {
				// searchString = currency.stringValue();
				CMSTransactionHeader[] transactionHeaders = CMSTransactionPOSHelper.findByExactAmount(theAppMgr, theStore.getId(), date, date, currency);
				// PaymentTransaction[] paymentTransactions = null;
				if (transactionHeaders != null && transactionHeaders.length > 0) {
					searchTitle = res.getString("View transactions for amount") + " " + currency.formattedStringValue();
					displaySearchResults(transactionHeaders);
				} else {
					theAppMgr.showErrorDlg(NOTHING_FOUND);
					model.clear();
					enterTxnNumber();
				}
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * put your documentation comment here
	 * @param command
	 * @param aDate
	 */
	public void editAreaEvent(String command, Object oValue) {
		System.out.println("OBJECT EDIT AREA EVENT");
		CMSTransactionHeader[] transactionHeaders = null;
		try {
			if (command.equals("CUSTOMER")) {
				Telephone value = (Telephone) oValue;
				CMSCustomer[] custList = (CMSCustomer[]) CMSCustomerHelper.findByTelephone(theAppMgr, value);
				if (custList != null && custList.length > 0) {
					if (custList.length == 1) {
						// populateCustomer(custList[0].getId());
						String cust = custList[0].getId();
						transactionHeaders = CMSTransactionPOSHelper.findByCustomerIdHeader(theAppMgr, cust);
						if (transactionHeaders != null && transactionHeaders.length > 0) {
							searchTitle = res.getString("View customer transactions for") + " " + ((Telephone) value).getFormattedNumber();
							displaySearchResults(transactionHeaders);
						} else {
							theAppMgr.showErrorDlg(NOTHING_FOUND);
							model.clear();
							enterTxnNumber();
						}
					} else {
						theAppMgr.addStateObject("CUSTOMER_LIST", custList);
						theAppMgr.fireButtonEvent("CUST_LIST");
						return;
					}
				} else {
					theAppMgr.showErrorDlg(res.getString("Cannot find customer."));
					enterCustomer();
					return;
				}
			}
		} catch (TooManySearchResultsException e) {
			theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
			enterCustomer();
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * put your documentation comment here
	 * @param command
	 * @param date
	 */
	public void editAreaEvent(String command, Date date) {
		System.out.println("DATE EDIT AREA EVENT");
		CMSTransactionHeader[] transactionHeaders = null;
		try {
			if (command.equals("DATE")) {
				dateString = dateFormat.format(date);
				this.date = date;
				switch (searchType) {
					case SEARCH_PAYMENT:
						if (searchSelection.SearchPredicate.equals("CREDIT_CARD")) {
							// then check for payments that are credit
							String[] creditPayments = PaymentMgr.getReportCreditPayments(); // .getCreditPaymentDesc();
							Arrays.sort(creditPayments);
							transactionHeaders = CMSTransactionPOSHelper.findByCreditPaymentType(theAppMgr, theStore.getId(), date, date, creditPayments);
							if (transactionHeaders != null && transactionHeaders.length > 0) {
								searchTitle = res.getString("View credit card transactions");
								displaySearchResults(transactionHeaders);
							} else {
								theAppMgr.showErrorDlg(NOTHING_FOUND);
								model.clear();
								enterTxnNumber();
							}
						} else {
							System.out.println("@@@@@Search Predicate : " + searchSelection.SearchPredicate);
							transactionHeaders = CMSTransactionPOSHelper.findByPaymentType(theAppMgr, theStore.getId(), date, date, searchSelection.SearchPredicate);
							if (transactionHeaders != null && transactionHeaders.length > 0) {
								searchTitle = res.getString("View") + " " + searchSelection.SearchLabel + " " + res.getString("transactions");
								displaySearchResults(transactionHeaders);
							} else {
								theAppMgr.showErrorDlg(NOTHING_FOUND);
								model.clear();
								enterTxnNumber();
							}
						}
						break;
					case SEARCH_DISCOUNT:
						transactionHeaders = CMSTransactionPOSHelper.findByDiscountType(theAppMgr, theStore.getId(), date, date, searchSelection.SearchPredicate);
						if (transactionHeaders != null && transactionHeaders.length > 0) {
							searchTitle = res.getString("View") + " " + searchSelection.SearchLabel + " " + res.getString("discount transactions");
							displaySearchResults(transactionHeaders);
						} else {
							theAppMgr.showErrorDlg(NOTHING_FOUND);
							model.clear();
							enterTxnNumber();
						}
						break;
					case SEARCH_TRANSACTION:
						// System.out.println("Search string is " + searchString);
						transactionHeaders = CMSTransactionPOSHelper.findByTransactionType(theAppMgr, theStore.getId(), date, date, searchSelection.SearchPredicate);
						if (transactionHeaders != null && transactionHeaders.length > 0) {
							searchTitle = res.getString("View") + " " + searchSelection.SearchLabel + " " + res.getString("transactions");
							displaySearchResults(transactionHeaders);
						} else {
							theAppMgr.showErrorDlg(NOTHING_FOUND);
							model.clear();
							enterTxnNumber();
						}
						break;
					case SEARCH_SHIPPING:
						// System.out.println("Search string is " + searchString);
						transactionHeaders = CMSTransactionPOSHelper.findByShippingRequested(theAppMgr, theStore.getId(), date, date);
						if (transactionHeaders != null && transactionHeaders.length > 0) {
							searchTitle = res.getString("View transactions with shipping");
							displaySearchResults(transactionHeaders);
						} else {
							theAppMgr.showErrorDlg(NOTHING_FOUND);
							model.clear();
							enterTxnNumber();
						}
						break;
					case SEARCH_AMOUNT:
						enterAmount();
						break; /*
								 * case SEARCH_CUSTOMER: enterCustomer(); break;
								 */
					case SEARCH_CONSULTANT:
						// enterConsultantName();
						String empID = employee.getExternalID();
						transactionHeaders = CMSTransactionPOSHelper.findByConsultantID(theAppMgr, theStore.getId(), date, date, empID);
						if (transactionHeaders != null && transactionHeaders.length > 0) {
							searchTitle = res.getString("View consultant transactions for") + " " + employee.getFirstName() + " " + employee.getLastName();
							displaySearchResults(transactionHeaders);
						} else {
							theAppMgr.showErrorDlg(NOTHING_FOUND);
							model.clear();
							enterTxnNumber();
						}
						break;
					case SEARCH_OPERATOR:
						// enterOperatorName();
						String oprID = employee.getExternalID(); // .getId();
						transactionHeaders = CMSTransactionPOSHelper.findByOperatorID(theAppMgr, theStore.getId(), date, date, oprID);
						if (transactionHeaders != null && transactionHeaders.length > 0) {
							searchTitle = res.getString("View operator transactions for") + " " + employee.getFirstName() + " " + employee.getLastName();
							displaySearchResults(transactionHeaders);
						} else {
							theAppMgr.showErrorDlg(NOTHING_FOUND);
							model.clear();
							enterTxnNumber();
						}
						break;
					case SEARCH_ALL:
						transactionHeaders = CMSTransactionPOSHelper.findByDate(theAppMgr, theStore.getId(), date, date);
						if (transactionHeaders != null && transactionHeaders.length > 0) {
							searchTitle = res.getString("View all transactions");
							displaySearchResults(transactionHeaders);
						} else {
							theAppMgr.showErrorDlg(NOTHING_FOUND);
							model.clear();
							enterTxnNumber();
						}
						break;
				}
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
		/*
		 * if (transactionHeaders != null && transactionHeaders.length > 0) displaySearchResults(transactionHeaders); else {
		 * theAppMgr.showErrorDlg(NOTHING_FOUND); enterTxnNumber(); }
		 */
	}

	/**
	 * @param command
	 * @param value
	 */
	public void editAreaEvent(String command, String value) {
		CMSTransactionHeader[] transactionHeaders = null;
		try {
			if (command.equals("CONSULTANT")) {
				employee = CMSEmployeeHelper.findByShortName(theAppMgr, value);
				if (employee == null) {
					theAppMgr.showErrorDlg(res.getString("Cannot find employee."));
					enterConsultantName();
					return;
				}
				enterDate();
			}
			if (command.equals("OPERATOR")) {
				employee = CMSEmployeeHelper.findByShortName(theAppMgr, value);
				if (employee == null) {
					theAppMgr.showErrorDlg(res.getString("Cannot find employee."));
					enterOperatorName();
					return;
				}
				enterDate();
			}
			if (command.equals("TXN_ID")) {
				PaymentTransaction txn = CMSTransactionPOSHelper.findById(theAppMgr, value);
				// PaymentTransaction txn = null;
				if (txn != null) {
					theAppMgr.removeStateObject("TXN_HEADER_LIST");
					theAppMgr.addStateObject("THE_TXN", txn);
					theAppMgr.fireButtonEvent("OK"); // goto TxnDetailApplet
					return;
				} else {
					theAppMgr.showErrorDlg(NOTHING_FOUND);
					model.clear();
					enterTxnNumber();
				}
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
		/*
		 * if (transactionHeaders != null && transactionHeaders.length > 0) displaySearchResults(transactionHeaders); else {
		 * theAppMgr.showErrorDlg(NOTHING_FOUND); enterTxnNumber(); }
		 */
	}

	/**
	 */
	private void displaySearchResults(CMSTransactionHeader[] transactionHeaders) {
		theAppMgr.addStateObject("ARM_TXN_HEADERS", transactionHeaders);
		// theAppMgr.addStateObject("TXN_HEADER", transactionHeaders);
		if (dateString != null && dateString.length() > 0)
			theAppMgr.addStateObject("DATE_STRING", dateString);
		if (searchTitle != null && searchTitle.length() > 0)
			theAppMgr.addStateObject("TITLE_STRING", searchTitle);
		theAppMgr.fireButtonEvent("OK_LIST");
	}

	/**
	 * Method is inside an invoke late to allow time to come back from Customer 
	 * List Applet or else the StateManager will try to invoke "OK_LIST" on it.
	 * @see displaySearchResults()
	 * @param id
	 *            Customer ID
	 */
	private void populateCustomer(final CMSCustomer cust) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * put your documentation comment here
			 */
			public void run() {
				try {
					CMSTransactionHeader[] transactionHeaders = CMSTransactionPOSHelper.findByCustomerIdHeader(theAppMgr, cust.getId());
					String sNameString = com.chelseasystems.cs.util.CustomerUtil.getCustomerNameString(cust, false);
					searchTitle = res.getString("Customer is ") + sNameString;
					dateString = res.getString("All Dates");
					// - MSB (01/25/06) Use CustomerUtil to get name string.
					// searchTitle = "Customer is " + cust.getFirstName() + " " + cust.getLastName();
					// dateString = "All";
					if (transactionHeaders == null || transactionHeaders.length == 0) {
						theAppMgr.showErrorDlgLater(res.getString("No transactions found for : ") + sNameString);
						return;
					}
					displaySearchResults(transactionHeaders);
				} catch (Exception ex) {
					theAppMgr.showExceptionDlg(ex);
				}
			}
		});
	}

	/**
	 * put your documentation comment here
	 */
	private void enterDate() {
		theAppMgr.setSingleEditArea(res.getString("Enter date. (MM/DD/YYYY)  or  \"C\" for Calendar."), "DATE", new Date(), CMSMaskConstants.CALENDAR_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterTxnNumber() {
		lblSearchType.setText(res.getString("SELECT OPTION"));
		theAppMgr.setSingleEditArea(res.getString("Enter transaction ID or select option."), "TXN_ID", theAppMgr.TRANS_ID_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterSelectTable() {
		theAppMgr.setSingleEditArea(res.getString("Highlight the desired search and press 'Enter'."));
	}

	/**
	 * put your documentation comment here
	 */
	private void enterConsultantName() {
		theAppMgr.setSingleEditArea(res.getString("Enter associate user Id."), "CONSULTANT", theAppMgr.UPPER_CASE_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterOperatorName() {
		theAppMgr.setSingleEditArea(res.getString("Enter cashier user Id."), "OPERATOR", theAppMgr.UPPER_CASE_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterCustomer() {
		System.out.println("USING PHONE MASK...");
		theAppMgr.setSingleEditArea(res.getString("Enter customer's phone number."), "CUSTOMER", theAppMgr.PHONE_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterAmount() {
		theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT", theAppMgr.CURRENCY_MASK);
	}

	/**
	 * callback when <b>Page Down</b> icon is clicked
	 */
	public void pageDown(MouseEvent e) {
		if (model.getRowCount() == 0)
			return;
		model.nextPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
		table.setRowSelectionInterval(0, 0);
		table.setColumnSelectionInterval(0, 0);
	}

	/**
	 * callback when <b>Page Up</b> icon is clicked
	 */
	public void pageUp(MouseEvent e) {
		if (model.getRowCount() == 0)
			return;
		model.prevPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
		table.setRowSelectionInterval(0, 0);
		table.setColumnSelectionInterval(0, 0);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getVersion() {
		return ("$Revision: 1.22.2.4.4.1 $");
	}

	/** ****************************************************************** */
	private class ViewTxnCellRenderer extends DefaultTableCellRenderer {
		/**
		 * put your documentation comment here
		 */
		public ViewTxnCellRenderer() {
			this.setHorizontalAlignment(SwingConstants.CENTER);
		}

		/**
		 * put your documentation comment here
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param column
		 * @return
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			return (comp);
		}
	}

	/**
	 * Extended this class to prevent Eu from selecting empty cells
	 */
	private class ViewTxnTable extends JCMSTable {
		/**
		 * put your documentation comment here
		 * @param ScrollableTableModel
		 *            model
		 * @param int
		 *            type
		 */
		public ViewTxnTable(ScrollableTableModel model, int type) {
			super(model, type);
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			String command = e.getActionCommand();
			if (!command.equals(JCMSTable.SELECT_CMD)) {
				String text = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
				if (text.trim().length() == 0) {
					table.setRowSelectionInterval(0, 0);
					table.setColumnSelectionInterval(0, 0);
				}
			}
		}
	}

	/**
	 * MP: Home pressed at customer display exits transaction with no message
	 * @return
	 */
	public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}

		/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the Sign
		 * Off event for Fiscal Solutions Service START--
		 */
		boolean goToHomeView = (theAppMgr
				.showOptionDlg(
						res.getString("Cancel Transaction"),
						res.getString("Are you sure you want to cancel this transaction?")));
		if (goToHomeView) {

			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr);
		}

		return goToHomeView;
		/*
		 * --END
		 */

	}
} // end applet
