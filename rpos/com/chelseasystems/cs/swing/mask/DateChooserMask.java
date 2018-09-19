/*
 * put your module comment here
 */

package com.chelseasystems.cs.swing.mask;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.mask.DateMask;
import com.chelseasystems.cs.swing.dlg.DateDlg;
import java.text.ParseException;
import com.chelseasystems.cr.util.ResourceManager;

/**
 * put your documentation comment here
 */
public class DateChooserMask extends DateMask {

  SimpleDateFormat fmt;

  public DateChooserMask() {
    fmt = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    fmt.setLenient(false);
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @param edtValue
   * @return
   */
  public Object validateMask(IApplicationManager theAppMgr, Object edtValue) {
    Object passedIn = null;
    if (edtValue instanceof String && ((String)edtValue).equalsIgnoreCase("C")) {
      DateDlg dlg = new DateDlg(theAppMgr.getParentFrame(), theAppMgr);
      dlg.setVisible(true);
      if (dlg.getDate() != null) {
        //SimpleDateFormat fmt = new SimpleDateFormat(com.chelseasystems.cr.util.ResourceManager.getResourceBundle().getString("M/dd/yyyy"));
        passedIn = fmt.format(dlg.getDate());
      } else {
        return ("");
      }
    } else {
      passedIn = edtValue;
    }
    //passedIn = super.validateMask(theAppMgr, passedIn);
    if (passedIn instanceof String) {
      try {
        passedIn = fmt.parse((String)passedIn);
      } catch (ParseException e) {
        passedIn = null;
        theAppMgr.showErrorDlg(ResourceManager.getResourceBundle().getString("Invalid date. Please provide valid date in mm/dd/yyyy format."));
      }
    }

    if (passedIn instanceof Date) {
      passedIn = com.chelseasystems.cr.util.DateUtil.roundToDay((Date)passedIn);
    }
    return (passedIn);
  }

  /**
   * put your documentation comment here
   * @param initialValue
   * @return
   */
  public boolean validateInitialValueType(Object initialValue) {
    return ((initialValue instanceof String) || (initialValue instanceof Date));
  }

  public String getRenderedValue(Object initialValue)
  {
    if (initialValue != null && initialValue instanceof Date)
      return fmt.format((Date)initialValue);
    //else
    return super.getRenderedValue(initialValue);
  }

}
