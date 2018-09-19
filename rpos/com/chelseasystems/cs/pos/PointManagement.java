/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.loyalty.Loyalty;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PointManagement extends CMSNoSaleTransaction {
  /**
   * Member
   * @param aStore Store
   */
  protected Loyalty loyalty;
  protected long points;

  /**
   * Need to add method that the Txn type returned is POINTS
   * @param aStore Store
   */
  public String getTransactionType() {
    return "POINTS";
  }

  /**
   * put your documentation comment here
   * @param   Store aStore
   */
  public PointManagement(Store aStore) {
    super(aStore);
  }

  /**
   * put your documentation comment here
   * @param   Store aStore
   * @param   Loyalty loyalty
   */
  public PointManagement(Store aStore, Loyalty loyalty) {
    super(aStore);
    this.loyalty = loyalty;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Loyalty getLoyalty() {
    return this.loyalty;
  }

  /**
   * put your documentation comment here
   * @param obj
   */
  public void setLoyalty(Loyalty obj) {
    this.doSetLoyalty(obj);
  }

  /**
   * put your documentation comment here
   * @param obj
   */
  public void doSetLoyalty(Loyalty obj) {
    this.loyalty = obj;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public long getPoints() {
    return this.points;
  }

  /**
   * put your documentation comment here
   * @param points
   */
  public void setPoints(long points) {
    this.doSetPoints(points);
  }

  /**
   * put your documentation comment here
   * @param points
   */
  public void doSetPoints(long points) {
    this.points = points;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getComment() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String comStr = null;
    if (loyalty != null) {
      if (getPoints() >= 0)
        comStr = getPoints() + " "+res.getString("POINTS ADDED FOR LOYALTY CARD")+" : " + loyalty.getLoyaltyNumber();
      else {
        comStr = getPoints() + " "+res.getString("POINTS SUBTRACTED FOR LOYALTY CARD")+" : " + loyalty.getLoyaltyNumber();
      }
      if (super.getComment() != null && !super.getComment().trim().equals("")) {
        comStr += " :: " + res.getString(super.getComment());
      }
    } else
      comStr = res.getString("Loyalty Point management transaction");
    return comStr;
  }
}

