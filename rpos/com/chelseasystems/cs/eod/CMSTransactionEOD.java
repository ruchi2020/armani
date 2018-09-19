/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package com.chelseasystems.cs.eod;

import com.chelseasystems.cr.eod.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.Hashtable;
import java.util.ArrayList;
import com.chelseasystems.cr.rules.BusinessRuleException;


/**
 * put your documentation comment here
 */
public class CMSTransactionEOD extends TransactionEOD implements com.chelseasystems.cr.rules.
    IRuleEngine {
  static final long serialVersionUID = 5936470872752606879L;
  private Hashtable hTenderTotal = null;
  private Hashtable hTransactionTotal = null;
  private Hashtable hTaxTotal = null;
  public static final String TENDER_GROUP_CODE = "TND";
  public static final String TXN_GROUP_CODE = "TXN";
  public static final String TAX_GROUP_CODE = "TAX";
  private ArmCurrency mgrSaysMallCerts = null;

  /**
   * put your documentation comment here
   * @param   Store store
   */
  public CMSTransactionEOD(Store store) {
    super(store);
    hTenderTotal = new Hashtable();
    hTransactionTotal = new Hashtable();
    hTaxTotal = new Hashtable();
//ks: #1421 Mall Certificate Tender doesn't appear on the Consolidated Over/Short Report.
    mgrSaysMallCerts = new ArmCurrency(0.0);
  }

//ks: #1421 Mall Certificate Tender doesn't appear on the Consolidated Over/Short Report.
  public void setMgrSaysMallCert(ArmCurrency aCurrency)
      throws BusinessRuleException
  {
      checkForNullParameter("addMgrSaysMallCert", aCurrency);
      executeRule("setMgrSaysTravelersChecks", aCurrency);
      doSetMgrSaysMallCert(aCurrency);
  }

//ks: #1421 Mall Certificate Tender doesn't appear on the Consolidated Over/Short Report.
  public void doSetMgrSaysMallCert(ArmCurrency amt)
  {
      if(amt == null)
      {
          return;
      } else
      {
          mgrSaysMallCerts = amt;
          return;
      }
  }

//ks: #1421 Mall Certificate Tender doesn't appear on the Consolidated Over/Short Report.
  public ArmCurrency getMgrSaysMallCert()
  {
      return this.mgrSaysMallCerts;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception java.lang.Exception
   */
  public boolean post()
      throws java.lang.Exception {
    return TransactionEODServices.getCurrent().submit(this);
  }

  /**
   * put your documentation comment here
   * @param type
   * @param total
   * @param mediaCount
   */
  public void addEODTenderTotal(String type, ArmCurrency total, ArmCurrency reportedTotal, int mediaCount) {
    CMSEODNonDepositTotal eodNonDepositTotal = new CMSEODNonDepositTotal(type, total, reportedTotal, mediaCount
        , TENDER_GROUP_CODE);
    hTenderTotal.put(type, eodNonDepositTotal);
  }

  /**
   * put your documentation comment here
   * @param eodNonDepositTotal
   */
  public void addEODTenderTotal(CMSEODNonDepositTotal eodNonDepositTotal) {
    hTenderTotal.put(eodNonDepositTotal.getTypeCode(), eodNonDepositTotal);
  }

  /**
   * put your documentation comment here
   * @param type
   * @return
   */
  public CMSEODNonDepositTotal getEODTenderTotal(String type) {
    return (CMSEODNonDepositTotal)hTenderTotal.get(type);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getEODTenderTotals() {
    return hTenderTotal;
  }

  /**
   * put your documentation comment here
   * @param type
   * @param total
   * @param mediaCount
   */
  public void addEODTransactionTotal(String type, ArmCurrency total, int mediaCount) {
    CMSEODNonDepositTotal eodNonDepositTotal = new CMSEODNonDepositTotal(type, total, new ArmCurrency(0.0d), mediaCount
        , TXN_GROUP_CODE);
    hTransactionTotal.put(type, eodNonDepositTotal);
  }

  /**
   * put your documentation comment here
   * @param eodNonDepositTotal
   */
  public void addEODTransactionTotal(CMSEODNonDepositTotal eodNonDepositTotal) {
    hTransactionTotal.put(eodNonDepositTotal.getTypeCode(), eodNonDepositTotal);
  }

  /**
   * put your documentation comment here
   * @param type
   * @return
   */
  public CMSEODNonDepositTotal getEODTransactionTotal(String type) {
    return (CMSEODNonDepositTotal)hTransactionTotal.get(type);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getEODTransactionTotals() {
    return hTransactionTotal;
  }

  /**
   * put your documentation comment here
   * @param type
   * @param total
   */
  public void addEODTaxTotal(String type, ArmCurrency total) {
    CMSEODNonDepositTotal eodNonDepositTotal = new CMSEODNonDepositTotal(type, total, new ArmCurrency(0.0d), 0
        , TAX_GROUP_CODE);
    hTaxTotal.put(type, eodNonDepositTotal);
  }

  /**
   * put your documentation comment here
   * @param eodNonDepositTotal
   */
  public void addEODTaxTotal(CMSEODNonDepositTotal eodNonDepositTotal) {
    hTaxTotal.put(eodNonDepositTotal.getTypeCode(), eodNonDepositTotal);
  }

  /**
   * put your documentation comment here
   * @param type
   * @return
   */
  public CMSEODNonDepositTotal getEODTaxTotal(String type) {
    return (CMSEODNonDepositTotal)hTaxTotal.get(type);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getEODTaxTotals() {
    return hTaxTotal;
  }
}

