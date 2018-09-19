/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.ResourceBundle;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cr.telephone.Telephone;
import java.util.Vector;
import com.chelseasystems.cr.telephone.TelephoneType;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.swing.CMSTextFilter;
import java.awt.event.ItemListener;
import com.chelseasystems.cs.tax.ArmTaxRate;
import com.chelseasystems.cs.tax.CMSTaxHelper;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cr.swing.CMSApplet;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cs.swing.dlg.CityDlgBox;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.store.Store;


/**
 * <p>Title: USAAddressPanel</p>
 *
 * <p>Description: USAAddressPanel</p>
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
 | 2    | 04-30-2005 | Khyati    | N/A       | Resizing the text fields                           |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 06-22-2005 | Vikram    | 262       | Address field sizes and intro of address Format    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 07-07-2005 | Jin       | 265       | All numeric format for phone numbers               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 07-14-2005 | Megha     | PCR       | Zip Code Verifier                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 07-22-2005 | Vikram    | 286       | Changes to support shipping to foreign countries.  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class USAAddressPanel extends BaseAddressPanel {
  // Valid USA states
  private static final Collection validStates = Arrays.asList(new String[] {"AL", "AK", "AS", "AZ"
      , "AR", "CA", "CO", "CT", "DE", "DC", "FM", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA"
      , "KS", "KY", "LA", "ME", "MH", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH"
      , "NJ", "NM", "NY", "NC", "ND", "MP", "OH", "OK", "OR", "PW", "PA", "PR", "RI", "SC", "SD"
      , "TN", "TX", "UT", "VT", "VI", "VA", "WA", "WV", "WI", "WY"
  });
  public static ResourceBundle res = null;
  /**
   * ALPHA_NUMERIC WITH SPECIAL CHARACTER.
   */
  public static final String ALPHA_NUMERIC_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";
  /*
   * NUMERIC_ONLY used for text filter; includes space for common usage.
   */
  private static final String NUMERIC_ONLY_SPEC = "-0123456789";
  // For state.
  private static final String ALPHA_ONLY_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * Default Constructor
   */
  public USAAddressPanel() {
    this(null);
  }

  /**
   * Pass Address Object
   * @param addressCurrent Customer'sAddress
   */
  public USAAddressPanel(Address addressCurrent) {
    this(addressCurrent, FULL_ADDRESS_PANEL);
  }

  /**
   * put your documentation comment here
   * @param   Address addressCurrent
   * @param   String addressPanelType
   */
  public USAAddressPanel(Address addressCurrent, String addressPanelType) {
    super(addressPanelType);
    //RESOURCE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    try {
      jbInit();
      this.addressCurrent = addressCurrent;
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
      addressCurrent.setAddressFormat(addressFormat);
      addressCurrent.setAddressLine1(getAddressLine1());
      addressCurrent.setAddressLine2(getAddressLine2());
      if (!validateCityStateZipCode()) {
        txtCity.setText("");
        txtState.setText("");
        txtZipCode.setText("");
        txtZipCode.requestFocus();
        return null;
      }
      addressCurrent.setCity(getCity());
      addressCurrent.setState(getState());
      addressCurrent.setZipCode(getZipCode());
      addressCurrent.setAddressType(getSelectedAddressType());
      addressCurrent.setCountry(addressFormat);
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
          // MSB -01/12/2006
//        addressCurrent.setPrimaryPhone(new Telephone(new TelephoneType(getSelectedPhoneType1())
//              , "", "", getPhone1()));

        }
        if (getPhone2().length() > 0) {
          Telephone tele = new Telephone(new TelephoneType(
              getSelectedPhoneType2()) , "", "", getPhone2());
          tele = tele.newTelephoneNumber(getPhone2());
          addressCurrent.setSecondaryPhone(tele);
          // MSB -01/12/2006
//          addressCurrent.setSecondaryPhone(new Telephone(new TelephoneType(getSelectedPhoneType2())
//              , "", "", getPhone2()));
        }
        if (getPhone3().length() > 0) {
          Telephone tele = new Telephone(new TelephoneType(
              getSelectedPhoneType3()) , "", "", getPhone3());
          tele = tele.newTelephoneNumber(getPhone3());
          addressCurrent.setTernaryPhone(tele);
          // MSB -01/12/2006
//          addressCurrent.setTernaryPhone(new Telephone(new TelephoneType(getSelectedPhoneType3())
//              , "", "", getPhone3()));
        }
      } catch (Exception e) {}
      return addressCurrent;
    } catch (Exception e) {}
    return null;
  }

  /**
   * CustomerAddress
   * @param address CustomerAddress
   */
  public void setCustomerAddress(Address address) {
    reset();
    addressCurrent = address;
    setPrimary(address.isUseAsPrimary());
    setAddressLine1(address.getAddressLine1());
    setAddressLine2(address.getAddressLine2());
    // Before Setting city, state & zipCode need to know if their combo is correct
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
    return txtAddressLine1.getText().trim();
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
    return txtAddressLine2.getText().trim();
  }

  /**
   * Set City
   * @param sValue City
   */
  public void setCity(String sValue) {
    //      if(sValue == null || sValue.length() <1) return;
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
    //      if(sValue == null || sValue.length() <1) return;
    txtState.setText(sValue);
  }

  /**
   * Get State
   * @return State
   */
  public String getState() {
    return txtState.getText().trim();
  }

  /**
   * put your documentation comment here
   * @return
   * @exception BusinessRuleException
   */
  public boolean isStateValid()
      throws BusinessRuleException {
    String state = txtState.getText().trim().toUpperCase();
    if (state.length() < 1)
      return true;
    //txtState.requestFocus();
    if (validStates.contains(state))
      return true;
    else {
      txtState.setText("");
      txtState.requestFocus();
      throw new BusinessRuleException(res.getString(
          "Address is not valid. Check state abbreviation."));
    }
  }

  /**
   * put your documentation comment here
   * @return
   * @exception BusinessRuleException
   */
  public boolean isAllScreenInputValid()
      throws BusinessRuleException {
    return super.isAllScreenInputValid() && validateCityStateZipCode();
  }

  /**
   * Set ZipCode
   * @param sValue ZipCode
   */
  public void setZipCode(String sValue) {
    //      if(sValue == null || sValue.length() <1) return;
    txtZipCode.setText(sValue);
  }

  /**
   * Get ZipCode
   * @return ZipCode
   */
  public String getZipCode() {
    return txtZipCode.getText().trim();
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
    return txtPhone1.getText().trim();
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
    return txtPhone2.getText().trim();
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
    return txtPhone3.getText().trim();
  }

  /**
   * Set PhoneType1
   * @param sValue PhoneType1
   */
  public void setSelectedPhoneType1(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    cbxPhoneType1.setSelectedIndex(selectPhoneKey(sValue));
    cbxPhoneType1.repaint();
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
   * Initialize Components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    res = ResourceManager.getResourceBundle();
    initAddressPanel();
    //      lblAddressLine1.setLabelFor(txtAddressLine1);
    //      lblAddressLine1.setText(RESOURCE.getString("AddressLine1"));
    //      lblAddressLine2.setText(RESOURCE.getString("AddressLine2"));
    //      lblZipCode.setText(RESOURCE.getString("Zip Code"));
    //      lblCity.setText(RESOURCE.getString("City"));
    //      lblState.setText(RESOURCE.getString("State"));
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    this.setLayout(gridBagLayout1);
    this.add(lblAddressLine1
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 2, 0, 0), 17, 2));
    this.add(lblAddressLine2
        , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 0, 25), 15, 2));
    this.add(lblZipCode
        , new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 0, 28), 67, 0));
    this.add(lblState
        , new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 15, 0, 1), 19, 0));
    this.add(txtState
        , new GridBagConstraints(4, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 15, 0, 22), 6, -2));
    this.add(lblPhone2
        , new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 5, 0));
    this.add(lblPhone3
        , new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 5, 0));
    this.add(lblPhone1
        , new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 2, 0, 0), 5, 0));
    this.add(cbxAddressType
        , new GridBagConstraints(2, 9, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(2, 9, 8, 1), 106, 1));
    this.add(lblAddressType
        , new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 2, 8, 0), 40, 0));
    this.add(txtAddressLine2
        , new GridBagConstraints(0, 3, 5, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, -3));
    this.add(txtAddressLine1
        , new GridBagConstraints(0, 1, 5, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 1, -3));
    this.add(txtCity
        , new GridBagConstraints(3, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 18, 0, 0), 104, -3));
    this.add(txtZipCode
        , new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 2, 0, -95), 72, -3));
    this.add(txtPhone1
        , new GridBagConstraints(1, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, -55, 0, -45), 3, -3));
    this.add(cbxPhoneType2
        , new GridBagConstraints(3, 7, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(2, 100, 0, 1), -15, 1));
    this.add(cbxPhoneType3
        , new GridBagConstraints(3, 8, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(2, 100, 0, 1), -15, 1));
    this.add(txtPhone2
        , new GridBagConstraints(1, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, -55, 0, -45), 3, -3));
    this.add(txtPhone3
        , new GridBagConstraints(1, 8, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(2, -55, 0, -45), 3, -3));
    this.add(cbxPhoneType1
        , new GridBagConstraints(3, 6, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.HORIZONTAL, new Insets(2, 100, 0, 1), -15, 1));
    this.add(lblPhoneType1
        , new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(2, 70, 0, 0), 16, 0));
    this.add(lblPhoneType2
        , new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 70, 0, 0), 19, 0));
    this.add(lblPhoneType3
        , new GridBagConstraints(3, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 70, 0, 0), 20, 0));
    this.add(lblCity
        , new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(0, 16, 0, 84), 11, 0));
    JPanel bufferPanel = new JPanel();
    this.add(bufferPanel
        , new GridBagConstraints(0, 20, 0, GridBagConstraints.REMAINDER, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    gridBagLayout1.layoutContainer(this);
    // Set weights of all columns and rows to 1
    double[][] weights = gridBagLayout1.getLayoutWeights();
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < weights[i].length; j++) {
        weights[i][j] = 1;
      }
    }
    gridBagLayout1.columnWeights = weights[0];
    gridBagLayout1.rowWeights = weights[1];
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
//    txtAddressLine1.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 40));
//    txtAddressLine2.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 30));
    //txtZipCode.setVerifier(new ZipVerifier());
    txtZipCode.setDocument(new TextFilter(TextFilter.NUMERIC, 5));
    txtPhone1.setDocument(new TextFilter(NUMERIC_ONLY_SPEC, 15));
//    txtState.setDocument(new TextFilter(ALPHA_ONLY_SPEC, 2));
    if (this.addressPanelType.trim().equalsIgnoreCase(FULL_ADDRESS_PANEL)) {
      txtPhone2.setDocument(new TextFilter(NUMERIC_ONLY_SPEC, 15));
      txtPhone3.setDocument(new TextFilter(NUMERIC_ONLY_SPEC, 15));
    }
    //      else if(this.addressPanelType.trim().equalsIgnoreCase(SHIPPING_ADDRESS_PANEL))
    //      {
    //
    //      }
    txtState.addFocusListener(new FocusAdapter() {

      /**
       * put your documentation comment here
       * @param evt
       */
      public void focusLost(FocusEvent evt) {
        String State = txtState.getText();
        String value = (String)CMSApplet.theAppMgr.getStateObject("CUST_MGMT_MODE");
        if (State.trim().length() < 2 && State.trim().length() != 0) {
          CMSApplet.theAppMgr.showErrorDlg(" The State is of length less than 2");
          SwingUtilities.invokeLater(new Runnable() {

            /**
             * put your documentation comment here
             */
            public void run() {
              SwingUtilities.invokeLater(new Runnable() {

                /**
                 * put your documentation comment here
                 */
                public void run() {
                  txtState.requestFocus();
                }
              });
            }
          });
          txtState.setText("");
        }
      }
    });
    txtZipCode.addFocusListener(new FocusAdapter() {

      /**
       * put your documentation comment here
       * @param evt
       */
      public void focusLost(FocusEvent evt) {
        String zipCode = txtZipCode.getText();
        try {
          ArmTaxRate armState[] = validateZipCode(zipCode);
          if (txtZipCode.getText() == null
              || (txtZipCode.getText().length() < 5 && txtZipCode.getText().length() != 0)) {
            CMSApplet.theAppMgr.showErrorDlg(" The zip code is of length less than 5");
            txtZipCode.requestFocus();
            txtZipCode.setText("");
          } else if (armState != null && armState.length <= 0 && txtZipCode.getText().length() != 0) {
            SwingUtilities.invokeLater(new Runnable() {

              /**
               * put your documentation comment here
               */
              public void run() {
                SwingUtilities.invokeLater(new Runnable() {

                  /**
                   * put your documentation comment here
                   */
                  public void run() {
                    txtZipCode.requestFocus();
                  }
                });
              }
            });
            txtState.setText("");
            txtCity.setText("");
            txtZipCode.setText("");
            txtZipCode.requestFocus();
            CMSApplet.theAppMgr.showErrorDlg("The zip code is incorrect.");
          } else if (armState != null && armState.length > 0 && txtZipCode.getText().length() != 0) {
            populateCityAndState(armState);
          }
          setZipCode(txtZipCode.getText());
        } catch (Exception bx) {
          SwingUtilities.invokeLater(new Runnable() {

            /**
             * put your documentation comment here
             */
            public void run() {
              SwingUtilities.invokeLater(new Runnable() {

                /**
                 * put your documentation comment here
                 */
                public void run() {
                  txtZipCode.requestFocus();
                }
              });
            }
          });
          CMSApplet.theAppMgr.showErrorDlg(bx.getMessage());
          return;
        }
        return ;
      } // FocusLost Ends
    });
  }

  /**
   * Validates Zip Code : MP
   * @param zipCode String
   * @return ArmTaxRate[]
   */
  ArmTaxRate[] validateZipCode(String zipCode) {
    try {
      Store store = (Store)CMSApplet.theAppMgr.getGlobalObject("STORE");
      if (!("US".equalsIgnoreCase(Version.CURRENT_REGION)))
        return null;
      if (zipCode != null) {
        ArmTaxRate armTaxRate[] = CMSTaxHelper.findByZipCode(CMSApplet.theAppMgr, zipCode);
        return armTaxRate;
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  /**
   * Populates the state & the city : MP
   * @param armTaxRate ArmTaxRate[]
   */
  void populateCityAndState(ArmTaxRate armTaxRate[]) {
    ArmTaxRate val = loadCityDlg(armTaxRate);
    String city = null;
    if (val != null)
      city = val.getCity();
    if (city != null) {
      txtState.setText(armTaxRate[0].getState());
      txtCity.setText(city);
    } else
      txtZipCode.requestFocus();
    return;
  }

  /**
   * put your documentation comment here
   */
  void requestFocusOnState() {
    txtState.requestFocus();
  }

  /**
   * put your documentation comment here
   * @param armTaxRate
   * @return
   */
  public ArmTaxRate loadCityDlg(ArmTaxRate[] armTaxRate) {
    GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[armTaxRate.length];
    for (int i = 0; i < availMiscItemTemplates.length; i++) {
      availMiscItemTemplates[i] = new GenericChooserRow(new String[] {armTaxRate[i].getCity()
      }, armTaxRate[i]);
    }
    CityDlgBox dlg = new CityDlgBox(CMSApplet.theAppMgr.getParentFrame(), CMSApplet.theAppMgr
        , availMiscItemTemplates, new String[] {(res.getString("CITIES"))
    });
    dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(CityDlgBox.getCenterRenderer());
    dlg.setVisible(true);
    if (dlg.isOK()) {
      return (ArmTaxRate)((ArmTaxRate)dlg.getSelectedRow().getRowKeyData());
    } else
      return (null);
  }

  /**
   * put your documentation comment here
   * @return
   * @exception MissingResourceException, BusinessRuleException
   */
  public boolean validateCityStateZipCode()
      throws MissingResourceException, BusinessRuleException {
    Store store = (Store)CMSApplet.theAppMgr.getGlobalObject("STORE");
    if (!("US".equalsIgnoreCase(Version.CURRENT_REGION)))
      return true;
    // Before setting the address need to check if the zipCode + City + State are correct.
    // This is used to calculate the Tax Rate.
    ArmTaxRate armState[] = validateZipCode(txtZipCode.getText());
    boolean allFieldsClear = txtState.getText().length() == 0 && txtCity.getText().length() == 0
        && txtZipCode.getText().length() == 0;
    boolean allFieldsSet = txtState.getText().length() > 0 && txtCity.getText().length() > 0
        && txtZipCode.getText().length() > 0;
    if (allFieldsClear)
      return true;
    // For offline Mode
    //        if ((armState == null) && isValidState() && !allFieldsSet)
    //        {
    //            return false;
    //        }
    //        else if ((armState == null) && !isValidState())
    //        {
    //            return false;
    //        }
    //        else if ((armState == null) && isValidState())
    //        {
    //            return true;
    //        }
    try {
      if ((armState == null) && isStateValid()) {
        if (allFieldsSet)
          return true;
        //else
        return false;
      }
    } catch (BusinessRuleException ex) {
      return false;
    }
    String city = txtCity.getText();
    String state = txtState.getText();
    boolean correctCity = false;
    boolean correctState = false;
    if (armState != null && armState.length > 0) {
      // Check to see if the city & the state are correct.
      for (int i = 0; i < armState.length; i++) {
        if (city.equalsIgnoreCase(armState[i].getCity())) {
          correctCity = true;
        }
      }
      if (state.equalsIgnoreCase(armState[0].getState()))
        correctState = true;
      if (correctCity && correctState) {
        return true;
      } else {
        txtZipCode.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtZipCode.requestFocus();
        throw new BusinessRuleException(res.getString("Incorrect Zip, City and State combination"));
      }
    } else {
      txtZipCode.setText("");
      txtCity.setText("");
      txtState.setText("");
      txtZipCode.requestFocus();
      throw new BusinessRuleException(res.getString("The zip code is incorrect."));
    }
    //return false;
  }
}

