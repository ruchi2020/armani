/*
 * History (tab separated):-
 * Vers	Date	       By	Spec		                Description
 * 1	2/4/05	       KS	POS_IS_ItemDownload_Rev1	Item bootstrap
 * 2    4/18/05         KS      Changes the item file to         Enhancement
 *                              regidstoreID_items.dat
 * 3    5/3/05          KS      Show Error dlg if nothing is downloaded Bug
 * 4    2/23/06         MD  Issues #1128 and 1320       ReadLine on RandomAccessFile
 *                                                      is not safe for unicode chars
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.util.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.logging.CMSLoggingFileServices;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.dlg.OptionDlg;


/**
 *
 * <p>Title: ItemDetailsBootStrap</p>
 *
 * <p>Description: This is item Boot strap class for processing the items
 * details file at RPOS startup</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public class ItemDetailsBootStrap implements IBootStrap {
  private IBrowserManager theMgr;
  private BootStrapManager bootMgr;
  private static ResourceBundle res = ResourceManager.getResourceBundle();
  String datFile = "";
  Process process = null;
  String dataDir = "";
  String storeId = "";
  String zipFileName = "";
  String zipFile = "";
  File newFile;
  String incFileName = "";
  String logDir = "";
  ItemDetailsMap map = new ItemDetailsMap();
  LoggingFileServices loggingFileServ;
  CMSLoggingFileServices loggingFileServices;
  String logFileName;
  String regStoreId = null;
  String registerId = null;

  //  CMSLoggingFileServices loggingFileCurrent = (CMSLoggingFileServices)LoggingServices.getCurrent();
  /**
   * This method is used to get name of item boot strap file
   * @return String
   */
  public String getName() {
    return "ItemDetailsBootStrap";
  }

  /**
   * This method returns the long description of the bootstrap
   * @return String the long description of the bootstrap
   */
  public String getDesc() {
    return res.getString("This bootstrap loads item details file");
  }

  /**
   * This method reads item.cfg and download item dat file at client location
   * @param theMgr IBrowserManager
   * @param parentFrame Window
   * @param bootMgr BootStrapManager
   * @return BootStrapInfo
   */
 
  
  
  public long daysBetween(Date startDate, Date endDate) {
      Calendar cal1 = Calendar.getInstance();cal1.setTime(endDate);  
      Calendar cal2 = Calendar.getInstance();cal2.setTime(startDate);  

      Calendar date = (Calendar) cal1.clone();
      long daysBetween = 0;
      while (date.before(cal2)) {
      date.add(Calendar.DAY_OF_MONTH, 1);
      daysBetween++;
      }
      return daysBetween;
      } 


  
 
  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
    try {
      this.bootMgr = bootMgr;
      this.theMgr = theMgr;
      ConfigMgr configMgr = new ConfigMgr("item.cfg");
      //logfile
      //      logDir = FileMgr.getLocalDirectory(configMgr.getString("ITEM_LOG_DIR"));
      logDir = "../files/prod/" + (configMgr.getString("ITEM_LOG_DIR"));
      loggingFileServ = (LoggingFileServices)LoggingServices.getCurrent();
      loggingFileServices = (CMSLoggingFileServices)loggingFileServ;
      logFileName = loggingFileServices.getLogFile();
      loggingFileServices.setLogFile(logDir);
      registerId = ((Register)theMgr.getGlobalObject("REGISTER")).getId();
      storeId = ((Store)theMgr.getGlobalObject("STORE")).getId();
      regStoreId = registerId + storeId;
      dataDir = FileMgr.getLocalDirectory(configMgr.getString("ITEM_DATA_CLIENT_DIR"));
      zipFileName = regStoreId + "_items.zip";
      incFileName = regStoreId + "_items.inc";
      //fs
      //zipFile = dataDir + incFileName;
      //      String newZipFile = dataDir + File.separator + "new_" + storeId + "_items.zip";
      zipFile = dataDir + File.separator + incFileName;
      String newZipFile = dataDir + "new_" + regStoreId + "_items.zip";
      datFile = dataDir + regStoreId + "_items.dat";
      //newFile = new File(dataDir + File.separator + zipFileName);
      newFile = new File(dataDir + zipFileName);
      //Check if downloaded today
      Date businessDate = (Date)theMgr.getGlobalObject("PROCESS_DATE");
      Date d = (Date)theMgr.getGlobalObject("ITEM_DOWNLOAD_DATE");
      long days = 0;
 
      
      if (new File(datFile).exists() == true) 
      {
     
         Date filedate = new Date(new File(datFile).lastModified());
          days =  daysBetween( businessDate, filedate);
         System.out.println("data file : " + days);
        
      }
      
      
      if (new File(datFile).exists() == false) {
        d = null;
      }
   
      boolean success = false; 
     
      boolean doRetry;
      //Anjana:: to download the item file at every POS startup
       //   if (d == null || checkDownloadDate(d)) 
        //      {
        	  if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
                  doRetry = ((IApplicationManager)theMgr).showOptionDlg(res.getString("Item download file "),
                      res.getString("Do you want to download the item local file?" +"(is "+days+" day old)"
                              +""),res.getString("Yes"), res.getString("No"));  
        	  } else {
        		    doRetry = true;  
        	  }
              if (doRetry)//YES 
              	{ 
            	  success = downloadFile(d);
                }
       //   }   
     
        if (success) {
            try {
            process = Runtime.getRuntime().exec("cmd /c copy " + newZipFile + " " + zipFileName);
            process.waitFor();
            Runtime.getRuntime().exec("cmd /c del " + newZipFile);
            ZipUtil.unzip(newFile.toString(), dataDir);
            File test = new File(dataDir + File.separator + regStoreId + "_items");
            if (test.exists()) {
              String incFileName = dataDir + File.separator + regStoreId + "_items.inc";
              File checkFile = new File(incFileName);
              if (checkFile.exists()) {
                Process ps = Runtime.getRuntime().exec("cmd /c del " + incFileName);
                ps.waitFor();
            }
              boolean successf = test.renameTo(new File(incFileName));
            }
            if("FALSE".equalsIgnoreCase(configMgr.getString("SHOULD_MERGE_ITEM_FILE"))){
            	mergeFiles(false);
            }else{
            	mergeFiles(true);
            }
            if (newFile.exists())
              newFile.delete();
            Runtime.getRuntime().exec("cmd /c del " + newFile);
          } catch (Exception e) {
            Runtime.getRuntime().exec("cmd /c del " + newZipFile);
          }
        } else {
          loggingFileServices.logMsg(res.getString("No item data file to be downloaded"));
          loggingFileServices.recordMsg();
        }
      
      
      // load barcodemap
      map.loadMapByBarCode(dataDir + regStoreId);
      //load by Id
      //map.loadMapById(regStoreId, "_items.dat", true);
      if (!theMgr.isOnLine())
        return new BootStrapInfo(this.getClass().getName());
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
          , "See Exception", LoggingServices.MAJOR, ex);
    } finally {
      File f = new File(dataDir + File.separator + regStoreId + "_items.dat");
      if (!f.exists()) {
        f = null;
        ((IApplicationManager)theMgr).showErrorDlg(res.getString("Items file does not exist. Contact technical support. Shutting down POS client..."));
        loggingFileServices.logMsg(res.getString("Items file does not exist. Contact technical support. Shutting down POS client..."));
        System.out.println(res.getString("Items file does not exist. Contact technical support. Shutting down POS client..."));
        System.exit(1);
      } else {
        if (f.length() == 0) {
          IApplicationManager theAppMgr = (IApplicationManager)theMgr;
          theAppMgr.showErrorDlg(res.getString("Problem with Item file, contact technical support"));
          loggingFileServices.logMsg(res.getString(
              "Problem with Item file, contact technical support"));
          loggingFileServices.recordMsg();
        }
      }
      //      loggingFileServices.setCurrent(loggingFileCurrent);
      loggingFileServices.setLogFile(logFileName);
    }
    return new BootStrapInfo(this.getClass().getName());
  }

  /**
   * This method reads item.cfg and download item dat file at client location
   * @param d Date
   * @return boolean
   */
  private boolean downloadFile(Date d) {
    ConfigMgr config = new ConfigMgr("item.cfg");
    CMSItemServices itemDownloadServices = (CMSItemServices)config.getObject("CLIENT_IMPL");
    bootMgr.setBootStrapStatus("Downloading Item data file");
    try {
      byte[] itemZipBytes = itemDownloadServices.getItemFile(regStoreId + "_items", storeId, d);
      if (itemZipBytes != null) {
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(itemZipBytes);
        fos.flush();
        fos.close();
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      //ex.printStackTrace();
      loggingFileServices.logMsg(this.getClass().getName(), ex);
      loggingFileServices.recordMsg();
      return false;
    }
  }

  /**
   *
   * @param fileName String
   * @param m Map
   * @param ID String
   * @return String
   */
  String getItemString(String fileName, Map m, String ID) {
    String temp = "";
    if (m.containsKey(ID)) {
      try {
        ConfigMgr configMgr = new ConfigMgr("item.cfg");
        File test = new File(dataDir + File.separator + regStoreId + fileName);
        RandomAccessFile rf = new RandomAccessFile(test, "rw");
        Long pos = (Long)m.get(ID);
        rf.seek(pos.longValue());
        String s = rf.readLine();
        temp = s;
        rf.close();
      } catch (Exception lf) {
        loggingFileServices.logMsg(this.getClass().getName(), lf);
        loggingFileServices.recordMsg();
      }
    }
    return temp;
  }

  /*
   * Utility method to extract string upto newline from byte array
   */
  private String getLineString(byte[] b, int len) {
      String tmp = new String(b, 0, len);
      int lineLen = tmp.indexOf('\n');
      if (lineLen == -1) { // Most likely end of file reached.
          return tmp;
      } else if (lineLen == 0) { // First char is \n
          return "";
      } else if (tmp.charAt(lineLen-1) == '\r') { // Prev char is \r
          lineLen = lineLen - 1;
          if (lineLen == 0) { // First two chars \r\n
              return "";
          }
      }
      return tmp.substring(0, lineLen);
  }

  /**
   *
   * @param rf RandomAccessFile
   * @param m Map
   * @param ID String
   * @param byteArray byte[]
   * @return String
   */
  String getItemString(RandomAccessFile rf, Map m, String ID, byte[] byteArray) {
    String temp = "";
    if (m.containsKey(ID)) {
      try {
        Long pos = (Long)m.get(ID);
        System.out.println("Seeking at : " + pos);
        rf.seek(pos.longValue());
        byte[] b = new byte[1024];
        int bRead = rf.read(b);
        temp = getLineString(b, bRead);
      } catch (Exception lf) {
        loggingFileServices.logMsg(this.getClass().getName(), lf);
        loggingFileServices.recordMsg();
      }
    }
    return temp;
  }

  /**
   * This method is used to add item in item dat file
   * @param rf BufferedWriter
   * @param s String
   * @param hasCRLF boolean
   */
  public void addItem(BufferedWriter rf, String s, boolean hasCRLF) {
    try {
      rf.write(s, 0, s.length());
      if (hasCRLF)
        rf.write("\r\n");
      else
        rf.write("\n");
    } catch (Exception df) {
      loggingFileServices.logMsg(this.getClass().getName(), df);
      loggingFileServices.recordMsg();
      loggingFileServices.getCurrent().logMsg("my log", df);
    }
  }

  /**
   * This method is used to merge items.inc and items.dat file
   */
  private void mergeFiles(boolean shouldMerge) {
    try {
	//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
    	//added as global variable to resolve item downloading issue of US
    	BufferedWriter bufWrite = null;
    	BufferedReader bufRead = null;
	//Ends here	
      System.out.println("MERGING");
      Map dataHash = null;
      ConfigMgr configMgr = new ConfigMgr("item.cfg");
      String dataDir = FileMgr.getLocalDirectory(configMgr.getString("ITEM_DATA_CLIENT_DIR"));
      Map incHash = map.loadMapById(dataDir + regStoreId, "_items.inc", false);
      if (shouldMerge)
        dataHash = map.loadMapById(dataDir + regStoreId, "_items.dat", false);
      byte[] byteArray = new byte[1024];
      if (dataHash == null || dataHash.size() == 0) {
        // No dat file exists ..
        // Rename inc to dat and proceed
        File incFile = new File(dataDir + regStoreId + "_items.inc");
        File datFile = new File(dataDir + regStoreId + "_items.dat");
        if (datFile.exists()) {
          System.out.println("Delete of DAT file : " + datFile.delete());
        }
        if (incFile.exists()) { // Inc file exists
          File newDatFile = new File(dataDir + regStoreId + "_items.dat");
		  //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
          //region check added by shushma to resolve item downloading issue of US
	  if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
	  //BufferedWriter bufWrite = new BufferedWriter(new OutputStreamWriter 
      //    				(new FileOutputStream(newDatFile), "UTF-8"));
		  bufWrite = new BufferedWriter(new OutputStreamWriter 
    				(new FileOutputStream(newDatFile), "UTF-8"));
       //BufferedReader bufRead = new BufferedReader(new InputStreamReader 
       //   						(new FileInputStream(incFile), "UTF-8"));
          bufRead = new BufferedReader(new InputStreamReader 
                  						(new FileInputStream(incFile), "UTF-8"));
	 }
	 else{
          //BufferedWriter bufWrite = new BufferedWriter(new FileWriter(newDatFile));
		 bufWrite = new BufferedWriter(new FileWriter(newDatFile));
	      //BufferedReader bufRead = new BufferedReader(new FileReader(incFile));
		 bufRead = new BufferedReader(new FileReader(incFile));
	  }
	  //Ends here
          char[] arr = new char[512];
          int len = 0;
          while ((len = bufRead.read(arr)) > 0) {
            bufWrite.write(arr, 0, len);
          }
          bufRead.close();
          bufWrite.close();
          System.out.println("&&&&&&&&&&&&&Deleting the INC file : " + incFile.delete());
          theMgr.addGlobalObject("ITEM_DOWNLOAD_DATE", new java.util.Date(), true);
          return;
        }
      }
      boolean hasCRLF = false;
      BufferedWriter rf = null;
      if (true) {
        File tmpFile = new File(dataDir + File.separator + regStoreId + "_items.tmp");
        rf = new BufferedWriter(new FileWriter(tmpFile));
      }
      RandomAccessFile datrf = null;
      if (true) {
        File tmpFile = new File(dataDir + File.separator + regStoreId + "_items.dat");
        datrf = new RandomAccessFile(tmpFile, "rw");
      }
      RandomAccessFile incrf = null;
      if (true) {
        File tmpFile = new File(dataDir + File.separator + regStoreId + "_items.inc");
        incrf = new RandomAccessFile(tmpFile, "rw");
        hasCRLF = ItemDetailsMap.hasCRLF(tmpFile);
      }
      if (dataHash.size() > 0) {
        //Read data from .dat
        for (Iterator dit = dataHash.keySet().iterator(); dit.hasNext(); ) {
          String ID = (String)dit.next();
          if (!incHash.containsKey(ID)) {
            String itemS = getItemString(datrf, dataHash, ID, byteArray);
            addItem(rf, itemS, hasCRLF);
          }
        }
      }
      //Read data from .inc
      for (Iterator hit = incHash.keySet().iterator(); hit.hasNext(); ) {
        String ID = (String)hit.next();
        String itemS = getItemString(incrf, incHash, ID, byteArray);
        addItem(rf, itemS, hasCRLF);
      }
      rf.close();
      datrf.close();
      incrf.close();
      datrf = null;
      incrf = null;
      if (true) {
        String datFile = dataDir + regStoreId + "_items.dat";
        File FF = new File(datFile);
        if (FF.exists()) {
          System.out.println("Deleting old DAT FILE .. new will be made : " + FF.delete());
        }
      }
      if (true) {
        File tmpFile = new File(dataDir + regStoreId + "_items.tmp");
        File datFile = new File(dataDir + regStoreId + "_items.dat");
        if (datFile.exists()) {
          System.out.println("Name of DAT file : " + datFile.getAbsolutePath() + ":"
              + datFile.getName());
          System.out.println("Delete of DAT file : " + datFile.delete());
        }
        if (tmpFile.exists()) { // Inc file exists
          File newDatFile = new File(dataDir + regStoreId + "_items.dat");
		  //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16
          //Region check added by shushma to resolve item downloading issue of US
	  if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
	   //BufferedWriter bufWrite = new BufferedWriter(new OutputStreamWriter (
       //   		new FileOutputStream(newDatFile), "UTF-8"));  
		  bufWrite = new BufferedWriter(new OutputStreamWriter (
	          		new FileOutputStream(newDatFile), "UTF-8"));     
        //BufferedReader bufRead = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFile), "UTF-8"));	
		  bufRead = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFile), "UTF-8"));
	   }
	  else{
	   //BufferedWriter bufWrite = new BufferedWriter(new FileWriter(newDatFile));
		  bufWrite = new BufferedWriter(new FileWriter(newDatFile));
	   //BufferedReader bufRead = new BufferedReader(new FileReader(tmpFile));
		  bufRead = new BufferedReader(new FileReader(tmpFile));
	   }
	   //Ends here
          char[] arr = new char[512];
          int len = 0;
          while ((len = bufRead.read(arr)) > 0) {
            bufWrite.write(arr, 0, len);
          }
          bufRead.close();
          bufWrite.close();
        }
        // Delete the tmp file
        System.out.println("&&&&&&&&&&&&&Deleting the INC file : " + tmpFile.delete());
        String sFile = dataDir + regStoreId + "_items.inc";
        File incFile = new File(sFile);
        if (incFile.exists()) {
          System.out.println("&&&&&&&&&&&&&Deleting the INC file : " + incFile.delete());
        }
      }
      if (true) {}
      theMgr.addGlobalObject("ITEM_DOWNLOAD_DATE", new java.util.Date(), true);
    } catch (Exception fg) {
      fg.printStackTrace();
      loggingFileServices.logMsg(this.getClass().getName(), fg);
      loggingFileServices.recordMsg();
    }
  }
  
//# 1913 Download timing to register
  private boolean checkDownloadDate(Date d){
	  Date businessDate = (Date)theMgr.getGlobalObject("PROCESS_DATE");
	  if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
		  	  return (!DateUtil.isSameDay(d,businessDate));
	  }
	  else{
		  return (DateUtil.isDate24HourOld(d));
	  }
  }
  
}

