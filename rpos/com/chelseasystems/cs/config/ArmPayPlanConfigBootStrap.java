package com.chelseasystems.cs.config;

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
public class ArmPayPlanConfigBootStrap
    implements IBootStrap {
  /**
   * BootstrapManager
   */
  private BootStrapManager bootMgr;

  /**
   * BrowserManager
   */
  private IBrowserManager theMgr;

  private final String TENDER_TYPE = "CREDIT_CARD_";
  private final String CARD_PLAN = "_CARD_PLAN";
  private final String CARD_PLANS = ".CARD_PLANS";
  private final String CODE = ".CODE";
  private final String LABEL = ".LABEL";

  /**
   * put your documentation comment here
   */
  public ArmPayPlanConfigBootStrap() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "Payment Plan Configuration BootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if the Payment Plan Configuration needs to be downloaded.";
  }

  /**
   * put your documentation comment here
   * @param theMgr
   * @param parentFrame
   * @param bootMgr
   * @return
   */
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame,
                             BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      // check to make sure that ArmCreditCardPlans.cfg exists and its not in the backup
      File armPaymentConfig = new File(FileMgr.getLocalFile("config",
          "ArmCreditCardPlans.cfg"));
      if (!armPaymentConfig.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("config",
            "ArmCreditCardPlans.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(armPaymentConfig);
        }
        else {
          // no files, so delete date from last configuration download
          File fileDate = new File(FileMgr.getLocalFile("repository"
              , "ARMANI_PAY_PLAN_CONFIG_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
      bootMgr.setBootStrapStatus(
          "Checking armani pay plan configurations download date");
      Date date = (Date) theMgr.getGlobalObject(
          "ARMANI_PAY_PLAN_CONFIG_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)
          // Needed for offline scenario.
          ) {
        downloadFile();
      }
    }
    catch (Exception ex) {
      System.out.println("Exception PaymentPlanConfigBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start",
                                          "Exception"
                                          , "See Exception",
                                          LoggingServices.MAJOR, ex);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   * Download ArmPaymentCommon.cfg on Client
   * @throws Exception
   */
  private void downloadFile() throws Exception {
    try {
      File backup = new File(FileMgr.getLocalFile("config",
                                                  "ArmCreditCardPlans.bkup"));
      backup.delete();
      File armPaymentCFGFile = new File(FileMgr.getLocalFile("config",
          "ArmCreditCardPlans.cfg"));
      armPaymentCFGFile.renameTo(backup);
      armPaymentCFGFile.delete();
      ConfigMgr armConfigFile = new ConfigMgr("ArmCreditCardPlans.cfg");
      ConfigMgr config = new ConfigMgr("armaniDownload.cfg");
      ArmaniDownloadServices armDownloadServices = (ArmaniDownloadServices)
          config.getObject(
          "CLIENT_IMPL");
      bootMgr.setBootStrapStatus(
          "Downloading Armani Payment Plan Configurations file");
      CMSStore store = (CMSStore) theMgr.getGlobalObject("STORE");
      ArmPayPlanConfigDetail armPayPlanConfig[] = armDownloadServices.
          getPayPlanConfigByCountryAndLanguage(
          store.getPreferredISOCountry(), store.getPreferredISOLanguage());

      String sNewTenderCode = "";
      String sPlanCode = "";
      String sPlanDescription = "";
      String sOldTenderCode = "";
      String sCardPlansKey = "";
      String sCardPlansValues = "";
      int iPlanCtr = 1;
      for (int iCtr = 0; iCtr < armPayPlanConfig.length; iCtr++) {
        sNewTenderCode = armPayPlanConfig[iCtr].getTenderCode();
        if (sOldTenderCode.length() < 1) {
          sCardPlansKey = TENDER_TYPE + sNewTenderCode + CARD_PLANS;
          sOldTenderCode = armPayPlanConfig[iCtr].getTenderCode();
        }
        if (sNewTenderCode.equals(sOldTenderCode)) {
          String sTmp = "_"+armPayPlanConfig[iCtr].getCardPlanCode();
          sCardPlansValues += TENDER_TYPE + sNewTenderCode + CARD_PLAN + sTmp +
              ",";
          sPlanCode = TENDER_TYPE + sNewTenderCode + CARD_PLAN + sTmp + CODE;
          sPlanDescription = TENDER_TYPE + sNewTenderCode + CARD_PLAN + sTmp +
              LABEL;
          armConfigFile.setString(sPlanCode, armPayPlanConfig[iCtr].getCardPlanCode());
          armConfigFile.setString(sPlanDescription,
                                  armPayPlanConfig[iCtr].getCardPlanDescription());

          iPlanCtr++;
        }
        else {
          if (sCardPlansValues.indexOf(",") != -1)
            sCardPlansValues = sCardPlansValues.substring(0,
                sCardPlansValues.lastIndexOf(','));
          armConfigFile.setString(sCardPlansKey, sCardPlansValues);
          sCardPlansKey = TENDER_TYPE + sNewTenderCode + CARD_PLANS;
          sCardPlansValues = "";
          sOldTenderCode = armPayPlanConfig[iCtr].getTenderCode();
          iCtr--;
          iPlanCtr = 0;
        }
      }
      if (sCardPlansValues.length() > 1 && iPlanCtr > 0) {
        if (sCardPlansValues.indexOf(",") != -1)
          sCardPlansValues = sCardPlansValues.substring(0,
              sCardPlansValues.lastIndexOf(','));
        armConfigFile.setString(sCardPlansKey, sCardPlansValues);
      }
      theMgr.addGlobalObject("ARMANI_PAY_PLAN_CONFIG_DOWNLOAD_DATE",
                             new java.util.Date(), true);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      try {
        File armaniPayment = new File(FileMgr.getLocalFile("config",
            "ArmCreditCardPlans.cfg"));
        armaniPayment.delete();
        File backup = new File(FileMgr.getLocalFile("config",
            "ArmCreditCardPlans.bkup"));
        backup.renameTo(armaniPayment);
      }
      catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    }
    finally {
      if (theMgr instanceof IApplicationManager) {
        ( (IApplicationManager) theMgr).closeStatusDlg();
      }
    }
  }

}
