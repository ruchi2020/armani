/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

/*
 History:
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | Ver# | Date       | By        | Defect # | Description                                                    |
 +------+------------+-----------+----------+----------------------------------------------------------------+
 | 1    | 07-08-2005 | Megha     | N/A      | New Class for redeemable                                       |
 +------+------------+-----------+----------+----------------------------------------------------------------+
 */
import java.util.*;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.msr.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.loyalty.Loyalty;
import com.chelseasystems.cs.employee.CMSEmployee;

import java.text.SimpleDateFormat;

import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.loyalty.LoyaltyHelper;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cs.util.Version;

import javax.swing.SwingUtilities;


/**
 */
public class RedeemableInquiryBldr implements IObjectBuilder {
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String accountNum = null;
  private String giftCardID = null; // added by Himani for GC Balance Inquiry
  private Redeemable cmsRedeemable = null;
  private CMSMSR cmsMSR = null;
  private String gcCommand=null;
  private ConfigMgr config;
  private String fipay_gc_flag;
  //added by Himani for GC Balance Inquiry

  /**
   */
  public RedeemableInquiryBldr() {
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    this.accountNum = "";
    //Added to identify Gift Card AJB intergation in US region
    String fileName = "store_custom.cfg";
	config = new ConfigMgr(fileName);
	fipay_gc_flag = config.getString("FIPAY_GIFTCARD_INTEGRATION");
	if(fipay_gc_flag == null){
		fipay_gc_flag = "N";
	}
    //Ends here
  }

  /**
   */
  public void cleanup() {
  //Added by Anjana 02/24 to fix production issue:- null pointer for cmsMSR object  while doing RedeemableInquiry
	  if(cmsMSR!=null)
    cmsMSR.release();
    this.accountNum = "";
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
	if (theCommand.equals("GIFT_CARD")) { // added by Himani for GC Balance Inquiry
		gcCommand=theCommand;
		processSwipe((String)theEvent);  
	}
	
    if (theCommand.equals("REDEEM_ITEM")) {
      processSwipe((String)theEvent);
    }
    if (theCommand.equals("ACCOUNT")) {
      processAccount((String)theEvent);
    }
    if (completeAttributes()) {
      CMSEmployee theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
      if(gcCommand!=null && gcCommand.equals("GIFT_CARD"))   // added by Himani for GC Balance Inquiry
      {
    	  //theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
    	  theBldrMgr.processObject(applet, "GIFTCARD_INQUIRY", cmsRedeemable, this);
      }
      else
      {
    	  theAppMgr.showMenu(MenuConst.PREV_PRINT, theOpr);
    	  theBldrMgr.processObject(applet, "REDEEMABLE_INQUIRY", cmsRedeemable, this);
      }
      cmsRedeemable = null;
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
    if(Command.equals("GIFTCARD_INQUIRY")) //added by Himani for GC Balance Inquiry
    {
    	theAppMgr.setSingleEditArea(applet.res.getString(
    	        "Swipe Gift Card or press 'Enter' for manual entry."), "GIFT_CARD");
    	theAppMgr.setEditAreaFocus();
    }
    else
    {
    	//Start: Added by Himani for redeemable buy back fipay integration
    	if(initValue==null || initValue=="")
    	{
    		if (Version.CURRENT_REGION.equalsIgnoreCase("US") && fipay_gc_flag!=null && fipay_gc_flag.toUpperCase().equals("Y"))
    		{
    			theAppMgr.showErrorDlg(applet.res.getString("The Redeemable Inquiry data may or may not be correct due to AJB integration inplace."));	
    		}
    		
    		theAppMgr.setSingleEditArea(applet.res.getString(
    				"Swipe redeemable card or press 'Enter' for manual entry."), "REDEEM_ITEM");
    		theAppMgr.setEditAreaFocus();
    		System.out.println("Redeemable card builder getting instance of CMSMSR...");
    		CMSMSR cmsMSR = CMSMSR.getInstance();
    		cmsMSR.registerCreditCardBuilder(this);
    		cmsMSR.activate();
    	}
    	else
    	{
    		try {
				cmsRedeemable = CMSRedeemableHelper.findRedeemable(theAppMgr, initValue.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	      //if (cmsRedeemable == null) 
    	        //theAppMgr.showErrorDlg(applet.res.getString("The Redeemable Card doesn't exist"));
    	      theBldrMgr.processObject(applet, "REDEEMABLE_INQUIRY", cmsRedeemable, this);
    	}
    	
    	//End: Added by Himani for redeemable buy back fipay integration
    }
    
  }

  /**
   * @param input
   */
  public void processSwipe(String input) {
    if (input == null || input.length() == 0
        || (getRedeemableCardInfo(input) && processAccount(accountNum))) {
      completeAttributes();
      return;
    }
    return;
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    // didn't swipe, get account number for payment type
    if (cmsRedeemable == null) {
    	if(gcCommand!=null && gcCommand.equals("GIFT_CARD"))
    		theAppMgr.setSingleEditArea(applet.res.getString("Enter Gift Card ID."), "ACCOUNT");
    	else
    		theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ACCOUNT");
      return (false);
    }
    return (true);
  }

  /**
   */
  private void resetAttributes() {
    cmsRedeemable = null;
    accountNum = null;
    giftCardID=null; 
  }

  /**
   * Fix for 1847 Redeemable Lookup not working for house accounts removed the didgitCheck code.
   * @param theEvent
   * @return
   */
  private boolean processAccount(String theEvent) {
    CMSEmployee theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    accountNum = (String)(theEvent);
    boolean reply = true;
    // Check to see if the loyaltyNum already exists.
    try {
    	// Start: added by Himani for GC Balance Inquiry
    	
    	if(gcCommand!=null && gcCommand.equals("GIFT_CARD"))
    	{
    		cmsRedeemable = CMSRedeemableHelper.findStoreValueCard(theAppMgr, accountNum);
  	      if (cmsRedeemable == null) {
  	        theAppMgr.showErrorDlg(applet.res.getString("The Gift Card doesn't exist"));
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
  	                    "Swipe Gift Card or press 'Enter' for manual entry."), "GIFT_CARD");
  	              }
  	            });
  	          }
  	        });
  	        return false;
  	      }
    	}
    	//End: added by Himani for GC Balance Inquiry
    	else
    	{
      cmsRedeemable = CMSRedeemableHelper.findRedeemable(theAppMgr, accountNum);
      if (cmsRedeemable == null) {
        theAppMgr.showErrorDlg(applet.res.getString("The Redeemable Card doesn't exist"));
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
                    "Swipe Redeemable card or press 'Enter' for manual entry."), "REDEEM_ITEM");
              }
            });
          }
        });
        return false;
      }
    }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
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
  public boolean getRedeemableCardInfo(String inputStr) {
    if (this.cmsMSR instanceof NonJavaPOSMSR) {
      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
        theAppMgr.setSingleEditArea(applet.res.getString(
            "Swipe redeemable card or press 'Enter' for manual entry."), "REDEEM_ITEM");
        theAppMgr.setEditAreaFocus();
        theAppMgr.showErrorDlg(applet.res.getString("Error reading data from MSR."));
        return (false);
      }
    }
  //Mayuri Edhara 07-18-2016: added the else loop to add the error dialog incase the cashier enter the card instead of swipe.
    else if(!(this.cmsMSR instanceof NonJavaPOSMSR) && ("US".equalsIgnoreCase(Version.CURRENT_REGION))){

    	theAppMgr.showErrorDlg(applet.res.getString("Please try again if you did not press the ENTER key before manually keying in the gift card number."));
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                    theAppMgr.setSingleEditArea(applet.res.getString(
                        "Swipe Redeemable card or press 'Enter' for manual entry."), "REDEEM_ITEM");
                  }
                });
              }
            });
            return (false);   	
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
}

