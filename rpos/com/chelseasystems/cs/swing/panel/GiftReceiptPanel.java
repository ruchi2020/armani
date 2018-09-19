/*
 * @copyright (c) 1998-2002 Chelsea Market Systems LLC 
 * 
 * 
 */

package com.chelseasystems.cs.swing.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.GiftReceiptModel;
import com.chelseasystems.cs.swing.model.TxnDetailModel;

import java.util.Vector;

/**
 * Deo.. taken from rpos core 3.01 
 * Date: 06/16/2006
 */
public class GiftReceiptPanel extends JPanel implements ScrollProcessor {
	private IApplicationManager theAppMgr;
	private GiftReceiptModel giftModel;
	private PaymentTransactionAppModel txn;
	private JCMSTable table;

	/**
	 * Inner class to render check boxes.
	 */
	private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

		/**
		 * Used to render checkboxes
		 */
		public CheckBoxRenderer() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
			setBackground(Color.white);
			setForeground(Color.blue);
		}

		/**
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param column
		 * @return this
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			// getGiftReceiptModel().setSelected(row, new Boolean(value));
			// this.setSelected(isSelected);
			Boolean b = (Boolean) value;
			if (b != null)
				this.setSelected(b.booleanValue());
			return this;
		}
	}

	/**
	 * Inner class to render text fields
	 */
	private class TextRenderer extends DefaultTableCellRenderer {

		/**
		 */
		TextRenderer() {
			super();
			setFont(theAppMgr.getTheme().getTextFieldFont());
		}

		/**
		 * @param JTable
		 * @param SaleLineItem
		 * @param boolean
		 * @param boolean
		 * @param int
		 * @param int
		 * @return Component
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			GiftReceiptModel giftModel = (GiftReceiptModel) table.getModel();
			this.setText((String) giftModel.getValueAt(row, col));
			switch (col) {
				case 1:
					setHorizontalAlignment(JLabel.CENTER);
					break;
				case 2:
					setHorizontalAlignment(JLabel.LEFT);
					break;
				default:
					setHorizontalAlignment(JLabel.CENTER);
					break;
			}
			return this;
		}
	}

	/**
	 * Need to pass the appManager
	 */
	public GiftReceiptPanel(IApplicationManager theAppMgr, PaymentTransactionAppModel txn) {
		super();
		this.theAppMgr = theAppMgr;
		this.txn = txn;

		table = new JCMSTable(getGiftReceiptModel(), JCMSTable.SELECT_ROW);
		//   tblTxn = new JCMSTable(model, JCMSTable.VIEW_ROW);
		init();
	}

	/**
	 */
	private void init() {
		this.setLayout(new BorderLayout());
		getTable().setAppMgr(theAppMgr);
		this.add(getTable().getTableHeader(), BorderLayout.NORTH);
		this.add(getTable(), BorderLayout.CENTER);
		getTable().addMouseListener(new java.awt.event.MouseAdapter() {

			/**
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				clickEvent();
			}
		});
		getTable().addComponentListener(new java.awt.event.ComponentAdapter() {

			/**
			 * @param e
			 */
			public void componentResized(ComponentEvent e) {
				tableResized(e);
			}
		});
		TableColumnModel columnModel = getTable().getColumnModel();
		TextRenderer renderer = new TextRenderer();
		for (int i = 1; i < columnModel.getColumnCount(); i++) {
			columnModel.getColumn(i).setCellRenderer(renderer);
		}
		CheckBoxRenderer boxRenderer = new CheckBoxRenderer();
		columnModel.getColumn(0).setCellRenderer(boxRenderer);
	}

	/**
	 */
	public void clickEvent() {
		int row = getTable().getSelectedRow();
		boolean state = getGiftReceiptModel().isSelected(row);
		getGiftReceiptModel().setSelected(row, new Boolean(!state));
		table.repaint();
	}

	/* public void setAllItemSelection(Boolean b){
	 Vector vRows =  getGiftReceiptModel().getAllRows();
	 for (int i = 0; i < vRows.size(); i++){
	 //getGiftReceiptModel().setSelected( i, b );
	 getGiftReceiptModel().getGiftEntry(i).setSelected(b);
	 }
	 }*/
	private void tableResized(ComponentEvent e) {
		getGiftReceiptModel().setRowsShown(getTable().getHeight() / getTable().getRowHeight());
		TableColumn selectCol = getTable().getColumnModel().getColumn(getGiftReceiptModel().SELECT);
		selectCol.setPreferredWidth(75);
		TableColumn itemCol = getTable().getColumnModel().getColumn(getGiftReceiptModel().CODE);
		itemCol.setPreferredWidth((int) (150 * CMSApplet.r));
		TableColumn descCol = getTable().getColumnModel().getColumn(getGiftReceiptModel().DESC);
		descCol.setPreferredWidth(getTable().getWidth() - (selectCol.getPreferredWidth() + itemCol.getPreferredWidth()));
	}

	/**
	 * Creates a new table if one does not exist.
	 * @return JCMSTable table
	 */
	public JCMSTable getTable() {
		if (table == null) {
			table = new JCMSTable(getGiftReceiptModel(), JCMSTable.SELECT_ROW);
			//table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return table;
	}

	/**
	 * Creates the GiftReceiptModel
	 * @return   GiftReceiptModel
	 */
	public GiftReceiptModel getGiftReceiptModel() {
		if (giftModel == null) {
			giftModel = new GiftReceiptModel(txn);
		}
		return giftModel;
	}

	/**
	 * Next page
	 */
	public void nextPage() {
		getGiftReceiptModel().nextPage();
	}

	/**
	 * Prev page
	 */
	public void prevPage() {
		getGiftReceiptModel().prevPage();
	}

	/**
	 * Gets the PageNumber (For Up/Down Functionality)
	 * @return
	 */
	public int getCurrentPageNumber() {
		return (getGiftReceiptModel().getCurrentPageNumber());
	}

	/**
	 * Gets the pageCount( For Up/Down functionality)
	 * @return
	 */
	public int getPageCount() {
		return (getGiftReceiptModel().getPageCount());
	}

}
