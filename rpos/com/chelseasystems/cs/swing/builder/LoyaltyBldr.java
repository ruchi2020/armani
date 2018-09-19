/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 5    | 08-16-2005 | Vikram    | 878       | Changes to support new business rule to check|
 |      |            |           |           | for validity of Customer Loyalty Enrollment. |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 07-06-2005 | Megha     |           | Modified to support MSR + Set Account        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-15-2005 | Vikram    | 144       | Modified to support MSR                      |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 05-31-2005 | Megha     | 83       |Loyalty/Enroll: Number format Exception        |
 |      |            |           |             occurs when alpha-numeric value added.       |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.swing.builder;

import java.util.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.employee.CMSEmployee;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.util.IsDigit;
import javax.swing.SwingUtilities;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.store.CMSStore;


/**
 */
public class LoyaltyBldr implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String accountNum = null;
  private Loyalty theLoyalty = new Loyalty();
  private CMSMSR cmsMSR = null;

  /**
   */
  public LoyaltyBldr() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    this.accountNum = "";
  }

  /**
   */
  public void cleanup() {
    cmsMSR.release();
    this.accountNum = "";
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("LOYALTY_ENROLL")) {
      processSwipe((String)theEvent);
      //return;
    }
    if (theCommand.equals("ACCOUNT")) {
      processAccount((String)theEvent);
    }
    if (completeAttributes()) {
      theBldrMgr.processObject(applet, "ENROLL_LOYALTY", theLoyalty, this);
      theLoyalty = null;
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    resetAttributes();
    this.applet = applet;
    theAppMgr.setSingleEditArea(applet.res.getString(
        "Swipe loyalty card or press 'Enter' for manual entry."), "LOYALTY_ENROLL");
    theAppMgr.setEditAreaFocus();
    System.out.println("loyalty card builder getting instance of CMSMSR...");
    CMSMSR cmsMSR = CMSMSR.getInstance();
    cmsMSR.registerCreditCardBuilder(this);
    cmsMSR.activate();
  }

  /**
   * @param input
   */
  public void processSwipe(String input) {
    if (input == null || input.length() == 0
        || (getLoyaltyCardInfo(input) && processAccount(accountNum))) {
      completeAttributes();
      return;
    }
    //    if (getLoyaltyCardInfo(accountNum)) {
    //
    //      // Check to see if the loyalty Card number already exists
    //      try {
    //
    //        if (LoyaltyHelper.getLoyalty(theAppMgr, accountNum) != null) {
    //          return;
    //        }
    //
    //        else {
    //          theLoyalty = LoyaltyBinRangeUtil.allocLoyaltyCard(accountNum);
    //          if (theLoyalty != null) {
    //            populateLoyaltyCard();
    //            theLoyalty.setLoyaltyNumber(accountNum);
    //            completeAttributes();
    //            return;
    //          }
    //          else {
    //            theAppMgr.showErrorDlg(applet.res.getString(
    //                "The account number is invalid."));
    //          }
    //        }
    //      }
    //      catch (Exception e) {
    //        e.printStackTrace();
    //      }
    //
    //    }
    return;
  }

  /**
   * put your documentation comment here
   * @param theEvent
   * @return
   */
  private boolean processAccount(String theEvent) {
    accountNum = (String)theEvent;
    boolean reply = true;
    IsDigit digitCheck = new IsDigit();
    if (!digitCheck.isDigit(accountNum)) {
      theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
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
              theAppMgr.setSingleEditArea(applet.res.getString(
                  "Swipe loyalty card or press 'Enter' for manual entry."), "LOYALTY_ENROLL");
            }
          });
        }
      });
      return false;
    }
    // Check to see if the loyaltyNum already exists.
    try {
      if (LoyaltyHelper.getLoyalty(theAppMgr, accountNum) != null) {
        theAppMgr.showErrorDlg(applet.res.getString("The LoyaltyCard already exists"));
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
                theAppMgr.setSingleEditArea(applet.res.getString(
                    "Swipe loyalty card or press 'Enter' for manual entry."), "LOYALTY_ENROLL");
              }
            });
          }
        });
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    theLoyalty = LoyaltyBinRangeUtil.allocLoyaltyCard(accountNum);
    // if payment is still null (manual account is bad then
    // return null and start over
    if (theLoyalty == null) {
      theAppMgr.showErrorDlg(applet.res.getString("The account number is invalid."));
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
              theAppMgr.setSingleEditArea(applet.res.getString(
                  "Swipe loyalty card or press 'Enter' for manual entry."), "LOYALTY_ENROLL");
            }
          });
        }
      });
      return false;
    }
    theLoyalty.setLoyaltyNumber(accountNum);
    populateLoyaltyCard();
    return true;
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    // didn't swipe, get account number for payment type
    if (theLoyalty == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return (false);
    }
    return isValidForEnroll();
  }

  /**
   */
  private void resetAttributes() {
    theLoyalty = null;
    accountNum = null;
  }

  /**
   */
  private void populateLoyaltyCard() {
    CMSEmployee cmsEmployee = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    Date currDate = new Date();
    try {
      SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      df.setLenient(false);
      String date = df.format(currDate);
      currDate = df.parse(date);
    } catch (Exception e) {}
    //lc.setLoyaltyNumber(loyaltyNumber);
    theLoyalty.setStoreType(LoyaltyBinRangeUtil.CardType);
    theLoyalty.setIssuedBy(cmsEmployee.getExternalID());
    theLoyalty.setIssueDate(currDate);
    theLoyalty.setCurrBalance(0);
    theLoyalty.setLifeTimeBalance(0);
    theLoyalty.setStatus(true);
    //loyaltyNew.setCustomerID(cmsCust.getId());
    theLoyalty.setCustomer((CMSCustomer)theAppMgr.getStateObject("CUSTOMER_LOOKUP"));
    return;
  }

  /**
   * put your documentation comment here
   * @param accountNum
   */
  public void setAccountNum(String accountNum) {
    this.accountNum = new IsDigit().filterToGetDigits(accountNum);
  }

  /**
   * put your documentation comment here
   * @param inputStr
   * @return
   */
  public boolean getLoyaltyCardInfo(String inputStr) {
    if (this.cmsMSR instanceof NonJavaPOSMSR) {
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
        theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe loyalty card or press 'Enter' for manual entry."), "LOYALTY_ENROLL");
        theAppMgr.setEditAreaFocus();
        theAppMgr.showErrorDlg(applet.res.getString("Error reading data from MSR."));
        return (false);
      }
    }
    return (true);
  }

  /**
   * @return
   */
  public String getAccountNum() {
    return (accountNum);
  }

  /**
   * put your documentation comment here
   * @param cmsMSR
   */
  public void setCMSMSR(CMSMSR cmsMSR) {
    this.cmsMSR = cmsMSR;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isValidForEnroll() {
    try {
      theLoyalty.checkIfValidForEnroll(theAppMgr.getStateObject("LOYALTYFORREISSUE") != null
          , LoyaltyHelper.getCustomerLoyalties(theAppMgr, theLoyalty.getCustomer().getId()));
      if (!isValidInStore()) {
        theAppMgr.showErrorDlg(applet.res.getString(
            "This loyalty card type is not valid in this store."));
        return false;
      }
    } catch (BusinessRuleException ex) {
      theAppMgr.showErrorDlg(ex.getMessage());
      return false;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @return
   */
  private boolean isValidInStore() {
    ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
    String brandID = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getBrandID();
    String loyaltyStrBrandID = loyaltyConfigMgr.getString(LoyaltyBinRangeUtil.CardType + ".TYPE");
    if (loyaltyStrBrandID != null && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim()))
      return true;
    //else
    return false;
  }
}

