/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	CMSExchangeRateJDBCServices
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import java.util.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cr.currency.MissingExchangeRateException;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: CMSExchangeRateJDBCServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSExchangeRateJDBCServices extends CMSExchangeRateServices {
  private ArmCurrencyRateDAO currencyRateDao;
  static private String counter101 = "101";

  /**
   * Default constructor
   */
  public CMSExchangeRateJDBCServices() {
    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
    currencyRateDao = (ArmCurrencyRateDAO)configMgr.getObject("EXCHANGERATE_DAO");
  }

  /**
   * This method is used to find all currency rates
   * @return CurrencyRate[]
   * @throws Exception
   */
  public CurrencyRate[] findAllCurrencyRates()
      throws Exception {
    try {
      return currencyRateDao.selectAll();
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAllCurrencyRates"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * This method is used to find all currency rates
   * @return CurrencyRate[]
   * @throws Exception
   */
  public CurrencyRate[] findAllCurrencyRates(String sCountry, String sLanguage)
      throws Exception {
    try {
      return currencyRateDao.selectAll(sCountry, sLanguage);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findAllCurrencyRates"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      throw exception;
    }
  }

  /**
   * put your documentation comment here
   * @param from
   * @param to
   * @return
   * @exception MissingExchangeRateException
   */
  public ArmCurrency getExchangeRate(CurrencyType from, CurrencyType to)
      throws MissingExchangeRateException {
    // Not needed to look from database, only used through files services
    return null;
  }
}

