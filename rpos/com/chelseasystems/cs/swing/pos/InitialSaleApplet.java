/*
 * @copyright (C) 1998-2002 Retek Inc
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 29   | 11-14-2005 | Manpreet  | N/A       | Implementing Reservations                    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 28   | 09-14-2005 | Manpreet  | 508       | Implementing Alterations on PreSale Open     |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 27   | 09-08-2005 | Manpreet  | 903       | Storing Reason code for priceOverride        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 26   | 08-15-2005 | Vikram    | 875       | Enforced brand-specific loyalty cards.       |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 25   | 08-11-2005 | Vikram    | 861       | Initiate loyalty when customer is added to   |
 |      |            |           |           | transaction.                                 |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 24   | 07-18-2005 | Vikram    | 330       |Default focus in Item List to be on first page|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 23   | 07-06-2005 | Megha     | 105       |Loyalty Card button shows under Options menu  |
 |      |            |           |           | rather than part of Add Item builder.        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 22   | 06-27-2005 | Vikram    | 285       |Customer VIP discount message change          |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 21   | 06-20-2005 | Vikram    | 196       |System should not allow to choose Gift Card   |
 |      |            |           |           |Services in return mode.                      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 20   | 06-15-2005 | Vikram    | 216       |Cancel Action should Reset the Input Area     |
 |      |            |           |           |Prompt back to "Scan/Enter Item"              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 19   | 06-14-2005 | Vikram    | 173       |Suspend transaction/Menus: Cancel button (F1) |
 |      |            |           |           |did not work                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 20   | 06-10-2005 | Vikram    | 61        |Added check for VIP discount                  |
 --------------------------------------------------------------------------------------------
 | 19   | 06-07-2005 | Vikram    | 65, 66    |Transfer Associate and Customer between sale /|
 |      |            |           |           |Pre-Sale / Consignment                        |
 --------------------------------------------------------------------------------------------
 | 18   | 06-02-2005 | Sameena   | 57        |  Line Item Discount on sale and return       |
 |      |            | Megha     |           |  Modified  the 'DISCOUNT' object event.      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 17   | 05-31-2005 | Sameena   | 78        | Discount/By Price: Discount not applied      |
 |      |            |           |           | correctly. Applied as "By Amount". Modified  |
 |      |            |           |           | the 'DISCOUNT' object event.                 |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 16   | 05-27-2005 | Sameena   | 79        | Discounts: Should NOT display discount reason|
 |      |            |           |           | code with discount on screen. Modified the   |
 |      |            |           |           | method updateDiscountLabel.                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 15   | 05-27-2005 | Sameena   | 76        | NullPointerException while deleting a return |
 |      |            |           |           | line item.                                   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 14   | 05-25-2005 | Megha     | N/A       | Customer Messages                            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 14   | 05-17-2005 | Vikram    | N/A       | Updated for Escape Key handling              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 13   | 05-12-2005 | Vikram    | N/A       | Reward Discount Specification                |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 12   | 04-30-2005 | Manpreet    | N/A     | Alteration Specifications                    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 11   | 04-18-2005 | Khyati    | N/A	     |1.Redeemable Management -	(GiftCard Issue)    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 10   | 04-17-2005 | Pankaja   | N/A	     |1.Consignment Close -	(convertCsgntoSale) |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 9    | 04-15-2005 | Manpreet    | N/A       |1.PresaleClose - see invokeEmployeeSale()   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 8    | 04-14-2005 | Khyati    | N/A       |1.Consignment Open Specification              |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 7    | 04-13-2005 | Manpreet  | N/A       |1.PreSale Open Specification                  |
 ---------------------------------------------------------------------------------------------
 | 6    | 04-11-2005 | Khyati    | N/A       |1.Return Specification                        |
 ---------------------------------------------------------------------------------------------
 | 5    | 03-15-2005 | Khyati    | N/A       |1.Discount and Pricing specification          |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 02-08-2005 | Anand     | N/A       |1.Modification to add new menu and related    |
 |      |            |           |           |  menu functionality.                         |
 --------------------------------------------------------------------------------------------
 | 3    | 02-04-2005 | Manpreet  | N/A       | Modified  to add Loyality place holders      |
 |      |            |           |           |   -- See lblLoyality, sLoyalityString        |
 |      |            |           |           |   -- setLoyalityTag(CMSCustomer customer)    |
 |      |            |           |           |   -- to be called from CustSaleApplet        |
 |      |            |           |           |       on applyCustomer                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 02-03-2005 | Manpreet  | N/A       | Modified  to add consultant to line item     |
 |      |            |           |           |   -- See setLineConsultant(POSLineItem line) |
 +------+------------+-----------+-----------+----------------------------------------------+
 |      | 05-02-2012 | Deepika   | Bug 8443  | Modified for adding New Dolci POS Button     |               |
 +------+------------+-----------+-----------+----------------------------------------------+
 */

package com.chelseasystems.cs.swing.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.Theme;
import com.chelseasystems.cr.appmgr.mask.ItemCodeMask;
import com.chelseasystems.cr.appmgr.mask.QuantifiedInput;
import com.chelseasystems.cr.appmgr.mask.ItemCodeMask.QuantifiedItemId;
import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.discount.DiscountMgr;
import com.chelseasystems.cr.dm.display.DisplayManager;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.item.MiscItemManager;
import com.chelseasystems.cr.item.MiscItemTemplate;
import com.chelseasystems.cr.item.RelatedItem;
import com.chelseasystems.cr.park.ParkFileServices;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.MiscItem;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.ShippingRequest;
import com.chelseasystems.cr.register.LightPoleDisplay;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.BRERetry;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.GlobalBar;
import com.chelseasystems.cr.swing.ScrollableToolBarPanel;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cr.user.UserAccessRole;
import com.chelseasystems.cr.util.HTML;
import com.chelseasystems.cs.ajbauthorization.AJBRequestResponseMessage;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cs.customer.CMSCustomerMessage;
import com.chelseasystems.cs.customer.CustomerCreditCard;
import com.chelseasystems.cs.customer.SAXCustomerMessageUnMarshaller;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.discount.CMSDiscountMgr;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;
import com.chelseasystems.cs.discount.RewardDiscount;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.item.CMSItemMgr;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.pos.AlterationDetail;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.pos.AlterationLineItemDetail;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSConsignmentLineItem;
import com.chelseasystems.cs.pos.CMSMiscItem;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;
import com.chelseasystems.cs.pos.CMSPresaleLineItemDetail;
import com.chelseasystems.cs.pos.CMSReduction;
import com.chelseasystems.cs.pos.CMSReservationLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItemDetail;
import com.chelseasystems.cs.pos.CMSSpecificItem;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.pos.CMSVoidLineItem;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.scheduling.CMSRole;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterSessionAppModel;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.swing.builder.AssociateUserNamePasswordBuilder;
import com.chelseasystems.cs.swing.builder.CMSItemWrapper;
import com.chelseasystems.cs.swing.customer.CustomerMessageDialog;
import com.chelseasystems.cs.swing.dlg.AlterationIDDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooseFromTableDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cs.swing.dlg.ReservationReasonDlg;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.DetailLineItemPanel;
import com.chelseasystems.cs.swing.panel.LineItemPOSPanel;
import com.chelseasystems.cs.swing.panel.LineItemPOSPanelMultiSelect;
import com.chelseasystems.cs.swing.panel.MarkdownModifier;
import com.chelseasystems.cs.swing.panel.POSHeaderPanel;
import com.chelseasystems.cs.swing.panel.PageNumberGetter;
import com.chelseasystems.cs.tax.TaxUtilities;
import com.chelseasystems.cs.util.AlterationLookUpUtil;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cs.pricing.CMSPromotion;
import com.chelseasystems.cs.pricing.CMSPromotionBasedPriceEngine;
import com.chelseasystems.cs.pricing.ItemThresholdPromotion;
import com.chelseasystems.cs.pricing.ItemThresholdPromotionEngine;
import com.chelseasystems.cs.pricing.MultiunitPromotion;
import com.chelseasystems.cs.pricing.MultiunitPromotionEngine;
import com.chelseasystems.cr.pricing.IActivatedPromotion;
import com.chelseasystems.cr.pricing.IPromotion;
import com.chelseasystems.cr.pricing.IPromotionEngine;

public class InitialSaleApplet extends CMSApplet {

	//changed by shushma for shipping options
	private ArmCurrency armCurrency;
	private String shipdesc;
	private MiscItemTemplate misItemTemplateForFedEx;
	private long privilege;



//changed for Fed-Ex closed

	public static final int SALE_MODE = 0;
	public static final int RETURN_MODE = 1;
	public static final int LAYAWAY_MODE = 2;
	public static final int EMPLOYEE_SALE = 3;
	//mb: added for pre-sale
	public static final int PRE_SALE_OPEN = 4;
	public static final int PRE_SALE_CLOSE = 5;
	//ks: Added for Consignment
	public static final int CONSIGNMENT_OPEN = 6;
	public static final int CONSIGNMENT_CLOSE = 7;
	//Transferred from Europe code base
	public static final int NO_SALE_MODE = 8;
	public static final int RESERVATIONS_OPEN = 9;
	public static final int RESERVATIONS_CLOSE = 10;
	private int qtyToApply = -1;
	private int iAppletMode = SALE_MODE;
	private boolean totalPressed;
	private boolean isGoHomeAllowed = false;
	private boolean solicitOverrideReason;
	private boolean isMarkdownMulti;
	private CMSCompositePOSTransaction theTxn;
	private POSLineItem workInProgressLine;
	private ConfigMgr config;
	private String itemDivision;
	private String itemBuilder;
	private PriceOverrideHelper overRideHelper;
	private GenericChooseFromTableDlg overRideDlg;
	private SuggestiveSellDlg suggestDlg;
	private SelectionResetListener selectionResetListener;
	private String sAction_global;
	private Vector vecMenus;
	private String rewardDiscountBldr;
	private boolean isCustomerAdded = false;
	private ConfigMgr loyaltyConfigMgr;
	private boolean search = false;
	private boolean isBasketItemPresent = true;
	LineItemPOSPanelMultiSelect pnlLineItemMulti;
	LineItemPOSPanel pnlLineItem;
	MarkdownModifier markdownModifier;
	DetailLineItemPanel pnlDetailLineItem;
	RolodexLayout cardLayout;
	JPanel cardPanel;
	POSHeaderPanel pnlHeader;
	JPanel pnlSouth;
	JLabel labDeposit;
	JLabel fldSubTotal;
	JLabel fldDeposit;
	JLabel fldTotalUnits;
	JLabel labDiscount;
	JLabel labLayaway;
	JLabel labMerchandiseCount;
	// For displaying Employee Sale EMployee.
	JLabel lblEMployeeSale;
	JLabel lblLoyality; // Loyality Label
	private String sLoyalityString; // Loyality string to fetch value from Resource
	private AlterationIDDlg alterationIDDlg;
	// Reservation Deposit
	private MiscItem miscDepositItem;
	private POSLineItem lineItmDeposit;
	private double dReservationDepositPercent = 0;
	private boolean bUpdateDepositAmount;
	private ArmCurrency amtRSVODeposit = null;
	public static final int NO_OPEN_RESERVATIONS_CLOSE_SALE = 11;
	public static final int NO_OPEN_RESERVATIONS_CLOSE_RETURN = 12;
	//code added by deepika for NonServiceItem PCR
	MiscItemTemplate miscItemTemplate = new MiscItemTemplate();
	private String exceptionbuilder;
	private String baseItemId; //added by shushma for Shipping Fed-Ex
	//Added by Rachana for apporval of return transaction
	String totalTransactionAmount;
	public CMSEmployee theApprover;
	//protected boolean isPasswordRequired;
	protected boolean isPasswordEntered;
	protected int passwordWrongCount;

	private boolean deleteFlag = false;
	private boolean Refresh = false;
	//Vivek Mishra : Added to fix fatal exception issue during shipping override
	private boolean isShipOpr = false;

	//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
	private int count = 0;
	CMSPresaleLineItem prsLnItm;
	private static ConfigMgr configMgr;
	private String fipay_flag;
	//Vishal Yevale 25 Oct 2017: removed 'Gift Card' option from Services button where gc flag = Y 
	//bug #28674
	private String fipayGiftcardFlag;

	//Initialize the applet
	public void init() {
		try {
			sLoyalityString = res.getString("Loyality Discount");
			lblLoyality = new JLabel(sLoyalityString);
			pnlHeader = new POSHeaderPanel();
			pnlSouth = new JPanel();
			labDeposit = new JLabel();
			fldSubTotal = new JLabel();
			fldDeposit = new JLabel();
			fldTotalUnits = new JLabel();
			labDiscount = new JLabel();
			labLayaway = new JLabel();
			this.lblEMployeeSale = new JLabel();
			// Set the font
			Font txtFont = theAppMgr.getTheme().getTextFieldFont();
			//lblEMployeeSale.setFont(txtFont);
			selectionResetListener = new SelectionResetListener();
			jbInit();
			loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
			config = new ConfigMgr("item.cfg");
			itemBuilder = config.getString("ITEM.BUILDER");
			//Added by deepika
			exceptionbuilder=config.getString("EXCEPTION_ITEM.BUILDER");
			config = new ConfigMgr(System.getProperty("USER_CONFIG"));
			//***********************************************************************************
			//isPasswordRequired = config.getString("IS_PASSWORD_REQUIRED").equalsIgnoreCase("true");
			//************************************************************************************
			//      config = new ConfigMgr("loyalty.cfg");
			//      rewardDiscountBldr = config.getString("REWARDDISCOUNT.BUILDER");
			rewardDiscountBldr = CMSDiscountMgr.getBuilderName("REWARD_DISCOUNT");
			checkForPriceOverrides();
			config = new ConfigMgr("priceoverride.cfg");
			isMarkdownMulti = config.getString("MARKDOWN_MULTI_SELECT").equals("true");
			markdownModifier = isMarkdownMulti ? (MarkdownModifier) pnlLineItemMulti : (MarkdownModifier) pnlLineItem;
			suggestDlg = new SuggestiveSellDlg(theAppMgr.getParentFrame());
			suggestDlg.addKeyListener(new KeyAdapter() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
				public void keyTyped(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						suggestDlg.setVisible(false);
					}
				}
			});
			//      , KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			//          JComponent.WHEN_IN_FOCUSED_WINDOW);
			// if they change the selection, re-init in case they were in the middle of something.   djr
			pnlLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
			pnlDetailLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Start the applet
	public void start() {
		try {
		 	String fileName = "store_custom.cfg";
		 	configMgr = new ConfigMgr(fileName);
			fipay_flag = configMgr.getString("FIPAY_Integration");

			 //Default value of the flag is Y if its not present in credit_auth.cfg
			if (fipay_flag == null) {
				fipay_flag = "Y";
			}

			//Vishal Yevale 25 Oct 2017: removed 'Gift Card' option from Services button where gc flag = Y 
			 //Default value of the flag is N if its not present in store_custom.cfg
			//bug #28674
			fipayGiftcardFlag = configMgr.getString("FIPAY_GIFTCARD_INTEGRATION");
			if (fipayGiftcardFlag == null) {
				fipayGiftcardFlag = "N";
			}
			// end Vishal Yevale
			theAppMgr.unRegisterSingleEditArea();
			miscDepositItem = null;
			amtRSVODeposit = null;
			bUpdateDepositAmount = false;
			lineItmDeposit = null;
			fldDeposit.setText("");
			labDeposit.setText("");
			if (theAppMgr.getStateObject("ARM_RSV_DEPOSIT") != null) {
				amtRSVODeposit = (ArmCurrency) theAppMgr.getStateObject("ARM_RSV_DEPOSIT");
			}
			LightPoleDisplay.getInstance().stopDefaultDisplay();
			totalPressed = false;
			// In the start, the label should not be visible.
			lblEMployeeSale.setVisible(false);
			lblEMployeeSale.setText(" ");
			theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
			theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr, null);
			theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
			//      theAppMgr.removeStateObject("TXN_CUSTOMER"); // Cust passed to CustSaleApplet
			theAppMgr.removeStateObject("SHIPPING_REQUEST"); // in case coming back from add/mod a shipping request
			theAppMgr.removeStateObject("TXN");
			theAppMgr.removeStateObject("SHIPPING_INQUIRY_ONLY");
			//mb: To dertimine the mode of the sale apple
			if (theAppMgr.getStateObject("TXN_MODE") != null) {
				iAppletMode = ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue();
			}
			pnlLineItem.clear();
			vecMenus = new Vector();
			isGoHomeAllowed = false;
			sAction_global = new String();
			theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
			CMSV12Basket cmsV12Basket = (CMSV12Basket) theAppMgr
					.getStateObject("V12BASKET_LOOKUP");
			if (cmsV12Basket != null && theTxn.getCmsV12Basket()==null) {
				objectEvent("V12BASKET", cmsV12Basket);
				enterItem();
				updateLabel();
			}
			// check for customer
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
				try {
					CMSCustomer txnCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
					theAppMgr.removeStateObject("TXN_CUSTOMER");
					if (theTxn != null && txnCust != null) {
						updateMerchandiseCountLabel();
						//Fix 1840 :Deposit total amount is not calculated on the SALES screen.
						setCustomer(txnCust);
						//theTxn.setCustomer(txnCust);
						isCustomerAdded = true;
						// Checking if the customer has any messages to be displayed
						CMSCustomerMessage cmsCustomerMsg = isCustomerMessagePresent(theTxn);
						// Get the message Type
						String msgType = cmsCustomerMsg.getMessageType();
						// See if the message search has been by Id.
						boolean searchById = getSearchById();
						if (cmsCustomerMsg != null) {
							// Populate the DialogBox
							CustomerMessageDialog custMsgDlg = new CustomerMessageDialog(theAppMgr.getParentFrame(), theAppMgr, cmsCustomerMsg.getMessage(), searchById);
							// If the msg Type = Q
							if (msgType.equals("Q")) {
								custMsgDlg.setVisibleForQandA();
								custMsgDlg.setVisible(true);
							}
							// If the msg type = M
							else if (msgType.equals("M")) {
								custMsgDlg.setVisibleForMType();
								custMsgDlg.setVisible(true);
							}
						}
						TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore) theTxn.getStore(), (CMSStore) theTxn.getStore(), theTxn.getProcessDate());
						LightPoleDisplay.getInstance().displayMessage(txnCust.getFirstName(), txnCust.getLastName());
					}
				} catch (Exception e) {
					theAppMgr.removeStateObject("TXN_CUSTOMER");
				}
			}
			if (theTxn != null) {
				theTxn.setPostAndPack(false, new ArmCurrency(0.0));
				if (theTxn.getLineItemsArray().length > 0) {
					updateLabel();
				}
				//VM: Transfer of Associate and Customer between Sale, PreSale and Consignment
				Employee associate = (Employee) theAppMgr.getStateObject("ASSOCIATE");
				Customer customer = (Customer) theAppMgr.getStateObject("CUSTOMER");
				if (theTxn.getConsultant() == null) {
					if (associate != null)
						theTxn.setConsultant(associate);
					else
						theTxn.setConsultant((Employee) theOpr);
				}
				if (theTxn.getCustomer() == null && customer != null) {
					//theTxn.setCustomer(customer);
					//Fix 1840 :Deposit total amount is not calculated on the SALES screen.
					setCustomer((CMSCustomer)customer);
				}
				theAppMgr.removeStateObject("ASSOCIATE");
				theAppMgr.removeStateObject("CUSTOMER");
			}
			if ((theTxn.getCustomer() != null && theTxn.getLoyaltyCard() == null)
					|| (theTxn.getCustomer() != null && theTxn.getLoyaltyCard() != null && !theTxn.getLoyaltyCard().getCustomer().equals(theTxn.getCustomer()))) {
				// Set the default loyalty card
				setCustomerLoyalty();
			}
			initHeaders();
			populateLineItems();
			updateDiscountLabel();
			showButtons();
			enterItem();
			cardLayout.show(cardPanel, "LINE_ITEM");
			if (isCustomerAdded && theTxn.getCustomer() != null) {
				if (((CMSCustomer) theTxn.getCustomer()).getVIPDiscount() > 0) {
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMaximumFractionDigits(1);
					theAppMgr.showErrorDlg(res.getString("Customer has VIP discount: ") + nf.format(((CMSCustomer) theTxn.getCustomer()).getVIPDiscount()));
				}
				theAppMgr.removeStateObject("ARM_ADDED_CUSTOMER");
				isCustomerAdded = false;
			}
			// Addedby - MSB
			// To switch mode to EMPLOYEE_SALE when applet is returning
			// from SelectItemsListApplet after selecting EMPLOYEE_SALE
			if (theAppMgr.getStateObject("TXN_MODE") != null) {
				iAppletMode = ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue();
				if (iAppletMode == EMPLOYEE_SALE) {
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
									invokeEmployeeSale();
								}
							});
						}
					});
				}
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
		ScrollableToolBarPanel obj = (ScrollableToolBarPanel) AppManager.getCurrent().getMainFrame().getAppToolBar();
		Object rewardDiscount = theAppMgr.getStateObject("REWARD_DISCOUNT");
		theAppMgr.removeStateObject("REWARD_DISCOUNT");
		if (rewardDiscount != null && theTxn.getCustomer() != null) {
			objectEvent("DISCOUNT", rewardDiscount);
		}
		if (theAppMgr.getStateObject("MISC_ITEM_TEMPLATE") != null && theTxn.getCustomer() != null) {
			SwingUtilities.invokeLater(new Runnable() {
				MiscItemTemplate miscItemTemplate = (MiscItemTemplate) theAppMgr.getStateObject("MISC_ITEM_TEMPLATE");

				/**
				 * put your documentation comment here
				 */
				public void run() {
					buildMiscItem(miscItemTemplate);
				}
			});
		}
		theAppMgr.removeStateObject("MISC_ITEM_TEMPLATE");
		if (theAppMgr.getStateObject("ARM_DEPOSIT_CUSTOMER") != null) {
			theAppMgr.removeStateObject("ARM_DEPOSIT_CUSTOMER");
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
							buildDepositItem(true);
						}
					});
				}
			});
		}
		if (amtRSVODeposit != null && lineItmDeposit == null) {
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
							buildDepositItem(false);
							if (miscDepositItem != null) {
								calculateDepositAmount(true);
								bUpdateDepositAmount = true;
								updateLabel();
							}
						}
					});
				}
			});
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void invokeEmployeeSale() {
		try {
			theTxn.checkIfValidForEmployeeSale();
			String bldrName = CMSDiscountMgr.getBuilderName("EMPLOYEE");
			theAppMgr.buildObject(this, "DISCOUNT", bldrName, "EMPLOYEE_SALE");
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(ex.getMessage());
		}
	}

	//Stop the applet
	public void stop() {
		if (!totalPressed) {
			try {
				theTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr)
						.printCancelReceipt(theAppMgr);
				if(theTxn.getCmsV12Basket()!=null) {
					CMSV12BasketHelper.setBasketStatus(theAppMgr,
							theTxn.getCmsV12Basket(), CMSV12Basket.open);
					theAppMgr.removeStateObject("V12BASKET_LOOKUP");
				} 
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		suggestDlg.clearSuggestions();
		suggestDlg.setVisible(false);
		//sendItemMessageData(null, null, false, true, false, false, "");
	}

	// callback whan an application tool bar button is pressed
	public void editAreaEvent(String Command, ArmCurrency amount) {
		String breRetry = ENTER_ITEM;
		//       System.out.println("edit area event 1 : "+Command);
		try {
			if (Command.equals("ADJUST_DEPOSIT") && (isReservationCloseMode())) {
				//Fix for 1624: Reservations/Special Orders:  The closed deposit amount
				//can be adjusted without financial accounting.
			    ArmCurrency depositAmt = (ArmCurrency) theAppMgr.getStateObject("ARM_RSV_DEPOSIT");
				if (amount.getDoubleValue() > depositAmt.getDoubleValue()) {
					theAppMgr.showErrorDlg(res.getString("Adjust deposit amount cannot be greater than deposit amount"));
					enterAdjustDepositAmount();
					return;
				}
				lineItmDeposit.setManualUnitPrice(amount);
				pnlLineItem.getModel().fireTableDataChanged();
				bUpdateDepositAmount = false;
				updateLabel();
				showButtons();
				enterItem();
			}
			if (Command.equals("RSVO_DEPOSIT") && isReservationOpenMode()) {
				if (amount.getDoubleValue() < 0) {
					theAppMgr.showErrorDlg(res.getString("Deposit amount can not be less than zero"));
					enterRSVODepositAmount();
					return;
				}
				if (lineItmDeposit != null) {
					pnlLineItem.getTable().getSelectionModel().removeListSelectionListener(selectionResetListener);
					pnlLineItem.deleteLineItem(lineItmDeposit);
				}
				miscDepositItem.setUnitPrice(amount);
				lineItmDeposit = theTxn.addSaleMiscItem(miscDepositItem);
				theTxn.getReservationTransaction().setDepositAmount(amount);
				addLineItem(lineItmDeposit);
				fldDeposit.setText(amount.formattedStringValue());
				bUpdateDepositAmount = false;
				updateLabel();
				showButtons();
				enterItem();
			}
			if (Command.equals("MARKDOWN_AMT")) {
				breRetry = ENTER_MARKDOWN_AMOUNT;
				if (solicitOverrideReason) {
					overRideDlg.setVisible(true);
					if (overRideDlg.isOK()) {
						Object[] reasons = overRideDlg.getSelectedRow().getDisplayRow();
						markdownModifier.modifySelectedMarkdown(amount);
						markdownModifier.setManualMarkdownReason((String) reasons[0]);
					} else {
						//markdownModifier.undoModifySelectedMarkdown();
					}
				} else {
					markdownModifier.modifySelectedMarkdown(amount);
				}
				cardLayout.show(cardPanel, "LINE_ITEM");
				showButtons();
				enterItem();
			} else if (Command.equals("DEFAULT_PRICE")) {
				pnlLineItem.addLineItem(workInProgressLine);
				enterItem();
			} else if (Command.equals("PRICE_OVERRIDE")) {
				if (amount.doubleValue() < 0) {
					theAppMgr.showErrorDlg("Price can't be less than 0");
					return;
				}
				//        pnlLineItem.modifySelectedPrice(amount);
				if (solicitOverrideReason) {
					overRideDlg.setVisible(true);
					if (overRideDlg.isOK()) {
						// MSB - 09/08/05
						// Store ReasonCode not Reason in the database.
						//Object[] reasons = overRideDlg.getSelectedRow().getDisplayRow();
						String reason = (String) overRideDlg.getSelectedRow().getRowKeyData();
						//start for issue 1947 Pole display by neeti
						lineItmDeposit = pnlLineItem.getSelectedLineItem();
						if(lineItmDeposit.getDiscount()==null)
						{
							LightPoleDisplay.getInstance().itemSoldForDiscount(lineItmDeposit,lineItmDeposit.getExtendedRetailAmount().formattedStringValue(),amount.formattedStringValue());
						}
						pnlLineItem.modifySelectedUnitPrice(amount);
						if(lineItmDeposit.getDiscount()!=null){
							LightPoleDisplay.getInstance().itemSoldForDiscount(lineItmDeposit,lineItmDeposit.getExtendedRetailAmount().formattedStringValue(),lineItmDeposit.getTotalAmountDue().formattedStringValue());
						}
						//End for issue 1947 Pole display by neeti
						//            pnlLineItem.getSelectedLineItem().setManualMarkdownReason( (String)
						//                reasons[0]);
						pnlLineItem.getSelectedLineItem().setManualMarkdownReason(reason);
					} else {
						//pnlLineItem.undoModifySelectedPrice();
					}
				} else {
					pnlLineItem.modifySelectedUnitPrice(amount);
				}
				 POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
				showButtons();
				enterItem();
				//Vivek Mishra : Added for sending AJB 150 *POSITEM-CHANGE and REFRESH request after overriding the price
				//sendItemMessageData(pnlLineItem.getSelectedLineItem() , false, false); -- Commented by Anjana , POS-REFRESH should work for price override
				//sendItemMessageData(pnlLineItem.getSelectedLineItem() ,lineItemArray, false, true,false,false,"");
				//End
			} else if (Command.equals("MODIFY_UNIT_PRICE")) {
				pnlLineItem.modifySelectedUnitPrice(amount);
				enterItem();
			} else if (Command.equals("MODIFY_DEPOSIT")) {
				theTxn.getLayawayTransaction().setDesiredTotalAmountDue(amount);
				// there is a rule in place that throws an exception so the following situation does not occur in the Chelsea implementation
				if (theTxn.getLayawayTransaction().getTotalAmountDue().doubleValue() != theTxn.getLayawayTransaction().getDesiredTotalAmountDue().doubleValue()) {
					theAppMgr.showErrorDlg(res.getString("You have set the desired amount due to less than the current minimum.  It will take effect if the desired amount becomes more than the minimum."));
				}
				enterItem();
			} else if (Command.startsWith("MODIFY_TAX")) {
				breRetry = SELECT_LINE_ITEM;
				boolean isRegional = Command.endsWith(((CMSStore) theStore).getRegionalTaxLabel());
				pnlDetailLineItem.modifySelectedLineTax(amount, false);
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
			} else if (Command.equals("POST_AND_PACK_CHARGE")) {
				try {
					theTxn.setPostAndPack(true, amount);
					//CMSValueAddedTaxHelper.applyVAT(theAppMgr, theTxn, (CMSStore) theStore,
					//(CMSStore) theStore, theTxn.getProcessDate(), true);
					TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore) theTxn.getStore(), (CMSStore) theTxn.getStore(), theTxn.getProcessDate());
					pnlLineItem.getTable().repaint();
				} catch (BusinessRuleException bre) {
					theAppMgr.showErrorDlg(bre.getMessage());
				} catch (Exception ex) {
					theAppMgr.showExceptionDlg(ex);
				}
				showButtons();
				enterItem();
			}
		} catch (BusinessRuleException ex) {
			BRERetry.retry(breRetry, this, ex);
		} finally {
			updateLabel();
		}
	}

	// callback whan an application tool bar button is pressed
	public void editAreaEvent(String Command, Double number) {
		//System.out.println("edit area event 2 : "+Command);
		try {
			if (Command.startsWith("MODIFY_TAX_PERCENT")) {
				double price = pnlDetailLineItem.getSelectedLineItem().getLineItem().getNetAmount().doubleValue();
				boolean isRegional = Command.endsWith(((CMSStore) theStore).getRegionalTaxLabel());
				pnlDetailLineItem.modifySelectedLineTax(new ArmCurrency((number.doubleValue() / 1000000.0) * (price * 10000.0)).round(), number.doubleValue(), false);
			}
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		} finally {
			updateLabel();
			theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
			theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
		}
	}

	// callback whan an application tool bar button is pressed
	public void editAreaEvent(String Command, Integer number) {
		//System.out.println("edit area event 3 : "+Command);
		String breRetry = ENTER_ITEM;
		try {
			if (Command.equals("MARKDOWN_PCT")) {
				breRetry = this.ENTER_MARKDOWN_PERCENT;
				markdownModifier.modifySelectedMarkdownPercent(number.intValue());
				if (solicitOverrideReason) {
					overRideDlg.setVisible(true);
					if (overRideDlg.isOK()) {
						Object[] reasons = overRideDlg.getSelectedRow().getDisplayRow();
						markdownModifier.setManualMarkdownReason((String) reasons[0]);
					} else {
						markdownModifier.undoModifySelectedMarkdownPercent();
					}
				}
				cardLayout.show(cardPanel, "LINE_ITEM");
				showButtons();
			} else if (Command.equals("QUANTITY")) {
				pnlLineItem.modifySelectedQuantity(number.intValue());
				if (isLayawayMode()) {
					 //theTxn.resetLayawayDeposit();
					//remove postandpack
				}
				theTxn.setPostAndPack(false, new ArmCurrency(0.0));
				// Issue # 1128
				updateLabel();
				showButtons();
				enterItem();
				 POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
				//Vivek Mishra : Added for sending AJB 150 *POSITEM-CHANGE and REFRESH request after updating quantity
			//	sendItemMessageData(pnlLineItem.getSelectedLineItem() , false, false); --commented by Anjana, POS-REFRESH should work
				//sendItemMessageData(pnlLineItem.getSelectedLineItem() ,lineItemArray, false, true,false,false,"");
				//End
			} else if (Command.startsWith("MODIFY_TAX_PERCENT")) {
				double price = pnlDetailLineItem.getSelectedLineItem().getLineItem().getNetAmount().doubleValue();
				boolean isRegional = Command.endsWith(((CMSStore) theStore).getRegionalTaxLabel());
				pnlDetailLineItem.modifySelectedLineTax(new ArmCurrency(((number.doubleValue() / 10000.0) * price) * 100.0), false);
			}
			enterItem();
		} catch (BusinessRuleException ex) {
			BRERetry.retry(breRetry, this, ex);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		} finally {
			updateLabel();
		}
	}

	/**
	 * @param command
	 * @param input
	 */
	public void editAreaEvent(String command, QuantifiedInput input) {
		try {
			if (command.equals("ITEM")) {
				// MP: Changed for the loyalty card swipe.
				if (input.getInput().toString().equalsIgnoreCase("P")) {
					theAppMgr.unRegisterSingleEditArea();
					theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "LOYALTYCARD", theOpr);
					ConfigMgr config = new ConfigMgr("loyalty.cfg");
					String Builder = config.getString("LOYALTY_ITEM_BUILDER");
					theAppMgr.buildObject("LOYALTY_ITEM", Builder, "");
				} else {
					qtyToApply = input.getQtyToApply();
					theAppMgr.buildObject(this, "ITEM", itemBuilder, ((ItemCodeMask.QuantifiedItemId) input).getItemId());
					//added for Dolci by vivek
					if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
						String itemId = input.getInput().toString();
						//this will check whether item is dolci or not
						boolean flag = isDolciCandy(itemId);
						if(flag){
							HashMap dolciSkuValue =(HashMap)theAppMgr.getGlobalObject("DOLCI_ITEM_PRESENT");
							String key = getKeyFromValue(dolciSkuValue,itemId);
							int index = key.indexOf(".");
							String keyIndex = key.substring(index+1);
							pnlLineItem.modifyDescription(keyIndex);
						}
					}
					//ended for dolci

				}
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	// callback when an edit area event occurs
	public void editAreaEvent(String Command, String sEdit) {
		//System.out.println("edit area event 5: "+Command);
		try {

		//Rachana - added for approval of return transaction
			if(Command.equals("APPROVER")){
				isPasswordEntered = false;
				CMSEmployee theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
				//System.out.println("--- In InitialSaleApplet class : editAreaEvent()-  theOpr  :"+theOpr.getExternalID());

				theApprover = CMSEmployeeHelper.findByExternalId(theAppMgr, sEdit);
				if (theApprover == null){
					theAppMgr.showErrorDlg(res.getString("You have entered wrong employee id."));
					enterUserName();
					return;
				}
				 if (testCanBeOperator()){
						 if( !theApprover.getExternalID().equals(theOpr.getExternalID())) {
							// if (isPasswordRequired && !isPasswordEntered) {
							 if(!isPasswordEntered){
								 enterPassword();
								 return;
							 }
						 }else{
								theAppMgr.showErrorDlg(res.getString("An additional employee's User-ID and password must be entered to approve the return."));
								enterUserName();
								return;
						}
				 }else{
						enterUserName();
						return;
				}
				//this.buildApprover(sEdit,theTxn);
				//initHeaders();
				//theAppMgr.fireButtonEvent("RETURN_ITEM");
				theAppMgr.addStateObject("APPROVER", sEdit);
				return;
			}

			//Rachana - to validate password for approval of return transaction
			if (Command.equals("PASSWORD")) {
				if(!sEdit.equals(null)){
					if(processPasswordEntry((String)sEdit)){
						isPasswordEntered = true;
				      	try {
				      		if(!theApprover.equals(null)){
				      			theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
				      			POSLineItem[] returnItems = theTxn.getReturnLineItemsArray();
				      			if((CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS")!= null){
				      				theTxn.setApprover(theApprover.getExternalID());
				      				  if (returnItems.length > 0) {
				      					  for (int i = 0; i < returnItems.length; i++) {
				      						  returnItems[i].setApprover(theApprover.getExternalID());
				      					  }
				      				  }
				      			}
				      			theAppMgr.fireButtonEvent("TENDER");
				      			return;
				      		}
						} catch (BusinessRuleException e) {
							e.printStackTrace();
						}
					}else{
						if ((passwordWrongCount < 3)|| (sEdit.length() == 0)) {
					          enterPassword();
					    } else {
					          passwordWrongCount = 0;
					          enterUserName();
					    }
					}
				}
				return;
			}
			//Added By shushma for Shipping FED-EX
			if (Command.equals("OPERATOR")) {
				//String s = sEdit.toUpperCase();
				setCmsEmployee(CMSEmployeeHelper.findByShortName(theAppMgr, sEdit));
				if(cmsEmployee==null)
				{
					theAppMgr.showErrorDlg(res.getString("You have entered wrong Manager's id."));
					enterItem();
					return;
				}
				else{
				//Vivek Mishra : Added to fix fatal exception issue during shipping override
				isShipOpr = true;
				enterPassword();
				return;
				}
			}
			else if(Command.equals("PASSWORDSHIP")){
				String password=cmsEmployee.getPassword();
				if (password.equalsIgnoreCase(sEdit))
				enterShippingOverride(cmsEmployee);
				else
				{
					theAppMgr.showErrorDlg(res.getString("You have entered wrong password."));
					enterItem();
				}
				return;
			}
			//Fed-Ex closed
			//MSB - Alterations
			else if (Command.equals("ALTERATION_ID")) {
				if (sEdit == null || sEdit.trim().length() < 1) {
					theAppMgr.showErrorDlg(res.getString("Enter Alteration ID to proceed"));
					enterAlterationID();
					return;
				}
				if (isDuplicateAlterationID(sEdit)) {
					theAppMgr.showErrorDlg(res.getString("Entered Alteration ID already present"));
					enterAlterationID();
					return;
				}
				enterItem();
				theAppMgr.addStateObject("ARM_ALTERATION_ID", sEdit);
				theAppMgr.addStateObject("POS_LINE_ITEM", pnlLineItem.getSelectedLineItem());
				theAppMgr.fireButtonEvent("ALTERATIONS");
			}
			if (Command.equals("MANAGER")) {
				CMSEmployee emp = CMSEmployeeHelper.findById(theAppMgr, sEdit);
				if (emp == null) {
					theAppMgr.showErrorDlg(res.getString("Cannot find employee."));
					enterItem();
					return;
				}
				if (false) { //!emp.isManager()) {
					theAppMgr.showErrorDlg(res.getString("The Operator is not a manager. Returns must be performed by a Manager."));
					enterItem();
					return;
				}
				theTxn.setTheOperator(emp);
				initHeaders();
				theAppMgr.fireButtonEvent("RETURN_ITEM");
				return;
			} else if (Command.equals("SUSPEND_TXN_COMMENT")) {
				totalPressed = true; // don't print the cancelled receipt, fake out like total was pressed
				PaymentTransactionAppModel appModel = theTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
				appModel.setSuspendComment(sEdit);
				appModel.printSuspendedReceipt(theAppMgr);
				ParkFileServices ParkServices = new ParkFileServices();
				try {
					ParkServices.suspend(theTxn, sEdit);
					isGoHomeAllowed = true;
					theAppMgr.fireButtonEvent("CANCEL_TXN");
				} catch (Exception e) {
					theAppMgr.showExceptionDlg(e);
				}
			} else if (Command.equals("ITEM_DIV")) {
				itemDivision = sEdit;
				enterItemNameSearch();
			} else if (Command.equals("ITEM_NAME")) { //ITEM LOOKUP
				/*Item[] itemSearch = ItemHelper.getItemByDivAndDesc(theAppMgr, itemDivision, sEdit);
				 if (itemSearch == null) {
				 theAppMgr.showErrorDlg("No items were found with that brand name in that division");
				 enterItem();
				 showButtons();
				 return;
				 }
				 ItemLookupDlg dlg = new ItemLookupDlg(theAppMgr.getParentFrame(),
				 theAppMgr, itemSearch);
				 dlg.setVisible(true);
				 Item itemLookup = (Item)theAppMgr.getStateObject("ITEM_SELECT");
				 theAppMgr.removeStateObject("ITEM_SELECT");
				 //if canceled
				 if (itemLookup == null) {
				 enterItem();
				 showButtons();
				 return;
				 }
				 enterItem(itemLookup.getId() + "80000");
				 showButtons();*/
			} else if (Command.equals("CONSULTANT_LINE_ID")) {
				CMSEmployee emp = CMSEmployeeHelper.findByShortName(theAppMgr, sEdit);
				if (emp == null) {
					theAppMgr.showErrorDlg(res.getString("Cannot find employee."));
					theAppMgr.setSingleEditArea(res.getString("Enter the associate ID"), "CONSULTANT_LINE_ID");
					return;
				}
				if (false) { //!emp.isConsultant()) {
					theAppMgr.showErrorDlg(res.getString("The employee is not a consultant"));
					theAppMgr.setSingleEditArea(res.getString("Enter the associate ID"), "CONSULTANT_LINE_ID");
					return;
				}
				pnlDetailLineItem.modifySelectedLineConsultant(emp);
				//pnlDetailLineItem.fireTableDataChanged();
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
				return;
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}
	//added by Shushma for Shipping Fed-Ex
	public void enterShippingOverride(CMSEmployee cmEmployee) {
		// TODO Auto-generated method stub
		setCmsEmployee(cmEmployee);
		setPrivilege(this.cmsEmployee.doGetPrivileges());
		miscItemTemplate=getMisItemTemplateForFedEx();
		if(privilege==11||privilege==1||privilege==3||privilege==9){
			String str[]={miscItemTemplate.getMiscItemDescription()};
			miscItemTemplate.setDescription(str);
			miscItemTemplate.setCanOverrideAmount(true);
			theAppMgr.buildObject(this, "MISC_ITEM", itemBuilder, miscItemTemplate);
			}
		else{
			theAppMgr.showErrorDlg(res.getString("You are not authorized for Shipping cost override."));
			enterItem();
			return;
			}
	}
//Fed-Exclose

	// return to the builders the txn so they may do intermediate validations
	public BusinessObject getDestinationObjectForCurrentBuilder() {
		return (theTxn);
	}

	//
	public String getScreenName() {
		return (res.getString("Add Items"));
	}

	//
	public String getVersion() {
		return ("$Revision: 1.147.2.12.4.56 $");
	}

	//
	public int getMode() {
		return (iAppletMode);
	}

	//mb: to set line discount on presale items
	private void setPresaleLineDiscounts(CMSDiscount dis) {
		try {
			Discount priceLineDiscount = null;
			CMSPresaleLineItem cmsPresaleLineItem = (CMSPresaleLineItem) pnlLineItem.getSelectedLineItem();
			if (theTxn.isEmployeeSale && dis instanceof CMSEmployeeDiscount) {
				if (cmsPresaleLineItem != null) {
					Discount discount[] = cmsPresaleLineItem.getDiscountsArray();
					if (discount != null) {
						for (int j = 0; j < discount.length; j++) {
							if (discount[j] instanceof CMSEmployeeDiscount) {
								cmsPresaleLineItem.removeDiscount(discount[j]);
							}
							break;
						}
					}
					cmsPresaleLineItem.addDiscount((CMSEmployeeDiscount) dis);
				}
			} else {
				if (dis.getType().equals("BY_PRICE_DISCOUNT")) {
					double disPercent = dis.getAmount().doubleValue() / cmsPresaleLineItem.getItemRetailPrice().doubleValue();
					dis.doSetPercent(disPercent);
					dis.doSetAmount(new ArmCurrency(0.0d));
					dis.setIsDiscountPercent(true);
					if (cmsPresaleLineItem.isPriceDiscountAdded && cmsPresaleLineItem.getPriceDiscount() != null) {
						priceLineDiscount = cmsPresaleLineItem.getPriceDiscount();
						cmsPresaleLineItem.removeDiscount(priceLineDiscount);
						theTxn.removeDiscount(priceLineDiscount);
						cmsPresaleLineItem.removeAddPriceDiscount();
					}
					cmsPresaleLineItem.setAddPriceDiscount(dis);
				}
				cmsPresaleLineItem.addDiscount((CMSDiscount) dis);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ks: to set line discount on consignmentline items
	private void setConsginmentLineDiscount(CMSDiscount dis) {
		try {
			Discount priceLineDiscount = null;
			CMSConsignmentLineItem consignmentLineItem = (CMSConsignmentLineItem) pnlLineItem.getSelectedLineItem();
			if (theTxn.isEmployeeSale && dis instanceof CMSEmployeeDiscount) {
				if (consignmentLineItem != null) {
					Discount discount[] = consignmentLineItem.getDiscountsArray();
					if (discount != null) {
						for (int j = 0; j < discount.length; j++) {
							if (discount[j] instanceof CMSEmployeeDiscount) {
								consignmentLineItem.removeDiscount(discount[j]);
							}
							break;
						}
					}
					consignmentLineItem.addDiscount((CMSEmployeeDiscount) dis);
				}
			} else {
				if (dis.getType().equals("BY_PRICE_DISCOUNT")) {
					double disPercent = dis.getAmount().doubleValue() / consignmentLineItem.getItemRetailPrice().doubleValue();
					dis.doSetPercent(disPercent);
					dis.doSetAmount(new ArmCurrency(0.0d));
					dis.setIsDiscountPercent(true);
					if (consignmentLineItem.isPriceDiscountAdded && consignmentLineItem.getPriceDiscount() != null) {
						priceLineDiscount = consignmentLineItem.getPriceDiscount();
						consignmentLineItem.removeDiscount(priceLineDiscount);
						theTxn.removeDiscount(priceLineDiscount);
						consignmentLineItem.removeAddPriceDiscount();
					}
					consignmentLineItem.setAddPriceDiscount(dis);
				}
				consignmentLineItem.addDiscount((CMSDiscount) dis);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * object event for discounts
	 */
	public void objectEvent(String Command, Object obj) {
		//  System.out.println("objecte event " + Command);
		/*
		 * I commented these to speed Item-lookup up.  These gui calls were costing
		 * more than a second.  We need to determine if there is a better way to
		 * ensure the gui is reset here. - CG 6/9/2002
		 showButtons();
		 enterItem();
		 */
		//#1217
		if (Command.equals("CREDIT_CARD")) {
			try {
				if (obj != null) {
					CustomerCreditCard creditCard = (CustomerCreditCard) obj;

					theTxn.getPresaleTransaction().setCardZipcode(creditCard.getBillingZipCode());
					theTxn.getPresaleTransaction().setCardExpirationDate(creditCard.getExpDate());
					theTxn.getPresaleTransaction().setCardType(creditCard.getCreditCardType());
					//vivek added for ISD
					//if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
					theTxn.getPresaleTransaction().setCreditCardNumber(creditCard.getCreditCardNumber());
					//System.out.println("encrypted Data ---- " + creditCard.getEncryptedCCData());
					//theTxn.getPresaleTransaction().setEncryptedCCData(creditCard.getEncryptedCCData());
					//theTxn.getPresaleTransaction().setUsedKeyId((creditCard.getKey_id()));
					//theTxn.getPresaleTransaction().set
					//}
				//else{
					//EncryptionUtils util = new EncryptionUtils();
					//String ccNum = creditCard.getCreditCardNumber();
					//String enCCNum = util.encrypt(ccNum);
					//theTxn.getPresaleTransaction().setCreditCardNumber(enCCNum);
				//}
					theTxn.getPresaleTransaction().setIsCreditCardSaved(true);
					theAppMgr.removeStateObject("ADD_CREDITCARD");
					theAppMgr.fireButtonEvent("TENDER");
				}
				if (obj == null) {
					String Builder = CMSPaymentMgr.getPaymentBuilder("CUSTOMER_CREDIT_CARD");
					theAppMgr.buildObject("CREDIT_CARD", Builder, "");
					return;
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
		} else if (Command.equals("V12BASKET")) { 
				try {
			CMSV12Basket cmsV12Basket = (CMSV12Basket) obj;
			ArrayList<String> items = cmsV12Basket.getItemList();
			for (String item : items) {
				ItemCodeMask itemCodeMask = new ItemCodeMask();
				ItemCodeMask.QuantifiedItemId in = itemCodeMask.new QuantifiedItemId(
						item);
				theAppMgr.buildObject(this, "V12BASKET", itemBuilder,
						((ItemCodeMask.QuantifiedItemId) in).getItemId());
				if (!isBasketItemPresent) {
					break;
				}
			}
			if (!isBasketItemPresent) {
				isBasketItemPresent = true;
						do {
							deleteItem();
						} while (pnlLineItem.isRowSelected());
						CMSV12BasketHelper.setBasketStatus(theAppMgr,
								cmsV12Basket, CMSV12Basket.open);
				return;
			}
			String customerId = cmsV12Basket.getCustomerId();
			if (customerId != null) {
				try {
					CMSCustomer cmsCustomer = CMSCustomerHelper.findById(
							theAppMgr, customerId);
					if (cmsCustomer != null)
						theTxn.setCustomer(cmsCustomer);
						theTxn.setCmsV12Basket(cmsV12Basket);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			return ;
				} catch (Exception e) {
					e.printStackTrace();
		}
			}
		if (Command.equals("LOYALTY_ITEM")) {
			// MP (105) For Loyalty Card
			Loyalty loyalty = new Loyalty();
			if (obj != null) {
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
								enterItem();
							}
						});
					}
				});
				theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
				loyalty = (Loyalty) obj;
				try {
					setLoyalty(loyalty);
				} catch (BusinessRuleException ex2) {
					theAppMgr.showErrorDlg(ex2.getMessage());
				}
			}
			try {
				//theTxn.setCustomer(loyalty.getCustomer());
				//Fix 1840 :Deposit total amount is not calculated on the SALES screen.
				setCustomer((CMSCustomer)loyalty.getCustomer());
				theTxn.update();
				} catch (BusinessRuleException ex1) {
					ex1.printStackTrace();
				}
				// Display Customer Specific messages.
				// Checking if the customer has any messages to be displayed
				CMSCustomerMessage cmsCustomerMsg = isCustomerMessagePresent(theTxn);
				if (cmsCustomerMsg != null) {
					// Get the message Type
					String msgType = cmsCustomerMsg.getMessageType();
					// See if the message search has been by Id.
					boolean searchById = getSearchById();
					// Populate the DialogBox
					CustomerMessageDialog custMsgDlg = new CustomerMessageDialog(theAppMgr.getParentFrame(), theAppMgr, cmsCustomerMsg.getMessage(), searchById);
					// If the msg Type = Q
					if (msgType.equals("Q")) {
						custMsgDlg.setVisibleForQandA();
						custMsgDlg.setVisible(true);
					}
					// If the msg type = M
					else if (msgType.equals("M")) {
						custMsgDlg.setVisibleForMType();
						custMsgDlg.setVisible(true);
					}
					CMSCustomer cmsCust = (CMSCustomer) theTxn.getCustomer();
			}
			pnlHeader.updateAmtToReward();
			enterItem();
		}
		if (Command.equals("SALE_TAX")) {
			try {
				theTxn.setTaxExemptId((String) obj);
				try {
					// Clear manual tax only in case of setting a tax exempt and not in case of
					// remove
					if ((String) obj != null) {
						theTxn.clearManualTax();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				updateLabel();
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
		}
		if (Command.equals("DISCOUNT")) {
			if (obj == null) {
				//        selectDiscount();
				enterItem();
				this.showButtons();
			} else {
				POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
				POSLineItem lineItem = pnlLineItem.getSelectedLineItem();
				CMSDiscount dis = (CMSDiscount) obj;
				// Remove the Selected line item state, if applicable
				theAppMgr.removeStateObject("SELECTED_LINE_ITEM");
				if (dis instanceof RewardDiscount) {
					CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
					if (theTxn.getCustomer() == null && (theTxn.getLineItemsArray().length > 0 || theTxn.getPresaleLineItemsArray().length > 0)) {
						theAppMgr.addStateObject("REWARD_DISCOUNT", dis);
						theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
						theAppMgr.fireButtonEvent("ADD_CUSTOMER");
						return;
					}
				}
				Integer disSeqNum = (Integer) theAppMgr.getStateObject("ARM_DISCOUNT_SEQNUM");
				if (disSeqNum == null) {
					disSeqNum = new Integer(1);
					if (theTxn.getDiscountsArray() != null && theTxn.getDiscountsArray().length > 0) {
						disSeqNum = new Integer(theTxn.getDiscountsArray().length + 1);
					}
					theAppMgr.addStateObject("ARM_DISCOUNT_SEQNUM", disSeqNum);
				} else {
					disSeqNum = new Integer(disSeqNum.intValue() + 1);
					theAppMgr.addStateObject("ARM_DISCOUNT_SEQNUM", disSeqNum);
				}
				dis.setSequenceNumber(disSeqNum.intValue());
				try {
					//if line item or subtotal discount add it to saleline item.
					Discount priceLineDiscount = null;
					if (dis.isSubTotalDiscount) {
						addSubtotalDiscount(dis);

					} else if (dis.isLineItemDiscount && !dis.isMultiDiscount) {
						//if (pnlLineItem.getSelectedLineItem() instanceof
						//         CMSSaleLineItem) {
						//  CMSSaleLineItem cmsSaleLineItem = (CMSSaleLineItem) pnlLineItem.
						//      getSelectedLineItem();

						if (lineItem != null) {
							if (!dis.isDiscountPercent()) {
								if (!validatePriceDiscount(dis, lineItem))
									return;
							}
							//}
							addLineItemDiscount(dis, lineItem);

						}
						// # 57 Line Item Discount on sale and return (else)
					} else if (dis.isMultiDiscount) {
						POSLineItem[] lineItems = pnlLineItemMulti.getSelectedLineItems();
						if (!dis.isDiscountPercent()) {
							for (int i = 0; i < lineItems.length; i++) {
								if (!validatePriceDiscount(dis, lineItems[i]))
									return;
							}
						}

						dis.setIsLineItemDiscount(true);
						for (int i = 0; i < lineItems.length; i++) {
							addLineItemDiscount(dis, lineItems[i]);
							//First clear the screen before applying discount and then show all items with the applied discount
							//sendItemMessageData(POSLineItem line,POSLineItem[] lineItemArray , boolean deleteFlag, boolean Refresh, boolean idleMessage) {
						//sendItemMessageData(lineItems[i], lineItems, true, false, false,false);
							//refresh
							//sendItemMessageData(lineItems[i], lineItemArray, false, true, false,false);
						}
					}
					if (!(dis instanceof CMSEmployeeDiscount)) {
						if (!dis.isLineItemDiscount && !dis.isSubTotalDiscount && !dis.isMultiDiscount)
							theTxn.addSettlementDiscount(dis);
						else{
							
							theTxn.addDiscount(dis);
						}
					} else {
						if (!dis.isLineItemDiscount && !dis.isSubTotalDiscount && !dis.isMultiDiscount) {
							addTransactionDiscount(dis);
						}
						theTxn.addDiscount((CMSEmployeeDiscount) dis);
					}
					if (isLayawayMode()) {
						initLayawayButtons();
					} else if (isPreSaleOpen()) {
						initPreSaleOpenButtons();
					} else if (isConsignmentIn()) {
						this.initConsignmentInButtons();
					} else if (isReturnMode()) {
						initReturnButtons();
					} else if (isReservationOpenMode()) {
						initReservationsOpenButtons();
					} else {
						initSaleButtons();
					}
					updateDiscountLabel();
					pnlLineItem.repaint();
					updateLabel();
				} catch (BusinessRuleException ex) {
					showButtons();
					theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
				}
				enterItem();
			}
		
} else if (Command.equals("ITEM"))
		{
			if (obj == null) {
				this.enterItem();
				qtyToApply = -1;
				isBasketItemPresent = false;
			} else {
				POSLineItem lineItem = null;
				CMSItemWrapper itemWrapper = (CMSItemWrapper)obj;
				CMSItem item = itemWrapper.getItem();
				if (item == null) {
					this.enterItem();
					qtyToApply = -1;
					isBasketItemPresent = false;
				} else {
					try {
						//6th feb,17 :Poonam added to show dialog for multiple promotions on an item
						CMSPromotion promotions = null;
						ItemThresholdPromotionEngine itemThresholdPromotionEngine = null;
						MultiunitPromotionEngine multiunitPromotionEngine = null;
						ItemThresholdPromotion itemThresholdPromotion = null;
						MultiunitPromotion multiunitPromotion = null;
						Iterator<String> promotionIDs=item.getPromotionIds();
						ArrayList<String> promIdList=new ArrayList<String>();
						String promDetails = null;
						if(promotionIDs!=null){
						//this list will contain all the promotion id for an item
						while(promotionIDs.hasNext()) {
							promIdList.add(promotionIDs.next());
							}
						}
						int count=0;
						//if an item have multiple promotions applied
						if(promIdList.size() > 1){
							String multiplePromMsg="Item is on multiple promotions.";
							for (String id : promIdList) {
								
								CMSPromotionBasedPriceEngine promotion=new CMSPromotionBasedPriceEngine(null);
								itemThresholdPromotionEngine = new ItemThresholdPromotionEngine();
								multiunitPromotionEngine = new MultiunitPromotionEngine();
								promotions =  (CMSPromotion) promotion.getPromotionById(id);
								if(promotions!=null){
									count++;
									//Item threshold promotions
									//30 OCT 2017 :Mahesh Nandure added method of reduction in the prompt of multiple promotion
									if((promotions.getPromotionEngine().getClass()).equals(itemThresholdPromotionEngine.getClass())){
										itemThresholdPromotion=(ItemThresholdPromotion) promotions;
										if(promotions.isReductionByPercentageOff()){
											promDetails="Promotion ID: "+promotions.getId() +",\n"
													+"Description: "+promotions.getDescription() +",\n"
													+"Method of Reduction :"+promotions.doGetMethodOfReduction()+",\n"
													+"Reduction Percentage: "+promotions.getReductionPercent() +",\n"
													+"Trigger Quantity: "+itemThresholdPromotion.getTriggerQuantity() +",\n"
													+"Trigger Amount: "+itemThresholdPromotion.getTriggerAmount();
											}
										else{
											promDetails="Promotion ID: "+promotions.getId() +",\n"
												+"Description: "+promotions.getDescription() +",\n"
												+"Method of Reduction :"+promotions.doGetMethodOfReduction()+",\n"
												+"Reduction Amount: "+promotions.getReductionAmount() +",\n"
												+"Trigger Quantity: "+itemThresholdPromotion.getTriggerQuantity() +",\n"
												+"Trigger Amount: "+itemThresholdPromotion.getTriggerAmount();
											}
										}
									//Multiunit promotions
									else if((promotions.getPromotionEngine().getClass()).equals(multiunitPromotionEngine.getClass())){
										multiunitPromotion=(MultiunitPromotion) promotions;
										if(promotions.isReductionByPercentageOff()){
											promDetails="Promotion ID: "+promotions.getId() +",\n"
													+"Description: "+promotions.getDescription() +",\n"
													+"Method of Reduction :"+promotions.doGetMethodOfReduction()+",\n"
													+"Reduction Percentage: "+promotions.getReductionPercent() +",\n"
													+"Quantity Break: "+multiunitPromotion.getQuantityBreak();
											}
										else{
											promDetails="Promotion ID: "+promotions.getId() +",\n"
												+"Description: "+promotions.getDescription() +",\n"
												+"Method of Reduction :"+promotions.doGetMethodOfReduction()+",\n"
												+"Reduction Amount: "+promotions.getReductionAmount() +",\n"
												+"Quantity Break: "+multiunitPromotion.getQuantityBreak();
											}
									}
									multiplePromMsg=multiplePromMsg+"\n\n"+"Promotion "+count+":\n"+promDetails;
								}
								
							}
							JTextArea msgArea = new JTextArea();
							msgArea.addKeyListener(new KeyAdapter() {
							  public void keyPressed(KeyEvent e) {
							    if (e.getKeyCode()==KeyEvent.VK_ENTER){
							       SwingUtilities.getWindowAncestor(e.getComponent()).dispose();
							    }
							  }
							});
							String[] options = {"OK"};
							msgArea.setText(multiplePromMsg);
							msgArea.setEditable(false);
							msgArea.setFont(new Font("Serif", Font.BOLD, 14));
							msgArea.setWrapStyleWord(true);
							msgArea.setBorder(null);
							JScrollPane pane = new JScrollPane(msgArea);
							msgArea.setBackground(pane.getBackground());
							pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
							//25 OCT 2017 :Mahesh Nandure added check to show only validated promotions
							if(multiplePromMsg != null && promDetails!=null){
								JOptionPane.showOptionDialog(
								    theAppMgr.getParentFrame(), pane, null, 
								    JOptionPane.OK_OPTION,
								    JOptionPane.INFORMATION_MESSAGE,
								    null,
								    options,0);
							}
							//theAppMgr.showErrorDlg(multiplePromMsg);
						}
						//Poonam:Ends here
						// remove postandpack
						theTxn.setPostAndPack(false, new ArmCurrency(0.0));
						// create line item
						switch (iAppletMode) {
							case RETURN_MODE:
								lineItem = theTxn.addReturnItem(item);
								if (theAppMgr.getStateObject("RETURN_COMMENTS") != null) {
									((ReturnLineItem) lineItem).setComments(theAppMgr.getStateObject("RETURN_COMMENTS").toString());
								}
								if (theAppMgr.getStateObject("RETURN_REASON") != null) {
									((ReturnLineItem) lineItem).setReasonId(theAppMgr.getStateObject("RETURN_REASON").toString());
								}
								if (theAppMgr.getStateObject("ORIG_CONSULT") != null) {
									// Add the original selected consultant to the return item
									((ReturnLineItem) lineItem).setAdditionalConsultant((Employee) theAppMgr.getStateObject("ORIG_CONSULT"));
								}
								break;
							case LAYAWAY_MODE:
								lineItem = theTxn.addLayawayItem(item);
								break;
							// mb: added for pre-sale
							case PRE_SALE_OPEN:
								lineItem = theTxn.addPresaleItem(item);
								break;
							// ks: added for consignment
							case CONSIGNMENT_OPEN:
								// System.out.println("INITIALSALE: " + item.getRetailPrice());
								lineItem = theTxn.addConsignmentItem(item);
								break;
							case RESERVATIONS_OPEN:
								lineItem = theTxn.addReservationItem(item);
								break;
							default:
								lineItem = theTxn.addSaleItem(item);
						}
						// Employee Discount for Armani
						if (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") != null
								&& (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") instanceof CMSEmployeeDiscount)) {
							lineItem.addDiscount((CMSEmployeeDiscount) theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT"));
						}
						// apply quantity
						if (qtyToApply > 1) {
							lineItem.setQuantity(new Integer(qtyToApply));
						}
						lineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());

						//added by shushma for promotion code
						//Uncommented the code by Anjana as Riccardo need the promotion changes for US to save the promotion in arm_stg_txn_dtl
						Iterator<String> promotionCode=item.getPromotionIds();
						if(promotionCode!=null){
						while(promotionCode.hasNext()){
							String id = promotionCode.next();
							//Modified by deepika for setting the promotionId
							CMSPromotionBasedPriceEngine promotion=new CMSPromotionBasedPriceEngine(null);
							IPromotion prom=promotion.getPromotionById(id);
							try {
								//25 OCT 2017 :Mahesh Nandure added check to show dialog for multiple promotions on an item
								if(prom!=null)
								lineItem.setPromotionCode(prom.getId());

							} catch (Exception e) {
								// TODO Auto-generated catch block
								  theAppMgr.showErrorDlg(res.getString("Error in loading the promotions for item"));

							}

						}
						}

						else{
							lineItem.setPromotionCode(null);
						}	//promotion code close
						addLineItem(lineItem);
						// suggestive sell logic
						if (lineItem instanceof SaleLineItem) {
							final Vector suggestions = ((SaleLineItem) lineItem).getSuggestedRelatedItems();
							if (suggestions != null && suggestions.size() > 0) {
								Thread suggestion = new Thread(new Runnable() {

									/**
									 *
									 */
									public void run() {
										suggestDlg.setSuggestions(theAppMgr,
												(RelatedItem[]) suggestions.toArray(new RelatedItem[suggestions.size()]));
										suggestDlg.setVisible(true);
									}
								});
								suggestion.start();
							}
						}
					} catch (BusinessRuleException bx) {
						theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
						try {
							if (lineItem != null)
								lineItem.delete();
						} catch (BusinessRuleException bre) {
							theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
						}
					} finally {
						qtyToApply = -1;
					}
				}
			}
			pnlLineItem.repaint();
			showButtons();
			//            if(isLayawayMode())
			//            {
			//                initLayawayButtons();
			//            }
			//            else
			//            {
			//                initSaleButtons();
			//            }
			enterItem();
			updateLabel();
		} else if (Command.equals("SPECIFIC")) {
			specific_item: if (obj != null) {
				CMSItemWrapper itemWrapper = (CMSItemWrapper)obj;
				CMSSpecificItem item = itemWrapper.getSpecificItem();
				if (item != null) {
					try {
						POSLineItem lineItem = null;
						switch (iAppletMode) {
							case LAYAWAY_MODE:
								if (item.getItem().isRedeemable()) {
									theAppMgr.showErrorDlg(res.getString("A redeemable can not be added to a transaction while in layaway iAppletMode."));
									break specific_item;
								} else { // Specific items other than redeemables, should be added to layaways RR
									lineItem = theTxn.addLayawaySpecificItem(item);
									break;
								}
							case RETURN_MODE: {
								lineItem = theTxn.addReturnSpecificItem(item);
								break;
							}
							case PRE_SALE_OPEN: {
								lineItem = theTxn.addPresaleSpecificItem(item);
								break;
							}
							case RESERVATIONS_OPEN:
								lineItem = theTxn.addReservationSpecificItem(item);
								break;
							case CONSIGNMENT_OPEN: {
								lineItem = theTxn.addConsignmentSpecificItem(item);
								break;
							}
							default:
								lineItem = theTxn.addSaleSpecificItem(item);
						}
						// Fix for issue #1875 Associate Sales Report - Counting gift card sales(US)
						String strMiscItemId = MiscItemManager.getInstance().getMiscItemsArray(item.getItem().getId())[0].getKey();
						lineItem.doSetMiscItemId(strMiscItemId);
						lineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());
						//vishal yevale 10 nov 2016 to print reloaded gc balance
						if(item.getGiftCardBalance()!=null){
							lineItem.setGiftCardBalance(item.getGiftCardBalance());
						} // end vishal 10 nov 2016
						addLineItem(lineItem);
					} catch (BusinessRuleException bx) {
						theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
					}
					pnlLineItem.repaint();
					updateLabel();
				}
			}
			enterItem();
		} else if (Command.equals("MISC_ITEM")) {
			if (obj == null)
				return;
			if (obj != null) {

				CMSItemWrapper itemWrapper = (CMSItemWrapper)obj;
				CMSMiscItem item = itemWrapper.getMiscItem();
				if(item == null){
					return;
				}

				try {
					if (theTxn != null) {
						theTxn.setPostAndPack(false, new ArmCurrency(0.0));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					POSLineItem lineItem = null;
					String depositType = (String) theAppMgr.getStateObject("DEPOSIT_TYPE");
					if (((CMSItem) item.getItem()).isDeposit() && depositType != null && depositType.equals("CLOSE_DEPOSIT")) {
						lineItem = theTxn.addReturnMiscItem(item);
						if (iAppletMode == this.NO_OPEN_RESERVATIONS_CLOSE_SALE || iAppletMode == this.NO_OPEN_RESERVATIONS_CLOSE_RETURN) {
							lineItmDeposit = lineItem;
							bUpdateDepositAmount = lineItmDeposit.isManualUnitPrice();
						}
					} else {
						switch (iAppletMode) {
							case LAYAWAY_MODE:
								lineItem = theTxn.addLayawayMiscItem(item);
								break;
							case RETURN_MODE:
								lineItem = theTxn.addReturnMiscItem(item);
								if (theAppMgr.getStateObject("RETURN_COMMENTS") != null) {
									((ReturnLineItem) lineItem).setComments(theAppMgr.getStateObject("RETURN_COMMENTS").toString());
								}
								if (theAppMgr.getStateObject("RETURN_REASON") != null) {
									((ReturnLineItem) lineItem).setReasonId(theAppMgr.getStateObject("RETURN_REASON").toString());
								}
								if (theAppMgr.getStateObject("ORIG_CONSULT") != null) {
									// Add the original selected consultant to the return item
									((ReturnLineItem) lineItem).setAdditionalConsultant((Employee) theAppMgr.getStateObject("ORIG_CONSULT"));
								}
								break;
							case PRE_SALE_OPEN:
								lineItem = theTxn.addPresaleMiscItem(item);
								break;
							case RESERVATIONS_OPEN:
								// Def : 1603 : Reservatoion of Service Item: if the item is not deposit it shld be added as reservation service item or sale item
								if (!((CMSItem) item.getItem()).isDeposit()) {
									lineItem = theTxn.addReservationMiscItem(item);
								} else {
									lineItem = theTxn.addSaleMiscItem(item);
								}
								break;
							case CONSIGNMENT_OPEN:
								if (LineItemPOSUtil.isNotOnFileItem(item.getItem().getId())) { // if Not-on-File Item
									lineItem = theTxn.addConsignmentMiscItem(item);
									break;
								}
							default:
								lineItem = theTxn.addSaleMiscItem(item);
						}
					}

					ConfigMgr itemCfg = new ConfigMgr("item.cfg");
					//Employee Discount for Armani
					if (MiscItemManager.getInstance().isMiscItem(item.getItem().getId()) && LineItemPOSUtil.isNotOnFileItem(item.getItem().getId())) {
						//							&& item.getItem().getId().equals(itemCfg.getString("NOTONFILE.BASE_ITEM"))) {
						if (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") != null && (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") instanceof CMSEmployeeDiscount)) {
							lineItem.addDiscount((CMSEmployeeDiscount) theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT"));
						}
					}
					lineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());
					addLineItem(lineItem);
					if (lineItem instanceof SaleLineItem && !LineItemPOSUtil.isNotOnFileItem(item.getItem().getId())) {
						if (isAlterationAllowed()) {
							solicitCustomerOwnedAlteration();
							return;
						}
					}
				} catch (BusinessRuleException bx) {
					theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
				}
				pnlLineItem.repaint();
				updateLabel();
			}
			enterItem();
		} else if (Command.equals("SALE_TAX")) { // federal taxes
			try {
				theTxn.setTaxExemptId((String) obj);
				pnlHeader.setTaxExemptId((CMSStore) theStore, theTxn.getTaxExemptId(), theTxn.getRegionalTaxExemptId());
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
			if (isLayawayMode()) {
				initLayawayButtons();
			} else {
				initSaleButtons();
			}
			enterItem();
		} else if (Command.equals(((CMSStore) theStore).getRegionalTaxLabel())) { // regional taxes
			try {
				theTxn.setRegionalTaxExemptId((String) obj);
				pnlHeader.setTaxExemptId((CMSStore) theStore, theTxn.getTaxExemptId(), theTxn.getRegionalTaxExemptId());
				//theAppMgr.unRegisterSingleEditArea();
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
			if (isLayawayMode()) {
				initLayawayButtons();
			} else {
				initSaleButtons();
			}
			enterItem();
		} else if (Command.equals("BOTH_TAXES")) { // both federal and regional taxes
			try {
				theTxn.setTaxExemptId((String) obj);
				theTxn.setRegionalTaxExemptId((String) obj);
				pnlHeader.setTaxExemptId((CMSStore) theStore, theTxn.getTaxExemptId(), theTxn.getRegionalTaxExemptId());
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
			if (isLayawayMode()) {
				initLayawayButtons();
			} else {
				initSaleButtons();
			}
			enterItem();
			updateLabel();
		}
	} // end objectEvent

	/**
	 * put your documentation comment here
	 * @param saleItem
	 * @return
	 */
	private ArmCurrency getAmountDiscountOnSaleItem(CMSSaleLineItem saleItem) {
		Reduction[] reds = saleItem.getLineItemDetailsArray()[0].getReductionsArray();
		double amtReds = 0.0;
		for (int i = 0; i < reds.length; i++) {
			if (reds[i] instanceof CMSReduction && ((CMSReduction) reds[i]).getDiscount() != null) {
				//System.out.println("&&&&&&&&&&Type of Discount : " + ((CMSReduction)reds[i]).getDiscount().getType());
				//System.out.println("&&&&&&&&&&Amount of Discount : " + ((CMSReduction)reds[i]).getAmount());
				//System.out.println("&&&&&&&&&&Reason of Discount : " + ((CMSReduction)reds[i]).getReason());
				if (((CMSReduction) reds[i]).getDiscount().getType().equals("BY_AMOUNT_DISCOUNT")) {
					amtReds = amtReds + reds[i].getAmount().doubleValue();
				}
			}
		}
		return new ArmCurrency(amtReds);
	}

	/**
	 * @param Header
	 * @param anEvent
	 */
	public void appButtonEvent(String Header, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		//System.out.println("Header appButtonEvent : "+ sAction);
		if (sAction.equals("RESERVATIONS_OPEN")) {
			if (pnlLineItem.getSelectedLineItem() == null) {
				initReservationsOpenTxn();
				return;
			} else if (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?"))) {
				initReservationsOpenTxn();
				return;
			}
			anEvent.consume();
		} else if (sAction.equals("RESERVATIONS_CLOSE")) {
			if (pnlLineItem.getSelectedLineItem() == null) {
				initReservationsCloseTxn();
				return;
			} else if (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?"))) {
				initReservationsCloseTxn();
				return;
			}
			anEvent.consume();
		} else if (Header.equals("DISCOUNT")) {
			if (sAction.equals("BACK")) {
				showButtons();
				enterItem();
			} else if (sAction.equals("REMOVE")) {
				try {
					theTxn.removeAllDiscounts();
					theTxn.removeAllSettlementDiscounts();
					theAppMgr.removeStateObject("ARM_DISCOUNT_SEQNUM");
					theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
				}
				updateDiscountLabel();
				if (isLayawayMode()) {
					 //theTxn.resetLayawayDeposit();
				}
				pnlLineItem.repaint();
				updateLabel();
				showButtons();
				enterItem();
			} else {
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
			}
		} else if (Header.equals("SUBTOTAL_DISCOUNT")) {
			if (sAction.equals("BACK")) {
				theAppMgr.removeStateObject("IS_SUBTOTAL_DISCOUNT");
				showButtons();
				enterItem();
			} else if (sAction.equals("REMOVE")) {
				boolean ok = theAppMgr.showOptionDlg(res.getString("Remove Discounts"), res.getString("All discounts will be removed. Do you want to continue?"));
				if (ok) {
					theAppMgr.removeStateObject("IS_SUBTOTAL_DISCOUNT");
					try {
						POSLineItem posLineItems[] = theTxn.getLineItemsArray();
						for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
							posLineItems[i].removeAllDiscounts();
							if (posLineItems[i] instanceof CMSSaleLineItem)
								((CMSSaleLineItem) posLineItems[i]).removeAddPriceDiscount();
							if (posLineItems[i] instanceof CMSReturnLineItem)
								((CMSReturnLineItem) posLineItems[i]).removeAddPriceDiscount();
						}
						theTxn.removeAllDiscounts();
						theTxn.removeAllSettlementDiscounts();
						theAppMgr.removeStateObject("ARM_DISCOUNT_SEQNUM");
						theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
					} catch (BusinessRuleException ex) {
						theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					}
					updateDiscountLabel();
					if (isLayawayMode()) {
						 //theTxn.resetLayawayDeposit();
					}
					pnlLineItem.repaint();
					updateLabel();
					showButtons();
					enterItem();
				}
			} else {
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
				//started code for issue #1947 Light Pole Display by Neeti

				lineItmDeposit = pnlLineItem.getSelectedLineItem();
				LightPoleDisplay.getInstance().itemSoldForDiscount(lineItmDeposit, lineItmDeposit.getExtendedRetailAmount().formattedStringValue(),lineItmDeposit.getExtendedNetAmount().formattedStringValue());

				//Ended code for issue #1947 Light Pole Display by Neeti
			}
		} else if (Header.equals("MULTI_DISCOUNT")) {
			if (sAction.equals("BACK")) {
				theAppMgr.removeStateObject("IS_MULTI_DISCOUNT");
				showButtons();
				enterItem();
			} else if (sAction.equals("REMOVE")) {
				boolean ok = theAppMgr.showOptionDlg(res.getString("Remove Discounts"), res.getString("All discounts will be removed. Do you want to continue?"));
				if (ok) {
					theAppMgr.removeStateObject("IS_MULTI_DISCOUNT");
					try {
						POSLineItem posLineItems[] = theTxn.getLineItemsArray();
						for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
							posLineItems[i].removeAllDiscounts();
							if (posLineItems[i] instanceof CMSSaleLineItem)
								((CMSSaleLineItem) posLineItems[i]).removeAddPriceDiscount();
							if (posLineItems[i] instanceof CMSReturnLineItem)
								((CMSReturnLineItem) posLineItems[i]).removeAddPriceDiscount();
						}
						theTxn.removeAllDiscounts();
						theTxn.removeAllSettlementDiscounts();
						theAppMgr.removeStateObject("ARM_DISCOUNT_SEQNUM");
						theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
					} catch (BusinessRuleException ex) {
						theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					}
					updateDiscountLabel();
					if (isLayawayMode()) {
						System.out.println("InitialSaleApplet.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
					}
					pnlLineItem.repaint();
					updateLabel();
					showButtons();
					enterItem();
				}
			} else {
				if (pnlLineItemMulti.getSelectedLineItems().length <= 0) {
					theAppMgr.showErrorDlg(res.getString("At least one item need to be selected to give Multi discount."));
					//selectMultiDiscount();
					return;
				}
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);

				//Start code for Issue #1947 pole display by Neeti

			    POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
			    for(int i=0; i<lineItemArray.length ; i++){
			    	lineItmDeposit = lineItemArray[i];
			    	LightPoleDisplay.getInstance().itemSoldForDiscount(lineItmDeposit, lineItmDeposit.getExtendedRetailAmount().formattedStringValue(),lineItmDeposit.getExtendedNetAmount().formattedStringValue());
			    }

				//Ended code for Issue #1947 pole display by Neeti
			}
		} else if (Header.equals("LINE_DISCOUNT")) {
			if (sAction.equals("BACK")) {
				theAppMgr.removeStateObject("IS_LINE_ITEM_DISCOUNT");
				theAppMgr.removeStateObject("OVERRIDE_EMPLOYEE_DISCOUNT");
				theAppMgr.removeStateObject("SELECTED_LINE_ITEM");
				showButtons();
				enterItem();
			} else if (sAction.equals("REMOVE")) {
				boolean ok = theAppMgr.showOptionDlg(res.getString("Remove Discounts"), res.getString("All discounts will be removed. Do you want to continue?"));
				if (ok) {
					theAppMgr.removeStateObject("IS_LINE_ITEM_DISCOUNT");
					theAppMgr.removeStateObject("OVERRIDE_EMPLOYEE_DISCOUNT");
					theAppMgr.removeStateObject("SELECTED_LINE_ITEM");
					try {
						POSLineItem posLineItems[] = theTxn.getLineItemsArray();
						for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
							posLineItems[i].removeAllDiscounts();
							if (posLineItems[i] instanceof CMSSaleLineItem)
								((CMSSaleLineItem) posLineItems[i]).removeAddPriceDiscount();
							if (posLineItems[i] instanceof CMSReturnLineItem)
								((CMSReturnLineItem) posLineItems[i]).removeAddPriceDiscount();
						}
						theTxn.removeAllDiscounts();
						theTxn.removeAllSettlementDiscounts();
						theAppMgr.removeStateObject("ARM_DISCOUNT_SEQNUM");
						theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
					} catch (BusinessRuleException ex) {
						theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					}
					updateDiscountLabel();
					if (isLayawayMode()) {
						System.out.println("InitialSaleApplet.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
					}
					pnlLineItem.repaint();
					updateLabel();
					showButtons();
					enterItem();
				}
			} else {
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
				//Start code for Issue #1947 pole display  by Neeti

				lineItmDeposit = pnlLineItem.getSelectedLineItem();
				LightPoleDisplay.getInstance().itemSoldForDiscount(lineItmDeposit, lineItmDeposit.getExtendedRetailAmount().formattedStringValue(),lineItmDeposit.getExtendedNetAmount().formattedStringValue());

				//Ended code for Issue #1947 pole display by Neeti
			}
		}
		//Added transaction level discount
		else if (Header.equals("TRANS_DISCOUNT")) {
			if (sAction.equals("BACK")) {
				theAppMgr.removeStateObject("IS_SUBTOTAL_DISCOUNT");
				theAppMgr.removeStateObject("IS_MULTI_DISCOUNT");
				showButtons();
				enterItem();
			} else if (sAction.equals("REMOVE")) {
				boolean ok = theAppMgr.showOptionDlg(res.getString("Remove Discounts"), res.getString("All discounts will be removed. Do you want to continue?"));
				if (ok) {
					try {
						POSLineItem posLineItems[] = theTxn.getLineItemsArray();
						for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
							posLineItems[i].removeAllDiscounts();
							if (posLineItems[i] instanceof CMSSaleLineItem)
								((CMSSaleLineItem) posLineItems[i]).removeAddPriceDiscount();
							if (posLineItems[i] instanceof CMSReturnLineItem)
								((CMSReturnLineItem) posLineItems[i]).removeAddPriceDiscount();
						}
						theTxn.removeAllDiscounts();
						theTxn.removeAllSettlementDiscounts();
						theAppMgr.removeStateObject("ARM_DISCOUNT_SEQNUM");
						theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
					} catch (BusinessRuleException ex) {
						theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					}
					updateDiscountLabel();
					if (isLayawayMode()) {
						System.out.println("InitialSaleApplet.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
					}
					pnlLineItem.repaint();
					updateLabel();
					showButtons();
					enterItem();
				}
			} else {
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
				//started code for issue #1947 Light Pole Display  by Neeti
				lineItmDeposit = pnlLineItem.getSelectedLineItem();
				LightPoleDisplay.getInstance().itemSoldForDiscount(lineItmDeposit, lineItmDeposit.getExtendedRetailAmount().formattedStringValue(),lineItmDeposit.getExtendedNetAmount().formattedStringValue());
				//Ended code for issue #1947 Light Pole Display  by Neeti
			}
		} else if (Header.equals("EMPLOYEE_DISCOUNT")) {
			if (sAction.equals("CANCEL")) {
				appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0, "DISCOUNT", 0));
			}
		} else if (Header.equals("GENERIC_DISCOUNT")) {
			if (sAction.equals("CANCEL")) {
				String reDirectDiscount = (String) theAppMgr.getStateObject("ARM_DISCOUNT_TYPE");
				theAppMgr.removeStateObject("ARM_DISCOUNT_TYPE");
				if (reDirectDiscount.equals("PRICE_DISCOUNT"))
					goPrevious(anEvent);
				else
					appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0, reDirectDiscount, 0));
			}
		} else if (Header.equals("TAX_EXEMPT")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.unRegisterSingleEditArea();
				goPrevious(anEvent);
			} else if (sAction.equals("REMOVE_TAX_EXEMPT")) {
				objectEvent("SALE_TAX", "");
			}
			// Cancel also removes the tax exempt.
			//objectEvent("SALE_TAX", "");
		} else if (Header.equals("RESTORE_TAX")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
			} else {
				//         clearLineItemTax(sAction.equals( ( (CMSStore) theStore).getRegionalTaxLabel()));
				clearLineItemTax(false);
				updateLabel();
			}
		} else if (Header.equals("MODIFY_TAX")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
			} else {
				if (pnlDetailLineItem.isRowSelected()) {
					theAppMgr.setSingleEditArea(res.getString("Enter the manual tax amount."), "MODIFY_TAX" + "_" + sAction, theAppMgr.CURRENCY_MASK);
				} else {
					theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
					theAppMgr.setEditAreaFocus();
				}
			}
		} else if (Header.equals("MODIFY_TAX_PERCENT")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
			} else {
				if (pnlDetailLineItem.isRowSelected()) {
					theAppMgr.setSingleEditArea(res.getString("Enter the manual tax percent."), "MODIFY_TAX_PERCENT" + "_" + sAction, theAppMgr.DOUBLE_MASK);
				} else {
					theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
					theAppMgr.setEditAreaFocus();
				}
			}
		} else if (Header.equals("MARK_AMT")) {
			if (sAction.equals("PREV")) {
				cardLayout.show(cardPanel, "LINE_ITEM");
				updateLabel();
				appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0, "MARKDOWNS", 0));
				//showButtons();
				//enterItem();
			} else if (sAction.equals("CONTINUE")) {
				if ((isMarkdownMulti && pnlLineItemMulti.getSelectedLineItems().length == 0) || (!isMarkdownMulti && !pnlLineItem.isRowSelected())) {
					theAppMgr.showErrorDlg(res.getString("No items were selected."));
				} else {
					enterMarkdownAmount();
				}
			}
		} else if (Header.equals("MARKDOWN_PCT")) {
			if (sAction.equals("PREV")) {
				cardLayout.show(cardPanel, "LINE_ITEM");
				updateLabel();
				appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0, "MARKDOWNS", 0));
				//showButtons();
				//enterItem();
			} else if (sAction.equals("CONTINUE")) {
				if ((isMarkdownMulti && pnlLineItemMulti.getSelectedLineItems().length == 0) || (!isMarkdownMulti && !pnlLineItem.isRowSelected())) {
					theAppMgr.showErrorDlg(res.getString("No items were selected."));
				} else {
					enterMarkdownPercent();
				}
			}
		} else if (Header.equals("MARKDOWNS")) {
			if (sAction.equals("MOD_PRICE")) {
				if (pnlLineItem.isRowSelected()) {
					theAppMgr.setSingleEditArea(res.getString("Enter the new price for selected item."), "MODIFY_PRICE", theAppMgr.CURRENCY_MASK);
				} else {
					theAppMgr.setEditAreaFocus();
				}
			} else if (sAction.equals("MARK_AMT")) {
				if (isMarkdownMulti && pnlLineItem.getModel().getTotalRowCount() > 0) {
					pnlLineItemMulti.loadModel((POSLineItem[]) pnlLineItem.getModel().getAllRows().toArray(new POSLineItem[pnlLineItem.getModel().getAllRows().size()]));
					cardLayout.show(cardPanel, "LINE_ITEM_MULTI");
					theAppMgr.showMenu(MenuConst.POS_CONTINUE, "MARK_AMT", theOpr, theAppMgr.PREV_BUTTON);
					theAppMgr.setSingleEditArea(res.getString("Please indicate which item(s) you want to mark down and then press 'Continue'."));
				} else {
					if (pnlLineItem.isRowSelected()) {
						appButtonEvent("MARK_AMT", new CMSActionEvent(this, 0, "CONTINUE", 0));
					} else {
						theAppMgr.setEditAreaFocus();
					}
				}
			} else if (sAction.equals("MARK_PCT")) {
				if (isMarkdownMulti && pnlLineItem.getModel().getTotalRowCount() > 0) {
					pnlLineItemMulti.loadModel((POSLineItem[]) pnlLineItem.getModel().getAllRows().toArray(new POSLineItem[pnlLineItem.getModel().getAllRows().size()]));
					cardLayout.show(cardPanel, "LINE_ITEM_MULTI");
					theAppMgr.showMenu(MenuConst.POS_CONTINUE, "MARKDOWN_PCT", theOpr, theAppMgr.PREV_BUTTON);
					theAppMgr.setSingleEditArea(res.getString("Please indicate which item(s) you want to mark down and then press 'Continue'."));
				} else {
					if (pnlLineItem.isRowSelected()) {
						appButtonEvent("MARKDOWN_PCT", new CMSActionEvent(this, 0, "CONTINUE", 0));
					} else {
						theAppMgr.setEditAreaFocus();
					}
				}
			} else if (sAction.equals("PREV")) {
				//updateLabel();
				showButtons();
				enterItem();
			}
		} else if (Header.equals("ITEMBUILDER")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.unRegisterSingleEditArea();
				showButtons();
				enterItem();
			}
		} else if (sAction.equals("V12_BASKET")) {
			 if (!theAppMgr.isOnLine()) {
					theAppMgr.showErrorDlg(res
									.getString("This function is not available in Offline Mode"));
					anEvent.consume();
					return;
			 }
		} else if (Header.equals("LOYALTYCARD")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.unRegisterSingleEditArea();
				showButtons();
				enterItem();
			}
		} else if (Header.equals("SUSPEND_TXN")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.unRegisterSingleEditArea();
				goPrevious(anEvent);
			}
		} else if (Header.equals("RESERVATIONS")) {
			if (sAction.equals("PREV")) {
				goPrevious(anEvent);
			}
		}
		//Started code for Dolci PCR
		else if(Header.equals("DOLCI")){
			//if (sAction.equals("PRALINE_CLUSTER")) {
			//	String[] buttons = CMSItemMgr.getPralinesClustersBoxKeys();
				//setButtons(buttons, "PRALINE_CLUSTER");
			//}
			//Added by Satin for HOT_CHOCO Dolci buttons(OCT-18-2012)
			if (sAction.equals("HOT_CHOCO")) {
				String[] buttons = CMSItemMgr.getHotChocoBoxKeys();
				setButtons(buttons, "HOT_CHOCO");
			}
			else if (sAction.equals("CARAQUE")) {
				String[] buttons = CMSItemMgr.getCaraqueKeys();
				setButtons(buttons, "CARAQUE");
			}
			else if (sAction.equals("DRAGEES")) {
				String[] buttons = CMSItemMgr.getDrageesBoxKeys();
				setButtons(buttons, "DRAGEES");
			}
			else if(sAction.equals("FRUIT_JELLY")){
				String[] buttons = CMSItemMgr.getFruitJellyBoxKeys();
				setButtons(buttons, "FRUIT_JELLY");
			}
			else if(sAction.equals("PRALINES")){
				String[] buttons = CMSItemMgr.getPralineBoxKeys();
				setButtons(buttons, "PRALINES");
			}
			//start code for new dolci items:deepika
			else if (sAction.equals("JARS")) {
				String[] buttons = CMSItemMgr.getJarsBoxKeys();
				setButtons(buttons, "JARS");
			}
			else if (sAction.equals("TEA")) {
				String[] buttons = CMSItemMgr.getTeaBoxKeys();
				setButtons(buttons, "TEA");
			}
			else if (sAction.equals("MARRON_GLACEE")) {
				String[] buttons = CMSItemMgr.getMarronGlaceeBoxKeys();
				setButtons(buttons, "MARRON_GLACEE");
			}
			else if (sAction.equals("SHORTBRD_BISC")) {
				String[] buttons = CMSItemMgr.getShortbrdBiscBoxKeys();
				setButtons(buttons, "SHORTBRD_BISC");
			}
			else if (sAction.equals("ORANGE_PEELS")) {
				String[] buttons = CMSItemMgr.getOrangePeelsBoxKeys();
				setButtons(buttons, "ORANGE_PEELS");
			}
			else if (sAction.equals("HOLIDAY_PEELS")) {
				String[] buttons = CMSItemMgr.getHolidayBoxKeys();
				setButtons(buttons, "HOLIDAY_PEELS");
			}
			//Code added by vivek mishra on 26-SEP-2012
			else if (sAction.equals("CORP_GIFT")) {
				String[] buttons = CMSItemMgr.getCorpGiftBoxKeys();
				setButtons(buttons, "CORP_GIFT");
			}
			//Code ended vivek mishra

			//end code for new dolci items :deepika

		}

		//Ended code for Dolci PCR
	}
	//Added new method for code for Dolci PCR
	private void clickButton(String key) {
		ConfigMgr config = new ConfigMgr("item.cfg");
		config = new ConfigMgr("item.cfg");
		Double amount = config.getDouble(key+".AMT");
		String keyValue = config.getString(key);
		HashMap map = new HashMap();
		map.put("ItemCode", keyValue);
		map.put("Amount", amount);
		theAppMgr.buildObject(this, "ITEM", itemBuilder, map);
		int index = key.indexOf(".");
		String keyIndex = key.substring(index+1);
		pnlLineItem.modifyDescription(keyIndex);
	}
	//Ended code for Dolci PCR

	/**
	 * @param anEvent
	 */
	/**
	 * put your documentation comment here
	 */
	private void enterRSVODepositAmount() {
		ArmCurrency depositBalance = null;
		if (theTxn.getCustomer() != null) {
			depositBalance = ((CMSCustomer) theTxn.getCustomer()).getCustomerBalance();
		}
		if (depositBalance == null)
			depositBalance = new ArmCurrency(0.0d);
		String sPrompt = res.getString("Enter Amount") + " ( " + res.getString("Previous Balance") + depositBalance.formattedStringValue() + " )";
		theAppMgr.setSingleEditArea(sPrompt, "RSVO_DEPOSIT", theAppMgr.CURRENCY_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterAdjustDepositAmount() {
		ArmCurrency txnDepositBalance = (ArmCurrency) theAppMgr.getStateObject("ARM_RSV_DEPOSIT");
		if (txnDepositBalance == null) {
			txnDepositBalance = new ArmCurrency(0.0d);
		}
		//String sPrompt = res.getString("Enter new deposit amount") + " ( " + res.getString("Transaction Deposit Balance") + txnDepositBalance.formattedStringValue() + " )";
		StringBuffer sPrompt = new StringBuffer();
		sPrompt.append(res.getString("Enter new deposit amount"));
		sPrompt.append(" ( ");
		sPrompt.append(res.getString("Transaction Deposit Balance "));
		sPrompt.append(txnDepositBalance.formattedStringValue());
		sPrompt.append(" )");
		theAppMgr.setSingleEditArea(sPrompt.toString(), "ADJUST_DEPOSIT", theAppMgr.CURRENCY_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void initReservationsOpenTxn() {
		try {
			fldDeposit.setText("");
			CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");

			Employee txnConsultant = null;
			if (theTxn.getCustomer() != null)
				theAppMgr.addStateObject("CUSTOMER", theTxn.getCustomer());
			if (theTxn.getConsultant() != null) {
				txnConsultant = theTxn.getConsultant();
				theAppMgr.addStateObject("ASSOCIATE", txnConsultant);
			}

			theTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
			//  theTxn.setIsNoSaleTxn(false);
			theTxn.setConsultant(txnConsultant);
			//      theTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
			// theTxn.setIsNoSaleTxn(false);
			ConfigMgr reservationConfig = new ConfigMgr("reservation.cfg");
			try {
				Calendar calendar = Calendar.getInstance();
				ConfigMgr config = new ConfigMgr("pos.cfg");
				int iDays = -1;
				if (config != null) {
					iDays = Integer.parseInt(config.getString("RESERVATION_EXPIRY_NUMBER_OF_DAYS"));
					iDays += calendar.get(Calendar.DATE);
					calendar.set(Calendar.DATE, iDays);
					theTxn.getReservationTransaction().setExpirationDate(calendar.getTime());
				}
			} catch (Exception e) {
				System.out.println("Exception--> Setting Reservation Expiry date");
			}
			theAppMgr.addStateObject("TXN_POS", theTxn);
			theAppMgr.addStateObject("TXN_MODE", new Integer(RESERVATIONS_OPEN));
			//dReservationDepositPercent
			if (reservationConfig != null && reservationConfig.getString("SOLICIT_RESERVATION_REASON") != null) {
				if (reservationConfig.getString("SOLICIT_RESERVATION_REASON").equalsIgnoreCase("TRUE")) {
					ReservationReasonDlg reservationDlg = new ReservationReasonDlg(theAppMgr.getParentFrame(), theAppMgr, res.getString("Reservation Reason"));
					if (reservationDlg.areReservationReasonsAvailable()) {
						reservationDlg.setVisible(true);
						if (reservationDlg.getSelectedReservationReason() != null)
							theTxn.getReservationTransaction().setReservationReason(reservationDlg.getSelectedReservationReason());
					}
				}
				if (reservationConfig.getDouble("SUGGESTIVE_DEPOSIT_PERCENT") != null)
					dReservationDepositPercent = reservationConfig.getDouble("SUGGESTIVE_DEPOSIT_PERCENT").doubleValue();
				bUpdateDepositAmount = true;
			}
			//Fix for issue# 1912 Broken Transaction Discount was removed at transaction level
			Object obj = theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT");
			CMSDiscount dis = (CMSDiscount)obj;
			if ((dis instanceof CMSEmployeeDiscount)) {
				if (!dis.isLineItemDiscount && !dis.isSubTotalDiscount && !dis.isMultiDiscount) {
					addTransactionDiscount(dis);
				}
				theTxn.addDiscount((CMSEmployeeDiscount) dis);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void initReservationsCloseTxn() {
		try {
			CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
			Employee txnConsultant = null;
			if (theTxn.getCustomer() != null)
				theAppMgr.addStateObject("CUSTOMER", theTxn.getCustomer());
			if (theTxn.getConsultant() != null) {
				txnConsultant = theTxn.getConsultant();
				theAppMgr.addStateObject("ASSOCIATE", txnConsultant);
			}
			theTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
			//    theTxn.setIsNoSaleTxn(false);
			theTxn.setConsultant(txnConsultant);
			theAppMgr.addStateObject("TXN_POS", theTxn);
			theAppMgr.addStateObject("TXN_MODE", new Integer(RESERVATIONS_CLOSE));
			//      ReservationReasonDlg reservationDlg = new ReservationReasonDlg(theAppMgr.getParentFrame()
			//          , theAppMgr, res.getString("Reservation Reason"));
			//      reservationDlg.setVisible(true);
			//      if (reservationDlg.getSelectedReservationReason() != null)
			//        theTxn.getReservationTransaction().setReservationReason(reservationDlg.
			//            getSelectedReservationReason());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		theAppMgr.unRegisterSingleEditArea(); // cancel any builders in progress
		String sAction = anEvent.getActionCommand();
		//     System.out.println("Appbuttonevent : "+ sAction);
		// Alterations - MSB
		// Commented for #105.
		//    if (sAction.equals("LOYALTY"))
		//      theAppMgr.unRegisterSingleEditArea();
		//
		//      ConfigMgr config = new ConfigMgr("loyalty.cfg");
		//
		//      String Builder = config.getString("LOYALTY_ITEM_BUILDER");
		//
		//      System.out.println("the builder is: " + Builder);
		//      theAppMgr.buildObject("LOYALTY_ITEM", Builder, "");
		if (sAction.equals("REMOVE_PROMOTION")) {
			anEvent.consume();
			if (!pnlLineItem.isRowSelected()) {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				return;
			}
			if (pnlLineItem.getSelectedLineItem().getItem().getPromotionIds() != null && (!pnlLineItem.getSelectedLineItem().getItem().getPromotionIds().hasNext())) {
				theAppMgr.showErrorDlg(res.getString("There are no promotions on line item to be removed"));
				return;
			}
			removePromotion();
			return;
		}
		if (sAction.equals("TENDER")) {
			//#1217
			if (this.getMode() == InitialSaleApplet.PRE_SALE_OPEN) {
				if (theAppMgr.getStateObject("ADD_CREDITCARD") == null) {
					//Anjana commenting the presale open tender code as we dont need to capture the tender information during presale open
				/*	if (theAppMgr.showOptionDlg(res.getString("Presale Open"), res.getString("Do want to add a Credit Card to this Presale transaction?"))) {
						theAppMgr.addStateObject("ADD_CREDITCARD", "ADD_CREDITCARD");
						String Builder = CMSPaymentMgr.getPaymentBuilder("CUSTOMER_CREDIT_CARD");
						theAppMgr.buildObject("PRESALE_CREDIT_CARD", Builder, "");
						anEvent.consume();
					}*/
				}
				//return;
			}

			totalTransactionAmount = theTxn.getCompositeNetAmount().formattedStringValue() + " / " + theTxn.getCompositeTotalAmountDue().formattedStringValue();
			String txnType  = theTxn.getTransactionType();
			//Anjana added manger approval for even exchanges as well
			if(totalTransactionAmount.startsWith("-") || txnType.equalsIgnoreCase("SALE/RETN")){
				CMSEmployee employee = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
				theApprover = null;
				theAppMgr.setSingleEditArea(res.getString("Enter approver id for return and press 'Enter'.")
				        , "APPROVER", theAppMgr.PASSWORD_MASK);
				//this.editAreaEvent("APPROVER",employee.getExternalID());
				anEvent.consume();
				//return;
			}


		}
		if (sAction.equals("RESERVATIONS")) {
			theAppMgr.showMenu(MenuConst.RESERVATIONS, "RESERVATIONS", theOpr);
			anEvent.consume();
			vecMenus.add(sAction);
			sAction_global = sAction;
			selectOption();
		}
		if (sAction.equals("DEPOSIT")) {
			if (isReservationOpenMode()) {
				if (theTxn.getCustomer() == null) {
					theAppMgr.addStateObject("ARM_DEPOSIT_CUSTOMER", "REQUIRED");
					return;
				}
				buildDepositItem(true);
				anEvent.consume();
			}
		}
		if (sAction.equals("ADJUST_DEPOSIT")) {
			anEvent.consume();
			if (lineItmDeposit == null) {
				theAppMgr.showErrorDlg(res.getString("There is no deposit to adjust"));
				return;
			} else {
				enterAdjustDepositAmount();
			}
		}
		//
		//    }
		if (sAction.equals("OPTIONS_ALTERATIONS") || sAction.equals("ALTERATIONS")) {
			// If No Line Item is selected
			if (!pnlLineItem.isRowSelected()) {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				anEvent.consume();
				return;
			}
			Integer quantity = (pnlLineItem.getSelectedLineItem()).getQuantity();
			// MP: Added for bug #500
			if (quantity.intValue() > 1) {
				theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for alteration items"));
				anEvent.consume();
				return;
			}
			// If selected LineItem is Instance of ReturnLineItem
			if (!(pnlLineItem.getSelectedLineItem() instanceof SaleLineItem || pnlLineItem.getSelectedLineItem() instanceof CMSPresaleLineItem)) {
				theAppMgr.showErrorDlg(res.getString("Alteration not allowed on this item"));
				anEvent.consume();
				return;
			}
			if (!isAlterationAllowed()) {
				theAppMgr.showErrorDlg(res.getString("Alteration not allowed on this item"));
				anEvent.consume();
				return;
			}
			// If selected LineItem already has any alterations.
			// populate them in a dialog box and show
			if (lineItemHasAlteration(true)) {
				alterationIDDlg.setVisible(true);
				if (alterationIDDlg.getSelectedAlterationID() == null) {
					anEvent.consume();
					return;
				}
				if (alterationIDDlg.getSelectedAlterationID().equals("ARM_NEW_ALTERATION")) {
					enterAlterationID();
					theAppMgr.addStateObject("ALTERATION_SUB_GROUP_ID", ((CMSItem) pnlLineItem.getSelectedLineItem().getItem()).getClassId());
					anEvent.consume();
					return;
				}
				theAppMgr.addStateObject("ARM_SELECT_ALTERATION_ID", alterationIDDlg.getSelectedAlterationID());
				theAppMgr.addStateObject("POS_LINE_ITEM", pnlLineItem.getSelectedLineItem());
				theAppMgr.addStateObject("ALTERATION_SUB_GROUP_ID", ((CMSItem) pnlLineItem.getSelectedLineItem().getItem()).getClassId());
			}
			// Prompt to enter new alteration ID.
			else {
				// Issue # 530
				theAppMgr.addStateObject("POS_LINE_ITEM", pnlLineItem.getSelectedLineItem());
				if (checkAlterationValid())
					enterAlterationID();
				theAppMgr.addStateObject("ALTERATION_SUB_GROUP_ID", ((CMSItem) pnlLineItem.getSelectedLineItem().getItem()).getClassId());
				anEvent.consume();
			}
		}
		if (sAction.equals("SALE_TXN")) {
			try {
				//System.out.println("going to convertPresaleToSaleTransaction from SaleApplet...................");
				theTxn.convertPresaleToSaleTransaction();
				theTxn.convertConsignmentToSaleTransaction();
				theAppMgr.addStateObject("TXN_MODE", new Integer(SALE_MODE));
				start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (sAction.equals("SALE_TXN_RESERVATIONS")) {
			try {
				theTxn.convertReservationToSaleTransaction();
				theAppMgr.addStateObject("TXN_MODE", new Integer(SALE_MODE));
				anEvent.consume();
				start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (sAction.equals("ADD_ITEM")) {
			// enterItem(); //  return to default action, for example if user wants to cancel out of a builder
		//	CMSItemWrapper itemWrapper = (CMSItemWrapper)getSelectedLineItem();
			updateLabel();
			initSaleButtons();
			enterItem();
			//CMSApplet.theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, super.theOpr);
		} else if (sAction.equals("SEARCH")) {
			vecMenus.add(sAction);
			CMSApplet.theAppMgr.showMenu(MenuConst.SEARCH, super.theOpr);
		} else if (sAction.equals("LOCATE")) {
			vecMenus.add(sAction);
			CMSApplet.theAppMgr.showMenu(MenuConst.LOCATE, super.theOpr);
		} else if (sAction.equals("REWARD_DISCOUNT")) {
			theAppMgr.buildObject(this, "DISCOUNT", rewardDiscountBldr, "");
		} else if (sAction.equals("BY_PRICE_DISCOUNT")) {
			if (theTxn.getLineItemsArray().length <= 0) {
				theAppMgr.showErrorDlg(res.getString("Atleast one item need to be added to transaction to give Line Item discount"));
				enterItem();
				return;
			}
			if (pnlLineItem.getSelectedLineItem() != null && pnlLineItem.getSelectedLineItem().isMiscItem() && !LineItemPOSUtil.isNotOnFileItem(pnlLineItem.getSelectedLineItem().getItem().getId())) { // if Not-on-File Item)
				theAppMgr.showErrorDlg(res.getString("Price discount cannot be applied to Service Item."));
				enterItem();
				return;
			}
			String discountBldr = CMSDiscountMgr.getBuilderName("BY_PRICE_DISCOUNT");
			theAppMgr.addStateObject("IS_LINE_ITEM_DISCOUNT", new Boolean(true));
			theAppMgr.addStateObject("ARM_DISCOUNT_TYPE", "PRICE_DISCOUNT");
			theAppMgr.addStateObject("SELECTED_LINE_ITEM", pnlLineItem.getSelectedLineItem());
			theAppMgr.buildObject(this, "DISCOUNT", discountBldr, sAction);
		} else if (sAction.equals("SUBTOTAL_DISCOUNT")) {
			if (theTxn.getLineItemsArray().length <= 0) {
				theAppMgr.showErrorDlg(res.getString("Atleast one item need to be added to transaction to give Sub-total discount"));
				enterItem();
				return;
			}
			if (theTxn.getReturnLineItemsArray().length > 0 && theTxn.getSaleLineItemsArray().length > 0) {
				theAppMgr.showErrorDlg(res.getString("SubTotal Discount Cannot be Applied to a Transaction containing Sale/Return Line Item."));
				return;
			}
			String[] dis = CMSDiscountMgr.getDiscountTypes();
			ArrayList usedDiscount = new ArrayList();
			int ct = 0;
			for (int i = 0; i < dis.length; i++) {
				if (!dis[i].equals("EMPLOYEE") && (!dis[i].equals("BY_PRICE_DISCOUNT"))) {
					usedDiscount.add(ct, dis[i]);
					ct++;
				}
			}
			JButton[] btns = new JButton[usedDiscount.size() + 2];
			for (int x = 0; x < usedDiscount.size(); x++) {
				String label = CMSDiscountMgr.getLabel((String) usedDiscount.get(x));
				btns[x] = new JButton(label);
				btns[x].setActionCommand((String) usedDiscount.get(x));
			}
			Theme theme = theAppMgr.getTheme();
			btns[usedDiscount.size()] = new JButton();
			btns[usedDiscount.size()].setText(HTML.formatLabeltoHTML(res.getString("Remove All Discounts"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size()].setActionCommand("REMOVE");
			btns[usedDiscount.size() + 1] = new JButton(res.getString("Previous"));
			btns[usedDiscount.size() + 1].setActionCommand("BACK");
			theAppMgr.setButtons("SUBTOTAL_DISCOUNT", btns);
			theAppMgr.addStateObject("IS_SUBTOTAL_DISCOUNT", new Boolean(true));
			theAppMgr.addStateObject("ARM_DISCOUNT_TYPE", "SUBTOTAL_DISCOUNT");
			selectDiscount();
		} else if (sAction.equals("MULTI_DISCOUNT")) {
			if (theTxn.getLineItemsArray().length <= 0) {
				theAppMgr.showErrorDlg(res.getString("Atleast one item need to be added to transaction to give Multi discount"));
				enterItem();
				return;
			}
			if (theTxn.getReturnLineItemsArray().length > 0 && theTxn.getSaleLineItemsArray().length > 0) {
				theAppMgr.showErrorDlg(res.getString("Multi Discount Cannot be Applied to a Transaction containing Sale/Return Line Item."));
				return;
			}
			if (pnlLineItem.getModel().getTotalRowCount() > 0) {
				pnlLineItemMulti.loadModel((POSLineItem[]) pnlLineItem.getModel().getAllRows().toArray(new POSLineItem[pnlLineItem.getModel().getAllRows().size()]));
			}
			String[] dis = CMSDiscountMgr.getDiscountTypes();
			ArrayList usedDiscount = new ArrayList();
			int ct = 0;
			for (int i = 0; i < dis.length; i++) {
				if (!dis[i].equals("EMPLOYEE")) {
					usedDiscount.add(ct, dis[i]);
					ct++;
				}
			}
			JButton[] btns = new JButton[usedDiscount.size() + 2];
			for (int x = 0; x < usedDiscount.size(); x++) {
				String label = CMSDiscountMgr.getLabel((String) usedDiscount.get(x));
				btns[x] = new JButton(label);
				btns[x].setActionCommand((String) usedDiscount.get(x));
			}
			Theme theme = theAppMgr.getTheme();
			btns[usedDiscount.size()] = new JButton();
			btns[usedDiscount.size()].setText(HTML.formatLabeltoHTML(res.getString("Remove All Discounts"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size()].setActionCommand("REMOVE");
			btns[usedDiscount.size() + 1] = new JButton(res.getString("Previous"));
			btns[usedDiscount.size() + 1].setActionCommand("BACK");
			theAppMgr.setButtons("MULTI_DISCOUNT", btns);
			theAppMgr.addStateObject("IS_MULTI_DISCOUNT", new Boolean(true));
			theAppMgr.addStateObject("ARM_DISCOUNT_TYPE", "MULTI_DISCOUNT");
			cardLayout.show(cardPanel, "LINE_ITEM_MULTI");
			selectMultiDiscount();
		} else if (sAction.equals("LINE_DISCOUNT")) {
			if (theTxn.getLineItemsArray().length <= 0) {
				theAppMgr.showErrorDlg(res.getString("Atleast one item need to be added to transaction to give Line Item discount"));
				enterItem();
				return;
			}
			String[] dis = CMSDiscountMgr.getDiscountTypes();
			ArrayList usedDiscount = new ArrayList();
			int ct = 0;
			for (int i = 0; i < dis.length; i++) {
				if (!dis[i].equals("EMPLOYEE")) {
					usedDiscount.add(ct, dis[i]);
					ct++;
				}
			}
			JButton[] btns = new JButton[usedDiscount.size() + 2];
			for (int x = 0; x < usedDiscount.size(); x++) {
				String label = CMSDiscountMgr.getLabel((String) usedDiscount.get(x));
				btns[x] = new JButton(label);
				btns[x].setActionCommand((String) usedDiscount.get(x));
			}
			Theme theme = theAppMgr.getTheme();
			btns[usedDiscount.size()] = new JButton();
			btns[usedDiscount.size()].setText(HTML.formatLabeltoHTML(res.getString("Remove All Discounts"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size()].setActionCommand("REMOVE");
			btns[usedDiscount.size() + 1] = new JButton(res.getString("Previous"));
			btns[usedDiscount.size() + 1].setActionCommand("BACK");
			theAppMgr.setButtons("LINE_DISCOUNT", btns);
			theAppMgr.addStateObject("IS_LINE_ITEM_DISCOUNT", new Boolean(true));
			theAppMgr.addStateObject("ARM_DISCOUNT_TYPE", "LINE_DISCOUNT");
			theAppMgr.addStateObject("SELECTED_LINE_ITEM", pnlLineItem.getSelectedLineItem());
			selectDiscount();
		} else if (sAction.equals("TRANS_DISCOUNT")) {
			String[] dis = CMSDiscountMgr.getDiscountTypes();
			ArrayList usedDiscount = new ArrayList();
			int ct = 0;
			for (int i = 0; i < dis.length; i++) {
				if (!dis[i].equals("EMPLOYEE") && (!dis[i].equals("BY_PRICE_DISCOUNT"))) {
					usedDiscount.add(ct, dis[i]);
					ct++;
				}
			}
			JButton[] btns = new JButton[usedDiscount.size() + 2];
			for (int x = 0; x < usedDiscount.size(); x++) {
				String label = CMSDiscountMgr.getLabel((String) usedDiscount.get(x));
				btns[x] = new JButton(label);
				btns[x].setActionCommand((String) usedDiscount.get(x));
			}
			Theme theme = theAppMgr.getTheme();
			btns[usedDiscount.size()] = new JButton();
			btns[usedDiscount.size()].setText(HTML.formatLabeltoHTML(res.getString("Remove All Discount"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size()].setActionCommand("REMOVE");
			btns[usedDiscount.size() + 1] = new JButton(res.getString("Previous"));
			btns[usedDiscount.size() + 1].setActionCommand("BACK");
			theAppMgr.setButtons("TRANS_DISCOUNT", btns);
			theAppMgr.addStateObject("ARM_DISCOUNT_TYPE", "TRANS_DISCOUNT");
			selectDiscount();
		} else if (sAction.equals("OVERRIDE_EMPLOYEE_DISCOUNT")) {
			if (!theTxn.isEmployeeSale) {
				theAppMgr.showErrorDlg(res.getString("This transaction is not an employee sale."));
				enterItem();
				return;
			}
			if (theTxn.getLineItemsArray().length <= 0) {
				theAppMgr.showErrorDlg(res.getString("Atleast one item need to be added to transaction to override employee discount."));
				enterItem();
				return;
			}
			theAppMgr.addStateObject("OVERRIDE_EMPLOYEE_DISCOUNT", new Boolean(true));
			CMSActionEvent newActionEvent = new CMSActionEvent(anEvent.getSource(), anEvent.getID(), "LINE_DISCOUNT", anEvent.getModifiers());
			this.appButtonEvent(newActionEvent);
		} else if (sAction.equals("OPTIONS")) {
			sAction_global = sAction;
			vecMenus.add(sAction);
			CMSApplet.theAppMgr.showMenu(MenuConst.OPTIONS, super.theOpr);
			// Issue # 804
			theAppMgr.setSingleEditArea(res.getString("Select option"));
		} else if (sAction.equals("PRICING")) {
			sAction_global = sAction;
			vecMenus.add(sAction);
			CMSApplet.theAppMgr.showMenu(MenuConst.PRICING, super.theOpr);
			// Issue # 804
			theAppMgr.setSingleEditArea(res.getString("Select option"));
		} else if (sAction.equals("OTHER_TXN")) {
			sAction_global = sAction;
			vecMenus.add(sAction);
			CMSApplet.theAppMgr.showMenu(MenuConst.OTHER_TXNS_MENU, super.theOpr);
			// Issue # 804
			theAppMgr.setSingleEditArea(res.getString("Select option"));
		} //mayuri Edhara :: for Gift Card Integration.
		else if (sAction.equals("GIFTCARD_SERVICES")) {
			sAction_global = sAction;
			vecMenus.add(sAction);
			CMSApplet.theAppMgr.showMenu(MenuConst.GIFTCARD_SERVICES, super.theOpr,theAppMgr.PREV_BUTTON);
			theAppMgr.setSingleEditArea(res.getString("Select option"));
		}else if (sAction.equals("BALANCE_INQUIRY")){
				String gcBalance = CMSCreditAuthHelper.getBalanceInquiry(theAppMgr);
				if(gcBalance != null){
					theAppMgr.showErrorDlg("The Balance Amount on the card is : $" + gcBalance);
				}else{
					theAppMgr.showErrorDlg("Balance could not be retrieved. Please try again later.");
				}
		} // end mayuri edhara::
		//Start: Added by Himani for GC Transaction History on 03-NOV-2016
		else if(sAction.equals("GC_TRANSACTION_HISTORY")){
			Object gcTxnHistory=CMSCreditAuthHelper.getTransactionHistory(theAppMgr);
			if(gcTxnHistory != null){
				System.out.println(gcTxnHistory);
				String[] txns=gcTxnHistory.toString().split("\u001D");
				if(txns.length > 0)
				{
					String miniStmt="";
					String[] actTxn=txns[0].split("\u001C");
					if(actTxn[2].trim().equals("GA")){
					String actDateTime=actTxn[0].substring(2, 4)+"/"+actTxn[0].substring(0, 2)+"/"+actTxn[0].substring(4, 6)+" "+actTxn[1].substring(0, 2)+":"+actTxn[1].substring(2, 4);
					String actAmount=actTxn[4];
					double actAmt=(Double.parseDouble(actAmount))/100;
					miniStmt+="Activation Date and Time:"+actDateTime+"\nActivation Amount:$"+actAmt+"";
					}
					miniStmt+="\nTransaction Details:";
					miniStmt+="\n\nDate and Time\tAmount\tBrand\t\tTransaction Type\n";
					for(int i=1;i<txns.length;i++)
					{
						String[] eachTxn=txns[i].split("\u001C");
						String txnDateTime=eachTxn[0].substring(2, 4)+"/"+eachTxn[0].substring(0, 2)+"/"+eachTxn[0].substring(4, 6)+" "+eachTxn[1].substring(0, 2)+":"+eachTxn[1].substring(2, 4);
						String txnAmount=eachTxn[4];
						double txnAmt=(Double.parseDouble(txnAmount))/100;
						String brand=eachTxn[5];
						String txnType="";
						if(eachTxn[2]!=null)
						{
							if(eachTxn[2].trim().equals("GP"))
								txnType="Sale";
							else if(eachTxn[2].trim().equals("GL"))
								txnType="Reload";
							else if(eachTxn[2].trim().equals("GF"))
								txnType="Refund"; 
							else if(eachTxn[2].trim().equals("GU"))
								txnType="Cashout";
							else
								txnType="Void";
						}
						miniStmt+="\n"+txnDateTime+"\t"+txnAmt+"\t"+brand+"\t"+txnType;
					}
					//theAppMgr.showErrorDlg("Transaction History :                  " + miniStmt);
					JPanel middlePanel = new JPanel ();
				    middlePanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Transaction History" ) );

				    // create the middle panel components

				    JTextArea display = new JTextArea ( 10, 58 );
				    display.setEditable ( false ); // set textArea non-editable
				    display.setText(miniStmt);
				    JScrollPane scroll = new JScrollPane ( display );
				    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
				    
				    //Add Textarea in to middle panel
				    middlePanel.add ( scroll );

				    // My code
				    JFrame frame = new JFrame ();
				    frame.add ( middlePanel );
				    frame.pack ();
				    frame.setLocationRelativeTo ( null );
				    frame.setVisible ( true );
				}
			}
		}
		//End: Added by Himani for GC Transaction History on 03-NOV-2016
		//Start: Added by Himani for Reload GC on 19-SEP-2016
		else if(sAction.equals("RELOAD_GC")){
			MiscItemTemplate miscItemTemplate=null;
			MiscItemTemplate[] misc = MiscItemManager.getInstance().getMiscItemsArray();
			for(int i=0;i<misc.length;i++)
			{
				if (misc[i].getKey().equals("STORE_VALUE_CARD"))
				{
					miscItemTemplate=misc[i];
					break;
				}
			}
			sAction_global = sAction;
			vecMenus.add(sAction);

			try {
				new CMSStoreValueCard("STORE_VALUE_CARD").isValidForIssue(iAppletMode);
				String Builder = CMSPaymentMgr.getPaymentBuilder("STORE_VALUE_CARD"); // retrieve the credit card builder
				theAppMgr.addStateObject("ARM_STORE_VALUE_ID", miscItemTemplate);
				theAppMgr.buildObject("SPECIFIC", Builder, "RELOAD"); // invoke the builder
				anEvent.consume();
			} catch (BusinessRuleException ex1) {
				theAppMgr.showErrorDlg(ex1.getMessage());
			}

		}
		//End: Added by Himani for Reload GC on 19-SEP-2016
		//Start : Added by Himani for GC Integration
		else if (sAction.equals("ADD_NEW_GIFTCARD"))
		{
			MiscItemTemplate miscItemTemplate=null;
			MiscItemTemplate[] misc = MiscItemManager.getInstance().getMiscItemsArray();
			for(int i=0;i<misc.length;i++)
			{
				if (misc[i].getKey().equals("STORE_VALUE_CARD"))
				{
					miscItemTemplate=misc[i];
					break;
				}
			}
			sAction_global = sAction;
			vecMenus.add(sAction);

			try {
				new CMSStoreValueCard("STORE_VALUE_CARD").isValidForIssue(iAppletMode);
				String Builder = CMSPaymentMgr.getPaymentBuilder("STORE_VALUE_CARD"); // retrieve the credit card builder
				theAppMgr.addStateObject("ARM_STORE_VALUE_ID", miscItemTemplate);
				theAppMgr.buildObject("SPECIFIC", Builder, "CREATE"); // invoke the builder
				anEvent.consume();
			} catch (BusinessRuleException ex1) {
				theAppMgr.showErrorDlg(ex1.getMessage());
			}

		}
		//End : Added by Himani for GC Integration
		else if (sAction.equals("INQUIRIES")) {
			sAction_global = sAction;
			vecMenus.add(sAction);
			CMSApplet.theAppMgr.showMenu(MenuConst.INQUIRIES, super.theOpr);
			// Issue # 804
			theAppMgr.setSingleEditArea(res.getString("Select option"));
		} else if (sAction.equals("PREV")) {
			goPrevious(anEvent); /*if (vecMenus.size() > 1) {
			 appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0,
			 ( (String) vecMenus.elementAt(vecMenus.size() - 2)),
			 0));
			 vecMenus.removeElementAt(vecMenus.size() - 2);
			 vecMenus.removeElementAt(vecMenus.size() - 1);
			 }
			 else {
			 if (sAction_global != null) {
			 showButtons();
			 enterItem();
			 vecMenus = new Vector();
			 sAction_global = new String();
			 }
			 else {
			 theAppMgr.goBack();
			 }
			 }*/

		}
		//    else if (sAction.equals("DISCOUNT_PERCENTAGE")) {
		//      theAppMgr.showErrorDlg("Development in progress...");
		//    }
		//    else if (sAction.equals("DISCOUNT_AMOUNT")) {
		//      theAppMgr.showErrorDlg("Development in progress...");
		//    }
		//    else if (sAction.equals("DISCOUNT_PRICE")) {
		//      theAppMgr.showErrorDlg("Development in progress...");
		//    }
		else if (sAction.equals("POST_AND_PACK")) {
			anEvent.consume();
			postAndPackButton();
		} else if (sAction.equals("TAX_EXEMPT")) {
			if (((CMSStore) theStore).usesRegionalTaxCalculations()) {
				//showTaxButtons("TAX_EXEMPT", true);
			} else {
				if (theTxn.getCustomer() != null) {
					buildTaxExemptId("SALE_TAX");
					vecMenus.add(sAction);
					CMSApplet.theAppMgr.showMenu(MenuConst.TAX_EXEMPT, "TAX_EXEMPT", super.theOpr);
				} else {
					theAppMgr.showErrorDlg(res.getString("Please select a customer to continue"));
				}
			}
		} else if (sAction.equals("REMOVE_TAX_EXEMPT")) {
			if (((CMSStore) theStore).usesRegionalTaxCalculations()) {
				// showTaxButtons("REMOVE_TAX_EXEMPT", true);
			} else {
				objectEvent("TAX_EXEMPT", "");
			}
		} else if (sAction.equals("RESTORE_TAX")) {
			//      if ( ( (CMSStore) theStore).usesRegionalTaxCalculations()) {
			//        showTaxButtons(sAction, false);
			//     }
			//      else {
			clearLineItemTax(false);
			updateLabel();
			//      }
		} else if (sAction.equals("MODIFY_TAX")) {
			if (((CMSStore) theStore).usesRegionalTaxCalculations()) {
				showTaxButtons(sAction, false);
			} else {
				if (pnlDetailLineItem.isRowSelected()) {
					theAppMgr.setSingleEditArea(res.getString("Enter the manual tax amount."), "MODIFY_TAX", theAppMgr.CURRENCY_MASK);
				} else {
					theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
					theAppMgr.setEditAreaFocus();
				}
			}
		} else if (sAction.equals("MODIFY_TAX_PERCENT")) {
			if (((CMSStore) theStore).usesRegionalTaxCalculations()) {
				showTaxButtons(sAction, false);
			} else {
				if (pnlDetailLineItem.isRowSelected()) {
					theAppMgr.setSingleEditArea(res.getString("Enter the manual tax percent."), "MODIFY_TAX_PERCENT", theAppMgr.DOUBLE_MASK);
				} else {
					theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
					theAppMgr.setEditAreaFocus();
				}
			}
		} else if (sAction.equals("MARKDOWNS")) {
			theAppMgr.setSingleEditArea(res.getString("Select option."));
			theAppMgr.showMenu(MenuConst.MARKDOWN_MENU, "MARKDOWNS", theOpr, theAppMgr.PREV_BUTTON);
		} else if (sAction.equals("DISCOUNT")) {
			String[] dis = DiscountMgr.getDiscountTypes();
			JButton[] btns = new JButton[dis.length + 2];
			for (int x = 0; x < dis.length; x++) {
				String label = DiscountMgr.getLabel(dis[x]);
				btns[x] = new JButton(label);
				btns[x].setActionCommand(dis[x]);
			}
			Theme theme = theAppMgr.getTheme();
			btns[dis.length] = new JButton();
			btns[dis.length].setText(HTML.formatLabeltoHTML(res.getString("Remove Discount"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[dis.length].setActionCommand("REMOVE");
			btns[dis.length + 1] = new JButton(res.getString("Previous"));
			btns[dis.length + 1].setActionCommand("BACK");
			theAppMgr.setButtons("DISCOUNT", btns);
			selectDiscount();
		} else if (sAction.equals("RETURN_ITEM")) {
			//Khyati: Commented to allow multiple return transaction in one Return/Sale TXn
			/*      // until Chelsea can handle multiple original txns on a return
			 if (theTxn.getReturnLineItemsArray().length > 0) {
			 theAppMgr.showErrorDlg(res.getString(
			 "Additional sales must be returned under a separate transaction."));
			 anEvent.consume();
			 }
			 }*/
			//End Khyati
			theAppMgr.addStateObject("TXN_MODE", new Integer(RETURN_MODE));
			totalPressed = true;
			//enterOperator();  asks for mgr id
		} else if (sAction.equals("LAYAWAY")) {
			if (convertSalesToLayaway()) {
				initLayawayButtons();
			}
			updateLabel();
			enterItem();
		} else if (sAction.equals("CHANGE_DEPOSIT")) {
			enterDesiredAmount();
			((CMSActionEvent) anEvent).consume();
		} else if (sAction.equals("SALE")) {
			if (isLayawayMode()) {
				convertLayawayToSales();
			}
			updateLabel();
			initSaleButtons();
			enterItem();
		} else if (sAction.equals("SUSPEND_TXN")) {
			try {
				theTxn.testIsSuspendable();
				theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "SUSPEND_TXN", theOpr);
				theAppMgr.setSingleEditArea(res.getString("Enter Suspend transcation comments."), "SUSPEND_TXN_COMMENT");
			} catch (BusinessRuleException bre) {
				theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
				enterItem();
			}
		} else if (sAction.equals("CANCEL_ACTION")) {
			//VM: Cancel Action is to be used to Reset the Input Area Prompt back to "Scan/Enter Item"
			//isGoHomeAllowed = true;
			//theAppMgr.fireButtonEvent("CANCEL_TXN");
			showButtons();
			enterItem();
		} else if (sAction.equals("CHANGE_ASSOCIATE")) {
			totalPressed = true;
			theAppMgr.addStateObject("CONSULTANT", theTxn.getConsultant());
		} else if (sAction.equals("ADD_CUSTOMER")) {
			totalPressed = true;
			if (theTxn.getCustomer() == null) {
				isCustomerAdded = true;
			} else {
				isCustomerAdded = false;
			}
			//      theAppMgr.addStateObject("TXN_CUSTOMER",
			//                               theTxn.getAppModel(CMSAppModelFactory.
			//                                                  getInstance(),
			//                                                  theAppMgr).getCustomer());
		} else if (sAction.equals("TOTAL")) {
			// if in return iAppletMode, add state flag so that if this applet will show
			// up in return mode, if previous is hit on payment applet
			if (iAppletMode == RETURN_MODE) {
				theAppMgr.addStateObject("RETURN_MODE", "");
			}
			totalPressed = true;
		} else if (sAction.equals("TENDER")) {
			if (theAppMgr.getStateObject("ADD_CREDITCARD") != null) {
				String Builder = CMSPaymentMgr.getPaymentBuilder("CUSTOMER_CREDIT_CARD");
				theAppMgr.buildObject("CREDIT_CARD", Builder, "");
				anEvent.consume();
				return;
			}
			//if (((CMSStore) theStore).getPreferredISOCountry().equals("JP")) {
			if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
				if (isCustomerRequiredForDiscounts() || (saleHasAlterationItems() && theTxn.getCustomer() == null) || saleHasRedeemableItems())
					theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
				else
					theAppMgr.addStateObject("ARM_CUST_REQUIRED", "FALSE");
			} else if ((saleHasAlterationItems() && theTxn.getCustomer() == null) || saleHasRedeemableItems() || theTxn.isEmployeeSale) {
				theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
			}
			if (isReservationOpenMode() && theTxn.getReservationTransaction().getDepositAmount() == null) {
				try {
					theTxn.getReservationTransaction().setDepositAmount(new ArmCurrency(0.0d));
				} catch (Exception e) {
					System.out.println(" ** Exception setting Deposit amount");
				}
			}
			totalPressed = true;
		} else if (sAction.equals("CANCEL_TXN")) {
		} else if (sAction.equals("MOD_QTY")) {
			if (!pnlLineItem.isRowSelected()) {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				anEvent.consume();
				return;
			}
			//MSB - To restrict no. of items for Alteration item.
			// Check if line has any alteration item.
			if (!isModifyQtyAllowed()) {
				// -MSB 02/01/06
				// Error dialogue moved to the method.
				//        theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for alteration items"));
				anEvent.consume();
				enterItem();
				return;
			}
			if (iAppletMode != CONSIGNMENT_OPEN && iAppletMode != PRE_SALE_OPEN && iAppletMode != RESERVATIONS_OPEN) {
				if (pnlLineItem.isRowSelected()) {
					theAppMgr.setSingleEditArea(res.getString("Enter the new quantity for selected item."), "QUANTITY", theAppMgr.INTEGER_MASK);
				} else {
					theAppMgr.setEditAreaFocus();
				}
			} else {
				theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for this type of transaction"));
				enterItem();
				return;
			}
		} else if (sAction.equals("PRICE_OVERRIDE") || (sAction.equals("PRICING_PRICE_OVERRIDE"))) {
			if (pnlLineItem.isRowSelected()) {
				theAppMgr.setSingleEditArea(res.getString("Enter the new unit price for selected item."), "PRICE_OVERRIDE", theAppMgr.CURRENCY_MASK);
			} else {
				theAppMgr.setEditAreaFocus();
			}
		} else if (sAction.equals("MANUAL_UNIT_PRICE")) {
			if (pnlLineItem.isRowSelected()) {
				theAppMgr.setSingleEditArea(res.getString("Enter the new unit price for selected item."), "MODIFY_UNIT_PRICE", theAppMgr.CURRENCY_MASK);
			} else {
				theAppMgr.setEditAreaFocus();
			}
		} else if (sAction.equals("DEL_ITEM")) {
			deleteItem();
		} else if (sAction.equals("ITEM_LOOKUP")) {
			//      System.out.println("Item lookup");
			enterItem();
			//            enterDivision();
		} else if (sAction.equals("ITEM_DETAIL")) { //SHOW ITEM DETAILS (CONSULTMAT INFO, TAX INFO)
			try {
				TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore) theTxn.getStore(), (CMSStore) theTxn.getStore(), theTxn.getProcessDate());
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
			//MSB - 01/13/2005
			//Needs to add action into vector
			//to ensure flow.
			sAction_global = sAction;
			vecMenus.add(sAction);
			populateLineItemDetails();
		} else if (sAction.equals("RETURN_TRANS")) {

			cardLayout.show(cardPanel, "LINE_ITEM");
			vecMenus.removeAllElements();
			this.repaint();
			showButtons();
			enterItem();
		} else if (sAction.equals("RESTORE_CONS")) {
			if (pnlDetailLineItem.isRowSelected()) {
				if (pnlDetailLineItem.getSelectedLineItem().getLineItem() instanceof ReturnLineItem) {
					theAppMgr.showErrorDlg(res.getString("The consultant on a return line item cannot be modified."));
					theAppMgr.setEditAreaFocus();
					return;
				}
				try {
					pnlDetailLineItem.removeSelectedLineConsultant(); //reset additional consultant to null
					theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
					theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
					setLineConsultant(pnlDetailLineItem.getSelectedLineItem().getLineItem());
					return;
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					return;
				}
			} else {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				theAppMgr.setEditAreaFocus();
			}
		} else if (sAction.equals("ITEM_DETAILS")) {
			//
			if (pnlLineItem.getSelectedLineItem() == null) {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				return;
			}
			theAppMgr.addStateObject("InitialSale_POSLineItem", pnlLineItem.getSelectedLineItem());
			theAppMgr.setSingleEditArea(res.getString("Select Option"));
		} else if (sAction.equals("EMPLOYEE_SALE")) {
			//          theAppMgr.setSingleEditArea(res.getString("Enter employee ID"),
			//                                      "EMPLOYEE_SALE", theAppMgr.NO_MASK);
			//Commented By - MSB (moved code into invokeEmployeeSale() method
			//          String bldrName = CMSDiscountMgr.getBuilderName("EMPLOYEE");
			//          theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
			invokeEmployeeSale();
		} else if (sAction.equals("CHANGE_LINE_CONS")) {
			if (pnlDetailLineItem.isRowSelected()) {
				if (pnlDetailLineItem.getSelectedLineItem().getLineItem() instanceof ReturnLineItem) {
					theAppMgr.showErrorDlg(res.getString("The consultant on a return line item cannot be modified."));
					theAppMgr.setEditAreaFocus();
					return;
				}
				theAppMgr.setSingleEditArea(res.getString("Enter the user name of the new consultant."), "CONSULTANT_LINE_ID");
			} else {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				theAppMgr.setEditAreaFocus();
			}
		} else if (sAction.equals("SERVICES")) {
			MiscItemTemplate miscItemTemplate = null;
			if (miscItemTemplate == null)
				miscItemTemplate = solicitMiscItemTemplateChoice();
			buildMiscItem(miscItemTemplate);
		} else if (sAction.equals("SEND_SALE")) {
			// remember add resource...
			if (iAppletMode == SALE_MODE) {
				if ((!theTxn.getSaleLineItems().hasMoreElements())) {
					theAppMgr.showErrorDlg(res.getString("You must add items to the sale before proceeding to entering shipping information"));
					anEvent.consume();
				}
			} else if (iAppletMode == this.CONSIGNMENT_OPEN) {
				if ((!theTxn.getConsignmentLineItems().hasMoreElements())) {
					theAppMgr.showErrorDlg(res.getString("You must add items to the sale before proceeding to entering shipping information"));
					anEvent.consume();
				}
			}
			GenericChooserRow[] shippingRequests = getShippingRequests();
			theAppMgr.addStateObject("TXN", theTxn);
			if (shippingRequests.length > 1) {
				GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, shippingRequests, new String[] { res.getString("First Name"),
					res.getString("Last Name"), res.getString("Street Address") });
				dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(dlg.getCenterRenderer());
				dlg.getTable().getColumnModel().getColumn(1).setCellRenderer(dlg.getCenterRenderer());
				dlg.getTable().getColumnModel().getColumn(2).setCellRenderer(dlg.getCenterRenderer());
				dlg.setVisible(true);
				if (dlg.isOK()) {
					if (dlg.getSelectedRow().getRowKeyData() != null) {
						theAppMgr.addStateObject("SHIPPING_REQUEST", dlg.getSelectedRow().getRowKeyData());
					}
				} else {
					anEvent.consume();
				}
			}
			totalPressed = true;
		}
		//For testing issue
		//else if (sAction.equals("WRITE_TXN")){
		//	try{
		//		theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		//		ObjectStore  objStore = new ObjectStore(".\\txns\\reservation_"+theTxn.getId());
		//		System.out.println("TheTxn object before writting ---->"+theTxn.getId());
		//		if(theTxn!=null)
		//		objStore.write(theTxn);
		//	}catch(Exception e){
		//		e.printStackTrace();
		//	}
		//
		//}

		//Started code for Dolci PCR
		else if(sAction.equals("DOLCI")){
			String[] buttons = CMSItemMgr.getCandyNames();
		JButton[] btns = new JButton[CMSItemMgr.getCandyNames().length];
			for (int x = 0; x < buttons.length; x++) {
				CharSequence chSequence ="_";
				if(buttons[x].contains(chSequence)){
					btns[x] = new JButton(buttons[x]);
					int index = buttons[x].indexOf("_");
					String firstIndex =  buttons[x].substring(0,index);
					String lastIndex =  buttons[x].substring(index+1);
					String upperCase = firstIndex.substring(0,1).toUpperCase();
					String lowerCase = firstIndex.substring(1).toLowerCase();
					String addCase = upperCase+lowerCase;
					String upperCases = lastIndex.substring(0,1).toUpperCase();
					String lowerCases = lastIndex.substring(1).toLowerCase();
					String addCases = upperCases+lowerCases;
					btns[x].setText(addCase+" "+addCases);
					btns[x].setActionCommand((String) buttons[x]);
				}else{
					btns[x] = new JButton(buttons[x]);
					String last = buttons[x].substring(1).toLowerCase();
					String first =buttons[x].substring(0,1);
					btns[x].setText(first+last);
					btns[x].setActionCommand((String) buttons[x]);

				}
				/*btns[x] = new JButton(dis[x]);
				btns[x].setText(dis[x].toLowerCase());
				btns[x].setActionCommand((String) dis[x]);*/
			}
			Theme theme = theAppMgr.getTheme();
			theAppMgr.setButtons("DOLCI", btns);
		}
		//Ended code for Dolci PCR
	}


	private boolean sendItemMessageData(POSLineItem line,POSLineItem[] lineItemArray ,boolean Refresh, boolean idleMessage, boolean clearMessage,String discountAmt) {
		try {
			String result = "";
			//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
			int qty = theTxn.getLineItemsArray().length;
			//End
			String responseArray[] = null;
			String ajbResponse[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");

				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				if(!storeCountry.equals("CAN")){
					//String tt = orgTxn.getTransactionType();
					responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line,lineItemArray,Refresh,idleMessage,clearMessage,discountAmt,false );
					if(responseArray!=null){
						//End
					int length = responseArray.length;
					for (int i=0; i<length ;i++){
					//Vivek Mishar : Added one codition for blank respose when AJB server gets down in middle of transaction.
					//if(responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers")||responseArray[i].equals(""))){
						//Vivek Mishra : Removed the blak condition as it was causing the All AJB Server down dialogue even in case of server is recover.
						if(responseArray[i] != null && responseArray[0].toString().contains("All the Ajb Servers")){
						//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
						//if(!Refresh)
							//count++;
						/*if(count==qty)
						{*/
							//commenting as the message is not needed in initial sales screen
						//theAppMgr.showErrorDlg("All the AJB servers are down");
						/*count=0;
						}//End
*/						return false;
					}
					}
					}
				}else

				if (responseArray == null) {
					return true;
				}
			//count=0;
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}

  }



	/**
	 * put your documentation comment here
	 */
	void postAndPackButton() {
		if (theTxn.isPostAndPack()) {
			try {
				if (theAppMgr.showOptionDlg(res.getString("Are you Sure?"), res.getString("You have asked to cancel Post and Pack on this sale.  Are you sure you want to do this?"))) {
					theTxn.setPostAndPack(false, new ArmCurrency(0.0));
					updateLabel();
				}
			} catch (BusinessRuleException e) {
				theAppMgr.showErrorDlg(e.getMessage());
			}
		} else {
			theAppMgr.setSingleEditArea(res.getString("Enter the amount to charge for Post and Pack"), "POST_AND_PACK_CHARGE", theAppMgr.CURRENCY_MASK);
		}
	}

	/**
	 * callback when <b>Page Down</b> icon is clicked
	 */
	public void pageDown(MouseEvent e) {
		Component panel = cardLayout.getCurrent(cardPanel);
		if (pnlLineItem.getClass().isInstance(panel)) {
			pnlLineItem.nextPage();
		} else if (pnlDetailLineItem.getClass().isInstance(panel)) {
			pnlDetailLineItem.nextPage();
		} else if (pnlLineItemMulti.getClass().isInstance(panel)) {
			pnlLineItemMulti.nextPage();
		}
		theAppMgr.showPageNumber(e, ((PageNumberGetter) panel).getCurrentPageNumber() + 1, ((PageNumberGetter) panel).getTotalPages());
	}

	/**
	 * callback when <b>Page Up</b> icon is clicked
	 */
	public void pageUp(MouseEvent e) {
		Component panel = cardLayout.getCurrent(cardPanel);
		if (pnlLineItem.getClass().isInstance(panel)) {
			pnlLineItem.prevPage();
		} else if (pnlDetailLineItem.getClass().isInstance(panel)) {
			pnlDetailLineItem.prevPage();
		} else if (pnlLineItemMulti.getClass().isInstance(panel)) {
			pnlLineItemMulti.prevPage();
		}
		theAppMgr.showPageNumber(e, ((PageNumberGetter) panel).getCurrentPageNumber() + 1, ((PageNumberGetter) panel).getTotalPages());
	}

	/**
	 * @return
	 */
	public boolean isHomeAllowed() {
		if (isGoHomeAllowed) {
			return (true);
		}
		return (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?")));
	}

	/**
	 *
	 * @return
	 */
	public CompositePOSTransaction getTheTxn() {
		return (theTxn);
	}

	/**
	 * Static name of methods that can be triggered after a BussinessRuleException
	 */
	private static final String ENTER_ITEM = "enterItem";
	private static final String ENTER_MARKDOWN_AMOUNT = "enterMarkdownAmount";
	private static final String ENTER_MARKDOWN_PERCENT = "enterMarkdownPercent";
	private static final String SELECT_LINE_ITEM = "selectLineItem";

	/**
	 * put your documentation comment here
	 */
	public void enterAlterationID() {
		theAppMgr.setSingleEditArea(res.getString("Scan or enter Alteration ID"), "ALTERATION_ID", theAppMgr.NO_MASK);
	}

	/**
	 * Method to trigger <code>Item</code> builder.
	 */
	public void enterItem() {
		//MP: Changed the string for loyalty card swipe
		ConfigMgr loyaltyCfg = new ConfigMgr("loyalty.cfg");
		String rewardTypes = loyaltyCfg.getString("LOYALTY_CARDS");
		boolean storePremio = false;
		String brandID = ((CMSStore) theTxn.getStore()).getBrandID();
		for (StringTokenizer tokenizer = new StringTokenizer(rewardTypes, ","); tokenizer.hasMoreElements();) {
			String rewardType = (String) tokenizer.nextElement();
			String storeType = loyaltyCfg.getString(rewardType + ".TYPE");
			if (storeType != null && brandID != null && storeType.equalsIgnoreCase(brandID)) {
				storePremio = true;
				break;
			}
		}
		if (storePremio) {
			theAppMgr.setSingleEditArea(res.getString("Scan/Enter barcode or enter 'P' to swipe Premio card"), "ITEM", theAppMgr.ITEM_MASK);
		} else {
			theAppMgr.setSingleEditArea(res.getString("Scan/Enter barcode or enter 'S' to search"), "ITEM", theAppMgr.ITEM_MASK);
		}
	}

	/**
	 *
	 */
	public void enterMarkdownAmount() {
		theAppMgr.setSingleEditArea(res.getString("Enter the amount to markdown the selected item."), "MARKDOWN_AMT", theAppMgr.CURRENCY_MASK);
	}

	/**
	 *
	 */
	public void enterMarkdownPercent() {
		theAppMgr.setSingleEditArea(res.getString("Enter the percentage to markdown the selected item."), "MARKDOWN_PCT", theAppMgr.INTEGER_MASK);
	}

	/**
	 *
	 */
	public void selectLineItem() {
		theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
	}

	////////////////////////////////////////////////////////////
	//   Private Methods
	////////////////////////////////////////////////////////////
	/**
	 */
	private void updateLabel() {
		// Bawa Manpreet Singh 2/07/05
		// Invoke Tax Engine to Total and subtotal
		String sTotalAmount = null;
		POSLineItem[] lineItem = theTxn.getLineItemsArray();
		try {
			//        if(theTxn.getTaxExemptId() == null || theTxn.getTaxExemptId().length() == 0){
			TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore) theTxn.getStore(), (CMSStore) theTxn.getStore(), theTxn.getProcessDate());
			//        }
		} catch (Exception ex) {
			System.out.println("Exceptioin applying tax ");
			ex.printStackTrace();
		}
//Anjana : Added to show actaul due amount while PRSSALE OPEN
		if(iAppletMode == this.PRE_SALE_OPEN){
						sTotalAmount = theTxn.getCompositeNetAmount().formattedStringValue() + " / " + theTxn.getPresaleCompositeTotalAmountDue().formattedStringValue();

		}else{

		sTotalAmount = theTxn.getCompositeNetAmount().formattedStringValue() + " / " + theTxn.getCompositeTotalAmountDue().formattedStringValue();
		}

		//Vivek Mishra : Added to make a single pont of request for 150 Item Bucket request
		if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
			if(lineItem.length==0)
			sendItemMessageData(null, null,true,false,true,"");
		if(lineItem.length>0){

		sendItemMessageData(lineItem[0] ,lineItem,true,false,false,"");
		}
		}
		//Ends
		for(int i= 0; i<lineItem.length;i++){

		//Initiates a 150 request to Verifone on Item Look Up
		//System.out.println(theTxn.getLineItemsArray().length);
		//Vivek Mishra : Changed the condition for sending *POSITEMS AJB request for reservation, presale and consignment.
		//if(theTxn.getCompositeTotalQuantityOfItems()>0){
		//Vivek Mishra : Commented to restrict sending duplicate AJB 150 request.
		/*if(lineItem.length>0){
			//Vivek Mishra : Changed the parameter sequesnce as per the sendItemMessageData method
			//boolean toVerifone = sendItemMessageData(lineItem[i] ,Refresh, deleteFlag);
			boolean toVerifone = sendItemMessageData(lineItem[i] , deleteFlag, Refresh);
			//End
	    }*/
		}
		if (sTotalAmount != null) {
			fldSubTotal.setText(sTotalAmount);
		}
		fldTotalUnits.setText(res.getString("Total Items") + " " + theTxn.getCompositeTotalQuantityOfItems());
		if (theTxn.isPostAndPack()) {
			fldDeposit.setText(theTxn.getPostAndPackChargeAmount().formattedStringValue());
			labDeposit.setText(res.getString("Post and Pack Fee"));
			labDeposit.setVisible(true);
			fldDeposit.setVisible(true);
		} else if (iAppletMode == this.LAYAWAY_MODE) {
			fldDeposit.setText(theTxn.getCompositeTotalAmountDue().formattedStringValue());
			labDeposit.setText(res.getString("Deposit"));
			labDeposit.setVisible(true);
			fldDeposit.setVisible(true);
			lblEMployeeSale.setVisible(false);
		} else if (iAppletMode == this.RESERVATIONS_OPEN) {
			labDeposit.setText(res.getString("Deposit"));
			labDeposit.setVisible(true);
			if (bUpdateDepositAmount) {
				double newDeposit = theTxn.getCompositeNetAmount().doubleValue() * (dReservationDepositPercent / 100);
				fldDeposit.setText(new ArmCurrency(newDeposit).formattedStringValue());
			}
			try {
				POSLineItem[] line = theTxn.getDepositLineItems();
				ArmCurrency totalDeposits = new ArmCurrency(0);
				for (int i = 0; i < line.length; i++) {
					totalDeposits = totalDeposits.add(line[i].getNetAmount());
				}
				fldDeposit.setText(totalDeposits.formattedStringValue());
				if (fldDeposit.getText() != null && fldDeposit.getText().trim().length() != 0) {
					ArmCurrency totalSale = new ArmCurrency(0.0);
					POSLineItem[] posLineItem = theTxn.getLineItemsArray();
					for (int i = 0; i < posLineItem.length; i++) {
						if (posLineItem[i].isMiscItem()) {
							totalSale = totalSale.add(posLineItem[i].getTotalAmountDue());
						}
					}
					ArmCurrency sTot = theTxn.getCompositeNetAmount().subtract(new ArmCurrency(fldDeposit.getText()));
					sTotalAmount = sTot.formattedStringValue() + " / " + totalSale.formattedStringValue();
					fldSubTotal.setText(sTotalAmount);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			fldDeposit.setVisible(true);
		} else {
			labDeposit.setVisible(false);
			fldDeposit.setVisible(false);
		}
		// make call to display manager
		DisplayManager dm = (DisplayManager) theAppMgr.getGlobalObject("DISPLAY_MANAGER");
		if (dm != null) {
			dm.updateTransaction(theTxn);
		}
		if (theTxn.getCustomer() != null) {
			pnlHeader.updateAmtToReward();
		}
		updateMerchandiseCountLabel();
		//Initiates a 150 Refresh Request at the end of updateLabel
		//Vivek Mishra : Commented for not sending AJB 150 refresh message from update label method
		/*Refresh= true;
		for(int i= 0; i<lineItem.length;i++){
			sendItemMessageData(lineItem[i] ,deleteFlag, Refresh);
	}
		//Vivek Mishra : Added code to make refresh flag false after sending refresh message.
		Refresh= false;	*/
		//End
	}

	/**
	 * @param dis
	 */
	private void updateDiscountLabel() {
		// SP: commented the following code to not display the reason code
		// on the applet
		/*Discount[] discounts = theTxn.getDiscountsArray();
		 if (discounts != null && discounts.length > 0) {
		 Discount dis = discounts[discounts.length - 1];
		 StringBuffer buf = new StringBuffer("");
		 //      buf.append(CMSDiscountMgr.getLabel(dis.getType()) + " - ");
		 buf.append(dis.getType() + " - ");
		 if (dis.isDiscountPercent()) {
		 //double percent = dis.getPercent() * 100;
		 //buf.append(percent + "%");
		 NumberFormat percentFormat = NumberFormat.getPercentInstance();
		 percentFormat.setMinimumFractionDigits(2);
		 buf.append(percentFormat.format(dis.getPercent()));
		 }
		 else {
		 buf.append(dis.getAmount().formattedStringValue());
		 }
		 labDiscount.setText(buf.toString());
		 }
		 else {
		 labDiscount.setText("");
		 }*/
	}

	/**
	 */
	private void deleteItem() {
		deleteFlag =true;
		POSLineItem theLine = null;
		POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
		if (pnlLineItem.isRowSelected()) {
			theLine = pnlLineItem.getSelectedLineItem();
			try {
				pnlLineItem.getTable().getSelectionModel().removeListSelectionListener(selectionResetListener);
				if ((isReservationCloseMode()) && lineItmDeposit == pnlLineItem.getSelectedLineItem()) {
					miscDepositItem = null;
					amtRSVODeposit = null;
					bUpdateDepositAmount = false;
					lineItmDeposit = null;
					theAppMgr.removeStateObject("ARM_RSV_DEPOSIT");
				} else if (lineItmDeposit == pnlLineItem.getSelectedLineItem()) {
					miscDepositItem = null;
					amtRSVODeposit = null;
					lineItmDeposit = null;
					//TD
					theTxn.getReservationTransaction().setDepositAmount(new ArmCurrency(0.0d));
					fldDeposit.setText("");
				}
				//Ks: Set the selected returnItem to false to enable the item to be selected again in the same transaction
				if (theLine instanceof CMSReturnLineItem) {
					//SP: added code to check if there is a salelineitem associated with the return line item
					// as there might not be one in case of return w/o receipt
					if (((CMSReturnLineItem) theLine).getSaleLineItem() != null) {
						((CMSSaleLineItem) ((CMSReturnLineItem) theLine).getSaleLineItem()).setIsSelectedForReturn(false);
					}
				}
				pnlLineItem.deleteSelectedLineItem();

				pnlLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
				//commented for issue #1947 light pole display by Neeti
				//LightPoleDisplay.getInstance().itemReturned(theLine, theLine.getExtendedRetailAmount().formattedStringValue(),theTxn.getCompositeTotalAmountDue().formattedStringValue());

				//start code for issue #1947 light pole display by Neeti
				LightPoleDisplay.getInstance().itemReturnedForDiscount(theLine, theLine.getExtendedRetailAmount().formattedStringValue(),theLine.getTotalAmountDue().formattedStringValue());
				//End code for issue #1947 light pole display by Neeti
				suggestDlg.clearSuggestions();
				suggestDlg.setVisible(false);
				//remove postandpack
				theTxn.setPostAndPack(false, new ArmCurrency(0.0));
				// Update to set the loyalty points again
				if (theTxn.getCustomer() != null) {
					pnlHeader.updateAmtToReward();
				}
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
		}
		//Initiates a 150 request while deleteing an item
		//sendItemMessageData(theLine,lineItemArray, deleteFlag, Refresh, false,false,"");

		//Vivek Mishra : Added code to make deleteFlag false after sending the delete message.
		deleteFlag=false;
		//End

		//Vivek Mishra : Added code for sending the refresh request after clearing the item as in updateLable the refresh call is based on number of items.
		/*Refresh = true;
		sendItemMessageData(theLine, deleteFlag, Refresh);
		Refresh = false;*/
		//End

		updateLabel();
		enterItem();
	}

	/**
	 */
	private void addLineItem(POSLineItem lineItem) {
		POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
		// update table - it is important to be quick, so remove selection listener
		pnlLineItem.getTable().getSelectionModel().removeListSelectionListener(selectionResetListener);
		setLineConsultant(lineItem);
		pnlLineItem.addLineItem(lineItem);
		pnlLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
		//update light pole
		//commented for issue #1947 Light Pole Display by Neeti
		//LightPoleDisplay.getInstance().itemSold(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),theTxn.getCompositeTotalAmountDue().formattedStringValue());

		//Start code for Issue #1947 Light Pole Display by Neeti
		LightPoleDisplay.getInstance().itemSoldForDiscount(lineItem, lineItem.getExtendedRetailAmount().formattedStringValue(),lineItem.getTotalAmountDue().formattedStringValue());
		//Ended code for #issue 1947 Light Ple Display by Neeti
		//Initiates a 150 request while deleteing an item

		//Vivek Mishra : Added a AJB 150 request sent after adding every new item
		//sendItemMessageData(lineItem,lineItemArray, deleteFlag, Refresh,false,false,"");
		//End
	}

	// -- Manpreet S Bawa -- 02/02/2005
	// -- Sets consultant for for current line item.
	// Retrieve current consultant from Transaction.
	// If it is null, set consultant to current operator.
	private void setLineConsultant(POSLineItem line) {
		CMSEmployee currentConsultant = (CMSEmployee) theTxn.getConsultant();
		if (currentConsultant == null) {
			currentConsultant = (CMSEmployee) theOpr; // Default operator
		}
		try {
			if (line.getAdditionalConsultant() == null) { // If its not already been set for current line
				line.setAdditionalConsultant(currentConsultant);
			}
		} catch (BusinessRuleException bre) {
			bre.printStackTrace();
		}
	}

	/**
	 * @param itemCode
	 */
	private void alterationItem(String itemCode) {
		if (iAppletMode != SALE_MODE) {
			return;
		}
		editAreaEvent("ENTER_ITEM", itemCode);
	}

	/**
	 *
	 */
	private void checkForPriceOverrides() {
		overRideHelper = new PriceOverrideHelper();
		solicitOverrideReason = overRideHelper.isSolicitReasons();
		if (solicitOverrideReason) {
			String[] titles = { res.getString("Valid Reason for Price Override") };
			overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, overRideHelper.getTabelData(), titles);
		}
	}

	/**
	 * convert all layaways to sales.. any non sales line items are not converted
	 */
	private void convertLayawayToSales() {
		if (theTxn.getSaleLineItemsArray().length > 0 || theTxn.getReturnLineItemsArray().length > 0) {
			theAppMgr.showErrorDlg(res.getString("All items must be layaway items."));
			theAppMgr.setEditAreaFocus();
			return;
		}
		try {
			//POSLineItem[] lineItems = theTxn.getLayawayLineItemsArray();
			//for(int i = 0; i < lineItems.length; i++)
			//    ((LayawayLineItem)lineItems[i]).makeSaleLineItem();
			theTxn.getLayawayTransaction().makeSaleLineItems();
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
		}
		populateLineItems();
	}

	/**
	 * convert all sales to layaway.. any non sales line items are
	 * not converted
	 */
	private boolean convertSalesToLayaway() {
		// check for returns. if present, do not allow return
		boolean isReturn = false;
		if (theTxn.getReturnLineItems().hasMoreElements()) {
			isReturn = true;
		}
		if (isReturn) {
			theAppMgr.showErrorDlg(res.getString("Unable to convert return into a layaway."));
			theAppMgr.setEditAreaFocus();
			return (false);
		}
		try {
			//POSLineItem[] lineItems = theTxn.getSaleLineItemsArray();
			//for(int i = 0; i < lineItems.length; i++)
			//    ((SaleLineItem)lineItems[i]).makeLayawayLineItem();
			theTxn.getSaleTransaction().makeLayawayLineItems();
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			return (false);
		}
		populateLineItems();
		return (true);
	}

	/**
	 *
	 * @return
	 */
	private GenericChooserRow[] getShippingRequests() {
		ShippingRequest[] shippingRequests = theTxn.getShippingRequestsArray() == null ? new ShippingRequest[0] : theTxn.getShippingRequestsArray();
		GenericChooserRow[] chooseShippingRequest = new GenericChooserRow[shippingRequests.length + 1];
		int i = 0;
		for (; i < shippingRequests.length; i++) {
			chooseShippingRequest[i] = new GenericChooserRow(new String[] { shippingRequests[i].getFirstName(), shippingRequests[i].getLastName(), shippingRequests[i].getAddress() },
					shippingRequests[i]);
		}
		chooseShippingRequest[i] = new GenericChooserRow(new String[] { "", res.getString("NEW SHIPPING REQUEST"), "" }, null);
		return (chooseShippingRequest);
	}

	/**
	 * Load item detail and model
	 */
	private void populateLineItemDetails() {
		//         System.out.println("lineitemdetails****");
		pnlDetailLineItem.clear();
		POSLineItem[] lineItems;
		if (iAppletMode == PRE_SALE_OPEN) {
			lineItems = theTxn.getPresaleLineItemsArray();
		} else if (iAppletMode == RESERVATIONS_OPEN) {
			lineItems = theTxn.getSaleAndReservationLineItemArray();
		} else if (iAppletMode == CONSIGNMENT_OPEN) {
			lineItems = theTxn.getConsignmentLineItemsArray();
		} else {
			lineItems = theTxn.getLineItemsArray();
		}
		pnlDetailLineItem.getTable().getSelectionModel().removeListSelectionListener(selectionResetListener);
		for (int i = 0; i < lineItems.length; i++) {
			if (lineItems[i] instanceof CMSVoidLineItem)
				continue;
			pnlDetailLineItem.addLineItem(lineItems[i]);
		}
		if (lineItems.length > 0) {
			pnlDetailLineItem.getModel().firstPage();
			pnlDetailLineItem.selectFirstRow();
		}
		pnlDetailLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
		cardLayout.show(cardPanel, "DETAIL_LINE_ITEM");
		//this.remove(pnlLineItem);
		//this.getContentPane().add(pnlDetailLineItem, BorderLayout.CENTER);
		this.repaint();
		//this.revalidate();
		theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
		theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
	}

	/**
	 */
	private void populateLineItems() {
		pnlLineItem.clear();
		POSLineItem[] lineItems;
		//ks: check for presale and consigmnment type txn
		if (iAppletMode == PRE_SALE_OPEN) {
			lineItems = theTxn.getPresaleLineItemsArray();
		} else if (iAppletMode == RESERVATIONS_OPEN) {
			lineItems = theTxn.getSaleAndReservationLineItemArray();
		} else {
			lineItems = theTxn.getLineItemsArray();
		}
		pnlLineItem.getTable().getSelectionModel().removeListSelectionListener(selectionResetListener);
		for (int i = 0; i < lineItems.length; i++) {
			if (lineItems[i] instanceof CMSVoidLineItem)
				continue;
			// If its Deposit Item.
			if (((CMSItem) lineItems[i].getItem()).isDeposit()) {
				lineItmDeposit = lineItems[i];
				bUpdateDepositAmount = lineItmDeposit.isManualUnitPrice();
			}
			pnlLineItem.addLineItem(lineItems[i]);
			if (lineItems[i].getAdditionalConsultant() == null) {
				setLineConsultant(lineItems[i]);
			}
		}
		if (lineItems.length > 0) {
			pnlLineItem.getModel().firstPage();
			pnlLineItem.selectFirstRow();
		}
		pnlLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
		cardLayout.show(cardPanel, "DETAIL_LINE_ITEM");
		this.repaint();
		updateLabel();
	}

	/**
	 */
	private void enterOperator() {
		theAppMgr.setSingleEditArea(res.getString("Enter Manager's ID."), "MANAGER", theAppMgr.SSN_MASK);
	}

	/**
	 * Moved this prompt to a builder because this applet will never hear when
	 * the Eu presses <Enter> to remove the tax ID.
	 */
	private void buildTaxExemptId(String header) {
		theAppMgr.buildObject(this, header, "com.chelseasystems.cs.swing.builder.TaxIdBldr", null);
	}

	/**
	 */
	private void enterDesiredAmount() {
		theAppMgr.setSingleEditArea(res.getString("Enter desired amount of deposit."), "MODIFY_DEPOSIT", theAppMgr.CURRENCY_MASK);
	}

	/**
	 */
	private void enterControlID() {
		theAppMgr.setSingleEditArea(res.getString("Enter Control Id."), "CONTROL_ID");
	}

	/**
	 */
	private void enterDefaultPrice() {
		theAppMgr.setSingleEditArea(res.getString("Enter Price."), "DEFAULT_PRICE", theAppMgr.CURRENCY_MASK);
	}

	/**
	 */
	private void enterDivision() {
		theAppMgr.setSingleEditArea(res.getString("Enter division of item to lookup."), "ITEM_DIV");
	}

	/**
	 */
	private void enterItemNameSearch() {
		theAppMgr.setSingleEditArea(res.getString("Enter brand name of item to lookup."), "ITEM_NAME");
	}

	/**
	 */
	private void selectDiscount() {
		theAppMgr.setSingleEditArea(res.getString("Select Discount."));
	}

	/**
	 * put your documentation comment here
	 */
	private void selectOption() {
		theAppMgr.setSingleEditArea(res.getString("Select Option"));
	}

	/**
	 * shows the sale or layaway buttons based on the line items
	 */
	private void showButtons() {
		if (isReservationOpenMode()) {
			initReservationsOpenButtons();
		} else if (isPreSaleOpen()) {
			initPreSaleOpenButtons();
		} else if (isConsignmentIn()) {
			initConsignmentInButtons();
		} else if (isLayawayMode()) {
			initLayawayButtons();
		} else if (theAppMgr.getStateObject("RETURN_MODE") != null && theAppMgr.getStateObject("RETURN_TXN_POS") == null) {
			// Straight return with identified transaction
			theAppMgr.removeStateObject("RETURN_MODE");
			initReturnButtons();
			//PM: Issue #1607 - The vector contains the history of the screens browsed which used by the previous button
			//                  Emptying the vector resets the history
			vecMenus.removeAllElements();
		} else if (theAppMgr.getStateObject("RETURN_SALE") != null) { // a misc return through the Ring a Sale
			//theAppMgr.removeStateObject("RETURN_SALE");
			initReturnButtons();
			//PM: Issue #1607 - The vector contains the history of the screens browsed which used by the previous button
			//                  Emptying the vector resets the history
			vecMenus.removeAllElements();
		} else {
			initSaleButtons();
			//PM: Issue #1607 - The vector contains the history of the screens browsed which used by the previous button
			//                  Emptying the vector resets the history
			vecMenus.removeAllElements();
		}
	}

	/**
	 * shows the regional and federal tax related buttons
	 * @param header for menu
	 */
	private void showTaxButtons(String header, boolean showBothButton) {
		Theme theme = theAppMgr.getTheme();
		JButton[] btns = new JButton[showBothButton ? 4 : 3];
		// federal
		btns[0] = new JButton(HTML.formatLabeltoHTML(res.getString(((CMSStore) theStore).getTaxLabel()), theme.getButtonFont(), theme.getButtonTextColor()));
		btns[0].setActionCommand(((CMSStore) theStore).getTaxLabel());
		// regionalx
		btns[1] = new JButton(HTML.formatLabeltoHTML(res.getString(((CMSStore) theStore).getRegionalTaxLabel()), theme.getButtonFont(), theme.getButtonTextColor()));
		btns[1].setActionCommand(((CMSStore) theStore).getRegionalTaxLabel());
		btns[2] = new JButton(HTML.formatLabeltoHTML(res.getString(showBothButton ? "Both" : "Cancel"), theme.getButtonFont(), theme.getButtonTextColor()));
		btns[2].setActionCommand(showBothButton ? "BOTH_TAXES" : "CANCEL");
		if (showBothButton) {
			btns[3] = new JButton(HTML.formatLabeltoHTML(res.getString("Cancel"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[3].setActionCommand("CANCEL");
		}
		theAppMgr.setSingleEditArea(res.getString("Select option."));
		theAppMgr.setButtons(header, btns);
	}

	/**
	 * PreSale Buttons
	 */
	private void initPreSaleOpenButtons() {
		iAppletMode = PRE_SALE_OPEN;
		labDeposit.setVisible(false);
		fldDeposit.setVisible(false);
		lblEMployeeSale.setVisible(false);
		labLayaway.setForeground(new Color(0, 128, 255));
		labLayaway.setText(res.getString("PRE-SALE OPEN"));
		//Vivek Mishra : Changed menu from PRESALE to ADDITEM because as per Edwin POS should show all the discount options while presale also.
		//theAppMgr.showMenu(MenuConst.PRE_SALE_OPEN, theOpr);
		theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
		//Ends
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	/**
	 * PreSale Buttons
	 */
	private void initConsignmentInButtons() {
		iAppletMode = CONSIGNMENT_OPEN;
		labDeposit.setVisible(false);
		fldDeposit.setVisible(false);
		lblEMployeeSale.setVisible(false);
		labLayaway.setForeground(new Color(0, 140, 255));
		labLayaway.setText(res.getString("CONSIGNMENT OPEN"));
		theAppMgr.showMenu(MenuConst.CONSIGNMENTS_IN, theOpr);
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	/**
	 * put your documentation comment here
	 */
	private void initReservationsOpenButtons() {
		iAppletMode = RESERVATIONS_OPEN;
		labDeposit.setText(res.getString("Deposit"));
		labDeposit.setVisible(true);
		lblEMployeeSale.setVisible(false);
		fldDeposit.setVisible(true);
		fldDeposit.invalidate();
		pnlSouth.validate();
		labLayaway.setForeground(new Color(128, 0, 0));
		labLayaway.setText(res.getString("RESERVATION OPEN"));
		labLayaway.repaint();
		theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	/**
	 */
	private void initSaleButtons() { // pos_sale
		//dp: remove state object to come out of the no-receipt return mode
		theAppMgr.removeStateObject("RETURN_SALE");
		iAppletMode = SALE_MODE;
		labDeposit.setVisible(false);
		fldDeposit.setVisible(false);
		labLayaway.setForeground(new Color(0, 0, 175));
		// Set the Foreground & set the lable for showing Employee visible.
		//lblEMployeeSale.setForeground(new Color(0, 0, 175));
		lblEMployeeSale.setVisible(true);
		if (theTxn.isEmployeeSale) {
			CMSEmployee cmsEmp = theTxn.getEmployee();
			labLayaway.setText(res.getString("EMPLOYEE SALE"));
			// Employee ID & Name.
			this.lblEMployeeSale.setText(cmsEmp.getExternalID() + " - " + cmsEmp.getFirstName() + " " + cmsEmp.getLastName());
		} else {
			labLayaway.setText(res.getString("SALE"));
		}
		theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	/**
	 */
	private void initReturnButtons() { // pos_return
		iAppletMode = RETURN_MODE;
		labDeposit.setVisible(false);
		fldDeposit.setVisible(false);
		lblEMployeeSale.setVisible(false);
		labLayaway.setForeground(Color.red);
		labLayaway.setText(res.getString("RETURN"));
		//Ks: There is no separate Return menu.
		theAppMgr.showMenu(MenuConst.ADD_RETURN_MENU, theOpr);
		//        theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
		//    theAppMgr.showMenu(MenuConst.POS_RETURN, theOpr);
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	/**
	 */
	private void initLayawayButtons() { // pos_layaway
		iAppletMode = LAYAWAY_MODE;
		labDeposit.setVisible(true);
		labDeposit.invalidate();
		fldDeposit.setVisible(true);
		lblEMployeeSale.setVisible(false);
		fldDeposit.invalidate();
		pnlSouth.validate();
		labLayaway.setForeground(new Color(0, 132, 121));
		theAppMgr.showMenu(MenuConst.POS_LAYAWAY, theOpr);
		labLayaway.setText(res.getString("LAYAWAY"));
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	/**
	 * initHeader
	 * initializes the OperatorName, ConsultantName
	 * and CustomerName and Phone
	 */
	private void initHeaders() {
		try {
			pnlHeader.setAppMgr(theAppMgr);
			pnlHeader.setProperties((CMSCustomer) theTxn.getCustomer(), (CMSEmployee) theTxn.getTheOperator(), (CMSEmployee) theTxn.getConsultant(), (CMSStore) theStore, theTxn.getTaxExemptId(),
					theTxn.getRegionalTaxExemptId());
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	/**
	 * clear tax on a line item
	 */
	private void clearLineItemTax(boolean isRegional) {
		try {
			if (pnlDetailLineItem.isRowSelected()) {
				pnlDetailLineItem.removeManualTax(isRegional);
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
				updateLabel();
				return; //maintain state and prompt
			} else {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				theAppMgr.setEditAreaFocus();
			}
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			return;
		}
	}

	// isConsignmentIn
	private boolean isConsignmentIn() {
		return (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == CONSIGNMENT_OPEN);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isConsignmentClose() {
		return (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == CONSIGNMENT_CLOSE);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isReservationCloseMode() {
		return (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == RESERVATIONS_CLOSE);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isReservationOpenMode() {
		return (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == RESERVATIONS_OPEN);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isPreSaleOpen() {
		return (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == PRE_SALE_OPEN);
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isPreSaleClose() {
		return (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == PRE_SALE_CLOSE);
	}

	/**
	 * determines if in sale or layaway mode
	 */
	private boolean isLayawayMode() {
		// check for state object when a new txn is started,
	if (theAppMgr.getStateObject("NEW_TXN_POS") != null) {
			theAppMgr.removeStateObject("NEW_TXN_POS");
			return (false);
		}
		if (theAppMgr.getStateObject("RETURN_MODE") != null) {
			return (false); // to fix a bug of layaway then Misc return rrr1
		}
		if ((theTxn.getLayawayLineItemsArray().length > 0) || (iAppletMode == LAYAWAY_MODE)) {
			return (true);
		}
		return (false);
	}

	/**
	 * determines if in return mode
	 */
	private boolean isReturnMode() {
		return (iAppletMode == RETURN_MODE);
	}

	/**
	 * put your documentation comment here
	 * @param amount
	 * @return
	 */
	private boolean isValidDepositAmount(ArmCurrency amount) {
		ArmCurrency amtTransaction = theTxn.getCompositeNetAmount();
		POSLineItem posLineItems[] = theTxn.getLineItemsArray();
		boolean bReturnLineItemsPresent = false;
		try {
			for (int iCtr = 0; iCtr < posLineItems.length; iCtr++) {
				if (posLineItems[iCtr] instanceof CMSReturnLineItem) {
					bReturnLineItemsPresent = true;
					break;
				}
			}
			if (bReturnLineItemsPresent) {
				return !(amount.greaterThan(amtTransaction.add(lineItmDeposit.getExtendedRetailAmount())));
			} else {
				return !(amount.greaterThan(amtTransaction.subtract(lineItmDeposit.getExtendedRetailAmount())));
			}
		} catch (Exception e) {
			System.out.println("*** Exception validating Deposit Amount ***");
		}
		return false;
	}

	/**
	 * Calculate DepositAmount for ReservationClose
	 * @param bUpdateLabels
	 */
	private void calculateDepositAmount(boolean bUpdateLabels) {
		try {
			//      ArmCurrency amtTxn = null;
			ArmCurrency amtDeposit = amtRSVODeposit.absoluteValue();
			// If DepositItem is being created for first time
			if (lineItmDeposit == null) {
				miscDepositItem.setUnitPrice(amtDeposit);
				lineItmDeposit = theTxn.addReturnMiscItem(miscDepositItem);
				addLineItem(lineItmDeposit);
			} else {
				lineItmDeposit.setManualUnitPrice(amtDeposit);
				pnlLineItem.getModel().fireTableDataChanged();
			}
			if (bUpdateLabels)
				updateLabel();
		} catch (Exception e) {
			System.out.println(" ***** Exception calculating Deposit amount ****");
		}
	}

	/**
	 * put your documentation comment here
	 * @param bOpenDeposit
	 */
	private void buildDepositItem(boolean bOpenDeposit) {
		CMSItem cmsItem = null;
		miscDepositItem = null;
		MiscItemTemplate miscDepositTemplate = solicitDepositItemTemplateChoice(bOpenDeposit);
		if (miscDepositTemplate == null) {
			theAppMgr.showErrorDlg(res.getString("No Deposit Items found"));
			return;
		}
		try {
			cmsItem = CMSItemHelper.findByBarCode(theAppMgr, miscDepositTemplate.getBaseItemId(), theStore.getId());
			if (cmsItem == null) {
				theAppMgr.showErrorDlg(res.getString("Item not found"));
				enterItem();
				return;
			}
			cmsItem.setIsDeposit(true);
			miscDepositItem = new CMSMiscItem(miscDepositTemplate.getKey(), cmsItem);
			if (miscDepositItem == null) {
				theAppMgr.showErrorDlg(res.getString("Item not found"));
				enterItem();
				return;
			}
			if (!miscDepositTemplate.getCanOverrideTaxable())
				miscDepositItem.setTaxable(new Boolean(miscDepositTemplate.getTaxable()));
			if (!miscDepositTemplate.getCanOverrideDescription()) {
				String[] descriptions = miscDepositTemplate.getDescription();
				if (descriptions != null && descriptions.length > 0 && miscDepositTemplate.getDescIdx() > -1)
					miscDepositItem.setDescription(descriptions[miscDepositTemplate.getDescIdx()]);
			}
			if (!miscDepositTemplate.getCanOverrideComment())
				miscDepositItem.setComment(miscDepositTemplate.getComment());
			miscDepositItem.setGLAccount(miscDepositTemplate.getGLaccount());
			miscDepositItem.setDefaultQuantity(new Integer(miscDepositTemplate.getDefaultQty()));
		} catch (Exception e) {
			//      e.printStackTrace();
			System.out.println("********** Exception building deposit item -->: " + miscDepositTemplate.getKey());
			miscDepositItem = null;
			return;
		}
		if (bOpenDeposit)
			enterRSVODepositAmount();
	}

	/**
	 * put your documentation comment here
	 * @param bOpenDeposit
	 * @return
	 */
	private MiscItemTemplate solicitDepositItemTemplateChoice(boolean bOpenDeposit) {
		MiscItemTemplate[] misc = MiscItemManager.getInstance().getMiscItemsArray();
		Vector vecMiscItems = new Vector();
		Vector vecDepKeys = new Vector();
		ConfigMgr itmCfg = new ConfigMgr("item.cfg");
		String sDepositType = "OPEN_DEPOSIT_TYPES";
		if (!bOpenDeposit)
			sDepositType = "CLOSE_DEPOSIT_TYPES";
		String sDepositKeys = itmCfg.getString(sDepositType);
		if (sDepositKeys == null)
			return null;
		StringTokenizer sTokens = new StringTokenizer(sDepositKeys, ",");
		while(sTokens.hasMoreTokens())
			vecDepKeys.addElement(sTokens.nextToken());
		for (int iCtr = 0; iCtr < misc.length; iCtr++) {
			if (vecDepKeys.contains(misc[iCtr].getKey()))
				vecMiscItems.add(misc[iCtr]);
		}
		MiscItemTemplate[] miscDepositTemplates = (MiscItemTemplate[]) vecMiscItems.toArray(new MiscItemTemplate[vecMiscItems.size()]);
		if (miscDepositTemplates.length == 1) {
			return miscDepositTemplates[0];
		}
		GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[miscDepositTemplates.length];
		for (int i = 0; i < availMiscItemTemplates.length; i++) {
			availMiscItemTemplates[i] = new GenericChooserRow(new String[] { miscDepositTemplates[i].getMiscItemDescription() }, miscDepositTemplates[i]);
		}
		GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availMiscItemTemplates, new String[] { (res.getString("DEPOSIT")) });
		dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
		dlg.setVisible(true);
		if (dlg.isOK()) {
			return (MiscItemTemplate) ((MiscItemTemplate) dlg.getSelectedRow().getRowKeyData()).clone();
		} else {
			theAppMgr.showErrorDlg(res.getString("You did not select any deposit type"));
			enterItem();
			return null;
		}
	}

	/**
	 *
	 * @return
	 */
	private MiscItemTemplate solicitMiscItemTemplateChoice() {
		MiscItemTemplate[] oldmiscItemTemplates = MiscItemManager.getInstance().getMiscItemsArray();
		ConfigMgr mgr = new ConfigMgr("item.cfg");
		//added by Shushma for Shipping Fed-Ex
		String shipId=mgr.getString("SHIP.BASE_ITEM");
		for(int i=0;i<oldmiscItemTemplates.length;i++){
		//if(oldmiscItemTemplates[i].getBaseItemId().equals("60009997")){
			if(oldmiscItemTemplates[i].getBaseItemId().equalsIgnoreCase(shipId)){
			String str[]={"Shipping Charge"};
			oldmiscItemTemplates[i].setDescription(str);
			oldmiscItemTemplates[i].setMiscItemDescription("Shipping Charge");
			oldmiscItemTemplates[i].setCanOverrideAmount(true);
		}
		}
		//Fed-Ex change close
		//Vishal Yevale 25 Oct 2017: removed 'Gift Card' option from Services button where gc flag = Y 
		//bug #28674
		MiscItemTemplate[] miscItemTemplates = null;
 		if(fipayGiftcardFlag.equalsIgnoreCase("Y")){
 			miscItemTemplates = new MiscItemTemplate[oldmiscItemTemplates.length-1];	
		}else{
			miscItemTemplates = new MiscItemTemplate[oldmiscItemTemplates.length];	
		}
 		//end Vishal Yevale 25 Oct 2017
		String misckeys = mgr.getString("MISC_ITEM_KEYS");
		StringTokenizer strTok = new StringTokenizer(misckeys, ",");
		int k = 0;
		while(strTok.hasMoreTokens()) {
			String misckey = strTok.nextToken();
			//Vishal Yevale 25 Oct 2017: removed 'Gift Card' option from Services button where gc flag = Y 
			//bug #28674
			if(misckey.equalsIgnoreCase("STORE_VALUE_CARD") && fipayGiftcardFlag.equalsIgnoreCase("Y")){
				continue;
			}
			//end Vishal Yevale 25 Oct 2017
			for (int j = 0; j < oldmiscItemTemplates.length; j++) {
				if (oldmiscItemTemplates[j].getKey().equals(misckey))
					miscItemTemplates[k] = oldmiscItemTemplates[j];
			}
			k++;
		}
		if (exceptionbuilder == null)
		{
			GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[miscItemTemplates.length];
			for (int i = 0; i < availMiscItemTemplates.length; i++) {
				availMiscItemTemplates[i] = new GenericChooserRow(new String[] { miscItemTemplates[i].getMiscItemDescription() }, miscItemTemplates[i]);
			}
			GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availMiscItemTemplates, new String[] { (res.getString("SERVICES")) });
			dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
			dlg.setVisible(true);
			if (dlg.isOK()) {
				return (MiscItemTemplate) ((MiscItemTemplate) dlg.getSelectedRow().getRowKeyData()).clone();
			} else {
				theAppMgr.showErrorDlg(res.getString("You did not select a miscellaneous item, item was not added."));
				enterItem();
				return null;
			}
		}
		else
		//code modified by deepika to add the Exception Item in Services Menu.
		//Created new availMiscItemTemplates1 and initialized it with the miscItemTemplates including Exception item template.
		{
			int i;
			GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[miscItemTemplates.length];
			GenericChooserRow[] availMiscItemTemplates1 =  new GenericChooserRow[availMiscItemTemplates.length+1];

			// Changes made by Satin to move "Exception Item" to second option.
			availMiscItemTemplates1[0] = new GenericChooserRow(new String[] { miscItemTemplates[0].getMiscItemDescription() }, miscItemTemplates[0]);
			for ( i=2; i <= availMiscItemTemplates.length; i++) {
				availMiscItemTemplates1[i] = new GenericChooserRow(new String[] { miscItemTemplates[i-1].getMiscItemDescription() }, miscItemTemplates[i-1]);
			}
			// Changes made by Satin to move "Exception Item" to second menu option.
			availMiscItemTemplates1[1] = new GenericChooserRow(new String[] { "Exception Item" }, miscItemTemplate);
			GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availMiscItemTemplates1, new String[] { (res.getString("SERVICES")) });
			dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
			dlg.setVisible(true);
			if (dlg.isOK()) {
				return (MiscItemTemplate) ((MiscItemTemplate) dlg.getSelectedRow().getRowKeyData()).clone();
			} else {
				theAppMgr.showErrorDlg(res.getString("You did not select a miscellaneous item, item was not added."));
				enterItem();
				return null;
			}
		}
	}//end of solicitMiscItemTemplateChoice

	// Manpreet S Bawa (02/04/05)
	// Used to show loyality if customer has Loyality card.
	public void setLoyalityTag(CMSCustomer cmsCustomer) {
		String sLoyalityAmount = ": nn%"; // To be changed when Loyality spec. is done
		if (cmsCustomer.isLoyaltyMember()) {
			// TO - DO  : Get loyality amount for customer
			// sLoyalityAmount = ??
			lblLoyality.setText(sLoyalityString + sLoyalityAmount);
			lblLoyality.setVisible(true);
		}
	}

	/**
	 * Component initialization
	 */
	private void jbInit() throws Exception {
		pnlLineItem = new LineItemPOSPanel();
		pnlDetailLineItem = new DetailLineItemPanel();
		pnlLineItemMulti = new LineItemPOSPanelMultiSelect();
		JLabel labSubTotal = new JLabel();
		labMerchandiseCount = new JLabel();
		cardLayout = new RolodexLayout();
		cardPanel = new JPanel();
		cardPanel.setLayout(cardLayout);
		this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
		this.getContentPane().add(cardPanel, BorderLayout.CENTER);
		this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		cardPanel.add(pnlLineItem, "LINE_ITEM");
		cardPanel.add(pnlDetailLineItem, "DETAIL_LINE_ITEM");
		cardPanel.add(pnlLineItemMulti, "LINE_ITEM_MULTI");
		pnlSouth.setLayout(new GridBagLayout());
		pnlLineItem.setAppMgr(theAppMgr);
		pnlLineItemMulti.setAppMgr(theAppMgr);
		pnlDetailLineItem.setAppMgr(theAppMgr);
		labLayaway.setText(res.getString("SALE"));
		//Manpreet S Bawa (02/02/05)
		// Loyality place holder
		lblLoyality.setFont(theAppMgr.getTheme().getMessageFont());
		lblLoyality.setVisible(false); // Loyality is shown only if customer has loyality card.
		labSubTotal.setText(res.getString("Total/Subtotal"));
		labDeposit.setText(res.getString("Deposit"));
		String file = theAppMgr.getTheme().getFileName();
		ConfigMgr cfgMgr = new ConfigMgr(file);
		String strFont_subtotal = cfgMgr.getString("FONT.SUBTOTAL_AMOUNT_DUE");
		Font font_subtotal = getFont(strFont_subtotal);
		labSubTotal.setForeground(new Color(0, 0, 175));
		labSubTotal.setFont(font_subtotal);
		fldSubTotal.setHorizontalAlignment(4);
		fldSubTotal.setForeground(new Color(0, 0, 175));
		fldSubTotal.setFont(font_subtotal);
		labDeposit.setForeground(new Color(0, 0, 175));
		labDeposit.setFont(theAppMgr.getTheme().getMessageFont());
		fldDeposit.setForeground(new Color(0, 0, 175));
		fldDeposit.setHorizontalAlignment(4);
		fldDeposit.setFont(theAppMgr.getTheme().getMessageFont());
		fldTotalUnits.setForeground(new Color(0, 0, 175));
		fldTotalUnits.setFont(theAppMgr.getTheme().getMessageFont());
		pnlSouth.add(labLayaway, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 10, 0));
		pnlSouth.add(this.lblEMployeeSale, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		// Manpreet S Bawa (02/02/05) -- Loyality Place holder
		pnlSouth.add(lblLoyality, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 80, 1, 1), 0, 0));
		// Manpreet S Bawa (02/08/05)
		// As per Armani fucntional specs - Dont need to display total items.
		// pnlSouth.add(fldTotalUnits, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
		//                                                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
		//                                                0, 5, 0), 0, 0));
		pnlSouth.add(labDiscount, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnlSouth.add(fldSubTotal, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnlSouth.add(labSubTotal, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		pnlSouth.add(labDeposit, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		pnlSouth.add(fldDeposit, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnlSouth.add(labMerchandiseCount, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
		pnlSouth.setBackground(theAppMgr.getBackgroundColor());
		labLayaway.setForeground(new Color(0, 0, 175));
		labLayaway.setFont(theAppMgr.getTheme().getSaleFont());
		labDiscount.setFont(theAppMgr.getTheme().getMessageFont());
		labDiscount.setForeground(new Color(0, 0, 175));
		lblEMployeeSale.setFont(theAppMgr.getTheme().getMessageFont());
		lblEMployeeSale.setForeground(new Color(0, 0, 175));
		labMerchandiseCount.setFont(theAppMgr.getTheme().getMessageFont());
		labMerchandiseCount.setForeground(new Color(0, 0, 175));
		MouseAdapter focusMouseAdapter = new MouseAdapter() {

			/**
			 * put your documentation comment here
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				theAppMgr.setEditAreaFocus();
			}
		};
		pnlLineItem.addMouseListener(focusMouseAdapter);
		pnlDetailLineItem.addMouseListener(focusMouseAdapter);
		//  fldSubTotal.setPreferredSize(new Dimension((int)(r*100), (int)(r*20)));
		//    fldDeposit.setPreferredSize(new Dimension((int)(r * 100), (int)(r * 20)));
		/* ---For Testing layout purposes
		 //---
		 pnlSouth.setBorder( BorderFactory.createLineBorder(Color.red));
		 labSubTotal.setBorder( BorderFactory.createLineBorder(Color.green));
		 lblLoyality.setBorder( BorderFactory.createLineBorder(Color.blue));
		 labDeposit.setBorder( BorderFactory.createLineBorder(Color.black));
		 fldSubTotal.setBorder( BorderFactory.createLineBorder(Color.lightGray));
		 fldTotalUnits.setBorder( BorderFactory.createLineBorder(Color.red));*/
	}

	/**
	 * put your documentation comment here
	 * @param value
	 * @return
	 */
	private Font getFont(String value) {
		Font font = null;
		if (value != null) {
			StringTokenizer st = new StringTokenizer(value, ",");
			if (st.countTokens() == 3) {
				String name = st.nextToken();
				int style = Integer.parseInt(st.nextToken());
				int size = Integer.parseInt(st.nextToken());
				font = new Font(name, style, size);
			}
		}
		return font;
	}

	/**********************************************************************/
	private class SelectionResetListener implements ListSelectionListener {
		/**
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (cardLayout.getCurrent(cardPanel) == pnlLineItem) {
				showButtons();
				enterItem();
			} else if (cardLayout.getCurrent(cardPanel) == pnlDetailLineItem) {
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.CONSULTANT_LINE, theOpr);
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	private void goPrevious(CMSActionEvent anEvent) {
		if (suggestDlg != null && suggestDlg.isVisible()) {
			suggestDlg.setVisible(false);
			return;
		}
		if (vecMenus.size() > 1) {
			appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0, ((String) vecMenus.elementAt(vecMenus.size() - 2)), 0));
			vecMenus.removeElementAt(vecMenus.size() - 2);
			vecMenus.removeElementAt(vecMenus.size() - 1);
		} else {
			if (sAction_global != null) {
				showButtons();
				enterItem();
				vecMenus = new Vector();
				sAction_global = new String();
			} else {
				theAppMgr.goBack();
			}
		}
	}

	/**
	 * Look if alteration id is already present for the selected line item.
	 * @param sAlterationID AlterationId
	 * @return boolean
	 */
	private boolean isDuplicateAlterationID(String sAlterationID) {
		//      CMSSaleLineItem cmsSaleLineItem = cmsSaleLineItem = (CMSSaleLineItem) pnlLineItem.
		//          getSelectedLineItem();
		//    POSLineItemDetail details[] = cmsSaleLineItem.getLineItemDetailsArray();
		POSLineItemDetail details[] = pnlLineItem.getSelectedLineItem().getLineItemDetailsArray();
		for (int iCtr = 0; iCtr < details.length; iCtr++) {
			//      CMSSaleLineItemDetail cmsSaleLineItemDetail = (CMSSaleLineItemDetail)
			//          details[iCtr];
			//      AlterationLineItemDetail alterationLineDetail[] = cmsSaleLineItemDetail.
			//          getAlterationLineItemDetailArray();
			AlterationLineItemDetail alterationLineDetail[] = null;
			if (details[iCtr] instanceof CMSPresaleLineItemDetail) {
				alterationLineDetail = ((CMSPresaleLineItemDetail) details[iCtr]).getAlterationLineItemDetailArray();
			} else if (details[iCtr] instanceof CMSSaleLineItemDetail) {
				alterationLineDetail = ((CMSSaleLineItemDetail) details[iCtr]).getAlterationLineItemDetailArray();
			}
			if (alterationLineDetail == null) {
				continue;
			}
			int iDetCtr = 0;
			for (iDetCtr = 0; iDetCtr < alterationLineDetail.length; iDetCtr++) {
				if (alterationLineDetail[iDetCtr].getAlterationTicketID().equalsIgnoreCase(sAlterationID.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * LineItem has alterations associated to it.
	 * @param bBuildAlterationTickets
	 * bBuildAlterationTickets = true --> If dailog box with alterationids needs to be shown
	 * bBuildAlterationTickets = false --> Dialog box not needed.
	 * @return boolean
	 */
	private boolean lineItemHasAlteration(boolean bBuildAlterationTickets) {
		if (!(pnlLineItem.getSelectedLineItem() instanceof CMSSaleLineItem || pnlLineItem.getSelectedLineItem() instanceof CMSPresaleLineItem))
			return false;
		//    CMSSaleLineItem cmsSaleLineItem = (CMSSaleLineItem) pnlLineItem.
		//        getSelectedLineItem();
		//    POSLineItemDetail details[] = cmsSaleLineItem.getLineItemDetailsArray();
		POSLineItemDetail details[] = pnlLineItem.getSelectedLineItem().getLineItemDetailsArray();
		Hashtable htAlterationIDs = new Hashtable();
		for (int iCtr = 0; iCtr < details.length; iCtr++) {
			//      CMSSaleLineItemDetail cmsSaleLineItemDetail = (CMSSaleLineItemDetail)
			//          details[iCtr];
			//      AlterationLineItemDetail alterationLineDetail[] = cmsSaleLineItemDetail.
			//          getAlterationLineItemDetailArray();
			AlterationLineItemDetail alterationLineDetail[] = null;
			if (details[iCtr] instanceof CMSPresaleLineItemDetail) {
				alterationLineDetail = ((CMSPresaleLineItemDetail) details[iCtr]).getAlterationLineItemDetailArray();
			} else if (details[iCtr] instanceof CMSSaleLineItemDetail) {
				alterationLineDetail = ((CMSSaleLineItemDetail) details[iCtr]).getAlterationLineItemDetailArray();
			}
			// If alterations are available but
			// AlterationTicketId dailog is not needed
			if (alterationLineDetail != null & (!bBuildAlterationTickets)) {
				return true;
			}
			if (alterationLineDetail == null) {
				continue;
			}
			int iDetCtr = 0;
			for (iDetCtr = 0; iDetCtr < alterationLineDetail.length; iDetCtr++) {
				if (alterationLineDetail[iDetCtr] == null) {
					continue;
				}
				htAlterationIDs.put(alterationLineDetail[iDetCtr].getAlterationTicketID(), "Alteration ID: " + alterationLineDetail[iDetCtr].getAlterationTicketID());
			}
		}
		if (htAlterationIDs.size() > 0) {
			htAlterationIDs.put("ARM_NEW_ALTERATION", res.getString("Add New ALteration ID"));
			alterationIDDlg = new AlterationIDDlg(theAppMgr.getParentFrame(), theAppMgr, htAlterationIDs, res.getString("Alteration Components"));
			return true;
		}
		return false;
	}

	/**
	 * Customer is required for tenderign
	 * If there is any alteration item in sale.
	 * @return boolean
	 */
	private boolean saleHasAlterationItems() {
		POSLineItem[] lineItems = theTxn.getLineItemsArray();
		if (lineItems == null)
			return false;
		for (int iCtr = 0; iCtr < lineItems.length; iCtr++) {
			POSLineItemDetail details[] = lineItems[iCtr].getLineItemDetailsArray();
			AlterationLineItemDetail alterationLineDetail[] = null;
			for (int iDtCtr = 0; iDtCtr < details.length; iDtCtr++) {
				if (details[iDtCtr] instanceof CMSPresaleLineItemDetail) {
					alterationLineDetail = ((CMSPresaleLineItemDetail) details[iDtCtr]).getAlterationLineItemDetailArray();
				} else if (details[iDtCtr] instanceof CMSSaleLineItemDetail) {
					alterationLineDetail = ((CMSSaleLineItemDetail) details[iDtCtr]).getAlterationLineItemDetailArray();
				} else {
					continue;
				}
				if (alterationLineDetail != null)
					return true;
			}
		}
		return false;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean saleHasRedeemableItems() {
		POSLineItem[] lineItems = theTxn.getLineItemsArray();
		if (lineItems == null) {
			return false;
		}
		for (int iCtr = 0; iCtr < lineItems.length; iCtr++) {
			if (lineItems[iCtr].getItem().isRedeemable()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Load all the Altertaion ITEM_GROUP_NAMES
	 * from Alteration.xml into popup
	 * for customer owned merchandise item
	 * and solicit alteration on it.
	 */
	private void solicitCustomerOwnedAlteration() {
		Hashtable htGroups = (new AlterationLookUpUtil().getAlterationGrpNamesWithDefaultSubGroupId());
		// No Groups available
		if (htGroups == null) {
			theAppMgr.showErrorDlg(res.getString("Alteration data not available at this time"));
			enterItem();
			return;
		}
		if (htGroups.size() > 0) {
			alterationIDDlg = new AlterationIDDlg(theAppMgr.getParentFrame(), theAppMgr, htGroups, "Class Group");
			alterationIDDlg.setVisible(true);
		}
		if (alterationIDDlg.getSelectedAlterationID() == null) {
			return;
		}
		// Set default class id for customer owned merchandise.
		((CMSItem) pnlLineItem.getSelectedLineItem().getItem()).setClassId(alterationIDDlg.getSelectedAlterationID());
		enterAlterationID();
	}

	/**
	 * Check if alteration is allowed
	 * on selected MISC item.
	 * @return boolean
	 */
	private boolean isAlterationAllowed() {
		POSLineItem lineItem = pnlLineItem.getSelectedLineItem();
		if (lineItem.isMiscItem()) {
			String sKey = lineItem.getMiscItemId();
			if (sKey == null || !(sKey.equalsIgnoreCase("CUSTOMER_OWNED") || LineItemPOSUtil.isNotOnFileItem(lineItem.getItem().getId())))
				return false;
		}
		return true;
	}

	/**
	 * Check if Modify Qty is allowed for
	 * selected line item.
	 * @return boolean
	 */
	private boolean isModifyQtyAllowed() {
		if (iAppletMode != CONSIGNMENT_OPEN && iAppletMode != PRE_SALE_OPEN && iAppletMode != RESERVATIONS_OPEN) {
			if (pnlLineItem.getSelectedLineItem() instanceof SaleLineItem) {
				CMSSaleLineItem saleLineItem = (CMSSaleLineItem) pnlLineItem.getSelectedLineItem();
				if (saleLineItem.getConsignmentLineItem() != null) {
					theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for Consignment line item."));
					return false;
				}
				if (saleLineItem.getReservationLineItem() != null) {
					theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for Reservation line item."));
					return false;
				}
				if (saleLineItem.getPresaleLineItem() != null) {
					theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for Presale line item."));
					return false;
				}
			}
			if (pnlLineItem.getSelectedLineItem() instanceof ReturnLineItem) {
				CMSReturnLineItem returnLineItem = (CMSReturnLineItem) pnlLineItem.getSelectedLineItem();
				if (returnLineItem.getConsignmentLineItem() != null) {
					theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for Consignment line item."));
					return false;
				}
				if (returnLineItem.getReservationLineItem() != null) {
					theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for Reservation line item."));
					return false;
				}
				if (returnLineItem.getPresaleLineItem() != null) {
					theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for Presale line item."));
					return false;
				}
			}
		}
		if (lineItemHasAlteration(false) || (pnlLineItem.getSelectedLineItem().isMiscItem() && pnlLineItem.getSelectedLineItem().getMiscItemId().equalsIgnoreCase("CUSTOMER_OWNED"))
				|| (pnlLineItem.getSelectedLineItem().isMiscItem() && pnlLineItem.getSelectedLineItem().getMiscItemId().equalsIgnoreCase("ALTERATION"))) {
			theAppMgr.showErrorDlg(res.getString("Quantity should not be more than 1 for alteration items"));
			return false;
		}
		return true;
	}

	/**
	 * Check to see if the customer has any messages.
	 */
	private CMSCustomerMessage isCustomerMessagePresent(CMSCompositePOSTransaction theTxn) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		StringBuffer strBuff = new StringBuffer();
		File filecustMsg = new File(FileMgr.getLocalFile("xml", "customer_messages.xml"));
		CMSCustomer cust = (CMSCustomer) (((CMSCompositePOSTransaction) theTxn).getCustomer());
		SAXCustomerMessageUnMarshaller custMsgUnmarshaller = new SAXCustomerMessageUnMarshaller(cust.getId(), cust.getCustomerType());
		try {
			OutputStreamWriter out = new OutputStreamWriter(System.out, "UTF8");
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(filecustMsg, custMsgUnmarshaller);
		} catch (Throwable err) {
			err.printStackTrace();
		}
		//Get the boolean if the search is done by Id.
		boolean isSearchedById = custMsgUnmarshaller.getSearchById();
		setSearchById(isSearchedById);
		return custMsgUnmarshaller.getCustomerMessage();
	}

	/**
	 * Sets the searchById value.
	 */
	private void setSearchById(boolean search) {
		this.search = search;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean getSearchById() {
		return this.search;
	}

	/**
	 * put your documentation comment here
	 * @param dis
	 * @param lineItem
	 */
	private void addLineItemDiscount(CMSDiscount dis, POSLineItem lineItem) {
		if (lineItem == null)
			return;
		try {
			POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
			Discount priceLineDiscount = null;
			//POSLineItem lineItem = (POSLineItem)pnlLineItem.getSelectedLineItem();
			if (theTxn.isEmployeeSale && dis instanceof CMSEmployeeDiscount) {
				if (lineItem != null) {
					Discount discount[] = lineItem.getDiscountsArray();
					if (discount != null) {
						for (int j = 0; j < discount.length; j++) {
							if (discount[j] instanceof CMSEmployeeDiscount) {
								lineItem.removeDiscount(discount[j]);
							}
						}
					}
					//          lineItem.addDiscount( (CMSEmployeeDiscount) dis);
				}
			}
			//      else {
			if (dis.getType().equals("BY_PRICE_DISCOUNT")) {
				if (lineItem instanceof CMSSaleLineItem) {
					if (((CMSSaleLineItem) lineItem).isPriceDiscountAdded && ((CMSSaleLineItem) lineItem).getPriceDiscount() != null) {
						priceLineDiscount = ((CMSSaleLineItem) lineItem).getPriceDiscount();
						((CMSSaleLineItem) lineItem).removeDiscount(priceLineDiscount);
						theTxn.removeDiscount(priceLineDiscount);
						((CMSSaleLineItem) lineItem).removeAddPriceDiscount();
					}
					((CMSSaleLineItem) lineItem).setAddPriceDiscount(dis);
				} else if (lineItem instanceof CMSReturnLineItem) {
					if (((CMSReturnLineItem) lineItem).isPriceDiscountAdded && ((CMSReturnLineItem) lineItem).getPriceDiscount() != null) {
						priceLineDiscount = ((CMSReturnLineItem) lineItem).getPriceDiscount();
						((CMSReturnLineItem) lineItem).removeDiscount(priceLineDiscount);
						theTxn.removeDiscount(priceLineDiscount);
						((CMSReturnLineItem) lineItem).removeAddPriceDiscount();
					}
					((CMSReturnLineItem) lineItem).setAddPriceDiscount(dis);
				} else if (lineItem instanceof CMSConsignmentLineItem) {
					if (((CMSConsignmentLineItem) lineItem).isPriceDiscountAdded && ((CMSConsignmentLineItem) lineItem).getPriceDiscount() != null) {
						priceLineDiscount = ((CMSConsignmentLineItem) lineItem).getPriceDiscount();
						((CMSConsignmentLineItem) lineItem).removeDiscount(priceLineDiscount);
						theTxn.removeDiscount(priceLineDiscount);
						((CMSConsignmentLineItem) lineItem).removeAddPriceDiscount();
					}
					((CMSConsignmentLineItem) lineItem).setAddPriceDiscount(dis);
				} else if (lineItem instanceof CMSPresaleLineItem) {
					if (((CMSPresaleLineItem) lineItem).isPriceDiscountAdded && ((CMSPresaleLineItem) lineItem).getPriceDiscount() != null) {
						priceLineDiscount = ((CMSPresaleLineItem) lineItem).getPriceDiscount();
						((CMSPresaleLineItem) lineItem).removeDiscount(priceLineDiscount);
						theTxn.removeDiscount(priceLineDiscount);
						((CMSPresaleLineItem) lineItem).removeAddPriceDiscount();
					}
					((CMSPresaleLineItem) lineItem).setAddPriceDiscount(dis);
				} else if (lineItem instanceof CMSReservationLineItem) {
					if (((CMSReservationLineItem) lineItem).isPriceDiscountAdded && ((CMSReservationLineItem) lineItem).getPriceDiscount() != null) {
						priceLineDiscount = ((CMSReservationLineItem) lineItem).getPriceDiscount();
						((CMSReservationLineItem) lineItem).removeDiscount(priceLineDiscount);
						theTxn.removeDiscount(priceLineDiscount);
						((CMSReservationLineItem) lineItem).removeAddPriceDiscount();
					}
					((CMSReservationLineItem) lineItem).setAddPriceDiscount(dis);
				} else {
				}
			}
			if (lineItem != null) {
				lineItem.addDiscount(dis);
				//sendItemMessageData(lineItem, lineItemArray, deleteFlag, true, false, false,dis.getAmount().formattedStringValue());
			}
			//      }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @param discount
	 * @exception BusinessRuleException
	 */
	private void addSubtotalDiscount(CMSDiscount discount) throws BusinessRuleException {
		if (discount.isSubTotalDiscount) {
			POSLineItem[] posLineItems = (POSLineItem[]) theTxn.getLineItemsArray();
			for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
				POSLineItem posLineItem = posLineItems[i];
				if (theTxn.isEmployeeSale && discount instanceof CMSEmployeeDiscount) {
					Discount discounts[] = posLineItem.getDiscountsArray();
					if (discount != null) {
						for (int j = 0; j < discounts.length; j++) {
							if (discounts[j] instanceof CMSEmployeeDiscount) {
								posLineItem.removeDiscount(discounts[j]);
							}
							break;
						}
					}
					posLineItem.addDiscount((CMSEmployeeDiscount) discount);
				} else {
					posLineItem.addDiscount((CMSDiscount) discount);
				}
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param discount
	 * @exception BusinessRuleException
	 */
	private void addTransactionDiscount(CMSDiscount discount) throws BusinessRuleException {
		//Remove employee discount at txn level
		if (theTxn.isEmployeeSale && discount instanceof CMSEmployeeDiscount) {
			Discount discounts[] = theTxn.getDiscountsArray();
			if (discounts != null) {
				for (int j = 0; j < discounts.length; j++) {
					if (discounts[j] instanceof CMSEmployeeDiscount) {
						theTxn.removeDiscount(discounts[j]);
						break;
					}
				}
			}
		}
		//Remove existing employee discount and add new employee discount at line item level
		POSLineItem posLineItems[] = (POSLineItem[]) theTxn.getSaleLineItemsArray();
		if (posLineItems != null) {
			for (int i = 0; i < posLineItems.length; i++) {
				POSLineItem posLineItem = posLineItems[i];
				Discount discounts[] = posLineItem.getDiscountsArray();
				if (discount != null) {
					for (int j = 0; j < discounts.length; j++) {
						if (discounts[j] instanceof CMSEmployeeDiscount) {
							posLineItem.removeDiscount(discounts[j]);
						}
						break;
					}
				}
				posLineItem.addDiscount((CMSEmployeeDiscount) discount);
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param discount
	 * @param lineItem
	 * @return
	 */
	private boolean validatePriceDiscount(CMSDiscount discount, POSLineItem lineItem) {
		if (discount.getType().equals("BY_PRICE_DISCOUNT")) {
			ArmCurrency sellingPrice = new ArmCurrency(0.0d);
			try {
				sellingPrice = LineItemPOSUtil.getSellingPrice(lineItem);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (discount.getAmount().doubleValue() > sellingPrice.doubleValue()) {
				theAppMgr.showErrorDlg(res.getString("Price Discount amount cannot be greater than the selling price of the item"));
				showButtons();
				enterItem();
				return false;
			}
		} else {
			// By Amount Discount
			if (discount.getAmount().doubleValue() > Math.abs(lineItem.getNetAmount().doubleValue())) {
				theAppMgr.showErrorDlg(res.getString("Discount amount cannot be greater than the unit item's due amount"));
				showButtons();
				enterItem();
				return false;
			}
		}
		return true;
	}

	/**
	 * put your documentation comment here
	 */
	private void updateMerchandiseCountLabel() {
		labMerchandiseCount.setText(res.getString("Merchandise Count: ") + this.theTxn.getTotalMerchandiseCount());
	}

	/**
	 * put your documentation comment here
	 */
	private void setCustomerLoyalty() {
		if (theTxn.getCustomer() == null)
			return;
		CMSCustomer customer = (CMSCustomer) theTxn.getCustomer();
		try {
			Loyalty[] custLoyalties = LoyaltyHelper.getCustomerLoyalties(theAppMgr, customer.getId());
			String brandID = ((CMSStore) theTxn.getStore()).getBrandID();
			if (custLoyalties == null || brandID == null) {
				setLoyalty(null);
				return;
			}
			String loyaltyStrBrandID = null;
			for (int i = 0; i < custLoyalties.length; i++) {
				try {
					if (setLoyalty(custLoyalties[i])) {
						return;
					}
				} catch (BusinessRuleException ex) {
					//ex.printStackTrace();
				}
			}
			setLoyalty(null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @param loyalty
	 * @return
	 * @exception BusinessRuleException
	 */
	private boolean setLoyalty(Loyalty loyalty) throws BusinessRuleException {
		if (loyalty != null) {
			String brandID = ((CMSStore) theTxn.getStore()).getBrandID();
			String loyaltyStrBrandID = loyaltyConfigMgr.getString(loyalty.getStoreType() + ".TYPE");
			if (loyaltyStrBrandID != null && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim()) && loyalty.getStatus()) {
				theTxn.setLoyaltyCard(loyalty);
				theTxn.update();
				return true;
			} else {
				theTxn.setLoyaltyCard(null);
				theTxn.update();
				throw new BusinessRuleException(res.getString("This loyalty card cannot be used in this store."));
			}
		}
		//else
		theTxn.setLoyaltyCard(null);
		theTxn.update();
		return false;
	}

	/**
	 */
	private void selectMultiDiscount() {
		theAppMgr.setSingleEditArea(res.getString("Please select the item(s) you want to apply discount to."));
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean checkAlterationValid() {
		try {
			if (theAppMgr.getStateObject("POS_LINE_ITEM") != null) {
				POSLineItem posLineItem = (POSLineItem) theAppMgr.getStateObject("POS_LINE_ITEM");
				if (!(posLineItem instanceof SaleLineItem || posLineItem instanceof CMSPresaleLineItem)) {
					theAppMgr.showErrorDlg(res.getString("Alteration not allowed on this item"));
					return false;
				}
				String classId = ((CMSItem) posLineItem.getItem()).getClassId();
				if (posLineItem.isMiscItem() && LineItemPOSUtil.isNotOnFileItem(posLineItem.getItem().getId())) //make sure item is not not-on-file
					classId = LineItemPOSUtil.getNotOnFileItemClass(posLineItem);
				if (!populateAlterationDetails(classId)) {
					theAppMgr.showErrorDlg(res.getString("Alteration not allowed on this item"));
					return false;
				}
			} else {
				theAppMgr.showErrorDlg(res.getString("Alteration not allowed on this item"));
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Populate all the alteration details.
	 * @param sClassID String
	 * @return boolean
	 */
	private boolean populateAlterationDetails(String sClassID) {
		try {
			AlterationItemGroup alterationObj = null;
			alterationObj = new AlterationLookUpUtil().findBySubGroupId(sClassID);
			if (alterationObj == null) {
				return false;
			}
			AlterationDetail alterationDetails[] = alterationObj.getAlterationDetailsArray();
			if (alterationDetails == null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * put your documentation comment here
	 * @param miscItemTemplate
	 */
	private void buildMiscItem(MiscItemTemplate miscItemTemplate) {
		if (miscItemTemplate != null) {
			//Added by deepika for Exception_Item PCR to call the ExceptionItemBldr
			if(miscItemTemplate.getKey()== null)
			{
				theAppMgr.buildObject(this, "MISC_ITEM", exceptionbuilder, miscItemTemplate);
			}
			//code ended by deepika for Exception_Item PCR
			else if (miscItemTemplate.getKey().equals("STORE_VALUE_CARD")) {
				if (iAppletMode == RETURN_MODE)
					theAppMgr.showErrorDlg(res.getString("Redeemable items cannot be returned. Use the buy-back transaction."));
				else {
					try {
						new CMSStoreValueCard("STORE_VALUE_CARD").isValidForIssue(iAppletMode);
						String Builder = CMSPaymentMgr.getPaymentBuilder("STORE_VALUE_CARD"); // retrieve the credit card builder
						theAppMgr.addStateObject("ARM_STORE_VALUE_ID", miscItemTemplate);
						theAppMgr.buildObject("SPECIFIC", Builder, "CREATE"); // invoke the builder
					} catch (BusinessRuleException ex1) {
						theAppMgr.showErrorDlg(ex1.getMessage());
					}
				}
			}//changed by shushma for shipping FED-EX
			else if (miscItemTemplate.getKey().equals("SHIP")) {
				buildShippingOptions(this, miscItemTemplate);
			}
			//Shipping FED-EX close

			else if (LineItemPOSUtil.isNotOnFileItem(miscItemTemplate.getBaseItemId())) {
				String builderStr = new ConfigMgr("item.cfg").getString(miscItemTemplate.getKey() + ".BUILDER");
				theAppMgr.buildObject(this, "MISC_ITEM", builderStr, miscItemTemplate);
			} else {
				theAppMgr.buildObject(this, "ITEM", itemBuilder, miscItemTemplate);
			}
		}
	}

	//added by Shushma for Shipping Fed-Ex
	public void buildShippingOptions(CMSApplet cmsApplet,MiscItemTemplate miscItemTemplate){
		ConfigMgr mgr = new ConfigMgr("item.cfg");
		ConfigMgr configMgr=new ConfigMgr("client_master.cfg");
		String shipping = mgr.getString("SHIPPING_OPTIONS");
		StringTokenizer strTok = new StringTokenizer(shipping, ",");
		int n=strTok.countTokens();
		itemBuilder = mgr.getString("ITEM.BUILDER");
		GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[strTok.countTokens()];
		for(int k=0;k<n;k++){
			String misckey = (String) strTok.nextToken();
			shipdesc = mgr.getString(misckey + ".DESC");
			double amt = Double.parseDouble(mgr.getString(misckey + ".AMOUNT"));
			armCurrency=new ArmCurrency(amt);
			availMiscItemTemplates[k] = new GenericChooserRow(new String[]{shipdesc},amt);
		}
		GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availMiscItemTemplates, new String[] { (res.getString("FedEx OPTIONS")) });
		dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
		dlg.setVisible(true);

		if (dlg.isOK()) {
			Object curr=dlg.getSelectedRow().getRowKeyData();
			Object[] desc=dlg.getSelectedRow().getDisplayRow();
			armCurrency=new ArmCurrency((Double) curr);
			miscItemTemplate.setAmount(armCurrency);
			miscItemTemplate.setMiscItemDescription((String) desc[0]);
			if(((String) desc[0]).equals("Shipping Cost Override")){
				setMisItemTemplateForFedEx(miscItemTemplate);
				enterManagerID();
				return;
				}
			else{
				int i=((String) desc[0]).indexOf(":");
				String str1[]={((String) desc[0]).substring(0,i)};
				miscItemTemplate.setDescription(str1);
				miscItemTemplate.setCanOverrideAmount(false);

			}
			theAppMgr.buildObject(cmsApplet, "MISC_ITEM", itemBuilder, miscItemTemplate);
		}

		else {
			theAppMgr.showErrorDlg(res.getString("You did not select any shipping options, item was not added."));
		}
	}
//method close
//changed by shushma for shipping Fed-Ex
	private void enterManagerID() {
		// TODO Auto-generated method stub
		theAppMgr.setSingleEditArea(res.getString("Enter Manager's ID."), "OPERATOR", theAppMgr.SSN_MASK);
		theAppMgr.setEditAreaFocus();
		return;
	}
	private void enterPassword() {
		//Vivek Mishra : Added in order to fix fatal exception during ship override.
		if(isShipOpr)
		{
			theAppMgr.setSingleEditArea(res.getString("Please enter your secret password.")
			        , "PASSWORDSHIP", theAppMgr.PASSWORD_MASK);
			isShipOpr = false;
		}
		else
		{
	    theAppMgr.setSingleEditArea(res.getString("Please enter your secret password.")
	        , "PASSWORD", theAppMgr.PASSWORD_MASK);
		}
		theAppMgr.setEditAreaFocus();
		return;
	}

//method close

	/**
	 * put your documentation comment here
	 */
	private void removePromotion() {
		POSLineItem posLineItem = pnlLineItem.getSelectedLineItem();
		int iIndex = pnlLineItem.getTable().getSelectedRow();
		try {
			if (posLineItem instanceof SaleLineItem) {
				((CMSSaleLineItem) posLineItem).removePromotion();
			} else if (posLineItem instanceof ReturnLineItem) {
				((CMSReturnLineItem) posLineItem).removePromotion();
			}
			pnlLineItem.repaint();
			pnlLineItem.selectRow(iIndex);
			updateLabel();
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString("Exception removing promotion"));
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isCustomerRequiredForDiscounts() {
		Discount discounts[] = theTxn.getDiscountsArray();
		if (discounts == null)
			return false;
		for (int iCtr = 0; iCtr < discounts.length; iCtr++) {
			if (((CMSDiscount) discounts[iCtr]).isCustomerRequired())
				return true;
		}
		return false;
	}

	/**
   * Fix 1840 :Deposit total amount is not calculated on the SALES screen.
   * @param customer
   * @exception BusinessRuleException
   */
  private void setCustomer (CMSCustomer customer) throws BusinessRuleException {
	  theTxn.setCustomer(customer);
	  String thisStoreId = ((CMSStore)theStore).getId();
	  if (thisStoreId != null && customer != null) {
		  ArmCurrency custCurr = null;
		  try {
			  custCurr = CustomerUtil.getDepositHistoryBalance(theAppMgr, customer.getId(), thisStoreId);
		  } catch (Exception e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  if (custCurr != null) {
			  customer.setCustomerBalance(custCurr);
		  }
    }
    else {

    }
  }

  //Added new method for Dolcy Candy
public void setButtons(String [] buttons, String CandyName){
	JButton[] btns = new JButton[buttons.length+1];

	for (int x = 0; x < buttons.length; x++) {
		btns[x] = new JButton(buttons[x]);
        btns[x].setActionCommand((String) buttons[x]);
	}

	Theme theme = theAppMgr.getTheme();
	btns[buttons.length] = new JButton(res.getString("Previous"));
	btns[buttons.length].addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
	    	showButtons();
      }
    });
	theAppMgr.setButtons(CandyName, btns);
	for (int i = 0; i < buttons.length; i++) {
		 final String key = CandyName+"."+buttons[i];
		btns[i].addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent ae) {
			    	clickButton(key);
			      }
		    });
	}
}

/**
 * This is method is add the skues of dolci in HashMap and also set in Golbalobject
 * @return boolean
 * @param ItemID
 *
 * */
private boolean isDolciCandy(String itemID){
     java.util.Map dolciSkuValue =(java.util.Map)theAppMgr.getGlobalObject("DOLCI_ITEM_PRESENT");
	  java.util.List<String>  dolciSkuList = null;
	  String  dolciValue = null;
	  if(dolciSkuValue == null) {
          dolciSkuList = new ArrayList<String>();
          dolciSkuValue = new HashMap();
          ConfigMgr mgr = new ConfigMgr("item.cfg");
      	  String dolciKeys = mgr.getString("DOLCI_CANDY_KEYS");
          StringTokenizer strTok = new StringTokenizer(dolciKeys, ",");
	          while(strTok.hasMoreTokens()) {
	                dolciSkuList.add(strTok.nextToken());
	          }
          int length = dolciSkuList.size();
          if(length!=0){
          	for(int i=0;i<length;i++){
          		String key = dolciSkuList.get(i);
          		String boxesKeys = mgr.getString(key+"_BOXES_KEYS");
          		if(boxesKeys!=null){
          		     StringTokenizer strToken = new StringTokenizer(boxesKeys, ",");
	              	 while(strToken.hasMoreTokens()) {
	              		String token = strToken.nextToken();
	              		String newKey = key+"."+token;
	              		dolciValue = mgr.getString(newKey);
	              		dolciValue = dolciValue.trim();
	              		//this is added all skues in hashmap.
	              		dolciSkuValue.put(newKey,dolciValue);
	                }
          		}
          	 }
          }
          theAppMgr.addGlobalObject("DOLCI_ITEM_PRESENT",dolciSkuValue );
         }
	  return dolciSkuValue.containsValue(itemID);
}
/**
 * This is method which retrived the key from value in Dolci Candy.
 * HashMap is set in Application for Dolci candy
 * Value is ItemId from EditArea Event.
 * @author vivek sawant
 * */
	 public String getKeyFromValue(HashMap hashmap,String value){
		Set ref = hashmap.keySet();
		Iterator it = ref.iterator();

		while (it.hasNext()) {
			 String key = (String)it.next();
			 String values =(String)hashmap.get(key);
			 if(values.equals(value)){
		        return key;
			 }
		}
		return null;
	 }

//Ended code for Dolci Candy

//added setters and getter for fed-ex by shsuhma
	public MiscItemTemplate getMisItemTemplateForFedEx() {
		return misItemTemplateForFedEx;
	}


	public void setMisItemTemplateForFedEx(MiscItemTemplate misItemTemplateForFedEx) {
		this.misItemTemplateForFedEx = misItemTemplateForFedEx;
	}

	CMSEmployee cmsEmployee;
	public CMSEmployee getCmsEmployee() {
		return cmsEmployee;
	}

	public void setCmsEmployee(CMSEmployee cmsEmployee) {
		this.cmsEmployee = cmsEmployee;
	}


	public long getPrivilege() {
		return privilege;
	}

	public void setPrivilege(long privilege) {
		this.privilege = cmsEmployee.doGetPrivileges();
	}

	 //Added by Rachana for checking the approverID for return transaction
	 protected void enterUserName() {
		    theAppMgr.setSingleEditArea(res.getString("Enter approver id for return and press 'Enter'.")
		        , "APPROVER", theAppMgr.PASSWORD_MASK);
		    //theAppMgr.setEditAreaFocus();
		  }

	/* private void enterPassword() {
		    theAppMgr.setSingleEditArea(res.getString("Please enter your secret password.")
		        , "PASSWORD", theAppMgr.PASSWORD_MASK);
			//theAppMgr.setEditAreaFocus();
			//return;
		}*/

	 protected boolean processPasswordEntry(String password) {

		    if (theApprover.getPassword()!=null && theApprover.getPassword().equals(password)) {
		      return (true);
		    } else {
		      theAppMgr.showErrorDlg(res.getString("Incorrect Password entered"));
		      passwordWrongCount++;
		      return (false);
		    }
		  }


	 //Added by Rachana for checking the approverID for return transaction
	 protected boolean testCanBeOperator() {
		 try {
			 theApprover.testCanBeOperator();
		     return (true);
		 } catch (BusinessRuleException bx) {
		    theAppMgr.showErrorDlg(res.getString("A terminated employee cannot login to the system"));
		    invalidLogOnAttempt(theApprover.getExternalID());
		    return (false);
		 }
	 }

	 protected void invalidLogOnAttempt(String id) {
		 CMSRegisterSessionAppModel sessionAppModel = new CMSRegisterSessionAppModel();
		 sessionAppModel.setRegister((CMSRegister)theAppMgr.getGlobalObject("REGISTER"));
		 sessionAppModel.setSessionDate(new Date());
		 sessionAppModel.setStore((CMSStore)theAppMgr.getGlobalObject("STORE"));
		 sessionAppModel.setLogonEntered(id);
		 sessionAppModel.print(ReceiptBlueprintInventory.CMSInvalidLogonAttempt, theAppMgr);
	}


}
