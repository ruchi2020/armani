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
 */

package com.chelseasystems.cs.swing.pos;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.mask.*;
import com.chelseasystems.cr.business.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.dm.display.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.park.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.pricing.IPromotion;
import com.chelseasystems.cr.register.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cr.swing.layout.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.appmgr.mask.CMSCurrencyMask;
import com.chelseasystems.cs.appmgr.mask.QuantifiedCurrencyInput;
import com.chelseasystems.cs.appmgr.mask.QuantifiedCurrencyMask;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.pricing.CMSPromotionBasedPriceEngine;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.swing.menu.*;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cs.tax.*;

import java.util.Vector;

import com.chelseasystems.cs.swing.dlg.AlterationIDDlg;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cs.util.AlterationLookUpUtil;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.loyalty.Loyalty;

import java.text.NumberFormat;
import java.io.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import com.chelseasystems.cs.swing.builder.CMSItemWrapper;
import com.chelseasystems.cs.swing.customer.CustomerMessageDialog;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.appmgr.mask.ItemCodeMask;
import com.chelseasystems.cr.appmgr.mask.ItemCodeMask.QuantifiedItemId;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.swing.mask.CMSMaskConstants;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;
/**
 */
public class InitialSaleApplet_JP extends CMSApplet {
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
	JPanel pointsAndDepositPanel;
	JPanel cardPanel;
	POSHeaderPanel_JP pnlHeader;
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
	JLabel lblTxnLoyalty; // label for "Points you've got today"
	JLabel lblTotalLoyaltyPoints; // label for "Total Points"
	private String sLoyalityString; // Loyality string to fetch value from Resource
	private AlterationIDDlg alterationIDDlg;

	// Reservation Deposit
	private MiscItem miscDepositItem;
	private POSLineItem lineItmDeposit;
	private double dReservationDepositPercent = 0;
	private boolean bUpdateDepositAmount;
	private ArmCurrency amtRSVODeposit = null;

	//  private double loyaltyRewardLevel;

	public static final int NO_OPEN_RESERVATIONS_CLOSE_SALE = 11;
	public static final int NO_OPEN_RESERVATIONS_CLOSE_RETURN = 12;

	//Initialize the applet
	public void init() {
		try {
			sLoyalityString = res.getString("Loyality Discount");
			lblLoyality = new JLabel(sLoyalityString);
			pnlHeader = new POSHeaderPanel_JP();
			pnlSouth = new JPanel();
			labDeposit = new JLabel();
			fldSubTotal = new JLabel();
			fldDeposit = new JLabel();
			fldTotalUnits = new JLabel();
			labDiscount = new JLabel();
			labLayaway = new JLabel();
			lblTxnLoyalty = new JLabel();
			lblTotalLoyaltyPoints = new JLabel();
			this.lblEMployeeSale = new JLabel();
			// Set the font
			Font txtFont = theAppMgr.getTheme().getTextFieldFont();
			//lblEMployeeSale.setFont(txtFont);
			selectionResetListener = new SelectionResetListener();
			jbInit();

			loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
			//      String loyaltyRewardAmt = loyaltyConfigMgr.getString("LOYALTY_REWARD_AMOUNT");
			//      String loyaltyRewardRedemptionRatio = loyaltyConfigMgr.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
			//      loyaltyRewardLevel = new Double(loyaltyRewardAmt).doubleValue() * new Double(loyaltyRewardRedemptionRatio).doubleValue();

			config = new ConfigMgr("item.cfg");
			itemBuilder = config.getString("ITEM.BUILDER");
			config = new ConfigMgr(System.getProperty("USER_CONFIG"));
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
			lblTxnLoyalty.setVisible(false);
			lblTxnLoyalty.setText("");
			pointsAndDepositPanel.repaint();
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
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
				try {
					CMSCustomer txnCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
					theAppMgr.removeStateObject("TXN_CUSTOMER");
					if (theTxn != null && txnCust != null) {
						updateMerchandiseCountLabel();
						//theTxn.setCustomer(txnCust);
						//Fix 1840 :Deposit total amount is not calculated on the SALES screen.
						setCustomer(txnCust);
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
			//      CMSCustomer currentCust = (CMSCustomer) theAppMgr.getStateObject(
			//          "CUSTOMER_FOUND");
			//      if (theTxn != null && currentCust != null) {
			//        theTxn.setCustomer(currentCust);
			//        //remove postandpack
			//      }
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
		//    obj.registerKeyboardAction(new ActionListener() {
		//      public void actionPerformed(ActionEvent e) {
		//                goPrevious(new CMSActionEvent(e));
		//      }
		//    }
		//
		//    , "ESC_CMD", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
		//        JComponent.WHEN_IN_FOCUSED_WINDOW);
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
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							buildDepositItem(false);
							if (miscDepositItem != null) {
								calculateDepositAmount(true);
								bUpdateDepositAmount = true;
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
	}

	public void editAreaEvent(String Command, Object obj) {
		try {
			if (Command.equals("RSVO_DEPOSIT") && obj != null && obj instanceof QuantifiedCurrencyInput) {
				QuantifiedCurrencyInput qcInput = (QuantifiedCurrencyInput) obj;
				//BUG 1695: updated to allow calculation from editArea 
			ArmCurrency rsvoDeposit = new ArmCurrency(qcInput.getCurrency()).truncate(0);
				if (qcInput.getMultiplier() == 1.0) {
					//assume only amount was entered
					this.editAreaEvent("RSVO_DEPOSIT", rsvoDeposit);
				} else {
					// assume amount*multiplier was entered => let user confirm the amount again
					enterRSVODepositAmount(rsvoDeposit.multiply(qcInput.getMultiplier()).truncate(0));
				}
			} else {
				// null or invalid entry => let user re-enter amount
				enterRSVODepositAmount();
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	// callback whan an application tool bar button is pressed
	public void editAreaEvent(String Command, ArmCurrency amount) {
		String breRetry = ENTER_ITEM;
		//       System.out.println("edit area event 1 : "+Command);
		try {
			if (Command.equals("ADJUST_DEPOSIT") && (isReservationCloseMode())) {
				// No validation needed for Adjusting deposit
				// ---Amount in case of US and Japan - MSB 11/18/05
				//        if (amount.getDoubleValue() < 0) {
				//          theAppMgr.showErrorDlg(res.getString("Deposit amount can not be less than zero"));
				//          enterAdjustDepositAmount();
				//          return;
				//        }
				// Check if amount is more than threshhold amount (total amount due)
				//        try {
				//          if(!isValidDepositAmount(amount))
				//          {
				//            theAppMgr.showErrorDlg(res.getString(
				//                "Deposit amount can not be more than zero total amount due"));
				//            enterAdjustDepositAmount();
				//            return;
				//          }
				//        } catch (Exception e) {}
				//----
				// Once Deposit is adjusted
				// it doesnt have to be recalculated automatically
				// anymore, so its safe to remove the state object now.
				theAppMgr.removeStateObject("ARM_RSV_DEPOSIT");

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
						pnlLineItem.modifySelectedUnitPrice(amount);
						//            pnlLineItem.getSelectedLineItem().setManualMarkdownReason( (String)
						//                reasons[0]);
						pnlLineItem.getSelectedLineItem().setManualMarkdownReason(reason);
					} else {
						//pnlLineItem.undoModifySelectedPrice();
					}
				} else {
					pnlLineItem.modifySelectedUnitPrice(amount);
				}
				showButtons();
				enterItem();
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
					CMSValueAddedTaxHelper.applyVAT(theAppMgr, theTxn, (CMSStore) theStore, (CMSStore) theStore, theTxn.getProcessDate(), true);
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
					System.out.println("IntialSaleApplet.editAreaEvent(QUANTITY)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
					//remove postandpack
				}
				theTxn.setPostAndPack(false, new ArmCurrency(0.0));
				// Issue # 1128
				updateLabel();
				showButtons();
				enterItem();
			} else if (Command.startsWith("MODIFY_TAX_PERCENT")) {
				double price = pnlDetailLineItem.getSelectedLineItem().getLineItem().getNetAmount().doubleValue();
				boolean isRegional = Command.endsWith(((CMSStore) theStore).getRegionalTaxLabel());
				pnlDetailLineItem.modifySelectedLineTax(new ArmCurrency(((number.doubleValue() / 10000.0) * price) * 100.0), false);
				System.out.println("\t\t\tprice:  " + price);
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
					System.out.println("the builder is: " + Builder);
					theAppMgr.buildObject("LOYALTY_ITEM", Builder, "");
				} else {
					qtyToApply = input.getQtyToApply();
					theAppMgr.buildObject(this, "ITEM", itemBuilder, ((ItemCodeMask.QuantifiedItemId) input).getItemId());
				}
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	// callback whan an edit area event occurs
	public void editAreaEvent(String Command, String sEdit) {
		//System.out.println("edit area event 5: "+Command);
		try {
			//MSB - Alterations
			if (Command.equals("ALTERATION_ID")) {
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
			}
			//ENTER ADDITIONAL CONSULTANT ID AND VALIDATE
			// -- Employee Sale
			// Added by - Manpreet S Bawa
			// 03/18/0
			//            else if(Command.equals("EMPLOYEE_SALE"))
			//            {
			//              if(sEdit.equals(((CMSEmployee) theOpr).getExternalID()))
			//              {
			//                theAppMgr.showErrorDlg(res.getString("Employee cant be same as operator"));
			//                theAppMgr.setSingleEditArea(res.getString("Enter employee ID"),
			//                                      "EMPLOYEE_SALE", theAppMgr.NO_MASK);
			//                return;
			//
			//              }
			//              CMSEmployee emp = CMSEmployeeHelper.findByExternalId(theAppMgr,sEdit);
			//              if(emp == null && theAppMgr.isOnLine())
			//              {
			//                theAppMgr.showErrorDlg(res.getString("Cannot find employee."));
			//                theAppMgr.setSingleEditArea(res.getString("Enter employee ID"),
			//                                      "EMPLOYEE_SALE", theAppMgr.NO_MASK);
			//                return;
			//              }
			//              else
			//              {
			//                labLayaway.setText(res.getString("EMPLOYEE SALE"));
			//                String bldrName = CMSDiscountMgr.getBuilderName("EMPLOYEE");
			//                theAppMgr.buildObject(this, "DISCOUNT", bldrName, "EMPLOYEE");
			//
			//                enterItem();
			//                return;
			//              }
			//
			//      }
			else if (Command.equals("CONSULTANT_LINE_ID")) {
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
		return ("$Revision: 1.1.2.15.4.5 $");
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
			if (theTxn.getEmployeeSale() && dis instanceof CMSEmployeeDiscount) {
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
			if (theTxn.getEmployeeSale() && dis instanceof CMSEmployeeDiscount) {
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
		enterItem();
		if (Command.equals("LOYALTY_ITEM")) {
			// MP (105) For Loyalty Card
			Loyalty loyalty = new Loyalty();
			if (obj != null) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						enterItem();
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
			//___Tim: BUG 1706: In case customer has been marked deleted, popup error msg
			if (loyalty.getCustomer() == null) {
				theAppMgr.showErrorDlg(res.getString("Customer does not exist!!"));
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						enterItem();
					}
				});
				return;
			}
			try {
				//theTxn.setCustomer(loyalty.getCustomer());
				//Fix 1840 :Deposit total amount is not calculated on the SALES screen.
				setCustomer((CMSCustomer)loyalty.getCustomer());
				theTxn.update();
				this.setLoyaltyPointsStatus(); // To display loyalty points after adding customer
				System.out.println("The associated customer : " + loyalty.getCustomer().getFirstName());
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
				System.out.println("****** Message is: " + cmsCustomerMsg.getMessage());
				CMSCustomer cmsCust = (CMSCustomer) theTxn.getCustomer();
				System.out.println(" ****response= " + cmsCust.getCustomerMessage().getResponse());
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
		if (Command.equals("DISCOUNT")) {
			if (obj == null) {
				//        selectDiscount();
				enterItem();
				this.showButtons();
			} else {
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
						POSLineItem lineItem = pnlLineItem.getSelectedLineItem();
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
						}
					}
					if (!(dis instanceof CMSEmployeeDiscount)) {
						if (!dis.isLineItemDiscount && !dis.isSubTotalDiscount && !dis.isMultiDiscount)
							theTxn.addSettlementDiscount(dis);
						else
							theTxn.addDiscount(dis);
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
		} else if (Command.equals("ITEM")) {
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
					//remove postandpack
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
						//mb: added for pre-sale
						case PRE_SALE_OPEN:
							lineItem = theTxn.addPresaleItem(item);
							break;
						//ks: added for consignment
						case CONSIGNMENT_OPEN:

							//System.out.println("INITIALSALE: " + item.getRetailPrice());
							lineItem = theTxn.addConsignmentItem(item);
							break;
						case RESERVATIONS_OPEN:
							lineItem = theTxn.addReservationItem(item);
							break;

						default:
							lineItem = theTxn.addSaleItem(item);
					}
					//Employee Discount for Armani
					if (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") != null && (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") instanceof CMSEmployeeDiscount)) {
						lineItem.addDiscount((CMSEmployeeDiscount) theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT"));
					}
					// apply quantity
					if (qtyToApply > 1) {
						lineItem.setQuantity(new Integer(qtyToApply));
					}
					lineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());
					//added by Anjana for promotion code
					Iterator<String> promotionCode=item.getPromotionIds();
					if(promotionCode!=null){
					while(promotionCode.hasNext()){
						String id = promotionCode.next();
						CMSPromotionBasedPriceEngine promotion=new CMSPromotionBasedPriceEngine(null);
						IPromotion prom=promotion.getPromotionById(id);
						try {
							lineItem.setPromotionCode(prom.getId());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							  theAppMgr.showErrorDlg(res.getString("Error in loading the promotions for item"));
							
						}
					
					}
					}
					
					else{
						lineItem.setPromotionCode(null);
					}
					//promotion code ends
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
									suggestDlg.setSuggestions(theAppMgr, (RelatedItem[]) suggestions.toArray(new RelatedItem[suggestions.size()]));
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
				CMSItemWrapper itemWrapper = (CMSItemWrapper) obj;
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
							case CONSIGNMENT_OPEN: {
								lineItem = theTxn.addConsignmentSpecificItem(item);
								break;
							}
							default:
								lineItem = theTxn.addSaleSpecificItem(item);
						}
						lineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());
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
								/*              if (item.getItem().getId().equals(new ConfigMgr("item.cfg").getString(
								 "NOTONFILE.BASE_ITEM"))) { //if Not-on-File Item{
								 lineItem = theTxn.addReservationMiscItem(item);
								 }else {
								 lineItem = theTxn.addSaleMiscItem(item);
								 }*/
								if (!((CMSItem) item.getItem()).isDeposit()) {
									lineItem = theTxn.addReservationMiscItem(item);
								} else {
									lineItem = theTxn.addSaleMiscItem(item);
								}
								break;
							case CONSIGNMENT_OPEN:
								if (item.getItem().getId().equals(new ConfigMgr("item.cfg").getString("NOTONFILE.BASE_ITEM"))) { //if Not-on-File Item
									lineItem = theTxn.addConsignmentMiscItem(item);
									break;
								}
							default:
								lineItem = theTxn.addSaleMiscItem(item);
						}
					}
					lineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());
					addLineItem(lineItem);
					if (lineItem instanceof SaleLineItem && !item.getItem().getId().equals(new ConfigMgr("item.cfg").getString("NOTONFILE.BASE_ITEM"))) {
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
					System.out.println("InitialSaleApplet_JP.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
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
						System.out.println("InitialSaleApplet_JP.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
					}
					pnlLineItem.repaint();
					updateLabel();
					showButtons();
					enterItem();
				}
			} else {
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
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
						System.out.println("InitialSaleApplet_JP.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
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
						System.out.println("InitialSaleApplet_JP.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
					}
					pnlLineItem.repaint();
					updateLabel();
					showButtons();
					enterItem();
				}
			} else if (sAction.equals("REMOVE_DIS")) {
				if (pnlLineItem.getSelectedLineItem() == null) {
					theAppMgr.showErrorDlg(res.getString("Please select the item first"));
					return;
				}
				System.out.println("REMOVE DISCOUNT TRIGGERED for " + pnlLineItem.getSelectedLineItem().getItemRetailPrice());
				Discount[] allDiscounts = pnlLineItem.getSelectedLineItem().getDiscountsArray();
				theTxn.getDiscountsArray();
				GenericChooserRow[] discountRows = new GenericChooserRow[allDiscounts.length];
				for (int i = 0; i < discountRows.length; i++) {
					discountRows[i] = new GenericChooserRow(new String[] { ((CMSDiscount) allDiscounts[i]).getType() + " : " + (((CMSDiscount) allDiscounts[i]).getPercent()) * 100 + "%" },
							allDiscounts[i]);
				}
				GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, discountRows, new String[] { (res.getString("DISCOUNTS")) });
				dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
				dlg.setVisible(true);
				if (dlg.isOK()) {
					CMSDiscount discountSelected = (CMSDiscount) dlg.getSelectedRow().getRowKeyData();
					try {	
					//__Tim:1809 Cannot remove discount: code is reorganised and addtional checks added for multidiscounts.					
						pnlLineItem.repaint();
						if (discountSelected.isLineItemDiscount) {
							boolean lineItemFound = false;
							boolean isEmployeeDiscount = false;
							if(discountSelected instanceof CMSEmployeeDiscount) { 
							//if((discountSelected.getReason().equals("EMPLOYEE DISCOUNT"))&& (discountSelected.getDiscountCode().length()==0)){
								isEmployeeDiscount = true;
								pnlLineItem.getSelectedLineItem().removeDiscount(discountSelected);
								System.out.println("employee discount present "+isEmployeeDiscount);
							}else{							
								POSLineItem posLineItems[] = theTxn.getLineItemsArray();
								for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
									Discount[] lineItemDiscounts = posLineItems[i].getDiscountsArray();
									if(pnlLineItem.getSelectedLineItem() != posLineItems[i]){
										System.out.println("when pnlLineItem and posLineItems are not same");
										continue;
									}
									for (int j = 0; j < lineItemDiscounts.length; j++) {
										if (discountSelected == ((CMSDiscount) lineItemDiscounts[j]) && (!discountSelected.getIsMultiDiscount())) {
											System.out.println("inside if when discount selected for line item and multidiscounts ");
											continue;
										}
										if (((CMSDiscount) lineItemDiscounts[j]).getDiscountCode().equals(discountSelected.getDiscountCode())) {
											System.out.println("discount link present "+((CMSDiscount) lineItemDiscounts[j]).getDiscountCode().equals(discountSelected.getDiscountCode()));
											lineItemFound = true;
											break;
										}
										}
									}
								if ((!lineItemFound)&& (discountSelected.getDiscountCode().length()!=0)) {
									//1809:Delete only lineitemDiscounts which do not have associated discounts .
									System.out.println("deleting discount from txn  and the line item");
									pnlLineItem.getSelectedLineItem().removeDiscount(discountSelected);
									theTxn.removeDiscount(discountSelected);								
								} else {
									System.out.println("not deleting discount from txn.. its linked");
									theAppMgr.showErrorDlg(res.getString("Cannot delete Linked/Multi Discount"));
								}
							}
						}
						pnlLineItem.repaint();
						updateLabel();
						setLoyaltyPointsStatus();
					} catch (BusinessRuleException bpe) {
						bpe.printStackTrace();
						}
					} else {
					System.out.println("dlg NOT OK");
					}
				} else {
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				System.out.println("discount bldrname "+bldrName+" Event "+anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
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
						System.out.println("InitialSaleApplet_JP.appButtonEvent(REMOVE)->theTxn.resetLayawayDeposit();"); //theTxn.resetLayawayDeposit();
					}
					pnlLineItem.repaint();
					updateLabel();
					showButtons();
					enterItem();
				}
			} else if (sAction.equals("REMOVE_DIS")) {
				System.out.println("REMOVE TXN DISCOUNT TRIGGERED for ");
				Discount[] allDiscounts = theTxn.getSettlementDiscountsArray();
				System.out.println("discount array " + theTxn.getDiscountsArray().length);
				System.out.println("settdiscount array " + theTxn.getSettlementDiscountsArray().length);
				GenericChooserRow[] discountRows = new GenericChooserRow[allDiscounts.length];
				for (int i = 0; i < discountRows.length; i++) {
					discountRows[i] = new GenericChooserRow(new String[] { ((CMSDiscount) allDiscounts[i]).getType() + ":" + ((CMSDiscount) allDiscounts[i]).getPercent() }, allDiscounts[i]);
				}
				GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, discountRows, new String[] { (res.getString("DISCOUNTS")) });
				dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
				dlg.setVisible(true);
				if (dlg.isOK()) {
					CMSDiscount discountSelected = (CMSDiscount) dlg.getSelectedRow().getRowKeyData();
					try {
						System.out.println("inside ok when discountSelected");
						POSLineItem posLineItems[] = theTxn.getLineItemsArray();
						for (int i = 0; posLineItems != null && i < posLineItems.length; i++) {
							posLineItems[i].removeDiscount(discountSelected);
						}
						theTxn.removeSettlementDiscount(discountSelected);
						pnlLineItem.repaint();
						updateLabel();
						setLoyaltyPointsStatus();
					} catch (BusinessRuleException bpe) {
						bpe.printStackTrace();
					}
				} else {
					System.out.println("dlg NOT OK");
				}
			} else {
				String bldrName = CMSDiscountMgr.getBuilderName(anEvent.getActionCommand());
				theAppMgr.buildObject(this, "DISCOUNT", bldrName, sAction);
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
			}
			//      else
			//      {
			//        if (theTxn.getCustomer() != null)
			//        {
			//          buildTaxExemptId(sAction);
			//        }
			//        else {
			//          theAppMgr.showErrorDlg("Please add customer to continue");
			//        }
			//
			//      }
			else if (sAction.equals("REMOVE_TAX_EXEMPT")) {
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

	}

	private void enterRSVODepositAmount() {
		this.enterRSVODepositAmount(null);
	}

	private void enterRSVODepositAmount(ArmCurrency initialAmount) {
		//BUG 1695: updated to take currency input for display 
	ArmCurrency depositBalance = null;
		if (theTxn.getCustomer() != null) {
			depositBalance = ((CMSCustomer) theTxn.getCustomer()).getCustomerBalance();
		}
		if (depositBalance == null)
			depositBalance = new ArmCurrency(0.0d);
		String sPrompt = res.getString("Enter Amount") + " ( " + res.getString("Previous Balance") + depositBalance.formattedStringValue() + " )";
		//    theAppMgr.setSingleEditArea(sPrompt, "RSVO_DEPOSIT", theAppMgr.CURRENCY_MASK);
		theAppMgr.setSingleEditArea(sPrompt, "RSVO_DEPOSIT", initialAmount == null ? null : initialAmount.stringValue(), CMSMaskConstants.QUANTIFIED_CURRENCY_MASK);
	}

	/**
	 * put your documentation comment here
	 */
	private void enterAdjustDepositAmount() {
		theAppMgr.setSingleEditArea(res.getString("Enter new deposit amount"), "ADJUST_DEPOSIT", theAppMgr.CURRENCY_MASK);
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
//			Fix for issue# 1912 Broken Transaction Discount was removed at transaction level
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
			//___Tim: Bug 1702: commented out to allow quantity change for alteration
			/*if (quantity.intValue() > 1) {
			 theAppMgr.showErrorDlg(res.getString(
			 "Quantity should not be more than 1 for alteration items"));
			 anEvent.consume();
			 return;
			 }*/
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
			if (pnlLineItem.getSelectedLineItem() != null && pnlLineItem.getSelectedLineItem().isMiscItem()
					&& !pnlLineItem.getSelectedLineItem().getItem().getId().equalsIgnoreCase(new ConfigMgr("item.cfg").getString("NOTONFILE.BASE_ITEM"))) { //if Not-on-File Item)
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
			JButton[] btns = new JButton[usedDiscount.size() + 3];
			for (int x = 0; x < usedDiscount.size(); x++) {
				String label = CMSDiscountMgr.getLabel((String) usedDiscount.get(x));
				btns[x] = new JButton(label);
				btns[x].setActionCommand((String) usedDiscount.get(x));
			}
			Theme theme = theAppMgr.getTheme();
			btns[usedDiscount.size()] = new JButton();
			btns[usedDiscount.size()].setText(HTML.formatLabeltoHTML(res.getString("Remove Discount"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size()].setActionCommand("REMOVE_DIS");

			btns[usedDiscount.size() + 1] = new JButton();
			btns[usedDiscount.size() + 1].setText(HTML.formatLabeltoHTML(res.getString("Remove All Discounts"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size() + 1].setActionCommand("REMOVE");

			btns[usedDiscount.size() + 2] = new JButton(res.getString("Previous"));
			btns[usedDiscount.size() + 2].setActionCommand("BACK");
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
			JButton[] btns = new JButton[usedDiscount.size() + 3];
			for (int x = 0; x < usedDiscount.size(); x++) {
				String label = CMSDiscountMgr.getLabel((String) usedDiscount.get(x));
				btns[x] = new JButton(label);
				btns[x].setActionCommand((String) usedDiscount.get(x));
			}
			Theme theme = theAppMgr.getTheme();
			btns[usedDiscount.size()] = new JButton();
			btns[usedDiscount.size()].setText(HTML.formatLabeltoHTML(res.getString("Remove Discount"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size()].setActionCommand("REMOVE_DIS");

			btns[usedDiscount.size() + 1] = new JButton();
			btns[usedDiscount.size() + 1].setText(HTML.formatLabeltoHTML(res.getString("Remove All Discount"), theme.getButtonFont(), theme.getButtonTextColor()));
			btns[usedDiscount.size() + 1].setActionCommand("REMOVE");
			btns[usedDiscount.size() + 2] = new JButton(res.getString("Previous"));
			btns[usedDiscount.size() + 2].setActionCommand("BACK");
			theAppMgr.setButtons("TRANS_DISCOUNT", btns);
			theAppMgr.addStateObject("ARM_DISCOUNT_TYPE", "TRANS_DISCOUNT");
			selectDiscount();
		} else if (sAction.equals("OVERRIDE_EMPLOYEE_DISCOUNT")) {
			if (!theTxn.getEmployeeSale()) {
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
		}
		//ks: Handled in InitialSaleApplet_State
		/*    else if (sAction.equals("CONSIGNMENTS")) {
		 sAction_global = sAction;
		 vecMenus.add(sAction);
		 CMSApplet.theAppMgr.showMenu(MenuConst.CONSIGNMENTS, super.theOpr);
		 }
		 */
		// Manpreet S Bawa  - 04/11/2005 -- Moved to InitialPreSalesApplet
		//    else if (sAction.equals("PRESALES"))
		//    {
		//
		//      sAction_global = sAction;
		//      vecMenus.add(sAction);
		//      theAppMgr.setSingleEditArea(res.getString("Select option."));
		//      CMSApplet.theAppMgr.showMenu(MenuConst.PRESALES, super.theOpr);
		//    }
		//    else if (sAction.equals("RESERVATIONS")) {
		//      sAction_global = sAction;
		//      vecMenus.add(sAction);
		//      CMSApplet.theAppMgr.showMenu(MenuConst.RESERVATIONS, super.theOpr);
		//
		//    }
		//    else if (sAction.equals("MANAGEMENT")) {
		//            sAction_global = sAction;
		//            vecMenus.add(sAction);
		//           //CMSApplet.theAppMgr.showMenu(MenuConst.MGMT_MENU, super.theOpr);
		//           //theAppMgr.fireButtonEvent("MGMT_MENU_HIDDEN");
		//
		//    }
		//    else if (sAction.equals("TRANS_MGMT")) {
		//            vecMenus.add(sAction);
		//            sAction_global = sAction;
		//      CMSApplet.theAppMgr.showMenu(MenuConst.TRANS_MGMT, super.theOpr);
		//
		//    }
		//    else if (sAction.equals("SYSTEM_UTILS")) {
		//                  sAction_global = sAction;
		//                  vecMenus.add(sAction);
		//      CMSApplet.theAppMgr.showMenu(MenuConst.SYSTEM_UTILS, super.theOpr);
		//
		//    }
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
			// Fix for issue 1868
			try {
				if (isReservationOpenMode() && theTxn.getReservationTransaction().getDepositAmount().equals(new ArmCurrency(0.0d))) {
				    ReservationOpenDialog dlg = new ReservationOpenDialog(CMSApplet.theAppMgr.getParentFrame(), CMSApplet.theAppMgr);
			        dlg.setVisible(true);
			        if (!dlg.isYes()) {
			        	anEvent.consume();
			        	return;
			        }
				}
			} catch (Exception e) {
				System.out.println("Exception processing the deposit amount.");
				e.printStackTrace();
			}
			//    	if (((CMSStore) theStore).getPreferredISOCountry().equals("JP")) {
			if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
				if (isCustomerRequiredForDiscounts() || (saleHasAlterationItems() && theTxn.getCustomer() == null) || saleHasRedeemableItems())
					theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
				else
					theAppMgr.addStateObject("ARM_CUST_REQUIRED", "FALSE");
			} else if ((saleHasAlterationItems() && theTxn.getCustomer() == null) || saleHasRedeemableItems() || theTxn.getEmployeeSale()) {
				System.out.println("saleHasRedeemableItems() : " + saleHasRedeemableItems());
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
			
			//Added by Vivek Mishra for PCR : Dummy Customer Association
			if (theTxn.getCustomer() != null) {
				// The below statement is required
				// for the PCR: Dummy and default customer not allowed
				// for Reservation, Deposit and Credit transactions.
				theAppMgr.addStateObject("TXN_CUSTOMER", theTxn.getCustomer());
			}//end
			// totalPressed = false;
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

			//MSB -  01/13/2005
			//Return to transaction needs to go two steps back.
			//so do PREV twice, this ensures
			//all the references of menu are
			// removed from vector and flow remains consistent
			// through out.
			//Fixed defect# 1409
			//appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0, "PREV", 0));
			//appButtonEvent(new CMSActionEvent(anEvent.getSource(), 0, "PREV", 0));
			cardLayout.show(cardPanel, "LINE_ITEM");

			//      //this.remove(pnlDetailLineItem);
			//      //this.getContentPane().add(pnlLineItem, BorderLayout.CENTER);

			//
			//PM: Issue #1607 - The vector contains the history of the screens browsed which used by the previous button
			//                  Emptying the vector resets the history
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
			
			//Added by Vivek Mishra for Browser Integration PCR : JAPAN
			
			ConfigMgr websitesCfg = new ConfigMgr("employee.cfg");

	    	   // Get browser
	    	   String browser = websitesCfg.getString("BROWSER");
		       
	    	   //Get url
		       String url = websitesCfg.getString("URL");
		    	
		    	
		    	if(!((url.equals(""))|| browser.equals("")))
		    	{
		    	try {
		    		System.out.println("cmd /c start " + browser + " " + url);
		    		Runtime.getRuntime().exec("cmd /c start " + browser + " " + url);
		    	} catch (Exception ee) {
		    		System.err.println("Failed to launch " + browser + " " + url);
		    		ee.printStackTrace();
		    	}
		    	}

			//End
		    	
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
		try {
			//        if(theTxn.getTaxExemptId() == null || theTxn.getTaxExemptId().length() == 0){
			TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore) theTxn.getStore(), (CMSStore) theTxn.getStore(), theTxn.getProcessDate());
			//        }
		} catch (Exception ex) {
			System.out.println("Exceptioin applying tax ");
			ex.printStackTrace();
		}
		sTotalAmount = theTxn.getCompositeNetAmount().formattedStringValue() + " / " + theTxn.getCompositeTotalAmountDue().formattedStringValue();
		//        }
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
		this.setLoyaltyPointsStatus();
		if (theTxn.getCustomer() != null) {
			pnlHeader.updateAmtToReward();
		}
		updateMerchandiseCountLabel();
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
		if (pnlLineItem.isRowSelected()) {
			POSLineItem theLine = pnlLineItem.getSelectedLineItem();
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
				//Ruchi : Fix for 1837 In case of  "cancel reservation", broken txn is occurred.
				if(iAppletMode == RESERVATIONS_CLOSE) {
				 if (pnlLineItem.getSelectedLineItem() instanceof SaleLineItem) {
					 CMSSaleLineItem saleLineItem = (CMSSaleLineItem) pnlLineItem.getSelectedLineItem();
						if (saleLineItem.getReservationLineItem() != null) {
							theAppMgr.showErrorDlg(res.getString("Reserved SaleLine Items can not be deleted."));
							return;
						}
					}
					if (pnlLineItem.getSelectedLineItem() instanceof ReturnLineItem) {
						CMSReturnLineItem returnLineItem = (CMSReturnLineItem) pnlLineItem.getSelectedLineItem();
						 if (returnLineItem.getReservationLineItem() != null) {
							theAppMgr
									.showErrorDlg(res.getString("Reserved ReturnLine Items can not be deleted."));
							return;
						}
					}
				}
				pnlLineItem.deleteSelectedLineItem();
				pnlLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
				LightPoleDisplay.getInstance().itemReturned(theLine, theTxn.getCompositeTotalAmountDue().formattedStringValue());
				suggestDlg.clearSuggestions();
				suggestDlg.setVisible(false);
				//remove postandpack
				theTxn.setPostAndPack(false, new ArmCurrency(0.0));
				// Update to set the loyalty points again
				this.setLoyaltyPointsStatus();
				if (theTxn.getCustomer() != null) {
					pnlHeader.updateAmtToReward();
				}
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
		}
		updateLabel();
		enterItem();
	}

	/**
	 */
	private void addLineItem(POSLineItem lineItem) {
		// update table - it is important to be quick, so remove selection listener
		pnlLineItem.getTable().getSelectionModel().removeListSelectionListener(selectionResetListener);
		setLineConsultant(lineItem);
		pnlLineItem.addLineItem(lineItem);
		pnlLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
		// update light pole
		LightPoleDisplay.getInstance().itemSold(lineItem, theTxn.getCompositeTotalAmountDue().formattedStringValue());
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
		setLoyaltyPointsStatus();
		labLayaway.setForeground(new Color(0, 128, 255));
		labLayaway.setText(res.getString("PRE-SALE OPEN"));
		theAppMgr.showMenu(MenuConst.PRE_SALE_OPEN, theOpr);
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
		setLoyaltyPointsStatus();
		labLayaway.setForeground(new Color(0, 140, 255));
		labLayaway.setText(res.getString("CONSIGNMENT OPEN"));
		theAppMgr.showMenu(MenuConst.CONSIGNMENTS_IN, theOpr);
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

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
		setLoyaltyPointsStatus();
		theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	/**
	 */
	private void initSaleButtons() { // pos_sale
		//dp: remove state object to come out of the no-receipt return mode
		theAppMgr.removeStateObject("RETURN_SALE");
		if (iAppletMode != this.RESERVATIONS_CLOSE) {
			iAppletMode = SALE_MODE;
			theAppMgr.addStateObject("TXN_MODE", new Integer(iAppletMode));
		}
		//    theAppMgr.addStateObject("TXN_MODE", new Integer(iAppletMode));
		labDeposit.setVisible(false);
		fldDeposit.setVisible(false);
		labLayaway.setForeground(new Color(0, 0, 175));
		// Set the Foreground & set the lable for showing Employee visible.
		//lblEMployeeSale.setForeground(new Color(0, 0, 175));
		//Ruchi : Fix for issue 1835 AFTER EMPLOYEE SALES
		lblEMployeeSale.setVisible(false);
		if (theTxn.getEmployeeSale()) {
			CMSEmployee cmsEmp = theTxn.getEmployee();
			labLayaway.setText(res.getString("EMPLOYEE SALE"));
			// Employee ID & Name.
			lblEMployeeSale.setVisible(true);
			this.lblEMployeeSale.setText(cmsEmp.getExternalID() + " - " + cmsEmp.getFirstName() + " " + cmsEmp.getLastName());
		} else {
			labLayaway.setText(res.getString("SALE"));
		}
		setLoyaltyPointsStatus();
		theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr);
		cardLayout.show(cardPanel, "LINE_ITEM");
	}

	private void setLoyaltyPointsStatus() {
		double currpoints = 0.0;
		if (theTxn.getCustomer() != null && theTxn.getLoyaltyCard() != null) {
			// Applicable only to sale transactions
//			if ((theTxn.getTransactionType().startsWith("SALE") || theTxn.getTransactionType().startsWith("RETN") || theTxn.getTransactionType().startsWith("PEND"))) {
			if (theTxn.getTransactionType().indexOf("SALE") != -1
	                || theTxn.getTransactionType().indexOf("RETN") != -1 
	                || theTxn.getTransactionType().indexOf("PEND") != -1) {
				java.text.NumberFormat numFor = java.text.NumberFormat.getInstance();
				currpoints = theTxn.getLoyaltyPoints();
				lblTxnLoyalty.setVisible(true);
				lblTxnLoyalty.setText(res.getString("Points you've got today") + ": " + numFor.format(currpoints));

			} else {
				lblTxnLoyalty.setVisible(false);
				lblTotalLoyaltyPoints.setVisible(false);
				lblTxnLoyalty.setText("");
				lblTotalLoyaltyPoints.setText("");
			}
		} else {
			lblTxnLoyalty.setVisible(false);
			lblTotalLoyaltyPoints.setVisible(false);
			lblTxnLoyalty.setText("");
			lblTotalLoyaltyPoints.setText("");
		}
		pointsAndDepositPanel.repaint();
	}

	/**
	 */
	private void initReturnButtons() { // pos_return
		iAppletMode = RETURN_MODE;
		labDeposit.setVisible(false);
		fldDeposit.setVisible(false);
		lblEMployeeSale.setVisible(false);
		setLoyaltyPointsStatus();
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
		setLoyaltyPointsStatus();
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

	private boolean isReservationCloseMode() {
		return (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == RESERVATIONS_CLOSE);
	}

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
			//----------- START --------
			// Deposit Calculation isn't needed for US
			// as there is no way to refund balance deposit amount
			// - MSB (11/14/2005)

			//      if (lineItmDeposit != null) {
			//        lineItmDeposit.setManualUnitPrice(new ArmCurrency(0.0d));
			//        try {
			//          TaxUtilities.applyTax(theAppMgr, theTxn, (CMSStore) theTxn.getStore()
			//                                , (CMSStore) theTxn.getStore(),
			//                                theTxn.getProcessDate());
			//        }
			//        catch (Exception ex) {
			//          theAppMgr.showExceptionDlg(ex);
			//        }
			//
			//      }
			//      amtTxn = theTxn.getCompositeTotalAmountDue();
			//      amtDeposit = amtRSVODeposit.absoluteValue();
			//      if (amtDeposit.greaterThan(amtTxn)) {
			//        amtDeposit = amtTxn;
			//      }
			//      // If all the reserved items are
			//      // being returned.
			//      if (theTxn.getSaleLineItemsArray().length < 1
			//          &&
			//          theTxn.getLineItemsArray().length ==
			//          theTxn.getReturnLineItemsArray().length
			//          ) {
			//        amtDeposit = amtRSVODeposit.absoluteValue();
			//      }
			//-----------------END---------------------//
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
		MiscItemTemplate[] miscItemTemplates = new MiscItemTemplate[oldmiscItemTemplates.length];
		ConfigMgr mgr = new ConfigMgr("item.cfg");
		String misckeys = mgr.getString("MISC_ITEM_KEYS");
		StringTokenizer strTok = new StringTokenizer(misckeys, ",");
		int k = 0;
		while(strTok.hasMoreTokens()) {
			String misckey = strTok.nextToken();
			for (int j = 0; j < oldmiscItemTemplates.length; j++) {
				if (oldmiscItemTemplates[j].getKey().equals(misckey))
					miscItemTemplates[k] = oldmiscItemTemplates[j];
			}
			k++;
		}
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
		pointsAndDepositPanel = new JPanel();
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
		//  Tim
		lblTxnLoyalty.setText("");
		lblTxnLoyalty.setFont(theAppMgr.getTheme().getMessageFont());
		lblTxnLoyalty.setForeground(new Color(200, 0, 50));
		lblTotalLoyaltyPoints.setText("");
		lblTotalLoyaltyPoints.setFont(theAppMgr.getTheme().getMessageFont());
		lblTotalLoyaltyPoints.setForeground(new Color(0, 0, 175));
		pointsAndDepositPanel.setLayout(new GridBagLayout());
		//    pointsAndDepositPanel.setForeground(new Color(192,192,192));
		pointsAndDepositPanel.setBackground(theAppMgr.getBackgroundColor());
		pointsAndDepositPanel.add(lblTxnLoyalty, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
		pointsAndDepositPanel.add(labDeposit, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		pointsAndDepositPanel.add(fldDeposit, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		//EndTim
		pnlSouth.add(labLayaway, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 10, 0));
		pnlSouth.add(this.lblEMployeeSale, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		//Tim (07/07/06) - Loyalty labels
		pnlSouth.add(pointsAndDepositPanel, new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
		//EndTim
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
		//    pnlSouth.add(labDeposit
		//        , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST
		//        , GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		//    pnlSouth.add(fldDeposit
		//        , new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
		//        , GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
			//      if (lineItems[iCtr] instanceof SaleLineItem)
			//      {
			//        CMSSaleLineItem cmsSaleLineItem = (CMSSaleLineItem) lineItems[iCtr];
			//        POSLineItemDetail details[] = cmsSaleLineItem.getLineItemDetailsArray();
			//        for (int iDtCtr = 0; iDtCtr < details.length; iDtCtr++) {
			//          CMSSaleLineItemDetail cmsSaleLineItemDetail = (CMSSaleLineItemDetail)
			//              details[iDtCtr];
			//          AlterationLineItemDetail alterationLineDetail[] =
			//              cmsSaleLineItemDetail.getAlterationLineItemDetailArray();
			//          if (alterationLineDetail != null) {
			//            return true;
			//          }
			//        }
			//
			//      }
			//
			//      else {
			//        continue;
			//      }
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
			if (sKey == null || !(sKey.equalsIgnoreCase("CUSTOMER_OWNED") || sKey.equalsIgnoreCase("NOTONFILE")))
				return false;
		}
		//    CMSSaleLineItem saleLineItem  saleLineItem = (CMSSaleLineItem) pnlLineItem.
		//          getSelectedLineItem();
		//    if (saleLineItem !=null && saleLineItem.isMiscItem()) {
		//      String sKey = saleLineItem.getMiscItemId();
		//      if (sKey == null) {
		//        return false;
		//      }
		//      if (sKey.equalsIgnoreCase("CUSTOMER_OWNED")) {
		//        return true;
		//      }
		//      else {
		//        return false;
		//      }
		//    }
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
		//___Tim: Bug 1702: commented out to allow quantity change for alteration
		/*|| (pnlLineItem.getSelectedLineItem().isMiscItem() && pnlLineItem.getSelectedLineItem()
		 .getMiscItemId().equalsIgnoreCase("ALTERATION"))*/
		) {
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
		try {
			Discount priceLineDiscount = null;
			//POSLineItem lineItem = (POSLineItem)pnlLineItem.getSelectedLineItem();
			if (theTxn.getEmployeeSale() && dis instanceof CMSEmployeeDiscount) {
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
				/*double dExtendedSellingPrice = 0.0d;
				 try
				 {
				 dExtendedSellingPrice = getExtendedSellingPrice(lineItem).doubleValue();
				 }
				 catch (CurrencyException e) {}
				 double disPercent = (dExtendedSellingPrice - dis.getAmount().doubleValue()) /
				 dExtendedSellingPrice;
				 dis.doSetPercent(disPercent);
				 dis.doSetAmount(new ArmCurrency(0.0d));
				 dis.setIsDiscountPercent(true);
				 */
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
			lineItem.addDiscount(dis);
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
				if (theTxn.getEmployeeSale() && discount instanceof CMSEmployeeDiscount) {
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
		if (theTxn.getEmployeeSale() && discount instanceof CMSEmployeeDiscount) {
			Discount discounts[] = theTxn.getDiscountsArray();
			if (discounts != null) {
				for (int j = 0; j < discounts.length; j++) {
					if (discounts[j] instanceof CMSEmployeeDiscount) {
						System.out.println("Removing employee discount !!!!!!!!!!!!!!");
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
						System.out.println("##########  Loyalty Card used = " + custLoyalties[i].getLoyaltyNumber() + "  ##########");
						return;
					}
				} catch (BusinessRuleException ex) {
					//ex.printStackTrace();
				}
			}
			setLoyalty(null);
			System.out.println("##########  No valid Loyalty cards could be found for this store.  ##########");
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
				if (posLineItem.isMiscItem() && posLineItem.getItem().getId().equals(new ConfigMgr("item.cfg").getString("NOTONFILE.BASE_ITEM"))) //make sure item is not not-on-file
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

	private void buildMiscItem(MiscItemTemplate miscItemTemplate) {
		if (miscItemTemplate != null) {
			if (miscItemTemplate.getKey().equals("STORE_VALUE_CARD")) {
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
			} else if (miscItemTemplate.getKey().equals("NOTONFILE")) {
				String builderStr = new ConfigMgr("item.cfg").getString("NOTONFILE.BUILDER");
				theAppMgr.buildObject(this, "MISC_ITEM", builderStr, miscItemTemplate);
			} else {
				theAppMgr.buildObject(this, "ITEM", itemBuilder, miscItemTemplate);
			}
		}
	}

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
	      System.out.println("storeId " + thisStoreId);
	      System.out.println("txnCust " + (customer == null));
	    }
	  }

}
