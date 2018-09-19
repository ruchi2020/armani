/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2005 |Megha   | N/A          | Build the class                                    |
 --------------------------------------------------------------------------------------------------
 */
import com.chelseasystems.cs.loyalty.Loyalty;
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
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Date;


/**
 * put your documentation comment here
 */
public class LoyaltyManagementModel extends ScrollableTableModel {
  private final String COLUMN_NAMES[] = {"Loyalty No.", "Type", "Issued by", "Issue Date"
      , "Current Bal.", "Lifetime Bal.", "Active"
  };
  public static final int LOYALTY_NUM = 0;
  public static final int TYPE = 1;
  public static final int ISSUED_BY = 2;
  public static final int ISSUE_DATE = 3;
  public static final int CURR_BAL = 4;
  public static final int LIFETIME_BAL = 5;
  public static final int ACTIVE = 6;
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private int currentSortColumn;
  boolean reverseSort = false;
  private Loyalty loyalty[];
  String date = "12/12/2003";
  Date IssueDate = new Date();

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public LoyaltyManagementModel(IApplicationManager theAppMgr) {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = "<HTML><B>" + res.getString(COLUMN_NAMES[iCtr]) + "</B></HTML>";
    }
    this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * Sets items on panel
   * @param loyalty Loyalty[]
   */
  public void setItems(Loyalty loyalty[]) {
    clear();
    this.loyalty = loyalty;
    if (loyalty == null)
      return;
    for (int i = 0; i < loyalty.length; i++) {
      addItem(loyalty[i]);
    }
    fireTableDataChanged();
  }

  /**
   * Adds the item to the Panel
   * @param loyalty Loyalty
   */
  public void addItem(Loyalty loyalty) {
    if (loyalty == null)
      return;
    addRow(loyalty);
  }

  /**
   * Deletes the item from the panel
   * @param row int
   */
  public void deleteItemAt(int row) {
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * Gets the item at particular Row
   * @param row int
   * @return Loyalty
   */
  public Loyalty getItemAt(int row) {
    return (Loyalty)this.getRowInPage(row);
  }

  /**
   * Gets the item at given(row,column)
   * @param row int
   * @param column int
   * @return Object
   */
  public Object getValueAt(int row, int column) {
    Loyalty loyalty = (Loyalty)this.getCurrentPage().elementAt(row);
    switch (column) {
      case LOYALTY_NUM:
        return loyalty.getLoyaltyNumber();
      case TYPE:
        return loyalty.getStoreType();
      case ISSUED_BY:
        return loyalty.getIssuedBy();
      case ISSUE_DATE:
        try {
          SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          df.setLenient(false);
          date = df.format(loyalty.getIssueDate());
          IssueDate = df.parse(date);
          return date.toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
        case CURR_BAL:
          return new Float(loyalty.getCurrBalance());
      case LIFETIME_BAL:
        return new Float(loyalty.getLifeTimeBalance());
      case ACTIVE:
        if (loyalty.getStatus())
          return "Y";
        else
          return "N";
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
}

