/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 07-08-2005 | Vikram    | 406       | Display Barcode instead of SKU               |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 04-12-2005 | Khyati    | N/A       |1.Send Sale specification.                    |
 --------------------------------------------------------------------------------------------
 | 1    | 04-12-2005 | Base      | N/A       |                                              |
 --------------------------------------------------------------------------------------------
 */
import java.util.*;
import javax.swing.event.TableModelEvent;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.item.CMSItem;


/**
 */
public class ItemsToShipModel extends ScrollableTableModel {
  // fields that make up lineItemPOS
  public final static int SELECTED = 0;
  public final static int ID = 1;
  public final static int DESC = 2;
  public final static int QTY = 3;
  public final static int RETAIL = 4;
  public final static int AMOUNT_DUE = 5;

  /**
   */
  public ItemsToShipModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Select"), res.getString("Item Code")
        , res.getString("Item Description"), res.getString("Quantity"), res.getString("Unit Price")
        , res.getString("Amount Due")
    });
    ;
  }

  /**
   * put your documentation comment here
   * @param posLineItem
   * @param shippingRequest
   */
  public void addLineItem(POSLineItem posLineItem, ShippingRequest shippingRequest) {
    //      System.out.println("model add line item");
    RowStruct rowStruct = new RowStruct(posLineItem);
    if (posLineItem instanceof SaleLineItem)
      addLineItem(posLineItem, shippingRequest, rowStruct);
    else if (posLineItem instanceof CMSConsignmentLineItem)
      addLineItem(posLineItem, shippingRequest, rowStruct);
    else if (posLineItem instanceof CMSPresaleLineItem)
      addLineItem(posLineItem, shippingRequest, rowStruct); /*
         if(item.getShippingRequest() != null) {
         //         if(item.getShippingRequest().equals(shippingRequest)) {
         //ks: should do == to return false if its a new shipping request
         if (item.getShippingRequest() == shippingRequest) {
         rowStruct.selected = new Boolean(true);
         addRow(rowStruct);
         }
         //         }
         }
         else {
         if (CMSApplet.theAppMgr.getStateObject("SHIPPING_INQUIRY_ONLY") == null)
         // only display un-shipped items if we are in update mode
         //System.out.println("add new line item");
         addRow(rowStruct);
         }*/

  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @param shippingRequest
   * @param rowStruct
   */
  private void addLineItem(POSLineItem lineItem, ShippingRequest shippingRequest
      , RowStruct rowStruct) {
    ShippingRequest lnItmShippingRequest = null;
    if (lineItem instanceof CMSConsignmentLineItem)
      lnItmShippingRequest = ((CMSConsignmentLineItem)lineItem).getShippingRequest();
    else if (lineItem instanceof CMSPresaleLineItem)
      lnItmShippingRequest = ((CMSPresaleLineItem)lineItem).getShippingRequest();
    else if (lineItem instanceof SaleLineItem)
      lnItmShippingRequest = ((SaleLineItem)lineItem).getShippingRequest();
    if (lnItmShippingRequest != null) {
      //         if(item.getShippingRequest().equals(shippingRequest)) {
      //ks: should do == to return false if its a new shipping request
      if (lnItmShippingRequest == shippingRequest) {
        rowStruct.selected = new Boolean(true);
        addRow(rowStruct);
      }
      //         }
    } else {
      if (CMSApplet.theAppMgr.getStateObject("SHIPPING_INQUIRY_ONLY") == null)
        // only display un-shipped items if we are in update mode
        //System.out.println("add new line item");
        addRow(rowStruct);
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
    return rowStruct.selected.booleanValue();
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
    return rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getUnSelectedLineItems() {
    Vector rowStructs = getAllRows();
    Vector selectedPOSLineItems = new Vector();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowStruct rowStruct = (RowStruct)itr.next();
      if (!rowStruct.selected.booleanValue())
        selectedPOSLineItems.add(rowStruct.lineItemPos);
    }
    return rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 6;
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
    Vector vTemp = this.getCurrentPage();
    RowStruct rowStruct = (RowStruct)vTemp.elementAt(row);
    POSLineItem line = rowStruct.lineItemPos;
    switch (column) {
      case SELECTED:
        return rowStruct.selected;
      case ID:
        return getItemID(line);
      case DESC:

        //return  line.getItem().getDescription();
        return line.getItemDescription();
      case QTY:

        /*if (line instanceof ReturnLineItem) {Commented this out because BCF doesn't care.
         ReturnLineItem lineRtn = (ReturnLineItem)line;
         return  line.getQuantity().intValue() + " of " + line.getQuantityAvailToReturn();
         }
         else*/
        return line.getQuantity() + "";
      case RETAIL:
        if (line instanceof ReturnLineItem)
          return line.getItemRetailPrice().formattedStringValue(); //original price it was sold at
        else
          //return  line.getItem().getRetailPrice().formattedStringValue();
          return line.getItemRetailPrice().formattedStringValue();
      case AMOUNT_DUE:
        return line.getExtendedNetAmount().formattedStringValue();
      default:
        return " ";
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

