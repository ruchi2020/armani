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
 | 1    | 11-03-2006 | Sandhya   | N/A       | Customer Alert Rules                         |                            
 --------------------------------------------------------------------------------------------
 */
import java.awt.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.util.DateUtil;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.xml.*;
import com.chelseasystems.cr.logging.*;


/**
 * put your documentation comment here
 */
public class CustomerAlertRuleBootStrap implements IBootStrap {
  private IBrowserManager theMgr;

  /**
   *
   */
  public CustomerAlertRuleBootStrap() {
  }

  /**
   *
   * @return String
   */
  public String getName() {
    return "CustomerAlertRuleBootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if customer alert rule needs to be downloaded";
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
      this.theMgr = theMgr;
      File filecustAlertRule = new File(FileMgr.getLocalFile("xml", "customer_alert_rule.xml"));
      if (!filecustAlertRule.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("xml", "customer_alert_rule.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(filecustAlertRule);
        } else {
          File fileDate = new File(FileMgr.getLocalFile("repository", "CUSTOMER_ALERT_RULE_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
      Date date = (Date)theMgr.getGlobalObject("CUSTOMER_ALERT_RULE_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)) {
        downloadFile();
      }
    } catch (Exception ex) {
      System.out.println("Exception CustomerAlertRuleBootStrap.start()->" + ex);
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
      File backup = new File(FileMgr.getLocalFile("xml", "customer_alert_rule.bkup"));
      backup.delete();
      File customerAlertRuleFile = new File(FileMgr.getLocalFile("xml", "customer_alert_rule.xml"));
      customerAlertRuleFile.renameTo(backup);
      customerAlertRuleFile.delete();
      Store store = (Store)theMgr.getGlobalObject("STORE");
      String countryCode = store.getPreferredISOCountry();
      ConfigMgr config = new ConfigMgr("customer.cfg");
      CMSCustomerServices custAlertRuleDownloadServices = (CMSCustomerServices)config.getObject(
          "CLIENT_IMPL");
      CMSCustomerAlertRule[] custAlertRules = custAlertRuleDownloadServices.getAllCustomerAlertRules(countryCode);
      new CustomerAlertRuleXML().writToFile(customerAlertRuleFile.getAbsolutePath(), custAlertRules);
      theMgr.addGlobalObject("CUSTOMER_ALERT_RULE_DOWNLOAD_DATE", new java.util.Date(), true);
      //need to implement this
      CMSCustomerFileServices custAlertRuleFileServices = (CMSCustomerFileServices)config.getObject(
          "CLIENT_LOCAL_IMPL");
      System.out.println(
          "LOADING ALL CUSTOMER ALERT RULES FROM CUSTOMER ALERT RULE BOOTSTRAP INTO FILE SERVICES...");
      custAlertRuleFileServices.getAllCustomerAlertRules(countryCode);
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File customerAlertRuleFile = new File(FileMgr.getLocalFile("xml", "customer_alert_rule.xml"));
        customerAlertRuleFile.delete();
        File backup = new File(FileMgr.getLocalFile("xml", "customer_alert_rule.bkup"));
        backup.renameTo(customerAlertRuleFile);
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

