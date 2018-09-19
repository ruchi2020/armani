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

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.igray.naming.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.pos.CMSTransactionPOSServices;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterServices;

import java.io.File;

/**
 *
 * <p>Title: CMSItemRMIServerImpl</p>
 *
 * <p>Description: This is the server side of the RMI connection used for
 * fetching/submitting information. This class delgates all method calls to the
 * object referenced by the return value from CMSItemServices.getCurrent(). </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public class CMSItemRMIServerImpl extends CMSComponent implements ICMSItemRMIServer {

  /**
   * put your documentation comment here
   * @param   Properties props
   */
  public CMSItemRMIServerImpl(Properties props)
      throws RemoteException {
    super(props);
    setImpl();
    init();
  }

  /**
   * This method is used to set the server side implementation
   */
  private void setImpl() {
    Object obj = getConfigManager().getObject("SERVER_IMPL");
    if (null == obj) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
          , "Could not instantiate SERVER_IMPL.", "Make sure item.cfg contains SERVER_IMPL"
          , LoggingServices.MAJOR);
    }
    CMSItemServices.setCurrent((CMSItemServices)obj);
  }

  /**
   * This method is used to bind item object to RMI registery
   */
  private void init() {
    System.out.println("Binding to RMIRegistry...");
    String theName = getConfigManager().getString("REMOTE_NAME");
    if (null != theName) {
      bind(theName, this);
    } else {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Could not find name to bind to in registry."
          , "Make sure item.cfg contains a RMIREGISTRY entry.", LoggingServices.MAJOR);
    }
  }

  /**
   * This method receives callback when the config file changes
   * @param aKey an array of keys that have changed
   */
  protected void configEvent(String[] aKey) {}

  /**
   * This method is used by the DowntimeManager to determine when this object is
   * available.
   * @return boolean <code>true</code> indicates that this class is available.
   * @throws RemoteException
   */
  public boolean ping()
      throws RemoteException {
    return true;
  }

  /**
   * This method is used to search an item by its item id
   * @param id String
   * @return CMSItem
   * @throws RemoteException
   */
  public CMSItem findById(String id)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSItem)CMSItemServices.getCurrent().findById(id);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }

  /**
   * This method is used to search an item by its bar code for a given store
   * @param barCode String
   * @param storeId String
   * @throws RemoteException
   * @return CMSItem
   */
  public CMSItem findByBarCode(String barCode, String storeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findByBarCode(barCode
          , storeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }
  
  /**
   * Added by Anjana to fetch the SAP item
   * @param barCode String
   * @param storeId String
   * @param itemIdLength String
   * @return CMSItem
   * @throws RemoteException
   */
  public CMSItem findSAPBarCode(String barCode, String storeId , String itemIdLength)
	      throws RemoteException {
	  long start = getStartTime();
	    try {
	      if (!isAvailable())
	        throw new ConnectException("Service is not available");
	      incConnection();
	      return (CMSItem)((CMSItemServices)CMSItemServices.getCurrent()).findSAPBarCode(barCode
	          , storeId, itemIdLength);
	    } catch (Exception e) {
	      throw new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("findSAPBarCode", start);
	      decConnection();
	    }
	  }
  

  /**
   * This method is used to download items file at client site
   * @param fileName String
   * @param storeId String
   * @param d Date
   * @return byte[]
   * @throws RemoteException
   */
  public byte[] getItemFile(String fileName, String storeId, Date d)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (byte[])((CMSItemServices)CMSItemServices.getCurrent()).getItemFile(fileName, storeId
          , d);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findById", start);
      decConnection();
    }
  }

  /**
   * This method is used to find items by its description
   * @param description String
   * @return String[]
   * @throws RemoteException
   */
  public String[] findIDListByDescription(String description)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String[])CMSItemServices.getCurrent().findIDListByDescription(description);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findIDListByDescription", start);
      decConnection();
    }
  }

  /**
   * This method is used to find items by its description for a given store
   * @param description String
   * @param storeId String
   * @return String[]
   * @throws RemoteException
   */
  public String[] findIDListByDescription(String description, String storeId)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String[])((CMSItemServices)CMSItemServices.getCurrent()).findIDListByDescription(
          description, storeId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findIDListByDescription", start);
      decConnection();
    }
  }

  /**
   * This method is used to find all items
   * @return CMSItem[]
   * @throws RemoteException
   */
  public CMSItem[] findAllItems()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSItem[])CMSItemServices.getCurrent().findAllItems();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllItems", start);
      decConnection();
    }
  }

  /**
   * This method is used to find all the items corresponding to a given store
   * @param store Store
   * @return CMSItem[]
   * @throws RemoteException
   */
  public CMSItem[] findAllItemsForStore(Store store)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSItem[])CMSItemServices.getCurrent().findAllItemsForStore(store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllItemsForStore", start);
      decConnection();
    }
  }

  /**
   * This method is used to update an Item, specified by its Id, with a new price,
   * which is specified by the amount. Only local services need to implement
   * this method.
   * @param itemId the ID of the Item to update
   * @param amount the new amount of the Item specified
   */
  public boolean updateItemPrice(String itemId, ArmCurrency amount)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (boolean)CMSItemServices.getCurrent().updateItemPrice(itemId, amount);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("updateItemPrice", start);
      decConnection();
    }
  }

  /**
   *
   * @param store Store
   * @throws RemoteException
   * @return Map
   */
  public Map getSupplierSeasonYear(Store store)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (Map)((CMSItemServices)CMSItemServices.getCurrent()).getSupplierSeasonYear(store);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("getSupplierSeasonYear", start);
      decConnection();
    }
  }

  /**
   * This method is used to search items on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws RemoteException
   * @return Item[]
   */
  public Item[] findItems(ItemSearchString searchString)
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (Item[])((CMSItemServices)CMSItemServices.getCurrent()).findItems(searchString);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findItems", start);
      decConnection();
    }
  }
  
  
  /**
   * This method is used to find items with zero unit price
   * @throws RemoteException
   * @return CMSItem[]
   */
  public CMSItem[] findItemsWithNoUnitPrice()
      throws RemoteException {
    long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (CMSItem[])((CMSItemServices)CMSItemServices.getCurrent()).findItemsWithNoUnitPrice();
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findItemsWithNoUnitPrice", start);
      decConnection();
    }
  }

  
 
  /**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws RemoteException
	 * @return String
	 */
  public String selectItemIdFromAsItm(String barcode)
		throws RemoteException {
	long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String) ((CMSItemServices)CMSItemServices.getCurrent()).selectItemIdFromAsItm(barcode);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("findAllItemsForStore", start);
      decConnection();
    }
  }
  
  
  /**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws RemoteException
	 * @return String
	 */
  public String selectItemFromAsItm(String itemId)
		throws RemoteException {
	long start = getStartTime();
    try {
      if (!isAvailable())
        throw new ConnectException("Service is not available");
      incConnection();
      return (String) ((CMSItemServices)CMSItemServices.getCurrent()).selectItemFromAsItm(itemId);
    } catch (Exception e) {
      throw new RemoteException(e.getMessage(), e);
    } finally {
      addPerformance("selectItemFromAsItm", start);
      decConnection();
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
	 * @throws RemoteException
	 */
  public void insertIntoAsItmRtlStr(String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable)
	     throws RemoteException {
	   long start = getStartTime();
	   try {
	     if (!isAvailable())
	      throw new ConnectException("Service is not available");
	     incConnection();
	     CMSItemServices.getCurrent().insertIntoAsItmRtlStr(storeId, itemId, retailPrice, currencyCode, itemDesc, vatRate, taxable);
	   } catch (Exception e) {
	     throw new RemoteException(e.getMessage(), e);
	   } finally {
	     addPerformance("insertIntoAsItmRtlStr", start);
	     decConnection();
	   }
	 }
  



}


