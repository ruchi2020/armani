/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.transaction;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterSessionAppModel;
import com.chelseasystems.cs.swing.model.RedeemableHistoryModel;
import com.chelseasystems.cs.authorization.bankcard.CMSCreditAuthHelper;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransaction;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.payment.RedeemableHist;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 */
public class RedeemableBuyBackApplet extends CMSApplet {
	static final long serialVersionUID = 6986630571992302569L;

	JCMSTextField jtfId = new JCMSTextField();
	JCMSTextField jtfPurchaseDate = new JCMSTextField();
	JCMSTextField jtfCustomer = new JCMSTextField();
	JCMSTextField jtfPhone = new JCMSTextField();
	JCMSTextField jtfIssueAmt = new JCMSTextField();
	JCMSTextField jtfCurrentAmt = new JCMSTextField();
	JCMSTextField jtfType = new JCMSTextField();
	RedeemableHistoryModel model = new RedeemableHistoryModel();
	JCMSTable tblHist = new JCMSTable(model, JCMSTable.VIEW_ROW);
	private CMSEmployee theOpr;
	private Redeemable redeemable;
	private SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
	//Start: Added by Himani for redeemable buy back fipay integration
	 private static ConfigMgr config;
	 private String fipay_gc_flag;
	 private String gcID;
	 private Object gcBalance;
	 public CMSEmployee theApprover;
	 protected boolean isPasswordEntered;
	 protected int passwordWrongCount;
	 private boolean isGCdtl = false;
	//End: Added by Himani for redeemable buy back fipay integration
	/**
	 *
	 */
	public RedeemableBuyBackApplet() {
	}

	/**
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		JPanel jPanel1 = new JPanel();
		JPanel pnlHist = new JPanel();
		JCMSLabel lblPurchaseDate = new JCMSLabel();
		JCMSLabel lblId = new JCMSLabel();
		JCMSLabel lblPhone = new JCMSLabel();
		JCMSLabel lblIssueAmt = new JCMSLabel();
		JCMSLabel lblCustomer = new JCMSLabel();
		JCMSLabel lblCurrentAmount = new JCMSLabel();
		JCMSLabel lblType = new JCMSLabel();
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		jPanel1.setLayout(gridBagLayout1);
		pnlHist.setLayout(new BorderLayout());
		pnlHist.add(tblHist.getTableHeader(), BorderLayout.NORTH);
		pnlHist.add(tblHist, BorderLayout.CENTER);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jtfId, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
		jPanel1.add(lblCustomer, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 70, 1));
		jPanel1.add(jtfCustomer, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 250, 0));
		jPanel1.add(lblIssueAmt, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 60, 0));
		jPanel1.add(jtfIssueAmt, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
		jPanel1.add(lblId, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 41, 1));
		jPanel1.add(pnlHist, new GridBagConstraints(0, 7, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(7, 9, 14, 8), 487, 239));
		jPanel1.add(jtfPurchaseDate, new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 90), 90, 0));
		jPanel1.add(lblPurchaseDate, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 9, 0, 64), 0, 0));
		jPanel1.add(lblPhone, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 9, 0, 56), 90, 1));
		jPanel1.add(jtfPhone, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 90), 90, 0));
		jPanel1.add(jtfCurrentAmt, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
		jPanel1.add(lblCurrentAmount, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 36, 1));
		jPanel1.add(lblType, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 9, 0, 0), 36, 1));
		jPanel1.add(jtfType, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 9, 0, 50), 60, 0));
		lblType.setAppMgr(theAppMgr);
		lblType.setText(res.getString("Redeemable Type"));
		jtfType.setEnabled(false);
		jtfType.setEditable(false);
		jtfType.setAppMgr(theAppMgr);
		jtfType.setEnabled(false);
		jtfType.setEditable(false);
		jtfType.setAppMgr(theAppMgr);
		lblId.setText(res.getString("Reedemable ID"));
		lblPurchaseDate.setText(res.getString("Purchase Date"));
		lblPurchaseDate.setAppMgr(theAppMgr);
		lblCustomer.setText(res.getString("Customer"));
		lblPhone.setText(res.getString("Customer Id"));
		lblIssueAmt.setText(res.getString("Issue Amount"));
		lblCurrentAmount.setText(res.getString("Current Balance"));
		lblId.setAppMgr(theAppMgr);
		jtfId.setAppMgr(theAppMgr);
		jtfId.setEditable(false);
		jtfId.setEnabled(false);
		jtfPurchaseDate.setAppMgr(theAppMgr);
		jtfPurchaseDate.setEditable(false);
		jtfPurchaseDate.setEnabled(false);
		lblCustomer.setAppMgr(theAppMgr);
		jtfCustomer.setAppMgr(theAppMgr);
		jtfCustomer.setEditable(false);
		jtfCustomer.setEnabled(false);
		lblPhone.setAppMgr(theAppMgr);
		jtfPhone.setAppMgr(theAppMgr);
		jtfPhone.setEditable(false);
		jtfPhone.setEnabled(false);
		lblIssueAmt.setAppMgr(theAppMgr);
		jtfIssueAmt.setAppMgr(theAppMgr);
		jtfIssueAmt.setEditable(false);
		jtfIssueAmt.setEnabled(false);
		lblCurrentAmount.setAppMgr(theAppMgr);
		jtfCurrentAmt.setAppMgr(theAppMgr);
		jtfCurrentAmt.setEditable(false);
		jtfCurrentAmt.setEnabled(false);
		tblHist.setAppMgr(theAppMgr);
		jPanel1.setBackground(theAppMgr.getBackgroundColor());
		tblHist.addComponentListener(new java.awt.event.ComponentAdapter() {

			/**
			 * @param e
			 */
			public void componentResized(ComponentEvent e) {
				model.setRowsShown(tblHist.getHeight() / tblHist.getRowHeight());
			}
		});
	}

	/**
	 *
	 */
	public void init() {
		try {
			jbInit();
			//Start: Added by Himani for redeemable buy back fipay integration
		    String fileName = "store_custom.cfg";
			config = new ConfigMgr(fileName);
			fipay_gc_flag = config.getString("FIPAY_GIFTCARD_INTEGRATION");
			if(fipay_gc_flag == null){
				fipay_gc_flag = "N";
			}
			//End: Added by Himani for redeemable buy back fipay integration
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public String getScreenName() {
		return (res.getString("Redeemable BuyBack"));
	}

	//
	public String getVersion() {
		return ("$Revision: 1.9.2.3.4.4 $");
	}

	// Stop the applet
	public void stop() {
		isGCdtl = false;
	}

	// Destroy the applet
	public void destroy() {
	}

	/**
	 *
	 */
	public void start() {
		clear();
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		//Added by Himani for redeemable buy back fipay Integration
		if (Version.CURRENT_REGION.equalsIgnoreCase("US") && fipay_gc_flag!=null && fipay_gc_flag.toUpperCase().equals("Y"))
		theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr); 
		else
			theAppMgr.showMenu(MenuConst.PREV_CANCEL, theOpr);
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		CMSRedeemableBuyBackTransaction txn = (CMSRedeemableBuyBackTransaction) theAppMgr.getStateObject("TXN_POS");
		if (txn != null) {
			redeemable = txn.getRedeemable();
			if (redeemable != null) {
				displayRedeemable();
				theAppMgr.showMenu(MenuConst.REDEEMABLE_BUY_BACK, "REDEEMABLE", theOpr);
				theAppMgr.setSingleEditArea(res.getString("Select option."));
			}
		} else {
			// Calling the AppButton
			if (Version.CURRENT_REGION.equalsIgnoreCase("US")){ //Added by Himani for redeemable buy back fipay Integration
			 if((fipay_gc_flag.toUpperCase().equals("N"))) //Added by Himani for redeemable buy back fipay Integration
			  {
				 RedeemableBuyBackApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "REDEEM_CARD", 0));
			  }
			//Start: Added by Himani for redeemable buy back fipay integration
			 else if(!isGCdtl)
			 {
				 Object gcDetails = CMSCreditAuthHelper.getGCTrackData(theAppMgr);
				 isGCdtl = true;
				 if(gcDetails != null)
				 {
				 gcID=(gcDetails.toString().split(","))[0];
				 gcBalance=(gcDetails.toString().split(","))[1];

					if(gcBalance != null){
						//gcBalance = (Object)(Double.parseDouble(gcBalance.toString())/100);
						
					//	ArmCurrency gcbal=(ArmCurrency)gcBalance;
												
						//jtfCurrentAmt.setText("$"+gcBalance.toString());
						//theAppMgr.showErrorDlg("The Balance Amount on the card is :$" + gcBalance.toString());
						RedeemableBuyBackApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "REDEEM_ID", 0));
					}
				 }
				 else
				 {
					String errmsg= CMSCreditAuthHelper.getResponseErrorMsg();
					if(errmsg!=null && errmsg!="")
						theAppMgr.showErrorDlg(errmsg);
				    CMSCreditAuthHelper.setResponseErrorMsg(""); 
					theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
					theAppMgr.setSingleEditArea(res.getString("Select option."));
				 }
			 }
			 else{
				  isGCdtl = false;
			 }
			}
			else
				RedeemableBuyBackApplet.this.appButtonEvent(new CMSActionEvent(this, 0, "REDEEM_CARD", 0));
			//End: Added by Himani for redeemable buy back fipay integration
		}
	}

	/**
	 *
	 */
	private void enterRedeemable() {
		theAppMgr.setSingleEditArea(res.getString("Enter/Scan the redeemable control ID"), "REDEEMABLE_ID");
	}

	// callback whan an application tool bar button is pressed
	public void editAreaEvent(String command, String input) {
		try {
			if (command.equals("COMMENT")) {
				if (Version.CURRENT_REGION.equalsIgnoreCase("US")){ //Added by Himani for redeemable buy back fipay Integration
				if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")) //Added by Himani for redeemable buy back fipay Integration
				{
					if (redeemable == null) {
						throw new BusinessRuleException(res.getString("You must first look up a redeemable."));
					} 
					else if (redeemable.getRemainingBalance().doubleValue() <= 0) {
						throw new BusinessRuleException(res.getString("The redeemable has no remaining value."));
					} else {
						CMSRedeemableBuyBackTransaction txn = new CMSRedeemableBuyBackTransaction((CMSStore) theStore);
						txn.setRedeemable(redeemable);
						txn.setAmount(new ArmCurrency(redeemable.getRemainingBalance().doubleValue() * -1));
						txn.setProcessDate((Date) theAppMgr.getGlobalObject("PROCESS_DATE"));
						txn.setTheOperator((CMSEmployee) theOpr);
						txn.setComment(input);
						theAppMgr.addStateObject("TXN_POS", txn);
						theAppMgr.fireButtonEvent("OK");
					}
				}
				//Start: Added by Himani for redeemable buy back fipay Integration
				else
				{
					if (redeemable == null) {
						throw new BusinessRuleException(res.getString("You must first look up a redeemable."));
					} 
					else if ( Double.parseDouble(gcBalance.toString()) <= 0.0) {
						throw new BusinessRuleException(res.getString("The redeemable has no remaining value."));
					} else {
						CMSRedeemableBuyBackTransaction txn = new CMSRedeemableBuyBackTransaction((CMSStore) theStore);
						txn.setRedeemable(redeemable);
						txn.setAmount(new ArmCurrency(Double.parseDouble(gcBalance.toString()) * -1));
						txn.setProcessDate((Date) theAppMgr.getGlobalObject("PROCESS_DATE"));
						txn.setTheOperator((CMSEmployee) theOpr);
						txn.setComment(input);
						/*boolean success=CMSCreditAuthHelper.validateRedeemableCashout(txn,gcBalance.toString(),theAppMgr);
						if(success==false) //commented by vishal yevale
						{*/
							theAppMgr.addStateObject("TXN_POS", txn);
							theAppMgr.fireButtonEvent("OK");
						//}
					}
				}
				}
				else{
					if (redeemable == null) {
						throw new BusinessRuleException(res.getString("You must first look up a redeemable."));
					} 
					else if (redeemable.getRemainingBalance().doubleValue() <= 0) {
						throw new BusinessRuleException(res.getString("The redeemable has no remaining value."));
					} else {
						CMSRedeemableBuyBackTransaction txn = new CMSRedeemableBuyBackTransaction((CMSStore) theStore);
						txn.setRedeemable(redeemable);
						txn.setAmount(new ArmCurrency(redeemable.getRemainingBalance().doubleValue() * -1));
						txn.setProcessDate((Date) theAppMgr.getGlobalObject("PROCESS_DATE"));
						txn.setTheOperator((CMSEmployee) theOpr);
						txn.setComment(input);
						theAppMgr.addStateObject("TXN_POS", txn);
						theAppMgr.fireButtonEvent("OK");
					}
				}
		
					
				//End: Added by Himani for redeemable buy back fipay Integration
			}
			
			//Start: Added by Himani for redeemable buy back fipay Integration
			if(command.equals("APPROVER"))
			{
				isPasswordEntered = false;
				CMSEmployee theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
				//System.out.println("--- In InitialSaleApplet class : editAreaEvent()-  theOpr  :"+theOpr.getExternalID());
				
				theApprover = CMSEmployeeHelper.findByExternalId(theAppMgr, input);
				if (theApprover == null){
					theAppMgr.showErrorDlg(res.getString("You have entered wrong employee id."));
					enterUserName();
					return;
				}
				 if (testCanBeOperator()){
						 if( !theApprover.getExternalID().equals(theOpr.getExternalID())) {
							// if (isPasswordRequired && !isPasswordEntered) {
							 if(!isPasswordEntered){
								 enterPassword();
								 return;
							 }
						 }else{
								theAppMgr.showErrorDlg(res.getString("An additional employee's User-ID and password must be entered to approve the return."));
								enterUserName();
								return;
						}
				 }else{
						enterUserName();
						return;
				}		
				//this.buildApprover(sEdit,theTxn);
				//initHeaders();
				//theAppMgr.fireButtonEvent("RETURN_ITEM");
				theAppMgr.addStateObject("APPROVER", input);
				return;
			}
			if (command.equals("PASSWORD")) {
				if(!input.equals(null)){
					if(processPasswordEntry((String)input)){
						isPasswordEntered = true;
				      	
				      		if(!theApprover.equals(null)){
				      			theAppMgr.setSingleEditArea(res.getString("Enter comments."), "COMMENT");
				      			}
				      			return;		
						
					}else{
						if ((passwordWrongCount < 3)|| (input.length() == 0)) {
					          enterPassword();
					    } else {
					          passwordWrongCount = 0;
					          enterUserName();
					    }
					}
				}
				return;
			}
			//End: Added by Himani for redeemable buy back fipay Integration
		} catch (CurrencyException ce) {
			theAppMgr.showExceptionDlg(ce);
		} catch (BusinessRuleException bre) {
			theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}
	
	//Start: Added by Himani for redeemable buy back fipay Integration
	protected void enterUserName() {
	    theAppMgr.setSingleEditArea(res.getString("Enter approver id for cashout and press 'Enter'.")
	        , "APPROVER", theAppMgr.PASSWORD_MASK);
	    //theAppMgr.setEditAreaFocus();
	  }
	
	private void enterPassword() {
		
	    theAppMgr.setSingleEditArea(res.getString("Please enter your secret password.")
	        , "PASSWORD", theAppMgr.PASSWORD_MASK);
		
		theAppMgr.setEditAreaFocus();
		return;
	}
	
	protected boolean processPasswordEntry(String password) {
	    
	    if (theApprover.getPassword()!=null && theApprover.getPassword().equals(password)) {
	      return (true);
	    } else {
	      theAppMgr.showErrorDlg(res.getString("Incorrect Password entered"));
	      passwordWrongCount++;
	      return (false);
	    }
	  }
 
	protected boolean testCanBeOperator() {
	 try {
		 theApprover.testCanBeOperator();
	     return (true);
	 } catch (BusinessRuleException bx) {
	    theAppMgr.showErrorDlg(res.getString("A terminated employee cannot login to the system"));
	    invalidLogOnAttempt(theApprover.getExternalID());
	    return (false);
	 }
	}
	
	protected void invalidLogOnAttempt(String id) {
		 CMSRegisterSessionAppModel sessionAppModel = new CMSRegisterSessionAppModel();
		 sessionAppModel.setRegister((CMSRegister)theAppMgr.getGlobalObject("REGISTER"));
		 sessionAppModel.setSessionDate(new Date());
		 sessionAppModel.setStore((CMSStore)theAppMgr.getGlobalObject("STORE"));
		 sessionAppModel.setLogonEntered(id);
		 sessionAppModel.print(ReceiptBlueprintInventory.CMSInvalidLogonAttempt, theAppMgr);
	}
	
	//End: Added by Himani for redeemable buy back fipay Integration

	/**
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		// MP: For Redeemable Card Bldr.
		if (sAction.equals("REDEEM_CARD")) {
			theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
			ConfigMgr config = new ConfigMgr("payment.cfg");
			final String Builder = config.getString("REDEEMABLE_INQUIRY_BUILDER");
			System.out.println("the builder is: " + Builder);
			SwingUtilities.invokeLater(new Runnable() {

				/**
				 * put your documentation comment here
				 */
				public void run() {
					theAppMgr.buildObject("REDEEMABLE_INQUIRY", Builder, "");
				}
			});
		}
		//Start: Added by Himani for redeemable buy back fipay integration
		else if(sAction.equals("REDEEM_ID")){
			theAppMgr.unRegisterSingleEditArea(); // remove any builders in process
			ConfigMgr config = new ConfigMgr("payment.cfg");
			final String Builder = config.getString("REDEEMABLE_INQUIRY_BUILDER");
			System.out.println("the builder is: " + Builder);
			SwingUtilities.invokeLater(new Runnable() {

				/**
				 * put your documentation comment here
				 */
				public void run() {
					theAppMgr.buildObject("REDEEMABLE_INQUIRY", Builder, gcID);
				}
			});
		} 
		//End: Added by Himani for redeemable buy back fipay integration
		else if (sAction.equals("PREV")) {
			theAppMgr.goBack();
			anEvent.consume();
		}
	}

	/**
	 * put your documentation comment here
	 * @param sHeader
	 * @param anEvent
	 */
	public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sHeader.equals("REDEEMABLE")) {
			// Modified by Himani to change name to cash out
			if (sAction.equals("BUY_BACK1")) {
				if (redeemable != null && (redeemable instanceof RewardCard || redeemable instanceof HouseAccount)) {
					theAppMgr.showErrorDlg(res.getString("INVALID REDEEMABLE: Buy-back not allowed for Loyalty Reward Card and House Account"));
					return;
				}
				theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "COMMENTS", theOpr);
				if (Version.CURRENT_REGION.equalsIgnoreCase("US")){ //Added by Himani for redeemable buy back fipay Integration
				if(fipay_gc_flag==null || fipay_gc_flag.toUpperCase().equals("N")) //Added by Himani for redeemable buy back fipay Integration
					theAppMgr.setSingleEditArea(res.getString("Enter comments."), "COMMENT");
				//Start: Added by Himani for redeemable buy back fipay Integration
				else
				{
					CMSStore s = (CMSStore) AppManager.getCurrent().getGlobalObject("STORE");
					if(!(s.getCompState().equalsIgnoreCase("CA")) && (Double.parseDouble(gcBalance.toString()) > 3.0))
					{
						theAppMgr.setSingleEditArea(res.getString("Enter approver id for cash out and press 'Enter'.")
						        , "APPROVER", theAppMgr.PASSWORD_MASK);
					}
					else
					{
						theAppMgr.setSingleEditArea(res.getString("Enter comments."), "COMMENT");
					}
						
				}
				}
				else
					theAppMgr.setSingleEditArea(res.getString("Enter comments."), "COMMENT");
				//End: Added by Himani for redeemable buy back fipay Integration
			} else if (sAction.equals("LOOK_UP")) {
				clear();
				// Calling the AppButton
				appButtonEvent(new CMSActionEvent(this, 0, "REDEEM_CARD", 0));
			} else if (sAction.equals("PREV")) {
				start();
				anEvent.consume();
			} else if (sAction.equals("CANCEL")) {
				theAppMgr.showMenu(MenuConst.PREV_CANCEL, theOpr);
				anEvent.consume();
			}
		} else if (sHeader.equals("COMMENTS")) {
			if (sAction.equals("CANCEL")) {
				theAppMgr.showMenu(MenuConst.REDEEMABLE_BUY_BACK, "REDEEMABLE", theOpr);
				theAppMgr.setSingleEditArea(res.getString("Select option."));
				anEvent.consume();
			}
		}
	}

	/**
	 *
	 */
	private void displayRedeemable() {
		jtfId.setText(redeemable.getId());
		jtfType.setText(redeemable.getGUIPaymentName());
		jtfPurchaseDate.setText(df.format(redeemable.getCreateDate()));
		//Added by Himani for redeemable buyback fipay integration
		if(redeemable.getFirstName() != null && redeemable.getLastName()!=null)
			jtfCustomer.setText(redeemable.getFirstName() + " " + redeemable.getLastName());
		jtfPhone.setText(redeemable.getPhoneNumber());
		if (redeemable instanceof CMSStoreValueCard) {
			jtfPhone.setText(((CMSStoreValueCard) redeemable).getCustomerId());
		} else if (redeemable instanceof CMSDueBillIssue) {
			jtfPhone.setText(((CMSDueBillIssue) redeemable).getCustomerId());
		}
		jtfIssueAmt.setText(redeemable.getIssueAmount().formattedStringValue());
		try {
			//Added by Himani for redeemable buyback fipay integration
			if(gcBalance==null)
				jtfCurrentAmt.setText(redeemable.getRemainingBalance().formattedStringValue());
			else
				jtfCurrentAmt.setText("$"+gcBalance.toString());
		} catch (CurrencyException ce) {
			jtfCurrentAmt.setText("");
			System.err.println("RedeemableBuyBackApplet.displayRedeemable()->" + ce);
		}
		model.clear();
		Iterator historys = redeemable.getRedemptionHistory().iterator();
		while(historys.hasNext())
			model.addRow((RedeemableHist) historys.next());
		tblHist.repaint();
	}

	/**
	 *
	 */
	private void clear() {
		jtfId.setText("");
		jtfPurchaseDate.setText("");
		jtfCustomer.setText("");
		jtfPhone.setText("");
		jtfIssueAmt.setText("");
		jtfCurrentAmt.setText("");
		jtfType.setText("");
		model.clear();
		tblHist.repaint();
	}

	// MP: For handling obj event.
	public void objectEvent(String Command, Object obj) {
		if (Command.equals("REDEEMABLE_INQUIRY")) {
			if (obj != null) {
				redeemable = (Redeemable) obj;
				displayRedeemable();
				theAppMgr.showMenu(MenuConst.REDEEMABLE_BUY_BACK, "REDEEMABLE", theOpr);
				theAppMgr.setSingleEditArea(res.getString("Select option."));
			} else {
				if (theAppMgr.isOnLine())
				{
					theAppMgr.showErrorDlg(res.getString("Redeemable not found"));
					if(fipay_gc_flag.toUpperCase().equals("Y") && Version.CURRENT_REGION.equalsIgnoreCase("US")) //Added by Himani for redeemable buy back fipay Integration
						return;
				}
				else
					theAppMgr.showErrorDlg(res.getString("This request cannot be completed while in offline mode.  Please try later."));
				enterRedeemable();
			}
		}
	}
}
