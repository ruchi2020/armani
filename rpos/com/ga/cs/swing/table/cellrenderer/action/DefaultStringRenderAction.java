/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import javax.swing.JLabel;
import javax.swing.JTable;


/**
 * default string render action: sets label text to (String)value
 * @author fbulah
 *
 */
public class DefaultStringRenderAction implements RenderActionInterface {

  /**
   * put your documentation comment here
   */
  public DefaultStringRenderAction() {
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
    cellComponent.setText((String)value);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "DefaultStringRenderAction";
  }
}

