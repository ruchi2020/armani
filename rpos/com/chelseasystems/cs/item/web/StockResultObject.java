/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item.web;


/**
 * put your documentation comment here
 */
public class StockResultObject {
  private String storeId;
  private String storeName;
  private String colorId;
  private String colorDesc;
  private StockObject[] stockObjects;

  /**
   * <p>Title: </p>
   * <p>Description: </p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Sumit Krishnan
   * @version 1.0
   */
  public StockResultObject(String storeId, String storeName, String colorId, String colorDesc) {
    this.storeId = storeId;
    this.storeName = storeName;
    this.colorId = colorId;
    this.colorDesc = colorDesc;
  }

  /**
   * put your documentation comment here
   * @param obj
   * @return
   */
  public boolean equals(Object obj) {
    if (storeId.equals(((StockResultObject)obj).storeId)
        && colorId.equals(((StockResultObject)obj).colorId))
      return true;
    return false;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int hashCode() {
    return storeId.hashCode() & colorId.hashCode();
  }

  /**
   * This method is used to get item's storeId
   * @return String
   */
  public String getStoreId() {
    return this.storeId;
  }

  /**
   * This method is used to get item's storeId
   * @return String
   */
  public String getStoreName() {
    return this.storeName;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public String getColorId() {
    return this.colorId;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public String getColorDesc() {
    return this.colorDesc;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public void setStockObjects(StockObject[] stockObjects) {
    this.stockObjects = stockObjects;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public StockObject[] getStockObjects() {
    return this.stockObjects;
  }
}

