/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.peripherals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * put your documentation comment here
 */
public class Printer {
  private static Printer printer;
  private jpos.POSPrinter jposPrinter;
  private String printerName;
  private int printerCharacterSet;
  private Properties props;
  private boolean changeCS;

  /**
   * Method to return singleton.
   */
  private static Printer getPrinter() {
    if (printer == null) {
      try {
        printer = new Printer();
      } catch (IOException e) {
        System.err.println("Exception occurred in getPrinter. Error reading properties file: "
            + e.getMessage());
        e.printStackTrace();
      } catch (jpos.JposException e) {
        System.err.println("Exception occurred in getPrinter. Error opening printer: "
            + e.getMessage());
        e.printStackTrace();
      }
    }
    return printer;
  }

  /**
   * put your documentation comment here
   */
  private Printer()
      throws IOException, FileNotFoundException, jpos.JposException {
    this.jposPrinter = this.getJPOSPrinter();
    this.readProperties();
    //this.openAndClaimPrinter();
  }

  /**
   * put your documentation comment here
   * @return
   */
  private jpos.POSPrinter getJPOSPrinter() {
    if (jposPrinter == null) {
      jposPrinter = new jpos.POSPrinter();
    }
    return jposPrinter;
  }

  /**
   * put your documentation comment here
   * @exception FileNotFoundException, IOException
   */
  private void readProperties()
      throws FileNotFoundException, IOException {
    props = new Properties();
    String fileName = this.getPropertyFileName();
    FileInputStream in = new FileInputStream(fileName);
    props.load(in);
    in.close();
    this.printerName = props.getProperty("LOGICAL_PRINTER_NAME");
    this.printerCharacterSet = Integer.parseInt(props.getProperty("PRINTER_CHARACTER_SET"));
    String cs = null;
    if (this.printerCharacterSet != 0) {
      this.changeCS = true;
      cs = "" + this.printerCharacterSet;
    } else {
      cs = "default";
    }
    System.out.println("Printer to open: " + printerName);
    System.out.println("Character set to use: " + cs);
    System.out.println("Change character set: " + this.changeCS);
  }

  /**
   * put your documentation comment here
   * @return
   */
  private String getPropertyFileName() {
    String separator = System.getProperty("file.separator");
    char sep = separator.trim().charAt(0);
    String fileName = ".." + sep + "files" + sep + "prod" + sep + "config" + sep
        + "JPOS_peripherals.cfg";
    System.out.println("Property file: " + fileName);
    return fileName;
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  public static void openClaimEnablePrinter()
      throws jpos.JposException {
    Printer printer = getPrinter();
    printer.getJPOSPrinter().open(printer.printerName);
    printer.getJPOSPrinter().claim(3000);
    printer.getJPOSPrinter().setDeviceEnabled(true);
    if (printer.changeCS) {
      printer.getJPOSPrinter().setCharacterSet(printer.printerCharacterSet);
      System.out.println("Printer using character set: " + printer.printerCharacterSet);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static String getPrinterName() {
    return getPrinter().printerName;
  }

  /**
   * This method returns the current character set of the printer.
   */
  public static int getPrinterCharacterSet()
      throws jpos.JposException {
    return getPrinter().getJPOSPrinter().getCharacterSet();
  }

  /**
   * This method returns the installed character sets of the printer.
   */
  public static String getPrinterCharacterList()
      throws jpos.JposException {
    return getPrinter().getJPOSPrinter().getCharacterSetList();
  }

  /**
   * put your documentation comment here
   * @param s
   * @exception jpos.JposException
   */
  public static void printLn(String s)
      throws jpos.JposException {
    getPrinter().getJPOSPrinter().printNormal(jpos.POSPrinterConst.PTR_S_RECEIPT, s + '\n');
  }

  /**
   * put your documentation comment here
   * @param s
   * @exception jpos.JposException
   */
  public static void printBarCode(String s)
      throws jpos.JposException {
    int symbology = jpos.POSPrinterConst.PTR_BCS_Code39;
    //int symbology = jpos.POSPrinterConst.PTR_BCS_Codabar;
    //int symbology = jpos.POSPrinterConst.PTR_PTR_BCS_EAN128;
    int type = jpos.POSPrinterConst.PTR_S_RECEIPT;
    //int type = jpos.POSPrinterConst.PTR_S_SLIP;
    //int type = jpos.POSPrinterConst.PTR_S_JOURNAL;
    getPrinter().getJPOSPrinter().printBarCode(type, s, symbology, 100, 200
        , jpos.POSPrinterConst.PTR_BC_CENTER, jpos.POSPrinterConst.PTR_BC_TEXT_BELOW);
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  public static void cutPaper()
      throws jpos.JposException {
    getPrinter().getJPOSPrinter().cutPaper(95);
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  public static void close()
      throws jpos.JposException {
    getPrinter().getJPOSPrinter().setDeviceEnabled(false);
    getPrinter().getJPOSPrinter().release();
    getPrinter().getJPOSPrinter().close();
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  public static void openPrinter()
      throws jpos.JposException {
    getPrinter().getJPOSPrinter().open(getPrinter().printerName);
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  public static void claimPrinter()
      throws jpos.JposException {
    getPrinter().getJPOSPrinter().claim(3000);
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  public static void enablePrinter()
      throws jpos.JposException {
    getPrinter().getJPOSPrinter().setDeviceEnabled(true);
  }

  /**
   * put your documentation comment here
   * @param characterSet
   * @exception jpos.JposException
   */
  public static void setCharacterSet(int characterSet)
      throws jpos.JposException {
    getPrinter().getJPOSPrinter().setCharacterSet(characterSet);
  }
}

