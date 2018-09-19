/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.peripherals;

import com.chelseasystems.cr.peripherals.Printer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;
import sun.tools.tree.ThisExpression;


/**
 * put your documentation comment here
 */
public class TestJPOSPrinter {
  private Properties props;
  private Double amount;
  private Locale[] locales;
  private String[] definedStrings;
  private String javaCharacterSet;

  /**
   * put your documentation comment here
   */
  public TestJPOSPrinter() {
    try {
      this.init();
      this.start();
      this.stop();
    } catch (jpos.JposException e) {
      System.err.println("Exception occurred in TestJPOSPrinter. Printer exception occurred: "
          + e.getErrorCode());
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      System.err.println("Exception occurred in TestJPOSPrinter. Property file was not found: "
          + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("Exception occurred in TestJPOSPrinter. IOException exception occurred: "
          + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Exception occurred in TestJPOSPrinter. " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  private void init() {
    this.createDefinedStrings();
    this.createLocales();
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void printBarCode()
      throws jpos.JposException {
    this.printBlanks();
    this.print("----> Start");
    Printer.printBarCode("80743866");
    Printer.printBarCode("10743866");
    Printer.printBarCode("12345678");
    Printer.printBarCode("012345678");
    Printer.printBarCode("012345678");
    Printer.printBarCode("101234567");
    Printer.printBarCode("10123456");
    Printer.printBarCode("1012345");
    Printer.printBarCode("101234");
    Printer.printBarCode("10123");
    Printer.printBarCode("1012");
    Printer.printBarCode("101");
    Printer.printBarCode("10");
    Printer.printBarCode("1");
    this.print("----> End");
    this.printBlanks();
  }

  /**
   * put your documentation comment here
   * @exception FileNotFoundException, IOException, jpos.JposException, Exception
   */
  private void start()
      throws FileNotFoundException, IOException, jpos.JposException, Exception {
    this.readProperties();
    this.openAndClaimPrinter();
    this.printBlanks();
    this.printBarCode();
    this.printBlanks();
    //        this.printStringArray();
    //        this.printLocaleArray();
    //        this.printLast125Characters();
    //        this.printLocales();
    //        this.printBlanks();
    //        this.printBlanks();
    Printer.cutPaper();
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void openAndClaimPrinter()
      throws jpos.JposException {
    Printer.openClaimEnablePrinter();
    this.printBlanks();
    this.printLine();
    this.printToBoth("Printer name: " + Printer.getPrinterName());
    this.printToBoth("Printer Character set is: " + Printer.getPrinterCharacterSet());
    this.printToConsole("Printer Character List: " + Printer.getPrinterCharacterList());
    this.printToBoth("The currency to use: " + amount);
    this.printToBoth("Java Character set is: " + this.javaCharacterSet);
    this.printToBoth("The printer is opened...");
    this.printLine();
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void stop()
      throws jpos.JposException {
    Printer.close();
  }

  /**
   * put your documentation comment here
   * @exception FileNotFoundException, IOException
   */
  private void readProperties()
      throws FileNotFoundException, IOException {
    props = new Properties();
    FileInputStream in = new FileInputStream("testJPOS.properties");
    props.load(in);
    in.close();
    this.amount = new Double(props.getProperty("AMOUNT"));
    this.javaCharacterSet = props.getProperty("JAVA_CHARACTER_SET");
  }

  /**
   * put your documentation comment here
   */
  private void createDefinedStrings() {
    this.definedStrings = new String[11];
    this.definedStrings[0] = "00D5: �";
    this.definedStrings[1] = "009C: ?";
    this.definedStrings[2] = "00BE: �";
    this.definedStrings[3] = "20AC: �";
    this.definedStrings[4] = "00A3: �";
    this.definedStrings[5] = "00A4: �";
    this.definedStrings[6] = "00A5: �";
    this.definedStrings[7] = "003F: ?";
    this.definedStrings[8] = "0153: �";
    this.definedStrings[9] = "FFE5: ?";
    this.definedStrings[10] = "00CF: �";
  }

  /**
   * put your documentation comment here
   */
  private void createLocales() {
    locales = new Locale[5];
    locales[0] = new Locale("en", "US");
    locales[1] = new Locale("en", "GB");
    locales[2] = new Locale("en", "IE", "EURO"); //Ireland,EURO
    locales[3] = new Locale("de", "EURO");
    locales[4] = new Locale("ja", "JP");
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void printLocaleArray()
      throws jpos.JposException {
    this.printBlanks();
    this.printToBoth("Printing Java Locale Objects");
    this.printToBoth("First line is the name of locale");
    this.printToBoth("Second line is the default locale");
    this.printToBoth("Third line converted to: " + this.javaCharacterSet);
    for (int i = 0; i < locales.length; i++) {
      this.printToBoth(locales[i].toString());
      String s = this.formatLocale(locales[i]);
      this.printToBoth(this.convertToHex(s));
      this.printToBoth(convertToHex(this.changeCS(s)));
      this.printToBoth("");
    }
    this.printLine();
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void printStringArray()
      throws jpos.JposException {
    this.printBlanks();
    this.printToBoth("Printing String Array");
    this.printToBoth("First line is the string.");
    this.printToBoth("Second line uses: " + this.javaCharacterSet);
    for (int i = 0; i < definedStrings.length; i++) {
      String s = this.convertToHex(definedStrings[i]);
      String s1 = this.convertToHex(this.changeCS(definedStrings[i]));
      this.printToBoth(s);
      this.printToBoth(s1);
      this.printToBoth("");
    }
    this.printLine();
  }

  /**
   * put your documentation comment here
   * @param locale
   * @return
   */
  private String formatLocale(Locale locale) {
    NumberFormat cf = NumberFormat.getCurrencyInstance(locale);
    String format = cf.format(amount);
    return (format);
  }

  /**
   * put your documentation comment here
   * @param s
   * @return
   */
  private String changeCS(String s) {
    try {
      byte[] converted = s.getBytes(this.javaCharacterSet);
      return new String(converted);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * return the initial string with its hex value append.
   *
   * @param s
   * @return String the Formatted string
   */
  private String convertToHex(String s) {
    String buf = new String(s) + "- Hex: ";
    for (int i = 0; i < s.length(); i++) {
      buf = buf + " " + Integer.toHexString(s.charAt(i));
    }
    return buf;
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void printBlanks()
      throws jpos.JposException {
    this.printToBoth("");
    this.printToBoth("");
    this.printToBoth("");
    this.printToBoth("");
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void printLine()
      throws jpos.JposException {
    this.printToBoth("-----------------------------");
  }

  /**
   * put your documentation comment here
   * @param s
   * @exception jpos.JposException
   */
  private void printToBoth(String s)
      throws jpos.JposException {
    this.printToConsole(s);
    this.print(s);
  }

  /**
   * put your documentation comment here
   * @param s
   */
  private void printToConsole(String s) {
    System.out.println(s);
  }

  /**
   * put your documentation comment here
   * @param s
   * @exception jpos.JposException
   */
  private void print(String s)
      throws jpos.JposException {
    Printer.printLn(s);
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void printLocales()
      throws jpos.JposException {
    if (props.getProperty("PRINT_ALL_LOCALES").equalsIgnoreCase("false")) {
      return;
    }
    Locale[] locales = Locale.getAvailableLocales();
    for (int i = 0; i < locales.length; i++) {
      Locale locale = locales[i];
      this.printToBoth("Available Locale: " + locale.getDisplayName());
    }
  }

  /**
   * put your documentation comment here
   * @exception jpos.JposException
   */
  private void printLast125Characters()
      throws jpos.JposException {
    if (props.getProperty("PRINT_EXTENDED_ASCII").equalsIgnoreCase("false")) {
      return;
    }
    this.printToBoth("0080: " + '?');
    this.printToBoth("0081: " + '?');
    this.printToBoth("0082: " + '?');
    this.printToBoth("0083: " + '?');
    this.printToBoth("0084: " + '?');
    this.printToBoth("0085: " + '?');
    this.printToBoth("0086: " + '?');
    this.printToBoth("0087: " + '?');
    this.printToBoth("0088: " + '?');
    this.printToBoth("0089: " + '?');
    this.printToBoth("008A: " + '?');
    this.printToBoth("008B: " + '?');
    this.printToBoth("008C: " + '?');
    this.printToBoth("008D: " + '?');
    this.printToBoth("008E: " + '?');
    this.printToBoth("008F: " + '?');
    this.printToBoth("0090: " + '?');
    this.printToBoth("0091: " + '?');
    this.printToBoth("0092: " + '?');
    this.printToBoth("0093: " + '?');
    this.printToBoth("0094: " + '?');
    this.printToBoth("0095: " + '?');
    this.printToBoth("0096: " + '?');
    this.printToBoth("0097: " + '?');
    this.printToBoth("0098: " + '?');
    this.printToBoth("0099: " + '?');
    this.printToBoth("009A: " + '?');
    this.printToBoth("009B: " + '?');
    this.printToBoth("009C: " + '?');
    this.printToBoth("009D: " + '?');
    this.printToBoth("009E: " + '?');
    this.printToBoth("009F: " + '?');
    this.printToBoth("00A0: " + '�');
    this.printToBoth("00A1: " + '�');
    this.printToBoth("00A2: " + '�');
    this.printToBoth("00A3: " + '�');
    this.printToBoth("00A4: " + '�');
    this.printToBoth("00A5: " + '�');
    this.printToBoth("00A6: " + '�');
    this.printToBoth("00A7: " + '�');
    this.printToBoth("00A8: " + '�');
    this.printToBoth("00A9: " + '�');
    this.printToBoth("00AA: " + '�');
    this.printToBoth("00AB: " + '�');
    this.printToBoth("00AC: " + '�');
    this.printToBoth("00AD: " + '�');
    this.printToBoth("00AE: " + '�');
    this.printToBoth("00AF: " + '�');
    this.printToBoth("00B0: " + '�');
    this.printToBoth("00B1: " + '�');
    this.printToBoth("00B2: " + '�');
    this.printToBoth("00B3: " + '�');
    this.printToBoth("00B4: " + '�');
    this.printToBoth("00B5: " + '�');
    this.printToBoth("00B6: " + '�');
    this.printToBoth("00B7: " + '�');
    this.printToBoth("00B8: " + '�');
    this.printToBoth("00B9: " + '�');
    this.printToBoth("00BA: " + '�');
    this.printToBoth("00BB: " + '�');
    this.printToBoth("00BC: " + '�');
    this.printToBoth("00BD: " + '�');
    this.printToBoth("00BE: " + '�');
    this.printToBoth("00BF: " + '�');
    this.printToBoth("00C0: " + '�');
    this.printToBoth("00C1: " + '�');
    this.printToBoth("00C2: " + '�');
    this.printToBoth("00C3: " + '�');
    this.printToBoth("00C4: " + '�');
    this.printToBoth("00C5: " + '�');
    this.printToBoth("00C6: " + '�');
    this.printToBoth("00C7: " + '�');
    this.printToBoth("00C8: " + '�');
    this.printToBoth("00C9: " + '�');
    this.printToBoth("00CA: " + '�');
    this.printToBoth("00CB: " + '�');
    this.printToBoth("00CC: " + '�');
    this.printToBoth("00CD: " + '�');
    this.printToBoth("00CE: " + '�');
    this.printToBoth("00CF: " + '�');
    this.printToBoth("00D0: " + '�');
    this.printToBoth("00D1: " + '�');
    this.printToBoth("00D2: " + '�');
    this.printToBoth("00D3: " + '�');
    this.printToBoth("00D4: " + '�');
    this.printToBoth("00D5: " + '�');
    this.printToBoth("00D6: " + '�');
    this.printToBoth("00D7: " + '�');
    this.printToBoth("00D8: " + '�');
    this.printToBoth("00D9: " + '�');
    this.printToBoth("00DA: " + '�');
    this.printToBoth("00DB: " + '�');
    this.printToBoth("00DC: " + '�');
    this.printToBoth("00DD: " + '�');
    this.printToBoth("00DE: " + '�');
    this.printToBoth("00DF: " + '�');
    this.printToBoth("00E0: " + '�');
    this.printToBoth("00E1: " + '�');
    this.printToBoth("00E2: " + '�');
    this.printToBoth("00E3: " + '�');
    this.printToBoth("00E4: " + '�');
    this.printToBoth("00E5: " + '�');
    this.printToBoth("00E6: " + '�');
    this.printToBoth("00E7: " + '�');
    this.printToBoth("00E8: " + '�');
    this.printToBoth("00E9: " + '�');
    this.printToBoth("00EA: " + '�');
    this.printToBoth("00EB: " + '�');
    this.printToBoth("00EC: " + '�');
    this.printToBoth("00ED: " + '�');
    this.printToBoth("00EE: " + '�');
    this.printToBoth("00EF: " + '�');
    this.printToBoth("00F0: " + '�');
    this.printToBoth("00F1: " + '�');
    this.printToBoth("00F2: " + '�');
    this.printToBoth("00F3: " + '�');
    this.printToBoth("00F4: " + '�');
    this.printToBoth("00F5: " + '�');
    this.printToBoth("00F6: " + '�');
    this.printToBoth("00F7: " + '�');
    this.printToBoth("00F8: " + '�');
    this.printToBoth("00F9: " + '�');
    this.printToBoth("00FA: " + '�');
    this.printToBoth("00FB: " + '�');
    this.printToBoth("00FC: " + '�');
    this.printToBoth("00FD: " + '�');
    this.printToBoth("00FE: " + '�');
    this.printToBoth("00FF: " + '�');
    this.printToBoth("20A0: " + '?');
    this.printToBoth("20A1: " + '?');
    this.printToBoth("20A2: " + '?');
    this.printToBoth("20A3: " + '?');
    this.printToBoth("20A4: " + '?');
    this.printToBoth("20A5: " + '?');
    this.printToBoth("20A6: " + '?');
    this.printToBoth("20A7: " + '?');
    this.printToBoth("20A8: " + '?');
    this.printToBoth("20A9: " + '?');
    this.printToBoth("20AA: " + '?');
    this.printToBoth("20AB: " + '?');
    this.printToBoth("20AC: " + '�');
    this.printToBoth("20AD: " + '?');
    this.printToBoth("20AE: " + '?');
    this.printToBoth("20AA: " + '?');
  }

  /**
   * put your documentation comment here
   * @param args
   */
  public static void main(String[] args) {
    new TestJPOSPrinter();
    System.exit( -1);
  }
}

