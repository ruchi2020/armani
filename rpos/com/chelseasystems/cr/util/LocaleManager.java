/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright © 2004 Retek Inc.  All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.util;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


/**
 * put your documentation comment here
 */
public class LocaleManager {
  private static LocaleManager localeManager;
  private HashMap locales = new HashMap();
  private Locale defaultLocale;
  private Locale storeLocale;
  private boolean storeLocaleAsDefault = false;

  /**
   * put your documentation comment here
   */
  private LocaleManager() {
    try {
      loadFromConfig();
    } catch (INIFileException e) {
      System.out.println("INIFile Exception on loading supported locales:" + e);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static LocaleManager getInstance() {
    if (localeManager == null) {
      localeManager = new LocaleManager();
    }
    return localeManager;
  }

  /**
   * put your documentation comment here
   * @exception INIFileException
   */
  private void loadFromConfig()
      throws INIFileException {
    INIFile file = new INIFile(FileMgr.getLocalFile("config", "locales.cfg"), true);
    storeLocaleAsDefault = file.getValue("USE_STORE_LOCALE_AS_DEFAULT").equals("true");
    String defaultLocaleKey = file.getValue("DEFAULT_LOCALE");
    String supportedLocales = file.getValue("SUPPORTED_LOCALES");
    StringTokenizer st = new StringTokenizer(supportedLocales, ",");
    while (st.hasMoreElements()) {
      String key = (String)st.nextElement();
      String country = file.getValue(key, "COUNTRY", "");
      String language = file.getValue(key, "LANGUAGE", "");
      Locale locale = new Locale(language, country);
      if (key.equals(defaultLocaleKey)) {
        defaultLocale = locale;
      }
      String flagImagePath = file.getValue(key, "FLAG", "");
      ImageIcon flagIcon;
      try {
        flagImagePath = flagImagePath.replace('/', File.separatorChar);
        flagIcon = new ImageIcon(flagImagePath);
      } catch (Exception e) {
        System.out.println("get for flag: failed, getting world ");
        flagIcon = CMSImageIcons.getInstance().getWorld();
      }
      locales.put(locale, flagIcon);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Locale[] getSupportedLocales() {
    Locale myLocale = this.getDefaultLocale();
    List list = new ArrayList();
    Locale[] allLocales = (Locale[])locales.keySet().toArray(new Locale[0]);
    list.add(myLocale);
    for (int i = 0; i < allLocales.length; i++) {
      if (!allLocales[i].equals(myLocale) && allLocales[i].getCountry().equals(myLocale.getCountry())) {
        list.add(allLocales[i]);
      }
    }
    return (Locale[])list.toArray(new Locale[0]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Locale[] getAllSupportedLocales() {
    Locale myLocale = this.getDefaultLocale();
    List list = new ArrayList();
    Locale[] allLocales = (Locale[])locales.keySet().toArray(new Locale[0]);
    for (int i = 0; i < allLocales.length; i++) {
      list.add(allLocales[i]);
    }
    return (Locale[])list.toArray(new Locale[0]);
  }

  /**
   * put your documentation comment here
   * @param countryCode
   * @return
   */
  public Locale[] getSupportedLocales(String countryCode) {
    List list = new ArrayList();
    Locale[] allPossibleLocales = (Locale[])locales.keySet().toArray(new Locale[0]);
    for (int i = 0; i < allPossibleLocales.length; i++) {
      if (allPossibleLocales[i].getCountry().equals(countryCode)) {
        list.add(allPossibleLocales[i]);
      }
    }
    return (Locale[])list.toArray(new Locale[0]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Locale getDefaultLocale() {
    if (!this.isUseStoreLocaleAsDefault()) {
      return this.defaultLocale;
    }
    if (storeLocale != null) {
      return this.storeLocale;
    }
    this.storeLocale = this.getLocalStoreLocaleFromFile();
    if (storeLocale != null) {
      System.out.println("LocaleManager: Default Locale set to " + storeLocale); //please keep this line. DF
      return this.storeLocale;
    }
    return this.defaultLocale;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isUseStoreLocaleAsDefault() {
    return this.storeLocaleAsDefault;
  }

  /**
   * put your documentation comment here
   * @return
   */
  private Locale getLocalStoreLocaleFromFile() {
    Store store = null;
    try {
      File storeFile = new File(FileMgr.getLocalFile("repository", "STORE"));
      store = (Store)new ObjectStore(storeFile).read();
    } catch (ObjectStoreException ose) {
      ose.printStackTrace();
      System.out.println("LocaleManager.getLocalStoreLocaleFromFile(): Error in reading STORE object at repository.  Will use default from locale.cfg.");
    }
    if (store == null) {
      return null;
    }
    String la = store.getPreferredISOLanguage();
    String co = store.getPreferredISOCountry();
    return new Locale(la, co);
  }

  /**
   * put your documentation comment here
   * @param locale
   * @return
   */
  public ImageIcon getFlagForLocale(Locale locale) {
    return (ImageIcon)locales.get(locale);
  }
}

