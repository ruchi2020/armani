/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 07-08-2005 | Vikram    | 406       | Display Barcode instead of SKU               |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.model;

import java.util.*;
import javax.swing.event.TableModelEvent;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.item.CMSItem;


/**
 */
public class LineItemPOSModelMultiSelect extends ScrollableTableModel {
  // fields that make up lineItemPOS
  public final static int SELECTED = 0;
  public final static int ID = 1;
  public final static int DESC = 2;
  public final static int QTY = 3;
  public final static int RETAIL = 4;
  public final static int MARKDOWN = 5;
  public final static int AMOUNT_DUE = 6;

  /**
   */
  public LineItemPOSModelMultiSelect() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Select"), res.getString("Item Code")
        , res.getString("Item Description"), res.getString("Quantity"), res.getString("Unit Price")
        , res.getString("Markdown"), res.getString("Amount Due")
    });
    ;
  }

  /**
   * @param item
   */
  public void addLineItem(POSLineItem item) {
    addRow(new RowStruct(item));
    if (getTotalRowCount() > getRowsShown()) { // scroll down with rows
      lastPage();
    }
  }

  /**
   * put your documentation comment here
   * @param row
   * @param selected
   */
  public void setRowSelected(int row, boolean selected) {
    RowStruct rowStruct = (RowStruct)getRowInPage(row);
    rowStruct.selected = new Boolean(selected);
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void setRowSelected(int row) {
    RowStruct rowStruct = (RowStruct)getRowInPage(row);
    rowStruct.selected = new Boolean(!rowStruct.selected.booleanValue());
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public boolean getRowSelected(int row) {
    RowStruct rowStruct = (RowStruct)getRowInPage(row);
    return (rowStruct.selected.booleanValue());
  }

  /**
   * @param row
   * @return
   */
  public boolean isSale(int row) {
    POSLineItem line = ((RowStruct)getRowInPage(row)).lineItemPos;
    return (line instanceof SaleLineItem);
  }

  /**
   * @param row
   * @return
   */
  public boolean isLayaway(int row) {
    POSLineItem line = ((RowStruct)getRowInPage(row)).lineItemPos;
    return (line instanceof LayawayLineItem);
  }

  /**
   * @param row
   */
  public void deleteLineItem(int row) {
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * @param row
   * @return
   */
  public POSLineItem getLineItem(int row) {
    return ((RowStruct)this.getRowInPage(row)).lineItemPos;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getSelectedLineItems() {
    Vector rowStructs = getAllRows();
    Vector selectedPOSLineItems = new Vector();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowStruct rowStruct = (RowStruct)itr.next();
      if (rowStruct.selected.booleanValue())
        selectedPOSLineItems.add(rowStruct.lineItemPos);
    }
    return (rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]));
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return (7);
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return (false);
  }

  /**
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = this.getCurrentPage();
    RowStruct rowStruct = (RowStruct)vTemp.elementAt(row);
    POSLineItem line = rowStruct.lineItemPos;
    switch (column) {
      case SELECTED:
        return (rowStruct.selected);
      case ID:
        return (getItemID(line));
      case DESC:

        //return  line.getItem().getDescription();
        return (line.getItemDescription());
      case QTY:
        if (line instanceof ReturnLineItem) {
          return (line.getQuantity().intValue() * -1 + "");
        }
        return (line.getQuantity() + "");
      case RETAIL:
        if (line instanceof ReturnLineItem)
          return (line.getItemRetailPrice().formattedStringValue()); //original price it was sold at
        else
          //return  line.getItem().getRetailPrice().formattedStringValue();
          return (line.getItemRetailPrice().formattedStringValue());
      case MARKDOWN:

        //if (line.isDiscountGreaterThanMarkdown())
        //   return line.getDiscountAmount().formattedStringValue();
        //else
        //   return line.getTotalMarkdownAmount().formattedStringValue();
        return (line.getExtendedReductionAmount().formattedStringValue());
      case AMOUNT_DUE:
        return (line.getExtendedNetAmount().formattedStringValue());
      default:
        return (" ");
    }
  }

  /**
   * @param line
   * @return
   */
  private String getItemID(POSLineItem line) {
    if (((CMSItem)line.getItem()).getBarCode() != null)
      return " " + ((CMSItem)line.getItem()).getBarCode();
    else
      return " ";
  }

  private class RowStruct {
    public POSLineItem lineItemPos;
    public Boolean selected;

    /**
     * put your documentation comment here
     * @param     POSLineItem lineItemPos
     */
    public RowStruct(POSLineItem lineItemPos) {
      this.lineItemPos = lineItemPos;
      this.selected = new Boolean(false);
    }
  }
}

