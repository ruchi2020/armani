/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.tax.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import java.util.*;


/**
 *
 * <p>Title: CMSTaxHelper</p>
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
public class CMSTaxHelper {

  /**
   * @param theAppMgr
   * @param aCompositeTransaction
   * @param fromStore
   * @param toStore
   * @param aProcessDate
   * @return
   * @exception Exception
   */
  public static SaleTax getTaxAmounts(IRepositoryManager theAppMgr
      , CMSCompositePOSTransaction aCompositeTransaction, CMSStore fromStore, CMSStore toStore
      , Date aProcessDate)
      throws Exception {
    CMSTaxClientServices service = (CMSTaxClientServices)theAppMgr.getGlobalObject("TAX_SRVC");
    return service.getTaxAmounts(aCompositeTransaction, fromStore, toStore, aProcessDate);
  }
  
  public static SaleTax getTaxAmounts(IRepositoryManager theAppMgr
	      , CMSCompositePOSTransaction aCompositeTransaction, CMSStore fromStore, CMSStore toStore
	      , Date aProcessDate, HashMap<String, Object[]> taxDetailMap)
	      throws Exception {
	    CMSTaxClientServices service = (CMSTaxClientServices)theAppMgr.getGlobalObject("TAX_SRVC");
	    return service.getTaxAmounts(aCompositeTransaction, fromStore, toStore, aProcessDate, taxDetailMap);
	  }

  //findByZipCode
  public static ArmTaxRate[] findByZipCode(IRepositoryManager theAppMgr, String zipcode)
      throws Exception {
    CMSTaxClientServices service = (CMSTaxClientServices)theAppMgr.getGlobalObject("TAX_SRVC");
    return service.findByZipCode(zipcode);
  }
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
  /* Moved to com.chelseasystems.cs.tax.TaxUtilities
   public static void applyTax (IRepositoryManager theAppMgr, CMSCompositePOSTransaction aCompositeTransaction,
   CMSStore fromStore, CMSStore toStore, Date aProcessDate) throws Exception {
   SaleTax saleTax = getTaxAmounts(theAppMgr, aCompositeTransaction, fromStore, toStore, aProcessDate);
   SaleTaxDetail[] taxDetails = saleTax.getSaleTaxDetailArray();
   POSLineItemDetail[] taxableLineItemDetails = aCompositeTransaction.getTaxableLineItemDetailsArray();
   if (taxDetails.length != taxableLineItemDetails.length)
   throw new ArrayIndexOutOfBoundsException("Unable to apply taxes.  Index out of range.");
   for (int i = 0; i < taxDetails.length; i++) {
   taxableLineItemDetails[i].doSetTaxAmount(taxDetails[i].getAmount());
   }
   SaleTaxDetail[] regionalTaxDetails = saleTax.getRegionalSaleTaxDetailArray();
   POSLineItemDetail[] regionalTaxableLineItemDetails = aCompositeTransaction.getRegionalTaxableLineItemDetailsArray();
   if (regionalTaxDetails.length != regionalTaxableLineItemDetails.length)
   throw new ArrayIndexOutOfBoundsException("Unable to apply regional taxes.  Index out of range.");
   for (int i = 0; i < regionalTaxDetails.length; i++) {
   regionalTaxableLineItemDetails[i].doSetRegionalTaxAmount(regionalTaxDetails[i].getAmount());
   }
   }
   */
}

