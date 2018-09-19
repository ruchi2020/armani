/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import javax.swing.table.*;
import java.util.*;
import com.chelseasystems.cr.swing.*;
import java.text.*;
import com.chelseasystems.cr.appmgr.*;
import javax.swing.*;


/**
 */
public class CouponModel extends ScrollableTableModel {
  private Vector vecKeys;

  /**
   */
  public CouponModel() {
    super(new String[] {"Choose the appropriate Coupon Type"
    });
    setRowsShown(4);
  }

  /**
   * @param grade
   */
  public void addCoupon(Hashtable htLabels) {
    String sTmp;
    vecKeys = new Vector();
    for (Enumeration e = htLabels.keys(); e.hasMoreElements(); ) {
      sTmp = (String)e.nextElement();
      vecKeys.addElement(sTmp);
      addRow(htLabels.get(sTmp));
    }
    //   	  htClasses =htLabels;
    //   	  Vector list = new Vector();
    //   	  for (Enumeration e = htLabels.keys() ; e.hasMoreElements() ;) {
    //   	  list.addElement(e.nextElement());
    //   	  }
    //		String[] displayValues = (String[])list.toArray(new String[htLabels.size()]);
    //   	  //String[] displayValues = (String[])htLabels.keys().toArray(new String[htLabels.size()]);
    //
    //   	  for(int i=0;i<displayValues.length;i++){
    //   	  addRow(displayValues[i]);
    //   	  }
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
  public String getSelectedCoupon(int row) {
    return (String)getRowInPage(row);
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
      System.out.println("Coupon is null...");
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

