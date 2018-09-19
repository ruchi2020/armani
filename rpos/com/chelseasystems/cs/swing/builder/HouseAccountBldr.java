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
import com.chelseasystems.cr.payment.StoreValueCard;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.util.TransactionUtil;


/**
 * The purpose of a "Bldr" class is to build a complex object, and pass it back
 * to the GUI.
 * The GUI provides a store value card control number.  The store value
 * card builder takes control until it can return a store value card
 * with all properties set (from the server), or null if the store value
 * card does not exist.
 */
public class HouseAccountBldr implements IObjectBuilder {

  /** The gift certificate to return */
  private HouseAccount houseAccount = null;
  private IObjectBuilderManager theBldrMgr;

  /** The client-side applet */
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  //ks: flag to check if offline
  private boolean isOffline = false;
  private String manualAuthCode = null;
  private ArmCurrency amt = new ArmCurrency("0.0");

  /** Default constructor */
  public HouseAccountBldr() {
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
      String card = (String)theEvent;
      //KS: Not needed. Its not a card
      //         card = card.replace('.', '*');
      try {
        //ks: find if online
        if (!isOffline) {
          Redeemable received = CMSRedeemableHelper.findHouseAccount(theAppMgr, card);
          if (received != null) {
            theAppMgr.addStateObject("THE_EVENT", "SUCCESS");
          } else {
            theAppMgr.addStateObject("THE_EVENT", "FAILURE");
          }
          if (received == null) {
            //Incorporated by Anand to accomodate CustlistApplet related changes
            // Got to the server successfully, but the store value card
            // is not in the file.
            theAppMgr.showErrorDlg(applet.res.getString("Cannot find the entered House Account. "
                + "Call help desk, or select another payment type." + " House Account ID entered:")
                + " " + theEvent);
            // Send a null back (to get out of this class).
            theBldrMgr.processObject(applet, "PAYMENT", null, this);
            return;
          } else {
            HouseAccount receivedCard = (HouseAccount)received;
            // MP: Commented for new House Bld Flow, now we don't consider the remaning Bal.
            // check for remaing balance if isOnline
            //            if (receivedCard.getRemainingBalance().equals(new ArmCurrency(0.0))) {
            //              theAppMgr.showErrorDlg(applet.res.getString(
            //                  "House Account has a zero balance."));
            //              // Send a null back (to get out of this class).
            //              theBldrMgr.processObject(applet, "PAYMENT", null, this);
            //              return;
            //            }
            if (!receivedCard.getStatus()) {
              theAppMgr.showErrorDlg(applet.res.getString("House Account is inactive"));
              // Send a null back (to get out of this class).
              theBldrMgr.processObject(applet, "PAYMENT", null, this);
              return;
            } else {
              houseAccount = (HouseAccount)receivedCard;
            }
          }
        } else { //end isOnline
          houseAccount.setId((String)theEvent);
        }
      } catch (Exception e) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "EditAreaEvent"
            , "Cannot read House Account file.", "See Exception", LoggingServices.MAJOR, e);
        // Send a null back (to get out of this class).
        theBldrMgr.processObject(applet, "PAYMENT", null, this);
      }
    } else if (theCommand.equals("GC_AMOUNT")) {
      //issue # 711
      /*Currency enteredAmt = (ArmCurrency) theEvent;
       try {
       if (amt.absoluteValue().lessThan(enteredAmt)) {
       theAppMgr.showErrorDlg(
       "Payments greater than amount due are not allowed.");
       theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."),
       "GC_AMOUNT", amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
       //theBldrMgr.processObject(applet, "PAYMENT", null, this);
       return;
       }
       }
       catch (CurrencyException e) {
       }*/
      //houseAccount.setAmount( (ArmCurrency) theEvent);
      String paymentTypeView = houseAccount.getGUIPaymentName();
      String paymentType = houseAccount.getTransactionPaymentName();
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
          , (ArmCurrency)theEvent))
        houseAccount.setAmount((ArmCurrency)theEvent);
    } else if (theCommand.equals("AUTH_CODE")) { //ks AUTH_CODE
      if (theEvent == null || ((String)theEvent).length() == 0) {
        theAppMgr.showErrorDlg(applet.res.getString("Auth Code cannot be blank."));
        return;
      } else {
        manualAuthCode = (String)theEvent;
      }
    }
    if (completeAttributes()) {
      // The store value card object is built, so return it (and control)
      // to the applet.
      theBldrMgr.processObject(applet, "PAYMENT", houseAccount, this);
      houseAccount = null;
    }
  }

  /**
   * Create a new HouseAccount object, and call the completeAttributes method to
   * get the search criteria.
   * @param applet the applet calling the builder
   * @param initValue (?)
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    if (!theAppMgr.isOnLine()) {
      //ks: House account will be handled offline also
      //         theAppMgr.showErrorDlg(applet.res.getString("Store value card are not valid while in offline mode."));
      //         return;
      isOffline = true;
    }
    houseAccount = new HouseAccount((String)initValue);
    PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
    if (theTxn instanceof CMSCompositePOSTransaction
        && ((CMSCompositePOSTransaction)theTxn).getCustomer() != null) {
      houseAccount.setCustomerId(((CMSCompositePOSTransaction)theTxn).getCustomer().getId());
      houseAccount.setFirstName(((CMSCompositePOSTransaction)theTxn).getCustomer().getFirstName());
      houseAccount.setLastName(((CMSCompositePOSTransaction)theTxn).getCustomer().getLastName());
    }
    this.applet = applet;
    // register for call backs
    completeAttributes();
  }

  /**
   * If the user has not provided either a transaction number or a control
   * number (for the find to use as a key), prompt them to do so.
   */
  private boolean completeAttributes() {
    PaymentTransaction txn = (PaymentTransaction)CMSApplet.theAppMgr.getStateObject("TXN_POS");
    // MP: Gets the composite total amt Due.
    CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)txn.getAppModel(
        CMSAppModelFactory.getInstance(), theAppMgr);
    try {
      amt = appModel.getCompositeTotalAmountDue().subtract(txn.getTotalPaymentAmount());
    } catch (Exception e) {}
    if ((houseAccount.getId() == null || houseAccount.getId().length() == 0)
        & (houseAccount.getControlNum() == null || houseAccount.getControlNum().length() == 0)) {
      String defaultHouseAccount = "";
      if (txn instanceof CMSCompositePOSTransaction
          && ((CMSCompositePOSTransaction)txn).getCustomer() != null) {
        if (!isOffline) {
          try {
            Redeemable[] received = CMSRedeemableHelper.findHouseAccountByCustomerId(theAppMgr
                , ((CMSCompositePOSTransaction)txn).getCustomer().getId());
            if (received != null && received.length > 0) {
              // Found the House account
              defaultHouseAccount = received[0].getId();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      theAppMgr.setSingleEditArea(applet.res.getString("Enter account number."), "ID"
          , defaultHouseAccount, theAppMgr.UPPER_CASE_MASK);
      return false;
    }
    if (houseAccount.getAmount() == null || houseAccount.getAmount().doubleValue() == 0) {
      // MP: Changed remaining Bal to amt Due.
      theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT"
          , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
      return false;
    }
    // MP: Commented for accepting amt even if it is greater than the Remaining Balance.
    //         if (!isOffline && (houseAccount.getAmount().doubleValue() > houseAccount.getRemainingBalance().doubleValue())) {
    //                 theAppMgr.showErrorDlg(applet.res.getString("The amount applied can not be greater than the remaining balance."));
    //            theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "GC_AMOUNT", amtDue, theAppMgr.CURRENCY_MASK);
    //            return  false;
    //         }
    if (isOffline && manualAuthCode == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter Manual Auth Code."), "AUTH_CODE");
      return false;
    }
    return true;
  }
}

