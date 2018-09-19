/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

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
import com.chelseasystems.cs.loyalty.LoyaltyHistory;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.chelseasystems.cs.customer.DepositHistory;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import com.chelseasystems.cs.util.HistoryComparator;

import java.util.Comparator;
import java.util.Collections;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.config.ConfigMgr;


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
public class DepositHistoryModel extends ScrollableTableModel {

  /**
   * put your documentation comment here
   */
  public DepositHistoryModel() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static final String CONFIGURATION_FILE = "pos.cfg";
  private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
  String custDepositHistModelString = configMgr.getString("CUSTOMER.CUST_DEPOSIT_HIST_MODEL");
  private String[] COLUMN_NAMES;  
  public static final int DATE = 0;
  public static final int STORE_ID = 1;
  public static final int TXN_ID = 2;
  public static final int TXN_TYPE = 3;
  public static final int ASSOC = 4;
  public static final int AMOUNT = 5;
  private String[] sHotKeys;
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  private int currentSortColumn;
  boolean reverseSort = false;
  private DepositHistory depositHistory[];
  String date = "12/12/2003";
  Date TxnDate = new Date();
  private int lastSelectedRow = -1;

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public DepositHistoryModel(IApplicationManager theAppMgr) {
    makeColumnHeaders();
  }

  /**
   * Set Item in the panel
   * @param loyaltyHistory LoyaltyHistory[]
   */
  public void setItems(DepositHistory depositHistory[]) {
    this.depositHistory = depositHistory;
    if (depositHistory == null)
      return;
    for (int i = 0; i < depositHistory.length; i++) {
      addItem(depositHistory[i]);
    }
    Vector vecRows = this.getAllRows();
    Collections.sort(vecRows, new HistoryComparator());
    fireTableDataChanged();
  }

  /**
   * AddItem to the panel
   * @param loyaltyHistory LoyaltyHistory
   */
  public void addItem(DepositHistory depositHistory) {
    if (depositHistory == null)
      return;
    addRow(depositHistory);
  }

  /**
   * DeleteItem from panel
   * @param row int
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
  public DepositHistory getItemAt(int row) {
    if (row < 0)
      return null;
    return (DepositHistory)this.getRowInPage(row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    DepositHistory depositHistory = (DepositHistory)this.getCurrentPage().elementAt(row);
    switch (column) {
      case DATE:
        try {
          SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          df.setLenient(false);
          date = df.format(depositHistory.getTransactionDate());
          TxnDate = df.parse(date);
          return date.toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
        case STORE_ID:
        	//PCR1326 DepositHistory fix for Armani Japan
  		  	if(custDepositHistModelString == null) {
  		  		return depositHistory.getStoreID();
  		  	} else {
  		  		return depositHistory.getStoreName();
  		  	}
          //return depositHistory.getStoreID();
      case TXN_ID:
        return depositHistory.getTransactionId();
      case TXN_TYPE:
        return depositHistory.getTransactionType();
      case ASSOC:
    	  //PCR1326 DepositHistory fix for Armani Japan	
    	  if(custDepositHistModelString == null) {
    		  return depositHistory.getassoc();
    	  } else {
    		  return depositHistory.getAssocLastName() + " " + depositHistory.getAssocFirstName();
    	  }
        //return depositHistory.getassoc();
      case AMOUNT:
        return depositHistory.getamount().formattedStringValue();
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
   * @param selectedHistory
   * @return
   */
  public int sortItems(int sortColumn, DepositHistory selectedHistory) {
    if (getAllRows().size() < 1)
      return -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
    TreeMap sortColumnMap = new TreeMap();
    int selectedRow = -1;
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
    Vector vecRows = this.getAllRows();
    int iCtr = 0;
    DepositHistory history;
    {
      super.clear();
      Collections.sort(vecRows, new DepositHistoryComparator(sortColumn));
      for (int i = 0; i < vecRows.size(); i++) {
        removeRowInModel(vecRows.elementAt(i));
      }
      for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
        if (reverseSort)
          history = (DepositHistory)vecRows.elementAt(vecRows.size() - iCtr - 1);
        else
          history = (DepositHistory)vecRows.elementAt(iCtr);
        addItem(history);
        if (selectedHistory != null && history.equals(selectedHistory))
          selectedRow = iCtr;
      }
      fireTableDataChanged();
      //return selectedRow;
    }
    lastSelectedRow = selectedRow;
    return selectedRow;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getLastSelectedRow() {
    return lastSelectedRow;
  }

  /**
   * put your documentation comment here
   * @param lastSelectedRow
   */
  public void setLastSelectedRow(int lastSelectedRow) {
    this.lastSelectedRow = lastSelectedRow;
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
   * Make column headers
   * Picks up column names and fetches their hot key from
   * message bundle.
   */
  private void makeColumnHeaders() {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    HTMLColumnHeaderUtil util = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().
        getTextFieldFont());
    if (custDepositHistModelString == null) {
		COLUMN_NAMES = new String[] {"Date", "Store ID", "Txn ID", "Txn Type", "Assoc.", "Amount"};
	} else {
		COLUMN_NAMES = new String[] {"Date", "Store Name", "Txn ID", "Txn Type", "Assoc.", "Amount"};
	}
    sHotKeys = new String[COLUMN_NAMES.length];
    String sTmp = "";
    StringBuffer columnTag = new StringBuffer();    
    String col_identifiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sTmp = res.getString(COLUMN_NAMES[iCtr]);
      columnTag = new StringBuffer(res.getString(COLUMN_NAMES[iCtr]));
      switch (iCtr) {
        case DATE:
        case STORE_ID:
        case TXN_ID:
        case TXN_TYPE:
        case ASSOC:
        case AMOUNT:
          String hotKey = res.getString(COLUMN_NAMES[iCtr] + "_HOT_KEY");
          if (hotKey != null && !hotKey.trim().equals("")) {
            sHotKeys[iCtr] = hotKey;
            hotKey = hotKey.toUpperCase();
            int index = sTmp.toUpperCase().indexOf(hotKey);
            if (index >= 0) {
              columnTag.insert(sTmp.toUpperCase().indexOf(hotKey), "<U>");
              columnTag.insert(sTmp.toUpperCase().indexOf(hotKey) + 4, "</U>");
            }
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
   * @exception Exception
   */
  private void jbInit()
      throws Exception {}


  public void clear() {
    currentSortColumn = 0;
    reverseSort = false;
    depositHistory = null;
    date = "12/12/2003";
    TxnDate = new Date();
    lastSelectedRow = -1;
    super.clear();
  }


  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public DepositHistory getDepositHistory(int row) {
    return (DepositHistory)getRowInPage(row);
  }

  private class DepositHistoryComparator implements Comparator {
    private int column;

    /**
     * put your documentation comment here
     * @param     int column
     */
    public DepositHistoryComparator(int column) {
      this.column = column;
    }

    /**
     * put your documentation comment here
     * @param obj1
     * @param obj2
     * @return
     */
    public int compare(Object obj1, Object obj2) {
      int initialCompare = 0;
      DepositHistory history1 = (DepositHistory)obj1;
      DepositHistory history2 = (DepositHistory)obj2;
      switch (column) {
        case DATE:
          initialCompare = history1.getTransactionDate().compareTo(history2.getTransactionDate());
          break;
        case STORE_ID:
          initialCompare = history1.getStoreID().compareTo(history2.getStoreID());
          break;
        case TXN_ID:
          initialCompare = history1.getTransactionId().compareTo(history2.getTransactionId());
          break;
        case TXN_TYPE:
          initialCompare = history1.getTransactionType().compareTo(history2.getTransactionType());
          break;
        case ASSOC:
          initialCompare = history1.getassoc().compareTo(history2.getassoc());
          break;
        case AMOUNT:
          try {
            if (history1.getamount().lessThan(history2.getamount()))
              initialCompare = -1;
            if (history1.getamount().greaterThan(history2.getamount()))
              initialCompare = 1;
          } catch (CurrencyException e) {
            e.printStackTrace();
          }
          break
              ;
      }
      if (initialCompare == 0) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
        return (dateFormat.format(history1.getTransactionDate()) + ","
            + history1.getTransactionId().
            trim()).compareTo(dateFormat.format(history2.getTransactionDate()) + ","
            + history2.getTransactionId().trim());
      } else
        return initialCompare;
    }
  }
}

