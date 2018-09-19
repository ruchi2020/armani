/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Specifications               |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.collections;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import com.chelseasystems.cs.swing.menu.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.collection.*;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.collection.CMSMiscCollectionCredit;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cr.swing.CMSFocusManager;
import com.chelseasystems.cs.swing.collections.CollectionsPanelCMSMiscPayment;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.pos.ASISTxnData;
import com.chelseasystems.cs.collection.CMSCollectionTransaction;
import com.chelseasystems.cs.swing.collections.CollectionsPanelNFSCheckPayment;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.pos.PaymentTransaction;


/**
 *
 * <p>Title: CollectionsApplet</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CollectionsApplet extends CMSApplet {
	// public class PaidoutApplet extends JApplet implements IPaidOutGUIConst, ActionListener{
	private int paidOutType = -1;
	private CMSCustomer theCustomer = null;
	private CollectionTransaction currentTxn = null;
	private CMSCustomer cmsCustomer = null;
	public static final String MISC_PAID_IN = "MISC_PAYMENT";
	public static final String NFS_CHECK_PAYMENT = "NFS_CHECK_PAYMENT";
	public static final String HOUSE_ACCOUNT_PAYMENT = "HOUSE_ACCOUNT_PAYMENT";
	public static final String OPEN_DEPOSIT = "OPEN_DEPOSIT";
	private CMSCustomer cmsCustomerInitial = null;
	private int row;
	private boolean bAccountNo = true;
	private HouseAccount houseAccount = null;
	private boolean bCustLookUpDone = false;
	private boolean bOKButtonClicked = false;
	private ConfigMgr config;
	private ArmCurrency houseAccountInitialIssueAmount;
	JPanel jBottomPanel = new JPanel();
	CollectionsPanel jDetailPanel = null;
	ReasonModel model = new ReasonModel();
	JCMSTable tblReason = new JCMSTable(model, JCMSTable.SELECT_ROW);

	/**
	 * Method initializes all the values of applet
	 */
	public void init() {
		try {
			jbInit();
			initCollectionsTypes(); // load from config file only the first time this app is used
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
			e.printStackTrace();
		}
	}

	/**
	 * Component initialization
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		JPanel jMainPanel = new JPanel();
		JPanel jTopPanel = new JPanel();
		Border border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new java.awt.Color(148, 145, 140));
		TitledBorder titledBorder1 = new TitledBorder(border1, res.getString("SELECT REASON FOR PAIDIN"));
		Border border2 = BorderFactory.createCompoundBorder(titledBorder1, BorderFactory.createEmptyBorder(15, 25, 15, 5));
		jMainPanel.setLayout(new BorderLayout());
		jMainPanel.add(jTopPanel, BorderLayout.NORTH);
		jTopPanel.setBackground(theAppMgr.getBackgroundColor());
		jTopPanel.setPreferredSize(new Dimension((int) (555 * r), (int) (300 * r)));
		jTopPanel.setBorder(border2);
		jTopPanel.setLayout(new BorderLayout());
		jTopPanel.add(tblReason, BorderLayout.CENTER);
		jBottomPanel.setBackground(theAppMgr.getBackgroundColor());
		jBottomPanel.setLayout(new BorderLayout());
		jBottomPanel.setBorder(BorderFactory.createTitledBorder(res.getString("PAIDIN DETAILS")));
		jMainPanel.add(jBottomPanel, BorderLayout.CENTER);
		this.getContentPane().add(jMainPanel, BorderLayout.CENTER);
		tblReason.setAppMgr(theAppMgr);
		tblReason.getColumnModel().getColumn(0).setCellRenderer(new RadioRenderer());
		tblReason.addMouseListener(new MouseAdapter() {

			/**
			 * put your documentation comment here
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				clickEvent(e);
			}
		});
		tblReason.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			/**
			 * put your documentation comment here
    		 * @param e
			 */
			public void valueChanged(ListSelectionEvent e) {
				clickEvent(null);
			}
		});
		tblReason.addComponentListener(new java.awt.event.ComponentAdapter() {

			/**
			 * put your documentation comment here
			 * @param e
			 */
			public void componentResized(ComponentEvent e) {
				tblReason_componentResized(e);
			}
		});
	}

	/**
	 * Method reads the collections.cfg and initialize the panal class
	 * @throws Exception
	 */
	private void initCollectionsTypes() throws Exception {
		StringTokenizer st;
		config = new ConfigMgr("collections.cfg");
		String paidOutTypes = config.getString("COLLECTION_TYPES");
		Class[] parameterTypes = { this.getClass(), Class.forName("java.lang.String") };
		st = new StringTokenizer(paidOutTypes, ",");
		while(st.hasMoreElements()) {
			String key = (String) st.nextElement();
			String collectionsPanelClassName = config.getString(key + ".PANEL_CLASS");
			String collectionsDisplay = config.getString(key + ".DISPLAY");
			Constructor panelConstructor = Class.forName(collectionsPanelClassName).getConstructor(parameterTypes);
			Object[] parameters = { this, collectionsDisplay };
			CollectionsPanel collectionsPanel = (CollectionsPanel) panelConstructor.newInstance(parameters);
			collectionsPanel.setCollectionType(key);
			model.addRow(collectionsPanel);
		}
	}

	/**
	 * Method start the execution of applet
	 */
	public void start() {
		try {
			theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
			theAppMgr.setSingleEditArea(res.getString("Enter required information.  Select 'OK' when complete.")); // clear out the potential old
																													// message
			theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
			theAppMgr.showMenu(MenuConst.PAID_IN, theOpr);
			cmsCustomer = null;
			PaymentTransaction paymentTransaction = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
			if (paymentTransaction != null && paymentTransaction instanceof CollectionTransaction) {
				currentTxn = (CollectionTransaction) theAppMgr.getStateObject("TXN_POS");
			} else if (paymentTransaction != null) {
				theAppMgr.addStateObject("PREV_TXN_POS", paymentTransaction);
			}

			// MP: Store the customer in Diff state obj
			// This state obj is removed at the time the Txn is posted.
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null)
				CMSApplet.theAppMgr.addStateObject("CUST_NFS", CMSApplet.theAppMgr.getStateObject("TXN_CUSTOMER"));
			if (currentTxn == null && theAppMgr.getStateObject("CUST_NFS") == null) {
				jBottomPanel.removeAll();
				tblReason.setRowSelectionInterval(0, 0);
				clickEvent(null);
				theAppMgr.showMenu(MenuConst.OK_CANCEL_PREVIOUS, theOpr);
			} else
				theAppMgr.showMenu(MenuConst.OK_CANCEL_PREVIOUS, theOpr);
			// houseAccount = null;
			// check whether control ID is present. Typical scenario when coming back from CustomerLookupApplet. added by Anand
			if (CMSApplet.theAppMgr.getStateObject("CUST_NFS") != null) {
				// if bAccountNo is true, then attach THAT customer to the transaction as it is coming from CUSTOMER LOOKUP
				// and irrespective of account lookup customer, this is the customer that needs to be attached to txn.
				if (!bAccountNo) {
					cmsCustomer = (CMSCustomer) CMSApplet.theAppMgr.getStateObject("CUST_NFS");
					bCustLookUpDone = true;
				} else {
					// if customer lookup completed, then let that customer remain
					if (!bCustLookUpDone) {
						cmsCustomer = ((CMSCustomer) CMSApplet.theAppMgr.getStateObject("CUST_NFS"));
					}
					houseAccount = getHouseAccount((String) ((CMSCustomer) CMSApplet.theAppMgr.getStateObject("CUST_NFS")).getId());
					if (houseAccount == null) {
						theAppMgr.showErrorDlg(res.getString("There is no House Account for this customer ID"));
						clickEvent(null);
						return;
					}
					if (jDetailPanel instanceof CollectionsPanelHouseAccount && houseAccount != null) {
						(((CollectionsPanelHouseAccount) jDetailPanel).getAccountFld()).setText(houseAccount.getControlNum());
						// ( ( (CollectionsPanelHouseAccount) jDetailPanel).getAccountFld()).
						// setEditable(false);
					}
				}
				bAccountNo = false;
			}			
			if (jDetailPanel instanceof CollectionsPanelOpenDeposit) {
				((CollectionsPanelOpenDeposit) jDetailPanel).setBackgroundColorToGrey();
				((CollectionsPanelOpenDeposit) jDetailPanel).setFocusOnAmount();
				if (theAppMgr.getStateObject("ASIS_TXN_DATA") != null || theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
					// Don't reset.
				} else {
					((CollectionsPanelOpenDeposit) jDetailPanel).reset();
				}
				if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
					cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
					((CollectionsPanelOpenDeposit) jDetailPanel).setCustId(cmsCustomer.getId());
					((CollectionsPanelOpenDeposit) jDetailPanel).setFamilyName(cmsCustomer.getLastName());
					((CollectionsPanelOpenDeposit) jDetailPanel).setNameCust(cmsCustomer.getFirstName());
					CMSCustomer txnCust = cmsCustomer;
					String thisStoreId = ((CMSStore) theStore).getId();

					try {
						if (thisStoreId != null) {
						ArmCurrency custCurr = CustomerUtil.getDepositHistoryBalance(CMSApplet.theAppMgr, txnCust.getId(), thisStoreId);
							if (custCurr != null) {
								txnCust.setCustomerBalance(custCurr);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (cmsCustomer.getCustomerBalance() != null) {
						((CollectionsPanelOpenDeposit) jDetailPanel).setPrevBalance(cmsCustomer.getCustomerBalance());
					}
					((CollectionsPanelOpenDeposit) jDetailPanel).setFocusOnAmount();
				}
				bAccountNo = false;
			}
			if (jDetailPanel instanceof CollectionsPanelCMSMiscPaymentCredit) {
				((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setBackgroundColorToGrey();
				((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setFocusOnAmount();
				if (theAppMgr.getStateObject("ASIS_TXN_DATA") != null || theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
					// Don't reset.
				} else {
					((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).reset();
				}
				if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
					cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
					((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setCustId(cmsCustomer.getId());
					((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setFamilyName(cmsCustomer.getLastName());
					((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setNameCust(cmsCustomer.getFirstName());

					CMSCustomer txnCust = cmsCustomer;
					String thisStoreId = ((CMSStore) theStore).getId();

					try {
						if (thisStoreId != null) {
						ArmCurrency custCurr = CustomerUtil.getCreditHistoryBalance(CMSApplet.theAppMgr, txnCust.getId(), thisStoreId);
							if (custCurr != null) {
								txnCust.setCreditBalance(custCurr);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (cmsCustomer.getCustomerBalance() != null) {
						((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setPrevBalance(cmsCustomer.getCreditBalance());
					}
					((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setFocusOnAmount();
				}
				bAccountNo = false;
			}
			String sReasonCode = (String) theAppMgr.getStateObject("REASON_CODE");
			if (sReasonCode == null && currentTxn == null) {
				tblReason.setRowSelectionInterval(0, 0);
			}
			clickEvent(null);
			// if(currentTxn == null)
			// tblReason.setRowSelectionInterval(0, 0);
			// clickEvent(null);
		} catch (Exception e) {
			e.printStackTrace();
			showErrMsg("Error starting applet!\n" + e, true);
			return;
		}
		theAppMgr.removeStateObject("FROM_ASIS");
		theAppMgr.removeStateObject("FROM_ASIS_CANCEL");
	}

	/**
	 * Method returns the house account
	 * @return HouseAccount
	 */
	public HouseAccount getHouseAccount() {
		if (jDetailPanel instanceof CollectionsPanelHouseAccount) {
			String accountId = ((CollectionsPanelHouseAccount) jDetailPanel).getAccountFld().getText();
			if (accountId != null && accountId.trim().length() > 0)
			// Commented out as houseAccount is a private member as is set
			// && (houseAccount == null || !accountId.equals(houseAccount.getControlNum())))
			{
				if (theAppMgr.isOnLine()) {
					try {
						houseAccount = CMSRedeemableHelper.findHouseAccount(theAppMgr, accountId.trim());
						if (houseAccount != null) {
							if (houseAccount.getCustomerId() != null && houseAccount.getCustomerId().trim().length() > 0) {
								cmsCustomer = CMSCustomerHelper.findById(theAppMgr, houseAccount.getCustomerId().trim());
							}
						}
					} catch (Exception ex) {
						houseAccount = null;
						ex.printStackTrace();
					}
					if (houseAccount == null) {
						theAppMgr.showErrorDlg(res.getString("ERROR: Could not find this House Account."));
						((CollectionsPanelHouseAccount) jDetailPanel).getAccountFld().requestFocus();
						// return null;
					}
				} else {
					// do offline code here
				}
			}
			// assign customer if over-ridden by going to the customer lookup applet
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null)
				cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
		}
		return houseAccount;
	}

	/**
	 * Method returns the customer
	 * @return CMSCustomer
	 */
	public CMSCustomer getCMSCustomer() {
		return cmsCustomer;
	}

	/**
	 * @return Currency
	 */
	public ArmCurrency getHouseAccountInitialIssueAmount() {
		return ((HouseAccount) getHouseAccount()).getIssueAmount();
	}

	/**
	 * Stop the applet
	 */
	public void stop() {
	}

	/**
	 * Method returns the version
	 * @return String
	 */
	public String getVersion() {
		return ("$Revision: 1.20.2.4 $");
	}

	/**
	 * Method returns the screen Name
	 * @return String
	 */
	public String getScreenName() {
		return (res.getString("Store Paidins"));
	}

	/**
	 * This method shows the error message and sets up only <CANCEL> button 
     * to exit if 'true' is passed to 'resetButtons'(case of severe errors).
	 * @param errMsg
	 *            String
	 * @param resetButtons
	 *            boolean
	 */
	protected void showErrMsg(String errMsg, boolean resetButtons) {
		theAppMgr.showErrorDlg(errMsg);
		if (resetButtons) { // severe error case
			theAppMgr.goBack();
		}
	}

	/**
	 * Handling button events
	 * @param anEvent
	 *            CMSActionEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		try {
			String sAction = anEvent.getActionCommand();
			if (sAction.equals("CANCEL")) {
				theAppMgr.removeStateObject("TXN_POS");
				theAppMgr.removeStateObject("ASIS_TXN_DATA");
				// theAppMgr.goHome();
			} else if (sAction.equals("OK_COLLECTIONS")) {
				bOKButtonClicked = true;
				if (!processInputData()) {
					anEvent.consume();
				} else {
					bAccountNo = false;
					bCustLookUpDone = false;
					cmsCustomer = null;
				}
			} else if (sAction.equals("ACCOUNT_LOOKUP")) {
				// if (!processInputData())
				// anEvent.consume();
				bAccountNo = true;
				bOKButtonClicked = false;
			}
			// added by Anand
			else if (sAction.equals("CUST_LOOKUP")) {
				// if (!processInputData())
				// anEvent.consume();
				bAccountNo = false;
				theAppMgr.addStateObject("PAID_IN_TXN_PRESENT", "PAID_IN_TXN_PRESENT");
				bOKButtonClicked = false;
			} else if (sAction.equals("ADD_CUSTOMER")) {
				// if (!processInputData())
				// anEvent.consume();
				bAccountNo = false;
				bOKButtonClicked = false;
			} else if (sAction.equals("ADD_ASIS")) {
				bAccountNo = false;
				bOKButtonClicked = false;
				theAppMgr.addStateObject("FROM_PAID_IN", "FROM_PAID_IN");
			} else if (sAction.equals("PREV")) {
				theAppMgr.removeStateObject("TXN_POS");
				theAppMgr.removeStateObject("ASIS_TXN_DATA");
				theAppMgr.removeStateObject("CUST_NFS");
				PaymentTransaction paymentTransaction = (PaymentTransaction) theAppMgr.getStateObject("PREV_TXN_POS");
				if (paymentTransaction != null) {
					theAppMgr.addStateObject("TXN_POS", paymentTransaction);
				}
			}
		} catch (BusinessRuleException e) {
			System.out.println("a business rule exception was thrown");
			anEvent.consume();
			theAppMgr.showErrorDlg(res.getString(e.getMessage()));
			CMSFocusManager.requestFocusFieldSupplyingInfo();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex);
			showErrMsg(res.getString("Error initializing the next screen.  Select 'Cancel' to start over."), true);
			return;
		}
	}

	/**
	 * This method creates a new Txn and prepares for the next processing stage.
	 * @return boolean
	 * @throws BusinessRuleException
	 */
	private boolean processInputData() throws BusinessRuleException {
		currentTxn = jDetailPanel.exportData();
		if (currentTxn instanceof CMSMiscCollection) {
			if (((CMSMiscCollection) currentTxn).getType().equals("OPEN_DEPOSIT")) {
				((CMSMiscCollection) currentTxn).setCustomer((CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER"));
			}
			if (((CMSMiscCollection) currentTxn).getType().equals("MISC_PAID_IN")) {
				((CMSMiscCollection) currentTxn).setCustomer((CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER"));
			}
		}
		if (currentTxn == null) {
			return (false); // error in input data
		}
		if (theAppMgr.getGlobalObject("PROCESS_DATE") != null) {
			currentTxn.setProcessDate((Date) theAppMgr.getGlobalObject("PROCESS_DATE"));
		} else {
			currentTxn.setProcessDate(new Date());
		}
		currentTxn.setTheOperator((CMSEmployee) theAppMgr.getStateObject("OPERATOR"));
		((CMSMiscCollection) currentTxn).setCustomer(cmsCustomer);
		if (theAppMgr.getStateObject("ASIS_TXN_DATA") != null) {
			if (currentTxn instanceof CMSCollectionTransaction) {
				((CMSCollectionTransaction) currentTxn).setASISTxnData((ASISTxnData) theAppMgr.getStateObject("ASIS_TXN_DATA"));
				theAppMgr.removeStateObject("ASIS_TXN_DATA");
			}
		}
		theAppMgr.addStateObject("TXN_POS", currentTxn);
		return (true);
	}

	/**
	 * This function updates the content of the bottom panel according to the 
     * radio button selection.
	 * @param newPanel
	 *            CollectionsPanel
	 */
	private void updateBottomPanel(final CollectionsPanel newPanel) {
		if (!(jBottomPanel.getComponentCount() > 0 && jBottomPanel.getComponent(0).equals(newPanel))) {
			jBottomPanel.removeAll();
			newPanel.setBackground(theAppMgr.getBackgroundColor());
			jBottomPanel.add(newPanel, BorderLayout.CENTER);
			newPanel.setVisible(true);
			jBottomPanel.validate();
			jDetailPanel = newPanel; // reset reference
			jDetailPanel.repaint();
			SwingUtilities.invokeLater(new Runnable() {

				/**
				 * put your documentation comment here
				 */
				public void run() {
					if (jDetailPanel instanceof CollectionsPanelNFSCheckPayment) {
						((CollectionsPanelNFSCheckPayment) jDetailPanel).clearFields();
					} else if (jDetailPanel instanceof CollectionsPanelHouseAccount) {
						((CollectionsPanelHouseAccount) jDetailPanel).clearFields();
					}
					newPanel.initialFocus();
				}
			});
		}
		if (jDetailPanel instanceof CollectionsPanelOpenDeposit) {
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
				((CollectionsPanelOpenDeposit) jDetailPanel).setBackgroundColorTowhite();
				((CollectionsPanelOpenDeposit) jDetailPanel).setEnableAfter();
				((CollectionsPanelOpenDeposit) jDetailPanel).setFocusOnAmount();
			} else {
				((CollectionsPanelOpenDeposit) jDetailPanel).reset();
				((CollectionsPanelOpenDeposit) jDetailPanel).setEnabled();
			}
			theAppMgr.setSingleEditArea(res.getString("Select Customer"));
		}
		if (jDetailPanel instanceof CollectionsPanelCMSMiscPaymentCredit) {
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
				((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setBackgroundColorTowhite();
				((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setEnableAfter();
				((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setFocusOnAmount();
			} else {
				((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).reset();
				((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).setEnabled();
			}
			theAppMgr.setSingleEditArea(res.getString("Select Customer"));
		}
	}

	/**
	 * @param me
	 *            MouseEvent
	 */
	public void clickEvent(MouseEvent me) {
		row = tblReason.getSelectedRow();
		// int rowCount = tblReason.getSelectedRowCount();
		// strRow = new Integer(row).toString();
		if (row > -1) {
			CollectionsPanel collectionsPanel = (CollectionsPanel) model.getUnderlyingObject(row);
			updateBottomPanel(collectionsPanel);
			populateButtons(collectionsPanel.getCollectionType());
			if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
				theAppMgr.showMenu(MenuConst.COLLECTIONS_MENU, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
			}
		}
	}

	/**
	 * Scroll the page down
	 * @param e
	 *            MouseEvent
	 */
	public void pageDown(MouseEvent e) {
		model.nextPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
	}

	/**
	 * Scroll the page down
	 * @param e
	 *            MouseEvent
	 */
	public void pageUp(MouseEvent e) {
		model.prevPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
	}

	/**
	 * @param e
	 *            ComponentEvent
	 */
	public void tblReason_componentResized(ComponentEvent e) {
		model.setRowsShown(tblReason.getHeight() / tblReason.getRowHeight());
		tblReason.getColumnModel().getColumn(1).setPreferredWidth((int) (750 * r));
		tblReason.repaint();
	}

	/** ***************************************************************** */
  /**
   *
   * <p>Title: RadioRenderer</p>
   *
   * <p>Description: </p>
   *
   * <p>Copyright: Copyright (c) 2005</p>
   *
   * <p>Company: </p>
   *
   * @author not attributable
   * @version 1.0
   */
	private class RadioRenderer extends JRadioButton implements TableCellRenderer {

		/**
		 * Default Constructor
		 */
		public RadioRenderer() {
			this.setOpaque(false);
			this.setHorizontalAlignment(SwingConstants.CENTER);
		}

		/**
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param column
		 * @return
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			this.setEnabled(table.isEnabled());
			this.setSelected(isSelected);
			return (this);
		}
	}

  /**
   *
   * <p>Title: ReasonModel</p>
   *
   * <p>Description: </p>
   *
   * <p>Copyright: Copyright (c) 2005</p>
   *
   * <p>Company: </p>
   *
   * @author not attributable
   * @version 1.0
   */
	private class ReasonModel extends ScrollableTableModel {

		/**
		 * Default Constructor
		 */
		public ReasonModel() {
			super(new String[] { " ", res.getString("Select Reason for Paidin") });
		}

		/**
		 * Returns the object ofgiven row
		 * @param row
		 * @return
		 */
		public Object getUnderlyingObject(int row) {
			Vector vPage = getCurrentPage();
			return (vPage.elementAt(row));
		}

		/**
		 * Returns the value specified by the row and column
		 * @param row
		 * @param column
		 * @return
		 */
		public Object getValueAt(int row, int column) {
			Vector vPage = getCurrentPage();
			if (vPage.size() == 0) {
				return (null);
			}
			if (column == 0) {
				return (new Boolean(((JRadioButton) tblReason.getCellRenderer(row, column)).isSelected()));
			} else {
				return ((CollectionsPanel) vPage.elementAt(row)).getDisplayName();
			}
		}
	}

	/**
	 * Find the reason type that has been selected and show the relevant menu buttons
	 * @param menu
	 *            int
	 */
	public void populateButtons(String collectionType) {
		if (collectionType.trim().equalsIgnoreCase(this.MISC_PAID_IN)) {
			theAppMgr.addStateObject("REASON_CODE", "MISC_PAID_IN");
			if (theAppMgr.getStateObject("TXN_CUSTOMER") == null && theAppMgr.getStateObject("ASIS_TXN_DATA") == null) {
				// TD - Support for US
				if (jDetailPanel instanceof CollectionsPanelCMSMiscPaymentCredit) {
					((CollectionsPanelCMSMiscPaymentCredit) jDetailPanel).reset();
				} else if (jDetailPanel instanceof CollectionsPanelCMSMiscPayment) {
					((CollectionsPanelCMSMiscPayment) jDetailPanel).clearFields();
				}
			}
			theAppMgr.showMenu(MenuConst.COLLECTIONS_MENU, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
		} else if (collectionType.trim().equalsIgnoreCase(this.NFS_CHECK_PAYMENT)) {
			theAppMgr.addStateObject("REASON_CODE", "NFS_CHECK_PAYMENT");
			if (theAppMgr.getStateObject("TXN_CUSTOMER") == null && theAppMgr.getStateObject("ASIS_TXN_DATA") == null) {
				((CollectionsPanelNFSCheckPayment) jDetailPanel).clearFields();
			}
			theAppMgr.showMenu(MenuConst.nfs_menu, theOpr);
		} else if (collectionType.trim().equalsIgnoreCase(this.HOUSE_ACCOUNT_PAYMENT)) {
			theAppMgr.addStateObject("REASON_CODE", "HOUSE_ACCOUNT_PAYMENT");
		} else if (collectionType.trim().equalsIgnoreCase(this.OPEN_DEPOSIT)) {
			theAppMgr.addStateObject("REASON_CODE", "OPEN_DEPOSIT");
			theAppMgr.showMenu(MenuConst.COLLECTIONS_MENU, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
			if (theAppMgr.getStateObject("TXN_CUSTOMER") == null && theAppMgr.getStateObject("ASIS_TXN_DATA") == null) {
				((CollectionsPanelOpenDeposit) jDetailPanel).reset();
			}
		}
	}

	/**
	 * Returns HouseAccount from an array of Redeemables
	 * @param customerId
	 *            String
	 * @return HouseAccount
	 */
	private HouseAccount getHouseAccount(String customerId) {
		try {
			// Redeemable redeemablesArray = null;
			Redeemable[] redeemablesArray = null;
			HouseAccount houseAccount = null;
			redeemablesArray = CMSRedeemableHelper.findHouseAccountByCustomerId(theAppMgr, customerId);
			// findByCustomerId(theAppMgr, customerId);
			if (redeemablesArray != null) {
				for (int i = 0; i < redeemablesArray.length; i++) {
					if (redeemablesArray[i] instanceof HouseAccount) {
						// if (redeemablesArray[i] instanceof HouseAccoun(HouseAccount) redeemablesArray[i]t) {
						// houseAccount = (HouseAccount) redeemablesArray;
						houseAccount = (HouseAccount) redeemablesArray[i];
						return houseAccount;
					}
				}
				if (houseAccount == null) {
					theAppMgr.showErrorDlg(res.getString("Cannot find House Account" + "for this customer or remaining balance is zero."));
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
