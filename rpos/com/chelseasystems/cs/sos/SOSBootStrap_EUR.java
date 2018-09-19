/*
 * Copyright 2001, Chelsea Market Systems LLC
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 4    | 06-13-2005 |Vikram     |  188      | SOD: Password is not verified during SOD        |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 3    | 04-29-2005 |Pankaja    |           |  Resolved the problem related to Transaction
 |      |            |           |           |  number.                                               |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method for validating UserID &
 |      |            |           |           |  Password.                                      |
 |      |            |           |           |  SetTxnNumber for the Txn                       |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.sos;

import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.MainFrame;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.register.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.swing.session.*;
import com.chelseasystems.cs.txnnumber.*;
import com.chelseasystems.cs.txnposter.*;
import com.chelseasystems.rb.*;
import jpos.*;
import com.chelseasystems.cr.txnnumber.TransactionNumberStore;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;


/**
 *
 * <p>Title: SOSBootStrap</p>
 *
 * <p>Description: This class drives the GUI for the Start-of-day process.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SOSBootStrap_EUR implements IBootStrap, JposConst, POSPrinterConst {
  private ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private IBrowserManager theMgr;
  private MainFrame mainFrame;
  private BootStrapInfo info;
  private StartofSessionDialog_EUR dlg;
  private ObjectStore storeStore;
  private ObjectStore registerStore;
  private Date today = DateUtil.getBeginingOfDay();
  private CMSStore currStore; // The store in the object repository
  private Date currProcessDate; // The date for which this register is currently processing
  private CMSRegister currRegister; // The register that this machine was last time it booted.
  private CMSStore newStore; // The store whose ID the user entered
  private CMSRegister newRegister; // The reg whose ID the user entered
  private String newPassword; // The password of the newStore (if required)
  private String operatorId; // Whoever is running the start-of-session process
  private CMSEmployee oper; // Whoever is running the start-of-session process
  CMSRegister storeRegisters[];
  //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
	//ruchi
  private String lastSaleDigitalSignature;
  private String lastReturnDigitalSignature;
  private String lastSaleDigitalSignatureSaved = null;
  private String lastReturnDigitalSignatureSaved = null;
  //Ends here
  
  private int startTxnSeqNo = new Integer(new ConfigMgr("txnnumber.cfg").getString(
      "STARTING_TXN_SEQ_NUM")).intValue();
	ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
	String activateDigitalSignature = cfgMgr.getString("ACTIVATE_DIGITAL_SIGNATURE");

  /** A flag to determine if in training mode for printing **/
  private boolean trainingFlag = false;
  public static final String TXN_NUMBER = "TXN_NUMBER";

  /** Default constructor */
  public SOSBootStrap_EUR() {
  }

  /** Required by IBootStrap. (What is this supposed to return?) */
  public String getName() {
    return ("Start-of-session bootstrap");
  }

  /** Required by IBootStrap. (What is this supposed to return?) */
  public String getDesc() {
    return ("Start-of-session bootstrap");
  }

  /**
   * The entry point for a bootstrap process.
   * @param theMgr
   * @param parentFrame
   * @return BootStrapInfo
   */
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    if (parentFrame instanceof MainFrame) {
      mainFrame = (MainFrame)parentFrame;
    }
    this.theMgr = theMgr;
    bootMgr.setBootStrapStatus("Start of Session");
    info = new BootStrapInfo(getClass().getName());
    dlg = new StartofSessionDialog_EUR(mainFrame, "Start of Day", true, this, theMgr);
    init(); // Load the object repository and drive the dialog
    return (info);
  }

  /**
   * The user has decided to just bail out of this process.
   */
  public void cancelButtonPressed() {
    //abort("Operator has cancelled start-of-day.");
    // Reset all the fields.
    dlg.reset();
    dlg.setEnabledForReset();
    dlg.requestFocusUserID();
  }

  /**
   * Set the BootstrapInfo object with the abort condition and return control
   * to the start() method.
   **/
  private void abort(String msg) {
    info.setError(true);
    info.setMsg(msg);
    info.setShutDown(true); // Can't continue without completing this dialog.
    dlg.setVisible(false);
  }

  /**
   * Initialize the environment and display the dialog.
   */
  private void init() {
    loadCurrentValues();
    // If the processing date exists and is today then there is no reason to
    // go through this process.
    if (!info.isShutdown()) {
      if ((null == currProcessDate) || (!todayEqualsProcessDate()) || currStore == null
          || currRegister == null) {
        // if eod has not been run, then load global from yesterday file
        // and show dlg in done state,  disable edit boxes
		//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
		//ruchi
          lastSaleDigitalSignatureSaved = (String)BrowserManager.getInstance().getGlobalObject("SALE_DIGITAL_SIGNATURE");
          System.out.println("lastSaleDigitalSignatureSaved   :"+lastSaleDigitalSignatureSaved);
          if(lastSaleDigitalSignatureSaved != null){
          	lastSaleDigitalSignature = lastSaleDigitalSignatureSaved;
          	System.out.println("set the saved signature  : "+lastSaleDigitalSignature);
          }
          System.out.println("Ruchi printing saved signature for sale  "+lastSaleDigitalSignatureSaved);
          
          lastReturnDigitalSignatureSaved = (String)BrowserManager.getInstance().getGlobalObject("RETURN_DIGITAL_SIGNATURE");
          System.out.println("lastReturnDigitaurnlSignatureSaved   :"+lastSaleDigitalSignatureSaved);
          if(lastReturnDigitalSignatureSaved != null){
          	lastReturnDigitalSignature = lastReturnDigitalSignatureSaved;
          	System.out.println("set the saved signature  : "+lastReturnDigitalSignature);
          }
          System.out.println("Ruchi printing saved signature for return  "+lastReturnDigitalSignatureSaved);
          //Ends here
        if (!validateProcessDate()) {
          loadGlobalRepository();
          dlg.updateGUIForDone();
          dlg.enableFields(false);
        } else {
          // clear txn summary info
          cleanUpTxnSummary();
          // 5133 - clear yesterdays txn data
          // clear tmp files
          File tmpDir = new File(FileMgr.getLocalDirectory("tmp"));
          tmpDir.listFiles(new FileFilter() {

            /**
             *
             * @param file
             * @return
             */
            public boolean accept(File file) {
              if (file.getName().endsWith(".ser")) {
                file.delete();
              }
              return (false);
            }
          });
          // begin Eu interaction
          //dlg.requestFocusStoreID();
        }
        dlg.setVisible(true);
        dlg.requestFocusUserID();
      } else {
        loadGlobalRepository();
        // cehck locale
        Locale storeLocale = new Locale(currStore.getPreferredISOLanguage()
            , currStore.getPreferredISOCountry());
        if (!Locale.getDefault().equals(storeLocale)) {
          System.out.println("resetting locale to store locale: " + storeLocale);
          ((IApplicationManager)theMgr).setLocale(storeLocale);
          mainFrame.setLocale(storeLocale);
        }
        printSessionStartReceipt();
      }
      if (!info.isShutdown()) {
        setFrameIndicators();
      }
    }
  }

  /**
   * Prints the start of day receipt
   */
  private void printStartOfDayReceipt(CMSTransactionSOS txnSOS) {
    CMSRegisterSessionAppModel appModel = new CMSRegisterSessionAppModel();
    appModel.setOperator(oper);
    appModel.setRegister((CMSRegister)theMgr.getGlobalObject("REGISTER"));
    appModel.setSessionDate(new Date());
    appModel.setStore(currStore);
    appModel.setSOSTxn(txnSOS);
    if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
      String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "StartOfSession" + ".rdo";
      try {
        ObjectStore objectStore = new ObjectStore(fileName);
        objectStore.write(appModel);
      } catch (Exception e) {
        System.out.println("exception on writing object to blueprint folder: " + e);
      }
    }
    appModel.print(ReceiptBlueprintInventory.CMSStartOfDay, (IReceiptAppManager)theMgr);
  }

  /**
   * Prints the start session receipt
   */
  private void printSessionStartReceipt() {
    CMSRegisterSessionAppModel appModel = new CMSRegisterSessionAppModel();
    //appModel.setOperator(oper);  don't know who starts the session, logon is not required to start a session
    appModel.setRegister((CMSRegister)theMgr.getGlobalObject("REGISTER"));
    appModel.setSessionDate(new Date());
    appModel.setStore(currStore);
    if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
      String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "StartOfSession" + ".rdo";
      try {
        ObjectStore objectStore = new ObjectStore(fileName);
        objectStore.write(appModel);
      } catch (Exception e) {
        System.out.println("exception on writing object to blueprint folder: " + e);
      }
    }
    appModel.print(ReceiptBlueprintInventory.CMSSessionStart, (IReceiptAppManager)theMgr);
  }

  /**
   * Initialize the AppManager's object repository using values that were written
   * to persistent storage on the client.  If it is possible to update the store's
   * info from the database then do so, but in offline mode that won't happen.
   */
  private void loadCurrentValues() {
    // Get the current (persisted) store.
	//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
    System.out.println("loadCurrentValues ");
    currStore = (CMSStore)theMgr.getGlobalObject("STORE");
    System.out.println("currStore in load curret valaues   :"+currStore);
    //RUCHI
    if (activateDigitalSignature.trim().equalsIgnoreCase("true")){
    	lastSaleDigitalSignature = (String)theMgr.getGlobalObject("SALE_DIGITAL_SIGNATURE");
    	System.out.println("Inside loadCurrentValues  :"+lastSaleDigitalSignature);
    	lastReturnDigitalSignature = (String)theMgr.getGlobalObject("RETURN_DIGITAL_SIGNATURE");
    	System.out.println("lastReturnDigitalSignature   :"+lastReturnDigitalSignature);
    if(lastSaleDigitalSignature == null){
    	theMgr.addGlobalObject("SALE_DIGITAL_SIGNATURE", "0", true);
    }
    if(lastReturnDigitalSignature == null){
    	theMgr.addGlobalObject("RETURN_DIGITAL_SIGNATURE", "0", true);
    }
    }	
    //changes ends
    //Ends here
    if (currStore != null && !currStore.getCurrencyType().equals(ArmCurrency.getBaseCurrencyType())) {
      this.abort("The store's currency type does not match the base "
          + "currency type specified in the config file.  Please call support.");
    }
    //    // Get the current (persisted) register.
    currRegister = (CMSRegister)theMgr.getGlobalObject("REGISTER");
    // Get the current (persisted) processing date.
    currProcessDate = (Date)theMgr.getGlobalObject("PROCESS_DATE");
    if (currStore != null) {
      try {
        CMSStore updatedInfo = CMSStoreHelper.findById(theMgr, currStore.getId());
        if (updatedInfo != null) {
          currStore = updatedInfo;
        }
      } catch (Exception ex) {
        System.out.println("SOSBootStrap.loadCurrentValues() Error, unable to load new store: "
            + ex);
      }
    }
  }

  /**
   * clean up the txnsummary
   */
  private void cleanUpTxnSummary() {
    try {
      System.out.println("Cleaning up txn summary");
      TxnSummary.clear();
    } catch (Exception ex) {
      System.out.println("Exception cleanUpTxnSummary()->" + ex);
    }
  }

  /**
   * Method puts the current values of store, register and process date into the
   * global repository. CAN ONLY BE DONE ONCE PER SESSION!
   */
  private void loadGlobalRepository() {
    boolean isNewRegister = false;
    theMgr.addGlobalObject("STORE", currStore, true);
    theMgr.addGlobalObject("REGISTER", currRegister, true);
	//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
  //  ruchi
	
    theMgr.addGlobalObject("SALE_DIGITAL_SIGNATURE", lastSaleDigitalSignature, true);
    theMgr.addGlobalObject("RETURN_DIGITAL_SIGNATURE", lastReturnDigitalSignature, true);
    
       

    System.out.println("done!!!!!!");
	//Ends here
    // Beginning of testing code...(lar)
    SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    System.out.println("   --> Setting processing date to " + sdf.format(currProcessDate));
    // End of testing code...(lar)
    theMgr.addGlobalObject("PROCESS_DATE", currProcessDate, true);
    // If the currRegister is null then getnew TXN number
    if (currRegister == null) {
      isNewRegister = true;
    }
    /**
     * Added Code for storing the TXN_NUMBER
     */
    TransactionNumberStore transactionNumber = (TransactionNumberStore)BrowserManager.getInstance().
        getGlobalObject(TXN_NUMBER);
    try {
      if ((isNewRegister) || (transactionNumber == null)) {
        CMSTransactionSOS txnSOS = new CMSTransactionSOS(currStore);
        int txnInt = startTxnSeqNo;
        if (transactionNumber == null) {
          Integer seqInit = new Integer(0);
          transactionNumber = new TransactionNumberStore();
          transactionNumber.setSequenceNumber(seqInit);
        }
        txnInt = setTxnNumber(txnSOS);
        CMSTransactionNumberHelper.resetSequenceNumber(theMgr, currStore, currRegister, txnInt);
        System.out.println("*****In finish it Peek NEXT TRANS # CMSTransactionNumberHelper: "
            + CMSTransactionNumberHelper.peekSequenceNumber(theMgr, currStore, currRegister)
            + " currnet txn: " + txnInt);
        txnSOS.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theMgr, currStore
            , currRegister));
        transactionNumber.setSequenceNumber(new Integer(txnInt));
        theMgr.addGlobalObject(TXN_NUMBER, transactionNumber, true);
      }
    } catch (Exception ex) {
      System.out.println("Unable to create new TXN object");
      ex.printStackTrace();
    }
  }

  /**
   *
   *  Added New Method for setting the txn number
   * @return int
   * @throws Exception
   */
  private int setTxnNumber(CMSTransactionSOS txnSos)
      throws Exception {
    int newTxnNumber = 0;
    String txnIds[] = CMSTransactionPOSHelper.findTxnIdsByStoreIdAndRegisterId(theMgr
        , currStore.getId(), currRegister.getId());
    if (txnIds == null || txnIds.length == 0) {
      txnIds[0] = "0";
    } else if (txnIds != null) {
      if (txnIds[0] == null) {
        txnIds[0] = "0";
      }
    }
    String sTxnId = "";
    int dIds = 0;
    sTxnId = txnIds[0];
    if (sTxnId.indexOf("*") == -1)
      dIds = new Integer(sTxnId).intValue();
    else {
      dIds = new Integer(sTxnId.substring(sTxnId.indexOf("*") + 1)).intValue();
    }
    newTxnNumber = dIds + 150;
    return newTxnNumber;
  }

  /**
   * Set the current store and register number in the main frame.
   */
  private void setFrameIndicators() {
    if (mainFrame != null) {
      mainFrame.getGlobalBar().setStoreNum(currStore.getId());
      //mainFrame.getGlobalBar().setRegister(currRegister.getId());
      SimpleDateFormat sdf = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      mainFrame.getGlobalBar().setRegister(currRegister.getId() + "      "
          + res.getString("B. Date") + ":" + sdf.format(currProcessDate));
    }
  }

  /**
   * If currProcess date is <code>null</code> then the register completed end-of-day and
   * needs to run a full-blown start-of-day to prepare the register for business.
   * If currProcess date exists and is not today, the store did not perform an end-of-day
   * after a previous day's business.  End-of-day must be run before running the start-of-day
   * process.
   * If currProcess exists and is today then the register has already been initialized for
   * today and the user may proceed directly to the employee signon screen.
   * @return boolean
   */
  public boolean validateProcessDate() {
    if (null == currProcessDate) {
      return (true);
    }
    if (currStore != null && !todayEqualsProcessDate()) {
      dlg.setStatus("You must run EOD for this register.");
      theMgr.addGlobalObject("RUN_EOD", ""); // marker for app to run eod
      return (false);
    }
    return (true); // processing date is today
  }

  /**
   * Make sure that the store number that the user entered is a valid store.  This method
   * is also responsible for setting <code>newStore</code>.  Upon completion, <code>newStore</code>
   * should be the store the user is trying to open.
   * @return boolean <code>true</code> if the store number is valid or <code>false</code> otherwise.
   */
  public boolean validateStoreID() {
    boolean result = true;
    try {
      String tempStoreId = dlg.getStoreID();
      //Store object exist locally and EU is opening store as the same store
      if (currStore != null && currRegister == null) {
        storeRegisters = CMSRegisterHelper.selectByStoreID(theMgr, tempStoreId.trim());
      }
      if (currStore != null && currRegister != null) {
        storeRegisters = CMSRegisterHelper.selectByStoreID(theMgr, tempStoreId.trim());
      }
      if ((null != currStore) && (tempStoreId.equals(currStore.getId()))) {
        newStore = currStore;
      }
      //Store object does not exist locally and EU needs to validate new store
      else if (null == newStore || !tempStoreId.equals(newStore.getId())) {
        CMSStore tempStore = CMSStoreHelper.findById(theMgr, tempStoreId);
        storeRegisters = null;
        if (null == tempStore) {
          dlg.setStatus("That is not a valid store number.");
          newStore = null;
          result = false;
          return (result);
        } else {
          // This is to get all the registers for a corresponding store.
          storeRegisters = CMSRegisterHelper.selectByStoreID(theMgr, tempStoreId.trim());
        }
        // display dlg if store changes
        if (currStore != null) {
          IApplicationManager theAppMgr = (IApplicationManager)theMgr;
          java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.
              getResourceBundle();
          boolean ok = theAppMgr.showOptionDlg(res.getString("Store Id")
              , res.getString("You are changing the store number.") + "  " + res.getString("From")
              + " " + currStore.getId() + " " + res.getString("to") + " " + tempStoreId + " .  "
              + res.getString("Are you sure?"));
          dlg.repaint();
          if (!ok) {
            result = false;
            return (result);
          } else {
            newStore = tempStore;
          }
        } else {
          // passwordRequired = true;
          newStore = tempStore;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      dlg.setStatus("Cannot validate store.");
      result = false;
      return (result);
    }
    return (result);
  }

  /**
   * Method validats the register ID
   * @return boolean
   */
  public boolean validateRegisterID() {
    boolean result = false;
    String regID = dlg.getRegId();
    // Return true in off-line mode as can't validate the register anyways.
    if (!theMgr.isOnLine()) {
      newRegister = currRegister;
      return true;
    }
    if (storeRegisters != null) {
      for (int i = 0; i < storeRegisters.length; i++) {
        if (regID.equals(storeRegisters[i].getId())) {
          newRegister = storeRegisters[i];
          return true;
        }
      }
    }
    dlg.setStatus("Can't validate the register");
    return false;
  }

  /**
   * Validates the user
   * @return boolean
   */
  public boolean validateUserID() {
    String operatorId = null;
    try {
      String tempOperId = dlg.getUserID().trim();
      if (tempOperId.equals("") || tempOperId == null) {
        operatorId = null;
        dlg.setStatus("User ID is required.");
        return (false);
      }
      oper = CMSEmployeeHelper.findByExternalId(theMgr, tempOperId);
      if (oper == null) {
        dlg.setStatus("That is not a valid User ID.");
        return (false);
      }
      /**
       * Added for privilege
       */
      long accessRole = oper.doGetPrivileges();
      boolean isTechnicalSupport = oper.hasAnyRoleFromRoles(16) || oper.hasAnyRoleFromRoles(1);
      if (isTechnicalSupport == false) {
        dlg.setStatus("Access is denied");
        return (false);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      dlg.setStatus("  Cannot validate employee.");
      return false;
    }
    return (true);
    //return false;  // Operator is not management
  }

  /**
   * Validates the password of user
   * @return boolean
   */
  public boolean validatePassword() {
    boolean result = true;
    String password = null;
    password = dlg.getPassword();
    //CMSEmployee employee = new CMSEmployee(dlg.getUserID());
    if (oper == null)
      return false;
    if (password != null && oper != null) {
      if ((password.equals(oper.getPassword()))) {
        return true;
      }
      if (!(password.equals(oper.getPassword()))) {
        dlg.setStatus("Password is incorrect.");
        result = false;
      } else if (password.equals("")) {
        result = true;
      }
    } else {
      result = false;
    }
    return (result);
  }

  /**
   * Make sure that the operator performing this function has the authority to do so.
   * @return boolean <code>true</code> if the user is authorized to run start-of-day or
   *                 <code>false</code> otherwise.
   */
  public boolean validateOperatorID() {
    try {
      String tempOperId = dlg.getUserID();
      if (tempOperId.equals("") || tempOperId == null) {
        operatorId = null;
        dlg.setStatus("Operator is required.");
        return (false);
      }
      oper = CMSEmployeeHelper.findById(theMgr, tempOperId);
      if (null == oper) {
        dlg.setStatus("That is not a valid operator ID.");
        return (false);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      dlg.setStatus("Cannot validate employee.");
    }
    return (true);
    //return false;  // Operator is not management
  }

  /**
   * Make sure we've gotten a valid drawer fund from the operator.
   * @return <CODE>true</CODE> if the operator has entered a drawer fund;
   *  <CODE>false</CODE> otherwise.
   */
  private boolean validateDrawer() {
    ArmCurrency newDrawerFund = null;
    try {
      newDrawerFund = dlg.getDrawerFund();
      if (newDrawerFund == null) {
        return (false);
      }
      if (newDrawerFund.getDoubleValue() < 0.0d) {
        dlg.setStatus("Drawer fund can not be negative.");
        return (false);
      } else if (currStore != null && !newDrawerFund.equals(currRegister.getDrawerFund())) {
        //Store is not null and new Drawer Fund does not equal current");
        IApplicationManager theAppMgr = (IApplicationManager)theMgr;
        java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
        ConfigMgr config = new ConfigMgr("client_master.cfg");
        boolean isUseInitialDrawerFund = config.getString("USE_INITIAL_DRAWER_FUND").
            equalsIgnoreCase("YES");
        if (isUseInitialDrawerFund) {
          if (!theAppMgr.showOptionDlg(res.getString("Drawer Fund")
              , res.getString("The expected drawer fund was ")
              + currRegister.getDrawerFund().getFormattedStringValue() + ". "
              + res.getString("Are you sure you want to change it to ")
              + newDrawerFund.getFormattedStringValue() + "?")) {
            return (false);
          }
        }
      }
      if (!newDrawerFund.getCurrencyType().equals(currRegister.getDrawerFund().getCurrencyType())) {
        throw new CurrencyException(
            "The new register's currency type is different than the original.");
      }
      currRegister.setDrawerFund(newDrawerFund);
    } catch (BusinessRuleException businessRuleEx) {
      dlg.setStatus(businessRuleEx.getMessage());
      return (false);
    } catch (NumberFormatException numberFormatEx) {
      dlg.setStatus("Drawer fund must be a valid, non-negative number.");
      return (false);
    } catch (CurrencyException cex) { // only the equals() method will throw this
      System.err.println("Attempt to set drawer fund to an different currency amount: "
          + cex.getMessage());
      IApplicationManager theAppMgr = (IApplicationManager)theMgr;
      java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
      if (!theAppMgr.showOptionDlg(res.getString("Currency Type Change")
          , res.getString("The currency type for the store has changed.") + "  "
          + res.getString("Please contact support before continuing.")
          , IApplicationManager.OK_CANCEL_BUTTON)) {
        dlg.setStatus("Drawer fund must be a valid currency amount.", "\n" + cex.getMessage());
        return (false);
      }
      currRegister.doSetDrawerFund(newDrawerFund);
      return (validateDrawer());
    }
    dlg.setStatus("Drawer Fund", ": " + currRegister.getDrawerFund().formattedStringValue());
    return (true);
  }

  /**
   * Do all the processing required to set up the register for the current session.
   * By the time this is called, newStore, newRegister and currProcessDate are
   * all correct.
   * @return BootStrapInfo
   */
  public BootStrapInfo finishIt() {
    if (!newStore.getCurrencyType().equals(ArmCurrency.getBaseCurrencyType())) {
      this.abort("The store's currency type does not match the base "
          + "currency type specified in the config file.  Please call support.");
      return (info);
    }
    // clean up old journal files
    Runnable cleanUpJournals = new Runnable() {

      /**
       *
       */
      public void run() {
        System.out.println("purging old journals in the background...");
        ReceiptAuditFile.getInstance().purgeJournals(currProcessDate);
        System.out.println("purge of journals complete...");
      }
    };
    Thread journalThread = new Thread(cleanUpJournals);
    journalThread.setPriority(Thread.MIN_PRIORITY);
    journalThread.start();
    try {
      if (currStore == null || !currStore.getId().equals(newStore.getId())) {
        // If the store changes you must get the next register number for the new store here.
        System.out.println("Getting new register number.");
        System.out.println("Register #" + newRegister.getId());
        if (null == newRegister) {
          System.out.println("Cannot validate register number for store: " + newStore.getId());
          String msg = "Cannot validate register number for store.";
          info.setError(true);
          info.setMsg(msg);
          info.setShutDown(true);
          return (info);
        }
        // Since the user may not have control of the register until all the
        // start-of-day processing is complete, we should display a changing status
        // message so that the user knows that something is happening.  Do not overwrite
        // the currStore until you have a good register number!
        System.out.println("Setting store. " + newStore.getId());
        dlg.setStatus("Setting store.");
        currStore = newStore;
        System.out.println("Setting register. " + newRegister.getId());
        dlg.setStatus("Setting register.");
        currRegister = newRegister;
        try {
          System.out.println("Resetting transaction sequence number.");
          CMSTransactionNumberHelper.resetSequenceNumber(theMgr, currStore, currRegister);
        } catch (Exception e) {
          System.out.println("Cannot reset beginning transaction sequence number!");
          info.setError(true);
          info.setMsg("Failed to successfully reset transaction sequence number.");
          info.setShutDown(true);
          return (info);
        }
      }
      if (currRegister == null || !currRegister.getId().equals(newRegister.getId())) {
        currRegister = newRegister;
      }
      if (!validateDrawer()) {
        String msg = "Cannot validate drawer fund.";
        info.setError(true);
        info.setMsg(msg);
        info.setShutDown(false);
        return (info);
      }
      Locale storeLocale = new Locale(currStore.getPreferredISOLanguage()
          , currStore.getPreferredISOCountry());
      if (!Locale.getDefault().equals(storeLocale)) {
        System.out.println("resetting locale to store locale: " + storeLocale);
        ((IApplicationManager)theMgr).setLocale(storeLocale);
        mainFrame.setLocale(storeLocale);
      }
      if (currProcessDate == null || !todayEqualsProcessDate()) {
        // Persist the current processing date.
        System.out.println("Setting processing date.");
        dlg.setStatus("Setting process date.");
        //currProcessDate = today;
        if (dlg.getBusinessDate() != null)
          currProcessDate = dlg.getBusinessDate();
        else
          currProcessDate = today;
      }
      // Create and post a start-of-day transaction
      try { // vishal code : sos dig sign : @below Armani is calling one method which return's digital signature in String.
    	  String digitalSignatureSOS = "X1IxLUFUMV9kZW1va2Fzc2U0Ml8wXzIwMTYtMDYtMTNUMTQ6MjI6MjNfMCwwMF8wLDAwXzAsMDBfMCwwMF8wLDAwX05jZHFwOTI0SXhHYUpnaThVRFZtaUE9PV82ZWRjZWIyYl9wS3JrMUlEYmN4TT0.";
          if(digitalSignatureSOS!=null){
          	currStore.setDigitalSignatureSOS(digitalSignatureSOS);
          }  // end vishal
        CMSTransactionSOS txnSOS = new CMSTransactionSOS(currStore);
        txnSOS.setTheOperator(oper);
        txnSOS.setProcessDate(currProcessDate);
        txnSOS.setRegister(currRegister);
        /**
         * Added the code for setting txn-number
         */
        TransactionNumberStore transactionNumber = (TransactionNumberStore)BrowserManager.
            getInstance().getGlobalObject(TXN_NUMBER);
        if (transactionNumber == null
            || (transactionNumber != null
            && transactionNumber.getSequenceNumber().intValue() == startTxnSeqNo)) {
          int txnInt = startTxnSeqNo;
          int sysTxnInt = Integer.parseInt(CMSTransactionNumberHelper.peekSequenceNumber(theMgr
              , currStore, currRegister));
          // Find the last txn id used
          if (txnSOS != null) {
            txnInt = setTxnNumber(txnSOS);
          }
          sysTxnInt = txnInt;
          System.out.println("sysTxnInt" + sysTxnInt + " txnInt " + txnInt);
          if (sysTxnInt < startTxnSeqNo) {
            if (txnInt > (startTxnSeqNo - 1)) {
              sysTxnInt = txnInt;
            } else {
              sysTxnInt = startTxnSeqNo;
            }
            System.out.println("sysTxnInt2 " + sysTxnInt + " txnInt2 " + txnInt);
          }
          CMSTransactionNumberHelper.resetSequenceNumber(theMgr, currStore, currRegister, sysTxnInt);
          System.out.println("*****In finish it Peek NEXT TRANS # CMSTransactionNumberHelper: "
              + CMSTransactionNumberHelper.peekSequenceNumber(theMgr, currStore, currRegister)
              + " currnet txn: " + txnInt);
        }
        txnSOS.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theMgr, currStore
            , currRegister));
        System.out.println("*****NEXT TRANS # HAS BEEN SET TO " + txnSOS.getId());
		 //Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
        CMSStore store = (CMSStore) txnSOS.getStore();
		store.checkForFranceStore();
		if(store.isFranceStore()){
		CMSRegister cmsRegister = (CMSRegister) txnSOS.getRegister();
		cmsRegister.setMonthEnd(false);
		cmsRegister.setYearEnd(false);
		cmsRegister.setCheckedEopAtSos(false);
		cmsRegister.setEopPersisted(false);
		cmsRegister.setNetAmountOfMonth(0.0d);
		cmsRegister.setNetAmountOfYear(0.0d);
		Date date = txnSOS.getProcessDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int currentDay=calendar.get(calendar.DAY_OF_MONTH);
		int currentMonth = calendar.get(calendar.MONTH)+1;
		if(currentDay==1){
			cmsRegister.setMonthEnd(true);
			if(currentMonth==1){
			cmsRegister.setYearEnd(true);
			}
		}else{
			Date lastBusinessDate = currRegister.getCurrentBusinessDate();
			if(lastBusinessDate!=null){
			calendar = Calendar.getInstance();
			calendar.setTime(lastBusinessDate);
			int lastMonth = calendar.get(calendar.MONTH)+1;
			if(lastMonth!=currentMonth){
				cmsRegister.setMonthEnd(true);
				if(currentMonth==1){
				cmsRegister.setYearEnd(true);
				}
			}
		}
		}
		currRegister.setCurrentBusinessDate(currProcessDate);
		if(cmsRegister.isMonthEnd()){
			try {
				IApplicationManager theAppMgr = (IApplicationManager)theMgr;
				txnSOS.setRegister(cmsRegister);
				HashMap netAmountMap = CMSRegisterHelper.submitAndReturnNetAmount(theAppMgr,txnSOS);
				if(netAmountMap!=null && netAmountMap.size()>=1){
					try{
						if(!(netAmountMap.get("MONTH") instanceof String )){
							cmsRegister.setCheckedEopAtSos(true);
					if(cmsRegister.isYearEnd()){
						Double netAmountOfMonth = (Double) netAmountMap.get("MONTH");
						cmsRegister.setNetAmountOfMonth(netAmountOfMonth);
						Double netAmountOfYear  = (Double) netAmountMap.get("YEAR");
						cmsRegister.setNetAmountOfYear(netAmountOfYear);
					}else{
						Double netAmountOfMonth = (Double) netAmountMap.get("MONTH");
						cmsRegister.setNetAmountOfMonth(netAmountOfMonth);
					}
					}else{
						cmsRegister.setCheckedEopAtSos(true);
						cmsRegister.setEopPersisted(true );	
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					cmsRegister.setCheckedEopAtSos(false);
					}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		txnSOS.setRegister(cmsRegister);
		}
		 // END Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
	
        CMSTxnPosterHelper.post(theMgr, txnSOS);
        dlg.setStatus("Posting Transaction", " " + txnSOS.getId());
        loadGlobalRepository();
        info.setError(false);
        info.setMsg("");
        System.out.println("start of day receipt printing");
        printStartOfDayReceipt(txnSOS);
      } catch (BusinessRuleException be) {
        IApplicationManager theAppMgr = (IApplicationManager)theMgr;
        theAppMgr.showErrorDlg(be.getMessage());
        info.setError(true);
        info.setMsg(be.getMessage());
      } catch (Exception e) {
        System.out.println("Error during submit: ");
        e.printStackTrace();
        info.setError(true);
        info.setMsg("Cannot get transaction number for start-of-day.");
        info.setShutDown(true);
      }
    } catch (Exception e) {
      System.out.println("Error in start-of-day: " + e.getMessage());
      e.printStackTrace();
      info.setError(true);
      info.setMsg(e.getMessage());
      info.setShutDown(true);
    }
    return (info);
  }

  /**
   * Return <code>true</code> if the current processing date is the same as today's
   * calendar date.
   **/
  private boolean todayEqualsProcessDate() {
    if (null == currProcessDate) {
      return (false);
    }
    String todayStr = null;
    String processDateStr = null;
    try {
      SimpleDateFormat fmt = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      todayStr = fmt.format(today);
      processDateStr = fmt.format(currProcessDate);
    } catch (Exception e) {
      System.out.println("Error in Date Format");
    }
    return (todayStr.equals(processDateStr));
  }
}
