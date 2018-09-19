/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.SaleLineItemDetail;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.item.CMSItem;


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
public class CMSNoSaleLineItemDetail extends SaleLineItemDetail {

  /**
   *
   * @param aLineItem POSLineItem
   */
  public CMSNoSaleLineItemDetail(POSLineItem aLineItem) {
    super(aLineItem);
  }

  /**
   * This method is used to get the total amount due for the consignment
   * @return Currency
   */
//  public ArmCurrency getTotalAmountDue() {
//    return new ArmCurrency(0.0d);
//  }

  public ArmCurrency getVatAmount() {
      ArmCurrency vatAmount = new ArmCurrency(getBaseCurrencyType(), 0.0d);
      if(this.getLineItem().getItem().getVatRate() != null)
      {
              CMSItem item = (CMSItem)this.getLineItem().getItem();
              double rate = item.getVatRate().doubleValue();
              //vatAmount = this.getLineItem().getItemRetailPrice().multiply((rate) / (1.0D + rate));
              //Fix for 1801: Problem on the NFNS with items with discounts
              vatAmount = this.getLineItem().getExtendedNetAmount().multiply((rate) / (1.0D + rate));
      }
      return vatAmount;
  }
}

