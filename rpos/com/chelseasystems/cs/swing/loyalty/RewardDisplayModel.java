/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

import java.util.Date;
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
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05/03/2005 |Megha   | N/A          | Build the class                                    |
 --------------------------------------------------------------------------------------------------
 */
import com.chelseasystems.cs.loyalty.RewardCard;
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


/**
 * put your documentation comment here
 */
public class RewardDisplayModel extends ScrollableTableModel {
  private final String COLUMN_NAMES[] = {"Reward No.", "Loyalty No", "Issue Date", "Expire Date"
      , "Amount Remaining", "Active"
  };
  public static final int REWARD_NO = 0;
  public static final int LOYALTY_NO = 1;
  public static final int ISSUE_DATE = 2;
  public static final int EXPIRE_DATE = 3;
  public static final int AMOUNT_REMAINING = 4;
  public static final int ACTIVE = 5;
  private int currentSortColumn;
  boolean reverseSort = false;
  private RewardCard rewardCard[];
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  /**
   * put your documentation comment here
   * @param   IApplicationManager theAppMgr
   */
  public RewardDisplayModel(IApplicationManager theAppMgr) {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
      sIdentiFiers[iCtr] = "<HTML><B>" + res.getString(COLUMN_NAMES[iCtr]) + "</B></HTML>";
    }
    this.setColumnIdentifiers(sIdentiFiers);
  }

  /**
   * put your documentation comment here
   * @param rewardCard[]
   */
  public void setItems(RewardCard rewardCard[]) {
    this.rewardCard = rewardCard;
    if (rewardCard == null)
      return;
    for (int i = 0; i < rewardCard.length; i++) {
      addItem(rewardCard[i]);
    }
    fireTableDataChanged();
  }

  /**
   * Adds Items to the panel
   * @param rewardCard RewardCard
   */
  public void addItem(RewardCard rewardCard) {
    if (rewardCard == null)
      return;
    addRow(rewardCard);
  }

  /**
   * Deletes Item from the panel
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
  public RewardCard getItemAt(int row) {
    return (RewardCard)this.getRowInPage(row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    RewardCard rewardCard = (RewardCard)this.getCurrentPage().elementAt(row);
    String date = "";
    Date DateObj = new Date();
    switch (column) {
      case REWARD_NO:
        return rewardCard.getId();
      case LOYALTY_NO:
        return rewardCard.getLoyalty().getLoyaltyNumber();
      case ISSUE_DATE:
        try {
          SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          df.setLenient(false);
          date = df.format(rewardCard.getCreateDate());
          DateObj = df.parse(date);
          return date;
        } catch (Exception e) {
          e.printStackTrace();
        }
        case EXPIRE_DATE:
          try {
            SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
            df.setLenient(false);
            date = df.format(rewardCard.getExpDate());
            DateObj = df.parse(date);
            return date;
          } catch (Exception e) {
            e.printStackTrace();
          }
          case AMOUNT_REMAINING:
            try {
              return rewardCard.getRemainingBalance().formattedStringValue();
            } catch (Exception e) {}
            case ACTIVE:
              if (rewardCard.getStatus())
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

