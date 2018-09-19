/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;


/**
 * @author fbulah
 *
 */
public class Utils {
  public static boolean DEBUG = false;
  public static boolean DEBUG2 = false;

  /**
   *
   */
  public Utils() {
  }

  /**
   * put your documentation comment here
   * @param nElements
   * @return
   */
  public static Vector createInitializedVector(int nElements) {
    Vector v = new Vector();
    for (int i = 0; i < nElements; i++) {
      v.add(null);
    }
    return v;
  }

  /**
   * put your documentation comment here
   * @param map
   */
  protected static void printHashKeys(HashMap map) {
    Iterator iter = map.keySet().iterator();
    while (iter.hasNext()) {
      System.out.println("key=" + (String)iter.next());
    }
  }

  /**
   * put your documentation comment here
   * @param map
   * @param name
   */
  public static void printHashMap(HashMap map, String name) {
    if (map == null) {
      System.out.println(name + ": map is null");
      return;
    }
    printHashMap(map, name, Arrays.asList(map.keySet().toArray()));
  }

  /**
   * put your documentation comment here
   * @param map
   * @param name
   * @param columnList
   */
  public static void printHashMap(HashMap map, String name, List columnList) {
    if (map == null) {
      System.out.println(name + ": map is null");
      return;
    }
    Iterator iter = map.keySet().iterator();
    while (iter.hasNext()) {
      Object key = iter.next();
      if (columnList.contains(key)) {
        System.out.println(name + ": key=" + key + " value=" + map.get(key));
      }
    }
  }

  /**
   * put your documentation comment here
   * @param map
   * @param name
   * @param keyName
   * @param valueName
   */
  public static void printHashMap(HashMap map, String name, String keyName, String valueName) {
    Iterator iter = map.keySet().iterator();
    while (iter.hasNext()) {
      Object key = iter.next();
      System.out.println(name + ": " + keyName + "=" + key + " " + valueName + "=" + map.get(key));
    }
  }

  /**
   * put your documentation comment here
   * @param map1
   * @param map2
   * @param name
   */
  public static void printHashMaps(HashMap map1, HashMap map2, String name) {
    if (map1 == null) {
      System.out.println(name + ": map1 is null");
      return;
    }
    if (map2 == null) {
      System.out.println(name + ": map2 is null");
      return;
    }
    TreeMap sortedMap1 = new TreeMap(map1);
    Iterator iter = sortedMap1.keySet().iterator();
    while (iter.hasNext()) {
      Object key = iter.next();
      System.out.println(name + ": key=" + key + "       value1=" + map1.get(key) + " value2="
          + map2.get(key));
    }
  }

  /**
   * put your documentation comment here
   * @param list
   * @param name
   */
  public static void printList(List list, String name) {
    if (list == null) {
      System.out.println("Utils.printList: input list for " + name + " is null");
      return;
    }
    for (int i = 0; i < list.size(); i++) {
      System.out.println(name + ": list[" + i + "]=" + list.get(i));
    }
  }

  /**
   * put your documentation comment here
   * @param src
   * @param dest
   */
  public static void copyHash(HashMap src, HashMap dest) {
    copyHash(src, dest, true);
  }

  /**
   * put your documentation comment here
   * @param src
   * @param dest
   * @param noOverwrite
   */
  public static void copyHash(HashMap src, HashMap dest, boolean noOverwrite) {
    Iterator iter = src.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      Object key = entry.getKey();
      if (noOverwrite && (dest.get(key) != null)) {
        continue;
      }
      dest.put(key, entry.getValue());
    }
    printHashMaps(src, dest, "Utils.copyHash.DEBUG.src..dest.after");
    return;
  }

  /**
   * put your documentation comment here
   * @param s
   * @param delim
   * @return
   */
  public static List getTokens(String s, String delim) {
    if (DEBUG2) {
      System.out.println("getTokens: entry: s=<" + s + ">");
    }
    List list = new ArrayList();
    int ct = 0;
    StringTokenizer st = new StringTokenizer(s, delim, true);
    String lastToken = "";
    while (st.hasMoreTokens()) {
      String nextToken = st.nextToken();
      if (DEBUG2) {
        System.out.println("nextToken=<" + nextToken + "> delim=<" + delim + ">");
      }
      if (!nextToken.equals(delim)) {
        if (DEBUG2) {
          System.out.println("getTokens.token[" + ct + "]=" + nextToken);
        }
        list.add(nextToken);
        ct++;
      } else {
        if (nextToken.equals(lastToken)) {
          if (DEBUG2) {
            System.out.println("getTokens.token[" + ct + "]=null");
          }
          list.add(null);
          ct++;
        }
      }
      lastToken = nextToken;
    }
    if (DEBUG2) {
      System.out.println("getTokens: return: list.size=" + list.size() + " ct=" + ct + " tokens");
    }
    return list;
  }

  /**
   * put your documentation comment here
   * @param e
   * @return
   */
  public static TreeMap sort(Enumeration e) {
    HashMap map = new HashMap();
    while (e.hasMoreElements()) {
      map.put(e.nextElement(), null);
    }
    return (new TreeMap(map));
  }

  /**
   * put your documentation comment here
   * @param props
   */
  public static void printProperties(Properties props) {
    TreeMap sortedPropertyNamesMap = sort(props.propertyNames());
    Iterator iter = sortedPropertyNamesMap.keySet().iterator();
    while (iter.hasNext()) {
      String propName = (String)iter.next();
      System.out.println(propName + "=" + props.getProperty(propName));
    }
  }

  /**
   * This method return the begining of today
   */
  static public java.util.Date getBeginingOfDay() {
    return getBeginingOfDay(new java.util.Date());
  }

  /**
   * This method return the begining of the day
   */
  static public java.util.Date getBeginingOfDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * This method return the end of today
   */
  static public java.util.Date getEndOfDay() {
    return getEndOfDay(new java.util.Date());
  }

  /**
   * This method return the end of the day
   */
  static public java.util.Date getEndOfDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return calendar.getTime();
  }
}

