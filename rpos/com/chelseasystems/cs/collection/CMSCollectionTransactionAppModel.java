/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 *
 * History:
 * 3552 mgs Added printing and reprinting of Credit Memo Issue
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.atd
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.collection;

import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.collection.*;
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
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cr.item.*;


/**
 * put your documentation comment here
 */
public class CMSCollectionTransactionAppModel extends CMSPaymentTransactionAppModel implements
    Serializable {
  static final long serialVersionUID = -794268666289874315L;
  CollectionTransaction collectionTransaction;
  java.util.ResourceBundle resBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
  /**
   * put your documentation comment here
   */
  public CMSCollectionTransactionAppModel() {
  }

  /**
   * put your documentation comment here
   * @param   CollectionTransaction collectionTransaction
   */
  public CMSCollectionTransactionAppModel(CollectionTransaction collectionTransaction) {
    this.collectionTransaction = collectionTransaction;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ViewLineItemAppModel[] getLineItemsAppModelArray() {
    ArrayList list = new ArrayList();
    CollectionTransactionViewLines line = new CollectionTransactionViewLines(collectionTransaction.
        getType(), "", "", "", "", collectionTransaction.getAmount().formattedStringValue());
    list.add(line);
    return (CollectionTransactionViewLines[])list.toArray(new CollectionTransactionViewLines[list.
        size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public PaymentTransaction getPaymentTransaction() {
    return collectionTransaction;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Employee getConsultant() {
    return collectionTransaction.getTheOperator();
    // return  new CMSEmployee();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Customer getCustomer() {
    //return  new CMSCustomer();
    if (collectionTransaction instanceof CMSMiscCollection
        && ((CMSMiscCollection)collectionTransaction).getCustomer() != null) {
      return ((CMSMiscCollection)collectionTransaction).getCustomer();
    } else {
      return super.getCustomer();
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printCancelReceipt(IApplicationManager theAppMgr) {
    // implement call to cancel receipt for ej here if desired
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printSuspendedReceipt(IApplicationManager theAppMgr) {
    // implement call to suspend receipt if desired
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSCollection);
    System.out.println("calling receiptFactory.print");
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(collectionTransaction.getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
    if (getSignaturePaymentsArray().length > 0) {
      receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSCollection_Sigs);
      localeSetter.setLocale(receiptFactory);
      receiptFactory.print(theAppMgr);
    }
    // to do
    ////////////////////////////////////////////////////////////////////
    //    CR # 3552
    //    Added if block
    ////////////////////////////////////////////////////////////////////
    if (getDueBillIssuePaymentsArray().length > 0) {
      receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSDueBillIssue);
      localeSetter.setLocale(receiptFactory);
      receiptFactory.print(theAppMgr);
    }
    // end to do
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void rePrintReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    if (getSignaturePaymentsArray().length > 0) {
      if (theAppMgr.showOptionDlg(resBundle.getString("Store copy?"), resBundle.getString("Do you also want to print a signature copy?"))) {
        ReceiptFactory receiptFactory = new ReceiptFactory(arguments
            , ReceiptBlueprintInventory.CMSCollection_Sigs);
        ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(collectionTransaction.getStore()
            , getCustomer());
        localeSetter.setLocale(receiptFactory);
        receiptFactory.reprint(theAppMgr);
      }
    }
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSCollection);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(collectionTransaction.getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.reprint(theAppMgr);
    ////////////////////////////////////////////////////////////////////
    //    CR # 3552
    //    Added if block
    ////////////////////////////////////////////////////////////////////
    if (getDueBillIssuePaymentsArray().length > 0) {
      receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSDueBillIssue);
      localeSetter.setLocale(receiptFactory);
      receiptFactory.reprint(theAppMgr);
    }
    // end to do
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Enumeration getDiscounts() {
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeNetAmount() {
    return collectionTransaction.getAmount();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeReductionAmount() {
    return new ArmCurrency(0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeTaxAmount() {
    return new ArmCurrency(0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeTotalAmountDue() {
    return collectionTransaction.getAmount();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleNetAmount() {
    return collectionTransaction.getAmount();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeRetailAmount() {
    return collectionTransaction.getAmount();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleReductionAmount() {
    return new ArmCurrency(0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleTaxAmount() {
    return new ArmCurrency(0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleTotalAmountDue() {
    return collectionTransaction.getAmount();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getLineItemsArray() {
    return new POSLineItem[0];
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAuditString() {
    StringBuffer auditSB = new StringBuffer();
    if (collectionTransaction.getComment().length() > 0) {
      auditSB.append(collectionTransaction.getComment() + " ");
    }
    auditSB.append(super.getAuditString());
    return auditSB.toString();
  }

  public class CollectionTransactionViewLines extends ViewLineItemAppModel {
    String itemID;
    String itemDesc;
    String quantityString;
    String retailPriceString;
    String reductionString;
    String totalAmtDueString;

    /**
     * put your documentation comment here
     * @param     String itemID
     * @param     String itemDesc
     * @param     String quantityString
     * @param     String retailPriceString
     * @param     String reductionString
     * @param     String totalAmtDueString
     */
    public CollectionTransactionViewLines(String itemID, String itemDesc, String quantityString
        , String retailPriceString, String reductionString, String totalAmtDueString) {
      this.itemID = itemID;
      this.itemDesc = itemDesc;
      this.quantityString = quantityString;
      this.retailPriceString = retailPriceString;
      this.reductionString = reductionString;
      this.totalAmtDueString = totalAmtDueString;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Item getItem() {
      CMSItem item = new CMSItem(itemID);
      item.doSetDescription(itemDesc);
      return item;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getItemDescription() {
      return itemDesc;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getQuantity() {
      return quantityString;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getItemRetailPrice() {
      return retailPriceString;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getExtendedReductionAmount() {
      return reductionString;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getExtendedNetAmount() {
      return totalAmtDueString;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getType() {
      return CMSCollectionTransactionAppModel.this.collectionTransaction.getTransactionType();
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getTaxAmount() {
      return "";
    }
  } // end La
}
