/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.table.cellrenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


/**
 * @author fbulah
 *
 */
public class MultiLineHeaderRenderer extends JPanel implements TableCellRenderer {
  protected int verticalAlignment;
  protected int horizontalAlignment;
  protected float alignmentX;
  // These attributes may be explicitly set
  // They are defaulted to the colors and attributes
  // of the table header
  protected Color foreground;
  protected Color background;
  // These attributes have fixed defaults
  protected Border headerBorder = UIManager.getBorder("TableHeader.cellBorder");
  protected Font font = UIManager.getFont("TableHeader.font");

  /**
   *
   */
  public MultiLineHeaderRenderer() {
    super();
  }

  /**
   * @param isDoubleBuffered
   */
  public MultiLineHeaderRenderer(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
  }

  /**
   * @param layout
   */
  public MultiLineHeaderRenderer(LayoutManager layout) {
    super(layout);
  }

  /**
   * @param layout
   * @param isDoubleBuffered
   */
  public MultiLineHeaderRenderer(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
  }

  /**
   * put your documentation comment here
   * @param   int horizontalAlignment
   * @param   int verticalAlignment
   */
  public MultiLineHeaderRenderer(int horizontalAlignment, int verticalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;
    switch (horizontalAlignment) {
      case SwingConstants.LEFT:
        alignmentX = (float)0.0;
        break;
      case SwingConstants.CENTER:
        alignmentX = (float)0.5;
        break;
      case SwingConstants.RIGHT:
        alignmentX = (float)1.0;
        break;
      default:
        throw new IllegalArgumentException("Illegal horizontal alignment value");
    }
    setBorder(headerBorder);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(true);
    background = null;
  }

  /**
   * put your documentation comment here
   * @param foreground
   */
  public void setForeground(Color foreground) {
    this.foreground = foreground;
    super.setForeground(foreground);
  }

  /**
   * put your documentation comment here
   * @param background
   */
  public void setBackground(Color background) {
    this.background = background;
    super.setBackground(background);
  }

  /**
   * put your documentation comment here
   * @param font
   */
  public void setFont(Font font) {
    this.font = font;
  }

  // Implementation of TableCellRenderer interface
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
      , boolean hasFocus, int row, int column) {
    removeAll();
    invalidate();
    if (value == null) {
      return this;
    }
    // Set the foreground and background colors
    // from the table header if they are not set
    if (table != null) {
      JTableHeader header = table.getTableHeader();
      if (header != null) {
        if (foreground == null) {
          super.setForeground(header.getForeground());
        }
        if (background == null) {
          super.setBackground(header.getBackground());
        }
      }
    }
    if (verticalAlignment != SwingConstants.TOP) {
      add(Box.createVerticalGlue());
    }
    Object[] values;
    int length;
    if (value instanceof Object[]) {
      // Input is an array - use it
      values = (Object[])value;
    } else {
      // Not an array - turn it into one
      values = new Object[1];
      values[0] = value;
    }
    length = values.length;
    // Configure each row of the header using
    // a separate JLabel. If a given row is
    // a JComponent, add it directly..
    for (int i = 0; i < length; i++) {
      Object thisRow = values[i];
      if (thisRow instanceof JComponent) {
        add((JComponent)thisRow);
      } else {
        JLabel l = new JLabel();
        setValue(l, thisRow, i);
        add(l);
      }
    }
    if (verticalAlignment != SwingConstants.BOTTOM) {
      add(Box.createVerticalGlue());
    }
    return this;
  }

  // Configures a label for one line of the header.
  // This can be overridden by derived classes
  protected void setValue(JLabel l, Object value, int lineNumber) {
    if (value != null && value instanceof Icon) {
      l.setIcon((Icon)value);
    } else {
      l.setText(value == null ? "" : value.toString());
    }
    l.setHorizontalAlignment(horizontalAlignment);
    l.setAlignmentX(alignmentX);
    l.setOpaque(false);
    l.setForeground(foreground);
    l.setFont(font);
  }
}

