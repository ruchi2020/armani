/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import java.util.Vector;
import com.chelseasystems.cr.swing.ScrollableTableModel;


/**
 * Abstract base class TableModel for GA Reports
 * @author fbulah
 *
 */
public class BaseVectorReportModel extends BaseReportModel {

  /**
   */
  public BaseVectorReportModel() {
  }

  /**
   * put your documentation comment here
   * @param   String[] colhdgs
   */
  public BaseVectorReportModel(String[] colhdgs) {
    super(colhdgs);
    ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(colhdgs);
  }

  /**
   * @param list
   */
  public void addList(String[] list) {
    addRow((Object)list);
  }

  /**
   * @param object
   */
  private void addRow(Object object) {
    // TODO Auto-generated method stub
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
    Vector list = (Vector)vTemp.elementAt(row);
    return list.get(column);
  }

  /**
   * @return
   */
  private Vector getCurrentPage() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getAbsoluteValueAt(int row, int column) {
    Vector vTemp = this.getAllRows();
    Vector list = (Vector)vTemp.elementAt(row);
    return list.get(column);
  }

  /**
   * @return
   */
  private Vector getAllRows() {
    // TODO Auto-generated method stub
    return null;
  }
}

