/*
 * @copyright (c) 2000 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;
import com.chelseasystems.cs.swing.model.TxnHistModel_EUR;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet_EUR;
import com.chelseasystems.cr.pos.TransactionHeader;
import javax.swing.table.*;
import com.chelseasystems.cr.swing.CMSApplet;

/**
 * Panel has the capability of displaying a list of <code>TransactionHeaders</code> or retreiving them a batch at a time.
 * 
 * @author Dan Reading
 */
/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+
| 1    | 02/06/2006 |Sandhya    | N/A       | Base Version                                       |
+------+------------+-----------+-----------+----------------------------------------------------+
*/
public class TxnHistPanel_EUR extends TxnHistPanel implements PageNumberGetter {
	private static final long serialVersionUID = 1L;	

	/**
	 */
	public TxnHistPanel_EUR() {
		try {
			model = new TxnHistModel_EUR();
			tblList = new ArmJCMSTable(model, JCMSTable.SELECT_ROW);
			jbInit();
			tblList.addMouseListener(new MouseAdapter() {
				/**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
				public void mouseClicked(MouseEvent e) {
					toggleRowSelection();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Toggle row selection
	 */
	public void toggleRowSelection() {
		String source = (String) (CMSApplet.theAppMgr.getStateObject("FROM_VIEW_TXN"));
		Integer txnMode = (Integer) CMSApplet.theAppMgr.getStateObject("TXN_MODE");
		if (((CMSApplet.theAppMgr.getStateObject("OPERATOR") != null)) 
				&& source != null && source.equals("FROM_VIEW_TXN") 
				&& (CMSApplet.theAppMgr.getStateObject("TXN_POS") != null) 
				&& (txnMode == null || txnMode.intValue() == InitialSaleApplet_EUR.SALE_MODE)) {
			int iRow = tblList.getSelectedRow();
			model.setRowSelected(iRow);
			model.fireTableDataChanged();
			if (model.getRowSelected(iRow)) {
				selectRow(iRow, false);
			}
		}
	}

	/**
	 * Create HotKeys for columns
	 */
	/*private void setHotKeys() {
		tblList.getTableHeader().addMouseListener(new MouseAdapter() {*/
			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			/*public void mouseClicked(MouseEvent e) {
				JTableHeader h = (JTableHeader) e.getSource();
				TableColumnModel columnModel = h.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = columnModel.getColumn(viewColumn).getModelIndex();
				(new SortTxnAction(column)).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "SORT_TXNS"));
			}
		});*/
		/*InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
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
	}*/

	/**
	 * Select Row
	 * 
	 * @param row
	 *            ROWINDEX
	 * @param bEnforceSelection
	 *            ToggleCheckbox
	 */
	public void selectRow(int row, boolean bEnforceSelection) {
		if (bEnforceSelection) {
			model.setRowSelected(row, true);
			model.fireTableDataChanged();
		}
		ListSelectionModel model = tblList.getSelectionModel();
		model.setSelectionInterval(row, row);
	}

	/**
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		this.add(tblList.getTableHeader(), BorderLayout.NORTH);
		this.add(tblList, BorderLayout.CENTER);
		model.setRowsShown(13);
		TableColumnModel modelColumn = tblList.getColumnModel();
		CheckBoxRenderer boxRenderer = new CheckBoxRenderer();
		modelColumn.getColumn(0).setCellRenderer(boxRenderer);
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
		tblList.setRequestFocusEnabled(false);
		tblList.setCellSelectionEnabled(false);
		tblList.setColumnSelectionAllowed(false);
		tblList.setRowSelectionAllowed(true);
		setHotKeys();
	}

	/**
	 *
	 */
	public void resetColumnWidths() {
		model.setColumnWidth(tblList);
	}

	public TransactionHeader[] getSelectedTransactionHeaders() {
		int[] rows = tblList.getSelectedRows();
		TransactionHeader[] headers = new TransactionHeader[rows.length];
		for (int i = 0; i < rows.length; i++) {
			headers[i] = model.getTransactionHeader(i);
		}
		return headers;
	}

	public TransactionHeader[] getSelectedRows() {
		return model.getSelectedRows();
	}

	/**
	 * <p>
	 * Title: CheckBoxRenderer
	 * </p>
	 * <p>
	 * Description: Renderer for Checkbox cell
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
	private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
		private Color DefaultBackground;
		private Color DefaultForeground;

		/**
		 */
		public CheckBoxRenderer() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
			setForeground(new Color(0, 0, 175));
			setBackground(Color.white);
			DefaultBackground = getBackground();
			DefaultForeground = getForeground();
		}

		/**
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param column
		 * @return
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Boolean b = (Boolean) value;
			if (b != null)
				setSelected(b.booleanValue());
			setBackground(DefaultBackground);
			boolean rowSelected = TxnHistPanel_EUR.this.model.getRowSelected(row);
			String source = (String) (CMSApplet.theAppMgr.getStateObject("FROM_VIEW_TXN"));
			Integer txnMode = (Integer) CMSApplet.theAppMgr.getStateObject("TXN_MODE");
			if (((CMSApplet.theAppMgr.getStateObject("OPERATOR") != null)) && (source != null && source.equals("FROM_VIEW_TXN")) && (CMSApplet.theAppMgr.getStateObject("TXN_POS") != null) && (txnMode == null || txnMode.intValue() == InitialSaleApplet_EUR.SALE_MODE)) {
				this.setEnabled(true);
			} else {
				this.setEnabled(false);
			}
			if (rowSelected)
				setForeground(Color.red);
			else
				setForeground(DefaultForeground);
			return this;
		}
	}
} // end of TxnHistPanel_EUR
