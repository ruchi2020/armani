/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.paidout;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.customer.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.rb.*;
import java.io.*;
import java.util.*;


/**
 * put your documentation comment here
 */
public class CMSPaidOutTransactionAppModel extends CMSPaymentTransactionAppModel implements
    Serializable {
  static final long serialVersionUID = -3594655892324763620L;
  private PaidOutTransaction paidOutTransaction;

  /**
   * put your documentation comment here
   */
  public CMSPaidOutTransactionAppModel() {
  }

  /**
   * put your documentation comment here
   * @param   PaidOutTransaction paidOutTransaction
   */
  public CMSPaidOutTransactionAppModel(PaidOutTransaction paidOutTransaction) {
    this.paidOutTransaction = paidOutTransaction;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ResourceBundleKey getReceiptLabel() {
    return (new ResourceBundleKey("PAID OUT"));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ViewLineItemAppModel[] getLineItemsAppModelArray() {
    ArrayList list = new ArrayList();
    PaidoutTransactionViewLines line = new PaidoutTransactionViewLines(paidOutTransaction.getType()
        , "", "", "", "", paidOutTransaction.getAmount().formattedStringValue());
    list.add(line);
    return (PaidoutTransactionViewLines[])list.toArray(new PaidoutTransactionViewLines[list.size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public PaymentTransaction getPaymentTransaction() {
    return (paidOutTransaction);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Employee getConsultant() {
    return (new CMSEmployee());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Customer getCustomer() {
    if (paidOutTransaction instanceof CMSMiscPaidOut
        && ((CMSMiscPaidOut)paidOutTransaction).getCustomer() != null) {
      return ((CMSMiscPaidOut)paidOutTransaction).getCustomer();
    } else {
      return super.getCustomer();
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSPaidOut);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(this.paidOutTransaction.getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
    if (getSignaturePaymentsArray().length > 0) {
      //  change to sigs in a minute...
      receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSPaidOut_Sigs);
      receiptFactory.print(theAppMgr);
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printCancelReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSPaidOut_Cancel);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(this.paidOutTransaction.getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printSuspendedReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSPaidOut_Suspend);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(this.paidOutTransaction.getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void rePrintReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    if (getSignaturePaymentsArray().length > 0) {
      if (theAppMgr.showOptionDlg("Store copy?", "Do you also want to print a signature copy?")) {
        System.out.println("printing the signature copy");
        ReceiptFactory receiptFactory = new ReceiptFactory(arguments
            , ReceiptBlueprintInventory.CMSPaidOut_Sigs);
        ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(this.paidOutTransaction.getStore()
            , getCustomer());
        localeSetter.setLocale(receiptFactory);
        receiptFactory.reprint(theAppMgr);
      }
    }
    System.out.println("printing the customer copy");
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSPaidOut);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(this.paidOutTransaction.getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.reprint(theAppMgr);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Enumeration getDiscounts() {
    return (null);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeNetAmount() {
    return (paidOutTransaction.getAmount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeRetailAmount() {
    return (paidOutTransaction.getAmount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeReductionAmount() {
    return (new ArmCurrency(0));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeTaxAmount() {
    return (new ArmCurrency(0));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeTotalAmountDue() {
    return (paidOutTransaction.getAmount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleNetAmount() {
    return (paidOutTransaction.getAmount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleReductionAmount() {
    return (new ArmCurrency(0));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleTaxAmount() {
    return (new ArmCurrency(0));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSaleTotalAmountDue() {
    return (paidOutTransaction.getAmount());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getType() {
    if (paidOutTransaction.getType().indexOf("CASH_TRANSFER") != -1) {
      return ("Cash Pickup");
    } else if (paidOutTransaction.getType().indexOf("MISC_PAID_OUT") != -1) {
      return ("Miscellaneous Paidout");
    } else {
      return (paidOutTransaction.getType());
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public POSLineItem[] getLineItemsArray() {
    return (new POSLineItem[0]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAuditString() {
    StringBuffer auditSB = new StringBuffer();
    if (paidOutTransaction.getComment().length() > 0)
      auditSB.append(paidOutTransaction.getComment() + " ");
    auditSB.append(super.getAuditString());
    return auditSB.toString();
  }

  public class PaidoutTransactionViewLines extends ViewLineItemAppModel {
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
    public PaidoutTransactionViewLines(String itemID, String itemDesc, String quantityString
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
      return (item);
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
      return (quantityString);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getItemRetailPrice() {
      return (retailPriceString);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getExtendedReductionAmount() {
      return (reductionString);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getExtendedNetAmount() {
      return (totalAmtDueString);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getType() {
      return (CMSPaidOutTransactionAppModel.this.paidOutTransaction.getTransactionType());
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getTaxAmount() {
      return ("");
    }
  } // end La
}

