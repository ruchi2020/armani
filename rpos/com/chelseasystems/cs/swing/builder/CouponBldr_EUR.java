/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.payment.GiftCert;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.payment.Coupon;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;
import com.chelseasystems.cs.swing.*;
import java.util.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.dlg.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Enumeration;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.employee.CMSEmployee;


/**
 */
public class CouponBldr_EUR implements IObjectBuilder {
  private Payment coupon = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String command;
  private Payment thePayment;
  private String barCode;
  private String amount;
  private CMSPaymentTransactionAppModel theTxn = null;
  //added for new coupon management PCR
  private CMSStore cmsStore;
  private String storeId;
  private CMSCoupon cmsCoupon;
  private CMSCompositePOSTransaction aTxn;
  

  /** Default constructor */
  public CouponBldr_EUR() {
  }

  /**
   * Initialize the environment.
   * @param theBldrMgr the builder manager
   * @param theAppMgr the application manager
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    
  }

  /**
   * Cleanup before exiting.
   */
  public void cleanup() {}
  

  /**
   * Try to find the CMSCoupon on the server, using the coupon barcode and StoreId.
   * @param theCommand the description of what the user typed.
   * @param theEvent what the user typed (Scanned coupon code.
   * 
   */

  public void EditAreaEvent(String theCommand, Object theEvent) {
  if (theCommand.equals("COUPON")) {
    barCode = (String)theEvent;
    cmsStore = (CMSStore) theAppMgr.getGlobalObject("STORE");
    storeId = cmsStore.getId();
    Date date = new Date();

   // Added by Satin
   // Checks if barcode is null or if the length is less than 8 or if the length is greater than 16
    if ((barCode == null) || (barCode.trim().length() < 8) || (barCode.trim().length() > 16)) {
		theAppMgr.showErrorDlg(applet.res.getString("Barcode length not valid"));
		theAppMgr.setSingleEditArea(applet.res.getString("Scan or Enter Coupon Barcode."), "COUPON");
		return;
		}    

    try {
    	
    	// Added by Satin for New Coupon Management PCR.
    	// Calls findByBarcodeAndStoreId() with arguments theAppMgr , coupon barcode and StoreId
    	thePayment = CMSRedeemableHelper.findByBarcodeAndStoreId(theAppMgr, barCode, storeId);
		
	} catch (Exception e) {
		e.printStackTrace();
	};

	
	
    if (thePayment == null) {
      theAppMgr.showErrorDlg("The Barcode is invalid");
      theBldrMgr.processObject(applet, "PAYMENT", null, this);
      return;
    }

    // Changes made by Satin for New Coupon Management PCR.
    // Checks if the coupon has already been used.
	  else if((((CMSCoupon)thePayment).getCouponUsedFlag()).equals("1   ") || (((CMSCoupon)thePayment).getCouponUsedFlag()).equals("1")){
      theAppMgr.showErrorDlg(applet.res.getString("The coupon has already been used."));
	  theBldrMgr.processObject(applet, "PAYMENT", null, this);
      return;
		  
	  }
    
    // Changes made by Satin for New Coupon Management PCR.
    // Checks for the coupon effective date and Coupon Expiry date.
	  else if ((date.after(((CMSCoupon)thePayment).getExpirateDate())) || (date.before(((CMSCoupon)thePayment).getEffectiveDate()))){
		  theAppMgr.showErrorDlg(applet.res.getString("Please check effective and expirate date."));
		  theBldrMgr.processObject(applet, "PAYMENT", null, this);
      return;
	  }

    
    coupon = (CMSCoupon)thePayment;
    
    //String reasonCode = barCode.substring(7, 9);
    
    CMSStore store = (CMSStore)theAppMgr.getGlobalObject("STORE");
    CMSRegister reg = (CMSRegister)theAppMgr.getGlobalObject("REGISTER");
    CMSEmployee cmsEmployee = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    
    if (coupon instanceof CMSCoupon) {
      ((CMSCoupon)coupon).setStoreId(store.getId());
      ((CMSCoupon)coupon).setRegisterId(reg.getId());
      // SG: Added coupon ID in coupon Description
      ((CMSCoupon)coupon).setDesc(((CMSCoupon)coupon).getCouponCode() );
      //((CMSCoupon)coupon).setDesc(((CMSCoupon)coupon).getCouponCode() /*+applet.res.getString("Type: ")+ ((CMSCoupon)coupon).getType() + applet.res.getString("CouponID: ")+ ((CMSCoupon)coupon).getCouponCode() + applet.res.getString("Code: ") + reasonCode */);
      ((CMSCoupon)coupon).setAmount(((CMSCoupon)thePayment).getAmount());
      ((CMSCoupon)coupon).setScanCode(((CMSCoupon)thePayment).getCouponCode());
      
      //((CMSCoupon)coupon).setExpirateDate(((CMSCoupon)thePayment).getExpirateDate());
      //((CMSCoupon)coupon).setPromotionCode(reasonCode);
      //((CMSCoupon)coupon).setType(barCode.substring(9, 12));
      //((CMSCoupon)coupon).setAmount(val);
            
    }
    
    theBldrMgr.processObject(applet, "PAYMENT", coupon, this);
    return;
  }
}
  

  public void build(String Command, CMSApplet applet, Object initValue) {
	    resetAttributes();
	    this.applet = applet;
	    this.command = Command;
	    theAppMgr.setSingleEditArea(applet.res.getString("Scan or Enter Coupon Barcode."), "COUPON");
	    theAppMgr.setEditAreaFocus();
	  }

	  /**
	   * put your documentation comment here
	   */
	  private void resetAttributes() {
	    thePayment = null;
	  }
  


}

/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


