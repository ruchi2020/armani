/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 8    | 09-01-2005 | Manpreet  | N/A       | Changed adMgr.getCountryList() to                  |
 |      |            |           |           | adMgr.getAddressFormats()                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 07-15-2005 | Vikram    | 522       | If Customer is already attached, it should be      |
 |      |            |           |           | listed in customer lookup by default.              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 06-13-2005 | Vikram    | 167       | LASTNAME+(POST CODE or CITY) combination is not    |
 |      |            |           |           | working properly                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 06/08/2005 | Vikram    | 122       | Return Item: Focus lost on cursor after clearing   |
 |      |            |           |           | error in Return                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05/31/2005 | Sameena   | 73        | Customer Search by phone does not work. Modified   |
 |      |            |           |           | the method 'findByPhone'                             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 05/17/2005 | Vikram    | N/A       | Updated for Escape Key                             |
 --------------------------------------------------------------------------------------------------
 | 2    | 04/12/2005 | Anand     | N/A       | PCR related changes 13,14,15,26,28                 |
 --------------------------------------------------------------------------------------------------
 | 1    | 03-05-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.armani.business.rules.ARMCustomerBR;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.RedeemableBuyBackTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.CMSInputVerifier;
import com.chelseasystems.cr.swing.CMSSwingConstants;
import com.chelseasystems.cr.swing.IFocusTraversable;
import com.chelseasystems.cr.swing.IValidatable;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cr.swing.bean.JCMSComboBox;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cr.telephone.Telephone;
import com.chelseasystems.cr.telephone.TelephoneType;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.address.AddressMgr;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerAlertRule;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cs.customer.CMSVIPMembershipDetail;
import com.chelseasystems.cs.customer.CustomerSearchString;
import com.chelseasystems.cs.customer.TooManySearchResultsException;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.discount.CMSDiscountMgr;
import com.chelseasystems.cs.discount.CMSVIPDiscount;
import com.chelseasystems.cs.discount.CMSVIPDiscountMgr;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransaction;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.CustAdvanceSearchPanel;
import com.chelseasystems.cs.swing.panel.CustEmployeeSearchPanel;
import com.chelseasystems.cs.swing.panel.CustomerListPanel;
import com.chelseasystems.cs.swing.panel.CustomerListPanel_JP;
import com.chelseasystems.cs.util.Version;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 * Title:CustomerLookupApplet
 * Description: Searches customer on provided search criteria
 * Copyright: Copyright (c) 2005
 * Company: SkillnetInc
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class CustomerLookupApplet extends CMSApplet {
	private CustomerListPanel pnlCustList;
	private CustAdvanceSearchPanel pnlAdvanceSearch;
	private JPanel pnlSearch;
	private JPanel pnlMainScreen;
	private RolodexLayout cardLayout;
	private RolodexLayout searchPanelCardLayout;
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
//	1922
	private JCMSLabel lblDocNumber;
	private JCMSTextField txtDocNumber;
	
	private final String QUERY_DELIMITER = "|";
	private CMSCustomer cmsCustomer;
	private CMSEmployee theOperator;
	protected int passwordWrongCount;
	private CustEmployeeSearchPanel pnlCustEmpSearch;
	private JPanel pnlMainScreenNorth;
	
	public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-'";
	//added by shushma to provide space in name while customer lookup
	public static final String NAME_SPEC_US = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";
	//change closed
	private static final String CONFIGURATION_FILE = "customer.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	private String custLookupAppletString = configMgr.getString("CUSTOMER.LOOKUP_APPLET");
	private String customerListPanelString = configMgr.getString("CUSTOMER_LIST_PANEL");
	private String prepopulateSearchCriteria = configMgr.getString("PREPOPULATE_SEARCH_CRITERIA");
	private TextFieldFocusAdapter focusAdapter = new TextFieldFocusAdapter();

    //added for PCR -  VIP membership by vivek sawant
	 private CMSCompositePOSTransaction theTxn;
	 private CMSVIPMembershipDetail membershipDetail = null;
	 private CMSVIPDiscount discount;
	 private double discount_pct;
	 private boolean empFlag = true;
	/**
	 * put your documentation comment here
	 */
	public void stop() {
		// reset();
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
			//Added by Vivek Mishra for adding default country in JAPAN region
			if ("JP".equalsIgnoreCase(Version.CURRENT_REGION))		
			cbxCountry.setSelectedItem(adMgr.getDefaultAddressFormat());
			//End
			cbxCountry.setAppMgr(theAppMgr);
			pnlCustList.addMouseListener(new MouseAdapter() {

				/**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() < 2)
						return;
					cmsCustomer = pnlCustList.getSelectedCustomer();
					if (cmsCustomer == null)
						return;
					if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
							//Added by Vivek Mishra for PCR : Dummy Customer Association JAPAN
							||("JP".equalsIgnoreCase(Version.CURRENT_REGION))) 
					        //End
					{
						if (ARMCustomerBR.isDummy(cmsCustomer.getId())
								|| ARMCustomerBR.isDefault(cmsCustomer.getId())) {
							theAppMgr.showErrorDlg(res
									.getString("Unable to view details for Dummy or Default customer"));
							return;
						}
					}
					if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
						if (ARMCustomerBR.isDefault(cmsCustomer.getId())) {
							theAppMgr.showErrorDlg(res
									.getString("Unable to view details for Default customer"));
							return;
						}
					}
					//Added for displaying Date as Field in Europe by Vivek
					if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						if(theAppMgr.isOnLine()){
							Date createDt = null;
							try {
								createDt = CMSCustomerHelper.getCustomerCreatationDate(theAppMgr, cmsCustomer.getId());
							} catch (Exception ex) {
								ex.printStackTrace();
							}if(createDt!= null){
							String createdDate = formatter.format(createDt);
							theAppMgr.addStateObject("CREATED_DATE", createdDate);
							}else{
								theAppMgr.addStateObject("CREATED_DATE", formatter.format(new Date()));
							}
						}else{
							theAppMgr.addStateObject("CREATED_DATE", formatter.format(new Date()));
						}
						
					}

					customerSelected(cmsCustomer);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param cmsCustomer
	 */
	public void customerSelected(CMSCustomer cmsCustomer) {
		if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
			if (!cmsCustomer.testIfAddressViewable(theStore.getId())) {
				theAppMgr
						.showErrorDlg(res
								.getString("VIP Customer details not viewable to non-owning stores. Customer must be first registered in this store."));
				return;
			}
		}
		if (theAppMgr.getStateObject("OPERATOR") == null
				|| ((CMSEmployee) theAppMgr.getStateObject("OPERATOR")).getExternalID().length() < 1)
			return;
		theAppMgr.addStateObject("CUSTOMER_LOOKUP", cmsCustomer);
		theAppMgr.addStateObject("CUST_MGMT_MODE", "MODIFY");
		theAppMgr.fireButtonEvent("LOAD_MGMT");
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param submitCust
	 */
	void updatePanelWithCustomer(CMSCustomer submitCust) {
		Vector allRows = pnlCustList.getAddressModel().getAllRows();
		if (allRows.size() < 0 || submitCust == null)
			return; // New customer case or no change
		// Modified case
		CMSCustomer[] newArray = new CMSCustomer[allRows.size()];
		for (int i = 0; i < allRows.size(); i++) {
			if (((CMSCustomer) allRows.elementAt(i)).getId().equals(submitCust.getId())) {
				newArray[i] = submitCust;
			} else {
				newArray[i] = (CMSCustomer) allRows.elementAt(i);
			}
		}
		// Refresh the Panel
		this.loadCustomers(newArray);
		pnlCustList.sortCustomer(submitCust.getId());
	}

	/**
	 * put your documentation comment here
	 */
	public void start() {
		try {
  // vishal : for customer_id prefix in europe for FRANCHISING store
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) theAppMgr
						.getStateObject("TXN_POS");
				if(theTxn!=null){
				ConfigMgr configMgr = new ConfigMgr("customer.cfg");
				if(configMgr.getString("FRANCHISING_SHOP")!=null){
					String FRANCHISING_SHOP=configMgr.getString("FRANCHISING_SHOP");
					if(FRANCHISING_SHOP.toLowerCase().startsWith("y")||FRANCHISING_SHOP.equalsIgnoreCase("y")){
						theTxn.setFranchising_Store(true);
					}
					}
					if(configMgr.getString("STAGING_CUSTID")!=null){
						String STAGING_CUSTID=configMgr.getString("STAGING_CUSTID");
						if(STAGING_CUSTID.toLowerCase().startsWith("y")||STAGING_CUSTID.equalsIgnoreCase("y")){
							theTxn.setStaging_cust_id(true);
						}
				}
			}
			}
			//end vishal 19 sept 2016
			if (theAppMgr.getStateObject("DEFAULT_SEARCH_LIST") != null) {
				showEuropeSearch();
				loadCustomers((CMSCustomer[]) theAppMgr.getStateObject("DEFAULT_SEARCH_LIST"));
				theAppMgr.removeStateObject("DEFAULT_SEARCH_LIST");
				pnlCustList.selectFirstRow();
				return;
			}
			if (theAppMgr.getStateObject("CUST_SEARCH") != null) {
				theAppMgr.removeStateObject("CUST_SEARCH");
			}
			if (theAppMgr.getStateObject("CUSTOMER_LOOKUP") != null) {
				CMSCustomer cust = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_LOOKUP");
				pnlCustList.sortCustomer(cust.getId());
				theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
			} else {
				//Added by Vivek Mishra to resolve bug : 9487 EUROPE region
				//Applet not properly refreshed when the button Previous was pressed
				if("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
						{
				           showEuropeSearch();
				           setFocusOnLastName();
						}
				//End
				reset();
			}
			if (theAppMgr.getStateObject("CUSTOMER_SUBMIT") != null) {
				CMSCustomer submittedCust = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_SUBMIT");
				// Replace this object in the Panel
				updatePanelWithCustomer(submittedCust);
				theAppMgr.removeStateObject("CUSTOMER_SUBMIT");
			}
			if (theAppMgr.getStateObject("OPERATOR") == null) {
				if (theAppMgr.getStateObject("ARM_CUST_REQUIRED") != null)
					theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_INIT_OK, theOpr);
				else
					theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_INIT, theOpr);
			} else {
				if (theAppMgr.getStateObject("ARM_DIRECTED_FROM") != null
						&& (theAppMgr.getStateObject("ARM_DIRECTED_FROM").equals("VIEW_TXN_APPLET") || theAppMgr
								.getStateObject("ARM_DIRECTED_FROM").equals("RETURN_TXN_HIST_APPLET")))
					theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_INIT_OK, theOpr);
				else
					theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_SALE, theOpr);
			}
			enter();
			if (theAppMgr.getStateObject("NEW_CUSTOMER") != null) {
				cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("NEW_CUSTOMER");
				pnlCustList.addCustomer(cmsCustomer);
				theAppMgr.removeStateObject("NEW_CUSTOMER");
				pnlCustList.sortCustomer(cmsCustomer.getId());
			} else if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
				cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
				boolean toAdd = true;
				for (int i = 0; i < pnlCustList.getNoOfRows(); i++) {
					CMSCustomer custAti = pnlCustList.getCustomerAt(i);
					if (custAti.getId().equals(cmsCustomer.getId())) {
						toAdd = false;
					}
				}
				if (toAdd)
					pnlCustList.addCustomer(cmsCustomer);
				// theAppMgr.removeStateObject("TXN_CUSTOMER");
				pnlCustList.sortCustomer(cmsCustomer.getId());
			} else {
				PaymentTransaction txn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
				if (txn != null) {
					if (txn instanceof CompositePOSTransaction) {
						cmsCustomer = (CMSCustomer) ((CompositePOSTransaction) txn).getCustomer();
					} else if (txn instanceof CMSMiscCollection) {
						cmsCustomer = (CMSCustomer) ((CMSMiscCollection) txn).getCustomer();
					} else if (txn instanceof CMSRedeemableBuyBackTransaction) {
						cmsCustomer = (CMSCustomer) ((CMSRedeemableBuyBackTransaction) txn).getCustomer();
					}
					//Anjana added for paid out transaction Mail check for customer tombe added properly
					 else if (txn instanceof CMSMiscPaidOut) {
						cmsCustomer = (CMSCustomer) ((CMSMiscPaidOut) txn).getCustomer();
						} 
					if (cmsCustomer != null) {
						boolean toAdd = true;
						for (int i = 0; i < pnlCustList.getNoOfRows(); i++) {
							CMSCustomer custAti = pnlCustList.getCustomerAt(i);
							if (custAti.getId().equals(cmsCustomer.getId())) {
								toAdd = false;
							}
						}
						if (toAdd)
							pnlCustList.addCustomer(cmsCustomer);
						pnlCustList.sortCustomer(cmsCustomer.getId());
					}
				}
			}
			this.addComponentListener(new java.awt.event.ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					resize();
				}
			});
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					txtLastName.requestFocusInWindow();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setFocusOnLastName() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtLastName.setEditable(true);
				txtLastName.requestFocusInWindow();
			}
		});
	}
	
	//start code changes for issue #1922
	private void setFocusOnDocumentNumber(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtDocNumber.setEditable(true);
				txtDocNumber.requestFocusInWindow();
			}
		});
	}
    //end code changes for issue # 1922
	
	private void showRegularSearch() {
		cardLayout.show(this, "REGULAR_SEARCH");
		searchPanelCardLayout.show(pnlMainScreenNorth, "SEARCH");
		if (theAppMgr.getStateObject("OPERATOR") == null) {
			theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_INIT, theOpr);
		} else {
			theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_SALE, theOpr);
		}
		enter();
		reset();
	}

	private void showEuropeSearch() {
		cardLayout.show(this, "REGULAR_SEARCH");
		searchPanelCardLayout.show(pnlMainScreenNorth, "SEARCH");
		if (theAppMgr.getStateObject("OPERATOR") == null) {
			// Fix for 1619: Inquires> "OK" button is missing, when do customer search
			theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_INIT_OK, theOpr);
		} else {
			theAppMgr.showMenu(MenuConst.CUSTOMER_LOOKUP_SALE, theOpr);
		}
	}

	private void showAdvanceSearch() {
		cardLayout.show(this, "ADVANCED_SEARCH");
		theAppMgr.showMenu(MenuConst.CUST_ADVANCED_SEARCH, "ADVANCED_SEARCH", theOpr);
		enterAdvancedSearch();
		reset();
		pnlAdvanceSearch.requestFocusToFamilyName();
	}

	private void showEmployeeSearch() {
		cardLayout.show(this, "REGULAR_SEARCH");
		searchPanelCardLayout.show(pnlMainScreenNorth, "EMPLOYEE_SEARCH");
		theAppMgr.showMenu(MenuConst.CUST_ADVANCED_SEARCH, "EMPLOYEE_SEARCH", theOpr);
		enterEmployeeSearch();
		reset();
		pnlCustEmpSearch.requestFocusToFamilyName();
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
		if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
		txtDocNumber.setText("");
		
		//Added by Vivek Mishra for adding default country in JAPAN region
		if ("JP".equalsIgnoreCase(Version.CURRENT_REGION))
		{
			AddressMgr adMgr = new AddressMgr();
			cbxCountry.setSelectedItem(adMgr.getDefaultAddressFormat());
		
		}
		else
		//End
		cbxCountry.setSelectedIndex(0);
		pnlCustEmpSearch.reset();
	}

	/**
	 * put your documentation comment here
	 */
	private void enterAdvancedSearch() {
		theAppMgr.setSingleEditArea(res.getString("Select Option"));
	}

	private void enterEmployeeSearch() {
		theAppMgr.setSingleEditArea(res.getString("Enter employee information and press 'Search'"));
	}

	private void enterManagerID() {
		theAppMgr.setSingleEditArea(res.getString("Enter Manager ID"), "MANAGER_ID",
				theAppMgr.PASSWORD_MASK);
	}

	private void enterManagerPWD() {
		theAppMgr.setSingleEditArea(res.getString("Enter Manager password"), "MANAGER_PWD",
				theAppMgr.PASSWORD_MASK);
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
		theAppMgr
				.setSingleEditArea(res.getString("Swipe or enter customer reward card"), "REWARD_CARD");
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sCommand
	 * @param sInput
	 */
	public void editAreaEvent(String sCommand, String sInput) {
		if (sCommand.equals("MANAGER_ID")) {
			try {
				if (sInput == null || sInput.trim().length() < 1) {
					theAppMgr.showErrorDlg(res.getString("No matching employee was found."));
					enterManagerID();
					return;
				}
				theOperator = CMSEmployeeHelper.findByExternalId(theAppMgr, sInput);
				if (theOperator == null) {
					theAppMgr.showErrorDlg(res.getString("No matching employee was found."));
					enterManagerID();
					return;
				}
				if (!theOperator.hasAnyRoleFromRoles(1)) {
					theAppMgr.showErrorDlg(res.getString("Employee is not a manager."));
					enterManagerID();
					return;
				}
				passwordWrongCount = 0;
				enterManagerPWD();
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
				return;
			}
		} else if (sCommand.equals("MANAGER_PWD")) {
			if (!processPasswordEntry(sInput)) {
				if (passwordWrongCount < 4) {
					enterManagerPWD();
				} else {
					passwordWrongCount = 0;
					enterManagerID();
				}
			} else {
				proceedToAssociateScreen();
			}
		} else if (sCommand.equals("CUST_PHONE")) {
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
			String Builder = CMSDiscountMgr.getBuilderName("REWARD_DISCOUNT");
			theAppMgr.buildObject(this, "REWARD_CARD", Builder, "");
		} else if (sCommand.equals("CUST_BARCODE")) {
			if (sInput == null || sInput.trim().length() < 1) {
				theAppMgr.showErrorDlg(res.getString("Barcode not valid"));
				enterCustomerBarcode();
				return;
			} else {
				if (sInput.trim().length() == 13)
					sInput = sInput.substring(0, 12);
				findByBarcode(sInput);
			}
		}

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param Command
	 * @param obj
	 */
	public void objectEvent(String Command, Object obj) {
		if (Command.equals("REWARD_CARD")) {
			try {
				if (obj != null) {
					findByRewardCard(obj.toString());
				} else if (obj == null) {
					String Builder = CMSDiscountMgr.getBuilderName("REWARD_DISCOUNT");
					theAppMgr.buildObject(this, "REWARD_CARD", Builder, "");
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
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
			}
		} else if (sHeader.equals("EMPLOYEE_SEARCH")) {
			if (sAction.equals("SEARCH")) {
				findByEmployeeSearchQuery();
			} else if (sAction.equals("NEW_SEARCH")) {
				pnlCustEmpSearch.reset();
				pnlCustEmpSearch.requestFocusToFamilyName();
			}
		}
		if (sAction.equals("PREVIOUS")) {
			anEvent.consume();
			//theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
			//theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr, null);
			// theAppMgr.goBack();
			//showRegularSearch();
			// txtLastName.requestFocusInWindow();
			//setFocusOnLastName();
		}
	}

	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		String sQuery = "";

		if (sAction.equals("REGISTER_VIP")) {
			anEvent.consume();
			cmsCustomer = pnlCustList.getSelectedCustomer();
			if (cmsCustomer == null) {
				theAppMgr.showErrorDlg(res.getString("Please select a customer to continue"));
				// txtLastName.requestFocusInWindow();
				setFocusOnLastName();
				return;
			}
			if (!cmsCustomer.isVIPCustomer()) {
				theAppMgr.showErrorDlg(res.getString("Customer is not VIP."));
				// TD
				// txtLastName.requestDefaultFocus();
				// txtLastName.requestFocusInWindow();
				setFocusOnLastName();
				return;
			}
			if (cmsCustomer.testIfAddressViewable(theStore.getId())) {
				theAppMgr.showErrorDlg(res.getString("Customer already registered with store."));
				// Check whether this works or not.......
				// TD
				// txtLastName.requestDefaultFocus();
				// txtLastName.requestFocusInWindow();
				setFocusOnLastName();
				return;
			}
			registerVIP();
		} else if (sAction.equals("NEW_CUST")) {
			theAppMgr.addStateObject("CUST_MGMT_MODE", "NEW");
			theAppMgr.addStateObject("NEW_CUSTOMER", "NEW_CUSTOMER");
			//Added for displaying Date as Field in Europe by Vivek
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				theAppMgr.addStateObject("CREATED_DATE", formatter.format(new Date()));
			}

		}
		// Added Dummy Customer
		else if (sAction.equals("DUMMY_CUST")) {
			ConfigMgr configMgr = new ConfigMgr("customer.cfg");
			CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) theAppMgr
					.getStateObject("TXN_POS");
			try {
				String custID = configMgr.getString("DUMMY_CUST_ID");
				String custLastName = configMgr.getString("DUMMY_CUST_FIRST_NAME");
				String custFirstName = configMgr.getString("DUMMY_CUST_LAST_NAME");
				CMSCustomer dummyCustomer = new CMSCustomer(custID);
				// dummyCustomer
				dummyCustomer.setFirstName(custFirstName);
				dummyCustomer.setLastName(custLastName);
				enter();
				pnlCustList.clear();
				pnlCustList.addCustomer(dummyCustomer);
				pnlCustList.sortCustomer(dummyCustomer.getId());
			} catch (BusinessRuleException ex) {
			}
		}
		// MP : For Add new menu option "View Custmer Details"
		else if (sAction.equals("VIEW_CUST_DETAILS")) {
			cmsCustomer = pnlCustList.getSelectedCustomer();
			if (cmsCustomer == null) {
				theAppMgr.showErrorDlg(res.getString("Please select a customer to continue"));
				// txtLastName.requestFocusInWindow();
				setFocusOnLastName();
				return;
			}
			
			if (("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
					//Added by Vivek Mishra for PCR : Dummy Customer Association JAPAN
					||("JP".equalsIgnoreCase(Version.CURRENT_REGION)))
			        //End
			{
				if (ARMCustomerBR.isDummy(cmsCustomer.getId())
						|| ARMCustomerBR.isDefault(cmsCustomer.getId())) {
					theAppMgr.showErrorDlg(res
							.getString("Unable to view details for Dummy or Default customer"));
					anEvent.consume();
					return;
				}
			}
//			if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
//				if (ARMCustomerBR.isDefault(cmsCustomer.getId())) {
//					theAppMgr.showErrorDlg(res
//							.getString("Unable to view details for Default customer"));
//					anEvent.consume();
//					return;
//				}
//			}
			anEvent.consume();
			customerSelected(cmsCustomer);
		} else if (sAction.equals("SEARCH")) {
			pnlCustList.clear();
			if (txtLastName.getText() == null) {
				theAppMgr.showErrorDlg(res.getString("Last Name can't be blank"));
				// txtLastName.requestFocusInWindow();
				setFocusOnLastName();
				return;
			}
			if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
				if (txtLastName.getText().trim().length() < 2) {
					theAppMgr.showErrorDlg(res.getString("Last Name can't be less than 2 characters"));
					// txtLastName.requestFocusInWindow();
					setFocusOnLastName();
					return;
				}
			} else {
				if (txtLastName.getText().trim().length() < 1) {
					if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
					findByDocNumber();
					}
					else {
						 theAppMgr.showErrorDlg(res.getString("Last Name can't be less than 1 character"));
						 txtLastName.requestFocusInWindow();
						 setFocusOnLastName();
					}
					return;
				}
			}
			if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				if(txtDocNumber.getText().trim().length()>1){
				txtLastName.setText("");
				findByDocNumber();
				return;
				}
			}
			// Fix for issue #1886 Search Customer Name
			if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
					//|| ("EUR".equalsIgnoreCase(Version.CURRENT_REGION))) {
				    if (txtFirstName.getText() == null || txtFirstName.getText().trim().length() == 0) {
					    sQuery = txtLastName.getText().trim().replaceAll(" ", "") + QUERY_DELIMITER;
					   }else {
				    	sQuery = txtLastName.getText().trim() + QUERY_DELIMITER;
				    }
			} else {
				sQuery = txtLastName.getText().trim() + QUERY_DELIMITER;
			}
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
			sQuery += Version.CURRENT_REGION + QUERY_DELIMITER;
			processQuery(sQuery);
			// Add Customer Search flag in the state to disable New Customer in Europe
			theAppMgr.addStateObject("CUST_SEARCH", "YES");
			showEuropeSearch();
			setFocusOnLastName();
		}else if(sAction.equals("REFRESH")){
			try {
				
					if(!theAppMgr.isOnLine()){
						theAppMgr.showErrorDlg(res.getString("Refresh not available in Offline mode. Please try other options."));
						setFocusOnLastName();			
						return;
					}			
				
					getNewCustomersForStore(theStore.getId());
					
				} catch (Exception e) {
					theAppMgr.showErrorDlg(res.getString("No customer found"));
				}
			}
		else if (sAction.equals("SKIP")) {
			if (theAppMgr.getStateObject("REFERRED_BY") != null) {
				theAppMgr.addStateObject("REFERRED_BY", "");
				anEvent.consume();
				theAppMgr.fireButtonEvent("LOAD_MGMT");
				return;
			}
		} else if (sAction.equals("CUST_PHONE")) {
			enterCustPhone();
		} else if (sAction.equals("CUST_NO")) {
			enterCustNo();
		} else if (sAction.equals("CUST_BARCODE")) {
			enterCustomerBarcode();
			anEvent.consume();
		} else if (sAction.equals("REWARD_CARD")) {
			if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
				enterRewardCard();
			} else {
				String Builder = CMSDiscountMgr.getBuilderName("REWARD_DISCOUNT");
				theAppMgr.buildObject(this, "REWARD_CARD", Builder, "");
			}
		} else if (sAction.equals("ADVANCED_SEARCH")) {
			showAdvanceSearch();
		} else if (sAction.equals("CUST_EMP_SEARCH")) {
			showEmployeeSearch();
		} else if (sAction.equals("PREVIOUS")) {
			anEvent.consume();
			//theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
			//theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr, null);
			//showRegularSearch();
			// txtLastName.requestFocusInWindow();
			//setFocusOnLastName();
			// Implemented through CustomerLookupApplet_State
		} else if (sAction.equals("OK")) {
			// code added by Anand on 04/15/2005 to ensure that the app does not
			// proceed
			// without a customer being selected
			if (pnlCustList.getSelectedRowIndex() < 0) {
				theAppMgr.showErrorDlg(res.getString("Please select a customer to continue"));
				anEvent.consume();
			   // txtLastName.requestFocusInWindow();
				setFocusOnLastName();
				return;
			}
			// theAppMgr.removeStateObject("ARM_CUST_REQUIRED");
			cmsCustomer = pnlCustList.getSelectedCustomer();
			//added by vivek for privacy mgmt
			if(cmsCustomer!=null){
				theAppMgr.addStateObject("OFFLINE_CUST",cmsCustomer);
			}
			//Ended by vivek for privacy mgmt

			//MB: 06/03/2011
			//Transaction object should be checked for NULL
			//because application control can reach here without cashier logging in
			//in that case there will be no Transaction object.
					
			if(theAppMgr.getStateObject("TXN_POS")!=null)
			{
				if(theAppMgr.getStateObject("TXN_POS") instanceof CMSCompositePOSTransaction){
					theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
				}
			}
			
			//added for VIP MEMBERSHIP SERACH - VIVEK 
			//flag added for Employee budget  
			if(empFlag && theTxn!=null){			
			  String custID = cmsCustomer.getId();
        	  CMSStore store = (CMSStore) theTxn.getStore();
			if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
			  String brand_ID = store.getBrandID();
			  String membershipID = CMSCustomerHelper.getVIPMembershipID(theAppMgr,custID);					
			 if(membershipID!=null){
				 if(theAppMgr.isOnLine()){
					   membershipDetail = CMSCustomerHelper.selectByMembershipNumber(theAppMgr , membershipID, brand_ID);
						 try {
							  if(membershipDetail.getCustomer_id()!=null)
								  cmsCustomer = CMSCustomerHelper.findById(theAppMgr , membershipDetail.getCustomer_id());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						Date expiry_dt = membershipDetail.getExpiry_dt();
						if(membershipDetail.getMembership_id()!=null && membershipDetail.getCustomer_id()!=null){
			// Lorenzo				theTxn.setVipMembershipID(membershipDetail.getMembership_id());
							discount_pct = membershipDetail.getDiscount_pct();
							boolean dialogFlag = checkExpiryDate(expiry_dt, cmsCustomer);
							if(!dialogFlag){
								reset();
								anEvent.consume();						
							}			
							else{
								if (!applyCustomer(cmsCustomer)) {
									anEvent.consume();
									return;
								}
							}
						}
						else{
							theAppMgr.showErrorDlg("Please Enter correct Membership ID");
						}  
				   }
				  
			}
			  else {
				  if (!applyCustomer(cmsCustomer)) {
						anEvent.consume();
						return;
				   }
			  }
			}	
		}
			else{
				if (!applyCustomer(cmsCustomer)) {
					anEvent.consume();
					return;
				}
			}
			//ENDED -VIVEK
			if("US".equalsIgnoreCase(Version.CURRENT_REGION) || "JP".equalsIgnoreCase(Version.CURRENT_REGION)){
				if (!applyCustomer(cmsCustomer)) {
					anEvent.consume();
					return;
				}
			}
			
			
			// reset();
		}
				//issue 1930 VIP membership by neeti
		else if (sAction.equals("VIP_DISCOUNT")) {
				//this code added by vivek to invoke the builder to ask VIP membership ID 
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				if(theAppMgr.isOnLine()){
					String bldrName = CMSVIPDiscountMgr.getBuilderName("VIP_DISCOUNT");
				    theAppMgr.buildObject(this, "VIP_DISCOUNT", bldrName, "VIP_DISCOUNT");
				}
				else{
					theAppMgr.showErrorDlg("VIP Discount is not possible in off-line mode");
				}
			}
		}
	}
	
	private void printQuery(String query, int loopNum){
		System.out.println("loop = " + loopNum + " - query => " + query);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sCardNumber
	 */
	private void findByRewardCard(String sCardNumber) {
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findByRewardCard(theAppMgr,
					sCardNumber);
			if (cmsCustomer == null
					|| cmsCustomer.length < 1
					|| cmsCustomer[0].getCustomerType().equalsIgnoreCase(
							CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)) {
				showNoCustFound();
				String Builder = CMSDiscountMgr.getBuilderName("REWARD_DISCOUNT");
				theAppMgr.buildObject(this, "REWARD_CARD", Builder, "");
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
	 * 
	 * SAP -CRM
	 */
	private void getNewCustomersForStore(String storeId) {
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findNewCustomersForStore(theAppMgr, storeId);
			if (cmsCustomer == null
					|| cmsCustomer.length < 1) {
				showNoCustFound();
				return;
			}
			enter();
			loadCustomers(cmsCustomer);
			pnlCustList.selectFirstRow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Lookup customer by Barcode
	 * 
	 * @param sBarcode
	 *          String
	 */
	private void findByBarcode(String sBarcode) {
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findByBarcode(theAppMgr,
					sBarcode);
			if (cmsCustomer == null
					|| cmsCustomer.length < 1
					|| cmsCustomer[0].getCustomerType().equalsIgnoreCase(
							CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)) {
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
	 * 
	 * @param sCustID
	 */
	private void findByID(String sCustID) {
		try {
			CMSCustomer cmsCustomer = (CMSCustomer) CMSCustomerHelper.findById(theAppMgr, sCustID
					.toUpperCase());
			if (cmsCustomer == null
					|| cmsCustomer.getCustomerType() == null
					|| cmsCustomer.getCustomerType().equalsIgnoreCase(
							CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)) {
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
	 * 
	 * @param sPhone
	 */
	private void findByPhone(String sPhone) {
		Telephone tele;
		//created global variables for customer lookup issue using phone number in HK and China region - by shushma
		Telephone teleCN;
		CMSCustomer cmsCustomer[];
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
			//changed by shushma for customer lookup issue using phone number in HK and China region
			//cmsCustomer = (CMSCustomer[]) CMSCustomerHelper
			//.findByTelephone(theAppMgr, tele);
			//added Locale check for customer lookup using phone number for HK and China region
			if(this.getLocale().getCountry()=="CN"||this.getLocale().getCountry()=="HK")
			{
				teleCN=new Telephone(tele.newTelephoneNumber(sPhone));
				cmsCustomer = (CMSCustomer[]) CMSCustomerHelper
					.findByTelephone(theAppMgr, teleCN);
			}
			else{
				cmsCustomer = (CMSCustomer[]) CMSCustomerHelper
				.findByTelephone(theAppMgr, tele);
			}
			//change closed - by shushma
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				return;
			}
			enter();
			loadCustomers(cmsCustomer);
		} catch (TooManySearchResultsException e) {
			theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
			enter();
			// txtLastName.requestFocusInWindow();
			setFocusOnLastName();
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString("Exception loading customer"));
			e.printStackTrace();
		}
	}

	private void findByAdvanceSearchQuery() {
		if (pnlAdvanceSearch.getFamilyName().length() < 1
				&& pnlAdvanceSearch.getFirstName().length() < 1
				&& pnlAdvanceSearch.getAddressLine1().length() < 1
				&& pnlAdvanceSearch.getCity().length() < 1 && pnlAdvanceSearch.getState().length() < 1
				&& pnlAdvanceSearch.getZipCode().length() < 1 && pnlAdvanceSearch.getPhone().length() < 1
				&& pnlAdvanceSearch.getFiscalCode().length() < 1
				&& pnlAdvanceSearch.getVATNumber().length() < 1
				&& pnlAdvanceSearch.getDocumentNumber().length() < 1) {
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
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findBySearchQuery(theAppMgr,
					custSrchString);
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

	private void findByEmployeeSearchQuery() {
		//Added for Employee Budget
		empFlag = false;
		//Ended for Employee Budget
		if (pnlCustEmpSearch.getFamilyName().length() < 1
				|| pnlCustEmpSearch.getCustomerID().length() < 1) {
			pnlCustEmpSearch.requestFocusToFamilyName();
			theAppMgr.showErrorDlg(res.getString("Both values needed for search"));
			return;
		}
		CustomerSearchString custSrchString = new CustomerSearchString();
		custSrchString.setLastName(pnlCustEmpSearch.getFamilyName());
		custSrchString.setCustomerID(pnlCustEmpSearch.getCustomerID());
		custSrchString.setIsEmployeeSearch(true);

		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findBySearchQuery(theAppMgr,
					custSrchString);
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				pnlCustEmpSearch.requestFocusToFamilyName();
				return;
			}
			showRegularSearch();
			loadCustomers(cmsCustomer);
		} catch (Exception e) {
			e.printStackTrace();
			showEmployeeSearch();
			showNoCustFound();
			pnlCustEmpSearch.requestFocusToFamilyName();
		}

	}
	
	
//	 issue # 1922 
	private void findByDocNumber() {
		if (txtLastName.getText().trim().length() < 1
			&& txtDocNumber.getText().trim().length()<1){
			theAppMgr.showErrorDlg(res.getString("Please enter atleast Last Name or Doc Number to Search"));
			if(txtLastName.getText().equals(null))
			setFocusOnDocumentNumber();
		    else
			setFocusOnLastName();
			return;
		}
		CustomerSearchString custSrchString = new CustomerSearchString();
		custSrchString.setLastName(txtLastName.getText());
		custSrchString.setDocumentNumber(txtDocNumber.getText());
		try {
			CMSCustomer cmsCustomer[] = (CMSCustomer[]) CMSCustomerHelper.findBySearchQuery(theAppMgr,
					custSrchString);
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
	//end code changes for issue #1922
	
	private void loadCustomers(CMSCustomer cmsCustomer[]) {
		pnlCustList.clear();
		for (int iCtr = 0; iCtr < cmsCustomer.length; iCtr++) {
			pnlCustList.addCustomer(cmsCustomer[iCtr]);
		}
		pnlCustList.sortCustomer(null);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sQuery
	 */
	private void processQuery(String sQuery) {
		try {
			// VISHAL : added for search criteria in EUR franchising STORE
			boolean isFranchisingStore=false;
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
			if(theAppMgr.getStateObject("TXN_POS")!=null)
			{
				if(theAppMgr.getStateObject("TXN_POS") instanceof CMSCompositePOSTransaction){
					theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
					if(theTxn.getFranchising_Store())
						isFranchisingStore=true;
				}
			}else{
				if(configMgr.getString("FRANCHISING_SHOP")!=null){
					String FRANCHISING_SHOP=configMgr.getString("FRANCHISING_SHOP");
					if(FRANCHISING_SHOP.toLowerCase().startsWith("y")||FRANCHISING_SHOP.equalsIgnoreCase("y")){
						isFranchisingStore=true;
					}
					}
			}
			}
			CMSCustomer cmsCustomer[] =null;
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
				 cmsCustomer = (CMSCustomer[]) CMSCustomerHelper.findBySearchQuery(theAppMgr,
						sQuery,isFranchisingStore);
			}else{
			 cmsCustomer = (CMSCustomer[]) CMSCustomerHelper.findBySearchQuery(theAppMgr,
					sQuery);
			}
			// end VISHAL : added for search criteria in EUR franchising STORE 16 sept
			if (cmsCustomer == null || cmsCustomer.length < 1) {
				showNoCustFound();
				setFocusOnLastName();
				prepopulateSearchCriteria(sQuery);
				return;
			}
			// Fix for 1823 (Japan): New Customer Entry Screen
			if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
				prepopulateSearchCriteria(sQuery);
			}
			loadCustomers(cmsCustomer);
			pnlCustList.selectFirstRow();
		} catch (TooManySearchResultsException e) {
			theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
			// txtLastName.requestFocusInWindow();
			setFocusOnLastName();
		} catch (Exception e) {
			e.printStackTrace();
			showNoCustFound();
			// txtLastName.requestFocusInWindow();
			setFocusOnLastName();
		}
	}

	/**
	 * This method is added for PCR 1823 (Japan): New Customer Entry Screen
	 * 
	 * @param sQuery
	 */
	private void prepopulateSearchCriteria(String sQuery) {
		if (prepopulateSearchCriteria != null && prepopulateSearchCriteria.equalsIgnoreCase("true")) {
			// Fix for 1533 (US): Bring the search criteria to the customer screen if
			// no customer is found
			if (sQuery != null && sQuery.length() > 0) {
				CMSCustomer customer = new CMSCustomer();
				StringTokenizer st = null;
				String lName = "";
				String fName = "";
				String country = "";
				String city = "";
				String zip = "";
				st = new StringTokenizer(sQuery, "|");
				lName = st.nextToken();
				fName = st.nextToken();
				country = st.nextToken();
				city = st.nextToken();
				zip = st.nextToken();
				Address newAddress = new Address();
				newAddress.setCountry(country);
				newAddress.setCity(city.toUpperCase());
				newAddress.setZipCode(zip);
				customer.addAddress(newAddress);
				customer.doSetLastName(lName);
				customer.doSetFirstName(fName);
				theAppMgr.addStateObject("CUSTOMER_SEARCH_CRITERIA", customer);
			}
		}
	}

	private void registerVIP() {
		if (theAppMgr.getStateObject("OPERATOR") == null
				|| ((CMSEmployee) theAppMgr.getStateObject("OPERATOR")).getExternalID().length() < 1)
			return;
		CMSEmployee operator = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		if (!operator.hasAnyRoleFromRoles(1)) {
			enterManagerID();
		} else {
			proceedToAssociateScreen();
		}
	}

	private void proceedToAssociateScreen() {
		theAppMgr.addStateObject("ARM_REGISTER_VIP", "ARM_REGISTER_VIP");
		theAppMgr.addStateObject("CUSTOMER_LOOKUP", cmsCustomer);
		theAppMgr.addStateObject("CUST_MGMT_MODE", "VIP_REGISTRATION");
		theAppMgr.fireButtonEvent("REGISTER_VIP");
	}

	protected boolean processPasswordEntry(String password) {
		if (theOperator.getPassword().equals(password) || theOperator.getPassword().length() == 0) {
			return (true);
		} else {
			theAppMgr.showErrorDlg(res.getString("Incorrect Password entered"));
			passwordWrongCount++;
			return (false);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param cust
	 */
	private boolean applyCustomer(CMSCustomer customer) {
		// Check for COLLECTIONS_APPLET: Do a PAID_IN txn with house account payment
		// This txn requires a customer. Select a customer and click 'OK'.
		// Nothing happens, because applyCustomer returns false.
		// Hence a check is done to see if the applet is from Collections module.
		// Check for LOGIN_APPLET: Without logging in, go to customer lookup
		// and search for a customer. Click OK. Nothing happens because
		// applyCustomer returns false.
		// Hence a check is done to see if the applet is from Login module.
		String from = (String) theAppMgr.getStateObject("ARM_DIRECTED_FROM");
		if (from != null
				&& from.length() > 0
				&& (from.equalsIgnoreCase("VIEW_TXN_APPLET") || from.equalsIgnoreCase("COLLECTIONS_APPLET") || from
						.equalsIgnoreCase("LOGIN_APPLET") || from.equalsIgnoreCase("TXN_DETAIL_APPLET"))) {
			theAppMgr.addStateObject("TXN_CUSTOMER", customer);
			return true;
		}
		PaymentTransaction theTxn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
		if (theTxn == null)
			return false;
		try {
			if (theTxn instanceof CMSCompositePOSTransaction) {
				CMSCompositePOSTransaction posTxn = (CMSCompositePOSTransaction) theTxn;
				// For PCR: Alert to show the sales limit for the employes for US ADDED region check 1915
				if(("US".equalsIgnoreCase(Version.CURRENT_REGION) || "EUR".equalsIgnoreCase(Version.CURRENT_REGION)) && customer != null && posTxn.getLineItemsArray().length > 0){
					String customerType = customer.getCustomerType();
					if (customerType != null
							&& customerType.equals(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)) {
					// Call the rule
						//this code is commented for Employee budget,
						/*			try{
							    customer = (CMSCustomer) posTxn.getCustomer();
								String countryCode = (posTxn.getStore()).getPreferredISOCountry();
								CMSStore store = (CMSStore) posTxn.getStore();
								String brand_ID = store.getBrandID();
								CMSCustomerAlertRule[] customerAlertRules = null;
								double ruleDiscountPct = 0.0d;
								customerAlertRules = CMSCustomerHelper.getAllCustomerAlertRules(CMSApplet.theAppMgr, countryCode,brand_ID);
							    if(customerAlertRules!=null)
								 ruleDiscountPct = Double.parseDouble(customerAlertRules[0].getDsc_level());						
							     POSLineItem [] lineItm = posTxn.getLineItemsArray();
							     for(int i=0;i<lineItm.length;i++){
							   	 Discount dis = lineItm[i].getDiscount();
							    	if(dis==null){
							    		try {
											posTxn.validateEmployeeRuleForCurrentTransaction(customer);
										} catch (BusinessRuleException bre) {
											if (bre.getMessage() != null && bre.getMessage().length() > 0) {
												if (!theAppMgr.showOptionDlg("Employee Alert", bre.getMessage())) {
													return false;
												}
											}
										}
							    	}else{
							    	double discountpct = lineItm[i].getDiscount().getPercent();
							    	if((discountpct*100)==ruleDiscountPct){
							    		try {
											posTxn.validateEmployeeRuleForCurrentTransaction(customer);
										} catch (BusinessRuleException bre) {
											if (bre.getMessage() != null && bre.getMessage().length() > 0) {
												if (!theAppMgr.showOptionDlg("Employee Alert", bre.getMessage())) {
													return false;
												}
											}
										}
									    }
							        }
							    }
							   
								  
						 }
						 catch(Exception e){
							 e.printStackTrace();
						 }*/
					
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		theAppMgr.addStateObject("TXN_CUSTOMER", customer);
		return true;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getVersion() {
		return ("$Revision: 1.57.2.21.4.27 $");
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getScreenName() {
		return "Customer Lookup";
	}

	/**
	 * put your documentation comment here
	 * 
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		if (customerListPanelString == null) {
			pnlCustList = new CustomerListPanel(((CMSStore) theStore).getPreferredISOCountry());
		} else {
			pnlCustList = new CustomerListPanel_JP(((CMSStore) theStore).getPreferredISOCountry());
		}
		pnlAdvanceSearch = new CustAdvanceSearchPanel();
		pnlSearch = new JPanel();
		pnlCustEmpSearch = new CustEmployeeSearchPanel();
		pnlMainScreenNorth = new JPanel();
		lblLastName = new JCMSLabel();
		// txtLastName = new IMETextField();
		// txtFirstName = new IMETextField();

		//Added By Vivek Mishra to remove IME control function 
		//from first name and last name in  JAPAN region.
		//if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION)||"JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
		//End
			txtLastName = new JCMSTextField();
			txtFirstName = new JCMSTextField();
		} else {
			txtLastName = new IMETextField();
			txtFirstName = new IMETextField();
		}

		lblFirstName = new JCMSLabel();
		lblCity = new JCMSLabel();
		txtCity = new JCMSTextField();
		lblCountry = new JCMSLabel();
		cbxCountry = new JCMSComboBox();
		lblPostCode = new JCMSLabel();
		txtPostCode = new JCMSTextField();
		cardLayout = new RolodexLayout();
		searchPanelCardLayout = new RolodexLayout();
		pnlMainScreen = new JPanel();
		setLayout(cardLayout);
		pnlMainScreenNorth.setLayout(searchPanelCardLayout);
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		BorderLayout borderLayout1 = new BorderLayout();
		pnlMainScreen.setLayout(borderLayout1);
		pnlMainScreen.add(pnlMainScreenNorth, BorderLayout.NORTH);
		pnlMainScreen.add(pnlCustList, BorderLayout.CENTER);
		pnlSearch.setPreferredSize(new Dimension(833, 100));
		pnlAdvanceSearch.setPreferredSize(new Dimension(833, 100));
		pnlCustEmpSearch.setPreferredSize(new Dimension(833, 100));
		pnlSearch.setLayout(gridBagLayout1);
		lblLastName.setLabelFor(txtLastName);
		lblLastName.setText(res.getString("Last Name"));
		txtLastName.setText("");
		lblFirstName.setLabelFor(txtFirstName);
		lblFirstName.setText(res.getString("First Name"));
		txtFirstName.setText("");
		lblCity.setLabelFor(txtCity);
		if ("JP".equalsIgnoreCase(Version.CURRENT_REGION))
			lblCity.setText(res.getString("Prefecture OR Address1"));
		else
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
		pnlCustEmpSearch.setAppMgr(theAppMgr);

		this.add(pnlMainScreen, "REGULAR_SEARCH");
		this.add(pnlAdvanceSearch, "ADVANCED_SEARCH");
		pnlMainScreenNorth.add(pnlSearch, "SEARCH");
		pnlMainScreenNorth.add(pnlCustEmpSearch, "EMPLOYEE_SEARCH");
		pnlSearch.add(lblCity, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(18, 6, 0, 0), 0, 0));
		pnlSearch.add(lblLastName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(18, 6, 0, 0), 0, 0));
		pnlSearch.add(lblCountry, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(18, 6, 18, 0), 0, 0));
		pnlSearch
				.add(txtLastName, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(18, 6, 0, 0),
						0, 0));
		pnlSearch.add(txtCity,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(18, 6, 18, 0), 0, 0));
		pnlSearch.add(lblPostCode, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(18, 6, 18, 0), 0, 0));
		pnlSearch
				.add(txtFirstName, new GridBagConstraints(4, 0, 2, 1, 1.0, 0.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(18, 6, 0, 6),
						2, 0));
		pnlSearch.add(lblFirstName, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(18, 6, 0, 0), 0, 0));
		pnlSearch.add(txtPostCode,
				new GridBagConstraints(5, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(18, 6, 18, 6), 0, 0));
		pnlSearch.add(cbxCountry,
				new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(18, 6, 18, 0), 0, 0));

//		issue 1922 add doc number label
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
			pnlSearch.add(lblCity, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 6, 3, 0), 0, 0));
			pnlSearch.add(lblCountry, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 6, 3, 0), 0, 0));
			pnlSearch.add(txtCity,
					new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(6, 6, 3, 0), 0, 0));
			pnlSearch.add(lblPostCode, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 6, 3, 0), 0, 0));
			pnlSearch.add(txtPostCode,
					new GridBagConstraints(5, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(6, 6, 3, 6), 0, 0));
			pnlSearch.add(cbxCountry,
					new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(6, 6, 3, 0), 0, 0));
			lblDocNumber = new JCMSLabel();
			txtDocNumber = new JCMSTextField();
			lblDocNumber.setLabelFor(txtDocNumber);
			lblDocNumber.setText(res.getString("Doc Number"));
			txtDocNumber.setText("");
			lblDocNumber.setAppMgr(theAppMgr);
			lblDocNumber.setFont(theAppMgr.getTheme().getLabelFont());
			txtDocNumber.setAppMgr(theAppMgr);
			txtDocNumber.setFont(theAppMgr.getTheme().getTextFieldFont());
			pnlSearch.add(lblDocNumber, new GridBagConstraints(0,2,1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 6, 3, 0), 0, 0));
				
			pnlSearch.add(txtDocNumber,
					new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 0), 0, 0));
			
		}
		//end UI code changes for 1922
		
		
		
		
		// PCR1358 IME Control fix for Armani Japan
		
		if (custLookupAppletString == null) {
			//added by shushma for to add space in name while customer lookup
			if ("US".equalsIgnoreCase(Version.CURRENT_REGION)){
				txtLastName.setDocument(new TextFilter(NAME_SPEC_US, 30));
				txtFirstName.setDocument(new TextFilter(NAME_SPEC_US, 30));	
			}
			else{
			txtLastName.setDocument(new TextFilter(NAME_SPEC, 30));
			txtFirstName.setDocument(new TextFilter(NAME_SPEC, 30));
			}
		} else {
			txtLastName.setDocument(new CMSTextFilter(30));
			txtFirstName.setDocument(new CMSTextFilter(30));
			txtLastName.addFocusListener(new TextFieldFocusAdapter(theAppMgr.getEditAreaColor()));
			txtFirstName.addFocusListener(new TextFieldFocusAdapter(theAppMgr.getEditAreaColor()));
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void resize() {
		/*
		 * double r = com.chelseasystems.cr.swing.CMSApplet.r;
		 * lblLastName.setPreferredSize(new Dimension((int) (65 * r), (int) (25 *
		 * r))); lblLastName.setMaximumSize(new Dimension((int) (65 * r), (int) (25 *
		 * r))); lblLastName.setMinimumSize(new Dimension((int) (65 * r), (int) (25 *
		 * r))); txtLastName.setPreferredSize(new Dimension((int) (65 * r), (int)
		 * (25 * r))); txtLastName.setMaximumSize(new Dimension((int) (65 * r),
		 * (int) (25 * r))); txtLastName.setMinimumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); lblFirstName.setPreferredSize(new Dimension((int)
		 * (65 * r), (int) (25 * r))); lblFirstName.setMaximumSize(new
		 * Dimension((int) (65 * r), (int) (25 * r)));
		 * lblFirstName.setMinimumSize(new Dimension((int) (65 * r), (int) (25 *
		 * r))); txtFirstName.setPreferredSize(new Dimension((int) (75 * r), (int)
		 * (25 * r))); txtFirstName.setMaximumSize(new Dimension((int) (75 * r),
		 * (int) (25 * r))); txtFirstName.setMinimumSize(new Dimension((int) (75 *
		 * r), (int) (25 * r))); lblCity.setPreferredSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); lblCity.setMaximumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); lblCity.setMinimumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); txtCity.setPreferredSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); txtCity.setMaximumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); txtCity.setMinimumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); lblCountry.setPreferredSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); lblCountry.setMaximumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); lblCountry.setMinimumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); cbxCountry.setPreferredSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); cbxCountry.setMaximumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); cbxCountry.setMinimumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); lblPostCode.setPreferredSize(new Dimension((int)
		 * (65 * r), (int) (25 * r))); lblPostCode.setMaximumSize(new
		 * Dimension((int) (65 * r), (int) (25 * r)));
		 * lblPostCode.setMinimumSize(new Dimension((int) (65 * r), (int) (25 *
		 * r))); txtPostCode.setPreferredSize(new Dimension((int) (65 * r), (int)
		 * (25 * r))); txtPostCode.setMaximumSize(new Dimension((int) (65 * r),
		 * (int) (25 * r))); txtPostCode.setMinimumSize(new Dimension((int) (65 *
		 * r), (int) (25 * r))); cbxCountry.setMaximumSize(new Dimension((int) (65 *
		 * r), (int) (22)));
		 */
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	public void pageDown(MouseEvent e) {
		int selectedRow = pnlCustList.getAddressModel().getAllRows().indexOf(
				pnlCustList.getSelectedCustomer());
		if (selectedRow < 0)
			selectedRow = pnlCustList.getAddressModel().getLastSelectedCustomerRow();
		pnlCustList.nextPage();
		theAppMgr
				.showPageNumber(e, pnlCustList.getCurrentPageNumber() + 1, pnlCustList.getTotalPages());
		pnlCustList.selectRowIfInCurrentPage(selectedRow);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	public void pageUp(MouseEvent e) {
		int selectedRow = pnlCustList.getAddressModel().getAllRows().indexOf(
				pnlCustList.getSelectedCustomer());
		if (selectedRow < 0)
			selectedRow = pnlCustList.getAddressModel().getLastSelectedCustomerRow();
		pnlCustList.prevPage();
		theAppMgr
				.showPageNumber(e, pnlCustList.getCurrentPageNumber() + 1, pnlCustList.getTotalPages());
		pnlCustList.selectRowIfInCurrentPage(selectedRow);
	}

	/**
	 * MP: Home pressed at customer display exits transaction with no message
	 * 
	 * @return
	 */
	public boolean isHomeAllowed() {
		PaymentTransaction cmsTxn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
		if (cmsTxn instanceof CMSCompositePOSTransaction
				|| cmsTxn instanceof CMSRedeemableBuyBackTransaction) {
			if (cmsTxn == null) {
				return (true);
			}
		}
	/*Added by Yves Agbessi (05-Dec-2017)
		 * Handles the posting of the Sign Off event for Fiscal Solutions Service
		 * START--
		 * */
		boolean goToHomeView = (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res
				.getString("Are you sure you want to cancel this transaction?")));
		if(goToHomeView){
			
			ARMFSBridge.postARMSignOffTransaction((CMSEmployee)theOpr);
		}
		
		return goToHomeView;
		
		/*
		 * --END
		 * */
	}

	/**
	 * Private inner class to respond to text area focus changes Some handlers
	 * were moved from TextCompFocusListener
	 */
	private class TextFieldFocusAdapter extends FocusAdapter {

		private Color idleBackground;

		/**
		 */
		public TextFieldFocusAdapter() {
			this(Color.white);
		}

		/**
		 */
		public TextFieldFocusAdapter(Color idleBackground) {
			this.idleBackground = idleBackground;
		}

		/**
		 * @return Color color to display when not in focus.
		 */
		public Color getIdleBackground() {
			return idleBackground;
		}

		/**
		 * @param idleBackground
		 *          color to display when not in focus.
		 */
		public void setIdleBackground(Color idleBackground) {
			this.idleBackground = idleBackground;
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void focusGained(FocusEvent e) {
			JTextComponent src = (JTextComponent) e.getSource();
			src.setBackground(Color.white);
			if (!(src instanceof JTextArea))
				src.selectAll();
			if (src.getParent() != null && src.getParent() instanceof JComponent)
				((JComponent) src.getParent()).scrollRectToVisible(new Rectangle(src.getParent()
						.getLocation())); // , new Dimension(src.getX(), src.getY()/2)));

			
			/* java.awt.im.InputSubset.HALFWIDTH_KATAKANA for single byte character */
			/* javazz.awt.im.InputSubset.KANJI for double byte character */
			/*
			 * following code is to set double byte characters as a defaut input when
			 * a textfield got focus
			 */
			Character.Subset[] subsets = null;
			subsets = new Character.Subset[] { java.awt.im.InputSubset.HALFWIDTH_KATAKANA };
			//Added By Vivek Mishra to remove IME control function 
			//from first name and last name in  JAPAN region.
			if((e.getSource())instanceof IMETextField)
				//End
			{
			IMETextField component = (IMETextField) (e.getSource());
			InputContext ic = component.getInputContext();
			if (ic != null) {
				ic.selectInputMethod(new Locale("ja", "JP"));
				// Fix for 1883 Testing for sweden
				// ic.selectInputMethod(new Locale("sv", "SE"));
				component.enableInputMethods(true);
				component.getInputContext().setCharacterSubsets(subsets);
			}
			}
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void focusLost(FocusEvent e) {
			((JTextComponent) e.getSource()).setBackground(idleBackground);
			// System.out.println("IN FOCUS LOST OF CUSTOMER LOOK UP...");

			/*
			 * When focus is moved from a textfield, set back the IME control to
			 * English
			 */
			//Added By Vivek Mishra to remove IME control function 
			//from first name and last name in  JAPAN region.
			if((e.getSource())instanceof IMETextField)
				//End
			{
			IMETextField component = (IMETextField) (e.getSource());
			if (component.getInputContext() != null) {
				component.getInputContext().setCharacterSubsets(null);
			} else {
				// theAppMgr.setEditAreaFocus();
				theAppMgr.getAppletManager().getCurrentCMSApplet().getInputContext().setCharacterSubsets(
						null);
			}
			}
		}
	}// End of TextFieldFocusAdaptor

	private class IMETextField extends JCMSTextField implements IValidatable, IFocusTraversable,
			CMSSwingConstants {

		private CMSInputVerifier verifier;

		/**
		 * Contruct a new JCMSTextField. This constrcutor sets the name to be TEXT,
		 * the font to be bold Helvetica point 12, black as the disabled text color
		 * and lastly calls setTextUI().
		 */
		public IMETextField() {
			super();
		}

		public InputContext getInputContext() {
			if (this.getParent() != null) {
				return this.getParent().getInputContext();
			} else {
				return InputContext.getInstance();
			}
		}

		/**
		 * This method will set the background to be the Theme's edit area color as
		 * well as apply a FocusListener that will change the background white when
		 * this component has focus. Also, the font will be set to the theme's text
		 * field font.
		 * 
		 * @param theAppMgr
		 */
		public void setAppMgr(IApplicationManager theAppMgr) {
			if (theAppMgr != null) {
				Color idleBackground = theAppMgr.getEditAreaColor();
				this.setBackground(idleBackground);
				this.setFont(theAppMgr.getTheme().getTextFieldFont());
			}
		}

	}
	//This is added by Vivek for VIP membership Search Function and to check expiry date
	public boolean checkExpiryDate (Date expiry_dt, CMSCustomer customer)
	{
		boolean flag = false ;
		theTxn = ((CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS"));
		discount = (CMSVIPDiscount)CMSVIPDiscountMgr.createDiscount("VIP_DISCOUNT");
		 //Sonali: added for DiscountCode=VIP_DISCOUNT.CODE
		/*ConfigMgr configm = new ConfigMgr("discount.cfg");
		String code=configm.getString("VIP_DISCOUNT.CODE");*/
		try{
		ConfigMgr configm = new ConfigMgr("ArmaniCommon.cfg");
		String code=configm.getString(configm.getString("VIP_DISCOUNT")+".CODE");
		if(discount!=null){
		discount.setDiscountCode(code);}}
		catch(Exception e){
			e.printStackTrace();
		}

		if(expiry_dt!=null){
			Date currentDate = new Date();
			String DATE_FORMAT = "MM/dd/yyyy";
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			String strExpiry_dt = dateFormat.format(expiry_dt);
			String strCurrentDate = dateFormat.format(currentDate);
			try {
				expiry_dt = dateFormat.parse(strExpiry_dt);
				currentDate = dateFormat.parse(strCurrentDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 int i = expiry_dt.compareTo(currentDate);
            	 if(i>(-1)|| i==0){
                	double displayDiscount = discount_pct*100;	
     				String message = "VIP Customer Found. The discount applied is : "+displayDiscount+". Do u want to continue?";
     				flag = theAppMgr.showOptionDlg("VIP Membership Expiry Date ",message);		           		 
           		if(membershipDetail.getDiscount_pct()!= 0.0d && flag){
           		 try {
           			discount_pct = membershipDetail.getDiscount_pct();
          	         POSLineItem[] posLineItems = (POSLineItem[])theTxn.getLineItemsArray();
      		          if (posLineItems != null ) {
      		            for (int j = 0; j < posLineItems.length; j++) {
      		            	if(posLineItems[j].getDiscount()==null){
      		                   	if (posLineItems[j] instanceof CMSSaleLineItem) {
		      		        	  discount.setIsLineItemDiscount(true);
		                	      discount.setPercent(discount_pct);
		                          posLineItems[j].addDiscount(discount);
		      		              }
      		                   	else if (posLineItems[j] instanceof CMSReturnLineItem
		      		                  && ((CMSReturnLineItem)posLineItems[j]).isMiscReturn()) {
		      		            	  discount.setIsLineItemDiscount(true);
		      	          	          discount.setPercent(discount_pct);
		      	                   	  posLineItems[j].addDiscount(discount);
      		                     }
      		                   	else
      		                continue; 
      		              }
      		            	else if (posLineItems[j].getDiscount()!=null && posLineItems[j].getDiscount().getPercent()<discount_pct){
      		                  posLineItems[j].removeDiscount(posLineItems[j].getDiscount());
      		            	  discount.setIsLineItemDiscount(true);
	                	      discount.setPercent(discount_pct);
	                          posLineItems[j].addDiscount(discount);
      		            	}      		            
      		            }//for loop
      		          }
      		        } catch (Exception e) {
      		          System.out.println(e);
      		        }
      		      try {
             			if(discount!=null){
  						theTxn.addDiscount(discount);
  						
  					}
  							} catch (BusinessRuleException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
           		}
           	  	}
           	else{     
           		theAppMgr.showErrorDlg("Current Date :" + currentDate + " is Greator than Expiry date :" + expiry_dt);
                }
        	}
			else{
				 String message = "The VIP membership Date is Expired";
				theAppMgr.showOptionDlg("VIP Membership Expiry Date ",message);
			
			}		
		return flag;
	}

}
