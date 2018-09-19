/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.pos.POSLineItemGrouping;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.SaleTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.Enumeration;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.item.CMSItem;


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
public class NonFiscalNoSaleTransaction extends SaleTransaction {
	int discSeqNum = 1;
  /**
   * put your documentation comment here
   * @param   CompositePOSTransaction aCompositeTransaction
   */
  public NonFiscalNoSaleTransaction(CompositePOSTransaction aCompositeTransaction) {
    super(aCompositeTransaction);
  }

  /**
   * This method is used to create sale transaction line item
   * @param anItem Item
   * @return POSLineItem
   */
  public POSLineItem createLineItem(Item anItem) {
    return new CMSNoSaleLineItem(this, anItem);
  }

  /**
   * This method is used to create sale transaction line item group
   * @param lineItem POSLineItem
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping createLineItemGrouping(POSLineItem lineItem) {
    return new CMSNoSaleLineItemGrouping(lineItem);
  }

  /**
   * This method is used to get net amount of the consignment
   * @return Currency
   */
//  public ArmCurrency getNetAmount() {
//    try {
//      ArmCurrency total = new ArmCurrency(getBaseCurrencyType(), 0.0D);
//      for (Enumeration aLineItemList = getLineItems(); aLineItemList.hasMoreElements(); ) {
//        ArmCurrency aLineItemExtendedNetAmount = ((CMSNoSaleLineItem)aLineItemList.nextElement()).
//            getExtendedRetailAmount();
//        total = total.add(aLineItemExtendedNetAmount);
//      }
//      return total;
//    } catch (CurrencyException anException) {
//      logCurrencyException("getNetAmount", anException);
//    }
//    return null;
//  }

  public SaleLineItem addEntireSaleLineItem(CMSSaleLineItem saleLnItm, int inc)
  throws BusinessRuleException {
	checkForNullParameter("addEntireSaleLineItem", saleLnItm);
	executeRule("addEntireSaleLineItem", saleLnItm);
    return addSaleLineItem(saleLnItm, saleLnItm.getQuantity().intValue(), inc);
	}

  /**
   * @param saleLnItm
   * @param aSaleQuantity
   * @param inc
   * @return
   * @exception BusinessRuleException
   */
  public SaleLineItem addSaleLineItem(SaleLineItem saleLnItm, int aSaleQuantity, int inc)
      throws BusinessRuleException {
		checkForNullParameter("addSaleLineItem", saleLnItm);
		executeRule("addSaleLineItem", saleLnItm);
    SaleLineItem aNoSaleLineItem = doAddSaleLineItem(saleLnItm, inc);
    aNoSaleLineItem.setQuantity(new Integer(aSaleQuantity));
    return aNoSaleLineItem;
	}

  /**
   * @param saleLnItm
   * @param inc
   * @return
   * @exception BusinessRuleException
   */
  public SaleLineItem doAddSaleLineItem(SaleLineItem saleLnItm, int inc)
  throws BusinessRuleException {
	  CMSNoSaleLineItem aNoSaleLineItem = (CMSNoSaleLineItem)newLineItem(saleLnItm.getItem());
//	  	System.out.println("Line Item Discount==="+saleLnItm.getDiscountsArray().length);
		for (Enumeration aDiscountList = this.getCompositeTransaction().getDiscounts(); aDiscountList.hasMoreElements();) {
			CMSDiscount discount = (CMSDiscount) aDiscountList.nextElement();
			for (Enumeration lineItemDiscounts = saleLnItm.getDiscounts(); lineItemDiscounts.hasMoreElements();) {
				CMSDiscount lineItemDiscount = (CMSDiscount)lineItemDiscounts.nextElement();
				if(discount.getSequenceNumber()==lineItemDiscount.getSequenceNumber()){
					discount.setIsLineItemDiscount(true);
					if (discount.getType().startsWith("BY_PRICE_DISCOUNT")) {
						aNoSaleLineItem.setAddPriceDiscount(discount);
					}
					aNoSaleLineItem.addDiscount(discount);
				}
			}
		}
		
		aNoSaleLineItem.doSetAdditionalConsultant(saleLnItm.getAdditionalConsultant());
		if (saleLnItm.getManualUnitPrice() != null)
			aNoSaleLineItem.setManualUnitPrice(saleLnItm.getManualUnitPrice());
		if (saleLnItm.getManualMarkdownReason() != null)
			aNoSaleLineItem.setManualMarkdownReason(saleLnItm.getManualMarkdownReason());
		if (saleLnItm.isMiscItem())
      saleLnItm.transferInformationTo(aNoSaleLineItem);
    return aNoSaleLineItem;
	}

}

