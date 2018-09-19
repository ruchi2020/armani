/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.ga.cs.currency.ComparableCurrency;
import com.ga.cs.swing.model.TransactionReportModel;
import com.ga.cs.swing.report.transactionanalysis.TransactionReportApplet;
import com.ga.cs.swing.table.tablesorter.TableSorter;


/**
 * Render action for data rows of Transaction Analysis Report
 * @author fbulah
 *
 */
public class TransactionReportDataRowsRenderAction implements RenderActionInterface {
  protected static final DecimalFormat df = new DecimalFormat("######.##");

  /**
   * put your documentation comment here
   */
  public TransactionReportDataRowsRenderAction() {
  }

  /**
   * put your documentation comment here
   * @param cellComponent
   * @param table
   * @param value
   * @param isSelected
   * @param hasFocus
   * @param row
   * @param col
   */
  public void renderAction(JLabel cellComponent, JTable table, Object value, boolean isSelected
      , boolean hasFocus, int row, int col) {
    //System.out.println( "TransactionReportDataRowsRenderAction.renderAction: row=" + row + " col=" + col );
    TableSorter sorter = (TableSorter)table.getModel();
    if (value instanceof Double) {
      cellComponent.setText(df.format((Double)sorter.getValueAt(row, col)));
    } else {
      cellComponent.setText(sorter.getValueAt(row, col).toString());
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "TransactionReportDataRowsRenderAction";
  }
}

