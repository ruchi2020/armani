/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.appmgr.mask;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cr.appmgr.mask.*;
import java.text.*;
import com.chelseasystems.cr.currency.CurrencyFormat;
import com.chelseasystems.cr.config.ConfigMgr;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 09-27-2005 | Manpreet  |  N/A      | Initial Version                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 * <p>Title:CMSCurrencyMask </p>
 * <p>Description: ArmCurrency mask for European money</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:Skillnet Inc. </p>
 * @author Manpreet S Bawa.
 * @version 1.0
 */
public class CMSCurrencyMask extends MaskGenericRenderer implements IInitializedMask {
  private IApplicationManager theAppMgr;
  private CurrencyType cType;
  private double dAmount = 0.0d;

  /**
   * put your documentation comment here
   */
  public CMSCurrencyMask() {
    init();
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @param edtValue
   * @return
   */
  public Object validateMask(IApplicationManager theAppMgr, Object edtValue) {
    this.theAppMgr = theAppMgr;
    String aNumber = edtValue.toString().trim();
    if(aNumber.length()==0)
      return null;
    try {
      return testMask(aNumber) ? new ArmCurrency(cType, dAmount) : null;
    } catch (Exception e) {}
    return null;
  }

  /**
   * put your documentation comment here
   */
  public void init() {
    cType = ArmCurrency.getBaseCurrencyType();
  }

  /**
   * put your documentation comment here
   * @param value
   * @return
   */
  private boolean testMask(String value) {
   java.util.ResourceBundle resBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    try {
      ConfigMgr currencyConfig = new ConfigMgr("currency.cfg");
      String key = "FORMAT_" + cType.getCode() + "_" + cType.getLocale().getLanguage() + "_"
          + cType.getLocale().getCountry();
      String decimalSeparator = currencyConfig.getString(key + ".DECIMAL_SEPARATOR");
      String groupSeparator = currencyConfig.getString(key + ".GROUP_SEPARATOR");
      DecimalFormatSymbols dfs = new DecimalFormatSymbols(cType.getLocale());
      if (groupSeparator == null)
        groupSeparator = String.valueOf(dfs.getGroupingSeparator());
      if (decimalSeparator == null)
        decimalSeparator = String.valueOf(dfs.getMonetaryDecimalSeparator());
      int index = -1;
      index = value.indexOf(decimalSeparator);
      if (index >= 0) {
        String decimalPlaces = value.substring(index + 1);
        if (decimalPlaces.length() > cType.getDecimalPlaces()) {
          theAppMgr.showErrorDlg(resBundle.getString("The currency has too many decimal places."));
          return false;
        }
      }
      StringBuffer buff = new StringBuffer(value);
      for (int i = value.length() - 1; i >= 0; i--) {

        if (buff.charAt(i) == groupSeparator.charAt(0)) {
          buff.deleteCharAt(i);
        } else if(Character.getType(buff.charAt(i)) == 26 && (i==0 || i==buff.length()-1)) { //first or last char may be currency symbol
          buff.deleteCharAt(i);
        }
      }
      if(buff.toString().trim().length()==0)
        return false;
      NumberFormat nf = NumberFormat.getNumberInstance(cType.getLocale());
      if (nf instanceof DecimalFormat) {
        DecimalFormatSymbols localizedDFS = ((DecimalFormat)nf).getDecimalFormatSymbols();
        if (decimalSeparator != null)
          localizedDFS.setDecimalSeparator(decimalSeparator.charAt(0));
        if (groupSeparator != null)
          localizedDFS.setGroupingSeparator(groupSeparator.charAt(0));
        ((DecimalFormat)nf).setDecimalFormatSymbols(localizedDFS);
      }
      dAmount = nf.parse(buff.toString().trim()).doubleValue();
      if (dAmount < 0.0D) {
        theAppMgr.showErrorDlg(resBundle.getString("Negative currency is not allowed."));
        return false;
      } else {
        return true;
      }
    } catch (Exception ex) {
      theAppMgr.showErrorDlg(resBundle.getString("The value is not a valid currency."));
    }
    return false;
  }

  /**
   * put your documentation comment here
   * @param initialValue
   * @return
   */
  public boolean validateInitialValueType(Object initialValue) {
    if (initialValue instanceof ArmCurrency) {
      cType = ((ArmCurrency)initialValue).getCurrencyType();
      return true;
    } else {
      return false;
    }
  }
}
