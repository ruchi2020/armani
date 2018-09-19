/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	ExchangeRateDownloadFileServices
 * 2    6/29/05         MP      "                               Changed findAllCurrencyRates().
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.INIFileException;
import com.chelseasystems.cr.currency.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * <p>Title: ExchangeRateDownloadFileServices</p>
 *
 * <p>Description: A simple implementation of ExchangeRateServices that manages a
 * non-persistent memory-based database of exchange rate information</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ExchangeRateDownloadFileServices extends CMSExchangeRateServices implements IConfig {
  private INIFile inifile = null;

  /**
   * Default Constructor
   */
  public ExchangeRateDownloadFileServices() {
    ConfigMgr mgr = new ConfigMgr("currency.cfg");
    if (null == mgr) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "Constructor"
          , "Could not create configuration manager.", "Make sure the currency.cfg file exists.", 1);
    }
    try {
      inifile = new INIFile(FileMgr.getLocalFile("currency", "currencyrates.dat"), false);
    } catch (INIFileException ie) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "Constructor"
          , "Initialization failed due to INIFile exception.", "Make sure the currency.cfg file contains an entry with a key of EXCHANGE_RATE_FILENAME that contains the name of a valid INIFile."
          , 1, ie);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "Constructor"
          , "Initialization failed.", "Examine the following exception information", 1, e);
    }
  }

  /**
   * This method is used to find all currency rates
   * @return CurrencyRate[]
   */
  public CurrencyRate[] findAllCurrencyRates() {
    StringBuffer msg;
    try {
      //          Enumeration enm = inifile.getKeys();
      Properties prop = inifile.getProperties();
      Enumeration enm = prop.propertyNames();
      enm = prop.propertyNames();
      ArrayList tempCurRates = new ArrayList();
      ConfigMgr config = new ConfigMgr("currency.cfg");
      while (enm.hasMoreElements()) {
        CurrencyRate curRates = new CurrencyRate();
        String toFromKey = (String)enm.nextElement();
        int seperator = toFromKey.indexOf(".");
        String valWithDateAndCode = inifile.getValue(toFromKey);
        int dateSeperator = valWithDateAndCode.indexOf("|");
        int tenderCodeSeperator = valWithDateAndCode.indexOf("@");
        String conversionRate = valWithDateAndCode.substring(0, dateSeperator);
        String dateString = valWithDateAndCode.substring(dateSeperator, tenderCodeSeperator);
        String tenderCode = valWithDateAndCode.substring(tenderCodeSeperator+1);
        curRates.setConversionRate(new Double(conversionRate));
        Date date = new Date();
        if (dateString != null) {
          try {
            String dateFormat = config.getString("DATE_FORMAT_STRING");
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            df.setLenient(false);
            date = df.parse(dateString);
          } catch (Exception e) {}
        }
        curRates.setFromCurrency(toFromKey.substring(0, seperator));
        curRates.setToCurrency(toFromKey.substring(seperator + 1));
        curRates.setUpdateDate(date);
        curRates.setTenderCode(tenderCode);
        tempCurRates.add(curRates);
      }
      return (CurrencyRate[])tempCurRates.toArray(new CurrencyRate[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  // Returns from the currency.dat file
  public CurrencyRate[] findAllCurrencyRates(String sCountry, String sLanguage) {
    return findAllCurrencyRates();
  }

  /**
   *
   * @param as String[]
   */
  public void processConfigEvent(String as[]) {}

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @return
   * @exception MissingExchangeRateException
   */
  public ArmCurrency getExchangeRate(CurrencyType from, CurrencyType to)
      throws MissingExchangeRateException {
    String conversionRate = null;
    String valWithDate = null;
    double rate = 0.0;
    try {
      valWithDate = inifile.getValue(to.getCode() + "." + from.getCode());
      int dateSeperator = valWithDate.indexOf("|");
      conversionRate = valWithDate.substring(0, dateSeperator);
      rate = new Double(conversionRate).doubleValue();
      return new ArmCurrency(CurrencyType.getCurrencyType(to.getCode()), rate);
    } catch (INIFileException ie) {
      // Try to get the rate with reverse conversion
      try {
        if (valWithDate == null) {
          valWithDate = inifile.getValue(from.getCode() + "." + to.getCode());
        }
        int dateSeperator = valWithDate.indexOf("|");
        conversionRate = valWithDate.substring(0, dateSeperator);
        rate = 1 / new Double(conversionRate).doubleValue();
        return new ArmCurrency(CurrencyType.getCurrencyType(to.getCode()), rate);
      } catch (Exception e) {
        StringBuffer msg = new StringBuffer("Missing exchange rate: ");
        msg.append(from.getCode());
        msg.append(", ");
        msg.append(to.getCode());
        throw new MissingExchangeRateException(msg.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}

