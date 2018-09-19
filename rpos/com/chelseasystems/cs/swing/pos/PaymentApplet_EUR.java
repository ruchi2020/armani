/**
 * <p>Title:PaymentApplet_EUR </p>
 * <p>Description: Transaction posting applet  </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Retek
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | N/A        | Retek     | N/A       | Base Version                                       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-26-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 08-10-2005 | KS        | N/A       | POS_104665_TS_PostTransaction_Rev5.doc             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListHelper;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerAlertRule;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
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
import com.chelseasystems.cs.item.CMSItem;
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
import com.chelseasystems.cs.pos.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.collection.CMSMiscCollectionCredit;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cs.collection.CMSCollectionTransaction;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.authorization.ISDServiceManager;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.util.FiscalDocumentUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.fiscaldocument.FiscalInterface;
import com.armani.SetefiManagement;
import com.armani.CMSCreditCard;
import com.armani.Setefi;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.payment.CMSPaymentCode;
import java.util.List;
import com.chelseasystems.cr.tax.ValueAddedTaxDetail;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cs.tax.vat.VATUtilities;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentResponse;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cs.fiscaldocument.VATInvoice;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cr.currency.UnsupportedCurrencyTypeException;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

//Added by Satin
import java.io.*;
import java.nio.channels.*;
import com.chelseasystems.cs.util.*;
import com.ga.fs.fsbridge.ARMFSBridge;
import com.ga.fs.fsbridge.object.ARMFSBOPayInObject;
import com.ga.fs.fsbridge.object.ARMFSBOPayOutObject;
import com.ga.fs.fsbridge.object.ARMFSBOPosEventObject;
import com.ga.fs.fsbridge.object.ARMFSBOReceiptCopyObject;
import com.ga.fs.fsbridge.object.ARMFSBOReturnTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBOReturnWithSalesTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBOSaleTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBOTrainingTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBridgeObject;
//import javax.xml.bind.DatatypeConverter;
import com.ga.fs.fsbridge.utils.ARMEventIDGenerator;
import com.ga.fs.fsbridge.utils.ConfigurationManager;
import com.ga.fs.fsbridge.utils.MessageTypeCode;
import com.ga.fs.fsbridge.utils.PosEvent;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;

/**
 * put your documentation comment here
 */
public class PaymentApplet_EUR extends CMSApplet implements Setefi {
	static boolean isVatEnabled = new ConfigMgr("vat.cfg").getString(
	"VAT_ENABLED").equalsIgnoreCase("TRUE");

	// instance members used to prompt user with an initial payment
	private String INITIAL_PAYMENT;
	private Payment anInitialPayment;
	private boolean shouldTxnPostWhenBalanceIsZero;
	private CMSPaymentTransactionAppModel theTxn = null;
	private boolean changeDue = false;
	private boolean stubRequired = false;
	private boolean cancelTxnPressed = false;
	private boolean prevPressed = false;
	// Vishal Yevale : changes to solve vat amount issue 23 dec 2016
	private boolean isPriceOverridedItem = false;
	// end vishal yevale
	boolean isPostComplete = false;
	int action = -1;
	POSHeaderPanel pnlHeader = new POSHeaderPanel();
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
	DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
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
	// private FiscalDocumentUtil fiscalUtil = null;
	// private ScrollableToolBarPanel screen;
	private boolean isBackFromCustomerApplet;
	// for Europe
	private JLabel lblFiscalNo = new JLabel();
	private JLabel fiscalNoLbl = new JLabel();
	private FiscalInterface fiscalInterface = null;
	private String fiscalReceiptNumber = "N/A";

	private CMSCompositePOSTransaction theTxnPOS = null;
	private boolean isFiscalReceiptPrinted = false;
	private ArmCurrency creditCardAmountForSetefi = null;
	// For Credit Card Status Code
	String StatusString = null;
	private boolean postingInProgress = false;
	private FiscalDocumentUtil fiscalDocUtil = null;
	private boolean isCreditAuthComplete = true;
	// added vivek for Employee sale
	private int counter = 0;
	private CMSCustomer txnCust;
	ASISTxnData asisData;
	// Vishal Yevale : If appears the Budget Message , the VIP message is not
	// required. 29th May 2017
	private boolean proceedVIPdiscountMsg = true;
	// end Vishal Yevale 29 May 2017

	// RICCARDO
	private static boolean isHKenv = false;
	static {

		ConfigMgr armCfg = new ConfigMgr("arm.cfg");
		if ((armCfg.getString("HK_ENV") != null)
				&& ("TRUE".equalsIgnoreCase(armCfg.getString("HK_ENV").trim()))) {
			isHKenv = true;
		}

	}
	// END RICCARDO

	public static boolean POST_RULE_1; // Added by Yves Agbessi on 06-jun-2018
	// -> Handles France Fiscal Posting
	// Rules

	// Initialize the Applet
	public void init() {
		try {
			ConfigMgr mgr = new ConfigMgr("payment.cfg");
			asisData = new ASISTxnData();
			INITIAL_PAYMENT = mgr.getString("INITIAL_PAYMENT");
			// ks: Europe - No Default payment so INITIAL_PAYMENT will be set to
			// ""
			if (INITIAL_PAYMENT != null && INITIAL_PAYMENT.length() > 0) {
				// anInitialPayment = PaymentMgr.getPayment(INITIAL_PAYMENT);
				anInitialPayment = CMSPaymentMgr.getPayment(INITIAL_PAYMENT);
			}
			shouldTxnPostWhenBalanceIsZero = mgr.getString(
			"SHOULD_TXN_POST_WHEN_BALANCE_ZERO").equalsIgnoreCase(
			"true");
			// Fiscal interface
			ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
			String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
			Class cls = Class.forName(sClassName);
			fiscalInterface = (FiscalInterface) cls.newInstance();
			// end fiscal
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Component initialization
	private void jbInit() throws Exception {
		JPanel pnlCenter = new JPanel();
		JPanel pnlSummary = new JPanel();
		JPanel pnlSummaryLt = new JPanel();
		JPanel pnlSummaryMid = new JPanel();
		JPanel pnlSummaryRt = new JPanel();
		JLabel SubTotDetailLbl = new JLabel();
		SubTotLbl = new JLabel();
		JLabel TranDiscLbl = new JLabel();
		JLabel TotalLbl = new JLabel();
		JPanel pnlSouth = new JPanel();
		JPanel pnlPayments = new JPanel();
		JPanel pnlTendered = new JPanel();
		JPanel pnlFiscal = new JPanel();
		JPanel pnlEmpty = new JPanel();
		JPanel pnlDue = new JPanel();
		JLabel lblAmt = new JLabel();
		this.setSize(844, 605);
		this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
		this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
		this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		pnlHeader.setPreferredSize(new Dimension(10, 64));
		pnlSummary.add(pnlSummaryLt, null);
		pnlSummaryLt.add(SubTotLbl, null);
		pnlSummaryLt.add(TranDiscLbl, null);
		// pnlSummaryLt.add(SalesTaxLbl, null);
		pnlSummaryLt.add(TotalLbl, null);
		pnlSummary.add(pnlSummaryMid, null);
		pnlSummaryMid.add(SubTotDetailLbl, null);
		pnlSummaryMid.add(TranDiscDetailLbl, null);
		pnlSummaryMid.add(TotalTaxDetailLbl, null);
		pnlSummary.add(pnlSummaryRt, null);
		pnlSummaryRt.add(SubTot$Lbl, null);
		pnlSummaryRt.add(TranDisc$Lbl, null);
		// pnlSummaryRt.add(SalesTax$Lbl, null);
		pnlSummaryRt.add(TotalAmt$Lbl, null);
		pnlCenter.setLayout(new BorderLayout());
		pnlCards = new JPanel();
		card = new CardLayout();
		pnlCards.setLayout(card);
		pnlCards.add(pnlPayments, "payments");
		pnlCenter.add(pnlCards, BorderLayout.CENTER);
		pnlSouth.add(pnlFiscal, null);
		pnlSouth.add(pnlTendered, null);
		pnlTendered.add(lblAmt, null);
		pnlTendered.add(AmtTnd$Lbl, null);
		pnlSouth.add(pnlEmpty, null);
		pnlSouth.add(pnlDue, null);
		pnlDue.add(lblDue, null);
		pnlDue.add(AmtDue$Lbl, null);
		pnlEmpty.setOpaque(false);
		pnlFiscal.setOpaque(false);
		pnlDue.setOpaque(false);
		pnlTendered.setOpaque(false);
		// For Europe Fiscal Number
		pnlFiscal.add(lblFiscalNo, null);
		pnlFiscal.add(fiscalNoLbl, null);
		// pnlSouth.add(fiscalNoLbl, null);
		lblFiscalNo.setText(res.getString("Fiscal No") + ":");
		lblFiscalNo.setForeground(Color.BLACK);
		lblFiscalNo.setHorizontalAlignment(SwingConstants.CENTER);
		lblFiscalNo.setVerticalAlignment(SwingConstants.CENTER);
		lblFiscalNo.setPreferredSize(new Dimension(130, 22));
		lblFiscalNo.setFont(theAppMgr.getTheme().getPromptAreaFont());
		lblFiscalNo.setForeground(Color.BLACK);
		// pnlSouth.add(fiscalNoLbl, null);
		fiscalNoLbl.setText(res.getString("N/A"));
		fiscalNoLbl.setHorizontalAlignment(SwingConstants.CENTER);
		fiscalNoLbl.setVerticalAlignment(SwingConstants.CENTER);
		fiscalNoLbl.setPreferredSize(new Dimension(130, 22));
		fiscalNoLbl.setFont(theAppMgr.getTheme().getPromptAreaFont());
		fiscalNoLbl.setForeground(Color.BLACK);

		// end Europe fiscal Number
		lblDue.setText(res.getString("Amount Due") + ":");
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
		// AmtDue$Lbl.setFont(theAppMgr.getTheme().getPromptAreaFont());
		pnlEmpty.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
		pnlFiscal.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
		pnlDue.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
		AmtTnd$Lbl.setText("0.00");
		AmtTnd$Lbl.setHorizontalAlignment(4);
		AmtTnd$Lbl.setPreferredSize(new Dimension(100, 22));
		AmtTnd$Lbl.setForeground(new Color(0, 0, 175));
		String strFont_amtTnd = cfgMgr.getString("FONT.AMOUNT_DUE");
		Font font_amtTnd = getFont(strFont_amtTnd);
		AmtTnd$Lbl.setFont(font_amtTnd);
		AmtTnd$Lbl.setForeground(new Color(0, 0, 175));
		// AmtTnd$Lbl.setFont(theAppMgr.getTheme().getPromptAreaFont());
		lblAmt.setText(res.getString("Amount Tendered") + ":");
		lblAmt.setFont(font_amtTnd);
		lblAmt.setForeground(new Color(0, 0, 175));
		// lblAmt.setFont(theAppMgr.getTheme().getPromptAreaFont());
		pnlTendered.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
		pnlPayments.setBackground(Color.white);
		pnlPayments.setLayout(new BorderLayout());
		pnlSouth.setPreferredSize(new Dimension(10, 48));
		pnlSouth.setBackground(theAppMgr.getBackgroundColor());
		pnlSouth.setLayout(new GridLayout(2, 2));
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
		TranDiscLbl.setText(" " + res.getString("Markdown") + " / "
				+ res.getString("Discount"));
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
		TableColumnModel modelColumn = tblPayment.getColumnModel();
		PaymentRenderer paymentRenderer = new PaymentRenderer();
		for (int i = 0; i < model.getColumnCount(); i++) {
			modelColumn.getColumn(i).setCellRenderer(paymentRenderer); // where
			// renderer
			// is an
			// instance
			// of
			// your
			// renderer
			// class
		}

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

	// Start the applet
	/*
	 * public void start() { boolean bBackFromPrintFiscalApplet = false;
	 * fiscalDocUtil = new FiscalDocumentUtil(); // fiscalReceiptNumber = null;
	 * postingInProgress = false;
	 * theAppMgr.removeStateObject("MISC_ITEM_TEMPLATE");
	 * theAppMgr.removeStateObject("ITEM_BUILDER_STR"); if
	 * (theAppMgr.getStateObject("TXN_CUSTOMER") != null) { txnCust =
	 * (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
	 * theAppMgr.removeStateObject("TXN_CUSTOMER"); Object object =
	 * theAppMgr.getStateObject("TXN_POS"); if (object instanceof
	 * CMSCompositePOSTransaction) { CMSCompositePOSTransaction theTxnPOS =
	 * (CMSCompositePOSTransaction) object; //PCR: Alert the employee when he
	 * exceeds the sales limit String customerType = txnCust.getCustomerType();
	 * if (customerType != null &&
	 * customerType.equals(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)) {
	 * try{ CMSCustomer customer = (CMSCustomer) theTxnPOS.getCustomer(); String
	 * countryCode = (theTxnPOS.getStore()).getPreferredISOCountry(); CMSStore
	 * store = (CMSStore) theTxnPOS.getStore(); String brand_ID =
	 * store.getBrandID(); CMSCustomerAlertRule[] customerAlertRules = null;
	 * double ruleDiscountPct = 0.0d; customerAlertRules =
	 * CMSCustomerHelper.getAllCustomerAlertRules(CMSApplet.theAppMgr,
	 * countryCode,brand_ID); if(customerAlertRules!=null) ruleDiscountPct =
	 * Double.parseDouble(customerAlertRules[0].getDsc_level()); POSLineItem []
	 * lineItm = theTxnPOS.getLineItemsArray(); for(int
	 * i=0;i<lineItm.length;i++){ Discount dis = lineItm[i].getDiscount();
	 * if(dis==null){ counter= counter+1; //callRule(true); }else{ double
	 * discountpct = lineItm[i].getDiscount().getPercent();
	 * if(((discountpct*100)==ruleDiscountPct) || (ruleDiscountPct == 0.0) ){
	 * counter = counter+1;
	 * 
	 * } } } if(counter!=0) callRule(counter);
	 * 
	 * 
	 * } catch(Exception e){ e.printStackTrace(); }
	 * 
	 * /* try { theTxnPOS.validateEmployeeRuleForCurrentTransaction(txnCust); }
	 * catch (BusinessRuleException bre) { if (bre.getMessage() != null &&
	 * bre.getMessage().length() > 0) { if
	 * (!theAppMgr.showOptionDlg("Employee Alert", bre.getMessage())) {
	 * SwingUtilities.invokeLater(new Runnable() { public void run() {
	 * theAppMgr.fireButtonEvent("PREV"); } }); return; } } } }
	 */
	// Start the applet
	public void start() {
		boolean bBackFromPrintFiscalApplet = false;
		fiscalDocUtil = new FiscalDocumentUtil();
		// fiscalReceiptNumber = null;
		postingInProgress = false;
		// Vishal Yevale : If appears the Budget Message , the VIP message is
		// not required. 29th May 2017
		proceedVIPdiscountMsg = true;
		// end Vishal Yevale 29 May 2017
		theAppMgr.removeStateObject("MISC_ITEM_TEMPLATE");
		theAppMgr.removeStateObject("ITEM_BUILDER_STR");
		if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
			txnCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
			theAppMgr.removeStateObject("TXN_CUSTOMER");
			Object object = theAppMgr.getStateObject("TXN_POS");
			if (object instanceof CMSCompositePOSTransaction) {
				CMSCompositePOSTransaction theTxnPOS = (CMSCompositePOSTransaction) object;
				// PCR: Alert the employee when he exceeds the sales limit
				String customerType = txnCust.getCustomerType();
				if (customerType != null
						&& customerType
						.equals(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)) {
					try {
						CMSCustomer customer = (CMSCustomer) theTxnPOS
						.getCustomer();
						String countryCode = (theTxnPOS.getStore())
						.getPreferredISOCountry();
						CMSStore store = (CMSStore) theTxnPOS.getStore();
						String brand_ID = store.getBrandID();
						CMSCustomerAlertRule[] customerAlertRules = null;
						// vishal: to control multiple discount level
						String ruleDiscountPct = "";
						Date date = (Date) theAppMgr
						.getGlobalObject("PROCESS_DATE");
						java.sql.Date businessDate = new java.sql.Date(
								date.getTime());
						// customerAlertRules =
						// CMSCustomerHelper.getAllCustomerAlertRules(CMSApplet.theAppMgr,
						// countryCode,brand_ID);
						customerAlertRules = CMSCustomerHelper
						.getAllCustomerAlertRules(CMSApplet.theAppMgr,
								countryCode, brand_ID, businessDate);
						if (customerAlertRules != null)
							ruleDiscountPct = String.valueOf(Double
									.parseDouble(customerAlertRules[0]
									                                .getDsc_level()));
						String validDiscounts = "";
						String records = "";
						boolean flag = false;
						if (customerAlertRules != null
								&& customerAlertRules.length > 1) {
							for (int countOne = 0; countOne < customerAlertRules.length; countOne++) {
								if (!records.contains(String.valueOf(countOne))) {
									for (int countTwo = countOne + 1; countTwo < customerAlertRules.length; countTwo++) {
										if (!records.contains(String
												.valueOf(countTwo))) {
											if ((customerAlertRules[countOne]
											                        .getPriority().intValue() == customerAlertRules[countTwo]
											                                                                        .getPriority().intValue())
											                                                                        && (customerAlertRules[countOne]
											                                                                                               .getRecordType()
											                                                                                               .toString()
											                                                                                               .equalsIgnoreCase(customerAlertRules[countTwo]
											                                                                                                                                    .getRecordType()
											                                                                                                                                    .toString()))
											                                                                                                                                    && (customerAlertRules[countOne]
											                                                                                                                                                           .getValue()
											                                                                                                                                                           .toString()
											                                                                                                                                                           .equalsIgnoreCase(customerAlertRules[countTwo]
											                                                                                                                                                                                                .getValue()
											                                                                                                                                                                                                .toString()))) {
												if (!validDiscounts
														.contains(String.valueOf(Double
																.parseDouble(customerAlertRules[countTwo]
																                                .getDsc_level()))))
													validDiscounts += String
													.valueOf(Double
															.parseDouble(customerAlertRules[countTwo]
															                                .getDsc_level()))
															                                + ",";
												records += String
												.valueOf(countTwo)
												+ ",";
												flag = true;
											}
										}
									}
									if (flag) {
										if (!validDiscounts
												.contains(String.valueOf(Double
														.parseDouble(customerAlertRules[countOne]
														                                .getDsc_level()))))
											validDiscounts += String
											.valueOf(Double
													.parseDouble(customerAlertRules[countOne]
													                                .getDsc_level()));
									}
									flag = false;
								}
							}
						}
						if (validDiscounts != null || validDiscounts != "")
							ruleDiscountPct = validDiscounts;
						POSLineItem[] lineItm = theTxnPOS.getLineItemsArray();
						for (int i = 0; i < lineItm.length; i++) {
							Discount dis = lineItm[i].getDiscount();
							if (dis == null) {
								// counter= counter+1; // Commented by Yves
								// Agbessi on 29-May-2018 -> BUG FIX (?)
								// callRule(true);
							} else {
								double discountpct = lineItm[i].getDiscount()
								.getPercent();
								// if(((discountpct*100)==ruleDiscountPct) ||
								// (ruleDiscountPct == 0.0) ){
								if ((ruleDiscountPct.contains(String
										.valueOf(discountpct * 100)))
										|| (ruleDiscountPct == null)
										|| (ruleDiscountPct == "")) {
									counter = counter + 1;

								}
							}
						}
						if (counter != 0)
							callRule(counter);

					} catch (Exception e) {
						e.printStackTrace();
					}

					/*
					 * try {
					 * theTxnPOS.validateEmployeeRuleForCurrentTransaction(
					 * txnCust); } catch (BusinessRuleException bre) { if
					 * (bre.getMessage() != null && bre.getMessage().length() >
					 * 0) { if (!theAppMgr.showOptionDlg("Employee Alert",
					 * bre.getMessage())) { SwingUtilities.invokeLater(new
					 * Runnable() { public void run() {
					 * theAppMgr.fireButtonEvent("PREV"); } }); return; } } }
					 */
				}
				try {
					// PCR: Dummy or default customer not allowed for
					// Reservation,
					// Deposit and Credid transactions
					theTxnPOS.setCustomer(txnCust);
					VATUtilities.applyVAT(theAppMgr, theTxnPOS,
							(CMSStore) theStore, (CMSStore) theStore,
							theTxnPOS.getProcessDate());
					// TaxUtilities.applyTax(theAppMgr, theTxnPOS,
					// (CMSStore) theTxnPOS.getStore(),
					// (CMSStore) theTxnPOS.getStore(),
					// theTxnPOS.getProcessDate());
					// sonali:priceOveride
					CMSCompositePOSTransaction trans = (CMSCompositePOSTransaction) theAppMgr
					.getStateObject("TXN_POS");

					POSLineItem[] lineItem = trans.getSaleLineItemsArray();
					for (int i = 0; i < lineItem.length; i++) {
						if (lineItem[i].getOverrideAmount() != null) {
							lineItem[i].setManualUnitPrice(lineItem[i]
							                                        .getOverrideAmount());
							// lineItem[i].doSetItemRetailPrice(lineItem[i].getItemRetailPrice());
							// lineItem[i].doSetItemSellingPrice(lineItem[i].getItemSellingPrice());
						}
					}
					LightPoleDisplay.getInstance().displayMessage(
							txnCust.getFirstName(), txnCust.getLastName());
				} catch (BusinessRuleException bre) {
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
				} catch (Exception e) {
					theAppMgr.removeStateObject("TXN_CUSTOMER");
				}
			}
		}

		if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null) {
			theAppMgr.removeStateObject("ARM_PRINT_FISCAL_MODE");
			System.out
			.println("======================Back From Print Fiscal Applet===================");
			bBackFromPrintFiscalApplet = true;
		} else {
			System.out
			.println("========================Not Back From Print Fiscal Applet===============");
		}
		// fiscalUtil = new FiscalDocumentUtil();
		LightPoleDisplay.getInstance().stopDefaultDisplay();
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		cfgMgr = new ConfigMgr("payment.cfg");
		isAutoChange = new Boolean(cfgMgr.getString("AUTO_CHANGE"))
		.booleanValue();
		changeDue = false;
		isPostComplete = false;
		cancelTxnPressed = false;
		// if(theAppMgr.getCallingApplet() instanceof Customer )
		isBackFromCustomerApplet = false;
		prevPressed = false;
		model.clear();
		card.show(pnlCards, "payments");
		theTxn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr
				.getStateObject("TXN_POS")).getAppModel(
						CMSAppModelFactory.getInstance(), theAppMgr);
		// if re-rentry mode, set attribute
		if (theAppMgr.getStateObject("RE_ENTER") != null) {
			String ticket = (String) theAppMgr.getStateObject("RE_ENTER");
			try {
				theTxn.setHandWrittenTicketNumber(ticket);
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(ex.getMessage());
			}
		}
		// Seed the model with the payments...
		Payment[] payments = theTxn.getPaymentsArray();
		for (int idx = 0; idx < payments.length; idx++) {
			model.addPayment(payments[idx]);
			model.fireTableDataChanged();
		}
		model.firstPage();
		// initButtons(payMgr.CREDIT);
		initHeaders();
		initValues();
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
		if ((theTxn.getPaymentTransaction().getId().length() == 0)) {
			selectPayment();
		}
		updateLabels();
		// if (!bBackFromPrintFiscalApplet)
		// createTxnNumber();
		SubTotLbl.setText(" "
				+ res.getString(theTxn.getPaymentScreenRetailLabel()));
		String giveCustomerVIPDiscount = (String) theAppMgr
		.getStateObject("ARM_ADDED_CUSTOMER");
		if (theTxn.getCustomer() != null) {
			// Vishal Yevale : If appears the Budget Message , the VIP message
			// is not required. 29th May 2017
			if (((CMSCustomer) theTxn.getCustomer()).getVIPDiscount() > 0
					&& proceedVIPdiscountMsg) {
				// end Vishal Yevale 29 May 2017
				NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMaximumFractionDigits(1);
				// ORGA-IS
				// theAppMgr.showErrorDlg(res.getString("Customer has VIP discount: ")
				// +
				// nf.format(((CMSCustomer)theTxn.getCustomer()).getVIPDiscount()));
				theAppMgr.showErrorDlg(res
						.getString("Customer has VIP discount: ")
						+ nf.format(((CMSCustomer) theTxn.getCustomer())
								.getVIPDiscount() / 100));
			}
			// if (giveCustomerVIPDiscount != null &&
			// giveCustomerVIPDiscount.equals("TRUE")
			// &&
			// (((CMSCustomer)theTxn.getCustomer()).getCustomerType().trim().equalsIgnoreCase("4")
			// ||
			// ((CMSCustomer)theTxn.getCustomer()).getCustomerType().trim().equalsIgnoreCase("VIP")))
			// {
			// theAppMgr.showErrorDlg(res.getString("Customer has VIP discount: 10%"));
			// }
			theAppMgr.removeStateObject("ARM_ADDED_CUSTOMER");
		}
		if (bBackFromPrintFiscalApplet) {
			System.out
			.println("===================Print Fiscal Mode==================");
			theAppMgr.showMenu(MenuConst.PRINT_FISCAL_DOCUMENT, "PRINT_FISCAL",
					theOpr);
			theAppMgr.setSingleEditArea("Select Option");
		}
		// VM: Check for CUST_NEEDED_PAYMENT object for customer requirement
		Object custNeededPaymentObj = theAppMgr
		.getStateObject("CUST_NEEDED_PAYMENT");
		if (custNeededPaymentObj != null
				&& custNeededPaymentObj instanceof Payment
				&& theTxn.getCustomer() != null
				&& theTxn.getCustomer().getId() != null
				&& theTxn.getCustomer().getId().trim().length() != 0) {
			objectEvent("PAYMENT", custNeededPaymentObj);
		}
		theAppMgr.removeStateObject("CUST_NEEDED_PAYMENT");
		// screen = (ScrollableToolBarPanel)
		// AppManager.getCurrent().getMainFrame().
		// getAppToolBar();
		// if (screen != null) {
		// screen.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
		// 0));
		// }

		// Riccardino implementation ...
		fiscalReceiptNumber = fiscalInterface.getNextFiscalReceiptNo(true);
		if (fiscalReceiptNumber == "0") {
			if (!theAppMgr
					.showOptionDlg("Errore",
					"Errore Stampante Fiscale.Riaccendere la stampante e rifare lo scontrino.")) {
				return;
			}
		} else {
			fiscalNoLbl.setText(fiscalReceiptNumber);
		}

	}

	/**
	 * Creates Transaction Number For Europe: Gets the fiscal number
	 */
	private void createTxnNumber() {
		try {
			// set ID
			if ((theTxn.getPaymentTransaction().getId() == null)
					|| (theTxn.getPaymentTransaction().getId().equals(""))) {
				txnNum = CMSTransactionNumberHelper.getNextTransactionNumber(
						theAppMgr, (CMSStore) theStore,
						(CMSRegister) theAppMgr.getGlobalObject("REGISTER"));
				theTxn.getPaymentTransaction().setId(txnNum);
				// fiscalReceiptNumber = null;
			}
			if (fiscalReceiptNumber == null
					|| fiscalReceiptNumber.length() == 0) {

				// fiscalReceiptNumber =
				// fiscalInterface.getNextFiscalReceiptNo();

				// String fiscalRcptNumber =
				// (String)theAppMgr.getStateObject("FISCAL_RCPT_NUMBER");
				// if(fiscalRcptNumber!=null && fiscalRcptNumber.length()>0){
				// fiscalReceiptNumber = fiscalRcptNumber;
				// System.out.println("localfiscal receipt number found locally. ");
				// }else{
				// System.out.println("calling fiscalreceipt method again");
				// fiscalReceiptNumber =
				// fiscalInterface.getNextFiscalReceiptNo();
				// }
				if (fiscalReceiptNumber != null)
					fiscalNoLbl.setText(fiscalReceiptNumber);
				else
					fiscalNoLbl.setText(res.getString("N/A"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Stop the applet
	public void stop() {
		if ((new ConfigMgr("JPOS_peripherals.cfg").getString("MSR_DEVICE_TYPE"))
				.equals("INGENICO_JPOS")) {
			ET1000JPOSForm.getInstance().displayLogo();
		}
		if (!isPostComplete) {
			if (!prevPressed) {
				if (theTxn != null) {
					System.out
					.println("calling app model to print cancel receipt");
					theTxn.printCancelReceipt(theAppMgr);
				}
			}
		}
	}

	// Destroy the applet
	public void destroy() {
	}

	// Get Applet screen name
	public String getScreenName() {
		return (res.getString("Add Tender"));
	}

	// Get Applet information
	public String getVersion() {
		return ("$Revision: 1.6 $");
	}

	/**
	 */
	private void initValues() {
		TranDiscDetailLbl.setText("");
		/*
		 * Payment Applet currently cannot handle displaying more than one
		 * discount while the business objects do allow this. For this
		 * implementation, just take the last discount.
		 */
		if (theTxn.getDiscountsArray().length > 0) {
			Discount dis = theTxn.getDiscountsArray()[theTxn
			                                          .getDiscountsArray().length - 1];
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
		// if (theTxn.getTaxExemptId().length() > 0 ||
		// theTxn.getRegionalTaxExemptId().length() > 0) {
		// // special regional tax code
		// StringBuffer buf = new StringBuffer();
		// if (theTxn.getTaxExemptId().length() > 0) {
		// buf.append(res.getString(theTxn.getTaxLabel()));
		// }
		// if (theTxn.getRegionalTaxExemptId().length() > 0)
		// {//((CMSStore)theStore).usesRegionalTaxCalculations()
		// if (buf.length() > 0) {
		// buf.append("/");
		// }
		// buf.append(res.getString(theTxn.getRegionalTaxLabel()));
		// }
		// buf.append(" ");
		// buf.append(res.getString("Exempt ID"));
		// buf.append(": ");
		// if (theTxn.getTaxExemptId().length() > 0) {
		// buf.append(theTxn.getTaxExemptId());
		// }
		// if (theTxn.getRegionalTaxExemptId().length() > 0)
		// {//((CMSStore)theStore).usesRegionalTaxCalculations()
		// if (theTxn.getTaxExemptId().length() > 0) {
		// buf.append("/");
		// }
		// buf.append(theTxn.getRegionalTaxExemptId());
		// }
		// TotalTaxDetailLbl.setText(buf.toString());
		// }
		// else {
		// TotalTaxDetailLbl.setText("");
		// }
		SubTot$Lbl.setText(theTxn.getCompositeRetailAmount()
				.formattedStringValue());
		TotalAmt$Lbl.setText(theTxn.getCompositeTotalAmountDue()
				.formattedStringValue());
		TranDisc$Lbl.setText(theTxn.getCompositeReductionAmount()
				.formattedStringValue());
		// special regional tax code
		if (((CMSStore) theStore).usesRegionalTaxCalculations()) {
			// SalesTaxLbl.setText(" " +
			// res.getString(((CMSStore)theStore).getTaxLabel())
			SalesTaxLbl
			.setText(" "
					+ res.getString("Sales Tax")
					+ "/"
					+ res.getString(((CMSStore) theStore)
							.getRegionalTaxLabel()));
			SalesTax$Lbl
			.setText(" "
					+ (theTxn.getCompositeTaxAmount() == null ? new ArmCurrency(
							0).formattedStringValue() : theTxn
							.getCompositeTaxAmount()
							.formattedStringValue())
							+ "/"
							+ theTxn.getCompositeRegionalTaxAmount()
							.formattedStringValue());
		} else {
			// SalesTaxLbl.setText(" " +
			// res.getString(((CMSStore)theStore).getTaxLabel()));
			SalesTaxLbl.setText(" " + res.getString("Sales Tax"));
			SalesTax$Lbl
			.setText((theTxn.getCompositeTaxAmount() == null ? new ArmCurrency(
					0).formattedStringValue() : theTxn
					.getCompositeTaxAmount().formattedStringValue()));
		}
		if (isVatEnabled) {
			if (theTxn instanceof CMSCompositePOSTransactionAppModel) {
				CMSCompositePOSTransaction posTran = (CMSCompositePOSTransaction) theTxn
				.getPaymentTransaction();
				if (posTran.isPostAndPack()) {
					try {
						SalesTaxLbl
						.setText(" "
								+ res.getString("Post and Pack Charge / VAT Refund"));
						TotalTaxDetailLbl.setText(posTran
								.getPostAndPackChargeAmount()
								.formattedStringValue()
								+ res.getString(" / ")
								+ posTran.getCompositeVatAmount()
								.formattedStringValue());
						SalesTax$Lbl.setText(posTran
								.getPostAndPackChargeAmount()
								.subtract(posTran.getCompositeVatAmount())
								.formattedStringValue());
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
		AmtDue$Lbl.setText(theTxn.getCompositeTotalAmountDue()
				.formattedStringValue());
		LightPoleDisplay.getInstance().displayMessage(AmtDue$Lbl.getText(), "");
	}

	/**
	 */
	private void initHeaders() {
		try {
			pnlHeader.setAppMgr(theAppMgr);
			if (theTxn.getPaymentTransaction() instanceof CMSMiscCollection) {
				pnlHeader.setProperties(
						(CMSCustomer) ((CMSMiscCollection) theTxn
								.getPaymentTransaction()).getCustomer(),
								(CMSEmployee) theOpr, null);
			}
			// MP: In case of PaidOut, no customer associated just the Employee.
			else if (theTxn.getPaymentTransaction() instanceof PaidOutTransaction) {
				pnlHeader.setProperties(null, (CMSEmployee) theOpr, null);
			} else {
				// added by vivek for privacy mgmt
				if ((CMSCustomer) theTxn.getCustomer() == null
						&& !theAppMgr.isOnLine()) {
					CMSCustomer cust = ((CMSCustomer) theAppMgr
							.getStateObject("OFFLINE_CUST"));
					if (cust != null) {
						pnlHeader.setProperties(cust, (CMSEmployee) theOpr,
								(CMSEmployee) theTxn.getConsultant(),
								(CMSStore) theStore, theTxn.getTaxExemptId(),
								theTxn.getRegionalTaxExemptId());
					}
					// ended by vivek for privacy mgmt
				} else {
					pnlHeader.setProperties((CMSCustomer) theTxn.getCustomer(),
							(CMSEmployee) theOpr,
							(CMSEmployee) theTxn.getConsultant(),
							(CMSStore) theStore, theTxn.getTaxExemptId(),
							theTxn.getRegionalTaxExemptId());

				}
			}
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sValue
	 * @return
	 */
	private boolean isANumber(String sValue) {
		String sTmp = "0123456789";
		try {
			for (int iCtr = 0; iCtr < sValue.length(); iCtr++) {
				if (sTmp.indexOf(sValue.substring(iCtr, iCtr)) == -1)
					return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @param Command
	 * @param Value
	 */
	public void editAreaEvent(String Command, String Value) {
		// if (Command.equals("CREDIT_CARD")) { // credit card swipe date
		// String Builder = PaymentMgr.getPaymentBuilder(Command);
		// System.out.println("the builder is: " + Builder);
		// theAppMgr.buildObject("PAYMENT", Builder, Value);
		// }
		// above code moved to builder
		if (Command.equals("MODIFY_DDT_NO")) {
			if (!isANumber(Value)) {
				theAppMgr.showErrorDlg(res
						.getString("DDT number should be a number"));
				selectModifyDDT();
				return;
			}
			this.modifyDDTNumber(Value);
			return;
		}
		if (Command.equals("MODIFY_TAX_NO")) {
			if (!isANumber(Value)) {
				theAppMgr.showErrorDlg(res
						.getString("Tax number should be a number"));
				selectModifyTaxNumber();
				return;
			}
			modifyTAXNumber(Value);
			return;
		}
		if (Command.equals("MODIFY_CREDIT_NOTE")) {
			if (!isANumber(Value)) {
				theAppMgr.showErrorDlg(res
						.getString("CreditNote number should be a number"));
				selectModifyCreditNote();
				return;
			}
			modifyCreditNote(Value);
			return;
		}
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
				theAppMgr.showErrorDlg(res
						.getString("You must first select a payment."));
				selectOption();
				return;
			}
			Payment pay = model.getPayment(row);
			if (pay instanceof CreditCard) {
				CreditCard cc = (CreditCard) pay;
				if (!cc.isAuthRequired()) {
					theAppMgr
					.showErrorDlg(res
							.getString("This credit card has already been authorized."));
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
					theAppMgr
					.showErrorDlg(res
							.getString("This check has already been authorized."));
					selectOption();
					return;
				}
				if (chk.getRespStatusCode().equals(Payment.DECLINED)
						& theAppMgr.isOnLine()) {
					theAppMgr
					.showErrorDlg(res
							.getString("This check can not have a manual override."));
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
			theAppMgr
			.showErrorDlg(res
					.getString("The payment does not require a manual override."));
			selectOption();
		}
	}

	/**
	 */
	private void initReceiptButtons() {
		theAppMgr.showMenu(MenuConst.POST_TXN_EU, theOpr);
	}

	/**
	 * @param type
	 */
	private void initButtons(int type) {
		// String[] pay = PaymentMgr.getPayments(type,
		// theTxn.getPaymentTransaction());
		try {
			String[] pay = CMSPaymentMgr.getPayments(type,
					theTxn.getPaymentTransaction());
			JButton[] btns = new JButton[pay.length + 2]; // allow for prev, and
			// delete button
			for (int x = 0; x < pay.length; x++) {

				// Payment payment = PaymentMgr.getPayment(pay[x]);
				Payment payment = CMSPaymentMgr.getPayment(pay[x]);
				if (pay[x].equalsIgnoreCase("CREDIT_CARD")) {
					btns[x] = new JButton(HTML.formatLabeltoHTML(res
							.getString("Credit Card/Debit Card"), theAppMgr
							.getTheme().getButtonFont(), theAppMgr.getTheme()
							.getButtonTextColor()));
				} else {
					btns[x] = new JButton(HTML.formatLabeltoHTML(res
							.getString(payment.getGUIPaymentName()), theAppMgr
							.getTheme().getButtonFont(), theAppMgr.getTheme()
							.getButtonTextColor()));
				}
				if (pay[x].equalsIgnoreCase("STORE_VALUE_CARD")) {
					btns[btns.length - 1] = new JButton(HTML.formatLabeltoHTML(
							res.getString("Gift Card"), theAppMgr.getTheme()
							.getButtonFont(), theAppMgr.getTheme()
							.getButtonTextColor()));
					btns[btns.length - 1].setActionCommand(pay[x]);
				}
				btns[x].setActionCommand(pay[x]);

			}
			btns[btns.length - 2] = new JButton(
					HTML.formatLabeltoHTML(res.getString("Delete Payment"),
							theAppMgr.getTheme().getButtonFont(), theAppMgr
							.getTheme().getButtonTextColor()));
			btns[btns.length - 2].setActionCommand("DEL_PAY");
			// btns[btns.length - 2] = new
			// JButton(HTML.formatLabeltoHTML(res.getString("Suspend"),
			// theAppMgr.getTheme().getButtonFont(),
			// theAppMgr.getTheme().getButtonTextColor()));
			// btns[btns.length - 2].setActionCommand("SUSPEND");
			btns[btns.length - 1] = new JButton(
					HTML.formatLabeltoHTML(res.getString("Previous"), theAppMgr
							.getTheme().getButtonFont(), theAppMgr.getTheme()
							.getButtonTextColor()));
			btns[btns.length - 1].setActionCommand("PREV");
			theAppMgr.setButtons(btns);
		}
		// added by shushma for disable payment option, if no payment is enabled
		// and pay throws NullPointerException
		catch (NullPointerException npe) {
			theAppMgr
			.showErrorDlg("All payment options are disabled. Please enable at least one payment option in payment.cfg and restart Client. Shutting Down....");
			System.exit(1);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theCommand
	 * @param currency
	 */
	public void editAreaEvent(String theCommand, ArmCurrency currency) {
		if (theCommand.equals("CREDIT_CARD_AMOUNT")) {

			/*
			 * if (!(TransactionUtil.validatePositiveAmount(theAppMgr,
			 * "CREDIT_CARD", "CREDIT_CARD", currency))) { isCreditAuthComplete
			 * =true; SwingUtilities.invokeLater(new Runnable() {
			 * 
			 * public void run() { theAppMgr.unRegisterSingleEditArea();
			 * theAppMgr.buildObject("PAYMENT",
			 * CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD"),
			 * PaymentApplet_EUR.this.creditCardAmountForSetefi); } }); return;
			 * 
			 * }
			 */

			if (TransactionUtil.validateChangeAmount(theAppMgr, "CREDIT_CARD",
					"CREDIT_CARD", currency)) {
				this.creditCardAmountForSetefi = currency;
				double p = 0.0d;
				p = currency.doubleValue();
				try {
					SetefiManagement sm = new SetefiManagement(this,
							new Double(p));
					theAppMgr.unRegisterSingleEditArea();
					theAppMgr.setSingleEditArea(res
							.getString("Credit Auth in progress..."));
					isCreditAuthComplete = false;
					// theAppMgr.getParentFrame().toBack();
					/*
					 * 
					 * Added by Yves Agbessi (15-Nov-2017) Handles the case when
					 * the COM PORT configured in ArmaniJPOS.xml is not found
					 */
					/*
					 * boolean serialPortLinkCreationResult =
					 * sm.createLink(sm.createJposEntry(), true); if
					 * (!serialPortLinkCreationResult) { isCreditAuthComplete =
					 * true; this.theAppMgr.getParentFrame().toBack(); String
					 * Builder = CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
					 * System.out.println("the builder is: " + Builder);
					 * 
					 * }// End of Yves Agbessi patch
					 */
					theAppMgr.unRegisterSingleEditArea();
					return;
				} catch (Exception e) {
					isCreditAuthComplete = true;
					e.printStackTrace();
					this.theAppMgr.getParentFrame().toBack();
					String Builder = CMSPaymentMgr
					.getPaymentBuilder("CREDIT_CARD");
					theAppMgr.unRegisterSingleEditArea();
					return;
				}
			} else {
				try {
					ArmCurrency amt = theTxn.getCompositeTotalAmountDue()
					.subtract(theTxn.getTotalPaymentAmount());
					theAppMgr.setSingleEditArea(
							res.getString("Enter amount....."),
							"CREDIT_CARD_AMOUNT", amt.absoluteValue(),
							theAppMgr.CURRENCY_MASK);
				} catch (Exception e) {
					theAppMgr.setSingleEditArea(
							res.getString("Enter amount....."),
							"CREDIT_CARD_AMOUNT", theAppMgr.CURRENCY_MASK);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param Command
	 * @param obj
	 */
	public void objectEvent(String Command, Object obj) {
		if (Command.equals("PAYMENT")) {
			try {
				if (obj != null) {
					/*
					 * //Vishal Yevale : Added for saving CREDIT_CARD rather
					 * than it's Merchant name : 6 March 2017 if(obj instanceof
					 * CMSCreditCard){
					 * ((Payment)obj).setGUIPaymentName("CREDIT_CARD");
					 * ((Payment)obj).setRegion(false); }
					 */
					Payment pay = (Payment) obj;
					// Sergio
					if (pay.getPaymentCode() == null
							|| pay.getPaymentCode() == ""
								|| pay.getPaymentCode().trim().length() == 0) {
						CMSPaymentCode payCode = getPaymentCode(pay
								.getTransactionPaymentName());
						if (payCode != null) {
							pay.setPaymentCode(payCode.getPaymentCode());
							pay.setGUIPaymentName(payCode.getPaymentDesc());
						}
					} else {
						// Sergio
						pay.setPaymentCode(pay.getPaymentCode());
						// pay.setGUIPaymentName(pay.getTransactionPaymentName());
						// pay.setTransactionPaymentName("CREDIT_CARD");
					}

					// do not allow payment of 0.00
					if (!pay.getAmount().equals(new ArmCurrency(0.0))) {
						// set the amount if change is due
						if (changeDue) {
							// make it a negative for change
							// theAppMgr.showR
							pay.setAmount(pay.getAmount().multiply(-1));
						}
						try {
							theTxn.addPayment((Payment) obj);
							model.addPayment((Payment) obj);
							model.fireTableDataChanged();
							updateLabels();
							// ks: Europe - "Choose change type" replaced with
							// "Select tender."
							// theAppMgr.setSingleEditArea(res.getString("Choose change type."));
							theAppMgr.setSingleEditArea(res
									.getString("Select Tender"));
							LightPoleDisplay.getInstance().paymentDisplay(
									(Payment) obj,
									theTxn.getCompositeTotalAmountDue());
						} catch (BusinessRuleException ex) {
							theAppMgr.showErrorDlg(res.getString(ex
									.getMessage()));
							theAppMgr.setSingleEditArea(res
									.getString("Select Tender"));
						}
					}
				} else {
					updateLabels();
					theAppMgr.setSingleEditArea(res.getString("Select Tender"));
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
			Loyalty loyaltyCardObj = (Loyalty) theAppMgr
			.getStateObject("LOYALTYOBJ");
			theAppMgr.removeStateObject("LOYALTYOBJ");
			Loyalty OldObj = loyaltyCardObj;
			// Refresh the loyalty card from database.
			try {
				OldObj = LoyaltyHelper.getLoyalty(theAppMgr,
						OldObj.getLoyaltyNumber());
			} catch (Exception e) {
				theAppMgr
				.showErrorDlg(res
						.getString("Cannot issue reward card. Loyalty card not found."));
				e.printStackTrace();
				return;
			}
			// For Reward Txn
			CMSStore cmsStore = (CMSStore) theAppMgr.getGlobalObject("STORE");
			CMSEmployee cmsEmployee = (CMSEmployee) theAppMgr
			.getStateObject("OPERATOR");
			RewardTransaction rewardTxn = new RewardTransaction(cmsStore);
			try {
				rewardTxn.setId(CMSTransactionNumberHelper
						.getNextTransactionNumber(theAppMgr,
								(CMSStore) theStore, (CMSRegister) theAppMgr
								.getGlobalObject("REGISTER")));
				rewardTxn.setRegisterId(((CMSRegister) theAppMgr
						.getGlobalObject("REGISTER")).getId());
				rewardTxn.setSubmitDate(new Date());
				rewardTxn.setProcessDate((Date) theAppMgr
						.getGlobalObject("PROCESS_DATE"));
				rewardTxn.setTheOperator(cmsEmployee);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ConfigMgr config = new ConfigMgr("loyalty.cfg");
			String loyaltyRewardRatio = config
			.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
			String loyaltyAmount = config.getString("LOYALTY_REWARD_AMOUNT");
			long points = (new Long(loyaltyRewardRatio)).longValue()
			* (new Long(loyaltyAmount)).longValue();
			RewardCard rewardCard = (RewardCard) obj;
			if (rewardCard != null) {
				rewardCard.setLoyalty(OldObj);
				rewardCard.getLoyalty().setLoyaltyNumber(
						OldObj.getLoyaltyNumber());
				rewardTxn.setRewardCard(rewardCard);
				// loyaltyCardObj.setCurrBalance(loyaltyCardObj.getCurrBalance()-points);
				try {
					rewardTxn.setComment("New reward card Issued");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Points that need to be added
				rewardTxn.setPoints(points);
				try {
					rewardTxn.post();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Need to enter the issued reward Card
				theAppMgr.setSingleEditArea(res.getString("Select Option"));
			}
		}
		// VM: the processObject of DueBillBldr now points to
		// STORE_VALUE_CREDIT_MEMO_PAYMENT for customer requirement
		if (Command.equals("STORE_VALUE_CREDIT_MEMO_PAYMENT")) {
			try {
				if (obj != null) {
					if (obj instanceof DueBill || obj instanceof StoreValueCard) {
						PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr
						.getStateObject("TXN_POS");
						if (txn instanceof CompositePOSTransaction) {
							CompositePOSTransaction theTxnPOS = ((CompositePOSTransaction) txn);
							if (theTxnPOS.getCustomer() == null) {
								theAppMgr.addStateObject("CUST_NEEDED_PAYMENT",
										obj);
								theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
							} else
								objectEvent("PAYMENT", obj);
						}
					}
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
				// ex.printStackTrace();
			}
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param forCredit
	 * @exception Exception
	 */
	private void applyVat(boolean forCredit) throws Exception {
		if (isVatEnabled) {
			if (theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) {
				// if(((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).isPostAndPack())
				VATUtilities.applyVAT(theAppMgr,
						(CMSCompositePOSTransaction) theTxn
						.getPaymentTransaction(), (CMSStore) theStore,
						(CMSStore) theStore, theTxn.getPaymentTransaction()
						.getProcessDate());
				// sonali:price Overide
				CMSCompositePOSTransaction trans = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");

				POSLineItem[] lineItem = trans.getSaleLineItemsArray();
				for (int i = 0; i < lineItem.length; i++) {
					if (lineItem[i].getOverrideAmount() != null) {
						lineItem[i].setManualUnitPrice(lineItem[i]
						                                        .getOverrideAmount());

					}
				}
			}
		}
	}

	/**
	 */
	private void selectOption() {
		theAppMgr.setSingleEditArea(res.getString("Select option."));
		theAppMgr.setEditAreaFocus(); // fixes problem when card is swiped
		// before the system is ready
	}

	/**
	 * Method checks for overage / underage for payments. If balance is zero,
	 * call end of tranaction. If return only, show refund buttons.
	 */
	private void selectPayment() {
		try {
			ArmCurrency amt = theTxn.getCompositeTotalAmountDue().subtract(
					theTxn.getTotalPaymentAmount());
			// MP: Changed the code as the comparison was not working fine in
			// case TaxExempt was added.
			ArmCurrency val = new ArmCurrency("0.00");
			ArmCurrency absVal = amt.absoluteValue();
			String amtStr = amt.stringValue();
			String valStr = val.stringValue();
			if (amtStr.equals(valStr) || amt.equals(val)
					|| absVal.stringValue().equals(valStr)) {
				if (shouldTxnPostWhenBalanceIsZero) {
					Thread post = new Thread(new Runnable() {

						/**
						 * put your documentation comment here
						 */
						public void run() {
							theAppMgr.setWorkInProgress(true);
							theAppMgr.setSingleEditArea("Posting...");
							theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
							PaymentApplet_EUR.this
							.appButtonEvent(new CMSActionEvent(this, 0,
									"POST_TXN", 0));
							theAppMgr.setWorkInProgress(false);
							theAppMgr.setEditAreaFocus();
						}
					});
					post.start();
				} else {
					// theAppMgr.showMenu(MenuConst.POST, theOpr,
					// theAppMgr.PREV_BUTTON);
					// Sergio
					// theAppMgr.showMenu(MenuConst.POST, theOpr);
					// selectOption();
					// PRINT_FISCAL_RECEIPT
					theAppMgr.showMenu(MenuConst.POST_TXN_EU,
							"PRINT_FISCAL_RECEIPT", theOpr);
					PaymentApplet_EUR.this.appButtonEvent(new CMSActionEvent(
							this, 0, "PRINT_FISCAL_RECEIPT", 0));
					// selectOption();
					// PaymentApplet_EUR.this.appButtonEvent(new
					// CMSActionEvent(this, 0, "PRINT_FISCAL_RECEIPTT, 0));
					// objectEvent("PAYMENT", "PRINT_FISCAL");
					// objectEvent("PAYMENT", "POST");
				}
				return;
			}
			if (amt.lessThan(new ArmCurrency(0.0))) {
				// added by Anand to accomodate NOT posting transaction
				// automatically if Paid-Out Txn
				if (theTxn.getPaymentTransaction() instanceof PaidOutTransaction) {
					selectOption();
					updateLabels();
					initButtons(CMSPaymentMgr.CHANGE);
					changeDue = true;
					return;
				}
				lblDue.setText(res.getString("Change Due") + ":");
				// if (!bool) {
				if (theTxn.getCompositeTotalAmountDue().doubleValue() < 0) {
					theAppMgr.setSingleEditArea(res
							.getString("Choose refund type."));
				} else if (!isMaxChangeAllowed()) {
					// initButtons(CMSPaymentMgr.CHANGE);
					theAppMgr.setSingleEditArea(res
							.getString("Choose change type."));
					// Khyati: No auto change if REfund due
					// }
					// else if (theTxn.isRefundPaymentRequired()) {
					// theAppMgr.setSingleEditArea(res.getString("Choose refund type."));
				} else {
					if (!changeDue) {
						theTxn.addPayment(change);
						model.addPayment(change);
						model.fireTableDataChanged();
						ArmCurrency amtDue = theTxn
						.getCompositeTotalAmountDue().subtract(
								theTxn.getTotalPaymentAmount());
						if (amtDue.equals(new ArmCurrency(0.0))) {
							if (shouldTxnPostWhenBalanceIsZero) {
								Thread post = new Thread(new Runnable() {

									/**
									 * put your documentation comment here
									 */
									public void run() {
										theAppMgr.setWorkInProgress(true);
										theAppMgr
										.setSingleEditArea("Posting...");
										theAppMgr.showMenu(
												MenuConst.NO_BUTTONS, theOpr);
										PaymentApplet_EUR.this
										.appButtonEvent(new CMSActionEvent(
												this, 0, "POST_TXN", 0));
										theAppMgr.setWorkInProgress(false);
										theAppMgr.setEditAreaFocus();
									}
								});
								post.start();
							} else {
								// Show the post menu
								updateLabels();
								// Added this for AutoPost
								theAppMgr.showMenu(MenuConst.POST_TXN_EU,
										"PRINT_FISCAL_RECEIPT", theOpr);
								PaymentApplet_EUR.this
								.appButtonEvent(new CMSActionEvent(
										this, 0,
										"PRINT_FISCAL_RECEIPT", 0));
								// selectOption();
								// Commented this for AutoPost
								// theAppMgr.showMenu(MenuConst.POST, theOpr,
								// theAppMgr.PREV_BUTTON);
								return;
							}
						} else {
							theAppMgr.showMenu(MenuConst.POST_TXN_EU, theOpr,
									theAppMgr.PREV_BUTTON);
							selectOption();
							updateLabels();
							changeDue = true;
							return;
						}
					}
				}
				// }
				if (bool) {
					if (theAppMgr.getStateObject("PAYMENT") != null) {
						SwingUtilities.invokeLater(new Runnable() {

							/**
							 * put your documentation comment here
							 */
							public void run() {
								PaymentApplet_EUR.this.appButtonEvent(new CMSActionEvent(
										this, 0, (String) theAppMgr
										.getStateObject("PAYMENT"), 0));
								theAppMgr.removeStateObject("PAYMENT");
							}
						});
					}
				}
				if (theTxn.isRefundPaymentRequired()) {
					// initButtons(PaymentMgr.REFUND);
					initButtons(CMSPaymentMgr.REFUND);
				} else {
					initButtons(CMSPaymentMgr.CHANGE); // only show if credit
					// initButtons(PaymentMgr.CHANGE); // only show if credit
				}
				changeDue = true;
			} else {
				lblDue.setText(res.getString("Amount Due") + ":");
				// initButtons(PaymentMgr.PAYMENT);
				initButtons(CMSPaymentMgr.PAYMENT);
				// MP: In Eur the INITIAL_PAYMENT is null, so when we return
				// from
				// customer-lookup, we need to check the 'PAYMENT' stateObj.
				// If it doesn't equal to null fire the event (In our case it is
				// to
				// call HOUSE_ACCOUNT bldr.
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
								if (bool
										&& !flag
										&& isButtonInList(sAction_global)
										&& theAppMgr.getStateObject("PAYMENT") != null) {
									PaymentApplet_EUR.this.appButtonEvent(new CMSActionEvent(
											this, 0, (String) theAppMgr
											.getStateObject("PAYMENT"),
											0));
									theAppMgr.removeStateObject("PAYMENT");
								}
							}
						});
					}
				});
				if (anInitialPayment != null) {
					try {
						anInitialPayment.isValidAsPayment(theTxn
								.getPaymentTransaction());
						// this in invoke later, because the applet may register
						// after the builder if not
						SwingUtilities.invokeLater(new Runnable() {

							/**
							 */
							public void run() {
								// bool will always be true when control returns
								// from the CustomerListApplet
								// flag will always be true when there is NO
								// customer attached to the txn
								if (!bool) {
									PaymentApplet_EUR.this
									.appButtonEvent(new CMSActionEvent(
											this, 0, INITIAL_PAYMENT, 0));
								} else {
									if (flag) {
										PaymentApplet_EUR.this
										.appButtonEvent(new CMSActionEvent(
												this, 0,
												INITIAL_PAYMENT, 0));
									} else if (isButtonInList(sAction_global)) {
										System.out
										.println("******** In else statement when bool = T & flag = F ");
										// for skip condition in CustListApplet
										// to return to PaymentApplet_EUR
										PaymentApplet_EUR.this
										.appButtonEvent(new CMSActionEvent(
												this,
												0,
												(String) theAppMgr
												.getStateObject("PAYMENT"),
												0));
									} else {
										PaymentApplet_EUR.this
										.appButtonEvent(new CMSActionEvent(
												this, 0,
												INITIAL_PAYMENT, 0));
									}
								}
							}
						});
					} catch (BusinessRuleException bex) {
						theAppMgr.setSingleEditArea(res
								.getString("Choose payment type."));
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
			if (!isFiscalReceiptPrinted)
				theAppMgr
				.showErrorDlg(res
						.getString("Please prepare manual receipt for customer"));
			// update display manager
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				DisplayManager dm = (DisplayManager) theAppMgr
				.getGlobalObject("DISPLAY_MANAGER");
				if (dm != null) {
					dm.endTransaction((CompositePOSTransaction) theTxn
							.getPaymentTransaction());
				}
			}

			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				CMSCompositePOSTransaction transaction = (CMSCompositePOSTransaction) theTxn
				.getPaymentTransaction();
				CMSV12Basket cmsV12Basket = transaction.getCmsV12Basket();
				if (cmsV12Basket != null
						&& transaction.getSaleLineItemsArray() != null) {
					boolean isBasketTransaction = false;
					POSLineItem lineItem[] = transaction
					.getSaleLineItemsArray();
					ArrayList<String> itemId = cmsV12Basket.getItemList();
					for (String id : itemId) {
						for (POSLineItem item : lineItem) {
							if (item.getItem().getId().equals(id)) {
								isBasketTransaction = true;
								break;
							}
						}
						if (isBasketTransaction) {
							break;
						}
					}
					if (!isBasketTransaction) {
						CMSV12BasketHelper.setBasketStatus(theAppMgr,
								cmsV12Basket, CMSV12Basket.open);
						transaction.setCmsV12Basket(null);
						((CMSCompositePOSTransaction) theTxn
								.getPaymentTransaction()).setCmsV12Basket(null);
					}
				}
			}
			// set layaway account balance
			// if (!layawayAccount()) {
			// selectPayment();
			// return false;
			// }
			// get Digital Signature if necessary
			if ((new ConfigMgr("JPOS_peripherals.cfg")
			.getString("MSR_DEVICE_TYPE")).equals("INGENICO_JPOS")) {
				if (SignatureCaptureDlg.isDigialSignatureRequired(theTxn
						.getPaymentTransaction())) {
					SignatureCaptureDlg signatureCaptureDlg = new SignatureCaptureDlg(
							theAppMgr, theTxn.getPaymentTransaction());
					String[] cardInfoMessage = SignatureCaptureDlg
					.getCardInfoMessage(theTxn.getPaymentTransaction());
					ET1000JPOSForm.getInstance().displaySignatureCapture(
							cardInfoMessage);
					signatureCaptureDlg.setVisible(true);
				} else {
					ET1000JPOSForm.getInstance().displayLogo();
				}
			}
			// pop drawer
			try {
				if (shouldDrawPop()) {
					System.out
					.println("PaymentApplet_EUR.postTransaction()->Pop Drawer...");
					// fiscalInterface.openDrawer();
				}
			} catch (Exception ex) {
				// CashDrawer not found!
				System.out.println("Cash Drawer Not Found");
			}
			// set ID and post transaction
			/*
			 * Moved this creation of Txn number to start for ISDValidation
			 * needed this for authorization. String txnNum =
			 * CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr,
			 * (CMSStore)theStore,
			 * (CMSRegister)theAppMgr.getGlobalObject("REGISTER"));
			 * theTxn.getPaymentTransaction().setId(txnNum);
			 */
			if (theTxn.getPaymentTransaction().getId() == null
					|| theTxn.getPaymentTransaction().getId().equals("")) {
				if (txnNum == null || txnNum.equals("")) {
					createTxnNumber();
				} else {
					theTxn.getPaymentTransaction().setId(txnNum);
				}
			}
			theTxn.getPaymentTransaction().setSubmitDate(new java.util.Date());
			// set presale txn id
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				if (((CMSCompositePOSTransaction) theTxn
						.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn
								.getPaymentTransaction())
								.getPresaleLineItemsArray().length > 0) {
					((CMSCompositePOSTransaction) theTxn
							.getPaymentTransaction()).getPresaleTransaction()
							.doSetPresaleId(
									theTxn.getPaymentTransaction().getId());
				}
			}
			// set consignment txn id
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				if (((CMSCompositePOSTransaction) theTxn
						.getPaymentTransaction())
						.getConsignmentLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn
								.getPaymentTransaction())
								.getConsignmentLineItemsArray().length > 0) {
					((CMSCompositePOSTransaction) theTxn
							.getPaymentTransaction())
							.getConsignmentTransaction().doSetConsignmentId(
									theTxn.getPaymentTransaction().getId());
				}
			}
			// Set Reservation Txn ID
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				if (((CMSCompositePOSTransaction) theTxn
						.getPaymentTransaction())
						.getReservationLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn
								.getPaymentTransaction())
								.getReservationLineItemsArray().length > 0) {
					((CMSCompositePOSTransaction) theTxn
							.getPaymentTransaction())
							.getReservationTransaction().doSetReservationId(
									theTxn.getPaymentTransaction().getId());
				}
			}
			/*
			 * 5095 - Post transaction before printing it. The issue is, is that
			 * if the post causes an exception, a receipt still exists. This
			 * post mechanism is merely just serializing this txn to disk, so
			 * its not that long.
			 * 
			 * @see com.chelseasystems.cr.txnposter.TxnPosterSyncRMIServices
			 */
			try {

				// /**
				// * Added by Satin to create digital signature.
				// * Code change for digital signature starts here.
				// */
				// ConfigMgr cfgMgr1 = new ConfigMgr("payment.cfg");
				// String activateDigitalSignature =
				// cfgMgr1.getString("ACTIVATE_DIGITAL_SIGNATURE");
				// if ((activateDigitalSignature.length())==0){
				// theAppMgr.showErrorDlg(res.getString("Specify the parameter ACTIVATE_DIGITAL_SIGNATURE in payment.cfg"));
				//
				// }
				// else {
				// PaymentTransaction txn = (PaymentTransaction)
				// CMSApplet.theAppMgr.getStateObject("TXN_POS");
				// String txnId = ((CMSCompositePOSTransaction)txn).getId();
				// if
				// (activateDigitalSignature.trim().equalsIgnoreCase("true")){
				// // To get any previous digital signature if any from TR_RTL
				// table in order to create the message
				// String previousDigitalSignature =
				// (String)CMSTransactionPOSHelper.selectDigitalSignature(theAppMgr,
				// txnId);
				// String digitalSignature =
				// (DigitalSignatureUtil.createMessageAndDigitalSignature(txn,
				// previousDigitalSignature));
				// ((CMSCompositePOSTransaction)txn).setDigitalSignature(digitalSignature);
				// }
				// }
				// /**
				// * Code change for digital signature ends here.
				// */

				// Poonam:Added for saving CREDIT_CARD to DB instead of it's
				// Merchant name.
				Payment[] payment = theTxn.getPaymentsArray();
				for (int i = 0; i < payment.length; i++) {
					if (payment[i] != null) {
						Payment pays = (Payment) payment[i].clone();
						if (payment[i] instanceof CMSCreditCard) {
							payment[i].setGuiPaymentType(payment[i]
							                                     .getGUIPaymentName());
							payment[i].setGUIPaymentName("CREDIT_CARD");
							payment[i].setRegion(false);
							// This is to show merchant name on payment screen
							if (model != null) {
								int rows = model.getRowCount();
								for (int count = 0; count < rows; count++) {
									Vector vTemp = model.getCurrentPage();
									Payment pay = (Payment) vTemp
									.elementAt(count);
									// start Bug #:-28022-saptarshi - putting
									// extra null check on payment code-
									if (pay != null && pays != null) {
										if ((pay.getPaymentCode() != null)
												&& (pays.getPaymentCode() != null)
												&& (pay.getPaymentCode()
														.equalsIgnoreCase(pays
																.getPaymentCode()))) {
											// end Bug #:-28022-saptarshi -
											// putting extra null check on
											// payment code-
											model.removeRow(count);
											model.addPayment(pays);
											model.fireTableDataChanged();
										}
									}// //end bug no:-28022-saptarshi - putting
									// extra null check on payment code-
								}
							}
						}
					}
				}
				// End Poonam

				CMSTxnPosterHelper.post(theAppMgr,
						theTxn.getPaymentTransaction());
			} catch (java.io.IOException ioe) {
				/*
				 * 5293 - Any failure to persist this transaction is a
				 * <b>huge</b> issue. We have decided to provide a receipt to
				 * record the used transaction number and prevent the Eu from
				 * continuing until help-desk records/fixes the failure and
				 * restarts the application with Mission Control.
				 */
				theTxn.printPostFailedReceipt(theAppMgr);
				while (true) {
					theAppMgr
					.showErrorDlg(res
							.getString("The transaction could not "
									+ "be recorded on this terminal.  Posting can not continue.  "
									+ "This application will not continue until support has "
									+ "resolved the issue.  Please call support and notify the manager "
									+ "of this transaction number."
									+ "  Txn Num: " + txnNum));
				}
			} catch (Exception e) {
				theAppMgr
				.showErrorDlg(res
						.getString("Transaction could not be posted.  Please call support."));
				return (false);
			}
			// print receipt
			System.out.println("about to print receipt...");
			// Sergio
			// Poonam:Added to show actual card type on receipt
			Payment pay[] = theTxn.getPaymentsArray();
			for (int i = 0; i < pay.length; i++) {
				String type = pay[i].getGuiPaymentType();
				if (type != null && pay[i].getGUIPaymentName() != null
						&& (pay[i].getGUIPaymentName().contains("CREDIT"))) {
					pay[i].setGUIPaymentName(type);
				}
			}
			// Poonam:Ends here
			printReceipt();
			System.out
			.println("************************************************");
			System.out.println("******    POSTED TXN: " + txnNum
					+ "      ******");
			System.out
			.println("************************************************");
			txnNum = null;
			// Remove Stateobjects
			theAppMgr.removeStateObject("ARM_DISCOUNT_SEQ");
			theAppMgr.removeStateObject("ARM_DISCOUNT_TYPE");
			theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
			theAppMgr.removeStateObject("ARM_FROM_SHIPPING_ADDRESS");
			theAppMgr.removeStateObject("ARM_SHIPPING_FEE");
			theAppMgr.removeStateObject("ARM_IS_CUSTOMER_ADDED");
			theAppMgr.removeStateObject("V12BASKET_LOOKUP");

			if (theAppMgr.getStateObject("ARM_TRANSACTION_AMOUNT_OVER_LIMIT") != null
					&& theAppMgr.getStateObject(
					"ARM_TRANSACTION_AMOUNT_OVER_LIMIT").equals("TRUE")) {
				theAppMgr
				.showErrorDlg(res
						.getString("Please remember to print Fiscal document for this transaction"));
				theAppMgr
				.removeStateObject("ARM_TRANSACTION_AMOUNT_OVER_LIMIT");
			}

			if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
				String txnClassName = theTxn.getPaymentTransaction().getClass()
				.getName();
				String shortClassName = txnClassName.substring(
						txnClassName.lastIndexOf(".") + 1,
						txnClassName.length());
				String fileName = ReceiptConfigInfo.getInstance()
				.getPathForRDO() + shortClassName + ".rdo";
				try {
					ObjectStore objectStore = new ObjectStore(fileName);
					objectStore.write(theTxn);
				} catch (Exception e) {
					System.out
					.println("exception on writing object to blueprint folder: "
							+ e);
				}
			}
			// if (theTxn.isFrankingRequired()) {
			// endorseCheck();
			// }
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
		theTxn.printReceipt(theAppMgr); // whoooo... that was easy.
	}

	/**
	 */
	private void loadGiftPanel() {
		theAppMgr.showMenu(MenuConst.GIFT_RECEIPTS, theOpr);
		pnlGiftReceipt = new GiftReceiptPanel(theAppMgr, theTxn);
		pnlGiftReceipt.getTable().addMouseListener(
				new java.awt.event.MouseAdapter() {

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
		String COPY_NUMBER = "";

		ARMFSBridge fsBridge = new ARMFSBridge().build();
		if (fsBridge.isCountryAllowed) {

			ARMFSBOReceiptCopyObject receiptCopyObject = new ARMFSBOReceiptCopyObject(
					(CommonTransaction) theTxnPOS);
			if (fsBridge.postObject(receiptCopyObject)) {

				String response = receiptCopyObject.processResponse();
				COPY_NUMBER = receiptCopyObject.COPY_NUMBER;

				try {
					theTxn.getPaymentTransaction().setHandWrittenTicketNumber(
							COPY_NUMBER + "-"
							+ receiptCopyObject.COPY_DIGITAL_KEY);
				} catch (BusinessRuleException e) {
					AppManager.getCurrent().showErrorDlg(e.getMessage());
					e.printStackTrace();
				}

				if (response.contains("ERROR") || response.contains("UNABLE")) {
					theAppMgr
					.showErrorDlg("[FS BRIDGE] "
							+ response
							+ "\n Please call the Help Desk Immediately. Press OK to continue");

				}

			} else {

				theAppMgr
				.showErrorDlg("[FS BRIDGE] : Unable to post RECEIPT_COPY_TRANSACTION. Press OK to continue\n"
						+ "Please call the Help Desk Immediately");

			}

		}

		/*
		 * -- END
		 */

		int result = fiscalInterface.printFiscalReceipt(theTxn);
		// Alex

		// if (result == 1) {
		// theAppMgr.showErrorDlg(res.getString("Receipt re-printed successfuly."));
		/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the Receipt Copy
		 * Transaction for Fiscal Solutions Service START --
		 */

		if (result == 0) {
		} else {
			theAppMgr.showErrorDlg(res
					.getString("Error re-printing Fiscal receipt."));
		}
	}

	/**
	 */
	private void enterStubQty() {
		theAppMgr.setSingleEditArea(
				res.getString("Enter number of alteration stubs to print."),
				"PRINT_STUB", new Integer(1), theAppMgr.INTEGER_MASK);
	}

	/**
	 * Handles SubMenu
	 * 
	 * @param sHeader
	 *            String
	 * @param anEvent
	 *            CMSActionEvent
	 */
	public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
		if (!isCreditAuthComplete) {
			theAppMgr.showErrorDlg(res.getString("Credit Auth in progress!"));
			anEvent.consume();
			return;
		}
		theAppMgr.unRegisterSingleEditArea();
		if (postingInProgress) {
			// Do not allow any button actions while posting is in progress
			anEvent.consume();
			return;
		}
		String sAction = anEvent.getActionCommand();
		String sTmp = "";
		if (sAction.equals("PREV")) {
			if (sHeader.equals("PRINT_FISCAL")) {
				anEvent.consume();
				theAppMgr.showMenu(MenuConst.POST_TXN_EU, theOpr);
			}
			if (sHeader.equals("MODIFY_FISCAL")) {
				anEvent.consume();
				theAppMgr.showMenu(MenuConst.POST_TXN_EU, theOpr);
			}
		} else if (sAction.equals("MODIFY_FISCAL_NUM")) {
			anEvent.consume();
			theAppMgr
			.showMenu(MenuConst.MODIFY_FISCAL, "MODIFY_FISCAL", theOpr);
		} else if (sAction.equals("MODIFY_DDT_NO")) {
			anEvent.consume();
			selectModifyDDT();
		} else if (sAction.equals("MODIFY_TAX_NO")) {
			anEvent.consume();
			selectModifyTaxNumber();
		} else if (sAction.equals("MODIFY_CREDIT_NOTE")) {
			anEvent.consume();
			selectModifyCreditNote();
		} else if (sAction.equals("NEW_TRANSACTION")) {
			try {
				CMSCompositePOSTransaction txn = CMSTransactionPOSHelper
				.allocate(theAppMgr);
				theAppMgr
				.showErrorDlg(res
						.getString("Mandatory add a Fiscal Document on this transaction"));
				CMSEmployee employee = (CMSEmployee) theAppMgr
				.getStateObject("OPERATOR");
				theAppMgr.clearStateObjects();
				LightPoleDisplay.getInstance().startDefaultDisplay();
				theAppMgr.addStateObject("TXN_POS", txn);
				theAppMgr.addStateObject("OPERATOR", employee);
				theAppMgr.addStateObject("NEW_TXN_AFTER_TENDER",
				"NEW_TXN_AFTER_TENDER");

			} catch (Exception ex) {
				ex.printStackTrace();
				anEvent.consume();
				theAppMgr.showErrorDlg(ex.getMessage());
			}
			return;
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void selectModifyDDT() {
		String sTmp = "";
		sTmp = res.getString("Enter new DDT No.") + ";";
		sTmp += res.getString("Current DDT No") + ":";
		sTmp += fiscalDocUtil.getAvailableDDTNumber();
		theAppMgr.setSingleEditArea(sTmp, "MODIFY_DDT_NO");
	}

	/**
	 * put your documentation comment here
	 */
	private void selectModifyTaxNumber() {
		String sTmp = "";
		sTmp = res.getString("Enter new Tax No.") + ";";
		sTmp += res.getString("Current Tax No") + ":";
		sTmp += fiscalDocUtil.getAvailableVATNumber();
		theAppMgr.setSingleEditArea(sTmp, "MODIFY_TAX_NO");
	}

	/**
	 * put your documentation comment here
	 */
	private void selectModifyCreditNote() {
		String sTmp = "";
		sTmp = res.getString("Enter new CreditNote No.") + ";";
		sTmp += res.getString("Current CreditNote No") + ":";
		sTmp += fiscalDocUtil.getAvailableCreditNoteNumber();
		theAppMgr.setSingleEditArea(sTmp, "MODIFY_CREDIT_NOTE");
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sInput
	 */
	private void modifyDDTNumber(String sInput) {
		try {
			if (!fiscalDocUtil.setNextDDTNumber(sInput)) {
				theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse
						.getErrorMessage(fiscalDocUtil.getResponseCode())));
				selectModifyDDT();
				return;
			}
			theAppMgr.showErrorDlg(res.getString("DDT number modified to ")
					+ sInput);
		} catch (Exception e) {
			theAppMgr
			.showErrorDlg(res
					.getString("Can't modify DDT number, contact technical support"));
		}
		selectOption();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sInput
	 */
	private void modifyTAXNumber(String sInput) {
		try {
			if (!fiscalDocUtil.setNextVATNumber(sInput)) {
				theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse
						.getErrorMessage(fiscalDocUtil.getResponseCode())));
				selectModifyTaxNumber();
				return;
			}
			theAppMgr.showErrorDlg(res.getString("Tax number modified to ")
					+ sInput);
		} catch (Exception e) {
			theAppMgr
			.showErrorDlg(res
					.getString("Can't modify Tax number, contact technical support"));
		}
		selectOption();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sInput
	 */
	private void modifyCreditNote(String sInput) {
		try {
			if (!fiscalDocUtil.setNextCreditNoteNumber(sInput)) {
				theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse
						.getErrorMessage(fiscalDocUtil.getResponseCode())));
				selectModifyCreditNote();
				return;
			}
			theAppMgr.showErrorDlg(res
					.getString("CreditNote number modified to ") + sInput);
		} catch (Exception e) {
			theAppMgr
			.showErrorDlg(res
					.getString("Can't modify CreditNote number, contact technical support"));
		}
		selectOption();
	}

	/**
	 * 
	 * @param anEvent
	 *            CMSActionEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {

		if (theAppMgr.getStateObject("TXN_POS") instanceof CMSCompositePOSTransaction) {
			// sonali:Changes for persisting discount code and discount percent
			// ;setting Discounts in reduction array
			CMSCompositePOSTransaction txnObj = (CMSCompositePOSTransaction) theAppMgr
			.getStateObject("TXN_POS");
			POSLineItem[] returnItems1 = txnObj.getReturnLineItemsArray();
			if (returnItems1 != null) {
				/*
				 * for(int k=0;k<returnItems1.length;k++) {
				 * 
				 * SaleLineItem
				 * saleItem=((CMSReturnLineItem)returnItems1[k]).getSaleLineItem
				 * (); if(saleItem!=null) { Discount[]
				 * discount=saleItem.getDiscountsArray(); if(discount!=null &&
				 * discount.length>0) { Reduction[]
				 * baseReductions=((POSLineItemDetail
				 * )returnItems1[k].getLineItemDetailsArray
				 * ()[0]).getReductionsArray();
				 * ((POSLineItemDetail)returnItems1[
				 * k].getLineItemDetailsArray()[0]).doRemoveAllReductions();
				 * 
				 * for(int l=0;l<discount.length;l++) { CMSReduction
				 * reduction=new
				 * CMSReduction(baseReductions[l].getAmount(),baseReductions
				 * [l].getReason()); reduction.doSetDiscount(discount[l]) ;
				 * ((POSLineItemDetail
				 * )returnItems1[k].getLineItemDetailsArray()[
				 * 0]).doAddReduction(reduction);
				 * 
				 * } } } } //end sonali
				 */
				// Vishal : done the changes for persisting same discount rows
				// in db for return transaction as Sale transaction 08-July-2016
				for (int k = 0; k < returnItems1.length; k++) {

					SaleLineItem saleItem = ((CMSReturnLineItem) returnItems1[k])
					.getSaleLineItem();
					if (saleItem != null) {
						Discount[] discount = saleItem.getDiscountsArray();
						if (discount != null && discount.length > 0) {
							Reduction[] baseReductions = ((POSLineItemDetail) returnItems1[k]
							                                                               .getLineItemDetailsArray()[0])
							                                                               .getReductionsArray();
							((POSLineItemDetail) returnItems1[k]
							                                  .getLineItemDetailsArray()[0])
							                                  .doRemoveAllReductions();
							int temp = 0;
							int arr = (discount.length >= baseReductions.length) ? discount.length
									: baseReductions.length;
							for (int l = 0, j = 0; l < arr; l++, j++) {
								if (temp == 0
										&& baseReductions.length > discount.length) {
									CMSReduction reduction = new CMSReduction(
											baseReductions[l].getAmount(),
											baseReductions[l].getReason());
									((POSLineItemDetail) returnItems1[k]
									                                  .getLineItemDetailsArray()[0])
									                                  .doAddReduction(reduction);
									temp++;
									j = -1;
								} else {
									/*
									 * Bug no 28132:-start-checking for
									 * automatic discount, automatic discount
									 * should not set in reduction
									 */
									if (baseReductions == null
											|| baseReductions.length <= 0
											|| l >= baseReductions.length) {
										continue;
									}
									/*
									 * Bug no 28132:-end-checking for automatic
									 * discount, automatic discount should not
									 * set in reduction
									 */

									CMSReduction reduction = new CMSReduction(
											baseReductions[l].getAmount(),
											baseReductions[l].getReason());
									reduction.doSetDiscount(discount[j]);
									((POSLineItemDetail) returnItems1[k]
									                                  .getLineItemDetailsArray()[0])
									                                  .doAddReduction(reduction);
								}

							}
						}
					}
				}
			}
		} // END of patch adde by Yves Agbessi (29-Nov-2017)
		// ends - Vishal 08-July-2016
		if (!isCreditAuthComplete) {
			theAppMgr.showErrorDlg(res.getString("Credit Auth in progress!"));
			anEvent.consume();
			return;
		}
		String sAction = anEvent.getActionCommand();
		System.out.println("Action: " + sAction);
		sAction_global = sAction;
		try {
			if (sAction.equals("PRINT_FISCAL")) {
				CMSCompositePOSTransaction theTxnPOS = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
				if (theTxnPOS != null && theTxnPOS.isTaxExempt()) {
					theAppMgr.showErrorDlg(res
							.getString("VAT Invoice is Compulsory"));
					theAppMgr.fireButtonEvent("VAT_INVOICE");
				} else {
					theAppMgr.showMenu(MenuConst.PRINT_FISCAL_DOCUMENT,
							"PRINT_FISCAL", theOpr);
					return;
				}
			}
			if (sAction.equals("NEW_TRANSACTION")) {
				try {
					// PCR 1864 Changes
					validateFiscalDocumentPresentInTransaction();
					CMSCompositePOSTransaction txn = CMSTransactionPOSHelper
					.allocate(theAppMgr);
					CMSEmployee employee = (CMSEmployee) theAppMgr
					.getStateObject("OPERATOR");
					theAppMgr.clearStateObjects();
					LightPoleDisplay.getInstance().startDefaultDisplay();
					theAppMgr.addStateObject("TXN_POS", txn);

					theAppMgr.addStateObject("OPERATOR", employee);
					// ___Tim: check this state object from
					// IdentifyConsultantApplet
					// so that employee cannot return to payment applet after
					// posting Txn
					theAppMgr.addStateObject("NEW_TXN_AFTER_TENDER",
					"NEW_TXN_AFTER_TENDER");
				} catch (Exception ex) {
					ex.printStackTrace();
					anEvent.consume();
					theAppMgr.showErrorDlg(ex.getMessage());
				}
				return;
			}
			if (sAction.equals("DEL_PAY")) {
				deletePayment();
			} else if (sAction.equals("PRINT_RECEIPT")) {
				reprintReceipt();
			} else if (sAction.equals("PRINT_CUSTOMER_RECEIPT")) {
				// for PCR 1864
				validateFiscalDocumentPresentInTransaction();
				fiscalInterface.printCustomerReceipt(theTxn);
				return;
			} else if (sAction.equals("PRINT_GIFT")) {
				// for PCR 1864
				validateFiscalDocumentPresentInTransaction();
				// Temporary fix, a rule has to be created and checked on the
				// isShowable method in CMSMenu/Option
				if (!(theTxn instanceof CMSCompositePOSTransactionAppModel)) {
					theAppMgr
					.showErrorDlg(res
							.getString("A gift receipt is not allowed for this transaction."));
					return;
				}
				if (theTxn instanceof CMSLayawayPaymentTransactionAppModel) {
					try {
						if (!((CMSLayawayPaymentTransactionAppModel) theTxn)
								.getLayaway().getCurrentAmountDue()
								.equals(new ArmCurrency(0.00))) {
							theAppMgr
							.showErrorDlg(res
									.getString("A gift receipt is not allowed on outstanding layaways."));
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
					theAppMgr
					.showErrorDlg(res
							.getString("There are no items on this transaction that are eligible to print on a gift receipt."));
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
				if (!(theTxn instanceof CMSCompositePOSTransactionAppModel)
						|| !isVATIItems) {
					theAppMgr
					.showErrorDlg(res
							.getString("A VAT Invoice is not allowed for this transaction."));
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
				if (!(theTxn instanceof CMSCompositePOSTransactionAppModel)
						|| !isREItems) {
					theAppMgr
					.showErrorDlg(res
							.getString("A Retail Export Receipt is not allowed for this transaction."));
					return;
				}
				printRetailExport();
			} else if (sAction.equals("ENDORSE_CHECK")) {
				System.out.println("*************** Check Button Pressed ");
				// if (theTxn.isFrankingRequired()) {
				// endorseCheck();
				// } else {
				theAppMgr.showErrorDlg(res
						.getString("There are no documents to endorse."));
				// }
			} else if (sAction.equals("POST_TXN")) {
				System.out.println("******** In post txn");
				// Check to see if any authorizable payment failed.
				// If so display message and retry.
				// DONT AUTHORIZE YET UNTIL REGISTER IS INTO GLOBAL REPOSITORY
				/*
				 * String reply = "04"; // If Approved, reply = "01", do
				 * nothing. if(reply =="01"){
				 * theAppMgr.showErrorDlg(res.getString
				 * ("Proceed to next Step. No change Required")); } // Referral
				 * else if(reply =="03") { theAppMgr.showErrorDlg(res.getString(
				 * "Call the authorizer at <1-000-000-0000>. Your Merchant number is <123456>, the Card number is 0000111122223333 and the amount is 99.99"
				 * )); } // Transaction TimeOut else if(reply =="04") {
				 * theAppMgr.showErrorDlg(res.getString(
				 * "Call the authorizer at <1-000-000-0000>. Your Merchant number is <123456>, the Card number is 0000111122223333 and the amount is 99.99"
				 * )); } //System Failure else if(reply =="05") {
				 * theAppMgr.showErrorDlg(res.getString(
				 * "Call the authorizer at <1-000-000-0000>. Your Merchant number is <123456>, the Card number is 0000111122223333 and the amount is 99.99"
				 * )); }
				 */
				// ks: Commented authorize() for Europe
				// if (!authorize()) {
				//
				// String check = null;
				// Register register = (Register)
				// theAppMgr.getGlobalObject("REGISTER");
				// theAppMgr.showErrorDlg(res.getString(
				// "Authorization failed on one or more of the payments."));
				//
				// Payment[] payments = theTxn.getPaymentsArray();
				// int len = payments.length;
				// /**
				// * Code Added for priniting the Error Messages
				// */
				// int len_result = result.length();
				// result = result.substring(2, len_result);
				//
				// int startIndex = 0;
				// for (int i = 0; i < (len_result - 2) / 2; i++) {
				//
				// check = result.substring(startIndex, startIndex + 2);
				// startIndex = startIndex + 2;
				// if (check.equals("03")) {
				//
				// theAppMgr.showErrorDlg(CMSPaymentMgr.
				// getCallCenterDesKey(payments[i].
				// getTransactionPaymentName()));
				//
				// if (check.equals("04")) {
				//
				// theAppMgr.showErrorDlg(CMSPaymentMgr.
				// getCallCenterDesKey(payments[i].
				// getTransactionPaymentName()));
				// }
				// }
				// }
				/**
				 * Code Ended
				 */
				// tblPayment.repaint();
				// theAppMgr.showMenu(MenuConst.POST_AUTH, theOpr,
				// theAppMgr.PREV_BUTTON);
				// selectLastRow();
				// selectOption();
				// return;
				// }
				// these should be taken away
				if ((new ConfigMgr("JPOS_peripherals.cfg")
				.getString("MSR_DEVICE_TYPE")).equals("INGENICO_JPOS")) {
					Payment[] payments = theTxn.getPaymentTransaction()
					.getPaymentsArray();
					boolean authDisplayOnET1000 = false;
					for (int i = 0; i < payments.length; i++) {
						if (payments[i] instanceof CreditCard) {
							try {
								Thread.sleep(1000L);
							} catch (InterruptedException iexp) {
							}
							String digits = "" + System.currentTimeMillis();
							String fourRandomDigits = (digits).substring(digits
									.length() - 4);
							((CreditCard) payments[i])
							.setRespAuthorizationCode(fourRandomDigits);
							if (!authDisplayOnET1000) {
								authDisplayOnET1000 = true;
								ET1000JPOSForm.getInstance()
								.displayAuthorizing();
							}
							tblPayment.repaint();
						}
					}
				}
				tblPayment.repaint();
				try {
					postingInProgress = true;
					if (postTransaction()) {
						isPostComplete = true;
						theAppMgr.addStateObject("NEW_TXN_AFTER_TENDER",
						"NEW_TXN_AFTER_TENDER");
						initReceiptButtons();
					} else {
						theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
					}
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(ex.getMessage());
				} finally {
					postingInProgress = false;
				}
				PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr
				.getStateObject("TXN_POS");
				if (txn instanceof CMSCompositePOSTransaction) {
					CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
					Loyalty OldObj = theTxnPOS.getLoyaltyCard();
					if (OldObj != null) {
						theAppMgr.addStateObject("LOYALTYOBJ", OldObj);
						if (OldObj.getCurrBalance()
								+ theTxnPOS.getLoyaltyPoints() > 1000) {
							theAppMgr
							.showErrorDlg("Issue Reward Discount Card. Customer has earned a Reward Discount");
						}
					}
				}
				selectOption();
			} else if (sAction.equals("PREV")) {
				// ___Tim: To prevent user from hitting 'ESC' during or after
				// posting Txn
				if (theAppMgr.getStateObject("NEW_TXN_AFTER_TENDER") != null) {
					theAppMgr.showErrorDlg("Cannot go back!!");
					anEvent.consume();
					return;
				}

				if (isPostComplete || postingInProgress) {
					anEvent.consume();
					return;
				}
				// remove all payments
				prevPressed = true;
				try {
					theTxn.removeAllPayments();
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(ex.getMessage());
				}
			} else if (sAction.equals("CANCEL")) {
				// Cancel suspending...
				selectPayment();
			} else if (sAction.equals("CANCEL_TXN")) {
				if (!theAppMgr
						.showOptionDlg(
								res.getString("Cancel Transaction"),
								res.getString("Are you sure you want to cancel this transaction?"))) {
					anEvent.consume();
					return;
				}
				cancelTxnPressed = true;
				theAppMgr.setHomeEnabled(true);
				// theAppMgr.goHome();
			} else if (sAction.equals("OK")) {
				// for PCR 1864
				validateFiscalDocumentPresentInTransaction();
				// add flag here
				isPostComplete = true;
				theAppMgr.addStateObject("NEW_TXN_AFTER_TENDER",
				"NEW_TXN_AFTER_TENDER");
				// MP: For NFS.
				// Sergio
				// postTransaction();
				theAppMgr.removeStateObject("CUST_NFS");
			} else if (sAction.equals("PRINT_ALL_GIFT")) {
				pnlGiftReceipt.getGiftReceiptModel().setAllItemSelection(
						new Boolean(true));
				printGiftReceipt();
			} else if (sAction.equals("ISSUE_REWARD")) {
				// If a LoyaltyCard is chosen
				PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr
				.getStateObject("TXN_POS");
				CMSCompositePOSTransaction theTxn = ((CMSCompositePOSTransaction) txn);
				theAppMgr.addStateObject("LOYALTYOBJ", theTxn.getLoyaltyCard());
				Loyalty OldObj = theTxn.getLoyaltyCard();
				theAppMgr.unRegisterSingleEditArea();
				ConfigMgr config = new ConfigMgr("loyalty.cfg");
				String Builder = config.getString("ISSUE_REWARD_CARD_BUILDER");
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
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in
				// process
				// String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
				// String Builder =
				// CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
				// System.out.println("the builder is: " + Builder);
				// theAppMgr.buildObject("PAYMENT", Builder, "");
				ArmCurrency amt = theTxn.getCompositeTotalAmountDue().subtract(
						theTxn.getTotalPaymentAmount());
				theAppMgr.setSingleEditArea(res.getString("Enter amount....."),
						"CREDIT_CARD_AMOUNT", amt.absoluteValue(),
						theAppMgr.CURRENCY_MASK);
				return;
			} else if (sAction.equals("MAN_OVERRIDE")) {
				theAppMgr.setSingleEditArea(
						res.getString("Enter authorization number."), "MANUAL",
						theAppMgr.REQUIRED_MASK);
				theAppMgr.setEditAreaFocus();
			} else if (sAction.equals("Suspend")) {
				try {
					theTxn.getPaymentTransaction().removeAllPayments();
					theTxn.testIsSuspendable();
					theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
					theAppMgr
					.setSingleEditArea(
							res.getString("Enter comments."),
					"SUSPEND_COMMENT");
				} catch (BusinessRuleException bre) {
					theAppMgr.showErrorDlg(bre.getMessage());
					selectOption();
				}
			} else if (sAction.equals("Retry")) {
				try {
					System.out.println("inside Retry");
					// theTxn.getPaymentTransaction().removeAllPayments();
					// theTxn.testIsSuspendable();
					// theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
					// theAppMgr.setSingleEditArea(res.getString("Enter comments."),
					// "SUSPEND_COMMENT");
				} catch (Exception bre) {
					theAppMgr.showErrorDlg(bre.getMessage());
					selectOption();
				}
			} else if (sAction.equals("PRINT_FISCAL_RECEIPT")) {
				try {
					createTxnNumber(); // creates a txn number if not already
					// created.
					System.out
					.println("****----This is to test theTxn Object. Store id is: "
							+ theTxn.getPaymentTransaction().getStore()
							+ " ----****");
					System.out.println("****----This is to test theTxn Object"
							+ theTxn.getPaymentTransaction().getId()
							+ " ----****");
					/**
					 * Added by Satin to create digital signature. Code change
					 * for digital signature starts here.
					 */
					ConfigMgr cfgMgr1 = new ConfigMgr("payment.cfg");
					String activateDigitalSignature = cfgMgr1
					.getString("ACTIVATE_DIGITAL_SIGNATURE");
					String austrianEnv = cfgMgr1
					.getString("AUSTRIAN_ENVOIREMENT");
					// Vivek Mishra : Merged updated code from source provided
					// by Sergio 19-MAY-16
					String shopId = cfgMgr1.getString("SHOP");
					String cash_register = cfgMgr1.getString("CASH_REGISTER");
					String myAesKey = cfgMgr1.getString("AES");
					String myCertificate = cfgMgr1.getString("KEY_CERTIFICATE");
					// SE esiste il digital signature
					if ((activateDigitalSignature.length()) != 0) {
						if (activateDigitalSignature.toUpperCase().compareTo(
						"FALSE") == 0) {
							// theAppMgr.showErrorDlg(res.getString("Specify the parameter ACTIVATE_DIGITAL_SIGNATURE in payment.cfg"));
						} else {
							if (austrianEnv.toUpperCase().compareTo("FALSE") == 0) {
								String txnId = "0";
								String transtype;
								String txnCompositeID = "0";
								int year = Calendar.getInstance().get(
										Calendar.YEAR);

								PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr
								.getStateObject("TXN_POS");
								CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
								transtype = txn.getTransactionType();

								if (txn instanceof CMSMiscCollectionCredit) {
									txnId = ((CMSMiscCollectionCredit) txn)
									.getId();
								}

								if (txn instanceof CMSMiscCollection) {
									txnId = ((CMSMiscCollection) txn).getId();
								}

								if (txn instanceof CMSCompositePOSTransaction) {
									txnId = ((CMSCompositePOSTransaction) txn)
									.getId();
								}

								if (txn instanceof CollectionTransaction) {
									txnId = ((CollectionTransaction) txn)
									.getId();
								}

								String recnum = fiscalInterface
								.getNextFiscalReceiptNo();

								if ((transtype.equals("SALE"))
										|| (transtype.equals("SALE/RETN"))) {

									txnCompositeID = "FT "
										+ Integer.toString(year) + shopId
										+ cash_register + "/" + recnum;

								}
								if (transtype.equals("RETN")) {

									txnCompositeID = "NC "
										+ Integer.toString(year) + shopId
										+ cash_register + "/"
										+ fiscalReceiptNumber;
									asisData.setTxnNo(theTxnPOS.getApprover());
									theTxnPOS.setASISTxnData(asisData);
								}
								if (transtype.equals("RSVO")) {

									txnCompositeID = "FT "
										+ Integer.toString(year) + shopId
										+ cash_register + "/" + recnum;
								}

								String registerID = txn.getRegisterId();

								// Riccardino modifica : messo nel nuovo metodo
								// createNewMessageAndDigitalSignature anche la
								// transazione composta (txnCompositeID)
								String digitalReturnSignature = null;
								String digitalSaleSignature = null;
								String previousSaleDigitalSignature = null;
								String previousReturnDigitalSignature = null;
								String digitalSignature = null;
								String previousDigitalSignature = null;

								// DIGITAL SIGNATURE PORTOGALLO
								if ((activateDigitalSignature.trim()
										.equalsIgnoreCase("true"))
										&& (austrianEnv.trim()
												.equalsIgnoreCase("false"))) {

									// To get any previous digital signature if
									// any from TR_RTL table in order to create
									// the message
									// String previousDigitalSignature =
									// (String)CMSTransactionPOSHelper.selectDigitalSignature(theAppMgr,
									// txnId);
									if (txn.getTransactionType()
											.equalsIgnoreCase("SALE")) {
										previousSaleDigitalSignature = (String) theAppMgr
										.getGlobalObject("SALE_DIGITAL_SIGNATURE");

										if (previousSaleDigitalSignature
												.equals("0")) {
											digitalSaleSignature = (DigitalSignatureUtil
													.createNewMessageAndDigitalSignature(
															txn, null,
															txnCompositeID));
											((CMSCompositePOSTransaction) txn)
											.setDigitalSignature(digitalSaleSignature);
											theAppMgr.addGlobalObject(
													"SALE_DIGITAL_SIGNATURE",
													digitalSaleSignature, true);
											System.out
											.println("digitalSignature IN SALE:"
													+ digitalSignature);
										} else
											digitalSaleSignature = (DigitalSignatureUtil
													.createNewMessageAndDigitalSignature(
															txn,
															previousSaleDigitalSignature,
															txnCompositeID));
										((CMSCompositePOSTransaction) txn)
										.setDigitalSignature(digitalSaleSignature);
										theAppMgr.addGlobalObject(
												"SALE_DIGITAL_SIGNATURE",
												digitalSaleSignature, true);
										System.out
										.println("previousDigitalSignature IN SALE not for the first time:"
												+ previousSaleDigitalSignature);
									}
									if (txn.getTransactionType()
											.equalsIgnoreCase("RETN")) {
										previousReturnDigitalSignature = (String) theAppMgr
										.getGlobalObject("RETURN_DIGITAL_SIGNATURE");

										if (previousReturnDigitalSignature
												.equals("0")) {
											digitalReturnSignature = (DigitalSignatureUtil
													.createNewMessageAndDigitalSignature(
															txn, null,
															txnCompositeID));
											((CMSCompositePOSTransaction) txn)
											.setDigitalSignature(digitalReturnSignature);
											theAppMgr.addGlobalObject(
													"RETURN_DIGITAL_SIGNATURE",
													digitalReturnSignature,
													true);
											System.out
											.println("digitalSignature IN RETURN:"
													+ digitalReturnSignature);
										} else
											digitalReturnSignature = (DigitalSignatureUtil
													.createNewMessageAndDigitalSignature(
															txn,
															previousReturnDigitalSignature,
															txnCompositeID));
										((CMSCompositePOSTransaction) txn)
										.setDigitalSignature(digitalReturnSignature);
										theAppMgr.addGlobalObject(
												"RETURN_DIGITAL_SIGNATURE",
												digitalReturnSignature, true);
										System.out
										.println("previousDigitalSignature IN RETURN not for the first time:"
												+ previousReturnDigitalSignature);
									}
								}

								// String digitalSignature =
								// (DigitalSignatureUtil.createNewMessageAndDigitalSignature(txn,
								// previousDigitalSignature,txnCompositeID));

								// }
								// AUSTRIA DIGITAL SIGNATURE CERTIFICATION
							} else {
								String txnId = "0";
								String transtype;
								String txnCompositeID = "0";
								int year = Calendar.getInstance().get(
										Calendar.YEAR);
								boolean isReturn = false;
								String Aeskey = null;

								PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr
								.getStateObject("TXN_POS");
								CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
								transtype = txn.getTransactionType();

								// CONTROLLO CHE E' RETURN
								if (transtype.equals("RETN")) {
									isReturn = true;
								}
								// PRENDO AESKEY
								Aeskey = (String) theAppMgr
								.getGlobalObject("AES_KEY");
								System.out.println("The ASK_KEY Stored:"
										+ Aeskey);

								if (Aeskey.equals(null)) {
									Aeskey = myAesKey;
								}

								if (txn instanceof CMSMiscCollectionCredit) {
									txnId = ((CMSMiscCollectionCredit) txn)
									.getId();
								}

								if (txn instanceof CMSMiscCollection) {
									txnId = ((CMSMiscCollection) txn).getId();
								}

								if (txn instanceof CMSCompositePOSTransaction) {
									txnId = ((CMSCompositePOSTransaction) txn)
									.getId();
								}

								if (txn instanceof CollectionTransaction) {
									txnId = ((CollectionTransaction) txn)
									.getId();
								}

								// Riccardino modifica : messo nel nuovo metodo
								// createNewMessageAndDigitalSignature anche la
								// transazione composta (txnCompositeID)
								String[] digitalSaleSignature;
								String previousSaleDigitalSignature = null;
								String previousTurnOver = null;
								String actualTurnOver = null;
								String encripted = null;

								if ((activateDigitalSignature.trim()
										.equalsIgnoreCase("true"))
										&& (austrianEnv.trim()
												.equalsIgnoreCase("true"))) {

									// "AES_KEY" "CARD_CERTIFICATE"
									// "TURN_OVER_AMT"

									// Calcolo il total takings
									previousTurnOver = (String) theAppMgr
									.getGlobalObject("TURN_OVER_AMT");
									actualTurnOver = DigitalSignatureUtil
									.sumTurnover(txn, previousTurnOver);
									theAppMgr.addGlobalObject("TURN_OVER_AMT",
											actualTurnOver, true);
									System.out
									.println("Turnover CASH REGISTER Stored:"
											+ actualTurnOver);
									// Ora Passo tutto nel Encripted

									// PRENDO IL TOTALE E LO METTO IN HASH

									encripted = DigitalSignatureUtil
									.getTurnoverEncripted(Aeskey,
											txnId, shopId,
											cash_register,
											actualTurnOver);

									// PRENDO LA STRINGA HASH PRECEDENTE
									previousSaleDigitalSignature = (String) theAppMgr
									.getGlobalObject("SALE_DIGITAL_SIGNATURE");
									// CALCOLA LA DIGITAL SIGNATURE E METTO IN
									// ARRAY : 0 = DIGITAL SIGNATURE / 1 =
									// FALLEN
									digitalSaleSignature = (DigitalSignatureUtil
											.createAustriaDigitalSignature(
													txn,
													shopId,
													cash_register,
													previousSaleDigitalSignature,
													txnId, encripted,
													myCertificate, isReturn));

									// AGGIUNGO NELLA TRANSAZIONE POSTRANSACTION
									// LA DIGITALSIGNATURE
									((CMSCompositePOSTransaction) txn)
									.setDigitalSignature(digitalSaleSignature[0]);
									((CMSCompositePOSTransaction) txn)
									.setApprover(digitalSaleSignature[1]);

									// SALVO NEL ASISTRANSACTION LA STRINGA DA
									// STAMPARE
									asisData.setTxnNo(digitalSaleSignature[2]);
									((CMSCompositePOSTransaction) txn)
									.setASISTxnData(asisData);
									// SALVO NEL OGGETTO SERIALIZZATO IL TOTALE
									theAppMgr.addGlobalObject(
											"SALE_DIGITAL_SIGNATURE",
											digitalSaleSignature[0], true);

									System.out
									.println("previousDigitalSignature IN SALE:"
											+ previousSaleDigitalSignature);
									System.out
									.println("ACTUAL DigitalSignature IN SALE :"
											+ digitalSaleSignature[0]);
								}
							}

						} // Fine entrata Digital signature

					}// Ends here
					/*
					 * Added by Yves Agbessi (28-Nov-2017) - Fiscal Solutions
					 * Service Digital Signature handling during the receipt
					 * printout
					 */
					// START --
					CommonTransaction trans = (CommonTransaction) theAppMgr
					.getStateObject("TXN_POS");

					// The bridge is opened and the connection is created only
					// if this rules are matched.
					/*
					 * PAID_INS and PAID_OUTS are not posted
					 */
					if (!trans.getTransactionType().equalsIgnoreCase("PDOT")
							&& !trans.getTransactionType().equalsIgnoreCase(
							"COLL")) {
						System.out.println(trans.getTransactionType());
						ARMFSBridge bridge = new ARMFSBridge().build();

						if (trans instanceof CMSPaidOutTransaction) {

							trans = (CMSPaidOutTransaction) trans;
						}

						if (trans instanceof CMSCompositePOSTransaction) {

							trans = (CMSCompositePOSTransaction) trans;
						}

						if (trans instanceof CMSMiscCollection) {

							trans = (CMSMiscCollection) trans;
						}

						String tt = trans.getTransactionType();
						/*
						 * IF the current country is allowed to build a FS
						 * Bridge AND the transaction is not of the types listed
						 * in the 'if' statement, THEN this patch is executed
						 */

						if (bridge.isCountryAllowed
								&& (!(tt.equalsIgnoreCase("RSVO"))
										&& !(tt.equalsIgnoreCase("RSVO/RETN"))
										&& !(tt.equalsIgnoreCase("CSGO"))
										&& !(tt.equalsIgnoreCase("CSGO/RETN")) && !(tt
												.equalsIgnoreCase("NFNS")))) {

							ARMFSBridgeObject object = new ARMFSBridgeObject();

							// --Constructing the correct BridgeObject according
							// to
							// the type of the transaction
							POST_RULE_1 = true;// OPEN DEPOSITS rule. If ==
							// false, open deposits are not
							// posted

							if (tt.equalsIgnoreCase("SALE")
									|| tt.equalsIgnoreCase("RSVO/SALE")
									|| tt.equalsIgnoreCase("CSGO/SALE")) {

								object = new ARMFSBOSaleTransactionObject(
										(CMSCompositePOSTransaction) trans);

								if (((CMSCompositePOSTransaction) trans)
										.getDepositLineItems().length == ((CMSCompositePOSTransaction) trans)
										.getLineItemsArray().length) {
									POST_RULE_1 = false;// OPEN DEPOSITS rule.
									// If == false, open
									// deposits are not
									// posted
								}

							}

							if (trans.getTransactionType().equalsIgnoreCase(
							"RETN")) {

								object = new ARMFSBOReturnTransactionObject(
										(CMSCompositePOSTransaction) trans);
							}

							if (trans.getTransactionType().equalsIgnoreCase(
							"SALE/RETN")) {

								object = new ARMFSBOReturnWithSalesTransactionObject(
										(CMSCompositePOSTransaction) trans);
							}

							/*
							 * Paid Out transaction case if
							 * (trans.getTransactionType
							 * ().equalsIgnoreCase("PDOT")) {
							 * 
							 * object = new ARMFSBOPayOutObject(
							 * (CMSPaidOutTransaction) trans);
							 * 
							 * }
							 * 
							 * // Paid In transaction case if
							 * (trans.getTransactionType
							 * ().equalsIgnoreCase("COLL")) {
							 * 
							 * object = new ARMFSBOPayInObject(
							 * (CMSMiscCollection) trans);
							 * 
							 * }
							 */

							/*
							 * If the transaction is a training transaction, the
							 * object will be a Training Transaction whichever
							 * transaction type
							 */

							/*
							 * if (trans.isTrainingFlagOn()) {
							 * 
							 * object = new ARMFSBOTrainingTransactionObject(
							 * (CMSCompositePOSTransaction) trans); }
							 */

							// ----------------------

							boolean postingResult = false;

							if (POST_RULE_1 == true) {// Preventing from posting
								// if the rules are not
								// matched

								postingResult = new ARMFSBridge().build()
								.postObject(object);
							}

							if (!postingResult && POST_RULE_1 == true) {
								theAppMgr
								.showErrorDlg("[FS BRIDGE] "
										+ "Unable to post Transaction Object. "
										+ "\n Please call the Help Desk Immediately. Press OK to print the recipt");

							} else if (POST_RULE_1 == false) {
							} else {

								String response = object.processResponse();

								if (response.contains("ERROR")
										|| response.contains("UNABLE")) {
									theAppMgr
									.showErrorDlg("[FS BRIDGE] "
											+ response
											+ "\n Please call the Help Desk Immediately. Press OK to print the recipt");

									((CMSCompositePOSTransaction) trans)
									.setDigitalSignature("UNABLE_TO_RETRIEVE_DIGITAL_SIGNATURE");

								} else {
									if (trans instanceof CMSCompositePOSTransaction) {
										((CMSCompositePOSTransaction) trans)
										.setDigitalSignature(response);
									}
									if (trans instanceof CMSMiscCollection) {
										ASISTxnData txnData = new ASISTxnData();
										txnData.setComments(response);
										((CMSMiscCollection) trans)
										.setASISTxnData(txnData);

									}
								}
							}
						}
					}

					/*
					 * 
					 * */
					// -- END
					// -----------------------------------------------------------------------------------------------------------------------------------------
					// -----------------------------------------------------------------------------------------------------------------------------------------
					// -----------------------------------------------------------------------------------------------------------------------------------------

					/**
					 * STAMPA LO SCONTRINO ....
					 */
					int result = fiscalInterface.printFiscalReceipt(theTxn);
					isFiscalReceiptPrinted = true;
					// Sergio
					if (result == 0) {
						fiscalReceiptNumber = fiscalInterface
						.getDocumentResponse().getDocumentNumber(); // Sergio)
						// set Fiscal Number
						if (fiscalReceiptNumber != null
								&& fiscalReceiptNumber.length() != 0) {
							CommonTransaction txn = (CommonTransaction) theAppMgr
							.getStateObject("TXN_POS");
							if (txn instanceof CMSCompositePOSTransaction) {
								theTxnPOS = (CMSCompositePOSTransaction) txn;
								theTxnPOS
								.setFiscalReceiptNumber(fiscalReceiptNumber); // Sergio
								// not
								// has
								// to
								// be
								// saved
								// but
								// just
								// after
								// the
								// printout
								theTxnPOS.setFiscalReceiptDate(fiscalInterface
										.getFiscalReceiptDate());

							} else if (txn instanceof CollectionTransaction) {
								((CMSCollectionTransaction) txn)
								.setFiscalReceiptNumber(fiscalReceiptNumber);
								((CMSCollectionTransaction) txn)
								.setFiscalReceiptDate(fiscalInterface
										.getFiscalReceiptDate());
							} else if (txn instanceof CMSPaidOutTransaction) {
								((CMSPaidOutTransaction) txn)
								.setFiscalReceiptNumber(fiscalReceiptNumber);
								((CMSPaidOutTransaction) txn)
								.setFiscalReceiptDate(fiscalInterface
										.getFiscalReceiptDate());
							}
						}
						/*
						 * Following code's been added by Yves Agbessi on
						 * 06-Jun-2018 In case of french store : if the
						 * transaction's an open deposit - the fiscal number
						 * will not be incremented /START--
						 */
						if (fiscalReceiptNumber != null
								&& fiscalReceiptNumber.length() != 0
								&& POST_RULE_1 == false) {
							CommonTransaction txn = (CommonTransaction) theAppMgr
							.getStateObject("TXN_POS");
							if (txn instanceof CMSCompositePOSTransaction) {
								theTxnPOS = (CMSCompositePOSTransaction) txn;
								String frn = ""; // If French Store and Open
								// deposit -> Do not
								// increment fiscal receipt
								// num
								theTxnPOS.setFiscalReceiptNumber(String
										.valueOf(frn));

								theTxnPOS.setFiscalReceiptDate(fiscalInterface
										.getFiscalReceiptDate());

							} else if (txn instanceof CollectionTransaction) {
								((CMSCollectionTransaction) txn)
								.setFiscalReceiptNumber(fiscalReceiptNumber);
								((CMSCollectionTransaction) txn)
								.setFiscalReceiptDate(fiscalInterface
										.getFiscalReceiptDate());
							} else if (txn instanceof CMSPaidOutTransaction) {
								((CMSPaidOutTransaction) txn)
								.setFiscalReceiptNumber(fiscalReceiptNumber);
								((CMSPaidOutTransaction) txn)
								.setFiscalReceiptDate(fiscalInterface
										.getFiscalReceiptDate());
							}
						}/*
						 * --END
						 */
						fiscalNoLbl.setText(fiscalReceiptNumber);
						// theAppMgr.showErrorDlg(res.getString("Receipt printed successfuly."));
						// if (postTransaction()) {
						// isPostComplete = true;
						// initReceiptButtons();
						// } else {
						// theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
						// }
						// //Sergio
						// theAppMgr.showMenu(MenuConst.PRINT_FISCAL_DOCUMENT,
						// "PRINT_FISCAL", theOpr);
						// return;
					} else {
						// As per request dated : 08 october 2007 -WE need to
						// modify the flow after an hardware error on the fiscal
						// printer.
						System.out.println("MESSAGGIO USCITO");
						LoggingServices.getCurrent().logMsg(
								getClass().getName(), "MESSAGE ERROR PAYMENT",
								"messaggio uscito", "messaggio uscito",
								LoggingServices.MAJOR);
						boolean doRetry = theAppMgr
						.showOptionDlg(
								res.getString("Print Fiscal Receipt Failed."),
								res.getString("Errore!Spegni e Riaccendi la Stampante Fiscale.Premi RETRY poi HOME e rifare lo scontrino."
										+ "The receipts cannot be printed.Replace the operation."),
										res.getString("Retry"), res
										.getString("Post Txn"));

						/*
						 * Added by Yves Agbessi (04-Dec-2017) Handles the
						 * Printer not available event for Fiscal Solutions
						 * Service START --
						 */

						ARMFSBridge fsBridge = new ARMFSBridge().build();
						if (fsBridge.isCountryAllowed) {
							Date now = new Date();
							ARMFSBOPosEventObject terminalShutdownEvent = new ARMFSBOPosEventObject(
									MessageTypeCode.MTC_POS_EVENT,
									ARMEventIDGenerator.generate(now),
									PosEvent.PEC_PRINTER_NOT_AVAILABLE_CODE,
									PosEvent.PEC_PRINTER_NOT_AVAILABLE_DESCRIPTION,
									Integer.parseInt(PosEvent.PEC_PRINTER_NOT_AVAILABLE_VALUE),
									now,
									new ConfigurationManager()
									.getConfig(
											ARMFSBridgeObject.registerCfgFilePath,
											"STORE_ID")
											+ new ConfigurationManager()
									.getConfig(
											ARMFSBridgeObject.registerCfgFilePath,
											"REGISTER_ID"),
							"DIRETTORE DIRETTORE");

							if (fsBridge.postObject(terminalShutdownEvent)) {

								String response = terminalShutdownEvent
								.processResponse();
								if (response.contains("ERROR")
										|| response.contains("UNABLE")) {
									theAppMgr
									.showErrorDlg("[FS BRIDGE] "
											+ response
											+ "\n Please call the Help Desk Immediately. Press OK to continue");

								}

							} else {

								theAppMgr
								.showErrorDlg("[FS BRIDGE] : Unable to post PRINTER_NOT_AVAILABLE_EVENT. Press OK to continue\n"
										+ "Please call the Help Desk Immediately");

							}
						}

						/*
						 * 
						 * -- END
						 */

						if (doRetry) {
							// theAppMgr.unRegisterSingleEditArea();
							// theTxn.testIsSuspendable();
							// theAppMgr.showMenu(MenuConst.CANCEL_ONLY,
							// theOpr);
							// theAppMgr.setSingleEditArea(res.getString("Enter comments."),
							// "SUSPEND_COMMENT");
							// theAppMgr.setEditAreaFocus();
							/*
							 * int rows = model.getTotalRowCount();
							 * System.out.println("rows------->"+rows);
							 * model.firstPage(); for(int i=0;i<rows;i++) {
							 * deletePayment(0); }
							 */
							int row = tblPayment.getRowCount();
							for (int i = 0; i < row; i++) {
								// Payment payment = model.getPayment(i);
								Vector vTemp = model.getCurrentPage();
								Payment payment = (Payment) vTemp.elementAt(i);
								int tableRows = tblPayment.getRowCount();
								System.out
								.println("tableRows count in table------->"
										+ tableRows);
								if ((tableRows != 0) && (payment != null)) {
									model.removeRow(i);
									theTxn.getPaymentTransaction()
									.removeAllPayments();
									// payment.setAmount(new ArmCurrency(0.00));
								} else {
									Payment[] payments = theTxn
									.getPaymentsArray();
									for (int idx = 0; idx < payments.length; idx++) {
										model.addPayment(payments[idx]);
										model.fireTableDataChanged();
									}
								}
							}
							model.fireTableDataChanged();
							anEvent.consume();
							initButtons(CMSPaymentMgr.PAYMENT);
							// selectOption();
							return;
						} else {
							theAppMgr
							.showErrorDlg(res
									.getString("Please prepare manual receipt for customer"));
							// if (postTransaction()) {
							// isPostComplete = true;
							// initReceiptButtons();
							// } else {
							// theAppMgr.showMenu(MenuConst.CANCEL_ONLY,
							// theOpr);
							// }
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
							PaymentApplet_EUR.this
							.appButtonEvent(new CMSActionEvent(this, 0,
									"POST_TXN", 0));
							theAppMgr.setWorkInProgress(false);
							theAppMgr.setEditAreaFocus();
						}
					});
					post.start();

					// } catch (BusinessRuleException bre) {
					// theAppMgr.showErrorDlg(bre.getMessage());
					// // theAppMgr.showMenu(MenuConst.POST,
					// "PRINT_FISCAL_RECEIPT", theOpr);
					// // PaymentApplet_EUR.this.appButtonEvent(new
					// CMSActionEvent(this, 0, "PRINT_FISCAL_RECEIPT"
					// // , 0));
					// // return;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (sAction.equals("PRINT_DUPLICATE_FISCAL_RECEIPT")) {
				int result = fiscalInterface.printFiscalReceipt(theTxn);
				// Alex
				if (result == 0) {
					// if (result == 1) {
					// theAppMgr.showErrorDlg(res.getString("Receipt re-  successfuly."));
					return;
				} else {
					theAppMgr.showErrorDlg(res
							.getString("Error re-printing Fiscal receipt."));
				}
			} else if (sAction.equals("CHECK")) {
				applyVat(true);
				initValues();
				updateLabels();
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in
				// process
				String Builder = PaymentMgr.getPaymentBuilder("CHECK");
				System.out.println("The Transaction Value  "
						+ theTxn.getPaymentTransaction());
				System.out.println("the builder is: " + Builder);
				theAppMgr.buildObject("PAYMENT", Builder, "");
			} else {
				applyVat(false);
				initValues();
				updateLabels();
				// String Builder = PaymentMgr.getPaymentBuilder(sAction);
				if (isButtonInList(sAction)) {
					// if customer is present
					if (!flag) {
						// and is coming from CustomerSaleApplet
						if (!bool) {
							String Builder = PaymentMgr
							.getPaymentBuilder(sAction);
							if (Builder != null) {
								theAppMgr.buildObject(this, "PAYMENT", Builder,
										sAction);
							}
						}
						// customer present but not from CustSaleApplet
						else if (theAppMgr.getStateObject("THE_EVENT") != null
								&& (theAppMgr.getStateObject("THE_EVENT")
										.equals("SUCCESS") || theAppMgr
										.getStateObject("THE_EVENT").equals(
										"FAILURE"))) {
							PaymentApplet_EUR.this
							.appButtonEvent(new CMSActionEvent(this, 0,
									INITIAL_PAYMENT, 0));
							theAppMgr.removeStateObject("THE_EVENT");
						} else {
							String Builder = PaymentMgr
							.getPaymentBuilder(sAction);
							if (Builder != null) {
								theAppMgr.buildObject(this, "PAYMENT", Builder,
										sAction);
							}
						}
					} else {
						// if not coming from CustolerListApplet
						if (!bool) {
							// if customer is not present then gather
							// information
							theAppMgr.addStateObject("PAYMENT", sAction);
							theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
						} else {
							// if the customer is already present when coming to
							// PaymentApplet_EUR for the
							// first time
							if (flag) {
								theAppMgr.addStateObject("PAYMENT", sAction);
								theAppMgr.fireButtonEvent("CUST_SALE_HIDDEN");
							} else {
								String Builder = PaymentMgr
								.getPaymentBuilder(sAction);
								if (Builder != null) {
									theAppMgr.buildObject(this, "PAYMENT",
											Builder, sAction);
								}
							}
						}
					}
				} else {
					String Builder = CMSPaymentMgr.getPaymentBuilder(sAction);
					if (Builder != null) {
						theAppMgr.buildObject("PAYMENT", Builder, sAction);
					}
					// flag = false;
				}
			}
		} catch (BusinessRuleException bre) {
			theAppMgr.showErrorDlg(bre.getMessage());
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	/*
	 * This method will remove a payment from the Txn and delete any other
	 * payments back to the customer. Ex. If the amount given is greater than
	 * the amount due, then the amount in change is then determined. If the Eu
	 * then deletes a payment, then change amount will remain unless found and
	 * deleted so the amount to give in change can be recalculated.
	 */
	private void deletePayment() {
		int row = tblPayment.getSelectedRow();
		if (row < 0) {
			theAppMgr.showErrorDlg(res
					.getString("You must first select a payment."));
		} else {
			deletePayment(row);
		}
	}

	private void deletePayment(int row) {
		if (row < 0) {
			return;
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
							System.out
							.print("Currency Ex deletePayment-> " + e);
						}
					}
				}
				updateLabels();
				tblPayment.repaint();
				selectPayment();
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(ex.getMessage());
			} catch (CurrencyException e) {
				System.out.print("Currency Ex deletePayment-> " + e);
			}
		}
	}

	/**
	 */
	private void updateLabels() {
		try {
			AmtTnd$Lbl.setText(theTxn.getTotalPaymentAmount()
					.formattedStringValue());
			ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue();
			amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
			AmtDue$Lbl.setText(amtDue.formattedStringValue());
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(getClass().getName(),
					"updateLabels", "Unable to subtract two currencies",
					"The two currencies are of different type.",
					LoggingServices.MINOR, ex);
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void printVATInvoice() {
		try {
			if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
				String txnClassName = theTxn.getPaymentTransaction().getClass()
				.getName();
				String shortClassName = txnClassName.substring(
						txnClassName.lastIndexOf(".") + 1,
						txnClassName.length());
				String fileName = ReceiptConfigInfo.getInstance()
				.getPathForRDO() + shortClassName + ".rdo";
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
				String txnClassName = theTxn.getPaymentTransaction().getClass()
				.getName();
				String shortClassName = txnClassName.substring(
						txnClassName.lastIndexOf(".") + 1,
						txnClassName.length());
				String fileName = ReceiptConfigInfo.getInstance()
				.getPathForRDO() + shortClassName + ".rdo";
				ObjectStore objectStore = new ObjectStore(fileName);
				objectStore.write(theTxn);
			}
			((CMSPaymentTransactionAppModel) theTxn)
			.printRetailExportReceipt(theAppMgr);
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void printGiftReceipt() {
		try {
			if (theTxn instanceof CMSPaymentTransactionAppModel) {
				if (!pnlGiftReceipt.getGiftReceiptModel()
						.containsSelectedItems()) {
					theAppMgr
					.showErrorDlg("No valid items are selected to print a gift receipt.");
					return;
				}
				GiftReceiptEntry[] entries = pnlGiftReceipt
				.getGiftReceiptModel().getSelectedEntries();
				((CMSPaymentTransactionAppModel) theTxn)
				.setSelectedGiftItems(entries);
				if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
					String txnClassName = theTxn.getPaymentTransaction()
					.getClass().getName();
					String shortClassName = txnClassName.substring(
							txnClassName.lastIndexOf(".") + 1,
							txnClassName.length());
					String fileName = ReceiptConfigInfo.getInstance()
					.getPathForRDO() + shortClassName + ".rdo";
					ObjectStore objectStore = new ObjectStore(fileName);
					objectStore.write(theTxn);
				}
				GiftReceiptEntry[] getEntries = ((CMSPaymentTransactionAppModel) theTxn)
				.getSelectedGiftItems();
				theTxn.printGiftReceipt(theAppMgr);
				// Alex: call to fiscalInterface
				fiscalInterface.printGiftReceipt(theTxn);
				pnlGiftReceipt.getGiftReceiptModel().setAllItemSelection(
						new Boolean(false));
				pnlGiftReceipt.repaint();
			} else {
				theAppMgr
				.showErrorDlg(res
						.getString("A gift receipt is not allowed for this transaction."));
			}
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(ex.getMessage());
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void endorseCheck() {
		FrankDlg dlg = new FrankDlg(theAppMgr, theTxn.getPaymentTransaction(),
				theTxn.getCustomer().getTelephone() == null ? "" : theTxn
						.getCustomer().getTelephone().getFormattedNumber());
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
	// private boolean authorize () {
	// try {
	// Register register = (Register)theAppMgr.getGlobalObject("REGISTER");
	// if (theTxn.getCompositeTotalAmountDue().greaterThan(new
	// ArmCurrency(0.0))) {
	// CMSCreditAuthHelper.validate(theAppMgr, theTxn.getPaymentTransaction(),
	// register.getId());
	// for (Enumeration enm = theTxn.getPayments(); enm.hasMoreElements();) {
	// Payment pay = (Payment)enm.nextElement();
	// if (pay.isAuthRequired())
	// return (false);
	// }
	// }
	// return (true);
	// } catch (Exception ex) {
	// theAppMgr.showExceptionDlg(ex);
	// return (false);
	// }
	// }
	private boolean authorize() {
		try {
			// * Set the result = "00" (Megha)
			result = "00";
			String StatusID[] = null;
			Register register = (Register) theAppMgr
			.getGlobalObject("REGISTER");
			Payment[] payments = theTxn.getPaymentsArray();
			int len = payments.length;
			if (theTxn.getCompositeTotalAmountDue().greaterThan(
					new ArmCurrency(0.0))) {
				StatusID = CMSCreditAuthHelper.validate(theAppMgr,
						theTxn.getPaymentTransaction(), register.getId());
				// Made change in checking the Error
				// Condition:StatusID[i]!=null(Megha)
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
				for (Enumeration enm = theTxn.getPayments(); enm
				.hasMoreElements();) {
					Payment pay = (Payment) enm.nextElement();
					if (pay.isAuthRequired()) {
						return (false);
					}
				}
			}
			return (true);
		} catch (Exception ex) {
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
		tblPayment.getSelectionModel().setSelectionInterval(
				tblPayment.getRowCount() - 1, tblPayment.getRowCount() - 1);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param absoluteRow
	 */
	void selectRow(int absoluteRow) {
		model.pageContainingRow(absoluteRow);
		tblPayment.getSelectionModel().setSelectionInterval(
				absoluteRow % model.getRowCount(),
				absoluteRow % model.getRowCount());
	}

	/**
	 * @param e
	 */
	void tblPayment_componentResized(ComponentEvent e) {
		tblPayment.getColumnModel().getColumn(model.TYPE)
		.setPreferredWidth(140);
		tblPayment.getColumnModel().getColumn(model.EXPIRE)
		.setPreferredWidth(95);
		tblPayment.getColumnModel().getColumn(model.APPROVAL)
		.setPreferredWidth(125);
		// Modified by Vivek Mishra for resize column PCR
		// tblPayment.getColumnModel().getColumn(model.AMOUNT).setPreferredWidth(115);
		tblPayment.getColumnModel().getColumn(model.AMOUNT)
		.setPreferredWidth(175);
		// End
		tblPayment
		.getColumnModel()
		.getColumn(model.DETAIL)
		.setPreferredWidth(
				tblPayment.getWidth()
				- (tblPayment.getColumnModel()
						.getColumn(model.TYPE)
						.getPreferredWidth()
						+ tblPayment.getColumnModel()
						.getColumn(model.EXPIRE)
						.getPreferredWidth()
						+ tblPayment.getColumnModel()
						.getColumn(model.APPROVAL)
						.getPreferredWidth() + tblPayment
						.getColumnModel()
						.getColumn(model.AMOUNT)
						.getPreferredWidth()));
		// DefaultTableCellRenderer HorAlignRenderer = new
		// DefaultTableCellRenderer();
		// HorAlignRenderer.setHorizontalAlignment(4);
		// tblPayment.getColumnModel().getColumn(model.AMOUNT).setCellRenderer(HorAlignRenderer);
		model.setRowsShown(tblPayment.getHeight() / tblPayment.getRowHeight());
	}

	/**
	 * callback when <b>Page Down</b> icon is clicked
	 */
	public void pageDown(MouseEvent e) {
		model.nextPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1,
				model.getPageCount());
		if (pnlGiftReceipt != null) {
			pnlGiftReceipt.getGiftReceiptModel().nextPage();
			theAppMgr.showPageNumber(e, pnlGiftReceipt.getGiftReceiptModel()
					.getCurrentPageNumber() + 1, pnlGiftReceipt
					.getGiftReceiptModel().getPageCount());
		}
	}

	/**
	 * callback when <b>Page Up</b> icon is clicked
	 */
	public void pageUp(MouseEvent e) {
		model.prevPage();
		theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1,
				model.getPageCount());
		if (pnlGiftReceipt != null) {
			pnlGiftReceipt.getGiftReceiptModel().prevPage();
			theAppMgr.showPageNumber(e, pnlGiftReceipt.getGiftReceiptModel()
					.getCurrentPageNumber() + 1, pnlGiftReceipt
					.getGiftReceiptModel().getPageCount());
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
		CMSCompositePOSTransaction transaction = (CMSCompositePOSTransaction) theTxn
		.getPaymentTransaction();
		CMSV12Basket cmsV12Basket = null;
		if (transaction != null)
			cmsV12Basket = transaction.getCmsV12Basket();
		if (postingInProgress)
			return false;
		if (!isCreditAuthComplete) {
			theAppMgr.showErrorDlg(res.getString("Credit Auth in progress!"));
			return false;
		}
		if (!isPostComplete && !cancelTxnPressed) {
			try {
				if (theAppMgr
						.showOptionDlg(
								res.getString("Cancel Transaction"),
								res.getString("Selecting 'Home' cancels the transaction.  Do you want to cancel this transaction?"))) {
					if (cmsV12Basket != null) {
						CMSV12BasketHelper.setBasketStatus(theAppMgr,
								cmsV12Basket, CMSV12Basket.open);
						theAppMgr.removeStateObject("V12BASKET_LOOKUP");
					}
					ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr);
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
	 * call this method to check if we need to proceed to the CustomerListApplet
	 * for gathering information about the customer. Added by Anand Kannan on
	 * 03/24/2005
	 */
	private boolean isButtonInList(String sAction_global) {
		ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
		String customerReqd = cfgMgr.getString("REQUIRES_CUSTOMER");
		Vector v = new Vector();
		StringTokenizer st = new StringTokenizer(customerReqd, ",");
		while (st.hasMoreTokens()) {
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
	 * To be used to set the font of "Amount Due" and "Amount Tendered" in
	 * PaymentApplet_EUR Method added by Anand Kannan on 03/25/2005
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
	 * 
	 * @return
	 */
	private boolean isMaxChangeAllowed() {
		String strChangeType = null;
		String strMaxChange = null;
		ArmCurrency maxChange = null;
		ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue();
		// theAppMgr.showErrorDlg("amtDue initial is "
		// +theTxn.getCompositeTotalAmountDue());
		try {
			amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
			Payment[] payments = theTxn.getPaymentsArray();
			if (isAutoChange) {
				strChangeType = cfgMgr.getString("AUTO_CHANGE_TYPE");
				String strLastPaymentName = payments[payments.length - 1]
				                                     .getTransactionPaymentName();
				if (strChangeType != null && strLastPaymentName != null) {
					strMaxChange = cfgMgr.getString(strLastPaymentName
							+ ".MAX_CHANGE_ALLOWED");
					maxChange = new ArmCurrency(strMaxChange);
					theAppMgr.addStateObject("MAX_CHANGE", maxChange);
					// theAppMgr.addStateObject("IS_CHANGE", "true");
				} else {
					theAppMgr
					.showErrorDlg("No valid change type has been configured as default");
					return false;
				}
				if (amtDue.absoluteValue().lessThanOrEqualTo(maxChange)) {
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

	/*
	 * (non Javadoc)
	 * 
	 * @see com.armani.Setefi#fireSetefiresult(java.lang.Object)
	 */
	public void fireSetefiResult(Object p) {
		// Sergio
		// TODO Stub di metodo generato automaticamente
		isCreditAuthComplete = true;
		((MainFrame) theAppMgr.getMainFrame()).setState(0);
		SetefiManagement set = (SetefiManagement) p;
		set.getSetefiResult();
		CreditCard sr = set.getSetefiresultObject();
		// Added by Yves Agbessi (09-Nov-2017). Prints a message on the console.
		if (sr == null) {
			System.out
			.println("NO or WRONG setefi value returned from the pos.");
			return;

		}// End
		if (((CMSCreditCard) sr).getPaymentCode() == null) {
			((CMSCreditCard) sr).setPaymentCode("000");
		}

		set.release();
		// this.theAppMgr.getParentFrame().toBack();
		set = null;
		// theAppMgr.showErrorDlg(sr.getRespStatusCode());
		if (sr.getRespStatusCode().compareTo("01") == 0) {
			System.out.print(sr.getAmount().doubleValue() + "\n"); // Amount
			System.out.print(sr.getTransactionPaymentName() + "\n"); // CC type
			System.out.print(sr.getTenderType() + "\n"); // Asis payment code
			System.out.print(sr.getAccountNumber() + "\n"); // CC number
			System.out.print(sr.getRespStatusCode() + "\n"); // Status code
			// 01=OK 02=Not
			// approved
			// 03=Referral
			// 04=timeout 05
			// = systemerror

			theAppMgr.unRegisterSingleEditArea();
			theAppMgr.getMainFrame().show();
			theAppMgr.showErrorDlg("Transaction completed ...");
			objectEvent("PAYMENT", sr);

			// this.theAppMgr.getParentFrame().toBack();
			// this.theAppMgr.getMainFrame().show();
			// this.theAppMgr.getParentFrame().toBack();
		} else {
			// In case the error = 05 the getTransactionPaymentName() is "";
			// theAppMgr.showErrorDlg(sr.getRespStatusCode() + "\n"); //Status
			// code 01=OK 02=Not approved 03=Referral 04=timeout 05 =
			// systemerror
			// String Builder = CMSPaymentMgr.getPaymentBuilder(
			// "CREDIT_CARD");
			// System.out.println("the builder is: " + Builder);
			// creditCardAmountForSetefi = sr.getAmount();
			SwingUtilities.invokeLater(new Runnable() {

				/**
				 * put your documentation comment here
				 */
				public void run() {
					theAppMgr.unRegisterSingleEditArea();
					theAppMgr.buildObject("PAYMENT",
							CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD"),
							PaymentApplet_EUR.this.creditCardAmountForSetefi);
				}
			});
			// return;
		}
	}

	/**
	 * @param amt
	 * @return
	 */
	private boolean validateChangeAmount(ArmCurrency amt) {
		try {
			PaymentTransaction theTxn = (PaymentTransaction) theAppMgr
			.getStateObject("TXN_POS");
			// CMSPaymentTransactionAppModel appModel = new
			// CMSPaymentTransactionAppModel(theTxn);
			CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) theTxn
			.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			ArmCurrency amtLeft = appModel.getCompositeTotalAmountDue()
			.subtract(appModel.getTotalPaymentAmount());
			// if (amtLeft.greaterThanOrEqualTo(new ArmCurrency(0.0)))
			// return true;
			if (amt.greaterThan(amtLeft.absoluteValue())) {
				theAppMgr
				.showErrorDlg(res
						.getString("You can not give more change than is due."));
				return (false);
			} else
				return (true);
		} catch (Exception ex) {
			return (true);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param paymentType
	 * @return
	 */
	private CMSPaymentCode getPaymentCode(String paymentType) {
		String cardCode = "";
		List paymentCodes = CMSPaymentMgr.getPaymentCode(paymentType);
		int numPaymentCodes = paymentCodes.size();
		if (numPaymentCodes == 0)
			return null;
		if (numPaymentCodes == 1) {
			return (CMSPaymentCode) paymentCodes.get(0);
			// cardCode = paymentCode.getPaymentCode();
		} else {
			CMSPaymentCode[] payCodes = new CMSPaymentCode[numPaymentCodes];
			GenericChooserRow[] availPaymentCodes = new GenericChooserRow[numPaymentCodes];
			for (int i = 0; i < numPaymentCodes; i++) {
				CMSPaymentCode paymentCode = (CMSPaymentCode) paymentCodes
				.get(i);
				payCodes[i] = paymentCode;
				availPaymentCodes[i] = new GenericChooserRow(
						new String[] { paymentCode.getPaymentDesc() },
						paymentCode);
			}
			GenericChooseFromTableDlg overRideDlg = new GenericChooseFromTableDlg(
					theAppMgr.getParentFrame(), theAppMgr, availPaymentCodes,
					new String[] { "Payment Desc" });
			overRideDlg.setVisible(true);
			if (overRideDlg.isOK()) {
				return (CMSPaymentCode) overRideDlg.getSelectedRow()
				.getRowKeyData();
				// cardCode = payCode.getPaymentCode();
				// paymentType = payCode.getPaymentType();
			}
		}
		return null;
	}

	/*
	 * Method for PCR from Europe 1864 For VAT Exempt Transaction : After the
	 * ticket printout all the buttons pressed by the operator except
	 * "Fiscal Document" will produce a warning window with
	 * "Mandatory add a fiscal Document on this transaction".
	 */

	private void validateFiscalDocumentPresentInTransaction() {
		PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr
		.getStateObject("TXN_POS");
		if (txn instanceof CMSCompositePOSTransaction) {
			CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
			if (theTxnPOS != null && theTxnPOS.isTaxExempt()) {
				try {
					theTxnPOS.validateFiscalDocumentPresent();
				} catch (BusinessRuleException bre) {
					if (bre.getMessage() != null
							&& bre.getMessage().length() > 0) {
						theAppMgr.showErrorDlg(bre.getMessage());
						return;
					}
				}
			}
		}

	}

	private class PaymentRenderer extends JLabel implements TableCellRenderer {
		private Color DefaultBackground;
		private Color DefaultForeground;

		/**
		 */
		public PaymentRenderer() {
			super();
			setFont(new Font("Helvetica", 1, 16));
			setForeground(new Color(0, 0, 175));
			setBackground(theAppMgr.getTheme()
					.getTableSelectionBackgroundColor());
			DefaultBackground = getBackground();
			DefaultForeground = getForeground();
			setOpaque(true);
		}

		/**
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {
			try {
				if (col == 2)
					setHorizontalAlignment(SwingConstants.CENTER);
				else
					setHorizontalAlignment(SwingConstants.LEFT);
				PaymentTableModel model = (PaymentTableModel) table.getModel();
				Payment payment = (Payment) model.getPayment(row);

				if (isSelected) {
					setForeground(Color.white);
					setBackground(table.getSelectionBackground());
				} else {
					setBackground(Color.white);
					setForeground(new Color(0, 0, 175));
				}

				if (payment.getAmount().lessThan(new ArmCurrency(0.0d))) {
					setFont(new Font("Helvetica", 1, 20));
					setForeground(Color.red);
					setBackground(Color.white);
				} else {
					setFont(new Font("Helvetica", 1, 16));
				}

			} catch (CurrencyException ce) {

			}
			setText(value.toString());
			return (this);
		}
	}

	// added by vivek for employee sale
	public void callRule(int obj) {
		Object object = theAppMgr.getStateObject("TXN_POS");
		CMSCompositePOSTransaction theTxnPOS = (CMSCompositePOSTransaction) object;
		try {
			theTxnPOS.validateEmployeeRuleForCurrentTransaction(txnCust);
		} catch (BusinessRuleException bre) {
			if (bre.getMessage() != null && bre.getMessage().length() > 0) {
				// Vishal Yevale : If appears the Budget Message , the VIP
				// message is not required. 29th May 2017
				proceedVIPdiscountMsg = false;
				// end Vishal Yevale 29 May 2017
				// IF HK FACCIO VEDERE UN DIALOG SENZA SCELTA DI PROSEGUIRE
				if (isHKenv) {
					theAppMgr.showErrorDlg(bre.getMessage());
					SwingUtilities.invokeLater(new Runnable() {

						public void run() {
							theAppMgr.fireButtonEvent("PREV");
						}
					});
					counter = 0;// Added by Yves Agbessi on 29-May-2018 ->
					// Resets the discount counter - BUG FIX
					return;
				} else { // SE E' EUROPA FACCIO VEDERE UN DIALOG SENZA SCELTA DI
					// PROSEGUIRE
					if (!theAppMgr.showOptionDlg("Employee Alert",
							bre.getMessage())) {
						SwingUtilities.invokeLater(new Runnable() {

							public void run() {
								theAppMgr.fireButtonEvent("PREV");
							}
						});
						counter = 0; // Added by Yves Agbessi on 29-May-2018 ->
						// Resets the discount counter - BUG FIX
						return;
					}
				}
			} //
		}
		// }
		// counter = 0;
	}
	/*
	 * boolean doRetry =
	 * theAppMgr.showOptionDlg(res.getString("Print Fiscal Receipt Failed."
	 * ),res.getString(
	 * "Errore!Spegni e Riaccendi la Stampante Fiscale.Premi RETRY poi HOME e rifare lo scontrino."
	 * + "The receipts cannot be printed.Replace the operation."),res.getString(
	 * "Retry"), res.getString("Post Txn"));
	 */

}
