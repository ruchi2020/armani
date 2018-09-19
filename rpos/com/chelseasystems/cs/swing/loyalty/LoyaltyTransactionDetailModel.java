/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

import com.chelseasystems.cr.swing.*;
import java.util.TreeMap;
import java.util.Vector;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.loyalty.LoyaltyHistory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Collections;


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
public class LoyaltyTransactionDetailModel extends ScrollableTableModel {
  private final String COLUMN_NAMES[] = {"Date", "Store ID", "Txn ID", "Txn Type", "Deposits"
      , "Withdrawals"
  };
  public static final int DATE = 0;
  public static final int STORE_ID = 1;
  public static final int TXN_ID = 2;
  public static final int TXN_TYPE = 3;
  public static final int DEPOSITS = 4;
  public static final int WITHDRAWLS = 5;
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  //private int currentSortColumn;
  boolean bReverseSort = false;
  //private LoyaltyHistory loyaltyHistory[];
  String date = "12/12/2003";
  Date TxnDate = new Date();
  private int iCurrentSortColIdx = Integer.MAX_VALUE;
  private int lastSelectedTxnRow = -1;

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public LoyaltyTransactionDetailModel(IApplicationManager theAppMgr) {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = "<HTML><B>" + res.getString(COLUMN_NAMES[iCtr]) + "</B></HTML>";
    }
    this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * Set Item in the panel
   * @param loyaltyHistory LoyaltyHistory[]
   */
  public void setItems(LoyaltyHistory loyaltyHistory[]) {
    //this.loyaltyHistory = loyaltyHistory;
    if (loyaltyHistory == null)
      return;
    for (int i = 0; i < loyaltyHistory.length; i++) {
    	// remove the loyalty with 0 points
    	// fix for BUG 1680: Remove points not displayed
   	 if(loyaltyHistory[i]!=null && 
   			 (loyaltyHistory[i].getPointEarned()==0.0) && (loyaltyHistory[i].getPointUsed()==0.0)){
   		 continue;
   	 }
      addItem(loyaltyHistory[i]);
    }
    fireTableDataChanged();
  }

  /**
   * AddItem to the panel
   * @param loyaltyHistory LoyaltyHistory
   */
  public void addItem(LoyaltyHistory loyaltyHistory) {
    if (loyaltyHistory == null)
      return;
    addRow(loyaltyHistory);
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
  public LoyaltyHistory getItemAt(int row) {
    if (row < 0)
      return null;
    return (LoyaltyHistory)this.getRowInPage(row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    LoyaltyHistory loyaltyHistory = (LoyaltyHistory)this.getCurrentPage().elementAt(row);
    double PointEarned = loyaltyHistory.getPointEarned();
    switch (column) {
      case DATE:
        try {
          SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          df.setLenient(false);
          date = df.format(loyaltyHistory.getTransactionDate());
          TxnDate = df.parse(date);
          return date.toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
        case STORE_ID:
          return loyaltyHistory.getStoreID();
      case TXN_ID:
        return loyaltyHistory.getTransactionId();
      case TXN_TYPE:
        return loyaltyHistory.getTransactionType();
      case DEPOSITS:
        if (loyaltyHistory.getTransactionType().equals("REWD"))
          return "";
       /* if (PointEarned > 0.0) {
       //   return new Double(PointEarned);
        	return String.valueOf((int)PointEarned);
        }*/
        if(loyaltyHistory.getPointEarned()>0){
        	return String.valueOf((int)loyaltyHistory.getPointEarned());
        }
        return "";
      case WITHDRAWLS:
        if (loyaltyHistory.getTransactionType().equals("REWD") && PointEarned > 0.0) //to take care of existing data inconsistancy
          return new Double(PointEarned * -1);
/*        if (PointEarned < 0.0)
      //    return new Double(PointEarned);
        	return String.valueOf((int)PointEarned*-1);*/
        if(loyaltyHistory.getPointUsed()<0){
        	return String.valueOf((int)loyaltyHistory.getPointUsed()*-1);
        }
        
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
   * Sorts table by column index
   * @param sortColumn ColumnIndex
   * @param selectedTranHeader TransactionHeader
   * @return int
   */
  public int sortByColumnType(int sortColumn, String selectedTransactionID) {
    if (getAllRows().size() < 1)
      return -1;
    TreeMap sortColumnMap = new TreeMap();
    int selectedRow = -1;
    if (sortColumn < 0) {
      bReverseSort = !bReverseSort;
      if (sortColumn == Integer.MIN_VALUE)
        sortColumn = 0;
      else
        sortColumn *= -1;
    } else {
      if (iCurrentSortColIdx == sortColumn && iCurrentSortColIdx != Integer.MAX_VALUE)
        bReverseSort = !bReverseSort;
      else {
        bReverseSort = false;
        iCurrentSortColIdx = sortColumn;
      }
    }
    Vector vecRows = this.getAllRows();
    int iCtr = 0;
    LoyaltyHistory loyaltyHistory;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
    DecimalFormat decimalFormat = new DecimalFormat("000000000000000.00"); //15.2
    if (sortColumn == DEPOSITS || sortColumn == WITHDRAWLS) {
      super.clear();
      Collections.sort(vecRows, new LoyaltyPointComparator());
      for (int i = 0; i < vecRows.size(); i++) {
        removeRowInModel(vecRows.elementAt(i));
      }
      for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
        if (bReverseSort) {
          loyaltyHistory = (LoyaltyHistory)vecRows.elementAt(vecRows.size() - iCtr - 1);
        } else {
          loyaltyHistory = (LoyaltyHistory)vecRows.elementAt(iCtr);
        }
        addItem(loyaltyHistory);
        if (selectedTransactionID != null
            && loyaltyHistory.getTransactionId().equals(selectedTransactionID))
          selectedRow = iCtr;
      }
      fireTableDataChanged();
      //return selectedRow;
    } else {
      for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
        loyaltyHistory = (LoyaltyHistory)vecRows.elementAt(iCtr);
        switch (sortColumn) {
          case STORE_ID:
            sortColumnMap.put(loyaltyHistory.getStoreID() + ","
                + dateFormat.format(loyaltyHistory.getTransactionDate()) + ","
                + loyaltyHistory.getTransactionId(), loyaltyHistory);
            break;
          case TXN_ID:
            sortColumnMap.put(loyaltyHistory.getTransactionId(), loyaltyHistory);
            break;
          case TXN_TYPE:
            sortColumnMap.put(loyaltyHistory.getTransactionType() + ","
                + dateFormat.format(loyaltyHistory.getTransactionDate()) + ","
                + loyaltyHistory.getTransactionId(), loyaltyHistory);
            break;
          default:
            sortColumnMap.put(dateFormat.format(loyaltyHistory.getTransactionDate()) + ","
                + loyaltyHistory.getTransactionId(), loyaltyHistory);
        }
      }
      if (sortColumnMap.size() > 0) {
        super.clear();
        Vector vecSortedHeaders = new Vector(sortColumnMap.values());
        for (int i = 0; i < vecSortedHeaders.size(); i++) {
          removeRowInModel(vecSortedHeaders.elementAt(i));
        }
        for (iCtr = 0; iCtr < vecSortedHeaders.size(); iCtr++) {
          if (bReverseSort) {
            loyaltyHistory = (LoyaltyHistory)vecSortedHeaders.elementAt(vecSortedHeaders.size()
                - iCtr - 1);
          } else {
            loyaltyHistory = (LoyaltyHistory)vecSortedHeaders.elementAt(iCtr);
          }
          addItem(loyaltyHistory);
          if (selectedTransactionID != null
              && loyaltyHistory.getTransactionId().equals(selectedTransactionID))
            selectedRow = iCtr;
        }
        fireTableDataChanged();
      }
    }
    lastSelectedTxnRow = selectedRow;
    return selectedRow;
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
    //loyaltyHistory = null;
    bReverseSort = false;
    iCurrentSortColIdx = Integer.MAX_VALUE;
    super.clear();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getLastSelectedTxnRow() {
    return lastSelectedTxnRow;
  }

  /**
   * put your documentation comment here
   * @param lastSelectedTxnRow
   */
  public void setLastSelectedTxnRow(int lastSelectedTxnRow) {
    this.lastSelectedTxnRow = lastSelectedTxnRow;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public LoyaltyHistory[] getLoyaltyHistoryArray() {
    return (LoyaltyHistory[])getAllRows().toArray(new LoyaltyHistory[0]);
  }

  private class LoyaltyPointComparator implements Comparator {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");

    /**
     * put your documentation comment here
     * @param obj1
     * @param obj2
     * @return
     */
    public int compare(Object obj1, Object obj2) {
      LoyaltyHistory loyaltyHistory1 = (LoyaltyHistory)obj1;
      LoyaltyHistory loyaltyHistory2 = (LoyaltyHistory)obj2;
      double points1 = loyaltyHistory1.getPointEarned();
      double points2 = loyaltyHistory2.getPointEarned();
      if (loyaltyHistory1.getTransactionType().equals("REWD") && points1 > 0)
        points1 *= -1;
      if (loyaltyHistory2.getTransactionType().equals("REWD") && points2 > 0)
        points2 *= -1;
      if (points1 < points2)
        return -1;
      if (points1 > points2)
        return 1;
      return (dateFormat.format(loyaltyHistory1.getTransactionDate()) + ","
          + loyaltyHistory1.
          getTransactionId()).compareTo(dateFormat.format(loyaltyHistory2.getTransactionDate())
          + "," + loyaltyHistory2.getTransactionId());
    }
  }
}

