/*
 * @copyright 1999-2002  Retek Inc.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 20   | 04-19-2006 | Sandhya   | 1411      |  View Transaction: Consignment & Consignment\Sale  |
 |      |            |           |           |  both are displaying consignment sale              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 19   | 10-06-2005 | Pankaja   | N/A       |  Restructing during the Merge Activity             |
 -+------+-----------+-----------+-----------+----------------------------------------------------+
 | 18   | 07-12-2005 |  Manpreet | N/A       | Added methods for ReservationLineItems and Txn.    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 17   | 06-15-2005 |  Manpreet | N/A       | Added methods to get/set fiscal document           |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 16   | 06-09-2005 | Megha     | N/A       | Added ASIS DATA set/get methods                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 15    | 05-03-2005 | Megha     |  N/A      | Added Loyalty Points.
 +-------------------------------------------------------------------------------------------------+
 | 14    | 05-05-2005|  Khyati   | N/A       |Added methods to set employeeId for Employee sale   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 13   | 04-29-2005 | Pankaja   | N/A       | Added new methods                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 12   | 04-22-2005 | Rajesh    | N/A       | Added new methods                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 04-17-2005 | Pankaja   | N/A       | Modify convertPresaleToSaleTxn/convertCsgToSaleTxn |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 04-16-2005 | Rajesh    | N/A       | added new method                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 04-14-2005 | Rajesh    | N/A       | changes in constructor                             |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-13-2005 | Rajesh    | N/A       | Specs Consignment impl/add required methods        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-12-2005 | Rajesh    | N/A       | Specs Presale impl                                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.pricing.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.pricing.*;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.currency.UnsupportedCurrencyTypeException;
import com.chelseasystems.cr.logging.LoggingInfo;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.customer.Customer;

import java.util.*;

import com.chelseasystems.cs.loyalty.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.rules.RuleEngine;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cs.pos.ASISTxnData;
import com.chelseasystems.cs.swing.builder.CMSPremioDiscountBldr;
import com.chelseasystems.cs.swing.returns.InitialReturnApplet;
import com.chelseasystems.cs.tax.CMSSaleTaxDetail;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cs.discount.ArmLineDiscount;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.customer.CMSCustomer;


/**
 *
 * <p>Title: CMSCompositePOSTransaction</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public class CMSCompositePOSTransaction extends CompositePOSTransaction implements com.
    chelseasystems.cr.rules.IRuleEngine {
  static final long serialVersionUID = -4210020863439077599L;
  public boolean isEmployeeSale;
  private PresaleTransaction prsTransaction = null;
  private ConsignmentTransaction csgTransaction = null;
  private ReservationTransaction resTransaction = null;
  private NonFiscalNoSaleTransaction nonFiscalNoSaleTransaction = null;
  private NonFiscalNoReturnTransaction nonFiscalNoReturnTransaction = null;
  private ReservationTransaction noRSVOpenTransaction = null;
  //ks: set employeeId for employee Sale
  private String employeeId = null;
  transient LoyaltyEngine loyaltyEngine = null;
  private Loyalty loyaltycard = null;
  private boolean truncatedPoints = false;
  private CMSEmployee employeeSale = null;
  private Vector fiscalDocuments = null;
  private FiscalDocument currFiscalDocument = null;
  private String sFiscalReceiptNumber = null;
  private Date dtFiscalReceipt = null;
  private CMSCompositePOSTransaction theNewRSVOTxn = null;
  private ASISTxnData asisTxnData = null;
  private int discountIndex = 0;
  protected boolean isPrintingReservationCloseReceipt = false;
  // Added by Satin for digital signature
  private String digitalSignature = null;
  //added by shushma for promotion
  private String promCode[];
  //Added by Rachana for approval of return transaction
  private String approverId;
  //Vivek Mishra : Added to capture Original PRESALE OPEN txn
  private  CompositePOSTransaction orgPRSOTxn = null;
  //Ends
  //Vivek Mishra : Added for Puerto Rico fiscal changes
  private String ivuProcessor = null;
  
private ArmCurrency stateTax = new ArmCurrency(0.0);
 private ArmCurrency citytax = new ArmCurrency(0.0);
  
  private Map taxMap;
  private Map taxExcMap;
  private ArmCurrency threshAmt;
  private String threhRule;


/**
   * Constructor
   * @param store Store
   */
  private double LoyaltyPoints = 0;

  private double redeemableAmount = 0;
  
  //added by shushma for duty free management PCR
  private CMSAirportDetails airportDetails;
  //Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16 
  private String vipMembershipID = null;
  private double vipDiscountPct = 0.0d;
  //Ends here
  // vishal : for customer_id prefix in europe for FRANCHISING store
  Boolean Franchising_Store = false;
  Boolean Staging_cust_id =false;
  //end vishal 14 sept 2016
  private CMSV12Basket cmsV12Basket;
/**
   * put your documentation comment here
   * @param   Store store
   */
  public CMSCompositePOSTransaction(Store store) {
    super(null, store);
    newPresaleTransaction();
    newConsignmentTransaction();
    newReservationTransaction();
    newNonFiscalNoSaleTransaction();
    newNonFiscalNoReturnTransaction();
    newNoReservationOpenTransaction();
    fiscalDocuments = new Vector();
  }

  /**
   * Constructor
   * @param anId String
   * @param store Store
   */
  public CMSCompositePOSTransaction(String anId, Store store) {
    super(anId, store);
    newPresaleTransaction();
    newConsignmentTransaction();
    newReservationTransaction();
    newNonFiscalNoSaleTransaction();
    newNonFiscalNoReturnTransaction();
    newNoReservationOpenTransaction();
    fiscalDocuments = new Vector();
  }

  
 // vishal : for customer_id prefix in europe for FRANCHISING store

  public CMSV12Basket getCmsV12Basket() {
	return cmsV12Basket;
}

public void setCmsV12Basket(CMSV12Basket cmsV12Basket) {
	this.cmsV12Basket = cmsV12Basket;
}

public Boolean getFranchising_Store() {
	return Franchising_Store;
}

public void setFranchising_Store(Boolean franchising_Store) {
	Franchising_Store = franchising_Store;
}

public Boolean getStaging_cust_id() {
	return Staging_cust_id;
}

public void setStaging_cust_id(Boolean staging_cust_id) {
	Staging_cust_id = staging_cust_id;
}

//end vishal 15 sept 2016
  /**
   * This method is used to post the composite pos transaction
   * @return boolean
   * @throws Exception
   */
  public boolean post()
      throws java.lang.Exception {
    return TransactionPOSServices.getCurrent().submit(this);
  }

  /**
   * This method is used to create the sale transaction
   * @return SaleTransaction
   */
  public SaleTransaction createSaleTransaction() {
    return new CMSSaleTransaction(this);
  }

  /**
   * This method is used to create shipping request
   * @param aCompositeTransaction CompositePOSTransaction
   * @return ShippingRequest
   */
  public ShippingRequest createShippingRequest(CompositePOSTransaction aCompositeTransaction) {
    return new CMSShippingRequest(aCompositeTransaction);
  }

  /**
   * This method is used to create return transaction
   * @return ReturnTransaction
   */
  public ReturnTransaction createReturnTransaction() {
    return new CMSReturnTransaction(this);
  }

  /**
   *
   * @return LayawayTransaction
   */
  public LayawayTransaction createLayawayTransaction() {
    return new CMSLayawayTransaction(this);
  }

  /**
   * This method is used to create pre sale transaction
   * @return PresaleTransaction
   */
  public PresaleTransaction createPresaleTransaction() {
    return new PresaleTransaction(this);
  }

  /**
   * This method is used to add pre sale transaction
   * @param aPresaleTransaction PresaleTransaction
   */
  public void doAddPresaleTransaction(PresaleTransaction aPresaleTransaction) {
    this.prsTransaction = aPresaleTransaction;
  }

  /**
   * This method is used to get pre sale transaction
   * @return PresaleTransaction
   */
  public PresaleTransaction getPresaleTransaction() {
    return this.prsTransaction;
  }

  /**
   * This method is used to get pre sale line item
   * @return Enumeration
   */
  public Enumeration getPresaleLineItems() {
    return prsTransaction.getLineItems();
  }

  /**
   * This method is used to get all line items of the pre sale transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getPresaleLineItemsArray() {
    return prsTransaction.getLineItemsArray();
  }

  /**
   * This method is used to get all deleted line items of the pre sale transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getPresaleDeletedLineItemsArray() {
    return prsTransaction.getDeletedLineItemsArray();
  }

  /**
   * This method is used to create consignment transaction
   * @return ConsignmentTransaction
   */
  public ConsignmentTransaction createConsignmentTransaction() {
    return new ConsignmentTransaction(this);
  }

  /**
   * This method is used to add consignment transaction
   * @param aConsignmentTransaction ConsignmentTransaction
   */
  public void doAddConsignmentTransaction(ConsignmentTransaction aConsignmentTransaction) {
    this.csgTransaction = aConsignmentTransaction;
  }

  /**
   * This method is used to get consignment transaction
   * @return ConsignmentTransaction
   */
  public ConsignmentTransaction getConsignmentTransaction() {
    return this.csgTransaction;
  }

  /**
   * This method is used to get line items of consignment transaction
   * @return Enumeration
   */
  public Enumeration getConsignmentLineItems() {
    return csgTransaction.getLineItems();
  }

  /**
   * This method is used to get all line items of consignment transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getConsignmentLineItemsArray() {
    return csgTransaction.getLineItemsArray();
  }

  /**
   * This method is used to get all deleted line items of consignment transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getConsignmentDeletedLineItemsArray() {
    return csgTransaction.getDeletedLineItemsArray();
  }

  /**
   * This method is used to get all line items groups of pre sale transaction
   * @return POSLineItemGrouping[]
   */
  public POSLineItemGrouping[] getPresaleLineItemGroupingsArray() {
    return prsTransaction.getLineItemGroupingsArray();
  }

  /**
   * This method is used to get all line items groups of consignment transaction
   * @return POSLineItemGrouping[]
   */
  public POSLineItemGrouping[] getConsignmentLineItemGroupingsArray() {
    return csgTransaction.getLineItemGroupingsArray();
  }

  /**
   * This method is used to get all line items groups of reservation transaction
   * @return POSLineItemGrouping[]
   */
  public POSLineItemGrouping[] getReservationLineItemGroupingsArray() {
    return resTransaction.getLineItemGroupingsArray();
  }

  public POSLineItem[] getVoidLineItemsArray() {
    return ((CMSReturnTransaction)getReturnTransaction()).getVoidLineItemsArray();
  }

  /**
   * This method is used to create reservation transaction
   * @return ReservationTransaction
   */
  public ReservationTransaction createReservationTransaction() {
    return new ReservationTransaction(this);
  }

  /**
   * This method is used to add reservation transaction
   * @param aRSVTransaction ReservationTransaction
   */
  public void doAddReservationTransaction(ReservationTransaction aRSVTransaction) {
    this.resTransaction = aRSVTransaction;
  }

  /**
   * This method is used to add no reservation transaction
   * @param aRSVTransaction ReservationTransaction
   */
  public void doAddNoReservationOpenTransaction(ReservationTransaction aRSVTransaction) {
    this.noRSVOpenTransaction = aRSVTransaction;
  }

  /**
   * This method is used to get reservation transaction
   * @return ReservationTransaction
   */
  public ReservationTransaction getNoReservationOpenTransaction() {
    return this.noRSVOpenTransaction;
  }

  /**
   * This method is used to get reservation transaction
   * @return ReservationTransaction
   */
  public ReservationTransaction getReservationTransaction() {
    return this.resTransaction;
  }

  /**
   * This method is used to get reservation line item
   * @return Enumeration
   */
  public Enumeration getReservationLineItems() {
    return resTransaction.getLineItems();
  }

  /**
   * This method is used to get all line items of the reservation transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getReservationLineItemsArray() {
    return resTransaction.getLineItemsArray();
  }

  /**
   * This method is used to get all deleted line items of the reservation transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getReservationDeletedLineItemsArray() {
    return resTransaction.getDeletedLineItemsArray();
  }

  /**
   * This method is used to create non fiscal no sale transaction
   * @return NonFiscalNoSaleTransaction
   */
  public NonFiscalNoSaleTransaction createNonFiscalNoSaleTransaction() {
    return new NonFiscalNoSaleTransaction(this);
  }

  /**
   * This method is used to add Non Fiscal No Sale transaction
   * @param anfnosTransaction NonFiscalNoSaleTransaction
   */
  public void doAddNonFiscalNoSaleTransaction(NonFiscalNoSaleTransaction anfnosTransaction) {
    this.nonFiscalNoSaleTransaction = anfnosTransaction;
  }

  /**
   * This method is used to get Non Fiscal No Sale transaction
   * @return NonFiscalNoSaleTransaction
   */
  public NonFiscalNoSaleTransaction getNonFiscalNoSaleTransaction() {
    return this.nonFiscalNoSaleTransaction;
  }

  /**
   * This method is used to get Non Fiscal No Sale line item
   * @return Enumeration
   */
  public Enumeration getNonFiscalNoSaleLineItems() {
    return nonFiscalNoSaleTransaction.getLineItems();
  }

  /**
   * This method is used to get all line items of theNon Fiscal No Sale transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getNonFiscalNoSaleLineItemsArray() {
    return nonFiscalNoSaleTransaction.getLineItemsArray();
  }

  /**
   * This method is used to get all deleted line items of the Non Fiscal No Sale transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getNonFiscalNoSaleDeletedLineItemsArray() {
    return nonFiscalNoSaleTransaction.getDeletedLineItemsArray();
  }

  /**
   * This method is used to create non fiscal no return transaction
   * @return NonFiscalNoSaleTransaction
   */
  public NonFiscalNoReturnTransaction createNonFiscalNoReturnTransaction() {
    return new NonFiscalNoReturnTransaction(this);
  }

  /**
   * This method is used to add Non Fiscal No return transaction
   * @param anfnosTransaction NonFiscalNoSaleTransaction
   */
  public void doAddNonFiscalNoReturnTransaction(NonFiscalNoReturnTransaction anfnorTransaction) {
    this.nonFiscalNoReturnTransaction = anfnorTransaction;
  }

  /**
   * This method is used to get Non Fiscal No return transaction
   * @return NonFiscalNoSaleTransaction
   */
  public NonFiscalNoReturnTransaction getNonFiscalNoReturnTransaction() {
    return this.nonFiscalNoReturnTransaction;
  }

  /**
   * This method is used to get Non Fiscal No return line item
   * @return Enumeration
   */
  public Enumeration getNonFiscalNoReturnLineItems() {
    return nonFiscalNoReturnTransaction.getLineItems();
  }

  /**
   * This method is used to get all line items of theNon Fiscal No Sale transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getNonFiscalNoReturnLineItemsArray() {
    return nonFiscalNoReturnTransaction.getLineItemsArray();
  }

  /**
   * This method is used to get all deleted line items of the Non Fiscal No Sale transaction
   * @return POSLineItem[]
   */
  public POSLineItem[] getNonFiscalNoReturnDeletedLineItemsArray() {
    return nonFiscalNoReturnTransaction.getDeletedLineItemsArray();
  }

  /**
   * This method is used to add pre sale item
   * @param anItem CMSItem
   * @return CMSPresaleLineItem
   * @throws BusinessRuleException
   */
  public CMSPresaleLineItem addPresaleItem(CMSItem anItem)
      throws BusinessRuleException {
    checkForNullParameter("addPresaleItem", anItem);
    executeRule("addPresaleItem", anItem);
    return (CMSPresaleLineItem)this.getPresaleTransaction().doAddItem(anItem);
  }

  /**
   * This method is used to add consignment item
   * @param anItem CMSItem
   * @return CMSConsignmentLineItem
   * @throws BusinessRuleException
   */
  public CMSConsignmentLineItem addConsignmentItem(CMSItem anItem)
      throws BusinessRuleException {
    checkForNullParameter("addConsignmentItem", anItem);
    executeRule("addConsignmentItem", anItem);
    return (CMSConsignmentLineItem)this.getConsignmentTransaction().doAddItem(anItem);
  }

  /**
   * This method is used to add reservation item
   * @param anItem CMSItem
   * @return CMSReservationLineItem
   * @throws BusinessRuleException
   */
  public CMSReservationLineItem addReservationItem(CMSItem anItem)
      throws BusinessRuleException {
    checkForNullParameter("addReservationItem", anItem);
    executeRule("addReservationItem", anItem);
    return (CMSReservationLineItem)this.getReservationTransaction().doAddItem(anItem);
  }

  /**
   * This method is used to add no sale line item
   * @param anItem CMSItem
   * @return CMSNoSaleLineItem
   * @throws BusinessRuleException
   */
  public CMSNoSaleLineItem addNoSaleItem(CMSItem anItem)
      throws BusinessRuleException {
    checkForNullParameter("addNoSaleItem", anItem);
    executeRule("addNoSaleItem", anItem);
    return (CMSNoSaleLineItem)this.getNonFiscalNoSaleTransaction().doAddItem(anItem);
  }

  /**
   * This method is used to add no sale line item
   * @param anItem CMSItem
   * @return CMSNoSaleLineItem
   * @throws BusinessRuleException
   */
  public CMSNoReturnLineItem addNoReturnItem(CMSItem anItem)
      throws BusinessRuleException {
    checkForNullParameter("addNoReturnItem", anItem);
    executeRule("addNoReturnItem", anItem);
    return (CMSNoReturnLineItem)this.getNonFiscalNoReturnTransaction().doAddItem(anItem);
  }

  /**
   * This method is used to add pre sale item as return
   * @param prsLnItm CMSPresaleLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addPresaleLineItemAsReturn(CMSPresaleLineItem prsLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addPresaleLineItemAsReturn", prsLnItm);
    executeRule("addPresaleLineItemAsReturn", prsLnItm);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addPresaleLineItem(prsLnItm, 1);
  }

  /**
   * This method is used to add all pre sale line item as return
   * @param prsLnItm CMSPresaleLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addEntirePresaleLineItemAsReturn(CMSPresaleLineItem prsLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntirePresaleLineItemAsReturn", prsLnItm);
    executeRule("addEntirePresaleLineItemAsReturn", prsLnItm);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addEntirePresaleLineItem(prsLnItm);
  }

  /**
   *
   * @param prsTxn PresaleTransaction
   * @throws BusinessRuleException
   */
  public void addEntirePresaleTransactionAsReturn(PresaleTransaction prsTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntirePresaleTransactionAsReturn", prsTxn);
    executeRule("addEntirePresaleTransactionAsReturn", prsTxn);
    ((CMSReturnTransaction)this.getReturnTransaction()).addEntirePresaleTransaction(prsTxn);
  }

  /**
   *
   * @param prsLnItm CMSPresaleLineItem
   * @return SaleLineItem
   * @throws BusinessRuleException
   */
  public SaleLineItem addPresaleLineItemAsSale(CMSPresaleLineItem prsLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addPresaleLineItemAsSale", prsLnItm);
    executeRule("addPresaleLineItemAsSale", prsLnItm);
    return ((CMSSaleTransaction)this.getSaleTransaction()).addPresaleLineItem(prsLnItm, 1);
  }

  /**
   *
   * @param prsLnItm CMSPresaleLineItem
   * @return SaleLineItem
   * @throws BusinessRuleException
   */
  public SaleLineItem addEntirePresaleLineItemAsSale(CMSPresaleLineItem prsLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntirePresaleLineItemAsSale", prsLnItm);
    executeRule("addEntirePresaleLineItemAsSale", prsLnItm);
    return ((CMSSaleTransaction)this.getSaleTransaction()).addEntirePresaleLineItem(prsLnItm);
  }

  /**
   *
   * @param prsTxn PresaleTransaction
   * @throws BusinessRuleException
   */
  public void addEntirePresaleTransactionAsSale(PresaleTransaction prsTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntirePresaleTransactionAsSale", prsTxn);
    executeRule("addEntirePresaleTransactionAsSale", prsTxn);
    ((CMSSaleTransaction)this.getSaleTransaction()).addEntirePresaleTransaction(prsTxn);
  }

  /**
   *
   * @param rsvLnItm CMSReservationLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addReservationLineItemAsVoid(CMSReservationLineItem rsvLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addReservationLineItemAsVoid", rsvLnItm);
    executeRule("addReservationLineItemAsVoid", rsvLnItm);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addReservationLineItemAsVoid(
        rsvLnItm, 1);
  }

  /**
   *
   * @throws BusinessRuleException
   */
  public void convertPresaleToSaleTransaction()
      throws BusinessRuleException {
    POSLineItem[] prsLnItmArr = this.getPresaleLineItemsArray();
    for (int i = 0; i < prsLnItmArr.length; i++) {
      SaleLineItem saleLnItm = this.addPresaleLineItemAsSale((CMSPresaleLineItem)prsLnItmArr[i]);
      prsLnItmArr[i].transferInformationTo(saleLnItm);
      ((CMSSaleLineItem)saleLnItm).cleanupLineItem();
      this.getPresaleTransaction().doRemoveLineItem(prsLnItmArr[i]);
    }
  }

  /**
   *
   * @throws BusinessRuleException
   */
  public void convertConsignmentToSaleTransaction()
      throws BusinessRuleException {
    POSLineItem[] csgLnItmArr = this.getConsignmentLineItemsArray();
    for (int i = 0; i < csgLnItmArr.length; i++) {
      SaleLineItem saleLnItm = this.addConsignmentLineItemAsSale((CMSConsignmentLineItem)
          csgLnItmArr[i]);
      csgLnItmArr[i].transferInformationTo(saleLnItm);
      ((CMSSaleLineItem)saleLnItm).cleanupLineItem();
      this.getConsignmentTransaction().doRemoveLineItem(csgLnItmArr[i]);
    }
  }

  /**
   *
   * @param csgLnItm CMSConsignmentLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addConsignmentLineItemAsReturn(CMSConsignmentLineItem csgLnItm)
      throws BusinessRuleException {
	checkForNullParameter("addConsignmentLineItemAsReturn", csgLnItm);
    executeRule("addConsignmentLineItemAsReturn", csgLnItm);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addConsignmentLineItem(csgLnItm, 1);
  }

  /**
   *
   * @param csgLnItm CMSConsignmentLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addEntireConsignmentLineItemAsReturn(CMSConsignmentLineItem csgLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntireConsignmentLineItemAsReturn", csgLnItm);
    executeRule("addEntireConsignmentLineItemAsReturn", csgLnItm);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addEntireConsignmentLineItem(
        csgLnItm);
  }

  /**
   *
   * @param csgTxn ConsignmentTransaction
   * @throws BusinessRuleException
   */
  public void addEntireConsignmentTransactionAsReturn(ConsignmentTransaction csgTxn)
      throws BusinessRuleException {
	checkForNullParameter("addEntireConsignmentTransactionAsReturn", csgTxn);
    executeRule("addEntireConsignmentTransactionAsReturn", csgTxn);
    ((CMSReturnTransaction)this.getReturnTransaction()).addEntireConsignmentTransaction(csgTxn);
  }

  /**
   *
   * @param csgLnItm CMSConsignmentLineItem
   * @return SaleLineItem
   * @throws BusinessRuleException
   */
  public SaleLineItem addConsignmentLineItemAsSale(CMSConsignmentLineItem csgLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addConsignmentLineItemAsSale", csgLnItm);
    executeRule("addConsignmentLineItemAsSale", csgLnItm);
    return ((CMSSaleTransaction)this.getSaleTransaction()).addConsignmentLineItem(csgLnItm, 1);
  }

  /**
   *
   * @param csgLnItm CMSConsignmentLineItem
   * @return SaleLineItem
   * @throws BusinessRuleException
   */
  public SaleLineItem addEntireConsignmentLineItemAsSale(CMSConsignmentLineItem csgLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntireConsignmentLineItemAsSale", csgLnItm);
    executeRule("addEntireConsignmentLineItemAsSale", csgLnItm);
    return ((CMSSaleTransaction)this.getSaleTransaction()).addEntireConsignmentLineItem(csgLnItm);
  }

  /**
   *
   * @param csgTxn ConsignmentTransaction
   * @throws BusinessRuleException
   */
  public void addEntireConsignmentTransactionAsSale(ConsignmentTransaction csgTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntireConsignmentTransactionAsSale", csgTxn);
    executeRule("addEntireConsignmentTransactionAsSale", csgTxn);
    ((CMSSaleTransaction)this.getSaleTransaction()).addEntireConsignmentTransaction(csgTxn);
  }

  /**
   * This method is used to add Reservation item as return
   * @param prsLnItm CMSReservationLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addReservationLineItemAsReturn(CMSReservationLineItem resLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addReservationLineItemAsReturn", resLnItm);
    executeRule("addReservationLineItemAsReturn", resLnItm);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addReservationLineItem(resLnItm, 1);
  }

  /**
   * This method is used to add all Reservation line item as return
   * @param prsLnItm CMSReservationLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addEntireReservationLineItemAsReturn(CMSReservationLineItem resLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntireReservationLineItemAsReturn", resLnItm);
    executeRule("addEntireReservationLineItemAsReturn", resLnItm);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addEntireReservationLineItem(
        resLnItm);
  }

  /**
   * This method is used to add No Reservation Open Txn line item as return
   * @param prsLnItm CMSReservationLineItem
   * @return ReturnLineItem
   * @throws BusinessRuleException
   */
  public ReturnLineItem addNoReservationLineItemAsReturn(CMSItem anItem)
      throws BusinessRuleException {
    checkForNullParameter("addNoReservationLineItemAsReturn", anItem);
    executeRule("addNoReservationLineItemAsReturn", anItem);
    return ((CMSReturnTransaction)this.getReturnTransaction()).addNoReservationLineItem(anItem);
  }
  /**
   *
   * @param resTxn ReservationTransaction
   * @throws BusinessRuleException
   */
  public void addEntireReservationTransactionAsReturn(ReservationTransaction resTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntireReservationTransactionAsReturn", resTxn);
    executeRule("addEntireReservationTransactionAsReturn", resTxn);
    ((CMSReturnTransaction)this.getReturnTransaction()).addEntireReservationTransaction(resTxn);
  }

  /**
   *
   * @param resLnItm CMSReservationLineItem
   * @return SaleLineItem
   * @throws BusinessRuleException
   */
  public SaleLineItem addReservationLineItemAsSale(CMSReservationLineItem resLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addReservationLineItemAsSale", resLnItm);
    executeRule("addReservationLineItemAsSale", resLnItm);
    return ((CMSSaleTransaction)this.getSaleTransaction()).addReservationLineItem(resLnItm, 1);
  }

  /**
   *
   * @param resLnItm CMSReservationLineItem
   * @return SaleLineItem
   * @throws BusinessRuleException
   */
  public SaleLineItem addNoReservationLineItemAsSale(CMSItem anItem)
      throws BusinessRuleException {
    checkForNullParameter("addNoReservationLineItemAsSale", anItem);
    executeRule("addNoReservationLineItemAsSale", anItem);
    return ((CMSSaleTransaction)this.getSaleTransaction()).addNoReservationLineItem(anItem);
  }

  /**
   *
   * @param resLnItm CMSReservationLineItem
   * @return SaleLineItem
   * @throws BusinessRuleException
   */
  public SaleLineItem addEntireReservationLineItemAsSale(CMSReservationLineItem resLnItm)
      throws BusinessRuleException {
    checkForNullParameter("addEntireReservationLineItemAsSale", resLnItm);
    executeRule("addEntireReservationLineItemAsSale", resLnItm);
    return ((CMSSaleTransaction)this.getSaleTransaction()).addEntireReservationLineItem(resLnItm);
  }

  /**
   *
   * @param resTxn ReservationTransaction
   * @throws BusinessRuleException
   */
  public void addEntireReservationTransactionAsSale(ReservationTransaction resTxn)
      throws BusinessRuleException {
    checkForNullParameter("addEntireReservationTransactionAsSale", resTxn);
    executeRule("addEntireReservationTransactionAsSale", resTxn);
    ((CMSSaleTransaction)this.getSaleTransaction()).addEntireReservationTransaction(resTxn);
  }

  /**
   *
   * @throws BusinessRuleException
   */
  public void convertReservationToSaleTransaction()
      throws BusinessRuleException {
    POSLineItem[] resLnItmArr = this.getReservationLineItemsArray();
    for (int i = 0; i < resLnItmArr.length; i++) {
      SaleLineItem saleLnItm = this.addReservationLineItemAsSale((CMSReservationLineItem)
          resLnItmArr[i]);
      resLnItmArr[i].transferInformationTo(saleLnItm);
      ((CMSSaleLineItem)saleLnItm).cleanupLineItem();
      this.getReservationTransaction().doRemoveLineItem(resLnItmArr[i]);
    }
  }

  public void convertSaleToSaleTransaction(CMSCompositePOSTransaction oldTxn)
  throws BusinessRuleException {
	Discount[] oldDiscounts = oldTxn.getDiscountsArray();
	for (int i = 0;  oldDiscounts!=null && i< oldDiscounts.length; i++) {
		addDiscount(oldDiscounts[i]);
	}
	  
	POSLineItem[] saleLnItmArr = oldTxn.getSaleLineItemsArray();
	for (int i = 0; i < saleLnItmArr.length; i++) {
	  SaleLineItem saleLnItm = this.addEntireSaleLineItemAsSale((CMSSaleLineItem)saleLnItmArr[i]);
	  saleLnItmArr[i].transferInformationTo(saleLnItm);
//	  ((CMSSaleLineItem)saleLnItm).cleanupLineItem();
//	  this.getSaleTransaction().doRemoveLineItem(saleLnItmArr[i]);
	}
	Discount[] oldSettlementDiscounts = oldTxn.getSettlementDiscountsArray();
	for (int i = 0;  oldSettlementDiscounts!=null && i< oldSettlementDiscounts.length; i++) {
		addSettlementDiscount(oldDiscounts[i]);
	}
	this.update();
  }

  public void convertSaleToNonFiscalTransaction(CMSCompositePOSTransaction oldTxn, int inc)
  throws BusinessRuleException {
		Discount[] oldDiscounts = oldTxn.getDiscountsArray();
		
		for (int i = 0;  oldDiscounts!=null && i< oldDiscounts.length; i++) {
			addDiscount(oldDiscounts[i]);
			((CMSDiscount)(oldDiscounts[i])).setSequenceNumber(((CMSDiscount)oldDiscounts[i]).
			getSequenceNumber() + (discountIndex));
		}
		if (oldDiscounts != null) {
			discountIndex = discountIndex + oldDiscounts.length;
		}
		POSLineItem[] saleLnItmArr = oldTxn.getSaleLineItemsArray();
		for (int i = 0; i < saleLnItmArr.length; i++) {
			SaleLineItem noSaleLnItm = this.addEntireSaleLineItemAsNonFiscal((CMSSaleLineItem)saleLnItmArr[i], 0);
			saleLnItmArr[i].transferInformationTo(noSaleLnItm);
		}

		Discount[] oldSettlementDiscounts = oldTxn.getSettlementDiscountsArray();
		for (int i = 0;  oldSettlementDiscounts!=null && i< oldSettlementDiscounts.length; i++) {
			addSettlementDiscount(oldDiscounts[i]);
		}
		this.update();
  	}
  /**
   * Override this method for presale type
   * @return String
   */
  public String getTransactionType() {
    StringBuffer buffy = new StringBuffer(20);
    POSLineItem[] saleLineItems;
    //Rachana added for consignment issue
    POSLineItem[] rtnLineItems;
    POSLineItemDetail[] rtnLineItemDetail;
    
    POSLineItemDetail[] saleLineItemDetail;
    CMSConsignmentLineItemDetail csgnLineItemDetail;
    CMSReservationLineItemDetail rsvLineItemDetail;
    if ((this.getNonFiscalNoSaleLineItemsArray().length != 0 || this.getNonFiscalNoReturnLineItemsArray().length != 0))
      buffy.append("NFNS");
    if (getPresaleLineItemsArray().length != 0)
      buffy.append("PRSO");
    if (getConsignmentLineItemsArray().length != 0)
    	buffy.append("CSGO");
    if (getReservationLineItemsArray().length != 0)
      buffy.append("RSVO");
    if (getSaleLineItemsArray().length != 0) {
            //Fix for issue 1411
            saleLineItems = this.getSaleLineItemsArray();
            for (int i = 0; i < saleLineItems.length; i++) {
                    saleLineItemDetail = saleLineItems[i].getLineItemDetailsArray();
                    //Fix for 1586: Presale:  If during a pre-sale open, you designate it
                    //as an employee sale, when you close it you get I/O errors and Broken Transaction.
                    if (saleLineItemDetail != null && saleLineItemDetail.length > 0) {
                    	csgnLineItemDetail = ((CMSSaleLineItemDetail) saleLineItemDetail[0]).getConsignmentLineItemDetail();
                    	rsvLineItemDetail = ((CMSSaleLineItemDetail) saleLineItemDetail[0]).getReservationLineItemDetail();
                    	if (csgnLineItemDetail != null) {
                    		buffy.append("CSGO");
                            break;
                    	} else if (rsvLineItemDetail != null) {
                            buffy.append("RSVO");
                            break;
                    	}
                    }
            }
      if (buffy.length() != 0) {
        buffy.append("/");
      }
      buffy.append("SALE");
    }
    //Edited by Rachana to get the correct transaction type in the transaction view. This was not happening when 
    //the customer was returning the items from consignment/reservation.
   /* if (getReturnLineItemsArray().length != 0 &&
                    (buffy.toString().indexOf("CSGO") == -1) &&
                    (buffy.toString().indexOf("RSVO") == -1)) {
            if (buffy.length() != 0) {
            	buffy.append("/");
            }
       buffy.append("RETN");
    }*/
    if(getReturnLineItemsArray().length != 0){
    	rtnLineItems = this.getReturnLineItemsArray();
        for (int i = 0; i < rtnLineItems.length; i++) {
            rtnLineItemDetail = rtnLineItems[i].getLineItemDetailsArray();
            if (rtnLineItemDetail != null && rtnLineItemDetail.length > 0) {
            	csgnLineItemDetail = ((CMSReturnLineItemDetail) rtnLineItemDetail[0]).getConsignmentLineItemDetail();
            	rsvLineItemDetail = ((CMSReturnLineItemDetail) rtnLineItemDetail[0]).getReservationLineItemDetail();
            	if (csgnLineItemDetail != null) {
            		buffy.append("CSGO");
                    break;
            	} else if (rsvLineItemDetail != null) {
                    buffy.append("RSVO");
                    break;
            	}
            }
        }
        if (buffy.length() != 0) {
            buffy.append("/");
        }
        buffy.append("RETN");
    }
    if (getLayawayLineItemsArray().length != 0) {
      if (buffy.length() != 0)
        buffy.append("/");
      buffy.append("LAWI");
    }
    if (getSaleLineItemsArray().length == 0 && getReturnLineItemsArray().length == 0
        && getLayawayLineItemsArray().length == 0 && getPresaleLineItemsArray().length == 0
        && getConsignmentLineItemsArray().length == 0 && getReservationLineItemsArray().length == 0
        && getNonFiscalNoSaleLineItemsArray().length == 0
        && getNonFiscalNoReturnLineItemsArray().length == 0)
      buffy.append("PEND");
    return buffy.toString();
  }


  /**
   *
   * @param aCompositeTransaction CompositePOSTransaction
   * @return PriceEngine
   */
  public PriceEngine createPriceEngine(CompositePOSTransaction aCompositeTransaction) {
    return new CMSPromotionBasedPriceEngine(aCompositeTransaction);
  }

  /**
   *
   * @param isEmployeeSale boolean
   */
  public void setEmployeeSale(boolean isEmployeeSale) {
    this.isEmployeeSale = isEmployeeSale;
  }

  /**
   *
   * @return boolean
   */
  public boolean getEmployeeSale() {
    return this.isEmployeeSale;
  }

  /**
   *
   */
  public void newPresaleTransaction() {
    PresaleTransaction aPresaleTransaction = createPresaleTransaction();
    doAddPresaleTransaction(aPresaleTransaction);
  }

  /**
   *
   */
  public void newConsignmentTransaction() {
    ConsignmentTransaction aConsignmentTransaction = createConsignmentTransaction();
    doAddConsignmentTransaction(aConsignmentTransaction);
  }

  /**
   *
   */
  public void newReservationTransaction() {
    ReservationTransaction aReservationTransaction = createReservationTransaction();
    doAddReservationTransaction(aReservationTransaction);
  }

  /**
   *
   */
  public void newNoReservationOpenTransaction() {
    ReservationTransaction aReservationTransaction = createReservationTransaction();
    doAddNoReservationOpenTransaction(aReservationTransaction);
  }

  /**
   *
   */
  public void newNonFiscalNoSaleTransaction() {
    NonFiscalNoSaleTransaction aNFNOSTransaction = this.createNonFiscalNoSaleTransaction();
    doAddNonFiscalNoSaleTransaction(aNFNOSTransaction);
  }

  /**
   *
   */
  public void newNonFiscalNoReturnTransaction() {
    NonFiscalNoReturnTransaction aNFNORTransaction = this.createNonFiscalNoReturnTransaction();
    doAddNonFiscalNoReturnTransaction(aNFNORTransaction);
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getPresaleNetAmount() {
    return this.getPresaleTransaction().getNetAmount();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getPresaleTotalAmountDue() {
//Anjana : Added in order to calculate actual Amount Due in case of PRESALE OPEN
	   POSLineItem[] posLineItems = this.getLineItemsArray();
	   ArmCurrency total  = null;
	   if (posLineItems != null && posLineItems.length > 0 && posLineItems[0] instanceof CMSPresaleLineItem){
		   
	   }
	   try
	        {
		   total   = new ArmCurrency(getBaseCurrencyType(), 0.0D);
	            for(int i=0; i<posLineItems.length;i++)
	            {
	                ArmCurrency aLineItemTotalAmountDue = ((POSLineItem)posLineItems[i]).getTotalAmountDue();
	                total = total.add(aLineItemTotalAmountDue);
	            }
	            return total;
	        }
	        catch(CurrencyException anException)
	        {
	            logCurrencyException("getTotalAmountDue", anException);
	        }
	        return null;
	   
	
    //return this.getPresaleTransaction().getTotalAmountDue();
  }
  
  //Anjana Added this method for calculating  presale composite total amount due
  public ArmCurrency getPresaleCompositeTotalAmountDue() {
	  ArmCurrency amt = null;
	  ArmCurrency amount = null;
	  try {
		 amt = getPresaleTotalAmountDue().subtract(getReturnTotalAmountDue()).add(getLayawayTotalAmountDue());
	} catch (CurrencyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  try {
		amount = amt.subtract(getCompositeVatAmount()).add(getPostAndPackChargeAmount());
	} catch (CurrencyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	    return amount;	  
//Ends
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getConsignmentNetAmount() {
    return this.getConsignmentTransaction().getNetAmount();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getConsignmentTotalAmountDue() {
    return this.getConsignmentTransaction().getTotalAmountDue();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getReservationNetAmount() {
    return this.getReservationTransaction().getNetAmount();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getReservationTotalAmountDue() {
    return this.getReservationTransaction().getTotalAmountDue();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getReturnNetAmount() {
    return ((CMSReturnTransaction)this.getReturnTransaction()).getNetAmount();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getNonFiscalNoSaleNetAmount() {
    return this.getNonFiscalNoSaleTransaction().getNetAmount();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getNonFiscalNoSaleTotalAmountDue() {
    return this.getNonFiscalNoSaleTransaction().getTotalAmountDue();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getNonFiscalNoReturnNetAmount() {
    return this.getNonFiscalNoReturnTransaction().getNetAmount();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getNonFiscalNoReturnTotalAmountDue() {
    return this.getNonFiscalNoReturnTransaction().getTotalAmountDue();
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getCompositeNetAmount() {
    if (!isPostAndPack())
      return getCompositeNetPreVatAmount();
    try {
      ArmCurrency amount = getCompositeNetPreVatAmount().subtract(getCompositeVatAmount()).add(
          getPostAndPackChargeAmount());
      return amount;
    } catch (CurrencyException e) {
      System.out.println(e);
      e.printStackTrace();
      LoggingServices.getCurrent().logMsg(new LoggingInfo("Currency Exception", e));
      throw new RuntimeException("currency exception");
    }
  }

  /**
   *
   * @return Currency
   */
  private ArmCurrency getCompositeNetPreVatAmount() {
    try {
      return getSaleNetAmount().subtract(getReturnNetAmount()).add(getLayawayNetAmount()).add(getPresaleNetAmount()).add(getConsignmentNetAmount()).
          add(getReservationNetAmount()).add(this.getNonFiscalNoSaleNetAmount()).subtract(this.getNonFiscalNoReturnNetAmount());
    } catch (CurrencyException anException) {
      logCurrencyException("getCompositeNetPreVatAmount", anException);
    }
    return null;
  }

  /**
   *
   * @return Currency
   */
  private ArmCurrency getCompositeTotalPreVatAmountDue() {
    try {
      return getSaleTotalAmountDue().subtract(getReturnTotalAmountDue()).add(getLayawayTotalAmountDue()).add(getPresaleTotalAmountDue()).add(
          getConsignmentTotalAmountDue()).add(getReservationTotalAmountDue()).add(this.getNonFiscalNoSaleTotalAmountDue()).subtract(this.getNonFiscalNoReturnTotalAmountDue());
    } catch (CurrencyException anException) {
      logCurrencyException("getCompositeTotalPreVatAmountDue", anException);
    }
    return null;
  }

  /**
   *
   * @param aMiscItem MiscItem
   * @return CMSPresaleLineItem
   * @throws BusinessRuleException
   */
  public CMSPresaleLineItem addPresaleMiscItem(MiscItem aMiscItem)
      throws BusinessRuleException {
    checkForNullParameter("addPresaleMiscItem", aMiscItem);
    executeRule("addPresaleMiscItem", aMiscItem);
    return (CMSPresaleLineItem)this.prsTransaction.doAddMiscItem(aMiscItem);
  }

  /**
   *
   * @param aSpecificItem SpecificItem
   * @return CMSPresaleLineItem
   * @throws BusinessRuleException
   */
  public CMSPresaleLineItem addPresaleSpecificItem(SpecificItem aSpecificItem)
      throws BusinessRuleException {
    checkForNullParameter("addPresaleSpecificItem", aSpecificItem);
    executeRule("addPresaleSpecificItem", aSpecificItem);
    return (CMSPresaleLineItem)prsTransaction.doAddSpecificItem(aSpecificItem);
  }

  /**
   *
   * @param aMiscItem MiscItem
   * @return CMSConsignmentLineItem
   * @throws BusinessRuleException
   */
  public CMSConsignmentLineItem addConsignmentMiscItem(MiscItem aMiscItem)
      throws BusinessRuleException {
    checkForNullParameter("addConsignmentMiscItem", aMiscItem);
    executeRule("addConsignmentMiscItem", aMiscItem);
    return (CMSConsignmentLineItem)this.csgTransaction.doAddMiscItem(aMiscItem);
  }

  /**
   *
   * @param aSpecificItem SpecificItem
   * @return CMSConsignmentLineItem
   * @throws BusinessRuleException
   */
  public CMSConsignmentLineItem addConsignmentSpecificItem(SpecificItem aSpecificItem)
      throws BusinessRuleException {
    checkForNullParameter("addConsignmentSpecificItem", aSpecificItem);
    executeRule("addConsignmentSpecificItem", aSpecificItem);
    return (CMSConsignmentLineItem)csgTransaction.doAddSpecificItem(aSpecificItem);
  }

  /**
   *
   * @param aMiscItem MiscItem
   * @return CMSConsignmentLineItem
   * @throws BusinessRuleException
   */
  public CMSReservationLineItem addReservationMiscItem(MiscItem aMiscItem)
      throws BusinessRuleException {
    checkForNullParameter("addReservationMiscItem", aMiscItem);
    executeRule("addReservationMiscItem", aMiscItem);
    return (CMSReservationLineItem)this.resTransaction.doAddMiscItem(aMiscItem);
  }

  /**
   *
   * @param aSpecificItem SpecificItem
   * @return CMSConsignmentLineItem
   * @throws BusinessRuleException
   */
  public CMSReservationLineItem addReservationSpecificItem(SpecificItem aSpecificItem)
      throws BusinessRuleException {
    checkForNullParameter("addReservationSpecificItem", aSpecificItem);
    executeRule("addReservationSpecificItem", aSpecificItem);
    return (CMSReservationLineItem)resTransaction.doAddSpecificItem(aSpecificItem);
  }

  /**
   *
   * @return POSLineItem[]
   */
  public POSLineItem[] getLineItemsArray() {
    POSLineItem saleLineItemsArray[] = getSaleLineItemsArray();
    POSLineItem returnLineItemsArray[] = getReturnLineItemsArray();
    POSLineItem layawayLineItemsArray[] = getLayawayLineItemsArray();
    POSLineItem presaleLineItemsArray[] = getPresaleLineItemsArray();
    POSLineItem consignmentLineItemsArray[] = getConsignmentLineItemsArray();
    POSLineItem reservationLineItemsArray[] = getReservationLineItemsArray();
    POSLineItem nonFiscalNoSaleLineItemsArray[] = getNonFiscalNoSaleLineItemsArray();
    POSLineItem nonFiscalNoReturnLineItemsArray[] = getNonFiscalNoReturnLineItemsArray();
    ArrayList aCompositeList = new ArrayList(((saleLineItemsArray.length
        + returnLineItemsArray.length + layawayLineItemsArray.length + presaleLineItemsArray.length
        + consignmentLineItemsArray.length + reservationLineItemsArray.length
        + nonFiscalNoSaleLineItemsArray.length + nonFiscalNoReturnLineItemsArray.length) * 110) / 100);
    aCompositeList.addAll(Arrays.asList(saleLineItemsArray));
    aCompositeList.addAll(Arrays.asList(returnLineItemsArray));
    aCompositeList.addAll(Arrays.asList(layawayLineItemsArray));
    aCompositeList.addAll(Arrays.asList(presaleLineItemsArray));
    aCompositeList.addAll(Arrays.asList(consignmentLineItemsArray));
    aCompositeList.addAll(Arrays.asList(reservationLineItemsArray));
    aCompositeList.addAll(Arrays.asList(nonFiscalNoSaleLineItemsArray));
    aCompositeList.addAll(Arrays.asList(nonFiscalNoReturnLineItemsArray));
    Collections.sort(aCompositeList);
    return (POSLineItem[])aCompositeList.toArray(new POSLineItem[0]);
  }

  /**
   *
   * @return POSLineItem[]
   */
  public POSLineItem[] getDeletedLineItemsArray() {
    ArrayList aCompositeList = new ArrayList();
    aCompositeList.addAll(Arrays.asList(getSaleDeletedLineItemsArray()));
    aCompositeList.addAll(Arrays.asList(getReturnDeletedLineItemsArray()));
    aCompositeList.addAll(Arrays.asList(getLayawayDeletedLineItemsArray()));
    aCompositeList.addAll(Arrays.asList(getPresaleDeletedLineItemsArray()));
    aCompositeList.addAll(Arrays.asList(getConsignmentDeletedLineItemsArray()));
    aCompositeList.addAll(Arrays.asList(getReservationDeletedLineItemsArray()));
    aCompositeList.addAll(Arrays.asList(getNonFiscalNoSaleDeletedLineItemsArray()));
    aCompositeList.addAll(Arrays.asList(getNonFiscalNoReturnDeletedLineItemsArray()));
    Collections.sort(aCompositeList);
    return (POSLineItem[])aCompositeList.toArray(new POSLineItem[0]);
  }

  /**
   *
   * @return ArrayList
   */
  public ArrayList getLineItemDetailsArrayList() {
    ArrayList aCompositeList = new ArrayList((getCompositeTotalQuantityOfItems() * 110) / 100);
    
    if(isPrintingReservationCloseReceipt && isReservationClose()){
        for (Enumeration aLineItemList = getSaleLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((SaleLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));        
        return aCompositeList;
    }
    
    for (Enumeration aLineItemList = getSaleLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((SaleLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));
    for (Enumeration aLineItemList = getReturnLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((ReturnLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));
    for (Enumeration aLineItemList = getLayawayLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((LayawayLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));
    for (Enumeration aLineItemList = getPresaleLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((CMSPresaleLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));
    for (Enumeration aLineItemList = getConsignmentLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((CMSConsignmentLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));
    for (Enumeration aLineItemList = getReservationLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((CMSReservationLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));
    for (Enumeration aLineItemList = getNonFiscalNoSaleLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((CMSNoSaleLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));
    for (Enumeration aLineItemList = getNonFiscalNoReturnLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((CMSNoReturnLineItem)aLineItemList.nextElement()).getLineItemDetailsArray())));

    return aCompositeList;
  }

  
  
  
  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItemDetail[] getLineItemDetailsArray() {
    return (POSLineItemDetail[])this.getLineItemDetailsArrayList().toArray(new POSLineItemDetail[0]);
  }

  /**
   *
   * @param aCustomer Customer
   * @throws BusinessRuleException
   */
  public void setCustomer(Customer aCustomer)
      throws BusinessRuleException {
    checkForNullParameter("setCustomer", aCustomer);
    executeRule("setCustomer", aCustomer);
    super.doSetCustomer(aCustomer);
    this.update();
  }

  /**
   *  setCustomer called after post transaction has happened
   * @param aCustomer Customer
   * @throws BusinessRuleException
   */
  public void setCustomerPostTransaction(Customer aCustomer)
      throws BusinessRuleException {
    checkForNullParameter("setCustomer", aCustomer);
    executeRule("setCustomer", aCustomer);
    super.doSetCustomer(aCustomer);
    this.updatePostTransaction();
  }

  /**
   *
   * @throws Exception
   */
  public void clearManualTax()
      throws Exception {
    POSLineItemDetail[] lineItemDetails = this.getLineItemDetailsArray();
    for (int i = 0; i < lineItemDetails.length; i++) {
      POSLineItemDetail posLineItemDetail = lineItemDetails[i];
      if (posLineItemDetail instanceof CMSSaleLineItemDetail) {
        posLineItemDetail.clearManualTaxAmount();
        posLineItemDetail.clearManualRegionalTaxAmount();
      }
    }
  }

  //ks: Set and get Method for Employee ID
  /**
   *
   * @return String
   */
  public String getEmployeeId() {
    return this.employeeId;
  }

  /**
   *
   * @param employeeId String
   */
  public void setEmployeeId(String employeeId) {
    doSetEmployeeId(employeeId);
    return;
  }

  /**
   *
   * @param employeeId String
   */
  public void doSetEmployeeId(String employeeId) {
    this.employeeId = employeeId;
    return;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSEmployee getEmployee() {
    return this.employeeSale;
  }

  /**
   *
   * @param employeeId String
   */
  public void setEmployee(CMSEmployee employee) {
    this.employeeSale = employee;
    return;
  }

  /**
   *
   * @return double
   */
  public double getLoyaltyPoints() {
	  if(isEvenExchange()){
		  return 0;
	  }
    return this.LoyaltyPoints;
  }

  /**
   *
   * @param LoyaltyPoints double
   */
  public void doSetLoyaltyPoints(double LoyaltyPoints) {
    this.LoyaltyPoints = LoyaltyPoints;
  }

  /**
   *
   * @param LoyaltyPoints double
   */
  public void setLoyaltyPoints(double LoyaltyPoints) {
    doSetLoyaltyPoints(LoyaltyPoints);
  }


  
  /**
  * Added by Satin for digital signature
  * @return String
  */
 public String getDigitalSignature() {
	 return this.digitalSignature;
 }

 /**
  * Added by Satin for digital signature
  * @param digitalSignature String
  */
 public void doSetDigitalSignature(String digitalSignature) {
	 this.digitalSignature = digitalSignature;
	 return;
 }

 /**
  * Added by Satin for digital signature
  * @param digitalSignature String
  */
 public void setDigitalSignature(String digitalSignature) {
   doSetDigitalSignature(digitalSignature);
   return;
 }
 
/**
   *
   */
  // over-riding the update method to invoke the loyalty engine
  public void update() {
    clearDiscountBalances();
    super.update();
    this.runLoyaltyEngine();
  }

  /**
   *
   */
  // update Transaction after posting has been done
  public void updatePostTransaction() {
    this.runLoyaltyEngine();
  }

  /**
   */
  private void runLoyaltyEngine() {
    //System.out.println("&&&&&&&&&&&&&& Run the loyalty engine here &&&&&&&&&&&&&&&&&&&&&");
    if (this.getLoyaltyCard() != null) {
      if (loyaltyEngine == null) {
        loyaltyEngine = createLoyaltyEngine();
      }
      if (LoyaltyEngine.isTruncatedPoints())
        truncatedPoints = true;
      assignLoyaltyPoints();
    } else
      setLoyaltyPoints(0.0);
  }

  /**
   *
   * @return LoyaltyEngine
   */
  public LoyaltyEngine createLoyaltyEngine() {
    return new LoyaltyEngine();
  }

  /**
   *
   */

public void assignLoyaltyPoints() {
    double saleloyaltyPts = 0.0;
    boolean returnTruncationRequired = false;
    double finalPoints = 0.0d;
    double returnPts = 0.0;
    POSLineItem[] saleItems = this.getSaleLineItemsArray();
    POSLineItem[] returnItems = this.getReturnLineItemsArray();
    for (int i = 0; i < saleItems.length; i++) {
    	if (saleItems[i] instanceof CMSSaleLineItem && this.getLoyaltyCard() != null) {
    		if (loyaltyEngine == null) {
    			loyaltyEngine = createLoyaltyEngine();
    		}
    		saleloyaltyPts = saleloyaltyPts
            	+ loyaltyEngine.calculateLoyaltyPoints((CMSSaleLineItem)saleItems[i]);
      	}
    }

    // Check if any of the return txns had a truncated points. If so, all the return txn points will
    // be truncated. As discussed with Gary on 06/28/2005, there will not be many cases where there
    // are multiple return txns and even so them having diff. truncation rules
    if (returnItems.length > 0) {
      for (int i = 0; i < returnItems.length; i++) {
        if (returnItems[i] instanceof CMSReturnLineItem) {
          SaleLineItem salItm = ((CMSReturnLineItem)returnItems[i]).getSaleLineItem();
          if (salItm != null) {
            CMSCompositePOSTransaction retTxn = (CMSCompositePOSTransaction)salItm.getTransaction().
                getCompositeTransaction();
            if (retTxn != null) {
              returnTruncationRequired = retTxn.isTruncatedPoints();
            }
          } else {
            if (this.getLoyaltyCard() != null) {
              if (loyaltyEngine == null) {
                loyaltyEngine = createLoyaltyEngine();
              }
              returnPts = returnPts
                  + loyaltyEngine.calculateLoyaltyPoints(returnItems[i]);
              //System.out.println("rtn poins " + returnPts);
     //         returnPts = Math.floor(returnPts);
//              if (LoyaltyEngine.isTruncatedPoints()) {
//                returnPts = java.lang.Math.floor(returnPts);
//              }

            }
          }
        }
      }
    }
    // Accumulate all the return points for this loyalty card ONLY !!!!!!
    for (int i = 0; i < returnItems.length; i++) {
      if (returnItems[i] instanceof CMSReturnLineItem) {
        if (((CMSReturnLineItemDetail)returnItems[i].getLineItemDetailsArray()[0]).
            getSaleLineItemDetail() != null) {
          CMSCompositePOSTransaction origTxn = (CMSCompositePOSTransaction)((
              CMSReturnLineItemDetail)returnItems[i].getLineItemDetailsArray()[0]).
              getSaleLineItemDetail().getLineItem().getTransaction().getCompositeTransaction();
          if (origTxn.getLoyaltyCard() != null
              && origTxn.getLoyaltyCard().getLoyaltyNumber().
              equals(this.getLoyaltyCard().getLoyaltyNumber())) {
            returnPts = returnPts + ((CMSReturnLineItem)returnItems[i]).getLoyaltyPointsToReturn();
           // System.out.println("rtn poins1 " + returnPts);
   //         returnPts = Math.floor(returnPts);
          }
        }
      }
    }

/*    double loyaltyRewardRatio = Double.parseDouble(CMSPremioDiscountBldr.loyaltyRewardRatio);
    double loyaltyAmt = Double.parseDouble(CMSPremioDiscountBldr.loyaltyAmount);*/

  /*  if(getUsedLoyaltyPoints()<0){

	    if(loyaltyRewardRatio>0 && loyaltyAmt>0){
	    	double returningPoints = returnPts/loyaltyRewardRatio;
	    	returningPoints = Math.ceil(returningPoints/loyaltyAmt);
	    	returningPoints = returningPoints * CMSPremioDiscountBldr.pointsMultiplier;
	    	returnPts = returningPoints;
	    }
    	returnPts = Math.floor(returnPts);
    }else{
    	returnPts = Math.floor(returnPts);
    }*/
    // Truncate if any of the return txn has truncated points
//    if (returnTruncationRequired) {
//      returnPts = java.lang.Math.floor(returnPts);
//    }
   // System.out.println("rtn Ponts " + returnPts);
   // System.out.println("sale Ponts " + saleloyaltyPts);
    finalPoints = saleloyaltyPts - returnPts;
    if (finalPoints > 0) {
//      finalPoints = java.lang.Math.floor(finalPoints);
      finalPoints = java.lang.Math.round(finalPoints *1000)/1000;
    } else if (finalPoints < 0) {
    	 if(java.lang.Math.floor(Math.abs(finalPoints))==0){
    		 finalPoints = 0;
    	 }else{
//        finalPoints = (java.lang.Math.floor(Math.abs(finalPoints))) * -1;
      	finalPoints = (Math.round(Math.abs(finalPoints) *1000)/1000) * -1;
    	 }
     }
    this.setLoyaltyPoints(finalPoints*CMSPremioDiscountBldr.currentPointRatio);
  }

  
  public double getReturnLoyaltyPoints(){
	  POSLineItem[] returnItems = this.getReturnLineItemsArray();
	  double returnPts = 0.0;
	    for (int i = 0; i < returnItems.length; i++) {
	        if (returnItems[i] instanceof CMSReturnLineItem) {
	          if (((CMSReturnLineItemDetail)returnItems[i].getLineItemDetailsArray()[0]).
	              getSaleLineItemDetail() != null) {
	            CMSCompositePOSTransaction origTxn = (CMSCompositePOSTransaction)((
	                CMSReturnLineItemDetail)returnItems[i].getLineItemDetailsArray()[0]).
	                getSaleLineItemDetail().getLineItem().getTransaction().getCompositeTransaction();
	            if (origTxn.getLoyaltyCard() != null
	                && origTxn.getLoyaltyCard().getLoyaltyNumber().
	                equals(this.getLoyaltyCard().getLoyaltyNumber())) {
	              returnPts = returnPts + ((CMSReturnLineItem)returnItems[i]).getLoyaltyPointsToReturn();
	              //System.out.println("rtn poins2 " + returnPts);
	            }
	          }
	        }
	    }
	    return returnPts;
  }
  
  public double getUsedLoyaltyPoints() {
    //  if(getRedeemablePoints()>0){
    double loyaltyAmount = Double.parseDouble(CMSPremioDiscountBldr.loyaltyAmount);
    if (loyaltyAmount > 0) {
    	double points = 0;
    	if(getRedeemableAmount()>0){
    		points = (Math.ceil(getRedeemableAmount() / loyaltyAmount))
            * CMSPremioDiscountBldr.pointsMultiplier;
    	}else{
    		points = (Math.floor(getRedeemableAmount() / loyaltyAmount))
            * CMSPremioDiscountBldr.pointsMultiplier;
    	}

      //System.out.println("getUsedLoyaltyPoints " + points);
      return points;
    }
    //  }


    return 0.0;
  }

  /**
   *
   * @param card Loyalty
   */
	  public void setLoyaltyCard(Loyalty card) {
		    doSetLoyaltyCard(card);
		    update();
		  }
	  public void doSetLoyaltyCard(Loyalty card) {
		    this.loyaltycard = card;
		  }

  /**
   *
   * @return Loyalty
   */
  public Loyalty getLoyaltyCard() {
    return this.loyaltycard;
  }

  /**
   *
   * @return boolean
   */
  public boolean isTruncatedPoints() {
    return truncatedPoints;
  }

  /**
   *
   * @param truncatedPoints boolean
   */
  public void setIsTruncatedPoints(boolean truncatedPoints) {
    this.truncatedPoints = truncatedPoints;
  }

  /**
   *
   * @return boolean
   */
  public boolean isConsignmentClose() {
    for (Enumeration em = this.getSaleLineItems(); em.hasMoreElements(); ) {
      if (((CMSSaleLineItem)em.nextElement()).getConsignmentLineItem() != null) {
        return true;
      }
    }
    for (Enumeration em = this.getReturnLineItems(); em.hasMoreElements(); ) {
      if (((CMSReturnLineItem)em.nextElement()).getConsignmentLineItem() != null) {
        return true;
      }
    }
    return false;
  }

  /**
   *
   * @return boolean
   */
  public boolean isPresaleClose() {
    for (Enumeration em = this.getSaleLineItems(); em.hasMoreElements(); ) {
      if (((CMSSaleLineItem)em.nextElement()).getPresaleLineItem() != null) {
        return true;
      }
    }
    for (Enumeration em = this.getReturnLineItems(); em.hasMoreElements(); ) {
      if (((CMSReturnLineItem)em.nextElement()).getPresaleLineItem() != null) {
        return true;
      }
    }
    return false;
  }

  /**
   *
   * @return boolean
   */
  public boolean isReservationClose() {
    for (Enumeration em = this.getSaleLineItems(); em.hasMoreElements(); ) {
      if (((CMSSaleLineItem)em.nextElement()).getReservationLineItem() != null) {
        return true;
      }
    }
    for (Enumeration em = this.getReturnLineItems(); em.hasMoreElements(); ) {
      if (((CMSReturnLineItem)em.nextElement()).getReservationLineItem() != null) {
        return true;
      }
    }
    return false;
  }

  public boolean isReservationOpen() {
    POSLineItem[] lineIms = getReservationLineItemsArray();
    if (lineIms != null && lineIms.length > 0) {
      return true;
    }
    return false;
  }

  /**
   *
   * @param anItem Item
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping getPresaleLineItemGroupingForItem(Item anItem) {
    return this.getPresaleTransaction().getLineItemGroupingForItem(anItem);
  }

  /**
   *
   * @param anItem Item
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping getConsignmentLineItemGroupingForItem(Item anItem) {
    return getConsignmentTransaction().getLineItemGroupingForItem(anItem);
  }

  /**
   *
   * @param anItem Item
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping getReservationLineItemGroupingForItem(Item anItem) {
    return getReservationTransaction().getLineItemGroupingForItem(anItem);
  }

  /**
   *
   * @param anItem Item
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping getNonFiscalNoSaleLineItemGroupingForItem(Item anItem) {
    return this.getNonFiscalNoSaleTransaction().getLineItemGroupingForItem(anItem);
  }

  /**
   *
   * @param anItem Item
   * @return POSLineItemGrouping
   */
  public POSLineItemGrouping getNonFiscalNoReturnLineItemGroupingForItem(Item anItem) {
    return this.getNonFiscalNoReturnTransaction().getLineItemGroupingForItem(anItem);
  }

  /**
   *
   * @param anItem Item
   * @return POSLineItemGrouping[]
   */
  public POSLineItemGrouping[] getLineItemGroupingForItem(Item anItem) {
    ArrayList list = new ArrayList();
    list.add(getSaleLineItemGroupingForItem(anItem));
    list.add(getReturnLineItemGroupingForItem(anItem));
    list.add(getLayawayLineItemGroupingForItem(anItem));
    list.add(getPresaleLineItemGroupingForItem(anItem));
    list.add(getConsignmentLineItemGroupingForItem(anItem));
    list.add(getReservationLineItemGroupingForItem(anItem));
    list.add(getNonFiscalNoSaleLineItemGroupingForItem(anItem));
    list.add(getNonFiscalNoReturnLineItemGroupingForItem(anItem));
    return (POSLineItemGrouping[])list.toArray(new POSLineItemGrouping[0]);
  }

  /**
   *
   * @return int
   */
  public int getTotalMerchandiseCount() {
    POSLineItemDetail[] details = this.getLineItemDetailsArray();
    if (details == null || details.length == 0) {
      return 0;
    }
    int total = 0;
    for (int i = 0; i < details.length; i++) {
    	
    	
    	
      if (!details[i].getLineItem().isMiscItem() && details[i].getGiftCertificateId() == null) {
        total++;
      }
      // Issue # 1162
      else if(details[i].getLineItem().isMiscItem() && LineItemPOSUtil.isNotOnFileItem(details[i].getLineItem().getItem().getId())){
        total++;
      }
    }
    

    
    return total;
  }

  /**
   *
   * @return Currency
   */
  public ArmCurrency getCompositeTotalDealMkdnAmount() {
    ArmCurrency totMkdwmAmt = new ArmCurrency(0.0);
    try {
      POSLineItem[] lines = this.getSaleLineItemsArray();
      for (int i = 0; i < lines.length; i++) {
        SaleLineItem saleItm = (SaleLineItem)lines[i];
        POSLineItemDetail[] dets = saleItm.getLineItemDetailsArray();
        for (int j = 0; j < dets.length; j++) {
          SaleLineItemDetail det = (SaleLineItemDetail)dets[j];
          ArmCurrency amt = det.getDealMarkdownAmount();
          totMkdwmAmt = totMkdwmAmt.add(amt);
        }
      }
    } catch (Exception e) {}
    return totMkdwmAmt;
  }

  /**
   *
   * @throws BusinessRuleException
   */
  public void checkIfValidForEmployeeSale()
      throws BusinessRuleException {
    RuleEngine.execute(this.getClass().getName(), "checkIfValidForEmployeeSale", this, new Object[0]);
  }

  /**
   *
   * @param theNewTxn CMSCompositePOSTransaction
   */
  public void attachNewReservationOpenTxn(CMSCompositePOSTransaction theNewTxn) {
    theNewRSVOTxn = theNewTxn;
  }

  /**
   *
   * @return CMSCompositePOSTransaction
   */
  public CMSCompositePOSTransaction getNewReservationOpenTxn() {
    return this.theNewRSVOTxn;
  }

  /**
   *
   * @return POSLineItem[]
   */
  public POSLineItem[] getDepositLineItems() {
    POSLineItem[] lineItems = this.getLineItemsArray();
    ArrayList list = new ArrayList();
    for (int index = 0; index < lineItems.length; index++) {
      if (((CMSItem)lineItems[index].getItem()).isDeposit()) {
        list.add(lineItems[index]);
      }
    }
    return (POSLineItem[])list.toArray(new POSLineItem[0]);
  }

  /**
   *
   * @param sValue String
   */
  public void setFiscalReceiptNumber(String sValue) {
    doSetFiscalReceiptNumber(sValue);
  }

  /**
   *
   * @param sValue String
   */
  public void doSetFiscalReceiptNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    sFiscalReceiptNumber = sValue;
  }

  /**
   *
   * @return String
   */
  public String getFiscalReceiptNumber() {
    return sFiscalReceiptNumber;
  }

  /**
   *
   * @param dtReciept Date
   */
  public void setFiscalReceiptDate(Date dtReciept) {
    doSetFiscalReceiptDate(dtReciept);
  }

  /**
   *
   * @param dtReciept Date
   */
  public void doSetFiscalReceiptDate(Date dtReciept) {
    if (dtReciept == null)
      return;
    dtFiscalReceipt = dtReciept;
  }

  /**
   *
   * @return Date
   */
  public Date getFiscalReceiptDate() {
    return dtFiscalReceipt;
  }

  /**
   *
   * @return Enumeration
   */
  public Enumeration getFiscalDocuments() {
    return fiscalDocuments.elements();
  }

  /**
   *
   * @return FiscalDocument[]
   */
  public FiscalDocument[] getFiscalDocumentArray() {
    return (FiscalDocument[])fiscalDocuments.toArray(new FiscalDocument[0]);
  }

  /**
   *
   * @param aFiscalDocument FiscalDocument
   */
  public void doAddFiscalDocument(FiscalDocument aFiscalDocument) {
    fiscalDocuments.addElement(aFiscalDocument);
  }

  /**
   *
   * @param aFiscalDocument FiscalDocument
   */
  public void doRemoveFiscalDocument(FiscalDocument aFiscalDocument) {
    fiscalDocuments.removeElement(aFiscalDocument);
  }

  /**
   *
   * @return boolean
   */
  public boolean hasFiscalDocuments() {
    return fiscalDocuments.size() != 0;
  }

  /**
   *
   * @param aFiscalDocument FiscalDocument
   */
  public void doSetCurrFiscalDocument(FiscalDocument aFiscalDocument) {
    this.currFiscalDocument = aFiscalDocument;
  }

  /**
   *
   * @param aFiscalDocument FiscalDocument
   */
  public void setCurrFiscalDocument(FiscalDocument aFiscalDocument) throws BusinessRuleException{
    executeRule("setCurrFiscalDocument", aFiscalDocument);
    doSetCurrFiscalDocument(aFiscalDocument);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public FiscalDocument getCurrFiscalDocument() {
    return this.currFiscalDocument;
  }

  /**
   * put your documentation comment here
   * @param asisData
   */
  public void doSetASISTxnData(ASISTxnData asisData) {
    this.asisTxnData = asisData;
  }

  /**
   *
   * @param ASIS
   */
  public void setASISTxnData(ASISTxnData asisData)
      throws BusinessRuleException {
    executeRule("setASISTxnData", asisData);
    doSetASISTxnData(asisData);
  }

  /**
   *
   * @return ASIS
   */
  public ASISTxnData getASISTxnData() {
    return this.asisTxnData;
  }

  /**
   * put your documentation comment here
   * @exception BusinessRuleException
   */
  public void clearManualUnitPrice()
      throws BusinessRuleException {
    POSLineItem[] lineItems = this.getLineItemsArray();
    for (int i = 0; i < lineItems.length; i++) {
      POSLineItem posLineItem = lineItems[i];
      if (posLineItem instanceof CMSSaleLineItem || (posLineItem instanceof CMSReturnLineItem && ((CMSReturnLineItem)posLineItem).isMiscReturn())
          || posLineItem instanceof CMSNoSaleLineItem || posLineItem instanceof CMSNoReturnLineItem) {
		  // Vishal Yevale  23 Oct 2017 -- Fixed fiscal issue : EUR -> NOFItem transaction -> createFiscal -> NOFItem price change to 1.00EUR
    	  if(Version.CURRENT_REGION.equalsIgnoreCase("EUR") && ((posLineItem.getMiscItemId()!=null && posLineItem.getMiscItemId().toUpperCase().contains("NOT")) || (posLineItem.doGetMiscItemDescription()!=null && posLineItem.doGetMiscItemDescription().toUpperCase().contains("NOT")))){
    		  if(posLineItem.getItem().getRetailPrice()!=null && posLineItem.getItemRetailPrice()!=null && (posLineItem.getItemRetailPrice().doubleValue()!=posLineItem.getItem().getRetailPrice().doubleValue()))
    		  continue;
    	  }
		  //end Vishal Yevale  23 Oct 2017
        posLineItem.clearManualUnitPrice();
      }
    }
    this.update();
  }

  public FiscalDocument newFiscalDocument() {
    return createFiscalDocument(this);
  }

  /**
   * This method is used to create shipping request
   * @param aCompositeTransaction CompositePOSTransaction
   * @return ShippingRequest
   */
  public FiscalDocument createFiscalDocument(CMSCompositePOSTransaction aCompositeTransaction) {
    return new FiscalDocument(aCompositeTransaction);
  }

  public POSLineItem[] getSaleAndReservationLineItemArray() {
    POSLineItem[] rsvLineItems = getReservationLineItemsArray();
    POSLineItem[] sLineItems = getSaleLineItemsArray();
    int nfnsLineItemsLength = rsvLineItems.length;
    int sLineItemsLength = sLineItems.length;

    ArrayList aCompositeList = new ArrayList(nfnsLineItemsLength + sLineItemsLength);
    aCompositeList.addAll(Arrays.asList(rsvLineItems));
    aCompositeList.addAll(Arrays.asList(sLineItems));

    return (POSLineItem[])aCompositeList.toArray(new POSLineItem[0]);
  }

  public POSLineItem[] getSaleAndNoSaleLineItemArray() {
    POSLineItem[] nfnsLineItems = getNonFiscalNoSaleLineItemsArray();
    POSLineItem[] nfnrLineItems = getNonFiscalNoReturnLineItemsArray();
    POSLineItem[] sLineItems = getSaleLineItemsArray();
    int nfnsLineItemsLength = nfnsLineItems.length;
    int nfnrLineItemsLength = nfnrLineItems.length;
    int sLineItemsLength = sLineItems.length;
    ArrayList aCompositeList = new ArrayList(nfnsLineItemsLength + sLineItemsLength + nfnrLineItemsLength);
    aCompositeList.addAll(Arrays.asList(nfnsLineItems));
    aCompositeList.addAll(Arrays.asList(nfnrLineItems));
    aCompositeList.addAll(Arrays.asList(sLineItems));
    return (POSLineItem[])aCompositeList.toArray(new POSLineItem[0]);
  }

  /**
   * @param aSaleLineItem
   * @return
   * @exception BusinessRuleException
   */
  public ReturnLineItem addEntireSaleLineItemAsExchange(SaleLineItem aSaleLineItem)
      throws BusinessRuleException {
    checkForNullParameter("addEntireSaleLineItemAsExchange", aSaleLineItem);
    executeRule("addEntireSaleLineItemAsExchange", aSaleLineItem);
    ReturnLineItem returnLineItem = this.getReturnTransaction().addEntireSaleLineItem(aSaleLineItem);
    if (returnLineItem instanceof CMSReturnLineItem){
      ((CMSReturnLineItem)returnLineItem).doSetIsForExchange(new Boolean(true));
    }
    return returnLineItem;
  }

  public void clearDiscountBalances() {
    Discount[] discounts = this.getDiscountsArray();
    for (int index=0; index < discounts.length; index++) {
      if (discounts[index] instanceof ArmLineDiscount) {
        ((ArmLineDiscount)discounts[index]).setTotalDiscountTakenOff(new ArmCurrency(getBaseCurrencyType(), 0.0d));
      }
    }
  }


// To return taxable presale line items
  public POSLineItemDetail[] getTaxableLineItemDetailsArray()
  {
      ArrayList aCompositeList = new ArrayList((getCompositeTotalQuantityOfItems() * 110) / 100);
      if(isTaxExempt())
          return (POSLineItemDetail[])aCompositeList.toArray(new POSLineItemDetail[0]);
      for(Enumeration aLineItemList = getSaleLineItems(); aLineItemList.hasMoreElements();)
      {
          SaleLineItem aSaleLineItem = (SaleLineItem)aLineItemList.nextElement();
          if(!aSaleLineItem.isTaxExempt() && aSaleLineItem.isItemTaxable())
              aCompositeList.addAll(Arrays.asList(aSaleLineItem.getLineItemDetailsArray()));
      }

      for(Enumeration aLineItemList = getReturnLineItems(); aLineItemList.hasMoreElements();)
      {
          ReturnLineItem aReturnLineItem = (ReturnLineItem)aLineItemList.nextElement();
          if(!aReturnLineItem.isTaxExempt() && aReturnLineItem.isMiscReturn() && aReturnLineItem.isItemTaxable())
              aCompositeList.addAll(Arrays.asList(aReturnLineItem.getLineItemDetailsArray()));
      }

      for(Enumeration aLineItemList = getLayawayLineItems(); aLineItemList.hasMoreElements();)
      {
          LayawayLineItem aLayawayLineItem = (LayawayLineItem)aLineItemList.nextElement();
          if(!aLayawayLineItem.isTaxExempt() && aLayawayLineItem.isItemTaxable())
              aCompositeList.addAll(Arrays.asList(aLayawayLineItem.getLineItemDetailsArray()));
      }

      for(Enumeration aLineItemList = this.getPresaleLineItems(); aLineItemList.hasMoreElements();)
      {
          CMSPresaleLineItem presaleLineItem = (CMSPresaleLineItem)aLineItemList.nextElement();
          if(!presaleLineItem.isTaxExempt() && presaleLineItem.isItemTaxable())
              aCompositeList.addAll(Arrays.asList(presaleLineItem.getLineItemDetailsArray()));
      }

      return (POSLineItemDetail[])aCompositeList.toArray(new POSLineItemDetail[0]);
  }

  public POSLineItemDetail[] getRegionalTaxableLineItemDetailsArray()
  {
      ArrayList aCompositeList = new ArrayList((getCompositeTotalQuantityOfItems() * 110) / 100);
      if(isRegionalTaxExempt())
          return (POSLineItemDetail[])aCompositeList.toArray(new POSLineItemDetail[0]);
      for(Enumeration aLineItemList = getSaleLineItems(); aLineItemList.hasMoreElements();)
      {
          SaleLineItem aSaleLineItem = (SaleLineItem)aLineItemList.nextElement();
          if(!aSaleLineItem.isRegionalTaxExempt() && aSaleLineItem.isItemRegionalTaxable())
              aCompositeList.addAll(Arrays.asList(aSaleLineItem.getLineItemDetailsArray()));
      }

      for(Enumeration aLineItemList = getReturnLineItems(); aLineItemList.hasMoreElements();)
      {
          ReturnLineItem aReturnLineItem = (ReturnLineItem)aLineItemList.nextElement();
          if(!aReturnLineItem.isRegionalTaxExempt() && aReturnLineItem.isMiscReturn() && aReturnLineItem.isItemRegionalTaxable())
              aCompositeList.addAll(Arrays.asList(aReturnLineItem.getLineItemDetailsArray()));
      }

      for(Enumeration aLineItemList = getLayawayLineItems(); aLineItemList.hasMoreElements();)
      {
          LayawayLineItem aLayawayLineItem = (LayawayLineItem)aLineItemList.nextElement();
          if(!aLayawayLineItem.isRegionalTaxExempt() && aLayawayLineItem.isItemRegionalTaxable())
              aCompositeList.addAll(Arrays.asList(aLayawayLineItem.getLineItemDetailsArray()));
      }

      for(Enumeration aLineItemList = getPresaleLineItems(); aLineItemList.hasMoreElements();)
      {
          CMSPresaleLineItem presaleLineItem = (CMSPresaleLineItem)aLineItemList.nextElement();
          if(!presaleLineItem.isRegionalTaxExempt() && presaleLineItem.isItemRegionalTaxable())
              aCompositeList.addAll(Arrays.asList(presaleLineItem.getLineItemDetailsArray()));
      }


      return (POSLineItemDetail[])aCompositeList.toArray(new POSLineItemDetail[0]);
  }

  public ArmCurrency getSaleTaxAmount()
  {
    POSLineItem[] posLineItems = this.getLineItemsArray();
    if (posLineItems != null && posLineItems.length > 0 && posLineItems[0] instanceof CMSPresaleLineItem){
      return getPresaleTransaction().getTaxAmount();
    } else
      return getSaleTransaction().getTaxAmount();
  }

  /**
  *
  * @return double
  */
 public double getRedeemableAmount() {
   return this.redeemableAmount;
 }

 public double getRedeemablePoints(){
	  return redeemableAmount*Double.parseDouble(CMSPremioDiscountBldr.loyaltyRewardRatio);
 }

 /**
  *
  * @param redeemablePoints double
  */
 public void setRedeemableAmount(double redeemableAmount) {
   this.redeemableAmount += redeemableAmount;
 }

 public void resetRedeemableAmount() {
   this.redeemableAmount = 0;
   this.assignLoyaltyPoints();
 }

 public ArmCurrency getNonMiscSaleAmountDue() {
   ArmCurrency total = new ArmCurrency(0);
   POSLineItem[] lineItems = getSaleLineItemsArray();
   try {
     for (int i = 0; i < lineItems.length; i++) {
       POSLineItem item = lineItems[i];
       if (item.isMiscItem()) {
         continue;
       }
       total = total.add(item.getExtendedRetailAmount()); // check with pankaja for the method
     }
   } catch (CurrencyException ex) {
     ex.printStackTrace();
   }
   return total;
 }

   public void addPayment(Payment aPayment) throws BusinessRuleException {
   super.addPayment(aPayment);
   if (aPayment instanceof CMSPremioDiscount) {
     this.assignLoyaltyPoints();
   }
 }

  public double getLoyaltyPointEarnRatio() {
   double usedPremioAmount = this.getUsedLoyaltyPoints() / LoyaltyEngine.rewdRedemptionRatio;
   if (usedPremioAmount > 0)
 //     return usedPremioAmount / this.getSaleTransaction().getNetAmount().doubleValue();
    	return usedPremioAmount
					/ ((CMSSaleTransaction)this.getSaleTransaction()).getNetAmountExcludingDiscountedItems()
							.doubleValue();
   return 0;
 }

 public String toString() {
   StringBuffer buffy = new StringBuffer();
   buffy.append(" Transaction : ");
   if (this.getEmployeeId() != null)
     buffy.append(" Employee = " + this.getEmployeeId());
   if (this.getCustomer() != null)
     buffy.append(" Customer = " + this.getCustomer().getId());
   if (this.getLoyaltyCard() != null)
     buffy.append(" Loyalty = "
         + this.getLoyaltyCard().getLoyaltyNumber());
   return buffy.toString();
 }

 public double getPremioDiscountToReturn(SaleLineItemDetail saleLineItemDetail) {
    double premioUsed = 0.0;
    Payment[] payments = getPaymentsArray();
    for (int j = 0; j < payments.length; j++) {
      Payment payment = payments[j];
      if (payment instanceof CMSPremioDiscount) {
        premioUsed = payment.getAmount().doubleValue();
        break;
      }
    }
    double totalPaid = getSaleTransaction().getNetAmount().doubleValue();

    ArmCurrency itemNetAmount = saleLineItemDetail.getNetAmount();

    if (totalPaid == 0)return 0;
    return (itemNetAmount.doubleValue()/(totalPaid))*(premioUsed);
  }
  
  public boolean isEvenExchange(){
		if (getReturnLineItemsArray().length > 0
				&& Math.abs(getReturnTotalAmountDue().doubleValue()) == Math.abs(getSaleTotalAmountDue()
						.doubleValue())
				//BUG 1688: Txns of ReservationClose, ConsignmentClose, PresaleClose are not evenExchange.
				&& !this.isReservationClose() && !this.isConsignmentClose() && !this.isPresaleClose()) {
		return true;
	}
	return false;
  }


 
  /**
   * @return
   */
  public ArmCurrency getCompositeTotalThresholdAmount() {
    try {
      return getSaleNetAmount().add(getLayawayOriginalNetAmount()).add(this.getNonFiscalNoSaleNetAmount());
    } catch (CurrencyException anException) {
      logCurrencyException("getCompositeTotalThresholdAmount", anException);
    }
    return null;
  }

  public void removeDiscount(Discount aDiscount)
      throws BusinessRuleException {
    checkForNullParameter("removeDiscount", aDiscount);
    executeRule("removeDiscount", aDiscount);
    doRemoveDiscount(aDiscount);
    if (aDiscount instanceof ArmLineDiscount) {
      POSLineItem[] lineItems = ((ArmLineDiscount)aDiscount).getLineItemsArray();
      for (int index=0; index < lineItems.length; index++) {
        lineItems[index].removeDiscount(aDiscount);
      }
    }
    update();
  }

  /**
   * @return
   */
  public ArrayList getSaleAndLayawayLineItemDetailsArrayList() {
    ArrayList aCompositeList = new ArrayList(((getSaleTotalQuantityOfItems() + getLayawayTotalQuantityOfItems()) * 110) / 100);
    for (Enumeration aLineItemList = getSaleLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((SaleLineItem)aLineItemList.nextElement()).getLineItemDetailsArray()))) {}
    for (Enumeration aLineItemList = getLayawayLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((LayawayLineItem)aLineItemList.nextElement()).getLineItemDetailsArray()))) {}
    for (Enumeration aLineItemList = this.getNonFiscalNoSaleLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((CMSNoSaleLineItem)aLineItemList.nextElement()).getLineItemDetailsArray()))) {}
/*    for (Enumeration aLineItemList = this.getNonFiscalNoReturnLineItems(); aLineItemList.hasMoreElements();
        aCompositeList.addAll(Arrays.asList(((CMSNoReturnLineItem)aLineItemList.nextElement()).getLineItemDetailsArray()))) {}*/

    return aCompositeList;
  }

  public SaleLineItem addEntireSaleLineItemAsSale(CMSSaleLineItem saleLineItem)
      throws BusinessRuleException {
	  checkForNullParameter("addEntireSaleLineItemAsSale", saleLineItem);
	  executeRule("addEntireConsignmentLineItemAsReturn", saleLineItem);
	  return ((CMSSaleTransaction)this.getSaleTransaction()).addEntireSaleLineItem(saleLineItem); 
  }

  public SaleLineItem addEntireSaleLineItemAsNonFiscal(CMSSaleLineItem saleLineItem, int inc)
      throws BusinessRuleException {
	  checkForNullParameter("addEntireSaleLineItemAsNonFiscal", saleLineItem);
	  executeRule("addEntireSaleLineItemAsNonFiscal", saleLineItem);
    return ((NonFiscalNoSaleTransaction)this.getNonFiscalNoSaleTransaction()).addEntireSaleLineItem(
        saleLineItem, inc);
  }
  public boolean isUpdateCustomerForTransactionAllowed()throws BusinessRuleException {
	  executeRule("isUpdateCustomerForTransactionAllowed",this);
	  return true;
  }
  
  public boolean validateEmployeeRuleForCurrentTransaction(CMSCustomer customer) throws BusinessRuleException {
	executeRule("validateEmployeeRuleForCurrentTransaction", customer);
	return true;
  }
  
  /**
  *  PCR 1864
  * @param  compTxn CMSCompositePOSTransaction
  * @throws BusinessRuleException
  */
 public boolean validateFiscalDocumentPresent()throws BusinessRuleException {
	 executeRule("validateFiscalDocumentPresent",this);
    return true;
 }  
  
  /**
  *
  * @param aDiscount Discount
  * @param lineItem POSLineItem
  * @throws BusinessRuleException
  */
 public boolean validateDiscountRule(Discount aDiscount, POSLineItem lineItem)
     throws BusinessRuleException {
   executeRule("validateDiscountRule", new Object[]{aDiscount, lineItem});
   return true;
 }  
 
 /**
 *
 * @param aDiscount Discount
 * @param lineItem POSLineItem
 * @throws BusinessRuleException
 */
	public boolean validateIsReturnAllowed(POSLineItem lineItem)
	    throws BusinessRuleException {
	  executeRule("validateIsReturnAllowed", lineItem);
	  return true;
	}
//Vivek Mishra : Merged updated code from source provided by Sergio 18-MAY-16	
public double getVipDiscountPct() {
		return vipDiscountPct;
	}
	/**
	 *@author vivek.sawant
	 *@param double
	 */
	public void setVipDiscountPct(double vipDiscountPct) {
		this.vipDiscountPct = vipDiscountPct;



	}
	/**
	 *@author vivek.sawant
	 *@return String
	 */
	public String getVipMembershipID() {
		return vipMembershipID;
	}
		public void setVipMembershipID(String vipMembershipID) {
		this.vipMembershipID = vipMembershipID;



	}
//Ends here
	public boolean isPrintingReservationCloseReceipt() {
		return isPrintingReservationCloseReceipt;
	}
	
	public void setPrintingReservationCloseReceipt(
			boolean isPrintingReservationCloseReceipt) {
		this.isPrintingReservationCloseReceipt = isPrintingReservationCloseReceipt;
	}
//added by shushma for promotion code
	public void setPromCode(String[] promCode) {
		// TODO Auto-generated method stub
		this.promCode=promCode;
	}
	public String[] getPromCode(){
		return promCode;
	}
	
	//added  by shushma for duty free management PCR
	 public CMSAirportDetails getAirportDetails() {
		return airportDetails;
	  }
	/*
			return airportDetails;
	 */
	 public void setAirportDetails(CMSAirportDetails airportDetails) {
			this.airportDetails = airportDetails;
	}
	 

	 //Added by Rachana for approval of return transaction
	 public String getApprover(){ return approverId; }
	 	
	 public void setApprover(String anApprover)
     throws BusinessRuleException
     {
     	checkForNullParameter("setConsultant", anApprover);
     	if(approverId == null || !approverId.equals(anApprover))
     	{
        	 executeRule("setConsultant", anApprover);
         	doSetApprover(anApprover);
     
    	 }
     }
	 
	 public void doSetApprover(String anApprover)
	 {
	     if(anApprover == null)
	     {
	    	 return;
	     } else
	     {
	    	 approverId = anApprover;
	         return;
	     }
	 }
//Vivek Mishra : Added to capture Original PRESALE OPEN txn
	public CompositePOSTransaction getOrgPRSOTxn() {
		return orgPRSOTxn;
	}

	public void setOrgPRSOTxn(CompositePOSTransaction orgPRSOTxn) {
		this.orgPRSOTxn = orgPRSOTxn;
	}
//Ends
	
	//Vivek Mishra : Added for Puerto Rico fiscal changes
	public String getIvuProcessor() {
		return ivuProcessor;
	}

	public void setIvuProcessor(String ivuProcessor) {
		this.ivuProcessor = ivuProcessor;
	}
	//Ends

	public ArmCurrency getStateTax() {
				return stateTax;
			}

			public void setStateTax(ArmCurrency stateTax) {
				this.stateTax = stateTax;
			}

			  public Map getTaxMap() {
					return taxMap;
				}

				public void setTaxMap(Map taxMap) {
					this.taxMap = taxMap;
				}

				public Map getTaxExcMap() {
					return taxExcMap;
				}

				public void setTaxExcMap(Map taxExcMap) {
					this.taxExcMap = taxExcMap;
				}

				public ArmCurrency getThreshAmt() {
					return threshAmt;
				}

				public void setThreshAmt(ArmCurrency threshAmt) {
					this.threshAmt = threshAmt;
				}

				public String getThrehRule() {
					return threhRule;
				}

				public void setThrehRule(String threhRule) {
					this.threhRule = threhRule;
				}
	
}

