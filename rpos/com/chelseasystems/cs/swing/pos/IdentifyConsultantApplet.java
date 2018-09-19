/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 05-24-2005 | Manpreet  | UAT-31    | Put a check to see if employees are loaded      |
 -----------------------------------------------------------------------------------------------
 */
package com.chelseasystems.cs.swing.pos;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.EmpListPanel;
import com.chelseasystems.cs.util.Version;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 */
public class IdentifyConsultantApplet extends CMSApplet {
	// public class IdentifyConsultantApplet extends JApplet {
	private CMSCompositePOSTransaction theTxn;
	EmpListPanel pnlEmp = new EmpListPanel();
	private CMSEmployee[] employeeList;

	// Initialize the applet
	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Component initialization
	private void jbInit() throws Exception {
		JPanel pnlNorth = new JPanel();
		JLabel lblTitle = new JLabel();
		pnlEmp.setOpaque(false);
		pnlEmp.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
		this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
		this.getContentPane().add(pnlEmp, BorderLayout.CENTER);
		pnlNorth.add(lblTitle, null);
		pnlNorth.setOpaque(false);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setText(res.getString("Store") + " " + theStore.getId() + " " + res.getString("Associates"));
		lblTitle.setFont(theAppMgr.getTheme().getHeaderFont());
		this.setBackground(theAppMgr.getBackgroundColor());
		pnlEmp.setAppMgr(theAppMgr);
		pnlEmp.addMouseListener(new MouseAdapter() {
			/**
			 * put your documentation comment here
			 *
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				clickEvent(e);
			}
		});
	}

	// Start the applet
	public void start() {
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		// remove any stranded customers
		theAppMgr.removeStateObject("CUSTOMER_FOUND");
		pnlEmp.clear();
		populateConsultants();
		if (theAppMgr.getStateObject("CONSULTANT") == null) // came from InitialLoginApplet
			theAppMgr.showMenu(MenuConst.CONSULTANT, theOpr, theAppMgr.CANCEL_BUTTON);
		else
			// came from InitialSaleApplet
			theAppMgr.showMenu(MenuConst.CONSULTANT, theOpr, theAppMgr.PREV_BUTTON);
		// enterShortName();
		if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
			enterLastName();
		} else {
			enterShortName();
		}
	}

	// Stop the applet
	public void stop() {
	}

	/**
	 */
	private void enterShortName() {
		theAppMgr.setSingleEditArea(res.getString("Select or enter associate ID."), "CONSULTANT", theAppMgr.PASSWORD_MASK);
	}

	private void enterLastName() {
		theAppMgr.setSingleEditArea(res.getString("Select or enter Last name or ID"), "CONSULTANT"
		/* , theAppMgr.PASSWORD_MASK */);
	}

	/**
	 * callback whan an Eu presses enter
	 */
	public void clickEvent(MouseEvent e) {
		CMSEmployee emp = (CMSEmployee) pnlEmp.getSelectedEmployee();
		if (emp != null) {
			applyConsultant(emp);
		}
	}

	// callback whan an application tool bar button is pressed
	public void appButtonEvent(CMSActionEvent anEvent) {
		try {
			String sAction = anEvent.getActionCommand();
			if (sAction.equals("HOUSE_ID")) {
				applyConsultant(CMSEmployeeHelper.findByExternalId(theAppMgr, ((CMSStore) theStore).getDefaultConsultantId()));
			} else if ("PREV".equals(sAction)) {
				// ___Tim:Disable return to PaymentApplet after posting txn
				if (theAppMgr.getStateObject("NEW_TXN_AFTER_TENDER") != null) {
					theAppMgr.showErrorDlg("Cannot go back!!");
					anEvent.consume();
					return;
				}
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	// get name of screen
	public String getScreenName() {
		return res.getString("Login Associate");
	}

	// get version
	public String getVersion() {
		return "$Revision: 1.11.2.4.4.7 $";
	}

	/**
	 * callback whan an application tool bar button is pressed
	 */
	public void editAreaEvent(String Command, String sEdit) {
		if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
			try {
					CMSEmployee[] emps = findByLastName(sEdit);
					if (emps == null) {
						theAppMgr.showErrorDlg(res.getString("There are no commissioned employees for this store.."));
						pnlEmp.clear();
					} else {
						emps = filterTerminatedEmployees(emps);
						if (emps == null) {
							theAppMgr.showErrorDlg(res.getString("There are no commissioned employees for this store."));
							pnlEmp.clear();
							return;
						}
						pnlEmp.setEmployees(emps);
					}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
				enterLastName();
			}
		} else {
			try {
				CMSEmployee emp = CMSEmployeeHelper.findByExternalId(theAppMgr, sEdit);
			    if (emp != null && emp.isEmploymentStatusActive()) {
			    	applyConsultant(emp);
			    } else {
			    	theAppMgr.showErrorDlg(res.getString("Cannot find Associate."));
			        enterShortName();
			    }
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			    enterShortName();
			}
		}
	}

	/*
	 * Method to create a transaction and assign a consultant to it, then move to next applet.
	 */
	private void applyConsultant(CMSEmployee consultant) {
		theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		if (theTxn == null)
			allocTransaction();
		try {
			if (theAppMgr.getStateObject("ARM_REGISTER_VIP") != null) {
				theAppMgr.addStateObject("ARM_REGISTER_VIP", consultant);
			} else {
				theTxn.setConsultant(consultant);
			}
			theAppMgr.fireButtonEvent("HOUSE_ID");
			// theAppMgr.showMenu(MenuConst.ADD_ITEM_MENU, theOpr,null);
		} catch (BusinessRuleException ex) {
			theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
			enterShortName();
		}
	}

	/**
	 */
	private void populateConsultants() {
		try {
			CMSEmployee[] emps = CMSEmployeeHelper.findCommissionedByStore(theAppMgr, (CMSStore) theStore);
			if (emps == null) {
				theAppMgr.showErrorDlg(res.getString("There are no commissioned employees for this store."));
				pnlEmp.clear();
			} else {
				emps = filterTerminatedEmployees(emps);
				if (emps == null) {
					theAppMgr.showErrorDlg(res.getString("There are no commissioned employees for this store."));
					pnlEmp.clear();
					return;
				}
				pnlEmp.setEmployees(emps);
				employeeList = emps;
			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
		}
	}

	/**
	 */
	private void allocTransaction() {
		try {
			theTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
			theAppMgr.addStateObject("TXN_POS", theTxn);
			theAppMgr.addStateObject("NEW_TXN_POS", ""); // add state object that flags a new txn as been started
		} catch (Exception ex) {
			theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
			theAppMgr.showExceptionDlg(ex);
			theAppMgr.setSingleEditArea(res.getString("Press 'Home' to return home."), "ERROR");
		}
	}

	/**
	 * callback when page down is pressed
	 */
	public void pageDown(MouseEvent e) {
		pnlEmp.nextPage();
		theAppMgr.showPageNumber(e, pnlEmp.getCurrentPageNumber() + 1, pnlEmp.getTotalPages());
	}

	/**
	 * callback when page up is pressed
	 */
	public void pageUp(MouseEvent e) {
		pnlEmp.prevPage();
		theAppMgr.showPageNumber(e, pnlEmp.getCurrentPageNumber() + 1, pnlEmp.getTotalPages());
	}

	/**
	 * put your documentation comment here
	 *
	 * @param emps
	 * @return
	 */
	private CMSEmployee[] filterTerminatedEmployees(CMSEmployee[] emps) {
		if (emps == null)
			return null;
		ArrayList empList = new ArrayList();
		for (int index = 0; index < emps.length; index++) {
			CMSEmployee emp = emps[index];
			// Issue # 1055
			if (emp.isEmploymentStatusActive() && emp.getTerminationDate() == null) {
				empList.add(emp);
			}
		}
		return (CMSEmployee[]) empList.toArray(new CMSEmployee[empList.size()]);
	}

	// Mahesh: added this
	public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN_POS");
		// Mahesh: delete TransSessionStateObjects related to PHONE_ORDER
		if (theAppMgr.getTransSessionStateObject("PHONE_ORDER") != null) {
			theAppMgr.removeTransSessionStateObject("PHONE_ORDER");
		}
		if (theAppMgr.getTransSessionStateObject("CREDIT_CARD") != null) {
			theAppMgr.removeTransSessionStateObject("CREDIT_CARD");
		}
		if (cmsTxn == null) {
			return (true);
		} else {
			/*
			 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the
			 * Sign Off event for Fiscal Solutions Service START--
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
	}

	public CMSEmployee[] findByLastName(String lastName) {
		ArrayList selectedEmployees = new ArrayList();
		CMSEmployee tmpEmp = null;
		for (int i = 0; i < employeeList.length; i++) {
			tmpEmp = employeeList[i];
			if (lastName.trim().equals("")) {
				selectedEmployees.add(tmpEmp);
			} else if (tmpEmp != null && tmpEmp.getLastName().toUpperCase().startsWith(lastName.trim().toUpperCase())) {
				selectedEmployees.add(tmpEmp);
			} else if (tmpEmp != null && tmpEmp.getExternalID().equalsIgnoreCase(lastName)) {
				selectedEmployees.add(tmpEmp);
			}
		}
		return (CMSEmployee[]) selectedEmployees.toArray(new CMSEmployee[0]);
	}
}
