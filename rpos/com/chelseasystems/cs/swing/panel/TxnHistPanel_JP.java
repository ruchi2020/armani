/*
 * @copyright (c) 2000 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Enumeration;
import javax.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.model.TxnHistModel;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.TransactionHeader;
import com.chelseasystems.cs.pos.CMSTransactionHeader;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 * <p>Title:TxnHistPanel_JP.java </p>
 *
 * <p>Description: Panel has the capability of displaying a list of TransactionHeaders </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc. </p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-06-2006 | Sandhya   | PCR1326	 | Panel displays transaction headers                 | 
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class TxnHistPanel_JP extends TxnHistPanel {
	private boolean eOF = false;
	private static final String CONFIGURATION_FILE = "pos.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	TxnHistModel model;
	JCMSTable tblList;

	/**
	 */
	public TxnHistPanel_JP() {
		try {
			// PCR1326 TransactionHistory fix for Armani Japan
			String txnHistModelClassName = configMgr.getString("TRANSACTION.TXN_HIST_MODEL");
			if (txnHistModelClassName == null) {
				model = new TxnHistModel();
			} else {
				model = (TxnHistModel) Class.forName(txnHistModelClassName).newInstance();
			}
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
   * @param custId
   * @param theAppMgr
   * @return
   * @exception Exception
   public boolean loadForCust (String custId, IApplicationManager theAppMgr) throws Exception {
   this.theAppMgr = theAppMgr;
   this.custId = custId;
   clear();
   TransactionHeader[] txnHeaderList = TransactionPOSHelper.findByCustomerIdHeader(theAppMgr,
   /custId, 1, BLOCK_SIZE);
   if (txnHeaderList == null)
   return  false;         // cust has no history
   eOF = false;
   blocksCached = 1;
   for (int x = 0; x < txnHeaderList.length; x++) {
   addTxnHeader(txnHeaderList[x]);
   }
   return  true;
   }*/
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
		TransactionHeader[] txnHeaderList = null; // TransactionPOSHelper.findByCustomerIdHeader(theAppMgr,
		// custId, ++blocksCached, BLOCK_SIZE);
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
		Vector allRows = model.getAllRows();
		CMSTransactionHeader[] allTransactions = new CMSTransactionHeader[allRows.size()];
		Enumeration e = allRows.elements();
		int i = 0;
		while(e.hasMoreElements())
			allTransactions[i++] = (CMSTransactionHeader) e.nextElement();
		return allTransactions;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public TransactionHeader[] getTxnHeaders() {
		Vector allRows = model.getAllRows();
		TransactionHeader[] allTransactions = new TransactionHeader[allRows.size()];
		Enumeration e = allRows.elements();
		int i = 0;
		while(e.hasMoreElements())
			allTransactions[i++] = (TransactionHeader) e.nextElement();
		return allTransactions;
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

	/**
	 * @param hdr
	 */
	public void addTxnHeader(TransactionHeader hdr) {
		model.addTxnHeader(hdr);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public TxnHistModel getAddressModel() {
		return model;
	}

	/**
	 * @param l mouse listener added to table
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
	 * Select Row
	 * @param row
	 *            int
	 */
	public void selectRow(int row) {
		// ListSelectionModel model = tblList.getSelectionModel();
		// model.setSelectionInterval(row, row);
		if (row < 0)
			return;
		model.pageContainingRow(row);
		selectRowIfInCurrentPage(row);
	}

	/**
	 * put your documentation comment here
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
		// selectFirstRow();
	}

	/**
	 * Pagedown
	 */
	public void pageDown() {
		model.nextPage();
		// selectLastRow();
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
   *
   * <p>Title: SortTxnAction</p>
   * <p>Description: Traps keyboard, mouse event to perform sort on columns</p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
	private class SortTxnAction extends AbstractAction {
		private int iColumnIndex = -1;

		/**
		 * put your documentation comment here
		 * @param int
		 *            iColumnIndex
		 */
		public SortTxnAction(int iColumnIndex) {
			this.iColumnIndex = iColumnIndex;
		}

		/**
		 * put your documentation comment here
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

