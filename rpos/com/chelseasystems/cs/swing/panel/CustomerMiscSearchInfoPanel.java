/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;

import java.awt.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.bean.*;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.*;
import com.chelseasystems.cr.config.ConfigMgr;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.StringTokenizer;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.util.ArmConfigLoader;
import com.chelseasystems.cs.util.CustomerUtil;


/**
 * <p>Title:CustomerMiscSearchInfoPanel </p>
 *
 * <p>Description: Holds Misc. info for customer</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Paresh
 * @version 1.0
 */
public class CustomerMiscSearchInfoPanel extends JPanel {
  /**
   * Gender Label
   */
  private JCMSLabel lblGender;


  /**
   * Gender Combo box
   */
  public JCMSComboBox cmboxGender;


  /**
   * Customer Type
   */
  private JCMSLabel lblCustType;


  /**
   * CustomerType Combobox
   */
  private JCMSComboBox cmboxCustType;


  private ConfigMgr config;


  /**
   * ALPHA_NUMERIC WITH SPECIAL CHARACTER.
   */
  public static final String ALPHA_NUMERIC_SPEC =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";


  /**
   * Resource
   */
  private final ResourceBundle RESOURCE;
  private Vector vecGenderKeys, vecGenderLabel;
  private Vector vecCustTypeKeys, vecCustTypeLabel;
  private Vector vecPrivacyKeys;
  private Vector vecPrivacyLabels;
  private static String types[];
  ArmConfigLoader configMgr;
  private JCMSLabel lblVIPValue;
  private JCMSLabel lblPrivacy;
  private JCMSComboBox cmbxPrivacy;
  private GridBagLayout gridBagLayout1;
  /**
   * Age
   */
  private JCMSLabel lblAge;


  /**
   * Selected age
   */
  public JCMSComboBox cmbxAge;


  /**
   * Default constructor
   */
  public CustomerMiscSearchInfoPanel() {
    //configMgr = new ConfigMgr("customer.cfg");  -- MSB 09/01/2005
//    configMgr = new ConfigMgr("ArmaniCommon.cfg");
    configMgr = new ArmConfigLoader();
    vecGenderKeys = new Vector();
    vecGenderLabel = new Vector();
    vecPrivacyKeys = new Vector();
    vecPrivacyLabels = new Vector();
    vecCustTypeKeys = new Vector();
    vecCustTypeLabel = new Vector();
    RESOURCE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    try {
      jbInit();
   //   cmbxAge.setModel(new DefaultComboBoxModel(CustomerUtil.AGE_RANGE));
      cmbxAge.setModel(new DefaultComboBoxModel(CustomerUtil.AGE_RANGE_DEFAULT));
      setEnabled(false);
      populateGender();
      populateCustomerType();
      populatePrivacyLabels();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * put your documentation comment here
   */
  private void populateGender() {
    try {
      //      String sTmpKeys = configMgr.getString("GENDER_TYPES");
      //      String sTmpValues = configMgr.getString("GENDER.VALUES");
      //      StringTokenizer sTokensKeys = new StringTokenizer(sTmpKeys,",");
      //      StringTokenizer sTokensValues = new StringTokenizer(sTmpValues,",");
      //      while(sTokensKeys.hasMoreTokens())
      //      {
      //        sTmpKeys = sTokensKeys.nextToken();
      //        sTmpValues = sTokensValues.nextToken();
      //        vecGenderKeys.addElement(sTmpKeys);
      //        vecGenderValues.addElement(sTmpValues);
      //      }
      //          String strSubTypes = configMgr.getString("GENDER_TYPES"); -- MSB 09/01/2005
      String strSubTypes = configMgr.getString("GENDER");
      StringTokenizer stk = null;
      int i = -1;
      if (strSubTypes != null && strSubTypes.trim().length() > 0) {
        stk = new StringTokenizer(strSubTypes, ",");
      } else
        return;
      if (stk != null) {
        types = new String[stk.countTokens()];
        while (stk.hasMoreTokens()) {
          types[++i] = stk.nextToken();
          String key = configMgr.getString(types[i] + ".CODE");
          vecGenderKeys.add(key);
          String value = configMgr.getString(types[i] + ".LABEL");
          vecGenderLabel.add(value);
        }
      }
      cmboxGender.setModel(new DefaultComboBoxModel(vecGenderLabel));
    } catch (Exception e) {}
  }


  /**
   * put your documentation comment here
   */
  private void populateCustomerType() {
    try {
      //      String sTmpKeys = configMgr.getString("CUST_TYPE.KEYS");
      //      String sTmpValues = configMgr.getString("CUST_TYPE.VALUES");
      //      StringTokenizer sTokensKeys = new StringTokenizer(sTmpKeys,",");
      //      StringTokenizer sTokensValues = new StringTokenizer(sTmpValues,",");
      //      while(sTokensKeys.hasMoreTokens())
      //      {
      //        sTmpKeys = sTokensKeys.nextToken();
      //        sTmpValues = sTokensValues.nextToken();
      //        vecCustTypeKeys.addElement(sTmpKeys);
      //        vecCustTypeValues.addElement(sTmpValues);
      //      }
      //      String strSubTypes = configMgr.getString("CUST_TYPES"); -- MSB 09/01/05
      String strSubTypes = configMgr.getString("CUSTOMER_TYPE");
      StringTokenizer stk = null;
      int i = -1;
      if (strSubTypes != null && strSubTypes.trim().length() > 0) {
        stk = new StringTokenizer(strSubTypes, ",");
      } else
        return;
      if (stk != null) {
        types = new String[stk.countTokens()];
        while (stk.hasMoreTokens()) {
          types[++i] = stk.nextToken();
          String key = configMgr.getString(types[i] + ".CODE");
          vecCustTypeKeys.add(key);
          String value = configMgr.getString(types[i] + ".LABEL");
          vecCustTypeLabel.add(value);
        }
      }
      cmboxCustType.setModel(new DefaultComboBoxModel(vecCustTypeLabel));
    } catch (Exception e) {}
  }

  private void populatePrivacyLabels() {
    try {
      String strSubTypes = configMgr.getString("PRIVACY_CD");
      StringTokenizer stk = null;
      int i = -1;
      if (strSubTypes != null && strSubTypes.trim().length() > 0) {
        stk = new StringTokenizer(strSubTypes, ",");
      } else
        return;
      if (stk != null) {
        types = new String[stk.countTokens()];
        while (stk.hasMoreTokens()) {
          types[++i] = stk.nextToken();
          String key = configMgr.getString(types[i] + ".CODE");
          vecPrivacyKeys.add(key);
          String value = configMgr.getString(types[i] + ".LABEL");
          vecPrivacyLabels.add(value);
        }
      }
      cmbxPrivacy.setModel(new DefaultComboBoxModel(vecPrivacyLabels));
    } catch (Exception e) {}

  }



  public void setPrivacy(String sValue) {
    if (sValue == null || sValue.length() < 1 ||
        vecPrivacyKeys.indexOf(sValue) == -1)
      return;
    cmbxPrivacy.setSelectedIndex(vecPrivacyKeys.indexOf(sValue));
  }

  public String getPrivacy() {
    if (cmbxPrivacy.getSelectedIndex() == -1)return null;
    return (String)vecPrivacyKeys.elementAt(cmbxPrivacy.getSelectedIndex());
  }


  /**
   * Get VIP Percentage
   * @return VIPPercentage
   */
  public String getVIPPercentage() {
    return lblVIPValue.getText();
  }


  /**
   * Set VIP percentage
   * @param sValue Percentage
   */
  public void setVIPPercentage(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    lblVIPValue.setText(sValue);
    lblVIPValue.setVisible(true);
  }


  /**
   * Set Customer gender
   * @param sValue Gender
   */
  public void setSelectedGender(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    cmboxGender.setSelectedIndex(vecGenderKeys.indexOf(sValue));
  }


  /**
   * Get Customer gender
   * @return Gender
   */
  public String getSelectedGender() {
    if (cmboxGender.getSelectedIndex() >= 0)
      return (String)vecGenderKeys.elementAt(cmboxGender.getSelectedIndex());
    return null;
    // return (String) cmboxGender.getSelectedItem();
  }


  /**
   * Select Customer Type
   * @param sValue CustomerType
   */
  public void setSelectedCustomerType(String sValue) {
    if (sValue == null || sValue.length() < 1 || vecCustTypeKeys.indexOf(sValue) == -1)
      return;
    cmboxCustType.setSelectedIndex(vecCustTypeKeys.indexOf(sValue));
  }


  /**
   * Get customer Type.
   * @return CustomerType
   */
  public String getSelectedCustomerType() {
    if (cmboxCustType.getSelectedIndex() == -1)return null;
    return (String)vecCustTypeKeys.elementAt(cmboxCustType.getSelectedIndex());
  }



  /**
   * Set Application Manager
   * @param theAppMgr ApplicationManager
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    this.setBackground(theAppMgr.getBackgroundColor());
    //Loop through and set Application managers for all
    // JCMS components.
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    Font labelFont = theAppMgr.getTheme().getLabelFont();
    Font textFont = theAppMgr.getTheme().getTextFieldFont();
    for (int iCtr = 0; iCtr < this.getComponentCount(); iCtr++) {
      Component component = this.getComponent(iCtr);
      if (component instanceof JCMSTextField) {
        ((JCMSTextField)component).setAppMgr(theAppMgr);
        ((JCMSTextField)component).setFont(textFont);
        ((JCMSTextField)component).setPreferredSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSTextField)component).setMaximumSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSTextField)component).setMinimumSize(new Dimension((int)(65 * r), (int)(25 * r)));
      } else if (component instanceof JCMSTextArea) {
        ((JCMSTextArea)component).setAppMgr(theAppMgr);
      } else if (component instanceof JCMSLabel) {
        ((JCMSLabel)component).setAppMgr(theAppMgr);
        ((JCMSLabel)component).setFont(labelFont);
        ((JCMSLabel)component).setPreferredSize(new Dimension((int)(85 * r), (int)(15 * r)));
      } else if (component instanceof JCMSComboBox) {
        ((JCMSComboBox)component).setAppMgr(theAppMgr);
        ((JCMSComboBox)component).setFont(textFont);
        ((JCMSComboBox)component).setPreferredSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSComboBox)component).setMaximumSize(new Dimension((int)(65 * r), (int)(25 * r)));
        ((JCMSComboBox)component).setMinimumSize(new Dimension((int)(65 * r), (int)(25 * r)));
      } else if (component instanceof JCMSMaskedTextField) {
        ((JCMSMaskedTextField)component).setAppMgr(theAppMgr);
        ((JCMSMaskedTextField)component).setFont(textFont);
      } else if (component instanceof JCMSCheckBox) {
        ((JCMSCheckBox)component).setAppMgr(theAppMgr);
        ((JCMSCheckBox)component).setFont(labelFont);
        ((JCMSCheckBox)component).setPreferredSize(new Dimension((int)(35 * r), (int)(25 * r)));
      }
    }
    lblVIPValue.setFont(textFont);
  }


  /**
   * put your documentation comment here
   * @param bEnabled
   */
  public void setEnabled(boolean bEnabled) {
    cmboxGender.setEnabled(bEnabled);
    cmboxCustType.setEnabled(bEnabled);
//    jrbPrvcyYes.setEnabled(bEnabled);
//    jrbPrvcyNo.setEnabled(bEnabled);
    cmbxPrivacy.setEnabled(bEnabled);
    cmbxAge.setEnabled(bEnabled);
  }


  /**
   * put your documentation comment here
   */
  public void reset() {
    if (cmboxGender.getItemCount() > 0)
      cmboxGender.setSelectedIndex(0);
    if (cmboxCustType.getItemCount() > 0)
      cmboxCustType.setSelectedIndex(0);
    if (cmbxPrivacy.getItemCount() > 0)
      cmbxPrivacy.setSelectedIndex(0);
    if (cmbxAge.getItemCount() > 0)
      cmbxAge.setSelectedIndex(cmbxAge.getItemCount() - 1);
  }


  /**
   * put your documentation comment here
   */
  private void makeReadOnlyFields() {
  }


  /**
   * Initialize components and lay them out.
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    lblGender = new JCMSLabel();
    cmboxGender = new JCMSComboBox();
    lblCustType = new JCMSLabel();
    cmboxCustType = new JCMSComboBox();
    lblVIPValue = new JCMSLabel();
    lblPrivacy = new JCMSLabel();
    cmbxPrivacy = new JCMSComboBox();
    lblAge = new JCMSLabel();
    cmbxAge = new JCMSComboBox();
    gridBagLayout1 = new GridBagLayout();

    makeReadOnlyFields();

    this.setLayout(gridBagLayout1);
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Misc"));

    lblGender.setText(RESOURCE.getString("Gender"));
    lblCustType.setText(RESOURCE.getString("Customer Type"));
    lblVIPValue.setText(RESOURCE.getString("xxx"));
    lblPrivacy.setText(RESOURCE.getString("Privacy"));
    lblAge.setText(RESOURCE.getString("Age"));

    this.add(lblGender, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 1, 0, 35), 9, 0));
    this.add(cmboxGender, new GridBagConstraints(0, 1, 5, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, -1, 2, 0), 72, 1));
    this.add(lblCustType, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 1, 0, 35), 9, 0));
    this.add(cmboxCustType, new GridBagConstraints(0, 3, 5, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, -1, 2, 0), 72, 1));
    this.add(lblPrivacy, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 1, 0, 35), 9, 0));
    this.add(cmbxPrivacy, new GridBagConstraints(0, 5, 5, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, -1, 2, 0), 72, 1));
    this.add(lblAge, new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 1, 0, 35), 9, 0));
    this.add(cmbxAge, new GridBagConstraints(0, 7, 5, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, -1, 2, 0), 72, 1));
  } //end of method jbInit()

  /**
   * put your documentation comment here
   * @param date
   */
  private void setAgeRange(Date date) {
   // int ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX;
	  int ageRangeIndex = CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX_DEFAULT;
    try {
      ageRangeIndex = CustomerUtil.getAgeRangeIndex(date);
      cmbxAge.setSelectedIndex(ageRangeIndex);
      //Commented by deepika for Customer Default PCR and enabled it to true.
      cmbxAge.setEnabled(false);
      //cmbxAge.setEnabled(true);
    } catch (Exception ex) {
      //ageRangeIndex = CustomerUtil.NOT_DEFINED_INDEX;
      cmbxAge.setSelectedIndex(ageRangeIndex);
      cmbxAge.setEnabled(true);
    }
  }


  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  private int getEstimatedBirthYear()
      throws Exception {
    return Calendar.getInstance().get(Calendar.YEAR)
        - CustomerUtil.getAgeEstimateForAgeRangeIndex(cmbxAge.getSelectedIndex());
  }

  /**
   * GetDateoFBirth
   * @return DateoFBirth
   */

  public Date generateDateOfBirth()
      throws BusinessRuleException {
    String sBirthDate = "";
    int iBirthDay = 1;
    String sBirthDay = "01";
    int iBirthMonth = 1;
    String sBirthMonth = "01";
    int iBirthYear = 0;
    int iValidBirthDay = 31;
 //   if (cmbxAge.getSelectedIndex() != CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX) {
    if (cmbxAge.getSelectedIndex() != CustomerUtil.AGE_RANGE_NOT_DEFINED_INDEX_DEFAULT) {
      try {
        iBirthYear = getEstimatedBirthYear();
      } catch (Exception ex) {
        throw new BusinessRuleException(
            "Invalid Age Range Estimate Value encountered");
      }
      // Validate Birth day + month + year combination.
      if (iBirthYear % 4 == 0 && iBirthMonth == 2)
        iValidBirthDay = 29;
      else if (iBirthMonth == 2)
        iValidBirthDay = 28;
      else if (iBirthMonth == 4 || iBirthMonth == 6 || iBirthMonth == 9 ||
          iBirthMonth == 11)
        iValidBirthDay = 30;
      else if (iBirthMonth < 12)
        iValidBirthDay = 31;

      if (iBirthDay > iValidBirthDay) {
        // February -- Check for Leap Year
        // in the range.
        if (iBirthMonth == 2) {
          if (iBirthDay == 29) {
          } else
            iBirthYear = getAgeRangeLeapYear(iBirthYear);
        } else {
          throw new BusinessRuleException(
              "Birth Day/Month combination not correct, please verify");
        }
      }
    }
    // Try formatting the constructed date.
    try {
      SimpleDateFormat defaultSDF = new SimpleDateFormat("MM/dd/yy");
      sBirthDate = sBirthMonth + "/" + sBirthDay + "/" + iBirthYear;
      Date genratedDate = defaultSDF.parse(sBirthDate);
      return genratedDate;
    } catch (Exception e) {
      throw new BusinessRuleException("Invalid Date format.");
    }
  }

  private int getAgeRangeLeapYear(int iAge) {
    for (int iCtr = 0; iCtr < 3; iCtr++, iAge++) {
      if (iAge % 4 == 0)return iAge;
    }
    return -1;
  }

  /**
   * put your documentation comment here
   * @return
   */
/*  public String getAgeRangeCode() {
    return "" + cmbxAge.getSelectedIndex();
  }*/
  public String getAgeRangeCode() {
	  String agecode ="0" +(cmbxAge.getSelectedIndex()+1);
	  System.out.println("Age  code:-- "+agecode);
	    return "0" +(cmbxAge.getSelectedIndex()+1);
	  }
  
}
