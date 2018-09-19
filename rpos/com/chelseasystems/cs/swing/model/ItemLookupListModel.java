/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 05/17/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/27/2005 | Vikram    | N/A       | POS_104665_FS_ItemLookup_Rev1                      |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.item.*;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import com.chelseasystems.cr.util.HTML;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import java.util.Hashtable;


/**
 * <p>Title:ItemLookupListModel </p>
 * <p>Description: Model Object for ItemLookupListPanel</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupListModel extends ScrollableTableModel {
  //private final String COLUMN_NAMES[] = {"<u>S</u>KU","Style/<br>Ref","Brand","Mdl","Fbr","<u>C</u>lr","Suplr","Yr","Season","Si<u>z</u>e","Desc","Price"};
  private final String COLUMN_NAMES[] = {"SKU", "Style/Ref", "Brand", "Mdl", "Fbr", "Clr", "Suplr"
      , "Yr", "Season", "Size", "Desc", "Price"
  };
  public static final int SKU = 0;
  public static final int STYLE = 1;
  public static final int BRAND = 2;
  public static final int MODEL = 3;
  public static final int FABRIC = 4;
  public static final int COLOR = 5;
  public static final int SUPPLIER = 6;
  public static final int YEAR = 7;
  public static final int SEASON = 8;
  public static final int SIZE = 9;
  public static final int DESCRIPTION = 10;
  public static final int PRICE = 11;
  public static final int STORE = 12;
  private String[] sHotKeys;
  private int currentSortColumn = Integer.MAX_VALUE;
  boolean reverseSort = false;
  private CMSItem cmsItems[];
  //private String sHotKeys[];
  private int lastSelectedItemRow = -1;

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public ItemLookupListModel(IApplicationManager theAppMgr) {
    //    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    //    String sIdentiFiers [] = new String[COLUMN_NAMES.length];
    //    for(int iCtr=0; iCtr < COLUMN_NAMES.length; iCtr++)
    //    {
    //        sIdentiFiers[iCtr] = "<HTML><B>"+res.getString(COLUMN_NAMES[iCtr])+"</B></HTML>";
    //    }
    //    this.setColumnIdentifiers(sIdentiFiers);
    makeColumnHeaders();
  }

  /**
   * Make column headers
   * Picks up column names and fetches their hot key from
   * message bundle.
   */
  private void makeColumnHeaders() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    HTMLColumnHeaderUtil util = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().
        getTextFieldFont());
    sHotKeys = new String[COLUMN_NAMES.length];
    String sTmp = "";
    StringBuffer columnTag = new StringBuffer();
    String col_identifiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sTmp = res.getString(COLUMN_NAMES[iCtr]);
      columnTag = new StringBuffer(res.getString(COLUMN_NAMES[iCtr]));
      switch (iCtr) {
        case SKU:
        case COLOR:
        case SIZE:
          String hotKey = res.getString(COLUMN_NAMES[iCtr] + "_HOT_KEY");
          if (hotKey != null && !hotKey.trim().equals("")) {
            sHotKeys[iCtr] = hotKey;
            hotKey = hotKey.toUpperCase();
            int index = sTmp.toUpperCase().indexOf(hotKey);
            if (index >= 0) {
              columnTag.insert(sTmp.toUpperCase().indexOf(hotKey), "<U>");
              columnTag.insert(sTmp.toUpperCase().indexOf(hotKey) + 4, "</U>");
            } else
              System.out.println("!!!----------  ERROR: HotKey character '" + sHotKeys[iCtr]
                  + "' missing in column name '" + sTmp + "'");
          }
      }
      col_identifiers[iCtr] = util.getHTMLHeaderFor(columnTag.toString());
    }
    this.setColumnIdentifiers(col_identifiers);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getHotKeyArray() {
    return sHotKeys;
  }

  /**
   * put your documentation comment here
   * @param cmsItems
   */
  public void setItems(CMSItem[] cmsItems) {
    this.cmsItems = cmsItems;
    if (cmsItems == null)
      return;
    for (int i = 0; i < cmsItems.length; i++) {
      addItem(cmsItems[i]);
    }
    reverseSort = false;
    currentSortColumn = Integer.MAX_VALUE;
    fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @param cmsItem
   */
  public void addItem(CMSItem cmsItem) {
    if (cmsItem == null)
      return;
    addRow(cmsItem);
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void deleteItemAt(int row) {
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public CMSItem getItemAt(int row) {
    return (CMSItem)this.getRowInPage(row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    CMSItem cmsItem = (CMSItem)this.getCurrentPage().elementAt(row);
    switch (column) {
      case SKU:
        return cmsItem.getId();
      case STYLE:
        return cmsItem.getStyleNum();
      case BRAND:
        return cmsItem.getBrand();
      case MODEL:
        return cmsItem.getModel();
      case FABRIC:
        return cmsItem.getFabric();
      case COLOR:
        return cmsItem.getColorId();
      case SUPPLIER:
        return cmsItem.getSupplierId();
      case YEAR:
        return cmsItem.getForTheYear();
      case SEASON:
        return cmsItem.getItemDetail().getSeasonDesc();
      case SIZE:
        String sizeIndex = cmsItem.getItemDetail().getSizeIndx();
        if (sizeIndex == null && cmsItem.getSizeId() != null)
          sizeIndex = cmsItem.getSizeId().trim();
        else if (sizeIndex == null)
          sizeIndex = "";
        if (cmsItem.getItemDetail().getExtSizeIndx() != null)
          sizeIndex = sizeIndex.trim() + ":" + cmsItem.getItemDetail().getExtSizeIndx().trim();
        return sizeIndex;
      case DESCRIPTION:
        return cmsItem.getDescription();
      case PRICE:
        return cmsItem.getRetailPrice().getFormattedStringValue();
      case STORE:
        return cmsItem.getStoreId();
    }
    return "";
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
   * @return
   */
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   * @param sortColumn
   * @param selectedItemSKU
   * @return
   */
  public int sortItems(int sortColumn, String selectedItemSKU) {
    if (cmsItems == null)
      return -1;
    TreeMap sortColumnMap = new TreeMap();
    int selectedItemRow = -1;
    if (sortColumn < 0) {
      reverseSort = !reverseSort;
      if (sortColumn == Integer.MIN_VALUE)
        sortColumn = 0;
      else
        sortColumn *= -1;
    } else {
      if (currentSortColumn == sortColumn && currentSortColumn != Integer.MAX_VALUE)
        reverseSort = !reverseSort;
      else {
        reverseSort = false;
        currentSortColumn = sortColumn;
      }
    }
    switch (sortColumn) {
      //        case SKU:
      //            for(int i=0; i<cmsItems.length; i++)
      //            {
      //                sortColumnMap.put(cmsItems[i].getId().trim(), cmsItems[i]);
      //            }
      //            break;
      case COLOR:
        for (int i = 0; i < cmsItems.length; i++) {
          sortColumnMap.put(cmsItems[i].getColorId().toLowerCase().trim() + ","
              + cmsItems[i].getId().trim(), cmsItems[i]);
        }
        break;
      case SIZE:
        for (int i = 0; i < cmsItems.length; i++) {
          String sizeIndex = cmsItems[i].getItemDetail().getSizeIndx();
          if (sizeIndex == null && cmsItems[i].getSizeId() != null)
            sizeIndex = cmsItems[i].getSizeId().trim();
          else if (sizeIndex == null)
            sizeIndex = "";
          if (cmsItems[i].getItemDetail().getExtSizeIndx() != null)
            sizeIndex = sizeIndex.trim() + ":" + cmsItems[i].getItemDetail().getExtSizeIndx().trim();
          sortColumnMap.put(sizeIndex.toLowerCase().trim() + "," + cmsItems[i].getId().trim()
              , cmsItems[i]);
        }
        break;
        //case SKU:
      default:
        for (int i = 0; i < cmsItems.length; i++) {
          sortColumnMap.put(cmsItems[i].getId().trim(), cmsItems[i]);
        }
        //break;
    }
    if (sortColumnMap.size() > 0) {
      for (int i = 0; i < cmsItems.length; i++) {
        removeRowInModel(cmsItems[i]);
      }
      CMSItem tempItem = null;
      Vector sortedItemsVec = new Vector(sortColumnMap.values());
      for (int i = 0; i < sortedItemsVec.size(); i++) {
        if (reverseSort)
          tempItem = (CMSItem)sortedItemsVec.elementAt(sortedItemsVec.size() - i - 1);
        else
          tempItem = (CMSItem)sortedItemsVec.elementAt(i);
        addItem(tempItem);
        if (selectedItemSKU != null && tempItem != null
            && selectedItemSKU.trim().equals(tempItem.getId().trim()))
          selectedItemRow = i;
      }
      fireTableDataChanged();
    }
    lastSelectedItemRow = selectedItemRow;
    return selectedItemRow;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getCurrentSortColumnAndType() {
    if (currentSortColumn == 0 && reverseSort)
      return Integer.MIN_VALUE;
    return (reverseSort) ? -currentSortColumn : currentSortColumn;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getLastSelectedItemRow() {
    return lastSelectedItemRow;
  }

  /**
   * put your documentation comment here
   * @param lastSelectedItemRow
   */
  public void setLastSelectedItemRow(int lastSelectedItemRow) {
    this.lastSelectedItemRow = lastSelectedItemRow;
  }
}

