/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer;

import java.awt.Color;
import java.awt.Font;
import com.ga.cs.swing.table.cellrenderer.action.BoldRenderAction;
import com.ga.cs.swing.table.cellrenderer.action.DefaultStringRenderAction;
import com.ga.cs.swing.table.cellrenderer.action.FontChangeRenderAction;
import com.ga.cs.swing.table.cellrenderer.action.HorizontalAlignmentRenderAction;
import com.ga.cs.swing.table.cellrenderer.action.RenderActionInterface;
import com.ga.cs.swing.table.cellrenderer.action.UnderlineRenderAction;


/**
 * MultipleRowCellRenderer Helper class
 * @author fbulah
 *
 */
public class MultipleRowCellRendererHelper2 {

  /**
   *
   */
  private MultipleRowCellRendererHelper2() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static MultipleRowCellRenderer createMultipleRowCellRenderer() {
    return new MultipleRowCellRenderer();
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   */
  public static void setDefaultStringAllRowsCellRendererAction(MultipleRowCellRenderer cellRenderer) {
    setAllRowsCellRendererAction(cellRenderer, new DefaultStringRenderAction());
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param allRowsCellRenderAction
   */
  public static void setAllRowsCellRendererAction(MultipleRowCellRenderer cellRenderer
      , RenderActionInterface allRowsCellRenderAction) {
    cellRenderer.setAllRowsRenderAction(allRowsCellRenderAction);
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param row
   */
  public static void addDefaultStringCellRendererAction(MultipleRowCellRenderer cellRenderer
      , int row) {
    cellRenderer.addRowRenderAction(row, new DefaultStringRenderAction());
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param row
   * @param boldFont
   */
  public static void addBoldUnderlineCellRendererAction(MultipleRowCellRenderer cellRenderer
      , int row, Font boldFont) {
    addBoldCellRendererAction(cellRenderer, row, boldFont);
    addUnderlineCellRendererAction(cellRenderer, row);
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param row
   * @param boldFont
   */
  public static void addBoldCellRendererAction(MultipleRowCellRenderer cellRenderer, int row
      , Font boldFont) {
    cellRenderer.addRowRenderAction(row, new BoldRenderAction(boldFont));
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param row
   */
  public static void addUnderlineCellRendererAction(MultipleRowCellRenderer cellRenderer, int row) {
    cellRenderer.addRowRenderAction(row, new UnderlineRenderAction());
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param row
   * @param col
   * @param Thick
   */
  public static void addUnderlineCellRendererActionChoose(MultipleRowCellRenderer cellRenderer
      , int row, Color col, int Thick) {
    UnderlineRenderAction action = new UnderlineRenderAction();
    action.setUnderlineThickness(Thick);
    action.setunderlineColor(col);
    cellRenderer.addRowRenderAction(row, action);
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param row
   * @param font
   */
  public static void addFontChangeCellRendererAction(MultipleRowCellRenderer cellRenderer, int row
      , Font font) {
    cellRenderer.addRowRenderAction(row, new FontChangeRenderAction(font));
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param col
   * @param alignment
   */
  public static void addHorizontalAlignmentCellRendererAction(MultipleRowCellRenderer cellRenderer
      , int col, int alignment) {
    cellRenderer.addColRenderAction(col, new HorizontalAlignmentRenderAction(alignment));
  }

  /**
   * put your documentation comment here
   * @param cellRenderer
   * @param col
   * @param font
   */
  public static void addHorizontalFontAction(MultipleRowCellRenderer cellRenderer, int col
      , Font font) {
    cellRenderer.addColRenderAction(col, new FontChangeRenderAction(font));
  }
}

