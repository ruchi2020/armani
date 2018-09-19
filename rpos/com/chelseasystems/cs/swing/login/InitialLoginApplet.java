/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 7    | 06-15-2005 | Vikram    | 225       |After log in when EOD was not run Input prompt|
 |      |            |           |           |must display "Select menu option to continue" |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 6    | 06-14-2005 | Vikram    | 165       | Operator Login screen - Click inquires-click |
 |      |            |           |           | previous- edit box was not focused by default|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 06-14-2005 | Vikram    | 155       | Customer: New Transactions default  to last  |
 |      |            |           |           | customer viewed through Customer Lookup.     |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 05-29-2005 |Khyati     | N/A       |1. Store Close Out                            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-27-2005 | Rajesh    | N/A       |Fixed problem with locale in applyPreferences |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-01-2005 | Anand     | N/A       |1.Modified to facilitate showing the main menu|
 |      |            |           |           | without log-on.                              |
 |      |            |           |           |2.New main menu created for Armani            |
 --------------------------------------------------------------------------------------------
 */

package com.chelseasystems.cs.swing.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.merchandise.Merchandise;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.register.LightPoleDisplay;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.panel.ScrollerPanel;
import com.chelseasystems.cr.util.HTML;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.ajbauthorization.AJBServiceManager;
import com.chelseasystems.cs.config.AJBPingThreadBootStrap;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.goaling.CMSStoreGoal;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.readings.CMSEmployeeSales;
import com.chelseasystems.cs.readings.CMSMerchandiseSales;
import com.chelseasystems.cs.readings.CMSReadingsHelper;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterSessionAppModel;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.dlg.GenericChooseFromTableDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.model.StoreSalesModel;
import com.chelseasystems.cs.swing.returns.InitialReturnApplet;
import com.chelseasystems.cs.swing.returns.ReturnSaleApplet;
import com.chelseasystems.cs.tax.TaxUtilities;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;
import com.klg.jclass.chart.ChartDataView;
import com.klg.jclass.chart.JCAxis;
import com.klg.jclass.chart.JCBarChartFormat;
import com.klg.jclass.chart.JCChartLabel;
import com.klg.jclass.chart.JCDataCoord;
import com.klg.jclass.chart.beans.SimpleChart;
import com.chelseasystems.cs.util.Version;
import com.ga.fs.fsbridge.ARMFSBridge;
import com.ga.fs.fsbridge.object.ARMFSBOPosEventObject;
import com.ga.fs.fsbridge.object.ARMFSBOSignOnTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBridgeObject;
import com.ga.fs.fsbridge.utils.ARMEventIDGenerator;
import com.ga.fs.fsbridge.utils.ConfigurationManager;
import com.ga.fs.fsbridge.utils.DateUtils;
import com.ga.fs.fsbridge.utils.MessageTypeCode;
import com.ga.fs.fsbridge.utils.PosEvent;
import com.ga.fs.fsbridge.utils.RandomStringIDGenerator;
import com.ga.fs.fsbridge.utils.TransactionTypeCode;
/**
 * @description initial login screen that will be used for the top level security.
 */


public class InitialLoginApplet extends CMSApplet {
	
	/*Added to log info and errors.*/
	private static Logger log = Logger.getLogger(InitialLoginApplet.class.getName());
	private CorpMsgThread corpThread;
	private ConfigMgr configMgr;
	private boolean isLocaleResetAfterLogout;
	JButton emailBtn;
	JTextArea txtCorpMsg;
	ScrollerPanel scroller;
	MediaPanel pnlMedia;
	StoreSalesModel model;
	GoalTable tblSales;
	SimpleChart goalChart;
	JCChartLabel lblGood;
	// JCChartLabel lblExcel;
	Font chartLabelFont;
	JLabel labStoreHeader1;
	JLabel labStoreHeader2;
	private boolean bLoginDone = false;
	SimpleDateFormat fmt = new 
		SimpleDateFormat(com.chelseasystems.cr.util.ResourceManager.getResourceBundle().getString("yyyyMMdd"));
	boolean inquiryMode = false;
	private static ConfigMgr config;
	private String fipay_flag;
	private String isRegisterCloseOutMandatory;
	
	private CMSApplet applet;

	// Initialize the applet
	public void init() {
		try {
		 	String fileName = "store_custom.cfg";
			config = new ConfigMgr(fileName);
			fipay_flag = config.getString("FIPAY_Integration");
			emailBtn = new JButton();
			txtCorpMsg = new JTextArea();
			scroller = new ScrollerPanel();
			pnlMedia = new MediaPanel(true, true);
			model = new StoreSalesModel();
			tblSales = new GoalTable(model);
			goalChart = new SimpleChart();
			lblGood = new JCChartLabel();
			labStoreHeader1 = new JLabel();
			labStoreHeader2 = new JLabel();
			jbInit();
			configMgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
			Merchandise[] merchs = (Merchandise[]) theAppMgr.getGlobalObject("MERCHANDISE");
			pnlMedia.setMerchandise(merchs);
			pnlMedia.actionPerformed(null); // comment this line to prevent showing movie initially
			corpThread = new CorpMsgThread(this);
			corpThread.start();
			isLocaleResetAfterLogout = configMgr.getString("RESET_LOCALE_AFTER_LOGOUT").equalsIgnoreCase("true");
			//mayuri edhara:: added the message to init so that when POS starts the POSitem- reset is done.
			if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
				if (fipay_flag == null) {
					fipay_flag = "Y";
				}
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
					sendIdleMessageData(null,null,false,true,true,"");
				}
			}
			
			
			
			/*
			 * Added by Yves Agbessi (04-Dec-2017)
			 * Handles the Start of Terminal event for Fiscal Solutions Service
			 * START --
			 * */
			
				ARMFSBridge fsBridge = new ARMFSBridge().build();
				if(fsBridge.isCountryAllowed){
					Date now = new Date();
					ARMFSBOPosEventObject startOfTerminal = new ARMFSBOPosEventObject(
							MessageTypeCode.MTC_POS_EVENT, 
							ARMEventIDGenerator.generate(now), 
							PosEvent.PEC_START_OF_TERMINAL_CODE, 
							PosEvent.PEC_START_OF_TERMINAL_DESCRIPTION, 
							Integer.parseInt(PosEvent.PEC_START_OF_TERMINAL_VALUE), 
							now,
							new ConfigurationManager().getConfig(ARMFSBridgeObject.registerCfgFilePath, "STORE_ID") + 
							new ConfigurationManager().getConfig(ARMFSBridgeObject.registerCfgFilePath, "REGISTER_ID"), 
							"DIRETTORE DIRETTORE");
					
					
					if(fsBridge.postObject(startOfTerminal)){
						
						String response = startOfTerminal.processResponse();
						if (response.contains("ERROR")
								|| response.contains("UNABLE")) {
							theAppMgr
									.showErrorDlg("[FS BRIDGE] "
											+ response
											+ "\n Please call the Help Desk Immediately. Press OK to continue");
						
						}
						
					}else{
						
						theAppMgr.showErrorDlg("[FS BRIDGE] : Unable to post START_OF_TERMINAL_EVENT. Press OK to continue\n" +
						"Please call the Help Desk Immediately");
						
					}
				}

			
			/*
			 * 
			 * -- END
			 * */
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setOnTop(boolean isOnTop) {
		//System.out.println("___Tim: " + " Calling WInterface in InitialLoginApplet ....");
		/*
	  	if(isOnTop){
	  		WInterface.setOnTop(theAppMgr.getParentFrame());
	  		//WInterface.setOnTop(theAppMgr.getParentFrame().getOwner());
	  	}else{
	  		WInterface.setNotOnTop(theAppMgr.getParentFrame());
	  		//WInterface.setNotOnTop(theAppMgr.getParentFrame().getOwner());
	  	}
	  	*/
		//TD
  		theAppMgr.getParentFrame().setAlwaysOnTop(isOnTop);
	}

	/**
	 * Start the applet
	 */
	public void start() {
		// Nullify the Return's hashtable after every log off
		// MP : Remove operator
		boolean idleMessage= true;
		boolean clearMessage =  true;
		
	 	String fileName = "store_custom.cfg";
		config = new ConfigMgr(fileName);
		fipay_flag = config.getString("FIPAY_Integration");
		isRegisterCloseOutMandatory = config.getString("REG_CLOSEOUT_FLAG");
		 //Default value of the flag is Y if its not present in credit_auth.cfg
		//Vivek Mishra : Added region check to invoke ajb related change only for US region
		if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
		if (fipay_flag == null) {
			fipay_flag = "Y";
			}		
		}
		//sendIdleMessageData(null,null,true,false,idleMessage,clearMessage);
		if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
		sendIdleMessageData(null,null,false,true,clearMessage,"");
		}
		setOnTop(false);
		theAppMgr.removeStateObject("OPERATOR");
		ReturnSaleApplet.hReturnTxns = null;
		InitialReturnApplet.hReturnTxn = null;
		theAppMgr.setTransitionColor(Color.black);
		tblSales.setEnabled(false);
		theAppMgr.removeStateObject("ARM_DIRECTED_FROM");
		theAppMgr.removeStateObject("ARM_DIRECT_TO");
		theAppMgr.removeStateObject("TXN_CUSTOMER");
		CMSEmployee theOperator = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		if (isBackOfficeRegister()) {
			if (theOperator == null) {
				bLoginDone = false;
			} else {
				theAppMgr.showMenu(MenuConst.MAIN, theOperator);
			}
		}
		LightPoleDisplay.getInstance().startDefaultDisplay();
		if (theAppMgr.getGlobalObject("EOD_COMPLETE") != null) {
			theAppMgr.addStateObject("OPERATOR", theAppMgr.getTransSessionStateObject("LAST_USED_OPR"));

			if (theAppMgr.getStateObject("INQUIRIES_AFTER_EOD") != null) {
				theAppMgr.removeStateObject("INQUIRIES_AFTER_EOD");
				theAppMgr.showMenu(MenuConst.INQUIRIES, theOpr, null);
				theAppMgr.getMainFrame().getGlobalBar().setScreenName("Inquiries");
				inquiryMode = true;
			} else {
				initEODCompleteButtons();
			}
			theAppMgr.setSingleEditArea(res.getString("Register Closeout successfully completed on this machine."));
			return;
		}
		try {
			CMSV12Basket cmsV12Basket = (CMSV12Basket) theAppMgr.getStateObject("V12BASKET_LOOKUP");
			if (cmsV12Basket != null) {
				theAppMgr.removeStateObject("V12BASKET_LOOKUP");
				CMSV12BasketHelper.setBasketStatus(theAppMgr,cmsV12Basket, CMSV12Basket.open);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (theAppMgr.getGlobalObject("EOS_COMPLETE") != null) {
			theAppMgr.addStateObject("OPERATOR", theAppMgr.getTransSessionStateObject("LAST_USED_OPR"));
			initEODCompleteButtons();
			theAppMgr.setSingleEditArea(res.getString("End-of-session successfully completed on this register."));
			return;
		}
		pnlMedia.startTimer();
		// readingsThread.startReading();
		// if the operator from the prior session should remain logged on, then stick him into
		// the repository so he will be reinstated.
		CMSEmployee lastUsedOpr = (CMSEmployee) theAppMgr.getTransSessionStateObject("LAST_USED_OPR");
		if (lastUsedOpr != null) {
			if (lastUsedOpr.isLoggedOffUponGoHome()) {
				printLogOffReceipt(lastUsedOpr);
				theAppMgr.removeTransSessionStateObject("LAST_USED_OPR");
			} else {
				theAppMgr.addStateObject("DUMMY_OPERATOR", "DUMMY_OPERATOR");
			}
		}
		// if the operator has already logged in then show the menu.
		CMSEmployee opr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		boolean bClockInOut = false;
		if (theAppMgr.getStateObject("CLOCK_IN_OUT") != null) {
			bClockInOut = true;
		}
		if (opr != null) {
			theAppMgr.clearStateObjects();
			theAppMgr.addStateObject("OPERATOR", opr);
			if (bClockInOut) {
				theAppMgr.addStateObject("RETURN_CLOCK_IN_OUT", "RETURN_CLOCK_IN_OUT");
				displayMenu(opr);
			} else {
				initButton();
				buildOperator();
			}
		} else {
			if (isLocaleResetAfterLogout) {
				Locale storeLocale = new Locale(((CMSStore) theStore).getPreferredISOLanguage(), 
						((CMSStore) theStore).getPreferredISOCountry());
				Locale frameLocale = ((JFrame) theAppMgr.getMainFrame()).getLocale();
				if (!frameLocale.equals(storeLocale)) {
					theAppMgr.setLocale(storeLocale);
					// get InitialLoginApplet to switch back
					try {
						tblSales.getColumnModel().getColumn(1).setHeaderValue(model.getColumnName(1));
						tblSales.getColumnModel().getColumn(2).setHeaderValue(model.getColumnName(2));
						this.jbInit();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			if (opr == null) {
				theAppMgr.addStateObject("DUMMY_OPERATOR", new CMSEmployee("DUMMY_OPERATOR"));
			}
			initButton();
			theAppMgr.setEditAreaFocus();
			SwingUtilities.invokeLater(new Runnable() {

				/**
				 * put your documentation comment here
				 */
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {

						/**
						 * put your documentation comment here
						 */
						public void run() {
							buildOperator();
						}
					});
				}
			});
		}
		//Send-Sale :: Added to clear out the contents inside the Map created for sendsale. So that every time cashier logs in, the MAP contents is refreshed.
		TaxUtilities.taxDetailMap.clear();
	}

	// Stop the applet
	public void stop() {
		theAppMgr.endTimeOut();
		pnlMedia.stopTimer();
		pnlMedia.stopPlayer();
	}

	// Destroy the applet
	public void destroy() {
		pnlMedia.releaseResources();
	}

	// Get Screen Description
	public String getScreenName() {
		if (inquiryMode) {
			return res.getString("Inquiries");
		}
		return res.getString("Operator Login");
	}

	// Get Applet revision
	public String getVersion() {
		return "$Revision: 1.26.2.8.4.14 $";
	}

	/**
	 * put your documentation comment here
	 * @param command
	 * @param object
	 */
	public void objectEvent(String command, Object object) {
		if (command.equals("OPERATOR")) {
			if (object != null) {
				CMSEmployee opr = (CMSEmployee) object;
				// set Eu preferences
				try {
					applyPreferences(opr);
					printLogOnReceipt(opr);
					theAppMgr.addTransSessionStateObject("LAST_USED_OPR", opr);
					theAppMgr.addStateObject("OPERATOR", opr);
					displayMenu(opr);
				} catch (Exception ex) {
					theAppMgr.showExceptionDlg(ex);
					buildOperator();
				}
			}
		}
	}

	/**
	 * @param date
	 *            Date
	 * @return String
	 */
	private String parseDate(Date date) {
		try {
			return (String) fmt.format(new Date());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 */
	private void displayMenu(CMSEmployee opr) {
	//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		
		/*
		 * /Added by Yves Agbessi (22-Nov-2017) - Handling Fiscal Solutions Bridge for the countries in need
		 */
		// START --
		
		String registerID = new ConfigMgr("register.cfg").getString("REGISTER_ID");
		Date date = new Date();
	ARMFSBridge fsBridge = new ARMFSBridge().build();
		if(fsBridge.isCountryAllowed){
		String id = ARMEventIDGenerator.generate(opr.getExternalID(), registerID,date);
		ARMFSBOSignOnTransactionObject signOnTransaction = new ARMFSBOSignOnTransactionObject(
				MessageTypeCode.MTC_TRANSACTION, 
				id, TransactionTypeCode.TTC_SIGN_ON_TRANSACTION, date, 
				id, opr.getExternalID(), opr.getFirstName() + " " + opr.getLastName());
		
		//Posting SignOnTransaction
		if(!fsBridge.postObject(signOnTransaction)){
			theAppMgr.showErrorDlg("[FS BRIDGE] : Unable to post SIGN_ON_TRANSACTION Object. Press OK to continue\n" +
			"Please call the Help Desk Immediately");
		}
		else{
		String response = signOnTransaction.processResponse();
		if (response.contains("ERROR")
				|| response.contains("UNABLE")) {
			theAppMgr
					.showErrorDlg("[FS BRIDGE] "
							+ response
							+ "\n Please call the Help Desk Immediately. Press OK to continue");
		
		}
		}
		}
		
		// END --
		//
		// theAppMgr.setSingleEditArea(res.getString("Select option."));
		// theAppMgr.showMenu(MenuConst.MAIN, opr, null);
		if (theAppMgr.getGlobalObject("RUN_EOD") == null) {
			// if (!todayEqualsProcessDate()) {
			if (((CMSStore) theStore).isEndOfDayRequired((Date) theAppMgr.getGlobalObject("PROCESS_DATE"))) {
				theAppMgr.addGlobalObject("RUN_EOD", "SHOW_WARNING");
			}
		}
		if (theAppMgr.getGlobalObject("RUN_EOD") != null) {
			// ks: Compare the last ignored suggested EOD date and display main menu if its today
			Date ignoredDate = null;
			String lastDate = null;
			String curDate = null;
			if (theAppMgr.getGlobalObject("ARM_LAST_UNPROCESSED_EOD_DATE") != null) {
				ignoredDate = (Date) theAppMgr.getGlobalObject("ARM_LAST_UNPROCESSED_EOD_DATE");
				lastDate = parseDate(ignoredDate);
			}
			Date currentDate = new Date();
			curDate = parseDate(currentDate);
			if (ignoredDate != null && ((lastDate.equals(curDate)))) {
				displayMenuForNotEOD(opr);
				return;
			} else if (ignoredDate != null && ignoredDate.before(currentDate)) {
				// Start: Added by Himani Verma for forcing register close out 
				if(isRegisterCloseOutMandatory!=null && isRegisterCloseOutMandatory.equals("Y"))
				{
					theAppMgr.showMenu(MenuConst.RUN_EOD_ONLY, opr);
					String warning = "Please perform Register Closeout before continuing";
					theAppMgr.showErrorDlg(res.getString(warning));
				}
				else
					theAppMgr.showMenu(MenuConst.RUN_EOD, opr);
				// End: Added by Himani Verma for forcing register close out
			} else if (((String) theAppMgr.getGlobalObject("RUN_EOD")).equals("SHOW_WARNING")) {
				SwingUtilities.invokeLater(new Runnable() {

					/**
					 * put your documentation comment here
					 */
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {

							/**
							 * put your documentation comment here
							 */
							public void run() {
								// ks: Do not enforce register Close out
								// String warning = "The system has detected that Register Closeout is now required. 
								//You must perform an 'End of
								// Session' or 'Register Closeout' on this terminal before you can continue to use it.";
								String warning = "System has detected a calendar date change. Perform Register Closeout or continue processing";
								theAppMgr.showErrorDlg(res.getString(warning));
							}
						});
					}
				});
			}
			// Start: Added by Himani Verma for forcing register close out 
			if(isRegisterCloseOutMandatory!=null && isRegisterCloseOutMandatory.equals("Y"))
			{
				theAppMgr.showMenu(MenuConst.RUN_EOD_ONLY, opr);
				String warning = "Please perform Register Closeout before continuing";
				theAppMgr.showErrorDlg(res.getString(warning));
			}
			else
				theAppMgr.showMenu(MenuConst.RUN_EOD, opr);
			// End: Added by Himani Verma for forcing register close out
			theAppMgr.setSingleEditArea(res.getString("Select menu option to continue"));
		} else {
			// ks: Moved this code to a method for StoreCloseout Armani
			displayMenuForNotEOD(opr);
		}
	}

	/**
	 * @param opr
	 *            CMSEmployee
	 */
	private void displayMenuForNotEOD(CMSEmployee opr) {
		if (theAppMgr.getStateObject("CLOCK_IN_OUT") == null) {
			try {
				CMSCompositePOSTransaction txn = CMSTransactionPOSHelper.allocate(theAppMgr);
				txn.setConsultant((CMSEmployee) theAppMgr.getStateObject("OPERATOR"));
				theAppMgr.addStateObject("NEW_TXN_POS", ""); // add state object that flags a new txn as been started
				theAppMgr.addStateObject("TXN_POS", txn);
			} catch (Exception ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
			if (theAppMgr.getStateObject("RETURN_CLOCK_IN_OUT") == null) {
				if (!isBackOfficeRegister()) {
					CMSApplet.theAppMgr.fireButtonEvent("CONSULTANT_LOOKUP");
				} else {
					if (bLoginDone) {
						theAppMgr.showMenu(MenuConst.MAIN, opr);
						theAppMgr.setSingleEditArea(res.getString("Select Option"));
						// bLoginDone = false;
					}
				}
				theAppMgr.removeStateObject("RETURN_CLOCK_IN_OUT");
			} else {
				initButton();
				buildOperator();
			}
			// theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, opr, theStore);
			theAppMgr.startTimeOut(60);
		} else {
			CMSApplet.theAppMgr.fireButtonEvent("CLOCK_IN_OUT_HIDDEN");
		}
	}

	/**
	 * Method to show a blank menu before the operator has logged in.
	 */
	public void initButton() {
		inquiryMode = false;
		String registerType = (String) ((CMSRegister) theAppMgr.getGlobalObject("REGISTER")).getRegisterType();
		// theAppMgr.showErrorDlg(((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getRegisterType());
		if (registerType != null && registerType.length() > 0) {
			if ((registerType.equals("B"))) {
				if (!bLoginDone) {
					theAppMgr.showMenu(MenuConst.BACK_OFFICE, theOpr);
					bLoginDone = true;
				} else {
					theAppMgr.showMenu(MenuConst.MAIN, (CMSEmployee) theAppMgr.getStateObject("OPERATOR"));
				}
				// theAppMgr.removeStateObject("FROM_PREV");
				return;
			}
		}
		theAppMgr.showMenu(MenuConst.MAIN, theOpr);
		theAppMgr.setEditAreaFocus();
		// theAppMgr.removeStateObject("FROM_PREV");
	}

	/**
	 */
	public void initEODCompleteButtons() {
		theAppMgr.showMenu(MenuConst.REGISTER_CLOSEOUT_COMPLETE, theOpr);
	}

	/**
	 * This method will be redone before 2.5
	 */
	private void applyPreferences(CMSEmployee opr) throws Exception {	
		Locale aLocale = new Locale(((CMSStore) theStore).getPreferredISOLanguage(), 
				((CMSStore) theStore).getPreferredISOCountry());
		if (!aLocale.equals(this.getLocale())) {
			theAppMgr.setLocale(aLocale);
		}
	}

	/**
	 * @param aLocale
	 *            a locale object to apply
	 */
	public void setLocale(Locale aLocale) {
		labStoreHeader1.setText(res.getString("Corporate Information") + ":");
		labStoreHeader2.setText(res.getString("Store Sales") + ":");
		emailBtn.setText(res.getString("Corporate E-Mail"));
		JLabel header = new JLabel(HTML.formatLabeltoHTML(res.getString("Weekly Store\nSales / Goal"), 
				theAppMgr.getTheme().getHeaderFont(), Color.white));
		goalChart.setHeader(header);
		tblSales.getColumnModel().getColumn(1).setHeaderValue(model.getColumnName(1));
		tblSales.getColumnModel().getColumn(2).setHeaderValue(model.getColumnName(2));
		this.validate();
		this.repaint();
		super.setLocale(aLocale);
		Thread updatesales = new Thread(new Runnable() {

			/**
			 *
			 */
			public void run() {
				loadEmployeeSales();
				loadStoreGoals();
			}
		});
		updatesales.start();
	}

	/**
	 */
	private void buildOperator() {
		this.buildOperator(null);
	}

	/**
	 */
	private void buildOperator(String initialValue) {
		theAppMgr.setEditAreaFocus();
		theAppMgr.buildObject(this, "OPERATOR", configMgr.getString("OPERATOR_BUILDER"), initialValue);
	}

	/**
	 */
	void setCorpMsg(String msg) {
		txtCorpMsg.setText(msg);
		txtCorpMsg.repaint();
	}

	/**
	 */
	private void pnlMedia_mousePressed(MouseEvent e) {
		pnlMedia.togglePlayer();
	}

	/**
	 * We shouldn't get any edit area events with this applet. All login 
     * interaction should be handled with ObjectBuilders that pass Employee objects
	 * to this gui.
	 * 
	 * @param anEvent
	 */
	public void editAreaEvent(String command, String value) {
		if (value != null) {
			// this should never happen!, instantiate builder and pass this value
			// cr 6099 - sometimes app comes up with editarea not registered to builder
			this.buildOperator(value);
		}
	}

	/**
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		theAppMgr.getMainFrame().getGlobalBar().setScreenName(res.getString("Operator Login"));
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("CONSULTANT_LOOKUP")) {
			try {
				CMSCompositePOSTransaction txn = CMSTransactionPOSHelper.allocate(theAppMgr);
				txn.setConsultant((CMSEmployee) theAppMgr.getStateObject("DUMMY_OPERATOR"));
				// 5062 we don't really need to set a "default" customer, leave it null
				// txn.setCustomer(CMSCustomerHelper.getDefaultCustomer(theAppMgr));
				theAppMgr.addStateObject("NEW_TXN_POS", ""); // add state object that flags a new txn as been started
				theAppMgr.addStateObject("TXN_POS", txn);
			} catch (Exception ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
				anEvent.consume();
			}
		}
		else if (sAction.equals("SHUTDOWN")) {
			shutdown(); // shutdown terminal
			anEvent.consume();
		} 
		else if(sAction.equals("ENDOFYEAR")){
			theAppMgr.setSingleEditArea("Headquarter's Authorization needed before continuing");
			theAppMgr.showOptionDlg(res.getString("Print Fiscal Receipt Failed."), res.getString("You are about to run the 'end of year' procedure on this client."
					+ "This is the STORE MANAGER procedure and your branch's Head Quarter's AUTHORIZATION is MANDATOERY before "
					+ "proceeding, as this a FISCAL PROCEDURE.Please refer to the Head quarters for the AUTHORIZATION PASSWORD,"
					+ "then click 'Continue' to proceed"), res.getString("Cancel"), res.getString("Continue"));
			if(anEvent.equals("Continue"))
			{
				  theAppMgr.setSingleEditArea(applet.res.getString("Please insert the authorization password(received from the HQ) to complete the 'End Of Year' procedure"), "CASH");
			}
		}
		else if (sAction.equals("MERCH_RETURN")) {
			theAppMgr.addStateObject("RETURN_MODE", "");
		} else if (sAction.equals("INQUIRIES")) {
			theAppMgr.showMenu(MenuConst.INQUIRIES, theOpr, null);
			theAppMgr.getMainFrame().getGlobalBar().setScreenName("Inquiries");
			inquiryMode = true;
		} else if (sAction.equals("MAIN_LOG_OFF")) {
			printLogOffReceipt((CMSEmployee) theAppMgr.getStateObject("OPERATOR"));
			theAppMgr.removeTransSessionStateObject("LAST_USED_OPR");
			theAppMgr.goHome();
		} else if (sAction.equals("CLOCK_IN_OUT")) {
			theAppMgr.addStateObject("CLOCK_IN_OUT", "CLOCK_IN_OUT");
			// theAppMgr.setSingleEditArea(res.getString("Enter Cashier ID "));
			buildOperator(null);
			// theAppMgr.buildObject(this, "OPERATOR", configMgr.getString("OPERATOR_BUILDER"), null);
			// theAppMgr.fireButtonEvent("CLOCK_IN_OUT_HIDDEN");
		}
		// ks: Reinitialize the InitalLoginApplet and set the current Date in the global object
		else if (sAction.equals("CONTINUE_PROCESSING")) {
			// ks: Add global object to check date and redirect the applet to consultant sales applet
			theAppMgr.addGlobalObject("ARM_LAST_UNPROCESSED_EOD_DATE", new Date());
			// initButton();
			displayMenuForNotEOD((CMSEmployee) theOpr);
			anEvent.consume();
		}else if (sAction.equals("LAUNCH_BROWSER")) {
	    	// Load list from websites.cfg
	    	ConfigMgr websitesCfg = new ConfigMgr("websites.cfg");
	    	String validUrls = websitesCfg.getString("URL_LOCATIONS");
	    	if (validUrls == null) {
	    		validUrls = "";
	    	}
	    	ArrayList vec = new ArrayList();
	    	StringTokenizer strTok = new StringTokenizer(validUrls, ",");
	    	while (strTok.hasMoreTokens()) {
	    		String prefix = (String)strTok.nextToken();
	    		String url = websitesCfg.getString(prefix + ".URL").trim();
	    		String desc = websitesCfg.getString(prefix + ".DESC").trim();
	    		if (url == null || desc == null || url.length() == 0 || desc.length() == 0) {
	    			continue;
	    		}
	    		vec.add(new GenericChooserRow(new String[] {desc}, url));
	    	}
		    GenericChooserRow[] availRows = (GenericChooserRow[])vec.toArray(new GenericChooserRow[0]);
		    GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(),
		                                                                  theAppMgr, availRows, new String[] {
		                                                                      (res.getString("Web Locations"))
		                                                                  });
		    dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
		    dlg.setVisible(true);
		    if(dlg.isOK()) {
		    	// Launch browser
		    	String url = (String)dlg.getSelectedRow().getRowKeyData();
		    	// Get browser
		    	String browser = websitesCfg.getString("LAUNCH_BROWSER");
		    	try {
		    		Runtime.getRuntime().exec(browser + " " + url);
		    	} catch (Exception ee) {
		    		System.err.println("Failed to launch " + browser + " " + url);
		    		ee.printStackTrace();
		    	}
		    }
	    }
	}

	/**
	 */
	public void updatesSales() {
		loadEmployeeSales();
		loadStoreGoals();
		loadStoreSales();
	}

	/**
	 */
	private void loadEmployeeSales() {
		try {
			ObjectStore store = new ObjectStore(FileMgr.getLocalFile("sales", "empsales.dat"));
			// System.out.println("InitialLoginApplet.loadEmployeeSales()->empsales.dat exists="+store.exists());
			if (store.exists()) {
				Hashtable htSales = (Hashtable) store.read();
				if (htSales == null || htSales.size() < 1) {
					return;
				}
				String[] msgs = new String[htSales.size()];
				int count = 0;
				String[] data = new String[htSales.size()];
				for (Enumeration enm = htSales.keys(); enm.hasMoreElements(); count++) {
					CMSEmployee emp = (CMSEmployee) enm.nextElement();
					CMSEmployeeSales[] sales = (CMSEmployeeSales[]) htSales.get(emp);
				ArmCurrency daySales = new ArmCurrency(0.0);
					for (int x = 0; x < sales.length; x++) {
						daySales = daySales.add(sales[x].getDayNetAmount());
					}
					StringBuffer sb = new StringBuffer();
					sb.append(emp.getFirstName());
					if (emp.getLastName().length() > 0) {
						sb.append(" " + emp.getLastName().substring(0, 1) + ".");
					}
					String msg = emp.getFirstName() + " " + 
						emp.getLastName().substring(0, 1) + " - " + 
						res.getString("sales for today") + ": " + daySales.formattedStringValue();
					data[count] = new String(msg);
				}
				scroller.setScrollerData(data);
			}
		} catch (CurrencyException cex) {
			LoggingServices.getCurrent().logMsg("InitialLoginApplet", "loadEmployeeSales", "Possible difference in currency type", "", LoggingServices.MAJOR, cex);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void loadStoreSales() {
		try {
			ObjectStore store = new ObjectStore(FileMgr.getLocalFile("sales", "storesales.dat"));
			if (store.exists()) {
				CMSMerchandiseSales[] sales = (CMSMerchandiseSales[]) store.read();
			ArmCurrency daySales = new ArmCurrency(0.0);
			ArmCurrency weekSales = new ArmCurrency(0.0);
				for (int x = 0; x < sales.length; x++) {
					daySales = daySales.add(sales[x].getNetSalesDay());
					weekSales = weekSales.add(sales[x].getNetSalesWtd());
				}
				model.setActualSales(daySales, weekSales);
				tblSales.repaint();
			}
		} catch (CurrencyException cex) {
			LoggingServices.getCurrent().logMsg("InitialLoginApplet", "loadStoreSales", "Possible difference in currency type", "", LoggingServices.MAJOR, cex);
		} catch (Exception ex) {
			System.out.println("Exception loadStoreSales()->" + ex);
		}
	}

	/**
	 * Method to load new Store Goals. If there is an excellent goal, resize the Y-axis and bind the labels to the appropriate data coordinates.
	 */
	private void loadStoreGoals() {
		try {
			ObjectStore store = new ObjectStore(FileMgr.getLocalFile("sales", "storegoals.dat"));
			// System.out.println("InitialLoginApplet.loadStoreSales()->storegoals.dat exists=" + store.exists());
			if (store.exists()) {
				CMSStoreGoal goals = (CMSStoreGoal) store.read();
			ArmCurrency yearGoal = new ArmCurrency(goals.getGoal().intValue());
			ArmCurrency good = yearGoal.multiply(.01923); // take yearly number and divide by 52 weeks
				// ArmCurrency excel = new ArmCurrency(goals.getGoal().intValue() * 1.10);
				// model.setGoalsWeek(good, excel);
				model.setGoalsWeek(good);
				tblSales.repaint();
				if (good.greaterThan(new ArmCurrency(0.0))) {
					goalChart.setYAxisMinMax("0.0," + good.doubleValue());
					lblGood.setDataCoord(new JCDataCoord(0, good.doubleValue()));
					String gAmt = good.formattedStringValue(); // remove decimals...
					lblGood.setText(HTML.formatLabeltoHTML(
							res.getString("Goal") + "\n" + gAmt.substring(0, gAmt.length() - 3), 
							chartLabelFont, Color.white));
				} else { // reset chart and labels
					goalChart.setYAxisMinMax("0.0,38.0");
					lblGood.setDataCoord(new JCDataCoord(0, 30));
					lblGood.setText(HTML.formatLabeltoHTML(res.getString("Goal"), chartLabelFont, Color.white));
				}
			}
		} catch (CurrencyException cex) {
			LoggingServices.getCurrent().
				logMsg("InitialLoginApplet", "loadStoreGoals", "Possible difference in currency type", "", 
						LoggingServices.MAJOR, cex);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void shutdown() {
		try {
			if (this.getPendingTxnsNum() == 0 || !theAppMgr.isOnLine())
				theAppMgr.restartTerminal();
/*
			 * Added by Yves Agbessi (04-Dec-2017)
			 * Handles the Shutdown event for Fiscal Solutions Service
			 * START --
			 * */
			
				ARMFSBridge fsBridge = new ARMFSBridge().build();
				if(fsBridge.isCountryAllowed){
					Date now = new Date();
					ARMFSBOPosEventObject terminalShutdownEvent = new ARMFSBOPosEventObject(
							MessageTypeCode.MTC_POS_EVENT, 
							ARMEventIDGenerator.generate(now), 
							PosEvent.PEC_TERMINAL_SHUTDOWN_CODE, 
							PosEvent.PEC_TERMINAL_SHUTDOWN_DESCRIPTION, 
							Integer.parseInt(PosEvent.PEC_TERMINAL_SHUTDOWN_VALUE), 
							now,
							new ConfigurationManager().getConfig(ARMFSBridgeObject.registerCfgFilePath, "STORE_ID") + 
							new ConfigurationManager().getConfig(ARMFSBridgeObject.registerCfgFilePath, "REGISTER_ID"), 
							"DIRETTORE DIRETTORE");
					
					
					if(fsBridge.postObject(terminalShutdownEvent)){
						
						String response = terminalShutdownEvent.processResponse();
						if (response.contains("ERROR")
								|| response.contains("UNABLE")) {
							theAppMgr
									.showErrorDlg("[FS BRIDGE] "
											+ response
											+ "\n Please call the Help Desk Immediately. Press OK to continue");
						
						}
						
					}else{
						
						theAppMgr.showErrorDlg("[FS BRIDGE] : Unable to post TERMINAL_SHUTDOWN_EVENT. Press OK to continue\n" +
						"Please call the Help Desk Immediately");
						
					}
				}

			
			/*
			 * 
			 * -- END
			 * */
			else
				theAppMgr.showErrorDlg(res.getString("Transaction is being Processed. Please Try later"));
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * Return <code>true</code> if the current processing date is the same as today's calendar date.
	 */
	// Let Store decide when it is time to do end-of-day
	// private boolean todayEqualsProcessDate () {
	// SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	// String todayStr = fmt.format(new Date());
	// String processDateStr = fmt.format(theAppMgr.getGlobalObject("PROCESS_DATE"));
	// return (todayStr.equals(processDateStr)) ? true : false;
	// }
	/**
	 * Method required to implement IGoalingRMIPeer
	 * @param amt
	 * @exception RemoteException
	 */
	public void setDailyGoalsGood(String currency) throws RemoteException {
		model.setValueAt(currency, 1, 1);
		tblSales.repaint();
	}

	/**
	 * Method required to implement IGoalingRMIPeer
	 * @param amt
	 * @exception RemoteException
	 */
	public void setDailyGoalsExcel(String currency) throws RemoteException {
		model.setValueAt(currency, 2, 1);
		tblSales.repaint();
	}

	/**
	 * put your documentation comment here
	 * @param opr
	 */
	public void printLogOffReceipt(CMSEmployee opr) {
		CMSRegisterSessionAppModel sessionAppModel = new CMSRegisterSessionAppModel();
		sessionAppModel.setRegister((CMSRegister) theAppMgr.getGlobalObject("REGISTER"));
		sessionAppModel.setSessionDate(new Date());
		sessionAppModel.setStore((CMSStore) theAppMgr.getGlobalObject("STORE"));
		sessionAppModel.setOperator(opr);
		sessionAppModel.print(ReceiptBlueprintInventory.CMSOperatorLogOff, theAppMgr);
	}

	/**
	 * put your documentation comment here
	 * @param opr
	 */
	public void printLogOnReceipt(CMSEmployee opr) {
		CMSRegisterSessionAppModel sessionAppModel = new CMSRegisterSessionAppModel();
		sessionAppModel.setRegister((CMSRegister) theAppMgr.getGlobalObject("REGISTER"));
		sessionAppModel.setSessionDate(new Date());
		sessionAppModel.setStore((CMSStore) theAppMgr.getGlobalObject("STORE"));
		sessionAppModel.setOperator(opr);
		sessionAppModel.print(ReceiptBlueprintInventory.CMSOperatorLogOn, theAppMgr);
	}

	/**
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		JPanel pnlEast = new JPanel();
		JPanel pnlWest = new JPanel();
		JPanel pnlSouth = new JPanel();
		JPanel pnlCenter = new JPanel();
		final JPanel pnlTable = new JPanel();
		JPanel pnlSalesFigures = new JPanel();
		JLabel lblLogo = new JLabel();
		this.setBackground(Color.black);
		pnlWest.setLayout(new BorderLayout());
		pnlTable.setLayout(new BorderLayout());
		pnlSouth.setLayout(new BorderLayout());
		pnlSalesFigures.setLayout(new BorderLayout(0, 10));
		labStoreHeader2.setPreferredSize(new Dimension(60, 18));
		// this.add(pnlSouth, BorderLayout.SOUTH);
		// this.add(pnlEast, BorderLayout.EAST);
		this.add(pnlWest, BorderLayout.CENTER);
		pnlEast.setBackground(Color.black);
		pnlTable.add(tblSales.getTableHeader(), BorderLayout.CENTER);
		pnlTable.add(tblSales, BorderLayout.SOUTH);
		pnlEast.add(goalChart, null);
		pnlWest.setBackground(Color.black);
		pnlWest.add(lblLogo, BorderLayout.CENTER);
		// pnlWest.add(pnlMedia, BorderLayout.CENTER);
		// this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.add(labStoreHeader1);
		pnlCenter.add(txtCorpMsg);
		pnlCenter.add(emailBtn);
		pnlCenter.add(pnlSalesFigures, null);
		pnlSalesFigures.add(pnlTable, BorderLayout.SOUTH);
		pnlSalesFigures.add(labStoreHeader2, BorderLayout.CENTER);
		pnlMedia.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pnlMedia.addMouseListener(new MouseAdapter() {

			/**
			 * @param e
			 */
			public void mousePressed(MouseEvent e) {
				pnlMedia_mousePressed(e);
			}
		});
		pnlSouth.add(scroller, BorderLayout.CENTER);
		pnlSouth.setBackground(Color.black);
		JLabel buffer = new JLabel();
		pnlSouth.add(buffer, BorderLayout.NORTH);
		pnlCenter.setBackground(Color.black);
		pnlSalesFigures.setBackground(Color.black);
		lblLogo.setIcon(theAppMgr.getTheme().getLogoIcon());
		lblLogo.setIconTextGap(SwingConstants.CENTER);
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		txtCorpMsg.setDisabledTextColor(Color.white);
		txtCorpMsg.setLineWrap(true);
		txtCorpMsg.setWrapStyleWord(true);
		txtCorpMsg.setBackground(Color.black);
		txtCorpMsg.setFont(theAppMgr.getTheme().getMessageFont());
		txtCorpMsg.setName("MESSAGE");
		txtCorpMsg.setEditable(false);
		txtCorpMsg.setEnabled(false);
		labStoreHeader1.setOpaque(true);
		labStoreHeader1.setText(res.getString("Corporate Information") + ":");
		labStoreHeader1.setVerticalAlignment(SwingConstants.BOTTOM);
		labStoreHeader1.setFont(theAppMgr.getTheme().getTitleFont());
		labStoreHeader1.setName("TITLE");
		labStoreHeader1.setBackground(Color.black);
		labStoreHeader1.setForeground(Color.white);
		labStoreHeader1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		labStoreHeader2.setText(res.getString("Store Sales") + ":");
		labStoreHeader2.setVerticalAlignment(SwingConstants.BOTTOM);
		labStoreHeader2.setOpaque(true);
		labStoreHeader2.setFont(theAppMgr.getTheme().getTitleFont());
		labStoreHeader2.setName("TITLE");
		labStoreHeader2.setBackground(Color.black);
		labStoreHeader2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		labStoreHeader2.setForeground(Color.white);
		emailBtn.setBackground(Color.black);
		emailBtn.setFont(theAppMgr.getTheme().getMessageFont());
		emailBtn.setForeground(Color.white);
		// emailBtn.setBorder(null);
		emailBtn.setHorizontalAlignment(SwingConstants.LEADING);
		emailBtn.setHorizontalTextPosition(SwingConstants.LEADING);
		// emailBtn.setIcon(mailLogo);
		emailBtn.setText(res.getString("Corporate E-Mail"));
		emailBtn.setVerticalAlignment(SwingConstants.TOP);
		emailBtn.setVerticalTextPosition(SwingConstants.TOP);
		emailBtn.addActionListener(new EmailButtonListener());
		tblSales.setRowSelectionAllowed(false);
		tblSales.setEnabled(false);
		tblSales.setBackground(Color.black);
		tblSales.setForeground(Color.white);
		tblSales.setAutoscrolls(false);
		tblSales.setOpaque(true);
		tblSales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblSales.setGridColor(Color.lightGray);
		tblSales.setFont(theAppMgr.getTheme().getTableFont());
		tblSales.setName("TABLE");
		tblSales.getTableHeader().setReorderingAllowed(false);
		tblSales.getTableHeader().setResizingAllowed(false);
		tblSales.getTableHeader().setFont(theAppMgr.getTheme().getHeaderFont());
		tblSales.getTableHeader().setName("HEADER");
		tblSales.getTableHeader().setForeground(Color.white);
		tblSales.getTableHeader().setBackground(Color.black);
		tblSales.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.lightGray));
		JTextField editor = new JTextField();
		editor.setFont(theAppMgr.getTheme().getTableFont());
		editor.setName("TABLE");
		tblSales.setDefaultEditor(Object.class, new DefaultCellEditor(editor));
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.RIGHT);
		tblSales.getColumnModel().getColumn(1).setCellRenderer(renderer);
		tblSales.getColumnModel().getColumn(2).setCellRenderer(renderer);
		// this listener will keep table from have extra line below bottom row
		tblSales.getTableHeader().addComponentListener(new ComponentAdapter() {

			/**
			 * @param e
			 */
			public void componentResized(ComponentEvent e) {
				int h = pnlTable.getHeight() - tblSales.getTableHeader().getHeight();
				tblSales.setRowHeight(h / 2);
			}
		});
		scroller.setInterval(40);
		scroller.setPause(1);
		scroller.addMouseListener(new ScrollerMouseListener());
		goalChart.setRequestFocusEnabled(false);
		goalChart.setDoubleBuffered(true);
		JLabel header = new JLabel(HTML.formatLabeltoHTML(res.getString("Weekly Store\nSales / Goal"), theAppMgr.getTheme().getHeaderFont(), Color.white));
		header.setName("HEADER");
		goalChart.setHeader(header);
		// goalChart.setData("ARRAY 'Goal BarChart' 2 1 'Sales' '' 0.0 'Good' 30.0 'Excellent' 8.0 ");
		ChartDataView dv = goalChart.getDataView(0);
		dv.setDataSource(model);
		dv.getSeries(0).getStyle().setFillColor(Color.blue);
		// dv.getSeries(1).getStyle().setFillColor(Color.blue);
		goalChart.setYAxisAnnotationMethod(JCAxis.VALUE_LABELS);
		goalChart.setXAxisAnnotationMethod(JCAxis.VALUE_LABELS);
		goalChart.setYAxisMinMax("0.0,38.0");
		goalChart.setBackground(Color.black);
		goalChart.setForeground(Color.black);
		goalChart.setChartType(com.klg.jclass.chart.JCChart.STACKING_BAR);
		goalChart.setView3D("20,24,21");
		((JCBarChartFormat) dv.getChartFormat()).setClusterWidth(63);
		// goalChart.setAllowUserChanges(true); // allow the chart options to show upon rightclick
		// goalChart.setTrigger(0, new EventTrigger(InputEvent.META_MASK, EventTrigger.CUSTOMIZE));
		chartLabelFont = theAppMgr.getTheme().getTextFieldFont();
		lblGood.setAnchor(JCChartLabel.SOUTHWEST);
		lblGood.setAttachMethod(JCChartLabel.ATTACH_DATACOORD);
		lblGood.setConnected(true);
		lblGood.setDataCoord(new JCDataCoord(0, 30));
		lblGood.setText(HTML.formatLabeltoHTML(res.getString("Good"), chartLabelFont, Color.white));
		goalChart.getChartLabelManager().addChartLabel(lblGood);
		// goalChart.getChartLabelManager().addChartLabel(lblExcel);
		// lblExcel.setOffset(new Point(-46, 4));
		lblGood.setOffset(new Point(-46, -4));
		lblGood.setOffset(new Point(-46, -4));
		tblSales.setRowHeight(45);
		pnlEast.setPreferredSize(new Dimension(200, 10));
		pnlWest.setPreferredSize(new Dimension(260, 10));
		// pnlMedia.setPreferredSize(new Dimension(260, 360));
		pnlSouth.setPreferredSize(new Dimension(10, 80));
		buffer.setPreferredSize(new Dimension(5, 5));
		pnlSalesFigures.setPreferredSize(new Dimension(340, 178));
		// lblLogo.setPreferredSize(new Dimension(260, 150));
		txtCorpMsg.setPreferredSize(new Dimension(364, 160));
		labStoreHeader1.setPreferredSize(new Dimension(364, 60));
		emailBtn.setPreferredSize(new Dimension(340, 80));
		pnlTable.setPreferredSize(new Dimension(10, 125));
		scroller.setPreferredSize(new Dimension(600, 80));
		goalChart.setPreferredSize(new Dimension(190, 515));
		// lblExcel.setOffset(new Point((int)(-46, (int)(4));
		lblGood.setOffset(new Point((int) (-46 * r), (int) (-4 * r)));
		tblSales.setRowHeight((int) (45 * r));
		pnlEast.setPreferredSize(new Dimension((int) (200 * r), (int) (10 * r)));
		pnlWest.setPreferredSize(new Dimension((int) (260 * r), (int) (10 * r)));
		// pnlMedia.setPreferredSize(new Dimension((int)(260*r), (int)(360*r)));
		pnlSouth.setPreferredSize(new Dimension((int) (10 * r), (int) (80 * r)));
		buffer.setPreferredSize(new Dimension((int) (5 * r), (int) (5 * r)));
		pnlSalesFigures.setPreferredSize(new Dimension((int) (340 * r), (int) (178 * r)));
		// lblLogo.setPreferredSize(new Dimension((int)(260*r), (int)(150*r)));
		txtCorpMsg.setPreferredSize(new Dimension((int) (364 * r), (int) (160 * r)));
		labStoreHeader1.setPreferredSize(new Dimension((int) (364 * r), (int) (60 * r)));
		emailBtn.setPreferredSize(new Dimension((int) (340 * r), (int) (80 * r)));
		pnlTable.setPreferredSize(new Dimension((int) (10 * r), (int) (125 * r)));
		scroller.setPreferredSize(new Dimension((int) (600 * r), (int) (80 * r)));
		goalChart.setPreferredSize(new Dimension((int) (190 * r), (int) (515 * r)));
	}

	/** ****************************************************************** */
	private class GoalTable extends JTable {

		/**
		 * @param TableModel
		 *            model
		 */
		public GoalTable(TableModel model) {
			super(model);
		}
		/**
		 * Method will send entered goaling information to peers. public void editingStopped(ChangeEvent e) { super.editingStopped(e); Remote[] rmts =
		 * theAppMgr.getPeerStubs("goaling"); for (int i = 0; i < rmts.length; i++) try { IGoalingRMIPeer goalingService = (IGoalingRMIPeer) rmts[i];
		 * goalingService.setDailyGoalsGood((String)model.getValueAt(1, 1)); goalingService.setDailyGoalsExcel((String)model.getValueAt(2, 1)); }
		 * catch (Exception ex) { ex.printStackTrace(); } }
		 */
	}

	/** ****************************************************************** */
	private class EmailButtonListener implements ActionListener {

		/**
		 * Method will respond to emailBtn presses.
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				if (theAppMgr.showOptionDlg(res.getString("Corporate E-Mail"), res.getString("The email browser has been selected and will take a few " + "minutes to load.  Do wish to continue?"))) {
					String file = configMgr.getFileName("EXT_URL");
					// Runtime.getRuntime().exec(emailFile);
					HTML.displayURL(file);
				}
				theAppMgr.setEditAreaFocus();
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
		}
	}

	/** ****************************************************************** */
	private class ScrollerMouseListener extends MouseAdapter {

		/**
		 * Method required to implement IScrollViewer
		 */
		public void mousePressed(MouseEvent me) {
			try {
				Date theDate = (Date) theAppMgr.getGlobalObject("PROCESS_DATE");
				CMSEmployee[] empList = CMSReadingsHelper.findEmployeesForReadings(theAppMgr, (CMSStore) theStore, theDate);
				if (empList == null || empList.length == 0) {
					if (theAppMgr.isOnLine()) {
						theAppMgr.showErrorDlg(res.getString("No employees have sales today."));
					} else {
						theAppMgr.showErrorDlg(res.getString("Sales reporting is unavailable in offline mode."));
					}
					return;
				}
				theAppMgr.addStateObject("EMP_LIST", empList);
				theAppMgr.fireButtonEvent("CON_SALES");
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public boolean isBackOfficeRegister() {
		CMSRegister register = (CMSRegister) CMSApplet.theAppMgr.getGlobalObject("REGISTER");
		if (register.getRegisterType() != null && register.getRegisterType().length() > 0) {
			if (register.getRegisterType().equals("B")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get number of pending txns
	 */
	public int getPendingTxnsNum() {
		ConfigMgr config = new ConfigMgr("txnposter.cfg");
		String theDir = FileMgr.getLocalDirectory(config.getString("PENDING_DIRECTORY"));
		File file = new File(theDir);
		String[] fns = file.list();
		return fns.length;
	}
	
	private boolean sendIdleMessageData(POSLineItem line,POSLineItem[] lineItemArray ,boolean Refresh, boolean idleMessage, boolean clearMessage, String discountAmt) {
		try {
			String result = "";
			//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
				//End
			String responseArray[] = null;
			String ajbResponse[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				if(!storeCountry.equals("CAN"))
				{
					
					// Initiating the AJB Service Manager
					AJBServiceManager ajbsrvmgr = AJBServiceManager.getCurrent();
					
					if (log.isDebugEnabled())
					{
						
						log.info("initilizing the socket for Item bucket");
					}
					
					ajbsrvmgr.initiateItemSocket();
					
					//String tt = orgTxn.getTransactionType();
					/*boolean ajbpingecho = AJBPingThreadBootStrap.echoping();
					if (ajbpingecho)
					{*/
						responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line,lineItemArray,Refresh,idleMessage,clearMessage,discountAmt,false);
						if(responseArray!=null)
						{
							//End
							int length = responseArray.length;
							for (int i=0; i<length ;i++){
							//Vivek Mishar : Added one codition for blank respose when AJB server gets down in middle of transaction.	
							//if(responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers")||responseArray[i].equals(""))){
							//Vivek Mishra : Removed the blak condition as it was causing the All AJB Server down dialogue even in case of server is recover. 
								if(responseArray[i] != null && responseArray[i].toString().contains("All the Ajb Servers")){
									//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
									//if(!Refresh)
									//count++;
									/*if(count==qty)
						{*/
									theAppMgr.showErrorDlg("All the AJB servers are down");
									/*count=0;
						}//End
*/										return false;
								}
								
								else if(responseArray[i] != null && responseArray[i].toString().contains("-1"))
								{
									theAppMgr.showErrorDlg("All the AJB servers are down");
										return false;
								}
							}
						}
					/*} else
					{
						theAppMgr.showErrorDlg("All the AJB servers are down");
						return false;
					}*/
				}else
					
				if (responseArray == null) {
					return true;
				}
				//count=0;	
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showErrorDlg("All the AJB servers are down");
			//theAppMgr.showExceptionDlg(ex);
			return (false);
		}
	
  }

} // InitialLoginApplet

