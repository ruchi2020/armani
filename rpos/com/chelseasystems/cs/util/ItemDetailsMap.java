/*
 * History (tab separated):-
 * Vers Date            By  Spec                        Description
 * 1    2/4/05          KS  POS_IS_ItemDownload_Rev1    Initial
 * 2    2/23/06         MD  Issues #1128 and 1320       ReadLine on RandomAccessFile
 *                                                      is not safe for unicode chars
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import java.util.*;
import java.io.*;

import com.chelseasystems.cr.logging.*;


/**
 *
 * <p>Title: ItemDetailsMap</p>
 *
 * <p>Description:This class reads the item.cfg and populate itemMap and barCodeMap </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public class ItemDetailsMap {
  /**
   * HashMap to stroe the item id
   */
  private static Map itemIDMap = new HashMap();
  /**
   * HashMap to stroe the bar code
   */
  private static Map barCodeMap = new HashMap();
  /**
   * RandomAccessFile to read the configration file
   */
  private static RandomAccessFile rf;
  /**
   * String store the log dir lpcation
   */
  private static String logDir = "";

  public static final String DEFAULT_CHAR_ENCODING = "UTF-8";

  // Encoding to use for XML parsing
  private static String encoding = null;

  static {
      ConfigMgr configMgr= new ConfigMgr("locales.cfg");
      if (configMgr == null) {
          encoding = DEFAULT_CHAR_ENCODING;
      }
      encoding = configMgr.getString("XML_ENCODING");
      if (encoding == null) {
          encoding = DEFAULT_CHAR_ENCODING;
      }
    }
  /**
   *
   * @param file File
   * @return boolean
   */
  public static boolean hasCRLF(File file) {
    RandomAccessFile rf = null;
    try {
      rf = new RandomAccessFile(file, "r");
      byte nextByte;
      boolean hasCR = false;
      while (true) {
        nextByte = rf.readByte();
        if (nextByte == (byte)'\n') {
          rf.close();
          if (hasCR)
            return true;
          else
            return false;
        }
        if (nextByte == (byte)'\r') {
          hasCR = true;
        }
      }
    } catch (Exception ee) {
      return false;
    }
  }

  /**
   * This method reads the items detail file and create a map of item id and their
   * respective offset in the data file.
   * @param storeId Store Id
   * @throws Exception
   */
  public static Map loadMapById(String storeId)
      throws Exception {
    return loadMapById(storeId, "_items.inc", false);
  }

  /**
   * This method reads the items log file and create a map of item id and their
   * respective offset in the data file.
   * @param storeId String
   * @param fileName String
   * @param isDatFile boolean
   * @return Map
   * @throws Exception
   */

  public static void main(String[] args) throws Exception{
	System.out.println("here");
	HashMap temp = null;
    try {
      String datFile = "D:\\skillnet\\workspace\\armani-japan\\rpos\\files\\prod\\item_dnld\\data\\check.dat";
      String outFile = "D:\\skillnet\\workspace\\armani-japan\\rpos\\files\\prod\\item_dnld\\data\\check.out.dat";
      System.out.println("loadMapById : " + datFile);
      temp = new HashMap(10000, 0.75f);
      File TTTT = new File(datFile);
      FileWriter fw = new FileWriter(outFile);
      int eolOffset = 1;
      if (hasCRLF(TTTT)) {
        eolOffset = 2;
      }
      if (TTTT.exists()) {
    	  BufferedReader BFR = new BufferedReader(
   	            new InputStreamReader(new FileInputStream(TTTT), "UTF8"));
        String s = "";
        long pos = 0;
        int counter = 0;
        while ((s = BFR.readLine()) != null) {
        	System.out.println("%loading into the memory%"+s);
        	fw.write(s);
          int pp = s.indexOf("|");
          int pppo = pp + 1;
          if (pp > 0 && pppo < s.length()) {
            int yy = s.indexOf("|", pppo);
            if (yy > 0) {
              counter++;
              String id = s.substring(pppo, yy);
              id = new String(id.getBytes());
              temp.put(id, new Long(pos));
              System.out.println("$$$$$ " + id);

              //System.out.print(id);
              //System.out.print("  ");
            }
          }
          if ((counter % 1000) == 0)
            System.out.print('*');
          //pos=rf.getFilePointer();
          pos += s.length() + eolOffset;
          s = null;
        }
        BFR.close();
        fw.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (rf != null)
        rf.close();
    }

    System.out.println("loadMapById End : " + Runtime.getRuntime().totalMemory());

  }

  public static Map loadMapById(String storeId, String fileName, boolean isDatFile)
      throws Exception {
    System.out.println("loadMapById Start : " + Runtime.getRuntime().totalMemory());
    HashMap temp = null;
    try {
      String datFile = storeId + fileName; //"_items.inc";
      System.out.println("loadMapById : " + datFile);
      temp = new HashMap(10000, 0.75f);
      File TTTT = new File(datFile);
      int eolOffset = 1;
      if (hasCRLF(TTTT)) {
        eolOffset = 2;
      }
      if (TTTT.exists()) {

    	  BufferedReader BFR = new BufferedReader(
     	            new InputStreamReader(new FileInputStream(TTTT), encoding));
        System.out.println("STARTING loadMapById: " + fileName + " " + isDatFile);
        String s = "";
        long pos = 0;
        int counter = 0;
        while ((s = BFR.readLine()) != null) {
          int pp = s.indexOf("|");
          int pppo = pp + 1;
          if (pp > 0 && pppo < s.length()) {
            int yy = s.indexOf("|", pppo);
            if (yy > 0) {
              counter++;
              String id = s.substring(pppo, yy);
              id = new String(id.getBytes(encoding));
              temp.put(id, new Long(pos));
              //System.out.print(id);
              //System.out.print("  ");
            }
          }

          byte[] byteArr = s.getBytes(encoding);

          if ((counter % 1000) == 0)
            System.out.print('*');
          //pos=rf.getFilePointer();
     //     pos += s.length() + eolOffset;
          pos += byteArr.length + eolOffset;
          s = null;
        }
        System.out.println("FINISHING loadMapById: " + fileName + " " + isDatFile);

        BFR.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (rf != null)
        rf.close();
    }
    //Assing values to itemIDMap
    if (isDatFile) {
      itemIDMap = temp;
    }
    System.out.println("loadMapById End : " + Runtime.getRuntime().totalMemory());
    return temp;
  }

  /**
   * Reads the items detail file and create a map of item id and their
   * respective offset in the data file.
   * @param storeId Store Id
   * @throws Exception
   */
  public static void loadMapByBarCode(String filepath)
      throws Exception {
    System.out.println("loadMapByBarCode Start : " + Runtime.getRuntime().totalMemory());
    HashMap temp = new HashMap(10000, 0.75f);
    Set iDSet = new HashSet();
    try {
    	  ConfigMgr configMgr= new ConfigMgr("item.cfg");
    	  String sapLookUp = configMgr.getString("SAP_LOOKUP");
      String datFile = filepath + "_items.dat";
      System.out.println("loadMapByBarCode File : " + datFile);
      File TTTT = new File(datFile);
      int eolOffset = 1;
      if (hasCRLF(TTTT)) {
        eolOffset = 2;
      }
      if (TTTT.exists()) {
    	  BufferedReader BFR = new BufferedReader(
     	            new InputStreamReader(new FileInputStream(TTTT), encoding));

        System.out.println("STARTING loadMapByBarCode: " + datFile + " encoding = " + encoding  + "*");
        String s = "";
        long pos = 0;
        int counter = 0;
        int pp = 0, pppo = 0, yy = 0;
        while ((s = BFR.readLine()) != null) {
          pp = s.indexOf("|");
          pppo = pp + 1;
          if (pp > 0 && pppo < s.length()) {
            String itemID = s.substring(0, pp);
            itemID = new String(itemID.getBytes(encoding));
            int ItemIdLength = itemID.length() ;
            temp.put(itemID, new Long(pos));
            yy = s.indexOf("|", pppo);
            if (yy > 0) {
              counter++;
              String id = s.substring(pppo, yy);
              id = new String(id.getBytes(encoding));
 // Added by Anjana to pick the correct line from the dat file              
      
              if(sapLookUp!=null && sapLookUp.equalsIgnoreCase("Y")){
              if((ItemIdLength >8 && ItemIdLength<=18) && (iDSet.add(id))  ){
           	  temp.put(id, new Long(pos));
         
              } else if((ItemIdLength >8 && ItemIdLength<=18) && (!iDSet.add(id))){
            	  temp.put(id, new Long(pos));
                }
        //Added by Anjana: When SAP=Y and only retek in dat file does DB look up      
              else if(!(ItemIdLength >8) && (iDSet.add(id))){
            	  temp.remove(id);
                } // ends
              }else{
            	  if(!(ItemIdLength >8 && ItemIdLength<=18) && (iDSet.add(id))  ){
				   temp.put(id, new Long(pos));
            	  }
              }
              
		    }
          }
          byte[] byteArr = s.getBytes(encoding);
          if ((counter % 1000) == 0)
            System.out.print(" *" + counter + "*");
          //pos=rf.getFilePointer();
     //     pos += s.length() + eolOffset;
          pos += byteArr.length + eolOffset;
          s = null;
        }
        System.out.println("FINISHING loadMapByBarCode: " + datFile);

        BFR.close();
      }
      rf = new RandomAccessFile(datFile, "r");
    } catch (Exception e) {
      if (rf != null)
        rf.close();
    }
    System.out.println("loadMapByBarCode End : " + Runtime.getRuntime().totalMemory());
    barCodeMap = temp;
  }

  /*
   * Utility method to extract string upto newline from byte array
   */
  private static String getLineString(byte[] b, int len) throws UnsupportedEncodingException  {
      String tmp = new String(b, 0, len, encoding);
      int lineLen = tmp.lastIndexOf('\n');
      if (lineLen == -1) { // Most likely end of file reached.
          return tmp;
      } else if (lineLen == 0) { // First char is \n
          return "";
      } else if (tmp.charAt(lineLen-1) == '\r') { // Prev char is \r
          lineLen = lineLen - 1;
          if (lineLen == 0) { // First two chars \r\n
              return "";
          }
      }
      return tmp.substring(0, lineLen);
  }
  
  /**
   * This method look's for the id in the map and return the item details string
   * @param id Item id
   * @throws Exception
   * @return Item details string
   */
  /*public static String getItemString(String fileName, Map m, String ID) {
    String temp = "";
    if (m.containsKey(ID)) {
      try {
        Long pos = (Long)m.get(ID);
        System.out.println("Seeking " + ID + " at " + pos);
        rf.seek(pos.longValue());
        byte[] b = new byte[1024];
        int bRead = rf.read(b);
        temp = getLineString(b, bRead);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return temp;
  }*/


  public static String getItemString(String fileName, Map m, String ID) {
    String temp = "";
    if (m.containsKey(ID)) {
      try {
        Long pos = (Long)m.get(ID);
    //    System.out.println("Seeking " + ID + " at " + pos);
        rf.seek(pos.longValue());
        byte[] b = new byte[1024];
        int bRead = rf.read(b);
        temp = getLineString(b, bRead);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return temp;
  }

  /**
   * This method look's for the id in the map and return the item details string
   * @param id String
   * @return String
   * @throws Exception
   */
  public static String getItemDetailsString(String id)
      throws Exception {
	  return getItemString("_items.dat", barCodeMap, id);
  }
  
  public static Map getBarcodeMap() {
	 return barCodeMap; 
  }
}

