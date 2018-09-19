/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.currency;

import java.text.*;
import java.util.*;
import com.chelseasystems.cr.config.*;


/**
 * put your documentation comment here
 */
public class CurrencyFormat {

  private String overrideCurrencyPattern = null;
  private CurrencyType currencyType = null;
  private Locale inLocale = null;
  private String decimalSeparator = null;
  private String groupSeparator = null;
  /**
   * put your documentation comment here
   * @param currency
   * @return
   */
  public static String format(ArmCurrency currency) {
    return format(currency, Locale.getDefault());
  }

  /**
   * put your documentation comment here
   * @param currency
   * @param inLocale
   * @return
   */
  public static String format(ArmCurrency currency, Locale inLocale) {
    return CurrencyFormat.getInstance(currency.getCurrencyType()
        , inLocale).format(currency.doubleValue());
  }

  /**
   * put your documentation comment here
   * @param   String currencyPattern
   */
  private CurrencyFormat(String currencyPattern) {
    this.overrideCurrencyPattern = currencyPattern;
  }

  /**
   * put your documentation comment here
   * @param   String currencyPattern
   * @param   Locale inLocale
   */
  private CurrencyFormat(String currencyPattern, Locale inLocale) {
    this.overrideCurrencyPattern = currencyPattern;
    this.inLocale = inLocale;
  }

  /**
   * put your documentation comment here
   * @param   CurrencyType currencyType
   * @param   Locale inLocale
   */
  private CurrencyFormat(CurrencyType currencyType, Locale inLocale) {
    this.currencyType = currencyType;
    this.inLocale = inLocale;
  }

  /**
   * put your documentation comment here
   * @param   String currencyPattern
   * @param   Locale inLocale
   */
  private CurrencyFormat(String currencyPattern, Locale inLocale, String decimalSeparator
      , String groupSeparator) {
    this.overrideCurrencyPattern = currencyPattern;
    this.inLocale = inLocale;
    this.decimalSeparator = decimalSeparator;
    this.groupSeparator = groupSeparator;
  }


  /**
   * put your documentation comment here
   * @param value
   * @return
   */
  private String format(double value) {
    if (overrideCurrencyPattern != null) {
      if (this.inLocale != null) {
        try {
          DecimalFormatSymbols dfs = new DecimalFormatSymbols(this.inLocale);
          if (this.decimalSeparator != null)
            dfs.setDecimalSeparator(this.decimalSeparator.charAt(0));
          if (this.groupSeparator != null)
            dfs.setGroupingSeparator(this.groupSeparator.charAt(0));
          return new DecimalFormat(this.overrideCurrencyPattern, dfs).format(value);
        } catch (MissingResourceException mrs) {
          //this will happen if locale is not a known one.
          //so we format the value using the default Locale
          mrs.printStackTrace();
          return new DecimalFormat(this.overrideCurrencyPattern).format(value);
        }
      } else {
        return new DecimalFormat(this.overrideCurrencyPattern).format(value);
      }
    } else if (currencyType != null) {
      Locale locale = inLocale;
      if (inLocale == null) {
        locale = Locale.getDefault();
      }
      NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(inLocale);
      String formatWithLocalSymbol = currencyFormat.format(value);
      String currencySymbol = getCurrencySymbol(currencyType.getLocale());
      String localeSymbol = getCurrencySymbol(locale);
      if (currencySymbol.equals(localeSymbol)) {
        return formatWithLocalSymbol;
      } else {
        return this.replace(formatWithLocalSymbol, localeSymbol, currencySymbol);
      }
    } else {
      return "" + value; //no format
    }
  }

  private static Hashtable allCurrencyFormats = null;

  /**
   * put your documentation comment here
   * @param currencyType
   * @param inLocale
   * @return
   */
  private static CurrencyFormat getInstance(CurrencyType currencyType, Locale inLocale) {
    if (allCurrencyFormats == null) {
      allCurrencyFormats = getFormatsFromConfigFile();
    }
    String key = "FORMAT_" + currencyType.getCode() + "_" + inLocale.getLanguage() + "_"
        + inLocale.getCountry();
    CurrencyFormat anInstance = (CurrencyFormat)allCurrencyFormats.get(key);
    if (anInstance == null) {
      anInstance = new CurrencyFormat(currencyType, inLocale);
    }
    allCurrencyFormats.put(key, anInstance);
    return anInstance;
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static Hashtable getFormatsFromConfigFile() {
    Hashtable table = new Hashtable();
    ConfigMgr currencyConfig = new ConfigMgr("currency.cfg");
    String list = currencyConfig.getString("FORMAT_LIST");
    if (list == null) {
      return table;
    }
    StringTokenizer st = new StringTokenizer(list, ",");
    while (st.hasMoreTokens()) {
      String key = "FORMAT_" + st.nextToken();
      String format = currencyConfig.getString(key + ".PATTERN");
      String decimalSeparator = currencyConfig.getString(key + ".DECIMAL_SEPARATOR");
      String groupSeparator = currencyConfig.getString(key + ".GROUP_SEPARATOR");
      if (format != null) {
        System.out.println("Current Format Config: " + key + " = " + format);
        String language = null;
        String country = null;
        StringTokenizer keyST = new StringTokenizer(key, "_"); //should have 4
        if (keyST.hasMoreTokens()) {
          keyST.nextToken(); //skip FORMAT
        }
        if (keyST.hasMoreTokens()) {
          keyST.nextToken(); //skip currency code
        }
        if (keyST.hasMoreTokens()) {
          language = keyST.nextToken();
        }
        if (keyST.hasMoreTokens()) {
          country = keyST.nextToken();
        }
        if (language != null && country != null && language.length() == 2 && country.length() == 2) {
          Locale locale = new Locale(language, country);
          table.put(key, new CurrencyFormat(format, locale, decimalSeparator, groupSeparator));
        } else {
          table.put(key, new CurrencyFormat(format));
        }
      }
    }
    return table;
  }

  /**
   * put your documentation comment here
   * @param locale
   * @return
   */
  private static String getCurrencySymbol(Locale locale) {
    String localeElement = "java.text.resources.LocaleElements";
    ResourceBundle rb = ResourceBundle.getBundle(localeElement, locale);
    String[] array = rb.getStringArray("CurrencyElements");
    if (array == null || array.length == 0) {
      return "";
    }
    return array[0];
  }

  /**
   * put your documentation comment here
   * @param string
   * @param oldSub
   * @param newSub
   * @return
   */
  private static String replace(String string, String oldSub, String newSub) {
    if (newSub == null) {
      return string;
    }
    if (oldSub == null || oldSub.length() == 0) {
      return string;
    }
    if (oldSub.equals(newSub)) {
      return string;
    }
    if (string == null || string.length() == 0) {
      return string;
    }
    int indexOf = string.indexOf(oldSub);
    if (indexOf == -1) {
      return string;
    }
    String newString = string.substring(0, indexOf) + newSub
        + string.substring(indexOf + oldSub.length());
    return CurrencyFormat.replace(newString, oldSub, newSub);
  }
}

