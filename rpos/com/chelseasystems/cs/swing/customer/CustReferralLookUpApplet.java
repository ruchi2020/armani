/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-05-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06/01/2005 | Sameena   | 73        | Customer Search by phone does not work. Modified   |
 |      |            |           |           | the method 'findByPhone'                           |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 09-01-2005 | Manpreet  | N/A       | Changed adMgr.getCountryList() to                  |
 |      |            |           |           | adMgr.getAddressFormats()                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import com.chelseasystems.cs.swing.panel.CustomerListPanel;
import com.chelseasystems.cs.swing.panel.CustAdvanceSearchPanel;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.address.AddressMgr;
import java.awt.event.MouseAdapter;
import java.awt.event.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cs.customer.CustomerSearchString;
import com.chelseasystems.cs.employee.CMSEmployee;
import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import java.util.Vector;
import com.ga.fs.fsbridge.ARMFSBridge;



/**
 * <p>Title:CustomerLookupApplet </p>
 * <p>Description: Searches customer on provided search criteria</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CustReferralLookUpApplet extends CMSApplet {
	private CustomerListPanel pnlCustList;
	private CustAdvanceSearchPanel pnlAdvanceSearch;
	private JPanel pnlSearch;
	private JPanel pnlMainScreen;
	private RolodexLayout cardLayout;
	private JCMSLabel lblLastName;
	private JCMSTextField txtLastName;
	private JCMSLabel lblFirstName;
	private JCMSTextField txtFirstName;
	private JCMSLabel lblCity;
	private JCMSTextField txtCity;
	private JCMSLabel lblCountry;
	private JCMSComboBox cbxCountry;
	private JCMSLabel lblPostCode;
	private JCMSTextField txtPostCode;
	private final String QUERY_DELIMITER = "|";
	private CMSCustomer cmsCustomer;
	public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";

	/**
	 * put your documentation comment here
	 */
	public void stop() {
	}

	/**
	 * put your documentation comment here
	 */
	public void init() {
		try {
			jbInit();
			AddressMgr adMgr = new AddressMgr();
			Vector countryList = new Vector();
			countryList.addElement(" ");
			countryList.addAll(adMgr.getAddressFormats());
			cbxCountry.setModel(new DefaultComboBoxModel(countryList));
			pnlCustList.addMouseListener(new MouseAdapter() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() < 2 || pnlCustList.getSelectedCustomer() == null)
						return;
					applyReferral();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void applyReferral() {
		cmsCustomer = pnlCustList.getSelectedCustomer();
		theAppMgr.addStateObject("REFERRED_BY", cmsCustomer.getId());
	}

	/**
	 * put your documentation comment here
	 */
	public void start() {
		try {
			reset();
			theAppMgr.showMenu(MenuConst.CUST_REFERRAL_LOOKUP, theOpr);
			enter();
			this.addComponentListener(new java.awt.event.ComponentAdapter() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
				public void componentResized(ComponentEvent e) {
					resize();
				}
			});
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
							txtLastName.requestFocus();
						}
					});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showRegularSearch() {
		cardLayout.show(this, "REGULAR_SEARCH");
		theAppMgr.showMenu(MenuConst.CUST_REFERRAL_LOOKUP, theOpr);
		enter();
		reset();
	}

	private void showAdvanceSearch() {
		cardLayout.show(this, "ADVANCED_SEARCH");
		theAppMgr.showMenu(MenuConst.CUST_ADVANCED_SEARCH, "ADVANCED_SEARCH", theOpr);
		enterAdvancedSearch();
		reset();
		pnlAdvanceSearch.requestFocusToFamilyName();
	}

	/**
	 * put your documentation comment here
	 */
	private void reset() {
		pnlCustList.clear();
		pnlAdvanceSearch.reset();
		txtLastName.setText("");
		txtFirstName.setText("");
		txtCity.setText("");
		txtPostCode.setText("");
		cbxCountry.setSelectedIndex(0);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterAdvancedSearch() {
		theAppMgr.setSingleEditArea(res.getString("Select Option"));
	}

	/**
	 * put your documentation comment here
	 */
	private void enter() {
		theAppMgr.setSingleEditArea(res.getString("Enter customer information and press 'Search'"));
	}

	/**
	 * put your documentation comment here
	 */
	private void enterCustPhone() {
		theAppMgr.setSingleEditArea(res.getString("Enter customer phone number"), "CUST_PHONE");
	}

	/**
	 * put your documentation comment here
	 */
	private void enterCustNo() {
		theAppMgr.setSingleEditArea(res.getString("Enter customer number"), "CUST_NO");
	}

	private void enterCustomerBarcode() {
		theAppMgr.setSingleEditArea(res.getString("Enter Customer Barcode"), "CUST_BARCODE");
	}

	/**
	 * put your documentation comment here
	 */
	private void enterRewardCard() {
		theAppMgr.setSingleEditArea(res.getString("Swipe or enter customer reward card"), "REWARD_CARD");
	}

	/**
	 * put your documentation comment here
	 * @param sCommand
	 * @param sInput
	 */
	public void editAreaEvent(String sCommand, String sInput) {
		if (sCommand.equals("CUST_PHONE")) {
			if (sInput == null || sInput.trim().length() < 1 || sInput.trim().length() > 20) {
				theAppMgr.showErrorDlg(res.getString("Phone Number not valid"));
				enterCustPhone();
				return;
			} else {
				findByPhone(sInput);
			}
		} else if (sCommand.equals("CUST_NO")) {
			if (sInput == null || sInput.trim().length() < 1 || sInput.trim().length() > 20) {
				theAppMgr.showErrorDlg(res.getString("Customer Number not valid"));
				enterCustNo();
				return;
			} else {
				findByID(sInput);
			}
		} else if (sCommand.equals("REWARD_CARD")) {
			if (sInput == null || sInput.trim().length() < 1) {
				theAppMgr.showErrorDlg(res.getString("Reward Card not valid"));
				enterRewardCard();
				return;
			} else {
				findByRewardCard(sInput);
			}
		} else if (sCommand.equals("CUST_BARCODE")) {
			if (sInput == null || sInput.trim().length() < 1) {
				theAppMgr.showErrorDlg(res.getString("Barcode not valid"));
				enterCustomerBarcode();
				return;
			} else {
				findByBarcode(sInput);
			}
		}

	}

	public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sHeader.equals("ADVANCED_SEARCH")) {
			if (sAction.equals("SEARCH")) {
				findByAdvanceSearchQuery();
			} else if (sAction.equals("NEW_SEARCH")) {
				pnlAdvanceSearch.reset();
				pnlAdvanceSearch.requestFocusToFamilyName();
			} else if (sAction.equals("PREV")) {
				anEvent.consume();
				showRegularSearch();
				txtLastName.requestFocus();
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		String sQuery = "";
		if (sAction.equals("SEARCH")) {
			pnlCustList.clear();
			if (txtLastName.getText() == null) {
				theAppMgr.showErrorDlg(res.getString("Last Name can't be blank"));
				txtLastName.requestFocus();
				return;
			}
			if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
				if (txtLastName.getText().trim().length() < 2) {
					theAppMgr.showErrorDlg(res.getString("Last Name can't be less than 2 characters"));
					txtLastName.requestFocus();
					return;
				}
			} else {
				if (txtLastName.getText().trim().length() < 1) {
					theAppMgr.showErrorDlg(res.getString("Last Name can't be less than 1 character"));
					txtLastName.requestFocus();
					return;
				}
			}
			sQuery = txtLastName.getText().trim() + QUERY_DELIMITER;
			// Fix for 1251: Customer Profile Search yields different results when searching in "Referred By"
			if (txtFirstName.getText() != null && txtFirstName.getText().trim().length() > 0) {
				sQuery += txtFirstName.getText().trim() + QUERY_DELIMITER;
			} else {
				sQuery += " " + QUERY_DELIMITER;
			}
			sQuery += cbxCountry.getSelectedItem() + QUERY_DELIMITER;
			if (txtCity.getText() != null && txtCity.getText().trim().length() > 0) {
				sQuery += txtCity.getText().trim() + QUERY_DELIMITER;
			} else {
				sQuery += " " + QUERY_DELIMITER;
			}
			if (txtPostCode.getText() != null && txtPostCode.getText().trim().length() > 0) {
				sQuery += txtPostCode.getText().trim() + QUERY_DELIMITER;
			} else {
				sQuery += " " + QUERY_DELIMITER;
			}
			processQuery(sQuery);
		} else if (sAction.equals("CUST_PHONE")) {
			enterCustPhone();
		} else if (sAction.equals("CUST_NO")) {
			enterCustNo();
		} else if (sAction.equals("CUST_BARCODE")) {
			enterCustomerBarcode();
			anEvent.consume();
		} else if (sAction.equals("REWARD_CARD")) {
			enterRewardCard();
		} else if (sAction.equals("ADVANCED_SEARCH")) {
			showAdvanceSearch();
		} else if (sAction.equals("PREV")) {
			theAppMgr.addStateObject("REFERRED_BY", "");
		} else if (sAction.equals("OK")) {
			if (pnlCustList.getSelectedRowIndex() < 0) {
				theAppMgr.showErrorDlg(res.getString("Please select a customer to continue"));
				txtLastName.requestFocus();
				anEvent.consume();
				return;
			}
			cmsCustomer = pnlCustList.getSelectedCustomer();
			applyReferral();
			// reset();
		}
	}

	/**
	 * put your documentation comment here
	 * @param sCardNumber
	 */
	private void findByRewardCard(String sCardNumber) {
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findByRewardCard(theAppMgr, sCardNumber);
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				return;
			}
			enter();
			loadCustomers(cmsCustomer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lookup customer by Barcode
	 * @param sBarcode
	 *            String
	 */
	private void findByBarcode(String sBarcode) {
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findByBarcode(theAppMgr, sBarcode);
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				return;
			}
			enter();
			loadCustomers(cmsCustomer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @param sCustID
	 */
	private void findByID(String sCustID) {
		try {
			CMSCustomer cmsCustomer = (CMSCustomer) CMSCustomerHelper.findById(theAppMgr, sCustID.toUpperCase());
			if (cmsCustomer == null) {
				showNoCustFound();
				return;
			}
			enter();
			pnlCustList.clear();
			pnlCustList.addCustomer(cmsCustomer);
			pnlCustList.sortCustomer(cmsCustomer.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void showNoCustFound() {
		theAppMgr.showErrorDlg(res.getString("Sorry, no customer found"));
	}

	/**
	 * put your documentation comment here
	 * @param sPhone
	 */
	private void findByPhone(String sPhone) {
		Telephone tele;
		try {
			// SP: modified the code to create a telephone object with no country
			// and area code values
			tele = new Telephone(new TelephoneType("Cellular"), "", "", sPhone);
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString("Phone Number not valid"));
			enterCustPhone();
			return;
		}
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findByTelephone(theAppMgr, tele);
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				return;
			}
			enter();
			loadCustomers(cmsCustomer);
		} catch (TooManySearchResultsException e) {
			theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
			enter();
			txtLastName.requestFocus();
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString("Exception loading customer"));
			e.printStackTrace();
		}
	}

	private void findByAdvanceSearchQuery() {

		if (pnlAdvanceSearch.getFamilyName().length() < 1 && pnlAdvanceSearch.getFirstName().length() < 1 && pnlAdvanceSearch.getAddressLine1().length() < 1 && pnlAdvanceSearch.getCity().length() < 1
				&& pnlAdvanceSearch.getState().length() < 1 && pnlAdvanceSearch.getZipCode().length() < 1 && pnlAdvanceSearch.getPhone().length() < 1 && pnlAdvanceSearch.getFiscalCode().length() < 1
				&& pnlAdvanceSearch.getVATNumber().length() < 1 && pnlAdvanceSearch.getDocumentNumber().length() < 1) {
			pnlAdvanceSearch.requestFocusToFamilyName();
			theAppMgr.showErrorDlg(res.getString("Please enter atleast one of the values"));
			return;
		}

		CustomerSearchString custSrchString = new CustomerSearchString();
		custSrchString.setLastName(pnlAdvanceSearch.getFamilyName());
		custSrchString.setFirstName(pnlAdvanceSearch.getFirstName());
		custSrchString.setAddressLine1(pnlAdvanceSearch.getAddressLine1());
		custSrchString.setCity(pnlAdvanceSearch.getCity());
		custSrchString.setState(pnlAdvanceSearch.getState());
		custSrchString.setZipCode(pnlAdvanceSearch.getZipCode());
		custSrchString.setCountry(pnlAdvanceSearch.getCountry());
		custSrchString.setPhoneNumber(pnlAdvanceSearch.getPhone());
		custSrchString.setFiscalReceiptNumber(pnlAdvanceSearch.getFiscalCode());
		custSrchString.setVATNumber(pnlAdvanceSearch.getVATNumber());
		custSrchString.setDocumentNumber(pnlAdvanceSearch.getDocumentNumber());

		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findBySearchQuery(theAppMgr, custSrchString);
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				pnlAdvanceSearch.requestFocusToFamilyName();
				return;
			}
			showRegularSearch();
			loadCustomers(cmsCustomer);
		} catch (TooManySearchResultsException e) {
			theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
			showAdvanceSearch();
			pnlAdvanceSearch.requestFocusToFamilyName();
		} catch (Exception e) {
			e.printStackTrace();
			showAdvanceSearch();
			showNoCustFound();
			pnlAdvanceSearch.requestFocusToFamilyName();
		}

	}

	private void loadCustomers(CMSCustomer cmsCustomer[]) {
		pnlCustList.clear();
		for (int iCtr = 0; iCtr < cmsCustomer.length; iCtr++) {
			pnlCustList.addCustomer(cmsCustomer[iCtr]);
		}
		pnlCustList.sortCustomer(null);
	}

	/**
	 * put your documentation comment here
	 * @param sQuery
	 */
	private void processQuery(String sQuery) {
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findBySearchQuery(theAppMgr, sQuery);
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				txtLastName.requestFocus();
				return;
			}
			loadCustomers(cmsCustomer);
			// Issue # 965
			pnlCustList.selectFirstRow();
		} catch (TooManySearchResultsException e) {
			theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
			txtLastName.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
			showNoCustFound();
			txtLastName.requestFocus();
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getVersion() {
		return ("$Revision: 1.1 $");
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getScreenName() {
		return "Referral Lookup";
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		pnlCustList = new CustomerListPanel(((CMSStore) theStore).getPreferredISOCountry());
		pnlAdvanceSearch = new CustAdvanceSearchPanel();
		pnlSearch = new JPanel();
		lblLastName = new JCMSLabel();
		txtLastName = new JCMSTextField();
		lblFirstName = new JCMSLabel();
		txtFirstName = new JCMSTextField();
		lblCity = new JCMSLabel();
		txtCity = new JCMSTextField();
		lblCountry = new JCMSLabel();
		cbxCountry = new JCMSComboBox();
		lblPostCode = new JCMSLabel();
		txtPostCode = new JCMSTextField();
		cardLayout = new RolodexLayout();
		pnlMainScreen = new JPanel();
		setLayout(cardLayout);
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		BorderLayout borderLayout1 = new BorderLayout();
		pnlMainScreen.setLayout(borderLayout1);
		pnlMainScreen.add(pnlSearch, BorderLayout.NORTH);
		pnlMainScreen.add(pnlCustList, BorderLayout.CENTER);
		pnlSearch.setPreferredSize(new Dimension(833, 100));
		pnlAdvanceSearch.setPreferredSize(new Dimension(833, 100));
		pnlSearch.setLayout(gridBagLayout1);
		lblLastName.setLabelFor(txtLastName);
		lblLastName.setText(res.getString("Last Name"));
		txtLastName.setText("");
		lblFirstName.setLabelFor(txtFirstName);
		lblFirstName.setText(res.getString("First Name"));
		txtFirstName.setText("");
		lblCity.setLabelFor(txtCity);
		lblCity.setText(res.getString("City"));
		txtCity.setText("");
		lblCountry.setLabelFor(cbxCountry);
		lblCountry.setText(res.getString("Country"));
		lblPostCode.setLabelFor(txtPostCode);
		lblPostCode.setText(res.getString("Post Code"));
		txtPostCode.setText("");
		this.setBackground(theAppMgr.getBackgroundColor());
		pnlSearch.setBackground(theAppMgr.getBackgroundColor());
		pnlMainScreen.setBackground(theAppMgr.getBackgroundColor());
		pnlCustList.setAppMgr(theAppMgr);
		lblLastName.setAppMgr(theAppMgr);
		lblLastName.setFont(theAppMgr.getTheme().getLabelFont());
		txtLastName.setAppMgr(theAppMgr);
		txtLastName.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblFirstName.setAppMgr(theAppMgr);
		lblFirstName.setFont(theAppMgr.getTheme().getLabelFont());
		txtFirstName.setAppMgr(theAppMgr);
		txtFirstName.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblCity.setAppMgr(theAppMgr);
		lblCity.setFont(theAppMgr.getTheme().getLabelFont());
		txtCity.setAppMgr(theAppMgr);
		txtCity.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblCountry.setAppMgr(theAppMgr);
		lblCountry.setFont(theAppMgr.getTheme().getLabelFont());
		cbxCountry.setAppMgr(theAppMgr);
		lblPostCode.setAppMgr(theAppMgr);
		lblCountry.setFont(theAppMgr.getTheme().getLabelFont());
		txtPostCode.setAppMgr(theAppMgr);
		txtPostCode.setFont(theAppMgr.getTheme().getTextFieldFont());
		pnlAdvanceSearch.setAppMgr(theAppMgr);

		this.add(pnlMainScreen, "REGULAR_SEARCH");
		this.add(pnlAdvanceSearch, "ADVANCED_SEARCH");
		/*
		   public GridBagConstraints(int gridx, int gridy, 
           int gridwidth, int gridheight, 
           double weightx, double weighty, 
           int anchor, int fill, 
           Insets insets, int ipadx, int ipady) { 
           public Insets(int top, int left, int bottom, int right) {
		 */
		pnlSearch.add(lblLastName, 
			new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(19, 6, 0, 0), 14, 4));
		pnlSearch.add(txtLastName, 
			new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(19, 0, 0, 0), 172, 4));
		pnlSearch.add(lblCity, 
			new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(18, 6, 35, 0), 17, 4));
		pnlSearch.add(txtCity, 
			new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(17, 0, 35, -35), 141, 4));
		pnlSearch.add(lblFirstName, 
			new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(19, 22, 0, 0), 3, 4));
		pnlSearch.add(txtFirstName, 
			new GridBagConstraints(5, 0, 3, 1, 1.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(19, 0, 0, 19), 197, 4));
		pnlSearch.add(lblCountry, 
			new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(17, 95, 35, 0), 17, 4));
		pnlSearch.add(cbxCountry, 
			new GridBagConstraints(4, 1, 2, 1, 1.0, 0.0, 
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(17, 0, 35, 0), -1, -1));
		pnlSearch.add(lblPostCode, 
			new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(17, 7, 35, 0), 15, 4));
		pnlSearch.add(txtPostCode, 
			new GridBagConstraints(7, 1, 1, 1, 1.0, 0.0, 
			GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(16, 0, 35, 28), 78, 4));
		txtLastName.setDocument(new TextFilter(NAME_SPEC, 30));
		txtFirstName.setDocument(new TextFilter(NAME_SPEC, 30));
	}

	/**
	 * put your documentation comment here
	 */
	public void resize() {
		double r = com.chelseasystems.cr.swing.CMSApplet.r;
		lblLastName.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblLastName.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblLastName.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtLastName.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtLastName.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtLastName.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblFirstName.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblFirstName.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblFirstName.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtFirstName.setPreferredSize(new Dimension((int) (75 * r), (int) (25 * r)));
		txtFirstName.setMaximumSize(new Dimension((int) (75 * r), (int) (25 * r)));
		txtFirstName.setMinimumSize(new Dimension((int) (75 * r), (int) (25 * r)));
		lblCity.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblCity.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblCity.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtCity.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtCity.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtCity.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblCountry.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblCountry.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblCountry.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		cbxCountry.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		cbxCountry.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		cbxCountry.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblPostCode.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblPostCode.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		lblPostCode.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtPostCode.setPreferredSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtPostCode.setMaximumSize(new Dimension((int) (65 * r), (int) (25 * r)));
		txtPostCode.setMinimumSize(new Dimension((int) (65 * r), (int) (25 * r)));
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageDown(MouseEvent e) {
		int selectedRow = pnlCustList.getAddressModel().getAllRows().indexOf(pnlCustList.getSelectedCustomer());
		if (selectedRow < 0)
			selectedRow = pnlCustList.getAddressModel().getLastSelectedCustomerRow();
		pnlCustList.nextPage();
		theAppMgr.showPageNumber(e, pnlCustList.getCurrentPageNumber() + 1, pnlCustList.getTotalPages());
		pnlCustList.selectRowIfInCurrentPage(selectedRow);
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageUp(MouseEvent e) {
		int selectedRow = pnlCustList.getAddressModel().getAllRows().indexOf(pnlCustList.getSelectedCustomer());
		if (selectedRow < 0)
			selectedRow = pnlCustList.getAddressModel().getLastSelectedCustomerRow();
		pnlCustList.prevPage();
		theAppMgr.showPageNumber(e, pnlCustList.getCurrentPageNumber() + 1, pnlCustList.getTotalPages());
		pnlCustList.selectRowIfInCurrentPage(selectedRow);
	}

	/**
	 * MP: Home pressed at customer display exits transaction with no message
	 * @return
	 */
	public boolean isHomeAllowed() {
		
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}

		/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the Sign
		 * Off event for Fiscal Solutions Service START--
		 */
		boolean goToHomeView = (theAppMgr
				.showOptionDlg(
						res.getString("Cancel Transaction"),
						res.getString("Are you sure you want to cancel this transaction?")));
		if (goToHomeView) {

			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr );
		}

		return goToHomeView;

		/*
		 * --END
		 */

	}
}
