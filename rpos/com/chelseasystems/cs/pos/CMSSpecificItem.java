/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.SpecificItem;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.pos.POSLineItem;


/**
 */
public class CMSSpecificItem extends SpecificItem {
  private String manualAuthCode;

  // vishal yevale to print reloaded giftcard balance
  private ArmCurrency giftCardBalance;
  
  public ArmCurrency getGiftCardBalance() {
	return giftCardBalance;
}

public void setGiftCardBalance(ArmCurrency giftCardBalance) {
	this.giftCardBalance = giftCardBalance;
}
// end 10 nov 2016
/**
   * @param    CMSItem item
   */
  public CMSSpecificItem(CMSItem item) {
    super(item);
  }

  /**
   *
   * @param aLineItem POSLineItem
   */
  public void transferInformationTo(POSLineItem aLineItem) {
    super.transferInformationTo(aLineItem);
    if(getUnitPrice() != null) {
        try {
        	// Himani: Added a check to allow multiple giftcards with different unit price in sale line item. 
        	if(!aLineItem.getItem().getDescription().equals("Gift Card"))
        		aLineItem.setItemPriceOverride(getUnitPrice());
        } catch(Exception e) {

        }
    }
    if (aLineItem.getLineItemDetailsArray().length > 0
        && aLineItem.getLineItemDetailsArray()[0] instanceof CMSSaleLineItemDetail) {
      ((CMSSaleLineItemDetail)aLineItem.getLineItemDetailsArray()[0]).setManualAuthCode(
          manualAuthCode);
    }
  }

  /**
   *
   * @return String
   */
  public String getManualAuthCode() {
    return manualAuthCode;
  }

  /**
   *
   * @param manualAuthCode String
   */
  public void setManualAuthCode(String manualAuthCode) {
    this.manualAuthCode = manualAuthCode;
  }
}

