/**
 * @copyright (c) 1998-2003 Retek Inc. All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;
import java.util.*;
import java.io.*;


/**
 * This class is intended to work hand in hand with an <code>UpdateableResourceBundle</code>.
 * Set a string of classnames tokenized by commas that represent the list of classes that
 * are resources for the application.  This class will maintain that list.  When
 * a resource is not found, the <code>UpdateableResourceBundle</code> will
 * attempt to get the next bundle and update its table of keys and values.
 * @see UpdateableResourceBundle
 * @author Christian Greene
 */
public class ResourceManager {
  private static String strResources;
  private static StringTokenizer tokResources;
  private static HashMap hmResources;
  // init
  static {
    try {
      hmResources = new HashMap();
      // ignore this nice hard-coded reference to the base resource bundle
      //setResourceBundle("com.chelseasystems.cs.util.Bundle");
      setResourceBundle("com.chelseasystems.cs.util.CoreBundle");
    } catch (Exception ex) {
      Trace.ex(ex);
    }
  }

  /**
   * @param resourceName a comma-delimited list of class names that contain
   * resources for this application.  This will clear any previously cached
   * resources.
   */
  public static void setResourceBundle(String resourceName) {
    strResources = resourceName;
    hmResources.clear();
  }

  /**
   * @return ResourceBundle a bundle based on the current default Locale
   */
  public static ResourceBundle getResourceBundle() {
    return getResourceBundle(Locale.getDefault());
  }

  /**
   * ResourceBundle a bundle based on the specified locale.  This class
   * will maintain a map of all the locales that have been asked for.  If the
   * resource does not exist, the resource will be <i>initialized</i>, meaning
   * that if this Manager is managing a list of resources, the list will start
   * over and the first resource will be instantiated.
   *
   * @param aLocale the Locale that specifies the desired resource bundle.
   * @return a resource bundle, likely a <code>UpdateableResourceBundle</code>
   */
  public static ResourceBundle getResourceBundle(Locale aLocale) {
    ResourceBundle res = (ResourceBundle)hmResources.get(aLocale);
    if (res == null && strResources != null) {
      res = initializeResourceBundles(aLocale);
      hmResources.put(aLocale, res);
    }
    return res;
  }

  /**
   * Having maintained a list of resources, return the next resource for the
   * specified locale that has not yet been asked for.  If the retrieving class
   * is a UpdateableResourceBundle, then it will likely update its own map
   * of keys and values.
   *
   * @see UpdateableResourceBundle
   * @param aLocale the Locale that specifies the desired resource bundle.
   * @return a resource bundle, likely a <code>UpdateableResourceBundle</code>
   */
  protected static ResourceBundle getNextResourceBundle(Locale aLocale) {
    if (tokResources.hasMoreTokens()) {
      String nextToken = tokResources.nextToken();
      ResourceBundle res = ResourceBundle.getBundle(nextToken, aLocale);
      if (res instanceof PropertyResourceBundle) {
        res = new UpdateableResourceBundle(res);
      }
      return res;
    }
    return null;
  }

  /**
   * Start a new list of resource bundles and retrieve the first bundle in the
   * list.
   *
   * @param aLocale the Locale that specifies the desired resource bundle.
   * @return a resource bundle, likely a <code>UpdateableResourceBundle</code>
   */
  private static ResourceBundle initializeResourceBundles(Locale aLocale) {
    tokResources = new StringTokenizer(strResources, ",");
    return getNextResourceBundle(aLocale);
  }

  /**
   * put your documentation comment here
   * @param args
   * @exception Exception
   */
  public static void main(String[] args)
      throws Exception {
    System.out.println("********************************** we are in main");
    String[] resourceNames = new String[] {"com.chelseasystems.cs.util.MessageBundle"
        , "com.chelseasystems.cs.util.MnemonicMessageBundle"
        , "com.chelseasystems.cs.util.RuleMessageBundle"
        , "com.chelseasystems.cs.util.ConfigMessageBundle"
        , "com.chelseasystems.cs.util.TAConfigMessageBundle"
        , "com.chelseasystems.cs.util.TAMessageBundle",
        //"com.chelseasystems.cs.util.MenuBundle"
    };
    String[] propNames = new String[] {"CoreBundle", "MnemonicBundle", "RuleBundle", "ConfigBundle"
        , "TAConfigBundle", "TABundle",
        //"MenuBundle"
    };
    Locale[] locales = new Locale[] {new Locale("", ""), Locale.US, Locale.CANADA
        , new Locale("fr", "")
    };
    String[] localeCodes = new String[] {"", "_en_US", "_en_CA", "_fr_CA"
    };
    for (int i = 0; i < resourceNames.length; i++) {
      ResourceManager.setResourceBundle(resourceNames[i]);
      for (int j = 0; j < locales.length; j++) {
        String outputFileName = "../files/" + propNames[i] + localeCodes[j] + ".properties";
        System.out.println("Locale -->" + locales[j] + " --- " + outputFileName);
        ResourceBundle res = ResourceManager.getResourceBundle(locales[j]);
        ((UpdateableResourceBundle)res).storeToPropertiesFile(new File(outputFileName));
      }
    }
  }
}

