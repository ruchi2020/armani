/**
 * <p>Title: SelectItemsApplet </p>
 *
 * <p>Description: Used for PreSales and Consignments </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc</p>
 *
 * @author
 * @version 1.0
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 10   | 10-25-2005 | Manpreet  |     N/A   | applyCustomer()- Make sure PREV wasnt used in|
 |      |            |           |           | CustomerLookUpApplet                         |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 9    | 09-13-2005 | Manpreet  |     956   | Validating  Expiry Date                      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 8    | 08-10-2005 | Vikram    | 638       | Consignment/Pre-Sale ID is set using new     |
 |      |            |           |           | sequence ARM_SEQ_CSG_PRS_ID                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 7    | 07-18-2005 | Vikram    | 330       |Default focus to be on first page.            |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 6    | 06-07-2005 | Vikram    | 65, 66    |Transfer Associate and Customer between sale /|
 |      |            |           |           |Pre-Sale / Consignment                        |
 --------------------------------------------------------------------------------------------
 | 5    | 05-13-2005 | Manpreet  | N/A       |1.Transaction History Specs.                  |
 -------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 04-29-2005 | Pankaja   | N/A       |1.Voided Transaction not allowed for closing  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-29-2005 | Pankaja   | N/A       |1.Resolved the bug related to expiration date |
 --------------------------------------------------------------------------------------------
 | 2    | 04-29-2005 | Pankaja   | N/A       |1.PreSale/Consignment Open Specification      |
 --------------------------------------------------------------------------------------------
 | 1    | 04-13-2005 | Manpreet  | N/A       |1.PreSale/Co4nsignment Open Specification      |
 --------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.item.*;

import javax.swing.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.txnnumber.CMSTransactionNumberHelper;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cs.customer.DepositHistory;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import java.awt.*;
import java.util.Date;
import com.ga.fs.fsbridge.ARMFSBridge;

import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.pos.CMSReservationLineItem;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cs.swing.dlg.ReservationReasonDlg;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Calendar;
/**
 * put your documentation comment here
 */
public class SelectItemsApplet extends CMSApplet {
  private SelectItemsHeaderPanel pnlHeader;
  private SelectItemsListPanel pnlList;
  private JPanel pnlFooter;
  private JCMSLabel jcmslblTranType;
  private JCMSLabel jcmslblExpiry;
  private JCMSLabel jcmslblExpiryValue;
  private JCMSLabel lblDeposit;
  private JCMSLabel lblDepositValue;
  private int iAppletMode;
  private CMSCompositePOSTransaction theTxn;
  private CMSCompositePOSTransaction posTransFound;
  private CMSCustomer txnCustomer;
  private boolean bTransactionSelected = false;
  private ArmCurrency amtDeposit;
  //Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
  private int count = 0;
  private int lineCount = 0; 
  //End	

  private static ConfigMgr config;
  private String fipay_flag;
  /**
   * Initialize
   */
  public void init() {
    try {
      jbinit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Start
   */
  public void start() {
	String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_flag = config.getString("FIPAY_Integration"); 
	
	 //Default value of the flag is Y if its not present in credit_auth.cfg
	if (fipay_flag == null) {
		fipay_flag = "Y";
	}
	
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    jcmslblExpiryValue.setText("");
    posTransFound = null;
    amtDeposit = null;
    if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
      txnCustomer = (CMSCustomer)theAppMgr.getStateObject("TXN_CUSTOMER");
      applyCustomer();
    } else if (theTxn != null && theTxn.getCustomer() != null) {
      txnCustomer = (CMSCustomer)theTxn.getCustomer();
      applyCustomer();
    }
    if (theAppMgr.getStateObject("TXN_MODE") != null)
      iAppletMode = ((Integer)theAppMgr.getStateObject("TXN_MODE")).intValue();
    else
      iAppletMode = InitialSaleApplet.PRE_SALE_CLOSE;
    pnlHeader.setAppMgr(theAppMgr);
    pnlList.setAppMgr(theAppMgr);
    setAppletMode();
    // ========= Commented by MSB (05/13/05)
    // === DONT NEED THIS ANY MORE AFTER IMPLEMENTATION OF
    // === TRANSACTION HISTORY SPEC
    //    // If applet returns back from CustLookUp applet.
    //    // And Presale was already selected before leaving SelectItemsApplet.
    //    if(theAppMgr.getStateObject("ARM_PRE_SALE_FOUND")!=null )
    //    {
    //      posTransFound = (CMSCompositePOSTransaction) theAppMgr.getStateObject("ARM_PRE_SALE_FOUND");
    //      theAppMgr.removeStateObject("ARM_PRE_SALE_FOUND");
    //      pnlHeader.setTransactionID(posTransFound.getId());
    //      populatePOSLineItems();
    //    }
    // If applet returns back
    // and Presale was passed.
    if (theAppMgr.getStateObject("ARM_TXN_SELECTED") != null) {
      posTransFound = (CMSCompositePOSTransaction)theAppMgr.getStateObject("ARM_TXN_SELECTED");
      pnlHeader.setTransactionID(posTransFound.getId());
      populatePOSLineItems();
    }
    if (posTransFound == null) {
      clear();
      doLayout();
    }
    pnlList.resetColumnWidths();
  }

  /**
   * put your documentation comment here
   */
  private void clear() {
    pnlHeader.reset();
    pnlList.clear();
  }

  /**
   * Set Applet Mode to TXN_MODE
   */
  private void setAppletMode() {
    switch (iAppletMode) {
      case InitialSaleApplet.PRE_SALE_CLOSE:
        if (bTransactionSelected)
          showSelectedBtns();
        else
          initPresaleBtns();
        break;
      case InitialSaleApplet.CONSIGNMENT_CLOSE:
        if (bTransactionSelected)
          showSelectedBtns();
        else
          initConsignmentBtns();
        break;
      case InitialSaleApplet.RESERVATIONS_CLOSE:
        pnlList.setReservationCloseHeaders();
        if (bTransactionSelected)
          showSelectedBtns();
        else
          initReservationBtns();
        break;
    }
  }

  /**
   * Get Version of applet
   * @return VersionNum.
   */
  public String getVersion() {
    return ("$Revision: 1.2 $");
  }

  /**
   * Get Screen Name for Applet
   * @return ScreenName
   */
  public String getScreenName() {
    if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE)
      return (res.getString("Pre-Sale Close"));
    else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE)
      return (res.getString("Consignment Close"));
    else
      return (res.getString("Reservation Close"));
  }

  /**
   * Set Expiration Date for transaction
   * @param sValue ExpirationDate
   */
  public void setExpirationDate(String sValue) {
    if (sValue == null || sValue.trim().length() < 1 || sValue == "")
      return;
    jcmslblExpiryValue.setText(sValue.trim());
    Date dtExpiration = null;
    try {
      dtExpiration = com.chelseasystems.cs.util.DateFormatUtil.
          getLocalDateFormat().parse(sValue);
    }
    catch (Exception e) {
      theAppMgr.showErrorDlg(res.getString("Please enter a valid date"));
      enterExpireDate();
    }
    if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE) {
      posTransFound.getPresaleTransaction().setExpirationDate(dtExpiration);
//      posTransFound.getPresaleTransaction().setExpirationDate(new Date(sValue));
      try {
        CMSTransactionPOSHelper.updatePresaleExpirationDate(theAppMgr
            , posTransFound.getPresaleTransaction());
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
    }
    else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
      posTransFound.getReservationTransaction().setExpirationDate(dtExpiration);
//      posTransFound.getReservationTransaction().setExpirationDate(new Date(sValue));
      try {
        CMSTransactionPOSHelper.updateReservationExpirationDate(theAppMgr
            , posTransFound.getReservationTransaction());
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
    }
    else {
      posTransFound.getConsignmentTransaction().setExpirationDate(dtExpiration);
//      posTransFound.getConsignmentTransaction().setExpirationDate(new Date(sValue));
      try {
        CMSTransactionPOSHelper.updateConsignmentExpirationDate(theAppMgr
            , posTransFound.getConsignmentTransaction());
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
    }
  }

  /**
   * Get Expiration Date for transaction
   * @return ExpirationDate
   */
  public Date getExpirationDate() {
    try {
      if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE) {
        return posTransFound.getPresaleTransaction().getExpirationDate();
      }
      else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
        return posTransFound.getReservationTransaction().getExpirationDate();
      }
      else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
        return posTransFound.getConsignmentTransaction().getExpirationDate();
      }
    }
    catch (Exception e) {e.printStackTrace();}
    return null;
  }

  /**
   * AppButton Event
   * @param Header ParentButton
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(String Header, CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("CUST_LOOKUP")) {
      if (posTransFound != null)
        theAppMgr.addStateObject("ARM_TXN_SELECTED", posTransFound);
    } else if (sAction.equals("SELL_RETURN_ITEMS")) {
      bTransactionSelected = false;
      showSellReturnBtns();
    } else if (sAction.equals("EXPIRE_DATE")) {
      enterExpireDate();
    } else if (sAction.equals("SELECT_ALL")) {
      //ks: Toggle select all and de-select items
      if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE)
        theAppMgr.showMenu(MenuConst.CONS_SELL_RETURN_DESELECT_ALL, "ITEMS_SELECTED", theOpr);
      else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE)
        theAppMgr.showMenu(MenuConst.RESERVATION_DESELECT, "ITEMS_SELECTED", theOpr);
      else
        theAppMgr.showMenu(MenuConst.SELL_RETURN, "ITEMS_SELECTED", theOpr);
      pnlList.toggleListSelection();
    } else if (sAction.equals("DE_SELECT_ALL")) {
      //ks: Toggle select all and de-select items
      if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE)
        theAppMgr.showMenu(MenuConst.CONS_SELL_RETURN, "ITEMS_SELECTED", theOpr);
      else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE)
        theAppMgr.showMenu(MenuConst.RESERVATION_SELECT, "ITEMS_SELECTED", theOpr);
      else
        theAppMgr.showMenu(MenuConst.SELL_RETURN_SELECT_ALL, "ITEMS_SELECTED", theOpr);
      pnlList.toggleListSelection();
    } else if (sAction.equals("SELL")) {
      /*if(pnlList.getSelectedLineItem() == null || (!pnlList.isRowChecked()))
       {
       theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
       return;
       }*/
      pnlList.sellSelectedLineItem();
    } else if (sAction.equals("RETURN")) {
    	//Vivek Mishra : Added to restrict the use of return button in case of PRESALE Close for US region as asked by Jason on 11-DEC-2015
    	
    	//Anjana: 02/24: prod issue : modified the null check as the error message was shown for consignment close as well
    	if("US".equalsIgnoreCase(Version.CURRENT_REGION) && (posTransFound.getPresaleTransaction().getPresaleId())!=null)
    	{ 
    		theAppMgr.showErrorDlg(res.getString(
            "Return is not possible during Presale Close."));
    		theAppMgr.setHomeEnabled(true);
    		return;
    	}
    	//Ends
      /*if(pnlList.getSelectedLineItem() == null|| (!pnlList.isRowChecked()))
       {
       theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
       return;
       }*/
      if (pnlList.returnSelectedLineItem()) {
        theAppMgr.showErrorDlg(res.getString(
            "Selected line item[s] can't be returned since they have been Altered."));
      }
    }
    else if (sAction.equals("RESERVE")) {
      pnlList.reserveSelectedLineItem();
    } else if (sAction.equals("ADD_ITEM_TENDER")) {
      bTransactionSelected = false;
      try {
      //  theTxn.setCustomer(posTransFound.getCustomer());
    	  setCustomer((CMSCustomer)posTransFound.getCustomer());
      } catch (Exception e) {}
      createSaleItemsForTender();
      createReturnItemsForTender();
      theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet.SALE_MODE));
      applyCustomer();
    } else if (sAction.equals("EMPLOYEE_SALE")) {
      bTransactionSelected = false;
      try {
  //      theTxn.setCustomer(posTransFound.getCustomer());
    	  setCustomer((CMSCustomer)posTransFound.getCustomer());
      } catch (Exception e) {}
      createSaleItemsForTender();
      createReturnItemsForTender();
      theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet.EMPLOYEE_SALE));
      applyCustomer();
    } else if (sAction.equals("OK") && Header.equals("ITEMS_SELECTED")) {
      bTransactionSelected = false;
      try {
 //       theTxn.setCustomer(posTransFound.getCustomer());
    	  setCustomer((CMSCustomer)posTransFound.getCustomer());
      } catch (Exception e) {}
      
      //Fix for 1641: Request for reservation close
	  //Vivek commented this region check as JAPAN needs to display the RESERVE button also.
	  //Previously it was just to SELL/RETURN functionality for JAPAN. 21-JUNE-2010
     /* if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){ 	  
	      int totalLineItems = pnlList.getModel().getAllRows().size();
	      int saleLineItems = pnlList.getModel().getSaleLineItems().length;
	      int returnLineItems = pnlList.getModel().getReturnLineItems().length;
	      if((saleLineItems + returnLineItems) != totalLineItems) {
	          theAppMgr.showErrorDlg(res.getString(
	           "Mark all items to Sell/Return to continue."));
	       anEvent.consume();
	       return;
	      }
	      createSaleItemsForTender();
	      createReturnItemsForTender();
      }else {*/
	      boolean bCreateSaleItemsForTender=createSaleItemsForTender();
	      boolean bCreateReturnItemsForTender=createReturnItemsForTender();
	      if(!bCreateSaleItemsForTender && !bCreateReturnItemsForTender)
	      {
	          theAppMgr.showErrorDlg(res.getString(
	           "Sell/Return atleast one Line item to continue."));
	       anEvent.consume();
	       return;
	      }
     // }
//      if (!createSaleItemsForTender() || !createReturnItemsForTender()) {
//        theAppMgr.showErrorDlg(res.getString(
//            "Sell/Return atleast one Line item to continue."));
//        anEvent.consume();
//        return;
//      }

      if (createVoidLineItemsForTender()) {
        //System.out.println("#@$@#$ Attaaching new RSVO GUI TXN #@$#@$#");
        attachNewReservationOpenTxn();
      }
      if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE)
        theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet.RESERVATIONS_CLOSE));
      else
      theAppMgr.addStateObject("TXN_MODE", new Integer(InitialSaleApplet.SALE_MODE));
      applyCustomer();
      // Add Deposit amount to state.
      if (amtDeposit != null && amtDeposit.doubleValue() > 0)
        theAppMgr.addStateObject("ARM_RSV_DEPOSIT", amtDeposit);
      theAppMgr.fireButtonEvent("ADD_ITEM_TENDER");
    } else if (sAction.equals("PREV")) {
      if (Header.equals("PRE_SALE_SELECTED")) {
        bTransactionSelected = false;
        setAppletMode();
        populatePOSLineItems();
      } else if (Header.equals("ITEMS_SELECTED")) {
        showSelectedBtns();
      }
      anEvent.consume();
    } else if (sAction.equals("CANCEL")) {
      if (Header.equals("PRE_SALE_SELECTED")) {
        bTransactionSelected = false;
        setAppletMode();
      } else if (Header.equals("ITEMS_SELECTED")) {
        showSelectedBtns();
      }
      anEvent.consume();
    }
  }

  /**
   * AppButtonEVent
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sCommand = anEvent.getActionCommand();
    if (sCommand.equals("VIEW_PRESALES")) {} else if (sCommand.equals("CUST_LOOKUP")) {
      if (posTransFound != null)
        theAppMgr.addStateObject("ARM_TXN_SELECTED", posTransFound);
    } else if (sCommand.equals("OK")) {
      if (posTransFound == null || pnlList.getModel().getTotalRowCount() < 1) {
        theAppMgr.showErrorDlg(res.getString("Transaction required to proceed"));
        return;
      }
      if (posTransFound != null && !posTransFound.getStore().getId().equals(theStore.getId())) {
        theAppMgr.showErrorDlg(res.getString("This Transaction belongs to a different store"));
        return;
      }
      bTransactionSelected = true;
      showSelectedBtns();
    } else if (sCommand.equals("PREV")) {
      //VM: Transfer of Associate and Customer between Sale, PreSale and Consignment
      if (theTxn.getCustomer() != null)
        theAppMgr.addStateObject("CUSTOMER", theTxn.getCustomer());
      if (theTxn.getConsultant() != null)
        theAppMgr.addStateObject("ASSOCIATE", theTxn.getConsultant());
      bTransactionSelected = false;
    } else if (sCommand.equals("CANCEL")) {
      //VM: Transfer of Associate and Customer between Sale, PreSale and Consignment
      if (theTxn.getCustomer() != null)
        theAppMgr.addStateObject("CUSTOMER", theTxn.getCustomer());
      if (theTxn.getConsultant() != null)
        theAppMgr.addStateObject("ASSOCIATE", theTxn.getConsultant());
      bTransactionSelected = false;
    }
//    else if(sCommand.equals("NO_OPEN_TXN"))
//    {
//      popUpReservationReasonCodes();
//    }
  }

  /**
   * EditArea Event
   * @param Command ButtonName
   * @param sEdit Value
   */
  public void editAreaEvent(String Command, String sEdit) {
    if (Command.equals("TRANSACTION")) {
      if (!findTransactionById(sEdit.trim())) {
        if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE)
          enterConsignmentTransaction();
        else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE)
            enterReservationTransaction();
        else
          enterPresaleTransaction();
        return;
      }
      populatePOSLineItems();
    } else if (Command.equals("EXPIRE_DATE")) {
      try {
        SimpleDateFormat dFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
        dFormat.setLenient(false);
        Date dtExpire = dFormat.parse(sEdit);
        if (dtExpire.getYear() > 9999)
          throw new Exception("Not valid date");
        if (dtExpire.before(new Date())) {
          theAppMgr.showErrorDlg(res.getString("Expire Date can't be prior to today's date"));
          enterExpireDate();
          return;
        }
        setExpirationDate(sEdit);
        if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE)
          enterConsignmentTransaction();
        else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE)
            enterReservationTransaction();
        else
          enterPresaleTransaction();
      } catch (Exception e) {
        theAppMgr.showErrorDlg(res.getString("Please enter a valid date"));
        enterExpireDate();
      }
    }
  }

  /**
   * Convert items selected to "SELL" into SaleLineItem
   * and add to the Transaction
   */
  private boolean createSaleItemsForTender() {
    try {
      if (theTxn == null || pnlList == null || pnlList.getModel() == null)
        return false;
      POSLineItem posLineItems[] = pnlList.getModel().getSaleLineItems();
      SaleLineItem saleLineItem = null;
      CMSItem cmsItem;
      boolean isConsultantChanged = false;
      if (posLineItems == null || posLineItems.length < 1)
        return false;
      for (int iCtr = 0; iCtr < posLineItems.length; iCtr++) {
    	  saleLineItem = null;
        if (posLineItems[iCtr] == null)
          continue;
        if (posLineItems[iCtr] instanceof CMSPresaleLineItem) {
          saleLineItem = theTxn.addPresaleLineItemAsSale((CMSPresaleLineItem)posLineItems[iCtr]);
        } else if (posLineItems[iCtr] instanceof CMSConsignmentLineItem) {
          saleLineItem = theTxn.addConsignmentLineItemAsSale((CMSConsignmentLineItem)posLineItems[
              iCtr]);
        } else if (posLineItems[iCtr] instanceof CMSReservationLineItem) {
          saleLineItem = theTxn.addReservationLineItemAsSale((CMSReservationLineItem)posLineItems[
              iCtr]);
        }
        if (saleLineItem == null){
          continue;
        }
        //Added for setting promotion code for reservation close
        if(saleLineItem.getPromotionCode()==null)
        {
        	if(saleLineItem.getLineItemDetailsArray()!=null){
        	String dealId=saleLineItem.getLineItemDetailsArray()[0].getDealId();
        	saleLineItem.setPromotionCode(dealId);}
        	}
        //Tim: Bug 1698: update associate's name with ORIGINAL associate's name on the SALE screen.
        if(!isConsultantChanged){
        	isConsultantChanged = true;
        	if(saleLineItem.getAdditionalConsultant() != null)
        		theTxn.setConsultant(saleLineItem.getAdditionalConsultant());
        }
        //___Tim:1864:
        if(posLineItems[iCtr].getExtendedBarCode() != null){
        	saleLineItem.setExtendedBarCode(posLineItems[iCtr].getExtendedBarCode());
        }
        cmsItem = (CMSItem)saleLineItem.getItem();
        ArmCurrency retail = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode()
            , theStore.getId()).getRetailPrice();
        ArmCurrency mkdn = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode(), theStore.getId()).
            getMarkdownAmount();
        cmsItem.setRetailPrice(retail);
        cmsItem.setMarkdownAmount(mkdn);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @return
   */
  private boolean createVoidLineItemsForTender() {
    boolean bHasVoidLineItems = false;
    try {
      if (theTxn == null || pnlList == null || pnlList.getModel() == null)
        return false;
      POSLineItem posLineItems[] = pnlList.getModel().getReservedLineItems();
      ReturnLineItem voidLineItem = null;
      CMSItem cmsItem;
      if (posLineItems == null || posLineItems.length < 1)
        return false;
      for (int iCtr = 0; iCtr < posLineItems.length; iCtr++) {
        if (posLineItems[iCtr] == null)
          continue;
        if (posLineItems[iCtr] instanceof CMSReservationLineItem) {
          bHasVoidLineItems = true;
          voidLineItem = theTxn.addReservationLineItemAsVoid((CMSReservationLineItem)posLineItems[
              iCtr]);
          if(posLineItems[iCtr].getAdditionalConsultant()!=null && voidLineItem!=null)
            voidLineItem.setAdditionalConsultant(posLineItems[iCtr].getAdditionalConsultant());
        }
        if (voidLineItem == null){
          continue;
        }
        //___Tim:1864:
        if(posLineItems[iCtr].getExtendedBarCode() != null){
        	voidLineItem.setExtendedBarCode(posLineItems[iCtr].getExtendedBarCode());
        }
        cmsItem = (CMSItem)voidLineItem.getItem();
        ArmCurrency retail = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode()
            , theStore.getId()).getRetailPrice();
        ArmCurrency mkdn = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode(), theStore.getId()).
            getMarkdownAmount();
        cmsItem.setRetailPrice(retail);
        cmsItem.setMarkdownAmount(mkdn);
        voidLineItem = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bHasVoidLineItems;
  }

  /**
   * put your documentation comment here
   */
  private void attachNewReservationOpenTxn() {
    try {
      CMSCompositePOSTransaction theRSVOTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
      if (theRSVOTxn == null || pnlList == null || pnlList.getModel() == null)
        return;
      if (theTxn.getCustomer() != null)
        theRSVOTxn.setCustomer(theTxn.getCustomer());
    	//  setCustomer((CMSCustomer)theTxn.getCustomer());
      if (theTxn.getConsultant() != null)
        theRSVOTxn.setConsultant(theTxn.getConsultant());
      POSLineItem posLineItems[] = pnlList.getModel().getReservedLineItems();
      if (posLineItems == null || posLineItems.length < 1)
        return;
      for (int iCtr = 0; iCtr < posLineItems.length; iCtr++) {
        if (posLineItems[iCtr] == null)
          continue;
        CMSReservationLineItem rsvLineItm = theRSVOTxn.addReservationItem((CMSItem)posLineItems[
            iCtr].getItem());
        if (rsvLineItm == null)
          continue;
        CMSItem cmsItem = (CMSItem)rsvLineItm.getItem();
        ArmCurrency retail = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode()
            , theStore.getId()).getRetailPrice();
        ArmCurrency mkdn = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode(), theStore.getId()).
            getMarkdownAmount();
        cmsItem.setRetailPrice(retail);
        cmsItem.setMarkdownAmount(mkdn);
      }
      String txnNum = CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr
          , (CMSStore)theStore, (CMSRegister)theAppMgr.getGlobalObject("REGISTER"));
      theRSVOTxn.setId(txnNum);
      theRSVOTxn.getReservationTransaction().setDepositAmount(new ArmCurrency(0.0d));
      theRSVOTxn.getReservationTransaction().setReservationReason(theTxn.getReservationTransaction().
          getReservationReason());
      theRSVOTxn.getReservationTransaction().doSetReservationId(txnNum);
      theRSVOTxn.getReservationTransaction().setOriginalRSVOTransaction(posTransFound.
          getReservationTransaction());
      theRSVOTxn.setSubmitDate(Calendar.getInstance().getTime());
      theRSVOTxn.setProcessDate(theTxn.getProcessDate());
      try {
        if (getExpirationDate() != null)
          theRSVOTxn.getReservationTransaction().setExpirationDate(getExpirationDate());
        else {
          Calendar calendar = Calendar.getInstance();
          ConfigMgr config = new ConfigMgr("pos.cfg");
          int iDays = -1;
          if (config != null) {
            iDays = Integer.parseInt(config.getString(
                "RESERVATION_EXPIRY_NUMBER_OF_DAYS"));
            iDays += calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, iDays);
            theRSVOTxn.getReservationTransaction().setExpirationDate(calendar.
                getTime());
          }
        }
      }
      catch (Exception e) {
        System.out.println("Exception--> Setting Reservation Expiry date");
      }

      if (theAppMgr.getGlobalObject("REGISTER") != null)
        theRSVOTxn.setRegisterId(((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId());
      theTxn.attachNewReservationOpenTxn(theRSVOTxn);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Convert items selected to "RETURN" into ReturnLineItem
   * and add to the Transaction
   */
  private boolean createReturnItemsForTender() {
    try {
      if (theTxn == null || pnlList == null || pnlList.getModel() == null)
        return false;
      POSLineItem posLineItems[] = pnlList.getModel().getReturnLineItems();
      ReturnLineItem returnLineItem = null;
      CMSItem cmsItem;
      boolean isConsultantChanged = false;
      if (posLineItems == null || posLineItems.length < 1)
        return false;
      for (int iCtr = 0; iCtr < posLineItems.length; iCtr++) {
    	  returnLineItem = null;
        if (posLineItems[iCtr] == null)
          continue;
        if (posLineItems[iCtr] instanceof CMSPresaleLineItem) {
          returnLineItem = theTxn.addPresaleLineItemAsReturn((CMSPresaleLineItem)posLineItems[iCtr]);
        } else if (posLineItems[iCtr] instanceof CMSConsignmentLineItem) {
          returnLineItem = theTxn.addConsignmentLineItemAsReturn((CMSConsignmentLineItem)
              posLineItems[iCtr]);
        } else if (posLineItems[iCtr] instanceof CMSReservationLineItem) {
          returnLineItem = theTxn.addReservationLineItemAsReturn((CMSReservationLineItem)
              posLineItems[iCtr]);
        }
        if (returnLineItem == null){
          continue;
        }
        //Tim: Bug 1698: update associate's name with ORIGINAL associate's name on the SALE screen.
        if(!isConsultantChanged){
        	isConsultantChanged = true;
        	if(returnLineItem.getAdditionalConsultant() != null)
        		theTxn.setConsultant(returnLineItem.getAdditionalConsultant());
        }
        //___Tim:1864:
        if(posLineItems[iCtr].getExtendedBarCode() != null){
        	returnLineItem.setExtendedBarCode(posLineItems[iCtr].getExtendedBarCode());
        }
        cmsItem = (CMSItem)returnLineItem.getItem();
        ArmCurrency retail = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode()
            , theStore.getId()).getRetailPrice();
        ArmCurrency mkdn = CMSItemHelper.findByBarCode(theAppMgr, cmsItem.getBarCode(), theStore.getId()).
            getMarkdownAmount();
        cmsItem.setRetailPrice(retail);
        cmsItem.setMarkdownAmount(mkdn);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Populate POSLineItems from Transaction
   * to list.
   */
  private void populatePOSLineItems() {
    pnlList.clear();
    POSLineItem[] lineItems = null;
    if (posTransFound == null)
      return;
    pnlHeader.reset();
    switch (iAppletMode) {
      case InitialSaleApplet.PRE_SALE_CLOSE:
        lineItems = posTransFound.getPresaleLineItemsArray();
        try {
          Date dtExpire = posTransFound.getPresaleTransaction().getExpirationDate();
          SimpleDateFormat dFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          jcmslblExpiryValue.setText(dFormat.format(dtExpire));
          //pnlHeader.setRefLabel(res.getString("Pre-Sale ID:"));
          pnlHeader.setRefID(posTransFound.getPresaleTransaction().getPresaleId());
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case InitialSaleApplet.CONSIGNMENT_CLOSE:
        lineItems = posTransFound.getConsignmentLineItemsArray();
        try {
          Date dtExpire = posTransFound.getConsignmentTransaction().getExpirationDate();
          SimpleDateFormat dFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          jcmslblExpiryValue.setText(dFormat.format(dtExpire));
          //pnlHeader.setRefLabel(res.getString("Consignment ID:"));
          pnlHeader.setRefID(posTransFound.getConsignmentTransaction().getConsignmentId());
        } catch (Exception e) {}
        break;
      case InitialSaleApplet.RESERVATIONS_CLOSE:
        lineItems = posTransFound.getReservationLineItemsArray();
        popUpReservationReasonCodes();
        try {
          Date dtExpire = posTransFound.getReservationTransaction().getExpirationDate();
          SimpleDateFormat dFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          jcmslblExpiryValue.setText(dFormat.format(dtExpire));
          pnlHeader.setRefID(posTransFound.getReservationTransaction().getOriginalRSVTransaction().getReservationId());
        } catch (Exception e) {System.out.println("**Exception--> Getting expiry date");}

        try {
          amtDeposit = posTransFound.getReservationTransaction().getDepositAmount();
          if (amtDeposit != null)
            lblDepositValue.setText(amtDeposit.formattedStringValue());
          else
            lblDepositValue.setText(new ArmCurrency(0.0d).formattedStringValue());
        } catch (Exception e) {
          System.out.println("**Exception--> Getting customer deposit history");
        }
        break;
      default:
        lineItems = posTransFound.getLineItemsArray();
    }
    lineCount = lineItems.length;
    pnlHeader.reset();
    pnlHeader.setCashierID(((CMSEmployee)posTransFound.getConsultant()).getExternalID());
    if (posTransFound.getCustomer() != null) {
      pnlHeader.setCustomer(posTransFound.getCustomer().getFirstName() + " "
          + posTransFound.getCustomer().getLastName());
      pnlHeader.setCustomerID(posTransFound.getCustomer().getId());
    }
    pnlHeader.setTransactionID(posTransFound.getId());
    pnlList.setSelectionOn(false);
    // Load lineitems into List panel.
    boolean toVerifone = false;
    for (int i = 0; i < lineItems.length; i++) {
      pnlList.addLineItem(lineItems[i]);
      //Vivek Mishra : Added to send AJB 150 massage for each item.
      //Vivek Mishra : Added Condition for Sending *POSITEMS for PreSale, Consignment and Reservation when price override is true 
      /*if(lineItems[i].isManualUnitPrice())
    	  toVerifone = sendItemMessageData(lineItems[i] ,lineItems, true, true);
      else
          toVerifone = sendItemMessageData(lineItems[i] ,lineItems, false, false);
      if(toVerifone)
      {
    	  toVerifone = sendItemMessageData(lineItems[i] ,lineItems, false, true);
      }*/
      //End
    }
    //Vivek Mishra : Added to send a single 150 request with all the items
    //if(toVerifone)
    //Vivek Mishra : Changed for sending only the refresh message.
	if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
	  toVerifone = sendItemMessageData(lineItems[0] ,lineItems,true, false,false);
	}
    /*else
      toVerifone = sendItemMessageData(lineItems[0] ,lineItems,false, false,false);
*/    //Ends
    if (lineItems.length > 0) {
      pnlList.getModel().firstPage();
      pnlList.selectFirstRow();
    }
    if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE)
      enterPresaleTransaction();
    else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE)
      enterConsignmentTransaction();
    else
      enterReservationTransaction();
  }

  private void popUpReservationReasonCodes() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            ReservationReasonDlg reservationDlg = new ReservationReasonDlg(theAppMgr.getParentFrame()
                , theAppMgr, res.getString("Reservation Reason"));
            if(reservationDlg.areReservationReasonsAvailable())
            {
              // Reservation reason should be selected
              // before Dialog is made visible
              // else selection wont show - (MSB 10/25/05)
              if (posTransFound != null) {
                ReservationTransaction rsvTxn = posTransFound.getReservationTransaction();
                if (rsvTxn != null && rsvTxn.getReservationReason() != null) {
                  reservationDlg.selectReservationReason(rsvTxn.getReservationReason());
                  reservationDlg.setEnabled(false);
                }
              }
              reservationDlg.setVisible(true);
            }
          }
        });
      }
    });
  }

  /**
   * put your documentation comment here
   */
  private void showTransactionNotFoundErr() {
    theAppMgr.showErrorDlg(res.getString("No match found for the transaction id"));
  }

  /**
   * put your documentation comment here
   */
  private void showSelectedBtns() {
    theAppMgr.showMenu(MenuConst.PRE_SALE_SELECTED, "PRE_SALE_SELECTED", theOpr);
    pnlList.setSelectionOn(false);
    enterSelectMenuOption();
  }

  /**
   * put your documentation comment here
   */
  private void initPresaleBtns() {
    theAppMgr.showMenu(MenuConst.PRE_SALE_CLOSE, theOpr);
    enterPresaleTransaction();
    pnlHeader.setHeaderLabel(res.getString("Pre-Sale Details"));
    pnlHeader.setRefLabel(res.getString("Pre-Sale ID:")+"         ");
    jcmslblTranType.setText(res.getString("PRE-SALE CLOSE"));
    jcmslblExpiryValue.setVisible(true);
    jcmslblExpiry.setVisible(true);
    lblDeposit.setVisible(false);
    lblDepositValue.setVisible(false);
  }

  /**
   * put your documentation comment here
   */
  private void initReservationBtns() {
    theAppMgr.showMenu(MenuConst.RESERVATION_CLOSE, theOpr);
    enterReservationTransaction();
    pnlHeader.setHeaderLabel(res.getString("Reservation Details"));
    pnlHeader.setRefLabel(res.getString("Reservation ID:  "));
    jcmslblTranType.setText(res.getString("RESERVATION CLOSE"));
    jcmslblExpiryValue.setVisible(true);
    jcmslblExpiry.setVisible(true);
    lblDeposit.setVisible(true);
    lblDepositValue.setVisible(true);
    lblDepositValue.setText("");
  }

  /**
   * put your documentation comment here
   */
  private void initConsignmentBtns() {
    theAppMgr.showMenu(MenuConst.CONSIGNMENTS_OUT, theOpr);
    enterConsignmentTransaction();
    pnlHeader.setHeaderLabel(res.getString("Consignment Details"));
    pnlHeader.setRefLabel(res.getString("Consignment ID:"));
    jcmslblTranType.setText(res.getString("CONSIGNMENT CLOSE"));
    jcmslblExpiryValue.setVisible(true);
    jcmslblExpiry.setVisible(true);
    lblDeposit.setVisible(false);
    lblDepositValue.setVisible(false);
  }

  /**
   * put your documentation comment here
   */
  private void showSellReturnBtns() {
    if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE)
      theAppMgr.showMenu(MenuConst.SELL_RETURN, "ITEMS_SELECTED", theOpr);
    else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE)
      theAppMgr.showMenu(MenuConst.CONS_SELL_RETURN_DESELECT_ALL, "ITEMS_SELECTED", theOpr);
    else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE)
      theAppMgr.showMenu(MenuConst.RESERVATION_SELECT, "ITEMS_SELECTED", theOpr);
    enterSelectSellReturnItems();
    pnlList.setSelectionOn(true);
  }

  /**
   * put your documentation comment here
   */
  private void enterSelectSellReturnItems() {
    theAppMgr.setSingleEditArea(res.getString("Select item(s) to sell or return"));
  }

  /**
   * put your documentation comment here
   */
  private void enterSelectMenuOption() {
    theAppMgr.setSingleEditArea(res.getString("Select menu option to proceed"));
  }

  /**
   * put your documentation comment here
   */
  private void enterExpireDate() {
    theAppMgr.setSingleEditArea(res.getString("Enter new expire date MM/DD/YYYY."), "EXPIRE_DATE"
        , theAppMgr.NO_MASK);
  }

  /**
   * put your documentation comment here
   */
  private void enterConsignmentTransaction() {
    theAppMgr.setSingleEditArea(res.getString("Scan or enter Consignment Open transaction number")
        , "TRANSACTION", theAppMgr.TRANS_ID_MASK);
  }

  /**
   * put your documentation comment here
   */
  private void enterPresaleTransaction() {
    theAppMgr.setSingleEditArea(res.getString("Scan or enter Pre-Sale Open transaction number")
        , "TRANSACTION", theAppMgr.TRANS_ID_MASK);
  }

  /**
   * put your documentation comment here
   */
  private void enterReservationTransaction() {
    theAppMgr.setSingleEditArea(res.getString("Scan or enter Reservation Open transaction number")
        , "TRANSACTION", theAppMgr.TRANS_ID_MASK);
  }

  /**
   * Retrieve Transaction with given ID
   * @param sTransactionID TransactionID
   * @return True/False - Transaction found or not
   */
  private boolean findTransactionById(String sTransactionID) {
    try {
      CMSTransactionHeader tranHeader = null;
      if (iAppletMode == InitialSaleApplet.PRE_SALE_CLOSE) {
        tranHeader = CMSTransactionPOSHelper.findOpenPresaleById(theAppMgr, sTransactionID);
      } else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
        tranHeader = CMSTransactionPOSHelper.findOpenConsignmentById(theAppMgr, sTransactionID);
      } else if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
        tranHeader = CMSTransactionPOSHelper.findOpenReservationById(theAppMgr, sTransactionID);
      }
      if (tranHeader == null) {
        showTransactionNotFoundErr();
        return false;
      }
      if (tranHeader.getVoidID() != null) {
        theAppMgr.showErrorDlg(res.getString("Transaction has already been voided"));
        return false;
      }
      if (!tranHeader.getStoreId().equals(theStore.getId())) {
        theAppMgr.showErrorDlg(res.getString("Transaction doesn't belong to this store"));
        return false;
      }
      posTransFound = (CMSCompositePOSTransaction)CMSTransactionPOSHelper.findById(theAppMgr
          , tranHeader.getId());
      if (posTransFound == null) {
        showTransactionNotFoundErr();
        return false;
      }
      return true;
    } catch (Exception e) {
      showTransactionNotFoundErr();
    }
    return false;
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbinit()
      throws Exception {
    pnlHeader = new SelectItemsHeaderPanel();
    pnlList = new SelectItemsListPanel();
    pnlFooter = new JPanel();
    this.setLayout(new BorderLayout());
    jcmslblTranType = new JCMSLabel(res.getString("PRE-SALE CLOSE"));
    jcmslblTranType.setForeground(new Color(0, 128, 255));
    jcmslblTranType.setFont(theAppMgr.getTheme().getSaleFont());
    jcmslblExpiry = new JCMSLabel(res.getString("Expiry Date:"));
    jcmslblExpiryValue = new JCMSLabel("");
    lblDeposit = new JCMSLabel(res.getString("Deposit") + " : ");
    lblDepositValue = new JCMSLabel("");
    lblDeposit.setFont(theAppMgr.getTheme().getMessageFont());
    lblDepositValue.setFont(theAppMgr.getTheme().getMessageFont());
    jcmslblExpiry.setFont(theAppMgr.getTheme().getMessageFont());
    jcmslblExpiryValue.setFont(theAppMgr.getTheme().getMessageFont());
    pnlFooter.setLayout(new GridBagLayout());
    jcmslblExpiry.setHorizontalAlignment(SwingConstants.RIGHT);
    lblDeposit.setHorizontalAlignment(SwingConstants.RIGHT);
    pnlFooter.add(lblDepositValue,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 0, 1, 37), 45, 4));
    pnlFooter.add(jcmslblExpiryValue,      new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 0, 3, 37), 53, 5));
    pnlFooter.add(lblDeposit,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 0, 1, 0), 14, 4));
    pnlFooter.add(jcmslblExpiry,       new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 0, 3, 0), 14, 5));
    pnlFooter.add(jcmslblTranType,  new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(13, 2, 14, 0), 76, 5));
    pnlFooter.setPreferredSize(new Dimension((int)(r * 833), (int)(r * 50)));
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlFooter.setBackground(theAppMgr.getBackgroundColor());
    this.add(pnlHeader, BorderLayout.NORTH);
    this.add(pnlList, BorderLayout.CENTER);
    this.add(pnlFooter, BorderLayout.SOUTH);
  }

  /**
   * put your documentation comment here
   */
  public void stop() {}

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageDown(MouseEvent e) {
    pnlList.nextPage();
    theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlList).getCurrentPageNumber() + 1
        , ((PageNumberGetter)pnlList).getTotalPages());
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageUp(MouseEvent e) {
    pnlList.prevPage();
    theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlList).getCurrentPageNumber() + 1
        , ((PageNumberGetter)pnlList).getTotalPages());
  }

  /**
   * put your documentation comment here
   */
  private void applyCustomer() {
    if (txnCustomer == null)
      return;
    pnlHeader.setCustomer(txnCustomer.getFirstName() + " " + txnCustomer.getLastName());
    pnlHeader.setCustomerID(txnCustomer.getId());
    if (theTxn.getCustomer() == null) {
      try {
 //       theTxn.setCustomer(txnCustomer);
    	  setCustomer(txnCustomer);
      } catch (Exception e) {}
    }
    if (theAppMgr.getStateObject("ARM_BACK_TO_SELECT_ITEMS") == null
        &&
        // MSB -10/25/05
        // Make sure PREV or SKIP button wasnt pressed
        // in CustomerLookup Applet.
        theAppMgr.getStateObject("ARM_CUST_LOOKUP_PREV_PRESSED")==null
        &&
        theAppMgr.getStateObject("ARM_TXN_SELECTED") == null
        ) {
      showCustomersTransactions();
    } else {
      theAppMgr.removeStateObject("ARM_BACK_TO_SELECT_ITEMS");
      theAppMgr.removeStateObject("ARM_CUST_LOOKUP_PREV_PRESSED");
    }
  }

  /**
   * put your documentation comment here
   */
  private void showCustomersTransactions() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        try {
          String dateString = null;
          String searchTitle = null;
          String sNameString = com.chelseasystems.cs.util.CustomerUtil.getCustomerNameString(txnCustomer,false);
          CMSTransactionHeader[] transactionHeaders = null;
          switch (iAppletMode) {
            case InitialSaleApplet.PRE_SALE_CLOSE:
              searchTitle = res.getString("View Presale Open transactions for ");
              searchTitle += sNameString;
              transactionHeaders = CMSTransactionPOSHelper.findOpenPresaleByCustomer(theAppMgr
                  , txnCustomer.getId());
              break;
            case InitialSaleApplet.CONSIGNMENT_CLOSE:
              searchTitle = res.getString("View Consignment Open transactions for ");
              searchTitle += sNameString;

              transactionHeaders = CMSTransactionPOSHelper.findOpenConsignmentByCustomer(theAppMgr
                  , txnCustomer.getId());
              break;
            case InitialSaleApplet.RESERVATIONS_CLOSE:
              searchTitle = res.getString("View Reservation Open transactions for ");
              searchTitle += sNameString;
              transactionHeaders = CMSTransactionPOSHelper.findOpenReservationByCustomer(theAppMgr
                  , txnCustomer.getId());
              break;
          }
          if (transactionHeaders != null) {

            dateString = res.getString("All Dates");
            theAppMgr.addStateObject("DATE_STRING", dateString);
            if (searchTitle != null && searchTitle.length() > 0)
              theAppMgr.addStateObject("TITLE_STRING", searchTitle);

            theAppMgr.addStateObject("ARM_TXN_HEADERS", transactionHeaders);
            theAppMgr.addStateObject("ARM_BACK_TO_SELECT_ITEMS", "SELECT_ITEMS_APPLET");
            theAppMgr.fireButtonEvent("LIST_CUSTOMER_TXNS");
          }
        } catch (Exception ex) {
          theAppMgr.showExceptionDlg(ex);
        }
      }
    });
  }

  public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}

		/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the Sign
		 * Off event for Fiscal Solutions Service START--
		 */
		boolean goToHomeView = (theAppMgr
				.showOptionDlg(
						res.getString("Cancel Transaction"),
						res.getString("Are you sure you want to cancel this transaction?")));
		if (goToHomeView) {

			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr);
		}

		return goToHomeView;
		/*
		 * --END
		 */

	}

  
  private void setCustomer(CMSCustomer customer) throws BusinessRuleException{
	  theTxn.setCustomer(customer);
	  String thisStoreId  = ((CMSStore)theStore).getId();
      if(thisStoreId!=null && customer!=null){
      ArmCurrency custCurr = null;
		try {
			custCurr = CustomerUtil.getDepositHistoryBalance(theAppMgr,customer.getId(),thisStoreId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	if(custCurr!=null){
      		customer.setCustomerBalance(custCurr);
      	}
      	
      }else{
    	  System.out.println("storeId " + thisStoreId);
    	  System.out.println("txnCust " + (customer==null));
      }
  }
  
    //Vivek Mishra : Added to send AJB 150 massage for each item.  
	private boolean sendItemMessageData(POSLineItem line ,POSLineItem[] lineItemArray,boolean Refresh,boolean idleMessage,boolean clearMessage) {
		try {
			String result = "";
			//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
			//int qty = theTxn.getLineItemsArray().length;
			//End
			String responseArray[] = null;
			String ajbResponse[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				if(!storeCountry.equals("CAN")){
					//String tt = orgTxn.getTransactionType();
					responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line,lineItemArray,Refresh,false,false,"",false);
					if(responseArray!=null){
						ajbResponse = responseArray;
						//End
					int length = responseArray.length;
					for (int i=0; i<length ;i++){	
						//Vivek Mishra : Removed the blak condition as it was causing the All AJB Server down dialogue even in case of server is recover.
						//if(responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers")||responseArray[i].equals(""))){ 
						if(responseArray[i] != null && responseArray[i].toString().contains("All the Ajb Servers")){
						//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
						if(!Refresh)
							count++;
						if(count==lineCount)
						{
						theAppMgr.showErrorDlg("All the AJB servers are down");
						count=0;
						}//End
						return false;
					}
					}
					}
				}else
				
				if (responseArray == null) {
					return true;
				}
			
			count=0;	
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}
	
  }
  //End
}

