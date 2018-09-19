/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item.web;

import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cs.item.ItemSearchString;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.dataaccess.ItemDAO;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.store.CMSStore;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Sumit Krishnan
 * @version 1.0
 */
public class WebItemLookupHelper {
  private ConfigMgr configMgr;
  private ItemDAO itemDAO;
  private Hashtable itemRowSizeHash = new Hashtable();
  private ArrayList itemRowKeyArray = null;

  /**
   * Default constructor
   */
  public WebItemLookupHelper() {
    configMgr = new ConfigMgr("jdbc.cfg");
    itemDAO = (ItemDAO)configMgr.getObject("ITEM_DAO");
  }

  /**
   * This method is used to search items on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws Exception
   * @return Item[]
   */
  public Item[] findBySearchCriteria(ItemSearchString searchString)
      throws Exception {
    try {
      return itemDAO.selectByItemSearchString(searchString);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg("WebItemLookupHelper", "findBySearchCriteria"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search items on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws Exception
   * @return Item[]
   */
  public StockResultObject[] findStockBySKU(CMSItem cmsItem)
      throws Exception {
    try {
      if (cmsItem == null)
        return null;
      ItemSearchString itemSearchString = new ItemSearchString();
      itemSearchString.setStyle(cmsItem.getStyleNum());
      itemSearchString.setModel(cmsItem.getModel());
      itemSearchString.setSupplier(cmsItem.getSupplierId());
      itemSearchString.setFabric(cmsItem.getFabric());
      itemSearchString.setColor(cmsItem.getColorId());
      itemSearchString.setYear(cmsItem.getForTheYear());
      itemSearchString.setSeason(cmsItem.getSeason());
      itemSearchString.setIsAdvancedSearch(true);
      itemSearchString.setColorDesc(cmsItem.getItemDetail().getColorDesc());
      itemSearchString.setSeasonDesc(cmsItem.getItemDetail().getSeasonDesc());
      itemSearchString.setSupplierName(cmsItem.getItemDetail().getSupplierName());
      itemSearchString.setStore(((CMSStore)com.chelseasystems.cr.appmgr.AppManager.getInstance().
          getGlobalObject("STORE")).getId());
      if (itemSearchString == null)
        System.out.println("The itemSearchString is " + itemSearchString);
      if (itemDAO == null)
        System.out.println("The itemDAO is " + itemDAO);
      Item[] items = itemDAO.selectByItemSearchString(itemSearchString);
      return findStockByItems(items);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg("WebItemLookupHelper", "findStockBySKU", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to search items on the basis of specified search parameter
   * @param searchString ItemSearchString
   * @throws Exception
   * @return Item[]
   */
  private StockResultObject[] findStockByItems(Item[] items)
      throws Exception {
    for (int i = 0; i < items.length; i++) {
      addItem((CMSItem)items[i]);
    }
    StockResultObject[] stockResultObjects = new StockResultObject[itemRowSizeHash.size()];
    int i = 0;
    for (Enumeration e = itemRowSizeHash.keys(); e.hasMoreElements(); ) {
      StockResultObject stockResultObject = (StockResultObject)e.nextElement();
      ArrayList stockObjects = (ArrayList)itemRowSizeHash.get(stockResultObject);
      stockResultObject.setStockObjects((StockObject[])stockObjects.toArray(new StockObject[
          stockObjects.size()]));
      stockResultObjects[i] = stockResultObject;
      i++;
    }
    return stockResultObjects;
  }

  /**
   * put your documentation comment here
   * @param cmsItem
   */
  private void addItem(CMSItem cmsItem) {
    if (cmsItem == null)
      return;
    StockResultObject itemRowKey = new StockResultObject(cmsItem.getStoreId(), cmsItem.getStoreName()
        , cmsItem.getColorId(), cmsItem.getItemDetail().getColorDesc());
    if (!itemRowSizeHash.containsKey(itemRowKey)) {
      itemRowKeyArray = new ArrayList();
      StockObject stockObject = new StockObject(getSizeKeyString(cmsItem), cmsItem.getId()
          , cmsItem.getItemStock().getQuantity(), cmsItem.getRetailPrice().getFormattedStringValue());
      itemRowKeyArray.add(stockObject);
      itemRowSizeHash.put(itemRowKey, itemRowKeyArray);
    } else {
      itemRowKeyArray = ((ArrayList)itemRowSizeHash.get(itemRowKey));
      StockObject stockObject = new StockObject(getSizeKeyString(cmsItem), cmsItem.getId()
          , cmsItem.getItemStock().getQuantity(), cmsItem.getRetailPrice().getFormattedStringValue());
      itemRowKeyArray.add(stockObject);
      itemRowSizeHash.put(itemRowKey, itemRowKeyArray);
    }
  }

  /**
   * put your documentation comment here
   * @param cmsItem
   * @return
   */
  private String getSizeKeyString(CMSItem cmsItem) {
    String sizeKeyString = "";
    if (cmsItem.getItemDetail().getSizeIndx() != null)
      sizeKeyString = sizeKeyString + cmsItem.getItemDetail().getSizeIndx().trim();
    if (cmsItem.getItemDetail().getExtSizeIndx() != null
        && !cmsItem.getItemDetail().getExtSizeIndx().trim().equals("")) {
      if (sizeKeyString.trim().length() > 0)
        sizeKeyString = sizeKeyString + ":";
      sizeKeyString = sizeKeyString + cmsItem.getItemDetail().getExtSizeIndx().trim();
    }
    return sizeKeyString;
  }
}

