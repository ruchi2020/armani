/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.model;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import com.chelseasystems.cr.pos.TransactionHeader;
import com.chelseasystems.cs.pos.CMSTransactionHeader;

/*
 * History:
 * +------+------------+-----------+-----------+----------------------------------------------------+
 * | Ver# | Date       | By        | Defect #  | Description                                        |
 * +------+------------+-----------+-----------+----------------------------------------------------+
 * | 1    | N/A        |Retek      | N/A       | Base Version                                       |
 * +------+------------+-----------+-----------+----------------------------------------------------+
 * | 2    | 04-21-2005 |Manpreet   | N/A       | POS_104665_TS_TransactionHistory_Rev0              |
 * +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 * This is to create table model for the Transaction History Summary
 */
public class TxnHistModel_EUR extends TxnHistModel {	
	private static final long serialVersionUID = 1L;	
	public static final int COL_SELECT = 0;
	public static final int COL_DATE = 1;
	public static final int COL_STORE_ID = 2;
	public static final int COL_TXN_ID = 3;
	public static final int COL_TXN_TYPE = 4;
	public static final int COL_ASSOCIATE = 5;
	public static final int COL_TXN_TOTAL = 6;
	
	/**
	 */
	public TxnHistModel_EUR() {		
		super();
	}

	protected void initColumnHeaders() {
		COLUMN_NAMES = new String[] { ("Select"), ("Date"), ("Store ID"), ("Rec./Trans ID"), ("Txn Type"), ("Assoc."), ("Txn Total") };
	}	

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	public Object getValueAt(int row, int column) {
		Vector vTemp = getCurrentPage();
		RowSelector rowSelector = (RowSelector) vTemp.elementAt(row);
		TransactionHeader txnHeader = rowSelector.txn;		
		String fiscalreceiptNumber = ((CMSTransactionHeader)txnHeader).getFiscalReceiptNum();
		switch (column) {
			case COL_SELECT:
				return rowSelector.isRowSelected;
			case COL_DATE:
				if (txnHeader.getSubmitDate() != null)
					return format.format(txnHeader.getSubmitDate());
			case COL_STORE_ID:
				return txnHeader.getStoreId();
			case COL_TXN_ID:
				if (fiscalreceiptNumber.equals("")) {
					return txnHeader.getId();
				} else {
					return fiscalreceiptNumber + " - " + txnHeader.getId();
				}
			case COL_TXN_TYPE:
				return tranCode.get(txnHeader.getTransactionType());
			case COL_ASSOCIATE:
				return txnHeader.getConsultantId();
			case COL_TXN_TOTAL:
				return txnHeader.getTotalAmountDue().formattedStringValue();
			default:
				return " ";
		}
	}

	/**
	 * @param tblList
	 */
	public void setColumnWidth(JTable tblList) {
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		setRowsShown(tblList.getHeight() / tblList.getRowHeight());
		TableColumn selectCol = tblList.getColumnModel().getColumn(COL_SELECT);
		selectCol.setPreferredWidth((int) (60 * r));
		TableColumn datecol = tblList.getColumnModel().getColumn(COL_DATE);
		datecol.setPreferredWidth((int) (150 * r));
		TableColumn storeCol = tblList.getColumnModel().getColumn(COL_STORE_ID);
		storeCol.setPreferredWidth((int) (105 * r));
		TableColumn transNocol = tblList.getColumnModel().getColumn(COL_TXN_ID);
		transNocol.setPreferredWidth((int) (190 * r));
		TableColumn transTypecol = tblList.getColumnModel().getColumn(COL_TXN_TYPE);
		transTypecol.setPreferredWidth((int) (135 * r));
		TableColumn transTotalcol = tblList.getColumnModel().getColumn(COL_TXN_TOTAL);
		transTotalcol.setPreferredWidth((int) (150 * r));
		TableColumn consCol = tblList.getColumnModel().getColumn(COL_ASSOCIATE);
		consCol.setPreferredWidth(100);
		DefaultTableCellRenderer ctrRenderer = new DefaultTableCellRenderer();
		ctrRenderer.setHorizontalAlignment(0);
		tblList.getColumnModel().getColumn(2).setCellRenderer(ctrRenderer);
		DefaultTableCellRenderer HorAlignRenderer = new DefaultTableCellRenderer();
		HorAlignRenderer.setHorizontalAlignment(4);
		transTotalcol.setCellRenderer(HorAlignRenderer);
	}
}
