/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.download;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08-24-2005 | Manpreet  | N/A       | Downloads Alteration.xml file on Client            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import java.awt.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.xml.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.pos.Alteration;
import com.chelseasystems.cs.store.CMSStore;


/**
 *
 * <p>Title: AlterationBootStrap</p>
 * <p>Description: Downloads Alteration.xml on client</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class AlterationBootStrap implements IBootStrap {
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
  public AlterationBootStrap() {
  }

  /**
   * Get Name of BootStrap
   * @return String
   */
  public String getName() {
    return "AlterationBootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if the Alteration file needs to be downloaded.";
  }

  /**
   * Start bootStrap process.
   * @param theMgr IBrowserManager
   * @param parentFrame Window
   * @param bootMgr BootStrapManager
   * @return BootStrapInfo
   */
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      // check to make sure that Alterations.xml exists and its not in the backup
      File fileAlterationsXML = new File(FileMgr.getLocalFile("xml", "Alterations.xml"));
      if (!fileAlterationsXML.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("xml", "Alterations.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(fileAlterationsXML);
        } else {
          // no files, so delete date from last alterations download
          File fileDate = new File(FileMgr.getLocalFile("repository", "ALTERATIONS_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
      bootMgr.setBootStrapStatus("Checking alterations download date");
      Date date = (Date)theMgr.getGlobalObject("ALTERATIONS_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)) {
        downloadFile();
      }
    } catch (Exception ex) {
      System.out.println("Exception AlterationBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   * Download Alterations.xml on Client
   * @throws Exception
   */
  private void downloadFile()
      throws Exception {
    try {
      File backup = new File(FileMgr.getLocalFile("xml", "Alterations.bkup"));
      backup.delete();
      File alterationsXML = new File(FileMgr.getLocalFile("xml", "Alterations.xml"));
      alterationsXML.renameTo(backup);
      alterationsXML.delete();
      ConfigMgr config = new ConfigMgr("armaniDownload.cfg");
      ArmaniDownloadServices armDownloadServices = (ArmaniDownloadServices)config.getObject(
          "CLIENT_IMPL");
      bootMgr.setBootStrapStatus("Downloading Alterations file");
      CMSStore store = (CMSStore)theMgr.getGlobalObject("STORE");
      // Retrieve AlterationItemGroups for Store's locale (Country and language)
      AlterationItemGroup[] alterationGrps = armDownloadServices.
          getAlterationItemGroupsByCountryAndLanguage(store.getPreferredISOCountry()
          , store.getPreferredISOLanguage());
      Alteration alteration = new Alteration();
      alteration.setAlterationGroups(alterationGrps);
      new AlterationXML().writToFile(alterationsXML.getAbsolutePath(), alteration);
      theMgr.addGlobalObject("ALTERATIONS_DOWNLOAD_DATE", new java.util.Date(), true);
      ArmaniDownloadFileServices armDwnldFileSrvc = (ArmaniDownloadFileServices)config.getObject(
          "CLIENT_LOCAL_IMPL");
      System.out.println("LOADING ALL ALTERATIONS FROM Alteration BOOTSTRAP INTO FILE SERVICES...");
      armDwnldFileSrvc.loadAllAlterationItemGrpsFromFile();
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File Alterations = new File(FileMgr.getLocalFile("xml", "Alterations.xml"));
        Alterations.delete();
        File backup = new File(FileMgr.getLocalFile("xml", "Alterations.bkup"));
        backup.renameTo(Alterations);
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

