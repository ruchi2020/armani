/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package com.chelseasystems.cs.swing.menu;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.MenuPanel;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentResponse;
import com.chelseasystems.cs.util.FiscalDocumentUtil;
import com.chelseasystems.cs.fiscaldocument.FiscalInterface;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cr.config.ConfigMgr;

public class InitialMgmtMenuApplet extends CMSApplet {
	private FiscalDocumentUtil fiscalDocUtil = null;
	private FiscalInterface fiscalInterface = null;
	private static ConfigMgr config;
	private String fipay_flag;

	// Initialize the applet
	public void init() {
		try {
			jbInit();
			ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
			String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
			if (sClassName != null) {
				Class cls = Class.forName(sClassName);
				fiscalInterface = (FiscalInterface) cls.newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Component initialization
	private void jbInit() throws Exception {
		MenuPanel pnlMenu = new MenuPanel();
		this.getContentPane().add(pnlMenu, BorderLayout.CENTER);
		pnlMenu.setAppMgr(theAppMgr);
		pnlMenu.setTitle(res.getString("Management Menu"));
	}

	// Start the applet
	public void start() {
	 	String fileName = "store_custom.cfg";
		config = new ConfigMgr(fileName);
		fipay_flag = config.getString("FIPAY_Integration");
		
		 //Default value of the flag is Y if its not present in credit_auth.cfg
		if (fipay_flag == null) {
			fipay_flag = "Y";
		}
		
		fiscalDocUtil = new FiscalDocumentUtil();
		theAppMgr.setTransitionColor(theAppMgr.getTheme().getMenuBackground());
		theOpr = (com.chelseasystems.cs.employee.CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		theAppMgr.showMenu(MenuConst.MANAGEMENT, "MAINMENU", theOpr, theAppMgr.PREV_BUTTON);
		theAppMgr.setSingleEditArea(res.getString("Select option."));
		theAppMgr.startTimeOut(60);
		boolean clearMessage =  true;
		//for clearing the screen on login screen
		if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){
			sendIdleMessageData(null,null,false,true,clearMessage,"");
		}
	}

	// Stop the applet
	public void stop() {
		theAppMgr.endTimeOut();
	}

	// Destroy the applet
	public void destroy() {
	}

	// Get Applet information
	public String getVersion() {
		return "$Revision: 1.1 $";
	}

	/**
	 * @return
	 */
	public String getScreenName() {
		return res.getString("Management Menu");
	}

	/**
	 * Handles SubMenu
	 * @param sHeader
	 *            String
	 * @param anEvent
	 *            CMSActionEvent
	 */
	public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
		theAppMgr.unRegisterSingleEditArea();
		String sAction = anEvent.getActionCommand();
		String sTmp = "";
		if (sAction.equals("PREV")) {
			if (sHeader.equals("MODIFY_FISCAL")) {
				anEvent.consume();
				theAppMgr.showMenu(MenuConst.MANAGEMENT, "MAINMENU", theOpr, theAppMgr.PREV_BUTTON);
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
		} else if (sAction.equals("OPEN_DRAWER")) {
			anEvent.consume();
			System.out.println("%%% Opening Drawer ... %%%");
			fiscalInterface.openDrawer();
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
	}

	/**
	 * put your documentation comment here
	 * @param sInput
	 */
	private void modifyDDTNumber(String sInput) {
		try {
			if (!fiscalDocUtil.setNextDDTNumber(sInput)) {
				theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse.getErrorMessage(fiscalDocUtil.getResponseCode())));
				selectModifyDDT();
				return;
			}
			theAppMgr.showErrorDlg(res.getString("DDT number modified to ") + sInput);
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString("Can't modify DDT number, contact technical support"));
		}
		theAppMgr.setSingleEditArea(res.getString("Select option."));
	}

	/**
	 * put your documentation comment here
	 * @param sInput
	 */
	private void modifyTAXNumber(String sInput) {
		try {
			if (!fiscalDocUtil.setNextVATNumber(sInput)) {
				theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse.getErrorMessage(fiscalDocUtil.getResponseCode())));
				selectModifyTaxNumber();
				return;
			}
			theAppMgr.showErrorDlg(res.getString("Tax number modified to ") + sInput);
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString("Can't modify Tax number, contact technical support"));
		}
		theAppMgr.setSingleEditArea(res.getString("Select option."));
	}

	/**
	 * put your documentation comment here
	 * @param sInput
	 */
	private void modifyCreditNote(String sInput) {
		try {
			if (!fiscalDocUtil.setNextCreditNoteNumber(sInput)) {
				theAppMgr.showErrorDlg(res.getString(FiscalDocumentResponse.getErrorMessage(fiscalDocUtil.getResponseCode())));
				selectModifyCreditNote();
				return;
			}
			theAppMgr.showErrorDlg(res.getString("CreditNote number modified to ") + sInput);
		} catch (Exception e) {
			theAppMgr.showErrorDlg(res.getString("Can't modify CreditNote number, contact technical support"));
		}
		theAppMgr.setSingleEditArea(res.getString("Select option."));
	}

	/**
	 * @param Command
	 * @param Value
	 */
	public void editAreaEvent(String Command, String Value) {
		// if (Command.equals("CREDIT_CARD")) { // credit card swipe date
		// String Builder = PaymentMgr.getPaymentBuilder(Command);
		// System.out.println("the builder is: " + Builder);
		// theAppMgr.buildObject("PAYMENT", Builder, Value);
		// }
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
	}

	/**
	 * put your documentation comment here
	 * @param sValue
	 * @return98
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
	
	private boolean sendIdleMessageData(POSLineItem line,POSLineItem[] lineItemArray ,boolean Refresh, boolean idleMessage, boolean clearMessage, String discountAmt) {
		try {
			String result = "";
			//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
				//End
			String responseArray[] = null;
			String ajbResponse[] = null;
			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
			
				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
				//Changes for Canada validate method needs to pass
				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
				if(!storeCountry.equals("CAN")){
					//String tt = orgTxn.getTransactionType();
					responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line,lineItemArray,Refresh,idleMessage,clearMessage,discountAmt,false);
					if(responseArray!=null){
					//End
					int length = responseArray.length;
					for (int i=0; i<length ;i++){
					//Vivek Mishar : Added one codition for blank respose when AJB server gets down in middle of transaction.	
					//if(responseArray[i] != null && (responseArray[i].toString().contains("All the Ajb Servers")||responseArray[i].equals(""))){
						//Vivek Mishra : Removed the blak condition as it was causing the All AJB Server down dialogue even in case of server is recover. 
						if(responseArray[i] != null && responseArray[i].toString().contains("All the Ajb Servers")){
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
					}
				}else
				
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

}
