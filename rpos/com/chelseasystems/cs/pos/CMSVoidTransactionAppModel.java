/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */

package com.chelseasystems.cs.pos;

import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.pos.ViewLineItemAppModel;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cr.paidout.*;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.employee.EmployeeAppModel;
import com.chelseasystems.cr.customer.CustomerAppModel;
import java.io.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import java.util.ArrayList;
import java.util.Enumeration;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.swing.CMSApplet;

public class CMSVoidTransactionAppModel extends CMSPaymentTransactionAppModel implements Serializable {
	//static final long serialVersionUID =
	private VoidTransaction voidTransaction;

	public CMSVoidTransactionAppModel() {
	}

	public CMSVoidTransactionAppModel(VoidTransaction voidTransaction) {
		this.voidTransaction = voidTransaction;
	}

	public PaymentTransaction getPaymentTransaction() {
		return (voidTransaction);
	}

	public ArmCurrency getTotalPaymentAmount() {
		return (super.getTotalPaymentAmount().multiply(-1d));
	}

	public String getAuditString() {
		return (CMSApplet.res.getString("Void Of") + " " + voidTransaction.getOriginalTransaction().getId() + ", " + CMSApplet.res.getString("Reason:") + " " + voidTransaction.getReason());
	}

	public ViewLineItemAppModel[] getLineItemsAppModelArray() {
		ArrayList list = new ArrayList();
		//System.out.println("the voided amount: " + voidTransaction.getVoidedAmount().formattedStringValue());
		VoidTxnViewLines line = new VoidTxnViewLines("", CMSApplet.res.getString("Void of") + " " + voidTransaction.getOriginalTransaction().getId(), "", "", "",
				voidTransaction.getVoidedAmount().multiply(-1d).formattedStringValue());
		list.add(line);
		return (VoidTxnViewLines[]) list.toArray(new VoidTxnViewLines[list.size()]);
	}

	public Employee getConsultant() {
		// return(voidTransaction.getTheOperator());
		return new CMSEmployee();
	}

	public Customer getCustomer() {
		return (new CMSCustomer());
	}

	public void printReceipt(IApplicationManager theAppMgr) {
		Object[] arguments = { this };
		ReceiptFactory receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSVoidTransaction);
		ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(getPaymentTransaction().getStore(), getCustomer());
		localeSetter.setLocale(receiptFactory);
		receiptFactory.print(theAppMgr);
	}

	public void printCancelReceipt(IApplicationManager theAppMgr) {
		// I don't think this applies here, but if it does just implement it by printing the CMSVoidTransaction_Cancel receipt
	}

	public void printSuspendedReceipt(IApplicationManager theAppMgr) {
		// I don't think this applies here, but if it does just implement it by printing the CMSVoidTransaction_Suspend receipt
	}

	public void rePrintReceipt(IApplicationManager theAppMgr) {
		Object[] arguments = { this };
		ReceiptFactory receiptFactory = new ReceiptFactory(arguments, ReceiptBlueprintInventory.CMSVoidTransaction);
		ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(getPaymentTransaction().getStore(), getCustomer());
		localeSetter.setLocale(receiptFactory);
		receiptFactory.reprint(theAppMgr);
	}

	public Enumeration getDiscounts() {
		return (null);
	}

	public ArmCurrency getCompositeNetAmount() {
		if (voidTransaction.getOriginalTransaction() instanceof CompositePOSTransaction)
			return ((CompositePOSTransaction) voidTransaction.getOriginalTransaction()).getCompositeNetAmount().multiply(-1d);
		else
			return (voidTransaction.getVoidedAmount().multiply(-1d));
	}

	public ArmCurrency getCompositeRetailAmount() {
		return (new ArmCurrency(0));
	}

	public ArmCurrency getCompositeReductionAmount() {
		return (new ArmCurrency(0));
	}

	public ArmCurrency getCompositeTaxAmount() {
		if (voidTransaction.getOriginalTransaction() instanceof CompositePOSTransaction)
			return ((CompositePOSTransaction) voidTransaction.getOriginalTransaction()).getSaleTaxAmount().multiply(-1d);
		else
			return (new ArmCurrency(0));
	}

	public ArmCurrency getCompositeRegionalTaxAmount() {
		if (voidTransaction.getOriginalTransaction() instanceof CompositePOSTransaction) {
			CompositePOSTransaction posTxn = (CompositePOSTransaction) voidTransaction.getOriginalTransaction();
			if (posTxn.getStore().usesRegionalTaxCalculations()) {
				return (posTxn.getCompositeRegionalTaxAmount().multiply(-1d));
			}
		}

		return (new ArmCurrency(0));
	}

	public ArmCurrency getCompositeTotalAmountDue() {
		return (new ArmCurrency(0));
	}

	public ArmCurrency getSaleNetAmount() {
		return (new ArmCurrency(0));
	}

	public ArmCurrency getSaleReductionAmount() {
		return (new ArmCurrency(0));
	}

	public ArmCurrency getSaleTaxAmount() {
		return (new ArmCurrency(0));
	}

	public ArmCurrency getSaleTotalAmountDue() {
		return (new ArmCurrency(0));
	}

	public POSLineItem[] getLineItemsArray() {
		return (new POSLineItem[0]);
	}

	public class VoidTxnViewLines extends ViewLineItemAppModel {

		String itemID;
		String itemDesc;
		String quantityString;
		String retailPriceString;
		String reductionString;
		String totalAmtDueString;

		public VoidTxnViewLines(String itemId, String itemDesc, String quantityString, String retailPriceString, String reductionString, String totalAmtDueString) {
			this.itemID = itemID;
			this.itemDesc = itemDesc;
			this.quantityString = quantityString;
			this.retailPriceString = retailPriceString;
			this.reductionString = reductionString;
			this.totalAmtDueString = totalAmtDueString;
		}

		public Item getItem() {
			CMSItem item = new CMSItem(itemID);
			item.doSetDescription(itemDesc);
			return (item);
		}

		public String getItemDescription() {
			return (itemDesc);
		}

		public String getQuantity() {
			return (quantityString);
		}

		public String getItemRetailPrice() {
			return (retailPriceString);
		}

		public String getExtendedReductionAmount() {
			return (reductionString);
		}

		public String getExtendedNetAmount() {
			return (totalAmtDueString);
		}

		public String getType() {
			return (CMSVoidTransactionAppModel.this.getPaymentTransaction().getTransactionType());
		}

		public String getTaxAmount() {
			return ("");
		}

	} // end La
}
