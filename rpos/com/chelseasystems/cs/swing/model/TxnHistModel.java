/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.model;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import java.text.SimpleDateFormat;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.pos.TransactionHeader;
import java.util.ResourceBundle;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.pos.CMSTransactionHeader;
import com.chelseasystems.cs.util.HTMLColumnHeaderUtil;
import java.util.Comparator;

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
public class TxnHistModel extends ScrollableTableModel {
	protected SimpleDateFormat format;
	private INIFile file;
	protected HashMap tranCode;
	protected String COLUMN_NAMES[] = null;
	public static final int COL_DATE = 0;
	public static final int COL_STORE_ID = 1;
	public static final int COL_TXN_ID = 2;
	public static final int COL_TXN_TYPE = 3;
	public static final int COL_ASSOCIATE = 4;
	public static final int COL_TXN_TOTAL = 5;
	private String sHotKeys[];
	private int iCurrentSortColIdx = Integer.MAX_VALUE;
	boolean bReverseSort = false;
	ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
	private int lastSelectedTxnRow = -1;

	/**
	 */
	public TxnHistModel() {
		format = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateTimeFormat();
		initColumnHeaders();
		makeColumnHeaders();
		try {
			file = new INIFile(FileMgr.getLocalFile("config", "pos.cfg"), true);
			tranCode = new HashMap();
			String transTypes = file.getValue("TRANS_TYPES");
			StringTokenizer st = new StringTokenizer(transTypes, ",");
			while(st.hasMoreElements()) {
				String key = (String) st.nextElement();
				String transTypeCode = file.getValue(key, "CODE", "");
				String transTypeShortDesc = file.getValue(key, "SHORT_DESC", "");
				tranCode.put(transTypeCode, transTypeShortDesc);
			}
		} catch (Exception e) {
			CMSApplet.theAppMgr.showExceptionDlg(e);
		}
	}

	protected void initColumnHeaders() {
		COLUMN_NAMES = new String[] { ("Date"), ("Store ID"), ("Txn ID"), ("Txn Type"), ("Assoc."), ("Txn Total") };
	}

	/**
	 * Make column headers Picks up column names and fetches their hot key from message bundle.
	 */
	private void makeColumnHeaders() {
		HTMLColumnHeaderUtil util = new HTMLColumnHeaderUtil(CMSApplet.theAppMgr.getTheme().getTextFieldFont());
		sHotKeys = new String[COLUMN_NAMES.length];
		String sTmp = "";
		StringBuffer columnTag = new StringBuffer();
		String col_identifiers[] = new String[COLUMN_NAMES.length];
		for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
			sTmp = res.getString(COLUMN_NAMES[iCtr]);
			columnTag = new StringBuffer(res.getString(COLUMN_NAMES[iCtr]));
			sHotKeys[iCtr] = res.getString(COLUMN_NAMES[iCtr] + "_HOT_KEY").toUpperCase();
			try {
				columnTag.insert(sTmp.toUpperCase().indexOf(sHotKeys[iCtr]), "<U>");
				columnTag.insert(sTmp.toUpperCase().indexOf(sHotKeys[iCtr]) + 4, "</U>");
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("!!!----------  ERROR: HotKey character '" + sHotKeys[iCtr] + "' missing in column name '" + sTmp + "'");
			}
			col_identifiers[iCtr] = util.getHTMLHeaderFor(columnTag.toString());
		}
		this.setColumnIdentifiers(col_identifiers);
	}

	/**
	 * @param txn
	 */
	public void addTxnHeader(TransactionHeader txn) {
		addRow(new RowSelector(txn));
		if (getTotalRowCount() > getRowsShown()) {
			lastPage();
		}
	}

	/**
	 * @param row
	 * @return
	 */
	public TransactionHeader getTransactionHeader(int row) {
		if (row < 0)
			return null;
		return ((RowSelector) this.getRowInPage(row)).txn;
	}

	/**
	 * @return TransactionHeader[]
	 */
	public TransactionHeader[] getTxnHeaders() {
		Vector rows = getAllRows();
		Vector txnHeaders = new Vector();
		Iterator itr = rows.iterator();
		while(itr.hasNext()) {
			RowSelector rowSelector = (RowSelector) itr.next();
			txnHeaders.add(rowSelector.txn);
		}
		return rows.isEmpty() ? null : 
			(TransactionHeader[]) txnHeaders.toArray(new TransactionHeader[txnHeaders.size()]);
	}

	/**
	 * @return
	 */
	public CMSTransactionHeader[] getCMSTxnHeaders() {
		Vector rows = getAllRows();
		Vector cmsTxnHeaders = new Vector();
		Iterator itr = rows.iterator();
		while(itr.hasNext()) {
			RowSelector rowSelector = (RowSelector) itr.next();
			cmsTxnHeaders.add(rowSelector.txn);
		}
		return rows.isEmpty() ? null : 
			(CMSTransactionHeader[]) cmsTxnHeaders.toArray(new CMSTransactionHeader[cmsTxnHeaders.size()]);
	}

	/**
	 * @return
	 */
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	/**
	 * Get HotKey Character for Column
	 * @param iIndex
	 *            ColumnNumber
	 * @return char
	 */
	public String getColumnHotKey(int iIndex) {
		if (iIndex < 0 || iIndex >= COLUMN_NAMES.length)
			return null;
		return sHotKeys[iIndex];
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param row
	 * @return
	 */
	public boolean getRowSelected(int row) {
		if (row < 0)
			return false;
		RowSelector rowStruct = (RowSelector) getRowInPage(row);
		return rowStruct.isRowSelected.booleanValue();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public TransactionHeader[] getSelectedRows() {
		Vector allRows = getAllRows();
		Vector selectedRows = new Vector();
		Iterator itr = allRows.iterator();
		while(itr.hasNext()) {
			RowSelector rowSelector = (RowSelector) itr.next();
			if (rowSelector.isRowSelected.booleanValue())
				selectedRows.add(rowSelector.txn);
		}
		return (allRows.isEmpty() ? null : 
			(TransactionHeader[]) selectedRows.toArray(new TransactionHeader[selectedRows.size()]));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param row
	 */
	public void setRowSelected(int row) {
		if (row < 0)
			return;
		RowSelector rowStruct = (RowSelector) getRowInPage(row);
		rowStruct.isRowSelected = new Boolean(!rowStruct.isRowSelected.booleanValue());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param row
	 * @param selected
	 */
	public void setRowSelected(int row, boolean selected) {
		if (row < 0)
			return;
		RowSelector rowStruct = (RowSelector) getRowInPage(row);
		rowStruct.isRowSelected = new Boolean(selected);
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
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
		switch (column) {
			case COL_DATE:
				if (txnHeader.getSubmitDate() != null)
					// return format.format(txnHeader.getProcessDate());
					// Fix for 1556: Time on View Transactions on Test system is 00:00 a.m.
					return format.format(txnHeader.getSubmitDate());
			case COL_STORE_ID:
				return txnHeader.getStoreId();
			case COL_TXN_ID:
				return txnHeader.getId();
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
		TableColumn datecol = tblList.getColumnModel().getColumn(COL_DATE);
		datecol.setPreferredWidth((int) (150 * r));
		TableColumn storeCol = tblList.getColumnModel().getColumn(COL_STORE_ID);
		storeCol.setPreferredWidth((int) (105 * r));
		TableColumn transNocol = tblList.getColumnModel().getColumn(COL_TXN_ID);
		transNocol.setPreferredWidth((int) (170 * r));
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

	/**
	 * put your documentation comment here
	 * @return
	 */
	public int getCurrentSortColumnAndType() {
		if (iCurrentSortColIdx == 0 && bReverseSort)
			return Integer.MIN_VALUE;
		return (bReverseSort) ? -iCurrentSortColIdx : iCurrentSortColIdx;
	}

	/**
	 * Sorts on following basis: 1). Most current Transaction to oldest for Current Store. 2). Most current Transaction to oldest Store independent.
	 * 
	 * @param selectedTranHeaderID
	 *            String
	 * @return int
	 */
	public int doInitialSort(String selectedTranHeaderID) {
		if (getAllRows().size() < 1)
			return -1;
		int iSelectedRow = -1;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
		Vector vecSortedHeaders;
		TreeMap currentStoreMap = new TreeMap();
		TreeMap otherStoresMap = new TreeMap();
		Vector vecRows = this.getAllRows();
		int iCtr = 0;
		int iTranCtr = 0;
		TransactionHeader tHeader;
		RowSelector rowSelector;
		String sCurrentStoreId = CMSApplet.theStore.getId();
		for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
			rowSelector = (RowSelector) vecRows.elementAt(iCtr);
			tHeader = rowSelector.txn;
			if (tHeader.getStoreId().equals(sCurrentStoreId)) {
				currentStoreMap.put(dateFormat.format(tHeader.getSubmitDate()) + "," + tHeader.getId(), tHeader);
			} else {
				otherStoresMap.put(dateFormat.format(tHeader.getSubmitDate()) + "," + tHeader.getId(), tHeader);
			}
		}
		if (currentStoreMap.size() > 0 || otherStoresMap.size() > 0)
			super.clear();
		if (currentStoreMap.size() > 0) {
			vecSortedHeaders = new Vector(currentStoreMap.values());
			for (int i = 0; i < vecSortedHeaders.size(); i++) {
				removeRowInModel(vecSortedHeaders.elementAt(i));
			}
			for (iCtr = 0; iCtr < vecSortedHeaders.size(); iCtr++, iTranCtr++) {
				tHeader = (TransactionHeader) vecSortedHeaders.elementAt(vecSortedHeaders.size() - iCtr - 1);
				addTxnHeader(tHeader);
				if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
					iSelectedRow = iTranCtr;
			}
			fireTableDataChanged();
		}
		if (otherStoresMap.size() > 0) {
			vecSortedHeaders = new Vector(otherStoresMap.values());
			for (int i = 0; i < vecSortedHeaders.size(); i++) {
				removeRowInModel(vecSortedHeaders.elementAt(i));
			}
			for (iCtr = 0; iCtr < vecSortedHeaders.size(); iCtr++, iTranCtr++) {
				tHeader = (TransactionHeader) vecSortedHeaders.elementAt(vecSortedHeaders.size() - iCtr - 1);
				addTxnHeader(tHeader);
				if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
					iSelectedRow = iTranCtr;
			}
			fireTableDataChanged();
		}
		iCurrentSortColIdx = Integer.MAX_VALUE;
		bReverseSort = false;
		return iSelectedRow;
	}

	/**
	 * Sorts table by column index
	 * @param sortColumn
	 *            ColumnIndex
	 * @param selectedTranHeader
	 *            TransactionHeader
	 * @return int
	 */
	public int sortByColumnType(int sortColumn, String selectedTranHeaderID) {
		if (getAllRows().size() < 1)
			return -1;
		if (sortColumn == Integer.MAX_VALUE)
			return doInitialSort(selectedTranHeaderID);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
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
		Vector rows = this.getAllRows();
		Vector vecRows = new Vector();
		Iterator itr = rows.iterator();
		while(itr.hasNext()) {
			vecRows.add(((RowSelector) (itr.next())).txn);
		}
		int iCtr = 0;
		TransactionHeader tHeader;
		if (sortColumn == COL_TXN_TOTAL) {
			super.clear();
			Collections.sort(vecRows, new TotalAmountDueComparator());
			for (int i = 0; i < vecRows.size(); i++) {
				removeRowInModel(vecRows.elementAt(i));
			}
			for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
				if (bReverseSort) {
					tHeader = (TransactionHeader) vecRows.elementAt(vecRows.size() - iCtr - 1);
				} else {
					tHeader = (TransactionHeader) vecRows.elementAt(iCtr);
				}
				addTxnHeader(tHeader);
				if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
					selectedRow = iCtr;
			}
			fireTableDataChanged();
			// return selectedRow;
		} else {
			for (iCtr = 0; iCtr < vecRows.size(); iCtr++) {
				tHeader = (TransactionHeader) vecRows.elementAt(iCtr);
				switch (sortColumn) {
					case COL_STORE_ID:
						sortColumnMap.put(tHeader.getStoreId() + "," + tHeader.getId(), tHeader);
						break;
					case COL_TXN_TYPE:
						sortColumnMap.put(tHeader.getTransactionType() + "," + tHeader.getId(), tHeader);
						break;
					case COL_ASSOCIATE:
						sortColumnMap.put(tHeader.getConsultantId() + "," + tHeader.getId() + dateFormat.format(tHeader.getSubmitDate()), tHeader);
						break;
					case COL_TXN_ID:
						sortColumnMap.put(tHeader.getId(), tHeader);
						break;
					// case COL_DATE:
					default:
						sortColumnMap.put(dateFormat.format(tHeader.getSubmitDate()) + "," + tHeader.getId(), tHeader);
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
						tHeader = (TransactionHeader) vecSortedHeaders.elementAt(vecSortedHeaders.size() - iCtr - 1);
					} else {
						tHeader = (TransactionHeader) vecSortedHeaders.elementAt(iCtr);
					}
					addTxnHeader(tHeader);
					if (selectedTranHeaderID != null && tHeader.getId().equals(selectedTranHeaderID))
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

	private class TotalAmountDueComparator implements Comparator {
		/**
		 * put your documentation comment here
		 * @param obj1
		 * @param obj2
		 * @return
		 */
		public int compare(Object obj1, Object obj2) {
			TransactionHeader header1 = (TransactionHeader) obj1;
			TransactionHeader header2 = (TransactionHeader) obj2;
			if (header1.getTotalAmountDue().doubleValue() < header2.getTotalAmountDue().doubleValue())
				return -1;
			if (header1.getTotalAmountDue().doubleValue() > header2.getTotalAmountDue().doubleValue())
				return 1;
			return header1.getId().compareTo(header2.getId());
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void clear() {
		bReverseSort = false;
		iCurrentSortColIdx = Integer.MAX_VALUE;
		super.clear();
	}

	protected class RowSelector {
		TransactionHeader txn;
		Boolean isRowSelected;

		/**
		 * put your documentation comment here
		 * 
		 * @param TransactionHeader
		 *            txn
		 */
		public RowSelector(TransactionHeader txn) {
			this.txn = txn;
			this.isRowSelected = new Boolean(false);
		}
	}
}
