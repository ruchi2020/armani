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

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.*;


/**
 */
public class ReturnSaleModel extends ScrollableTableModel {
  public static final int RETURN = 0;
  public static final int CODE = 1;
  public static final int DESC = 2;
  public static final int RET = 3;
  public static final int QTY = 4;
  public static final int PRICE = 5;
  public static final int MARKDOWN = 6;
  public static final int AMT = 7;
  java.util.ResourceBundle res;

  /**
   */
  public ReturnSaleModel() {
    res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Return"), res.getString("Item Code")
        , res.getString("Item Description"), res.getString("Ret"), res.getString("Qty")
        , res.getString("Unit Price"), res.getString("Markdown"), res.getString("Amount Due")
    });
    ;
  }

  /**
   * @param
   */
  public void addNewItem(SaleLineItem line) {
    ReturnItem returnItem = new ReturnItem(line);
    super.addRow(returnItem);
  }

  /**
   * @param row
   * @return
   */
  public boolean isSale(int row) {
    ReturnItem returnItem = (ReturnItem)getRowInPage(row);
    POSLineItem line = (POSLineItem)returnItem.line;
    return line instanceof SaleLineItem;
  }

  /**
   * @param row
   * @return
   */
  public boolean isLayaway(int row) {
    ReturnItem returnItem = (ReturnItem)getRowInPage(row);
    POSLineItem line = (POSLineItem)returnItem.line;
    return false; //line instanceof LineItemLayaway;
  }

  /**
   * @param row
   */
  public void deleteRow(int row) {
    removeRowInPage(row);
  }

  /**
   * @param row
   * @return
   */
  public POSLineItem getLineItem(int row) {
    ReturnItem returnItem = (ReturnItem)getRowInPage(row);
    POSLineItem line = (POSLineItem)returnItem.line;
    return line;
  }

  /**
   * @param row
   * @return
   */
  public ReturnItem getReturnItem(int row) {
    return (ReturnItem)getRowInPage(row);
  }

  /**
   * @return
   */
  public int getColumnCount() {
    return 8;
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
    ReturnItem returnItem = (ReturnItem)vTemp.elementAt(row);
    switch (column) {
      case RETURN:
        return returnItem.isReturn;
      case CODE:
        return getItemID(returnItem.line);
      case DESC:
        String value = "\n" + returnItem.line.getItemDescription();
        CMSSaleLineItem saleLineItem = null;
        if (returnItem.line instanceof CMSSaleLineItem)
          saleLineItem = (CMSSaleLineItem)returnItem.line;
        if (hasAlteration(saleLineItem))
          value += "\n " + res.getString("Altered Item");
        return value;
      case RET:

        // show the amount previously returned, not the amount
        // being returned in this txn.
        //return  returnItem.returnedAmount + "";
        return new Integer(returnItem.line.getQuantity().intValue()
            - returnItem.line.getQuantityAvailableForReturn());
      case QTY:
        return returnItem.line.getQuantity().toString();
      case PRICE:
        return returnItem.line.getItemRetailPrice().formattedStringValue();
      case MARKDOWN:

        //if (returnItem.line.isDiscountGreaterThanMarkdown())
        //   return returnItem.line.getDiscountAmount().formattedStringValue();
        //else
        //   return returnItem.line.getTotalMarkdownAmount().formattedStringValue();
        return returnItem.line.getExtendedReductionAmount().formattedStringValue();
      case AMT:
        return returnItem.line.getExtendedNetAmount().formattedStringValue();
      default:
        return " ";
    }
  }

  /**
   * @param line
   * @return
   */
  private String getItemID(POSLineItem line) {
    /*if (line.getLineAssoc().length() != 0) {
     return  " P" + DealManager.getDealSeqNum(line.getLineAssoc()) + "-"
     + line.getItem().getId();
     }
     else*/
    if (((CMSItem)line.getItem()).getBarCode() != null)
      return " " + ((CMSItem)line.getItem()).getBarCode();
    else
      return " ";
  }

  /**
   * @return if line items in model contain any items available for return;
   */
  public boolean containsReturnableItems() {
    Vector all = this.getAllRows();
    for (Enumeration enm = all.elements(); enm.hasMoreElements(); ) {
      ReturnItem returnItem = (ReturnItem)enm.nextElement();
      if (returnItem.line.getQuantityAvailableForReturn() > 0)
        return true;
    }
    return false;
  }

  /***********************************************************************/
  public class ReturnItem {
    public SaleLineItem line;
    public Boolean isReturn = new Boolean(false);
    public int returnedAmount = 0;

    /**
     * @param       LineItemPOS line
     */
    public ReturnItem(SaleLineItem line) {
      this.line = line;
    }
  }


  /**
   * Check if the sale line item has alteration
   * Used to display "Altered" label.
   * @param cmsSaleLineItem CMSSaleLineItem
   * @return boolean
   */
  private boolean hasAlteration(CMSSaleLineItem cmsSaleLineItem) {
    POSLineItemDetail details[] = cmsSaleLineItem.getLineItemDetailsArray();
    if (details == null)
      return false;
    for (int iCtr = 0; iCtr < details.length; iCtr++) {
      CMSSaleLineItemDetail det = (CMSSaleLineItemDetail)details[iCtr];
      if (det.getAlterationLineItemDetailArray() != null
          && det.getAlterationLineItemDetailArray().length > 0)
        return true;
      else
        continue;
    }
    return false;
  }
}

