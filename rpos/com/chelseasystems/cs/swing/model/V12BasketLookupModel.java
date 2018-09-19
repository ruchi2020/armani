package com.chelseasystems.cs.swing.model;

import java.util.Vector;

import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.v12basket.CMSV12Basket;

public class V12BasketLookupModel extends ScrollableTableModel {
	private static final long serialVersionUID = 1L;
	private final String COLUMN_NAMES[] = { "SKU", "QTY", "Unit Price",
			"Total Price" };
	public final static int SKU = 0;
	public final static int QTY = 1;
	public final static int UNIT_PRICE = 2;
	public final static int TOTAL_PRICE = 3;

	protected int lastSelectedV12BasketRow = -1;

	protected java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager
			.getResourceBundle();

	protected Vector v12BasketVec;
	public static final String ISO_COUNTRY_US = "EUR";

	CMSV12Basket v12Basket = null;

	public V12BasketLookupModel() {
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

	public void addV12Basket(CMSV12Basket cmsV12Basket, String itemId) {
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
		case SKU:
			return cmsV12Basket.getBarcode();
		case QTY:
			return cmsV12Basket.getItemQty();
		case UNIT_PRICE:
			return cmsV12Basket.getItemPrice();
		case TOTAL_PRICE:
			return cmsV12Basket.getItemTotalPrice();
		}
		return "";
	}

	public void setRowsShown(int RowsShown) {
		super.setRowsShown(RowsShown);
	}

	public int sortV12Basket(String selectedBasket) {
		return 0;
	}

	public int getLastSelectedCustomerRow() {
		return lastSelectedV12BasketRow;
	}

	public void setLastSelectedCustomerRow(int lastSelectedCustomerRow) {
		this.lastSelectedV12BasketRow = lastSelectedCustomerRow;
	}

	public void clear() {
		super.clear();
		v12BasketVec = new Vector();
	}

}
