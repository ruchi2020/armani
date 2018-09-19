/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import java.awt.*;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import java.awt.event.ItemListener;
import com.chelseasystems.cs.address.Address;
import java.util.Vector;
import com.chelseasystems.cr.telephone.TelephoneType;
import com.chelseasystems.cr.telephone.Telephone;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import com.chelseasystems.cr.rules.BusinessRuleException;


/**
 * <p>Title: CanadaAddressPanel</p>
 *
 * <p>Description: CanadaAddressPanel</p>
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
 */
public class CANAddressPanel extends BaseAddressPanel {
  /**
   * Directional
   */
  private JCMSLabel lblDirectional;
  /**
   * Directional Value
   */
  private JCMSTextField txtDirectional;

  /**
   * Default Constructor
   */
  public CANAddressPanel() {
    this(null);
  }

  /**
   * Pass Address Object
   * @param addressCurrent Customer'sAddress
   */
  public CANAddressPanel(Address addressCurrent) {
    this(addressCurrent, FULL_ADDRESS_PANEL);
  }

  /**
   * put your documentation comment here
   * @param   Address addressCurrent
   * @param   String addressPanelType
   */
  public CANAddressPanel(Address addressCurrent, String addressPanelType) {
    super(addressPanelType);
    try {
      jbInit();
      this.addressCurrent = addressCurrent;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void resize() {}

  /**
   * put your documentation comment here
   * @return
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
      addressCurrent.setDirectional(getDirectional());
      addressCurrent.setAddressFormat(addressFormat);
      //Fix for 1908 Customer Management - If you add address and select OTHER as country, you cannot go back and modify it later.
      addressCurrent.setCountry(addressFormat);
      /*
       Bug #96
       When entering customers with country other than USA  i.e Canada or
       Japan tel # is not assigned properly
       */
      try {
        // SP: Modified the code to check if the phone number is not empty and
        // construct the telephone object with no area code and country code.

        // MSB - 01/12/2006
        // Telephone object filters digits for telephone number
        // internally, so this removes any special characters
        // that were in input.
        // In order to make sure, telephone number is free format
        //  newTelephoneNumber() is used. It doesnt filter digits.
        if (getPhone1().length() > 0) {

          Telephone tele = new Telephone(new TelephoneType(
              getSelectedPhoneType1()) , "", "", getPhone1());
          tele = tele.newTelephoneNumber(getPhone1());
          addressCurrent.setPrimaryPhone(tele);
        }
        if (getPhone2().length() > 0) {
          Telephone tele = new Telephone(new TelephoneType(
              getSelectedPhoneType2()) , "", "", getPhone2());
          tele = tele.newTelephoneNumber(getPhone2());
          addressCurrent.setSecondaryPhone(tele);
        }
        if (getPhone3().length() > 0) {
          Telephone tele = new Telephone(new TelephoneType(
              getSelectedPhoneType3()) , "", "", getPhone3());
          tele = tele.newTelephoneNumber(getPhone3());
          addressCurrent.setTernaryPhone(tele);
        }
      } catch (Exception e) {}
      return addressCurrent;
    } catch (Exception e) {}
    return null;
  }

  /**
   * put your documentation comment here
   * @param address
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
    } catch (Exception e) {}
    setSelectedAddressType(address.getAddressType());
    setDirectional(address.getDirectional());
  }

  /**
   * put your documentation comment here
   * @param bEnabled
   */
  public void setEnabled(boolean bEnabled) {
    super.setEnabled(bEnabled);
    txtDirectional.setEnabled(bEnabled);
  }

  /**
   * put your documentation comment here
   */
  public void reset() {
    super.reset();
    txtDirectional.setText("");
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
   * @param bPrimary true/false
   */
  public void setPrimary(boolean bPrimary) {
    this.bIsPrimary = bPrimary;
  }

  /**
   * Set AddressLine1
   * @param sValue AddressLine1
   */
  public void setAddressLine1(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtAddressLine1.setText(sValue);
  }

  /**
   * Get AddressLine1
   * @return AddressLine1
   */
  public String getAddressLine1() {
    return txtAddressLine1.getText();
  }

  /**
   * Set AddressLine2
   * @param sValue AddressLine2
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
   * @param sValue City
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
   * @param sValue State
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
   * @param sValue ZipCode
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
   * @param sValue Phone1
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
   * @param sValue Phone2
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
   * @param sValue Phone1
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
   * @param sValue PhoneType1
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
   * @param sValue PhoneType2
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
   * @param sValue PhoneType3
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
   * @param sValue AddressType
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
    return (String)vecAddressTypeKeys.elementAt(cbxAddressType.getSelectedIndex());
  }

  /**
   * Set AddressTypes
   * @param vecAddressTypes AddressTypes
   */
  public void setAddressTypes(Vector vecAddressTypes, Vector vecAddressKeys) {
    cbxAddressType.setModel(new DefaultComboBoxModel(vecAddressTypes));
    vecAddressTypeKeys = vecAddressKeys;
  }

  /**
   * Set Directional
   * @param sValue Directional
   */
  public void setDirectional(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    txtDirectional.setText(sValue);
  }

  /**
   * Get Directional
   * @return Directional
   */
  public String getDirectional() {
    return txtDirectional.getText();
  }

  /**
   * Set AddressTypes
   * @param vecAddressTypes AddressTypes
   */
  public void setAddressTypes(Vector vecAddressTypes) {
    cbxAddressType.setModel(new DefaultComboBoxModel(vecAddressTypes));
  }

  /**
   * Set PhoneTypes
   * @param vecPhoneTypes PhoneTypes
   */
  public void setPhoneTypes(Vector vecPhoneTypes, Vector vecPhoneTypeKeys) {
    this.vecPhoneTypeKeys = vecPhoneTypeKeys;
    cbxPhoneType1.setModel(new DefaultComboBoxModel(vecPhoneTypes));
    cbxPhoneType2.setModel(new DefaultComboBoxModel(vecPhoneTypes));
    cbxPhoneType3.setModel(new DefaultComboBoxModel(vecPhoneTypes));
  }

  /**
   * put your documentation comment here
   * @param iIndex
   * @return
   */
  private String getPhoneKeyAt(int iIndex) {
    if (iIndex < 0 || iIndex >= vecPhoneTypeKeys.size())
      return null;
    return (String)vecPhoneTypeKeys.elementAt(iIndex);
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
   * put your documentation comment here
   * @param shippingRequest
   * @return
   * @exception BusinessRuleException
   */
  public CMSShippingRequest populateShippingRequest(CMSShippingRequest shippingRequest)
      throws BusinessRuleException {
    super.populateShippingRequest(shippingRequest);
    shippingRequest.setAddress(getAddressLine2()); //Street Name
    shippingRequest.setAddress2(getDirectional()); //Directional
    shippingRequest.setApartment(getAddressLine1()); //Unit
    return shippingRequest;
  }

  /**
   * put your documentation comment here
   * @param shippingRequest
   */
  public void setShippingRequest(CMSShippingRequest shippingRequest) {
    super.setShippingRequest(shippingRequest);
    setAddressLine1(shippingRequest.getApartment());
    setAddressLine2(shippingRequest.getAddress());
    setDirectional(shippingRequest.getAddress2());
  }

  /**
   * put your documentation comment here
   * @return
   * @exception BusinessRuleException
   */
  public boolean isAddressLine1Valid()
      throws BusinessRuleException {
    try {
      return super.isAddressLine1Valid();
    } catch (BusinessRuleException e) {
      throw new BusinessRuleException(RESOURCE.getString("Unit: This field is required."));
    }
  }

  /**
   * put your documentation comment here
   * @return
   * @exception BusinessRuleException
   */
  public boolean isAddressLine2Valid()
      throws BusinessRuleException {
    if (!addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL)
        || txtAddressLine2.getText().trim().length() > 0)
      return true;
    //else
    txtAddressLine2.requestFocus();
    throw new BusinessRuleException(RESOURCE.getString("Street Name: This field is required."));
  }

  /**
   * put your documentation comment here
   * @return
   * @exception BusinessRuleException
   */
  public boolean isStateValid()
      throws BusinessRuleException {
    try {
      return super.isStateValid();
    } catch (BusinessRuleException e) {
      throw new BusinessRuleException(RESOURCE.getString("Province: This field is required."));
    }
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

  /**
   * Initialize and Layout Components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    initAddressPanel();
    lblDirectional = new JCMSLabel();
    txtDirectional = new JCMSTextField();
    lblAddressLine1.setText(RESOURCE.getString("Unit No, Street No, Suffix"));
    lblAddressLine2.setText(RESOURCE.getString("Street Name"));
    lblCity.setText(RESOURCE.getString("City"));
    lblState.setText(RESOURCE.getString("Province"));
    lblZipCode.setText(RESOURCE.getString("Post Code"));
    lblDirectional.setText(RESOURCE.getString("Directional"));
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    this.setLayout(gridBagLayout1);
    //this.setPreferredSize(new Dimension(344, 400));
    this.add(lblState
        , new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 22, 0, 0), 20, 0));
    this.add(txtState
        , new GridBagConstraints(2, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 22, 0, 0), 39, 1));
    this.add(lblZipCode
        , new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 38, 0, 5), 17, 0));
    this.add(txtDirectional
        , new GridBagConstraints(3, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 5), 75, 1));
    this.add(lblAddressLine2
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 1, 0, 34), 12, 0));
    this.add(lblDirectional
        , new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 20, 0, 5), 25, 0));
    this.add(cbxPhoneType1
        , new GridBagConstraints(2, 7, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(0, 31, 0, 5), 21, -2));
    this.add(lblPhoneType2
        , new GridBagConstraints(2, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 31, 0, 5), 81, 0));
    this.add(cbxPhoneType2
        , new GridBagConstraints(2, 9, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(0, 31, 0, 5), 22, -2));
    this.add(lblPhoneType3
        , new GridBagConstraints(2, 10, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 31, 0, 5), 121, 0));
    this.add(lblPhoneType1
        , new GridBagConstraints(2, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 31, 0, 5), 78, 0));
    this.add(cbxPhoneType3
        , new GridBagConstraints(2, 11, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(2, 32, 0, 5), 21, -2));
    this.add(lblAddressLine1
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(1, 2, 0, 0), 5, 0));
    this.add(txtAddressLine1
        , new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 20), 214, 1));
    this.add(lblCity
        , new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 2, 29), 65, 0));
    this.add(txtCity
        , new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 163, 1));
    this.add(lblPhone1
        , new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 2, 0, 66), 2, 0));
    this.add(txtPhone1
        , new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 0), 162, 1));
    this.add(lblPhone2
        , new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 2, 0, 67), 2, 0));
    this.add(txtPhone2
        , new GridBagConstraints(0, 9, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 0), 162, 1));
    this.add(lblPhone3
        , new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 2, 0, 65), 4, 0));
    this.add(txtPhone3
        , new GridBagConstraints(0, 11, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 0), 162, 1));
    this.add(lblAddressType
        , new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 2, 0, 0), 40, 0));
    this.add(cbxAddressType
        , new GridBagConstraints(1, 12, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 5), 101, -2));
    this.add(txtAddressLine2
        , new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 0), 200, 1));
    this.add(txtZipCode
        , new GridBagConstraints(3, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 38, 0, 5), 57, 1));
    JPanel bufferPanel = new JPanel();
    this.add(bufferPanel
        , new GridBagConstraints(0, 13, 0, GridBagConstraints.REMAINDER, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    /**
     * Text Verifier
     */
    //Text limits set as per max sizes allowed over various db tables involved
    //-MSB 02/10/06
    // Since Input is free form and we just want to
    // restrict the text length we don't need to filter
    // input. This resolves double-byte character input issues.
    txtAddressLine1.setDocument(new CMSTextFilter(40));
    txtAddressLine2.setDocument(new CMSTextFilter(30));
    txtCity.setDocument(new CMSTextFilter(30));
    txtState.setDocument(new CMSTextFilter(30));
    txtZipCode.setDocument(new CMSTextFilter(10));
    if (nameSpec != null && nameSpec.length() > 0)
    	txtPhone1.setDocument(new TextFilter(nameSpec,15));
    else
    	txtPhone1.setDocument(new CMSTextFilter(15));
    if (this.addressPanelType.trim().equalsIgnoreCase(FULL_ADDRESS_PANEL)) {
    	if (nameSpec != null && nameSpec.length() > 0) {
    		txtPhone2.setDocument(new TextFilter(nameSpec,15));
    		txtPhone3.setDocument(new TextFilter(nameSpec,15));
    	} else {
    		txtPhone2.setDocument(new CMSTextFilter(15));
    		txtPhone3.setDocument(new CMSTextFilter(15));
    	}
    }
  }

  public static final String ALPHA_NUMERIC_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";
}
