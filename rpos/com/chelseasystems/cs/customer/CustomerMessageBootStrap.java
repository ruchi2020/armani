/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 05-20-2005 | Megha    | N/A        | Customer Messages                            |
 --------------------------------------------------------------------------------------------
 */
import java.awt.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.xml.*;
import com.chelseasystems.cr.logging.*;


/**
 * put your documentation comment here
 */
public class CustomerMessageBootStrap implements IBootStrap {
  private BootStrapManager bootMgr;
  private IBrowserManager theMgr;

  /**
   *
   */
  public CustomerMessageBootStrap() {
  }

  /**
   *
   * @return String
   */
  public String getName() {
    return "CustomerMessageBootStrapBootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if customer message needs to be downloaded";
  }

  /**
   *
   * @param theMgr IBrowserManager
   * @param parentFrame Window
   * @param bootMgr BootStrapManager
   * @return BootStrapInfo
   */
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      // check to make sure that customerMessage exists and its not in the backup
      File filecustMsg = new File(FileMgr.getLocalFile("xml", "customer_messages.xml"));
      if (!filecustMsg.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("xml", "customer_messages.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(filecustMsg);
        } else { // no files, so delete date from last loyalty rules download
          File fileDate = new File(FileMgr.getLocalFile("repository", "CUSTMSG_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
      //bootMgr.setBootStrapStatus("Checking customer msg download date");
      Date date = (Date)theMgr.getGlobalObject("CUSTMSG_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)) {
        downloadFile();
      }
    } catch (Exception ex) {
      System.out.println("Exception CustomerMessageBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   *
   * @throws Exception
   */
  private void downloadFile()
      throws Exception {
    try {
      File backup = new File(FileMgr.getLocalFile("xml", "customer_messages.bkup"));
      backup.delete();
      File custMessageFile = new File(FileMgr.getLocalFile("xml", "customer_messages.xml"));
      custMessageFile.renameTo(backup);
      custMessageFile.delete();
      Store store = (Store)theMgr.getGlobalObject("STORE");
      ConfigMgr config = new ConfigMgr("customer.cfg");
      CMSCustomerServices custMessageDownloadServices = (CMSCustomerServices)config.getObject(
          "CLIENT_IMPL");
      //bootMgr.setBootStrapStatus("Downloading customer message file");
      CMSCustomerMessage[] custMessage = custMessageDownloadServices.getAllCustomerMessages();
      new CustomerMessageXML().writToFile(custMessageFile.getAbsolutePath(), custMessage);
      theMgr.addGlobalObject("CUSTMSG_DOWNLOAD_DATE", new java.util.Date(), true);
      //need to implement this
      CMSCustomerFileServices CustMsgFileServices = (CMSCustomerFileServices)config.getObject(
          "CLIENT_LOCAL_IMPL");
      System.out.println(
          "LOADING ALL CUSTOMER MESSAGES FROM CUSTOMER MESSAGE BOOTSTRAP INTO FILE SERVICES...");
      CustMsgFileServices.getAllCustomerMessages();
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File customerMessageFile = new File(FileMgr.getLocalFile("xml", "customer_messages.xml"));
        customerMessageFile.delete();
        File backup = new File(FileMgr.getLocalFile("xml", "customer_messages.bkup"));
        backup.renameTo(customerMessageFile);
      } catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    } finally {
      if (theMgr instanceof IApplicationManager) {
        ((IApplicationManager)theMgr).closeStatusDlg();
      }
    }
  }
}

