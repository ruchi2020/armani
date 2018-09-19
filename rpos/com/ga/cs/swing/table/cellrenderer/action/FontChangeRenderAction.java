/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;


/**
 * font change render action
 * @author fbulah
 *
 */
public class FontChangeRenderAction implements RenderActionInterface {
  Font font;

  /**
   * put your documentation comment here
   * @param   Font font
   */
  public FontChangeRenderAction(Font font) {
    this.font = font;
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
    cellComponent.setFont(font);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "FontChangeRenderAction";
  }
}

