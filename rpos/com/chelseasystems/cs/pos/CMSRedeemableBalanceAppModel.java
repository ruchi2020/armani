/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.rb.*;

import java.util.*;

import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.HouseAccount;


/**
 *
 * <p>Title: CMSRedeemableBalanceAppModel</p>
 *
 * <p>Description: This class store the redeemable balance attributes</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public class CMSRedeemableBalanceAppModel
    /* extends CMSPaymentTransactionAppModel */
    implements java.io.Serializable {
  //static final long serialVersionUID = 3656968098151140700L;
  private Redeemable redeemMe;
  private CMSStore storeObj;
  private transient ReceiptLocaleSetter localeSetter;
  private String store;
  private String phone;
  //ks: phone needs to be replaced with customer id
  private String customerId;
 

  /**
   * Default constructor
   */
  public CMSRedeemableBalanceAppModel() {
  }

  /**
   * Constructor
   * @param redeemMe Redeemable
   */
  public CMSRedeemableBalanceAppModel(Redeemable redeemMe) {
    this.redeemMe = redeemMe;
  }

  /**
   * Constructor
   * @param txn CompositePOSTransaction
   */
  public CMSRedeemableBalanceAppModel(CompositePOSTransaction txn) {
    store = txn.getStore().getId();
    phone = txn.getStore().getTelephone() == null ? ""
        : txn.getStore().getTelephone().getFormattedNumber();
    localeSetter = new ReceiptLocaleSetter(txn.getStore(), (CMSCustomer)txn.getCustomer());
  }

  /**
   * This method is used to get receipt label for redeemable balance
   * @return ResourceBundleKey
   */
  public ResourceBundleKey getReceiptLabel() {
    return (new ResourceBundleKey("REDEEMABLE BALANCE"));
  }

  /**
   * This method is used to get customer id
   * @return String
   */
  public String getCustomerId() {
    if (redeemMe instanceof CMSStoreValueCard) {
      return ((CMSStoreValueCard)redeemMe).getCustomerId();
    } else if (redeemMe instanceof CMSDueBillIssue) {
      return ((CMSDueBillIssue)redeemMe).getCustomerId();
    } else if (redeemMe instanceof HouseAccount) {
      return ((HouseAccount)redeemMe).getCustomerId();
    }
    return null;
  }

  /**
   * This method is used to get customer first name
   * @return String
   */
  public String getCustomerName() {
    String customerName = "";
    if (redeemMe.getFirstName() != null)
      customerName += redeemMe.getFirstName();
    if (redeemMe.getLastName() != null)
      customerName += " " + redeemMe.getLastName();
    return customerName.trim();
  }

  /**
   * This method is used to get customer phone number
   * @return String
   */
  public String getCustomerPhoneNumber() {
    return (redeemMe.getPhoneNumber());
  }

  /**
   * This method is used to get redeemable amount
   * @return Currency
   */
  public ArmCurrency getAmount() {
    return (redeemMe.getAmount());
  }

  /**
   * This method is used to get redeemable audit note
   * @return String
   */
  public String getAuditNote() {
    return (redeemMe.getAuditNote());
  }

  /**
   * This method is used to get redeemable creation date
   * @return Date
   */
  public Date getCreationDate() {
    return (redeemMe.getCreateDate());
  }

  /**
   *
   * @return CurrencyType
   */
  public CurrencyType getCurrencyTypeConvertedFrom() {
    return (redeemMe.getCurrencyTypeConvertedFrom());
  }

  /**
   * This method is used to get GUI payment detail
   * @return String
   */
  public String getGUIPaymentDetail() {
    return (redeemMe.getGUIPaymentDetail());
  }

  /**
   * This method is used to get GUI payment name
   * @return String
   */
  public String getGUIPaymentName() {
    return (redeemMe.getGUIPaymentName());
  }

  /**
   * This method is used to redeemable id
   * @return String
   */
  public String getRedeemableID() {
    return (redeemMe.getId());
  }

  /**
   * This method is used to get issue amount for the Redeemable
   * @return Currency
   */
  public ArmCurrency getIssueAmount() {
    return (redeemMe.getIssueAmount());
  }

  /**
   * This method is used to get redemption history
   * @return Vector
   */
  public Vector getRedemptionHistory() {
    return (redeemMe.getRedemptionHistory());
  }

  /**
   * This method returns the store name
   * @return String
   */
  public String getStore() {
    if (store != null) {
      return (store);
    }
    return ("");
  }

  /**
   * This method returns the store phone number
   * @return String
   */
  public String getStorePhone() {
    if (phone != null) {
      return (phone);
    }
    return ("");
  }

  /**
   * This method is used to get remaining balance of redeemable
   * @return Currency
   */
  public ArmCurrency getRemainingBalance() {
    try {
      if (redeemMe instanceof HouseAccount)
        return new ArmCurrency(0.0d);
      ArmCurrency amount = redeemMe.getAmount();
      if (amount != null) {
    	  //vishal yevale 14 oct 2016
    	   String fipay_GiftCard_flag;
    	    String fileName = "store_custom.cfg";    
    	    ConfigMgr config = new ConfigMgr(fileName);
    	    fipay_GiftCard_flag = config.getString("FIPAY_GIFTCARD_INTEGRATION");
    	    if(fipay_GiftCard_flag!=null && fipay_GiftCard_flag.equalsIgnoreCase("Y")){
    	    	if (redeemMe instanceof CMSStoreValueCard && ((CMSStoreValueCard) redeemMe).getGiftcardBalance() !=null ) {
		        	 return ((CMSStoreValueCard) redeemMe).getGiftcardBalance().subtract(amount);

			   } else if(redeemMe instanceof CMSDueBill && ((CMSDueBill) redeemMe).getGiftcardBalance() !=null){
				        	 return ((CMSDueBill) redeemMe).getGiftcardBalance().subtract(amount);
		              }
			   else {  
			        return null;
			    	    }
    	    }else {  
        return (redeemMe.getRemainingBalance().subtract(amount));
    	    }
      }//end vishal yevale
      return (redeemMe.getRemainingBalance());
    } catch (CurrencyException ce) {
      return (null);
    }
  }

  /**
   * This method is used to get the transaction payment name
   * @return String
   */
  public String getTransactionPaymentName() {
    return (redeemMe.getTransactionPaymentName());
  }

  /**
   * This method return the current date
   * @return Date
   */
  public Date getDate() {
    return (new Date());
  }

  /**
   * This method is used to print the receipt of redeemable balance
   * @param theAppMgr IApplicationManager
   */
  public void printReceipt(IApplicationManager theAppMgr) {
	storeObj = (CMSStore)theAppMgr.getGlobalObject("STORE");
    if (store == null || store.equals("")) {
      store = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
    }
    if (phone == null || phone.equals("")) {
      phone = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getTelephone() == null ? ""
          : ((CMSStore)theAppMgr.getGlobalObject("STORE")).getTelephone().getFormattedNumber();
    }
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSRedeemableBalance);
    if (localeSetter == null) {
      localeSetter = new ReceiptLocaleSetter((CMSStore)theAppMgr.getGlobalObject("STORE")
          , (CMSCustomer)null);
    }
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
  }

  /**
   * This method is used to re print the receipt of redeemable balance
   * @param theAppMgr IApplicationManager
   */
  public void rePrintReceipt(IApplicationManager theAppMgr) {
    if (store == null || store.equals("")) {
      store = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
    }
    if (phone == null || phone.equals("")) {
      phone = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getTelephone() == null ? ""
          : ((CMSStore)theAppMgr.getGlobalObject("STORE")).getTelephone().getFormattedNumber();
    }
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSRedeemableBalance);
    if (localeSetter == null) {
      localeSetter = new ReceiptLocaleSetter((CMSStore)theAppMgr.getGlobalObject("STORE")
          , (CMSCustomer)null);
    }
    localeSetter.setLocale(receiptFactory);
    receiptFactory.reprint(theAppMgr);
  }
 
  /**
   * This method is used to print store address on receipt.
   * This is a fix for issue # 1558: Receipts - Add store address to receipt
   * @return
   */
  public CMSStore getStoreObject() {
	return storeObj;
  }
}

