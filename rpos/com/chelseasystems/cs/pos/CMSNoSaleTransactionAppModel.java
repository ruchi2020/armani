/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-20-2005 | Mukesh    | 900       |Added one new methods getReason, which        |
 |      |            |           |           | return the no sale reason.                   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 09-07-2005 | Manpreet  |           | Reading NoSale reasons from ArmaniCommon.cfg |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.employee.EmployeeAppModel;
import com.chelseasystems.cr.customer.CustomerAppModel;
import java.io.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import java.util.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.util.ArmConfigLoader;


/**
 * <p>Title: CMSNoSaleTransactionAppModel</p>
 * <p>Description: This class stores the no sale transaction attributes </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author
 * @version 1.0
 */
public class CMSNoSaleTransactionAppModel extends CMSPaymentTransactionAppModel implements
    Serializable {
  //static final long serialVersionUID =
  private NoSaleTransaction noSaleTransaction;


  /**
   * Default constructor
   */
  public CMSNoSaleTransactionAppModel() {
  }


  /**
   * Constructor
   * @param noSaleTransaction NoSaleTransaction
   */
  public CMSNoSaleTransactionAppModel(NoSaleTransaction noSaleTransaction) {
    this.noSaleTransaction = noSaleTransaction;
  }


  /**
   * This method is used to get the payment transaction type
   * @return PaymentTransaction
   */
  public PaymentTransaction getPaymentTransaction() {
    return noSaleTransaction;
  }


  /**
   * This method is used to get the total payment amount
   * @return Currency
   */
  public ArmCurrency getTotalPaymentAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   *
   * @return ViewLineItemAppModel[]
   */
  public ViewLineItemAppModel[] getLineItemsAppModelArray() {
    return new ViewLineItemAppModel[0];
  }


  /**
   * This method is used to get the consultant
   * @return Employee
   */
  public Employee getConsultant() {
    return null;
  }


  /**
   * This method is used to get the customer
   * @return Customer
   */
  public Customer getCustomer() {
    return null;
  }


  /**
   * Newly added method, this method used to get value of No sale transaction reason
   * @return String
   */
  public String getReason() {
    return noSaleTransaction.getComment();
  }


  /**
   * put your documentation comment here
   * @return
   */
  public String getReasonDescription() {
    String desc = null;
    String reasonCode = noSaleTransaction.getComment();
    // 09/07/05  -- Read reason codes from ArmaniCommon.cfg
    //ConfigMgr cfg = new ConfigMgr("no_sale.cfg");
//    ConfigMgr cfg = new ConfigMgr("ArmaniCommon.cfg");
    ArmConfigLoader cfg = new ArmConfigLoader();
    // StringTokenizer reasons = new StringTokenizer(cfg.getString("NO_SALE_REASON_CODES"), ",");
    StringTokenizer reasons = new StringTokenizer(cfg.getString("NO_SL_REASON_CD"), ",");
    while (reasons.hasMoreTokens()) {
      String token = reasons.nextToken();
      if (cfg.getString(token + ".CODE").equals(reasonCode)) {
        // Description is already locale specific
        // in ArmaniCommon.cfg
        // desc = res.getString(cfg.getString(token + ".LABEL"));
        desc = cfg.getString(token + ".LABEL");
      }
    }
    if (desc != null)
      return desc;
    else
      return reasonCode;
  }


  /**
   * This method is used to print the transaction receipt
   * @param theAppMgr IApplicationManager
   */
  public void printReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSNoSaleTransaction);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(noSaleTransaction.getStore()
        , (Customer)null);
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
  }


  /**
   * This method is used to print the cancel transaction receipt
   * @param theAppMgr IApplicationManager
   */
  public void printCancelReceipt(IApplicationManager theAppMgr) {
    // I don't think this applies here, but if it does just implement it by printing the CMSNoSaleTransaction_Cancel receipt
  }


  /**
   * This method is used to print the suspended transaction receipt
   * @param theAppMgr IApplicationManager
   */
  public void printSuspendedReceipt(IApplicationManager theAppMgr) {
    // I don't think this applies here, but if it does just implement it by printing the CMSNoSaleTransaction_Suspended receipt
  }


  /**
   * This method is used to re print the transaction receipt
   * @param theAppMgr IApplicationManager
   */
  public void rePrintReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSNoSaleTransaction);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(noSaleTransaction.getStore()
        , (Customer)null);
    localeSetter.setLocale(receiptFactory);
    receiptFactory.reprint(theAppMgr);
  }


  /**
   * This method is used to get the discounts
   * @return Enumeration
   */
  public Enumeration getDiscounts() {
    return null;
  }


  /**
   * This method is used to print the transaction receipt
   * @return Currency
   */
  public ArmCurrency getCompositeNetAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get composite retail amount
   * @return Currency
   */
  public ArmCurrency getCompositeRetailAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get composite reduction amount
   * @return Currency
   */
  public ArmCurrency getCompositeReductionAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get composite tax amount
   * @return Currency
   */
  public ArmCurrency getCompositeTaxAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get composite total amount due
   * @return Currency
   */
  public ArmCurrency getCompositeTotalAmountDue() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get sales net amount
   * @return Currency
   */
  public ArmCurrency getSaleNetAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get sales reduction amount
   * @return Currency
   */
  public ArmCurrency getSaleReductionAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get sales tax amount
   * @return Currency
   */
  public ArmCurrency getSaleTaxAmount() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get sales total amount
   * @return Currency
   */
  public ArmCurrency getSaleTotalAmountDue() {
    return new ArmCurrency(noSaleTransaction.getStore().getCurrencyType(), 0d);
  }


  /**
   * This method is used to get line items
   * @return POSLineItem[]
   */
  public POSLineItem[] getLineItemsArray() {
    return new POSLineItem[0];
  }


  /**
   *
   * @return String
   */
  public String getAuditString() {
    return noSaleTransaction.getComment();
  }
}
