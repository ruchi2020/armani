/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.util;

import java.util.*;
import java.text.SimpleDateFormat;


/**
 * StandardDateFormat.
 * This class contains date formats with standard patterns defined in ISO 8601.<p>
 * International Standard ISO 8601 specifies numeric representations of date
 * and time. This standard notation helps to avoid confusion in international
 * communication caused by the many different national notations and increases
 * the portability of computer user interfaces.
 *
 */
public class StandardDateFormat {
  /**
   * Date Format with pattern yyyy
   */
  static public final String YEAR_FORMAT = "yyyy";
  /**
   * Date Format with pattern yyyy-MM
   */
  static public final String MONTH_FORMAT = "yyyy-MM";
  /**
   * Date Format with pattern yyyy-'W'ww
   */
  static public final String WEEK_FORMAT = "yyyy-'W'ww";
  /**
   * Date Format with pattern yyyy-MM-dd
   */
  static public final String DATE_FORMAT = "yyyy-MM-dd";
  /**
   * Date Format with pattern yyyy-MM-ddTHH:mm:ss.SSS
   */
  static public final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  /**
   * Date Format with pattern yyyy-MM-ddTHH:mm:ss.SSS z<p>
   * Here z is the timezone as defined in java.text.SimpleDateFormat.  It is not part of ISO 8601
   */
  static public final String TIME_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS z";

  /**
   * Converts a date to a format string - yyyy
   * @param date
   * @return a format string in yyyy
   */
  static public final String formatYear(Date date) {
    return new SimpleDateFormat(YEAR_FORMAT).format(date);
  }

  /**
   * Converts a date to a format string - yyyy-MM
   * @param date
   * @return a format string in yyyy-MM
   */
  static public final String formatMonth(Date date) {
    return new SimpleDateFormat(MONTH_FORMAT).format(date);
  }

  /**
   * Converts a date to a format string - yyyy-Www
   * @param date
   * @return a format string in yyyy-Www
   */
  static public final String formatWeek(Date date) {
    return new SimpleDateFormat(WEEK_FORMAT).format(date);
  }

  /**
   * Converts a date to a format string - yyyy-MM-dd
   * @param date
   * @return a format string in yyyy-MM-dd
   */
  static public final String formatDate(Date date) {
    return new SimpleDateFormat(DATE_FORMAT).format(date);
  }

  /**
   * Converts a date to a format string - yyyy-MM-dd'T'HH:mm:ss.SSS
   * @param date
   * @return a format string in yyyy-MM-dd'T'HH:mm:ss.SSS
   */
  static public final String formatTime(Date date) {
    return new SimpleDateFormat(TIME_FORMAT).format(date);
  }

  /**
   * Converts a date to a format string - yyyy-MM-dd'T'HH:mm:ss.SSS z<p>
   * Here z is the timezone as defined in java.text.SimpleDateFormat.  It is not part of ISO 8601
   * @param date
   * @return a format string in yyyy-MM-dd'T'HH:mm:ss.SSS
   */
  static public final String formatTimeWithTimezone(Date date) {
    return new SimpleDateFormat(TIME_TIMEZONE_FORMAT).format(date);
  }

  /**
   * Converts current date to a format string - yyyy
   * @return a format string in yyyy
   */
  static public final String formatYear() {
    return formatYear(new Date());
  }

  /**
   * Converts current date to a format string - yyyy-MM
   * @return a format string in yyyy-MM
   */
  static public final String formatMonth() {
    return formatMonth(new Date());
  }

  /**
   * Converts current date to a format string - yyyy-Www
   * @return a format string in yyyy-Www
   */
  static public final String formatWeek() {
    return formatWeek(new Date());
  }

  /**
   * Converts current date to a format string - yyyy-MM-dd
   * @return a format string in yyyy-MM-dd
   */
  static public final String formatDate() {
    return formatDate(new Date());
  }

  /**
   * Converts current date to a format string - yyyy-MM-dd'T'HH:mm:ss.SSS
   * @return a format string in yyyy-MM-dd'T'HH:mm:ss.SSS
   */
  static public final String formatTime() {
    return formatTime(new Date());
  }

  /**
   * Converts a date to a format string - yyyy-MM-dd'T'HH:mm:ss.SSS z<p>
   * Here z is the timezone as defined in java.text.SimpleDateFormat.  It is not part of ISO 8601
   * @return a format string in yyyy-MM-dd'T'HH:mm:ss.SSS z
   */
  static public final String formatTimeWithTimezone() {
    return formatTimeWithTimezone(new Date());
  }

  /**
   * Converts a formatted sting in yyyy-MM-dd to a java.util.Date object
   * @param formattedString
   * @return a Date object
   * @throws ParseException
   */
  static public final Date parseUsingDateFormat(String formattedString)
      throws java.text.ParseException {
    return new SimpleDateFormat(DATE_FORMAT).parse(formattedString);
  }

  /**
   * Converts a formatted sting in yyyy-MM-dd'T'HH:mm:ss.SSS to a java.util.Date object
   * @param formattedString
   * @return a Date object
   * @throws ParseException
   */
  static public final Date parseUsingTimeFormat(String formattedString)
      throws java.text.ParseException {
    return new SimpleDateFormat(TIME_FORMAT).parse(formattedString);
  }

  /**
   * Converts a formatted sting in yyyy-MM-dd'T'HH:mm:ss.SSS z to a java.util.Date object
   * @param formattedString
   * @return a Date object
   * @throws ParseException
   */
  static public final Date parseUsingTimeWithTimezoneFormat(String formattedString)
      throws java.text.ParseException {
    return new SimpleDateFormat(TIME_TIMEZONE_FORMAT).parse(formattedString);
  }
}

