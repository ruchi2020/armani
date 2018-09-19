package com.chelseasystems.cs.swing.model;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.v12basket.CMSV12Basket;

public class V12BasketListModel extends ScrollableTableModel {
	private static final long serialVersionUID = 1L;

	protected Vector v12BasketVec;
	
	private final String COLUMN_NAMES[] = { "Comment", "Timestamp",
			"Trans Type", "No. of items", "Price" };
	public final static int COMMNENT = 0;
	public final static int TIMESTAMP = 1;
	public final static int TRANS_TYPE = 2;
	public final static int NO_OF_ITEMS = 3;
	public final static int PRICE = 4;
	protected int lastSelectedV12BasketRow = -1;
	protected java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager
			.getResourceBundle();

	public V12BasketListModel() {
		v12BasketVec = new Vector();
		String sIdentiFiers[] = new String[COLUMN_NAMES.length];
		for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
			sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES[iCtr]);
		}
		this.setColumnIdentifiers(sIdentiFiers);
	}
	
	
	public void addV12Basket(CMSV12Basket cmsV12Basket) {
		if (cmsV12Basket == null)
			return;
		addRow(cmsV12Basket);
		v12BasketVec.add(cmsV12Basket);
		this.fireTableDataChanged();
	}

	public CMSV12Basket getV12BasketAt(int row) {
		return (CMSV12Basket) this.getRowInPage(row);
	}

	public Object getValueAt(int row, int column) {
		CMSV12Basket cmsV12Basket = (CMSV12Basket) this.getCurrentPage()
				.elementAt(row);

		switch (column) {
		case COMMNENT:
			return cmsV12Basket.getTrnComment();
		case TIMESTAMP:
			return cmsV12Basket.getTrnTimestamp();
		case TRANS_TYPE:
			return cmsV12Basket.getTransactionType();
		case NO_OF_ITEMS:
			return cmsV12Basket.getTotalItemNo();
		case PRICE:
			return cmsV12Basket.getTotalBasketAmount();
		}
		return "";
	}
		  /**
		   * @return
		   */
		  public int getColumnCount() {
		    return 5;
		  }

		  /**
		   * @param row
		   * @param column
		   * @return
		   */
		  public boolean isCellEditable(int row, int column) {
		    return false;
		  }

		  public void setColumnWidth(JTable table) {
			    TableColumn comment = table.getColumnModel().getColumn(0);
			    comment.setPreferredWidth(170);
			    TableColumn timestamp = table.getColumnModel().getColumn(1);
			    timestamp.setPreferredWidth(170);
			    TableColumn transType = table.getColumnModel().getColumn(2);
			    transType.setPreferredWidth(95);
			    TableColumn noOfItem = table.getColumnModel().getColumn(3);
			    noOfItem.setPreferredWidth(95);
			    TableColumn price = table.getColumnModel().getColumn(4);
			    price.setPreferredWidth(table.getWidth()
			        - (comment.getPreferredWidth() + timestamp.getPreferredWidth() + 
			        		transType.getPreferredWidth() + noOfItem.getPreferredWidth()));
			    DefaultTableCellRenderer ctrRenderer = new DefaultTableCellRenderer();
				ctrRenderer.setHorizontalAlignment(0);
				table.getColumnModel().getColumn(2).setCellRenderer(ctrRenderer);
				ctrRenderer.setHorizontalAlignment(0);
				table.getColumnModel().getColumn(3).setCellRenderer(ctrRenderer);
			  }
		  
		  public void clear() {
				super.clear();
				v12BasketVec = new Vector();
			}

		  public int getLastSelectedCustomerRow() {
				return lastSelectedV12BasketRow;
			}

			public void setLastSelectedCustomerRow(int lastSelectedCustomerRow) {
				this.lastSelectedV12BasketRow = lastSelectedCustomerRow;
			}
	
	}
