/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import javax.swing.JLabel;
import javax.swing.JTable;


/**
 * Horizontal alignment render action
 * @author fbulah
 *
 */
public class HorizontalAlignmentRenderAction implements RenderActionInterface {
  int alignment;

  /**
   * put your documentation comment here
   * @param   int alignment
   */
  public HorizontalAlignmentRenderAction(int alignment) {
    this.alignment = alignment;
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
    cellComponent.setHorizontalAlignment(alignment);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "HorizontalAlignmentRenderAction";
  }
}

