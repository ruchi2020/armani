/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import java.io.Serializable;
import java.util.ResourceBundle;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.customer.CustomerAppModel;
import com.chelseasystems.cr.employee.EmployeeAppModel;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.util.ResourceBundleKey;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerAppModel;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeAppModel;
import com.chelseasystems.cs.payment.CMSPaymentAppModel;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.swing.model.GiftReceiptEntry;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.customer.Customer;
import java.util.MissingResourceException;
import com.chelseasystems.cs.store.CMSStore;
import java.text.SimpleDateFormat;


/**
 * put your documentation comment here
 */
public abstract class CMSPaymentTransactionAppModel extends PaymentTransactionAppModel implements
    Serializable {
  static final long serialVersionUID = 6986630571992302569L;
  ResourceBundle res;
  GiftReceiptEntry[] entries;
  private ConfigMgr jPosCfg = new ConfigMgr("JPOS_peripherals.cfg");
  private String defaultCustomerFirstName = "";
  private String defaultCustomerLastName = "";
  private SimpleDateFormat dateFormat = null;

  /**
   * put your documentation comment here
   */
  public CMSPaymentTransactionAppModel() {
    res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    if(jPosCfg.getString("DEFAULT_CUSTOMER.FIRST_NAME") != null)
      defaultCustomerFirstName = jPosCfg.getString("DEFAULT_CUSTOMER.FIRST_NAME");
    if(jPosCfg.getString("DEFAULT_CUSTOMER.LAST_NAME") != null)
    defaultCustomerLastName = jPosCfg.getString("DEFAULT_CUSTOMER.LAST_NAME");
    dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CustomerAppModel getCustomerAppModel() {
    return new CMSCustomerAppModel((CMSCustomer)getCustomer());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public EmployeeAppModel getConsultantAppModel() {
    return new CMSEmployeeAppModel((CMSEmployee)getConsultant());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSPaymentAppModel[] getPaymentModelArray() {
    return CMSPaymentAppModel.getPaymentModelArray(this.getPaymentsArray());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getCompositeRegionalTaxAmount() {
    return new ArmCurrency(0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTaxExemptId() {
    return "";
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getRegionalTaxExemptId() {
    return "";
  }

  /**
   * put your documentation comment here
   * @param entries
   * @exception BusinessRuleException
   */
  public void setSelectedGiftItems(GiftReceiptEntry[] entries)
      throws BusinessRuleException {
    System.out.println("Setting the Selected Gift Items");
    this.entries = entries;
    if (entries == null)
      System.out.println("ENTRIES ARE NULL");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public GiftReceiptEntry[] getSelectedGiftItems() {
    if (entries == null)
      System.out.println("ENTRIES ARE NULLin get()");
    return this.entries;
  }

  /**
   * put your documentation comment here
   * @param id
   * @exception BusinessRuleException
   */
  public void setTaxExemptId(String id)
      throws BusinessRuleException {
    throw new BusinessRuleException("Cannot set tax exempt ID for this transaction type");
  }

  /**
   * put your documentation comment here
   * @param id
   * @exception BusinessRuleException
   */
  public void setRegionalTaxExemptId(String id)
      throws BusinessRuleException {
    throw new BusinessRuleException("Cannot set regional tax exempt ID for this transaction type");
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printGiftReceipt(IApplicationManager theAppMgr) {
    Object[] arguments = {this
    };
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments
        , ReceiptBlueprintInventory.CMSGiftReceipt);
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter(getPaymentTransaction().getStore()
        , getCustomer());
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAuditString() {
    if (getPaymentTransaction().getVoidTransaction() != null)
      return res.getString("Voided via ") + getPaymentTransaction().getVoidTransaction().getId();
    else
      return "";
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printVATInvoice(IApplicationManager theAppMgr) {}

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void printRetailExportReceipt(IApplicationManager theAppMgr) {}

  /**
   * put your documentation comment here
   * @return
   */
  public String getPaymentScreenRetailLabel() {
    return "Retail";
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ResourceBundleKey getRegionalTaxLabel() {
    PaymentTransaction txn = getPaymentTransaction();
    ResourceBundleKey resKey = new ResourceBundleKey();
    String label = "";
    if (txn.getStore() != null) {
      label = txn.getStore().getRegionalTaxLabel();
    }
    if (label == null || label.length() == 0) {
      resKey.setKey("regional tax label");
    } else {
      resKey.setKey(label);
    }
    return resKey;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ResourceBundleKey getTaxLabel() {
    PaymentTransaction txn = getPaymentTransaction();
    ResourceBundleKey resKey = new ResourceBundleKey();
    String label = "";
    if (txn.getStore() != null) {
      label = txn.getStore().getTaxLabel();
    }
    if (label == null || label.length() == 0) {
      resKey.setKey("tax label");
    } else {
      resKey.setKey(label);
    }
    return resKey;
  }

  public Customer getCustomer() {
    //Customer cust = new CMSCustomer();
    Customer cust = null;
    if (defaultCustomerFirstName.trim().length() > 0 && defaultCustomerLastName.trim().length() > 0){
      cust = new CMSCustomer();
      try {
        if (defaultCustomerFirstName.trim().length() > 0)
          cust.setFirstName(res.getString(defaultCustomerFirstName).trim());
        if (defaultCustomerLastName.trim().length() > 0)
          cust.setLastName(res.getString(defaultCustomerLastName).trim());
      } catch (MissingResourceException ex) {
        ex.printStackTrace();
      } catch (BusinessRuleException ex) {
        ex.printStackTrace();
      }
    }
    return cust;
  }

  public String getMessage()
  {
    return ((CMSStore)getPaymentTransaction().getStore()).getMessage();
  }

  /**
   * Return Formatted Date string for a said format.
   * @param sFormat String
   * @return String
   */

  public String getFormattedSaleDate() {
    PaymentTransaction txn = getPaymentTransaction();
    try {
       return dateFormat.format(txn.getSubmitDate());
    }
    catch (Exception e) {
      System.out.println("****Exception formatting Transaction date**** CMSPaymentTransactionAppModel-> getFormattedSaleDate()");
    }
    return txn.getSubmitDate().toString();
  }

  /**
   * Return Process Date string for a said format.
   * @return String
   */

  public String getFormattedProcessDate() {
    PaymentTransaction txn = getPaymentTransaction();
    try {
       return dateFormat.format(txn.getProcessDate());
    }
    catch (Exception e) {
      System.out.println("****Exception process Transaction date**** CMSPaymentTransactionAppModel-> getFormattedProcessDate()");
    }
    return txn.getProcessDate().toString();
  }
}

