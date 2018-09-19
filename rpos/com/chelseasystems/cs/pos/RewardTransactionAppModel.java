/*
 * put your module comment here
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


/**
 * put your documentation comment here
 */
public class RewardTransactionAppModel extends CMSNoSaleTransactionAppModel implements Serializable {
  //static final long serialVersionUID =
  private RewardTransaction rewardTransaction;
  private Customer customer = null;

  /**
   * Default constructor
   */
  public RewardTransactionAppModel() {
  }

  /**
   * Constructor
   * @param rewardTransaction RewardTransaction
   */
  public RewardTransactionAppModel(RewardTransaction rewardTransaction) {
    super(rewardTransaction);
    this.rewardTransaction = rewardTransaction;
    try {
      this.customer = rewardTransaction.getCustomer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is used to get the payment transaction type
   * @return PaymentTransaction
   */
  public PaymentTransaction getPaymentTransaction() {
    return rewardTransaction;
  }

  /**
   * This method is used to get the total payment amount
   * @return Currency
   */
  public ArmCurrency getTotalPaymentAmount() {
    return new ArmCurrency(rewardTransaction.getStore().getCurrencyType(), 0d);
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
   * This method is used to print the transaction receipt
   * @param theAppMgr IApplicationManager
   */
  public void printReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.RewardTransaction);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(rewardTransaction.getStore()
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
        , ReceiptBlueprintInventory.RewardTransaction);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(rewardTransaction.getStore()
        , (Customer)null);
    localeSetter.setLocale(receiptFactory);
    receiptFactory.reprint(theAppMgr);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getLoyaltyType() {
    return res.getString(rewardTransaction.getRewardCard().getLoyalty().getStoreType());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getLoyaltyPointsBalance() {
    double bal = rewardTransaction.getRewardCard().getLoyalty().getCurrBalance();
    double issuedPoints = rewardTransaction.getPoints();
    double curLoyaltyBalance = bal - issuedPoints;
    return Double.toString(curLoyaltyBalance);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Customer getCustomer() {
    return this.customer;
  }
}

