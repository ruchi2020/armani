/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;


/**
 * Abstract base class TableModel for GA Reports
 *
 * @author fbulah
 *
 */
public class BaseReportModel extends DefaultTableModel {
  private String fullTitle = "TITLE NOT SET";

  /**
   */
  public BaseReportModel() {
  }

  /**
   * put your documentation comment here
   * @param   String[] colhdgs
   */
  public BaseReportModel(String[] colhdgs) {
    super(colhdgs, 0);
    ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(colhdgs);
  }

  /**
   * @param list
   */
  public void addList(String[] list) {
    addRow(new Vector(Arrays.asList(list)));
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.swing.table.TableModel#getColumnClass(int)
   */
  public Class getColumnClass(int columnIndex) {
    return getValueAt(0, columnIndex).getClass();
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
    while (getRowCount() > 0) {
      removeRow(0);
    }
  }

  /**
   * @return Returns the fullTitle.
   */
  public String getFullTitle() {
    return fullTitle;
  }

  /**
   * @param fullTitle
   *            The fullTitle to set.
   */
  public void setFullTitle(String fullTitle) {
    this.fullTitle = fullTitle;
  }
}

