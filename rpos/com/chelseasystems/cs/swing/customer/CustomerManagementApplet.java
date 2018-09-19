/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.customer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.apache.commons.codec.binary.Base64;

import com.chelseasystems.cs.swing.builder.CreditCardBldrUtil;
import com.chelseasystems.cs.swing.builder.CustomerCreditCardBldr;
import com.chelseasystems.cs.swing.dlg.AddressDlg;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.dlg.OptionDlg;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cr.swing.layout.*;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.ajbauthorization.AJBCardOnFileFormatter;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.IObjectBuilder;
import com.chelseasystems.cr.appmgr.IObjectBuilderManager;
import com.chelseasystems.cr.appmgr.ObjectBuilderManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.swing.menu.*;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cs.tax.ArmTaxRate;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.util.DateFormatUtil;
import com.chelseasystems.cs.util.ArmConfigLoader;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.swing.NonFocusableButton;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 * <p>Title: CustomerManagementApplet</p>
 *
 * <p>Description: Create/Modify Customer Details </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    |            |           |           | Initial Version                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-02-2005 | Manpreet  |           | When entering customers with country other than USA|
 |      |            | Sameena   | 96        | i.e Canada or Japan cust, address panel not getting|
 |      |            |           |           | reset. Modified : setCreateModeOn()                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 06-20-2005 | Vikram    |  215      | Previously viewed customer address details was     |
 |      |            |           |           | displayed.                                         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 06-21-2005 | Vikram    |  262      | Added check for duplicate pri and sec email.       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 06-24-2005 | Vikram    |  268      | Title of customer was not highlighted when tabbing.|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 07-13-2005 | Vikram    |  419      | Cashier was being assigned as the Associate.       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 07-15-2005 | Jin       |  543      | New customer id should show up right after saved.  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 8    | 04-04-2006 | Sandhya   | PCR 67    | QAS                                                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerManagementApplet extends CMSApplet {
	private static final long serialVersionUID = 1L;
	private JPanel viewPortPanel;
	private JPanel customerBasicPanel;
	private BaseAddressPanel currentAddressPanel;
	private AddressPanel pnlAddress;
	private CardDetailsPanel pnlCardDetails;
	private CustomerMiscInfoPanel pnlMisc;
	private CustomerBasicPanel pnlCustBasic;
	private CustomerDetailPanel pnlCustDetail;
	private OtherPersonalInfoPanel pnlOtherPersInfo;
	private ViewAddressPanel pnlViewAddress;
	private ViewCreditCardPanel pnlViewCreditCard;
	private ViewCommentsPanel pnlViewComments;
	private ViewPurchaseHistoryPanel pnlViewPurchHistory;
	private RolodexLayout cardLayout;
	private PaymentTransaction theTxn;
	private JPanel pnlLastVisited;
	private CMSCustomer cmsCustomer;
	private CustomerCreditCard creditCard;
	private boolean bShowPrevApplet = false;
	private boolean bUpdateMode = false;
	private boolean bViewsEnabled = false;
	private boolean bNewCustBasicDone = false;
	private boolean loyaltyEnrollmentAttempted = false;
	private String sAssignedAssociate = "";
	double dResolution = com.chelseasystems.cr.swing.CMSApplet.r;
	private CMSStore cmsStore;
	private CMSCustomer customerInitial;
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	private boolean bConsumeEvent = false;
	private Component compTabEvent;
	private CustomerMgmtEvtListener mgmtEvtListener;
	private AddressTypeListener addTypeListener;
	private static final String CONFIGURATION_FILE = "customer.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
	private String custBasicPanelClassName = configMgr.getString("CUSTOMER.BASIC_PANEL");
	private String custDetailPanelClassName = configMgr.getString("CUSTOMER.DETAIL_PANEL");
	private String enableReturnMail = configMgr.getString("ENABLE_RETURN_MAIL");
	private boolean updateAllStgTbl = false;
	private static final String VERIFIED = "Verified";  
	private String defaultPrivacyCode = null;
	private boolean isInited = false;
	String ajbresponse;
	String[] tokenList = null;
	ArrayList cardInSession = new ArrayList();
	private static ConfigMgr config;
	private String fipay_flag;
	/**
	 * Get ScreeName
	 * @return ScreenName
	 */
	public String getScreenName() {
		return "Customer Mgmt";
	}

	/**
	 * getVersioni
	 * @return Version
	 */
	public String getVersion() {
		return ("$Revision: 1.78.2.12.4.38 $");
	}

	/**
	 * init
	 */
	public void init() {
		try {
			cmsCustomer = new CMSCustomer();
			creditCard = new CustomerCreditCard();
//			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * start
	 */
	public void start() {
		
	 	String fileName = "store_custom.cfg";
		config = new ConfigMgr(fileName);
		fipay_flag = config.getString("FIPAY_Integration");
		
		 //Default value of the flag is Y if its not present in credit_auth.cfg
		if (fipay_flag == null) {
			fipay_flag = "Y";
		}
		if(!isInited){
			try {
				System.out.println("Re-initializing CustomerManagementApplet.........");
				jbInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Register AWT Listeners for the applet.
		mgmtEvtListener = new CustomerMgmtEvtListener();
		addTypeListener = new AddressTypeListener();
		addAWTListners();
		loyaltyEnrollmentAttempted = false;
		// MP: Reset only when we have not returned from the Customer Lookup screen.
		if (theAppMgr.getStateObject("REFERRED_BY") == null && theAppMgr.getStateObject("CUST_DEPOSIT") == null) {
			reset();
		}
		if (theAppMgr.getStateObject("REFERRED_BY") != null) {
			applyReferral();
		} else {
			theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
			theAppMgr.showMenu(MenuConst.CUST_MGMT, theOpr);
			theTxn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
			cmsStore = (CMSStore) theAppMgr.getGlobalObject("STORE");
			cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_LOOKUP");
			if (cmsCustomer != null) {
				customerInitial = (CMSCustomer) cmsCustomer.clone();
				bShowPrevApplet = false;
				if (!bViewsEnabled)
					initViews();
				else
					reset();
				loadCustomer();
				if (theAppMgr.getStateObject("CUST_MGMT_MODE") != null 
					&& theAppMgr.getStateObject("CUST_MGMT_MODE").equals("VIP_REGISTRATION")) {
					theAppMgr.removeStateObject("CUST_MGMT_MODE");
					setEnabled(true);
					// PCR1325 Customer Details fix for Armani Japan
					if (custBasicPanelClassName == null) {
						pnlCustBasic.requestFocusOnFirstName();
					} else {
						pnlCustBasic.requestFocusOnLastName();
					}
				} else
					setEnabled(false);
				bUpdateMode = true;
			} else if (theAppMgr.getStateObject("NEW_CUSTOMER") != null) {
				// reset();
				theAppMgr.removeStateObject("NEW_CUSTOMER");
				cmsCustomer = new CMSCustomer();
				bShowPrevApplet = true;
				setCreateModeOn();
				bUpdateMode = false;
				bNewCustBasicDone = false;
				// PCR1325 Customer Details fix for Armani Japan
				if (custBasicPanelClassName == null) {
					pnlCustBasic.requestFocusOnFirstName();
				} else {
					pnlCustBasic.requestFocusOnLastName();
				}
			} else if (theAppMgr.getStateObject("CUST_DEPOSIT") != null) {
				cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("CUST_DEPOSIT");
				theAppMgr.removeStateObject("CUST_DEPOSIT");
			}
			setEditArea(res.getString("Select option"), null, -1);
			cardLayout.first(viewPortPanel);
		}
		bConsumeEvent = false;
		// Eur- Update all customer records into Staging table
		ConfigMgr custConfigMgr = new ConfigMgr("customer.cfg");
		String strupdateAllStgTbl = custConfigMgr.getString("UPDATE_ALL_CUST_STG_TABLE");
		if (strupdateAllStgTbl != null && strupdateAllStgTbl.equalsIgnoreCase("true")) {
			updateAllStgTbl = true;
		}
		// Added for displaying Date as Field in Europe by Vivek
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
		String createDate =(String)theAppMgr.getStateObject("CREATED_DATE");
		if(createDate == null || createDate.length()==0){
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			pnlMisc.setDate(formatter.format(new Date()));
		}
		else{
	    	pnlMisc.setDate(createDate);
		 }
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void applyReferral() {
		pnlOtherPersInfo.setReferredBy((String) theAppMgr.getStateObject("REFERRED_BY"));
		if (bUpdateMode) {
			theAppMgr.showMenu(MenuConst.OK_CANCEL, "MOD_CUST", theOpr);
		} else if (!bNewCustBasicDone) {
			theAppMgr.showMenu(MenuConst.OK_CANCEL, "NEW_CUST", theOpr);
		} else {
			theAppMgr.showMenu(MenuConst.CUST_MGMT, theOpr);
		}
		theAppMgr.removeStateObject("REFERRED_BY");
		cardLayout.show(viewPortPanel, pnlOtherPersInfo);
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				pnlOtherPersInfo.requestFocusOnFirstField();
			}
		});
	}

	/**
	 * Initialize all view panels.
	 */
	private void initViews() {
		try {
			pnlViewComments = new ViewCommentsPanel();
			pnlViewCreditCard = new ViewCreditCardPanel();
			pnlViewAddress = new ViewAddressPanel(pnlAddress);
			pnlAddress.setPrimary(true);
			pnlViewAddress.setCustomer(cmsCustomer);
			pnlCardDetails = new CardDetailsPanel();
			pnlViewPurchHistory = new ViewPurchaseHistoryPanel();
			pnlCustBasic.setAppMgr(theAppMgr);
			pnlViewComments.setAppMgr(theAppMgr);
			pnlViewCreditCard.setAppMgr(theAppMgr);
			pnlViewAddress.setAppMgr(theAppMgr);
			pnlCardDetails.setAppMgr(theAppMgr);
			pnlViewComments.setAppMgr(theAppMgr);
			pnlViewPurchHistory.setAppMgr(theAppMgr);
			viewPortPanel.add(pnlViewComments, pnlViewComments);
			viewPortPanel.add(pnlViewCreditCard, pnlViewCreditCard);
			viewPortPanel.add(pnlViewAddress, pnlViewAddress);
			viewPortPanel.add(pnlCardDetails, pnlCardDetails);
			viewPortPanel.add(pnlViewPurchHistory, pnlViewPurchHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bViewsEnabled = true;
	}

	/**
	 * Loads the New Customer menu and sets default Associate ID.
	 */
	private void setCreateModeOn() {
		bUpdateMode = false;
		reset();
		setEnabled(true);
		// PCR1325 Customer Details fix for Armani Japan
		if (custBasicPanelClassName == null) {
			pnlCustBasic.requestFocusOnFirstName();
			// Fix for 1533: Bring the search criteria to the customer screen if no customer is found
			CMSCustomer customer = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_SEARCH_CRITERIA");
			if (customer != null) {
				pnlCustBasic.setLastName(customer.getLastName());
				pnlCustBasic.setFirstName(customer.getFirstName());
				List newAddressList = new ArrayList();
				Address address = new Address();
				ArmTaxRate armTaxRate[] = null;
				String zip = null;
				newAddressList = customer.getAddresses();
				if (newAddressList != null && newAddressList.size() > 0) {
					address = (Address) newAddressList.get(0);
				}
				if (address != null) {
					zip = address.getZipCode();
					if (zip != null && zip.length() > 0) {
						currentAddressPanel = pnlAddress.getCurrentAddressPanel();
						if (currentAddressPanel instanceof QASAddressPanel) {
							if (!((QASAddressPanel) currentAddressPanel).getQasVerifiedCustomerAddress().isQasModified()) {
								armTaxRate = ((QASAddressPanel) currentAddressPanel).validateZipCode(zip);
								if (armTaxRate != null && armTaxRate.length > 0) {
									address.setState(armTaxRate[0].getState());
								}
							}
						}
					}
				}
				pnlAddress.setAddress(address);
			}
		} else {
			pnlCustBasic.requestFocusOnLastName();
			CMSCustomer customer = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_SEARCH_CRITERIA");
			if (customer != null) {
				pnlCustBasic.setLastName(customer.getLastName());
				pnlCustBasic.setFirstName(customer.getFirstName());
			}
		}
		theAppMgr.showMenu(MenuConst.OK_CANCEL, "NEW_CUST", theOpr);
		if (theTxn != null && theTxn instanceof CompositePOSTransaction 
			&& ((CompositePOSTransaction) theTxn).getConsultant() != null) {
			pnlMisc.setAssociateId(((CompositePOSTransaction) theTxn).getConsultant().getExternalID());
		} else if (theOpr != null)
			pnlMisc.setAssociateId(((CMSEmployee) theOpr).getExternalID());
		/*
		 * Added utility for Bug 96 : When entering customers with country other than USA 
			i.e Canada or Japan cust, address panel not getting reset
		 */

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
						if (theAppMgr.getStateObject("CUSTOMER_SEARCH_CRITERIA") == null) {
							reset();
						} else {
							theAppMgr.removeStateObject("CUSTOMER_SEARCH_CRITERIA");
						}
					}
				});
			}
		});
	}

	/**
	 * stop
	 */
	public void stop() {
		removeAWTListeners();
	}

	/**
	 * Intialize and Layout components
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		viewPortPanel = new JPanel();
		customerBasicPanel = new JPanel();
		pnlAddress = new AddressPanel();
		pnlMisc = new CustomerMiscInfoPanel();
		// PCR1325 Customer Details fix for Armani Japan
		if (custBasicPanelClassName == null) {
			pnlCustBasic = new CustomerBasicPanel();
		} else {
			pnlCustBasic = (CustomerBasicPanel) Class.forName(custBasicPanelClassName).newInstance();
		}
		// Fix for Europe: Interchange email info with birth info
		if (custDetailPanelClassName == null) {
			pnlCustDetail = new CustomerDetailPanel();
		} else {
			pnlCustDetail = (CustomerDetailPanel) Class.forName(custDetailPanelClassName).newInstance();
			// Set the reference object in the respective panel
			((CustomerDetailPanel_EUR) pnlCustDetail).setBasicReference((CustomerBasicPanel_EUR) pnlCustBasic);
			((CustomerBasicPanel_EUR) pnlCustBasic).setDetailReference((CustomerDetailPanel_EUR) pnlCustDetail);
		}
		pnlOtherPersInfo = new OtherPersonalInfoPanel();
		cardLayout = new RolodexLayout();
		viewPortPanel.setLayout(cardLayout);
		//TD
		pnlCustBasic.setPreferredSize(new Dimension((int) (800 * dResolution), (int) (135 * dResolution)));
		pnlAddress.setPreferredSize(new Dimension((int) (350 * dResolution), (int) (400 * dResolution)));
		pnlMisc.setPreferredSize(new Dimension((int) (250 * dResolution), (int) (400 * dResolution)));
		pnlAddress.setAppMgr(theAppMgr);
		pnlCustBasic.setAppMgr(theAppMgr);
		pnlCustDetail.setAppMgr(theAppMgr);
		pnlMisc.setAppMgr(theAppMgr);
		pnlOtherPersInfo.setAppMgr(theAppMgr);
		customerBasicPanel.setLayout(new BorderLayout());
		customerBasicPanel.add(pnlCustBasic, BorderLayout.NORTH);
		customerBasicPanel.add(pnlAddress, BorderLayout.CENTER);
		customerBasicPanel.add(pnlMisc, BorderLayout.EAST);
		viewPortPanel.add(customerBasicPanel, customerBasicPanel);
		viewPortPanel.add(pnlCustDetail, pnlCustDetail);
		viewPortPanel.add(pnlOtherPersInfo, pnlOtherPersInfo);
		showTitle(true);
		this.setBackground(theAppMgr.getBackgroundColor());
		this.getContentPane().add(viewPortPanel, BorderLayout.CENTER);
		pnlAddress.setEnabled(false);
		pnlLastVisited = customerBasicPanel;
		bShowPrevApplet = true;
		Verifiers verifiers = new Verifiers();
		// FirstName is optional for Japan
		if (!("JP".equalsIgnoreCase(Version.CURRENT_REGION))) {
			pnlCustBasic.getFirstNameJTXT().setVerifier(verifiers.getFirstNameVerifier());
		}
		pnlCustBasic.getLastNameJTXT().setVerifier(verifiers.getLastNameVerifier());
		pnlCustDetail.getDateOfIssueTxt().setVerifier(verifiers.getIssueDateVerifier());
		pnlOtherPersInfo.getSpecialDateTxt().setVerifier(verifiers.getSpecialEDVerifier());
		pnlCustDetail.getLastFieldOnScreen().setVerifier(verifiers.getLastFieldOfCustDetailVerifier());
    
		if(configMgr.getString("UNDEFINED_PRIVACY_CODE") == null 
				|| "".equalsIgnoreCase(configMgr.getString("UNDEFINED_PRIVACY_CODE"))){
			defaultPrivacyCode = "99";
		} else {
			defaultPrivacyCode = configMgr.getString("UNDEFINED_PRIVACY_CODE");
		}
		
		isInited = true;
	}

	/**
	 * Register AWT Listnerers for Applet.
	 */
	private void addAWTListners() {
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(mgmtEvtListener, AWTEvent.KEY_EVENT_MASK);
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(mgmtEvtListener, AWTEvent.MOUSE_EVENT_MASK);
		pnlAddress.addItemListener(addTypeListener);
	}

	/**
	 * Remove AWT Listeners.
	 */
	private void removeAWTListeners() {
		java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener(mgmtEvtListener);
		pnlAddress.removeItemListener(addTypeListener);
	}

	/**
	 * put your documentation comment here
	 */
	private void setReadOnlyFields() {
		pnlCustDetail.setCompany(cmsStore.getCompanyId());
		pnlCustDetail.setInterCoCode(cmsStore.getCompanyCode());
	}

	/**
	 * PageDownEvent
	 * @param e
	 * MouseEvent
	 */
	public void pageDown(MouseEvent e) {
		pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
		// bShowPrevApplet = false;
		if (pnlLastVisited.equals(pnlOtherPersInfo))
			return;
		if (pnlLastVisited.equals(pnlViewComments)) {
			pnlViewComments.pageDown();
			return;
		}
		if (pnlLastVisited.equals(pnlViewCreditCard)) {
			pnlViewCreditCard.pageDown();
			return;
		}
		if (pnlLastVisited.equals(pnlViewAddress)) {
			pnlViewAddress.pageDown();
			return;
		}
		cardLayout.next(viewPortPanel);
		while(cardLayout.getCurrent(viewPortPanel).equals(pnlViewComments) 
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlViewCreditCard)
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlViewAddress) 
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlCardDetails)
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlViewPurchHistory))
			cardLayout.next(viewPortPanel);
		pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
		if (pnlLastVisited.equals(customerBasicPanel)) {
			showTitle(true);
			if (pnlCustBasic.getFirstNameJTXT().isEnabled())
				pnlCustBasic.requestFocusOnLastName();
			return;
		} else if (pnlLastVisited instanceof CustomerDetailPanel ) {//&& pnlCustDetail.getDateOfIssueTxt().isEnabled()) {
			pnlCustDetail.requestFocusOnFirstField();
		} else if (pnlLastVisited instanceof OtherPersonalInfoPanel ) { //&& pnlOtherPersInfo.getSpecialDateTxt().isEnabled()) {
			pnlOtherPersInfo.requestFocusOnFirstField();
		}
		showTitle(false);
	}

	/**
	 * PageUpEvent
	 * @param e
	 *            MouseEvent
	 */
	public void pageUp(MouseEvent e) {
		pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
		if (pnlLastVisited.equals(customerBasicPanel))
			return;
		if (pnlLastVisited.equals(pnlViewComments)) {
			pnlViewComments.pageUp();
			return;
		}
		if (pnlLastVisited.equals(pnlViewCreditCard)) {
			pnlViewCreditCard.pageUp();
			return;
		}
		if (pnlLastVisited.equals(pnlViewAddress)) {
			pnlViewAddress.pageUp();
			return;
		}
		cardLayout.previous(viewPortPanel);
		while(cardLayout.getCurrent(viewPortPanel).equals(pnlViewComments) 
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlViewCreditCard)
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlViewAddress) 
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlCardDetails)
				|| cardLayout.getCurrent(viewPortPanel).equals(pnlViewPurchHistory))
			cardLayout.previous(viewPortPanel);
		if (cardLayout.getCurrent(viewPortPanel).equals(customerBasicPanel)) {
			showTitle(true);
			if (pnlCustBasic.getFirstNameJTXT().isEnabled())
				pnlCustBasic.requestFocusOnLastName();
			return;
		} else if (pnlLastVisited instanceof CustomerDetailPanel ) { //&& pnlCustDetail.getDateOfIssueTxt().isEnabled()) {
			pnlCustDetail.requestFocusOnFirstField();
		} else if (pnlLastVisited instanceof OtherPersonalInfoPanel ) {//&& pnlOtherPersInfo.getSpecialDateTxt().isEnabled()) {
			pnlOtherPersInfo.requestFocusOnFirstField();
		}
		showTitle(false);
	}

	/**
	 * Edit Area event
	 * @param command
	 *            Command
	 * @param input
	 *            Input
	 */
	public void editAreaEvent(String sCommand, String sInput) {
		if (sCommand.equals("ADD_COMMENTS")) {
			CustomerComment comment = new CustomerComment();
			comment = new CustomerComment();
			comment.setDateCommented(new Date());
			comment.setComment(sInput);
			if (theTxn != null && theTxn instanceof CompositePOSTransaction 
				&& ((CompositePOSTransaction) theTxn).getConsultant() != null) {
				comment.setAssociateId(((CompositePOSTransaction) theTxn).getConsultant().getExternalID());
			} else
				comment.setAssociateId(((CMSEmployee) theOpr).getExternalID());
			comment.setBrandId(cmsStore.getBrandID());
			comment.setStoreId(cmsStore.getId());
			// Set the modified flag to set it in new comments array
			comment.setIsModified(true);
			pnlViewComments.addCustomerComment(comment);
			setEditArea(res.getString("Select option"), null, -1);
		}
	}

	/**
	 * Create PurchaseHistory for customer.
	 * @return HistoryLoaded or not
	 */
	private boolean loadPurchaseHistory() {
		if (cmsCustomer == null)
			return false;
		try {
			CustomerSaleSummary summary[] = CMSCustomerHelper.getCustSaleSummary(theAppMgr, cmsCustomer.getId(), cmsStore.getId());
			pnlViewPurchHistory.setCustomerName(cmsCustomer.getFirstName() + " " + cmsCustomer.getLastName());
			pnlViewPurchHistory.setCustomerSummary(summary);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Show/Hide Title
	 * 
	 * @param bShowTitle
	 *            true/false
	 */
	private void showTitle(boolean bShowTitle) {
		if (bShowTitle)
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), res.getString("Basic Information")));
		else
			this.setBorder(null);
	}

	/**
	 * Check if customer is valid
	 * 
	 * @return True/False
	 */
	private boolean isValidCustomer() {
		try {
			// First name is optional for Japan
			if (!("JP".equalsIgnoreCase(Version.CURRENT_REGION)))
				if (pnlCustBasic.getFirstName().trim().length() < 1) {
					theAppMgr.showErrorDlg(res.getString("First Name required"));
					showTitle(true);
					cardLayout.show(viewPortPanel, customerBasicPanel);
					pnlCustBasic.setFirstName("");
					pnlCustBasic.requestFocusOnFirstName();
					return false;
				}
			if (pnlCustBasic.getLastName().trim().length() < 1) {
				theAppMgr.showErrorDlg(res.getString("Last Name required"));
				showTitle(true);
				cardLayout.show(viewPortPanel, customerBasicPanel);
				pnlCustBasic.setLastName("");
				pnlCustBasic.requestFocusOnLastName();
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Error message when views are disabled.
	 */
	private void showViewsDisabledMsg() {
		theAppMgr.showErrorDlg(res.getString("No customer present"));
	}

	/**
	 * Persist Customer.
	 */
	private void submitCustomer() {
		try {
			CMSCustomer cust;	
			//Added for Privacy Mgmt Marketing and Master
			if("US".equalsIgnoreCase(Version.CURRENT_REGION) || "JP".equalsIgnoreCase(Version.CURRENT_REGION) ){
				cmsCustomer.setPrivacyMarketing(false);
				cmsCustomer.setPrivacyMaster(false);
				//Added for displaying Date as Field in Europe by Vivek
				cmsCustomer.setCustIssueDate(null);
			}
				 cust = CMSCustomerHelper.submit(theAppMgr, cmsCustomer);			
			if (cust != null)
				cmsCustomer = cust;
			if (!bUpdateMode) {
				customerInitial = (CMSCustomer) cmsCustomer.clone();
				pnlCustBasic.setCustomerID(cmsCustomer.getId());
			}
			// Refresh the customer lookup panel list with this new customer object
			theAppMgr.addStateObject("CUSTOMER_SUBMIT", cmsCustomer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean telephonesValid() {
		int iIndex = pnlAddress.containsDuplicatePhone();
		if (iIndex != -1) {
			theAppMgr.showErrorDlgLater(res.getString("Duplicate phone, please change to continue"));
			if (iIndex == 1)
				pnlAddress.requestFocusToPrimaryPhone();
			else if (iIndex == 2)
				pnlAddress.requestFocusToSecondaryPhone();
			else if (iIndex == 3)
				pnlAddress.requestFocusToTernaryPhone();
			return false;
		}
		return true;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isAddressValid() {
		try {
			if (!pnlAddress.isValidAddress()) {
				this.requestFocusOnAddress();
				theAppMgr.showErrorDlg(res.getString("Address is not valid."));
				return false;
			}
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * put your documentation comment here
	 * @param Command
	 * @param obj
	 */
	public void objectEvent(String Command, Object obj) {
		if (Command.equals("CREDIT_CARD")) {
			try {
				if (obj != null) {
					creditCard = (CustomerCreditCard) obj;
					pnlViewCreditCard.addCreditCard(creditCard);
					setEditArea(res.getString("Select option"), null, -1);
				}
				if (obj == null) {
					String Builder = CMSPaymentMgr.getPaymentBuilder("CUSTOMER_CREDIT_CARD");
					theAppMgr.buildObject("CREDIT_CARD", Builder, "");
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
		} else if (Command.equals("ENROLL_LOYALTY")) { // BUG 1649
			if (obj != null) {
				Loyalty loyalty = (Loyalty) obj;
				try {
					LoyaltyHelper.addLoyalty(theAppMgr, loyalty);
					loyalty.getCustomer().setLoyaltyMember();
				} catch (Exception e) {
					e.printStackTrace();
					theAppMgr.showErrorDlg(CMSApplet.res.getString("Unable to persist Loyalty! Please try from Loyalty Management."));
				}
			}
			theAppMgr.fireButtonEvent("OK");
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusOnAddress() {
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				pnlAddress.cbCountry.requestFocus();
			}
		});
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isEmailsValid() {
		if (pnlCustBasic.getPrimaryEmail() != null && pnlCustBasic.getSecondaryEmail() != null && !pnlCustBasic.getPrimaryEmail().trim().equals("")
				&& !pnlCustBasic.getSecondaryEmail().trim().equals("") && pnlCustBasic.getPrimaryEmail().trim().equalsIgnoreCase(pnlCustBasic.getSecondaryEmail().trim())) {
			pnlCustBasic.requestFocusOnPrimaryEmail();
			theAppMgr.showErrorDlg(res.getString("Duplicate email, please change to continue"));
			return false;
		}
		try {
			RuleEngine.execute("com.chelseasystems.cr.customer.Customer.setEMail", this, new Object[] { pnlCustBasic.getPrimaryEmail().trim() });
		} catch (BusinessRuleException ex) {
			pnlCustBasic.requestFocusOnPrimaryEmail();
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			return false;
		}
		try {
			RuleEngine.execute("com.chelseasystems.cr.customer.Customer.setEMail", this, new Object[] { pnlCustBasic.getSecondaryEmail().trim() });
		} catch (BusinessRuleException ex) {
			pnlCustBasic.requestFocusOnSecondaryEmail();
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			return false;
		}
		return true;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isBirthDateValid() {
		try {
			pnlCustDetail.getBirthDate();
			return pnlCustDetail.isValidDateOfBirth();
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString(e.getMessage()));
			return false;
		}
	}

	private boolean isIssueDateValid() {
		try {
			if (pnlCustDetail.getDateOfIssue().trim().length() < 1)
				return true;
			CMSCustomer cms = new CMSCustomer();
			cms.setIssueDate(DateFormatUtil.getFormattedDate(pnlCustDetail.getDateOfIssue()));
			return (true);
		} catch (Exception bex) {
			theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
			cardLayout.show(viewPortPanel, pnlCustDetail);
			pnlCustDetail.requestFocusToIssueDate();
			return (false);
		}
	}

	private boolean isSpecialEvtDateValid() {
		try {
			if (pnlOtherPersInfo.getSpecialDate().trim().length() < 1)
				return true;
			CMSCustomer cms = new CMSCustomer();
			cms.setSpecialEventDate(DateFormatUtil.getFormattedDate(pnlOtherPersInfo.getSpecialDate()));
			return (true);
		} catch (Exception bex) {
			theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
			cardLayout.show(viewPortPanel, pnlOtherPersInfo);
			pnlOtherPersInfo.requestFocusToSpecialDate();
			return (false);
		}
	}

	/**
	 * SUB MENU EVENTS
	 * 
	 * @param sHeader
	 *            SUB_MENU Name
	 * @param anEvent
	 *            CMSActionEvent
	 */
	public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("OK")) {
			if (theAppMgr.getStateObject("CUSTOMER_SEARCH_CRITERIA") != null) {
				theAppMgr.removeStateObject("CUSTOMER_SEARCH_CRITERIA");
			}
			if (sHeader.equals("NEW_CUST")) {
				// pnlCustBasic.requestFocusOnFirstName();
				// If customer isnt valid return.
				// If Current panel is Detail panel
				// BirthDate and IssueDate should be validated first.
				if (cardLayout.getCurrent(viewPortPanel) instanceof CustomerDetailPanel) {
					if (!isBirthDateValid() || !isIssueDateValid() || !isValidCustomer() 
						|| !telephonesValid() || !isEmailsValid() || !isAddressValid() 
						|| !isSpecialEvtDateValid()) {
						anEvent.consume();
						return;
					}
				}
				// If Current panel is OtherPersonalInfoPanel
				// SpecialEventDate should be validated first.
				else if (cardLayout.getCurrent(viewPortPanel) instanceof OtherPersonalInfoPanel) {
					if (!isSpecialEvtDateValid() || !isValidCustomer() 
						|| !telephonesValid() || !isEmailsValid() || !isAddressValid() 
						|| !isBirthDateValid() || !isIssueDateValid()) {
						anEvent.consume();
						return;
					}
				}
				//Added for Privacy Mgmt Marketing and Master
				else if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				 if (isMarketingMasterValid()) {
					anEvent.consume();
					return;
				}
				}
				// If Current panle is BasicInfoPanel
				// Customer should be validated first.
				else {
					if (!isValidCustomer() || !telephonesValid() || !isEmailsValid() 
						|| !isAddressValid() || !isBirthDateValid() || !isIssueDateValid() 
						|| !isSpecialEvtDateValid()) {
						anEvent.consume();
						return;
					}
				}
				cmsCustomer = new CMSCustomer();
				initViews();
				setReadOnlyFields();
				bUpdateMode = false;
				setEnabled(true);
				bNewCustBasicDone = true;
				// PCR1325 Customer Details fix for Armani Japan
				if (custBasicPanelClassName == null) {
					pnlCustBasic.requestFocusOnFirstName();
				} else {
					pnlCustBasic.requestFocusOnLastName();
				}
			} else if (sHeader.equals("MOD_CUST")) {
				if (cardLayout.getCurrent(viewPortPanel) instanceof CustomerDetailPanel) {
					if (!isBirthDateValid() || !isIssueDateValid() || !isValidCustomer() 
						|| !telephonesValid() || !isEmailsValid() || !isAddressValid() 
						|| !isSpecialEvtDateValid()) {
						anEvent.consume();
						return;
					}
				} else if (cardLayout.getCurrent(viewPortPanel) instanceof OtherPersonalInfoPanel) {
					if (!isSpecialEvtDateValid() || !isValidCustomer() 
						|| !telephonesValid() || !isEmailsValid() || !isAddressValid() 
						|| !isBirthDateValid() || !isIssueDateValid()) {
						anEvent.consume();
						return;
					}
				}
				//Added for Privacy Mgmt Marketing and Master
				else if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
					if(cmsCustomer!=null){
					//added on 29/10/2009 for modify customer
					String dt = pnlMisc.getDate();
	   				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	   				Date modDate;
					try {
						modDate = (Date)formatter.parse(dt);
					 	if(modDate!=null){
					 		pnlMisc.setDate(formatter.format(new Date()));
					 	}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					//changes ends
					if (isMarketingMasterValid()) {
						anEvent.consume();
						return;
					}
					}
				}
				else {
					if (!isValidCustomer() || !telephonesValid() || !isEmailsValid() 
						|| !isAddressValid() || !isBirthDateValid() || !isIssueDateValid() 
						|| !isSpecialEvtDateValid()) {
						anEvent.consume();
						return;
					}
				}
			setEnabled(false);
			} else if (sHeader.equals("NEW_CUST_ENROLL_LOYALTY")) { // BUG 1649
				anEvent.consume();
				return;
			}
			theAppMgr.showMenu(MenuConst.CUST_MGMT, theOpr);
			cardLayout.show(viewPortPanel, customerBasicPanel);
			// PCR67 QAS for US
			if (pnlAddress.getCurrentAddressPanel() instanceof QASAddressPanel)
				((QASAddressPanel) pnlAddress.getCurrentAddressPanel()).qasVerifyAddress();
			anEvent.consume();
			setEditArea(res.getString("Select option."), null, -1);
		} else if (sAction.equals("CANCEL")) {
			theAppMgr.addStateObject("ARM_CONSUME_FOCUS_EVT", "TRUE");
			if (!theAppMgr.showOptionDlg(res.getString("Cancel Changes"), 
					res.getString("Are you sure you want to cancel possible changes?"))) {
				anEvent.consume();
				return;
			}
			if (sHeader.equals("NEW_CUST")) {
				theAppMgr.removeStateObject("NEW_CUSTOMER");
				if (bShowPrevApplet) {
					bShowPrevApplet = false;
					return;
				} else {
					bUpdateMode = true;
					reset();
					if (cmsCustomer != null)
						loadCustomer();
					setEnabled(false);
				}
			} else if (sHeader.equals("MOD_CUST")) {
				reset();
				loadCustomer();
				setEnabled(false);
			} else if (sHeader.equals("NEW_CUST_ENROLL_LOYALTY")) { // BUG 1649
				try {
					cmsCustomer.setNotLoyaltyMember();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
				anEvent.consume();
				theAppMgr.fireButtonEvent("OK");
				return;
			} else if (sHeader.equals("VIEW_CARD_DETAILS")) {
				// do nothing
			}
			theAppMgr.showMenu(MenuConst.CUST_MGMT, theOpr);
			cardLayout.show(viewPortPanel, customerBasicPanel);
			anEvent.consume();
			setEditArea(res.getString("Select option"), null, -1);
		} else if (sAction.equals("PREV")) {
			cardInSession.clear();
			anEvent.consume();
			if (sHeader.equals("LOYALTY_OP")) {
				cmsCustomer = (CMSCustomer) theAppMgr.getStateObject("CUSTOMER_LOOKUP");
				loadCustomer();
			}
			if (sHeader.equals("VIEW_ADDRESSES")) {
				if (pnlViewAddress.getNumberAddresses() > 0 && (!pnlViewAddress.isPrimaryAddressPresent())) {
					theAppMgr.showErrorDlg(res.getString("Please select primary address"));
					return;
				}
			}
			theAppMgr.showMenu(MenuConst.CUST_MGMT, theOpr);
			cardLayout.show(viewPortPanel, customerBasicPanel);
			pnlAddress.repaint();
			setEditArea(res.getString("Select option"), null, -1);
		}
		// Issue # 989
		else if (sAction.equalsIgnoreCase("ADD_CREDITCARD")) {
			theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
			creditCard = new CustomerCreditCard();
			//commented by sonali for Card on file 
			//String Builder = CMSPaymentMgr.getPaymentBuilder("CUSTOMER_CREDIT_CARD");
			//theAppMgr.buildObject("CREDIT_CARD", Builder, "");
			//AJB card on file
			String[] response = getResponse(creditCard);
			String[] respStatusCode ;
			String statusCode ="";
			
			if(response!=null){
			respStatusCode = parse(response[0]);
			statusCode = (respStatusCode)[3].toString();
			creditCard.setRespStatusCode(statusCode);
			}
			
	if(response==null){return;}
			if(statusCode.equals("0")){
			 	 setCustomerCreditAttributes(response,creditCard);
				}else{
					theAppMgr.showErrorDlg("This card is not approved");
					return;
				}	
		//Sonali:modified
			//	if(!checkCardToken(creditCard.getCardToken())){return;}

		  	
			if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
			showSignature(creditCard);
			}
			objectEvent("CREDIT_CARD",creditCard);
			return;
		} else if (sAction.equalsIgnoreCase("MODIFY_CREDITCARD")) {
			if (pnlViewCreditCard.getNumberCreditCard() < 1) {
				theAppMgr.showErrorDlg(res.getString("No Credit Card present"));
				return;
			}
			try {
				CustomerCreditCard modifyCreditCard = pnlViewCreditCard.modifySelectedCreditCard();
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				String Builder = CMSPaymentMgr.getPaymentBuilder("CUSTOMER_CREDIT_CARD");
				theAppMgr.buildObject("CREDIT_CARD", Builder, modifyCreditCard);
				return;
			} catch (Exception e) {
			}
			return;
		} else if (sAction.equalsIgnoreCase("REMOVE_CREDITCARD")) {
			if (pnlViewCreditCard.getNumberCreditCard() < 1) {
				theAppMgr.showErrorDlg(res.getString("No Credit Card present"));
				return;
			}
			try {
				cardInSession.remove(pnlViewCreditCard.getSelectedCreditCard().getCardToken());
				pnlViewCreditCard.deleteSelectedCreditCard();
				return;
			} catch (Exception e) {
			}
		}
		// End Issue # 989
		else if (sAction.equalsIgnoreCase("ADD_ADDRESS")) {
			int iAddressCount = 3;
			if ("US".equalsIgnoreCase(Version.CURRENT_REGION))
				iAddressCount = 5;
			if (pnlViewAddress.getNumberAddresses() >= iAddressCount) {
				theAppMgr.showErrorDlg(res.getString("Can't enter more addresses"));
				return;
			}
			AddressDlg dlg = new AddressDlg(theAppMgr.getParentFrame(), theAppMgr, pnlViewAddress, true);
			dlg.setAppMgr(theAppMgr);
			dlg.setVisible(true);
			return;
		} else if (sAction.equalsIgnoreCase("MODIFY_ADDRESS")) {
			if (pnlViewAddress.getNumberAddresses() < 1) {
				theAppMgr.showErrorDlg(res.getString("No Address present"));
				return;
			}
			AddressDlg dlg = new AddressDlg(theAppMgr.getParentFrame(), theAppMgr, pnlViewAddress, false);
			dlg.setAppMgr(theAppMgr);
			dlg.setVisible(true);
			return;
		} else if (sAction.equalsIgnoreCase("REMOVE_ADDRESS")) {
			if (pnlViewAddress.getNumberAddresses() < 1) {
				theAppMgr.showErrorDlg(res.getString("No Address present"));
				return;
			}
			try {
				pnlViewAddress.deleteSelectedAddress();
				return;
			} catch (Exception e) {
			}
		} else if (sAction.equals("ADD_COMMENTS")) {
			setEditArea("Enter Comment", "ADD_COMMENTS", theAppMgr.NO_MASK);
			return;
		}
	}
/*	private void checkCardToken(String string) {
		// TODO Auto-generated method stub
		boolean retry=theAppMgr.showOptionDlg("Card Error","Card already added", "retry", "Cancel");
					if(retry){
						String response=	((String[])	getResponse())[0];

						String[] tokenString=parse(response);
						checkCardToken(tokenString[33]);
					}else{return;}
				}*/
			
	private String[] getResponse(CustomerCreditCard creditcard) {
		// TODO Auto-generated method stub
		String[] response = null;
		if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){

	//	boolean retry;
		 response=(String[]) CMSCreditAuthHelper.getCardOnFileTokenResponse(theAppMgr,creditcard);
		if((response)[0].toString().contains("All the Ajb Servers are down at the moment"))
		{
	boolean retry=theAppMgr.showOptionDlg("AJB error","Payment Device is offline", "retry", "Cancel");
		if(retry){
		 response=(String[]) getResponse(creditCard);
		// return response;
			}else
				return null;
		}
		String[] status=parse(response[0]);
		//Changed the manual Auth response code from 3 to 2 
	//mayuri edhara :: added 2 and 3 for manual auth response codes
		if((status)[3].toString().equals("2") || (status)[3].toString().equals("3"))
		{
			theAppMgr.showErrorDlg("The card cant be added as connectivity to Vantive could not be established");
			return null;
		}
		//Its decline if AJB cant connect to bank which is response 3
		if((status)[3].toString().equals("1") || (status)[3].toString().equals("8"))
		{
			theAppMgr.showErrorDlg("The card is declined by AJB");
			return null;
	
		}
	

	
		//check if card is already added
	 if(!checkTokenNum((status)[33]))
			{
	 		 System.out.println("check if card is already added or not>>>>>>>>>>.");
	 		 theAppMgr.showErrorDlg("Card already added");
	 		return null;
			}
	 	//Sonali:added Approval message
			if((status)[3].toString().equals("0"))
			{
				theAppMgr.showErrorDlg("Card Approved");
			}
	 	  //If the card type is not present in SUPPORTED_CARD_TYPES in payment.cfg we cannot add that card
	 	/*  	if(!checkSupportedCreditCard(creditCard.getCreditCardType()))
	 	  		{
	 	  		theAppMgr.showErrorDlg("This type of credit card is not Supported");
	 	  		return null;
	 	  		}*/
	 
	 	 //Add card on file is not supported if link is down 
		}else {
	 		 theAppMgr.showErrorDlg("Cannot add card when AJB is offline");

		}
		return response;
		
	}

	private boolean checkSupportedCreditCard(String cardType) {
		// TODO Auto-generated method stub
		 ConfigMgr mgr = null;
		 mgr = new ConfigMgr("payment.cfg");
		String cardTypes= mgr.getString("SUPPORTED_CARD_TYPES");
		String[] cardTypeList=cardTypes.split(",");	
		for (int i=0;i<cardTypeList.length;i++){
		
		if(cardType.equalsIgnoreCase(cardTypeList[i]))
		{
			return true;
			}
		
		}
		return false;
	}

	private boolean checkTokenNum(String string) {
	
		// TODO Auto-generated method stub
		try {
			if(cardInSession.isEmpty()){
		 tokenList = CMSCustomerHelper.getCardTokens(cmsCustomer.getId(), theAppMgr);
		 for(int i=0; i<tokenList.length; i++){
		 cardInSession.add(tokenList[i]);
		 }
			}
		Iterator iter =  cardInSession.iterator();
	    while (iter.hasNext()) {
	        Object key = iter.next();
	        if (string.equals(key))	{
	        	return false;
			}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
		}
	cardInSession.add(string);
	return true;
		
	}
	
	private void setCustomerCreditAttributes(Object response,
			CustomerCreditCard cc) {
		// TODO Auto-generated method stub
	
		String ajbresponse = ((String[])response)[0];
		String[] responeString=parse(ajbresponse);
		cc.setCreditCardType(responeString[11]);
		cc.setStoreId(responeString[7]);
		cc.setIsNew(true);
		cc.setMaskedCreditCardNum(responeString[12]);
	    Calendar cal = CreditCardBldrUtil.getCalendar(responeString[13]);
		cc.setExpDate(cal.getTime());
	   //Added so that when remove is clicked it was not allowing to add
	    cc.setIsRemove(false);
	    cc.setCardToken(responeString[33]);
	}

	/**
	 * Button Event
	 * @param anEvent
	 *            CMSActionEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("TXN_HIST")) {
			if (cmsCustomer == null || cmsCustomer.getId() == null || cmsCustomer.getId().length() < 1) {
				theAppMgr.showErrorDlg(res.getString("Customer must exist in order to view transaction history"));
				anEvent.consume();
				return;
			}
			theAppMgr.addStateObject("TXN_CUSTOMER", cmsCustomer);
		}
		if (sAction.equals("MOD_CUST")) {
			// bShowPrevApplet = false;
			// PCR1325 Customer Details fix for Armani Japan
			if (custBasicPanelClassName == null) {
				pnlCustBasic.requestFocusOnFirstName();
			} else {
				pnlCustBasic.requestFocusOnLastName();
			}
			pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
			setModifyEnabled(true);
			// Fix for US issue 1566:
			// Customer Management - During a modify customer, the RETURNED MAIL check box stays grey and can't be checked
			if (enableReturnMail != null && enableReturnMail.equalsIgnoreCase("true")) {
				pnlCustBasic.setReturnMailEnabled(true);
			}
			theAppMgr.showMenu(MenuConst.OK_CANCEL, "MOD_CUST", theOpr);
		} else if (sAction.equals("NEW_CUST")) {
			if (theAppMgr.showOptionDlg(res.getString("Remove Current Customer"), "Are you sure ?")) {
				// PCR1325 Customer Details fix for Armani Japan
				if (custBasicPanelClassName == null) {
					pnlCustBasic.requestFocusOnFirstName();
				} else {
					pnlCustBasic.requestFocusOnLastName();
				}
				setCreateModeOn();
				if (enableReturnMail != null && enableReturnMail.equalsIgnoreCase("true")) {
					pnlCustBasic.setReturnMailEnabled(false);
				}
				bUpdateMode = false;
				bNewCustBasicDone = false;
			} else {
				// setCreateModeOn();
				// pnlCustBasic.requestFocusOnFirstName();
			}
		} else if (sAction.equals("VIEW_ADDRESSES")) {
			if (!bViewsEnabled) {
				showViewsDisabledMsg();
				return;
			}
			// Update Address from Main screen to ViewAddress
			pnlAddress.getAddress();
			try {
				if (pnlViewAddress.getNumberAddresses() < 1 && pnlAddress.isValidAddress()) {
					pnlViewAddress.addAddress(pnlAddress.getAddress());
				}
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(ex.getMessage());
			}
			pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
			theAppMgr.showMenu(MenuConst.VIEW_ADDRESS, "VIEW_ADDRESSES", theOpr);
			cardLayout.show(viewPortPanel, pnlViewAddress);
			setEditArea(res.getString("Select option"), null, -1);
			showTitle(false);
		}else if (sAction.equals("VIEW_COMMENTS")) {
			if (!bViewsEnabled) {
				showViewsDisabledMsg();
				return;
			} else {
				pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
				theAppMgr.showMenu(MenuConst.VIEW_COMMENTS, "VIEW_COMMENTS", theOpr);
				cardLayout.show(viewPortPanel, pnlViewComments);
				setEditArea(res.getString("Select option"), null, -1);
				showTitle(false);
			}
		}else if (sAction.equals("VIEW_CARD_DETAILS")) {
			if (!bViewsEnabled) {
				showViewsDisabledMsg();
				return;
			} else {
				pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
				// Issue # 989
				theAppMgr.showMenu(MenuConst.VIEW_CARD_DETAILS, "VIEW_CARD_DETAILS", theOpr);
				//sonali: AJB added to reload card details after pressing modify(which removes card from the list) and then previous
				pnlViewCreditCard.clear();
				List	listTmp = cmsCustomer.getCreditCard();
				for (int iCtr = 0; iCtr < listTmp.size(); iCtr++) {
					CustomerCreditCard creditCard = (CustomerCreditCard) listTmp.get(iCtr);
					String storeID = creditCard.getStoreId();
					// Add only Current Store's Credit Card and if token is not null
						if ((creditCard.getCardToken()) != null && storeID.equalsIgnoreCase(cmsStore.getId())) {
						pnlViewCreditCard.addCreditCard(creditCard);
					}
				}
				cardLayout.show(viewPortPanel, pnlViewCreditCard);
				setEditArea(res.getString("Select option"), null, -1);
				showTitle(false);
			}
		} else if (sAction.equals("TXN_HIST")) {
		}
		// added by Anand on 04/12/2005. Launch e-mail when the button pressed is LAUNCH_EMAIL.
		else if (sAction.equals("LAUNCH_EMAIL")) {
			if (pnlCustBasic.getPrimaryEmail() != null && pnlCustBasic.getPrimaryEmail().trim().length() > 0) {
				if (pnlMisc.getNoEmail()) {
					String[] params = new String[2];
					params[0] = (String) sAction;
					params[1] = pnlCustBasic.getPrimaryEmail();
					LaunchExternalApp launcher = new LaunchExternalApp();
					launcher.launchApp(params);
				} else {
					theAppMgr.showErrorDlg(res.getString("Customer has requested that no emails be sent"));
				}
			} else {
				theAppMgr.showErrorDlg(res.getString("E-mail client cannot be launched without a primary e-mail address"));
			}
		}
		// added by Anand on 04/12/2005. Launch CRM when the button pressed is LAUNCH_CRM.
		else if (sAction.equals("LAUNCH_CRM")) {
			if (theTxn.getRegisterId() != null && theTxn.getStore().getId() != null 
				&& (pnlCustBasic.getCustomerID() != null && pnlCustBasic.getCustomerID().trim().length() > 0)) {
				// if(pnlCustBasic.getPrimaryEmail() != null && pnlCustBasic.getPrimaryEmail().length() > 0){
				String[] params = new String[4];
				params[0] = (String) sAction;
				params[1] = theTxn.getStore().getId();
				params[2] = theTxn.getRegisterId();
				params[3] = pnlCustBasic.getCustomerID();
				LaunchExternalApp launcher = new LaunchExternalApp();
				launcher.launchApp(params);
			} else {
				if (theTxn.getRegisterId() == null) {
					theAppMgr.showErrorDlg(res.getString("CRM module cannot be launched without a valid register ID"));
				}
				if (theTxn.getStore().getId() == null) {
					theAppMgr.showErrorDlg(res.getString("CRM module cannot be launched without a valid store"));
				}
				if (pnlCustBasic.getCustomerID() == null || pnlCustBasic.getCustomerID().length() == 0) {
					theAppMgr.showErrorDlg(res.getString("CRM module cannot be launched without a valid customer"));
				}
			}
		} else if (sAction.equals("PURCHASE_HISTORY")) {
			if (!bViewsEnabled) {
				showViewsDisabledMsg();
				return;
			}
			if (loadPurchaseHistory())
				;
			{
				showTitle(false);
				pnlLastVisited = (JPanel) cardLayout.getCurrent(viewPortPanel);
				cardLayout.show(viewPortPanel, pnlViewPurchHistory);
				theAppMgr.showMenu(MenuConst.PREV_ONLY, "PURCHASE_HISTORY", theOpr);
				setEditArea(res.getString("Select option"), null, -1);
			}
		} else if (sAction.equals("LOYALTY")) {
			if (cmsCustomer == null || cmsCustomer.getId() == "" || cmsCustomer.getId().length() == 0) {
				anEvent.consume();
				theAppMgr.showErrorDlg(res.getString("Please save the customer"));
				theAppMgr.setSingleEditArea(res.getString("Select Option"));
			} else
				theAppMgr.showMenu(MenuConst.LOYALTY, "LOYALTY_OP", theOpr);
		} else if (sAction.equals("VIEW_DEPOSITS")) {
			theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
			theAppMgr.addStateObject("TO_DEPOSIT", "TO_DEPOSIT");
		} else if (sAction.equals("OK")) {
			if (pnlViewAddress.getNumberAddresses() > 0 && (!pnlViewAddress.isPrimaryAddressPresent())) {
				theAppMgr.showErrorDlg(res.getString("Please select primary address"));
				anEvent.consume();
				return;
			}  //Added for displaying Date as Field in Europe by Vivek
			if(!"EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				if (pnlMisc.getAssociateId() == null || pnlMisc.getAssociateId().trim().length() < 1) {
					theAppMgr.showErrorDlg(res.getString("Please fill in associate id"));
					anEvent.consume();
					return;
				}
			}			
			//this is added for US region to check the length of Associate ID doesnot more than 30 chars.
			//added by vivek
			 if(("US".equalsIgnoreCase(Version.CURRENT_REGION)) && pnlMisc.getAssociateId() != null && pnlMisc.getAssociateId().trim().length() > 30){
				theAppMgr.showErrorDlg(res.getString("Associate ID should not more than 30 characters"));
				pnlMisc.getAssociateIDTxt().setText("");
				pnlMisc.getAssociateIDTxt().requestFocus();
				anEvent.consume();
				return;
			}
			// Check Gender for US and EUROPE
			// Gender is Optional for JAPAN
			if ((("US".equalsIgnoreCase(Version.CURRENT_REGION)) || ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)))) {
				// ConfigMgr configMgr = new ConfigMgr("ArmaniCommon.cfg");
				ArmConfigLoader configMgr = new ArmConfigLoader();
				if (configMgr.getString("GENDER_" + pnlMisc.getSelectedGender() + ".LABEL") == null 
					|| configMgr.getString("GENDER_" + pnlMisc.getSelectedGender() + ".LABEL").trim().length() < 1) {
					theAppMgr.showErrorDlg(res.getString("Please select the gender"));
					anEvent.consume();
					return;
				}
			}
			if (pnlAddress.getCurrentAddressPanel() instanceof QASAddressPanel) {
				Address qasVerifiedAddress = ((QASAddressPanel) pnlAddress.getCurrentAddressPanel()).qasVerifyAddress();
				// YES/NO dialog
				if (qasVerifiedAddress != null
						&& ((qasVerifiedAddress.getQasVerifyLevel() != null && !qasVerifiedAddress.getQasVerifyLevel().equals(VERIFIED)) || qasVerifiedAddress.getQasVerifyLevel() == null)) {
					if (!theAppMgr.showOptionDlg("", res.getString("Do you want to continue?"))) {
						anEvent.consume();
						return;
					}
				}
			}
			// Get the latest primary address
			// Must be done so to make sure primary address is latest.
			pnlAddress.getAddress();
			try {
				if (pnlViewAddress.getNumberAddresses() < 1 && pnlAddress.isValidAddress()) {
					pnlViewAddress.addAddress(pnlAddress.getAddress());
				}
			} catch (BusinessRuleException ex1) {
				theAppMgr.showErrorDlg(ex1.getMessage());
			}
			setReadOnlyFields();
			// Fix for duplicate telephones type
			if (!telephonesValid()) {
				anEvent.consume();
				return;
			}
			if (!createModifyCustomer()) {
				anEvent.consume();
				return;
			}
			
			// BUG 1649
			if (theTxn == null || shouldSaveCustomer()) {
				if (!loyaltyEnrollmentAttempted)
				submitCustomer();
				cmsCustomer.setExisting();
				if (!loyaltyEnrollmentAttempted) {
					loyaltyEnrollmentAttempted = true;
					if (theAppMgr.isOnLine() && !cmsCustomer.isLoyaltyMember()
							&& (configMgr.getString("PROMPT_LOYALTY_ENROLLMENT") != null && "true".equalsIgnoreCase(configMgr.getString("PROMPT_LOYALTY_ENROLLMENT")))// for
																																										// JP
							&& isLoyaltyValidInStore()) {
						theAppMgr.unRegisterSingleEditArea();
						theAppMgr.showMenu(MenuConst.OK_CANCEL, "NEW_CUST_ENROLL_LOYALTY", theOpr);
						ConfigMgr loyaltyCfg = new ConfigMgr("loyalty.cfg");
						String builder = loyaltyCfg.getString("LOYALTY_BUILDER");
						theAppMgr.buildObject("ENROLL_LOYALTY", builder, "");
						anEvent.consume();
					}
				}
			}
			if (!bUpdateMode) {
				theAppMgr.addStateObject("NEW_CUSTOMER", cmsCustomer);
				// also make the newly created customer ready for lookup edit
				theAppMgr.addStateObject("CUSTOMER_LOOKUP", cmsCustomer);
			}
			// If Fiscal mode set the PrimaryAddress to be Default address.
			if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null) {
				theAppMgr.addStateObject("ARM_FISCAL_ADDRESS", cmsCustomer.getPrimaryAddress());
				theAppMgr.addStateObject("TXN_CUSTOMER", cmsCustomer);
			}
			/**
			 * Initializing the variables to INIT State.
			 */
			//Added for PRivacy Mgmt Marketing and Master
			else if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				 if (isMarketingMasterValid()) {
					anEvent.consume();
					return;
				}
				}
			bShowPrevApplet = false;
			bUpdateMode = false;
			bNewCustBasicDone = false;
			// bViewsEnabled = false;
		} else if (sAction.equals("PREV")) {
			cardInSession.clear();
			// theAppMgr.goBack();
		}
	}

  
	/*private boolean isPrivacySelected() {
	  if (pnlMisc.getPrivacy() == null || pnlMisc.getPrivacy().equalsIgnoreCase(defaultPrivacyCode)) {
		theAppMgr.showErrorDlg(res.getString("Privacy should be changed"));
		return false;
	  }
	  return true;
    }*/

	// BUG 1649
	/*
	 * Method to check if the current store brandID matches loyalty brand ID
	 */
	private boolean isLoyaltyValidInStore() {
		ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
		String brandID = cmsStore.getBrandID();
		StringTokenizer st = new StringTokenizer(loyaltyConfigMgr.getString("LOYALTY_CARDS"), ",");
		String loyaltyStrBrandID;
		while(st.hasMoreTokens()) {
			loyaltyStrBrandID = loyaltyConfigMgr.getString(st.nextToken() + ".TYPE");
			if (loyaltyStrBrandID != null && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim()))
				return true;
		}
		return false;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	private boolean shouldSaveCustomer() {
		return theAppMgr.isOnLine();
	}

	/**
	 * Set Edit area message
	 * @param sMessage
	 *            Message
	 * @param sCommand
	 *            Command
	 * @param iMask
	 *            Mask
	 */
	public void setEditArea(String sMessage, String sCommand, int iMask) {
		if (sCommand != null)
			theAppMgr.setSingleEditArea(res.getString(sMessage), sCommand, iMask);
		else
			theAppMgr.setSingleEditArea(res.getString(sMessage));
	}

	/**
	 * Load customer info into panels.
	 */
	private void loadCustomer() {
		try {
			int iCtr = 0;
			String sTmp;
			List listTmp;
			// Load Basic Information
			if (cmsCustomer == null)
				return;
			pnlMisc.reset();
			pnlCustBasic.setCustomerID(cmsCustomer.getId());
			pnlCustBasic.setTitle(cmsCustomer.getTitle());
			pnlCustBasic.setFirstName(cmsCustomer.getFirstName());
			pnlCustBasic.setFirstNameJP(cmsCustomer.getDBFirstName());
			pnlCustBasic.setMiddleName(cmsCustomer.getMiddleName());
			pnlCustBasic.setLastName(cmsCustomer.getLastName());
			pnlCustBasic.setLastNameJP(cmsCustomer.getDBLastName());
			pnlCustBasic.setSuffix(cmsCustomer.getSuffix());
			pnlCustBasic.setPrimaryEmail(cmsCustomer.getEMail());
			pnlCustBasic.setSecondaryEmail(cmsCustomer.getSecondaryEmail());
			pnlCustBasic.setReturnMail(cmsCustomer.isReceivingMail());
			// Load Misc. Info
			pnlMisc.setSelectedGender(cmsCustomer.getGender());
			pnlMisc.setSelectedCustomerType(cmsCustomer.getCustomerType());
			pnlMisc.setVIPPercentage("" + cmsCustomer.getVIPDiscount());
			//Added for Privacy Mgmt Marketing and Master
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				pnlMisc.setPrivacyMarketing(cmsCustomer.isPrivacyMarketing());
				pnlMisc.setPrivacyMaster(cmsCustomer.isPrivacyMaster());
			}else{
				if (cmsCustomer.isLoyaltyMember())
					pnlMisc.setLoyalityProgram(true);
				else
					pnlMisc.setLoyalityProgram(false);
				if (cmsCustomer.getPrivacyCode() != null)
					pnlMisc.setPrivacy(cmsCustomer.getPrivacyCode());
			}
			pnlMisc.setNoMail(cmsCustomer.isCanMail());
			pnlMisc.setNoEmail(cmsCustomer.isCanEmail());
			pnlMisc.setNoCall(cmsCustomer.isCanCall());
			pnlMisc.setNoSMS(cmsCustomer.isCanSMS());
			if (theAppMgr.getStateObject("ARM_REGISTER_VIP") != null) {
				CMSEmployee associate = (CMSEmployee) theAppMgr.getStateObject("ARM_REGISTER_VIP");
				pnlMisc.setAssociateId(associate.getExternalID());
				theAppMgr.removeStateObject("ARM_REGISTER_VIP");
			} else {
				// Set Default Associate
				// Last Sales Associate.
				listTmp = cmsCustomer.getSalesAssociates();
				for (iCtr = 0; iCtr < listTmp.size(); iCtr++) {
					CustomerSalesAssociate defaultAssc = (CustomerSalesAssociate) listTmp.get(iCtr);
					if (defaultAssc.getStoreId().equals(cmsStore.getId())) {
						pnlMisc.setAssociateId(defaultAssc.getAssocId());
						break;
					}
				}
				// If associate exists for this store.
				if ((sTmp = cmsCustomer.getAssociateForStore(cmsStore.getId())) != null) {
					sAssignedAssociate = sTmp;
					pnlMisc.setAssociateId(sTmp);
				} else {
					if (theTxn != null && theTxn instanceof CompositePOSTransaction && ((CompositePOSTransaction) theTxn).getConsultant() != null) {
						pnlMisc.setAssociateId(((CompositePOSTransaction) theTxn).getConsultant().getExternalID());
					} else if (theOpr != null)
						pnlMisc.setAssociateId(((CMSEmployee) theOpr).getExternalID());
					/**
					 * Added TextFilter here.
					 */
					// pnlMisc.getAssociateIDTxt().setDocument(new TextFilter(TextFilter.NUMERIC,8));
				}
			}
			// Load addresses
			pnlViewAddress.setCustomer(cmsCustomer);
			// Issue # 989
			listTmp = cmsCustomer.getCreditCard();
			for (iCtr = 0; iCtr < listTmp.size(); iCtr++) {
				CustomerCreditCard creditCard = (CustomerCreditCard) listTmp.get(iCtr);
				String storeID = creditCard.getStoreId();
				// Add only Current Store's Credit Card
				if (storeID.equalsIgnoreCase(cmsStore.getId())) {
					pnlViewCreditCard.addCreditCard(creditCard);
				}
			}
			// ENd Issue # 989
			listTmp = cmsCustomer.getCustomerComments();
			for (iCtr = 0; iCtr < listTmp.size(); iCtr++) {
				CustomerComment comment = (CustomerComment) listTmp.get(iCtr);
				pnlViewComments.addCustomerComment(comment);
			}
			listTmp = cmsCustomer.getNewCustomerComments();
			for (iCtr = 0; iCtr < listTmp.size(); iCtr++) {
				CustomerComment comment = (CustomerComment) listTmp.get(iCtr);
				pnlViewComments.addCustomerComment(comment);
			}
			pnlCardDetails.setCardNumber1(cmsCustomer.getCreditCardNum1());
			pnlCardDetails.setCardDetails1(cmsCustomer.getCreditCardType1());
			pnlCardDetails.setCardNumber2(cmsCustomer.getCreditCardNum2());
			pnlCardDetails.setCardDetails2(cmsCustomer.getCreditCardType2());
			// Detail info
			pnlCustDetail.setCompany(cmsCustomer.getCompanyId());
			pnlCustDetail.setInterCoCode(cmsCustomer.getInterCompanyCode());
			pnlCustDetail.setAcctNo(cmsCustomer.getAccountNum());
			pnlCustDetail.setStatus(cmsCustomer.getCustomerStatus());
			pnlCustDetail.setBarcode(cmsCustomer.getCustomerBC());
			// Birth Info
			if (cmsCustomer.getDateOfBirth() != null)
				pnlCustDetail.setDateOfBirth(DateFormatUtil.getFormattedDateString(cmsCustomer.getDateOfBirth()), cmsCustomer.getAge());
			// Fiscal Details
			pnlCustDetail.setVatNo(cmsCustomer.getVatNumber());
			pnlCustDetail.setSelectedPaymentType(cmsCustomer.getPymtType());
			pnlCustDetail.setFiscalCode(cmsCustomer.getFiscalCode());
			pnlCustDetail.setIDType(cmsCustomer.getIdType());
			pnlCustDetail.setDocNo(cmsCustomer.getDocNumber());
			pnlCustDetail.setPlcOfIssue(cmsCustomer.getPlaceOfIssue());
			if (cmsCustomer.getIssueDate() != null)
				pnlCustDetail.setDateOfIssue(DateFormatUtil.getFormattedDateString(cmsCustomer.getIssueDate()));
			pnlCustDetail.setSupplierPayment(cmsCustomer.getSupplierPymt());
			pnlCustDetail.setBank(cmsCustomer.getBank());
			pnlOtherPersInfo.setReferredBy(cmsCustomer.getRefBy());
			pnlOtherPersInfo.setSelectedProfession(cmsCustomer.getProfession());
			pnlOtherPersInfo.setSelectedEducation(cmsCustomer.getEducation());
			pnlOtherPersInfo.setNotes1(cmsCustomer.getNotes1());
			pnlOtherPersInfo.setNotes2(cmsCustomer.getNotes2());
			pnlOtherPersInfo.setPartnerFamilyName(cmsCustomer.getPtLastName());
			pnlOtherPersInfo.setPartnerName(cmsCustomer.getPtFirstName());
			pnlOtherPersInfo.setSelectedSpecialEvent(cmsCustomer.getSpEventType());
			if (cmsCustomer.getSpecialEventDate() != null)
				pnlOtherPersInfo.setSpecialDate(DateFormatUtil.getFormattedDateString(cmsCustomer.getSpecialEventDate()));
			pnlOtherPersInfo.setChildNames(cmsCustomer.getChildName());
			pnlOtherPersInfo.setNumChildren(cmsCustomer.getNumOfChildren());
			pnlOtherPersInfo.setBirthPlace(cmsCustomer.getPlaceOfBirth());
			this.doLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * See if customer's details were modified.
	 */
	private boolean isCustomerDetailsModified() {
		try {
			if ((customerInitial.getVatNumber() != null 
				&& cmsCustomer.getVatNumber().length() > 1) 
				&& !cmsCustomer.getVatNumber().equalsIgnoreCase(customerInitial.getVatNumber()))
				return true;
			if ((customerInitial.getFiscalCode() != null 
				&& cmsCustomer.getFiscalCode().length() > 1) 
				&& !cmsCustomer.getFiscalCode().equalsIgnoreCase(customerInitial.getFiscalCode()))
				return true;
			if ((customerInitial.getIdType() != null 
				&& cmsCustomer.getIdType().length() > 1) 
				&& !cmsCustomer.getIdType().equalsIgnoreCase(customerInitial.getIdType()))
				return true;
			if ((customerInitial.getDocNumber() != null 
				&& cmsCustomer.getDocNumber().length() > 1) 
				&& !cmsCustomer.getDocNumber().equalsIgnoreCase(customerInitial.getDocNumber()))
				return true;
			if ((customerInitial.getPlaceOfIssue() != null 
				&& cmsCustomer.getPlaceOfIssue().length() > 1) 
				&& !cmsCustomer.getPlaceOfIssue().equalsIgnoreCase(customerInitial.getPlaceOfIssue()))
				return true;
			if (!equalsNullsOK(cmsCustomer.getIssueDate(), customerInitial.getIssueDate()))
				return true;
			if ((customerInitial.getPymtType() != null 
				&& cmsCustomer.getPymtType().length() > 1) 
				&& !cmsCustomer.getPymtType().equalsIgnoreCase(customerInitial.getPymtType()))
				return true;
			if ((customerInitial.getCompanyId() != null 
				&& cmsCustomer.getCompanyId().length() > 0) 
				&& !cmsCustomer.getCompanyId().equalsIgnoreCase(customerInitial.getCompanyId()))
				return true;
			if ((customerInitial.getTitle() != null && cmsCustomer.getTitle().length() > 1) && !cmsCustomer.getInterCompanyCode().equalsIgnoreCase(customerInitial.getInterCompanyCode()))
				return true;
			if ((customerInitial.getCustomerStatus() != null 
				&& cmsCustomer.getCustomerStatus().length() > 1) 
				&& !cmsCustomer.getCustomerStatus().equalsIgnoreCase(customerInitial.getCustomerStatus()))
				return true;
			if ((customerInitial.getAccountNum() != null 
				&& cmsCustomer.getAccountNum().length() > 1) 
				&& !cmsCustomer.getAccountNum().equalsIgnoreCase(customerInitial.getAccountNum()))
				return true;
			if (!equalsNullsOK(cmsCustomer.getDateOfBirth(), customerInitial.getDateOfBirth()))
				return true;
			if ((customerInitial.getAge() != null 
				&& cmsCustomer.getAge().length() > 0) 
				&& !cmsCustomer.getAge().equalsIgnoreCase(customerInitial.getAge()))
				return true;
			if ((customerInitial.getRefBy() != null 
				&& cmsCustomer.getRefBy().length() > 1) 
				&& !cmsCustomer.getRefBy().equalsIgnoreCase(customerInitial.getRefBy()))
				return true;
			if ((customerInitial.getProfession() != null 
				&& cmsCustomer.getProfession().length() > 1) 
				&& !cmsCustomer.getProfession().equalsIgnoreCase(customerInitial.getProfession()))
				return true;
			if ((customerInitial.getEducation() != null 
				&& cmsCustomer.getEducation().length() > 1) 
				&& !cmsCustomer.getEducation().equalsIgnoreCase(customerInitial.getEducation()))
				return true;
			if ((customerInitial.getNotes1() != null 
				&& cmsCustomer.getNotes1().length() > 1) 
				&& !cmsCustomer.getNotes1().equalsIgnoreCase(customerInitial.getNotes1()))
				return true;
			if ((customerInitial.getNotes2() != null 
				&& cmsCustomer.getNotes2().length() > 1) 
				&& !cmsCustomer.getNotes2().equalsIgnoreCase(customerInitial.getNotes2()))
				return true;
			if ((customerInitial.getPtFirstName() != null 
				&& cmsCustomer.getPtFirstName().length() > 1) 
				&& !cmsCustomer.getPtFirstName().equalsIgnoreCase(customerInitial.getPtFirstName()))
				return true;
			if ((customerInitial.getPtLastName() != null 
				&& cmsCustomer.getPtLastName().length() > 1) 
				&& !cmsCustomer.getPtLastName().equalsIgnoreCase(customerInitial.getPtLastName()))
				return true;
			if ((customerInitial.getPlaceOfBirth() != null 
				&& cmsCustomer.getPlaceOfBirth().length() > 1) 
				&& !cmsCustomer.getPlaceOfBirth().equalsIgnoreCase(customerInitial.getPlaceOfBirth()))
				return true;
			if ((customerInitial.getSpEventType() != null 
				&& cmsCustomer.getSpEventType().length() > 1) 
				&& !cmsCustomer.getSpEventType().equalsIgnoreCase(customerInitial.getSpEventType()))
				return true;
			if (!equalsNullsOK(cmsCustomer.getSpecialEventDate(), customerInitial.getSpecialEventDate()))
				return true;
			if ((customerInitial.getChildName() != null 
				&& cmsCustomer.getChildName().length() > 1) 
				&& !cmsCustomer.getChildName().equalsIgnoreCase(customerInitial.getChildName()))
				return true;
			if ((customerInitial.getNumOfChildren() != null 
				&& cmsCustomer.getNumOfChildren().length() > 1) 
				&& !cmsCustomer.getNumOfChildren().equalsIgnoreCase(customerInitial.getNumOfChildren()))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * put your documentation comment here
	 * @param o1
	 * @param o2
	 * @return
	 */
	private static boolean equalsNullsOK(Object o1, Object o2) {
		return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
	}

	/**
	 * See if customer's Basic information was modified.
	 * 
	 * @return boolean
	 */
	private boolean isCustomerBasicInfoModified() {
		try {
			if ((customerInitial.getTitle() != null 
				&& cmsCustomer.getTitle().length() > 1) 
				&& !cmsCustomer.getTitle().equalsIgnoreCase(customerInitial.getTitle()))
				return true;
			if ((customerInitial.getCustomerBC() != null 
				&& cmsCustomer.getCustomerBC().length() > 1) 
				&& !cmsCustomer.getCustomerBC().equalsIgnoreCase(customerInitial.getCustomerBC()))
				return true;
			if ((customerInitial.getCustomerType() != null 
				&& cmsCustomer.getCustomerType().length() > 1) 
				&& !cmsCustomer.getCustomerType().equalsIgnoreCase(customerInitial.getCustomerType()))
				return true;
			if ((customerInitial.getFirstName() != null 
				&& cmsCustomer.getFirstName().length() > 1) 
				&& !cmsCustomer.getFirstName().equalsIgnoreCase(customerInitial.getFirstName()))
				return true;
			if ((customerInitial.getLastName() != null 
				&& cmsCustomer.getLastName().length() > 1) 
				&& !cmsCustomer.getLastName().equalsIgnoreCase(customerInitial.getLastName()))
				return true;
			if ((customerInitial.getDBLastName() != null 
				&& cmsCustomer.getDBLastName().length() > 1) 
				&& !cmsCustomer.getDBLastName().equalsIgnoreCase(customerInitial.getDBLastName()))
				return true;
			if ((customerInitial.getDBFirstName() != null 
				&& cmsCustomer.getDBFirstName().length() > 1) 
				&& !cmsCustomer.getDBFirstName().equalsIgnoreCase(customerInitial.getDBFirstName()))
				return true;

			if ((customerInitial.getMiddleName() != null 
				&& cmsCustomer.getMiddleName().length() > 1) 
				&& !cmsCustomer.getMiddleName().equalsIgnoreCase(customerInitial.getMiddleName()))
				return true;
			if ((customerInitial.getSuffix() != null 
				&& cmsCustomer.getSuffix().length() > 1) 
				&& !cmsCustomer.getSuffix().equalsIgnoreCase(customerInitial.getSuffix()))
				return true;
			if ((customerInitial.getPrivacyCode() != null 
				&& cmsCustomer.getPrivacyCode().length() > 1) 
				&& !cmsCustomer.getPrivacyCode().equalsIgnoreCase(customerInitial.getPrivacyCode()))
				return true;
			if ((customerInitial.getEMail() != null 
				&& cmsCustomer.getEMail().length() > 1) 
				&& !cmsCustomer.getEMail().equalsIgnoreCase(customerInitial.getEMail()))
				return true;
			if ((customerInitial.getSecondaryEmail() != null 
				&& cmsCustomer.getSecondaryEmail().length() > 1) 
				&& !cmsCustomer.getSecondaryEmail().equalsIgnoreCase(customerInitial.getSecondaryEmail()))
				return true;
			if ((customerInitial.getGender() != null 
				&& cmsCustomer.getGender().length() > 1) 
				&& !cmsCustomer.getGender().equalsIgnoreCase(customerInitial.getGender()))
				return true;
			if (cmsCustomer.isCanCall() != customerInitial.isCanCall())
				return true;
			if (cmsCustomer.isCanEmail() != customerInitial.isCanEmail())
				return true;
			if (cmsCustomer.isCanSMS() != customerInitial.isCanSMS())
				return true;
			if (cmsCustomer.isLoyaltyMember() != customerInitial.isLoyaltyMember())
				return true;
			if (cmsCustomer.getVIPDiscount() != customerInitial.getVIPDiscount())
				return true;
			if (cmsCustomer.isPrivacyMarketing() != customerInitial.isPrivacyMarketing())
					return true;
			if (cmsCustomer.isPrivacyMaster() != customerInitial.isPrivacyMaster())
					return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Create/Modify Customerf
	 */
	private boolean createModifyCustomer() {
		try {
			int iCtr = 0;
			Vector vRows = new Vector();
			Vector vCCRows = new Vector();
			if (cmsCustomer == null)
				cmsCustomer = new CMSCustomer();
			if (cmsCustomer.getId() == null || cmsCustomer.getId().length() < 1) {
				cmsCustomer.setNew();
			} else {
				cmsCustomer.setModified();
			}
		// vishal : for customer_id prefix in europe for FRANCHISING store

						if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
							if(configMgr.getString("FRANCHISING_SHOP")!=null){
							String FRANCHISING_SHOP=configMgr.getString("FRANCHISING_SHOP");
							if(FRANCHISING_SHOP.toLowerCase().startsWith("y")||FRANCHISING_SHOP.equalsIgnoreCase("y")){
								cmsCustomer.setFranchising_Store(true);
							}
							}
							if(configMgr.getString("STAGING_CUSTID")!=null){
								String STAGING_CUSTID=configMgr.getString("STAGING_CUSTID");
								if(STAGING_CUSTID.toLowerCase().startsWith("y")||STAGING_CUSTID.equalsIgnoreCase("y")){
									cmsCustomer.setStaging_cust_id(true);
								}
						}
						}	
						//end vishal 14 sept 2016
			
			// Europe- Update all the customer information into Staging Table
			// Europe- Update all the customer information into Staging Table
			if (updateAllStgTbl) {
				cmsCustomer.setIsUpdateAllStgTbl(true);
			}
			CustomerSalesAssociate salesAss = new CustomerSalesAssociate();
			cmsCustomer.setTitle(pnlCustBasic.getTitle());
			// Verify if its valid First Name
			// if Store country isnt Japan
			if (!("JP".equalsIgnoreCase(Version.CURRENT_REGION))) {
				if (pnlCustBasic.getFirstName().trim().length() < 1) {
					theAppMgr.showErrorDlg("First Name required");
					showTitle(true);
					cardLayout.show(viewPortPanel, customerBasicPanel);
					pnlCustBasic.setFirstName("");
					pnlCustBasic.requestFocusOnFirstName();
					return false;
				}
			}
			cmsCustomer.setFirstName(pnlCustBasic.getFirstName());
			cmsCustomer.setDBFirstName(pnlCustBasic.getFirstNameJP());
			cmsCustomer.setMiddleName(pnlCustBasic.getMiddleName());
			cmsCustomer.setLastName(pnlCustBasic.getLastName());
			cmsCustomer.setDBLastName(pnlCustBasic.getLastNameJP());
			cmsCustomer.setSuffix(pnlCustBasic.getSuffix());
			cmsCustomer.setEMail(pnlCustBasic.getPrimaryEmail());
			cmsCustomer.setSecondaryEmail(pnlCustBasic.getSecondaryEmail());
			if (enableReturnMail != null && enableReturnMail.equalsIgnoreCase("true")) {
				cmsCustomer.doSetReceiveMail(pnlCustBasic.getReturnMail());
			}
			if (!isAddressValid())
				return false;
			if (pnlMisc.getSelectedGender() != null)
				cmsCustomer.setGender(pnlMisc.getSelectedGender());
			if (pnlMisc.getSelectedCustomerType() != null)
				cmsCustomer.setCustomerType(pnlMisc.getSelectedCustomerType());
			if (pnlMisc.getLoyalityProgram())
				cmsCustomer.setLoyaltyMember();
			else
				cmsCustomer.setNotLoyaltyMember();
			if (pnlMisc.getPrivacy() != null)
				cmsCustomer.setPrivacyCode(pnlMisc.getPrivacy());
			/*if("EUR".equalsIgnoreCase(Version.CURRENT_REGION) && !isPrivacySelected()){
				return false;
	        }*/

			// MP: If allowed set as true else as false.
			if (pnlMisc.getNoMail())
				cmsCustomer.setCanMail(true);
			else
				cmsCustomer.setCanMail(false);
			//Added for Privacy Mgmt Marketing and Master
			if (pnlMisc.getPrivacyMarketing())
				cmsCustomer.setPrivacyMarketing(true);
			else
				cmsCustomer.setPrivacyMarketing(false);
			
			if (pnlMisc.getPrivacyMaster())
				cmsCustomer.setPrivacyMaster(true);
			else
				cmsCustomer.setPrivacyMaster(false);	
           if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
        	   if(isCustomerModified()){
   				cmsCustomer.setCustIssueDate(new Date());
   			}
   			else{
   				String dt = pnlMisc.getDate();
   				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
   				Date modDate = (Date)formatter.parse(dt); 
  				if(modDate!=null){
   				 cmsCustomer.setCustIssueDate(modDate);
   				}else{
   					cmsCustomer.setCustIssueDate(new Date());
   				}
   			}
                 
           }
           else {
        		cmsCustomer.setCustIssueDate(null);
           }
			
			//---------------------------------
			if (pnlMisc.getNoEmail())
				cmsCustomer.setCanEmail(true);
			else
				cmsCustomer.setCanEmail(false);
			if (pnlMisc.getNoCall())
				cmsCustomer.setCanCall(true);
			else
				cmsCustomer.setCanCall(false);
			if (pnlMisc.getNoSMS())
				cmsCustomer.setCanSMS(true);
			else
				cmsCustomer.setCanSMS(false);
			if (bUpdateMode) {
				if (updateAllStgTbl) {
					List salesAssocList = cmsCustomer.getSalesAssociates();
					for (iCtr = 0; iCtr < salesAssocList.size(); iCtr++) {
						CustomerSalesAssociate defaultAssc = (CustomerSalesAssociate) salesAssocList.get(iCtr);
						if (defaultAssc.getStoreId().equals(cmsStore.getId())) {
							defaultAssc.setIsModified(true);
							break;
						}
					}
				}
				if (!sAssignedAssociate.equals(pnlMisc.getAssociateId()))
					cmsCustomer.setAssociateForStore(cmsStore.getId(), pnlMisc.getAssociateId());
			} else {
				salesAss.setAssocId(pnlMisc.getAssociateId());
				salesAss.setStoreId(cmsStore.getId());
				cmsCustomer.addSalesAssociate(salesAss);
			}
			// -- to do Customer details
			cmsCustomer.setCompanyId(pnlCustDetail.getCompany());
			cmsCustomer.setInterCompanyCode(pnlCustDetail.getInterCoCode());
			cmsCustomer.setAccountNum(pnlCustDetail.getAcctNo());
			cmsCustomer.setBank(pnlCustDetail.getBank());
			cmsCustomer.setCustomerBC(pnlCustDetail.getBarcode());
			cmsCustomer.setCustomerStatus(pnlCustDetail.getStatus());
			try {
				cmsCustomer.setDateOfBirth(pnlCustDetail.getBirthDate());
			} catch (Exception e) {
			}
			cmsCustomer.setAge(pnlCustDetail.getAgeRangeCode());
			cmsCustomer.setVatNumber(pnlCustDetail.getVatNo());
			cmsCustomer.setFiscalCode(pnlCustDetail.getFiscalCode());
			cmsCustomer.setIdType(pnlCustDetail.getIDType());
			cmsCustomer.setDocNumber(pnlCustDetail.getDocNo());
			cmsCustomer.setPlaceOfIssue(pnlCustDetail.getPlcOfIssue());
			cmsCustomer.setSupplierPymt(pnlCustDetail.getSupplierPayment());
			try {
				cmsCustomer.setIssueDate(DateFormatUtil.getFormattedDate(pnlCustDetail.getDateOfIssue()));
			} catch (Exception e) {
				cmsCustomer.setIssueDate(null);
			}
			cmsCustomer.setPymtType(pnlCustDetail.getSelectedPaymentType().trim());
			cmsCustomer.setCreditCardNum1(pnlCardDetails.getCardNumber1().trim());
			cmsCustomer.setCreditCardNum2(pnlCardDetails.getCardNumber2().trim());
			cmsCustomer.setCreditCardType1(pnlCardDetails.getCardDetails1().trim());
			cmsCustomer.setCreditCardType2(pnlCardDetails.getCardDetails2().trim());
			cmsCustomer.setRefBy(pnlOtherPersInfo.getReferredBy().trim());
			cmsCustomer.setProfession(pnlOtherPersInfo.getSelectedProfession().trim());
			cmsCustomer.setEducation(pnlOtherPersInfo.getSelectedEducation().trim());
			cmsCustomer.setNotes1(pnlOtherPersInfo.getNotes1().trim());
			cmsCustomer.setNotes2(pnlOtherPersInfo.getNotes2().trim());
			cmsCustomer.setPtFirstName(pnlOtherPersInfo.getPartnerName().trim());
			cmsCustomer.setPtLastName(pnlOtherPersInfo.getPartnerFamilyName().trim());
			cmsCustomer.setPlaceOfBirth(pnlOtherPersInfo.getBirthPlace().trim());
			cmsCustomer.setSpEventType(pnlOtherPersInfo.getSelectedSpecialEvent().trim());
			if (pnlCardDetails != null) {
				cmsCustomer.setCreditCardNum1(pnlCardDetails.getCardNumber1());
				cmsCustomer.setCreditCardType1(pnlCardDetails.getCardDetails1());
				cmsCustomer.setCreditCardNum2(pnlCardDetails.getCardNumber2());
				cmsCustomer.setCreditCardType2(pnlCardDetails.getCardDetails2());
			}
			try {
				cmsCustomer.setSpecialEventDate(DateFormatUtil.getFormattedDate(pnlOtherPersInfo.getSpecialDate()));
			} catch (Exception e) {
				cmsCustomer.setSpecialEventDate(null);
			}
			cmsCustomer.setChildName(pnlOtherPersInfo.getChildNames().trim());
			cmsCustomer.setNumOfChildren(pnlOtherPersInfo.getNumChildren().trim());
			if (theAppMgr.isOnLine())
				cmsCustomer.setCreateOffline("N");
			else
				cmsCustomer.setCreateOffline("Y");
			vRows = pnlViewComments.getCommentModel().getAllRows();
			if (cmsCustomer.getId() != null && cmsCustomer.getId().length() > 0 && vRows != null) {
				cmsCustomer.getNewCustomerComments().clear();
				for (iCtr = 0; iCtr < vRows.size(); iCtr++) {
					if (((CustomerComment) vRows.elementAt(iCtr)).isModified())
						cmsCustomer.addNewCustomerComment((CustomerComment) vRows.elementAt(iCtr));
				}
			} else if (vRows != null) {
				for (iCtr = 0; iCtr < vRows.size(); iCtr++) {
					cmsCustomer.addCustomerComment((CustomerComment) vRows.elementAt(iCtr));
				}
			}
			// Issue # 989
			vCCRows = pnlViewCreditCard.getCreditCardModel().getAllRows();
			if (vCCRows != null) {
				// Clear the Credit Cards
				cmsCustomer.getCreditCard().clear();
				for (iCtr = 0; iCtr < vCCRows.size(); iCtr++) {
					// Check for New Credit Card
					CustomerCreditCard creditCard = (CustomerCreditCard) vCCRows.elementAt(iCtr);
					if (creditCard.isNew()) {
						cmsCustomer.addCreditCard(creditCard);
					} else {
						if (creditCard.isModified()) {
							cmsCustomer.addCreditCard(creditCard);
						}
					}
				}
			}
			// Remove Existing Credit Card
			vCCRows = pnlViewCreditCard.getCreditCardModel().getVRemoveCC();
			if (vCCRows != null) {
				for (iCtr = 0; iCtr < vCCRows.size(); iCtr++) {
					// Check for New Credit Card
					CustomerCreditCard creditCard = (CustomerCreditCard) vCCRows.elementAt(iCtr);
					if (creditCard.isRemove()) {
						if (creditCard.isNew()) {
							// System.out.println("Do Nothing");
						} else
							cmsCustomer.addCreditCard(creditCard);
					}
				}
			}
			// End Issue # 989
			if (customerInitial != null) {
				// Europe- Update all the customer information into Staging Table
				if (updateAllStgTbl) {
					cmsCustomer.setIsModifiedForB(true);
					cmsCustomer.setIsModifiedForD(true);
				} else {
					cmsCustomer.setIsModifiedForB(isCustomerBasicInfoModified());
					cmsCustomer.setIsModifiedForD(isCustomerDetailsModified());
				}
			}
			return true;
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			return false;
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
			return false;
		}
	}

	/**
	 * Reset the fields.
	 */
	public void reset() {
		pnlCustBasic.reset();
		if (pnlViewAddress != null)
			pnlViewAddress.clear();
		pnlAddress.reset();
		pnlMisc.reset();
		pnlCustDetail.reset();
		pnlOtherPersInfo.reset();
		if (pnlViewComments != null)
			pnlViewComments.clear();
		if (pnlViewCreditCard != null)
			pnlViewCreditCard.clear();
		if (pnlCardDetails != null)
			pnlCardDetails.reset();
		if (pnlViewPurchHistory != null)
			pnlViewPurchHistory.clear();
	}

	/**
	 * Enable pages.
	 * 
	 * @param bEnabled
	 *            True/False
	 */
	public void setEnabled(boolean bEnabled) {
		pnlCustBasic.setEnabled(bEnabled);
		pnlAddress.setEnabled(bEnabled);
		pnlMisc.setEnabled(bEnabled);
		pnlCustDetail.setEnabled(bEnabled);
		pnlOtherPersInfo.setEnabled(bEnabled);
	}

	/**
	 * Enable modify.
	 * 
	 * @param bEnabled
	 *            True/False
	 */
	public void setModifyEnabled(boolean bEnabled) {
		pnlCustBasic.setEnabled(bEnabled);
		pnlAddress.setModifyEnabled(bEnabled);
		pnlMisc.setEnabled(bEnabled);
		pnlCustDetail.setEnabled(bEnabled);
		pnlOtherPersInfo.setEnabled(bEnabled);
	}

	private class AddressTypeListener implements ItemListener {

		/**
		 * put your documentation comment here
		 * @param itmEvt
		 */
		public void itemStateChanged(ItemEvent itmEvt) {
			JCMSComboBox comp = (JCMSComboBox) itmEvt.getSource();
			if (bConsumeEvent)
				return;
			if (comp.getName().equals("ADDRESS_TYPE")) {
				if (pnlViewAddress != null && pnlViewAddress.getNumberAddresses() > 1 && pnlViewAddress.addressTypeExists(pnlAddress.getAddressType()) != -1) {
					bConsumeEvent = true;
					comp.removeItemListener(this);
					theAppMgr.showErrorDlg(res.getString("Address type already exists, please select a different type"));
					pnlAddress.setAddressType(pnlViewAddress.getPrimaryAddress().getAddressType());
					comp.addItemListener(this);
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
									bConsumeEvent = false;
								}
							});
						}
					});
				}
			}
		}
	}

  /**
   *
   * <p>Title: CustomerMgmtEvtListener</p>
   * <p>Description: Traps Mouse and Tab events. </p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
	private class CustomerMgmtEvtListener implements AWTEventListener {

		/**
		 * put your documentation comment here
		 * @param awtEvent
		 */
		public void eventDispatched(AWTEvent awtEvent) {
			// - MSB 01/26/2006
			// Focus lost event for text fields is fired before
			// appButton event if button is pressed using
			// mouse. So in case of CANCEL event when everything has to be ignored
			// and no validation has to be done, focusEvent would still take
			// precedence and validate the data. To stop this event had to be captured
			// at event que level and checked if its CANCEL button event, focus Event validation
			// should be ignored.
			if (awtEvent instanceof MouseEvent) {
				MouseEvent me = (MouseEvent) awtEvent;
				if (me.getClickCount() == 1) {
					if (me.getComponent() instanceof NonFocusableButton) {
						NonFocusableButton button = (NonFocusableButton) me.getComponent();
						if (button.getMenuOption() == null)
							return;
						if (button.getMenuOption().getCommand().equals("CANCEL") && cardLayout.getCurrent(viewPortPanel) instanceof CustomerDetailPanel) {
							// State object to tell focuse event has to be consumed.
							theAppMgr.addStateObject("ARM_CONSUME_FOCUS_EVT", "TRUE");
						}
					}
				}
				return;
			}
			KeyEvent keyEvent;
			try {
				// Typecast to KeyEvent
				keyEvent = (KeyEvent) awtEvent;
			} catch (ClassCastException ce) {
				ce.printStackTrace();
				return;
			}
			
			//MB : 11/19/2007: Fixed for issue#1883-- Start---
			//Document listener for the name fields 
			//isn't taking care of characters
			//when SHIFT key is used.
			//Filter SHIFT key for non alphanumeric characters.
			if(keyEvent.isShiftDown() && awtEvent.getSource() instanceof JCMSTextField)
				{			
					String txtFieldName = ((Component) awtEvent.getSource()).getName().trim().toUpperCase();
					if( (txtFieldName.indexOf("FIRST_NAME")!=-1) || (txtFieldName.indexOf("SECOND_NAME")!=-1))
					{
						if(CustomerBasicPanel.NAME_ALPHA_NUMERIC.indexOf(keyEvent.getKeyChar())==-1)
						{
							keyEvent.consume();
							return;
						}						
					}
				}
			//MB : 11/19/2007: Fixed for issue#1883-- End ---
			
			
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
				if (awtEvent.getSource() instanceof JButton) {
					JButton btnEvent = (JButton) awtEvent.getSource();
					if (btnEvent.getActionCommand().equals("Lookup")) {
						if (!theAppMgr.isOnLine()) {
							theAppMgr.showErrorDlg(res.getString("Search not available this time"));
							btnEvent.setEnabled(false);
							return;
						}
						theAppMgr.fireButtonEvent("CUST_LOOKUP");
					}
				}
			}
			// Check If its TAB event.
			if (keyEvent.getKeyCode() == KeyEvent.VK_TAB) {
				// JCMSTextField is source
				if (awtEvent.getSource() instanceof JCMSTextField) {
					compTabEvent = (Component) awtEvent.getSource();
					/*
					 * if (compTabEvent.getName().trim().equals("AssociateID")) { pnlCustBasic.setFocusOnTitle(); return; } else
					 */if (compTabEvent.getName().trim().equals("AssociateID") || compTabEvent.getName().trim().equals("ChildNames")/* ||compTabEvent.getName().trim().equals("DateOfIssue") */) {
						invokeFocusThread();
						return;
					}
				}
				if (awtEvent.getSource() instanceof JCMSComboBox) {
					compTabEvent = (Component) awtEvent.getSource();
					// System.out.println("Event Name : " + compTabEvent.getName());
					if (compTabEvent.getName().indexOf("PHONE") != -1) {
						int iIndexDuplicatePhone = pnlAddress.containsDuplicatePhone();
						if (iIndexDuplicatePhone != -1) {
							theAppMgr.showErrorDlg(res.getString("Duplicate phone type, please select a different type"));
							((Component) awtEvent.getSource()).requestFocus();
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Invoke thread to switch panels.
	 */
	private void invokeFocusThread() {
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
						switchPanel();
					}
				});
			}
		});
	}

	/**
	 * Switch panels
	 */
	private void switchPanel() {
		// Check if TAB event was result of
		// focus lost not focus gain.
		if (!compTabEvent.hasFocus()) {
			if (compTabEvent.getName().trim().equals("ChildNames"))
				cardLayout.first(viewPortPanel);
			else
				cardLayout.next(viewPortPanel);
			if (cardLayout.getCurrent(viewPortPanel).equals(customerBasicPanel)) {
				showTitle(true);
				// PCR1325 Customer Details fix for Armani Japan
				if (custBasicPanelClassName == null) {
					pnlCustBasic.requestFocusOnFirstName();
				} else {
					pnlCustBasic.requestFocusOnLastName();
				}
			} else if (cardLayout.getCurrent(viewPortPanel) instanceof CustomerDetailPanel) {
				showTitle(false);
				pnlCustDetail.requestFocusOnFirstField();
			} else if (cardLayout.getCurrent(viewPortPanel) instanceof OtherPersonalInfoPanel) {
				pnlOtherPersInfo.requestFocusOnFirstField();
			}
		}
	}

	// ------------------------------------------------------------------
	// ********************* Verifier Classes ***********************
	// ------------------------------------------------------------------
	private class Verifiers {

		/**
		 * @return
		 */
		public CMSInputVerifier getFirstNameVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent c) {
					try {
						CMSCustomer cms = new CMSCustomer();
						if (cms != null)
							// Verify if its valid first name.
							cms.verifyFirstName(pnlCustBasic.getFirstName());
						return (true);
					} catch (BusinessRuleException bex) {
						theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
						return (false);
					}
				}
			});
		} // getLastName

		/**
		 * put your documentation comment here
		 * @return
		 */
		public CMSInputVerifier getLastNameVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent c) {
					try {
						CMSCustomer cms = new CMSCustomer();
						cms.setLastName(pnlCustBasic.getLastName());
						return (true);
					} catch (BusinessRuleException bex) {
						theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
						return (false);
					}
				}
			});
		} // getLastName

		/**
		 * put your documentation comment here
		 * @return
		 */
		public CMSInputVerifier getRealBDVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent c) {
					try {
						CMSCustomer cms = new CMSCustomer();
						// cms.setBirthDate(pnlCustDetail.getBirthDate());
						cms.setDateOfBirth(pnlCustDetail.getBirthDate());
						return (true);
					} catch (Exception bex) {
						theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
						return (false);
					}
				}
			});
		} // getRealBDVerifier

		/**
		 * put your documentation comment here
		 * @return
		 */
		public CMSInputVerifier getIssueDateVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * put your documentation comment here
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent c) {
					try {
						CMSCustomer cms = new CMSCustomer();
						// cms.setIssueDate(pnlCustDetail.getDateOfIssue());
						String issueDate = pnlCustDetail.getDateOfIssue();
						if (issueDate == null || issueDate.length() <= 0) {
							showTitle(false);
							//cardLayout.show(viewPortPanel, pnlOtherPersInfo);
							return true;
						} else {
							cms.setIssueDate(DateFormatUtil.getFormattedDate(issueDate));
							showTitle(false);
							//cardLayout.show(viewPortPanel, pnlOtherPersInfo);
							return (true);
						}
					} catch (Exception bex) {
						theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
						return (false);
					}
				}
			});
		} // getIssueDateVerifier

		//This is used for only to show next page of customer details.
		public CMSInputVerifier getLastFieldOfCustDetailVerifier() {
			return (new CMSInputVerifier() {
				/**
				 * put your documentation comment here
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent c) {
					cardLayout.show(viewPortPanel, pnlOtherPersInfo);
					return true;
				}
			});
		} // getLastFieldOfCustDetailVerifier
		
		
		/**
		 * put your documentation comment here
		 * @return
		 */
		public CMSInputVerifier getSpecialEDVerifier() {
			return (new CMSInputVerifier() {

				/**
				 * @param c
				 * @return
				 */
				public boolean verify(JComponent c) {
					try {
						CMSCustomer cms = new CMSCustomer();
						// cms.setSpecialEventDate(pnlOtherPersInfo.getSpecialDate());
						String specialDate = pnlOtherPersInfo.getSpecialDate();
						if (specialDate == null || specialDate.length() <= 0) {
							return true;
						} else {
							cms.setSpecialEventDate(DateFormatUtil.getFormattedDate(pnlOtherPersInfo.getSpecialDate()));
							return (true);
						}
					} catch (Exception bex) {
						theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
						return (false);
					}
				}
			});
		} // getIssueDateVerifier
	}

	/**
	 * MP: Home pressed at customer display exits transaction with no message
	 * @return
	 */
	public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}
	/*Added by Yves Agbessi (05-Dec-2017)
		 * Handles the posting of the Sign Off event for Fiscal Solutions Service
		 * START--
		 * */
		boolean goToHomeView = (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?")));
		if(goToHomeView){
			
			ARMFSBridge.postARMSignOffTransaction((CMSEmployee)theOpr);
		}
		
		return goToHomeView;
		
		/*
		 * --END
		 * */
		
	}
	
	public boolean isAllowedForName(int iValue)
	{
		if(iValue == 46 || (iValue>64 && iValue< 91) || (iValue > 96 && iValue <122) )
		{
			return true;
		}
		
		return false;
	}
	
	//Added for Privacy Mgmt Marketing and Master
	public boolean isMarketingMasterValid(){
		boolean validFlag = false;		
			if (pnlMisc.getPrivacyMarketing() == false && pnlMisc.getPrivacyMaster()== false){				
				validFlag = true;
				theAppMgr.showErrorDlg(res.getString("Select Proper Check box for Marketing and Master"));
				return validFlag;
			}	
			else{
				validFlag =  false;	
				return validFlag;
			}
	}	

	public boolean isCustomerModified(){
		boolean isCustModified;
		if(isCustomerBasicInfoModified()){
			isCustModified =  true;
		}else{
			isCustModified =  false;
		}
		return isCustModified;
	}
	public String[] parse(String string) {
		String[] data=new String[132];
		if (string != null) {
			data = string.split(",");
		} else {
			throw new NullPointerException("data is null");
		}
		return data;
	}
	
	
	//Anjana:- Added for getting Signature image from 3-Byte Ascii	
	private void showSignature(CustomerCreditCard cc){
 		//Vivek Mishra : Changes the condition to restrict showing signature varification dialougue only for non varified payments.
 		//if(pay instanceof CreditCard)
		if(cc.isSignatureValidationRequired()==true)
		{	
		//if(!(cc.getSignatureAscii().equals(null)) || !(cc.getSignatureAscii().equals(""))){
		//Vivek Mishra : Changed the condition to avoid fatal exception in case of response status code other then approval(0) from credit card sale. 
		if(cc.getRespStatusCode().equals("0")) {
				if(!(cc.getSignatureAscii()==(null))) {
						if(!(cc.getSignatureAscii().equals(""))){
			/*JFrame editorFrame = new JFrame("Image Demo");
	        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
*/                        Image img = getSignature(cc.getSignatureAscii(), cc);
	        ImageIcon imageIcon = new ImageIcon(img);
	        JLabel jLabel = new JLabel(imageIcon);
	        //jLabel.setIcon(imageIcon);
	        /*editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

	        editorFrame.pack();
	        editorFrame.setLocationRelativeTo(null);
	        editorFrame.setVisible(true);*/
		boolean isSignatureAuth;
		/*isSignatureAuth = theAppMgr.showOptionDlg("Signature Validation"
    	            , "Please press 'Yes' if Signature is valid else press 'No'", res.getString("Yes"), res.getString("No"));
		System.out.println("isSignatureAuth>>>>>>>>>>>>>>>>>>"+isSignatureAuth);
		
*/					isSignatureAuth = showOptionDlg("Signature Validation"
        , "Please press 'Yes' if Signature is valid else press 'No'", res.getString("Yes"), res.getString("No"), jLabel);
        if(!isSignatureAuth){
			//In case of invalid signature setting the signature valid flag as false.
			//cc.setSignatureAscii("");
			cc.setSignatureValid(false);
		 
		}else{
			//In case of valid signature setting the signature valid flag as true.
			cc.setSignatureValid(true);
		}
		cc.setSignatureValidationRequired(true);
		/*editorFrame.setVisible(false);
        editorFrame.dispose();*/
					}//	if(!(cc.getSignatureAscii().equals("")))
				}//if(!(cc.getSignatureAscii()==(null))) {
			}
		}
 	}
	
	
	//Anjana:- Added for getting Signature image from 3-Byte Ascii	
	 	private Image getSignature(String signAscii, CustomerCreditCard cc)
	 	{
	 		byte[] signByteCode = Base64.decodeBase64(signAscii.getBytes());
	 		//Setting byte array to Credit Card
	 		cc.setSignByteCode(signByteCode);
	 		Image img = null;
	 		InputStream in = new ByteArrayInputStream(signByteCode);
			//BufferedImage bImageFromConvert = ImageIO.read(in);
			try {
				img = ImageIO.read(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
	      return img;
	 	}
	//Ends
	 	
	 	//Anjana : Added method for showing Option dialouge box with signature 	
	 	public boolean showOptionDlg(String title, String message, String captionTrue, String captionFalse, JLabel jLabel)
	 	  {
	 	    OptionDlg dlg = new OptionDlg(theAppMgr.getParentFrame(), title, (AppManager) theAppMgr, captionTrue, captionFalse);
	 	    dlg.getContentPane().add(jLabel, BorderLayout.NORTH);
	 	    try
	 	    {
	 	      dlg.txtStatus.setText(message);
	 	    }
	 	    catch (Exception ex) {
	 	      ex.printStackTrace();
	 	    }

	 	    dlg.setVisible(true);
	 	    return dlg.isOK();
	 	  }
	 //Ends	
}
