/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.txnnumber;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.mask.IMask;
import com.chelseasystems.cr.appmgr.mask.MaskGenericRenderer;
import com.chelseasystems.cr.util.ResourceManager;


/**
 * put your documentation comment here
 */
public class ArmaniTransIDMask extends MaskGenericRenderer implements IMask {
  private IApplicationManager theAppMgr;
  String finalString;

  /**
   * put your documentation comment here
   */
  public ArmaniTransIDMask() {
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @param edtValue
   * @return
   */
  public Object validateMask(IApplicationManager theAppMgr, Object edtValue) {
    this.theAppMgr = theAppMgr;
    String edtString = reformatForIBM((String)edtValue);
    if (!testMask(edtString))
      return null;
    else {
      edtValue = finalString;
      return edtValue;
    }
  }

  /**
   * put your documentation comment here
   * @param s
   * @return
   */
  private String removeLeadingZeros(String s) {
    if (s != null && s.length() > 1) {
      for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) != '0') {
          return s.substring(i);
        }
      }
    }
    return s;
  }

  /**
   * put your documentation comment here
   * @param value
   * @return
   */
  private String reformatForIBM(String value) {
    if (value.length() >= 13 && value.lastIndexOf("*") == -1 && value.lastIndexOf(".") == -1) {
      System.out.println("IBM barcode found " + value + ", reformatting to standard...");
      String value1 = this.removeLeadingZeros(value.substring(0, 5));
      String value2 = this.removeLeadingZeros(value.substring(5, 10));
      String value3 = this.removeLeadingZeros(value.substring(10));
      return value1 + "*" + value2 + "*" + value3;
    } else {
      return value;
    }
  }

  /**
   * put your documentation comment here
   * @param value
   * @return
   */
  private boolean testMask(String value) {
    try {
      finalString = value.replace('.', '*');
      return true;
    } catch (Exception ex) {
      theAppMgr.showErrorDlg(ResourceManager.getResourceBundle().getString("The value is not a Transaction ID."));
      return false;
    }
  }

  /**
   * put your documentation comment here
   * @param initialValue
   * @return
   */
  public boolean validateInitialValueType(Object initialValue) {
    if (initialValue instanceof String)
      return true;
    else
      return false;
  }
}

