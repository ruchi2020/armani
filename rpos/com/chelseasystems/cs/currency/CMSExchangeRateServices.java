/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	CMSExchangeRateServices
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import com.chelseasystems.cr.config.ConfigMgr;
import java.io.PrintStream;
import com.chelseasystems.cr.currency.*;


/**
 * <p>Title: CMSExchangeRateServices</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public abstract class CMSExchangeRateServices extends ExchangeRateServices {
  private static CMSExchangeRateServices current = null;

  /**
   * This method is used to find all currency rates
   * @throws Exception
   * @return CurrencyRate[]
   */
  public abstract CurrencyRate[] findAllCurrencyRates()
      throws Exception;

  /**
   * This method is used to find all currency rates
   * @throws Exception
   * @return CurrencyRate[]
   */
  public abstract CurrencyRate[] findAllCurrencyRates(String sCountry, String sLanguage)
      throws Exception;
}

