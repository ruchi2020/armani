/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.ga.cs.swing.model.BaseReportModel;


/**
 * Convenenience wrapper class around JCMSTable.  Encapsulates model and implements simple row operations.
 * @author fbulah
 *
 */
public class ReportTable extends JCMSTable {
  BaseReportModel model;

  /**
   *
   */
  public ReportTable() {
    super();
  }

  /**
   * FIXME: included for backwards compatibility; remove once Media and Transaction Reports
   *        have been converted
   */
  public ReportTable(BaseReportModel model, int rowType) {
    super(model, rowType);
    this.model = model;
  }

  /**
   * put your documentation comment here
   * @param   String[] colhdgs
   * @param   IApplicationManager theAppMgr
   */
  public ReportTable(String[] colhdgs, IApplicationManager theAppMgr) {
    model = new BaseReportModel(colhdgs);
    setModel(model);
    setRowType(JCMSTable.VIEW_ROW);
    setAppMgr(theAppMgr);
  }

  /**
   * put your documentation comment here
   */
  public void clearAllRows() {
    model.clear();
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void addRow(String[] row) {
    for (int i = 0; i < row.length; i++) {
      System.out.println("ReportTable.addRow: row[" + i + "]=" + row[i]);
    }
    model.addList(row);
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void addRowAndRepaint(String[] row) {
    model.addList(row);
    repaint();
  }

  /**
   * @return Returns the model.
   */
  public BaseReportModel getTableModel() {
    return model;
  }

  /**
   * @param model The model to set.
   */
  public void setTableModel(BaseReportModel model) {
    this.model = model;
  }
}

