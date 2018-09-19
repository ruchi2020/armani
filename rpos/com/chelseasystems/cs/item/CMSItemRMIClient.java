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

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.igray.naming.*;
import java.rmi.*;
import java.sql.SQLException;

import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.*;
import java.util.Map;
import java.util.Date;


/**
 *
 * <p>Title: CMSItemRMIClient</p>
 *
 * <p>Description: This class deal with client-side of an RMI connection for
 * fetching/submitting item object.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public class CMSItemRMIClient extends CMSItemServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSItemRMIServer cmsitemServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;
  //Added by Satin
  private int flag = 0;

  /**
   * This method set the configuration manager and make sure that the system
   * has a security manager set.
   */
  public CMSItemRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("item.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSItemRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the update.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * This method is used to lookup the remote object from the RMI registry.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmsitemServer = (ICMSItemRMIServer)NamingService.lookup(connect);
  }

  /**
   * This method is used to check whether remote server is available or not
   * @return boolean
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsitemServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * This method is used to search an item by its item id
   * @param id String
   * @return Item
   * @throws Exception
   */
  public Item findById(String id)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findById((String)id);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }

  /**
   * This method is used to search an item by its bar code for a given store
   * @param barCode String
   * @param storeId String
   * @throws Exception
   * @return CMSItem
   */
  public CMSItem findByBarCode(String barCode, String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findByBarCode(barCode, storeId);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }
  
  /**
   * Added by Anjana to fetch the SAP item
   * @param barCode String
   * @param storeId String
   * @param itemIdLength String
   * @throws Exception
   * @return CMSItem
   */
  public CMSItem findSAPBarCode(String barCode, String storeId, String itemIdLength)
	      throws Exception {
	    for (int x = 0; x < maxTries; x++) {
	      if (cmsitemServer == null)
	        init();
	      try {
	        return cmsitemServer.findSAPBarCode(barCode, storeId, itemIdLength );
	      } catch (ConnectException ce) {
	        cmsitemServer = null;
	      } catch (Exception ex) {
	        throw new DowntimeException(ex.getMessage());
	      }
	    }
	    throw new DowntimeException("Unable to establish connection to CMSItemServices");
	  }

  /**
   * This method used to download items file at client site
   * @param fileName String
   * @param storeID String
   * @throws Exception
   * @return byte[]
   */
  public byte[] getItemFile(String fileName, String storeID, Date d)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.getItemFile(fileName, storeID, d);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }

  /**
   * This method is used to find items by its description
   * @param description description
   */
  public String[] findIDListByDescription(String description)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findIDListByDescription((String)description);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }

  /**
   * This method is used to find items by its description for a given store
   * @param description description
   */
  public String[] findIDListByDescription(String description, String storeId)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findIDListByDescription((String)description, storeId);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }

  /**
   * This methods returns an array of all Items
   * @return Item[]
   * @throws Exception
   */
  public Item[] findAllItems()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findAllItems();
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }

  /**
   * This method is used to find all the items corresponding to a given store
   * @param store Store
   * @return Item[]
   * @throws Exception
   */
  public Item[] findAllItemsForStore(Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findAllItemsForStore((Store)store);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
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
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.updateItemPrice((String)itemId, (ArmCurrency)amount);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }

  /**
   *
   * @param store Store
   * @throws Exception
   * @return Map
   */
  public Map getSupplierSeasonYear(Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.getSupplierSeasonYear(store);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }

  /**
   * This method is used to search items on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws Exception
   * @return Item[]
   */
  public Item[] findItems(ItemSearchString searchString)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findItems(searchString);
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }
  
  /**
   * This method is used to find items with zero unit price
   * @return CMSItem[]
   * @throws Exception
   */
  public CMSItem[] findItemsWithNoUnitPrice()
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsitemServer == null)
        init();
      try {
        return cmsitemServer.findItemsWithNoUnitPrice();
      } catch (ConnectException ce) {
        cmsitemServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSItemServices");
  }


  /**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws Exception
	 * @return String
	 */
public String selectItemIdFromAsItm(String barcode)
		throws Exception {
	for (int x = 0; x < maxTries; x++) {
	      if (cmsitemServer == null)
	        init();
	      try {
	        String itemId = (cmsitemServer.selectItemIdFromAsItm(barcode));
	        return itemId;
	        } catch (ConnectException ce) {
	        cmsitemServer = null;
	      } catch (Exception ex) {
	        throw new DowntimeException(ex.getMessage());
	      }
	    }
	    throw new DowntimeException("Unable to establish connection to CMSItemServices");
}


	/**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws Exception
	 * @return String
	 */
	public String selectItemFromAsItm(String itemId)
			throws Exception {
		for (int x = 0; x < maxTries; x++) {
			if (cmsitemServer == null)
				init();
			try {
				String itemId1 = (cmsitemServer.selectItemFromAsItm(itemId));
				return itemId1;
	        	} catch (ConnectException ce) {
	        		cmsitemServer = null;
	        	} catch (Exception ex) {
	        		throw new DowntimeException(ex.getMessage());
	        	}
	    	}
	    	throw new DowntimeException("Unable to establish connection to CMSItemServices");
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
	    	for (int x = 0; x < maxTries; x++) {
	    		if (cmsitemServer == null)
	    			init();
	    		try {
	    			cmsitemServer.insertIntoAsItmRtlStr(storeId, itemId, retailPrice, currencyCode, itemDesc, vatRate, taxable);
	    			return;
	    		} catch (ConnectException ce) {
	    			cmsitemServer = null;
	    		} catch (Exception ex) {
	    			throw new DowntimeException(ex.getMessage());
	    		}
	    	}
	    	throw new DowntimeException("Unable to establish connection to CMSItemServices");
	  	}




}

