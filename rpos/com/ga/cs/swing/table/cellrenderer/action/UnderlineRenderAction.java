/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;


/**
 * Underline render action
 * underlining is not supported by java.awt.Font as of 1.4.*, so it is approximated
 * here by creating a line border at the bottom of each cell and setting the vertical alignment of the text
 * @author fbulah
 *
 */
public class UnderlineRenderAction implements RenderActionInterface {
  protected static final Color UNDERLINE_COLOR = Color.black;
  protected static final int UNDERLINE_THICKNESS = 5;
  protected Color underlineColor = UNDERLINE_COLOR;
  protected int underlineThickness = UNDERLINE_THICKNESS;
  public static final boolean DEBUG = false;

  /**
   * put your documentation comment here
   */
  public UnderlineRenderAction() {
  }

  /**
   * put your documentation comment here
   * @param   Color underlineColor
   */
  public UnderlineRenderAction(Color underlineColor) {
    this.underlineColor = underlineColor;
  }

  /**
   * put your documentation comment here
   * @param   int underlineThickness
   */
  public UnderlineRenderAction(int underlineThickness) {
    this.underlineThickness = underlineThickness;
  }

  /**
   * put your documentation comment here
   * @param   Color underlineColor
   * @param   int underlineThickness
   */
  public UnderlineRenderAction(Color underlineColor, int underlineThickness) {
    this.underlineColor = underlineColor;
    this.underlineThickness = underlineThickness;
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
    //		if (DEBUG) {
    //			System.out.println( "UnderlineRenderAction.renderAction: row=" + row + " col=" + col);
    //		}
    cellComponent.setBorder(BorderFactory.createMatteBorder(0, 0, underlineThickness, 0
        , underlineColor));
    cellComponent.setVerticalTextPosition(SwingConstants.BOTTOM);
  }

  /**
   * @return Returns the underlineColor.
   */
  public Color getUnderlineColor() {
    return underlineColor;
  }

  /**
   * @param underlineColor The underlineColor to set.
   */
  public void setunderlineColor(Color underlineColor) {
    this.underlineColor = underlineColor;
  }

  /**
   * @return Returns the underlineThickness.
   */
  public int getUnderlineThickness() {
    return underlineThickness;
  }

  /**
   * @param underlineThickness The underlineThickness to set.
   */
  public void setUnderlineThickness(int underlineThickness) {
    this.underlineThickness = underlineThickness;
  }

  /* (non-Javadoc)
   * @see com.ga.cs.swing.table.RenderActionInterface#getName()
   */
  public String getName() {
    return "UnderlineRenderAction";
  }
}

