package com.ga.fs.fsbridge.fiscaldocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.armani.utils.FileUtil;
import com.chelseasystems.cr.config.ConfigMgr;

public class FSFiscalDocumentsCopiesTracker {

	public static ConfigMgr fiscal_document_cfg = new ConfigMgr(
			"fiscal_document.cfg");
	public static File tracker_global = new File(
			fiscal_document_cfg.getString("FISCAL_DOCUMENT_PATH")
					+ "\\fiscal_copies_tracker.properties");
	public static File tracker_local = new File(
			"../files/prod/javapos/armani/conf/_FSBridgeFiscalFiles/fiscal_copies_tracker.properties");
	public static File tracker_local_folder = new File(
	"../files/prod/javapos/armani/conf/_FSBridgeFiscalFiles");
	
	public static int COPIES = -1;
	public static String DOCNUM = null;

	public static String getCurrentCopies(String docNumber) {
		
		if (!tracker_global.exists()) {

			boolean genResult = generateGlobalTrackerFromLocalTracker();
			if (genResult == false) {
				try {
					tracker_global.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return " ";
				}
			}

		}

		Properties prop = new Properties();
		FileInputStream input = null;
		DOCNUM = docNumber;
		
		try {
			input = new FileInputStream(tracker_global);
			prop.load(input);
			Object[] keys = prop.keySet().toArray();
			if(keys.length==0){
				String copies = "0";
				COPIES = Integer.parseInt(copies);
				return copies;	
			}
			boolean containsEntry = false;
			for(Object key : keys){
				if(((String)key).equals(docNumber)){
					containsEntry = true;
				}
			}
			if (containsEntry == false) {
				input.close();
				String copies = "0";
				COPIES = Integer.parseInt(copies);
				return copies;	
			}
			

			String currentCopies = prop.getProperty(docNumber);
			COPIES = Integer.parseInt(currentCopies);
			input.close();
			return currentCopies;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				input.close();
				return " ";
			} catch (IOException e1) {
				e1.printStackTrace();
				return " ";
			}
			
		} catch (Exception e) {
			try {
				input.close();
				return " ";
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return " ";
		}

		
	}

	public static boolean storeCopiesCount() {

		if (!tracker_global.exists()) {

			boolean genResult = generateGlobalTrackerFromLocalTracker();
			if (genResult == false) {
				try {
					throw new Exception();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

		}

		Properties prop = new Properties();
		FileOutputStream output = null;
		

		try {
			FileInputStream input = new FileInputStream(tracker_global.getPath());
			prop.load(input);
			
			Set keys = prop.keySet();

			String newvalue = String.valueOf(COPIES + 1);
			HashMap<String,String> map = new HashMap<String,String>();
			
			for(Object key : keys){
				String value = prop.getProperty((String)key);
				map.put((String)key, value);
			}
			map.put(DOCNUM, newvalue);
			input.close();
			
			Iterator iterator = map.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Map.Entry pair = (Map.Entry)iterator.next();
				
				prop.setProperty((String)pair.getKey(), (String)pair.getValue());
				
				iterator.remove();
			}
			output = new FileOutputStream(tracker_global.getPath());
			prop.store(output, "");
			
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}finally{
			try {
				output.close();
				storeUpdatedTracker();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
	}

	public static boolean generateGlobalTrackerFromLocalTracker() {
		if (!tracker_local.exists()) {
			System.out.println("["
					+ FSFiscalDocumentsCopiesTracker.class.getSimpleName()
					+ "]" + " Unable to generate Global Tracker!"
					+ "No local tracker available");
			return false;
		} else {
			try {
				FileUtil.copyFile(tracker_local, tracker_global);
				return true;
			} catch (Exception e) {
				System.out.println("["
						+ FSFiscalDocumentsCopiesTracker.class.getSimpleName()
						+ "]" + " Unable to generate Global Tracker!");
				e.printStackTrace();
				return false;
			}
		}
	}

	public static boolean storeUpdatedTracker() {
		try {
			if(!tracker_local_folder.exists()){
				tracker_local_folder.mkdirs();
			}
			FileUtil.copyFile(tracker_global, tracker_local);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
