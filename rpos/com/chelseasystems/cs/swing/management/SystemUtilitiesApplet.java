/*
 * @copyright 2001, Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.management;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeServices;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.item.CMSItemServices;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.MenuPanel;
import com.chelseasystems.cs.xml.EmployeeXML;
import com.chelseasystems.cs.xml.ItemXML;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.receipt.*;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.logging.CMSLoggingFileServices;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.util.*;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.register.Register;
import java.io.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import java.awt.Window;
import com.chelseasystems.cs.appmgr.bootstrap.UpdateFilesManager;
import com.chelseasystems.cr.appmgr.AppManager;
import java.text.SimpleDateFormat;

/**
 * put your documentation comment here
 */
public class SystemUtilitiesApplet extends CMSApplet {

  /**
   * put your documentation comment here
   */

  protected java.awt.Window activeWindow = null;
  
  private static ConfigMgr config;
  
  private String fipay_flag;

  public SystemUtilitiesApplet() {
  }

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
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    MenuPanel pnlMenu = new MenuPanel();
    pnlMenu.setAppMgr(theAppMgr);
    pnlMenu.setTitle(res.getString("System Utilites Menu"));
    this.getContentPane().add(pnlMenu, BorderLayout.CENTER);
  }

  /**
   * put your documentation comment here
   */
  public void start() {
	String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_flag = config.getString("FIPAY_Integration");
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theAppMgr.setTransitionColor(theAppMgr.getTheme().getMenuBackground());
    theAppMgr.showMenu(MenuConst.SYSTEM_UTILS, theOpr, theAppMgr.PREV_BUTTON);
    theAppMgr.setSingleEditArea(res.getString("Select option."));
    boolean clearMessage =  true;
	//for clearing the screen on login screen
    
    //Default value of the flag is Y if its not present in credit_auth.cfg
	if (fipay_flag == null) {
		fipay_flag = "Y";
	}
	if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
		sendIdleMessageData(null,null,false,true,clearMessage,"");
	}
  }

  /**
   * put your documentation comment here
   */
  public void stop() {}

  /**
   * put your documentation comment here
   */
  public void destroy() {}

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.6.2.2.4.7 $");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return (res.getString("System Utilities"));
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
 //Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16  
 public void doLaunchBackOffice() {
	    //CMSItem cmsItem = pnlItemLookupList.getSelectedItem();
	//    if (cmsItem == null)
	  //    return;
	    String cmd = new ConfigMgr("item.cfg").getString("BACKOFFICE_SCRIPT");
	    //, cmsItem.getId() , cmsItem.getStoreId()	    };
	    try {
	      Runtime.getRuntime().exec(cmd);
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }
	  }
 //Ends	  
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("CALIBRATE_SCREEN")) {
      try {
        ConfigMgr mgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
        String cmd = mgr.getString("ELO_EXE");
        System.out.println("\t\telo_exe: --> " + cmd);
        if (cmd != null & cmd.length() != 0) {
          Runtime.getRuntime().exec(cmd);
        }
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
    } else if (sAction.equals("MAGSTRIPE_TEST")) {
      theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
      theAppMgr.buildObject("MAGSTRIPE Test", "com.chelseasystems.cs.swing.builder.MSRTestBldr", "");
	//Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16  
    } else if (sAction.equals("SCRIPTS")) {
        doLaunchBackOffice();//Ends here
    } else if (sAction.equals("PRINTER_TEST")) {
      ReceiptFactory receiptFactory = new ReceiptFactory(null, "PrinterTest");
      receiptFactory.print(theAppMgr);
    } else if (sAction.equals("RESEND_BROKEN_TXNS")) {
      if (theAppMgr.showOptionDlg(res.getString("Retry Broken Transactions")
          , res.getString("You should only do this at the request of the helpdesk.  Are you sure?"))) {
        try {
          CMSTxnPosterHelper.repostBrokenTransactions(theAppMgr);
          SystemUtilitiesAppModel appModel = new SystemUtilitiesAppModel(new ResourceBundleKey(
              "RESEND BROKEN TRANS"), (CMSEmployee)theOpr);
          appModel.print(theAppMgr, ReceiptBlueprintInventory.CMSResendBrokenTxn);
        } catch (Exception ex) {
          theAppMgr.showExceptionDlg(ex);
        }
      }
    } else if (sAction.equals("UPDATE_EMPLOYEE_FILE")) {
      if (theAppMgr.showOptionDlg(res.getString("Update Employee File"), res.getString("The employee file will take a few minutes to update.  Once the update has finished, the new employee information will be available.  It is not necessary to perform an 'End of Session' for the update to take effect.  Do you want to continue?"))) {
        try {
          setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          File backup = new File(FileMgr.getLocalFile("xml", "employees.bkup"));
          backup.delete();
          File employeeFile = new File(FileMgr.getLocalFile("xml", "employees.xml"));
          employeeFile.renameTo(backup);
          employeeFile.delete();
          ConfigMgr config = new ConfigMgr("employee.cfg");
          Employee[] employees = ((CMSEmployeeServices)config.getObject("CLIENT_DOWNLOAD_IMPL")).
              findByStore((Store)theAppMgr.getGlobalObject("STORE"));
          String employeeXml = new EmployeeXML().toXML(employees);
          FileWriter employeeFileWriter = new FileWriter(employeeFile);
          employeeFileWriter.write(employeeXml);
          employeeFileWriter.close();
          theAppMgr.addGlobalObject("EMPLOYEE_DOWNLOAD_DATE", new java.util.Date(), true);
        } catch (Exception ex) {
          try {
            File employeeFile = new File(FileMgr.getLocalFile("xml", "employees.xml"));
            employeeFile.delete();
            File backup = new File(FileMgr.getLocalFile("xml", "employees.bkup"));
            backup.renameTo(employeeFile);
          } catch (Exception ex1) {}
          System.out.println("Exception UPDATE_EMPLOYEE_FILE --> " + ex);
          ex.printStackTrace();
        }
        SystemUtilitiesAppModel appModel = new SystemUtilitiesAppModel(new ResourceBundleKey(
            "UPDATE EMPLOYEE FILE"), (CMSEmployee)theOpr);
        appModel.print(theAppMgr, ReceiptBlueprintInventory.CMSUpdateEmployeeFile);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        theAppMgr.showErrorDlg(res.getString("The update is complete."));
      }
    } else if (sAction.equals("UPDATE_ITEM_FILE")) {
      if (theAppMgr.showOptionDlg(res.getString("Update Item File")
          , res.getString("The item file will take a few minutes to update. Do you want to continue?"))) {
        try {
          setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          ResourceBundle res = ResourceManager.getResourceBundle();
          String datFile = "";
          Process process = null;
          String dataDir = "";
          String storeId = "";
          String zipFileName = "";
          String zipFile = "";
          File newFile;
          String incFileName = "";
          String logDir = "";
          ItemDetailsMap map = new ItemDetailsMap();
          LoggingFileServices loggingFileServ;
          CMSLoggingFileServices loggingFileServices;
          String logFileName;
          String regStoreId = null;
          String registerId = null;
          ConfigMgr configMgr = new ConfigMgr("item.cfg");
          try {
            logDir = "../files/prod/" + (configMgr.getString("ITEM_LOG_DIR"));
            loggingFileServ = (LoggingFileServices)LoggingServices.getCurrent();
            loggingFileServices = (CMSLoggingFileServices)loggingFileServ;
            logFileName = loggingFileServices.getLogFile();
            loggingFileServices.setLogFile(logDir);
            registerId = ((Register)theAppMgr.getGlobalObject("REGISTER")).getId();
            storeId = ((Store)theAppMgr.getGlobalObject("STORE")).getId();
            regStoreId = registerId + storeId;
            dataDir = FileMgr.getLocalDirectory(configMgr.getString("ITEM_DATA_CLIENT_DIR"));
            zipFileName = regStoreId + "_items.zip";
            incFileName = regStoreId + "_items.inc";
            zipFile = dataDir + File.separator + incFileName;
            String newZipFile = dataDir + "new_" + regStoreId + "_items.zip";
            datFile = dataDir + regStoreId + "_items.dat";
            newFile = new File(dataDir + zipFileName);
            // Check if downloaded today
            Date d = (Date)theAppMgr.getGlobalObject("ITEM_DOWNLOAD_DATE");
            if (true) {
              boolean success = downloadFile(d, registerId, storeId, newFile);
              if (success) {
                try {
                  process = Runtime.getRuntime().exec("cmd /c copy " + newZipFile + " "
                      + zipFileName);
                  process.waitFor();
                  Runtime.getRuntime().exec("cmd /c del " + newZipFile);
                  ZipUtil.unzip(newFile.toString(), dataDir);
                  File test = new File(dataDir + File.separator + regStoreId + "_items");
                  if (test.exists()) {
                    test.renameTo(new File(dataDir + File.separator + regStoreId + "_items.inc"));
                  }
                  mergeFiles(regStoreId, map);
                  if (newFile.exists())
                    newFile.delete();
                  Runtime.getRuntime().exec("cmd /c del " + newFile);
                } catch (Exception e) {
                  Runtime.getRuntime().exec("cmd /c del " + newZipFile);
                }
              } else {
                loggingFileServices.logMsg(res.getString("No item data file to be downloaded"));
                loggingFileServices.recordMsg();
              }
            }
            // load barcodemap
            map.loadMapByBarCode(dataDir + regStoreId);
          } catch (Exception ex) {
            LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
                , "See Exception", LoggingServices.MAJOR, ex);
            ex.printStackTrace();
          } finally {
            File f = new File(dataDir + File.separator + regStoreId + "_items.dat");
            if (!f.exists()) {
              f = null;
              theAppMgr.showErrorDlg(res.getString("Items file does not exist. Contact technical support. Shutting down POS client..."));
              LoggingServices.getCurrent().logMsg(res.getString("Items file does not exist. Contact technical support. Shutting down POS client..."));
              System.out.println(res.getString("Items file does not exist. Contact technical support. Shutting down POS client..."));
              System.exit(1);
            } else {
              if (f.length() == 0) {
                theAppMgr.showErrorDlg(res.getString(
                    "Problem with Item file, contact technical support"));
              }
              else
                theAppMgr.showErrorDlg(res.getString("The update is complete."));
            }
          }
         } catch (Exception ex) {
        	 ex.printStackTrace();
          try {
            File itemFile = new File(FileMgr.getLocalFile("xml", "items.xml"));
            itemFile.delete();
            File backup = new File(FileMgr.getLocalFile("xml", "items.bkup"));
            backup.renameTo(itemFile);
          } catch (Exception ex1) {}
          System.out.println("Exception UPDATE_ITEM_FILE --> " + ex);
          ex.printStackTrace();
          if (ex instanceof DowntimeException)
            theAppMgr.showErrorDlg(
                "The item file is not available from the server, try again later");
          else
            theAppMgr.showExceptionDlg(ex);
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      }
    } else if (sAction.equals("UPDATE_LOCAL_FILES")) {
      if (theAppMgr.showOptionDlg(res.getString("Update Local Files")
          , res.getString("The files will take a few minutes to update. Do you want to continue?"))) {
        try {
          setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          // Call the Methods for all the local flat files except Print Fiscal Documents
          // Delete the Repository Objects
          deleteRepository();
          UpdateFilesManager bootMgr = new UpdateFilesManager("client_master.cfg", (IBrowserManager)theAppMgr,theAppMgr.getParentFrame());
          while (bootMgr.hasMoreBootStraps()) {
            BootStrapInfo infoBoot = bootMgr.nextBootStrap();
            if (infoBoot.isError()) {
                LoggingServices.getCurrent().logMsg("Fatal Error loading bootstraps...");
              }
            }
        } catch (Exception ex) {
        ex.printStackTrace();
      }
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      theAppMgr.showErrorDlg(res.getString("The update is complete."));
      }
    }
  }

  /**
   * put your documentation comment here
   * @param Header
   * @param anEvent
   */
  public void appButtonEvent(String Header, CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("CANCEL")) {
      theAppMgr.showMenu(MenuConst.SYSTEM_UTILS, theOpr, theAppMgr.PREV_BUTTON);
      theAppMgr.setSingleEditArea(res.getString("Select option."));
    }
  }

  /**
   * put your documentation comment here
   * @param Command
   * @param value
   */
  public void editAreaEvent(String Command, String value) {
    /* if (Command.equals("MAGSTRIPE")) {
     CreditCardBldr bldr = new CreditCardBldr();
     CMSMSR.getInstance().registerCreditCardBuilder(bldr);
     if (bldr.getCreditCardInfo(value)) {
     value = bldr.getAccountNum();
     if (value.length() > 4) {
     StringBuffer buffer = new StringBuffer();
     for (int idx = 0; idx < (value.length() - 4); idx++) {
     buffer.append("x");
     }
     value = buffer.toString() + value.substring(value.length() - 4, value.length());
     }
     theAppMgr.showErrorDlg(res.getString("The system has detected magstripe input.  The card number read was ") + value);
     }
     else {
     theAppMgr.showErrorDlg(res.getString("The system failed to validate that card number.  Try the test again or call the Help Desk."));
     }
     */
    theAppMgr.showMenu(MenuConst.SYSTEM_UTILS, theOpr, theAppMgr.PREV_BUTTON);
    theAppMgr.setSingleEditArea(res.getString("Select option."));
  }

  /**
   * put your documentation comment here
   * @param d
   * @param registerId
   * @param storeId
   * @param newFile
   * @return
   */
  private boolean downloadFile(Date d, String registerId, String storeId, File newFile) {
    ConfigMgr config = new ConfigMgr("item.cfg");
    CMSItemServices itemDownloadServices = (CMSItemServices)config.getObject("CLIENT_IMPL");
    try {
      byte[] itemZipBytes = itemDownloadServices.getItemFile(registerId + storeId + "_items"
          , storeId, d);
      if (itemZipBytes != null) {
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(itemZipBytes);
        fos.flush();
        fos.close();
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      theAppMgr.showErrorDlg(ex.getMessage());
      return false;
    }
  }

  /**
   * put your documentation comment here
   * @param regStoreId
   * @param map
   */
  private void mergeFiles(String regStoreId, ItemDetailsMap map) {
    try {
      System.out.println("MERGING");
      ConfigMgr configMgr = new ConfigMgr("item.cfg");
      String dataDir = FileMgr.getLocalDirectory(configMgr.getString("ITEM_DATA_CLIENT_DIR"));
      Map incHash = map.loadMapById(dataDir + regStoreId, "_items.inc", false);
      Map dataHash = map.loadMapById(dataDir + regStoreId, "_items.dat", false);
      byte[] byteArray = new byte[1024];
      if (dataHash.size() == 0) {
        // No dat file exists ..
        // Rename inc to dat and proceed
        File incFile = new File(dataDir + regStoreId + "_items.inc");
        File datFile = new File(dataDir + regStoreId + "_items.dat");
        if (datFile.exists()) {
          System.out.println("Delete of DAT file : " + datFile.delete());
        }
        if (incFile.exists()) { // Inc file exists
          File newDatFile = new File(dataDir + regStoreId + "_items.dat");
          BufferedWriter bufWrite = new BufferedWriter(new FileWriter(newDatFile));
          BufferedReader bufRead = new BufferedReader(new FileReader(incFile));
          char[] arr = new char[512];
          int len = 0;
          while ((len = bufRead.read(arr)) > 0) {
            bufWrite.write(arr, 0, len);
          }
          bufRead.close();
          bufWrite.close();
          theAppMgr.addGlobalObject("ITEM_DOWNLOAD_DATE", new java.util.Date(), true);
          return;
        }
      }
      boolean hasCRLF = false;
      BufferedWriter rf = null;
      if (true) {
        File tmpFile = new File(dataDir + File.separator + regStoreId + "_items.tmp");
        rf = new BufferedWriter(new FileWriter(tmpFile));
      }
      RandomAccessFile datrf = null;
      if (true) {
        File tmpFile = new File(dataDir + File.separator + regStoreId + "_items.dat");
        datrf = new RandomAccessFile(tmpFile, "rw");
      }
      RandomAccessFile incrf = null;
      if (true) {
        File tmpFile = new File(dataDir + File.separator + regStoreId + "_items.inc");
        incrf = new RandomAccessFile(tmpFile, "rw");
        hasCRLF = ItemDetailsMap.hasCRLF(tmpFile);
      }
      if (dataHash.size() > 0) {
        //Read data from .dat
        for (Iterator dit = dataHash.keySet().iterator(); dit.hasNext(); ) {
          String ID = (String)dit.next();
          if (!incHash.containsKey(ID)) {
            String itemS = getItemString(datrf, dataHash, ID, byteArray);
            addItem(rf, itemS, hasCRLF);
          }
        }
      }
      //Read data from .inc
      for (Iterator hit = incHash.keySet().iterator(); hit.hasNext(); ) {
        String ID = (String)hit.next();
        String itemS = getItemString(incrf, incHash, ID, byteArray);
        addItem(rf, itemS, hasCRLF);
      }
      rf.close();
      datrf.close();
      incrf.close();
      datrf = null;
      incrf = null;
      if (true) {
        String datFile = dataDir + regStoreId + "_items.dat";
        File FF = new File(datFile);
        if (FF.exists()) {
          System.out.println("Deleting old DAT FILE .. new will be made : " + FF.delete());
        }
      }
      if (true) {
        File tmpFile = new File(dataDir + regStoreId + "_items.tmp");
        File datFile = new File(dataDir + regStoreId + "_items.dat");
        if (datFile.exists()) {
          System.out.println("Name of DAT file : " + datFile.getAbsolutePath() + ":"
              + datFile.getName());
          System.out.println("Delete of DAT file : " + datFile.delete());
        }
        if (tmpFile.exists()) { // Inc file exists
          File newDatFile = new File(dataDir + regStoreId + "_items.dat");
          BufferedWriter bufWrite = new BufferedWriter(new FileWriter(newDatFile));
          BufferedReader bufRead = new BufferedReader(new FileReader(tmpFile));
          char[] arr = new char[512];
          int len = 0;
          while ((len = bufRead.read(arr)) > 0) {
            bufWrite.write(arr, 0, len);
          }
          bufRead.close();
          bufWrite.close();
        }
        // Delete the tmp file
        String sFile = dataDir + regStoreId + "_items.inc";
        File incFile = new File(sFile);
        if (incFile.exists()) {
          System.out.println("&&&&&&&&&&&&&Deleting the INC file : " + incFile.delete());
        }
      }
      if (true) {}
      theAppMgr.addGlobalObject("ITEM_DOWNLOAD_DATE", new java.util.Date(), true);
    } catch (Exception fg) {
      fg.printStackTrace();
      theAppMgr.showErrorDlg(fg.getMessage());
    }
  }

  /**
   * put your documentation comment here
   * @param rf
   * @param m
   * @param ID
   * @param byteArray
   * @return
   */
  String getItemString(RandomAccessFile rf, Map m, String ID, byte[] byteArray) {
    String temp = "";
    if (m.containsKey(ID)) {
//      try {
//        Long pos = (Long)m.get(ID);
//        //System.out.println("Seeking at : " + pos);
//        rf.seek(pos.longValue());
//        String s = rf.readLine();
//        temp = s;
//      } catch (Exception lf) {
//        lf.printStackTrace();
//      }
      try {
        Long pos = (Long)m.get(ID);
        System.out.println("Seeking at : " + pos);
        rf.seek(pos.longValue());
        byte[] b = new byte[1024];
        int bRead = rf.read(b);
        temp = getLineString(b, bRead);
      } catch (Exception lf) {
        lf.printStackTrace();
      }

    }
    return temp;
  }

  /*
   * Utility method to extract string upto newline from byte array
   */
  private String getLineString(byte[] b, int len) {
      String tmp = new String(b, 0, len);
      int lineLen = tmp.indexOf('\n');
      if (lineLen == -1) { // Most likely end of file reached.
          return tmp;
      } else if (lineLen == 0) { // First char is \n
          return "";
      } else if (tmp.charAt(lineLen-1) == '\r') { // Prev char is \r
          lineLen = lineLen - 1;
          if (lineLen == 0) { // First two chars \r\n
              return "";
          }
      }
      return tmp.substring(0, lineLen);
  }

  /**
   * put your documentation comment here
   * @param rf
   * @param s
   * @param hasCRLF
   */
  public void addItem(BufferedWriter rf, String s, boolean hasCRLF) {
    try {
      rf.write(s, 0, s.length());
      if (hasCRLF)
        rf.write("\r\n");
      else
        rf.write("\n");
    } catch (Exception df) {
      df.printStackTrace();
    }
  }

  private void deleteRepository(){

    Vector vRepository = new Vector();
    vRepository.add("ALTERATIONS_DOWNLOAD_DATE");
    vRepository.add("ARMANI_CONFIG_DOWNLOAD_DATE");
    vRepository.add("ARMANI_PAY_CONFIG_DOWNLOAD_DATE");
    vRepository.add("CURRENCYRT_DOWNLOAD_DATE");
    vRepository.add("CUSTMSG_DOWNLOAD_DATE");
    vRepository.add("EMPLOYEE_DOWNLOAD_DATE");
    vRepository.add("ITEM_DOWNLOAD_DATE");
    vRepository.add("LOYALTY_DOWNLOAD_DATE");
    vRepository.add("PROMOTION_DOWNLOAD_DATE");
    vRepository.add("THRESHOLD_PROMOTION_DOWNLOAD_DATE");
    //PCR: Rule to alert the employee about the sales limit
    vRepository.add("CUSTOMER_ALERT_RULE_DOWNLOAD_DATE");
    vRepository.add("ARMANI_DISCOUNT_RULE_DOWNLOAD_DATE");
    for (int i = 0; i < vRepository.size(); i++) {
      File repositoryFile = new File(FileMgr.getLocalFile("repository", (String)vRepository.get(i)));
      if(repositoryFile.exists()){
        repositoryFile.delete();
        ConfigMgr configMgr = new ConfigMgr("item.cfg");
        String sDate = configMgr.getString("INITIAL_FROM_DATE");
        Date date=null;
        if (sDate != null) {
          date=new Date(sDate);
        }else{
          date = new Date();
          date.setYear(date.getYear() - 1);
        }
        theAppMgr.addGlobalObject((String)vRepository.get(i),date,true);
      }
    }
  }
  
  private void deleteRepository(String key){
	  File repositoryFile = new File(FileMgr.getLocalFile("repository", key));
      if(repositoryFile.exists()){
        repositoryFile.delete();
        ConfigMgr configMgr = new ConfigMgr("item.cfg");
        String sDate = configMgr.getString("INITIAL_FROM_DATE");
        Date date=null;
        if (sDate != null) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
 //         date=new Date(sDate);
        	try{
        		date = sdf.parse(sDate);
        	}catch(Exception e){
        		date = new Date(sDate);
        	}
        }else{
          date = new Date();
          date.setYear(date.getYear() - 1);
        }
        theAppMgr.addGlobalObject(key,date,true);
      }
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
				if(!storeCountry.equals("CAN")){
					//String tt = orgTxn.getTransactionType();
					responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line,lineItemArray,Refresh,idleMessage,clearMessage,discountAmt,false);
					if(responseArray!=null){
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
*/						return false;
					}
					}
					}
				}else
				
				if (responseArray == null) {
					return true;
				}
				//count=0;	
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}
	
}

}

