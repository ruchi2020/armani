/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 07-08-2005 |  Megha    | N/A       | Employee Sale : For terminated employee no Discount|
 --------------------------------------------------------------------------------------------------
 | 2    | 05-05-2005|  Khyati    | N/A        |Added set employeeId for Employee sale             |
 --------------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.builder;


import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.discount.*;
import com.chelseasystems.cr.discount.DiscountMgr;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.discount.*;
import java.util.ResourceBundle;
import com.chelseasystems.cr.util.ResourceManager;


/**
 * Builder to construct an 'Employee' <code>Discount</code> object.
 */
public class EmployeeDiscountBldr implements IObjectBuilder {
  private CMSEmployeeDiscount discount = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private ConfigMgr config = null;
  private CMSEmployee theOpr;
  private CMSEmployee emp;
  private CMSCompositePOSTransaction theTxn;
  private String employeeId = null;

  /**
   */
  public EmployeeDiscountBldr() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    config = new ConfigMgr(System.getProperty("USER_CONFIG"));
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
  }

  /**
   */
  public void cleanup() {
    discount = null;
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("EMPLOYEE")) {
      try {
        String sValue = (String)theEvent;
        if (sValue.equals(((CMSEmployee)theOpr).getExternalID())) {
          theAppMgr.showErrorDlg(applet.res.getString("Employee cant be same as operator"));
          employeeId = null;
          completeAttributes();
          return;
          //                theAppMgr.setSingleEditArea(applet.res.getString("Enter employee ID"),"EMPLOYEE_SALE", theAppMgr.NO_MASK);
        }
        CMSEmployee emp = CMSEmployeeHelper.findByExternalId(theAppMgr, (String)theEvent);
        // __Tim: Fix for exception in Offline mode
        if (emp == null) {
          theAppMgr.showErrorDlg(applet.res.getString("Cannot find employee."));
          completeAttributes();
          employeeId = null;
          return;
        } else {
          try {
            if (emp.isEmploymentStatusTerminated()) {
              theAppMgr.showErrorDlg(CMSApplet.res.getString("Cannot give an employee discount to a terminated employee."));
              completeAttributes();
            } else {
              //MP: Check if the employee status is not terminated. If terminated prompt for valid employee ID
              if (emp.isEmploymentStatusTerminated()) {
                theAppMgr.showErrorDlg(CMSApplet.res.getString("Cannot give an employee discount to a terminated employee."));
                completeAttributes();
              } else {
                discount.setEmployee(emp);
                employeeId = (String)theEvent;
                discount.setIsLineItemDiscount(true);
                //ks: Set employee Id to the composite Transaction
                theTxn.setEmployeeId(employeeId);
                theTxn.setEmployee(emp);
              }
            }
          } catch (BusinessRuleException bre) {
            theAppMgr.showErrorDlg(applet.res.getString("In Business Rule Exception "
                + bre.getMessage()));
          }
        }
      } catch (Exception ex) {
        System.out.println("Exception BuildEmplDiscount: " + ex);
      }
    } // end if
    if (completeAttributes()) {
      theTxn.setEmployeeSale(true);
      if (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") != null) {
        CMSEmployeeDiscount oldDisc = (CMSEmployeeDiscount)theAppMgr.getStateObject(
            "ARM_EMPLOYEE_DISCOUNT");
        theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
        try {
          theTxn.removeDiscount(oldDisc);
          POSLineItem[] posLineItems = (POSLineItem[])theTxn.getLineItemsArray();
          if (posLineItems != null) {
            for (int i = 0; i < posLineItems.length; i++) {
              if (posLineItems[i] instanceof CMSSaleLineItem) {
                posLineItems[i].removeDiscount(oldDisc);
              } else if (posLineItems[i] instanceof CMSReturnLineItem
                  && ((CMSReturnLineItem)posLineItems[i]).isMiscReturn()) {
                posLineItems[i].removeDiscount(oldDisc);
              } else
                continue;
            }
          }
        } catch (Exception ex) {
          System.out.println(ex);
        }
      }
      theAppMgr.addStateObject("ARM_EMPLOYEE_DISCOUNT", discount);
      try {
        POSLineItem[] posLineItems = (POSLineItem[])theTxn.getLineItemsArray();
        if (posLineItems != null) {
          for (int i = 0; i < posLineItems.length; i++) {
            if (posLineItems[i] instanceof CMSSaleLineItem) {
              posLineItems[i].addDiscount(discount);
            } else if (posLineItems[i] instanceof CMSReturnLineItem
                && ((CMSReturnLineItem)posLineItems[i]).isMiscReturn()) {
              posLineItems[i].addDiscount(discount);
            } else
              continue;
          }
        }
      } catch (Exception e) {
        System.out.println(e);
      }
      theBldrMgr.processObject(applet, "DISCOUNT", discount, this);
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    discount = (CMSEmployeeDiscount)CMSDiscountMgr.createDiscount("EMPLOYEE");
    //sonali:Added for setting DiscountCode=EMPLOYEE_DISCOUNT_CODE
 try{
	String code = null ;
	if(discount!=null){
		//Anjana added 05-22-2017 to save discount code for employee discounts
		ConfigMgr configm = new ConfigMgr("ArmaniCommon.cfg");
		code = configm.getString(configm.getString("EMPLOYEE_DISCOUNT")+".CODE");
		System.out.println("Discount code   :"+code);
		if(code==null){
			code = CMSDiscountMgr.getEmployeeReasonCode();
			discount.setDiscountCode(code);
		}else
		{
			discount.setDiscountCode(code);
		}
	}
 }
         catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    this.applet = applet;
    // register for call backs
    if (completeAttributes()) {
      if (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") != null)
        theAppMgr.removeStateObject("ARM_EMPLOYEE_DISCOUNT");
      theAppMgr.addStateObject("ARM_EMPLOYEE_DISCOUNT", discount);
      theBldrMgr.processObject(applet, "DISCOUNT", discount, this);
    }
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    if (discount.getEmployee() == null || employeeId == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter employee ID."), "EMPLOYEE"
          , theAppMgr.NO_MASK);
      return (false);
    }
    theTxn.setEmployeeSale(true);
    return (true);
  }
}

