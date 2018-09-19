/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import java.util.Enumeration;
import java.io.Serializable;
import java.util.Date;
import com.chelseasystems.cr.pos.MiscItem;
import com.chelseasystems.cr.item.MiscItemManager;


/**
 *
 * <p>Title: ConsignmentTransaction</p>
 *
 * <p>Description: This class store the consignment transaction attribute</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh Pradhan
 * @version 1.0
 */
public class ConsignmentTransaction extends POSTransaction implements IRuleEngine, Serializable {
  private String csgId;
  private Date expDate;

  /**
   * Constructor
   * @param compTxn CompositePOSTransaction
   */
  public ConsignmentTransaction(CompositePOSTransaction compTxn) {
    super(compTxn);
  }

  /**
   * This method is used to create line item for the consignment
   * @param anItem Item
   * @return POSLineItem
   */
  public POSLineItem createLineItem(Item anItem) {
    return new CMSConsignmentLineItem(this, anItem);
  }

  /**
   * This method is used to create line item group for the consignment
   * @param lineItem POSLineItem
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping createLineItemGrouping(POSLineItem lineItem) {
    return new ConsignmentLineItemGrouping(lineItem);
  }

  /**
   * This method is used to set expiration date for consignment
   * @param val Date
   */
  public void doSetExpirationDate(Date val) {
    this.expDate = val;
  }

  /**
   * This method is used to set expiration date for consignment
   * @param val Date
   */
  public void setExpirationDate(Date val) {
    doSetExpirationDate(val);
  }

  /**
   * This method is used to get expiration date for consignment
   * @return Date
   */
  public Date getExpirationDate() {
    return this.expDate;
  }

  /**
   * This method is used to set consignment transaction id
   * @param val String
   */
  public void doSetConsignmentId(String val) {
    this.csgId = val;
  }

  /**
   * This method is used to get consignment transaction id
   * @return String
   */
  public String getConsignmentId() {
    return this.csgId;
  }

  /**
   * This method is used to get net amount of the consignment
   * @return Currency
   */
  public ArmCurrency getNetAmount() {
    try {
      ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
      for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements(); ) {
        ArmCurrency aLineItemExtendedNetAmount = ((CMSConsignmentLineItem)aLineItemList.nextElement()).
            getExtendedRetailAmount();
        total = total.add(aLineItemExtendedNetAmount);
      }
      return total;
    } catch (CurrencyException anException) {
      logCurrencyException("getNetAmount", anException);
    }
    return null;
  }
}

