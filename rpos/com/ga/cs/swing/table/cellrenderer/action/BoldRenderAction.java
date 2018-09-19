/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;


/**
 * Bolding render action
 * @author fbulah
 *
 */
public class BoldRenderAction implements RenderActionInterface {
  Font boldFont;

  /**
   * put your documentation comment here
   * @param   Font f
   */
  public BoldRenderAction(Font f) {
    String fontName = f.getName();
    int fontSize = f.getSize();
    boldFont = new Font(fontName, Font.BOLD, fontSize);
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
    cellComponent.setFont(boldFont);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "BoldRenderAction";
  }
}

