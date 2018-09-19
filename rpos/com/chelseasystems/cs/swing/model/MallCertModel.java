/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import javax.swing.table.*;
import java.util.*;
import com.chelseasystems.cr.swing.*;
import javax.swing.*;


/**
 */
public class MallCertModel extends ScrollableTableModel {
  private static Vector vecKeys;

  /**
   */
  public MallCertModel() {
    super(new String[] {"Choose the appropriate MallCert Type"
    });
    setRowsShown(4);
  }

  /**
   * @param grade
   */
  public void addMallCert(Hashtable htLabels) {
    String sTmp;
    vecKeys = new Vector();
    for (Enumeration e = htLabels.keys(); e.hasMoreElements(); ) {
      sTmp = (String)e.nextElement();
      vecKeys.addElement(sTmp);
      addRow(htLabels.get(sTmp));
    }
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 1;
  }

  /**
   * @param row
   * @return
   */
  public String getSelectedMallCert(int row) {
    return (String)vecKeys.elementAt(row);
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = getCurrentPage();
    String type = (String)vTemp.elementAt(row);
    if (type == null) {
      System.out.println("MallCert is null...");
      return "";
    }
    switch (column) {
      case 0:
        return type;
      default:
        return "";
    }
  }

  /**
   * @param table
   */
  public void setColumnWidth(JTable table) {
    TableColumn acctCol = table.getColumnModel().getColumn(0);
    acctCol.setWidth(table.getWidth());
        /*TableColumn nameCol = table.getColumnModel().getColumn(1);
         nameCol.setWidth(table.getWidth() - (acctCol.getWidth() + 5));*/

  }
}

