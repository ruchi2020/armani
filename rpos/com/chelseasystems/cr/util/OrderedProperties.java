/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright © 2004 Retek Inc.  All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.util;

import java.io.*;
import java.util.*;


/**
 * put your documentation comment here
 */
public class OrderedProperties extends Properties {

  /**
   * put your documentation comment here
   * @return
   */
  public Iterator keyIterator() {
    ArrayList list = new ArrayList();
    for (Enumeration e = keys(); e.hasMoreElements(); ) {
      list.add(e.nextElement());
    }
    Collections.sort(list);
    return list.iterator();
  }

  /**
   * put your documentation comment here
   * @param out
   * @param header
   * @exception IOException
   */
  public synchronized void storeOrderedByKeys(OutputStream out, String header)
      throws IOException {
    BufferedWriter awriter;
    awriter = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
    if (header != null) {
      writeln(awriter, "#" + header);
    }
    //writeln(awriter, "#" + new Date().toString());
    for (Iterator it = this.keyIterator(); it.hasNext(); ) {
      String key = (String)it.next();
      String val = (String)get(key);
      key = saveConvert(key, true);
      /* No need to escape embedded and trailing spaces for value, hence
       * pass false to flag.
       */
      val = saveConvert(val, false);
      writeln(awriter, key + "=" + val);
    }
    awriter.flush();
  }

  /**
   * put your documentation comment here
   * @param bw
   * @param s
   * @exception IOException
   */
  private static void writeln(BufferedWriter bw, String s)
      throws IOException {
    bw.write(s);
    bw.newLine();
  }

  /**
   * put your documentation comment here
   * @param theString
   * @param escapeSpace
   * @return
   */
  private String saveConvert(String theString, boolean escapeSpace) {
    int len = theString.length();
    StringBuffer outBuffer = new StringBuffer(len * 2);
    for (int x = 0; x < len; x++) {
      char aChar = theString.charAt(x);
      switch (aChar) {
        case ' ':
          if (x == 0 || escapeSpace) {
            outBuffer.append('\\');
          }
          outBuffer.append(' ');
          break;
        case '\\':
          outBuffer.append('\\');
          outBuffer.append('\\');
          break;
        case '\t':
          outBuffer.append('\\');
          outBuffer.append('t');
          break;
        case '\n':
          outBuffer.append('\\');
          outBuffer.append('n');
          break;
        case '\r':
          outBuffer.append('\\');
          outBuffer.append('r');
          break;
        case '\f':
          outBuffer.append('\\');
          outBuffer.append('f');
          break;
        default:
          if ((aChar < 0x0020) || (aChar > 0x007e)) {
            outBuffer.append('\\');
            outBuffer.append('u');
            outBuffer.append(toHex((aChar >> 12) & 0xF));
            outBuffer.append(toHex((aChar >> 8) & 0xF));
            outBuffer.append(toHex((aChar >> 4) & 0xF));
            outBuffer.append(toHex(aChar & 0xF));
          } else {
            if (specialSaveChars.indexOf(aChar) != -1) {
              outBuffer.append('\\');
            }
            outBuffer.append(aChar);
          }
      }
    }
    return outBuffer.toString();
  }

  /**
   * put your documentation comment here
   * @param nibble
   * @return
   */
  private static char toHex(int nibble) {
    return hexDigit[(nibble & 0xF)];
  }

  private static final char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A'
      , 'B', 'C', 'D', 'E', 'F'
  };
  private static final String specialSaveChars = "=: \t\r\n\f#!";
}

