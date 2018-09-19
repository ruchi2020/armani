/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.logging.LoggingInfo;
import com.chelseasystems.cr.logging.LoggingServices;
import com.ga.cs.swing.report.ReportConstants;


/**
 * Utilities for reports development and debugging
 * @author fbulah
 *
 */
public class ReportUtils {

  /**
   *
   */
  private ReportUtils() {
  }

  /**
   * put your documentation comment here
   * @param message
   */
  public static void logInfoMsg(String message) {
    LoggingServices.getCurrent().logMsg(message);
  }

  /**
   * put your documentation comment here
   * @param message
   */
  public static void logInfoTsMsg(String message) {
    LoggingServices.getCurrent().logMsg(new Date(System.currentTimeMillis()) + " " + message);
  }

  /**
   * put your documentation comment here
   * @param rs
   */
  public static void printResultSetMetadata(ResultSet rs) {
    try {
      ResultSetMetaData rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      System.out.println("printResultSetMetaData: columnCount=" + columnCount);
      for (int colNum = 1; colNum <= columnCount; colNum++) { // in java.sql all column numbers start at 1
        System.out.println("printResultSetMetaData: column[" + colNum + "]: name="
            + rsmd.getColumnName(colNum) + " type=" + rsmd.getColumnTypeName(colNum) + " class="
            + rsmd.getColumnClassName(colNum));
      }
    } catch (SQLException e) {
      System.out.println("printResultSetMetaData: SQLException: msg=" + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param dateString
   * @return
   */
  public static Date parseDate(String dateString) {
    SimpleDateFormat sdf = new SimpleDateFormat(ReportConstants.DATE_FORMAT_SLASH_MDY);
    Date date = sdf.parse(dateString, new ParsePosition(0));
    return date;
  }

  /**
   * put your documentation comment here
   * @param table
   * @param cellRenderer
   */
  public static void setCellRenderer(JTable table, TableCellRenderer cellRenderer) {
    Enumeration enm = table.getColumnModel().getColumns();
    while (enm.hasMoreElements()) {
      TableColumn c = (TableColumn)enm.nextElement();
      c.setCellRenderer(cellRenderer);
    }
  }

  /**
   * put your documentation comment here
   * @param column
   * @param table
   * @param cellRenderer
   */
  public static void setCellRenderer(int column, JTable table, TableCellRenderer cellRenderer) {
    TableColumn c = table.getColumnModel().getColumn(column);
    c.setCellRenderer(cellRenderer);
  }

  /**
   * put your documentation comment here
   * @param args
   */
  public static void main(String[] args) {}
}

