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
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06-16-2005 | Pankaja   | 189       | Resolution to the defectID 189                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

package com.chelseasystems.cs.pos;

import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.paidout.*;
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
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cs.collection.CMSCollectionTransactionAppModel;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.util.*;

/**
 * put your documentation comment here
 */
public class CMSRedeemableBuyBackTransactionAppModel extends CMSPaymentTransactionAppModel implements Serializable {
	static final long serialVersionUID = 3656968098151140700L;
	private RedeemableBuyBackTransaction txn;

	/**
	 * put your documentation comment here
	 */
	public CMSRedeemableBuyBackTransactionAppModel() {
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param RedeemableBuyBackTransaction
	 *            txn
	 */
	public CMSRedeemableBuyBackTransactionAppModel(RedeemableBuyBackTransaction txn) {
		this.txn = txn;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ResourceBundleKey getReceiptLabel() {
		return (new ResourceBundleKey("REDEEMABLE BUY BACK"));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getAuditString() {
		StringBuffer auditSB = new StringBuffer();
		if (txn.getComment().length() > 0) {
			auditSB.append(res.getString("Comments"));
			auditSB.append(": ");
			auditSB.append(txn.getComment() + " ");
		}
		auditSB.append("\n");
		auditSB.append(res.getString("Buy Back of Redeemable "));
		auditSB.append(txn.getRedeemable().getId() + " ");
		auditSB.append(super.getAuditString());
		return auditSB.toString();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ViewLineItemAppModel[] getLineItemsAppModelArray() {
		ArrayList list = new ArrayList();
		BuyBackViewLines line = new BuyBackViewLines("BuyBack", "", "", "", "", txn.getAmount().formattedStringValue());
		list.add(line);
		return (BuyBackViewLines[]) list.toArray(new BuyBackViewLines[list.size()]);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public PaymentTransaction getPaymentTransaction() {
		return (txn);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getComments() {
		return (txn.getComment());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Employee getConsultant() {
		// return(txn.getTheOperator());
		return new CMSEmployee();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Customer getCustomer() {
		if (txn instanceof CMSRedeemableBuyBackTransaction && ((CMSRedeemableBuyBackTransaction) txn).getCustomer() != null) {
			return ((CMSRedeemableBuyBackTransaction) txn).getCustomer();
		} else {
			return super.getCustomer();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 */
	public void printReceipt(IApplicationManager theAppMgr) {
		Object[] arguments = { this };
		ReceiptFactory receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSBuyBack);
		ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(txn.getStore(), getCustomer());
		localeSetter.setLocale(receiptFactory);
		receiptFactory.print(theAppMgr);
		if (getSignaturePaymentsArray().length > 0) {
			receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSBuyBack_Sigs);
			localeSetter.setLocale(receiptFactory);
			receiptFactory.print(theAppMgr);
		}
		if (getDueBillIssuePaymentsArray().length > 0) {
			receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSDueBillIssue);
			localeSetter.setLocale(receiptFactory);
			receiptFactory.print(theAppMgr);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 */
	public void printCancelReceipt(IApplicationManager theAppMgr) {
		Object[] arguments = { this };
		ReceiptFactory receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSBuyBack_Cancel);
		ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(txn.getStore(), getCustomer());
		localeSetter.setLocale(receiptFactory);
		receiptFactory.print(theAppMgr);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 */
	public void printSuspendedReceipt(IApplicationManager theAppMgr) {
		Object[] arguments = { this };
		ReceiptFactory receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSBuyBack_Suspend);
		ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(txn.getStore(), getCustomer());
		localeSetter.setLocale(receiptFactory);
		receiptFactory.print(theAppMgr);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 */
	public void rePrintReceipt(IApplicationManager theAppMgr) {
		Object[] arguments = { this };
		if (getSignaturePaymentsArray().length > 0) {
			if (theAppMgr.showOptionDlg(res.getString("Store copy?"), res.getString("Do you also want to print a signature copy?"))) {
				ReceiptFactory receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSBuyBack_Sigs);
				ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(txn.getStore(), getCustomer());
				localeSetter.setLocale(receiptFactory);
				receiptFactory.reprint(theAppMgr);
			}
		}
		ReceiptFactory receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSBuyBack);
		ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(txn.getStore(), getCustomer());
		localeSetter.setLocale(receiptFactory);
		receiptFactory.reprint(theAppMgr);
		if (getDueBillIssuePaymentsArray().length > 0) {
			receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSDueBillIssue);
			localeSetter.setLocale(receiptFactory);
			receiptFactory.reprint(theAppMgr);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Enumeration getDiscounts() {
		return (null);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getCompositeNetAmount() {
		return (txn.getAmount());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getCompositeRetailAmount() {
		return (txn.getAmount());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getCompositeReductionAmount() {
		return (new ArmCurrency(0));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getCompositeTaxAmount() {
		return (new ArmCurrency(0));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getCompositeTotalAmountDue() {
		return (txn.getAmount());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getSaleNetAmount() {
		return (txn.getAmount());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getSaleReductionAmount() {
		return (new ArmCurrency(0));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getSaleTaxAmount() {
		return (new ArmCurrency(0));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public ArmCurrency getSaleTotalAmountDue() {
		return (txn.getAmount());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public POSLineItem[] getLineItemsArray() {
		return (new POSLineItem[0]);
	}

	public class BuyBackViewLines extends ViewLineItemAppModel {
		String itemID;
		String itemDesc;
		String quantityString;
		String retailPriceString;
		String reductionString;
		String totalAmtDueString;

		/**
		 * put your documentation comment here
		 * 
		 * @param String
		 *            itemID
		 * @param String
		 *            itemDesc
		 * @param String
		 *            quantityString
		 * @param String
		 *            retailPriceString
		 * @param String
		 *            reductionString
		 * @param String
		 *            totalAmtDueString
		 */
		public BuyBackViewLines(String itemID, String itemDesc, String quantityString, String retailPriceString, String reductionString, String totalAmtDueString) {
			this.itemID = itemID;
			this.itemDesc = itemDesc;
			this.quantityString = quantityString;
			this.retailPriceString = retailPriceString;
			this.reductionString = reductionString;
			this.totalAmtDueString = totalAmtDueString;
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public Item getItem() {
			CMSItem item = new CMSItem(itemID);
			item.doSetDescription(itemDesc);
			return (item);
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public String getItemDescription() {
			return (itemDesc);
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public String getQuantity() {
			return (quantityString);
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public String getItemRetailPrice() {
			return (retailPriceString);
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public String getExtendedReductionAmount() {
			return (reductionString);
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public String getExtendedNetAmount() {
			return (totalAmtDueString);
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public String getType() {
			// return CMSBuyBackTransactionAppModel.this.paidOutTransaction.getTransactionType();
			return ("BuyBack");
		}

		/**
		 * put your documentation comment here
		 * 
		 * @return
		 */
		public String getTaxAmount() {
			return ("");
		}
	} // end La

	/**
	 * Method to determine if the transaction is valid as refund.
	 * 
	 * @return
	 */
	public boolean isRefundPaymentRequired() {
		return true;
	}
}
