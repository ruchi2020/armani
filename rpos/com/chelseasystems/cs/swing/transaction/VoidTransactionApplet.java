/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-22-2005 | Mukesh    | N/A       | Following methods newly added or modified to |
 |      |            |           |           | give user option to select the reason of     |
 |      |            |           |           | void trxn                                    |
 |      |            |           |           | 1. postVoidDlg()                             |
 |      |            |           |           | 2. appButtonEvent (CMSActionEvent anEvent)   |
 |      |            |           |           | 3. editAreaEvent (String command,String value)
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 09-07-2005 | Manpreet  | 901       | Reading Void reason from ArmaniCommon.cfg    |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.transaction;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.payment.CMSBankCheck;
import com.chelseasystems.cs.payment.CMSDebitCard;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.payment.BankCheck;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.DebitCard;
import com.chelseasystems.cr.payment.DueBillIssue;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.SaleLineItemDetail;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.cs.txnnumber.CMSTransactionNumberHelper;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;

import java.util.*;

import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.util.ArmConfigLoader;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.emobilepos.ivu.IVU;
import com.emobilepos.ivu.models.TransactionRequest;
import com.emobilepos.ivu.models.TransactionRequest.CARDTYPE;
import com.emobilepos.ivu.models.TransactionRequest.TRANSTYPE;
import com.emobilepos.ivu.models.TransactionResponse;

/**
 */
public class VoidTransactionApplet extends CMSApplet {
  //public class VoidTransactionApplet extends JApplet {
  CMSVoidTransaction voidTxn;
  private Redeemable cmsRedeemable = null;
  TxnHeaderPanel pnlHeader = new TxnHeaderPanel();
  TxnDetailPanel pnlDetail = new TxnDetailPanel();
  JLabel lblReason = new JLabel();
  JPanel pnlSouth = new JPanel();
  
  PaymentTransaction orgTxn;
  
//Vivek Mishra : Added for POST VOID AJB Offline changes
  boolean isOffline = false;
  public static final int RETURN_MODE = 1;
//Ends
	  
  //Vivek Mishra : Added to implementing manual authorization flow for POST VOID.
  private CommunicationErrorDlg communicationErrorDlg;
  
//Vivek Mishra : Added to implementing manual authorization flow for POST VOID.
	private Payment authPay;
	
	private static ConfigMgr config;
	private String fipay_flag;
	//vishal yevale 3 nov 2016
	private String fipayGiftcardFlag;
	/*Added to log info and errors.*/
	private static Logger logger = Logger.getLogger(VoidTransactionApplet.class.getName());

  /**
   *Initialize the applet
   */
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   *Component initialization
   */
  private void jbInit()
      throws Exception {
    this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
    this.getContentPane().add(pnlDetail, BorderLayout.CENTER);
    this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
    //lblReason.setPreferredSize(new Dimension((int)(840*r), (int)(40*r)));
    pnlSouth.setPreferredSize(new Dimension((int)(0 * r), (int)(50 * r)));
    lblReason.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblReason.setBorder(BorderFactory.createTitledBorder(res.getString("Reason")));
    pnlSouth.setLayout(new BorderLayout());
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlSouth.add(lblReason, BorderLayout.CENTER);
    pnlDetail.setAppMgr(theAppMgr);
    pnlHeader.setAppMgr(theAppMgr);
  }


  /**
   * Start the applet
   */
  public void start() {
	String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_flag = config.getString("FIPAY_Integration");
	
	 //Default value of the flag is Y if its not present in credit_auth.cfg
	if (fipay_flag == null) {
		fipay_flag = "Y";
	}
	//vishal yevale 3 nov 2016
	fipayGiftcardFlag = config.getString("FIPAY_GIFTCARD_INTEGRATION");

	 //Default value of the flag is N if its not present in store_custom.cfg
	if (fipayGiftcardFlag == null) {
		fipayGiftcardFlag = "N";
	}
	//end vishal 3 nov
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    clear();
    voidTxn = null;
    theAppMgr.showMenu(MenuConst.PREV_CANCEL, theOpr);
    enterTxnId();
  }


  /**
   * edit area event handling
   */
  public void editAreaEvent(String command, String value) {
    try {
      if (command.equals("TXN_ID")) {
        PaymentTransaction origTxn = CMSTransactionPOSHelper.findById(theAppMgr, value);
        if (origTxn == null) {
          theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
          enterTxnId();
          return;
        }
        //vishal yevale 9 dec 2016 : gc buyback
        if (origTxn instanceof CMSRedeemableBuyBackTransaction) {
            theAppMgr.showErrorDlg(res.getString("Giftcard buyback or giftcard cashout transaction can not be voided"));
            enterTxnId();
            return;
          }
        //end vishal yevale 9 dec 2016
        orgTxn = origTxn;
        //Vivek Mishra : Added to restrict PRESALE Close void.
        if(orgTxn instanceof CMSCompositePOSTransaction && ((CMSCompositePOSTransaction)orgTxn).getOrgPRSOTxn()!=null)
        {
      	theAppMgr.showErrorDlg(res.getString("PRESALE Close transactions are not allowed to be voided. Please continue with return."));
      	theAppMgr.fireButtonEvent("CANCEL");
      	return;
        }
        //Test
        /*boolean flag = authorize(origTxn);
        if(flag=false)
        	return;*/
        Register curReg = (Register)theAppMgr.getGlobalObject("REGISTER");
        //Fix for issue 1249 - Rule to not allow Post Void from different registers.
        Store curStore = (Store)theAppMgr.getGlobalObject("STORE");
        if ((!curReg.getId().equals(origTxn.getRegisterId())) ||
        	(!curStore.getId().equals(origTxn.getStore().getId())))	{        		
          theAppMgr.showErrorDlg(res.getString("Cannot void transaction originated on a different register."));
          enterTxnId();
          return;
        }
        PaymentTransactionAppModel appModel = origTxn.getAppModel(CMSAppModelFactory.getInstance()
            , theAppMgr);
        
        //Fix for defect# 1247
        //Get Redeemable object based on the transaction id entered
        //if the object is not null, display error message
        //else insert a record in RK_REDM_HIST table
        DueBillIssue[] dueBillIssueArray = appModel.getDueBillIssuePaymentsArray();        
        String controlNumber = null;
        for (int i = 0; i < dueBillIssueArray.length; i++) {
        	controlNumber = dueBillIssueArray[i].getId();
            try {            	
            	cmsRedeemable = CMSRedeemableHelper.findRedeemable(theAppMgr, controlNumber);            	
            	if (cmsRedeemable != null) {
            		if ((cmsRedeemable.getRedemptionHistory()).size() > 0) {
            			theAppMgr.showErrorDlg(res.getString("Cannot void transaction with redeemed credit note"));
            			return;
            		} 
            	}
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        pnlHeader.showTransaction(appModel);
        pnlDetail.showTransaction(appModel);
        // make sure it has not been already voided
        try {
          origTxn.testIsVoidable();
          voidTxn = new CMSVoidTransaction((CMSStore)theStore);
          voidTxn.setOriginalTransaction(origTxn);
          voidTxn.setTheOperator((CMSEmployee)theOpr);
          //enterReason();
          postVoidDlg();
        } catch (Exception bx) {
       // } catch (BusinessRuleException bx) { vishal 6 dec 2016
          theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
          enterTxnId();
          pnlHeader.clear();
          pnlDetail.clear();
        }
      } else if (command.equals("REASON")) {
        try {
          voidTxn.setReason(value);
          lblReason.setText(value);
          theAppMgr.showMenu(MenuConst.VOID_TRANSACTION, "REASON", theOpr
              , (theAppMgr.OK_CANCEL_BUTTON | theAppMgr.PREV_BUTTON));
          selectOK();
        } catch (BusinessRuleException bx) {
          theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
          enterReason();
        }
      }
    //Vivek Mishra : Added to implementing manual authorization flow for POST VOID.
      else if (command.equals("MANUAL"))
      {

		   try{
			
			Payment pay = authPay;
			if (pay instanceof CreditCard) {
				CreditCard cc = (CreditCard) pay;
				cc.setManualOverride(value);
				sendSAFRequest(pay);
				
				//	}
				return;
			}}
 catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }}}catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);}
  }


  /**
   * put your documentation comment here
   */
  private void postVoidDlg() {
    String types[];
    String desc[];
    String title[];
    ConfigMgr config = new ConfigMgr("post_void.cfg");
    String strSolicitNoSale = config.getString("SOLICIT_POST_VOID_TYPES");
    if (strSolicitNoSale != null && strSolicitNoSale.trim().equals("false")) {
      //MSB - 09/07/05
      //Read reasons from ArmaniCommon.cfg
//      ConfigMgr armConfig = new ConfigMgr("ArmaniCommon.cfg");
      ArmConfigLoader armConfig = new ArmConfigLoader();
      String reasonTypes = armConfig.getString("POST_VOID_REASON_CD");
      //             String reasonTypes = config.getString("POST_VOID_REASON_CODES");
      ArrayList list = new ArrayList();
      String nextToken;
      String strSelectedReason;
      title = new String[1];
      title[0] = res.getString("Post_Void_Screen_Title");
      if ((reasonTypes != null) && (reasonTypes.trim().length() > 0)) {
        StringTokenizer stk = new StringTokenizer(reasonTypes, ",");
        types = new String[stk.countTokens()];
        desc = new String[stk.countTokens()];
        int i = 0;
        while (stk.hasMoreTokens()) {
          nextToken = stk.nextToken();
          types[i] = armConfig.getString(nextToken + ".CODE");
          desc[i] = armConfig.getString(nextToken + ".LABEL");
          if ((desc[i] != null) && (desc[i].trim().length() > 0)) {
            // MSB -09/07/05 -- Reasons are already locale specific
            // in ArmaniCommon.cfg
            //desc[i] = res.getString(desc[i]);
            list.add(new GenericChooserRow(new String[] {desc[i]
            }
                , types[i]));
          }
          i++;
        }
        GenericChooserRow[] chooserRows = (GenericChooserRow[])list.toArray(new GenericChooserRow[0]);
        GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
            , theAppMgr, chooserRows, title);
        dlg.setVisible(true);
        if (dlg.isOK()) {
          strSelectedReason = (String)dlg.getSelectedRow().getRowKeyData();
          String[] selectedReason = (String[])dlg.getSelectedRow().getDisplayRow();
          try {
            voidTxn.setReason(strSelectedReason);
            lblReason.setText(selectedReason[0]);
            theAppMgr.showMenu(MenuConst.VOID_TRANSACTION, "REASON", theOpr
                , (theAppMgr.OK_CANCEL_BUTTON | theAppMgr.PREV_BUTTON));
            selectOK();
          } catch (BusinessRuleException bre) {
            theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
          } catch (Exception e) {
            theAppMgr.showExceptionDlg(e);
          }
        }
      } else {
        try {
          //processNoSale(null);
          voidTxn.setReason("");
          lblReason.setText("");
          theAppMgr.showMenu(MenuConst.VOID_TRANSACTION, "REASON", theOpr
              , (theAppMgr.OK_CANCEL_BUTTON | theAppMgr.PREV_BUTTON));
          selectOK();
        } catch (BusinessRuleException bre) {
          theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
        } catch (Exception e) {
          theAppMgr.showExceptionDlg(e);
        }
      }
    }
  }


  /**
   *Method to clear the labels
   */
  private void enterTxnId() {
    theAppMgr.setSingleEditArea(res.getString("Enter transaction ID."), "TXN_ID"
        , theAppMgr.TRANS_ID_MASK);
  }


  /**
   *Method to clear the labels
   */
  private void enterReason() {
    theAppMgr.setSingleEditArea(res.getString("Enter reason."), "REASON");
  }


  /**
   *Method to clear the labels
   */
  private void selectOK() {
    theAppMgr.setSingleEditArea(res.getString("Select 'OK' to void this transaction."));
  }


  /**
   *Method to clear the labels
   */
  private void clear() {
    lblReason.setText("");
    pnlDetail.clear();
    pnlHeader.clear();
    repaint();
  }


  /**
   * Start the applet
   */
  public void stop() {}


  //
  public String getScreenName() {
    return res.getString("Void Transaction");
  }


  //
  public String getVersion() {
    return "$Revision: 1.1 $";
  }


  /**
   *Button event handler
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PREV")) {
      theAppMgr.goBack();
      ((CMSActionEvent)anEvent).consume();
    }
  }


  /**
   *
   * @param Header
   * @param anEvent
   */
  public void appButtonEvent(String Header, CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (Header.equals("REASON")) {
      if (sAction.equals("OK")) {
    	if("US".equalsIgnoreCase(Version.CURRENT_REGION) && fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y"))  {
    	//Vivek Mishra : Added for Sending Void(sale/refund) request and receiving response from AJB Server
    	  //Vivek Mishra : Changed the condition for not sending request in AJB Sequence in null(Original transaction was completed in offline mode.)
    	  Payment[] payments = orgTxn.getPaymentsArray();
    	  boolean isAuthRequired = false; 
    	  for(int i=0;i<payments.length;i++)
    	  {
    		 if(payments[i] instanceof CreditCard && ((CreditCard)payments[i]).getAjbSequence()!=null)
    		 {
    			 isAuthRequired = true;
    			 break;
    		 }
    		 //vishal yevale 3 Nov 2016
    		 if(fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y")){
    			 isAuthRequired = true;
    			 break;
    		 }
    		 //end vishal yevale
    	  }
    	  if(isAuthRequired){
    		  logger.info("DEBUG-PROD-ISSUE::::::In Void transaction applet buttonn click ::: "+ sAction);
    	  boolean flag = authorize(orgTxn);
    	  //Vivek Mishra : Added for POST VOID AJB Offline changes
    	  if(isOffline)
    	  {
    		  ((CMSActionEvent)anEvent).consume();//Ends
    		  return;
    	  } 
    	  else if(flag==false)
          	return;
    	  }
          //End
    	}
        if (!postTransaction())
          ((CMSActionEvent)anEvent).consume();
      } else if (sAction.equals("MODIFY_REASON")) {
        postVoidDlg();
      } else if (sAction.equals("CANCEL")) {
        //theAppMgr.goBack();
        //((CMSActionEvent) anEvent).consume();
      } else if (sAction.equals("PREV")) {
        clear();
        voidTxn = null;
        theAppMgr.showMenu(MenuConst.PREV_CANCEL, theOpr);
        enterTxnId();
        //((CMSActionEvent) anEvent).consume();
      }
      else if (sAction.equals("RETURN_ITEM")) {
			//Khyati: Commented to allow multiple return transaction in one Return/Sale TXn
			/*      // until Chelsea can handle multiple original txns on a return
			 if (theTxn.getReturnLineItemsArray().length > 0) {
			 theAppMgr.showErrorDlg(res.getString(
			 "Additional sales must be returned under a separate transaction."));
			 anEvent.consume();
			 }
			 }*/
			//End Khyati
			theAppMgr.addStateObject("TXN_MODE", new Integer(RETURN_MODE));
			theAppMgr.addStateObject("RETURN_TXN_POS", orgTxn);
			//enterOperator();  asks for mgr id
		}
    }
  }

  /**
   */
  private boolean postTransaction() {
    try {
      voidTxn.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr
          , (CMSStore)theStore, (CMSRegister)theAppMgr.getGlobalObject("REGISTER")));
      voidTxn.setSubmitDate(new java.util.Date());
      voidTxn.setProcessDate((Date)theAppMgr.getGlobalObject("PROCESS_DATE"));
      //Vivek Mishra : Added for Puerto Rico fical changes 
      String countryId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
	  String stateId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getState();
	  if("US".equalsIgnoreCase(Version.CURRENT_REGION) && stateId.equalsIgnoreCase("PR"))
           getFiscalNum();
      //Ends
      CMSTxnPosterHelper.post(theAppMgr, voidTxn);
      System.out.println("************************************************");
      System.out.println("******       POSTED TXN    " + voidTxn.getId() + "    ******");
      System.out.println("************************************************");
      PaymentTransactionAppModel voidTransactionAppModel = voidTxn.getAppModel(CMSAppModelFactory.
          getInstance(), theAppMgr);
      if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
        String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "VoidTransaction.rdo";
        try {
          ObjectStore objectStore = new ObjectStore(fileName);
          objectStore.write(voidTransactionAppModel);
        } catch (Exception e) {
          System.out.println("exception on writing object to blueprint folder: " + e);
        }
      }
      voidTransactionAppModel.printReceipt(theAppMgr);
      //IReceipt r = null;
      //r = new VoidTransactionReceipt(voidTxn);
      return true;
    } catch (BusinessRuleException bx) {
      theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
      LoggingServices.getCurrent().logMsg(getClass().getName(), "postTransaction()"
          , "Error posting void transaction.", "Error posting void transaction."
          , LoggingServices.MAJOR);
    }
    return false;
  }


  /**
   * callback when <b>Page Down</b> icon is clicked
   */
  public void pageDown(MouseEvent e) {
    pnlDetail.nextPage();
    theAppMgr.showPageNumber(e, pnlDetail.getCurrentPageNumber() + 1, pnlDetail.getPageCount());
  }


  /**
   * callback when <b>Page Up</b> icon is clicked
   */
  public void pageUp(MouseEvent e) {
    pnlDetail.prevPage();
    theAppMgr.showPageNumber(e, pnlDetail.getCurrentPageNumber() + 1, pnlDetail.getPageCount());
  }
  
  //Vivek Mishra : Added for Sending Void(sale/refund) request and receiving response from AJB Server
  
  private boolean authorize(PaymentTransaction orgTxn)
  {

	  isOffline = false;
		try {
			String result = "";
			String responseArray[] = null;
			String ajbResponse[] = null;
			//boolean isRefundPaymentRequired = orgTxn.isVoidable();
			boolean isRefundPaymentRequired = orgTxn.isVoidable();
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			Payment[] payments = orgTxn.getPaymentsArray();
			int len = payments.length;
			//vishal 3 Nov 2016 
			CMSCompositePOSTransaction theTxn=(CMSCompositePOSTransaction) orgTxn;
			boolean isReturnTxn = false;
			if(theTxn.getTransactionType().equals("RETN"))
			{
				isReturnTxn = true;
			}
			POSLineItem[] itemArray=theTxn.getSaleLineItemsArray();
			POSLineItemDetail[] saleItem=theTxn.getLineItemDetailsArray();
			for(int i=0;i<itemArray.length;i++){
				String ajbSequence=null;
				String giftCertficateId=null;
				String amount=null;
				if(((CMSSaleLineItem)itemArray[i]).getAjbSequence()!=null  && (((SaleLineItemDetail)saleItem[i]).getGiftCertificateId())!=null){
					ajbSequence=((CMSSaleLineItem)itemArray[i]).getAjbSequence();
					giftCertficateId=(((SaleLineItemDetail)saleItem[i]).getGiftCertificateId());
					String netAmount=String.valueOf(((SaleLineItemDetail)saleItem[i]).getNetAmount());
					if(netAmount.contains(",") && netAmount.contains("-")){
					int start=netAmount.indexOf(',');
					int end=netAmount.indexOf('-');
					 amount=netAmount.substring(start+1,end).trim();
				}else{
					amount=netAmount;
				}
				}
				if(giftCertficateId!=null ){
					boolean reload=false;
					if((giftCertficateId.charAt(giftCertficateId.length()-1))=='R'){
						giftCertficateId = giftCertficateId.replaceAll("R", "");
						reload=true;				
						}
				logger.info("DEBUG-PROD-ISSUE::::Gift Card ::Voiding From Void Transaction Applet :::::");
				boolean res=CMSCreditAuthHelper.validateGiftCardItemVoid(ajbSequence, giftCertficateId, reload, isReturnTxn);
				responseArray =new String[1];
				if(res){
				responseArray = null;
				
				}else{
					responseArray[0] =CMSCreditAuthHelper.getResponseErrorMsg();
					if(responseArray[0] != null && responseArray[0].toString().contains("All the Ajb Servers")){
						theAppMgr.showErrorDlg("Fipay Error "+responseArray[0]+"  Please press Return for refund.");
						isOffline=true;
						return false;
					}else{
						theAppMgr.showErrorDlg(responseArray[0]+"  Please press Return for refund.");
						isOffline=true;
						return false;
					}
				}
				
					}
				
				
				}
			
	        //end vishal yevale
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				//Vivek Mishra : Added to implementing manual authorization flow for POST VOID.
			       
				for(int j = 0; j<payments.length; j++){
				responseArray = null;
				//Vivek Mishra : Added check for Mobile tenders
				
				if(!storeCountry.equals("CAN") && payments[j] instanceof CreditCard && ((CreditCard)payments[j]).getAjbSequence()!=null && (((CreditCard)payments[j]).getCreditCardHolderName()==null || ((CreditCard)payments[j]).getCreditCardHolderName().equals(""))){
					if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
					String tt = orgTxn.getTransactionType();
					
					//responseArray = CMSCreditAuthHelper.validate(theAppMgr, orgTxn, register.getId(), false, true, false);
					//Vivek Mishra : Added code to freez the POS
					theAppMgr.setWorkInProgress(true);
					logger.info("DEBUG-PROD-ISSUE:::::Voiding From Void Transaction Applet original transaction amount::::"+orgTxn.getTotalPaymentAmount());
					logger.info("DEBUG-PROD-ISSUE:::::Voiding From Void Transaction Applet::::"+payments[j].getAmount());
					responseArray = CMSCreditAuthHelper.validateVoid(theAppMgr, orgTxn, register.getId(), false, true, false, payments[j], 1);
					theAppMgr.setWorkInProgress(false);
					if(responseArray!=null){
						ajbResponse = responseArray;
						//End
					int length = responseArray.length;
					//for (int i=0; i<length ;i++){	
					if(responseArray[0] != null && responseArray[0].toString().contains("All the Ajb Servers")){
						theAppMgr.showErrorDlg("All the AJB servers are down. Please press Return for refund.");
						//Vivek Mishra : Added for POST VOID AJB Offline changes
						/*theAppMgr.showMenu(MenuConst.RETURN_VOID_TRANSACTION, theOpr);*/
						//End
						isOffline=true;
						return false;
					}
					
					}
				}
				}
				// vishal yevale 3 nov 2016 giftcard void and gc item (reload void and activation void)
				if(!storeCountry.equals("CAN") && payments[j] instanceof Redeemable){
					if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y") && fipayGiftcardFlag!=null && fipayGiftcardFlag.equalsIgnoreCase("Y")){
						String tt = orgTxn.getTransactionType();
					String ajbSequence=null;
					String amount=null;
					String netAmount=null;
					if(payments[j] instanceof CMSStoreValueCard){
						ajbSequence=((CMSStoreValueCard)payments[j]).getAjbSequence();
						amount=String.valueOf(((CMSStoreValueCard)payments[j]).getAmount());
					}
					if(payments[j] instanceof CMSDueBill){
						ajbSequence=((CMSDueBill)payments[j]).getAjbSequence();
						amount=String.valueOf(((CMSDueBill)payments[j]).getAmount());
					}
					if(payments[j] instanceof CMSDueBillIssue){
						ajbSequence=((CMSDueBillIssue)payments[j]).getAjbSequence();
						amount=String.valueOf(((CMSDueBillIssue)payments[j]).getAmount());
					}
					if(amount.contains(",") && amount.contains("-")){
						int start=amount.indexOf(',');
						int end=amount.indexOf('-');
						 netAmount=amount.substring(start+1,end).trim();
					}else{
						netAmount=amount;
					}
					String id = ((Redeemable)payments[j]).getId();
					theAppMgr.setWorkInProgress(true);
					logger.info("DEBUG-PROD-ISSUE::::::In VoidTransasctionApplet reload void and activation void");
					boolean response=CMSCreditAuthHelper.validateGiftCardPaymentVoid(ajbSequence, id, netAmount,isReturnTxn);
					responseArray =new String[1];
					if(response){
					responseArray[0] ="0";
					}else{
						responseArray[0] =CMSCreditAuthHelper.getResponseErrorMsg();
						if(responseArray[0] != null && responseArray[0].toString().contains("All the Ajb Servers")){
							theAppMgr.showErrorDlg("Fipay Error "+responseArray[0]+" Please press Return for refund.");
							isOffline=true;
							return false;
						}else{
							theAppMgr.showErrorDlg(responseArray[0]+" Please press Return for refund.");
							isOffline=true;
							return false;
						}
					}
					theAppMgr.setWorkInProgress(false);
					if(responseArray!=null){
						ajbResponse = responseArray;
						//End
					int length = responseArray.length;
					
					
					}
				}
				}	
				//end vishal 3 nov 2016
				if (responseArray == null) {
					//return true;
					//return false;
					continue;
				}
				
				boolean responseFlag = false;
				//if(responseArray != null)
				
					//if (responseArray[i] != null) {
					if (responseArray[0] != null && responseArray[0].equals("0")) {
						result = result + responseArray[0];
						//return true;
						continue;
					//} else if (responseArray[i] == null) {
					} 
					//Decline both 1 and 3
					else if(responseArray[0] != null && (responseArray[0].equals("1"))){
						responseFlag=theAppMgr.showOptionDlg("AJB Error "
			                    , "Post Void request has been declined by AJB", res.getString("Retry"), res.getString("Cancel"));

						if(responseFlag)
						{
							//return authorize(orgTxn);
							j=j-1;
							continue;
						}
						else
						{
							//theAppMgr.fireButtonEvent("CANCEL");
							return false;
						}
					}
					//Vivek Mishra : Added to implement SAF flow for POST VOID.
					//Changed the manual Auth response code from 3 to 2
					//added the authcodes based on which SAF request can be sent.
					else if(responseArray[0] != null && (responseArray[0].equals("2") || responseArray[0] != null && responseArray[0].equals("3") && payments[j].isSAFable()))
					{

							authPay = payments[j];
							boolean safStatus = sendSAFRequest(authPay);
							if(safStatus)
								continue;
					}
					//Ends
					else{
						responseFlag=theAppMgr.showOptionDlg("AJB Error "
			                    , "Error received from AJB", res.getString("Retry"), res.getString("Cancel"));

						if(responseFlag)
						{
							logger.info("DEBUG-PROD-ISSUE:::::::In Void Transaction applet:::AJB Error ::RETRY");
							return authorize(orgTxn);
						}
						else
						{
							//theAppMgr.fireButtonEvent("CANCEL");
							return false;
						}
					}
					
					//System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKResult : "+result+"KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
				
			//return (true);
				
				}
				//return (false);
				return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}
	
  }
  
//Vivek Mishra : Added to implement SAF flow for POST VOID.
	private boolean sendSAFRequest(Payment pay) {
		try {
			String result = "";
	
			String responseArray[] = null;
			String ajbResponse[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				Payment[] payments = orgTxn.getPaymentsArray();
				int len = payments.length;
				//Vivek Mishra : Added check for Mobile tenders
				//if(!storeCountry.equals("CAN")){
				if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
				if(!storeCountry.equals("CAN") && (((CreditCard)pay).getCreditCardHolderName()==null || ((CreditCard)pay).getCreditCardHolderName().equals(""))){
					//String tt = orgTxn.getTransactionType();
					logger.info("DEBUG-PROD-ISSUE::::::SAF Voiding From Void Transaction Applet::::");
						responseArray = CMSCreditAuthHelper.validateSAF(theAppMgr, orgTxn, register.getId(), false, true, true,pay);
				
						for (int x = 0; x < payments.length; x++) {
//						
							
							if(payments[x].getRespStatusCode().equals("0")){
								return true;
					
							}else{
								return false;
							}
						}
						
					//	if(){}
						
					if(responseArray!=null){
						ajbResponse = responseArray;
						//End
					int length = responseArray.length;
					//for (int i=0; i<length ;i++){
					//Vivek Mishar : Added one codition for blank respose when AJB server gets down in middle of transaction.	
					//if(responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers")||responseArray[i].equals(""))){
						//Vivek Mishra : Removed the blak condition as it was causing the All AJB Server down dialogue even in case of server is recover. 
						if(responseArray[0] != null && responseArray[0].toString().contains("All the Ajb Servers")){
						//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
						//if(!Refresh)
							//count++;
						/*if(count==qty)
						{*/
						theAppMgr.showErrorDlg("All the AJB servers are down");
						/*count=0;
						}//End
*/						return false;
					}
					
					}
				}}else
				
				if (responseArray == null) {
					return true;
				}
			
							
			//count=0;	
			return (true);
		} catch (Exception ex) {
			ex.printStackTrace();
			theAppMgr.showExceptionDlg(ex);
			return (false);
		}
	}
	//Ends
	
	//Vivek Mishra : Added for Puerto Rico fical changes 
	
	private boolean getFiscalNum()
	  {
		  PaymentTransaction theTxnPOS = (PaymentTransaction) theAppMgr.getStateObject("TXN_POS");
			  
		  String txnType = "Void";
		  Payment[] payments = orgTxn.getPaymentsArray();
		  String cardType = null;
		  CreditCard cc = null;
		  String last4 = null;
		  String authCode = null;
		  String tranId = null;
		  String invId = (orgTxn.getId().replace("*", "")) + Calendar.getInstance().get(Calendar.MILLISECOND);
		  //Vivek Mishra : Changed to fix transaction posting issue in case of 100% discount
		  //if(payments.length == 0 || payments.length > 1)
		  if(payments.length == 0 || payments.length > 1)
			  cardType = "Cash";
		  else if(payments.length == 1)
		  {
			  if(payments[0] instanceof CreditCard)
			  {
				  cc = (CreditCard)payments[0];
				  if(cc.getGUIPaymentName().contains("Visa"))
					  cardType = "Visa";
				  else if(cc.getGUIPaymentName().contains("Master"))
					  cardType = "MasterCard";
				  else if(cc.getGUIPaymentName().contains("Discover"))
					  cardType = "Discover";
				  else if(cc.getGUIPaymentName().contains("American"))
					  cardType = "AmericanExpress";
				  else if(cc.getGUIPaymentName().contains("Debit"))
					  cardType = "DebitCard";
				  else
					  cardType = "Cash";
				  last4 = cc.getMaskCardNum().substring(cc.getMaskCardNum().length()-4, cc.getMaskCardNum().length());
				  authCode = cc.getRespAuthorizationCode();
				  tranId = cc.getAjbSequence();
			  }
			  
			  else if(payments[0] instanceof CMSBankCheck)
			  {
				  cardType = "Check";
				  authCode = ((CMSBankCheck)payments[0]).getRespAuthorizationCode();
			  }
			  
			  else 
				  cardType = "Cash";
		  }
		  	TransactionRequest _request = new TransactionRequest();
		    TransactionResponse _response = new TransactionResponse();
		    
		     _request.setPay_id(orgTxn.getId().replace("*", ""));
		     System.out.println("*************************************** Request payid : "+_request.getPay_id()+"************************************");
		     _request.setPay_cardtype(CARDTYPE.valueOf(cardType));
			 System.out.println("*************************************** Request paycardtype : "+_request.getPay_cardtype()+"************************************");
			 _request.setPay_amount(Float.parseFloat(orgTxn.getTotalPaymentAmount().getAbsoluteValue().stringValue()));
			 System.out.println("*************************************** Request payamount : "+_request.getPay_amount()+"************************************");
			 _request.setPay_timecreated_device(new Date());
			 System.out.println("*************************************** Request paytimecreateddevice : "+_request.getPay_timecreated_device()+"************************************");
			 _request.setPay_transid(tranId);
			 System.out.println("*************************************** Request paytransid : "+_request.getPay_transid()+"************************************");
			 CMSCompositePOSTransactionAppModel origTxn = new CMSCompositePOSTransactionAppModel((CMSCompositePOSTransaction)orgTxn);
			 try {
				_request.setTax1(Float.parseFloat(origTxn.getStateTaxAmount().getAbsoluteValue().stringValue()));
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CurrencyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 System.out.println("*************************************** Request Tax1 : "+_request.getTax1()+"************************************");
			 try {
				_request.setTax2(Float.parseFloat(origTxn.getCityTaxAmount().getAbsoluteValue().stringValue()));
				System.out.println("*************************************** Request Tax2 : "+_request.getTax2()+"************************************");
			} catch (CurrencyException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 _request.setTipAmount(0.0f);
			 System.out.println("*************************************** Request tipAmount : "+_request.getTipAmount()+"************************************");
			 _request.setAuthcode(authCode);
			 System.out.println("*************************************** Request authcode : "+_request.getAuthcode()+"************************************");
			 _request.setCCNUMLast4(last4);
			 System.out.println("*************************************** Request CCNUMLast4 : "+_request.getCCNUMLast4()+"************************************");
			 _request.setInv_id(invId);
			 System.out.println("*************************************** Request invid : "+_request.getInv_id()+"************************************");
			 
			//_request.settranstype(Sale);
			 _request.setTrans_type(TRANSTYPE.Void);
			 System.out.println("*************************************** Request transtype : "+_request.getTrans_type()+"************************************");
			 IVU _ivu = new IVU();
			 try {
				_response =  (TransactionResponse)_ivu.transactionRequest(_request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 System.out.println("*************************************** Response PayId : "+_response.getPay_id()+"************************************");
			 System.out.println("*************************************** Response IVUNumber : "+_response.getIVUNumber()+"**************************************");
			 System.out.println("*************************************** Response IVUNumber : "+_response.getIVUprocessor()+"**************************************");
			 
			 if(_response.getIVUNumber()!=null)
			 {
				 System.out.println("**************************************** Inside PaymentApplet's getFiscal method to set fical number : "+_response.getIVUNumber()+"**********************************************");
				 /*((CMSCompositePOSTransaction)theTxnPOS).setFiscalReceiptNumber(_response.getIVUNumber());
				 ((CMSCompositePOSTransaction)theTxnPOS).setFiscalReceiptDate(new Date());
*/				 ((CMSCompositePOSTransaction)orgTxn).setFiscalReceiptNumber(_response.getIVUNumber());
				 ((CMSCompositePOSTransaction)orgTxn).setFiscalReceiptDate(new Date());
				 ((CMSCompositePOSTransaction)orgTxn).setIvuProcessor(_response.getIVUprocessor());
			 }
			 
		  return true;
	  }
	
	//Ends
  
}
