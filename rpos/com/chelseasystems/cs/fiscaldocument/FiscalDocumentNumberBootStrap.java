/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.fiscaldocument;

/**
 * <p>Title: FiscalDocumentNumberBootStrap </p>
 * <p>Description:Downlaods FiscalDocumentNumber at BootStrap </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 06-14-2005 | Manpreet  | N/A       | Print Fiscal Document                        |
 --------------------------------------------------------------------------------------------
 */
import  java.awt.*;
import  java.util.*;
import  java.io.*;
import  com.chelseasystems.cr.util.*;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cr.store.*;
import  com.chelseasystems.cs.pos.CMSTransactionPOSServices;
import  com.chelseasystems.cr.appmgr.bootstrap.*;
import  com.chelseasystems.cr.appmgr.*;
import  com.chelseasystems.cr.logging.*;
import  com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import  com.chelseasystems.cs.register.CMSRegister;


/**
 * put your documentation comment here
 */
public class FiscalDocumentNumberBootStrap
    implements IBootStrap {
  /**
   * BootStrapManager
   */
  private BootStrapManager bootStrapMgr;
  /**
   * BrowswerManager
   */
  private IBrowserManager theBrowserMgr;
  /**
   * FiscalFileLocation
   */
  private final String FISCAL_FILE_LOCATION;
  /**
   * Fiscal File Name
   */
  private final String FISCAL_FILE = "/FiscalDocumentNumber.ser";
  /**
   * Backup File Name
   */
  private final String BACKUP_FILE = "/FiscalDocumentNumber.bkup";
  /**
   * Fiscal Config file
   */
  private final String FISCAL_CONFIG_FILE = "fiscal_document.cfg";
  /**
   * Document path string
   */
  private final String FISCAL_DOCUMENT_PATH = "FISCAL_DOCUMENT_PATH";

  /**
   * Default Constructor
   */
  public FiscalDocumentNumberBootStrap () {
    ConfigMgr configFiscalDoc = new ConfigMgr(FISCAL_CONFIG_FILE);
    FISCAL_FILE_LOCATION = configFiscalDoc.getString(FISCAL_DOCUMENT_PATH);
  }

  /**
   *
   * @return String
   */
  public String getName () {
    return  "FiscalDocumentNumberBootStrap";
  }

  /**
   * returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc () {
    return  "This bootstrap determines if the FiscalDocumentNumber file needs to be downloaded. on Master register";
  }

  /**
   * Start BootStrap process
   * @param theMgr IBrowserManager
   * @param parentFrame Window
   * @param bootStrapMgr BootStrapManager
   * @return BootStrapInfo
   */
  public BootStrapInfo start (IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootStrapMgr = bootMgr;
      this.theBrowserMgr = theMgr;
      CMSRegister register = (CMSRegister)theBrowserMgr.getGlobalObject("REGISTER");
      // If currentRegister isnt a master register dont download file.
      if (register.getRegisterType() == null || !register.getRegisterType().equals("M"))
        return  new BootStrapInfo(this.getClass().getName());
      File fileFiscalDocNum = new File(FISCAL_FILE_LOCATION + FISCAL_FILE);
      if (!fileFiscalDocNum.exists()) {
        File fileBackup = new File(FISCAL_FILE_LOCATION + BACKUP_FILE);
        if (fileBackup.exists()) {
          fileBackup.renameTo(fileFiscalDocNum);
        }
        else {
          File fileDate = new File(FileMgr.getLocalFile("repository", "FISCAL_DOCUMENT_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      if (!theBrowserMgr.isOnLine())
        return  new BootStrapInfo(this.getClass().getName());
      bootStrapMgr.setBootStrapStatus("Checking Fiscal Document download date");
      Date date = (Date)theBrowserMgr.getGlobalObject("FISCAL_DOCUMENT_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12) || !fileFiscalDocNum.exists())
        downloadFile();
    } catch (Exception ex) {
      System.out.println("Exception FiscalDocumentBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception", "See Exception", LoggingServices.MAJOR, ex);
    }
    return  new BootStrapInfo(this.getClass().getName());
  }

  /**
   * Download FiscalDocumentNumber file on Master register
   * @throws Exception
   */
  private void downloadFile () throws Exception {
    try {
      Store store = (Store)theBrowserMgr.getGlobalObject("STORE");
      CMSRegister register = (CMSRegister)theBrowserMgr.getGlobalObject("REGISTER");
      // IF register is a Master Register
      if (register.getRegisterType() != null && register.getRegisterType().equals("M")) {
        File backup = new File(FISCAL_FILE_LOCATION + BACKUP_FILE);
        if (backup != null && backup.exists())
          backup.delete();
        File fiscalDocNoFile = new File(FISCAL_FILE_LOCATION + FISCAL_FILE);
        fiscalDocNoFile.renameTo(backup);
        fiscalDocNoFile.delete();

        //Extract the previous Fiscal Document Number Information
        FiscalDocumentNumber oldFiscalDocNum = null;
        try {
          FileInputStream fileReader = new FileInputStream(backup);
          ObjectInputStream objectReader = new ObjectInputStream(fileReader);
          oldFiscalDocNum = (FiscalDocumentNumber)objectReader.readObject();
          fileReader.close();
          objectReader.close();
        } catch (Exception e) {}

        fiscalDocNoFile.createNewFile();
        ConfigMgr config = new ConfigMgr("pos.cfg");
        CMSTransactionPOSServices cmsTransactionPOSServices = (CMSTransactionPOSServices)config.getObject("CLIENT_IMPL");
        bootStrapMgr.setBootStrapStatus("Downloading FiscalDocumentNumber file");
        FiscalDocumentNumber fiscalDocumentNumber = cmsTransactionPOSServices.findFiscalDocNumByStoreAndRegister(store.getId(), register.getId());

        //Check for the Latest Fiscal Document Number and assign accordingly
        if (oldFiscalDocNum != null) {
          if (oldFiscalDocNum.getNextCreditNoteNo() > fiscalDocumentNumber.getNextCreditNoteNo())
            fiscalDocumentNumber.setNextCreditNoteNo(oldFiscalDocNum.getNextCreditNoteNo());
          if (oldFiscalDocNum.getNextDDTNo() > fiscalDocumentNumber.getNextDDTNo())
            fiscalDocumentNumber.setNextDDTNo(oldFiscalDocNum.getNextDDTNo());
          if (oldFiscalDocNum.getNextVATNo() > fiscalDocumentNumber.getNextVATNo())
            fiscalDocumentNumber.setNextVATNo(oldFiscalDocNum.getNextVATNo());
        }

        FileOutputStream fileStream = null;
        fileStream = new FileOutputStream(fiscalDocNoFile);
        ObjectOutputStream objStream = new ObjectOutputStream(fileStream);
        objStream.writeObject(fiscalDocumentNumber);
        objStream.flush();
        theBrowserMgr.addGlobalObject("FISCAL_DOCUMENT_DOWNLOAD_DATE", new java.util.Date(), true);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        File fiscalDocNoFile = new File(FISCAL_FILE_LOCATION + FISCAL_FILE);
        fiscalDocNoFile.delete();
        File backup = new File(FISCAL_FILE_LOCATION + BACKUP_FILE);
        backup.renameTo(fiscalDocNoFile);
      } catch (Exception ex1) {}
      System.out.println("Exception downloadFile()->" + ex);
      ex.printStackTrace();
    } finally {
      if (theBrowserMgr instanceof IApplicationManager)
        ((IApplicationManager)theBrowserMgr).closeStatusDlg();
    }
  }
}



