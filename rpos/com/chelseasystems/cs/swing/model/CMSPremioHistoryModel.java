package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.loyalty.CMSPremioHistory;
import com.chelseasystems.cs.loyalty.RewardCard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CMSPremioHistoryModel extends ScrollableTableModel {
	
	private final String[] COLUMN_NAMES = { "Sales_Date", "Store_Name", "Transaction_Number", "Premio_Number", "Points",
			"Premio_Discount_Amount" };
	
	public static final int TXN_DATE            = 0;
	public static final int STORE_NAME          = 1;
	public static final int TXN_NUM             = 2;
	public static final int LOYALTY_NUM         = 3;
	public static final int REDEEMED_POINTS     = 4;
	public static final int PREMIO_DISCOUNT_AMT = 5;
	
	boolean reverseSort = false;
	private CMSPremioHistory premioHistory[];
	java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

	public CMSPremioHistoryModel(IApplicationManager theAppMgr){
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
	public void setItems(CMSPremioHistory premioHistory[]) {
		this.premioHistory = premioHistory;
		if (premioHistory == null)
			return;
		for (int i = 0; i < premioHistory.length; i++) {
			addItem(premioHistory[i]);
		}
		fireTableDataChanged();
	}

	  /**
		 * Adds Items to the panel
		 * 
		 * @param premioHistory
		 *            premioHistory
		 */
	  public void addItem(CMSPremioHistory premioHistory) {
	    if (premioHistory == null)
	      return;
	    addRow(premioHistory);
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
	  public CMSPremioHistory getItemAt(int row) {
	    return (CMSPremioHistory)this.getRowInPage(row);
	  }

	  /**
	   * put your documentation comment here
	   * @param row
	   * @param column
	   * @return
	   */
	  public Object getValueAt(int row, int column) {
		CMSPremioHistory premioHistory = (CMSPremioHistory) this.getCurrentPage().elementAt(row);
		String date = "";
		Date DateObj = new Date();
		switch (column) {
		case TXN_NUM:
			return premioHistory.getTransactionID();
		case LOYALTY_NUM:
			return premioHistory.getLoyaltyNumber();
		case TXN_DATE:
			try {
				SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();                                                 
				df.setLenient(false);
				date = df.format(premioHistory.getTransactionDate());
				DateObj = df.parse(date);
				return date;
			} catch (Exception e) {
				e.printStackTrace();
			}
		case PREMIO_DISCOUNT_AMT:
				return premioHistory.getTransactionPremioDiscountAmt().getFormattedStringValue();
		case REDEEMED_POINTS:
			return premioHistory.getRedeemedPoints();
		case STORE_NAME:
			return premioHistory.getStoreName();
		}
		return "";
	}

	  /**
		 * put your documentation comment here
		 * 
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
