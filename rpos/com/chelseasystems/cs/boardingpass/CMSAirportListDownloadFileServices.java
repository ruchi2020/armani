/*
* CMSAirportListDownloadFileServices.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.config.IConfig;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.INIFileException;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

// On the basis of parameter value the airport list local file will be downloaded to the local register.
public class CMSAirportListDownloadFileServices extends CMSAirportListServices implements IConfig {
	private INIFile inifile = null;
	
	 public CMSAirportListDownloadFileServices() {
		    ConfigMgr mgr = new ConfigMgr("airport.cfg");
		    if (null == mgr) {
		      LoggingServices.getCurrent().logMsg(getClass().getName(), "Constructor"
		          , "Could not create configuration manager.", "Make sure the airport.cfg file exists.", 1);
		    }
		    try {
		      inifile = new INIFile(FileMgr.getLocalFile("airportlist", "airportlist.dat"), false);
		    } catch (INIFileException ie) {
		      LoggingServices.getCurrent().logMsg(getClass().getName(), "Constructor"
		          , "Initialization failed due to INIFile exception.", "Make sure the airport.cfg file contains an entry with a key of EXCHANGE_RATE_FILENAME that contains the name of a valid INIFile."
		          , 1, ie);
		    } catch (Exception e) {
		      LoggingServices.getCurrent().logMsg(getClass().getName(), "Constructor"
		          , "Initialization failed.", "Examine the following exception information", 1, e);
		    }
		  }
	public void processConfigEvent(String[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CMSAirportDetails[] getAirportDetails() throws Exception {
		// TODO Auto-generated method stub
		 StringBuffer msg;
		    try {
		      //          Enumeration enm = inifile.getKeys();
		      Properties prop = inifile.getProperties();
		      Enumeration enm = prop.propertyNames();
		      enm = prop.propertyNames();
		      ArrayList tempCurRates = new ArrayList();
		      ConfigMgr config = new ConfigMgr("airport.cfg");
		      while (enm.hasMoreElements()) {
		    	CMSAirportDetails airportDetails = new CMSAirportDetails();
		        String toFromKey = (String)enm.nextElement();
		        int seperator = toFromKey.indexOf(".");
		        String valWithDateAndCode = inifile.getValue(toFromKey);
		        int dateSeperator = valWithDateAndCode.indexOf("|");
		        int tenderCodeSeperator = valWithDateAndCode.indexOf("@");
		        String conversionRate = valWithDateAndCode.substring(0, dateSeperator);
		        String dateString = valWithDateAndCode.substring(dateSeperator, tenderCodeSeperator);
		        String tenderCode = valWithDateAndCode.substring(tenderCodeSeperator+1);
		        airportDetails.setAirportCode(toFromKey.substring(0, seperator));
		        airportDetails.setAirportCode(toFromKey.substring(seperator + 1));
		        airportDetails.setAreaCode(dateString);
		        airportDetails.setFlag(tenderCode);
		        tempCurRates.add(airportDetails);
		      }
		      return (CMSAirportDetails[])tempCurRates.toArray(new CMSAirportDetails[0]);
		    } 
		    catch (Exception ex) {
		      ex.printStackTrace();
		    }
		    return null;
	}
	@Override
	public boolean submit(CMSAirportDetails airportDetails) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
}
