/*
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.payment.*;

/**
 *
 * <p>Title: Coupon</p>
 *
 * <p>Description: This class store the information of coupon</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSForeignCash extends Cash implements IRuleEngine {
   /**
   * Default Constructor
   */
  public CMSForeignCash() {
    this(null);
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public CMSForeignCash(String transactionPaymentName) {
    super(transactionPaymentName);
  }

  public String getEODTenderType() {
    if (getAmount() != null) {
      return getAmount().getConvertedFrom().getCurrencyType().getCode() + "_CASH";
    }

    return "";
  }
}
