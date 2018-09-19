package com.chelseasystems.cs.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.V12BasketLookupModel;
import com.chelseasystems.cs.v12basket.CMSV12Basket;

public class V12BasketLookupPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected V12BasketLookupModel modelV12BasketList;
	protected JCMSTable tblV12Basket;
	protected TextRenderer renderer;

	public V12BasketLookupPanel() {
		try {
			modelV12BasketList = new V12BasketLookupModel();
			tblV12Basket = new JCMSTable(modelV12BasketList,
					JCMSTable.SELECT_ROW);
			renderer = new TextRenderer();
			this.setLayout(new BorderLayout());
			this.add(tblV12Basket.getTableHeader(), BorderLayout.NORTH);
			this.add(tblV12Basket, BorderLayout.CENTER);
			modelV12BasketList.setRowsShown(15); // arbitrarily set until resize
													// occurs
			TableColumnModel modelColumn = tblV12Basket.getColumnModel();
			for (int i = 0; i < modelV12BasketList.getColumnCount(); i++) {
				modelColumn.getColumn(i).setCellRenderer(renderer);
			}
			this.addComponentListener(new java.awt.event.ComponentAdapter() {

				/**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
				public void componentResized(ComponentEvent e) {
					resetColumnWidths();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getNoOfRows() {
		return modelV12BasketList.getRowCount();
	}

	public int getSelectedRowIndex() {
		return tblV12Basket.getSelectedRow();
	}

	public CMSV12Basket getSelectedV12Basket(int row) {
		return (row > -1) ? modelV12BasketList.getV12BasketAt(row) : null;
	}

	public CMSV12Basket getSelectedV12Basket() {
		int row = tblV12Basket.getSelectedRow();
		return (row > -1) ? modelV12BasketList.getV12BasketAt(row) : null;
	}

	public JCMSTable getTable() {
		return (this.tblV12Basket);
	}

	public void selectRow(int row) {
		if (row < 0)
			return;
		modelV12BasketList.pageContainingRow(row);
		selectRowIfInCurrentPage(row);
	}

	public void selectRowIfInCurrentPage(int absoluteRow) {
		if (absoluteRow < 0)
			return;
		int rowInPage = modelV12BasketList.getCurrentPage().indexOf(
				modelV12BasketList.getAllRows().elementAt(absoluteRow));
		modelV12BasketList.setLastSelectedCustomerRow(absoluteRow);
		if (rowInPage < 0)
			return;
		ListSelectionModel model = tblV12Basket.getSelectionModel();
		model.setSelectionInterval(rowInPage, rowInPage);
	}

	public void addBasket(CMSV12Basket cmsV12Basket) {
		modelV12BasketList.addV12Basket(cmsV12Basket);
		selectLastRow();
	}

	public CMSV12Basket getV12BasketAt(int row) {
		return (modelV12BasketList.getV12BasketAt(row));
	}

	public void pageUp() {
		modelV12BasketList.prevPage();
		selectFirstRow();
	}

	public void clear() {
		modelV12BasketList.clear();
	}

	public void addMouseListener(MouseListener l) {
		tblV12Basket.addMouseListener(l);
	}

	public void update() {
		modelV12BasketList.fireTableDataChanged();
	}

	/**
	 * put your documentation comment here
	 */
	public void pageDown() {
		modelV12BasketList.nextPage();
		selectLastRow();
	}

	public void selectFirstRow() {
		selectRow(0);
	}

	public void selectLastRow() {
		int rowCount = tblV12Basket.getRowCount();
		if (rowCount < 1) {
			modelV12BasketList.prevPage();
		}
		if (rowCount > 0) {
			tblV12Basket.setRowSelectionInterval(rowCount - 1, rowCount - 1);
		}
	}

	public void resetColumnWidths() {
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		int iWidth = 0;
		TableColumn CommentCol = tblV12Basket.getColumnModel().getColumn(
				V12BasketLookupModel.SKU);
		CommentCol.setPreferredWidth((int) (150 * r));
		TableColumn DateCol = tblV12Basket.getColumnModel().getColumn(
				V12BasketLookupModel.QTY);
		DateCol.setPreferredWidth((int) (100 * r));
		TableColumn AddressCol = tblV12Basket.getColumnModel().getColumn(
				V12BasketLookupModel.UNIT_PRICE);
		AddressCol.setPreferredWidth((int) (100 * r));
		TableColumn PriceCol = tblV12Basket.getColumnModel().getColumn(
				V12BasketLookupModel.TOTAL_PRICE);
		PriceCol.setPreferredWidth((int) (100 * r));
		iWidth = tblV12Basket.getWidth()
				- (CommentCol.getPreferredWidth() + DateCol.getPreferredWidth()
						+ AddressCol.getPreferredWidth() + PriceCol
							.getPreferredWidth());
		AddressCol.setPreferredWidth(iWidth);
		modelV12BasketList.setRowsShown(tblV12Basket.getHeight()
				/ tblV12Basket.getRowHeight());
	}

	private class TextRenderer extends JLabel implements TableCellRenderer {
		private static final long serialVersionUID = 1L;
		private ConfigMgr custConfigMgr;
		private int custAAALength = 0;
		private Color DefaultBackground;
		private Color DefaultForeground;

		/**
		 */
		public TextRenderer() {
			super();
			custConfigMgr = new ConfigMgr("v12basket.cfg");
			String strCustAAALength = custConfigMgr
					.getString("V12BASKET_AAA_LENGTH");
			if (strCustAAALength != null) {
				custAAALength = new Integer(strCustAAALength).intValue();
			}
			setFont(new Font("Helvetica", 1, 12));
			setForeground(new Color(0, 0, 175));
			setBackground(Color.white);
			DefaultBackground = getBackground();
			DefaultForeground = getForeground();
			setOpaque(true);
		}

		/**
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {
			CMSV12Basket v12Basket = getV12BasketAt(row);
			boolean custHasChar = false;
			String v12BasketComment = v12Basket.getTrnComment();
			int num = v12BasketComment.trim().length();
			if (num == custAAALength) {
				try {
				} catch (Exception ex) {
					custHasChar = true;
				}
			}

			if (value != null) {
				setText(value.toString());
			} else
				setText("");
			setHorizontalAlignment(JLabel.CENTER);
			if (isSelected) {
				setForeground(Color.white);
				setBackground(new Color(0, 0, 128));
			} else if (custHasChar) {
				setForeground(Color.red);
				setBackground(DefaultBackground);
			} else {
				setBackground(DefaultBackground);
				setForeground(DefaultForeground);
			}
			return (this);
		}

	}

	public void setAppMgr(IApplicationManager theAppMgr) {

		if (theAppMgr != null) {
			tblV12Basket.setAppMgr(theAppMgr);
			renderer.setFont(theAppMgr.getTheme().getTextFieldFont());
		}

	}

	public void addV12Basket(CMSV12Basket cmsv12Basket) {
		modelV12BasketList.addV12Basket(cmsv12Basket);
		selectLastRow();

	}
}
