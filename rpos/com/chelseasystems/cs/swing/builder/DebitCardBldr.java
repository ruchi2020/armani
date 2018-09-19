/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06/23/2005 | Khyati    | N/A       | Base. Copied from Global                           |
 --------------------------------------------------------------------------------------------------
 | 2    | 06/23/2005 | Khyati    | N/A       | Europe: debit  card                                |
 --------------------------------------------------------------------------------------------------
 */

package com.chelseasystems.cs.swing.builder;

import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.payment.CMSDebitCard;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;
import com.armani.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.swing.dlg.CreditCardsDlg;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cr.payment.DebitCard;
import com.chelseasystems.cs.util.TransactionUtil;

/**
 */
public class DebitCardBldr implements IObjectBuilder {
	private IObjectBuilderManager theBldrMgr;
	private CMSApplet applet;
	private IApplicationManager theAppMgr;
	private String accountNum = null;
	private String cardHolderName = null;
	private String expDate = null;
	private String serviceCode = null;
	private String rawData = null;
	private String trackNumber = null;
	private String cid = null;
	private Payment thePayment;
	private boolean manual = false;
	private boolean digitVerify = false;
	private CMSMSR cmsMSR = null;
	private String zipCode = null;
	boolean zipTraversed = false;
	private PaymentTransaction aTxn = null;
	private double theAmount = 0.0d;
	private GenericChooseFromTableDlg overRideDlg;
	private String cardType = null;
	private String cardCode = null;
	private ArmCurrency amtDue;
	private String issueNumber = null;

	/**
   */
	public DebitCardBldr() {
	}

	/**
	 * @param theBldrMgr
	 * @param theAppMgr
	 */
	public void init(IObjectBuilderManager theBldrMgr,
			IApplicationManager theAppMgr) {
		this.theBldrMgr = theBldrMgr;
		this.theAppMgr = theAppMgr;
	}

	/**
   */
	public void cleanup() {
		// cmsMSR.release();
	}

	/**
	 * Display Credit Card options
	 */
	private void displayDebitCardTypes() {
		CreditCardsDlg creditCardHelper = new CreditCardsDlg("DEBIT_CARD_TYPES");
		String[] titles = { ResourceManager.getResourceBundle().getString(
				"Select the Debit Card type.") };
		overRideDlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(),
				theAppMgr, creditCardHelper.getTabelData(), titles);
	}

	/**
	 * put your documentation comment here
	 */
	private void getSelectedType() {
		overRideDlg.setVisible(true);
		if (overRideDlg.isOK()) {
			Object reasonCode = overRideDlg.getSelectedRow().getRowKeyData();
			cardCode = (String) reasonCode;
			Object cardDesc[] = overRideDlg.getSelectedRow().getDisplayRow();
			cardType = (String) cardDesc[0];
		}
	}

	/**
	 * @param theCommand
	 * @param theEvent
	 */
	public void EditAreaEvent(String theCommand, Object theEvent) {
		if (theCommand.equals("DEBIT_CARD")) {
			if (theEvent == null || theEvent.toString().length() == 0) {
				manual = true;
				// display credit card picklist
				// Vivek Mishra : Commented for escaping the card selection
				// dialogue for POS-AJB integration
				// displayDebitCardTypes();
				// getSelectedType();
				// End
			}
		}
		if (theCommand.equals("ACCOUNT")) {
			accountNum = (String) theEvent;
			((CreditCard) thePayment).setAccountNumber(accountNum);
			((CreditCard) thePayment).setAmount(new ArmCurrency(theAmount));
			// ((CreditCard)thePayment).setAmount((ArmCurrency)theEvent);
			((CreditCard) thePayment).setPaymentCode(cardCode);
			((CreditCard) thePayment).setMessageIdentifier("CC");
			((CreditCard) thePayment).setMessageType("0");
			((CreditCard) thePayment).setTenderType("03");
			((CreditCard) thePayment).setManuallyKeyed(true);
			manual = true;
			digitVerify = true;
			// end
		}
		if (theCommand.equals("AMOUNT")) {
			try {
				// Vivek Mishra : Added to populate details before entering the
				// account number for POS-AJB integration
				cardCode = "145";
				// accountNum = "987656787654";
				// End
				if (cardCode.equals("145")) {
					thePayment = new CMSDebitCard(
							ICMSPaymentConstants.DEBIT_CARD);
					System.out.println("Ruchi before setting dcrd:");
					thePayment.setGUIPaymentName("DEBIT_CARD");
				}
				if (TransactionUtil.validateChangeAmount(theAppMgr,
						thePayment.getTransactionPaymentName(),
						thePayment.getGUIPaymentName(), (ArmCurrency) theEvent))
					// if (validateChangeAmount((ArmCurrency)theEvent)) //
					// remove, this is checked in a business rule
					theAmount = ((ArmCurrency) theEvent).doubleValue();
				else
					theAppMgr.setSingleEditArea(
							applet.res.getString("Enter amount."), "AMOUNT",
							amtDue.absoluteValue(), theAppMgr.CURRENCY_MASK);
				// invoke the call the armani's api
				/*
				 * SetefiManagement set = new SetefiManagement(new
				 * Double(theAmount)); set.getSetefiResult(); CMSCreditCard sr =
				 * (CMSCreditCard)set.getSetefiresultObject(); set.release(); if
				 * (sr.getRespStatusCode().compareTo("01") == 0) { String
				 * sCardCode = sr.getTypeCode(); if (sCardCode.equals("145")) {
				 * sr.setTransactionPaymentName("DCRD");
				 * sr.setGUIPaymentName("DCRD"); sr.setPaymentCode(sCardCode);
				 * sr.setMessageIdentifier("CC"); sr.setMessageType("0");
				 * sr.setTenderType("03"); } else {
				 * theAppMgr.showErrorDlg(applet
				 * .res.getString("Not a valid debit card type")); return; } //
				 * If approved then return object.
				 * theBldrMgr.processObject(applet, "PAYMENT", sr, this); } //
				 * If declined then return null . else if
				 * (sr.getRespStatusCode().compareTo("02") == 0) {
				 * theAppMgr.showErrorDlg( ( (CreditCard) thePayment).
				 * getRespAuthorizationResponseCode());
				 * theBldrMgr.processObject(applet, "PAYMENT", null, this);
				 * return; } // If status code is 04 or 05 do manual auth steps.
				 * else if (sr.getRespStatusCode().compareTo("04") == 0 ||
				 * sr.getRespStatusCode().compareTo("05") == 0) {
				 * theAppMgr.setSingleEditArea(applet.res.getString(
				 * "Swipe debit card, press K for keyed entry or press Enter to skip entry"
				 * ), "CREDIT_CARD"); theAppMgr.setEditAreaFocus(); return; }
				 */
				// For the time being process mnanual: To be removed once we get
				// the complete api from armani
				theAppMgr
						.setSingleEditArea(
								applet.res
										.getString("Swipe debit card, press 'K' for keyed entry or press 'Enter' to skip entry"),
								"DEBIT_CARD");
				theAppMgr.setEditAreaFocus();
				return;
			} catch (Exception ex) {
				ex.printStackTrace();
				theAppMgr
						.setSingleEditArea(
								applet.res
										.getString("Swipe debit card, press 'K' for keyed entry or press 'Enter' to skip entry"),
								"DEBIT_CARD");
				theAppMgr.setEditAreaFocus();
			}
		}
		if (theCommand.equals("DATE")) {
			// Expiration date on the magstripe is coded YYMM. Jay wanted the
			// manual entry
			// of an expiration date to come in MMYY. So we have to reverse the
			// month
			// and year here for the getCalendar() method.
			String testExpDate = (String) theEvent;
			if (4 == testExpDate.length()) {
				StringBuffer buf = new StringBuffer(testExpDate.substring(2));
				buf.append(testExpDate.substring(0, 2));
				testExpDate = buf.toString();
				Calendar cal = CreditCardBldrUtil.getCalendar(testExpDate);
				if (cal == null) {
				} else {
					if (CreditCardBldrUtil.validateDate(cal)) {
						expDate = (String) theEvent;
						CreditCard cc = (CreditCard) thePayment;
						Date dt = cal.getTime();
						cc.setExpirationDate(dt);
					} else {
						theAppMgr
								.showErrorDlg(applet.res
										.getString("This card has passed its expiration date."));
					}
				}
			} else {
				theAppMgr
						.showErrorDlg(applet.res
								.getString("The expiration date must be formatted as MMYY."));
			}
		}
		if (theCommand.equals("ISSUENUMBER")) {
			if (theEvent == null || theEvent.toString().length() == 0) {
				issueNumber = "";
			} else {
				try {
					String temp = (String) theEvent;
					Integer.parseInt(temp);
					((CMSDebitCard) thePayment).setIssueNumber(temp);
					issueNumber = temp;
				} catch (NumberFormatException ex) {
					theAppMgr.showErrorDlg(applet.res
							.getString("Issue Number is numeric only."));
					issueNumber = null;
				}
			}
		}
		if (theCommand.equals("OVERRIDE")) {
			CreditCard cc = (CreditCard) thePayment;
			cc.setManualOverride((String) theEvent);
		}
		if (completeAttributes()) {
			theBldrMgr.processObject(applet, "PAYMENT", thePayment, this);
			thePayment = null;
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
						.showErrorDlg(applet.res
								.getString("You can not give more change than is due."));
				return (false);
			} else
				return (true);
		} catch (Exception ex) {
			return (true);
		}
	}

	/**
	 * @param Command
	 * @param applet
	 * @param initValue
	 */
	public void build(String Command, CMSApplet applet, Object initValue) {
		resetAttributes();
		this.applet = applet;
		aTxn = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
		try {
			CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) aTxn
					.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			amtDue = appModel.getCompositeTotalAmountDue().subtract(
					appModel.getTotalPaymentAmount());
			// ks: Prompt to Enter amount.
			theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."),
					"AMOUNT", amtDue.absoluteValue(), theAppMgr.CURRENCY_MASK);
			theAppMgr.setEditAreaFocus();
		} catch (Exception ex) {
			theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."),
					"AMOUNT", theAppMgr.CURRENCY_MASK);
		}
	}

	/**
	 * @return
	 */
	private boolean completeAttributes() {
		// didn't swipe, get account number for payment type
		if (cardCode == null) {
			// display credit card picklist
			// Vivek Mishra : Commented for escaping the card selection dialogue
			// for POS-AJB integration
			// displayDebitCardTypes();
			// getSelectedType();
		}
		// Vivek Mishra : Commented and added a new condition for adding account
		// number
		// if (thePayment == null) {
		if (accountNum == null) {
			theAppMgr.setSingleEditArea(
					applet.res.getString("Enter account number."), "ACCOUNT",
					theAppMgr.REQUIRED_MASK);
			return (false);
		}
		if (expDate == null) {
			theAppMgr.setSingleEditArea(
					applet.res.getString("Enter expiration date.  MMYY"),
					"DATE");
			return (false);
		}
		if (issueNumber == null) {
			theAppMgr.setSingleEditArea(
					applet.res.getString("Enter issue number."), "ISSUENUMBER");
			return (false);
		}
		if (aTxn.getHandWrittenTicketNumber().length() > 0
				&& thePayment.isAuthRequired()) {
			theAppMgr.setSingleEditArea(
					applet.res.getString("Enter authorization number."),
					"OVERRIDE", theAppMgr.REQUIRED_MASK);
			return (false);
		}
		if (thePayment.getAmount() == null) {
			// calculate remaining balance
			try {
				PaymentTransaction theTxn = (PaymentTransaction) theAppMgr
						.getStateObject("TXN_POS");
				// CMSPaymentTransactionAppModel appModel = new
				// CMSPaymentTransactionAppModel(theTxn);
				CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel) theTxn
						.getAppModel(CMSAppModelFactory.getInstance(),
								theAppMgr);
				ArmCurrency amt = appModel.getCompositeTotalAmountDue()
						.subtract(appModel.getTotalPaymentAmount());
				theAppMgr.setSingleEditArea(
						applet.res.getString("Enter amount."), "AMOUNT",
						amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
			} catch (Exception ex) {
				theAppMgr.setSingleEditArea(
						applet.res.getString("Enter amount."), "AMOUNT",
						theAppMgr.CURRENCY_MASK);
			}
			return (false);
		}
		if (manual) {
			((CreditCard) thePayment).setManuallyKeyed(true);
			// the following dlg boxed caused the edit area not to take any
			// additional key stroks.. jrg
			theAppMgr
					.showErrorDlg(applet.res
							.getString("Don't forget to get an imprint of the customer's card."));
		}
		return (true);
	}

	/**
   */
	private void resetAttributes() {
		thePayment = null;
		manual = false;
		digitVerify = false;
		accountNum = null;
		cardHolderName = null;
		expDate = null;
		serviceCode = null;
		cid = null;
	}

	/**
   */
	private void populateCreditCard() {
		CreditCard cc = (CreditCard) thePayment;
		if (accountNum != null) {
			cc.setAccountNumber(accountNum);
		}
		if (cardHolderName != null) {
			cc.setCreditCardHolderName(cardHolderName);
		}
		if (expDate != null) {
			Calendar cal = CreditCardBldrUtil.getCalendar(expDate);
			if (cal == null) {
				theAppMgr.showErrorDlg(applet.res
						.getString("The expiration date is not valid."));
			} else {
				Date dt = cal.getTime();
				cc.setExpirationDate(dt);
			}
		}
		if (rawData != null) {
			cc.setTrackData(rawData);
		}
		if (trackNumber != null) {
			cc.setTrackNumber(trackNumber);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param accountNum
	 */
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param cardHolderName
	 */
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param mmyyExpDate
	 */
	public void setExpDate(String mmyyExpDate) {
		this.expDate = mmyyExpDate;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param serviceCode
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param rawData
	 */
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param trackNumber
	 */
	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getServiceCode() {
		return this.serviceCode;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param inputStr
	 * @return
	 */
	public boolean getCreditCardInfo(String inputStr) {
		if (this.cmsMSR instanceof NonJavaPOSMSR)
			if (!(((NonJavaPOSMSR) cmsMSR).extractDataToBuilder(inputStr)))
				return (false);
		Calendar cal = CreditCardBldrUtil.getCalendar(expDate);
		if (cal == null)
			theAppMgr.showErrorDlg(applet.res
					.getString("The expiration date is not valid."));
		if (cal != null && !CreditCardBldrUtil.validateDate(cal)) {
			theAppMgr.showErrorDlg(applet.res
					.getString("This card has passed its expiration date."));
			return (false);
		}
		return (true);
	}

	/**
	 * @return
	 */
	public String getAccountNum() {
		return (accountNum);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param cmsMSR
	 */
	public void setCMSMSR(CMSMSR cmsMSR) {
		this.cmsMSR = cmsMSR;
	}
}
