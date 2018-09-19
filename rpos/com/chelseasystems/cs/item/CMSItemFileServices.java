/**
 * Title:        <p>CMSItemFileServices
 * Description:  <p>CMSItemFileServices
 * Copyright:    Copyright (c) <p>2001
 * Company:      <p>Chelsea Store
 * @author David Fung
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.xml.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: CMSItemFileServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSItemFileServices extends CMSItemServices {
	static private Hashtable allItems = null;
	static private String fileName = FileMgr.getLocalFile("xml", "items.xml");

	/**
	 * Default Constructor
	 */
	public CMSItemFileServices() {
	}

	/**
	 * This method is used to find an item on the basis of item id
	 * @param id
	 *            String
	 * @return Item
	 * @throws Exception
	 */
	public Item findById(String id) throws java.lang.Exception {
		if (allItems == null || allItems.size() == 0)
			loadAllItemsFromFile();
		if (allItems == null || allItems.size() == 0)
			return null;
		return (CMSItem) allItems.get(id);
	}

	/**
	 * This method is used to find an item on the basis of id for a specific store
	 * @param id
	 *            String
	 * @param storeId
	 *            String
	 * @throws Exception
	 * @return CMSItem
	 */
	public CMSItem findByBarCode(String id, String storeId) throws java.lang.Exception {
		return null;
	}
	
	/**
	 * Added by Anjana to return the correct SAP item.
	 * @param itemId String
	 * @param storeId String
	 * @param itemIdLength String
	 * @throws Exception
	 * @return String
	 */
	public CMSItem findSAPBarCode(String id, String storeId, String itemIdLength) throws java.lang.Exception {
		return null;
	}
	
	/**
	 * This method download items dat file at client site
	 * @param fileName
	 *            String
	 * @param storeId
	 *            String
	 * @throws Exception
	 * @return byte[]
	 */
	public byte[] getItemFile(String fileName, String storeId, Date d) throws java.lang.Exception {
		return new byte[0];
	}

	/**
	 * This method is used to get an item on the basis of bar code.
	 * 
	 * @param aStore
	 *            the store requesting the item
	 */
	public CMSItem findByBarCode(String BarCode) throws java.lang.Exception {
		if (allItems == null || allItems.size() == 0)
			loadAllItemsFromFile();
		if (allItems == null || allItems.size() == 0)
			return null;
		return (CMSItem) allItems.get(BarCode);
	}

	/**
	 * This method is used to get all the items
	 * @return Item[]
	 * @throws Exception
	 */
	public Item[] findAllItems() throws java.lang.Exception {
		if (allItems == null || allItems.size() == 0)
			loadAllItemsFromFile();
		if (allItems == null || allItems.size() == 0)
			return null;
		return (CMSItem[]) allItems.values().toArray(new CMSItem[allItems.size()]);
	}

	/**
	 * This method is used to find all items for a store
	 * @param aStore
	 *            Store
	 * @return Item[]
	 * @throws Exception
	 */
	public Item[] findAllItemsForStore(Store aStore) throws java.lang.Exception {
		return findAllItems();
	}

	/**
	 * This method is used to find item ids by its description for a store
	 * @param description
	 *            String
	 * @param storeId
	 *            String
	 * @return String[]
	 * @throws Exception
	 */
	public String[] findIDListByDescription(String description, String storeId) throws java.lang.Exception {
		return null;
	}

	/**
	 * This method will return an array of item ids whose description at least partially matches the submitted description
	 * 
	 * @param description
	 *            String
	 * @return String[]
	 * @throws Exception
	 */
	public String[] findIDListByDescription(String description) throws java.lang.Exception {
		if (allItems == null || allItems.size() == 0)
			loadAllItemsFromFile();
		if (allItems == null || allItems.size() == 0)
			return new String[0];
		if (description == null || description.length() == 0)
			return new String[0];
		Vector itemVector = new Vector();
		for (Enumeration enm = allItems.elements(); enm.hasMoreElements();) {
			CMSItem item = (CMSItem) enm.nextElement();
			if (item.getDescription().toUpperCase().indexOf(description) > -1)
				itemVector.add(item.getId());
		}
		if (itemVector.size() == 0)
			return new String[0];
		String[] itemArray = new String[itemVector.size()];
		Iterator r = itemVector.iterator();
		int i = 0;
		while(r.hasNext()) {
			itemArray[i++] = (String) r.next();
		}
		return itemArray;
	}

	/**
	 * This method is used to update an Item, specified by its Id, with a new price, which is specified by the amount. The change will be persisted
	 * after the vector of items is updated.
	 * 
	 * @param itemId
	 *            String
	 * @param amount
	 *            Currency
	 * @return boolean
	 * @throws Exception
	 */
	public boolean updateItemPrice(String itemId, ArmCurrency amount) throws Exception {
		Item[] items = findAllItems();
		if (items != null) {
			int index = Arrays.binarySearch(items, new CMSItem(itemId));
			if (index > -1) {
				// apply change
				items[index].doSetRetailPrice(amount);
				// backup file
				File backup = new File(FileMgr.getLocalFile("xml", "items.bkup"));
				backup.delete();
				File itemFile = new File(FileMgr.getLocalFile("xml", "items.xml"));
				itemFile.renameTo(backup);
				itemFile.delete();
				// write new file
				String itemXml = new ItemXML().toXML(items);
				FileWriter itemFileWriter = new FileWriter(itemFile);
				itemFileWriter.write(itemXml);
				itemFileWriter.close();
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to load all the items from the file
	 */
	private void loadAllItemsFromFile() {
		long begin = new java.util.Date().getTime();
		if (fileName == null || fileName.equals("")) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "CMSItemFileServices()", "Missing xml data file name.", "Make sure the item data file is there.", LoggingServices.CRITICAL);
			System.exit(1);
		}
		try {
			allItems = new Hashtable();
			Vector list = (new ItemXML()).toObjects(fileName);
			for (Enumeration enm = list.elements(); enm.hasMoreElements();) {
				CMSItem item = (CMSItem) enm.nextElement();
				allItems.put(item.getId(), item);
			}
		} catch (org.xml.sax.SAXException saxException) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllItemFromFile()", "Cannot parse the item data file.", "Verify the integrity of the item data file",
					LoggingServices.CRITICAL);
			System.exit(1);
		} catch (IOException e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllItemFromFile()", "Cannot process the item data file.", "Verify the integrity of the item data file",
					LoggingServices.CRITICAL);
			System.exit(1);
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllItemFromFile()", "Cannot process the item data file.", "Unknow exception: " + e, LoggingServices.CRITICAL);
			System.exit(1);
		}
		long end = new java.util.Date().getTime();
		System.out.println("Number of items loaded: " + allItems.size() + " (" + (end - begin) + "ms)");
	}

	/**
	 * @param store
	 *            Store
	 * @throws Exception
	 * @return Map
	 */
	public Map getSupplierSeasonYear(Store store) throws Exception {
		return null;
	}

	/**
	 * This method is used to search item on the basis of specified search parameter
	 * 
	 * @param searchString
	 *            ItemSearchString
	 * @throws Exception
	 * @return Item[]
	 */
	public Item[] findItems(ItemSearchString searchString) throws Exception {
		return null;
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
		return null;
	}
	
	
	/**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws Exception
	 * @return String
	 */
	public String selectItemFromAsItm(String itemId)
			throws Exception {
		return null;
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
			throws Exception {}


	
}
