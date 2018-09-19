/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ga.fs.fsbridge.object;

import com.ga.fs.fsbridge.local.ARMFSBTransactionItem;
import com.ga.fs.fsbridge.local.ARMFSBTransactionPayment;
import com.ga.fs.fsbridge.utils.ConfigurationManager;
import com.ga.fs.fsbridge.utils.DateUtils;
import com.ga.fs.fsbridge.utils.NumbersUtils;
import com.ga.fs.fsbridge.utils.TaxUtils;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.Reduction;
import com.chelseasystems.cs.discount.ArmLineDiscount;
import com.chelseasystems.cs.discount.CMSBasicDiscount;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author Yves Agbessi
 */
public class ARMFSBOReturnWithSalesTransactionObject extends ARMFSBridgeObject {

	/**
	 * ************ HEADER ************
	 */
	private int InterfaceVersion;
	private String ClientID;
	private String MessageTypeCode;
	private String MessageID;
	private Date MessageDateTime;
	private String CountryCode;
	private String ProjectCode;

	/**
	 * ************ BODY ************
	 */
	// TransactionInfo
	private String TransactionTypeCode;
	private Date TransactionDateTime;
	private String TransactionID;
	private boolean TrainingTransaction;

	// TransactionItems
	private ArrayList<ARMFSBTransactionItem> TransactionItems;

	// TotalDiscount
	private String TotalDiscountID;
	private String TotalDiscountName;
	private double TotalDiscountPercentage;
	private double TotalDiscountAmount;

	// TransactionTaxTotal
	private String TransactionTaxTotalTaxGroupCode;
	private double TransactionTaxTotalTaxPercent;
	private double TransactionTaxTotalTaxableAmount;
	private double TransactionTaxTotalTaxAmount;

	// NonTaxableFeeTotal
	private String NonTaxableFeeTotalFeeCode;
	private double NonTaxableFeeTotalFeeAmount;

	// TransactionTotal
	private double TransactionTotal;

	// TransactionPayments
	private ArrayList<ARMFSBTransactionPayment> TransactionPayments;

	// Operator
	private String OperatorID;
	private String OperatorDisplayName;

	private ConfigurationManager config = new ConfigurationManager();
	private final static String XML_FILE_PATH = xmlFolder
			+ "SalesWithReturnTransaction.xml";

	private ArrayList<HashMap<String, Object>> TransactionTaxArrayList = new ArrayList<HashMap<String, Object>>();

	boolean hasPromotion = false;
	String promId = null;
	String promReas = null;
	double promPerc = 0.0;

	/**
	 * @return the InterfaceVersion
	 */
	public int getInterfaceVersion() {
		return InterfaceVersion;
	}

	/**
	 * @param InterfaceVersion
	 *            the InterfaceVersion to set
	 */
	public void setInterfaceVersion(int InterfaceVersion) {
		this.InterfaceVersion = InterfaceVersion;
	}

	/**
	 * @return the ClientID
	 */
	public String getClientID() {
		return ClientID;
	}

	/**
	 * @param ClientID
	 *            the ClientID to set
	 */
	public void setClientID(String ClientID) {
		this.ClientID = ClientID;
	}

	/**
	 * @return the MessageTypeCode
	 */
	public String getMessageTypeCode() {
		return MessageTypeCode;
	}

	/**
	 * @param MessageTypeCode
	 *            the MessageTypeCode to set
	 */
	public void setMessageTypeCode(String MessageTypeCode) {
		this.MessageTypeCode = MessageTypeCode;
	}

	/**
	 * @return the MessageID
	 */
	public String getMessageID() {
		return MessageID;
	}

	/**
	 * @param MessageID
	 *            the MessageID to set
	 */
	public void setMessageID(String MessageID) {
		this.MessageID = MessageID;
	}

	/**
	 * @return the MessageDateTime
	 */
	public Date getMessageDateTime() {
		return MessageDateTime;
	}

	/**
	 * @param MessageDateTime
	 *            the MessageDateTime to set
	 */
	public void setMessageDateTime(Date MessageDateTime) {
		this.MessageDateTime = MessageDateTime;
	}

	/**
	 * @return the CountryCode
	 */
	public String getCountryCode() {
		return CountryCode;
	}

	/**
	 * @param CountryCode
	 *            the CountryCode to set
	 */
	public void setCountryCode(String CountryCode) {
		this.CountryCode = CountryCode;
	}

	/**
	 * @return the ProjectCode
	 */
	public String getProjectCode() {
		return ProjectCode;
	}

	/**
	 * @param ProjectCode
	 *            the ProjectCode to set
	 */
	public void setProjectCode(String ProjectCode) {
		this.ProjectCode = ProjectCode;
	}

	/**
	 * @return the TaxIdentityNumber
	 */
	public String getTaxIdentityNumber() {
		return TaxIdentityNumber;
	}

	/**
	 * @param TaxIdentityNumber
	 *            the TaxIdentityNumber to set
	 */
	public void setTaxIdentityNumber(String TaxIdentityNumber) {
		this.TaxIdentityNumber = TaxIdentityNumber;
	}

	/**
	 * @return the StoreID
	 */
	public String getStoreID() {
		return StoreID;
	}

	/**
	 * @param StoreID
	 *            the StoreID to set
	 */
	public void setStoreID(String StoreID) {
		this.StoreID = StoreID;
	}

	/**
	 * @return the WorkstationID
	 */
	public String getWorkstationID() {
		return WorkstationID;
	}

	/**
	 * @param WorkstationID
	 *            the WorkstationID to set
	 */
	public void setWorkstationID(String WorkstationID) {
		this.WorkstationID = WorkstationID;
	}

	/**
	 * @return the TransactionTypeCode
	 */
	public String getTransactionTypeCode() {
		return TransactionTypeCode;
	}

	/**
	 * @param TransactionTypeCode
	 *            the TransactionTypeCode to set
	 */
	public void setTransactionTypeCode(String TransactionTypeCode) {
		this.TransactionTypeCode = TransactionTypeCode;
	}

	/**
	 * @return the TransactionDateTime
	 */
	public Date getTransactionDateTime() {
		return TransactionDateTime;
	}

	/**
	 * @param TransactionDateTime
	 *            the TransactionDateTime to set
	 */
	public void setTransactionDateTime(Date TransactionDateTime) {
		this.TransactionDateTime = TransactionDateTime;
	}

	/**
	 * @return the TransactionID
	 */
	public String getTransactionID() {
		return TransactionID;
	}

	/**
	 * @param TransactionID
	 *            the TransactionID to set
	 */
	public void setTransactionID(String TransactionID) {
		this.TransactionID = TransactionID;
	}

	/**
	 * @return the TrainingTransaction
	 */
	public boolean isTrainingTransaction() {
		return TrainingTransaction;
	}

	/**
	 * @param TrainingTransaction
	 *            the TrainingTransaction to set
	 */
	public void setTrainingTransaction(boolean TrainingTransaction) {
		this.TrainingTransaction = TrainingTransaction;
	}

	/**
	 * @return the TransactionItems
	 */
	public ArrayList<ARMFSBTransactionItem> getTransactionItems() {
		return TransactionItems;
	}

	/**
	 * @param TransactionItems
	 *            the TransactionItems to set
	 */
	public void setTransactionItems(
			ArrayList<ARMFSBTransactionItem> TransactionItems) {
		this.TransactionItems = TransactionItems;
	}

	/**
	 * @return the TotalDiscountID
	 */
	public String getTotalDiscountID() {
		return TotalDiscountID;
	}

	/**
	 * @param TotalDiscountID
	 *            the TotalDiscountID to set
	 */
	public void setTotalDiscountID(String TotalDiscountID) {
		this.TotalDiscountID = TotalDiscountID;
	}

	/**
	 * @return the TotalDiscountDiscountName
	 */
	public String getTotalDiscountDiscountName() {
		return TotalDiscountName;
	}

	/**
	 * @param TotalDiscountDiscountName
	 *            the TotalDiscountDiscountName to set
	 */
	public void setTotalDiscountDiscountName(String TotalDiscountDiscountName) {
		this.TotalDiscountName = TotalDiscountDiscountName;
	}

	/**
	 * @return the TotalDiscountPercentage
	 */
	public double getTotalDiscountPercentage() {
		return TotalDiscountPercentage;
	}

	/**
	 * @param TotalDiscountPercentage
	 *            the TotalDiscountPercentage to set
	 */
	public void setTotalDiscountPercentage(double TotalDiscountPercentage) {
		this.TotalDiscountPercentage = TotalDiscountPercentage;
	}

	/**
	 * @return the TotalDiscountAmount
	 */
	public double getTotalDiscountAmount() {
		return TotalDiscountAmount;
	}

	/**
	 * @param TotalDiscountAmount
	 *            the TotalDiscountAmount to set
	 */
	public void setTotalDiscountAmount(double TotalDiscountAmount) {
		this.TotalDiscountAmount = TotalDiscountAmount;
	}

	/**
	 * @return the TransactionTaxTotalTaxGroupCode
	 */
	public String getTransactionTaxTotalTaxGroupCode() {
		return TransactionTaxTotalTaxGroupCode;
	}

	/**
	 * @param TransactionTaxTotalTaxGroupCode
	 *            the TransactionTaxTotalTaxGroupCode to set
	 */
	public void setTransactionTaxTotalTaxGroupCode(
			String TransactionTaxTotalTaxGroupCode) {
		this.TransactionTaxTotalTaxGroupCode = TransactionTaxTotalTaxGroupCode;
	}

	/**
	 * @return the TransactionTaxTotalTaxPercent
	 */
	public double getTransactionTaxTotalTaxPercent() {
		return TransactionTaxTotalTaxPercent;
	}

	/**
	 * @param TransactionTaxTotalTaxPercent
	 *            the TransactionTaxTotalTaxPercent to set
	 */
	public void setTransactionTaxTotalTaxPercent(
			double TransactionTaxTotalTaxPercent) {
		this.TransactionTaxTotalTaxPercent = TransactionTaxTotalTaxPercent;
	}

	/**
	 * @return the TransactionTaxTotalTaxableAmount
	 */
	public double getTransactionTaxTotalTaxableAmount() {
		return TransactionTaxTotalTaxableAmount;
	}

	/**
	 * @param TransactionTaxTotalTaxableAmount
	 *            the TransactionTaxTotalTaxableAmount to set
	 */
	public void setTransactionTaxTotalTaxableAmount(
			double TransactionTaxTotalTaxableAmount) {
		this.TransactionTaxTotalTaxableAmount = TransactionTaxTotalTaxableAmount;
	}

	/**
	 * @return the TransactionTaxTotalTaxAmount
	 */
	public double getTransactionTaxTotalTaxAmount() {
		return TransactionTaxTotalTaxAmount;
	}

	/**
	 * @param TransactionTaxTotalTaxAmount
	 *            the TransactionTaxTotalTaxAmount to set
	 */
	public void setTransactionTaxTotalTaxAmount(
			double TransactionTaxTotalTaxAmount) {
		this.TransactionTaxTotalTaxAmount = TransactionTaxTotalTaxAmount;
	}

	/**
	 * @return the NonTaxableFeeTotalFeeCode
	 */
	public String getNonTaxableFeeTotalFeeCode() {
		return NonTaxableFeeTotalFeeCode;
	}

	/**
	 * @param NonTaxableFeeTotalFeeCode
	 *            the NonTaxableFeeTotalFeeCode to set
	 */
	public void setNonTaxableFeeTotalFeeCode(String NonTaxableFeeTotalFeeCode) {
		this.NonTaxableFeeTotalFeeCode = NonTaxableFeeTotalFeeCode;
	}

	/**
	 * @return the NonTaxableFeeTotalFeeAmount
	 */
	public double getNonTaxableFeeTotalFeeAmount() {
		return NonTaxableFeeTotalFeeAmount;
	}

	/**
	 * @param NonTaxableFeeTotalFeeAmount
	 *            the NonTaxableFeeTotalFeeAmount to set
	 */
	public void setNonTaxableFeeTotalFeeAmount(
			double NonTaxableFeeTotalFeeAmount) {
		this.NonTaxableFeeTotalFeeAmount = NonTaxableFeeTotalFeeAmount;
	}

	/**
	 * @return the TransactionTotal
	 */
	public double getTransactionTotal() {
		return TransactionTotal;
	}

	/**
	 * @param TransactionTotal
	 *            the TransactionTotal to set
	 */
	public void setTransactionTotal(double TransactionTotal) {
		this.TransactionTotal = TransactionTotal;
	}

	/**
	 * @return the TransactionPayments
	 */
	public ArrayList<ARMFSBTransactionPayment> getTransactionPayments() {
		return TransactionPayments;
	}

	/**
	 * @param TransactionPayments
	 *            the TransactionPayments to set
	 */
	public void setTransactionPayments(
			ArrayList<ARMFSBTransactionPayment> TransactionPayments) {
		this.TransactionPayments = TransactionPayments;
	}

	/**
	 * @return the OperatorID
	 */
	public String getOperatorID() {
		return OperatorID;
	}

	/**
	 * @param OperatorID
	 *            the OperatorID to set
	 */
	public void setOperatorID(String OperatorID) {
		this.OperatorID = OperatorID;
	}

	/**
	 * @return the OperatorDisplayName
	 */
	public String getOperatorDisplayName() {
		return OperatorDisplayName;
	}

	/**
	 * @param OperatorDisplayName
	 *            the OperatorDisplayName to set
	 */
	public void setOperatorDisplayName(String OperatorDisplayName) {
		this.OperatorDisplayName = OperatorDisplayName;
	}

	@Override
	public int getType() {

		return ARMFSBridgeObjectTypes.SALES_TRANSACTION;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// C O N S T R U C T O R S & M E T H O D S
	//
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ARMFSBOReturnWithSalesTransactionObject(
			CMSCompositePOSTransaction transaction) {

		this.Document = XML_FILE_PATH;
		// Getting 'InterfaceVersion' value from arm.cfg
		String interfaceVersionConfigValue = config.getConfig(
				fsBridgeCfgFilePath, "FS_BRIDGE_INTERFACE_VERSION");
		this.InterfaceVersion = Integer.valueOf(interfaceVersionConfigValue);

		// Getting 'ClientID' value from register.cfg
		String clientIDConfigValue = config.getConfig(registerCfgFilePath,
				"STORE_ID")
				+ config.getConfig(registerCfgFilePath, "REGISTER_ID");
		this.ClientID = clientIDConfigValue;

		// Setting 'MessageTypeCode' value from available MessageTypeCodes
		this.MessageTypeCode = com.ga.fs.fsbridge.utils.MessageTypeCode.MTC_TRANSACTION;

		// MessageID = TransactionID as per internal policy
		this.MessageID = transaction.getId();
		//
		// Creating a new date for the 'MessageDateTime' value
		this.MessageDateTime = new Date();

		/**
		 * Getting 'CountryCode' value from arm.cfg. This line of code retrieves
		 * the LOCALE value and gets the substring starting from the 3rd
		 * character of the string Example : LOCALE = it_IT CountryCode = IT
		 */
		this.CountryCode = (config.getConfig(fsBridgeCfgFilePath, "LOCALE"))
				.substring(3);

		// Getting 'ProjectCode' value from arm.cfg
		this.ProjectCode = config.getConfig(fsBridgeCfgFilePath,
				"FS_BRIDGE_PROJECT_CODE");

		// Getting 'TaxIdentityNumber' (Company VAT Code) value from arm.cfg
		this.TaxIdentityNumber = config.getConfig(fsBridgeCfgFilePath,
				"FS_BRIDGE_TAX_IDENTITY_NUMBER");

		// Getting 'StoreID' value from register.cfg
		this.StoreID = config.getConfig(registerCfgFilePath, "STORE_ID");

		// Getting 'WorkstationID' value from register.cfg
		this.WorkstationID = config.getConfig(registerCfgFilePath,
				"REGISTER_ID");

		this.TransactionTypeCode = com.ga.fs.fsbridge.utils.TransactionTypeCode.TTC_RETURN_WITH_SALES;
		this.TransactionDateTime = transaction.getCreateDate();
		this.TransactionID = transaction.getId();
		this.TrainingTransaction = transaction.isTrainingFlagOn();

		this.TransactionItems = new ArrayList<ARMFSBTransactionItem>();

		double totDisc = 0.00;

		// TotalDiscountID
		this.TotalDiscountID = "NO_DISCOUNT";
		// TotalDiscountDiscountName
		this.TotalDiscountName = "NO_DISCOUNT";
		// --
		// TotalDiscountPercentage
		this.TotalDiscountPercentage = 0.00;

		String DISCID = "";
		String DISCREAS = "";
		double DISCPERC = 0.00;
		int cursorSales = 0;
		int cursorReturns = 0;

		for (POSLineItem item : transaction.getLineItemsArray()) {

			// Getting the discounts applied on the transaction item line

			// Creating a new ARMFBTransactionItem : retrieving all the info
			// from the transaction
			Discount[] discounts = item.getDiscountsArray();
			if (transaction.getSettlementDiscountsArray().length > 0) {

				if (transaction.getSettlementDiscountsArray()[0] instanceof CMSBasicDiscount) {

					DISCID = ((CMSBasicDiscount) transaction
							.getSettlementDiscountsArray()[0])
							.getDiscountCode();

					DISCREAS = ((CMSBasicDiscount) transaction
							.getSettlementDiscountsArray()[0]).getReason();

					DISCPERC = ((CMSBasicDiscount) transaction
							.getSettlementDiscountsArray()[0]).getPercent() * 100;

				}

				if (transaction.getSettlementDiscountsArray()[0] instanceof ArmLineDiscount) {

					DISCID = ((ArmLineDiscount) transaction
							.getSettlementDiscountsArray()[0])
							.getDiscountCode();

					DISCREAS = ((ArmLineDiscount) transaction
							.getSettlementDiscountsArray()[0]).getReason();

					DISCPERC = ((ArmLineDiscount) transaction
							.getSettlementDiscountsArray()[0]).getPercent() * 100;

				}

				String discId = "", discName = "";
				double discPerc = -0.00;

				if (transaction.getSettlementDiscountsArray().length > 0) {

					discounts = (transaction.getSettlementDiscountsArray());

				}

				else if (item instanceof CMSReturnLineItem) {
					discounts = ((CMSReturnLineItem) item).getDiscountsArray();

				} else {

					discounts = item.getDiscountsArray();
				}

				if (item instanceof CMSReturnLineItem) {

					ARMFSBTransactionItem i = new ARMFSBTransactionItem(
					// ItemID
							item.getItem().getId(),
							// Description
							item.getItem().getDescription(),
							// UnitDescription is set to 'PCS' (pieces) by
							// default
							"PCS",
							// RegularUnitPrice
							item.getItemRetailPrice().doubleValue(),
							// ActualUnitPrice
							-item.getNetAmount().doubleValue(),
							// Quantity
							-item.getQuantity(),
							// TotalLineAmount
							item.getTotalAmountDue().doubleValue(),
							// -----
							/**
							 * TaxGroupCode IF Zero is applied on the item then
							 * TaxGroupCode = 'Zero' ELSE TaxGroupCode =
							 * 'Normal'
							 */
							item.isTaxExempt() ? "Zero" : "Normal",
							// ItemTaxPercent
							item.getItem().getVatRate().doubleValue() * 100,
							// Calculating 'TaxableAmount'
							TaxUtils.getTaxableAmount(item.getTotalAmountDue()
									.doubleValue()),
							// TaxAmount
							TaxUtils.getTaxAmount(item.getTotalAmountDue()
									.doubleValue()),
							// -----
							/**
							 * Only the first discount data are taken in
							 * consideration for the header
							 */
							// DiscountID
							DISCID,
							// Discount reason
							DISCREAS,
							// Discount percent (Total percent discount on the
							// line)
							DISCPERC,
							/*
							 * Discount UnitAmount
							 */
							discounts.length > 0 ? -(item.getItemRetailPrice()
									.doubleValue() - item.getNetAmount()
									.doubleValue()) : 0.0,
							// Discount Amount
							discounts.length > 0 ? -(item.getItemRetailPrice()
									.doubleValue() - item.getNetAmount()
									.doubleValue() * item.getQuantity()) : 0.0);
					totDisc = totDisc
							+ -(item.getItemRetailPrice().doubleValue() - item
									.getNetAmount().doubleValue()
									* item.getQuantity());
					// Adding the item to the TransactionItems list
					TransactionItems.add(i);
					if (discounts.length > 0) {
						if (discId.equals("")) {

							discId = DISCID;
							this.TotalDiscountID = discId;
						}

						if (discName.equals("")) {
							discName = DISCREAS;
							this.TotalDiscountName = discName;
						}

						if (discPerc == -0.00) {
							discPerc = DISCPERC * 100;
							this.TotalDiscountPercentage = discPerc;
						}
					}

				}
			}

			else {

				String discID = "";
				String discReason = "";
				double discPerc = 0.0;

				if (discounts.length > 0) {

					if (discounts[0] instanceof ArmLineDiscount) {
						discID = ((ArmLineDiscount) discounts[0])
								.getDiscountCode();

						discReason = ((ArmLineDiscount) discounts[0])
								.getReason();
					}

					if (discounts[0] instanceof CMSBasicDiscount) {

						discID = ((CMSBasicDiscount) discounts[0])
								.getDiscountCode();

						discReason = ((CMSBasicDiscount) discounts[0])
								.getReason();

					}

				}

				boolean itemHasPromotion = false;
				if (discounts.length == 0 || discounts == null) {
					Reduction[] reductionsArray = null;

					if (item instanceof CMSSaleLineItem) {

						reductionsArray = ((POSLineItemDetail) ((CMSSaleLineItem) (transaction
								.getSaleTransaction().getCompositeTransaction()
								.getSaleTransaction().getLineItemsArray()[cursorSales]))
								.getLineItemDetailsArray()[0])
								.getReductionsArray();

						cursorSales++;
					}

					if (item instanceof CMSReturnLineItem) {

						reductionsArray = ((POSLineItemDetail) ((CMSReturnLineItem) (transaction
								.getReturnTransaction()
								.getCompositeTransaction()
								.getReturnTransaction().getLineItemsArray()[cursorReturns]))
								.getLineItemDetailsArray()[0])
								.getReductionsArray();

						cursorReturns++;

					}

					if (reductionsArray != null && reductionsArray.length > 0) {

						discID = reductionsArray[0].getReason();
						discReason = reductionsArray[0].getReason();
						discPerc = ((Double) (item.getNetAmount().doubleValue() / (item
								.getItemRetailPrice().doubleValue() / 100)));
						discPerc = NumbersUtils.round(discPerc, 2);
						hasPromotion = true;
						itemHasPromotion = true;
						promId = discID;
						promReas = discReason;
						promPerc = discPerc;
					}

				}

				if ((discounts.length == 0 || discounts == null)
						&& itemHasPromotion == false) {
					discID = "NO_DISCOUNT";
					discReason = "NO_DISCOUNT";
					discPerc = 0.0;

				}

				ARMFSBTransactionItem i = new ARMFSBTransactionItem(
				// ItemID
						item.getItem().getId(),
						// Description
						item.getItem().getDescription(),
						// UnitDescription is set to 'PCS' (pieces) by default
						"PCS",
						// RegularUnitPrice
						item.getItemRetailPrice().doubleValue(),
						// ActualUnitPrice
						item.getNetAmount().doubleValue(),
						// Quantity
						item instanceof CMSReturnLineItem ? -item.getQuantity()
								: item.getQuantity(),
						// TotalLineAmount
						item instanceof CMSReturnLineItem ? -item
								.getTotalAmountDue().doubleValue() : -item
								.getTotalAmountDue().doubleValue(),
						// -----
						/**
						 * TaxGroupCode IF Zero is applied on the item then
						 * TaxGroupCode = 'Zero' ELSE TaxGroupCode = 'Normal'
						 */
						item.isTaxExempt() ? "Zero" : "Normal",
						// ItemTaxPercent
						item.getItem().getVatRate().doubleValue() * 100,
						// Calculating 'TaxableAmount'
						TaxUtils.getTaxableAmount(item.getTotalAmountDue()
								.doubleValue()),
						// TaxAmount
						TaxUtils.getTaxAmount(item.getTotalAmountDue()
								.doubleValue()),
						// -----
						/**
						 * Only the first discount data are taken in
						 * consideration for the header
						 */
						// DiscountID
						discID,
						// Discount reason
						discReason,
						// Discount percent (Total percent discount on the line)
						discPerc,
						/*
						 * Discount UnitAmount retrieved by performing this
						 * operation ->
						 * 
						 * TotalDiscountAmount(of the line) / Line items
						 * quantity
						 */

						discounts.length > 0 ? item.getDiscount().getAmount()
								.doubleValue()
								/ item.getQuantity() : 0.0,
						// Discount Amount
						discounts.length > 0 ? item.getDiscount().getAmount()
								.doubleValue() : 0.0);
				totDisc = totDisc
						+ (item.getItemRetailPrice().doubleValue() - item
								.getNetAmount().doubleValue()
								* item.getQuantity());
				// Adding the item to the TransactionItems list
				TransactionItems.add(i);
			}
		}
		/**
		 * Only the first discount data are taken in consideration for the
		 * header
		 */

		String totDiscountID = "";
		String totReason = "";
		double totPercent = 0.00;

		if (transaction.getDiscountsArray().length > 0) {
			if (transaction.getDiscountsArray()[0] instanceof CMSBasicDiscount) {

				totDiscountID = ((CMSBasicDiscount) (transaction
						.getDiscountsArray()[0])).getDiscountCode();
				totReason = ((CMSBasicDiscount) (transaction
						.getDiscountsArray()[0])).getReason();
				totPercent = ((CMSBasicDiscount) (transaction
						.getDiscountsArray()[0])).getPercent() * 100;
			}

			if (transaction.getDiscountsArray()[0] instanceof ArmLineDiscount) {
				totDiscountID = ((ArmLineDiscount) (transaction
						.getDiscountsArray()[0])).getDiscountCode();
				totReason = ((ArmLineDiscount) (transaction.getDiscountsArray()[0]))
						.getReason();
				totPercent = ((ArmLineDiscount) (transaction
						.getDiscountsArray()[0])).getPercent() * 100;

			}
		}

		if ((transaction.getDiscountsArray().length == 0 || transaction
				.getDiscountsArray() == null) && hasPromotion) {
			totDiscountID = promId;
			totReason = promReas;
			totPercent = promPerc;

		}

		if ((transaction.getDiscountsArray().length == 0 || transaction
				.getDiscountsArray() == null) && hasPromotion == false) {
			totDiscountID = "NO_DISCOUNT";
			totReason = "NO_DISCOUNT";
			totPercent = 0.0;

		}

		if (transaction.getSettlementDiscountsArray().length > 0) {
			this.TotalDiscountID = totDiscountID;
			this.TotalDiscountName = totReason;
			this.TotalDiscountPercentage = totPercent;

		}

		// TotalDiscountAmount
		this.TotalDiscountAmount = totDisc;

		/*
		 * START TAX TOTALS
		 */

		// Retrieving eventual different VAT Rates in the transaction
		ArrayList<Double> transactionVatRates = new ArrayList<Double>();
		for (POSLineItem pItem : transaction.getLineItemsArray()) {
			// The VATRates are only considered for items in Sale

			double itemVatRate = pItem.getItem().getVatRate();
			if (!transactionVatRates.contains(itemVatRate)) {

				transactionVatRates.add(itemVatRate);
			}

		}

		/*
		 * Creating Hashmap to store vat rates and their amounts
		 * HashMap<VatRate,TotalAmountForVatRate>
		 */

		HashMap<Double, Double> vatRatesHashMap = new HashMap<Double, Double>();
		for (POSLineItem pItem : transaction.getLineItemsArray()) {
			double itemVatRate = pItem.getItem().getVatRate();
			double itemAmount = pItem.getNetAmount().doubleValue();
			if (vatRatesHashMap.containsKey(itemVatRate)) {
				double currentKeyValue = vatRatesHashMap.get(itemVatRate);
				double updatedKeyValue = (pItem instanceof CMSReturnLineItem) ? currentKeyValue
						- itemAmount
						: currentKeyValue + itemAmount;
				vatRatesHashMap.put(itemVatRate, updatedKeyValue);

			} else {
				vatRatesHashMap.put(itemVatRate,
						(pItem instanceof CMSReturnLineItem) ? -itemAmount
								: itemAmount);

			}

		}

		for (Map.Entry<Double, Double> mapEntry : vatRatesHashMap.entrySet()) {

			HashMap<String, Object> taxRateMap = new HashMap<String, Object>();
			// --
			// TransactionTaxTotalTaxGroupCode
			this.TransactionTaxTotalTaxGroupCode = (mapEntry.getKey() * 100 == Double
					.valueOf(config.getConfig(fsBridgeCfgFilePath,
							"TAX_PERCENT")) ? "Normal" : "Reduced");

			this.TransactionTaxTotalTaxGroupCode = transaction.isTaxExempt() ? "Zero"
					: this.TransactionTaxTotalTaxGroupCode;

			taxRateMap.put("TransactionTaxTotalTaxGroupCode",
					this.TransactionTaxTotalTaxGroupCode);

			// TransactionTaxTotalTaxPercent
			this.TransactionTaxTotalTaxPercent = mapEntry.getKey() * 100;

			taxRateMap.put("TransactionTaxTotalTaxPercent",
					this.TransactionTaxTotalTaxPercent);

			this.TransactionTotal = transaction.getTotalPaymentAmount()
					.doubleValue();

			if (this.TransactionTotal != 0.0) {

				// TransactionTaxTotalTaxableAmount
				this.TransactionTaxTotalTaxableAmount = TaxUtils
						.getTaxableAmount(mapEntry.getValue());

				taxRateMap.put("TransactionTaxTotalTaxableAmount",
						this.TransactionTaxTotalTaxableAmount);

				// TransactionTaxTotalTaxAmount
				this.TransactionTaxTotalTaxAmount = TaxUtils
						.getTaxAmount(mapEntry.getValue());

				taxRateMap.put("TransactionTaxTotalTaxAmount",
						this.TransactionTaxTotalTaxAmount);

				TransactionTaxArrayList.add(taxRateMap);
			} else {

				this.TransactionTaxTotalTaxableAmount = 0.0;
				this.TransactionTaxTotalTaxAmount = 0.0;

			}
		}

		/*
		 * 
		 * END TAX TOTALS
		 */

		// TransactionTotal

		// TransactionPayments
		this.TransactionPayments = new ArrayList<ARMFSBTransactionPayment>();
		for (Payment p : transaction.getPaymentsArray()) {

			String PaymentMediaCode = p.getGUIPaymentName();
			double AmountLocal = p.getAmount().doubleValue();
			ARMFSBTransactionPayment payment = new ARMFSBTransactionPayment(
					PaymentMediaCode, AmountLocal);
			TransactionPayments.add(payment);

		}
		if (this.TransactionPayments.size() == 0) {

			this.TransactionPayments.add(new ARMFSBTransactionPayment("", 0.0));
		}

		// OperatorID
		this.OperatorID = transaction.getTheOperator().getExternalID();
		// OperatorDisplayName
		this.OperatorDisplayName = transaction.getTheOperator().getFirstName()
				+ " " + transaction.getTheOperator().getLastName();

		// Seller
		Employee seller = transaction.getConsultant();
		this.SellerID = seller.getExternalID();
		this.SellerDisplayName = seller.getFirstName() + " "
				+ seller.getLastName();
	}

	@Override
	public boolean generateXML() {
		System.out.println("[FISCAL SOLUTIONS BRIDGE] : Generating xml...");
		try {

			// ---------------------------------------------------------------------------------
			//
			// Initializing xml document

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			/*
			 * ------------------------------------------------------------------
			 * ---------------
			 */

			// Filling the document
			// Appending the default root (Message) element
			Element rootElement = doc.createElement("Message");
			doc.appendChild(rootElement);

			// Appending Request element
			Element Request = doc.createElement("Request");
			rootElement.appendChild(Request);

			/* * * * * H E A D E R * * * * */
			// --
			// Creating Header element
			Element Header = doc.createElement("Header");
			Request.appendChild(Header);
			// Creating Header's elements
			ArrayList<Element> headerElements = new ArrayList<Element>();

			Element InterfaceVersion = doc.createElement("InterfaceVersion");
			InterfaceVersion.appendChild(doc.createTextNode(String
					.valueOf(this.InterfaceVersion)));
			headerElements.add(InterfaceVersion);

			Element ClientID = doc.createElement("ClientID");
			ClientID.appendChild(doc.createTextNode(String
					.valueOf(this.ClientID)));
			headerElements.add(ClientID);

			Element MessageTypeCode = doc.createElement("MessageTypeCode");
			MessageTypeCode.appendChild(doc.createTextNode(String
					.valueOf(this.MessageTypeCode)));
			headerElements.add(MessageTypeCode);

			Element MessageID = doc.createElement("MessageID");
			MessageID.appendChild(doc.createTextNode(String
					.valueOf(this.MessageID)));
			headerElements.add(MessageID);

			Element MessageDateTime = doc.createElement("MessageDateTime");
			MessageDateTime
					.appendChild(doc.createTextNode(String.valueOf(DateUtils
							.formatDateForXML(this.MessageDateTime))));
			headerElements.add(MessageDateTime);

			Element CountryCode = doc.createElement("CountryCode");
			CountryCode.appendChild(doc.createTextNode(String
					.valueOf(this.CountryCode)));
			headerElements.add(CountryCode);

			Element ProjectCode = doc.createElement("ProjectCode");
			ProjectCode.appendChild(doc.createTextNode(String
					.valueOf(this.ProjectCode)));
			headerElements.add(ProjectCode);

			Element TaxIdentityNumber = doc.createElement("TaxIdentityNumber");
			TaxIdentityNumber.appendChild(doc.createTextNode(String
					.valueOf(this.TaxIdentityNumber)));
			headerElements.add(TaxIdentityNumber);

			Element StoreID = doc.createElement("StoreID");
			StoreID.appendChild(doc.createTextNode(String.valueOf(this.StoreID)));
			headerElements.add(StoreID);

			Element WorkstationID = doc.createElement("WorkstationID");
			WorkstationID.appendChild(doc.createTextNode(String
					.valueOf(this.WorkstationID)));
			headerElements.add(WorkstationID);

			// ****** Appending Header's elements *******//

			for (Element e : headerElements) {

				Header.appendChild(e);
			}

			// --
			// --
			/* * * * * E N D O F H E A D E R * * * * */

			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//

			/* * * * * B O D Y * * * * */
			// --

			// Appending 'Body' element
			Element Body = doc.createElement("Body");
			Request.appendChild(Body);

			ArrayList<Element> bodyElements = new ArrayList<Element>();

			// Creating and appending 'TransactionInfo' element
			Element TransactionInfo = doc.createElement("TransactionInfo");
			Body.appendChild(TransactionInfo);
			bodyElements.add(TransactionInfo);

			Element TransactionTypeCode = doc
					.createElement("TransactionTypeCode");
			TransactionTypeCode.appendChild(doc.createTextNode(String
					.valueOf(this.TransactionTypeCode)));
			TransactionInfo.appendChild(TransactionTypeCode);

			Element TransactionDateTime = doc
					.createElement("TransactionDateTime");
			TransactionDateTime.appendChild(doc.createTextNode(String
					.valueOf(DateUtils
							.formatDateForXML(this.TransactionDateTime))));
			TransactionInfo.appendChild(TransactionDateTime);

			Element TransactionID = doc.createElement("TransactionID");
			TransactionID.appendChild(doc.createTextNode(String
					.valueOf(this.TransactionID)));
			TransactionInfo.appendChild(TransactionID);

			Element TrainingTransaction = doc
					.createElement("TrainingTransaction");
			TrainingTransaction.appendChild(doc.createTextNode(String
					.valueOf(this.TrainingTransaction)));
			TransactionInfo.appendChild(TrainingTransaction);

			/*
			 * Creating and appending 'TransactionItems' element
			 * 
			 * -
			 */

			Element TransactionItems = doc.createElement("TransactionItems");
			Body.appendChild(TransactionItems);
			bodyElements.add(TransactionItems);

			for (ARMFSBTransactionItem item : this.TransactionItems) {

				Element Item = doc.createElement("Item");
				TransactionItems.appendChild(Item);

				/**
				 * Getting all the variables in the ARMFBTransactionItem class
				 * to automatically generate the xml entries
				 */
				Field[] fields = ARMFSBTransactionItem.class.getFields();

				for (int i = 0; i < 16; i++) {

					Element e = null;

					Field f = fields[i];

					if (i < 7) {
						e = doc.createElement(f.getName());
						// System.out.println("get"+f.getName());

						// Retrieving 'get' method of the specific field in
						// ARMFBTransactionItem class to create the xml entry
						Method m = ARMFSBTransactionItem.class
								.getDeclaredMethod("get" + f.getName(), null);
						e.appendChild(doc.createTextNode(String.valueOf(m
								.invoke(item))));
						Item.appendChild(e);
					}

					if (i == 7) {

						// Creating and appending 'TaxLineItem' element
						Element TaxLineItem = doc.createElement("TaxLineItem");
						Item.appendChild(TaxLineItem);

						// Creating and appending 'TaxLineItem' elements
						for (int x = 0; x < 4; x++) {
							f = fields[i];
							e = doc.createElement(f.getName());
							Method m = ARMFSBTransactionItem.class
									.getDeclaredMethod("get" + f.getName(),
											null);
							e.appendChild(doc.createTextNode(String.valueOf(m
									.invoke(item))));
							TaxLineItem.appendChild(e);
							i++;

						}

						continue;

					}

					if (!item.getDiscountID().equals("NO_DISCOUNT")) {// /////////////
						if (i == 12) {
							// Creating and appending 'DiscountLineItem' element
							Element DiscountLineItem = doc
									.createElement("DiscountLineItem");
							Item.appendChild(DiscountLineItem);
							i = i - 1;
							// Creating and appending 'DiscountLineItem'
							// elements
							for (int x = 0; x < 5; x++) {
								f = fields[i];
								if (i == 11 || i == 12) {
									e = doc.createElement(f.getName());
								} else {
									e = doc.createElement(f.getName()
											.substring("Discount".length()));
								}

								Method m = ARMFSBTransactionItem.class
										.getDeclaredMethod("get" + f.getName(),
												null);
								e.appendChild(doc.createTextNode(String
										.valueOf(m.invoke(item))));
								DiscountLineItem.appendChild(e);
								i++;

							}

							break;

						}
					}// /////////////

				}

			}

			/*
			 * End of 'TransactionItems' fields
			 * 
			 * -
			 */
			// ---
			// ---

			/*
			 * Creating and appending 'TotalDiscount' element
			 * 
			 * -
			 */
			if (!this.TotalDiscountID.equals("NO_DISCOUNT")) {
				Element TotalDiscount = doc.createElement("TotalDiscount");
				Body.appendChild(TotalDiscount);
				bodyElements.add(TotalDiscount);

				Element TotalDiscountID = doc.createElement("TotalDiscountID");
				TotalDiscountID.appendChild(doc.createTextNode(String
						.valueOf(this.TotalDiscountID)));
				TotalDiscount.appendChild(TotalDiscountID);

				Element TotalDiscountName = doc.createElement("DiscountName");
				TotalDiscountName.appendChild(doc.createTextNode(String
						.valueOf(this.TotalDiscountName)));
				TotalDiscount.appendChild(TotalDiscountName);

				Element TotalDiscountPercentage = doc
						.createElement("Percentage");
				TotalDiscountPercentage.appendChild(doc.createTextNode(String
						.valueOf(this.TotalDiscountPercentage)));
				TotalDiscount.appendChild(TotalDiscountPercentage);

				Element TotalDiscountAmount = doc.createElement("Amount");
				TotalDiscountAmount.appendChild(doc.createTextNode(String
						.valueOf(this.TotalDiscountAmount)));
				TotalDiscount.appendChild(TotalDiscountAmount);
			}

			/*
			 * End of 'TotalDiscount' fields
			 * 
			 * -
			 */
			// ---
			// ---

			/*
			 * Creating and appending 'TransactionTax' element
			 * 
			 * -
			 */
			Element TransactionTax = doc.createElement("TransactionTax");
			Body.appendChild(TransactionTax);
			bodyElements.add(TransactionTax);

			for (HashMap<String, Object> map : TransactionTaxArrayList) {

				// Creating and appending 'TransactionTaxTotal' element
				Element TransactionTaxTotal = doc
						.createElement("TransactionTaxTotal");
				TransactionTax.appendChild(TransactionTaxTotal);

				Element TaxGroupCode = doc.createElement("TaxGroupCode");
				TaxGroupCode.appendChild(doc.createTextNode(String.valueOf(map
						.get("TransactionTaxTotalTaxGroupCode"))));
				TransactionTaxTotal.appendChild(TaxGroupCode);

				Element TaxPercent = doc.createElement("TaxPercent");
				TaxPercent.appendChild(doc.createTextNode(String.valueOf(map
						.get("TransactionTaxTotalTaxPercent"))));
				TransactionTaxTotal.appendChild(TaxPercent);

				Element TaxableAmount = doc.createElement("TaxableAmount");
				TaxableAmount.appendChild(doc.createTextNode(String.valueOf(map
						.get("TransactionTaxTotalTaxableAmount"))));
				TransactionTaxTotal.appendChild(TaxableAmount);

				Element TaxAmount = doc.createElement("TaxAmount");
				TaxAmount.appendChild(doc.createTextNode(String.valueOf(map
						.get("TransactionTaxTotalTaxAmount"))));
				TransactionTaxTotal.appendChild(TaxAmount);

			}

			/*
			 * End of 'TransactionTax' fields
			 * 
			 * -
			 */
			// ---
			// ---

			/*
			 * Creating and appending 'TransactionTotal' element
			 * 
			 * -
			 */
			Element TransactionTotal = doc.createElement("TransactionTotal");
			Body.appendChild(TransactionTotal);
			bodyElements.add(TransactionTotal);

			Element TransactionTotalAmount = doc.createElement("Amount");
			TransactionTotalAmount.appendChild(doc.createTextNode(String
					.valueOf(this.TransactionTotal)));
			TransactionTotal.appendChild(TransactionTotalAmount);

			/*
			 * End of 'TransactionTotal' fields
			 * 
			 * -
			 */
			// ---
			// ---

			/*
			 * Creating and appending 'TransactionPayments' element
			 * 
			 * -
			 */

			Element TransactionPayments = doc
					.createElement("TransactionPayments");
			Body.appendChild(TransactionPayments);
			bodyElements.add(TransactionPayments);

			for (ARMFSBTransactionPayment payment : this.TransactionPayments) {

				Element PaymentMedia = doc.createElement("PaymentMedia");
				TransactionPayments.appendChild(PaymentMedia);

				/**
				 * Getting all the variables in the ARMFBTransactionItem class
				 * to automatically generate the xml entries
				 */
				Field[] fields = ARMFSBTransactionPayment.class.getFields();

				for (Field f : fields) {

					Element e = doc.createElement(f.getName());
					// System.out.println("get"+f.getName());

					// Retrieving 'get' method of the specific field in
					// ARMFBTransactionItem class to create the xml entry
					Method m = ARMFSBTransactionPayment.class
							.getDeclaredMethod("get" + f.getName(), null);
					e.appendChild(doc.createTextNode(String.valueOf(m
							.invoke(payment))));
					PaymentMedia.appendChild(e);

				}

			}

			/*
			 * End of 'TransactionPayments' fields
			 * 
			 * -
			 */
			// ---
			// ---

			/*
			 * Creating and appending 'Operator' element
			 * 
			 * -
			 */
			Element Operator = doc.createElement("Operator");
			Body.appendChild(Operator);
			bodyElements.add(Operator);
			
			
			//Cashier
			Element OperatorID = doc.createElement("OperatorID");
			OperatorID.appendChild(doc.createTextNode(String
					.valueOf(this.OperatorID)));
			Operator.appendChild(OperatorID);

			Element OperatorDisplayName = doc
					.createElement("OperatorDisplayName");
			OperatorDisplayName.appendChild(doc.createTextNode(String
					.valueOf(this.OperatorDisplayName)));
			Operator.appendChild(OperatorDisplayName);
			
			Element OperatorTypeCode = doc
					.createElement("OperatorTypeCode");
			OperatorTypeCode.appendChild(doc.createTextNode(String
					.valueOf("Cashier")));
			Operator.appendChild(OperatorTypeCode);
			
			
			//Seller
			
			Element Operator2 = doc.createElement("Operator");
			Body.appendChild(Operator2);
			bodyElements.add(Operator2);
			
			Element OperatorID2 = doc.createElement("OperatorID");
			OperatorID2.appendChild(doc.createTextNode(String
					.valueOf(this.SellerID)));
			Operator2.appendChild(OperatorID2);

			Element OperatorDisplayName2 = doc
					.createElement("OperatorDisplayName");
			OperatorDisplayName2.appendChild(doc.createTextNode(String
					.valueOf(this.SellerDisplayName)));
			Operator2.appendChild(OperatorDisplayName2);
			
			Element OperatorTypeCode2 = doc.createElement("OperatorTypeCode");
			OperatorTypeCode2.appendChild(doc.createTextNode(String
					.valueOf("Seller")));
			Operator2.appendChild(OperatorTypeCode2);

			/*
			 * End of 'Operator' fields
			 * 
			 * -
			 */
			// ---
			// ---
			
			//FISCAL DATA			
			Element FiscalData = doc.createElement("FiscalData");
			Body.appendChild(FiscalData);
			bodyElements.add(FiscalData);
			
			//SIRET
			Element FiscalValue1 = doc.createElement("FiscalValue");
			FiscalData.appendChild(FiscalValue1);
			
			Element FiscalValueCode1 = doc.createElement("FiscalValueCode");
			FiscalValueCode1.appendChild(doc.createTextNode(String
					.valueOf("CustomerCustomerNumber")));
			FiscalValue1.appendChild(FiscalValueCode1);
			
			Element Value = doc.createElement("Value");
			Value.appendChild(doc.createTextNode(String
					.valueOf(this.SIRET)));
			FiscalValue1.appendChild(Value);
			
			//NAF
			Element FiscalValue2 = doc.createElement("FiscalValue");
			FiscalData.appendChild(FiscalValue2);
			
			Element FiscalValueCode2 = doc.createElement("FiscalValueCode");
			FiscalValueCode2.appendChild(doc.createTextNode(String
					.valueOf("CustomerCustomerCode")));
			FiscalValue2.appendChild(FiscalValueCode2);
			
			Element Value2 = doc.createElement("Value");
			Value2.appendChild(doc.createTextNode(String
					.valueOf(this.NAF)));
			FiscalValue2.appendChild(Value2);

			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//

			// ****** Appending Body's elements *******//

			for (Element e : bodyElements) {

				Body.appendChild(e);
			}

			// --
			// --
			/* * * * * E N D O F B O D Y * * * * */

			// --------------------------------------------------------------------------------------------------------//

			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//
			// --------------------------------------------------------------------------------------------------------//

			// ---------------------------------------------------------------------------------
			// Generating and saving xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(
					new File(XML_FILE_PATH).getPath());
			transformer.transform(source, result);
			System.out
					.println("[FISCAL SOLUTIONS BRIDGE] : xml file generated. Available in "
							+ XML_FILE_PATH);
			System.out.println("");
			setXmlDocument(doc);
			/*
			 * ------------------------------------------------------------------
			 * ---------------
			 */

		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
			return false;
		} catch (TransformerConfigurationException ex) {
			Logger.getLogger(
					ARMFSBOReturnWithSalesTransactionObject.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		} catch (TransformerException ex) {
			Logger.getLogger(
					ARMFSBOReturnWithSalesTransactionObject.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(
					ARMFSBOReturnWithSalesTransactionObject.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		} catch (SecurityException ex) {
			Logger.getLogger(
					ARMFSBOReturnWithSalesTransactionObject.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		} catch (IllegalAccessException ex) {
			Logger.getLogger(
					ARMFSBOReturnWithSalesTransactionObject.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(
					ARMFSBOReturnWithSalesTransactionObject.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		} catch (InvocationTargetException ex) {
			Logger.getLogger(
					ARMFSBOReturnWithSalesTransactionObject.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		}

		return true;

	}



}
