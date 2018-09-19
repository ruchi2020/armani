/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.logging;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import com.chelseasystems.cr.logging.LoggingFileServices;
import com.chelseasystems.cr.config.FileMgr;


/**
 * put your documentation comment here
 */
public class CMSLoggingFileServices extends LoggingFileServices {
  private String logFile;

  /**
   * put your documentation comment here
   */
  public CMSLoggingFileServices() {
    super();
  }

  /**
   * put your documentation comment here
   * @param logFile
   */
  public void setLogFile(String logFile) {
    this.logFile = logFile;
    super.setLogFile(logFile);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getLogFile() {
    return this.logFile;
  }
}

