/*
 * Chelsea Market Systems
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 * History (tab separated):-
 * Vers Date            By      Spec                Description
 * 1    -               -                           Initial
 * 2    2/23/06         MD      Issues #797         Incorrect condition to
 *                                                  designate printer to be ibm
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
//Copyright:    Copyright (c) 1999, Chelsea Market Systems
//


package com.chelseasystems.cr.receipt;

import com.chelseasystems.cr.appmgr.IReceiptAppManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.util.Version;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ResourceBundle;
import jpos.*;

// Referenced classes of package com.chelseasystems.cr.receipt:
//			ReceiptConfigInfo, PrintReady

public class CMSPrinter implements JposConst, POSPrinterConst {

    private static ArrayList validatedLogoFiles = new ArrayList();
    private static POSPrinter varPrinter = new POSPrinter();
    private static final int printerCharacterSet;
    private static ReceiptConfigInfo receiptInfo = ReceiptConfigInfo.
            getInstance();
    private static boolean isHeaderGraphicLoaded = false;
    private static boolean isFooterGraphicLoaded = false;
    private static boolean printerIsOpen = false;
    private static boolean ibmBarCode = false;
    private static IReceiptAppManager theAppMgr = null;
    public static final String CRLF = new String("\n");
    public static final String ESC = new String("\033");

    static {
        ConfigMgr cMgr = new ConfigMgr("JPOS_peripherals.cfg");
        String pcs = cMgr.getString("PRINTER_CHARACTER_SET");
        int cs = 0;
        if (pcs != null) {
            try {
                cs = Integer.parseInt(pcs);
            } catch (NumberFormatException e) {
                cs = 0;
            }
        }
        printerCharacterSet = cs;
        System.out.println("Printer character set: " + printerCharacterSet);
        String ibm = cMgr.getString("IBM_BARCODE");
        if (ibm == null || ibm.equalsIgnoreCase("TRUE")) {
            ibmBarCode = true;
        } else {
            ibmBarCode = false;
        }
        System.out.println("IBM with 3 code set support: " + ibmBarCode);
    }

  /**
   * IBM has added a third code set for 128 type barcodes.   This feature should be
   * added to the UPOS 1.8 spec.   Until then, the work aroung is to pass {A to the printer
   * when you want to print a barcode.
   * @return boolean - ibmjpos.jar has 3 code sets for 128 barcode.
   */
    public static boolean isIbmBarCode() {
        return ibmBarCode;
    }

  /**
   * put your documentation comment here
   * @return
   */
    public static POSPrinter getInstance() {
        return varPrinter;
    }

  /**
   * put your documentation comment here
   * @param Name
   * @param app
   * @return
   * @exception Exception
   */
    public static boolean openDevice(String Name, IReceiptAppManager app) throws
            Exception {
        System.out.println("*******METHOD openDevice() called:**********");
        boolean success = true;
        if (!printerIsOpen) {
            success = false;
            System.out.println(
                    "*******OPENING PRINTER, THIS ONLY HAPPENS ONCE**********");
            boolean responseToContinue = true;
            try {
                while ((!printerIsOpen) & responseToContinue) {
                    varPrinter.open(Name);
                    printerIsOpen = true;
                    theAppMgr = app;
                    responseToContinue = false;
                    success = true;
                }
            } catch (JposException e) {
                LoggingServices.getCurrent().logMsg(
                        "com.chelseasystems.receipt.CMSPrinter", "openDevice()",
                        "Cannot open printer.", "N/A", 2);
                System.out.println("OPEN Printer Code e -> " + e);
                System.out.println("OPEN PrinterError Code -> " +
                                   e.getErrorCode());
                System.out.println("OPEN PrinterExt Error Code -> " +
                                   e.getErrorCodeExtended());
                System.out.println("OPEN Message -> " + e.getMessage());
                System.out.println("OPEN Localized Message -> " +
                                   e.getLocalizedMessage());
                ResourceBundle res = ResourceManager.getResourceBundle();
                String errMessage = res.getString("Printer Error:  ") +
                                    res.getString(e.getMessage()) +
                                    res.
                                    getString(".   Do you wish to try again?");
                boolean flag = app.showOptionDlg(res.getString("Printer Error."),
                                                 errMessage);
            }
        }
        return success;
    }

    public static boolean reclaim(IReceiptAppManager app) throws Exception {
        System.out.println("*******METHOD reclaim() called:**********");
        boolean success = false;
        boolean claimed = false;
		//Vivek Mishra : Merged missing code from source provided by Sergio 18-MAY-16
        boolean responseToContinue = true;
        //System.out.println("responseToContinue    :"+responseToContinue);
        //System.out.println("!claimed    :"+!claimed);
        //System.out.println("responseToContinue = true; (!claimed) & responseToContinue;    "+((!claimed) & responseToContinue));
        for (responseToContinue = true; (!claimed) & responseToContinue; ) {
            try {
            	   if(printerIsOpen)
            	  	 release();
		//Ends here			 
                varPrinter.claim(2000);
                claimed = true;
            } catch (JposException e) {
                LoggingServices.getCurrent().logMsg(
                        "com.chelseasystems.receipt.CMSPrinter", "openDevice()",
                        "Cannot Re-claim printer.", "N/A", 2);
                System.out.println("RE-CLAIM Printer Code e -> " + e);
                System.out.println("RE-CLAIM PrinterError Code -> " +
                                   e.getErrorCode());
                System.out.println("RE-CLAIM PrinterExt Error Code -> " +
                                   e.getErrorCodeExtended());
                System.out.println("RE-CLAIM Message -> " + e.getMessage());
                System.out.println("Re-CLAIM Localized Message -> " +
                                   e.getLocalizedMessage());
                ResourceBundle res = ResourceManager.getResourceBundle();
                String errMessage = res.getString("Printer Error:  ") +
                                    res.getString(e.getMessage()) +
                                    res.
                                    getString(".   Do you wish to try again?");
                responseToContinue = app.showOptionDlg(res.getString(
                        "Printer Error."), errMessage);
            }
        }

        try {
            if (claimed) {
                varPrinter.setDeviceEnabled(true);
                success = true;
                if (printerCharacterSet != 0) {
                    varPrinter.setCharacterSet(printerCharacterSet);
                    System.out.println("Printer characterset = " +
                                       printerCharacterSet);
                }
            }
        } catch (JposException e) {
            LoggingServices.getCurrent().logMsg(
                    "com.chelseasystems.receipt.CMSPrinter", "openDevice()",
                    "Cannot Re-Enable printer.", "N/A", 2);
            System.out.println("RE-ENABLE Printer Code e -> " + e);
            System.out.println("RE-ENABLE PrinterError Code -> " +
                               e.getErrorCode());
            System.out.println("RE-ENABLE PrinterExt Error Code -> " +
                               e.getErrorCodeExtended());
            System.out.println("RE-ENABLE Message -> " + e.getMessage());
            System.out.println("RE-ENABLE Localized Message -> " +
                               e.getLocalizedMessage());
        }
        return success;
    }

    public static void release() {
        System.out.println(
                "*******METHOD release() called: Release Ignored**********");
    }

    public static void cut() throws JposException {
        try {
            varPrinter.printNormal(2, CRLF + ESC + "|90fP");
        } catch (JposException ee) {
            LoggingServices.getCurrent().logMsg(
                    "com.chelseasystems.receipt.CMSPrinter", "cut()",
                    "Cannot perform method cut() on printer.", "N/A", 2);
            throw ee;
        }
    }

  public static void barcode(String value)
      throws JposException {
    int symbology = jpos.POSPrinterConst.PTR_BCS_Code128;
    try {
      varPrinter.printNormal(PTR_S_RECEIPT, CRLF);
      String newValue = isIbmBarCode() ? "{A" + value : value;
      varPrinter.printBarCode(PTR_S_RECEIPT, newValue, symbology, 100, 200, PTR_BC_CENTER
          , PTR_BC_TEXT_BELOW);
    } catch (JposException ee) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "barcode()"
          , "Cannot perform method barcode on printer.", "N/A", LoggingServices.MAJOR);
      throw ee;
    }
  }

  /**
   * Internal method to print the logo from the filename given.
   **/
  public static void logoHeader(String logoFileName)
      throws JposException, Exception {
    PrintReady printReady = new PrintReady();
    //if (receiptInfo.isPrinterEpson()) {
    logo(logoFileName);
    return; /*}
         try {
         System.out.println("IBM printer logo: " + logoFileName);
         if (!logoFileName.equals("")) {
         if (!isHeaderGraphicLoaded) {
         System.out.println("Loading header graphic one-time-only delay");
         isHeaderGraphicLoaded = true;
    varPrinter.setBitmap(1, PTR_S_RECEIPT, FileMgr.getAbsoluteFile("..\\images\\" + logoFileName),
         PTR_BM_ASIS, PTR_BM_CENTER);
         System.out.println("Header graphic load complete");
         }
         System.out.println("Printing pre-loaded header graphic");
         varPrinter.printNormal(PTR_S_RECEIPT, ESC + "|1B");
         }
         } catch (JposException jpe) {
    LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "logoHeader()",
         "Error during receipt printing.", "N/A", LoggingServices.MAJOR, jpe);
         throw  jpe;
         } catch (Exception ex) {
    LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "logoHeader()",
         "Error during receipt printing.", "N/A", LoggingServices.MAJOR, ex);
         throw  ex;
         }*/

  }

  /**
   * Internal method to print footer logo from the filename given.
   **/
  public static void logoFooter(String logoFileName)
      throws JposException, Exception {
    if (logoFileName == null || logoFileName.equals("")) {
      System.out.println("bug out footer logo...");
      return;
    }
    try {
      if (receiptInfo.isPrinterEpson()) {
        // epson printer won't print logo directly anymore for some reason, probably soemthing to do with
        // transaction print mode.  It will print from cache2 however...    weird.
        logo(logoFileName);
        //         varPrinter.printBitmap(PTR_S_RECEIPT, FileMgr.getAbsoluteFile("..\\images\\" + logoFileName),
        //               PTR_BM_ASIS, PTR_BM_CENTER);
        //         StringBuffer line = new StringBuffer(" ");
        //         varPrinter.printNormal(PTR_S_RECEIPT, line.toString());
      } else {
        verifyLogoFileExists(FileMgr.getAbsoluteFile("..\\images\\" + logoFileName));
        varPrinter.printBitmap(PTR_S_RECEIPT, FileMgr.getAbsoluteFile("..\\images\\" + logoFileName)
            , PTR_BM_ASIS, PTR_BM_CENTER);
        StringBuffer line = new StringBuffer();
        varPrinter.printNormal(PTR_S_RECEIPT, line.toString());
      }
    } catch (JposException jpe) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "logoFooter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, jpe);
      throw jpe;
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "logoFooter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, ex);
      throw ex;
    }
  }

  /**
   * Internal method to print the logo from the filename given.
   * This should not be used for the re-ocurring header and footer logo as it is slower
   **/
  public static void logo(String logoFileName)
      throws JposException, Exception {
    if (logoFileName == null || logoFileName.length() == 0) {
      return;
    }
    try {
      verifyLogoFileExists(FileMgr.getAbsoluteFile("..\\images\\" + logoFileName));
      //if (receiptInfo.isPrinterEpson() || !isHeaderGraphicLoaded) {
      if (!isHeaderGraphicLoaded) {
        isHeaderGraphicLoaded = true;
        if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
        	varPrinter.setRecLetterQuality(true);
        }        
        varPrinter.setBitmap(2, PTR_S_RECEIPT
            , FileMgr.getAbsoluteFile("..\\images\\" + logoFileName), PTR_BM_ASIS, PTR_BM_CENTER);        
      }
      varPrinter.printNormal(PTR_S_RECEIPT, ESC + "|2B");
      if ("JP".equalsIgnoreCase(Version.CURRENT_REGION)) {
    	  varPrinter.setRecLetterQuality(false);
      }
      //            varPrinter.printBitmap(PTR_S_RECEIPT, FileMgr.getAbsoluteFile("..\\images\\" + logoFileName),
      //                  PTR_BM_ASIS, PTR_BM_CENTER);
      //            StringBuffer line = new StringBuffer();
      //            varPrinter.printNormal(PTR_S_RECEIPT, line.toString());
      //            System.out.println("logo prints ok");
      //         }
    } catch (JposException jpe) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "logo()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, jpe);
      throw jpe;
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "logo()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, ex);
      throw ex;
    }
  }

  /**
   * put your documentation comment here
   * @param logoFileName
   * @exception Exception
   */
  private static void verifyLogoFileExists(String logoFileName)
      throws Exception {
    if (validatedLogoFiles.contains(logoFileName)) {
      return;
    }
    File file = new File(logoFileName);
    if (!file.canRead()) {
      throw new Exception("Logo File is not found or is not readable: " + logoFileName);
    }
    validatedLogoFiles.add(logoFileName);
  }

  /**
   * Internal method to print the Header information on the top of the receipt
   * in EXPANDED PRINT
   **/
  public static PrintReady CMSheaderExpanded(String[] exHeader)
      throws JposException, Exception {
    PrintReady printReady = null;
    try {
      //varPrinter.printBitmap(PTR_S_RECEIPT, AppManager.getLocalFile("..\\images\\Mw-logo3.bmp"), PTR_BM_ASIS, PTR_BM_CENTER);
      printReady = printStringArray(exHeader, true); /*
             if (exHeader != null) {
             StringBuffer line = new StringBuffer();
             line.append(CRLF);
             line.append(ESC + "|cA");           // center the text
             line.append(ESC + "|2C");           // double-width font
             for (int x = 0; x < exHeader.length; x++) {
             line.append(exHeader[x]);
             line.append(CRLF);
             }
             varPrinter.printNormal(PTR_S_RECEIPT, line.toString());
             }*/

    } catch (JposException jpe) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, jpe);
      throw jpe;
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.JPOSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, ex);
      throw ex;
    } finally {
      return printReady;
    }
  }

  /**
   * Internal method to print the Header Information
   **/
  public static PrintReady CMSheader(String[] header)
      throws JposException, Exception {
    PrintReady printReady = null;
    try {
      //varPrinter.printBitmap(PTR_S_RECEIPT, AppManager.getLocalFile("..\\images\\Mw-logo3.bmp"), PTR_BM_ASIS, PTR_BM_CENTER);
      printReady = printStringArray(header, false); /*
             if (header != null) {
             StringBuffer line = new StringBuffer();
             //line.append(CRLF);
             line.append(ESC + "|cA");           // center the text
             //line.append(ESC + "|2C");          // double-width font
             for (int x = 0; x < header.length; x++) {
             line.append(header[x]);
             line.append(CRLF);
             }
             varPrinter.printNormal(PTR_S_RECEIPT, line.toString());
             }*/

    } catch (JposException jpe) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, jpe);
      throw jpe;
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.JPOSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, ex);
      throw ex;
    } finally {
      return printReady;
    }
  }

  /**
   * Internal method to print the Company message footer information
   **/
  public static PrintReady CMSfooter(String[] footer)
      throws JposException, Exception {
    PrintReady printReady = null;
    try {
      printReady = printStringArray(footer, false); /*
             if (footer != null) {
             StringBuffer line = new StringBuffer();
             line.append(CRLF);
             line.append(CRLF);
             line.append(ESC + "|cA");           // center the text
             for (int x = 0; x < footer.length; x++) {
             line.append(footer[x]);
             line.append(CRLF);
             }
             varPrinter.printNormal(PTR_S_RECEIPT, line.toString());
             }
      */

    } catch (JposException jpe) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.JPOSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, jpe);
      throw jpe;
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.JPOSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, ex);
      throw ex;
    } finally {
      return printReady;
    }
  }

  /**
   * Internal method to print the Expanded print footer information
   **/
  public static PrintReady CMSfooterExpanded(String[] footerExpanded)
      throws JposException, Exception {
    return printStringArray(footerExpanded, true);
  }

  /**
   * put your documentation comment here
   * @param stringArray
   * @param isBig
   * @return
   * @exception JposException, Exception
   */
  private static PrintReady printStringArray(String[] stringArray, boolean isBig)
      throws JposException, Exception {
    return printStringArray(stringArray, isBig, true);
  }

  /**
   * put your documentation comment here
   * @param stringArray
   * @param isBig
   * @param isPhysicalPrint
   * @return
   * @exception JposException, Exception
   */
  protected static PrintReady printStringArray(String[] stringArray, boolean isBig
      , boolean isPhysicalPrint)
      throws JposException, Exception {
    PrintReady printReady = new PrintReady();
    printReady.isFontDoubleWidth(isBig);
    printReady.isCentered(true);
    try {
      if (stringArray != null) {
        StringBuffer line = new StringBuffer();
        StringBuffer printReadyLine = new StringBuffer();
        line.append(CRLF);
        printReadyLine.append(CRLF);
        line.append(ESC + "|cA"); // center the text
        if (isBig) {
          line.append(ESC + "|2C"); // double-width font
        }
        for (int x = 0; x < stringArray.length; x++) {
          line.append(stringArray[x]);
          printReadyLine.append(stringArray[x]);
          line.append(CRLF);
          printReadyLine.append(CRLF);
        }
        //System.out.println("expanded footer to printer: " + line.toString());
        if (isPhysicalPrint) {
          varPrinter.printNormal(PTR_S_RECEIPT, line.toString());
        }
        printReady.setStringToPrint(printReadyLine.toString());
        //            }
      }
    } catch (JposException jpe) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.JPOSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, jpe);
      throw jpe;
    } catch (Exception ex) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.JPOSPrinter", "CMSPrinter()"
          , "Error during receipt printing.", "N/A", LoggingServices.MAJOR, ex);
      throw ex;
    } finally {
      return printReady;
    }
  }

  /**
   * put your documentation comment here
   * @param Name
   * @exception Exception
   */
  public static void openDeviceForTest(String Name)
      throws Exception {
    System.out.println("*******METHOD openDeviceForTest() called:**********");
    System.out.println("*******OPENING PRINTER**********");
    // Open the Device
    boolean opened = false;
    boolean responseToContinue = true;
    try {
      //while (!opened & responseToContinue) {
      varPrinter.open(Name);
      //theAppMgr = app;
      //responseToContinue = false;   // Printer open successful, let's not continue...
      //}
    } catch (JposException e) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter"
          , "openDeviceForTest()", "Cannot open printer.", "N/A", LoggingServices.MAJOR);
      System.out.println("OPEN Printer Code e -> " + e);
      System.out.println("OPEN PrinterError Code -> " + e.getErrorCode());
      System.out.println("OPEN PrinterExt Error Code -> " + e.getErrorCodeExtended());
      System.out.println("OPEN Message -> " + e.getMessage());
      System.out.println("OPEN Localized Message -> " + e.getLocalizedMessage());
      //String errMessage =  "Printer Error:  " + e.getMessage() + "    Do you wish to re-try?";
      //responseToContinue = app.showOptionDlg("Printer Error.", errMessage );
    }
  }

  /**
   * Claim the printer.
   **/
  public static void reclaimForTest()
      throws Exception {
    System.out.println("*******METHOD reclaim() called:**********");
    boolean claimed;
    boolean responseToContinue;
    // Re-Claim the Device
    claimed = false;
    responseToContinue = true;
    //while (!claimed & responseToContinue) {
    try {
      varPrinter.claim(2000);
      claimed = true;
    } catch (JposException e) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "openDevice()"
          , "Cannot Re-claim printer.", "N/A", LoggingServices.MAJOR);
      System.out.println("RE-CLAIM Printer Code e -> " + e);
      System.out.println("RE-CLAIM PrinterError Code -> " + e.getErrorCode());
      System.out.println("RE-CLAIM PrinterExt Error Code -> " + e.getErrorCodeExtended());
      System.out.println("RE-CLAIM Message -> " + e.getMessage());
      System.out.println("Re-CLAIM Localized Message -> " + e.getLocalizedMessage());
      //String errMessage =  "Printer Error:  " + e.getMessage() + ".   Do you wish to re-try?";
      //responseToContinue = app.showOptionDlg("Printer Error.", errMessage);
    }
    //}
    // Re-Enable the Device
    try {
      if (claimed) {
        varPrinter.setDeviceEnabled(true);
        if (printerCharacterSet != 0) {
          varPrinter.setCharacterSet(printerCharacterSet);
          System.out.println("Printer characterset = " + printerCharacterSet);
        }
      }
    } catch (JposException e) {
      LoggingServices.getCurrent().logMsg("com.chelseasystems.receipt.CMSPrinter", "openDevice()"
          , "Cannot Re-Enable printer.", "N/A", LoggingServices.MAJOR);
      System.out.println("RE-ENABLE Printer Code e -> " + e);
      System.out.println("RE-ENABLE PrinterError Code -> " + e.getErrorCode());
      System.out.println("RE-ENABLE PrinterExt Error Code -> " + e.getErrorCodeExtended());
      System.out.println("RE-ENABLE Message -> " + e.getMessage());
      System.out.println("RE-ENABLE Localized Message -> " + e.getLocalizedMessage());
    }
  }

  /**
   * Set the configuration information from the receipt file.
   **/
  public static ReceiptConfigInfo getConfigInfo(ConfigMgr cfg) {
    return receiptInfo;
  }
}

