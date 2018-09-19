/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

import java.util.Date;
import com.chelseasystems.cr.business.BusinessObject;
import java.util.Calendar;
import java.util.TimeZone;


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
public class LoyaltyRule extends BusinessObject {
  /**
   * Attributes
   */
  protected String ruleID = "";
  protected Date startDate = new Date();
  protected Date endDate = new Date();
  protected String storeID = "";
  protected String custType = "";
  protected double pointsRatio;
  protected String itemDepart = "";
  protected String itemClass = "";
  protected String itemSubclass = "";
  protected String styleNumber = "";

  /**
   * put your documentation comment here
   * @param   String anId
   */
  public LoyaltyRule(String anId) {
    ruleID = anId;
  }

  /**
   * put your documentation comment here
   */
  public LoyaltyRule() {
  }

  /**
   * Methods
   */
  public String getRuleID() {
    return this.ruleID;
  }

  /**
   * put your documentation comment here
   * @param RuleID
   */
  public void doSetRuleID(String RuleID) {
    this.ruleID = RuleID;
  }

  /**
   * put your documentation comment here
   * @param RuleID
   */
  public void setRuleID(String RuleID) {
    doSetRuleID(RuleID);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Date getStartDate() {
    if (startDate == null)
      return null;
    //convert from local timezone to GMT without change in time values
    Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar localCal = Calendar.getInstance();
    gmtCal.setTime(startDate);
    localCal.set(gmtCal.get(Calendar.YEAR), gmtCal.get(Calendar.MONTH), gmtCal.get(Calendar.DATE)
        , gmtCal.get(Calendar.HOUR_OF_DAY), gmtCal.get(Calendar.MINUTE), gmtCal.get(Calendar.SECOND));
    localCal.set(Calendar.MILLISECOND, 0);
    return localCal.getTime();
  }

  /**
   * put your documentation comment here
   * @param startDate
   */
  public void doSetStartDate(Date startDate) {
    if (startDate == null) {
      this.startDate = null;
      return;
    }
    //convert to GMT timezone without change in time values
    Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar localCal = Calendar.getInstance();
    localCal.setTime(startDate);
    gmtCal.set(localCal.get(Calendar.YEAR), localCal.get(Calendar.MONTH)
        , localCal.get(Calendar.DATE), localCal.get(Calendar.HOUR_OF_DAY)
        , localCal.get(Calendar.MINUTE), localCal.get(Calendar.SECOND));
    gmtCal.set(Calendar.MILLISECOND, 0);
    this.startDate = gmtCal.getTime();
  }

  /**
   * put your documentation comment here
   * @param StartDate
   */
  public void setStartDate(Date StartDate) {
    doSetStartDate(StartDate);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Date getEndDate() {
    if (endDate == null)
      return null;
    //convert from local timezone to GMT without change in time values
    Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar localCal = Calendar.getInstance();
    gmtCal.setTime(endDate);
    localCal.set(gmtCal.get(Calendar.YEAR), gmtCal.get(Calendar.MONTH), gmtCal.get(Calendar.DATE)
        , gmtCal.get(Calendar.HOUR_OF_DAY), gmtCal.get(Calendar.MINUTE), gmtCal.get(Calendar.SECOND));
    localCal.set(Calendar.MILLISECOND, 0);
    return localCal.getTime();
  }

  /**
   * put your documentation comment here
   * @param endDate
   */
  public void doSetEndDate(Date endDate) {
    if (endDate == null) {
      this.endDate = null;
      return;
    }
    //convert to GMT timezone without change in time values
    Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar localCal = Calendar.getInstance();
    localCal.setTime(endDate);
    gmtCal.set(localCal.get(Calendar.YEAR), localCal.get(Calendar.MONTH)
        , localCal.get(Calendar.DATE), localCal.get(Calendar.HOUR_OF_DAY)
        , localCal.get(Calendar.MINUTE), localCal.get(Calendar.SECOND));
    gmtCal.set(Calendar.MILLISECOND, 0);
    this.endDate = gmtCal.getTime();
  }

  /**
   * put your documentation comment here
   * @param EndDate
   */
  public void setEndDate(Date EndDate) {
    doSetEndDate(EndDate);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getStoreID() {
    return this.storeID;
  }

  /**
   * put your documentation comment here
   * @param StoreID
   */
  public void doSetStoreID(String StoreID) {
    this.storeID = StoreID;
  }

  /**
   * put your documentation comment here
   * @param StoreID
   */
  public void setStoreID(String StoreID) {
    doSetStoreID(StoreID);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCustType() {
    return this.custType;
  }

  /**
   * put your documentation comment here
   * @param CustType
   */
  public void doSetCustType(String CustType) {
    this.custType = CustType;
  }

  /**
   * put your documentation comment here
   * @param CustType
   */
  public void setCustType(String CustType) {
    doSetCustType(CustType);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double getPointsRatio() {
    return this.pointsRatio;
  }

  /**
   * put your documentation comment here
   * @param PointsRatio
   */
  public void doSetPointsRatio(double PointsRatio) {
    this.pointsRatio = PointsRatio;
  }

  /**
   * put your documentation comment here
   * @param PointsRatio
   */
  public void setPointsRatio(double PointsRatio) {
    doSetPointsRatio(PointsRatio);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getItemDepart() {
    return this.itemDepart;
  }

  /**
   * put your documentation comment here
   * @param ItemDepart
   */
  public void doSetItemDepart(String ItemDepart) {
    this.itemDepart = ItemDepart;
  }

  /**
   * put your documentation comment here
   * @param ItemDepart
   */
  public void setItemDepart(String ItemDepart) {
    doSetItemDepart(ItemDepart);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getItemClass() {
    return this.itemClass;
  }

  /**
   * put your documentation comment here
   * @param ItemClass
   */
  public void doSetItemClass(String ItemClass) {
    this.itemClass = ItemClass;
  }

  /**
   * put your documentation comment here
   * @param ItemClass
   */
  public void setItemClass(String ItemClass) {
    doSetItemClass(ItemClass);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getItemSubclass() {
    return this.itemSubclass;
  }

  /**
   * put your documentation comment here
   * @param ItemSubclass
   */
  public void doSetItemSubclass(String ItemSubclass) {
    this.itemSubclass = ItemSubclass;
  }

  /**
   * put your documentation comment here
   * @param ItemSubclass
   */
  public void setItemSubclass(String ItemSubclass) {
    doSetItemSubclass(ItemSubclass);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getStyleNumber() {
    return this.styleNumber;
  }

  /**
   * put your documentation comment here
   * @param StyleNumber
   */
  public void doSetStyleNumber(String StyleNumber) {
    this.styleNumber = StyleNumber;
  }

  /**
   * put your documentation comment here
   * @param StyleNumber
   */
  public void setStyleNumber(String StyleNumber) {
    doSetStyleNumber(StyleNumber);
  }
}

