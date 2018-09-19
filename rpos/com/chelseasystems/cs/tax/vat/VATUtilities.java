/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax.vat;

import java.util.*;
import com.chelseasystems.cs.tax.CMSTaxHelper;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.tax.ValueAddedTax;
import com.chelseasystems.cr.tax.ValueAddedTaxDetail;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.tax.*;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.pos.*;


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
public class VATUtilities {
  static ConfigMgr config = new ConfigMgr("tax.cfg");

  /**
   * @param theAppMgr
   * @param aCompositeTransaction
   * @param fromStore
   * @param toStore
   * @param aProcessDate
   * @return A SaleTax object. This corresponds the sale tax of the taxable line
   * item details in the composite transaction.
   * @exception Exception
   */
  public static void applyVAT(IRepositoryManager theAppMgr
      , CMSCompositePOSTransaction aCompositeTransaction, CMSStore fromStore, CMSStore toStore
      , Date aProcessDate)
      throws Exception {
    try {
      aCompositeTransaction.clearManualUnitPrice();
      if (aCompositeTransaction.isTaxExempt()) {
    	 POSLineItem[] posLineItems = aCompositeTransaction.getLineItemsArray();
        for (int index = 0; index < posLineItems.length; index++) {
        	//Fix for issue #1919 Vat Exemption txn isue
            if(posLineItems[index].isMiscItem()
            	&& !LineItemPOSUtil.isNotOnFileItem(posLineItems[index].getItem().getId())){
            	continue;
            }
            	POSLineItemDetail[] lineItemDetails = posLineItems[index].getLineItemDetailsArray();
            	double dVatRate = posLineItems[index].getItem().getVatRate().doubleValue();
            	posLineItems[index].doSetManualUnitPrice(posLineItems[index].getItemSellingPrice().divide(1
            			+ dVatRate));
            	posLineItems[index].doSetItemRetailPrice(null);
            	//bug# 30916
            	//posLineItems[index].doSetItemSellingPrice(null);
            	//end
            	//System.out.println("=====Misc Item id======= "+posLineItems[index].getMiscItemId());
            	//System.out.println("==============Pre VAT Amount========="+posLineItems[index].getManualUnitPrice());
            	for (int j = 0; j < lineItemDetails.length; j++) {
        	   		lineItemDetails[j].doSetVatAmount(new ArmCurrency(0.0d));
             }
          //System.out.println("============retail Price================" + posLineItems[index].getItemSellingPrice());
          //System.out.println("============Net Amount================" + posLineItems[index].getNetAmount());
            
        aCompositeTransaction.update();
        }//new check
      } else {
        ValueAddedTaxDetail[] vatTaxDetails = null;
        POSLineItemDetail[] taxableLineItemDetails = null;
        ValueAddedTax vatTax = null;
        CMSGenericValueAddedTaxServices localServices = new CMSGenericValueAddedTaxServices();
        vatTax = localServices.getOriginalValueAddedTax(aCompositeTransaction, fromStore, toStore
            , aProcessDate);
        vatTaxDetails = vatTax.getValueAddedTaxDetailArray();
        taxableLineItemDetails = aCompositeTransaction.getLineItemDetailsArray();
        for (int i = 0; i < vatTaxDetails.length; i++) {
          POSLineItem item = taxableLineItemDetails[i].getLineItem();
          taxableLineItemDetails[i].doSetVatAmount(vatTaxDetails[i].getAmount());
          //System.out.println("Ruchi misc items  "+aCompositeTransaction.getId());
          //System.out.println("==============VAT Amount========="+taxableLineItemDetails[i].getVatAmount());
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

