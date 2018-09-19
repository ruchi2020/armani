/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 06-08-2005 | Vikram    | 123       |Item Return Screen flow issue                 |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05/03/2005 | Khyati    | N/A       | Removed House Account after talking to P & D  |         |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.returns;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.menu.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.swing.panel.EmpListPanel;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 */
public class OriginalConsultantApplet extends CMSApplet {
  //public class OriginalConsultantApplet extends JApplet{
  private CMSCompositePOSTransaction theTxn;
  EmpListPanel pnlEmp = new EmpListPanel();

  //Initialize the applet
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit()
      throws Exception {
    JPanel pnlSouth = new JPanel();
    JPanel pnlWest = new JPanel();
    JPanel pnlEast = new JPanel();
    JPanel pnlNorth = new JPanel();
    JLabel lblTitle = new JLabel();
    this.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
    this.getContentPane().add(pnlWest, BorderLayout.WEST);
    this.getContentPane().add(pnlEast, BorderLayout.EAST);
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    this.getContentPane().add(pnlEmp, BorderLayout.CENTER);
    pnlNorth.add(lblTitle, null);
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    if (theStore != null)
      lblTitle.setText(res.getString("Store") + " " + ((CMSStore)theStore).getId() + " "
          + res.getString("Consultants"));
    lblTitle.setFont(theAppMgr.getTheme().getHeaderFont());
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlSouth.setBackground(theAppMgr.getBackgroundColor());
    pnlEast.setBackground(theAppMgr.getBackgroundColor());
    pnlWest.setBackground(theAppMgr.getBackgroundColor());
    pnlEmp.setAppMgr(theAppMgr);
    pnlEmp.addMouseListener(new MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        clickEvent(e);
      }
    });
    lblTitle.setPreferredSize(new Dimension((int)(200 * r), (int)(50 * r)));
    pnlNorth.setPreferredSize(new Dimension((int)(10 * r), (int)(56 * r)));
    pnlSouth.setPreferredSize(new Dimension((int)(10 * r), (int)(37 * r)));
    pnlEast.setPreferredSize(new Dimension((int)(50 * r), (int)(10 * r)));
    pnlWest.setPreferredSize(new Dimension((int)(50 * r), (int)(10 * r)));
  }

  //Stop the applet
  public void stop() {}

  //get name of screen
  public String getScreenName() {
    return res.getString("Original Consultant");
  }

  //get revision
  public String getVersion() {
    return "$Revision: 1.1 $";
  }

  //Start the applet
  public void start() {
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    pnlEmp.clear();
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    enterShortName();
    populateConsultants();
    //ks: Remove House account id and Prev button should display Cancel button only
    //      theAppMgr.showMenu(MenuConst.HOUSE_IDONLY, theOpr, theAppMgr.PREV_BUTTON);
    theAppMgr.showMenu(MenuConst.PREV_CANCEL, theOpr);
  }

  //callback whan an application tool bar button is pressed
  public void appButtonEvent(CMSActionEvent anEvent) {
    try {
      String sAction = anEvent.getActionCommand();
      if (sAction.equals("HOUSE_ID")) {
        setConsultant(CMSEmployeeHelper.findById(theAppMgr
            , ((CMSStore)theStore).getDefaultConsultantId()));
      }
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /** This method displays the list of consultants */
  public void populateConsultants() {
    try {
      CMSEmployee[] emps = CMSEmployeeHelper.findCommissionedByStore(theAppMgr, (CMSStore)theStore);
if(emps!=null){
      for (int x = 0; x < emps.length; x++) {
        pnlEmp.addEmployee(emps[x]);
		 }
      }
      pnlEmp.sortEmp();
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  // callback whan an application tool bar button is pressed
  public void editAreaEvent(String Command, String sEdit) {
    // validate consultant exists
    try {
      CMSEmployee consultant = CMSEmployeeHelper.findByShortName(theAppMgr, sEdit);
      if (consultant == null) {
        theAppMgr.showErrorDlg(res.getString("Cannot find consultant."));
        enterShortName();
        return;
      }
      setConsultant(consultant);
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /**
   */
  private void enterShortName() {
    theAppMgr.setSingleEditArea(res.getString("Select original consultant, or enter user name.")
        , "CONSULTANT", theAppMgr.UPPER_CASE_MASK);
  }

  /**
   * @return
   */
  private boolean isReturnOnly() {
    if (theAppMgr.getStateObject("TXN_POS") == null)
      return true;
    else
      return false;
  }

  /**
   * callback whan an Eu presses enter
   */
  public void clickEvent(MouseEvent me) {
    Employee emp = pnlEmp.getSelectedEmployee();
    if (emp != null)
      employeeSelected(emp);
  }

  /**
   * @param consultant
   */
  private void setConsultant(Employee consultant) {
    // set consultant
    if (theAppMgr.getStateObject("RETURN_MODE") != null) { //Is a straight return, no Sale
      try {
        theTxn.setConsultant(consultant);
      } catch (BusinessRuleException e) {
        theAppMgr.showErrorDlg(res.getString(e.getMessage()));
        enterShortName();
      }
    } else {
      theAppMgr.addStateObject("RETURN_SALE", ""); // signify a misc return through Ring a Sale
    }
    theAppMgr.addStateObject("ORIG_CONSULT", consultant); //save consultant to add to the LineItem Returns
    //TransactionPOS theTxn = (TransactionPOS) theAppMgr.getStateObject("TXN_POS");
    if (theTxn.getCustomer() == null) {
      theAppMgr.fireButtonEvent("CUST");
    } else if (theTxn.getCustomer().getId().equals(CMSCustomer.UNKNOWN_CUSTOMER_PHONE)) {
      theAppMgr.fireButtonEvent("CUST");
    } else {
      //theAppMgr.addStateObject("RETURN_SALE", "");
      theAppMgr.fireButtonEvent("OK");
    }
  }

  /**
   * This method reponses to the mouse click by sending the selected item
   * (consultant) in the global pool
   */
  public void employeeSelected(Employee emp) {
    setConsultant(emp);
  }

  /**
   * callback when page down icon is clicked
   */
  public void pageDown(MouseEvent e) {
    pnlEmp.nextPage();
    theAppMgr.showPageNumber(e, pnlEmp.getCurrentPageNumber() + 1, pnlEmp.getTotalPages());
  }

  /**
   * callback when page up icon is clicked
   */
  public void pageUp(MouseEvent e) {
    pnlEmp.prevPage();
    theAppMgr.showPageNumber(e, pnlEmp.getCurrentPageNumber() + 1, pnlEmp.getTotalPages());
  }

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
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
}

