/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import javax.swing.JLabel;
import javax.swing.JTable;
import com.ga.cs.swing.model.AssocSalesReportModel;
import com.ga.cs.swing.table.tablesorter.TableSorter;


/**
 * default string render action: sets label text to (String)value
 *
 *
 */
public class AssocSalesAllRowsRenderAction implements RenderActionInterface {

  /**
   * put your documentation comment here
   */
  public AssocSalesAllRowsRenderAction() {
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
    //System.out.println( "AssociateSalesAllRowsRenderAction.renderAction: row=" + row + " col=" + col );
    TableSorter sorter = (TableSorter)table.getModel();
    AssocSalesReportModel model = (AssocSalesReportModel)sorter.getTableModel();
    cellComponent.setText(model.getValueAt(row, col).toString());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "AssocSalesReportAllRowsRenderAction";
  }
}

