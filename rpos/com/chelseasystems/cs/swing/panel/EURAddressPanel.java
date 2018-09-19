/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import java.awt.*;
import com.chelseasystems.cr.swing.bean.*;
import java.awt.event.ItemListener;
import com.chelseasystems.cs.address.Address;

import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;
import com.chelseasystems.cs.address.AddressMgr;
import com.chelseasystems.cr.telephone.TelephoneType;
import com.chelseasystems.cr.telephone.Telephone;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import com.chelseasystems.cr.rules.BusinessRuleException;

/**
 * <p>Title: EURAddressPanel</p>
 *
 * <p>Description: EURAddressPanel</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc.</p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+
| 1    | 03-07-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
+------+------------+-----------+-----------+----------------------------------------------------+
| 2    | 06-02-2005 | Sameena   | 96        | When entering customers with country other than USA|
|      |            |           |           |  i.e Canada or Japan phone num not assigned        |
|      |            | Manpreet  |           |  properly                                          |
|      |            |           |           |  Modified : getCustomerAddress()                   |
+------+------------+-----------+-----------+-------------- -------------------------------------+
| 3    | 06-16-2005 | Vikram    | N/A       | Postal Code should be limited to 10 characters     |
+------+------------+-----------+-----------+----------------------------------------------------+
| 4    | 06-22-2005 | Vikram    | 262       | Address field sizes and intro of address Format    |
+------+------------+-----------+-----------+----------------------------------------------------+
| 5    | 07-22-2005 | Vikram    | 286       | Changes to support shipping to foreign countries.  |
+------+------------+-----------+-----------+----------------------------------------------------+
| 6    | 09-01-2005 |  Manpreet | 812       | Implemented Country as a drop down instead of      |
|      |            |           |           |  texfield. Chanaged jbinit() to reset position of  |
|      |            |           |           |  components. Introduced populateCountries() and    |
|      |            |           |           |  setCountries(). Changed getCountry,setCountry and |
|      |            |           |           |      isCountryValid().                             |
+------+------------+-----------+-----------+-------------- -------------------------------------+
*/
public class EURAddressPanel extends BaseAddressPanel {
	/**
	 * Country
	 */
	private JCMSLabel lblCountry;
	/**
	 * Message
	 */
	private JCMSLabel lblMessage;
	private JCMSComboBox cbxCountry;
	private Vector vecCountryKeys;

	/**
	 * Default Constructor
	 */
	public EURAddressPanel() {
		this(null);
	}

	/**
	 * put your documentation comment here
	 */
	private void populateCountries() {
		AddressMgr addMgr = new AddressMgr();
		Vector vecCountryLabels = addMgr.getCountryLabels();
		Vector vecCountryCodes = addMgr.getCountryKeys();
		if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
			sortCountryLabels(vecCountryLabels, vecCountryCodes);
		}
//		Collections.sort(vecCountryLabels);
		setCountries(vecCountryLabels, vecCountryCodes);
	}

	private void sortCountryLabels(Vector vecCountryLabels, Vector vecCountryCodes) {
		TreeMap labelCodeMap = new TreeMap();
		final String SEP = "-||-";
		for(int i=0; i < vecCountryLabels.size();i++){
			labelCodeMap.put((String)vecCountryLabels.get(i),
					((String)vecCountryLabels.get(i) + SEP + (String)vecCountryCodes.get(i)));
		}
		
		Vector tempVec = new Vector(labelCodeMap.values());
		vecCountryCodes.removeAllElements();
		vecCountryLabels.removeAllElements();
		String labelCodeString = "";
		for(int i=0; i < tempVec.size();i++){
			labelCodeString = (String) tempVec.get(i);
			vecCountryLabels.add(labelCodeString.substring(0, labelCodeString.indexOf(SEP)));
			vecCountryCodes.add(labelCodeString.substring(labelCodeString.indexOf(SEP) + SEP.length()));
		}
	}

	/**
	 * put your documentation comment here
	 * @param boolean
	 *            bSemiDisplay
	 */
	public EURAddressPanel(boolean bSemiDisplay) {
		this();
		if (bSemiDisplay)
			this.resetAddressPanelType(SHIPPING_ADDRESS_PANEL);
	}

	/**
	 * Pass Address Object
	 * 
	 * @param addressCurrent
	 *            Customer'sAddress
	 */
	public EURAddressPanel(Address addressCurrent) {
		this(addressCurrent, FULL_ADDRESS_PANEL);
	}

	/**
	 * put your documentation comment here
	 * @param Address
	 *            addressCurrent
	 * @param String
	 *            addressPanelType
	 */
	public EURAddressPanel(Address addressCurrent, String addressPanelType) {
		super(addressPanelType);
		try {
			jbInit();
			this.addressCurrent = addressCurrent;
			populateCountries();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * CustomerAddress
	 * @return CustomerAddress
	 */
	public Address getCustomerAddress() {
		try {
			if (addressCurrent == null)
				addressCurrent = new Address();
			addressCurrent.setAddressLine1(getAddressLine1());
			addressCurrent.setAddressLine2(getAddressLine2());
			addressCurrent.setCity(getCity());
			addressCurrent.setState(getState());
			addressCurrent.setZipCode(getZipCode());
			addressCurrent.setAddressType(getSelectedAddressType());
			addressCurrent.setAddressFormat(addressFormat);
			/*
			 * Bug #: 96 When entering customers with country other than USA i.e Canada or Japan phone num not assigned properly
			 */
			try {
				// SP: Modified the code to check if the phone number is not empty and
				// construct the telephone object with no area code and country code.
				// SP: Modified the code to check if the phone number is not empty and
				// construct the telephone object with no area code and country code.
				// MSB - 01/12/2006
				// Telephone object filters digits for telephone number
				// internally, so this removes any special characters
				// that were in input.
				// In order to make sure, telephone number is free format
				// newTelephoneNumber() is used. It doesnt filter digits.
				if (getPhone1().length() > 0) {
					Telephone tele = new Telephone(new TelephoneType(getSelectedPhoneType1()), "", "", getPhone1());
					tele = tele.newTelephoneNumber(getPhone1());
					addressCurrent.setPrimaryPhone(tele);
					// MSB -01/12/2006
				}
				if (getPhone2().length() > 0) {
					Telephone tele = new Telephone(new TelephoneType(getSelectedPhoneType2()), "", "", getPhone2());
					tele = tele.newTelephoneNumber(getPhone2());
					addressCurrent.setSecondaryPhone(tele);
					// MSB -01/12/2006
				}
				if (getPhone3().length() > 0) {
					Telephone tele = new Telephone(new TelephoneType(getSelectedPhoneType3()), "", "", getPhone3());
					tele = tele.newTelephoneNumber(getPhone3());
					addressCurrent.setTernaryPhone(tele);
					// MSB -01/12/2006
				}
			} catch (Exception e) {
			}	
			addressCurrent.setCountry(getCountry());
			return addressCurrent;
		} catch (Exception e) {
		}
		return null;
	}

	public Address getCustomerAddressForFiscalDocument() {
		try {
			Address fiscalAddress = new Address();
			fiscalAddress.setAddressLine1(getAddressLine1());
			fiscalAddress.setAddressLine2(getAddressLine2());
			fiscalAddress.setCity(getCity());
			fiscalAddress.setState(getState());
			fiscalAddress.setZipCode(getZipCode());
			fiscalAddress.setAddressType(getSelectedAddressType());
			fiscalAddress.setAddressFormat(addressFormat);
			try {
				if (getPhone1().length() > 0) {
					Telephone tele = new Telephone(new TelephoneType(getSelectedPhoneType1()), "", "", getPhone1());
					tele = tele.newTelephoneNumber(getPhone1());
					fiscalAddress.setPrimaryPhone(tele);
				}
				if (getPhone2().length() > 0) {
					Telephone tele = new Telephone(new TelephoneType(getSelectedPhoneType2()), "", "", getPhone2());
					tele = tele.newTelephoneNumber(getPhone2());
					fiscalAddress.setSecondaryPhone(tele);
				}
				if (getPhone3().length() > 0) {
					Telephone tele = new Telephone(new TelephoneType(getSelectedPhoneType3()), "", "", getPhone3());
					tele = tele.newTelephoneNumber(getPhone3());
					fiscalAddress.setTernaryPhone(tele);
				}
			} catch (Exception e) {
			}
			fiscalAddress.setCountry(getCountry());
			return fiscalAddress;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * CustomerAddress
	 * @param address
	 *            CustomerAddress
	 */
	public void setCustomerAddress(Address address) {
		reset();
		addressCurrent = address;
		setPrimary(address.isUseAsPrimary());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setState(address.getState());
		setZipCode(address.getZipCode());
		try {
			if (address.getPrimaryPhone() != null) {
				setPhone1(address.getPrimaryPhone().getTelephoneNumber());
				setSelectedPhoneType1(address.getPrimaryPhone().getTelephoneType().getType());
			}
			if (address.getSecondaryPhone() != null) {
				setPhone2(address.getSecondaryPhone().getTelephoneNumber());
				setSelectedPhoneType2(address.getSecondaryPhone().getTelephoneType().getType());
			}
			if (address.getTernaryPhone() != null) {
				setPhone3(address.getTernaryPhone().getTelephoneNumber());
				setSelectedPhoneType3(address.getTernaryPhone().getTelephoneType().getType());
			}
		} catch (Exception e) {
		}
		setSelectedAddressType(address.getAddressType());
		// setCountry(address.getEuropeanCountry());
		setCountry(address.getCountry());
	}

	/**
	 * Is Primary
	 * @return True/False
	 */
	public boolean isPrimary() {
		return this.bIsPrimary;
	}

	/**
	 * Set address is primary or not
	 * 
	 * @param bPrimary
	 *            true/false
	 */
	public void setPrimary(boolean bPrimary) {
		this.bIsPrimary = bPrimary;
	}

	/**
	 * Set AddressLine1
	 * @param sValue
	 *            AddressLine1
	 */
	public void setAddressLine1(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtAddressLine1.setText(sValue);
	}

	/**
	 * Get AddressLine1
	 * 
	 * @return AddressLine1
	 */
	public String getAddressLine1() {
		return txtAddressLine1.getText();
	}

	/**
	 * Set AddressLine2
	 * @param sValue
	 *            AddressLine2
	 */
	public void setAddressLine2(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtAddressLine2.setText(sValue);
	}

	/**
	 * Get AddressLine2
	 * @return AddressLine2
	 */
	public String getAddressLine2() {
		return txtAddressLine2.getText();
	}

	/**
	 * Set City
	 * @param sValue
	 *            City
	 */
	public void setCity(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtCity.setText(sValue);
	}

	/**
	 * Get City
	 * @return City
	 */
	public String getCity() {
		return txtCity.getText();
	}

	/**
	 * Set State
	 * @param sValue
	 *            State
	 */
	public void setState(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtState.setText(sValue);
	}

	/**
	 * Get State
	 * @return State
	 */
	public String getState() {
		return txtState.getText();
	}

	/**
	 * Set ZipCode
	 * @param sValue
	 *            ZipCode
	 */
	public void setZipCode(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtZipCode.setText(sValue);
	}

	/**
	 * Get ZipCode
	 * @return ZipCode
	 */
	public String getZipCode() {
		return txtZipCode.getText();
	}

	/**
	 * Set Phone1
	 * @param sValue
	 *            Phone1
	 */
	public void setPhone1(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtPhone1.setText(sValue);
	}

	/**
	 * Get Phone1
	 * @return Phone1
	 */
	public String getPhone1() {
		return txtPhone1.getText();
	}

	/**
	 * Set Phone2
	 * @param sValue
	 *            Phone2
	 */
	public void setPhone2(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtPhone2.setText(sValue);
	}

	/**
	 * Get Phone2
	 * @return Phone2
	 */
	public String getPhone2() {
		return txtPhone2.getText();
	}

	/**
	 * Set Phone1
	 * @param sValue
	 *            Phone1
	 */
	public void setPhone3(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtPhone3.setText(sValue);
	}

	/**
	 * Get Phone1
	 * @return Phone1
	 */
	public String getPhone3() {
		return txtPhone3.getText();
	}

	/**
	 * Set PhoneType1
	 * @param sValue
	 *            PhoneType1
	 */
	public void setSelectedPhoneType1(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		cbxPhoneType1.setSelectedIndex(selectPhoneKey(sValue));
	}

	/**
	 * Get PhoneType1
	 * @return PhoneType1
	 */
	public String getSelectedPhoneType1() {
		return getPhoneKeyAt(cbxPhoneType1.getSelectedIndex());
	}

	/**
	 * Set PhoneType2
	 * @param sValue
	 *            PhoneType2
	 */
	public void setSelectedPhoneType2(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		cbxPhoneType2.setSelectedIndex(selectPhoneKey(sValue));
	}

	/**
	 * Get PhoneType2
	 * @return PhoneType2
	 */
	public String getSelectedPhoneType2() {
		return getPhoneKeyAt(cbxPhoneType2.getSelectedIndex());
	}

	/**
	 * Set PhoneType3
	 * @param sValue
	 *            PhoneType3
	 */
	public void setSelectedPhoneType3(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		cbxPhoneType3.setSelectedIndex(selectPhoneKey(sValue));
	}

	/**
	 * Get PhoneType3
	 * @return PhoneType3
	 */
	public String getSelectedPhoneType3() {
		return getPhoneKeyAt(cbxPhoneType3.getSelectedIndex());
	}

	/**
	 * Set AddressType
	 * @param sValue
	 *            AddressType
	 */
	public void setSelectedAddressType(String sValue) {
		if (sValue == null || sValue.length() < 1 || vecAddressTypeKeys.indexOf(sValue) == -1)
			return;
		cbxAddressType.setSelectedIndex(vecAddressTypeKeys.indexOf(sValue));
	}

	/**
	 * Get AddressType
	 * @return AddressType
	 */
	public String getSelectedAddressType() {
		return (String) vecAddressTypeKeys.elementAt(cbxAddressType.getSelectedIndex());
	}

	/**
	 * Set AddressTypes
	 * @param vecAddressTypes
	 *            AddressTypes
	 */
	public void setAddressTypes(Vector vecAddressTypes, Vector vecAddressKeys) {
		if (cbxAddressType != null)
			cbxAddressType.setModel(new DefaultComboBoxModel(vecAddressTypes));
		vecAddressTypeKeys = vecAddressKeys;
	}

	/**
	 * Set Country
	 * @param sValue
	 *            Country
	 */
	public void setCountry(String sValue) {
		if (sValue == null || sValue.length() < 1 || vecCountryKeys.indexOf(sValue) == -1)
			return;
		cbxCountry.setSelectedIndex(vecCountryKeys.indexOf(sValue));
	}

	/**
	 * Get country
	 * @return Country
	 */
	public String getCountry() {
		if (cbxCountry.getSelectedIndex() < 0 || cbxCountry.getSelectedIndex() > vecCountryKeys.size())
			return null;
		return (String) vecCountryKeys.elementAt(cbxCountry.getSelectedIndex());
	}

	/**
	 * Set AddressTypes
	 * @param vecAddressTypes
	 *            AddressTypes
	 */
	public void setAddressTypes(Vector vecAddressTypes) {
		cbxAddressType.setModel(new DefaultComboBoxModel(vecAddressTypes));
	}

	/**
	 * put your documentation comment here
	 * @param iIndex
	 * @return
	 */
	private String getPhoneKeyAt(int iIndex) {
		if (iIndex < 0 || iIndex >= vecPhoneTypeKeys.size())
			return null;
		return (String) vecPhoneTypeKeys.elementAt(iIndex);
	}

	/**
	 * put your documentation comment here
	 * @param sKey
	 * @return
	 */
	private int selectPhoneKey(String sKey) {
		if (sKey == null || sKey.length() < 1 || vecPhoneTypeKeys.indexOf(sKey) == -1)
			return -1;
		return vecPhoneTypeKeys.indexOf(sKey);
	}

	/**
	 * Set PhoneTypes
	 * @param vecPhoneTypes
	 *            PhoneTypes
	 */
	public void setPhoneTypes(Vector vecPhoneTypes, Vector vecPhoneTypeKeys) {
		this.vecPhoneTypeKeys = vecPhoneTypeKeys;
		if (cbxPhoneType1 != null)
			cbxPhoneType1.setModel(new DefaultComboBoxModel(vecPhoneTypes));
		if (cbxPhoneType2 != null)
			cbxPhoneType2.setModel(new DefaultComboBoxModel(vecPhoneTypes));
		if (cbxPhoneType3 != null)
			cbxPhoneType3.setModel(new DefaultComboBoxModel(vecPhoneTypes));
	}

	/**
	 * put your documentation comment here
	 * @param vecCountryList
	 * @param vecCountryKeys
	 */
	public void setCountries(Vector vecCountryList, Vector vecCountryKeys) {
		this.vecCountryKeys = vecCountryKeys;
		cbxCountry.setModel(new DefaultComboBoxModel(vecCountryList));
	}

	/**
	 * put your documentation comment here
	 * @param shippingRequest
	 */
	public void setShippingRequest(CMSShippingRequest shippingRequest) {
		super.setShippingRequest(shippingRequest);
		setCountry(shippingRequest.getCountry());
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isAllScreenInputValid() throws BusinessRuleException {
		return super.isAllScreenInputValid() && isCountryValid();
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isCityValid() throws BusinessRuleException {
		try {
			return super.isCityValid();
		} catch (BusinessRuleException e) {
			throw new BusinessRuleException(RESOURCE.getString("City / CEDEX: This field is required."));
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isStateValid() throws BusinessRuleException {
		try {
			return super.isStateValid();
		} catch (BusinessRuleException e) {
			throw new BusinessRuleException(RESOURCE.getString("Prov. / Canton / County: This field is required."));
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isCountryValid() throws BusinessRuleException {
		return true;
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusToPrimaryPhone() {
		txtPhone1.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusToSecondaryPhone() {
		txtPhone2.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusToTernaryPhone() {
		txtPhone3.requestFocus();
	}

	/**
	 * put your documentation comment here
	 * @param listener
	 */
	public void addItemListener(ItemListener listener) {
		cbxAddressType.addItemListener(listener);
	}

	public void removeItemListener(ItemListener listener) {
		cbxAddressType.removeItemListener(listener);
	}

	public void getAddressLine1Focus() {
		txtAddressLine1.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void getAddressLine2Focus() {
		txtAddressLine2.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void getCityFocus() {
		txtCity.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void getStateFocus() {
		txtState.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void getPhone1Focus() {
		txtPhone1.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void getCountryFocus() {
		cbxCountry.requestFocus();
	}

	/**
	 * put your documentation comment here
	 */
	public void getZipCodeFocus() {
		txtZipCode.requestFocus();
	}

	public void setModifyEnabled(boolean bEnabled) {
		super.setEnabled(bEnabled);
		if (addressFormat != null && addressFormat.indexOf("EUR") != -1) {
			cbxCountry.setEnabled(true);
		} else {
			cbxCountry.setEnabled(false);
		}
	}

	/**
	 * Initialize and Layout Components
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		initAddressPanel();
		lblCountry = new JCMSLabel();
		lblMessage = new JCMSLabel();
		lblAddressLine1.setText(RESOURCE.getString("Address Line1"));
		lblAddressLine2.setText(RESOURCE.getString("Address Line2 (*)"));
		lblCity.setText(RESOURCE.getString("City/CEDEX"));
		lblState.setText(RESOURCE.getString("Prov./Canton/County"));
		lblZipCode.setText(RESOURCE.getString("Zip/Post Code"));
		lblCountry.setText(RESOURCE.getString("Country"));
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setText(RESOURCE.getString("(*) Can also be used for CP/Postfatch, Box, Localite   "));
		cbxCountry = new JCMSComboBox();
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		this.setLayout(gridBagLayout1);
		//TD
        this.add(lblCountry, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 1, 0, 0), 0, 0));
        this.add(cbxCountry, new GridBagConstraints(1, 0,3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 0, 3), 1, 1));
        this.add(lblAddressLine1, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 6), 0, 0));
        this.add(txtAddressLine1, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 0, 0));
        this.add(lblAddressLine2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 0), 0, 0));
        this.add(txtAddressLine2, new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 1, 1));
        this.add(lblZipCode, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 0), 1, 1));
        this.add(txtZipCode, new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 0, 0));
        this.add(lblCity, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 0), 0, 0));
        this.add(txtCity, new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), -1000, 0));
        this.add(lblState, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 3), 0, 0));
        this.add(txtState, new GridBagConstraints(1, 5, 3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 0, 0));
        this.add(lblPhone1, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 0), 1, 0));
        this.add(txtPhone1, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 0), 0, 0));
        this.add(lblPhoneType1, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(2, 6, 0, 0), 0, 0));
        this.add(cbxPhoneType1, new GridBagConstraints(3, 7, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 0, 0));
        this.add(lblPhone2, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 0), 0, 0));
        this.add(txtPhone2, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 0), 0, 0));
        this.add(lblPhoneType2, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(2, 6, 0, 0), 0, 0));
        this.add(cbxPhoneType2, new GridBagConstraints(3, 8, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 0, 0));
        this.add(lblPhone3, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 0, 0), 0, 0));
        this.add(txtPhone3, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 0), 0, 0));
        this.add(lblPhoneType3, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(2, 6, 0, 0), 0, 0));
        this.add(cbxPhoneType3, new GridBagConstraints(3, 9, 1, 1, 1.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 0, 0));
        this.add(lblAddressType, new GridBagConstraints(0, 10, 1, 1, 0.0, 1.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 1, 3, 0), 0, 0));
        this.add(cbxAddressType, new GridBagConstraints(1, 10, 3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 3, 3), 0, 0));
        this.add(lblMessage, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0
                , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 0, 3), 0, 0));

		/**
		 * Text Verifier
		 */
		// Text limits set as per max sizes allowed over various db tables involved
		// -MSB 02/10/06
		// Since Input is free form and we just want to
		// restrict the text length we don't need to filter
		// input. This resolves double-byte character input issues.
		txtAddressLine1.setDocument(new CMSTextFilter(60));
		txtAddressLine2.setDocument(new CMSTextFilter(60));
		txtCity.setDocument(new CMSTextFilter(40));
		txtState.setDocument(new CMSTextFilter(40));
		//Fix for 1860: Broken Transaction Zip Code more than 10 digits in US reason when others selected
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
		txtZipCode.setDocument(new CMSTextFilter(10));
		}else {
			txtZipCode.setDocument(new CMSTextFilter(15));
		}
		if (nameSpec != null && nameSpec.length() > 0)
			txtPhone1.setDocument(new TextFilter(nameSpec, 15));
		else
			txtPhone1.setDocument(new CMSTextFilter(15));
		if (this.addressPanelType.trim().equalsIgnoreCase(FULL_ADDRESS_PANEL)) {
			if (nameSpec != null && nameSpec.length() > 0) {
				txtPhone2.setDocument(new TextFilter(nameSpec, 15));
				txtPhone3.setDocument(new TextFilter(nameSpec, 15));
			} else {
				txtPhone2.setDocument(new CMSTextFilter(15));
				txtPhone3.setDocument(new CMSTextFilter(15));
			}
		}
	}

	public void enableDefaultSearch() {
		setEnabled(false);
		txtCity.setEnabled(true);
		txtZipCode.setEnabled(true);
		cbxCountry.setEnabled(true);
	}

	public void reset() {
		super.reset();
		if (cbxCountry.getItemCount() > 0)
			cbxCountry.setSelectedIndex(0);
	}
	public static final String ALPHA_NUMERIC_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";
}
