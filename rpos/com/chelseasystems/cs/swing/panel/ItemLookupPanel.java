package com.chelseasystems.cs.swing.panel;

import java.awt.*;

import javax.swing.*;

import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.item.ItemSearchString;
import com.chelseasystems.cs.store.CMSStore;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

public class ItemLookupPanel extends JPanel{
	
	public static final String ALPHA_NUMERIC_SPEC =
	      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\"\\? ";
	private JCMSLabel lblSKU;
	private JCMSTextField txtSKU;
	private JCMSLabel lblOR;
	private JCMSLabel lblStyle;
	private JCMSTextField txtStyle;
	private JCMSLabel lblModel;
	private JCMSTextField txtModel;
	private JCMSLabel lblFabric;
	private JCMSTextField txtFabric;
	private JCMSLabel lblColor;
	private JCMSTextField txtColor;
	private JCMSLabel lblSupplier;
	private JCMSComboBox cbxSupplier;
	private JCMSLabel lblYear;
	private JCMSTextField txtYear;
	private JCMSLabel lblSeason;
	private JCMSComboBox cbxSeason;
	private Map supplierSeasonYearMap;
	
	private IApplicationManager theAppMgr;
	private ResourceBundle res = CMSApplet.res;
//	private double r = CMSApplet.r;
	public ItemLookupPanel(IApplicationManager theAppMgr) {
		try {
			this.theAppMgr = theAppMgr;
			jbInit();
			initSupplierSeasonYearMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit()throws Exception {
		lblSKU = new JCMSLabel();
		txtSKU = new JCMSTextField();
		lblOR = new JCMSLabel();
		lblStyle = new JCMSLabel();
		txtStyle = new JCMSTextField();
		lblModel = new JCMSLabel();
		txtModel = new JCMSTextField();
		lblFabric = new JCMSLabel();
		txtFabric = new JCMSTextField();
		lblColor = new JCMSLabel();
		txtColor = new JCMSTextField();
		lblSupplier = new JCMSLabel();
		cbxSupplier = new CustomComboBox();
		lblYear = new JCMSLabel();
		txtYear = new JCMSTextField();
		lblSeason = new JCMSLabel();
		cbxSeason = new CustomComboBox();
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		this.setLayout(gridBagLayout1);
		lblSKU.setLabelFor(txtSKU);
		lblSKU.setText(res.getString("SKU/Ref. No."));
		txtSKU.setText("");
		lblOR.setLabelFor(null);
		lblOR.setText(res.getString("OR"));
		lblStyle.setLabelFor(txtStyle);
		lblStyle.setText(res.getString("Style"));
		txtStyle.setText("");
		lblModel.setLabelFor(txtModel);
		lblModel.setText(res.getString("Model"));
		txtModel.setText("");
		lblFabric.setLabelFor(txtFabric);
		lblFabric.setText(res.getString("Fabric"));
		txtFabric.setText("");
		lblColor.setLabelFor(txtColor);
		lblColor.setText(res.getString("Color"));
		txtColor.setText("");
		lblSupplier.setLabelFor(cbxSupplier);
		lblSupplier.setText(res.getString("Supplier"));
		//lblYear.setLabelFor(cbxYear);
		lblYear.setLabelFor(txtYear);
		lblYear.setText(res.getString("Year"));
		lblSeason.setLabelFor(cbxSeason);
		lblSeason.setText(res.getString("Season"));
		this.setBackground(theAppMgr.getBackgroundColor());
		lblSKU.setAppMgr(theAppMgr);
		lblSKU.setFont(theAppMgr.getTheme().getLabelFont());
		txtSKU.setAppMgr(theAppMgr);
		txtSKU.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblOR.setAppMgr(theAppMgr);
		lblOR.setFont(theAppMgr.getTheme().getLabelFont());
		lblStyle.setAppMgr(theAppMgr);
		lblStyle.setFont(theAppMgr.getTheme().getLabelFont());
		txtStyle.setAppMgr(theAppMgr);
		txtStyle.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblModel.setAppMgr(theAppMgr);
		lblModel.setFont(theAppMgr.getTheme().getLabelFont());
		txtModel.setAppMgr(theAppMgr);
		txtModel.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblFabric.setAppMgr(theAppMgr);
		lblFabric.setFont(theAppMgr.getTheme().getLabelFont());
		txtFabric.setAppMgr(theAppMgr);
		txtFabric.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblColor.setAppMgr(theAppMgr);
		lblColor.setFont(theAppMgr.getTheme().getLabelFont());
		txtColor.setAppMgr(theAppMgr);
		txtColor.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblSupplier.setAppMgr(theAppMgr);
		lblSupplier.setFont(theAppMgr.getTheme().getLabelFont());
		cbxSupplier.setAppMgr(theAppMgr);
		lblYear.setAppMgr(theAppMgr);
		lblYear.setFont(theAppMgr.getTheme().getLabelFont());
		txtYear.setAppMgr(theAppMgr);
		txtYear.setFont(theAppMgr.getTheme().getTextFieldFont());
		txtYear.setDocument(new TextFilter(TextFilter.NUMERIC, 4));
		lblSeason.setAppMgr(theAppMgr);
		lblSeason.setFont(theAppMgr.getTheme().getLabelFont());
		cbxSeason.setAppMgr(theAppMgr);
		this.add(lblSKU
				, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
						, GridBagConstraints.NONE, new Insets(0, 12, 0, 4), 4, 4));
		this.add(txtSKU
				, new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 4), 4, 4));
		this.add(lblOR
				, new GridBagConstraints(4, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER
						, GridBagConstraints.NONE, new Insets(0, 8, 0, 8), 4, 4));
		this.add(lblStyle
				, new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
						, GridBagConstraints.NONE, new Insets(0, 8, 0, 4), 4, 4));
		this.add(txtStyle
				, new GridBagConstraints(7, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 8), 4, 4));
		this.add(lblModel
				, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
						, GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
		this.add(txtModel
				, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 4));
		this.add(lblFabric
				, new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
						, GridBagConstraints.NONE, new Insets(4, 8, 0, 4), 4, 0));
		this.add(txtFabric
				, new GridBagConstraints(3, 2, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 4));
		this.add(lblColor
				, new GridBagConstraints(6, 1, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
						, GridBagConstraints.NONE, new Insets(4, 8, 0, 8), 4, 0));
		this.add(txtColor
				, new GridBagConstraints(6, 2, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 8), 4, 4));
		this.add(lblSupplier
				, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
						, GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
		this.add(cbxSupplier
				, new GridBagConstraints(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 0));
		this.add(lblYear
				, new GridBagConstraints(3, 3, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
						, GridBagConstraints.NONE, new Insets(4, 8, 0, 4), 4, 0));
		this.add(txtYear
				, new GridBagConstraints(3, 4, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 4));
		this.add(lblSeason
				, new GridBagConstraints(6, 3, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
						, GridBagConstraints.NONE, new Insets(4, 8, 0, 8), 4, 0));
		this.add(cbxSeason
				, new GridBagConstraints(6, 4, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
						, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 8), 4, 0));
		txtSKU.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 128));
		txtStyle.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
		txtModel.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
		txtFabric.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
		txtColor.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 6));
	}
	
	public void initSupplierSeasonYearMap() {
		try {
			CMSStore cmsStore = (CMSStore)theAppMgr.getGlobalObject("STORE");
			if (cmsStore != null)
				supplierSeasonYearMap = CMSItemHelper.getSupplierSeasonYear(theAppMgr
						, (CMSStore)theAppMgr.getGlobalObject("STORE"));
			else
				return;
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		Vector supplierVec = new Vector();
		Vector seasonVec = new Vector();
		if (supplierSeasonYearMap != null) {
			Map supplierMap = (Map)(supplierSeasonYearMap.get("supplier"));
			supplierVec.addElement(new KeyValuePairObject("", ""));
			for (Iterator i = supplierMap.keySet().iterator(); i.hasNext(); ) {
				Object keyObj = i.next();
				supplierVec.addElement(new KeyValuePairObject(keyObj, supplierMap.get(keyObj)));
			}
			//Fix for Defect # 1257
			Collections.sort(supplierVec, new StoreNameComparator());
			
			Map seasonMap = (Map)(supplierSeasonYearMap.get("season"));
			seasonVec.addElement(new KeyValuePairObject("", ""));
			for (Iterator i = seasonMap.keySet().iterator(); i.hasNext(); ) {
				Object keyObj = i.next();
				// Issue # 580
				seasonVec.addElement(new KeyValuePairObject(keyObj, keyObj + ": " + seasonMap.get(keyObj)));
			}
		}
		cbxSupplier.setModel(new DefaultComboBoxModel(supplierVec));
		cbxSeason.setModel(new DefaultComboBoxModel(seasonVec)); 
		
	}
	
	public void reset() {
		txtSKU.setText("");
		txtModel.setText("");
		txtStyle.setText("");
		cbxSupplier.setSelectedIndex(0);
		txtFabric.setText("");
		txtColor.setText("");
		txtYear.setText("");
		cbxSeason.setSelectedIndex(0);
	}
	
	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getSku() {
		return txtSKU.getText().trim();
	}
	
	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getModel() {
		return txtModel.getText().trim();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getStyle() {
		return txtStyle.getText().trim();
	}
	
	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getSupplier() {
		if (this.cbxSupplier.getSelectedItem() != null
				&& !cbxSupplier.getSelectedItem().toString().trim().equals("")) {
			return((KeyValuePairObject)cbxSupplier.getSelectedItem()).key.toString();
		}
		return null;
	}

	
	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getFabric() {
		return txtFabric.getText().trim();
	}
	
	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getColor() {
		return txtColor.getText().trim();
	}
	
	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getYear() {
		return txtYear.getText().trim();
	}
	
	
	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getSeason() {
		if (this.cbxSeason.getSelectedItem() != null
				&& !cbxSeason.getSelectedItem().toString().trim().equals("")) {
			return (((KeyValuePairObject)cbxSeason.getSelectedItem()).key.toString());
		}
		return null;
	}
	
	
	private class KeyValuePairObject {
		Object key;
		Object value;
		
		/**
		 * put your documentation comment here
		 * @param     Object key
		 * @param     Object value
		 */
		KeyValuePairObject(Object key, Object value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * put your documentation comment here
		 * @return
		 */
		public String toString() {
			return value.toString();
		}
		
		/**
		 * put your documentation comment here
		 * @param obj
		 * @return
		 */
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof KeyValuePairObject) || key == null || value == null)
				return false;
			else
				return key.equals(((KeyValuePairObject)obj).key)
				&& value.equals(((KeyValuePairObject)obj).value);
		}
	}
	
	private class CustomComboBox extends JCMSComboBox {
		
		/**
		 * put your documentation comment here
		 * @return
		 */
		public Dimension getPreferredSize() {
			return new Dimension((int)(95 * CMSApplet.r), (int)(30 * CMSApplet.r));
		}
		
		/**
		 * put your documentation comment here
		 * @return
		 */
		public Dimension getMaximumSize() {
			return new Dimension((int)(95 * CMSApplet.r), (int)(30 * CMSApplet.r));
		}
		
		/**
		 * put your documentation comment here
		 * @return
		 */
		public Dimension getMinimumSize() {
			return new Dimension((int)(95 * CMSApplet.r), (int)(30 * CMSApplet.r));
		}
	}
	
	private class StoreNameComparator implements Comparator {
		public int compare(Object obj1, Object obj2) {
			String value1 = ((KeyValuePairObject) obj1).value.toString().toUpperCase();
			String value2 = ((KeyValuePairObject) obj2).value.toString().toUpperCase();        
			return value1.compareTo(value2);
		}
	}

}
