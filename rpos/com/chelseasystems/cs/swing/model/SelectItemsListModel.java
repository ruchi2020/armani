/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 09-13-2005 | Manpreet  | 508       |getValueAt() - Displaying Altered Item              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 08-01-2006 | Sandhya   | 1617      | Inquires> Sale & Return transaction search type are| 
 |      |            |           |           | showing first letter only                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import java.util.Vector;
import java.awt.Font;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import java.util.Iterator;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.item.CMSItem;
import java.util.ResourceBundle;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.text.NumberFormat;
/**
 */
public class SelectItemsListModel extends ScrollableTableModel {
  private final int SELECT = 0;
  private final int SELL_RETURN = 1;
  private final int ITEM_CODE = 2;
  private final int ITEM_DESCRIPTION = 3;
  private final int SELL = 4;
  private final int RETURN = 5;
  private final int QUANTITY = 6;
  private final int UNIT_PRICE = 7;
  private final int MARKDOWN = 8;
  private final int AMOUNT_DUE = 9;
  private ResourceBundle resourceBundle;
  private String COLUMN_NAMES[] = {"Select", "S/R", "SKU", "Item Description", "S", "R", "Q"
      , "Unit\nPrice", "Mkdn/\nDisc", "Amount Due"
  };
  private Font fontHeaders;

  /**
   * put your documentation comment here
   */
  public SelectItemsListModel() {
    resourceBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  }

  /**
   * put your documentation comment here
   */
  public void setReservationCloseHeaders() {
    COLUMN_NAMES = new String[] {"Select", "S/R/V", "SKU", "Item Description", "S", "R", "Q"
        , "Unit\nPrice", "Mkdn/\nDisc", "Amount Due"
    };
    makeHTMLColumnHeaders(fontHeaders);
  }

  /**
   * put your documentation comment here
   * @param fontHeader
   */
  public void makeHTMLColumnHeaders(Font fontHeader) {
    String sHeader = "";
    fontHeaders = fontHeader;
    HTMLColumnHeaderUtil htmlUtil = new HTMLColumnHeaderUtil(fontHeader);
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sHeader = resourceBundle.getString(COLUMN_NAMES[iCtr]);
      sIdentiFiers[iCtr] = htmlUtil.getHTMLHeaderFor(sHeader);
    }
    this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * put your documentation comment here
   * @param item
   */
  public void addLineItem(POSLineItem item) {
    RowSelector rowSelector;
    if (item instanceof CMSPresaleLineItem) {
      CMSPresaleLineItem presItem = (CMSPresaleLineItem)item;
      CMSPresaleLineItemDetail prsLnDtl = (CMSPresaleLineItemDetail)presItem.
          getLineItemDetailsArray()[0];
      String lineType = " ";
      if (prsLnDtl.isForSale())
//TD
        lineType = resourceBundle.getString("SaleIndicator");
      else if (prsLnDtl.isForReturn())
        lineType = resourceBundle.getString("ReturnIndicator");
      rowSelector = new RowSelector(item, !prsLnDtl.getProcessed(), prsLnDtl.getProcessed()
          , (prsLnDtl.getSaleLineItemDetail() != null) ? presItem.getQuantity() : new Integer(0)
          , (prsLnDtl.getReturneLineItemDetail() != null) ? presItem.getQuantity() : new Integer(0)
          , new String(lineType));
    } else if (item instanceof CMSReservationLineItem) {
      CMSReservationLineItem rsvLnItm = (CMSReservationLineItem)item;
      CMSReservationLineItemDetail rsvLnItmDtl = (CMSReservationLineItemDetail)rsvLnItm.
          getLineItemDetailsArray()[0];
      rowSelector = new RowSelector(item, false, rsvLnItmDtl.getProcessed()
          , (rsvLnItmDtl.getSaleLineItemDetail() != null) ? rsvLnItm.getQuantity() : new Integer(0)
          , (rsvLnItmDtl.getReturneLineItemDetail() != null) ? rsvLnItm.getQuantity()
          : new Integer(0), new String(resourceBundle.getString("ReserveIndicator")));
    } else if (item instanceof CMSConsignmentLineItem) {
      CMSConsignmentLineItem consItem = (CMSConsignmentLineItem)item;
      CMSConsignmentLineItemDetail csgLnItmDtl = (CMSConsignmentLineItemDetail)consItem.
          getLineItemDetailsArray()[0];
      String lineType = " ";
      if (csgLnItmDtl.isForSale())
        lineType = resourceBundle.getString("SaleIndicator");
      else if (csgLnItmDtl.isForReturn())
        lineType = resourceBundle.getString("ReturnIndicator");
      rowSelector = new RowSelector(item, !csgLnItmDtl.getProcessed(), csgLnItmDtl.getProcessed()
          , (csgLnItmDtl.getSaleLineItemDetail() != null) ? consItem.getQuantity() : new Integer(0)
          , (csgLnItmDtl.getReturneLineItemDetail() != null) ? consItem.getQuantity()
          : new Integer(0), new String(lineType));
    } else {
      rowSelector = new RowSelector(item, false, false);
    }
    addRow(rowSelector);
    if (getTotalRowCount() > getRowsShown()) {
      lastPage();
    }
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public boolean isSale(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof SaleLineItem);
  }

  /**
   * @param row
   * @return
   */
  public boolean isLayaway(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof LayawayLineItem);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public boolean isPreSaleOpen(int row) {
    POSLineItem line = (POSLineItem)getRowInPage(row);
    return (line instanceof CMSPresaleLineItem);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public boolean getRowSelected(int row) {
    if (row < 0)
      return false;
    RowSelector rowStruct = (RowSelector)getRowInPage(row);
    return rowStruct.isRowSelected.booleanValue();
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public boolean getIsAlreadyProcessed(int row) {
    if (row < 0)
      return false;
    RowSelector rowStruct = (RowSelector)getRowInPage(row);
    return rowStruct.isRowAlreadyProcessed.booleanValue();
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void setRowSelected(int row) {
    if (row < 0)
      return;
    RowSelector rowStruct = (RowSelector)getRowInPage(row);
    if (!rowStruct.isRowAlreadyProcessed.booleanValue())
      rowStruct.isRowSelected = new Boolean(!rowStruct.isRowSelected.booleanValue());
    if (!rowStruct.isRowSelected.booleanValue() && !rowStruct.isRowAlreadyProcessed.booleanValue()) {
      clearRowAttribs(rowStruct);
    }
  }

  /**
   * put your documentation comment here
   * @param rowStruct
   */
  private void clearRowAttribs(RowSelector rowStruct) {
    if (rowStruct.lineItemPos instanceof CMSReservationLineItem)
      rowStruct.cSelectionType = new String(resourceBundle.getString("ReserveIndicator"));
    else
      rowStruct.cSelectionType = new String(" ");
    rowStruct.iReturnQty = new Integer(0);
    rowStruct.iSellQty = new Integer(0);
  }

  /**
   * @param row
   */
  public void deleteLineItem(int row) {
    if (row < 0)
      return;
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public POSLineItem getLineItem(int row) {
    if (row < 0)
      return null;
    return ((RowSelector)this.getRowInPage(row)).lineItemPos;
  }

  /**
   * put your documentation comment here
   * @param row
   * @param selected
   */
  public void setRowSelected(int row, boolean selected) {
    if (row < 0)
      return;
    RowSelector rowStruct = (RowSelector)getRowInPage(row);
    if (!rowStruct.isRowAlreadyProcessed.booleanValue())
      rowStruct.isRowSelected = new Boolean(selected);
    if (!selected)
      clearRowAttribs(rowStruct);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getSaleLineItems() {
    Vector rowStructs = getAllRows();
    Vector selectedPOSLineItems = new Vector();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      if (rowStruct.cSelectionType.equalsIgnoreCase(resourceBundle.getString("SaleIndicator"))
//TD
          && !rowStruct.isRowAlreadyProcessed.booleanValue())
        selectedPOSLineItems.add(rowStruct.lineItemPos);
    }
    return rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getReservedLineItems() {
    Vector rowStructs = getAllRows();
    Vector selectedPOSLineItems = new Vector();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      if (rowStruct.cSelectionType.equalsIgnoreCase(resourceBundle.getString("ReserveIndicator"))
          && !rowStruct.isRowAlreadyProcessed.booleanValue())
        selectedPOSLineItems.add(rowStruct.lineItemPos);
    }
    return rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getReturnLineItems() {
    Vector rowStructs = getAllRows();
    Vector selectedPOSLineItems = new Vector();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      if (rowStruct.cSelectionType.equalsIgnoreCase(resourceBundle.getString("ReturnIndicator"))
//TD
          && !rowStruct.isRowAlreadyProcessed.booleanValue())
        selectedPOSLineItems.add(rowStruct.lineItemPos);
    }
    return rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]);
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
      RowSelector rowStruct = (RowSelector)itr.next();
      if (rowStruct.isRowSelected.booleanValue() && !rowStruct.isRowAlreadyProcessed.booleanValue())
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
      RowSelector rowStruct = (RowSelector)itr.next();
      if (!rowStruct.isRowAlreadyProcessed.booleanValue())
        selectedPOSLineItems.add(rowStruct.lineItemPos);
    }
    return rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]);
  }

  /**
   * put your documentation comment here
   * @param bSelect
   */
  public void toggleRowsSelection(boolean bSelect) {
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      if (!rowStruct.isRowAlreadyProcessed.booleanValue())
        rowStruct.isRowSelected = new Boolean(bSelect);
    }
    fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @param iRow
   */
  public void sellLineItem(int iRow) {
    if (iRow < 0)
      return;
    RowSelector rowStruct = (RowSelector)getRowInPage(iRow);
    rowStruct.iSellQty = new Integer(1);
    rowStruct.iReturnQty = new Integer(0);
    rowStruct.cSelectionType = new String(resourceBundle.getString("SaleIndicator"));
//TD
  }

  /**
   * put your documentation comment here
   */
  public void sellSelectedLineItems() {
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      if (rowStruct.isRowSelected.booleanValue() && !rowStruct.isRowAlreadyProcessed.booleanValue()) {
        rowStruct.iSellQty = new Integer(1);
        rowStruct.iReturnQty = new Integer(0);
        rowStruct.cSelectionType = new String(resourceBundle.getString("SaleIndicator"));
//TD
        rowStruct.isRowSelected = new Boolean(false);
      }
    }
    fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   */
  public boolean returnSelectedLineItems() {
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    boolean bContainsAlteredLineItem=false;
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector) itr.next();
      try {
        if (rowStruct.lineItemPos instanceof CMSPresaleLineItem) {
          ( (CMSPresaleLineItem) rowStruct.lineItemPos).testIsReturnable();
        }
      }
      catch (BusinessRuleException br) {
        bContainsAlteredLineItem = true;
        continue;
      }
      if (rowStruct.isRowSelected.booleanValue() &&
          !rowStruct.isRowAlreadyProcessed.booleanValue()) {
        rowStruct.iSellQty = new Integer(0);
        rowStruct.iReturnQty = new Integer(1);
        rowStruct.cSelectionType = new String(resourceBundle.getString("ReturnIndicator"));
//TD
        rowStruct.isRowSelected = new Boolean(false);
      }
    }
    fireTableDataChanged();
    return bContainsAlteredLineItem;
  }

  /**
   * put your documentation comment here
   */
  public void reserveSelectedLineItem() {
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      if (rowStruct.isRowSelected.booleanValue() && !rowStruct.isRowAlreadyProcessed.booleanValue()) {
        rowStruct.iSellQty = new Integer(0);
        rowStruct.iReturnQty = new Integer(0);
        rowStruct.cSelectionType = new String(resourceBundle.getString("ReserveIndicator"));
        rowStruct.isRowSelected = new Boolean(false);
      }
    }
    fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @param iRow
   */
  public void returnLineItem(int iRow) {
    if (iRow < 0)
      return;
    RowSelector rowStruct = (RowSelector)getRowInPage(iRow);
    rowStruct.iSellQty = new Integer(0);
    rowStruct.iReturnQty = new Integer(1);
    rowStruct.cSelectionType = new String(resourceBundle.getString("ReturnIndicator"));
//TD
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    return (false);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = this.getCurrentPage();
    RowSelector rowSelector = (RowSelector)vTemp.elementAt(row);
    POSLineItem line = rowSelector.lineItemPos;
    switch (column) {
      case SELECT:
        return rowSelector.isRowSelected;
      case SELL_RETURN:
        return rowSelector.cSelectionType + "";
      case ITEM_CODE:
        return (getItemID(line));
      case ITEM_DESCRIPTION:

        // Modified to display 'Altered item' --Manpreet (09/14/2005)
        // if LineItem has alterations attached.
        // return line.getItemDescription() ;
        String value = "<HTML><B>" + line.getItemDescription();
        if (line instanceof CMSPresaleLineItem && hasAlteration(line)) {
          value += "<BR> " + resourceBundle.getString("Altered Item");
        }
        value += "</B></HTML>";
        return value;
      case SELL:
        return rowSelector.iSellQty;
      case RETURN:
        return rowSelector.iReturnQty;
      case QUANTITY:
        if (line instanceof ReturnLineItem) {
          return (line.getQuantity().intValue() * -1 + "");
        }
        return (line.getQuantity() + "");
      case UNIT_PRICE:
        if (line instanceof ReturnLineItem)
          return (line.getItemRetailPrice().formattedStringValue()); //original price it was sold at
        else
          return (line.getItemRetailPrice().formattedStringValue());
      case MARKDOWN:
        String sZeroAmount = new ArmCurrency(0.0d).formattedStringValue();
        if(line instanceof CMSPresaleLineItem){
          ArmCurrency totalMarkDown = line.getExtendedReductionAmount();
          double d = 0.00;
          if (line.getItemRetailPrice().doubleValue() != 0.00)
                  d = totalMarkDown.doubleValue() / line.getItemRetailPrice().doubleValue()/ line.getQuantity().intValue();
          NumberFormat nf = NumberFormat.getPercentInstance();
          nf.setMaximumFractionDigits(1);
          return totalMarkDown.formattedStringValue() + " (" + nf.format(d) + ")";

//          return sZeroAmount + " (0.00%) ";
        }
         else if (line instanceof CMSReservationLineItem)
          return sZeroAmount + " (0.00%) ";

      case AMOUNT_DUE:
        return (line.getExtendedNetAmount().formattedStringValue());
      default:
        return (" ");
    }
  }

  /**
   * put your documentation comment here
   * @param line
   * @return
   */
  private String getItemID(POSLineItem line) {
    if (((CMSItem)line.getItem()).getBarCode() != null)
      return " " + ((CMSItem)line.getItem()).getBarCode();
    else
      return " ";
  }

  /**
   * put your documentation comment here
   * @param RowsShown
   */
  public void setRowsShown(int RowsShown) {
    super.setRowsShown(RowsShown);
  }

  /**
   * put your documentation comment here
   * @param columnIndex
   * @return
   */
  public Class getColumnClass(int columnIndex) {
    return getValueAt(0, columnIndex).getClass();
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   */
  private boolean hasAlteration(POSLineItem lineItem) {
    POSLineItemDetail details[] = lineItem.getLineItemDetailsArray();
    if (details == null)
      return false;
    for (int iCtr = 0; iCtr < details.length; iCtr++) {
      AlterationLineItemDetail altDets[] = null;
      if (details[iCtr] instanceof CMSPresaleLineItemDetail) {
        altDets = ((CMSPresaleLineItemDetail)details[iCtr]).getAlterationLineItemDetailArray();
      } else if (details[iCtr] instanceof CMSSaleLineItemDetail) {
        altDets = ((CMSSaleLineItemDetail)details[iCtr]).getAlterationLineItemDetailArray();
      }
      if (altDets != null && altDets.length > 0)
        return true;
      else
        continue;
    }
    return false;
  }

  private class RowSelector {
    POSLineItem lineItemPos;
    Boolean isRowSelected;
    Boolean isRowAlreadyProcessed;
    String cSelectionType;
    Integer iSellQty = new Integer(0);
    Integer iReturnQty = new Integer(0);

    /**
     * put your documentation comment here
     * @param     POSLineItem lineItemPos
     * @param     boolean bInitialSelect
     * @param     boolean bRowProcessed
     */
    public RowSelector(POSLineItem lineItemPos, boolean bInitialSelect, boolean bRowProcessed) {
      cSelectionType = new String(" ");
      this.lineItemPos = lineItemPos;
      this.isRowSelected = new Boolean(bInitialSelect);
      this.isRowAlreadyProcessed = new Boolean(bRowProcessed);
    }

    /**
     * put your documentation comment here
     * @param     POSLineItem lineItemPos
     * @param     boolean bInitialSelect
     * @param     boolean bRowProcessed
     * @param     Integer sellQty
     * @param     Integer returnQty
     * @param     String selectionType
     */
    public RowSelector(POSLineItem lineItemPos, boolean bInitialSelect, boolean bRowProcessed
        , Integer sellQty, Integer returnQty, String selectionType) {
      cSelectionType = selectionType;
      this.lineItemPos = lineItemPos;
      this.isRowSelected = new Boolean(bInitialSelect);
      this.isRowAlreadyProcessed = new Boolean(bRowProcessed);
      this.iSellQty = sellQty;
      this.iReturnQty = returnQty;
    }
  }
}

