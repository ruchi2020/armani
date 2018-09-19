/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2003, Retek Inc
//


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.business.*;
import java.util.*;


/**
 *
 * <p>Title: CMSPromotion</p>
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
public abstract class CMSPromotion extends BusinessObject implements IPromotion
    , java.io.Serializable {
  private static final long serialVersionUID = 4544774900295612987L;
  public static final String REDUCTION_BY_FIXED_UNIT_PRICE = "FIXED_UNIT_PRICE";
  public static final String REDUCTION_BY_FIXED_TOTAL_PRICE = "FIXED_TOTAL_PRICE";
  public static final String REDUCTION_BY_UNIT_PRICE_OFF = "UNIT_PRICE_OFF";
  public static final String REDUCTION_BY_TOTAL_PRICE_OFF = "TOTAL_PRICE_OFF";
  public static final String REDUCTION_BY_PERCENTAGE_OFF = "PERCENTAGE_OFF";

  /**
   * Constructor
   * @param id String
   */
  public CMSPromotion(String id) {
    this.id = id;
  }

  private String id = new String();
  private String ruleDrvId = new String();
  private String description = "";
  private Calendar beginTime = null;
  private Calendar endTime = null;
  protected String methodOfReduction = null;
  private Date startDate = null;
  private Date endDate = null;
  protected ArmCurrency reductionAmount = null; //for types other than REDUCTION_BY_PERCENTAGE_OFF
  protected double reductionPercent = 0.0; //for REDUCTION_BY_PERCENTAGE_OFF
  private boolean isDiscount;
  private String applicableItm;
  private String idStrRt = "";
  private String discountType = "";
  private String applyTo;
  private String promotionNum = "";
  private String promotionName = "";

  /**
   * This method is used to get id
   * @return String
   */
  public String getId() {
    return this.id;
  }

  /**
   * This method is used to get description of promotion
   * @return String
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * This method is used to get name of promotion
   * @return String
   */
  public String getPromotionName() {
    return promotionName;
  }

  /**
   * This method is used to get begin time of promotion
   * @return Calendar
   */
  public Calendar getBeginTime() {
    return this.beginTime;
  }

  /**
   * This method is used to get end time of promotion
   * @return Calendar
   */
  public Calendar getEndTime() {
    return this.endTime;
  }

  /**
   * This method is used to get reduction amount
   * @return Currency
   */
  public ArmCurrency getReductionAmount() {
    return this.reductionAmount;
  }

  /**
   * This method is used to get reduction percent
   * @return double
   */
  public double getReductionPercent() {
    return this.reductionPercent;
  }

  /**
   * This method is used to get is discount is there or not
   * @return boolean
   */
  public boolean getIsDiscount() {
    return this.isDiscount;
  }

  /**
   * This method is used to get applicable item of promotion
   * @return boolean
   */
  public String getApplicableItm() {
    return this.applicableItm;
  }

  /**
   *
   * @return String
   */
  public String getIdStrRt() {
    return this.idStrRt;
  }

  /**
   * This method is used to get discount type
   * @return String
   */
  public String getDiscountType() {
    return this.discountType;
  }

  /**
   * This method is used to get apply to for promotion
   * @return String
   */
  public String getApplyTo() {
    return this.applyTo;
  }

  /**
   * This method is used to get promotion number
   * @return String
   */
  public String getPromotionNum() {
    return this.promotionNum;
  }

  /**
   * This method is used to get start date of promotion
   * @return Date
   */
  public Date getStartDate() {
    return this.startDate;
  }

  /**
   * This method is used to get end date of promotion
   * @return Date
   */
  public Date getEndDate() {
    return this.endDate;
  }

  /**
   * This method is used to get Rule Derivation Id of promotion
   * @return Date
   */
  public String getRuleDrvId() {
    return this.ruleDrvId;
  }

  /**
   * This method is used to check whether reduction is by fixed unit price
   * @return boolean
   */
  public boolean isReductionByFixedUnitPrice() {
    return this.methodOfReduction.equals(REDUCTION_BY_FIXED_UNIT_PRICE);
  }

  /**
   * This method is used to check whether reduction is by fixed total price
   * @return boolean
   */
  public boolean isReductionByFixedTotalPrice() {
    return this.methodOfReduction.equals(REDUCTION_BY_FIXED_TOTAL_PRICE);
  }

  /**
   * This method is used to check whether reduction is by unit price off
   * @return boolean
   */
  public boolean isReductionByUnitPriceOff() {
    return this.methodOfReduction.equals(REDUCTION_BY_UNIT_PRICE_OFF);
  }

  /**
   * This method is used to check whether reduction is by total price off
   * @return boolean
   */
  public boolean isReductionByTotalPriceOff() {
    return this.methodOfReduction.equals(REDUCTION_BY_TOTAL_PRICE_OFF);
  }

  /**
   * This method is used to check whether reduction is by percent off
   * @return boolean
   */
  public boolean isReductionByPercentageOff() {
    return this.methodOfReduction.equals(REDUCTION_BY_PERCENTAGE_OFF);
  }

  /**
   * This method is used to get method of reduction
   * @return String
   */
  public String doGetMethodOfReduction() {
    return this.methodOfReduction;
  }

  /**
   * This method is used to set id
   * @param id String
   */
  public void doSetId(String id) {
    this.id = id;
  } //promotion ID

  /**
   * This method is used to set rule derivation id
   * @param id String
   */
  public void doSetRuleDrvId(String ruleDrvId) {
    this.ruleDrvId = ruleDrvId;
  }

  /**
   * This method is used to set description of the promotion
   * @param description String
   */
  public void doSetDescription(String description) {
    this.description = description;
  }

  /**
   * This method is used to set name of the promotion
   * @param promotionName String
   */
  public void doSetPromotionName(String promotionName) {
    this.promotionName = promotionName;
  }

  /**
   * This method is used to set begin time of promotion
   * @param beginTime Calendar
   */
  public void doSetBeginTime(Calendar beginTime) {
    this.beginTime = beginTime;
  }

  /**
   * This method is used to set end time of promotion
   * @param endTime Calendar
   */
  public void doSetEndTime(Calendar endTime) {
    this.endTime = endTime;
  }

  /**
   * This method is used to set reduction amount
   * @param reductionAmount Currency
   */
  public void doSetReductionAmount(ArmCurrency reductionAmount) {
    this.reductionAmount = reductionAmount;
  }

  /**
   * This method is used to set reduction percent
   * @param reductionPercent double
   */
  public void doSetReductionPercent(double reductionPercent) {
    this.reductionPercent = reductionPercent;
  }

  /**
   * This method is used to set discount
   * @param isDiscount boolean
   */
  public void doSetIsDiscount(boolean isDiscount) {
    this.isDiscount = isDiscount;
  }

  /**
   * This method is used to set applicable item for the promotion
   * @param applicableItm boolean
   */
  public void doSetApplicableItm(String applicableItm) {
    this.applicableItm = applicableItm;
  }

  /**
   *
   * @param idStrRt String
   */
  public void doSetIdStrRt(String idStrRt) {
    this.idStrRt = idStrRt;
  } //store Id

  /**
   * This method is used to set discount type
   * @param discountType String
   */
  public void doSetDiscountType(String discountType) {
    this.discountType = discountType;
  }

  /**
   * This method is used to set promotion apply to
   * @param applyTo String
   */
  public void doSetApplyTo(String applyTo) {
    this.applyTo = applyTo;
  }

  /**
   * This method is used to set promotion number
   * @param promotionNum String
   */
  public void doSetPromotionNum(String promotionNum) {
    this.promotionNum = promotionNum;
  } //id_prm

  /**
   * This method is used to get end date of promotion
   * @param startDate Date
   */
  public void doSetStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   * This method is used to set end date of promotion
   * @param endDate Date
   */
  public void doSetEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * This method is used check whether calling object is equal to passed object
   * @param anObject Object
   * @return boolean
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof CMSPromotion) {
      CMSPromotion other = (CMSPromotion)anObject;
      if (!this.id.equals(other.id))
        return false;
      else
        return true;
    }
    return false;
  }

  /**
   *
   * @return int
   */
  public int hashCode() {
    return this.id.hashCode();
  }

  /**
   *
   * @return String
   */
  public String toString() {
    return super.toString() + " " + this.description + " (" + this.id + ")";
  }
}

