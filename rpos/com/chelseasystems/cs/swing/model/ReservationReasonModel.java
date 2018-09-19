/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

/**
 * <p>Title: ReservationReasonModel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
import javax.swing.table.*;
import java.util.*;
import com.chelseasystems.cr.swing.*;
import javax.swing.*;


/**
 */
public class ReservationReasonModel extends ScrollableTableModel {
  private static Vector vecKeys;

  /**
   */
  public ReservationReasonModel(String sHeader) {
    super(new String[] {sHeader
    });
    vecKeys = new Vector();
    setRowsShown(4);

  }

  /**
   * @param grade
   */
  public void addReservationReason(String sReservationCodeKey, String sReservationCodeLabel) {
    vecKeys.addElement(sReservationCodeKey);
    addRow(sReservationCodeLabel);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 1;
  }

  public int getReservationReasonIndex(String sReason) {
    for (int iCtr = 0; iCtr < vecKeys.size(); iCtr++) {
      if (((String)vecKeys.elementAt(iCtr)).equalsIgnoreCase(sReason))return iCtr;
    }
    return -1;
  }

  /**
   * @param row
   * @return
   */
  public String getSelectedReservationReason(int row) {
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
      System.out.println("No ReservationReason found...");
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
  }
}

