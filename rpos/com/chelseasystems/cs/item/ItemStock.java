/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-21-2005 | Rajesh    | N/A       |modify to implement Serializable / new methods      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import java.io.Serializable;


/**
 *
 * <p>Title: ItemStock</p>
 *
 * <p>Description: This class keep stocks of items</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh Pradhan
 * @version 1.0
 */
public class ItemStock implements Serializable {
  String storeId, itemId;
  int availQty = 0, unAvailQty = 0, availStoreQty = 0, unAvailStoreQty = 0;

  /**
   * Default Constructor
   */
  public ItemStock() {
  }

  /**
   * This method is used to set store id
   * @param val String
   */
  public void setStoreId(String val) {
    this.storeId = val;
  }

  /**
   * This method is used to get store id
   * @return String
   */
  public String getStoreId() {
    return this.storeId;
  }

  /**
   * This method is used to set item id
   * @param val String
   */
  public void setItemId(String val) {
    this.itemId = val;
  }

  /**
   * This method is used to get item id
   * @return String
   */
  public String getItemId() {
    return this.itemId;
  }

  /**
   * This method is used to set available quantity
   * @param val int
   */
  public void setAvailableQty(int val) {
    this.availQty = val;
  }

  /**
   * This method is used to get available quantity
   * @return int
   */
  public int getAvailableQty() {
    return this.availQty;
  }

  /**
   * This method is used to set un available quantity
   * @param val int
   */
  public void setUnAvailableQty(int val) {
    this.unAvailQty = val;
  }

  /**
   * This method is used to get un available quantity
   * @return int
   */
  public int getUnAvailableQty() {
    return this.unAvailQty;
  }

  /**
   * This method is used to set available store quantity
   * @param val int
   */
  public void setAvailableStoreQty(int val) {
    this.availStoreQty = val;
  }

  /**
   * This method is used to get available store quantity
   * @return int
   */
  public int getAvailableStoreQty() {
    return this.availStoreQty;
  }

  /**
   * This method is used to set un available store quantity
   * @param val int
   */
  public void setUnAvailableStoreQty(int val) {
    this.unAvailStoreQty = val;
  }

  /**
   * This method is used to set un available store quantity
   * @return int
   */
  public int getUnAvailableStoreQty() {
    return this.unAvailStoreQty;
  }

  /**
   * This has been added just to support the references.
   * @deprecated
   */
  public int getQuantity() {
  	return getNetAvailableQuantity();
  }


  /**
   * This method is used to get Net total available quantity (i.e. TotalAvailableQuantity - TotalUnAvailableQuantity
   * @return int
   */
  public int getNetAvailableQuantity() {
    return (this.availQty - this.unAvailQty) + (this.availStoreQty - this.unAvailStoreQty);
  }

  /**
   * This method is used to get total available quantity
   * @return int
   */
  public int getTotalAvailableQuantity() {
  	return (this.availQty + this.availStoreQty);
  }
  
  //___Tim:1709
  /**
   * This method is used to get total unavailable quantity
   * @return int
   */
  public int getTotalUnAvailableQuantity(){
  	return (this.unAvailQty + this.unAvailStoreQty);
  }
}

