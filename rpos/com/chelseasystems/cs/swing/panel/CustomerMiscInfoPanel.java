/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.bean.*;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.*;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Vector;
import java.util.StringTokenizer;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.swing.bean.JCMSTextField_JP;
import com.chelseasystems.cs.util.ArmConfigLoader;
import com.chelseasystems.cs.util.Version;


/**
 * <p>Title:CustomerMiscInfoPanel </p>
 *
 * <p>Description: Holds Misc. info for customer</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-03-2005 | Manpreet  | N/A       | Original Version                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-20-2005 | Vikram    |  215      | Previously viewed customer address details was     |
 |      |            |           |           | displayed.: Put chack before setting gender        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 09/01/2005 | Manpreet  | 894/895   | Change Request to pick-up Gender, Customer Type    |
 |      |            |           |           |  from ArmaniCommon.cfg file                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 09/06/2005 | Manpreet  | N/A       |Try catch block in populate methods                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 04/18/2006 | Sandhya   | 1500      |Customer Management - All new customers are         |
 |      |            |           |           |defaulting as "Do Not Mail".                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerMiscInfoPanel extends JPanel {
	/**
	 * Gender Label
	 */
	private JCMSLabel lblGender;

	/**
	 * Gender Combo box
	 */
	private JCMSComboBox cmboxGender;

	/**
	 * Customer Type
	 */
	private JCMSLabel lblCustType;

	/**
	 * CustomerType Combobox
	 */
	private JCMSComboBox cmboxCustType;

	/**
	 * Loyality label
	 */
	private JCMSLabel lblLoyality;

	/**
	 * VIP Label
	 */
	private JCMSLabel lblVip;

	/**
	 * No Mail label
	 */
	private JCMSLabel lblNoMail;

	/**
	 * NoCall Label
	 */
	private JCMSLabel lblNoCall;

	/**
	 * NoEmail label
	 */
	private JCMSLabel lblNoEmail;

	/**
	 * No SMS Label
	 */
	private JCMSLabel lblNoSMS;

	/**
	 * Associated ID label
	 */
	private JCMSLabel lblAssoc;

	/**
	 * Associated Id TxtField
	 */
	private JCMSTextField txtAssocID;
	
	//Added for Date Field for Europe
	/**
	 * Date label
	 */
	private JCMSLabel lblDate;

	/**
	 * Date  TxtField
	 */
	private JCMSTextField txtDate;


	/**
	 * NoMail buttongroup
	 */
	private ButtonGroup bgrpNoMail;

	/**
	 * NoCall buttonGroup
	 */
	private ButtonGroup bgrpNoCall;

	/**
	 * NoEmail buttonGroup
	 */
	private ButtonGroup bgrpNoEMail;

	/**
	 * NoSms buttonGroup
	 */
	private ButtonGroup bgrpNoSMS;

	/**
	 * Loyality buttonGroup
	 */
	private ButtonGroup bgrpLoyality;

	/**
	 * LoyalityNo
	 */
	private JCMSCheckBox jrbLoyalityNo;

	/**
	 * LoyalityYes
	 */
	private JCMSCheckBox jrbLoyalityYes;

	/**
	 * PrivacyYes
	 */
	/**
	 * PrivacyNo
	 */
	/**
	 * NoMailYes
	 */
	private JCMSCheckBox jrbNoMailYes;

	/**
	 * NoCallYes
	 */
	private JCMSCheckBox jrbNoCallYes;

	/**
	 * NoEmailYes
	 */
	private JCMSCheckBox jrbNoEmailYes;

	/**
	 * NoSMSYes
	 */
	private JCMSCheckBox jrbNoSMSYes;

	/**
	 * NoMailNo
	 */
	private JCMSCheckBox jrbNoMailNo;

	/**
	 * NoCallNo
	 */
	private JCMSCheckBox jrbNoCallNo;

	/**
	 * NoEmailNo
	 */
	private JCMSCheckBox jrbNoEmailNo;

	/**
	 * NoSMSNo
	 */
	private JCMSCheckBox jrbNoSMSNo;
	private ConfigMgr config;
	
	//Added for Privacy Mgmt Marketing Button Group
	
	private ButtonGroup bgrpPrivacyMarketing;
	//Privacy Marketing Yes
	private JCMSCheckBox jrbPrivacyMarketingYes;
	//Privacy Marketing No
	private JCMSCheckBox jrbPrivacyMarketingNo;
	//Privacy Marketing Label 
	private JCMSLabel lblPrivacyMarketing;
	
	//Added for Privacy Mgmt Master Button Group
	
	private ButtonGroup bgrpPrivacyMaster;
	//Privacy Master Yes
	private JCMSCheckBox jrbPrivacyMasterYes;
	//Privacy Master No
	private JCMSCheckBox jrbPrivacyMasterNo;
	//Privacy Master Label 
	private JCMSLabel lblPrivacyMaster;
	/**
	 * ALPHA_NUMERIC WITH SPECIAL CHARACTER.
	 */
	public static final String ALPHA_NUMERIC_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";

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
	 * Default constructor
	 */
	public CustomerMiscInfoPanel() {
		// configMgr = new ConfigMgr("customer.cfg"); -- MSB 09/01/2005
		// configMgr = new ConfigMgr("ArmaniCommon.cfg");
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
			// String sTmpKeys = configMgr.getString("GENDER_TYPES");
			// String sTmpValues = configMgr.getString("GENDER.VALUES");
			// StringTokenizer sTokensKeys = new StringTokenizer(sTmpKeys,",");
			// StringTokenizer sTokensValues = new StringTokenizer(sTmpValues,",");
			// while(sTokensKeys.hasMoreTokens())
			// {
			// sTmpKeys = sTokensKeys.nextToken();
			// sTmpValues = sTokensValues.nextToken();
			// vecGenderKeys.addElement(sTmpKeys);
			// vecGenderValues.addElement(sTmpValues);
			// }
			// String strSubTypes = configMgr.getString("GENDER_TYPES"); -- MSB 09/01/2005
			String strSubTypes = configMgr.getString("GENDER");
			StringTokenizer stk = null;
			int i = -1;
			if (strSubTypes != null && strSubTypes.trim().length() > 0) {
				stk = new StringTokenizer(strSubTypes, ",");
			} else
				return;
			if (stk != null) {
				types = new String[stk.countTokens()];
				while(stk.hasMoreTokens()) {
					types[++i] = stk.nextToken();
					String key = configMgr.getString(types[i] + ".CODE");
					vecGenderKeys.add(key);
					String value = configMgr.getString(types[i] + ".LABEL");
					vecGenderLabel.add(value);
				}
			}
			cmboxGender.setModel(new DefaultComboBoxModel(vecGenderLabel));
		} catch (Exception e) {
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void populateCustomerType() {
		try {
			// String sTmpKeys = configMgr.getString("CUST_TYPE.KEYS");
			// String sTmpValues = configMgr.getString("CUST_TYPE.VALUES");
			// StringTokenizer sTokensKeys = new StringTokenizer(sTmpKeys,",");
			// StringTokenizer sTokensValues = new StringTokenizer(sTmpValues,",");
			// while(sTokensKeys.hasMoreTokens())
			// {
			// sTmpKeys = sTokensKeys.nextToken();
			// sTmpValues = sTokensValues.nextToken();
			// vecCustTypeKeys.addElement(sTmpKeys);
			// vecCustTypeValues.addElement(sTmpValues);
			// }
			// String strSubTypes = configMgr.getString("CUST_TYPES"); -- MSB 09/01/05
			String strSubTypes = configMgr.getString("CUSTOMER_TYPE");
			StringTokenizer stk = null;
			int i = -1;
			if (strSubTypes != null && strSubTypes.trim().length() > 0) {
				stk = new StringTokenizer(strSubTypes, ",");
			} else
				return;
			if (stk != null) {
				types = new String[stk.countTokens()];
				while(stk.hasMoreTokens()) {
					types[++i] = stk.nextToken();
					String key = configMgr.getString(types[i] + ".CODE");
					vecCustTypeKeys.add(key);
					String value = configMgr.getString(types[i] + ".LABEL");
					vecCustTypeLabel.add(value);
				}
			}
			cmboxCustType.setModel(new DefaultComboBoxModel(vecCustTypeLabel));
		} catch (Exception e) {
		}
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
				while(stk.hasMoreTokens()) {
					types[++i] = stk.nextToken();
					String key = configMgr.getString(types[i] + ".CODE");
					vecPrivacyKeys.add(key);
					String value = configMgr.getString(types[i] + ".LABEL");
					vecPrivacyLabels.add(value);
				}
			}
			cmbxPrivacy.setModel(new DefaultComboBoxModel(vecPrivacyLabels));
		} catch (Exception e) {
		}

	}

	/**
	 * Get AssociateId
	 * 
	 * @return AssociateId
	 */
	public String getAssociateId() {
		return txtAssocID.getText();
	}

	/**
	 * Get AssociateId() text.
	 * 
	 * @param sValue
	 *            String
	 */
	public JCMSTextField getAssociateIDTxt() {
		return this.txtAssocID;
	}

	/**
	 * Set Associate ID
	 * 
	 * @param sValue
	 *            AssociateId
	 */
	public void setAssociateId(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtAssocID.setText(sValue);
	}

	/**
	 * Get No SMS
	 * 
	 * @return noSMS
	 */
	public boolean getNoSMS() {
		return jrbNoSMSYes.isSelected();
	}

	/**
	 * Set No SMS
	 * 
	 * @param bSMS
	 *            noSMS
	 */
	public void setNoSMS(boolean bSMS) {
		if (bSMS)
			jrbNoSMSYes.setSelected(true);
		else if (!bSMS)
			jrbNoSMSNo.setSelected(true);
	}

	/**
	 * Get No email
	 * 
	 * @return noEmail
	 */
	public boolean getNoEmail() {
		return jrbNoEmailYes.isSelected();
	}

	/**
	 * Set No email
	 * 
	 * @param bNoEmail
	 *            noEmail
	 */
	public void setNoEmail(boolean bNoEmail) {
		if (bNoEmail)
			jrbNoEmailYes.setSelected(true);
		else if (!bNoEmail)
			jrbNoEmailNo.setSelected(true);
	}

	/**
	 * Set No Call
	 * 
	 * @param bNoCall
	 *            noCall
	 */
	public void setNoCall(boolean bNoCall) {
		if (bNoCall)
			jrbNoCallYes.setSelected(true);
		else if (!bNoCall)
			jrbNoCallNo.setSelected(true);
	}

	/**
	 * Get No Call
	 * 
	 * @return NoCall
	 */
	public boolean getNoCall() {
		return jrbNoCallYes.isSelected();
	}

	/**
	 * Get NoMail
	 * 
	 * @return NoMail
	 */
	public boolean getNoMail() {
		return jrbNoMailYes.isSelected();
	}

	/**
	 * Set No mail
	 * 
	 * @param bNoMail
	 *            NoMail
	 */
	public void setNoMail(boolean bNoMail) {
		if (bNoMail)
			jrbNoMailYes.setSelected(true);
		else if (!bNoMail)
			jrbNoMailNo.setSelected(true);
	}

	// /**
	// * Set Privacy
	// * @param bPrivacy Privacy
	// */
	// public void setPrivacy(boolean bPrivacy) {
	// jrbPrvcyYes.setSelected(bPrivacy);
	// }
	//
	// /**
	// * Get Privacy
	// * @return PrivacyYes or No
	// */
	// public boolean getPrivacy() {
	// return jrbPrvcyYes.isSelected();
	// }

	public void setPrivacy(String sValue) {
		if (sValue == null || sValue.length() < 1 || vecPrivacyKeys.indexOf(sValue) == -1)
			return;
		cmbxPrivacy.setSelectedIndex(vecPrivacyKeys.indexOf(sValue));
	}

	public String getPrivacy() {
		if (cmbxPrivacy.getSelectedIndex() == -1)
			return null;
		return (String) vecPrivacyKeys.elementAt(cmbxPrivacy.getSelectedIndex());
	}

	/**
	 * Get VIP Percentage
	 * 
	 * @return VIPPercentage
	 */
	public String getVIPPercentage() {
		return lblVIPValue.getText();
	}

	/**
	 * Set VIP percentage
	 * 
	 * @param sValue
	 *            Percentage
	 */
	public void setVIPPercentage(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		lblVIPValue.setText(sValue);
		lblVIPValue.setVisible(true);
	}

	/**
	 * Set Customer gender
	 * 
	 * @param sValue
	 *            Gender
	 */
	public void setSelectedGender(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		cmboxGender.setSelectedIndex(vecGenderKeys.indexOf(sValue));
	}

	/**
	 * Get Customer gender
	 * 
	 * @return Gender
	 */
	public String getSelectedGender() {
		if (cmboxGender.getSelectedIndex() >= 0)
			return (String) vecGenderKeys.elementAt(cmboxGender.getSelectedIndex());
		return null;
		// return (String) cmboxGender.getSelectedItem();
	}

	/**
	 * Select Customer Type
	 * 
	 * @param sValue
	 *            CustomerType
	 */
	public void setSelectedCustomerType(String sValue) {
		if (sValue == null || sValue.length() < 1 || vecCustTypeKeys.indexOf(sValue) == -1)
			return;
		cmboxCustType.setSelectedIndex(vecCustTypeKeys.indexOf(sValue));
	}

	/**
	 * Get customer Type.
	 * 
	 * @return CustomerType
	 */
	public String getSelectedCustomerType() {
		if (cmboxCustType.getSelectedIndex() == -1)
			return null;
		return (String) vecCustTypeKeys.elementAt(cmboxCustType.getSelectedIndex());
	}

	/**
	 * Set LoyalityProgram
	 * 
	 * @param bLoyality
	 *            Yes/No
	 */
	public void setLoyalityProgram(boolean bLoyality) {
		if (bLoyality) {
			jrbLoyalityYes.setSelected(true);
			jrbLoyalityNo.setSelected(false);
		} else {
			jrbLoyalityYes.setSelected(false);
			jrbLoyalityNo.setSelected(true);
		}
	}

	/**
	 * Get LoyalityProgram
	 * 
	 * @return True/False
	 */
	public boolean getLoyalityProgram() {
		return jrbLoyalityYes.isSelected();
	}

	/**
	 * Set Application Manager
	 * 
	 * @param theAppMgr
	 *            ApplicationManager
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		this.setBackground(theAppMgr.getBackgroundColor());
		// Loop through and set Application managers for all
		// JCMS components.
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		Font labelFont = theAppMgr.getTheme().getLabelFont();
		Font textFont = theAppMgr.getTheme().getTextFieldFont();
		for (int iCtr = 0; iCtr < this.getComponentCount(); iCtr++) {
			Component component = this.getComponent(iCtr);
			if (component instanceof JCMSTextField) {
				((JCMSTextField) component).setAppMgr(theAppMgr);
				((JCMSTextField) component).setFont(textFont);
				//((JCMSTextField) component).setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
				//((JCMSTextField) component).setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
				//((JCMSTextField) component).setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
			} else if (component instanceof JCMSTextArea) {
				((JCMSTextArea) component).setAppMgr(theAppMgr);
			} else if (component instanceof JCMSLabel) {
				((JCMSLabel) component).setAppMgr(theAppMgr);
				((JCMSLabel) component).setFont(labelFont);
				//((JCMSLabel) component).setPreferredSize(new Dimension((int) (85 * r), (int) (15 * r)));
			} else if (component instanceof JCMSComboBox) {
				((JCMSComboBox) component).setAppMgr(theAppMgr);
				((JCMSComboBox) component).setFont(textFont);
				//((JCMSComboBox) component).setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
				//((JCMSComboBox) component).setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
				//((JCMSComboBox) component).setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
			} else if (component instanceof JCMSMaskedTextField) {
				((JCMSMaskedTextField) component).setAppMgr(theAppMgr);
				((JCMSMaskedTextField) component).setFont(textFont);
			} else if (component instanceof JCMSCheckBox) {
				((JCMSCheckBox) component).setAppMgr(theAppMgr);
				((JCMSCheckBox) component).setFont(labelFont);
				//((JCMSCheckBox) component).setPreferredSize(new Dimension((int) (35 * r), (int) (25 * r)));
			}
		}
		lblVIPValue.setFont(textFont);
		//txtAssocID.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		//txtAssocID.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		//txtAssocID.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param bEnabled
	 */
	public void setEnabled(boolean bEnabled) {
		cmboxGender.setEnabled(bEnabled);
		cmboxCustType.setEnabled(bEnabled);
		// jrbPrvcyYes.setEnabled(bEnabled);
		// jrbPrvcyNo.setEnabled(bEnabled);
		cmbxPrivacy.setEnabled(bEnabled);
		jrbNoMailYes.setEnabled(bEnabled);
		jrbNoCallYes.setEnabled(bEnabled);
		jrbNoEmailYes.setEnabled(bEnabled);
		jrbNoSMSYes.setEnabled(bEnabled);
		jrbNoMailNo.setEnabled(bEnabled);
		jrbNoCallNo.setEnabled(bEnabled);
		jrbNoEmailNo.setEnabled(bEnabled);
		jrbNoSMSNo.setEnabled(bEnabled);
		txtAssocID.setEnabled(bEnabled);
		//Added for Field Date for Europe by Vivek
		txtDate.setEnabled(false);
		//Added for Privacy Mgmt Marketing and Master 		
		jrbPrivacyMarketingNo.setEnabled(bEnabled);
		jrbPrivacyMarketingYes.setEnabled(bEnabled);
		jrbPrivacyMasterNo.setEnabled(bEnabled);
		jrbPrivacyMasterYes.setEnabled(bEnabled);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param bEnabled
	 */
	public void setModifyEnabled(boolean bEnabled) {
		cmboxGender.setEnabled(bEnabled);
		cmbxPrivacy.setEnabled(bEnabled);
		jrbNoMailYes.setEnabled(bEnabled);
		jrbNoCallYes.setEnabled(bEnabled);
		jrbNoEmailYes.setEnabled(bEnabled);
		jrbNoSMSYes.setEnabled(bEnabled);
		jrbNoMailNo.setEnabled(bEnabled);
		jrbNoCallNo.setEnabled(bEnabled);
		jrbNoEmailNo.setEnabled(bEnabled);
		jrbNoSMSNo.setEnabled(bEnabled);
		txtAssocID.setEnabled(bEnabled);
		//Added for Field Date for Europe by Vivek
		txtDate.setEnabled(bEnabled);
		//Added for Privacy Mgmt Marketing and Master
		jrbPrivacyMarketingNo.setEnabled(bEnabled);
		jrbPrivacyMarketingYes.setEnabled(bEnabled);
		jrbPrivacyMasterNo.setEnabled(bEnabled);
		jrbPrivacyMasterYes.setEnabled(bEnabled);
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
		jrbLoyalityNo.setSelected(true);
		jrbLoyalityYes.setSelected(false);
		config = new ConfigMgr("customer.cfg");
		// String privacyRequired = config.getString("PRIVACY_REQUESTED");
		String mailAllowed = config.getString("MAIL_ALLOWED");
		String callAllowed = config.getString("CALL_ALLOWED");
		String emailAllowed = config.getString("EMAIL_ALLOWED");
		String smsAllowed = config.getString("SMS_ALLOWED");
		//Added for Privacy Mgmt Marketing and Master
		String privacyMarketing = config.getString("PRIVACY_MARKETING");
		String privacyMaster = config.getString("PRIVACY_MASTER");
		//boolean boolPrvy = true;
		boolean boolMail = true;
		boolean boolCall = true;
		boolean boolEmail = true;
		boolean boolSMS = true;
		boolean boolPrivacyMarketing = true;
		boolean boolPrivacyMaster = true;
//		 if (privacyRequired.equalsIgnoreCase("yes"))
		// boolPrvy = true;
		// else if (privacyRequired.equalsIgnoreCase("no"))
		// boolPrvy = false;
		if (mailAllowed.equalsIgnoreCase("yes"))
			boolMail = true;
		else if (mailAllowed.equalsIgnoreCase("no"))
			boolMail = false;
		if (callAllowed.equalsIgnoreCase("yes"))
			boolCall = true;
		else if (callAllowed.equalsIgnoreCase("no"))
			boolCall = false;
		if (emailAllowed.equalsIgnoreCase("yes"))
			boolEmail = true;
		else if (emailAllowed.equalsIgnoreCase("no"))
			boolEmail = false;
		if (smsAllowed.equalsIgnoreCase("yes"))
			boolSMS = true;
		else if (smsAllowed.equalsIgnoreCase("no"))
			boolSMS = false;		
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
		if (privacyMarketing.equalsIgnoreCase("yes"))
			 boolPrivacyMarketing = true;		
		else if (privacyMarketing.equalsIgnoreCase("no"))
			 boolPrivacyMarketing = false;
		if (privacyMaster.equalsIgnoreCase("yes"))
			 boolPrivacyMaster = true;
		else if (privacyMaster.equalsIgnoreCase("no"))
			 boolPrivacyMaster = false;
		}
		// Fix for issue 1500
		jrbNoMailYes.setSelected(boolMail);
		jrbNoMailNo.setSelected(!boolMail);
		jrbNoCallYes.setSelected(boolCall);
		jrbNoCallNo.setSelected(!boolCall);
		jrbNoEmailYes.setSelected(boolEmail);
		jrbNoEmailNo.setSelected(!boolEmail);
		jrbNoSMSYes.setSelected(boolSMS);
		jrbNoSMSNo.setSelected(!boolSMS);
		lblVIPValue.setText("0.0");
		//Added for Privacy Mgmt Marketing and Master
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
		jrbPrivacyMarketingNo.setSelected(!boolPrivacyMarketing);
		jrbPrivacyMarketingYes.setSelected(boolPrivacyMarketing);
		jrbPrivacyMasterNo.setSelected(!boolPrivacyMaster);
		jrbPrivacyMasterYes.setSelected(boolPrivacyMaster);	
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void makeReadOnlyFields() {
		jrbLoyalityYes.setEnabled(false);
		jrbLoyalityNo.setEnabled(false);
	}
	
	private JCMSTextField getJCMSTextField(){
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
			return new JCMSTextField_JP();
		}
		
		return  new JCMSTextField();
	}

	/**
	 * Initialize components and lay them out.
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		lblGender = new JCMSLabel();
		cmboxGender = new JCMSComboBox();
		lblCustType = new JCMSLabel();
		cmboxCustType = new JCMSComboBox();
		lblLoyality = new JCMSLabel();
		lblVip = new JCMSLabel();
		lblNoMail = new JCMSLabel();
		lblNoCall = new JCMSLabel();
		lblNoEmail = new JCMSLabel();
		lblNoSMS = new JCMSLabel();
		lblAssoc = new JCMSLabel();
		//Added for Field Date for Europe by Vivek
		lblDate = new JCMSLabel();
		txtAssocID = getJCMSTextField();
//		Added for Field Date for Europe by Vivek
		txtDate = getJCMSTextField();		
		bgrpNoMail = new ButtonGroup();
		bgrpNoCall = new ButtonGroup();
		bgrpNoEMail = new ButtonGroup();
		bgrpNoSMS = new ButtonGroup();
		bgrpLoyality = new ButtonGroup();
		jrbLoyalityNo = new JCMSCheckBox();
		jrbLoyalityYes = new JCMSCheckBox();
		jrbNoMailYes = new JCMSCheckBox();
		jrbNoCallYes = new JCMSCheckBox();
		jrbNoEmailYes = new JCMSCheckBox();
		jrbNoSMSYes = new JCMSCheckBox();
		jrbNoMailNo = new JCMSCheckBox();
		jrbNoCallNo = new JCMSCheckBox();
		jrbNoEmailNo = new JCMSCheckBox();
		jrbNoSMSNo = new JCMSCheckBox();
		lblVIPValue = new JCMSLabel();
		lblPrivacy = new JCMSLabel();
		cmbxPrivacy = new JCMSComboBox();
		//Added for Privacy Mgmt Marketing and Master
		lblPrivacyMarketing = new JCMSLabel();
		bgrpPrivacyMarketing = new ButtonGroup();
		jrbPrivacyMarketingNo = new JCMSCheckBox();
		jrbPrivacyMarketingYes = new JCMSCheckBox();
		//Added for Privacy Mgmt Marketing and Master
		lblPrivacyMaster = new JCMSLabel();
		bgrpPrivacyMaster = new ButtonGroup();
		jrbPrivacyMasterNo = new JCMSCheckBox();
		jrbPrivacyMasterYes = new JCMSCheckBox();
		
		gridBagLayout1 = new GridBagLayout();

		txtAssocID.setName("AssociateID");
		//Added for Field Date for Europe by Vivek
		txtDate.setName("Date");
		makeReadOnlyFields();

		this.setLayout(gridBagLayout1);
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Misc"));

		lblGender.setText(RESOURCE.getString("Gender"));
		lblCustType.setText(RESOURCE.getString("Customer Type"));
		lblLoyality.setText(RESOURCE.getString("Loyality Prog:"));
		lblVip.setText(RESOURCE.getString("VIP %:"));
		lblNoMail.setText(RESOURCE.getString("Mail Allowed"));
		lblNoCall.setText(RESOURCE.getString("Call Allowed"));
		lblNoEmail.setText(RESOURCE.getString("Email Allowed"));
		lblNoSMS.setText(RESOURCE.getString("SMS Allowed"));
		lblAssoc.setText(RESOURCE.getString("Assoc. ID"));
		//Added for Field Date for Europe by vivek
		lblDate.setText(RESOURCE.getString("Date"));		
		txtAssocID.setText("");
		//Added for Field Date for Europe by vivek
		txtDate.setText("");
		jrbLoyalityNo.setText(RESOURCE.getString("No"));
		jrbLoyalityYes.setText(RESOURCE.getString("Yes"));
		jrbNoMailYes.setText(RESOURCE.getString("Yes"));
		jrbNoCallYes.setText(RESOURCE.getString("Yes"));
		jrbNoEmailYes.setText(RESOURCE.getString("Yes"));
		jrbNoSMSYes.setText(RESOURCE.getString("Yes"));
		jrbNoMailNo.setText(RESOURCE.getString("No"));
		jrbNoCallNo.setText(RESOURCE.getString("No"));
		jrbNoEmailNo.setText(RESOURCE.getString("No"));
		jrbNoSMSNo.setText(RESOURCE.getString("No"));
		lblVIPValue.setText(RESOURCE.getString("xxx"));
		lblPrivacy.setText(RESOURCE.getString("Privacy"));
		//Added for Privacy Marketing and Master
		lblPrivacyMarketing.setText(RESOURCE.getString("Marketing"));
		jrbPrivacyMarketingNo.setText(RESOURCE.getString("No"));
		jrbPrivacyMarketingYes.setText(RESOURCE.getString("Yes"));
		lblPrivacyMaster.setText(RESOURCE.getString("Master"));
		jrbPrivacyMasterNo.setText(RESOURCE.getString("No"));
		jrbPrivacyMasterYes.setText(RESOURCE.getString("Yes"));
		bgrpLoyality.add(jrbLoyalityYes);
		bgrpLoyality.add(jrbLoyalityNo);
		bgrpNoCall.add(jrbNoCallYes);
		bgrpNoCall.add(jrbNoCallNo);
		bgrpNoMail.add(jrbNoMailYes);
		bgrpNoMail.add(jrbNoMailNo);
		bgrpNoEMail.add(jrbNoEmailYes);
		bgrpNoEMail.add(jrbNoEmailNo);
		bgrpNoSMS.add(jrbNoSMSYes);
		bgrpNoSMS.add(jrbNoSMSNo);
		//Added for Privacy Mgmt Marketing and Master
		bgrpPrivacyMarketing.add(jrbPrivacyMarketingYes);		
		bgrpPrivacyMarketing.add(jrbPrivacyMarketingNo);
		bgrpPrivacyMaster.add(jrbPrivacyMasterYes);
		bgrpPrivacyMaster.add(jrbPrivacyMasterNo);
		/*
		 * public GridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets
		 * insets, int ipadx, int ipady) { public Insets(int top, int left, int bottom, int right) {
		 */
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			this.add(lblPrivacyMaster, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0 , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1, 1, 0, 0), 1, 0));
	        this.add(jrbPrivacyMasterYes, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1, 0, 0, 0), 2, 3));
	        this.add(lblPrivacyMarketing, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1, 1, 0, 0), 1, 1));
	        this.add(jrbPrivacyMarketingYes,new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 0), 1, 1));
	        this.add(jrbPrivacyMarketingNo,new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 1, 1));
	        this.add(jrbPrivacyMasterNo, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 6, 1));
		}
		else{
			 this.add(lblPrivacy, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1, 1, 0, 0), 1, 1));		
		     this.add(lblLoyality, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0 , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1, 1, 0, 0), 1, 0));
		     this.add(jrbLoyalityYes, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(1, 0, 0, 0), 2, 3));
		     this.add(cmbxPrivacy,new GridBagConstraints(1, 3, GridBagConstraints.REMAINDER, 1,1.0, 0.0, GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(1, 0, 0, 6), 1, 1));       
		     this.add(jrbLoyalityNo, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 6, 1));
				}       
		this.add(lblVip, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 0, 0), 1, 0));
        this.add(lblNoMail, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 1, 6, 0), 0, 0));
        this.add(lblNoCall, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 1, 0, 0), 1, 0));
        this.add(jrbNoCallYes, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 0), 1, 1));
        this.add(lblNoEmail, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 1, 0, 0), 1, 0));
        this.add(jrbNoEmailYes, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 0), 1, 1));
        this.add(jrbNoEmailNo, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 1, 0));
        this.add(lblNoSMS, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 0, 0), 1, 1));        
        this.add(jrbNoSMSYes, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 0), 1, 1));
        this.add(jrbNoSMSNo, new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 1, 1));
        this.add(cmboxGender,new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1,1.0, 0.0, GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(1, 0, 0, 6), 1, 1));
        this.add(cmboxCustType,new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1,1.0, 0.0, GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(1, 1, 2, 6), 1, 1));
		//Commented for Privacy Mgmt Marketing and Master
       // this.add(cmbxPrivacy,new GridBagConstraints(1, 3, GridBagConstraints.REMAINDER, 1,1.0, 0.0, GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(1, 0, 0, 6), 1, 1));
       // this.add(jrbLoyalityNo, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 6, 1));        
        this.add(jrbNoMailNo, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 1, 1));
        this.add(jrbNoCallNo, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 1), 1, 1));        
        this.add(jrbNoMailYes, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 0, 0, 0), 1, 1));
        this.add(lblVIPValue,new GridBagConstraints(1, 5, GridBagConstraints.REMAINDER, 2,1.0, 0.0, GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(1, 6, 0, 6), 0, 3));
        this.add(lblGender, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,new Insets(1, 1, 0, 0), 0, 0));
        this.add(lblCustType,new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1,0.0, 0.0, GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 0, 6), 0, 0));
       //Added for Field Date for Europe by Vivek.
        if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
        this.add(lblDate, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 0, 6), 1,0));
        this.add(txtDate, new GridBagConstraints(0, 12, 4, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,new Insets(1, 1, 3, 1), 1, 1));
        }else{
        this.add(lblAssoc, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 0, 6), 1,0));
        this.add(txtAssocID, new GridBagConstraints(0, 12, 4, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,new Insets(1, 1, 3, 1), 1, 1));
        }
		 //Set weights of all columns and rows to 1
		 double[][] weights = gridBagLayout1.getLayoutWeights();
		 for (int i = 0; i < 2; i++) {
		 for (int j = 0; j < weights[i].length; j++) {
		 weights[i][j] = 1;
		 }
		 }
		 gridBagLayout1.columnWeights = weights[0];
		 gridBagLayout1.rowWeights = weights[1];
	}
	
	//Added for Privacy Mgmt Marketing and Master 
	public boolean getPrivacyMarketing() {
		return jrbPrivacyMarketingYes.isSelected();
	}
	/**
	 * Set Marketing
	 * 
	 * @param bPrivacyMarketing
	 *            
	 */
	public void setPrivacyMarketing(boolean bPrivacyMarketing) {
		if (bPrivacyMarketing)
			jrbPrivacyMarketingYes.setSelected(true);
		else if (!bPrivacyMarketing)
			jrbPrivacyMarketingNo.setSelected(true);
	}
	
	public boolean getPrivacyMaster() {
		return jrbPrivacyMasterYes.isSelected();
	}

	/**
	 * Set Master
	 * 
	 * @param bPrivacyMaster
	 *            
	 */
	public void setPrivacyMaster(boolean bPrivacyMaster) {
		if (bPrivacyMaster)
			jrbPrivacyMasterYes.setSelected(true);
		else if (!bPrivacyMaster)
			jrbPrivacyMasterNo.setSelected(true);
	}
	//Added getter and setter for field Date for Europe by Vivek
	/**
	 * Get Date
	 * 
	 * @return Date
	 */
	public String getDate() {
		return txtDate.getText();
	}

	/**
	 * Get Date() text.
	 * 
	 * @param sValue
	 *            String
	 */
	public JCMSTextField getDateTxt() {
		return this.txtDate;
	}

	/**
	 * Set Date
	 * 
	 * @param sValue
	 *            Date
	 */
	public void setDate(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtDate.setText(sValue);
	}


}
