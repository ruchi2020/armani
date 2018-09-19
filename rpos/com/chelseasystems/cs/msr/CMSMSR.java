/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 06-15-2005 | Vikram    | 144       | Modified to support MSR to other builders    |
 +------+------------+-----------+-----------+----------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */


package com.chelseasystems.cs.msr;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.swing.builder.CreditCardBldr;
import com.chelseasystems.cs.swing.builder.CustomerCreditCardBldr;
import com.chelseasystems.cs.swing.builder.ET1000CreditCardBldr;
import com.chelseasystems.cr.appmgr.IObjectBuilder;
import com.chelseasystems.cs.swing.builder.CMSBoardingPassBldr;
import com.chelseasystems.cs.swing.builder.DueBillBldr;
import com.chelseasystems.cs.swing.builder.DueBillIssueBldr;
import com.chelseasystems.cs.swing.builder.StoreValueCardBldr;
import com.chelseasystems.cs.swing.builder.IssueRewardCardBldr;
import com.chelseasystems.cs.swing.builder.LoyaltyBldr;
import com.chelseasystems.cs.swing.builder.LoyaltyItemBldr;
import com.chelseasystems.cs.swing.builder.RewardDiscountBldr;
import com.chelseasystems.cs.swing.builder.RedeemableInquiryBldr;
import com.chelseasystems.cs.util.Version;


/**
 * An MSR object will be created the first time the static method is called. Under JPOS, the MSR will be opened
 * and claimed.  It will also be enabled.  To receive events, call activate. Release removes event handling
 * participation.
 */
public abstract class CMSMSR {
  private static ConfigMgr configMgr = new ConfigMgr("JPOS_peripherals.cfg");
  private static String msrType = configMgr.getString("MSR_DEVICE_TYPE");
  private static String msrLogicalName = configMgr.getString("MSR_LOGICAL_NAME");
  private IObjectBuilder bldr;

  /**
   * This method will create a new MSR object that is activated!   It has to create a new object each time that
   * you want to use it.
   * @return
   */
  public static CMSMSR getInstance() {
    if (msrType.equals("NON_JPOS"))
      return NonJavaPOSMSR.getNonJavaPOSMSRInstance();
    else if (msrType.equals("GENERIC_JPOS"))
      return GenericJPOSMSR.getGenericJPOSMSRInstance();
    else if (msrType.equals("INGENICO_JPOS"))
      return ET1000JPOSMSR.getET1000JPOSMSRInstance();
    else
      return null;
  }

  /**
   * Made this a static call.  Calling getInstance turns the MSR on.
   * @return
   */
  public static String getMSRType() {
    return msrType;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static String getMSRLogicalName() {
    return msrLogicalName;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public IObjectBuilder getCreditCardBldr() {
    return this.bldr;
  }

  /**
   * put your documentation comment here
   * @param bldr
   */
  public void registerCreditCardBuilder(IObjectBuilder bldr) {
    this.bldr = bldr;
    if (msrType.equals("INGENICO_JPOS"))
    	//MSR needs to stop working to make it PCI complaince
      //((ET1000CreditCardBldr)bldr).setCMSMSR(this);
    //else if (bldr instanceof CreditCardBldr)
      //((CreditCardBldr)bldr).setCMSMSR(this);
    //else if (bldr instanceof CustomerCreditCardBldr)
      //((CustomerCreditCardBldr)bldr).setCMSMSR(this);
    	//changes ends
     if (bldr instanceof DueBillBldr)
      ((DueBillBldr)bldr).setCMSMSR(this);
    else if (bldr instanceof DueBillIssueBldr)
      ((DueBillIssueBldr)bldr).setCMSMSR(this);
    else if (bldr instanceof StoreValueCardBldr)
      ((StoreValueCardBldr)bldr).setCMSMSR(this);
    else if (bldr instanceof IssueRewardCardBldr)
      ((IssueRewardCardBldr)bldr).setCMSMSR(this);
    else if (bldr instanceof LoyaltyBldr)
      ((LoyaltyBldr)bldr).setCMSMSR(this);
    else if (bldr instanceof LoyaltyItemBldr)
      ((LoyaltyItemBldr)bldr).setCMSMSR(this);
    else if (bldr instanceof RewardDiscountBldr)
      ((RewardDiscountBldr)bldr).setCMSMSR(this);
    else if (bldr instanceof RedeemableInquiryBldr)
      ((RedeemableInquiryBldr)bldr).setCMSMSR(this);
    //added by shushma for duty free management PCR
    if("EUR".equalsIgnoreCase(Version.CURRENT_REGION)){
     if (bldr instanceof CMSBoardingPassBldr)
        ((CMSBoardingPassBldr)bldr).setCMSMSR(this);
    }
  }

  /**
   * This method is called by the builder and should disable event handling.
   */
  public abstract void release();

  /**
   * This method should enable event handling.
   */
  public abstract void activate();
}
