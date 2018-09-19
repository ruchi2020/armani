/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 * <p>Title:CustomerBasicPanel.java </p>
 *
 * <p>Description: Displays customer's basic information </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 06-24-2005 | Vikram    |  268      | Title of customer was not highlighted when tabbing.|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-21-2005 | Vikram    |  262      | Added limit on Title length.                       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-05-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerBasicPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Title
	 */
	protected JCMSLabel lblTitle;
	/**
	 * First Name
	 */
	protected JCMSLabel lblFirstName;
	/**
	 * Middle Name
	 */
	protected JCMSLabel lblMiddleName;
	/**
	 * Last Name
	 */
	protected JCMSLabel lblLastName;
	/**
	 * Suffix
	 */
	protected JCMSLabel lblSuffix;
	/**
	 * Title value
	 */
	protected JCMSTextField txtTitle;
	/**
	 * First Name Value
	 */
	protected JCMSTextField txtFirstName;
	/**
	 * Middle Name Value
	 */
	protected JCMSTextField txtMidName;
	/**
	 * Last Name Value
	 */
	protected JCMSTextField txtLastName;
	/**
	 * Suffix Value
	 */
	protected JCMSTextField txtSuffix;
	/**
	 * Primary Email
	 */
	protected JCMSLabel lblPrimaryEmail;
	/**
	 * Secondary Email
	 */
	protected JCMSLabel lblSecondEmail;
	/**
	 * PrimaryEmail Value
	 */
	protected JCMSTextField txtPrimaryEmail;
	/**
	 * SecondaryEmail Value
	 */
	protected JCMSTextField txt2ndEmail;
	/**
	 * Return Mail
	 */
	protected JCMSCheckBox chbReturnMail;

	/**
	 * ALPHA_NUMERIC WITH SPECIAL CHARACTER.
	 */
	public static final String ALPHA_NUMERIC_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]'{}/;\" ";
	public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";
	public static final String NAME_ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.-' ";
	protected JCMSLabel lblFirstNameJP;
	protected JCMSTextField txtFirstNameJP;
	protected JCMSLabel lblLastNameJP;
	protected JCMSTextField txtLastNameJP;
	protected JCMSLabel lblID;
	protected JCMSTextField txtID;

	private static final String CONFIGURATION_FILE = "customer.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	private String custBasicPanelClassName = configMgr.getString("CUSTOMER.BASIC_PANEL");

	/**
	 * Default constructor
	 */
	public CustomerBasicPanel() {
		try {
			if (custBasicPanelClassName == null) {
				jbInit();
				setEnabled(false);
				reset();
			} else {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Set Application Manager
	 * @param theAppMgr
	 *            ApplicationManager
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		this.setBackground(theAppMgr.getBackgroundColor());
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		// Loop through and set Application managers for all
		// JCMS components.
		for (int iCtr = 0; iCtr < this.getComponentCount(); iCtr++) {
			Component component = this.getComponent(iCtr);
			Font lblFont = theAppMgr.getTheme().getLabelFont();
			Font txtFont = theAppMgr.getTheme().getTextFieldFont();
			if (component instanceof JCMSTextField) {
				((JCMSTextField) component).setAppMgr(theAppMgr);
				((JCMSTextField) component).setFont(txtFont);
				((JCMSTextField) component).setPreferredSize(new Dimension((int) (55 * r), (int) (25 * r)));
			} else if (component instanceof JCMSTextArea) {
				((JCMSTextArea) component).setAppMgr(theAppMgr);
				((JCMSTextArea) component).setFont(txtFont);
				((JCMSTextArea) component).setPreferredSize(new Dimension((int) (55 * r), (int) (50 * r)));
			} else if (component instanceof JCMSLabel) {
				((JCMSLabel) component).setAppMgr(theAppMgr);
				((JCMSLabel) component).setFont(lblFont);
				((JCMSLabel) component).setPreferredSize(new Dimension((int) (85 * r), (int) (15 * r)));
			} else if (component instanceof JCMSComboBox) {
				((JCMSComboBox) component).setAppMgr(theAppMgr);
				((JCMSComboBox) component).setPreferredSize(new Dimension((int) (55 * r), (int) (25 * r)));
			} else if (component instanceof JCMSMaskedTextField) {
				((JCMSMaskedTextField) component).setAppMgr(theAppMgr);
				((JCMSMaskedTextField) component).setFont(txtFont);
				((JCMSMaskedTextField) component).setPreferredSize(new Dimension((int) (55 * r), (int) (25 * r)));
			} else if (component instanceof JCMSCheckBox) {
				((JCMSCheckBox) component).setAppMgr(theAppMgr);
				((JCMSCheckBox) component).setFont(txtFont);
			}
			txtID.setBackground(theAppMgr.getBackgroundColor());
			// lblID.setPreferredSize(new Dimension((int)(35 * r), (int)(15 * r)));
			// txtID.setFont(txtFont);
		}
	}

	/**
	 * Sets Customer Id
	 * @param sValue
	 *            CustomerId
	 */
	public void setCustomerID(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtID.setText(sValue);
	}

	/**
	 * Get CustomerID
	 * @return CustomerID
	 */
	public String getCustomerID() {
		return txtID.getText();
	}

	/**
	 * Set Title
	 * @param sValue
	 *            Title
	 */
	public void setTitle(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtTitle.setText(sValue);
	}

	/**
	 * Get Title
	 * @return Title
	 */
	public String getTitle() {
		return txtTitle.getText();
	}

	/**
	 * Set First Name
	 * @param sValue
	 *            FirstName
	 */
	public void setFirstName(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtFirstName.setText(sValue.trim());
	}

	/**
	 * Set First Name JP
	 * @param sValue
	 *            FirstName JP
	 */
	public void setFirstNameJP(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtFirstNameJP.setText(sValue);
	}

	/**
	 * Get FirstName
	 * @return FirstName
	 */
	public String getFirstName() {
		return txtFirstName.getText();
	}

	/**
	 * Get FirstName JP
	 * @return FirstName
	 */
	public String getFirstNameJP() {
		return txtFirstNameJP.getText();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public JCMSTextField getFirstNameJTXT() {
		return txtFirstName;
	}

	/**
	 * Set Middle Name
	 * @param sValue
	 *            MiddleName
	 */
	public void setMiddleName(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtMidName.setText(sValue);
	}

	/**
	 * Get MiddleName
	 * @return MiddleName
	 */
	public String getMiddleName() {
		return txtMidName.getText();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public JCMSTextField getLastNameJTXT() {
		return txtLastName;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public JCMSTextField getSuffixJTXT() {
		return txtSuffix;
	}

	/**
	 * Set Last name
	 * @param sValue
	 *            LastName
	 */
	public void setLastName(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtLastName.setText(sValue);
	}

	/**
	 * Set Last name JP
	 * @param sValue
	 *            LastName JP
	 */
	public void setLastNameJP(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtLastNameJP.setText(sValue);
	}

	/**
	 * Set Last name
	 * @param sValue
	 *            LastName
	 */
	public void setSuffix(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtSuffix.setText(sValue);
	}

	/**
	 * Get LastName
	 * @return LastName
	 */
	public String getLastName() {
		return txtLastName.getText();
	}

	/**
	 * Get LastName JP
	 * 
	 * @return LastName JP
	 */
	public String getLastNameJP() {
		return txtLastNameJP.getText();
	}

	/**
	 * Get Suffix
	 * @return Suffix
	 */
	public String getSuffix() {
		return txtSuffix.getText();
	}

	/**
	 * Initialize and layout components.
	 * @throws Exception
	 */
	/**
	 * Set primary Email.
	 * 
	 * @param sValue
	 *            PrimaryEmail/SMS
	 */
	public void setPrimaryEmail(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txtPrimaryEmail.setText(sValue);
	}

	/**
	 * Get PrimaryEmail
	 * @return PrimaryEmail/SMS
	 */
	public String getPrimaryEmail() {
		return txtPrimaryEmail.getText();
	}

	/**
	 * Set SecondaryEmail.
	 * 
	 * @param sValue
	 *            SecondaryEmail/SMS
	 */
	public void setSecondaryEmail(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		txt2ndEmail.setText(sValue);
	}

	/**
	 * Set SecondaryEmail
	 * @return SecondaryEmail/SMS
	 */
	public String getSecondaryEmail() {
		return txt2ndEmail.getText();
	}

	/**
	 * Set ReturnMail
	 * 
	 * @param bReturnMail
	 *            True/False
	 */
	public void setReturnMail(boolean bReturnMail) {
		chbReturnMail.setSelected(bReturnMail);
	}

	/**
	 * GetReturnMail
	 * @return ReturnMail
	 */
	public boolean getReturnMail() {
		return chbReturnMail.isSelected();
	}

	/**
	 * Set ReturnMail
	 * 
	 * @param bReturnMail
	 *            Y/N
	 */
	public void setReturnMail(String sValue) {
		if (sValue == null || sValue.length() < 1)
			return;
		chbReturnMail.setSelected(sValue.toUpperCase().equals("Y"));
	}

	/**
	 * Get ReturnMail as String
	 * @return Y or N
	 */
	public String getReturnMailString() {
		if (chbReturnMail.isSelected())
			return "Y";
		return "N";
	}

	/**
	 * put your documentation comment here
	 */
	public void reset() {
		txtID.setText("");
		txtTitle.setText("");
		txtFirstName.setText("");
		txtMidName.setText("");
		txtLastName.setText("");
		txtSuffix.setText("");
		txtPrimaryEmail.setText("");
		txt2ndEmail.setText("");
		txtFirstNameJP.setText("");
		txtLastNameJP.setText("");
		chbReturnMail.setSelected(false);
	}

	/**
	 * put your documentation comment here
	 * @param bEnabled
	 */
	public void setEnabled(boolean bEnabled) {
		txtTitle.setEnabled(bEnabled);
		txtFirstName.setEnabled(bEnabled);
		txtMidName.setEnabled(bEnabled);
		txtLastName.setEnabled(bEnabled);
		txtSuffix.setEnabled(bEnabled);
		txtPrimaryEmail.setEnabled(bEnabled);
		txt2ndEmail.setEnabled(bEnabled);
		txtFirstNameJP.setEnabled(bEnabled);
		txtLastNameJP.setEnabled(bEnabled);
		setReturnMailEnabled(bEnabled);
	}

	/**
	 * Initialize and Layout components
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		java.util.ResourceBundle resBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		lblFirstNameJP = new JCMSLabel();
		txtFirstNameJP = new JCMSTextField();
		lblLastNameJP = new JCMSLabel();
		txtLastNameJP = new JCMSTextField();
		lblID = new JCMSLabel();
		txtID = new JCMSTextField();
		lblTitle = new JCMSLabel();
		lblFirstName = new JCMSLabel();
		lblMiddleName = new JCMSLabel();
		lblLastName = new JCMSLabel();
		lblSuffix = new JCMSLabel();
		txtTitle = new JCMSTextField();
		txtTitle.setName("Title");
		txtFirstName = new JCMSTextField();
		txtFirstName.setName("FIRST_NAME");
		txtMidName = new JCMSTextField();
		txtLastName = new JCMSTextField();
		txtLastName.setName("SECOND_NAME");
		txtSuffix = new JCMSTextField();
		txtSuffix.setName("SUFFIX");
		lblPrimaryEmail = new JCMSLabel();
		lblSecondEmail = new JCMSLabel();
		txtPrimaryEmail = new JCMSTextField();
		txt2ndEmail = new JCMSTextField();
		chbReturnMail = new JCMSCheckBox();
		// chbReturnMail.setEnabled(false);
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		this.setLayout(gridBagLayout1);
		//this.setPreferredSize(new Dimension(844, 155));
		lblTitle.setText(resBundle.getString("Title"));
		lblFirstName.setText(resBundle.getString("*First Name"));
		lblMiddleName.setText(resBundle.getString("Middle Name"));
		lblLastName.setText(resBundle.getString("*Last Name"));
		lblSuffix.setText(resBundle.getString("Suffix"));
		lblPrimaryEmail.setText(resBundle.getString("Primary Email/SMS Address"));
		lblSecondEmail.setText(resBundle.getString("2nd Email/SMS Address"));
		chbReturnMail.setHorizontalTextPosition(SwingConstants.LEADING);
		chbReturnMail.setText(resBundle.getString("Return Mail"));
		lblFirstNameJP.setText(resBundle.getString("First Name (JP)"));
		lblLastNameJP.setText(resBundle.getString("Last Name (JP)"));
		lblID.setText(resBundle.getString("ID"));
		txtID.setBorder(null);
		txtID.setEnabled(false);
		txtID.setEditable(false);
		this.add(txtPrimaryEmail, new GridBagConstraints(0, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 3, 5), 165, 0));
		this.add(lblPrimaryEmail, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 3, 5), 22, -1));
		this.add(txt2ndEmail, new GridBagConstraints(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 3, 5), 151, 0));
		this.add(lblSecondEmail, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 3, 5), 29, -1));
		this.add(chbReturnMail, new GridBagConstraints(3, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 13, 3, 33), 21, -5));
		this.add(txtFirstNameJP, new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 3, 5), 165, 0));
		this.add(txtLastNameJP, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 3, 5), 151, 0));
		this.add(txtSuffix, new GridBagConstraints(3, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 9, 3, 5), 131, 0));
		this.add(lblFirstNameJP, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 2, 3, 5), 90, -1));
		this.add(lblLastNameJP, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 5), 76, -1));
		this.add(lblSuffix, new GridBagConstraints(3, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 10, 3, 38), 79, -1));
		this.add(txtMidName, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 151, 0));
		this.add(txtFirstName, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 133, 0));
		this.add(txtTitle, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 5), 18, 0));
		this.add(lblTitle, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 2, 3, 5), 6, -2));
		this.add(lblFirstName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 0, 3, 5), 78, -2));
		this.add(lblMiddleName, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 0, 3, 34), 53, -2));
		this.add(txtLastName, new GridBagConstraints(3, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 0, 0), 137, 0));
		this.add(lblLastName, new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 8, 0, 0), 82, -2));
		this.add(lblID, new GridBagConstraints(3, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-5, 115, 0, 0), 7, 0));
		this.add(txtID, new GridBagConstraints(4, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-5, 0, 0, 0), 25, 5));
		// // Set weights of all columns and rows to 1
		double[][] weights = gridBagLayout1.getLayoutWeights();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = 1;
			}
		}
		gridBagLayout1.columnWeights = weights[0];
		gridBagLayout1.rowWeights = weights[1];
		
		/**
		 * TextFilters Implemented
		 */
		// text lengths are limited by corresponding size in ARM_STG_CUST_OUT
		txtTitle.setDocument(new TextFilter(NAME_SPEC, 10));
		txtFirstName.setDocument(new TextFilter(NAME_ALPHA_NUMERIC, 30));
		txtLastName.setDocument(new TextFilter(NAME_ALPHA_NUMERIC, 30));
		// MSB 02/10/2006
		// To resolve Double byte issues
		// we need to make sure text isn't filtered
		// and only length restriction is imposed on input.
		txtSuffix.setDocument(new CMSTextFilter(12));
		// txtSuffix.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 12));
		txtMidName.setDocument(new TextFilter(NAME_SPEC, 15));
		txtPrimaryEmail.setDocument(new CMSTextFilter(40));
		txt2ndEmail.setDocument(new CMSTextFilter(40));
		// txtPrimaryEmail.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 40));
		// txt2ndEmail.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 40));
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusOnFirstName() {
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
						txtFirstName.requestFocusInWindow();
					}
				});
			}
		});
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusOnLastName() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				txtLastName.requestFocusInWindow();
			}
		});
	}

	public void requestFocusOnTitle() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				txtTitle.requestFocusInWindow();
			}
		});
	}
	/**
	 * put your documentation comment here
	 */
	public void requestFocusOnPrimaryEmail() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				txtPrimaryEmail.requestFocusInWindow();
			}
		});
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusOnSecondaryEmail() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				txt2ndEmail.requestFocusInWindow();
			}
		});
	}

	/**
	 * put your documentation comment here
	 */
	public void setFocusOnTitle() {
		txtTitle.requestFocusInWindow();
	}

	public void setReturnMailEnabled(boolean bEnabled) {
		chbReturnMail.setEnabled(bEnabled);
	}
}
