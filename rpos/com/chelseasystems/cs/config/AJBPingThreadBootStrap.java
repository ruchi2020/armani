package com.chelseasystems.cs.config;

import java.awt.Window;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.appmgr.IBrowserManager;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapInfo;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapManager;
import com.chelseasystems.cr.appmgr.bootstrap.IBootStrap;
import java.awt.*;
import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cs.download.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.ajbauthorization.AJBPingThread;
import com.chelseasystems.cs.config.ArmTaxRateConfig;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

/**
 * This boot strap is added to start the AJBPingThread with client startup.
 */

public class AJBPingThreadBootStrap implements IBootStrap {

	 // Vivek Mishra : This is the reference for AJBPingThread used for starting
	 // the thread.
	 public static AJBPingThread ajbPingThread; 
	  /**
	   * BootstrapManager
	   */
	  private BootStrapManager bootMgr;
	  /**
	   * BrowserManager
	   */
	  private IBrowserManager theMgr;
	  
	  /*Added to log info and errors.*/
	  private static Logger log = Logger.getLogger(AJBPingThreadBootStrap.class.getName());

	  private static String storeID;
	  
	  private static String regiterID;
	  
	  private static ConfigMgr config;

      private static ConfigMgr storeCustomConfig;
	  
	  private String fipay_flag;
	  
	  /**
	   * put your documentation comment here
	   */
	  public AJBPingThreadBootStrap() 
	  {
		  

	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getName() {
	    return "AJB ping thread BootStrap";
	  }

	  /**
	   * returns the long description of the bootstrap
	   * @return String the long description of the bootstrap
	   */
	  public String getDesc() {
	    return "This bootstrap instantiates the AJBPingThread.";
	  }

	  /**
	   * put your documentation comment here
	   * @param theMgr
	   * @param parentFrame
	   * @param bootMgr
	   * @return
	   */
	  public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
		  	String fileName = "credit_auth.cfg";
			config = new ConfigMgr(fileName);
			storeCustomConfig = new ConfigMgr("store_custom.cfg");
			fipay_flag = storeCustomConfig.getString("FIPAY_Integration");
		// Vivek Mishra : Added code to start the AJBPingThread with server startup
		    String storeId = ((CMSStore)theMgr.getGlobalObject("STORE")).getId();
		    String registerId = ((CMSRegister)theMgr.getGlobalObject("REGISTER")).getId();

		    setStoreID(storeId);
		    setRegiterID(registerId);
		    
		    //Default value of the flag is Y if its not present in credit_auth.cfg
			if (fipay_flag == null) {
				fipay_flag = "Y";
			}
		    
			if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
		    	
			ajbPingThread = new AJBPingThread(storeId,registerId);
			//Vivek Mishra : Added in order to send the ping 106 at the time of POS startup if there is no 106 ping before 150 message.
			//ajbPingThread.ping();
			ajbPingThread.echoping();
			//Ends
		//Vivek Mishra : Commented as Ping Thread is no longer required to be run as a thread. 	
			//Thread ping = new Thread(ajbPingThread);
			//ping.start();
			log.info("AJBPIngThread has been started.");
			// End
		    }
		    else {
				log.info("AJBPIngThread is not started.");

		    }
	    try {
	    } catch (Exception ex) {
	    	log.error("Error caught during ping");
	      log.error(ex.toString());
	    }
	    return new BootStrapInfo(this.getClass().getName());
	  }
	  
	 // Vivek Mishra : Added method for providing current AJBPingTherad instance.
		public static AJBPingThread getPingThread() {
			return ajbPingThread;
		}
		
		public static boolean echoping()
		{
			//ajbPingThread.ping();
			if (ajbPingThread == null )
			{
				if(log.isDebugEnabled())
					log.info("AJBPIngThread echo ping has been called.");
				
				ajbPingThread = new AJBPingThread(getStoreID() ,getRegiterID());
				
			}
			
			return  ajbPingThread.echoping();
			
		}

		public static String getStoreID() {
			return storeID;
		}

		public static void setStoreID(String storeID) {
			AJBPingThreadBootStrap.storeID = storeID;
		}

		public static String getRegiterID() {
			return regiterID;
		}

		public static void setRegiterID(String regiterID) {
			AJBPingThreadBootStrap.regiterID = regiterID;
		}

		
		
}
