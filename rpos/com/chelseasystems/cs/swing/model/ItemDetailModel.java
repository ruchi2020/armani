/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.util.ResourceManager;
import java.util.ResourceBundle;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemDetail;
import com.chelseasystems.cs.store.CMSStore;
import java.text.NumberFormat;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ItemDetailModel extends ScrollableTableModel {
	private String COLUMN_NAMES[] = { "Field", "Value" };
	private String ROW_LABELS[] = { "Style", "Barcode", "ExtendedBarcode", "Model", "Fabric", "Color", "Season Code",
			"Season Description", "Year", "Brand", "Label", "Subline", "Gender", "Category", "Drop", "Variant",
			"Size Index", "Size Index (Kids)", "Product", "Supplier", "Department", "Class", "Subclass",
			"Promotion Description", "Taxable" };
	private final int COL_FIELD = 0;
	private final int COL_VALUE = 1;
	private final int ROW_STYLE = 0;
	private final int ROW_BARCODE = 1;
	private final int ROW_EXT_BARCODE = 2;
	private final int ROW_MODEL = 3;
	private final int ROW_FABRIC = 4;
	private final int ROW_COLOR = 5;
	private final int ROW_SEASON_CODE = 6;
	private final int ROW_SEASON_DESC = 7;
	private final int ROW_YEAR = 8;
	private final int ROW_BRAND = 9;
	private final int ROW_LABEL = 10;
	private final int ROW_SUBLINE = 11;
	private final int ROW_GENDER = 12;
	private final int ROW_CATEGORY = 13;
	private final int ROW_DROP = 14;
	private final int ROW_VARIANT = 15;
	private final int ROW_SIZE_INDEX = 16;
	private final int ROW_SIZE_INDEX_KIDS = 17;
	private final int ROW_PRODUCT = 18;
	private final int ROW_SUPPLER = 19;
	private final int ROW_DEPARTMENT = 20;
	private final int ROW_CLASS = 21;
	private final int ROW_SUBCLASS = 22;
	private final int ROW_PROMOTION_DESC = 23;
	private final int ROW_TAXABLE = 24;
	private String arrayData[][];
	private ResourceBundle resource;
	private CMSItem cmsItem;
	private CMSItemDetail cmsItemDetail;
	private boolean vatEnabled = false;
	private POSLineItem posLineItem = null;
	private final NumberFormat percentFormat = NumberFormat.getPercentInstance();

	/**
	 * put your documentation comment here
	 */
	public ItemDetailModel() {
		int iCtr;
		resource = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		for (iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
			COLUMN_NAMES[iCtr] = resource.getString(COLUMN_NAMES[iCtr]);
		}
		this.setColumnIdentifiers(COLUMN_NAMES);
		for (iCtr = 0; iCtr < ROW_LABELS.length; iCtr++) {
			ROW_LABELS[iCtr] = resource.getString(ROW_LABELS[iCtr]);
		}
		CMSStore store = (CMSStore) CMSApplet.theAppMgr.getGlobalObject("STORE");
		vatEnabled = store.isVATEnabled();
		percentFormat.setMaximumFractionDigits(2);
	}

	/**
	 * put your documentation comment here
	 * @param posLineItem
	 */
	public void setPOSLineItem(POSLineItem posLineItem) {
		if (this.getTotalRowCount() > 1)
			clear();
		this.posLineItem = posLineItem;
		cmsItem = (CMSItem) posLineItem.getItem();
		cmsItemDetail = (CMSItemDetail) cmsItem.getItemDetail();
		arrayData = new String[ROW_LABELS.length][COLUMN_NAMES.length];
		buildData();
		fireTableDataChanged();
	}

	/**
	 * put your documentation comment here
	 */
	private void buildData() {
		int iRowCtr = 0;
		int iColCtr = 0;
		for (iRowCtr = 0; iRowCtr < arrayData.length; iRowCtr++) {
			for (iColCtr = 0; iColCtr < COLUMN_NAMES.length; iColCtr++) {
				if (iColCtr == COL_FIELD) {

					if (vatEnabled && iRowCtr == ROW_TAXABLE)
						arrayData[iRowCtr][iColCtr] = resource.getString("Vat Rate");
					else
						arrayData[iRowCtr][iColCtr] = ROW_LABELS[iRowCtr];
					continue;
				}
				switch (iRowCtr) {
					case ROW_STYLE:
						arrayData[iRowCtr][iColCtr] = cmsItem.getStyleNum();
						break;
					case ROW_BARCODE:
						arrayData[iRowCtr][iColCtr] = cmsItem.getBarCode();
						break;
					case ROW_EXT_BARCODE:
						arrayData[iRowCtr][iColCtr] = 
							(posLineItem.getExtendedBarCode() == null)? "" : posLineItem.getExtendedBarCode();
						break;
					case ROW_MODEL:
						arrayData[iRowCtr][iColCtr] = cmsItem.getModel();
						break;
					case ROW_FABRIC:
						arrayData[iRowCtr][iColCtr] = cmsItem.getFabric();
						break;
					case ROW_COLOR:
						String colorString = "";
						if (cmsItem.getColorId() != null && cmsItem.getColorId().trim().length() > 0)
							colorString = cmsItem.getColorId();
            if (cmsItemDetail.getColorDesc() != null
                && cmsItemDetail.getColorDesc().trim().length() > 0)
							colorString = colorString + "-" + cmsItemDetail.getColorDesc();
						arrayData[iRowCtr][iColCtr] = colorString;
						break;
					case ROW_SEASON_CODE:
						arrayData[iRowCtr][iColCtr] = cmsItem.getSeason();
						break;
					case ROW_SEASON_DESC:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getSeasonDesc();
						break;
					case ROW_YEAR:
						arrayData[iRowCtr][iColCtr] = cmsItem.getForTheYear();
						break;
					case ROW_BRAND:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getBrandDesc();
						break;
					case ROW_LABEL:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getLabelDesc();
						break;
					case ROW_SUBLINE:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getSubLineDesc();
						break;
					case ROW_GENDER:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getGenderDesc();
						break;
					case ROW_CATEGORY:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getCategoryDesc();
						break;
					case ROW_DROP:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getDropDesc();
						break;
					case ROW_VARIANT:
						arrayData[iRowCtr][iColCtr] = cmsItem.getVariant();
						break;
					case ROW_SIZE_INDEX:
						String sizeIndex = cmsItem.getItemDetail().getSizeIndx();
						if ((sizeIndex == null || sizeIndex.equals("")) && cmsItem.getSizeId() != null)
							sizeIndex = cmsItem.getSizeId().trim();
						else if (sizeIndex == null)
							sizeIndex = "";
						if (cmsItem.getItemDetail().getExtSizeIndx() != null
								&& !cmsItem.getItemDetail().getExtSizeIndx().equals(""))
							sizeIndex = sizeIndex.trim() + ":" + cmsItem.getItemDetail().getExtSizeIndx().trim();
						if (cmsItem.getItemDetail().getSizeDesc() != null
								&& !cmsItem.getItemDetail().getSizeDesc().trim().equals(""))
              sizeIndex = sizeIndex.trim() + " (" + cmsItem.getItemDetail().getSizeDesc().trim()
                  + ")";
						arrayData[iRowCtr][iColCtr] = sizeIndex;
						break;
					case ROW_SIZE_INDEX_KIDS:
						//__Tim: Null pointer check added.
						String kidsSizeIndex = (cmsItem.getKidsSize() == null) ? "" :  cmsItem.getKidsSize();
						if (cmsItem.getItemDetail().getKidsSizeDesc() != null
								&& !cmsItem.getItemDetail().getKidsSizeDesc().trim().equals("")){
								sizeIndex = kidsSizeIndex.trim() + " ("
									+ cmsItem.getItemDetail().getKidsSizeDesc().trim() + ")";
						}
						arrayData[iRowCtr][iColCtr] = kidsSizeIndex;
						break;
					case ROW_PRODUCT:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getProductDesc();
						break;
					case ROW_SUPPLER:
						arrayData[iRowCtr][iColCtr] = cmsItemDetail.getSupplierName();
						break;
					case ROW_DEPARTMENT:
						String deptString = "";
						if (cmsItem.getDepartment() != null && cmsItem.getDepartment().trim().length() > 0)
							deptString = cmsItem.getDepartment();
            if (cmsItemDetail.getPosDeptDesc() != null
                && cmsItemDetail.getPosDeptDesc().trim().length() > 0)
							deptString = deptString + "-" + cmsItemDetail.getPosDeptDesc();
						arrayData[iRowCtr][iColCtr] = deptString;
						break;
					case ROW_CLASS:
						String classString = "";
						if (cmsItem.getClassId() != null && cmsItem.getClassId().trim().length() > 0)
							classString = cmsItem.getClassId();
            if (cmsItemDetail.getClassDesc() != null
                && cmsItemDetail.getClassDesc().trim().length() > 0)
							classString = classString + "-" + cmsItemDetail.getClassDesc().trim();
						arrayData[iRowCtr][iColCtr] = classString;
						break;
					case ROW_SUBCLASS:
						String subclassString = "";
						if (cmsItem.getSubClassId() != null && cmsItem.getSubClassId().trim().length() > 0)
							subclassString = cmsItem.getSubClassId();
            if (cmsItemDetail.getSubClassDesc() != null
                && cmsItemDetail.getSubClassDesc().trim().length() > 0)
							subclassString = subclassString + "-" + cmsItemDetail.getSubClassDesc().trim();
						arrayData[iRowCtr][iColCtr] = subclassString;
						break;
					case ROW_PROMOTION_DESC:
						arrayData[iRowCtr][iColCtr] = cmsItem.getPromotion();
						break;
					case ROW_TAXABLE:
						if (vatEnabled)
							arrayData[iRowCtr][iColCtr] = percentFormat.format(cmsItem.getVatRate());
						else
							arrayData[iRowCtr][iColCtr] = cmsItem.isTaxable() ? "Yes" : "No";
						break;
				}
			}
		}
		removeNullRows();
	}

	/**
	 * put your documentation comment here
	 */
	private void removeNullRows() {
		int iLength = 0;
		int iCtr = 0;
		String sNewArray[][] = new String[arrayData.length][COLUMN_NAMES.length];
		for (iCtr = 0; iCtr < arrayData.length; iCtr++) {
			if (arrayData[iCtr][COL_VALUE] != null && arrayData[iCtr][COL_VALUE].trim().length() > 0) {
				sNewArray[iLength][COL_FIELD] = arrayData[iCtr][COL_FIELD];
				sNewArray[iLength][COL_VALUE] = arrayData[iCtr][COL_VALUE];
				iLength++;
			}
		}
		arrayData = new String[iLength][COLUMN_NAMES.length];
		for (iCtr = 0; iCtr < arrayData.length; iCtr++) {
			arrayData[iCtr][COL_FIELD] = sNewArray[iCtr][COL_FIELD];
			arrayData[iCtr][COL_VALUE] = sNewArray[iCtr][COL_VALUE];
			super.addRow(arrayData[iCtr]);
		}
	}

	/**
	 * put your documentation comment here
	 * @param iRow
	 * @param iColumn
	 * @return
	 */
	public Object getValueAt(int iRow, int iColumn) {
		if (arrayData == null || arrayData.length < 1 || iRow > arrayData.length)
			return null;
		return arrayData[iRow][iColumn];
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	/**
	 * put your documentation comment here
	 * @param iColumn
	 * @return
	 */
	public String getColumnName(int iColumn) {
		return COLUMN_NAMES[iColumn];
	}
}
