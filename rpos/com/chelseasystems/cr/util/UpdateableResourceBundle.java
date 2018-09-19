/**
 * @copyright (c) 2003 Retek Inc. All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.io.*;
import java.util.*;


/**
 * This class will attempt to lazily-update itself with the proper resources
 * when the desired resources are not found.  The trigger in is <code>handleGetObject</code>
 * and works in conjuction with the <code>ResourceManager</code>.  This class
 * will also not throw a <code>MissingResourceException</code>, instead it
 * will println the missing key and return the key as the value.
 * @author Christian Greene
 * @version 1.0
 */
public class UpdateableResourceBundle extends ResourceBundle implements java.io.Serializable {
  static final long serialVersionUID = -3296445037163423031L;
  protected HashMap hmRes;

  /**
   * put your documentation comment here
   */
  public UpdateableResourceBundle() {
    this(null);
  }

  /**
   * put your documentation comment here
   * @param   ResourceBundle res
   */
  public UpdateableResourceBundle(ResourceBundle res) {
    //this.setParent(res);
    if (res != null) {
      this.updateMap(res);
    }
  }

  /**
   * Override of ResourceBundle, same semantics.  Return key when not found.
   */
  public Object handleGetObject(String key) {
    Object obj = hmRes.get(key);
    if (obj == null) {
      // update table
      Locale aLocale = (this.getLocale() != null) ? this.getLocale() : Locale.getDefault();
      ResourceBundle rb = ResourceManager.getNextResourceBundle(aLocale);
      if (rb != null && rb instanceof UpdateableResourceBundle) {
        this.updateMap(((UpdateableResourceBundle)rb).hmRes);
        return handleGetObject(key);
      }
      Trace.out("UpdateableResourceBundle.handleGetObject()->missing: \"" + key + "\"");
      /*
       try {
       Set set = hmRes.keySet();
       if (! new File("debug\\debug.txt").exists()) {
       FileWriter fw = new FileWriter(new File("debug\\debug.txt"));
       Iterator itr = set.iterator();
       while (itr.hasNext()) {
       String key1 = itr.next().toString();
       fw.write(key1 + " : " + hmRes.get(key1) + "\n");
       }
       }
       } catch (Exception e) {
       //System.out.println("%%%%%%%%%%%%%%%%%% EXCEPTION %%%%%%%%%%%%%%%%%%% " + e);
       }
       */
      obj = key;
    }
    return obj;
  }

  /**
   * Override base class's method to protect its locale beling null
   * @return My Locale or Parent's Locale if I do not have one
   */
  public Locale getLocale() {
    Locale myLocale = super.getLocale();
    if (myLocale != null) {
      return myLocale;
    } else if (this.parent != null) {
      return this.parent.getLocale();
    }
    return null;
  }

  /**
   * Implementation of ResourceBundle#getKeys.
   */
  public Enumeration getKeys() {
    Enumeration result = null;
    if (parent != null) {
      final Iterator myKeys = hmRes.keySet().iterator();
      final Enumeration parentKeys = parent.getKeys();
      result = new Enumeration() {

        /**
         * @return
         */
        public boolean hasMoreElements() {
          if (temp == null) {
            nextElement();
          }
          return temp != null;
        }

        /**
         * @return
         */
        public Object nextElement() {
          Object returnVal = temp;
          if (myKeys.hasNext()) {
            temp = myKeys.next();
          } else {
            temp = null;
            while (temp == null && parentKeys.hasMoreElements()) {
              temp = parentKeys.nextElement();
              if (hmRes.containsKey(temp)) {
                temp = null;
              }
            }
          }
          return returnVal;
        }

        Object temp = null;
      };
    } else {
      result = new Enumeration() {
        final Iterator myKeys = hmRes.keySet().iterator();

        /**
         * @return
         */
        public boolean hasMoreElements() {
          return myKeys.hasNext();
        }

        /**
         * @return
         */
        public Object nextElement() {
          return myKeys.next();
        }
      };
    }
    return result;
  }

  /**
   * Update the specified  key-value pair
   */
  public void update(String key, String value) {
    String[][] contents = new String[1][2];
    contents[0][0] = key;
    contents[0][1] = value;
    this.updateMap(contents);
  }

  /**
   * Apply the specified two-dimensional array as a key-value pair set
   * to the instance member map of resources.
   */
  protected void updateMap(Object[][] contents) {
    for (int i = contents.length - 1; i >= 0; i--) {
      hmRes.put(contents[i][0], contents[i][1]);
    }
  }

  /**
   * Apply the specified map to the instance member map of resources.
   */
  protected void updateMap(Map mapping) {
    for (Iterator iter = mapping.keySet().iterator(); iter.hasNext(); ) {
      Object key = iter.next();
      hmRes.put(key, mapping.get(key));
    }
  }

  /**
   * Apply the specified resource bundle mapping to this map of resources.
   */
  protected void updateMap(ResourceBundle res) {
    if (hmRes == null) {
      hmRes = new HashMap();
    }
    for (Enumeration enm = res.getKeys(); enm.hasMoreElements(); ) {
      Object key = enm.nextElement();
      hmRes.put(key, res.getObject(key.toString()));
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public OrderedProperties toProperties() {
    OrderedProperties prop = new OrderedProperties();
    for (Enumeration enm = this.getKeys(); enm.hasMoreElements(); ) {
      String key = (String)enm.nextElement();
      String value = this.getString(key);
      prop.put(key, value);
    }
    return prop;
  }

  /**
   * Save the Resource Bundle to a Properties file
   * @param outputFile the output file
   * @throws java.io.IOException
   */
  public void storeToPropertiesFile(File outputFile)
      throws java.io.IOException {
    OrderedProperties prop = toProperties();
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
    prop.storeOrderedByKeys(bos, outputFile.getName());
  }
}

