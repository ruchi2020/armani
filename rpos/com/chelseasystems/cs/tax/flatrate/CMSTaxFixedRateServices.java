/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package com.chelseasystems.cs.tax.flatrate;

import java.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.tax.*;
import com.chelseasystems.cs.tax.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.currency.*;

/**
 */
public class CMSTaxFixedRateServices extends CMSTaxServices {

  public static final String TAX_ENGINE_NAME = TaxTypeKey.TAX_ENGINE_NAME_FIXED_RATE;
  private static final double FIXED_TAX_RATE = new ConfigMgr("tax.cfg").getDouble("FIXED_RATE").
      doubleValue();

  /**
   */
  public CMSTaxFixedRateServices() {
  }

  // Javadoc already defined in ancestor.
  public SaleTax getTaxAmounts(CompositePOSTransaction aCompositeTransaction,
      Store fromStore, Store toStore, Date aProcessDate)
      throws Exception {
    POSLineItemDetail[] taxableLineItemdetails = aCompositeTransaction.
        getTaxableLineItemDetailsArray();
    SaleTaxDetail[] taxDetails = new CMSSaleTaxDetail[taxableLineItemdetails.length];
    double rate = FIXED_TAX_RATE;
    for (int i = 0; i < taxDetails.length; i++) {
      ArmCurrency amount = taxableLineItemdetails[i].getNetAmount().multiply(rate).truncate(0);
      taxDetails[i] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_FIXED_RATE, amount, rate, null, null);
    }

    POSLineItemDetail[] regionalTaxableLineItemdetails = aCompositeTransaction.
        getRegionalTaxableLineItemDetailsArray();
    SaleTaxDetail[] regionalTaxDetails = new SaleTaxDetail[regionalTaxableLineItemdetails.length];
    for (int i = 0; i < regionalTaxDetails.length; i++)
      regionalTaxDetails[i] = new CMSSaleTaxDetail(TaxTypeKey.TAX_KEY_FIXED_RATE, new ArmCurrency(0.0)
          , 0.0, null, null);

    return new SaleTax(TAX_ENGINE_NAME, taxDetails, regionalTaxDetails);
  }
  
  
  // Javadoc already defined in ancestor.
  public SaleTax getTaxAmounts(CompositePOSTransaction aCompositeTransaction,
      Store fromStore, Store toStore, Date aProcessDate, HashMap<String, Object[]> taxDetailMap)
      throws Exception {
	  return getTaxAmounts(aCompositeTransaction,fromStore,toStore,aProcessDate);
  }


  /**
   * put your documentation comment here
   * @param zipCode
   * @return
   * @exception Exception
   */
  public ArmTaxRate[] findByZipCode(String zipCode)
      throws Exception {
    return null;
  }

}
