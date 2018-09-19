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
 | 2    | 02-19-2005 | Vikram    | 104665    | Removed SSN Text Field                          |
 |      |            |           |           | Made Address, City, State, Zip & Phone read Only|
 |      |            |           |           | Made UserID as writeable                        |
 |      |            |           |           | Removed Register Fingerprint menu               |
 -----------------------------------------------------------------------------------------------
 | 3    | 05-24-2005 | Manpreet  | UAT-32    | Put a check to see if employees are loaded      |
 -----------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.employee;

import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.CMSFocusManager;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSInputVerifier;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.model.EmployeeAccessModel;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.util.LocaleManager;
import com.chelseasystems.cr.thumb.*;
import com.chelseasystems.cs.txnposter.CMSTxnPosterHelper;
import com.chelseasystems.cs.txnnumber.CMSTransactionNumberHelper;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.receipt.*;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cr.user.UserAuthInfo;
import com.chelseasystems.cr.user.*;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cs.util.Version;


/**
 */
public class EmployeeAccessApplet extends CMSApplet {
  //public class EmployeeAccessApplet extends JApplet {
  public static SimpleDateFormat dateFormat;
  public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";
  protected CMSEmployee employee;
  protected FingerPrintReader reader;
  private boolean isFingerPrintDllInstalled;
  private INIFile file;
  private Vector vAllRoles;
  private CMSEmployeeAccessAppModel accessAppModel;
  private CMSEmployee beforeEmployee;
  JCMSTextField fldFirst;
  JCMSTextField fldLast;
  JCMSTextField fldZip;
  JCMSTextField fldCity;
  JCMSTextField fldMiddle;
  JCMSTextField fldAddress;
  JCMSTextField fldState;
  JCMSTextField fldHomePhone;
  JCMSTextField fldSSN;
  JCMSTextField fldSName;
  JCMSTextField fldHireDate;
  JCMSTextField fldStore;
  JCMSComboBox localeCombo;
  // tab two
  JCMSTextField fldExternalId;
  JCMSComboBox cboEmploymentStatus;
  ButtonGroup grpWageType;
  JRadioButton rbtnWageTypeHourly;
  JRadioButton rbtnWageTypeSalary;
  ButtonGroup grpPermTemp;
  JRadioButton rbtnPerm;
  JRadioButton rbtnTemp;
  ButtonGroup grpFullPart;
  JRadioButton rbtnFullTime;
  JRadioButton rbtnPartTimeWithHoliday;
  JRadioButton rbtnPartTimeWithoutHoliday;
  JCMSCheckBox chkOnTenHourByFourDayOTAgreement;
  RolodexLayout cardLayout;
  JPanel pnlRolodex;
  EmployeeAccessModel model;
  JCMSTable tbl;
  ItemListener localeListener;
  private GenericChooserRow[] availEmployees;
  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

  /**
   */
  public void init() {
    try {
      dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      reader = new FingerPrintReader(theAppMgr.getParentFrame());
      fldFirst = new JCMSTextField();
      fldLast = new JCMSTextField();
      fldZip = new JCMSTextField();
      fldCity = new JCMSTextField();
      fldMiddle = new JCMSTextField();
      fldAddress = new JCMSTextField();
      fldState = new JCMSTextField();
      fldHomePhone = new JCMSTextField();
      fldSSN = new JCMSTextField();
      fldSName = new JCMSTextField();
      fldHireDate = new JCMSTextField();
      fldStore = new JCMSTextField();
      model = new EmployeeAccessModel();
      tbl = new JCMSTable(model, JCMSTable.SELECT_ROW);
      employee = new CMSEmployee();
      // tab two
      fldExternalId = new JCMSTextField();
      cboEmploymentStatus = new JCMSComboBox();
      grpWageType = new ButtonGroup();
      rbtnWageTypeHourly = new JRadioButton();
      rbtnWageTypeSalary = new JRadioButton();
      grpPermTemp = new ButtonGroup();
      rbtnPerm = new JRadioButton();
      rbtnTemp = new JRadioButton();
      grpFullPart = new ButtonGroup();
      rbtnFullTime = new JRadioButton();
      rbtnPartTimeWithHoliday = new JRadioButton();
      rbtnPartTimeWithoutHoliday = new JRadioButton();
      chkOnTenHourByFourDayOTAgreement = new JCMSCheckBox();
      // not currently used
      chkOnTenHourByFourDayOTAgreement.setVisible(false);
      jbInit();
      loadRoles();
      loadLocales();
      loadEmploymentStatus();
      centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
      //loadEmployees();
      try {
        System.loadLibrary("FPJni");
        isFingerPrintDllInstalled = true;
      } catch (java.lang.UnsatisfiedLinkError usle) {
        isFingerPrintDllInstalled = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   */
  public void start() {
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    loadEmployees();
    employee = new CMSEmployee();
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS, theOpr, theAppMgr.PREV_BUTTON);
    enterEmployeeShortName();
    enableFields(false);
    clearFieldsandObjects();
    model.clear();
    cardLayout.show(pnlRolodex, "pnlEmp");
    theAppMgr.addStateObject("EmployeeAccessApplet", this);
  }

  /**
   */
  public void stop() {}

  /**
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * @return
   */
  public String getScreenName() {
    return (res.getString("Employee Access"));
  }

  /**
   * @param enabled
   */
  public void appButtonEvent(String header, CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.startsWith("ADDITIONAL_EMP_INFO")) {
      appButtonEvent(anEvent);
      return;
    } else if (sAction.equals("EMPLOYEE_SEARCH")) {
      appButtonEvent(anEvent);
      return;
    }
    try {
      if (header.equalsIgnoreCase("EAM")) {
        if (sAction.equals("OK")) {
          try {
            buildEmployeeObject();
          } catch (BusinessRuleException bex) {
            CMSFocusManager.requestFocusFieldSupplyingInfo();
            theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
            ((CMSActionEvent)anEvent).consume();
            return;
          }
          // create appmodel
          if (employee.isNew()) {
            accessAppModel = new CMSEmployeeAccessAppModel(new ResourceBundleKey(
                "Employee Access: New"), (CMSEmployee)theOpr, null, employee, vAllRoles);
            accessAppModel.setBlueprintName(ReceiptBlueprintInventory.CMSEmployeeAccessNew);
            theAppMgr.showErrorDlg(res.getString("Please complete your new hire paperwork and forward to HomeOffice immediately. New Hires will not be paid until the paperwork is received and processed."));
          } else {
            accessAppModel = new CMSEmployeeAccessAppModel(new ResourceBundleKey(
                "Employee Access: Mod"), (CMSEmployee)theOpr, beforeEmployee, employee, vAllRoles);
            accessAppModel.setBlueprintName(ReceiptBlueprintInventory.CMSEmployeeAccessMod);
          }
          submitEmployee(employee);
          loadEmployees();
        } else if (sAction.equals("PREV")) {
          clearFieldsandObjects();
          model.clear();
          enterEmployeeShortName();
          theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS, theOpr, theAppMgr.PREV_BUTTON);
          anEvent.consume();
        } else if (sAction.equals("ADD_ROLE")) {
          EmployeeAccessDialog dlg = new EmployeeAccessDialog(theAppMgr.getParentFrame(), theAppMgr
              , vAllRoles);
          dlg.setVisible(true);
          UserAccessRole accessRole = (UserAccessRole)theAppMgr.getStateObject("ACCESS_ROLE");
          if (accessRole == null || employee.hasAccessRole(accessRole))
            return;
          theAppMgr.removeStateObject("ACCESS_ROLE");
          model.addAccessRole(accessRole);
          tbl.repaint();
        } else if (sAction.equals("DELETE_ROLE")) {
          int row = tbl.getSelectedRow();
          if (row < 0) {
            theAppMgr.showErrorDlg(res.getString("An access role must be selected."));
            return;
          } else {
            UserAccessRole role = model.getAccessRole(row);
            //if (employee.hasAccessRole(role)){ **No need to check , the employee obj already checks
            //employee.removeAccessRole(role);
            //}
            model.deleteAccessRole(row);
            tbl.repaint();
          }
        } else if (sAction.equals("REGISTER_FINGERPRINT")) {
          if (isFingerPrintDllInstalled) {
            acquireFingerprints();
          } else {
            theAppMgr.showErrorDlg(
                "A neccessary fingerprint library file is not installed on this terminal.");
          }
        } else if (sAction.equals("RESET_PASSWORD")) {
          if (theAppMgr.showOptionDlg(res.getString("Are you sure")
              , res.getString("Reset this employee's password?"))) {
            employee.setPassword("");
          }
        }
      }
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /**
   * @param enabled
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    try {
      if (sAction.equals("EMPLOYEE_SEARCH")) {
        employeeSearch();
      } else if (sAction.startsWith("ADDITIONAL_EMP_INFO")) {
        cardLayout.next(pnlRolodex);
        theAppMgr.reShowMenu();
      }
      /*else if (sAction.equals("REGISTER_FINGERPRINT")) {
       if (isFingerPrintDllInstalled) {
       acquireFingerprints();
       }
       else {
       theAppMgr.showErrorDlg("A neccessary fingerprint library file is not installed on this terminal.");
       }
       }*/
      else if (sAction.equals("PREV")) {
        if (fldLast.getText().length() > 0) { // stay in applet if previous is selected while emp info is displayed
          anEvent.consume();
        }
        clearFieldsandObjects();
        model.clear();
        enterEmployeeShortName();
        theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS, theOpr, theAppMgr.PREV_BUTTON);
      } else if (sAction.equals("MODIFY_EMPLOYEE")) {
        enableFields(true);
        theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS_MODIFY, "EAM", theOpr
            , theAppMgr.OK_CANCEL_BUTTON);
        selectOption();
      } else if (sAction.equals("NEW_EMPLOYEE")) {
        clearFieldsandObjects();
        employee.setNew();
        enableFields(true);
        fldHireDate.setText(dateFormat.format(new Date()));
        fldStore.setText(theStore.getId());
        theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS_MODIFY, "EAM", theOpr
            , theAppMgr.OK_CANCEL_BUTTON);
        selectOption();
      } else if (sAction.equals("REMOVE_EMPLOYEE")) {
        if (employee.isNew()) {
          fldLast.requestFocus();
          theAppMgr.showErrorDlg(res.getString("This employee does not yet exist in the database."));
          return;
        }
        if (!theAppMgr.showOptionDlg("Remove Employee Security"
            , "The employee's security access will be removed. Are you sure you want to do this?"))
          return;
        //temporarily use this method
        employee.setEmploymentStatusTerminated();
        employee.setTerminationDate(new Date());
        accessAppModel = new CMSEmployeeAccessAppModel(new ResourceBundleKey(
            "Employee Access: Terminated"), (CMSEmployee)theOpr, beforeEmployee, employee
            , vAllRoles);
        accessAppModel.setBlueprintName(ReceiptBlueprintInventory.CMSEmployeeAccessTerm);
        submitEmployee(employee);
        model.clear();
        enableFields(false);
        enterEmployeeShortName();
        theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS, theOpr, theAppMgr.PREV_BUTTON);
        clearFieldsandObjects();
        loadEmployees();
      }
    } catch (BusinessRuleException ex) {
      theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /**
   * put your documentation comment here
   */
  private void employeeSearch() {
    GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame()
        , theAppMgr, availEmployees, new String[] {res.getString("User Name")
        , res.getString("First Name"), res.getString("Last Name")
    });
    dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    dlg.setVisible(true);
    if (dlg.isOK())
      editAreaEvent("EMP_ID", (String)dlg.getSelectedRow().getRowKeyData());
  }

  /**
   */
  public void editAreaEvent(String command, String value) {
    try {
      if (command.equals("EMP_ID")) {
        CMSEmployee tempEmp = CMSEmployeeHelper.findByShortName(theAppMgr, value);
        if (tempEmp == null) {
          theAppMgr.showErrorDlg(res.getString(
              "Employee was not found, try again or select 'New Employee' option."));
          enterEmployeeShortName();
          return;
        }
        beforeEmployee = (CMSEmployee)tempEmp.clone();
        if (tempEmp.isEmploymentStatusTerminated()) {
          boolean ok = theAppMgr.showOptionDlg(res.getString("Employee Terminated")
              , res.getString("Employee has already been terminated. Do you want to reinstate this employee?"));
          if (!ok) {
            //enterEmployeeShortName();
            //return;
            theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS_INQUIRE, theOpr
                , theAppMgr.PREV_CANCEL_BUTTON);
          } else {
            tempEmp.setEmploymentStatusActive(); // this will clear peoplesoft termination date also
            // so that the emploee will get paid
            //tempEmp.setTerminationDate(null);  done internally by setEmploymentStatusActive
            tempEmp.setHireDate(new Date());
            theAppMgr.setSingleEditArea(res.getString("Select option."));
            theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS_MODIFY, "EAM", theOpr
                , theAppMgr.OK_CANCEL_BUTTON);
            employee = tempEmp; //set to global variable employee
            populateScreen(employee);
            enableFields(true);
            return;
          }
        } else {
          System.out.println("showing regular menu");
          theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS_FOUND, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
          enableFields(false);
        }
        employee = tempEmp; //set to global variable employee
        System.out.println("employee hire date: " + employee.getHireDate());
        populateScreen(employee);
        selectOption();
      }
    } catch (Exception ex) {
      theAppMgr.showErrorDlg(ex.getMessage());
    }
  }

  /**
   * callback when <b>Page Down</b> icon is clicked
   */
  public void pageDown(MouseEvent e) {
    model.nextPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  /**
   * callback when <b>Page Up</b> icon is clicked
   */
  public void pageUp(MouseEvent e) {
    model.prevPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  /**
   */
  private void loadLocales() {
    Locale[] locales = LocaleManager.getInstance().getSupportedLocales();
    for (int i = 0; i < locales.length; i++)
      localeCombo.addItem(locales[i]);
  }

  /**
   * @exception Exception
   */
  private void loadRoles()
      throws Exception {
    model.clear();
    StringTokenizer st;
    INIFile file = new INIFile(FileMgr.getLocalFile("config", "employee.cfg"), false);
    String roleList = file.getValue("ROLE_LIST");
    st = new StringTokenizer(roleList, ",");
    vAllRoles = new Vector();
    while (st.hasMoreElements()) {
      String sRole = (String)st.nextElement();
      if (file.getValue(sRole, "VISIBLE", "").equals("TRUE")) {
        String sRoleLabel = res.getString("employee.cfg." + sRole + ".LABEL");
        String sRoleLevel = file.getValue(sRole, "LEVEL", "");
        long longRole = new Long(sRoleLevel).intValue();
        UserAccessRole accessRole = new UserAccessRole(longRole, sRoleLabel);
        vAllRoles.add(accessRole);
      }
    }
  }

  /**
   *
   */
  private void loadEmploymentStatus() {
    cboEmploymentStatus.removeAllItems();
    cboEmploymentStatus.addItem(new EmploymentStatusChoice(res.getString("Active")
        , CMSEmployee.ACTIVE));
    cboEmploymentStatus.addItem(new EmploymentStatusChoice(res.getString("Terminated")
        , CMSEmployee.TERMINATED));
        /*Base Code: Identified as not required during ArmaniPOS_104665_TS_usermanagement_Rev0
         cboEmploymentStatus.addItem(new EmploymentStatusChoice(res.getString("On Leave"),
         CMSEmployee.ONLEAVE));
         cboEmploymentStatus.addItem(new EmploymentStatusChoice(res.getString("On Call"),
         CMSEmployee.ONCALL));
    */

  }

  public class EmploymentStatusChoice {
    int key;
    String display;

    /**
     *
     * @param         String display
     * @param         int key
     */
    EmploymentStatusChoice(String display, int key) {
      this.key = key;
      this.display = display;
    }

    /**
     *
     * @return
     */
    public String toString() {
      return (display);
    }

    /**
     *
     * @param compareTo
     * @return
     */
    public boolean equals(Object compareTo) {
      if (compareTo != null && ((EmploymentStatusChoice)compareTo).key == key)
        return (true);
      return (false);
    }
  }


  /**
   */
  private void enterEmployeeShortName() {
    theAppMgr.setSingleEditArea(res.getString("Enter the employee user name or select option.")
        , "EMP_ID");
  }

  /**
   */
  private void selectOption() {
    theAppMgr.setSingleEditArea(res.getString("Select option."));
  }

  /**
   * Need to eventually validate all the sets to make sure things like the stores are accurate
   * SName and SSN # are not already in use
   */
  private CMSEmployee buildEmployeeObject()
      throws BusinessRuleException {
    employee.setLastName(fldLast.getText());
    employee.setFirstName(fldFirst.getText());
    employee.setMiddleName(fldMiddle.getText());
    //edited by Vikram Mundhra
    employee.setShortName(fldSName.getText());
    //employee.doSetId(fldSName.getText());
    employee.doSetExternalID(fldSName.getText());
    if (employee.isNew()) {
      employee.doSetAddress(fldAddress.getText() == null ? "" : fldAddress.getText());
      employee.doSetCity(fldCity.getText() == null ? "" : fldCity.getText());
      employee.doSetState(fldState.getText() == null ? "" : fldState.getText());
      employee.doSetZipCode(fldZip.getText() == null ? "" : fldZip.getText());
      employee.doSetTelephone(Telephone.getInstanceForGUI(TelephoneType.EMPLOYEE
          , (fldHomePhone.getText() == null) ? "" : fldHomePhone.getText().trim()));
      employee.setHomeStore((Store)theStore);
      employee.setHireDate(new Date());
    }
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     employee.setAddress(fldAddress.getText());
     employee.setCity(fldCity.getText());
     employee.setState(fldState.getText());
     employee.setZipCode(fldZip.getText());
     employee.setTelephone(Telephone.getInstanceForGUI(TelephoneType.EMPLOYEE,
     fldHomePhone.getText().trim()));
     employee.setSSN(fldSSN.getText());
     //dont change employee home store when updating
     //employee.setHomeStore(theStore);
     //employee.setShortName(fldSName.getText());
     if (employee.isNew()) {
     employee.setHomeStore((Store)theStore);
     employee.setHireDate(new Date());
     employee.setShortName(findUniqueShortName(employee));
     }
     */
    localeComboChanged();
    for (Iterator it = vAllRoles.iterator(); it.hasNext(); ) {
      try {
        employee.removeAccessRole((UserAccessRole)it.next());
      } catch (Exception e) {}
    }
    Vector allRoles = model.getAllRows();
    if (allRoles.size() == 0) {
      employee.setAccessRole(new UserAccessRole( -1, ""));
    } else {
      for (Enumeration enm = allRoles.elements(); enm.hasMoreElements(); ) {
        UserAccessRole role = (UserAccessRole)enm.nextElement();
        employee.setAccessRole(role);
      }
    }
    // pier additional info to employee object
    EmploymentStatusChoice choice = (EmploymentStatusChoice)cboEmploymentStatus.getSelectedItem();
    if (choice != null)
      switch (choice.key) {
        case CMSEmployee.ACTIVE:
          employee.setEmploymentStatusActive();
          break;
        case CMSEmployee.TERMINATED:
          employee.setEmploymentStatusTerminated();
          if (employee.getTerminationDate() == null) {
            employee.setTerminationDate(new Date());
            theAppMgr.showErrorDlg(res.getString(
                "Please complete the Associate's termination paperwork and forward to Home Office immediately."));
          }
          break;
        case CMSEmployee.ONLEAVE:
          employee.setEmploymentStatusOnLeave();
          break;
          //case CMSEmployee.ONCALL:
          //    employee.setEmploymentStatusOnCall();
          //    break;
      }
    //employee.setPeopleSoftID(fldPeopleSoftId.getText());  set by batch process, not this screen
    if (rbtnWageTypeHourly.isSelected())
      employee.setWagesHourly();
    else if (rbtnWageTypeSalary.isSelected())
      employee.setWagesSalaried();
    if (rbtnPerm.isSelected())
      employee.setPermanent();
    else if (rbtnTemp.isSelected())
      employee.setTemporary();
    if (rbtnFullTime.isSelected())
      employee.setFullTime();
    else if (rbtnPartTimeWithHoliday.isSelected())
      employee.setPartTime();
    else if (rbtnPartTimeWithoutHoliday.isSelected())
      employee.setLessThan20HoursPartTime();
    //if (chkOnTenHourByFourDayOTAgreement.isSelected())
    //    employee.setOn10Hours4DaysPerWeekAgreement();
    //else
    //    employee.setNotOn10Hours4DaysPerWeekAgreement();
    return (employee);
  }

  /**
   * put your documentation comment here
   * @param employee
   * @return
   */
  private String findUniqueShortName(CMSEmployee employee) {
    String shortName = "";
    theAppMgr.setWorkInProgress(true);
    try {
      shortName = CMSEmployee.findUniqueShortName(theAppMgr, employee);
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    } finally {
      theAppMgr.setWorkInProgress(false);
      return (shortName);
    }
  }

  /**
   * @param employee
   */
  private void submitEmployee(final CMSEmployee emp) {
    if (emp != null) {
      try {
        if (!CMSEmployeeHelper.submit(theAppMgr, emp)) {
          theAppMgr.showErrorDlg("The system was unable to submit the employee." + "  "
              + "Please re-enter later.");
          return;
        }
        accessAppModel.setAfterEmployee(employee);
        accessAppModel.print(theAppMgr);
        Thread thread = new Thread(new Runnable() {

          /**
           * put this in a seperate thread because it may significantly slow
           * down the client if the peers are off-line, bug 4203 - CG
           */
          public void run() {
            IEmployeeRMIPeer employeePeer = null;
            theAppMgr.setWorkInProgress(true);
            System.out.println("Employee is being updated to other peers");
            Remote[] rmts = theAppMgr.getPeerStubs("employee");
            for (int x = 0; x < rmts.length; x++) {
              try {
                employeePeer = (IEmployeeRMIPeer)rmts[x];
                employeePeer.submitLocalEmployee(emp);
              } catch (Exception ex) {
                theAppMgr.removeRemotePeerStub("employee", employeePeer);
                //System.out.println("EmployeeAccessApplet.submitEmployee()->"  + ex);
              }
            }
            theAppMgr.setWorkInProgress(false);
          }
        });
        thread.start();
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      } finally {
        theAppMgr.setWorkInProgress(false);
      }
    }
  }

  /**
   */
  private void acquireFingerprints() {
    try {
      UserAuthInfo authInfo = new UserAuthInfo();
      authInfo.setUserId(employee.getId());
      theAppMgr.showErrorDlg("Press 'OK' to capture your primary fingerprint.");
      byte[] primary = reader.acquireMaster();
      if (primary != null) {
        authInfo.setPrimary(primary);
        theAppMgr.getParentFrame().toFront();
        theAppMgr.showErrorDlg("Press 'OK' to capture your secondary fingerprint.");
        byte[] secondary = reader.acquireMaster();
        if (secondary != null) {
          authInfo.setSecondary(secondary);
          theAppMgr.getParentFrame().toFront();
          theAppMgr.showMenu(MenuConst.OK_CANCEL, "CONFIRM", theOpr);
          theAppMgr.setSingleEditArea("Press 'OK' to post the new prints.");
          TransactionEmployeeAuthInfo txn = new TransactionEmployeeAuthInfo(authInfo
              , (CMSStore)theStore);
          txn.setTheOperator((CMSEmployee)theOpr);
          txn.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr
              , (CMSStore)theStore, (CMSRegister)theAppMgr.getGlobalObject("REGISTER")));
          //  to do:  ask Ruben if this should be moved to only happen if the EU presses "OK"?
          CMSTxnPosterHelper.post(theAppMgr, txn); // submit to the database
          CMSEmployeeHelper.submitAuthInfo(theAppMgr, txn); // place in the local cache
          accessAppModel = new CMSEmployeeAccessAppModel(new ResourceBundleKey(
              "Employee Access: Finger"), (CMSEmployee)theOpr, beforeEmployee, employee, vAllRoles);
          accessAppModel.setBlueprintName(ReceiptBlueprintInventory.CMSEmployeeAccessFinger);
          accessAppModel.print(theAppMgr);
          return; // avoid unnecessary toFront();
        }
      }
      // else user cancelled
      theAppMgr.getParentFrame().toFront();
    } catch (BusinessRuleException be) {
      theAppMgr.showErrorDlg(res.getString(be.getMessage()));
    } catch (FingerPrintReaderRecoverableException fpre) {
      if (fpre.getErrorCode() != FingerPrintReaderException.OPER_CANCEL)
        theAppMgr.showErrorDlg(fpre.getMessage());
    } catch (FingerPrintReaderException fpe) {
      theAppMgr.showExceptionDlg(fpe);
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
    }
  }

  /**
   *Resizing of the content pane
   */
  void tbl_componentResized(ComponentEvent e) {
    model.setRowsShown(tbl.getHeight() / tbl.getRowHeight());
    tbl.repaint();
  }

  /**
   */
  private void localeComboChanged() {
    if (localeCombo.getSelectedIndex() > -1)
      try {
        //System.out.println( "setting country: " + ((Locale)localeCombo.getSelectedItem()).getCountry());
        //System.out.println( "setting language: " + ((Locale)localeCombo.getSelectedItem()).getLanguage());
        employee.setPreferredISOCountry(((Locale)localeCombo.getSelectedItem()).getCountry());
        employee.setPreferredISOLanguage(((Locale)localeCombo.getSelectedItem()).getLanguage());
      } catch (BusinessRuleException e) {
        theAppMgr.showErrorDlg(e.getLocalizedMessage());
      }
  }

  /**
   * @param emp
   */
  private void populateScreen(CMSEmployee emp) {
    try {
      fldLast.setText(emp.getLastName());
      fldFirst.setText(emp.getFirstName());
      fldAddress.setText(emp.getAddress());
      fldCity.setText(emp.getCity());
      fldState.setText(emp.getState());
      fldZip.setText(emp.getZipCode());
      fldMiddle.setText(emp.getMiddleName());
      fldHomePhone.setText(emp.getTelephone().getFormattedNumber() == null ? ""
          : emp.getTelephone().getFormattedNumber());
      fldSSN.setText(emp.getSSN());
      fldStore.setText(emp.getHomeStoreId());
      fldSName.setText(emp.getShortName());
      fldHireDate.setText(dateFormat.format(emp.getHireDate()));
      if (emp.getPreferredISOCountry() != null && emp.getPreferredISOLanguage() != null) {
        localeCombo.removeItemListener(localeListener);
        System.out.println("setting locale combo to display emps preferred language");
        Locale empLocale = new Locale(emp.getPreferredISOLanguage(), emp.getPreferredISOCountry());
        localeCombo.setSelectedItem(empLocale);
        localeCombo.addItemListener(localeListener);
      }
      //populate the table with assigned employee access roles
      Enumeration enumRoles = vAllRoles.elements();
      Vector vCurrentRoles = emp.hasWhichAccessRoles(enumRoles);
      for (Enumeration enm = vCurrentRoles.elements(); enm.hasMoreElements(); ) {
        UserAccessRole role = (UserAccessRole)enm.nextElement();
        model.addAccessRole(role);
        tbl.repaint();
        //theAppMgr.setSingleEditArea(res.getString("Select option."));
        //theAppMgr.showMenu(MenuConst.EMPLOYEE_ACCESS_MODIFY, "EAM",
        //        theOpr, theAppMgr.OK_CANCEL_BUTTON);
      }
      // pier additional info from employee object
      if (employee.isEmploymentStatusActive()) {
        cboEmploymentStatus.setSelectedIndex(0);
      } else if (employee.isEmploymentStatusTerminated()) {
        cboEmploymentStatus.setSelectedIndex(1);
      } else if (employee.isEmploymentStatusOnLeave()) {
        cboEmploymentStatus.setSelectedIndex(2);
      }
      //else if (employee.isEmploymentStatusOnCall()) {
      //    cboEmploymentStatus.setSelectedIndex(3);
      //}
      fldExternalId.setText(employee.getExternalID());
      if (employee.isWagesHourly())
        rbtnWageTypeHourly.setSelected(true);
      else if (employee.isWagesSalaried())
        rbtnWageTypeSalary.setSelected(true);
      if (employee.isPermanent())
        rbtnPerm.setSelected(true);
      else if (employee.isTemporary())
        rbtnTemp.setSelected(true);
      if (employee.isFullTime())
        rbtnFullTime.setSelected(true);
      else if (employee.isPartTime())
        rbtnPartTimeWithHoliday.setSelected(true);
      else if (employee.isLessThan20HoursPartTime())
        rbtnPartTimeWithoutHoliday.setSelected(true);
      //if (employee.isOn10Hours4DaysPerWeekAgreement())
      //    chkOnTenHourByFourDayOTAgreement.setSelected(true);
      //else
      chkOnTenHourByFourDayOTAgreement.setSelected(false);
    } catch (Exception ex) {
      theAppMgr.showErrorDlg(ex.getMessage());
    }
  }

  /**
   */
  private void clearFieldsandObjects() {
    employee = new CMSEmployee(); // always rest employee when clearing fields
    fldFirst.setText("");
    fldLast.setText("");
    fldZip.setText("");
    fldCity.setText("");
    fldMiddle.setText("");
    fldAddress.setText("");
    fldState.setText("");
    fldHomePhone.setText("");
    fldHireDate.setText("");
    fldStore.setText("");
    fldSSN.setText("");
    fldSName.setText("");
    localeCombo.setSelectedIndex(0);
    cboEmploymentStatus.setSelectedIndex(0);
    fldExternalId.setText("");
    rbtnWageTypeHourly.setSelected(true);
    rbtnPerm.setSelected(true);
    rbtnFullTime.setSelected(true);
    cardLayout.show(pnlRolodex, "pnlEmp");
  }

  /**
   * @param enabled
   */
  private void enableFields(boolean enabled) {
    cboEmploymentStatus.setEnabled(enabled); // they can update status always
    if (fldExternalId.getText().length() > 0) {
      enabled = false;
    }
    fldFirst.setEnabled(enabled);
    fldLast.setEnabled(enabled);
    fldMiddle.setEnabled(enabled);
    fldFirst.setEditable(enabled);
    fldLast.setEditable(enabled);
    fldMiddle.setEditable(enabled);
    //edited by Vikram Mundhra
    fldZip.setEnabled(false);
    fldCity.setEnabled(false);
    fldAddress.setEnabled(false);
    fldState.setEnabled(false);
    fldHomePhone.setEnabled(false);
    fldSSN.setEnabled(false);
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     fldZip.setEnabled(enabled);        fldCity.setEnabled(enabled);
     fldAddress.setEnabled(enabled);
     fldState.setEnabled(enabled);
     fldHomePhone.setEnabled(enabled);
     fldSSN.setEnabled(enabled);
     */
    fldHireDate.setEnabled(false);
    fldStore.setEnabled(false);
    //edited by Vikram Mundhra
    fldSName.setEnabled(true);
    fldSName.setEditable(true);
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     fldSName.setEnabled(false);
     */
    localeCombo.setEnabled(enabled);
    if (enabled)
      fldLast.requestFocus();
    fldExternalId.setEnabled(false);
    rbtnWageTypeHourly.setEnabled(enabled);
    rbtnWageTypeSalary.setEnabled(enabled);
    rbtnPerm.setEnabled(enabled);
    rbtnTemp.setEnabled(enabled);
    rbtnFullTime.setEnabled(enabled);
    rbtnPartTimeWithHoliday.setEnabled(enabled);
    rbtnPartTimeWithoutHoliday.setEnabled(enabled);
    chkOnTenHourByFourDayOTAgreement.setEnabled(enabled);
  }

  /**
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    JPanel pnlEmp = new JPanel();
    JPanel pnlPosition = new JPanel();
    JCMSLabel lblState = new JCMSLabel();
    JCMSLabel lblHireDate = new JCMSLabel();
    JCMSLabel lblSName = new JCMSLabel();
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     JCMSLabel lblSSN = new JCMSLabel();
     */
    JCMSLabel lblHomePhone = new JCMSLabel();
    JCMSLabel lblFirst = new JCMSLabel();
    JCMSLabel lblStore = new JCMSLabel();
    JCMSLabel lblZip = new JCMSLabel();
    JCMSLabel lblAddress = new JCMSLabel();
    JCMSLabel lblLast = new JCMSLabel();
    JCMSLabel lblMiddle = new JCMSLabel();
    JCMSLabel lblCity = new JCMSLabel();
    JCMSLabel lblLocale = new JCMSLabel();
    JCMSLabel lblDummy = new JCMSLabel();
    Color bgColor = theAppMgr.getBackgroundColor();
    // tab 2
    JPanel pnlEmpAdditional = new JPanel();
    pnlEmpAdditional.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white
        , new Color(142, 142, 142)), res.getString("ADDITIONAL EMPLOYEE INFORMATION")));
    pnlRolodex = new JPanel();
    cardLayout = new RolodexLayout();
    pnlRolodex.setLayout(cardLayout);
    pnlRolodex.add(pnlEmp, "pnlEmp");
    pnlRolodex.add(pnlEmpAdditional, "pnlEmpAdditional");
    JCMSLabel lblPeopleSoftId = new JCMSLabel();
    lblPeopleSoftId.setAppMgr(theAppMgr);
    lblPeopleSoftId.setText(res.getString("External ID") + ":");
    JCMSLabel lblEmploymentStatus = new JCMSLabel();
    lblEmploymentStatus.setText(res.getString("Employment Status") + ":");
    lblEmploymentStatus.setAppMgr(theAppMgr);
    pnlEmpAdditional.setBackground(bgColor);
    fldExternalId.setAppMgr(theAppMgr);
    chkOnTenHourByFourDayOTAgreement.setAppMgr(theAppMgr);
    chkOnTenHourByFourDayOTAgreement.setText(res.getString("Apply Ten Hours x Four Days Exception"));
    cboEmploymentStatus.setAppMgr(theAppMgr);
    chkOnTenHourByFourDayOTAgreement.setAppMgr(theAppMgr);
    Color nonFading = new Color(bgColor.getRGB()) {

      /**
       * put your documentation comment here
       * @return
       */
      public Color darker() {
        return (Color.black);
      }

      /**
       * put your documentation comment here
       * @return
       */
      public Color brighter() {
        return (this);
      }
    };
    rbtnWageTypeHourly.setBackground(nonFading);
    rbtnWageTypeHourly.setText(res.getString("Hourly"));
    rbtnWageTypeHourly.setBackground(nonFading);
    rbtnWageTypeSalary.setText(res.getString("Salaried"));
    rbtnWageTypeSalary.setBackground(nonFading);
    grpWageType.add(rbtnWageTypeHourly);
    grpWageType.add(rbtnWageTypeSalary);
    rbtnPerm.setText(res.getString("Permanent"));
    rbtnPerm.setBackground(nonFading);
    rbtnTemp.setText(res.getString("Temporary"));
    rbtnTemp.setBackground(nonFading);
    grpPermTemp.add(rbtnPerm);
    grpPermTemp.add(rbtnTemp);
    rbtnFullTime.setText(res.getString("Full Time"));
    rbtnFullTime.setBackground(nonFading);
    rbtnPartTimeWithHoliday.setText(res.getString("Part Time (with Holiday)"));
    rbtnPartTimeWithHoliday.setBackground(nonFading);
    rbtnPartTimeWithoutHoliday.setText(res.getString("Part Time (without Holiday)"));
    rbtnPartTimeWithoutHoliday.setBackground(nonFading);
    grpFullPart.add(rbtnFullTime);
    grpFullPart.add(rbtnPartTimeWithHoliday);
    grpFullPart.add(rbtnPartTimeWithoutHoliday);
    pnlEmpAdditional.setLayout(new GridLayout(0, 1));
    fldExternalId.setPreferredSize(new Dimension((int)(300 * r), (int)(30 * r)));
    //fldPeopleSoftId.setDocument(new TextFilter(TextFilter.NUMERIC),6);  can't enter this field anyway...
    lblPeopleSoftId.setPreferredSize(new Dimension((int)(300 * r), (int)(30 * r)));
    lblEmploymentStatus.setPreferredSize(new Dimension((int)(300 * r), (int)(30 * r)));
    cboEmploymentStatus.setPreferredSize(new Dimension((int)(300 * r), (int)(30 * r)));
    JPanel pnlOne = new JPanel();
    pnlOne.setLayout(new GridLayout(1, 2));
    JPanel pnlOneLeft = new JPanel();
    pnlOneLeft.setBackground(bgColor);
    JPanel pnlOneRight = new JPanel();
    pnlOneRight.setBackground(bgColor);
    pnlOneLeft.add(lblPeopleSoftId);
    pnlOneLeft.add(fldExternalId);
    pnlOneRight.add(lblEmploymentStatus);
    pnlOneRight.add(cboEmploymentStatus);
    pnlOne.add(pnlOneLeft);
    pnlOne.add(pnlOneRight);
    pnlEmpAdditional.add(pnlOne);
    /*
     //JCMSLabel dummyLbl = new JCMSLabel();
     //dummyLbl.setPreferredSize(new Dimension((int)(300*r), (int)(20*r)));
     JPanel pnlTwo = new JPanel();
     pnlTwo.setLayout(new GridLayout(1,2));
     JPanel pnlTwoLeft = new JPanel();
     pnlTwoLeft.setBackground(bgColor);
     JPanel pnlTwoRight = new JPanel();
     pnlTwoRight.setBackground(bgColor);
     pnlTwoLeft.add(chkOnTenHourByFourDayOTAgreement);
     pnlTwo.add(pnlTwoLeft);
     pnlTwo.add(pnlTwoRight);
     pnlEmpAdditional.add(pnlTwo);
     */
    //rbtnWageTypeHourly.setPreferredSize(new Dimension((int)(300*r), (int)(30*r)));
    //rbtnWageTypeSalary.setPreferredSize(new Dimension((int)(300*r), (int)(30*r)));
    JPanel pnlWage = new JPanel();
    pnlWage.setBackground(bgColor);
    pnlWage.setLayout(new GridLayout(1, 2));
    JPanel pnlWageLeft = new JPanel();
    pnlWageLeft.setBackground(bgColor);
    pnlWageLeft.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder((int)(
        5 * r), (int)(10 * r), (int)(5 * r), (int)(10 * r))
        , BorderFactory.createEtchedBorder(Color.white, bgColor)));
    JPanel pnlWageRight = new JPanel();
    pnlWageRight.setBackground(bgColor);
    pnlWageRight.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder((int)(
        5 * r), (int)(10 * r), (int)(5 * r), (int)(10 * r))
        , BorderFactory.createEtchedBorder(Color.white, bgColor)));
    pnlWage.add(pnlWageLeft);
    pnlWage.add(pnlWageRight);
    rbtnWageTypeHourly.setPreferredSize(new Dimension((int)(300 * r), (int)(25 * r)));
    rbtnWageTypeSalary.setPreferredSize(new Dimension((int)(300 * r), (int)(25 * r)));
    rbtnPerm.setPreferredSize(new Dimension((int)(300 * r), (int)(25 * r)));
    rbtnTemp.setPreferredSize(new Dimension((int)(300 * r), (int)(25 * r)));
    pnlWageLeft.add(rbtnWageTypeHourly);
    pnlWageLeft.add(rbtnWageTypeSalary);
    pnlWageRight.add(rbtnPerm);
    pnlWageRight.add(rbtnTemp);
    pnlEmpAdditional.add(pnlWage);
    /*
     //rbtnPerm.setPreferredSize(new Dimension((int)(300*r), (int)(30*r)));
     //rbtnTemp.setPreferredSize(new Dimension((int)(300*r), (int)(30*r)));
     JPanel pnlPermTemp = new JPanel();
     pnlPermTemp.setLayout(new GridLayout(1,2));
     pnlPermTemp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder((int)(5*r),(int)(10*r),(int)(5*r),(int)(10*r)),BorderFactory.createEtchedBorder(Color.white,
     bgColor)));
     JPanel pnlPermTempLeft = new JPanel();
     pnlPermTempLeft.setBackground(bgColor);
     JPanel pnlPermTempRight = new JPanel();
     pnlPermTempRight.setBackground(bgColor);
     pnlPermTemp.add(pnlPermTempLeft);
     pnlPermTemp.add(pnlPermTempRight);
     pnlPermTempLeft.add(rbtnPerm);
     pnlPermTempRight.add(rbtnTemp);
     pnlEmpAdditional.add(pnlPermTemp);
     */
    //rbtnFullTime.setPreferredSize(new Dimension((int)(300*r), (int)(30*r)));
    //rbtnPartTime.setPreferredSize(new Dimension((int)(300*r), (int)(30*r)));
    JPanel pnlFullPart = new JPanel();
    pnlFullPart.setLayout(new GridLayout(1, 2));
    JPanel pnlFullPartLeft = new JPanel();
    pnlFullPartLeft.setBackground(bgColor);
    pnlFullPartLeft.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder((int)(
        5 * r), (int)(10 * r), (int)(5 * r), (int)(10 * r))
        , BorderFactory.createEtchedBorder(Color.white, bgColor)));
    JPanel pnlFullPartRight = new JPanel();
    pnlFullPartRight.setBackground(bgColor);
    pnlFullPart.add(pnlFullPartLeft);
    pnlFullPart.add(pnlFullPartRight);
    rbtnFullTime.setPreferredSize(new Dimension((int)(300 * r), (int)(25 * r)));
    rbtnPartTimeWithHoliday.setPreferredSize(new Dimension((int)(300 * r), (int)(25 * r)));
    rbtnPartTimeWithoutHoliday.setPreferredSize(new Dimension((int)(300 * r), (int)(25 * r)));
    //chkOnTenHourByFourDayOTAgreement.setPreferredSize(new Dimension((int)(200*r), (int)(25*r)));
    pnlFullPartLeft.add(rbtnFullTime);
    pnlFullPartLeft.add(rbtnPartTimeWithHoliday);
    pnlFullPartLeft.add(rbtnPartTimeWithoutHoliday);
    pnlFullPartRight.add(chkOnTenHourByFourDayOTAgreement);
    pnlEmpAdditional.add(pnlFullPart);
    localeCombo = new JCMSComboBox();
    localeCombo.setEditable(false);
    LocaleRenderer renderer = new LocaleRenderer();
    LocaleRendererBig rendererBig = new LocaleRendererBig();
    localeCombo.setRenderer(rendererBig);
    localeCombo.setEditRenderer(renderer);
    pnlEmp.setBackground(bgColor);
    pnlPosition.setBackground(bgColor);
    this.setBackground(bgColor);
    this.setBorder(BorderFactory.createEmptyBorder((int)(10 * r), (int)(10 * r), (int)(10 * r)
        , (int)(10 * r)));
    fldHomePhone.setDocument(new TextFilter(TextFilter.NUMERIC + "-() "));
    lblHomePhone.setText(res.getString("Home Phone Number") + ":");
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     fldSSN.setDocument(new TextFilter(TextFilter.NUMERIC));
     lblSSN.setText(res.getString("Social Security Number") + ":");
     */
    pnlEmp.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white
        , new Color(142, 142, 142)), res.getString("EMPLOYEE")));
    lblZip.setText(res.getString("Zip Code") + ":");
    lblAddress.setText(res.getString("Address") + ":");
    lblLast.setText(res.getString("Last Name") + ":");
    lblMiddle.setText(res.getString("Middle Name") + ":");
    lblFirst.setText(res.getString("First Name") + ":");
    fldZip.setDocument(new TextFilter(TextFilter.ALPHA_NUMERIC + "-"));
    lblCity.setText(res.getString("City") + ":");
    pnlPosition.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white
        , new Color(142, 142, 142)), res.getString("ASSIGNED ACCESS ROLES")));
    lblStore.setText(res.getString("Home Store") + ":");
    //this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    lblState.setText(res.getString("State") + ":");
    lblState.setVerticalAlignment(SwingConstants.BOTTOM);
    lblHireDate.setText(res.getString("Hire Date") + ":");
    lblHireDate.setVerticalAlignment(SwingConstants.BOTTOM);
    lblLocale.setText("Preferred Language" + ":");
    fldHireDate.setDocument(new TextFilter(TextFilter.NUMERIC + "-/."));
    fldStore.setDocument(new TextFilter(TextFilter.ALPHA_NUMERIC));
    lblSName.setText(res.getString("User Name") + ":");
    //fldSName.setDocument(new TextFilter(TextFilter.ALPHA_NUMERIC));
    pnlPosition.setLayout(new BorderLayout());
    fldHomePhone.setAppMgr(theAppMgr);
    fldAddress.setAppMgr(theAppMgr);
    fldCity.setAppMgr(theAppMgr);
    fldFirst.setAppMgr(theAppMgr);
    fldLast.setAppMgr(theAppMgr);
    fldMiddle.setAppMgr(theAppMgr);
    fldState.setAppMgr(theAppMgr);
    fldZip.setAppMgr(theAppMgr);
    tbl.setAppMgr(theAppMgr);
    fldSSN.setAppMgr(theAppMgr);
    fldHireDate.setAppMgr(theAppMgr);
    fldStore.setAppMgr(theAppMgr);
    fldSName.setAppMgr(theAppMgr);
    Verifiers verifiers = new Verifiers();
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     fldSSN.setVerifier(verifiers.getSSNVerifier());
     */
    //fldSName.setVerifier(verifiers.getShortNameVerifier());
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     fldHomePhone.setVerifier(verifiers.getPhoneVerifier());
     fldAddress.setVerifier(verifiers.getAddressVerifier());
     fldCity.setVerifier(verifiers.getCityVerifier());
     */
    fldFirst.setVerifier(verifiers.getFirstVerifier());
    fldLast.setVerifier(verifiers.getLastVerifier());
    fldMiddle.setVerifier(verifiers.getMiddleVerifier());
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     fldState.setVerifier(verifiers.getStateVerifier());
     fldZip.setVerifier(verifiers.getZipCodeVerifier());
     */
    localeCombo.setAppMgr(theAppMgr);
    renderer.setAppMgr(theAppMgr);
    rendererBig.setAppMgr(theAppMgr);
    //sizes used for production to account for screen sizes
    fldSName.setPreferredSize(new Dimension((int)(130 * r), (int)(30 * r)));
    //edited by Vikram Mundhra
    if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
    	fldSName.setDocument(new TextFilter(TextFilter.ALPHA_NUMERIC, 9));
    } else if ("EUR".equalsIgnoreCase(Version.CURRENT_REGION)) {
    	//changed by shushma as to increase the user ID size while editing the employee's information 
    	//fldSName.setDocument(new CMSTextFilter(9));
    	fldSName.setDocument(new CMSTextFilter(12));
    	//change closed
    } else {
    	fldSName.setDocument(new TextFilter(TextFilter.NUMERIC, 9));
    }
    lblSName.setPreferredSize(new Dimension((int)(130 * r), (int)(20 * r)));
    //edited by Vikram Mundhra
    fldStore.setPreferredSize(new Dimension((int)(160 * r), (int)(30 * r)));
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     fldStore.setPreferredSize(new Dimension((int)(135*r), (int)(30*r)));
     */
    fldHireDate.setPreferredSize(new Dimension((int)(105 * r), (int)(30 * r)));
    lblHireDate.setPreferredSize(new Dimension((int)(105 * r), (int)(20 * r)));
    lblState.setPreferredSize(new Dimension((int)(250 * r), (int)(20 * r)));
    fldFirst.setPreferredSize(new Dimension((int)(250 * r), (int)(30 * r)));
    lblLast.setPreferredSize(new Dimension((int)(250 * r), (int)(20 * r)));
    fldLast.setPreferredSize(new Dimension((int)(250 * r), (int)(30 * r)));
    lblMiddle.setPreferredSize(new Dimension((int)(185 * r), (int)(20 * r)));
    lblFirst.setPreferredSize(new Dimension((int)(250 * r), (int)(20 * r)));
    lblZip.setPreferredSize(new Dimension((int)(150 * r), (int)(20 * r)));
    lblAddress.setPreferredSize(new Dimension((int)(700 * r), (int)(20 * r)));
    fldZip.setPreferredSize(new Dimension((int)(150 * r), (int)(30 * r)));
    fldCity.setPreferredSize(new Dimension((int)(290 * r), (int)(30 * r)));
    fldMiddle.setPreferredSize(new Dimension((int)(185 * r), (int)(30 * r)));
    lblCity.setPreferredSize(new Dimension((int)(290 * r), (int)(20 * r)));
    fldAddress.setPreferredSize(new Dimension((int)(700 * r), (int)(30 * r)));
    fldState.setPreferredSize(new Dimension((int)(250 * r), (int)(30 * r)));
    //pnlRolodex.setPreferredSize(new Dimension((int)(600*r), (int)(200*r)));
    pnlPosition.setPreferredSize(new Dimension((int)(300 * r), (int)(50 * r)));
    //edited by Vikram Mundhra
    lblStore.setPreferredSize(new Dimension((int)(160 * r), (int)(20 * r)));
    fldHomePhone.setPreferredSize(new Dimension((int)(290 * r), (int)(30 * r)));
    lblHomePhone.setPreferredSize(new Dimension((int)(290 * r), (int)(20 * r)));
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     lblStore.setPreferredSize(new Dimension((int)(135*r), (int)(20*r)));
     fldHomePhone.setPreferredSize(new Dimension((int)(160*r), (int)(30*r)));
     lblHomePhone.setPreferredSize(new Dimension((int)(160*r), (int)(20*r)));
     fldSSN.setPreferredSize(new Dimension((int)(150*r), (int)(30*r)));
     lblSSN.setPreferredSize(new Dimension((int)(150*r), (int)(20*r)));
     */
    lblLocale.setPreferredSize(new Dimension((int)(700 * r), (int)(20 * r)));
    localeCombo.setPreferredSize(new Dimension((int)(300 * r), (int)(30 * r)));
    lblDummy.setPreferredSize(new Dimension((int)(400 * r), (int)(30 * r)));
    //sizes used for GUI designer
    /*fldSName.setPreferredSize(new Dimension(130, 30));
     lblSName.setPreferredSize(new Dimension(130, 20));
     fldStore.setPreferredSize(new Dimension(135, 30));
     fldHireDate.setPreferredSize(new Dimension(105, 30));
     lblHireDate.setPreferredSize(new Dimension(105, 20));
     lblState.setPreferredSize(new Dimension(250, 20));
     fldFirst.setPreferredSize(new Dimension(250, 30));
     lblLast.setPreferredSize(new Dimension(250, 20));
     fldLast.setPreferredSize(new Dimension(250, 30));
     lblMiddle.setPreferredSize(new Dimension(185, 20));
     lblFirst.setPreferredSize(new Dimension(250, 20));
     lblZip.setPreferredSize(new Dimension(150, 20));
     lblAddress.setPreferredSize(new Dimension(690, 20));
     fldZip.setPreferredSize(new Dimension(150, 30));
     fldCity.setPreferredSize(new Dimension(280, 30));
     fldMiddle.setPreferredSize(new Dimension(185, 30));
     lblCity.setPreferredSize(new Dimension(280, 20));
     fldAddress.setPreferredSize(new Dimension(690, 30));
     fldState.setPreferredSize(new Dimension(250, 30));
     pnlEmp.setPreferredSize(new Dimension(520, 290));
     pnlPosition.setPreferredSize(new Dimension(300, 285));
     lblStore.setPreferredSize(new Dimension(135, 20));
     fldHomePhone.setPreferredSize(new Dimension(150, 30));
     lblHomePhone.setPreferredSize(new Dimension(150, 20));
     fldSSN.setPreferredSize(new Dimension(150, 30));
     lblSSN.setPreferredSize(new Dimension(150, 20));*/
    //this.getContentPane().add(pnlEmp);        //, BorderLayout.NORTH);
    this.getContentPane().add(pnlRolodex);
    pnlEmp.add(lblLast, null);
    pnlEmp.add(lblFirst, null);
    pnlEmp.add(lblMiddle, null);
    pnlEmp.add(fldLast, null);
    pnlEmp.add(fldFirst, null);
    pnlEmp.add(fldMiddle, null);
    pnlEmp.add(lblAddress, null);
    pnlEmp.add(fldAddress, null);
    pnlEmp.add(lblCity, null);
    pnlEmp.add(lblState, null);
    pnlEmp.add(lblZip, null);
    pnlEmp.add(fldCity, null);
    pnlEmp.add(fldState, null);
    pnlEmp.add(fldZip, null);
    pnlEmp.add(lblHomePhone, null);
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     pnlEmp.add(lblSSN, null);
     */
    pnlEmp.add(lblHireDate, null);
    pnlEmp.add(lblStore, null);
    pnlEmp.add(lblSName, null);
    pnlEmp.add(fldHomePhone, null);
    /*Base Code: Not required as per ArmaniPOS_104665_TS_usermanagement_Rev0
     pnlEmp.add(fldSSN, null);
     */
    pnlEmp.add(fldHireDate, null);
    pnlEmp.add(fldStore, null);
    pnlEmp.add(fldSName, null);
    pnlEmp.add(lblLocale, null);
    pnlEmp.add(localeCombo, null);
    pnlEmp.add(lblDummy, null);
    this.getContentPane().add(pnlPosition); //, BorderLayout.CENTER);
    pnlPosition.add(tbl, BorderLayout.CENTER);
    pnlPosition.add(tbl.getCMSTableHeader(), BorderLayout.NORTH);
    tbl.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tbl_componentResized(e);
      }
    });
    localeListener = new ItemListener() {

      /**
       * @param e
       */
      public void itemStateChanged(ItemEvent e) {
        localeComboChanged();
      }
    };
    fldLast.setDocument(new TextFilter(NAME_SPEC, 30));
    fldFirst.setDocument(new TextFilter(NAME_SPEC, 30));
    fldMiddle.setDocument(new TextFilter(NAME_SPEC, 30));
  }

  /**************************************************************************/
  private class LocaleRenderer extends BasicComboBoxRenderer {
    SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
    Color idleBackground = Color.white;

    /**
     */
    public LocaleRenderer() {
      super();
      this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     *
     * @param theAppMgr
     */
    public void setAppMgr(IApplicationManager theAppMgr) {
      if (theAppMgr != null) {
        idleBackground = theAppMgr.getEditAreaColor();
        this.setBackground(idleBackground);
        this.setFont(theAppMgr.getTheme().getTextFieldFont());
      }
    }

    //public Dimension getPreferredSize () {
    //   Dimension size = super.getPreferredSize();
    //   return  new Dimension(size.width, (int)(60*r));
    //}
    public Component getListCellRendererComponent(JList list, Object value, int index
        , boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      this.setBorder(BorderFactory.createRaisedBevelBorder());
      //this.setBackground(theAppMgr.getBackgroundColor());
      if (value != null)
        this.setText(((Locale)value).getDisplayName());
      else
        this.setText("");
      if (localeCombo.hasFocus())
        setBackground(Color.white);
      else
        setBackground(idleBackground);
      return (this);
    }
  }


  /**************************************************************************/
  private class LocaleRendererBig extends BasicComboBoxRenderer {
    SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();

    /**
     */
    public LocaleRendererBig() {
      super();
      this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     *
     * @param theAppMgr
     */
    public void setAppMgr(IApplicationManager theAppMgr) {
      if (theAppMgr != null) {
        Color idleBackground = theAppMgr.getEditAreaColor();
        this.setBackground(idleBackground);
        this.setFont(theAppMgr.getTheme().getTextFieldFont());
      }
    }

    /**
     * @return
     */
    public Dimension getPreferredSize() {
      Dimension size = super.getPreferredSize();
      return (new Dimension(size.width, size.height * 2));
    }

    /**
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     */
    public Component getListCellRendererComponent(JList list, Object value, int index
        , boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      this.setBorder(BorderFactory.createRaisedBevelBorder());
      //this.setBackground(theAppMgr.getBackgroundColor());
      if (value != null)
        this.setText(((Locale)value).getDisplayName());
      else
        this.setText("");
      return (this);
    }
  }


  //------------------------------------------------------------------
  //*********************   Verifier Classes   ***********************
   //------------------------------------------------------------------
   private class Verifiers {

     /**
      * @return
      */
     public CMSInputVerifier getSSNVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setSSN(fldSSN.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getSSNVerifier

     /**
      * put your documentation comment here
      * @return
      */
     public CMSInputVerifier getShortNameVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setShortName(fldSName.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getSSNVerifier

     /**
      * @return
      */
     public CMSInputVerifier getLastVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setLastName(fldLast.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getLastVerifier

     /**
      * @return
      */
     public CMSInputVerifier getFirstVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setFirstName(fldFirst.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getFirstVerifier

     /**
      * @return
      */
     public CMSInputVerifier getAddressVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setAddress(fldAddress.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getAddressVerifier

     /**
      * @return
      */
     public CMSInputVerifier getCityVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setCity(fldCity.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getCityVerifier

     /**
      * @return
      */
     public CMSInputVerifier getStateVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setState(fldState.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getStateVerifier

     /**
      * @return
      */
     public CMSInputVerifier getZipCodeVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setZipCode(fldZip.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getZipCodeVerifier

     /**
      * @return
      */
     public CMSInputVerifier getMiddleVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setMiddleName(fldMiddle.getText());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getMiddleVerifier

     /**
      * @return
      */
     public CMSInputVerifier getPhoneVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             employee.setTelephone(Telephone.getInstanceForGUI(TelephoneType.EMPLOYEE
                 , fldHomePhone.getText().trim()));
             fldHomePhone.setText(Telephone.getInstanceForGUI(TelephoneType.EMPLOYEE
                 , fldHomePhone.getText().trim()).getFormattedNumber());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getPhoneVerifier
   } // Verifiers


  /**
   * put your documentation comment here
   */
  private void loadEmployees() {
    CMSEmployee[] emps = null;
    try {
      emps = CMSEmployeeHelper.findByStore(theAppMgr, (CMSStore)theStore);
      if (emps == null)
        return;
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
      return;
    }
    Arrays.sort(emps);
    int terminatedCount = 0;
    for (int i = 0; i < emps.length; i++) {
      System.out.println("store employee: " + emps[i].getShortName());
      if (emps[i].isEmploymentStatusTerminated())
        terminatedCount++;
    }
    availEmployees = new GenericChooserRow[emps.length - terminatedCount];
    for (int i = 0, y = 0; i < emps.length; i++) {
      if (!emps[i].isEmploymentStatusTerminated()) {
        availEmployees[y++] = new GenericChooserRow(new String[] {emps[i].getShortName()
            , emps[i].getFirstName(), emps[i].getLastName()
        }, emps[i].getShortName());
      }
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public RolodexLayout getRolodexLayout() {
    return (cardLayout);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Container getRolodexContainer() {
    return (pnlRolodex);
  }
}

