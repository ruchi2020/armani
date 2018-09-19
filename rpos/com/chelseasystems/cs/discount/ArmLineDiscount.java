package com.chelseasystems.cs.discount;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.discount.Discount;

import com.chelseasystems.cs.pos.*;

import java.util.Vector;
import java.util.Enumeration;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ArmLineDiscount extends CMSDiscount {
  private static final long serialVersionUID = 0xd6aa8b68cc66d9b8L;
  private Vector lineItemDetails = null;
  private Vector lineItems = null;
  private ArmCurrency discountTakenOff = null;
  private ArmCurrency discountNetAmount = null;
  private ArmCurrency totalPriceDiscount = null;

  public ArmLineDiscount() {
    super();
    lineItemDetails = new Vector();
    lineItems = new Vector();
    discountTakenOff = new ArmCurrency(0.0d);
    discountNetAmount = new ArmCurrency(0.0d);
    totalPriceDiscount = new ArmCurrency(0.0d);
  }

  /**
   * isValid
   *
   * @param compositePOSTransaction CompositePOSTransaction
   * @throws BusinessRuleException
   */
  public void isValid(CompositePOSTransaction txnPos)
      throws BusinessRuleException {
    RuleEngine.execute(getClass().getName(), "isValid", txnPos, null);
  }

  /**
   * This method is used to add the line item
   * @param aLineItem POSLineItemDetail
   */
  public void doAddLineItem(POSLineItem aLineItem) {
    if (aLineItem instanceof CMSSaleLineItem) {
      Discount priceDiscount = ((CMSSaleLineItem)aLineItem).getPriceDiscount();
      if (priceDiscount != null)
        try {
          totalPriceDiscount = totalPriceDiscount.add(/*aLineItem.getItemSellingPrice().subtract(aLineItem.getExtendedDealMarkdownAmount()).subtract(
              */priceDiscount.getAmount());
        } catch (CurrencyException e) {}
    }
    for (Enumeration aLineItemDetailList = aLineItem.getLineItemDetails();
        aLineItemDetailList.hasMoreElements(); ) {
      lineItemDetails.addElement(aLineItemDetailList.nextElement());
    }
    lineItems.addElement(aLineItem);
  }

  /**
   * This method is used to add the line item
   * @param aLineItem POSLineItemDetail
   */
  public void doAddLineItemDetail(POSLineItemDetail aLineItemDetail) {
    lineItemDetails.addElement(aLineItemDetail);
  }

  /**
   * This method is used to remove the line item
   * @param aLineItem POSLineItemDetail
   */
  public void doRemoveLineItemDetail(POSLineItemDetail aLineItemDetail) {
    lineItemDetails.removeElement(aLineItemDetail);
  }

  /**
   * This method is used to get the line items
   * @return Enumeration
   */
  public Enumeration getLineItemDetails() {
    return lineItemDetails.elements();
  }

  /**
   * This method is used to add the line items
   * @return POSLineItem[]
   */
  public POSLineItemDetail[] getLineItemDetailArray() {
    return (POSLineItemDetail[])lineItemDetails.toArray(new POSLineItemDetail[0]);
  }

  /**
   * This method is used to remove the line item
   * @param aLineItem POSLineItemDetail
   */
  public void doRemoveLineItem(POSLineItem aLineItem) {
    lineItems.removeElement(aLineItem);
  }

  /**
   * This method is used to get the line items
   * @return Enumeration
   */
  public Enumeration getLineItems() {
    return lineItems.elements();
  }

  /**
   * This method is used to add the line items
   * @return POSLineItem[]
   */
  public POSLineItem[] getLineItemsArray() {
    return (POSLineItem[])lineItems.toArray(new POSLineItem[0]);
  }

  /**
   *
   * @param balance Currency
   */
  public void setTotalDiscountTakenOff(ArmCurrency discount) {
    this.discountTakenOff = discount;
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getTotalDiscountTakenOff() {
    return this.discountTakenOff;
  }

  public ArmCurrency getRemainingBalance() throws CurrencyException {
    return this.getAmount().subtract(this.discountTakenOff);
  }

  public ArmCurrency getCompositeNetAmount() {
    ArmCurrency totalNetAmount = new ArmCurrency(0.0d);
    try {
      for (Enumeration aLineItemDetailList = this.getLineItemDetails();
          aLineItemDetailList.hasMoreElements(); ) {
        POSLineItemDetail lineItemDetail = (POSLineItemDetail)aLineItemDetailList.nextElement();
        totalNetAmount = totalNetAmount.add(lineItemDetail.getNetAmount());
      }
      return totalNetAmount.subtract(this.totalPriceDiscount);
    } catch (CurrencyException ce) {
      return new ArmCurrency(0.0d);
    }
  }

  public void setDiscountNetAmount(ArmCurrency netAmount) {
    this.discountNetAmount = netAmount;
  }

/*  public ArmCurrency getDiscountNetAmount()
      throws CurrencyException {
    return this.discountNetAmount.add(this.totalPriceDiscount);
  }*/

  public ArmCurrency getDiscountNetAmount() {
    return this.discountNetAmount;
  }

  public void addDiscountNetAmount(ArmCurrency netAmount)
      throws CurrencyException {
    this.discountNetAmount = this.discountNetAmount.add(netAmount);
  }

}
