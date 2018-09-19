/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	2/4/05	        		                        Base
 * 2	2/4/05	        KS	POS_IS_ItemDownload_Rev1	CMSItem
 * 3    4/18/05         KS      Changes the item file to         Enhancement
 *                              regidstoreID_items.dat
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cs.ajbauthorization.AJBServiceManager;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.DueBill;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.logging.CMSLoggingFileServices;
import com.chelseasystems.cr.logging.LoggingFileServices;



/**
 *
 * <p>Title: CMSItemJDBCServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public class CMSItemJDBCServices extends CMSItemServices {
	private ItemDAO itemDAO;
	boolean localTestMode = false;
	boolean verboseMode = false; 
	private static final int CHARACTERS = 2;
	/**
	 * Default constructor
	 */
	public CMSItemJDBCServices() {
		ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
		ConfigMgr config = new ConfigMgr("credit_auth.cfg");
		itemDAO = (ItemDAO) configMgr.getObject("ITEM_DAO");
		// itemVwDAO = (ItemVwDAO)configMgr.getObject("ITEMVIEW_DAO");
		config = new ConfigMgr("credit_auth.cfg");
		String strLocalTestMode = config.getString("LOCAL_TEST_MODE");
		String strVerboseMode = config.getString("VERBOSE_MODE");
		if (strLocalTestMode.trim().equalsIgnoreCase("TRUE")) {
			localTestMode = true;
		}
		if (strVerboseMode.trim().equalsIgnoreCase("TRUE")) {
			verboseMode = true;
		}
	}

	/**
	 * This method is used to search an item by its item id
	 * 
	 * @param id
	 *            String
	 * @return Item
	 * @throws Exception
	 */
	public Item findById(String id) throws java.lang.Exception {
		try {
			return itemDAO.selectById(id, null); // currency type = null
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to search an item by its bar code for a given store
	 * 
	 * @param barCode
	 *            String
	 * @param storeId
	 *            String
	 * @return CMSItem
	 * @throws Exception
	 */
	public CMSItem findByBarCode(String barCode, String storeId) throws java.lang.Exception {
		try {
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				return itemDAO.selectByBarcode(barCode, storeId); // currency type = null
			} else {
				return itemDAO.selectByIDOrBarcode(barCode, storeId); // currency type = null
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}
	
	/**
	 * Added by Anjana to look up the SAP item.
	 * @param barcode String
	 * @param storeId String
	 * @param itemIdLength String
	 * @throws Exception
	 * @return String
	 */
	public CMSItem findSAPBarCode(String barCode, String storeId, String itemIdLength) throws java.lang.Exception {
		try {
			return itemDAO.findSAPBarCode(barCode, storeId , itemIdLength);
			} catch (Exception exception) {
			exception.printStackTrace();
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findSAPBarCode", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}
	
	 

	/**
	 * This method reads the jdbc.cfg and item.cfg and download items file at client site
	 * 
	 * @param fileName
	 *            String
	 * @param storeId
	 *            String
	 * @throws Exception
	 * @return byte[]
	 */
	public byte[] getItemFile(String fileName, String storeId, Date date) throws java.lang.Exception {
		ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
		// dbUSer name
		String dbUser = (String) configMgr.getString("USER_NAME");
		// db password
		String dbPassword = (String) configMgr.getString("PASSWORD");
		// db name
		String dbName = (String) configMgr.getString("DB_NAME");
		// db server ip
		String dbIP = (String) configMgr.getString("DB_IP");
		configMgr = new ConfigMgr("item.cfg");
		// Item download dir
		String downloadDir = configMgr.getString("ITEM_DATA_DIR");
		downloadDir = downloadDir + configMgr.getString("ITEM_DATA_CLIENT_DIR");
		// Logfile
		String logDir = logDir = "../files/prod/" + (configMgr.getString("ITEM_LOG_DIR"));
		// Download script name
		String scriptName = configMgr.getString("ITEM_DOWNLOAD_SCRIPT");
		//
		// File zipFile = new File(FileMgr.getLocalFile(downloadDir, storeId+"_items.zip"));
		File zipFile = new File(downloadDir + File.separator + fileName + ".zip");
		LoggingFileServices loggingFileServ = (LoggingFileServices) LoggingServices.getCurrent();
		CMSLoggingFileServices logserv = new CMSLoggingFileServices();
		loggingFileServ.setCurrent(logserv);
		CMSLoggingFileServices loggingFileServices = (CMSLoggingFileServices) loggingFileServ.getCurrent();
		String prevLogFile = loggingFileServices.getLogFile();
		loggingFileServices.setLogFile(logDir);
		try {
			String sDate = configMgr.getString("INITIAL_FROM_DATE");
			String dateFormat = configMgr.getString("FROM_DATE_FORMAT");
			if (date != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				sDate = sdf.format(date);
			}
			String dateTag = (new SimpleDateFormat("yyMMddHHmmss")).format(Calendar.getInstance().getTime());
			// params: scripname, dbUser, dbPassword, dbname, fileName, dbIp, storeID, dironAppserver, fromDate(DD-MON-YYYY, dateTag)
			String params[] = { scriptName, dbUser, dbPassword, dbName, fileName, dbIP, storeId, downloadDir, sDate, dateTag };
			System.out.println(">>>>> Start Item Download script param <<<<<<<");
			for (int i = 0; i < params.length; i++) {
				System.out.println("  " + params[i]);
			}
			System.out.println(">>>>> End Item Download script param <<<<<<<");
			Process p = Runtime.getRuntime().exec(params);
			InputStream stderr = p.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			loggingFileServices.logMsg("Begin shell script: ");
			loggingFileServices.recordMsg();
			System.out.println("<Message from shell script>");
			while((line = br.readLine()) != null) {
				System.out.println(line);
				loggingFileServices.logMsg(line);
				loggingFileServices.recordMsg();
			}
			int exitVal = p.waitFor();
			System.out.println("</exitVal > " + exitVal);
			loggingFileServices.logMsg("exit value from shell script: " + exitVal);
			loggingFileServices.recordMsg();
			loggingFileServices.logMsg("End shell script: ");
			loggingFileServices.recordMsg();
			System.out.println("</End message from shell script>");
			if (exitVal != 0)
				throw new Exception();
			byte[] bytes;
			if (zipFile.exists()) {
				long fileLength = zipFile.length();
				bytes = new byte[(int) fileLength];
				FileInputStream fin = new FileInputStream(zipFile);
				// Read in the bytes
				int offset = 0;
				int numRead = 0;
				while(offset < bytes.length && (numRead = fin.read(bytes, offset, bytes.length - offset)) >= 0) {
					offset += numRead;
				}
				// Ensure all the bytes have been read in
				if (offset < bytes.length) {
					throw new IOException("Could not completely read file " + zipFile.getName());
				}
				fin.close();
				if (true) {
					if (zipFile.exists()) {
						zipFile.delete();
					}
				}
				loggingFileServices.logMsg("Total bytes sent to the Client: " + bytes.length);
				loggingFileServices.recordMsg();
				return bytes;
			}
		} catch (Exception ex) {
			if (true) {
				if (zipFile.exists()) {
					zipFile.delete();
				}
			}
			loggingFileServices.logMsg(this.getClass().getName(), ex);
			loggingFileServices.recordMsg();
		} finally {
			loggingFileServices.setLogFile(prevLogFile);
		}
		return null;
	}

	/**
	 * This method is used to find all the items
	 * 
	 * @return Item[]
	 * @throws Exception
	 */
	public Item[] findAllItems() throws java.lang.Exception {
		try {
			return itemDAO.selectAll(null);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAllItems", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to find all the items corresponding to a given store
	 * 
	 * @param store
	 * @return
	 * @exception java.lang.Exception
	 */
	public Item[] findAllItemsForStore(Store store) throws java.lang.Exception {
		try {
			return itemDAO.selectAll(store.getCurrencyType().getCode());
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAllItemsForStore", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to find items by its description
	 * 
	 * @param description
	 * @return
	 * @exception java.lang.Exception
	 */
	public String[] findIDListByDescription(String description) throws java.lang.Exception {
		try {
			if (description == null || description.length() == 0)
				return new String[0];
			Item[] items = itemDAO.selectByDescription(description, null); // currency type = null
			String[] ids = new String[items.length];
			for (int i = 0; i < items.length; i++)
				ids[i] = items[i].getId();
			return ids;
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findIDListByDescription", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to find items by its description for a given store
	 * 
	 * @param description
	 * @return
	 * @exception java.lang.Exception
	 */
	public String[] findIDListByDescription(String description, String storeId) throws java.lang.Exception {
		try {
			if ((description == null || description.length() == 0) && (storeId == null || storeId.length() == 0))
				return new String[0];
			// Item[] items = itemDAO.selectByDescription(description, null); //currency type = null
			CMSItem[] items = itemDAO.selectByDescriptionAndStoreId(description, storeId);
			if (items != null) {
				String[] ids = new String[items.length];
				for (int i = 0; i < items.length; i++)
					ids[i] = items[i].getBarCode();
				return ids;
			}
			return new String[0];
		} catch (Exception exception) {
			exception.printStackTrace();
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findIDListByDescription", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to update an Item, specified by its Id, with a new price, which is specified by the amount.
	 * 
	 * @param itemId
	 * @param amount
	 * @return
	 * @exception Exception
	 */
	public boolean updateItemPrice(String itemId, ArmCurrency amount) throws Exception {
		return false;
	}

	/**
	 * @param store
	 *            Store
	 * @throws Exception
	 * @return Map
	 */
	public Map getSupplierSeasonYear(Store store) throws java.lang.Exception {
		try {
			return itemDAO.getSupplierSeasonYear(store);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getSupplierSeasonYear", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to search items on the basis of specified search parameter
	 * 
	 * @param searchString
	 *            ItemSearchString
	 * @throws Exception
	 * @return Item[]
	 */
	public Item[] findItems(ItemSearchString searchString) throws java.lang.Exception {
		try {
			return itemDAO.selectByItemSearchString(searchString);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findItems", "Exception", "See Exception", LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	/**
	 * This method is used to find items with zero unit price
	 * 
	 * @return CMSItem[]
	 * @throws Exception
	 */
	public CMSItem[] findItemsWithNoUnitPrice() throws Exception {
		return null;
	}

	
	/**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws Exception
	 * @return String
	 */
	public String selectItemIdFromAsItm(String barcode)
			throws Exception {
	  try {
		   String itemId = (String) (itemDAO.selectItemIdFromAsItm(barcode));
		    return (String)itemId;
	      } catch (Exception exception) {
	      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "selectItemIdFromAsItm"
	          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
	      exception.printStackTrace();
	      throw exception;
	    }
	}
	

	/**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws Exception
	 * @return String
	 */
	public String selectItemFromAsItm(String itemId)
			throws Exception {
	  try {
		   String itemId1 = (String) (itemDAO.selectItemFromAsItm(itemId));
		    return (String)itemId1;
	      } catch (Exception exception) {
	      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "FindItemFromAsItm"
	          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
	      exception.printStackTrace();
	      throw exception;
	    }
	}
		

	/**
	 * Added by Satin.
	 * This method is used to insert a record into AS_ITM_RTL_STR for exception item.
	 * @param theAppMgr IRepositoryManager
	 * @param storeId String
	 * @param itemId String
	 * @param retailPrice ArmCurrency
	 * @param currencyCode String
	 * @param vatRate Double
	 * @param taxable String
	 * @return void
	 * @throws Exception
	 */
	public void insertIntoAsItmRtlStr(String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable)
		      throws Exception {
		    try {
		    	itemDAO.insertExceptionItemtoAsItmRtlStr(storeId, itemId, retailPrice, currencyCode, itemDesc, vatRate, taxable);
		    } catch (Exception exception) {
		      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "InsertExceptionItemToAsItemRtlStr", "Exception"
		          , "See Exception", LoggingServices.MAJOR, exception);
			      throw exception;
		    }
		  }


			
}
