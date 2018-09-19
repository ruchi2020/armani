/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08-16-2005 | KS        | N/A       |From Global Implementation                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 08-16-2005 | KS        | N/A       |Post Txn                                            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.readings;

import com.chelseasystems.cr.readings.TrialBalance;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import java.util.Hashtable;
/**
 *
 * <p>Title: CMSTrialBalance</p>
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
public class CMSTrialBalance extends TrialBalance implements com.chelseasystems.cr.rules.
    IRuleEngine {
  private ArmCurrency cmsRedeemable = null;
  private ArmCurrency saleReturnTotal = new ArmCurrency(0.0);
  private ArmCurrency houseAccountTotal = new ArmCurrency(0.0);
  private ArmCurrency mallCertTotal = new ArmCurrency(0.0);
  private ArmCurrency mailCheckTotal = new ArmCurrency(0.0);
  private Hashtable paidIns = new Hashtable();
  private Hashtable paidOuts = new Hashtable();
  private Hashtable vatRateTable = new Hashtable();
  private Hashtable taxExemptIdTotal = new Hashtable();
  private ArmCurrency vatInvoiceTotals = new ArmCurrency(0.0);
  private Hashtable vatRateTotalForVatInvoice = new Hashtable();
  private ArmCurrency taxFreeTotalAmt = new ArmCurrency(0.0);
  private ArmCurrency totReductionAmt = new ArmCurrency(0.0);
  private ArmCurrency totVATAmount = new ArmCurrency(0.0);
  private Hashtable paymentTotalTable = new Hashtable();
  private Hashtable paymentMediaCountTable = new Hashtable();
  private Hashtable itemCountTable = new Hashtable();

  /**
   * Default Constructor
   */
  public CMSTrialBalance() {
    super();
    cmsRedeemable = new ArmCurrency(0.0D);
  }

  /**
   * Method sets redeemable total
   * @param amt Currency
   */
  public void doSetCMSRedeemableTotal(ArmCurrency amt) {
    if (amt == null) {
      return;
    } else {
      cmsRedeemable = amt;
      return;
    }
  }

  /**
   * Method returns redeemable total
   * @return Currency
   */
  public ArmCurrency getCMSRedeemable() {
    return cmsRedeemable;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleReturnTotal() {
    return saleReturnTotal;
  }

  /**
   * put your documentation comment here
   * @param total
   */
  public void setSaleReturnTotal(ArmCurrency total) {
    saleReturnTotal = total;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getHouseAccountTotal() {
    return houseAccountTotal;
  }

  /**
   * put your documentation comment here
   * @param houseAccountTotal
   */
  public void setHouseAccountTotal(ArmCurrency houseAccountTotal) {
    this.houseAccountTotal = houseAccountTotal;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getMallCertTotal() {
    return mallCertTotal;
  }

  /**
   * put your documentation comment here
   * @param mallCertTotal
   */
  public void setMallCertTotal(ArmCurrency mallCertTotal) {
    this.mallCertTotal = mallCertTotal;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getMailCheckTotal() {
    return mailCheckTotal;
  }

  /**
   * put your documentation comment here
   * @param mailCheckTotal
   */
  public void setMailCheckTotal(ArmCurrency mailCheckTotal) {
    this.mailCheckTotal = mailCheckTotal;
  }


  /**
   * put your documentation comment here
   * @param paidIns
   */
  public void setPaidOutsTotal(Hashtable paidOut) {
    this.paidOuts = paidOut;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getPaidOutsTotal() {
    return paidOuts;
  }


  /**
   * put your documentation comment here
   * @param paidIns
   */
  public void setPaidInsTotal(Hashtable paidIns) {
    this.paidIns = paidIns;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getPaidInsTotal() {
    return paidIns;
  }

  /**
   * put your documentation comment here
   * @param vatRatesTable
   */
  public void setVATTotalByRates(Hashtable vatRatesTable) {
    this.vatRateTable = vatRatesTable;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getVATTotalByRates() {
    return this.vatRateTable;
  }

  /**
   *
   * @param totalVATAmount Currency
   */
  public void setTotalVATAmount(ArmCurrency totalVATAmount) {
    this.totVATAmount = totalVATAmount;
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getTotalVATAmount() {
    return this.totVATAmount;
  }


  /**
   * put your documentation comment here
   * @param vatInvoiceTotal
   */
  public void setVATInvoiceTotal(ArmCurrency vatInvoiceTotal) {
    this.vatInvoiceTotals = vatInvoiceTotal;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getVATInvoiceTotal() {
    return this.vatInvoiceTotals;
  }

  /**
   * put your documentation comment here
   * @param taxExemptIdTotal
   */
  public void setTaxExemptIdTotal(Hashtable taxExemptIdTotal) {
    this.taxExemptIdTotal = taxExemptIdTotal;
//    java.util.Enumeration e = this.taxExemptIdTotal.keys();
//    while (e.hasMoreElements()) {
//    String key = (String)e.nextElement();
//    System.out.println("Tax Exempt key : " + key);
//    System.out.println("Tax Exempt Amount : " + this.taxExemptIdTotal.get(key));
//     }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getTaxExemptIdTotal() {
    return this.taxExemptIdTotal;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getVATInvoiceTotalByVatRate() {
    return this.vatRateTotalForVatInvoice;
  }

  /**
   * put your documentation comment here
   * @param vatRateTotalForVatInvoice
   */
  public void setVATInvoiceTotalByVatRate(Hashtable vatRateTotalForVatInvoice) {
    this.vatRateTotalForVatInvoice = vatRateTotalForVatInvoice;
    java.util.Enumeration e = this.vatRateTotalForVatInvoice.keys();
    while (e.hasMoreElements()) {
    Double key = (Double)e.nextElement();
    System.out.println("VAT Rate Invoice key : " + key);
    System.out.println("VAT Rate Invoice Amount : " + this.vatRateTotalForVatInvoice.get(key));
    }
  }

  /**
   * put your documentation comment here
   * @param taxFreeTotalAmt
   */
  public void setTaxFreeTotalAmt(ArmCurrency taxFreeTotalAmt) {
    this.taxFreeTotalAmt = taxFreeTotalAmt;
    System.out.println("Tax Free Total : " + taxFreeTotalAmt);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTaxFreeTotalAmt() {
    return this.taxFreeTotalAmt;
  }

  /**
   * put your documentation comment here
   * @param totReductionAmt
   */
  public void setTotReductionAmt(ArmCurrency totReductionAmt) {
    this.totReductionAmt = totReductionAmt;
    System.out.println("Reduction Total : " + totReductionAmt);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTotReductionAmt() {
    return this.totReductionAmt;
  }


  /**
   *
   * @return Currency
   */
  public ArmCurrency getTotalNetAmount() throws CurrencyException{
      return (this.getSaleTotal().add(this.getSaleReturnTotal())).
          subtract(this.getReturnsTotal());
  }

  public void setPaymentTotalsAllType(Hashtable paymentTotalsTable){
    this.paymentTotalTable = paymentTotalsTable;
    if(paymentTotalsTable.contains("DCRD")){
    	
    }
    System.out.println("Ruchi 1111paymentTotalTable " + paymentTotalTable);
  }

  public Hashtable getPaymentTotalsByType(){
    return this.paymentTotalTable;
  }


  public void setMediaCountAllPaymentType(Hashtable paymentMediaCountTable){
    this.paymentMediaCountTable = paymentMediaCountTable;
    System.out.println("Ruchi 2222 paymentMediaCountTable " + paymentMediaCountTable);
  }

  public Hashtable getMediaCountAllPaymentType(){
    return this.paymentMediaCountTable;
  }

  public Hashtable getTotalItemCount(){
    return this.itemCountTable;
  }

  public void setTotalItemCount(Hashtable itemCountTable){
    this.itemCountTable = itemCountTable;
    System.out.println("TB itemCountTable " + itemCountTable);
  }
}
