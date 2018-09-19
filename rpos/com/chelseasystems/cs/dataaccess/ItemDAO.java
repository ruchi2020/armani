/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/**
 * Description:retek Item View
 * Created By:Khyati Shah
 * Created Date:2/4/2005
 */
/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 4	4/19/05	        RP	Item lookup specs	        Implementation
 * 1	2/4/05	        KS	POS_IS_ItemDownload_Rev1	RKItemDAO
 *
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cr.item.*;
import  java.sql.*;
import  java.util.Map;
import  com.chelseasystems.cs.item.*;
import  com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.currency.ArmCurrency;
import  com.chelseasystems.cr.database.ParametricStatement;


public interface ItemDAO extends BaseDAO
{

  /**
   * @param item
   * @exception SQLException
   */
  public void insert (Item item) throws SQLException;



  /**
   * @param id
   * @param currencyCode
   * @return
   * @exception SQLException
   */
  public Item selectById (String id, String currencyCode) throws SQLException;



  /**
   * @param description
   * @param currencyCode
   * @return
   * @exception SQLException
   */
  public Item[] selectByDescription (String description, String currencyCode) throws SQLException;



  /**
   * @param currencyCode
   * @return
   * @exception SQLException
   */
  public Item[] selectAll (String currencyCode) throws SQLException;



  /**
   * @param description
   * @param storeId
   * @return
   * @exception SQLException
   */
  public CMSItem[] selectByDescriptionAndStoreId (String description, String storeId) throws SQLException;



  /**
   *
   * @param barcode String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem[]
   */
  public CMSItem selectByBarcode (String barcode, String storeId) throws SQLException;

  /**
  *Added by Anjana to Fetch SAP item
  * @param barcode String
  * @param storeId String
  * @throws SQLException
  * @return CMSItem
  */
  public CMSItem findSAPBarCode (String barcode, String storeId , String itemIdLength) throws SQLException;
  

  /**
   *
   * @param store Store
   * @throws SQLException
   * @return Map
   */
  public Map getSupplierSeasonYear (Store store) throws SQLException;



  /**
   *
   * @param searchString ItemSearchString
   * @throws SQLException
   * @return CMSItem[]
   */
  public CMSItem[] selectByItemSearchString (ItemSearchString searchString) throws SQLException;



  /**
   * @param barcode
   * @param storeId
   * @return
   * @exception SQLException
   */
  public CMSItem selectByIDOrBarcode (String barcode, String storeId) throws SQLException;
  
  /**
   * Inserts CMSItem object into AS_ITM_RTL_STR table
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQLForItemRetailStore (CMSItem object, String storeId) 
	throws SQLException;
  
  /**
   * Inserts CMSItem object into ARM_ITM_HIST table
   * @param object
   * @return
   * @exception SQLException
   */
//  public ParametricStatement[] getInsertSQLForItemHistory (CMSItem object, String storeId) 
//  	throws SQLException;
  
  /**
   * Updates CMSItem object into AS_ITM_RTL_STR table
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQLForItemRetailStore (CMSItem object, String storeId) 
  	throws SQLException;
  
  /**  
   * @param itemId String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem
   */
  public CMSItem selectByItemIdAndStoreId (String itemId, String storeId) throws SQLException;


  /**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws SQLException
	 * @return String
	 */
  public String selectItemIdFromAsItm(String barcode) throws SQLException;
  
  /**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws SQLException
	 * @return String
	 */
  public String selectItemFromAsItm(String itemId) throws SQLException;
  
  
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
	 * @throws SQLException
	 */
  public void insertExceptionItemtoAsItmRtlStr(String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable) throws SQLException;

}


