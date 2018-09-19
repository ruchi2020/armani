/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.payment.GiftCert;
import com.chelseasystems.cs.payment.*;


/**
 * The purpose of a "Bldr" class is to build a complex object, and pass it back
 * to the GUI.
 * The GUI provides a gift certificate id or control number.  The gift
 * certificate builder takes control until it can return a gift certificate
 * with all properties set (from the server), or null if the gift
 * certificate does not exist.
 * @author John Gray
 * @version 1.0a
 */
public class GiftCertBldr implements IObjectBuilder {

  /** The gift certificate to return */
  private GiftCert gift = null;
  private IObjectBuilderManager theBldrMgr;

  /** The client-side applet */
  private CMSApplet applet;
  private IApplicationManager theAppMgr;

  /** Default constructor */
  public GiftCertBldr() {
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
   * Try to find the gift certificate on the server, using the record id or the
   * control number.
   * @param theCommand the description of what the user typed.
   * @param theEvent what the user typed (the gift certificate id or control
   *        number.
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("ID")) {
      String cert = (String)theEvent;
      cert = cert.replace('.', '*');
      try {
        GiftCert received = CMSRedeemableHelper.findGiftCert(theAppMgr, cert);
        if (received == null) {
          // Got to the server successfully, but the gift certificate
          // is not in the file.
          theAppMgr.showErrorDlg(applet.res.getString("Cannot find gift certificate "
              + "id or control number.  Call help desk, or select another payment type."
              + "  ID entered:") + " " + theEvent);
          // Send a null back (to get out of this class).
          theBldrMgr.processObject(applet, "PAYMENT", null, this);
          return;
        } else {
          // check for remaing balance
          if (received.getRemainingBalance().equals(new ArmCurrency(0.0))) {
            theAppMgr.showErrorDlg(applet.res.getString("Gift certificate has a zero balance."));
            // Send a null back (to get out of this class).
            theBldrMgr.processObject(applet, "PAYMENT", null, this);
            return;
          } else {
            gift = (GiftCert)received;
          }
        }
      } catch (Exception e) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "EditAreaEvent"
            , "Cannot read gift certificate file.", "See Exception", LoggingServices.MAJOR, e);
        // Send a null back (to get out of this class).
        theBldrMgr.processObject(applet, "PAYMENT", null, this);
      }
    } else if (theCommand.equals("GC_AMOUNT")) {
      gift.setAmount((ArmCurrency)theEvent);
    }
    if (completeAttributes()) {
      // The gift certificate object is built, so return it (and control)
      // to the applet.
      theBldrMgr.processObject(applet, "PAYMENT", gift, this);
      gift = null;
    }
  }

  /**
   * Create a new GiftCert object, and call the completeAttributes method to
   * get the search criteria.
   * @param applet the applet calling the builder
   * @param initValue (?)
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    System.out.println("init value = " + initValue);
    if (!theAppMgr.isOnLine()) {
      theAppMgr.showErrorDlg(applet.res.getString(
          "Gift certificates cannot be purchased or redeemed while in offline mode."));
      return;
    }
    gift = new GiftCert((String)initValue);
    this.applet = applet;
    // register for call backs
    completeAttributes();
  }

  /**
   * If the user has not provided either a transaction number or a control
   * number (for the find to use as a key), prompt them to do so.
   */
  private boolean completeAttributes() {
    if ((gift.getId() == null || gift.getId().length() == 0)
        & (gift.getControlNum() == null || gift.getControlNum().length() == 0)) {
      theAppMgr.setSingleEditArea(applet.res.getString(
          "Enter gift certificate ID or control number."), "ID", theAppMgr.UPPER_CASE_MASK);
      return (false);
    }
    try {
      if (gift.getAmount() == null || gift.getAmount().doubleValue() == 0) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
            , gift.getRemainingBalance(), theAppMgr.CURRENCY_MASK);
        return (false);
      }
      if (gift.getAmount().doubleValue() > gift.getRemainingBalance().doubleValue()) {
        theAppMgr.showErrorDlg(applet.res.getString(
            "The amount applied can not be greater than the remaining balance."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
            , gift.getRemainingBalance(), theAppMgr.CURRENCY_MASK);
        return (false);
      }
    } catch (CurrencyException ce) {
      System.err.println("GiftCertBldr.completeAttributes()->" + ce);
      theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
          , theAppMgr.CURRENCY_MASK);
    }
    return (true);
  }
}

