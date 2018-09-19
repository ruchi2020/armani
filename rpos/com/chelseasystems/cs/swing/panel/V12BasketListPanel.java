package com.chelseasystems.cs.swing.panel;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.swing.model.V12BasketListModel;
import com.chelseasystems.cs.v12basket.CMSV12Basket;

public class V12BasketListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected V12BasketListModel modelV12BasketList;
	protected JCMSTable tblV12Basket;

	public V12BasketListPanel() {
		try {
			modelV12BasketList = new V12BasketListModel();
			tblV12Basket = new JCMSTable(modelV12BasketList,
					JCMSTable.SELECT_ROW);
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		tblV12Basket
				.addComponentListener(new java.awt.event.ComponentAdapter() {

					/**
					 * put your documentation comment here
					 * 
					 * @param e
					 */
					public void componentResized(ComponentEvent e) {
						resetColumnWidths();
					}
				});
		this.add(tblV12Basket.getTableHeader(), BorderLayout.NORTH);
		this.add(tblV12Basket, BorderLayout.CENTER);
		tblV12Basket.setRequestFocusEnabled(false);
		tblV12Basket.setCellSelectionEnabled(false);
		tblV12Basket.setColumnSelectionAllowed(false);
		tblV12Basket.setRowSelectionAllowed(true);
	}

	/**
	 * @param e
	 */
	public void resetColumnWidths() {
		modelV12BasketList.setColumnWidth(tblV12Basket);
		modelV12BasketList.setRowsShown(tblV12Basket.getHeight()
				/ tblV12Basket.getRowHeight());
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

	/**
	 * @param theAppMgr
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		tblV12Basket.getTableHeader().setBackground(
				theAppMgr.getBackgroundColor());
	}

	public void addV12Basket(CMSV12Basket cmsv12Basket) {
		modelV12BasketList.addV12Basket(cmsv12Basket);
		selectLastRow();

	}

	/**
	   */
	public void nextPage() {
		modelV12BasketList.nextPage();
	}

	/**
	   */
	public void prevPage() {
		modelV12BasketList.prevPage();
	}

	/**
	 * @return
	 */
	public int getTotalPages() {
		return modelV12BasketList.getPageCount();
	}

	/**
	 * 
	 * @return
	 */
	public int getCurrentPageNumber() {
		return modelV12BasketList.getCurrentPageNumber();
	}
}
