/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.MiscItem;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.rules.*;


/**
 */
public class CMSMiscItem extends MiscItem {

  /**
   * @param    CMSItem item
   */
  public CMSMiscItem(String miscItemId, CMSItem item) {
    super(miscItemId, item);
  }

  public void transferInformationTo(POSLineItem aLineItem) {
    super.transferInformationTo(aLineItem);
    try {
      if (getUnitPrice() != null)
        aLineItem.getItem().setRetailPrice(getUnitPrice());
    } catch (BusinessRuleException ex) {
      ex.printStackTrace();
    }
  }

}
