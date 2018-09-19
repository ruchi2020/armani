/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.config;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | N/A        | Sumit  | N/A       | Initial version                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Sumit Krishnan
 * @version 1.0
 */
import java.awt.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cs.download.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.store.CMSStore;


/**
 * put your documentation comment here
 */
public class PaymentConfigBootStrap implements IBootStrap {
  /**
   * BootstrapManager
   */
  private BootStrapManager bootMgr;
  /**
   * BrowserManager
   */
  private IBrowserManager theMgr;

  /**
   * put your documentation comment here
   */
  public PaymentConfigBootStrap() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "Payment Configuration BootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if the Payment Configuration needs to be downloaded.";
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param parentFrame
   * @param bootMgr
   * @return
   */
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      // check to make sure that Arm_payment_common.cfg exists and its not in the backup
      File armPaymentConfig = new File(FileMgr.getLocalFile("config", "ArmPaymentCommon.cfg"));
      if (!armPaymentConfig.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("config", "ArmPaymentCommon.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(armPaymentConfig);
        } else {
          // no files, so delete date from last configuration download
          File fileDate = new File(FileMgr.getLocalFile("repository"
              , "ARMANI_PAY_CONFIG_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
      bootMgr.setBootStrapStatus("Checking armani configurations download date");
      Date date = (Date)theMgr.getGlobalObject("ARMANI_PAY_CONFIG_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)
          // Needed for offline scenario.
          ) {
        downloadFile();
      }
    } catch (Exception ex) {
      System.out.println("Exception PaymentConfigBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   * Download ArmPaymentCommon.cfg on Client
   * @throws Exception
   */
  private void downloadFile()
      throws Exception {
    try {
      File backup = new File(FileMgr.getLocalFile("config", "ArmPaymentCommon.bkup"));
      backup.delete();
      File armPaymentCFGFile = new File(FileMgr.getLocalFile("config", "ArmPaymentCommon.cfg"));
      armPaymentCFGFile.renameTo(backup);
      armPaymentCFGFile.delete();
      ConfigMgr armConfigFile = new ConfigMgr("ArmPaymentCommon.cfg");
      ConfigMgr config = new ConfigMgr("armaniDownload.cfg");
      ArmaniDownloadServices armDownloadServices = (ArmaniDownloadServices)config.getObject(
          "CLIENT_IMPL");
      bootMgr.setBootStrapStatus("Downloading Armani Payment Configurations file");
      CMSStore store = (CMSStore)theMgr.getGlobalObject("STORE");
      ArmPayConfigDetail[] armPayConfig = armDownloadServices.getPayConfigByCountryAndLanguage(
          store.getPreferredISOCountry(), store.getPreferredISOLanguage());
      String oldTenderType = "";
      StringBuffer sTenderTypeHeader = null;
      ArrayList keyArray = null;
      ArrayList valueArray = null;
      for (int i = 0; i < armPayConfig.length; i++) {
        ArmPayConfigDetail payConfigDetail = (ArmPayConfigDetail)armPayConfig[i];
        String sTenderType = payConfigDetail.getTenderType();
        String sTenderCode = payConfigDetail.getCode();
        String sTenderDesc = payConfigDetail.getDescription();
        if (oldTenderType.equalsIgnoreCase("")) {
          sTenderTypeHeader = new StringBuffer();
          keyArray = new ArrayList();
          valueArray = new ArrayList();
          oldTenderType = sTenderType;
        }
        if (!oldTenderType.equalsIgnoreCase(sTenderType)) {
          writeFile(armConfigFile, oldTenderType, sTenderType, sTenderTypeHeader, keyArray
              , valueArray);
          keyArray = new ArrayList();
          valueArray = new ArrayList();
          sTenderTypeHeader = new StringBuffer();
          oldTenderType = sTenderType;
        }
        if (oldTenderType.equalsIgnoreCase(sTenderType)) {
          sTenderTypeHeader.append(sTenderType + "_" + sTenderCode + ",");
          keyArray.add(sTenderType + "_" + sTenderCode + ".CODE");
          valueArray.add(sTenderCode);
          keyArray.add(sTenderType + "_" + sTenderCode + ".DESC");
          valueArray.add(sTenderDesc);
        }
        if (i == (armPayConfig.length - 1)) {
          writeFile(armConfigFile, oldTenderType, sTenderType, sTenderTypeHeader, keyArray
              , valueArray);
          keyArray = new ArrayList();
          valueArray = new ArrayList();
          sTenderTypeHeader = new StringBuffer();
          oldTenderType = "";
        }
      }
      theMgr.addGlobalObject("ARMANI_PAY_CONFIG_DOWNLOAD_DATE", new java.util.Date(), true);
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File armaniPayment = new File(FileMgr.getLocalFile("config", "ArmPaymentCommon.cfg"));
        armaniPayment.delete();
        File backup = new File(FileMgr.getLocalFile("config", "ArmPaymentCommon.bkup"));
        backup.renameTo(armaniPayment);
      } catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    } finally {
      if (theMgr instanceof IApplicationManager) {
        ((IApplicationManager)theMgr).closeStatusDlg();
      }
    }
  }

  /**
   * put your documentation comment here
   * @param armConfigFile
   * @param oldTenderType
   * @param sTenderType
   * @param sTenderTypeHeader
   * @param keyArray
   * @param valueArray
   */
  private void writeFile(ConfigMgr armConfigFile, String oldTenderType, String sTenderType
      , StringBuffer sTenderTypeHeader, ArrayList keyArray, ArrayList valueArray) {
    try {
      String sHeader = sTenderTypeHeader.toString();
      sHeader = sHeader.substring(0, sHeader.length() - 1);
      armConfigFile.setString(oldTenderType, sHeader);
      for (int j = 0; j < keyArray.size(); j++) {
        String key = (String)keyArray.get(j);
        String value = (String)valueArray.get(j);
        armConfigFile.setString(key, value);
      }
    } catch (Exception e) {
      System.out.println("Exception writeFile()->" + e);
      e.printStackTrace();
    }
  }
}
