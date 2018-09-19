/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pricing;

import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.pricing.DiscountPriceEngine;
import com.chelseasystems.cr.pricing.LineItemDetailPriceEngine;


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
public class CMSConsignmentLineItemDetailPriceEngine extends LineItemDetailPriceEngine {

  /**
   * put your documentation comment here
   */
  public CMSConsignmentLineItemDetailPriceEngine() {
  }

  /**
   * createDiscountPriceEngine
   *
   * @param discount Discount
   * @return DiscountPriceEngine
   * @todo Implement this
   *   com.chelseasystems.cr.pricing.LineItemDetailPriceEngine method
   */
  public DiscountPriceEngine createDiscountPriceEngine(Discount discount) {
    return new CMSDiscountPriceEngine(discount);
  }
}

