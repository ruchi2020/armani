//
// Copyright 1999-2002, Chelsea Market Systems
//

package com.chelseasystems.cr.pos;

import java.util.*;

import com.chelseasystems.cr.business.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cs.util.Version;

// ***************************
// *** Class POS LINE ITEM ***
// ***************************

/**
 *  Abstract class for all POS line items.
 *  @author (rev. a) Mark Mayfield
 *  @version 1.0a
 **/
public abstract class POSLineItem extends BusinessObject implements Comparable {
	// **************************
	// *** DEFINE - CONSTANTS ***
	// **************************

	private static final long serialVersionUID = -5135889651140126558L;

	// ***************************
	// *** DEFINE - PROPERTIES ***
	// ***************************

	private POSTransaction transaction;
	private Item item;
	private String extendedBarCode;
	private ArmCurrency itemRetailPrice;
	private ArmCurrency itemSellingPrice;
	private Integer quantity;
	private Vector lineItemDetails;
	private boolean validNetAmountFlag;
	private ArmCurrency manualMarkdownAmount;
	private String manualMarkdownReason;
	private Vector discounts;
	private ArmCurrency manualUnitPrice;
	private Employee additionalConsultant;
	private int sequenceNumber;
	private String taxExemptId;
	private String regionalTaxExemptId;
	private boolean deletedFlag;
	private String miscItemId;
	private String miscItemDescription;
	private Boolean miscItemTaxable;
	private Boolean miscItemRegionalTaxable;
	private String miscItemGLAccount;
	private String miscItemComment;	
	//added by shushma for promotion code
	private String promotionCode;
	//Added by Rachana for apporval of return transaction
	private String approverId;
	/**
	 * The line item grouping does not need to be persisted.
	 */
	private POSLineItemGrouping lineItemGrouping;
	private ArmCurrency stateTax;
	private ArmCurrency cityTax;
//sonali:PriceOvveride
	  private ArmCurrency overrideAmount;
//ends

  //Test
	private ArmCurrency gstTaxAmt;
	private ArmCurrency pstTaxAmt;
	private ArmCurrency qstTaxAmt;
	private ArmCurrency gsthstTaxAmt;
    //Ends
	
	//Vivek Mishra : Added for Extended Barcode CR Europe 06-OCT-2016
	private String extendedStagingBarCode = null;
	//Ends here 06-OCT-2016
	
	//Vivek Mishra : Added to capture track data from 107 gift card swipe response 27-SEP-2016
	private String trackData = null;
	//Vivek Mishra : Added to capture AJB sequence number for gift card activation and reload 27-OCT-2016
	private String ajbSequence = null;
	// vishal yevale : Added to print reloaded gc balance
	private ArmCurrency giftCardBalance = null;
	private boolean isApplicableForPromotion = true; // Added by Himani for Promotions Best Deal CR
	// **********************
	// *** INITIALIZATION ***
	// **********************

	/**
	 * Constructs a POS Line Item using the passed pos transaction's composite pos transction to get
	 * the next sequence number.
	 * @param aTransaction The pos transaction that contains it.
	 * @param anItem The item for the line item.
	 * @see POSTransaction#getCompositeTransaction
	 * @see CompositePOSTransaction#getNextSequenceNumber
	 * @see POSLineItem#POSLineItem(POSTransaction, Item, int)
	 */
	//  public POSLineItem(POSTransaction aTransaction, Item anItem)
	//  {
	//    this(aTransaction, anItem, aTransaction.getCompositeTransaction().getNextSequenceNumber());
	//  }
	/**
	 * Constructs a POS Line Item.
	 * @param aTransaction The pos transaction that contains it.
	 * @param anItem The item for the line item.
	 * @param aSequenceNumber An integer that represents the sequence number.
	 * @see POSTransaction#doAddLineItem(POSLineItem)
	 */
	public POSLineItem(POSTransaction aTransaction, Item anItem, int aSequenceNumber) {
		this.transaction = aTransaction;
		this.item = anItem;
		this.extendedBarCode = null;
		this.sequenceNumber = aSequenceNumber;
		this.transaction.doAddLineItem(this);
          
		this.itemRetailPrice = null;
		this.itemSellingPrice = null;
		this.quantity = new Integer(0);
		this.lineItemDetails = new Vector(0);
		this.validNetAmountFlag = true;
		this.manualMarkdownAmount = new ArmCurrency(this.getBaseCurrencyType(), 0.0);
		this.manualMarkdownReason = new String();
		this.discounts = new Vector();
		this.manualUnitPrice = null;
		this.additionalConsultant = null;
		this.taxExemptId = new String();
		this.regionalTaxExemptId = new String();
		this.deletedFlag = false;
		this.miscItemId = null;
		this.miscItemDescription = null;
		this.miscItemTaxable = null;
		this.miscItemRegionalTaxable = null;
		this.miscItemGLAccount = null;
		this.miscItemComment = null;
		this.lineItemGrouping = null;
		//added by shushma for promotion code
		if("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
		this.promotionCode=null;
	}

	// **********************************
	// *** ACCESSING - GET PROPERTIES ***
	// **********************************

	/**
	 * Return the pos transaction.
	 * @return The pos transaction.
	 */
	public POSTransaction getTransaction() {
		return (this.transaction);
	}

	/**
	 * Return the item.
	 * @return The item.
	 */
	public Item getItem() {
		return (this.item);
	}

	/**
	 * Return the quantity.
	 * @return The quantity.
	 */
	public Integer getQuantity() {
		return (this.quantity);
	}

	/**
	 * Return the line item details.
	 * @return The line item details.
	 */
	public Enumeration getLineItemDetails() {
		return (this.lineItemDetails.elements());
	}

	/**
	 * Return the manual markdown amount.
	 * @return The manual markdown amount.
	 */
	public ArmCurrency getManualMarkdownAmount() {
		return (this.manualMarkdownAmount);
	}

	/**
	 * Return the manual markdown reason.
	 * @return The manual markdown reason.
	 */
	public String getManualMarkdownReason() {
		return (this.manualMarkdownReason);
	}

	/**
	 * Return the discounts.
	 * @return The discounts.
	 */
	public Enumeration getDiscounts() {
		return (this.discounts.elements());
	}

	/**
	 * Return the manual unit price.
	 * @return The manual unit price.
	 */
	public ArmCurrency getManualUnitPrice() {
		return (this.manualUnitPrice);
	}

	/**
	 * Return the additional consultant.
	 * @return The additional consultant.
	 */
	public Employee getAdditionalConsultant() {
		return (this.additionalConsultant);
	}

	/**
	 * Return the sequence number.
	 * @return The the sequence number.
	 */
	public int getSequenceNumber() {
		return (this.sequenceNumber);
	}

	/**
	 * Return the tax exempt ID.
	 * @return The tax exempt ID.
	 */
	public String getTaxExemptId() {
		return (this.taxExemptId);
	}

	/**
	 * Return the regional tax exempt ID.
	 * @return The regional tax exempt ID.
	 */
	public String getRegionalTaxExemptId() {
		return (this.regionalTaxExemptId);
	}

	/**
	 * Return the misc item ID.
	 * @return The the misc item ID.
	 */
	public String getMiscItemId() {
		return (this.miscItemId);
	}

	/**
	 * Return the misc item GL account.
	 * @return The misc it GL account.
	 */
	public String getMiscItemGLAccount() {
		return (this.miscItemGLAccount);
	}

	/**
	 * Return the misc item comment.
	 * @return The misc item comment.
	 */
	public String getMiscItemComment() {
		return (this.miscItemComment);
	}

	/**
	 * Return the item description.
	 * First a check is made against the misc item description. If the misc item description in not null,
	 * then it is returned. If the misc item description is null, then a string is built that contains the
	 * associated item's description. If the item is a redeemable item, then a list of
	 * control ID's are included in the description.
	 * @return The item description.
	 * @see #doGetMiscItemDescription
	 * @see #getItem
	 * @see Item#getDescription
	 * @see Item#isRedeemable
	 * @see #getLineItemDetailsArray
	 * @see POSLineItemDetail#getGiftCertificateId
	 */
	public String getItemDescription() {
		if (this.doGetMiscItemDescription() != null)
			return (this.doGetMiscItemDescription());

		StringBuffer descriptionBuffer = new StringBuffer(this.getItem().getDescription());
		if (this.getItem().isRedeemable()) {
			POSLineItemDetail[] detailArray = this.getLineItemDetailsArray();
			int detailArrayLength = detailArray.length;
			if (detailArrayLength == 1) {
				descriptionBuffer.append(" ID: ");
				descriptionBuffer.append(detailArray[0].getGiftCertificateId());
			} else if (detailArrayLength > 1) {
				descriptionBuffer.append(" IDs: ");
				for (int index = 0; index < (detailArrayLength - 1); index++) {
					descriptionBuffer.append(detailArray[0].getGiftCertificateId());
					descriptionBuffer.append(", ");
				}
				descriptionBuffer.append(detailArray[detailArrayLength - 1].getGiftCertificateId());
			}
		}

		return (descriptionBuffer.toString());
	}

	/**
	 * Return the item retail price.
	 * If the internal item retail price is null, check to see if there is an item price override on the
	 * line item grouping. If there is, use that override for the item retail price.
	 * Next check if there exists a manual unit price. If there is, use that manual price for the item retail price.
	 * If there is no price override or a manual unit price, then use the associated item's retail
	 * price to set the internal item retail price.
	 * @return The ArmCurrency that represents the item's retail price for this line item.
	 * @see POSLineItemGrouping#isItemPriceOverride
	 * @see POSLineItemGrouping#getItemPriceOverride
	 * @see #isManualUnitPrice
	 * @see #getManualUnitPrice
	 * @see #getItem
	 * @see Item#getRetailPrice
	 * @see #doSetItemRetailPrice(ArmCurrency)
	 */
	public ArmCurrency getItemRetailPrice() {
		if (this.itemRetailPrice == null)
			if (this.getLineItemGrouping().isItemPriceOverride())
				this.doSetItemRetailPrice(this.getLineItemGrouping().getItemPriceOverride());
			else if (this.isManualUnitPrice())
				this.doSetItemRetailPrice(this.getManualUnitPrice());
			else
				this.doSetItemRetailPrice(this.getItem().getRetailPrice());
		return (this.itemRetailPrice);
	}

	/**
	 * Return the item selling price.
	 * If the internal item selling price is null, check to see if there is an item price override on the
	 * line item grouping. If there is, use that override for the item selling price.
	 * Next check if there exists a manual unit price. If there is, use that manual price for the item selling price.
	 * If there is no price override or a manual unit price, then use the associated item's selling
	 * price to set the internal item selling price.
	 * @return The ArmCurrency that represents the item's selling price for this line item.
	 * @see POSLineItemGrouping#isItemPriceOverride
	 * @see POSLineItemGrouping#getItemPriceOverride
	 * @see #isManualUnitPrice
	 * @see #getManualUnitPrice
	 * @see #getItem
	 * @see Item#getSellingPrice
	 * @see #doSetItemSellingPrice(ArmCurrency)
	 */
	public ArmCurrency getItemSellingPrice() {
		if (this.itemSellingPrice == null)
			if (this.getLineItemGrouping().isItemPriceOverride())
				this.doSetItemSellingPrice(this.getLineItemGrouping().getItemPriceOverride());
			else if (this.isManualUnitPrice())
				this.doSetItemSellingPrice(this.getManualUnitPrice());
			else
				this.doSetItemSellingPrice(this.getItem().getSellingPrice());
		return (this.itemSellingPrice);
	}

	/**
	 * Return the line item details in an array.
	 * @return An array of line item details.
	 */
	public POSLineItemDetail[] getLineItemDetailsArray() {
		return (POSLineItemDetail[]) this.lineItemDetails.toArray(new POSLineItemDetail[0]);
	}

	/**
	 * Return a discount. If the discount vector contains more than one discount, the last
	 * discount is returned. If there are no discounts, then <code>null</code> is returned.
	 * @return A discount.
	 */
	public Discount getDiscount() {
		if (this.discounts.size() > 0)
			return (Discount) this.discounts.lastElement();
		return (null);
	}

	/**
	 * Return the discounts in an array.
	 * @return An array of discounts.
	 */
	public Discount[] getDiscountsArray() {
		return (Discount[]) this.discounts.toArray(new Discount[0]);
	}

	/**
	 * Get the base currency type used by the pos transaction.
	 * @return A currency type used by the pos transaction.
	 * @see #getTransaction
	 * @see POSTransaction#getBaseCurrencyType
	 */
	public CurrencyType getBaseCurrencyType() {
		return (this.getTransaction().getBaseCurrencyType());
	}

	/**
	 * Get the line item grouping that knows about this line item.
	 * If a line item is not deleted, the transaction should find and return a line item grouping.
	 * @return The line item grouping for this line item.
	 * @see #getTransaction
	 * @see #getItem
	 * @see POSTransaction#getLineItemGroupingForItem
	 */
	public POSLineItemGrouping getLineItemGrouping() {
		if (this.lineItemGrouping == null)
			this.lineItemGrouping = this.getTransaction().getLineItemGroupingForItem(this.getItem());
		return (this.lineItemGrouping);
	}

	// *************************************
	// *** ACCESSING - DO GET PROPERTIES ***
	// *************************************

	/**
	 * Do get the misc item description. This method is not to be used by the GUI code.
	 * @return The misc item description
	 */
	public String doGetMiscItemDescription() {
		return (this.miscItemDescription);
	}

	/**
	 * Do get the misc item taxable flag. This method is not to be used by the GUI code.
	 * @return The misc item taxable flag.
	 */
	public Boolean doGetMiscItemTaxable() {
		return (this.miscItemTaxable);
	}

	// Start: Added by Himani for Promotions Best Deal CR
	/** Should the Line Item be considered for Promotion
	   * @return
	   */

	 public boolean isApplicableForPromotion() {
	    return this.isApplicableForPromotion;
	 }
	 
	 /** Remove Promotion from the Line Item
	   * @exception BusinessRuleException
	   */
	 public void removePromotion()
	      throws BusinessRuleException {
	    executeRule("removePromotion", this);
	    doRemovePromotion();
	    zeroAllLineItemDetailAmounts();
	    this.getTransaction().getCompositeTransaction().update();
	 }

	  /**
	   */
	 public void doRemovePromotion() {
	    this.isApplicableForPromotion = false;
	 }
	 
	// End: Added by Himani for Promotions Best Deal CR
	  
	/**
	 * Do get the misc item regional taxable flag. This method is not to be used by the GUI code.
	 * @return The misc item regional taxable flag.
	 */
	public Boolean doGetMiscItemRegionalTaxable() {
		return (this.miscItemRegionalTaxable);
	}

	// ***************************************
	// *** ACCESSING - GET PROPERTY VALUES ***
	// ***************************************

	/**
	 * Return <code>true</code> if the net amount is value,
	 * <code>false</code> otherwise. This flag can be set when there is a quantity greater than
	 * 1 and there have been various deal markdowns made to individual line item details.
	 * @return <code>true</code> if the net amount is valid,
	 * <code>false</code> otherwise.
	 */
	public boolean isNetAmountValid() {
		return (this.validNetAmountFlag);
	}

	// **********************************
	// *** ACCESSING - SET PROPERTIES ***
	// **********************************

	/**
	 * Sets the quantity, initializes the line item details, and broadcasts an update.
	 * The quantity *must* be great than or equal to 1, and less than 999. Otherwise a
	 * business rule expetion is thrown.
	 * @param aQuantity An Integer that represents the quantity. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetQuantityAndInitializeDetails(Integer)
	 * @see #broadcastUpdate
	 */
	public void setQuantity(Integer aQuantity) throws BusinessRuleException {
		java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		this.checkForNullParameter("setQuantity", aQuantity);
		if (!this.quantity.equals(aQuantity)) {
			if ((aQuantity.intValue() < 1) || (aQuantity.intValue() > 999))
				throw new BusinessRuleException(res.getString("Quantity must be greater than 0 and less than 1000"));
			this.executeRule("setQuantity", aQuantity);
			this.doSetQuantityAndInitializeDetails(aQuantity);
			this.broadcastUpdate();
		}
	}

	/**
	 * Sets the manual markdown amount and broadcasts an update.
	 * @param aCurrency A ArmCurrency that represents the manual markdown amount. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetManualMarkdownAmount(ArmCurrency)
	 * @see #broadcastUpdate
	 */
	public void setManualMarkdownAmount(ArmCurrency aCurrency) throws BusinessRuleException {
		this.checkForNullParameter("setManualMarkdownAmount", aCurrency);
		this.executeRule("setManualMarkdownAmount", aCurrency);
		this.doSetManualMarkdownAmount(aCurrency);
		this.broadcastUpdate();
	}

	/**
	 * Sets the manual markdown reason.
	 * @param aString A String that represents the manual markdown reason. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetManualMarkdownReason(String)
	 */
	public void setManualMarkdownReason(String aString) throws BusinessRuleException {
		this.checkForNullParameter("setManualMarkdownReason", aString);
		String aManualMarkdownReason = aString.trim();
		if (!this.manualMarkdownReason.equals(aManualMarkdownReason)) {
			this.executeRule("setManualMarkdownReason", aManualMarkdownReason);
			this.doSetManualMarkdownReason(aManualMarkdownReason);
		}
	}

	/**
	 * Sets the manual markdown percent and broadcasts an update.
	 * @param aPercent A Double that represents the manual markdown percent. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetManualMarkdownPercent(Double)
	 * @see #broadcastUpdate
	 */
	public void setManualMarkdownPercent(Double aPercent) throws BusinessRuleException {
		this.checkForNullParameter("setManualMarkdownPercent", aPercent);
		this.executeRule("setManualMarkdownPercent", aPercent);
		this.doSetManualMarkdownPercent(aPercent);
		this.broadcastUpdate();
	}

	/**
	 * Sets the item price override and broadcasts an update. It also clears the internal item
	 * retail price and internal item selling price.
	 * @param aCurrency A ArmCurrency that represents the item price override. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #getLineItemGrouping
	 * @see POSLineItemGrouping#doSetItemPriceOverride(ArmCurrency)
	 * @see #doSetItemRetailPrice(ArmCurrency)
	 * @see #doSetItemSellingPrice(ArmCurrency)
	 * @see #broadcastUpdate
	 */
	public void setItemPriceOverride(ArmCurrency aCurrency) throws BusinessRuleException {
		this.checkForNullParameter("setItemPriceOverride", aCurrency);
		this.executeRule("setItemPriceOverride", aCurrency);
		this.getLineItemGrouping().doSetItemPriceOverride(aCurrency);
		this.doSetItemRetailPrice(null);
		this.doSetItemSellingPrice(null);
		this.broadcastUpdate();
	}

	/**
	 * Clears (sets to <code>null</code>) the item price override and broadcasts an update.
	 * It also clears the internal item retail price and internal item selling price.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #getLineItemGrouping
	 * @see POSLineItemGrouping#doClearItemPriceOverride
	 * @see #doSetItemRetailPrice(ArmCurrency)
	 * @see #doSetItemSellingPrice(ArmCurrency)
	 * @see #broadcastUpdate
	 */
	public void clearItemPriceOverride() throws BusinessRuleException {
		this.executeRule("clearItemPriceOverride");
		this.getLineItemGrouping().doClearItemPriceOverride();
		this.doSetItemRetailPrice(null);
		this.doSetItemSellingPrice(null);
		this.broadcastUpdate();
	}

	/**
	 * Sets the manual unit price and broadcasts an update. It also clears the internal item
	 * retail price and internal item selling price.
	 * @param aCurrency A ArmCurrency that represents the manual unit price. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetManualUnitPrice(ArmCurrency)
	 * @see #doSetItemRetailPrice(ArmCurrency)
	 * @see #doSetItemSellingPrice(ArmCurrency)
	 * @see #broadcastUpdate
	 */
	public void setManualUnitPrice(ArmCurrency aCurrency) throws BusinessRuleException {
		this.checkForNullParameter("setManualUnitPrice", aCurrency);
		this.executeRule("setManualUnitPrice", aCurrency);
		this.doSetManualUnitPrice(aCurrency);
		this.doSetItemRetailPrice(null);
		this.doSetItemSellingPrice(null);
		this.broadcastUpdate();
	}

	/**
	 * Clears (sets to <code>null</code>) the manual unit price and broadcasts an update.
	 * It also clears the internal item retail price and internal item selling price.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetManualUnitPrice(ArmCurrency)
	 * @see #doSetItemRetailPrice(ArmCurrency)
	 * @see #doSetItemSellingPrice(ArmCurrency)
	 * @see #broadcastUpdate
	 */
	public void clearManualUnitPrice() throws BusinessRuleException {
		this.executeRule("clearManualUnitPrice");
		this.doSetManualUnitPrice(null);
		this.doSetItemRetailPrice(null);
		this.doSetItemSellingPrice(null);
		this.broadcastUpdate();
	}

	/**
	 * Sets the tax exempt ID and broadcasts an update.
	 * @param aString A String that represents the tax exempt ID. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetTaxExemptId(String)
	 * @see #broadcastUpdate
	 */
	public void setTaxExemptId(String aString) throws BusinessRuleException {
		this.checkForNullParameter("setTaxExemptId", aString);
		String aTaxExemptId = aString.trim();
		if (!this.taxExemptId.equals(aTaxExemptId)) {
			this.executeRule("setTaxExemptId", aTaxExemptId);
			this.doSetTaxExemptId(aTaxExemptId);
			this.broadcastUpdate();
		}
	}

	/**
	 * Sets the regional tax exempt ID and broadcasts an update.
	 * @param aString A String that represents the regional tax exempt ID. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetRegionalTaxExemptId(String)
	 * @see #broadcastUpdate
	 */
	public void setRegionalTaxExemptId(String aString) throws BusinessRuleException {
		this.checkForNullParameter("setRegionalTaxExemptId", aString);
		String aRegionalTaxExemptId = aString.trim();
		if (!this.regionalTaxExemptId.equals(aRegionalTaxExemptId)) {
			this.executeRule("setRegionalTaxExemptId", aRegionalTaxExemptId);
			this.doSetRegionalTaxExemptId(aRegionalTaxExemptId);
			this.broadcastUpdate();
		}
	}

	/**
	 * Sets the misc item description.
	 * @param aString A String that represents the misc item description. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemDescription(String)
	 * @see #clearMiscItemDescription
	 */
	public void setMiscItemDescription(String aString) throws BusinessRuleException {
		this.checkForNullParameter("setMiscItemDescription", aString);
		String aMiscItemDescription = aString.trim();
		this.executeRule("setMiscItemDescription", aMiscItemDescription);
		this.doSetMiscItemDescription(aMiscItemDescription);
	}

	/**
	 * Clears (sets to <code>null</code>) the misc item description.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemDescription(String)
	 */
	public void clearMiscItemDescription() throws BusinessRuleException {
		this.executeRule("clearMiscItemDescription");
		this.doSetMiscItemDescription(null);
	}

	/**
	 * Sets the misc item taxable flag.
	 * @param aBoolean A Boolean that represents the setting of the misc item taxable flag. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemTaxable(Boolean)
	 * @see #clearMiscItemTaxable
	 */
	public void setMiscItemTaxable(Boolean aBoolean) throws BusinessRuleException {
		this.checkForNullParameter("setMiscItemTaxable", aBoolean);
		this.executeRule("setMiscItemTaxable", aBoolean);
		this.doSetMiscItemTaxable(aBoolean);
	}

	/**
	 * Clears (sets to <code>null</code>) the misc item taxable flag.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemTaxable(Boolean)
	 */
	public void clearMiscItemTaxable() throws BusinessRuleException {
		this.executeRule("clearMiscItemTaxable");
		this.doSetMiscItemTaxable(null);
	}

	/**
	 * Sets the misc item regional taxable flag.
	 * @param aBoolean A Boolean that represents the setting of the misc item regional taxable flag. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemRegionalTaxable(Boolean)
	 * @see #clearMiscItemRegionalTaxable
	 */
	public void setMiscItemRegionalTaxable(Boolean aBoolean) throws BusinessRuleException {
		this.checkForNullParameter("setMiscItemRegionalTaxable", aBoolean);
		this.executeRule("setMiscItemRegionalTaxable", aBoolean);
		this.doSetMiscItemRegionalTaxable(aBoolean);
	}

	/**
	 * Clears (sets to <code>null</code>) the misc item regional taxable flag.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemRegionalTaxable(Boolean)
	 */
	public void clearMiscItemRegionalTaxable() throws BusinessRuleException {
		this.executeRule("clearMiscItemRegionalTaxable");
		this.doSetMiscItemRegionalTaxable(null);
	}

	/**
	 * Sets the misc item GL account.
	 * @param aString A String that represents the misc item GL account. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemGLAccount(String)
	 * @see #clearMiscItemGLAccount
	 */
	public void setMiscItemGLAccount(String aString) throws BusinessRuleException {
		this.checkForNullParameter("setMiscItemGLAccount", aString);
		String aMiscItemGLAccount = aString.trim();
		this.executeRule("setMiscItemGLAccount", aMiscItemGLAccount);
		this.doSetMiscItemGLAccount(aMiscItemGLAccount);
	}

	/**
	 * Clears (sets to <code>null</code>) the misc item GL account.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemGLAccount(String)
	 */
	public void clearMiscItemGLAccount() throws BusinessRuleException {
		this.executeRule("clearMiscItemGLAccount");
		this.doSetMiscItemGLAccount(null);
	}

	/**
	 * Sets the misc item comment.
	 * @param aString A String that represents the misc item comment. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemComment(String)
	 * @see #clearMiscItemComment
	 */
	public void setMiscItemComment(String aString) throws BusinessRuleException {
		this.checkForNullParameter("setMiscItemComment", aString);
		String aMiscItemComment = aString.trim();
		this.executeRule("setMiscItemComment", aMiscItemComment);
		this.doSetMiscItemComment(aMiscItemComment);
	}

	/**
	 * Clears (sets to <code>null</code>) the misc item comment.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetMiscItemComment(String)
	 */
	public void clearMiscItemComment() throws BusinessRuleException {
		this.executeRule("clearMiscItemComment");
		this.doSetMiscItemComment(null);
	}

	// *************************************
	// *** ACCESSING - ADD COLLABORATORS ***
	// *************************************

	/**
	 * Sets the additional consultant.
	 * @param anAdditionalConsultant An Employee that represents the additional consutant. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doSetAdditionalConsultant(Employee)
	 */
	public void setAdditionalConsultant(Employee anAdditionalConsultant) throws BusinessRuleException {
		this.checkForNullParameter("setAdditionalConsultant", anAdditionalConsultant);
		this.executeRule("setAdditionalConsultant", anAdditionalConsultant);
		this.doSetAdditionalConsultant(anAdditionalConsultant);
	}

	/**
	 * Adds a discount and broadcasts an update.
	 * @param aDiscount A Discount to add. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doAddDiscount(Discount)
	 * @see #broadcastUpdate
	 */
	public void addDiscount(Discount aDiscount) throws BusinessRuleException {
		this.checkForNullParameter("addDiscount", aDiscount);
		this.executeRule("addDiscount", aDiscount);
		this.doAddDiscount(aDiscount);
		this.broadcastUpdate();
	}

	// ****************************************
	// *** ACCESSING - REMOVE COLLABORATORS ***
	// ****************************************

	/**
	 * Removes the additional consultant.
	 * @param anAdditionalConsultant An Employee that represents the additional consutant. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doRemoveAdditionalConsultant(Employee)
	 */
	public void removeAdditionalConsultant(Employee anAdditionalConsultant) throws BusinessRuleException {
		this.checkForNullParameter("removeAdditionalConsultant", anAdditionalConsultant);
		this.executeRule("removeAdditionalConsultant", anAdditionalConsultant);
		this.doRemoveAdditionalConsultant(anAdditionalConsultant);
	}

	/**
	 * Removes a discount and broadcasts an update.
	 * @param aDiscount A Discount to add. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doRemoveDiscount(Discount)
	 * @see #broadcastUpdate
	 */
	public void removeDiscount(Discount aDiscount) throws BusinessRuleException {
		this.checkForNullParameter("removeDiscount", aDiscount);
		this.executeRule("removeDiscount", aDiscount);
		this.doRemoveDiscount(aDiscount);
		this.broadcastUpdate();
	}

	/**
	 * Removes all discounts and broadcasts an update.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #doRemoveAllDiscounts
	 * @see #broadcastUpdate
	 */
	public void removeAllDiscounts() throws BusinessRuleException {
		this.executeRule("removeAllDiscounts");
		this.doRemoveAllDiscounts();
		this.broadcastUpdate();
	}

	// *************************************
	// *** ACCESSING - DO SET PROPERTIES ***
	// *************************************

	/**
	 * Do set the quantity and initialize the the line item details.
	 * @param aQuantity The quantity.
	 * If passed null the method does nothing.
	 * @see #doSetQuantity(Integer)
	 * @see #initializeLineItemDetails(int)
	 */
	public void doSetQuantityAndInitializeDetails(Integer aQuantity) {
		if (aQuantity == null)
			return;
		this.doSetQuantity(aQuantity);
		this.initializeLineItemDetails(aQuantity.intValue());
	}

	/**
	 * Do set the quantity.
	 * @param aQuantity The quantity.
	 * If passed null the method does nothing.
	 */
	public void doSetQuantity(Integer aQuantity) {
		if (aQuantity == null)
			return;
		this.quantity = aQuantity;
	}

	/**
	 * Do set the manual markdown amount.
	 * @param aMarkdownAmount The manual markdown amount.
	 * If passed null the method does nothing.
	 */
	public void doSetManualMarkdownAmount(ArmCurrency aMarkdownAmount) {
		if (aMarkdownAmount == null)
			return;
		this.manualMarkdownAmount = aMarkdownAmount;
	}

	/**
	 * Do set the manual markdown reason.
	 * @param aManualMarkdownReason The manual markdown reason.
	 * If passed null the method does nothing.
	 */
	public void doSetManualMarkdownReason(String aManualMarkdownReason) {
		if (aManualMarkdownReason == null)
			return;
		this.manualMarkdownReason = aManualMarkdownReason;
	}

	/**
	 * Do set the manual markdown percent.
	 * The manual markdown amount is set to the internal item retail price multiplied by the
	 * passed markdown percent.
	 * @param aMarkdownPercent The manual markdown percent.
	 */
	public void doSetManualMarkdownPercent(Double aMarkdownPercent) {
	ArmCurrency anAmount = this.getItemRetailPrice();
		this.manualMarkdownAmount = anAmount.multiply(aMarkdownPercent.doubleValue()).round();
	}

	/**
	 * Do set the valid net amount flag.
	 * @param aFlag The valid net amount flag.
	 */
	public void doSetValidNetAmountFlag(boolean aFlag) {
		this.validNetAmountFlag = aFlag;
	}

	/**
	 * Do set the manual unit price.
	 * @param aUnitPrice The manual unit price.
	 */
	public void doSetManualUnitPrice(ArmCurrency aUnitPrice) {
		this.manualUnitPrice = aUnitPrice;
	}

	/**
	 * Do set the tax exempt ID.
	 * @param aTaxExemptId The tax exempt ID.
	 * If passed null the method does nothing.
	 */
	public void doSetTaxExemptId(String aTaxExemptId) {
		if (aTaxExemptId == null)
			return;
		this.taxExemptId = aTaxExemptId;
	}

	/**
	 * Do set the regional tax exempt ID.
	 * @param aRegionalTaxExemptId The regional tax exempt ID.
	 * If passed null the method does nothing.
	 */
	public void doSetRegionalTaxExemptId(String aRegionalTaxExemptId) {
		if (aRegionalTaxExemptId == null)
			return;
		this.regionalTaxExemptId = aRegionalTaxExemptId;
	}

	/**
	 * Do set the deleted flag.
	 */
	public void doSetDeletedFlag() {
		this.deletedFlag = true;
	}

	/**
	 * Do set the item retail price.
	 * @param anItemRetailPrice The item retail price.
	 */
	public void doSetItemRetailPrice(ArmCurrency anItemRetailPrice) {
		this.itemRetailPrice = anItemRetailPrice;
	}

	/**
	 * Do set the item selling price.
	 * @param anItemSellingPrice The item selling price.
	 */
	public void doSetItemSellingPrice(ArmCurrency anItemSellingPrice) {
		this.itemSellingPrice = anItemSellingPrice;
	}

	/**
	 * Do set the misc item ID.
	 * @param aString The misc item ID.
	 */
	public void doSetMiscItemId(String aString) {
		this.miscItemId = aString;
	}

	/**
	 * Do set the misc item description.
	 * @param aString The misc item description.
	 */
	public void doSetMiscItemDescription(String aString) {
		this.miscItemDescription = aString;
	}

	/**
	 * Do set the misc item taxable.
	 * @param aBoolean The misc item taxable flag.
	 */
	public void doSetMiscItemTaxable(Boolean aBoolean) {
		this.miscItemTaxable = aBoolean;
	}

	/**
	 * Do set the misc item regional taxable.
	 * @param aBoolean The misc item regional taxable flag.
	 */
	public void doSetMiscItemRegionalTaxable(Boolean aBoolean) {
		this.miscItemRegionalTaxable = aBoolean;
	}

	/**
	 * Do set the misc item GL account.
	 * @param aString The misc item GL account.
	 */
	public void doSetMiscItemGLAccount(String aString) {
		this.miscItemGLAccount = aString;
	}

	/**
	 * Do set the misc item comment.
	 * @param aString The misc item comment.
	 */
	public void doSetMiscItemComment(String aString) {
		this.miscItemComment = aString;
	}

	// ****************************************
	// *** ACCESSING - DO ADD COLLABORATORS ***
	// ****************************************

	/**
	 * Do set the item. If the item is null, then the method does nothing. If the passed item's ID
	 * equals the current assigned item's ID, then the method does nothing.
	 * @param anItem The item to set.
	 */
	public void doSetItem(Item anItem) {
		if (anItem == null)
			return;
		if (!anItem.getId().equals(this.getItem().getId()))
			return;
		this.item = anItem;
	}

	/**
	 * Do set the additional consultant.
	 * @param anAdditionalConsultant The employee that represents the additional consultant.
	 */
	public void doSetAdditionalConsultant(Employee anAdditionalConsultant) {
		this.additionalConsultant = anAdditionalConsultant;
	}

	/**
	 * Do add a line item detail.
	 * @param aLineItemDetail The line item detail to add.
	 */
	public void doAddLineItemDetail(POSLineItemDetail aLineItemDetail) {
		this.lineItemDetails.addElement(aLineItemDetail);
	}

	/**
	 * Do add a discount.
	 * @param aDiscount The discount to add.
	 */
	public void doAddDiscount(Discount aDiscount) {
		this.discounts.addElement(aDiscount);
	}

	// *******************************************
	// *** ACCESSING - DO REMOVE COLLABORATORS ***
	// *******************************************

	/**
	 * Do remove the additional consultant.
	 * @param anAdditionalConsultant The employee that represents the additional consultant.
	 */
	public void doRemoveAdditionalConsultant(Employee anAdditionalConsultant) {
		this.additionalConsultant = null;
	}

	/**
	 * Do remove a discount.
	 * @param aDiscount The discount to remove.
	 */
	public void doRemoveDiscount(Discount aDiscount) {
		this.discounts.removeElement(aDiscount);
	}

	/**
	 * Do remove all discounts. Tells the discount vector to remove all elements.
	 */
	public void doRemoveAllDiscounts() {
		this.discounts.removeAllElements();
	}

	// ************************
	// *** CONDUCT BUSINESS ***
	// ************************

	/**
	 * Gets the net amount for the line item by asking the first line item detail for its net amount.
	 * This net amount may not be the same for each line item detail because there may be some sort
	 * of deal pricing in effect.
	 * @return A currency that represents the net amount.
	 * @see POSLineItemDetail#getNetAmount
	 */
	public ArmCurrency getNetAmount() {
		return ((POSLineItemDetail) this.lineItemDetails.firstElement()).getNetAmount();
	}

	/**
	 * Gets the tax amount for the line item by asking the first line item detail for its tax amount.
	 * This tax amount may not be the same for each line item detail because there may be some sort
	 * of deal pricing in effect.
	 * @return A currency that represents the tax amount.
	 * @see POSLineItemDetail#getTaxAmount
	 */
	public ArmCurrency getTaxAmount() {
		return ((POSLineItemDetail) this.lineItemDetails.firstElement()).getTaxAmount();
	}

	/**
	 * Gets the regional tax amount for the line item by asking the first line item detail for its regional tax amount.
	 * This regional tax amount may not be the same for each line item detail because there may be some sort
	 * of deal pricing in effect.
	 * @return A currency that represents the regional tax amount.
	 * @see POSLineItemDetail#getRegionalTaxAmount
	 */
	public ArmCurrency getRegionalTaxAmount() {
		return ((POSLineItemDetail) this.lineItemDetails.firstElement()).getRegionalTaxAmount();
	}

	/**
	 * Gets the extended retail amount for the line item by multiplying the internal item retail price
	 * by the quantity.
	 * @return A currency that represents the extended retail amount.
	 * @see #getItemRetailPrice
	 * @see Currency#multiply(int)
	 * @see #getQuantity
	 * @see Integer#intValue
	 */
	public ArmCurrency getExtendedRetailAmount() {
		return (this.getItemRetailPrice().multiply(this.getQuantity().intValue()));
	}

	/**
	 * Gets the extended net amount for the line item by adding the net amount
	 * from each of its line item details. Addition of the individual line item detail's
	 * net amount is required because of deal pricing where each net amount could be different.
	 * @return A currency that represents the extended net amount.
	 * @see Currency#Currency(CurrencyType, double)
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#getNetAmount
	 * @see Currency#add(ArmCurrency)
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 */
	public ArmCurrency getExtendedNetAmount() {
		try {
		ArmCurrency total = new ArmCurrency(this.getBaseCurrencyType(), 0.0);
			Enumeration aLineItemDetailList = this.getLineItemDetails();
			while(aLineItemDetailList.hasMoreElements()) {
			ArmCurrency aNetAmount = ((POSLineItemDetail) aLineItemDetailList.nextElement()).getNetAmount();
				total = total.add(aNetAmount);
			}
			return (total);
		} catch (CurrencyException anException) {
			this.logCurrencyException("getExtendedNetAmount", anException);
			return (null);
		}
		
	}

	/**
	 * Gets the extended manual markdown amount for the line item by multiplying the manual markdown amount
	 * by the quantity.
	 * @return A currency that represents the extended manual markdown amount.
	 * @see #getManualMarkdownAmount
	 * @see Currency#multiply(int)
	 * @see #getQuantity
	 * @see Integer#intValue
	 */
	public ArmCurrency getExtendedManualMarkdownAmount() {
		return (this.getManualMarkdownAmount().multiply(this.getQuantity().intValue()));
	}

	/**
	 * Gets the extended reduction amount for the line item by adding the reduction amount
	 * from each of its line item details. Addition of the individual line item detail's
	 * reduction amount is required because of deal pricing where each reduction amount could be different.
	 * @return A currency that represents the extended reduction amount.
	 * @see Currency#Currency(CurrencyType, double)
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#getReductionAmount
	 * @see Currency#add(ArmCurrency)
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 */
	public ArmCurrency getExtendedReductionAmount() {
		try {	
			ArmCurrency total = new ArmCurrency(this.getBaseCurrencyType(), 0.0);
			Enumeration aLineItemDetailList = this.getLineItemDetails();
			while(aLineItemDetailList.hasMoreElements()) {
			ArmCurrency aReductionAmount = ((POSLineItemDetail) aLineItemDetailList.nextElement()).getReductionAmount();
				total = total.add(aReductionAmount);
			}			
			return (total);
		} catch (CurrencyException anException) {
			this.logCurrencyException("getExtendedReductionAmount", anException);
			return (null);
		}
	
	}

	/**
	 * Gets the extended tax amount for the line item by adding the tax amount
	 * from each of its line item details. Addition of the individual line item detail's
	 * tax amount is required because of deal pricing where each tax amount could be different.
	 * @return A currency that represents the extended tax amount.
	 * @see Currency#Currency(CurrencyType, double)
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#getTaxAmount
	 * @see Currency#add(ArmCurrency)
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 */
	public ArmCurrency getExtendedTaxAmount() {
		try {
		ArmCurrency total = new ArmCurrency(this.getBaseCurrencyType(), 0.0);
			Enumeration aLineItemDetailList = this.getLineItemDetails();
			while(aLineItemDetailList.hasMoreElements()) {
			ArmCurrency aTaxAmount = ((POSLineItemDetail) aLineItemDetailList.nextElement()).getTaxAmount();
				total = total.add(aTaxAmount);
			}
			return (total);		
		} catch (CurrencyException anException) {
			this.logCurrencyException("getExtendedTaxAmount", anException);
			return (null);
		}		
	}

	/**
	 * Gets the extended regional tax amount for the line item by adding the regional tax amount
	 * from each of its line item details. Addition of the individual line item detail's
	 * regional tax amount is required because of deal pricing where each regional tax amount could be different.
	 * @return A currency that represents the extended regional tax amount.
	 * @see Currency#Currency(CurrencyType, double)
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#getRegionalTaxAmount
	 * @see Currency#add(ArmCurrency)
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 */
	public ArmCurrency getExtendedRegionalTaxAmount() {
		try {
		ArmCurrency total = new ArmCurrency(this.getBaseCurrencyType(), 0.0);
			Enumeration aLineItemDetailList = this.getLineItemDetails();
			while(aLineItemDetailList.hasMoreElements()) {
			ArmCurrency aRegionalTaxAmount = ((POSLineItemDetail) aLineItemDetailList.nextElement()).getRegionalTaxAmount();
				total = total.add(aRegionalTaxAmount);
			}
			return (total);
		} catch (CurrencyException anException) {
			this.logCurrencyException("getExtendedRegionalTaxAmount", anException);
			return (null);
		}
	}

	/**
	 * Gets the total amount due for the line item by adding the total amount due
	 * from each of its line item details. Addition of the individual line item detail's
	 * total amount due is required because of deal pricing where each total amount due could be different.
	 * @return A currency that represents the total amount due.
	 * @see Currency#Currency(CurrencyType, double)
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#getTotalAmountDue
	 * @see Currency#add(ArmCurrency)
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 */
	public ArmCurrency getTotalAmountDue() {
		try {
		ArmCurrency total = new ArmCurrency(this.getBaseCurrencyType(), 0.0);
				Enumeration aLineItemDetailList = this.getLineItemDetails();
			while(aLineItemDetailList.hasMoreElements()) {
			ArmCurrency aTotalAmountDue = ((POSLineItemDetail) aLineItemDetailList.nextElement()).getTotalAmountDue();
			//Anjana modified the below to fix presale total maount due zero issue
				total = total.add(getNetAmount().add(getTaxAmount()).add(getRegionalTaxAmount()));
			}			
			return (total);			
		} catch (CurrencyException anException) {
			this.logCurrencyException("getTotalAmountDue", anException);
			return (null);
		}	
	}

	/**
	 * Gets the extended vat amount for the line item by adding the vat amount
	 * from each of its line item details. Addition of the individual line item detail's
	 * vat amount is required because of deal pricing where each vat amount could be different.
	 * @return A currency that represents the extended vat amount.
	 * @see Currency#Currency(CurrencyType, double)
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#getVatAmount
	 * @see Currency#add(ArmCurrency)
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 */
	public ArmCurrency getExtendedVatAmount() {
		try {
		ArmCurrency total = new ArmCurrency(this.getBaseCurrencyType(), 0.0);
			Enumeration aLineItemDetailList = this.getLineItemDetails();
			while(aLineItemDetailList.hasMoreElements()) {
			ArmCurrency aVatAmount = ((POSLineItemDetail) aLineItemDetailList.nextElement()).getVatAmount();
				total = total.add(aVatAmount);
			}
			return (total);
		} catch (CurrencyException anException) {
			this.logCurrencyException("getExtendedVatAmount", anException);
			return (null);
		}
	}

	/**
	 * Delete the line item.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String)
	 * @see #doDelete
	 */
	public void delete() throws BusinessRuleException {
		this.executeRule("delete");
		this.doDelete();
	}

	/**
	 * Do delete the line item by telling the transaction to remove the reference to it,
	 * setting lineItemGrouping to nil, cleanup the line item details, and setting the delete flag.
	 * @see #getTransaction
	 * @see POSTransaction#doRemoveLineItem(POSLineItem)
	 * @see #cleanupLineItemDetails
	 * @see #doSetDeletedFlag
	 */
	public void doDelete() {
		this.getTransaction().doRemoveLineItem(this);
		this.lineItemGrouping = null;
		this.cleanupLineItemDetails();
		this.doSetDeletedFlag();
	}

	/**
	 * Delete the line item without an audit trail.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String)
	 * @see #doDeleteWithoutAuditTrail
	 */
	public void deleteWithoutAuditTrail() throws BusinessRuleException {
		this.executeRule("deleteWithoutAuditTrail");
		this.doDeleteWithoutAuditTrail();
	}

	/**
	 * Do delete without an audit trail. Use of this method occurs when transferring sale line items
	 * to layaway line items and vise versa.
	 * @see #getTransaction
	 * @see POSTransaction#doRemoveLineItemWithoutAuditTrail(POSLineItem)
	 * @see #cleanupLineItemDetails
	 * @see #doSetDeletedFlag
	 */
	public void doDeleteWithoutAuditTrail() {
		this.getTransaction().doRemoveLineItemWithoutAuditTrail(this);
		this.lineItemGrouping = null;
		this.cleanupLineItemDetails();
		this.doSetDeletedFlag();
	}

	/**
	 * Sets the manual unit price if the item requires a manual unit price,
	 * otherwise it sets the item price override.
	 * @param aCurrency A ArmCurrency that represents the manual unit price. The parameter
	 * cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #isItemRequiringManualUnitPrice
	 * @see #setManualUnitPrice(ArmCurrency)
	 * @see #setItemPriceOverride(ArmCurrency)
	 */
	public void adjustUnitPrice(ArmCurrency aCurrency) throws BusinessRuleException {
		this.checkForNullParameter("adjustUnitPrice", aCurrency);
		this.executeRule("adjustUnitPrice", aCurrency);
		if (this.isItemRequiringManualUnitPrice())
			this.setManualUnitPrice(aCurrency);
		else
			this.setItemPriceOverride(aCurrency);
	}

	/**
	 * Clears the manual unit price if the item requires a manual unit price,
	 * otherwise it clears the item price override.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #isItemRequiringManualUnitPrice
	 * @see #clearManualUnitPrice()
	 * @see #clearItemPriceOverride()
	 */
	public void clearAdjustedUnitPrice() throws BusinessRuleException {
		this.executeRule("clearAdjustedUnitPrice");
		if (this.isItemRequiringManualUnitPrice())
			this.clearManualUnitPrice();
		else
			this.clearItemPriceOverride();
	}

	/**
	 * Adjusts the manual markdown amount.
	 * @param aCurrency A ArmCurrency that is subtracted from the internal item retail price.
	 * The parameter cannot be null.
	 * @throws NullPointerException If passed null.
	 * @throws BusinessRuleException If a business rule fails.
	 * @see BusinessObject#checkForNullParameter(String, Object)
	 * @see BusinessObject#executeRule(String, Object)
	 * @see #setManualMarkdownAmount(ArmCurrency)
	 * @see #getItemRetailPrice
	 * @see Currency#subtract(ArmCurrency)
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 * @see #broadcastUpdate
	 */
	public void adjustMarkdownAmount(ArmCurrency aCurrency) throws BusinessRuleException {
		this.checkForNullParameter("adjustMarkdownAmount", aCurrency);
		this.executeRule("adjustMarkdownAmount", aCurrency);
		try {
			this.setManualMarkdownAmount(this.getItemRetailPrice().subtract(aCurrency));
		} catch (CurrencyException anException) {
			this.logCurrencyException("adjustMarkdownAmount", anException);
		}
		this.broadcastUpdate();
	}

	/**
	 * Do set the unit price by setting the net amounts of all the line item details.
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#doSetNetAmount(ArmCurrency)
	 */
	public void doSetUnitPrice(ArmCurrency aUnitPrice) {
		Enumeration aLineItemDetailList = this.getLineItemDetails();
		while(aLineItemDetailList.hasMoreElements()) {
			((POSLineItemDetail) aLineItemDetailList.nextElement()).doSetNetAmount(aUnitPrice);
		}
	}

	/**
	 * Do set the unit tax by setting the tax amounts of all the line item details.
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#doSetTaxAmount(ArmCurrency)
	 */
	public void doSetUnitTax(ArmCurrency aUnitTax) {
		Enumeration aLineItemDetailList = this.getLineItemDetails();
		while(aLineItemDetailList.hasMoreElements()) {
			((POSLineItemDetail) aLineItemDetailList.nextElement()).doSetTaxAmount(aUnitTax);
		}
	}

	/**
	 * Do set the unit regional tax by setting the regional tax amounts of all the line item details.
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#doSetRegionalTaxAmount(ArmCurrency)
	 */
	public void doSetUnitRegionalTax(ArmCurrency aUnitRegionalTax) {
		Enumeration aLineItemDetailList = this.getLineItemDetails();
		while(aLineItemDetailList.hasMoreElements()) {
			((POSLineItemDetail) aLineItemDetailList.nextElement()).doSetRegionalTaxAmount(aUnitRegionalTax);
		}
	}

	// ***********************
	// *** OBJECT CREATION ***
	// ***********************

	/**
	 * Creates a new line item detail.
	 * @see #createLineItemDetail
	 */
	public void newLineItemDetail() {
		this.createLineItemDetail();
	}

	/**
	 * The implementation of this method should create a new line item detail. For an example see
	 * the class com.chelseasystems.cs.pos.CMSSaleLineItem.
	 * @return A specific line item detail.
	 */
	public abstract POSLineItemDetail createLineItemDetail();

	// ***************
	// *** TESTING ***
	// ***************

	/**
	 * Return <code>true</code> if the line item is manually marked down,
	 * <code>false</code> otherwise.
	 * @return <code>true</code> if the line item is manually marked down,
	 * <code>false</code> otherwise.
	 * @see BusinessObject#logCurrencyException(String, Exception)
	 */
	public boolean isManualMarkdown() {
		try {
			return (this.manualMarkdownAmount.absoluteValue().greaterThan(new ArmCurrency(this.getBaseCurrencyType(), 0.0)));
		} catch (CurrencyException anException) {
			this.logCurrencyException("isManualMarkdown", anException);
			return (false);
		}
	}

	/**
	 * Return <code>true</code> if the line item has a manual markdown reason,
	 * <code>false</code> otherwise. A check is made against the length of the
	 * internal manual markdown reason string.
	 * @return <code>true</code> if the line item has a manual markdown reason,
	 * <code>false</code> otherwise.
	 * @see #getManualMarkdownReason
	 */
	public boolean hasManualMarkdownReason() {
		return (this.getManualMarkdownReason().length() != 0);
	}

	/**
	 * Return <code>true</code> if the line item has a manual unit price,
	 * <code>false</code> otherwise. A check is made to see if the internal
	 * manual unit price is null.
	 * @return <code>true</code> if the line item has a manual unit price,
	 * <code>false</code> otherwise.
	 * @see #getManualUnitPrice
	 */
	public boolean isManualUnitPrice() {
		return (this.getManualUnitPrice() != null);
	}

	/**
	 * Return <code>true</code> if the line item is tax exempt,
	 * <code>false</code> otherwise. A check is made against the length of the
	 * internal tax exempt ID string.
	 * @return <code>true</code> if the line item is tax exempt,
	 * <code>false</code> otherwise.
	 * @see #getTaxExemptId
	 */
	public boolean isTaxExempt() {
		return (this.getTaxExemptId().length() != 0);
	}

	/**
	 * Return <code>true</code> if the line item is regional tax exempt,
	 * <code>false</code> otherwise. A check is made against the length of the
	 * internal regional tax exempt ID string.
	 * @return <code>true</code> if the line item is regional tax exempt,
	 * <code>false</code> otherwise.
	 * @see #getRegionalTaxExemptId
	 */
	public boolean isRegionalTaxExempt() {
		return (this.getRegionalTaxExemptId().length() != 0);
	}

	/**
	 * Return <code>true</code> if the line item has a manual tax,
	 * <code>false</code> otherwise. A check is made against each line item
	 * detail to see if it has a manual tax amount.
	 * @return <code>true</code> if the line item has a manual tax,
	 * <code>false</code> otherwise.
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#isManualTaxAmount
	 */
	public boolean hasManualTax() {
		Enumeration aLineItemDetailList = this.getLineItemDetails();
		while(aLineItemDetailList.hasMoreElements()) {
			if (((POSLineItemDetail) aLineItemDetailList.nextElement()).isManualTaxAmount())
				return (true);
		}
		return (false);
	}

	/**
	 * Return <code>true</code> if the line item has a manual regional tax,
	 * <code>false</code> otherwise. A check is made against each line item
	 * detail to see if it has a manual regional tax amount.
	 * @return <code>true</code> if the line item has a manual regional tax,
	 * <code>false</code> otherwise.
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#isManualRegionalTaxAmount
	 */
	public boolean hasManualRegionalTax() {
		Enumeration aLineItemDetailList = this.getLineItemDetails();
		while(aLineItemDetailList.hasMoreElements()) {
			if (((POSLineItemDetail) aLineItemDetailList.nextElement()).isManualRegionalTaxAmount())
				return (true);
		}
		return (false);
	}

	/**
	 * Return <code>true</code> if the line item is a misc item,
	 * <code>false</code> otherwise. A check is made against the misc item ID
	 * to see if it's null.
	 * @return <code>true</code> if the line item is a misc item,
	 * <code>false</code> otherwise.
	 * @see #getMiscItemId
	 */
	public boolean isMiscItem() {
		return (this.getMiscItemId() != null);
	}

	/**
	 * Return <code>true</code> if the item is taxable,
	 * <code>false</code> otherwise. A check is made first to see if it's a misc item.
	 * If it is then return the misc item's taxable flag, else return the item's taxable flag.
	 * @return <code>true</code> if the item is taxable,
	 * <code>false</code> otherwise.
	 * @see #doGetMiscItemTaxable
	 * @see #getItem
	 * @see Item#isTaxable
	 * @see #doGetMiscItemTaxable
	 */
	public boolean isItemTaxable() {
		if (this.doGetMiscItemTaxable() == null)
			return (this.getItem().isTaxable());
		else
			return (this.doGetMiscItemTaxable().booleanValue());
	}

	/**
	 * Return <code>true</code> if the item is regional taxable,
	 * <code>false</code> otherwise. A check is made first to see if it's a misc item.
	 * If it is then return the misc item's taxable flag, else return the item's taxable flag.
	 * @return <code>true</code> if the item is taxable,
	 * <code>false</code> otherwise.
	 * @see #doGetMiscItemRegionalTaxable
	 * @see #getItem
	 * @see Item#isRegionalTaxable
	 * @see #doGetMiscItemRegionalTaxable
	 */
	public boolean isItemRegionalTaxable() {
		if (this.doGetMiscItemRegionalTaxable() == null)
			return (this.getItem().isRegionalTaxable());
		else
			return (this.doGetMiscItemRegionalTaxable().booleanValue());
	}

	/**
	 * Return <code>true</code> if the item requires a manual unit price,
	 * <code>false</code> otherwise.
	 * @return <code>true</code> if the item requires a manual unit price,
	 * <code>false</code> otherwise.
	 * @see #getItem
	 * @see Item#requiresManualUnitPrice
	 */
	public boolean isItemRequiringManualUnitPrice() {
		return (this.getItem().requiresManualUnitPrice());
	}

	// ***************
	// *** UTILITY ***
	// ***************

	/**
	 * Compare to another line item. If the sequence numbers are equal, return a 0. If this object's
	 * sequence number is less than passed object's sequence number, return a -1. Otherwise, return
	 * a 1.
	 * @param aLineItem An object that better be another POSLineItem.
	 * @return The result of the comparison.
	 * @see #getSequenceNumber
	 */
	public int compareTo(Object aLineItem) {
		int aLineItemSequenceNumber = ((POSLineItem) aLineItem).getSequenceNumber();
		if (this.sequenceNumber == aLineItemSequenceNumber)
			return (0);
		if (this.sequenceNumber < aLineItemSequenceNumber)
			return (-1);
		return (1);
	}

	/**
	 * Transfer internal information to another line item.
	 * The following is transferred: manualMarkdownAmount, manualMarkdownReason, manualUnitPrice,
	 * itemRetailPrice, itemSellingPrice, additionalConsultant, taxExemptId, regionalTaxExemptId,
	 * miscItemId, miscItemDescription, miscItemTaxable, miscItemRegionalTaxable, miscItemGLAccount,
	 * miscItemComment.
	 * @param aLineItem The line item to transfer the information to.
	 * @see #getManualMarkdownAmount
	 * @see #doSetManualMarkdownAmount(ArmCurrency)
	 * @see #getManualMarkdownReason
	 * @see #doSetManualMarkdownReason(String)
	 * @see #getManualUnitPrice
	 * @see #doSetManualUnitPrice(ArmCurrency)
	 * @see #getItemRetailPrice
	 * @see #doSetItemRetailPrice(ArmCurrency)
	 * @see #getItemSellingPrice
	 * @see #doSetItemSellingPrice(ArmCurrency)
	 * @see #getAdditionalConsultant
	 * @see #doSetAdditionalConsultant(Employee)
	 * @see #getTaxExemptId
	 * @see #doSetTaxExemptId(String)
	 * @see #getRegionalTaxExemptId
	 * @see #doSetRegionalTaxExemptId(String)
	 * @see #getMiscItemId
	 * @see #doSetMiscItemId(String)
	 * @see #doGetMiscItemDescription
	 * @see #doSetMiscItemDescription(String)
	 * @see #doGetMiscItemTaxable
	 * @see #doSetMiscItemTaxable(Boolean)
	 * @see #doGetMiscItemRegionalTaxable
	 * @see #doSetMiscItemRegionalTaxable(Boolean)
	 * @see #getMiscItemGLAccount
	 * @see #doSetMiscItemGLAccount(String)
	 * @see #getMiscItemComment
	 * @see #doSetMiscItemComment(String)
	 * @see #getLineItemGrouping
	 * @see POSLineItemGrouping#doSetItemPriceOverrideWithoutReset(ArmCurrency)
	 */
	public void transferInformationTo(POSLineItem aLineItem) {
		//added by shushma for promotion code
//		 if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
//		aLineItem.doSetPromotionCode(this.getPromotionCode());
//		 }
		aLineItem.doSetManualMarkdownAmount(this.getManualMarkdownAmount());
		aLineItem.doSetManualMarkdownReason(this.getManualMarkdownReason());
		aLineItem.doSetManualUnitPrice(this.getManualUnitPrice());
		aLineItem.doSetItemRetailPrice(this.getItemRetailPrice());
		aLineItem.doSetItemSellingPrice(this.getItemSellingPrice());
		aLineItem.doSetAdditionalConsultant(this.getAdditionalConsultant());
		aLineItem.doSetTaxExemptId(this.getTaxExemptId());
		aLineItem.doSetRegionalTaxExemptId(this.getRegionalTaxExemptId());
		aLineItem.doSetMiscItemId(this.getMiscItemId());
		aLineItem.doSetMiscItemDescription(this.doGetMiscItemDescription());
		aLineItem.doSetMiscItemTaxable(this.doGetMiscItemTaxable());
		aLineItem.doSetMiscItemRegionalTaxable(this.doGetMiscItemRegionalTaxable());
		aLineItem.doSetMiscItemGLAccount(this.getMiscItemGLAccount());
		aLineItem.doSetMiscItemComment(this.getMiscItemComment());
		aLineItem.getLineItemGrouping().doSetItemPriceOverrideWithoutReset(this.getLineItemGrouping().getItemPriceOverride());
	}

	/**
	 * Initialize line item details.
	 * When the quantity changes the number of line item detail objects must either increase
	 * or decrease. When increasing the number of line it details, more are just added. When
	 * decreasing the number of line item details, objects are removed from the rear.
	 * Once the number of line item details are set they are each asigned a simple sequence
	 * number.
	 * @param newQuantity The new quantity.
	 * @see POSLineItemDetail#doSetSequenceNumber(int)
	 */
	public void initializeLineItemDetails(int newQuantity) {
		int requiredAdjustment = newQuantity - this.lineItemDetails.size();
		if (requiredAdjustment > 0)
			for (int i = 0; i < requiredAdjustment; i++)
				this.newLineItemDetail();
		if (requiredAdjustment < 0)
			for (int i = 0; i < Math.abs(requiredAdjustment); i++)
				this.lineItemDetails.removeElementAt(this.lineItemDetails.size() - 1);

		Enumeration aLineItemDetailList = this.getLineItemDetails();
		int aSequenceNumber = 1;
		while(aLineItemDetailList.hasMoreElements())
			((POSLineItemDetail) aLineItemDetailList.nextElement()).doSetSequenceNumber(aSequenceNumber++);
	}

	/**
	 * Zero out all the line item detail amounts.
	 * @see #getLineItemDetails
	 * @see POSLineItemDetail#zeroAllAmounts
	 */
	public void zeroAllLineItemDetailAmounts() {
		Enumeration aLineItemDetailList = this.getLineItemDetails();
		while(aLineItemDetailList.hasMoreElements()) {
			((POSLineItemDetail) aLineItemDetailList.nextElement()).zeroAllAmounts();
		}
	}

	/**
	 * Cleanup line item details. This method's base implementation does nothing. See the subclasses.
	 */
	public void cleanupLineItemDetails() {
	}

	/**
	 * Broadcast the update to the transaction is the line item is not deleted.
	 * @see #getTransaction
	 * @see POSTransaction#update
	 */
	public void broadcastUpdate() {
		if (this.deletedFlag == false)
			this.getTransaction().update();
	}

	/**
	 * @return the extendedBarCode
	 */
	public String getExtendedBarCode() {
		return extendedBarCode;
	}

	/**
	 * @param extendedBarCode the extendedBarCode to set
	 */
	public void setExtendedBarCode(String extendedBarCode) {
		this.doSetExtendedBarCode(extendedBarCode);
	}
	
	/**
	 * @param extendedBarCode the extendedBarCode to set
	 */
	public void doSetExtendedBarCode(String extendedBarCode) {
		this.extendedBarCode = extendedBarCode;
	}

	//added by shushma for promoion code
	public void setPromotionCode(String promotionCode) {
		this.doSetPromotionCode(promotionCode);
	}

	public String getPromotionCode() {
		return promotionCode;
	}
	public void doSetPromotionCode(String promotionCode){
		this.promotionCode = promotionCode;
	}
//promoion code close
	
    //Added by Rachana for apporval of return transaction
	public String getApprover(){
		return approverId;
	 }
	 
	
	 public void setApprover(String anApprover)
	 throws BusinessRuleException
	 {
		 if(approverId == null || !approverId.equals(anApprover))
		 {
			 doSetApprover(anApprover);
		 }
	 }
	 
	 public void doSetApprover(String anApprover)
	 {
	     if(anApprover == null)
	     {
	    	 return;
	     } else
	     {
	    	 approverId = anApprover;
	         return;
	     }
	 }
	//End of approval of return transaction
    
	//Vivek Mishra : Added in order to fectch deleteFlag's value. 
	public boolean isDeletedFlag() {
		return deletedFlag;
	//Ends
	}

	public ArmCurrency getStateTax() {
		return stateTax;
	}

	public void setStateTax(ArmCurrency stateTax) {
		this.stateTax = stateTax;
	}

	public ArmCurrency getCityTax() {
		return cityTax;
	}

	public void setCityTax(ArmCurrency cityTax) {
		this.cityTax = cityTax;
	}
	public void setOverrideAmount(ArmCurrency overrideAmount) {
			this.overrideAmount = overrideAmount;
		}

		public ArmCurrency getOverrideAmount() {
			return overrideAmount;
		}
	public ArmCurrency getGstTaxAmt() {
		return gstTaxAmt;
	}

	public void setGstTaxAmt(ArmCurrency gstTaxAmt) {
		this.gstTaxAmt = gstTaxAmt;
	}

	public ArmCurrency getPstTaxAmt() {
		return pstTaxAmt;
	}

	public void setPstTaxAmt(ArmCurrency pstTaxAmt) {
		this.pstTaxAmt = pstTaxAmt;
	}

	public ArmCurrency getQstTaxAmt() {
		return qstTaxAmt;
	}

	public void setQstTaxAmt(ArmCurrency qstTaxAmt) {
		this.qstTaxAmt = qstTaxAmt;
	}

	public ArmCurrency getGsthstTaxAmt() {
		return gsthstTaxAmt;
	}

	public void setGsthstTaxAmt(ArmCurrency gsthstTaxAmt) {
		this.gsthstTaxAmt = gsthstTaxAmt;
	}

	//Vivek Mishra : Added to capture track data from 107 gift card swipe response 27-SEP-2016
	public String getTrackData() {
		return trackData;
	}

	public void setTrackData(String trackData) {
		this.trackData = trackData;
	}
    //Ends here 27-SEP-2016

	//Vivek Mishra : Added for Extended Barcode CR Europe 06-OCT-2016
	public String getExtendedStagingBarCode() {
		return extendedStagingBarCode;
	}

	public void setExtendedStagingBarCode(String extendedStagingBarCode) {
		this.extendedStagingBarCode = extendedStagingBarCode;
	}
	//Ends here 06-OCT-2016
 
	//Vivek Mishra : Added to capture AJB sequence number for gift card activation and reload 27-OCT-2016
	public String getAjbSequence() {
		return ajbSequence;
	}

	public void setAjbSequence(String ajbSequence) {
		this.ajbSequence = ajbSequence;
	}
	//Ends here 27-OCT-2016

// vishal yevale 10 nov 2016
	public ArmCurrency getGiftCardBalance() {
		return giftCardBalance;
	}

	public void setGiftCardBalance(ArmCurrency giftCardBalance) {
		this.giftCardBalance = giftCardBalance;
	}
// end vishal yevale 10 nov 2016
	
	//Ends here 27-OCT-2016
	
}