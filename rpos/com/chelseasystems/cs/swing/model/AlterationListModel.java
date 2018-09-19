/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.currency.*;
import java.util.*;


/**
 * <p>Title:AlterationListModel </p>
 * <p>Description:Model for AlterationList </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-21-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AlterationListModel extends ScrollableTableModel {
  /**
   * Select Column
   */
  private final int SELECT = 0;
  /**
   * Alteration Code Column
   */
  private final int ALTERATION_CODE = 1;
  /**
   * Description Column
   */
  private final int DESCRIPTION = 2;
  /**
   * Price Column
   */
  private final int PRICE = 3;
  /**
   * Column Names
   */
  private final String COLUMN_NAMES[] = {"Select", "Code", "Description", "Price"
  };

  /**
   * Default Constructor
   */
  public AlterationListModel() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES[iCtr]);
    }
    this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * GetColumn Count
   * @return int
   */
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /**
   * Set Number of rows shown in a page
   * @param RowsShown int
   */
  public void setRowsShown(int RowsShown) {
    super.setRowsShown(RowsShown);
  }

  /**
   * Get Class for column
   * @param columnIndex int
   * @return Class
   */
  public Class getColumnClass(int columnIndex) {
    return getValueAt(0, columnIndex).getClass();
  }

  /**
   * Add alteration to the model
   * @param alterationDetail AlterationDetail
   * @param bInitialSelect boolean
   */
  public void addALteration(AlterationDetail alterationDetail, boolean bInitialSelect) {
    RowSelector rowSelect = new RowSelector(bInitialSelect, alterationDetail);
    addRow(rowSelect);
  }

  /**
   * Get selected alterationDetail items.
   * @return AlterationDetail[]
   */
  public AlterationDetail[] getSelectedAlterationDetails() {
    Vector rowStructs = getAllRows();
    Vector seletedAlterationDets = new Vector();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      if (rowStruct.isRowSelected.booleanValue() == true)
        seletedAlterationDets.add(rowStruct.alterationDetail);
    }
    return rowStructs.isEmpty() ? null
        : (AlterationDetail[])seletedAlterationDets.toArray(new AlterationDetail[
        seletedAlterationDets.size()]);
  }

  /**
   * Get subtotal.
   * @return Currency
   */
  public ArmCurrency getSubTotal() {
    Vector rowStructs = getAllRows();
    ArmCurrency subTotal = new ArmCurrency(0);
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      try {
        if (rowStruct.isRowSelected.booleanValue() == true) {
          // ArmCurrency.add don't work for some reasons
          // so this is work around. (MSB)
          subTotal = new ArmCurrency(subTotal.doubleValue()
              + rowStruct.alterationDetail.getEstimatedPrice().doubleValue());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return rowStructs.isEmpty() ? null : subTotal;
  }

  /**
   * If cell is editable
   * @param row int
   * @param column int
   * @return boolean
   */
  public boolean isCellEditable(int row, int column) {
    if (column == PRICE)
      return true;
    return (false);
  }

  /**
   * Value at a cell
   * @param iRow int
   * @param iCol int
   * @return Object
   */
  public Object getValueAt(int iRow, int iCol) {
    Vector vTemp = this.getCurrentPage();
    RowSelector rowSelector = (RowSelector)vTemp.elementAt(iRow);
    AlterationDetail alterationDetail = rowSelector.alterationDetail;
    switch (iCol) {
      case SELECT:
        return rowSelector.isRowSelected;
      case ALTERATION_CODE:
        return alterationDetail.getAlterationCode();
      case DESCRIPTION:
        return alterationDetail.getDescription();
      case PRICE:
        return alterationDetail.getEstimatedPrice().formattedStringValue();
    }
    return null;
  }

  /**
   * Find row by alteration code
   * @param sAlterationCode String
   * @return int
   */
  public int findByAlterationCode(String sAlterationCode) {
    if (sAlterationCode == null || sAlterationCode.trim().length() < 1)
      return -1;
    int iCtr = 0;
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      try {
        if (rowStruct.alterationDetail.getAlterationCode().equalsIgnoreCase(sAlterationCode))
          return iCtr;
        iCtr++;
      } catch (Exception e) {
        return -1;
      }
    }
    return -1;
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public RowSelector getRowFromModel(int row) {
    int iCtr = 0;
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      try {
        RowSelector rowStruct = (RowSelector)itr.next();
        if (row == iCtr)
          return rowStruct;
        iCtr++;
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  /**
   * Check if a row is selected
   * @param row int
   * @return boolean
   */
  public boolean getRowSelected(int row) {
    if (row < 0)
      return false;
    RowSelector rowStruct = (RowSelector)getRowInPage(row);
    if (rowStruct == null || rowStruct.isRowSelected == null)
      return false;
    return rowStruct.isRowSelected.booleanValue();
  }

  /**
   * Select a row
   * @param row int
   */
  public void setRowSelected(int row) {
    if (row < 0)
      return;
    RowSelector rowStruct = (RowSelector)getRowInPage(row);
    if (rowStruct == null || rowStruct.isRowSelected == null)
      return;
    rowStruct.isRowSelected = new Boolean(!rowStruct.isRowSelected.booleanValue());
  }

  /**
   * put your documentation comment here
   * @param iRow
   */
  public void selectRow(int iRow) {
    if (iRow < 0)
      return;
    RowSelector rowStruct = (RowSelector)getRowFromModel(iRow);
    if (rowStruct == null || rowStruct.isRowSelected == null)
      return;
    rowStruct.isRowSelected = new Boolean(true);
  }

  /**
   * Select a row by alteration code.
   * @param sAlterationCode String
   */
  public void selectRowByAlterationCode(String sAlterationCode) {
    if (sAlterationCode == null || sAlterationCode.trim().length() < 1)
      ;
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      try {
        if (rowStruct == null || rowStruct.isRowSelected == null)
          return;
        if (rowStruct.alterationDetail.getAlterationCode().equalsIgnoreCase(sAlterationCode)) {
          rowStruct.isRowSelected = new Boolean(true);
        }
      } catch (Exception e) {}
    }
  }

  /**
   * Select a line by AlterationDetail
   * @param detail AlterationDetail
   */
  public void selectByAlterationDetailInList(AlterationDetail detail) {
    String sAlterationCode = detail.getAlterationCode();
    Vector rowStructs = getAllRows();
    Iterator itr = rowStructs.iterator();
    while (itr.hasNext()) {
      RowSelector rowStruct = (RowSelector)itr.next();
      try {
        if (rowStruct == null || rowStruct.isRowSelected == null)
          return;
        if (rowStruct.alterationDetail.getAlterationCode().equalsIgnoreCase(sAlterationCode)) {
          rowStruct.isRowSelected = new Boolean(true);
          rowStruct.setPrice(detail.getEstimatedPrice());
        }
      } catch (Exception e) {}
    }
  }

  /**
   * Set Price
   * @param curPrice Currency
   * @param iCtr int
   */
  public void setPriceAt(ArmCurrency curPrice, int iCtr) {
    if (iCtr < 0 || iCtr >= getRowCount() || curPrice == null)
      return;
    Vector vTemp = this.getCurrentPage();
    RowSelector rowSelector = (RowSelector)vTemp.elementAt(iCtr);
    rowSelector.setPrice(curPrice);
  }

  /**
   *
   * <p>Title: RowSelector</p>
   * <p>Description: Used to implement checkbox</p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
  private class RowSelector {
    AlterationDetail alterationDetail;
    Boolean isRowSelected;

    /**
     * put your documentation comment here
     * @param     boolean bInitialSelect
     * @param     AlterationDetail alterationDetail
     */
    public RowSelector(boolean bInitialSelect, AlterationDetail alterationDetail) {
      this.isRowSelected = new Boolean(bInitialSelect);
      this.alterationDetail = alterationDetail;
    }

    /**
     * put your documentation comment here
     * @param price
     */
    public void setPrice(ArmCurrency price) {
      alterationDetail.setEstimatedPrice(price);
    }
  }
}

