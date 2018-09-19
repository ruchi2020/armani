/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item.web;


/**
 * put your documentation comment here
 */
public class StockObject {
  public String size;
  public String SKUId;
  public int quantity;
  public String price;

  /**
   * <p>Title: </p>
   * <p>Description: </p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Sumit Krishnan
   * @version 1.0
   */
  public StockObject(String size, String SKUId, int quantity, String price) {
    this.size = size;
    this.SKUId = SKUId;
    this.quantity = quantity;
    this.price = price;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public String getSize() {
    return this.size;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public String getSKUId() {
    return this.SKUId;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public int getQuantity() {
    return this.quantity;
  }

  /**
   * This method is used to get item's size
   * @return String
   */
  public String getPrice() {
    return this.price;
  }
}

