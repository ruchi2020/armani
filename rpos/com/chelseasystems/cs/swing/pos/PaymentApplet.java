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
 --------------------------------------------------------------------------------------------
 | 10   | 09-15-2010 | Vivek     |    CR     | Changed POS application to make it           |
 | 		|			 |			 |			 |	PCI-Complaint.        						|
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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.jai.RenderedImageAdapter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.discount.DiscountMgr;
import com.chelseasystems.cr.dm.display.DisplayManager;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.park.ParkFileServices;
import com.chelseasystems.cr.payment.BankCheck;
import com.chelseasystems.cr.payment.Cash;
import com.chelseasystems.cr.payment.Check;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.DebitCard;
import com.chelseasystems.cr.payment.IPaymentConstants;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.PaymentMgr;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.payment.StoreValueCard;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.receipt.CMSDrawer;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cr.register.LightPoleDisplay;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.dlg.OptionDlg;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.util.HTML;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CustomerCreditCard;
import com.chelseasystems.cs.customer.CustomerSearchString;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.layaway.CMSLayawayPaymentTransactionAppModel;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.msr.ET1000JPOSForm;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.payment.CMSBankCheck;
import com.chelseasystems.cs.payment.CMSDebitCard;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.CMSPaymentCode;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.payment.CMSRedeemable;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.payment.MailCheck;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransactionAppModel;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransaction;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransactionAppModel;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItemDetail;
import com.chelseasystems.cs.pos.CMSSaleLineItemDetail;
import com.chelseasystems.cs.pos.PresaleTransaction;
import com.chelseasystems.cs.pos.RewardTransaction;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterSessionAppModel;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.swing.builder.CreditCardBldrUtil;
import com.chelseasystems.cs.swing.builder.DueBillIssueBldr;
import com.chelseasystems.cs.swing.builder.ValidateCardType;
import com.chelseasystems.cs.swing.dlg.CommunicationErrorDlg;
import com.chelseasystems.cs.swing.dlg.FrankDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooseFromTableDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cs.swing.dlg.SignatureCaptureDlg;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.model.GiftReceiptEntry;
import com.chelseasystems.cs.swing.model.PaymentTableModel;
import com.chelseasystems.cs.swing.panel.GiftReceiptPanel;
import com.chelseasystems.cs.swing.panel.POSHeaderPanel;
import com.chelseasystems.cs.tax.CMSValueAddedTaxHelper;
import com.chelseasystems.cs.tax.TaxUtilities;
import com.chelseasystems.cs.txnnumber.CMSTransactionNumberHelper;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.cs.util.EncryptionUtils;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;
import com.chelseasystems.rb.EMVDeclineRDO;
import com.chelseasystems.rb.ReceiptFactory;
import com.emobilepos.ivu.IVU;
import com.emobilepos.ivu.models.TransactionRequest;
import com.emobilepos.ivu.models.TransactionResponse;
import com.emobilepos.ivu.models.TransactionRequest.CARDTYPE;
import com.emobilepos.ivu.models.TransactionRequest.TRANSTYPE;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFDecodeParam;
/**
 */
public class PaymentApplet extends CMSApplet {
	static final long serialVersionUID = 3656968098151140700L;

	static boolean isVatEnabled = new ConfigMgr("vat.cfg").getString("VAT_ENABLED").equalsIgnoreCase("TRUE");
	// instance members used to prompt user with an initial payment
	private String INITIAL_PAYMENT;
	private Payment anInitialPayment;
	//Vishal Yevale : created new property to avoid Bug 28872 Double charge Issues (void,sale,refund) 
	private boolean isTotalAmountTendered = false;
	private Payment thePayment;
	// Vishal Yevale : for manual flow of gc to check card nu. and entered amount  17 March 2017
	private Payment gcPayment;
	private String strUSECARD;
	private boolean shouldTxnPostWhenBalanceIsZero;
	private CMSPaymentTransactionAppModel theTxn = null;
	private boolean changeDue = false;
	private boolean cancelTxnPressed = false;
	private boolean prevPressed = false;
	//vishal yevale : RedeemableBuyBack void and cashout request : 5 Dec 2016
	private boolean reedBuyBackSuccess = false;
	//end 5 Dec 2016 Vishal Yevale
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
	String storeState = null;
	String storeCountry = null;
	//Ruchi #27968
	boolean amountdueCheck = false;
	boolean showRefundOption = 	false;
	private boolean isMobileTerminal = false;
    /*Vivek Mishra : Added for capturing AJB response*/
	private String[] ajbResponse = null;
    //End
	/*Vivek Mishra : Added for Credit force AJB response*/
	private boolean isForced = false;
    //End
	//Initialize the applet
	//Anjana added the variable to return to tender list from payment screen 
	private boolean returntotender = false;
	//To show 3 buttons when communication is down
	private CommunicationErrorDlg communicationErrorDlg;
	//To post transcation after manual override
	private boolean isManualOverride = false;
	//Mayuri Edhara :: added check to not do manual auth for mobile terminal flow.
	//private boolean isMobileManualAuthRequired = true;
	//Mayuri Edhara :: to show menu for refund with return from menu constants
	private boolean showMenuForRefundWithReturn = false;
	//Anjana added this not to send SAF message if it is already safed
	boolean alreadySAFed =false;
	private boolean isRefundPaymentRequired = false;
	private boolean cardDecline = false;
	private boolean isManualOverrideCheck = false;
	private boolean manualOnCommunicationError = false;
	private boolean authorize = true;
	private boolean donotPost = false;
	private boolean offlineMode = false;
	private boolean doNotValidate = false;
	private boolean doofflineOverride = false;
	//Vivek Mishra : Added for capturing first 6 and last 4 digits of the card in AJB offline scenario.
	private String firstSixOrEight="";
	private String lastfour="";
	private boolean fiPayOffline = false;
	String [] responseArrayinAuthorize =null;
	//Ends
	//Vivek Mishra : Added for showing Credit Card types to choose in AJB offline mode
	private GenericChooseFromTableDlg overRideDlg;
	private String paymentType = null;
	private String paymentDesc = null;
	//Ends
	//vishal for double (confirmation)authcode 19 oct 2016
		private boolean authorizeConfirm=true;
		private String firstAuthCode="";
		//end vishal
	//boolean to update the amount on Verifone device when different tender type is used
	boolean Refresh =false;
	boolean idleMessage = false;
	boolean clearMessage = false;
	boolean fromPayment = false;
	boolean isManualkeyEntry  = false;
    //Added changes for new Return flow.
	public static  CMSCompositePOSTransaction OrgSaleTxn;
	public CMSEmployee theApprover;
	String totalTransactionAmount;
	//protected boolean isPasswordRequired;
	protected boolean isPasswordEntered;
	protected int passwordWrongCount;
	public static boolean overrideReturn = false;
	public static ArmCurrency amtOverridden = null;
	//Vivek Mishra : Added for Mobile terminal return flow
	public int delRow = -1;
	private Payment delPay = null;
	public static ArmCurrency retAmt = new ArmCurrency(0.0d);
	//Ends
	public boolean  isGiftCardReturn = false;
	public static int rownum = -1;
	//Vivek Mishra : Added for Signature printing
	BufferedImage signImg = null;
	//End
	public ArmCurrency reqAmt = null;
	private static ConfigMgr fipayConfig;
	private String fipay_flag;
	private String fipayGiftcardFlag;
	private boolean isMobileTerminalRefund = false;
	private boolean mailCheckFlag = false;
	
	private CMSCompositePOSTransaction compositePOSTransaction; //Added by Himani
	//Vivek Mishra : Added to generate new RDO for EMV decline receipt
	private EMVDeclineRDO emvDecRdo = new  EMVDeclineRDO();
	
	/*Added to log info and errors.*/
	private static Logger logger = Logger.getLogger(PaymentApplet.class.getName());
	
	public void init() {
		try {
			ConfigMgr mgr = new ConfigMgr("payment.cfg");
			INITIAL_PAYMENT = mgr.getString("INITIAL_PAYMENT");
			if (INITIAL_PAYMENT != null && INITIAL_PAYMENT.trim().length() > 0) {
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
		pnlCards = new JPanel();
		card = new CardLayout();
		pnlCards.setLayout(card);
		pnlCards.add(pnlPayments, "payments");
		pnlCenter.add(pnlCards, BorderLayout.CENTER);
		pnlSouth.add(pnlTendered, null);
		pnlTendered.add(lblAmt, null);
		pnlTendered.add(AmtTnd$Lbl, null);
		pnlSouth.add(pnlDue, null);
		pnlDue.add(lblDue, null);
		pnlDue.add(AmtDue$Lbl, null);
		pnlDue.setOpaque(false);
		pnlTendered.setOpaque(false);
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
		// Just to be safe, resetting the flag to false everytime Applet is started
		
	 	String fileName = "store_custom.cfg";
	 	fipayConfig = new ConfigMgr(fileName);
		fipay_flag = fipayConfig.getString("FIPAY_Integration");
		
		 //Default value of the flag is Y if its not present in credit_auth.cfg
		if (fipay_flag == null) {
			fipay_flag = "Y";
		}

		fipayGiftcardFlag = fipayConfig.getString("FIPAY_GIFTCARD_INTEGRATION");

		 //Default value of the flag is N if its not present in store_custom.cfg
		if (fipayGiftcardFlag == null) {
			fipayGiftcardFlag = "N";
		}
		
		postingInProgress = false;
		boolean customerAdded = false;
		offlineMode = false;
		doNotValidate = false;
		isManualOverride = false;
		isMobileTerminal = false;
		isManualkeyEntry  = false;
		isMobileTerminalRefund = false;
		OrgSaleTxn = null;
		//Vishal Yevale : initialized new property to avoid Bug 28872 Double charge Issues (void,sale,refund) 
		isTotalAmountTendered = false;
		//Vivek Mishra : Added for Signature printing
		//signImg = null;
		//Ends
		PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
		/*
		 * Added this method which corrects the transaction discount Array and re- arranges its sequence.
		 * Also corrects the settlement discount sequence
		 */
		try{
			correctDiscountsAndSequences(theTxnPOS);
		}catch (Exception e){
			e.printStackTrace();
		}
		CMSCustomer txnCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
		if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
			try {
				 txnCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
				theAppMgr.removeStateObject("TXN_CUSTOMER");
				
				if (theTxnPOS instanceof CMSCompositePOSTransaction)
					((CMSCompositePOSTransaction) theTxnPOS).setCustomer(txnCust);
				//Redeemable Buyback Transaction
				if (theTxnPOS instanceof CMSRedeemableBuyBackTransaction) {
					((CMSRedeemableBuyBackTransaction) theTxnPOS).setCustomer(txnCust);
				}
				//Paid out transaction 
				if (theTxnPOS instanceof CMSMiscPaidOut) {
					((CMSMiscPaidOut) theTxnPOS).setCustomer(txnCust);
				}
				//Paid in transaction 
				if (theTxnPOS instanceof CMSMiscCollection) {
					((CMSMiscCollection) theTxnPOS).setCustomer(txnCust);
				}
				customerAdded = true;
				LightPoleDisplay.getInstance().displayMessage(txnCust.getFirstName(), txnCust.getLastName());
			} catch (Exception e) {
				theAppMgr.removeStateObject("TXN_CUSTOMER");
			}
		}
//for issue #1915 employee sale threshold for US
		if (theTxnPOS instanceof CMSCompositePOSTransaction) {
					 txnCust = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
					 if(txnCust!=null){
						String customerType = txnCust.getCustomerType();
						if (customerType != null && customerType.equals(CustomerSearchString.CUSTOMER_TYPE_EMPLOYEE_CODE)) {
						  try {
							  	((CMSCompositePOSTransaction) theTxnPOS).validateEmployeeRuleForCurrentTransaction(txnCust);
						  } catch (BusinessRuleException bre) {
							 if (bre.getMessage() != null && bre.getMessage().length() > 0) {
								  if (!theAppMgr.showOptionDlg("Employee Alert", bre.getMessage())) {
							     	  	SwingUtilities.invokeLater(new Runnable() {
										public void run() {
										theAppMgr.fireButtonEvent("PREV");
										}
									  	});
									  	return;
									}
							  }
						}
						}
						}
			try {
				TaxUtilities.applyTax(theAppMgr, (CMSCompositePOSTransaction) theTxnPOS, (CMSStore) theTxnPOS.getStore(), (CMSStore) theTxnPOS.getStore(), theTxnPOS.getProcessDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LightPoleDisplay.getInstance().stopDefaultDisplay();
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		cfgMgr = new ConfigMgr("payment.cfg");
		isAutoChange = new Boolean(cfgMgr.getString("AUTO_CHANGE")).booleanValue();
		changeDue = false;
		isPostComplete = false;
		cancelTxnPressed = false;
		isBackFromCustomerApplet = false;
		prevPressed = false;
		model.clear();	
		//mayuri edhara fix for Change due name
		model.setNoShowChangeDueName(false);
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
		}
		//Seed the model with the payments...
		Payment[] payments = theTxn.getPaymentsArray();
		for (int idx = 0; idx < payments.length; idx++) {
			model.addPayment(payments[idx]);
			model.fireTableDataChanged();
		}
		model.firstPage();
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
		// Start Issue # 996
		strUSECARD = (String) theAppMgr.getStateObject("USE_CARD_ON_FILE");
		// End Issue # 996
		//Added changes for new Return flow.
		//Mayuri Edhara :: moved the original sale transaction to the below method.
		if(theTxnPOS!=null && !(theTxnPOS instanceof CMSRedeemableBuyBackTransaction)){
		OrgSaleTxn = getOriginalTransaction(theTxnPOS);
		}
		if(OrgSaleTxn!=null){
			Payment[] orgPayments = OrgSaleTxn.getPaymentsArray();
		if(orgPayments.length == 0){
			showMenuForRefundWithReturn = false;
				theAppMgr.showErrorDlg(res.getString("No Payments available for the original transaction ; please proceeed with No Recipt Return"));		
				SwingUtilities.invokeLater(new Runnable() {
				public void run() {
						theAppMgr.fireButtonEvent("CANCEL_TXN");
						//isPostComplete = true; // needed to fake out isHomeAllowed()
						theAppMgr.setHomeEnabled(true);
					}
				 	});
			}
		showMenuForRefundWithReturn = true;
		//Vishal Yevale : done changes for giftcard return transaction approval status.
			for (int idx = 0; idx < orgPayments.length; idx++) {
				if(orgPayments[idx] instanceof CreditCard){
					((CreditCard)orgPayments[idx]).setRespStatusCode("99");
				}else if(orgPayments[idx] instanceof CMSBankCheck)
					((CMSBankCheck)orgPayments[idx]).setRespStatusCode("99");
				else if(orgPayments[idx] instanceof CMSStoreValueCard && fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y"))
					((CMSStoreValueCard)orgPayments[idx]).setRespStatusCode("99");
				else if(orgPayments[idx] instanceof CMSDueBill && fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y"))
					((CMSDueBill)orgPayments[idx]).setRespStatusCode("99");
				else if(orgPayments[idx] instanceof CMSDueBillIssue && fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y"))
					((CMSDueBillIssue)orgPayments[idx]).setRespStatusCode("99");
				else if(orgPayments[idx] instanceof Cash && ((Cash)orgPayments[idx]).getPaymentCode()==null)
					continue;
				else if(orgPayments[idx] instanceof Cash){
					orgPayments[idx].setRespMessage("99");
				}
				//end vishal
				model.addPayment(orgPayments[idx]);
				//Mayuri Edhara fix for Change due name
				model.setNoShowChangeDueName(true);
				model.fireTableDataChanged();
			}
			updateLabels();
			tblPayment.repaint();
			//Vivek Mishra : Added to select first row in case of refund.
			selectRow(0);
			//Ends
		}
		
		//Ends
		selectPayment();
		updateLabels();
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
	
		) {
			objectEvent("PAYMENT", custNeededPaymentObj);
		}
		theAppMgr.removeStateObject("CUST_NEEDED_PAYMENT");
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
		// Issue # 861
		// Added the Loyalty
		CMSCompositePOSTransaction posTran = null;
		if (theTxn instanceof CMSCompositePOSTransactionAppModel) {
			posTran = (CMSCompositePOSTransaction) theTxn.getPaymentTransaction();
			if ((posTran.getCustomer() != null && posTran.getLoyaltyCard() == null)
					|| (posTran.getCustomer() != null && posTran.getLoyaltyCard() != null && !posTran.getLoyaltyCard().getCustomer().equals(theTxn.getCustomer()))) {
				// Set the default loyalty card
				setCustomerLoyalty(posTran);
			}
			if (theTxnPOS instanceof CMSCompositePOSTransaction) {
				try {
					TaxUtilities.applyTax(theAppMgr, (CMSCompositePOSTransaction) theTxnPOS, (CMSStore) theTxnPOS.getStore(), (CMSStore) theTxnPOS.getStore(), theTxnPOS.getProcessDate());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		System.out.println("In the start DEBUG-PROD-ISSUE isPostComplete in start method of payment applet::::  "+isPostComplete);
		


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
		//to delete the org txn from payment applet during return
		OrgSaleTxn = null;
		retAmt = new ArmCurrency(0.0d);
		DueBillIssueBldr.gcAmtTnd = new ArmCurrency(0.0d);
        delRow = -1;
		delPay = null;
		rownum = -1;
		showMenuForRefundWithReturn = false;
		if ((new ConfigMgr("JPOS_peripherals.cfg").getString("MSR_DEVICE_TYPE")).equals("INGENICO_JPOS")) {
			ET1000JPOSForm.getInstance().displayLogo();
		}
		if (!isPostComplete) {
			if (!prevPressed) {
				//System.out.println("calling app model to print cancel receipt");
				theTxn.printCancelReceipt(theAppMgr);
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
		return ("$Revision: 1.4 $");
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
		
		//Vivek Mishra : Added to show correct amount due in case of presale transaction.
	
		if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
				&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
			TotalAmt$Lbl.setText(((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().formattedStringValue());
			SubTot$Lbl.setText(((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getRetailAmount().formattedStringValue());
			TranDisc$Lbl.setText(((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getReductionAmount().formattedStringValue());
		}
		//Ends
		else{
		TotalAmt$Lbl.setText(theTxn.getCompositeTotalAmountDue().formattedStringValue());
		SubTot$Lbl.setText(theTxn.getCompositeRetailAmount().formattedStringValue());
		TranDisc$Lbl.setText(theTxn.getCompositeReductionAmount().formattedStringValue());
		}
		// special regional tax code
		PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
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
	//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
	ArmCurrency amtDue = new ArmCurrency(0.0d);
	if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
			&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
		amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
		AmtDue$Lbl.setText(amtDue.formattedStringValue());
	}
	
	//Ends
	else{
		AmtDue$Lbl.setText(theTxn.getCompositeTotalAmountDue().formattedStringValue());
	}
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
			} else if (theTxn.getPaymentTransaction() instanceof CMSRedeemableBuyBackTransaction) {
				pnlHeader.setProperties((CMSCustomer) ((CMSRedeemableBuyBackTransaction) theTxn.getPaymentTransaction()).getCustomer(), (CMSEmployee) theOpr, null);
			} else {
				pnlHeader.setProperties((CMSCustomer) theTxn.getCustomer(), (CMSEmployee) theOpr, (CMSEmployee) theTxn.getConsultant(), (CMSStore) theStore, theTxn.getTaxExemptId(),
						theTxn.getRegionalTaxExemptId());
			}
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	/**
	 * put your documentation comment here
	 * @param Command
	 * @param Value
	 */
	public void editAreaEvent(String Command, ArmCurrency Value) {
		if (Command.equals("AMOUNT")) {
			if(!(thePayment == null)){
			String paymentType = thePayment.getTransactionPaymentName();
			String paymentTypeView = thePayment.getGUIPaymentName();
			if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView, Value)) {
				thePayment.setAmount(Value);
				String storeId = ((CMSStore) theAppMgr.getGlobalObject("STORE")).getId();
				String registerId = ((CMSRegister) theAppMgr.getGlobalObject("REGISTER")).getId();
				//Anjana commenting the journal key as it has to be set from AJB response
				//thePayment.setJournalKey(CreditAuthUtil.getJournalKey(storeId, registerId));
				objectEvent("PAYMENT", thePayment);
				thePayment = null;
			
				}
			}else{
				theAppMgr.showErrorDlg(res.getString("Please select Payment Type."));
			}
				
		}
		
		if (Command.equals("AMOUNT_MANOVERRIDE")) {
			//Vivek Mishra : Added to avoid manual authorization skip issue.
			offlineMode = true;
			Payment payment = null;
			ArmCurrency amtVal= null;
			   try {
				amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
				//Vivek Mishra : Added isMobileTerminalRefund to make mobile terminal an independent teder in case of Refund just like cash 13-JUN-2016
				if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0)) && !isMobileTerminalRefund){//Ends here
					int row2 = tblPayment.getSelectedRow();
					payment = model.getPayment(row2);
				if(Value.greaterThan(payment.getAmount().absoluteValue())){
					theAppMgr.showErrorDlg(res.getString("Cannot refund more than tendered amount"));
					theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
							, "", theAppMgr.CURRENCY_MASK);	
				return;
				}
				if(Value.lessThan(payment.getAmount().absoluteValue())){
					theAppMgr.showErrorDlg(res.getString("Cannot refund less than tendered amount"));
					theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
							, "", theAppMgr.CURRENCY_MASK);	
					return;
				}
				((CreditCard)payment).setReturnWithReceipt(true);
				}
				isMobileTerminalRefund =  false;
			} catch (CurrencyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/* Vishal Yevale : Giftcard manual payment flow - for manual authorizatin code && 16 March 2017 : set auth to true and added check for 
			validating entered amount with balance amount.
			 */
			   try{
			   if(thePayment instanceof CMSStoreValueCard){
				  authorize=false;
				  ((CMSStoreValueCard)thePayment).setAuthRequired(true);
				 if(Value.greaterThan(((CMSStoreValueCard)gcPayment).getGiftcardBalance())){
					  theAppMgr.showErrorDlg(res.getString("Cannot tender more amount than balance amount"));
						theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
								, "", theAppMgr.CURRENCY_MASK);	
						return;
				  }
			   }else if(thePayment instanceof CMSDueBill){
					  authorize=false;
					  ((CMSDueBill)thePayment).setAuthRequired(true);
					  if(Value.greaterThan(((CMSDueBill)gcPayment).getGiftcardBalance())){
						  theAppMgr.showErrorDlg(res.getString("Cannot tender more amount than balance amount"));
							theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
									, "", theAppMgr.CURRENCY_MASK);	
							return;
					  }
				   }
			   } catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//Ends
			//Vivek Mishra : Added for displaying Default call center number in case of AJB offline scenario.
			   //Mayuri Edhara : Removing the dlg as manual authorization is not required for mobile terminal flow.
			   //if(isMobileManualAuthRequired){
				   theAppMgr.showErrorDlg(res.getString("Please press Manual Auth button and enter the authorization code."));
			  // }
			//Ends
			String paymentType = thePayment.getTransactionPaymentName();
			String paymentTypeView = thePayment.getGUIPaymentName();
			
			   //Anjana: added to remove the card from org Sale txn when returning offline 
			try {
				if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0))){
					Payment[] orgPayments = OrgSaleTxn.getPaymentsArray();
					for (int idx = 0; idx < orgPayments.length; idx++) {
						if(orgPayments[idx] instanceof CreditCard){
						if(!(orgPayments[idx].getRespStatusCode().equals("0"))){
							if((orgPayments[idx].getRespStatusCode().equals("99")) && ((CreditCard)orgPayments[idx]).isReturnWithReceipt()){
							model.removeRowInModel(orgPayments[idx]);
							((CreditCard)orgPayments[idx]).setReturnWithReceipt(false);
							}}
						}}}
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView, Value)) {
				thePayment.setAmount(Value);
				String storeId = ((CMSStore) theAppMgr.getGlobalObject("STORE")).getId();
				String registerId = ((CMSRegister) theAppMgr.getGlobalObject("REGISTER")).getId();
				//thePayment.setJournalKey(CreditAuthUtil.getJournalKey(storeId, registerId));
				objectEvent("PAYMENT_OFFLINE", thePayment);
				thePayment = null;
				offlineMode = false;
				selectOption();
				selectLastRow();
				}//Vivek Mishra : Added to fix MObile Terminal over tender issue 10-JUN-2016
			else
			{
				//isMobileManualAuthRequired = true;
				offlineMode = false;
				returnToTenderList();
				return;
			}//Ends Here
			//Mayuri Edhara :: 05/10/17 : commented the below changes for MT since we need manual auth approval. 
			// Mayuri Edhara :: added to post the transaction for Mobile Terminal without manual authorization approval.
		/*	if(!isMobileManualAuthRequired){ 
				Payment[] payments = theTxn.getPaymentsArray();

				for (int i = 0; i < payments.length; i++) {

					// Added to set card holder name while taking first 6 and
					// last 4 credit card digit entry when using Mobile terminal
					// 22-APR-2016
					if (payments[i] instanceof CreditCard) {
						CreditCard cc = (CreditCard) payments[i];
						if (cc.getExpirationDate() == null) {
							cc.setCreditCardHolderName("Mobile");
							cc.setTenderType(cc.getGUIPaymentName());
						}
					}
				}
				this.editAreaEvent("MANUAL_OFFLINE", "");
				isMobileManualAuthRequired = true;
				selectPayment();
				

			}*/
		}
		
	}

	/**
	 * @param Command
	 * @param Value
	 */
	public void editAreaEvent(String Command, String Value) {

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
		   if (Command.equals("MANUAL_OFFLINE")) {
			   try{
				int row = tblPayment.getSelectedRow();
				if (row < 0) {
					theAppMgr.showErrorDlg(res.getString("You must first select a payment."));
					selectOption();
					return;
				}
				Payment pay = model.getPayment(row);
				ArmCurrency amtVal= null;
			try {
				if(pay instanceof CreditCard){
					amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
					if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0))){
						
								if((pay.getRespStatusCode().equals("99")) && !(((CreditCard)pay).isReturnWithReceipt())){
									theAppMgr.showErrorDlg(res.getString("Refund is not initiated for this payment line"));
									return;
								}
							if((pay.getRespStatusCode().equals("0")) && ((CreditCard)pay).isReturnWithReceipt()){
									theAppMgr.showErrorDlg(res.getString("This credit card has already been authorized."));
									((CreditCard)pay).setReturnWithReceipt(false);
									return;
								}
							
							}}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (pay instanceof CreditCard) {
					CreditCard cc = (CreditCard) pay;
					
					try {
						if (!cc.isAuthRequired() && !(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0)))) {
							theAppMgr.showErrorDlg(res.getString("This credit card has already been authorized."));
							selectOption();
							//Vivek Mishra : Added for restricting the flow to go into MANUAL instead of MANUAL_OFFLINE which was causing fatal exception.
							doofflineOverride=true;
							//Ends
							return;
						}
						
					} catch (CurrencyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//End
					cc.setManualOverride(Value);
					//Vivek Mishra : Commented because second card was getting added without any authorization in case of split tender in AJB offline scenario.  
			
					Payment[] payments = theTxn.getPaymentsArray();
					int len = payments.length;
					//vishal credit card auth changes 19 oct 2016
					//Anjana: FEB 13 Added mobile terminal check to not show manual auth edit area 
					//Anjana commented on 04/2017 for new PCR to add manual Auth for mobile terminal
				//	if((cc.getCreditCardHolderName()!=null && !(cc.getCreditCardHolderName().equals("Mobile")))){
						if(authorizeConfirm){
							cc.setAuthRequired(true);
							firstAuthCode=Value;
							theAppMgr.setSingleEditArea(res.getString("Enter authorization number to confirm."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);
							theAppMgr.setEditAreaFocus();
							authorizeConfirm=false;
							return;
						}else {
							//Anjana added on 04/2017 for new PCR to add manual Auth for mobile terminal
							if(Value.length() > 8){
								theAppMgr.showErrorDlg(res.getString("Authorization code cannot be greater than 8 characters"));
								cc.setAuthRequired(true);
								authorize=false;
								authorizeConfirm=true;
								firstAuthCode="";
								theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);
								theAppMgr.setEditAreaFocus();
								return;
							}
							if(!(Value.matches("[A-Za-z0-9]+"))){
								theAppMgr.showErrorDlg(res.getString("Authorization code can only be alpha numeric"));
								cc.setAuthRequired(true);
								authorize=false;
								authorizeConfirm=true;
								firstAuthCode="";
								theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);
								theAppMgr.setEditAreaFocus();
								return;
							}//ends
							if(firstAuthCode.equalsIgnoreCase(Value)){
								authorizeConfirm=true;
								firstAuthCode="";
								authorize=true;
								model.fireTableDataChanged();
								selectOption();
								//Anjana added may1 2017 to do auto post for mobile tender
								selectPayment();
								return;
							}else{
								theAppMgr.showErrorDlg(res.getString("Auth Code are not matching please Enter again"));
								cc.setAuthRequired(true);
								authorize=false;
								authorizeConfirm=true;
								firstAuthCode="";
								theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);
								theAppMgr.setEditAreaFocus();
								return;
							}
					
						//end vishal credit card auth changes 19 oct 2016
						//for (int x = 0; x < payments.length; x++) {
	
						//Vivek Mishra : Added for sending force request to AJB. Even the authorization gets failed the AuthRequired flag gets changed to false.
						//So to send the force request required to set true explicitly.
						//Vivek Mishra : Added for sending force request to AJB
						//Anjana commenting the auth required true to prevent cashier to do multiple manual auths
						//cc.setAuthRequired(true);
						//Anjana added  as we dont need to auth while manual
	
						//End
						//model.fireTableDataChanged();
						//selectOption();  vishal commented three lines 19 oct 2016
						//return;
						}
				//	Anjana commented on 04/2017 for new PCR to add manual Auth for mobile terminal
				/*	}
					selectOption();
					return;*/
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
					isManualOverrideCheck = true;
					model.fireTableDataChanged();
					selectOption();
					return;
				}
				//  Vishal Yevale : Giftcard manual payment flow 28 sept 2016
				 if(pay instanceof CMSStoreValueCard || pay instanceof CMSDueBill){

					if(authorizeConfirm){
						if(pay instanceof CMSStoreValueCard){
							CMSStoreValueCard cmsStoreValueCard=(CMSStoreValueCard) pay;
							if(!pay.isAuthRequired()){
								theAppMgr.showErrorDlg(res.getString("The payment is already Authorized !!"));
								return;
							}
						}
						if(pay instanceof CMSDueBill){
							CMSDueBill cmsDueBill=(CMSDueBill) pay;
							if(!pay.isAuthRequired()){
								theAppMgr.showErrorDlg(res.getString("The payment is already Authorized !!"));
								return;
							}
						}
						Payment[] payments = theTxn.getPaymentsArray();
						//isMobileManualAuthRequired=false;
						firstAuthCode=Value;
						theAppMgr.setSingleEditArea(res.getString("Enter authorization number to confirm."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);
						theAppMgr.setEditAreaFocus();
						authorizeConfirm=false;
						return;
				
					}
					else{
						if(firstAuthCode.equalsIgnoreCase(Value)){
							authorizeConfirm=true;
							firstAuthCode="";
							authorize=true;
							if(pay instanceof CMSStoreValueCard){
								CMSStoreValueCard cmsStoreValueCard=(CMSStoreValueCard) pay;
								cmsStoreValueCard.setManualAuthCode(Value);
								cmsStoreValueCard.setAuthRequired(false);
							}
							if(pay instanceof CMSDueBill){
								CMSDueBill cmsDueBill=(CMSDueBill) pay;
								cmsDueBill.setManualAuthCode(Value);
								cmsDueBill.setAuthRequired(false);
							}
							model.fireTableDataChanged();
							selectOption();
							selectPayment();
							return;
						}else{
							theAppMgr.showErrorDlg(res.getString("Auth Code are not matching please Enter again"));
							if(pay instanceof CMSStoreValueCard){
								CMSStoreValueCard cmsStoreValueCard=(CMSStoreValueCard) pay;
								cmsStoreValueCard.setAuthRequired(true);
							}
							if(pay instanceof CMSDueBill){
								CMSDueBill cmsDueBill=(CMSDueBill) pay;
								cmsDueBill.setAuthRequired(true);
							}
							authorize=false;
							authorizeConfirm=true;
							firstAuthCode="";
						theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);
						theAppMgr.setEditAreaFocus();
						return;
						}
				}
					}
				//end Vishal Yevale
				// only the above to type are valid, show error dlg if
				// here
				theAppMgr.showErrorDlg(res.getString("The payment does not require a manual override."));
				selectOption();
		   }
		   //Vivek Mishra : Added for showing tender options after manual authorization if amount due is not 0.
		   finally{
			   if(authorizeConfirm && authorize){
			   ArmCurrency amt;
			   Payment[] payments = theTxn.getPaymentsArray();
			   boolean leftToAuth = false;
			   for(Payment payment : payments)
			   {
				  
				   if(payment.isAuthRequired() && !(payment instanceof CMSDebitCard))
				   {
					   leftToAuth = true;
					   break;
				   }
			   }
				try {
					//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
					ArmCurrency amtDue = new ArmCurrency(0.0d);
					if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
							&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
						amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
						amt = amtDue.subtract(theTxn.getTotalPaymentAmount());
					}
					//Ends
					else
					amt = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
				if( (OrgSaleTxn!=null && amt.lessThan(new ArmCurrency(0.0)))){
					offlineMode = false;
					selectPayment();
				}
				else if (!leftToAuth && !(amt.equals(new ArmCurrency(0.0))) )
					{
						updateLabels();
						tblPayment.repaint();
						initButtons(CMSPaymentMgr.PAYMENT);
						selectPayment();
						manualOnCommunicationError=false;
					}
				} catch (CurrencyException e) {
					e.printStackTrace();
				}
			   }else{
				   
			   }
		   }
		   //Ends
		   }
	   if (Command.equals("MANUAL")) {
		   try{
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
				//Vivek Mishra : Added check for Debit Cards as debit cards should not be eligible for Manual Authorization.Discussed in the AJB status call on 13 May 2015.
				else if (pay instanceof DebitCard)
				{
					theAppMgr.showErrorDlg(res.getString("Debit Cards are not eligible for manual authorization."));
					//Vivek Mishra : Added to delete the unauthorized debit card tender.
					logger.info("DEBUG-PROD-ISSUE:::: deletePayment() from EditArea Event Command::::"+Command);
					deletePayment();
					logger.info("DEBUG-PROD-ISSUE:::: deleted payment from Manual Flow ::"+pay.getGUIPaymentName());

					selectOption();
					return;
				}
				//End
				isManualOverride = true;
				isForced = true;
				
				//Anjana added  as we dont need to auth while manual
				if(!isManualOverride && !isManualOverrideCheck && !offlineMode){
				authorize();
				}
				//End
				//model.fireTableDataChanged(); vishal commented 19 oct 2016 
				//selectOption();
				//return;
				//vishal authcode for creditcardPayment 19 oct 2016
				if(authorizeConfirm){
					//Mayuri Edhara :: added the length check for bank approval code :: 20 OCT 2016
					if(Value.length() > 8){
						theAppMgr.showErrorDlg(res.getString("Authorization code cannot be greater than 8 characters"));
						cc.setAuthRequired(true);
						authorize=false;
						authorizeConfirm=true;
						firstAuthCode="";
						theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL", theAppMgr.REQUIRED_MASK);
						theAppMgr.setEditAreaFocus();
						return;
					} else {
						firstAuthCode=Value;
						cc.setAuthRequired(true);
						theAppMgr.setSingleEditArea(res.getString("Re-Enter authorization number to confirm."), "MANUAL", theAppMgr.REQUIRED_MASK);
						theAppMgr.setEditAreaFocus();
						authorizeConfirm=false;
						return;
					}
				}else{
					//Mayuri Edhara :: added the lenght check for bank approval code :: 20 OCT 2016
					if(Value.length() > 8){
						theAppMgr.showErrorDlg(res.getString("Authorization code cannot be greater than 8 characters"));
						cc.setAuthRequired(true);
						authorize=false;
						authorizeConfirm=false;
						theAppMgr.setSingleEditArea(res.getString("Re-Enter authorization number to confirm."), "MANUAL", theAppMgr.REQUIRED_MASK);
						theAppMgr.setEditAreaFocus();
						return;
					} else {
						if(firstAuthCode.equalsIgnoreCase(Value)){
							authorizeConfirm=true;
							firstAuthCode="";
							authorize=true;
							cc.setManualOverride(Value);
							if(pay instanceof CreditCard && pay.getRespStatusCode().equals("0") && !offlineMode){
								((CreditCard)pay).setCreditCardHolderName("");
								sendSAFRequest(pay);
								showSignature(pay);
							}
							model.fireTableDataChanged();
							selectOption();
							return;
						}else{
							theAppMgr.showErrorDlg(res.getString("Authorization codes are not matching. Please enter again."));
							cc.setAuthRequired(true);
							authorize=false;
							authorizeConfirm=true;
							firstAuthCode="";
						theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL", theAppMgr.REQUIRED_MASK);
						theAppMgr.setEditAreaFocus();
						return;
						}
					}
				}
				//end vishal authcode 19 oct 2016
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
				//Vivek Mishra : Added the code to allow Bank Checks to bypass fipay as requested by Jason on 02-NOV-2015. This code needs to be commented if they need request in future.
				chk.setRespStatusCode("0");
				authorize=true;
				//Ends
				isManualOverrideCheck = true;
				model.fireTableDataChanged();
				selectOption();
				return;
			}
			// only the above to type are valid, show error dlg if
			// here
			theAppMgr.showErrorDlg(res.getString("The payment does not require a manual override."));
			selectOption();
	   }
	   //Vivek Mishra : Added for showing tender options after manual authorization if amount due is not 0.
	   finally{
		   
		   ArmCurrency amt;
		   Payment[] payments = theTxn.getPaymentsArray();
		   //vishal added if condition (credit card auth changes) 19 oct 2016
		   if(!(payments[payments.length-1] instanceof CreditCard) ||authorizeConfirm && authorize){
		   boolean leftToAuth = false;
		   for(Payment payment : payments)
		   {
			   if(payment.isAuthRequired() && !(payment instanceof CMSDebitCard))
			   {
				   leftToAuth = true;
				   break;
			   }
		   }
			try {
				//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				ArmCurrency amtDue = new ArmCurrency(0.0d);
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction ) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
					amt = amtDue.subtract(theTxn.getTotalPaymentAmount());
				}
				//Ends
				else
				amt = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
				if( (OrgSaleTxn!=null && amt.lessThan(new ArmCurrency(0.0)))){
					offlineMode = false;
					selectPayment();
				}
				else if (!leftToAuth && !(amt.equals(new ArmCurrency(0.0))))
				{
					updateLabels();
					tblPayment.repaint();
					initButtons(CMSPaymentMgr.PAYMENT);
					selectPayment();
					manualOnCommunicationError=false;
					//Anjana Feb 13:- flag is N and mobile plus check tender option was not showing when amt is due
					return;
				}//Mayuri Edhara :: added to auto post the manual auth after entering approval code : 20 OCT 2016
				else if(!leftToAuth && (amt.equals(new ArmCurrency(0.0)))){
					selectPayment();
				}
			} catch (CurrencyException e) {
				e.printStackTrace();
			}
		   }
	   }
	   //Ends
	   }
	   //Added to implement AJB Offline with POS
	   else if (Command.equals("OFFLINE_CARD")) {
			 //  Vishal Yevale : Giftcard manual payment flow 
		   if(paymentDesc.equals("CMSSTOREVALUECARD")){
			   String validateValue = "";
			   String gfAccNo=Value;
			   ArmCurrency amtVal= null;
			   try {
				   amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   /**
			    *  Vishal Yevale :: added to get the card number from the Edit area Event. 16 March 2017
			    *  Enter the GIFT CARD ID : should be same as swiped giftcard Id
			    **/
			   if(!(Value!=null && gcPayment!=null && ((Redeemable)gcPayment).getId().equals(Value))){
					 theAppMgr.showErrorDlg(res.getString("Please enter giftcard number you swiped."));
					 theAppMgr.setSingleEditArea(res.getString(
			            "Enter 16 digits of account number."), "OFFLINE_CARD"
			            , "");
				 theAppMgr.setEditAreaFocus();
				 return;
				 } else {
					 thePayment = gcPayment;
				 }
			   		Payment payment = null;
					/*commented this as we should not get an instance of duebillIssue. This is only used incase of creditnote issue.
					 * In the payment flow duebillissue / credit note issue should be considered as duebill/ credit note. 
					 * thePayment = ValidateCardType.allocGiftCardPayment(gfAccNo,theAppMgr);
					 if(thePayment==null)
				        {
				        	theAppMgr.showErrorDlg(res.getString("The account number is invalid."));
				        	returnToOfflineTenderList();
						    return;
				        } */
					 
						paymentDesc=((thePayment.getClass()).getSimpleName()).toUpperCase();
						
						PaymentTransaction txn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
				        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
				        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)txn.getAppModel(
				            CMSAppModelFactory.getInstance(), theAppMgr);
				        ArmCurrency amt = null;
						try {
							//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario
							if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
									&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
								amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.
									    getTotalPaymentAmount());
							}
							//Ends
							else
							amt = appModel.getCompositeTotalAmountDue().subtract(appModel.
							    getTotalPaymentAmount());
						} catch (CurrencyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						doNotValidate = true;
						   try {
								if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0)) && !isMobileTerminalRefund){//Ends here
									doofflineOverride = true;
									int row2 = tblPayment.getSelectedRow();
									payment = model.getPayment(row2);
									theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
											, payment.getAmount().absoluteValue(), theAppMgr.CURRENCY_MASK);
								}
								else{
										theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
								, amt.absoluteValue(), theAppMgr.CURRENCY_MASK);}
								
							} catch (CurrencyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        theAppMgr.setEditAreaFocus();
				        theAppMgr.showMenu(MenuConst.POST_AUTH, theOpr, theAppMgr.PREV_BUTTON);
				        authorize = false;
				        //selectLastRow();
				       return;
		 }			 //end Vishal Yevale 28 sept 2016
		   else{
		   String validateValue = "";
		   ArmCurrency amtVal= null;
		   try {
			   amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 //Vivek Mishra : Added for capturing first 6 digits in AJB offline scenario.
		 //Vivek Mishra : Added for showing Credit Card types to choose in AJB offline mode
		 if(paymentDesc=="CUP")
		 {
			 if(Value.length()!=8){
			 theAppMgr.showErrorDlg(res.getString("Please enter exactly 8 digts."));
			 theAppMgr.setSingleEditArea(res.getString(
	            "Enter first 8 digits of account number."), "OFFLINE_CARD"
	            , "");
			 theAppMgr.setEditAreaFocus();
		 		return;
			 }
			 //validate the card in offline return
				try {

					if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0))){
						int row2 = tblPayment.getSelectedRow();
						Payment payment = model.getPayment(row2);
						String creditCardHolderName;
						if(payment instanceof CreditCard){
						validateValue = ((CreditCard)payment).getAccountNumber();
						creditCardHolderName = ((CreditCard)payment).getCreditCardHolderName();
							if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
								if(!(creditCardHolderName != null && creditCardHolderName.equals("Mobile"))){
									if(!((validateValue.substring(0, 8)).equals(Value))){
										 theAppMgr.showErrorDlg(res.getString("Please use the same "+payment.getGUIPaymentName()+ " card "+((CreditCard)payment).getAccountNumber()+" from original SALE transaction."));
										 theAppMgr.setSingleEditArea(res.getString(
								            "Enter first 8 digits of account number."), "OFFLINE_CARD"
								            , "");
									 theAppMgr.setEditAreaFocus();
									 return;
									}
								}
							}

						}
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


		 }//Ends
		 else
		 {
			 if(Value.length()!=6){
			 theAppMgr.showErrorDlg(res.getString("Please enter exactly 6 digts."));
			 theAppMgr.setSingleEditArea(res.getString(
	            "Enter first 6 digits of account number."), "OFFLINE_CARD"
	            , "");
		 theAppMgr.setEditAreaFocus();
		 return;}

			 //validate the card in offline return
				try {
					if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0))){
						int row2 = tblPayment.getSelectedRow();
						Payment payment = null;
						String creditCardHolderName;
						if(row2>=0)
							payment= model.getPayment(row2);
						if(payment instanceof CreditCard){
						validateValue = ((CreditCard)payment).getAccountNumber();
						creditCardHolderName = ((CreditCard)payment).getCreditCardHolderName();

							if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
								if(!(creditCardHolderName != null && creditCardHolderName.equals("Mobile"))){
									if(validateValue.length()>0 && !((validateValue.substring(0, 6)).equals(Value))){
									 theAppMgr.showErrorDlg(res.getString("Please use the same "+payment.getGUIPaymentName()+ " card "+((CreditCard)payment).getAccountNumber()+" from original SALE transaction."));
										 theAppMgr.setSingleEditArea(res.getString(
										    "Enter first 6 digits of account number."), "OFFLINE_CARD"
										    , "");
										 theAppMgr.setEditAreaFocus();
										 return;
									}
								}
							}
						}
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		 	firstSixOrEight=Value;
		 	//Ends
		  	theAppMgr.setSingleEditArea(res.getString(
		            "Enter last 4 digits of account number."), "AMOUNT_OFFLINE" , "");
			 theAppMgr.setEditAreaFocus();
			 }   
	   }

	   else if (Command.equals("AMOUNT_OFFLINE")) {
		   doofflineOverride = true;
		   String validateValue = "";
		   Payment payment = null;
		   ArmCurrency amtVal = null;
		   try {
			   amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 //Vivek Mishra : Added for capturing last 6 digits in AJB offline scenario.
			 if(Value.length()!=4)
			 {
				 theAppMgr.showErrorDlg(res.getString("Please enter exactly 4 digts."));
				 theAppMgr.setSingleEditArea(res.getString(
		            "Enter last 4 digits of account number."), "AMOUNT_OFFLINE" , "");
			 theAppMgr.setEditAreaFocus();
			 return;
			 }
			 
			 //Vivek Mishra : Added isMobileTerminalRefund make mobile terminal an independent teder in case of Refund just like cash 13-JUN-2016
				try {
					if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0)) && !isMobileTerminalRefund){//Ends here
						int row2 = tblPayment.getSelectedRow();
						payment = model.getPayment(row2);
						String creditCardHolderName;
						if(payment instanceof CreditCard){
						validateValue = ((CreditCard)payment).getAccountNumber();
						creditCardHolderName = ((CreditCard)payment).getCreditCardHolderName();	

							if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
								if(!(creditCardHolderName != null && creditCardHolderName.equals("Mobile"))){
									if(validateValue.length() > 0 && !((validateValue.substring(((validateValue.length())-4), (validateValue.length()))).equals(Value))){
										theAppMgr.showErrorDlg(res.getString("Please use the same "+payment.getGUIPaymentName()+ " card "+((CreditCard)payment).getAccountNumber()+" from original SALE transaction."));
										theAppMgr.setSingleEditArea(res.getString(
												    "Enter first 6 digits of account number."), "OFFLINE_CARD"
												    , "");
										theAppMgr.setEditAreaFocus();
										return;
									}
								}
							}						
						}
					}
				} catch (CurrencyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			 lastfour=Value;
			 //Ends
		    PaymentTransaction txn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
	        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
	        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)txn.getAppModel(
	            CMSAppModelFactory.getInstance(), theAppMgr);
	        ArmCurrency amt = null;
			try {
				//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.
						    getTotalPaymentAmount());
				}
				//Ends
				else
				amt = appModel.getCompositeTotalAmountDue().subtract(appModel.
				    getTotalPaymentAmount());
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Vivek Mishra : Added for capturing last 6 digits in AJB offline scenario.
			thePayment = ValidateCardType.allocCreditCardPayment(firstSixOrEight,lastfour,paymentDesc);
	     if(thePayment==null)
	        {
	        	theAppMgr.showErrorDlg(res.getString("The account number is invalid."));
	        	returnToOfflineTenderList();
			    return;
	        }
	        //Ends
	        firstSixOrEight = "";
	        lastfour = "";
			doNotValidate = true;
			//Show the correct amount in offline return
			//Vivek Mishra : Added isMobileTerminalRefund to make mobile terminal an independent teder in case of Refund just like cash 13-JUN-2016
			   try {
					if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0)) && !isMobileTerminalRefund){//Ends here
						doofflineOverride = true;
						int row2 = tblPayment.getSelectedRow();
						payment = model.getPayment(row2);
						theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
								, payment.getAmount().absoluteValue(), theAppMgr.CURRENCY_MASK);	
					}
					else{
						theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT_MANOVERRIDE"
					, amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        theAppMgr.setEditAreaFocus();
	        theAppMgr.showMenu(MenuConst.POST_AUTH, theOpr, theAppMgr.PREV_BUTTON);
	        authorize = false;
	       return;
	   }
		if(Command.equals("APPROVER")){
			isPasswordEntered = false;
			CMSEmployee theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
	
			try {
				theApprover = CMSEmployeeHelper.findByExternalId(theAppMgr, Value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (theApprover == null){
				theAppMgr.showErrorDlg(res.getString("You have entered wrong employee id."));
				enterUserName();
				return;
			}
			 if (testCanBeOperator()){
					 if( !theApprover.getExternalID().equals(theOpr.getExternalID())) {
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
		
			theAppMgr.addStateObject("APPROVER", Value);
			return;
		}
		
		//Rachana - to validate password for approval of return transaction
		if (Command.equals("PASSWORD")) {
			if(!Value.equals(null)){
				if(processPasswordEntry((String)Value)){
					isPasswordEntered = true;
			      	if(!theApprover.equals(null)){
								      		
			      		theAppMgr.setEditAreaFocus();
						initButtons(CMSPaymentMgr.REFUND);
						theAppMgr.setSingleEditArea(res.getString("Please select refund tender type."));
						return;
					}
				}else{
					if ((passwordWrongCount < 3)|| (Value.length() == 0)) {
				          enterPassword();
				    } else {
				          passwordWrongCount = 0;
				          enterUserName();
				    }
				}
			}
			return;
		}
	}

	//Added by ANjana for return override manager approval
	private void enterPassword() {
	    theAppMgr.setSingleEditArea(res.getString("Please enter your secret password.")
	        , "PASSWORD", theAppMgr.PASSWORD_MASK);
		theAppMgr.setEditAreaFocus();
		return;
	}
	
	 protected void enterUserName() {
		    theAppMgr.setSingleEditArea(res.getString("Enter approver id for return and press 'Enter'.")
		        , "APPROVER", theAppMgr.PASSWORD_MASK);
		    //theAppMgr.setEditAreaFocus();
		  }
	 
	 protected boolean processPasswordEntry(String password) {
		    if (theApprover.getPassword().equals(password) || theApprover.getPassword().length() == 0) {
		      return (true);
		    } else {
		      theAppMgr.showErrorDlg(res.getString("Incorrect Password entered"));
		      passwordWrongCount++;
		      return (false);
		    }
		  }
	 
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
		String[] pay = CMSPaymentMgr.getPayments(type, theTxn.getPaymentTransaction());
		//Mayuri Edhara :: 18-NOV-16 - Modified for Menu Changes requested by Edwin.
		//JButton[] btns = new JButton[pay.length + 3]; // allow for prev, and delete button
		JButton[] btns = new JButton[pay.length + 2]; // allow for prev, and delete button
		for (int x = 0; x < pay.length; x++) {
			try {
				Payment payment = CMSPaymentMgr.getPayment(pay[x]);
				//Mayuri Edhara :: 18-NOV-16 - Modified for Menu Changes requested by Edwin.
				// Issue # 996
				if (pay[x].equalsIgnoreCase("CREDIT_CARD")) {

					btns[x] = new JButton(HTML.formatLabeltoHTML(res.getString("Other"), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
					btns[x].setActionCommand("VIEW_CREDIT_CARD");

				} else {

					btns[x] = new JButton(HTML.formatLabeltoHTML(res.getString(payment.getGUIPaymentName()), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
					btns[x].setActionCommand(pay[x]);
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
		}
		//Mayuri Edhara ::  18-NOV-16 - Modified for Menu Changes requested by Edwin.
		//Mayuri edhara :: 01-Mar-17 - Show the mobile terminal button on the Tender Screen for canada stores
		String countryId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
		if(countryId.equalsIgnoreCase("CAN")){
			btns[btns.length - 3] = new JButton(HTML.formatLabeltoHTML(res.getString("Mobile Terminal"), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
			btns[btns.length - 3].setActionCommand("Mobile Terminal");
		}


		btns[btns.length - 2] = new JButton(HTML.formatLabeltoHTML(res.getString("Delete Payment"), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
		btns[btns.length - 2].setActionCommand("DEL_PAY");

		btns[btns.length - 1] = new JButton(HTML.formatLabeltoHTML(res.getString("Previous"), theAppMgr.getTheme().getButtonFont(), theAppMgr.getTheme().getButtonTextColor()));
		btns[btns.length - 1].setActionCommand("PREV");
		theAppMgr.setButtons(btns);
	}

	/**
	 * @param Command
	 * @param obj
	 */
	public void objectEvent(String Command, Object obj) {
		//Added by Vivek Mishra to fetch country id
		String countryId = (((CMSStore)com.chelseasystems.cr.appmgr.AppManager.getInstance().
		          getGlobalObject("STORE")).getCountry());
		//End
		if (Command.equals("PAYMENT_OFFLINE")) {
			doofflineOverride = true;
			try {
 			if (obj != null) {
					Payment pay = (Payment) obj; 
					if (pay.getPaymentCode() == null || pay.getPaymentCode() == "" || pay.getPaymentCode().trim().length() == 0) {
						
						//Added by Vivek Mishra for US region payment code changes
						
						String payCode="";
						String paymentDesc=((pay.getClass()).getSimpleName()).toUpperCase();
						
							payCode = getPaymentCode(pay.getTransactionPaymentName(),paymentDesc);
						
						//End
						
						pay.setPaymentCode(payCode);
					}
					// do not allow payment of 0.00
					if (!pay.getAmount().equals(new ArmCurrency(0.0))) {
						// set the amount if change is due
						if (changeDue) {
							// make it a negative for change
							pay.setAmount(pay.getAmount().multiply(-1));
						}
						try {
							theTxn.addPayment((Payment) obj);
							model.addPayment((Payment) obj);
							//Vivek Mishra : Added for Mobile terminal return flow
						    if(OrgSaleTxn!=null && !(obj instanceof CreditCard) && delRow!=-1)
						    {	
							model.removeRowInModel(delPay);
							updateLabels();
			                                tblPayment.repaint();
							delRow = -1;
							delPay = null;
						    }
						    //Ends
							//VM: Cash sale over $10,000 must prompt for IRS form 8300
							Payment[] payments = theTxn.getPaymentsArray();
							ArmCurrency totalCashPayment = new ArmCurrency(0.0);
							for (int i = 0; i < payments.length; i++) {
								if (payments[i] instanceof Cash){
									totalCashPayment = totalCashPayment.add(payments[i].getAmount());
									}
							
							}
							if (CMSPaymentMgr.getIRSAlertAmount(ArtsConstants.PAYMENT_TYPE_CASH) != null
									&& totalCashPayment.doubleValue() >= CMSPaymentMgr.getIRSAlertAmount(ArtsConstants.PAYMENT_TYPE_CASH).doubleValue())
								theAppMgr.showErrorDlg(res.getString("Please have the customer complete IRS form 8300."));
							model.fireTableDataChanged();
							updateLabels();
							theAppMgr.setSingleEditArea(res.getString("Choose change type."));
							//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
							ArmCurrency amtDue = new ArmCurrency(0.0d);
							if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
									&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
								amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
								LightPoleDisplay.getInstance().paymentDisplay((Payment) obj, amtDue);
							}
							//Ends
							else
							LightPoleDisplay.getInstance().paymentDisplay((Payment) obj, theTxn.getCompositeTotalAmountDue());
						} catch (BusinessRuleException ex) {
							theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
						}
					}
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			} finally {
				if(!manualOnCommunicationError){
				selectPayment();
		}else{
			selectOption();
			selectLastRow();
			return;
		}
			}

		}
		if (Command.equals("PAYMENT")) {
			try {
				if(rownum > -1 && OrgSaleTxn!=null){
					isGiftCardReturn = true;
					//Payment[] payments = ;
					for(int i = 0 ;i<model.getRowCount();i++){
						if(model.getRowInPage(i) instanceof StoreValueCard && isGiftCardReturn ){
						   model.removeRow(i);
							i= -1;
						}
					
					}
					
				
					isGiftCardReturn = false;
				}
 			if (obj != null) {
				Payment pay = (Payment) obj; 
				reqAmt = pay.getAmount();
				if (pay.getPaymentCode() == null || pay.getPaymentCode() == "" || pay.getPaymentCode().trim().length() == 0) {
					
					//Added by Vivek Mishra for US region payment code changes
					
					String payCode="";
					String paymentDesc=((pay.getClass()).getSimpleName()).toUpperCase();
			
						payCode = getPaymentCode(pay.getTransactionPaymentName(),paymentDesc);
					
					//End
					
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
						
						// add the amount to payment only if it is not credit card payment // added one more condition Vishal Yevale 26 sept 2016 giftcard manual payment flow
						if( !(obj instanceof CreditCard) )
					    {
							//VISHAL ADDED CONDITION
							if(fipayGiftcardFlag!=null & fipayGiftcardFlag.equalsIgnoreCase("Y") && (obj instanceof CMSStoreValueCard || obj instanceof CMSDueBill || obj instanceof CMSRedeemable) &&  !(obj instanceof CMSDueBillIssue)) {

							}else{
							theTxn.addPayment((Payment) obj);
							model.addPayment((Payment) obj);
							}
							
					    }
				
						//Vivek Mishra : Added for Mobile terminal return flow
					    if(OrgSaleTxn!=null && !(obj instanceof CreditCard) && delRow!=-1)
					    {	
						//model.removeRow(delRow);
					    model.removeRowInModel(delPay);
						updateLabels();
		                                tblPayment.repaint();
						delRow = -1;
						delPay = null;
					    }
					    //Ends
						//Vivek Mishra : Added for sending AJB 100 request for every addition of tender
						doNotValidate = false;
						if(pay instanceof CreditCard && isManualkeyEntry){
							((CreditCard)pay).setManuallyKeyed(true);
							isManualkeyEntry  = false;
						}
						if(!authorize((Payment) obj))
						{
							NotAuthorize();
							//Vivek Mishra : Added to send 111 SAF request in case of AJB reaponse code 2(bank link down) 15-JUN-2016
							//added one more condition by Vishal Yevale 26 sept 2016 gift card manual flow
							if(manualOnCommunicationError && ( (Payment) obj instanceof CreditCard ||(Payment) obj instanceof CMSStoreValueCard || (Payment) obj instanceof CMSDueBillIssue || (Payment) obj instanceof CMSDueBill))
							{
								model.addPayment((Payment) obj);
								theTxn.addPayment((Payment) obj);
							}
							//Ends here
							
						}else
						{
							model.addPayment((Payment) obj);
							theTxn.addPayment((Payment) obj);
						}
						//Ends
						//VM: Cash sale over $10,000 must prompt for IRS form 8300
						isMobileTerminal = false;
						Payment[] payments = theTxn.getPaymentsArray();
						ArmCurrency totalCashPayment = new ArmCurrency(0.0);
						for (int i = 0; i < payments.length; i++) {
							if (payments[i] instanceof Cash){
								totalCashPayment = totalCashPayment.add(payments[i].getAmount());
								}
						
						}
						if (CMSPaymentMgr.getIRSAlertAmount(ArtsConstants.PAYMENT_TYPE_CASH) != null
								&& totalCashPayment.doubleValue() >= CMSPaymentMgr.getIRSAlertAmount(ArtsConstants.PAYMENT_TYPE_CASH).doubleValue())
							theAppMgr.showErrorDlg(res.getString("Please have the customer complete IRS form 8300."));
						model.fireTableDataChanged();
						updateLabels();
						theAppMgr.setSingleEditArea(res.getString("Choose change type."));
						//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
						ArmCurrency amtDue = new ArmCurrency(0.0d);
						if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
								&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
							amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
							LightPoleDisplay.getInstance().paymentDisplay((Payment) obj, amtDue);
						}
						//Ends
						else
						LightPoleDisplay.getInstance().paymentDisplay((Payment) obj, theTxn.getCompositeTotalAmountDue());
					} 
					catch (BusinessRuleException ex) {
						theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					}
				}
			}

			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			} finally {
				//Vishal Yevale :Bug 28872 Double charge Issues (void,sale,refund) : 15 Nov 2017
				//if total amount is tendered then new flag will set to true.
				 try {
					 ArmCurrency amountVal = null;
					 logger.info("DEBUG-PROD-ISSUE:: compositeAmountDue  "+theTxn.getCompositeTotalAmountDue()+"  TotalPaymentAmount  "+theTxn.getTotalPaymentAmount());
					amountVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
					if(amountVal.doubleValue() == 0.0d){
						isTotalAmountTendered = true;
						 logger.info("DEBUG-PROD-ISSUE:: set isTotalAmountTendered as true");
					}
				} catch (CurrencyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//End Vishal Yevale :Bug 28872 Double charge Issues (void,sale,refund) : 15 Nov 2017
				
				if(!manualOnCommunicationError){
				selectPayment();
				//Vivek Mishra : Added to fix cash tender AJB offline issue.
				offlineMode=false;
				//Ends
				}//Mayuri Edhara :: kick start the manual auth process without explicitly clicking on the manual auth button:: 20 OCT 2016
				else if(manualOnCommunicationError){
					selectOption();
					selectLastRow();
					ArmCurrency amtVal = null;
					   try {
						   amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
						} catch (CurrencyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL", theAppMgr.REQUIRED_MASK);

					theAppMgr.setEditAreaFocus();
					return;
				} else {
					selectOption();
					selectLastRow();
					return;
				}
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
					//	If Txn is Paid Out Transaction.
						else if (txn instanceof CMSMiscPaidOut) {
							//check tender type not supported in paid out txn
							theAppMgr.showErrorDlg("Check tender type not supported in paid out transaction");
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
	private void selectPayment() {
		try {
			ArmCurrency amt = null;
		
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			ArmCurrency amountDue = new ArmCurrency(0.0d);
			
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amountDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
				amt = amountDue.subtract(theTxn.getTotalPaymentAmount());
			}
			//Ends
			else {
				amt = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
			}
			//}
	
		if(offlineMode){
			//Vivek Mishra : Added for Mobile terminal return flow
			if(OrgSaleTxn!=null && delRow != -1)
			{
				selectRow(delRow);
			}
			updateLabels();
			tblPayment.repaint();
			
			//Ends
			//Vivek Mishra : Added for showing Credit Card types to choose in AJB offline mode
			if(paymentDesc!=null && paymentDesc=="CUP")
			 theAppMgr.setSingleEditArea(res.getString(
	            "Enter first 8 digits of account number."), "OFFLINE_CARD"
	            , "");//Ends
			else if(paymentDesc==null)
			{
			 theAppMgr.setSingleEditArea("Please select one tender option");
			 return;
			}//vishal Yevale : Giftcard manual Entry 3 oct 2016
			else if(paymentDesc.equalsIgnoreCase("CMSSTOREVALUECARD") || paymentDesc.equalsIgnoreCase("CMSDUEBILL")){
				theAppMgr.setSingleEditArea(res.getString(
			            "Enter 16 digits of account number."), "OFFLINE_CARD"
			            , "");
			}// end Vishal 
			else{
			 theAppMgr.setSingleEditArea(res.getString(
		            "Enter first 6 digits of account number."), "OFFLINE_CARD"
		            , "");
			}
			 theAppMgr.setEditAreaFocus();
			 return;
		}
		if (amt.equals(new ArmCurrency(0.0))) {
				if (shouldTxnPostWhenBalanceIsZero) {
					Thread post = new Thread(new Runnable() {

						/**
						 * put your documentation comment here
						 */
						public void run() {
							theAppMgr.setWorkInProgress(true);
							theAppMgr.setSingleEditArea("Posting...");
							theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
							PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "POST_TXN", 0));
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
			if (amt.lessThan(new ArmCurrency(0.0))) {
				// added by Anand to accomodate NOT posting transaction automatically if Paid-Out Txn
				//Added changes for new Return flow.
				if(OrgSaleTxn!=null)
				{
					
					Payment[] orgPayments = theTxn.getPaymentsArray();
					for (int idx = 0; idx < orgPayments.length; idx++) {
						if(orgPayments[idx] instanceof CreditCard){
							if(orgPayments[idx].getRespStatusCode().equals("")){
								theAppMgr.showMenu(MenuConst.POST_AUTH, theOpr, theAppMgr.PREV_BUTTON);
								return;
							}
					}}
					theAppMgr.showMenu(MenuConst.OK_PREVIOUS, theOpr);
					theAppMgr.setSingleEditArea(res.getString("Press Refund to initiate return and post"));
					changeDue = true;
					return;
				}
				//Ends
				if (theTxn.getPaymentTransaction() instanceof PaidOutTransaction) {
					if(mailCheckFlag){
						String Builder = PaymentMgr.getPaymentBuilder("MAIL_CHECK");
						if (Builder != null) {
							theAppMgr.buildObject(this, "PAYMENT", Builder, "MAIL_CHECK");
							mailCheckFlag  = false;
						}
						}
			
					
				}
				lblDue.setText(res.getString("Change Due") + ":");
				//if (!bool) {
				//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amountDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
				}
				else{
					amountDue = theTxn.getCompositeTotalAmountDue();
				}
				//if (theTxn.getCompositeTotalAmountDue().doubleValue() < 0) {
				if (amountDue.doubleValue() < 0) {
				//Ends	
					theAppMgr.setSingleEditArea(res.getString("Choose refund type."));
				} else if (!isMaxChangeAllowed()) {
					//initButtons(CMSPaymentMgr.CHANGE);
					theAppMgr.setSingleEditArea(res.getString("Choose change type."));
					//Khyati: No auto change if REfund due
				} else {
					if (!changeDue) {
						theTxn.addPayment(change);
						//Mahesh Nandure:Bug 29665: TND code populated for cash change
						if (change.getPaymentCode() == null || change.getPaymentCode() == "" || change.getPaymentCode().trim().length() == 0) {
							String payCode="";
							String paymentDesc=((change.getClass()).getSimpleName()).toUpperCase();
					
								payCode = getPaymentCode(change.getTransactionPaymentName(),paymentDesc);
							
							change.setPaymentCode(payCode);
						}
						//end 02/09/2017
						model.addPayment(change);
						model.fireTableDataChanged();
						ArmCurrency amtDue = new ArmCurrency(0.0d);
						//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
						if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
								&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
							amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
							
						}
						else
							amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
						//Ends
						
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
										PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "POST_TXN", 0));
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
								PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, (String) theAppMgr.getStateObject("PAYMENT"), 0));
								theAppMgr.removeStateObject("PAYMENT");
							}
						});
					}
				}
				//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amountDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
				}
				else{
					amountDue = theTxn.getCompositeTotalAmountDue();
				}
				//Ends
				if (amountDue.doubleValue() < 0) {
					//                    initButtons(PaymentMgr.REFUND);
					if(showMenuForRefundWithReturn){
						theAppMgr.showMenu(MenuConst.OK_PREVIOUS, theOpr);
					}else {
					//Anjana :FEB 23-17 added for no return default cc payment
						theAppMgr.unRegisterSingleEditArea();
						theAppMgr.removeStateObject("THE_EVENT");
						initButtons(CMSPaymentMgr.REFUND);
						String Builder = CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
						theAppMgr.buildObject("PAYMENT", Builder, "");
						thePayment =  new CreditCard(IPaymentConstants.CREDIT_CARD);
						showTitle(false);
						isPostComplete = false;
										
					}
				} else {
					initButtons(CMSPaymentMgr.CHANGE); // only show if credit
					//                    initButtons(PaymentMgr.CHANGE);             // only show if credit
				}
				changeDue = true;
			} else {
				lblDue.setText(res.getString("Amount Due") + ":");
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
									// Issue # 1230
									// Don't invoke Credit Card Bldr
									if (changeDue) {
										System.out.println("Don't invoke Credit Card Bldr");
									} else {
										PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
									}
								} else {
									if (flag) {
										PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
									} else if (isButtonInList(sAction_global) && theAppMgr.getStateObject("PAYMENT") != null) {
										// for skip condition in CustListApplet to return to PaymentApplet
										PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, (String) theAppMgr.getStateObject("PAYMENT"), 0));
										theAppMgr.removeStateObject("PAYMENT");
									} else {
										PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
									}
								}
							}
						});
					} catch (BusinessRuleException bex) {
						theAppMgr.setSingleEditArea(res.getString("Choose payment type."));
					}
				}
				changeDue = false;
				manualOnCommunicationError = false;
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
			logger.info("DEBUG-PROD-ISSUE:::::: isPostComplete inside the postTransaction method:::::"+isPostComplete);
			// update display manager
			
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				DisplayManager dm = (DisplayManager) theAppMgr.getGlobalObject("DISPLAY_MANAGER");
				if (theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) {
					try {
						TaxUtilities.applyTax(theAppMgr, (CMSCompositePOSTransaction) theTxn.getPaymentTransaction(),
								(CMSStore) ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getStore(),
								(CMSStore) ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getStore(), ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getProcessDate());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (dm != null) {
					dm.endTransaction((CompositePOSTransaction) theTxn.getPaymentTransaction());
				}
			}
			
			/*if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				CMSCompositePOSTransaction transaction = (CMSCompositePOSTransaction)theTxn.getPaymentTransaction();
				CMSV12Basket cmsV12Basket = transaction.getCmsV12Basket();
				if (cmsV12Basket!=null && transaction.getSaleLineItemsArray()!=null) {
					boolean isBasketTransaction = false;
					POSLineItem lineItem[] = transaction.getSaleLineItemsArray();
					ArrayList<String> itemId = cmsV12Basket.getItemList();
					for (String id : itemId) {
						for (POSLineItem item : lineItem) {
							if(item.getItem().getId()!=null && item.getItem().getId().equals(id)) {
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
			}*/
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
			//set reservation txn id
			if (theTxn.getPaymentTransaction() instanceof CompositePOSTransaction) {
				if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getReservationLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getReservationLineItemsArray().length > 0) {
					((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getReservationTransaction().doSetReservationId(theTxn.getPaymentTransaction().getId());
				}
			}
			/*
			 * 5095 - Post transaction before printing it.  The issue is, is that if the
			 * post causes an exception, a receipt still exists.  This post mechanism
			 * is merely just serializing this txn to disk, so its not that long.
			 * @see com.chelseasystems.cr.txnposter.TxnPosterSyncRMIServices
			 */
			
			//set the customer credit card mask and encrypted data as per region
			//boolean flag = fillAjbResponse();
			try {
				//Vivek Mishra : Added for Puerto Rico fical changes
				String countryId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				String stateId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getState();
				if("US".equalsIgnoreCase(Version.CURRENT_REGION) && stateId.equalsIgnoreCase("PR")){
				boolean flag = getFiscalNum();
				//Vivek Mishra : Added for handling null response from eNabler.
				if(!flag)
					theAppMgr.showErrorDlg(res.getString("No response received from eNabler."));
				}
				//Ends
				//Test
				//Vivek Mishra : Commented for allowing mobile terminal to work in US region as well
			    if(countryId.equals("CAN")){
			    	
			  
				((CMSCompositePOSTransactionAppModel)theTxn).getGsthstTaxAmt();
			  
				((CMSCompositePOSTransactionAppModel)theTxn).getGstTaxAmt();
			    	
				((CMSCompositePOSTransactionAppModel)theTxn).getPstTaxAmt();
			    
				((CMSCompositePOSTransactionAppModel)theTxn).getQstTaxAmt();
			    }
				//Ends
			    //Vishal Yevale : added this to send cashout request ( giftcard buyback) 6 Dec 2016
				PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
				if(theTxnPOS instanceof CMSRedeemableBuyBackTransaction && fipay_flag.equalsIgnoreCase("Y") && fipayGiftcardFlag.equalsIgnoreCase("Y"))
				{
					CMSRedeemableBuyBackTransaction txn = (CMSRedeemableBuyBackTransaction) theTxnPOS;
					String balanceAmount = "";
					String netAmount=String.valueOf(txn.getAmount().absoluteValue());
					if(netAmount.contains("USD")){
						netAmount=netAmount.replace("USD", "");
					}
					if(netAmount.contains(",") && netAmount.contains("-")){
					int start=netAmount.indexOf(',');
					int end=netAmount.indexOf('-');
					balanceAmount=String.valueOf(Double.valueOf(netAmount.substring(start+1,end).trim())/100);
				}else{
					balanceAmount=netAmount;
				}
					do{
						logger.info("DEBUG-PROD-ISSUE::::::validateRedeemableCashout:");
					boolean success=CMSCreditAuthHelper.validateRedeemableCashout(txn,balanceAmount,theAppMgr);
					logger.info("DEBUG-PROD-ISSUE::::::validateRedeemableCashout::::success"+ success);
					if(!success){
						
						boolean retryConnect = theAppMgr.showOptionDlg("Fipay Error"
 			    	            ,  CMSCreditAuthHelper.getResponseErrorMsg()+". Gift Card Buyback or Cashout request failed. Please try again or Void this transaction", "Retry", "Back");
						CMSCreditAuthHelper.setResponseErrorMsg("");
						if(retryConnect)	
						{
							continue;
						}
						else{	
							//Mayuri Edhara :: 03-23-17 - added the deletePaymentCancel to cancel the existing payments incase cashout failed on gift card .
							logger.info("DEBUG-PROD-ISSUE::::::deletePaymentCancel() for Cashout Failed on Gift Cards::::");	
							deletePaymentCancel();
							logger.info("DEBUG-PROD-ISSUE::::::deletePaymentCancel() cancelled the existing payments as cashout failed on gift card::::");	

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
										theAppMgr.fireButtonEvent("PREV");
										theAppMgr.setHomeEnabled(true);
									}
								 	});
								return false;
						}
					}
					else{
						break;
					}
					}while(true);
					
				}
				//end Vishal Yevale 6 Dec 2016
			  //Vivek Mishra : Added to send Gift Card activation and reload requests to AJB. 20-SEP-2016
				if(fipay_flag.equalsIgnoreCase("Y") && fipayGiftcardFlag.equalsIgnoreCase("Y") && theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getSaleTransaction() !=null)
				{
					POSLineItem[] items = theTxn.getLineItemsArray();
					boolean activationResponse = true;
					for(POSLineItem item : items)
					{
						//System.out.println(item.isMiscItem()+""+item.getMiscItemId()+""+item.getMiscItemComment()+""+item.getMiscItemGLAccount());
						if(item.isMiscItem() && item.getMiscItemId().equals("STORE_VALUE_CARD"))
						{
							boolean isReload = false;
							boolean retryConnect = false;
							String trackData;
							POSLineItemDetail[] lineItemDetails = item.getLineItemDetailsArray();
							for(int i=0; i<lineItemDetails.length; i++)
							{
								trackData = lineItemDetails[i].getGiftCertificateId();
									if(trackData!=null && trackData!="")
									{
										//Modified by Himani for adding new parameter for credit note issue on 22-SEP-2016
										if(trackData.contains("="))
										    activationResponse = CMSCreditAuthHelper.reloadGiftCard(null,trackData, theAppMgr, item, null);
										else
											activationResponse = CMSCreditAuthHelper.reloadGiftCard(trackData,null, theAppMgr, item, null);
										isReload = true;
										lineItemDetails[i].setGiftCertificateId(trackData+"R");
										if(!activationResponse)
										{

											retryConnect = theAppMgr.showOptionDlg("Fipay Error"
													 			    	            ,  CMSCreditAuthHelper.getResponseErrorMsg()+". Gift Card reload failed. Please try again or Void this transaction.", "Retry", "Void");
											 CMSCreditAuthHelper.setResponseErrorMsg("");
											if(retryConnect)	
											{
												i = i-1;
												continue;
											}
											else
											{
												logger.info("DEBUG-PROD-ISSUE::::::::deletePaymentCancel() in Gift Card reload failed::::::");
												deletePaymentCancel();
												SwingUtilities.invokeLater(new Runnable() {
													public void run() {
															theAppMgr.fireButtonEvent("PREV");
															//("CANCEL_TXN");
															//isPostComplete = true; // needed to fake out isHomeAllowed()
															theAppMgr.setHomeEnabled(true);
														}
													 	});
												return false;
											}

										}
									}
									else
									{
										isReload = false;
										break;
									}
							}
							if(!isReload)
							{
							do
							{//Modified by Himani for adding new parameter for credit note issue on 22-SEP-2016
							activationResponse = CMSCreditAuthHelper.activateGiftCard(item,null,theAppMgr); 

							if(!activationResponse)
							{
							retryConnect = theAppMgr.showOptionDlg("Fipay Error"
									 			    	            ,  CMSCreditAuthHelper.getResponseErrorMsg()+". Gift Card activation failed. Please try again or Void this transaction.", "Retry", "Void");
							CMSCreditAuthHelper.setResponseErrorMsg("");
							if(retryConnect)	
							{
								continue;
							}
							else
							{
								logger.info("DEBUG-PROD-ISSUE::::deletePaymentCancel() Gift Card activation failed::::");
								deletePaymentCancel();
								logger.info("DEBUG-PROD-ISSUE::::deletePaymentCancel() Payment voided after gift card activation failed::::");

								SwingUtilities.invokeLater(new Runnable() {
								public void run() {
										theAppMgr.fireButtonEvent("PREV");
										//("CANCEL_TXN");
										//isPostComplete = true; // needed to fake out isHomeAllowed()
										theAppMgr.setHomeEnabled(true);
									}
								 	});
								return false;
							}
							}
							else
								break;
						
							}while(true);
							}
						}
					}
			     }
				//Ends Here 20-SEP-2016
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
			System.out.println("about to print receipt...");
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
			if (theTxn.isFrankingRequired()) {
				endorseCheck();
			}
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
		//Vivek Mishra : Added for signature printing
		printSignature();
		//Ends
		theTxn.printReceipt(theAppMgr); // whoooo...   that was easy.
		//Vivek Mishra : Added for signature printing
		deleteSignFile();
		//Ends
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
		//Vivek Mishra : Added for signature printing
		printSignature();
		//Ends
		theTxn.rePrintReceipt(theAppMgr);
		//Vivek Mishra : Added for signature printing
		deleteSignFile();
		//Ends
	}

	/**
	 */
	private void enterStubQty() {
		theAppMgr.setSingleEditArea(res.getString("Enter number of alteration stubs to print."), "PRINT_STUB", new Integer(1), theAppMgr.INTEGER_MASK);
	}

	/**
	 * @param anEvent
	 */
	public void appButtonEvent(String Header, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		String countryId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
		//Vivek Mishra : Commented for allowing mobile terminal to work in US region as well
	    //if(countryId.equals("CAN")){
			if(sAction.equals("VISA"))
				sAction = "VISA_CA";
			if(sAction.equals("MASTER_CARD"))
				sAction = "MASTER_CARD_CA";
			if(sAction.equals("AMEX"))
				sAction = "AMEX_CA";
			if(sAction.equals("JCB"))
				sAction = "JCB_CA";
			if(sAction.equals("Debit"))
				sAction = "Debit_CA";
		//}
		
		if (sAction.equals("BACK")) {
			isMobileTerminal = false;
			isManualkeyEntry  = false;
			 anEvent.consume();
			//theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
			selectPayment();
			setEditArea("Select option", null, -1);
			showTitle(false);
			isPostComplete = false;
		}
		if (sAction.equals("VISA_CA")) {
				initValues();
				updateLabels();
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				//                String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
				String Builder = CMSPaymentMgr.getPaymentBuilder("VISA_CA");
				theAppMgr.buildObject("PAYMENT", Builder, "VISA");
				CreditCard cc = (CreditCard)thePayment;
				//cc.setTenderType("VISA");
				showTitle(false);
				isPostComplete = false;
		}
		if (sAction.equals("MASTER_CARD_CA")) {
			initValues();
			updateLabels();
			theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
			//                String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
			String Builder = CMSPaymentMgr.getPaymentBuilder("MASTER_CARD_CA");
			theAppMgr.buildObject("PAYMENT", Builder, "MASTER_CARD");
			CreditCard cc = (CreditCard)thePayment;
			showTitle(false);
			isPostComplete = false;
	}
		if (sAction.equals("AMEX_CA")) {
			initValues();
			updateLabels();
			theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
			//                String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
			String Builder = CMSPaymentMgr.getPaymentBuilder("AMEX_CA");
			theAppMgr.buildObject("PAYMENT", Builder, "AMEX");
			CreditCard cc = (CreditCard)thePayment;
			showTitle(false);
			isPostComplete = false;
	}
		if (sAction.equals("JCB_CA")) {
			initValues();
			updateLabels();
			theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
			//                String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
			String Builder = CMSPaymentMgr.getPaymentBuilder("JCB_CA");
			theAppMgr.buildObject("PAYMENT", Builder, "JCB");
			CreditCard cc = (CreditCard)thePayment;
			showTitle(false);
			isPostComplete = false;
	}
		if (sAction.equals("Debit_CA")) {
			initValues();
			updateLabels();
			theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
			//                String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
			String Builder = CMSPaymentMgr.getPaymentBuilder("Debit_CA");
			theAppMgr.buildObject("PAYMENT", Builder, "Debit");
			CreditCard cc = (CreditCard)thePayment;
			showTitle(false);
			isPostComplete = false;
	}
		if(Header.equals("GIFT_CARD")){

			if (sAction.equals("PREV")) {
				strUSECARD = null;
				anEvent.consume();
				//theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
				selectPayment();
				setEditArea("Select option", null, -1);
				showTitle(false);
				isPostComplete = false;
			}
			//Mayuri Edhara :: 
			// VISHAL YEVALE : 13 OCT 2016 MANUAL GIFT CARD ENTRY
			else if (sAction.equals("MANUAL_KEY_ENTRY")){
				try {
			/*	 ArmCurrency amt = null;
				 PaymentTransaction txn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
					CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) txn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
					try {
						//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario
						if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
								&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
							amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
						}
						//Ends
						else
					amt	 = appModel.getCompositeTotalAmountDue().subtract(appModel.getTotalPaymentAmount());*/
						
						applyVat(false);
						initValues();
						updateLabels();
						theAppMgr.unRegisterSingleEditArea();
						//Vivek Mishra : Added for Mobile terminal return flow
						getMobileTerminalReturnAmount();
					
						String Builder = CMSPaymentMgr.getPaymentBuilder("STORE_VALUE_CREDIT_MEMO");
						if (Builder != null) {
							
								theAppMgr.unRegisterSingleEditArea();
								theAppMgr.showMenu(MenuConst.GIFT_CARD, "GIFT_CARD", theOpr);
								theAppMgr.buildObject("MANUAL", Builder, sAction);
								}
					} catch (CurrencyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(Exception e){
						e.printStackTrace();
					}
						
               	}
//END VISHAL YEVALE
		
		}
		//Ruchi Canada changes ends
		if (Header.equals("CREDIT_CARD")) {
			if (sAction.equals("PREV")) {
				strUSECARD = null;
				anEvent.consume();
				//theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
				// vishal Yevale :  23 Nov 2016 menu change request by Edwin
				offlineMode = false;
				selectPayment();
				setEditArea("Select option", null, -1);
						//Anjana :FEB 23-17 added for no return default cc payment
				 ArmCurrency amtDue;
				try {
					amtDue = theTxn.getCompositeTotalAmountDue().subtract(
							theTxn.getTotalPaymentAmount());

					if ((!(OrgSaleTxn != null)) && (amtDue.doubleValue() < 0)) {
						theAppMgr.unRegisterSingleEditArea();
						initButtons(CMSPaymentMgr.REFUND);
						String Builder = CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
						theAppMgr.buildObject("PAYMENT", Builder, "");
					}
				} catch (CurrencyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				showTitle(false);
				isPostComplete = false;
			} else if (sAction.equals("USE_CARD_ON_FILE")) {
				//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
				// If Customer is Null-- Display Customer Lookup
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
							if ((creditCard.getStoreId()).equalsIgnoreCase(storeId) && creditCard.getCardToken()!=null) {
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
							//___Tim:1903 Expired Cards on File are going out and getting authorized
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
								//vivek added
								String ccNum;
								
								availCreditCards[i] = new GenericChooserRow(new String[] { creditCard.getCreditCardType(), creditCard.getMaskedCreditCardNum(),
										dateFormat.format(creditCard.getExpDate()) }, creditCard);
								//ended vivek
							}
							GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availCreditCards, new String[] { "Card Type", "Card Number",
									"Expiration Date" });
							dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
							dlg.setVisible(true);
							if (dlg.isOK()) {
								CustomerCreditCard creditCard = (CustomerCreditCard) dlg.getSelectedRow().getRowKeyData();
								//___Tim:1903 Expired Cards on File are going out and getting authorized
								if(!checkExpiryDate(creditCard)){
									theAppMgr.showErrorDlg(res.getString("Credit Card on file has expired. Update or use other card."));
									selectPayment();
								}else {
									createPayment(creditCard);
								}
							} else {
								theAppMgr.showErrorDlg(res.getString("You did not select a Credit Card, you must select a Credit Card."));
							}
						}
					}
				} //#1217
				//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				}else{
					theAppMgr.showErrorDlg(res.getString("Please use Mobile Terminal for credit card payments"));
				}
			} else if (sAction.equals("USE_CARD_FROM_TRANSACTION")) {
				//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
				PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
				if ((theTxnPOS instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) theTxnPOS).isPresaleClose()) {
					POSLineItemDetail[] detail = ((CMSCompositePOSTransaction) theTxnPOS).getLineItemDetailsArray();
					if (detail != null) {
						if (((CMSSaleLineItemDetail) detail[0]).getPresaleLineItemDetail() != null) {
							CMSCompositePOSTransaction compositePOSTxn = (CMSCompositePOSTransaction) ((CMSSaleLineItemDetail) detail[0]).getPresaleLineItemDetail().getLineItem().getTransaction().getCompositeTransaction();
							String ccNumber = null;
							PresaleTransaction presaleTxn = compositePOSTxn.getPresaleTransaction();
							if (presaleTxn != null) {
								ccNumber = compositePOSTxn.getPresaleTransaction().getCreditCardNumber();
							}
							CustomerCreditCard cc = new CustomerCreditCard();
							if (ccNumber != null) {
								if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
									cc.setCreditCardNumber(presaleTxn.getCreditCardNumber());
									cc.setExpDate(presaleTxn.getCardExpirationDate());
								}else{
									EncryptionUtils util = new EncryptionUtils();
									String enCCNum = presaleTxn.getCreditCardNumber();
									String desCCNum = util.decrypt(enCCNum);
									cc.setCreditCardNumber(desCCNum);
									cc.setExpDate(presaleTxn.getCardExpirationDate());
									
								}
								cc.setCreditCardType(presaleTxn.getCardType());
								cc.setBillingZipCode(presaleTxn.getCardZipCode());
								//vivek added
								
								createPayment(cc);
							} else {
								theAppMgr.showErrorDlg(res.getString("No Credit card added during the presale open transaction"));
							}
						}
					}
				}
				//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				}else{
					theAppMgr.showErrorDlg(res.getString("Please use Mobile Terminal for credit card payments"));
				}
			}

			else if (sAction.equals("MANUAL_KEY_ENTRY")){
				//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
				 isManualkeyEntry = true;
				 ArmCurrency amt = null; 
				 PaymentTransaction txn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
					CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) txn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
					try {
						//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
						if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
								&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
							amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
						}
						//Ends
						else	
					amt	 = appModel.getCompositeTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
					} catch (CurrencyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                                        theAppMgr.showErrorDlg(res.getString("Please enter card number, CVV value and zip code on pinpad."));
					theAppMgr.setSingleEditArea(res.getString("Enter amount."), "AMOUNT", amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
					//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				}else {
					theAppMgr.showErrorDlg(res.getString("Please use Mobile Terminal for credit card payments"));
				}
			}
			// Issue # 996
			//Mayuri Edhara ::  18-NOV-16 - Modified for Menu Changes requested by Edwin.
			else if(sAction.equals("Mobile Terminal")){

				PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "Mobile Terminal", 0));

			}

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
		sAction_global = sAction;
		try {
			
			
			if (sAction.equals("DEL_PAY")) {
				manualOnCommunicationError = false;
				logger.info("DEBUG-PROD-ISSUE::::Calling deletePayment()::: button Clicked ::::"+sAction);
				deletePayment();
				logger.info("DEBUG-PROD-ISSUE:::Deleted Payment() by clicking delete tender button::");
			}//Mayuri Edhara :: added for credit note issue 
			else if(sAction.equals("Credit Note Issue")){
				if(((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).getTransactionType().contains("RETN") && OrgSaleTxn != null){
					 OrgSaleTxn = null;
				}					
					String Builder = PaymentMgr.getPaymentBuilder("CREDIT_MEMO_ISSUE");
					theAppMgr.buildObject("PAYMENT", Builder, "CREDIT_MEMO_ISSUE");
				
			}//New button is added for Canada support
			else if(sAction.equals("Mobile Terminal")){
				//Vivek Mishra : Added for Mobile terminal return flow
				retAmt = new ArmCurrency(0.0d);	
				
				if(!showRefundOption && OrgSaleTxn !=null){
				//Vivek Mishra : Added to show cahier tender menu in case all the tenders are already refunded from original transaction. 12-MAY-2016
				ArmCurrency totalModelAmt = new ArmCurrency(0.0d);
			    int totalRows = tblPayment.getRowCount();
			    System.out.println(theTxn.getSaleTotalAmountDue());
			    int j = 0;
			    for(int i =0; i<totalRows; i++)
			    {
			     Payment pay = model.getPayment(i);
			     totalModelAmt = totalModelAmt.add(pay.getAmount());
			     if(pay.getRespStatusCode().equalsIgnoreCase("0") || (pay.getRespMessage()!=null && pay.getRespMessage().equalsIgnoreCase("0"))){
			      j++;
			       
			     }
			     else
			      break;
			     if(j==totalRows)
			     {
			      if(!(totalModelAmt.equals(theTxn.getCompositeTotalAmountDue()))){
			      OrgSaleTxn = null;
			      showRefundOption = true;
			      showMenuForRefundWithReturn = false;
			      theAppMgr.setEditAreaFocus();
			      initButtons(CMSPaymentMgr.REFUND);
			      theAppMgr.setSingleEditArea(res.getString("Please select refund tender type."));
			         return;
			     }}
			    }
				}
			    //Ends

				
				// Mayuri edhara ::  added so that if the refund payment has original transaction - then we set the original transaction back.	
				if(OrgSaleTxn == null &&  showMenuForRefundWithReturn){
					// Mayuri edhara ::  added so that if the refund payment has original transaction - then we set the original transaction back.
					OrgSaleTxn = getOriginalTransaction(theTxn.getPaymentTransaction());		
				}
				//Vivek Mishra : Commented the code to make mobile terminal an independent teder in case of Refund just like cash 13-JUN-2016
				if(OrgSaleTxn != null)
				{
				isMobileTerminalRefund = true;
					
				}
				//Ends here 13-JUN-2016
				//Ends
			
				//Manual Authorization not required.
				//isMobileManualAuthRequired = false;
				//Added to take first 6 and last 4 credit card digit entry when using Mobile terminal 22-APR-2016
				displayCreditCardTypes();
				getSelectedType();
					//Ends
					//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				if(!isMobileTerminal){
					isPostComplete = false;
					theAppMgr.showErrorDlg(res.getString("Exiting the mobile Terminal."));
					invokeViewCCEvent("VIEW_CREDIT_CARD");

				} else {
					returnToOfflineTenderList();
					isPostComplete = false;
					return;
				}
			}
			//Vishal : added for offline payment by giftcard 23 sept 2016
			else if(sAction.equals("Manual GiftCard Entry")){
				//Vivek Mishra : Added for Mobile terminal return flow
				retAmt = new ArmCurrency(0.0d);

				if(!showRefundOption && OrgSaleTxn !=null){
				//Vivek Mishra : Added to show cahier tender menu in case all the tenders are already refunded from original transaction. 12-MAY-2016
				ArmCurrency totalModelAmt = new ArmCurrency(0.0d);
			    int totalRows = tblPayment.getRowCount();
			    System.out.println(theTxn.getSaleTotalAmountDue());
			    int j = 0;
			    for(int i =0; i<totalRows; i++)
			    {
			     Payment pay = model.getPayment(i);
			     totalModelAmt = totalModelAmt.add(pay.getAmount());
			     if(pay.getRespStatusCode().equalsIgnoreCase("0") || (pay.getRespMessage()!=null && pay.getRespMessage().equalsIgnoreCase("0"))){
			      j++;

			     }
			     else
			      break;
			     if(j==totalRows)
			     {
			      if(!(totalModelAmt.equals(theTxn.getCompositeTotalAmountDue()))){
			      OrgSaleTxn = null;
			      showRefundOption = true;
			      showMenuForRefundWithReturn = false;
			      theAppMgr.setEditAreaFocus();
			      initButtons(CMSPaymentMgr.REFUND);
			      theAppMgr.setSingleEditArea(res.getString("Please select refund tender type."));
			         return;
			     }}
			    }
				}
			    //Ends
				// Mayuri edhara ::  added so that if the refund payment has original transaction - then we set the original transaction back.
				if(OrgSaleTxn == null &&  showMenuForRefundWithReturn){
					// Mayuri edhara ::  added so that if the refund payment has original transaction - then we set the original transaction back.
					OrgSaleTxn = getOriginalTransaction(theTxn.getPaymentTransaction());
				}
				//Vivek Mishra : Commented the code to make mobile terminal an independent teder in case of Refund just like cash 13-JUN-2016
				if(OrgSaleTxn != null)
				{
				isMobileTerminalRefund = true;
				}
	
				//isMobileManualAuthRequired = true;
				//Added to take first 6 and last 4 credit card digit entry when using Mobile terminal 22-APR-2016
					//Ends
					returnToOfflineTenderList();

			isPostComplete = false;
				return;
			}// end vishal 23 sept 2016
			if (sAction.equals("BACK")) {
				updateLabels();
				selectPayment();
				return;
			}
			//changes ends for Canada
			
			else if (sAction.equals("PRINT_RECEIPT")) {
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
				boolean showTenderOptions = false;
				

				//Anjana added to make sure transaction is not posted , proper validation is shown when it cant be posted
				Payment[] payment= theTxn.getPaymentsArray();
				int len = payment.length;
				
				ArmCurrency amtDue = new ArmCurrency(0.0d);
				//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();							
				}
				//Ends
				else
			    amtDue	= theTxn.getCompositeTotalAmountDue();
				amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
				
				if(!amtDue.equals(new ArmCurrency(0.0))){
					theAppMgr.showErrorDlg(res.getString("Amount Due should be zero to post the transaction!!!"));
					 showTenderOptions = true;
					 donotPost = true;
					 //Anjana Added the return statement to stop POS posting the trnasaction after showing the  message "Amount Due should be zero to post the transaction!!!".
					 return;
				}
				
				if(!authorize){
					for (int i = 0; i < payment.length; i++) {
				
					if (payment[i] instanceof CreditCard && !payment[i].getRespStatusCode().equals("0")) {
						String errorMsg = "";
						
						if(result!=null && result.equals("1")){

							returnCheckDeclined();
							donotPost = true;
							break;
						}
						else{
							//Vivek Mishra : Changed the error message to make it more clear 19-OCT-2016
							//errorMsg = "Authorization failed on one or more of the Card payments. Please click on manual override button and call for authorization number";
                             errorMsg = "Please press Manual Override button and enter the authorization code.";
							/*if (payments[i].getRespMessage() != null) {
								errorMsg += ("Message: " + payments[i].getRespMessage());
							} else {
								errorMsg = "Authorization failed on one or more of the payments. Server does not response or timeout.";
							}*/
							theAppMgr.showErrorDlg(res.getString(errorMsg));
							donotPost = true;
							break;
						}

					}
					else if(fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y") && (payment[i] instanceof CMSStoreValueCard || payment[i] instanceof CMSDueBillIssue || payment[i] instanceof CMSDueBill)&& !payment[i].getRespStatusCode().equals("0")){
						String errorMsg = "Please call for Manual Authorization.";
						theAppMgr.showErrorDlg(res.getString(errorMsg));
						donotPost = true;
						break;
					}
					else {
						//Removed time out check as it will be handled by AJB
						if(payment[i] instanceof BankCheck && !payment[i].getRespStatusCode().equals("0")){
							String errorMsg = "Please call for Manual Authorization.";
				           //Ends
						theAppMgr.showErrorDlg(res.getString(errorMsg));
						donotPost = true;
						break;
					}
					}
					if(payment[i].getRespStatusCode().equals("0")){
						donotPost = false;
					}
				}
				}
			
				if ((new ConfigMgr("JPOS_peripherals.cfg").getString("MSR_DEVICE_TYPE")).equals("INGENICO_JPOS")) {
					Payment[] payments = theTxn.getPaymentTransaction().getPaymentsArray();
					boolean authDisplayOnET1000 = false;
				
				}
				tblPayment.repaint();
				try {
					// Set the Posting progress flag
					postingInProgress = true;
					//Anjana added do not post if return to tender list is clicked
					if(doNotValidate){
						returntotender=false;
					}
					//Vivek Mishra : Added to fix the POS Hanging issue when one Card is declined by AJB. 
				
					if(donotPost){
					returntotender=true;
					}else{
					returntotender=false;
					}
					//Ends
			
					if(!returntotender){
						if (postTransaction()) {
						isPostComplete = true;
						theAppMgr.addStateObject("POST_COMPLETE", "POST_COMPLETE");
						initReceiptButtons();
					} else {
						theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
					}
					}
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
				} finally {
					// UnSet the posting progress flag
					postingInProgress = false;
				}
				PaymentTransaction txn = (PaymentTransaction) CMSApplet.theAppMgr.getStateObject("TXN_POS");
				if (txn instanceof CMSCompositePOSTransaction) {
					CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
					Loyalty OldObj = theTxnPOS.getLoyaltyCard();
					if (OldObj != null) {
						theAppMgr.addStateObject("LOYALTYOBJ", OldObj);
						if (OldObj.getCurrBalance() + theTxnPOS.getLoyaltyPoints() >= rewardPointsLevel) {
							theAppMgr.showErrorDlg("Issue Reward Discount Card. Customer has earned a Reward Discount");
						}
					}
				}
				//Added by Anjana to send a POS_REFRESH 150 request after posting the transaction
				POSLineItem[] lineItems = theTxn.getLineItemsArray();
			
				//Send the final refresh after posting only and if returnn to tender list is clcked do not send refresh
				if(!returntotender){
			
					Refresh = true;
					idleMessage = true;
					clearMessage = true;
					if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
						sendItemMessageData(null,null,Refresh,idleMessage,clearMessage,"",false);
				
						}
				selectOption();
				}
				//Added since cash was not getting posted
				returntotender = false;
				donotPost = false;
			} else if (sAction.equals("PREV")) {
				//Vishal Yevale :Bug 28872 Double charge Issues (void,sale,refund) : 15 Nov 2017
				//if flag is true then total amount is tender so it should return back
				if(isTotalAmountTendered){
					 logger.info("DEBUG-PROD-ISSUE:: trying to return back even if total amount is tendered but refused with new FIX");
			    //fix for 27968 dated 12/06
				anEvent.consume();
				return;
				}
				//Vishal Yevale :Bug 28872 Double charge Issues (void,sale,refund) : 15 Nov 2017
				isGiftCardReturn = false;
				rownum = -1;
				manualOnCommunicationError = false;
				isMobileTerminalRefund = false;
				//Vivek Mishra : resetting delRow on the click of previous
				delRow = -1;
				delPay = null;
				//Ends
				logger.info("DEBUG-PROD-ISSUE:::::: isPostComplete :::::"+isPostComplete);
				logger.info("DEBUG-PROD-ISSUE:::::: amountdueCheck :::::"+amountdueCheck);
				if (!(isPostComplete && amountdueCheck)) {					
					// remove all payments
					//AJB: For sending Void
					PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
					Payment[] paymentArray= 	theTxnPOS.getPaymentsArray();
					//System.out.println("" + theTxnPOS.getTotalPaymentAmount());
					logger.info("DEBUG-PROD-ISSUE:::::: In Payment Screen button clicked :::::"+sAction);
					
					boolean flag = deletePaymentCancel();
					logger.info("DEBUG-PROD-ISSUE:::::: In Payment Screen ->Payment Deleted :->:flag:::"+flag);
					if(!flag)
					{
						anEvent.consume();
						return;
					}
					//}}
					//end
					prevPressed = true;
					try {
						theTxn.removeAllPayments();
						//Vivek Mishra : Added to fix wrong Amount tendered in case of return previous scenario
						if(OrgSaleTxn != null)
						{
							Payment[] payments = OrgSaleTxn.getPaymentsArray();
							for(Payment payment : payments)
							{
								if(payment.getAmount().lessThan(new ArmCurrency(0.0)))
								{
									payment.setAmount(payment.getAmount().multiply(-1d));
								}
							}
						}
						//Ends
					} catch (BusinessRuleException ex) {
						theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					}
				}
			} else if (sAction.equals("CANCEL")) {
				// Cancel suspending...
				selectPayment();
			} else if (sAction.equals("CANCEL_TXN")) {
				if (!theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?"))) {
					manualOnCommunicationError = false;
					anEvent.consume();
					return;
				}
				cancelTxnPressed = true;
			
				//Vivek Mishra : Added for not allowing cashier to delete a tender in AJB offline scenario as asked by Phil in status call 06-AUG-15.
				logger.info("DEBUG-PROD-ISSUE:::::::Calling deletePaymentCancel() on button click of ::::"+sAction);
				boolean flag = deletePaymentCancel();
				logger.info("DEBUG-PROD-ISSUE:::::::Calling deletePaymentCancel() Cancelled the Payment  :flag:::"+flag);

				if(!flag)
				{
					manualOnCommunicationError = false;
					anEvent.consume();
					return;
				}
				//Ends
				theAppMgr.setHomeEnabled(true);
				//theAppMgr.goHome();
			} else if (sAction.equals("OK")) {
				// add flag here
				isPostComplete = true;
				// MP: For NFS.
				theAppMgr.removeStateObject("CUST_NFS");
            //Added changes for new Return flow. 
			}else if (sAction.equals("ok_REFUND")) {
		        isGiftCardReturn = false;
		      //Mayuri Edhara fix for Change due name
		        model.setNoShowChangeDueName(false);
		        retAmt = new ArmCurrency(0.0d);
				if(tblPayment.getSelectedRow()<0){
					theAppMgr.showErrorDlg(res.getString("Please select a payment type"));
					return;
				}
			
				ArmCurrency amt = new ArmCurrency(0.0d);
				//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
				}
				//Ends
				else
					amt = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
				
				// Mayuri edhara ::  added so that if the refund payment has original transaction - then we set the original transaction back.				
				if(OrgSaleTxn == null &&  showMenuForRefundWithReturn){
				OrgSaleTxn = getOriginalTransaction(theTxn.getPaymentTransaction());		
				}
				
				if(OrgSaleTxn!=null && amt.lessThan(new ArmCurrency(0.0))){
				int row = tblPayment.getSelectedRow();
				Payment payment = model.getPayment(row);
			

				if(payment.getRespStatusCode().equalsIgnoreCase("0") || (payment.getRespMessage()!=null && payment.getRespMessage().equalsIgnoreCase("0"))){
					theAppMgr.showErrorDlg("This payment line is already refunded");
					return;
				}
			
				ArmCurrency retAmt = payment.getAmount();
				//Mayuri Edhara: fix for Change due amount
				if(!(OrgSaleTxn!=null && amt.lessThan(new ArmCurrency(0.0)))){ 
					if(retAmt.greaterThan(amt.getAbsoluteValue()))
						retAmt=amt;
					else
						retAmt=retAmt.multiply(-1d);
				}else if(OrgSaleTxn!=null && amt.lessThan(new ArmCurrency(0.0))){
					retAmt = retAmt.absoluteValue();
					if(retAmt.greaterThan(amt.getAbsoluteValue()))
						retAmt=amt;
					else
						retAmt=retAmt.multiply(-1d);
				}
				
				payment.setAmount(retAmt);
				if(payment instanceof CreditCard){	
				CreditCard cc= (CreditCard)payment;
			
				//Mayuri Edhara :: added to get out of refund incase of credit card payment.
				String token = cc.getTokenNo();	
				//Chnged the txt Anjana :FEB 23-17 added for no return default cc payment
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("N")){
				if(token != null){
					theAppMgr.showErrorDlg("Please refund the payment using mobile terminal.");
					return;
				}else if((cc.getCreditCardHolderName()!=null && cc.getCreditCardHolderName().equals("Mobile"))){
					theAppMgr.showErrorDlg("The original credit card transaction was not processed on the pinpad.Please process this refund using a Ԏo Receipt ReturnԮ");
					return; 
					}
				}
				else{
				
					if((cc.getCreditCardHolderName()!=null && cc.getCreditCardHolderName().equals("Mobile"))){
					theAppMgr.showErrorDlg("The original credit card transaction was not processed on the pinpad.Please process this refund using a Ԏo Receipt ReturnԮ");
					return;
				}}
				if(cc.getAmount().greaterThan(amt.getAbsoluteValue()))
				cc.setAmount(amt.getAbsoluteValue());
				cc.setReturnWithReceipt(true);
				//Added for resolving null response scenario.
				cc.setAuthRequired(true);
				//Vivek Mishra : Added to restrict sending refund request to AJB for Mabile Tender
				if(cc.getCreditCardHolderName() !=null && cc.getCreditCardHolderName().equals("Mobile"))
				{
					//Vivek Mishra : Added in order to not to send request to AJB for Mobile terminal refund.
					cc.setAuthRequired(false);
				}
				else
				    authorize(payment);
				if(cc.getRespStatusCode().equalsIgnoreCase("0")){
				theTxn.addPayment(payment);
				} //mayuri edhara :: added response code 10 to show override option.
				else if (cc.getRespStatusCode().equalsIgnoreCase("1") || cc.getRespStatusCode().equalsIgnoreCase("8") || cc.getRespStatusCode().equalsIgnoreCase("10")
						|| ((cc.getRespStatusCode().equalsIgnoreCase("2") || cc.getRespStatusCode().equalsIgnoreCase("3")) && !cc.isSAFable())
						|| (cc.getCreditCardHolderName()!=null && cc.getCreditCardHolderName().equals("Mobile"))){
					amtOverridden = cc.getAmount();
					if(cc.getCreditCardHolderName()!=null && cc.getCreditCardHolderName().equals("Mobile"))
					{
						theAppMgr.showErrorDlg("Mobile tenders can't be refunded. Please press Override to continue.");
					}
					theAppMgr.showMenu(MenuConst.OK_OVERRIDE, theOpr);
					theAppMgr.setSingleEditArea(res.getString("Press Override to return using other card"));
					return;
					//Mayuri edhara : added for refund SAF request
				}else if ((cc.getRespStatusCode().equalsIgnoreCase("2") || cc.getRespStatusCode().equalsIgnoreCase("3")) && cc.isSAFable()){
					NotAuthorize();
					if(manualOnCommunicationError)
					{
						theTxn.addPayment(payment);
						selectOption();
						selectLastRow();
						return;
					}
					//Ends here
				}
				
				updateLabels();
				selectPayment();
				cc.setReturnWithReceipt(false);
				//to remove IO error
				cc.setCreditCardHolderName("");
				
				
				}
				
				//Vivek Mishra : Added for cash refund
				else if(payment instanceof Cash)
				{
					payment.setRespMessage("0");
					theTxn.addPayment(payment);
					updateLabels();
					selectPayment();
				}//Ends
				// VISHAL YEVALE 25 OCT 2016 giftcard transaction return flow
				else if((payment instanceof CMSStoreValueCard || payment instanceof CMSDueBill || payment instanceof CMSDueBillIssue) && fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y")){
					rownum = tblPayment.getSelectedRow();
					Redeemable dBill=(Redeemable) payment;
					//String Builder = PaymentMgr.getPaymentBuilder("CREDIT_MEMO_ISSUE");
					//theAppMgr.buildObject("PAYMENT", Builder, payment);
					//ANJANA :: SETAUTH REQ
					if(payment instanceof CMSStoreValueCard){
						CMSStoreValueCard cmsStoreValueCard=(CMSStoreValueCard) payment;								
						cmsStoreValueCard.setAuthRequired(true);
					}
					if(payment instanceof CMSDueBill){
						CMSDueBill cmsDueBill=(CMSDueBill) payment;
						cmsDueBill.setAuthRequired(true);
					}
					if(payment instanceof CMSDueBillIssue){
						CMSStoreValueCard cmsStoreValueCard=(CMSStoreValueCard) payment;								
						cmsStoreValueCard.setAuthRequired(true);
					}
					authorize(payment);
					if(dBill.getRespStatusCode().equalsIgnoreCase("0")){
					theTxn.addPayment(payment);
					}else if (dBill.getRespStatusCode().equalsIgnoreCase("1") || dBill.getRespStatusCode().equalsIgnoreCase("8") 
							|| ((dBill.getRespStatusCode().equalsIgnoreCase("2") || dBill.getRespStatusCode().equalsIgnoreCase("3")) && !dBill.isSAFable())
							){
						amtOverridden = dBill.getAmount();
						theAppMgr.showMenu(MenuConst.OK_OVERRIDE, theOpr);
						theAppMgr.setSingleEditArea(res.getString("Press Override to return using other card"));
						return;
					}else if ((dBill.getRespStatusCode().equalsIgnoreCase("2") || dBill.getRespStatusCode().equalsIgnoreCase("3")) && dBill.isSAFable()){
						NotAuthorize();
						if(manualOnCommunicationError)
						{
							theTxn.addPayment(payment);
							selectOption();
							selectLastRow();
							return;
						}
					}else if(dBill.getRespStatusCode().equalsIgnoreCase("10")){
						theAppMgr.showErrorDlg(res.getString("Payment Error - Please click refund again"));
						return;
					}
					updateLabels();
					selectPayment();
				}
				//end  VISHAL YEVALE 25 OCT 2016 giftcard transaction return flow

				else if(payment instanceof StoreValueCard &&(fipayGiftcardFlag==null ||(!fipayGiftcardFlag.equalsIgnoreCase("Y")))){
					rownum = tblPayment.getSelectedRow();
				 ArmCurrency amtGiftCard = new ArmCurrency(0.0d);
					 ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());

					String Builder = PaymentMgr.getPaymentBuilder("CREDIT_MEMO_ISSUE");
					theAppMgr.buildObject("PAYMENT", Builder, "CREDIT_MEMO_ISSUE");
					
					
				
					}
				else if(payment instanceof BankCheck)
				{
					MailCheck pay = new MailCheck("MAIL_CHECK");
					pay.setAmount(payment.getAmount());
					pay.setRespMessage("0");
					theTxn.addPayment(pay);
					model.removeRowInModel(payment);
					model.addPayment(pay);
					model.fireTableDataChanged();
	                tblPayment.repaint();
					updateLabels();
					selectPayment();
					}
				
					
				//Vivek Mishra : Added to show cahier tender menu in case all the tenders are already refunded from original transaction. 12-MAY-2016
				ArmCurrency totalModelAmt = new ArmCurrency(0.0d);
			    int totalRows = tblPayment.getRowCount();
			    System.out.println(theTxn.getSaleTotalAmountDue());
			    int j = 0;
			    for(int i =0; i<totalRows; i++)
			    {
			     Payment pay = model.getPayment(i);
			     totalModelAmt = totalModelAmt.add(pay.getAmount());
			     if(pay.getRespStatusCode().equalsIgnoreCase("0") || (pay.getRespMessage()!=null && pay.getRespMessage().equalsIgnoreCase("0"))){
			      j++;
			       
			     }
			     else
			      break;
			     if(j==totalRows)
			     {
			      if(!(totalModelAmt.equals(theTxn.getCompositeTotalAmountDue()))){
			      OrgSaleTxn = null;
			      theAppMgr.setEditAreaFocus();
			      initButtons(CMSPaymentMgr.REFUND);
			      theAppMgr.setSingleEditArea(res.getString("Please select refund tender type."));
			         return;
			     }}
			    }
			    //Ends
			    }
				else{
				// add flag here
				isPostComplete = true;
				// MP: For NFS.
				theAppMgr.removeStateObject("CUST_NFS");
				}
				//Vishal Yevale :Bug 28872 Double charge Issues (void,sale,refund) : 15 Nov 2017
				//if total amount is tendered then new flag will set to true.
				try {
					 logger.info("DEBUG-PROD-ISSUE:: compositeAmountDue  "+theTxn.getCompositeTotalAmountDue()+"  TotalPaymentAmount  "+theTxn.getTotalPaymentAmount());
					 ArmCurrency amountVal = null;
					amountVal = theTxn.getCompositeTotalAmountDue().add(theTxn.getTotalPaymentAmount().multiply(-1d));
					if(amountVal.doubleValue() == 0.0d){
						 logger.info("DEBUG-PROD-ISSUE:: set isTotalAmountTendered :::::::"+isTotalAmountTendered);
						isTotalAmountTendered = true;
					}
				} catch (CurrencyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//Vishal Yevale :Bug 28872 Double charge Issues (void,sale,refund) : 15 Nov 2017
			} else if (sAction.equals("OK_OVERRIDE")) {
				overrideReturn  = true;
				//Vivek Mishra : Added for Mobile terminal return flow
				int row = tblPayment.getSelectedRow();
				Payment payment = model.getPayment(row);
				if(payment instanceof CreditCard)
				{
					delRow = row;
					delPay = payment;
				}
				//Ends
				CMSEmployee employee = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
				theApprover = null;
				theAppMgr.setSingleEditArea(res.getString("Enter approver id for return and press 'Enter'.")
				        , "APPROVER", theAppMgr.PASSWORD_MASK);
				//this.editAreaEvent("APPROVER",employee.getExternalID());
				anEvent.consume();	
			
			}
			
			else if (sAction.equals("PRINT_ALL_GIFT")) {
           //Ends
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
				//Vivek Mishra : Added for Mobile terminal return flow
				getMobileTerminalReturnAmount();
				//End
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				//                String Builder = PaymentMgr.getPaymentBuilder("CREDIT_CARD");
				String Builder = CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
				theAppMgr.buildObject("PAYMENT", Builder, "");
				//Vishal Yevale : Giftcard integration 5 oct 2016
			}else if(sAction.equals("STORE_VALUE_CREDIT_MEMO")){
				applyVat(false);
				initValues();
				updateLabels();
				//Vivek Mishra : Added for Mobile terminal return flow
				getMobileTerminalReturnAmount();
			
				String Builder = CMSPaymentMgr.getPaymentBuilder(sAction);
				if (Builder != null) {
					
						theAppMgr.unRegisterSingleEditArea();
						if(fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y")){
						theAppMgr.showMenu(MenuConst.GIFT_CARD, "GIFT_CARD", theOpr);
						theAppMgr.buildObject("PAYMENT", Builder, sAction);
						}else{
						theAppMgr.buildObject("PAYMENT", Builder, sAction);
						}
			                        }
			}// end Vishal Yevale 5 oct 2016
			
			else if (sAction.equals("VIEW_CREDIT_CARD")) {
				//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
				//if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
				PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
				//#1217: Display Presale added credit card option if its presale close txn
				if ((theTxnPOS instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) theTxnPOS).isPresaleClose()) {
					theAppMgr.showMenu(MenuConst.CREDIT_CARD_PRESALE_CLOSE, "CREDIT_CARD", theOpr);
				} else
					theAppMgr.showMenu(MenuConst.CREDIT_CARD, "CREDIT_CARD", theOpr);
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				//Vivek Mishra : Added for Mobile terminal return flow
				getMobileTerminalReturnAmount();
				//End
				String Builder = CMSPaymentMgr.getPaymentBuilder("CREDIT_CARD");
				theAppMgr.buildObject("PAYMENT", Builder, "");
				showTitle(false);
				isPostComplete = false;
				//Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
			//}else{
			//	theAppMgr.showErrorDlg(res.getString("Please use Mobile Terminal for credit card payments"));
			//}
			}
			// Issue # 996
			else if (sAction.equals("MAN_OVERRIDE")) {
				 ArmCurrency amtVal = null;
				   try {
					   amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
					} catch (CurrencyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				//Addded by sonali for error msg in case of manual overrride of bank check
				 PaymentTransaction txn = (PaymentTransaction) AppManager.getCurrent().getStateObject(
				"TXN_POS");
				 Payment[] payments= txn.getPaymentsArray();
				 //Anjana chnged on 05-23-2017 to use the selected payment from model instead of payments in transaction
				// for(int i =0; i<payments.length;i++)
				// {
				 int row = tblPayment.getSelectedRow();
				Payment payment =  model.getPayment(row);
					//Added to set card holder name while taking first 6 and last 4 credit card digit entry when using Mobile terminal 22-APR-2016
					 String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
					 if(payment instanceof CreditCard){
						 CreditCard cc = (CreditCard)payment;
						 if(cc.getExpirationDate() == null){
							 cc.setCreditCardHolderName("Mobile");
							 cc.setTenderType(cc.getGUIPaymentName());
						 }
					//Anjana added 05-23-2017 for adding support for Return SAF for CC	 
						 if (cc.isAuthRequired()) {
							 
							if (cc.getCreditCardHolderName() != null
									&& cc.getCreditCardHolderName()
											.equalsIgnoreCase("Mobile")) {
								theAppMgr.setSingleEditArea(res.getString("Enter authorization number."),"MANUAL_OFFLINE",theAppMgr.REQUIRED_MASK);
							} else {
								theAppMgr.setSingleEditArea(res.getString("Enter authorization number."),"MANUAL",theAppMgr.REQUIRED_MASK);
							}
						}
						 
					}
					 //Anjana added 04/2017 for enabling manual auth , for an issue whn already authed line is authed again, unable to auth the actual line
				if (payment instanceof CMSBankCheck) {
						if (payment.isAuthRequired()) {
							theAppMgr.setSingleEditArea(res.getString("Enter authorization number."),"MANUAL", theAppMgr.REQUIRED_MASK);
						}
					}
				
			
				// }
				 //end
				if(doofflineOverride){
					theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);	
					doofflineOverride =false;
				}
				else{
					// Vishal Yevale : added 5 oct 2016
					boolean authDone=true;
				
					//int row = tblPayment.getSelectedRow();
					//Anjana chnged 05-23-2017 to avoid fatal exception if unknowingly cashier selects some other line to manual auth causing array index out of bound
					Payment authPayment = model.getPayment(row);
					//Payment authPayment=payments[row];
					if(authPayment instanceof CMSStoreValueCard || authPayment instanceof CMSDueBill ||authPayment instanceof CMSDueBillIssue ){

						if(authorizeConfirm){
						if(authPayment instanceof CMSStoreValueCard){
							CMSStoreValueCard cmsStoreValueCard=(CMSStoreValueCard) authPayment;
							authDone=cmsStoreValueCard.isAuthRequired();
						}
						if(authPayment instanceof CMSDueBill){
							CMSDueBill cmsDueBill=(CMSDueBill) authPayment;
							authDone=cmsDueBill.isAuthRequired();
						}
						if(authPayment instanceof CMSDueBillIssue){
							CMSDueBillIssue cmsDueBillIssue=(CMSDueBillIssue) authPayment;
							authDone=cmsDueBillIssue.isAuthRequired();
						}
						}
					if(authDone){
						theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL_OFFLINE", theAppMgr.REQUIRED_MASK);
					}else{
					theAppMgr.setSingleEditArea(res.getString("Enter authorization number."), "MANUAL", theAppMgr.REQUIRED_MASK);
					}
				}}
				//end Vishal
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
				//Anjana:- check tender type not supported in paid out txn 06/16/2016
				if (theTxn.getPaymentTransaction() instanceof CMSMiscPaidOut) {
					theAppMgr.showErrorDlg("Check tender type not supported in paid out transaction");
					return;
				}
				applyVat(true);
				initValues();
				updateLabels();
				theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
				//Vivek Mishra : Added for Mobile terminal return flow
				getMobileTerminalReturnAmount();
				//End
				String Builder = PaymentMgr.getPaymentBuilder("CHECK");
				theAppMgr.buildObject("PAYMENT", Builder, "");
				
			} //Mayuri Edhara :: Added for Cash button on Ok_previous Menu const. For Return with Recipt to return with another tender
			else if(OrgSaleTxn!=null && sAction.equals("CASH")){	
				if(((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).getTransactionType().contains("RETN")){
				model.setNoShowChangeDueName(false);
				OrgSaleTxn = null;
				String Builder = PaymentMgr.getPaymentBuilder(sAction);
				if (Builder != null) {
					theAppMgr.buildObject(this, "PAYMENT", Builder, sAction);
				}
				}
			} else {
				if(sAction.equals("MAIL_CHECK")){
					if(!mailCheckFlag){
						theAppMgr.removeStateObject("CUST_SEARCH");
						theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
						theAppMgr.removeStateObject("CUSTOMER_SUBMIT");
					}
					mailCheckFlag = true;
					//Mayuri Edhara :: 17-05-17 : added so that the mail check builder takes the amount from amount due instead of getting
					// from the previous transaction payment object. This is only when the cashier does not select the payment. 
					OrgSaleTxn = null;
				}
				applyVat(false);
				initValues();
				updateLabels();
				//Vivek Mishra : Added for Mobile terminal return flow
				getMobileTerminalReturnAmount();
				//End
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
							PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, INITIAL_PAYMENT, 0));
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
					//Vishal Yevale : show buttons when select Giftcard as tender type
						if(sAction.equals("STORE_VALUE_CREDIT_MEMO")){
							theAppMgr.unRegisterSingleEditArea();
							theAppMgr.showMenu(MenuConst.GIFT_CARD, "GIFT_CARD", theOpr);
						}
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
	 * This method will remove a payment from the Txn and delete any other
	 *  payments back to the customer. Ex. If the amount given is greater than
	 *  the amount due, then the amount in change is then determined. If the
	 *  Eu then deletes a payment, then change amount will remain unless found
	 *  and deleted so the amount to give in change can be recalculated.
	 */
	private void deletePayment() {
		boolean offlineDelete = false;
		int row = tblPayment.getSelectedRow();
		if (row < 0) {
			theAppMgr.showErrorDlg(res.getString("You must first select a payment."));
		} else {
			try {
				Payment payment = model.getPayment(row);
			    
				if(payment instanceof CreditCard && ((CreditCard)payment).getCreditCardHolderName()!=null && (((CreditCard)payment).getCreditCardHolderName().equalsIgnoreCase("Mobile")))
				{
					offlineDelete = true;
				}
				else if(payment instanceof CreditCard ){
				CreditCard cc = (CreditCard)payment;
				if(cc.getExpirationDate()==null){
					offlineDelete = true;
					}
				}
				
				//Vivek Mishra : Added for not allowing cashier to delete a tender in AJB offline scenario as asked by Phil in status call 06-AUG-15.
				Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
				PaymentTransaction theTxn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
				String[] voidRes = null;
				//removed voiding checks which was causing issue for delete check
				if(!offlineDelete && payment instanceof CreditCard){
					
					try {
					theAppMgr.setWorkInProgress(true);
					logger.info("DEBUG-PROD-ISSUE::::Voiding from deletePayment()::::");
					voidRes = CMSCreditAuthHelper.validateVoid(theAppMgr, theTxn, register.getId(), false, true, false,payment,row);
					logger.info("DEBUG-PROD-ISSUE::::Voiding from deletePayment() response received:::"+voidRes[0]);
					theAppMgr.setWorkInProgress(false);
						} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			//To handle bank link down delte scenario	
					//Changed the manual Auth response code from 3 to 2
					//mayuri edhara : added response code 3 as manual auth is done for that scenario
					if(voidRes!=null && voidRes[0]!=null && ((voidRes[0].toString().equals("2") || voidRes[0].toString().equals("3")) && payment.isSAFable())){
						theAppMgr.setWorkInProgress(true);
						logger.info("DEBUG-PROD-ISSUE::::Voiding from deletePayment():::bank link down ::::");
						voidRes = CMSCreditAuthHelper.validateVoid(theAppMgr, theTxn, register.getId(), false, true, true,payment,row);	
						logger.info("DEBUG-PROD-ISSUE::::Voiding from deletePayment():::bank link down ::voidRes::"+voidRes[0]);
						theAppMgr.setWorkInProgress(false);
					}
					
				if(!offlineDelete){
						if(voidRes!=null && voidRes[0]!=null && (voidRes[0].toString().contains("All the Ajb Servers are down at the moment")))
						{
							boolean retryConnect = false;

							retryConnect = theAppMgr.showOptionDlg(res.getString("Offline Error")
									 			    	            , res.getString("Payment Device is Offline. Already authorized tenders cannot be deleted at the moment."), res.getString("Retry"), res.getString("Cancel"));
									 				
									 				
									 					if(!retryConnect){
									 						updateLabels();
									 						tblPayment.repaint();
									 						CMSPaymentTransactionAppModel txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
									 						ArmCurrency amt = new ArmCurrency(0.0d);
									 						//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
									 						if (((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
									 								&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
									 							amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(txn.getTotalPaymentAmount());
									 						}
									 						//Ends
									 						else
									 							txn.getCompositeTotalAmountDue().subtract(txn.getTotalPaymentAmount());
									 						if (!amt.equals(new ArmCurrency(0.0))) {
									 						selectPayment();
									 						} 
									 						return;
									 					 
									 					}else{
									 						//If clicked on Retry , send 100 request again
									 						logger.info("DEBUG-PROD-ISSUE::::deletePayment(): Recalled from Else loop after clicking RETRY:::::::");
									 						deletePayment();
									 						logger.info("DEBUG-PROD-ISSUE::::deleted the payment after recall:::::::");

									 						return;
									 					}
						}
		}
				
				}//Vivek Mishra : Added to void Gift Card payment 03-NOV-2016
				else if(payment instanceof Redeemable && fipay_flag.equalsIgnoreCase("Y") && fipayGiftcardFlag.equalsIgnoreCase("Y") && theTxn instanceof CMSCompositePOSTransaction)
				{
					boolean response = deleteRedeemablePayment(payment, theTxn);
					if(!response)
						return;
				}
				//Ends here 03-NOV-2016
			
				//Ends		
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
				updateLabels();
				tblPayment.repaint();
				selectPayment();
				
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			} catch (CurrencyException e) {
				System.out.print("Currency Ex deletePayment-> " + e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 */
	private void updateLabels() {
		try {
			fromPayment = true;
			
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			ArmCurrency amtDue = new ArmCurrency(0.0d);
			if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
			AmtTnd$Lbl.setText(theTxn.getTotalPaymentAmount().formattedStringValue());
			}
			//Ends
			else{
			AmtTnd$Lbl.setText(theTxn.getTotalPaymentAmount().formattedStringValue());
			
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) &&  (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
			}
			//Ends
			else{
			amtDue = theTxn.getCompositeTotalAmountDue();
			}
			}
		
			amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
			AmtDue$Lbl.setText(amtDue.formattedStringValue());
			POSLineItem[] lineItemArray = theTxn.getLineItemsArray();
			Refresh = true;
			idleMessage =false;
			clearMessage = false;
			if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
				sendItemMessageData(lineItemArray[0], lineItemArray, Refresh, idleMessage, clearMessage, "",fromPayment);
			}
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
					theAppMgr.showErrorDlg("No valid items are selected to print a gift receipt.");
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
		FrankDlg dlg = new FrankDlg(theAppMgr, theTxn.getPaymentTransaction(), theTxn.getCustomer() == null || theTxn.getCustomer().getTelephone() == null ? ""
				: theTxn.getCustomer().getTelephone().getFormattedNumber());
		dlg.setVisible(true);
	}


	private boolean authorize() {
		try {
	
			result = "0";
			isRefundPaymentRequired = false;
			
			 PaymentTransaction paymtTxn = theTxn.getPaymentTransaction();
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				ArmCurrency amountDue = new ArmCurrency(0.0d);
				if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amountDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
				}
				//Ends
				else
					amountDue = theTxn.getCompositeTotalAmountDue();
		        if(paymtTxn instanceof CompositePOSTransaction  && !(amountDue.greaterThan(new ArmCurrency(0.0))))
		        {
		            POSLineItem lines[] = ((CompositePOSTransaction)paymtTxn).getLineItemsArray();
		            for(int i = lines.length - 1; i >= 0; i--){
		                if((lines[i] instanceof CMSReturnLineItem)){
		                	isRefundPaymentRequired = true;
		                	  break;
		                }
		            }
		        } 
		        
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			Payment[] payments = null;
			if(OrgSaleTxn!=null && !overrideReturn){
			payments = OrgSaleTxn.getPaymentsArray();}
			else{
			payments = theTxn.getPaymentsArray();}
			int len = payments.length;
			
			/*Vivek Mishra : Changed for addind refund in AJB flow*/ 
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amountDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
			}
			//Ends
			else
				amountDue = theTxn.getCompositeTotalAmountDue();
			if (amountDue.greaterThan(new ArmCurrency(0.0))||isRefundPaymentRequired||isForced) {
			//End	
				//Removed ISD changes 
	
			 //ended
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				//Vivek Mishra : Changed the condition for allowing mobile terminal to work in US region as well
				//if(!storeCountry.equals("CAN"))
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
				if(!storeCountry.equals("CAN") && !isMobileTerminal){
			
					//Vivek Mishra : Changed the validate method call for AJB
				//Added For AJB-POS offline 
				
					//Anjana added For manual override send a 111 SAF request
					if(!isManualOverride && !doNotValidate){
						theAppMgr.setWorkInProgress(true);
						if(OrgSaleTxn!=null && !overrideReturn){
							logger.info("DEBUG-PROD-ISSUE:::::to check Void parameter :::");
							responseArrayinAuthorize = CMSCreditAuthHelper.validate(theAppMgr, OrgSaleTxn, register.getId(), isRefundPaymentRequired, false, false);
							/*if(responseArrayinAuthorize!=null)
							responseArrayinAuthorize[0]="0";
							*/
						}else{
							logger.info("DEBUG-PROD-ISSUE:::::to check Void parameter222 :::");
						responseArrayinAuthorize = CMSCreditAuthHelper.validate(theAppMgr, theTxn.getPaymentTransaction(), register.getId(), isRefundPaymentRequired, false, false);
						}
						theAppMgr.setWorkInProgress(false);
					}
					
					if(responseArrayinAuthorize!=null){
						/*Vivek Mishra : Added for capturing the AJB Response*/
						ajbResponse = responseArrayinAuthorize;
						//End

					int length = responseArrayinAuthorize.length;
					boolean retryConnect = false;
					
				a:for (int i=0; i<length ;i++){
						//Vivek Mishra : Changed the condition to check for status 10 instead of 4 in case of AJB servers are down.
						//Retry and Return to tender List message comes when AJB cant connect to Verifone device ---> need to get response code for that 
						//What message to be shown when 106 is down that is AJB is down 
						for (int k = 0; k < payments.length; k++) {
							
						
							if(!payments[k].getRespStatusCode().equals("0")  ){ //Added this check as we dont want to execute this loop for already completed payment 
							
							if(responseArrayinAuthorize[i] != null && (responseArrayinAuthorize[i].toString().contains("All the Ajb Servers"))){
							fiPayOffline = true;
								
							if(fiPayOffline){
						
								//Vivek Mishra : Added changes to show dialouge box for Debit card not supported in offline mode.
								
								if(!payments[k].getRespStatusCode().equals("0")){
									
									fiPayOffline = false;
									
								if(payments[k] instanceof CMSDebitCard){
								
									theAppMgr.showErrorDlg("Debit cards are not supported in offline mode.");
									paymentDesc=null;
									returnToOfflineTenderList();
									break a;
									}
								
								if(payments[k] instanceof BankCheck){
									Check chk = (Check)payments[k];
									chk.setAuthorizeOffline(true);
										authorize = false;
										return false;
									 }

								//Ends
								//Vishal : added for offline payment by giftcard 23 sept 2016
								if(payments[k] instanceof CMSStoreValueCard || payments[k] instanceof CMSDueBill ||payments[k] instanceof CMSDueBillIssue)
								{
									CommunicationErrorDlg offlineDlg = null;
									 offlineDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,true,"Press Retry "
												+ "to reconnect, otherwise please charge the"
		 			    	            		+ " card on the Mobile Terminal device and once payment is approved use the mobile "
		 			    	            		+ "terminal button to post the transaction");
									 offlineDlg.setVisible(true);
					 					if(offlineDlg.isManual()){
					 						PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "Manual GiftCard Entry", 0));
					 						paymentDesc="CMSSTOREVALUECARD";
					 						//isMobileTerminal = true;
					 						authorize = false;
					 						return false;

					 					}else if(offlineDlg.isRetry()){
					 						//If clicked on Retry , send 100 request again
					 						authorize();
					 					}
					 					else if(offlineDlg.isCancel()){
					 						returnToTenderList();
											return false;
					 					}else {
					 						returnToTenderList();
					 						return false;
					 					}
								} //end vishal 23 sept 2016
								if(payments[k] instanceof CreditCard)
								{
									
									String error_display_text = ((CreditCard)payments[k]).getErrordiscription();
									CommunicationErrorDlg offlineDlg = null;
									//Vivek Mishra : Added Mobile terminal option while POS receive response other than 0 or timeout as asked by Jason 25-APR-2016
									if ( error_display_text != null  && !error_display_text.equals("") && error_display_text.length() >0  )
									{
										
									offlineDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,true,error_display_text);
				 				
									}
									else
									{
									
										offlineDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,true,"Press Retry "
														+ "to reconnect, otherwise please charge the"
				 			    	            		+ " card on the Mobile Terminal device and once payment is approved use the mobile "
				 			    	            		+ "terminal button to post the transaction"); 
										
									
									}//Ends
			 						
									offlineDlg.setVisible(true);	
			 					if(offlineDlg.isManual()){
			 					
			 						PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "Mobile Terminal", 0));
			 						//Vivek Mishra : Commented to overcome Mobile terminal flag remains true after adding mobile tender
			 						isMobileTerminal = true;
			 						return false;
			 							 
			 					}else if(offlineDlg.isRetry()){
			 						//If clicked on Retry , send 100 request again
			 						authorize();
			 					}
			 					else if(offlineDlg.isCancel()){
			 						returnToTenderList();
									return false;
			 					}else {
			 						returnToTenderList();
			 						return false;
			 					}
								}
							}//if(!payments[k].getRespStatusCode().equals("0")){
			 				//return false;
						//}
					}//fiPayOffline
				}//responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers"))
			//if(!payments[k].getRespStatusCode().equals("0")		
				
			else if(!fiPayOffline){
				
				
				if(!payments[k].getRespStatusCode().equals("0")){ //already executed code should not execute as response array comes as null for already executed ones 
					
					 if( payments[k] instanceof CreditCard)
					 {
						 String error_display_text = ((CreditCard)payments[k]).getErrordiscription();
							
					 if(responseArrayinAuthorize[i] != null && responseArrayinAuthorize[i].equals("1"))
					 {
								
								cardDecline =true;
								returnPaymentDeclined();
								break a;
					 }
					 
					 //If response is 8 that means pinpad is timedout after swipe:eg:- not accepting the amount on pinpad
					//mayuri edhara :: added response code 3 and 2 to try again as the safable flag is not given in the response.
					 else if(responseArrayinAuthorize[i] != null && ((responseArrayinAuthorize[i].equals("8")) || 
							 ((responseArrayinAuthorize[i].equals("2") || responseArrayinAuthorize[i].equals("3") || responseArrayinAuthorize[i].equals("10")) && !payments[k].isSAFable())))
					 {
						 returnToTenderList();
						 theAppMgr.showErrorDlg(res.getString(error_display_text));
						 
							break a;
						}
					 // Respone Auth code for time out 
					 else if(responseArrayinAuthorize[i] != null && (responseArrayinAuthorize[i].equals("10") && payments[k].isSAFable()))
					 {
						 //Mayuri Edhara : Modified for Debit cards issue 17-NOV-2016
						 if (payments[k] instanceof CreditCard || payments[k] instanceof CMSDebitCard)
							{
							 
							//Mayuri Edhara :: 13-JAN-2017 - COMMENTED the changes pertaining to TOR.
							//sendTimeOutReversalRequest(payments[k]);	
								returnToTenderList();
								 theAppMgr.showErrorDlg(res.getString(error_display_text));
								 
									break a;
							}
						 
						}						
						//mayuri edhara :: added response code 3 and 2 for manual auth and safable flag
					 else if(responseArrayinAuthorize[i] != null && ((responseArrayinAuthorize[i].equals("2") || responseArrayinAuthorize[i].equals("3")) && payments[k].isSAFable()) && !payments[k].getRespStatusCode().equals("0")){
							//Vivek Mishra  : Added to show  Debit cards are not supported in offline mode for bank link down.
							if(payments[k] instanceof CreditCard && ((CreditCard)payments[k]).getProcessedTyp()!=null &&((CreditCard)payments[k]).getProcessedTyp().equalsIgnoreCase("Debit"))
							{
								retryConnect = theAppMgr.showOptionDlg(res.getString("Offline Error")
		 			    	            , res.getString("Debit cards are not supported in offline mode."), res.getString("Retry"), res.getString("Cancel"));
								
                                if(retryConnect)
                                {
                                	authorize();
                                }	
                                else
                                {
                                	returnToTenderList();
    								return false;
                                }
							}
							else
							{
							communicationErrorDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,false,null);
								
							communicationErrorDlg.setVisible(true);
								
								if (communicationErrorDlg.isRetry()) {
									authorize();
								} 
								
								else if (communicationErrorDlg.isManual()) {
									//Mayuri Edhara :: Commented the below as we only show 1 dialog 20-OCT-2016
									/*theAppMgr.showErrorDlg(res
											.getString("Authorization failed on one or more of the Card payments. Please click on manual override button and call for authorization number"));
				*/					manualOnCommunicationError = true;
									authorize = false;
									break a;
								}
								
							 else if (communicationErrorDlg.isCancel()) {
								returnToTenderList();
								return false;
							 }else {
								 returnToTenderList();
								 return false;
							 }
					         }
						}
					 //Vivek Mishra : Added for handling other response codes.
					 else{
						 returnToTenderList();
						 //theAppMgr.showErrorDlg(res.getString("Improper response received from AJB"));
						 theAppMgr.showErrorDlg(res.getString(error_display_text));
							break a;
					 	}
								
						}
					
						if(payments[k] instanceof BankCheck){
					 	 authorize = false;
						 return false;
						}
									}	//if(!payments[k].getRespStatusCode().equals("0")){
							} //if(!fiPayOffline){
							}	//	if(!payments[k].getRespStatusCode().equals("0")){ //Added this check as we dont want to execute this loop for already completed payment 
				
							//Vivek Mishra : Added for Mobile terminal return flow
						    if(payments[k].getRespStatusCode().equals("0") && overrideReturn && delRow!=-1)
						    {	
							model.removeRow(delRow);
							delRow = -1;
						    }

							//Vivek Mishra : Added to fix null pointer exception in case of refund SAF scenario 13-OCT-2016
						    if(((CreditCard)payments[k]).getCreditCardHolderName()==null)
						    {
						    ((CreditCard)payments[k]).setCreditCardHolderName("");
						    }
						    //Ends here 13-OCT-2016
						    
						    //Anjana added : During partial approval, cashier gets a message: card is only partially approved and customer has to use another tender type
						    if(payments[k].getRespStatusCode().equals("0") && payments[k] instanceof CreditCard && !(((CreditCard)payments[k]).isPartialAuth()) && !(((CreditCard)payments[k]).getCreditCardHolderName().equals("Mobile")))
						    {
						    	((CreditCard)payments[k]).setPartialAuth(true);
						    	CreditCard cc = (CreditCard)payments[k];
						    	ArmCurrency respAmt = cc.getAmount().absoluteValue();
								if (reqAmt  != null)
								{
							    	ArmCurrency remainingAmt = reqAmt.subtract(respAmt);
							    	ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
							    	if(reqAmt.greaterThan(respAmt)){
										theAppMgr.showErrorDlg(res.getString("Partially approved ")+(respAmt.formattedStringValue()) + res.getString(". Please use other tender for remaining amount ")
																+ (amtDue.formattedStringValue()));
										break a;
									}
								}
						    }
						   
						}//	for (int k = 0; k < payments.length; k++) {
					    //Ends
				}//for (int i=0; i<length ;i++){
				}//if(responseArray!=null)
			}//	if(!storeCountry.equals("CAN")){
		}// fipay flag	
				// Made change in checking the Error Condition:StatusID[i]!=null(Megha)
				if (responseArrayinAuthorize == null) {
					return true;
				}
				
				//adding null check in case of Canada reponse array is always null and POS is throwing null pointer exception
				//Vivek Mishra : Added condition in order to not to check for Mobile terminal tender type
				if(responseArrayinAuthorize != null && !isMobileTerminal){
				for (int i = 0; i < len; i++) {
					if (responseArrayinAuthorize[i] != null) {
						result = result + responseArrayinAuthorize[i];
					} else if (responseArrayinAuthorize[i] == null) {
						//Vivek Mishra : Changed for avoding array index out of bounds exception after removing extra 0 on reesponse code.
						//result = result + "00";
						result = result + "0";
					}
					//Vivek Misra : Added to show Signature to the cashier from approval
					
					showSignature(payments[i]);
				   //End
				}}
				//}//ISD changes else commented
				
				for (Enumeration enm = theTxn.getPayments(); enm.hasMoreElements();) {
					Payment pay = (Payment) enm.nextElement();
					if (pay.isAuthRequired()) {
						//1940
						updateLabels();
						return (false);
					}
				}
			}
			//1940
			//Dated :11/17/2014 :-Ruchi commenting this call for updateLables because on payment screen Change due was coming $0.0
			//updateLabels();
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			ArmCurrency amtDue = new ArmCurrency(0.0d);
			if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
			}
			//Ends
			else
				amtDue = theTxn.getCompositeTotalAmountDue();
			amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
			if(amtDue.doubleValue()>0.0){
				 return false;
			}
			//1940 finish
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}
		
		finally{
			doNotValidate = false;
			overrideReturn = false;
			isMobileTerminal = false;	
		}
	}

	private boolean authorize(Payment payment) 
	{
		String responseAuthorize =null;
		String responseajb = null;
		boolean isfiPayOffline = false;
		
		try {
			
			result = "0";
			isRefundPaymentRequired = false;
			 PaymentTransaction paymtTxn = theTxn.getPaymentTransaction();
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				ArmCurrency amountDue = new ArmCurrency(0.0d);
				if ( (theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel))  && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amountDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
				}
				//Ends
				else
					amountDue = theTxn.getCompositeTotalAmountDue();
		        if(paymtTxn instanceof CompositePOSTransaction  && !(amountDue.greaterThan(new ArmCurrency(0.0))))
		        {
		            POSLineItem lines[] = ((CompositePOSTransaction)paymtTxn).getLineItemsArray();
		            for(int i = lines.length - 1; i >= 0; i--){
		                if((lines[i] instanceof CMSReturnLineItem)){
		                	isRefundPaymentRequired = true;
		                	  break;
		                }
		            }
		        } 
		      //Mayuri added check for paid out transaction.
		        if(!(payment instanceof CreditCard) && (paymtTxn instanceof CMSPaidOutTransaction || paymtTxn instanceof CMSRedeemableBuyBackTransaction )){
		            isRefundPaymentRequired = true;
		           }
		        //Vivek Mishra : Changed the condition to include CMSRedeemableBuyBackTransaction 19-OCT-2016
		        if((payment instanceof CreditCard) && (((paymtTxn instanceof CMSPaidOutTransaction) && !(amountDue.greaterThan(new ArmCurrency(0.0))) || paymtTxn instanceof CMSRedeemableBuyBackTransaction))){
		            isRefundPaymentRequired = true;
		           }
		           // end
		        //vishal yevale 25 oct 2016
		        if((fipayGiftcardFlag.equalsIgnoreCase("Y")) && !(payment instanceof CMSStoreValueCard || payment instanceof CMSDueBill
						|| payment instanceof CMSDueBillIssue) && (paymtTxn instanceof CMSPaidOutTransaction || paymtTxn instanceof CMSRedeemableBuyBackTransaction )){
		            isRefundPaymentRequired = true;
		           }
		        if((fipayGiftcardFlag.equalsIgnoreCase("Y")) && (payment instanceof CMSStoreValueCard || payment instanceof CMSDueBill
						|| payment instanceof CMSDueBillIssue) && (paymtTxn instanceof CMSPaidOutTransaction) && !(amountDue.greaterThan(new ArmCurrency(0.0)))){
		            isRefundPaymentRequired = true;
		           }
		        // end vishal 25 oct 2016
		        
		      //Mayuri Edhara :: 02-09-2017 - Current Gift card flow (No AJB GIFT CARD INTEGRATION) should not loop into validate.
				if((payment instanceof Redeemable) && !(payment instanceof CMSStoreValueCard || payment instanceof CMSDueBill
						|| payment instanceof CMSDueBillIssue)){
					doNotValidate = true;
				}
		        
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			Payment[] payments = null;
			if(OrgSaleTxn!=null && !overrideReturn)
			{
				payments = OrgSaleTxn.getPaymentsArray();
			}
			else
			{
				payments = theTxn.getPaymentsArray();
			}
			int len = payments.length;
			if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) &&  (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel))  && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amountDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
			}
			else
				amountDue = theTxn.getCompositeTotalAmountDue();
			
			if (amountDue.greaterThan(new ArmCurrency(0.0))||isRefundPaymentRequired||isForced)
			{
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				//Vivek Mishra : Changed the condition for allowing mobile terminal to work in US region as well
				//if(!storeCountry.equals("CAN"))
				
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){

				if(!storeCountry.equals("CAN") && !isMobileTerminal){
					//ISD call commented for AJBImplementation by Sonali
					//	responseArray = CMSCreditAuthHelper.validate(theAppMgr, theTxn.getPaymentTransaction(), register.getId());
					//end:ISD call commented
					
					//Mayuri Edhara :: 02-08-2017 - Current Gift card flow (No AJB GIFT CARD INTEGRATION) should not loop into validate.
					if((fipayGiftcardFlag==null || fipayGiftcardFlag.equalsIgnoreCase("N")) && (payment instanceof CMSStoreValueCard || payment instanceof CMSDueBill
							|| payment instanceof CMSDueBillIssue)){
						doNotValidate = true;
					} 
						
					//Vivek Mishra : Changed the validate method call for AJB
				//Added For AJB-POS offline 
				
					//Anjana added For manual override send a 111 SAF request
					if(!isManualOverride && !doNotValidate)
					{
						theAppMgr.setWorkInProgress(true);
						if(OrgSaleTxn!=null && !overrideReturn)
						{
							//responseArrayinAuthorize = CMSCreditAuthHelper.validate(theAppMgr, OrgSaleTxn, register.getId(), isRefundPaymentRequired, false, false);
							logger.info("DEBUG-PROD-ISSUE:::::to check Void parameter 333:::");
							responseAuthorize = CMSCreditAuthHelper.validate(theAppMgr, OrgSaleTxn, payment,register.getId(), isRefundPaymentRequired, false, false, theAppMgr);
							
						}else
						{
							//responseArrayinAuthorize = CMSCreditAuthHelper.validate(theAppMgr, theTxn.getPaymentTransaction(), register.getId(), isRefundPaymentRequired, false, false);
							//This will also print in normal 100 request
							logger.info("DEBUG-PROD-ISSUE:::::to check Void parameter 444:::");
							responseAuthorize = CMSCreditAuthHelper.validate(theAppMgr, theTxn.getPaymentTransaction(),payment, register.getId(), isRefundPaymentRequired, false, false, theAppMgr);
						}
						theAppMgr.setWorkInProgress(false);
					}
					// start from here ---- YK
					//vishal yevale : 21 dec 2016 
					if((fipayGiftcardFlag != null && fipayGiftcardFlag.equalsIgnoreCase("Y")) && payment instanceof CMSDueBillIssue && responseAuthorize.equalsIgnoreCase("UnableToActivate")){
						boolean retryConnect = theAppMgr
								.showOptionDlg(
										"",
										res.getString("Unable to Activate Giftcard Please Retry or use other Tender"),
										res.getString("Retry"),
										res.getString("Cancel"));
						
						if (retryConnect) 
						{
							//Vivek Mishra : added return to break the recursive calling properly 6-MAY-2016
							return authorize(payment);
						} else {
							returnToTenderList();
							return false;
						}
					}
					//end vishal yevale 21 dec 2016
					if(responseAuthorize != null)
					{
						/*Vivek Mishra : Added for capturing the AJB Response*/
						ajbResponse = responseArrayinAuthorize;
						responseajb = responseAuthorize;
						//End
						
						//Mayuri Edhara :: Only for testing offline flow
						//responseAuthorize = "All the Ajb Servers";

					boolean retryConnect = false;
					
					if(!payment.getRespStatusCode().equals("0"))
					{ //Added this check as we dont want to execute this loop for already completed payment 
						
						if(responseAuthorize != null && ( responseAuthorize.toString().contains("All the Ajb Servers")))
						{
							isfiPayOffline = true;
							if(isfiPayOffline)
							{								
								if(!payment.getRespStatusCode().equals("0"))
								{
									isfiPayOffline = false;
									if(payment instanceof CMSDebitCard)
									{
										theAppMgr.showErrorDlg("Debit cards are not supported in offline mode.");
										paymentDesc=null;
										returnToOfflineTenderList();
										return false;
									}
									if(payment instanceof BankCheck)
									{
										Check chk = (Check)payment;
										chk.setAuthorizeOffline(true);
										authorize = false;
										return false;
									}
									//Vishal Yevale : for giftcard tender Manual entry flow 28 sept 2016
									if(payment instanceof CMSStoreValueCard || payment instanceof CMSDueBill
											|| payment instanceof CMSDueBillIssue){
										CommunicationErrorDlg offlineDlg = null;
										//Anjana added one more arguement for GC communication error 
										 offlineDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,false,true,"Press Retry"
													+ " to reconnect, Manual Auth button to call bank for authorization number, or Cancel.");
										 offlineDlg.setVisible(true);
										 if(offlineDlg.isManual())
											{
											 	gcPayment=payment;
											 	PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "Manual GiftCard Entry", 0));
											 	paymentDesc="CMSSTOREVALUECARD";
											 	//isMobileTerminal = true;
											 	authorize = false;
											 	return false;

											}else if(offlineDlg.isRetry()){
												//If clicked on Retry , send 100 request again
												//Vivek Mishra : added return to break the recursive calling properly 6-MAY-2016
												return authorize(payment);
											}
											else if(offlineDlg.isCancel()){
												returnToTenderList();
												return false;
											}else{
												returnToTenderList();
												return false;
											}
										 
									}
									//end Vishal Yevale
									if(payment instanceof CreditCard)
									{
										
										String error_display_text = ((CreditCard)payment).getErrordiscription();
										CommunicationErrorDlg offlineDlg = null;
										
										if ( error_display_text != null  && !error_display_text.equals("") && error_display_text.length() >0  )
										{
											
											offlineDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,true,error_display_text);
					 				
										}
										else
										{
											
											offlineDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,true,"Press Retry"
															+ " to reconnect, otherwise please charge the"
					 			    	            		+ " card on the Mobile Terminal device and once payment is approved use the mobile "
					 			    	            		+ "terminal button to post the transaction");
											
										}
										
										offlineDlg.setVisible(true);	
										if(offlineDlg.isManual())
										{
											
				 						PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "Mobile Terminal", 0));
				 						//Vivek Mishra : Commented to overcome Mobile terminal flag remains true after adding mobile tender
				 						isMobileTerminal = true;
				 						return false;
				 							 
										}else if(offlineDlg.isRetry()){
											//If clicked on Retry , send 100 request again
											//Vivek Mishra : added return to break the recursive calling properly 6-MAY-2016
											return authorize(payment);
										}
										else if(offlineDlg.isCancel()){
											returnToTenderList();
											return false;
										}else{
											returnToTenderList();
											return false;
										}
									}
								}
							}
						}
						else if(!isfiPayOffline)
						{
							if (!payment.getRespStatusCode().equals("0"))
							{ 
								if (payment instanceof CreditCard)
								{
									String error_display_text = ((CreditCard) payment).getErrordiscription();
									System.out.println("");System.out.println("");
									System.out.println("");
									System.out.println(error_display_text + "                    "    + "response from  AJB........." );
									//mayuri edhara: removed the response code 3
									if (responseAuthorize != null && responseAuthorize.equals("1")) 
									{
										cardDecline = true;
										returnPaymentDeclined(payment);
										return false;
										//break a;
									}
									else if (responseAuthorize != null && responseAuthorize.equals("8"))
									{
										returnToTenderList();
										theAppMgr.showErrorDlg(res.getString(error_display_text));
										return false;
										
									}else if(responseAuthorize != null && (responseAuthorize.equals("2") || responseAuthorize.equals("3"))  
											//|| responseAuthorize.equals("10") //Mayuri Edhara :: 13-JAN-2017 - COMMENTED the no TOR is sent.
										
											&& !(payment.isSAFable())){
										//Vivek Mishra : Added check for debit cards 13-OCT-2016
										//Mayuri Edhara : Modified for Debit cards issue 17-NOV-2016
										if (payment instanceof CreditCard || payment instanceof CMSDebitCard){
											returnToTenderList();
											theAppMgr.showErrorDlg(res.getString("Payment cannot be processed as connectivity to Vantiv cannot be established and Manual authorization is not allowed. "
													+ "Please choose another payment method or try again."));
											return false;
										}
										
									}//Mayuri Edhara :: 13-JAN-2017 - COMMENTED the SAFable Flag as no TOR is sent.
									else if (responseAuthorize != null && responseAuthorize.equals("10") //&& payment.isSAFable() 
											&& !payment.getRespStatusCode().equals("0")) 
									{

										//Mayuri Edhara : Modified for Debit cards issue 17-NOV-2016
										if (payment instanceof CreditCard || payment instanceof CMSDebitCard)
										{
											//Mayuri Edhara :: 13-JAN-2017 - COMMENTED the changes pertaining to TOR. 
											//sendTimeOutReversalRequest(payment);
											returnToTenderList();
											theAppMgr.showErrorDlg(res.getString(error_display_text));
											return false;
										} 
									}
									//mayuri edhara: added the response code 3  and 2 check for SAF.
									else if (responseAuthorize != null && ((responseAuthorize.equals("2") || responseAuthorize.equals("3")) && payment.isSAFable())&& !payment.getRespStatusCode().equals("0")) 
									{
										if (payment instanceof CreditCard && ((CreditCard) payment).getProcessedTyp() != null && ((CreditCard) payment).getProcessedTyp().equalsIgnoreCase("Debit"))
										{
											retryConnect = theAppMgr
													.showOptionDlg(
															res.getString("Offline Error"),
															res.getString("Debit cards are not supported in offline mode."),
															res.getString("Retry"),
															res.getString("Cancel"));
											
											if (retryConnect) 
											{
												//Vivek Mishra : added return to break the recursive calling properly 6-MAY-2016
												return authorize(payment);
											} else {
												returnToTenderList();
												return false;
											}
										} else
										{
											communicationErrorDlg = new CommunicationErrorDlg(CMSApplet.theAppMgr.getParentFrame(),CMSApplet.theAppMgr,false,null);
											communicationErrorDlg.setVisible(true);
											
											if (communicationErrorDlg.isRetry()) {
												//Vivek Mishra : added return to break the recursive calling properly 6-MAY-2016
												return authorize(payment);
											} 
											
											else if (communicationErrorDlg.isManual()) {
												//Mayuri Edhara :: Commented the below as we only show 1 dialog 20-OCT-2016	
												/*theAppMgr.showErrorDlg(res
												.getString("Authorization failed on one or more of the Card payments. Please click on manual override button and call for authorization number"));
					*/							manualOnCommunicationError = true;
												authorize = false;
												//Vivek Mishra : added to return authorize status in order to stop going further in this method
												return authorize;
												//Ends here
											}
											else if (communicationErrorDlg.isCancel()) {
												returnToTenderList();
												return false;
											}
										}
									} else
									{
										returnToTenderList();
										theAppMgr.showErrorDlg(res.getString(error_display_text));
										//break a;
										return false;
									}

								}
								//vishal yevale giftcard 25 oct 2016
								//Vivek Mishra : To show error message for AJB response code other than 0. 01-NOV-2016
								if(payment instanceof Redeemable){
									String error_display_text= "";
									if(payment instanceof CMSStoreValueCard)
									{ 
										error_display_text = ((CMSStoreValueCard)payment).getErrordiscription();
									}
									else if(payment instanceof CMSDueBill)
									{
										error_display_text = ((CMSDueBill)payment).getErrordiscription();	
									}	
									else if(payment instanceof CMSDueBillIssue)
									{
										error_display_text = ((CMSDueBillIssue)payment).getErrordiscription();
									}	
											//" fipay is online but not received exact output ";
									

									//System.out.println(error_display_text + "                    "    + "response from  AJB........." );
									theAppMgr.showErrorDlg(error_display_text+". Please use another tender type.");
									returnToTenderList();
									return false;
									//Ends here 01-NOV-2016
																	
								} //end vishal yevale 25 oct 2016
								if (payment instanceof BankCheck)
								{
									authorize = false;
									return false;
								}
							} 
						}
					}	 
					if(payment.getRespStatusCode().equals("0") && overrideReturn && delRow!=-1 )
					{	
						model.removeRow(delRow);
						delRow = -1;
					}
					
					//Vivek Mishra : Added to fix null pointer exception in case of refund SAF scenario 13-OCT-2016
				    if(payment instanceof CreditCard && ((CreditCard)payment).getCreditCardHolderName()==null)
				    {
				    ((CreditCard)payment).setCreditCardHolderName("");
				    }
				    //Ends here 13-OCT-2016
					if(payment.getRespStatusCode().equals("0") && payment instanceof CreditCard && !(((CreditCard)payment).isPartialAuth()) && ((CreditCard)payment).getCreditCardHolderName() != null  )
				    {
						if(  !(((CreditCard)payment).getCreditCardHolderName().equals("Mobile")) )
						{
							((CreditCard)payment).setPartialAuth(true);
							CreditCard cc = (CreditCard)payment;
							ArmCurrency respAmt = cc.getAmount();
							if (reqAmt  != null)
							{
								ArmCurrency remainingAmt = reqAmt.subtract(respAmt);
								ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
								if(reqAmt.greaterThan(respAmt.absoluteValue())){
									theAppMgr.showErrorDlg(res.getString("Partially approved ")+(respAmt.formattedStringValue()) + res.getString(". Please use other tender for remaining amount ")
										//To show the correct remaining balance
											+ (amtDue.subtract(respAmt)).formattedStringValue());
								}
							}
						}
				    }
					//Ruchi adding check for random VOID requests #27968
					logger.info("DEBUG-PROD-ISSUE:::::amountdueCheck before if  "+amountdueCheck);
					if(payment.getRespStatusCode().equals("0") && payment instanceof CreditCard){
						amountdueCheck = false;
						CreditCard cc = (CreditCard)payment;
						ArmCurrency respAmt = cc.getAmount();
						ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
						if(respAmt.equals(amtDue)){
							//assuming that whole transaction amount is approved by AJB
							amountdueCheck = true;
							logger.info("DEBUG-PROD-ISSUE:::::amountdueCheck "+amountdueCheck);
						}
					}
					
					if(payment.getRespStatusCode().equals("0") && (payment instanceof CMSStoreValueCard || payment instanceof CMSDueBill
							|| payment instanceof CMSDueBillIssue))
				    {
						if(payment instanceof CMSStoreValueCard){
							CMSStoreValueCard cc = (CMSStoreValueCard)payment;
							if(!(cc.isPartialAuth())){
								ArmCurrency respAmt = cc.getAmount();
								if (reqAmt  != null)
								{
									ArmCurrency remainingAmt = reqAmt.subtract(respAmt);
									ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
									if(reqAmt.greaterThan(respAmt.absoluteValue())){
										theAppMgr.showErrorDlg(res.getString("Partially approved ")+(respAmt.formattedStringValue()) + res.getString(". Please use other tender for remaining amount ")
											//To show the correct remaining balance
												+ (amtDue.subtract(respAmt)).formattedStringValue());
									}
								}
							}
						}
						if(payment instanceof CMSDueBill){
							CMSDueBill cc = (CMSDueBill)payment;
							if(!(cc.isPartialAuth())){
								ArmCurrency respAmt = cc.getAmount();
								if (reqAmt  != null)
								{
									ArmCurrency remainingAmt = reqAmt.subtract(respAmt);
									ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
									if(reqAmt.greaterThan(respAmt.absoluteValue())){
										theAppMgr.showErrorDlg(res.getString("Partially approved ")+(respAmt.formattedStringValue()) + res.getString(". Please use other tender for remaining amount ")
											//To show the correct remaining balance
												+ (amtDue.subtract(respAmt)).formattedStringValue());
									}
								}
							}
						}
						if(payment instanceof CMSDueBillIssue){
							CMSDueBillIssue cc = (CMSDueBillIssue)payment;
							if(!(cc.isPartialAuth())){
								ArmCurrency respAmt = cc.getAmount();
								if (reqAmt  != null)
								{
									ArmCurrency remainingAmt = reqAmt.subtract(respAmt);
									ArmCurrency amtDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
									if(reqAmt.greaterThan(respAmt.absoluteValue())){
										theAppMgr.showErrorDlg(res.getString("Partially approved ")+(respAmt.formattedStringValue()) + res.getString(". Please use other tender for remaining amount ")
											//To show the correct remaining balance
												+ (amtDue.subtract(respAmt)).formattedStringValue());
									}
								}
							}
						}}
					}//if(responseArray!=null)
				}//	if(!storeCountry.equals("CAN")){
			} // fipay flag
				// Made change in checking the Error Condition:StatusID[i]!=null(Megha)
				if (responseAuthorize == null )
				{
					return false;
				}

				//adding null check in case of Canada reponse array is always null and POS is throwing null pointer exception
				//Vivek Mishra : Added condition in order to not to check for Mobile terminal tender type
				if(responseAuthorize != null && !isMobileTerminal)
				{
					if ( responseAuthorize != null) 
					{
						result = result + responseAuthorize ;
					} 
					showSignature(payment);
				}
				//}//ISD changes else commented
				
				for (Enumeration enm = theTxn.getPayments(); enm.hasMoreElements();) 
				{
					Payment pay = (Payment) enm.nextElement();
					//Mayuri Edhara :: commented the below as the isAuthRequired is made to false in the authorization object.
//					//vishal added code 8 nov 2016					 
//					if(pay instanceof Redeemable){
//						if (!pay.isAuthRequired()){
//							//1940
//							updateLabels();
//							return (false);
//							 }
//					} //end vishal
//					else
//					{
						 if (pay.isAuthRequired()){
						//1940
						updateLabels();
						return (false);
						 }
					//}
				}
			}
			//1940
			//Dated :11/17/2014 :-Ruchi commenting this call for updateLables because on payment screen Change due was coming $0.0
			//updateLabels();
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			ArmCurrency amtDue = new ArmCurrency(0.0d);
			if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
			}
			//Ends
			else
				amtDue = theTxn.getCompositeTotalAmountDue();
			amtDue = amtDue.subtract(theTxn.getTotalPaymentAmount());
			/*if(amtDue.doubleValue()>0.0){
				 return false;
			}*/
			//1940 finish
			return (true);
		}catch (SocketException e)
 {
			theAppMgr.showErrorDlg(res.getString("Payment Device is Offline. Please charge the"
									+ " card on the Mobile Terminal device and once payment is approved use the mobile "
									+ "terminal button to post the transaction"));
        	theAppMgr.setWorkInProgress(false);
			return (false);
		}
		catch (IOException io)
		{
			if (io instanceof SocketException)
			{
				theAppMgr.showErrorDlg(res.getString(io.getMessage()));
				theAppMgr.setWorkInProgress(false);
				return (false);
				
			} else
			{
				theAppMgr.showErrorDlg(res.getString("Payment Device is Offline. Please charge the"
						+ " card on the Mobile Terminal device and once payment is approved use the mobile "
						+ "terminal button to post the transaction"));
				theAppMgr.setWorkInProgress(false);
				return (false);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			theAppMgr.setWorkInProgress(false);
			return (false);
		}
		
		finally{
			doNotValidate = false;
			overrideReturn = false;
			isMobileTerminal = false;	
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
		if( model.getRowCount() != 0 && absoluteRow != 0)
		  {
		   tblPayment.getSelectionModel().setSelectionInterval(absoluteRow % model.getRowCount(), absoluteRow % model.getRowCount());
		  }
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
		boolean status = false;
		if (!isPostComplete && !cancelTxnPressed) {
			try {
				//Added to not show the pop up to cancel the transaction and go back to home if cashier is in the middle of posting a transaction to DB
				if(!(theTxn.getTotalPaymentAmount().equals(theTxn.getCompositeTotalAmountDue()))){
					 status= (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Selecting 'Home' cancels the transaction.  Do you want to cancel this transaction?")));	
				}
			if (status) {
				if(cmsV12Basket!=null) {
					CMSV12BasketHelper.setBasketStatus(theAppMgr,
							cmsV12Basket, CMSV12Basket.open);
					theAppMgr.removeStateObject("V12BASKET_LOOKUP");
				} 
				//Vivek Mishra : Added for not allowing cashier to delete a tender in AJB offline scenario as asked by Phil in status call 06-AUG-15.
				logger.info("DEBUG-PROD-ISSUE:::deletePaymentCancel() called From isHomeAllowed()::status:::"+ status );
				boolean flag = deletePaymentCancel();
				logger.info("DEBUG-PROD-ISSUE:::deletePaymentCancel() cancelled from Home ::flag:::"+ flag );

				if(!flag)
				{
					return flag;
				}
			  }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return status;
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
		ArmCurrency amtDue = new ArmCurrency(0.0d);;
		//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
		//Fixed ClassCast Exception in case of Japanese Yen tender type
		if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ( (CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
				&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
			amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
		}
		//Ends
		else
			amtDue = theTxn.getCompositeTotalAmountDue();
		try {
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
	private boolean validateChangeAmount(ArmCurrency amt){
		try {
			PaymentTransaction txn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
			//CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
			CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) txn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			ArmCurrency amtLeft = new ArmCurrency(0.0d);
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			if (((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amtLeft = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
			}
			//Ends
			else
				amtLeft = appModel.getCompositeTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
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
	
					//For card on File Checking for card number null and mininum number of digits and returning credit card object. BIN lookup will be done using Fipay.
				thePayment = CreditCardBldrUtil.allocCreditCardObject(accountNum);
				//}
		 	if (thePayment == null) {
				theAppMgr.showErrorDlg(res.getString("The account number is invalid."));
				return;
			} else {
				CreditCard cc = (CreditCard) thePayment;
				//sonali:AJB commented for Card on file cc.setAccountNumber(creditCard.getCreditCardNumber());
				cc.setZipCode(creditCard.getBillingZipCode());
				cc.setExpirationDate(creditCard.getExpDate());
				//sonali:AJB added for Card on file
				cc.setTokenNo(creditCard.getCardToken());
				cc.setAccountNumber(creditCard.getCreditCardNumber());
							
				if (thePayment.getAmount() == null) {
					try {
						PaymentTransaction txn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
						CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) txn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
						ArmCurrency amt = new ArmCurrency(0.0d);
						//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
						if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
								&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
							amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
						}
						//Ends
						else
							amt = appModel.getCompositeTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
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
				PaymentApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "VIEW_CREDIT_CARD", 0));
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
				PaymentApplet.this.appButtonEvent("CREDIT_CARD", new CMSActionEvent(this, 0, "USE_CARD_ON_FILE", 0));
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
		List paymentCodes = CMSPaymentMgr.getPaymentCode(paymentType);
		String cardCode = "";
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
	
	//Added by Vivek Mishra for US region Payment Code changes 
	/**
	 * put your documentation comment here
	 * @param paymentType
	 * @param paymentDesc
	 * @return
	 */
	private String getPaymentCode(String paymentType, String paymentDesc) {
		List paymentCodes = CMSPaymentMgr.getPaymentCode(paymentType);
		if(paymentType.equalsIgnoreCase("BCRD") || paymentType.equalsIgnoreCase("AMEX") || paymentType.equalsIgnoreCase("JCB") || paymentType.equalsIgnoreCase("DINERS")){
	   
			paymentCodes = CMSPaymentMgr.getPaymentCode("CREDIT_CARD");
	  	}
		String cardCode = "";

		int numPaymentCodes = paymentCodes.size();
		if (numPaymentCodes == 0)
			return cardCode;
		if (numPaymentCodes == 1) {
			CMSPaymentCode paymentCode = (CMSPaymentCode) paymentCodes.get(0);
			cardCode = paymentCode.getPaymentCode();
		} else {
			CMSPaymentCode[] payCodes = new CMSPaymentCode[numPaymentCodes];
			//GenericChooserRow[] availPaymentCodes = new GenericChooserRow[numPaymentCodes];
			String tempDesc = "";
			for (int i = 0; i < numPaymentCodes; i++) {
				CMSPaymentCode paymentCode = (CMSPaymentCode) paymentCodes.get(i);
				tempDesc = (paymentCode.getPaymentDesc()).replaceAll("_", "");
				if((paymentDesc).contains(tempDesc.toUpperCase()))
				{
					cardCode=paymentCode.getPaymentCode();
					break;
				}
			}
		}
		return cardCode;
	}

	/**
	 * put your documentation comment here
	 */
	private void setCustomerLoyalty(CMSCompositePOSTransaction posTxn) {
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
						return;
					}
				} catch (BusinessRuleException ex) {
					//ex.printStackTrace();
				}
			}
			setLoyalty(posTxn, null);
			
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
				//Fix for 861: Loyalty:  Initiate loyalty when customer is added to transaction
				posTxn.update();
				pnlHeader.updateAmtToReward();
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
	
	
	
	//Added by Sonali to Check if manual authorization is allowed
	//Manual Auth is allowed when AJB server is down or when no response or timeOut is received from AJB
	//Removed the response code 10 as time out is not manual Auth
	boolean isManualAuthRequired()
	{
		
	         	if (ajbResponse!=null){
		     for (int i=0; i<ajbResponse.length ;i++){
		    	 //AJB handles time out scenario
				if(ajbResponse!=null && ajbResponse[i]!=null && ((ajbResponse[i].toString().contains("All the Ajb Servers are down at the moment"))))
					{
		          return true;
              	}
		}
		}
	         	return false;
		}
	
	//Added by Anjana to send a POS_REFRESH 150 request after posting the transaction
 	private boolean sendItemMessageData(POSLineItem line, POSLineItem[] lineItemArray ,boolean Refresh, boolean idleMessage,boolean clearMessage,String discountAmt,boolean fromPayment) {
 		try {
 			String result = "";
 			//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
 			//int qty = theTxn.getLineItemsArray().length;
 			//End
 			String responseArray[] = null;
 			String ajbResponse[] = null;
 			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
 			
 				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
 				//Changes for Canada validate method needs to pass
 				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
 				if(!storeCountry.equals("CAN")){
 					//String tt = orgTxn.getTransactionType();
 			
 	 				responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line, lineItemArray,Refresh, idleMessage, clearMessage,discountAmt,fromPayment);

 					if(responseArray!=null){
 						ajbResponse = responseArray;
 						//End
 					int length = responseArray.length;
 					for (int i=0; i<length ;i++){	
 						if(responseArray[i]!=null)
 					if(responseArray[i] != null && responseArray[0].toString().contains("All the Ajb Servers")){
 					//	theAppMgr.showErrorDlg("All the AJB servers are down");
 					}
 						return false;
 				
 					}
 					}
 				}else
 				
 				if (responseArray == null) {
 					return true;
 				}
 				return (true);
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			theAppMgr.showExceptionDlg(ex);
 			return (false);
 		}

   }
   //End
 	
 	//Added by Anjana to return to tender list
	private void returnToTenderList() {
		returntotender = true;

		try {
			Payment[] payments = theTxn.getPaymentsArray();
			for (int i = 0; i < payments.length; i++) {
				//Vivek Mishra : Changed the condition to add Redeemable in the flow 01_NOV-2016
				 if ((payments[i] instanceof CreditCard || payments[i] instanceof Redeemable) && !payments[i].getRespStatusCode().equals("0")) {
					if (theAppMgr.getStateObject("MAX_CHANGE") != null) {
						theAppMgr.removeStateObject("MAX_CHANGE");
					}
					//Added to not delete tender if offline mode in case of split tender 
					theTxn.removePayment(payments[i]);
					model.removeRowInModel(payments[i]);
					model.fireTableDataChanged();
				}

			}
			updateLabels();
			tblPayment.repaint();
			initButtons(CMSPaymentMgr.PAYMENT);
			selectPayment();
			return;
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
		}
	}
 	
 	//Added by Anjana to return to tender list on offline scenario
	private void returnToOfflineTenderList() {
		
		returntotender = true;
		try{
		Payment[] payments = theTxn.getPaymentsArray();
		for (int i = 0; i < payments.length; i++) {
			if (payments[i] instanceof CreditCard && !payments[i].getRespStatusCode().equals("0")) {
				if (theAppMgr.getStateObject("MAX_CHANGE") != null) {
					theAppMgr.removeStateObject("MAX_CHANGE");
				}
				//Vivek Mishra : Modified to exclude the expiration date criteria to remove credit card in AJB offline flow
				//if(!doNotValidate && ((CreditCard)payments[i]).getExpirationDate()!=null){
				if(!doNotValidate){
					theTxn.removePayment(payments[i]);
					model.removeRowInModel(payments[i]);
					model.fireTableDataChanged();
				}
			
			}

		}
		updateLabels();
		tblPayment.repaint();
		//Anjana added to show the menu corresponding to offline return
		 ArmCurrency amtVal = null;
		   try {
		  amtVal = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
		if(OrgSaleTxn!=null && amtVal.lessThan(new ArmCurrency(0.0))){
			theAppMgr.showMenu(MenuConst.OK_PREVIOUS, theOpr);
			theAppMgr.setSingleEditArea(res.getString("Press Refund to initiate return and post"));
		}else {
			initButtons(CMSPaymentMgr.PAYMENT);
		}
		   } catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		offlineMode = true;	
		selectPayment();
		return;
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
	}

	//Added by Anjana to return to tender list
		private void returnPaymentDeclined() {
			returntotender = true;
			try {
				Payment[] payments = theTxn.getPaymentsArray();
				//Added for returns
				if(OrgSaleTxn!=null && !overrideReturn){
					 payments = OrgSaleTxn.getPaymentsArray();
				}
				//Ends
				for (int i = 0; i < payments.length; i++) {
					//For card decline scenario
					//Decline both 1
					 if (payments[i].getRespStatusCode().equals("1")) {
						if (theAppMgr.getStateObject("MAX_CHANGE") != null) {
							theAppMgr.removeStateObject("MAX_CHANGE");
						}
						
						String display_txt = "The "+ payments[i].getGUIPaymentDetail() +" card  was declined";

						if(payments[i] instanceof CreditCard)
						{
							String error_display_text = ((CreditCard)payments[i]).getErrordiscription();
							{
								if ( error_display_text != null  && !error_display_text.equals("") && error_display_text.length() >0  )
								{
									//Mayuri Edhara :: added custom msg.
									if(error_display_text.contains("CALL OPER")){
										display_txt = "Please have the client call the number on the back of the card and retry the transaction on the pinpad.";
									}else{
										display_txt = error_display_text ;
									}
									
								}
								
							}
						}
						theAppMgr.showErrorDlg(res.getString(display_txt));
						
						theTxn.removePayment(payments[i]);
						model.removeRowInModel(payments[i]);
						model.fireTableDataChanged();
					}
					
				}
				updateLabels();
				tblPayment.repaint();
				initButtons(CMSPaymentMgr.PAYMENT);
				selectPayment();

				return;
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
		}
		
		private void returnPaymentDeclined(Payment payment) 
		{
			returntotender = true;
			try 
			{
				Payment[] payments = theTxn.getPaymentsArray();
				//Added for returns
				if(OrgSaleTxn!=null && !overrideReturn){
					payments = OrgSaleTxn.getPaymentsArray();
				}
				//Ends
				//for (int i = 0; i < payments.length; i++) 
				{
					//For card decline scenario
					//Decline both 1
					//mayuri edhara :  removed the declined 3 as we send SAF
					if ( payment.getRespStatusCode().equals("1"))
					{
						if (theAppMgr.getStateObject("MAX_CHANGE") != null)
						{
							theAppMgr.removeStateObject("MAX_CHANGE");
						}
						
						String display_txt = "The "+ payment.getGUIPaymentDetail() +" card  was declined";

						if(payment instanceof CreditCard)
						{
							String error_display_text = ((CreditCard)payment).getErrordiscription();
							{
								if ( error_display_text != null  && !error_display_text.equals("") && error_display_text.length() >0  )
								{
									//Mayuri Edhara :: added custom msg.
									if(error_display_text.contains("CALL OPER")){
										display_txt = "Please have the client call the number on the back of the card and retry the transaction on the pinpad.";
									}else{
										display_txt = error_display_text ;
									}
									
								}
								
							}
						}
						theAppMgr.showErrorDlg(res.getString(display_txt));
						//Vivek Mishra : Added to generate new RDO for EMV decline receipt
						if(payment instanceof CreditCard && ((CreditCard) payment).getAID()!=null){
						
						if(emvDecRdo.getStoreAddress()==null)
						{
							emvDecRdo.setStoreAddress(theTxn.getPaymentTransaction().getStore().getAddress());
						}
						if(emvDecRdo.getCity()==null)
						{
							emvDecRdo.setCity(theTxn.getPaymentTransaction().getStore().getCity());
						}
						if(emvDecRdo.getZipCode()==null)
						{
							emvDecRdo.setZipCode(theTxn.getPaymentTransaction().getStore().getZipCode());
						}
						if(emvDecRdo.getState()==null)
						{
							emvDecRdo.setState(theTxn.getPaymentTransaction().getStore().getState());
						}
						if(emvDecRdo.getAreaCode()==null)
						{
							emvDecRdo.setAreaCode(theTxn.getPaymentTransaction().getStore().getTelephone().getAreaCode());
						}
						if(emvDecRdo.getTelephoneNumber()==null)
						{
							emvDecRdo.setTelephoneNumber(theTxn.getPaymentTransaction().getStore().getTelephone().getTelephoneNumber());
						}
						if(emvDecRdo.getStoreId()==null)
						{
							emvDecRdo.setStoreId(theTxn.getPaymentTransaction().getStore().getId());
						}
						if(emvDecRdo.getRegisterId()==null)
						{
							emvDecRdo.setRegisterId(((CMSRegister)AppManager.getCurrent().getGlobalObject(
									"REGISTER")).getId());
						}
						emvDecRdo.setProcessDate(theTxn.getPaymentTransaction().getProcessDate());
						emvDecRdo.setPayment(payment);
						//Ends here
						 // himani
						//PaymentTransactionAppModel appModel = rewardTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
						//CMSPaymentAppModel[] appModel= theTxn.getPaymentModelArray();
						if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
							String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "DeclineTransaction.rdo";
							try {
								ObjectStore objectStore = new ObjectStore(fileName);
								objectStore.write(emvDecRdo);
							} catch (Exception e) {
								System.out.println("exception on writing object to blueprint folder: " + e);
							}
						}
						ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(theTxn.getPaymentTransaction().getStore(),theTxn.getCustomer());
						printDeclineReceipt(theAppMgr,localeSetter,true,true,emvDecRdo); //himani
						/*theTxn.removePayment(payments[i]);
						model.removeRowInModel(payments[i]);
						model.fireTableDataChanged();*/
						}
					}
					
				}
				updateLabels();
				tblPayment.repaint();
				initButtons(CMSPaymentMgr.PAYMENT);
				selectPayment();	

				return;
			} catch (Exception ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
		}

		
		//Start: Added by Himani for Decline receipt changes

		public void printDeclineReceipt(IApplicationManager theAppMgr, ReceiptLocaleSetter localeSetter, boolean isFirstPrint
			      , boolean isPrintSigCopies, EMVDeclineRDO emvDecRdo) {
			
			ReceiptFactory receiptFactory;
		    Object[] arguments = {emvDecRdo};
		   // if (theTxn.getLineItemsArray().length == 0) {
		     // receiptFactory = new ReceiptFactory(arguments,ReceiptBlueprintInventory.CMSCompositePOSTransaction_Declined);
		      receiptFactory = new ReceiptFactory(arguments,ReceiptBlueprintInventory.CMSCompositePOSTransaction_EMVDecline);
		      localeSetter.setLocale(receiptFactory);
		      if (isFirstPrint) {
		        receiptFactory.print(theAppMgr);
		      }
		   // }
			
		}
		//End : Added by Himani for Decline receipt changes
		
		//Added by Anjana to return to tender list for check declined
		private void returnCheckDeclined() {
			returntotender = true;
			try {
				Payment[] payments = theTxn.getPaymentsArray();
				for (int i = 0; i < payments.length; i++) {
					//For card decline scenario
					//Decline both 1 and 3
					//mayuri edhara : removed 3 for declined status.
					 if (payments[i].getRespStatusCode().equals("1")) {
						if (theAppMgr.getStateObject("MAX_CHANGE") != null) {
							theAppMgr.removeStateObject("MAX_CHANGE");
						}
						theAppMgr.showErrorDlg(res
								.getString("The "+ payments[i].getGUIPaymentDetail() +" check  was declined"));
						theTxn.removePayment(payments[i]);
						model.removeRowInModel(payments[i]);
						model.fireTableDataChanged();
					}
					
				}
				updateLabels();
				tblPayment.repaint();
				initButtons(CMSPaymentMgr.PAYMENT);
				selectPayment();

				return;
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			}
		}
		
//Vivek Mishra : Added for getting Signature image from 3-Byte Ascii	
 	private Image getSignature(String signAscii, CreditCard cc)
 	{   
 		/*byte[] signByteCode = Base64.decodeBase64(signAscii.getBytes());
 		
 		byte[] encoded = Base64.encodeBase64(signAscii.getBytes());
 		String printMe = "";
 		try {
			printMe = new String(encoded, "US-ASCII");
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		byte[] signByteCode = Base64.decodeBase64(encoded);
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
		//Test
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(signAscii.getBytes()));
			img=image;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Test
*/      
 		BufferedImage imag = null;
 		  try {
 		    byte[] buff = Base64.decodeBase64(signAscii.getBytes());   
 			cc.setSignByteCode(buff);
 		    ByteArraySeekableStream stream;
 		    stream = new ByteArraySeekableStream(buff);
 		    TIFFDecodeParam decodeParam = new TIFFDecodeParam();
 		    decodeParam.setDecodePaletteAsShorts(true);
 		    ImageDecoder decoder = ImageCodec.createImageDecoder("TIFF", stream, decodeParam);
 		    RenderedImageAdapter ria = new RenderedImageAdapter(decoder.decodeAsRenderedImage(0));
 		    imag = ria.getAsBufferedImage();
 		   // ImageIO.write(imag, "bmp", new File("C:/ArmaniDocs" + "temp_sig.bmp"));
 		  }
 		  catch (IOException ex) {}
 		   
 		     return imag;

 	}
//Ends

//Vivek Mishra : Added method for showing Option dialouge box with signature 	
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
 	
 	//Anjana added to send SAF request
 	private boolean sendSAFRequest(Payment pay) {
		try {
			String result = "";
	
			String responseArray[] = null;
			String ajbResponse[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			isRefundPaymentRequired = theTxn.isRefundPaymentRequired();
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				Payment[] payments = theTxn.getPaymentsArray();
				int len = payments.length;
				//Vivek Mishra : Added check for Mobile tenders
				//if(!storeCountry.equals("CAN")){
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
					if(!storeCountry.equals("CAN") && (((CreditCard)pay).getCreditCardHolderName()==null || ((CreditCard)pay).getCreditCardHolderName().equals(""))){
					//String tt = orgTxn.getTransactionType();
					if(isManualOverride){
						responseArray = CMSCreditAuthHelper.validateSAF(theAppMgr, theTxn.getPaymentTransaction(), register.getId(), isRefundPaymentRequired, false, isManualOverride,pay);
					
						isManualOverride = false;
				
						for (int x = 0; x < payments.length; x++) {
							
							if(payments[x].getRespStatusCode().equals("0")){
								return true;
					
							}else{
								return false;
							}
						}
						
					}
					if(responseArray!=null){
						ajbResponse = responseArray;
						//End
					int length = responseArray.length;
					for (int i=0; i<length ;i++){
					//Vivek Mishar : Added one codition for blank respose when AJB server gets down in middle of transaction.	
					//if(responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers")||responseArray[i].equals(""))){
						//Vivek Mishra : Removed the blak condition as it was causing the All AJB Server down dialogue even in case of server is recover. 
						if(responseArray[i] != null && responseArray[i].toString().contains("All the Ajb Servers")){
						//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
						//if(!Refresh)
							//count++;
						/*if(count==qty)
						{*/
						theAppMgr.showErrorDlg("All the AJB servers are down");
						/*count=0;
						}//End
*/						return false;
					}
					}
					}
				}}else
				
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
 	
 	
 	private void showSignature(Payment pay){
 		//Vivek Mishra : Changes the condition to restrict showing signature varification dialougue only for non varified payments.
 		//if(pay instanceof CreditCard)
		if(pay instanceof CreditCard && ((CreditCard) pay).isSignatureValidationRequired()==true)
		{	
		CreditCard cc = (CreditCard)pay;
		//if(!(cc.getSignatureAscii().equals(null)) || !(cc.getSignatureAscii().equals(""))){
		//Vivek Mishra : Changed the condition to avoid fatal exception in case of response status code other then approval(0) from credit card sale. 
		if(pay.getRespStatusCode().equals("0") && cc.getRespStatusCode().equals("0")) {
				if(!(cc.getSignatureAscii()==(null))) {
						if(!(cc.getSignatureAscii().equals(""))){

                       Image img = getSignature(cc.getSignatureAscii(), cc);
            //Vivek Mishra : Added to avoid fatal exception in case of no Image 
            if(img!=null){
	        ImageIcon imageIcon = new ImageIcon(img);
	        JLabel jLabel = new JLabel(imageIcon);
	       
		boolean isSignatureAuth;
		isSignatureAuth = showOptionDlg("Signature Validation"
        , "Please press 'Yes' if Signature is valid else press 'No'", res.getString("Yes"), res.getString("No"), jLabel);
        if(!isSignatureAuth){
			//In case of invalid signature setting the signature valid flag as false.
			cc.setSignatureValid(false);
		 
		}else{
			//In case of valid signature setting the signature valid flag as true.
			//Vivek Mishra : Added for Signature printing
			signImg = (BufferedImage)img;
          	//Ends
			cc.setSignatureValid(true);
		}}
					}//	if(!(cc.getSignatureAscii().equals("")))
				}//if(!(cc.getSignatureAscii()==(null))) {
			}
		}
 	}
 	
 	//Vivek Mishra : Moved a block from POST_TXN comman to a method for sending AJB 100 request for every addition of tender
 	private void NotAuthorize()
 	{

 		boolean showTenderOptions = false;
 		String check = null;
		Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
		Payment[] payments = theTxn.getPaymentsArray();
		int len = payments.length;
		
		/**
		 * Code Added for priniting the Error Messages
		 */
		/*Vivek Mishra:Added check for no response AJB*/ 
		if(result.length()==0)
			return;
		//End
		int len_result = result.length();
		//result = result.substring(1, len_result);
		int startIndex = 0;
		/*Vivek Mishra:Changed for AJB CR*/
		
		for (int i = 0; i < payments.length; i++) {
		
			//Vivek Mishra : Changed for avoding array index out of bounds exception after removing extra 0 on response code.
			//startIndex = startIndex + 2;
			startIndex = startIndex + 1;
			//End
			// For Credit Card
			// # 94. Payment/Credit Card: Authorization response  message returned from host not displayed
			if (payments[i] instanceof CreditCard) {
				String errorMsg = "Authorization failed on one or more of the payments!!!!! ";
				//1940
				CreditCard cc = (CreditCard) payments[i];
				if(Payment.APPROVED.equals(cc.getRespStatusCode())){
					continue;
				}
				ArmCurrency amtDue = new ArmCurrency(0.0d);
				//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
				}
				//Ends
				else
					amtDue = theTxn.getCompositeTotalAmountDue();
				
				//1940 PCR Changes ends
				if (payments[i].getRespMessage() != null) {
					errorMsg += ("Message: " + payments[i].getRespMessage());
					} else {
					errorMsg = "Authorization failed on one or more of the payments. Server does not response or timeout.";
				}
				//Anjana commenting the below code as we have new time out scenarios for AJB
				//theAppMgr.showErrorDlg(res.getString(errorMsg));
			}
			
			else {
			
				
				//if(payments[i] instanceof BankCheck && !payments[i].getRespStatusCode().equals("0")){
				if(payments[i] instanceof BankCheck && ((CMSBankCheck)payments[i]).getRespStatusCode()!="0"){
			//Vivek Mishra : Commented the code to restrict using Fipay status code for Bank Checks as requested by Jason on 02-NOV-2015. This code needs to be uncommented if they need request in future.		
				
					//Vivek Mishra : Added code to Change the error message for Bank Checks as requested by Jason on 02-NOV-2015. This code needs to be commented if they need in future.	
				String errorMsg = "Please call for Manual Authorization.";
		
				theAppMgr.showErrorDlg(res.getString(errorMsg));
				manualOnCommunicationError = true;
				break;
			}
		}}
		/**
		 * Code Ended
		 */
		//1940
		if(showTenderOptions){
			updateLabels();
			tblPayment.repaint();		
			selectPayment();
			return;
		}
		//1940 ends
		//Anjana Added the below check to show proper buttons on payment applet when clicked on return to tender list
		//Vivek Mishra : Changed the condition in order to fix communication error dialouge cancel issue.
		//if(!returntotender){
		if(!returntotender || manualOnCommunicationError){
		
		tblPayment.repaint();
		//System.out.println("continues come here from there");
		theAppMgr.showMenu(MenuConst.POST_AUTH, theOpr, theAppMgr.PREV_BUTTON);
		selectLastRow();
		selectOption();
		return;
		}
	
 	}
 	//Ends
 	
 	//Vivek Mishra : Added for not allowing cashier to delete a tender in AJB offline scenario as asked by Phil in status call 06-AUG-15.
	private boolean deletePaymentCancel() {
			try {
				//Vivek Mishra : Added for not allowing cashier to delete a tender in AJB offline scenario as asked by Phil in status call 06-AUG-15.
				Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
				PaymentTransaction txnPos = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
				Payment[] payments = txnPos.getPaymentsArray();
				for(int i=0;i<payments.length;i++)
				{
					String[] voidRes = null;
					boolean offlineDelete = false;
					if(payments[i] instanceof CreditCard && ((CreditCard)payments[i]).getCreditCardHolderName()!=null && (((CreditCard)payments[i]).getCreditCardHolderName().equalsIgnoreCase("Mobile")))
					{
						offlineDelete = true;
					}
					else if(payments[i] instanceof CreditCard )
					{
						CreditCard cc = (CreditCard)payments[i];
						if(cc.getExpirationDate()==null){
							offlineDelete = true;
						}
					}
					
								

				if(!offlineDelete)
				{
					logger.info("DEBUG-PROD-ISSUE:::::: Printing theTxn.getTotalPaymentAmount() :"+theTxn.getTotalPaymentAmount());
					ArmCurrency amountDue = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
					logger.info("DEBUG-PROD-ISSUE:::::: Printing amount due  :"+amountDue);
					//Ruchi:Putting check to avoid random VOID requests by checking composite total amount due new condition  !(theTxn.getCompositeTotalAmountDue().equals(0)) added 
					//post Void is allowed only in split tender. This condition will make sure if amount due is 0 then post void will not be sent from POS
					//09/19/2017 Adding new condition to avoid post voids in production for fully approved transactions
					System.out.println("Printing currency type :"+new ArmCurrency(0));
					if((payments[i] instanceof CreditCard)&& !(payments[i].isAuthRequired())){
						try {
							//Vivek Mishra : Added code to freez the POS
							theAppMgr.setWorkInProgress(true);
							logger.info("DEBUG-PROD-ISSUE:::::: Void triggered from deletePaymentCancel when amount is not due :::::"+!(amountDue.equals(new ArmCurrency(0.0))));
							voidRes = CMSCreditAuthHelper.validateVoid(theAppMgr, txnPos, register.getId(), false, true, false,payments[i],i);
							logger.info("DEBUG-PROD-ISSUE:::::: Void triggered from deletePaymentCancel ::::voidRes:::"+voidRes[0]);
							theAppMgr.setWorkInProgress(false);							
						} catch (SocketException e)
						{
				        	theAppMgr.showErrorDlg(res.getString("Payment Device is Offline. Already authorized tenders cannot be deleted at the moment."));
				        	theAppMgr.setWorkInProgress(false);
							return (false);
						}
						catch (IOException io)
						{
							if (io instanceof SocketException)
							{
								theAppMgr.showErrorDlg(res.getString(io.getMessage()));
								theAppMgr.setWorkInProgress(false);
								return (false);
								
							} else
							{
								theAppMgr.showErrorDlg(res.getString("Payment Device is Offline. Already authorized tenders cannot be deleted at the moment."));
								theAppMgr.setWorkInProgress(false);
								return (false);
							}
						}catch (Exception e) {
							// TODO Auto-generated catch block
							logger.info("DEBUG-PROD-ISSUE:::::: Exception occured while voiding from deletePaymentCancel() :::::"+ e);
							e.printStackTrace();
						}
						//handling bank link down delete scenario	
						//Changed the manual Auth response code from 3 to 2
						//mayuri edhara : added the response code 3 - needed manual auth
						if(voidRes!=null && voidRes[0]!=null && ((voidRes[0].toString().equals("2") || voidRes[0].toString().equals("3")) && payments[i].isSAFable())){
							//Vivek Mishra : Added code to freez the POS
							theAppMgr.setWorkInProgress(true);
							logger.info("DEBUG-PROD-ISSUE:::::: Void triggered from deletePaymentCancel() Bank Link Down:::::");
							voidRes = CMSCreditAuthHelper.validateVoid(theAppMgr, txnPos, register.getId(), false, true, true,payments[i],i);
							logger.info("DEBUG-PROD-ISSUE:::::: Void triggered from deletePaymentCancel() Bank Link Down::voidRes::"+voidRes[0]);
							theAppMgr.setWorkInProgress(false);
							//}
						}
						if(voidRes!=null && voidRes[0]!=null && (voidRes[0].toString().contains("All the Ajb Servers are down at the moment")))
						{
							boolean retryConnect = false;
							
							retryConnect = theAppMgr.showOptionDlg(res.getString("Offline Error")
									, res.getString("Payment Device is Offline. Already authorized tenders cannot be deleted at the moment."), res.getString("Retry"), res.getString("Cancel"));
									 				
							if(!retryConnect){
								//Added this as offline delete cancel wouldnt land the chashier baack to the remaining payment options
								updateLabels();
								tblPayment.repaint();
								CMSPaymentTransactionAppModel txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
								ArmCurrency amt = new ArmCurrency(0.0d);
								//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
								if (((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
										&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
									amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(txn.getTotalPaymentAmount());
								}
								//Ends
								else
									amt = txn.getCompositeTotalAmountDue().subtract(txn.getTotalPaymentAmount());
								if (!amt.equals(new ArmCurrency(0.0))) {
									selectPayment();
								} 
								return false;
								
							}else{
								//If clicked on Retry , send 100 request again
								//Vivek Mishra : Added to fix the retry issue in which it is allowing to delete the approved tender after retry.
								logger.info("DEBUG-PROD-ISSUE::::::deletePaymentCancel() Called from Else loop :::::");
								boolean status = deletePaymentCancel();
								logger.info("DEBUG-PROD-ISSUE::::::deletePaymentCancel() cancelled  from Else loop ::status:::"+status);
								return status;
							}
						}
					}
					//Vivek Mishra : Added to void Gift Card payment 03-NOV-2016
					else if(payments[i] instanceof Redeemable && fipay_flag.equalsIgnoreCase("Y") && fipayGiftcardFlag.equalsIgnoreCase("Y") && theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction)
					{
						logger.info("DEBUG-PROD-ISSUE::::::deletePaymentCancel() void Gift Card payment :::::");
						boolean response = deleteRedeemablePayment(payments[i], txnPos);
						if(!response)
							return response;
					}
					//Ends here 03-NOV-2016
				}
				//Ends		
				theTxn.removePayment(payments[i]);
				if (theAppMgr.getStateObject("MAX_CHANGE") != null) {
					theAppMgr.removeStateObject("MAX_CHANGE");
				}
				model.removeRow(0);
				
				updateLabels();
				tblPayment.repaint();
				//Vivek Mishra : Commented the selectPayment call to fix wrong improper edit area in InitialSaleApllet on previous.  
				//selectPayment();
				//Ends
				}
				//Vivek Mishra : Added to send void activation and reload request 02-NOV-2016
				if(fipay_flag.equalsIgnoreCase("Y") && fipayGiftcardFlag.equalsIgnoreCase("Y") && theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction)
				{
					POSLineItem[] items = theTxn.getLineItemsArray();
					boolean response = false;
	                boolean isRefund = false;
	                if(txnPos.getTransactionType().equals("RETN"))
	                {
	                	isRefund = true;
	                }
					for(POSLineItem item : items)
					{
						//System.out.println(item.isMiscItem()+""+item.getMiscItemId()+""+item.getMiscItemComment()+""+item.getMiscItemGLAccount());
						if(item.isMiscItem() && item.getMiscItemId().equals("STORE_VALUE_CARD") && item.getAjbSequence()!=null)
						{
							boolean isReload = false;
							boolean retryConnect = false;
							String gcControlNo = null;
							POSLineItemDetail[] lineItemDetails = item.getLineItemDetailsArray();
							for(int i=0; i<lineItemDetails.length; i++)
							{
								gcControlNo = lineItemDetails[i].getGiftCertificateId();
									if(gcControlNo!=null && gcControlNo!="")
									{
										if(gcControlNo.contains("R"))
										{
											isReload = true;
										}
									}
									else
									{
										isReload = true;
									}	
									logger.info("DEBUG-PROD-ISSUE::::::deletePaymentCancel() validateGiftCardItemVoid :::::");

										response = CMSCreditAuthHelper.validateGiftCardItemVoid(item.getAjbSequence(), item.getNetAmount().absoluteValue().toString(), isReload, isRefund);
										if(!response)
										{

											retryConnect = theAppMgr.showOptionDlg("Fipay Error"
													 			    	            , CMSCreditAuthHelper.getResponseErrorMsg()+". Unable to void Gift Card Activation/Reload. Please select Retry to try againg or Cancel to Post Void the transaction later.", "Retry","Cancel");
											CMSCreditAuthHelper.setResponseErrorMsg("");
											if(retryConnect)	
											{
												i = i-1;
												continue;
											}
											else
											{
												return false;
											}

										}
							}
						}
					}
				}
				//Ends here 02-NOV-2016
				return true;

			//end
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			} /*catch (CurrencyException e) {
				System.out.print("Currency Ex deletePayment-> " + e);
			}*/ catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return true;
	}
 	
	//Ends
	
	//Vivek Mishra : Added for showing Credit Card types to choose in AJB offline mode
	
	/**
	   *Display Credit Card options
	   */
	  private void displayCreditCardTypes() {
	    List paymentCCodes = CMSPaymentMgr.getPaymentCode("CREDIT_CARD");
	    CMSPaymentCode pc = null;
	    paymentType = null;
	    paymentDesc = null;
	    for(Object obj:paymentCCodes)
	    {
	    	if(((CMSPaymentCode)obj).getPaymentDesc().equals("DEBIT_CARD"))
	    	{
	    		pc=(CMSPaymentCode)obj;
	    		break;
	    	}	
	    }
	    paymentCCodes.remove(pc);
	    int numPaymentCCodes = paymentCCodes.size()+1;
	    ArrayList aPaymentCodes = new ArrayList(numPaymentCCodes);
	    aPaymentCodes.addAll(paymentCCodes);
	    Collections.sort(aPaymentCodes, new CreditCardCodeComparator());
	    CMSPaymentCode[] paymentCodes =(CMSPaymentCode[])aPaymentCodes.toArray(new CMSPaymentCode[0]);
	    GenericChooserRow[] availPaymentCodes = new GenericChooserRow[numPaymentCCodes];
	    if (numPaymentCCodes>0){
	        for (int i = 0; i < numPaymentCCodes-1; i++) {
	        	if(((paymentCodes[i].getPaymentDesc()).equals("JCB")))
	        		pc=paymentCodes[i];
	            availPaymentCodes[i] = new GenericChooserRow(new String[] {
	                    paymentCodes[i].getPaymentDesc()}, paymentCodes[i]);
	        }
	    }
	    if(pc!=null){
	    CMSPaymentCode payCodeCup = new CMSPaymentCode(pc.paymentType, pc.paymentCode, "CUP");
	    availPaymentCodes[numPaymentCCodes-1] = new GenericChooserRow(new String[] {
	    		payCodeCup.getPaymentDesc()}, payCodeCup);
	    }
	    overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr
	        , availPaymentCodes, new String[] {"Credit Card Types"
	    });
	  }

	  /**
	   * put your documentation comment here
	   */
	  private void getSelectedType() {
	    overRideDlg.setVisible(true);
	    if (overRideDlg.isOK()) {
	      CMSPaymentCode payCode = (CMSPaymentCode)overRideDlg.getSelectedRow().getRowKeyData();
	      //cardCode = payCode.getPaymentCode();
	      paymentType = payCode.getPaymentType();
	      paymentDesc = payCode.getPaymentDesc();
	      //Mayuri Edhara : 21 NOV 16 : - Modified for Menu Changes requested by Edwin.
			isMobileTerminal = true;
	    }
	    else {
	    	isMobileTerminal=false;
		}
	  }
	  
	  private class CreditCardCodeComparator implements Comparator {
		  public int compare(Object obj1, Object obj2) {
			  String code1 = ((CMSPaymentCode) obj1).getPaymentCode();
		      String code2 = ((CMSPaymentCode) obj2).getPaymentCode();        
		      return code1.compareTo(code2);
		  }
	  }
	  //Mayuri Edhara :: added because we are making this null for certain actions hence we are resetting it by calling this method.
	  public CMSCompositePOSTransaction getOriginalTransaction(PaymentTransaction theTxnPOS)
	  {
		  OrgSaleTxn = null;
		  POSLineItem[] lineitem = null;
		  if(((theTxnPOS instanceof CMSRedeemableBuyBackTransaction))){
			  return null;
		  }else
		  {
			  if(theTxnPOS instanceof CMSCompositePOSTransaction)
			  lineitem = ((CMSCompositePOSTransaction) theTxnPOS).getLineItemsArray();
		  }
		  if(lineitem!=null){
		for(int i =0 ; i<lineitem.length ;i++){
		    //CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) detail[i].getLineItemDetailsArray()[0];
		    if(lineitem[i] instanceof CMSReturnLineItem ){
		     CMSReturnLineItem rtnLnItm = (CMSReturnLineItem) lineitem[i];
		     CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) rtnLnItm.getLineItemDetailsArray()[0];
		     CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) rtnLnItmDtl.getSaleLineItemDetail();
		     if(saleLnItmDtl!=null){
		     CompositePOSTransaction txnObject = saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction();
		   //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
				ArmCurrency amtDue = new ArmCurrency(0.0d);
				if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
						&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
					amtDue = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue();
				}
				//Ends
				else
					amtDue = theTxn.getCompositeTotalAmountDue();
		    if(amtDue.doubleValue()<0){
		     OrgSaleTxn = ((CMSCompositePOSTransaction) txnObject);
		    }
		    
		     break;
		    }
		    }
		    
		   }}
		return OrgSaleTxn;
	  }
	  
	//Vivek Mishra : Added for Mobile terminal return flow
	  private ArmCurrency getMobileTerminalReturnAmount()
	  {
		  ArmCurrency amt = new ArmCurrency(0.0d);
          //ArmCurrency retAmt = new ArmCurrency(0.0d);
		  try{
			//Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
			if ((theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && (!(theTxn instanceof CMSRedeemableBuyBackTransactionAppModel)) && ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray() != null
					&& ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
				amt = ((CMSCompositePOSTransaction) theTxn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
			}
			//Ends
			else
				amt = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
			if(OrgSaleTxn!=null && amt.lessThan(new ArmCurrency(0.0))){
			int row = tblPayment.getSelectedRow();
			if(row == -1)
			{
				retAmt = new ArmCurrency(0.0d);
				return retAmt;
			}
			Payment payment = model.getPayment(row);
			retAmt = payment.getAmount();
			if(retAmt.greaterThan(amt.getAbsoluteValue()))
				retAmt=amt;
			else
				retAmt=retAmt.multiply(-1d);
	  }
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
	//Ends
			return retAmt.absoluteValue();
	  }
    //Ends
	//Vivek Mishra : Added for Signature printing
	  private void printSignature()
	  {
		  Payment[] payments = theTxn.getPaymentsArray();
		  for(Payment payment : payments)
		  {
			  if(payment instanceof CreditCard)
			  {
				  CreditCard cc = (CreditCard)payment;
				  if(cc.isSignatureValid())
				  {
					  /*String signLocation = "C://clientwindows1.5//US//retek//rpos//Signature//MyFile.bmp";  
			 			File f = new File("C://clientwindows1.5//US//retek//rpos//Signature//MyFile.bmp");*/
			 			String signLocation = "signature.bmp";  
			 			File f = new File(signLocation);
			 			try {
			 				ImageIO.write(signImg, "bmp", f);
//Anjana 05/16: commented below to avoaid fatal exception while printing dup signature 
							//signImg = null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (theTxn instanceof CMSCompositePOSTransactionAppModel) 
						((CMSCompositePOSTransactionAppModel)theTxn).setSignLocation(signLocation);
						break;
				  }
			  }
		  }
	  }
	  
	  private void deleteSignFile()
	  {
				File file = new File("signature.bmp");
				if(file.exists())
				file.delete();

	  }
	  //Ends
	  //Vivek Mishra : Added for Puerto Rico fical changes 
	  private boolean getFiscalNum()
	  {
		  PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
		  if (!(theTxn.getPaymentTransaction() instanceof CMSCompositePOSTransaction)){
			  return false;
		  }
		  ConfigMgr mgr = new ConfigMgr("tax.cfg");	  
		  String txnType = theTxn.getPaymentTransaction().getTransactionType();
		  Payment[] payments = theTxn.getPaymentsArray();
		  String cardType = null;
		  CreditCard cc = null;
		  String last4 = null;
		  String authCode = null;
		  String tranId = null;
		  ArmCurrency stateTax = null;
		  ArmCurrency cityTax = null;
		  String invId = (theTxn.getPaymentTransaction().getId().replace("*", "")) + Calendar.getInstance().get(Calendar.MILLISECOND);
		  try {
			 stateTax  = ((CMSCompositePOSTransactionAppModel)theTxn).getStateTaxAmount();
			 cityTax  = ((CMSCompositePOSTransactionAppModel)theTxn).getCityTaxAmount();
		} catch (CurrencyException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		  //Vivek Mishra : Changed to fix transaction posting issue in case of 100% discount
		  //if(payments.length == 0 || payments.length > 1)
		  if(payments.length == 0 || payments.length > 1)
			  cardType = "Cash";
		  else if(payments.length == 1)
		  {
			  if(payments[0] instanceof CreditCard)
			  {
				  cc = (CreditCard)payments[0];
				  if(cc.getCreditCardHolderName()!=null && cc.getCreditCardHolderName().equalsIgnoreCase("Mobile"))
				  {
					  cardType = "Cash";
				  }
				  else{
				  if((cc.getGUIPaymentName().toUpperCase()).contains("VISA"))
					  cardType = "Visa";
				  else if((cc.getGUIPaymentName().toUpperCase()).contains("MASTER"))
					  cardType = "MasterCard";
				  else if((cc.getGUIPaymentName().toUpperCase()).contains("DISCOVER"))
					  cardType = "Discover";
				  else if((cc.getGUIPaymentName().toUpperCase()).contains("AMEX"))
					  cardType = "AmericanExpress";
				  else if((cc.getGUIPaymentName().toUpperCase()).contains("DEBIT"))
					  cardType = "DebitCard";
				  else
					  cardType = "Cash";
				  last4 = cc.getMaskCardNum().substring(cc.getMaskCardNum().length()-4, cc.getMaskCardNum().length());
				  authCode = cc.getRespAuthorizationCode();
				  tranId = cc.getAjbSequence();
				  }
			  }
			  
			  else if(payments[0] instanceof CMSBankCheck)
			  {
				  cardType = "Check";
				  authCode = ((CMSBankCheck)payments[0]).getRespAuthorizationCode();
			  }
			  
			  else 
				  cardType = "Cash";
		  }
		  if(!txnType.contains("SALE")&&!txnType.contains("RETN")&&!txnType.contains("PRSO"))
		  {
			  return false;
		  }
			TransactionRequest _request = new TransactionRequest();
		    TransactionResponse _response = new TransactionResponse();
		    String parseFloat = "0.0";
		    parseFloat = theTxn.getTotalPaymentAmount().absoluteValue().stringValue();
		    parseFloat = parseFloat.replaceAll(",", "");
		    _request.setPay_id(theTxn.getPaymentTransaction().getId().replace("*", ""));
		    LoggingServices.getCurrent().logMsg("*************************************** Request payid : "+theTxn.getPaymentTransaction().getId().replace("*", "")+"************************************");
			 _request.setPay_cardtype(CARDTYPE.valueOf(cardType));
			 LoggingServices.getCurrent().logMsg("*************************************** Request paycardtype : "+CARDTYPE.valueOf(cardType).toString()+"************************************");
			 _request.setPay_amount(Float.parseFloat(parseFloat));
			 LoggingServices.getCurrent().logMsg("*************************************** Request payamount : "+parseFloat+"************************************");
			 _request.setPay_timecreated_device(new Date());
			 LoggingServices.getCurrent().logMsg("*************************************** Request paytimecreateddevice : "+new Date()+"************************************");
			 _request.setPay_transid(tranId);
			 LoggingServices.getCurrent().logMsg("*************************************** Request paytransid : "+tranId+"************************************");
			 try {
			if(stateTax == null)
				_request.setTax1(Float.parseFloat(new ArmCurrency(0.0d).absoluteValue().stringValue()));
			else{
				parseFloat = stateTax.absoluteValue().stringValue();
			    parseFloat = parseFloat.replaceAll(",", "");

				_request.setTax1(Float.parseFloat(parseFloat));
			}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 LoggingServices.getCurrent().logMsg("*************************************** Request Tax1 : "+_request.getTax1()+"************************************");
			 if(cityTax == null)
				 _request.setTax2(Float.parseFloat(new ArmCurrency(0.0d).stringValue()));
			else{
				parseFloat = cityTax.absoluteValue().stringValue().replace("-", "");
			    parseFloat = parseFloat.replaceAll(",", "");
				_request.setTax2(Float.parseFloat(parseFloat));
			}
			 LoggingServices.getCurrent().logMsg("*************************************** Request Tax2 : "+_request.getTax2()+"************************************");
			 _request.setTipAmount(0.0f);
			 LoggingServices.getCurrent().logMsg("*************************************** Request tipAmount : "+0.0f+"************************************");
			 _request.setAuthcode(authCode);
			 LoggingServices.getCurrent().logMsg("*************************************** Request authcode : "+authCode+"************************************");
			 _request.setCCNUMLast4(last4);
			 LoggingServices.getCurrent().logMsg("*************************************** Request CCNUMLast4 : "+last4+"************************************");
			 _request.setInv_id(invId);
			 LoggingServices.getCurrent().logMsg("*************************************** Request invid : "+invId+"************************************");
			//_request.settranstype(Sale);
			 if(txnType.contains("SALE")||txnType.contains("PRSO"))
				 _request.setTrans_type(TRANSTYPE.Sale);
			 else
				 _request.setTrans_type(TRANSTYPE.Refund);
			 LoggingServices.getCurrent().logMsg("*************************************** Request transtype : "+_request.getTrans_type()+"************************************");
			 IVU _ivu = new IVU();
			 
			 try {
			 
				_response =  (TransactionResponse)_ivu.transactionRequest(_request);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
			
				e.printStackTrace();
				return false;
			}
			//Vivek Mishra : Added for handling null response from eNabler.
			if(_response== null){
				return false;
			}
			//Ends
			LoggingServices.getCurrent().logMsg("*************************************** Response PayId : "+_response.getPay_id()+"************************************");
			LoggingServices.getCurrent().logMsg("*************************************** Response IVUNumber : "+_response.getIVUNumber()+"**************************************");
			LoggingServices.getCurrent().logMsg("*************************************** Response IVUNumber : "+_response.getIVUprocessor()+"**************************************");
			 
			 if(_response.getIVUNumber()!=null)
			 {
				 /*((CMSCompositePOSTransaction)theTxnPOS).setFiscalReceiptNumber(_response.getIVUNumber());
				 ((CMSCompositePOSTransaction)theTxnPOS).setFiscalReceiptDate(new Date());*/
				 ((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).setFiscalReceiptNumber(_response.getIVUNumber());
				 ((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).setFiscalReceiptDate(new Date());
				 ((CMSCompositePOSTransaction)theTxn.getPaymentTransaction()).setIvuProcessor(_response.getIVUprocessor());
			 }
			 
		  return true;
	  }
	  //Ends
	  
/*	mayuri edhara : for Timeout scenarios
 * */
   public void sendTimeOutReversalRequest(Payment payment){
		try {
	
			String responseArray = null;
			
				responseArray = CMSCreditAuthHelper.validateTimeoutReversal(theAppMgr, payment);
	
				if(responseArray!=null){
					for(int i=0 ; i< responseArray.length(); i++){
						if(responseArray!= null && responseArray.contains("All the Ajb Servers")){	
							theAppMgr.showErrorDlg("All the AJB servers are down");	
						}
					}
				}


		} catch (Exception e) {
			e.printStackTrace();
			theAppMgr.showExceptionDlg(e);
		}
	}

	  public void correctDiscountsAndSequences(PaymentTransaction theTxnPOS) throws  java.lang.Exception
	  {


		  /*******START DISCOUNTS CORRECTION*****/
		  if (theTxnPOS instanceof CompositePOSTransaction) {
			  CompositePOSTransaction compositePOSTransaction = (CompositePOSTransaction)theTxnPOS;

		      Discount[] discounts = compositePOSTransaction.getDiscountsArray();
	 	      Discount[] settlementdiscounts = compositePOSTransaction.getSettlementDiscountsArray();
		      POSLineItem[] posLineItem = compositePOSTransaction.getLineItemsArray();
		      ArrayList<CMSDiscount> dislist = new ArrayList<CMSDiscount>();
		 	  ArrayList<CMSDiscount> settledislist = new ArrayList<CMSDiscount>();

	 	      /*
	 	       * remove the discounts so that other discounts apart from line item array are removed.
	 	       * To Correct the discounts. first add it to array and then check from the array.
	  		   * comment this code if no discounts..
	  		   */

	  	 	  if(discounts != null){
		      	compositePOSTransaction.removeAllDiscounts();
		      	discounts = compositePOSTransaction.getDiscountsArray();
		      	for(int dis=0; dis<discounts.length; dis++){
	 	    		CMSDiscount discount = (CMSDiscount)discounts[dis];
	 	    		dislist.add(discount);
	 	     	}

		 	       if (posLineItem != null) {
		 	    	   for (int i = 0; i < posLineItem.length; i++){

		 	    			  /* Add the sale line dicounts to the object transaction discount array
		 	    			   * From the object dislist; check if the line discount is present
		 	    			   * add the line discount to dislist and also object.
		 	    			   * un-comment the correct sequence to set the sequences
		 	    			   */
		 	    			 Discount[] lineDiscount =  posLineItem[i].getDiscountsArray();
		 	    			 if(lineDiscount != null){
		 	    				 for(int m=0; m< lineDiscount.length; m++){
		 	    					CMSDiscount discount = (CMSDiscount)lineDiscount[m];

		 	    					if(dislist.size() == 0){
		 	    						dislist.add(discount);
		 	    						compositePOSTransaction.addDiscount(lineDiscount[m]);
		 	    					}else {
			 	    					for(int di = 0; di<dislist.size(); di++){
			 	    						if(!dislist.contains(discount)){
			 	    						dislist.add(discount);
			 	    						compositePOSTransaction.addDiscount(lineDiscount[m]);
			 	    						System.out.println("added discount discount " + discount);
			 	    						break;
			 	    						}
			 	    					}
		 	    					}
		 	    				 }
		 	    			 }

		 	    	  } // end for
		 	       } // end if
	  	 	   }// end if (discount != null)

	 	       /*
	 	        * Remove the settlement discount Array from the discount Array.
	 	        */
  	 	     settlementdiscounts = compositePOSTransaction.getSettlementDiscountsArray();
	 	     discounts = compositePOSTransaction.getDiscountsArray();
	 	     
	  	 	 if(settlementdiscounts != null){
	 		     for(int dis1=0; dis1<settlementdiscounts.length; dis1++){
	 		    	CMSDiscount discount = (CMSDiscount)settlementdiscounts[dis1];
	 		    	settledislist.add(discount);
	 		     }
		 	     for(int n=0; n<discounts.length; n++){
		 	    	 CMSDiscount discount = (CMSDiscount)discounts[n];
		 	    	for(int di = 0; di<settledislist.size(); di++){
		 	    		if(settledislist.contains(discount)){
		 	    			compositePOSTransaction.removeDiscount(discount);
		 	    			System.out.println("remove discount " + discount);
		 	    		}
		 	    	}
		 	      }

	  	 	    	} // end if (settlementdiscounts !=null)

	 	       /*
	 	        * Correct the sequence of Discount Array
	 	        */

	 	      int i = 1;
	 	      discounts = compositePOSTransaction.getDiscountsArray();
	  	      settlementdiscounts = compositePOSTransaction.getSettlementDiscountsArray();

	 	      if(discounts !=null){
		 	      for(int j = 0; j < discounts.length; j++){
		  	    	 CMSDiscount discount = (CMSDiscount)discounts[j];
		  	    		discount.setSequenceNumber(i);
		  	    		i++;

		  	      }
	 	      }
	 	      if(settlementdiscounts != null){
		 	     for(int j=0; j<settlementdiscounts.length; j++){
		 	    	CMSDiscount discount = (CMSDiscount)settlementdiscounts[j];
			    		discount.setSequenceNumber(i);
			    		i++;
		 	      }
	 	      }

		  }
		   /*******END DISCOUNTS CORRECTION*****/
	  }

	//Vivek Mishra : Added to void Gift Card payment 03-NOV-2016
	  private boolean deleteRedeemablePayment(Payment pay, PaymentTransaction txnPos)
	  {

			boolean response = false;
          boolean isRefund = false;
          boolean retryConnect = false;
          String ajbSequence = null;
          String id = null;
          if(txnPos.getTransactionType().equals("RETN"))
          {
          	isRefund = true;
          }
          if(pay instanceof CMSStoreValueCard)
          {
          	ajbSequence = ((CMSStoreValueCard)pay).getAjbSequence();
          }
          else if(pay instanceof CMSDueBill)
          {
          	ajbSequence = ((CMSDueBill)pay).getAjbSequence();
          }
          else if(pay instanceof CMSDueBillIssue)
          {
          	ajbSequence = ((CMSDueBillIssue)pay).getAjbSequence();
          }
          id = ((Redeemable)pay).getId();
          if(ajbSequence!=null && ajbSequence!="")
          {
        	  String amount="";
        	  String netAmount=String.valueOf(pay.getAmount());
				if(netAmount.contains(",") && netAmount.contains("-")){
				int start=netAmount.indexOf(',');
				int end=netAmount.indexOf('-');
				 amount=netAmount.substring(start+1,end).trim();
			}else{
				amount=netAmount;
			}  // Vishal Yevale : credit note void request :17 March 2017
				if(pay instanceof CMSDueBillIssue)
		          {
		          	response = CMSCreditAuthHelper.validateGiftCardItemVoid(ajbSequence, id, false, false);
		          }else{
          	response = CMSCreditAuthHelper.validateGiftCardPaymentVoid(ajbSequence, id, amount, isRefund);
		          }
          }
          else
          	return true;
          if(!response)
			{

				retryConnect = theAppMgr.showOptionDlg("Fipay Error"
						 			    	            , CMSCreditAuthHelper.getResponseErrorMsg()+". Unable to void Redeemable Payment. Please select Retry to try again or Cancel to POST VOID the transaction later.", "Retry", "Cancel");
				CMSCreditAuthHelper.setResponseErrorMsg("");
				if(retryConnect)	
				{
					return deleteRedeemablePayment(pay, txnPos);
				}
				else
				{
					return false;
				}

			}
          return true;
		
	  }
	  //Ends here 03-NOV-2016

}