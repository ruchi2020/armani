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
 | 1    | 08-18-2005 | KS        | N/A       |From Global Implementation                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 08-18-2005 | KS        | N/A       |Post Txn                                            |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.eod;

import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.readings.CMSTrialBalance;
import java.util.*;


/**
 *
 * <p>Title: CMSEODReport</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSEODReport implements java.io.Serializable {

  /** Serial version id. */
  static final long serialVersionUID = -328318546230725951L;
  // ***************************
  // *** DEFINE - PROPERTIES ***
  // ***************************
  private ArmCurrency returnTotal = new ArmCurrency(0.0);
  private ArmCurrency voidTotal = new ArmCurrency(0.0);
  private ArmCurrency layawayRTSTotal = new ArmCurrency(0.0);
  private ArmCurrency layawaySaleTotal = new ArmCurrency(0.0);
  private ArmCurrency layawayPaymentTotal = new ArmCurrency(0.0);
  private ArmCurrency collectionTotal = new ArmCurrency(0.0);
  private ArmCurrency paidoutTotal = new ArmCurrency(0.0);
  private ArmCurrency paidInsTotal = new ArmCurrency(0.0);
  private ArmCurrency saleTotal = new ArmCurrency(0.0);
  private ArmCurrency drawerFundTotal = new ArmCurrency(0.0);
  private ArmCurrency initialDrawerFund = new ArmCurrency(0.0);
  private ArmCurrency storeExpenses = new ArmCurrency(0.0);
  private ArmCurrency cashDrops = new ArmCurrency(0.0);
  //finacial totals
  private ArmCurrency markdownTotal = new ArmCurrency(0.0);
  private ArmCurrency reductionTotal = new ArmCurrency(0.0);
  private ArmCurrency salesTaxTotal = new ArmCurrency(0.0);
  private ArmCurrency regionalTaxTotal = new ArmCurrency(0.0);
  private ArmCurrency discountTotal = new ArmCurrency(0.0);
  private ArmCurrency feeTotal = new ArmCurrency(0.0);
  //payment totals
  private Vector accountableMedia = new Vector();
  // non-dynamic payment amounts
  private ArmCurrency cmsRedeemableTendered = new ArmCurrency(0.0);
  private ArmCurrency giftCertificateIssued = new ArmCurrency(0.0);
  private ArmCurrency giftCertificateTendered = new ArmCurrency(0.0);
  private ArmCurrency giftCertificateReported = new ArmCurrency(0.0);
  private ArmCurrency creditMemoTendered = new ArmCurrency(0.0);
  private ArmCurrency creditMemoReported = new ArmCurrency(0.0);
  private ArmCurrency creditMemoIssued = new ArmCurrency(0.0);
  private ArmCurrency storeValueCardIssued = new ArmCurrency(0.0);
  private ArmCurrency storeValueCardTendered = new ArmCurrency(0.0);
  private ArmCurrency mfrCouponTendered = new ArmCurrency(0.0);
  private ArmCurrency mfrCouponReported = new ArmCurrency(0.0);
  private ArmCurrency privateCardTendered = new ArmCurrency(0.0);
  private ArmCurrency privateCardReported = new ArmCurrency(0.0);
  private ArmCurrency redeemableBuyBackTotal = new ArmCurrency(0.0);
  private ArmCurrency houseAccountTotal = new ArmCurrency(0.0);
  private ArmCurrency mallCertTotal = new ArmCurrency(0.0);
  private ArmCurrency mailCheckTotal = new ArmCurrency(0.0);
  private Vector bankDepositTotal = new Vector();
  private String oper;
  private String store;
  private String eodDate;
  private String register;
  private ArmCurrency saleReturnTotal = new ArmCurrency(0.0);
  //Europe
  private CMSTrialBalance cmsTrialBalance;
  //paid ins totals
  private Vector paidInTotal = new Vector();

  private String txnNumber;

  /**
   *
   * <p>Title: ReductionsStruct</p>
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
  public class ReductionsStruct implements java.io.Serializable {
    private ResourceBundleKey reductionType;
    private ArmCurrency reductionAmt;

    /**
     *
     * @param reductionType ResourceBundleKey
     * @param reductionAmt Currency
     */
    public ReductionsStruct(ResourceBundleKey reductionType, ArmCurrency reductionAmt) {
      this.reductionAmt = reductionAmt;
      this.reductionType = reductionType;
    }

    /**
     * This method is used to get reduction key
     * @return ResourceBundleKey
     */
    public ResourceBundleKey getReductionType() {
      return reductionType;
    }

    /**
     * This method is used to get reduction amount
     * @return Currency
     */
    public ArmCurrency getReductionAmt() {
      return reductionAmt;
    }
  }


  public class PaidInsStruct implements java.io.Serializable {
    private String paidInType;
    private ArmCurrency paidInAmt;

    /**
     *
     * @param reductionType ResourceBundleKey
     * @param reductionAmt Currency
     */
    public PaidInsStruct(String paidInType, ArmCurrency paidInAmt) {
      this.paidInAmt = paidInAmt;
      this.paidInType = paidInType;
    }

    /**
     * This method is used to get reduction key
     * @return ResourceBundleKey
     */
    public String getPaidInType() {
      return paidInType;
    }

    /**
     * This method is used to get reduction amount
     * @return Currency
     */
    public ArmCurrency getPaidInAmt() {
      return paidInAmt;
    }
  }


  public class PaidOutsStruct implements java.io.Serializable {
    private String paidOutType;
    private ArmCurrency paidOutAmt;

    /**
     *
     * @param reductionType ResourceBundleKey
     * @param reductionAmt Currency
     */
    public PaidOutsStruct(String paidOutType, ArmCurrency paidOutAmt) {
      this.paidOutAmt = paidOutAmt;
      this.paidOutType = paidOutType;
    }

    /**
     * This method is used to get reduction key
     * @return ResourceBundleKey
     */
    public String getPaidOutType() {
      return paidOutType;
    }

    /**
     * This method is used to get reduction amount
     * @return Currency
     */
    public ArmCurrency getPaidOutAmt() {
      return paidOutAmt;
    }
  }
  public class CreditCardStruct implements java.io.Serializable {
    private String cardType;
    private ArmCurrency cardAmt;

    /**
     *
     * @param reductionType ResourceBundleKey
     * @param reductionAmt Currency
     */
    public CreditCardStruct(String cardType, ArmCurrency cardAmt) {
      this.cardAmt = cardAmt;
      this.cardType = cardType;
    }

    /**
     * This method is used to get reduction key
     * @return ResourceBundleKey
     */
    public String getCreditCardType() {
      return cardType;
    }

    /**
     * This method is used to get reduction amount
     * @return Currency
     */
    public ArmCurrency getCreditCardAmt() {
      return cardAmt;
    }
  }


  private ReductionsStruct[] reductionsStruct;
  private CreditCardStruct[] creditCardsStruct;
  private PaidInsStruct[] paidInsStruct;
  private PaidOutsStruct[] paidOutsStruct;
  /**
   * Default Constructor
   */
  public CMSEODReport() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   *
   * @param reductionsStruct ReductionsStruct[]
   */
  public void setPaidInsStruct(PaidInsStruct[] paidInsStruct) {
    this.paidInsStruct = paidInsStruct;
  }

  /**
   *
   * @return ReductionsStruct[]
   */
  public PaidInsStruct[] getPaidInsStruct() {
    return paidInsStruct;
  }

  /**
   *
   * @param reductionsStruct ReductionsStruct[]
   */
  public void setPaidOutsStruct(PaidOutsStruct[] paidOutsStruct) {
    this.paidOutsStruct = paidOutsStruct;
  }

  /**
   *
   * @return ReductionsStruct[]
   */
  public PaidOutsStruct[] getPaidOutsStruct() {
    return paidOutsStruct;
  }
  /**
   *
   * @param reductionsStruct ReductionsStruct[]
   */
  public void setReductionsStruct(ReductionsStruct[] reductionsStruct) {
    this.reductionsStruct = reductionsStruct;
  }

  /**
   *
   * @return ReductionsStruct[]
   */
  public ReductionsStruct[] getReductionsStruct() {
    return reductionsStruct;
  }

  /**
   *
   * @param reductionsStruct ReductionsStruct[]
   */
  public void setCreditCardsStruct(CreditCardStruct[] creditCardsStruct) {
    this.creditCardsStruct = creditCardsStruct;
  }

  /**
   *
   * @return ReductionsStruct[]
   */
  public CreditCardStruct[] getCreditCardsStruct() {
    return creditCardsStruct;
  }

  /**
   * This method is used to set closing store id
   * @param amt
   */
  public void setStore(String theStore) {
    if (theStore == null)
      return;
    store = theStore;
  }

  /**
   * This method is used to get closing store id
   */
  public String getStore() {
    return (store);
  }

  /**
   * This method is used to set Register
   * @param theRegister String
   */
  public void setRegister(String theRegister) {
    if (theRegister != null) {
      register = theRegister;
    }
  }

  /**
   * This method is used to get Register
   * @return String
   */
  public String getRegister() {
    return (register);
  }

  /**
   * This method is used to set closing operator name
   * @param theOper String
   */
  public void setOperator(String theOper) {
    if (theOper == null)
      return;
    oper = theOper;
  }

  /**
   * This method is used to get closing operator name
   * @return String
   */
  public String getOperator() {
    return (oper);
  }

  /**
   * This method is used to set EODDate
   * @param theDate String
   */
  public void setEODDate(String theDate) {
    if (theDate == null)
      return;
    eodDate = theDate;
  }

  /**
   * This method is used to get EODDate
   * @return String
   */
  public String getEODDate() {
    return (eodDate);
  }

  /**
   * This method is used to set layaway sales total amount
   * @param amt Currency
   */
  public void setLayawaySaleTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    layawaySaleTotal = amt;
  }

  /**
   * This method is used to get layaway sales total amount
   * @return Currency
   */
  public ArmCurrency getLayawaySaleTotal() {
    return (layawaySaleTotal);
  }

  /**
   * This method is used to set layaway payment total amount
   * @param amt Currency
   */
  public void setLayawayPaymentTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    layawayPaymentTotal = amt;
  }

  /**
   * This method is used to get layaway payment total amount
   * @return Currency
   */
  public ArmCurrency getLayawayPaymentTotal() {
    return (layawayPaymentTotal);
  }

  /**
   * This method is used to set gift certificate total amount that was issued
   * @param amt Currency
   */
  public void setGiftCertificateIssued(ArmCurrency amt) {
    if (amt == null)
      return;
    giftCertificateIssued = amt;
  }

  /**
   * This method is used to get gift certificate total amount that was issued
   * @return Currency
   */
  public ArmCurrency getGiftCertificateIssued() {
    return (giftCertificateIssued);
  }

  /**
   * This method is used to set gift certificate total amount that was
   * tendered(received) as payment
   * @param amt Currency
   */
  public void setGiftCertificateTendered(ArmCurrency amt) {
    if (amt == null)
      return;
    giftCertificateTendered = amt;
  }

  /**
   * This method is used to get gift certificate total amount that was
   * tendered(received) as payment
   * @return Currency
   */
  public ArmCurrency getGiftCertificateTendered() {
    return (giftCertificateTendered);
  }

  /**
   * This method is used to set gift certificate total amount that was
   * reported(counted) at end of day
   * @param amt Currency
   */
  public void setGiftCertificateReported(ArmCurrency amt) {
    if (amt == null)
      return;
    giftCertificateReported = amt;
  }

  /**
   * This method is used to get gift certificate total amount that was
   * reported(counted) at end of day
   * @return Currency
   */
  public ArmCurrency getGiftCertificateReported() {
    return (giftCertificateReported);
  }

  /**
   * This method is used to set credit memo total amount that was issued
   * @param amt Currency
   */
  public void setCreditMemoIssued(ArmCurrency amt) {
    if (amt == null)
      return;
    creditMemoIssued = amt;
  }

  /**
   * This method is used to get credit memo total amount that was issued
   * @return Currency
   */
  public ArmCurrency getCreditMemoIssued() {
    return (creditMemoIssued);
  }

  /**
   * This method is used to set credit memo total amount that was tendered
   * (received) as payment
   * @param amt Currency
   */
  public void setCreditMemoTendered(ArmCurrency amt) {
    if (amt == null)
      return;
    creditMemoTendered = amt;
  }

  /**
   * This method is used to get credit memo total amount that was tendered
   * (received) as payment
   * @return Currency
   */
  public ArmCurrency getCreditMemoTendered() {
    return (creditMemoTendered);
  }

  /**
   * This method is used to set credit memo total amount that was reported at
   * end of day
   * @param amt Currency
   */
  public void setCreditMemoReported(ArmCurrency amt) {
    if (amt == null)
      return;
    creditMemoReported = amt;
  }

  /**
   * This method is used to get credit memo total amount that was reported at
   * end of day
   * @return Currency
   */
  public ArmCurrency getCreditMemoReported() {
    return (creditMemoReported);
  }

  /**
   * This method is used to set storeValueCard total amount that was issued
   * @param amt Currency
   */
  public void setStoreValueCardIssued(ArmCurrency amt) {
    if (amt == null)
      return;
    storeValueCardIssued = amt;
  }

  /**
   * This method is used to get storeValueCard total amount that was issued
   * @return Currency
   */
  public ArmCurrency getStoreValueCardIssued() {
    return (storeValueCardIssued);
  }

  /**
   * This method is used to set storeValueCard total amount that was tendered
   * (received) as payment
   * @param amt Currency
   */
  public void setStoreValueCardTendered(ArmCurrency amt) {
    if (amt == null)
      return;
    storeValueCardTendered = amt;
  }

  /**
   * This method is used to get storeValueCard total amount that was tendered
   * (received) as payment
   * @return Currency
   */
  public ArmCurrency getStoreValueCardTendered() {
    return (storeValueCardTendered);
  }

  /**
   * This method is used to add account table
   * @param am AccountableMedia
   */
  public void addAccountableMedia(AccountableMedia am) {
    if (am != null) {
      accountableMedia.add(am);
    }
  }

  /**
   * This method is used to get cash total amount that was tendered(received)
   * formatted to match checks
   * @return AccountableMedia[]
   */
  public AccountableMedia[] getAccountableMedia() {
    return (AccountableMedia[])accountableMedia.toArray(new AccountableMedia[0]);
  }

  /**
   * This method is used to clear account table
   */
  public void clearAccountableMedia() {
    accountableMedia.clear();
  }

  /**
   * This method is used to deposit amount that the store is taking to bank
   * @param desc String
   * @param amt Currency
   */
  public void addBankDeposit(String desc, ArmCurrency amt) {
    if (desc == null || amt == null)
      return;
    bankDepositTotal.add(new BankDepositHolder(desc, amt));
  }

  /**
   * This method is used to get cash total amount that was reported at end
   * of day
   * @return BankDepositHolder[]
   */
  public BankDepositHolder[] getBankDeposits() {
    return (BankDepositHolder[])bankDepositTotal.toArray(new BankDepositHolder[0]);
  }

  /**
   * This method is used to set private card total amount that was
   * tendered(received)
   * @param amt Currency
   */
  public void setPrivateCardTendered(ArmCurrency amt) {
    if (amt == null)
      return;
    privateCardTendered = amt;
  }

  /**
   * This method is used to get private card total amount that was
   * tendered(received)
   * @return Currency
   */
  public ArmCurrency getPrivateCardTendered() {
    return (privateCardTendered);
  }

  /**
   * This method is used to set privatedcard total amount that was reported at
   *  end of day
   * @param amt
   */
  public void setPrivateCardReported(ArmCurrency amt) {
    if (amt == null)
      return;
    privateCardReported = amt;
  }

  /**
   * This method is used to get private card total amount that was reported
   * at end of day
   * @return Currency
   */
  public ArmCurrency getPrivateCardReported() {
    return (privateCardReported);
  }

  /**
   * This method is used to set mfr coupon total amount that was tendered
   * (received)
   * @param amt
   */
  public void setMfrCouponTendered(ArmCurrency amt) {
    if (amt == null)
      return;
    mfrCouponTendered = amt;
  }

  /**
   * This method is used to get mfr coupon total amount that was tendered
   * (received)
   * @return Currency
   */
  public ArmCurrency getMfrCouponTendered() {
    return (mfrCouponTendered);
  }

  /**
   * set mfr coupon total amount that was reported at end of day
   * @param amt
   */
  public void setMfrCouponReported(ArmCurrency amt) {
    if (amt == null)
      return;
    mfrCouponReported = amt;
  }

  /**
   * This method is used to get mfr coupon total amount that was reported at
   * end of day
   * @return
   */
  public ArmCurrency getMfrCouponReported() {
    return (mfrCouponReported);
  }

  /**
   * This method is used to set collection total amount
   * @param amt
   */
  public void setCollections(ArmCurrency amt) {
    if (amt == null)
      return;
    collectionTotal = amt;
  }

  /**
   * This method is used to get collection total amount
   * @return
   */
  public ArmCurrency getCollections() {
    return (collectionTotal);
  }

  /**
   * This method is used to set paidout total amount
   * @param amt
   */
  public void setPaidouts(ArmCurrency amt) {
    if (amt == null)
      return;
    paidoutTotal = amt;
  }

  /**
   * This method is used to get paidout total amount
   * @return
   */
  public ArmCurrency getPaidouts() {
    return (paidoutTotal);
  }

  public void setPaidIns(ArmCurrency amt) {
    if (amt == null)
      return;
    paidInsTotal = amt;
  }

  /**
   * This method is used to get paidIn total amount
   * @return
   */
  public ArmCurrency getPaidIns() {
    return (paidInsTotal);
  }
  /**
   * This method is used to set cashdrop total amount
   * @param amt
   */
  public void setCashdrops(ArmCurrency amt) {
    if (amt == null)
      return;
    cashDrops = amt;
  }

  /**
   * This method is used to get cashdrop total amount
   * @return
   */
  public ArmCurrency getCashdrops() {
    return (cashDrops);
  }

  /**
   * This method is used to set store expenses total amount
   * @param amt
   */
  public void setStoreExpenses(ArmCurrency amt) {
    if (amt == null)
      return;
    storeExpenses = amt;
  }

  /**
   * This method is used to get store expenses total amount
   * @return
   */
  public ArmCurrency getStoreExpenses() {
    return (storeExpenses);
  }

  /**
   * This method is used to set sale total amount
   * @param amt
   */
  public void setSaleTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    saleTotal = amt;
  }

  /**
   * This method is used to get sale total amount
   * @return
   */
  public ArmCurrency getSaleTotal() {
    return (saleTotal);
  }

  /**
   * This method is used to set sales tax total amount
   * @param amt
   */
  public void setSalesTax(ArmCurrency amt) {
    if (amt == null)
      return;
    salesTaxTotal = amt;
  }

  /**
   * This method is used to get sales tax total amount
   * @return
   */
  public ArmCurrency getSalesTax() {
    return (salesTaxTotal);
  }

  /**
   * This method is used to set regional tax total amount
   * @param amt
   */
  public void setRegionalTax(ArmCurrency amt) {
    if (amt == null)
      return;
    regionalTaxTotal = amt;
  }

  /**
   * This method is used to get sales tax total amount
   * @return
   */
  public ArmCurrency getRegionalTax() {
    return (regionalTaxTotal);
  }

  /**
   * This method is used to set return total amount
   * @param amt
   */
  public void setReturnTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    returnTotal = amt;
  }

  /**
   * This method is used to get return total amount
   * @return
   */
  public ArmCurrency getReturnTotal() {
    return (returnTotal);
  }

  /**
   * This method is used to set void total amount
   * @param amt
   */
  public void setVoidTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    voidTotal = amt;
  }

  /**
   * This method is used to get void total amount
   * @return
   */
  public ArmCurrency getVoidTotal() {
    return (voidTotal);
  }

  /**
   * This method is used to set redeemable buy back total amount
   * @param amt
   */
  public void setRedeemableBuyBackTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    redeemableBuyBackTotal = amt;
  }

  /**
   * This method is used to get redeemable buy back total amount
   * @return
   */
  public ArmCurrency getRedeemableBuyBackTotal() {
    return (redeemableBuyBackTotal);
  }

  /**
   * This method is used to set store's new drawer fund amount
   * @param amt store's new drawer fund amount
   */
  public void setDrawerFundTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    drawerFundTotal = amt;
  }

  /**
   * This method is used to get store's new drawer fund amount
   * @return
   */
  public ArmCurrency getDrawerFundTotal() {
    return (drawerFundTotal);
  }

  /**
   * This method is used to set store's SOD drawer fund amount
   * @param amt Currency
   */
  public void setDrawerFundInitial(ArmCurrency amt) {
    if (amt == null)
      return;
    initialDrawerFund = amt;
  }

  /**
   * This method is used to get store's SOD drawer fund amount
   *  @return Currency
   */
  public ArmCurrency getDrawerFundInitial() {
    return (initialDrawerFund);
  }

  /**
   * This method is used to set layaway Return To Stock total amount
   * @param amt
   */
  public void setLayawayRTSTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    layawayRTSTotal = amt;
  }

  /**
   * This method is used to get layaway Return To Stock total amount
   * @return
   */
  public ArmCurrency getLayawayRTSTotal() {
    return (layawayRTSTotal);
  }

  /**
   * This method is used to set fee total amount
   * @param amt
   */
  public void setFeeTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    feeTotal = amt;
  }

  /**
   * This method is used to get fee total amount
   * @return
   */
  public ArmCurrency getFeeTotal() {
    return (feeTotal);
  }

  /**
   * This method is used to set markdown total amount
   * @param amt
   */
  public void setMarkdownTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    markdownTotal = amt;
  }

  /**
   * This method is used to get markdown total amount
   * @return
   */
  public ArmCurrency getMarkdownTotal() {
    return (markdownTotal);
  }

  /**
   * This method is used to set reduction total amount (final reduction of
   * line items, usually best deal of markdown and discount)
   * @param amt
   */
  public void setReductionTotal(ArmCurrency amt) {
    if (amt == null)
      return;
    reductionTotal = amt;
  }

  /**
   * This method is used to get reduction total amount (final reduction of
   * line items, usually best deal of markdown and discount)
   * @return
   */
  public ArmCurrency getReductionTotal() {
    return (reductionTotal);
  }

  /**
   * This method is used to set discount total amount
   * @param amt
   */
  public void setDiscounts(ArmCurrency amt) {
    if (amt == null)
      return;
    discountTotal = amt;
  }

  /**
   * This method is used to get discount total amount
   * @return
   */
  public ArmCurrency getDiscounts() {
    return (discountTotal);
  }

  /**
   * This method is used to set redeemable amount
   * @param amt Currency
   */
  public void setCMSRedeemable(ArmCurrency amt) {
    if (amt == null)
      return;
    cmsRedeemableTendered = amt;
  }

  /**
   * This method is used to get redeemable amount
   * @return Currency
   */
  public ArmCurrency getCMSRedeemable() {
    return (cmsRedeemableTendered);
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
   * @param saleReturnTotal
   */
  public void setSaleReturnTotal(ArmCurrency saleReturnTotal) {
    this.saleReturnTotal = saleReturnTotal;
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
   * @param cmsTrialBalance
   */
  public void setTrialBalance(CMSTrialBalance cmsTrialBalance) {
    this.cmsTrialBalance = cmsTrialBalance;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSTrialBalance getTrialBalance() {
    return this.cmsTrialBalance;
  }

  public String getEODTxnNumber() {
    return this.txnNumber;
  }

  public void setEODTxnNumber(String txnNumber) {
    this.txnNumber = txnNumber;
  }


  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {}
}


/************************************************************************/
/**
 *
 * <p>Title: CurrencyArrayTypeComparator</p>
 *
 * <p>Description: This class is used to compare currency types of two
 * currency object </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
class CurrencyArrayTypeComparator implements java.util.Comparator {

  /**
   * put your documentation comment here
   * @param o1
   * @param o2
   * @return
   */
  public int compare(Object o1, Object o2) {
    CurrencyType c1 = ((ArmCurrency)o1).getCurrencyType();
    CurrencyType c2 = ((ArmCurrency)o2).getCurrencyType();
    return (c1.compareTo(c2));
  }
}
