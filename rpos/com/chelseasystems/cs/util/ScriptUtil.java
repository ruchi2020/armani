package com.chelseasystems.cs.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.chelseasystems.cs.logging.CMSLoggingFileServices;

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
public class ScriptUtil {
  public static final String DEFAULT_CURRENT_DATE_TIME_STRING_FORMAT = "yyyyMMddHHmmssS";
  private static CMSLoggingFileServices loggingService;


  private ScriptUtil() {
  }

  public static void main(String[] args) {
    if(args.length < 1) {
      loggingService.logMsg("ERROR in "+ScriptUtil.class.getName()+": Method not specified");
      loggingService.logMsg("USAGE: java ScriptUtil <method> [arguments]");
      loggingService.logMsg("Using default method: CurrentDateTimeString");
      printCurrentDateTimeString(args);
      return;
    }
    if (args[0].trim().equalsIgnoreCase("printCurrentDateTimeString")
        || args[0].trim().equalsIgnoreCase("CurrentDateTimeString"))
      printCurrentDateTimeString(args);
  }

  public static void printCurrentDateTimeString(String args[])
  {
    String dateFormatStr = DEFAULT_CURRENT_DATE_TIME_STRING_FORMAT;
    if(args.length > 1) {
      dateFormatStr = args[1].trim();
    }
    SimpleDateFormat df = null;
    try {
      df = new SimpleDateFormat(dateFormatStr);
    } catch (IllegalArgumentException e){
      loggingService.logMsg("ERROR in "+ScriptUtil.class.getName()+".printCurrentDateTimeString(): Illegal date format specified: " + dateFormatStr);
      loggingService.logMsg("Using date format: " + DEFAULT_CURRENT_DATE_TIME_STRING_FORMAT+"\n");
      df = new SimpleDateFormat(DEFAULT_CURRENT_DATE_TIME_STRING_FORMAT);
    }
    System.out.println(df.format(new Date()));
  }
}
