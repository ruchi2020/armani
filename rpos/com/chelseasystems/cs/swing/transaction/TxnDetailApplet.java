/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 05-17-2005 | Manpreet  | N/A       |1. Modifications as to show Txn History       |
 |      |            |           |           |   using ARM_TXN_IDS_FOUND                    |
 -------------------------------------------------------------------------------------------|
 | 3    | 05-10-2005 | Anand     | N/A       |2. Modifications as per specifications for    |
 |      |            |           |           |   Txn History                                |
 -------------------------------------------------------------------------------------------|
 | 2    | 02-10-2005 | Anand     | N/A       |1.Modification to add new menu and related    |
 |      |            |           |           |  functionality                               |
 --------------------------------------------------------------------------------------------
 */
package com.chelseasystems.cs.swing.transaction;

import java.text.NumberFormat;
import java.util.HashMap;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.media.jai.RenderedImageAdapter;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.armani.business.rules.ARMCustomerBR;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.ScrollProcessor;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.PaymentAppModel;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.pos.ASISTxnData;
import com.chelseasystems.cs.pos.CMSCompositePOSTransactionAppModel;
import com.chelseasystems.cs.pos.CMSTransactionHeader;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSVoidLineItem;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.layaway.CMSLayawayPaymentTransactionAppModel;
import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cs.swing.panel.GiftReceiptPanel;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet_EUR;
import com.chelseasystems.cs.swing.model.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.loyalty.LoyaltyHistory;
import com.chelseasystems.cs.collection.CMSCollectionTransaction;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.DepositHistory;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;

import com.chelseasystems.cs.fiscaldocument.FiscalInterface;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.ga.fs.fsbridge.ARMFSBridge;
import com.ga.fs.fsbridge.object.ARMFSBOReceiptCopyObject;

/**
 */
public class TxnDetailApplet extends CMSApplet implements com.chelseasystems.cr.rules.IRuleEngine {
	private HashMap hmCachedTxns;
	private CMSTransactionHeader[] headers;
	private LoyaltyHistory[] loyaltyHistoryArray;
	private DepositHistory[] depositHistoryArray;
	private Boolean updatedCustomer;
	private int index;
	private PaymentTransaction theTxn;
	TxnHeaderPanel pnlHeader;
	TxnDetailPanel pnlDetails;
	RolodexLayout cardLayout = new RolodexLayout();
	JPanel pnlCardDetails;
	TxnLineItemDetailPanel pnlLineItemDetails;
	PaymentTransactionAppModel appModel;
	GiftReceiptPanel pnlGiftReceipt;
	PaymentTransactionAppModel aTxn;
	PaymentTableModel model = new PaymentTableModel();
	String txnIDs[];
	private FiscalInterface fiscalInterface = null;
	private boolean isSourceFromTxnDetailApplet = false;
	private SelectionResetListener selectionResetListener;
	private TransactionHeader currentTxnHeader;
	//added by shushma for promotion code
	private CMSCompositePOSTransaction compositePOSTransaction;
	
	//Vivek Mishra : Added to print signature
	//BufferedImage signImg;
	//End
	// Initialize the applet
	public void init() {
		hmCachedTxns = new HashMap();
		try {
			// Fiscal interface
			ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
			String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
			if (sClassName != null) {
				Class cls = Class.forName(sClassName);
				fiscalInterface = (FiscalInterface) cls.newInstance();
			}
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Component initialization
	private void jbInit() throws Exception {
		selectionResetListener = new SelectionResetListener();
		pnlHeader = new TxnHeaderPanel();
		pnlDetails = new TxnDetailPanel();
		pnlDetails.setName("DETAILS");
		pnlLineItemDetails = new TxnLineItemDetailPanel();
		pnlLineItemDetails.setName("LINE_ITEM_DETAILS");
		pnlDetails.setAppMgr(theAppMgr);
		pnlLineItemDetails.setAppMgr(theAppMgr);
		pnlHeader.setAppMgr(theAppMgr);
		pnlCardDetails = new JPanel();
		pnlCardDetails.setLayout(cardLayout);
		pnlCardDetails.add(pnlDetails, "DETAILS");
		pnlCardDetails.add(pnlLineItemDetails, "LINE_ITEM_DETAILS");
		pnlDetailLineItem = new DetailLineItemPanel();
		this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
		this.getContentPane().add(pnlCardDetails, BorderLayout.CENTER);
		pnlCardDetails.add(pnlLineItemDetails, "LINE_ITEM_DETAILS");
		pnlCardDetails.add(pnlDetailLineItem, "XXXX");
		pnlLineItem = new LineItemPOSPanel();
		pnlLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
		pnlDetailLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
	}

	private TransactionHeader getTransactionHeaders(String id) {
		if (headers == null) {
			return null;
		}
		TransactionHeader txnHeader = null;
		for (int i = 0; i < headers.length; i++) {
			txnHeader = headers[i];
			if (txnHeader.getId().equals(id)) {
				return txnHeader;
			}
		}
		return null;
	}

	// Start the applet
	public void start() {
		theAppMgr.removeStateObject("SHIPPING_REQUEST");
		cardLayout.show(pnlCardDetails, "DETAILS");
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		headers = (CMSTransactionHeader[]) theAppMgr.getStateObject("TXN_HEADER_LIST");
		loyaltyHistoryArray = (LoyaltyHistory[]) theAppMgr.getStateObject("LOYALTY_HIST_LIST");
		depositHistoryArray = (DepositHistory[]) theAppMgr.getStateObject("DEPOSIT_LIST");
		//Modified by Deepika to handle the DelayedCustomer Association Issue for Japan Region
		updatedCustomer = (Boolean)theAppMgr.getStateObject("UPDATE_TXN_CUSTOMER");
		Integer currentRow = (Integer) theAppMgr.getStateObject("TXN_HEADER_ROW");
		theTxn = (PaymentTransaction) theAppMgr.getStateObject("THE_TXN");
		try {
			if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
				CMSCustomer newCustomer = (CMSCustomer) theAppMgr.getStateObject("TXN_CUSTOMER");
				Customer lastCustomer = null;
				if (theTxn instanceof CMSCompositePOSTransaction) {
					lastCustomer = ((CMSCompositePOSTransaction) theTxn).getCustomer();
					//Modified by Deepika to handle the DelayedCustomer Association Issue for Japan Region
					if(("JP".equalsIgnoreCase(Version.CURRENT_REGION))){
						if(ARMCustomerBR.isDummy(lastCustomer) && updatedCustomer != null)
						{
					((CMSCompositePOSTransaction) theTxn).setCustomerPostTransaction(newCustomer);
						}
						else
							lastCustomer = ((CMSCompositePOSTransaction) theTxn).getCustomer();
						}
					else
					{
						((CMSCompositePOSTransaction) theTxn).setCustomerPostTransaction(newCustomer);
					}
				} else if (theTxn instanceof CMSMiscCollection)
					lastCustomer = ((CMSMiscCollection) theTxn).getCustomer();
				if (lastCustomer != null) {
					theAppMgr.addStateObject("UPDATE_TXN_CUSTOMER_LAST", lastCustomer);
				}
			}
		} catch (BusinessRuleException ex) {
			//ex.printStackTrace();
		}
		if (theAppMgr.getStateObject("ARM_TXN_IDS_FOUND") != null) {
			txnIDs = (String[]) theAppMgr.getStateObject("ARM_TXN_IDS_FOUND");
			theAppMgr.removeStateObject("ARM_TXN_IDS_FOUND");
		}
		if (currentRow != null)
			index = currentRow.intValue();
		if (theTxn != null) {
			//added by shushma for promotion code PCR
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			try {
				compositePOSTransaction=(CMSCompositePOSTransaction)theTxn;
				String[] promCode = CMSTransactionPOSHelper.findPromCodeByTxnId(theAppMgr, theTxn.getId());
				if(promCode!=null){
				compositePOSTransaction.setPromCode(promCode);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}//change closed
			isSourceFromTxnDetailApplet = true;
			appModel = theTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			// Fix for US issue 886: Receipt for a sale involving a loyalty customer needs
			// to show that points were earned
			if (appModel != null) {
				if (appModel instanceof CMSCompositePOSTransactionAppModel) {
					((CMSCompositePOSTransactionAppModel) appModel).setIsSourceFromTxnDetailApplet(isSourceFromTxnDetailApplet);
				}
			}
			pnlDetails.showTransaction(appModel);
			pnlHeader.showTransaction(appModel);
			pnlLineItemDetails.showTransaction(theTxn);
			hmCachedTxns.put(theTxn.getId(), theTxn);
			theAppMgr.addStateObject("TXN_HIST_SELECTEDTXNID", theTxn.getId());
			// PCR21 & 24 for Armani Europe (ASIS Txn Information)
			ASISTxnData asisTxnData = null;
			if (theTxn instanceof CMSCompositePOSTransaction) {
				asisTxnData = ((CMSCompositePOSTransaction) theTxn).getASISTxnData();
			} else if (theTxn instanceof CMSCollectionTransaction) {
				asisTxnData = ((CMSCollectionTransaction) theTxn).getASISTxnData();
			} else if (theTxn instanceof CMSPaidOutTransaction) {
				asisTxnData = ((CMSPaidOutTransaction) theTxn).getASISTxnData();
			}
			if (asisTxnData != null) {
				theAppMgr.addStateObject("ASIS_TXN_DATA", asisTxnData);
			}
			currentTxnHeader = getTransactionHeaders(theTxn.getId());
		} else if (currentRow != null) {
			if (headers != null) {
				displayTransaction(headers[index].getId());
				currentTxnHeader = headers[index];
			} else if (loyaltyHistoryArray != null)
				displayTransaction(loyaltyHistoryArray[index].getTransactionId());
		}
		theAppMgr.setSingleEditArea(res.getString("Select option."));
		if (theAppMgr.getStateObject("ARM_TXN_HIST_FISCAL_TXN") != null) {
			theAppMgr.showMenu(MenuConst.PRINT_FISCAL_DOCUMENT_VIEW, "PRINT_FISCAL", theOpr);
		} else if (theAppMgr.getStateObject("UPDATE_TXN_ASSOCIATE") != null || theAppMgr.getStateObject("UPDATE_TXN_CUSTOMER") != null || (theAppMgr.getStateObject("ADD_FROM_INQUIRY") != null)) {
			showUpdateTxnMenu();
		} else
			theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
		theAppMgr.removeStateObject("ARM_TXN_HIST_FISCAL_TXN");
		theAppMgr.addStateObject("TXN_DETAIL_CURR_PANEL", "DETAILS");
		theAppMgr.removeStateObject("UPDATE_TXN_CUSTOMER");
		theAppMgr.removeStateObject("UPDATE_TXN_ASSOCIATE");
	}

	private void showUpdateTxnMenu() {
		if (theTxn instanceof CMSCompositePOSTransaction) {
			try {
				if (((CMSCompositePOSTransaction) theTxn).isUpdateCustomerForTransactionAllowed()) {
					theAppMgr.showMenu(MenuConst.UPDATE_TXN_DATA, "UPDATE_TXN_DATA", theOpr);
				}
			} catch (BusinessRuleException exp) {
				theAppMgr.showMenu(MenuConst.UPDATE_TXN_DATA_NO_CUSTOMER, theOpr);
			}
		} else {
			theAppMgr.showMenu(MenuConst.UPDATE_TXN_DATA, "UPDATE_TXN_DATA", theOpr);
		}
	}

	public void stop() {
	}

	public String getScreenName() {
		return (res.getString("View Txn Detail"));
	}

	public String getVersion() {
		return ("$Revision: 1.24.2.6.4.13 $");
	}

	/**
	 * put your documentation comment here
	 */
	private void showPreviousMenu() {
		cardLayout.show(pnlCardDetails, "DETAILS");
		theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param Header
	 * @param anEvent
	 */
	public void appButtonEvent(String Header, CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("DDT") || sAction.equals("TAX_FREE") || sAction.equals("VAT_INVOICE") || sAction.equals("CREDIT_NOTE")) {
			// Sergio
			/*
			 * if (!hasLineItemsToPrintFiscalDocument()) { theAppMgr.showErrorDlg(res.getString( "Fiscal document for all the itmes have been printed
			 * for this transaction.")); anEvent.consume(); return; }
			 */
			theAppMgr.addStateObject("ARM_TXN_HIST_FISCAL_TXN", theTxn);
		}
		if (Header.equals("PRINT_FISCAL") && sAction.equals("PREV")) {
			theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
		}
		if (Header.equals("UPDATE_TXN_DATA")) {
			if (sAction.equals("OK")) {
				persistCustomerUpdate();
				persistShippingRequests();
				showMainCardLayoutScreen();
				theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
				updateTxnDetailPanel();
				this.repaint();
			} else if (sAction.equals("CANCEL")) {
				showMainCardLayoutScreen();
				theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
			} else if (sAction.equals("UPDATE_ITEM_DETAIL")) {
				populateLineItemDetails();
				theAppMgr.showMenu(MenuConst.UPDATE_CONSULTANT, theOpr);
			} else if (sAction.equals("SEND_SALE")) {
				if (theTxn != null && theTxn instanceof CMSCompositePOSTransaction) {
						if (((CMSCompositePOSTransaction) theTxn).getCustomer() == null) {
						theAppMgr.showErrorDlg(res.getString("Add a customer to this transaction before adding shipping request"));
						anEvent.consume();
						return;
					} else if (((CMSCompositePOSTransaction) theTxn).hasShippingRequests()) {
						GenericChooserRow[] shippingRequests = getShippingRequests((CMSCompositePOSTransaction) theTxn, true);
						theAppMgr.addStateObject("TXN", theTxn);
						GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, shippingRequests, new String[] { res.getString("First Name"),
								res.getString("Last Name"), res.getString("Street Address") });
						dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(dlg.getCenterRenderer());
						dlg.getTable().getColumnModel().getColumn(1).setCellRenderer(dlg.getCenterRenderer());
						dlg.getTable().getColumnModel().getColumn(2).setCellRenderer(dlg.getCenterRenderer());
						dlg.setVisible(true);
						if (dlg.isOK()) {
							if (dlg.getSelectedRow().getRowKeyData() != null)
								theAppMgr.addStateObject("SHIPPING_REQUEST", dlg.getSelectedRow().getRowKeyData());
							theAppMgr.addStateObject("ADD_FROM_INQUIRY", "ADD_FROM_INQUIRY");
						} else
							anEvent.consume();
					} else {
						theAppMgr.addStateObject("TXN", theTxn);
						theAppMgr.fireButtonEvent("SEND_SALE");
					}
				}
			} else {
				System.out.println("UNKNOWN ACTION FOR UPDATE_TXN_DATA");
			}
		}
	}

	/**
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equals("PRINT_FISCAL")) {
			theAppMgr.showMenu(MenuConst.PRINT_FISCAL_DOCUMENT_VIEW, "PRINT_FISCAL", theOpr);
			return;
		}
		if (sAction.equals("PRINT_DUPLICATE_FISCAL_RECEIPT")) {
			aTxn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("THE_TXN")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			setNF525DataForDuplicateReceipt(appModel); 
			int result = fiscalInterface.printFiscalReceipt(aTxn);
			if (result == 0) {
				return;
			} else {
				theAppMgr.showErrorDlg(res.getString("Error re-printing Fiscal receipt."));
			}
		}
		if (sAction.equals("PRINT_CUSTOMER_RECEIPT")) {
			aTxn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("THE_TXN")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			fiscalInterface.printCustomerReceipt(aTxn);
			return;
		}
		if (sAction.equals("PREV_DETAIL")) {
			if (headers != null)
				theAppMgr.addStateObject("ARM_TXN_HEADERS", headers);
			else if (loyaltyHistoryArray != null)
				theAppMgr.addStateObject("LOYALTY_HIST_LIST", loyaltyHistoryArray);
			// theAppMgr.goBack();
		}
		if (sAction.equals("RETURN_TO_PAYMENTS")) {
			showPreviousMenu();
		} else if (sAction.equals("PRINT_FISCAL")) {
			theAppMgr.showMenu(MenuConst.PRINT_FISCAL, theOpr, null);
		} else if (sAction.equals("PRINT_RECEIPT")) {
			// appModel.rePrintReceipt(theAppMgr);
			// Sumit: call to fiscalInterface
			if (fiscalInterface != null) {

					setNF525DataForDuplicateReceipt(appModel);
					
					// fiscalInterface.printFiscalReceipt(appModel);
					int result = fiscalInterface.printFiscalReceipt(appModel);
					if (result == 0) {} else {


					theAppMgr.showErrorDlg(res.getString("Error re-printing Fiscal receipt."));
				}
			} else {
				//Vivek Mishra : Added to print signature
			//Anjana 05/16: uncomented signature for dup receipt
				printSignature();
				//Ends
				appModel.rePrintReceipt(theAppMgr);
			}
		} else if (sAction.equals("PRINT_GIFT")) {
			aTxn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("THE_TXN")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			// Temporary fix, a rule has to be created and checked on the
			// isShowable method in CMSMenu/Option
			if (!(aTxn instanceof CMSCompositePOSTransactionAppModel)) {
				theAppMgr.showErrorDlg(res.getString("A gift receipt is not allowed for this transaction."));
				return;
			}
			if (aTxn instanceof CMSLayawayPaymentTransactionAppModel) {
				try {
					if (!((CMSLayawayPaymentTransactionAppModel) aTxn).getLayaway().getCurrentAmountDue().equals(new ArmCurrency(0.00))) {
						theAppMgr.showErrorDlg(res.getString("A gift receipt is not allowed on outstanding layaways."));
						return;
					}
				} catch (CurrencyException e) {
					theAppMgr.showExceptionDlg(e);
				}
			}
			POSLineItem[] lineItems = aTxn.getLineItemsArray();
			boolean isGiftItems = false;
			for (int i = 0; i < lineItems.length; i++) {
				if (lineItems[i] instanceof SaleLineItem) {
					isGiftItems = true;
					continue;
				}
			}
			if (!isGiftItems) {
				theAppMgr.showErrorDlg(res.getString("There are no items on this transaction that are eligible to print on a gift receipt."));
				return;
			}
			loadGiftPanel();
		} else if (sAction.equals("PRINT_ALL_GIFT")) {
			pnlGiftReceipt.getGiftReceiptModel().setAllItemSelection(new Boolean(true));
			printGiftReceipt();
		} else if (sAction.equals("PRINT_SELECT_ITEMS")) {
			printGiftReceipt();
		} else if (sAction.equals("ITEM_DETAILS")) {
			//
			if (pnlLineItemDetails.getSelectedLineItem() == null) {
				theAppMgr.showErrorDlg(res.getString(res.getString("A line item must be selected")));
				return;
			}
			if (!((pnlLineItemDetails.getSelectedLineItem()).isMiscItem())) {
				theAppMgr.addStateObject("InitialSale_POSLineItem", pnlLineItemDetails.getSelectedLineItem());
				theAppMgr.setSingleEditArea(res.getString("Select Option"));
			} else {
				theAppMgr.showErrorDlg(res.getString("Cannot get Item details for a miscellaneous item"));
			}
		} else if (sAction.equals("NEXT_TXN")) {
			if (headers != null && index < headers.length - 1) {
				CMSTransactionHeader header = headers[++index];
				currentTxnHeader = header;
				displayTransaction(header.getId());
			} else if (loyaltyHistoryArray != null && index < loyaltyHistoryArray.length - 1) {
				displayTransaction(loyaltyHistoryArray[++index].getTransactionId());
			} else if (depositHistoryArray != null && index < depositHistoryArray.length - 1) {
				displayTransaction(depositHistoryArray[++index].getTransactionId());
			} else if (txnIDs != null && index < txnIDs.length - 1) {
				displayTransaction(txnIDs[++index]);
			} else {
				theAppMgr.showErrorDlg(res.getString(res.getString("This is the last transaction in the list.")));
			}
			// anEvent.consume();
		} else if (sAction.equals("PREV_TXN")) {
			if (index > 0 && headers != null) {
				CMSTransactionHeader header = headers[--index];
				currentTxnHeader = header;
				displayTransaction(header.getId());
			} else if (index > 0 && loyaltyHistoryArray != null) {
				displayTransaction(loyaltyHistoryArray[--index].getTransactionId());
			} else if (index > 0 && depositHistoryArray != null) {
				displayTransaction(depositHistoryArray[--index].getTransactionId());
			} else if (txnIDs != null && index > 0 && index < txnIDs.length) {
				displayTransaction(txnIDs[--index]);
			} else {
				theAppMgr.showErrorDlg(res.getString(res.getString("This is the first transaction in the list.")));
			}
			// anEvent.consume();
		} else if (sAction.equals("TOGGLE_DETAIL")) {
			final int displayKey = ((PositionableDisplayer) cardLayout.getCurrent(pnlCardDetails)).getPositionKey();
			if (cardLayout.getCurrent(pnlCardDetails).getName().equals("DETAILS")) {
				cardLayout.next(pnlCardDetails);
				theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_LINE_ITEM_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
			} else if (cardLayout.getCurrent(pnlCardDetails).getName().equals("LINE_ITEM_DETAILS")) {
				cardLayout.previous(pnlCardDetails);
				theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
			}
			// cardVerifier = new CardPanelVerifier();
			SwingUtilities.invokeLater(new Runnable() {
				/**
				 *
				 */
				public void run() {
					// cardVerifier.executeRule(theAppMgr.getStateObject("TXN_DETAIL_CURR_PANEL"));
					((PositionableDisplayer) cardLayout.getCurrent(pnlCardDetails)).positionToKey(displayKey);
				}
			});
		} else if (sAction.equals("SEND_SALE_DETAILS")) {
			if (theTxn != null && theTxn instanceof CMSCompositePOSTransaction && ((CMSCompositePOSTransaction) theTxn).hasShippingRequests()) {
				GenericChooserRow[] shippingRequests = getShippingRequests((CMSCompositePOSTransaction) theTxn, false);
				theAppMgr.addStateObject("TXN", theTxn);
				GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, shippingRequests,
						new String[] { ("First Name"), ("Last Name"), ("Street Address") });
				dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(dlg.getCenterRenderer());
				dlg.getTable().getColumnModel().getColumn(1).setCellRenderer(dlg.getCenterRenderer());
				dlg.getTable().getColumnModel().getColumn(2).setCellRenderer(dlg.getCenterRenderer());
				dlg.setVisible(true);
				if (dlg.isOK()) {
					theAppMgr.addStateObject("SHIPPING_REQUEST", dlg.getSelectedRow().getRowKeyData());
					theAppMgr.addStateObject("SHIPPING_INQUIRY_ONLY", "");
				} else
					anEvent.consume();
			} else {
				theAppMgr.showErrorDlg(res.getString("There are no shipping requests associated with this transaction"));
				anEvent.consume();
			}
		} else if (sAction.equals("UPDATE_TXN_DATA")) {
			// theAppMgr.showMenu(MenuConst.UPDATE_TXN_DATA, "UPDATE_TXN_DATA", theOpr);
			showUpdateTxnMenu();
		} else if (sAction.equals("CHANGE_LINE_CONS")) {
			theAppMgr.addStateObject("UPDATE_TXN_CONSULTANT", new Boolean(true));
			if (pnlDetailLineItem.isRowSelected()) {
				POSLineItem lineItem = pnlDetailLineItem.getSelectedLineItem().getLineItem();
				// using isMiscReturn is no good in this scenario
				if (lineItem instanceof ReturnLineItem && ((ReturnLineItemDetail) lineItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
					theAppMgr.showErrorDlg(res.getString("The consultant on a return line item cannot be modified."));
					theAppMgr.setEditAreaFocus();
					return;
				}
				theAppMgr.setSingleEditArea(res.getString("Enter the user name of the new consultant."), "CONSULTANT_LINE_ID");
			} else {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				theAppMgr.setEditAreaFocus();
			}
		} else if (sAction.equals("RESTORE_CONS")) {
			theAppMgr.addStateObject("UPDATE_TXN_CONSULTANT", new Boolean(true));
			if (pnlDetailLineItem.isRowSelected()) {
				POSLineItem lineItem = pnlDetailLineItem.getSelectedLineItem().getLineItem();
				if (lineItem instanceof ReturnLineItem && ((ReturnLineItemDetail) lineItem.getLineItemDetailsArray()[0]).getSaleLineItemDetail() != null) {
					theAppMgr.showErrorDlg(res.getString("The consultant on a return line item cannot be modified."));
					theAppMgr.setEditAreaFocus();
					return;
				}
				try {
					pnlDetailLineItem.removeSelectedLineConsultant();
					theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
					theAppMgr.showMenu(MenuConst.UPDATE_CONSULTANT, theOpr);
					setLineConsultant(pnlDetailLineItem.getSelectedLineItem().getLineItem());
					updateConsultantUpdateList();
					return;
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
					return;
				}
			} else {
				theAppMgr.showErrorDlg(res.getString("A line item must be selected"));
				theAppMgr.setEditAreaFocus();
			}
		}
		// Sandhya Needs to Verify
		/*
		 * else if (sAction.equals("RETURN_TRANS")) { System.out.println("TxnDetailApplet:ACtion:RETURN_CONS"); cardLayout.show(pnlCardDetails,
		 * "DETAILS"); // theAppMgr.showMenu(MenuConst.UPDATE_TXN_DATA, theOpr); showUpdateTxnMenu(); // this.remove(pnlDetailLineItem); //
		 * this.getContentPane().add(pnlLineItem, BorderLayout.CENTER); this.repaint(); /* showButtons(); enterItem(); }
		 */
		// else if (Header.equals("UPDATE_TXN_DATA")){
		else if (sAction.equals("OK")) {
			updateDefaultConsultant();
			persistConsultantUpdate();	
			//MB:07/19/2011
			//Bug#6317 - Customer not persisting.
			persistCustomerUpdate();
			showMainCardLayoutScreen();
			theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
			updateTxnDetailPanel();
			this.repaint();
		} else if (sAction.equals("CANCEL")) {
			showMainCardLayoutScreen();
			theAppMgr.showMenu(MenuConst.NEXT_PREV_TXN_DETAILS, theOpr, theAppMgr.CANCEL_BUTTON);
			theAppMgr.removeStateObject("UPDATED_CONSULTANTS");
		} else if (sAction.equals("UPDATE_ITEM_DETAIL")) {
			populateLineItemDetails();
			theAppMgr.showMenu(MenuConst.UPDATE_CONSULTANT, theOpr);
		} else if (sAction.equals("ASIS_INFORMATION")) {
		} else {
			System.out.println("UNKNOWN ACTION: " + sAction);
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void showMainCardLayoutScreen() {
		cardLayout.show(pnlCardDetails, "DETAILS");
		this.repaint();
	}

	/**
	 * @param theTxn
	 * @return
	 */
	private GenericChooserRow[] getShippingRequests(CMSCompositePOSTransaction theTxn, boolean isNew) {
		ShippingRequest[] shippingRequests = theTxn.getShippingRequestsArray();
		int length = shippingRequests.length;
		if (isNew)
			length = length + 1;
		GenericChooserRow[] chooseShippingRequest = new GenericChooserRow[length];
		int i = 0;
		for (; i < shippingRequests.length; i++) {
			chooseShippingRequest[i] = new GenericChooserRow(new String[] { shippingRequests[i].getFirstName(), shippingRequests[i].getLastName(), shippingRequests[i].getAddress() },
					shippingRequests[i]);
		}
		if (isNew) {
			chooseShippingRequest[i] = new GenericChooserRow(new String[] { "", res.getString("NEW SHIPPING REQUEST"), "" }, null);
		}
		return (chooseShippingRequest);
	}

	/**
	 * callback when <b>Page Down</b> icon is clicked
	 */
	public void pageDown(MouseEvent e) {
		ScrollProcessor scrollProcessor = (ScrollProcessor) cardLayout.getCurrent(pnlCardDetails);
		scrollProcessor.nextPage();
		theAppMgr.showPageNumber(e, scrollProcessor.getCurrentPageNumber() + 1, scrollProcessor.getPageCount());
	}

	/**
	 * callback when <b>Page Up</b> icon is clicked
	 */
	public void pageUp(MouseEvent e) {
		ScrollProcessor scrollProcessor = (ScrollProcessor) cardLayout.getCurrent(pnlCardDetails);
		scrollProcessor.prevPage();
		theAppMgr.showPageNumber(e, scrollProcessor.getCurrentPageNumber() + 1, scrollProcessor.getPageCount());
	}

	/**
	 */
	private void displayTransaction(String id) {
		try {
			theTxn = (PaymentTransaction) hmCachedTxns.get(id);
			if (theTxn == null) {
				theTxn = CMSTransactionPOSHelper.findById(theAppMgr, id);
				hmCachedTxns.put(id, theTxn);
			}
			if (theTxn != null) {
				//added by shushma for promotion
				if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
				compositePOSTransaction=(CMSCompositePOSTransaction)theTxn;
				String[] promCode=CMSTransactionPOSHelper.findPromCodeByTxnId(theAppMgr, id);
				compositePOSTransaction.setPromCode(promCode);
				}
				appModel = theTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
				pnlDetails.showTransaction(appModel);
				pnlHeader.showTransaction(appModel);
				pnlLineItemDetails.showTransaction(theTxn);
				theAppMgr.addStateObject("THE_TXN", theTxn);
				theAppMgr.addStateObject("TXN_HEADER_ROW", new Integer(index));
				theAppMgr.addStateObject("TXN_HIST_SELECTEDTXNID", theTxn.getId());
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void loadGiftPanel() {
		theAppMgr.showMenu(MenuConst.GIFT_RECEIPTS, theOpr);
		pnlGiftReceipt = new GiftReceiptPanel(theAppMgr, aTxn);
		pnlCardDetails.add(pnlGiftReceipt, "GIFT_RECEIPT");
		cardLayout.show(pnlCardDetails, "GIFT_RECEIPT");
	}

	/**
	 */
	private void printGiftReceipt() {
		try {
			if (aTxn instanceof CMSPaymentTransactionAppModel) {
				if (!pnlGiftReceipt.getGiftReceiptModel().containsSelectedItems()) {
					theAppMgr.showErrorDlg("No valid items are selected to print a gift receipt.");
					return;
				}
				GiftReceiptEntry[] entries = pnlGiftReceipt.getGiftReceiptModel().getSelectedEntries();
				((CMSPaymentTransactionAppModel) aTxn).setSelectedGiftItems(entries);
				if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
					String txnClassName = aTxn.getPaymentTransaction().getClass().getName();
					String shortClassName = txnClassName.substring(txnClassName.lastIndexOf(".") + 1, txnClassName.length());
					String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + shortClassName + ".rdo";
					ObjectStore objectStore = new ObjectStore(fileName);
					objectStore.write(aTxn);
				}
				GiftReceiptEntry[] getEntries = ((CMSPaymentTransactionAppModel) aTxn).getSelectedGiftItems();
				aTxn.printGiftReceipt(theAppMgr);
				// Sumit: call to fiscalInterface
				if (fiscalInterface != null)
					fiscalInterface.printGiftReceipt(aTxn);
				pnlGiftReceipt.getGiftReceiptModel().setAllItemSelection(new Boolean(false));
				pnlGiftReceipt.repaint();
			} else {
				theAppMgr.showErrorDlg(res.getString("A gift receipt is not allowed for this transaction."));
			}
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * MP: Home pressed at customer display exits transaction with no message
	 * 
	 * @return
	 */
	public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}
		return (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?")));
	}

	private boolean hasLineItemsToPrintFiscalDocument() {
		return false;
	}

	DetailLineItemPanel pnlDetailLineItem;
	private int iAppletMode = InitialSaleApplet_EUR.SALE_MODE;
	LineItemPOSPanel pnlLineItem;
	JPanel cardPanel;

	/**
	 * Load item detail and model
	 */
	private void populateLineItemDetails() {
		if (pnlDetailLineItem != null) {
			pnlDetailLineItem.clear();
		}
		POSLineItem[] lineItems;
		if (iAppletMode == InitialSaleApplet_EUR.PRE_SALE_OPEN) {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getPresaleLineItemsArray();
		} else if (iAppletMode == InitialSaleApplet_EUR.RESERVATIONS_OPEN) {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getSaleAndReservationLineItemArray();
		} else if (iAppletMode == InitialSaleApplet_EUR.CONSIGNMENT_OPEN) {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getConsignmentLineItemsArray();
		} else {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getLineItemsArray();
		}
		pnlDetailLineItem.getTable().getSelectionModel().removeListSelectionListener(selectionResetListener);
		for (int i = 0; i < lineItems.length; i++) {
			if (lineItems[i] instanceof CMSVoidLineItem)
				continue;
			pnlDetailLineItem.addLineItem(lineItems[i]);
		}
		if (lineItems.length > 0) {
			pnlDetailLineItem.getModel().firstPage();
			pnlDetailLineItem.selectFirstRow();
		}
		pnlDetailLineItem.getTable().getSelectionModel().addListSelectionListener(selectionResetListener);
		cardLayout.show(pnlCardDetails, "XXXX");
		this.repaint();
		theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
		theAppMgr.showMenu(MenuConst.UPDATE_CONSULTANT, theOpr);
	}

	// -- Manpreet S Bawa -- 02/02/2005 .. copied from InitialSaleApplet_EUR
	// -- Sets consultant for for current line item.
	// Retrieve current consultant from Transaction.
	// If it is null, set consultant to current operator.
	private void setLineConsultant(POSLineItem line) {
		CMSEmployee currentConsultant = (CMSEmployee) ((CMSCompositePOSTransaction) theTxn).getConsultant();
		if (currentConsultant == null) {
			currentConsultant = (CMSEmployee) theOpr; // Default operator
		}
		try {
			if (line.getAdditionalConsultant() == null) {
				line.setAdditionalConsultant(currentConsultant);
			}
		} catch (BusinessRuleException bre) {
			bre.printStackTrace();
		}
	}

	/** ******************************************************************* */
	private class SelectionResetListener implements ListSelectionListener {
		/**
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (cardLayout.getCurrent(pnlCardDetails) == pnlDetailLineItem) {
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.UPDATE_CONSULTANT, theOpr);
			}
		}
	}

	// callback whan an edit area event occurs
	public void editAreaEvent(String Command, String sEdit) {
		try {
			if (Command.equals("CONSULTANT_LINE_ID")) {
				CMSEmployee emp = CMSEmployeeHelper.findByShortName(theAppMgr, sEdit);
				if (emp == null) {
					theAppMgr.showErrorDlg(res.getString("Cannot find employee."));
					theAppMgr.setSingleEditArea(res.getString("Enter the associate ID"), "CONSULTANT_LINE_ID");
					return;
				}
				if (false) { // !emp.isConsultant()) {
					theAppMgr.showErrorDlg(res.getString("The employee is not a consultant"));
					theAppMgr.setSingleEditArea(res.getString("Enter the associate ID"), "CONSULTANT_LINE_ID");
					return;
				}
				pnlDetailLineItem.modifySelectedLineConsultant(emp);
				theAppMgr.setSingleEditArea(res.getString("Select line item and option to modify."));
				theAppMgr.showMenu(MenuConst.UPDATE_CONSULTANT, theOpr);
				theAppMgr.addStateObject("UPDATE_TXN_CONSULTANT", new Boolean(true));
				updateConsultantUpdateList();
				return;
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void persistCustomerUpdate() {
		// persist them here...
		Customer oldCustomer = (Customer) theAppMgr.getStateObject("UPDATE_TXN_CUSTOMER_LAST");
		try {
			theTxn = CMSTransactionPOSHelper.updateCustomer(theAppMgr, (CMSCompositePOSTransaction) theTxn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void persistShippingRequests() {
		try {
			if (theAppMgr.getStateObject("ADD_FROM_INQUIRY") != null) {
				theTxn = CMSTransactionPOSHelper.addShippingRequestToTransaction(theAppMgr, (CMSCompositePOSTransaction) theTxn);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void persistConsultantUpdate() {
		POSLineItem[] lineItems = ((CMSCompositePOSTransaction) theTxn).getLineItemsArray();
		ArrayList lineItemsUpdated = new ArrayList();
		Hashtable changedConsultants = (Hashtable) theAppMgr.getStateObject("UPDATED_CONSULTANTS");
		if (changedConsultants == null) {
			return;
		}
		for (int i = 0; i < lineItems.length; i++) {
			POSLineItem thisLineItem = lineItems[i];
			String id = thisLineItem.getItem().getId();
			int idNumber = thisLineItem.getSequenceNumber();
			if (changedConsultants.get(new Integer(idNumber)) != null) {
				lineItemsUpdated.add(thisLineItem);
			}
		}
		lineItems = (POSLineItem[]) lineItemsUpdated.toArray(new POSLineItem[0]);
		try {
			theTxn = CMSTransactionPOSHelper.updateConsultant(theAppMgr, (CMSCompositePOSTransaction) theTxn, lineItems);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changedConsultants.clear();
		theAppMgr.removeStateObject("UPDATED_CONSULTANTS");
	}

	/**
	 * put your documentation comment here
	 */
	private void updateTxnDetailPanel() {
		if (theTxn != null) {
			//added by shushma for promotion code PCR
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			try {
				compositePOSTransaction=(CMSCompositePOSTransaction)theTxn;
				String promCode[]= CMSTransactionPOSHelper.findPromCodeByTxnId(theAppMgr, theTxn.getId());
				if(promCode!=null)
				compositePOSTransaction.setPromCode(promCode);
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}//change closed
			appModel = theTxn.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
			pnlDetails.showTransaction(appModel);
			pnlLineItemDetails.showTransaction(theTxn);
			this.repaint();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param lineItem
	 * @return
	 */
	public static String getDiscountPercentString(POSLineItem lineItem) {
		String sMkdNPercent = new String();
		double dSellingPrice, dReductionAmount, iLineItemQuantity, dNetDiscount;
		if (lineItem instanceof CMSReturnLineItem) {
			if (((CMSReturnLineItemDetail) lineItem.getLineItemDetailsArray()[0]).getReservationLineItemDetail() != null
					|| ((CMSReturnLineItemDetail) lineItem.getLineItemDetailsArray()[0]).getPresaleLineItemDetail() != null
					|| ((CMSReturnLineItemDetail) lineItem.getLineItemDetailsArray()[0]).getConsignmentLineItemDetail() != null)
				return new ArmCurrency(0.0d).formattedStringValue() + " (0.00%) ";
		}
		dSellingPrice = lineItem.getItemSellingPrice().doubleValue();
		dReductionAmount = lineItem.getExtendedReductionAmount().doubleValue();
		iLineItemQuantity = lineItem.getQuantity().intValue();
		// #1217
		if (lineItem instanceof CMSPresaleLineItem)
			dNetDiscount = (dReductionAmount) / iLineItemQuantity;
		else
			dNetDiscount = (dReductionAmount / dSellingPrice) / iLineItemQuantity;
		// If dNetDiscount is not number or infinite
		if (Double.isNaN(dNetDiscount) || Double.isInfinite(dNetDiscount))
			dNetDiscount = 0.00;
		// Trim discount to 2 decimal points.
		// Integer digits cant be more than 2.
		sMkdNPercent = String.valueOf(dNetDiscount);
		// Check if length of integer digits is 2
		// Total length would be more than 3
		try {
		ArmCurrency totalMarkDown = null;
			// #1217
			if (lineItem instanceof CMSPresaleLineItem)
				totalMarkDown = lineItem.getExtendedReductionAmount();
			else
				totalMarkDown = lineItem.getExtendedRetailAmount().subtract(lineItem.getExtendedNetAmount());
			double d = 0.00;
			if (lineItem.getItemRetailPrice().doubleValue() != 0.00)
				d = totalMarkDown.doubleValue() / lineItem.getItemRetailPrice().doubleValue() / iLineItemQuantity;
			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMaximumFractionDigits(1);
			
			//Added by shushma for promotion
			if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
			String promCode=lineItem.getPromotionCode();
			if(promCode!=null)
			sMkdNPercent = totalMarkDown.formattedStringValue() + " (" + nf.format(d) + ")/("+promCode+")";
			else
				sMkdNPercent = totalMarkDown.formattedStringValue() + " (" + nf.format(d) + ")";
			}
			else
				sMkdNPercent = totalMarkDown.formattedStringValue() + " (" + nf.format(d) + ")";
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return sMkdNPercent;
	}

	private void updateConsultantUpdateList() {
		Hashtable changedConsultants = (Hashtable) theAppMgr.getStateObject("UPDATED_CONSULTANTS");
		if (changedConsultants == null) {
			changedConsultants = new Hashtable();
		}
		changedConsultants.put(new Integer(pnlDetailLineItem.getSelectedLineItem().getLineItem().getSequenceNumber()), new Boolean(true));
		theAppMgr.addStateObject("UPDATED_CONSULTANTS", changedConsultants);
	}

	private POSLineItem[] getLineItems() {
		POSLineItem[] lineItems = null;
		if (iAppletMode == InitialSaleApplet_EUR.PRE_SALE_OPEN) {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getPresaleLineItemsArray();
		} else if (iAppletMode == InitialSaleApplet_EUR.RESERVATIONS_OPEN) {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getSaleAndReservationLineItemArray();
		} else if (iAppletMode == InitialSaleApplet_EUR.CONSIGNMENT_OPEN) {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getConsignmentLineItemsArray();
		} else {
			lineItems = ((CMSCompositePOSTransaction) theTxn).getLineItemsArray();
		}
		return lineItems;
	}

	private CMSEmployee getCommonConsultantIfAny() {
		POSLineItem[] lineItems = getLineItems();
		if (lineItems == null) {
			return null;
		}
		CMSEmployee thisEmp = null;
		for (int i = 0; i < lineItems.length; i++) {
			POSLineItem item = lineItems[i];
			CMSEmployee tmpEmp = (CMSEmployee) item.getAdditionalConsultant();
			if (tmpEmp == null) {
				continue;
			}
			if (thisEmp == null) {
				thisEmp = tmpEmp;
			} else if (!thisEmp.getExternalID().equalsIgnoreCase(tmpEmp.getExternalID())) {
				return null;
			}
		}
		return thisEmp;
	}

	private void updateDefaultConsultant() {
		Object obj = theAppMgr.getStateObject("UPDATED_CONSULTANTS");
		if (obj == null) {
			return;
		}
		CMSEmployee mainEmp = (CMSEmployee) ((CMSCompositePOSTransaction) theTxn).getConsultant();
		if (mainEmp == null) {
			return;
		}
		CMSEmployee commonEmployee = getCommonConsultantIfAny();
		if (commonEmployee != null) {
			((CMSCompositePOSTransaction) (theTxn)).doSetConsultant(commonEmployee);
			pnlHeader.showTransaction(appModel);
			if (currentTxnHeader != null) {
				currentTxnHeader.doSetConsultantId(commonEmployee.getExternalID());
			} else {
				System.out.println("null header ");
			}
		}
	}
	
	//Vivek Mishra : Added to print signature
	private void printSignature()
	{
		if (appModel instanceof CMSCompositePOSTransactionAppModel)
		{
			
			Payment[] payments = appModel.getPaymentsArray();
			  for(Payment payment : payments)
			  {
				  if(payment instanceof CreditCard)
				  {
					  CreditCard cc = (CreditCard)payment;
					  if(cc.getSignByteCode()!=null)
					  {
						  /*String signLocation = "C://clientwindows1.5//US//retek//rpos//Signature//MyFile.bmp";  
				 			File f = new File("C://clientwindows1.5//US//retek//rpos//Signature//MyFile.bmp");*/
						  
						  byte[] buff = cc.getSignByteCode();
						  ByteArraySeekableStream stream;
						  BufferedImage imag = null;
						  try{
				 		  stream = new ByteArraySeekableStream(buff);
				 		  TIFFDecodeParam decodeParam = new TIFFDecodeParam();
				 		  decodeParam.setDecodePaletteAsShorts(true);
				 		  ImageDecoder decoder = ImageCodec.createImageDecoder("TIFF", stream, decodeParam);
				 		  RenderedImageAdapter ria = new RenderedImageAdapter(decoder.decodeAsRenderedImage(0));
				 		  imag = ria.getAsBufferedImage();
						  }
						  catch(Exception e)
						  {
							  e.printStackTrace();
							//Vivek Mishra : Added to handle exception while rinting duplicate receipt in case of improper signature image data 20-APR-2016
							  ((CMSCompositePOSTransactionAppModel)appModel).setSignLocation(null);
                              return;//Ends
						  }

				 			String signLocation = "signature.bmp";  
				 			File f = new File(signLocation);

				 			try {
								ImageIO.write(imag, "bmp", f);
								//signImg = null;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								//Vivek Mishra : Added to handle exception while printing duplicate receipt in case of improper signature image data 20-APR-2016
								e.printStackTrace();
								((CMSCompositePOSTransactionAppModel)appModel).setSignLocation(null);
	                              return;//Ends
							}
							((CMSCompositePOSTransactionAppModel)appModel).setSignLocation(signLocation);
							break;
					  }
				  }
			  }
		
		}
	}
	
	private void deleteSignFile()
	  {
				File file = new File("signature.bmp");
				if(file.exists())
				file.delete();

	  }
	
	private static void setNF525DataForDuplicateReceipt(PaymentTransactionAppModel appModel){
		
		/*Added by Yves Agbessi (05-Dec-2017)
		 * Handles the Receipt Copy Transaction for Fiscal Solutions Service
		 * START --
		 * */
		String COPY_NUMBER = "";
		ARMFSBridge fsBridge = new ARMFSBridge().build();
		

		if(fsBridge.isCountryAllowed){
			
			PaymentTransaction p = (PaymentTransaction) theAppMgr.getStateObject("THE_TXN");
			ARMFSBOReceiptCopyObject receiptCopyObject = 
				new ARMFSBOReceiptCopyObject(p);
			if (fsBridge.postObject(receiptCopyObject)) {

				String response = receiptCopyObject
						.processResponse();
				 
				COPY_NUMBER = receiptCopyObject.COPY_NUMBER;
				try {
					appModel.getPaymentTransaction().setHandWrittenTicketNumber(COPY_NUMBER+"-"+receiptCopyObject.COPY_DIGITAL_KEY);
				} catch (BusinessRuleException e) {
					AppManager.getCurrent().showErrorDlg(e.getMessage());
					e.printStackTrace();
				}
				
				if (response.contains("ERROR")
						|| response.contains("UNABLE")) {
					theAppMgr
							.showErrorDlg("[FS BRIDGE] "
									+ response
									+ "\n Please call the Help Desk Immediately. Press OK to continue");

				}

			} else {

				theAppMgr
						.showErrorDlg("[FS BRIDGE] : Unable to post RECEIPT_COPY_TRANSACTION. Press OK to continue\n"
								+ "Please call the Help Desk Immediately");

			}

		}
		
		/*
		 * -- END
		 * 
		 * */
		
	}
	
	//Ends
}
