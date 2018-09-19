// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) definits braces deadcode noctor radix(10) lradix(10)
// Source File Name:   PrinterCodeConverter.java

package com.chelseasystems.rb;

import com.chelseasystems.cr.config.ConfigMgr;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class PrinterCodeConverter {
    private static PrinterCodeConverter pcc = null;
    private static final int max = 256;
    public char charArray[] = null;
    private boolean swap = false;
    private ConfigMgr config = null;


    public static boolean isCharacterConversionNeeded() {
        return getPrinterCodeConverter().swap;
    }

    public static char switchCharacter(char c) {
        if (getPrinterCodeConverter().swap) {
            if (c > '\377') {
                return c;
            } else {
                char swapped = getPrinterCodeConverter().charArray[c];
                return swapped != '\uFFFF' ? swapped : c;
            }
        } else {
            return c;
        }
    }

    public PrinterCodeConverter() {
        config = new ConfigMgr("JPOS_peripherals.cfg");
        swap = setSwap();
        if (swap) {
            initArray();
            readCharacterToSwap();
        }
    }

    private void readCharacterToSwap() {
        String s = config.getString("ANSI_CODES_TO_CHANGE");
        if (s == null) {
            swap = false;
            return;
        }
        for (StringTokenizer tokens = new StringTokenizer(s, ",");
                                      tokens.hasMoreTokens(); ) {
            String key = tokens.nextToken().trim();
            char value = getValueToTranslate(key);
            char c = key.charAt(0);
            if (key.length() == 2) {
                c = (char) Integer.parseInt(key, 16);
            }
            charArray[c] = value;
      //System.out.println(c + " : 0x" + Integer.toHexString(c) + " translated to: 0x" + Integer.toHexString(value));
        }

    }

    private char getValueToTranslate(String key) {
        String s = config.getString(key);
        int i = Integer.parseInt(s, 16);
        return (char) i;
    }

  /**
   * put your documentation comment here
   */
    private void initArray() {
        charArray = new char[max];
        char initial = '\uFFFF';
        for (int i = 0; i < max; i++) {
            charArray[i] = initial;
        }

    }

    private boolean setSwap() {
        String swap = config.getString("SWAP_PRINTER_CODES");
        if (swap == null) {
      //            System.out.println("Config Key: SWAP_PRINTER_CODES could not be read... ");
            return false;
        } else {
            return swap.equalsIgnoreCase("true");
        }
    }

  /**
   * This return the singleton for the class
   * @return  singleton object of type PrinterCodeConverter
   */
    private static PrinterCodeConverter getPrinterCodeConverter() {
        if (pcc == null) {
            pcc = new PrinterCodeConverter();
        }
        return pcc;
    }

  /**
   * Main routine used to test program.
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("Running PrinterCodeConverter...");
    System.out.println("A : 0x" + Integer.toHexString('A') + " translated to: "
        + PrinterCodeConverter.switchCharacter('A'));
    System.out.println("t : 0x" + Integer.toHexString('t') + " translated to: "
        + PrinterCodeConverter.switchCharacter('t'));
    System.out.println("c : 0x" + Integer.toHexString('c') + " translated to: "
        + PrinterCodeConverter.switchCharacter('c'));
    System.out.println("4 : 0x" + Integer.toHexString('4') + " translated to: "
        + PrinterCodeConverter.switchCharacter('4'));
    System.out.println("9 : 0x" + Integer.toHexString('9') + " translated to: "
        + PrinterCodeConverter.switchCharacter('9'));
    System.out.println("* : 0x" + Integer.toHexString('*') + " translated to: "
        + PrinterCodeConverter.switchCharacter('*'));
    char[] array = {'à', 'À', 'â', 'Â', 'ä', 'Â', 'æ', 'Æ', 'ç', 'Ç', 'é', 'É', 'è', 'È', 'ê', 'Ê'
        , 'ë', 'Ë', 'î', 'Î', 'ï', 'Ï', 'ô', 'Ô', 'ò', 'Ò', 'ö', 'Ö', 'ù', 'Ù', 'û', 'Û', 'ü', 'Ü'
    };
    for (int i = 0; i < array.length; i++) {
      char ac = array[i];
      char c = PrinterCodeConverter.switchCharacter(ac);
      System.out.println(ac + " : " + Integer.toString(ac) + " translated to: 0x"
          + Integer.toHexString(c));
    }
  }
}
