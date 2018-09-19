/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import javax.swing.table.*;
import java.util.*;
import com.chelseasystems.cr.swing.*;
import javax.swing.*;
import com.chelseasystems.cs.item.CMSItem;


/**
 */
public class ItemModel extends ScrollableTableModel {

	// This constructor is required for pcr 1859
	public ItemModel(String [] columns) {
		super(columns);
		setRowsShown(100);
	}
  /**
   */
  public ItemModel() {
    super(new String[] {"Code", "Description", "Retail Amount", "Size", "Color"
    });
    setRowsShown(100);
  }

  /**
   * @param item
   */
  public void addItem(CMSItem item) {
    addRow(item);
  }

  /**
   * @param row
   * @return
   */
  public CMSItem getItem(int row) {
    return (CMSItem)getRowInPage(row);
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = getCurrentPage();
    CMSItem item = (CMSItem)vTemp.elementAt(row);
    if (item == null) {
      System.out.println("item is null...");
      return "";
    }
    switch (column) {
      case 0:
        return item.getBarCode();
      case 1:
        return item.getDescription();
      case 2:
        if (item.getSellingPrice() != null)
          return item.getSellingPrice().formattedStringValue();
        else
          return "";
      case 3:
        String sizeIndex = item.getItemDetail().getSizeIndx();
        if ((sizeIndex == null || sizeIndex.equals("")) && item.getSizeId() != null)
          sizeIndex = item.getSizeId().trim();
        else if (sizeIndex == null)
          sizeIndex = "";
        if (item.getItemDetail().getExtSizeIndx() != null
            && !item.getItemDetail().getExtSizeIndx().trim().equals(""))
          sizeIndex = sizeIndex.trim() + ":" + item.getItemDetail().getExtSizeIndx().trim();
        if (item.getItemDetail().getSizeDesc() != null
            && !item.getItemDetail().getSizeDesc().trim().equals(""))
          sizeIndex = sizeIndex.trim() + " (" + item.getItemDetail().getSizeDesc().trim() + ")";
        return sizeIndex;
      case 4:
        String colorString = "";
        if (item.getColorId() != null && item.getColorId().trim().length() > 0)
          colorString = item.getColorId();
        if (item.getItemDetail().getColorDesc() != null
            && item.getItemDetail().getColorDesc().trim().length() > 0)
          colorString = colorString + "-" + item.getItemDetail().getColorDesc();
        return colorString;
      default:
        return "";
    }
  }

  /**
   * @param table
   */
  public void setColumnWidth(JTable table) {
    TableColumn acctCol = table.getColumnModel().getColumn(0);
    acctCol.setWidth(150);
    TableColumn nameCol = table.getColumnModel().getColumn(1);
    nameCol.setWidth(table.getWidth() - (acctCol.getWidth() + 5));
  }
}

