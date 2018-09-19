/*
 * @copyright (c) 2000 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.model.TxnHistModel;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.TransactionHeader;
import com.chelseasystems.cs.pos.CMSTransactionHeader;
import javax.swing.table.*;
import com.chelseasystems.cr.swing.ScrollableTableModel;

/**
 * Panel has the capability of displaying a list of <code>TransactionHeaders</code>
 *    or retreiving them a batch at a time.
 * @author Dan Reading
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | N/A        |Retek      | N/A       | Base Version                                       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-21-2005 |Manpreet   | N/A       | POS_104665_TS_TransactionHistory_Rev0              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 07-07-2005 | Vikram    | 368       | Transaction history details to work with Keyboard  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class TxnHistPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private boolean eOF = false;
	TxnHistModel model = new TxnHistModel();
	JCMSTable tblList;

	/**
	 */
	public TxnHistPanel() {
		try {
			tblList = new CustomJCMSTable(model, JCMSTable.SELECT_ROW);
			jbInit();
			this.requestFocus();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		tblList.addComponentListener(new java.awt.event.ComponentAdapter() {

			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void componentResized(ComponentEvent e) {
				resetColumnWidths();
			}
		});
		this.add(tblList.getTableHeader(), BorderLayout.NORTH);
		this.add(tblList, BorderLayout.CENTER);
		setHotKeys();
	}

	/**
	 * Will the next page step off of what we have cached? If so, load some more before performing next page.
	 * 
	 * @note currentPageNumber is zero based
	 * @exception Exception
	 */
	public void nextCachedPage() throws Exception {
		if (model.getCurrentPageNumber() + 2 == model.getPageCount() && !eOF) {
			loadMoreCache();
		}
		nextPage();
	}

	/**
	 * @exception Exception
	 */
	public void loadMoreCache() throws Exception {
		TransactionHeader[] txnHeaderList = null;
		if (txnHeaderList == null)
			eOF = true;
		else
			for (int x = 0; x < txnHeaderList.length; x++)
				addTxnHeader(txnHeaderList[x]);
	}

	/**
	 * @return
	 */
	public CMSTransactionHeader[] getCMSTxnHeaders() {
		return model.getCMSTxnHeaders();
	}

	/**
	 * @param theAppMgr
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		tblList.setAppMgr(theAppMgr);
	}

	/**
	 * @param hdr
	 */
	public void setTxnHeaders(TransactionHeader[] hdr) {
		clear();
		for (int x = 0; x < hdr.length; x++) {
			addTxnHeader(hdr[x]);
		}
	}

	/**
	 * @return
	 */
	public int getSelectedRow() {
		if (tblList.getSelectedRow() < 0)
			return tblList.getSelectedRow();
		int page = model.getCurrentPageNumber();
		int row = (page * model.getRowsShown()) + tblList.getSelectedRow();
		return row;
	}

	/**
	 */
	public TransactionHeader getSelectedTransactionHeader() {
		int row = tblList.getSelectedRow();
		return (row > -1) ? model.getTransactionHeader(row) : null;
	}

	public TransactionHeader[] getSelectedRows() {
		if (tblList.getSelectedRow() < 0)
			return null;
		TransactionHeader[] txnHeaders = null;
		int page = model.getCurrentPageNumber();
		int[] selectedRows = tblList.getSelectedRows();
		int row = 0;
		if (selectedRows.length > 0) {
			txnHeaders = new TransactionHeader[selectedRows.length];
			for (int i = 0; i < selectedRows.length; i++) {
				row = (page * model.getRowsShown()) + i;
				txnHeaders[i] = model.getTransactionHeader(row);
			}
		}
		return txnHeaders;
	}

	/**
	 * @param hdr
	 */
	public void addTxnHeader(TransactionHeader hdr) {
		model.addTxnHeader(hdr);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public TxnHistModel getAddressModel() {
		return model;
	}

	/**
	 * @param l
	 *            mouse listener added to table
	 */
	public void addMouseListener(MouseListener l) {
		tblList.addMouseListener(l);
	}

	/**
	 */
	public void clear() {
		model.clear();
		model.fireTableDataChanged();
	}

	/**
	 */
	public void nextPage() {
		model.nextPage();
	}

	/**
	 */
	public void prevPage() {
		model.prevPage();
	}

	/**
	 * @return
	 */
	public int getCurrentPageNumber() {
		return model.getCurrentPageNumber();
	}

	/**
	 * @return
	 */
	public int getTotalPages() {
		return model.getPageCount();
	}

	/**
	 *
	 */
	public void resetColumnWidths() {
		model.setColumnWidth(tblList);
	}

	/**
	 */
	public void doClick() {
		int index = tblList.getSelectedRow();
		if (index >= 0) {
			Rectangle r = tblList.getCellRect(index, 0, true);
			int x = r.x + r.width / 2;
			int y = r.y + r.height / 2;
			MouseEvent me = new MouseEvent(tblList, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x, y, 1, false);
			tblList.dispatchEvent(me);
		}
	}

	/**
	 * Create HotKeys for columns
	 */
	protected void setHotKeys() {
		tblList.getTableHeader().addMouseListener(new MouseAdapter() {

			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				JTableHeader h = (JTableHeader) e.getSource();
				TableColumnModel columnModel = h.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = columnModel.getColumn(viewColumn).getModelIndex();
				(new SortTxnAction(column)).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "SORT_TXNS"));
			}
		});
		InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		KeyStroke key = KeyStroke.getKeyStroke(Character.toUpperCase(model.getColumnHotKey(model.COL_DATE).charAt(0)), Event.ALT_MASK);
		inputMap.put(key, "SORT_COL_DATE");
		getActionMap().put("SORT_COL_DATE", new SortTxnAction(model.COL_DATE));
		key = KeyStroke.getKeyStroke(Character.toUpperCase(model.getColumnHotKey(model.COL_STORE_ID).charAt(0)), Event.ALT_MASK);
		inputMap.put(key, "SORT_COL_STORE_ID");
		getActionMap().put("SORT_COL_STORE_ID", new SortTxnAction(model.COL_STORE_ID));
		key = KeyStroke.getKeyStroke(Character.toUpperCase(model.getColumnHotKey(model.COL_TXN_ID).charAt(0)), Event.ALT_MASK);
		inputMap.put(key, "SORT_COL_TXN_ID");
		getActionMap().put("SORT_COL_TXN_ID", new SortTxnAction(model.COL_TXN_ID));
		key = KeyStroke.getKeyStroke(Character.toUpperCase(model.getColumnHotKey(model.COL_TXN_TYPE).charAt(0)), Event.ALT_MASK);
		inputMap.put(key, "SORT_COL_TXN_TYPE");
		getActionMap().put("SORT_COL_TXN_TYPE", new SortTxnAction(model.COL_TXN_TYPE));
		key = KeyStroke.getKeyStroke(Character.toUpperCase(model.getColumnHotKey(model.COL_ASSOCIATE).charAt(0)), Event.ALT_MASK);
		inputMap.put(key, "SORT_COL_ASSOCIATE");
		getActionMap().put("SORT_COL_ASSOCIATE", new SortTxnAction(model.COL_ASSOCIATE));
		key = KeyStroke.getKeyStroke(Character.toUpperCase(model.getColumnHotKey(model.COL_TXN_TOTAL).charAt(0)), Event.ALT_MASK);
		inputMap.put(key, "SORT_COL_TXN_TOTAL");
		getActionMap().put("SORT_COL_TXN_TOTAL", new SortTxnAction(model.COL_TXN_TOTAL));
	}

	/**
	 * Select Row
	 * 
	 * @param row
	 *            int
	 */
	public void selectRow(int row) {
		if (row < 0)
			return;
		model.pageContainingRow(row);
		selectRowIfInCurrentPage(row);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param absoluteRow
	 */
	public void selectRowIfInCurrentPage(int absoluteRow) {
		if (absoluteRow < 0)
			return;
		int rowInPage = model.getCurrentPage().indexOf(model.getAllRows().elementAt(absoluteRow));
		model.setLastSelectedTxnRow(absoluteRow);
		if (rowInPage < 0)
			return;
		ListSelectionModel model = tblList.getSelectionModel();
		model.setSelectionInterval(rowInPage, rowInPage);
	}

	/**
	 * Pageup
	 */
	public void pageUp() {
		model.prevPage();
	}

	/**
	 * Pagedown
	 */
	public void pageDown() {
		model.nextPage();
	}

	/**
	 * Select Last Row
	 */
	public void selectLastRow() {
		int rowCount = tblList.getRowCount();
		if (rowCount < 1) {
			model.prevPage();
		}
		if (rowCount > 0) {
			tblList.setRowSelectionInterval(rowCount - 1, rowCount - 1);
		}
	}

	/**
	 * Select First Row
	 */
	public void selectFirstRow() {
		selectRow(0);
	}

	/**
	 * <p>
	 * Title: SortTxnAction
	 * </p>
	 * <p>
	 * Description: Traps keyboard, mouse event to perform sort on columns
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2005
	 * </p>
	 * <p>
	 * Company: SkillNet Inc
	 * </p>
	 * 
	 * @author Manpreet S Bawa
	 * @version 1.0
	 */
	protected class SortTxnAction extends AbstractAction {
		private int iColumnIndex = -1;

		/**
		 * put your documentation comment here
		 * 
		 * @param int
		 *            iColumnIndex
		 */
		public SortTxnAction(int iColumnIndex) {
			this.iColumnIndex = iColumnIndex;
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			if (getSelectedTransactionHeader() == null) {
				model.sortByColumnType(iColumnIndex, null);
				return;
			}
			int selectedItemRowNew = model.sortByColumnType(iColumnIndex, getSelectedTransactionHeader().getId());
			if (selectedItemRowNew > -1)
				selectRow(selectedItemRowNew);
		}
	}

	/**
	 * Extended this class to get the Enter Key working
	 */
	private class CustomJCMSTable extends JCMSTable {

		/**
		 * @param ScrollableTableModel
		 *            model
		 * @param int
		 *            type
		 */
		public CustomJCMSTable(ScrollableTableModel model, int type) {
			super(model, type);
		}

		/**
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			String command = e.getActionCommand();
			if (command.equals("SELECT_CMD")) {
				processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 2, false));
			}
		}
	}
} // end of TxnHistPanel

