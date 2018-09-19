/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright © 2004 Retek Inc.  All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.util.*;
import java.io.*;
import java.util.*;
import jxl.Sheet;
import jxl.Workbook;
import jxl.Cell;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.Alignment;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WritableCellFormat;
import jxl.write.Label;


/**
 * <p>Title: MicrosoftExcelUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author
 * @version 1.0
 */
public class MicrosoftExcelUtil {
  /**
   * Used to store the value of message key
   */
  private static String MESSAGE_KEY_STRING = "MESSAGE_KEY";

  /**
   * main method used to execute mrthods of this class to create resource bundle
   * @param args String[]
   * @throws Exception
   */
  public static void main(String[] args)
      throws Exception {
    String command = "";
    String filename = "";
    if (args == null || args.length == 0) {
      System.out.println("\nUsage: resource.bat <spreadsheet filename> -bundle");
      System.out.println(
          "                (to update the properties files from the original format spreadsheet)\n");
      System.out.println("    or resource.bat <spreadsheet filename> -newbundle");
      System.out.println(
          "                (to update the properties files from new format spreadsheet)\n");
      System.out.println("    or resource.bat <spreadsheet filename> -xls");
      System.out.println(
          "                (reverse, to update the Excel file to the original format spreadsheet)\n");
      System.out.println("    or resource.bat -filename <spreadsheet filename> -newxls");
      System.out.println(
          "                (reverse, to update the Excel file to the new format spreadsheet)\n");
    } else {
      if (args[1].compareToIgnoreCase("-bundle") == 0) {
        System.out.println("coming");
        updateResourceBundleFiles(new File(args[0]));
      } else if (args[1].compareToIgnoreCase("-newbundle") == 0) {
        updateResourceBundleFilesFromNewWorkbook(new File(args[0]));
      } else if (args[1].compareToIgnoreCase("-xls") == 0) {
        writeResourceBundlesToWorkbook(new File(args[0]));
      } else if (args[1].compareToIgnoreCase("-newxls") == 0) {
        writeResourceBundlesToNewWorkbook(new File(args[0]));
      } else if (args[1].compareToIgnoreCase("-compare") == 0) {
        compareWorkbooks(new File(args[0]), new File(args[2]));
      }
    }
    System.exit(0);
  }

  /**
   * Used to update ResourceBundle files
   * @param excelFile File
   * @throws Exception
   */
  public static void updateResourceBundleFiles(File excelFile)
      throws Exception {
    Workbook book = Workbook.getWorkbook(excelFile);
    Sheet[] sheets = book.getSheets();
    for (int i = 0; i < sheets.length; i++) {
      File file = new File("../src/com/chelseasystems/cs/util/" + sheets[i].getName().trim()
          + ".properties");
      //System.out.println("Writing to: " + file.getName());
      String header = sheets[i].getName().trim() + ".properties";
      OrderedProperties prop = toProperties(sheets[i], 0, 1, 0);
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
      prop.storeOrderedByKeys(bos, header);
    }
  }

  /**
   * Used to update ResourceBundle files from new Workbook
   * @param excelFile File
   * @throws Exception
   */
  public static void updateResourceBundleFilesFromNewWorkbook(File excelFile)
      throws Exception {
    Workbook book = Workbook.getWorkbook(excelFile);
    Sheet[] sheets = book.getSheets();
    //System.out.println("sheet " + sheets.length);
    // For each sheet in the workbook
    for (int sheetIndex = 0; sheetIndex < sheets.length; sheetIndex++) {
      //System.out.println("sheet " + sheetIndex);
      // For each column in the sheet
      //System.out.println("sheet:columns " + sheets[sheetIndex].getColumns());
      for (int colIndex = 1; colIndex < sheets[sheetIndex].getColumns(); colIndex++) {
        if (sheets[sheetIndex].getRow(0).length > colIndex) {
          String columnHeader = sheets[sheetIndex].getRow(0)[colIndex].getContents().trim();
          if ((columnHeader != null) && (columnHeader.length() > 0)) {
            // Get the column name to determine the file name and create the file
            File file = new File("../src/com/chelseasystems/cs/util/" + columnHeader
                + ".properties");
            //System.out.println("Writing to: " + file.getName());
            // Get the ordered properties
            OrderedProperties prop = toProperties(sheets[sheetIndex], 0, colIndex, 1);
            // Create the buffered output stream
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            // Store the properties
            prop.storeOrderedByKeys(bos, file.getName());
          }
        }
      }
    }
  }

  /**
   * Used to write Resource Bundle to new Workbook
   * @param twoColumnSheet Sheet
   * @param keyCol int
   * @param valueCol int
   * @param startRow int
   * @return prop OrderedProperties
   */
  public static OrderedProperties toProperties(Sheet twoColumnSheet, int keyCol, int valueCol
      , int startRow) {
    OrderedProperties prop = new OrderedProperties();
    for (int i = startRow; i < twoColumnSheet.getRows(); i++) {
      Cell[] cellsInRow = twoColumnSheet.getRow(i);
      String key = cellsInRow[keyCol].getContents();
      //System.out.println("key " + key);
      String value = cellsInRow[valueCol].getContents();
      if (value.length() > 0) {
        prop.put(key, value);
      }
    }
    return prop;
  }

  /**
   * Used to write Resource Bundle to Workbook
   * @param outputFile File
   * @throws IOException
   */
  public static void writeResourceBundlesToWorkbook(File outputFile)
      throws java.io.IOException {
    String[] classNames = getTokens(new ConfigMgr("client_master.cfg").getString("MESSAGE_BUNDLE"));
    String[] sheetNames = new String[classNames.length * armaniLocales.length];
    OrderedProperties[] properties = new OrderedProperties[classNames.length * armaniLocales.length];
    for (int i = 0; i < classNames.length; i++) {
      for (int j = 0; j < armaniLocales.length; j++) {
        String classLassName = classNames[i].substring(classNames[i].lastIndexOf(".") + 1);
        String localeName = armaniLocales[j].toString().trim();
        if (localeName.length() > 0) {
          localeName = "_" + localeName;
        }
        sheetNames[i * armaniLocales.length + j] = classLassName + localeName;
        ResourceBundle res = ResourceBundle.getBundle(classNames[i], armaniLocales[j]);
        UpdateableResourceBundle updateableRes = new UpdateableResourceBundle(res);
        properties[i * armaniLocales.length + j] = updateableRes.toProperties();
      }
    }
    //
    createResourceBundleWorkbook(properties, sheetNames, outputFile);
  }

  /**
   * Used to write Resource Bundle to new Workbook
   * @param outputFile File
   * @throws IOException
   */
  public static void writeResourceBundlesToNewWorkbook(File outputFile)
      throws java.io.IOException {
    String[] classNames = getTokens(new ConfigMgr("client_master.cfg").getString("MESSAGE_BUNDLE"));
    String[] sheetNames = new String[classNames.length];
    OrderedProperties[][] properties = new OrderedProperties[classNames.length][armaniLocales.
        length];
    // For each of the class names
    for (int i = 0; i < classNames.length; i++) {
      // Get the first class name
      String className = classNames[i].substring(classNames[i].lastIndexOf(".") + 1);
      // Make the sheet name the class name
      sheetNames[i] = className;
      // For each of the locales
      for (int j = 0; j < armaniLocales.length; j++) {
        String localeName = armaniLocales[j].toString().trim();
        if (localeName.length() > 0) {
          localeName = "_" + localeName;
        }
        ResourceBundle res = ResourceBundle.getBundle(classNames[i], armaniLocales[j]);
        UpdateableResourceBundle updateableRes = new UpdateableResourceBundle(res);
        properties[i][j] = updateableRes.toProperties();
        properties[i][j].put(MESSAGE_KEY_STRING, className + localeName);
      }
    }
    createResourceBundleNewWorkbook(mungeProperties(properties), sheetNames, outputFile);
  }

  /**
   * Used to create Resource Bundle Workbook
   * @param propertiesArray OrderedProperties[]
   * @param sheetNames String[]
   * @param outputFile File
   * @throws IOException
   */
  public static void createResourceBundleWorkbook(OrderedProperties[] propertiesArray
      , String[] sheetNames, File outputFile)
      throws java.io.IOException {
    WorkbookSettings settings = new WorkbookSettings();
    settings.setEncoding("UTF8");
    WritableWorkbook book = Workbook.createWorkbook(outputFile, settings);
    for (int i = 0; i < propertiesArray.length; i++) {
      //System.out.println("Generating sheet [" + (i + 1) + "]: " + sheetNames[i]);
      WritableSheet sheet = book.createSheet(sheetNames[i], i + 1);
      writeToTwoColunmSheet(sheet, propertiesArray[i]);
    }
    //System.out.println("\nWriting to Excel file: " + outputFile);
    book.write();
    book.close();
  }

  /**
   * Used to create Resource Bundle New Workbook
   * @param propertiesArray OrderedProperties[]
   * @param sheetNames String[]
   * @param outputFile File
   * @throws IOException
   */
  public static void createResourceBundleNewWorkbook(OrderedProperties[] propertiesArray
      , String[] sheetNames, File outputFile)
      throws java.io.IOException {
    WorkbookSettings settings = new WorkbookSettings();
    settings.setEncoding("UTF8");
    WritableWorkbook book = Workbook.createWorkbook(outputFile, settings);
    for (int i = 0; i < propertiesArray.length; i++) {
      //System.out.println("Generating sheet [" + (i + 1) + "]: " + sheetNames[i]);
      WritableSheet sheet = book.createSheet(sheetNames[i], i + 1);
      writeToMultiColumnSheet(sheet, propertiesArray[i]);
    }
    //System.out.println("\nWriting to Excel file: " + outputFile);
    book.write();
    book.close();
  }

  /**
   * Used to write to two Colunm Sheet
   * @param sheet WritableSheet
   * @param properties OrderedProperties
   * @throws IOException
   */
  public static void writeToTwoColunmSheet(WritableSheet sheet, OrderedProperties properties)
      throws java.io.IOException {
    try {
      WritableCellFormat wrapFormat = new WritableCellFormat();
      wrapFormat.setWrap(true);
      int R1 = 0;
      int R2 = 1;
      sheet.setColumnView(R1, 60);
      sheet.setColumnView(R2, 60);
      int i = 0;
      for (Iterator it = properties.keyIterator(); it.hasNext(); ) {
        String key = (String)it.next();
        String value = (String)properties.get(key);
        sheet.addCell(new Label(R1, i, key, wrapFormat));
        sheet.addCell(new Label(R2, i, value, wrapFormat));
        i++;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      throw new java.io.IOException(exception.getMessage());
    }
  }

  /**
   * Used to write to Multi Column Sheet
   * @param sheet WritableSheet
   * @param properties WritableSheet
   * @throws IOException
   */
  public static void writeToMultiColumnSheet(WritableSheet sheet, OrderedProperties properties)
      throws java.io.IOException {
    try {
      // Format for first column non header cell
      WritableCellFormat firstCellFormat = new WritableCellFormat();
      firstCellFormat.setWrap(true);
      firstCellFormat.setBackground(Colour.GREY_25_PERCENT);
      // Format for non first column non header cell
      WritableCellFormat cellFormat = new WritableCellFormat();
      cellFormat.setWrap(true);
      // Need to get the basic font out first
      Font currentFont = cellFormat.getFont();
      // Now we need to create a bold font
      WritableFont headerBoldFont = new WritableFont(currentFont);
      headerBoldFont.setBoldStyle(WritableFont.BOLD);
      // Format for Header Cell First Column (Bold, Centered, Grey 25% Background)
      WritableCellFormat headerFirstCellFormat = new WritableCellFormat(headerBoldFont);
      headerFirstCellFormat.setWrap(true);
      headerFirstCellFormat.setAlignment(Alignment.CENTRE);
      headerFirstCellFormat.setBackground(Colour.GREY_25_PERCENT);
      // Format for Header Cell non-first column (Bold, Centered)
      WritableCellFormat headerCellFormat = new WritableCellFormat(headerBoldFont);
      headerCellFormat.setWrap(true);
      headerCellFormat.setAlignment(Alignment.CENTRE);
      int row = 0;
      String[] headerRow = ((LocaleValues)properties.get(MESSAGE_KEY_STRING)).getValues();
      for (int i = 0; i <= armaniLocales.length; i++) {
        sheet.setColumnView(i, 60);
        String value = "";
        WritableCellFormat tempCellFormat;
        if (i == 0) {
          value = MESSAGE_KEY_STRING;
          tempCellFormat = headerFirstCellFormat;
        } else {
          value = headerRow[i - 1];
          tempCellFormat = headerCellFormat;
        }
        sheet.addCell(new Label(i, row, value, tempCellFormat));
      }
      properties.remove(MESSAGE_KEY_STRING);
      // Write Column Headers
      int col1 = 0;
      row++;
      for (Iterator it = properties.keyIterator(); it.hasNext(); ) {
        String key = (String)it.next();
        LocaleValues allValues = (LocaleValues)properties.get(key);
        sheet.addCell(new Label(col1, row, key, firstCellFormat));
        for (int col = 0; col < allValues.getValues().length; col++) {
          String value = allValues.getValues()[col];
          sheet.addCell(new Label(col + 1, row, value, cellFormat));
        }
        row++;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      throw new java.io.IOException(exception.getMessage());
    }
  }

  // private static Locale[] armaniLocales = new Locale[] {
  // new Locale("", "")
  //Locale.US,
  //Locale.CANADA,
  //Locale.CANADA_FRENCH
  // };
  // Changed 01/18/2005 by Manpreet Bawa.
  // Retrives available locales from LocaleManager
  private static Locale[] armaniLocales = LocaleManager.getInstance().getSupportedLocales();

  /**
   * Used to tokenaise a string delimited by ,
   * @param commanDelimitedString String
   * @return String[]
   */
  private static String[] getTokens(String commanDelimitedString) {
    StringTokenizer stringTokenizer = new StringTokenizer(commanDelimitedString, ",");
    ArrayList list = new ArrayList();
    while (stringTokenizer.hasMoreTokens()) {
      list.add(stringTokenizer.nextToken());
    }
    return (String[])list.toArray(new String[0]);
  }

  /**
   * Used to  properties
   * @param properties OrderedProperties[]
   * @return prop OrderedProperties[]
   */
  private static OrderedProperties[] mungeProperties(OrderedProperties[][] properties) {
    OrderedProperties[] prop = new OrderedProperties[properties.length];
    OrderedProperties tempMap = new OrderedProperties();
    // For each Class (ie CoreBundle, ConfigBundle)
    for (int classCounter = 0; classCounter < properties.length; classCounter++) {
      tempMap = new OrderedProperties();
      // For each locale (ie blank, en_US, en_CA, fr_CA)
      for (int localeCounter = 0; localeCounter < properties[classCounter].length; localeCounter++) {
        // Iterate through each item in the properties map and build the class
        // and place it in the temp properties map
        for (Iterator it = properties[classCounter][localeCounter].keyIterator(); it.hasNext(); ) {
          String key = (String)it.next();
          String value = (String)properties[classCounter][localeCounter].get(key);
          LocaleValues tempClass = (LocaleValues)tempMap.get(key);
          // If we don't have a class for this key yet
          if (tempClass == null) {
            tempClass = new LocaleValues(armaniLocales.length);
          }
          tempClass.setValue(localeCounter, value);
          tempMap.put(key, tempClass);
        }
      }
      prop[classCounter] = tempMap;
    }
    return prop;
  }

  /**
   * Used to compare Workbooks
   * @param workbook1 File
   * @param workbook2 File
   */
  public static void compareWorkbooks(File workbook1, File workbook2)
      throws Exception {
    // Workbook 1 is the original, workbook 2 is the new file
    Workbook book1 = Workbook.getWorkbook(workbook1);
    Workbook book2 = Workbook.getWorkbook(workbook2);
    OrderedProperties[] book1Properties = readInWorkbook(book1);
    OrderedProperties[] book2Properties = readInWorkbook(book2);
    compareCombinedProperties(combineProperties(book1Properties, book2Properties));
  }

  /**
   * Used to combine  properties
   * @param prop1 OrderedProperties[]
   * @param prop2 OrderedProperties[]
   * @return allProperties OrderedProperties[]
   */
  private static OrderedProperties[] combineProperties(OrderedProperties[] prop1
      , OrderedProperties[] prop2) {
    OrderedProperties[] allProperties = new OrderedProperties[prop1.length >= prop2.length
        ? prop1.length : prop2.length];
    for (int i = 0; i < allProperties.length; i++) {
      allProperties[i] = new OrderedProperties();
    }
    // Loop through book 1 and move it's properties over to the new properties files
    for (int propIndex = 0; propIndex < prop1.length; propIndex++) {
      for (Iterator it = prop1[propIndex].keyIterator(); it.hasNext(); ) {
        String key = (String)it.next();
        LocaleValues allValues;
        if (allProperties[propIndex].get(key) == null) {
          allValues = new LocaleValues(armaniLocales.length * 2);
        } else {
          allValues = (LocaleValues)allProperties[propIndex].get(key);
        }
        LocaleValues value = (LocaleValues)prop1[propIndex].get(key);
        for (int i = 0; i < value.getValues().length; i++) {
          allValues.setValue(i, value.getValues()[i]);
        }
        allProperties[propIndex].put(key, allValues);
      }
    }
    for (int propIndex = 0; propIndex < prop2.length; propIndex++) {
      for (Iterator it = prop2[propIndex].keyIterator(); it.hasNext(); ) {
        String key = (String)it.next();
        LocaleValues allValues = (LocaleValues)allProperties[propIndex].get(key);
        if (allValues == null) {
          allValues = new LocaleValues(armaniLocales.length * 2);
        }
        LocaleValues value = (LocaleValues)prop2[propIndex].get(key);
        for (int i = 0; i < value.getValues().length; i++) {
          allValues.setValue(i + armaniLocales.length, value.getValues()[i]);
        }
        allProperties[propIndex].put(key, allValues);
      }
    }
    return allProperties;
  }

  /**
   * Used to compare combine properties
   * @param allProperties OrderedProperties[]
   */
  private static void compareCombinedProperties(OrderedProperties[] allProperties) {
    boolean sheetPrinted = false;
    boolean messageKeyPrinted = false;
    for (int i = 0; i < allProperties.length; i++) {
      if (!sheetPrinted) {
        sheetPrinted = true;
        //System.out.println("Sheet Number: " + i);
      }
      OrderedProperties singleProp = allProperties[i];
      for (Iterator it = singleProp.keyIterator(); it.hasNext(); ) {
        String key = (String)it.next();
        LocaleValues value = (LocaleValues)singleProp.get(key);
        String[] theValues = value.getValues();
        for (int x = 0; x < value.getValues().length - armaniLocales.length; x++) {
          if ((theValues[x] != null) && (!theValues[x].equals(theValues[x + armaniLocales.length]))) {
            if (!messageKeyPrinted) {
              messageKeyPrinted = true;
              //System.out.println("     Message Key: " + key);
            }
            //System.out.println("          Value " + x + "=[" + theValues[x] + "]");
            //System.out.println("          Value " + (x + armaniLocales.length) + "=[" + theValues[x
                //+ armaniLocales.length] + "]");
          } else if ((theValues[x + armaniLocales.length] != null)
              && (!theValues[x + armaniLocales.length].equals(theValues[x]))) {
            if (!messageKeyPrinted) {
              messageKeyPrinted = true;
              //System.out.println("     Message Key: " + key);
            }
            //System.out.println("          Value " + x + "=[" + theValues[x] + "]");
            //System.out.println("          Value " + (x + armaniLocales.length) + "=[" + theValues[x
                //+ armaniLocales.length] + "]");
          }
        }
        messageKeyPrinted = false;
      }
      sheetPrinted = false;
    }
  }

  /**
   * Used to get properties in workbook
   * @param book Workbook
   * @param workbookProperties OrderedProperties[]
   */
  private static OrderedProperties[] readInWorkbook(Workbook book) {
    Sheet[] sheets = book.getSheets();
    OrderedProperties[] workbookProperties = new OrderedProperties[sheets.length];
    LocaleValues values = null;
    // For each sheet in the original workbook
    for (int sheetIndex = 0; sheetIndex < sheets.length; sheetIndex++) {
      OrderedProperties singleSheetProperties = new OrderedProperties();
      // For each row in the original sheet
      for (int rowIndex = 1; rowIndex < sheets[sheetIndex].getRows(); rowIndex++) {
        String messageKey = sheets[sheetIndex].getRow(rowIndex)[0].getContents();
        values = new LocaleValues(armaniLocales.length);
        // For each column in the original sheet
        for (int colIndex = 1; colIndex < sheets[sheetIndex].getColumns(); colIndex++) {
          values.setValue(colIndex - 1, sheets[sheetIndex].getRow(rowIndex)[colIndex].getContents());
        }
        singleSheetProperties.put(messageKey, values);
      }
      workbookProperties[sheetIndex] = singleSheetProperties;
    }
    return workbookProperties;
  }
}


/**
 * <p>Title: LocaleValues</p>
 * <p>Description: This class is used to get the local values</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author
 * @version 1.0
 */
class LocaleValues {
  String[] values;

  /**
   *  Construct LocaleValues
   */
  public LocaleValues(int numValues) {
    values = new String[numValues];
  }

  /**
   * Used to set the values
   * @param values String
   * @param location
   */
  public void setValue(int location, String value) {
    values[location] = value;
  }

  /**
   * Used to get the values
   * @return values String[]
   */
  public String[] getValues() {
    return values;
  }
}

