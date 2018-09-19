/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import javax.swing.JLabel;
import javax.swing.JTable;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.ga.cs.swing.model.ConsolidatedOverShortReportModel;
import com.ga.cs.swing.model.MediaReportModel;
import com.ga.cs.swing.report.mediareport.MediaReportApplet;
import com.ga.cs.swing.table.tablesorter.TableSorter;


/**
 * default string render action: sets label text to (String)value
 * @author fbulah
 *
 */
public class ConsolidatedOverShortReportAllRowsRenderAction implements RenderActionInterface {
  public static final boolean DEBUG = false;

  /**
   * put your documentation comment here
   */
  public ConsolidatedOverShortReportAllRowsRenderAction() {
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
    //		if (DEBUG ) {
    //			
    //			System.out.println( "ConsolidatedOverShortReportAllRowsRenderAction.renderAction: row=" + row + " col=" + col );
    //
    //		}
    TableSorter sorter = (TableSorter)table.getModel();
    ConsolidatedOverShortReportModel model = (ConsolidatedOverShortReportModel)sorter.getTableModel();
    Object valueAt = model.getValueAt(row, col);
    if (valueAt != null) {
      String valueAtString = valueAt.toString();
      cellComponent.setText(valueAtString);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "ConsolidatedOverShortReportAllRowsRenderAction";
  }
}

