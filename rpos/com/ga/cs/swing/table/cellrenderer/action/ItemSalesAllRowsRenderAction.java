/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import com.ga.cs.swing.model.GaItemReportModel;
import com.ga.cs.swing.table.tablesorter.TableSorter;


/**
 * default string render action: sets label text to (String)value
 *
 *
 */
public class ItemSalesAllRowsRenderAction implements RenderActionInterface {

  /**
   * put your documentation comment here
   */
  public ItemSalesAllRowsRenderAction() {
  }

  protected static final DecimalFormat df = new DecimalFormat("######.##");

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
    //System.out.println( "ItemSalesAllRowsRenderAction.renderAction: row=" + row + " col=" + col );
    TableSorter sorter = (TableSorter)table.getModel();
    GaItemReportModel model = (GaItemReportModel)sorter.getTableModel();
    //cellComponent.setText( model.getValueAt(row, col).toString() );
    if (sorter.getValueAt(row, col)!=null){
      if (value instanceof Double) {
        cellComponent.setText(df.format((Double)sorter.getValueAt(row, col)));
      } else {
        cellComponent.setText(sorter.getValueAt(row, col).toString());
      }
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "ItemSalesReportAllRowsRenderAction";
  }
}

