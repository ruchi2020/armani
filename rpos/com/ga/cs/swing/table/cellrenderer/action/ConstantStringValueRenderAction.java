/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer.action;

import javax.swing.JLabel;
import javax.swing.JTable;


/**
 * sets cell to constant string value
 * @author fbulah
 *
 */
public class ConstantStringValueRenderAction implements RenderActionInterface {
  public static final String DEFAULT_STRING_VALUE = "";
  protected String stringValue = DEFAULT_STRING_VALUE;
  public static boolean DEBUG = true;

  /**
   * put your documentation comment here
   */
  public ConstantStringValueRenderAction() {
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
    //		if ( DEBUG ) {
    //			System.out.println( "ConstantStringValueRenderAction.renderAction: setting text to <" + stringValue +
    //					            "> for row=" + row + " column=" + col );
    //		}
    cellComponent.setText(stringValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getName() {
    return "ConstantStringValueRenderAction";
  }

  /**
   * @return Returns the stringValue.
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * @param stringValue The stringValue to set.
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }
}

