/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

/**
 * Description:Downloads currency rates
 * Created By:Khyati Shah
 * Created Date:1/24/2005
 */
/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	CurrencyRateBootStrap
 * 2    07/01/05        KS/MP                                   ArmCurrency Download Prob(INC file -> DAT file)
 */
import java.awt.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.logging.*;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.store.CMSStore;


/**
 *
 * <p>Title: CurrencyRateBootStrap</p>
 *
 * <p>Description: This is currency Boot strap class for downloading currency
 * rate details file at RPOS startup</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CurrencyRateBootStrap implements IBootStrap {
  private IBrowserManager theMgr;
  private BootStrapManager bootMgr;
  ConfigMgr config = new ConfigMgr("currency.cfg");
  private boolean backUpNeeded = false;
  private File backup = new File(FileMgr.getLocalFile("currency", "currencyrates.bkup"));

  /**
   * Default Constructor
   */
  public CurrencyRateBootStrap() {
  }

  /**
   * This method is used to get name of item boot strap file
   * @return String
   */
  public String getName() {
    return "CurrencyRateBootStrap";
  }

  /**
   * This method returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return "This bootstrap determines if the currency rate file needs to be downloaded.";
  }

  /**
   * This method is used to download currencyrates dat file at client location
   * @param theMgr IBrowserManager
   * @param parentFrame Window
   * @param bootMgr BootStrapManager
   * @return BootStrapInfo
   */
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      File fileBackup = new File(FileMgr.getLocalFile("currency", "currencyrates.bkup"));
      // check to make sure that currencyrates.dat exists and its not in the backup
      File fileArmCurrency = new File(FileMgr.getLocalFile("currency", "currencyrates.dat"));
      if (!fileArmCurrency.exists()) {
        fileBackup = new File(FileMgr.getLocalFile("currency", "currencyrates.bkup"));
        if (fileBackup.exists()) {
          fileBackup.renameTo(fileArmCurrency);
        } else { // no files, so delete date from last item download
          File fileDate = new File(FileMgr.getLocalFile("repository", "CURRENCYRT_DOWNLOAD_DATE"));
          fileDate.delete();
        }
      }
      //
      //      else if(!backup.exists()&& fileArmCurrency.exists() ){
      //         backUpNeeded = true;
      //
      //      }
      //      if(backup.exists())
      //        backUpNeeded = false;
      if (!theMgr.isOnLine()) {
        return new BootStrapInfo(this.getClass().getName());
      }
      bootMgr.setBootStrapStatus("Checking currency rate download date...");
      Date date = (Date)theMgr.getGlobalObject("CURRENCYRT_DOWNLOAD_DATE");
      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12) || isFileCorrupted()) {
        downloadFile();
      }
    } catch (Exception ex) {
      System.out.println("Exception CurrencyRateBootStrap.start()->" + ex);
      ex.printStackTrace();
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   * This method reads currency.cfg and download currencyrates dat file at
   * client location
   * @throws Exception
   */
  private void downloadFile()
      throws Exception {
    try {
      File fileArmCurrencyBkup = new File(FileMgr.getLocalFile("currency", "currencyrates.bkup"));
      if (fileArmCurrencyBkup.exists()) {
        fileArmCurrencyBkup.delete();
      }
      File fileArmCurrency = new File(FileMgr.getLocalFile("currency", "currencyrates.dat"));
      boolean renam = fileArmCurrency.renameTo(new File(FileMgr.getLocalFile("currency"
          , "currencyrates.bkup")));
      File fileArmCurrencyDat = new File(FileMgr.getLocalFile("currency", "currencyrates.dat"));
      ConfigMgr config = new ConfigMgr("currency.cfg");
      CMSExchangeRateServices currencyRateDownloadServices = (CMSExchangeRateServices)config.
          getObject("CURRENCYRATE_DOWNLOAD_IMPL");
      bootMgr.setBootStrapStatus("Downloading currency rate file");
      //CurrencyRate[] currencyRates = currencyRateDownloadServices.findAllCurrencyRates();
      CMSStore store = (CMSStore)theMgr.getGlobalObject("STORE");
      CurrencyRate[] currencyRates = currencyRateDownloadServices.findAllCurrencyRates(store.
          getPreferredISOCountry(), store.getPreferredISOLanguage());
      writeToFile(fileArmCurrencyDat, currencyRates);
      theMgr.addGlobalObject("CURRENCYRT_DOWNLOAD_DATE", new java.util.Date(), true);
    } catch (Exception ex) {
      try {
        File fileArmCurrency = new File(FileMgr.getLocalFile("currency", "CurrencyRate.dat"));
        fileArmCurrency.delete();
        File backup = new File(FileMgr.getLocalFile("currency", "CurrencyRate.bkup"));
        backup.renameTo(fileArmCurrency);
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
   * This method is used to write in currencyrates.dat file
   * @param fileArmCurrency File
   * @param currencyRates CurrencyRate[]
   * @throws INIFileException
   */
  private void writeToFile(File fileArmCurrency, CurrencyRate currencyRates[])
      throws INIFileException {
    INIFile iniFileCurrency = new INIFile(fileArmCurrency, true);
    int size = currencyRates.length;
    String date = new String();
    for (int i = 0; i < size; i++) {
      if (currencyRates[i].getUpdateDate() != null) {
        try {
          String dateFormat = config.getString("DATE_FORMAT_STRING");
          SimpleDateFormat df = new SimpleDateFormat(dateFormat);
          df.setLenient(false);
          date = df.format(currencyRates[i].getUpdateDate());
        } catch (Exception e) {}
      }
      if (currencyRates[i].getFromCurrency() != null && currencyRates[i].getToCurrency() != null
          && currencyRates[i].getConversionRate() != null && currencyRates[i].getUpdateDate() != null) {
        iniFileCurrency.writeEntry(currencyRates[i].getFromCurrency() + "."
            + currencyRates[i].getToCurrency()
            , currencyRates[i].getConversionRate().toString() + "|" + date.toString() + "@" + currencyRates[i].getTenderCode());
      }
    }
  }

  /**
   * This method id used check whether file is currupted or not
   * @return boolean
   */
  private boolean isFileCorrupted() {
    File fileArmCurrency = new File(FileMgr.getLocalFile("currency", "currencyrates.dat"));
    INIFile iniFileCurrency = null;
    try {
      iniFileCurrency = new INIFile(fileArmCurrency, true);
      String key = "";
      String value = "";
      StringTokenizer st = null;
      StringTokenizer stVal = null;
      String ratio = "";
      for (Enumeration e = iniFileCurrency.getKeys(); e.hasMoreElements(); ) {
        key = (String)e.nextElement();
        value = iniFileCurrency.getValue(key);
        st = new StringTokenizer(key, ".");
        stVal = new StringTokenizer(value, "|");
        if (st.countTokens() != 2) {
          return true;
        }
        // Will be 1 in case of US and 2 in case of Europe
        if (stVal.countTokens() == 0 || stVal.countTokens() > 2) {
          return true;
        } while (stVal.hasMoreElements()) {
          ratio = (String)stVal.nextElement();
          break;
        }
        try {
          Double.parseDouble(ratio);
        } catch (NumberFormatException ne) {
          return true;
        }
      }
    } catch (INIFileException ie) {
      return true;
    }
    return false;
  }
}

