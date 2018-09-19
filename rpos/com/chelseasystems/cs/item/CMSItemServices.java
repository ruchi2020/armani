/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	2/4/05	        KS		                        Base
 * 2	2/8/05	        KS	POS_IS_ItemDownload_Rev1	CMSItemServices
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

//commented for exception item
//import com.chelseasystems.cr.item.ItemServices;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.store.Store;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
//added for exception item
import com.chelseasystems.cs.item.ItemServices;



/**
 *
 * <p>Title: CMSItemServices</p>
 *
 * <p>Description: This is an abstract extension of ItemServices</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public abstract class CMSItemServices extends ItemServices {

  /**
   * This method is used to search an item by its bar code corresponding to
   * a given store
   * @param barCode String
   * @throws Exception
   * @return CMSItem[]
   */
  public abstract CMSItem findByBarCode(String barCode, String StoreId)
      throws Exception;
  
  /**
   *Added by Anjana to fetch the SAP item
   * @param barCode String
   *@param storeId String
   * @param itemIdLength String
   * @throws Exception
   * @return CMSItem
   */
  public abstract CMSItem findSAPBarCode(String barcode , String storeId , String itemIdLength)
	  	  throws Exception;  
  

  /**
   * This method is used to download items file at client site
   * @param fileName String
   * @param storeID String
   * @throws Exception
   * @return byte[]
   */
  public abstract byte[] getItemFile(String fileName, String storeID, Date date)
      throws Exception;

  /**
   * This method is used to find items by its description corresponding to
   * a given store
   * @param fileName String
   * @param storeID String
   * @throws Exception
   * @return byte[]
   */
  public abstract String[] findIDListByDescription(String description, String storeId)
      throws Exception;

  /**
   *
   * @param store Store
   * @throws Exception
   * @return Map
   */
  public abstract Map getSupplierSeasonYear(Store store)
      throws Exception;

  /**
   * This method is used to search items on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws Exception
   * @return Item[]
   */
  public abstract Item[] findItems(ItemSearchString searchString)
      throws Exception;
  
  /**
   * This method is used to find items with zero unit price
   * @return CMSItem[]
   * @throws Exception
   */
  public abstract CMSItem[] findItemsWithNoUnitPrice()
  	  throws Exception;
  
  /**
   * Added by Satin to search an itemId corresponding to entered barcode for exception item.
   * @param barcode String
   * @throws Exception
   * @return String
   */
  public abstract String selectItemIdFromAsItm(String barcode)
		   	 throws Exception;
  
  
  /**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws Exception
	 * @return String
	 */
  public abstract String selectItemFromAsItm(String itemId)
		   	 throws Exception;
  
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
   public abstract void insertIntoAsItmRtlStr(String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable)
		   	 throws Exception;

}

