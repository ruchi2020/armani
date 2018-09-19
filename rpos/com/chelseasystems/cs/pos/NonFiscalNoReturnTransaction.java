/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.pos.POSLineItemGrouping;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.ReturnTransaction;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.Enumeration;
import com.chelseasystems.cr.currency.CurrencyException;


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
public class NonFiscalNoReturnTransaction extends ReturnTransaction {

  /**
   * put your documentation comment here
   * @param   CompositePOSTransaction aCompositeTransaction
   */
  public NonFiscalNoReturnTransaction(CompositePOSTransaction aCompositeTransaction) {
    super(aCompositeTransaction);
  }

  /**
   * This method is used to create sale transaction line item
   * @param anItem Item
   * @return POSLineItem
   */
  public POSLineItem createLineItem(Item anItem) {
    return new CMSNoReturnLineItem(this, anItem);
  }

  /**
   * This method is used to create sale transaction line item group
   * @param lineItem POSLineItem
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping createLineItemGrouping(POSLineItem lineItem) {
    return new CMSNoReturnLineItemGrouping(lineItem);
  }

  /**
   * This method is used to get net amount of the consignment
   * @return Currency
   */
//  public ArmCurrency getNetAmount() {
//    try {
//      ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
//      for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements(); ) {
//        ArmCurrency aLineItemExtendedNetAmount = ((CMSNoSaleLineItem)aLineItemList.nextElement()).
//            getExtendedRetailAmount();
//        total = total.add(aLineItemExtendedNetAmount);
//      }
//      return total;
//    } catch (CurrencyException anException) {
//      logCurrencyException("getNetAmount", anException);
//    }
//    return null;
//  }

}

