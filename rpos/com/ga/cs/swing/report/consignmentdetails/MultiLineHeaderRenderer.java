/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.swing.report.consignmentdetails;

import java.awt.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;


/**
 * put your documentation comment here
 */
public class MultiLineHeaderRenderer extends JList implements TableCellRenderer {

  /**
   * put your documentation comment here
   * @param   Color background
   */
  public MultiLineHeaderRenderer(Color background) {
    setOpaque(true);
    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    setForeground(UIManager.getColor("TableHeader.foreground"));
    setBackground(background);
    ListCellRenderer renderer = getCellRenderer();
    ((JLabel)renderer).setHorizontalAlignment(JLabel.CENTER);
    setCellRenderer(renderer);
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
    setFont(table.getFont());
    String str = (value == null) ? "" : value.toString();
    BufferedReader br = new BufferedReader(new StringReader(str));
    String line;
    Vector v = new Vector();
    try {
      while ((line = br.readLine()) != null) {
        v.addElement(line);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    setListData(v);
    return this;
  }
}

