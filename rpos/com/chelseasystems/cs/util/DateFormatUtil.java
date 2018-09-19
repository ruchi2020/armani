/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 10/26/2005 | ??        |           |                                                    |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import java.util.Date;
import java.text.*;
import java.util.Locale;

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
public class DateFormatUtil {

  static private java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  static private SimpleDateFormat dateDisplayFormat = null;
  static private SimpleDateFormat dateTimeDisplayFormat = null;

  // Default Constructor
  public DateFormatUtil() {
  }

  // Fimd the localized display format string from the resource bundle and construct the SimpleDateFormat
  // object to be used by the client
  static public SimpleDateFormat getLocalDateFormat() {
//    if (dateDisplayFormat != null) return dateDisplayFormat;

    String dateFormatString = com.chelseasystems.cr.util.ResourceManager.getResourceBundle().getString("DATE_DISPLAY_FORMAT");
    if (dateFormatString.equals("DATE_DISPLAY_FORMAT")) {
      // Not defined in resource bundle .. return default
      dateFormatString = "MM/dd/yyyy";
    }

    dateDisplayFormat = new SimpleDateFormat(dateFormatString);
    dateDisplayFormat.setLenient(false);
    return dateDisplayFormat;
  }

  // Fimd the localized display format string from the resource bundle and construct the SimpleDateFormat
  // object to be used by the client
  static public SimpleDateFormat getLocalDateTimeFormat() {
    //if (dateTimeDisplayFormat != null)return dateTimeDisplayFormat;

    String dateTimeFormatString = com.chelseasystems.cr.util.ResourceManager.getResourceBundle().getString("DATETIME_DISPLAY_FORMAT");
    if (dateTimeFormatString.equals("DATETIME_DISPLAY_FORMAT")) {
      // Not defined in resource bundle .. return default
      dateTimeFormatString = "MM/dd/yyyy HH:mm a";
    }

    dateTimeDisplayFormat = new SimpleDateFormat(dateTimeFormatString);
    dateTimeDisplayFormat.setLenient(false);
    return dateTimeDisplayFormat;
  }

  static public Date getFormattedDate(String sDateToFormat) throws Exception {
    Date aDate = null;
    try {
      if (dateDisplayFormat == null) dateDisplayFormat = getLocalDateFormat();
      aDate = dateDisplayFormat.parse(sDateToFormat);
    }
    catch (Exception e) {
      throw new Exception("Invalid Date Format.");
    }
    return aDate;
  }

  static public String getFormattedDateString(Date aDate) {
    if (dateDisplayFormat == null) dateDisplayFormat = getLocalDateFormat();
    return dateDisplayFormat.format(aDate);
  }

}
