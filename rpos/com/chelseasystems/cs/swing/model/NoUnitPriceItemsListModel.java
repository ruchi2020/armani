/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 10/17/2006 | Sandhya   | N/A       | Displays items with zero unit price          |
 +------+------------+-----------+-----------+----------------------------------------------+
 */

package com.chelseasystems.cs.swing.model;

import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.item.CMSItem;

/**
 */
public class NoUnitPriceItemsListModel extends ScrollableTableModel {
  private static final long serialVersionUID = 0;	
  private Vector itemVec;

  /**
   */
  public NoUnitPriceItemsListModel() {
    this(new String[] {ResourceManager.getResourceBundle().getString("SKU")
        , ResourceManager.getResourceBundle().getString("Description")
        , ResourceManager.getResourceBundle().getString("Price")
    });
  }

  /**
   * Make a model with set column headers
   * @param sIdentifiers String[]
   */
  public NoUnitPriceItemsListModel(String sIdentifiers[]) {
	itemVec = new Vector();
    setColumnIdentifiers(sIdentifiers);
  }

  /**
   * @param item
   */
  public void addItem(CMSItem item) {
    addRow(item);
    itemVec.add(item);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 3;
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
    Vector vTemp = this.getCurrentPage();
    CMSItem item = (CMSItem)vTemp.elementAt(row);
    switch (column) {
      case 0:
        return item.getId();
      case 1:
        return item.getDescription();
      case 2:
        return item.getRetailPrice().getFormattedStringValue();
      default:
        return " ";
    }
  }

  /**
   * @param table
   */
  public void setColumnWidth(JTable table) {
    TableColumn ShtNameCol = table.getColumnModel().getColumn(0);
    ShtNameCol.setPreferredWidth(115);
    TableColumn FNameCol = table.getColumnModel().getColumn(1);
    FNameCol.setPreferredWidth(220);
    TableColumn LNameCol = table.getColumnModel().getColumn(2);
    LNameCol.setPreferredWidth(table.getWidth()
        - (ShtNameCol.getPreferredWidth() + FNameCol.getPreferredWidth()));
  }

  /**
   * put your documentation comment here
   */
  public void sortItems() {
    TreeMap sortColumnMap = new TreeMap();
    CMSItem item = null;
    for (int i = 0; i < itemVec.size(); i++) {
      item = (CMSItem)itemVec.elementAt(i);
      sortColumnMap.put(item.getId(), item);
    }
    if (sortColumnMap.size() > 0) {
      for (int i = 0; i < itemVec.size(); i++) {
        removeRowInModel(itemVec.elementAt(i));
      }
      Vector sortedItemVec = new Vector(sortColumnMap.values());
      for (int i = 0; i < sortedItemVec.size(); i++) {
        item = (CMSItem)sortedItemVec.elementAt(i);
        addRow(item);
      }
      fireTableDataChanged();
    }
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
	itemVec = new Vector();
    super.clear();
  }
}

