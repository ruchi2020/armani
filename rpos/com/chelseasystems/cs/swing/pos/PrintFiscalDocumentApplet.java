/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cs.fiscaldocument.*;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.FiscalDocumentUtil;
import com.chelseasystems.cs.xml.TaxFreeXML;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cs.pos.DocumentResponse;
import com.armani.reports.FiscalDocumentEvent;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.armani.business.rules.ARMCustomerBR;
import com.ga.fs.fsbridge.ARMFSBridge;
import com.ga.fs.fsbridge.object.ARMFSBOInvoiceTransactionObject;
import com.ga.fs.fsbridge.object.ARMFSBridgeObject;
import com.armani.business.rules.ARMCustomerBR;
import java.text.Normalizer;

/**
 * <p>Title:PrintFiscalDocumentApplet </p>
 * <p>Description:PrintFiscalDocumentApplet </p>
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
public class PrintFiscalDocumentApplet extends CMSApplet implements FiscalDocumentEvent {
  private PrintFiscalDocumentCustPanel pnlCustInfo;
  private DDTDetailsPanel pnlDDTDetails;
  private RolodexLayout cardLayout;
  private CMSCustomer cmsCustomer;
  private POSHeaderPanel posHeaderPanel;
  private CMSCompositePOSTransaction theTxn;
  private JCMSLabel lblRetail;
  private JCMSLabel lblRetailValue;
  private JCMSLabel lblMkdn;
  private JCMSLabel lblMkdnValue;
  private JCMSLabel lblTax;
  private JCMSLabel lblTaxValue;
  private JCMSLabel lblTotal;
  private JCMSLabel lblTotalValue;
  private JCMSLabel lblDocumentType;
  private JCMSLabel lblDocumentNumber;
  private JCMSLabel lblAmtTendered;
  private JCMSLabel lblAmtDue;
  private JCMSLabel lblAmtTenderedValue;
  private JCMSLabel lblAmtDueValue;
  private String sFiscalString;
  private JPanel pnlSelectItems;
  private FiscalItemsPanel pnlFiscalItems;
  private boolean bDDTDetailsAvailable;
  private FiscalDocument prevFiscalDocument;
  private int iFiscalMode;
  private Component compTabEvent;
  public static final int MODE_DDT = 1;
  public static final int MODE_TAX_FREE = 2;
  public static final int MODE_VAT_INVOICE = 3;
  public static final int MODE_CREDIT_NOTE = 4;
  private FiscalDocumentUtil fiscalDocUtil = null;
  private Hashtable hFiscalDocuments = null;
  private boolean isPrintComplete = true;
  public static CMSCompositePOSTransaction globalGuiTransaction;

  /**
   * put your documentation comment here
   */
  public void start() {
    fiscalDocUtil = new FiscalDocumentUtil();
    hFiscalDocuments = new Hashtable();
    bDDTDetailsAvailable = false;
    cmsCustomer = null;
    pnlCustInfo.reset();
    pnlDDTDetails.reset();
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");

    if (theAppMgr.getStateObject("ARM_TXN_HIST_FISCAL_TXN") != null)
      theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("ARM_TXN_HIST_FISCAL_TXN");
    iFiscalMode = 2;
    //Customer from ChangeAddress
    if (theAppMgr.getStateObject("ARM_FISCAL_CUSTOMER") != null) {
      cmsCustomer = (CMSCustomer)theAppMgr.getStateObject("ARM_FISCAL_CUSTOMER");
    }
    //Customer from LookUp.
    else if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
      cmsCustomer = (CMSCustomer)theAppMgr.getStateObject("TXN_CUSTOMER");
    }
    //Transaction Customer
    else if (theTxn.getCustomer() != null) {
      cmsCustomer = (CMSCustomer)theTxn.getCustomer();
    }
    if (theAppMgr.getStateObject("CUSTOMER_SUBMIT") != null) {
      cmsCustomer = (CMSCustomer)theAppMgr.getStateObject("CUSTOMER_SUBMIT");
    }
    FiscalDocument[] fiscalDocuments = theTxn.getFiscalDocumentArray();
    if (fiscalDocuments != null) {
      for (int index = 0; index < theTxn.getFiscalDocumentArray().length; index++) {
        if (!hFiscalDocuments.containsKey(fiscalDocuments[index].getDocumentType())) {
          hFiscalDocuments.put(fiscalDocuments[index].getDocumentType(), fiscalDocuments[index]);
        }
      }
    }
    pnlCustInfo.setCompanyCode(((CMSStore)theStore).getCompanyCode());
    pnlCustInfo.setStoreCode(((CMSStore)theStore).getShopCode());
    pnlCustInfo.setRegisterID(((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId());
    pnlCustInfo.setFiscalReceiptNumber(theTxn.getFiscalReceiptNumber());
    pnlCustInfo.setFiscalReceiptDate(theTxn.getFiscalReceiptDate());
    if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null) {
      iFiscalMode = ((Integer)theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE")).intValue();
    }

    sFiscalString = res.getString("Fiscal") + "[";
    switch (iFiscalMode) {
      case MODE_DDT:
        bDDTDetailsAvailable = true;
        sFiscalString += res.getString("DDT") + "]" + res.getString("No") + ":";
        this.prevFiscalDocument = (DDT)hFiscalDocuments.get("DD");
        break;
      case MODE_TAX_FREE:
          pnlCustInfo.setEnabled(false);
          bDDTDetailsAvailable = false;
          sFiscalString += res.getString("Tax") + "]" + res.getString("No") + ":";
          this.prevFiscalDocument = (TaxFree) hFiscalDocuments.get("TF");
          break;
      case MODE_VAT_INVOICE:
          pnlCustInfo.setEnabled(false);
          bDDTDetailsAvailable = false;
          sFiscalString = res.getString("VAT Invoice No") + ":";
          this.prevFiscalDocument = (VATInvoice) hFiscalDocuments.get("VI");
          break;
      case MODE_CREDIT_NOTE:
          pnlCustInfo.setEnabled(false);
          pnlCustInfo.setEnabled(false);
          bDDTDetailsAvailable = false;
          sFiscalString += res.getString("Credit Note") + "]" +
                  res.getString("No") + ":";
          this.prevFiscalDocument = (CreditNote) hFiscalDocuments.get("CN");
          break;
      default:
        pnlCustInfo.setEnabled(false);
        bDDTDetailsAvailable = false;
        sFiscalString += res.getString("DDT/Tax/Credit Note") + "]" + res.getString("No") + ":";
        break;
    }
    if (this.prevFiscalDocument != null && this.prevFiscalDocument.getDocumentNumber() != null){
      pnlCustInfo.setEnabled(false);
    }
    else if (ARMCustomerBR.isDummy(cmsCustomer.getId())|| ARMCustomerBR.isDefault(cmsCustomer.getId())){
      pnlCustInfo.setEnabled(true);
      pnlCustInfo.requestFocusTo(pnlCustInfo.COMPANY_NAME1);
    }
    if (cmsCustomer != null) {
      cmsCustomer.setIsUpdateAllStgTbl(true);
      theTxn.doSetCustomer(cmsCustomer);
      pnlCustInfo.setCompanyName1(cmsCustomer.getLastName());
      pnlCustInfo.setCompanyName2(cmsCustomer.getFirstName());
      if (theAppMgr.getStateObject("ARM_FISCAL_ADDRESS") != null)
        pnlCustInfo.setAddress((Address)theAppMgr.getStateObject("ARM_FISCAL_ADDRESS"));
      else {
        Address tmpAddress = null;
        if (cmsCustomer != null && this.prevFiscalDocument == null) {
          tmpAddress = cmsCustomer.getPrimaryAddress();
          pnlCustInfo.setAddress(tmpAddress);
        } else if (this.prevFiscalDocument != null && this.prevFiscalDocument.getDocumentNumber().trim().length() > 0){
          tmpAddress = new Address();
          if (this.prevFiscalDocument.getCompanyName() != null)
        	  pnlCustInfo.setCompanyName1(this.prevFiscalDocument.getCompanyName());
          if (this.prevFiscalDocument.getCompanyName2() != null)
        	  pnlCustInfo.setCompanyName2(this.prevFiscalDocument.getCompanyName2());
          tmpAddress.setAddressLine1(this.prevFiscalDocument.getAddressLine1());
          if (this.prevFiscalDocument.getAddressLine2() != null)
        	  tmpAddress.setAddressLine2(this.prevFiscalDocument.getAddressLine2());
          tmpAddress.setCity(this.prevFiscalDocument.getCity());
          tmpAddress.setCountry(this.prevFiscalDocument.getCountry());
          tmpAddress.setZipCode(this.prevFiscalDocument.getZipCode());
          if (this.prevFiscalDocument.getCounty() != null)
        	  tmpAddress.setState(this.prevFiscalDocument.getCounty());
          pnlCustInfo.setAddress(tmpAddress);
        }
        if (theAppMgr.getStateObject("ARM_FISCAL_CUSTOMER") == null)
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              theAppMgr.addStateObject("ARM_FISCAL_CUSTOMER", cmsCustomer);
              //if (cmsCustomer.getPrimaryAddress().getAddressFormat() != null
              //    && cmsCustomer.getPrimaryAddress().getAddressFormat().toUpperCase().indexOf(
              //    "EUROPE") >= 0) {
              //  pnlCustInfo.setAddress(cmsCustomer.getPrimaryAddress());
              //} else {
              //  System.out.println("cmsCustomer.getPrimaryAddress().getAddressFormat() : " + cmsCustomer.getPrimaryAddress().getAddressFormat());
              //  System.out.println("cmsCustomer.getLastName() : " + cmsCustomer.getLastName());
              //  theAppMgr.showErrorDlg(res.getString(
              //      "Please select an EUROPE address format to proceed"));
              //  theAppMgr.fireButtonEvent("CHANGE_FISCAL_ADDRESS");
              //}
            }
          });
        else
          theAppMgr.removeStateObject("ARM_FISCAL_CUSTOMER");
      }
    }

    theAppMgr.addStateObject("CUST_MGMT_MODE", "MODIFY");
    initMainBtns();
  }

//  public String debugAddress(Address address){
//    return address.getAddressLine1() + " " + address.getAddressLine2() + " " + address.getZipCode() + " " + address.getCity();
//  }

  /**
   * put your documentation comment here
   */
  private void resetGUI() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            pnlCustInfo.reset();
            pnlDDTDetails.reset();
          }
        });
      }
    });
  }


  /**
   * put your documentation comment here
   */
  private void initMainBtns() {
    theAppMgr.showMenu(MenuConst.FISCAL_CUST_DETAILS, theOpr);
    if (bDDTDetailsAvailable)
      selectDDT();
    else
      selectOption();
    cardLayout.show(this, "CUSTOMER_INFO");
  }


  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.3 $");
  }


  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return res.getString("Print Fiscal Doc");
  }

  private class FocusTraverseListener implements AWTEventListener {

    /**
     * put your documentation comment here
     * @param awtEvent
     */
    public void eventDispatched(AWTEvent awtEvent) {
      KeyEvent keyEvent;
      if (!bDDTDetailsAvailable)
        return;
      try {
        // Typecast to KeyEvent
        keyEvent = (KeyEvent)awtEvent;
      } catch (ClassCastException ce) {
        //  ce.printStackTrace();
        return;
      }
      // Check If its TAB event.
      if (keyEvent.getKeyCode() == KeyEvent.VK_TAB) {
        // JCMSTextField is source
        if (awtEvent.getSource() instanceof JCMSTextField) {
          compTabEvent = (Component)awtEvent.getSource();
          if (compTabEvent.getName().trim().indexOf("PHONE") != -1 && keyEvent.getModifiers() == 0) {
            invokeFocusThread();
            return;
          }
        }
        if (awtEvent.getSource() instanceof JCMSComboBox
            && keyEvent.getModifiers() == KeyEvent.SHIFT_MASK) {
          compTabEvent = (Component)awtEvent.getSource();
          if (compTabEvent.getName().indexOf("DESTINATION_CODE") != -1) {
            invokeFocusThread();
            return;
          }
        }
      }
    }
  }


  /**
   * Invoke thread to switch panels.
   */
  private void invokeFocusThread() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            switchPanel();
          }
        });
      }
    });
  }


  /**
   * Switch panels
   */
  private void switchPanel() {
    // Check if TAB event was result of
    // focus lost not focus gain.
    if (!compTabEvent.hasFocus()) {
      if (compTabEvent.getName().trim().equals("DESTINATION_CODE")) {
        cardLayout.first(this);
        this.pnlCustInfo.requestFocusTo(pnlCustInfo.PHONE);
      } else if (compTabEvent.getName().trim().equals("PHONE1")) {
        cardLayout.next(this);
        pnlDDTDetails.requestFocusToDestinationCode();
      }
    }
  }


  /**
   * put your documentation comment here
   * @param e
   */
  public void pageDown(MouseEvent e) {
    if (!bDDTDetailsAvailable && cardLayout.getCurrent(this) != pnlSelectItems)
      return;
    if (cardLayout.getCurrent(this) == pnlSelectItems) {
      int selectedRow = pnlFiscalItems.getModel().getAllRows().indexOf(pnlFiscalItems.getSelectedLineItem());
      if (selectedRow < 0)
        selectedRow = pnlFiscalItems.getModel().getLastSelectedItemRow();
      pnlFiscalItems.nextPage();
      theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlFiscalItems).getCurrentPageNumber() + 1
          , ((PageNumberGetter)pnlFiscalItems).getTotalPages());

      return;
    }
    if (cardLayout.getCurrent(this) == pnlDDTDetails)
      return;
    selectCustomerDetails();
    cardLayout.next(this);
  }


  /**
   * put your documentation comment here
   * @param e
   */
  public void pageUp(MouseEvent e) {
    if (!bDDTDetailsAvailable && cardLayout.getCurrent(this) != pnlSelectItems)
      return;
    if (cardLayout.getCurrent(this) == pnlSelectItems) {
      int selectedRow = pnlFiscalItems.getModel().getAllRows().indexOf(pnlFiscalItems.getSelectedLineItem());
      if (selectedRow < 0)
        selectedRow = pnlFiscalItems.getModel().getLastSelectedItemRow();
      pnlFiscalItems.prevPage();
      theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlFiscalItems).getCurrentPageNumber() + 1
          , ((PageNumberGetter)pnlFiscalItems).getTotalPages());

      return;
    }
    if (cardLayout.getCurrent(this) == pnlCustInfo)
      return;
    selectDDT();
    cardLayout.previous(this);
  }


  /**
   * put your documentation comment here
   */
  private void selectOption() {
    theAppMgr.setSingleEditArea(res.getString("Select option"));
  }


  /**
   * put your documentation comment here
   */
  private void selectDDT() {
    theAppMgr.setSingleEditArea(res.getString(
        "Select option or press Page Down to view DDT Details"));
  }


  /**
   * put your documentation comment here
   */
  private void selectCustomerDetails() {
    theAppMgr.setSingleEditArea(res.getString(
        "Select option or press Page Up to view Customer Details"));
  }


  /**
   * put your documentation comment here
   * @param sHeader
   * @param anEvent
   */
  public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
    if(!isPrintComplete) {
      theAppMgr.showErrorDlg(res.getString("Fiscal Document Print Window Open!"));
      anEvent.consume();
      return;
    }
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PREV")) {
      // Clear the Fiscal Document from TXN
      try {
        theTxn.setCurrFiscalDocument(null);
      } catch (BusinessRuleException ex) {
      }
      if (sHeader.equals("SELECT_ITEMS")) {
    	this.remove(pnlSelectItems);
        cardLayout.show(this, "CUSTOMER_INFO");
        initMainBtns();
        anEvent.consume();
      }
    } else if (sAction.equals("PRINT")) {
      if (completeAttributes())
        if (this.prevFiscalDocument != null)
          printFiscalDocument(this.prevFiscalDocument);
        else if (theTxn.getCurrFiscalDocument() != null) {
//          printFiscalDocument((FiscalDocument)theAppMgr.getStateObject("PRINT_FISCAL_DOCUMENT"));
          printFiscalDocument(theTxn.getCurrFiscalDocument());
        } else {
          printFiscalDocument(null);
        }

    } else if (sAction.equals("MODIFY_FISCAL_NUM")) {
      anEvent.consume();
      theAppMgr.showMenu(MenuConst.MODIFY_FISCAL, "MODIFY_FISCAL", theOpr);
    } else if (sAction.equals("MODIFY_DDT_NO")) {
      anEvent.consume();
      selectModifyDDT();
    } else if (sAction.equals("MODIFY_TAX_NO")) {
      anEvent.consume();
      selectModifyTaxNumber();
    } else if (sAction.equals("MODIFY_CREDIT_NOTE")) {
      anEvent.consume();
      selectModifyCreditNote();
    }

  }


  /**
   * put your documentation comment here
   * @return
   */
  private boolean isDocumentReadyForPrinting() {
    if (pnlCustInfo.getCompanyName1().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Company name required"));
      pnlCustInfo.requestFocusTo(pnlCustInfo.COMPANY_NAME1);
      return false;
    }
    if (pnlCustInfo.getCompanyName2().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Company name2 required"));
      pnlCustInfo.requestFocusTo(pnlCustInfo.COMPANY_NAME2);
      return false;
    }
    Address custAddress = pnlCustInfo.getAddress();
    if (custAddress == null) {
      theAppMgr.showErrorDlg(res.getString("Address information required"));
      pnlCustInfo.requestFocusTo(pnlCustInfo.ADDRESS_LINE1);
      return false;
    }
    if (cmsCustomer == null
        && (iFiscalMode == MODE_VAT_INVOICE || iFiscalMode == MODE_TAX_FREE
        || iFiscalMode == MODE_CREDIT_NOTE)) {
      theAppMgr.showErrorDlg(res.getString("Customer is required to continue"));
      return false;
    }
    if (!bDDTDetailsAvailable)
      return true;
    if (custAddress.getCountry() == null || custAddress.getCountry().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Country required"));
      pnlCustInfo.requestFocusTo(pnlCustInfo.COUNTRY);
      return false;
    }
    if (custAddress.getAddressLine1() == null || custAddress.getAddressLine1().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Address Line1 required"));
      pnlCustInfo.requestFocusTo(pnlCustInfo.ADDRESS_LINE1);
      return false;
    }
    if (custAddress.getZipCode() == null || custAddress.getZipCode().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Zip code required"));
      pnlCustInfo.requestFocusTo(pnlCustInfo.ZIPCODE);
      return false;
    }
    if (custAddress.getCity() == null || custAddress.getCity().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("City required"));
      pnlCustInfo.requestFocusTo(pnlCustInfo.CITY);
      return false;
    }
//    if (custAddress.getState() == null || custAddress.getState().length() < 1) {
//      theAppMgr.showErrorDlg(res.getString("State required"));
//      pnlCustInfo.requestFocusTo(pnlCustInfo.STATE);
//      return false;
//    }
    if (pnlDDTDetails.getWeight().length() > 0) {
      try {
        Double dWeight = new Double(pnlDDTDetails.getWeight());
      } catch (Exception e) {
        theAppMgr.showErrorDlg(res.getString("Weight should be numeric value"));
        return false;
      }
    }
    return true;
  }


  /**
   * put your documentation comment here
   * @return
   */
  private boolean completeAttributes() {
    if (pnlFiscalItems.getSelectedLineItems() == null
        || pnlFiscalItems.getSelectedLineItems().length < 1) {
      theAppMgr.showErrorDlg(res.getString("A line item should be selected to print"));
      return false;
    }
    return true;
  }


  /**
   * put your documentation comment here
   */
  private void printFiscalDocument(FiscalDocument fiscDoct) {
    //Sergio
    int mode_type = -1;

    FiscalDocument fiscDoc = null;
    try {
      if (fiscDoct == null) {
        switch (iFiscalMode) {
          case MODE_DDT:
            fiscDoc = getDocumentForDDT();
            break;
          case MODE_TAX_FREE:
            fiscDoc = getDocumentForTaxFree();
            break;
          case MODE_VAT_INVOICE:
            fiscDoc = getDocumentForVATInvoice();
            break;
          case MODE_CREDIT_NOTE:
            fiscDoc = getDocumentForCreditNote();
            break;
        }
        Address address = pnlCustInfo.getAddress();
        CMSRegister register = (CMSRegister)theAppMgr.getGlobalObject("REGISTER");
        System.out.println("issue #1920 in PrintFiscalDocument setting the register id  "+register.getId());
        fiscDoc.setRegister(register);
  
        /* Patch - Added by Yves Agbessi (15-Nov-2017)	
         * 
         * FIX : Handles the cases where non-ASCII characters are in the customer's info
         * LOGIC : the characters are normalized and substitued with similar ASCII characters
         * METHOD :Action performed by the method normalizeAndSubstitue(String s)
         * 
         * */
        
        
        pnlCustInfo.setCompanyName1(normalizeAndSubstitue(pnlCustInfo.getCompanyName1()));
        theTxn.getCustomer().doSetLastName(pnlCustInfo.getCompanyName1());
        //System.out.println("COMPANY NAME 1 : " + pnlCustInfo.getCompanyName1());
        pnlCustInfo.setCompanyName2(normalizeAndSubstitue(pnlCustInfo.getCompanyName2()));
        theTxn.getCustomer().doSetFirstName(pnlCustInfo.getCompanyName2());
        //System.out.println("COMPANY NAME 2 : " + pnlCustInfo.getCompanyName2());
        fiscDoc.setCompanyName(pnlCustInfo.getCompanyName1()); 
        //System.out.println(fiscDoc.getCompanyName());
        fiscDoc.setCompanyName2(pnlCustInfo.getCompanyName2());
        
        if (address != null) {
        	
        	 address.setAddressLine1(normalizeAndSubstitue(address.getAddressLine1()));
        	 fiscDoc.setAddressLine1(address.getAddressLine1());

        	 address.setAddressLine2(normalizeAndSubstitue(address.getAddressLine2()));
        	 fiscDoc.setAddressLine2(address.getAddressLine2());
        	 //theTxn.getCustomer().setAddress(address.getAddressLine2());
        	 //System.out.println("AD 2 : " + theTxn.getCustomer().getAddress() );
        	 
        	 address.setCity(normalizeAndSubstitue(address.getCity()));
        	 fiscDoc.setCity(address.getCity());
        	 theTxn.getCustomer().setCity(address.getCity());
        	 
        	 address.setState(normalizeAndSubstitue(address.getState()));
        	 fiscDoc.setCounty(address.getState());
        	 theTxn.getCustomer().setState(address.getState());
        	 
        	 address.setCountry(normalizeAndSubstitue(address.getCountry()));
        	 fiscDoc.setCountry(address.getCountry());
        	 theTxn.getCustomer().setCountry(address.getCountry());
        	 
        	 if (! address.getZipCode().trim().isEmpty())
        	 { 
        		 //!str.trim().isEmpty()
        	 address.setZipCode(normalizeAndSubstitue(address.getZipCode()));
        	 fiscDoc.setZipCode(address.getZipCode());
        	 theTxn.getCustomer().setZipCode(address.getZipCode());
        	 }
        	 
        	 ((CMSCustomer)theTxn.getCustomer()).getAddresses().clear();
        	 ((CMSCustomer)theTxn.getCustomer()).getAddresses().add(address);
          
          /**
           * End of Yves Agbessi Patch
           * 
           * **/
          
        }
        fiscDoc.setTxn(theTxn);
        System.out.println(theTxn.getCustomer().getLastName() + ", " + theTxn.getCustomer().getFirstName());
        theTxn.setCurrFiscalDocument(fiscDoc);
        POSLineItem fiscalItems[] = pnlFiscalItems.getSelectedLineItems();
        for (int iCtr = 0; iCtr < fiscalItems.length; iCtr++) {
          // theAppMgr.showErrorDlg(fiscalItems[iCtr].getExtendedVatAmount().formattedStringValue());
          fiscDoc.addLineItem(fiscalItems[iCtr]);
        }

      } else {
        fiscDoc = fiscDoct;
        theAppMgr.removeStateObject("PRINT_FISCAL_DOCUMENT");
      }

      ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
      String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
      Class cls = Class.forName(sClassName);
      Vector taxFreeXMLObjs = new TaxFreeXML().getObjectsFromFile(FileMgr.getLocalFile("xml"
          , "tax_free.xml"));
      // System.out.println("vector>>>" +taxFreeXMLObjs.size());
      ArrayList listTaxFreeObjs = new ArrayList();
      for (int iCtr = 0; iCtr < taxFreeXMLObjs.size(); iCtr++) {
        listTaxFreeObjs.add(taxFreeXMLObjs.elementAt(iCtr));
        // System.out.println("TaxFree::::: " + ((TaxFree)taxFreeXMLObjs.elementAt(iCtr)).getStoreCode());
      }
      FiscalInterface fiscalPrinterInterface = (FiscalInterface)cls.newInstance();
      fiscalPrinterInterface.setModeType(iFiscalMode);
      isPrintComplete = false;
	/*
       * 
       * Patch added by Yves Agbessi (15-Nov-2017)
       * FIX : This patch avoids the application to freeze when errors are thrown during the fiscal document generation
       * 
       * LOGIC : Catches the return value from the method fiscalPrinterInterface.printFiscalDocument(this, fiscDoc,listTaxFreeOBjs)
       * 
       * */
      
		globalGuiTransaction = theTxn;
      int retVal = fiscalPrinterInterface.printFiscalDocument(this, fiscDoc, listTaxFreeObjs);
      /*if(retVal==1){
    	  isPrintComplete = true;
          theAppMgr.showErrorDlg(res.getString("Errors generating the fiscal document.\n" +
          		"Please check the customer's data and avoid non-accepted characters!\n" +
          		"Example of non-accepted characters\n\n .,-()^/°*çäöüß"));

      }*/
      
      if(retVal==2){
    	  isPrintComplete = true;
          theAppMgr.showErrorDlg(res.getString("Errors generating the fiscal document.\n" +
          		"Reports configuration files may be missing!\n" +
          		"Please call the Help Desk!"));

      }
      
      if(retVal==3){
    	  isPrintComplete = true;
          theAppMgr.showErrorDlg(res.getString("Errors generating the fiscal document.\n" +
          		"Errors parsing the xml file\n" +
          		"Please double check the transaction and the customer's data!" +
          		""));

      }
      if(retVal==4){
    	  isPrintComplete = true;
          theAppMgr.showErrorDlg(res.getString("Errors generating the fiscal document.\n" +
          		"Errors executing internal query to retrieve data : some files may be missing" +
          		"Please call the Help Desk! " +
          		""));

      }
      
      
      /*
       * End of Yves Agbessi Patch 
       * 
       * */
    } catch (Exception e) {
      isPrintComplete = true;
      theAppMgr.showErrorDlg(res.getString("Unable to print the Fiscal Document, contact Technical support"));
      e.printStackTrace();
    }
  }


  /**
   * put your documentation comment here
   * @param fiscDoc
   */
  private void setDocumentNumberForLineItems(FiscalDocument fiscDoc) {
    POSLineItem lineItems[] = fiscDoc.getLineItemsArray();
    if (lineItems == null)
      return;
    for (int iCtr = 0; iCtr < lineItems.length; iCtr++) {
      if (lineItems[iCtr] instanceof CMSNoSaleLineItem) {
//        try {
        ((CMSNoSaleLineItem)lineItems[iCtr]).addFiscalDocument(fiscDoc);
//        } catch (BusinessRuleException ex) {
//        }
      }
      if (lineItems[iCtr] instanceof CMSNoReturnLineItem) {
//        try {
        ((CMSNoReturnLineItem)lineItems[iCtr]).addFiscalDocument(fiscDoc);
//        } catch (BusinessRuleException ex) {
//        }
      }
      if (lineItems[iCtr] instanceof CMSSaleLineItem) {
//        try {
        ((CMSSaleLineItem)lineItems[iCtr]).addFiscalDocument(fiscDoc);
//        } catch (BusinessRuleException ex) {
//        }
      }
      if (lineItems[iCtr] instanceof CMSConsignmentLineItem) {
//        try {
        ((CMSConsignmentLineItem)lineItems[iCtr]).addFiscalDocument(fiscDoc);
//        } catch (BusinessRuleException ex) {
//        }
      }
      if (lineItems[iCtr] instanceof CMSPresaleLineItem) {
//        try {
        ((CMSPresaleLineItem)lineItems[iCtr]).addFiscalDocument(fiscDoc);
//        } catch (BusinessRuleException ex) {
//        }
      }
      if (lineItems[iCtr] instanceof CMSReservationLineItem) {
//        try {
        ((CMSReservationLineItem)lineItems[iCtr]).addFiscalDocument(fiscDoc);
//        } catch (BusinessRuleException ex) {
//        }
      }

    }
  }


  /**
   * put your documentation comment here
   * @param fiscDoc
   */
  private void submitFiscalDocument(FiscalDocument fiscDoc) {
    try {
      //Post is done in order to attain Offline posting functionality.
      CMSTxnPosterHelper.post(theAppMgr, fiscDoc.getTxn());
    } catch (Exception e) {
      theAppMgr.showErrorDlg(res.getString("Unable to print the Fiscal Document, contact Technical support"));
      e.printStackTrace();
    }
  }


  /**
   * put your documentation comment here
   * @return
   */
  private DDT getDocumentForDDT() {
    DDT ddtDoc = new DDT();
    ddtDoc.setDocumentType(ddtDoc.getCodeForDDTDocumentType());
    ddtDoc.setCarrierCode(pnlDDTDetails.getCarrierCode());
    ddtDoc.setCarrierDesc(pnlDDTDetails.getCarrierDesc());
    ddtDoc.setCarrierType(pnlDDTDetails.getCarrierType());
    ddtDoc.setSender(pnlDDTDetails.getSender());
    ddtDoc.setSenderCode(pnlDDTDetails.getSenderCode());
    ddtDoc.setPackageType(pnlDDTDetails.getPackageType());
    ddtDoc.setGoodsNumber(pnlDDTDetails.getGoodsNumber());
    ddtDoc.setDestinationCode(pnlDDTDetails.getDestinationCode());
    ddtDoc.setExpeditionCode(pnlDDTDetails.getExpeditionCode());
    String sTmp = pnlDDTDetails.getWeight();
    if (sTmp != null && sTmp.length() > 0)
      ddtDoc.setWeight(new Double(sTmp).doubleValue());
    ddtDoc.setNote(pnlDDTDetails.getNotes());
    return ddtDoc;
  }


  /**
   * put your documentation comment here
   * @return
   */
  private TaxFree getDocumentForTaxFree() {
    TaxFree taxFreeDoc = new TaxFree();
    taxFreeDoc.setDocumentType(taxFreeDoc.getCodeForTaxFreeDocumentType());
    taxFreeDoc.setIDType(cmsCustomer.getIdType());
    taxFreeDoc.setIssueDate(cmsCustomer.getIssueDate());
    taxFreeDoc.setPaymentType(cmsCustomer.getPymtType());
    taxFreeDoc.setPlaceOfIssue(cmsCustomer.getPlaceOfIssue());
    taxFreeDoc.setStoreCode(theTxn.getStore().getId());
    return taxFreeDoc;
  }


  /**
   * put your documentation comment here
   * @return
   */
  private VATInvoice getDocumentForVATInvoice() {
    VATInvoice vatInvoice = new VATInvoice();
    vatInvoice.setDocumentType(vatInvoice.getCodeForVatInvoiceDocumentType());
    vatInvoice.setBank(cmsCustomer.getBank());
    vatInvoice.setFiscalCode(cmsCustomer.getFiscalCode());
    vatInvoice.setSupplierPayType(cmsCustomer.getSupplierPymt());
    return vatInvoice;
  }


  /**
   * put your documentation comment here
   * @return
   */
  private CreditNote getDocumentForCreditNote() {
    CreditNote creditNote = new CreditNote();
    creditNote.setDocumentType(creditNote.getCodeForCreditNoteDocumentType());
    creditNote.setBank(cmsCustomer.getBank());
    creditNote.setFiscalCode(cmsCustomer.getFiscalCode());
    creditNote.setSupplierPayType(cmsCustomer.getSupplierPymt());
    return creditNote;
  }

  public void editAreaEvent(String Command, String Value) {
    //if (Command.equals("CREDIT_CARD")) {      // credit card swipe date
    //   String Builder = PaymentMgr.getPaymentBuilder(Command);
    //   System.out.println("the builder is: " + Builder);
    //   theAppMgr.buildObject("PAYMENT", Builder, Value);
    //}
    // above code moved to builder
    if (Command.equals("MODIFY_DDT_NO")) {
      if (!isANumber(Value)) {
        theAppMgr.showErrorDlg(res.getString("DDT number should be a number"));
        selectModifyDDT();
        return;
      }
      this.modifyDDTNumber(Value);
      return;
    }
    if (Command.equals("MODIFY_TAX_NO")) {
      if (!isANumber(Value)) {
        theAppMgr.showErrorDlg(res.getString("Tax number should be a number"));
        selectModifyTaxNumber();
        return;
      }
      modifyTAXNumber(Value);
      return;
    }
    if (Command.equals("MODIFY_CREDIT_NOTE")) {
      if (!isANumber(Value)) {
        theAppMgr.showErrorDlg(res.getString("CreditNote number should be a number"));
        selectModifyCreditNote();
        return;
      }
      modifyCreditNote(Value);
      return;
    }

    if (Command.equals("VAT_COMMENT")) {
    	if(pnlFiscalItems.getSelectedRow()<0){
    		theAppMgr.showErrorDlg("Please select an item");
    		theAppMgr.setSingleEditArea(res.getString("Please enter the VAT comment"), "VAT_COMMENT");
    	}else{
        	pnlFiscalItems.updateComment(Value);
        	pnlFiscalItems.repaint();
        	if(pnlFiscalItems.isFiscalDocAlreadyPrinted()){
        		theAppMgr.setSingleEditArea(res.getString("Fiscal document already printed"));
        	}else{
        		theAppMgr.setSingleEditArea(res.getString("Please enter the VAT comment"), "VAT_COMMENT");
        	}
    	}
    	return;
    }

  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    if(!isPrintComplete) {
      theAppMgr.showErrorDlg(res.getString("Fiscal Document Print Window Open!"));
      anEvent.consume();
      return;
    }
    String sAction = anEvent.getActionCommand();

    if (sAction.equals("MOD_CUST")) {
      if (cmsCustomer == null) {
        theAppMgr.showErrorDlg(res.getString("A Customer must exist in order to perform this action"));
        anEvent.consume();
        return;
      } else if (this.prevFiscalDocument != null && this.prevFiscalDocument.getDocumentNumber().trim().length() > 0) {
        theAppMgr.showErrorDlg(res.getString("This Fiscal document is already printed. Hence cannot modify Customer."));
        anEvent.consume();
        return;
      } else if (ARMCustomerBR.isDummy(cmsCustomer.getId())|| ARMCustomerBR.isDefault(cmsCustomer.getId())){
        theAppMgr.showErrorDlg(res.getString("Default and Dummy customer can be modified using the current screen."));
        anEvent.consume();
        return;
      }
      theAppMgr.addStateObject("CUSTOMER_LOOKUP", cmsCustomer);
    } else if (sAction.equals("CUST_LOOKUP")) {
        if (cmsCustomer != null && this.prevFiscalDocument != null && this.prevFiscalDocument.getDocumentNumber().trim().length() > 0) {
          theAppMgr.showErrorDlg(res.getString("This Fiscal document is already printed. Hence cannot select different Customer."));
          anEvent.consume();
          return;
        }
    } else if (sAction.equals("CHANGE_FISCAL_ADDRESS")) {
      if (cmsCustomer == null) {
        theAppMgr.showErrorDlg(res.getString("A Customer must exist in order to perform this action"));
        anEvent.consume();
        return;
      } else if (ARMCustomerBR.isDummy(cmsCustomer.getId())|| ARMCustomerBR.isDefault(cmsCustomer.getId())){
          theAppMgr.showErrorDlg(res.getString("Default and Dummy customer address cannot be changed using the current screen."));
          anEvent.consume();
          return;
      }
      theAppMgr.addStateObject("ARM_FISCAL_CUSTOMER", cmsCustomer);
    } else if (sAction.equals("CLEAR")) {
      cmsCustomer = null;
      pnlCustInfo.clearCustomerDetails();
    } else if (sAction.equals("SELECT_ITEMS")) {
      if (!isDocumentReadyForPrinting()) {
        anEvent.consume();
        return;
      }
      // Set Fiscal Document to Null in the TXN
      try {
        theTxn.setCurrFiscalDocument(null);
      } catch (BusinessRuleException ex) {
      }
      Thread post = new Thread(new Runnable() {

        /**
         * put your documentation comment here
         */
        public void run() {
          theAppMgr.setSingleEditArea("Posting...");
          theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
          displaySelectItemsScreen((((Integer)theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE")).
              intValue() == MODE_VAT_INVOICE));
        }
      });
      post.start();

    } else if (sAction.equals("PREV")) {
      //
      try {
        theTxn.setCurrFiscalDocument(null);
      } catch (BusinessRuleException ex) {
      }
    }

  }

  /**
   * put your documentation comment here
   */
  private void selectModifyDDT() {
    String sTmp = "";
    sTmp = res.getString("Enter new DDT No.") + ";";
    sTmp += res.getString("Current DDT No") + ":";
    sTmp += fiscalDocUtil.getAvailableDDTNumber();
    theAppMgr.setSingleEditArea(sTmp, "MODIFY_DDT_NO");
    theAppMgr.setEditAreaFocus();
  }

  /**
   * put your documentation comment here
   */
  private void selectModifyTaxNumber() {
    String sTmp = "";
    sTmp = res.getString("Enter new Tax No.") + ";";
    sTmp += res.getString("Current Tax No") + ":";
    sTmp += fiscalDocUtil.getAvailableVATNumber();
    theAppMgr.setSingleEditArea(sTmp, "MODIFY_TAX_NO");
    theAppMgr.setEditAreaFocus();
  }

  /**
   * put your documentation comment here
   */
  private void selectModifyCreditNote() {
    String sTmp = "";
    sTmp = res.getString("Enter new CreditNote No.") + ";";
    sTmp += res.getString("Current CreditNote No") + ":";
    sTmp += fiscalDocUtil.getAvailableCreditNoteNumber();
    theAppMgr.setSingleEditArea(sTmp, "MODIFY_CREDIT_NOTE");
    theAppMgr.setEditAreaFocus();
  }

  /**
   * put your documentation comment here
   * @param sInput
   */
  private void modifyDDTNumber(String sInput) {
    try {
      FiscalDocument fiscDoc = theTxn.getCurrFiscalDocument();
      if (fiscDoc != null) {
        fiscDoc.setDocumentNumber(sInput);
        fiscDoc.setIsDocNumAssigned();
        lblDocumentNumber.setText(fiscDoc.getDocumentNumber());
        theAppMgr.addStateObject("PRINT_FISCAL_DOCUMENT", fiscDoc);
        // Call Print Event
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            PrintFiscalDocumentApplet.this.appButtonEvent(""
                , new CMSActionEvent(this, 0, "PRINT", 0));
          }
        });
      } else {
        if (!fiscalDocUtil.setNextDDTNumber(sInput)) {
          theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse.getErrorMessage(fiscalDocUtil.
              getResponseCode())));
          selectModifyDDT();
          return;
        }
        theAppMgr.showErrorDlg(res.getString("DDT number modified to ") + sInput);
      }
    } catch (Exception e) {
      theAppMgr.showErrorDlg(res.getString("Can't modify DDT number, contact technical support"));
    }
    theAppMgr.showMenu(MenuConst.PREV_PRINT, "SELECT_ITEMS", theOpr);
    selectOption();
  }

  /**
   * put your documentation comment here
   * @param sInput
   */
  private void modifyTAXNumber(String sInput) {
    try {
      FiscalDocument fiscDoc = theTxn.getCurrFiscalDocument();
      if (fiscDoc != null) {
        fiscDoc.setIsDocNumAssigned();
        fiscDoc.setDocumentNumber(sInput);
        lblDocumentNumber.setText(fiscDoc.getDocumentNumber());
        theAppMgr.addStateObject("PRINT_FISCAL_DOCUMENT", fiscDoc);
        // Call Print Event
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            PrintFiscalDocumentApplet.this.appButtonEvent(""
                , new CMSActionEvent(this, 0, "PRINT", 0));
          }
        });
      } else {
        if (!fiscalDocUtil.setNextVATNumber(sInput)) {
          theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse.getErrorMessage(fiscalDocUtil.
              getResponseCode())));
          selectModifyTaxNumber();
          return;
        }
        theAppMgr.showErrorDlg(res.getString("Tax number modified to ") + sInput);
      }
    } catch (Exception e) {
      theAppMgr.showErrorDlg(res.getString("Can't modify Tax number, contact technical support"));
    }
    theAppMgr.showMenu(MenuConst.PREV_PRINT, "SELECT_ITEMS", theOpr);
    selectOption();
  }

  /**
   * put your documentation comment here
   * @param sInput
   */
  private void modifyCreditNote(String sInput) {
    try {
      FiscalDocument fiscDoc = theTxn.getCurrFiscalDocument();
      if (fiscDoc != null) {
        fiscDoc.setDocumentNumber(sInput);
        fiscDoc.setIsDocNumAssigned();
        lblDocumentNumber.setText(fiscDoc.getDocumentNumber());
        theAppMgr.addStateObject("PRINT_FISCAL_DOCUMENT", fiscDoc);
        // Call Print Event
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            PrintFiscalDocumentApplet.this.appButtonEvent(""
                , new CMSActionEvent(this, 0, "PRINT", 0));
          }
        });
      } else {
        if (!fiscalDocUtil.setNextCreditNoteNumber(sInput)) {
          theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse.getErrorMessage(fiscalDocUtil.
              getResponseCode())));
          selectModifyCreditNote();
          return;
        }
        theAppMgr.showErrorDlg(res.getString("CreditNote number modified to ") + sInput);
      }
    } catch (Exception e) {
      theAppMgr.showErrorDlg(res.getString(
          "Can't modify CreditNote number, contact technical support"));
    }
    theAppMgr.showMenu(MenuConst.PREV_PRINT, "SELECT_ITEMS", theOpr);
    selectOption();
  }

  /**
   * put your documentation comment here
   * @param sValue
   * @return
   */
  private boolean isANumber(String sValue) {
    String sTmp = "0123456789";
    try {
      for (int iCtr = 0; iCtr < sValue.length(); iCtr++) {
        if (sTmp.indexOf(sValue.substring(iCtr, iCtr)) == -1)
          return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * put your documentation comment here
   */
  public void init() {
    try {
      pnlCustInfo = new PrintFiscalDocumentCustPanel();
      pnlDDTDetails = new DDTDetailsPanel();
      pnlCustInfo.setAppMgr(theAppMgr);
      pnlDDTDetails.setAppMgr(theAppMgr);
      cardLayout = new RolodexLayout();
      this.setLayout(cardLayout);
      this.add(pnlCustInfo, "CUSTOMER_INFO");
      this.add(pnlDDTDetails, "DDT_DETAILS");
      FocusTraverseListener key = new FocusTraverseListener();
      java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(key, AWTEvent.KEY_EVENT_MASK);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * put your documentation comment here
   */
  public void stop() {}


  /**
   * put your documentation comment here
   */
  private void initHeaders() {
    try {
      posHeaderPanel.setAppMgr(theAppMgr);
      CMSPaymentTransactionAppModel theTxnApp = (CMSPaymentTransactionAppModel)((PaymentTransaction)
          theTxn).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      if (theTxnApp.getPaymentTransaction() instanceof CMSMiscCollection) {
        posHeaderPanel.setProperties(cmsCustomer, (CMSEmployee)theOpr, null);
      } else {
        posHeaderPanel.setProperties(cmsCustomer, (CMSEmployee)theOpr
            , (CMSEmployee)theTxn.getConsultant(), (CMSStore)theStore, theTxn.getTaxExemptId()
            , theTxn.getRegionalTaxExemptId());
      }
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }


  /**
   * put your documentation comment here
   * @return
   */
  private String getFiscalNumber() {
    //FiscalDocumentUtil util = new FiscalDocumentUtil();

    String sFiscalNumber = "";
    try {
      switch (iFiscalMode) {
        case MODE_DDT:
          if (this.prevFiscalDocument != null)
            sFiscalNumber = this.prevFiscalDocument.getDocumentNumber();
          else
            sFiscalNumber = fiscalDocUtil.getAvailableDDTNumber();
          break;
        case MODE_TAX_FREE:
        case MODE_VAT_INVOICE:
          if (this.prevFiscalDocument != null)
            sFiscalNumber = this.prevFiscalDocument.getDocumentNumber();
          else
            sFiscalNumber = fiscalDocUtil.getAvailableVATNumber();
          break;
        case MODE_CREDIT_NOTE:
          if (this.prevFiscalDocument != null)
            sFiscalNumber = this.prevFiscalDocument.getDocumentNumber();
          else
            sFiscalNumber = fiscalDocUtil.getAvailableCreditNoteNumber();
          break;
      }
    } catch (Exception ex) {
      sFiscalNumber = "N/A";
    }

    return sFiscalNumber;
  }


  /**
   * put your documentation comment here
   * @param bVATInvoice
   */
  private void displaySelectItemsScreen(boolean bVATInvoice) {
    pnlSelectItems = new JPanel();
    JPanel pnlTenderInfo = new JPanel();
    JPanel pnlHeader = new JPanel();
    String strDocType = new String();
    switch (iFiscalMode) {
      case MODE_DDT:
        strDocType = "DD";
        break;
      case MODE_TAX_FREE:
        strDocType = "TF";
        break;
      case MODE_VAT_INVOICE:
        strDocType = "VI";
        break;
      case MODE_CREDIT_NOTE:
        strDocType = "CN";
        break;
    }

    pnlFiscalItems = new FiscalItemsPanel(bVATInvoice, strDocType, (this.prevFiscalDocument == null));
    JPanel pnlFooter = new JPanel();
    posHeaderPanel = new POSHeaderPanel();
    pnlSelectItems.setBackground(theAppMgr.getBackgroundColor());
    pnlFooter.setBackground(theAppMgr.getBackgroundColor());
    pnlTenderInfo.setBackground(Color.white);
    posHeaderPanel.setAppMgr(theAppMgr);
    pnlFiscalItems.setAppMgr(theAppMgr);
    posHeaderPanel.setPreferredSize(new Dimension(833, 80));
    pnlTenderInfo.setPreferredSize(new Dimension(10, 80));
    pnlFooter.setPreferredSize(new Dimension(833, 40));
    initHeaders();
    pnlTenderInfo.setBorder(BorderFactory.createEtchedBorder());
    lblRetail = new JCMSLabel(res.getString("Retail"));
    lblMkdn = new JCMSLabel(res.getString("Markdown/Discount"));
    lblTax = new JCMSLabel(res.getString("Sales Tax"));
    lblTotal = new JCMSLabel(res.getString("Total"));
    lblAmtTendered = new JCMSLabel(res.getString("Amount Tendered"));
    lblAmtDue = new JCMSLabel(res.getString("Amount Due"));
    lblRetailValue = new JCMSLabel();
    lblMkdnValue = new JCMSLabel();
    lblTaxValue = new JCMSLabel();
    lblTotalValue = new JCMSLabel();
    lblAmtTenderedValue = new JCMSLabel();
    lblAmtDueValue = new JCMSLabel();
    lblDocumentType = new JCMSLabel(sFiscalString);
    lblDocumentNumber = new JCMSLabel(getFiscalNumber());
    lblRetail.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblMkdn.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblTax.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblTotal.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblAmtTendered.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblAmtDue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblRetailValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblMkdnValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblTaxValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblTotalValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblAmtTenderedValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblAmtDueValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblDocumentType.setFont(theAppMgr.getTheme().getMessageFont());
    lblDocumentNumber.setFont(theAppMgr.getTheme().getMessageFont());
    lblRetailValue.setHorizontalAlignment(SwingConstants.RIGHT);
    lblMkdnValue.setHorizontalAlignment(SwingConstants.RIGHT);
    lblTaxValue.setHorizontalAlignment(SwingConstants.RIGHT);
    lblTotalValue.setHorizontalAlignment(SwingConstants.RIGHT);
    lblAmtTenderedValue.setHorizontalAlignment(SwingConstants.RIGHT);
    lblAmtDueValue.setHorizontalAlignment(SwingConstants.RIGHT);
    pnlTenderInfo.setLayout(new GridLayout(4, 4));
    pnlTenderInfo.add(lblRetail);
    pnlTenderInfo.add(lblRetailValue);
    pnlTenderInfo.add(lblMkdn);
    pnlTenderInfo.add(lblMkdnValue);
    //pnlTenderInfo.add(lblTax);
    //pnlTenderInfo.add(lblTaxValue);
    pnlTenderInfo.add(lblTotal);
    pnlTenderInfo.add(lblTotalValue);
    lblRetailValue.setText(theTxn.getCompositeRetailAmount().formattedStringValue());
    lblMkdnValue.setText(theTxn.getCompositeReductionAmount().formattedStringValue());
    lblTaxValue.setText(theTxn.getCompositeTaxAmount().formattedStringValue());
    lblTotalValue.setText(theTxn.getTotalPaymentAmount().formattedStringValue());
    pnlHeader.setLayout(new BorderLayout());
    pnlHeader.add(posHeaderPanel, BorderLayout.NORTH);
    pnlHeader.add(pnlTenderInfo, BorderLayout.CENTER);
    pnlHeader.setPreferredSize(new Dimension(833, 160));
    pnlFooter.setLayout(new GridBagLayout());
    pnlFooter.setBorder(BorderFactory.createEtchedBorder());
    pnlFooter.add(lblAmtTenderedValue
        , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(1, 0, 0, 2), 0, 4));
    pnlFooter.add(lblAmtTendered
        , new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(1, 0, 0, 0), 0, 4));
    pnlFooter.add(lblAmtDue
        , new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 32, 8, 0), 0, 4));
    pnlFooter.add(lblAmtDueValue
        , new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 0, 8, 2), 0, 4));
    pnlFooter.add(lblDocumentType
        , new GridBagConstraints(0, 0, 1, 2, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(3, 6, 0, 0), 6, 5));
    pnlFooter.add(lblDocumentNumber
        , new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 12, 8));
    lblAmtTenderedValue.setText(theTxn.getCompositeTotalAmountDue().formattedStringValue());
    lblAmtDueValue.setText(new ArmCurrency(0.0d).formattedStringValue());
    pnlSelectItems.setLayout(new BorderLayout());
    pnlSelectItems.add(pnlHeader, BorderLayout.NORTH);
    pnlSelectItems.add(pnlFiscalItems, BorderLayout.CENTER);
    pnlSelectItems.add(pnlFooter, BorderLayout.SOUTH);

//    POSLineItem lineItems[] = theTxn.getSaleAndNoSaleLineItemArray();
    POSLineItem lineItems[] = theTxn.getLineItemsArray();
    for (int iCtr = 0; iCtr < lineItems.length; iCtr++) {
      
      // if (iFiscalMode==MODE_VAT_INVOICE){
      if (iFiscalMode==MODE_VAT_INVOICE || iFiscalMode==MODE_CREDIT_NOTE){
        pnlFiscalItems.addLineItem(lineItems[iCtr]);
      }
      else if (!lineItems[iCtr].isMiscItem() || LineItemPOSUtil.isNotOnFileItem(lineItems[iCtr].getItem().getId()))
    	// Add Only Non Misc Items only
        pnlFiscalItems.addLineItem(lineItems[iCtr]);
    }

    if(pnlFiscalItems.getLineItems()==null || pnlFiscalItems.getLineItems().length==0){
    	theAppMgr.showErrorDlg(res.getString("There are no eligible items"));
        theAppMgr.showMenu(MenuConst.FISCAL_CUST_DETAILS, theOpr);
    	return;
    }
    this.add(pnlSelectItems, "SELECT_ITEMS");
    cardLayout.show(this, "SELECT_ITEMS");
    pnlFiscalItems.getModel().firstPage();
    pnlFiscalItems.selectFirstRow();
    pnlFiscalItems.repaint();
//    theAppMgr.setSingleEditArea("Select items to be printed");
    if(pnlFiscalItems.isFiscalDocAlreadyPrinted()){
		theAppMgr.setSingleEditArea(res.getString("Fiscal document already printed"));
	}else{
		theAppMgr.setSingleEditArea(res.getString("Please enter the VAT comment"), "VAT_COMMENT");
	}
        theAppMgr.showMenu(MenuConst.PREV_PRINT, "SELECT_ITEMS", theOpr);
  }


  /* (non Javadoc)
   * @see com.armani.reports.FiscalDocumentEvent#fireResult(boolean, com.chelseasystems.cs.fiscaldocument.FiscalDocumentResponse)
   */
  public void fireResult(boolean result, FiscalDocumentResponse documentResponse) {

    // TODO Stub di metodo generato automaticamente

    isPrintComplete = true;

    try {
      int iResponseCode = documentResponse.getResponseStatusCode();
      FiscalDocument fiscDoc = theTxn.getCurrFiscalDocument();
      switch (iResponseCode) {
        case FiscalDocumentResponse.RESPONSE_SUCCESS:
          if (prevFiscalDocument != null)
            theAppMgr.showErrorDlg(res.getString(documentResponse.getError_message()));
          else {
            fiscDoc.setDocumentNumber(documentResponse.getDocumentNumber());
            fiscDoc.setRefundAmount(documentResponse.getRefundAmount());
            fiscDoc.setIsDocNumAssigned();

            //PCR_72: set the Response Date
            fiscDoc.setFiscalDate(documentResponse.getFiscalDate());
            setDocumentNumberForLineItems(fiscDoc);
            if (fiscDoc instanceof TaxFree) {
              ((TaxFree)fiscDoc).setDetaxCode(documentResponse.getTaxFreeCode());
            }
            theTxn.setCurrFiscalDocument(fiscDoc);
            theTxn.doAddFiscalDocument(fiscDoc);
            submitFiscalDocument(fiscDoc);
            theAppMgr.showErrorDlg(res.getString(documentResponse.getError_message()));
          
        	/*
			 * Code Added by Yves Agbessi (29-Nov-2017) - Handles the
			 * Fiscal Solutions Service posting in case of a Vat Invoice
			 */
			// START --
			ARMFSBridge fsBridge = new ARMFSBridge().build();
			if (fsBridge.isCountryAllowed) {
				if (iFiscalMode == MODE_VAT_INVOICE) {
					ARMFSBOInvoiceTransactionObject invoiceTransactionObject = new ARMFSBOInvoiceTransactionObject(theTxn);
					boolean postingResult = fsBridge.postObject(invoiceTransactionObject);
					if(!postingResult){
						theAppMgr.showErrorDlg("[FS BRIDGE] "
								+ "Unable to post Transaction Object. "
								+ "\n Please call the Help Desk Immediately. Press OK to print the recipt");
						
					}
					else{
					String response = invoiceTransactionObject.processResponse();

					if (response.contains("ERROR")
							|| response.contains("UNABLE")) {
						theAppMgr
								.showErrorDlg("[FS BRIDGE] "
										+ response
										+ "\n Please call the Help Desk Immediately. Press OK to print the recipt");
						((CMSCompositePOSTransaction)theTxn).setDigitalSignature("UNABLE_TO_RETRIEVE_DIGITAL_SIGNATURE");
					} else {
						((CMSCompositePOSTransaction)theTxn).setDigitalSignature(response);
					}
					}
				}
			}
			// END--
			/*
     * 
     * */
          
          
          
          
          }
          break;
        case FiscalDocumentResponse.RESPONSE_TIME_OUT:
          theAppMgr.showErrorDlg(res.getString(documentResponse.getError_message()));
          theTxn.setCurrFiscalDocument(null);
          break;
        case FiscalDocumentResponse.RESPONSE_SYSTEM_ERROR:
          theAppMgr.showErrorDlg(res.getString(documentResponse.getError_message()));
          theTxn.setCurrFiscalDocument(null);
          break;
        case FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER:
          theAppMgr.showErrorDlg(res.getString(documentResponse.getError_message()));

          //theAppMgr.showMenu(MenuConst.MODIFY_FISCAL, "MODIFY_FISCAL", theOpr);

// Eur PCR  02/21
          if (iFiscalMode == MODE_DDT) {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                PrintFiscalDocumentApplet.this.appButtonEvent(""
                    , new CMSActionEvent(this, 0, "MODIFY_DDT_NO", 0));
              }
            });
          } else if (iFiscalMode == MODE_TAX_FREE || iFiscalMode == MODE_VAT_INVOICE) {
            // Call the
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                PrintFiscalDocumentApplet.this.appButtonEvent(""
                    , new CMSActionEvent(this, 0, "MODIFY_TAX_NO", 0));
              }
            });
          } else if (iFiscalMode == MODE_CREDIT_NOTE) {
            // Call the
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                PrintFiscalDocumentApplet.this.appButtonEvent(""
                    , new CMSActionEvent(this, 0, "MODIFY_CREDIT_NOTE", 0));
              }
            });
          }
          return;
        default:
          theAppMgr.showErrorDlg(res.getString(documentResponse.getError_message()));
          theAppMgr.showMenu(MenuConst.MODIFY_FISCAL, "MODIFY_FISCAL", theOpr);
      }
    } catch (Exception e) {
      theAppMgr.showErrorDlg(res.getString("Unable to print the Fiscal Document, contact Technical support"));
      e.printStackTrace();
    }
    theAppMgr.fireButtonEvent("PREV");

  }

  public boolean isHomeAllowed()
  {
    if(!isPrintComplete) {
      theAppMgr.showErrorDlg(res.getString("Fiscal Document Print Window Open!"));
      return false;
    }
    
    /*Added by Yves Agbessi (05-Dec-2017)
	 * Handles the posting of the Sign Off event for Fiscal Solutions Service
	 * START--
	 * */

		ARMFSBridge.postARMSignOffTransaction((CMSEmployee)theOpr);

	/*
	 * --END
	 * */
    
    //else
    return true;
  }

  /* Method added by Yves Agbessi (15-Nov-2017) to remove non-ASCII characters from a given String
   * 
   * */
  private String normalizeAndSubstitue(String s){
	  
	  if(s == null){
		  
		  return null;
	  }
	  
	  if(s.contains(String.valueOf('"'))){
		  
		  s = s.replace(String.valueOf('"'), " ");
	  }
	  
	  int index = s.indexOf('\\');
	  
	  if(index>=0){
		  
		  s = s.replace(String.valueOf('\\'), " ");
		  
	  }
	  
	  char[] cArray = "!&/;,:._-@#°§'?=|+*§%()£^$".toCharArray();
	  for(char c : cArray){
		  //System.out.println(String.valueOf(c));
		  if(s.contains(String.valueOf(c))){

			  s = s.replace(String.valueOf(c), " ");
		  }
	  }
	 
      s = Normalizer.normalize(s, Normalizer.Form.NFD);
      String resultString = s.replaceAll("[^\\x00-\\x7F]", "");	
      //System.out.println("PrintFiscalDocumentApplet.normalizeAndSubstitue : normalized String = " + resultString);
	  
	  return resultString;
	  
  }

}
