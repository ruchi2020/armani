/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	3/14/05	        KS	Pricing	                        Discount
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.discount;

import com.chelseasystems.cr.discount.*;
import com.chelseasystems.cr.currency.*;
import java.util.Date;


/**
 *
 * <p>Title: CMSDiscount</p>
 * <p>Description: This class store the attributes of discount</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Khyati Shah
 * @version 1.0
 */
public abstract class CMSDiscount extends Discount implements com.chelseasystems.cr.rules.
    IRuleEngine {
  static final long serialVersionUID = 8350808060163710291L;
  private int methodOfReduction = 0;
  public static int PERCENTAGE_OFF = 0;
  public static int FIXED_TOTAL_PRICE = 1;
  public static int TOTAL_PRICE_OFF = 2;
  public static int UNIT_PRICE_OFF = 3;
  private boolean manualEntry = false;
  private String discountCode = "";
  private String discountCodeNote = "";
  private boolean applyTo = false;
  public boolean isLineItemDiscount = false;
  public boolean isSubTotalDiscount = false;
  public boolean isMultiDiscount = false;
  private int sequenceNumber = 0;
  private boolean isCustomerRequired = true;
  private boolean shouldApplyDiscount = true;

  /**
   * Default Constructor
   */
  public CMSDiscount() {
  }

  /**
   * This method is used to check whether line item discount is there or not
   * @return boolean
   */
  public boolean getIsLineItemDiscount() {
    return this.isLineItemDiscount;
  }

  /**
   * This method is used to set whether line item discount is there or not
   * @param isLineItemDiscount boolean
   */
  public void setIsLineItemDiscount(boolean isLineItemDiscount) {
    this.isLineItemDiscount = isLineItemDiscount;
  }

  /**
   * This method is used to check whether sub discount is there or not
   * @return boolean
   */
  public boolean getIsSubTotalDiscount() {
    return this.isSubTotalDiscount;
  }

  /**
   * This method is used to get sequence number
   * @return int
   */
  public int getSequenceNumber() {
    return this.sequenceNumber;
  }

  /**
   * This method is used to set sequence number
   * @param sequenceNumber int
   */
  public void setSequenceNumber(int sequenceNumber) {
    doSetSequenceNumber(sequenceNumber);
  }

  /**
   * This method is used to set sequence number
   * @param sequenceNumber int
   */
  public void doSetSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * This method is used to set whether sub discount is there or not
   * @param isSubTotalDiscount boolean
   */
  public void setIsSubTotalDiscount(boolean isSubTotalDiscount) {
    this.isSubTotalDiscount = isSubTotalDiscount;
  }

  /**
   * This method is used to check whether discount is applied or not
   * @return boolean
   */
  public boolean getApplyTo() {
    return this.applyTo;
  }

  /**
   * This method is used to set whether discount is applied or not
   * @param applyTo boolean
   */
  public void setApplyTo(boolean applyTo) {
    this.applyTo = applyTo;
  }

  /**
   * This method is used to get method of reduction
   * @return int
   */
  public int getMethodOfReduction() {
    return this.methodOfReduction;
  }

  /**
   * This method is used to set method of reduction
   * @param methodOfReduction int
   */
  public void setMethodOfReduction(int methodOfReduction) {
    doSetMethodOfReduction(methodOfReduction);
  }

  /**
   * This method is used to set manual entry for discount
   * @param methodOfReduction int
   */
  public void doSetMethodOfReduction(int methodOfReduction) {
    this.methodOfReduction = methodOfReduction;
  }

  /**
   * This method is used to check whether a manual entry for discount or not
   * @return boolean
   */
  public boolean isManualEntry() {
    return this.manualEntry;
  }

  /**
   * This method is used to set manual entry for discount
   * @param manualEntry boolean
   */
  public void setManualEntry(boolean manualEntry) {
    this.manualEntry = manualEntry;
  }

  /**
   * This method is used to get discount code
   * @return String
   */
  public String getDiscountCode() {
    return this.discountCode;
  }

  /**
   * This method is used to set discount code
   * @param discountCode String
   */
  public void setDiscountCode(String discountCode) {
    doSetDiscountCode(discountCode);
  }

  /**
   * This method is used to set discount code
   * @param discountCode String
   */
  public void doSetDiscountCode(String discountCode) {
    this.discountCode = discountCode;
  }

  /**
   * This method is used to get discount code note
   * @return String
   */
  public String getDiscountCodeNote() {
    return this.discountCodeNote;
  }

  /**
   * This method is used to set discount code
   * @param discountCode String
   */
  public void setDiscountCodeNote(String discountCodeNote) {
    doSetDiscountCodeNote(discountCodeNote);
  }

  /**
   * This method is used to set discount code
   * @param discountCode String
   */
  public void doSetDiscountCodeNote(String discountCodeNote) {
    this.discountCodeNote = discountCodeNote;
  }

  /**
   * This method is used to check if multi discount exists
   * @return boolean
   */
  public boolean getIsMultiDiscount() {
    return this.isMultiDiscount;
  }

  /**
   * This method is used to set if multi discount exists
   * @param isMultiDiscount boolean
   */
  public void setIsMultiDiscount(boolean isMultiDiscount) {
    this.isMultiDiscount = isMultiDiscount;
  }

  /**
   * This method is used to set if customer is required
   * for transaction.
   * @param isCustRequired boolean
   */
  public void setIsCustomerRequired(boolean isCustRequired) {
    this.isCustomerRequired = isCustRequired;
  }

  /**
   * Check if Customer is required for the transaction.
   * @return boolean
   */
  public boolean isCustomerRequired() {
    return this.isCustomerRequired;
  }

  /**
   * This method is used to validate the Discount against
   * the Discount Rule.
   * @param isCustRequired boolean
   */
  public void setApplyDiscount(boolean shouldApplyDiscount) {
    this.shouldApplyDiscount = shouldApplyDiscount;
  }

  /**
   * Check if discount should be applied to the line item/transaction.
   * @return boolean
   */
  public boolean shouldApplyDiscount() {
    return this.shouldApplyDiscount;
  }

}

