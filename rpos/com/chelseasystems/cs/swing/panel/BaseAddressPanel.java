/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import java.util.Vector;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.swing.bean.JCMSComboBox;
import com.chelseasystems.cr.swing.bean.JCMSMaskedTextField;
import java.util.ResourceBundle;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.swing.bean.JCMSMaskedTextField_JP;
import com.chelseasystems.cs.swing.bean.JCMSTextField_JP;
import com.chelseasystems.cs.util.Version;

import java.awt.Component;
import com.chelseasystems.cr.swing.bean.JCMSTextArea;
import java.awt.Dimension;

/**
 * <p>Title: BaseAddressPanel</p>
 *
 * <p>Description: Base Address Panel </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillnetInc</p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
public abstract class BaseAddressPanel extends JPanel {
	private static final String CONFIGURATION_FILE = "address.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	public static final String FULL_ADDRESS_PANEL = "FULL_ADDRESS_PANEL";
	public static final String SHIPPING_ADDRESS_PANEL = "SHIPPING_ADDRESS_PANEL";
	public static String ALPHA_NUMERIC_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";	  
	protected String addressPanelType;
	protected String addressFormat;
	protected Address addressCurrent;
	protected String nameSpec;
	/**
	 * AddressLine1
	 */
	protected JCMSLabel lblAddressLine1;
	/**
	 * AddressLine1 Value
	 */
	protected JCMSTextField txtAddressLine1;
	/**
	 * AddressLine2
	 */
	protected JCMSLabel lblAddressLine2;
	/**
	 * AddressLine2 value
	 */
	protected JCMSTextField txtAddressLine2;
	/**
	 * City
	 */
	protected JCMSLabel lblCity;
	/**
	 * City Value
	 */
	protected JCMSTextField txtCity;
	/**
	 * State
	 */
	protected JCMSLabel lblState;
	/**
	 * State value
	 */
	protected JCMSTextField txtState;
	/**
	 * ZipCode
	 */
	protected JCMSLabel lblZipCode;
	/**
	 * ZipCode value
	 */
	protected JCMSTextField txtZipCode;
	/**
	 * Phone1
	 */
	protected JCMSLabel lblPhone1;
	/**
	 * Phone1 Value
	 */
	protected JCMSTextField txtPhone1;
	/**
	 * PhoneType1
	 */
	protected JCMSLabel lblPhoneType1;
	/**
	 * Seleted PhoneType1
	 */
	protected JCMSComboBox cbxPhoneType1;
	/**
	 * Phone2
	 */
	protected JCMSLabel lblPhone2;
	/**
	 * Phone2 value
	 */
	protected JCMSTextField txtPhone2;
	/**
	 * PhoneType2
	 */
	protected JCMSLabel lblPhoneType2;
	/**
	 * Selected PhoneType2
	 */
	protected JCMSComboBox cbxPhoneType2;
	/**
	 * Phone3
	 */
	protected JCMSLabel lblPhone3;
	/**
	 * Phone3 value
	 */
	protected JCMSTextField txtPhone3;
	/**
	 * PhoneTyp2
	 */
	protected JCMSLabel lblPhoneType3;
	/**
	 * Selected PhoneType3
	 */
	protected JCMSComboBox cbxPhoneType3;
	/**
	 * Address Type
	 */
	protected JCMSLabel lblAddressType;
	/**
	 * Selected AddressType
	 */
	protected JCMSComboBox cbxAddressType;
	/**
	 * Resource
	 */
	protected final ResourceBundle RESOURCE;
	/**
	 * Is primary address
	 */
	protected boolean bIsPrimary;
	protected Vector vecAddressTypeKeys;
	protected Vector vecPhoneTypeKeys;
	protected IApplicationManager theAppMgr;

	/**
	 * put your documentation comment here
	 */
	public BaseAddressPanel() {
		this(FULL_ADDRESS_PANEL);
	}

	/**
	 * put your documentation comment here
	 * @param String
	 *            addressPanelType
	 */
	public BaseAddressPanel(String addressPanelType) {
		this.addressPanelType = addressPanelType;
		RESOURCE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		nameSpec = configMgr.getString("NAME_SPEC");
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getAddressLine1();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getAddressLine2();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getCity();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getState();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getZipCode();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getPhone1();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getPhone2();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getPhone3();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getSelectedPhoneType1();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getSelectedPhoneType2();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getSelectedPhoneType3();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract String getSelectedAddressType();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract Address getCustomerAddress();

	/**
	 * put your documentation comment here
	 * @return
	 */
	public abstract boolean isPrimary();

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setAddressLine1(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setAddressLine2(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setCity(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setState(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setZipCode(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setPhone1(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setPhone2(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setPhone3(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setSelectedPhoneType1(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setSelectedPhoneType2(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setSelectedPhoneType3(String sValue);

	/**
	 * put your documentation comment here
	 * @param sValue
	 */
	public abstract void setSelectedAddressType(String sValue);

	/**
	 * put your documentation comment here
	 * @param bIsPrimary
	 */
	public abstract void setPrimary(boolean bIsPrimary);

	/**
	 * put your documentation comment here
	 * @param vecAddressTypes
	 * @param vecAddressTypeKeys
	 */
	public abstract void setAddressTypes(Vector vecAddressTypes, Vector vecAddressTypeKeys);

	/**
	 * put your documentation comment here
	 * @param vecPhoneTypes
	 * @param vecPhoneTypeKeys
	 */
	public abstract void setPhoneTypes(Vector vecPhoneTypes, Vector vecPhoneTypeKeys);

	/**
	 * put your documentation comment here
	 * @param address
	 */
	public abstract void setCustomerAddress(Address address);

	/**
	 * put your documentation comment here
	 */
	public abstract void requestFocusToPrimaryPhone();

	/**
	 * put your documentation comment here
	 */
	public abstract void requestFocusToSecondaryPhone();

	/**
	 * put your documentation comment here
	 */
	public abstract void requestFocusToTernaryPhone();

	/**
	 * put your documentation comment here
	 * @param listener
	 */
	public abstract void addItemListener(ItemListener listener);

	public abstract void removeItemListener(ItemListener listener);

	/**
	 * put your documentation comment here
	 * @param theAppMgr
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		this.theAppMgr = theAppMgr;
		this.setBackground(theAppMgr.getBackgroundColor());
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		// Loop through and set Application managers for all
		// JCMS components.
		for (int iCtr = 0; iCtr < this.getComponentCount(); iCtr++) {
			Component component = this.getComponent(iCtr);
			if (component instanceof JCMSTextField) {
				((JCMSTextField) component).setAppMgr(theAppMgr);
				((JCMSTextField) component).setFont(theAppMgr.getTheme().getTextFieldFont());
				((JCMSTextField) component).setPreferredSize(new Dimension((int) (65 * r), (int) (30 * r)));
			} else if (component instanceof JCMSTextArea) {
				((JCMSTextArea) component).setAppMgr(theAppMgr);
				((JCMSTextArea) component).setFont(theAppMgr.getTheme().getTextFieldFont());
				((JCMSTextArea) component).setPreferredSize(new Dimension((int) (55 * r), (int) (50 * r)));
			} else if (component instanceof JCMSLabel) {
				((JCMSLabel) component).setAppMgr(theAppMgr);
				((JCMSLabel) component).setFont(theAppMgr.getTheme().getLabelFont());
				((JCMSLabel) component).setPreferredSize(new Dimension((int) (85 * r), (int) (25 * r)));
			} else if (component instanceof JCMSComboBox) {
				((JCMSComboBox) component).setAppMgr(theAppMgr);
				((JCMSComboBox) component).setPreferredSize(new Dimension((int) (55 * r), (int) (25 * r)));
			} else if (component instanceof JCMSMaskedTextField) {
				((JCMSMaskedTextField) component).setAppMgr(theAppMgr);
				((JCMSMaskedTextField) component).setFont(theAppMgr.getTheme().getTextFieldFont());
				((JCMSMaskedTextField) component).setPreferredSize(new Dimension((int) (55 * r), (int) (30 * r)));
			} else if (component instanceof JPanel) {
				((JPanel) component).setFont(theAppMgr.getTheme().getTextFieldFont());
				((JPanel) component).setBackground(theAppMgr.getBackgroundColor());
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param addressFormat
	 */
	public void setAddressFormat(String addressFormat) {
		this.addressFormat = addressFormat;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getCountry() {
		return addressFormat;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param shippingRequest
	 * @return
	 * @exception BusinessRuleException
	 */
	public CMSShippingRequest populateShippingRequest(CMSShippingRequest shippingRequest) 
		throws BusinessRuleException {
		shippingRequest.setAddressFormat(addressFormat);
		shippingRequest.setAddress(getAddressLine1());
		shippingRequest.setAddress2(getAddressLine2());
		shippingRequest.setCity(getCity());
		shippingRequest.setState(getState());
		shippingRequest.setCountry(getCountry());
		shippingRequest.setZipCode(getZipCode());
		shippingRequest.setPhone(getPhone1());
		return shippingRequest;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param shippingRequest
	 */
	public void setShippingRequest(CMSShippingRequest shippingRequest) {
		setAddressLine1(shippingRequest.getAddress());
		setAddressLine2(shippingRequest.getAddress2());
		setCity(shippingRequest.getCity());
		setState(shippingRequest.getState());
		setZipCode(shippingRequest.getZipCode());
		setPhone1(shippingRequest.getPhone());
	}

	/**
	 * Reset the GUI
	 */
	public void reset() {
		txtAddressLine1.setText("");
		txtAddressLine2.setText("");
		txtZipCode.setText("");
		txtState.setText("");
		txtCity.setText("");
		txtPhone1.setText("");
		if (this.addressPanelType.trim().equalsIgnoreCase(FULL_ADDRESS_PANEL)) {
			if (cbxPhoneType1.getItemCount() > 0)
				cbxPhoneType1.setSelectedIndex(0);
			txtPhone2.setText("");
			if (cbxPhoneType2.getItemCount() > 0)
				cbxPhoneType2.setSelectedIndex(0);
			txtPhone3.setText("");
			if (cbxPhoneType3.getItemCount() > 0)
				cbxPhoneType3.setSelectedIndex(0);
			if (cbxAddressType.getItemCount() > 0)
				cbxAddressType.setSelectedIndex(0);
		}
		addressCurrent = new Address();
		addressCurrent.setAddressFormat(addressFormat);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param bEnabled
	 */
	public void setEnabled(boolean bEnabled) {
		txtAddressLine1.setEnabled(bEnabled);
		txtAddressLine2.setEnabled(bEnabled);
		txtZipCode.setEnabled(bEnabled);
		txtCity.setEnabled(bEnabled);
		txtState.setEnabled(bEnabled);
		txtPhone1.setEnabled(bEnabled);
		if (this.addressPanelType.trim().equalsIgnoreCase(FULL_ADDRESS_PANEL)) {
			cbxPhoneType1.setEnabled(bEnabled);
			txtPhone2.setEnabled(bEnabled);
			cbxPhoneType2.setEnabled(bEnabled);
			txtPhone3.setEnabled(bEnabled);
			cbxPhoneType3.setEnabled(bEnabled);
			cbxAddressType.setEnabled(bEnabled);
		}
	}

	public void setModifyEnabled(boolean bEnabled) {
		setEnabled(bEnabled);
	}

	/**
	 * put your documentation comment here
	 * @param addressPanelType
	 */
	public void resetAddressPanelType(String addressPanelType) {
		if (addressPanelType == null)
			return;
		this.addressPanelType = addressPanelType;
		if (this.addressPanelType.trim().equalsIgnoreCase(FULL_ADDRESS_PANEL)) {
			lblPhone2 = new JCMSLabel();
			txtPhone2 = getJCMSMaskedTextField();
			txtPhone3 = getJCMSMaskedTextField();
			if (nameSpec != null && nameSpec.length() > 0) {
				txtPhone2.setDocument(new TextFilter(nameSpec, 15));
				txtPhone3.setDocument(new TextFilter(nameSpec, 15));
			}
			lblPhone3 = new JCMSLabel();			
			lblPhoneType1 = new JCMSLabel();
			cbxPhoneType1 = new JCMSComboBox();
			lblPhoneType2 = new JCMSLabel();
			cbxPhoneType2 = new JCMSComboBox();
			lblPhoneType3 = new JCMSLabel();
			cbxPhoneType3 = new JCMSComboBox();
			lblAddressType = new JCMSLabel();
			cbxAddressType = new JCMSComboBox();
			lblPhone1.setText(RESOURCE.getString("Phone 1"));
			lblPhoneType1.setText(RESOURCE.getString("Type "));
			lblPhone2.setText(RESOURCE.getString("Phone 2"));
			lblPhoneType2.setText(RESOURCE.getString("Type"));
			lblPhone3.setText(RESOURCE.getString("Phone 3"));
			lblPhoneType3.setText(RESOURCE.getString("Type"));
			lblAddressType.setText(RESOURCE.getString("Address Type"));
			txtPhone1.setName("PHONE1");
			txtPhone2.setName("PHONE2");
			txtPhone3.setName("PHONE3");
			cbxPhoneType1.setName("PHONE_TYPE_1");
			cbxPhoneType2.setName("PHONE_TYPE_2");
			cbxPhoneType3.setName("PHONE_TYPE_3");
			cbxAddressType.setName("ADDRESS_TYPE");
		} else if (this.addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL)) {
			remove(lblPhone2);
			remove(txtPhone2);
			remove(lblPhone3);
			remove(txtPhone3);
			remove(lblPhoneType1);
			remove(cbxPhoneType1);
			remove(lblPhoneType2);
			remove(cbxPhoneType2);
			remove(lblPhoneType3);
			remove(cbxPhoneType3);
			remove(lblAddressType);
			remove(cbxAddressType);
			lblPhone2 = null;
			txtPhone2 = null;
			lblPhone3 = null;
			txtPhone3 = null;
			lblPhoneType1 = null;
			cbxPhoneType1 = null;
			lblPhoneType2 = null;
			cbxPhoneType2 = null;
			lblPhoneType3 = null;
			cbxPhoneType3 = null;
			lblAddressType = null;
			lblPhone1.setText(RESOURCE.getString("Phone"));
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void initAddressPanel() {
		lblAddressLine1 = new JCMSLabel();
		txtAddressLine1 = getJCMSTextField();
		lblAddressLine2 = new JCMSLabel();
		txtAddressLine2 = getJCMSTextField();
		lblZipCode = new JCMSLabel();
		txtZipCode = getJCMSTextField();
		lblCity = new JCMSLabel();
		txtCity = getJCMSTextField();
		lblState = new JCMSLabel();
		txtState = getJCMSTextField();
		lblPhone1 = new JCMSLabel();
		txtPhone1 = getJCMSMaskedTextField();
		txtPhone1.setName("PHONE1");
		if (nameSpec != null && nameSpec.length() > 0)
			txtPhone1.setDocument(new TextFilter(nameSpec, 15));
		lblAddressLine1.setLabelFor(txtAddressLine1);
		lblAddressLine1.setText(RESOURCE.getString("AddressLine1"));
		lblAddressLine2.setText(RESOURCE.getString("AddressLine2"));
		lblZipCode.setText(RESOURCE.getString("Zip Code"));
		lblCity.setText(RESOURCE.getString("City"));
		lblState.setText(RESOURCE.getString("State"));
		lblPhone1.setText(RESOURCE.getString("Phone 1"));
		resetAddressPanelType(this.addressPanelType);
		/**
		 * Text Verifier
		 */
		// Text limits set as per max sizes allowed over various db tables involved
		// -MSB 02/10/06
		// Since Input is free form and we just want to
		// restrict the text length we don't need to filter
		// input. This resolves double-byte character input issues.
		txtAddressLine1.setDocument(new CMSTextFilter(40));
		txtAddressLine2.setDocument(new CMSTextFilter(30));
		txtCity.setDocument(new CMSTextFilter(30));
		txtState.setDocument(new CMSTextFilter(30));
		txtZipCode.setDocument(new CMSTextFilter(10));
		txtPhone1.setDocument(new CMSTextFilter(15));
		txtPhone2.setDocument(new CMSTextFilter(15));
		txtPhone3.setDocument(new CMSTextFilter(15));
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isAllScreenInputValid() throws BusinessRuleException {
		if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))) {
			return isAddressLine1Valid() && isAddressLine2Valid() && isZipCodeValid() && isCityValid()
			//Remove isStateValide for Europe
			//&& isStateValid() && isPhone1Valid() && isPhoneType1Valid() && isPhone2Valid()
			&& isPhone1Valid() && isPhoneType1Valid() && isPhone2Valid()
			&& isPhoneType2Valid() && isPhone3Valid() && isPhoneType3Valid();
		} else {		
			return isAddressLine1Valid() && isAddressLine2Valid() && isZipCodeValid() 
			&& isCityValid() && isStateValid() && isPhone1Valid() && isPhoneType1Valid() 
			&& isPhone2Valid() && isPhoneType2Valid() && isPhone3Valid() && isPhoneType3Valid();
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isAddressLine1Valid() throws BusinessRuleException {
		if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))) {
			if (txtAddressLine1.getText().trim().length() > 0)
				return true;
		} else if (!addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL) 
				|| txtAddressLine1.getText().trim().length() > 0) {
				return true;
		}
		txtAddressLine1.requestFocus();
		throw new BusinessRuleException(RESOURCE.getString("AddressLine1: This field is required."));
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isAddressLine2Valid() throws BusinessRuleException {
		return true;
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isCityValid() throws BusinessRuleException {
		if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))) {
			if (txtCity.getText().trim().length() > 0)
		      return true;
		} else if (!addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL) 
				|| txtCity.getText().trim().length() > 0) {
			return true;
		}
		txtCity.requestFocus();
		throw new BusinessRuleException(RESOURCE.getString("Town / City: This field is required."));
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isStateValid() throws BusinessRuleException {
		if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))) {
			if (txtState.getText().trim().length() > 0)
				return true;
		} else if (!addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL) 
				|| txtState.getText().trim().length() > 0) {
				return true;
		}
		txtState.requestFocus();
		throw new BusinessRuleException(RESOURCE.getString("State: This field is required."));
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isZipCodeValid() throws BusinessRuleException {
		if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))) {
			if (txtZipCode.getText().trim().length() > 0)
		      return true;
		} else if (!addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL) 
				|| txtZipCode.getText().trim().length() > 0) {
			return true;
		}
		txtZipCode.requestFocus();
		throw new BusinessRuleException(RESOURCE.getString("Zip / Postal Code: This field is required."));
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isPhone1Valid() throws BusinessRuleException {
		if (!addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL) 
				|| txtPhone1.getText().trim().length() > 0)
			return true;
		txtPhone1.requestFocus();
		throw new BusinessRuleException(RESOURCE.getString("Phone: This field is required."));
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isPhone2Valid() throws BusinessRuleException {
		return true;
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isPhone3Valid() throws BusinessRuleException {
		return true;
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isPhoneType1Valid() throws BusinessRuleException {
		return true;
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isPhoneType2Valid() throws BusinessRuleException {
		return true;
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isPhoneType3Valid() throws BusinessRuleException {
		return true;
	}

	private class NumericListener implements KeyListener {
		private final String KEYS_ALLOWED = "0123456789-";

		/**
		 * put your documentation comment here
		 * 
		 * @param ke
		 */
		public void keyPressed(KeyEvent ke) {
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param ke
		 */
		public void keyTyped(KeyEvent ke) {
			JCMSTextField txt = (JCMSTextField) ke.getComponent();
			if (KEYS_ALLOWED.indexOf(ke.getKeyChar()) == -1) {
				ke.consume();
				return;
			}
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param ke
		 */
		public void keyReleased(KeyEvent ke) {
		}
	}

	public JCMSTextField getTxtCity() {
		return this.txtCity;
	}

	public JCMSTextField getTxtZipCode() {
		return this.txtZipCode;
	}
	
	private JCMSTextField getJCMSTextField(){
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
			return new JCMSTextField_JP();
		}
		
		return  new JCMSTextField();
	}
	
	private JCMSTextField getJCMSMaskedTextField(){
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
			return new JCMSMaskedTextField_JP();
		}
		
		return  new JCMSMaskedTextField();
	}

}
