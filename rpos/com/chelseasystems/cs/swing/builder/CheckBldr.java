/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import jpos.*;
import jpos.events.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cr.zipcode.ValidStates;
import com.chelseasystems.cs.payment.CMSBankCheck;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.CreditAuthUtil;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.util.TransactionUtil;


/**
 *  Creates a payment check object from a MICR read or a manual keyboard entry.
 *  @author Angela Tritter
 *  @version 1.0a
 */
public class CheckBldr implements IObjectBuilder, MICRConst, JposConst, DataListener {
  private Check theCheck = null;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  //   private boolean businessCheck = false;
  //   private boolean isTypeEntered = false;

  /** The static reference to the receipt printer **/
  static private POSPrinter posPrinter = CMSPrinter.getInstance();

  /** The static reference to the MICR on the printer **/
  static private MICR posMICR = CMSMICR.getInstance();

  /** The configuration information from the receipt.cfg file needed by the printer **/
  private ReceiptConfigInfo receiptInfo = ReceiptConfigInfo.getInstance();

  /** A flag to determine if reprint after an error has occured **/
  private boolean responseToContinue = true;
  private CMSPaymentTransactionAppModel txn = null;

  /**
   */
  public CheckBldr() {
    ConfigMgr cfg = new ConfigMgr("JPOS_peripherals.cfg");
    receiptInfo = CMSPrinter.getConfigInfo(cfg);
    if (null == receiptInfo.getPrinterName()) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "CheckBldr()"
          , "Cannot get instance of Receipt printer & MICR reader for the Check Builder."
          , "Make sure JPOS_peripherals.cfg contains an entry"
          + " with key LOGICAL_PRINTER_NAME and LOGICAL_MICR_NAME that matches"
          + " a logical printer name defined in JavaPOS.inf.", LoggingServices.MAJOR);
    }
  }

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
    responseToContinue = true;
    //      isTypeEntered = false;
    posMICR.addDataListener(this);
  }

  /**
   */
  public void cleanup() {
    try {
      //System.out.println("CheckBldr CLEANUP BEING CALLED");
      theCheck = null;
      //         businessCheck = false;
      //         isTypeEntered = false;
      responseToContinue = true;
      // Just in case a dataOccured event did not happen this will release the MICR and Printer.
      // Always remove the listener first in case of exceptions.
      posMICR.removeDataListener(this);
      posMICR.setDataEventEnabled(false);
      posPrinter.clearOutput();
      CMSPrinter.release();
      posMICR.clearInput();
      CMSMICR.release();
    } catch (Exception ex) {
      System.err.println("Exception cleanup()->" + ex);
    }
  }

  /**
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String Command, CMSApplet applet, Object initValue) {
    theCheck = null;
    this.applet = applet;
    completeAttributes();
  }

  /**
   * @return
   */
  private boolean completeAttributes() {
    try {
      // In re-entry mode, do not prompt for MICR.
      PaymentTransaction aTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      if (aTxn.getHandWrittenTicketNumber().length() > 0) {
        responseToContinue = false;
      }
      // If MICR is enabled...
      //Vivek Mishra : Added the code to disable MICR read as requested by Jason on 02-NOV-2015. This code needs to be commented if they need MICR in future.
      receiptInfo.setMICREnabled(false);
      //Ends
      if (receiptInfo.getMICREnabled()) {
        if (theCheck == null) {
          // to do: change this method name to "isPrinterAPieceOfCrap()"
          //               if(receiptInfo.isPrinterIBM() && !isTypeEntered) {
          //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter 'B' for business or 'P' for personal check."),
          //                     "IBMTYPE");
          //                  return  false;
          //               }
          if (responseToContinue) {
            theAppMgr.setSingleEditArea(applet.res.getString(
                "Place check in reader.  Press 'Enter' when ready."), "MICR");
            return false;
          }
          //               else {
          //                  if(receiptInfo.isPrinterIBM()) {
          //                     EditAreaEvent("TYPE",(businessCheck?"B":"P"));
          //                  }
          //                  else {
          //                     theAppMgr.setSingleEditArea(applet.res.getString("Check read failed.  Enter 'B' for business or 'P' for personal check."),
          //                           "TYPE");
          //                     return  false;
          //                  }
          //               }
        }
      }
      /**
       * Added ABA number for business check
       */
      //MICR failed or MICR is NOT enabled...
      // check is not null
      // If MICR failed, hand enter personal & business check attributes
      if (theCheck == null)
        theCheck = new CMSBankCheck(IPaymentConstants.CHECK);
      if (theCheck != null) {
        BankCheck chk = (BankCheck)theCheck;
        if (chk.getTransitNumber().trim().equals("")) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter ABA number."), "ABA"
              , theAppMgr.REQUIRED_MASK);
          return false;
        }
      }
      /**
       * Added DDA number for business check
       */
      // If MICR failed, hand enter personal & business check attributes
      if (theCheck != null) {
        BankCheck chk = (BankCheck)theCheck;
        if (chk.getAccountNumber().trim().equals("")) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter DDA number."), "DDA"
              , theAppMgr.REQUIRED_MASK);
          return false;
        }
      }
      // If MICR failed, hand enter personal check attributes
      if (theCheck != null) { // && (!businessCheck)) {
        BankCheck chk = (BankCheck)theCheck;
        if (chk.getCheckNumber().length() == 0) {
          theAppMgr.setSingleEditArea(applet.res.getString("Enter check number."), "NUMBER"
              , theAppMgr.REQUIRED_MASK);
          return false;
        }
        //            }
        // Rest of check info for personal checks when read by MICR
        //            if (theCheck != null && (!businessCheck)) {
        //               BankCheck chk = (BankCheck)theCheck;
        //               if (chk.getDriversLicenseNumber().length() == 0) {
        //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter driver's license number."),
        //                        "DRIVER");
        //                  return  false;
        //               }
        //               if (chk.getStateIdCode().length() == 0) {
        //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter ID type code."), "ID_CODE");
        //                  return  false;
        //               }
        //            }
        // If MICR failed, hand enter business check attributes
        //            if (theCheck != null && (businessCheck)) {
        //               BusinessCheck chk = (BusinessCheck)theCheck;
        //               if (chk.getCheckNumber().length() == 0) {
        //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter check number."), "BUSINESS_NUMBER",
        //                        theAppMgr.INTEGER_MASK);
        //                  return  false;
        //               }
        //               if (chk.getCheckMICRdata().length() == 0) {
        //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter MICR account number."),
        //                        "BUSINESS_MICR");
        //                  return  false;
        //               }
        //            }
      } // End of if ( receiptInfo.getMICREnabled() ) {
      // If MICR is NOT enabled...
      //         if (!receiptInfo.getMICREnabled()) {
      //            if (theCheck == null) {
      //               theAppMgr.setSingleEditArea(applet.res.getString("Enter 'B' for business or 'P' for personal check."),
      //                     "TYPE");
      //               return  false;
      //            }
      //            if (theCheck != null && (!businessCheck)) {
      //               BankCheck chk = (BankCheck)theCheck;
      //               if (chk.getCheckNumber().length() == 0) {
      //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter check number."), "NUMBER",
      //                        theAppMgr.INTEGER_MASK);
      //                  return  false;
      //               }
      //               if (chk.getDriversLicenseNumber().length() == 0) {
      //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter driver's license number."),
      //                        "DRIVER");
      //                  return  false;
      //               }
      //               if (chk.getStateIdCode().length() == 0) {
      //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter ID type code."), "ID_CODE");
      //                  return  false;
      //               }
      //            }
      //            if (theCheck != null && (businessCheck)) {
      //               BusinessCheck chk = (BusinessCheck)theCheck;
      //               if (chk.getCheckMICRdata().length() == 0) {
      //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter MICR account number."),
      //                        "BUSINESS_MICR");
      //                  return  false;
      //               }
      //               if (chk.getCheckNumber().length() == 0) {
      //                  theAppMgr.setSingleEditArea(applet.res.getString("Enter check number."), "BUSINESS_NUMBER",
      //                        theAppMgr.INTEGER_MASK);
      //                  return  false;
      //               }
      //            }
      //         }      // End of if ( !receiptInfo.getMICREnabled() ) {
      // manual override only if in re-entry mode
      if (aTxn.getHandWrittenTicketNumber().length() > 0 && theCheck.isAuthRequired()) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter authorization number."), "OVERRIDE"
            , theAppMgr.REQUIRED_MASK);
        return false;
      }
    } catch (Exception e) {
      //System.out.println("Catch all exception for completeAttributes() in CheckBldr." + e);
      String errMessage = applet.res.getString("MICR Read Error") + ".  "
          + applet.res.getString("Enter check information manually.");
      theAppMgr.showErrorDlg(errMessage);
      //         theAppMgr.setSingleEditArea(applet.res.getString("Enter 'B' for business or 'P' for personal check."),
      //               "TYPE");
      theAppMgr.setSingleEditArea(applet.res.getString("Enter ABA number."), "ABA"
          , theAppMgr.REQUIRED_MASK);
      LoggingServices.getCurrent().logMsg(getClass().getName(), "checkBuilder completeAttributes()"
          , "Cannot complete check read.", "Make sure MICR reader is working properly."
          , LoggingServices.MINOR, e);
      return false;
    }
    if (theCheck.getAmount() == null) {
      try {
        PaymentTransaction theTxn = (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
        //CMSPaymentTransactionAppModel appModel = new CMSPaymentTransactionAppModel(theTxn);
        CMSPaymentTransactionAppModel appModel = (CMSPaymentTransactionAppModel)theTxn.getAppModel(
            CMSAppModelFactory.getInstance(), theAppMgr);
        ArmCurrency amt = new ArmCurrency(0.0d);
        txn = (CMSPaymentTransactionAppModel) ((PaymentTransaction) theAppMgr.getStateObject("TXN_POS")).getAppModel(CMSAppModelFactory.getInstance(), theAppMgr);
      //Vivek Mishra : Added to show the actual amount due in PRE SALE scenario 
  		if ((txn.getPaymentTransaction() instanceof CMSCompositePOSTransaction) && ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray() != null
  				&& ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleLineItemsArray().length > 0) {
  			amt = ((CMSCompositePOSTransaction) txn.getPaymentTransaction()).getPresaleTransaction().getTotalAmountDue().subtract(appModel.getTotalPaymentAmount());
  		}
  		//Ends
  		else
             amt = appModel.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount());
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , amt.absoluteValue(), theAppMgr.CURRENCY_MASK);
        return false;
      } catch (Exception ex) {
        theAppMgr.setSingleEditArea(applet.res.getString("Enter amount."), "AMOUNT"
            , theAppMgr.CURRENCY_MASK);
        return false;
      }
    }
    return true;
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("MICR")) {
      theAppMgr.setSingleEditArea(applet.res.getString("Processing MICR input..."));
      readMICR();
    }
    //      if (theCommand.equals("IBMTYPE")) {
    //         String action = (String)theEvent;
    //         if (action.equalsIgnoreCase("B") || action.equalsIgnoreCase("P")) {
    //            isTypeEntered = true;
    //            if (action.equalsIgnoreCase("P")) {
    //               businessCheck = false;
    //            }
    //            else {
    //               businessCheck = true;
    //            }
    //         }
    //         else {
    //           theAppMgr.setSingleEditArea(applet.res.getString("Enter 'B' for business or 'P' for personal check."),
    //                     "IBMTYPE");
    //         }
    //      }
    //      if (theCommand.equals("TYPE")) {
    //         String action = (String)theEvent;
    //         if (action.equalsIgnoreCase("B") || action.equalsIgnoreCase("P")) {
    //            if (action.equalsIgnoreCase("P")) {
    //               theCheck = new CMSBankCheck(IPaymentConstants.CHECK);
    //               businessCheck = false;
    //            }
    //            else {
    //               theCheck = new BusinessCheck(IPaymentConstants.CHECK);
    //               businessCheck = true;
    //            }
    //         }
    //      }
    //      if (theCommand.equals("DRIVER")) {
    //         BankCheck chk = (BankCheck)theCheck;
    //         chk.setDriversLicenseNumber(((String)theEvent).toUpperCase());
    //      }
    //      if (theCommand.equals("ID_CODE")) {
    //         String code = setIdCode((String)theEvent);
    //         //System.out.println("STATE CODE input event=" + theEvent);
    //         BankCheck chk = (BankCheck)theCheck;
    //         //System.out.println("STATE CODE BEING SET FOR CHECK=" + code);
    //         chk.setStateIdCode(code);
    //      }
    if (theCommand.equals("ABA")) {
      IsDigit digitCheck = new IsDigit();
      if (!digitCheck.isDigit(theEvent + "")) {
        theAppMgr.showErrorDlg(applet.res.getString("The value is not a number."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter ABA number."), "ABA"
            , theAppMgr.REQUIRED_MASK);
        return;
      }
      if ((theEvent + "").length() > 9) {
        theAppMgr.showErrorDlg(applet.res.getString("The ABA number should not exceed 9 digits."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter ABA number."), "ABA"
            , theAppMgr.REQUIRED_MASK);
        return;
      }
      BankCheck chk = (BankCheck)theCheck;
      //Integer nCheckNum = (Integer)theEvent;
      //chk.setTransitNumber(nCheckNum.toString());
      chk.setTransitNumber(theEvent + "");
      chk.setIsCheckScannedIn(false);
    }
    if (theCommand.equals("DDA")) {
      IsDigit digitCheck = new IsDigit();
      if (!digitCheck.isDigit(theEvent + "")) {
        theAppMgr.showErrorDlg(applet.res.getString("The value is not a number."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter DDA number."), "DDA"
            , theAppMgr.REQUIRED_MASK);
        return;
      }
      if ((theEvent + "").length() > 15) {
        theAppMgr.showErrorDlg(applet.res.getString("The DDA number should not exceed 15 digits."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter DDA number."), "DDA"
            , theAppMgr.REQUIRED_MASK);
        return;
      }
      BankCheck chk = (BankCheck)theCheck;
      //Integer nCheckNum = (Integer)theEvent;
      //chk.setDDANumber(nCheckNum.intValue());
      chk.setAccountNumber(theEvent + "");
      chk.setIsCheckScannedIn(false);
    }
    if (theCommand.equals("NUMBER")) {
      IsDigit digitCheck = new IsDigit();
      if (!digitCheck.isDigit(theEvent + "")) {
        theAppMgr.showErrorDlg(applet.res.getString("The value is not a number."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter check number."), "NUMBER"
            , theAppMgr.REQUIRED_MASK);
        return;
      }
      if ((theEvent + "").length() > 6) {
        theAppMgr.showErrorDlg(applet.res.getString("The Check number should not exceed 6 digits."));
        theAppMgr.setSingleEditArea(applet.res.getString("Enter check number."), "NUMBER"
            , theAppMgr.REQUIRED_MASK);
        return;
      }
      BankCheck chk = (BankCheck)theCheck;
      //Integer nCheckNum = (Integer)theEvent;
      //chk.setCheckNumber(nCheckNum.intValue() + "");
      chk.setCheckNumber(theEvent + "");
      chk.setIsCheckScannedIn(false);
    }
    //      if (theCommand.equals("ACCOUNT")) {
    //         BankCheck chk = (BankCheck)theCheck;
    //         chk.setAccountNumber((String)theEvent);
    //         chk.setIsCheckScannedIn(false);
    //      }
    //      if (theCommand.equals("BUSINESS_MICR")) {
    //         BusinessCheck chk = (BusinessCheck)theCheck;
    //         chk.setCheckMICRdata((String)theEvent);
    //         chk.setAccountNumber((String)theEvent);
    //         chk.setIsCheckScannedIn(false);
    //      }
    //      if (theCommand.equals("BUSINESS_NUMBER")) {
    //         BusinessCheck chk = (BusinessCheck)theCheck;
    //         Integer nCheckNum = (Integer)theEvent;
    //         chk.setCheckNumber(nCheckNum.intValue() + "");
    //      }
    if (theCommand.equals("OVERRIDE")) {
      CMSBankCheck chk = (CMSBankCheck)theCheck;
      chk.setManualOverride((String)theEvent);
    }
    if (theCommand.equals("AMOUNT")) {
      String paymentTypeView = theCheck.getGUIPaymentName();
      String paymentType = theCheck.getTransactionPaymentName();
      if (TransactionUtil.validateChangeAmount(theAppMgr, paymentType, paymentTypeView
          , (ArmCurrency)theEvent))
        theCheck.setAmount((ArmCurrency)theEvent);
    }
    if (completeAttributes()) {
      String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
      String registerId = ((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId();
      //Anjana commenting this as journal key has to be fetched from resposne as the unique sequence number  (IxInvoice) feild from response
     // theCheck.setJournalKey(CreditAuthUtil.getJournalKey(storeId, registerId));
      theBldrMgr.processObject(applet, "CHECK_PAYMENT", theCheck, this);
    }
  }

  // Read MICR on printer
  private void readMICR() {
    try {
      //System.out.println("BEGIN CheckBldr readMICR() event.");
      while (posMICR.getState() == JPOS_S_BUSY) {
        try {
          Thread.sleep(1000);
        } catch (Exception e) {}
        //System.out.println("MICR busy...waiting to read.");
      }
      CMSPrinter.openDevice(receiptInfo.getPrinterName(), theAppMgr);
      CMSPrinter.reclaim(theAppMgr);
      CMSMICR.openDevice(receiptInfo.getMICRName(), theAppMgr);
      CMSMICR.reclaim(theAppMgr);
      posMICR.clearInput();
      posMICR.beginInsertion(5000);
      posMICR.endInsertion();
      //theMICR.setAutoDisable(true);
      posMICR.setDataEventEnabled(true);
      System.out.println("completed check readMICR...");
    } catch (JposException jpe) {
      String errMessage = "";
      System.out.println("CHECK BLDR READ MICR JposException Code e -> " + jpe);
      System.out.println("CHECK BLDR READ MICR JposException Code -> " + jpe.getErrorCode());
      System.out.println("CHECK BLDR READ MICR Ext JposException Code -> "
          + jpe.getErrorCodeExtended());
      errMessage = applet.res.getString("Printer Error") + ":  " + jpe.getMessage() + ".  "
          + applet.res.getString("Do you want to try again?");
      if (jpe.getErrorCode() == 106) {
        System.out.println("RECEIVED readMICR() error 106");
        return;
      }
      if (jpe.getErrorCode() == 112 || jpe.getErrorCode() == 114) {
        System.out.println("RECEIVED readMICR() error 112 or 114");
        errMessage = applet.res.getString("Printer Error:  Check not inserted in printer.  Select 'Retry' to try again or 'Manual' to enter check information manually.");
        boolean printerRetry = theAppMgr.showOptionDlg(applet.res.getString("Printer Error")
            , errMessage, applet.res.getString("Retry"), applet.res.getString("Manual"));
        if (!printerRetry) {
          responseToContinue = false;
          completeAttributes();
        }
      }
      // Copied next four line up from the Exception block below per Angela.   lar
      //If any other errors... just fail the MICR and let them manually redo.   alt
      if (jpe.getErrorCode() != 112 & jpe.getErrorCode() != 114 & jpe.getErrorCode() != 106) {
        System.out.println("RECEIVED readMICR() general catch all error.");
        errMessage = applet.res.getString("MICR Read Error") + ".  "
            + applet.res.getString("Enter check information manually.");
        theAppMgr.showErrorDlg(errMessage);
        LoggingServices.getCurrent().logMsg(getClass().getName(), "checkBuilder readMICR()"
            , "Cannot complete check read.", "Make sure MICR reader is working properly."
            , LoggingServices.MINOR, jpe);
        try {
          posMICR.beginRemoval(1000);
          posMICR.endRemoval();
          posMICR.setDataEventEnabled(false);
          posMICR.clearInput();
          CMSMICR.release();
          posPrinter.clearOutput();
          CMSPrinter.release();
        } catch (JposException pe) {
          LoggingServices.getCurrent().logMsg(getClass().getName(), "checkBuilder readMICR()"
              , "Cannot complete check read.", "Make sure MICR reader is working properly."
              , LoggingServices.MINOR, pe);
        }
        responseToContinue = false;
        completeAttributes();
      }
    } catch (Exception e) {
      //If any other errors... just fail the MICR and let them manually redo.   alt
      String errMessage = "";
      System.out.println("CHECK BLDR READ MICR Exception e -> " + e);
      System.out.println("CHECK BLDR READ MICRError Exception -> " + e.getMessage());
      errMessage = applet.res.getString("MICR Read Error") + ".  "
          + applet.res.getString("Enter check information manually.");
      theAppMgr.showErrorDlg(errMessage);
      //theAppMgr.setSingleEditArea("Enter 'B' for business or 'P' for personal check.", "TYPE");
      LoggingServices.getCurrent().logMsg(getClass().getName(), "checkBuilder readMICR()"
          , "Cannot complete check read.", "Make sure MICR reader is working properly."
          , LoggingServices.MINOR, e);
      try {
        posMICR.beginRemoval(1000);
        posMICR.endRemoval();
        posMICR.setDataEventEnabled(false);
        posMICR.clearInput();
        CMSMICR.release();
        posPrinter.clearOutput();
        CMSPrinter.release();
      } catch (JposException pe) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "checkBuilder readMICR()"
            , "Cannot complete check read.", "Make sure MICR reader is working properly."
            , LoggingServices.MINOR, pe);
      }
      responseToContinue = false;
      completeAttributes();
      //errMessage =  "Printer Error:  " + e.getMessage() + ".   Do you want to try again?";
      //responseToContinue = theAppMgr.showOptionDlg("Printer Error.", errMessage);
      //if ( responseToContinue ) { readMICR(); } else { completeAttributes(); }
    }
  }

  /**
   * @param event
   */
  public void errorOccurred(ErrorEvent event) {
    System.out.println("errorOccurred->" + event.getErrorCode());
  }

  /**
   * @param event
   */
  public void dataOccurred(DataEvent event) {
    try {
      //System.out.println("BEGIN CheckBldr dataOccured() event.");
      System.out.println("data event has occured - we may have micr data = " + event);
      posMICR.beginRemoval(6000);
      posMICR.endRemoval();
      theCheck = allocCheck();
      responseToContinue = false; // Setting this to false, allow only one pass to read MICR.
      posMICR.setDataEventEnabled(false);
      //posMICR.removeDataListener(this);
      //posMICR.clearInput();
      completeAttributes();
      //System.out.println("END CheckBldr dataOccured event.");
    } catch (JposException jpe) {
      String errMessage = "";
      System.out.println("CHECK BLDR dataOccurred MICR Code e -> " + jpe);
      System.out.println("CHECK BLDR dataOccurred MICRError Code -> " + jpe.getErrorCode());
      System.out.println("CHECK BLDR dataOccurred MICRExt Error Code -> "
          + jpe.getErrorCodeExtended());
      errMessage = applet.res.getString("Printer Error") + ":  " + jpe.getMessage() + ".  "
          + applet.res.getString("Do you want to try again?");
      //if ( jpe.getErrorCode() == 106 ) {
      //   System.out.println("RECEIVED dataOccurred() error 106");
      //   return;
      //}
      // This error is caught if check is not removed after 6 seconds.
      // More of informational message than error.
      if (jpe.getErrorCode() == 112) {
        System.out.println("RECEIVED dataOccurred() error 112 to remove check.");
        errMessage = applet.res.getString("Please remove check from printer.");
        theAppMgr.showErrorDlg(errMessage);
        // Call rest of data event methods to continue GUI flow.
        theCheck = allocCheck();
        responseToContinue = false; // Setting this to false, allow only one pass to read MICR.
        completeAttributes();
      }
      //if ( jpe.getErrorCode() != 112 || jpe.getErrorCode() != 106 ) {
      if (jpe.getErrorCode() != 112) {
        System.out.println("RECEIVED dataOccurred() error catch all." + jpe.getErrorCode());
        responseToContinue = false;
        completeAttributes();
        //responseToContinue = theAppMgr.showOptionDlg("Printer Error.", errMessage);
        //if ( responseToContinue ) { completeAttributes(); } else { completeAttributes(); }
      }
    } catch (Exception e) {
      responseToContinue = false;
      String errMessage = "";
      System.out.println("CHECK BLDR dataOccurred MICR Code e -> " + e);
      System.out.println("CHECK BLDR dataOccurred MICRError Code -> " + e.getMessage());
      completeAttributes();
      //errMessage =  "Printer Error:  " + e.getMessage() + ".   Do you want to try again?";
      //responseToContinue = theAppMgr.showOptionDlg("Printer Error.", errMessage);
      //if ( responseToContinue ) { completeAttributes(); } else { completeAttributes(); }
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private Check allocCheck() {
    //System.out.println("allocCheck()");
    try {
      String micr = "";
      String sRawData = posMICR.getRawData();
      int sCheckType = posMICR.getCheckType();
      boolean businessCheck = (sCheckType == MICR_CT_BUSINESS);
      // parse this information out of the MICR string once
      // the MICR string has been digested into the standard format.
      // The hardware is not really capable of returning these fields reliably
      String sAccountNumber = "";
      String sBankNumber = "";
      String sSequenceNumber = "";
      String sTransitNumber = posMICR.getTransitNumber();
      System.out.println("~~~~~~Raw MICR Data: >>>" + sRawData + "<<<");
      if (sRawData != null) {
        micr = sRawData;
        int micrLength = micr.length();
        //Removing any null or space characters from the Raw MICR data.
        char testMICRString[] = micr.toCharArray();
        StringBuffer newMICRString = new StringBuffer();
        boolean removeExtraneousA = false;
        if (micr.endsWith("a")) {
          if ((micr.indexOf("at") > 0 || micr.indexOf("a t") > 0 || micr.indexOf("a  t") > 0)
              && !micr.startsWith("a")) {
            micr = "a" + micr;
          }
          if (micr.indexOf("a") == micr.lastIndexOf("a")) {
            System.out.println(
                "the TAA style, but with only one 'a' delimiter, will attempt to place delimiter for sequence no");
            boolean initialSpacesFound = false;
            boolean numbersAfterInitialSpacesFound = false;
            for (int i = 0; i < testMICRString.length; i++) {
              if (testMICRString[i] == ' ' || testMICRString[i] == '-') {
                if (initialSpacesFound && numbersAfterInitialSpacesFound) {
                  System.out.println("'a' delimiter has been placed");
                  testMICRString[i] = 'a';
                  i = testMICRString.length;
                } else
                  initialSpacesFound = true;
              } else if (Character.isDigit(testMICRString[i]) && initialSpacesFound)
                numbersAfterInitialSpacesFound = true;
            }
          } else if (countChars('a', micr) == 3 && !micr.substring(0, 1).equals("t")) {
            String reArrange = micr.substring(1);
            String seq = micr.substring(1, reArrange.indexOf("a") + 1);
            reArrange = reArrange.substring(reArrange.indexOf("a") + 1);
            System.out.println("bank + acct with bracket as removed: " + reArrange);
            //System.out.println("seq: " + seq);
            testMICRString = (reArrange + seq).toCharArray();
          } else {
            System.out.println("the TAA style with proper delimters in place");
          }
        } else if (micr.indexOf("a") != micr.lastIndexOf("a")) {
          System.out.println("the TAA style, will remove extraneous initial 'a' character...");
          removeExtraneousA = true;
        }
        for (int x = 0; x < testMICRString.length; x++) {
          if (testMICRString[x] != '\0' && testMICRString[x] != ' ' && testMICRString[x] != '-') {
            if (testMICRString[x] == 'a' && removeExtraneousA) {
              removeExtraneousA = false;
            } else {
              newMICRString.append(testMICRString[x]);
            }
          } else {
            System.out.println("removing invalid char from micr: >>" + testMICRString[x] + "<<");
          }
        }
        micr = newMICRString.toString();
        System.out.println("Cleaned Up MICR String PRIOR to TA manipulation >" + micr + "<");
        micr = fixMicr(micr);
        System.out.println("Cleaned Up MICR String AFTER TA manipulation >" + micr + "<");
      }
      if (micr.length() > 0 && micr.indexOf("A") > -1 && micr.indexOf("T") > -1) {
        sBankNumber = micr.substring(0, micr.indexOf("T"));
        sAccountNumber = micr.substring(micr.indexOf("T") + 1, micr.indexOf("A"));
        sSequenceNumber = micr.substring(micr.indexOf("A") + 1);
      } else {
        System.out.println("Invalid MICR string format, will not attempt to parse aba,account,seq");
        sAccountNumber = "0";
        sBankNumber = "0";
        sSequenceNumber = "1";
      }
      System.out.println("ABA: " + sBankNumber);
      System.out.println("Account: " + sAccountNumber);
      System.out.println("Sequence Number: " + sSequenceNumber);
      int checkType = sCheckType;
      //System.out.println("WILL USE MANUALLY ENTERED CHECK TYPE");
      checkType = businessCheck ? MICR_CT_BUSINESS : MICR_CT_PERSONAL;
      switch (checkType) {
        case MICR_CT_PERSONAL:
          System.out.println("check is personal");
          BankCheck chk = new CMSBankCheck(IPaymentConstants.CHECK);
          if (micr.length() == 0) {
            chk.setIsCheckScannedIn(false);
            theAppMgr.showErrorDlg(
                "The MICR cannot be interpreted, you must enter this check manually");
          } else {
            chk.setCheckNumber(sSequenceNumber);
            chk.setAccountNumber(sAccountNumber);
            chk.setBankNumber(sBankNumber);
            chk.setTransitNumber(sTransitNumber);
            chk.setCheckMICRdata(micr);
            chk.setIsCheckScannedIn(true);
          }
          return chk;
        case MICR_CT_BUSINESS: {
          System.out.println("check is business");
          BusinessCheck bchk = new BusinessCheck(IPaymentConstants.CHECK);
          if (micr.length() == 0) {
            bchk.setIsCheckScannedIn(false);
            theAppMgr.showErrorDlg(
                "The MICR cannot be interpreted, you must enter this check manually");
          } else {
            bchk.setCheckNumber(sSequenceNumber);
            bchk.setAccountNumber(sAccountNumber);
            bchk.setBankNumber(sBankNumber);
            bchk.setTransitNumber(sTransitNumber);
            bchk.setCheckMICRdata(micr);
            bchk.setIsCheckScannedIn(true);
          }
          businessCheck = true;
          return bchk;
        }
        default: {
          return null;
        }
      }
    } catch (Exception ex) {
      System.err.println("Error allocCheck()-> " + ex);
      return null;
    }
  }

  /**
   * put your documentation comment here
   * @param countMe
   * @param inString
   * @return
   */
  private int countChars(char countMe, String inString) {
    int count = 0;
    char[] chars = inString.toCharArray();
    for (int i = 0; i < chars.length; i++)
      if (chars[i] == countMe)
        count++;
    return count;
  }

  /**
   * put your documentation comment here
   * @param rawMicr
   * @return
   */
  private String fixMicr(String rawMicr) {
    if (rawMicr == null || rawMicr.length() < 6) {
      System.out.println("invalid MICR received from hardware, will not attempt to fix: " + rawMicr);
      return "";
    }
    if (Character.isDigit(rawMicr.charAt(0))) {
      rawMicr = rawMicr.substring(1);
    }
    try {
      if (rawMicr.startsWith("t")) {
        if (rawMicr.endsWith("a")) {
          return fixMicrForStyleTAA(rawMicr);
        } else {
          return fixMicrForStyleTA(rawMicr);
        }
      } else if (rawMicr.startsWith("a")) {
        return fixMicrForStyleAATTAA(rawMicr);
      } else {
        System.out.println("invalid MICR received from hardware, will not attempt to fix: "
            + rawMicr);
        return "";
      }
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * put your documentation comment here
   * @param rawMicr
   * @return
   * @exception Exception
   */
  private String fixMicrForStyleAATTAA(String rawMicr)
      throws Exception {
    StringBuffer aba = new StringBuffer();
    StringBuffer acct = new StringBuffer();
    StringBuffer seq = new StringBuffer();
    char[] rawMicrChars = rawMicr.toCharArray();
    System.out.println("AATTAA style with check seq number in the middle detected");
    int i = 1;
    while (rawMicrChars[i] != 'a') {
      seq.append(rawMicrChars[i]);
      i++;
    }
    i++;
    i++;
    while (rawMicrChars[i] != 't') {
      aba.append(rawMicrChars[i]);
      i++;
    }
    i++;
    i++;
    while (rawMicrChars[i] != 'a') {
      acct.append(rawMicrChars[i]);
      i++;
    }
    return aba.toString() + "T" + acct.toString() + "A" + seq.toString();
  }

  /**
   * put your documentation comment here
   * @param rawMicr
   * @return
   * @exception Exception
   */
  private String fixMicrForStyleTAA(String rawMicr)
      throws Exception {
    StringBuffer aba = new StringBuffer();
    StringBuffer acct = new StringBuffer();
    StringBuffer seq = new StringBuffer();
    char[] rawMicrChars = rawMicr.toCharArray();
    System.out.println("TAA style with check seq number in the middle detected");
    int i = 1;
    while (rawMicrChars[i] != 't') {
      aba.append(rawMicrChars[i]);
      i++;
    }
    i++;
    while (rawMicrChars[i] != 'a') {
      seq.append(rawMicrChars[i]);
      i++;
    }
    i++;
    while (rawMicrChars[i] != 'a') {
      acct.append(rawMicrChars[i]);
      i++;
    }
    return aba.toString() + "T" + acct.toString() + "A" + seq.toString();
  }

  /**
   * put your documentation comment here
   * @param rawMicr
   * @return
   * @exception Exception
   */
  private String fixMicrForStyleTA(String rawMicr)
      throws Exception {
    StringBuffer aba = new StringBuffer();
    StringBuffer acct = new StringBuffer();
    StringBuffer seq = new StringBuffer();
    char[] rawMicrChars = rawMicr.toCharArray();
    System.out.println("TA style with check seq number at the end detected");
    int i = 1;
    while (rawMicrChars[i] != 't') {
      aba.append(rawMicrChars[i]);
      i++;
    }
    i++;
    while (rawMicrChars[i] != 'a') {
      acct.append(rawMicrChars[i]);
      i++;
    }
    i++;
    while (i < rawMicrChars.length) {
      seq.append(rawMicrChars[i]);
      i++;
    }
    return aba.toString() + "T" + acct.toString() + "A" + seq.toString();
  }

  /**
   * @param userInput
   * @return
   */
  private String setIdCode(String userInput) {
    String code = "";
    if (!userInput.equals("") || userInput != null) {
      code = userInput.toUpperCase();
      if (!ValidStates.isValidState(code)) {
        theAppMgr.showErrorDlg(applet.res.getString("Invalid state code"));
        code = "";
      }
    }
    return code;
  }
} //end class

