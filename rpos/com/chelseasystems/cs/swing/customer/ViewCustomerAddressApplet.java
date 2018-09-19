/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 06-02-2005 | Vikram    | 98        |Removed explicit use of "USA" as store country|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-12-2005 | Khyati    | N/A       |1.Send Sale specification.                    |
 --------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.customer;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.address.Address;
import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cs.swing.panel.ViewAddressPanel;
import com.chelseasystems.cs.swing.dlg.AddressDlg;
import com.chelseasystems.cr.swing.layout.*;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.customer.CMSCustomerHelper;


/**
 * put your documentation comment here
 */
public class ViewCustomerAddressApplet extends CMSApplet {
	CMSCompositePOSTransaction theTxn;
	CMSShippingRequest shippingRequest;
	ViewAddressPanel pnlViewAddress;
	private JPanel viewPortPanel;
	private RolodexLayout cardLayout;
	CMSCustomer customer;

	/**
	 */
	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 */
	public void stop() {
	}

	public String getScreenName() {
		return (res.getString("Change Address"));
	}

	public String getVersion() {
		return ("$Revision: 1.1 $");
	}

	/**
	 * Start the applet.
	 */
	public void start() {
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		theAppMgr.addStateObject("ARM_FROM_SHIPPING_ADDRESS", "FROM_SHIPPING");
		clear();
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		theAppMgr.showMenu(MenuConst.SHIPPING_HEADER, theOpr);
		if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null) {
			customer = (CMSCustomer) theAppMgr.getStateObject("ARM_FISCAL_CUSTOMER");
			pnlViewAddress.setCustomer(customer);
			theAppMgr.removeStateObject("ARM_FISCAL_CUSTOMER");
		} else {
			theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN");
			if (theTxn == null && theAppMgr.getStateObject("ADD_FROM_INQUIRY") != null) {
				theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("THE_TXN");
			}
			shippingRequest = (CMSShippingRequest) theAppMgr.getStateObject("SHIPPING_REQUEST");
			customer = (CMSCustomer) theTxn.getCustomer();
			pnlViewAddress.setCustomer(customer);
			pnlViewAddress.setShippingRequest(shippingRequest);
			String fromShipping = (String) theAppMgr.getStateObject("ARM_FROM_SHIPPING_ADDRESS");
		}
		pnlViewAddress.setAppMgr(theAppMgr);
		theAppMgr.showMenu(MenuConst.SELECT_ADDRESS, theOpr);
	}

	/**
	 *
	 */
	private void displayShippingRequest() {
		// txtGiftMessage.setText(shippingRequest.getGiftMessage());
	}

	/**
	 *
	 */
	private void clear() {
		// txtGiftMessage.setText("");
	}

	/**
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		try {
			if (sAction.equalsIgnoreCase("ADD_ADDRESS")) {
				if (pnlViewAddress.getNumberAddresses() >= 5) {
					theAppMgr.showErrorDlg("Can't enter more than 5 addresses");
					return;
				}
				AddressDlg dlg = new AddressDlg(theAppMgr.getParentFrame(), theAppMgr, pnlViewAddress, true);
				dlg.setAppMgr(theAppMgr);
				dlg.setVisible(true);
				return;
			} else if (sAction.equalsIgnoreCase("MODIFY_ADDRESS")) {
				if (pnlViewAddress.getNumberAddresses() < 1) {
					theAppMgr.showErrorDlg("No Address present");
					return;
				}
				AddressDlg dlg = new AddressDlg(theAppMgr.getParentFrame(), theAppMgr, pnlViewAddress, false);
				dlg.setAppMgr(theAppMgr);
				dlg.setVisible(true);
				return;
			} else if (sAction.equalsIgnoreCase("REMOVE_ADDRESS")) {
				if (pnlViewAddress.getNumberAddresses() < 1) {
					theAppMgr.showErrorDlg("No Address present");
					return;
				}
				try {
					pnlViewAddress.deleteSelectedAddress();
					return;
				} catch (Exception e) {
				}
			} else if (sAction.equals("USE_ADDRESS")) {
				if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null) {
					if (pnlViewAddress.getSelectedAddress().getAddressFormat() != null 
							&& pnlViewAddress.getSelectedAddress().getAddressFormat().toUpperCase().indexOf("EUROPE") == -1) {
						theAppMgr.showErrorDlg(res.getString("Please select an EUROPE address format to proceed"));
						anEvent.consume();
						return;
					}
					Address selectedAddress = pnlViewAddress.getSelectedAddress();
					theAppMgr.addStateObject("ARM_FISCAL_ADDRESS", selectedAddress);
				} else {
					Store store = (Store) theAppMgr.getGlobalObject("STORE");
					Address selectedAddress = pnlViewAddress.getSelectedAddress();
					if (selectedAddress != null) {
						shippingRequest.setAddress(pnlViewAddress.getSelectedAddress());
						customer.setModified();
						if (pnlViewAddress.isPersistRequired() && theAppMgr.isOnLine())
							submitCustomer();
					} else {
						anEvent.consume();
						return;
					}
				}
			} else if (sAction.equals("CANCEL")) {
				// theAppMgr.goBack();
			} else if (sAction.equals("PREV")) {
				if (theAppMgr.getStateObject("ARM_PRINT_FISCAL_MODE") != null)
					theAppMgr.addStateObject("ARM_FISCAL_CUSTOMER", customer);
				// theAppMgr.goBack();
			}
		}
		catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	/**
	 * @param command
	 * @param value
	 */
	public void editAreaEvent(String command, String value) {
	}

	/**
	 * ks: Modified to Add ShippingAddressPanel to the existing Applet.
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		viewPortPanel = new JPanel();
		cardLayout = new RolodexLayout();
		viewPortPanel.setLayout(cardLayout);
		pnlViewAddress = new ViewAddressPanel();
		viewPortPanel.add(pnlViewAddress, pnlViewAddress);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
				res.getString("Basic Information")));
		this.setBackground(theAppMgr.getBackgroundColor());
		this.getContentPane().add(viewPortPanel, BorderLayout.CENTER);
	}

	/**
	 * Persist Customer.
	 */
	private void submitCustomer() {
		try {
			CMSCustomer cmsCustomer = CMSCustomerHelper.submit(theAppMgr, customer);
			if (cmsCustomer != null)
				customer = cmsCustomer;
			theAppMgr.addStateObject("CUSTOMER_SUBMIT", customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ********************************************************************* */
} // end Applet

