/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.model;

import java.util.ResourceBundle;
import java.util.Vector;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.ga.cs.swing.report.mediareport.MediaReportApplet;
import com.ga.cs.swing.report.transactionanalysis.TransactionReportApplet;


/**
 * TableModel for GA Transaction Analysis Report
 * @author fbulah
 *
 */
public class TransactionReportModel extends BaseReportModel {
  public static final String[] TRANSACTION_REPORT_COLNAMES = TransactionReportApplet.COL_HEADINGS;

  /**
   *
   */
  public TransactionReportModel() {
    ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    initializeColumnIdentifers(res);
  }

  /**
   * @param colhdgs
   */
  public TransactionReportModel(String[] colhdgs) {
    super(colhdgs);
  }

  /**
   */
  public TransactionReportModel(ResourceBundle res) {
    initializeColumnIdentifers(res);
  }

  /**
   * put your documentation comment here
   * @param res
   */
  protected void initializeColumnIdentifers(ResourceBundle res) {
    this.setColumnIdentifiers(new String[] {res.getString("Register"), res.getString("Transactions")
        , res.getString("Units"), res.getString("Sale Amount"), res.getString("Units Per Trans")
        , res.getString("Dollars Per Trans"),
    });
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnClass(int)
   */
  public Class getColumnClass(int columnIndex) {
    return getValueAt(0, columnIndex).getClass();
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return TRANSACTION_REPORT_COLNAMES.length;
  }
}

