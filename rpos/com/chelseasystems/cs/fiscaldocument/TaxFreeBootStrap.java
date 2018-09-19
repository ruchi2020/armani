/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;

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
 | 1    | 06-24-2005 | Megha     | N/A       |      Tax Free BootStrap                      |
 --------------------------------------------------------------------------------------------
 */
import  java.awt.*;
import  java.util.*;
import  java.io.*;
import  com.chelseasystems.cr.util.*;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cr.employee.*;
import  com.chelseasystems.cr.appmgr.bootstrap.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cs.xml.*;
import  com.chelseasystems.cr.logging.*;


/**
 * put your documentation comment here
 */
public class TaxFreeBootStrap
    implements IBootStrap {
  private BootStrapManager bootMgr;
  private IBrowserManager theMgr;

  /**
   *
   */
  public TaxFreeBootStrap () {
  }

  /**
   *
   * @return String
   */
  public String getName () {
    return  "TaxFreeBootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc () {
    return  "This bootstrap determines if the tax free doc needs to be downloaded";
  }

  /**
   *
   * @param theMgr IBrowserManager
   * @param parentFrame Window
   * @param bootMgr BootStrapManager
   * @return BootStrapInfo
   */
  public BootStrapInfo start (IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      // check to make sure that tax_free.xml exists and its not in the backup
      File filetaxFree = new File(FileMgr.getLocalFile("xml", "tax_free.xml"));
      if (!filetaxFree.exists()) {
        File fileBackup = new File(FileMgr.getLocalFile("xml", "tax_free.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(filetaxFree);
        }
        else {                  // no files, so delete date from last loyalty rules download
          File fileDate = new File(FileMgr.getLocalFile("repository", "TAX_FREE_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theMgr.isOnLine())
        return  new BootStrapInfo(this.getClass().getName());
      bootMgr.setBootStrapStatus("Checking tax free download date");
      Date date = (Date)theMgr.getGlobalObject("TAX_FREE_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12))
        downloadFile();
    } catch (Exception ex) {
      System.out.println("Exception TaxFreeBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception", "See Exception", LoggingServices.MAJOR, ex);
    }
    return  new BootStrapInfo(this.getClass().getName());
  }

  /**
   *
   * @throws Exception
   */
  private void downloadFile () throws Exception {
    try {
      File backup = new File(FileMgr.getLocalFile("xml", "tax_free.bkup"));
      backup.delete();
      File taxFreeFile = new File(FileMgr.getLocalFile("xml", "tax_free.xml"));
      taxFreeFile.renameTo(backup);
      taxFreeFile.delete();
      Store store = (Store)theMgr.getGlobalObject("STORE");
      ConfigMgr config = new ConfigMgr("taxfree.cfg");
      TaxFreeServices taxFreeDownloadServices = (TaxFreeServices)config.getObject("CLIENT_IMPL");
      bootMgr.setBootStrapStatus("Downloading tax free file");
      TaxFree[] taxFree = taxFreeDownloadServices.findTaxFreeForStore(store.getId());
      new TaxFreeXML().writToFile(taxFreeFile.getAbsolutePath(), taxFree);
      theMgr.addGlobalObject("TAX_FREE_DOWNLOAD_DATE", new java.util.Date(), true);
      //need to implement this
      TaxFreeFileServices taxFreeFileServices = (TaxFreeFileServices)config.getObject("CLIENT_LOCAL_IMPL");
      System.out.println("LOADING ALL TAX FREE FROM TAXFREE BOOTSTRAP INTO FILE SERVICES...");
      taxFreeFileServices.loadAllTaxFreeFromFile();
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File taxFreeFile = new File(FileMgr.getLocalFile("xml", "tax_free.xml"));
        taxFreeFile.delete();
        File backup = new File(FileMgr.getLocalFile("xml", "tax_free.bkup"));
        backup.renameTo(taxFreeFile);
      } catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    } finally {
      if (theMgr instanceof IApplicationManager)
        ((IApplicationManager)theMgr).closeStatusDlg();
    }
  }
}



