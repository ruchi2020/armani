/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Renderer for a Double valued column with a settable format
 * @author fbulah
 *
 */
public class DoubleValueCellRenderer extends DefaultTableCellRenderer {
  /**
   *
   */
  protected DecimalFormat df = new DecimalFormat("######.##");
  public static boolean DEBUG = true;

  /**
   * put your documentation comment here
   */
  public DoubleValueCellRenderer() {
    super();
  }

  /**
   * put your documentation comment here
   * @param   DecimalFormat df
   */
  public DoubleValueCellRenderer(DecimalFormat df) {
    super();
    this.df = df;
  }

  /**
   * put your documentation comment here
   * @param table
   * @param value
   * @param isSelected
   * @param hasFocus
   * @param row
   * @param column
   * @return
   */
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
      , boolean hasFocus, int row, int column) {
    if (DEBUG) {
      System.out.println("DoubleValueCellRenderer.getTableCellRendererComponent: value=" + value
          + " value.class.name=" + value.getClass().getName() + " row=" + row + " column=" + column);
    }
    setText(df.format((Double)value));
    return this;
  }

  /**
   * @return Returns the df.
   */
  public DecimalFormat getDecimalFormat() {
    return df;
  }

  /**
   * @param df The df to set.
   */
  public void setDecimalFormat(DecimalFormat df) {
    this.df = df;
  }
}

