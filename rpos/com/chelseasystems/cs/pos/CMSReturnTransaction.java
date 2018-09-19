/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-12-2005 | Rajesh    | N/A       |Specs Consignment impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-12-2005 | Rajesh    | N/A       |Specs Presale impl                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import java.io.*;
import java.util.*;
import com.chelseasystems.cr.business.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.pos.*;


/**
 *
 * <p>Title: CMSReturnTransaction</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh
 * @version 1.0
 */
public class CMSReturnTransaction extends ReturnTransaction implements com.chelseasystems.cr.rules.
    IRuleEngine {
  static final long serialVersionUID = 6563278136718664642L;

  /**
   * Constructor
   * @param aCompositeTransaction CompositePOSTransaction
   */
  public CMSReturnTransaction(CompositePOSTransaction aCompositeTransaction) {
    super(aCompositeTransaction);
  }

  /**
   * This method is used to create return line item
   * @param anItem Item
   * @return POSLineItem
   */
  public POSLineItem createLineItem(Item anItem) {
    return new CMSReturnLineItem(this, anItem);
  }

  /**
   * This method is used to create return line item groups
   * @param lineItem POSLineItem
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping createLineItemGrouping(POSLineItem lineItem) {
    return new CMSReturnLineItemGrouping(lineItem);
  }

  /**
   * This method is used to add presale return line item to the transaction
   * @param prsLnItm CMSPresaleLineItem
   * @param aReturnQuantity int
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addPresaleLineItem(CMSPresaleLineItem prsLnItm, int aReturnQuantity)
      throws BusinessRuleException {
    checkForNullParameter("addPresaleLineItem", prsLnItm);
    executeRule("addPresaleLineItem", prsLnItm);
    ReturnLineItem aReturnLineItem = doAddPresaleLineItem(prsLnItm);
    //  ___Tim: Bug 1701: set consultant for consignment/presale/reservation returns.
    if(prsLnItm.getAdditionalConsultant() != null)
    	aReturnLineItem.setAdditionalConsultant(prsLnItm.getAdditionalConsultant());
    aReturnLineItem.setQuantity(new Integer(aReturnQuantity));
    return aReturnLineItem;
  }

  /**
   * This method is used to add presale return line item to the transaction
   * @param prsLnItm CMSPresaleLineItem
   * @return ReturnLineItem
   */
  public ReturnLineItem doAddPresaleLineItem(CMSPresaleLineItem prsLnItm) {
    CMSReturnLineItem aReturnLineItem = (CMSReturnLineItem)newLineItem(prsLnItm.getItem());
    aReturnLineItem.doSetPresaleLineItem(prsLnItm);
    if (prsLnItm.isMiscItem())
      prsLnItm.transferInformationTo(aReturnLineItem);
    return aReturnLineItem;
  }

  /**
   * This method is used to add all presale return line item to the transaction
   * @param prsLnItm CMSPresaleLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addEntirePresaleLineItem(CMSPresaleLineItem prsLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntirePresaleLineItem", prsLnItm);
    executeRule("addEntirePresaleLineItem", prsLnItm);
    return addPresaleLineItem(prsLnItm, prsLnItm.getQuantityAvailableForReturn());
  }

  /**
   * This method is used to add all presale return line item to the transaction
   * @param prsTxn PresaleTransaction
   * @throws BusinessRuleException
   */
  public void addEntirePresaleTransaction(PresaleTransaction prsTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntirePresaleTransaction", prsTxn);
    executeRule("addEntirePresaleTransaction", prsTxn);
    for (Enumeration aPrsLineItemList = prsTxn.getLineItems(); aPrsLineItemList.hasMoreElements();
        addEntirePresaleLineItem((CMSPresaleLineItem)aPrsLineItemList.nextElement()));
  }

  /**
   * This method is used to add consignment line item to the transaction
   * as a return item
   * @param csgLnItm CMSConsignmentLineItem
   * @param aReturnQuantity int
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addConsignmentLineItem(CMSConsignmentLineItem csgLnItm, int aReturnQuantity)
      throws BusinessRuleException {
    checkForNullParameter("addConsignmentLineItem", csgLnItm);
    executeRule("addConsignmentLineItem", csgLnItm);
    ReturnLineItem aReturnLineItem = doAddConsignmentLineItem(csgLnItm);
		//  ___Tim: Bug 1701: set consultant for consignment/presale/reservation returns.
    if(csgLnItm.getAdditionalConsultant() != null)
		aReturnLineItem.setAdditionalConsultant(csgLnItm.getAdditionalConsultant());
    aReturnLineItem.setQuantity(new Integer(aReturnQuantity));
    return aReturnLineItem;
  }

  /**
   * This method is used to add consignment line item to the transaction
   * as a return item
   * @param csgLnItm CMSConsignmentLineItem
   * @return ReturnLineItem
   */
  public ReturnLineItem doAddConsignmentLineItem(CMSConsignmentLineItem csgLnItm) {
    CMSReturnLineItem aReturnLineItem = (CMSReturnLineItem)newLineItem(csgLnItm.getItem());
    aReturnLineItem.doSetConsignmentLineItem(csgLnItm);
    if (csgLnItm.isMiscItem())
      csgLnItm.transferInformationTo(aReturnLineItem);
    return aReturnLineItem;
  }

  /**
   * This method is used to add all consignment return line item to the transaction
   * @param csgLnItm CMSConsignmentLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addEntireConsignmentLineItem(CMSConsignmentLineItem csgLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntireConsignmentLineItem", csgLnItm);
    executeRule("addEntireConsignmentLineItem", csgLnItm);
    return addConsignmentLineItem(csgLnItm, csgLnItm.getQuantityAvailableForReturn());
  }

  /**
   * This method is used to add all consignment return line item to the transaction
   * @param csgTxn ConsignmentTransaction
   * @throws BusinessRuleException
   */
  public void addEntireConsignmentTransaction(ConsignmentTransaction csgTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntireConsignmentTransaction", csgTxn);
    executeRule("addEntireConsignmentTransaction", csgTxn);
    for (Enumeration aPrsLineItemList = csgTxn.getLineItems(); aPrsLineItemList.hasMoreElements();
        addEntireConsignmentLineItem((CMSConsignmentLineItem)aPrsLineItemList.nextElement()));
  }

  /**
   * This method is used to add reservation return line item to the transaction
   * @param resLnItm CMSReservationLineItem
   * @param aReturnQuantity int
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addReservationLineItem(CMSReservationLineItem resLnItm, int aReturnQuantity)
      throws BusinessRuleException {
    checkForNullParameter("addReservationLineItem", resLnItm);
    executeRule("addReservationLineItem", resLnItm);
    ReturnLineItem aReturnLineItem = doAddReservationLineItem(resLnItm);
    //___Tim: Bug 1701: set consultant for consignment/presale/reservation returns.
    if(resLnItm.getAdditionalConsultant() != null)
    	aReturnLineItem.setAdditionalConsultant(resLnItm.getAdditionalConsultant());
    aReturnLineItem.setQuantity(new Integer(aReturnQuantity));
    return aReturnLineItem;
  }

  /**
   * This method is used to add reservation return line item to the transaction
   * @param prsLnItm CMSReservationLineItem
   * @return ReturnLineItem
   */
  public ReturnLineItem doAddReservationLineItem(CMSReservationLineItem resLnItm) {
    CMSReturnLineItem aReturnLineItem = (CMSReturnLineItem)newLineItem(resLnItm.getItem());
    aReturnLineItem.doSetReservationLineItem(resLnItm);
    if (resLnItm.isMiscItem())
      resLnItm.transferInformationTo(aReturnLineItem);
    return aReturnLineItem;
  }

  /**
   * This method is used to add all reservation return line item to the transaction
   * @param resLnItm CMSReservationLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addEntireReservationLineItem(CMSReservationLineItem resLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntireReservationLineItem", resLnItm);
    executeRule("addEntireReservationLineItem", resLnItm);
    return addReservationLineItem(resLnItm, resLnItm.getQuantityAvailableForReturn());
  }

  /**
   * This method is used to add all Reservation return line item to the transaction
   * @param resTxn ReservationTransaction
   * @throws BusinessRuleException
   */
  public void addEntireReservationTransaction(ReservationTransaction resTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntirePOSLineItemTransaction", resTxn);
    executeRule("addEntirePOSLineItemTransaction", resTxn);
    for (Enumeration aResLineItemList = resTxn.getLineItems(); aResLineItemList.hasMoreElements();
        addEntireReservationLineItem((CMSReservationLineItem)aResLineItemList.nextElement()));
  }

  /**
   *
   * @param rsLnItm CMSReservationLineItem
   * @param iReturnQty int
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addReservationLineItemAsVoid(CMSReservationLineItem rsLnItm, int iReturnQty)
      throws BusinessRuleException {
    checkForNullParameter("addReservationLineItemAsVoid", rsLnItm);
    executeRule("addReservationLineItemAsVoid", rsLnItm);
    ReturnLineItem aReturnLineItem = doAddReservationLineItemAsVoid(rsLnItm);
    //  ___Tim: Bug 1701: set consultant for consignment/presale/reservation returns.
    if(rsLnItm.getAdditionalConsultant() != null)
    	aReturnLineItem.setAdditionalConsultant(rsLnItm.getAdditionalConsultant());
    aReturnLineItem.setQuantity(new Integer(iReturnQty));
    return aReturnLineItem;
  }

  /**
   *
   * @param rsLnItm CMSReservationLineItem
   * @return ReturnLineItem
   */
  public ReturnLineItem doAddReservationLineItemAsVoid(CMSReservationLineItem rsLnItm) {
    CMSVoidLineItem aVoidLineItem = (CMSVoidLineItem)newVoidLineItem(rsLnItm.getItem());
    aVoidLineItem.doSetReservationLineItem(rsLnItm);
    if (rsLnItm.isMiscItem())
      rsLnItm.transferInformationTo(aVoidLineItem);
    return aVoidLineItem;
  }

  /**
   * This method is used to get the net amount of return line item
   * @return Currency
   */
  public ArmCurrency getNetAmount() {
    try {
      ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
      for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements(); ) {
        CMSReturnLineItem lineItem = (CMSReturnLineItem)aLineItemList.nextElement();
        ArmCurrency aLineItemExtendedNetAmount = new ArmCurrency(getBaseCurrencyType(), 0.0d);
        if (lineItem.getPresaleLineItem() != null || lineItem.getConsignmentLineItem() != null
            || lineItem.getReservationLineItem() != null)
          continue;
        else
          aLineItemExtendedNetAmount = lineItem.getExtendedNetAmount();
        total = total.add(aLineItemExtendedNetAmount);
      }
      return total;
    } catch (CurrencyException anException) {
      logCurrencyException("getNetAmount", anException);
    }
    return null;
  }

  /**
   * put your documentation comment here
   * @param aSaleLineItem
   * @return
   */
  public ReturnLineItem doAddSaleLineItem(SaleLineItem aSaleLineItem) {
    ReturnLineItem aReturnLineItem = super.doAddSaleLineItem(aSaleLineItem);
    aReturnLineItem.doSetItemRetailPrice(aSaleLineItem.getItemRetailPrice());
    ((CMSReturnLineItem)aReturnLineItem).doSetIsForExchange(new Boolean(false));
    return aReturnLineItem;
  }

  /**
   * This method is used to get the retail amount of return line item
   * @return Currency
   */
  public ArmCurrency getRetailAmount() {
    try {
      ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
      for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements(); ) {
        CMSReturnLineItem lineItem = (CMSReturnLineItem)aLineItemList.nextElement();
        ArmCurrency aLineItemExtendedRetailAmount = new ArmCurrency(getBaseCurrencyType(), 0.0d);
        if (lineItem.getPresaleLineItem() != null || lineItem.getConsignmentLineItem() != null
            || lineItem.getReservationLineItem() != null)
          continue;
        else
          aLineItemExtendedRetailAmount = lineItem.getExtendedRetailAmount();
        total = total.add(aLineItemExtendedRetailAmount);
      }
      return total;
    } catch (CurrencyException anException) {
      logCurrencyException("getRetailAmount", anException);
    }
    return null;
  }

  /**
   *
   * @param anItem Item
   * @return POSLineItem
   */
  public POSLineItem newVoidLineItem(Item anItem) {
    POSLineItem aLineItem = createVoidLineItem(anItem);
    return aLineItem;
  }

  /**
   * This method is used to create void line item
   * @param anItem Item
   * @return POSLineItem
   */
  public POSLineItem createVoidLineItem(Item anItem) {
    return new CMSVoidLineItem(this, anItem);
  }

  /**
   *
   * @return POSLineItem[]
   */
  public POSLineItem[] getLineItemsArray() {
    POSLineItem[] lineItems = super.getLineItemsArray();
    ArrayList list = new ArrayList();
    for (int index = 0; index < lineItems.length; index++) {
      //            if (lineItems[index] instanceof CMSVoidLineItem)
      //                continue;
      list.add(lineItems[index]);
    }
    return (POSLineItem[])list.toArray(new POSLineItem[0]);
  }

  /**
   *
   * @return POSLineItem[]
   */
  public POSLineItem[] getVoidLineItemsArray() {
    POSLineItem[] lineItems = super.getLineItemsArray();
    ArrayList list = new ArrayList();
    for (int index = 0; index < lineItems.length; index++) {
      if (lineItems[index] instanceof CMSVoidLineItem)
        list.add(lineItems[index]);
    }
    return (POSLineItem[])list.toArray(new POSLineItem[0]);
  }


  /**
   * This method is used to add no reservation line item
   * @param item CMSItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addNoReservationLineItem(Item item)
      throws BusinessRuleException {
    checkForNullParameter("addNoReservationLineItem", this);
    executeRule("addNoReservationLineItem", this);
    CMSReservationLineItem rsvLineItem = new CMSReservationLineItem(((
        CMSCompositePOSTransaction)getCompositeTransaction()).getNoReservationOpenTransaction()
        , item);
    rsvLineItem.setQuantity(new Integer(1));
    return addReservationLineItem(rsvLineItem, 1);
  }
}

