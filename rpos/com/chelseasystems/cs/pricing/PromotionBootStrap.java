/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//

package com.chelseasystems.cs.pricing;

import java.awt.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cs.pricing.*;
import com.chelseasystems.cr.appmgr.bootstrap.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.xml.*;
import com.chelseasystems.cr.logging.*;

/**
 *
 * <p>Title: PromotionBootStrap</p>
 *
 * <p>Description: This is item Boot strap class for processing the promotion
 * details file at RPOS startup</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PromotionBootStrap implements IBootStrap {
	private IBrowserManager theMgr;
	private BootStrapManager bootMgr;

	/**
	 * Default construction
	 */
	public PromotionBootStrap() {
	}

	/**
	 * Method used to get name of item boot strap file
	 * @return String
	 */
	public String getName() {
		return "PromotionBootStrap";
	}

	/**
	 * Method returns the long description of the bootstrap
	 * @return String the long description of the bootstrap
	 */
	public String getDesc() {
		return "This bootstrap determines if the promotion file needs to be downloaded.";
	}

	/**
	 * This method is used to download promotion and threshold promotion xml file
	 * at client site
	 * @param theMgr IBrowserManager
	 * @param parentFrame Window
	 * @param bootMgr BootStrapManager
	 * @return BootStrapInfo
	 */
	public BootStrapInfo start(IBrowserManager theMgr, Window parentFrame, BootStrapManager bootMgr) {
		try {
			this.bootMgr = bootMgr;
			this.theMgr = theMgr;
		//Anjana commented below for promotion xml auto download
		//checkPromotionFile();
		loadPromotions();
			if (!theMgr.isOnLine())
				return new BootStrapInfo(this.getClass().getName());
			bootMgr.setBootStrapStatus("Checking promotion download dates...");
			Date date = (Date) theMgr.getGlobalObject("PROMOTION_DOWNLOAD_DATE");
			Date itemDate = (Date) theMgr.getGlobalObject("ITEM_DOWNLOAD_DATE");
			//Anjana commented to remove date check for promotion download
			//if ((itemDate != null && DateUtil.isDateAtLeastHoursOld(itemDate, 12) == false) && (date == null || DateUtil.isDateAtLeastHoursOld(date, 12))) {
				//downloadPromotionFile();
			//}
			date = (Date) theMgr.getGlobalObject("THRESHOLD_PROMOTION_DOWNLOAD_DATE");
//		    PCR from Japan issue # 1913 Download timing to register
//          Code commented			
//			if ((itemDate != null && DateUtil.isDateAtLeastHoursOld(itemDate, 12) == false) && (date == null || DateUtil.isDateAtLeastHoursOld(date, 12))) {
//			end
//         ADDED new check for the same day		
			//Anjana commented to remove date check for promotion download	
			//if ((itemDate != null && checkDownloadDate(itemDate) == false) && (date == null || checkDownloadDate(date))) {
				//downloadThresholdPromotionFile();
			//}
		} catch (Exception ex) {
			System.out.println("Exception PromotionBootStrap.start()->" + ex);
			ex.printStackTrace();
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "start", "Exception", "See Exception", LoggingServices.MAJOR, ex);
		}
		return new BootStrapInfo(this.getClass().getName());
	}

	/**
	 * Method used to check  promotion.xml file existance
	 */
	public void checkPromotionFile() {
		// check to make sure that promotions.xml exists and its not in the backup
		File filePromotion = new File(FileMgr.getLocalFile("xml", "promotions.xml"));
		if (!filePromotion.exists()) {
			File fileBackup = new File(FileMgr.getLocalFile("xml", "promotions.bkup"));
			if (fileBackup.exists()) {
				fileBackup.renameTo(filePromotion);
			} else { // no files, so delete date from last item download
				File fileDate = new File(FileMgr.getLocalFile("repository", "PROMOTION_DOWNLOAD_DATE"));
				fileDate.delete();
			}
		}
	}

	/**
	 * Method used to check threshold promotion.xml file existance
	 */
	public void checkThresholdPromotionFile() {
		// check to make sure that threshold_promotions.xml exists and its not in the backup
		File filePromotion = new File(FileMgr.getLocalFile("xml", "threshold_promotions.xml"));
		if (!filePromotion.exists()) {
			File fileBackup = new File(FileMgr.getLocalFile("xml", "threshold_promotions.bkup"));
			if (fileBackup.exists()) {
				fileBackup.renameTo(filePromotion);
			} else { // no files, so delete date from last item download
				File fileDate = new File(FileMgr.getLocalFile("repository", "THRESHOLD_PROMOTION_DOWNLOAD_DATE"));
				fileDate.delete();
			}
		}
	}

	/**
	 * Method reads promotion.cfg and downloads promotion.xml file at client site
	 * @throws Exception
	 */
	private void downloadPromotionFile() throws Exception {
		try {
			File backup = new File(FileMgr.getLocalFile("xml", "promotions.bkup"));
			backup.delete();
			File promotionFile = new File(FileMgr.getLocalFile("xml", "promotions.xml"));
			promotionFile.renameTo(backup);
			promotionFile.delete();
			ConfigMgr config = new ConfigMgr("promotion.cfg");
			CMSPromotionServices promotionDownloadServices = (CMSPromotionServices) config.getObject("CLIENT_DOWNLOAD_IMPL");
			bootMgr.setBootStrapStatus("Downloading promotion file");
			IPromotion[] promotions = promotionDownloadServices.findAllForStore((Store) theMgr.getGlobalObject("STORE"));
			CMSPromotion[] objects = null;
			if (promotions == null || promotions.length == 0) {
				objects = new CMSPromotion[0];
			} else {
				objects = new CMSPromotion[promotions.length];
				for (int i = 0; i < objects.length; i++){
					objects[i] = (CMSPromotion) promotions[i];
				}
			}

			//Issue #1457: filter out expired promotions for Japan
			//if (((Store)theMgr.getGlobalObject("STORE")).getPreferredISOCountry().equals("JP")) {
			if (true) {
				Vector activePromotionsVec = new Vector();
				for (int i = 0; i < promotions.length; i++) {
				//Anjana added null check
					if(promotions[i]!=null){					
					if (((CMSPromotion) promotions[i]).getEndTime().before(Calendar.getInstance()))
						continue;
					//else
					activePromotionsVec.add(promotions[i]);
				}}
				promotions = (IPromotion[]) activePromotionsVec.toArray(new CMSPromotion[activePromotionsVec.size()]);
			}

			new PromotionXML().writToFile(promotionFile.getAbsolutePath(), promotions);
			theMgr.addGlobalObject("PROMOTION_DOWNLOAD_DATE", new java.util.Date(), true);
			//load promotions
			System.out.println("******** Loading promotions from Bootstrap ******** ");
			Hashtable hTablePromotions = new Hashtable();
			for (int i = 0; i < promotions.length; i++) {
				hTablePromotions.put(((CMSPromotion) promotions[i]).getRuleDrvId(), promotions[i]);
			}
			CMSPromotionBasedPriceEngine.setPromotions(hTablePromotions);
			System.out.println("******** Promotions loaded successfully = " + promotions.length + " ******** ");
		} catch (Exception ex) {
			try {
				File promotionFile = new File(FileMgr.getLocalFile("xml", "promotions.xml"));
				promotionFile.delete();
				File backup = new File(FileMgr.getLocalFile("xml", "promotions.bkup"));
				backup.renameTo(promotionFile);
			} catch (Exception ex1) {
			}
			System.out.println("Exception downloadFile()->" + ex);
			ex.printStackTrace();
		} finally {
			if (theMgr instanceof IApplicationManager)
				((IApplicationManager) theMgr).closeStatusDlg();
		}
	}

	/**
	 * Method reads promotion.cfg and downloads threshold promotion.xml file at client site
	 * @throws Exception
	 */
	private void downloadThresholdPromotionFile() throws Exception {
		try {
			File backup = new File(FileMgr.getLocalFile("xml", "threshold_promotions.bkup"));
			backup.delete();
			File promotionFile = new File(FileMgr.getLocalFile("xml", "threshold_promotions.xml"));
			promotionFile.renameTo(backup);
			promotionFile.delete();
			ConfigMgr config = new ConfigMgr("promotion.cfg");
			CMSPromotionServices promotionDownloadServices = (CMSPromotionServices) config.getObject("CLIENT_DOWNLOAD_IMPL");
			bootMgr.setBootStrapStatus("Downloading threshold promotion file");
			ThresholdPromotion[] promotions = promotionDownloadServices.findThresholdPromotionsForStore((Store) theMgr.getGlobalObject("STORE"));
			CMSThresholdPromotion[] objects = null;
			if (promotions == null || promotions.length == 0) {
				objects = new CMSThresholdPromotion[0];
			} else {
				objects = new CMSThresholdPromotion[promotions.length];
				for (int i = 0; i < objects.length; i++)
					objects[i] = (CMSThresholdPromotion) promotions[i];
			}
			new ThresholdPromotionXML().writToFile(promotionFile.getAbsolutePath(), promotions);
			theMgr.addGlobalObject("THRESHOLD_PROMOTION_DOWNLOAD_DATE", new java.util.Date(), true);
		} catch (Exception ex) {
			try {
				File promotionFile = new File(FileMgr.getLocalFile("xml", "threshold_promotions.xml"));
				promotionFile.delete();
				File backup = new File(FileMgr.getLocalFile("xml", "threshold_promotions.bkup"));
				backup.renameTo(promotionFile);
			} catch (Exception ex1) {
			}
			System.out.println("Exception downloadFile()->" + ex);
			ex.printStackTrace();
		} finally {
			if (theMgr instanceof IApplicationManager)
				((IApplicationManager) theMgr).closeStatusDlg();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void loadPromotions() {
		Hashtable hTablePromotions = new Hashtable();
		System.out.println("******** Loading promotions from Bootstrap ******** ");
		ConfigMgr config = new ConfigMgr("promotion.cfg");
		PromotionServices promotionServices = (PromotionServices) config.getObject("CLIENT_IMPL");
		try {
			IPromotion[] promotionArray = promotionServices.findAllForStore(null);
			System.out.println("******** PromotionArray size is  ******** " +promotionArray.length);
			hTablePromotions = new Hashtable();
			for (int i = 0; i < promotionArray.length; i++) {
				if (promotionArray[i] instanceof ItemThresholdPromotion) {
				}
				hTablePromotions.put(((CMSPromotion) promotionArray[i]).getRuleDrvId(), promotionArray[i]);
				// added by deepika
				//hTablePromotions.put(((CMSPromotion) promotionArray[i]).getPromotionNum(), promotionArray[i]);
			}
			CMSPromotionBasedPriceEngine.setPromotions(hTablePromotions);
			System.out.println("******** Promotions loaded successfully = " + promotionArray.length + " ******** ");
		} catch (Exception exp) {
			LoggingServices.getCurrent().logMsg("PromotionBootStrap", "static", "" + exp, "See exception.", LoggingServices.CRITICAL);
			System.out.println("Excpetion --> " + exp);
			exp.printStackTrace();
		}
	}
//	# 1913 Download timing to register
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
