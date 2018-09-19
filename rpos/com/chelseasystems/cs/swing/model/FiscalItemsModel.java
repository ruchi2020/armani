/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import java.util.Vector;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import java.util.Iterator;


/**
 */
public class FiscalItemsModel extends ScrollableTableModel {
  private final int SKU = 0;
  private final int QUANTITY = 1;
  private final int ITEM_DESCRIPTION = 2;
  private final int ITEM_COMMENT = 3;
  private boolean bVAtInvoice = false;
  private boolean bSelectable = false;
  private String docType = null;
  private final String COLUMN_NAMES[] = {"SKU", "Qty", "Item Description", "VAT Comments"
  };
  java.util.ResourceBundle res;
  private int lastSelectedItemRow = -1;

  /**
   * put your documentation comment here
   * @param   boolean bVatInvoice
   */
  public FiscalItemsModel(boolean bVatInvoice, String docType, boolean bSelectable) {
    bVAtInvoice = bVatInvoice;
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
//    if (bVatInvoice)
      for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++)
        sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES[iCtr]);
//    else
//      for (int iCtr = 0; iCtr < COLUMN_NAMES.length - 1; iCtr++)
//        sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES[iCtr]);
    this.setColumnIdentifiers(sIdentiFiers);
    this.docType = docType;
    this.bSelectable = bSelectable;
  }

  /**
   * put your documentation comment here
   * @param posLineItem
   */
  public void addLineItem(POSLineItem posLineItem) {
    RowSelector rowSelector;
    boolean bSelect = true;

    if (posLineItem instanceof CMSNoSaleLineItem) {
      if (!((CMSNoSaleLineItem)posLineItem).isDocumentPrintedForDocType(docType) && !this.bSelectable)
        bSelect = false;
    }
    if (posLineItem instanceof CMSNoReturnLineItem) {
      if (!((CMSNoReturnLineItem)posLineItem).isDocumentPrintedForDocType(docType) && !this.bSelectable)
        bSelect = false;
    }
    if (posLineItem instanceof CMSSaleLineItem) {
      if (!((CMSSaleLineItem)posLineItem).isDocumentPrintedForDocType(docType) && !this.bSelectable)
        bSelect = false;
    }
    if (posLineItem instanceof CMSConsignmentLineItem) {
      if (!((CMSConsignmentLineItem)posLineItem).isDocumentPrintedForDocType(docType) && !this.bSelectable)
        bSelect = false;
    }
    if (posLineItem instanceof CMSReservationLineItem) {
      if (!((CMSReservationLineItem)posLineItem).isDocumentPrintedForDocType(docType) && !this.bSelectable)
        bSelect = false;
    }
    if (posLineItem instanceof CMSPresaleLineItem) {
      if (!((CMSPresaleLineItem)posLineItem).isDocumentPrintedForDocType(docType) && !this.bSelectable)
        bSelect = false;
    }
    if (posLineItem instanceof CMSReturnLineItem) {
      if (!((CMSReturnLineItem)posLineItem).isDocumentPrintedForDocType(docType) && !this.bSelectable)
        bSelect = false;
    }

    rowSelector = new RowSelector(posLineItem, bSelect);
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
    POSLineItem line = ((RowSelector)getRowInPage(row)).lineItemPos;
    return (line instanceof SaleLineItem);
  }

  /**
   * @param row
   * @return
   */
  public boolean isLayaway(int row) {
    POSLineItem line = ((RowSelector)getRowInPage(row)).lineItemPos;
    return (line instanceof LayawayLineItem);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public boolean isPreSaleOpen(int row) {
    POSLineItem line = ((RowSelector)getRowInPage(row)).lineItemPos;
    return (line instanceof CMSPresaleLineItem);
  }

  public int getLastSelectedItemRow() {
    return lastSelectedItemRow;
  }

  public void setLastSelectedItemRow(int lastSelectedItemRow) {
    this.lastSelectedItemRow = lastSelectedItemRow;
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
   */
  public void setRowSelected(int row) {
    if (row < 0)
      return;
    RowSelector rowStruct = (RowSelector)getRowInPage(row);
    rowStruct.isRowSelected = new Boolean(!rowStruct.isRowSelected.booleanValue());
  }

  /**
   * put your documentation comment here
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
    rowStruct.isRowSelected = new Boolean(selected);
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
      if (rowStruct.isRowSelected.booleanValue())
        selectedPOSLineItems.add(rowStruct.lineItemPos);
    }
    return rowStructs.isEmpty() ? null
        : (POSLineItem[])selectedPOSLineItems.toArray(new POSLineItem[selectedPOSLineItems.size()]);
  }
  
  public POSLineItem[] getLineItems() {
	    Vector rowStructs = getAllRows();
	    Vector selectedPOSLineItems = new Vector();
	    Iterator itr = rowStructs.iterator();
	    while (itr.hasNext()) {
	      RowSelector rowStruct = (RowSelector)itr.next();
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
      if (!rowStruct.isRowSelected.booleanValue())
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
      rowStruct.isRowSelected = new Boolean(bSelect);
    }
    fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getColumnCount() {
//    if (!bVAtInvoice)
//      return COLUMN_NAMES.length - 1;
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public boolean isCellEditable(int row, int column) {
    /*if (column == ITEM_COMMENT && (isFiscalDocumentAlreadyPrintedForLineItem(row)))
      return true;*/
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
      case SKU:
        return line.getItem().getId() + "";
      case ITEM_DESCRIPTION:
        String value = line.getItemDescription();
        if (line instanceof CMSPresaleLineItem && ((CMSPresaleLineItem)line).hasShippingRequest())
          value += res.getString(" (Ship)");
        else if (line instanceof CMSSaleLineItem && ((CMSSaleLineItem)line).hasShippingRequest())
          value += res.getString(" (Ship)");
        return value;
      case QUANTITY:
        if (line instanceof ReturnLineItem) {
          return (line.getQuantity().intValue() * -1 + "");
        }
        return (line.getQuantity() + "");
      case ITEM_COMMENT:
        if (rowSelector.lineItemPos instanceof CMSNoSaleLineItem) {
          return ((CMSNoSaleLineItem)rowSelector.lineItemPos).getFiscalDocComment(docType);
        } else if (rowSelector.lineItemPos instanceof CMSNoReturnLineItem) {
          return ((CMSNoReturnLineItem)rowSelector.lineItemPos).getFiscalDocComment(docType);
        } else if (rowSelector.lineItemPos instanceof CMSSaleLineItem) {
          return ((CMSSaleLineItem)rowSelector.lineItemPos).getFiscalDocComment(docType);
        } else if (rowSelector.lineItemPos instanceof CMSReturnLineItem) {
          return ((CMSReturnLineItem)rowSelector.lineItemPos).getFiscalDocComment(docType);
        } else if (rowSelector.lineItemPos instanceof CMSPresaleLineItem) {
          return ((CMSPresaleLineItem)rowSelector.lineItemPos).getFiscalDocComment(docType);
        } else if (rowSelector.lineItemPos instanceof CMSConsignmentLineItem) {
          return ((CMSConsignmentLineItem)rowSelector.lineItemPos).getFiscalDocComment(docType);
        } else if (rowSelector.lineItemPos instanceof CMSReservationLineItem) {
          return ((CMSReservationLineItem)rowSelector.lineItemPos).getFiscalDocComment(docType);
        }

      default:
        return (" ");
    }
  }

  /**
   * put your documentation comment here
   * @param sComment
   * @param iCtr
   */
  public void setCommentAt(String sComment, int iCtr) {
    if (iCtr < 0 || iCtr >= this.getAllRows().size() || sComment == null)
      return;
    Vector vTemp = this.getAllRows();
    RowSelector rowSelector = (RowSelector)vTemp.elementAt(iCtr);
    if (rowSelector.lineItemPos instanceof CMSNoSaleLineItem) {
      ((CMSNoSaleLineItem)rowSelector.lineItemPos).addFiscalDocComment(docType, sComment);
    } else if (rowSelector.lineItemPos instanceof CMSNoReturnLineItem) {
      ((CMSNoReturnLineItem)rowSelector.lineItemPos).addFiscalDocComment(docType, sComment);
    } else if (rowSelector.lineItemPos instanceof CMSSaleLineItem) {
      ((CMSSaleLineItem)rowSelector.lineItemPos).addFiscalDocComment(docType, sComment);
    } else if (rowSelector.lineItemPos instanceof CMSReturnLineItem) {
      ((CMSReturnLineItem)rowSelector.lineItemPos).addFiscalDocComment(docType, sComment);
    }else if (rowSelector.lineItemPos instanceof CMSPresaleLineItem) {
      ((CMSPresaleLineItem)rowSelector.lineItemPos).addFiscalDocComment(docType, sComment);
    }else if (rowSelector.lineItemPos instanceof CMSConsignmentLineItem) {
      ((CMSConsignmentLineItem)rowSelector.lineItemPos).addFiscalDocComment(docType, sComment);
    }else if (rowSelector.lineItemPos instanceof CMSReservationLineItem) {
      ((CMSReservationLineItem)rowSelector.lineItemPos).addFiscalDocComment(docType, sComment);
    }



    rowSelector.sComment = sComment;
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
   * @param iRow
   * @return
   */
  public boolean isFiscalDocumentAlreadyPrintedForLineItem(int iRow) {
    if (iRow < 0)
      return false;
    RowSelector rowStruct = (RowSelector)getRowInPage(iRow);
    if (rowStruct.lineItemPos instanceof CMSNoSaleLineItem) {
      if (!((CMSNoSaleLineItem)rowStruct.lineItemPos).isDocumentPrintedForDocType(docType))
        return true;
    }
    if (rowStruct.lineItemPos instanceof CMSNoReturnLineItem) {
      if (!((CMSNoReturnLineItem)rowStruct.lineItemPos).isDocumentPrintedForDocType(docType))
        return true;
    }
    if (rowStruct.lineItemPos instanceof CMSSaleLineItem) {
      if (!((CMSSaleLineItem)rowStruct.lineItemPos).isDocumentPrintedForDocType(docType))
        return true;
    }
    if (rowStruct.lineItemPos instanceof CMSConsignmentLineItem) {
      if (!((CMSConsignmentLineItem)rowStruct.lineItemPos).isDocumentPrintedForDocType(docType))
        return true;
    }
    if (rowStruct.lineItemPos instanceof CMSPresaleLineItem) {
      if (!((CMSPresaleLineItem)rowStruct.lineItemPos).isDocumentPrintedForDocType(docType))
        return true;
    }
    if (rowStruct.lineItemPos instanceof CMSReservationLineItem) {
      if (!((CMSReservationLineItem)rowStruct.lineItemPos).isDocumentPrintedForDocType(docType))
        return true;
    }
    if (rowStruct.lineItemPos instanceof CMSReturnLineItem) {
      if (!((CMSReturnLineItem)rowStruct.lineItemPos).isDocumentPrintedForDocType(docType))
        return true;
    }


    //      else if(rowStruct.lineItemPos instanceof CMSReturnLineItem)
    //      {
    //        if(((CMSReturnLineItem)rowStruct.lineItemPos).getDocumentNumber()!=null)
    //          return true;
    //      }
    return false;
  }

  private class RowSelector {
    POSLineItem lineItemPos;
    Boolean isRowSelected;
    String sComment;

    /**
     * put your documentation comment here
     * @param     POSLineItem lineItemPos
     * @param     boolean bInitialSelect
     */
    public RowSelector(POSLineItem lineItemPos, boolean bInitialSelect) {
      this.lineItemPos = lineItemPos;
      this.isRowSelected = new Boolean(bInitialSelect);
    }
  }
}

