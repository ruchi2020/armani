/*
* ArmAirportListBootStrap.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IBrowserManager;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapInfo;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapManager;
import com.chelseasystems.cr.appmgr.bootstrap.IBootStrap;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.util.DateUtil;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.INIFileException;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListServices;
import com.chelseasystems.cs.logging.CMSLoggingFileServices;
import com.chelseasystems.cs.store.CMSStore;

/*
* This class is responsible to read the parameter value from the configuration file.
* On the basis of parameter value the airport list local file will be downloaded to the local register.
*
*/

public class ArmAirportListBootstrap implements IBootStrap{
	
	private IBrowserManager theMgr;
	  private BootStrapManager bootMgr;
	  ConfigMgr config = new ConfigMgr("airport.cfg");
	  private boolean backUpNeeded = false;
	  private File backup = new File(FileMgr.getLocalFile("airportlist", "airportlist.bkup"));
	  CMSLoggingFileServices loggingFileServices;
	  private static ResourceBundle res = ResourceManager.getResourceBundle();
	  
	  public ArmAirportListBootstrap() {
	  }
	  
	public String getDesc() {
		// TODO Auto-generated method stub
		return "This bootstrap determines if the Airport list file needs to be downloaded.";
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "ArmAirportListBootstrap";
	}

	public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
		// TODO Auto-generated method stub
		try{
	      this.bootMgr = bootMgr;
	      this.theMgr = theMgr;
	      File fileBackup = new File(FileMgr.getLocalFile("airportlist", "airportlist.bkup"));
	      // check to make sure that currencyrates.dat exists and its not in the backup
	      File fileArmAirport = new File(FileMgr.getLocalFile("airportlist", "airportlist.dat"));
	      if (!fileArmAirport.exists()) {
	        fileBackup = new File(FileMgr.getLocalFile("airportlist", "airportlist.bkup"));
	        if (fileBackup.exists()) {
	          fileBackup.renameTo(fileArmAirport);
	        } else { // no files, so delete date from last item download
	          File fileDate = new File(FileMgr.getLocalFile("repository", "AIRPORT_DOWNLOAD_DATE"));
	          fileDate.delete();
	        }
	      }
	      if (!theMgr.isOnLine()) {
	        return new BootStrapInfo(this.getClass().getName());
	      }
	      bootMgr.setBootStrapStatus("Checking airport list download date...");
	      Date date = (Date)theMgr.getGlobalObject("AIRPORT_DOWNLOAD_DATE");
	      if (date == null || DateUtil.isDateAtLeastHoursOld(date, 12)) {
	        downloadFile();
	      }
	    } catch (Exception ex) {
	      System.out.println("Exception ArmAirportListBootstrap.start()->" + ex);
	      ex.printStackTrace();
	      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception"
	          , "See Exception", LoggingServices.MAJOR, ex);
	    }
	    return new BootStrapInfo(this.getClass().getName());
	}
	
private void downloadFile()throws Exception {
  try {
    File fileArmCurrencyBkup = new File(FileMgr.getLocalFile("airportlist", "airportlist.bkup"));
    if (fileArmCurrencyBkup.exists()) {
      fileArmCurrencyBkup.delete();
    }
    File fileArmAirport = new File(FileMgr.getLocalFile("airportlist", "airportlist.dat"));
    boolean renam = fileArmAirport.renameTo(new File(FileMgr.getLocalFile("airportlist", "airportlist.bkup")));
    File fileArmAirportDat = new File(FileMgr.getLocalFile("airportlist", "airportlist.dat"));
    ConfigMgr config = new ConfigMgr("airport.cfg");
    String AIRPORTLIST_DOWNLOAD_IMPL = config.getString("AIRPORTLIST_DOWNLOAD_IMPL");
    CMSAirportListServices airportListDownloadServices = (CMSAirportListServices)config.
        getObject("AIRPORTLIST_DOWNLOAD_IMPL");
    bootMgr.setBootStrapStatus("Downloading airport list file");
    CMSStore store = (CMSStore)theMgr.getGlobalObject("STORE");
    CMSAirportDetails[] airportDetails = airportListDownloadServices.getAirportDetails();
    writeToFile(fileArmAirportDat, airportDetails);
    theMgr.addGlobalObject("AIRPORT_DOWNLOAD_DATE", new java.util.Date(), true);
  } catch (Exception ex) {
    try {
      File fileArmAirport = new File(FileMgr.getLocalFile("airportlist", "airportlist.dat"));
      fileArmAirport.delete();
      File backup = new File(FileMgr.getLocalFile("airportlist", "airportlist.bkup"));
      backup.renameTo(fileArmAirport);
    } catch (Exception ex1) {}
    System.out.println("Exception downloadFile()->" + ex);
    ex.printStackTrace();
  } finally {
    if (theMgr instanceof IApplicationManager) {
      ((IApplicationManager)theMgr).closeStatusDlg();
    }
  }
}
	private void writeToFile(File fileArmAirport, CMSAirportDetails airportDetails[])
    throws INIFileException, IOException {
  int size = airportDetails.length;
  String date = new String();
  BufferedWriter output = new BufferedWriter(new FileWriter(fileArmAirport));
  if(fileArmAirport.exists()){
  BufferedReader bufReader = new BufferedReader(new FileReader(fileArmAirport));
  StringBuffer buff = new StringBuffer();
  String lineSeperator = System.getProperty("line.separator");
  String readln = bufReader.readLine();
 
  for (int i = 0; i < size; i++) {
    if (airportDetails[i].getAirportDesc()!= null && airportDetails[i].getAirportCode()!= null
        && airportDetails[i].getAreaCode() != null && airportDetails[i].getFlag()!= null) {
        String airportDetailsValues = airportDetails[i].getAirportDesc().trim()+"|"+airportDetails[i].getAirportCode().trim()+"|"+airportDetails[i].getAreaCode().trim()+"|"+airportDetails[i].getFlag().trim();
        readln = (new StringBuilder()).append(airportDetailsValues).toString();
        buff.append((new StringBuilder()).append(readln).append(lineSeperator).toString());
        }
  } 
  if(bufReader != null)
  {
      bufReader.close();
  }
  FileWriter writer = new FileWriter(fileArmAirport);
  writer.write(buff.toString());
  if(writer != null)
  {
      writer.close();
  }
  }
  else{
	  IApplicationManager theAppMgr = (IApplicationManager)theMgr;
      theAppMgr.showErrorDlg(res.getString("Problem with airportlist.dat file, contact technical support"));
      loggingFileServices.logMsg(res.getString(
          "Problem with airportlist.dat file, contact technical support"));
      loggingFileServices.recordMsg();
  }
  }

}
