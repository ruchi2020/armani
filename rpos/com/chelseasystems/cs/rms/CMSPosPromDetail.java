/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-27-2005 | Anand     | N/A       | Modified and armani-specific accessor and    |
 |      |            |           |           | mutator methods                              |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.rms;

import com.chelseasystems.cr.rms.PosPromDetail;
import java.util.*;


/**
 *
 * <p>Title: CMSPosPromDetail</p>
 *
 * <p>Description: Class store the details of promotion</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSPosPromDetail extends PosPromDetail {
  private String startDate;
  private String endDate;
  private String storeId;
  private String promotionNum;
  private String ApplyTo;
  private double discountPercent;
  private boolean isDiscount;
  private String applicableItm;
  private String stgEventId;
  private String stgStatus;
  private String stgErrorMessage;
  private Date stgLoadDate;
  private Date stgProcessDate;
  private String promotionName;

  //public void doSetStartDate(String startDate){ this.startDate = startDate;}
  //public String getStartDate() { return this.startDate;}
  //public void doSetEndDate(String endDate){ this.endDate = endDate;}
  //public String getEndDate() { return this.endDate;}
  /**
   * Method sets promotion number
   * @param promotionNum String
   */
  public void doSetPromotionNum(String promotionNum) {
    this.promotionNum = promotionNum;
  }

  /**
   * Method returns promotion number
   * @return String
   */
  public String getPromotionNum() {
    return this.promotionNum;
  }

  /**
   * Method sets promotion name
   * @param promotionName String
   */
  public void doSetPromotionName(String promotionName) {
    this.promotionName = promotionName;
  }

  /**
   * Method returns promotion name
   * @return String
   */
  public String getPromotionName() {
    return this.promotionName;
  }

  //	public void doSetApplyTo(boolean applyTo){ this.applyTo = applyTo;}
  //	public boolean getApplyTo() { return this.applyTo;}
  /**
   * Method sets discount percent
   * @param discountPercent double
   */
  public void doSetDiscountPercent(double discountPercent) {
    this.discountPercent = discountPercent;
  }

  /**
   * Method returns discount percent
   * @return double
   */
  public double getDiscountPercent() {
    return this.discountPercent;
  }

  //public void doSetDiscountType(String discountType){ this.discountType = discountType;}
  //public String getDiscountType() { return this.discountType;}
  //public void doSetThresholdAmt(String thresholdAmt){ this.thresholdAmt = thresholdAmt;}
  //    public String getThresholdAmt() { return this.thresholdAmt;}
  /**
   * Method set the discount
   * @param isDiscount boolean
   */
  public void doSetIsDiscount(boolean isDiscount) {
    this.isDiscount = isDiscount;
  }

  /**
   * Method checks whether discount is there
   * @return boolean
   */
  public boolean getIsDiscount() {
    return this.isDiscount;
  }

  /**
   * Method sets application item
   * @param applicableItm boolean
   */
  public void doSetApplicableItm(String applicableItm) {
    this.applicableItm = applicableItm;
  }

  /**
   * Method returns application item
   * @return boolean
   */
  public String getApplicableItm() {
    return this.applicableItm;
  }

  /**
   * Method returns stagging event id
   * @param stgEventId String
   */
  public void doSetStgEventId(String stgEventId) {
    this.stgEventId = stgEventId;
  }

  /**
   * Method returns stagging event id
   * @return String
   */
  public String getStgEventId() {
    return this.stgEventId;
  }

  /**
   * Method sets stagging status
   * @param stgStatus String
   */
  public void doSetStgStatus(String stgStatus) {
    this.stgStatus = stgStatus;
  }

  /**
   * Method returns stagging status
   * @return String
   */
  public String getStgStatus() {
    return this.stgStatus;
  }

  /**
   * Method sets stagging error message
   * @param stgErrorMessage String
   */
  public void doSetStgErrorMessage(String stgErrorMessage) {
    this.stgErrorMessage = stgErrorMessage;
  }

  /**
   * Method returns stagging error message
   * @return String
   */
  public String getStgErrorMessage() {
    return this.stgErrorMessage;
  }

  /**
   * Method sets stagging load date
   * @param stgLoadDate Date
   */
  public void doSetStgLoadDate(Date stgLoadDate) {
    this.stgLoadDate = stgLoadDate;
  }

  /**
   * Method returns stagging load date
   * @return Date
   */
  public Date getStgLoadDate() {
    return this.stgLoadDate;
  }

  /**
   * Method sets stagging process date
   * @param stgProcessDate Date
   */
  public void doSetStgProcessDate(Date stgProcessDate) {
    this.stgProcessDate = stgProcessDate;
  }

  /**
   * Method returns stagging process date
   * @return Date
   */
  public Date getStgProcessDate() {
    return this.stgProcessDate;
  }

  /**
   *
   * @return String
   */
  public String getStoreId() {
    return this.storeId;
  }

  /**
   *
   * @param storeId String
   */
  public void doSetStoreId(String storeId) {
    this.storeId = storeId;
  }
}

