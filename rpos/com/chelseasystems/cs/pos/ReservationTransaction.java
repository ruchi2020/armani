/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * <p>Title:ReservationTransaction </p>
 * <p>Description: Used to persist Reservation transactions </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 10-06-2005 | Pankaja   | N/A       |  Restructing during the Merge Activity             |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 2    | 09-26-2005 | Manpreet  | N/A       |  Calculation of Net AMount                         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 07-11-2005 | Manpreet  | N/A       |  POS_104665_TS_Reservations_Rev4                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class ReservationTransaction extends POSTransaction implements IRuleEngine, Serializable {
  private String sReservationId;
  private Date dtExpiration;
  private ArmCurrency depositAmount;
  private String sReservationReasonCode;
  private ReservationTransaction originalRSVOTxn;
  static ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  /**
   * Constructor
   * @param compTxn CompositePOSTransaction
   */
  public ReservationTransaction(CompositePOSTransaction compTxn) {
    super(compTxn);
    depositAmount = new ArmCurrency(getBaseCurrencyType(), 0.0d);
  }

  /**
   * This method is used to create the line item for pre sale
   * @param anItem Item
   * @return POSLineItem
   */
  public POSLineItem createLineItem(Item anItem) {
    return new CMSReservationLineItem(this, anItem);
  }

  /**
   * This method is used to create the line item grouping for pre sale
   * @param lineItem POSLineItem
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping createLineItemGrouping(POSLineItem lineItem) {
    return new ReservationLineItemGrouping(lineItem);
  }

  /**
   * This method is used to set expiration date for pre sale
   * @param val Date
   */
  public void doSetExpirationDate(Date val) {
    this.dtExpiration = val;
  }

  /**
   * This method is used to set expiration date for pre sale
   * @param val Date
   */
  public void setExpirationDate(Date val) {
    doSetExpirationDate(val);
  }

  /**
   * This method is used to get expiration date for pre sale
   * @return Date
   */
  public Date getExpirationDate() {
    return this.dtExpiration;
  }

  /**
   * This method is used to set pre sale transaction id
   * @param val String
   */
  public void doSetReservationId(String val) {
    this.sReservationId = val;
  }

  /**
   * This method is used to get pre sale transaction id
   * @return String
   */
  public String getReservationId() {
    return this.sReservationId;
  }

  /**
   * This method is used to get net amount of the pre sale
   * @return Currency
   */
  public ArmCurrency getNetAmount() {
    try {
      ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
      for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements(); ) {
        ArmCurrency aLineItemExtendedNetAmount = ((CMSReservationLineItem)aLineItemList.nextElement()).
            getExtendedRetailAmount();
        total = total.add(aLineItemExtendedNetAmount);
      }
      return total;
    } catch (CurrencyException anException) {
      logCurrencyException("getNetAmount", anException);
    }
    return null;
  }

  /**
   * Deposit Amount
   * @param amount Currency
   */
  public void setDepositAmount(ArmCurrency amount)
      throws BusinessRuleException {
    executeRule("SetDepositAmount", amount);
    doSetDepositAmount(amount);
  }

  /**
   * Deposit Amount
   * @param amount Currency
   */
  public void doSetDepositAmount(ArmCurrency amount) {
    this.depositAmount = amount;
  }

  /**
   * Get Deposit Amount
   * @return Currency
   */
  public ArmCurrency getDepositAmount() {
    return this.depositAmount;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void doSetReservationReason(String sValue) {
    sReservationReasonCode = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setReservationReason(String sValue) {
    doSetReservationReason(sValue);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getReservationReason() {
    return sReservationReasonCode;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ReservationTransaction getOriginalRSVTransaction() {
    return originalRSVOTxn;
  }

  /**
   * put your documentation comment here
   * @param txn
   */
  public void setOriginalRSVOTransaction(ReservationTransaction txn) {
    originalRSVOTxn = txn;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ReservationTransaction getOriginalRSVOTxn() {
    return originalRSVOTxn;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getUnProcessedLineItemsArray() {
    ArrayList list = new ArrayList();
    for (Enumeration enm = this.getLineItems(); enm.hasMoreElements(); ) {
      CMSReservationLineItemDetail detail = (CMSReservationLineItemDetail)enm.nextElement();
      if (!detail.getProcessed())
        list.add(detail);
    }
    return (POSLineItem[])list.toArray(new POSLineItem[0]);
  }
  
  public String getExpireDateMessage(){
	return res.getString("RSV_EXPIRATION_DATE");  
  }
}

