/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

/*
 * Description:
 * Created By:
 * Created Date:
 */

/*
 * History (tab separated):
 * Vers	Date		By			Spec	Comments
 * 1	10/01/07	Ken					Modified view registration table for Japan
 */

package com.chelseasystems.cs.swing.transaction;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.panel.*;

import java.awt.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.cs.txnnumber.*;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;

/**
 * put your documentation comment here
 */
public class ViewOpenTxnApplet extends CMSApplet {
	private ViewOpenTxnPanel pnlViewTxn;
	private int iAppletMode = 0;
	private CMSCompositePOSTransaction theTxn;
	private CMSCompositePOSTransaction posTransFound;
	private CMSTransactionHeader tranHeaderSelected;
	private static final String CONFIGURATION_FILE = "pos.cfg";
	private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);

	/**
	 * put your documentation comment here
	 */
	public void stop() {
	}

	/**
	 * put your documentation comment here
	 */
	public void init() {
		try {
			System.out.println("Calling jbinit()");
			jbinit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void start() {
		tranHeaderSelected = null;
		posTransFound = null;
		theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		if (theAppMgr.getStateObject("TXN_MODE") != null)
			iAppletMode = ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue();
		else
			iAppletMode = InitialSaleApplet.PRE_SALE_CLOSE;
		if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
			System.out.println("showing VIEW_RESERVATIONS screen.");
			theAppMgr.showMenu(MenuConst.VIEW_RESERVATIONS, theOpr);
		} else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
			theAppMgr.showMenu(MenuConst.VIEW_CONSIGNMENTS, theOpr);
		} else {
			theAppMgr.showMenu(MenuConst.VIEW_PRE_SALES, theOpr);
		}
		/*
		 * We must call jbinit() from start() because we need to know the mode
		 * in order to determine whether to use the Japanse panel (see comments
		 * in jbinit()).
		 */
		try {
			jbinit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pnlViewTxn.clear();
		enterExpireDate();
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
						populateTxns();
					}
				});
			}
		});
	}

	/**
	 * put your documentation comment here
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sCommand = anEvent.getActionCommand();
		if (sCommand.equals("CANCEL_PRE_SALE")) {
			tranHeaderSelected = pnlViewTxn.getSelectedTranHeader();
			if (tranHeaderSelected == null) {
				theAppMgr.showErrorDlg(res.getString("Select transaction to proceed"));
				return;
			}
			if (theAppMgr.showOptionDlg(res.getString("Cancel Pre Sale"), 
					res.getString("Are you sure you want to cancel this transaction?")))
				removePresaleTxn();
		} else if (sCommand.equals("OK")) {
			tranHeaderSelected = pnlViewTxn.getSelectedTranHeader();
			if (tranHeaderSelected == null) {
				theAppMgr.showErrorDlg(res.getString("Select transaction to proceed"));
				return;
			}
			if (applyPresaleTxn()) {
				theAppMgr.addStateObject("ARM_TXN_SELECTED", posTransFound);
				theAppMgr.goBack();
			}
		} else if (sCommand.equals("PREV")) {
		} else if (sCommand.equals("CANCEL")) {
		} else if (sCommand.equals("CANCEL_RESERVATION")) {
			tranHeaderSelected = pnlViewTxn.getSelectedTranHeader();
			if (tranHeaderSelected == null) {
				theAppMgr.showErrorDlg(res.getString("Select transaction to proceed"));
				return;
			}
//			if (theAppMgr.showOptionDlg(res.getString("Cancel Reservation"), 
//					res.getString("Are you sure you want to cancel this transaction?")))
			removeReservationTxn();
		} else if (sCommand.equals("CANCEL_CONSIGNMENT")) {
			tranHeaderSelected = pnlViewTxn.getSelectedTranHeader();
			if (tranHeaderSelected == null) {
				theAppMgr.showErrorDlg(res.getString("Select transaction to proceed"));
				return;
			}
			if (theAppMgr.showOptionDlg(res.getString("Cancel Consignment"), 
					res.getString("Are you sure you want to cancel this transaction?")))
				removeConsignmentTxn();
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void removePresaleTxn() {
		String txnNum = null;
		try {
			posTransFound = 
				(CMSCompositePOSTransaction) CMSTransactionPOSHelper.findById(theAppMgr, tranHeaderSelected.getId());
			if (posTransFound == null)
				return;
			PresaleTransaction prsTxn = posTransFound.getPresaleTransaction();
			if (prsTxn == null)
				return;
			//if(prsTxn.getLineItemsArray())
			POSLineItem presaleLineItems[] = prsTxn.getLineItemsArray();
			CMSPresaleLineItemDetail[] presaleLineItemDetails = null;
			CMSPresaleLineItemDetail presaleLineItemDetail = null;
			if (presaleLineItems != null && presaleLineItems.length > 0) {
				for (int i = 0; i < presaleLineItems.length; i++) {
					if (presaleLineItems[i] instanceof CMSPresaleLineItem){
						presaleLineItemDetail = (CMSPresaleLineItemDetail)(presaleLineItems[i]).getLineItemDetailsArray()[0];
						if (presaleLineItemDetail.getProcessed()) {
							theAppMgr.showErrorDlg(res.getString(
								"Some items in this txn have been processed. Cannot cancel txn."));
							return;
						}
					}
				}
			}
			
			CMSCompositePOSTransaction newTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
			if (newTxn == null)
				return;
			if (newTxn.getId() == null || newTxn.getId().equals("")) {
				txnNum = CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr, 
						(CMSStore) theStore, (CMSRegister) theAppMgr.getGlobalObject("REGISTER"));
				newTxn.setId(txnNum);
				newTxn.setSubmitDate(new java.util.Date());
			}
			newTxn.addEntirePresaleTransactionAsReturn(prsTxn);
			newTxn.setConsultant(posTransFound.getConsultant());
//			issue# 1917 Customer_id was not set in ARM_STG_TXN_HDR
			newTxn.setCustomer(posTransFound.getCustomer());
			CMSTxnPosterHelper.post(theAppMgr, newTxn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			pnlViewTxn.deleteSelectedTranHeader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeReservationTxn() {
		String txnNum = null;
		try {
			posTransFound = 
				(CMSCompositePOSTransaction) CMSTransactionPOSHelper.findById(theAppMgr, tranHeaderSelected.getId());
			if (posTransFound == null)
				return;
			ReservationTransaction rsvTxn = posTransFound.getReservationTransaction();
			if (rsvTxn == null)
				return;
			//Ruchi: Fix for 1837 Broken transaction 
			//In case of  "cancel reservation", broken txn is occurred.
			POSLineItem resLineItems[] = rsvTxn.getLineItemsArray();
			CMSReservationLineItemDetail[] resLineItemDetails = null;
			CMSReservationLineItemDetail resLineItemDetail = null;
			if (resLineItems != null && resLineItems.length > 0) {
				for (int i = 0; i < resLineItems.length; i++) {
					if (resLineItems[i] instanceof CMSReservationLineItem){
						resLineItemDetail = (CMSReservationLineItemDetail) resLineItems[i].getLineItemDetailsArray()[0];
						if (resLineItemDetail.getProcessed()) {
							theAppMgr.showErrorDlg(res.getString(
									"Some items in this txn have been processed. Cannot cancel txn."));
							return;
						}
					}
			
				}
			}
			if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){		
				if(rsvTxn.getDepositAmount().doubleValue() > 0.0d){
					theAppMgr.showErrorDlg(res.getString("Caution; This txn has a deposit.You CANNOT delete this txn."));
					return;
				}
			}
			if (!theAppMgr.showOptionDlg(res.getString("Cancel Reservation"), 
					res.getString("Are you sure you want to cancel this transaction?"))){
				return;
			}
			CMSCompositePOSTransaction newTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
			if (newTxn == null)
				return;
			if (newTxn.getId() == null || newTxn.getId().equals("")) {
				txnNum = CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr, (CMSStore) theStore, (CMSRegister) theAppMgr.getGlobalObject("REGISTER"));
				newTxn.setId(txnNum);
				newTxn.setSubmitDate(new java.util.Date());
			}
			newTxn.addEntireReservationTransactionAsReturn(rsvTxn);
			newTxn.setConsultant(posTransFound.getConsultant());
			//1917 Cancel Resrvation Customer_id was not setting in ARM_STG_TXN_HDR Issue from Japan
			newTxn.setCustomer(posTransFound.getCustomer());
			CMSTxnPosterHelper.post(theAppMgr, newTxn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			pnlViewTxn.deleteSelectedTranHeader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeConsignmentTxn() {
		String txnNum = null;
		try {
			posTransFound = (CMSCompositePOSTransaction) CMSTransactionPOSHelper.findById(theAppMgr, tranHeaderSelected.getId());
			if (posTransFound == null)
				return;
			ConsignmentTransaction csgTxn = posTransFound.getConsignmentTransaction();
			if (csgTxn == null)
				return;
			POSLineItem csgLineItems[] = csgTxn.getLineItemsArray();
			CMSConsignmentLineItemDetail[] csgLineItemDetails = null;
			CMSConsignmentLineItemDetail csgLineItemDetail = null;
			if (csgLineItems != null && csgLineItems.length > 0) {
				for (int i = 0; i < csgLineItems.length; i++) {
					if (csgLineItems[i] instanceof CMSConsignmentLineItem){
						csgLineItemDetail = (CMSConsignmentLineItemDetail) csgLineItems[i].getLineItemDetailsArray()[0];
						if (csgLineItemDetail.getProcessed()) {
							theAppMgr.showErrorDlg(res.getString(
									"Some items in this txn have been processed. Cannot cancel txn."));
							return;
						}
					}
					/*for (int j = 0; j < csgLineItemDetails.length; j++) {
						if (csgLineItemDetails[j].getProcessed()) {
							theAppMgr.showErrorDlg(res.getString(
									"Some items in this txn have been processed. Cannot cancel txn."));
							return;
						}
					}*/
				}
			}
			CMSCompositePOSTransaction newTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
			if (newTxn == null)
				return;
			if (newTxn.getId() == null || newTxn.getId().equals("")) {
				txnNum = CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr, 
						(CMSStore) theStore, (CMSRegister) theAppMgr.getGlobalObject("REGISTER"));
				newTxn.setId(txnNum);
				newTxn.setSubmitDate(new java.util.Date());
			}
			newTxn.addEntireConsignmentTransactionAsReturn(csgTxn);
			newTxn.setConsultant(posTransFound.getConsultant());
			//issue# 1917 Customer_id was not set in ARM_STG_TXN_HDR
			newTxn.setCustomer(posTransFound.getCustomer());
			CMSTxnPosterHelper.post(theAppMgr, newTxn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			pnlViewTxn.deleteSelectedTranHeader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	private boolean applyPresaleTxn() {
		try {
			if (tranHeaderSelected == null)
				return false;
			posTransFound = 
				(CMSCompositePOSTransaction) CMSTransactionPOSHelper.findById(theAppMgr, tranHeaderSelected.getId());
			if (posTransFound != null)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * put your documentation comment here
	 */
	private void populateTxns() {
		try {
			CMSTransactionHeader tranHeader[] = null;
			if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
				tranHeader = CMSTransactionPOSHelper.findOpenReservationByStore(theAppMgr, theStore.getId());
			} else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
				tranHeader = CMSTransactionPOSHelper.findOpenConsignmentByStore(theAppMgr, theStore.getId());
			} else {
				tranHeader = CMSTransactionPOSHelper.findOpenPresaleByStore(theAppMgr, theStore.getId());
			}
			if (tranHeader == null || tranHeader.length < 1)
				return;
			for (int iCtr = 0; iCtr < tranHeader.length; iCtr++) {
				if (tranHeader[iCtr] == null)
					continue;
				pnlViewTxn.addTransactionHeader(tranHeader[iCtr]);
			}
			pnlViewTxn.doLayout();
			pnlViewTxn.selectFirstRowFirstPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getVersion() {
		return ("$Revision: 1.2 $");
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getScreenName() {
		if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
			return res.getString("View Reservation");
		} else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
			return res.getString("View Consignment");
		} else {
			return res.getString("View Presale");
		}
	}

	/**
	 * put your documentation comment here
	 */
	private void enterExpireDate() {
		if (iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE) {
			theAppMgr.setSingleEditArea(res.getString("Select a Reservation"));
		} else if (iAppletMode == InitialSaleApplet.CONSIGNMENT_CLOSE) {
			theAppMgr.setSingleEditArea(res.getString("Select a Consignment"));
		} else {
			theAppMgr.setSingleEditArea(res.getString("Select a Pre-Sale"));
		}
	}

	/**
	 * put your documentation comment here
	 * @exception Exception
	 */
	private void jbinit() throws Exception {
		if (pnlViewTxn != null){
			remove(pnlViewTxn);
		}
		/*
		 * Use the Japanese panel only if the region is Japan and the mode is RESERVATIONS_CLOSE.
		 * If the Japanese panel class name has not been specified in pos.cfg, revert to the
		 * standard panel.
		 */
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION) &&
				(iAppletMode == InitialSaleApplet.RESERVATIONS_CLOSE)){
			String pnlViewTxnClassName = configMgr.getString("RESERVATIONS.VIEW_OPEN_TXN_PANEL");
			if (pnlViewTxnClassName != null) {
				pnlViewTxn = (ViewOpenTxnPanel) Class.forName(pnlViewTxnClassName).newInstance();
			} else {
				pnlViewTxn = new ViewOpenTxnPanel();
			}
		} else {
			pnlViewTxn = new ViewOpenTxnPanel();
		}
		
		this.setBackground(theAppMgr.getBackgroundColor());
		pnlViewTxn.setAppMgr(theAppMgr);
		this.setLayout(new BorderLayout());
		this.add(pnlViewTxn, BorderLayout.CENTER);
		pnlViewTxn.addMouseListener(new MouseAdapter() {
			/**
			 * put your documentation comment here
			 * @param me
			 */
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() != 2)
					return;
				tranHeaderSelected = pnlViewTxn.getSelectedTranHeader();
				if (applyPresaleTxn()) {
					theAppMgr.addStateObject("ARM_TXN_SELECTED", posTransFound);
					theAppMgr.goBack();
				}
			}
		});
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageDown(MouseEvent e) {
		pnlViewTxn.nextPage();
		theAppMgr.showPageNumber(e, ((PageNumberGetter) pnlViewTxn).getCurrentPageNumber() + 1, 
				((PageNumberGetter) pnlViewTxn).getTotalPages());
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void pageUp(MouseEvent e) {
		pnlViewTxn.prevPage();
		theAppMgr.showPageNumber(e, ((PageNumberGetter) pnlViewTxn).getCurrentPageNumber() + 1, 
				((PageNumberGetter) pnlViewTxn).getTotalPages());
	}
}
