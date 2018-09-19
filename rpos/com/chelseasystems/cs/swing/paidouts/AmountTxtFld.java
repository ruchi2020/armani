/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 */


package com.chelseasystems.cs.swing.paidouts;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.appmgr.mask.MaskManager;


/**
 */
public class AmountTxtFld extends JCMSTextField {
  private double maxAmount = 100000.00;
  private IApplicationManager theAppMgr; //reference to the appplication manager
  private boolean isErrDlgShown = false;
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  /**
   * @param    IApplicationManager theAppMgr
   */
  public AmountTxtFld(IApplicationManager theAppMgr) {
    this.theAppMgr = theAppMgr;
//    setDocument(new TextFilter(TextFilter.FLOAT));
    enableEvents(AWTEvent.KEY_EVENT_MASK);
    this.setVerifier(new Verifier());
  }

  /**
   * @param x
   */
  public void setMaxAmount(double x) {
    maxAmount = x;
  }

  /**
   * Methods that validates the entry for 'Amount' text field.
   */
  public boolean verifyAmount() {
    if (isErrDlgShown)
      return false;
//    try {
      if (getText().length() <= 0) {
        isErrDlgShown = true;
        theAppMgr.showErrorDlg(res.getString("A currency amount is required."));
        isErrDlgShown = false;
        requestFocus();
        return false;
      }
      if (MaskManager.getInstance(theAppMgr).getMaskVerify(getText(), theAppMgr.CURRENCY_MASK) == null)
        return false;
//      String amountStr = getText();
//      int index = amountStr.indexOf(".");
//      if (index == -1) {
//        isErrDlgShown = true;
//        theAppMgr.showErrorDlg(res.getString("A currency format is required. (xx.xx)"));
//        isErrDlgShown = false;
//        requestFocus();
//        return false;
//      }
//      if (index >= 0) {
//        String decimalPlaces = amountStr.substring(index);
//        CurrencyType type = ArmCurrency.getBaseCurrencyType();
//        if (decimalPlaces.length() > type.getDecimalPlaces() + 1) {
//          theAppMgr.showErrorDlg(res.getString("A currency format is required. (xx.xx)"));
//          return false;
//        }
//        if (decimalPlaces.length() < type.getDecimalPlaces() + 1) {
//          theAppMgr.showErrorDlg(res.getString("A currency format is required. (xx.xx)"));
//          return false;
//        }
//      }
      double amount = new ArmCurrency(getText()).doubleValue();
      if (amount > maxAmount) {
        isErrDlgShown = true;
        theAppMgr.showErrorDlg(res.getString("Currency over 100,000.00 is not allowed."));
        isErrDlgShown = false;
        requestFocus();
        return false;
      }
//    } catch (NumberFormatException e) {
//      e.printStackTrace();
//      isErrDlgShown = true;
//      theAppMgr.showErrorDlg(res.getString("Invalid entry for 'Amount'. Please re-enter."));
//      isErrDlgShown = false;
//      requestFocus();
//      return false;
//    }
    return true;
  }

  class Verifier extends CMSInputVerifier {

    /**
     * @param component
     * @return
     */
    public boolean verify(JComponent component) {
      return verifyAmount();
    }
  }
}
