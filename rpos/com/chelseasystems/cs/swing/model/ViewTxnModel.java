/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import javax.swing.table.*;
import java.util.*;
import javax.swing.event.TableModelEvent;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.paidouts.*;


/**
 */
public class ViewTxnModel extends ScrollableTableModel {

  /**
   */
  public ViewTxnModel() {
    this.setColumnIdentifiers(new String[] {" ", " ", " ", " "
    });
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 4;
  }

  /**
   * @param value
   */
  public void addSearchButton(SearchSelection value) {
    //System.out.println("adding search button: " + value.SearchLabel);
    int row = getRowCount();
    if (row == 0) {
      //System.out.println("adding as first row");
      SearchSelection[] values = allocRow();
      values[0] = value;
      addRow((Object)values);
      return;
    }
    boolean newRow = true;
    SearchSelection[] values = (SearchSelection[])getRowInPage(row - 1);
    for (int x = 0; x < values.length; x++) {
      if (values[x].SearchLabel.equals(" ")) {
        values[x] = value;
        newRow = false;
        break;
      }
    }
    if (newRow) {
      SearchSelection[] newRows = allocRow();
      newRows[0] = value;
      addRow((Object)newRows);
    }
    this.fireTableRowsUpdated(getRowCount() - 1, getRowCount() - 1);
  }

  /**
   * @param row
   * @param col
   * @return
   */
  public SearchSelection getSearchButton(int row, int col) {
    SearchSelection[] values = (SearchSelection[])getRowInPage(row);
    if (values[col] == null)
      return null;
    else
      return values[col];
  }

  /**
   * @return
   */
  public SearchSelection[] allocRow() {
    SearchSelection[] result = new SearchSelection[getColumnCount()];
    for (int x = 0; x < getColumnCount(); x++) {
      result[x] = new SearchSelection();
    }
    return result;
  }

  /**
   * @param row
   * @param col
   */
  public void removeCell(int row, int col) {
    SearchSelection[] thisRow = (SearchSelection[])getRowInPage(row);
    thisRow[col] = null;
    pack();
  }

  /**
   */
  private void pack() {
    Vector vRows = this.getAllRows();
    clear();
    for (Enumeration enm = vRows.elements(); enm.hasMoreElements(); ) {
      SearchSelection[] row = (SearchSelection[])enm.nextElement();
      for (int y = 0; y < row.length; y++) {
        if (row[y] != null)
          addRow(row[y]);
      }
    }
    this.fireTableDataChanged();
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
    SearchSelection[] values = (SearchSelection[])vTemp.elementAt(row);
    if (values[column] != null) {
      return values[column].SearchLabel; //  what string to display in table
    } else
      return " ";
  }

  public class SearchSelection {
    public String SearchLabel = null;
    public String SearchPredicate = null;

    /**
     */
    public SearchSelection() {
      SearchLabel = new String(" ");
      SearchPredicate = new String(" ");
    }
  }
} // end of class

