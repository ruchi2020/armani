/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.discount.Discount;


/**
 * <p>Title: CMSReduction</p>
 * @author Rajesh Pradhan
 */
public class CMSReduction extends Reduction {
  private Discount discount = null;

  /**
   *
   * @param anAmount Currency
   * @param aReason String
   */
  public CMSReduction(ArmCurrency anAmount, String aReason) {
    super(anAmount, aReason);
  }

  /**
   *
   * @param val Discount
   */
  public void doSetDiscount(Discount val) {
    this.discount = val;
  }

  /**
   *
   * @return Discount
   */
  public Discount getDiscount() {
    return this.discount;
  }
}

