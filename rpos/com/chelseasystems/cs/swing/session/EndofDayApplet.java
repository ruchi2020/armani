/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 3    | 07-11-2005 |Vikram     |  550      | Cash Drawer to open at store closing            |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Khayti     |           | Store Close Out                                                |
 -----------------------------------------------------------------------------------------------
 */
package com.chelseasystems.cs.swing.session;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.Remote;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.park.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.layout.VerticalFlowLayout;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.eod.*;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.readings.CMSReadingsHelper;
import com.chelseasystems.cs.readings.CMSTrialBalance;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.txnnumber.CMSTransactionNumberHelper;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cr.util.ResourceBundleKey;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cr.receipt.CMSDrawer;
import com.chelseasystems.cs.txnposter.CMSTxnSummary;
import com.chelseasystems.cs.fiscaldocument.FiscalInterface;

/**
 */
public class EndofDayApplet extends CMSApplet {
	public static final String CASH_DEPOSIT_KEY = "Cash";
	public static final String TOTAL_DEPOSIT_KEY = "Total Deposit";
	public final static int STEP_START = 0;
	public final static int STEP_TIMECARD = 1;
	public final static int STEP_DEPOSIT = 2;
	public final static int STEP_BONUS = 3;
	public final static int STEP_MOD_DWR = 4;
	public final static int STEP_RECON = 5;
	public final static int STEP_POST = 6;
	public final static int STEP_REPRINT = 7;
	private int currentStep = STEP_START;
	private boolean bypassGoHomePrompt;
	private CMSTrialBalance result;
	private ArmCurrency initialDrawerFund;
	private ArmCurrency tomorrowDrawerFund;
	private SimpleDateFormat dateFormat;
	private Hashtable htUserFields = new Hashtable();
	private Hashtable htSystemFields = new Hashtable();
	private Hashtable htTotalFields = new Hashtable();
	private JCMSTextField firstComp;
	private CMSRegister theRegister;
	private CMSTransactionEOD eodTxn;
	private String sAction = "";
	private boolean isProcessingStoreEOD;
	private FiscalInterface fiscalInterface = null;
	ScrollablePane scrollpane = new ScrollablePane();
	FieldPanel pnlFields = new FieldPanel();
	JPanel pnlTotals = new JPanel();
	JCMSLabel lblStep = new JCMSLabel();
	JCMSComboBox cboExchange = new JCMSComboBox();
	JCMSTextField fldDrawerToday = new JCMSTextField();
	JCMSTextField fldDrawerTomorrow = new JCMSTextField();

	/**
	 */
	public void init() {
		try {
			ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
			String sFiscInterfaceName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
			if (sFiscInterfaceName != null) {
				Class cls = Class.forName(sFiscInterfaceName);
				fiscalInterface = (FiscalInterface) cls.newInstance();
			}
		} catch (Exception e) {
			System.out.println("No Fiscal Interface could be initialized");
		}
		try {
			dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
			// Fiscal interface
			jbInit();
			constructConfigurableDisplay();
			//TD
			//setFocusTraversalRoute();
			//sandhya
			setFocusTraversalRoute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 */
	public void stop() {
	}

	//
	public String getScreenName() {
		return (res.getString("Register Closeout"));
	}

	//
	public String getVersion() {
		return ("$Revision: 1.2 $");
	}

	/**
	 * Start the applet.
	 */
	public void start() {
		try {
			isProcessingStoreEOD = theAppMgr.getStateObject("END_OF_STORE_DAY") != null;
			bypassGoHomePrompt = false;
			theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
			theOpr = (com.chelseasystems.cs.employee.CMSEmployee) theAppMgr.getStateObject("OPERATOR");
			// initialDrawerFund = theStore.getDrawerFund();
			theRegister = (CMSRegister) theAppMgr.getGlobalObject("REGISTER");
			initialDrawerFund = theRegister.getDrawerFund();
			selectOption();
			clear();
			setAppletState(STEP_START);
			result = CMSReadingsHelper.getTrialBalance(theAppMgr, (CMSStore) theStore, (Date) theAppMgr.getGlobalObject("PROCESS_DATE"));
			if (result == null)
				result = new CMSTrialBalance();
			populateScreen();
			goToParkAppletIfNeeded();
			if (theAppMgr.getBrokenTxnCount() > 0) {
				SwingUtilities.invokeLater(new Runnable() {
					/**
					 * put your documentation comment here
					 */
					public void run() {
						if (!theAppMgr.showOptionDlg(res.getString("Message"), res.getString("There are broken transactions on this terminal.  "
								+ "Press 'OK' to continue Register Closeout, or 'Cancel' to go Home."), res.getString("OK"), res.getString("Cancel")))
							theAppMgr.goHome();
					}
				});
			}
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
			return;
		}
	}

	/**
	 * @return
	 */
	public boolean isHomeAllowed() {
		if (bypassGoHomePrompt)
			return (true);
		else
			return (theAppMgr.showOptionDlg(res.getString("Home"), res.getString("Are you sure you want to cancel Register Closeout?")));
	}

	/**
	 */
	private void setAppletState(int step) {
		enableFields(step == STEP_DEPOSIT);
		switch (step) {
			case STEP_START:
				lblStep.setText(res.getString("Current Step") + ": " + res.getString("Organize Drawer Media"));
				theAppMgr.setSingleEditArea(res.getString("Select 'Enter Media Totals' to continue."));
				theAppMgr.showMenu(MenuConst.START_EOD, theOpr);
				break;
			case STEP_DEPOSIT:
				lblStep.setText(res.getString("Current Step") + ": " + res.getString("Determine Deposit"));
				theAppMgr.setSingleEditArea(res.getString("Enter media totals.  Select 'Verify Deposit' when done."));
				theAppMgr.showMenu(MenuConst.VERIFY_EOD, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
				firstComp.requestFocus();
				break;
			case STEP_MOD_DWR:
				lblStep.setText(res.getString("Current Step") + ": " + res.getString("Modify Drawer Fund"));
				theAppMgr.setSingleEditArea(res.getString("Enter new drawer amount."), "DRAWER_FUND", theAppMgr.CURRENCY_MASK);
				theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "DRAWER_FUND_CANCEL", theOpr);
				break;
			case STEP_RECON:
				lblStep.setText(res.getString("Current Step") + ": " + res.getString("Check Recon"));
				break;
			case STEP_POST:
				try {
					lblStep.setText(res.getString("Current Step") + ": " + res.getString("Process"));
					theAppMgr.showMenu(MenuConst.PROCESS_EOD, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
					theAppMgr.setSingleEditArea(res.getString("Select 'Process' to finalize Register Closeout."));
				} catch (Exception e) {
					System.out.println("Exception obtaining Double for YrUSCshFld : ");
					e.printStackTrace();
				}
				break;
			case STEP_REPRINT:
				theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.OK_BUTTON);
				selectOption();
				break;
		} // switch (step)
	}

	/**
	 * Handler for user's pressing of <RETURN> key when being asked for input.
	 */
	public void editAreaEvent(String command, ArmCurrency amount) {
		try {
			if (command.equals("DRAWER_FUND")) {
				selectOption();
				// setAppletState(STEP_POST); // Re-verify deposit after modding drawer fund since added ability to mod drawer fund prior to deposit
				setAppletState(STEP_DEPOSIT);
			ArmCurrency reportedCash = getUserAmount("CASH");
				if (amount.greaterThan(reportedCash.add(initialDrawerFund))) {
					((KeyedTextField) htUserFields.get("CASH")).requestFocus();
					theAppMgr.showErrorDlg(res.getString("There is not enough cash to make that drawer."));
					return;
				}
				// theStore.setDrawerFund(amount);
				theRegister.setDrawerFund(amount);
				fldDrawerTomorrow.setText(amount.formattedStringValue());
				tomorrowDrawerFund = new ArmCurrency(amount.doubleValue());
				if (validateFields(false))
					updateDeposit();
			}
		} catch (BusinessRuleException bex) {
			theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
			setAppletState(STEP_MOD_DWR);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
			setAppletState(STEP_MOD_DWR);
		}
	}

	/**
	 * This method will only be triggered by the single cancel button
	 * 
	 * @param header
	 * @param anEvent
	 */
	public void appButtonEvent(String header, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("CANCEL")) {
			if (header.equals("DRAWER_FUND_CANCEL")) {
				setAppletState(STEP_POST);
				anEvent.consume();
			}
		}
	}

	/**
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		sAction = anEvent.getActionCommand();
		if (sAction.equals("MOD_FUND")) {
			setAppletState(STEP_MOD_DWR);
			anEvent.consume();
		} else if (sAction.equals("PROCESS")) {
			eodTxn = generateEODTxn();
			if (eodTxn != null) {
				theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
				generateEODReport(eodTxn, ReceiptBlueprintInventory.CMSEODTotals);
				this.postEOD(eodTxn);
				setAppletState(STEP_REPRINT);
			} else {
				anEvent.consume();
			}
		} else if (sAction.equals("PRINT")) {
			if (eodTxn != null) {
				generateEODReport(eodTxn, isProcessingStoreEOD ? ReceiptBlueprintInventory.CMSEODStoreTotals : ReceiptBlueprintInventory.CMSEODTotals);
				setAppletState(STEP_REPRINT);
			} else {
				anEvent.consume();
			}
		} else if (sAction.equals("PROCESS_Z")) {
			eodTxn = generateEODTxn();
			if (eodTxn != null) {
				theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
				generateEODReport(eodTxn, ReceiptBlueprintInventory.CMSEODTotals);
				fiscalInterface.printZReport(eodTxn, result);
				this.postEOD(eodTxn);
				setAppletState(STEP_REPRINT);
			} else {
				anEvent.consume();
			}
		} else if (sAction.equals("PRINT_TRIAL")) {
			CMSTransactionEOD txn = generateEODTxn();
			if (txn != null) {
				generateEODReport(txn, ReceiptBlueprintInventory.CMSEODTrial);
			}
			anEvent.consume();
		} else if (sAction.equals("PRINT_TRIAL_X")) {
			CMSTransactionEOD txn = generateEODTxn();
			if (txn != null) {
				CMSEODReport report = generateEODReport(txn, ReceiptBlueprintInventory.CMSEODTrial);
				// MP: For Europe printing.
				if (report != null) {
					fiscalInterface.printXReport(report);
				}
			}
			anEvent.consume();
		} else if (sAction.equals("MODIFY_TOTALS")) {
			setAppletState(STEP_DEPOSIT);
			selectOption();
		} else if (sAction.equals("MEDIA_TOTALS")) {
			// pop drawer
			openDrawer();
			setAppletState(STEP_DEPOSIT);
		} else if (sAction.equals("CLEAR_TOTALS")) {
			clear();
			anEvent.consume();
			firstComp.requestFocus();
		} else if (sAction.equals("VERIFY_DEPOSIT")) {
			setupStep();
			if (!verifyDepositTotals())
				theAppMgr.showErrorDlg(res.getString("Deposit totals do not match system."));
		} else if (sAction.equals("CHECK_RECON")) {
		} else if (sAction.equals("CANCEL")) {
			if (theAppMgr.showOptionDlg(res.getString("Cancel"), res.getString("Are you sure you want to cancel Register Closeout?"))) {
				bypassGoHomePrompt = true;
				// Reset the Register drawer fund, if changed
				try {
					theRegister.setDrawerFund(this.initialDrawerFund);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				anEvent.consume();
		} else if (sAction.equals("PREV")) {
			theAppMgr.goBack();
			anEvent.consume();
		}
	}

	/**
	 */
	private void selectOption() {
		theAppMgr.setSingleEditArea(res.getString("Select option."));
	}

	/**
	 * put your documentation comment here
	 */
	private void goToParkAppletIfNeeded() {
		new Thread() {
			/**
			 * put your documentation comment here
			 */
			public void run() {
				int count = 0;
				try {
					theAppMgr.setWorkInProgress(true);
					// get local
					ParkFileServices parkServices = new ParkFileServices();
					count += parkServices.recall().length;
					if (theAppMgr.isOnLine() && count == 0) {
						// get remotes
						IParkRmiServer parkServer = null;
						Remote[] rmts = theAppMgr.getPeerStubs("park");
						if (rmts == null) {
							return;
						}
						// display non-modal dialog
						for (int i = 0; i < rmts.length; i++) {
							try {
								parkServer = (IParkRmiServer) rmts[i];
								count += parkServer.recall().length;
								if (count > 0)
									break;
							} catch (Exception e) {
							}
						}
					}
					theAppMgr.setWorkInProgress(false);
					if (count > 0) {
						theAppMgr.showErrorDlg(res.getString("Cannot perform Register Closeout until " + "suspended transactions are cleared.  Select 'OK' to view " + "suspended transactions."));
						bypassGoHomePrompt = true;
						theAppMgr.fireButtonEvent("PARK");
					}
					return;
				} catch (Exception ex) {
					theAppMgr.showExceptionDlg(ex);
				} finally {
					theAppMgr.setWorkInProgress(false);
				}
			}
		}.start();
	}

	/**
	 * Method will determine if it is ok to continue with next EOD step. Bypass the validateDeposit(Check Recon) if offline. Accept the "Mgr says
	 * numbers" RRR1
	 */
	private void setupStep() {
		if (validateDrawer() && validateFields(true)) {
			updateDeposit();
			setAppletState(STEP_POST);
		}
	}

	/**
	 * @return the total deposit by adding all deposit fields together and subtracting the drawer fund
	 */
	private ArmCurrency calculateTotal() {
	ArmCurrency totalDeposit = new ArmCurrency(0.0);
		try {
			for (Enumeration eTotalFields = htTotalFields.elements(); eTotalFields.hasMoreElements();) {
				KeyedTextField fldDepositAmount = (KeyedTextField) eTotalFields.nextElement();
				String sDepositKey = fldDepositAmount.getTotalKey();
				// if this is not the "Total Deposit" field we will add all its amounts
				if (!sDepositKey.equals(TOTAL_DEPOSIT_KEY)) {
				ArmCurrency aDepositAmount = new ArmCurrency(0.0);
					for (Enumeration eUserFields = htUserFields.elements(); eUserFields.hasMoreElements();) {
						KeyedTextField fldUserAmount = (KeyedTextField) eUserFields.nextElement();
						String aDepositKey = fldUserAmount.getTotalKey();
						// if this is one of the fields that contributes to this subtotal
						if (aDepositKey != null && aDepositKey.equals(sDepositKey)) {
						ArmCurrency userAmount = this.getUserAmount(fldUserAmount.getPaymentKey());
							// if foreign, convert amount
							if (!userAmount.getCurrencyType().equals(aDepositAmount.getCurrencyType())) {
								userAmount = userAmount.convertTo(aDepositAmount.getCurrencyType());
							}
							aDepositAmount = aDepositAmount.add(userAmount);
						}
					}
					fldDepositAmount.setText(aDepositAmount.formattedStringValue());
					fldDepositAmount.setAmount(aDepositAmount.doubleValue());
					totalDeposit = totalDeposit.add(aDepositAmount);
				}
			}
			// subtract drawer fund from total cash field
			KeyedTextField fldCash = (KeyedTextField) htTotalFields.get(CASH_DEPOSIT_KEY);
			ArmCurrency aCashAmount = new ArmCurrency(fldCash.getAmount());
			if (!aCashAmount.equals(new ArmCurrency(0.0d))) {
				aCashAmount = aCashAmount.add(this.initialDrawerFund).subtract(tomorrowDrawerFund);
				fldCash.setText(aCashAmount.formattedStringValue());
				totalDeposit = totalDeposit.add(this.initialDrawerFund).subtract(tomorrowDrawerFund);
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		} finally {
			return (totalDeposit);
		}
	}

	/**
	 */
	private void updateDeposit() {
		JCMSTextField fldTotal = (JCMSTextField) htTotalFields.get(TOTAL_DEPOSIT_KEY);
		if (fldTotal != null)
			fldTotal.setText(calculateTotal().formattedStringValue());
	}

	/**
	 * @return true if values are present for all fields
	 */
	private boolean validateFields(boolean showErrMsg) {
		JComponent comp = firstComp;
		try {
			for (Enumeration enm = htUserFields.elements(); enm.hasMoreElements();) {
				KeyedTextField field = (KeyedTextField) enm.nextElement();
				Locale locale = null;
				if (field.getForeignCurrencyTypeKey() != null) {
					locale = CurrencyType.getCurrencyType(field.getForeignCurrencyTypeKey()).getLocale();
				}
				if (locale == null) {
					locale = ArmCurrency.getBaseCurrencyType().getLocale();
				}
				DecimalFormat currencyFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
				if (field.getPaymentKey().equalsIgnoreCase("AMEX") || field.getPaymentKey().equalsIgnoreCase("BCRD") || field.getPaymentKey().equalsIgnoreCase("DISC")) {
					currencyFormat.setNegativePrefix("-$");
					currencyFormat.setNegativeSuffix("");
				}
				try {
					currencyFormat.parse(field.getText());
				} catch (Exception e) {
					new ArmCurrency(field.getForeignCurrencyTypeKey() + field.getText());
				}
			}
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("EndofDayApplet.validateFields()" + ex);
			if (showErrMsg)
				theAppMgr.showErrorDlg(res.getString("All fields must contain valid currencies."));
			comp.requestFocus();
			return (false);
		}
	}

	/**
	 * @return
	 */
	private boolean validateDrawer() {
	ArmCurrency aDrawerFund = new ArmCurrency(tomorrowDrawerFund.doubleValue());
		KeyedTextField fldCash = (KeyedTextField) htTotalFields.get(CASH_DEPOSIT_KEY);
	ArmCurrency aCashAmount = new ArmCurrency(fldCash.getAmount());
		if (aCashAmount.doubleValue() >= 0.0) {
			try {
				// theStore.setDrawerFund(aDrawerFund);
				theRegister.setDrawerFund(aDrawerFund);
				return (true);
			} catch (BusinessRuleException busRuleEx) {
				theAppMgr.showErrorDlg(res.getString(busRuleEx.getMessage()));
			}
		} else {
			((KeyedTextField) htUserFields.get("CASH")).requestFocus();
			theAppMgr.showErrorDlg(res.getString("There is not enough cash to make the drawer." + " Please enter a new drawer fund or modify totals."));
		}
		return (false);
	}

	/**
	 */
	private ArmCurrency getUserAmount(String key) {
		KeyedTextField field = (KeyedTextField) htUserFields.get(key);
		if (field == null)
			return new ArmCurrency(0d);
		String foriegnkey = field.getForeignCurrencyTypeKey();
		try {
			if (foriegnkey != null) {
				return (new ArmCurrency(foriegnkey + field.getText()));
			}
			try {
				// Verify data entry or not
				new Double(field.getText());
				// No Exception, use the entered value
				return (new ArmCurrency(field.getText()));
			} catch (NumberFormatException e) {
				// Exception means there is a character in the text field
				return (new ArmCurrency(ArmCurrency.getBaseCurrencyType(), field.getAmount()));
			}
		} catch (NumberFormatException ex) {
			System.out.println("EndofDayApplet.getUserAmount->NumberFormatException getting: " + key);
			return ((foriegnkey != null) ? new ArmCurrency(foriegnkey + "0") : new ArmCurrency(0));
		}
	}

	/**
	 */
	private void clear() {
		for (Enumeration enm = htTotalFields.elements(); enm.hasMoreElements();) {
			JCMSTextField field = (JCMSTextField) enm.nextElement();
			field.setText("");
		}
		for (Enumeration enm = htUserFields.elements(); enm.hasMoreElements();) {
			JCMSTextField field = (JCMSTextField) enm.nextElement();
			field.setText("");
		}
	}

	/**
	 */
	private void enableFields(boolean enabled) {
		for (Enumeration enm = htUserFields.elements(); enm.hasMoreElements();) {
			JCMSTextField field = (JCMSTextField) enm.nextElement();
			field.setEnabled(enabled);
		}
		if (!enabled)
			theAppMgr.setEditAreaFocus();
	}

	/**
	 */
	private CMSTransactionEOD generateEODTxn() {
		if (!validateDrawer()) {
			return (null);
		}
		try {
			// create EodTxn
			CMSTransactionEOD eodTxn = isProcessingStoreEOD ? new CMSTransactionEODStore((CMSStore) theStore) : new CMSTransactionEOD((CMSStore) theStore);
			eodTxn.setProcessDate((Date) theAppMgr.getGlobalObject("PROCESS_DATE"));
			eodTxn.setTheOperator((CMSEmployee) theOpr);
			// Fix for US Issue 1570:
			// Register closeout trial receipt - Is creating a transaction number but not writing anything to the database
			if (sAction != null && !sAction.equals("") && !sAction.equals("PRINT_TRIAL")) {
				eodTxn.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr, (CMSStore) theStore, (CMSRegister) theAppMgr.getGlobalObject("REGISTER")));
			}
			try {
				eodTxn.setMgrSaysAmex(getUserAmount("AMEX"));
				eodTxn.setMgrSaysBcrd(getUserAmount("BCRD"));
				eodTxn.setMgrSaysDisc(getUserAmount("DISC"));
				eodTxn.setMgrSaysMoneyOrders(getUserAmount("MONEY_ORDER"));
				eodTxn.setMgrSaysTravelersChecks(getUserAmount("TRAVERLS_CHECK"));
				// #1421: MAll cert to be displayed in over/short report
				eodTxn.setMgrSaysMallCert(getUserAmount("GIFT_CERT"));
			} catch (NumberFormatException ne) {
				theAppMgr.showErrorDlg(res.getString("Unable to parse totals entered.  Please re-enter totals."));
				return (null);
			}
			// going to add all the cashes to the txn
			eodTxn.addMgrSaysCash(getUserAmount("CASH"));
			for (Enumeration enm = htUserFields.elements(); enm.hasMoreElements();) {
				KeyedTextField field = (KeyedTextField) enm.nextElement();
				if (field.getForeignCurrencyTypeKey() != null && field.getPaymentKey().length() == 8 && field.getPaymentKey().indexOf("_CASH") > 0) {
					eodTxn.addMgrSaysCash(getUserAmount(field.getPaymentKey()));
				}
			}
			// goinf to add all the checks
			eodTxn.addMgrSaysCheck(getUserAmount("CHECK"));
			for (Enumeration enm = htUserFields.elements(); enm.hasMoreElements();) {
				KeyedTextField field = (KeyedTextField) enm.nextElement();
				if ( // field.getForeignCurrencyTypeKey() != null && field.getPaymentKey().length() == 9 &&
				field.getPaymentKey().indexOf("_CHECK") > 0 && !field.getPaymentKey().equals("TRAVERLS_CHECK")) {
					eodTxn.addMgrSaysCheck(getUserAmount(field.getPaymentKey()));
				}
			}
			eodTxn.setRegister(theRegister);
			setEODTenderTotal(eodTxn);
			setEODTransactionTotal(eodTxn);
			setEODTaxTotal(eodTxn);
			return (eodTxn);
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
			return (null);
		}
	}

	/**
	 * This method processes the EOD procedures by posting a EodTxn and delete the current local "processdate.dat" file.
	 * 
	 * @param eodTxn
	 */
	private void postEOD(CMSTransactionEOD eodTxn) {
		try {
			if (!isProcessingStoreEOD) {
				deleteProcessDateFile();
				updateRegister();
			} // post the txn
			CMSTxnPosterHelper.post(theAppMgr, eodTxn);
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		} finally {
			if (isProcessingStoreEOD)
				theAppMgr.addGlobalObject("EOD_STORE_COMPLETE", "EOD_STORE_COMPLETE");
			else
				theAppMgr.addGlobalObject("EOD_COMPLETE", "");
			theAppMgr.closeStatusDlg();
			bypassGoHomePrompt = true;
		}
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void updateRegister() throws Exception {
		// persist register info to save any drawer fund change
		String registerFile = FileMgr.getLocalFile("repository", "REGISTER");
		ObjectStore registerStore = new ObjectStore(registerFile);
		registerStore.write(theRegister);
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void deleteProcessDateFile() throws Exception {
		String error = res.getString("Error") + ".  " + res.getString("Register Closeout process halts.");
		String fullFileName = FileMgr.getLocalFile("repository", "PROCESS_DATE");
		// delete 'processdate.dat' file
		File processDateFile = new File(fullFileName);
		if (!processDateFile.exists()) {
			error = res.getString("Process Date file does not exist.") + "  " + res.getString("Register Closeout process halts.");
			throw new Exception(error);
		} else if (!processDateFile.canWrite()) {
			error = res.getString("Process Date file is read-only.") + "  " + res.getString("Register Closeout process halts.");
			throw new Exception(error);
		} else if (!processDateFile.delete()) {
			error = res.getString("Unable to delete Process Date file.") + "  " + res.getString("Register Closeout process halts.");
			throw new Exception(error);
		}
	}

	/**
	 * Method that builds and generates the Register Closeout report.
	 */
	private CMSEODReport generateEODReport(CMSTransactionEOD eodTxn, String receiptBlueprintName) {
		try {
			CMSEODReport report = new CMSEODReport();
			// Fix for US Issue 1570:
			// Register closeout trial receipt - Is creating a transaction number but not writing anything to the database
			if (sAction != null && !sAction.equals("") && !sAction.equals("PRINT_TRIAL")) {
				report.setEODTxnNumber(eodTxn.getId());
			}
			for (int i = 0; i < pnlFields.getComponentCount(); i++) {
				GroupingPanel pnl = (GroupingPanel) pnlFields.getComponent(i);
				for (int j = 0; j < pnl.getComponentCount(); j += 3) {
					JCMSLabel fldLabel = (JCMSLabel) pnl.getComponent(j + 0);
					KeyedTextField fldSystem = (KeyedTextField) pnl.getComponent(j + 1);
					KeyedTextField fldUser = (KeyedTextField) pnl.getComponent(j + 2);
				ArmCurrency tend = TxnSummary.getPaymentAmountByRegister(fldSystem.getSystemAmountKey());
				ArmCurrency rept = new ArmCurrency((fldUser.getForeignCurrencyTypeKey() == null) ? fldUser.getText() : fldUser.getForeignCurrencyTypeKey() + fldUser.getText());
					AccountableMedia am = new AccountableMedia(fldLabel.getText(), rept, tend);
					report.addAccountableMedia(am);
				}
			}
			// add all totaled amounts to report
			for (int i = 0; i < pnlTotals.getComponentCount(); i++)
				if (pnlTotals.getComponent(i) instanceof JLabel) {
					JLabel lblBankDeposit = (JLabel) pnlTotals.getComponent(i);
					KeyedTextField fldBankDeposit = (KeyedTextField) lblBankDeposit.getLabelFor();
					ArmCurrency currBankDeposit = new ArmCurrency(fldBankDeposit.getText());
					if (currBankDeposit.lessThan(new ArmCurrency(0.0d))) {
						currBankDeposit = new ArmCurrency(0.0d);
					}
					report.addBankDeposit(lblBankDeposit.getText(), currBankDeposit);
				}
			report.setPrivateCardTendered(result.getPrivateCardTendered());
			report.setCreditMemoTendered(result.getCreditMemoTendered());
			report.setCreditMemoIssued(result.getCreditMemoIssued());
			report.setGiftCertificateIssued(result.getGiftCertificateIssued());
			report.setGiftCertificateTendered(result.getGiftCertificateTendered());
			report.setStoreValueCardIssued(result.getStoreValueCardIssued());
			report.setStoreValueCardTendered(result.getStoreValueCardTendered());
			report.setMfrCouponTendered(result.getMfrCouponTendered());
			// ks: added this to set Redeemable payment type
			report.setCMSRedeemable(result.getCMSRedeemable());
			// sales totals
			report.setSalesTax(result.getSalesTaxTotal());
			report.setRegionalTax(result.getRegionalTaxTotal());
			report.setSaleTotal(result.getSaleTotal());
			report.setSaleReturnTotal(result.getSaleReturnTotal());
			/**
			 * The markdown and discount totals are not final totals applied to a transaction. Until this is changed use the reduction total to
			 * represent the final amount a trnsaction was reduced. This includes all markdowns and discounts that took precedence in the transaction.
			 */
			// report.setMarkdownTotal(report.getMarkdownTotal());
			// report.setDiscounts(result.getDiscountTotal());
			report.setReductionTotal(result.getReductionTotal());
			// transaction types
			report.setLayawayPaymentTotal(result.getLayawayPaymentTotal());
			report.setLayawaySaleTotal(result.getLayawaySaleTotal());
			report.setPaidouts(result.getPaidoutTotal());
			report.setCashdrops(TxnSummary.getTypeAmountByRegister("PDOT|CASH_TRANSFER"));
			report.setStoreExpenses(TxnSummary.getTypeAmountByRegister("PDOT|MISC_PAID_OUT"));
			report.setCollections(result.getCollectionTotal());
			report.setLayawayRTSTotal(result.getLayawayRTSTotal());
			report.setReturnTotal(result.getReturnsTotal());
			report.setVoidTotal(result.getVoidsTotal());
			report.setRedeemableBuyBackTotal(result.getRedeemableBuyBackTotal());
			// report headers
			report.setOperator(eodTxn.getTheOperator().getFirstName() + " " + eodTxn.getTheOperator().getLastName());
			report.setStore(eodTxn.getStore().getId());
			report.setRegister(eodTxn.getRegister().getId());
			report.setEODDate(dateFormat.format(eodTxn.getCreateDate()));
			// report.setDrawerFundTotal(theStore.getDrawerFund());
			report.setDrawerFundTotal(theRegister.getDrawerFund());
			report.setDrawerFundInitial(initialDrawerFund);
			Hashtable reductionsTable = result.getReductions();
			Enumeration enm = reductionsTable.keys();
			CMSEODReport.ReductionsStruct[] reductionsArray = new CMSEODReport.ReductionsStruct[reductionsTable.size()];
			for (int i = 0; enm.hasMoreElements(); i++) {
				String theKey = (String) enm.nextElement();
			ArmCurrency reductionAmt = (ArmCurrency) reductionsTable.get(theKey);
				reductionsArray[i] = report.new ReductionsStruct(new ResourceBundleKey(theKey), reductionAmt);
			}
			report.setReductionsStruct(reductionsArray);
			// Set Array of Credit cards in report
			ArrayList arr = new ArrayList();
			ArrayList arrPaymentType = new ArrayList();
			ConfigMgr cfg = new ConfigMgr("payment.cfg");
			String credit_keys = cfg.getString("EOD_CREDIT_CARD_KEYS");
			String[] paymentTypes = CMSTxnSummary.getPaymentsByRegister();
			for (int index = 0; index < paymentTypes.length; index++) {
				String paymentType = paymentTypes[index];
				arrPaymentType.add(paymentType);
				if (credit_keys != null && credit_keys.indexOf(paymentType) >= 0) {
				ArmCurrency eodTotal = CMSTxnSummary.getPaymentAmountByRegister(paymentType);
					arr.add(report.new CreditCardStruct(res.getString(paymentType), eodTotal));
				}
			}
			//issue # 29756 added for EOD Report support for all payment types. To support this change DCRD entry in payment.cfg has been replaced by DEBIT_CARD.
			// Issue #775
			// Register closeout report from EOD should show $0.00 for all credit card payments that you are authorized to accept.
		/*	if (credit_keys != null) {
				StringTokenizer st = new StringTokenizer(credit_keys, ",");
			ArmCurrency zeroCur = new ArmCurrency(0.00);
				while(st.hasMoreTokens()) {
					String key = st.nextToken();
					if (!arrPaymentType.contains(key)) {
						arr.add(report.new CreditCardStruct(res.getString(key), zeroCur));
					}
				}
			}*/
			// End- Issue # 775
			//end issue # 29756
			CMSEODReport.CreditCardStruct[] creditCardsArray = new CMSEODReport.CreditCardStruct[arr.size()];
			arr.toArray(creditCardsArray);
			report.setCreditCardsStruct(creditCardsArray);
			// Additional tender data
			report.setHouseAccountTotal(result.getHouseAccountTotal());
			report.setMailCheckTotal(result.getMailCheckTotal());
			report.setMallCertTotal(result.getMallCertTotal());
			// Add Paid in totals by type
			Hashtable paidInsTable = result.getPaidInsTotal();
			Enumeration enumPaidIn = paidInsTable.keys();
			CMSEODReport.PaidInsStruct[] paidInsArray = new CMSEODReport.PaidInsStruct[paidInsTable.size()];
			for (int i = 0; enumPaidIn.hasMoreElements(); i++) {
				String theKey = (String) enumPaidIn.nextElement();
				ArmCurrency collAmt = (ArmCurrency) paidInsTable.get(theKey);
				paidInsArray[i] = report.new PaidInsStruct(res.getString(theKey), collAmt);
			}
			report.setPaidInsStruct(paidInsArray);
			report.setTrialBalance(result);
			// ended here
			if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
				String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "EOD.rdo";
				try {
					ObjectStore objectStore = new ObjectStore(fileName);
					objectStore.write(report);
				} catch (Exception e) {
					System.out.println("exception on writing object to blueprint folder: " + e);
				}
			}
			Object[] arguments = { report };
			ReceiptFactory receiptFactory = new ReceiptFactory(arguments, receiptBlueprintName);
			// print the report in the employee's preferred language
			ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(eodTxn.getStore(), eodTxn.getTheOperator());
			localeSetter.setLocale(receiptFactory);
			receiptFactory.print(theAppMgr);
			// Return the report object
			return report;
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
			return null;
		}
	}

	/**
	 * callback when <b>Page Down</b> is pressed
	 */
	public void pageDown(MouseEvent me) {
		//scrollpane.nextPage();//Ruchi 
		return;
	}

	/**
	 * callback when <b>Page Up</b> is pressed
	 */
	public void pageUp(MouseEvent me) {
		//scrollpane.prevPage();//Ruchi
		return;
	}

	/**
	 */
	private void populateScreen() {
		ArmCurrency currCash = new ArmCurrency(0.0d);
		try {
			for (Enumeration enm = htSystemFields.elements(); enm.hasMoreElements();) {
				KeyedTextField field = (KeyedTextField) enm.nextElement();
				String key = field.getSystemAmountKey();
				if (key != null)
					if (key.equals("CASH")) {
						currCash = TxnSummary.getPaymentAmountByRegister(key);
					}
				field.setText(PaymentMgr.getShouldHideTotal(key) ? res.getString("\"hidden\"") : TxnSummary.getPaymentAmountByRegister(key).formattedStringValue());
			}
			// ks: displaying User total if boolean is set to true htUserFields
			for (Enumeration enm = htUserFields.elements(); enm.hasMoreElements();) {
				KeyedTextField field = (KeyedTextField) enm.nextElement();
				String key = field.getPaymentKey();
				if (key != null) {
					if (CMSPaymentMgr.getIsEODDefaultTotal(key)) {
						ArmCurrency amount = TxnSummary.getPaymentAmountByRegister(key);
						if (amount.lessThan(new ArmCurrency(0.0d))) {
							field.setText(new ArmCurrency(0.0d).formattedStringValue());
							field.setAmount(0.0d);
						} else {
							field.setText(TxnSummary.getPaymentAmountByRegister(key).formattedStringValue());
							field.setAmount(TxnSummary.getPaymentAmountByRegister(key).doubleValue());
						}
					} else {
						field.setText("");
					}
				}
			}
			fldDrawerToday.setText(initialDrawerFund.formattedStringValue());
			if (currCash.lessThan(new ArmCurrency(0.0d)) && currCash.absoluteValue().lessThanOrEqualTo(initialDrawerFund)) {
				fldDrawerTomorrow.setText(initialDrawerFund.add(currCash).formattedStringValue());
				tomorrowDrawerFund = new ArmCurrency(initialDrawerFund.add(currCash).doubleValue());
			} else {
				fldDrawerTomorrow.setText(initialDrawerFund.formattedStringValue());
				tomorrowDrawerFund = new ArmCurrency(initialDrawerFund.doubleValue());
			}
		} catch (CurrencyException ce) {
		}
	}

	/**
	 */
	private void jbInit() throws Exception {
		scrollpane.getViewport().setBackground(theAppMgr.getBackgroundColor());
		JPanel pnlDrawer = new JPanel();
		JPanel pnlExchangeRate = new JPanel();
		JPanel pnlHeaders = new JPanel();
		JPanel pnlNE = new JPanel();
		JCMSLabel lblMediaType = new JCMSLabel();
		JCMSLabel lblSystemTotals = new JCMSLabel();
		JCMSLabel lblYourTotals = new JCMSLabel();
		JCMSLabel lblDrawerToday = createBankDepositLabel("Today");
		JCMSLabel lblDrawerTomorrow = createBankDepositLabel("Tomorrow");
		JPanel pnlCenter = new JPanel();
		JPanel pnlNorth = new JPanel();
		JPanel pnlEast = new JPanel();
		JPanel pnlWest = new JPanel();
		JPanel pnlSouth = new JPanel();
		pnlCenter.setLayout(new BorderLayout());
		pnlEast.setBackground(theAppMgr.getBackgroundColor());
		pnlEast.setLayout(new VerticalFlowLayout());
		pnlSouth.setBackground(theAppMgr.getBackgroundColor());
		pnlNorth.setBackground(theAppMgr.getBackgroundColor());
		pnlNorth.setLayout(new BorderLayout());
		lblStep.setText(res.getString("Current Step") + ":");
		lblStep.setHorizontalAlignment(SwingConstants.CENTER);
		lblStep.setVerticalAlignment(SwingConstants.TOP);
		lblStep.setFont(theAppMgr.getTheme().getEditAreaFont());
		pnlWest.setBackground(theAppMgr.getBackgroundColor());
		pnlTotals.setBackground(theAppMgr.getBackgroundColor());
		pnlTotals.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray, 1), res.getString("DEPOSIT TOTALS")));
		pnlTotals.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, (int) (15 * r)));
		pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		pnlCenter.setBackground(theAppMgr.getBackgroundColor());
		pnlDrawer.setBackground(theAppMgr.getBackgroundColor());
		pnlDrawer.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray, 1), res.getString("DRAWER FUND")));
		pnlDrawer.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, (int) (15 * r)));
		pnlExchangeRate.setBackground(theAppMgr.getBackgroundColor());
		pnlExchangeRate.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray, 1), res.getString("EXCHANGE RATE")));
		pnlExchangeRate.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		pnlNE.setBackground(theAppMgr.getBackgroundColor());
		lblMediaType.setText(res.getString("Media Type") + ":");
		lblMediaType.setHorizontalAlignment(SwingConstants.CENTER);
		lblMediaType.setFont(theAppMgr.getTheme().getHeaderFont());
		lblSystemTotals.setText(res.getString("System Totals") + ":");
		lblSystemTotals.setHorizontalAlignment(SwingConstants.CENTER);
		lblSystemTotals.setFont(theAppMgr.getTheme().getHeaderFont());
		lblYourTotals.setText(res.getString("Your Totals") + ":");
		lblYourTotals.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourTotals.setFont(theAppMgr.getTheme().getHeaderFont());
		pnlHeaders.setBackground(theAppMgr.getBackgroundColor());
		this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.add(scrollpane, BorderLayout.CENTER);
		fldDrawerToday.setEnabled(false);
		fldDrawerToday.setHorizontalAlignment(SwingConstants.TRAILING);
		fldDrawerToday.setAppMgr(theAppMgr);
		fldDrawerTomorrow.setEnabled(false);
		fldDrawerTomorrow.setHorizontalAlignment(SwingConstants.TRAILING);
		fldDrawerTomorrow.setAppMgr(theAppMgr);
		cboExchange.setEnabled(false);
		cboExchange.setEditRenderer(new ExchangeRenderer());
		cboExchange.setAppMgr(theAppMgr);
		pnlFields.setBackground(theAppMgr.getBackgroundColor());
		scrollpane.setViewportView(pnlFields);
		scrollpane.setBackground(theAppMgr.getBackgroundColor());
		this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
		pnlNorth.add(pnlHeaders, BorderLayout.CENTER);
		pnlHeaders.add(lblMediaType, null);
		pnlHeaders.add(lblSystemTotals, null);
		pnlHeaders.add(lblYourTotals, null);
		pnlNorth.add(pnlNE, BorderLayout.EAST);
		this.getContentPane().add(pnlEast, BorderLayout.EAST);
		pnlEast.add(lblStep, null);
		pnlEast.add(pnlDrawer, null);
		pnlDrawer.add(lblDrawerToday, null);
		pnlDrawer.add(fldDrawerToday, null);
		pnlDrawer.add(lblDrawerTomorrow, null);
		pnlDrawer.add(fldDrawerTomorrow, null);
		// pnlEast.add(pnlExchangeRate, null);
		pnlExchangeRate.add(cboExchange, null);
		pnlEast.add(pnlTotals, null);
		this.getContentPane().add(pnlWest, BorderLayout.WEST);
		fldDrawerToday.setBackground(theAppMgr.getBackgroundColor());
		fldDrawerTomorrow.setBackground(theAppMgr.getBackgroundColor());
		cboExchange.setBackground(theAppMgr.getBackgroundColor());
		pnlEast.setPreferredSize(new Dimension((int) (300 * r), (int) (500 * r))); // Deepak
		pnlNorth.setPreferredSize(new Dimension((int) (10 * r), (int) (40 * r)));
		lblStep.setPreferredSize(new Dimension((int) (290 * r), (int) (100 * r)));
		pnlTotals.setPreferredSize(new Dimension((int) (290 * r), (int) (260 * r)));
		fldDrawerToday.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		fldDrawerTomorrow.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		pnlDrawer.setPreferredSize(new Dimension((int) (290 * r), (int) (130 * r)));
		pnlExchangeRate.setPreferredSize(new Dimension((int) (290 * r), (int) (65 * r)));
		cboExchange.setPreferredSize(new Dimension((int) (220 * r), (int) (30 * r)));
		pnlNE.setPreferredSize(new Dimension((int) (290 * r), (int) (10 * r)));
		lblMediaType.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		lblSystemTotals.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		lblYourTotals.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		//TD
		/*addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event){
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						System.out.println("EndOfDay .... Setting Policy.......");
						setFocusTraversalRoute();
					}
				});
			}
			public void ancestorMoved(AncestorEvent event) {
			}
			public void ancestorRemoved(AncestorEvent event){
				resetDefaultPolicy();
			}
		});*/

	}

	/*private void resetDefaultPolicy() {  
		getFocusCycleRootAncestor().setFocusTraversalPolicy(null);
	}*/
	
	/**
	 */
	private void constructConfigurableDisplay() {
		try {
			clearDisplay();
			Hashtable htSummaries = new Hashtable();
			String[] types = PaymentMgr.getEndSessionPaymentTypes();
			// loop through payment types included in EOD
			for (int i = 0; i < types.length; i++) {
				KeyedTextField fldUser = createUserAmountField(types[i]);
				// create or get existing panel that the textfields go in
				GroupingPanel panel = null;
				String title = res.getString(PaymentMgr.getEndSessionGrouping(types[i]));
				if (htSummaries.containsKey(title))
					panel = (GroupingPanel) htSummaries.get(title);
				else {
					panel = new GroupingPanel(title);
					htSummaries.put(title, panel);
					if (title.equals(res.getString("DEPOSIT"))) {
						pnlFields.add(panel);
					} else {
						pnlFields.add(panel, 0);
						firstComp = fldUser;
					}
				}
				panel.add(createUserFieldLabel(types[i]));
				KeyedTextField fldSystem = (KeyedTextField) createSystemAmountField(types[i]);
				panel.add(fldSystem);
				panel.add(fldUser);
				// remember all the user fields in a hashtable
				htUserFields.put(types[i], fldUser);
				// remember all the systems fields in a hashtable
				htSystemFields.put(types[i], fldSystem);
				// if you can tell me how to get these panels to resize properly lemme know - CG
				int count = panel.getComponentCount() / 3;
				panel.setPreferredSize(new Dimension(10, (int) (50 * r * count + 47)));
				// if payment type is part of bank deposit
				String sDeposit = PaymentMgr.getEndSessionBankDepositGrouping(types[i]);
				if (sDeposit.length() > 0) {
					// mark the user field so it knows it is to be totaled
					fldUser.setToolTipText(sDeposit);
					fldUser.setTotalKey(sDeposit);
					// if the subtotal field already exists, then just continue
					if (htTotalFields.get(sDeposit) == null) {
						JLabel lblBankDeposit = createBankDepositLabel(sDeposit);
						pnlTotals.add(lblBankDeposit);
						JCMSTextField fldDepositTotal = createBankDepositField(sDeposit);
						pnlTotals.add(fldDepositTotal);
						lblBankDeposit.setLabelFor(fldDepositTotal);
						htTotalFields.put(sDeposit, fldDepositTotal);
					}
				}
				// if payment type is foreign
				Payment payment = PaymentMgr.getPayment(types[i]);
				if (payment.isForeign()) {
					String key = types[i].substring(0, 3);
					CurrencyType cType = CurrencyType.getCurrencyType(key);
					if (cType != null) {
					ArmCurrency exchange = new ArmCurrency(Math.random() * 100); // random number around a dollar
						final ArmCurrency foreign = exchange.convertTo(cType);
						cboExchange.addItem(foreign);
						fldUser.setForeignCurrencyTypeKey(key);
						fldUser.addFocusListener(new FocusAdapter() {
							/**
							 * @param e
							 */
							public void focusGained(FocusEvent e) {
								cboExchange.setSelectedItem(foreign);
							}
						});
					}
				}
			}
			// add total field
			JLabel lblTotalDeposit = createBankDepositLabel(TOTAL_DEPOSIT_KEY);
			pnlTotals.add(lblTotalDeposit);
			JCMSTextField fldTotal = createBankDepositField(TOTAL_DEPOSIT_KEY);
			lblTotalDeposit.setLabelFor(fldTotal);
			pnlTotals.add(fldTotal);
			htTotalFields.put(TOTAL_DEPOSIT_KEY, fldTotal);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void clearDisplay() {
		htUserFields.clear();
		htSystemFields.clear();
		htTotalFields.clear();
		pnlFields.removeAll();
		pnlTotals.removeAll();
	}

	/**
	 * @return JCMSLabel a label for a bank deposit amount
	 */
	protected JCMSLabel createBankDepositLabel(String aDepositCategory) {
		JCMSLabel lblDepositGroup = new JCMSLabel();
		lblDepositGroup.setText(res.getString(aDepositCategory) + ":");
		lblDepositGroup.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDepositGroup.setPreferredSize(new Dimension((int) (110 * r), (int) (17 * r)));
		lblDepositGroup.setAppMgr(theAppMgr);
		return (lblDepositGroup);
	}

	/**
	 * @return KeyedTextField a textfield a bank deposit amount
	 */
	protected KeyedTextField createBankDepositField(String aDepositCategory) {
		KeyedTextField fldDepositGroup = new KeyedTextField();
		fldDepositGroup.setTotalKey(aDepositCategory);
		fldDepositGroup.setEnabled(false);
		fldDepositGroup.setHorizontalAlignment(SwingConstants.RIGHT);
		fldDepositGroup.setBackground(theAppMgr.getBackgroundColor());
		fldDepositGroup.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		return (fldDepositGroup);
	}

	/**
	 * @return KeyedTextField a textfield for the user amount
	 */
	protected KeyedTextField createUserAmountField(String aPaymentType) {
		KeyedTextField fldUser = new KeyedTextField();
		fldUser.setPaymentKey(aPaymentType);
		fldUser.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		fldUser.setHorizontalAlignment(SwingConstants.RIGHT);
		fldUser.addFocusListener(new EODFocusListener());
		fldUser.setAppMgr(theAppMgr);
		return (fldUser);
	}

	/**
	 * @return KeyedTextField a textfield for the system amount
	 */
	protected KeyedTextField createSystemAmountField(String aPaymentType) {
		KeyedTextField fldSystem = new KeyedTextField();
		fldSystem.setSystemAmountKey(aPaymentType);
		fldSystem.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		fldSystem.setHorizontalAlignment(SwingConstants.RIGHT);
		fldSystem.setBackground(theAppMgr.getBackgroundColor());
		fldSystem.setAppMgr(theAppMgr);
		fldSystem.setEnabled(false);
		return (fldSystem);
	}

	/**
	 * @return JCMSLabel a label for the payment type
	 */
	protected JCMSLabel createUserFieldLabel(String aPaymentType) {
		JCMSLabel fldLabel = new JCMSLabel();
		try {
			Payment pay = PaymentMgr.getPayment(aPaymentType);
			fldLabel.setText(res.getString(pay.getGUIPaymentName()) + ":");
		} catch (Exception ex) {
			System.err.println("EndofDayApplet.createUserFieldLabel()->" + ex);
		}
		fldLabel.setPreferredSize(new Dimension((int) (150 * r), (int) (30 * r)));
		fldLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		fldLabel.setFont(theAppMgr.getTheme().getTextFieldFont());
		return (fldLabel);
	}

	/**
	 */
	//private void setFocusTraversalRoute() {
		//TD
		//Existing code.
		/*
		//------------------------------------------- 
		int count = pnlFields.getComponentCount();
		if (count > 0) {
			JPanel pnl = (JPanel) pnlFields.getComponent(0);
			firstComp = (JCMSTextField) pnl.getComponent(2);
			JPanel lastPnl = (JPanel) pnlFields.getComponent(count - 1);
			int lastField = lastPnl.getComponentCount();
			((JCMSTextField) lastPnl.getComponent(lastField - 1)).setNextFocusableComponent(firstComp);
		}
		//------------------------------------------- 
		*/
		/*int count = pnlFields.getComponentCount();
		if (count > 0) {
			JPanel pnl = (JPanel) pnlFields.getComponent(0);
			firstComp = (JCMSTextField) pnl.getComponent(2);
			JPanel lastPnl = (JPanel) pnlFields.getComponent(count - 1);
			int lastField = lastPnl.getComponentCount();
			//((JCMSTextField) lastPnl.getComponent(lastField - 1)).setNextFocusableComponent(firstComp);
			JComponent lastComp = ((JComponent) lastPnl.getComponent(lastField - 1));
			getFocusCycleRootAncestor().setFocusTraversalPolicy(new LocalFocusPolicy(getFocusCycleRootAncestor().getFocusTraversalPolicy(), firstComp, lastComp ));
		}*/
	//}
	
	/**
	 */
	private void setFocusTraversalRoute() {
		int count = pnlFields.getComponentCount();
		if (count > 0) {
			JPanel pnl = (JPanel) pnlFields.getComponent(0);
			firstComp = (JCMSTextField) pnl.getComponent(2);
			JPanel lastPnl = (JPanel) pnlFields.getComponent(count - 1);
			int lastField = lastPnl.getComponentCount();
			((JCMSTextField) lastPnl.getComponent(lastField - 1)).setNextFocusableComponent(firstComp);
		}
	}

	/** ********************************************************************* */
	private class KeyedTextField extends JCMSTextField {
		private String paymentKey;
		private String totalKey;
		private String systemAmountKey;
		private String currencyTypeKey;
		private double amount = 0.0;

		/**
		 * @param amount
		 */
		public void setAmount(double amount) {
			this.amount = amount;
		}

		/**
		 * @return
		 */
		public double getAmount() {
			return amount;
		}

		/**
		 * @param aKey
		 */
		public void setPaymentKey(String aKey) {
			this.paymentKey = aKey;
		}

		/**
		 * @return
		 */
		public String getPaymentKey() {
			return (paymentKey);
		}

		/**
		 * @param aKey
		 */
		public void setTotalKey(String aKey) {
			this.totalKey = aKey;
		}

		/**
		 * @return
		 */
		public String getTotalKey() {
			return (totalKey);
		}

		/**
		 * @param aKey
		 */
		public void setSystemAmountKey(String aKey) {
			this.systemAmountKey = aKey;
		}

		/**
		 * @return
		 */
		public String getSystemAmountKey() {
			return (systemAmountKey);
		}

		/**
		 * @param aKey
		 */
		public void setForeignCurrencyTypeKey(String aKey) {
			this.currencyTypeKey = aKey;
		}

		/**
		 * @return
		 */
		public String getForeignCurrencyTypeKey() {
			return (currencyTypeKey);
		}
	}

	/** ********************************************************************* */
	private class GroupingPanel extends JPanel {
		/**
		 * @param title
		 */
		public GroupingPanel(String title) {
			this.setBackground(theAppMgr.getBackgroundColor());
			this.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(142, 142, 142)), title));
			this.setLayout(new FlowLayout(FlowLayout.RIGHT, (int) (15 * r), (int) (20 * r)));
			/*this.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
		    c.gridwidth = GridBagConstraints.REMAINDER;
		    */
		}
	}

	/** ********************************************************************* */
	private class FieldPanel extends JPanel implements Scrollable {
		/**
		 */
		public FieldPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}

		/**
		 * @return
		 */
		public Dimension getPreferredScrollableViewportSize() {
			return (this.getPreferredSize());
		}

		/**
		 * @param visibleRect
		 * @param orientation
		 * @param direction
		 * @return
		 */
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return (scrollpane.getVerticalDistance());
		}

		/**
		 * @param visibleRect
		 * @param orientation
		 * @param direction
		 * @return
		 */
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return (scrollpane.getVerticalDistance());
		}

		/**
		 * @return true if a viewport should force the Scrollables width to match its own.
		 */
		public boolean getScrollableTracksViewportWidth() {
			return (true);
		}

		/**
		 * @return true if a viewport should force the Scrollables height to match its own.
		 */
		public boolean getScrollableTracksViewportHeight() {
			return (false);
		}
	}

	/** ********************************************************************* */
	private class EODFocusListener implements FocusListener {
		/**
		 */
		public void focusGained(FocusEvent evt) {
			JCMSTextField field = (JCMSTextField) evt.getSource();
			if (field.equals(firstComp))
				scrollpane.getVerticalScrollBar().setValue(0);
			else {
				Point p = field.getParent().getLocation();
				Dimension d = field.getParent().getSize();
				pnlFields.scrollRectToVisible(new Rectangle(p.x, p.y, d.width, d.height));
			}
		}

		/**
		 */
		public void focusLost(FocusEvent evt) {
			KeyedTextField field = (KeyedTextField) evt.getSource();
		ArmCurrency curr = getUserAmount(field.getPaymentKey());
			field.setText(curr.formattedStringValue());
			field.setAmount(curr.doubleValue());
			updateDeposit();
		}
	}

	/** ********************************************************************* */
	private class ExchangeRenderer extends BasicComboBoxRenderer {
		/**
		 * This class assumes that the currency will format like $1.00 (USD), 1 (USD)=0.333 (MEX) and will tokenize it on the comma and use the last
		 * half.
		 */
		public ExchangeRenderer() {
			this.setHorizontalAlignment(SwingConstants.RIGHT);
			this.setBackground(theAppMgr.getEditAreaColor());
		}

		/**
		 * @param list
		 * @param value
		 * @param index
		 * @param isSelected
		 * @param cellHasFocus
		 * @return
		 */
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		ArmCurrency curr = (ArmCurrency) value;
			if (curr != null) {
				String text = curr.formatForeignCurrency();
				if (text.indexOf(") ,") > -1)
					this.setText(text.substring(text.indexOf(") ,") + 3));
			}
			return (this);
		}
	}

	/**
	 * put your documentation comment here
	 * @param txnEOD
	 */
	public void setEODTenderTotal(CMSTransactionEOD txnEOD) {
		String[] paymentTypes = CMSTxnSummary.getPaymentsByRegister();
		for (int index = 0; index < paymentTypes.length; index++) {
			String paymentType = paymentTypes[index];
		ArmCurrency reportedEODTotal = new ArmCurrency(0.0d);
		ArmCurrency eodTotal = CMSTxnSummary.getPaymentAmountByRegister(paymentType);
			int mediaCount = CMSTxnSummary.getMediaCountByRegister(paymentType);
			if (htUserFields.containsKey(paymentType))
				reportedEODTotal = getUserAmount(paymentType);
			else
				reportedEODTotal = eodTotal;
			txnEOD.addEODTenderTotal(paymentType, eodTotal, reportedEODTotal, mediaCount);
			// System.out.println("Payment Type : " + paymentType);
			// System.out.println("Total : " + eodTotal);
		}
		// Add a new value for Drawer Fund
		txnEOD.addEODTenderTotal("DRWFND", theRegister.getDrawerFund(), new ArmCurrency(0.0d), 0);
	}

	/**
	 * put your documentation comment here
	 * @param txnEOD
	 */
	public void setEODTransactionTotal(CMSTransactionEOD txnEOD) {
		String[] transactionTypes = CMSTxnSummary.getTypesByRegister();
		for (int index = 0; index < transactionTypes.length; index++) {
			String transactionType = transactionTypes[index];
		ArmCurrency eodTotal = CMSTxnSummary.getTypeAmountByRegister(transactionType);
			// int mediaCount = CMSTxnSummary.getMediaCountByRegister(paymentType);
			txnEOD.addEODTransactionTotal(transactionType, eodTotal, 0);
		}
	}

	/**
	 * put your documentation comment here
	 * @param txnEOD
	 */
	public void setEODTaxTotal(CMSTransactionEOD txnEOD) {
		txnEOD.addEODTaxTotal("SALES_TAX", result.getSalesTaxTotal());
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public boolean verifyDepositTotals() {
		String[] types = PaymentMgr.getEndSessionPaymentTypes();
		// loop through payment types included in EOD
		for (int i = 0; i < types.length; i++) {
			if (htUserFields.get(types[i]) != null && htSystemFields.get(types[i]) != null
					&& !((KeyedTextField) htUserFields.get(types[i])).getText().equals(((KeyedTextField) htSystemFields.get(types[i])).getText())) {
				return false;
			}
		}
		return true;
	}

	private void openDrawer() {
		System.out.println("->Pop Drawer...");
		try {
			if (fiscalInterface != null && fiscalInterface.openDrawer()) // try the FiscalInterface if available
				return;
		} catch (Exception e) {
			System.out.println("Could not find or use FiscalInterface cash drawer. Trying CMSDrawer...");
		}
		try { // else try CMSDrawer
			CMSDrawer.openDevice(ReceiptConfigInfo.getInstance().getCashDrawerName());
			CMSDrawer.openDrawer();
			CMSDrawer.closeDevice();
		} catch (Exception ex) {
			// CashDrawer not found!
			System.out.println("Cash Drawer Not Found");
		}
	}
        //TD
	/*private class LocalFocusPolicy extends FocusTraversalPolicy {
		private FocusTraversalPolicy ftp = null;
		private Component fComponent = null;
		private Component lComponent = null;
		public LocalFocusPolicy( FocusTraversalPolicy fPolicy, Component firstComp, Component lastComp ) {
			ftp = fPolicy;
			fComponent = firstComp;
			lComponent = lastComp;
		}
		public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
			System.out.println("EOD - Next Component .... ");
			if (aComponent.equals(lComponent) && lComponent.isEnabled() ) {
				System.out.println("EOD - Next Component .... Returning LastComponent..");
                return fComponent;
            } else {
            	return ftp.getComponentAfter(focusCycleRoot, aComponent);
            }
		}
		public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
			System.out.println("EOD - Previous Component .... ");
			if (aComponent.equals(fComponent) && fComponent.isEnabled() ) {
				System.out.println("EOD - Previous Component .... Returning first Component");
                return lComponent;
            } else {
            	return ftp.getComponentBefore(focusCycleRoot, aComponent);
            }
		}
		public Component getDefaultComponent(Container focusCycleRoot) {
            return ftp.getDefaultComponent(focusCycleRoot);
		}
		public Component getLastComponent(Container focusCycleRoot) {
            return ftp.getLastComponent(focusCycleRoot);
		}
		public Component getFirstComponent(Container focusCycleRoot) {
            return ftp.getFirstComponent(focusCycleRoot);
		}
	}*/		
	
}
