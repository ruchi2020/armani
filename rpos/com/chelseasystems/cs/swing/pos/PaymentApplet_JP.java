/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.att
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*validate
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 9    | 08-01-2005 | Vikram    |    620    | Customer profile is required for check txn.  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 8    | 08-01-2005 | Vikram    |    619    | Cash sale over $10,000 must prompt for IRS   |
 |      |            |           |           | form 8300                                    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 7    | 07-05-2005 | Vikram    |    332    | Buy Back options as Refund type - House      |
 |      |            |           |           | Acccount and Credit Note results in error    |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 6    | 06-28-2005 | Vikram    | 341       |Could not accept gift cards offline           |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 06-27-2005 | Vikram    | 285       |Customer VIP discount message change          |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 06-10-2005 | Vikram    | 61        |Added check for VIP discount                  |
 --------------------------------------------------------------------------------------------
 | 3    | 06-07-2005 | Vikram    |    120    |  "Change Type" displayed incorrectly for     |
 |      |            |           |           |  "No Receipt Return".                        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 06-06-2005 | Vikram    |    70     |  Customer required if tender is Gift Card /  |
 |      |            |           |           |  Credit Note. Customer is checked after      |
 |      |            |           |           |  payment details are build.                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 06-02-2005 | Megha     |    94     |  Payment/Credit Card: Authorization response |
 |      |            |           |           |  message returned from host not displayed    |
 |      |            |           |           |                                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 */

package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CustomerCreditCard;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.dm.display.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.park.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.register.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.authorization.bankcard.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.layaway.*;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.register.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.swing.menu.*;
import com.chelseasystems.cs.swing.model.*;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cs.txnnumber.*;
import com.chelseasystems.cs.txnposter.*;
import com.chelseasystems.cs.tax.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cs.paidout.CMSCashDropPaidOut;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.authorization.ISDServiceManager;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.loyalty.LoyaltyEngine;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import com.chelseasystems.cs.payment.Diners;
import com.chelseasystems.cs.payment.JCBCreditCard;

import java.text.NumberFormat;

import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.chelseasystems.cs.util.CreditAuthUtil;
import com.chelseasystems.cs.swing.builder.CMSPremioDiscountBldr;
import com.chelseasystems.cs.swing.builder.CreditCardBldrUtil;

import java.text.SimpleDateFormat;

import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.payment.CMSPaymentCode;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;

/**
 */
public class PaymentApplet_JP extends CMSApplet
//public class PaymentApplet extends JApplet {
{
	static boolean isVatEnabled = new ConfigMgr("vat.cfg").getString("VAT_ENABLED").equalsIgnoreCase("TRUE");

	private boolean doNotAddDeletedPremioDiscRefund = false;
	// instance members used to prompt user with an initial payment
	private String INITIAL_PAYMENT;
	private Payment anInitialPayment;
	private Payment thePayment;
	private String strUSECARD;
	private boolean shouldTxnPostWhenBalanceIsZero;
	private CMSPaymentTransactionAppModel theTxn = null;
	private boolean changeDue = false;
	private boolean stubRequired = false;
	private boolean cancelTxnPressed = false;
	private boolean prevPressed = false;
	boolean isPostComplete = false;
	int action = -1;
	POSHeaderPanel_JP pnlHeader = new POSHeaderPanel_JP();
	JLabel TranDisc$Lbl = new JLabel();
	JLabel SalesTaxLbl = new JLabel();
	JLabel SalesTax$Lbl = new JLabel();
	JLabel TotalAmt$Lbl = new JLabel();
	JLabel TranDiscDetailLbl = new JLabel();
	JLabel TotalTaxDetailLbl = new JLabel();
	JLabel SubTot$Lbl = new JLabel();
	JLabel SubTotLbl;
	JLabel AmtTnd$Lbl = new JLabel();
	JLabel AmtDue$Lbl = new JLabel();
	JLabel lblDue = new JLabel();
	PaymentTableModel model = new PaymentTableModel();
	JCMSTable tblPayment = new JCMSTable(model, JCMSTable.SELECT_ROW);
	DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	private JPanel pnlCards;
	private GiftReceiptPanel pnlGiftReceipt;
	private CardLayout card;
	private boolean flag;
	private String txnNum = null;
	private boolean bool;
	private String sAction_global;
	private boolean isAutoChange = false;
	private String result;
	private ConfigMgr cfgMgr;
	private Payment change;
	//  private ScrollableToolBarPanel screen;
	private boolean isBackFromCustomerApplet;
	double rewardPointsLevel = 0.0;
	private boolean postingInProgress = false;
	//For Credit Card Status Code
	String StatusString = null;

	JPanel pointsAndDepositPanel;
	JLabel lblTxnLoyalty; // label for "Points you've got today"

	private String sLoyalityString; // Loyality string to fetch value from Resource

	//Initialize the applet
	public void init() {
		try {
			ConfigMgr mgr = new ConfigMgr("payment.cfg");
			INITIAL_PAYMENT = mgr.getString("INITIAL_PAYMENT");
			if (INITIAL_PAYMENT != null && INITIAL_PAYMENT.trim().length() > 0) {
				//                anInitialPayment = PaymentMgr.getPayment(INITIAL_PAYMENT);
				anInitialPayment = CMSPaymentMgr.getPayment(INITIAL_PAYMENT);
			}
			shouldTxnPostWhenBalanceIsZero = mgr.getString("SHOULD_TXN_POST_WHEN_BALANCE_ZERO").equalsIgnoreCase("true");
			jbInit();
			ConfigMgr loyaltyconfig = new ConfigMgr("loyalty.cfg");
			String loyaltyRewardRatio = loyaltyconfig.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
			String loyaltyAmount = loyaltyconfig.getString("LOYALTY_REWARD_AMOUNT");
			rewardPointsLevel = (new Double(loyaltyRewardRatio)).doubleValue() * (new Double(loyaltyAmount)).doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Component initialization
	private void jbInit() throws Exception {
		lblTxnLoyalty = new JLabel();
		JPanel pnlCenter = new JPanel();
		JPanel pnlSummary = new JPanel();
		JPanel pnlSummaryLt = new JPanel();
		JPanel pnlSummaryMid = new JPanel();
		JPanel pnlSummaryRt = new JPanel();
		JLabel SubTotDetailLbl = new JLabel();
		pointsAndDepositPanel = new JPanel();
		SubTotLbl = new JLabel();
		JLabel TranDiscLbl = new JLabel();
		JLabel TotalLbl = new JLabel();
		JPanel pnlSouth = new JPanel(new GridBagLayout());
		JPanel pnlPayments = new JPanel();
		JPanel pnlTendered = new JPanel();
		JPanel pnlDue = new JPanel();
		JLabel lblAmt = new JLabel();
		this.setSize(844, 605);
		this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
		this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
		this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		pnlHeader.setPreferredSize(new Dimension(10, 84));
		pnlSummary.add(pnlSummaryLt, null);
		pnlSummaryLt.add(SubTotLbl, null);
		pnlSummaryLt.add(TranDiscLbl, null);
		pnlSummaryLt.add(SalesTaxLbl, null);
		pnlSummaryLt.add(TotalLbl, null);
		pnlSummary.add(pnlSummaryMid, null);
		pnlSummaryMid.add(SubTotDetailLbl, null);
		pnlSummaryMid.add(TranDiscDetailLbl, null);
		pnlSummaryMid.add(TotalTaxDetailLbl, null);
		pnlSummary.add(pnlSummaryRt, null);
		pnlSummaryRt.add(SubTot$Lbl, null);
		pnlSummaryRt.add(TranDisc$Lbl, null);
		pnlSummaryRt.add(SalesTax$Lbl, null);
		pnlSummaryRt.add(TotalAmt$Lbl, null);
		pnlCenter.setLayout(new BorderLayout());

		//  deo
		lblTxnLoyalty.setText("");
		lblTxnLoyalty.setFont(theAppMgr.getTheme().getMessageFont());
		lblTxnLoyalty.setForeground(new Color(200, 0, 50));
		pointsAndDepositPanel.setLayout(new GridBagLayout());
		pointsAndDepositPanel.setBackground(theAppMgr.getBackgroundColor());
		pointsAndDepositPanel.add(lblTxnLoyalty, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));

		//deo

		pnlCards = new JPanel();
		card = new CardLayout();
		pnlCards.setLayout(card);
		pnlCards.add(pnlPayments, "payments");
		pnlCenter.add(pnlCards, BorderLayout.CENTER);

		pnlSouth.add(pointsAndDepositPanel, new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pointsAndDepositPanel.setOpaque(false);

		pnlSouth.add(pnlTendered, new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnlTendered.add(lblAmt, null);
		pnlTendered.add(AmtTnd$Lbl, null);
		pnlTendered.setOpaque(false);

		pnlSouth.add(new JLabel(), new GridBagConstraints(0, 1, 2, 1, 0.1, 0.1, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		pnlSouth.add(pnlDue, new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnlDue.add(lblDue, null);
		pnlDue.add(AmtDue$Lbl, null);
		pnlDue.setOpaque(false);

		lblDue.setText(res.getString("ADD_TENDER_AMOUNT_DUE") + ":");
		lblDue.setHorizontalAlignment(4);
		lblDue.setPreferredSize(new Dimension(130, 22));
		lblDue.setForeground(new Color(0, 0, 175));
		lblDue.setFont(theAppMgr.getTheme().getPromptAreaFont());
		String file = theAppMgr.getTheme().getFileName();
		ConfigMgr cfgMgr = new ConfigMgr(file);
		String strFont_amtDue = cfgMgr.getString("FONT.AMOUNT_DUE");
		Font font_amountDue = getFont(strFont_amtDue);
		lblDue.setFont(font_amountDue);
		AmtDue$Lbl.setText("0.00");
		AmtDue$Lbl.setHorizontalAlignment(4);
		AmtDue$Lbl.setPreferredSize(new Dimension(100, 22));
		AmtDue$Lbl.setForeground(new Color(0, 0, 175));
		AmtDue$Lbl.setFont(font_amountDue);
		//AmtDue$Lbl.setFont(theAppMgr.getTheme().getPromptAreaFont());
		pnlDue.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
		AmtTnd$Lbl.setText("0.00");
		AmtTnd$Lbl.setHorizontalAlignment(4);
		AmtTnd$Lbl.setPreferredSize(new Dimension(100, 22));
		AmtTnd$Lbl.setForeground(new Color(0, 0, 175));
		String strFont_amtTnd = cfgMgr.getString("FONT.AMOUNT_DUE");
		Font font_amtTnd = getFont(strFont_amtTnd);
		AmtTnd$Lbl.setFont(font_amtTnd);
		AmtTnd$Lbl.setForeground(new Color(0, 0, 175));
		//AmtTnd$Lbl.setFont(theAppMgr.getTheme().getPromptAreaFont());
		lblAmt.setText(res.getString("Amount Tendered") + ":");
		lblAmt.setFont(font_amtTnd);
		lblAmt.setForeground(new Color(0, 0, 175));
		//lblAmt.setFont(theAppMgr.getTheme().getPromptAreaFont());
		pnlTendered.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
		pnlPayments.setBackground(Color.white);
		pnlPayments.setLayout(new BorderLayout());
		pnlSouth.setPreferredSize(new Dimension(10, 48));
		pnlSouth.setBackground(theAppMgr.getBackgroundColor());
		pnlSouth.setLayout(new GridLayout(2, 1));
		pnlSummaryRt.setBackground(Color.white);
		pnlSummaryMid.setBackground(Color.white);
		pnlSummaryLt.setBackground(Color.white);
		pnlSummaryMid.setLayout(new GridLayout(4, 1));
		pnlSummaryLt.setLayout(new GridLayout(4, 1, 5, 0));
		pnlSummaryRt.setLayout(new GridLayout(4, 1));
		TotalLbl.setText(" " + res.getString("Total"));
		TotalLbl.setMaximumSize(new Dimension(300, 20));
		TotalLbl.setBackground(Color.white);
		TotalLbl.setPreferredSize(new Dimension(124, 15));
		TotalLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		SalesTaxLbl.setText(" " + res.getString("Sales Tax"));
		SalesTaxLbl.setMaximumSize(new Dimension(300, 20));
		SalesTaxLbl.setBackground(Color.white);
		SalesTaxLbl.setPreferredSize(new Dimension(124, 15));
		SalesTaxLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		TranDiscLbl.setText(" " + res.getString("Markdown") + " / " + res.getString("Discount"));
		TranDiscLbl.setMaximumSize(new Dimension(300, 20));
		TranDiscLbl.setBackground(Color.white);
		TranDiscLbl.setPreferredSize(new Dimension(124, 15));
		TranDiscLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		SubTotLbl.setText(" " + res.getString("Retail"));
		SubTotLbl.setMaximumSize(new Dimension(300, 20));
		SubTotLbl.setBackground(Color.white);
		SubTotLbl.setPreferredSize(new Dimension(124, 15));
		SubTotLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		TotalTaxDetailLbl.setHorizontalAlignment(0);
		TotalTaxDetailLbl.setMaximumSize(new Dimension(300, 20));
		TotalTaxDetailLbl.setBackground(Color.white);
		TotalTaxDetailLbl.setPreferredSize(new Dimension(124, 15));
		TotalTaxDetailLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		TotalTaxDetailLbl.setText("");
		TranDiscDetailLbl.setText("");
		TranDiscDetailLbl.setHorizontalAlignment(0);
		TranDiscDetailLbl.setMaximumSize(new Dimension(300, 20));
		TranDiscDetailLbl.setBackground(Color.white);
		TranDiscDetailLbl.setPreferredSize(new Dimension(124, 15));
		TranDiscDetailLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		SubTotDetailLbl.setHorizontalAlignment(0);
		SubTotDetailLbl.setMaximumSize(new Dimension(300, 20));
		SubTotDetailLbl.setBackground(Color.white);
		SubTotDetailLbl.setPreferredSize(new Dimension(124, 15));
		SubTotDetailLbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		TotalAmt$Lbl.setText("0.00");
		TotalAmt$Lbl.setHorizontalAlignment(4);
		TotalAmt$Lbl.setMaximumSize(new Dimension(300, 20));
		TotalAmt$Lbl.setBackground(Color.white);
		TotalAmt$Lbl.setPreferredSize(new Dimension(124, 15));
		TotalAmt$Lbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		SalesTax$Lbl.setText("0.00");
		SalesTax$Lbl.setHorizontalAlignment(4);
		SalesTax$Lbl.setMaximumSize(new Dimension(300, 20));
		SalesTax$Lbl.setBackground(Color.white);
		SalesTax$Lbl.setPreferredSize(new Dimension(124, 15));
		SalesTax$Lbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		TranDisc$Lbl.setText("0.00");
		TranDisc$Lbl.setHorizontalAlignment(4);
		TranDisc$Lbl.setMaximumSize(new Dimension(300, 20));
		TranDisc$Lbl.setBackground(Color.white);
		TranDisc$Lbl.setPreferredSize(new Dimension(124, 15));
		TranDisc$Lbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		SubTot$Lbl.setText("");
		SubTot$Lbl.setHorizontalAlignment(4);
		SubTot$Lbl.setMaximumSize(new Dimension(300, 20));
		SubTot$Lbl.setBackground(Color.white);
		SubTot$Lbl.setPreferredSize(new Dimension(124, 15));
		SubTot$Lbl.setFont(theAppMgr.getTheme().getTextFieldFont());
		pnlSummary.setPreferredSize(new Dimension(10, 80));
		pnlSummary.setBackground(Color.white);
		pnlSummary.setBorder(BorderFactory.createEtchedBorder());
		pnlSummary.setLayout(new GridLayout(1, 3, 5, 0));
		pnlCenter.add(pnlSummary, BorderLayout.NORTH);
		pnlPayments.add(tblPayment.getTableHeader(), BorderLayout.NORTH);
		pnlPayments.add(tblPayment, BorderLayout.CENTER);
		tblPayment.setAppMgr(theAppMgr);
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		tblPayment.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tblPayment.addComponentListener(new java.awt.event.ComponentAdapter() {

			/**
			 * @param e
			 */
			public void componentResized(ComponentEvent e) {
				tblPayment_componentResized(e);
			}
		});
		tblPayment.addMouseListener(new java.awt.event.MouseAdapter() {

			/**
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				tblPayment_mouseClicked(e);
			}
		});
	}

	//Start the applet
	public void start() {
		doNotAddDeletedPremioDiscRefund = false;
		// Just to be safe, resetting the flag to false everytime Applet is started
		postingInProgress = false;
		boolean customerAdded = false;
		PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
		if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
			try {
				CMSCustomer txnCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
				theAppMgr.removeStateObject("TXN_CUSTOMER");
				if (theTxnPOS instanceof CMSCompositePOSTransaction)
					((CMSCompositePOSTransaction) theTxnPOS).setCustomer(txnCust);
				customerAdded = true;
				LightPoleDisplay.getInstance().displayMessage(txnCust.getFirstName(), txnCust.getLastName());
			}
			//Added by Vivek Mishra for PCR : Dummy Customer Association start
			catch (BusinessRuleException bre) {
				theAppMgr.showErrorDlg(bre.getMessage());
				SwingUtilities.invokeLater(new Runnable() {

					/**
					 * put your documentation comment here
					 */
					public void run() {
						theAppMgr.fireButtonEvent("PREV");
					}
				});
				return;
			}//End 
			catch (Exception e) {
				theAppMgr.removeStateObject("TXN_CUSTOMER");
			}
		}
		/*if (theTxnPOS instanceof CMSCompositePOSTransaction) {
		 try {
		 TaxUtilities.applyTax(theAppMgr, (CMSCompositePOSTransaction)theTxnPOS
		 , (CMSStore)theTxnPOS.getStore(), (CMSStore)theTxnPOS.getStore()
		 , theTxnPOS.getProcessDate());
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
		 }*/
		LightPoleDisplay.getInstance().stopDefaultDisplay();
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		cfgMgr = new ConfigMgr("payment.cfg");
		isAutoChange = new Boolean(cfgMgr.getString("AUTO_CHANGE")).booleanValue();
		changeDue = false;
		isPostComplete = false;
		cancelTxnPressed = false;
		//if(theAppMgr.getCallingApplet() instanceof Customer )
		isBackFromCustomerApplet = false;
		prevPressed = false;
		model.clear();
		card.show(pnlCards, "payments");
		theTxn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
		// if re-rentry mode, set attribute
		if (theAppMgr.getStateObject("RE_ENTER") != null) {
			String ticket = (String) theAppMgr.getStateObject("RE_ENTER");
			try {
				theTxn.setHandWrittenTicketNumber(ticket);
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
		} else {
			// Just in case of receipt with no receipt,
			// Ask operator to adjust premio refund
			CMSCompositePOSTransaction cpt = null;
			if (theTxn instanceof CMSCompositePOSTransactionAppModel) {
				//posTran = (CMSCompositePOSTransaction)theTxn.getPaymentTransaction();
				cpt = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
				if (cpt != null && cpt.getLoyaltyCard() != null) {
					POSLineItem[] pli = (POSLineItem[]) cpt.getReturnLineItemsArray();
					boolean premioRefundAlertFlag = false;
					for (int i = 0; i < pli.length; i++) {
						if (pli[i] instanceof ReturnLineItem && ((ReturnLineItem) pli[i]).isMiscReturn() && !LineItemPOSUtil.isDeposit(pli[i].getMiscItemId())) {
							Enumeration enumer = ((ReturnLineItem) pli[i]).getLineItemDetails();
							while(enumer.hasMoreElements()) {
								CMSReturnLineItemDetail returnLineItemDtl = (CMSReturnLineItemDetail) enumer.nextElement();
								if (returnLineItemDtl.getReservationLineItemDetail() != null || returnLineItemDtl.getConsignmentLineItemDetail() != null
										|| returnLineItemDtl.getPresaleLineItemDetail() != null) {
									premioRefundAlertFlag = true;
									break;
								}
							}
							if (premioRefundAlertFlag)
								break;
							else if (!premioRefundAlertFlag && i == (pli.length - 1)) {
								theAppMgr.showErrorDlg(res.getString("If required, please enter Premio Refund" + " using 'Adjust Premio Refund' button on Payment screen."));
							}
						}
					}
				}
			}
		}
		//Seed the model with the payments...
		Payment[] payments = theTxn.getPaymentsArray();
		for (int idx = 0; idx < payments.length; idx++) {
			model.addPayment(payments[idx]);
			model.fireTableDataChanged();
		}
		model.firstPage();
		//initButtons(payMgr.CREDIT);
		CMSCompositePOSTransaction posTran = null;
		if (theTxn instanceof CMSCompositePOSTransactionAppModel) {
			//posTran = (CMSCompositePOSTransaction)theTxn.getPaymentTransaction();
			posTran = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
			if (posTran != null) {
				if ((posTran.getCustomer() != null && posTran.getLoyaltyCard() == null)
						|| (posTran.getCustomer() != null && posTran.getLoyaltyCard() != null && !posTran.getLoyaltyCard().getCustomer().equals(theTxn.getCustomer()))) {
					// Set the default loyalty card
					setCustomerLoyalty(posTran);
				}
			}
		}

		if (theAppMgr.getStateObject("PAYMENT") != null) {
			bool = true;
		} else {
			bool = false;
		}
		if (theTxn.getCustomerAppModel().getCustomer() == null) {
			flag = true;
		} else {
			flag = false;
		}
		theAppMgr.setSingleEditArea(res.getString("Select tender"));
		// Start Issue # 996
		strUSECARD = (String) theAppMgr.getStateObject("USE_CARD_ON_FILE");
		// End Issue # 996

		SubTotLbl.setText(" " + res.getString(theTxn.getPaymentScreenRetailLabel()));
		String giveCustomerVIPDiscount = (String) theAppMgr.getStateObject("ARM_ADDED_CUSTOMER");
		if (theTxn.getCustomer() != null) {
			if (customerAdded && ((CMSCustomer) theTxn.getCustomer()).getVIPDiscount() > 0) {
				NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMaximumFractionDigits(1);
				theAppMgr.showErrorDlg(res.getString("Customer has VIP discount: ") + nf.format(((CMSCustomer) theTxn.getCustomer()).getVIPDiscount()));
			}
			theAppMgr.removeStateObject("ARM_ADDED_CUSTOMER");
		}
		//VM: Check for CUST_NEEDED_PAYMENT object for customer requirement
		Object custNeededPaymentObj = theAppMgr.getStateObject("CUST_NEEDED_PAYMENT");
		if (custNeededPaymentObj != null && custNeededPaymentObj instanceof Payment && theTxn.getCustomer() != null
		//       && theTxn.getCustomer().getId() != null
		//       && theTxn.getCustomer().getId().trim().length() != 0
		) {
			objectEvent("PAYMENT", custNeededPaymentObj);
		}
		theAppMgr.removeStateObject("CUST_NEEDED_PAYMENT");
		//    screen = (ScrollableToolBarPanel) AppManager.getCurrent().getMainFrame().
		//        getAppToolBar();
		//    if (screen != null) {
		//      screen.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
		//          0));
		//    }
		// Start Issue # 996
		if (strUSECARD == null) {
			System.out.println("Normal Payment Flow");
		} else {
			if (theTxn.getCustomerAppModel().getCustomer() == null && strUSECARD != null && strUSECARD.equalsIgnoreCase("TRUE")) {
				invokeViewCCEvent("VIEW_CREDIT_CARD");
			} else if (theTxn.getCustomerAppModel().getCustomer() != null && strUSECARD != "" && strUSECARD.equalsIgnoreCase("TRUE")) {
				invokeUseCOFEvent("USE_CARD_ON_FILE");
			}
		}
		if (theTxnPOS instanceof CMSCompositePOSTransaction) {
			try {
				TaxUtilities.applyTax(theAppMgr, (CMSCompositePOSTransaction) theTxnPOS, (CMSStore) theTxnPOS.getStore(), (CMSStore) theTxnPOS.getStore(), theTxnPOS.getProcessDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		selectPayment();
		updateLabels();
		initHeaders();
		initValues();
		// Issue # 996
	}

	/**
	 * put your documentation comment here
	 */
	private void createTxnNumber() {
		try {
			// set ID
			if ((theTxn.getPaymentTransaction().getId() == null) || (theTxn.getPaymentTransaction().getId().equals(""))) {
				txnNum = CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr, (CMSStore) theStore, (CMSRegister) theAppMgr.getGlobalObject("REGISTER"));
				theTxn.getPaymentTransaction().setId(txnNum);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	//Stop the applet
	public void stop() {
		if ((new ConfigMgr("JPOS_peripherals.cfg").getString("MSR_DEVICE_TYPE")).equals("INGENICO_JPOS")) {
			ET1000JPOSForm.getInstance().displayLogo();
		}
		if (!isPostComplete) {
			if (!prevPressed) {
				//Added by Vivek Mishra for PCR : Dummy Customer Association
				if (theTxn != null)
				//end
				{
				System.out.println("calling app model to print cancel receipt");
				theTxn.printCancelReceipt(theAppMgr);
				}
			}
		}
	}

	//Destroy the applet
	public void destroy() {
	}

	//Get Applet screen name
	public String getScreenName() {
		return (res.getString("Add Tender"));
	}

	//Get Applet information
	public String getVersion() {
		return ("$Revision: 1.2 $");
	}

	/**
	 */
	private void initValues() {
		TranDiscDetailLbl.setText("");
		/*
		 * Payment Applet currently cannot handle displaying more than one discount
		 * while the business objects do allow this.  For this implementation,
		 * just take the last discount.
		 */
		if (theTxn.getDiscountsArray().length > 0) {
			Discount dis = theTxn.getDiscountsArray()[theTxn.getDiscountsArray().length - 1];
			if (dis != null) {
				TranDiscDetailLbl.setText(DiscountMgr.getLabel(dis.getType()));
			}
		}
		/*
		 * the following logic is valid only if a tax exempt is at the whole
		 * transaction level, not the item level
		 *
		 * following code to display tax exempt id is moved to POSHeaderPanel
		 */
		//      if (theTxn.getTaxExemptId().length() > 0 || theTxn.getRegionalTaxExemptId().length() > 0) {
		//         // special regional tax code
		//         StringBuffer buf = new StringBuffer();
		//         if (theTxn.getTaxExemptId().length() > 0) {
		//            buf.append(res.getString(theTxn.getTaxLabel()));
		//         }
		//         if (theTxn.getRegionalTaxExemptId().length() > 0) {//((CMSStore)theStore).usesRegionalTaxCalculations()
		//            if (buf.length() > 0) {
		//               buf.append("/");
		//            }
		//            buf.append(res.getString(theTxn.getRegionalTaxLabel()));
		//         }
		//         buf.append(" ");
		//         buf.append(res.getString("Exempt ID"));
		//         buf.append(": ");
		//         if (theTxn.getTaxExemptId().length() > 0) {
		//            buf.append(theTxn.getTaxExemptId());
		//         }
		//         if (theTxn.getRegionalTaxExemptId().length() > 0) {//((CMSStore)theStore).usesRegionalTaxCalculations()
		//            if (theTxn.getTaxExemptId().length() > 0) {
		//               buf.append("/");
		//            }
		//            buf.append(theTxn.getRegionalTaxExemptId());
		//         }
		//         TotalTaxDetailLbl.setText(buf.toString());
		//      }
		//      else {
		//         TotalTaxDetailLbl.setText("");
		//      }
		SubTot$Lbl.setText(theTxn.getCompositeRetailAmount().formattedStringValue());
		TotalAmt$Lbl.setText(theTxn.getCompositeTotalAmountDue().formattedStringValue());
		TranDisc$Lbl.setText(theTxn.getCompositeReductionAmount().formattedStringValue());
		// special regional tax code
		if (((CMSStore) theStore).usesRegionalTaxCalculations()) {
			//            SalesTaxLbl.setText(" " + res.getString(((CMSStore)theStore).getTaxLabel())
			SalesTaxLbl.setText(" " + res.getString("Sales Tax") + "/" + res.getString(((CMSStore) theStore).getRegionalTaxLabel()));
			SalesTax$Lbl.setText(" " + (theTxn.getCompositeTaxAmount() == null ? new ArmCurrency(0).formattedStringValue() : theTxn.getCompositeTaxAmount().formattedStringValue()) + "/"
					+ theTxn.getCompositeRegionalTaxAmount().formattedStringValue());
		} else {
			//            SalesTaxLbl.setText(" " + res.getString(((CMSStore)theStore).getTaxLabel()));
			SalesTaxLbl.setText(" " + res.getString("Sales Tax"));
			SalesTax$Lbl.setText((theTxn.getCompositeTaxAmount() == null ? new ArmCurrency(0).formattedStringValue() : theTxn.getCompositeTaxAmount().formattedStringValue()));
		}
		if (isVatEnabled) {
			if (theTxn instanceof CMSCompositePOSTransactionAppModel) {
				CMSCompositePOSTransaction posTran = (CMSCompositePOSTransaction) theTxn.getPaymentTransaction();
				if (posTran.isPostAndPack()) {
					try {
						SalesTaxLbl.setText(" " + res.getString("Post and Pack Charge / VAT Refund"));
						TotalTaxDetailLbl.setText(posTran.getPostAndPackChargeAmount().formattedStringValue() + res.getString(" / ") + posTran.getCompositeVatAmount().formattedStringValue());
						SalesTax$Lbl.setText(posTran.getPostAndPackChargeAmount().subtract(posTran.getCompositeVatAmount()).formattedStringValue());
					} catch (CurrencyException ce) {
						theAppMgr.showExceptionDlg(ce);
					}
				} else {
					SalesTaxLbl.setText("");
					SalesTax$Lbl.setText("");
					TotalTaxDetailLbl.setText("");
				}
			} else {
				SalesTaxLbl.setText("");
				SalesTax$Lbl.setText("");
				TotalTaxDetailLbl.setText("");
			}
		}
		// set amount on label and lightpole
		AmtDue$Lbl.setText(theTxn.getCompositeTotalAmountDue().formattedStringValue());
		LightPoleDisplay.getInstance().displayMessage(AmtDue$Lbl.getText(), "");
	}

	/**
	 */
	private void initHeaders() {
		try {
			pnlHeader.setAppMgr(theAppMgr);
			if (theTxn.getPaymentTransaction() instanceof CMSMiscCollection) {
				pnlHeader.setProperties((CMSCustomer) ((CMSMiscCollection) theTxn.getPaymentTransaction()).getCustomer(), (CMSEmployee) theOpr, null);
			}
			// MP: In case of PaidOut, no customer associated just the Employee.
			else if (theTxn.getPaymentTransaction() instanceof PaidOutTransaction) {
				pnlHeader.setProperties(null, (CMSEmployee) theOpr, null);
			} else {
				pnlHeader.setProperties((CMSCustomer) theTxn.getCustomer(), (CMSEmployee) theOpr, (CMSEmployee) theTxn.getConsultant(), (CMSStore) theStore, theTxn.getTaxExemptId(),
						theTxn.getRegionalTaxExemptId());
			}
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	private String getValueWithNoDecimal(double val) {
		return (new ArmCurrency(val)).formattedStringValue();
	}

	/**
	 * put your documentation comment here
	 * @param Command
	 * @param Value
	 */
	public void editAreaEvent(String Command, ArmCurrency Value) {
		if (Command.equals("NEW_PREMIO_REFUND_AMOUNT")) {
			double loyaltyAmount = Double.parseDouble(new ConfigMgr("loyalty.cfg").getString("LOYALTY_REWARD_AMOUNT"));

			// Check value
			if ((new Double(LoyaltyEngine.rewdRedemptionRatio)).doubleValue() != 0) {
				double loyaltyMultiple = loyaltyAmount;

				if ((Value.doubleValue() % loyaltyMultiple) > 0) {
					theAppMgr.showErrorDlg("Please enter multiple of " + getValueWithNoDecimal(loyaltyMultiple));
					return;
				}
			}

			// Now that we have verified value, lets get the transaction changed.
			CMSCompositePOSTransaction ccpt = (CMSCompositePOSTransaction) theTxn.getPaymentTransaction();
			Payment[] payments = ccpt.getPaymentsArray();
			CMSPremioDiscount cpd = null;
			for (int i = 0; i < payments.length; i++) {
				if (payments[i] instanceof CMSPremioDiscount) {
					cpd = (CMSPremioDiscount) payments[i];
					break;
				}
			}

			boolean newCpd = false;
			if (cpd == null) {
				try {
					cpd = (CMSPremioDiscount) PaymentMgr.getPayment("PREMIO_DISCOUNT");
				} catch (Exception e) {
					e.printStackTrace();
				}
				CMSStore store = (CMSStore) theAppMgr.getGlobalObject("STORE");
				CMSRegister reg = (CMSRegister) theAppMgr.getGlobalObject("REGISTER");
				cpd.setStoreId(store.getId());
				cpd.setRegisterId(reg.getId());
				newCpd = true;
			}

			try {
				double loyaltyMaxValue = ((int) ((theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount()).absoluteValue().doubleValue() + cpd.getAmount().absoluteValue().doubleValue()) / loyaltyAmount))
						* loyaltyAmount;
				if (Value.doubleValue() > loyaltyMaxValue) {
					theAppMgr.showErrorDlg("Premio discount can not exceed " + getValueWithNoDecimal(loyaltyMaxValue));
					return;
				} else if (Value.doubleValue() < loyaltyAmount) {
					theAppMgr.showErrorDlg("Premio discount can not be lower than " + getValueWithNoDecimal(loyaltyAmount));
					return;
				}
			} catch (Exception ce) {
			}

			try {
				/*MD*/System.out.println("==>> Payment lines: " + payments.length);
				if (!newCpd) {
					ccpt.removePayment(cpd);
					model.removeRowInModel(cpd);
				}
				/*MD*/System.out.println("==>> Payment lines after remove: " + ccpt.getPaymentsArray().length);
				cpd.setAmount(new ArmCurrency(Value.doubleValue() * -1));
				cpd.setRedeemPoints(String.valueOf(ccpt.getRedeemablePoints()));
				ccpt.addPayment(cpd);
				ccpt.resetRedeemableAmount();
				ccpt.setRedeemableAmount(Value.doubleValue() * -1);
				model.addPayment(cpd);
				model.fireTableDataChanged();
				this.updateLabels();
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.fireTableDataChanged();
			selectPayment();
			return;
		} else if (Command.equals("AMOUNT")) {
			//if (validateChangeAmount(Value)) {
			String paymentType = thePayment.getTransactionPaymentName();
			String paymentTypeView = thePayment.getGUIPaymentName();
			if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView, Value)) {
				thePayment.setAmount(Value);
				String storeId = ((CMSStore) theAppMgr.getGlobalObject("STORE")).getId();
				String registerId = ((CMSRegister) theAppMgr.getGlobalObject("REGISTER")).getId();
				thePayment.setJournalKey(CreditAuthUtil.getJournalKey(storeId, registerId));
				objectEvent("PAYMENT", thePayment);
				thePayment = null;
			}
		}
	}

	/**
	 * @param Command
	 * @param Value
	 */
	public void editAreaEvent(String Command, String Value) {
		//if (Command.equals("CREDIT_CARD")) {      // credit card swipe date
		//   String Builder = PaymentMgr.getPaymentBuilder(Command);
		//   System.out.println("the builder is: " + Builder);
		//   theAppMgr.buildObject("PAYMENT", Builder, Value);
		//}
		// above code moved to builder
		if (Command.equals("SUSPEND_COMMENT")) {
			ParkFileServices ParkServices = new ParkFileServices();
			try {
				theTxn.getPaymentTransaction().removeAllPayments();
				theTxn.setSuspendComment(Value);
				theTxn.printSuspendedReceipt(theAppMgr);
				ParkServices.suspend(theTxn.getPaymentTransaction(), Value);
				isPostComplete = true; // needed to fake out isHomeAllowed()
				cancelTxnPressed = false;
				theAppMgr.fireButtonEvent("CANCEL_TXN");
			} catch (Exception e) {
				theAppMgr.showExceptionDlg(e);
			}
		}
		if (Command.equals("MANUAL")) {
			int row = tblPayment.getSelectedRow();
			if (row < 0) {
				theAppMgr.showErrorDlg(res.getString("You must first select a payment."));
				selectOption();
				return;
			}
			Payment pay = model.getPayment(row);
			if (pay instanceof CreditCard) {
				CreditCard cc = (CreditCard) pay;
				if (!cc.isAuthRequired()) {
					theAppMgr.showErrorDlg(res.getString("This credit card has already been authorized."));
					selectOption();
					return;
				}
				cc.setManualOverride(Value);
				model.fireTableDataChanged();
				selectOption();
				return;
			}
			if (pay instanceof BankCheck) {
				BankCheck chk = (BankCheck) pay;
				if (!chk.isAuthRequired()) {
					theAppMgr.showErrorDlg(res.getString("This check has already been authorized."));
					selectOption();
					return;
				}
				if (chk.getRespStatusCode().equals(Payment.DECLINED) & theAppMgr.isOnLine()) {
					theAppMgr.showErrorDlg(res.getString("This check can not have a manual override."));
					selectOption();
					return;
				}
				chk.setManualOverride(Value);
				model.fireTableDataChanged();
				selectOption();
				return;
			}
			// only the above to type are valid, show error dlg if
			// here
			theAppMgr.showErrorDlg(res.getString("The payment does not require a manual override."));
			selectOption();
		}
	}

	/**
	 */
	private void initReceiptButtons() {
		theAppMgr.showMenu(MenuConst.PRINT_RECEIPT, theOpr);
	}

	/**
	 * @param type
	 */
	private void initButtons(int type) {
		//        String[] pay = PaymentMgr.getPayments(type, theTxn.getPaymentTransaction());

		boolean hideAdjustPremioRefundButton = true;
		// Check payments to see if there is one with Premio discount and -ve value
		if (theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) {
			CMSCompositePOSTransaction ccpt = (CMSCompositePOSTransaction) theTxn.getPaymentTransaction();

			// Requirement changed.  Always need to adjust in refund scenario.  Same button
			// to add, adjust PremioDiscount.
			if (ccpt.getReturnLineItemsArray().length > 0) {
				// Check if txn has premio card associated.
				if (ccpt.getLoyaltyCard() != null)
					hideAdjustPremioRefundButton = false;
			}

			//        Payment[] payments = ccpt.getPaymentsArray();
			//        for (int i=0; i < payments.length; i++) {
			//            if (payments[i] instanceof CMSPremioDiscount) {
			//                CMSPremioDiscount pd = (CMSPremioDiscount)payments[i];
			//                /*MD*/ System.out.println("==>> pd.getAmount(): " + pd.getAmount());
			//                if (pd.getAmount().doubleValue() < 0.0) {
			//                    hideAdjustPremioRefundButton = false;
			//                }
			//            }
			//        }
		}

		String[] pay = CMSPaymentMgr.getPayments(type, theTxn.getPaymentTransaction());
		// allow for prev, and delete button, adjust premio refund
		JButton[] btns = new JButton[pay.length + (hideAdjustPremioRefundButton ? 2 : 3)];
		for (int x = 0; x < pay.length; x++) {
			try {
				//                Payment payment = PaymentMgr.getPayment(pay[x]);
				Payment payment = CMSPaymentMgr.getPayment(pay[x]);
				btns[x] = new JButton(HTML.formatLabeltoHTML(res.getString(payment.getGUIPaymentName()), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
				// Issue # 996
				if (pay[x].equalsIgnoreCase("CREDIT_CARD")) {
					btns[x].setActionCommand("VIEW_CREDIT_CARD");
				} else {
					btns[x].setActionCommand(pay[x]);
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
		}
		if (!hideAdjustPremioRefundButton) {
			btns[btns.length - 3] = new JButton(HTML.formatLabeltoHTML(res.getString("Adjust Premio Refund"), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
			btns[btns.length - 3].setActionCommand("ADJUST_PREMIO_REFUND");
		}
		btns[btns.length - 2] = new JButton(HTML.formatLabeltoHTML(res.getString("Delete Payment"), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
		btns[btns.length - 2].setActionCommand("DEL_PAY");
		//btns[btns.length - 2] = new JButton(HTML.formatLabeltoHTML(res.getString("Suspend"),
		//theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
		//btns[btns.length - 2].setActionCommand("SUSPEND");
		btns[btns.length - 1] = new JButton(HTML.formatLabeltoHTML(res.getString("Previous"), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
		btns[btns.length - 1].setActionCommand("PREV");
		theAppMgr.setButtons(btns);
	}

	/**
	 * @param Command
	 * @param obj
	 */
	public void objectEvent(String Command, Object obj) {
		if (Command.equals("PAYMENT")) {
			System.out.println("calling initHeaders start");
			initHeaders();
			System.out.println("calling initHeaders end ");
			try {
				if (obj != null) {
					Payment pay = (Payment) obj;
					if (pay.getPaymentCode() == null || pay.getPaymentCode() == "" || pay.getPaymentCode().trim().length() == 0) {
						String payCode = getPaymentCode(pay.getTransactionPaymentName());
						pay.setPaymentCode(payCode);
					}
					// do not allow payment of 0.00
					if (!pay.getAmount().equals(new ArmCurrency(0.0))) {
						// set the amount if change is due
						if (changeDue) {
							// make it a negative for change
							//theAppMgr.showR
							pay.setAmount(pay.getAmount().multiply(-1));
						}
						try {
							theTxn.addPayment((Payment) obj);
							model.addPayment((Payment) obj);
							//VM: Cash sale over $10,000 must prompt for IRS form 8300
							Payment[] payments = theTxn.getPaymentsArray();
						ArmCurrency totalCashPayment = new ArmCurrency(0.0);
							for (int i = 0; i < payments.length; i++) {
								if (payments[i] instanceof Cash)
									totalCashPayment = totalCashPayment.add(payments[i].getAmount());
							}
							if (CMSPaymentMgr.getIRSAlertAmount(ArtsConstants.PAYMENT_TYPE_CASH) != null
									&& totalCashPayment.doubleValue() >= CMSPaymentMgr.getIRSAlertAmount(ArtsConstants.PAYMENT_TYPE_CASH).doubleValue())
								theAppMgr.showErrorDlg(res.getString("Please have the customer complete IRS form 8300."));
							model.fireTableDataChanged();
							updateLabels();
							theAppMgr.setSingleEditArea(res.getString("Choose change type."));
							LightPoleDisplay.getInstance().paymentDisplay((Payment) obj, theTxn.getCompositeTotalAmountDue());
						} catch (BusinessRuleException ex) {
							theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
						}
					}
				} else {
					theAppMgr.setSingleEditArea(res.getString("Select tender"));
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			} finally {
				selectPayment();
			}
		}
		/**
		 * Added for Issuing Reward
		 */
		if (Command.equals("ISSUERWDCARD")) {
			Loyalty loyaltyCardObj = (Loyalty) theAppMgr.getStateObject("LOYALTYOBJ");
			theAppMgr.removeStateObject("LOYALTYOBJ");
			Loyalty OldObj = loyaltyCardObj;
			// Refresh the loyalty card from database.
			try {
				OldObj = LoyaltyHelper.getLoyalty(theAppMgr, OldObj.getLoyaltyNumber());
			} catch (Exception e) {
				theAppMgr.showErrorDlg(res.getString("Cannot issue reward card. Loyalty card not found."));
				e.printStackTrace();
				return;
			}
			// Check whether there are enough points to issue reward
			if (OldObj.getCurrBalance() < rewardPointsLevel) {
				theAppMgr.showErrorDlg(res.getString("Cannot issue reward card. Loyalty card does not have enough points."));
				return;
			}
			// For Reward Txn
			CMSStore cmsStore = (CMSStore) theAppMgr.getGlobalObject("STORE");
			CMSEmployee cmsEmployee = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
			RewardTransaction rewardTxn = new RewardTransaction(cmsStore);
			try {
				rewardTxn.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr, (CMSStore) theStore, (CMSRegister) theAppMgr.getGlobalObject("REGISTER")));
				rewardTxn.setRegisterId(((CMSRegister) theAppMgr.getGlobalObject("REGISTER")).getId());
				rewardTxn.setSubmitDate(new Date());
				rewardTxn.setProcessDate((Date) theAppMgr.getGlobalObject("PROCESS_DATE"));
				rewardTxn.setTheOperator(cmsEmployee);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ConfigMgr config = new ConfigMgr("loyalty.cfg");
			String loyaltyRewardRatio = config.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
			String loyaltyAmount = config.getString("LOYALTY_REWARD_AMOUNT");
			double points = (new Double(loyaltyRewardRatio)).doubleValue() * (new Double(loyaltyAmount)).doubleValue();
			RewardCard rewardCard = (RewardCard) obj;
			if (rewardCard != null) {
				rewardCard.setLoyalty(OldObj);
				rewardCard.getLoyalty().setLoyaltyNumber(OldObj.getLoyaltyNumber());
				rewardTxn.setRewardCard(rewardCard);
				//loyaltyCardObj.setCurrBalance(loyaltyCardObj.getCurrBalance()-points);
				try {
					if (theTxn.getCustomer() != null)
						rewardTxn.setCustomer(theTxn.getCustomer());
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Points that need to be added
				rewardTxn.setPoints((long) points);
				try {
					rewardTxn.post();

					/* This code added by usha for producing the Reward Transaction RDO */
					PaymentTransactionAppModel appModel = rewardTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
					if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
						String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "RewardTransaction.rdo";
						try {
							ObjectStore objectStore = new ObjectStore(fileName);
							objectStore.write(appModel);
						} catch (Exception e) {
							System.out.println("exception on writing object to blueprint folder: " + e);
						}
					}
					appModel.printReceipt(theAppMgr);
					/* end code addition */

				} catch (Exception e) {
					e.printStackTrace();
				}
				//Need to enter the issued reward Card
				theAppMgr.setSingleEditArea(res.getString("Select Option"));
			}
		}
		//VM: the processObject of DueBillBldr now points to STORE_VALUE_CREDIT_MEMO_PAYMENT for customer requirement
		//Customer profile required for Check transaction also.
		if (Command.equals("STORE_VALUE_CREDIT_MEMO_PAYMENT") || Command.equals("CHECK_PAYMENT")) {
			try {
				if (obj != null) {
					if (obj instanceof Redeemable || obj instanceof Check) {
						PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr.getStateObject("TXN_POS");
						if (txn instanceof CompositePOSTransaction) {
							CompositePOSTransaction theTxnPOS = ((CompositePOSTransaction) txn);
							if (theTxnPOS.getCustomer() == null) {
								theAppMgr.addStateObject("CUST_NEEDED_PAYMENT", obj);
								theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
							} else
								objectEvent("PAYMENT", obj);
						}
						// MP: If Txn is Collection Transaction.
						else if (txn instanceof CMSMiscCollection) {
							CMSMiscCollection theTxnPOS = ((CMSMiscCollection) txn);
							if (theTxnPOS.getCustomer() == null) {
								theAppMgr.addStateObject("CUST_NEEDED_PAYMENT", obj);
								theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
							} else
								objectEvent("PAYMENT", obj);
						}
					}
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
				//ex.printStackTrace();
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param forCredit
	 * @exception Exception
	 */
	private void applyVat(boolean forCredit) throws Exception {
		if (isVatEnabled) {
			if (theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) {
				//if(((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).isPostAndPack())
				CMSValueAddedTaxHelper.applyVAT(theAppMgr, (CMSCompositePOSTransaction) theTxn.getPaymentTransaction(), (CMSStore) theStore, (CMSStore) theStore,
						theTxn.getPaymentTransaction().getProcessDate(), forCredit);
			}
		}
	}

	/**
	 */
	private void selectOption() {
		theAppMgr.setSingleEditArea(res.getString("Select option."));
		theAppMgr.setEditAreaFocus(); // fixes problem when card is swiped before the system is ready
	}

	/**
	 * Method checks for overage / underage for payments. If balance is zero,
	 *    call end of tranaction. If return only, show refund buttons.
	 */

	private boolean hasTwoPremioDiscounts() {
		Payment[] pays = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPaymentsArray();
		if (pays.length < 2) {
			return false;
		}
		boolean firstFound = false;
		for (int i = 0; i < pays.length; i++) {
			if (pays[i] instanceof CMSPremioDiscount) {
				System.out.println("premio found ?? ");
				if (!firstFound) {
					firstFound = true;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	private void selectPayment() {
		try {
		ArmCurrency amt = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
			if (amt.absoluteValue().truncate(0).equals(new ArmCurrency(0.0))) {
				if (shouldTxnPostWhenBalanceIsZero) {

					// calling assignloyalty for adjusting the premioBalance
					if (theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) {
						((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).assignLoyaltyPoints();

						if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).isEvenExchange() && !hasTwoPremioDiscounts()) {
							System.out.println("%%% even exchange");
							changeDue = true;
							boolean evenExchangeWithPremioDiscount = false;
							evenExchangeWithPremioDiscount = forEvenExchange();
							/*SwingUtilities.invokeLater(new Runnable() {
							 public void run() {
							 initButtons(CMSPaymentMgr.REFUND);
							 }
							 });*/
							//System.out.println("%%% evenExchangeWithPremioDiscount = " + evenExchangeWithPremioDiscount);
							// BUG 1688: If premio discount was not used as tender, continue and post txn
							if (evenExchangeWithPremioDiscount) {
								initButtons(CMSPaymentMgr.REFUND);
								return;
							}
						}
					}
					Thread post = new Thread(new Runnable() {

						/**
						 * put your documentation comment here
						 */
						public void run() {
							theAppMgr.setWorkInProgress(true);
							theAppMgr.setSingleEditArea("Posting...");
							theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
							PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, "POST_TXN", 0));
							theAppMgr.setWorkInProgress(false);
							theAppMgr.setEditAreaFocus();
						}
					});
					post.start();
				} else {
					theAppMgr.showMenu(MenuConst.POST, theOpr, theAppMgr.PREV_BUTTON);
					selectOption();
				}
				return;
			}
			if (amt.absoluteValue().truncate(0).greaterThan(new ArmCurrency(0.0)) && amt.truncate(0).lessThan(new ArmCurrency(0.0))) {
				// added by Anand to accomodate NOT posting transaction automatically if Paid-Out Txn
				if (theTxn.getPaymentTransaction() instanceof PaidOutTransaction) {
					selectOption();
					updateLabels();
					if (((PaidOutTransaction) theTxn.getPaymentTransaction()).getType().equalsIgnoreCase("MISC_PAID_OUT"))
						initButtons(CMSPaymentMgr.REFUND);
					else
						initButtons(CMSPaymentMgr.CHANGE);
					changeDue = true;
					return;
				}
				lblDue.setText(res.getString("Change Due") + ":");
				//if (!bool) {
				if (theTxn.getCompositeTotalAmountDue().doubleValue() < 0) {
					theAppMgr.setSingleEditArea(res.getString("Choose refund type."));
				} else if (!isMaxChangeAllowed()) {
					//initButtons(CMSPaymentMgr.CHANGE);
					theAppMgr.setSingleEditArea(res.getString("Choose change type."));
					//Khyati: No auto change if REfund due
				} else {
					if (!changeDue) {
						theTxn.addPayment(change);
						model.addPayment(change);
						model.fireTableDataChanged();
					ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
						if (amtDue.equals(new ArmCurrency(0.0))) {
							if (shouldTxnPostWhenBalanceIsZero) {
								Thread post = new Thread(new Runnable() {

									/**
									 * put your documentation comment here
									 */
									public void run() {
										theAppMgr.setWorkInProgress(true);
										theAppMgr.setSingleEditArea("Posting...");
										theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
										PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, "POST_TXN", 0));
										theAppMgr.setWorkInProgress(false);
										theAppMgr.setEditAreaFocus();
									}
								});
								post.start();
							}
						} else {
							theAppMgr.showMenu(MenuConst.POST, theOpr, theAppMgr.PREV_BUTTON);
							selectOption();
							updateLabels();
							changeDue = true;
							return;
						}
					}
				}
				//}
				if (bool) {
					if (theAppMgr.getStateObject("PAYMENT") != null) {
						SwingUtilities.invokeLater(new Runnable() {

							/**
							 * put your documentation comment here
							 */
							public void run() {
								PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, (String) theAppMgr.getStateObject("PAYMENT"), 0));
								theAppMgr.removeStateObject("PAYMENT");
							}
						});
					}
				}
				if (theTxn.getCompositeTotalAmountDue().doubleValue() < 0) {
					//                    initButtons(PaymentMgr.REFUND);
					System.out.println("############## doing for refund ##############33");
					if (!doNotAddDeletedPremioDiscRefund) {
						forReturnTxn();
					}
					initButtons(CMSPaymentMgr.REFUND);
				} else {
					initButtons(CMSPaymentMgr.CHANGE); // only show if credit
					//                    initButtons(PaymentMgr.CHANGE);             // only show if credit
				}
				changeDue = true;
			} else {
				//PM: Issue #1474
				lblDue.setText(res.getString("AddTender_Amount_Due") + ":");
				//                initButtons(PaymentMgr.PAYMENT);
				initButtons(CMSPaymentMgr.PAYMENT);
				if (anInitialPayment != null && strUSECARD == null) {
					try {
						anInitialPayment.isValidAsPayment(theTxn.getPaymentTransaction());
						// this in invoke later, because the applet may register after the builder if not
						SwingUtilities.invokeLater(new Runnable() {

							/**
							 */
							public void run() {
								//bool will always be true when control returns from the CustomerListApplet
								//flag will always be true when there is NO customer attached to the txn
								if (!bool) {
									PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
								} else {
									if (flag) {
										PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
									} else if (isButtonInList(sAction_global) && theAppMgr.getStateObject("PAYMENT") != null) {
										// for skip condition in CustListApplet to return to PaymentApplet
										PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, (String) theAppMgr.getStateObject("PAYMENT"), 0));
										theAppMgr.removeStateObject("PAYMENT");
									} else {
										PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
									}
								}
							}
						});
					} catch (BusinessRuleException bex) {
						theAppMgr.setSingleEditArea(res.getString("Choose payment type."));
					}
				}
				changeDue = false;
			}
			selectLastRow();
			theAppMgr.setEditAreaFocus();
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * @return
	 */
	private boolean postTransaction() throws BusinessRuleException {
		try {
			// update display manager
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				DisplayManager dm = (DisplayManager) theAppMgr.getGlobalObject("DISPLAY_MANAGER");
				if (dm != null) {
					dm.endTransaction((CompositePOSTransaction) theTxn.getPaymentTransaction());
				}
			}
			
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				CMSCompositePOSTransaction transaction = (CMSCompositePOSTransaction)theTxn.getPaymentTransaction();
				CMSV12Basket cmsV12Basket = transaction.getCmsV12Basket();
				if (cmsV12Basket!=null && transaction.getSaleLineItemsArray()!=null) {
					boolean isBasketTransaction = false;
					POSLineItem lineItem[] = transaction.getSaleLineItemsArray();
					ArrayList<String> itemId = cmsV12Basket.getItemList();
					for (String id : itemId) {
						for (POSLineItem item : lineItem) {
							if(item.getItem().getId().equals(id)) {
								isBasketTransaction = true;
								break;
							}
						}
						if (isBasketTransaction) {
							break;
						}
					}
					if(!isBasketTransaction) {
						CMSV12BasketHelper.setBasketStatus(theAppMgr,cmsV12Basket, CMSV12Basket.open);
						transaction.setCmsV12Basket(null);
						((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).setCmsV12Basket(null);
					}
				}
			}
			// set layaway account balance
			//if (!layawayAccount()) {
			//   selectPayment();
			//   return  false;
			//}
			//get Digital Signature if necessary
			if ((new ConfigMgr("JPOS_peripherals.cfg").getString("MSR_DEVICE_TYPE")).equals("INGENICO_JPOS")) {
				if (SignatureCaptureDlg.isDigialSignatureRequired(theTxn.getPaymentTransaction())) {
					SignatureCaptureDlg signatureCaptureDlg = new SignatureCaptureDlg(theAppMgr, theTxn.getPaymentTransaction());
					String[] cardInfoMessage = SignatureCaptureDlg.getCardInfoMessage(theTxn.getPaymentTransaction());
					ET1000JPOSForm.getInstance().displaySignatureCapture(cardInfoMessage);
					signatureCaptureDlg.setVisible(true);
				} else {
					ET1000JPOSForm.getInstance().displayLogo();
				}
			}
			// pop drawer
			try {
				if (shouldDrawPop()) {
					System.out.println("PaymentApplet.postTransaction()->Pop Drawer...");
					CMSDrawer.openDevice(ReceiptConfigInfo.getInstance().getCashDrawerName());
					CMSDrawer.openDrawer();
					CMSDrawer.closeDevice();
				}
			} catch (Exception ex) {
				// CashDrawer not found!
				System.out.println("Cash Drawer Not Found");
			}
			// set ID and post transaction
			/*Moved this creation of Txn number to start for ISDValidation needed this for authorization.
			 String txnNum = CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr,
			 (CMSStore)theStore, (CMSRegister)theAppMgr.getGlobalObject("REGISTER"));
			 theTxn.getPaymentTransaction().setId(txnNum);
			 */
			if (theTxn.getPaymentTransaction().getId() == null || theTxn.getPaymentTransaction().getId().equals("")) {
				if (txnNum == null || txnNum.equals("")) {
					createTxnNumber();
				} else {
					theTxn.getPaymentTransaction().setId(txnNum);
				}
			}
			theTxn.getPaymentTransaction().setSubmitDate(new java.util.Date());
			// set presale txn id
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().doSetPresaleId(theTxn.getPaymentTransaction().getId());
				}
			}
			//set consignment txn id
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getConsignmentLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getConsignmentLineItemsArray().length > 0) {
					((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getConsignmentTransaction().doSetConsignmentId(theTxn.getPaymentTransaction().getId());
				}
			}
			/*
			 * 5095 - Post transaction before printing it.  The issue is, is that if the
			 * post causes an exception, a receipt still exists.  This post mechanism
			 * is merely just serializing this txn to disk, so its not that long.
			 * @see com.chelseasystems.cr.txnposter.TxnPosterSyncRMIServices
			 */
			try {
				CMSTxnPosterHelper.post(theAppMgr, theTxn.getPaymentTransaction());
			} catch (java.io.IOException ioe) {
				/*
				 * 5293 - Any failure to persist this transaction is a <b>huge</b>
				 * issue.  We have decided to provide a receipt to record the
				 * used transaction number and prevent the Eu from continuing
				 * until help-desk records/fixes the failure and restarts the
				 * application with Mission Control.
				 */
				theTxn.printPostFailedReceipt(theAppMgr);
				while(true) {
					theAppMgr.showErrorDlg(res.getString("The transaction could not " + "be recorded on this terminal.  Posting can not continue.  "
							+ "This application will not continue until support has " + "resolved the issue.  Please call support and notify the manager " + "of this transaction number."
							+ "  Txn Num: " + txnNum));
				}
			} catch (Exception e) {
				theAppMgr.showErrorDlg(res.getString("Transaction could not be posted.  Please call support."));
				return (false);
			}
			// print receipt
			System.out.println("about to print receiop...");
			printReceipt();
			System.out.println("************************************************");
			System.out.println("******    POSTED TXN: " + txnNum + "      ******");
			System.out.println("************************************************");
			txnNum = null;
			//Remove Stateobjects
			theAppMgr.removeStateObject("ARM_DISCOUNT_SEQ");
			theAppMgr.removeStateObject("ARM_DISCOUNT_TYPE");
			theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
			theAppMgr.removeStateObject("ARM_FROM_SHIPPING_ADDRESS");
			theAppMgr.removeStateObject("ARM_SHIPPING_FEE");
			theAppMgr.removeStateObject("ARM_IS_CUSTOMER_ADDED");
			theAppMgr.removeStateObject("V12BASKET_LOOKUP");
			if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
				String txnClassName = theTxn.getPaymentTransaction().getClass().getName();
				String shortClassName = txnClassName.substring(txnClassName.lastIndexOf(".") + 1, txnClassName.length());
				String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + shortClassName + ".rdo";
				try {
					ObjectStore objectStore = new ObjectStore(fileName);
					objectStore.write(theTxn);
				} catch (Exception e) {
					System.out.println("exception on writing object to blueprint folder: " + e);
				}
			}
			//      if (theTxn.isFrankingRequired()) {
			//        endorseCheck();
			//      }
			return (true);
		} catch (Exception ex) {
			System.out.println("catching hard exception in post...");
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}
	}

	/**
	 * @return
	 */
	private boolean shouldDrawPop() {
		for (Enumeration enm = theTxn.getPayments(); enm.hasMoreElements();) {
			Payment pay = (Payment) enm.nextElement();
			if (pay instanceof Cash) {
				return (true);
			}
		}
		return (false);
	}

	/**
	 */
	private void printReceipt() {
		theTxn.printReceipt(theAppMgr); // whoooo...   that was easy.
	}

	/**
	 */
	private void loadGiftPanel() {
		theAppMgr.showMenu(MenuConst.GIFT_RECEIPTS, theOpr);
		pnlGiftReceipt = new GiftReceiptPanel(theAppMgr, theTxn);
		pnlGiftReceipt.getTable().addMouseListener(new java.awt.event.MouseAdapter() {

			/**
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				tblPayment_mouseClicked(e);
			}
		});
		pnlCards.add(pnlGiftReceipt, "giftReceipt");
		card.show(pnlCards, "giftReceipt");
	}

	/**
	 */
	private void reprintReceipt() {
		theTxn.rePrintReceipt(theAppMgr);
	}

	/**
	 */
	private void enterStubQty() {
		theAppMgr.setSingleEditArea(res.getString("Enter number of alteration stubs to print."), "PRINT_STUB", new Integer(1), theAppMgr.INTEGER_MASK);
	}

	/**
	 * @param anEvent
	 */
	public void appButtonEvent(String Header, CMSActionEvent anEvent, ArmCurrency curr) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("PREMIO_DISCOUNT")) {
			System.out.println("xxxxxxxxxxxxx");
			initValues();
			updateLabels();
			theAppMgr.unRegisterSingleEditArea();
			String Builder = CMSPaymentMgr.getPaymentBuilder("PREMIO_DISCOUNT");
			if (Builder != null) {
				theAppMgr.buildObject("PAYMENT", Builder, curr);
			}
			tblPayment.repaint();
			System.out.println(" calling refund of appButtonEvent action premio_discount ");
			theAppMgr.setSingleEditArea(res.getString("Choose refund type."));
			initButtons(CMSPaymentMgr.REFUND);
			return;
		}
	}

	public void appButtonEvent(String Header, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("PREMIO_DISCOUNT")) {
			System.out.println("xxxxxxxxxxxxx");
			initValues();
			updateLabels();
			theAppMgr.unRegisterSingleEditArea();
			String Builder = CMSPaymentMgr.getPaymentBuilder("PREMIO_DISCOUNT");
			if (Builder != null) {
				theAppMgr.buildObject("PAYMENT", Builder, "PREMIO_DISCOUNT");
			}
			tblPayment.repaint();
			theAppMgr.setSingleEditArea(res.getString("Choose refund type."));
			initButtons(CMSPaymentMgr.REFUND);
			return;
		}
		if (Header.equals("CREDIT_CARD")) {
			if (sAction.equals("PREV")) {
				strUSECARD = null;
				anEvent.consume();
				//theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
				selectPayment();
				setEditArea(res.getString("Select option"), null, -1);
				showTitle(false);
				isPostComplete = false;
			} else if (sAction.equals("USE_CARD_ON_FILE")) {
				// If Customer is Null-- Display Customer Lo`up
				CMSCustomer customer = (CMSCustomer) theTxn.getCustomerAppModel().getCustomer();
				if (customer == null) {
					// Display Customer Lookup
					theAppMgr.addStateObject("USE_CARD_ON_FILE", "TRUE");
					theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
				} else {
					String storeId = ((CMSStore) theAppMgr.getGlobalObject("STORE")).getId();
					List listCCreditCard = customer.getCreditCard();
					int numCustCC = listCCreditCard.size();
					if (listCCreditCard == null || numCustCC == 0) {
						theAppMgr.showErrorDlg(res.getString("No Cards on file for this customer at this store"));
						strUSECARD = null;
						selectPayment();
					} else {
						ArrayList listCreditCard = new ArrayList();
						for (int i = 0; i < numCustCC; i++) {
							CustomerCreditCard creditCard = (CustomerCreditCard) listCCreditCard.get(i);
							if ((creditCard.getStoreId()).equalsIgnoreCase(storeId)) {
								listCreditCard.add(creditCard);
							}
						}
						int cardNum = listCreditCard.size();
						if (listCreditCard == null || cardNum == 0) {
							System.out.println("No Credit Card ");
							theAppMgr.showErrorDlg(res.getString("No Cards on file for this customer at this store"));
							strUSECARD = null;
							selectPayment();
						} else if (cardNum == 1) {
							CustomerCreditCard creditCard = (CustomerCreditCard) listCreditCard.get(0);
							if(!checkExpiryDate(creditCard)){
								theAppMgr.showErrorDlg(res.getString("Credit Card on file has expired. Update or use other card."));
								selectPayment();
							}else{
								createPayment(creditCard);
							}
						} else if (cardNum > 1) {
							// Dialog Box
							int numCreditcards = listCreditCard.size();
							CustomerCreditCard[] creditCards = new CustomerCreditCard[numCreditcards];
							GenericChooserRow[] availCreditCards = new GenericChooserRow[numCreditcards];
							SimpleDateFormat dateFormat = new SimpleDateFormat("MMyy");
							for (int i = 0; i < numCreditcards; i++) {
								CustomerCreditCard creditCard = (CustomerCreditCard) listCreditCard.get(i);
								creditCards[i] = creditCard;
								availCreditCards[i] = new GenericChooserRow(new String[] { creditCard.getCreditCardType(), CreditAuthUtil.maskCreditCardNo(creditCard.getCreditCardNumber()),
										dateFormat.format(creditCard.getExpDate()) }, creditCard);
							}
							GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availCreditCards, new String[] { "Card Type", "Card Number",
									"Expiration Date" });
							dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
							dlg.setVisible(true);
							if (dlg.isOK()) {
								CustomerCreditCard creditCard = (CustomerCreditCard) dlg.getSelectedRow().getRowKeyData();
								if(!checkExpiryDate(creditCard)){
									theAppMgr.showErrorDlg(res.getString("Credit Card on file has expired. Update or use other card."));
									selectPayment();
								}else{
									createPayment(creditCard);
								}
							} else {
								theAppMgr.showErrorDlg(res.getString("You did not select a Credit Card, you must select a Credit Card."));
							}
						}
					}
				}
			}
			// Issue # 996
		}
	}
	
	/**
	 * Check if the card on file has expierd w.r.t current date.
	 * @param creditCard
	 * @return false if the card has expired.
	 */
	private boolean checkExpiryDate(CustomerCreditCard creditCard) {
		if(((Date)theAppMgr.getGlobalObject("PROCESS_DATE")).compareTo(creditCard.getExpDate()) == 1){
			return false;
		}
		
		return true;
	}

	/**
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		theAppMgr.unRegisterSingleEditArea(); // cancel any builders in progress
		if (postingInProgress) {
			// Do not allow any button actions while posting is in progress
			anEvent.consume();
			return;
		}
		String sAction = anEvent.getActionCommand();
		System.out.println("Action: " + sAction);
		sAction_global = sAction;
		try {
			if (sAction.equals("NEW_TRANSACTION")) {
				try {
					CMSCompositePOSTransaction txn = CMSTransactionPOSHelper.allocate(theAppMgr);
					CMSEmployee employee = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
					theAppMgr.clearStateObjects();
					theAppMgr.addStateObject("TXN_POS", txn);
					theAppMgr.addStateObject("OPERATOR", employee);
				} catch (Exception ex) {
					anEvent.consume();
					theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
				}
				return;
			} else if (sAction.equals("ADJUST_PREMIO_REFUND")) {
				theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
				theAppMgr.unRegisterSingleEditArea();
				theAppMgr.setSingleEditArea(res.getString("Enter new premio refund amount."), "NEW_PREMIO_REFUND_AMOUNT", theAppMgr.CURRENCY_MASK);
				return;
			} else if (sAction.equals("DEL_PAY")) {
				deletePayment();
			} else if (sAction.equals("PRINT_RECEIPT")) {
				reprintReceipt();
			} else if (sAction.equals("PRINT_GIFT")) {
				//Temporary fix, a rule has to be created and checked on the isShowable method in CMSMenu/Option
				if (!(theTxn instanceof CMSCompositePOSTransactionAppModel)) {
					theAppMgr.showErrorDlg(res.getString("A gift receipt is not allowed for this transaction."));
					return;
				}
				if (theTxn instanceof CMSLayawayPaymentTransactionAppModel) {
					try {
						if (!((CMSLayawayPaymentTransactionAppModel) theTxn).getLayaway().getCurrentAmountDue().equals(new ArmCurrency(0.00))) {
							theAppMgr.showErrorDlg(res.getString("A gift receipt is not allowed on outstanding layaways."));
							return;
						}
					} catch (CurrencyException e) {
						theAppMgr.showExceptionDlg(e);
					}
				}
				POSLineItem[] lineItems = theTxn.getLineItemsArray();
				boolean isGiftItems = false;
				for (int i = 0; i < lineItems.length; i++) {
					if (lineItems[i] instanceof SaleLineItem) {
						isGiftItems = true;
						continue;
					}
				}
				if (!isGiftItems) {
					theAppMgr.showErrorDlg(res.getString("There are no items on this transaction that are eligible to print on a gift receipt."));
					return;
				}
				loadGiftPanel();
			} else if (sAction.equals("PRINT_VAT")) {
				theAppMgr.addStateObject("PRINT_VAT", "PRINT_VAT");
				POSLineItem[] lineItems = theTxn.getLineItemsArray();
				boolean isVATIItems = false;
				for (int i = 0; i < lineItems.length; i++) {
					if (lineItems[i] instanceof SaleLineItem) {
						isVATIItems = true;
						continue;
					}
				}
				if (!(theTxn instanceof CMSCompositePOSTransactionAppModel) || !isVATIItems) {
					theAppMgr.showErrorDlg(res.getString("A VAT Invoice is not allowed for this transaction."));
					return;
				}
				printVATInvoice();
			} else if (sAction.equals("PRINT_EXPORT")) {
				theAppMgr.addStateObject("PRINT_EXPORT", "PRINT__EXPORT");
				POSLineItem[] lineItems = theTxn.getLineItemsArray();
				boolean isREItems = false;
				for (int i = 0; i < lineItems.length; i++) {
					if (lineItems[i] instanceof SaleLineItem) {
						isREItems = true;
						continue;
					}
				}
				if (!(theTxn instanceof CMSCompositePOSTransactionAppModel) || !isREItems) {
					theAppMgr.showErrorDlg(res.getString("A Retail Export Receipt is not allowed for this transaction."));
					return;
				}
				printRetailExport();
			} else if (sAction.equals("ENDORSE_CHECK")) {
				System.out.println("*************** Check Button Pressed ");
				if (theTxn.isFrankingRequired()) {
					endorseCheck();
				} else {
					theAppMgr.showErrorDlg(res.getString("There are no documents to endorse."));
				}
			} else if (sAction.equals("POST_TXN")) {
				System.out.println("******** In post txn");
				// Check to see if any authorizable payment failed.
				// If so display message and retry.
				// DONT AUTHORIZE YET UNTIL REGISTER IS INTO GLOBAL REPOSITORY
				/*
				 String reply = "04";
				 // If Approved, reply = "01", do nothing.
				 if(reply =="01"){
				 theAppMgr.showErrorDlg(res.getString("Proceed to next Step. No change Required"));
				 }
				 // Referral
				 else if(reply =="03") {
				 theAppMgr.showErrorDlg(res.getString("Call the authorizer at <1-000-000-0000>. Your Merchant number is <123456>, the Card number is 0000111122223333 and the amount is 99.99"));
				 }
				 // Transaction TimeOut
				 else if(reply =="04") {
				 theAppMgr.showErrorDlg(res.getString("Call the authorizer at <1-000-000-0000>. Your Merchant number is <123456>, the Card number is 0000111122223333 and the amount is 99.99"));
				 }
				 //System Failure
				 else if(reply =="05") {
				 theAppMgr.showErrorDlg(res.getString("Call the authorizer at <1-000-000-0000>. Your Merchant number is <123456>, the Card number is 0000111122223333 and the amount is 99.99"));
				 }
				 */
				//        if (!authorize()) {
				//          String check = null;
				//          Register register = (Register)theAppMgr.getGlobalObject("REGISTER");
				//          Payment[] payments = theTxn.getPaymentsArray();
				//          int len = payments.length;
				//          /**
				//           * Code Added for priniting the Error Messages
				//           */
				//          int len_result = result.length();
				//          result = result.substring(2, len_result);
				//          int startIndex = 0;
				//          for (int i = 0; i < (len_result - 2) / 2; i++) {
				//            check = result.substring(startIndex, startIndex + 2);
				//            startIndex = startIndex + 2;
				//            // For Credit Card
				//            // # 94. Payment/Credit Card: Authorization response  message returned from host not displayed
				//            if (payments[i] instanceof CreditCard) {
				//              String errorMsg = "Authorization failed on one or more of the payments. ";
				//              if (payments[i].getRespMessage() != null) {
				//                errorMsg += ("Message: " + payments[i].getRespMessage());
				//              } else {
				//                errorMsg =
				//                    "Authorization failed on one or more of the payments. Server does not response or timeout.";
				//              }
				//              theAppMgr.showErrorDlg(res.getString(errorMsg));
				//            }
				//            // For other payment type
				//            else if (check.equals("03") || check.equals("04") || check.equals("05")) {
				//              String errorMsg = "Authorization failed on one or more of the payments. ";
				//              if (payments[i].getRespMessage() != null) {
				//                errorMsg += ("Message: " + payments[i].getRespMessage());
				//              } else {
				//                errorMsg =
				//                    "Authorization failed on one or more of the payments. Server does not response or timeout.";
				//              }
				//              theAppMgr.showErrorDlg(res.getString(errorMsg));
				//            }
				//          }
				//          /**
				//           * Code Ended
				//           */
				//          tblPayment.repaint();
				//          theAppMgr.showMenu(MenuConst.POST_AUTH, theOpr, theAppMgr.PREV_BUTTON);
				//          selectLastRow();
				//          selectOption();
				//          return;
				//        }
				//these should be taken away
				if ((new ConfigMgr("JPOS_peripherals.cfg").getString("MSR_DEVICE_TYPE")).equals("INGENICO_JPOS")) {
					Payment[] payments = theTxn.getPaymentTransaction().getPaymentsArray();
					boolean authDisplayOnET1000 = false;
					for (int i = 0; i < payments.length; i++) {
						if (payments[i] instanceof CreditCard) {
							try {
								Thread.sleep(1000L);
							} catch (InterruptedException iexp) {
							}
							String digits = "" + System.currentTimeMillis();
							String fourRandomDigits = (digits).substring(digits.length() - 4);
							((CreditCard) payments[i]).setRespAuthorizationCode(fourRandomDigits);
							if (!authDisplayOnET1000) {
								authDisplayOnET1000 = true;
								ET1000JPOSForm.getInstance().displayAuthorizing();
							}
							tblPayment.repaint();
						}
					}
				}
				tblPayment.repaint();
				try {
					// Set the Posting progress flag
					postingInProgress = true;
					if (postTransaction()) {
						isPostComplete = true;
						theAppMgr.addStateObject("POST_COMPLETE", "POST_COMPLETE");
						initReceiptButtons();
					} else {
						theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
					}
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
				} finally {
					// UnSet the posting progress flag
					postingInProgress = false;
				}
				PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr.getStateObject("TXN_POS");

				// No need to issue card for JP

				/* if (txn instanceof CMSCompositePOSTransaction) {
				 CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction)txn);
				 Loyalty OldObj = theTxnPOS.getLoyaltyCard();
				 if (OldObj != null) {
				 theAppMgr.addStateObject("LOYALTYOBJ", OldObj);
				 if (OldObj.getCurrBalance() + theTxnPOS.getLoyaltyPoints() >= rewardPointsLevel) {
				 theAppMgr.showErrorDlg(
				 res.getString("Issue Reward Discount Card. Customer has earned a Reward Discount"));
				 }
				 }
				 }*/
				selectOption();
			} else if (sAction.equals("PREV")) {
				if (!isPostComplete) {
					// remove all payments
					prevPressed = true;
					try {
						theTxn.removeAllPayments();
						PaymentTransaction payTran = theTxn.getPaymentTransaction();
						if (payTran instanceof CMSCompositePOSTransaction) {
							((CMSCompositePOSTransaction) payTran).resetRedeemableAmount();
						}
					} catch (BusinessRuleException ex) {
						theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					}
				}
			} else if (sAction.equals("CANCEL")) {
				// Cancel suspending...
				selectPayment();
			} else if (sAction.equals("CANCEL_TXN")) {
				if (!theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?"))) {
					anEvent.consume();
					return;
				}
				cancelTxnPressed = true;
				theAppMgr.setHomeEnabled(true);
				//theAppMgr.goHome();
			} else if (sAction.equals("OK")) {
				// add flag here
				isPostComplete = true;
				// MP: For NFS.
				theAppMgr.removeStateObject("CUST_NFS");
			} else if (sAction.equals("PRINT_ALL_GIFT")) {
				pnlGiftReceipt.getGiftReceiptModel().setAllItemSelection(new Boolean(true));
				printGiftReceipt();
			} else if (sAction.equals("ISSUE_REWARD")) {
				// If a LoyaltyCard is chosen
				PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr.getStateObject("TXN_POS");
				CMSCompositePOSTransaction theTxn = ((CMSCompositePOSTransaction) txn);
				theAppMgr.addStateObject("LOYALTYOBJ", theTxn.getLoyaltyCard());
				Loyalty OldObj = theTxn.getLoyaltyCard();
				theAppMgr.unRegisterSingleEditArea();
				ConfigMgr config = new ConfigMgr("loyalty.cfg");
				String Builder = config.getString("ISSUE_REWARD_CARD_BUILDER");
				System.out.println("the builder is: " + Builder);
				theAppMgr.buildObject("ISSUERWDCARD", Builder, "");
			} else if (sAction.equals("PRINT_SELECT_ITEMS")) {
				printGiftReceipt();
			} else if (sAction.equals("RETURN_TO_PAYMENTS")) {
				card.show(pnlCards, "payments");
				initReceiptButtons();
			}
			// credit card requires swipe of card
			else if (sAction.equals("CREDIT_CARD")) {
				applyVat(true);
				initValues();
				updateLabels();
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				//                String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
				String Builder = CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
				System.out.println("the builder is: " + Builder);
				theAppMgr.buildObject("PAYMENT", Builder, "");
			} else if (sAction.equals("DCRD")) {
				applyVat(true);
				initValues();
				updateLabels();
				theAppMgr.unRegisterSingleEditArea();
				String Builder = CMSPaymentMgr.getPaymentBuilder("DCRD");
				System.out.println("the builder is: " + Builder);
				theAppMgr.buildObject("PAYMENT", Builder, "");
			}

			// Issue # 996
			else if (sAction.equals("VIEW_CREDIT_CARD")) {
				//        theAppMgr.showMenu(MenuConst.CREDIT_CARD, "CREDIT_CARD", theOpr);
				//setEditArea("Select option", null, -1);
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				String Builder = CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
				System.out.println("the builder is: " + Builder);
				theAppMgr.buildObject("PAYMENT", Builder, "");
				showTitle(false);
				isPostComplete = false;
			}
			// Issue # 996
			else if (sAction.equals("MAN_OVERRIDE")) {
				theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL", theAppMgr.REQUIRED_MASK);
				theAppMgr.setEditAreaFocus();
			} else if (sAction.equals("SUSPEND")) {
				try {
					theTxn.testIsSuspendable();
					theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
					theAppMgr.setSingleEditArea(res.getString("Enter comments."), "SUSPEND_COMMENT");
				} catch (BusinessRuleException bre) {
					theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
					selectOption();
				}
			} else if (sAction.equals("CHECK")) {
				applyVat(true);
				initValues();
				updateLabels();
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				String Builder = PaymentMgr.getPaymentBuilder("CHECK");
				System.out.println("The Transaction Value  " + theTxn.getPaymentTransaction());
				System.out.println("the builder is: " + Builder);
				theAppMgr.buildObject("PAYMENT", Builder, "");
			} else {
				applyVat(false);
				initValues();
				updateLabels();
				//                String Builder = PaymentMgr.getPaymentBuilder(sAction);
				if (isButtonInList(sAction)) {
					//if customer is present
					if (!flag) {
						//and is coming from CustomerSaleApplet
						if (!bool) {
							String Builder = PaymentMgr.getPaymentBuilder(sAction);
							if (Builder != null) {
								theAppMgr.buildObject(this, "PAYMENT", Builder, sAction);
							}
						}
						//customer present but not from CustSaleApplet
						else if (theAppMgr.getStateObject("THE_EVENT") != null && (theAppMgr.getStateObject("THE_EVENT").equals("SUCCESS") || theAppMgr.getStateObject("THE_EVENT").equals("FAILURE"))) {
							PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
							theAppMgr.removeStateObject("THE_EVENT");
						} else {
							String Builder = PaymentMgr.getPaymentBuilder(sAction);
							if (Builder != null) {
								theAppMgr.buildObject(this, "PAYMENT", Builder, sAction);
							}
						}
					} else {
						//if not coming from CustolerListApplet
						if (!bool) {
							//if customer is not present then gather information
							theAppMgr.addStateObject("PAYMENT", sAction);
							theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
						} else {
							//if the customer is already present when coming to PaymentApplet for the
							//first time
							if (flag) {
								theAppMgr.addStateObject("PAYMENT", sAction);
								theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
							} else {
								String Builder = PaymentMgr.getPaymentBuilder(sAction);
								if (Builder != null) {
									theAppMgr.buildObject(this, "PAYMENT", Builder, sAction);
								}
							}
						}
					}
				} else {
					String Builder = CMSPaymentMgr.getPaymentBuilder(sAction);
					if (Builder != null) {
						theAppMgr.buildObject("PAYMENT", Builder, sAction);
					}
					//flag = false;
				}

			}
		} catch (BusinessRuleException bre) {
			theAppMgr.showErrorDlg(bre.getMessage());
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	/*
	 * Private method to adjust premio refund
	 */
	private void adjustPremioRefund() {
		// Set the limits on input
		// Ask question
		// Get back from edit area event
		// Make sure that the values are within limit
		// Else appropriate error message
		// Valid amount
		// Reset redeemable on txn
		// Set redeemable on txn
		// Get premio payment
		// Reset
	}

	/*
	 * This method will remove a payment from the Txn and delete any other
	 *  payments back to the customer. Ex. If the amount given is greater than
	 *  the amount due, then the amount in change is then determined. If the
	 *  Eu then deletes a payment, then change amount will remain unless found
	 *  and deleted so the amount to give in change can be recalculated.
	 */
	private void deletePayment() {
		int row = tblPayment.getSelectedRow();
		if (row < 0) {
			theAppMgr.showErrorDlg(res.getString("You must first select a payment."));
		} else {
			try {
				Payment payment = model.getPayment(row);
				theTxn.removePayment(payment);
				if (theAppMgr.getStateObject("MAX_CHANGE") != null) {
					theAppMgr.removeStateObject("MAX_CHANGE");
				}
				model.removeRow(row);
				if (!payment.getAmount().lessThan(new ArmCurrency(0.00))) {
					for (int i = model.getRowCount() - 1; i >= 0; i--) {
						Payment pay = (Payment) model.getPayment(i);
						try {
							if (pay.getAmount().lessThan(new ArmCurrency(0.00))) {
								theTxn.removePayment(pay);
								model.removeRow(i);
							}
						} catch (CurrencyException e) {
							System.out.print("Currency Ex deletePayment-> " + e);
						}
					}
				}
				if (payment instanceof CMSPremioDiscount) {
					if (payment.getAmount().doubleValue() < 0.0) {
						doNotAddDeletedPremioDiscRefund = true;
					}

					PaymentTransaction payTran = theTxn.getPaymentTransaction();
					if (payTran instanceof CMSCompositePOSTransaction) {
						((CMSCompositePOSTransaction) payTran).resetRedeemableAmount();
					}
				}

				updateLabels();
				tblPayment.repaint();
				selectPayment();
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			} catch (CurrencyException e) {
				System.out.print("Currency Ex deletePayment-> " + e);
			}
		}
	}

	/**
	 */
	private void updateLabels() {
		try {
			AmtTnd$Lbl.setText(theTxn.getTotalPaymentAmount().formattedStringValue());
		ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue();
			amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
			AmtDue$Lbl.setText(amtDue.formattedStringValue());
			initHeaders();
			setLoyaltyPointsStatus();
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "updateLabels", "Unable to subtract two currencies", "The two currencies are of different type.", LoggingServices.MINOR, ex);
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void printVATInvoice() {
		try {
			if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
				String txnClassName = theTxn.getPaymentTransaction().getClass().getName();
				String shortClassName = txnClassName.substring(txnClassName.lastIndexOf(".") + 1, txnClassName.length());
				String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + shortClassName + ".rdo";
				ObjectStore objectStore = new ObjectStore(fileName);
				objectStore.write(theTxn);
			}
			((CMSPaymentTransactionAppModel) theTxn).printVATInvoice(theAppMgr);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void printRetailExport() {
		try {
			if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
				String txnClassName = theTxn.getPaymentTransaction().getClass().getName();
				String shortClassName = txnClassName.substring(txnClassName.lastIndexOf(".") + 1, txnClassName.length());
				String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + shortClassName + ".rdo";
				ObjectStore objectStore = new ObjectStore(fileName);
				objectStore.write(theTxn);
			}
			((CMSPaymentTransactionAppModel) theTxn).printRetailExportReceipt(theAppMgr);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void printGiftReceipt() {
		try {
			if (theTxn instanceof CMSPaymentTransactionAppModel) {
				if (!pnlGiftReceipt.getGiftReceiptModel().containsSelectedItems()) {
					theAppMgr.showErrorDlg(res.getString("No valid items are selected to print a gift receipt."));
					return;
				}
				GiftReceiptEntry[] entries = pnlGiftReceipt.getGiftReceiptModel().getSelectedEntries();
				((CMSPaymentTransactionAppModel) theTxn).setSelectedGiftItems(entries);
				if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
					String txnClassName = theTxn.getPaymentTransaction().getClass().getName();
					String shortClassName = txnClassName.substring(txnClassName.lastIndexOf(".") + 1, txnClassName.length());
					String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + shortClassName + ".rdo";
					ObjectStore objectStore = new ObjectStore(fileName);
					objectStore.write(theTxn);
				}
				GiftReceiptEntry[] getEntries = ((CMSPaymentTransactionAppModel) theTxn).getSelectedGiftItems();
				theTxn.printGiftReceipt(theAppMgr);
				pnlGiftReceipt.getGiftReceiptModel().setAllItemSelection(new Boolean(false));
				pnlGiftReceipt.repaint();
			} else {
				theAppMgr.showErrorDlg(res.getString("A gift receipt is not allowed for this transaction."));
			}
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void endorseCheck() {
		FrankDlg dlg = new FrankDlg(theAppMgr, theTxn.getPaymentTransaction(), theTxn.getCustomer().getTelephone() == null ? "" : theTxn.getCustomer().getTelephone().getFormattedNumber());
		dlg.setVisible(true);
	}

	/**
	 * @return
	 */
	/**
	 *
	 * @return String
	 */
	/**
	 * @return
	 */
	//    private boolean authorize () {
	//        try {
	//            Register register = (Register)theAppMgr.getGlobalObject("REGISTER");
	//            if (theTxn.getCompositeTotalAmountDue().greaterThan(new ArmCurrency(0.0))) {
	//                CMSCreditAuthHelper.validate(theAppMgr, theTxn.getPaymentTransaction(),
	//                        register.getId());
	//                for (Enumeration enm = theTxn.getPayments(); enm.hasMoreElements();) {
	//                    Payment pay = (Payment)enm.nextElement();
	//                    if (pay.isAuthRequired())
	//                        return  (false);
	//                }
	//            }
	//            return  (true);
	//        } catch (Exception ex) {
	//            theAppMgr.showExceptionDlg(ex);
	//            return  (false);
	//        }
	//    }
	private boolean authorize() {
		try {
			//       * Set the result = "00" (Megha)
			result = "00";
			String StatusID[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			Payment[] payments = theTxn.getPaymentsArray();
			int len = payments.length;
			if (theTxn.getCompositeTotalAmountDue().greaterThan(new ArmCurrency(0.0))) {
				StatusID = CMSCreditAuthHelper.validate(theAppMgr, theTxn.getPaymentTransaction(), register.getId());
				//         Made change in checking the Error Condition:StatusID[i]!=null(Megha)
				if (StatusID == null) {
					return true;
				}
				for (int i = 0; i < len; i++) {
					if (StatusID[i] != null) {
						result = result + StatusID[i];
					} else if (StatusID[i] == null) {
						result = result + "00";
					}
				}
				for (Enumeration enm = theTxn.getPayments(); enm.hasMoreElements();) {
					Payment pay = (Payment) enm.nextElement();
					if (pay.isAuthRequired()) {
						return (false);
					}
				}
			}
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
	void selectLastRow() {
		if (tblPayment.getRowCount() < 1) {
			model.prevPage();
		}
		tblPayment.getSelectionModel().setSelectionInterval(tblPayment.getRowCount() - 1, tblPayment.getRowCount() - 1);
	}

	/**
	 * put your documentation comment here
	 * @param absoluteRow
	 */
	void selectRow(int absoluteRow) {
		model.pageContainingRow(absoluteRow);
		tblPayment.getSelectionModel().setSelectionInterval(absoluteRow % model.getRowCount(), absoluteRow % model.getRowCount());
	}

	/**
	 * @param e
	 */
	void tblPayment_componentResized(ComponentEvent e) {
		tblPayment.getColumnModel().getColumn(model.TYPE).setPreferredWidth(140);
		tblPayment.getColumnModel().getColumn(model.EXPIRE).setPreferredWidth(95);
		tblPayment.getColumnModel().getColumn(model.APPROVAL).setPreferredWidth(125);
		tblPayment.getColumnModel().getColumn(model.AMOUNT).setPreferredWidth(115);
		tblPayment.getColumnModel().getColumn(model.DETAIL).setPreferredWidth(
				tblPayment.getWidth()
						- (tblPayment.getColumnModel().getColumn(model.TYPE).getPreferredWidth() + tblPayment.getColumnModel().getColumn(model.EXPIRE).getPreferredWidth()
								+ tblPayment.getColumnModel().getColumn(model.APPROVAL).getPreferredWidth() + tblPayment.getColumnModel().getColumn(model.AMOUNT).getPreferredWidth()));
		DefaultTableCellRenderer HorAlignRenderer = new DefaultTableCellRenderer();
		HorAlignRenderer.setHorizontalAlignment(4);
		tblPayment.getColumnModel().getColumn(model.AMOUNT).setCellRenderer(HorAlignRenderer);
		model.setRowsShown(tblPayment.getHeight() / tblPayment.getRowHeight());
	}

	/**
	 * callback when <b>Page Down</b> icon is clicked
	 */
	public void pageDown(MouseEvent e) {
		model.nextPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
		if (pnlGiftReceipt != null) {
			pnlGiftReceipt.getGiftReceiptModel().nextPage();
			theAppMgr.showPageNumber(e, pnlGiftReceipt.getGiftReceiptModel().getCurrentPageNumber() + 1, pnlGiftReceipt.getGiftReceiptModel().getPageCount());
		}
	}

	/**
	 * callback when <b>Page Up</b> icon is clicked
	 */
	public void pageUp(MouseEvent e) {
		model.prevPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
		if (pnlGiftReceipt != null) {
			pnlGiftReceipt.getGiftReceiptModel().prevPage();
			theAppMgr.showPageNumber(e, pnlGiftReceipt.getGiftReceiptModel().getCurrentPageNumber() + 1, pnlGiftReceipt.getGiftReceiptModel().getPageCount());
		}
	}

	/**
	 * @param e
	 */
	void tblPayment_mouseClicked(MouseEvent e) {
		theAppMgr.setEditAreaFocus();
	}

	/**
	 * @return
	 */
	public boolean isHomeAllowed() {
		CMSCompositePOSTransaction transaction = (CMSCompositePOSTransaction)theTxn.getPaymentTransaction();
		CMSV12Basket cmsV12Basket = null;
		if(transaction !=null)
			cmsV12Basket = transaction.getCmsV12Basket();
		if (postingInProgress)
			return false;
		if (!isPostComplete && !cancelTxnPressed) {
			try {
			if (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Selecting 'Home' cancels the transaction.  Do you want to cancel this transaction?"))) {
				if(cmsV12Basket!=null) {
					CMSV12BasketHelper.setBasketStatus(theAppMgr,
							cmsV12Basket, CMSV12Basket.open);
					theAppMgr.removeStateObject("V12BASKET_LOOKUP");
				} 
				return true;
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else {
			return (true); // post is complete or Cancel Txn was pressed
		}
	}

	/**
	 * call this method to check if we need to proceed to the CustomerListApplet for gathering information
	 * about the customer.
	 * Added by Anand Kannan on 03/24/2005
	 */
	private boolean isButtonInList(String sAction_global) {
		ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
		String customerReqd = cfgMgr.getString("REQUIRES_CUSTOMER");
		Vector v = new Vector();
		StringTokenizer st = new StringTokenizer(customerReqd, ",");
		while(st.hasMoreTokens()) {
			String button = st.nextToken();
			v.add(button);
		}
		if (v.contains(sAction_global)) {
			theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
			return true;
		}
		return false;
	}

	/**
	 * To be used to set the font of "Amount Due" and "Amount Tendered" in PaymentApplet
	 * Method added by Anand Kannan on 03/25/2005
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

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean isMaxChangeAllowed() {
		String strChangeType = null;
		String strMaxChange = null;
	ArmCurrency maxChange = null;
	ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue();
		//theAppMgr.showErrorDlg("amtDue initial is " +theTxn.getCompositeTotalAmountDue());
		try {
			//theAppMgr.showErrorDlg("total payment amt is " + theTxn.getTotalPaymentAmount());
			amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
			Payment[] payments = theTxn.getPaymentsArray();
			if (isAutoChange) {
				strChangeType = cfgMgr.getString("AUTO_CHANGE_TYPE");
				String strLastPaymentName = payments[payments.length - 1].getTransactionPaymentName();
				if (strChangeType != null && strLastPaymentName != null) {
					strMaxChange = cfgMgr.getString(strLastPaymentName + ".MAX_CHANGE_ALLOWED");
					maxChange = new ArmCurrency(strMaxChange);
					theAppMgr.addStateObject("MAX_CHANGE", maxChange);
					//theAppMgr.addStateObject("IS_CHANGE", "true");
				} else {
					theAppMgr.showErrorDlg("No valid change type has been configured as default");
					return false;
				}
				if (amtDue.absoluteValue().truncate(0).lessThanOrEqualTo(maxChange)) {
					try {
						change = CMSPaymentMgr.getPayment(strChangeType);
						change.setAmount(amtDue);
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (CurrencyException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set Edit area message
	 * @param sMessage Message
	 * @param sCommand Command
	 * @param iMask Mask
	 */
	public void setEditArea(String sMessage, String sCommand, int iMask) {
		if (sCommand != null)
			theAppMgr.setSingleEditArea(sMessage, sCommand, iMask);
		else
			theAppMgr.setSingleEditArea(sMessage);
	}

	/**
	 * Show/Hide Title
	 * @param bShowTitle true/false
	 */
	private void showTitle(boolean bShowTitle) {
		if (bShowTitle)
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), res.getString("Basic Information")));
		else
			this.setBorder(null);
	}

	// Issue # 996
	/**
	 * @param amt
	 * @return
	 */
	private boolean validateChangeAmount(ArmCurrency amt) {
		try {
			PaymentTransaction theTxn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
			//CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
			CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) theTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
		ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
			// if (amtLeft.greaterThanOrEqualTo(new ArmCurrency(0.0)))
			//    return true;
			if (amt.greaterThan(amtLeft.absoluteValue())) {
				theAppMgr.showErrorDlg(res.getString("You can not give more change than is due."));
				return (false);
			} else
				return (true);
		} catch (Exception ex) {
			return (true);
		}
	}

	/**
	 * put your documentation comment here
	 * @param creditCard
	 */
	private void createPayment(CustomerCreditCard creditCard) {
		String accountNum = creditCard.getCreditCardNumber();
		thePayment = CreditCardBldrUtil.allocCreditCardPayment(accountNum);
		if (thePayment == null) {
			theAppMgr.showErrorDlg(res.getString("The account number is invalid."));
			return;
		} else {
			CreditCard cc = (CreditCard) thePayment;
			cc.setAccountNumber(accountNum);
			cc.setZipCode(creditCard.getBillingZipCode());
			cc.setExpirationDate(creditCard.getExpDate());
			if (thePayment.getAmount() == null) {
				try {
					PaymentTransaction theTxn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
					CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) theTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
				ArmCurrency amt = appModel.getCompositeTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
					theAppMgr.unRegisterSingleEditArea();
					theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT", amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
					theAppMgr.setEditAreaFocus();
					showTitle(false);
				} catch (Exception ex) {
					theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT", theAppMgr.CURRENCY_MASK);
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * put your documentation comment here
	 * @param buttonEvent
	 */
	private void invokeViewCCEvent(String buttonEvent) {
		theAppMgr.removeStateObject(buttonEvent);
		Thread post = new Thread(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				theAppMgr.setWorkInProgress(true);
				PaymentApplet_JP.this.appButtonEvent(new CMSActionEvent(this, 0, "VIEW_CREDIT_CARD", 0));
				theAppMgr.setWorkInProgress(false);
				theAppMgr.setEditAreaFocus();
			}
		});
		post.start();
	}

	/**
	 * put your documentation comment here
	 * @param buttonEvent
	 */
	private void invokeUseCOFEvent(String buttonEvent) {
		theAppMgr.removeStateObject(buttonEvent);
		Thread post = new Thread(new Runnable() {

			/**
			 * put your documentation comment here
			 */
			public void run() {
				theAppMgr.setWorkInProgress(true);
				PaymentApplet_JP.this.appButtonEvent("CREDIT_CARD", new CMSActionEvent(this, 0, "USE_CARD_ON_FILE", 0));
				theAppMgr.setWorkInProgress(false);
				theAppMgr.setEditAreaFocus();
			}
		});
		post.start();
	}

	/**
	 * put your documentation comment here
	 * @param paymentType
	 * @return
	 */
	private String getPaymentCode(String paymentType) {
		String cardCode = "";
		List paymentCodes = CMSPaymentMgr.getPaymentCode(paymentType);
		int numPaymentCodes = paymentCodes.size();
		if (numPaymentCodes == 0)
			return cardCode;
		if (numPaymentCodes == 1) {
			CMSPaymentCode paymentCode = (CMSPaymentCode) paymentCodes.get(0);
			cardCode = paymentCode.getPaymentCode();
		} else {
			CMSPaymentCode[] payCodes = new CMSPaymentCode[numPaymentCodes];
			GenericChooserRow[] availPaymentCodes = new GenericChooserRow[numPaymentCodes];
			for (int i = 0; i < numPaymentCodes; i++) {
				CMSPaymentCode paymentCode = (CMSPaymentCode) paymentCodes.get(i);
				payCodes[i] = paymentCode;
				availPaymentCodes[i] = new GenericChooserRow(new String[] { paymentCode.getPaymentDesc() }, paymentCode);
			}
			GenericChooseFromTableDlg overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availPaymentCodes, new String[] { "Payment Desc" });
			overRideDlg.setVisible(true);
			if (overRideDlg.isOK()) {
				CMSPaymentCode payCode = (CMSPaymentCode) overRideDlg.getSelectedRow().getRowKeyData();
				cardCode = payCode.getPaymentCode();
				paymentType = payCode.getPaymentType();
			}
		}
		return cardCode;
	}

	/**
	 * put your documentation comment here
	 */
	private void setCustomerLoyalty(CMSCompositePOSTransaction posTxn) {
		System.out.println("______Tim: " + "in PaymentApplet_JP.setCustomerLoyalty() ==> " + posTxn.toString());
		if (posTxn.getCustomer() == null)
			return;
		CMSCustomer customer = (CMSCustomer) posTxn.getCustomer();
		try {
			Loyalty[] custLoyalties = LoyaltyHelper.getCustomerLoyalties(theAppMgr, customer.getId());
			String brandID = ((CMSStore) posTxn.getStore()).getBrandID();
			if (custLoyalties == null || brandID == null) {
				setLoyalty(posTxn, null);
				return;
			}
			String loyaltyStrBrandID = null;
			for (int i = 0; i < custLoyalties.length; i++) {
				try {
					if (setLoyalty(posTxn, custLoyalties[i])) {
						System.out.println("_________  Loyalty Card used = " + custLoyalties[i].getLoyaltyNumber() + "  __________");
						return;
					}
				} catch (BusinessRuleException ex) {
					//ex.printStackTrace();
				}
			}
			setLoyalty(posTxn, null);
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
	private boolean setLoyalty(CMSCompositePOSTransaction posTxn, Loyalty loyalty) throws BusinessRuleException {
		if (loyalty != null) {
			String brandID = ((CMSStore) posTxn.getStore()).getBrandID();
			ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
			String loyaltyStrBrandID = loyaltyConfigMgr.getString(loyalty.getStoreType() + ".TYPE");
			if (loyaltyStrBrandID != null && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim()) && loyalty.getStatus()) {
				posTxn.setLoyaltyCard(loyalty);
				posTxn.update();
				// Dont update the amount before calling initHeaders()
				//pnlHeader.updateAmtToReward();
				return true;
			} else {
				posTxn.setLoyaltyCard(null);
				posTxn.update();
				throw new BusinessRuleException(res.getString("This loyalty card cannot be used in this store."));
			}
		}
		//else
		posTxn.setLoyaltyCard(null);
		posTxn.update();
		return false;
	}

	private boolean forEvenExchange() {
		CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) CMSApplet.theAppMgr.getStateObject("TXN_POS");
		POSLineItem[] retItems = theTxn.getReturnLineItemsArray();
		double amtDue = 0.0d;
		double premioToReturn = 0.0d;
		boolean isPremioPayment = false;
		boolean isPartialPremioPayment = false;
		Hashtable txnsUsed = new Hashtable();
		if (retItems != null) {
			for (int i = 0; i < retItems.length; i++) {
				CMSReturnLineItem retItem = (CMSReturnLineItem) retItems[i];
				CMSCompositePOSTransaction origTxn = null;
				if (((CMSReturnLineItemDetail) retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
					origTxn = (CMSCompositePOSTransaction) ((CMSReturnLineItemDetail) retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail().getLineItem().getTransaction().getCompositeTransaction();
					String loyaltyRewardRatio = CMSPremioDiscountBldr.loyaltyRewardRatio;
					double loyaltyRatio = 1.0d;
					if (loyaltyRewardRatio != null) {
						loyaltyRatio = 1 / Double.parseDouble(loyaltyRewardRatio);
					}

					try {
						POSLineItemDetail[] posLineItems = retItem.getLineItemDetailsArray();
						for (int index = 0; index < posLineItems.length; index++) {
							//  	  amtDue = amtDue.add(new ArmCurrency(retItem.getLoyaltyPointsToReturn()*loyaltyRatio));
							amtDue = amtDue + origTxn.getPremioDiscountToReturn(((CMSReturnLineItemDetail) posLineItems[index]).getSaleLineItemDetail());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (origTxn != null) {
					if (txnsUsed.containsKey(origTxn.getId())) {
						continue;
					} else {
						txnsUsed.put(origTxn.getId(), "true");
					}
					Payment[] payments = origTxn.getPaymentsArray();
					for (int j = 0; j < payments.length; j++) {
						Payment payment = payments[j];
						if (payment instanceof CMSPremioDiscount) {
							isPremioPayment = true;
							System.out.println(" >> Premio discount present as payment");
							if (payments.length > 1) {
								// partial premio discount...
								isPartialPremioPayment = true;
							}
							/*Currency premioAmountToRefund = new ArmCurrency(Math.round(amtDue)).truncate(0);
							 double premioAmountRefundStep = 1.0/LoyaltyEngine.defaultPointRatio;
							 System.out.println("premioAmountToRefund " + premioAmountToRefund.doubleValue());
							 System.out.println("premioAmountRefundStep " + premioAmountRefundStep);
							 if ((premioAmountToRefund.getDoubleValue() % premioAmountRefundStep) != 0.0) {
							 double patrValue = ((int)((premioAmountToRefund.doubleValue() + premioAmountRefundStep) / premioAmountRefundStep))
							 * premioAmountRefundStep;
							 System.out.println("==>> partValue: " + patrValue);
							 premioToReturn+=patrValue;
							 }else{
							 premioToReturn+=payment.getAmount().doubleValue();
							 }
							 System.out.println("==>> payment.getAmount(: " + payment.getAmount());*/
							double premioPayment = payment.getAmount().doubleValue();
							double premioAmountRefundStep = 1.0 / LoyaltyEngine.defaultPointRatio;
							premioToReturn += (Math.ceil(premioPayment / premioAmountRefundStep)) * premioAmountRefundStep;
						}
					}
				}
			}
		}

		System.out.println(" $$ isPartialPremioPayment " + isPartialPremioPayment);
		System.out.println(" $$ premioToReturn " + premioToReturn);
		if (isPremioPayment) {
			final ArmCurrency c1 = new ArmCurrency(Math.round(premioToReturn)).truncate(0);
			final ArmCurrency c2 = new ArmCurrency((-1) * Math.round(premioToReturn)).truncate(0);
			//  CMSApplet.theAppMgr.addStateObject("REFUND_PREMIO", new ArmCurrency(Math.round(premioToReturn)).truncate(0));
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					appButtonEvent("", new CMSActionEvent(this, 0, "PREMIO_DISCOUNT", 0), c1);
				}
			});
			//	  CMSApplet.theAppMgr.addStateObject("REFUND_PREMIO_RET", new ArmCurrency((-1)*Math.round(premioToReturn)).truncate(0));
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					appButtonEvent("", new CMSActionEvent(this, 0, "PREMIO_DISCOUNT", 0), c2);
				}
			});
			return true;
		} else
			return false;
	}

	private void forReturnTxn() {
		CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) CMSApplet.theAppMgr.getStateObject("TXN_POS");
		Payment[] paymentsAlready = theTxn.getPaymentsArray();
		for (int i = 0; i < paymentsAlready.length; i++) {
			System.out.println(" $$ " + paymentsAlready[i].getGUIPaymentName());
			//if (paymentsAlready[i].getGUIPaymentName().trim().equals("Premio Discount")) {
			if (paymentsAlready[i] instanceof CMSPremioDiscount) {
				return;
			}
		}
		POSLineItem[] retItems = theTxn.getReturnLineItemsArray();
		double amtDue = 0.0d;
		CMSCompositePOSTransaction origTxn = null;
		if (retItems != null) {
			for (int i = 0; i < retItems.length; i++) {
				CMSReturnLineItem retItem = (CMSReturnLineItem) retItems[i];
				if (((CMSReturnLineItemDetail) retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
					origTxn = (CMSCompositePOSTransaction) ((CMSReturnLineItemDetail) retItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail().getLineItem().getTransaction().getCompositeTransaction();
					/*if(origTxn !=null){
					 break;
					 }*/

					String loyaltyRewardRatio = CMSPremioDiscountBldr.loyaltyRewardRatio;
					double loyaltyRatio = 1.0;
					if (loyaltyRewardRatio != null) {
						loyaltyRatio = 1 / Double.parseDouble(loyaltyRewardRatio);
					}

					try {
						POSLineItemDetail[] posLineItems = retItem.getLineItemDetailsArray();
						for (int index = 0; index < posLineItems.length; index++) {
							//  	  amtDue = amtDue.add(new ArmCurrency(retItem.getLoyaltyPointsToReturn()*loyaltyRatio));
							amtDue = amtDue + origTxn.getPremioDiscountToReturn(((CMSReturnLineItemDetail) posLineItems[index]).getSaleLineItemDetail());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (origTxn != null) {
				Payment[] payments = origTxn.getPaymentsArray();
				for (int j = 0; j < payments.length; j++) {
					Payment payment = payments[j];
					if (payment instanceof CMSPremioDiscount) {
						/*if(amtDue.getDoubleValue()>payment.getAmount().doubleValue()){
						 CMSApplet.theAppMgr.addStateObject("REFUND_PREMIO",payment.getAmount());
						 }else{
						 CMSApplet.theAppMgr.addStateObject("REFUND_PREMIO",amtDue);
						 }*/
					ArmCurrency premioAmountToRefund = new ArmCurrency(Math.round(amtDue)).truncate(0);
						double premioAmountRefundStep = 1.0 / LoyaltyEngine.defaultPointRatio;
						if ((premioAmountToRefund.getDoubleValue() % premioAmountRefundStep) != 0.0) {
							double patrValue = ((int) ((premioAmountToRefund.doubleValue() + premioAmountRefundStep) / premioAmountRefundStep)) * premioAmountRefundStep;
							/* MD */System.out.println("==>> partValue: " + patrValue);
							premioAmountToRefund = new ArmCurrency(patrValue);
						}
						/* MD */System.out.println("==>> payment.getAmount(: " + payment.getAmount());
						/* MD */System.out.println("==>>theTxn.getCompositeTotalAmountDue()(: " + theTxn.getCompositeTotalAmountDue());
					ArmCurrency curr = null;
						try {

							if (premioAmountToRefund.greaterThan(payment.getAmount().absoluteValue())) {
								//                CMSApplet.theAppMgr.addStateObject("REFUND_PREMIO", payment.getAmount());
								curr = payment.getAmount();
							} else {
								//          CMSApplet.theAppMgr.addStateObject("REFUND_PREMIO", premioAmountToRefund);
								curr = premioAmountToRefund;
							}
						} catch (CurrencyException ce) {
						}
						final ArmCurrency passCurr = curr;
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								appButtonEvent("", new CMSActionEvent(this, 0, "PREMIO_DISCOUNT", 0), passCurr);
							}
						});
						return;
					}
				}
			}
		}

	}

	private void setLoyaltyPointsStatus() {
		double currpoints = 0.0;
		CMSCompositePOSTransaction posTran = null;
		if (!(theTxn instanceof CMSCompositePOSTransactionAppModel)) {
			System.out.println("theTxn not compositePOSTransaction ");
			return;
		}
		posTran = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		if (posTran.getCustomer() != null && posTran.getLoyaltyCard() != null) {
			// Applicable only to sale transactions
			if (
			// (!theTxn.isEmployeeSale) &&
			// (!isReservationOpenMode()) &&
			// (!isReservationCloseMode()) &&
			(posTran.getTransactionType().startsWith("SALE") || posTran.getTransactionType().startsWith("RETN") || posTran.getTransactionType().startsWith("PEND"))) {
				java.text.NumberFormat numFor = java.text.NumberFormat.getInstance();
				currpoints = posTran.getLoyaltyPoints();
				lblTxnLoyalty.setVisible(true);
				lblTxnLoyalty.setText(res.getString("Points you've got today") + ": " + numFor.format(currpoints));
			} else {
				lblTxnLoyalty.setVisible(false);
				lblTxnLoyalty.setText("");
			}
		} else {
			lblTxnLoyalty.setVisible(false);
			lblTxnLoyalty.setText("");
		}
		pointsAndDepositPanel.repaint();
	}
}
