/*
 * @copyright (c) 1998-2002 Retek Inc
 *
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
 | 2    | 02-14-2005 | Anand     | N/A       | File added by Anand to accept Associate ID   |
 |      |            |           |           | for login                                    |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.builder;

import java.util.Date;
import java.rmi.*;
import com.chelseasystems.cr.appmgr.IObjectBuilder;
import com.chelseasystems.cr.appmgr.IObjectBuilderManager;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.register.CMSRegisterSessionAppModel;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.util.*;


/**
 *
 */
public class AssociateUserNamePasswordBuilder implements IObjectBuilder {
  protected CMSEmployee theOperator;
  protected IObjectBuilderManager theBldrMgr;
  protected IApplicationManager theAppMgr;
  protected CMSApplet theApplet;
  protected String theCommand;
  protected Object initialValue;
  protected boolean isPasswordRequired;
  protected boolean isPasswordEntered;
  protected int passwordWrongCount;
  private CMSEmployeeAccessAppModel accessAppModel;

  /**
   * Default constructor.  Does nothing.
   */
  public AssociateUserNamePasswordBuilder() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    ConfigMgr configMgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
    isPasswordRequired = configMgr.getString("IS_PASSWORD_REQUIRED").equalsIgnoreCase("true");
  }

  /**
   *
   * @param command
   * @param theApplet
   * @param initialValue
   */
  public void build(String theCommand, CMSApplet theApplet, Object initialValue) {
    theAppMgr.setEditAreaFocus();
    this.theApplet = theApplet;
    this.theCommand = theCommand;
    this.initialValue = initialValue;
    if (completeAttributes())
      theBldrMgr.processObject(theApplet, theCommand, theOperator, this);
    else
      theAppMgr.setEditAreaFocus();
  }

  /**
   *
   * @param header
   * @param event
   */
  public void EditAreaEvent(String command, Object event) {
    if (command.equals("USER_NAME")) {
      try {
        theOperator = CMSEmployeeHelper.findByExternalId(theAppMgr, (String)event);
        if (theOperator == null) {
          theAppMgr.showErrorDlg(theApplet.res.getString("No matching employee was found."));
          invalidLogOnAttempt((String)event);
        } else {
          CMSEmployee beforeEmployee = (CMSEmployee)theOperator.clone();
          accessAppModel = new CMSEmployeeAccessAppModel(new ResourceBundleKey(
              "Employee Access: Mod"), theOperator, beforeEmployee, theOperator, null);
          accessAppModel.setBlueprintName(ReceiptBlueprintInventory.CMSEmployeeAccessMod);
        }
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
    } else if (command.equals("NEW_PASSWORD")) {
      try {
        theOperator.setPassword((String)event);
        enterNewPasswordAgain();
        return;
      } catch (BusinessRuleException bre) {
        theAppMgr.showErrorDlg(bre.getMessage());
      }
    } else if (command.equals("NEW_PASSWORD_AGAIN")) {
      if (!(theOperator.getPassword().equals(event))) {
        theAppMgr.showErrorDlg(
            "The password you entered does not match the new password.  Reset your password again.");
        try {
          theOperator.setPassword("");
        } catch (BusinessRuleException bre) {
          theAppMgr.showExceptionDlg(bre);
        }
      } else {
        isPasswordEntered = true;
        submitEmployee(theOperator);
      }
    } else if (command.equals("PASSWORD")) {
      if (!processPasswordEntry((String)event)) {
        if (passwordWrongCount < 4) {
          enterPassword();
        } else {
          passwordWrongCount = 0;
          enterUserName();
        }
      } else
        isPasswordEntered = true;
    }
    // test attributes one more time
    if (completeAttributes()) {
      theAppMgr.removeStateObject("DUMMY_OPERATOR");
      theBldrMgr.processObject(theApplet, theCommand, theOperator, this);
    }
  }

  /**
   */
  public void cleanup() {
    theOperator = null;
    isPasswordEntered = false;
    passwordWrongCount = 0;
  }

  /**
   * Method responsible to check all aspects of creating this operator
   * @return
   */
  protected boolean completeAttributes() {
    if (initialValue != null && initialValue instanceof String) {
      String userName = (String)initialValue;
      // we only allow the initial value to exist once
      initialValue = null;
      this.EditAreaEvent("USER_NAME", userName);
      return (false);
    }
    if (theOperator == null) {
      enterUserName();
      return (false);
    }
    if (isPasswordRequired && theOperator.getPassword().length() == 0) {
      enterNewPassword();
      return (false);
    }
    // if password is required, make sure it is entered
    if (isPasswordRequired && !isPasswordEntered) {
      enterPassword();
      return (false);
    }
    if (!testCanBeOperator()) {
      cleanup();
      enterUserName();
      return (false);
    }
    return (true);
  }

  /**
   * @return
   */
  protected boolean testCanBeOperator() {
    try {
      theOperator.testCanBeOperator();
      return (true);
    } catch (BusinessRuleException bx) {
      theAppMgr.showErrorDlg(theApplet.res.getString(bx.getMessage()));
      invalidLogOnAttempt(theOperator.getId());
      return (false);
    }
  }

  /**
   * @param id
   */
  protected void invalidLogOnAttempt(String id) {
    CMSRegisterSessionAppModel sessionAppModel = new CMSRegisterSessionAppModel();
    sessionAppModel.setRegister((CMSRegister)theAppMgr.getGlobalObject("REGISTER"));
    sessionAppModel.setSessionDate(new Date());
    sessionAppModel.setStore((CMSStore)theAppMgr.getGlobalObject("STORE"));
    sessionAppModel.setLogonEntered(id);
    sessionAppModel.print(ReceiptBlueprintInventory.CMSInvalidLogonAttempt, theAppMgr);
  }

  /**
   *
   * @param password
   * @return
   */
  protected boolean processPasswordEntry(String password) {
    if (theOperator.getPassword().equals(password) || theOperator.getPassword().length() == 0) {
      return (true);
    } else {
//      System.out.println("Password required: " + theOperator.getPassword());
      theAppMgr.showErrorDlg(theApplet.res.getString("Incorrect Password entered"));
      passwordWrongCount++;
      return (false);
    }
  }

  /**
   */
  protected void enterUserName() {
    theAppMgr.setSingleEditArea(theApplet.res.getString("Enter cashier ID and press 'Enter'.")
        , "USER_NAME", theAppMgr.PASSWORD_MASK);
  }

  /**
   */
  protected void enterPassword() {
    theAppMgr.setSingleEditArea(theApplet.res.getString("Please enter your secret password.")
        , "PASSWORD", theAppMgr.PASSWORD_MASK);
  }

  /**
   */
  protected void enterNewPassword() {
    theAppMgr.setSingleEditArea(theApplet.res.getString(
        "You do not have a password.  You must enter a new password."), "NEW_PASSWORD"
        , theAppMgr.PASSWORD_MASK);
  }

  /**
   * put your documentation comment here
   */
  protected void enterNewPasswordAgain() {
    theAppMgr.setSingleEditArea(theApplet.res.getString(
        "Enter your new password again for verification."), "NEW_PASSWORD_AGAIN"
        , theAppMgr.PASSWORD_MASK);
  }

  /**
   * put your documentation comment here
   * @param emp
   */
  private void submitEmployee(final CMSEmployee emp) {
    if (emp != null) {
      System.out.println("submitting employee with new password: " + emp.getPassword());
      try {
        if (!CMSEmployeeHelper.submit(theAppMgr, emp)) {
          theAppMgr.showErrorDlg("The system was unable to submit the employee." + "  "
              + "Please re-enter later.");
          return;
        }
        accessAppModel.setAfterEmployee(theOperator);
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
}

