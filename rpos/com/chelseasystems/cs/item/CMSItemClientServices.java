/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	2/4/05	        KS		                        Base
 * 2	2/8/05	        KS	POS_IS_ItemDownload_Rev1	CMSItemClientServices
 * 3    4/14/05          KS      Send Sale                       FindById to read from dat file
 * 4    4/27/05          VM      Item Lookup                     Corrected CLIENT_DOWNLOAD_IMPL and CLIENT_IMPL usage
 * 5    5/24/05          VM                                      Corrected for Exceptions in Offline mode
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.payment.CMSRedeemableServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSServices;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterServices;

// added for exception Item.
//import com.chelseasystems.cs.item.ItemServices;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;


/**
 *
 * <p>Title: CMSItemClientServices</p>
 *
 * <p>Description: This is client-side object for retrieving and submitting
 * item object</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 * Created Date:2/8/2005
 */
public class CMSItemClientServices extends ClientServices {

  /** Configuration manager **/
 // private ConfigMgr config = null;

  /**
   * Default Constructor
   */
  public CMSItemClientServices() {
    // Set up the configuration manager.
    config = new ConfigMgr("item.cfg");
  }

  /**
   * This method initialize the primary implementation
   */
  public void init(boolean online)
      throws Exception {
    // Set up the proper implementation of the service.
    if (online)
      onLineMode();
    else
      offLineMode();
  }

  /**
   * Reads "CLIENT_IMPL" from config file. Returns the class that defines
   * what object is providing the service to objects using this client service
   * in "on-line" mode, i.e. connected to an app server.  If null, this 
   * clientservice is not considered when determining app online status.
   * @return a class of the online service.
   */
  protected Class getOnlineService () throws ClassNotFoundException {
    String className = config.getString("CLIENT_DOWNLOAD_IMPL");
    Class serviceClass = Class.forName(className);
    return  serviceClass;
  }
  
  /**
   * This method is invoked when system is online, to get the client remote
   * implementation from the item.cfg and set the same in CMSItemServices
   */
  public void onLineMode() {
    LoggingServices.getCurrent().logMsg("On-Line Mode for CMSItemClientServices");
    CMSItemServices serviceImpl = (CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSItemClientServices", "onLineMode()"
          , "Cannot instantiate the class that provides the"
          + "implementation of CMSItemServices in item.cfg."
          , "Make sure that item.cfg contains an entry with "
          + "a key of CLIENT_IMPL and a value that is the name of a class "
          + "that provides a concrete implementation of CMSItemServices.", LoggingServices.MAJOR);
      setOffLineMode();
      return;
    }
    CMSItemServices.setCurrent(serviceImpl);
  }
 
  /**
   * This method is invoked when system is offline, to get the client remote
   * downtime from the item.cfg and set the same in CMSItemServices
   */
  public void offLineMode() {
    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSItemClientServices");
    CMSItemServices serviceImpl = (CMSItemServices)config.getObject("CLIENT_DOWNTIME");
    if (null == serviceImpl) {
      LoggingServices.getCurrent().logMsg("CMSItemClientServices", "offLineMode()"
          , "Cannot instantiate the class that provides the"
          + " implementation of CMSItemServices in item.cfg."
          , "Make sure that item.cfg contains an entry with "
          + "a key of CLIENT_DOWNTIME and a value"
          + " that is the name of a class that provides a concrete"
          + " implementation of CMSItemServices.", LoggingServices.CRITICAL);
    }
    CMSItemServices.setCurrent(serviceImpl);
  }

  /**
   *
   * @return Object
   */
  public Object getCurrentService() {
    return CMSItemServices.getCurrent();
  }

  /**
   * This method is used to search an item by its item id
   * @param id String
   * @return CMSItem
   * @throws Exception
   */
  public CMSItem findById(String id)
      throws Exception {
    //ks: From base. new implemention to get the id from the dat file first
    /*      try {
     this.fireWorkInProgressEvent(true);
     //         return (CMSItem) CMSItemServices.getCurrent().findById(id);
     CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL");
     CMSItemServices.setCurrent(serverImpl);
     CMSItem cmsItem = (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findById(id);
     CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_IMPL"));
     return cmsItem;
     } catch (DowntimeException ex) {
     CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_IMPL"));
     LoggingServices.getCurrent().logMsg(getClass().getName(),"findById",
     "Primary Implementation for CMSItemServices failed, going Off-Line...",
     "See Exception", LoggingServices.MAJOR, ex);
     offLineMode();
     setOffLineMode();
     return (CMSItem) CMSItemServices.getCurrent().findById(id);
     } finally {
     this.fireWorkInProgressEvent(false);
     }*/
    try {
      this.fireWorkInProgressEvent(true);
      CMSItem cmsItem = (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findById(id);
      //If item == null, get from the database
      if (cmsItem == null) {
        CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
        if (serverImpl != null) {
          CMSItemServices.setCurrent(serverImpl);
          cmsItem = (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findById(id);
        }
        CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      }
      return cmsItem;
    } catch (DowntimeException ex) {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findById"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findById(id);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search an item by its bar code
   * @param barCode String
   * @param storeId String
   * @throws Exception
   * @return CMSItem
   */
  public CMSItem findByBarCode(String barCode, String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSItem cmsItem = (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findByBarCode(
          barCode, storeId);
      
   
      //If item == null, get from the database
     if (cmsItem == null && AppManager.getCurrent().isOnLine()) {
        CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
        if (serverImpl != null) {
          CMSItemServices.setCurrent(serverImpl);
         cmsItem = (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findByBarCode(barCode
              , storeId);
        }
        CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      }
      return cmsItem;
    } catch (DowntimeException ex) {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByBarcodeId"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findByBarCode(barCode
          , storeId);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }
  /**
   * Added by Anjana to select the proper barcode if it is a SAP Store
   * @param barCode String
   * @param storeId String
   * @throws Exception
   * @return CMSItem
   */
  public CMSItem findSAPItem(String barCode, String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSItem cmsItem = null;
     cmsItem = (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findByBarCode(
          barCode, storeId);
     String itemIdLength = "8";
 	if (cmsItem == null && AppManager.getCurrent().isOnLine()) {
        CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
        if (serverImpl != null) {
        CMSItemServices.setCurrent(serverImpl);
         cmsItem = (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findSAPBarCode(barCode
              , storeId,itemIdLength);
         return cmsItem;
        }
        CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      }else{
    	  return cmsItem;
      }
      return null;
    } catch (DowntimeException ex) {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findByBarcodeId"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findByBarCode(barCode
          , storeId);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method download items dat file at client site
   * @param fileName String
   * @param storeId String
   * @throws Exception
   * @return byte[]
   */
  public byte[] getItemFile(String fileName, String storeId, Date d)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (byte[])((CMSItemServices)CMSItemServices.getCurrent()).getItemFile(fileName, storeId
          , d);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findById"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (byte[])((CMSItemServices)CMSItemServices.getCurrent()).getItemFile(fileName, storeId
          , d);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method will return an array of item ids whose description at least
   * partially matches the submitted description
   * @param description String
   * @param storeId String
   * @return String[]
   * @throws Exception
   */
  public String[] findIDListByDescription(String description, String storeId)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      //         return (String[]) CMSItemServices.getCurrent().findIDListByDescription(description, storeId);
      CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
      String items[] = new String[0];
      if (serverImpl != null) {
        CMSItemServices.setCurrent(serverImpl);
        items = ((CMSItemServices)CMSItemServices.getCurrent()).findIDListByDescription(description
            , storeId);
      } else {
        CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      }
      return items;
    } catch (DowntimeException ex) {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findIDListByDescription"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (String[])((CMSItemServices)CMSItemServices.getCurrent()).findIDListByDescription(
          description, storeId);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This methos returns an array of all Items
   * @return CMSItem[]
   * @throws Exception
   */
  public CMSItem[] findAllItems()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSItem[])CMSItemServices.getCurrent().findAllItems();
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAllItems"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSItem[])CMSItemServices.getCurrent().findAllItems();
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method returns all items with the currency type corresponding to a
   * given store
   * @param store Store
   * @return CMSItem[]
   * @throws Exception
   */
  public CMSItem[] findAllItemsForStore(Store store)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (CMSItem[])CMSItemServices.getCurrent().findAllItemsForStore(store);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findAllItemsForStore"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSItem[])CMSItemServices.getCurrent().findAllItemsForStore(store);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to update an Item, specified by its Id, with a new price,
   * which is specified by the amount. Only local services need to implement
   * this method.
   * @param itemId String
   * @param amount Currency
   * @return boolean
   * @throws Exception
   */
  public boolean updateItemPrice(String itemId, ArmCurrency amount)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      return (boolean)CMSItemServices.getCurrent().updateItemPrice(itemId, amount);
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "updateItemPrice"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (boolean)CMSItemServices.getCurrent().updateItemPrice(itemId, amount);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   *
   * @param store Store
   * @return Map
   * @throws Exception
   */
  public Map getSupplierSeasonYear(Store store)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
      CMSItemServices.setCurrent(serverImpl);
      Map map = null;
      if (CMSItemServices.getCurrent() != null)
        map = (Map)((CMSItemServices)CMSItemServices.getCurrent()).getSupplierSeasonYear(store);
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      return map;
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "getSupplierSeasonYear"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Map)((CMSItemServices)CMSItemServices.getCurrent()).getSupplierSeasonYear(store);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  /**
   * This method is used to search item on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws Exception
   * @return Item[]
   */
  public Item[] findItems(ItemSearchString searchString)
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
      CMSItemServices.setCurrent(serverImpl);
      if (serverImpl == null)
        throw new DowntimeException("CLIENT_IMPL for CMSItemServices is Null");
      Item[] items = (Item[])((CMSItemServices)CMSItemServices.getCurrent()).findItems(searchString);
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      return items;
    } catch (DowntimeException ex) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findItems"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (Item[])((CMSItemServices)CMSItemServices.getCurrent()).findItems(searchString);
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }
  
  /**
   * This method is used to find items with zero unit price  
   * @return CMSItem[]
   * @throws Exception
   */
  public CMSItem[] findItemsWithNoUnitPrice()
      throws Exception {
    try {
      this.fireWorkInProgressEvent(true);
      CMSItem[] cmsItem = (CMSItem[])((CMSItemServices)CMSItemServices.getCurrent()).findItemsWithNoUnitPrice();
      if (cmsItem == null) {
        CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL");
        if (serverImpl != null) {
          CMSItemServices.setCurrent(serverImpl);
          cmsItem = (CMSItem[])((CMSItemServices)CMSItemServices.getCurrent()).findItemsWithNoUnitPrice();
        }
        CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      }
      return cmsItem;
    } catch (DowntimeException ex) {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      LoggingServices.getCurrent().logMsg(getClass().getName(), "findItemsWithNoUnitPrice"
          , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
          , LoggingServices.MAJOR, ex);
      offLineMode();
      setOffLineMode();
      return (CMSItem[])((CMSItemServices)CMSItemServices.getCurrent()).findItemsWithNoUnitPrice();
    } finally {
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
      this.fireWorkInProgressEvent(false);
    }
  }

  
  /**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws Exception
	 * @return String
	 */
public String selectItemIdFromAsItm(String barcode) 
		throws Exception{

	try {
    this.fireWorkInProgressEvent(true);
    String cmsItemId = null;

    if (AppManager.getCurrent().isOnLine()) {
  	  CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
      if (serverImpl != null) {
      	CMSItemServices.setCurrent(serverImpl);
        cmsItemId = (String)((CMSItemServices)CMSItemServices.getCurrent()).selectItemIdFromAsItm(barcode);
                
      }
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
    }
    return cmsItemId;
  } catch (DowntimeException ex) {
    CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
    LoggingServices.getCurrent().logMsg(getClass().getName(), "findByBarcodeId"
        , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
        , LoggingServices.MAJOR, ex);
    offLineMode();
    setOffLineMode();
    return (String)((CMSItemServices)CMSItemServices.getCurrent()).selectItemIdFromAsItm(barcode);
  } finally {
    CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
    this.fireWorkInProgressEvent(false);
  }
}


/**
 * Added by Satin to select an item corresponding to entered itemId for exception item.
 * @param itemId String
 * @throws Exception
 * @return String
 */
public String selectItemFromAsItm(String itemId) 
		throws Exception{

	try {
    this.fireWorkInProgressEvent(true);
    String cmsItemId = null;

    if (AppManager.getCurrent().isOnLine()) {
  	  CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
      if (serverImpl != null) {
      	CMSItemServices.setCurrent(serverImpl);
        cmsItemId = (String)((CMSItemServices)CMSItemServices.getCurrent()).selectItemFromAsItm(itemId);
                
      }
      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
    }
    return cmsItemId;
  } catch (DowntimeException ex) {
    CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
    LoggingServices.getCurrent().logMsg(getClass().getName(), "findItemInAsItm"
        , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
        , LoggingServices.MAJOR, ex);
    offLineMode();
    setOffLineMode();
    return (String)((CMSItemServices)CMSItemServices.getCurrent()).selectItemFromAsItm(itemId);
  } finally {
    CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
    this.fireWorkInProgressEvent(false);
  }
}


/**
 * Added by Satin.
 * This method is used to insert a record into AS_ITM_RTL_STR for exception item.
 * 
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
	    this.fireWorkInProgressEvent(true);
	    if (AppManager.getCurrent().isOnLine()) {
	  	  CMSItemServices serverImpl = (CMSItemServices)config.getObject("CLIENT_IMPL");
	      if (serverImpl != null) {
	      	CMSItemServices.setCurrent(serverImpl);
	      	CMSItemServices.getCurrent().insertIntoAsItmRtlStr(storeId, itemId, retailPrice, currencyCode, itemDesc, vatRate, taxable);
	      }
	      CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
	    }
	    } catch (DowntimeException ex) {
	    CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
	    LoggingServices.getCurrent().logMsg(getClass().getName(), "insertExceptionItemtoAsItmRtlStr"
	        , "Primary Implementation for CMSItemServices failed, going Off-Line...", "See Exception"
	        , LoggingServices.MAJOR, ex);
	    offLineMode();
	    setOffLineMode();
	    CMSItemServices.getCurrent().insertIntoAsItmRtlStr(storeId, itemId, retailPrice, currencyCode, itemDesc, vatRate, taxable);
	  } finally {
	    CMSItemServices.setCurrent((CMSItemServices)config.getObject("CLIENT_DOWNLOAD_IMPL"));
	    this.fireWorkInProgressEvent(false);
	  }


}



}






