/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.fiscaldocument;

import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.register.CMSRegister;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import com.chelseasystems.cs.pos.CMSNoSaleLineItem;
import com.chelseasystems.cs.pos.CMSNoReturnLineItem;
import com.chelseasystems.cs.pos.CMSPresaleLineItem;
import com.chelseasystems.cs.pos.CMSReturnLineItem;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSConsignmentLineItem;
import com.chelseasystems.cs.pos.CMSReservationLineItem;


/**
 * <p>Title:FiscalDocument </p>
 * <p>Description:Represents Fiscal Documents </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05-26-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class FiscalDocument extends BusinessObject {
  /**
   * AddressLine 1
   */
  private String sAddressLine1;
  /**
   * AddressLine 2
   */
  private String sAddressLine2;
  /**
   * City
   */
  private String sCity;
  /**
   * CompanyName 1
   */
  private String sCompanyName;
  /**
   * CompanyName 2
   */
  private String sCompanyName2;
  /**
   * Country
   */
  private String sCountry;
  /**
   * County
   */
  private String sCounty;
  /**
   * ZipCode
   */
  private String sZipCode;
  /**
   * Document Number
   */
  private String sDocumentNum;
  /**
   * Document Tye
   */
  private String sDocumentType;
  /**
   * Vat Number
   */
  private String sVatNum;
  /**
   * LineItems
   */
  private Vector vecLineItems;
  /**
   * Refund Amount
   */
  private ArmCurrency amtRefund;
  /**
   * Transaction
   */
  private CMSCompositePOSTransaction posTxn;
  /**
   * DDT type document.
   */
  private final String TYPE_DDT = "DDT";
  /**
   * VAT type document.
   */
  private final String TYPE_VAT_INVOICE = "VAT";
  /**
   * Credit Note type document.
   */
  private final String TYPE_CREDIT_NOTE = "CREDIT_NOTE";
  /**
   * Tax Free type document.
   */
  private final String TYPE_TAX_FREE = "TAX_FREE";
  
  private final String RECEIPT_DOCUMENT = "RECEIPT_DOCUMENT";
  /**
   * Fisal Document Configuration File
   */
  private final String FISAL_CONFIG_FILE = "fiscal_document.cfg";
  /**
   * Hashtable to store Document Types and Codes
   */
  private Hashtable htDocumentTypes;

  private String taxFreeCode;

  private boolean isCreditNoteCountry;

  private String masterRegister;

  private CMSRegister register;

  private Date fiscalDate;

  private boolean isDocumentNumberAssigned;

  /**
   * Default Constructor
   */
  public FiscalDocument() {
    ConfigMgr config = new ConfigMgr(FISAL_CONFIG_FILE);
    htDocumentTypes = new Hashtable();
    htDocumentTypes.put(TYPE_DDT, config.getString(TYPE_DDT + ".CODE"));
    htDocumentTypes.put(TYPE_VAT_INVOICE, config.getString(TYPE_VAT_INVOICE + ".CODE"));
    htDocumentTypes.put(TYPE_CREDIT_NOTE, config.getString(TYPE_CREDIT_NOTE + ".CODE"));
    htDocumentTypes.put(TYPE_TAX_FREE, config.getString(TYPE_TAX_FREE + ".CODE"));
    htDocumentTypes.put(RECEIPT_DOCUMENT, config.getString(RECEIPT_DOCUMENT + ".CODE"));
    masterRegister = config.getString("MASTER_REGISTER");
    sAddressLine1 = new String();
    sAddressLine2 = new String();
    sCity = new String();
    sCompanyName = new String();
    sCompanyName2 = new String();
    sCountry = new String();
    sCounty = new String();
    sZipCode = new String();
    sDocumentNum = new String();
    sDocumentType = new String();
    sVatNum = new String();
    vecLineItems = new Vector();
    posTxn = null;
    amtRefund = new ArmCurrency(0.0d);
    isCreditNoteCountry  = false;
    isDocumentNumberAssigned = false;
  }

  public FiscalDocument(CMSCompositePOSTransaction aCompositeTransaction) {
    ConfigMgr config = new ConfigMgr(FISAL_CONFIG_FILE);
    htDocumentTypes = new Hashtable();
    htDocumentTypes.put(TYPE_DDT, config.getString(TYPE_DDT + ".CODE"));
    htDocumentTypes.put(TYPE_VAT_INVOICE, config.getString(TYPE_VAT_INVOICE + ".CODE"));
    htDocumentTypes.put(TYPE_CREDIT_NOTE, config.getString(TYPE_CREDIT_NOTE + ".CODE"));
    htDocumentTypes.put(TYPE_TAX_FREE, config.getString(TYPE_TAX_FREE + ".CODE"));
    htDocumentTypes.put(RECEIPT_DOCUMENT, config.getString(RECEIPT_DOCUMENT + ".CODE"));
    sAddressLine1 = new String();
    sAddressLine2 = new String();
    sCity = new String();
    sCompanyName = new String();
    sCompanyName2 = new String();
    sCountry = new String();
    sCounty = new String();
    sZipCode = new String();
    sDocumentNum = new String();
    sDocumentType = new String();
    sVatNum = new String();
    vecLineItems = new Vector();
    amtRefund = new ArmCurrency(0.0d);
    posTxn = aCompositeTransaction;
    aCompositeTransaction.doAddFiscalDocument(this);

  }

  /**
   * Check if document type is DDT
   * @return boolean
   */
  public boolean isDDTDocument() {
    return ((String)htDocumentTypes.get(TYPE_DDT)).equalsIgnoreCase(sDocumentType);
  }

  /**
   * Check if Document type is Vat Invoice
   * @return boolean
   */
  public boolean isVatInvoiceDocument() {
    return ((String)htDocumentTypes.get(TYPE_VAT_INVOICE)).equalsIgnoreCase(sDocumentType);
  }

  /**
   * Check if Document type is TaxFree
   * @return boolean
   */
  public boolean isTaxFreeDocument() {
    return ((String)htDocumentTypes.get(TYPE_TAX_FREE)).equalsIgnoreCase(sDocumentType);
  }

  /**
   * Check if Document type is CreditNote
   * @return boolean
   */
  public boolean isCreditNoteDocument() {
    return ((String)htDocumentTypes.get(TYPE_CREDIT_NOTE)).equalsIgnoreCase(sDocumentType);
  }

  /**
   * Code for DDT type document
   * @return String
   */
  public String getCodeForDDTDocumentType() {
    return ((String)htDocumentTypes.get(TYPE_DDT));
  }

  /**
   * Code for VatInvoice type document
   * @return String
   */
  public String getCodeForVatInvoiceDocumentType() {
    return ((String)htDocumentTypes.get(TYPE_VAT_INVOICE));
  }

  /**
   * Code for CreditNote type document
   * @return String
   */
  public String getCodeForCreditNoteDocumentType() {
    return ((String)htDocumentTypes.get(TYPE_CREDIT_NOTE));
  }

  /**
   * Code for TaxFree type document
   * @return String
   */
  public String getCodeForTaxFreeDocumentType() {
    return ((String)htDocumentTypes.get(TYPE_TAX_FREE));
  }

  /**
   * Add LineItem to document
   * @param lineItem POSLineItem
   */
  public void addLineItem(POSLineItem lineItem) {
    if (lineItem == null)
      return;
    doAddLineItem(lineItem);
  }

  /**
   * Remove LineItem from Document
   * @param lineItem POSLineItem
   */
  public void removeLineItem(POSLineItem lineItem) {
    if (lineItem == null)
      return;
    doRemoveLineItem(lineItem);
  }

  /**
   * POSLineItem
   * @param iIndex int
   * @return POSLineItem
   */
  public POSLineItem getLineItemAt(int iIndex) {
    if (iIndex < 0 || vecLineItems == null || iIndex >= vecLineItems.size())
      return null;
    return (POSLineItem)vecLineItems.elementAt(iIndex);
  }

  /**
   * POSLineItem array.
   * @return POSLineItem[]
   */
  public POSLineItem[] getLineItemsArray() {
    return (POSLineItem[])vecLineItems.toArray(new POSLineItem[vecLineItems.size()]);
  }

  /**
   * Enumerated POSLineItems
   * @return Enumeration
   */
  public Enumeration getLineItems() {
    return vecLineItems.elements();
  }

  /**
   * Add line Item
   * @param lineItem POSLineItem
   */
  public void doAddLineItem(POSLineItem lineItem) {
    vecLineItems.addElement(lineItem);
  }

  /**
   * Remove LineItem
   * @param lineItem POSLineItem
   */
  public void doRemoveLineItem(POSLineItem lineItem) {
    vecLineItems.remove(lineItem);
  }

  /**
   * AddressLine1
   * @param sValue String
   */
  public void setAddressLine1(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetAddressLine1(sValue);
  }

  /**
   * AddressLine1
   * @param sValue String
   */
  public void doSetAddressLine1(String sValue) {
    this.sAddressLine1 = sValue;
  }

  /**
   * AddressLine1
   * @return String
   */
  public String getAddressLine1() {
    return this.sAddressLine1;
  }

  /**
   * AddressLine2
   * @return String
   */
  public void setAddressLine2(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetAddressLine2(sValue);
  }

  /**
   * AddressLine2
   * @return String
   */
  public void doSetAddressLine2(String sValue) {
    this.sAddressLine2 = sValue;
  }

  /**
   * AddressLine2
   * @return String
   */
  public String getAddressLine2() {
    return this.sAddressLine2;
  }

  /**
   * City
   * @return String
   */
  public void setCity(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCity(sValue);
  }

  /**
   * City
   * @param sValue String
   */
  public void doSetCity(String sValue) {
    this.sCity = sValue;
  }

  /**
   * City
   * @return String
   */
  public String getCity() {
    return this.sCity;
  }

  /**
   * CompanyName
   * @return String
   */
  public void setCompanyName(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCompanyName(sValue);
  }

  /**
   * CompanyName
   * @param sValue String
   */
  public void doSetCompanyName(String sValue) {
    this.sCompanyName = sValue;
  }

  /**
   * CompanyName
   * @return String
   */
  public String getCompanyName() {
    return this.sCompanyName;
  }

  /**
   * CompanyName2
   * @return String
   */
  public void setCompanyName2(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCompanyName2(sValue);
  }

  /**
   * CompanyName2
   * @param sValue String
   */
  public void doSetCompanyName2(String sValue) {
    this.sCompanyName2 = sValue;
  }

  /**
   * CompanyName2
   * @return String
   */
  public String getCompanyName2() {
    return this.sCompanyName2;
  }

  /**
   * Transaction
   * @param theTxn CMSCompositePOSTransaction
   */
  public void setTxn(CMSCompositePOSTransaction theTxn) {
    if (theTxn == null)
      return;
    doSetTxn(theTxn);
  }

  /**
   * Transaction
   * @param theTxn CMSCompositePOSTransaction
   */
  public void doSetTxn(CMSCompositePOSTransaction theTxn) {
    this.posTxn = theTxn;
  }

  /**
   * Transaction
   * @param theTxn CMSCompositePOSTransaction
   */
  public CMSCompositePOSTransaction getTxn() {
    return this.posTxn;
  }

  /**
   * Country
   * @return String
   */
  public void setCountry(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCountry(sValue);
  }

  /**
   * Country
   * @param sValue String
   */
  public void doSetCountry(String sValue) {
    this.sCountry = sValue;
  }

  /**
   * Country
   * @return String
   */
  public String getCountry() {
    return this.sCountry;
  }

  /**
   * County
   * @return String
   */
  public void setCounty(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetCounty(sValue);
  }

  /**
   * County
   * @param sValue String
   */
  public void doSetCounty(String sValue) {
    this.sCounty = sValue;
  }

  /**
   * County
   * @return String
   */
  public String getCounty() {
    return this.sCounty;
  }

  /**
   * ZipCode
   * @return String
   */
  public void setZipCode(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetZipCode(sValue);
  }

  /**
   * ZipCode
   * @param sValue String
   */
  public void doSetZipCode(String sValue) {
    this.sZipCode = sValue;
  }

  /**
   * ZipCode
   * @return String
   */
  public String getZipCode() {
    return this.sZipCode;
  }

  /**
   * DocumentNumber
   * @return String
   */
  public void setDocumentNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetDocumentNumber(sValue);
  }

  /**
   * DocumentNumber
   * @param sValue String
   */
  public void doSetDocumentNumber(String sValue) {
    this.sDocumentNum = sValue;
  }

  /**
   * DocumentNumber
   * @return String
   */
  public String getDocumentNumber() {
    return this.sDocumentNum;
  }

  /**
   * DocumentType
   * @return String
   */
  public void setDocumentType(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetDocumentType(sValue);
  }

  /**
   * DocumentType
   * @param sValue String
   */
  public void doSetDocumentType(String sValue) {
    this.sDocumentType = sValue;
  }

  /**
   * DocumentType
   * @return String
   */
  public String getDocumentType() {
    return this.sDocumentType;
  }

  /**
   * RefundAmount
   * @return Currency
   */
  public void setRefundAmount(ArmCurrency sValue) {
    if (sValue == null)
      return;
    doSetRefundAmount(sValue);
  }

  /**
   * RefundAmount
   * @param sValue Currency
   */
  public void doSetRefundAmount(ArmCurrency sValue) {
    this.amtRefund = sValue;
  }

  /**
   * RefundAmount
   * @return Currency
   */
  public ArmCurrency getRefundAmount() {
    return this.amtRefund;
  }

  /**
   * VATNumber
   * @return String
   */
  public void setVATNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetVATNumber(sValue);
  }

  /**
   * VATNumber
   * @param sValue String
   */
  public void doSetVATNumber(String sValue) {
    this.sVatNum = sValue;
  }

  /**
   * VATNumber
   * @return String
   */
  public String getVATNumber() {
    return this.sVatNum;
  }

  public ArmCurrency getCompositePreVATAmount() {
    POSLineItem[] lineItems = this.getLineItemsArray();
    ArmCurrency totalPreVATAmount = new ArmCurrency(0.0d);
    try {
      for (int index = 0; index < lineItems.length; index++) {
        POSLineItem lineItem = lineItems[index];
        if (lineItem instanceof CMSNoSaleLineItem) {
          totalPreVATAmount = totalPreVATAmount.add(lineItem.getExtendedNetAmount().subtract(lineItem.
            getExtendedVatAmount()));
        } else if (lineItem instanceof CMSNoReturnLineItem) {
          totalPreVATAmount = totalPreVATAmount.subtract(lineItem.getExtendedNetAmount().subtract(lineItem.
            getExtendedVatAmount()));
        } else if (lineItem instanceof CMSSaleLineItem) {
          totalPreVATAmount = totalPreVATAmount.add(lineItem.getExtendedNetAmount().subtract(lineItem.
            getExtendedVatAmount()));
        } else if (lineItem instanceof CMSReservationLineItem) {
          totalPreVATAmount = totalPreVATAmount.add(lineItem.getExtendedNetAmount().subtract(lineItem.
            getExtendedVatAmount()));
        } else if (lineItem instanceof CMSConsignmentLineItem) {
          totalPreVATAmount = totalPreVATAmount.add(lineItem.getExtendedNetAmount().subtract(lineItem.
            getExtendedVatAmount()));
        } else if (lineItem instanceof CMSPresaleLineItem) {
          totalPreVATAmount = totalPreVATAmount.add(lineItem.getExtendedNetAmount().subtract(lineItem.
            getExtendedVatAmount()));
        } else if (lineItem instanceof CMSReturnLineItem) {
          totalPreVATAmount = totalPreVATAmount.subtract(lineItem.getExtendedNetAmount().subtract(lineItem.
            getExtendedVatAmount()));
        }
      }
    } catch (CurrencyException anException) {
      logCurrencyException("getPreVATAmount", anException);
    }
    return totalPreVATAmount;
  }

  public ArmCurrency getCompositeNetAmount() {
    POSLineItem[] lineItems = this.getLineItemsArray();
    ArmCurrency totalNetAmount = new ArmCurrency(0.0d);
    try {
      for (int index = 0; index < lineItems.length; index++) {
        POSLineItem lineItem = lineItems[index];
        if (lineItem instanceof CMSNoSaleLineItem) {
          totalNetAmount = totalNetAmount.add(lineItem.getExtendedNetAmount());
        } else if (lineItem instanceof CMSNoReturnLineItem) {
          totalNetAmount = totalNetAmount.subtract(lineItem.getExtendedNetAmount());
        } else if (lineItem instanceof CMSSaleLineItem) {
          totalNetAmount = totalNetAmount.add(lineItem.getExtendedNetAmount());
        } else if (lineItem instanceof CMSReservationLineItem) {
          totalNetAmount = totalNetAmount.add(lineItem.getExtendedNetAmount());
        } else if (lineItem instanceof CMSConsignmentLineItem) {
          totalNetAmount = totalNetAmount.add(lineItem.getExtendedNetAmount());
        } else if (lineItem instanceof CMSPresaleLineItem) {
          totalNetAmount = totalNetAmount.add(lineItem.getExtendedNetAmount());
        } else if (lineItem instanceof CMSReturnLineItem) {
          totalNetAmount = totalNetAmount.subtract(lineItem.getExtendedNetAmount());
        }

      }
    } catch (CurrencyException anException) {
      logCurrencyException("getCompositeNetAmount", anException);
    }
    return totalNetAmount;
  }

  public String getMasterRegister() {
      return this.masterRegister;
  }

  public void setIsCreditNoteCountry() {
      this.isCreditNoteCountry = true;
  }

  public boolean isCreditNoteCountry() {
      return this.isCreditNoteCountry;
  }

  public void setIsDocNumAssigned() {
      this.isDocumentNumberAssigned = true;
  }

  public boolean isDocNumAssigned() {
      return this.isDocumentNumberAssigned;
  }

  public void setRegister(CMSRegister register) {
      this.register = register;
  }

  public CMSRegister getRegister() {
      return this.register;
  }

  /**
   *
   * @param fiscalDate Date
   */
  public void setFiscalDate (Date fiscalDate) {
    if (fiscalDate == null)
      return;
    doSetFiscalDate(fiscalDate);
  }

  /**
   *
   * @param fiscalDate Date
   */
  public void doSetFiscalDate (Date fiscalDate) {
    this.fiscalDate = fiscalDate;
  }

  /**
   *
   * @return Date
   */
  public Date getFiscalDate () {
    return  this.fiscalDate;
  }


}


