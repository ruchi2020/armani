/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

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
 | 1    | 05-03-2005 | Khyati    | N/A       | Loyalty Management                           |
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
public class LoyaltyBootStrap implements IBootStrap {
  private BootStrapManager bootMgr;
  private IBrowserManager theMgr;

  /**
   *
   */
  public LoyaltyBootStrap() {
  }

  /**
   *
   * @return String
   */
  public String getName() {
    return "LoyaltyBootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if the loyalty rules file needs to be downloaded.";
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
      // check to make sure that loyalty_rules.xml exists and its not in the backup
      File fileloyaltyRules = new File(FileMgr.getLocalFile("xml", "loyalty_rules.xml"));
      if (!fileloyaltyRules.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("xml", "loyalty_rules.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(fileloyaltyRules);
        } else { // no files, so delete date from last loyalty rules download
          File fileDate = new File(FileMgr.getLocalFile("repository", "LOYALTY_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine())
        return new BootStrapInfo(this.getClass().getName());
      bootMgr.setBootStrapStatus("Checking loyalty rules download date");
      Date date = (Date)theMgr.getGlobalObject("LOYALTY_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12))
        downloadFile();
    } catch (Exception ex) {
      System.out.println("Exception LoyaltyBootStrap.start()->" + ex);
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
      File backup = new File(FileMgr.getLocalFile("xml", "loyalty_rules.bkup"));
      backup.delete();
      File loyaltyRuleFile = new File(FileMgr.getLocalFile("xml", "loyalty_rules.xml"));
      loyaltyRuleFile.renameTo(backup);
      loyaltyRuleFile.delete();
      Store store = (Store)theMgr.getGlobalObject("STORE");
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      LoyaltyServices loyaltyDownloadServices = (LoyaltyServices)config.getObject("CLIENT_IMPL");
      bootMgr.setBootStrapStatus("Downloading loyatly file");
      System.out.println("***** " + store + " " + loyaltyDownloadServices);
      LoyaltyRule[] loyaltyRules = loyaltyDownloadServices.findRulesByStoreIdForDateRange(store.
          getId(), new java.util.Date(), null);
      new LoyaltyXML().writToFile(loyaltyRuleFile.getAbsolutePath(), loyaltyRules);
      theMgr.addGlobalObject("LOYALTY_DOWNLOAD_DATE", new java.util.Date(), true);
      //need to implement this
      LoyaltyFileServices loyaltyFileServices = (LoyaltyFileServices)config.getObject(
          "CLIENT_LOCAL_IMPL");
      System.out.println("LOADING ALL LOYALTY RULES FROM LOYALTY BOOTSTRAP INTO FILE SERVICES...");
      loyaltyFileServices.loadAllLoyaltyRulesFromFile();
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File loyaltyRuleFile = new File(FileMgr.getLocalFile("xml", "loyalty_rules.xml"));
        loyaltyRuleFile.delete();
        File backup = new File(FileMgr.getLocalFile("xml", "loyalty_rules.bkup"));
        backup.renameTo(loyaltyRuleFile);
      } catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    } finally {
      if (theMgr instanceof IApplicationManager)
        ((IApplicationManager)theMgr).closeStatusDlg();
    }
  }
}

