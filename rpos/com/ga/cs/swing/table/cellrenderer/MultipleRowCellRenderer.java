/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import com.ga.cs.swing.table.cellrenderer.action.RenderActionInterface;
import com.ga.cs.swing.table.cellrenderer.action.RenderActionNoOp;


/**
 * TableCellRenderer that changes allows a set of of rendering operations to be applied for a specific set of rows
 * each row can have its own customized set of cell rendering operations
 * @author fbulah
 *
 */
public class MultipleRowCellRenderer extends DefaultTableCellRenderer {
  protected HashMap rowRenderActionsMap = new HashMap();
  protected HashMap colRenderActionsMap = new HashMap();
  protected static final Font plainFont = (new DefaultTableCellRenderer()).
      getTableCellRendererComponent(new JTable(), new Object(), false, false, 0, 0).getFont();

  protected static final Border border = (new DefaultTableCellRenderer()).getBorder();

  protected int horizontalAlignment = SwingConstants.RIGHT;
  protected Color fgColor = Color.black;
  protected static final RenderActionInterface renderActionNoOp = new RenderActionNoOp();
  protected RenderActionInterface allRowsRenderAction = renderActionNoOp;
  public static final boolean DEBUG = false;

  /**
   *
   */
  public MultipleRowCellRenderer() {
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
      System.out.println("getTableCellRendererComponent: ENTRY: row=" + row + " column=" + column);
    }
    // initialize with noraml, plain properties
    setFont(plainFont);
    setForeground(fgColor);
    setBorder(border); // border is reset because underline is currently implemented as matte border 		
    setHorizontalAlignment(horizontalAlignment);
    // apply all rows render action
    allRowsRenderAction.renderAction(this, table, value, isSelected, hasFocus, row, column);
    // apply row renderer actions		
    ArrayList rowRenderActionsList = (ArrayList)rowRenderActionsMap.get(new Integer(row));
    if (rowRenderActionsList != null) {
      Iterator iter = rowRenderActionsList.iterator();
      while (iter.hasNext()) {
        RenderActionInterface ri = (RenderActionInterface)iter.next();
        if (DEBUG) {
          System.out.println("getTableCellRendererComponent: >> invoking row render action "
              + ri.getName() + " for row=" + row + " **");
        }
        ri.renderAction(this, table, value, isSelected, hasFocus, row, column);
      }
    } else {
      if (DEBUG) {
        System.out.println("getTableCellRendererComponent: ** no row render actions for row=" + row
            + " **");
      }
    }
    // apply column render actions  		
    ArrayList colRenderActionsList = (ArrayList)colRenderActionsMap.get(new Integer(column));
    if (colRenderActionsList != null) {
      Iterator iter = colRenderActionsList.iterator();
      while (iter.hasNext()) {
        RenderActionInterface ri = (RenderActionInterface)iter.next();
        if (DEBUG) {
          System.out.println("getTableCellRendererComponent: invoking col render action "
              + ri.getName() + " for column= " + column + " **");
        }
        ri.renderAction(this, table, value, isSelected, hasFocus, row, column);
      }
    } else {
      if (DEBUG) {
        System.out.println("getTableCellRendererComponent: ** no col render actions for column= "
            + column + " **");
      }
    }
    return this;
  }

  /**
   * put your documentation comment here
   * @param row
   * @param ri
   */
  public void addRowRenderAction(int row, RenderActionInterface ri) {
    ArrayList rowRenderActionsList = (ArrayList)rowRenderActionsMap.get(new Integer(row));
    if (rowRenderActionsList == null) {
      rowRenderActionsList = new ArrayList();
      rowRenderActionsMap.put(new Integer(row), rowRenderActionsList);
    }
    rowRenderActionsList.add(ri);
  }

  /**
   * put your documentation comment here
   * @param col
   * @param ri
   */
  public void addColRenderAction(int col, RenderActionInterface ri) {
    ArrayList colRenderActionsList = (ArrayList)colRenderActionsMap.get(new Integer(col));
    if (colRenderActionsList == null) {
      colRenderActionsList = new ArrayList();
      colRenderActionsMap.put(new Integer(col), colRenderActionsList);
    }
    colRenderActionsList.add(ri);
  }

  /**
   * @param allRowsRenderAction The allRowsRenderAction to set.
   */
  public void setAllRowsRenderAction(RenderActionInterface allRowsRenderAction) {
    this.allRowsRenderAction = allRowsRenderAction;
  }

  /**
   * put your documentation comment here
   */
  public void disableAllRowsRenderAction() {
    this.allRowsRenderAction = renderActionNoOp;
  }

  /**
   * @return Returns the fgColor.
   */
  public Color getFgColor() {
    return fgColor;
  }

  /**
   * @param fgColor The fgColor to set.
   */
  public void setFgColor(Color fgColor) {
    this.fgColor = fgColor;
  }
}

