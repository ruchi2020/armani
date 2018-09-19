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
 +  3   |07-20-2007  |Ruchi      |PCR        |New Priority Column has been added            +
 +		|			 |			 |   		 | into ArmaniCommon.cfg                        +
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 09-08-2005 | Manpreet  | N/A       | Removed a condition for downloading file.    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | N/A        | Manpreet  | N/A       | Initial version                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
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
public class ArmConfigBootStrap implements IBootStrap {
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
  public ArmConfigBootStrap() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "Armani Configuration BootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if the configuraiton file needs to be downloaded.";
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
      // check to make sure that ArmaniCommon.cfg exists and its not in the backup
      File armCustomConfig = new File(FileMgr.getLocalFile("config", "ArmaniCommon.cfg"));
      if (!armCustomConfig.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("config", "ArmaniCommon.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(armCustomConfig);
        } else {
          // no files, so delete date from last configuration download
          File fileDate = new File(FileMgr.getLocalFile("repository", "ARMANI_CONFIG_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
      bootMgr.setBootStrapStatus("Checking armani configurations download date");
      Date date = (Date)theMgr.getGlobalObject("ARMANI_CONFIG_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)
          // || (!armCustomConfig.exists())  MSB - 09/08/05
          // Needed for offline scenario.
          ) {
        downloadFile();
      }
    } catch (Exception ex) {
      System.out.println("Exception ArmConfigBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   * Download ArmaniCommon.cfg on Client
   * @throws Exception
   */
  private void downloadFile()
      throws Exception {
    try {
      File backup = new File(FileMgr.getLocalFile("config", "ArmaniCommon.bkup"));
      backup.delete();
      File armCustomCFGFile = new File(FileMgr.getLocalFile("config", "ArmaniCommon.cfg"));
      armCustomCFGFile.renameTo(backup);
      armCustomCFGFile.delete();
      //MSB- 02/08/2006 - Defect Id :1347
      // ConfigMgr converts characters into unicode characters
      // so if file is read using normal FileReader (See-> ArmConfigLoader.java)
      // characters are no more encoded. To make sure characters
      // maintain their encoding, config file is written using
      // normal Buffered writer.
      // Issue # 1347
//      ConfigMgr armConfigFile = new ConfigMgr("ArmaniCommon.cfg");
      FileWriter fileWriter = new FileWriter("..//files//prod//config//ArmaniCommon.cfg");
      BufferedWriter buffWriter = new BufferedWriter(fileWriter);
      ConfigMgr config = new ConfigMgr("armaniDownload.cfg");
      ArmaniDownloadServices armDownloadServices = (ArmaniDownloadServices)config.getObject(
          "CLIENT_IMPL");
      bootMgr.setBootStrapStatus("Downloading Armani Configurations file");
      CMSStore store = (CMSStore)theMgr.getGlobalObject("STORE");
      ArmConfig armConfig = armDownloadServices.getConfigByCountryAndLanguage(store.
          getPreferredISOCountry(), store.getPreferredISOLanguage());
      if (armConfig != null) {
        Enumeration enumrator = armConfig.getConfigurationTypes();
        while (enumrator.hasMoreElements()) {
          String sTmp = (String)enumrator.nextElement();
          String sConfigType = "";
          java.util.List configList = armConfig.getConfigDetailsFor(sTmp);
          for (int iCtr = 0; iCtr < configList.size(); iCtr++) {
            ArmConfigDetail configDet = (ArmConfigDetail)configList.get(iCtr);
            String sConfigCode = sTmp + "_" + configDet.getCode();
            sConfigType += sConfigCode + ",";
            buffWriter.write(sConfigCode + ".CODE="+configDet.getCode());
            buffWriter.newLine();
//            armConfigFile.setString(sConfigCode + ".CODE", configDet.getCode());
            if (configDet.getDescription() != null)
                buffWriter.write(sConfigCode + ".LABEL="+ configDet.getDescription());
//              armConfigFile.setString(sConfigCode + ".LABEL", configDet.getDescription());
            else
                buffWriter.write(sConfigCode + ".LABEL= ");
            buffWriter.newLine();
//              armConfigFile.setString(sConfigCode + ".LABEL", "");
			//Ruchi: 1800 - sort discounts by Code/Priority - Japan
            if(configDet.getPriority() != null) {
            	buffWriter.write(sConfigCode + ".PRIORITY= "+configDet.getPriority());
            } else {
            	 buffWriter.write(sConfigCode + ".PRIORITY= ");
            }
            buffWriter.newLine();
          }
          sConfigType = sConfigType.substring(0, sConfigType.lastIndexOf(","));
          buffWriter.write(sTmp+"="+sConfigType);
          buffWriter.newLine();
//          armConfigFile.setString(sTmp, sConfigType);
        }
        buffWriter.flush();
        buffWriter.close();
        fileWriter.close();
      }
      theMgr.addGlobalObject("ARMANI_CONFIG_DOWNLOAD_DATE", new java.util.Date(), true);
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File armaniCommon = new File(FileMgr.getLocalFile("config", "ArmaniCommon.cfg"));
        armaniCommon.delete();
        File backup = new File(FileMgr.getLocalFile("config", "ArmaniCommon.bkup"));
        backup.renameTo(armaniCommon);
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
