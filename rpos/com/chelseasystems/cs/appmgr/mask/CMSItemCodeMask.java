/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.appmgr.mask;

import java.util.ResourceBundle;
import com.chelseasystems.cr.appmgr.mask.ItemCodeMask;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.util.ResourceManager;


/**
 * put your documentation comment here
 */
public class CMSItemCodeMask extends ItemCodeMask {

  /**
   * put your documentation comment here
   */
  public CMSItemCodeMask() {
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @param edtValue
   * @return
   */
  public Object validateMask(IApplicationManager theAppMgr, Object edtValue) {
    try {
      ConfigMgr mgr = new ConfigMgr("item.cfg");
      String isAlphaNumericAllowed = mgr.getString("ALLOW_ALPHA_NUMERIC");
      QuantifiedItemId input = new QuantifiedItemId((String)edtValue);
      String itemId = input.getItemId();
      if (itemId.equalsIgnoreCase("S")) {
        return input;
      }
      if (itemId.equalsIgnoreCase("P")) {
        return input;
      }
      if (input.getQtyToApply() < 1) {
        ResourceBundle res = ResourceManager.getResourceBundle();
        theAppMgr.showErrorDlg(res.getString("Quantity cannot be less than 1."));
        return null;
      }
      if (itemId.length() < 4) {
        ResourceBundle res = ResourceManager.getResourceBundle();
        theAppMgr.showErrorDlg(res.getString("Item code must be at least 4 digits."));
        return null;
      }
      for (int i = itemId.length() - 1; i >= 0; i--) {
    //added by anjana to allow alphanumeric look up for EUR
    if(isAlphaNumericAllowed ==null || !isAlphaNumericAllowed.equalsIgnoreCase("Y")){
        if (!Character.isDigit(itemId.charAt(i))) {
          ResourceBundle res = ResourceManager.getResourceBundle();
          theAppMgr.showErrorDlg(res.getString("Item code must be numeric."));
          return null;
        }
    	  }
    }
      return input;
    } catch (Exception ex) {
      ResourceBundle res = ResourceManager.getResourceBundle();
      theAppMgr.showErrorDlg(res.getString("Not a valid item code entry."));
    }
    return null;
  }
}
