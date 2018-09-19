/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.discount;

import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.rules.RuleEngine;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import java.util.Vector;


/**
 *
 * <p>Title: CMSEmployeeDiscount</p>
 *
 * <p>Description: This class stores the information regarding employee discount</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEmployeeDiscount extends CMSDiscount {
  private static final long serialVersionUID = -3052687363903040688L;
  private double promoDiscountPercent = 0d;
  private double normalDiscountPercent = 0d;
  boolean isOverridden = false;

  /**
   * This method is used to check whether transaction is valid for discount
   * @param txnPos CompositePOSTransaction
   * @throws BusinessRuleException
   */
  public void isValid(CompositePOSTransaction txnPos)
      throws BusinessRuleException {
    RuleEngine.execute(getClass().getName(), "isValid", txnPos, null);
  }

  /**
   * This method is used to get promotion discount percent for employee
   * @return Double
   */
  public double getPromoDiscountPercent() {
    return this.promoDiscountPercent;
  }

  /**
   * This method is used to set promotion discount percent for employee
   * @param promoDiscountPercent Double
   */
  public void setPromoDiscountPercent(double promoDiscountPercent) {
    doSetPromoDiscountPercent(promoDiscountPercent);
  }

  /**
   * This method is used to set promotion discount percent for employee
   * @param promoDiscountPercent Double
   */
  public void doSetPromoDiscountPercent(double promoDiscountPercent) {
    this.promoDiscountPercent = promoDiscountPercent;
  }

  /**
   * put your documentation comment here
   * @param normalDiscountPercent
   */
  public void setNormalDiscountPercent(double normalDiscountPercent) {
	  doSetNormalDiscountPercent(normalDiscountPercent);
  }

  /**
   * put your documentation comment here
   * @param normalDiscountPercent
   */
  public void doSetNormalDiscountPercent(double normalDiscountPercent) {
    this.normalDiscountPercent = normalDiscountPercent;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double getNormalDiscountPrecent() {
    return normalDiscountPercent;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean getIsOverridden() {
    return this.isOverridden;
  }

  /**
   * put your documentation comment here
   * @param override
   */
  public void setIsOverridden(boolean override) {
    this.isOverridden = override;
  }
}

