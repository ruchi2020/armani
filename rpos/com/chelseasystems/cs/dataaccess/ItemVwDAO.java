/**
 * Description:retek Item View
 * Created By:Khyati Shah
 * Created Date:2/4/2005
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 3	4/19/05	        RP	Item lookup specs	        Implementation
 * 1	2/4/05	        KS	POS_IS_ItemDownload_Rev1	RKItemDAO
 *
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import  com.chelseasystems.cs.store.CMSStore;
import  com.chelseasystems.cs.item.*;
import  com.chelseasystems.cr.store.Store;
import  java.util.*;


public interface ItemVwDAO extends BaseDAO
{

  /**
   *
   * @param barcode String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem[]
   */
  public CMSItem selectByBarcode (String barcode, String storeId) throws SQLException;

  /**
  *Added by Anjana to fetch SAP Item
  * @param barcode String
  * @param storeId String
  *@param itemIdLength String
  * @throws SQLException
  * @return CMSItem
  */
  public CMSItem findSAPBarCode (String barcode, String storeId, String itemIdLength) throws SQLException;

  /**
   *
   * @param Id String
   * @throws SQLException
   * @return CMSItem
   */
  public CMSItem selectById (String Id) throws SQLException;



  /**
   *
   * @param description String
   * @param storeId String
   * @throws SQLException
   * @return CMSItem[]
   */
  public CMSItem[] selectByDescriptionAndStoreId (String description, String storeId) throws SQLException;



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
}



