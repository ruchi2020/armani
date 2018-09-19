/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	2/4/05	        		                        Base
 * 2	2/4/05	        KS	POS_IS_ItemDownload_Rev1	CMSItem
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import java.rmi.*;
import java.sql.SQLException;

import com.igray.naming.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.register.CMSRegister;

import java.util.Map;
import java.util.Date;


/**
 *
 * <p>Title: ICMSItemRMIServer</p>
 *
 * <p>Description: This interface defines the customer services that are
 * available remotely via RMI </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public interface ICMSItemRMIServer extends Remote, IPing {

  /**
   * This method is used to search an item by its item id
   * @param id String
   * @return CMSItem
   * @throws RemoteException
   */
  public CMSItem findById(String id)
      throws RemoteException;


  /**
   * This method is used to download items file at client site
   * @param fileName String
   * @param storeID String
   * @param d Date
   * @return byte[]
   * @throws RemoteException
   */
  public byte[] getItemFile(String fileName, String storeID, Date d)
      throws RemoteException;


  /**
   * This method is used to search an item by its bar code corresponding to
   * a given store
   * @param barCode String
   * @param storeId String
   * @return CMSItem
   * @throws RemoteException
   */
  public CMSItem findByBarCode(String barCode, String storeId)
      throws RemoteException;

  /**
   * Added by Anjana to fetch SAP item
   * @param barCode String
   * @param storeId String
   * @param itemIdLength String
   * @return CMSItem
   * @throws RemoteException
   */
  public CMSItem findSAPBarCode(String barCode, String storeId, String itemIdLength)
	      throws RemoteException;
  
  /**
   * This method is used to find items by its description corresponding to
   * a given store
   * @param description String
   * @param storeId String
   * @return String[]
   * @throws RemoteException
   */
  public String[] findIDListByDescription(String description, String storeId)
      throws RemoteException;


  /**
   * This method is used to find items by its description
   * @param description String
   * @return String[]
   * @throws RemoteException
   */
  public String[] findIDListByDescription(String description)
      throws RemoteException;


  /**
   * This method is used to return all items
   * @return CMSItem[]
   * @throws RemoteException
   */
  public CMSItem[] findAllItems()
      throws RemoteException;


  /**
   * This method is used to find all the items corresponding to a given store
   * @param store Store
   * @return CMSItem[]
   * @throws RemoteException
   */
  public CMSItem[] findAllItemsForStore(Store store)
      throws RemoteException;


  /**
   * This method is used to update an Item, specified by its Id, with a new price,
   * which is specified by the amount. Only local services need to implement
   * this method.
   * @param itemId String
   * @param amount Currency
   * @return boolean
   * @throws RemoteException
   */
  public boolean updateItemPrice(String itemId, ArmCurrency amount)
      throws RemoteException;


  /**
   *
   * @param store Store
   * @throws RemoteException
   * @return Map
   */
  public Map getSupplierSeasonYear(Store store)
      throws RemoteException;


  /**
   * This method is used to search items on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws RemoteException
   * @return Item[]
   */
  public Item[] findItems(ItemSearchString searchString)
      throws RemoteException;
  
  /**
   * This method is used to find items with zero unit price
   * @return CMSItem[]
   * @throws RemoteException
   */
  public CMSItem[] findItemsWithNoUnitPrice()
      throws RemoteException;


  /**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws RemoteException
	 * @return String
	 */
  public String selectItemIdFromAsItm(String barcode)
	  throws RemoteException;
  
  
  /**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws RemoteException
	 * @return String
	 */
  public String selectItemFromAsItm(String itemId)
		  throws RemoteException;
  
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
	 * @throws RemoteException
	 */
    public void insertIntoAsItmRtlStr(String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable)
			throws RemoteException;

}

